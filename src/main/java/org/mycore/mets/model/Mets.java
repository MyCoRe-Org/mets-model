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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.transform.JDOMSource;
import org.jdom.xpath.XPath;
import org.mycore.mets.model.files.FLocat;
import org.mycore.mets.model.files.File;
import org.mycore.mets.model.files.FileGrp;
import org.mycore.mets.model.files.FileSec;
import org.mycore.mets.model.sections.AmdSec;
import org.mycore.mets.model.sections.DmdSec;
import org.mycore.mets.model.sections.MdWrapSection;
import org.mycore.mets.model.sections.RightsMD;
import org.mycore.mets.model.struct.Fptr;
import org.mycore.mets.model.struct.IStructMap;
import org.mycore.mets.model.struct.LOCTYPE;
import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.LogicalSubDiv;
import org.mycore.mets.model.struct.MDTYPE;
import org.mycore.mets.model.struct.MdRef;
import org.mycore.mets.model.struct.MdWrap;
import org.mycore.mets.model.struct.PhysicalDiv;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.PhysicalSubDiv;
import org.mycore.mets.model.struct.SmLink;
import org.mycore.mets.model.struct.StructLink;
import org.xml.sax.SAXException;

/**
 * @author Silvio Hermann (shermann)
 */
public class Mets {

    private static final Logger LOGGER;

    private static final SchemaFactory SCHEMA_FACTORY;

    private static Schema SCHEMA;

    private static Validator VALIDATOR;
    static {
        LOGGER = Logger.getLogger(Mets.class);
        SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            SCHEMA = SCHEMA_FACTORY.newSchema(new StreamSource("http://www.loc.gov/standards/mets/mets.xsd"));
            VALIDATOR = SCHEMA.newValidator();
        } catch (Exception ex) {
            LOGGER.error("Error initializing mets validator", ex);
        }
    }

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
        this.fileSec = new FileSec();
        this.structLink = new StructLink();
    }

    /**
     * Creates a {@link Mets} object from the given {@link Document} object.
     * 
     * @param source
     */
    public Mets(Document source) throws Exception {
        this();
        if (!Mets.isValid(source)) {
            throw new IllegalArgumentException("The given document is not a valid mets document");
        }

        createDmdSec(source);
        createAmdSec(source);
        createFileSec(source);
        createLogicalStructMap(source);
        createPhysicalStructMap(source);
        createStuctLinks(source);
    }

    @SuppressWarnings("unchecked")
    private void createDmdSec(Document source) throws JDOMException {
        XPath xp = XPath.newInstance("mets:mets/mets:dmdSec");
        xp.addNamespace(IMetsElement.METS);

        for (Element section : (List<Element>) xp.selectNodes(source)) {
            DmdSec dmdSec = new DmdSec(section.getAttributeValue("ID"));
            dmdsecs.put(dmdSec.getId(), dmdSec);

            // handle mdWrap
            xp = XPath.newInstance("mets:mdWrap");
            xp.addNamespace(IMetsElement.METS);

            for (Element wrap : (List<Element>) xp.selectNodes(section)) {
                XPath xmlDataXP = XPath.newInstance("mets:xmlData/*");
                xmlDataXP.addNamespace(IMetsElement.METS);
                Element element = (Element) xmlDataXP.selectSingleNode(wrap);

                MdWrap mdWrap = new MdWrap(MdWrapSection.findTypeByName(wrap.getAttributeValue("MDTYPE")), (Element) element.clone());
                dmdSec.setMdWrap(mdWrap);
            }

            // handle mdRef
            xp = XPath.newInstance("mets:mdRef");
            xp.addNamespace(IMetsElement.METS);

            for (Element refElem : (List<Element>) xp.selectNodes(section)) {
                LOCTYPE loctype = LOCTYPE.valueOf(refElem.getAttributeValue("LOCTYPE"));
                String mimetype = refElem.getAttributeValue("MIMETYPE");
                MDTYPE mdtype = MdWrapSection.findTypeByName(refElem.getAttributeValue("MDTYPE"));
                String href = refElem.getAttributeValue("href", IMetsElement.XLINK);
                MdRef mdRef = new MdRef(href, loctype, mimetype, mdtype, refElem.getText());
                dmdSec.setMdRef(mdRef);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void createAmdSec(Document source) throws JDOMException {
        XPath xp = XPath.newInstance("mets:mets/mets:amdSec");
        xp.addNamespace(IMetsElement.METS);

        List<Element> allSections = xp.selectNodes(source);

        for (Element section : allSections) {
            AmdSec amdSec = new AmdSec(section.getAttributeValue("ID"));
            amdsecs.put(amdSec.getId(), amdSec);

            addOther(section, amdSec, "rightsMD");
            addOther(section, amdSec, "digiprovMD");
        }
    }

    /**
     * Creates the elements embedded in the mets:amdSec. Currently the
     * mets:rightsMD and the mets:digiprovMD sections are supported.
     * 
     * @param section
     * @param amdSec
     * @param flag
     *            one of "rightsMD" or "addRightsMd"
     * @throws JDOMException
     */
    private void addOther(Element section, AmdSec amdSec, String flag) throws JDOMException {
        XPath rightsMdXP = XPath.newInstance("mets:" + flag);
        rightsMdXP.addNamespace(IMetsElement.METS);

        Object obj = rightsMdXP.selectSingleNode(section);
        if (obj instanceof Element) {
            String id = ((Element) obj).getAttributeValue("ID");
            RightsMD rightsMd = new RightsMD(id);
            amdSec.setRightsMD(rightsMd);

            XPath mdWrapXP = XPath.newInstance("mets:mdWrap");
            mdWrapXP.addNamespace(IMetsElement.METS);

            obj = mdWrapXP.selectSingleNode(obj);
            if (obj instanceof Element) {
                XPath xmlDataXP = XPath.newInstance("mets:xmlData/*");
                xmlDataXP.addNamespace(IMetsElement.METS);

                String name = ((Element) obj).getAttributeValue("MDTYPE");
                Element element = (Element) xmlDataXP.selectSingleNode(obj);
                MdWrap mdWrap = new MdWrap(MdWrapSection.findTypeByName(name), (Element) element.clone());
                mdWrap.setOtherMdType(((Element) obj).getAttributeValue("OTHERMDTYPE"));
                rightsMd.setMdWrap(mdWrap);
            }
        }
    }

    /**
     * Creates the mets:structMap TYPE="LOGICAL".
     * 
     * @param source
     */
    @SuppressWarnings("unchecked")
    private void createLogicalStructMap(Document source) throws JDOMException {
        XPath xp = XPath.newInstance("mets:mets/mets:structMap[@TYPE='LOGICAL']/mets:div");
        xp.addNamespace(IMetsElement.METS);

        Element logDivContainerElem = (Element) xp.selectSingleNode(source);

        LogicalDiv logDivContainer = new LogicalDiv(logDivContainerElem.getAttributeValue("ID"),
                logDivContainerElem.getAttributeValue("TYPE"), logDivContainerElem.getAttributeValue("LABEL"),
                Integer.valueOf(logDivContainerElem.getAttributeValue("ORDER")), logDivContainerElem.getAttributeValue("ADMID"),
                logDivContainerElem.getAttributeValue("DMDID"));

        for (Element logSubDiv : (List<Element>) logDivContainerElem.getChildren()) {
            LogicalSubDiv lsd = new LogicalSubDiv(logSubDiv.getAttributeValue("ID"), logSubDiv.getAttributeValue("TYPE"),
                    logSubDiv.getAttributeValue("LABEL"), Integer.valueOf(logSubDiv.getAttributeValue("ORDER")));
            logDivContainer.add(lsd);

            processLogicalSubDivChildren((List<Element>) logSubDiv.getChildren(), lsd);
        }

        // add the div container to the struct map
        ((LogicalStructMap) getStructMap(LogicalStructMap.TYPE)).setDivContainer(logDivContainer);
    }

    /**
     * @param children
     * @param parent
     */
    @SuppressWarnings("unchecked")
    private void processLogicalSubDivChildren(List<Element> children, LogicalSubDiv parent) {
        for (Element logSubDiv : children) {
            LogicalSubDiv lsd = new LogicalSubDiv(logSubDiv.getAttributeValue("ID"), logSubDiv.getAttributeValue("TYPE"),
                    logSubDiv.getAttributeValue("LABEL"), Integer.valueOf(logSubDiv.getAttributeValue("ORDER")));
            parent.add(lsd);

            processLogicalSubDivChildren((List<Element>) logSubDiv.getChildren(), lsd);
        }
    }

    /**
     * Creates the mets:structMap TYPE="PHYSICAL".
     * 
     * @param source
     */
    @SuppressWarnings("unchecked")
    private void createPhysicalStructMap(Document source) throws JDOMException {
        XPath xp = XPath.newInstance("mets:mets/mets:structMap[@TYPE='PHYSICAL']/mets:div");
        xp.addNamespace(IMetsElement.METS);

        Element physDivElem = (Element) xp.selectSingleNode(source);
        String id = physDivElem.getAttributeValue("ID");
        String type = physDivElem.getAttributeValue("TYPE");

        if (id == null || type == null) {
            throw new IllegalArgumentException("ID and TYPE attribute of mets:div must not be null");
        }
        PhysicalStructMap structMap = (PhysicalStructMap) this.getStructMap(PhysicalStructMap.TYPE);
        PhysicalDiv physDivContainer = new PhysicalDiv(id, type);
        structMap.setDivContainer(physDivContainer);

        for (Element subDiv : (List<Element>) physDivElem.getChildren()) {
            PhysicalSubDiv psd = new PhysicalSubDiv(subDiv.getAttributeValue("ID"), subDiv.getAttributeValue("TYPE"),
                    Integer.parseInt(subDiv.getAttributeValue("ORDER")));

            String orderLabel = subDiv.getAttributeValue("ORDERLABEL");
            if (orderLabel != null) {
                psd.setOrderLabel(orderLabel);
            }

            String contentIDs = subDiv.getAttributeValue("CONTENTIDS");
            if (contentIDs != null) {
                psd.setContentids(contentIDs);
            }

            xp = XPath.newInstance("./mets:fptr");
            xp.addNamespace(IMetsElement.METS);

            for (Element fptrElem : (List<Element>) xp.selectNodes(subDiv)) {
                Fptr fptr = new Fptr(fptrElem.getAttributeValue("FILEID"));
                psd.add(fptr);
            }
            physDivContainer.add(psd);
        }
    }

    /**
     * Creates the mets:structLink section from the given source.
     * 
     * @param source
     */
    @SuppressWarnings("unchecked")
    private void createStuctLinks(Document source) throws JDOMException {
        XPath smLinksXP = XPath.newInstance("mets:mets/mets:structLink/mets:smLink");
        smLinksXP.addNamespace(IMetsElement.METS);

        for (Element smLink : (List<Element>) smLinksXP.selectNodes(source)) {
            String from = smLink.getAttributeValue("from", IMetsElement.XLINK);
            String to = smLink.getAttributeValue("to", IMetsElement.XLINK);
            if (from == null || to == null) {
                throw new IllegalArgumentException("xlink:from and xlink:to must not be null in mets:smLink element");
            }
            SmLink l = new SmLink(from, to);
            this.structLink.addSmLink(l);
        }
    }

    /**
     * Creates the file section from the given source document.
     * 
     * @param source
     * @throws JDOMException
     */
    @SuppressWarnings("unchecked")
    private void createFileSec(Document source) throws JDOMException {
        XPath grpXPath = XPath.newInstance("mets:mets/mets:fileSec/mets:fileGrp");
        grpXPath.addNamespace(IMetsElement.METS);

        List<Element> fGrpList = grpXPath.selectNodes(source);

        for (Element aFileGroup : fGrpList) {
            // create a fileGrp object
            String useAttribute = aFileGroup.getAttributeValue("USE");
            if (useAttribute == null) {
                throw new IllegalArgumentException("USE attribute value must not be null");
            }
            FileGrp grp = new FileGrp(useAttribute);

            // get the files to add to the file grp object
            XPath filesXP = XPath.newInstance("./mets:file");
            filesXP.addNamespace(IMetsElement.METS);

            for (Element aFile : (List<Element>) filesXP.selectNodes(aFileGroup)) {
                String id = aFile.getAttributeValue("ID");
                String mimeType = aFile.getAttributeValue("MIMETYPE");
                if (id == null || mimeType == null) {
                    throw new IllegalArgumentException("MIMETYPE or ID of a mets:file must not be null");
                }
                File f = new File(id, mimeType);
                // create the FLocat element and add it to the file element
                String locType = aFile.getChild("FLocat", IMetsElement.METS).getAttributeValue("LOCTYPE");
                String href = aFile.getChild("FLocat", IMetsElement.METS).getAttributeValue("href", IMetsElement.XLINK);

                if (locType == null || href == null) {
                    throw new IllegalArgumentException("LOCTYPE or xlink:href of a mets:FLocat must not be null");
                }
                FLocat fLoc = new FLocat(locType, href);
                f.setFLocat(fLoc);
                grp.addFile(f);
            }

            //add the new created file group to the file section
            this.fileSec.addFileGrp(grp);
        }
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
     * 
     * @return {@link Document}
     */
    public Document asDocument() {
        Document doc = new Document();
        doc.setRootElement(asElement());
        return doc;
    }

    public Element asElement() {
        Element mets = new Element("mets", IMetsElement.METS);
        mets.addNamespaceDeclaration(IMetsElement.XSI);
        mets.setAttribute("schemaLocation", IMetsElement.SCHEMA_LOC_METS, IMetsElement.XSI);

        Iterator<DmdSec> dmdSecIt = this.dmdsecs.values().iterator();
        while (dmdSecIt.hasNext()) {
            mets.addContent(dmdSecIt.next().asElement());
        }

        Iterator<AmdSec> amdSecIt = this.amdsecs.values().iterator();
        while (amdSecIt.hasNext()) {
            mets.addContent(amdSecIt.next().asElement());
        }
        if (this.getFileSec() != null) {
            mets.addContent(this.getFileSec().asElement());
        }
        for (IStructMap sM : this.structMaps.values()) {
            mets.addContent(sM.asElement());
        }
        if (this.getStructLink() != null) {
            mets.addContent(this.getStructLink().asElement());
        }
        return mets;
    }

    /**
     * @param doc
     *            the document to validate against the mets schema
     * @return true if the document is a valid mets document false otherwise
     */
    final public static boolean isValid(Document doc) {
        try {
            if (VALIDATOR == null) {
                LOGGER.warn("Validator has not been initialized. Returning 'false' as default value");
                return false;
            }
            VALIDATOR.validate(new JDOMSource(doc));
        } catch (SAXException saxEx) {
            LOGGER.error("Error parsing and validating mets document", saxEx);
            return false;
        } catch (IOException ioEx) {
            LOGGER.error("Error reading input stream", ioEx);
            return false;
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
            LOGGER.error("Error occured while validating mets document", t);
            return false;
        }

        return Mets.isValid(doc);
    }
}
