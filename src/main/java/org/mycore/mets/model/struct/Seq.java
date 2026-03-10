package org.mycore.mets.model.struct;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Represents a mets:seq element that groups a sequence of area elements within a file pointer.
 *
 * @author Matthias Eichner
 */
public class Seq implements IMetsElement {

    private final List<Area> areaList;

    /**
     * Creates a new empty Seq instance.
     */
    public Seq() {
        this.areaList = new ArrayList<Area>();
    }

    /**
     * Returns the modifiable list of area elements in this sequence.
     *
     * @return the list of area elements
     */
    public List<Area> getAreaList() {
        return areaList;
    }

    @Override
    public Element asElement() {
        Element seq = new Element("seq", IMetsElement.METS);
        for (Area area : areaList) {
            seq.addContent(area.asElement());
        }
        return seq;
    }

}
