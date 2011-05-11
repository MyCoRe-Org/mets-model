package org.mycore.mets.model.struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 *
 * @author Matthias Eichner
 */
public abstract class AbstractLogicalDiv extends AbstractDiv<LogicalSubDiv> {

    public final static String XML_ORDER = "ORDER";
    public final static String XML_LABEL = "LABEL";

    protected String label;
    protected List<LogicalSubDiv> subDivList;
    protected int order;

    public AbstractLogicalDiv(String id, String type, String label, int order) {
        this.subDivList = new ArrayList<LogicalSubDiv>();
        this.setId(id);
        this.setType(type);
        this.setLabel(label);
        this.setOrder(order);
    }

    @Override
    public void add(LogicalSubDiv element) {
        this.subDivList.add(element);
    }

    @Override
    public void remove(LogicalSubDiv element) {
        this.subDivList.remove(element);
    }

    @Override
    public List<LogicalSubDiv> getChildren() {
        return this.subDivList;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public int getOrder() {
        return order;
    }
    

    @Override
    public Element asElement() {
        Element div = super.asElement();
        if(this.getLabel() != null && !this.getLabel().equals("")) {
            div.setAttribute(XML_LABEL, this.getLabel());
        }
        if(this.getOrder() != -1) {
            div.setAttribute(XML_ORDER, String.valueOf(this.getOrder()));
        }
        Iterator<LogicalSubDiv> sbDivIterator = this.subDivList.iterator();
        while (sbDivIterator.hasNext()) {
            div.addContent(sbDivIterator.next().asElement());
        }
        return div;
    }
}
