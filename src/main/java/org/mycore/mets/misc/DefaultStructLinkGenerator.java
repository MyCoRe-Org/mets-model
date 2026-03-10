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

    private final Map<LogicalDiv, List<PhysicalSubDiv>> logicalToPhysical;

    /**
     * Creates a new DefaultStructLinkGenerator with the given logical-to-physical mapping.
     *
     * @param logicalToPhysical a map of logical divs to their associated physical sub-divs
     */
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
