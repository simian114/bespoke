package com.blog.bespoke.infrastructure.aop.logtrace;

import com.blog.bespoke.infrastructure.web.filter.transaction.TrIdHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TraceNode {
    private final String id;
    private final int level;

    public TraceNode() {
        this.id = TrIdHolder.getTrId();
        this.level = 0;
    }

    public TraceNode createNextTraceNode() {
        return new TraceNode(id, level + 1);
    }

    public TraceNode createPreviousTraceNode() {
        return new TraceNode(id, level - 1);
    }

    public boolean isHead() {
        return level == 0;
    }

}
