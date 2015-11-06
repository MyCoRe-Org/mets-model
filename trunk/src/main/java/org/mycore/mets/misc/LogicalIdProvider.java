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
package org.mycore.mets.misc;

/**
 * @author Silvio Hermann (shermann)
 */
public class LogicalIdProvider {

    private String prefix;

    private int counter;

    private int digits;

    /**
     * @param idPrefix
     *            the prefix of the generated identifiers
     * @param digits
     *            the amount of digits to use
     */
    public LogicalIdProvider(String idPrefix, int digits) {
        this.prefix = idPrefix;
        this.counter = 0;
        this.digits = digits;
    }

    /**
     * @return the prefix to be used for the generated logical identifiers
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * @return the next identifier
     */
    public String getNextId() {
        StringBuilder id = new StringBuilder(this.prefix);
        counter++;
        int len = String.valueOf(counter).length();

        int loopLen = this.digits - len;
        for (int i = 0; i < loopLen; i++) {
            id.append("0");
        }
        id.append(counter);
        return id.toString();
    }
}