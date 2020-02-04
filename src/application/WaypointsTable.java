package application;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WaypointsTable {
	int numwpts=0;
	static int counter;
	HBox hbox = new HBox();
	GridPane gridpane = new GridPane();	
	ScrollPane scrollpane = new ScrollPane(gridpane);
	VBox vbox = new VBox(hbox,scrollpane);
	Stage dataStage = new Stage();
	Scene sceneData= new Scene(vbox,1000,300);
	ArrayList<NumberField[]> wpTFArray= new ArrayList<NumberField[]>();
	
	
	WaypointsTable(){
// Initial Waypoints

		Button update = new Button("Update");
		Button calculateVDA = new Button("calculate V-D-A Profile");
		NumberField tfAddAfter = new NumberField();
		tfAddAfter.setMaxWidth(60);
		Button deletePoint = new Button("Delete");
		NumberField tfDelete= new NumberField();
		tfDelete.setMaxWidth(60);
		hbox.setSpacing(20);
		hbox.getChildren().addAll(deletePoint,tfDelete,update,calculateVDA);
		dataStage.initStyle(StageStyle.DECORATED);
		dataStage.initModality(Modality.NONE);
		dataStage.initOwner(null);
		dataStage.setScene(sceneData);

		writeLabels();
		

        deletePoint.setOnAction((event) -> {
        	if (Main.waypoints.size()>2) {
        	int value=tfDelete.getInt();
        	if (tfDelete.hasint) Main.deleteWP(value);
        	}
        });

        update.setOnAction((event) -> {
        	updateFromTable();
        });
        
        calculateVDA.setOnAction((event) -> {
        	updateFromTable();
    		if (!Utils.checkStop()) {Main.taMessages.appendText("\n"+"Last WP cannot be running");return;}
    		Main.profile.calculateVDA();   
    		Main.vdaWindow.graphProfile(Main.profile.numSpeedPoints);
        });
	}
	
	public void showWayPointTable() {
    	dataStage.show();
    	dataStage.setIconified(false);
    	dataStage.toFront();    	
	}
		
// clear the table contents and re-create input from the waypoint list
// also clear the chart series and recreate	
	public void updateTable() {
		int i=0;
		int size=Main.waypoints.size();	
		gridpane.getChildren().clear();
		writeLabels();
		wpTFArray.clear();		
		Main.clearWaypointSeries();
		while(i<size) {
			addToTable(Main.waypoints.get(i));
			Main.waypoints.get(i).addWPtoChart(i);
			i++;
		}
		Main.calculateProfile();
	}

	// clear the table contents and re-create input from the waypoint list
	// also clear the chart series and recreate	
		public void updateOnly() {
			int i=0;
			int size=Main.waypoints.size();	
			gridpane.getChildren().clear();
			writeLabels();
			wpTFArray.clear();		
			while(i<size) {
				addToTable(Main.waypoints.get(i));
				Main.waypoints.get(i).addWPtoChart(i);
				i++;
			}
		}
	
	
	
	public void addToTable(Waypoint waypoint_) {		
		Waypoint waypoint = waypoint_;		
		int num=wpTFArray.size();
		int i = 0;
		wpTFArray.add(new NumberField[14]);
		while(i<=13) {
			wpTFArray.get(num)[i]=new NumberField();		
			wpTFArray.get(num)[i].setMaxWidth(60);	
			gridpane.add(wpTFArray.get(num)[i], i, num+1);
			i++;
		}		
		wpTFArray.get(num)[0].setText(String.valueOf(num+1));
		i=1;
		while(i<=13) {
			wpTFArray.get(num)[i].setText(String.valueOf(waypoint.data[i-1]));
			i++;
		}			
	}
	

// delete all waypoints and re-create using input from the table	
	public void updateFromTable() {
		double[] data = new double[13];
		int j=0,i=0;
		int size = Main.waypoints.size();
		Main.waypoints.clear();	
		
		Waypoint.id=-1;
		Main.clearWaypointSeries();
		while (j<size) {
			while (i<13) {
				data[i]=wpTFArray.get(j)[i+1].getDouble(); // the first textfield is just the WP number
				i++;
			}
			Main.insertIndex=Main.waypoints.size();
			Main.waypoints.add(new Waypoint(data));
			i=0;

			j++;		
		}
		Main.calculateProfile();
	}
	
	private void writeLabels(){
		String[] labeltext = {"Num","X","Y","Angle","CtrlD","Jmax","Amax","Vmax","Vfinal","Type",
				"Rotate","Fit","C3D","C4D"};
		Label[] labels = new Label[14]; 
		int i=0;
		while(i<=13) {
			labels[i]=new Label(labeltext[i]);
			labels[i].setMaxWidth(60);
			gridpane.add(labels[i], i, 0);
			i++;
		}

	}

	
	
}
