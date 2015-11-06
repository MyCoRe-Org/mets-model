/* $Revision$ 
 * $Date$ 
 * $LastChangedBy$
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
package org.mycore.mets.model.sections;

import org.mycore.mets.model.IMetsElement;

/**
 * Abstract base class for all sections of a mets document
 * 
 * @author Silvio Hermann (shermann)
 */
public abstract class MdSection implements IMetsElement {
    /***/
    private String id;

    public MdSection(String id) {
        if (id == null || id.length() == 0) {
            throw new IllegalArgumentException("The id of the " + this.getClass().getSimpleName()
                    + " must not be null.");
        }
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
