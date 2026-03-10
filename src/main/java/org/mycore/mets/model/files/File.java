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

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Represents a mets:file element within a mets:fileGrp in a METS document.
 *
 * @author Silvio Hermann (shermann)
 */
public class File implements IMetsElement {

    /**
     * Constant for an id prefix when the file is in a file group with USE
     * attribute = MIN
     */
    public static final String PREFIX_MIN = "min_";

    /**
     * Constant for an id prefix when the file is in a file group with USE
     * attribute = MAX
     */
    public static final String PREFIX_MAX = "max_";

    /**
     * Constant for an id prefix when the file is in a file group with USE
     * attribute = DEFAULT
     */
    public static final String PREFIX_DEFAULT = "default_";

    /**
     * Constant for an id prefix when the file is in a file group with USE
     * attribute = MASTER
     */
    public static final String PREFIX_MASTER = "master_";

    /** Constant for image/jpeg mime type */
    public static final String MIME_TYPE_JPEG = "image/jpeg";

    /** Constant for image/tiff mime type */
    public static final String MIME_TYPE_TIFF = "image/tiff";

    private String id, mimeType;

    private FLocat fLocat;

    /**
     * Creates a new File with the given id and MIME type.
     *
     * @param id
     *            the id of the file
     * @param mimeType
     *            the mime type of the file
     */
    public File(String id, String mimeType) {
        this.id = id;
        this.mimeType = mimeType;
        this.fLocat = null;
    }

    /**
     * Returns the file identifier.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the file identifier.
     *
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the MIME type of this file.
     *
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the MIME type of this file.
     *
     * @param mimeType
     *            the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Returns the FLocat element associated with this file.
     *
     * @return the fLocat
     */
    public FLocat getFLocat() {
        return fLocat;
    }

    /**
     * Sets the FLocat element for this file.
     *
     * @param fLocat
     *            the fLocat to set
     */
    public void setFLocat(FLocat fLocat) {
        this.fLocat = fLocat;
    }

    public String toString() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element file = new Element("file", IMetsElement.METS);
        file.setAttribute("ID", this.getId());
        if (getMimeType() != null) {
            file.setAttribute("MIMETYPE", getMimeType());
        }
        file.addContent(this.getFLocat().asElement());
        return file;
    }
}
