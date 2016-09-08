package org.mycore.mets.model.header;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * The mets header element &lt;metsHdr&gt; captures metadata about the METS document itself,
 * not the digital object the METS document encodes. Although it records a more
 * limited set of metadata, it is very similar in function and purpose to the headers
 * employed in other schema such as the Text Encoding Initiative (TEI) or in the Encoded
 * Archival Description (EAD).
 * 
 * @see <a href="http://www.loc.gov/standards/mets/docs/mets.v1-9.html#metsHdr">metsHdr</a>
 * @author Matthias Eichner
 */
public class MetsHdr implements IMetsElement {

    private String id;

    private String admId;

    private Instant createDate;

    private Instant lastModDate;

    private String recordStatus;

    /**
     * Creates a new &lt;metsHdr&gt; object.
     * 
     */
    public MetsHdr() {
        this.id = null;
        this.admId = null;
        this.recordStatus = null;
        this.setCreateDate(Instant.now());
        this.setLastModDate(Instant.now());
    }

    @Override
    public Element asElement() {
        Element metsHdr = new Element("metsHdr", IMetsElement.METS);
        if (this.id != null) {
            metsHdr.setAttribute("ID", this.id);
        }
        if (this.admId != null) {
            metsHdr.setAttribute("ADMID", this.admId);
        }
        if (this.createDate != null) {
            metsHdr.setAttribute("CREATEDATE", DateTimeFormatter.ISO_INSTANT.format(this.createDate));
        }
        if (this.lastModDate != null) {
            metsHdr.setAttribute("LASTMODDATE", DateTimeFormatter.ISO_INSTANT.format(this.lastModDate));
        }
        if (this.recordStatus != null) {
            metsHdr.setAttribute("RECORDSTATUS", this.recordStatus);
        }
        return metsHdr;
    }

    /**
     * This attribute uniquely identifies the element within the METS document,
     * and would allow the element to be referenced unambiguously from another element or
     * document via an IDREF or an XPTR. For more information on using ID attributes for
     * internal and external linking see Chapter 4 of the METS Primer.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * This attribute uniquely identifies the element within the METS document,
     * and would allow the element to be referenced unambiguously from another element or
     * document via an IDREF or an XPTR. For more information on using ID attributes for
     * internal and external linking see Chapter 4 of the METS Primer.
     * 
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Contains the ID attribute values of the &lt;techMD&gt;, &lt;sourceMD&gt;,
     * &lt;rightsMD&gt; and/or &lt;digiprovMD&gt; elements within the &lt;amdSec&gt; of the METS document
     * that contain administrative metadata pertaining to the METS document itself. For
     * more information on using METS IDREFS and IDREF type attributes for internal
     * linking, see Chapter 4 of the METS Primer.
     * 
     * @return the adm identifier
     */
    public String getAdmId() {
        return admId;
    }

    /**
     * Contains the ID attribute values of the &lt;techMD&gt;, &lt;sourceMD&gt;,
     * &lt;rightsMD&gt; and/or &lt;digiprovMD&gt; elements within the &lt;amdSec&gt; of the METS document
     * that contain administrative metadata pertaining to the METS document itself. For
     * more information on using METS IDREFS and IDREF type attributes for internal
     * linking, see Chapter 4 of the METS Primer.
     * 
     * @param admId the new adm identifier
     */
    public void setAdmId(String admId) {
        this.admId = admId;
    }

    /**
     * Records the date/time the METS document was created.
     * 
     * @return mets document creation date time
     */
    public Instant getCreateDate() {
        return createDate;
    }

    /**
     * Records the date/time the METS document was created.
     * 
     * @param createDate new creation date
     */
    public void setCreateDate(Instant createDate) {
        this.createDate = Instant.ofEpochMilli(createDate.toEpochMilli()).with(ChronoField.MILLI_OF_SECOND, 0);
    }

    /**
     * Is used to indicate the date/time the METS document was last modified.
     * 
     * @return mets document modified date
     */
    public Instant getLastModDate() {
        return lastModDate;
    }

    /**
     * Is used to indicate the date/time the METS document was last modified.
     * 
     * @param lastModDate new modified date
     */
    public void setLastModDate(Instant lastModDate) {
        this.lastModDate = Instant.ofEpochMilli(lastModDate.toEpochMilli()).with(ChronoField.MILLI_OF_SECOND, 0);
    }

    /**
     * Specifies the status of the METS document. It is used for internal processing purposes.
     * 
     * @return status of the mets document
     */
    public String getRecordStatus() {
        return recordStatus;
    }

    /**
     * Specifies the status of the METS document. It is used for internal processing purposes.
     * 
     * @param recordStatus status of this mets document
     */
    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

}
