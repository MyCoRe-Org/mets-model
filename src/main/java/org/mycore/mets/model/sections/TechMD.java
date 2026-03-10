package org.mycore.mets.model.sections;

/**
 * Represents the mets:techMD (technical metadata) section of a METS document.
 */
public class TechMD extends MdWrapSection {

    /**
     * Creates a new TechMD with the given identifier.
     *
     * @param id the section identifier
     */
    public TechMD(String id) {
        super(id);
    }

    @Override
    public String getXMLName() {
        return "techMD";
    }

}
