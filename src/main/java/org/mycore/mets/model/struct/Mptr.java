/**
 * 
 */
package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * @author silvio
 *
 */
public class Mptr implements IMetsElement {

    /**
     * the href attribute of this element
     */
    private String href;

    /**
     * the {@link LOCTYPE} attribute of the element
     */
    private LOCTYPE loctype;

    public Mptr(String href, LOCTYPE loc) {
        this.href = href;
        this.loctype = loc;
    }

    @Override
    public Element asElement() {
        Element mptr = new Element("mptr", IMetsElement.METS);

        if (this.loctype != null) {
            mptr.setAttribute("LOCTYPE", String.valueOf(loctype));
        }
        if (this.href != null) {
            mptr.setAttribute("href", href, XLINK);
        }

        return mptr;
    }
}
