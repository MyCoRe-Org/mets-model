/* $Revision$ 
 * $Date$ 
 * $LastChangedBy$
 * Copyright 2010 - Thüringer Universitäts- und Landesbibliothek Jena
 *  
 * Mets-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mets-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mets-Editor.  If not, see http://www.gnu.org/licenses/.
 */
package org.mycore.mets.model.sections;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 */
public class AmdSec extends MdSection {

    private List<TechMD> techMD;

    private List<RightsMD> rightsMD;

    private List<SourceMD> sourceMD;

    private List<DigiprovMD> digiprovMD;

    public AmdSec(String id) {
        super(id);
        this.techMD = new ArrayList<>();
        this.rightsMD = new ArrayList<>();
        this.sourceMD = new ArrayList<>();
        this.digiprovMD = new ArrayList<>();
    }

    public TechMD getTechMD() {
        return techMD.isEmpty() ? null : techMD.get(0);
    }

    public List<TechMD> listTechMD() {
        return this.techMD;
    }

    public void setTechMD(TechMD techMD) {
        this.techMD.clear();
        this.techMD.add(techMD);
    }

    public RightsMD getRightsMD() {
        return rightsMD.isEmpty() ? null : rightsMD.get(0);
    }

    public List<RightsMD> listRightsMD() {
        return this.rightsMD;
    }

    public void setRightsMD(RightsMD rightsMD) {
        this.rightsMD.clear();
        this.rightsMD.add(rightsMD);
    }

    public SourceMD getSourceMD() {
        return sourceMD.isEmpty() ? null : sourceMD.get(0);
    }

    public List<SourceMD> listSourceMD() {
        return this.sourceMD;
    }

    public void setSourceMD(SourceMD sourceMD) {
        this.sourceMD.clear();
        this.sourceMD.add(sourceMD);
    }

    public DigiprovMD getDigiprovMD() {
        return digiprovMD.isEmpty() ? null : digiprovMD.get(0);
    }

    public List<DigiprovMD> listDigiprovMD() {
        return this.digiprovMD;
    }

    public void setDigiprovMD(DigiprovMD digiprovMD) {
        this.digiprovMD.clear();
        this.digiprovMD.add(digiprovMD);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element amdSec = new Element("amdSec", IMetsElement.METS);
        amdSec.setAttribute("ID", getId());
        rightsMD.stream().map(RightsMD::asElement).forEach(amdSec::addContent);
        sourceMD.stream().map(SourceMD::asElement).forEach(amdSec::addContent);
        techMD.stream().map(TechMD::asElement).forEach(amdSec::addContent);
        digiprovMD.stream().map(DigiprovMD::asElement).forEach(amdSec::addContent);
        return amdSec;
    }

}
