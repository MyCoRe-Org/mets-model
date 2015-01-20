package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        StructLink structLink = mets.getStructLink();
        for (AbstractLogicalDiv div : logicalDivs) {
            String fileId = findFirstFileId(div);
            String physicalId = fileIdRef.get(fileId);
            String logicalId = div.getId();
            structLink.addSmLink(new SmLink(logicalId, physicalId));
        }
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
