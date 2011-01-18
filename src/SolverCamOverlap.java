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

// fvlib module for forcing separation of nearby points in 3D space from a particular
// viewing point.
// Useful to avoid e.g. coincidence of nodes in 3D space when visualizing a graph.
// Accepts a point list and a position vector (Camera Position) and uses Camera-Point
// vectors to determine how close-by points would seem from the viewpoint. It then
// applies a repulsive force to points below a threshold.
// Initial implementation. Still slow and quite buggy. Also requires some thorough
// commenting.

package fvlib;

import processing.core.*;
import java.util.*;
import java.util.concurrent.*;

public final class SolverCamOverlap extends Solver {

private Point[] points;					// double array that holds points
private PVector camPos;					// Camera Position
private float bias=.001f, range=200;     // Bias limits the maximum force (at zero distance).
private float range2=range*range;		// Range specifies the maximum range below which force is applied.
private float magnitude=.1f;			// Repelling force magnitude.

// Constructor. Creates empty arrays.
  public SolverCamOverlap() {
    points=new Point[0];
  }
  
  // Constructor. Uses ArrayLists and generics.
  public SolverCamOverlap(ArrayList<Point> pointsin, PVector camPosIn) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
	camPos = camPosIn;
  }
  
  // Constructor. Uses arrays.
  public SolverCamOverlap(Point[] pointsin, PVector camPosIn) {
    points=pointsin;
	camPos = camPosIn;
  }
  
  // Various getter-setter functions.
  
  public SolverCamOverlap setP(ArrayList<Point> pointsin) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
  	return(this);
  }
  
  public SolverCamOverlap setP(Point[] pointsin) {
  	points=pointsin;
  	return(this);
  }
  
  public SolverCamOverlap setBias(float biasin) {
  	bias=biasin;
  	range2=range*range+bias;
  	return(this);
  }
  
  public SolverCamOverlap setRange(float rangein) {
  	range=rangein;
  	range2=range*range+bias;
  	return(this);
  }
  
  public SolverCamOverlap setCam(PVector camPosIn) {
	camPos=camPosIn;
	return(this);
  }
  
  public SolverCamOverlap setMagnitude(float magin) {
  	magnitude=magin;
  	return(this);
  }
  
  public Point[] getP() {
  	return(points);
  }
  
  public float getBias() {
  	return(bias);
  }
  
  public float getRange() {
  	return(range);
  }
  
  public PVector getCam() {
  	return(camPos);
  }
  
  public float getMagnitude() {
  	return(magnitude);
  }
  
  @Override
  protected final void stepFunction(int step, int offset) {
    stepPoints(points, step, offset);
  }
  
  private final void stepPoints(Point[] points, int step, int offset) {
    
	float d2, u;					// Square of (projected) distance, helper for projection
	
	float r2 = range2;				// Local reference for square of range.
	float m = magnitude;			// Local reference for magnitude.
	float cP1x, cP1y, cP1z, cP1d;	// Point1-Camera Vector.
	float cP2x, cP2y, cP2z, cP2d;	// Point2-Camera Vector.
    
	Point p1, p2;					// Points.
	
    float x1, y1, z1;				// Point 1 (p1) coordinates.
    float dx, dy, dz, lx, ly, lz;	// Distance components and force components.
    
	for (int i = offset, j = points.length; i < j; i += step) {
      p1 = points[i];
      x1 = p1.x;
      y1 = p1.y;
      z1 = p1.z;
	  cP1x = x1 - camPos.x;
	  cP1y = y1 - camPos.y;
	  cP1z = z1 - camPos.z;
	  cP1d = (float)Math.sqrt(cP1x * cP1x + cP1y * cP1y + cP1z * cP1z);
      for (int k = i + 1; k < j; k++) {
        p2 = points[k];
		cP2x = p2.x - camPos.x;
		cP2y = p2.y - camPos.y;
		cP2z = p2.z - camPos.z;
		// Check if the dot product between the two Camera-Point vectors is
		// higher than a threshhold; that is to say, if the points are far
		// enough from the camera or really close by.
		// We don't want points that lie around the camera to be affected
		// by this procedure. Should be improved in the future to really
		// include all points around the camera.
		if (cP1x * cP2x + cP1y * cP2y + cP1z * cP2z > 20) {
		  dx = p2.x - x1;
		  dy = p2.y - y1;
		  dz = p2.z - z1;
		  // Project to plane defined by point1-camera vector [cP1x, cP1y, cP1z].
		  u = (dx * cP1x + dy * cP1y + dz * cP1z) / (cP1x * cP1x + cP1y * cP1y + cP1z * cP1z);
		  dx -= u * cP1x;
		  dy -= u * cP1y;
		  dz -= u * cP1z;
		  d2 = (dx * dx + dy * dy + dz * dz) / cP1d ;
		  if (d2 < r2) {
			d2 = m / d2;
			lx = dx * d2;
			ly = dy * d2;
			lz = dz * d2;
			p1.sforce.x -= lx;
			p1.sforce.y -= ly;
			p1.sforce.z -= lz;
			p2.sforce.x += lx;
			p2.sforce.y += ly;
			p2.sforce.z += lz;
		  }
		}
      }
    }
  }
}