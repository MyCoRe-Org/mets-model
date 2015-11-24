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
package org.mycore.mets.model.files;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;
import org.mycore.mets.model.struct.LOCTYPE;

/**
 * Implementation for the mets:FLocat element in a mets document.
 * 
 * @author Silvio Hermann (shermann)
 */
public class FLocat implements IMetsElement {

    public final static String LOCTYPE_URL = "URL";

    private String href;

    private LOCTYPE type;

    /**
     * @param loctype
     *            the loctype
     * @param href
     *            the href
     */
    public FLocat(LOCTYPE loctype, String href) {
        this.type = loctype;
        this.href = href;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href
     *            the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * @return the type
     */
    public LOCTYPE getType() {
        return type;
    }

    /**
     * @param loctype
     *            the loctype to set
     * @deprecated use {@link FLocat#setType(LOCTYPE)}
     */
    public void setType(String loctype) {
        this.type = LOCTYPE.valueOf(loctype);
    }

    /**
     * @param loctype
     *            the {@link LOCTYPE} to set
     */
    public void setType(LOCTYPE loctype) {
        this.type = loctype;
    }

    @Override
    public String toString() {
        return "loctype=" + this.getType() + ", href=" + this.href;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element fLoc = new Element("FLocat", IMetsElement.METS);
        fLoc.setAttribute("LOCTYPE", this.getType().toString());
        fLoc.setAttribute("href", this.getHref(), IMetsElement.XLINK);
        return fLoc;
    }
}
