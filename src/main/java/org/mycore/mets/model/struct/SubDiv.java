/*
 * $Revision: 3271 $ $Date: 2011-01-13 15:06:19 +0100 (Do, 13. Jan 2011) $
 * $LastChangedBy: shermann $ Copyright 2010 - Thüringer Universitäts- und
 * Landesbibliothek Jena
 * 
 * Mets-Editor is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Mets-Editor is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Mets-Editor. If not, see http://www.gnu.org/licenses/.
 */
package org.mycore.mets.model.struct;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 */
public class SubDiv implements IMetsElement {
    public static final String TYPE_PAGE = "page";

    public static final String ID_PREFIX = "phys_";

    private String type, id, label, contentId, orderLabel;

    private int order;

    private List<Fptr> fptrList;

    private boolean withFptr;

    private List<SubDiv> children;

    /**
     * Use this constructur when sub div is part of a div within a physical
     * structure map
     * 
     * @param id
     * @param type
     *            the type of the div, e.g. page, index etc.
     * @param order
     * @param withFptr
     *            pass <code>true</code> if the {@link this#asElement()} should
     *            create mets:fptr elements, thus this div is a div within a div
     *            of type {@link Div#TYPE_PHYS_SEQ}
     */
    public SubDiv(String id, String type, int order, boolean withFptr) {
        this.id = id;
        this.type = type;
        this.order = order;
        this.withFptr = withFptr;
        fptrList = new Vector<Fptr>();
        this.label = null;
        this.contentId = null;
        children = new Vector<SubDiv>();
        this.orderLabel = null;
    }

    /**
     * Use this constructur when sub div is part of a div within a physical
     * structure map.
     * 
     * @param id
     * @param type
     *            the type of the div, e.g. page, index etc.
     * @param order
     * @param label
     * @param contentId
     */
    public SubDiv(String id, String type, int order, String label, String contentId, boolean withFptr) {
        this(id, type, order, withFptr);
        this.label = label;
        this.contentId = contentId;
    }

    /** Use this constructor for Divs within a logical structmap */
    public SubDiv(String id, String type, int order, String label) {
        this(id, type, order, false);
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
     * @param order
     *            the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * Adds a {@link Fptr} to this div
     * 
     * @param fptr
     * @return <code>true</code> if the {@link Fptr} was added,
     *         <code>false</code> otherwise
     */
    public boolean addFptr(Fptr fptr) {
        return this.fptrList.add(fptr);
    }

    /**
     * Removes a {@link Fptr} from this div
     * 
     * @param fptr
     * @return <code>true</code> if the {@link Fptr} was removed,
     *         <code>false</code> otherwise
     */
    public boolean removeFptr(Fptr fptr) {
        return this.fptrList.remove(fptr);
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
        return contentId;
    }

    /**
     * Content id might be an urn or something similar.
     * 
     * @param contentIds
     *            the contentIds to set
     */
    public void setContentIds(String contentIds) {
        this.contentId = contentIds;
    }

    /**
     * @return the orderLabel
     */
    public String getOrderLabel() {
        return orderLabel;
    }

    /**
     * @param orderLabel
     *            the orderLabel to set
     */
    public void setOrderLabel(String orderLabel) {
        this.orderLabel = orderLabel;
    }

    /**
     * @return the withFptr
     */
    public boolean isWithFptr() {
        return withFptr;
    }

    /**
     * @param withFptr
     *            the withFptr to set
     */
    public void setWithFptr(boolean withFptr) {
        this.withFptr = withFptr;
    }

    /**
     * If the this div is a logical div (a div with in logical structure map (
     * {@link LogicalStructMap})) you may add children to this div with that
     * method. Please note there is no validation mechanism, thus you can add a
     * physical div to a logical div, also that making no sense.
     * 
     * @param child
     *            the SubDiv to add
     */
    public boolean addLogicalDiv(SubDiv child) {
        return this.children.add(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.struct.Div#asElement()
     */
    public Element asElement() {
        Element div = new Element("div", IMetsElement.METS);
        div.setAttribute("TYPE", this.type);
        div.setAttribute("ID", this.getId());
        div.setAttribute("ORDER", String.valueOf(this.getOrder()));

        if (this.label != null && label.length() > 0) {
            div.setAttribute("LABEL", this.label);
        }

        if (this.contentId != null && contentId.length() > 0) {
            div.setAttribute("CONTENTIDS", this.contentId);
        }

        if (this.orderLabel != null && orderLabel.length() > 0) {
            div.setAttribute("ORDERLABEL", this.orderLabel);
        }

        Iterator<Fptr> fptrIterator = this.fptrList.iterator();
        if (withFptr) {
            while (fptrIterator.hasNext()) {
                div.addContent(fptrIterator.next().asElement());
            }
        }

        Iterator<SubDiv> childrenIterator = this.children.iterator();
        while (childrenIterator.hasNext()) {
            div.addContent(childrenIterator.next().asElement());
        }

        return div;
    }
}