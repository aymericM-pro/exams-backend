package com.app.examproject.commons.responses;


import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return !StringHttpMessageConverter.class.isAssignableFrom(converterType)
                && returnType.getContainingClass()
                .getPackageName()
                .startsWith("com.app.examproject.controller");
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {

        // 🔴 CRITIQUE : ne jamais wrapper du binaire
        if (body instanceof byte[]) {
            return body;
        }

        if (body instanceof ApiResponse<?>) {
            return body;
        }

        if (body instanceof Page<?> page) {
            return ApiResponse.ok(
                    page.getContent(),
                    new PageMeta(
                            page.getNumber(),
                            page.getSize(),
                            page.getTotalElements(),
                            page.getTotalPages(),
                            page.isFirst(),
                            page.isLast()
                    )
            );
        }

        return ApiResponse.ok(body);
    }
}
