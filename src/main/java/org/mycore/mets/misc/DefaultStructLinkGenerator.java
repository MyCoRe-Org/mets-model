package org.mycore.mets.misc;

import java.util.List;
import java.util.Map;

import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.PhysicalSubDiv;

/**
 * Default struct link generator implementation. Requires a map of logical divs associated with a list of
 * physical divs..
 * 
 * <p>logical mets:div : { physical mets:div }*</p>
 *
 * @author Matthias Eichner
 */
public class DefaultStructLinkGenerator extends StructLinkGenerator {

    private Map<LogicalDiv, List<PhysicalSubDiv>> logicalToPhysical;

    public DefaultStructLinkGenerator(Map<LogicalDiv, List<PhysicalSubDiv>> logicalToPhysical) {
        super();
        this.logicalToPhysical = logicalToPhysical;
    }

    @Override
    protected Map<LogicalDiv, List<PhysicalSubDiv>> getLinkedMap(PhysicalStructMap physicalStructMap,
        LogicalStructMap logicalStructMap) {
        return this.logicalToPhysical;
    }

}
