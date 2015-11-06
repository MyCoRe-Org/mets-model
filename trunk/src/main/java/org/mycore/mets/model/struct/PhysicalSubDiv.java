package org.mycore.mets.model.struct;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.jdom2.Element;

public class PhysicalSubDiv extends AbstractDiv<Fptr> {

    public static final String XML_ORDER = "ORDER";

    public static final String XML_ORDERLABEL = "ORDERLABEL";

    public static final String XML_CONTENTIDS = "CONTENTIDS";

    public static final String XML_LABEL = "LABEL";

    public static final String TYPE_PAGE = "page";

    public static final String ID_PREFIX = "phys_";

    private int order;

    private String orderLabel, label, contentids;

    private HashMap<String, Fptr> filePointers;

    public PhysicalSubDiv(String id, String type, int order) {
        this(id, type, order, null);
    }

    public PhysicalSubDiv(String id, String type, int order, String orderLabel) {
        this.id = id;
        this.type = type;
        this.order = order;
        this.orderLabel = orderLabel;
        this.label = null;
        this.contentids = null;
        this.filePointers = new LinkedHashMap<String, Fptr>();
    }

    public PhysicalSubDiv(String id, String type, int order, String orderLabel, String label) {
        this(id, type, order, orderLabel);
        this.label = label;
    }

    public PhysicalSubDiv(String id, String type, int order, String orderLabel, String label, String contentids) {
        this(id, type, order, orderLabel);
        this.label = label;
        this.contentids = contentids;
    }

    /**
     * Adds a new file pointer.
     * 
     * @param fprt
     *            the {@link Fptr} to add
     */
    @Override
    public void add(Fptr fprt) {
        if (fprt == null) {
            return;
        }
        this.filePointers.put(fprt.getFileId(), fprt);
    }

    @Override
    public void remove(Fptr fprt) {
        if (fprt == null) {
            return;
        }
        this.filePointers.remove(fprt.getFileId());
    }

    /**
     * Removes a {@link Fptr} by its file id.
     */
    public void remove(String fileId) {
        this.filePointers.remove(fileId);
    }

    /**
     * @return all {@link Fptr} in a list
     */
    @Override
    public List<Fptr> getChildren() {
        return new Vector<Fptr>(this.filePointers.values());
    }

    /**
     * Returns the {@link Fptr} with the given id, or null if there is no such
     * Fprt.
     * 
     * @param id
     *            the id of the {@link Fptr}
     * @return the {@link Fptr} with the given id
     */
    public Fptr getFptr(String id) {
        return this.filePointers.get(id);
    }

    /**
     * @return the value of order attribute of this div
     */
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the value of the orderlabel attribute
     */
    public String getOrderLabel() {
        return orderLabel;
    }

    public void setOrderLabel(String orderLabel) {
        this.orderLabel = orderLabel;
    }

    /**
     * @return the label of this div
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param contentids
     *            the contentids to set
     */
    public void setContentids(String contentids) {
        this.contentids = contentids;
    }

    /**
     * @return the contentids
     */
    public String getContentids() {
        return contentids;
    }

    @Override
    public Element asElement() {
        Element div = super.asElement();
        if (this.getOrder() != -1) {
            div.setAttribute(XML_ORDER, String.valueOf(this.getOrder()));
        }
        if (this.getOrderLabel() != null && !this.getOrderLabel().equals("")) {
            div.setAttribute(XML_ORDERLABEL, this.getOrderLabel());
        }
        if (this.getLabel() != null && !this.getLabel().equals("")) {
            div.setAttribute(XML_LABEL, this.getLabel());
        }
        if (this.getContentids() != null && !this.getContentids().equals("")) {
            div.setAttribute(XML_CONTENTIDS, this.getContentids());
        }

        for (Fptr f : filePointers.values()) {
            div.addContent(f.asElement());
        }

        return div;
    }
}