package com.geeks18.exceptions

import com.geeks18.entity.ErrorObject
import org.springframework.http.HttpStatus

open class ServiceException( val code: String,  val description: String?):  RuntimeException()  {

    open fun getErrors(): ErrorObject {
        return ErrorObject(this.code,this.description)
    }

    open fun getStatus() : HttpStatus {
        return HttpStatus.INTERNAL_SERVER_ERROR
    }
}