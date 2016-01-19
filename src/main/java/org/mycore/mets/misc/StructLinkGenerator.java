package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mycore.mets.model.Mets;
import org.mycore.mets.model.struct.Area;
import org.mycore.mets.model.struct.Fptr;
import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.PhysicalDiv;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.PhysicalSubDiv;
import org.mycore.mets.model.struct.Seq;
import org.mycore.mets.model.struct.SmLink;
import org.mycore.mets.model.struct.StructLink;

/**
 * Generates the struct link section automatically. Its required that the logical and the
 * physical struct map are set.
 * 
 * @author Matthias Eichner
 */
public class StructLinkGenerator {

    /**
     * Creates the struct link section of the given {@link Mets} object. This does just
     * create the struct link and does not add it to the mets object itself. Do this
     * by calling {@link Mets#setStructLink(StructLink)}.
     * 
     * @param mets the mets object for which you want to create a struct link
     * @return the new generated struct link
     */
    public StructLink generate(Mets mets) {
        PhysicalStructMap psm = (PhysicalStructMap) mets.getStructMap(PhysicalStructMap.TYPE);
        LogicalStructMap lsm = (LogicalStructMap) mets.getStructMap(LogicalStructMap.TYPE);
        HashMap<String, String> fileIdRef = getFileIdRef(psm);
        LogicalDiv logicalRootDiv = lsm.getDivContainer();
        List<LogicalDiv> logicalDivs = getLogicalDivs(logicalRootDiv);

        Set<String> missingPhysicalRefs = new HashSet<String>(fileIdRef.values());
        Map<String, String> invertSmLinkMap = new HashMap<String, String>();

        // go through all logical divs
        StructLink structLink = new StructLink();
        for (LogicalDiv div : logicalDivs) {
            // get all FILEID's of mets:area
            List<String> fileIds = getFileIdsFromArea(div);
            if (fileIds.isEmpty()) {
                String firstFileId = findFirstFileId(div);
                if (firstFileId == null) {
                    throw new RuntimeException("Unable to create struct link section because " + div.getId()
                        + " cannot be linked with any physical structure.");
                }
                fileIds.add(firstFileId);
            }
            // run through FILEID's and link them with the logical div
            for (String fileId : fileIds) {
                String physicalId = fileIdRef.get(fileId);
                missingPhysicalRefs.remove(physicalId);
                String logicalId = div.getId();
                structLink.addSmLink(new SmLink(logicalId, physicalId));
                invertSmLinkMap.put(physicalId, logicalId);
            }
        }

        // add missing physical divs
        List<String> orderedPhysicals = getOrderedPhysicals(psm);
        for (String physicalId : missingPhysicalRefs) {
            String previousPhyiscalId = physicalId;
            String logicalId = null;
            while (logicalId == null) {
                int index = orderedPhysicals.indexOf(previousPhyiscalId) - 1;
                if (index <= -1) {
                    // no previous physical div -> add it to root logical div
                    logicalId = logicalRootDiv.getId();
                } else {
                    previousPhyiscalId = orderedPhysicals.get(index);
                    if (missingPhysicalRefs.contains(previousPhyiscalId)) {
                        continue;
                    }
                    logicalId = invertSmLinkMap.get(previousPhyiscalId);
                }
            }
            structLink.addSmLink(new SmLink(logicalId, physicalId));
        }
        return structLink;
    }

    /**
     * Returns a list of all physical id's ordered by ORDER.
     * 
     * @param psm the pyhsical struct map
     * @return list list of physical id's
     */
    protected List<String> getOrderedPhysicals(PhysicalStructMap psm) {
        List<PhysicalSubDiv> orderedList = new ArrayList<PhysicalSubDiv>();
        PhysicalDiv divContainer = psm.getDivContainer();
        orderedList.addAll(divContainer.getChildren());
        Collections.sort(orderedList, new Comparator<PhysicalSubDiv>() {
            @Override
            public int compare(PhysicalSubDiv p1, PhysicalSubDiv p2) {
                return Integer.compare(p1.getOrder(), p2.getOrder());
            }
        });
        List<String> idList = new ArrayList<String>();
        for (PhysicalSubDiv div : orderedList) {
            idList.add(div.getId());
        }
        return idList;
    }

    /**
     * <p>Returns a map containing all file id references. Each file id is bound
     * to exactly one physical id. Its possible (and likely) that two file ids
     * have the same physical id.</p>
     *
     * <ul>
     * <li>key: fileid</li>
     * <li>value: physical id</li>
     * </ul>
     * 
     * @param psm the physical struct map where to get the file id references
     * @return map of file id references
     */
    protected HashMap<String, String> getFileIdRef(PhysicalStructMap psm) {
        HashMap<String, String> map = new HashMap<String, String>();
        PhysicalDiv divContainer = psm.getDivContainer();
        List<PhysicalSubDiv> divs = divContainer.getChildren();
        for (PhysicalSubDiv div : divs) {
            String id = div.getId();
            for (Fptr fptr : div.getChildren()) {
                map.put(fptr.getFileId(), id);
            }
        }
        return map;
    }

    /**
     * Returns a list of all logical divs.
     * 
     * @param div the div to start
     * @return list of divs
     */
    protected List<LogicalDiv> getLogicalDivs(LogicalDiv div) {
        List<LogicalDiv> divs = new ArrayList<LogicalDiv>();
        divs.add(div);
        for (LogicalDiv subDiv : div.getChildren()) {
            divs.addAll(getLogicalDivs(subDiv));
        }
        return divs;
    }

    /**
     * Finds the first corresponding file id for the given div.
     * 
     * @param div the div where the file id is looked for (runs recursive
     * through the child nodes)
     * 
     * @return id of the first file which is referenced within the div
     */
    protected String findFirstFileId(LogicalDiv div) {
        List<String> fileIds = getFileIdsFromArea(div);
        String fileId = fileIds.isEmpty() ? null : fileIds.get(0);
        if (fileId == null) {
            for (LogicalDiv subDiv : div.getChildren()) {
                fileId = findFirstFileId(subDiv);
                if (fileId != null) {
                    break;
                }
            }
        }
        return fileId;
    }

    /**
     * Returns the first file id found in the fptr -&gt; seq -&gt; area section
     * of the div. No sub div's are searched!
     * 
     * @param div the div to get the first file id from
     * @return file id or null
     */
    protected List<String> getFileIdsFromArea(LogicalDiv div) {
        List<String> fileIds = new ArrayList<>();
        for (Fptr fptr : div.getFptrList()) {
            for (Seq seq : fptr.getSeqList()) {
                for (Area area : seq.getAreaList()) {
                    fileIds.add(area.getFileId());
                }
            }
        }
        return fileIds;
    }

}
