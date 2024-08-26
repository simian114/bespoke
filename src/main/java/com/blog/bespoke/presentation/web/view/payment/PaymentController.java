package com.blog.bespoke.presentation.web.view.payment;

import com.blog.bespoke.application.dto.request.payment.TossFailureRequestDto;
import com.blog.bespoke.application.dto.request.payment.TossSuccessRequestDto;
import com.blog.bespoke.application.dto.response.PaymentResponseDto;
import com.blog.bespoke.application.usecase.payment.PaymentUseCase;
import com.blog.bespoke.domain.model.payment.PaymentRefType;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentUseCase paymentUseCase;

    @ModelAttribute
    void setPaymentModel(Model model) {
        // TODO: add toss keys to model
    }

    /**
     * ref 정보만을 이용해 payment 와 ref 객체를 가지고 온다.
     */
    @GetMapping("/payment/{refId}")
    public HtmxResponse paymentPage(@PathVariable("refId") Long refId,
                                    @RequestParam("refType") PaymentRefType refType,
                                    Model model) {
        // NOTE: payment & ref 객체가 필요
        PaymentResponseDto payment = paymentUseCase.getPaymentByRef(refId, refType);
        // (BannerFormResponseDto) paymentUseCase.getPaymentRefObj(refId, refType);
        model.addAttribute("payment", payment);

        /*
         * type 과 refId 를 반드시 받아와야 한다.
         *
         */
        return HtmxResponse.builder()
                .view("page/myblog/payment/payment")
                .build();
    }

    /*
     * success url 페이지에서는 로딩 컴포넌트만 보여주기
     * 동시에 script 를 이용해 bespoke 서버로 post 요청을 보낸다.
     * bespoke 서버에서는 post 요청에 날라온 정보를 취합해서 toss 서버로 최종 결제 승인 api 를 날린다.
     * bespoke 서버에서는 이 때 restTemplate 을 사용하면 된다.
     * toss 서버로부터 최종 결제 승인 api 결과를 받으면, 그 값을 이용해 payment 객체를 update 하고, 유저를 최종 결제 승인 페이지로 redirection 한다.
     */

    // TODO: toss 에서 redirect 해주는 parameter 를 Model 화 하기
    // 실제 success 는 아니고 백엔드에서 토스로 진짜 결제 confirm 을 보내야함
    @GetMapping("/payment/{paymentId}/success")
    public HtmxResponse paymentSuccessPage(@PathVariable("paymentId") Long paymentId,
                                           @ModelAttribute TossSuccessRequestDto requestDto,
                                           Model model) {
        /*
         * TODO: toss 로 실제 pay 요청을 보낸다.
         * tossPay 내부에서는 restTemplate 을 이용해 요청을 보낸다.
         * METHOD: POST
         * URL: https://api.tosspayments.com/v1/payments/confirm
         *   headers: {
         *      Authorization: encryptedSecretKey,
         *      "Content-Type": "application/json",
         *  }
      },
         */
        // TODO: success url...
        model.addAttribute("paymentId", paymentId);
        model.addAttribute("confirmUrl", "/payment/" + paymentId + "/confirm");
        return HtmxResponse.builder()
                .view("page/myblog/payment/success")
                .build();
    }

    @PostMapping("/payment/{paymentId}/confirm")
    public HtmxResponse tossPaymentConfirm(@PathVariable("paymentId") Long paymentId,
                                           @RequestBody TossSuccessRequestDto requestDto) {
        paymentUseCase.tossPayConfirm(paymentId, requestDto);

        return HtmxResponse.builder()
                .redirect("/payment/success")
                .build();
    }

    // TODO: toss 에서 redirect 해주는 parameter 를 Model 화 하기
    @GetMapping("/payment/{paymentId}/failure")
    public HtmxResponse paymentFailurePage(@PathVariable("paymentId") Long paymentId,
                                           @ModelAttribute TossFailureRequestDto requestDto) {
        return HtmxResponse.builder()
                .build();
    }

}
