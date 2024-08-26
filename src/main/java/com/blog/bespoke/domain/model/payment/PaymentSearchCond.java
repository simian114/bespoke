package com.blog.bespoke.domain.model.payment;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSearchCond extends CommonSearchCond {
    private Long userId;

    // NOTE: refId 와 refType 은 같이 묶어야 한다.
    private Long refId;
    private PaymentRefType refType;
}
