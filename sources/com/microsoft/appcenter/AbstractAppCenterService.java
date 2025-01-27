package com.microsoft.appcenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.async.AppCenterFuture;
import com.microsoft.appcenter.utils.async.DefaultAppCenterFuture;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;

/* loaded from: classes.dex */
public abstract class AbstractAppCenterService implements AppCenterService {
    protected Channel mChannel;
    private AppCenterHandler mHandler;

    protected abstract void applyEnabledState(boolean z);

    protected Channel.GroupListener getChannelListener() {
        return null;
    }

    protected String getEnabledPreferenceKey() {
        return "enabled_" + getServiceName();
    }

    protected abstract String getGroupName();

    protected abstract String getLoggerTag();

    protected int getTriggerCount() {
        return 50;
    }

    protected long getTriggerInterval() {
        return 3000L;
    }

    protected int getTriggerMaxParallelRequests() {
        return 3;
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public boolean isAppSecretRequired() {
        return true;
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public synchronized boolean isInstanceEnabled() {
        return SharedPreferencesManager.getBoolean(getEnabledPreferenceKey(), true);
    }

    protected synchronized AppCenterFuture isInstanceEnabledAsync() {
        final DefaultAppCenterFuture defaultAppCenterFuture;
        defaultAppCenterFuture = new DefaultAppCenterFuture();
        postAsyncGetter(new Runnable() { // from class: com.microsoft.appcenter.AbstractAppCenterService.1
            @Override // java.lang.Runnable
            public void run() {
                defaultAppCenterFuture.complete(Boolean.TRUE);
            }
        }, defaultAppCenterFuture, Boolean.FALSE);
        return defaultAppCenterFuture;
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
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
    }

    @Override // com.microsoft.appcenter.utils.ApplicationLifecycleListener.ApplicationLifecycleCallbacks
    public void onApplicationEnterBackground() {
    }

    @Override // com.microsoft.appcenter.utils.ApplicationLifecycleListener.ApplicationLifecycleCallbacks
    public void onApplicationEnterForeground() {
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public void onConfigurationUpdated(String str, String str2) {
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public synchronized void onStarted(Context context, Channel channel, String str, String str2, boolean z) {
        try {
            String groupName = getGroupName();
            boolean isInstanceEnabled = isInstanceEnabled();
            if (groupName != null) {
                channel.removeGroup(groupName);
                if (isInstanceEnabled) {
                    channel.addGroup(groupName, getTriggerCount(), getTriggerInterval(), getTriggerMaxParallelRequests(), null, getChannelListener());
                } else {
                    channel.clear(groupName);
                }
            }
            this.mChannel = channel;
            applyEnabledState(isInstanceEnabled);
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public final synchronized void onStarting(AppCenterHandler appCenterHandler) {
        this.mHandler = appCenterHandler;
    }

    protected synchronized void post(Runnable runnable) {
        post(runnable, null, null);
    }

    protected synchronized boolean post(final Runnable runnable, Runnable runnable2, final Runnable runnable3) {
        AppCenterHandler appCenterHandler = this.mHandler;
        if (appCenterHandler != null) {
            appCenterHandler.post(new Runnable() { // from class: com.microsoft.appcenter.AbstractAppCenterService.4
                @Override // java.lang.Runnable
                public void run() {
                    Runnable runnable4;
                    if (AbstractAppCenterService.this.isInstanceEnabled()) {
                        runnable4 = runnable;
                    } else {
                        runnable4 = runnable3;
                        if (runnable4 == null) {
                            AppCenterLog.info("AppCenter", AbstractAppCenterService.this.getServiceName() + " service disabled, discarding calls.");
                            return;
                        }
                    }
                    runnable4.run();
                }
            }, runnable2);
            return true;
        }
        AppCenterLog.error("AppCenter", getServiceName() + " needs to be started before it can be used.");
        return false;
    }

    protected synchronized void postAsyncGetter(final Runnable runnable, final DefaultAppCenterFuture defaultAppCenterFuture, final Object obj) {
        Runnable runnable2 = new Runnable() { // from class: com.microsoft.appcenter.AbstractAppCenterService.5
            @Override // java.lang.Runnable
            public void run() {
                defaultAppCenterFuture.complete(obj);
            }
        };
        if (!post(new Runnable() { // from class: com.microsoft.appcenter.AbstractAppCenterService.6
            @Override // java.lang.Runnable
            public void run() {
                runnable.run();
            }
        }, runnable2, runnable2)) {
            runnable2.run();
        }
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public synchronized void setInstanceEnabled(boolean z) {
        try {
            if (z == isInstanceEnabled()) {
                AppCenterLog.info(getLoggerTag(), String.format("%s service has already been %s.", getServiceName(), z ? "enabled" : "disabled"));
                return;
            }
            String groupName = getGroupName();
            Channel channel = this.mChannel;
            if (channel != null && groupName != null) {
                if (z) {
                    channel.addGroup(groupName, getTriggerCount(), getTriggerInterval(), getTriggerMaxParallelRequests(), null, getChannelListener());
                } else {
                    channel.clear(groupName);
                    this.mChannel.removeGroup(groupName);
                }
            }
            SharedPreferencesManager.putBoolean(getEnabledPreferenceKey(), z);
            AppCenterLog.info(getLoggerTag(), String.format("%s service has been %s.", getServiceName(), z ? "enabled" : "disabled"));
            if (this.mChannel != null) {
                applyEnabledState(z);
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
