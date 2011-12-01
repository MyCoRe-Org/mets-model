package org.mycore.mets.model.struct;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

/**
 * Abstract base class for all kinds of Divs within a logical struct map.
 * 
 * @author Matthias Eichner
 * @author Silvio Hermann
 */
public abstract class AbstractLogicalDiv extends AbstractDiv<LogicalSubDiv> {

    public final static String XML_ORDER = "ORDER";

    public final static String XML_LABEL = "LABEL";

    protected String label;

    protected HashMap<String, LogicalSubDiv> subDivContainer;

    protected int order;

    private AbstractLogicalDiv parent;

    /**
     * @param id
     *            the id of the div
     * @param type
     *            the type attribute
     * @param label
     *            the label of the div
     * @param order
     *            the order of the div
     */
    public AbstractLogicalDiv(String id, String type, String label, int order) {
        this.subDivContainer = new LinkedHashMap<String, LogicalSubDiv>();
        this.setId(id);
        this.setType(type);
        this.setLabel(label);
        this.setOrder(order);
    }

    @Override
    public void add(LogicalSubDiv lsd) {
        if (lsd == null) {
            return;
        }
        lsd.setParent(this);
        this.subDivContainer.put(lsd.getId(), lsd);
    }

    @Override
    public void remove(LogicalSubDiv divToDelete) {
        for (LogicalSubDiv lsd : subDivContainer.values().toArray(new LogicalSubDiv[0])) {
            if (lsd == divToDelete) {
                this.subDivContainer.remove(lsd.getId());
                lsd.setParent(null);
                return;
            } else {
                removeFromChildren(lsd.getChildren(), divToDelete);
            }
        }
    }

    private void removeFromChildren(List<LogicalSubDiv> children, LogicalSubDiv divToDelete) {
        for (LogicalSubDiv lsd : children) {
            if (lsd == divToDelete) {
                lsd.getParent().remove(divToDelete.getId());
                lsd.setParent(null);
                return;
            } else {
                removeFromChildren(lsd.getChildren(), divToDelete);
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

    @Override
    public List<LogicalSubDiv> getChildren() {
        return new Vector<LogicalSubDiv>(subDivContainer.values());
    }

    /**
     * @param parentToSet
     */
    protected void setParent(AbstractLogicalDiv parentToSet) {
        this.parent = parentToSet;
    }

    /**
     * @return the parent of this div
     */
    public AbstractLogicalDiv getParent() {
        return this.parent;
    }

    /**
     * Returns a {@link LogicalSubDiv} with the given id.
     * 
     * @param identifier
     * @return a {@link LogicalSubDiv} with the given id or null
     */
    public LogicalSubDiv getLogicalSubDiv(String identifier) {
        for (LogicalSubDiv lsd : subDivContainer.values()) {
            if (lsd.getId().equals(identifier)) {
                return lsd;
            } else {
                LogicalSubDiv logicalSubDiv = lookupChildren(lsd.getChildren(), identifier);
                if (logicalSubDiv != null) {
                    return logicalSubDiv;
                }
            }
        }
        return null;
    }

    private LogicalSubDiv lookupChildren(List<LogicalSubDiv> children, String identifier) {
        for (LogicalSubDiv lsd : children) {
            if (lsd.getId().equals(identifier)) {
                return lsd;
            } else {
                lookupChildren(lsd.getChildren(), identifier);
            }
        }
        return null;
    }

    /**
     * Sets the label attribute.
     * 
     * @param label
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

    /**
     * Sets the order attribute.
     * 
     * @param order
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the value of the order attribute
     */
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public Element asElement() {
        Element div = super.asElement();
        if (this.getLabel() != null && !this.getLabel().equals("")) {
            div.setAttribute(XML_LABEL, this.getLabel());
        }
        if (this.getOrder() != -1) {
            div.setAttribute(XML_ORDER, String.valueOf(this.getOrder()));
        }
        Iterator<LogicalSubDiv> sbDivIterator = this.subDivContainer.values().iterator();
        while (sbDivIterator.hasNext()) {
            div.addContent(sbDivIterator.next().asElement());
        }
        return div;
    }
}
