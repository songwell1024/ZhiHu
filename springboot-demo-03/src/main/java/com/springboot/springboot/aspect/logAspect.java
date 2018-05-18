package com.springboot.springboot.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class logAspect {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(logAspect.class);

    @Before("execution(* com.springboot.springboot.controller.indexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuffer sb = new StringBuffer();
        for(Object args: joinPoint.getArgs()){
            sb.append("arg: "+ args.toString()+ "|");
        }
        logger.info("before method"+ "|"+ sb.toString());

    }

    @After("execution(* com.springboot.springboot.controller.indexController.*(..))")
    public void afterMethod(){

        logger.info("after method");

    }
}
