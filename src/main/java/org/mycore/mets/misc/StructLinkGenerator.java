package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mycore.mets.model.Mets;
import org.mycore.mets.model.struct.AbstractLogicalDiv;
import org.mycore.mets.model.struct.Area;
import org.mycore.mets.model.struct.Fptr;
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
 *
 */
public class StructLinkGenerator {

    /**
     * Creates the struct link section of the given {@link Mets} object. 
     * 
     * @param mets
     */
    public void generate(Mets mets) {
        PhysicalStructMap psm = (PhysicalStructMap) mets.getStructMap(PhysicalStructMap.TYPE);
        LogicalStructMap lsm = (LogicalStructMap) mets.getStructMap(LogicalStructMap.TYPE);
        HashMap<String, String> fileIdRef = getFileIdRef(psm);
        List<AbstractLogicalDiv> logicalDivs = getLogicalDivs(lsm.getDivContainer());

        List<String> missingPhysicalRefs = new ArrayList<String>(fileIdRef.values());
        Map<String, String> invertSmLinkMap = new HashMap<String, String>();

        // go through all logical divs
        StructLink structLink = mets.getStructLink();
        for (AbstractLogicalDiv div : logicalDivs) {
            String fileId = findFirstFileId(div);
            String physicalId = fileIdRef.get(fileId);
            missingPhysicalRefs.remove(physicalId);
            String logicalId = div.getId();
            structLink.addSmLink(new SmLink(logicalId, physicalId));
            invertSmLinkMap.put(physicalId, logicalId);
        }

        // add missing physical divs
        List<String> orderedPhysicals = getOrderedPhysicals(psm);
        for (String physicalId : missingPhysicalRefs) {
            String previousPhyiscalId = physicalId;
            String logicalId = null;
            while (logicalId == null) {
                int index = orderedPhysicals.indexOf(previousPhyiscalId) - 1;
                if (index <= -1) {
                    throw new RuntimeException("unable to find referenced logical id for phyiscal id " + physicalId);
                }
                previousPhyiscalId = orderedPhysicals.get(index);
                if (missingPhysicalRefs.contains(previousPhyiscalId)) {
                    continue;
                }
                logicalId = invertSmLinkMap.get(previousPhyiscalId);
            }
            structLink.addSmLink(new SmLink(logicalId, physicalId));
        }
        // TODO: sort the struct links by logical or physical id (call addSmLink later)
    }

    /**
     * Returns a list of all physical id's ordered by ORDER.
     * 
     * @return list
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
     * <li>key: fileid</li>
     * <li>value: physical id</li>
     * 
     * @param psm
     * @return
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
    protected List<AbstractLogicalDiv> getLogicalDivs(AbstractLogicalDiv div) {
        List<AbstractLogicalDiv> divs = new ArrayList<AbstractLogicalDiv>();
        divs.add(div);
        for (AbstractLogicalDiv subDiv : div.getChildren()) {
            divs.addAll(getLogicalDivs(subDiv));
        }
        return divs;
    }

    /**
     * Finds the first corresponding file id for the given div.
     * 
     * @param div
     * @return
     */
    protected String findFirstFileId(AbstractLogicalDiv div) {
        String fileId = getFileIdFromArea(div);
        if (fileId == null) {
            for (AbstractLogicalDiv subDiv : div.getChildren()) {
                fileId = findFirstFileId(subDiv);
                if (fileId != null) {
                    break;
                }
            }
        }
        return fileId;
    }

    /**
     * Returns the first file id found in the fptr -> seq -> area section
     * of the div. No sub div's are searched!
     * 
     * @param div the div to get the first file id from
     * @return file id or null
     */
    protected String getFileIdFromArea(AbstractLogicalDiv div) {
        for (Fptr fptr : div.getFptrList()) {
            for (Seq seq : fptr.getSeqList()) {
                for (Area area : seq.getAreaList()) {
                    return area.getFileId();
                }
            }
        }
        return null;
    }

}