package org.mycore.mets.misc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mycore.mets.model.struct.LogicalDiv;

/**
 * Default struct link generator implementation. Requires a map of logical div identifiers associated with a list of
 * mets:file identifiers.
 * 
 * <p>logical mets:div/@ID : { mets:file/@ID }*</p>
 *
 * @author Matthias Eichner
 */
public class DefaultStructLinkGenerator extends StructLinkGenerator {

    private Map<String, Set<String>> logicalIdToFileIds;

    public DefaultStructLinkGenerator(Map<String, Set<String>> logicalIdToFileIds) {
        this.logicalIdToFileIds = logicalIdToFileIds;
    }

    @Override
    protected Set<String> getFileIds(LogicalDiv div) {
        Set<String> fileIds = logicalIdToFileIds.get(div.getId());
        return fileIds != null ? fileIds : new HashSet<>();
    }

}
