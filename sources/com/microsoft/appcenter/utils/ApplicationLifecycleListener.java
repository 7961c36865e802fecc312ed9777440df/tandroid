package com.microsoft.appcenter.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes.dex */
public class ApplicationLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private Handler mHandler;
    private int mStartedCounter = 0;
    private int mResumedCounter = 0;
    private boolean mPauseSent = true;
    private boolean mStopSent = true;
    private final Set mLifecycleCallbacks = new CopyOnWriteArraySet();
    private Runnable mDelayedPauseRunnable = new Runnable() { // from class: com.microsoft.appcenter.utils.ApplicationLifecycleListener.1
        @Override // java.lang.Runnable
        public void run() {
            ApplicationLifecycleListener.this.dispatchPauseIfNeeded();
            ApplicationLifecycleListener.this.dispatchStopIfNeeded();
        }
    };

    public interface ApplicationLifecycleCallbacks {
        void onApplicationEnterBackground();

        void onApplicationEnterForeground();
    }

    public ApplicationLifecycleListener(Handler handler) {
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPauseIfNeeded() {
        if (this.mResumedCounter == 0) {
            this.mPauseSent = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchStopIfNeeded() {
        if (this.mStartedCounter == 0 && this.mPauseSent) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                ((ApplicationLifecycleCallbacks) it.next()).onApplicationEnterBackground();
            }
            this.mStopSent = true;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        if (this.mStartedCounter == 0) {
            this.mStopSent = false;
        }
        int i = this.mResumedCounter;
        if (i == 0) {
            this.mPauseSent = false;
        }
        int max = Math.max(i - 1, 0);
        this.mResumedCounter = max;
        if (max == 0) {
            this.mHandler.postDelayed(this.mDelayedPauseRunnable, 700L);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        int i = this.mResumedCounter + 1;
        this.mResumedCounter = i;
        if (i == 1) {
            if (this.mPauseSent) {
                this.mPauseSent = false;
            } else {
                this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        int i = this.mStartedCounter + 1;
        this.mStartedCounter = i;
        if (i == 1 && this.mStopSent) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                ((ApplicationLifecycleCallbacks) it.next()).onApplicationEnterForeground();
            }
            this.mStopSent = false;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        this.mStartedCounter = Math.max(this.mStartedCounter - 1, 0);
        dispatchStopIfNeeded();
    }

    public void registerApplicationLifecycleCallbacks(ApplicationLifecycleCallbacks applicationLifecycleCallbacks) {
        this.mLifecycleCallbacks.add(applicationLifecycleCallbacks);
    }
}
