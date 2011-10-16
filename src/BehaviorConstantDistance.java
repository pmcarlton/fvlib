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

/**
 * @author      Yiannis Chatzikonstantinou <contact@volatileprototypes.com>
 * @version     0.5.9                                    
 * @since       0.4          
 */
public final class BehaviorConstantDistance extends Behavior {

private boolean fast=false;		// Option for fast/accurate spring solver.
private float C = 1.00f;
private float S = 0.25f;
private float R = 1.00f, R2 = R*R;
  
/**
 * Constructor, generates a new class instance.
 *
 */
  public BehaviorConstantDistance() {
    super();
  }
  
/**
 * Constructor, generates a new class instance using a copy of the supplied Point ArrayList.
 *
 * @param pointsin An ArrayList containing Point objects with which the object's list will be initialized.
 *
 */
  public BehaviorConstantDistance(ArrayList<? extends Point> pointsin) {
    super(pointsin);
  }
  
/**
 * Constructor, generates a new class instance using the supplied Point array.
 *
 * @param pointsin An array containing Point objects with which the object's array will be initialized.
 *
 */
  public BehaviorConstantDistance(Point[] pointsin) {
    super(pointsin);
  }

/**
 * Sets the rest length, the length at which points exert no force between each other.
 *
 * @param cin A float representing the new rest distance value.
 *
 * @return The current object.
 */
  public BehaviorConstantDistance setC(float cin) {
	C = cin;
	return(this);
  }

/**
 * Sets the stiffness of the virtual spring used to keep object distance.
 *
 * The stiffness of the virtual springs used to keep the object distance in this
 * behavior is an float value between 0.0 and 0.5, with 0.5 being the stiffest.
 * Be aware that high values may easily cause instability of the physics system.
 *
 * @param sin A float representing the new stiffness value.
 *
 * @return The current object.
 */
  public BehaviorConstantDistance setS(float sin) {
	S = sin;
	return(this);
  }
  
/**
 * Sets the maximum range below which objects will interact.
 *
 * @param rin A float representing the new range.
 *
 * @return The current object.
 */
  public BehaviorConstantDistance setRange(float rin) {
	R = rin;
	R2 = R * R;
	return(this);
  }

/**
 * Returns the rest length of the virtual springs.
 *
 * @return The rest length value.
 */
  public float getC() {
	return(C);
  }

/**
 * Returns the stiffness of the virtual springs.
 *
 * @return The stiffness value.
 */
  public float getS() {
	return(S);
  }

/**
 * Returns the range value.
 *
 * @return The range value.
 */
  public float getRange() {
	return(R);
  }

/**
 * Sets whether to use a faster solver or the normal one.
 *
 * In fvlib, a fast solver is implemented for spring interactions that uses just one Newton iteration to approximate
 * the square root of the squared length between points using the rest length as reference. The faster solver reduces precision but increases
 * simulation speed noticeably.
 *
 * @param fin A boolean representing whether to use fast sqrt.
 *
 * @return The current object.
 */
  public final BehaviorConstantDistance setFast(boolean fin) {
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