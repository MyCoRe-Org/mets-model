package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

public abstract class AbstractDiv<T extends IMetsElement> implements IDiv<T> {

    public static final String XML_ORDER = "ORDER";

    public static final String XML_ORDERLABEL = "ORDERLABEL";

    public static final String XML_CONTENTIDS = "CONTENTIDS";

    public final static String XML_ADMID = "ADMID";

    public final static String XML_DMDID = "DMDID";

    public final static String XML_LABEL = "LABEL";

    protected String id, type, label, orderLabel, dmdId, admId, contentIds;

    protected Integer order;

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public void setOrderLabel(String orderLabel) {
        this.orderLabel = orderLabel;
    }

    @Override
    public String getOrderLabel() {
        return orderLabel;
    }

    @Override
    public void setAdmId(String admId) {
        this.admId = admId;
    }

    @Override
    public String getAdmId() {
        return admId;
    }

    @Override
    public void setDmdId(String dmdId) {
        this.dmdId = dmdId;
    }

    @Override
    public String getDmdId() {
        return dmdId;
    }

    @Override
    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }

    @Override
    public String getContentIds() {
        return contentIds;
    }

    @Override
    public Element asElement() {
        Element div = new Element(XML_NAME, IMetsElement.METS);
        if (this.getId() != null && !this.getId().equals("")) {
            div.setAttribute(XML_ID, this.getId());
        }
        if (this.getType() != null && !this.getType().equals("")) {
            div.setAttribute(XML_TYPE, this.getType());
        }
        if (this.getLabel() != null && !this.getLabel().equals("")) {
            div.setAttribute(XML_LABEL, this.getLabel());
        }
        if (this.getOrder() != null && this.getOrder() != -1) {
            div.setAttribute(XML_ORDER, String.valueOf(this.getOrder()));
        }
        if (this.getOrderLabel() != null && !this.getOrderLabel().equals("")) {
            div.setAttribute(XML_ORDERLABEL, this.getOrderLabel());
        }
        if (this.getAdmId() != null && !this.getAdmId().equals("")) {
            div.setAttribute(XML_ADMID, this.getAdmId());
        }
        if (this.getDmdId() != null && !this.getDmdId().equals("")) {
            div.setAttribute(XML_DMDID, this.getDmdId());
        }
        if (this.getContentIds() != null && !this.getContentIds().equals("")) {
            div.setAttribute(XML_CONTENTIDS, this.getContentIds());
        }
        return div;
    }

}
