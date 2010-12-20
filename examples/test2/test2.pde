import fvlib.*;
import processing.opengl.*;

Point[] psa;
Link[] lsa;
IntegratorVerlet vi;
SolverRelaxation rs;
SolverStaticSingle sss;

int steps=15;
int it=0;
float msec=0;
PFont font;
float strokew=3;

void setup() {
  ArrayList ps=new ArrayList();
  ArrayList ls=new ArrayList();
  size(600,600,OPENGL);
  
  // Construct the curve all at once in the beginning.
  float tx=0,ty=299;
  for (int i=0;i<240;i++) {
    tx=max(-300,min(300,tx+random(2.5)-1.25));
    ty-=2.5;
    Point p=new Point(tx,ty,0,1,.03,200);
    ps.add(p);
    int s=ps.size();
    for (int j=max(0,s-8);j<s-1;j++) {
      Point p1=(Point)ps.get(j);
      PVector pv=p.get();
      pv.sub(p1);
      ls.add(new Link(p,p1,.25));
    }
  }

  vi=new IntegratorVerlet(ps);
  rs=new SolverRelaxation(ls).setFast(true);
  sss=new SolverStaticSingle(ps).setBias(1).setRange(300).setInv(false);
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
  
  Link s;
    float sl;
    for (int i=0, j=lsa.length;i<j;i++) {
      s=lsa[i];
      sl=s.getC();
      if (sl<600) {
        s.setC(sl*1.003);
      }
    }
    strokew*=1.003;
  Point p;
  for (int j=steps;j>=0;j--) {
    it++;
    for (int i=psa.length-1;i>=0;i--) {
      p=psa[i];
      p.y=max(p.y,-300);
      p.y=min(p.y,300);
      p.x=min(p.x,300);
      p.x=max(p.x,-300);
    }
    
    msec*=.995;
    long start=System.currentTimeMillis(); //Benchmark
    vi.step();
    rs.step();
    sss.step();
    msec+=(float)(System.currentTimeMillis()-start)*.005; //Benchmark
  }
  pushMatrix();
  translate(300,300);
  noFill();
  stroke(255,50);
  strokeWeight(strokew);
  beginShape();
  // Curve is interpolated every 4 points in order to speed-up
  // drawing.
  for (int i=psa.length-1;i>=0;i-=4) {
    p=psa[i];
    curveVertex(p.x,p.y);
  }
  endShape();
  popMatrix();
  text("Points: "+ psa.length + " Springs: "+lsa.length,10,height-62);
  text("Points*Points: "+ psa.length*psa.length,10,height-49);
  text("Steps: "+ steps,10,height-36);
  text("Total Loops: "+ steps*(psa.length+lsa.length+psa.length*psa.length),10,height-23);
  text("Solver Time: "+ (int)(msec*steps) + "msec",10,height-10);
}


