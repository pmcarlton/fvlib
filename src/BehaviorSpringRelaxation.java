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
import java.util.*;
import java.util.concurrent.*;

public final class BehaviorSpringRelaxation extends Solver {

private Link[] links;			// array that holds points
private boolean fast=false;		// Option for fast/accurate spring solver.

/**
 * Constructor, generates a new class instance.
 *
 */
  public BehaviorSpringRelaxation() {
    links=new Link[0];
  }
  
/**
 * Constructor, generates a new class instance using a copy of the supplied Link ArrayList.
 *
 * @param linksin An ArrayList containing Link objects with which the object's list will be initialized.
 *
 */
  public BehaviorSpringRelaxation(ArrayList<Link> linksin) {
    links=new Link[linksin.size()];
    linksin.toArray(links);
  }
  
/**
 * Constructor, generates a new class instance using the supplied Link array.
 *
 * @param linksin An array containing Link objects with which the object's array will be initialized.
 *
 */
  public BehaviorSpringRelaxation(Link[] linksin) {
    links=linksin;
  }
  
/**
 * Sets the object's Link array using a copy of the supplied ArrayList.
 *
 * @param linksin The ArrayList containing Link objects.
 *
 * @return The current object.
 */
  public BehaviorSpringRelaxation setL(ArrayList<Link> linksin) {
    links=new Link[linksin.size()];
  	linksin.toArray(links);
  	return(this);
  }

/**
 * Sets the object's Link array using the supplied array.
 *
 * @param linksin The array containing Link objects.
 *
 * @return The current object.
 */
  public BehaviorSpringRelaxation setL(Link[] linksin) {
  	links=linksin;
  	return(this);
  }

/**
 *  Get the current Link array.
 *
 * @return The current Link array.
 */
  public Link[] getL() {
  	return(links);
  }
  
/**
 * Sets whether to use a faster solver or the normal one.
 *
 * In fvlib, a fast solver is implemented for spring interactions that uses just one Newton iteration to approximate
 * the square root of the squared length between points using the rest length as reference. The faster solver reduces precision but increases
 * simulation speed noticeably.
 *
 * @param fin A boolean representing whether to use fast sqrt.
 *
 * @return The current object.
 */
  public final BehaviorSpringRelaxation setFast(boolean fin) {
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