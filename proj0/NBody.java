public class NBody {
    
    public static double readRadius(String filename) {
        In in = new In(filename);
        int number = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        int number = in.readInt();
        double radius = in.readDouble();
        Planet[] planets = new Planet[number];
        for (int i=0; i<number; i++) {
            planets[i] = new Planet(
                in.readDouble(), in.readDouble(),
                in.readDouble(), in.readDouble(),
                in.readDouble(), in.readString()
                );
        }
        return planets;
    }

    public static void main(String[] args) {

        // StdAudio.play("audio/2001.mid");
        
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        
        StdDraw.setScale(-radius, radius);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg", radius*2, radius*2);
        for (Planet p: planets) {
            p.draw();
        }
        StdDraw.show();

        double t = 0;
        int planetCount = planets.length;
        while (t <= T) {
            double[] xForces = new double[planetCount];
            double[] yForces = new double[planetCount];
            for (int i = 0; i < planetCount; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < planetCount; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg", radius*2, radius*2);
            for (Planet p: planets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            
            t += dt;
        }

        StdOut.printf("%d\n", planetCount);
        StdOut.printf("%.2e\n", radius);
        for (Planet p: planets) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
        }

    }

}
