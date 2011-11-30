package org.mycore.mets.model.struct;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

public class PhysicalDiv extends AbstractDiv<PhysicalSubDiv> {

    public final static String TYPE_PHYS_SEQ = "physSequence";

    private HashMap<String, PhysicalSubDiv> physicalSubDivContainer;

    public PhysicalDiv(String id, String type) {
        this.id = id;
        this.type = type;
        this.physicalSubDivContainer = new LinkedHashMap<String, PhysicalSubDiv>();
    }

    @Override
    public void add(PhysicalSubDiv element) {
        if (element == null) {
            return;
        }
        this.physicalSubDivContainer.put(element.getId(), element);
    }

    @Override
    public void remove(PhysicalSubDiv element) {
        this.physicalSubDivContainer.remove(element);
    }

    /**
     * Remove a {@link PhysicalSubDiv} by its id.
     * 
     * @param id
     *            the id of the {@link PhysicalSubDiv} to remove
     */
    public void remove(String id) {
        this.physicalSubDivContainer.remove(id);
    }

    @Override
    public List<PhysicalSubDiv> getChildren() {
        return new Vector<PhysicalSubDiv>(physicalSubDivContainer.values());
    }

    @Override
    public Element asElement() {
        Element div = super.asElement();
        for (PhysicalSubDiv subDiv : physicalSubDivContainer.values()) {
            div.addContent(subDiv.asElement());
        }
        return div;
    }

}
