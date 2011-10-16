import peasy.org.apache.commons.math.*;
import peasy.*;
import peasy.org.apache.commons.math.geometry.*;
import volatileprototypes.fvlib.*;
import processing.opengl.*;

Point[] psa;
Link[] lsa;
IntegratorVerlet vi;
BehaviorSpringRelaxation rs;
BehaviorConstantForce cf;
PeasyCam cam;

int steps=80;
int it=0;
float msec=0;
PFont font;
float strokew=3;

void setup() {
  ArrayList<Point> ps=new ArrayList();
  ArrayList<Link> ls=new ArrayList();
  size(640,600,OPENGL);
  cam = new PeasyCam(this, 0,0,0,1050);       // Camera Setup
  cam.setMinimumDistance(100);
  cam.setMaximumDistance(1500);
  
  // Construct some random points.
  for (int i=0;i<2000;i++) {
    Point p=new Point(random(width)-width*.5,random(height)-height*.5,random(width)-width*.5,1,.09,200);
    ps.add(p);
  }
  Point p=new Point(random(width)-width*.5,random(height)-height*.5,random(width)-width*.5,1,.09,200);
  p.setU(true);
  ps.add(p);
  
  
  for (Point p1 : ps) {
    float nl=0;
    for (Point p2 : ps) {
      if (p1 != p2) {
        PVector pv = p2.get();
        pv.sub(p1);
        if (pv.mag() < 100 && nl < 6) {
          ls.add(new Link(p1,p2,.002));
          nl++;
        }
      }
    }
  }
  
  vi=new IntegratorVerlet(ps);
  rs=new BehaviorSpringRelaxation(ls).setFast(true);
  cf=new BehaviorConstantForce(ps, new PVector(0,.001,0));
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
    it++;
    msec*=.998;
    long start=System.currentTimeMillis(); //Benchmark
    rs.step();
    cf.step();
    vi.step();
    msec+=(float)(System.currentTimeMillis()-start)*.002; //Benchmark
  }
  
  fill(255);
  stroke(255,30);
  strokeWeight(1);
  for (Link l : lsa) {
    line(l.p1.x,l.p1.y,l.p1.z,l.p2.x,l.p2.y,l.p2.z);
  }
  stroke(255,70);
  strokeWeight(4);
  beginShape(POINTS);
  for (Point pt : psa) {
    vertex(pt.x,pt.y,pt.z);
  }
  endShape();
  cam.beginHUD();
  text("Points: "+ psa.length + " Springs: "+lsa.length,10,height-49);
  text("Steps: "+ steps,10,height-36);
  text("Total Loops: "+ steps*(psa.length+lsa.length),10,height-23);
  text("Solver Time: "+ (int)(msec*steps) + "msec",10,height-10);
  cam.endHUD();
}
