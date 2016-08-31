package org.mycore.mets.model.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import org.jdom2.Element;

/**
 * @author Matthias Eichner
 */
public class LogicalDiv extends AbstractDiv<LogicalDiv> {

    public final static String XML_AMDID = "ADMID";

    public final static String XML_DMDID = "DMDID";

    public final static String XML_LABEL = "LABEL";

    protected String label;

    protected HashMap<String, LogicalDiv> subDivContainer;

    protected List<Fptr> fptrList;

    private LogicalDiv parent;

    protected String dmdId, amdId;

    private Mtpr mtpr;

    /**
     * @param id
     *            the id of the div
     * @param type
     *            the type attribute
     * @param label
     *            the label of the div
     */
    public LogicalDiv(String id, String type, String label) {
        this.subDivContainer = new LinkedHashMap<String, LogicalDiv>();
        this.fptrList = new ArrayList<Fptr>();
        this.setId(id);
        this.setType(type);
        this.setLabel(label);
    }

    public LogicalDiv(String id, String type, String label,  String amdId, String dmdId) {
        this(id, type, label);
        this.setAmdId(amdId);
        this.setDmdId(dmdId);
    }

    @Override
    public void add(LogicalDiv child) {
        if (child == null) {
            return;
        }
        child.setParent(this);
        this.subDivContainer.put(child.getId(), child);
    }

    @Override
    public void remove(LogicalDiv divToDelete) {
        for (LogicalDiv lsd : subDivContainer.values().toArray(new LogicalDiv[0])) {
            if (lsd == divToDelete) {
                this.subDivContainer.remove(lsd.getId());
                lsd.setParent(null);
                return;
            } else {
                removeFromChildren(lsd.getChildren(), divToDelete);
            }
        }
    }

    private void removeFromChildren(List<LogicalDiv> children, LogicalDiv divToDelete) {
        for (LogicalDiv child : children) {
            if (child == divToDelete) {
                child.getParent().remove(divToDelete.getId());
                child.setParent(null);
                return;
            } else {
                removeFromChildren(child.getChildren(), divToDelete);
            }
        }
    }

    /**
     * Removes the div from this logical div
     * 
     * @param identifier
     *            the identifier
     */
    public void remove(String identifier) {
        if (identifier == null) {
            return;
        }
        subDivContainer.remove(identifier);
    }

    public List<LogicalDiv> getChildren() {
        return new Vector<LogicalDiv>(subDivContainer.values());
    }

    protected void setParent(LogicalDiv parentToSet) {
        this.parent = parentToSet;
    }

    /**
     * Returns the index position of this div in its parent.
     * 
     * @return the index position
     */
    public Optional<Integer> getPositionInParent() {
        if(this.parent == null) {
            return Optional.empty();
        }
        return Optional.of(this.parent.getChildren().indexOf(this));
    }

    /**
     * @return the parent of this div
     */
    public LogicalDiv getParent() {
        return this.parent;
    }

    /**
     * Returns a modifiable list of file pointers.
     * 
     * @return list of file pointers
     */
    public List<Fptr> getFptrList() {
        return fptrList;
    }

    /**
     * Returns a {@link LogicalDiv} with the given id. This checks
     * all descendants.
     * 
     * @param identifier ID attribute to find  
     * @return a {@link LogicalDiv} with the given id or null
     */
    public LogicalDiv getLogicalSubDiv(String identifier) {
        for (LogicalDiv child : subDivContainer.values()) {
            if (child.getId().equals(identifier)) {
                return  child;
            } else {
                LogicalDiv logicalSubDiv = lookupChildren(child.getChildren(), identifier);
                if (logicalSubDiv != null) {
                    return logicalSubDiv;
                }
            }
        }
        return null;
    }

    private LogicalDiv lookupChildren(List<LogicalDiv> children, String identifier) {
        for (LogicalDiv child : children) {
            if (child.getId().equals(identifier)) {
                return child;
            } else {
                lookupChildren(child.getChildren(), identifier);
            }
        }
        return null;
    }

    /**
     * Returns a list of all descendant div's. Be aware that there
     * is no specific order.
     * 
     * @return list of descendants
     */
    public List<LogicalDiv> getDescendants() {
        List<LogicalDiv> descendants = new ArrayList<>();
        for(LogicalDiv subDiv : subDivContainer.values()) {
            descendants.add(subDiv);
            descendants.addAll(subDiv.getDescendants());
        }
        return descendants;
    }

    /**
     * Sets the label attribute.
     * 
     * @param label label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.id;
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

    public void setMtpr(Mtpr mtpr) {
        this.mtpr = mtpr;
    }

    public Mtpr getMtpr() {
        return this.mtpr;
    }

    @Override
    public Element asElement() {
        Element div = super.asElement();
        if (this.getLabel() != null && !this.getLabel().equals("")) {
            div.setAttribute(XML_LABEL, this.getLabel());
        }

        Iterator<LogicalDiv> sbDivIterator = this.subDivContainer.values().iterator();
        while (sbDivIterator.hasNext()) {
            div.addContent(sbDivIterator.next().asElement());
        }
        for (Fptr fptr : getFptrList()) {
            div.addContent(fptr.asElement());
        }
        if (this.getAmdId() != null && !this.getAmdId().equals("")) {
            div.setAttribute(XML_AMDID, this.getAmdId());
        }
        if (this.getDmdId() != null && !this.getDmdId().equals("")) {
            div.setAttribute(XML_DMDID, this.getDmdId());
        }
        if (mtpr != null) {
            div.addContent(mtpr.asElement());
        }
        return div;
    }
}
