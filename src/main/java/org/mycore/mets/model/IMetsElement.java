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

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * @author Silvio Hermann (shermann)
 */
public interface IMetsElement {

    /** Namespace constant for METS namespace */
    Namespace METS = Namespace.getNamespace("mets", "http://www.loc.gov/METS/");

    /** Namespace constant for XLINK namespace */

    Namespace XLINK = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");

    /** Namespace constant for XSI namespace */
    Namespace XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    /** Namespace constant for DV namespace */
    Namespace DV = Namespace.getNamespace("dv", "http://dfg-viewer.de/");

    /** Schema location of METS */
    String SCHEMA_LOC_METS = METS.getURI() + " http://www.loc.gov/standards/mets/mets.xsd";

    /**
     * Creates an {@link org.jdom2.Element} of the IMetsElement.
     */
    Element asElement();

}
