package com.javaweb.util.error;

import com.javaweb.domain.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
<<<<<<< HEAD
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import jakarta.validation.ConstraintViolationException;
=======
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleAllException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }


    @ExceptionHandler(value = {
            IdInvalidException.class,
            UsernameNotFoundException.class,
<<<<<<< HEAD
            BadCredentialsException.class,
            IllegalArgumentException.class,
            ConstraintViolationException.class})
=======
            BadCredentialsException.class})
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex)
    {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrorList = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = new ArrayList<>();

        for (FieldError fieldError : fieldErrorList) {
            errors.add(fieldError.getDefaultMessage());
        }

<<<<<<< HEAD
        res.setMessage(errors.isEmpty() ? "Dữ liệu không hợp lệ" : (errors.size() > 1? errors : errors.get(0)));
=======
        res.setMessage(errors.size() > 1? errors : errors.get(0));
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

    }

<<<<<<< HEAD
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<RestResponse<Object>> handleAccessDeniedException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setMessage("Bạn không có quyền thực hiện thao tác này");
        res.setError("Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    @ExceptionHandler(value = {
            StorageException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Exception upload file...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

    }
}
