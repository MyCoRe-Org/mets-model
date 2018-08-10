package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mycore.mets.model.struct.AbstractDiv;
import org.mycore.mets.model.struct.Fptr;
import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.PhysicalDiv;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.PhysicalSubDiv;
import org.mycore.mets.model.struct.SmLink;
import org.mycore.mets.model.struct.StructLink;

/**
 * Generates a struct link object.
 * 
 * @author Matthias Eichner
 */
public abstract class StructLinkGenerator {

    protected boolean failEasy = true;

    /**
     * Should the struct link generation fail if one logical div
     * couldn't be linked explicit with one physical div. If set to
     * true, an exception is thrown when the linking fails. If set
     * to false, the logical div will be linked with the last valid
     * physical div or the root div.
     * 
     * @param failEasy true or false
     */
    public StructLinkGenerator setFailEasy(boolean failEasy) {
        this.failEasy = failEasy;
        return this;
    }

    /**
     * {@link #setFailEasy(boolean)}
     * 
     * @return true or false
     */
    public boolean isFailEasy() {
        return this.failEasy;
    }

    /**
     * Creates the struct link section of the given {@link PhysicalStructMap} and {@link LogicalStructMap}object.
     * 
     * @param physicalStructMap the physical part of the mets
     * @param logicalStructMap the logical part part of the mets
     * @return the new generated struct link
     */
    public StructLink generate(PhysicalStructMap physicalStructMap, LogicalStructMap logicalStructMap) {
        HashMap<String, String> fileIdRef = getFileIdRef(physicalStructMap);
        LogicalDiv logicalRootDiv = logicalStructMap.getDivContainer();
        List<LogicalDiv> logicalDivs = getLogicalDivs(logicalRootDiv);

        Set<String> missingPhysicalRefs = new HashSet<>(fileIdRef.values());
        Map<String, String> invertSmLinkMap = new HashMap<>();

        // go through all logical divs
        String lastFileId = null;
        StructLink structLink = new StructLink();
        for (LogicalDiv div : logicalDivs) {
            Set<String> fileIds = getFileIds(div);
            if (fileIds.isEmpty()) {
                StringBuilder error = new StringBuilder(div.getId());
                if (div.getLabel() != null && !div.getLabel().equals("")) {
                    error.append(" (").append(div.getLabel()).append(")");
                }
                // we fail if we cannot get the correct FILEID
                if (this.failEasy) {
                    throw new StructLinkGenerationException(
                        "Unable to create struct link section because " + error.toString()
                            + " cannot be linked with any physical structure.");
                }
                // we do not fail -> try to get the FILEID in some other way
                if (lastFileId != null) {
                    // get the last file id
                    fileIds.add(lastFileId);
                } else {
                    // link with the first physical div
                    List<PhysicalSubDiv> children = physicalStructMap.getDivContainer().getChildren();
                    if (!children.isEmpty()) {
                        PhysicalSubDiv physicalDiv = children.get(0);
                        String physicalId = physicalDiv.getId();
                        String logicalId = div.getId();
                        missingPhysicalRefs.remove(physicalId);
                        structLink.addSmLink(new SmLink(logicalId, physicalId));
                        invertSmLinkMap.put(physicalId, logicalId);
                    } else {
                        throw new StructLinkGenerationException("Unable to create struct link section because "
                            + error.toString()
                            + " cannot be linked with any physical structure. Also there are no physical <mets:div> elements.");
                    }
                }
            }

            // run through FILEID's and link them with the logical div
            for (String fileId : fileIds) {
                lastFileId = fileId;
                String physicalId = fileIdRef.get(fileId);
                missingPhysicalRefs.remove(physicalId);
                String logicalId = div.getId();
                structLink.addSmLink(new SmLink(logicalId, physicalId));
                invertSmLinkMap.put(physicalId, logicalId);
            }
        }

        // add missing physical divs
        List<String> orderedPhysicals = physicalStructMap.getDivContainer()
            .getChildren()
            .stream()
            .map(AbstractDiv::getId)
            .collect(Collectors.toList());

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
     * Returns a set of file identifiers which are linked with the given logical div. If there are not linked files, an
     * empty set is returned.
     *
     * @param div the logical div
     * @return a list of FILEID's which represent a physical file
     */
    protected abstract Set<String> getFileIds(LogicalDiv div);

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
        HashMap<String, String> map = new HashMap<>();
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
        List<LogicalDiv> divs = new ArrayList<>();
        divs.add(div);
        for (LogicalDiv subDiv : div.getChildren()) {
            divs.addAll(getLogicalDivs(subDiv));
        }
        return divs;
    }

}
