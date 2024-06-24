package com.blog.bespoke.infrastructure.web.filter.transaction;

import java.util.UUID;

public class TrIdHolder {
    private static final ThreadLocal<String> trIdHolder = new ThreadLocal<>();

    public static void setTrId(String trId) {
        trIdHolder.set(trId);
    }

    public static String getTrId(){
        return trIdHolder.get();
    }

    public static void clear() {
        trIdHolder.remove();
    }

    public static String generateTrId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
