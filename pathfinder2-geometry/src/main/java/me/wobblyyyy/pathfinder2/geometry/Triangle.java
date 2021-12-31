/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.geometry;

import me.wobblyyyy.pathfinder2.math.Average;

/**
 * A polygon with 3 sides and 3 vertices.
 *
 * <p>
 * Update: this is about... I'd say 2 months, possibly, after I've written this
 * code. And I would like say that this is some very respectful documentation.
 * This must have been written by a charming young gentleman. It's so polite.
 * "A polygon with 3 sides and 3 vertices." I can just imagine a young
 * gentleman in a tuxedo saying those words in a respectful tone. Anyways.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class Triangle implements Shape<Triangle> {
    private final PointXY a;
    private final PointXY b;
    private final PointXY c;

    private final PointXY center;

    private final Line ab;
    private final Line bc;
    private final Line ca;

    /**
     * Create a new {@code Triangle} from three points.
     *
     * @param a one of the triangle's vertices.
     * @param b one of the triangle's vertices.
     * @param c one of the triangle's vertices.
     */
    public Triangle(PointXY a,
                    PointXY b,
                    PointXY c) {
        PointXY.checkArgument(a);
        PointXY.checkArgument(b);
        PointXY.checkArgument(c);

        if (PointXY.areDuplicatesPresent(a, b, c)) {
            throw new IllegalArgumentException(
                    "Cannot create a triangle with duplicate points! All three " +
                            "of the triangle's points must be unique."
            );
        }

        this.a = a;
        this.b = b;
        this.c = c;

        this.ab = new Line(a, b);
        this.bc = new Line(b, c);
        this.ca = new Line(c, a);

        this.center = getCentroid(ab, bc, ca);
    }

    /**
     * Get a given triangle's "centroid." The centroid is basically
     * the triangle's real center point.
     *
     * @param ab one of the three lines.
     * @param bc one of the three lines.
     * @param ca one of the three lines.
     * @return the triangle's "centroid." If there is an issue computing the
     * centroid (which shouldn't ever happen) return null.
     */
    public static PointXY getCentroid(Line ab,
                                      Line bc,
                                      Line ca) {
        /*
         * https://tutors.com/math-tutors/geometry-help/how-to-find-centroid-of-a-triangle-definition-formula
         * ^ very simple explanation of what's going on ^
         *
         *        B
         *        #
         *       # #
         *      #   #
         *     #     #
         *  A ######### C
         *
         * A opposes BC
         * B opposes CA
         * C opposes AB
         *
         * the centroid (the triangle's real center) is right in the middle
         * of that triangle. averaging the points doesn't return an accurate
         * center value, so we need to do some of this math instead. it's not
         * hard, but it can be kind of hard to visualize
         */

        PointXY abMidpoint = ab.midpoint();
        PointXY bcMidpoint = bc.midpoint();
        PointXY caMidpoint = ca.midpoint();

        Line line1 = new Line(ab.getStart(), bcMidpoint);
        Line line2 = new Line(bc.getStart(), caMidpoint);
        Line line3 = new Line(ca.getStart(), abMidpoint);

        PointXY intersection1_2 = line1.getIntersectionWith(line2);
        PointXY intersection2_3 = line2.getIntersectionWith(line3);
        PointXY intersection3_1 = line3.getIntersectionWith(line1);

        // if any of the intersections are null (meaning there's no
        // intersection) return null - this shouldn't ever happen!
        if (
                intersection1_2 == null ||
                        intersection2_3 == null ||
                        intersection3_1 == null
        ) {
            return null;
        }

        return Average.of(
                intersection1_2,
                intersection2_3,
                intersection3_1
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointXY getClosestPoint(PointXY reference) {
        PointXY.checkArgument(reference);

        if (reference.isInside(this)) {
            return reference;
        }

        PointXY abClosest = ab.getClosestPoint(reference);
        PointXY bcClosest = bc.getClosestPoint(reference);
        PointXY caClosest = ca.getClosestPoint(reference);

        return PointXY.getClosestPoint(
                reference,
                abClosest,
                bcClosest,
                caClosest
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPointInShape(PointXY reference) {
        PointXY.checkArgument(reference);

        PointXY target = center;

        if (reference.isNear(center, 0.01)) {
            target = ab.midpoint();
        }

        Line line = new Line(
                reference,
                reference.inDirection(
                        ab.length() + bc.length() + ca.length(),
                        reference.angleTo(target)
                )
        );

        return Shape.doesIntersectOdd(
                line,
                ab,
                bc,
                ca
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesCollideWith(Shape<?> shape) {
        return shape.getClosestPoint(center).isInside(this);
    }

    @Override
    public PointXY getCenter() {
        return center;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Triangle rotate(Angle rotation) {
        return rotate(rotation, center);
    }

    @Override
    public Triangle rotate(Angle rotation, PointXY centerOfRotation) {
        PointXY rotatedA = a.rotate(centerOfRotation, rotation);
        PointXY rotatedB = b.rotate(centerOfRotation, rotation);
        PointXY rotatedC = c.rotate(centerOfRotation, rotation);

        return new Triangle(
                rotatedA,
                rotatedB,
                rotatedC
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Triangle shift(double shiftX,
                          double shiftY) {
        PointXY shift = new PointXY(shiftX, shiftY);

        PointXY adjustedA = a.add(shift);
        PointXY adjustedB = a.add(shift);
        PointXY adjustedC = a.add(shift);

        return new Triangle(
                adjustedA,
                adjustedB,
                adjustedC
        );
    }

    @Override
    public Triangle moveTo(PointXY newCenter) {
        double dx = center.distanceX(newCenter);
        double dy = center.distanceY(newCenter);

        return shift(dx, dy);
    }

    private PointXY redrawPoint(PointXY point,
                                double scale) {
        Angle fromCenter = center.angleTo(point);
        double originalDistance = center.distance(point);

        return center.inDirection(
                scale * originalDistance,
                fromCenter
        );
    }

    private PointXY addDistance(PointXY point,
                                double distance) {
        Angle fromCenter = center.angleTo(point);
        double originalDistance = center.distance(point);

        return center.inDirection(
                originalDistance + distance,
                fromCenter
        );
    }

    @Override
    public Triangle scale(double scale) {
        PointXY newA = redrawPoint(a, scale);
        PointXY newB = redrawPoint(b, scale);
        PointXY newC = redrawPoint(c, scale);

        return new Triangle(newA, newB, newC);
    }

    @Override
    public Triangle growBy(double growth) {
        PointXY newA = addDistance(a, growth);
        PointXY newB = addDistance(b, growth);
        PointXY newC = addDistance(c, growth);

        return new Triangle(newA, newB, newC);
    }

    public PointXY getA() {
        return a;
    }

    public PointXY getB() {
        return b;
    }

    public PointXY getC() {
        return c;
    }

    public Line getAB() {
        return ab;
    }

    public Line getBC() {
        return bc;
    }

    public Line getCA() {
        return ca;
    }
}
