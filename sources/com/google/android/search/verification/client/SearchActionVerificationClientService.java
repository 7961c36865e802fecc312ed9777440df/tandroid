package com.google.android.search.verification.client;

import android.R;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.android.search.verification.api.ISearchActionVerificationService;

/* loaded from: classes.dex */
public abstract class SearchActionVerificationClientService extends IntentService {
    private static final int CONNECTION_TIMEOUT_IN_MS = 1000;
    public static final String EXTRA_INTENT = "SearchActionVerificationClientExtraIntent";
    private static final long MS_TO_NS = 1000000;
    private static final String NOTIFICATION_CHANNEL_ID = "Assistant_verifier";
    private static final int NOTIFICATION_ID = 10000;
    private static final String REMOTE_ASSISTANT_GO_SERVICE_ACTION = "com.google.android.apps.assistant.go.verification.VERIFICATION_SERVICE";
    private static final String REMOTE_GSA_SERVICE_ACTION = "com.google.android.googlequicksearchbox.SEARCH_ACTION_VERIFICATION_SERVICE";
    private static final String SEND_MESSAGE_ERROR_MESSAGE = "com.google.android.voicesearch.extra.ERROR_MESSAGE";
    private static final String SEND_MESSAGE_RESULT_RECEIVER = "com.google.android.voicesearch.extra.SEND_MESSAGE_RESULT_RECEIVER";
    private static final String TAG = "SAVerificationClientS";
    private static final int TIME_TO_SLEEP_IN_MS = 50;
    private final Intent assistantGoServiceIntent;
    private SearchActionVerificationServiceConnection assistantGoVerificationServiceConnection;
    private final long connectionTimeout;
    private final boolean dbg;
    private final Intent gsaServiceIntent;
    private SearchActionVerificationServiceConnection searchActionVerificationServiceConnection;

    class SearchActionVerificationServiceConnection implements ServiceConnection {
        private ISearchActionVerificationService iRemoteService;

        SearchActionVerificationServiceConnection() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isConnected() {
            return this.iRemoteService != null;
        }

        public ISearchActionVerificationService getRemoteService() {
            return this.iRemoteService;
        }

        public boolean isVerified(Intent intent, Bundle bundle) {
            ISearchActionVerificationService iSearchActionVerificationService = this.iRemoteService;
            return iSearchActionVerificationService != null && iSearchActionVerificationService.isSearchAction(intent, bundle);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (SearchActionVerificationClientService.this.dbg) {
                Log.d(SearchActionVerificationClientService.TAG, "onServiceConnected");
            }
            this.iRemoteService = ISearchActionVerificationService.Stub.asInterface(iBinder);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            this.iRemoteService = null;
            if (SearchActionVerificationClientService.this.dbg) {
                Log.d(SearchActionVerificationClientService.TAG, "onServiceDisconnected");
            }
        }
    }

    public SearchActionVerificationClientService() {
        super("SearchActionVerificationClientService");
        Intent intent = new Intent(REMOTE_GSA_SERVICE_ACTION).setPackage("com.google.android.googlequicksearchbox");
        this.gsaServiceIntent = intent;
        Intent intent2 = new Intent(REMOTE_ASSISTANT_GO_SERVICE_ACTION).setPackage("com.google.android.apps.assistant");
        this.assistantGoServiceIntent = intent2;
        this.dbg = isDebugMode();
        if (isTestingMode()) {
            intent.setPackage("com.google.verificationdemo.fakeverification");
            intent2.setPackage("com.google.verificationdemo.fakeverification");
        }
        this.connectionTimeout = getConnectionTimeout();
    }

    private void createChannel() {
        Object systemService;
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, getApplicationContext().getResources().getString(R$string.google_assistant_verification_channel_name), 2);
        notificationChannel.enableVibration(false);
        notificationChannel.enableLights(false);
        notificationChannel.setShowBadge(false);
        systemService = getApplicationContext().getSystemService((Class<Object>) NotificationManager.class);
        ((NotificationManager) systemService).createNotificationChannel(notificationChannel);
    }

    private boolean installedServicesConnected() {
        boolean isPackageInstalled = isPackageInstalled("com.google.android.googlequicksearchbox");
        boolean z = !isPackageInstalled || this.searchActionVerificationServiceConnection.isConnected();
        if (this.dbg) {
            Log.d(TAG, String.format("GSA app %s installed: %s connected %s", "com.google.android.googlequicksearchbox", Boolean.valueOf(isPackageInstalled), Boolean.valueOf(this.searchActionVerificationServiceConnection.isConnected())));
        }
        boolean isPackageInstalled2 = isPackageInstalled("com.google.android.apps.assistant");
        boolean z2 = !isPackageInstalled2 || this.assistantGoVerificationServiceConnection.isConnected();
        if (this.dbg) {
            Log.d(TAG, String.format("AssistantGo app %s installed: %s connected %s", "com.google.android.apps.assistant", Boolean.valueOf(isPackageInstalled2), Boolean.valueOf(this.assistantGoVerificationServiceConnection.isConnected())));
        }
        return z && z2;
    }

    private boolean isDebugMode() {
        return isTestingMode() || !"user".equals(Build.TYPE);
    }

    private boolean isPackageInstalled(String str) {
        ApplicationInfo applicationInfo;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(str, 0);
            if (packageInfo != null && (applicationInfo = packageInfo.applicationInfo) != null) {
                if (applicationInfo.enabled) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, String.format("Couldn't find package name %s", str), e);
            return false;
        }
    }

    private boolean isPackageSafe(String str) {
        return isPackageInstalled(str) && (isDebugMode() || SearchActionVerificationClientUtil.isPackageGoogleSigned(this, str));
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0104  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean maybePerformActionIfVerified(String str, Intent intent, SearchActionVerificationServiceConnection searchActionVerificationServiceConnection) {
        String valueOf;
        String str2;
        String str3;
        String message;
        boolean isVerified;
        if (!str.equals("com.google.android.googlequicksearchbox") && !str.equals("com.google.android.apps.assistant")) {
            if (this.dbg) {
                Log.d(TAG, String.format("Unsupported package %s for verification.", str));
            }
            return false;
        }
        if (!isDebugMode() && !SearchActionVerificationClientUtil.isPackageGoogleSigned(this, str)) {
            if (this.dbg) {
                Log.d(TAG, String.format("Cannot verify the intent with package %s in unsafe mode.", str));
            }
            return false;
        }
        if (!intent.hasExtra(EXTRA_INTENT)) {
            if (this.dbg) {
                String valueOf2 = String.valueOf(intent);
                StringBuilder sb = new StringBuilder(valueOf2.length() + 28);
                sb.append("No extra, nothing to check: ");
                sb.append(valueOf2);
                Log.d(TAG, sb.toString());
            }
            return false;
        }
        Intent intent2 = (Intent) intent.getParcelableExtra(EXTRA_INTENT);
        if (this.dbg) {
            SearchActionVerificationClientUtil.logIntentWithExtras(intent2);
        }
        if (searchActionVerificationServiceConnection.isConnected()) {
            try {
                Log.i(TAG, String.format("%s Service API version: %s", str, Integer.valueOf(searchActionVerificationServiceConnection.getRemoteService().getVersion())));
                Bundle bundle = new Bundle();
                isVerified = searchActionVerificationServiceConnection.isVerified(intent2, bundle);
                performAction(intent2, isVerified, bundle);
                message = "";
            } catch (RemoteException e) {
                e = e;
                valueOf = String.valueOf(e.getMessage());
                str2 = "Remote exception: ";
                if (valueOf.length() == 0) {
                    str3 = new String("Remote exception: ");
                    Log.e(TAG, str3);
                    message = e.getMessage();
                    isVerified = false;
                    if (intent2.hasExtra(SEND_MESSAGE_RESULT_RECEIVER)) {
                    }
                    return isVerified;
                }
                str3 = str2.concat(valueOf);
                Log.e(TAG, str3);
                message = e.getMessage();
                isVerified = false;
                if (intent2.hasExtra(SEND_MESSAGE_RESULT_RECEIVER)) {
                }
                return isVerified;
            } catch (Exception e2) {
                e = e2;
                valueOf = String.valueOf(e.getMessage());
                str2 = "Exception: ";
                if (valueOf.length() == 0) {
                    str3 = new String("Exception: ");
                    Log.e(TAG, str3);
                    message = e.getMessage();
                    isVerified = false;
                    if (intent2.hasExtra(SEND_MESSAGE_RESULT_RECEIVER)) {
                    }
                    return isVerified;
                }
                str3 = str2.concat(valueOf);
                Log.e(TAG, str3);
                message = e.getMessage();
                isVerified = false;
                if (intent2.hasExtra(SEND_MESSAGE_RESULT_RECEIVER)) {
                }
                return isVerified;
            }
            if (intent2.hasExtra(SEND_MESSAGE_RESULT_RECEIVER)) {
                ResultReceiver resultReceiver = (ResultReceiver) intent2.getExtras().getParcelable(SEND_MESSAGE_RESULT_RECEIVER);
                Bundle bundle2 = new Bundle();
                bundle2.putString(SEND_MESSAGE_ERROR_MESSAGE, message);
                resultReceiver.send(isVerified ? 0 : -1, bundle2);
            }
            return isVerified;
        }
        Object[] objArr = {str, intent};
        message = "VerificationService is not connected to %s, unable to check intent: %s";
        Log.e(TAG, String.format("VerificationService is not connected to %s, unable to check intent: %s", objArr));
        isVerified = false;
        if (intent2.hasExtra(SEND_MESSAGE_RESULT_RECEIVER)) {
        }
        return isVerified;
    }

    public long getConnectionTimeout() {
        return 1000L;
    }

    public boolean isTestingMode() {
        return false;
    }

    @Override // android.app.IntentService, android.app.Service
    public final void onCreate() {
        if (this.dbg) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate();
        this.searchActionVerificationServiceConnection = new SearchActionVerificationServiceConnection();
        if (isPackageSafe("com.google.android.googlequicksearchbox")) {
            bindService(this.gsaServiceIntent, this.searchActionVerificationServiceConnection, 1);
        }
        this.assistantGoVerificationServiceConnection = new SearchActionVerificationServiceConnection();
        if (isPackageSafe("com.google.android.apps.assistant")) {
            bindService(this.assistantGoServiceIntent, this.assistantGoVerificationServiceConnection, 1);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            postForegroundNotification();
        }
    }

    @Override // android.app.IntentService, android.app.Service
    public final void onDestroy() {
        if (this.dbg) {
            Log.d(TAG, "onDestroy");
        }
        super.onDestroy();
        if (this.searchActionVerificationServiceConnection.isConnected()) {
            unbindService(this.searchActionVerificationServiceConnection);
        }
        if (this.assistantGoVerificationServiceConnection.isConnected()) {
            unbindService(this.assistantGoVerificationServiceConnection);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true);
        }
    }

    @Override // android.app.IntentService
    protected final void onHandleIntent(Intent intent) {
        String str;
        if (intent == null) {
            if (this.dbg) {
                Log.d(TAG, "Unable to verify null intent");
                return;
            }
            return;
        }
        long nanoTime = System.nanoTime();
        while (!installedServicesConnected() && System.nanoTime() - nanoTime < this.connectionTimeout * MS_TO_NS) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                if (this.dbg) {
                    String valueOf = String.valueOf(e);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 33);
                    sb.append("Unexpected InterruptedException: ");
                    sb.append(valueOf);
                    Log.d(TAG, sb.toString());
                }
            }
        }
        if (maybePerformActionIfVerified("com.google.android.googlequicksearchbox", intent, this.searchActionVerificationServiceConnection)) {
            str = "Verified the intent with GSA.";
        } else {
            Log.i(TAG, "Unable to verify the intent with GSA.");
            str = maybePerformActionIfVerified("com.google.android.apps.assistant", intent, this.assistantGoVerificationServiceConnection) ? "Verified the intent with Assistant Go." : "Unable to verify the intent with Assistant Go.";
        }
        Log.i(TAG, str);
    }

    public abstract void performAction(Intent intent, boolean z, Bundle bundle);

    protected void postForegroundNotification() {
        createChannel();
        startForeground(NOTIFICATION_ID, new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID).setGroup(NOTIFICATION_CHANNEL_ID).setContentTitle(getApplicationContext().getResources().getString(R$string.google_assistant_verification_notification_title)).setSmallIcon(R.drawable.ic_dialog_email).setPriority(-2).setVisibility(1).build());
    }
}
