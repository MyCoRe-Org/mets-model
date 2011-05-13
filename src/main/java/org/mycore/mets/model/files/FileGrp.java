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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 * 
 */
public class FileGrp implements IMetsElement {

    public static final String USE_MIN = "MIN";
    public static final String USE_MAX = "MAX";
    public static final String USE_DEFAULT = "DEFAULT";
    public static final String USE_MASTER = "MASTER";

    public static final String PREFIX_MIN = "min_";
    public static final String PREFIX_MAX = "max_";
    public static final String PREFIX_DEFAULT = "default_";
    public static final String PREFIX_MASTER = "master_";

    private String use;

    private List<File> fList;

    /***/
    public FileGrp(String use) {
        this.use = use;
        this.fList = new Vector<File>();
    }

    /**
     * @return the use
     */
    public String getUse() {
        return use;
    }

    /**
     * @param use
     *            the use to set
     */
    public void setUse(String use) {
        this.use = use;
    }

    public void addFile(File f) {
        fList.add(f);
    }

    public void removeFile(File f) {
        fList.remove(f);
    }

    public List<File> getfList() {
        return fList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element fileGrp = new Element("fileGrp", IMetsElement.METS);
        fileGrp.setAttribute("USE", this.getUse());
        Iterator<File> iterator = this.fList.iterator();
        while (iterator.hasNext()) {
            fileGrp.addContent(iterator.next().asElement());
        }
        return fileGrp;
    }
}
