/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TemporaryBindingTrace extends DelegatingBindingTrace {

    @NotNull
    public static TemporaryBindingTrace create(@NotNull BindingTrace trace, String debugName) {
        return create(trace, debugName, BindingTraceFilter.Companion.getACCEPT_ALL());
    }

    @NotNull
    public static TemporaryBindingTrace create(@NotNull BindingTrace trace, String debugName, BindingTraceFilter filter) {
        return new TemporaryBindingTrace(trace, debugName, filter);
    }

    @NotNull
    public static TemporaryBindingTrace create(@NotNull BindingTrace trace, String debugName, @Nullable Object resolutionSubjectForMessage) {
        return create(trace, AnalyzingUtils.formDebugNameForBindingTrace(debugName, resolutionSubjectForMessage));
    }

    protected final BindingTrace trace;

    protected TemporaryBindingTrace(@NotNull BindingTrace trace, String debugName, BindingTraceFilter filter) {
        super(trace.getBindingContext(), debugName, true, filter, false);
        this.trace = trace;
    }

    public void commit() {
        addOwnDataTo(trace);
        clear();
    }

    public void commit(@NotNull TraceEntryFilter filter, boolean commitDiagnostics) {
        addOwnDataTo(trace, filter, commitDiagnostics);
        clear();
    }

    @Override
    public boolean wantsDiagnostics() {
        return trace.wantsDiagnostics();
    }
}
