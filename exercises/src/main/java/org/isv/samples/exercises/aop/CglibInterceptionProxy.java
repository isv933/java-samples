package org.isv.samples.exercises.aop;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class CglibInterceptionProxy implements MethodInterceptor {
    private final InvocationInterceptor[] interceptors;

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        var state = InvocationState.builder()
                .target(target).method(method).args(args).interceptors(interceptors).proxy(proxy).build();

        var callProxy = new InvocationProxyCall(state);
        return callProxy.process();
    }

    private record InvocationProxyCall(InvocationState state) implements MethodInvocation {
        @Override
        public Object process() throws Throwable {
            if (!state.hasInterceptor()) {
                return state.getProxy().invokeSuper(state.getTarget(), state.getArgs());
            }

            return state.nextInterceptor().processInterception(state.getTarget(), state.getMethod(),
                    state.getArgs(), this);
        }
    }

    @Builder
    @Data
    private static class InvocationState {
        private final InvocationInterceptor[] interceptors;
        private final Object target;
        private final Method method;
        private final Object[] args;
        private final MethodProxy proxy;
        private int interceptorIndex;

        public boolean hasInterceptor() {
            return interceptorIndex < interceptors.length;
        }

        public InvocationInterceptor nextInterceptor() {
            return interceptors[interceptorIndex++];
        }
    }


}
