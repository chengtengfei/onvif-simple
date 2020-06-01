package com.github.chengtengfei.onvif.diagnostics.logging;

public interface Logger {
    String getName();

    boolean isTraceEnabled();

    void trace(String var1);

    void trace(String var1, Throwable var2);

    boolean isDebugEnabled();

    void debug(String var1);

    void debug(String var1, Throwable var2);

    boolean isInfoEnabled();

    void info(String var1);

    void info(String var1, Throwable var2);

    boolean isWarnEnabled();

    void warn(String var1);

    void warn(String var1, Throwable var2);

    boolean isErrorEnabled();

    void error(String var1);

    void error(String var1, Throwable var2);
}
