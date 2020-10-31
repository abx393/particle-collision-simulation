public class Particle {
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
