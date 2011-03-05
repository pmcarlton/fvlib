// This Processing sketch demonstrates a simple and effective method
// to model deformable objects using plain point-spring systems.
// It simulates the impact of a car chassis generated as a point-spring
// system on a solid cube block.
// The car model was specifically designed for this simulation using
// Rhino. It was then exported as an .obj file and imported to
// Processing using the very nice ObjLoader library.
// The physics object is modelled using minimal information from the
// 3D-model, namely vertex coordinates. Then, springs between
// points within a specified distance threshold are generated.
// If this threshold is selected to be about double the average distance
// between mesh vertices, the mesh stiffens and behavior of solid
// (not cloth) sheet materials can be observed in the model.
// While using this procedure it is extremely simple to generate a basic
// deformable model, it is also the source of some drawbacks. 
// First, the physics model is highly dependent on
// the meshing of the imported 3D model. If vertices on the
// mesh are relatively dense and evenly spaced, a realistic
// behavior is more likely than in a model with uneven vertex
// spacing. 
// Second, because the physics object is essentially
// one large chunk of points and springs, detachments are harder
// to model. In the case of this simulation they do not exist at 
// all, and that is the reason that the body tends to be a bit
// 'gooey' and rubbery.
// Finally, it should be noted that as the object scale 
// increases it becomes increasingly difficult and computationally
// expensive to give the feeling of a solid body just by using
// local springs. Already at the scale of a car and the latency
// between the springs gives a noticeable elasticity to the model.
// Still though, it is my opinion that this example demonstrates
// what might be the simplest way to go when it comes to deformation
// modelling and a good tradeoff of speed vs. realism, trading off 
// some extra realism for near-realtime performance which for simpler
// and more carefully built models could even reach realtime.

import saito.objloader.*;

import fvlib.*;

import processing.opengl.*;

import peasy.org.apache.commons.math.*;
import peasy.*;
import peasy.org.apache.commons.math.geometry.*;

//
// Simulation Parameters
//

float Force = 0.0010;
float Friction = 0.998;
float Stiffness = 0.029;
float MaxRange = 220;
float MinRange = 20;
int Steps = 5;
int maxLinks = 28;

//
// Advanced Simulation Parameters
//

float PlasticLimitComp = 0.04;
float PlasticLimitTens = 0.04;
float PlasticityFactor = 0.5;

ArrayList<Point> Points;
ArrayList<Link> Links;
IntegratorVerlet vi;
SolverRelaxation sr;
ConstantForce cf;
PeasyCam cam;
String model = "carbody4.obj";
OBJModel o;

boolean reset = true;      // Set to false for synchronized reset
boolean go = false;        // Accelerate model
boolean capture = false;   // Capture the current frame
boolean enable = false;    // Enable/disable physics
boolean wall = true;       // Show the wall
boolean rot = false;       // Rotate

//
// Basic setup
//
void setup() {
  size(1200,720,OPENGL);
  frameRate(30);
  cam = new PeasyCam(this, 0,0,0,550);
  importObj(model);
}

//
// Stepping & drawing routine
//
void draw() {
  if (!reset) {
    background(0);
    // Lights
    pointLight(7550,7550,7505,1000,1000,1000);
    pointLight(8000,8000,8000,-2000,-2000,-2000);
    fill(255);
      noStroke();
    // The wall
    if (wall) {
      pushMatrix();
      translate(100,0,900);
      box(200,150,600);
      popMatrix();
    }
    // If physics is enabled
    if (enable) {
      for (int i=0;i<Steps;i++) {
      checkCollision();
      vi.step();
      sr.step();
        if (go) {
          cf.step();
        }
      }
      // This one is the heart of the plastic behavior simulation.
      // It allows for some plasticity during the elastic deformation.
      // Not very accurate because it is time based while it should
      // be strain-based. Possible future implementation in fvlib?
      for (Link l : Links) {
        l.updateL(); // Update with new length
        l.C = (1-PlasticityFactor*.005)*l.C + PlasticityFactor*.005*l.L;
        if (l.L*(1+PlasticLimitComp)<l.C || l.L*(1-PlasticLimitTens)>l.C) {
          l.C = (1-PlasticityFactor)*l.C + PlasticityFactor*l.L;
        }
      }
      // Update the positions of the obj mesh
      for (int i=0, j=Points.size();i<j;i++) {
        Point p = Points.get(i);
        o.setVertex(i, p.x, p.y, p.z);
      }
    }
    //Drawing Function
    /**fill(160);
    //stroke(255,20);
    for (int i=0, j=o.getFaceCount();i<j;i++) {
      PVector[] p = o.getFaceVertices(i);
      if (p.length==3) {
        beginShape(TRIANGLES);
      } else {
        beginShape(QUADS);
      }
      for (PVector vtx : p) {
        vertex(vtx.x,vtx.y,vtx.z);
      }
      endShape();   
    }*/
    // Enable this if you want to display
    // a wireframe of the physics object
    // !!WARNING: VERY SLOW RENDERING!!
    /**stroke(255,80);
    strokeWeight(1);
    for (Link l : Links) {
      line(l.p1.x,l.p1.y,l.p1.z,l.p2.x,l.p2.y,l.p2.z);
    }*/
  } else {
    // Synchronized reset / reload of .obj
    // Otherwise naturally all hell breaks loose.
    enable = false;
    go=false;
    importObj(model);
    reset = false;
  }
  // Capture frame
  if (capture) {
    saveFrame("out-####.png");
    noFill();
    cam.beginHUD();
    stroke(255,0,0);
    rect(1,1,width-1,height-1);
    cam.endHUD();
  }
  if (rot) {
    cam.rotateX(.01);
  }
}

//
// Import and initialization routine
//
void importObj(String filename) {
  Points = new ArrayList<Point>();
  Links = new ArrayList<Link>();
  // Import the file
  o = new OBJModel(this, filename);
  // Translate
  o.translate(new PVector(0,0,200));
  // Generate the physics object
  for( int i=0, j=o.getVertexCount(); i<j; i++ ) {
    Points.add(new Point(o.getVertex(i)));
  }
  for (int i=0, m=Points.size(); i<m; i++) {
    int numLinks = 0;
    for (int j=i+1; j<m; j++) {
      Point p1 = Points.get(i);
      Point p2 = Points.get(j);
      if (p1 != p2 && p1.distance2To(p2) < MaxRange && p1.distance2To(p2) > MinRange) {
        Links.add(new Link(p1,p2,Stiffness));
        numLinks++;
      }
      if (numLinks > maxLinks) break;
    }
  }
  // Initialize solvers
  vi = new IntegratorVerlet(Points).setF(Friction);      // Initialize Verlet Integration
  sr = new SolverRelaxation(Links);                      // Initialize Relaxation Solver
  //sr = new SolverRelaxation(Links).setFast(true);      // A bit faster but gooier
  cf = new ConstantForce(Points,new PVector(0,0,Force)); // Initialize simple constant force
                                                         // solver
}

// Simple point v box collision function
void checkCollision() {
  for (Point p : Points) {
    if (p.z>598 && p.x>0) {
      if (p.z-598<p.x) {
        p.sforce.z-=p.z-598;
      } else {
        p.sforce.x-=p.x;
      }
      // Stop accelerating as soon as the wall is hit.
      // Otherwise the car is crushed into spaghetti madness.
      go=false;
    }
  }
}

// Start/stop and reset
void keyPressed() {
  if (key == ' ') {
    go = true;
  } else if (key == 'r') {
    reset = true;
  } else if (key == 'c') {
    capture = !capture;
  } else if (key == 'e') {
    enable = !enable;
  } else if (key == 'w') {
    wall = !wall;
  } else if (key == 't') {
    rot = !rot;
  }
}

