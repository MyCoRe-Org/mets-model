/*
 * $Revision$ $Date$
 * $LastChangedBy$ Copyright 2010 - Thüringer Universitäts- und
 * Landesbibliothek Jena
 * 
 * Mets-Editor is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Mets-Editor is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Mets-Editor. If not, see http://www.gnu.org/licenses/.
 */
package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Represents a mets:smLink element that connects a logical div to a physical div in the struct link section.
 *
 * @author Silvio Hermann (shermann)
 */
public class SmLink implements IMetsElement {

    /** XML element name for smLink elements. */
    public static final String XML_NAME = "smLink";

    /** XML attribute name for the xlink:from attribute. */
    public static final String XML_FROM = "from";

    /** XML attribute name for the xlink:to attribute. */
    public static final String XML_TO = "to";

    private String from, to;

    /**
     * Creates a new SmLink connecting the given from and to identifiers.
     *
     * @param from the xlink:from attribute value (logical div id)
     * @param to   the xlink:to attribute value (physical div id)
     */
    public SmLink(String from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the xlink:from attribute value.
     *
     * @return the from identifier
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the xlink:to attribute value.
     *
     * @return the to identifier
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the xlink:from attribute value.
     *
     * @param from the from identifier to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Sets the xlink:to attribute value.
     *
     * @param to the to identifier to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return from + ", " + to;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element smLink = new Element(XML_NAME, IMetsElement.METS);
        if (this.getFrom() != null && this.getTo() != null) {
            smLink.setAttribute(XML_FROM, this.getFrom(), IMetsElement.XLINK);
            smLink.setAttribute(XML_TO, this.getTo(), IMetsElement.XLINK);
        }
        return smLink;
    }
}
