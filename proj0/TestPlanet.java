import java.math.*;

public class TestPlanet {
    
    public static void main(String[] args) {
        checkPlanet();
    }

    private static void checkEquals(double expected, double actual, String label, double eps) {
        if (Math.abs(expected - actual) <= eps * Math.max(expected, actual)) {
            System.out.println("PASS: " + label + ": Expected " + expected + " and you gave " + actual);
        } else {
            System.out.println("FAIL: " + label + ": Expected " + expected + " and you gave " + actual);
        }
    }

    private static void checkPlanet() {
        System.out.println("Checking planet...");

        Planet p1 = new Planet(1.0, 1.0, 3.0, 4.0, 5.0, "jupiter.gif");
        Planet p2 = new Planet(2.0, 1.0, 3.0, 4.0, 4e11, "jupiter.gif");
        Planet p3 = new Planet(4.0, 5.0, 3.0, 4.0, 5.0, "jupiter.gif");
        Planet p4 = new Planet(3.0, 2.0, 3.0, 4.0, 5.0, "jupiter.gif");

        Planet[] planets = new Planet[]{p1, p2, p3, p4};

        double xNetForce = p1.calcNetForceExertedByX(planets);
        double yNetForce = p1.calcNetForceExertedByY(planets);

        p1.update(2.0, xNetForce, yNetForce);

        checkEquals(56.4, p1.xxVel, "xxVel", 0.01);
        checkEquals(4.0, p1.yyVel, "yyVel", 0.01);
        checkEquals(113.7, p1.xxPos, "xxPos", 0.01);
        checkEquals(9.0, p1.yyPos, "yyPos", 0.01);

    }
}
