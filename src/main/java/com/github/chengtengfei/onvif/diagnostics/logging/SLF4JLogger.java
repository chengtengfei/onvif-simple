package com.github.chengtengfei.onvif.diagnostics.logging;

import org.slf4j.LoggerFactory;

class SLF4JLogger implements Logger {
    private final org.slf4j.Logger delegate;

    SLF4JLogger(String name) {
        this.delegate = LoggerFactory.getLogger(name);
    }

    SLF4JLogger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }

    public String getName() {
        return this.delegate.getName();
    }

    public boolean isTraceEnabled() {
        return this.delegate.isTraceEnabled();
    }

    public void trace(String msg) {
        this.delegate.trace(msg);
    }

    public void trace(String msg, Throwable t) {
        this.delegate.trace(msg, t);
    }

    public boolean isDebugEnabled() {
        return this.delegate.isDebugEnabled();
    }

    public void debug(String msg) {
        this.delegate.debug(msg);
    }

    public void debug(String msg, Throwable t) {
        this.delegate.debug(msg, t);
    }

    public boolean isInfoEnabled() {
        return this.delegate.isInfoEnabled();
    }

    public void info(String msg) {
        this.delegate.info(msg);
    }

    public void info(String msg, Throwable t) {
        this.delegate.info(msg, t);
    }

    public boolean isWarnEnabled() {
        return this.delegate.isWarnEnabled();
    }

    public void warn(String msg) {
        this.delegate.warn(msg);
    }

    public void warn(String msg, Throwable t) {
        this.delegate.warn(msg, t);
    }

    public boolean isErrorEnabled() {
        return this.delegate.isErrorEnabled();
    }

    public void error(String msg) {
        this.delegate.error(msg);
    }

    public void error(String msg, Throwable t) {
        this.delegate.error(msg, t);
    }
}
