package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

public abstract class AbstractDiv<T extends IMetsElement> implements IDiv<T> {

    protected String id, type;

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
    public Element asElement() {
        Element div = new Element(XML_NAME, IMetsElement.METS);
        if (this.getId() != null && !this.getId().equals("")) {
            div.setAttribute(XML_ID, this.getId());
        }
        if (this.getType() != null && !this.getType().equals("")) {
            div.setAttribute(XML_TYPE, this.getType());
        }
        return div;
    }
}
