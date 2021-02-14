import java.awt.*;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel {
	private final static double HZ = 0.5; // Frequency of redraw events
	private final static int NUM_PARTICLES = 100;
    private final static double PARTICLE_DENSITY = 0.8;

	private Particle[] particles;
	private Graphics g2 = (Graphics2D) this.getGraphics();
    private double t  = 0.0; // Simulation clock time
	private Image image = null;

	public World(Particle[] particles) {
		this.particles = particles.clone();
	}

	public World(int width, int height) {
		this.particles = new Particle[NUM_PARTICLES];
        this.setSize(height, width);

        // Create n random particles
        for (int i = 0; i < NUM_PARTICLES; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            double randSize = Math.random() * 9000 / NUM_PARTICLES + 15;
            this.particles[i] = new Particle(x, y, randSize, PARTICLE_DENSITY, this);
        }
    }

    @Override
    public void paint(Graphics g) {
		int width  = getSize().width;
		int height = getSize().height;

	    if (image == null || image.getWidth(null) != width || image.getHeight(null) != height) {
	    	image = createImage(width, height);
	    }

	    // Fill image background and spin through objects in the world to draw them over the  background
	    image.getGraphics().fillRect(0, 0, getWidth(), getHeight());

	    for (Particle particle : this.particles) {
	    	Graphics g2 = image.getGraphics();
	    	Random r = new Random();
	    	g2.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			particle.paint(g2);
		}

		g.drawImage(image, 0, 0, null);

		this.update();
	}

	public void update() {
		for (int i = 0; i < particles.length; i++) {
			particles[i].update(1);
		}

		for (int i = 0; i < particles.length; i++) {
			for (int j = 0; j < particles.length; j++) {
                if (i != j) {
                    Particle PI = particles[i];
                    Particle PJ = particles[j];
                    Point p1 = new Point((int) PI.getX(), (int) PI.getY());
                    Point p2 = new Point((int) PJ.getX(), (int) PJ.getY());
                    Point center1 = new Point ((int) (p1.getX() + PI.getSize() / 2.0), (int) (p1.getY() + PI.getSize() / 2.0));
                    Point center2 = new Point ((int) (p2.getX() + PJ.getSize() / 2.0), (int) (p2.getY() + PJ.getSize() / 2.0));

                    double dist = p1.distance(p2);

                    if (dist < PI.getSize() / 2.0 + PJ.getSize() / 2.0) {
                        double dx = p2.getX() - p1.getX();
                        double dy = p2.getY() - p1.getY();

                        double addDist = PI.getSize() / 2 + PJ.getSize() / 2 - dist;

                        PJ.setX(PJ.getX() + dx / dist * addDist);
                        PJ.setY(PJ.getY() + dy / dist * addDist);
                        PI.bounceOff(PJ);
                    }
                }
			}
		}
		this.repaint();
	}

	public void addAllParticles(Particle[] p) {
		this.particles = p;
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
     	        JFrame frame = new JFrame();

     	        // Make a "full screen" window
     	        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     	        int width = (int) d.getWidth() + 10;
     	        int height = (int) d.getHeight() - 40;
     			frame.setSize(d);
    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    			frame.setTitle("Particle Collision Simulation");
    			frame.setVisible(true);

                World world = new World(frame.getHeight(), frame.getWidth());
    			world.setVisible(true);
    			world.setBackground(Color.white);

    			frame.add(world);
            }
        });
	}
}
