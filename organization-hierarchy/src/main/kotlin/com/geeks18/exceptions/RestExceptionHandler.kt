package com.geeks18.exceptions

import com.geeks18.entity.ErrorObject
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [Exception::class])
    protected fun handleAnyException(
            ex: RuntimeException, request: WebRequest?): ResponseEntity<Any?>? {
        logger.error("There is an error in the application $ex")


        val error : ErrorObject = ErrorObject("ERR-500","Internal Server Error ")


        return handleExceptionInternal(ex, error,
                HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request!!)
    }



    @ExceptionHandler(value = [RequestValidationException::class,ResourceNotFoundException :: class])
    protected fun handleRequestServiceException(
            ex: ServiceException, request: WebRequest?): ResponseEntity<Any?>? {
        logger.error("There is an error in the application $ex")
        return handleExceptionInternal(ex, ex.getErrors(),
                HttpHeaders(), ex.getStatus(), request!!)
    }




}