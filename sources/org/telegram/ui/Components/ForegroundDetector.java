package org.telegram.ui.Components;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.recyclerview.widget.RecyclerView$ItemAnimator$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

/* loaded from: classes.dex */
public abstract class ForegroundDetector implements Application.ActivityLifecycleCallbacks {
    private static ForegroundDetector Instance;
    private int refs;
    private boolean wasInBackground = true;
    private long enterBackgroundTime = 0;
    private CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();

    /* loaded from: classes3.dex */
    public interface Listener {
    }

    public ForegroundDetector(Application application) {
        Instance = this;
        application.registerActivityLifecycleCallbacks(this);
    }

    public static ForegroundDetector getInstance() {
        return Instance;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public boolean isBackground() {
        return this.refs == 0;
    }

    public boolean isForeground() {
        return this.refs > 0;
    }

    public boolean isWasInBackground(boolean z) {
        if (z && Build.VERSION.SDK_INT >= 21 && SystemClock.elapsedRealtime() - this.enterBackgroundTime < 200) {
            this.wasInBackground = false;
        }
        return this.wasInBackground;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        int i = this.refs + 1;
        this.refs = i;
        if (i == 1) {
            if (SystemClock.elapsedRealtime() - this.enterBackgroundTime < 200) {
                this.wasInBackground = false;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("switch to foreground");
            }
            Iterator<Listener> it = this.listeners.iterator();
            while (it.hasNext()) {
                RecyclerView$ItemAnimator$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                try {
                    throw null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        int i = this.refs - 1;
        this.refs = i;
        if (i == 0) {
            this.enterBackgroundTime = SystemClock.elapsedRealtime();
            this.wasInBackground = true;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("switch to background");
            }
            Iterator<Listener> it = this.listeners.iterator();
            while (it.hasNext()) {
                RecyclerView$ItemAnimator$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                try {
                    throw null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void resetBackgroundVar() {
        this.wasInBackground = false;
    }
}
