package org.mycore.mets.model.struct;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import org.jdom2.Element;

public class PhysicalSubDiv extends AbstractDiv<Fptr> {

    public static final String XML_ORDERLABEL = "ORDERLABEL";

    public static final String XML_CONTENTIDS = "CONTENTIDS";

    public static final String TYPE_PAGE = "page";

    public static final String ID_PREFIX = "phys_";

    private PhysicalDiv parent;

    private String orderLabel, contentids;

    private HashMap<String, Fptr> filePointers;

    public PhysicalSubDiv(String id, String type) {
        this(id, type, null);
    }

    public PhysicalSubDiv(String id, String type, String orderLabel) {
        this.id = id;
        this.type = type;
        this.orderLabel = orderLabel;
        this.contentids = null;
        this.filePointers = new LinkedHashMap<String, Fptr>();
    }

    public PhysicalSubDiv(String id, String type, String orderLabel, String contentids) {
        this(id, type, orderLabel);
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
     * 
     * @param fileId file identifier to remove
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
     * @return the value of the orderlabel attribute
     */
    public String getOrderLabel() {
        return orderLabel;
    }

    public void setOrderLabel(String orderLabel) {
        this.orderLabel = orderLabel;
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

    /**
     * Sets the parent for this div.
     * 
     * @param parent the parent
     */
    public void setParent(PhysicalDiv parent) {
        this.parent = parent;
    }

    /**
     * Gets the parent of this div
     * 
     * @return the parent div
     */
    public PhysicalDiv getParent() {
        return parent;
    }

    /**
     * Returns the index position of this div in its parent.
     * 
     * @return the index position
     */
    public Optional<Integer> getPositionInParent() {
        if (this.parent == null) {
            return Optional.empty();
        }
        return Optional.of(this.parent.getChildren().indexOf(this));
    }

    @Override
    public Element asElement() {
        Element div = super.asElement();

        if (this.getOrderLabel() != null && !this.getOrderLabel().equals("")) {
            div.setAttribute(XML_ORDERLABEL, this.getOrderLabel());
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