package com.bogdan.calculator.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for logging method execution.
 * This aspect provides logging functionality for methods annotated with @Loggable,
 * including method entry/exit logging, parameter logging, result logging, and execution time measurement.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Around advice that logs method execution details.
     * This advice is applied to methods annotated with @Loggable.
     *
     * @param joinPoint The join point representing the method execution
     * @return The result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("@annotation(Loggable)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Loggable loggable = signature.getMethod().getAnnotation(Loggable.class);
        String methodName = getMethodName(joinPoint, loggable);
        
        logMethodEntry(methodName, joinPoint.getArgs(), loggable.logParams());
        
        long startTime = System.nanoTime();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = (System.nanoTime() - startTime) / 1_000_000;
            
            logMethodExit(methodName, result, loggable.logResult());
            logExecutionTime(methodName, executionTime, loggable.logExecutionTime());
            
            return result;
        } catch (Exception e) {
            long executionTime = (System.nanoTime() - startTime) / 1_000_000;
            logException(methodName, executionTime, e);
            throw e;
        }
    }

    /**
     * Logs method entry with optional parameter logging.
     *
     * @param methodName The name of the method being logged
     * @param args The method arguments
     * @param logParams Whether to log the method parameters
     */
    private void logMethodEntry(String methodName, Object[] args, boolean logParams) {
        if (logParams) {
            log.info("Entering method: {} with parameters: {}", methodName, Arrays.toString(args));
        } else {
            log.info("Entering method: {}", methodName);
        }
    }

    /**
     * Logs method exit with optional result logging.
     *
     * @param methodName The name of the method being logged
     * @param result The method result
     * @param logResult Whether to log the method result
     */
    private void logMethodExit(String methodName, Object result, boolean logResult) {
        if (logResult) {
            log.info("Exiting method: {} with result: {}", methodName, result);
        } else {
            log.info("Exiting method: {}", methodName);
        }
    }

    /**
     * Logs method execution time.
     *
     * @param methodName The name of the method being logged
     * @param executionTime The execution time in milliseconds
     * @param logExecutionTime Whether to log the execution time
     */
    private void logExecutionTime(String methodName, long executionTime, boolean logExecutionTime) {
        if (logExecutionTime) {
            log.info("Method: {} executed in {} ms", methodName, executionTime);
        }
    }

    /**
     * Logs exceptions that occur during method execution.
     *
     * @param methodName The name of the method where the exception occurred
     * @param executionTime The execution time in milliseconds before the exception
     * @param e The exception that occurred
     */
    private void logException(String methodName, long executionTime, Exception e) {
        log.error("Exception in method: {} after {} ms. Exception: {}", 
                methodName, executionTime, e.getMessage(), e);
    }

    /**
     * Gets the method name to use in logs.
     * If a custom name is specified in the @Loggable annotation, that is used;
     * otherwise, the actual method name is used.
     *
     * @param joinPoint The join point representing the method execution
     * @param loggable The @Loggable annotation instance
     * @return The method name to use in logs
     */
    private String getMethodName(ProceedingJoinPoint joinPoint, Loggable loggable) {
        String methodName = joinPoint.getSignature().getName();
        return loggable.value().isEmpty() ? methodName : loggable.value();
    }
} 