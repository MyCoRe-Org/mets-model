package org.mycore.mets.model.header;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * 
 * The alternative record identifier element &lt;altRecordID&gt; 
 * allows one to use alternative record identifier values 
 * for the digital object represented by the METS document;
 * the primary record identifier is stored in the OBJID 
 * attribute in the root &lt;mets&gt; element.
 * 
 * @see <a href=
 *      "http://www.loc.gov/standards/mets/docs/mets.v1-8.html#altRecordID">altRecordID</a>
 * 
 * @author Uwe Hartwig (M3ssman)
 *
 */
public class AltRecordID implements IMetsElement {

    private String id;

    private String type;

    /**
     * Creates a new AltRecordID instance.
     */
    public AltRecordID() {
    }

    /**
     * Returns the alternative record identifier value.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the alternative record identifier value.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the type attribute of this alternative record identifier.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type attribute of this alternative record identifier.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    @SuppressWarnings("exports")
    @Override
    public Element asElement() {
        Element altRecord = new Element("altRecordID", IMetsElement.METS);
        if (getId() != null) {
            altRecord.setAttribute("ID", getId());
        }
        if (getType() != null) {
            altRecord.setAttribute("TYPE", this.getType());
        }
        return altRecord;
    }
}
