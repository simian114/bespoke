package com.blog.bespoke.infrastructure.web.exception;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.EnvelopeResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<?> handleBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getHeader("HX-Request") == null) {
            response.sendRedirect("/errors");
            return null;
        }
        return EnvelopeResponse.envelope(e.getStatusCode(), e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        return "redirect:/errors";
    }
}
