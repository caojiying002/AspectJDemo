package com.example.aspectjdemo.aop.aspect;

import com.example.aspectjdemo.aop.internal.DebugLog;
import com.example.aspectjdemo.aop.internal.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect representing the cross cutting-concern: Method and Constructor Tracing.
 */
@Aspect
public class TraceAspect {

    private static final String POINTCUT_METHOD =
            "execution(@com.example.aspectjdemo.aop.annotation.DebugTrace * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.example.aspectjdemo.aop.annotation.DebugTrace *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithDebugTrace() {}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorWithAnnotatedDebugTrace() {}

    @Around("methodAnnotatedWithDebugTrace() || constructorWithAnnotatedDebugTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("成功了");

        Signature signature = joinPoint.getSignature();
        System.out.println(signature.toString());

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 执行实际代码
        Object result = joinPoint.proceed();
        stopWatch.stop();

        // MethodSignature methodSignature = (MethodSignature) signature;
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        DebugLog.log(className, buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));

        return result;
    }


    /**
     * Create a log message.
     *
     * @param methodName A string with the method name.
     * @param methodDuration Duration of the method in milliseconds.
     * @return A string representing message.
     */
    private static String buildLogMessage(String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append("Gintonic --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");

        return message.toString();
    }
}
