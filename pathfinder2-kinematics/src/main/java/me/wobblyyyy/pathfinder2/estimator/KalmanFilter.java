/*
 * Copyright (c) 2022.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

package me.wobblyyyy.pathfinder2.estimator;

import org.ejml.data.DMatrixRMaj;
import org.ejml.equation.Equation;
import org.ejml.equation.Sequence;

/**
 * Kalman filtering, or linear quadratic estimation, is "an algorithm that
 * uses a series of measurements observed over time, including statistical
 * noise and other inaccuracies, and produces estimates of unknown variables
 * for each timeframe" (Wikipedia). To be completely honest, I only have
 * a very basic understanding of the math going on here, so the documentation
 * might be... a little bit shaky. Kalman filters are commonly used in
 * autonomous navigation and control software because they help to safeguard
 * against unwanted and extraneous sensor input. In the case of robotics,
 * these filters can be used for several purposes, including measuring the
 * velocity of an encoder, fusing data from multiple sensors, or estimating
 * the robot's position.
 *
 * <p>
 * Note that a Kalman filter is designed to reduce noise, NOT increase the
 * accuracy of measurements. The only way to increase the accuracy of those
 * measurements is to increase the accuracy of the tools used in gathering
 * those measurements. If you have values with high accuracy but low precision,
 * one of these bad boys should help you a lot. If you values with low
 * accuracy but high precision... well, I hate to break it to you, but you're
 * really kind of out of luck.
 * </p>
 *
 * <p>
 * A Kalman filter is based upon three matrices:
 * <ul>
 *     <li>State matrix</li>
 *     <li>Covariance matrix</li>
 *     <li>Projection matrix</li>
 * </ul>
 * Of those three matrices, the state and covariance matrices are the
 * most important - the projection matrix can be disregarded for simple
 * use cases. The state matrix is the filter's current state - if you're
 * using an encoder, for example, this might be the filter's velocity.
 * The covariance matrix determines how heavily each value from the state
 * matrix is weighted - a higher covariance means that the value is less
 * accurate, meaning it will not affect the output as much, while a lower
 * covariance means the value is more accurate, and that value will impact
 * the output more. The projection matrix is used for outputting values.
 * </p>
 *
 * <p>
 * Here are a bunch of resources I used in creating this class:
 * <ul>
 *     <li>
 *         <a href="https://en.wikipedia.org/wiki/Kalman_filter">
 *             Wikipedia (of course...)
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://github.com/lessthanoptimal/ejml/blob/v0.41/examples/src/org/ejml/example/KalmanFilter.java">
 *             EJML example Kalman Filter interface
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://github.com/lessthanoptimal/ejml/blob/v0.41/examples/src/org/ejml/example/KalmanFilterEquation.java">
 *             EJML example Kalman Filter interface implementation
 *             using equations
 *         </a>
 *         (a very good portion of the code in this file is the same as, or
 *         very similar to, the code in the linked file)
 *     </li>
 *     <li>
 *         <a href="https://github.com/wpilibsuite/allwpilib/blob/main/wpimath/src/main/java/edu/wpi/first/math/estimator/KalmanFilter.java">
 *             wpilib Kalman Filter implementation
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://github.com/kartverket/BorderGo/blob/master/BorderGo/positionorientation/src/main/java/no/kartverket/positionorientation/TangoPositionOrientationProvider.java">
 *             BorderGo Kalman Filter usage
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://handwiki.org/wiki/Projection_matrix">
 *             HandWiki projection matrix
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://robotics.stackexchange.com/questions/20395/extended-kalman-filtering-for-imu-and-encoder">
 *             Extended Kalman Filtering for IMU and Encoder
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://robotics.stackexchange.com/questions/22115/how-to-actually-fuse-sensor-using-extended-kalman-filter">
 *             Fusing sensor data with the Extended Kalman Filter
 *         </a>
 *     </li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @see <a href="https://en.wikipedia.org/wiki/Kalman_filter">Wikipedia</a>
 * @since 0.8.0
 * @deprecated I don't know enough about math to be able to support this, so
 * I don't know if this works as its supposed to. Until either (a) I learn
 * enough about math to know what's going on here or (b) someone else who
 * knows enough about math contributes to this project, this class will not
 * be officially supported, but will still be included for posterity.
 */
@Deprecated
public class KalmanFilter {
    private final DMatrixRMaj state;
    private final DMatrixRMaj covariance;

    private final Equation equation;

    private final Sequence predictX;
    private final Sequence predictP;

    private final Sequence updateX;
    private final Sequence updateY;
    private final Sequence updateK;
    private final Sequence updateP;

    /**
     * Create a new Kalman filter.
     *
     * @param initialState      the initial states/measurements. This must be
     *                          the same size as the covariance array.
     * @param initialCovariance the initial covariance values. This must be
     *                          the same size as the state array.
     */
    public KalmanFilter(double[] initialState, double[] initialCovariance) {
        this(
            getStateMatrix(initialState),
            getCovarianceMatrix(initialCovariance),
            getProjectionMatrix(initialState.length)
        );
    }

    /**
     * Create a new Kalman filter.
     *
     * @param initialState      the initial states/measurements. This must be
     *                          the same size as the covariance array.
     * @param initialCovariance the initial covariance values. This must be
     *                          the same size as the state array.
     * @param initialProjection how output values are modified.
     */
    public KalmanFilter(
        double[] initialState,
        double[] initialCovariance,
        double[] initialProjection
    ) {
        this(
            getStateMatrix(initialState),
            getCovarianceMatrix(initialCovariance),
            getProjectionMatrix(initialProjection)
        );
    }

    /**
     * Create a new Kalman filter.
     *
     * @param stateTransitionMatrix a matrix containing the initial states
     *                              of the inputs.
     * @param plantNoiseMatrix      a matrix containing the initial covariance
     *                              of the values of the inputs.
     * @param projectionMatrix      a matrix containing values that will be
     *                              used when projecting outputs.
     */
    public KalmanFilter(
        DMatrixRMaj stateTransitionMatrix,
        DMatrixRMaj plantNoiseMatrix,
        DMatrixRMaj projectionMatrix
    ) {
        int size = stateTransitionMatrix.numCols;

        boolean validPlantNoise = plantNoiseMatrix.numCols == size;
        boolean validProjection = projectionMatrix.numCols == size;

        if (!validPlantNoise || !validProjection) {
            throw new IllegalArgumentException(
                "Cannot create a Kalman filter with matrices of " +
                "different sizes - make sure all of the " +
                "matrices have the same number of columns!"
            );
        }

        this.state = new DMatrixRMaj(size, 1);
        this.covariance = new DMatrixRMaj(size, size);

        equation = new Equation();

        equation.alias(
            state,
            "x",
            covariance,
            "p",
            stateTransitionMatrix,
            "f",
            plantNoiseMatrix,
            "q",
            projectionMatrix,
            "h"
        );

        equation.alias(new DMatrixRMaj(1, 1), "z");
        equation.alias(new DMatrixRMaj(1, 1), "r");

        predictX = equation.compile("x = f * x");
        predictP = equation.compile("p = f * p * f' + q");

        updateX = equation.compile("x = x + k * y");
        updateY = equation.compile("y = z - h * x");
        updateK = equation.compile("k = p * h' * inv(h * p * h' + r)");
        updateP = equation.compile("p = p - k * (h * p)");
    }

    /**
     * Create a matrix from state values.
     *
     * @param state the states.
     * @return a new matrix.
     */
    public static DMatrixRMaj getStateMatrix(double[] state) {
        int size = state.length;

        DMatrixRMaj matrix = new DMatrixRMaj(size, 1);

        for (int i = 0; i < size; i++) {
            matrix.set(i, 0, state[i]);
        }

        return matrix;
    }

    /**
     * Create a matrix from covariance values.
     *
     * @param covariance the covariance. Higher covariance means that the
     *                   value will be weighted less, while a lower covariance
     *                   means the value will be weighted more.
     * @return a new matrix.
     */
    public static DMatrixRMaj getCovarianceMatrix(double[] covariance) {
        int size = covariance.length;

        DMatrixRMaj matrix = new DMatrixRMaj(size, size);

        for (int i = 0; i < size; i++) {
            matrix.set(i, i, covariance[i]);
        }

        return matrix;
    }

    /**
     * Create a matrix from projection values.
     *
     * @param projection the projection values. To be completely and totally
     *                   honest, I have absolutely no idea what this means.
     * @return a new matrix.
     */
    public static DMatrixRMaj getProjectionMatrix(double[] projection) {
        int size = projection.length;

        DMatrixRMaj matrix = new DMatrixRMaj(size, size);

        for (int i = 0; i < size; i++) {
            matrix.set(i, i, projection[i]);
        }

        return matrix;
    }

    /**
     * Create a matrix from projection values.
     *
     * @return a new matrix with 1 as the only value.
     */
    public static DMatrixRMaj getProjectionMatrix(int size) {
        DMatrixRMaj matrix = new DMatrixRMaj(size, size);

        for (int i = 0; i < size; i++) {
            matrix.set(i, i, 1);
        }

        return matrix;
    }

    /**
     * Set the state of the filter.
     *
     * @param state      the state to set to the filter.
     * @param covariance the covariance of the state.
     */
    public void setState(DMatrixRMaj state, DMatrixRMaj covariance) {
        this.state.set(state);
        this.covariance.set(covariance);
    }

    /**
     * Predict values.
     */
    public void predict() {
        predictX.perform();
        predictP.perform();
    }

    /**
     * Update the filter. This should be called AFTER prediction has been
     * completed.
     *
     * @param state      the state of the input.
     * @param covariance the covariance of the input.
     */
    public void update(DMatrixRMaj state, DMatrixRMaj covariance) {
        equation.alias(state, "z", covariance, "r");

        // not sure if the order these are performed in matters or not
        updateY.perform();
        updateK.perform();
        updateX.perform();
        updateP.perform();
    }

    /**
     * Get the most recently calculated state.
     *
     * @return the most recently calculated state.
     */
    public DMatrixRMaj getState() {
        return state;
    }

    /**
     * Get the most recently calculated covariance.
     *
     * @return the most recently calculated covariance.
     */
    public DMatrixRMaj getCovariance() {
        return covariance;
    }
}
