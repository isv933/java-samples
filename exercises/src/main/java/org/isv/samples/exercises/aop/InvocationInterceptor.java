package org.isv.samples.exercises.aop;

import java.lang.reflect.Method;

public interface InvocationInterceptor {
    Object processInterception(Object target, Method method, Object[] args, MethodInvocation originalCall) throws Throwable;
}
