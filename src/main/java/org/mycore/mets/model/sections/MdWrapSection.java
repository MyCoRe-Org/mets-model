package org.mycore.mets.model.sections;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;
import org.mycore.mets.model.struct.MdRef;
import org.mycore.mets.model.struct.MdWrap;

/**
 * Abstract class for all md sections containing metadata.
 * 
 * @author Matthias Eichner
 */
public abstract class MdWrapSection extends MdSection {

    private MdRef mdRef;

    private MdWrap mdWrap;

    public MdWrapSection(String id) {
        super(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element wrapSection = new Element(getXMLName(), IMetsElement.METS);
        wrapSection.setAttribute("ID", getId());
        if (mdRef != null) {
            wrapSection.addContent(mdRef.asElement());
        }
        if (mdWrap != null) {
            wrapSection.addContent(mdWrap.asElement());
        }
        return wrapSection;
    }

    public MdRef getMdRef() {
        return mdRef;
    }

    public void setMdRef(MdRef mdRef) {
        this.mdRef = mdRef;
    }

    public MdWrap getMdWrap() {
        return mdWrap;
    }

    public void setMdWrap(MdWrap mdWrap) {
        this.mdWrap = mdWrap;
    }

    public abstract String getXMLName();
}
