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
package org.mycore.mets.model.struct;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * Implementation for the mets:structLink element in a mets document.
 * 
 * @author Silvio Hermann (shermann)
 */
public class StructLink implements IMetsElement {

    private HashMap<String, SmLink> links;

    /**
     * Creates a new StructLink section.
     */
    public StructLink() {
        links = new LinkedHashMap<String, SmLink>();
    }

    /**
     * Adds a {@link SmLink} to this struct link section.
     * 
     * @param smLink the SmLink to add
     */
    public void addSmLink(SmLink smLink) {
        String from = smLink.getFrom();
        String to = smLink.getTo();

        if (from == null || from.length() == 0 || to == null || to.length() == 0) {
            return;
        }

        this.links.put(getKey(from, to), smLink);
    }

    /**
     * Removes a {@link SmLink} from this struct link section.
     * 
     * @param smLink
     *            the {@link SmLink} to remove
     */
    public void removeSmLink(SmLink smLink) {
        this.links.remove(getKey(smLink.getFrom(), smLink.getTo()));
    }

    /**
     * Removes the {@link StructLink} given by its from and to attribute.
     * 
     * @param from
     *            the from attribute of the smlink (not null)
     * @param to
     *            the to attribute of the smlink (not null)
     */
    public void removeSmLink(String from, String to) {
        if (from == null || from.length() == 0 || to == null || to.length() == 0) {
            return;
        }
        this.links.remove(getKey(from, to));
    }

    /**
     * Returns the smlink key of the {@link #links} hashmap.
     * 
     * @param from
     *            the from attribute of the smlink (not null)
     * @param to
     *            the to attribute of the smlink (not null)
     * @return a generated key of the from and to values
     */
    protected String getKey(String from, String to) {
        StringBuilder key = new StringBuilder();
        key.append(from).append("#").append(to);
        return key.toString();
    }

    /**
     * @param from from attribute
     * @return all {@link SmLink} where the from attribute matches the value of
     *         the parameter
     */
    public List<SmLink> getSmLinkByFrom(String from) {
        Vector<SmLink> result = new Vector<SmLink>();
        if (from == null || from.length() == 0) {
            return result;
        }

        for (SmLink l : links.values()) {
            if (l.getFrom().equals(from)) {
                result.add(l);
            }
        }

        return result;
    }

    /**
     * @param to to attribute
     * @return all {@link SmLink} where the to attribute matches the value of
     *         the parameter
     */
    public List<SmLink> getSmLinkByTo(String to) {
        Vector<SmLink> result = new Vector<SmLink>();
        if (to == null || to.length() == 0) {
            return result;
        }

        for (SmLink l : links.values()) {
            if (l.getTo().equals(to)) {
                result.add(l);
            }
        }

        return result;
    }

    /**
     * @return all {@link SmLink} within this {@link StructLink} as a
     *         {@link List}
     */
    public List<SmLink> getSmLinks() {
        return new Vector<SmLink>(links.values());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    public Element asElement() {
        Element structLink = new Element("structLink", IMetsElement.METS);

        Iterator<SmLink> iterator = links.values().iterator();
        while (iterator.hasNext()) {
            structLink.addContent(iterator.next().asElement());
        }
        return structLink;
    }
}
