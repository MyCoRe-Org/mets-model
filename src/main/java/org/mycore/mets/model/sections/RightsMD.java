package org.mycore.mets.model.sections;

/**
 * Represents the mets:rightsMD (rights metadata) section of a METS document.
 */
public class RightsMD extends MdWrapSection {

    /**
     * Creates a new RightsMD with the given identifier.
     *
     * @param id the section identifier
     */
    public RightsMD(String id) {
        super(id);
    }

    @Override
    public String getXMLName() {
        return "rightsMD";
    }

}
