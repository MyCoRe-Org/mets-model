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

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author Silvio Hermann (shermann)
 */
public class AmdSec extends MdSection {

    private TechMD techMD;

    private RightsMD rightsMD;

    private SourceMD sourceMD;

    private DigiprovMD digiprovMD;

    public TechMD getTechMD() {
        return techMD;
    }

    public void setTechMD(TechMD techMD) {
        this.techMD = techMD;
    }

    public RightsMD getRightsMD() {
        return rightsMD;
    }

    public void setRightsMD(RightsMD rightsMD) {
        this.rightsMD = rightsMD;
    }

    public SourceMD getSourceMD() {
        return sourceMD;
    }

    public void setSourceMD(SourceMD sourceMD) {
        this.sourceMD = sourceMD;
    }

    public DigiprovMD getDigiprovMD() {
        return digiprovMD;
    }

    public void setDigiprovMD(DigiprovMD digiprovMD) {
        this.digiprovMD = digiprovMD;
    }

    /**
     * @param id
     *            the ID of the section
     */
    public AmdSec(String id) {
        super(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element amdSec = new Element("amdSec", IMetsElement.METS);
        amdSec.setAttribute("ID", getId());
        if(getRightsMD() != null) {
            amdSec.addContent(getRightsMD().asElement());
        }
        if(getSourceMD() != null) {
            amdSec.addContent(getSourceMD().asElement());
        }
        if(getTechMD() != null) {
            amdSec.addContent(getTechMD().asElement());
        }
        if(getDigiprovMD() != null) {
            amdSec.addContent(getDigiprovMD().asElement());
        }
        return amdSec;
    }
}
