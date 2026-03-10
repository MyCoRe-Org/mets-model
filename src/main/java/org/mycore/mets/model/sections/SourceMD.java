package org.mycore.mets.model.sections;

/**
 * Represents the mets:sourceMD (source metadata) section of a METS document.
 */
public class SourceMD extends MdWrapSection {

    /**
     * Creates a new SourceMD with the given identifier.
     *
     * @param id the section identifier
     */
    public SourceMD(String id) {
        super(id);
    }

    @Override
    public String getXMLName() {
        return "sourceMD";
    }

}
