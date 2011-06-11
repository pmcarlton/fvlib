//  Copyright (c) 2010-2011, Ioannis (Yiannis) Chatzikonstantinou, ?All rights reserved.
//  http://www.volatileprototypes.com
// 
//  Redistribution and use in source and binary forms, with or without modification, 
//  are permitted provided that the following conditions are met:
//  	- Redistributions of source code must retain the above copyright 
//  notice, this list of conditions and the following disclaimer.
//  	- Redistributions in binary form must reproduce the above copyright 
//  notice, this list of conditions and the following disclaimer in the documentation 
//  and/or other materials provided with the distribution.
//  
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
//  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
//  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
//  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
//  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
//  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
//  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
//  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
//  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
//  OF SUCH DAMAGE.

// This library uses portions of code and examples from Thomas Jakobsen's
// paper "Advanced Character Physics".

package volatileprototypes.fvlib;

import processing.core.*;
import java.util.*;
import java.util.concurrent.*;

public final class ConstantDistance extends Solver {

private Point[] points;			// array that holds points
private boolean fast=false;		// Option for fast/accurate spring solver.
private float C = 1.00f;
private float S = 0.25f;
private float R = 1.00f, R2 = R*R;

// Constructor. Creates empty arrays.
  public ConstantDistance() {
    points=new Point[0];
  }
  
  // Constructor. Uses ArrayLists and generics.
  public ConstantDistance(ArrayList<? extends Point> pointsin) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
  }
  
  // Constructor. Uses arrays.
  public ConstantDistance(Point[] pointsin) {
    points=pointsin;
  }
  
  // Various getter-setter functions.
  
  public ConstantDistance setP(ArrayList<? extends Point> pointsin) {
    points=new Point[pointsin.size()];
  	pointsin.toArray(points);
  	return(this);
  }
  
  public ConstantDistance setP(Point[] pin) {
  	points=pin;
  	return(this);
  }
  
  public ConstantDistance setC(float cin) {
	C = cin;
	return(this);
  }
  
  public ConstantDistance setS(float sin) {
	S = sin;
	return(this);
  }
  
  public ConstantDistance setR(float rin) {
	R = rin;
	R2 = R * R;
	return(this);
  }
  
  public Point[] getP() {
  	return(points);
  }
  
  public float getC() {
	return(C);
  }
  
  public float getS() {
	return(S);
  }
  
  public float getR() {
	return(R);
  }
  
  public final ConstantDistance setFast(boolean fin) {
  	fast=fin;
  	return(this);
  }
  
  @Override
  protected final void stepFunction(int step, int offset) {
    if (fast) {fStepDist(points, step, offset);} else {StepDist(points, step, offset);}
  }
  
  private final void fStepDist(Point[] points, int step, int offset) {
    float d;
    float L2;
    Point p1,p2;
    float dx,dy,dz,lx,ly,lz;
    for (int i=offset,k=points.length;i<k;i+=step) {
      p1=points[i];
	  for (int j=i+step;j<k;j+=step) {
		p2=points[j];
		dx=p2.x-p1.x;
		dy=p2.y-p1.y;
		dz=p2.z-p1.z;
		L2 = dx*dx+dy*dy+dz*dz;
		if (L2<R2) {
		  d = C + L2/C; 
		  d = (float)(d*.25 + L2/d);
		  d = (float)(S*(1-(C/d)));
		  lx=d*dx;
		  ly=d*dy;
		  lz=d*dz;
		  p1.sforce.x+=lx;
		  p1.sforce.y+=ly;
		  p1.sforce.z+=lz;
		  p2.sforce.x-=lx;
		  p2.sforce.y-=ly;
		  p2.sforce.z-=lz;
		}
	  }
    }
  }
  
  // Faster variant with one Newton iteration.
  private final void StepDist(Point[] points, int step, int offset) {
    float d;
    float L2,C2;
    Point p1,p2;
    float dx,dy,dz,lx,ly,lz;
    for (int i=offset,k=points.length;i<k;i+=step) {
      p1=points[i];
	  for (int j=i+step;j<k;j+=step) {
		p2=points[j];
		dx=p2.x-p1.x;
		dy=p2.y-p1.y;
		dz=p2.z-p1.z;
		L2 = dx*dx+dy*dy+dz*dz;
		C2 = C*C; 
		if (L2<R2) {
		  d = (float)(S*(C2/(L2+C2)-.5f));
		  lx=d*dx;
		  ly=d*dy;
		  lz=d*dz;
		  p1.sforce.x-=lx;
		  p1.sforce.y-=ly;
		  p1.sforce.z-=lz;
		  p2.sforce.x+=lx;
		  p2.sforce.y+=ly;
		  p2.sforce.z+=lz;
		}
	  }
	}
  }
}