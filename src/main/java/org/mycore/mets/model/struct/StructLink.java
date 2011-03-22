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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 * 
 */
public class StructLink implements IMetsElement {
    private List<SmLink> links;

    /***/
    public StructLink() {
        links = new Vector<SmLink>();
    }

    public boolean addSmLink(SmLink l) {
        return this.links.add(l);
    }

    public boolean removeSmLink(SmLink l) {
        return this.links.remove(l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element structLink = new Element("structLink", IMetsElement.METS);
        Iterator<SmLink> iterator = this.links.iterator();
        while (iterator.hasNext()) {
            structLink.addContent(iterator.next().asElement());
        }
        return structLink;
    }
}
