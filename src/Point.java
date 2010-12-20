//  Copyright (c) 2010, Ioannis (Yiannis) Chatzikonstantinou, ?All rights reserved.
//  http://prototy.blogspot.com
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

package fvlib;

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
  
  // Constructor using coordinates.
  public Point(float xin, float yin, float zin, float win) {
    super(xin,yin,zin);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=win;
    w1=1f/win;
  }
  
  // Constructor using coordinates with charge, weight and range.
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
  
  // Constructor using PVector.
  public Point(PVector v, float win) {
    super(v.x,v.y,v.z);
    old=this.get();
    sforce=new PVector(0,0,0);
    w=win;
    w1=1f/win;
  }
  
  // Constructor using PVector with charge and range.
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
  
  // Copy this to a new one and return.
  public Point copy() {
    Point p=new Point(x,y,z,w);
    p.U=U;
    return(p);
  }
  
  // Set the weight(W) and 1/weight (W1).
  // If =0 needs some more attention.
  public Point setW(float win) {
  	w=win;
  	if (win!=0) {
  		w1=1/win;
  	} else {
  		w1=100000;
  	}
  	return(this);
  }
  
  // Get the weight.
  public float getW() {
  	return(w);
  }
  
  // Set the charge.
  public Point setC(float cin) {
  	c=cin;
  	return(this);
  }
  
  // Get the charge.
  public float getC() {
  	return(c);
  }
  
  // Set the range.
  public Point setR(float rin) {
  	r=rin;
  	r2=r*r;
  	return(this);
  }
  
  // Get the range.
  public float getR() {
  	return(r);
  }
  
  // Set unyielging flag.
  public void setU(boolean uin) {
  	U=uin;
  }
  
  // Get unyielging flag.
  public boolean getU() {
  	return(U);
  }
  
  // Set unyielging multiplier.
  public Point setUMult(PVector umin) {
  	uMult=umin;
  	return(this);
  }
  
  // Get unyielding multiplier.
  public PVector getUMult() {
  	return(uMult);
  }
  
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
}