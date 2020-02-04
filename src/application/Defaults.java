package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Defaults {
    static CheckBox cbNum = new CheckBox("ID number");
    static CheckBox cbTime = new CheckBox("Time");
    static CheckBox cbDuration = new CheckBox("Duration");
    static CheckBox cbJerk = new CheckBox("Jerk");
    static CheckBox cbAcc = new CheckBox("Acc");
    static CheckBox cbVel = new CheckBox("Vel");
    static CheckBox cbPos = new CheckBox("Pos");
    static CheckBox cbLRAcc = new CheckBox("L/R Acc");
    static CheckBox cbLRVel = new CheckBox("L/R Vel");
    static CheckBox cbLRPos = new CheckBox("L/R Pos");
    static CheckBox cbAngle = new CheckBox("Heading");
    static CheckBox cbActions = new CheckBox("Actions");
    
    final Stage stageDefaults = new Stage();
	public Defaults() {
	    stageDefaults.initModality(Modality.NONE);
        stageDefaults.initOwner(null);
        GridPane grid = new GridPane();
        String[] labelText = {"Max Jerk","Max Acc","Max Vel","Rotation Speed","Wheel Diam (in)",
        		"Wheel Base(in)","Encoder Units/Rotation", "Profile Outputs"};

        NumberField nfJerk = new NumberField();
        NumberField nfAcc = new NumberField();
        NumberField nfVel = new NumberField();
        NumberField nfmaxRotate= new NumberField();
        NumberField nfwheelDiam = new NumberField();
        NumberField nfwheelBase = new NumberField();
        NumberField nfencUnitsPerRot = new NumberField();
        nfJerk.setMaxWidth(60);
        nfAcc.setMaxWidth(60);
        nfVel.setMaxWidth(60);        
        nfmaxRotate.setMaxWidth(60);
        nfwheelDiam.setMaxWidth(60);
        nfwheelBase.setMaxWidth(60);
        nfencUnitsPerRot.setMaxWidth(60);   
        nfJerk.setText(Double.toString(Waypoint.dataDefault[4]));
        nfAcc.setText(Double.toString(Waypoint.dataDefault[5]));
        nfVel.setText(Double.toString(Waypoint.dataDefault[6]));        
        nfmaxRotate.setText(Double.toString(Constants.rotRate));        
        nfwheelDiam.setText(Double.toString(Constants.wheelDiameter));
        nfwheelBase.setText(Double.toString(Constants.wheelBase));
        nfencUnitsPerRot.setText(Double.toString(Constants.encoderUnitsPerRotation)); 
        
        cbNum.setIndeterminate(false);
        cbNum.setSelected(false);
        cbTime.setIndeterminate(false);
        cbTime.setSelected(true);
        cbDuration.setIndeterminate(false);
        cbDuration.setSelected(false);
        cbJerk.setIndeterminate(false);
        cbJerk.setSelected(false);
        cbAcc.setIndeterminate(false);
        cbAcc.setSelected(false);
        cbVel.setIndeterminate(false);
        cbVel.setSelected(true);
        cbPos.setIndeterminate(false);
        cbPos.setSelected(true);
        cbLRAcc.setIndeterminate(false);
        cbLRAcc.setSelected(false);       
        cbLRVel.setIndeterminate(false);
        cbLRVel.setSelected(true);       
        cbLRPos.setIndeterminate(false);
        cbLRPos.setSelected(false);       
        cbAngle.setIndeterminate(false);
        cbAngle.setSelected(true);       
        cbActions.setIndeterminate(false);
        cbActions.setSelected(true);       
        
        
        Button bsetDefault = new Button("Set as Default");
        Button bsetCurrent= new Button("Set as Default and Update");
        int k=0;
        while(k<=7) {
        	grid.add(new Label(labelText[k]), 0,k);
        	k++;
        }
        grid.add(nfJerk, 1, 0);
        grid.add(nfAcc, 1, 1);
        grid.add(nfVel, 1, 2);
        grid.add(nfmaxRotate, 1, 3);
        grid.add(nfwheelDiam, 1, 4);
        grid.add(nfwheelBase, 1, 5);
        grid.add(nfencUnitsPerRot, 1, 6);
        grid.add(cbNum, 1, 7);
        grid.add(cbTime, 1, 8);
        grid.add(cbDuration, 1, 9);
        grid.add(cbJerk, 1, 10);
        grid.add(cbAcc, 1, 11);
        grid.add(cbVel, 1, 12);
        grid.add(cbPos, 1, 13);
        grid.add(cbLRAcc, 1, 14);
        grid.add(cbLRVel, 1, 15);
        grid.add(cbLRPos, 1, 16);
        grid.add(cbAngle, 1, 17);
        grid.add(cbActions, 1, 18);
        grid.add(bsetDefault, 0, 19);
        grid.add(bsetCurrent, 0, 20);
        
        
        Scene sceneDefaults= new Scene(grid,400,500);
        stageDefaults.setTitle("Set Default Values");

        stageDefaults.setScene(sceneDefaults);
        stageDefaults.hide();

        
        bsetDefault.setOnAction( (event) -> {
        	double num; int i=0;
        	num=nfJerk.getDouble();
        	if(nfJerk.hasvalue) {
        		Waypoint.dataDefault[4]=num;
        		Main.numfields[5].setText(Double.toString(num));
        	}
        	num=nfAcc.getDouble();
        	if(nfAcc.hasvalue) {
        		Waypoint.dataDefault[5]=num;
        		Main.numfields[6].setText(Double.toString(num));
        	}
        	num=nfVel.getDouble();
        	if(nfVel.hasvalue) {
        		Waypoint.dataDefault[6]=num;
        		Main.numfields[7].setText(Double.toString(num));
        	}
        	num=nfmaxRotate.getDouble();
        	if(nfmaxRotate.hasvalue) {
        		Constants.rotRate=num;
        	}
        	num=nfwheelDiam.getDouble();
        	if(nfwheelDiam.hasvalue) {
        		Constants.wheelDiameter=num;
        	}
        	i=nfencUnitsPerRot.getInt();
        	if(nfencUnitsPerRot.hasvalue) {
        		Constants.encoderUnitsPerRotation=i;
        	}
        });
        
        bsetCurrent.setOnAction( (event)-> {
        	bsetDefault.fire();
        	int i=0;
        	while (i<Main.waypoints.size()) {
        		Main.waypoints.get(i).data[4]=Waypoint.dataDefault[4];
        		Main.waypoints.get(i).data[5]=Waypoint.dataDefault[5];
        		Main.waypoints.get(i).data[6]=Waypoint.dataDefault[6];
        		i++;
        	}
        	Main.wpTable.updateTable();
        });
        
	}
	
	public void showDefaults() {
    	stageDefaults.show();
    	stageDefaults.setIconified(false);
    	stageDefaults.toFront();    	
	}
	
}
