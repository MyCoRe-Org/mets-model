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
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.util.XMLCatalogResolver;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.transform.JDOMSource;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Silvio Hermann (shermann)
 */
public class Mets {

    // thread-safe
    private static final XPathFactory XPATH_FACTORY = XPathFactory.instance();

    // thread-safe
    private static final Logger LOGGER = LogManager.getLogger(Mets.class);

    // thread-safe
    private static Schema SCHEMA;

    static {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        XMLCatalogResolver catalogResolver = getCatalogResolver();
        schemaFactory.setResourceResolver(catalogResolver);
            Source metsSchemaSource;
            try {
                metsSchemaSource = getMetsSchema(catalogResolver);
                LOGGER.info("Loading METS XML Schema from: " + metsSchemaSource.getSystemId());
                SCHEMA = schemaFactory.newSchema(metsSchemaSource);
            } catch (SAXException | IOException e) {
                LOGGER.error("Could not load METS XML Schema.");
                e.printStackTrace();
            }
    }

    private static Source getMetsSchema(XMLCatalogResolver catalogResolver) throws SAXException, IOException {
        InputSource resolvedSchema = catalogResolver.resolveEntity(null, "http://www.loc.gov/standards/mets/mets.xsd");
        StreamSource metsSchemaSource = new StreamSource(resolvedSchema.getSystemId());
        return metsSchemaSource;
    }

    private static XMLCatalogResolver getCatalogResolver() {
        Enumeration<URL> systemResources;
        try {
            systemResources = Mets.class.getClassLoader().getResources("catalog.xml");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        Vector<String> catalogURIs = new Vector<>();
        while (systemResources.hasMoreElements()) {
            URL catalogURL = systemResources.nextElement();
            LOGGER.info("Using XML catalog: " + catalogURL);
            catalogURIs.add(catalogURL.toString());
        }
        String[] catalogs = catalogURIs.toArray(new String[catalogURIs.size()]);
        return new XMLCatalogResolver(catalogs);
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

    private void createDmdSec(Document source) throws JDOMException {
        XPathExpression<Element> xp = getXpathExpression("mets:mets/mets:dmdSec");

        for (Element section : xp.evaluate(source)) {
            DmdSec dmdSec = new DmdSec(section.getAttributeValue("ID"));
            dmdsecs.put(dmdSec.getId(), dmdSec);

            // handle mdWrap
            xp = getXpathExpression("mets:mdWrap");

            for (Element wrap : xp.evaluate(section)) {
                XPathExpression<Element> xmlDataXP = getXpathExpression("mets:xmlData/*");
                Element element = xmlDataXP.evaluateFirst(wrap);
                MdWrap mdWrap = new MdWrap(MdWrapSection.findTypeByName(wrap.getAttributeValue("MDTYPE")),
                    (Element) element.clone());
                dmdSec.setMdWrap(mdWrap);
            }

            // handle mdRef
            xp = getXpathExpression("mets:mdRef");

            for (Element refElem : xp.evaluate(section)) {
                LOCTYPE loctype = LOCTYPE.valueOf(refElem.getAttributeValue("LOCTYPE"));
                String mimetype = refElem.getAttributeValue("MIMETYPE");
                MDTYPE mdtype = MdWrapSection.findTypeByName(refElem.getAttributeValue("MDTYPE"));
                String href = refElem.getAttributeValue("href", IMetsElement.XLINK);
                MdRef mdRef = new MdRef(href, loctype, mimetype, mdtype, refElem.getText());
                dmdSec.setMdRef(mdRef);
            }
        }
    }

    private void createAmdSec(Document source) throws JDOMException {
        XPathExpression<Element> xp = getXpathExpression("mets:mets/mets:amdSec");

        for (Element section : xp.evaluate(source)) {
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
        XPathExpression<Element> rightsMdXP = getXpathExpression("mets:" + flag);

        Element flagElement = rightsMdXP.evaluateFirst(section);
        if (flagElement != null) {
            String id = flagElement.getAttributeValue("ID");
            RightsMD rightsMd = new RightsMD(id);
            amdSec.setRightsMD(rightsMd);

            XPathExpression<Element> mdWrapXP = getXpathExpression("mets:mdWrap");

            flagElement = mdWrapXP.evaluateFirst(flagElement);
            if (flagElement instanceof Element) {
                String name = flagElement.getAttributeValue("MDTYPE");
                XPathExpression<Element> xmlDataXP = getXpathExpression("mets:xmlData/*");
                Element element = xmlDataXP.evaluateFirst(flagElement);
                MdWrap mdWrap = new MdWrap(MdWrapSection.findTypeByName(name), (Element) element.clone());
                mdWrap.setOtherMdType(flagElement.getAttributeValue("OTHERMDTYPE"));
                rightsMd.setMdWrap(mdWrap);
            }
        }
    }

    /**
     * Creates the mets:structMap TYPE="LOGICAL".
     * 
     * @param source
     */
    private void createLogicalStructMap(Document source) throws JDOMException {
        XPathExpression<Element> xp = getXpathExpression("mets:mets/mets:structMap[@TYPE='LOGICAL']/mets:div");

        Element logDivContainerElem = xp.evaluateFirst(source);

        LogicalDiv logDivContainer = new LogicalDiv(logDivContainerElem.getAttributeValue("ID"),
            logDivContainerElem.getAttributeValue("TYPE"), logDivContainerElem.getAttributeValue("LABEL"),
            Integer.valueOf(logDivContainerElem.getAttributeValue("ORDER")),
            logDivContainerElem.getAttributeValue("ADMID"), logDivContainerElem.getAttributeValue("DMDID"));

        for (Element logSubDiv : (List<Element>) logDivContainerElem.getChildren()) {
            LogicalSubDiv lsd = new LogicalSubDiv(logSubDiv.getAttributeValue("ID"),
                logSubDiv.getAttributeValue("TYPE"), logSubDiv.getAttributeValue("LABEL"), Integer.valueOf(logSubDiv
                    .getAttributeValue("ORDER")));
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
    private void processLogicalSubDivChildren(List<Element> children, LogicalSubDiv parent) {
        for (Element child : children) {
            if(!child.getName().equals("div")) {
                return;
            }
            LogicalSubDiv lsd = new LogicalSubDiv(child.getAttributeValue("ID"),
                child.getAttributeValue("TYPE"), child.getAttributeValue("LABEL"), Integer.valueOf(child
                    .getAttributeValue("ORDER")));
            parent.add(lsd);

            processLogicalSubDivChildren((List<Element>) child.getChildren(), lsd);
        }
    }

    /**
     * Creates the mets:structMap TYPE="PHYSICAL".
     * 
     * @param source
     */
    private void createPhysicalStructMap(Document source) throws JDOMException {
        XPathExpression<Element> xp = getXpathExpression("mets:mets/mets:structMap[@TYPE='PHYSICAL']/mets:div");

        Element physDivElem = xp.evaluateFirst(source);
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

            xp = getXpathExpression("./mets:fptr");

            for (Element fptrElem : xp.evaluate(subDiv)) {
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
    private void createStuctLinks(Document source) throws JDOMException {
        XPathExpression<Element> smLinksXP = getXpathExpression("mets:mets/mets:structLink/mets:smLink");

        for (Element smLink : smLinksXP.evaluate(source)) {
            String from = smLink.getAttributeValue("from", IMetsElement.XLINK);
            String to = smLink.getAttributeValue("to", IMetsElement.XLINK);
            if (from == null || to == null) {
                throw new IllegalArgumentException("xlink:from and xlink:to must not be null in mets:smLink element");
            }
            SmLink l = new SmLink(from, to);
            this.structLink.addSmLink(l);
        }
    }

    private static XPathExpression<Element> getXpathExpression(String xpath) {
        return XPATH_FACTORY.compile(xpath, Filters.element(), null, IMetsElement.METS);
    }

    /**
     * Creates the file section from the given source document.
     * 
     * @param source
     * @throws JDOMException
     */
    private void createFileSec(Document source) throws JDOMException {
        XPathExpression<Element> grpXPath = getXpathExpression("mets:mets/mets:fileSec/mets:fileGrp");

        for (Element aFileGroup : grpXPath.evaluate(source)) {
            // create a fileGrp object
            String useAttribute = aFileGroup.getAttributeValue("USE");
            if (useAttribute == null) {
                throw new IllegalArgumentException("USE attribute value must not be null");
            }
            FileGrp grp = new FileGrp(useAttribute);

            // get the files to add to the file grp object
            XPathExpression<Element> filesXP = getXpathExpression("./mets:file");

            for (Element aFile : filesXP.evaluate(aFileGroup)) {
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
                FLocat fLoc = new FLocat(LOCTYPE.valueOf(locType), href);
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
        Validator validator = SCHEMA.newValidator();
        try {
            validator.validate(new JDOMSource(doc));
            return true;
        } catch (SAXException saxEx) {
            LOGGER.error("Error parsing and validating mets document", saxEx);
        } catch (IOException ioEx) {
            LOGGER.error("Error reading input stream", ioEx);
        }
        return false;
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
