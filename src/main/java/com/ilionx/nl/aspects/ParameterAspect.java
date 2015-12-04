package com.ilionx.nl.aspects;

import com.ilionx.nl.properties.XmlPropertyProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ParameterAspect {

    private XmlPropertyProvider xmlPropertyProvider = new XmlPropertyProvider();

    @Around("execution(* com.xebia.incubator.xebium..*.*(..))")
    public Object xebiumReplaceParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        return replaceParameters(joinPoint);
    }

    @Around("execution(* com.ilionx.nl.soap..*.*(..))")
    public Object soapReplaceParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        return replaceParameters(joinPoint);
    }

    @Around("execution(* com.ilionx.nl.mail..*.*(..))")
    public Object mailReplaceParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        return replaceParameters(joinPoint);
    }

    private Object replaceParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        System.err.println("Joinpoint reached!!!");
        Object[] args = new Object[joinPoint.getArgs().length];
        for (int x = 0; x < joinPoint.getArgs().length; x++) {
            args[x] = joinPoint.getArgs()[x];
            if (joinPoint.getArgs()[x] instanceof String) {
                if (((String) joinPoint.getArgs()[x]).startsWith("#")) {

                    String argument = ((String) joinPoint.getArgs()[x]);
                    System.err.println("Argument found: " + argument);

                    int colonIndex = argument.indexOf(":");

                    String fileName = argument.substring(1, colonIndex);
                    String xpath = argument.substring(colonIndex+1);

                    System.err.println("Filename found: " + fileName);

                    if(fileName.startsWith("!")){
                        args[x] = xmlPropertyProvider.getXmlPropertyByXpathFromCommon(fileName.substring(1), xpath);
                    } else {
                        args[x] = xmlPropertyProvider.getXmlPropertyByXpath(fileName, xpath);
                    }
                }
            }
        }
        return joinPoint.proceed(args);
    }

}
