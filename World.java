/*
 * Created by: Abhinav Bandari
 * Project Description:
 * This is a particle collision simulation.
 * All collision are elastic (particles bounce off each other). 
 * There are two classes World and Particle. 
 * Each particle starts with a random mass and random x-velocity and random y-velocity.
 * For each frame of the simulation, the program checks if a particle has collided
 * with a vetical wall, a horizontal wall, or another particle. 
 * If so, the particle's velocity is adjusted accordingly.
 */

import java.awt.*;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel{
	private final static int numParticles = 100;
  
	public Particle[] particles;
	
	public World(Particle[] particles){
		this.particles = particles.clone();
	}

	public World(){
		this.particles = new Particle[numParticles];
	}
	
	private Image image = null;
	
	public void paint(Graphics g){
		int width  = getSize().width;
		int height = getSize().height;
		
	    if (image == null || image.getWidth(null) != width || image.getHeight(null) != height) {
	    	image = createImage(width, height);
	    }

	    // fill image background and spin through objects
	    // in the world to draw them over the  background
	    
	    image.getGraphics().fillRect(
	    	0, 0, getWidth(), getHeight()
	    );
	    
	    for (Particle p : this.particles) {
	    	Graphics g2 = image.getGraphics();
	    	Random r = new Random();
	    	g2.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			p.paint(g2);
		}
	    
		
		g.drawImage(image, 0, 0, null);
		
		this.update();
	}

	public void update(){
		for (int i=0; i<particles.length; i++){
			particles[i].update(0.25);
		}
		
		for (int i=0; i<particles.length; i++){
			for (int j=0; j<particles.length; j++){
				
				if (i==j) continue;
				Particle PI = particles[i];
				Particle PJ = particles[j];
				Point p1 = new Point((int) PI.getX(), (int) PI.getY());
				Point p2 = new Point((int) PJ.getX(), (int) PJ.getY());
				//System.out.println("p1=" + p1 + "\np2=" + p2);
				Point center1 = new Point ((int)(p1.getX()+PI.getSize()/2.0), (int) (p1.getY()+PI.getSize()/2.0));
				Point center2 = new Point ((int)(p2.getX()+PJ.getSize()/2.0), (int) (p2.getY()+PJ.getSize()/2.0));

				//System.out.println("c1=" + center1 + "\nc2" + center2);
				double dist = p1.distance(p2);
				
				if (dist<PI.getSize()/2.0+PJ.getSize()/2.0){
					double dx = p2.getX()-p1.getX();
					double dy = p2.getY()-p1.getY();
					
					//double angle = Math.asin(dx/p1.distance(p2));
					double addDist = PI.getSize()/2+PJ.getSize()/2-dist;
					
					PJ.setX(PJ.getX() + dx/dist *addDist);
					PJ.setY(PJ.getY()+dy/dist *addDist);
				
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
     			frame.setSize((int) (d.getWidth()+10),(int) (d.getHeight()-40)
     					);
    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    			frame.setTitle("Particle Collision Simulation");
    			frame.setVisible(true);
    			world.setSize(300000, 99999);
    			world.setVisible(true);
    			
    			world.setBackground(Color.black);
       	        // create n random particles
    	        int k = 0;
    	       
    	        for (int i=1; i<=numParticles; i++){        		
    	        	int x = (int) (Math.random() * frame.getWidth());//(int) (50+i*frame.getWidth()/15.0+ Math.random()*50-25);
    	        	int y = (int) (Math.random()*frame.getHeight());//(int) (50 + j*frame.getHeight()/15.0 + Math.random()*50-25);
    	       		world.particles[k] = new Particle(x, y, 
    	       				Math.random()*9000/(numParticles)+15, 0.8, world);		
    	       		k++;
    	        }
    	        
    			frame.add(world);
                
            }
        });
	}
}

class Particle {
	private World world;
	private Graphics g2;
	private double x, y;
	private double dx, dy;
	private double size;
	private final double density;
	private final double mass;
	private int count;
	private String element;
	
	public Particle(int x, int y, double size, double density, World world){
		this.world = world;
		g2 = (Graphics2D) world.getGraphics();
		
		this.x = x;
		this.y = y;
		this.size = size;
		dx = Math.random()*4-2;
		dy = Math.random()*4-2;
		this.density = density; // new Random().nextInt(200)
		this.mass = size* density;
		element = new String[] {"H","O", "He", "N"}[new Random().nextInt(4)];
	}

	public Particle() {
        x = Math.random() * world.getWidth();
        y = Math.random() * world.getHeight();
        dx = Math.random()*4 -2;
        dy = Math.random() * 4 - 2;
        size = 20;
        this.density = 0.8;
        this.mass = size*density;
    }

    // Given a graphics object, draws and renders this particle
	public void paint(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.white);

		g.fillOval((int)(x-this.getSize()/2), (int)(y-this.getSize()/2), (int) size, (int) size);
		//g.setColor(Color.white);
		//g.drawString(element,(int) (x+size/2),(int) (y+size/3*2));
		g.setColor(c);
		//update();

	}
	
    // Updates the (x, y) position of this particle
    public void update(double dt){
        if (this.x - this.size / 2 <= 0){
            this.x = this.size / 2;
            dx *= -1;
        } else if (this.x + this.size / 2 >= world.getWidth()){
            this.x = world.getWidth()-this.size/2;
            dx *= -1;
        } else if (this.y - this.size / 2 <= 0){
            this.y = this.size / 2;
            dy *= -1;
        } else if (this.y + this.size / 2 >= world.getHeight()) {
            this.y = this.world.getHeight() - this.size / 2;
            dy *= -1;
        }
        
        x+= dx * dt;
        y+= dy * dt;

        paint(world.getGraphics());
    }

    public int count(){
        return this.count;
    }

    // Updates the (x, y) position of two particles that collide
    public void bounceOff(Particle that) {
        double dx  = that.x - this.x;
        double dy  = that.y - this.y;
        double ddx = that.dx - this.dx;
        double ddy = that.dy - this.dy;
        double dvdr = dx*ddx + dy*ddy;             // dv dot dr
        double dist = this.size/2.0 + that.size/2.0;   // distance between particle centers at collison

        // magnitude of normal force
        double magnitude = 2*this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);

        // normal force, and in x and y directions
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;

        // update velocities according to normal force
        this.dx += 1.00 * fx / this.getMass();
        this.dy += 1.00 * fy / this.getMass();
        that.dx -= 1.00 * fx / that.getMass();
        that.dy -= 1.00 * fy / that.getMass();

        // update collision counts
        this.count++;
        that.count++;
    }

    // Updates the velocity of this particle upon collision with a vertical wall.
    public void bounceOffVerticalWall() {
        this.dx *= -1;
        count++;
    }

    // Updates the velocity of this particle upon collision with a horizontal wall.
    public void bounceOffHorizontalWall() {
        this.dy *= -1;
        count++;
    }

    // Returns the kinetic energy of this particle.
    // Kinetic energy is given by the formula E = 1/2 mv^2
    public double kineticEnergy() {
        return 0.5 * this.mass * (this.dx * this.dx + this.dy * this.dy);
    }
    
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }

    public double getDX(){
        return this.dx;
    }
    
    public double getDY(){
        return this.dy;
    }
    
    public void setDX(double dx){
        this.dx = dx;
    }
    
    public void setDY(double dy){
        this.dy = dy;
    }
    
    public double getSize(){
        return this.size;
    }
    
    public double getMass(){
        return this.mass;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setSize(double size){
        this.size = size;
    }

    public void setWorld(World w){
        this.world = w;
    }
}
