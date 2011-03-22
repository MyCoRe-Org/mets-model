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
    private SubDiv logical;

    private SubDiv physical;

    /**
     * @param logical
     * @param physical
     * */
    public SmLink(SubDiv logical, SubDiv physical) {
        this.logical = logical;
        this.physical = physical;
    }

    /**
     * @return the logical
     */
    public SubDiv getLogical() {
        return logical;
    }

    /**
     * @param logical
     *            the logical to set
     */
    public void setLogical(SubDiv logical) {
        this.logical = logical;
    }

    /**
     * @return the physical
     */
    public SubDiv getPhysical() {
        return physical;
    }

    /**
     * @param physical
     *            the physical to set
     */
    public void setPhysical(SubDiv physical) {
        this.physical = physical;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element smLink = new Element("smLink", IMetsElement.METS);
        smLink.setAttribute("from", this.getLogical().getId(), IMetsElement.XLINK);
        smLink.setAttribute("to", this.getPhysical().getId(), IMetsElement.XLINK);
        return smLink;
    }
}
