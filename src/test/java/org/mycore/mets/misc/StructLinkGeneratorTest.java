package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mycore.mets.model.struct.Fptr;
import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.PhysicalDiv;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.PhysicalSubDiv;
import org.mycore.mets.model.struct.SmLink;
import org.mycore.mets.model.struct.StructLink;

import static org.junit.Assert.assertEquals;

public class StructLinkGeneratorTest {

    PhysicalStructMap physicalStructMap;

    LogicalStructMap logicalStructMap;

    LogicalDiv root, a1, a2, a3, b1, b2, b3, b4, b5;

    @Before
    public void before() {
        // physical
        physicalStructMap = new PhysicalStructMap();
        PhysicalDiv divContainer = new PhysicalDiv();
        physicalStructMap.setDivContainer(divContainer);
        for (int i = 0; i < 10; i++) {
            addPage(divContainer, i);
        }
        // logical
        logicalStructMap = new LogicalStructMap();
        this.root = new LogicalDiv("root", "journal", "root");
        this.a1 = new LogicalDiv("a1", "volume", "a1");
        this.a2 = new LogicalDiv("a2", "volume", "a2");
        this.a3 = new LogicalDiv("a3", "volume", "a3");
        this.b1 = new LogicalDiv("b1", "article", "b1");
        this.b2 = new LogicalDiv("b2", "article", "b2");
        this.b3 = new LogicalDiv("b3", "article", "b3");
        this.b4 = new LogicalDiv("b4", "article", "b4");
        this.b5 = new LogicalDiv("b5", "article", "b5");

        this.root.add(a1);
        this.root.add(a2);
        this.root.add(a3);

        this.a1.add(b1);
        this.a1.add(b2);
        this.a2.add(b3);
        this.a2.add(b4);
        this.a3.add(b5);

        logicalStructMap.setDivContainer(this.root);
    }

    private void addPage(PhysicalDiv divContainer, Integer pos) {
        PhysicalSubDiv page = new PhysicalSubDiv("phys_" + pos, "page");
        Fptr fptr = new Fptr("MASTER_" + pos);
        page.add(fptr);
        divContainer.add(page);
    }

    private PhysicalSubDiv getDiv(Integer pos) {
        return physicalStructMap.getDivContainer().getChildren().get(pos);
    }

    private List<PhysicalSubDiv> asList(PhysicalSubDiv... divs) {
        return new ArrayList<>(Arrays.asList(divs));
    }

    private void check(StructLink structLink, LogicalDiv div, String... expected) {
        List<SmLink> links = structLink.getSmLinkByFrom(div.getId());
        assertEquals(expected.length, links.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], links.get(i).getTo());
        }
    }

    @Test
    public void generate() {
        Map<LogicalDiv, List<PhysicalSubDiv>> linkedMap = new HashMap<>();
        linkedMap.put(this.b1, asList(getDiv(2)));
        linkedMap.put(this.b2, asList(getDiv(5)));
        linkedMap.put(this.b3, asList(getDiv(8)));
        linkedMap.put(this.b4, asList(getDiv(8), getDiv(9)));

        DefaultStructLinkGenerator generator = new DefaultStructLinkGenerator(linkedMap);
        StructLink structLink = generator.generate(physicalStructMap, logicalStructMap);

        check(structLink, root, "phys_0", "phys_1");
        check(structLink, a1, "phys_2");
        check(structLink, b1, "phys_2", "phys_3", "phys_4");
        check(structLink, b2, "phys_5", "phys_6", "phys_7");
        check(structLink, a2, "phys_8");
        check(structLink, b3, "phys_8");
        check(structLink, b4, "phys_8", "phys_9");
        check(structLink, a3, "phys_9");
        check(structLink, b5, "phys_9");
    }

}
