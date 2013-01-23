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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Implementation for the mets:fileSec element in a mets document.
 * 
 * @author Silvio Hermann (shermann)
 */
public class FileSec implements IMetsElement {
    private HashMap<String, FileGrp> fGroups;

    /**
     * 
     */
    public FileSec() {
        fGroups = new LinkedHashMap<String, FileGrp>();
    }

    /**
     * Adds a file group to the file section.
     * 
     * @param grp
     *            the {@link FileGrp} to add
     * @return true if the operation was successful, false otherwise
     */
    public boolean addFileGrp(FileGrp grp) {
        if (grp.getUse() != null && !fGroups.containsKey(grp.getUse())) {
            fGroups.put(grp.getUse(), grp);
            return true;
        }
        return false;
    }

    /**
     * Removes the given file group from this file section. Please note: the USE
     * attribute of the file group must not be null.
     * 
     * @param grp
     *            the group to remove
     */
    public boolean removeFileGrp(FileGrp grp) {
        return removeFileGrpByUse(grp.getUse());
    }

    /**
     * Removes the given file group from this file section by its use attribute.
     * 
     * @param use
     *            the use attribute
     * @see {@link FileSec#removeFileGrp(FileGrp)}
     */
    public boolean removeFileGrpByUse(String use) {
        if (use == null || use.length() == 0) {
            return false;
        }

        this.fGroups.remove(use);
        return true;
    }

    /**
     * @return all {@link FileGrp} owned by this file section
     */
    public List<FileGrp> getFileGroups() {
        return new Vector<FileGrp>(fGroups.values());
    }

    /**
     * @param use
     *            one of {@link FileGrp#USE_DEFAULT}, {@link FileGrp#USE_MAX},
     *            {@link FileGrp#USE_MIN}, {@link FileGrp#USE_DEFAULT}
     * @return the {@link FileGrp} object with the given use attribute or null
     */
    public FileGrp getFileGroup(String use) {
        for (FileGrp grp : this.fGroups.values()) {
            if (grp.getUse().equals(use)) {
                return grp;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /* (non-Javadoc)
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element fSec = new Element("fileSec", IMetsElement.METS);
        Iterator<FileGrp> iterator = this.fGroups.values().iterator();
        while (iterator.hasNext()) {
            fSec.addContent(iterator.next().asElement());
        }
        return fSec;
    }
}
