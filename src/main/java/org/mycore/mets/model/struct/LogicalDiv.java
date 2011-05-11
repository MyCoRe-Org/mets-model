package org.mycore.mets.model.struct;

import org.jdom.Element;

/**
 * 
 *
 * @author Matthias Eichner
 */
public class LogicalDiv extends AbstractLogicalDiv {
    
    public final static String XML_AMDID = "AMDID";
    public final static String XML_DMDID = "DMDID";

    protected String dmdId, amdId;

    public LogicalDiv(String id, String type, String label) {
        this(id, type, label, -1, null, null);
    }
    public LogicalDiv(String id, String type, String label, int order) {
        this(id, type, label, order, null, null);
    }

    public LogicalDiv(String id, String type, String label, int order, String amdId, String dmdId) {
        super(id, type, label, order);
        this.setAmdId(amdId);
        this.setDmdId(dmdId);
    }

    public void setAmdId(String amdId) {
        this.amdId = amdId;
    }
    public void setDmdId(String dmdId) {
        this.dmdId = dmdId;
    }
    public String getAmdId() {
        return amdId;
    }
    public String getDmdId() {
        return dmdId;
    }

    @Override
    public Element asElement() {
        Element div = super.asElement();
        if(this.getAmdId() != null && !this.getAmdId().equals("")) {
            div.setAttribute(XML_AMDID, this.getAmdId());
        }
        if(this.getDmdId() != null && !this.getDmdId().equals("")) {
            div.setAttribute(XML_DMDID, this.getDmdId());
        }
        return div;
    }
}
