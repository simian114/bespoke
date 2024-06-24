package com.blog.bespoke.infrastructure.aop.logtrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogTrace {

    // NOTE: 이걸로 어떻게 이러쿵 저러쿵해서 기본 log 포맷이 아닌 다른 포맷의 로그 출력 가능하게 할 수 있을듯
    // private static final Logger log = LoggerFactory.getLogger("AOP_LOGGER");
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private final ThreadLocal<TraceNode> TraceNodeHolder = new ThreadLocal<>();

    public TraceStatus begin(String message) {
        syncTraceId();
        TraceNode traceNode = TraceNodeHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceNode.getId(), addSpace(START_PREFIX, traceNode.getLevel()), message);

        return new TraceStatus(traceNode, startTimeMs, message);
    }

    public void end(TraceStatus status, String res) {
        complete(status, null, res);
    }

    public void exception(TraceStatus status, Exception e) {
        complete(status, e, null);
    }

    private void complete(TraceStatus status, Exception e, String res) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.startTimeMs();
        TraceNode traceNode = status.traceNode();
        if (e == null) {
            log.info("[{}] {}{} time={}ms res={}", traceNode.getId(), addSpace(COMPLETE_PREFIX, traceNode.getLevel()), status.message(), resultTimeMs, res);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceNode.getId(), addSpace(EX_PREFIX, traceNode.getLevel()), status.message(), resultTimeMs, e.toString());
        }

        releaseTraceId();
    }

    private void syncTraceId() {
        TraceNode traceNode = TraceNodeHolder.get();
        if (traceNode == null) {
            TraceNodeHolder.set(new TraceNode());
        } else {
            TraceNodeHolder.set(traceNode.createNextTraceNode());
        }
    }

    private void releaseTraceId() {
        TraceNode traceNode = TraceNodeHolder.get();
        if (traceNode.isHead()) {
            TraceNodeHolder.remove();//destroy
        } else {
            TraceNodeHolder.set(traceNode.createPreviousTraceNode());
        }
    }

    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
