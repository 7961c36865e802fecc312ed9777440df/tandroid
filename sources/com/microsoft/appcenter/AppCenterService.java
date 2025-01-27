package com.microsoft.appcenter;

import android.app.Application;
import android.content.Context;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.utils.ApplicationLifecycleListener;
import java.util.Map;

/* loaded from: classes.dex */
public interface AppCenterService extends Application.ActivityLifecycleCallbacks, ApplicationLifecycleListener.ApplicationLifecycleCallbacks {
    Map getLogFactories();

    String getServiceName();

    boolean isAppSecretRequired();

    boolean isInstanceEnabled();

    void onConfigurationUpdated(String str, String str2);

    void onStarted(Context context, Channel channel, String str, String str2, boolean z);

    void onStarting(AppCenterHandler appCenterHandler);

    void setInstanceEnabled(boolean z);
}
