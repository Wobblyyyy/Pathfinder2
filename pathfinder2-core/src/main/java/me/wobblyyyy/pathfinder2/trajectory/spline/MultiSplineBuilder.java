/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.trajectory.spline;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXY;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.multi.segment.MultiSegmentTrajectory;
import me.wobblyyyy.pathfinder2.utils.StringUtils;
import me.wobblyyyy.pathfinder2.utils.Trio;

public class MultiSplineBuilder {
    private Node defaultNode = new Node();

    private final List<Node> nodes = new ArrayList<>();

    public MultiSplineBuilder() {
        defaultNode.mode = InterpolationMode.DEFAULT;
    }

    public MultiSplineBuilder setDefaultSpeed(double speed) {
        defaultNode.speed = speed;

        return this;
    }

    public MultiSplineBuilder setDefaultTolerance(double tolerance) {
        defaultNode.tolerance = tolerance;

        return this;
    }

    public MultiSplineBuilder setDefaultAngleTolerance(Angle angleTolerance) {
        defaultNode.angleTolerance = angleTolerance;

        return this;
    }

    public MultiSplineBuilder setDefaultStep(double step) {
        defaultNode.step = step;

        return this;
    }

    public MultiSplineBuilder setDefaultInterpolationMode(InterpolationMode mode) {
        defaultNode.mode = mode;

        return this;
    }

    public MultiSplineBuilder add(double x,
                                  double y,
                                  double zDeg,
                                  double speed,
                                  double step,
                                  double tolerance,
                                  Angle angleTolerance) {
        Node node = new Node(defaultNode);

        node.x = x;
        node.y = y;
        node.zDeg = zDeg;
        node.speed = speed;
        node.step = step;
        node.tolerance = tolerance;
        node.angleTolerance = angleTolerance;

        nodes.add(node);

        return this;
    }

    public MultiSplineBuilder add(double x,
                                  double y,
                                  Angle z,
                                  double speed,
                                  double step,
                                  double tolerance,
                                  Angle angleTolerance) {
        Node node = new Node(defaultNode);

        node.x = x;
        node.y = y;
        node.zDeg = z.deg();
        node.speed = speed;
        node.step = step;
        node.tolerance = tolerance;
        node.angleTolerance = angleTolerance;

        nodes.add(node);

        return this;
    }

    public MultiSplineBuilder add(double x,
                                  double y,
                                  Angle z,
                                  double step) {
        Node node = new Node(defaultNode);

        node.x = x;
        node.y = y;
        node.zDeg = z.deg();
        node.step = step;

        nodes.add(node);

        return this;
    }

    public MultiSplineBuilder add(PointXY point,
                                  double speed,
                                  double tolerance,
                                  Angle angleTolerance,
                                  double step) {
        Node node = new Node(defaultNode);

        node.x = point.x();
        node.y = point.y();

        if (point instanceof PointXYZ)
            node.zDeg = ((PointXYZ) point).z().deg();

        node.speed = speed;
        node.tolerance = tolerance;
        node.angleTolerance = angleTolerance;
        node.step = step;

        nodes.add(node);

        return this;
    }

    public MultiSplineBuilder add(PointXY point,
                                  double speed,
                                  double step) {
        Node node = new Node(defaultNode);

        node.x = point.x();
        node.y = point.y();

        if (point instanceof PointXYZ)
            node.zDeg = ((PointXYZ) point).z().deg();

        node.speed = speed;
        node.step = step;

        nodes.add(node);

        return this;
    }

    private static List<NodeTrio> getTrios(List<Node> nodes) {
        int size = nodes.size();

        if (size < 3)
            throw new IllegalArgumentException("You need at least three " +
                    "or more control points!");

        List<NodeTrio> trios = new ArrayList<>(nodes.size());

        for (int i = 0; i < size - 2; i++)
            trios.add(new NodeTrio(
                        nodes.get(i),
                        nodes.get(i + 1),
                        nodes.get(i + 2)
            ));

        return trios;
    }

    private static void verifyMonotonicity(List<NodeTrio> trios,
                                           List<Node> allNodes) {
        return;

        /*
        for (NodeTrio trio : trios) {
            PointXYZ a = trio.getA().point();
            PointXYZ b = trio.getB().point();
            PointXYZ c = trio.getC().point();

            if (!Spline.areMonotonic(a, b, c))
                throw new IllegalArgumentException(StringUtils.format(
                            "Failed to verify monotonicty because one or " +
                            "more trios (group of 3 adjacent points) were " +
                            "not monotonic. The set of three points that " +
                            "are not monotonic are %s, %s, and %s. " +
                            "All nodes: %s",
                            trio.getA().point(),
                            trio.getB().point(),
                            trio.getC().point(),
                            allNodes.toString()
                ));
        }
        */
    }

    public Trajectory build() {
        List<NodeTrio> trios = getTrios(nodes);

        // make sure all of the points are monotonic
        verifyMonotonicity(trios, nodes);

        SplineBuilderFactory factory = new SplineBuilderFactory()
            .setSpeed(defaultNode.speed)
            .setTolerance(defaultNode.tolerance)
            .setAngleTolerance(defaultNode.angleTolerance)
            .setStep(defaultNode.step);

        List<Trajectory> trajectories = trios.stream().map((trio) -> {
            Node a = trio.getA();
            Node b = trio.getB();
            Node c = trio.getC();

            PointXYZ aPoint = a.point();
            PointXYZ bPoint = b.point();
            PointXYZ cPoint = c.point();

            double aSpeed = a.speed;
            double bSpeed = b.speed;
            double cSpeed = c.speed;

            return factory.builder()
                .setInterpolationMode(a.mode)
                .add(aPoint, aSpeed)
                .add(bPoint, bSpeed)
                .add(cPoint, cSpeed)
                .build();
        }).collect(Collectors.toList());

        return new MultiSegmentTrajectory(trajectories);
    }

    private static class Node {
        double x, y, zDeg, speed, tolerance, step;
        Angle angleTolerance;
        InterpolationMode mode;

        Node() {

        }

        Node(Node node) {
            this.x = node.x;
            this.y = node.y;
            this.zDeg = node.zDeg;
            this.speed = node.speed;
            this.tolerance = node.tolerance;
            this.angleTolerance = node.angleTolerance;
            this.step = node.step;
            this.mode = node.mode;
        }

        PointXYZ point() {
            return new PointXYZ(x, y, zDeg);
        }

        @Override
        public String toString() {
            return StringUtils.format("(%s, %s)", x, y);
        }
    }

    private static class NodeTrio extends Trio<Node, Node, Node> {
        NodeTrio(Node a, Node b, Node c) {
            super(a, b, c);
        }
    }
}
