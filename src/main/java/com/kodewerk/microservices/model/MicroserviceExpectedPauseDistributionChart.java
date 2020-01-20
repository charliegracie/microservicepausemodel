package kodewerk.microservices.model;

/********************************************
 * Copyright (c) 2020 Kirk Pepperdine
 * All right reserved
 ********************************************/

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MicroserviceExpectedPauseDistributionChart extends Application {

    private MicroserviceExpectedPauseDistributionModel model;
    private ScatterChart<Number, Number> chart;
    private XYChart.Series<Number, Number> pauseEvents = new XYChart.Series<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pause Distribution Model");
        model = new MicroserviceExpectedPauseDistributionModel();

        chart = buildScatterChart(
                "Pause Times",
                "Number of Pause Events",
                "P(no pause)");

        Label numberOfNodesLabel = new Label("Number of Nodes: ");
        Label gcOverHeadLabel = new Label("GC Overhead: ");
        Label avePauseLabel = new Label("Ave Pause");
        TextField numberOfNodes = new TextField("5");
        TextField gcOverHead = new TextField("5");
        TextField avePause = new TextField("100");
        Button button = new Button("Compute");

        button.setOnAction((event) -> {
            ObservableList<XYChart.Data<Number, Number>> probabilityOfSeeingAPause = FXCollections.observableArrayList();
            model.expectedPauseDistribution(probabilityOfSeeingAPause, Integer.valueOf(numberOfNodes.getText()), Integer.valueOf(gcOverHead.getText()), Integer.valueOf(avePause.getText()));
            pauseEvents.getData().setAll(probabilityOfSeeingAPause);
        });

        HBox controls = new HBox(5, numberOfNodesLabel, numberOfNodes, gcOverHeadLabel, gcOverHead, avePauseLabel, avePause,button);
        VBox root = new VBox(5, controls, chart);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 900, 360);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    ScatterChart<Number, Number> buildScatterChart(String title, String xAxisLabel, String yAxisLabel) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle(title);
        pauseEvents.setName("No Pause");
        chart.getData().add(pauseEvents);
        return chart;
    }
}
