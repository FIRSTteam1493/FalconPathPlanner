package application;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Rectangle;

public class FieldLayout {
    
	FieldLayout(){
	}

	public LineChart getFieldChart() {
	    NumberAxis xAxis2 = new NumberAxis();
	    NumberAxis yAxis2 = new NumberAxis();	    	    	    
		LineChart fieldChart = new LineChart<Number,Number>(xAxis2,yAxis2);
	    fieldChart.setCreateSymbols(false);
	    fieldChart.setLegendVisible(false);
		fieldChart.setPrefHeight(500);
	 	xAxis2.setAutoRanging(false);
	 	xAxis2.setLowerBound(0);
	 	xAxis2.setUpperBound(324);
	 	xAxis2.setTickUnit(50);

	 	yAxis2.setAutoRanging(false);
	 	yAxis2.setLowerBound(0);
	 	yAxis2.setUpperBound(324);
	 	yAxis2.setTickUnit(50);
	 	
	 	
// 1/2 field is 27ft x 27ft, 324in x 324in		
// centerline is at y=162	 	  
// platform 1 - low, 128 x 	95.28 (includes other 2),   162-128/2 = 98,  162+128/2=226	    
    XYChart.Series seriesplatform1a= new XYChart.Series();		
    seriesplatform1a.getData().add(new XYChart.Data(1, 98));
    seriesplatform1a.getData().add(new XYChart.Data(95.28, 98));
    seriesplatform1a.getData().add(new XYChart.Data(95.28, 226));
     XYChart.Series seriesplatform1b= new XYChart.Series();	
     seriesplatform1b.getData().add(new XYChart.Data(1, 98));
    seriesplatform1b.getData().add(new XYChart.Data(1, 226));
    seriesplatform1b.getData().add(new XYChart.Data(95.28, 226));
    
//platform 3 - high platform, 48 x 48, 162+48/2 = 186,  162-48/2 = 138    
    XYChart.Series seriesplatform3a= new XYChart.Series();		
    seriesplatform3a.getData().add(new XYChart.Data(1, 138));
    seriesplatform3a.getData().add(new XYChart.Data(48, 138));
    seriesplatform3a.getData().add(new XYChart.Data(48, 186));
    XYChart.Series seriesplatform3b= new XYChart.Series();		
    seriesplatform3b.getData().add(new XYChart.Data(1, 138));
    seriesplatform3b.getData().add(new XYChart.Data(1, 186));
    seriesplatform3b.getData().add(new XYChart.Data(48, 186));

    
  //platform 2 - high platform, 48 x 48, 162+48/2 = 186,  162-48/2 = 138    
    XYChart.Series seriesplatform2= new XYChart.Series();		
    seriesplatform2.getData().add(new XYChart.Data(48, 98));
    seriesplatform2.getData().add(new XYChart.Data(48, 226));

// Cargo Ship - 7ft 11.75in x 4ft 7.75in, 95.75 x 55.75     
//     (228.25, 134.125)  (324, 134.125) (324, 189.875) (228.25, 189.875)
  //platform 3 - high platform, 48 x 48, 162+48/2 = 186,  162-48/2 = 138    
    XYChart.Series seriescargoa= new XYChart.Series();		
    seriescargoa.getData().add(new XYChart.Data(228.25, 134.125));
    seriescargoa.getData().add(new XYChart.Data(324,134.125));
    seriescargoa.getData().add(new XYChart.Data(324,189.875));
    XYChart.Series seriescargob= new XYChart.Series();		
    seriescargob.getData().add(new XYChart.Data(228.25, 134.125));
    seriescargob.getData().add(new XYChart.Data(228.25,189.875));
    seriescargob.getData().add(new XYChart.Data(324,189.875));

    //centerline
    XYChart.Series seriescenterline= new XYChart.Series();		
    seriescenterline.getData().add(new XYChart.Data(0, 162));
    seriescenterline.getData().add(new XYChart.Data(324, 162));
    
//    cargotape1  10.88 from centerline, 25.6in long (?)
// 228-25.6 =     
//   (202.4,  167.44)    (28, 167.44)
    XYChart.Series seriescargotp1= new XYChart.Series();		
    seriescargotp1.getData().add(new XYChart.Data(202.4, 172.88));
    seriescargotp1.getData().add(new XYChart.Data(227,172.88));
    
    XYChart.Series seriescargotp2= new XYChart.Series();		
    seriescargotp2.getData().add(new XYChart.Data(202.4, 151.12));
    seriescargotp2.getData().add(new XYChart.Data(227,151.12));
    
    
    
    
    fieldChart.getData().add(seriesplatform1a);	
    fieldChart.getData().add(seriesplatform1b);	
    fieldChart.getData().add(seriesplatform2);	
    fieldChart.getData().add(seriesplatform3a);	
    fieldChart.getData().add(seriesplatform3b);	
    fieldChart.getData().add(seriescargoa);	
    fieldChart.getData().add(seriescargob);	
    fieldChart.getData().add(seriescenterline);	
    fieldChart.getData().add(seriescargotp1);	
    fieldChart.getData().add(seriescargotp2);	
    
    seriesplatform1a.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 3px;");
    seriesplatform1b.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 3px;");
    seriesplatform2.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 3px;");
    seriesplatform3a.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 3px;");
    seriesplatform3b.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 3px;");
    seriescargoa.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 3px;");
    seriescargob.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 3px;");
    seriescenterline.getNode().setStyle("-fx-stroke: yellow; -fx-stroke-width: 1px;");   
    seriescargotp1.getNode().setStyle("-fx-stroke: yellow; -fx-stroke-width: 3px;");   
    seriescargotp2.getNode().setStyle("-fx-stroke: yellow; -fx-stroke-width: 3px;");   
    
    
    
    
	
    
    
    
	return fieldChart;
	}

}
