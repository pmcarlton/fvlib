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
// "Advanced Character Physics" paper.

package volatileprototypes.fvlib;

import java.lang.Math.*;
import processing.core.*;

/**
 * @author      Yiannis Chatzikonstantinou <contact@volatileprototypes.com>
 * @version     0.5.9                                    
 * @since       0.2          
 */
public class Link {

  public Point p1,p2;
  public float L,C,S; 	// Current Length, Original Length, Stiffness
  				// Plz Note! L does not update automatically to allow
				// more control over processor resources.
  				// One must call getL(true) to get the current
  				// L-value.

/**
 * Constructor.
 *
 * Constructor, generates a new class instance using point references and stiffness.
 *
 * @param p1in A Point object indicating the 1st point.
 * @param p2in A Point object indicating the 2nd point.
 * @param st A float value indicating stiffness.
 *
 */
  public Link(Point p1in, Point p2in,float st) {
    p1=p1in;
    p2=p2in;
	C = getL(true);
    S=st;
  }
  
/**
 * Set the rest length to the specified value.
 *
 * @param cin The new rest length value.
 * 
 * @return The current object.
 *
 */
  public final Link setC(float cin) {
  	C=cin;
  	return(this);
  }
  
/**
 * Get the rest length.
 * 
 * @return The rest length.
 *
 */
  public final float getC() {
  	return(C);
  }
  
/**
 * Get the current (cached) length value.
 * 
 * @return The current (cached) length value.
 *
 */
  public final float getL() {
  	return(L);
  }
  
/**
 * Get the current (cached) length value.
 *
 * @param update Whether to update the cached length value.
 * @return The current (cached) length value.
 *
 */
  public final float getL(boolean update) {
  	if (update) updateLength();
	return(L);
  }

  @Deprecated
  public final Link updateL() {
    float dx=p2.x-p1.x;
  	float dy=p2.y-p1.y;
  	float dz=p2.z-p1.z;
  	L=(float)Math.sqrt(dx*dx+dy*dy+dz*dz);
  	return(this);
  }
  
  // Update the current length to the correct value.
  private final void updateLength() {
	float dx=p2.x-p1.x;
  	float dy=p2.y-p1.y;
  	float dz=p2.z-p1.z;
  	L=(float)Math.sqrt(dx*dx+dy*dy+dz*dz);
  }
  
/**
 * Sets the 1st point.
 *
 * @param p1in The 1st point.
 */
  public final Link setP1(Point p1in) {
  	p1=p1in;
  	return(this);
  }

/**
 * Sets the 2nd point.
 *
 * @param p2in The 2nd point.
 */
  public final Link setP2(Point p2in) {
  	p2=p2in;
  	return(this);
  }

/**
 *  Get the bias value.
 *
 * @return The current bias value.
 */
  public final Point getP1() {
  	return(p1);
  }

/**
 *  Get the bias value.
 *
 * @return The current bias value.
 */
  public final Point getP2() {
  	return(p2);
  }
  
/**
 *  Get a PVector of the current coordinates.
 *
 * @return The PVector of the current coordinates.
 */
  public final PVector get() {
  	PVector r=p2.get();
  	r.sub(p1);
  	return(r);
  }
}