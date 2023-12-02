package jie.runtime.rpc;

import java.io.PrintStream;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * 表示远程调用服务执行期间发生的错误
 *
 * @author jiegg
 */
public class RpcException extends RuntimeException {

    //region --字段--
    private String stackTrace;
    private String source;
    //endregion

    //region --属性--
    public void setStackTrace(String value) {
        this.stackTrace = value;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String value) {
        this.source = value;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link RpcException} 类的新实例, 其详细消息为 {@code null}. 原因没有初始化, 并且可能随后通过调用 {@link #initCause(Throwable)} 初始化
     */
    public RpcException() {
    }

    /**
     * 使用指定的详细信息消息初始化 {@link RpcException} 类的新实例. 原因没有初始化, 并且可能随后通过调用 {@link #initCause(Throwable)} 初始化
     *
     * @param message 详细信息, 详细信息将通过 {@link #getMessage()} 方法保存以供以后检索
     */
    public RpcException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细信息和原因初始化 {@link RpcException} 类的新实例
     * <p>请注意，与{@code cause}关联的详细消息是<i>而不是</i>，它会自动并入这个异常的详细消息中</p>
     *
     * @param message 详细信息, 详细信息将通过 {@link #getMessage()} 方法保存以供以后检索
     * @param cause   原因, 通过{@link #getCause()}方法保存以供以后检索 (允许<tt>null</tt>, 表示原因不存在或未知)
     */
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 初始化 {@link RpcException} 类的新实例, 带有指定的原因和一个详细消息
     * <tt>(cause==null ?null: cause. tostring ())</tt>
     * (通常包含<tt>cause</tt>的类和详细消息). 此构造函数对于这个异常非常有用, 这些异常只不过是其他可抛出对象的包装器.
     *
     * @param cause 原因, 通过{@link #getCause()}方法保存以供以后检索 (允许<tt>null</tt>, 表示原因不存在或未知)
     */
    public RpcException(Throwable cause) {
        super(cause);
    }
    //endregion

    /**
     * Returns a short description of this throwable.
     * The result is the concatenation of:
     * <ul>
     * <li> the {@linkplain Class#getName() name} of the class of this object
     * <li> ": " (a colon and a space)
     * <li> the result of invoking this object's {@link #getLocalizedMessage}
     *      method
     * </ul>
     * If {@code getLocalizedMessage} returns {@code null}, then just
     * the class name is returned.
     *
     * @return a string representation of this throwable.
     */
    @Override
    public String toString() {
        String s = this.getSource();
        String message = this.getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }

    /**
     * Prints this throwable and its backtrace to the specified print stream.
     *
     * @param s {@code PrintStream} to use for output
     */
    @Override
    public void printStackTrace(PrintStream s) {
        this.printStackTrace(new WrappedPrintStream(s));
    }

    private void printStackTrace(PrintStreamOrWriter s) {
        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        Set<Throwable> dejaVu =
                Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
        dejaVu.add(this);

        synchronized (s.lock()) {
            // Print our stack trace
            s.println(this);
            s.println(this.stackTrace);

            // Print cause, if any
            Throwable ourCause = getCause();
            if (ourCause instanceof RpcException)
                ((RpcException) ourCause).printStackTrace(s);
        }
    }

    //region --内部类--
    private static class WrappedPrintStream extends PrintStreamOrWriter {
        private final PrintStream printStream;

        WrappedPrintStream(PrintStream printStream) {
            this.printStream = printStream;
        }

        Object lock() {
            return printStream;
        }

        void println(Object o) {
            printStream.println(o);
        }
    }

    private abstract static class PrintStreamOrWriter {
        /**
         * Returns the object to be locked when using this StreamOrWriter
         */
        abstract Object lock();

        /**
         * Prints the specified string as a line on this StreamOrWriter
         */
        abstract void println(Object o);
    }
    //endregion
}
