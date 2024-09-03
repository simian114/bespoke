package com.blog.bespoke.infrastructure.util;

import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.List;

public class RedisUtil {
    static public String generateKey(String entity, List<String> usages, String... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity);
        sb.append(":");

        sb.append(Strings.join(usages, ':'));

        if ((params != null) && (params.length != 0)) {
            sb.append(":");
            sb.append(Strings.join(Arrays.asList(params), ':'));
        }

        return sb.toString();
    }
}
