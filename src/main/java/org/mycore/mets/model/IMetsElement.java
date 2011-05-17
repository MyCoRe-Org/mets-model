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
package org.mycore.mets.model;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author Silvio Hermann (shermann)
 */
public interface IMetsElement {

    /** Namespace constant for METS namespace */
    public static final Namespace METS = Namespace.getNamespace("mets", "http://www.loc.gov/METS/");

    /** Namespace constant for XLINK namespace */
    public static final Namespace XLINK = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");

    /** Namespace constant for MODS namespace */
    public static final Namespace MODS = Namespace.getNamespace("mods", "http://www.loc.gov/mods/v3");

    /** Namespace constant for XSI namespace */
    public static final Namespace XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    /** Namespace constant for DV namespace */
    public static final Namespace DV = Namespace.getNamespace("dv", "http://dfg-viewer.de/");

    /**
     * Creates an {@link org.jdom.Element} of the IMetsElement.
     */
    public Element asElement();
}
