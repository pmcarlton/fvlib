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
import java.util.*;
import java.util.concurrent.*;

public final class IntegratorVerlet extends Solver {

private float F=.99f;			// Friction Constant.
private Point[] points;			// array that holds points

// Constructor. Creates empty arrays.
  public IntegratorVerlet() {
    points=new Point[0];
  }
  
  // Constructor. Uses ArrayLists and generics.
  public IntegratorVerlet(ArrayList<Point> pointsin) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
  }
  
  // Constructor. Uses arrays.
  public IntegratorVerlet(Point[] pointsin) {
    points=pointsin;
  }
  
  // Various getter-setter functions.
  
  public final IntegratorVerlet setF(float fin) {
  	F=fin;
  	return(this);
  }
  
  public final float getF() {
  	return(F);
  }
  
  public IntegratorVerlet setP(ArrayList<Point> pointsin) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
  	return(this);
  }
  
  public IntegratorVerlet setP(Point[] pin) {
  	points=pin;
  	return(this);
  }
  
  public Point[] getP() {
  	return(points);
  }
  
 // Verlet Integration function for stepping points.
  // Each loop:
  // 1.Current position gets copied to temp position.
  // 2.New position is defined by adding the difference
  //   from the previous and adding forces.
  // 3.Temp position is copied to the old position.
  // 4.Forces are reset.
  // If the point is flagged as unyielding, the current
  // position is set to the old one.
  
  @Override
  protected final void stepFunction(int step, int offset) {
    float tx,ty,tz;
    Point p;
    PVector o,f,u;
	for (int i=offset,j=points.length;i<j;i+=step) {
		p=points[i];
		o=p.old;
		f=p.sforce;
		tx=p.x;
		ty=p.y;
		tz=p.z;
		if (!p.U) {
			p.x+=(tx-o.x)*F+f.x;
			p.y+=(ty-o.y)*F+f.y;
			p.z+=(tz-o.z)*F+f.z;
			
		} 
		else {
			u=p.uMult;
			p.x+=((tx-o.x)*F+f.x)*u.x;
			p.y+=((ty-o.y)*F+f.y)*u.y;
			p.z+=((tz-o.z)*F+f.z)*u.z;
		}
		o.x=tx;
		o.y=ty;
		o.z=tz;
		f.x=f.y=f.z=0;
	}
  }
}