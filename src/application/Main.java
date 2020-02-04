// To Do
// add center, left, right profiles in rotate in place (currently center is net zero distance
// update rotate in place
// error handling in filehandler
// check profile output
// remember state of data dump


package application;
	

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;



public class Main extends Application {
	static WaypointsTable wpTable = new WaypointsTable();
	static ArrayList<Waypoint> waypoints= new ArrayList<Waypoint>();
	static NumberField[] numfields= new NumberField[13];
	double wpx,wpy,angle, ctrlDist, jmax,amax,vmax,cx1,cy1,cx2,cy2,rotate;
	int type, action;
	static int fitType=3;
	static XYChart.Series<Number,Number> seriesWaypoints = new XYChart.Series<Number,Number>();
	static XYChart.Series<Number,Number> seriesControlPoints= new XYChart.Series<Number,Number>();
	static XYChart.Series<Number,Number> seriesControlPointsOut= new XYChart.Series<Number,Number>();
	static XYChart.Series<Number,Number> seriesControlPoints2= new XYChart.Series<Number,Number>();
	static XYChart.Series<Number,Number> seriesControlPointsOut2= new XYChart.Series<Number,Number>();

	static 	XYChart.Series<Number,Number> seriesProfile = new XYChart.Series<Number,Number>();
	static 	XYChart.Series<Number,Number> seriesProfileL = new XYChart.Series<Number,Number>();
	static 	XYChart.Series<Number,Number> seriesProfileR = new XYChart.Series<Number,Number>();
	static NumberAxis xAxis = new NumberAxis();
	static NumberAxis yAxis = new NumberAxis();
	static Window window;
	static Scene scene;
	static Profile profile = new Profile();
	static VDAwindow vdaWindow = new VDAwindow();
	static int insertIndex=0;
	static Button bHidePath= new Button("Hide Path");
	static Button bShowVDA= new Button("Show VDA");
	static TextField tfLength=new TextField(" ");
	static TextArea taMessages = new TextArea();
	Defaults defaults = new Defaults();
	HelpScreen helpScreen = new HelpScreen();
   	FieldElements fieldElements ;
  	static StackPane paneLineChart = new StackPane();
  	static StackPane paneCharts = new StackPane();
  	static AnchorPane anchorPane = new AnchorPane();
  	
	@Override
	public void start(Stage primaryStage) {
		String[] labelText1={"Num","X","Y","Angle In","Ctrl D","Jmax","Amax","Vmax","Vfinal",
				"Type","Rotate","Fit"  };
		int i=0;
		while(i<=12) {
			numfields[i]=new NumberField();
			numfields[i].setMaxWidth(50);
			i++;
		}
		
		ScatterChart<Number,Number> lineChart = 
				new ScatterChart<Number,Number>(xAxis,yAxis);

		lineChart.setAnimated(false);
		lineChart.setPrefHeight(650);
		lineChart.setPrefWidth(650);
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(628);
		xAxis.setTickUnit(50);

		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(324);
		yAxis.setTickUnit(50);

		lineChart.getData().add(seriesWaypoints);
		lineChart.getData().add(seriesProfile);
		lineChart.getData().add(seriesControlPoints);
		lineChart.getData().add(seriesControlPointsOut);
		lineChart.getData().add(seriesControlPoints2);
		lineChart.getData().add(seriesControlPointsOut2);
		lineChart.getData().add(seriesProfileL);
		lineChart.getData().add(seriesProfileR);
		lineChart.setLegendVisible(false);
		lineChart.setVisible(true);


		MenuItem menuItemSaveWaypoints = new MenuItem("Save Waypoint File");
		MenuItem menuItemLoadWaypoints = new MenuItem("Load Waypoint File");
		MenuItem menuItemSaveProfile = new MenuItem("Save Profile");
		MenuItem menuItemSaveProfileToRio = new MenuItem("Save Profile to Rio");
		MenuItem menuItemSetDefaults = new MenuItem("Set Defaults and Constants");
		MenuItem menuItemDataDump = new MenuItem("Save Data");
		MenuItem menuItemHelp = new MenuItem("Show Help");
		SeparatorMenuItem separator = new SeparatorMenuItem();
		Menu menuFile = new Menu("File");
		Menu menuDefaults= new Menu("Settings");
		Menu menuHelp= new Menu("Help");
		Menu menuUtilities = new Menu("Utilities");
		menuFile.getItems().addAll(menuItemSaveWaypoints,menuItemLoadWaypoints,separator,
				menuItemSaveProfile,menuItemSaveProfileToRio);
		menuDefaults.getItems().addAll(menuItemSetDefaults);
		menuHelp.getItems().addAll(menuItemHelp);
		menuUtilities.getItems().add(menuItemDataDump);
		MenuBar menubar = new MenuBar();
		menubar.getMenus().addAll(menuFile,menuDefaults,menuUtilities,menuHelp);
		ComboBox<String> combo = new ComboBox<String>();
		combo.setPromptText("Fit Type");
		combo.getItems().addAll("Cubic","Quintic");
		combo.setValue("Cubic");
		VBox vbox1 = new VBox();
		vbox1.getStyleClass().add("vbox");
		HBox hbox1 = new HBox();
		hbox1.getStyleClass().add("hbox");
		GridPane gridpane = new GridPane();
		gridpane.getStyleClass().add("grid");

		
		Button bAddAfter= new Button("Add After");
		NumberField tfAddAfter = new NumberField();
		tfAddAfter.setMaxWidth(35);
		tfAddAfter.setText("3");
		Button bEditWaypoints = new Button("Edit Waypoints");
		Button bCalculateVDA= new Button("Calculate V-D-A");
		Button bAction= new Button("Actions");
		bHidePath.setMinWidth(60);
		tfLength.setEditable(false);
		Label lLength = new Label("Path Length");
		tfLength.setMaxWidth(70);
		taMessages.setPrefHeight(120);taMessages.setPrefWidth(220);taMessages.setEditable(false);	
		
		i=0;
		while(i<=11) {
			gridpane.add(new Label(labelText1[i]),1,i);
			gridpane.add(numfields[i],2,i);
			i++;
		}
		VBox vbox2 = new VBox(); 
		vbox2.getChildren().addAll(bAddAfter,tfAddAfter,bEditWaypoints,bCalculateVDA,
				bHidePath,bShowVDA,bAction,combo,lLength,tfLength,taMessages);
		vbox2.getStyleClass().add("vbox2");
		paneLineChart.getChildren().add(lineChart);
//		FieldLayout fieldLayout = new FieldLayout();
//		StackPane paneFieldChart = new StackPane(fieldLayout.getFieldChart());		
//		paneCharts.getChildren().addAll(paneFieldChart,paneLineChart);
		paneCharts.getChildren().addAll(paneLineChart);
		hbox1.getChildren().addAll(paneCharts,vbox2,gridpane);
		vbox1.getChildren().addAll(menubar,hbox1);
		paneCharts.setPrefWidth(900);
		anchorPane.getChildren().addAll(vbox1);		
		
		scene = new Scene(anchorPane,1200,850);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		window=scene.getWindow();
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	      primaryStage.setX(primaryScreenBounds.getMinX());
	      primaryStage.setY(primaryScreenBounds.getMinY());
	      primaryStage.setWidth(primaryScreenBounds.getWidth());
	      primaryStage.setHeight(primaryScreenBounds.getHeight());

		primaryStage.setScene(scene);		
		primaryStage.show();		
		
 		fieldElements = new FieldElements();       
		
// add first two waypoints - always have at least two waypoints
		waypoints.add(new Waypoint(Constants.initWpData1_3));
		waypoints.add(new Waypoint(Constants.initWpData2_3));
		wpTable.updateTable();
		calculateProfile();
		
        bEditWaypoints.setOnAction((event) -> {
        	wpTable.showWayPointTable();
        });
        
        bAddAfter.setOnAction((event) -> {    
        	int index =tfAddAfter.getInt();
    		addAfter(index);
        });
        
        bCalculateVDA.setOnAction((event) -> {        	
    		if (!Utils.checkStop()) {taMessages.appendText("\n"+"Last WP cannot be running");return;}
        		profile.calculateVDA();   
//        		System.out.println("numspdpoints "+profile.numSpeedPoints);
        		long startTime = System.nanoTime();
        		vdaWindow.graphProfile(profile.numSpeedPoints);
        		System.out.println("plot time = "+((System.nanoTime()-startTime)/1000000));
        });
			
        bHidePath.setOnAction((event) -> {
			lineChart.getData().get(1).getData().forEach
			(data->data.getNode().setVisible(!data.getNode().isVisible()));
			if(lineChart.getData().get(1).getData().get(0).getNode().isVisible()) bHidePath.setText("Hide Path");
			else bHidePath.setText("Show Path");
    });
       
        
        bShowVDA.setOnAction((event) -> {
        	vdaWindow.showVDA();	
    });

        bAction.setOnAction((event) -> {
        	Profile.actions.showHide();			
    });
               
        combo.setOnAction( (event)  ->{
        	if ( combo.getValue().equals("Cubic")&& fitType==5) {
        		fitType=3;taMessages.appendText("\n"+"Cubic Bezier");
        		waypoints.clear();
        		Waypoint.id=-1;
        		clearWaypointSeries();
        		insertIndex=0;        		
        		waypoints.add(new Waypoint(Constants.initWpData1_3));
        		insertIndex=waypoints.size();
        		waypoints.add(new Waypoint(Constants.initWpData2_3));
        		wpTable.updateTable();
        		calculateProfile();        		

        	}
        	else if ( combo.getValue().equals("Quintic")&& fitType==3) {
        		fitType=5;taMessages.appendText("\n"+"Quintic Bezier");
        		waypoints.clear();
        		Waypoint.id=-1;
        		clearWaypointSeries();
        		insertIndex=0;        		
        		waypoints.add(new Waypoint(Constants.initWpData1_5));
        		insertIndex=waypoints.size();
        		waypoints.add(new Waypoint(Constants.initWpData2_5));
        		wpTable.updateTable();
        		calculateProfile();        		
        	}

        });
        
		menuItemSaveWaypoints.setOnAction(e -> {
			FileHandler.saveWaypoints();
		});

		
		menuItemLoadWaypoints.setOnAction(e -> {
			waypoints.clear();
			Waypoint.id=-1;
			clearWaypointSeries();
			FileHandler.loadWaypoints();
			wpTable.updateTable();
			
		});

		menuItemSaveProfile.setOnAction(e -> {
			FileHandler.saveProfile(profile,false);
		});		
		
		menuItemSaveProfileToRio.setOnAction(e -> {
			FileHandler.saveProfile(profile,true);
		});		
		
		menuItemSetDefaults.setOnAction(e -> {
		defaults.showDefaults();	
		});				
		
		menuItemDataDump.setOnAction(e -> {
			FileHandler.dataDump();	
			});				
		
		menuItemHelp.setOnAction(e -> {
			helpScreen.showHelp();
		});		

                
		lineChart.setOnMouseClicked(event->  {
			Point2D mouseSceneCoords = new Point2D(event.getSceneX(), event.getSceneY());	         

			double xclick= xAxis.sceneToLocal(mouseSceneCoords).getX();
			double yclick = yAxis.sceneToLocal(mouseSceneCoords).getY();	
		
			xclick=(double)xAxis.getValueForDisplay(xclick);
			yclick=(double)yAxis.getValueForDisplay(yclick);
			
			numfields[1].setText(String.valueOf(Utils.round(xclick)));
			numfields[2].setText(String.valueOf(Utils.round(yclick)));

			if (event.isShiftDown()) {
				addToEnd();	
				calculateProfile();
			}
		});
	}
	
	public void addToEnd(){	
		insertIndex=waypoints.size();
		waypoints.add(new Waypoint(numfields));
		wpTable.addToTable(waypoints.get(waypoints.size()-1));		
	}
	
	public void addAfter(int index){
		insertIndex=index;
		waypoints.add(index,new Waypoint(numfields));
		wpTable.updateTable();		
	}
	
	public static void deleteWP(int num) {
		waypoints.remove(num-1);
		Waypoint.id --;
		wpTable.updateTable();
	}

	
	public static void clearWaypointSeries() {
		seriesWaypoints.getData().clear();
		seriesControlPoints.getData().clear();
		seriesControlPointsOut.getData().clear();
		seriesControlPoints2.getData().clear();
		seriesControlPointsOut2.getData().clear();
		seriesProfile.getData().clear();
		seriesProfileL.getData().clear();
		seriesProfileR.getData().clear();
		
	}
		
// handle deleting and dragging waypoints on chart	
	public static void mouseHandlerWP(Data<Number, Number> data) {
		
		data.getNode().setOnMouseDragged (event->{
			double pressPositionX;
			double pressPositionY;
			Point2D mouseSceneCoords = new Point2D(event.getSceneX(), event.getSceneY());	            
			pressPositionX= xAxis.sceneToLocal(mouseSceneCoords).getX();
			pressPositionY = yAxis.sceneToLocal(mouseSceneCoords).getY();
			pressPositionX=(double)xAxis.getValueForDisplay(pressPositionX);
			pressPositionY=(double)yAxis.getValueForDisplay(pressPositionY);
			data.setXValue(pressPositionX);
			data.setYValue(pressPositionY);			
			int where = Integer.parseInt(data.getNode().getId());
			waypoints.get(where).data[0]=pressPositionX;
			waypoints.get(where).data[1]=pressPositionY;
			waypoints.get(where).calculateCtrlPoints();
			calculateProfile();
		});		
		data.getNode().setOnMouseReleased(event->{
			if(event.isControlDown()) {			
				int where = Integer.parseInt(data.getNode().getId());
				if(waypoints.size()>2) {
				deleteWP(where+1);
				}
			}			
			wpTable.updateTable();		
			calculateProfile();
		});

		
}
	
	public static void mouseHandlerCP(Data<Number, Number> data) {
		data.getNode().setOnMouseDragged (event->{
			double pressPositionX;
			double pressPositionY;
			int index=0;
			Point2D mouseSceneCoords = new Point2D(event.getSceneX(), event.getSceneY());	            
			pressPositionX= xAxis.sceneToLocal(mouseSceneCoords).getX();
			pressPositionY = yAxis.sceneToLocal(mouseSceneCoords).getY();
			pressPositionX=(double)xAxis.getValueForDisplay(pressPositionX);
			pressPositionY=(double)yAxis.getValueForDisplay(pressPositionY);	            
			data.setXValue(pressPositionX);
			data.setYValue(pressPositionY);
			
			int where = Integer.parseInt(data.getNode().getId());
			if(where>=200 && where<300) {index=where-200;
			if(waypoints.get(index).data[8]!=3){
				waypoints.get(index).data[15]=pressPositionX;
				waypoints.get(index).data[16]=pressPositionY;}
			}
			else if (where>=100 && where<199) {index=where-100;
				waypoints.get(index).data[13]=pressPositionX;
				waypoints.get(index).data[14]=pressPositionY;
			}
			else if (where>=300 && where<399) {index=where-300;
			waypoints.get(index).data[17]=pressPositionX;
			waypoints.get(index).data[18]=pressPositionY;
		}
			else if (where>=400 && where<499) {index=where-400;
			if(waypoints.get(index).data[8]!=3){
			waypoints.get(index).data[19]=pressPositionX;
			waypoints.get(index).data[20]=pressPositionY;
			}
		}
			
			waypoints.get(index).calculateDraggedCP(index,where);
			calculateProfile();
		});

		data.getNode().setOnMouseReleased(event->{
			int index=0;
			int where = Integer.parseInt(data.getNode().getId());
			if(where>=100 && where<200) {index=where-100;}
			else if(where>=200 && where<300) {index=where-200;}
			else if(where>=300 && where<400) {index=where-300;}
			else if(where>=400 && where<500) {index=where-400;}			
			waypoints.get(index).calculateDraggedCP(index,where);
			wpTable.updateTable();		
		});

}	

	
public static void getTimeStamp(Data<Number, Number> data) {
		
		data.getNode().setOnMouseClicked(event->{
			if(Profile.time.size()>0) {			
				double time;
				int indexPosition=Integer.parseInt(data.getNode().getId());
				double d=profile.pdist_display.get(indexPosition);
				int indexTime = Arrays.binarySearch(Profile.dist2.toArray(),d);
				if(indexTime<0) indexTime=-indexTime;
				if(indexTime>1)indexTime=indexTime-2;
				else indexTime=0;

				if (indexTime<=Profile.time.size()-2) {
					time = Profile.time.get(indexTime)+( Profile.time.get(indexTime+1) 
							-Profile.time.get(indexTime) )*
							(d-Profile.dist2.get(indexTime))/(Profile.dist2.get(indexTime+1) - 
									Profile.dist2.get(indexTime));
				}
				else time=Profile.time.get(Profile.dist2.size()-1);		  		
				time=Utils.round(time);
				taMessages.setText("t="+String.valueOf(time));				
				}		
		});
}
	
	
	static public void calculateProfile() {
		double xtl,ytl,xtr,ytr;
		seriesProfile.getData().clear();
		seriesProfileL.getData().clear();
		seriesProfileR.getData().clear();
		profile.calculatePath();
		bHidePath.setText("HidePath");
		tfLength.setText(String.valueOf(Utils.round(profile.totalLength)));
		
		vdaWindow.tfLength.setText(String.valueOf(Utils.round(profile.totalLength)));
				System.out.println(" num pts "+profile.pointsInDisplayProfile);
		for(int j=0;j< profile.pointsInDisplayProfile;j++) {			
			double xt = profile.px_display.get(j);
			double yt = profile.py_display.get(j);			
			XYChart.Data<Number, Number> prof = new XYChart.Data<Number, Number>(xt,yt);		
			seriesProfile.getData().add(prof);
			prof.getNode().setId(String.valueOf(j));
			getTimeStamp(prof);
			
			xtl = profile.pxL_display.get(j);
			ytl = profile.pyL_display.get(j);			
			XYChart.Data<Number, Number> profL = new XYChart.Data<Number, Number>(xtl,ytl);		
			seriesProfileL.getData().add(profL);

			xtr = profile.pxR_display.get(j);
			ytr = profile.pyR_display.get(j);			
			XYChart.Data<Number, Number> profR = new XYChart.Data<Number, Number>(xtr,ytr);		
			seriesProfileR.getData().add(profR);
			
			
			if(profile.pdir.get(j)==-1) {
				prof.getNode().setStyle("-fx-background-color: #006400, green;\n");
				profL.getNode().setStyle("-fx-background-color: #006400, green;\n");
				profR.getNode().setStyle("-fx-background-color: #006400, green;\n");
			}
			
			
		}

	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
