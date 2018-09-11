package org.mycore.mets.model.struct;

import java.util.List;

import org.mycore.mets.model.IMetsElement;

public interface IDiv<T extends IMetsElement> extends IMetsElement {

    String XML_NAME = "div";

    String XML_ID = "ID";

    String XML_TYPE = "TYPE";

    /**
     * @param id new id
     */
    void setId(String id);

    /**
     * @return the id
     */
    String getId();

    /**
     * An optional integer representation of this div's order among its siblings (e.g., its sequence).
     *
     * @param order the order
     */
    void setOrder(Integer order);

    /**
     * Returns an integer representation of this div's order among its siblings (e.g., its sequence).
     *
     * @return order as integer
     */
    Integer getOrder();

    /**
     * An optional string representation of this div's order among its siblings (e.g., "xii"), or a non-integer native 
     * numbering system. It is presumed that this value will still be machine-actionable (e.g., supports a page 'go to'
     * function), and is not a replacement/substitute for the LABEL attribute.
     * 
     * @param orderLabel the order label as string
     */
    void setOrderLabel(String orderLabel);

    /**
     * Returns a string representation of this div's order among its siblings (e.g., "xii"), or a non-integer native 
     * numbering system. It is presumed that this value will still be machine-actionable (e.g., supports a page 'go to'
     * function), and is not a replacement/substitute for the LABEL attribute.
     * 
     * @return the order label as string
     */
    String getOrderLabel();

    /**
     * An optional string label to describe this div to an end user viewing the document, as per a table of contents
     * entry (NB: a div LABEL should be specific to its level in the structural map. In the case of a book with
     * chapters, the book div LABEL should have the book title, and the chapter div LABELS should have the individual
     * chapter titles, rather than having the chapter div LABELs combine both book title and chapter title).
     *
     * @param label the label as string
     */
    void setLabel(String label);

    /**
     * Returns a string label to describe this div to an end user viewing the document, as per a table of contents
     * entry (NB: a div LABEL should be specific to its level in the structural map. In the case of a book with
     * chapters, the book div LABEL should have the book title, and the chapter div LABELS should have the individual
     * chapter titles, rather than having the chapter div LABELs combine both book title and chapter title).
     *
     * @return the label as string
     */
    String getLabel();

    /**
     * An optional attribute providing the XML ID values for the descriptive metadata sections within this METS document
     * applicable to this div.
     *
     * @param dmdId the dmdId as string
     */
    void setDmdId(String dmdId);

    /**
     * Returns an attribute providing the XML ID values for the descriptive metadata sections within this METS document
     * applicable to this div.
     * 
     * @return the dmd identifier
     */
    String getDmdId();

    /**
     * An optional attribute providing the XML ID values for the administrative metadata sections within this METS
     * document applicable to this div.
     *
     * @param admId the admId as string
     */
    void setAdmId(String admId);

    /**
     * Returns an attribute providing the XML ID values for the administrative metadata sections within this METS
     * document applicable to this div.
     *
     * @return the adm identifier
     */
    String getAdmId();

    /**
     * Content IDs for this division (equivalent to DIDL DII).
     *
     * @param contentIds the content ids as string
     */
    void setContentIds(String contentIds);

    /**
     * Returns the content IDs for this division (equivalent to DIDL DII).
     *
     * @return the content ids as string
     */
    String getContentIds();

    /**
     * An optional string attribute for specifying a type of division (e.g., chapter, article, page, etc.).
     * 
     * @param type new type
     */
    void setType(String type);

    /**
     * Returns an string attribute for specifying a type of division (e.g., chapter, article, page, etc.).
     * 
     * @return the type
     */
    String getType();

    /**
     * @param element a new element
     */
    void add(T element);

    /**
     * @param element element to remove
     */
    void remove(T element);

    /**
     * @return list of all children
     */
    List<T> getChildren();

}
