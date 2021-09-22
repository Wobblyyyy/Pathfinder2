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

import java.util.ArrayList;
import java.util.List;

/**
 * Class for dealing with rectangles. There is only one constructor for this
 * class, and it accepts a start point and an end point. Based on these
 * two points, the side lengths will be determined, and lines created. By
 * default, rectangles will face "upwards," but you can change this by using
 * the {@link #rotate(PointXY, Angle)} method.
 *
 * @author Colin Robertson
 * @see #rotate(PointXY, Angle)
 * @since 0.0.0
 */
public class Rectangle implements Shape {
    private final PointXY startPoint;
    private final PointXY endPoint;
    private final PointXY center;
    private final double startPointX;
    private final double startPointY;
    private final double endPointX;
    private final double endPointY;
    private final PointXY aPoint;
    private final PointXY bPoint;
    private final PointXY cPoint;
    private final PointXY dPoint;
    private final Line abLine;
    private final Line bcLine;
    private final Line cdLine;
    private final Line daLine;
    private final List<Line> lines = new ArrayList<>(4);

    private Rectangle(PointXY a,
                      PointXY b,
                      PointXY c,
                      PointXY d) {
        this.startPoint = a;
        this.endPoint = c;
        this.center = PointXY.avg(a, b, c, d);

        this.startPointX = startPoint.x();
        this.startPointY = startPoint.y();
        this.endPointX = endPoint.x();
        this.endPointY = endPoint.y();

        aPoint = a;
        bPoint = b;
        cPoint = c;
        dPoint = d;

        abLine = new Line(aPoint, bPoint);
        bcLine = new Line(bPoint, cPoint);
        cdLine = new Line(cPoint, dPoint);
        daLine = new Line(dPoint, aPoint);

        lines.add(abLine);
        lines.add(bcLine);
        lines.add(cdLine);
        lines.add(daLine);
    }

    /**
     * Create a new {@code Rectangle}. This rectangle will have sides
     * parallel to the X and Y axes. This can be rotated using the
     * {@link #rotate(PointXY, Angle)} and {@link #rotateAroundCenter(Angle)}
     * methods provided.
     *
     * @param startPoint the rectangle's start point. Typically, this is
     *                   the smaller of the two points.
     * @param endPoint   the rectangle's end point. Typically, this is
     *                   the larger of the two points.
     */
    public Rectangle(PointXY startPoint,
                     PointXY endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.center = PointXY.avg(startPoint, endPoint);

        this.startPointX = startPoint.x();
        this.startPointY = startPoint.y();
        this.endPointX = endPoint.x();
        this.endPointY = endPoint.y();

        aPoint = new PointXY(
                startPointX,
                startPointY
        );
        bPoint = new PointXY(
                startPointX,
                endPointY
        );
        cPoint = new PointXY(
                endPointX,
                endPointY
        );
        dPoint = new PointXY(
                endPointX,
                startPointY
        );

        abLine = new Line(aPoint, bPoint);
        bcLine = new Line(bPoint, cPoint);
        cdLine = new Line(cPoint, dPoint);
        daLine = new Line(dPoint, aPoint);

        lines.add(abLine);
        lines.add(bcLine);
        lines.add(cdLine);
        lines.add(daLine);
    }

    public PointXY getStartPoint() {
        return startPoint;
    }

    public PointXY getEndPoint() {
        return endPoint;
    }

    public double getStartPointX() {
        return startPointX;
    }

    public double getStartPointY() {
        return startPointY;
    }

    public double getEndPointX() {
        return endPointX;
    }

    public double getEndPointY() {
        return endPointY;
    }

    public PointXY getPointA() {
        return aPoint;
    }

    public PointXY getPointB() {
        return bPoint;
    }

    public PointXY getPointC() {
        return cPoint;
    }

    public PointXY getPointD() {
        return dPoint;
    }

    public Line getLineAB() {
        return abLine;
    }

    public Line getLineBC() {
        return bcLine;
    }

    public Line getLineCD() {
        return cdLine;
    }

    public Line getLineDA() {
        return daLine;
    }

    public double getMinimumX() {
        return PointXY.minimumX(
                aPoint,
                bPoint,
                cPoint,
                dPoint
        );
    }

    public double getMinimumY() {
        return PointXY.minimumY(
                aPoint,
                bPoint,
                cPoint,
                dPoint
        );
    }

    public double getMaximumX() {
        return PointXY.maximumX(
                aPoint,
                bPoint,
                cPoint,
                dPoint
        );
    }

    public double getMaximumY() {
        return PointXY.maximumY(
                aPoint,
                bPoint,
                cPoint,
                dPoint
        );
    }

    @Override
    public boolean isPointInShape(PointXY point) {
        return Ray.intersectsOdd(
                point,
                center,
                abLine.getMidPoint(),
                lines
        );
    }

    @Override
    public boolean isPointNotInShape(PointXY point) {
        return Ray.intersectsEven(
                point,
                center,
                abLine.getMidPoint(),
                lines
        );
    }

    @Override
    public PointXY getClosestPoint(PointXY referencePoint) {
        PointXY abClosestPoint = abLine.getClosestPoint(referencePoint);
        PointXY bcClosestPoint = bcLine.getClosestPoint(referencePoint);
        PointXY cdClosestPoint = cdLine.getClosestPoint(referencePoint);
        PointXY daClosestPoint = daLine.getClosestPoint(referencePoint);

        return PointXY.getClosestPoint(
                referencePoint,
                abClosestPoint,
                bcClosestPoint,
                cdClosestPoint,
                daClosestPoint
        );
    }

    @Override
    public PointXY getFurthestPoint(PointXY referencePoint) {
        PointXY abFurthestPoint = abLine.getFurthestPoint(referencePoint);
        PointXY bcFurthestPoint = bcLine.getFurthestPoint(referencePoint);
        PointXY cdFurthestPoint = cdLine.getFurthestPoint(referencePoint);
        PointXY daFurthestPoint = daLine.getFurthestPoint(referencePoint);

        return PointXY.getFurthestPoint(
                referencePoint,
                abFurthestPoint,
                bcFurthestPoint,
                cdFurthestPoint,
                daFurthestPoint
        );
    }

    @Override
    public boolean collidesWith(Shape shape) {
        return shape.getClosestPoint(center).isInside(this);
    }

    @Override
    public PointXY getCenter() {
        return center;
    }

    public Rectangle rotate(PointXY centerOfRotation,
                            Angle rotationAmount) {
        return new Rectangle(
                aPoint.rotate(centerOfRotation, rotationAmount),
                bPoint.rotate(centerOfRotation, rotationAmount),
                cPoint.rotate(centerOfRotation, rotationAmount),
                dPoint.rotate(centerOfRotation, rotationAmount)
        );
    }

    public Rectangle rotateAroundCenter(Angle rotationAmount) {
        return rotate(
                center,
                rotationAmount
        );
    }

    public Rectangle shiftX(double x) {
        return shift(new PointXY(x, 0));
    }

    public Rectangle shiftY(double y) {
        return shift(new PointXY(0, y));
    }

    public Rectangle shift(PointXY offset) {
        return new Rectangle(
                aPoint.add(offset),
                bPoint.add(offset),
                cPoint.add(offset),
                dPoint.add(offset)
        );
    }

    public Rectangle shiftAndRotate(PointXY offset,
                                    Angle rotationAmount) {
        return shift(offset).rotateAroundCenter(rotationAmount);
    }
}
