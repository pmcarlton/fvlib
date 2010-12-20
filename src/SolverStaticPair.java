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

public final class SolverStaticPair extends Solver {

private Point[][] points=new Point[2][0];			     // the two points arrays

private float bias=.001f, range=200;     // Bias limits the maximum force (at zero distance).
private float range2=range*range;		 // Range specifies the maximum range below which force is applied.
private boolean inv=false;				 // Option for inverting attract/repel forces.

// Constructor. Creates empty arrays.
  public SolverStaticPair() {
  }
  
  // Constructor. Uses ArrayLists and generics.
  public SolverStaticPair(ArrayList<Point> pointsin0, ArrayList<Point> pointsin1) {
    points[0]=new Point[pointsin0.size()];
    points[1]=new Point[pointsin1.size()];
    pointsin0.toArray(points[0]);
    pointsin1.toArray(points[1]);
  }
  
  // Constructor. Uses arrays.
  public SolverStaticPair(Point[] pointsin0, Point[] pointsin1) {
    points[0]=pointsin0;
    points[1]=pointsin1;
  }
  
  // Various getter-setter functions.
  
  public SolverStaticPair setP(ArrayList<Point> pointsin0, ArrayList<Point> pointsin1) {
    points[0]=new Point[pointsin0.size()];
    points[1]=new Point[pointsin1.size()];
    pointsin0.toArray(points[0]);
    pointsin1.toArray(points[1]);
  	return(this);
  }
  
  public SolverStaticPair setP(Point[] pointsin0, Point[] pointsin1) {
  	points[0]=pointsin0;
    points[1]=pointsin1;
  	return(this);
  }
  
  public SolverStaticPair setBias(float biasin) {
  	bias=biasin;
  	range2=range*range+bias;
  	return(this);
  }
  
  public SolverStaticPair setRange(float rangein) {
  	range=rangein;
  	range2=range*range+bias;
  	return(this);
  }
  
  public SolverStaticPair setInv(boolean invin) {
    inv=invin;
    return(this);
  }
  
  public Point[][] getP() {
  	return(points);
  }
  
  public float getBias() {
  	return(bias);
  }
  
  public float getRange() {
  	return(range);
  }
  
  @Override
  protected final void stepFunction(int step, int offset) {
    if (!inv) {stepPoints(points, step, offset);} else {stepPointsi(points, step, offset);}
  }
  
  private final void stepPoints(Point[][] points, int step, int offset) {
    float d2;
    Point p1,p2;
    float x1,y1,z1,c1,r1;
    float dx,dy,dz,lx,ly,lz;
    for (int i=offset,j=points[0].length;i<j;i+=step) {
      p1=points[0][i];
      x1=p1.x;
      y1=p1.y;
      z1=p1.z;
      c1=p1.c;
      r1=p1.r2;
      for (int k=offset,l=points[1].length;k<l;k++) {
        p2=points[1][k];
        dx=p2.x-x1;
        dy=p2.y-y1;
        dz=p2.z-z1;
        d2=dx*dx+dy*dy+dz*dz+bias;
        if (d2<r1||d2<p2.r2) {
          d2=(c1*p2.c)/d2;
          lx=dx*d2;
          ly=dy*d2;
          lz=dz*d2;
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
  
  private final void stepPointsi(Point[][] points, int step, int offset) {
    float d2;
    Point p1,p2;
    float x1,y1,z1,c1,r1;
    float dx,dy,dz,lx,ly,lz;
    for (int i=offset,j=points[0].length;i<j;i+=step) {
      p1=points[0][i];
      x1=p1.x;
      y1=p1.y;
      z1=p1.z;
      c1=p1.c;
      r1=p1.r2;
      for (int k=offset,l=points[1].length;k<l;k++) {
        p2=points[1][k];
        dx=p2.x-x1;
        dy=p2.y-y1;
        dz=p2.z-z1;
        d2=dx*dx+dy*dy+dz*dz+bias;
        if (d2<r1||d2<p2.r2) {
          d2=(c1*p2.c)/d2;
          lx=dx*d2;
          ly=dy*d2;
          lz=dz*d2;
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