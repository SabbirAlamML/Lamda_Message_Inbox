package com.singtel.inbox.app;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by Dongwu on 13/1/2016.
 */
public class ExceptionMethodInterceptor implements MethodInterceptor {
    private static final Logger LOGGER = Logger.getLogger(ExceptionMethodInterceptor.class);

    private ExceptionMethodInterceptor() {
    }

    public static ExceptionMethodInterceptor exception() {
        return new ExceptionMethodInterceptor();
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        final String methodName = getMethodName(methodInvocation);
        try {
            LOGGER.debug(String.format("method(%s) call with: %s.", methodName, getArgs(methodInvocation)));
            final Object result = methodInvocation.proceed();
            LOGGER.debug(String.format("method(%s) return with: %s.", methodName, result));
            return result;
        } catch (Exception e) {
            LOGGER.error(String.format("method(%s) error with: %s.", methodName, e.getCause()), e);
            throw e;
        }

    }

    private Object getArgs(MethodInvocation methodInvocation) {
        final List<String> args = Lists.newArrayList();
        if (methodInvocation.getArguments() != null) {
            for (int i = 0; i < methodInvocation.getArguments().length; i++) {
                final Object arg = methodInvocation.getArguments()[i];
                args.add(arg == null ? "null" : arg.toString());
            }
        }
        return Joiner.on(",").join(args);
    }

    private String getMethodName(MethodInvocation methodInvocation) {
        return String.format("%s-(%s)", methodInvocation.getMethod().getDeclaringClass().getName(), methodInvocation.getMethod());
    }
}
