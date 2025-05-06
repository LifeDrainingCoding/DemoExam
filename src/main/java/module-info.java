module com.lifedrained.demoexam {
    requires jakarta.persistence;
    requires jakarta.transaction;
    requires jakarta.xml.bind;
    requires net.bytebuddy;

    requires jakarta.cdi;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.hibernate.orm.core;
    requires com.fasterxml.classmate;
    requires org.hibernate.commons.annotations;
    requires transitive org.checkerframework.checker.qual;
    requires transitive java.logging;
    requires java.sql; // Критически важно!

    requires java.desktop;
    requires transitive org.jboss.logging;
    requires org.apache.commons.io;
    requires mysql.connector.j;
    requires org.apache.poi.poi;
    requires org.apache.commons.csv;
    requires org.apache.commons.lang3;
    requires org.apache.xmlbeans;


    opens com.lifedrained.demoexam to javafx.fxml;
    exports com.lifedrained.demoexam;
    exports com.lifedrained.demoexam.entity;
    exports com.lifedrained.demoexam.tempdata;

    opens com.lifedrained.demoexam.entity to javafx.fxml , org.hibernate.orm.core;
    exports com.lifedrained.demoexam.entity.repo;
    opens com.lifedrained.demoexam.entity.repo to javafx.fxml;
    exports com.lifedrained.demoexam.utils;
    opens com.lifedrained.demoexam.utils to javafx.fxml, org.hibernate.orm.core;
    exports com.lifedrained.demoexam.controller;
    opens com.lifedrained.demoexam.controller to javafx.fxml;

}