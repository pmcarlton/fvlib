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

package fvlib;

import processing.core.*;
import java.util.*;
import java.util.concurrent.*;

public final class ConstantForce extends Solver {

private Point[] points;			// array that holds points
private PVector f;

// Constructor. Creates empty arrays.
  public ConstantForce() {
    points=new Point[0];
    f=new PVector(0,0,0);
  }
  
  // Constructor. Uses ArrayLists and generics.
  public ConstantForce(ArrayList<Point> pointsin, PVector fin) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
    f=fin;
  }
  
  // Constructor. Uses arrays.
  public ConstantForce(Point[] pointsin, PVector fin) {
    points=pointsin;
    f=fin;
  }
  
  // Various getter-setter functions.
  
  public ConstantForce setP(ArrayList<Point> pointsin) {
    points=new Point[pointsin.size()];
  	pointsin.toArray(points);
  	return(this);
  }
  
  public ConstantForce setP(Point[] pin) {
  	points=pin;
  	return(this);
  }
  
  public Point[] getP() {
  	return(points);
  }
  
  public ConstantForce setF(PVector fin) {
  	f=fin;
  	return(this);
  }
  
  public PVector getF() {
  	return(f);
  }
  
  @Override
  protected final void stepFunction(int step, int offset) {
    stepPoints(points, step, offset);
  }
  
  private final void stepPoints(Point[] points, int step, int offset) {
    Point p;
    for (int i=offset,j=points.length;i<j;i+=step) {
      p=points[i];
      p.sforce.x+=f.x*p.w;
      p.sforce.y+=f.y*p.w;
      p.sforce.z+=f.z*p.w;
    }
  }
}