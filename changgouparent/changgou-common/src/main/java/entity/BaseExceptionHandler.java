package entity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName com.changgou.controller.BaseExceptionHandler
 * @Description
 * @Author Maid
 * @Date 2020/7/4 0004 22:49
 * @Version v1.0
 */
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value=Exception.class)
    @ResponseBody

    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }

}
