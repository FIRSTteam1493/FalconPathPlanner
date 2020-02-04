package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
	class Profile {
	static Actions actions = new Actions();
	final double EPS = 0.000001;
	double[] pathFraction = new double[100];
	double[] pathLength = new double[100];
	int numwpts,numpaths, pointsInProfile=0,pointsInDisplayProfile=0,numSpeedPoints=0;
	double timestart=0;
	double totalLength=0.0;
	double angleOffset=0.0;
	double vi=0;
	static List<Double> px = new ArrayList<Double>();
	static List<Double> py = new ArrayList<Double>();
	static List<Double> pxL = new ArrayList<Double>();
	static List<Double> pyL = new ArrayList<Double>();
	static List<Double> pxR = new ArrayList<Double>();
	static List<Double> pyR = new ArrayList<Double>();
	List<Integer> pdir = new ArrayList<Integer>();
	static List<Double> angle1 = new ArrayList<Double>();
	static List<Double> dist1L = new ArrayList<Double>();
	static List<Double> dist1R = new ArrayList<Double>();
	static List<Double> dist1 = new ArrayList<Double>();
	static List<Double> pos1 = new ArrayList<Double>();
	static List<Double> pos1L = new ArrayList<Double>();
	static List<Double> pos1R = new ArrayList<Double>();
	
	static List<Double> time = new ArrayList<Double>();
	static List<Double> pos2 = new ArrayList<Double>();
	static List<Double> pos2L = new ArrayList<Double>();
	static List<Double> pos2R = new ArrayList<Double>();
	static List<Double> dist2L = new ArrayList<Double>();
	static List<Double> dist2R = new ArrayList<Double>();
	static List<Double> dist2 = new ArrayList<Double>();
	static List<Double> vel = new ArrayList<Double>();
	static List<Double> acc = new ArrayList<Double>();
	static List<Double> jerk = new ArrayList<Double>();
	static List<Double> angle2 = new ArrayList<Double>();
	static List<Double> velL = new ArrayList<Double>();
	static List<Double> velR = new ArrayList<Double>();
	static List<Double> accL = new ArrayList<Double>();
	static List<Double> accR = new ArrayList<Double>();

	
	List<Double> px_display= new ArrayList<Double>();
	List<Double> py_display= new ArrayList<Double>();
	List<Double> pxL_display= new ArrayList<Double>();
	List<Double> pyL_display= new ArrayList<Double>();
	List<Double> pxR_display= new ArrayList<Double>();
	List<Double> pyR_display= new ArrayList<Double>();
	List<Double> pdist_display= new ArrayList<Double>();
	List<Double> pangle_display= new ArrayList<Double>();

	
// used for autoscaling the VDA graph	
	static List<Double> maxvalues = new ArrayList<Double>();
	static List<Double> minvalues = new ArrayList<Double>();
	
	Profile(){
	}
	
	public void calculatePath() {
		clearProfile();
		numwpts=Main.waypoints.size();
		if(Main.waypoints.get(numwpts-1).data[8]==1)
			Main.waypoints.get(numwpts-1).data[8]=0;
		numpaths=numwpts-1;
		int i=0;
		int istart=0,iend=0;
		
		while(i<numwpts) {
			if(Main.waypoints.get(i).data[8]!=1) {
				iend=i;
				if (iend-istart>1) getCoeffs(istart,iend);
				istart=i;
			}
			i++;
		}
		calculatePath_(0,numwpts-1);	
	}
	
	
// Do the calculations to get the x-y-angle data along the path in the x-y plane	
	private void calculatePath_(int istart,int iend){
		int inumpaths=iend-istart;
		double x0,y0,x1,y1,xc0,yc0,xc1,yc1,xc2=0,yc2=0,xc3=0,yc3=0;
		double x,y,xp=0,yp=0,dx,dy,dt,t=0,angle,_angle;
		double xl,yl,xr,yr,xlp=0,ylp=0,xrp=0,yrp=0;
		double distl=0,distr=0,dist=0;
		double pos=0,posl=0,posr=0;
		double pathlength=0;
		double ds=0.005;
		boolean swap=false;
		int i=0, j,dir;
		int numintervals= 20000;
		int fit =(int) Main.waypoints.get(istart).data[10];
//		dt=1./ (double)numintervals;
		dt=0.0001;

		
		_angle= (Utils.angle(Main.waypoints.get(istart).data[15]-Main.waypoints.get(istart).data[0],
				Main.waypoints.get(istart).data[16]-Main.waypoints.get(istart).data[1]))*Math.PI/180.;
		
		if(_angle>Math.PI/2 && _angle<=Math.PI) {
			_angle=_angle-Math.PI;
			angleOffset=angleOffset+Math.PI;
		}
		if(_angle<-Math.PI/2 && _angle>=-Math.PI) {
			_angle=_angle+Math.PI;
			angleOffset=angleOffset-Math.PI;
		}
		System.out.println("_angle = "+_angle+"    offset = "+angleOffset);
		j=istart;
		while(j<iend) {
			x0=Main.waypoints.get(j).data[0];
			y0=Main.waypoints.get(j).data[1];
			xc0=Main.waypoints.get(j).data[15];  // exit control coord from point 0
			yc0=Main.waypoints.get(j).data[16];  // exit control coord from point 0
			x1=Main.waypoints.get(j+1).data[0];
			y1=Main.waypoints.get(j+1).data[1];			
			xc1=Main.waypoints.get(j+1).data[13];  // entry control coord from point 1
			yc1=Main.waypoints.get(j+1).data[14];  // entry control coord from point 1		
			if(fit==5) {
				xc2=Main.waypoints.get(j).data[19];  // exit secondary control from point 0
				yc2=Main.waypoints.get(j).data[20];  // exit control coord from point 0
				xc3=Main.waypoints.get(j+1).data[17];  // entry secondary control from point 1
				yc3=Main.waypoints.get(j+1).data[18];  // entry control coord from point 1
			}
// use swap entry/exit of point 0 if Vm < 0 for current point 1 or previous point 1			
			if (j>0) swap = Main.waypoints.get(j).data[6]*Main.waypoints.get(j-1).data[6]<0;					
			if (swap) {
				xc0=Main.waypoints.get(j).data[13];
				yc0=Main.waypoints.get(j).data[14];
				if (fit==5) {
					xc2=Main.waypoints.get(j).data[17];
					yc2=Main.waypoints.get(j).data[18];
				}
				
			}		

			if(Main.waypoints.get(j).data[6]<0) dir=-1; else dir=1;
			if(j==0) {
			xp=x0;
			yp=y0;}
			i=0;t=0;			
			if(j==inumpaths-1)numintervals ++;   //force loop to calculate last endpoint		
			

			while (t<1.0) {
				if(fit==3) {
				x=(1-t)*(1-t)*(1-t)*x0 + 3*t*(1-t)*(1-t)*xc0 + 3*t*t*(1-t)*xc1 + t*t*t*x1;
				y=(1-t)*(1-t)*(1-t)*y0 + 3*t*(1-t)*(1-t)*yc0 + 3*t*t*(1-t)*yc1 + t*t*t*y1;
				} 
				else {
					double omt=1-t;
					double t2=t*t,t3=t2*t,t4=t3*t,t5=t4*t;
					x=x0*Math.pow(omt,5) + 5*xc0*t*Math.pow(omt,4) + 10*xc2*t2*Math.pow(omt,3) +
							10*xc3*t3*omt*omt + 5*xc1*t4*omt + x1*t5;	
					y=Math.pow(omt,5)*y0 + 5*t*Math.pow(omt,4)*yc0  +10* t2*Math.pow(omt,3)*yc2+
							10*t3*omt*omt*yc3 +5*t4*omt*yc1 + t5*y1;	
				}
								 
				dx=x-xp;
				dy=y-yp;
				pathlength=pathlength+Math.sqrt(dx*dx+dy*dy);				
				
				angle = Math.atan(dy/(dx+EPS));
				if(t==0)angle=_angle;
				
				if (i<4)  System.out.println("t = "+t+"   _angle = "+_angle+"   angle = "+angle+
						"    offset = "+angleOffset);

//  If the angle wrapped around +/- 90 degrees, increment the angle offset				
				if (t!=0) {
				if(Math.tan(_angle)>1 && Math.tan(angle)<-1) angleOffset=angleOffset+Math.PI;		
				if(Math.tan(_angle)<-1 && Math.tan(angle)>1) angleOffset=angleOffset-Math.PI;								
				}
// if this is the first first point in the path of the first segment and we had done a rotate, 
// check if the rotate wrapped around +/- 90 and increment the angle offset				
				else {
					angle=_angle;
					if (j>0) {
					if(Main.waypoints.get(j).data[9]!=0) 
							angleOffset=angleOffset+(Math.PI)* (int)((Main.waypoints.get(j).data[9]-EPS)/90)  ;}
				}
				
				angle1.add( (angle+angleOffset)*180/Math.PI);
				


				xl=x+(Constants.wheelBase/2.)*Math.cos(angle+angleOffset+Math.PI/2);
				yl=y+(Constants.wheelBase/2.)*Math.sin(angle+angleOffset+Math.PI/2);
				
				xr=x+(Constants.wheelBase/2.)*Math.cos(angle+angleOffset-Math.PI/2);
				yr=y+(Constants.wheelBase/2.)*Math.sin(angle+angleOffset-Math.PI/2);

				
				dist=dist+Math.sqrt(dx*dx+dy*dy);
				pos=pos+(double)dir*Math.sqrt(dx*dx+dy*dy);
				double dangle=0;
				if(t!=0) dangle=angle1.get(angle1.size()-1) - angle1.get(angle1.size()-2);
				dangle=dangle*Math.PI/180;
				distl=distl+  Math.abs(Math.sqrt(dx*dx+dy*dy)-dangle*Constants.wheelBase/2.);
				distr=distr+ Math.abs(Math.sqrt(dx*dx+dy*dy)+dangle*Constants.wheelBase/2.);
				posl=posl+ (double)dir*(Math.sqrt(dx*dx+dy*dy)-dangle*Constants.wheelBase/2.);
				posr=posr+(double)dir*(Math.sqrt(dx*dx+dy*dy)+(dangle)*Constants.wheelBase/2.);
				py.add(y);
				px.add(x);
				
				pxL.add(xl);
				pyL.add(yl);
				
				pxR.add(xr);
				pyR.add(yr);
				
				xp=x;yp=y;
				xlp=xl;ylp=yl;
				xrp=xr;yrp=yr;
				_angle=angle;					
				
				dist1.add(dist);			
				dist1L.add(distl);			
				dist1R.add(distr);			
				pos1.add(pos);			
				pos1L.add(posl);			
				pos1R.add(posr);			

				pointsInProfile++;			
				if (i%100==0){
					px_display.add(x);
					py_display.add(y);
					pxL_display.add(xl);
					pyL_display.add(yl);
					pxR_display.add(xr);
					pyR_display.add(yr);
					pdist_display.add(dist);
					pdir.add(dir);
					pointsInDisplayProfile ++;
				}		
				dt=ds/Math.sqrt( (dx/dt)*(dx/dt)+(dy/dt)*(dy/dt));
				if(t==0) dt=0.0001;
				t=t+dt;
				i++;
			}
			pathLength[j]=pathlength;
			pathlength=0;
			totalLength=dist;
			j++;
		}
//		System.out.println("Number points in path = "+dist1.size());
	}
	
	public void calculateVDA() {
		double jmax,amax,vmax;
		double vf=0.0;
		double motionlength=0, distance=0;
		int k=1;
		numSpeedPoints=0;
		time.clear();pos2.clear();vel.clear();jerk.clear();acc.clear();dist2.clear();angle2.clear();
		pos2L.clear();velL.clear();accL.clear();dist2L.clear();
		pos2R.clear();velR.clear();accR.clear();dist2R.clear();
		jmax=Main.waypoints.get(0).data[4];
		amax=Main.waypoints.get(0).data[5];
		vmax=Main.waypoints.get(0).data[6];
		vf=Main.waypoints.get(0).data[7];
		vi=0;
		while (k<Main.waypoints.size()){
			motionlength=motionlength+pathLength[k-1];
			if (!(Main.waypoints.get(k).data[8]==1.0) ){    // type			
				calculateVDAprofile(jmax,amax,vmax,vi,vf,motionlength);
				distance=distance+motionlength;
				vi=vf;
				jmax=Main.waypoints.get(k).data[4];
				amax=Main.waypoints.get(k).data[5];
				vmax=Main.waypoints.get(k).data[6];
				vf=Main.waypoints.get(k).data[7];
				motionlength=0;
//				mergeAngle();
			}

			if(Main.waypoints.get(k).data[8]==3.0) {
				double targetAngle=Main.waypoints.get(k).data[9];
				calculateRotateInPlace(targetAngle);
				angleOffset=angleOffset+targetAngle*Math.PI/180.;
			}
			if(Main.waypoints.get(k).data[8]==2.0) {
				double pauseTime=Main.waypoints.get(k).data[9];
				calculatePause(pauseTime);
			}
		k++;
		}			
		getMaxValues();
	}
	
	
	public void calculateVDAprofile(double jmax, double amax, 
			double vmax, double vi, double vf, double d){
		int i=0,index=0;
		double t1,t2,t3,t5,t6,t7;
		double dt=0.0001,totalTime=0;
		double _jerk,_acc,_vel,_dist,_time,angle,_dist2L,_dist2R,_pos,_posl,_posr;
		double dpos2L,dpos2R,dpos2,vl,vr,al,ar;
		double timePrevious=0,posPrevious=0,distPrevious=0,temp,poslPrevious,posrPrevious;
		long startTime = System.nanoTime();
		if (time.size()>0) {
			timePrevious=time.get(time.size()-1);
			posPrevious=pos2.get(pos2.size()-1);
			posrPrevious=pos2R.get(pos2R.size()-1);
			poslPrevious=pos2L.get(pos2L.size()-1);
			distPrevious=dist2.get(dist2.size()-1);
		}
		//vi=0;//vf=0;
//  limit amax to the greatest acceleration achievable given vmax and jmax    
//  i.e., v and j are fixed, tweak a if there are issues     	    	
    	amax= Math.min(Math.sqrt((Math.abs(vmax-vi))*jmax),amax);

// Get the starting sign of acc and jerk correct - start negative if vmax-vi is negative    	
    	amax=Math.abs(amax)*Math.signum(vmax-vi);		
		jmax=Math.abs(jmax)*Math.signum(vmax-vi);
    	
		t1=amax/jmax; 
		if (vmax==vi) {t1=0;}
		double a1=amax;	
		double v1=vi + jmax*t1*t1/2; double s1=vi*t1+ jmax*t1*t1*t1/6;
		
		t3=t1; double a3=0;
		double v3=vmax;
		double a2=amax;
		double v2=v3-a2*t3+jmax*t3*t3/2;
		t2=(v2-v1)/a2;
		double s2=s1+v1*t2+a1*t2*t2/2;
		double s3=s2+v2*t3+a2*t3*t3/2-jmax*t3*t3*t3/6;
		
		double a4=0;double v4=vmax;
		double a5=-amax;
		if (vmax==vf) {t5=0;}else t5=(a5-a4)/(-jmax);  
		double v5=v4-jmax*t5*t5/2;
		double ds5=v4*t5-jmax*t5*t5*t5/6;
		double a6=-amax;
		double a7=0;
		double v7=vf;
		if (vmax==vf) {t7=0;}else t7=amax/jmax;  
		double v6=v7-a6*t7-jmax*t7*t7/2;
		if (amax==0) {t6=0;} else t6=(v6-v5)/(-amax);		
		double ds6=v5*t6+a5*t6*t6/2;
		double ds7 = v6*t7+a6*t7*t7/2+jmax*t7*t7*t7/6;
		double ds4=Math.signum(vmax)*(d-Math.abs(s3+ds5+ds6+ds7));
		double s4=ds4+s3;
		double t4=ds4/v4;
		double s5=s4+ds5;
		double s6=s5+ds6;
		double s7=s6+ds7;
		

		double s=0;
		if (t7>100.) t7=100.;
		double t=0;double ts=0;
		totalTime=t1+t2+t3+t4+t5+t6+t7;
		while (t<totalTime) {
			_time=(t+timePrevious);
			if(t<=t1) {
				ts=t;
				_pos=(posPrevious+vi*t+jmax*ts*ts*ts/6);
				_dist=(distPrevious+Math.abs(vi*t+jmax*ts*ts*ts/6));
				_vel=(vi+jmax*ts*ts/2);
				_acc=(jmax*ts);
				_jerk=(jmax);
			}			
			else if(t<=t1+t2) {
				ts=t-t1;
				_pos=(posPrevious+s1+v1*ts+amax*ts*ts/2);
				_dist=(distPrevious+Math.abs(s1)+Math.abs(v1*ts+amax*ts*ts/2));
				_vel=(v1+amax*ts);
				_acc=(amax);
				_jerk=(0.);				
			}
			else if(t<=t1+t2+t3) {
				ts=t-t1-t2;
				_pos=(posPrevious+s2+v2*ts+amax*ts*ts/2-jmax*ts*ts*ts/6);
				_dist=(distPrevious+Math.abs(s2)+Math.abs(v2*ts+amax*ts*ts/2-jmax*ts*ts*ts/6));
				_vel=(v2+amax*ts-jmax*ts*ts/2);
				_acc=(amax-jmax*ts);
				_jerk=(-jmax);				
			}
			else if(t<=t1+t2+t3+t4) {
				ts=t-t1-t2-t3;
				_pos=(posPrevious+s3+vmax*ts);
				_dist=(distPrevious+Math.abs(s3)+Math.abs(vmax*ts));
				_vel=(vmax);
				_acc=(0.);
				_jerk=(0.);				
			}
			else if(t<=t1+t2+t3+t4+t5) {
				ts=t-t1-t2-t3-t4;
				_pos=(posPrevious+s4+v4*ts-jmax*ts*ts*ts/6);
				_dist=(distPrevious+Math.abs(s4)+Math.abs(v4*ts-jmax*ts*ts*ts/6));
				_vel=(v4-jmax*ts*ts/2);
				_acc=(-jmax*ts);
				_jerk=(-jmax);				
			}
			else if(t<=t1+t2+t3+t4+t5+t6) {
				ts=t-t1-t2-t3-t4-t5;
				_pos=(posPrevious+s5+v5*ts-amax*ts*ts/2);
				_dist=(distPrevious+Math.abs(s5)+Math.abs(v5*ts-amax*ts*ts/2));
				_vel=(v5-amax*ts);
				_acc=(-amax);
				_jerk=(0.);				
			}
			else  {
				ts=t-t1-t2-t3-t4-t5-t6;
				_pos=(posPrevious+s6+v6*ts-amax*ts*ts/2+jmax*ts*ts*ts/6);
				_dist=(distPrevious+Math.abs(s6)+Math.abs(v6*ts-amax*ts*ts/2+jmax*ts*ts*ts/6));
				_vel=(v6-amax*ts+jmax*ts*ts/2);
				_acc=(-amax+jmax*ts);
				_jerk=(jmax);				
			}
			i++;
			t=t+dt;

			if(i%100==0) {
				numSpeedPoints++;
				jerk.add(_jerk);
				acc.add(_acc);
				vel.add(_vel);
				dist2.add(_dist);   //total distance
				pos2.add(_pos);	  //encoder distance
				time.add(_time);
				actions.addToActionProfile(t);

				
	// interpolate on the distance distance-angle pairs to find the matching angle, left wheel position
    // and right wheel position for the current distance. Then scale the velocity to get velL and velR
				index = Arrays.binarySearch(dist1.toArray(),index,dist1.size(),(_dist));
				if(index<0) index=-index;
				if(index>1)index=index-2;else index=0;
 
				if (index<=dist1.size()-2) {
					temp=(_dist-dist1.get(index))/(dist1.get(index+1) - dist1.get(index));
					angle = angle1.get(index)+( angle1.get(index+1) -angle1.get(index) )*temp;

					_dist2L = dist1L.get(index)+( dist1L.get(index+1) -dist1L.get(index) )*temp;
				
					_dist2R = dist1R.get(index)+( dist1R.get(index+1) -dist1R.get(index) )*temp;
				
					_posl = pos1L.get(index)+( pos1L.get(index+1) -pos1L.get(index) )*temp;
				
					_posr = pos1R.get(index)+( pos1R.get(index+1) -pos1R.get(index) )*temp;
				
				}
				else {
					angle=angle1.get(dist1.size()-1);
					_dist2L=dist1L.get(dist1.size()-1);
					_dist2R=dist1R.get(dist1.size()-1);
					_posl=pos1L.get(dist1.size()-1);
					_posr=pos1R.get(dist1.size()-1);
				}
				if(angle>999 || angle<-999)angle=angle2.get(angle2.size()-1);	
				if(_dist2L>999 || _dist2L<-999)angle=dist1L.get(dist1L.size()-1);	
				if(_dist2R>999 || _dist2R<-999)angle=dist1R.get(dist1R.size()-1);	
				if(_posr>999 || _posr<-999)angle=pos1R.get(pos1R.size()-1);	
				if(_posl>999 || _posl<-999)angle=pos1L.get(pos1L.size()-1);	


				angle2.add(angle);	
				dist2L.add(_dist2L);	
				dist2R.add(_dist2R);	
				pos2L.add(_posl);	
				pos2R.add(_posr);					

				if(dist2.size()>=2) {
					dpos2L=pos2L.get(pos2L.size()-1)-pos2L.get(pos2L.size()-2);
					dpos2R=pos2R.get(pos2R.size()-1)-pos2R.get(pos2R.size()-2);
					vl=dpos2L/(time.get(time.size()-1)-time.get(time.size()-2));
					vr=dpos2R/(time.get(time.size()-1)-time.get(time.size()-2));
					al=vl/(time.get(time.size()-1)-time.get(time.size()-2));
					ar=vr/(time.get(time.size()-1)-time.get(time.size()-2));

				}
				else {
					dpos2L=dist2L.get(dist2L.size()-1);
					dpos2R=dist2R.get(dist2R.size()-1);
					vl=dpos2L/(time.get(time.size()-1));
					vr=dpos2R/(time.get(time.size()-1));
					al=vl/(time.get(time.size()-1));
					ar=vr/(time.get(time.size()-1));
				}
				velL.add(vl);
				velR.add(vr);
				accL.add(al);
				accR.add(ar);								
				
				if (numSpeedPoints>=20000) {
					System.out.println("too many points, need to speed up!");
					break;
				}
			}
			
			
		}
		System.out.println("calc time = "+((System.nanoTime()-startTime)/1000000));
	}	
		
	
	
//**********************************
//*  Rotate in Place			   *
//**********************************	
	public void calculateRotateInPlace(double targetAngle) {
		double angle=0,t=0,posl,posr,distl,distr,dangle,temp;		
		double maxrotateVel=Constants.rotRate*Math.signum(targetAngle);
		double tf=2*targetAngle/maxrotateVel;
		double tfhalf=targetAngle/maxrotateVel;
		int numsteps=(int)(tf/0.01);	
		int i=0;
		int index = time.size()-1;
		double j = jerk.get(index);
		double a = acc.get(index);
		double v = vel.get(index);
		double dist = dist2.get(index)+0.005;
		posl=pos2L.get(pos2L.size()-1);
		posr=pos2R.get(pos2R.size()-1);
		distl=dist2L.get(dist2L.size()-1);
		distr=dist2R.get(dist2R.size()-1);
		
		double tstart = time.get(index);
// target angle is the angle of rotation, but it was already added when we calculated the
// x-y path (needed it for correct slopes of the path). So subtract it back out.
// What we are doing here is adding the angle-time profile that gets us to tartgetAngle		
//		double anglestart = angle2.get(index)-targetAngle;
		double anglestart = angle2.get(index);
		while(i<=numsteps) {
			t=t+0.01;
			jerk.add(j);
			acc.add(a);
			vel.add(v);
//			pos2.add(dist+EPS);  // can't repeat a distance or else angle won't interpolate correctly
//			dist2.add(dist+EPS);			
			pos2.add(dist);
			dist2.add(dist);			
			time.add(tstart+t);
			actions.addToActionProfile(t);
			
			if(t<=tfhalf)angle=maxrotateVel*maxrotateVel*t*t/(2*targetAngle);
			else angle = 0.5*targetAngle+(maxrotateVel*(t-tfhalf) )-
					(maxrotateVel*maxrotateVel/targetAngle)*(t-tfhalf)*(t-tfhalf)/2;			
			
			angle2.add(anglestart+angle);			
			dangle=angle2.get(angle2.size()-1)-angle2.get(angle2.size()-2);
			temp=(double)(Constants.wheelBase/2)*dangle*(Math.PI/180.);
			posl=posl-temp;
			posr=posr+temp;
			distl=distl-Math.abs(temp);
			distr=distr+Math.abs(temp);
			velL.add( ((posl-pos2L.get(pos2L.size()-1))/.01) );
			velR.add( ((posr-pos2R.get(pos2R.size()-1))/.01) );
			pos2L.add(posl);			
			pos2R.add(posr);			
			dist2L.add(distl);
			dist2R.add(distr);
//			System.out.println("dangle = "+dangle+"   posl = "+posl + "  "+velL.get(velL.size()-1));
			numSpeedPoints ++;
//			dist=dist+EPS;
			i++;
		}
	}
	
	public void calculatePause(double pauseTime) {
		int numsteps=(int)(pauseTime/0.01);
		int i=0;
		int index = time.size()-1;
		double dist = dist2.get(index);
		double position = pos2.get(index);
		double t = time.get(index);
		double angle = angle2.get(index);
		double posl=pos2L.get(index);
		double posr = pos2R.get(index);
		double distl = dist2L.get(index);
		double  distr = dist2R.get(index);
		
		while(i<=numsteps) {
			jerk.add(0.0);
			acc.add(0.0);
			vel.add(0.0);
			pos2.add(position);
			dist2.add(dist);
			time.add(t+0.01*i);
			angle2.add(angle);	
			pos2L.add(posl);
			pos2R.add(posr);
			dist2L.add(distl);
			dist2R.add(distr);
			velL.add(0.0);
			velR.add(0.0);
 			actions.addToActionProfile(t);
			i++;
		}
	}

	
	
	private void getMaxValues(){
		maxvalues.clear();minvalues.clear();
		maxvalues.add(Collections.max(pos2));
		maxvalues.add(Collections.max(dist2));
		maxvalues.add(Collections.max(jerk));
		maxvalues.add(Collections.max(acc));
		maxvalues.add(Collections.max(vel));
		maxvalues.add(Collections.max(angle2));
		maxvalues.add(Collections.max(dist2L));
		maxvalues.add(Collections.max(dist2R));
		maxvalues.add(Collections.max(velL));
		maxvalues.add(Collections.max(velR));
		maxvalues.add(Collections.max(pos2L));
		maxvalues.add(Collections.max(pos2R));
		
		minvalues.add(Collections.min(pos2));
		minvalues.add(Collections.min(dist2));
		minvalues.add(Collections.min(jerk));
		minvalues.add(Collections.min(acc));
		minvalues.add(Collections.min(vel));
		minvalues.add(Collections.min(angle2));
		minvalues.add(Collections.min(dist2L));
		minvalues.add(Collections.min(dist2R));
		minvalues.add(Collections.min(velL));
		minvalues.add(Collections.min(velR));
		minvalues.add(Collections.max(pos2L));
		minvalues.add(Collections.max(pos2R));
	}
	
	
	public void clearProfile(){
		px.clear();py.clear();angle1.clear();dist1.clear();pos1.clear();pdir.clear();
		pxL.clear();pyL.clear();dist1L.clear();pos1L.clear();
		pxR.clear();pyR.clear();dist1R.clear();pos1R.clear();

		px_display.clear();py_display.clear();pangle_display.clear();pdist_display.clear();
		pxL_display.clear();pyL_display.clear(); pxR_display.clear();pyR_display.clear();
		
		time.clear();pos2.clear();vel.clear();jerk.clear();acc.clear();dist2.clear();angle2.clear();
		pos2L.clear();velL.clear();accL.clear();dist2L.clear();
		pos2R.clear();velR.clear();accR.clear();dist2R.clear();

		numwpts=0;numpaths=0; pointsInProfile=0;pointsInDisplayProfile=0;
		totalLength=0.0; angleOffset=0.0;

	}


	private void getCoeffs(int istart, int iend) {
		int inumpaths=iend-istart,inumwpts=iend-istart+1;
		double[] a = new double[inumpaths];
		double[] b = new double[inumpaths];
		double[] c = new double[inumpaths];
		double[] p1x = new double[inumpaths];
		double[] p1y = new double[inumpaths];
		double[] p2x = new double[inumpaths];
		double[] p2y = new double[inumpaths];
		double[] p3x = new double[inumpaths];
		double[] p3y = new double[inumpaths];
		double[] p4x = new double[inumpaths];
		double[] p4y = new double[inumpaths];
		
		int i,j,m,n;		

	if(Main.fitType==3) {	
		double six=Main.waypoints.get(istart).data[15];
		double sfx= Main.waypoints.get(iend).data[13];
		double siy=Main.waypoints.get(istart).data[16];
		double sfy= Main.waypoints.get(iend).data[14];

	double[] dx = new double[inumpaths];
	double[] dy = new double[inumpaths];

	a[0]=0;a[inumpaths-1]=1;
	b[0]=1;b[inumpaths-1]=4;
	c[0]=0;c[inumpaths-1]=0;
	dx[0]=six;
	dx[inumpaths-1]=4*Main.waypoints.get(iend-1).data[0]+sfx;
	dy[0]=siy;
	dy[inumpaths-1]=4*Main.waypoints.get(iend-1).data[1]+sfy;
	
	i=1;
	while (i<=inumpaths-2) {
		a[i]=1;
		b[i]=4;
		c[i]=1;
		dx[i]=4*Main.waypoints.get(i+istart).data[0]+2*Main.waypoints.get(i+1+istart).data[0];
		dy[i]=4*Main.waypoints.get(i+istart).data[1]+2*Main.waypoints.get(i+1+istart).data[1];
		i++;
	}
	
	i=0;
	
	p1x=Utils.solveMatrix(a, b, c, dx);
	p1y=Utils.solveMatrix(a, b, c, dy);
	i=0;

	while(i<inumpaths-1) {
		p2x[i]=2*Main.waypoints.get(i+1+istart).data[0]-p1x[i+1];
		p2y[i]=2*Main.waypoints.get(i+1+istart).data[1]-p1y[i+1];
		i++;
	}

	p2x[inumpaths-1]=sfx;
	p2y[inumpaths-1]=sfy ;

	i=0;
	
	while(i<inumpaths){
		Main.waypoints.get(i+istart).data[15]=p1x[i];
		Main.waypoints.get(i+istart).data[16]=p1y[i];
		Main.waypoints.get(i+1+istart).data[13]=p2x[i];
		Main.waypoints.get(i+1+istart).data[14]=p2y[i];
		i++;
	}
	
}
else {
	double six=Main.waypoints.get(istart).data[15];
	double sfx= Main.waypoints.get(iend).data[13];
	double siy=Main.waypoints.get(istart).data[16];
	double sfy= Main.waypoints.get(iend).data[14];
	double cix=Main.waypoints.get(istart).data[19];
	double cfx= Main.waypoints.get(iend).data[17];
	double ciy=Main.waypoints.get(istart).data[20];
	double cfy= Main.waypoints.get(iend).data[18];
	double[] dx = new double[2*inumpaths];
	double[] dy = new double[2*inumpaths];

	dx[0]=six;
	dx[1]=cix;
	dy[0]=siy;
	dy[1]=ciy;
	double affx=Main.waypoints.get(iend).data[0];
	double afx=Main.waypoints.get(iend-1).data[0];
	double affy=Main.waypoints.get(iend).data[1];
	double afy=Main.waypoints.get(iend-1).data[1];
	
	dx[2*inumpaths-2]= -16*afx-14*affx+15*(2*affx-sfx)-4*(cfx+4*affx-4*sfx)  ;
	dx[2*inumpaths-1]= 8*afx-4*affx+4*(2*affx-sfx)-(cfx+4*affx-4*sfx);
	dy[2*inumpaths-2]= -16*afy-14*affy+15*(2*affy-sfy) -4*(cfy+4*affy-4*sfy) ;
	dy[2*inumpaths-1]= 8*afy-4*affy+4*(2*affy-sfy)-(cfy+4*affy-4*sfy);
	
	i=1;
	while(i<inumpaths-1) {
		n=2*(i-1)+2;
		dx[n]=-16*Main.waypoints.get(i+istart).data[0]-14*Main.waypoints.get(i+1+istart).data[0];
		dx[n+1]=8*Main.waypoints.get(i+istart).data[0]-4*Main.waypoints.get(i+1+istart).data[0];
		dy[n]=-16*Main.waypoints.get(i+istart).data[1]-14*Main.waypoints.get(i+1+istart).data[1];
		dy[n+1]=8*Main.waypoints.get(i+istart).data[1]-4*Main.waypoints.get(i+1+istart).data[1];
		i++;
	}				
	double[] resultx=Utils.solveLinSystem(inumpaths, dx);
	double[] resulty=Utils.solveLinSystem(inumpaths, dy);

	p1x[0]=Main.waypoints.get(istart).data[15];
	p2x[0]=Main.waypoints.get(istart).data[19];
	p1y[0]=Main.waypoints.get(istart).data[16];
	p2y[0]=Main.waypoints.get(istart).data[20];

	i=1;
	while(i<inumpaths) {
		n=2*(i-1)+2;
		p1x[i]=resultx[n];
		p2x[i]=resultx[n+1];
		p1y[i]=resulty[n];
		p2y[i]=resulty[n+1];
		i++;
	}
	p3x[0]=4*Main.waypoints.get(istart+1).data[0]-4*p1x[1]+p2x[1];
	p4x[0]=2*Main.waypoints.get(istart+1).data[0]-p1x[1];
	p3y[0]=4*Main.waypoints.get(istart+1).data[1]-4*p1y[1]+p2y[1];
	p4y[0]=2*Main.waypoints.get(istart+1).data[1]-p1y[1];
	
	i=1;
	while(i<inumpaths-1) {
    	p3x[i]=4*Main.waypoints.get(i+1+istart).data[0]-4*p1x[i+1]+p2x[i+1];
    	p4x[i]=2*Main.waypoints.get(i+1+istart).data[0]-p1x[i+1];
    	p3y[i]=4*Main.waypoints.get(i+1+istart).data[1]-4*p1y[i+1]+p2y[i+1];
    	p4y[i]=2*Main.waypoints.get(i+1+istart).data[1]-p1y[i+1];
    	i++;
	}
	p3x[inumpaths-1]=cfx;
	p4x[inumpaths-1]=sfx;
	p3y[inumpaths-1]=cfy;
	p4y[inumpaths-1]=sfy;
	
i=1;

while(i<inumwpts-1) {
	Main.waypoints.get(i+istart).data[17]=p3x[i-1];
	Main.waypoints.get(i+istart).data[13]=p4x[i-1];
	Main.waypoints.get(i+istart).data[15]=p1x[i];
	Main.waypoints.get(i+istart).data[19]=p2x[i];
	Main.waypoints.get(i+istart).data[18]=p3y[i-1];
	Main.waypoints.get(i+istart).data[14]=p4y[i-1];
	Main.waypoints.get(i+istart).data[16]=p1y[i];
	Main.waypoints.get(i+istart).data[20]=p2y[i];
	i++;
}
	
}	
	}
	
	
	
}	
