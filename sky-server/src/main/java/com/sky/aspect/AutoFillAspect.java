package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {


    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {
    }

    @Before("autoFillPointcut()")
    public void beforeAutoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充");

        //获取签名方法
        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType=autoFill.value();

        //获取参数
        Object[] args=joinPoint.getArgs();
        if(args ==null || args.length==0){
            return ;
        }
        Object obj=args[0];

        //根据操作类型进行填充
        if(operationType==OperationType.UPDATE){
            try{

                //获取方法:设定按钮
                Method setUpdateTimeMethod=obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUserMethod=obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                //给方法传参
                setUpdateTimeMethod.invoke(obj,LocalDateTime.now());
                setUpdateUserMethod.invoke(obj,BaseContext.getCurrentId());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if(operationType==OperationType.INSERT){
            try{
                Method setCreateTimeMethod=obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setCreateUserMethod=obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateTimeMethod=obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUserMethod=obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                setCreateTimeMethod.invoke(obj,LocalDateTime.now());
                setCreateUserMethod.invoke(obj,BaseContext.getCurrentId());
                setUpdateTimeMethod.invoke(obj,LocalDateTime.now());
                setUpdateUserMethod.invoke(obj,BaseContext.getCurrentId());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
