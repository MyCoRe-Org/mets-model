module org.mycore.mets.model {
    requires jdom2;
    requires java.xml;
    requires org.apache.logging.log4j;
    exports org.mycore.mets.misc;
    exports org.mycore.mets.model;
    exports org.mycore.mets.model.files;
    exports org.mycore.mets.model.header;
    exports org.mycore.mets.model.sections;
    exports org.mycore.mets.model.struct;
}
