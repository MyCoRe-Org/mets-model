package org.mycore.mets.model.header;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * The mets header element &lt;metsHdr&gt; captures metadata about the METS
 * document itself, not the digital object the METS document encodes. Although
 * it records a more limited set of metadata, it is very similar in function and
 * purpose to the headers employed in other schema such as the Text Encoding
 * Initiative (TEI) or in the Encoded Archival Description (EAD).
 * 
 * @see <a href=
 *      "http://www.loc.gov/standards/mets/docs/mets.v1-9.html#metsHdr">metsHdr</a>
 * @author Matthias Eichner
 */
public class MetsHdr implements IMetsElement {

	private String id;

	private String admId;

	private LocalDateTime createDate;

	private LocalDateTime lastModDate;

	private String recordStatus;

	private List<Agent> agents;

	private List<AltRecordID> altRecordIds;

	public static final DateTimeFormatter DT_FORMATTER = new DateTimeFormatterBuilder()
			.appendPattern("uuuu-MM-dd")
			.appendLiteral('T')
			.appendPattern("HH:mm:ss")
			.toFormatter();

	/**
	 * Creates a new &lt;metsHdr&gt; object.
	 * 
	 */
	public MetsHdr() {
		this.id = null;
		this.admId = null;
		this.recordStatus = null;
		this.setCreateDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
		this.setLastModDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
		this.agents = new ArrayList<>();
		this.altRecordIds = new ArrayList<>();
	}

	@SuppressWarnings("exports")
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
			String createDate = DT_FORMATTER.format(this.createDate);
			metsHdr.setAttribute("CREATEDATE", createDate);
		}
		if (this.lastModDate != null) {
			String modDate = DT_FORMATTER.format(this.lastModDate);
			metsHdr.setAttribute("LASTMODDATE", modDate);
		}
		if (this.recordStatus != null) {
			metsHdr.setAttribute("RECORDSTATUS", this.recordStatus);
		}
		if (this.agents != null && !this.agents.isEmpty()) {
			for (Agent agent : this.agents) {
				metsHdr.addContent(agent.asElement());
			}
		}
		return metsHdr;
	}

	/**
	 * This attribute uniquely identifies the element within the METS document, and
	 * would allow the element to be referenced unambiguously from another element
	 * or document via an IDREF or an XPTR. For more information on using ID
	 * attributes for internal and external linking see Chapter 4 of the METS
	 * Primer.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * This attribute uniquely identifies the element within the METS document, and
	 * would allow the element to be referenced unambiguously from another element
	 * or document via an IDREF or an XPTR. For more information on using ID
	 * attributes for internal and external linking see Chapter 4 of the METS
	 * Primer.
	 * 
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Contains the ID attribute values of the &lt;techMD&gt;, &lt;sourceMD&gt;,
	 * &lt;rightsMD&gt; and/or &lt;digiprovMD&gt; elements within the &lt;amdSec&gt;
	 * of the METS document that contain administrative metadata pertaining to the
	 * METS document itself. For more information on using METS IDREFS and IDREF
	 * type attributes for internal linking, see Chapter 4 of the METS Primer.
	 * 
	 * @return the adm identifier
	 */
	public String getAdmId() {
		return admId;
	}

	/**
	 * Contains the ID attribute values of the &lt;techMD&gt;, &lt;sourceMD&gt;,
	 * &lt;rightsMD&gt; and/or &lt;digiprovMD&gt; elements within the &lt;amdSec&gt;
	 * of the METS document that contain administrative metadata pertaining to the
	 * METS document itself. For more information on using METS IDREFS and IDREF
	 * type attributes for internal linking, see Chapter 4 of the METS Primer.
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
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Records the date/time the METS document was created.
	 * 
	 * @param createDate new creation date
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * Is used to indicate the date/time the METS document was last modified.
	 * 
	 * @return mets document modified date
	 */
	public LocalDateTime getLastModDate() {
		return lastModDate;
	}

	/**
	 * Is used to indicate the date/time the METS document was last modified.
	 * 
	 * @param lastModDate new modified date
	 */
	public void setLastModDate(LocalDateTime lastModDate) {
		this.lastModDate = lastModDate;
	}

	/**
	 * Specifies the status of the METS document. It is used for internal processing
	 * purposes.
	 * 
	 * @return status of the mets document
	 */
	public String getRecordStatus() {
		return recordStatus;
	}

	/**
	 * Specifies the status of the METS document. It is used for internal processing
	 * purposes.
	 * 
	 * @param recordStatus status of this mets document
	 */
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public List<Agent> getAgents() {
		return agents;
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}

	public List<AltRecordID> getAltRecordIds() {
		return altRecordIds;
	}

	public void setAltRecordIds(List<AltRecordID> altRecordIds) {
		this.altRecordIds = altRecordIds;
	}

}
