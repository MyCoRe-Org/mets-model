package org.mycore.mets.model.struct;

import org.mycore.mets.model.IMetsElement;

/**
 * Interface for METS structural map elements (mets:structMap).
 */
public interface IStructMap extends IMetsElement {

    /** XML element name for the structMap element. */
    public static final String XML_NAME = "structMap";

    /** XML attribute name for the TYPE attribute. */
    public static final String XML_TYPE = "TYPE";

    /**
     * Returns the type of this structural map (e.g., LOGICAL or PHYSICAL).
     *
     * @return the TYPE attribute value
     */
    String getType();

}
