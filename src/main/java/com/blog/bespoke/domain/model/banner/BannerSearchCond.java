package com.blog.bespoke.domain.model.banner;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

/**
 * 어드민은 이걸 볼 필요가 없다.
 * 어드민은 BannerFormSearch 를 통해 모든걸 확인할 수 있어야한다.
 * 이건 순전히 유저가 본인의 manage 페이지에서 볼 것을 위한 것임.
 * 따라서 본인의 id / nickname 으로 검색만 하면됨.
 */
@Getter
@Setter
public class BannerSearchCond extends CommonSearchCond {
    private Long advertiserId;
    private String advertiserNickname;
}
