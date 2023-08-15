package com.microsoft.appcenter.analytics;

import android.provider.Settings;
import com.microsoft.appcenter.channel.AbstractChannelListener;
import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.one.AppExtension;
import com.microsoft.appcenter.ingestion.models.one.CommonSchemaLog;
import com.microsoft.appcenter.ingestion.models.one.DeviceExtension;
import com.microsoft.appcenter.ingestion.models.one.UserExtension;
/* loaded from: classes.dex */
public class PropertyConfigurator extends AbstractChannelListener {
    private String mAppLocale;
    private String mAppName;
    private String mAppVersion;
    private boolean mDeviceIdEnabled;
    private final AnalyticsTransmissionTarget mTransmissionTarget;
    private String mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PropertyConfigurator(AnalyticsTransmissionTarget analyticsTransmissionTarget) {
        new EventProperties();
        this.mTransmissionTarget = analyticsTransmissionTarget;
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public void onPreparingLog(Log log, String str) {
        if (shouldOverridePartAProperties(log)) {
            CommonSchemaLog commonSchemaLog = (CommonSchemaLog) log;
            AppExtension app = commonSchemaLog.getExt().getApp();
            UserExtension user = commonSchemaLog.getExt().getUser();
            DeviceExtension device = commonSchemaLog.getExt().getDevice();
            String str2 = this.mAppName;
            if (str2 != null) {
                app.setName(str2);
            } else {
                AnalyticsTransmissionTarget analyticsTransmissionTarget = this.mTransmissionTarget;
                while (true) {
                    analyticsTransmissionTarget = analyticsTransmissionTarget.mParentTarget;
                    if (analyticsTransmissionTarget != null) {
                        String appName = analyticsTransmissionTarget.getPropertyConfigurator().getAppName();
                        if (appName != null) {
                            app.setName(appName);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            String str3 = this.mAppVersion;
            if (str3 != null) {
                app.setVer(str3);
            } else {
                AnalyticsTransmissionTarget analyticsTransmissionTarget2 = this.mTransmissionTarget;
                while (true) {
                    analyticsTransmissionTarget2 = analyticsTransmissionTarget2.mParentTarget;
                    if (analyticsTransmissionTarget2 != null) {
                        String appVersion = analyticsTransmissionTarget2.getPropertyConfigurator().getAppVersion();
                        if (appVersion != null) {
                            app.setVer(appVersion);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            String str4 = this.mAppLocale;
            if (str4 != null) {
                app.setLocale(str4);
            } else {
                AnalyticsTransmissionTarget analyticsTransmissionTarget3 = this.mTransmissionTarget;
                while (true) {
                    analyticsTransmissionTarget3 = analyticsTransmissionTarget3.mParentTarget;
                    if (analyticsTransmissionTarget3 != null) {
                        String appLocale = analyticsTransmissionTarget3.getPropertyConfigurator().getAppLocale();
                        if (appLocale != null) {
                            app.setLocale(appLocale);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            String str5 = this.mUserId;
            if (str5 != null) {
                user.setLocalId(str5);
            } else {
                AnalyticsTransmissionTarget analyticsTransmissionTarget4 = this.mTransmissionTarget;
                while (true) {
                    analyticsTransmissionTarget4 = analyticsTransmissionTarget4.mParentTarget;
                    if (analyticsTransmissionTarget4 != null) {
                        String userId = analyticsTransmissionTarget4.getPropertyConfigurator().getUserId();
                        if (userId != null) {
                            user.setLocalId(userId);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (this.mDeviceIdEnabled) {
                String string = Settings.Secure.getString(this.mTransmissionTarget.mContext.getContentResolver(), "android_id");
                device.setLocalId("a:" + string);
            }
        }
    }

    private boolean shouldOverridePartAProperties(Log log) {
        if (log instanceof CommonSchemaLog) {
            Object tag = log.getTag();
            AnalyticsTransmissionTarget analyticsTransmissionTarget = this.mTransmissionTarget;
            if (tag == analyticsTransmissionTarget && analyticsTransmissionTarget.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    private String getAppName() {
        return this.mAppName;
    }

    private String getAppVersion() {
        return this.mAppVersion;
    }

    private String getAppLocale() {
        return this.mAppLocale;
    }

    private String getUserId() {
        return this.mUserId;
    }
}