/*
 * Copyright (c) 2025 qrmcat
 *
 * This file is part of ac-drones.
 *
 * ac-drones is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * ac-drones is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ac-drones; if not, see <https://www.gnu.org/licenses/>.
 *
 */

package net.apiocraft.acdrones;

import dan200.computercraft.api.turtle.TurtleCommandResult;
import org.jetbrains.annotations.Nullable;


public class DroneCommandResult {
    private static final DroneCommandResult EMPTY_SUCCESS = new DroneCommandResult(true, null, null);
    private static final DroneCommandResult EMPTY_FAILURE = new DroneCommandResult(false, null, null);

    /**
     * Create a successful command result with no result.
     *
     * @return A successful command result with no values.
     */
    public static DroneCommandResult success() {
        return EMPTY_SUCCESS;
    }

    /**
     * Create a successful command result with the given result values.
     *
     * @param results The results of executing this command.
     * @return A successful command result with the given values.
     */
    public static DroneCommandResult success(@Nullable Object[] results) {
        if (results == null || results.length == 0) return EMPTY_SUCCESS;
        return new DroneCommandResult(true, null, results);
    }

    /**
     * Create a failed command result with no error message.
     *
     * @return A failed command result with no message.
     */
    public static DroneCommandResult failure() {
        return EMPTY_FAILURE;
    }

    /**
     * Create a failed command result with an error message.
     *
     * @param errorMessage The error message to provide.
     * @return A failed command result with a message.
     */
    public static DroneCommandResult failure(@Nullable String errorMessage) {
        if (errorMessage == null) return EMPTY_FAILURE;
        return new DroneCommandResult(false, errorMessage, null);
    }

    private final boolean success;
    private final @Nullable String errorMessage;
    private final @Nullable Object[] results;

    private DroneCommandResult(boolean success, @Nullable String errorMessage, @Nullable Object[] results) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.results = results;
    }

    /**
     * Determine whether the command executed successfully.
     *
     * @return If the command was successful.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Get the error message of this command result.
     *
     * @return The command's error message, or {@code null} if it was a success.
     */
    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Get the resulting values of this command result.
     *
     * @return The command's result, or {@code null} if it was a failure.
     */
    @Nullable
    public Object[] getResults() {
        return results;
    }
}
