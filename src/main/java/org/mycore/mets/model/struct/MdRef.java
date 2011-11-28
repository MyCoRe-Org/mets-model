package org.mycore.mets.model.struct;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author shermann
 */
public class MdRef implements IMetsElement {

    private LOCTYPE loctype;

    private String mimetype;

    private MDTYPE mdtype;

    private String value;

    private String href;

    /**
     * @param loctype
     * @param mimetype
     * @param mdtype
     * @param value
     */
    public MdRef(LOCTYPE loctype, String mimetype, MDTYPE mdtype, String value) {
        this.loctype = loctype;
        this.mimetype = mimetype;
        this.mdtype = mdtype;
        this.value = value;
    }

    /**
     * @param href
     * @param loctype
     * @param mimetype
     * @param mdtype
     * @param value
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

        if (this.value != null && value.length() > 0) {
            mdref.setText(this.value);
        }

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

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href
     *            the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }
}
