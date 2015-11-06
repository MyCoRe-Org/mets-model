package org.mycore.mets.model.struct;

import java.util.List;

import org.mycore.mets.model.IMetsElement;

public interface IDiv<T extends IMetsElement> extends IMetsElement {

    public final static String XML_NAME = "div";
    public final static String XML_ID = "ID";
    public final static String XML_TYPE = "TYPE";

    /**
     * @param id new id
     */
    public void setId(String id);
    /**
     * @return the id
     */
    public String getId();
    /**
     * @param type new type
     */
    public void setType(String type);
    /**
     * @return the type
     */
    public String getType();
    
    /**
     * @param element a new element
     */
    public void add(T element);
    /**
     * @param element element to remove
     */
    public void remove(T element);
    /**
     * @return list of all children
     */
    public List<T> getChildren();
}
