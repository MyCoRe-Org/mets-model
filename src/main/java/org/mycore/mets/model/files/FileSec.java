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
package org.mycore.mets.model.files;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 */
public class FileSec implements IMetsElement {
    private List<FileGrp> fGroups;

    /***/
    public FileSec() {
        fGroups = new Vector<FileGrp>();
    }

    /***/
    public boolean addFileGrp(FileGrp grp) {
        if (!fGroups.contains(grp)) {
            fGroups.add(grp);
            return true;
        }
        return false;
    }

    /***/
    public boolean removeFileGrp(FileGrp grp) {
        return this.fGroups.remove(grp);
    }

    public Element asElement() {
        Element fSec = new Element("fileSec", IMetsElement.METS);
        Iterator<FileGrp> iterator = this.fGroups.iterator();
        while (iterator.hasNext()) {
            fSec.addContent(iterator.next().asElement());
        }
        return fSec;
    }

    /**
     * @param use
     *            one of {@link FileGrp#USE_DEFAULT}, {@link FileGrp#USE_MAX},
     *            {@link FileGrp#USE_MIN}, {@link FileGrp#USE_DEFAULT}
     * @return the {@link FileGrp} object with the given use attribute or null
     */
    public FileGrp getFileGroup(String use) {
        for (FileGrp grp : this.fGroups) {
            if (grp.getUse().equals(use)) {
                return grp;
            }
        }

        return null;
    }
}
