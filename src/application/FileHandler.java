package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class FileHandler {
	static BufferedWriter bw;
	static BufferedReader br;

//  0  1    2     3        4   5    6   7    8     9       10     11     12   13     14	  15   16   17   18  19    20   21    22
//	x, y, angle,  ctrlD   jm,  am, vm,  vf  type, rotate,  fit,  angle3 cd3  angle4 cd4  cx1, cy1, cx2, cy2  cx3   cy3  cx4   cy4

//  0  1    2     3        4   5    6   7    8     9       10     11   12   13   14   15   16  17    18   19    20
//  x, y, angle,  ctrlD   jm,  am, vm,  vf  type, rotate,  fit,  cd3  cd4  cx1, cy1, cx2, cy2  cx3   cy3  cx4   cy4		
	
static public void saveWaypoints() {
	try {
		int i=0, k=0;
		double[] data = new double[13];
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(Main.window);
		
		// enforce correct file extensions - or else we tend to inadvertantly overwrite files !!!		
				String name=file.getAbsolutePath();
				String extension = "";
				i = name.lastIndexOf('.');
				if (i > 0) {
				    extension = (name.substring(i+1));
					if(!(extension.equals("wp")) ) {
						Main.taMessages.appendText("Not a waypoint file!");
						return;
					}		    
				}
				if (i==-1) {
					file=null;
					file=new File(name+".wp");
				}

		
		bw= new BufferedWriter(new FileWriter(file));
		int size = Main.waypoints.size();
		i = 0;k=0;
		String s="";
		bw.append(String.valueOf(size));
		bw.newLine();

		while (i <size) {
			data=Main.waypoints.get(i).data;		
			k=0;
			while(k<12) {
				s=s+String.valueOf(data[k])+",";
				k++;
			}
			s=s+data[12];
			bw.append(s);
			bw.newLine();
			k=0;
			s="";
			i++;
		}
		bw.close();
		Main.taMessages.appendText("\n"+"Waypoints saved to:"+"\n");
		Main.taMessages.appendText(file.getName());

	} catch (IOException e) {
		e.printStackTrace();
	}
	
}

static public void loadWaypoints() {
	try {
		double[] data = new double[13];
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("WP", "*.wp"));
		File filename = fileChooser.showOpenDialog(Main.window);
		br= new BufferedReader(new FileReader(filename));

		int i = 0,k=0;
		String s;
		String[] sarray = new String[13];
		int size = Integer.parseInt(br.readLine());
		while (i <size) {
			s=br.readLine();
			sarray=s.split(",");
			k=0;
			while(k<13) {
				data[k]=Double.parseDouble(sarray[k]);
				k++;
			}
	    	Main.waypoints.add(new Waypoint(data));
	    	i++;
		}
		br.close();
	} catch (IOException e) {
		e.printStackTrace();
	}

}

static public void saveProfile(Profile profile,boolean rio) {
//	dataOutputStream = null;
	try {
		 FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(Main.window);

// enforce correct file extensions - or else we tend to inadvertantly overwrite files !!!		
		String name=file.getAbsolutePath();
		String extension = "";
		int i = name.lastIndexOf('.');
		if (i > 0) {
		    extension = (name.substring(i+1));
			if(!(extension.equals("profile")) ) {
				Main.taMessages.appendText("Not a profile file!");
				return;
			}		    
		}
		if (i==-1) {
			System.out.println("B");
			file=null;
			file=new File(name+".profile");
		}
		
		bw= new BufferedWriter(new FileWriter(file));
		System.out.println("A:filename "+file);
		int size = Profile.time.size();
		System.out.println("Size = "+size);
		i = 0;
		String s="",pointnum,time,pos,vel,duration,heading,vell,velr,posl,posr,jerk,
				acc,accl,accr,actions1,actions2;
		double temp;
		duration=String.valueOf(Constants.duration);


// convert values as needed to required form for motion profile executor
// pointnum - integer, only included for diagnostics
// time - integer in milliseconds,  only included for diagnostics
// postion - integer, distance converted to encoder units
// velocity - integer, speed converted to encoder units per 100 ms
// duration - integer, ms
// heading - integer, angle converted to pigeon units		
		s = String.valueOf(size);
		bw.append(s);	
		bw.newLine();
		while (i <size-1) {	
			s="";
			pointnum=String.valueOf(i);			
			
			time=String.valueOf(  (int) (Profile.time.get(i)*1000));
			jerk = String.format ("%.3f", Profile.jerk.get(i));
			acc= String.format ("%.3f", Profile.acc.get(i));			
			pos= String.format ("%.3f", Profile.dist2.get(i));
			vel = String.format ("%.3f", Profile.vel.get(i));
			posl= String.format ("%.3f", Profile.dist2L.get(i));
			vell = String.format ("%.3f", Profile.velL.get(i));
			accl = String.format ("%.3f", Profile.accL.get(i));
			posr= String.format ("%.3f", Profile.dist2R.get(i));
			velr = String.format ("%.3f", Profile.velR.get(i));
			accr = String.format ("%.3f", Profile.accR.get(i));
			heading = String.format ("%.3f", Profile.angle2.get(i));
			actions1=String.valueOf((int)(Profile.actions.action1.get(i)));
			actions2=String.valueOf((int)(Profile.actions.action2.get(i)));

			
			if(Defaults.cbNum.isSelected()) s=s+","+pointnum;
			if(Defaults.cbTime.isSelected()) s=s+","+time;
			if(Defaults.cbDuration.isSelected()) s=s+","+duration;
			if(Defaults.cbJerk.isSelected()) s=s+","+jerk;
			if(Defaults.cbAcc.isSelected()) s=s+","+acc;
			if(Defaults.cbLRAcc.isSelected()) s=s+","+accl+","+accr;
			if(Defaults.cbVel.isSelected()) s=s+","+vel;
			if(Defaults.cbLRVel.isSelected()) s=s+","+velr+","+vell;
			if(Defaults.cbPos.isSelected()) s=s+","+pos;
			if(Defaults.cbLRPos.isSelected()) s=s+","+posr+","+posl;
			if(Defaults.cbAngle.isSelected()) s=s+","+heading;
			if(Defaults.cbActions.isSelected())s=s+","+actions1+","+actions2;
			s=s.substring(1);  // strip the leading ,			
			bw.append(s);	
			bw.newLine();
			i++;
		}
		bw.close();
		Main.taMessages.appendText("\n"+"Waypoints saved to:"+"\n");
		Main.taMessages.appendText(file.getName());
		if(rio) {
			SendMyFiles smf = new SendMyFiles();
			System.out.println("Sending file "+file+" to roborio");
			smf.send(file);
			Main.taMessages.appendText("\n"+"Files sent to Rio");
		}

	} catch (IOException e) {
		Main.taMessages.appendText("\n"+"File transfer failed");
		e.printStackTrace();
	}
	
	
}



static public void dataDump() {
	boolean[] savedata = new boolean[28];
	savedata=whatToSave();		
	String[] labels = {"x","y","xL","yL","xR","yR","Angle1", "Pos1L","Pos1R","Pos1",
			"Dist1","Dist1L","Dist1R","time","Pos2L","Pos2R","Pos2","Dist2 L","Dist2 R","Dist2",
			"Jerk","AccL","AccR","Acc","VelL","VelR","Vel","angle2"};
	
	try {
		 FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(Main.window);

// enforce correct file extensions - or else we tend to inadvertantly overwrite files !!!		
		String name=file.getAbsolutePath();
		String extension = "";
		int i = name.lastIndexOf('.');
		if (i > 0) {
		    extension = (name.substring(i+1));
			if(!(extension.equals("csv")||extension.equals("CSV") ) ) {
				Main.taMessages.appendText("Not a csv file!");
				return;
			}		    
		}
		if (i==-1) {
			file=null;
			file=new File(name+".csv");
		}
		
		bw= new BufferedWriter(new FileWriter(file));
		
		String s="";
		int size = Profile.dist1.size();

		i = 0;
		
		while(i<=27) {
			if (savedata[i]) s=s+labels[i]+",";
			i++;
		}
		
		bw.append(s);
		bw.newLine();
		s="";	
		

		i=0;
		System.out.println("size = "+size);
		while (i <size-1) {
			if(savedata[0])s=s+ String.valueOf(Profile.px.get(i))+",";
			if(savedata[1])s=s+ String.valueOf(Profile.py.get(i))+",";
			if(savedata[2])s=s+ String.valueOf(Profile.pxL.get(i))+",";
			if(savedata[3])s=s+ String.valueOf(Profile.pyL.get(i))+",";
			if(savedata[4])s=s+ String.valueOf(Profile.pxR.get(i))+",";						
			if(savedata[5])s=s+ String.valueOf(Profile.pyR.get(i))+",";
			if(savedata[6])s=s+ String.valueOf(Profile.angle1.get(i))+",";
			if(savedata[7])s=s+ String.valueOf(Profile.pos1L.get(i))+",";
			if(savedata[8])s=s+ String.valueOf(Profile.pos1R.get(i))+",";
			if(savedata[9])s=s+ String.valueOf(Profile.pos1.get(i))+",";
			if(savedata[10])s=s+ String.valueOf(Profile.dist1L.get(i))+",";
			if(savedata[11])s=s+ String.valueOf(Profile.dist1R.get(i))+",";
			if(savedata[12])s=s+ String.valueOf(Profile.dist1.get(i))+",";


			if(i<Profile.time.size()) {
				if(savedata[13])s=s+ String.valueOf(Profile.time.get(i))+",";
				if(savedata[14])s=s+ String.valueOf(Profile.pos2L.get(i))+",";
				if(savedata[15])s=s+ String.valueOf(Profile.pos2R.get(i))+",";
				if(savedata[16])s=s+ String.valueOf(Profile.pos2.get(i))+",";
				if(savedata[17])s=s+ String.valueOf(Profile.dist2L.get(i))+",";
				if(savedata[18])s=s+ String.valueOf(Profile.dist2R.get(i))+",";
				if(savedata[19])s=s+ String.valueOf(Profile.dist2.get(i))+",";
				if(savedata[20])s=s+ String.valueOf(Profile.jerk.get(i))+",";
				if(savedata[21])s=s+ String.valueOf(Profile.accL.get(i))+",";				
				if(savedata[22])s=s+ String.valueOf(Profile.accR.get(i))+",";				
				if(savedata[23])s=s+ String.valueOf(Profile.acc.get(i))+",";				
				if(savedata[24])s=s+ String.valueOf(Profile.velL.get(i))+",";
				if(savedata[25])s=s+ String.valueOf(Profile.velR.get(i))+",";
				if(savedata[26])s=s+ String.valueOf(Profile.vel.get(i))+",";
				if(savedata[27])s=s+ String.valueOf(Profile.angle2.get(i))+",";
			}	
		bw.append(s);
		bw.newLine();
		s="";
		i++;
	}

	
	bw.close();
	Main.taMessages.appendText("\n"+"Data saved to:"+"\n");
	Main.taMessages.appendText(file.getName());

	} catch (IOException e) {
		Main.taMessages.appendText("\n"+"File transfer failed");
		e.printStackTrace();
	}
	
	
}

   
    
public static boolean[] whatToSave()
{
	CheckBox checkbox[]=new CheckBox[28];
	String[] labels = {"x","y","xL","yL","xR","yR","Angle1","Pos1L","Pos1R","Pos","Dist1L","Dist1R", "Dist1",
			"time","Pos2L","Pos2R","Pos2","Dist2 L","Dist2 R","Dist2","Jerk","AccL","AccR","Acc",
			"VelL","VelR","Vel","angle2"};
	boolean[] savedata = new boolean[28];
	Stage popupwindow=new Stage();   
	popupwindow.initModality(Modality.APPLICATION_MODAL);
	popupwindow.setTitle("Data Dump");            
	Label label1= new Label("Choose what data to save");
	Button button1= new Button("OK");          
	button1.setOnAction(e -> {
		int j = 0;
		while (j<=27) {
			savedata[j]=checkbox[j].isSelected();
			j++;
		}
		popupwindow.close();
		
	});	
	
	VBox layout= new VBox(10);           
	GridPane gridpane = new GridPane();
	gridpane.setHgap(30);gridpane.setVgap(10);gridpane.setPadding(new Insets(10,5,20,20));
	layout.getChildren().addAll(label1,gridpane,button1);
	int i = 0;
	while(i<=27) {
		checkbox[i]=new CheckBox(labels[i]);
		checkbox[i].setIndeterminate(false);

		gridpane.add(checkbox[i],(i/8),i%8);
		i++;
	}
	checkbox[13].setSelected(true);
	
	Scene scene1= new Scene(layout, 350, 400);      
	popupwindow.setScene(scene1);      
	popupwindow.showAndWait();   
	

	return savedata;
}



}
