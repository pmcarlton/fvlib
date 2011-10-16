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

package volatileprototypes.fvlib;

import processing.core.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author      Yiannis Chatzikonstantinou <contact@volatileprototypes.com>
 * @version     0.5.9                                    
 * @since       0.4          
 */
public final class BehaviorConstantForce extends Behavior {

private PVector f;

/**
 * Constructor, generates a new class instance.
 *
 */
  public BehaviorConstantForce() {
    super();
  }
  
/**
 * Constructor, generates a new class instance using a copy of the supplied Point ArrayList.
 *
 * @param pointsin An ArrayList containing Point objects with which the object's list will be initialized.
 * @param fin A PVector object representing the force to be applied to the Point objects.

 *
 */
  public BehaviorConstantForce(ArrayList<? extends Point> pointsin, PVector fin) {
    super(pointsin);
    f=fin;
  }
  
/**
 * Constructor, generates a new class instance using the supplied Point array.
 *
 * @param pointsin An array containing Point objects with which the object's array will be initialized.
 * @param fin A PVector object representing the force to be applied to the Point objects.
 *
 */
  public BehaviorConstantForce(Point[] pointsin, PVector fin) {
    super(pointsin);
    f=fin;
  }

/**
 * Sets the force vector.
 *
 * @param fin The force vector.
 */
  public BehaviorConstantForce setF(PVector fin) {
  	f=fin;
  	return(this);
  }

/**
 * Returns the current force vector.
 *
 * @return The range value.
 */
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