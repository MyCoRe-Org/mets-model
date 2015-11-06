package org.mycore.mets.model.struct;

import org.mycore.mets.model.IMetsElement;

public interface IStructMap extends IMetsElement {

    public static final String XML_NAME = "structMap";
    public static final String XML_TYPE = "TYPE";

    public String getType();

}
