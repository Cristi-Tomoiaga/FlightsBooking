module map.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens map.project to javafx.fxml;
    exports map.project;
    exports map.project.controller;
    opens map.project.controller to javafx.fxml;

    exports map.project.domain;
    exports map.project.domain.dto;
    exports map.project.repository;
    exports map.project.service;
    exports map.project.utils.observer;
}