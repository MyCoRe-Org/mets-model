package org.mycore.mets.model.header;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.mycore.mets.model.IMetsElement;

/**
 * 
 * The &lt;agent&gt; element provides for various parties and 
 * their roles with respect to the METS record to be documented.
 * 
 * @see <a href=
 *      "http://www.loc.gov/standards/mets/docs/mets.v1-8.html#agent">agent</a>
 * 
 * @author Uwe Hartwig (M3ssman)
 *
 */
public class Agent implements IMetsElement {

    private String id;

    private String role;

    private String otherRole;

    private String type;

    private String otherType;

    private Name name;

    private List<Note> notes;

    /**
     * 
     * Set required Attribute "ROLE" per default to "OTHER"
     * 
     */
    public Agent() {
        this.role = "OTHER";
        this.notes = new ArrayList<>();
    }

    /**
     * Attribute "ROLE" is required only
     *
     * @param role the ROLE attribute value
     * @param name the agent name element
     */
    public Agent(String role, Name name) {
        this.role = role;
        this.name = name;
        this.notes = new ArrayList<>();
    }

    /**
     * Returns the agent identifier.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the agent identifier.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the agent type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the agent type.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the role of this agent.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of this agent.
     *
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the other role attribute value.
     *
     * @return the otherRole
     */
    public String getOtherRole() {
        return otherRole;
    }

    /**
     * Sets the other role attribute value.
     *
     * @param otherRole the otherRole to set
     */
    public void setOtherRole(String otherRole) {
        this.otherRole = otherRole;
    }

    /**
     * Returns the other type attribute value.
     *
     * @return the otherType
     */
    public String getOtherType() {
        return otherType;
    }

    /**
     * Sets the other type attribute value.
     *
     * @param otherType the otherType to set
     */
    public void setOtherType(String otherType) {
        this.otherType = otherType;
    }

    /**
     * Returns the name of this agent.
     *
     * @return the name
     */
    public Name getName() {
        return name;
    }

    /**
     * Sets the name of this agent.
     *
     * @param name the name to set
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * Returns the notes associated with this agent.
     *
     * @return the list of notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Sets the notes associated with this agent.
     *
     * @param notes the list of notes to set
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mycore.mets.model.IMetsElement#asElement()
     */
    @SuppressWarnings("exports")
    @Override
    public Element asElement() {
        Element agent = new Element("agent", IMetsElement.METS);
        if (getRole() != null) {
            agent.setAttribute("ROLE", getRole());
        }
        if (getOtherRole() != null) {
            agent.setAttribute("OTHERROLE", getOtherRole());
        }
        if (this.getType() != null) {
            agent.setAttribute("TYPE", this.getType());
        }
        if (getOtherType() != null) {
            agent.setAttribute("OTHERTYPE", getOtherType());
        }
        if (getId() != null) {
            agent.setAttribute("ID", getId());
        }
        if (getName() != null) {
            agent.addContent(getName().asElement());
        }
        if (getNotes() != null && !getNotes().isEmpty()) {
            for (Note note : getNotes()) {
                agent.addContent(note.asElement());
            }
        }
        return agent;
    }

}
