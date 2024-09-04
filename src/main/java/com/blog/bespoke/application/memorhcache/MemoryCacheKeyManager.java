package com.blog.bespoke.application.memorhcache;

import com.blog.bespoke.infrastructure.util.RedisUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 각 usecase 별로 cache 키를 우선 생성
 */
@RequiredArgsConstructor
public class MemoryCacheKeyManager {
    static public class Banner {
        static private final String ENTITY = "banner";

        static public String getTopBanners() {
            return RedisUtil.generateKey(ENTITY, List.of("top"));
        }

        static public String getMainHeroBanner() {
            return RedisUtil.generateKey(ENTITY, List.of("main", "hero"));
        }

        static public String getMainPopupBanner() {
            return RedisUtil.generateKey(ENTITY, List.of("main", "popup"));
        }

        static public List<String> getAllManagedKeys() {
            return List.of(getTopBanners(), getMainHeroBanner(), getMainPopupBanner());
        }
    }

}
