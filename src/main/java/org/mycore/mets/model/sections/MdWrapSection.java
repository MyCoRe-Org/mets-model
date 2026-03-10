package org.mycore.mets.model.sections;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;
import org.mycore.mets.model.struct.MDTYPE;
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

    /**
     * Creates a new MdWrapSection with the given identifier.
     *
     * @param id the section identifier
     */
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

    /**
     * Returns the mdRef element of this section.
     *
     * @return the mdRef or null if not set
     */
    public MdRef getMdRef() {
        return mdRef;
    }

    /**
     * Sets the mdRef element of this section.
     *
     * @param mdRef the mdRef to set
     */
    public void setMdRef(MdRef mdRef) {
        this.mdRef = mdRef;
    }

    /**
     * Returns the mdWrap element of this section.
     *
     * @return the mdWrap or null if not set
     */
    public MdWrap getMdWrap() {
        return mdWrap;
    }

    /**
     * Sets the mdWrap element of this section.
     *
     * @param mdWrap the mdWrap to set
     */
    public void setMdWrap(MdWrap mdWrap) {
        this.mdWrap = mdWrap;
    }

    /**
     * Returns the enum constant of the specified enum type with the specified
     * name. The name must match exactly an identifier used to declare an enum
     * constant in this type. (Extraneous whitespace characters are not
     * permitted.)
     * 
     * @param name
     *              name of the mdtype
     * @return {@link MDTYPE#OTHER} if no other matching MDTYPE could be found
     */
    public static MDTYPE findTypeByName(String name) {
        return MDTYPE.valueOf(name);
    }

    /**
     * Returns the XML element name for this metadata section.
     *
     * @return the XML element name
     */
    public abstract String getXMLName();
}
