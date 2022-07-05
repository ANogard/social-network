package ru.example.socnetwork.logging;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.example.socnetwork.model.ClassMethodInformation;

import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

  @Getter
  private final ConcurrentHashMap<String, Logger> loggerConcurrentHashMap = new ConcurrentHashMap<>();

  private ClassMethodInformation getClassMethodInformation(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String className = signature.getDeclaringType().getSimpleName();
    String methodName = signature.getName();
    String[] paramsNames = signature.getParameterNames();
    String outputLine;
    if (paramsNames.length != 0) {
      StringJoiner stringJoiner = new StringJoiner(", ");
      Object[] paramsValue = joinPoint.getArgs();
      for (int i = 0; i < paramsNames.length; i++) {
        stringJoiner.add(paramsNames[i] + " = " + paramsValue[i]);
      }
      outputLine = "(" + stringJoiner + ")";
    } else
      outputLine = "()";

    return new ClassMethodInformation(className, methodName, outputLine);
  }

  @Pointcut("within(@InfoLogs *)")
  public void getLabeledInfoLogs() {
  }

  @Pointcut("within(@DebugLogs *)")
  public void getLabeledDebugLogs() {
  }

  @Pointcut("execution(public * *(..))")
  public void getExecutionAllPublicMethods() {
  }

  @Pointcut("getLabeledInfoLogs() && getExecutionAllPublicMethods()")
  public void getExecutionAllPublicMethodsControllers() {
  }

  @Pointcut("getLabeledDebugLogs() && getExecutionAllPublicMethods()")
  public void getExecutionAllPublicMethodsServicesAndRepositories() {
  }

  @Before("getExecutionAllPublicMethodsControllers()")
  public void logBeforeExecutingControllerMethods(JoinPoint joinPoint) {

    ClassMethodInformation classMethodInformation = getClassMethodInformation(joinPoint);

    loggerConcurrentHashMap.getOrDefault(classMethodInformation.getClassName(), log)
            .info("start {}.{}{}", classMethodInformation.getClassName(), classMethodInformation.getMethodName(),
                    classMethodInformation.getOutputLine());
  }

  @AfterReturning(pointcut = "getExecutionAllPublicMethodsControllers()", returning = "result")
  public void logAfterReturningControllerResult(JoinPoint joinPoint, Object result) {

    ClassMethodInformation classMethodInformation = getClassMethodInformation(joinPoint);

    loggerConcurrentHashMap.getOrDefault(classMethodInformation.getClassName(), log)
            .info("finish {}.{}{} with result: {}", classMethodInformation.getClassName(),
                    classMethodInformation.getMethodName(), classMethodInformation.getOutputLine(), result);
  }

  @Before("getExecutionAllPublicMethodsServicesAndRepositories()")
  public void logBeforeExecutingServiceAndRepositoryMethods(JoinPoint joinPoint) {

    ClassMethodInformation classMethodInformation = getClassMethodInformation(joinPoint);

    loggerConcurrentHashMap.getOrDefault(classMethodInformation.getClassName(), log)
            .debug("start {}.{}{}", classMethodInformation.getClassName(), classMethodInformation.getMethodName(),
                    classMethodInformation.getOutputLine());
  }

  @AfterReturning(pointcut = "getExecutionAllPublicMethodsServicesAndRepositories()", returning = "result")
  public void logAfterReturningServiceAndRepositoriesResult(JoinPoint joinPoint, Object result) {

    ClassMethodInformation classMethodInformation = getClassMethodInformation(joinPoint);

    loggerConcurrentHashMap.getOrDefault(classMethodInformation.getClassName(), log)
            .debug("finish {}.{}{} with result: {}", classMethodInformation.getClassName(),
                    classMethodInformation.getMethodName(), classMethodInformation.getOutputLine(), result);
  }

  @AfterThrowing(pointcut = "getExecutionAllPublicMethodsControllers() || " +
          "getExecutionAllPublicMethodsServicesAndRepositories()", throwing = "ex")
  public void logThrowable(JoinPoint joinPoint, Throwable ex) {

    ClassMethodInformation classMethodInformation = getClassMethodInformation(joinPoint);

    loggerConcurrentHashMap.getOrDefault(classMethodInformation.getClassName(), log)
            .error("Exception {} was thrown while running method {}.{}{} with description: {}",
                    ex.getClass().getSimpleName(), classMethodInformation.getClassName(),
                    classMethodInformation.getMethodName(), classMethodInformation.getOutputLine(), ex.getMessage());
  }
}
