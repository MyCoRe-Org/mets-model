package org.mycore.mets.model.struct;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.jdom2.Element;

/**
 * Represents the root mets:div element within the physical structural map of a METS document.
 */
public class PhysicalDiv extends AbstractDiv<PhysicalSubDiv> {

    /** Default type value for the physical sequence div. */
    public final static String TYPE_PHYS_SEQ = "physSequence";

    private final HashMap<String, PhysicalSubDiv> physicalSubDivContainer;

    /**
     * Creates a new PhysicalDiv with no id and default type {@link #TYPE_PHYS_SEQ}.
     */
    public PhysicalDiv() {
        this(null, TYPE_PHYS_SEQ);
    }

    /**
     * Creates a new PhysicalDiv with the given id and type.
     *
     * @param id   the identifier of this div
     * @param type the type attribute value
     */
    public PhysicalDiv(String id, String type) {
        this.id = id;
        this.type = type;
        this.physicalSubDivContainer = new LinkedHashMap<>();
    }

    @Override
    public void add(PhysicalSubDiv element) {
        if (element == null) {
            return;
        }
        element.setParent(this);
        this.physicalSubDivContainer.put(element.getId(), element);
    }

    @Override
    public void remove(PhysicalSubDiv element) {
        this.physicalSubDivContainer.remove(element.getId());
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

    /**
     * Returns the physical sub-div with the given identifier, or null if not found.
     *
     * @param id the identifier to look up
     * @return the matching {@link PhysicalSubDiv} or null
     */
    public PhysicalSubDiv get(String id) {
        return this.physicalSubDivContainer.get(id);
    }

    /**
     * Returns the first {@link PhysicalSubDiv} which contains the given FILEID.
     * If no div can be found, null is returned.
     *
     * @param fileId the file id
     * @return list of physical sub div'S
     */
    public PhysicalSubDiv byFileId(String fileId) {
        return this.physicalSubDivContainer.values().stream()
            .filter(subDiv -> subDiv.getChildren().stream().anyMatch(fptr -> fptr.getFileId().equals(fileId)))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<PhysicalSubDiv> getChildren() {
        return new Vector<>(physicalSubDivContainer.values());
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
