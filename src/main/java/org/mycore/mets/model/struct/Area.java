package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Represents a mets:area element referencing a portion of a file within a mets:seq element.
 *
 * @author Matthias Eichner
 */
public class Area implements IMetsElement {

    private String id, fileId, begin, end, betype;

    /**
     * Creates a new Area with no attributes set.
     */
    public Area() {
    }

    /**
     * Creates a new Area with the given file reference and byte-range attributes.
     *
     * @param fileId the FILEID referencing the file
     * @param betype the type of the begin/end values (e.g., BYTE, IDREF)
     * @param begin  the begin position within the file
     * @param end    the end position within the file
     */
    public Area(String fileId, String betype, String begin, String end) {
        this.fileId = fileId;
        this.betype = betype;
        this.begin = begin;
        this.end = end;
    }

    /**
     * Returns the area identifier.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the area identifier.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the file identifier referenced by this area.
     *
     * @return the fileId
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * Sets the file identifier referenced by this area.
     *
     * @param fileId the fileId to set
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     * Returns the begin position within the referenced file.
     *
     * @return the begin
     */
    public String getBegin() {
        return begin;
    }

    /**
     * Sets the begin position within the referenced file.
     *
     * @param begin the begin to set
     */
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * Returns the end position within the referenced file.
     *
     * @return the end
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets the end position within the referenced file.
     *
     * @param end the end to set
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Returns the type of the begin/end coordinate values.
     *
     * @return the betype
     */
    public String getBetype() {
        return betype;
    }

    /**
     * Sets the type of the begin/end coordinate values.
     *
     * @param betype the betype to set
     */
    public void setBetype(String betype) {
        this.betype = betype;
    }

    @Override
    public Element asElement() {
        Element area = new Element("area", IMetsElement.METS);
        // id, fileId, begin, end, betype;
        if (id != null) {
            area.setAttribute("ID", id);
        }
        if (fileId != null) {
            area.setAttribute("FILEID", fileId);
        }
        if (begin != null) {
            area.setAttribute("BEGIN", begin);
        }
        if (end != null) {
            area.setAttribute("END", end);
        }
        if (betype != null) {
            area.setAttribute("BETYPE", betype);
        }
        return area;
    }

}
