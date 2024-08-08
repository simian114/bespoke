package com.blog.bespoke.infrastructure.web.htmx;

import java.util.Map;

public class Toast {
    static public final String TRIGGER = "toast";

    static private Map<String, String> show(Level level, String message) {
        return Map.of("level", level.level, "message", message);
    }

    static public Map<String, String> error(String message) {
        return show(Level.ERROR, message);
    }

    static public Map<String, String> info(String message) {
        return show(Level.INFO, message);
    }

    static public Map<String, String> success(String message) {
        return show(Level.SUCCESS, message);
    }

    public enum Level {
        WARNING("warning"),
        ERROR("error"),
        INFO("info"),
        SUCCESS("success");

        public final String level;

        Level(String level) {
            this.level = level;
        }
    }
}
