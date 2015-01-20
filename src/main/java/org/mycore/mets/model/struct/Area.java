package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * 
 * @author Matthias Eichner
 */
public class Area implements IMetsElement {

    private String id, fileId, begin, end, betype;

    public Area() {
    }

    public Area(String fileId, String betype, String begin, String end) {
        this.fileId = fileId;
        this.betype = betype;
        this.begin = begin;
        this.end = end;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the fileId
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * @param fileId the fileId to set
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the begin
     */
    public String getBegin() {
        return begin;
    }

    /**
     * @param begin the begin to set
     */
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * @return the end
     */
    public String getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * @return the betype
     */
    public String getBetype() {
        return betype;
    }

    /**
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
