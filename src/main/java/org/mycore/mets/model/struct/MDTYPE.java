package org.mycore.mets.model.struct;

/**
 * Enumeration of supported MDTYPE attribute values for mets:mdWrap and mets:mdRef elements.
 */
public enum MDTYPE {
    /** Dublin Core metadata. */
    DC,
    /** Data Documentation Initiative metadata. */
    DDI,
    /** Encoded Archival Description metadata. */
    EAD,
    /** Federal Geographic Data Committee metadata. */
    FGDC,
    /** Library of Congress Audiovisual metadata. */
    LC_AV,
    /** Learning Object Metadata. */
    LOM,
    /** Machine-Readable Cataloging metadata. */
    MARC,
    /** Metadata Object Description Schema. */
    MODS,
    /** NISO Imaging metadata. */
    NISOIMG,
    /** Other metadata type. */
    OTHER,
    /** PREMIS preservation metadata. */
    PREMIS,
    /** TEI header metadata. */
    TEIHDR,
    /** Visual Resources Association metadata. */
    VRA
}
