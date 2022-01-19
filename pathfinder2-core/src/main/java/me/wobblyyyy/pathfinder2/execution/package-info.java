/*
 * Copyright (c) 2021.
 *
 * This file is part of the "Pathfinder2" project, available here:
 * <a href="https://github.com/Wobblyyyy/Pathfinder2">GitHub</a>
 *
 * This project is licensed under the GNU GPL V3 license.
 * <a href="https://www.gnu.org/licenses/gpl-3.0.en.html">GNU GPL V3</a>
 */

/**
 * Internal code responsible for managing Pathfinder's actions. The concept
 * behind these execution constructs is as follows:
 *
 * <ul>
 *     <li>
 *         Individual followers are executed by a {@code FollowerExecutor}.
 *     </li>
 *     <li>
 *         {@code FollowerExecutor}s are executed and managed by a
 *         {@code ExecutorManager}.
 *     </li>
 *     <li>
 *         Pathfinder's autonomous movement is operated via controlling
 *         and managing {@code ExecutorManager}s.
 *     </li>
 * </ul>
 *
 * <p>
 * Classes in this package are pretty much only useful inside the library.
 * </p>
 */
package me.wobblyyyy.pathfinder2.execution;
