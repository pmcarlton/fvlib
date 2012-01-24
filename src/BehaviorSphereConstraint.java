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

//  pmcarlton added this class to try to constrain points to a given radius from another point.

package volatileprototypes.fvlib;

import processing.core.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author      Peter Carlton <pmcarlton@mac.com>
 * @version     0.1.0                                    
 * @since       0.0          
 */
public final class BehaviorSphereConstraint extends Behavior {

private float bias=.001f, fmult=1.0f;      // Bias limits the maximum force (at zero distance).
private boolean inv=false;				// Option for inverting attract/repel forces.
private PVector cpoint;  // The point to which all points are constrained
private float cradius;                  // The distance from cpoint that points can't exceed

/**
 * Constructor, generates a new class instance.
 *
 */
  public BehaviorSphereConstraint() {
    super();
  }
  
/**
 * Constructor, generates a new class instance using a copy of the supplied Point ArrayList.
 *
 * @param pointsin An ArrayList containing Point objects with which the object's list will be initialized.
 *
 */
  public BehaviorSphereConstraint(ArrayList<? extends Point> pointsin, PVector cpointin, float cradiusin) {
    super(pointsin);
    cpoint=cpointin;
    cradius=cradiusin*cradiusin;
  }
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance using the supplied array.
 *
 * @param pointsin An array containing Point objects with which the object's list will be initialized.
 *
 */
  public BehaviorSphereConstraint(Point[] pointsin, PVector cpointin, float cradiusin) {
    super(pointsin);
    cpoint=cpointin;
    cradius=cradiusin*cradiusin;
  }

/**
 * Sets the bias. Bias limits the maximum force (at zero distance).
 *
 * @param biasin A float representing the new bias value.
 *
 * @return The current object.
 */
  public BehaviorSphereConstraint setBias(float biasin) {
  	bias=biasin;
  	return(this);
  }

  public BehaviorSphereConstraint setRadius(float cradiusin) {
    cradius=cradiusin*cradiusin;
    return(this);
  }

  public BehaviorSphereConstraint setCPoint(PVector cpointin) {
    cpoint=cpointin;
    return(this);
  }

/**
 * Sets the force multiplier.
 *
 * @param fmultin A float representing the new force multiplier.
 *
 * @return The current object.
 */
  public BehaviorSphereConstraint setFMult(float fmultin) {
	fmult = fmultin;
	return(this);
  }

/**
 * Get the bias value.
 *
 * @return The current bias value.
 */
  public float getBias() {
  	return(bias);
  }

/**
 * Get the current force multiplier value.
 *
 * @return The current force multiplier value.
 */
  public float getFMult() {
	return(fmult);
  }
  
  @Override
  protected final void stepFunction(int step, int offset) {
    stepPoints(points, step, offset);
  }
  
  private final void stepPoints(Point[] points, int step, int offset) {
    float d2;
    Point p1;
    float x1,y1,z1;
    float dx,dy,dz,lx,ly,lz;
    for (int i=offset,j=points.length;i<j;i+=step) {
      p1=points[i];
      x1=p1.x;
      y1=p1.y;
      z1=p1.z;
        dx=cpoint.x-x1;
        dy=cpoint.y-y1;
        dz=cpoint.z-z1;
        d2=dx*dx+dy*dy+dz*dz;
        if (d2>cradius) {
          d2=(d2*fmult)/(d2+bias);
          lx=dx*d2;
          ly=dy*d2;
          lz=dz*d2;
          p1.sforce.x+=lx;
          p1.sforce.y+=ly;
          p1.sforce.z+=lz;
        }
      }
    
  }
}
