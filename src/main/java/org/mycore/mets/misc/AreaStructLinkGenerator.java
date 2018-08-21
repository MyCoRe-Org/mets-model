package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mycore.mets.model.struct.Area;
import org.mycore.mets.model.struct.Fptr;
import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.PhysicalDiv;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.PhysicalSubDiv;
import org.mycore.mets.model.struct.Seq;

/**
 * StructLinkGenerator implementation which can create the struct link section out of the box. It's required that the
 * logical div section uses area elements referencing the physical structure.
 *
 * @author Matthias Eichner
 */
public class AreaStructLinkGenerator extends StructLinkGenerator {

    @Override
    protected Map<LogicalDiv, List<PhysicalSubDiv>> getLinkedMap(PhysicalStructMap physicalStructMap,
        LogicalStructMap logicalStructMap) {
        Map<LogicalDiv, List<PhysicalSubDiv>> linkedMap = new HashMap<>();
        addLink(logicalStructMap.getDivContainer(), physicalStructMap, linkedMap);
        for (LogicalDiv logicalDiv : logicalStructMap.getDivContainer().getDescendants()) {
            addLink(logicalDiv, physicalStructMap, linkedMap);
        }
        return linkedMap;
    }

    protected void addLink(LogicalDiv logicalDiv, PhysicalStructMap physicalStructMap,
        Map<LogicalDiv, List<PhysicalSubDiv>> linkedMap) {
        Set<String> fileIds = getFileIdsFromArea(logicalDiv);
        List<PhysicalSubDiv> physicalDivs = new ArrayList<>();
        PhysicalDiv divContainer = physicalStructMap.getDivContainer();
        for (String fileId : fileIds) {
            PhysicalSubDiv physicalDiv = divContainer.byFileId(fileId);
            if (physicalDiv != null) {
                physicalDivs.add(physicalDiv);
            }
        }
        sortPhysicalDivs(divContainer, physicalDivs);
        linkedMap.put(logicalDiv, physicalDivs);
    }

    /**
     * Returns the file ids found in the fptr -&gt; seq -&gt; area section
     * of the logical div. No sub div's are searched!
     *
     * @param div the logical div
     * @return set of file ids or an empty set
     */
    protected Set<String> getFileIdsFromArea(LogicalDiv div) {
        Set<String> fileIds = new HashSet<>();
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
