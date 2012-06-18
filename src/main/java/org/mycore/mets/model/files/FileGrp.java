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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Implementation for the mets:fileGrp element in a mets document.
 * 
 * @author Silvio Hermann (shermann)
 */
public class FileGrp implements IMetsElement {

    /** Constant for the USE attribute */
    public static final String USE_MIN = "MIN";

    /** Constant for the USE attribute */
    public static final String USE_MAX = "MAX";

    /** Constant for the USE attribute */
    public static final String USE_DEFAULT = "DEFAULT";

    /** Constant for the USE attribute */
    public static final String USE_MASTER = "MASTER";

    private String use;

    private HashMap<String, File> fMap, hrefMap;

    /**
     * @param use
     *            the use attribute of the file group
     */
    public FileGrp(String use) {
        this.use = use;
        this.fMap = new LinkedHashMap<String, File>();
        this.hrefMap = new LinkedHashMap<String, File>();
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

    /**
     * Adds a {@link File} to the file group.
     * 
     * @param f
     *            the file to add, the id of the file must not be null
     * @throws IllegalArgumentException
     *             when the id of the file is null
     */
    public void addFile(File f) {
        if (f == null || f.getId() == null || f.getId().length() == 0) {
            throw new IllegalArgumentException("ID must not be null");
        }
        fMap.put(f.getId(), f);
        hrefMap.put(f.getFLocat().getHref(), f);
    }

    /**
     * Removes a file from the file group.
     * 
     * @param f
     *            the file to remove
     */
    public void removeFile(File f) {
        if (f == null) {
            return;
        }
        fMap.remove(f.getId());
        hrefMap.remove(f.getFLocat().getHref());
    }

    /**
     * Removes the file with the given id from the file group.
     * 
     * @param identifier
     *            the of of the file to be removed
     */
    public void removeFileById(String identifier) {
        if (identifier == null) {
            return;
        }
        removeFile(fMap.get(identifier));
    }

    /**
     * Gets all the files owned by this file group as {@link List}
     * 
     * @return a list of files
     */
    public List<File> getFileList() {
        return new Vector<File>(fMap.values());
    }

    /**
     * Retrieves the {@link File} by the given id.
     * 
     * @param identifier
     *            the id of the file to return
     * @return the {@link File} with the given id or null
     */
    public File getFileById(String identifier) {
        if (identifier == null || identifier.length() == 0) {
            return null;
        }

        return fMap.get(identifier);
    }

    /**
     * Gets the {@link File} where the href attribute of the {@link File}s inner
     * {@link FLocat} element equals the given parameter.
     * 
     * @param href
     *            the href attribute
     * @return the with the given href attribute or null if there is no such
     *         file
     */
    public File getFileByHref(String href) {
        return hrefMap.get(href);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " USE=" + this.use;
    }

    /**
     * @param href the href attribute value of the {@link FLocat}
     * 
     * @return true if the file is contained by this file group, false otherwise
     */
    public boolean contains(String href) {
        return hrefMap.containsKey(href);
    }

    /**
     * @param file
     * @return
     */
    public boolean contains(File file) {
        return fMap.containsKey(file.getId());
    }

    @Override
    public Element asElement() {
        Element fileGrp = new Element("fileGrp", IMetsElement.METS);
        fileGrp.setAttribute("USE", this.getUse());
        Iterator<File> iterator = this.fMap.values().iterator();
        while (iterator.hasNext()) {
            fileGrp.addContent(iterator.next().asElement());
        }
        return fileGrp;
    }
}
