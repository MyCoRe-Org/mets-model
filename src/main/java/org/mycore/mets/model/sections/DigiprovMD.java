package org.mycore.mets.model.sections;

/**
 * Represents the mets:digiprovMD (digital provenance metadata) section of a METS document.
 */
public class DigiprovMD extends MdWrapSection {

    /**
     * Creates a new DigiprovMD with the given identifier.
     *
     * @param id the section identifier
     */
    public DigiprovMD(String id) {
        super(id);
    }

    @Override
    public String getXMLName() {
        return "digiprovMD";
    }

}
