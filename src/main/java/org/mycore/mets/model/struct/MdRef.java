package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Represents a mets:mdRef element that references external metadata via a URI.
 *
 * @author shermann
 */
public class MdRef implements IMetsElement {

    private LOCTYPE loctype;

    private String mimetype;

    private MDTYPE mdtype;

    private String value;

    private String href;

    /**
     * Creates a new MdRef with the given location type, MIME type, metadata type, and text value.
     *
     * @param loctype  the location type
     * @param mimetype the MIME type of the referenced metadata
     * @param mdtype   the metadata type
     * @param value    the text value of the mdRef element
     */
    public MdRef(LOCTYPE loctype, String mimetype, MDTYPE mdtype, String value) {
        this.loctype = loctype;
        this.mimetype = mimetype;
        this.mdtype = mdtype;
        this.value = value;
    }

    /**
     * Creates a new MdRef with all attributes including an href reference.
     *
     * @param href     the xlink:href attribute value
     * @param loctype  the location type
     * @param mimetype the MIME type of the referenced metadata
     * @param mdtype   the metadata type
     * @param value    the text value of the mdRef element
     */
    public MdRef(String href, LOCTYPE loctype, String mimetype, MDTYPE mdtype, String value) {
        this(loctype, mimetype, mdtype, value);
        this.href = href;
    }

    @Override
    public Element asElement() {
        Element mdref = new Element("mdRef", IMetsElement.METS);
        mdref.setAttribute("href", this.href, IMetsElement.XLINK);
        mdref.setAttribute("LOCTYPE", this.loctype.name());
        mdref.setAttribute("MIMETYPE", this.mimetype);
        mdref.setAttribute("MDTYPE", this.mdtype.name());

        if (this.value != null && !value.isEmpty()) {
            mdref.setText(this.value);
        }

        return mdref;
    }

    /**
     * Returns the location type of this metadata reference.
     *
     * @return the loctype
     */
    public LOCTYPE getLoctype() {
        return loctype;
    }

    /**
     * Sets the location type of this metadata reference.
     *
     * @param loctype the loctype to set
     */
    public void setLoctype(LOCTYPE loctype) {
        this.loctype = loctype;
    }

    /**
     * Returns the MIME type of the referenced metadata.
     *
     * @return the mimetype
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * Sets the MIME type of the referenced metadata.
     *
     * @param mimetype the mimetype to set
     */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
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
     * Returns the text value of this mdRef element.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the text value of this mdRef element.
     *
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the xlink:href attribute value.
     *
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the xlink:href attribute value.
     *
     * @param href
     *            the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }
}
