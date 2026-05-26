package com.javaweb.util;

import com.javaweb.domain.response.RestResponse;
import com.javaweb.util.annotation.ApiMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
<<<<<<< HEAD
import org.springframework.core.io.Resource;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpServletResponse.getStatus();

<<<<<<< HEAD
        if (body instanceof RestResponse<?> || body instanceof Resource ||
                (selectedContentType != null && !MediaType.APPLICATION_JSON.includes(selectedContentType))) {
            return body;
        }
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);

        if(status >= 400){
            return body;
        }else{
            res.setData(body);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            res.setMessage(message != null ? message.value() : "Call API success");
        }
        return res;
    }
}
