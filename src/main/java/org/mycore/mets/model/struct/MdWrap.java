package org.mycore.mets.model.struct;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * An mdWrap element provides a wrapper around metadata embedded within a METS document.
 * 
 * @author Matthias Eichner
 */
public class MdWrap implements IMetsElement {

    private static final String MIMETYPE_XML = "text/xml";

    private MDTYPE mdtype;

    private String mimetype;

    private String label;

    private Element metadata;

    private String bindata;

    private String othermdtype;

    /**
     * To wrap xml data.
     * 
     * @param mdtype
     * @param metadata
     */
    public MdWrap(MDTYPE mdtype, Element metadata) {
        this.mdtype = mdtype;
        this.mimetype = MIMETYPE_XML;
        this.metadata = metadata;
    }

    /**
     * To wrap bin data.
     * 
     * @param mdtype
     * @param binData
     *            any arbitrary binary or textual form, PROVIDED that the metadata is Base64 encoded
     * @param mimetype
     */
    public MdWrap(MDTYPE mdtype, String bindata, String mimetype) {
        this.mdtype = mdtype;
        this.bindata = bindata;
        this.mimetype = mimetype;
    }

    @Override
    public Element asElement() {
        Element mdwrap = new Element("mdWrap", IMetsElement.METS);
        mdwrap.setAttribute("MIMETYPE", this.mimetype);
        mdwrap.setAttribute("MDTYPE", this.mdtype.name());
        if (this.label != null) {
            mdwrap.setAttribute("LABEL", this.label);
        }
        if (mimetype.equals(MIMETYPE_XML)) {
            Element xmlData = new Element("xmlData", IMetsElement.METS);
            xmlData.addContent(this.metadata);
            mdwrap.addContent(xmlData);
        } else {
            Element binData = new Element("binData", IMetsElement.METS);
            binData.setText(this.bindata);
            mdwrap.addContent(binData);
        }
        if(mdtype.equals(MDTYPE.OTHER) && othermdtype != null) {
            mdwrap.setAttribute("OTHERMDTYPE", this.othermdtype);
        }
        return mdwrap;
    }

    public MDTYPE getMdtype() {
        return mdtype;
    }

    public void setMdtype(MDTYPE mdtype) {
        this.mdtype = mdtype;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getOthermdtype() {
        return othermdtype;
    }
    
    public void setOthermdtype(String othermdtype) {
        this.othermdtype = othermdtype;
    }

}
