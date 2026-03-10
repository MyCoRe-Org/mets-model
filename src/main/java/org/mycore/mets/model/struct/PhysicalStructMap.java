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
 * Represents the mets:structMap of TYPE="PHYSICAL" describing the physical structure of a METS document.
 *
 * @author Silvio Hermann (shermann)
 * @author Matthias Eichner (matthias)
 */
public class PhysicalStructMap implements IStructMap {

    /** The TYPE attribute value for physical structure maps. */
    public final static String TYPE = "PHYSICAL";

    private PhysicalDiv divContainer;

    /**
     * Creates a new empty PhysicalStructMap.
     */
    public PhysicalStructMap() {
    }

    @Override
    public String getType() {
        return TYPE;
    }

    /**
     * Returns the root div container of this physical structure map.
     *
     * @return the divContainer
     */
    public PhysicalDiv getDivContainer() {
        return divContainer;
    }

    /**
     * Sets the root div container of this physical structure map.
     *
     * @param divContainer
     *            the divContainer to set
     */
    public void setDivContainer(PhysicalDiv divContainer) {
        this.divContainer = divContainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element structMap = new Element(XML_NAME, IMetsElement.METS);
        structMap.setAttribute(XML_TYPE, this.getType());
        if(this.getDivContainer() != null) {
            structMap.addContent(this.getDivContainer().asElement());
        }
        return structMap;
    }
}
