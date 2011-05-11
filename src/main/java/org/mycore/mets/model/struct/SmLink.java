/* $Revision: 3033 $ 
 * $Date: 2010-10-22 13:41:12 +0200 (Fr, 22. Okt 2010) $ 
 * $LastChangedBy: thosch $
 * Copyright 2010 - Thüringer Universitäts- und Landesbibliothek Jena
 *  
 * Mets-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mets-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mets-Editor.  If not, see http://www.gnu.org/licenses/.
 */
package org.mycore.mets.model.struct;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 * 
 */
public class SmLink implements IMetsElement {
    
    public static final String XML_NAME = "smLink";
    public static final String XML_FROM = "from";
    public static final String XML_TO = "to";
    
    private SubDiv logical;

    private SubDiv physical;

    private String from, to;
    
    /**
     * @param logical
     * @param physical
     * */
    @Deprecated
    public SmLink(SubDiv logical, SubDiv physical) {
        this.logical = logical;
        this.physical = physical;
    }

    public SmLink(String from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * @return the logical
     */
    @Deprecated
    public SubDiv getLogical() {
        return logical;
    }

    /**
     * @param logical
     *            the logical to set
     */
    @Deprecated
    public void setLogical(SubDiv logical) {
        this.logical = logical;
    }

    /**
     * @return the physical
     */
    @Deprecated
    public SubDiv getPhysical() {
        return physical;
    }

    /**
     * @param physical
     *            the physical to set
     */
    @Deprecated
    public void setPhysical(SubDiv physical) {
        this.physical = physical;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element smLink = new Element(XML_NAME, IMetsElement.METS);
        if(this.getLogical() != null && this.getPhysical() != null) {
            smLink.setAttribute(XML_FROM, this.getLogical().getId(), IMetsElement.XLINK);
            smLink.setAttribute(XML_TO, this.getPhysical().getId(), IMetsElement.XLINK);
        }else if(this.getFrom() != null && this.getTo() != null) {
            smLink.setAttribute(XML_FROM, this.getFrom(), IMetsElement.XLINK);
            smLink.setAttribute(XML_TO, this.getTo(), IMetsElement.XLINK);
        }
        return smLink;
    }
}
