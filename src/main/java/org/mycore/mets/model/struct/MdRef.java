package org.mycore.mets.model.struct;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

public class MdRef implements IMetsElement {

    private LOCTYPE loctype;

    private String mimetype;

    private MDTYPE mdtype;

    private String value;

    private String label;

    public MdRef(LOCTYPE loctype, String mimetype, MDTYPE mdtype, String value) {
        this.loctype = loctype;
        this.mimetype = mimetype;
        this.mdtype = mdtype;
        this.value = value;
    }

    @Override
    public Element asElement() {
        Element mdref = new Element("mdRef", IMetsElement.METS);
        mdref.setText(this.value);
        mdref.setAttribute("LOCTYPE", this.loctype.name());
        mdref.setAttribute("MIMETYPE", this.mimetype);
        mdref.setAttribute("MDTYPE", this.mdtype.name());
        if(label != null)
            mdref.setAttribute("LABEL", label);
        return mdref;
    }

    public LOCTYPE getLoctype() {
        return loctype;
    }

    public void setLoctype(LOCTYPE loctype) {
        this.loctype = loctype;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public MDTYPE getMdtype() {
        return mdtype;
    }

    public void setMdtype(MDTYPE mdtype) {
        this.mdtype = mdtype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
