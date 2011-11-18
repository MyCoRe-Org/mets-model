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

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 */
public class SmLink implements IMetsElement {

    public static final String XML_NAME = "smLink";

    public static final String XML_FROM = "from";

    public static final String XML_TO = "to";

    private String from, to;

    /**
     * @param from
     * @param to
     */
    public SmLink(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

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
