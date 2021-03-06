public class Planet {

    private static final double G = 6.67e-11;

    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        return Math.sqrt((this.xxPos-p.xxPos)*(this.xxPos-p.xxPos) + (this.yyPos-p.yyPos)*(this.yyPos-p.yyPos));
    }
    
    public double calcForceExertedBy(Planet p) {
        double r = this.calcDistance(p);
        return (G * this.mass * p.mass) / (r * r);
    }

    public double calcForceExertedByX(Planet p) {
        return this.calcForceExertedBy(p) * (p.xxPos-this.xxPos) / this.calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        return this.calcForceExertedBy(p) * (p.yyPos-this.yyPos) / this.calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] ps) {
        double f = 0;
        for (Planet p: ps) {
            if (this.equals(p)) {
                continue;
            }
            f += calcForceExertedByX(p);
        }
        return f;
    }

    public double calcNetForceExertedByY(Planet[] ps) {
        double f = 0;
        for (Planet p: ps) {
            if (this.equals(p)) {
                continue;
            }
            f += calcForceExertedByY(p);
        }
        return f;
    }

    public void update(double dt, double fX, double fY) {
        double aX = fX / this.mass;
        double aY = fY / this.mass;
        this.xxVel += dt * aX;
        this.yyVel += dt * aY;
        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;
    }

    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "images/"+this.imgFileName);
    }
}