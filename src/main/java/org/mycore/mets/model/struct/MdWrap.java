package org.mycore.mets.model.struct;

import org.jdom2.Element;
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
     * Creates a new MdWrap for XML metadata with the given metadata type and element.
     *
     * @param mdtype   the metadata type
     * @param metadata the XML metadata element to wrap
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
     *            the mdtype
     * @param bindata
     *            any arbitrary binary or textual form, PROVIDED that the
     *            metadata is Base64 encoded
     * @param mimetype
     *            the mimetype
     */
    public MdWrap(MDTYPE mdtype, String bindata, String mimetype) {
        this.mdtype = mdtype;
        this.bindata = bindata;
        this.mimetype = mimetype;
    }

    @Override
    public Element asElement() {
        // create the element to return
        Element mdwrap = new Element("mdWrap", IMetsElement.METS);
        mdwrap.setAttribute("MIMETYPE", this.mimetype);
        mdwrap.setAttribute("MDTYPE", this.mdtype.name());
        if (this.label != null) {
            mdwrap.setAttribute("LABEL", this.label);
        }
        if (mimetype.equals(MIMETYPE_XML)) {
            Element xmlData = new Element("xmlData", IMetsElement.METS);
            xmlData.addContent(this.metadata.clone());
            mdwrap.addContent(xmlData);
        } else {
            Element binData = new Element("binData", IMetsElement.METS);
            binData.setText(this.bindata);
            mdwrap.addContent(binData);
        }
        if (mdtype.equals(MDTYPE.OTHER) && othermdtype != null) {
            mdwrap.setAttribute("OTHERMDTYPE", this.othermdtype);
        }
        return mdwrap;
    }

    /**
     * Returns the metadata type.
     *
     * @return the mdtype
     */
    public MDTYPE getMdtype() {
        return mdtype;
    }

    /**
     * Sets the metadata type.
     *
     * @param mdtype the mdtype to set
     */
    public void setMdtype(MDTYPE mdtype) {
        this.mdtype = mdtype;
    }

    /**
     * Returns the MIME type of the wrapped metadata.
     *
     * @return the mimetype
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * Sets the MIME type of the wrapped metadata.
     *
     * @param mimetype the mimetype to set
     */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    /**
     * Returns the label attribute value.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label attribute value.
     *
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the OTHERMDTYPE attribute value.
     *
     * @return the othermdtype
     */
    public String getOthermdtype() {
        return othermdtype;
    }

    /**
     * Returns the wrapped XML metadata element.
     *
     * @return the metadata element
     */
    public Element getMetadata() {
        return metadata;
    }

    /**
     * Sets the OTHERMDTYPE attribute. As a side effect: if a non null argument
     * is passed the {@link #setMdtype(MDTYPE)} will be set to {@link MDTYPE#OTHER}.
     * 
     * @param otherMdType
     *            the OTHERMDTYPE attribute to set
     */
    public void setOtherMdType(String otherMdType) {
        if (otherMdType != null && !otherMdType.isEmpty()) {
            this.othermdtype = otherMdType;
            this.mdtype = MDTYPE.OTHER;
        }
    }
}
