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
public class PhysicalStructMap extends StructMap implements IMetsElement {

    private Div divContainer;

    public PhysicalStructMap() {
        super(StructMap.TYPE_PHYSICAL);
    }

    /**
     * @return the divContainer
     */
    public Div getDivContainer() {
        return divContainer;
    }

    /**
     * @param divContainer
     *            the divContainer to set
     */
    public void setDivContainer(Div divContainer) {
        this.divContainer = divContainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element structMap = new Element("structMap", IMetsElement.METS);
        structMap.setAttribute("TYPE", this.getType());
        structMap.addContent(this.getDivContainer().asElement());
        return structMap;
    }
}
