package com.minzheng.blog.handler;

import static com.minzheng.blog.enums.StatusCodeEnum.SYSTEM_ERROR;
import static com.minzheng.blog.enums.StatusCodeEnum.VALID_ERROR;

import com.minzheng.blog.exception.BizException;
import com.minzheng.blog.vo.Result;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理
 *
 * @author yezhqiu
 * @date 2021/06/11
 **/
@Log4j2
@RestControllerAdvice
public class ControllerAdviceHandler {

    /**
     * 处理服务异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = BizException.class)
    public Result<?> errorHandler(HttpServletRequest request, BizException e) {
        log(request, e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> errorHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        log(request, e);
        return Result.fail(VALID_ERROR.getCode(),
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(HttpServletRequest request, Exception e) {
        log(request, e);
        return Result.fail(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getDesc());
    }

    private void log(HttpServletRequest request, Exception exception) {
        //换行符
        String lineSeparatorStr = System.getProperty("line.separator");

        StringBuilder exStr = new StringBuilder();
        StackTraceElement[] trace = exception.getStackTrace();
        //获取堆栈信息并输出为打印的形式
        for (StackTraceElement s : trace) {
            exStr.append("\tat " + s + "\r\n");
        }
        //打印error级别的堆栈日志
        log.error("accessAddr：" + request.getRequestURL() + ",method：" + request.getMethod() +
                ",remoteAddr：" + request.getRemoteAddr() + lineSeparatorStr +
                "errorInfo:" + exception.toString() + lineSeparatorStr + exStr);
    }

}
