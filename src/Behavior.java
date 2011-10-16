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
 * @since       0.5.9          
 */
public class Behavior extends Solver {

  protected Point[] points;					// double array that holds points

/**
 * Constructor, generates a new class instance with empty point array.
 *
 */
  public Behavior() {
    points=new Point[0];
  }
  
/**
 * Constructor, generates a new class instance using a copy of the supplied Point ArrayList.
 *
 * @param pointsin An ArrayList containing Point objects with which the object's list will be initialized.
 *
 */
  public Behavior(ArrayList<? extends Point> pointsin) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
  }
  
/**
 * Constructor.
 *
 * Constructor, generates a new class instance using the supplied array.
 *
 * @param pointsin An array containing Point objects with which the object's list will be initialized.
 *
 */
  public Behavior(Point[] pointsin) {
    points=pointsin;
  }
  
/**
 * Sets the object's Point array using a copy of the supplied ArrayList.
 *
 * @param pointsin The ArrayList containing Point objects.
 */
  public void setP(ArrayList<? extends Point> pointsin) {
    points=new Point[pointsin.size()];
    pointsin.toArray(points);
  }

/**
 * Sets the object's Point array using the supplied array.
 *
 * @param pointsin The array containing Point objects.
 */
  public void setP(Point[] pointsin) {
  	points=pointsin;
  }
  
/**
 *  Get the current point array.
 *
 * @return The current point array.
 */
  public Point[] getP() {
  	return(points);
  }
}