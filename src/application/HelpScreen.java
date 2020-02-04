package application;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpScreen {
    final Stage stageHelp = new Stage();
    
	HelpScreen(){
	    stageHelp.initModality(Modality.NONE);
        stageHelp.initOwner(null);
        VBox vbox = new VBox();
        TextArea taHelp1 = new TextArea();
        taHelp1.prefWidthProperty().bind(vbox.widthProperty());
        taHelp1.prefHeightProperty().bind(vbox.heightProperty());
        taHelp1.setEditable(false);
        vbox.getChildren().add(taHelp1);
        
        taHelp1.appendText("Falcons Path and Action Planner");
        taHelp1.appendText("\n");
        taHelp1.appendText("\n"+"Add Waypoints:  ");
        taHelp1.appendText("\n"+"Shift click in chart to add WP to end, or enter data at right and click 'add after'.");
        taHelp1.appendText("\n"+"Only x and y coordinates are required,default values used for others if not specified");
        
        taHelp1.appendText("\n"+"\n"+"Delete Waypoints:  ");
        taHelp1.appendText("\n"+"Control click on waypoint or use the 'delete' button in the waypoint table ");
        
        taHelp1.appendText("\n"+"\n"+"Edit Waypoints and Cotnrol Points:  ");
        taHelp1.appendText("\n"+"Click and drag point on chartor use 'edit waypoint' table.");
        taHelp1.appendText("\n"+"When using the table you must click 'update to save your changes");
        
        taHelp1.appendText("\n"+"\n"+"Path Parameters");
        taHelp1.appendText("\n"+"The entire route of the robot is a 'profile'. Along the profile the robot");
        taHelp1.appendText("\n"+"may start moving, stop, and start again. Each set of points between a ");
        taHelp1.appendText("\n"+"start and stop is a 'path'. Every point in the path is assigned a point type.");
        taHelp1.appendText("\n"+"Point types:  0-stop 	1-run	2-pause	  3-rotate");
        taHelp1.appendText("\n"+"The type for the first point in the path must be 1 and the last point type must be 0, 2,or 3.");
        taHelp1.appendText("\n"+"If type is rotate enter the angle(degrees) in the rotate field. ");
        taHelp1.appendText("\n"+"If pause is used enter the pause time in seconds in the rotate field.");
        taHelp1.appendText("\n"+"\n"+"Max Jerk, acceleration, and velocity are determined from the first point");
        taHelp1.appendText("\n"+"in the path. Final velocity also is determined from the first point");
        
        
        Scene sceneHelp= new Scene(vbox,600,600);
        stageHelp.setTitle("Read Me!");

        stageHelp.setScene(sceneHelp);
        stageHelp.hide();

	}
	public void showHelp() {
		stageHelp.setIconified(false);
		stageHelp.show();
		stageHelp.toFront();
	}
}

	