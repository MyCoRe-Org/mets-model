package org.mycore.mets.model.struct;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * 
 * @author Matthias Eichner
 */
public class Seq implements IMetsElement {

    private List<Area> areaList;

    public Seq() {
        this.areaList = new ArrayList<Area>();
    }

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
