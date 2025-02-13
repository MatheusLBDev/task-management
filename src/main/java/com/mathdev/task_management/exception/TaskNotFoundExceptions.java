package com.mathdev.task_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TaskNotFoundExceptions extends RuntimeException {

    public TaskNotFoundExceptions(String message){
        super(message);
    }

}
