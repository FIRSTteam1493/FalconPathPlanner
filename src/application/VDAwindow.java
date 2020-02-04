package application;
import java.util.*;

import com.sun.javafx.charts.Legend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;		

public class VDAwindow {
	Stage VDAstage= new Stage();	
    XYChart.Series pos = new XYChart.Series();
    XYChart.Series angle = new XYChart.Series();
    XYChart.Series vel = new XYChart.Series();
    XYChart.Series acc = new XYChart.Series();
    XYChart.Series jerk = new XYChart.Series();
    XYChart.Series dist = new XYChart.Series();
    XYChart.Series distL = new XYChart.Series();
    XYChart.Series distR = new XYChart.Series();
    XYChart.Series velL = new XYChart.Series();
    XYChart.Series velR = new XYChart.Series();
    XYChart.Series posL = new XYChart.Series();
    XYChart.Series posR = new XYChart.Series();

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    LineChart<Number,Number> lc = new
    		LineChart<Number,Number>(xAxis,yAxis);
    TextField tfLength = new TextField();
	
	
	double vi=4,vf=0;
	double dt=0.02;

	public VDAwindow() {
		lc.setAnimated(false);
		VDAstage.initModality(Modality.NONE);
		VDAstage.initOwner(null);


        pos.setName("pos");
        angle.setName("angle");
        vel.setName("vel");
        acc.setName("acc");
        jerk.setName("jerk");
        dist.setName("dist");
        distL.setName("distL");
        distR.setName("distR");
        velL.setName("velL");
        velR.setName("velR");
        posL.setName("posL");
        posR.setName("posR");
        lc.getData().addAll(pos, angle,vel,acc,jerk,dist,distL,distR,velL,velR,posL,posR);
        lc.setLegendVisible(true);

        createWindow();
		
	}
		
	
	

	
	public void graphProfile(int numpts) {
		int i,size;
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(false);
		long startTime = System.nanoTime();
		size=pos.getData().size();
		i=size-1;
		while(i>0) {
    	pos.getData().remove(i);
    	angle.getData().remove(i);
    	vel.getData().remove(i);
    	acc.getData().remove(i);
    	jerk.getData().remove(i);
    	dist.getData().remove(i);
    	distL.getData().remove(i);
    	distR.getData().remove(i);
    	velL.getData().remove(i);
    	velR.getData().remove(i);
    	posL.getData().remove(i);
    	posR.getData().remove(i);
    	
    	i--;
		}
    	i=0;
    	Profile.time.set(0,	0.0);
	        while(i<numpts) {
	        	if (i%5==0) {
		        pos.getData().add(new XYChart.Data(Profile.time.get(i),Profile.pos2.get(i)));
		        angle.getData().add(new XYChart.Data(Profile.time.get(i),Profile.angle2.get(i)));
		        vel.getData().add(new XYChart.Data(Profile.time.get(i),Profile.vel.get(i)));
		        acc.getData().add(new XYChart.Data(Profile.time.get(i),Profile.acc.get(i)));
		        jerk.getData().add(new XYChart.Data(Profile.time.get(i),Profile.jerk.get(i)));
		        dist.getData().add(new XYChart.Data(Profile.time.get(i),Profile.dist2.get(i)));
		        distL.getData().add(new XYChart.Data(Profile.time.get(i),Profile.dist2L.get(i)));
		        distR.getData().add(new XYChart.Data(Profile.time.get(i),Profile.dist2R.get(i)));
		        velL.getData().add(new XYChart.Data(Profile.time.get(i),Profile.velL.get(i)));
		        velR.getData().add(new XYChart.Data(Profile.time.get(i),Profile.velR.get(i)));
		        posL.getData().add(new XYChart.Data(Profile.time.get(i),Profile.pos2L.get(i)));
		        posR.getData().add(new XYChart.Data(Profile.time.get(i),Profile.pos2R.get(i)));
	        	}
	        	i++;
	        }	        	        	
	        scaleAxis();
	        showVDA();
	        System.out.println("graph time = "+((System.nanoTime()-startTime)/1000000.));
	    }
	
	private void createWindow() {		
		VDAstage.setTitle("Speed Profile");
		
        Button bup = new Button("Upper");	    
        NumberField tfup = new NumberField();
        tfup.setMaxWidth(60);
        
        Button blow = new Button("Lower");	    
        NumberField tflow = new NumberField();
        tflow.setMaxWidth(60);
        
        

        Label ldist = new Label("Length");
        tfLength.setDisable(true);
        tfLength.setMaxWidth(60);
        HBox hbox = new HBox();
        HBox hbox2 = new HBox();
        VBox vbox = new VBox();
        hbox.setSpacing(10);
        hbox2.setSpacing(10);
        hbox.getChildren().addAll(bup,tfup,blow,tflow,ldist,tfLength);

        hbox2.getChildren().addAll();

        
        xAxis.setLabel("Time");                
        yAxis.setLabel("s,v,a,j");
//        lc.setTitle("Speed Profile");
        lc.setCreateSymbols(false);
       	    
        pos.setName("pos");
        angle.setName("angle");
        vel.setName("vel");
        acc.setName("acc");
        jerk.setName("jerk");
        dist.setName("dist");
        distL.setName("distL"); 
        distR.setName("distR"); 
        velL.setName("velL"); 
        velR.setName("velR"); 
        posL.setName("posL"); 
        posR.setName("posR"); 
        
        for (Node n : lc.getChildrenUnmodifiable()) {
            if (n instanceof Legend) {
                Legend l = (Legend) n;
                for (Legend.LegendItem li : l.getItems()) {
                    for (XYChart.Series<Number, Number> s : lc.getData()) {
                        if (s.getName().equals(li.getText())) {
                            li.getSymbol().setCursor(Cursor.HAND); // Hint user that legend symbol is clickable
                            li.getSymbol().setOnMouseClicked(me -> {
                                if (me.getButton() == MouseButton.PRIMARY) {
                                    s.getNode().setVisible(!s.getNode().isVisible()); // Toggle visibility of line
                                    scaleAxis();                                    
                                }
                            });
                            break;
                        }
                    }
                }
            }
        }        
        
        dist.getNode().setVisible(false);
        distL.getNode().setVisible(false);
        distR.getNode().setVisible(false);
        pos.getNode().setVisible(false);
        posL.getNode().setVisible(false);
        posR.getNode().setVisible(false);
        
        vbox.getChildren().addAll(hbox,hbox2,lc);

        
        bup.setOnAction((event) -> {
        	double value=tfup.getDouble();
        	if (tfup.hasvalue) yAxis.setUpperBound(value);	                  
        });

        blow.setOnAction((event) -> {
        	double value=tflow.getDouble();
        	if (tflow.hasvalue) yAxis.setLowerBound(value);	                      
        });	       	        		
		
		
		
		Scene scene  = new Scene(vbox, 900, 500);	        
	    VDAstage.setScene(scene);
	    VDAstage.show();
	}
	
	public void showVDA() {
		VDAstage.show();
    	VDAstage.setIconified(false);
		VDAstage.toFront();
	}
	
	public void scaleAxis() {
		
		List<Double> maxvalues = new ArrayList<Double>();
		List<Double> minvalues = new ArrayList<Double>();
		maxvalues.addAll(Profile.maxvalues);
		minvalues.addAll(Profile.minvalues);
		
		if(pos.getNode().isVisible()) {
			maxvalues.set(0,maxvalues.get(0));
			minvalues.set(0,minvalues.get(0));
		}
		else {
			maxvalues.set(0,0.);
			minvalues.set(0,0.);
		}
		
		if(dist.getNode().isVisible()) {
			maxvalues.set(1,maxvalues.get(1));
			minvalues.set(1,minvalues.get(1));
		}
		else {
			maxvalues.set(1,0.);
			minvalues.set(1,0.);
		}
		
		if(jerk.getNode().isVisible()) {
			maxvalues.set(2,maxvalues.get(2));
			minvalues.set(2,minvalues.get(2));
		}
		else {
			maxvalues.set(2,0.);
			minvalues.set(2,0.);
		}
		
		if(acc.getNode().isVisible()) {
			maxvalues.set(3,maxvalues.get(3));
			minvalues.set(3,minvalues.get(3));
		}
		else {
			maxvalues.set(3,0.);
			minvalues.set(3,0.);
		}
		
		if(vel.getNode().isVisible()) {
			maxvalues.set(4,maxvalues.get(4));
			minvalues.set(4,minvalues.get(4));
		}
		else {
			maxvalues.set(4,0.);
			minvalues.set(4,0.);
		}
		
		if(angle.getNode().isVisible()) {
			maxvalues.set(5,maxvalues.get(5));
			minvalues.set(5,minvalues.get(5));
		}
		else {
			maxvalues.set(5,0.);
			minvalues.set(5,0.);
		}
		
		if(distL.getNode().isVisible()) {
			maxvalues.set(6,maxvalues.get(6));
			minvalues.set(6,minvalues.get(6));
		}
		else {
			maxvalues.set(6,0.);
			minvalues.set(6,0.);
		}
		if(distR.getNode().isVisible()) {
			maxvalues.set(7,maxvalues.get(7));
			minvalues.set(7,minvalues.get(7));
		}
		else {
			maxvalues.set(7,0.);
			minvalues.set(7,0.);
		}
		if(velL.getNode().isVisible()) {
			maxvalues.set(8,maxvalues.get(8));
			minvalues.set(8,minvalues.get(8));
		}
		else {
			maxvalues.set(8,0.);
			minvalues.set(8,0.);
		}
		if(velR.getNode().isVisible()) {
			maxvalues.set(9,maxvalues.get(9));
			minvalues.set(9,minvalues.get(9));
		}
		else {
			maxvalues.set(9,0.);
			minvalues.set(9,0.);
		}
		if(posL.getNode().isVisible()) {
			maxvalues.set(10,maxvalues.get(10));
			minvalues.set(10,minvalues.get(10));
		}
		else {
			maxvalues.set(10,0.);
			minvalues.set(10,0.);
		}
		if(posR.getNode().isVisible()) {
			maxvalues.set(11,maxvalues.get(11));
			minvalues.set(11,minvalues.get(11));
		}
		else {
			maxvalues.set(11,0.);
			minvalues.set(11,0.);
		}
		
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(false);

		double ymax=Collections.max(maxvalues);
		double ymin=Collections.min(minvalues);
		int a = ((int)Math.log10(ymax)-1 ); if (ymax<100) a=a+1;
		ymax= ((int)(ymax/10))*10 + Math.pow(10, a);
		ymin=Math.abs(ymin);
		ymin= ((int)(ymin/10))*10 + Math.pow( 10,a);
		ymin=-ymin;
		yAxis.setUpperBound(ymax);
		yAxis.setLowerBound(ymin);		
	}
	
	
}
