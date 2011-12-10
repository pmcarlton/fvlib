import volatileprototypes.fvlib.*;
import processing.opengl.*;

IntegratorVerlet vi;
BehaviorConstantDistance repel;
BehaviorConstantForce gravity;

ArrayList<Point> ps=new ArrayList();

void setup() {
  	size(640,600,OPENGL);
  	for (int i=0; i< width; i+= 20) {
		for (int j=300; j< height; j+= 20) {
			ps.add(new Point(i + random(3),j + random(3),0));
		}
	}
  
  	vi=new IntegratorVerlet(ps);
	// Same restlength and range means points will only repel.
  	repel=new BehaviorConstantDistance(ps).setC(20).setRange(20);
        gravity = new BehaviorConstantForce(ps, new PVector(0,0.01,0));
}

void draw() {
        // Simulation part
        repel.step();
        //StepDist();
        gravity.step();
        // Also check for crossing of window boundaries
        for (Point p : ps) {
                if (p.x < 0) p.sforce.x -= p.x;
                if (p.x > width) p.sforce.x -= (p.x - width);
                if (p.y < 0) p.sforce.y -= p.y;
                if (p.y > height) p.sforce.y -= (p.y - height);
                p.sforce.z = 0;
                p.z = 0;
        }
	vi.step();

        // Drawing part
	background(245);
	fill(40);
  	for (Point p : ps) {
    		ellipse(p.x, p.y, 5, 5);
  	}
}

void StepDist() {
    float R2 = 20*20;
    float C = 20;
    float S = 0.1;
    float d;
    float L2;
    Point p1,p2;
    float dx,dy,dz,lx,ly,lz;
    for (int i=0,k=ps.size();i<k;i++) {
      p1=ps.get(i);
	  for (int j=i+1;j<k;j++) {
		p2=ps.get(j);
		dx=p2.x-p1.x;
		dy=p2.y-p1.y;
		dz=p2.z-p1.z;
		L2 = dx*dx+dy*dy+dz*dz;
		if (L2<R2) {
		  d = C + L2/C; 
		  d = (float)(d*.25 + L2/d);
		  d = (float)(S*(1-(C/d)));
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
    }
  }
