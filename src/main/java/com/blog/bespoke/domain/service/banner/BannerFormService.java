package com.blog.bespoke.domain.service.banner;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.banner.BannerForm;
import com.blog.bespoke.domain.model.banner.BannerFormStatus;
import org.springframework.stereotype.Service;

@Service
public class BannerFormService {
    public boolean checkCanBeAuditedBannerForm(BannerForm bannerForm) throws BusinessException {
        if (bannerForm.getStatus() != BannerFormStatus.PENDING) {
            throw new BusinessException(ErrorCode.BANNER_FORM_CANNOT_BE_AUDITED_STATUS);
        }
        return true;
    }
}
