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
// "Advanced Character Physics" paper.

package fvlib;

import java.lang.Math.*;
import processing.core.*;

public class Link {

  public Point p1,p2;
  public float L,C,S; 	// Current Length, Original Length, Stiffness
  				// Plz Notice! L does not get updated automatically.
  				// One must call updateL() to get the correct
  				// L-value.

  // Constructor.
  public Link(Point p1in, Point p2in,float st) {
    p1=p1in;
    p2=p2in;
    updateC();
    S=st;
  }
  
  // Update the original length(C) to reflect the current
  // distance between points. Also copy to current length (L)
  // and return.
  public final Link updateC() {
  	float dx=p2.x-p1.x;
  	float dy=p2.y-p1.y;
  	float dz=p2.z-p1.z;
  	C=(float)Math.sqrt(dx*dx+dy*dy+dz*dz);
  	return(this);
  }
  
  // Set the original length to the specified value.
  public final Link setC(float cin) {
  	C=cin;
  	return(this);
  }
  
  // Get original length.
  public final float getC() {
  	return(C);
  }
  
  // Get current length.
  public final float getL() {
  	return(L);
  }
  
  // Update the current length to the correct value. This
  // must be called explicitly before getting the length
  // because it is not updated automatically during the
  // dynamic relaxation step.
  public final Link updateL() {
    float dx=p2.x-p1.x;
  	float dy=p2.y-p1.y;
  	float dz=p2.z-p1.z;
  	L=(float)Math.sqrt(dx*dx+dy*dy+dz*dz);
  	return(this);
  }
  
  // Get/set points.
  public final Link setP1(Point p1in) {
  	p1=p1in;
  	return(this);
  }
  
  public final Link setP2(Point p2in) {
  	p2=p2in;
  	return(this);
  }
  
  public final Point getP1() {
  	return(p1);
  }
  
  public final Point getP2() {
  	return(p2);
  }
  public final PVector get() {
  	PVector r=p2.get();
  	r.sub(p1);
  	return(r);
  }
}