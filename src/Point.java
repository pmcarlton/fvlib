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

public class Point extends PVector {

  protected float w;							// Weight
  protected float w1;							// 1/Weight. Cached for speedup.
  protected float c=0;							// Charge. Used for electrostatic calculations.
  protected float r=0,r2=0;						// Range, also for electrostatic calculations.
  public PVector sforce;						// force accumulator.
  protected PVector old;						// Position in previous timestep.
  protected PVector uMult=new PVector(0,0,0);	// Multiplier for Unyielding constraints.
  protected boolean U=false;					// Unyielding flag.
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance using coordinates.
 *
 * @param xin X-Position of the Point.
 * @param yin Y-Position of the Point.
 * @param zin Z-Position of the Point.
 *
 */
  public Point(float xin, float yin, float zin) {
    super(xin,yin,zin);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=w1=1f;
  }
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance using coordinates and weight.
 * Weight is not yet implemented for use during the simulation.
 *
 * @param xin X-Position of the Point.
 * @param yin Y-Position of the Point.
 * @param zin Z-Position of the Point.
 * @param win Weight of the Point.
 *
 */
  public Point(float xin, float yin, float zin, float win) {
    super(xin,yin,zin);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=win;
    w1=1f/win;
  }
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance using coordinates, weight, charge and range.
 * Weight is not yet implemented for use during the simulation.
 * Charge is used in electric Charge simulations (BehaviorCharge / BehaviorChargePair).
 * Range is used to check for point-to-point interactions.
 *
 * @param xin X-Position of the Point.
 * @param yin Y-Position of the Point.
 * @param zin Z-Position of the Point.
 * @param win Weight of the Point.
 * @param cin Charge of the Point.
 * @param rin Range of the Point.
 *
 */
  public Point(float xin, float yin, float zin, float win, float cin, float rin) {
    super(xin,yin,zin);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=win;
    w1=1f/win;
    c=cin;
    r=rin;
    r2=r*r;
  }
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance using a PVector.
 *
 * @param v The PVector with the coordinates of the Point.
 *
 */
  public Point(PVector v) {
    super(v.x,v.y,v.z);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=w1=1f;
  }
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance a PVector and weight.
 * Weight is not yet implemented for use during the simulation.
 *
 * @param v The PVector with the coordinates of the Point.
 * @param win Weight of the Point.
 *
 */
  public Point(PVector v, float win) {
    super(v.x,v.y,v.z);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=win;
    w1=1f/win;
  }
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance using a PVector, weight, charge and range.
 * Weight is not yet implemented for use during the simulation.
 * Charge is used in electric Charge simulations (BehaviorCharge / BehaviorChargePair).
 * Range is used to check for point-to-point interactions.
 *
 * @param v The PVector with the coordinates of the Point.
 * @param win Weight of the Point.
 * @param cin Charge of the Point.
 * @param rin Range of the Point.
 *
 */
  public Point(PVector v, float win, float cin, float rin) {
    super(v.x,v.y,v.z);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=win;
    w1=1f/win;
    c=cin;
    r=rin;
    r2=r*r;
  }
  
/**
 * Get a copy of this Point. Velocity, charge and force information is not copied.
 *
 * @return The copy.
 *
 */
  public Point copy() {
    Point p = copy(false);
	return(p);
  }

/**
 * Get a copy of this Point.
 *
 * @param copyVelocityAndForce Whether to copy velocity, charge and force also.
 * 
 * @return The copy.
 *
 */
  public Point copy(boolean copyVelocityAndForce) {
	Point p;
	if (copyVelocityAndForce) {
	  p = new Point(x,y,z,w,c,r);
	} else {
	  p = new Point(x,y,z,w);
	}
    p.U=U;
    return(p);
  }
  
/**
 * Set the weight.
 *
 * @param win The weight value.
 *
 */
  public Point setW(float win) {
  	w=win;
  	if (win!=0) {
  		w1=1/win;
  	} else {
  		w1=Float.MAX_VALUE;
  	}
  	return(this);
  }
  
  // Get the weight.
  public float getW() {
  	return(w);
  }
  
/**
 * Set the charge.
 *
 * @param cin The charge value.
 *
 */
  public Point setC(float cin) {
  	c=cin;
  	return(this);
  }
  
  // Get the charge.
  public float getC() {
  	return(c);
  }
  
/**
 * Set the range.
 *
 * @param rin The range value.
 *
 */
  public Point setR(float rin) {
  	r=rin;
  	r2=r*r;
  	return(this);
  }
  
  // Get the range.
  public float getR() {
  	return(r);
  }
  
/**
 * Set whether the object is unyielding.
 *
 * @param uin The unyielding flag.
 *
 */
  public void setU(boolean uin) {
  	U=uin;
  }
  
  // Get unyielging flag.
  public boolean getU() {
  	return(U);
  }
  
/**
 * Set the unyielding multiplier.
 *
 * The unyielding multiplier indicated whether the object should drift around it's position when
 * unyielding (pinned)
 *
 * @param umin The weight value.
 *
 */
  public Point setUMult(PVector umin) {
  	uMult=umin;
  	return(this);
  }
  
  // Get unyielding multiplier.
  public PVector getUMult() {
  	return(uMult);
  }

/**
 * Get the current velocity vector as a PVector.
 *
 * @return The current velocity vector as a PVector.
 *
 */
  public PVector getV() {
   	PVector r=get();
   	r.sub(old);
   	return(r);
   }


  public Point setPos(PVector pos) {
  	x=pos.x;
  	y=pos.y;
  	z=pos.z;
  	old=pos.get();
  	return(this);
  }

/**
 * Get the squared distance to another Point (fast).
 *
 * @return The squared distance value.
 *
 */
  public float distance2To(Point p) {
      float dx=p.x-x;
      float dy=p.y-y;
      float dz=p.z-z;
      return dx*dx+dy*dy+dz*dz;
  }

/**
 * Get the distance to another Point.
 *
 * @return The distance value.
 *
 */
  public double distanceTo(Point p) {
    return(Math.sqrt(distance2To(p)));
  }
}