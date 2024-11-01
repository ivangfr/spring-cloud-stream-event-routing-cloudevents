package com.ivanfranchin.producerservice.config;

import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.messaging.Message;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class NativeRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        Method method = ReflectionUtils.findMethod(Message.class, "getHeaders");
        hints.reflection().registerMethod(method, ExecutableMode.INVOKE);
    }
}