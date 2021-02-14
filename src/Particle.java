
import java.awt.*;
import java.util.Random;

public class Particle {
	private World world;
	private Graphics g2;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private double size;
	private final double density;
	private final double mass;
	private int collisionCount;
	private String element;

	public Particle(double x, double y, double size, double density, World world) {
		this.world = world;
		this.g2 = (Graphics2D) world.getGraphics();
		this.x = x;
		this.y = y;
		this.size = size;
		this.dx = Math.random() * 2 - 2;
		this.dy = Math.random() * 2 - 2;
		this.density = density;
		this.mass = size * density;
		this.element = new String[] {"H","O", "He", "N"}[new Random().nextInt(4)];
	}

	public Particle(World world) {
		this(Math.random() * world.getWidth(),
             Math.random() * world.getHeight(),
			 20.0,
			 0.8,
			 world);
	}

	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.white);

		g.fillOval((int) (x - this.getSize() / 2),
                   (int) (y - this.getSize() / 2),
                   (int) size,
                   (int) size);
		g.setColor(c);

	}

	// Updates the position of this particle, given the change in time since last update
	public void update(double dt) {
		if (this.x - this.size / 2 <= 0) {
			this.x = this.size / 2;
			dx *= -1;
		} else if (this.x + this.size / 2 >= world.getWidth()) {
			this.x = world.getWidth() - this.size/2;
			dx *= -1;
		} else if (this.y - this.size/2 <= 0){
			this.y = this.size / 2;
			dy *= -1;
		} else if (this.y + this.size / 2 >= world.getHeight()) {
			this.y = this.world.getHeight() - this.size / 2;
			dy *= -1;
		}

		x += dx * dt;
		y += dy * dt;
		paint(world.getGraphics());
	}

	// Returns the number of collisions this particle has had thus far with any of the four walls
	// or with other particles
	public int collisionCount() {
		return this.collisionCount;
	}

    public boolean isColliding(Particle other) {
        double dx = Math.abs(this.x - other.x);
        double dy = Math.abs(this.y - other.y);

        return Math.sqrt(dx * dx + dy * dy) < this.size / 2 + other.size / 2;
    }

	//

    /**
     * Returns the time it would take for this Particle to hit that Particle
     *
     * @param that other Particle with which this Particle will collide
     * @return the time it would take for this Particle to hit the given Particle
     */
	public double timeToHit(Particle that) {
		if (this == that) return Double.POSITIVE_INFINITY;
        double dx  = that.x - this.x;
        double dy  = that.y - this.y;
        double ddx = that.dx - this.dx;
        double ddy = that.dy - this.dy;
        double dvdr = dx * ddx + dy * ddy;

        if (dvdr > 0) return Double.POSITIVE_INFINITY;
        double dvdv = ddx * ddx + ddy * ddy;
        double drdr = dx * dx + dy * dy;
        double sigma = this.size + that.getSize();
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);

        if (d < 0) return Double.POSITIVE_INFINITY;
        return -(dvdr + Math.sqrt(d)) / dvdv;
	}

    /**
     * Returns the time for this particle to hit top or bottom wall.
     *
     * @return the time it would take for this particle to hit top or bottom wall
     */
    public double timeToHitVerticalWall() {
        if (dx > 0) {
        	return (1.0 - dx - size) / dx;
        } else if (dx < 0) {
        	return (size - dx) / dx;
        } else {
        	return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Returns the time it would take for this particle to hit left or right wall
     *
     * @return the time it would take for this particle to hit left or right wall
     */
    public double timeToHitHorizontalWall() {
        if (dy > 0) {
        	return (1.0 - y - size) / dy;
        } else if (dy < 0) {
        	return (size - y) / dy;
        } else {
        	return Double.POSITIVE_INFINITY;
        }
    }

    public void bounceOff(Particle that) {
        double dx  = that.x - this.x;
        double dy  = that.y - this.y;
        double ddx = that.dx - this.dx;
        double ddy = that.dy - this.dy;
        double dvdr = dx*ddx + dy*ddy; // dv dot dr

        // Distance between particle centers at collison
        double dist = this.size / 2.0 + that.size / 2.0;

        // Magnitude of normal force
        double magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);

        // Normal force, and in x and y directions
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;

        // Update velocities according to normal force
        this.dx += fx / this.getMass();
        this.dy += fy / this.getMass();
        that.dx -= fx / that.getMass();
        that.dy -= fy / that.getMass();

        // Update collision counts
        this.collisionCount++;
        that.collisionCount++;
    }

    // Updates the velocity of this particle upon collision with either top or bottom wall
    public void bounceOffVerticalWall() {
        this.dx *= -1;
        this.collisionCount++;
    }

    // Updates the velocity of this particle upon collision with either left or right wall
    public void bounceOffHorizontalWall() {
        this.dy *= -1;
        this.collisionCount++;
    }

    // Returns the kinetic energy of this particle
    public double kineticEnergy() {
        return 0.5 * this.mass * (this.dx * this.dx + this.dy * this.dy);
    }

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDX() {
		return dx;
	}

	public double getDY() {
		return dy;
	}

	public void setDX(double dx) {
		this.dx = dx;
	}

	public void setDY(double dy) {
		this.dy = dy;
	}

	public double getSize() {
		return this.size;
	}

	public double getMass() {
		return mass;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public void setWorld(World w) {
		this.world = w;
	}
}
