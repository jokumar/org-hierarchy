package com.geeks18.exceptions

import org.springframework.http.HttpStatus


class RequestValidationException( code: String, description: String?): ServiceException(code,description) {

    override fun getStatus(): HttpStatus{
        return HttpStatus.BAD_REQUEST
    }
}