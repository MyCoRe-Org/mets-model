package org.mycore.mets.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mycore.mets.model.header.Agent;
import org.mycore.mets.model.header.MetsHdr;
import org.mycore.mets.model.header.Name;
import org.mycore.mets.model.header.Note;

/**
 * 
 * @author Uwe Hartwig (M3ssman)
 *
 */
public class MetsProcessResourceTest {

    @ClassRule
    @SuppressWarnings("exports")
    public static TemporaryFolder tempDir = new TemporaryFolder();

    static String sourcePath = "src/test/resources/urn:nbn:de:gbv:3:3-21437.xml";

    static Path targetPath;

    static Mets mets;

    @BeforeClass
    public static void beforeClass() throws Exception {

        targetPath = Path.of(tempDir.getRoot().getAbsolutePath(), "mets.xml");
        Files.copy(Path.of(sourcePath), targetPath);
        mets = extractMets(targetPath.toString());
    }

    private static Mets extractMets(String targetPath) throws JDOMException, IOException {
        File f = new File(targetPath);
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(f);
        return new Mets(document);
    }

    @Test
    public void testMetsDefaultStructureElementsPresent() {
        assertNotNull(mets);
        assertNotNull(mets.getAmdSections());
        assertEquals(1, mets.getAmdSections().length);
        assertNotNull(mets.getDmdSections());
        assertEquals(1, mets.getDmdSections().length);
        assertNotNull(mets.getFileSec());
        assertEquals(5, mets.getFileSec().getFileGroups().size());
        assertNotNull(mets.getLogicalStructMap());
        assertNotNull(mets.getLogicalStructMap().getDivContainer());
        assertNotNull(mets.getLogicalStructMap().getDivContainer().getChildren());
        assertEquals(3, mets.getLogicalStructMap().getDivContainer().getChildren().size());
        assertNotNull(mets.getPhysicalStructMap());
        assertNotNull(mets.getPhysicalStructMap().getDivContainer());
        assertNotNull(mets.getPhysicalStructMap().getDivContainer().getChildren());
        assertEquals(4, mets.getPhysicalStructMap().getDivContainer().getChildren().size());
        assertNotNull(mets.getStructLink());
        assertNotNull(mets.getStructLink().getSmLinks());
        assertEquals(4, mets.getStructLink().getSmLinks().size());
    }

    @Test
    public void testOriginalMetsHdrPresent() {
        assertNotNull(mets.getMetsHdr());
    }

    @Test
    public void testOrignalHetsHdrAgentsPresent() {
        MetsHdr metsHdr = mets.getMetsHdr();
        assertNotNull(metsHdr.getAgents());
        assertEquals(4, metsHdr.getAgents().size());
    }

    @Test
    public void testOriginalHetsHdrAgentDetails() {
        MetsHdr metsHdr = mets.getMetsHdr();
        assertNotNull(metsHdr.getAgents());
        assertNotNull(metsHdr.getAgents().get(0).getName());
        assertEquals("vls/2.12.1", metsHdr.getAgents().get(0).getName().getText());
        assertNotNull(metsHdr.getAgents().get(1).getName());
        assertEquals("ulbhal-hspe", metsHdr.getAgents().get(1).getName().getText());
        assertNotNull(metsHdr.getAgents().get(2).getName());
        assertEquals("digital.bibliothek.uni-halle.de/hd", metsHdr.getAgents().get(2).getName().getText());
        assertNotNull(metsHdr.getAgents().get(3).getName());
        assertEquals("vd", metsHdr.getAgents().get(3).getName().getText());
    }

    @Test
    public void testAddNewMetsHdrAgent() throws Exception {
        MetsHdr metsHdr = mets.getMetsHdr();
        assertEquals(4, metsHdr.getAgents().size());

        // act
        Agent agent = new Agent("OTHER", new Name("digitalDerivans"));
        agent.setNotes(List.of(new Note("created for test purposes")));
        metsHdr.getAgents().add(agent);
        final Instant nowI = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        testWithDate(metsHdr, "2021-03-17T15:21:26");
        testWithDate(metsHdr, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
    }

    private void testWithDate(MetsHdr metsHdr, String now) throws IOException, JDOMException {
        metsHdr.setLastModDate(now);

        // assert
        assertEquals(5, metsHdr.getAgents().size());

        try (OutputStream metsOut = Files.newOutputStream(targetPath)) {
            XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
            xout.output(mets.asDocument(), metsOut);
        }

        // re-act: check datetime serialization
        Mets mets = extractMets(targetPath.toString());
        assertEquals(5, mets.getMetsHdr().getAgents().size());
        String serializedLastModifiedDate = mets.getMetsHdr().getLastModDate().toString();
        assertEquals(now, serializedLastModifiedDate);
    }

}
