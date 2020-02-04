package application;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;

public class FieldElements {
    List<Rectangle> rectangle = new ArrayList<Rectangle>();

	FieldElements(){
	   addRectangle(0,512.5/2,0,378.168/2,Color.INDIGO,3);
	   addRectangle(1258.5/2,512.5/2,0,378.168/2,Color.INDIGO,3);
	   addRectangle(628-130-(12*18),111/2,18*12,111/2,Color.CYAN,3);
	   addRectangle(120,((26*12)+11.25),0,((26*12)+11.25),Color.STEELBLUE,3);
	   addRectangle(504.25,((26*12)+11.25),0,((26*12)+11.25),Color.STEELBLUE,3);
	   addRectangle(130,((26*12)+11.25),18*12,111/2,Color.CYAN,3);
	   addRectangle((566.5+72)/2,111/2,72,111/2,Color.DARKGOLDENROD,3);
	   addRectangle(130+144-36,((26*12)+11.25),72,111/2,Color.DARKGOLDENROD,3);
	   addLine(130+144,((26*12)+11.25-(4*12)-7.5),130,((26*12)+11.25-(4*12)-7.5)-108,Color.OLIVE,2);
	   addLine(130,((26*12)+11.25-(4*12)-7.5)-108,(566.5+72)/2+36,111/2,Color.OLIVE,2);
	   addLine((566.5+72)/2+36,111/2,504.25-10,111/2+108,Color.OLIVE,2);
	   addLine(130+144,((26*12)+11.25-(4*12)-7.5),504.25-10,111/2+108,Color.OLIVE,2);
	   addLine(130+144+109,((26*12)+11.25-(4*12)-7.5)-51,130+109,((26*12)+11.25-(4*12)-7.5)-108-51,Color.OLIVE,2);
	}
	
	private void addRectangle( double x, double y, double len, double width, Color color, int stroke) {
//		
		double factor1x = Main.xAxis.sceneToLocal(new Point2D(100,100)).getX();
		double factor2x = (double)Main.xAxis.getValueForDisplay(factor1x);
		double xslope=factor1x/factor2x;
		
		double ry1a = (double)Main.yAxis.getValueForDisplay(Main.yAxis.sceneToLocal(new Point2D(100,400)).getY());
		double ry1b = (double)Main.yAxis.getValueForDisplay(Main.yAxis.sceneToLocal(new Point2D(100,200)).getY());
		double yslope = (400.-200.)/(ry1a-ry1b);
	
		double rx=Main.xAxis.localToScene(new Point2D(0,0)).getX()+x*xslope;		
		double ry=400+yslope*(y-ry1a);
		
		Rectangle rectangle = new Rectangle(rx,ry,len*Math.abs(xslope),width*Math.abs(yslope));
		rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(color);
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(stroke);
	    Main.anchorPane.getChildren().add(rectangle);

		
	}

	private void addLine( double x1, double y1, double x2, double y2, Color color, int stroke) {
//		
		double factor1x = Main.xAxis.sceneToLocal(new Point2D(100,100)).getX();
		double factor2x = (double)Main.xAxis.getValueForDisplay(factor1x);
		double xslope=factor1x/factor2x;
		
		double ry1a = (double)Main.yAxis.getValueForDisplay(Main.yAxis.sceneToLocal(new Point2D(100,400)).getY());
		double ry1b = (double)Main.yAxis.getValueForDisplay(Main.yAxis.sceneToLocal(new Point2D(100,200)).getY());
		double yslope = (400.-200.)/(ry1a-ry1b);
	
		double rx1=Main.xAxis.localToScene(new Point2D(0,0)).getX()+x1*xslope;		
		double rx2=Main.xAxis.localToScene(new Point2D(0,0)).getX()+x2*xslope;		
		
		double ry1=400+yslope*(y1-ry1a);
		double ry2=400+yslope*(y2-ry1a);
		
		Line line = new Line (rx1,ry1,rx2,ry2);
		line.setFill(Color.TRANSPARENT);
        line.setStroke(color);
        line.setStroke(color);
        line.setStrokeWidth(stroke);
	    Main.anchorPane.getChildren().add(line);

		
	}

	
}
