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
package org.mycore.mets.model.struct;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Represents a mets:fptr (file pointer) element that references a file within a logical div.
 *
 * @author Silvio Hermann (shermann)
 */
public class Fptr implements IMetsElement {

    private String fileId;

    private final List<Seq> seqList;

    /**
     * Creates a new Fptr with no file reference.
     */
    public Fptr() {
        this(null);
    }

    /**
     * Creates a new Fptr referencing the given file identifier.
     *
     * @param fileId the FILEID to reference
     */
    public Fptr(String fileId) {
        this.fileId = fileId;
        this.seqList = new ArrayList<Seq>();
    }

    /**
     * Returns the file identifier referenced by this file pointer.
     *
     * @return the fileId
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * Sets the file identifier for this file pointer.
     *
     * @param fileId
     *            the fileId to set
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     * Returns the modifiable list of sequence elements.
     *
     * @return the modifiable sequence list.
     */
    public List<Seq> getSeqList() {
        return seqList;
    }

    @Override
    public String toString() {
        return this.fileId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element fptr = new Element("fptr", IMetsElement.METS);
        if (this.getFileId() != null) {
            fptr.setAttribute("FILEID", this.getFileId());
        }
        for (Seq seq : seqList) {
            fptr.addContent(seq.asElement());
        }
        return fptr;
    }
}
