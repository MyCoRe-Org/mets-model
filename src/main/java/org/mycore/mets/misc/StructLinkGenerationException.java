package org.mycore.mets.misc;

/**
 * Struct link generation failed.
 *
 * @author Matthias Eichner
 */
public class StructLinkGenerationException extends RuntimeException {

    /**
     * Constructs a new struct link generation exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public StructLinkGenerationException(String message) {
        super(message);
    }

}
