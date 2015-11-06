package org.mycore.mets.model.sections;

public class SourceMD extends MdWrapSection {

    public SourceMD(String id) {
        super(id);
    }

    @Override
    public String getXMLName() {
        return "sourceMD";
    }

}
