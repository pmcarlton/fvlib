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

public final class SolverRelaxation extends Solver {

private Link[] links;			// array that holds points
private boolean fast=false;		// Option for fast/accurate spring solver.

// Constructor. Creates empty arrays.
  public SolverRelaxation() {
    links=new Link[0];
  }
  
  // Constructor. Uses ArrayLists and generics.
  public SolverRelaxation(ArrayList<Link> linksin) {
    links=new Link[linksin.size()];
    linksin.toArray(links);
  }
  
  // Constructor. Uses arrays.
  public SolverRelaxation(Link[] linksin) {
    links=linksin;
  }
  
  // Various getter-setter functions.
  
  public SolverRelaxation setL(ArrayList<Link> linksin) {
    links=new Link[linksin.size()];
  	linksin.toArray(links);
  	return(this);
  }
  
  public SolverRelaxation setL(Link[] lin) {
  	links=lin;
  	return(this);
  }
  
  public Link[] getL() {
  	return(links);
  }
  
  public final SolverRelaxation setFast(boolean fin) {
  	fast=fin;
  	return(this);
  }
  
  @Override
  protected final void stepFunction(int step, int offset) {
    if (fast) {fStepLinks(links, step, offset);} else {stepLinks(links, step, offset);}
  }
  
  //Dynamic relaxation solver for springs
  //for details see:
  //Jakobsen, Thomas - Advanced Character Physics
  private final void stepLinks(Link[] links, int step, int offset) {
    float d;
    float L2,C;
    Point p1,p2;
    float dx,dy,dz,lx,ly,lz;
    Link l;
    for (int i=offset,j=links.length;i<j;i+=step) {
      l=links[i];
      p1=l.p1; p2=l.p2;
      dx=p2.x-p1.x;
      dy=p2.y-p1.y;
      dz=p2.z-p1.z;
      L2 = dx*dx+dy*dy+dz*dz;
      C = l.C;
      d = C + L2/C; 
      d = (float)(d*.25 + L2/d);
      d = (float)(l.S*(1-(C/d)));
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
  
  // Faster variant with one Newton iteration.
  private final void fStepLinks(Link[] links, int step, int offset) {
    float d;
    float L2,C2;
    Point p1,p2;
    float dx,dy,dz,lx,ly,lz;
    Link l;
    for (int i=offset,j=links.length;i<j;i+=step) {
      l=links[i];
      p1=l.p1; p2=l.p2;
      dx=p2.x-p1.x;
      dy=p2.y-p1.y;
      dz=p2.z-p1.z;
      L2 = dx*dx+dy*dy+dz*dz;
      C2 = l.C; 
      C2*= C2;
      d = (float)(l.S*(C2/(L2+C2)-.5f));
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