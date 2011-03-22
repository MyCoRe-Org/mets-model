/* $Revision: 3271 $ 
 * $Date: 2011-01-13 15:06:19 +0100 (Do, 13. Jan 2011) $ 
 * $LastChangedBy: shermann $
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
package org.mycore.mets.model;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author Silvio Hermann (shermann)
 * 
 */
public interface IMetsElement {
    public static final Namespace METS = Namespace.getNamespace("mets", "http://www.loc.gov/METS/");
    public static final Namespace XLINK = Namespace.getNamespace("xlink",
            "http://www.w3.org/1999/xlink");
    public static final Namespace MODS = Namespace.getNamespace("mods",
            "http://www.loc.gov/mods/v3");

    public static final Namespace DV = Namespace.getNamespace("dv", "http://dfg-viewer.de/");

    /**
     * Creates an {@link org.jdom.Element} of the IMetsElement.
     */
    public Element asElement();
}
