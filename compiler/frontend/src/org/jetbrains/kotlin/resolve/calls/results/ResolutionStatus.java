/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve.calls.results;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public enum ResolutionStatus {
    UNKNOWN_STATUS,
    UNSAFE_CALL_ERROR,
    WRONG_NUMBER_OF_TYPE_ARGUMENTS_ERROR,
    UNSTABLE_SMARTCAST_FOR_RECEIVER_ERROR,
    INVISIBLE_MEMBER_ERROR,
    NULLABLE_ARGUMENT_TYPE_MISMATCH,
    OTHER_ERROR,
    ARGUMENTS_MAPPING_ERROR,
    // '1.foo()' shouldn't be resolved to 'fun String.foo()'
    // candidates with such error are treated specially
    // (are mentioned in 'unresolved' error, if there are no other options)
    RECEIVER_TYPE_ERROR,
    // 'a.foo()' shouldn't be resolved to package level non-extension 'fun foo()'
    // candidates with such error are thrown away completely
    RECEIVER_PRESENCE_ERROR,
    INCOMPLETE_TYPE_INFERENCE,
    SUCCESS(true);

    @SuppressWarnings("unchecked")
    public static final EnumSet<ResolutionStatus>[] SEVERITY_LEVELS = new EnumSet[] {
            EnumSet.of(UNSAFE_CALL_ERROR), // weakest
            EnumSet.of(WRONG_NUMBER_OF_TYPE_ARGUMENTS_ERROR),
            EnumSet.of(UNSTABLE_SMARTCAST_FOR_RECEIVER_ERROR),
            EnumSet.of(INVISIBLE_MEMBER_ERROR),
            EnumSet.of(NULLABLE_ARGUMENT_TYPE_MISMATCH),
            EnumSet.of(OTHER_ERROR),
            EnumSet.of(ARGUMENTS_MAPPING_ERROR),
            EnumSet.of(RECEIVER_TYPE_ERROR),
            EnumSet.of(RECEIVER_PRESENCE_ERROR), // most severe
    };

    private final boolean success;
    private int severityIndex = -1;

    private ResolutionStatus(boolean success) {
        this.success = success;
    }

    private ResolutionStatus() {
        this(false);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean possibleTransformToSuccess() {
        return this == UNKNOWN_STATUS || this == INCOMPLETE_TYPE_INFERENCE || this == SUCCESS;
    }

    @NotNull
    public ResolutionStatus combine(ResolutionStatus other) {
        if (this == UNKNOWN_STATUS) return other;
        if (SUCCESS.among(this, other)) {
            return SUCCESS.chooseDifferent(this, other);
        }
        if (INCOMPLETE_TYPE_INFERENCE.among(this, other)) {
            return INCOMPLETE_TYPE_INFERENCE.chooseDifferent(this, other);
        }
        if (this.getSeverityIndex() < other.getSeverityIndex()) return other;
        return this;
    }

    private boolean among(ResolutionStatus first, ResolutionStatus second) {
        return this == first || this == second;
    }

    private ResolutionStatus chooseDifferent(ResolutionStatus first, ResolutionStatus second) {
        assert among(first, second);
        return this == first ? second : first;
    }

    private int getSeverityIndex() {
        if (severityIndex == -1) {
            for (int i = 0; i < SEVERITY_LEVELS.length; i++) {
                if (SEVERITY_LEVELS[i].contains(this)) {
                    severityIndex = i;
                    break;
                }
            }
        }
        assert severityIndex >= 0;

        return severityIndex;
    }
}
