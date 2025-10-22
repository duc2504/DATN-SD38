package com.example.datn.Config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Bắt các lỗi validate (IllegalArgumentException) mà chúng ta đã định nghĩa
     * trong UserService.
     * Trả về mã lỗi 400 BAD REQUEST.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage()); // Đây chính là thông báo lỗi của bạn

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Bắt các lỗi không tìm thấy (RuntimeException) từ các hàm như
     * findById.orElseThrow(...)
     * Trả về mã lỗi 404 NOT FOUND.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        // Phân biệt lỗi "Not Found" với các lỗi Runtime khác
        if (ex.getMessage().contains("Không tìm thấy")) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", ex.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Nếu là lỗi Runtime khác (ví dụ: lỗi logic 500)
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "Đã xảy ra lỗi hệ thống: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}