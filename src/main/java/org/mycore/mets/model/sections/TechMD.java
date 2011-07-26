package org.mycore.mets.model.sections;

public class TechMD extends MdWrapSection {

    public TechMD(String id) {
        super(id);
    }

    @Override
    public String getXMLName() {
        return "techMD";
    }

}
