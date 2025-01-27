package kotlinx.coroutines;

import java.util.concurrent.locks.LockSupport;

/* loaded from: classes.dex */
public abstract class EventLoopImplPlatform extends EventLoop {
    protected abstract Thread getThread();

    protected final void unpark() {
        Thread thread = getThread();
        if (Thread.currentThread() != thread) {
            AbstractTimeSourceKt.getTimeSource();
            LockSupport.unpark(thread);
        }
    }
}
