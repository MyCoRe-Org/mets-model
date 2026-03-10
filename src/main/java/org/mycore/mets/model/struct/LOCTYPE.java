package org.mycore.mets.model.struct;

/**
 * Enumeration of supported LOCTYPE attribute values for mets:FLocat elements.
 */
public enum LOCTYPE {
    /** Uniform Resource Name. */
    URN,
    /** Uniform Resource Locator. */
    URL,
    /** Persistent Uniform Resource Locator. */
    PURL,
    /** Handle identifier. */
    HANDLE,
    /** Digital Object Identifier. */
    DOI,
    /** Other location type. */
    OTHER
}
