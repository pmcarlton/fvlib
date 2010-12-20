import fvlib.*;
import processing.opengl.*;

Point[] psa;
Link[] lsa;
IntegratorVerlet vi;
SolverRelaxation rs;
int steps=15;
int it=0;
float msec=0;
PFont font;

void setup() {
  ArrayList ps=new ArrayList();
  ArrayList ls=new ArrayList();
  size(600,600,OPENGL);
  
  // Construct the curve all at once in the beginning.
  float tx=0,ty=-300;
  for (int i=0;i<9800;i++) {
    tx=max(-300,min(300,tx+random(10)-5));
    ty-=14;
    Point p=new Point(tx,ty,0,1);
    ps.add(p);
    int s=ps.size();
    for (int j=max(0,s-7);j<s-1;j++) {
      Point p1=(Point)ps.get(j);
      PVector pv=p.get();
      pv.sub(p1);
      ls.add(new Link(p,p1,.25));
    }
  }

  vi=new IntegratorVerlet(ps);
  rs=new SolverRelaxation(ls).setFast(true);
  // Convert ArrayLists to arrays for drawing performance.
  psa=new Point[ps.size()];
  lsa=new Link[ls.size()];
  ps.toArray(psa);
  ls.toArray(lsa);
  
  font = createFont("Arial",12);
}

void draw() {
  float h=0;
  background(50,100);
  Point p;
  for (int j=steps;j>=0;j--) {
    h=sin((float)it*.02);
    it++;
    for (int i=psa.length-1;i>=0;i--) {
      p=psa[i];
      p.y+=.013;
      p.y=min(p.y,230-h*70);
      p.x=min(p.x,300);
      p.x=max(p.x,-300);
    }
    msec*=.995;
    long start=System.currentTimeMillis(); //Benchmark
    vi.step();
    rs.step();
    msec+=(float)(System.currentTimeMillis()-start)*.005; //Benchmark
  }
  pushMatrix();
  translate(300,300);
  noFill();
  stroke(255,50);
  strokeWeight(3);
  beginShape();
  // Curve is interpolated every 4 points in order to speed-up
  // drawing.
  for (int i=psa.length-1;i>=0;i-=4) {
    p=psa[i];
    curveVertex(p.x,p.y);
  }
  endShape();
  stroke(255);
  strokeWeight(2);
  line(-300,230-h*70,300,230-h*70);
  popMatrix();
  text("Points: "+ psa.length + " Springs: "+lsa.length,10,height-49);
  text("Steps: "+ steps,10,height-36);
  text("Total Loops: "+ steps*(psa.length+lsa.length),10,height-23);
  text("Solver Time: "+ (int)(msec*steps) + "msec",10,height-10);
}


