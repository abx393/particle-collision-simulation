import java.awt.*;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import CollisionSystem.Event;

public class World extends JPanel{
	private final static double HZ = 0.5; //number of redraw events 
	private final static int numParticles = 100;
	public Particle[] particles;
	private Graphics g2 = (Graphics2D) this.getGraphics();
    private double t  = 0.0;          // simulation clock time
	private Image image = null;

	public World(Particle[] particles){
		this.particles = particles.clone();
	}

	public World(){
		this.particles = new Particle[numParticles];
	}
	
	public void paint(Graphics g){
		int width  = getSize().width;
		int height = getSize().height;
		
	    if (image == null || image.getWidth(null) != width || image.getHeight(null) != height) {
	    	image = createImage(width, height);
	    }

	    // fill image background and spin through objects
	    // in the world to draw them over the  background
	    image.getGraphics().fillRect(0, 0, getWidth(), getHeight());
	    
	    for (Particle p : this.particles) {
	    	Graphics g2 = image.getGraphics();
	    	Random r = new Random();
	    	g2.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			p.paint(g2);
		}
	    
		// take image we just created and  draw  it  onto
	    // the frame as  a  poor  mans  double  buffering
		g.drawImage(image, 0, 0, null);
		
		this.update();
	}

	public void update(){
		for (int i = 0; i < particles.length; i++){
			particles[i].update(1);
		}
		
		for (int i = 0; i < particles.length; i++){
			for (int j = 0; j < particles.length; j++){
				
				if (i == j) continue;
				Particle PI = particles[i];
				Particle PJ = particles[j];
				Point p1 = new Point((int) PI.getX(), (int) PI.getY());
				Point p2 = new Point((int) PJ.getX(), (int) PJ.getY());
				//System.out.println("p1=" + p1 + "\np2=" + p2);
				Point center1 = new Point ((int) (p1.getX() + PI.getSize() / 2.0), (int) (p1.getY() + PI.getSize() / 2.0));
				Point center2 = new Point ((int) (p2.getX() + PJ.getSize() / 2.0), (int) (p2.getY() + PJ.getSize() / 2.0));

				//System.out.println("c1=" + center1 + "\nc2" + center2);
				double dist = p1.distance(p2);
				
				if (dist < PI.getSize() / 2.0 + PJ.getSize() / 2.0){
					double dx = p2.getX() - p1.getX();
					double dy = p2.getY() - p1.getY();
					
					//double angle = Math.asin(dx/p1.distance(p2));
					double addDist = PI.getSize() / 2 + PJ.getSize() / 2 - dist;
					
					PJ.setX(PJ.getX() + dx / dist * addDist);
					PJ.setY(PJ.getY() + dy / dist * addDist);
				
					PI.bounceOff(PJ);
				}
			}
		}
		this.repaint();
	}

	public void addAllParticles(Particle[] p){
		this.particles = p;
	}
	
	public static void main(String[] args){
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	World world = new World();
     	        JFrame frame = new JFrame();
     	        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     			frame.setSize((int) (d.getWidth()+10),(int) (d.getHeight()-40));
    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    			frame.setTitle("Particle Collision Simulation");
    			frame.setVisible(true);
    			//frame.setBackground(Color.black);
    			world.setSize(300000, 99999);
    			world.setVisible(true);
    			
    			world.setBackground(Color.white);

       	        // create n random particles
    	        int k = 0;
    	        for (int i = 1; i <= numParticles; i++){
    	        	int x = (int) (Math.random() * frame.getWidth()); //(int) (50+i*frame.getWidth()/15.0+ Math.random()*50-25);
    	        	int y = (int) (Math.random() * frame.getHeight()); //(int) (50 + j*frame.getHeight()/15.0 + Math.random()*50-25);
    	       		world.particles[k] = new Particle(
    	       									x, 
    	       									y, 
    	       									Math.random() * 9000 / numParticles + 15, 
    	       									0.8, 
    	       									world);
    	       		k++;
    	        }
    			frame.add(world);
            }
        });
	}
}