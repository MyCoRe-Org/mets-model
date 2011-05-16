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
package org.mycore.mets.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mycore.mets.model.files.FileSec;
import org.mycore.mets.model.sections.AmdSec;
import org.mycore.mets.model.sections.DmdSec;
import org.mycore.mets.model.struct.IStructMap;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.StructLink;

/**
 * @author Silvio Hermann (shermann)
 */
public class Mets {

    private static final Logger LOGGER = Logger.getLogger(Mets.class);

    private Map<String, DmdSec> dmdsecs;

    private Map<String, AmdSec> amdsecs;

    private Map<String, IStructMap> structMaps;

    private StructLink structLink;

    private FileSec fileSec;

    /**
     * 
     */
    public Mets() {
        this.dmdsecs = new HashMap<String, DmdSec>();
        this.amdsecs = new HashMap<String, AmdSec>();
        this.structMaps = new HashMap<String, IStructMap>();
        this.structMaps.put(LogicalStructMap.TYPE, new LogicalStructMap());
        this.structMaps.put(PhysicalStructMap.TYPE, new PhysicalStructMap());
    }

    /**
     * Adds the section to the mets document
     * 
     * @param section
     *            the section to add
     * @return <code>true</code> if the section was added successfully,
     *         <code>false</code> otherwise
     */
    public boolean addDmdSec(DmdSec section) {
        String id = section.getId();
        if (dmdsecs.containsKey(id)) {
            return false;
        }
        dmdsecs.put(id, section);
        return true;
    }

    /** Removes the given MdSection from the Mets document */
    public void removeDmdSec(DmdSec section) {
        String id = section.getId();
        dmdsecs.remove(id);
    }

    /**
     * Adds the section to the mets document
     * 
     * @param section
     *            the section to add
     * @return <code>true</code> if the section was added successfully,
     *         <code>false</code> otherwise
     */
    public boolean addAmdSec(AmdSec section) {
        String id = section.getId();
        if (amdsecs.containsKey(id)) {
            return false;
        }
        amdsecs.put(id, section);
        return true;
    }

    /** Removes the given MdSection from the Mets document */
    public void removeAmdSec(AmdSec section) {
        String id = section.getId();
        amdsecs.remove(id);
    }

    /**
     * @param type
     */
    public IStructMap getStructMap(String type) {
        return this.structMaps.get(type);
    }

    public void addStructMap(IStructMap structMap) {
        this.structMaps.put(structMap.getType(), structMap);
    }

    public void removeStructMap(String type) {
        this.structMaps.remove(type);
    }

    /**
     * @return the structLink
     */
    public StructLink getStructLink() {
        return structLink;
    }

    /**
     * @param structLink
     *            the structLink to set
     */
    public void setStructLink(StructLink structLink) {
        this.structLink = structLink;
    }

    /**
     * @return the fileSec
     */
    public FileSec getFileSec() {
        return fileSec;
    }

    /**
     * @param fileSec
     *            the fileSec to set
     */
    public void setFileSec(FileSec fileSec) {
        this.fileSec = fileSec;
    }

    /**
     * @param id
     *            the id of the amdsec to retrieve
     * @return the amdsec with the given id or null if there is no such amd
     *         section
     */
    public AmdSec getAmdSecById(String id) {
        return this.amdsecs.get(id);
    }

    /**
     * @return all amd sections
     */
    public AmdSec[] getAmdSections() {
        return this.amdsecs.values().toArray(new AmdSec[0]);
    }

    /**
     * @return all dmd sections
     */
    public DmdSec[] getDmdSections() {
        return this.dmdsecs.values().toArray(new DmdSec[0]);
    }

    /**
     * @param id
     *            the id of the dmdsec to retrieve
     * @return the dmdsec with the given id or null if there is no such dmd
     *         section
     */
    public DmdSec getDmdSecById(String id) {
        return this.dmdsecs.get(id);
    }

    /**
     * Returns the Mets Object as {@link Document}. <br/>
     * Internally the document is validated and exception is thrown if the
     * document is invalid.
     * 
     * @return {@link Document}
     * @throws Exception
     *             if the document generated is invalid as the underlying mets
     *             is invalid
     */
    public Document asDocument() throws Exception {
        Document doc = new Document();

        Element mets = new Element("mets", IMetsElement.METS);
        mets.addNamespaceDeclaration(IMetsElement.XSI);
        mets.setAttribute(
                "schemaLocation",
                "http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-2.xsd",
                IMetsElement.XSI);
        doc.setRootElement(mets);

        Iterator<DmdSec> dmdSecIt = this.dmdsecs.values().iterator();
        while (dmdSecIt.hasNext()) {
            mets.addContent(dmdSecIt.next().asElement());
        }

        Iterator<AmdSec> amdSecIt = this.amdsecs.values().iterator();
        while (amdSecIt.hasNext()) {
            mets.addContent(amdSecIt.next().asElement());
        }

        mets.addContent(this.getFileSec().asElement());
        for (IStructMap sM : this.structMaps.values()) {
            mets.addContent(sM.asElement());
        }
        mets.addContent(this.getStructLink().asElement());

        boolean isValid = Mets.isValid(doc);
        if (!isValid) {
            throw new IllegalStateException("The mets document is not valid");
        }
        return doc;
    }

    /**
     * @param doc
     *            the document do validate against the mets schema
     * @return true if the document is a valid mets document false otherwise
     */
    final public static boolean isValid(Document doc) {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            outputter.output(doc, outStream);
        } catch (IOException e) {
            return false;
        }

        byte[] xml = outStream.toByteArray();
        SAXBuilder s = new SAXBuilder(true);
        s.setFeature("http://apache.org/xml/features/validation/schema", true);
        s.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", "http://www.loc.gov/standards/mets/mets.xsd");

        ByteArrayInputStream in = new ByteArrayInputStream(xml);
        try {
            s.build(in);
        } catch (JDOMException jdomEx) {
            LOGGER.error("Error parsing and validating mets document", jdomEx);
            return false;
        } catch (IOException ioEx) {
            LOGGER.error("Error reading input stream", ioEx);
            return false;
        } finally {
            try {
                in.close();
                outStream.close();
            } catch (IOException e) {
                LOGGER.error("Error while closing byte array streams", e);
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the mets object.
     * 
     * @return <code>true</code> if the underlying mets document ist valid,
     *         <code>false</code> otherwise
     */
    final public boolean isValid() {
        Document doc = null;
        try {
            doc = this.asDocument();
        } catch (Throwable t) {
            return false;
        }

        return Mets.isValid(doc);
    }
}
