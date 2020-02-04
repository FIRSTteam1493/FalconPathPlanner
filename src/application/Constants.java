package application;

public class Constants {
	public static final int pigeonUnitsPerRotation=8192;
	public static int encoderUnitsPerRotation=512;  // for 2019 comp robot
	public static double wheelDiameter = 5.4;  // for 2019 comp robot
	public static double wheelBase = 23;  // for 2019 comp robot

// conversion from inches per second to encoder	units per 100ms,  
// multiply by 2 because we are using a sensor sum as the feedback sensor
	public static final double InchPerSec_encoderUnitsPer100ms=2.0*2.7162;

// encoderUnitsPerInch is multiplied by 2 because we are using a sensor sum as the feedback sensor	
	public static final int encoderUnitsPerInch = (int) (2*1.0*encoderUnitsPerRotation/(Math.PI*wheelDiameter));

//duration of each time step in mSec	
	public static final int duration = 10;  

//	angular velocity used for rotate in place manuever, in degrees per second
	public static double rotRate=45.0;

// small number, added to denominator of slope calculations to avoid divide by zero error	
	public static final double eps = 0.000001;

// waypoint data for first two waypoints 	
	public static final double[] initWpData1_3 = {10,50,0,10,Waypoint.dataDefault[4],Waypoint.dataDefault[5],
			Waypoint.dataDefault[6],0,1,0,3,10,10};
	public static final double[] initWpData2_3 = {50,50,0,10,Waypoint.dataDefault[4],Waypoint.dataDefault[5],
			Waypoint.dataDefault[6],0,0,0,3,30,30};
	public static final double[]  initWpData1_5={10,50,0,10,Waypoint.dataDefault[4],Waypoint.dataDefault[5],
			Waypoint.dataDefault[6],0,1,0,5,10,10};
	public static final double[]  initWpData2_5={40,50,0,10,Waypoint.dataDefault[4],Waypoint.dataDefault[5],
			Waypoint.dataDefault[6],0,0,0,5,10,10};


	
}
