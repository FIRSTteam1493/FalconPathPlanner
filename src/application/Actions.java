package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Actions {
    final Stage stageActionInput = new Stage();
	List<Double> time = new ArrayList<Double>();
	List<Integer> action1= new ArrayList<Integer>();
	List<Integer> action2= new ArrayList<Integer>();
	List<Double> timePoint= new ArrayList<Double>();
	List<Integer> a1Point= new ArrayList<Integer>();
	List<Integer> a2Point= new ArrayList<Integer>();
    TextField tfTime = new TextField();
    TextField tfActionVal1 = new TextField();
    TextField tfActionVal2 = new TextField();
    TextField tfDeletePoint= new TextField("");
    TextArea taAction = new TextArea();
    
	Actions(){
        stageActionInput.initModality(Modality.NONE);
        stageActionInput.initOwner(null);
        VBox vbActionInput = new VBox();

        Label lblTime = new Label ("Time");
        tfTime.setPrefWidth(50);
        HBox hbox5 = new HBox();
        hbox5.getChildren().addAll(lblTime,tfTime);
		hbox5.setSpacing(5);
	    hbox5.setPadding(new Insets(10, 0, 0, 10));			

        Label lblActionVal = new Label ("Action Values");
        tfActionVal1.setPrefWidth(50);
        tfActionVal2.setPrefWidth(50);
        HBox hbox6 = new HBox();
        hbox6.getChildren().addAll(lblActionVal,tfActionVal1,tfActionVal2);
		hbox6.setSpacing(5);
	    hbox6.setPadding(new Insets(10, 0, 0, 10));			

        Button btnOK = new Button("Save");
        Button btnDeletePoint = new Button("Delete");
        tfDeletePoint.setPrefWidth(50);
        Button btnHide = new Button("Hide");
        HBox hbox7 = new HBox();
        hbox7.getChildren().addAll(btnOK,btnDeletePoint,tfDeletePoint);
		hbox7.setSpacing(5);
	    hbox7.setPadding(new Insets(10, 0, 0, 10));			
        
	    HBox hbox8 = new HBox();
	    taAction.setMaxWidth(300);
	    taAction.setEditable(false);
	    hbox8.getChildren().addAll(taAction);
		hbox8.setSpacing(5);
	    hbox8.setPadding(new Insets(10, 0, 0, 10));			

	    vbActionInput.setSpacing(5);
	    vbActionInput.setPadding(new Insets(10, 0, 0, 10));			

	    vbActionInput.getChildren().addAll(hbox5,hbox6,hbox7,hbox8,btnHide);
        
        Scene sceneActionInput= new Scene(vbActionInput,400,400);
        stageActionInput.setTitle("Enter Robot Actions");

        stageActionInput.setScene(sceneActionInput);
        stageActionInput.hide();
        
        timePoint.add(0.);
        a1Point.add(0);
        a2Point.add(0);

		btnHide.setOnAction(value ->  {
	        stageActionInput.hide();
		});	    		
		
		btnOK.setOnAction(value ->  {
	        save();
		});	    		
		
		btnDeletePoint.setOnAction(value ->  {
	        delete();
		});	    			
	}
	
	public void showHide() {
		if (stageActionInput.isShowing()) stageActionInput.hide();
		else stageActionInput.show();
	}

	public void save() {
		double t=Double.valueOf(tfTime.getText());
		try {
			int a1 = Integer.valueOf(tfActionVal1.getText());
			int a2 =Integer.valueOf(tfActionVal2.getText());
			timePoint.add(t);
			a1Point.add(a1);
			a2Point.add(a2);
			sort();
			display();
		}
		catch(Exception e) {
			taAction.setText("Not a number");
		}
	}
	
	public void delete() {
		int index=Integer.valueOf(tfDeletePoint.getText())-1;
			timePoint.remove(index);
			a1Point.remove(index);
			a2Point.remove(index);
			sort();
			display();
	}
	

	public void addToActionProfile(double t) {
		time.add(t);
		int index = Arrays.binarySearch(timePoint.toArray(),t);
		if(index<0) index=-index;
		if(index>1)index=index-2;else index=0;
		int val1=a1Point.get(index);
		int val2=a2Point.get(index);
		action1.add(val1);
		action2.add(val2);

	}	
	
	public void clear() {
		time.clear();
		action1.clear();
		action2.clear();		
	}

	
	public void display() {
		String s="";
		taAction.clear();
		int size=timePoint.size();
		int i=0;
		while (i<size) {
			s=s+timePoint.get(i)+"  "+a1Point.get(i)+"  "+a2Point.get(i)+"\n";
			i++;
		}
		taAction.appendText(s);
	}

	private void sort() {
		Utils.concurrentSort(timePoint, timePoint,a1Point,a2Point);
		
	}
	
	
	
	
	
}
