package org.traud.math.geom;


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * Created by traud on 7/6/2017.
 */
public class GeometryUtils {

    public static Point2D.Double[] circleIntersection(
            Point2D.Double p0, double r0,
            Point2D.Double p1, double r1)
    {
        double d = p0.distance(p1);
        if (r0 + r1 < d)
            return new Point2D.Double[0];

        double dx = p1.getX() - p0.getX();
        double dy = p1.getY() - p0.getY();

        // TODO: allow for _some_ error in this check?
        if (r0 + r1 == d) {
            Point2D.Double[] r = new Point2D.Double[1];

            double f1 = r0 / d;
            r[0] = new Point2D.Double(
                    p0.getX() +  f1 * dx,
                    p0.getY() +  f1 * dy
            );
            return r;
        }
        // see https://stackoverflow.com/questions/3349125/circle-circle-intersection-points

        double a = (r0*r0 - r1*r1 + d*d ) / (2*d);
        Point2D.Double p2 = new Point2D.Double(p0.getX() + a * dx / d, p0.getY() + a * dy / d);
        Point2D.Double[] r = new Point2D.Double[2];
        double h = Math.sqrt(r0*r0 - a*a);
        r[0] = new Point2D.Double(p2.getX() + h * dy / d, p2.getY() - h * dx / d);
        r[1] = new Point2D.Double(p2.getX() - h * dy / d, p2.getY() + h * dx / d);
        return r;
    }


    public static Point2D.Double[] lineCircleIntersection(
            Line2D.Double l,
            Point2D.Double p0, double r0)
    {
        double d = l.ptLineDist(p0);
        if (r0 < d)
            return new Point2D.Double[0];

        // TODO: allow for _some_ error in this check?
        if (r0  == d) {
            // TODO: implement this
            Point2D.Double[] r = new Point2D.Double[1];
            return r;
        }
        // see https://stackoverflow.com/questions/3349125/circle-circle-intersection-points

        Point2D.Double[] r = new Point2D.Double[2];
        r[0] = new Point2D.Double();
        r[1] = new Point2D.Double();
        return r;
    }




    public static void main(String[] args) {
        Point2D.Double c1 = new Point2D.Double(0, 0);
        Point2D.Double c2 = new Point2D.Double(10, 0);

        System.out.printf("intersection: %s\n", Arrays.toString(circleIntersection(c1, 2, c2, 5)));
        System.out.printf("intersection: %s\n", Arrays.toString(circleIntersection(c1, 5, c2, 5)));
        System.out.printf("intersection: %s\n", Arrays.toString(circleIntersection(c1, 3, c2, 7)));
        System.out.printf("intersection: %s\n", Arrays.toString(circleIntersection(c1, 1, c2, 9)));

        System.out.printf("intersection: %s\n", Arrays.toString(circleIntersection(c1, 2, c2, 9)));
    }
}
