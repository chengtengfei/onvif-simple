package space.fei.code.onvif.diagnostics.logging;

import java.util.logging.Level;

class JULLogger implements Logger {
    private final java.util.logging.Logger delegate;

    JULLogger(String name) {
        this.delegate = java.util.logging.Logger.getLogger(name);
    }



    public String getName() {
        return this.delegate.getName();
    }

    public boolean isTraceEnabled() {
        return this.isEnabled(Level.FINER);
    }

    public void trace(String msg) {
        this.log(Level.FINER, msg);
    }

    public void trace(String msg, Throwable t) {
        this.log(Level.FINER, msg, t);
    }

    public boolean isDebugEnabled() {
        return this.isEnabled(Level.FINE);
    }

    public void debug(String msg) {
        this.log(Level.FINE, msg);
    }

    public void debug(String msg, Throwable t) {
        this.log(Level.FINE, msg, t);
    }

    public boolean isInfoEnabled() {
        return this.delegate.isLoggable(Level.INFO);
    }

    public void info(String msg) {
        this.log(Level.INFO, msg);
    }

    public void info(String msg, Throwable t) {
        this.log(Level.INFO, msg, t);
    }

    public boolean isWarnEnabled() {
        return this.delegate.isLoggable(Level.WARNING);
    }

    public void warn(String msg) {
        this.log(Level.WARNING, msg);
    }

    public void warn(String msg, Throwable t) {
        this.log(Level.WARNING, msg, t);
    }

    public boolean isErrorEnabled() {
        return this.delegate.isLoggable(Level.SEVERE);
    }

    public void error(String msg) {
        this.log(Level.SEVERE, msg);
    }

    public void error(String msg, Throwable t) {
        this.log(Level.SEVERE, msg, t);
    }

    private boolean isEnabled(Level level) {
        return this.delegate.isLoggable(level);
    }

    private void log(Level level, String msg) {
        this.delegate.log(level, msg);
    }

    public void log(Level level, String msg, Throwable t) {
        this.delegate.log(level, msg, t);
    }
}
