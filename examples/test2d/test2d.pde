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
        gravity.step();
        // Also check for crossing of window boundaries
        for (Point p : ps) {
                if (p.x < 0) p.sforce.x -= p.x;
                if (p.x > width) p.sforce.x -= (p.x - width);
                if (p.y < 0) p.sforce.y -= p.y;
                if (p.y > height) p.sforce.y -= (p.y - height);
        }
	vi.step();

        // Drawing part
	background(245);
	fill(40);
  	for (Point p : ps) {
    		ellipse(p.x, p.y, 5, 5);
  	}
}
