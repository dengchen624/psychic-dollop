package com.itheima.reggie.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * The type Globa exception handler.
 *
 * @author wild
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobaExceptionHandler {
    /**
     * Exceptionhandler r.
     *
     * @return the r
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String > exceptionHandler(SQLIntegrityConstraintViolationException ex){
        if(ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String msg=s[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)
    public R<String > exceptionHandler(CustomException ce){


        return R.error(ce.getMessage());
    }
}
