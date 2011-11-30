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
        this.subDivContainer.put(lsd.getId(), lsd);
    }

    @Override
    public void remove(LogicalSubDiv element) {
        this.subDivContainer.remove(element);
    }

    /**
     * @param id
     */
    public void remove(String id) {
        this.subDivContainer.remove(id);
    }

    @Override
    public List<LogicalSubDiv> getChildren() {
        return new Vector<LogicalSubDiv>(subDivContainer.values());
    }

    /**
     * Returns a {@link LogicalSubDiv} with the given id.
     * 
     * @param id
     * @return a {@link LogicalSubDiv} with the given id or null
     */
    public LogicalSubDiv getLogicalSubDiv(String id) {
        return this.subDivContainer.get(id);
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
     * @return
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
