package com.ilionx.nl.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class ParameterAspect {

    @Around("execution(* com.xebia.incubator.xebium..*.*(..))")
    public Object replaceParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        System.err.println("Joinpoint reached!!!");
        Object[] args = new Object[joinPoint.getArgs().length];
        for (int x = 0; x < joinPoint.getArgs().length; x++) {
            args[x] = joinPoint.getArgs()[x];
            if (joinPoint.getArgs()[x] instanceof String) {
                if (((String) joinPoint.getArgs()[x]).startsWith("param:")) {
                    args[x] = "kip";
                }
            }
        }
        return joinPoint.proceed(args);
    }

}
