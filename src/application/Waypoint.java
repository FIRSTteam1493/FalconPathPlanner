package application;

import javafx.scene.chart.XYChart;

public class Waypoint {
	static final double eps = 0.000001;
	static final double ctrlDistDef=5;
//  0  1    2     3        4   5    6   7    8     9       10     11   12   13   14   15   16  17    18   19    20
// 	x, y, angle,  ctrlD   jm,  am, vm,  vf  type, rotate,  fit,  cd3  cd4  cx1, cy1, cx2, cy2  cx3   cy3  cx4   cy4
	
	
	static double[] dataDefault =  new double[] {0,0,-9999,10,40,20,15,0,1,0,3,15,15,-9999,
			-9999,-9999,-9999,-9999,-9999,-9999,-9999};
	static int id=-1;
	double[] data = new double[21];
	double prevAngle=0,prevX=0,prevY=0;
	
	Waypoint(double[] data_){
		id ++;
		int i=0;
		while(i<13) {
			data[i]=data_[i];
			i++;
		}	
		checkValues();
		calculateCtrlPoints();		
		addWPtoChart(id);
		
	}
		
	Waypoint(NumberField[] numfields){
		id ++;
		int i = 0;
		while (i<=20) {data[i]=dataDefault[i];i++;}
		i=0;
		while (i<=11) {
			data[i]=numfields[i+1].getDouble(); 
			if(data[i]<-9998) {
				data[i]=dataDefault[i];
// keep angle field blank so we can continue specifying the default action of calculating angle from wp coord.				
				if(i !=2)  Main.numfields[i+1].setText(String.valueOf(dataDefault[i]));
			}
			i++;
		}
		data[10]=Main.fitType;
		checkValues();
		calculateCtrlPoints();
		addWPtoChart(id);
	}

	

	
	public void calculateCtrlPoints() {
		double angle=data[2],dx=0,dy=0;
		int index=Main.insertIndex-1;
// if no angle provided, calculate angle from slope between WP and previous WP		
		if(angle<-9998) {
			dy =(data[1]-Main.waypoints.get(index).data[1]);
			dx = data[0]-Main.waypoints.get(index).data[0];  
			angle = 180.0*Math.atan( dy/(eps+ dx))/Math.PI ;
			if(dx<0 && dy>0 )angle = angle+180;
			if(dx<0 && dy<0)angle = angle-180;
			data[2]=angle;
		}
		data[15]=data[0]+data[3]*Math.cos(angle*Math.PI/180. );
		data[16]=data[1]+data[3]*Math.sin(angle*Math.PI/180. );
		if (data[8]==3.0) {
			double rotate=data[9]*Math.PI/180.;
			double dxrotate=data[15]-data[0],dyrotate=data[16]-data[1];
			data[15]=data[0]+dxrotate*Math.cos(rotate)-dyrotate*Math.sin(rotate);
			data[16]=data[1]+dxrotate*Math.sin(rotate)+dyrotate*Math.cos(rotate);			
		}
		
		data[13]=data[0]+data[3]*Math.cos(Math.PI+angle*Math.PI/180. );
		data[14]=data[1]+data[3]*Math.sin(Math.PI+angle*Math.PI/180. );				
		data[2]=angle;

		if (Main.fitType==5) {
			data[17]=data[0]+data[11]*Math.cos(Math.PI+angle*Math.PI/180. );
			data[18]=data[1]+data[11]*Math.sin(Math.PI+angle*Math.PI/180. );					
			data[19]=data[0]+data[12]*Math.cos(angle*Math.PI/180. );
			data[20]=data[1]+data[12]*Math.sin(angle*Math.PI/180. );					
			double rotate=data[9]*Math.PI/180.;
			double dxrotate=data[19]-data[0],dyrotate=data[20]-data[1];
			data[19]=data[0]+dxrotate*Math.cos(rotate)-dyrotate*Math.sin(rotate);
			data[20]=data[1]+dxrotate*Math.sin(rotate)+dyrotate*Math.cos(rotate);			
		}			
	}

	public void calculateDraggedCP(int numwp, int where) {
		double dx,dy;
		
		if (where>=200 && where <300) {
			if(data[8]!=3) {
			dx = data[15]-data[0];
			dy=data[16]-data[1];
			data[2]= Utils.angle(dx,dy);
			data[3]=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2) );
		}
		}
		else if (where>=100 && where <200) {
			dx = data[0]-data[13];
			dy=data[1]-data[14];
			data[2] = Utils.angle(dx,dy);
			data[3]=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2) );
		}
			
		else if (where>=400 && where <500) {
			if(data[8]!=3) {
			dx = data[19]-data[0];
			dy=data[20]-data[1];
			data[12]=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2) );
			}
		}
		else if (where>=300 && where <400) {
			dx = data[0]-data[17];
			dy=data[1]-data[18];
			data[11]=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2) );				
		}

// data: 2=ctrl angle	3=ctrl dist   12=2nd ctrl dist	14=3rd ctrl dist		

		data[13]=data[0]+data[3]*Math.cos(Math.PI+data[2]*Math.PI/180. );
		data[14]=data[1]+data[3]*Math.sin(Math.PI+data[2]*Math.PI/180. );
		data[15]=data[0]+data[3]*Math.cos(data[2]*Math.PI/180. );
		data[16]=data[1]+data[3]*Math.sin(data[2]*Math.PI/180. );
		
		data[17]=data[0]+data[11]*Math.cos(Math.PI+data[2]*Math.PI/180. );
		data[18]=data[1]+data[11]*Math.sin(Math.PI+data[2]*Math.PI/180. );				
		data[19]=data[0]+data[12]*Math.cos(data[2]*Math.PI/180. );
		data[20]=data[1]+data[12]*Math.sin(data[2]*Math.PI/180. );
		
		
	}

	
//  0  1    2     3        4   5    6   7    8     9       10     11   12   13   14   15   16  17    18   19    20
//  x, y, angle,  ctrlD   jm,  am, vm,  vf  type, rotate,  fit,  cd3  cd4  cx1, cy1, cx2, cy2  cx3   cy3  cx4   cy4

	public void addWPtoChart(int id) {
		XYChart.Data xycd = new XYChart.Data(data[0],data[1]);
		Main.seriesWaypoints.getData().add(xycd);
		xycd.getNode().setId(String.valueOf(id));
		Main.mouseHandlerWP(xycd);

		XYChart.Data c1cd = new XYChart.Data(data[13],data[14]);
		Main.seriesControlPoints.getData().add(c1cd);		
		c1cd.getNode().setId(String.valueOf(100+id));
		Main.mouseHandlerCP(c1cd);
		
		XYChart.Data c2cd = new XYChart.Data(data[15],data[16]);
		Main.seriesControlPointsOut.getData().add(c2cd);
		c2cd.getNode().setId(String.valueOf(200+id));
		Main.mouseHandlerCP(c2cd);
		
		if(Main.fitType==5) {
		XYChart.Data c3cd = new XYChart.Data(data[17],data[18]);
		Main.seriesControlPoints2.getData().add(c3cd);		
		c3cd.getNode().setId(String.valueOf(300+id));
		Main.mouseHandlerCP(c3cd);
		
		XYChart.Data c4cd = new XYChart.Data(data[19],data[20]);
		Main.seriesControlPointsOut2.getData().add(c4cd);
		c4cd.getNode().setId(String.valueOf(400+id));
		Main.mouseHandlerCP(c4cd);
		}
	}
	
	private void checkValues() {
		// set Vfinal=0 if rotating, pausing or stopping 
		if (data[8]==2|| data[8]==3 || data[8]==4) data[7]=0;
		// set rotate to 0 if running 
		if (data[8]==1) {data[9]=0;}
		if (data[8]<0 || data[8]>4) data[8]=1;
	}
	
}