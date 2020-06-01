package com.github.chengtengfei.onvif.diagnostics.logging;

import com.github.chengtengfei.onvif.assertions.Assertions;
import com.github.chengtengfei.onvif.exception.LogException;

public final class Loggers {
    public static final String PREFIX = "com.github.chengtengfei";
    private static final boolean USE_SLF4J = shouldUseSLF4J();


    public static Logger getLogger(Class<?> aClass) {
        if (!aClass.getName().startsWith(PREFIX)) {
            throw new LogException("Error suffix " + aClass.getName());
        }
        return (Logger)(USE_SLF4J ? new SLF4JLogger(aClass) : new JULLogger(aClass.getName()));
    }

    public static Logger getLogger(String suffix) {
        Assertions.notNull("suffix", suffix);
        if (!suffix.startsWith(".") && !suffix.endsWith(".")) {
            String name = "space.fei.code." + suffix;
            return (Logger)(USE_SLF4J ? new SLF4JLogger(name) : new JULLogger(name));
        } else {
            throw new LogException("The suffix can not start or end with a '.'");
        }
    }

    private Loggers() {
    }

    private static boolean shouldUseSLF4J() {
        try {
            Class.forName("org.slf4j.Logger");
            return true;
        } catch (ClassNotFoundException var1) {
            return false;
        }
    }
}
