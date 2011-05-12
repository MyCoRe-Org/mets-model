package org.mycore.mets.model.struct;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class PhysicalSubDiv extends AbstractDiv<Fptr> {

    public static final String XML_ORDER = "ORDER";

    public static final String XML_ORDERLABEL = "ORDERLABEL";

    public static final String XML_LABEL = "LABEL";

    public static final String TYPE_PAGE = "page";

    public static final String ID_PREFIX = "phys_";

    private int order;

    private String orderLabel, label;

    private Fptr fprt;

    public PhysicalSubDiv(String id, String type, int order) {
        this(id, type, order, null);
    }

    public PhysicalSubDiv(String id, String type, int order, String orderLabel) {
        this.id = id;
        this.type = type;
        this.order = order;
        this.orderLabel = orderLabel;
        this.label = null;
    }

    public PhysicalSubDiv(String id, String type, int order, String orderLabel, String label) {
        this(id, type, order, orderLabel);
        this.label = label;
    }

    /**
     * Adds a new file pointer. Only one is allowed.
     */
    @Override
    public void add(Fptr fprt) {
        this.fprt = fprt;
    }

    @Override
    public void remove(Fptr element) {
        if (this.fprt != null && this.fprt.equals(element)) {
            this.fprt = null;
        }
    }

    @Override
    public List<Fptr> getChildren() {
        ArrayList<Fptr> list = new ArrayList<Fptr>();
        if (this.fprt != null)
            list.add(fprt);
        return list;
    }

    public Fptr getFprt() {
        return fprt;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getOrderLabel() {
        return orderLabel;
    }

    public void setOrderLabel(String orderLabel) {
        this.orderLabel = orderLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        if (this.fprt != null) {
            div.addContent(this.fprt.asElement());
        }
        return div;
    }

}
