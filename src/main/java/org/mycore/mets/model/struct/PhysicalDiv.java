package org.mycore.mets.model.struct;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class PhysicalDiv extends AbstractDiv<PhysicalSubDiv> {

    public final static String TYPE_PHYS_SEQ = "physSequence";

    private List<PhysicalSubDiv> physicalSubDivList;

    public PhysicalDiv(String id, String type) {
        this.id = id;
        this.type = type;
        this.physicalSubDivList = new ArrayList<PhysicalSubDiv>();
    }

    @Override
    public void add(PhysicalSubDiv element) {
        this.physicalSubDivList.add(element);
    }
    @Override
    public void remove(PhysicalSubDiv element) {
        this.physicalSubDivList.remove(element);
    }
    @Override
    public List<PhysicalSubDiv> getChildren() {
        return this.physicalSubDivList;
    }

    @Override
    public Element asElement() {
        Element div = super.asElement();
        for(PhysicalSubDiv subDiv : physicalSubDivList) {
            div.addContent(subDiv.asElement());
        }
        return div;
    }

}
