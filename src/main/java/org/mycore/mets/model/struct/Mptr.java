/**
 * 
 */
package org.mycore.mets.model.struct;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Represents a mets:mptr (METS pointer) element that references an external METS document.
 *
 * @author silvio
 */
public class Mptr implements IMetsElement {

    /**
     * the href attribute of this element
     */
    private final String href;

    /**
     * the {@link LOCTYPE} attribute of the element
     */
    private final LOCTYPE loctype;

    /**
     * Creates a new Mptr with the given href and location type.
     *
     * @param href the xlink:href attribute value
     * @param loc  the location type
     */
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
