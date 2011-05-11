/* $Revision: 3384 $ 
 * $Date: 2011-02-24 14:45:32 +0100 (Do, 24. Feb 2011) $ 
 * $LastChangedBy: matthias $
 * Copyright 2010 - Thüringer Universitäts- und Landesbibliothek Jena
 *  
 * Mets-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mets-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mets-Editor.  If not, see http://www.gnu.org/licenses/.
 */
package org.mycore.mets.model.struct;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 * 
 * @deprecated use IDiv classes instead e.g. LogicalDiv
 */
@Deprecated
public class Div implements IMetsElement {

    public static String TYPE_PHYS_SEQ = "physSequence";

    private String dmdId, amdId, label, contentIds;

    /** the document type e.g. monograph */
    private String documentType;

    private String id, type;

    private int order;

    private List<SubDiv> subDivList;

    /**
     * Use this constructor to create a Div as part of a physical structure map
     * 
     * @param id
     *            the id of the Div
     * @param type
     *            the type of the Div (use {@link Div#TYPE_PHYS_SEQ})
     * 
     * @see {@link PhysicalStructMap} and
     *      {@link PhysicalStructMap#setDivContainer(Div)}
     * */
    public Div(String id, String type) {
        this.id = id;
        this.type = type;
        subDivList = new Vector<SubDiv>();
        dmdId = null;
        amdId = null;
        documentType = null;
        label = null;
        contentIds = null;
        order = -1;
    }

    /**
     * Use this constructor to create a Div as part of a logical structure map.
     * The type field is set to <code>null</code>.
     * 
     * @param id
     *            the id of the Div
     * @param dmdId
     *            the id of a dmd section in the mets document
     * @param admId
     *            the id of a amd section in the mets document
     * @param documentType
     *            the type of the document e.g. monograph
     * @param label
     *            the label of the div, e.g. the title of the book
     * 
     *@see {@link LogicalStructMap} and
     *      {@link LogicalStructMap#setDivContainer(Div)}
     */
    public Div(String id, String dmdId, String admId, String documentType, String label) {
        this(id, null);
        this.dmdId = dmdId;
        this.amdId = admId;
        this.documentType = documentType;
        this.label = label;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Adds a div section to this div
     * 
     * @param div
     *            the {@link SubDiv} to add
     * @return <code>true</code> if the SubDiv was added,<code>false</code>
     *         otherwise
     */
    public boolean addSubDiv(SubDiv div) {
        return this.subDivList.add(div);
    }

    /**
     * Removes a div section from this div
     * 
     * @param div
     *            the {@link SubDiv} to remove
     * @return <code>true</code> if the SubDiv was removed,<code>false</code>
     *         otherwise
     */
    public boolean removeSubDiv(SubDiv div) {
        return this.subDivList.remove(div);
    }

    public List<SubDiv> getSubDivList() {
        return subDivList;
    }

    /**
     * @return the dmdId
     */
    public String getDmdId() {
        return dmdId;
    }

    /**
     * @param dmdId
     *            the dmdId to set
     */
    public void setDmdId(String dmdId) {
        this.dmdId = dmdId;
    }

    /**
     * @return the amdId
     */
    public String getAmdId() {
        return amdId;
    }

    /**
     * @param amdId
     *            the amdId to set
     */
    public void setAmdId(String amdId) {
        this.amdId = amdId;
    }

    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType
     *            the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the contentIds
     */
    public String getContentIds() {
        return contentIds;
    }

    /**
     * @param contentIds
     *            the contentIds to set
     */
    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Use this method to create an object suitable for the constructor of {@link SmLink}
     * 
     * */
    public SubDiv asLogicalSubDiv() {
        return new SubDiv(this.id, this.type, this.order, false);
    }

    public Element asElement() {
        Element div = new Element("div", IMetsElement.METS);
        div.setAttribute("ID", this.getId());
        /* Div is part of a physical structure map */
        if (type != null) {
            div.setAttribute("TYPE", this.getType());
        }
        /* Div is part of a logical structure map */
        else {
            if (this.dmdId != null) {
                div.setAttribute("DMDID", this.getDmdId());
            }
            if (this.amdId != null) {
                div.setAttribute("ADMID", this.getAmdId());
            }
            if (this.documentType != null) {
                div.setAttribute("TYPE", this.getDocumentType());
            }
            if (this.label != null) {
                div.setAttribute("LABEL", this.getLabel());
            }
            if (this.order != -1) {
                div.setAttribute("ORDER", String.valueOf(this.getOrder()));
            }
        }
        Iterator<SubDiv> sbDivIterator = this.subDivList.iterator();
        while (sbDivIterator.hasNext()) {
            div.addContent(sbDivIterator.next().asElement());
        }
        return div;
    }
}