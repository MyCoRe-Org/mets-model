package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mycore.mets.model.struct.Area;
import org.mycore.mets.model.struct.Fptr;
import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.Seq;

/**
 * StructLinkGenerator implementation which can create the struct link section out of the box. It's required that the
 * logical div section uses area elements referencing the physical structure.
 *
 * @author Matthias Eichner
 */
public class AreaStructLinkGenerator extends StructLinkGenerator {

    @Override
    protected Set<String> getFileIds(LogicalDiv div) {
        List<String> fileIds = getFileIdsFromArea(div);
        if (fileIds.isEmpty()) {
            String firstFileId = findFirstFileId(div);
            if (firstFileId == null) {
                return new HashSet<>(fileIds);
            }
            fileIds.add(firstFileId);
        }
        return new HashSet<>(fileIds);
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

}
