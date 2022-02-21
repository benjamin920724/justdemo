package com.example.assignment.biz.placesearch.common.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response CustomException(Exception e) {
        e.printStackTrace();
        return new Response("400", e.getMessage());
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response ConstraintViolationException(Exception e) {
        e.printStackTrace();
        return new Response("500", "Too many request at once! Please take a breath and try again.");
    }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response MissingServletRequestParameterException(Exception e) {
        e.printStackTrace();
        return new Response("400", "cURL request format is incorrect, Correct it and try again");
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response ServerException(Exception e) {
        e.printStackTrace();
        return new Response("500", "Server Error.. Please try this later.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundException(Exception e) {
        e.printStackTrace();
        return new Response("404", "No Handler Found Exception. Try [/api/hotkeyword] or [/api/place/{keyword}]");
    }

    @Data
    @AllArgsConstructor
    static class Response {
        private String code;
        private String msg;
    }
}
