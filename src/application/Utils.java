package application;
import java.util.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.lang.*;
import java.io.*;
public class Utils {

	
	Utils(){
		
	}

	
// Round to the nearest hundredth	
	static double round (double val) {
		val=Math.round(val*1000.)/1000.;
		return val;
	}
	
	public static double angle(double dx,double dy) {
		double angle;
		angle = 180.0*Math.atan( (dy)/(Constants.eps+ dx))/Math.PI ;
		if(dx<0 && dy>0 )angle = angle+180;
		if(dx<0 && dy<0)angle = angle-180;
		return angle;
	}
	
	public static boolean checkStop() {
		boolean endStop=true;
		int lastWP=Main.waypoints.size();
		if (Main.waypoints.get(lastWP-1).data[8]==1) endStop=false;
		return endStop;
	}

	public static void printarray(int size, double[][] a) {
		int i=0,j=0;
		String s="";
		while(i<size) {
			while (j<size)
			{
				s=s+String.valueOf(a[i][j])+"  ";
				j++;
				}
			System.out.println(s);
			s="";
			j=0;
			i++;
			}	
	}
	
	public static void printarray(int size, double[] a) {
		int j=0;
		String s="";
			while (j<size)
			{
				s=s+String.valueOf(a[j])+"  ";
				j++;
				}
			System.out.println(s);
	
	}
	
	
	// Sorts the list of actions by increasing time. Found this method online (sorts an array of lists 
	// using one of the lists as a key)	
			public static <T extends Comparable<T>> void concurrentSort( final List<T> key, List<?>... lists){
		        // Do validation
		        if(key == null || lists == null)
		        	throw new NullPointerException("key cannot be null.");
		 
		        for(List<?> list : lists)
		        	if(list.size() != key.size())
		        		throw new IllegalArgumentException("all lists must be the same size");
		 
		        // Lists are size 0 or 1, nothing to sort
		        if(key.size() < 2)
		        	return;
		 
		        // Create a List of indices
				List<Integer> indices = new ArrayList<Integer>();
				for(int i = 0; i < key.size(); i++)
					indices.add(i);
		 
		        // Sort the indices list based on the key
				Collections.sort(indices, new Comparator<Integer>(){
					@Override public int compare(Integer i, Integer j) {
						return key.get(i).compareTo(key.get(j));
					}
				});
		 
				Map<Integer, Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
				List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
							  swapTo   = new ArrayList<Integer>(indices.size());
		 
		        // create a mapping that allows sorting of the List by N swaps.
				for(int i = 0; i < key.size(); i++){
					int k = indices.get(i);
					while(i != k && swapMap.containsKey(k))
						k = swapMap.get(k);
		 
					swapFrom.add(i);
					swapTo.add(k);
					swapMap.put(i, k);
				}
		 
		        // use the swap order to sort each list by swapping elements
				for(List<?> list : lists)
					for(int i = 0; i < list.size(); i++)
						Collections.swap(list, swapFrom.get(i), swapTo.get(i));
			}
			

			
/*solves Ax = v where A is a tridiagonal matrix consisting of vectors a, b, c
	x - initially contains the input vector v, and returns the solution x. indexed from 0 to X - 1 inclusive
	X - number of equations (length of vector x)
	a - subdiagonal (means it is the diagonal below the main diagonal), indexed from 1 to X - 1 inclusive
	b - the main diagonal, indexed from 0 to X - 1 inclusive
	c - superdiagonal (means it is the diagonal above the main diagonal), indexed from 0 to X - 2 inclusive
*/					     			
			static public double[] solveMatrix(double[] _a, double[] _b, double[] _c, double[] x) {
				int i=0;						
				int len = _b.length;
				double[] a = new double[len];
				double[] b = new double[len];
				double[] c = new double[len];
				while (i<len) {
					a[i]=_a[i]; b[i]=_b[i]; c[i]=_c[i]; 
					i++;
				}
				double m;
				i=1;
			    c[0] = c[0] / b[0];
			    x[0] = x[0] / b[0];
			    
			    /* loop from 1 to X - 1 inclusive, performing the forward sweep */
			    while(i<len) {
			        m = 1.0f / (b[i] - a[i] * c[i-1]);
			        c[i] = c[i] * m;
			        x[i] = (x[i] - a[i] * x[i-1])*m;
			        i++;
			    }
			    i=len-2;
			    /* loop from X - 2 to 0 inclusive (safely testing loop condition for an unsigned integer), to perform the back substitution */
			    while(i>=0) {
			        x[i]-=c[i]*x[i+1];
			        i--;
			}								
				return x;				
		}
	
//***************************************************************
//* Use Apache Math library to solve the linear system
//* of equations that determine the coefficients P1, P2 in the 
//* quintic bezier spline
//***************************************************************			
	static public double[] solveLinSystem(int size, double[] d) {
		double[][] a = new double[2*size][2*size];
		int i=0,j=0,m=0,n=0;
		while (i<2*size) {
			while(j<2*size) {
				a[i][j]=0;
				j++;
			}
			i++;
		}
		a[0][0]=1;
		a[1][1]=1;
		a[2*size-2][2*size-4]=1;
		a[2*size-2][2*size-3]=-4;
		a[2*size-2][2*size-2]=-16;
		a[2*size-2][2*size-1]=0;
		a[2*size-1][2*size-4]=0;
		a[2*size-1][2*size-3]=1;
		a[2*size-1][2*size-2]=12;
		a[2*size-1][2*size-1]=-6;
		
		i=1;
		while(i<size-1) {
			m=2*(i-1);
			n=2*(i-1)+2;
			a[n][m]=1;
			a[n][m+1]=-4;
			a[n][m+2]=-16;
			a[n][m+3]=0;
			a[n][m+4]=-15;
			a[n][m+5]=4;
			a[n+1][m]=0;
			a[n+1][m+1]=1;
			a[n+1][m+2]=12;
			a[n+1][m+3]=-6;
			a[n+1][m+4]=-4;
			a[n+1][m+5]=1;
			i++;
		}
		RealMatrix coefficients =
			    new Array2DRowRealMatrix(a,false);
		
		DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
		RealVector constants = new ArrayRealVector(d, false);
		RealVector solution = solver.solve(constants);
		return solution.toArray();
	}
			
			
}

	
	
	
	

