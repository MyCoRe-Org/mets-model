package org.mycore.mets.model.sections;

public class DigiprovMD extends MdWrapSection {

    public DigiprovMD(String id) {
        super(id);
    }

    @Override
    public String getXMLName() {
        return "digiprovMD";
    }

}
