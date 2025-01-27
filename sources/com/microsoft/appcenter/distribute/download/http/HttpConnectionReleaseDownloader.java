package com.microsoft.appcenter.distribute.download.http;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import com.microsoft.appcenter.distribute.PermissionUtils;
import com.microsoft.appcenter.distribute.R$string;
import com.microsoft.appcenter.distribute.ReleaseDetails;
import com.microsoft.appcenter.distribute.download.AbstractReleaseDownloader;
import com.microsoft.appcenter.distribute.download.ReleaseDownloader;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.AsyncTaskUtils;
import com.microsoft.appcenter.utils.NetworkStateHelper;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class HttpConnectionReleaseDownloader extends AbstractReleaseDownloader {
    private HttpConnectionCheckTask mCheckTask;
    private HttpConnectionDownloadFileTask mDownloadTask;
    private Notification.Builder mNotificationBuilder;
    private File mTargetFile;

    public HttpConnectionReleaseDownloader(Context context, ReleaseDetails releaseDetails, ReleaseDownloader.Listener listener) {
        super(context, releaseDetails, listener);
    }

    private void cancelProgressNotification() {
        getNotificationManager().cancel(getNotificationId());
    }

    private synchronized void check() {
        this.mCheckTask = (HttpConnectionCheckTask) AsyncTaskUtils.execute("AppCenterDistribute", new HttpConnectionCheckTask(this), new Void[0]);
    }

    private synchronized void downloadFile(File file) {
        if (this.mDownloadTask != null) {
            AppCenterLog.debug("AppCenterDistribute", "Downloading of " + file.getPath() + " is already in progress.");
            return;
        }
        Uri downloadUrl = this.mReleaseDetails.getDownloadUrl();
        AppCenterLog.debug("AppCenterDistribute", "Start downloading new release from " + downloadUrl);
        this.mDownloadTask = (HttpConnectionDownloadFileTask) AsyncTaskUtils.execute("AppCenterDistribute", new HttpConnectionDownloadFileTask(this, downloadUrl, file), new Void[0]);
    }

    private static int getNotificationId() {
        return HttpConnectionReleaseDownloader.class.getName().hashCode();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) this.mContext.getSystemService("notification");
    }

    private void removeFile(File file) {
        AppCenterLog.debug("AppCenterDistribute", "Removing downloaded file from " + file.getAbsolutePath());
        AsyncTaskUtils.execute("AppCenterDistribute", new HttpConnectionRemoveFileTask(file), new Void[0]);
    }

    static String[] requiredPermissions() {
        return (String[]) new ArrayList().toArray(new String[0]);
    }

    private void showProgressNotification(long j, long j2) {
        if (this.mReleaseDetails.isMandatoryUpdate()) {
            return;
        }
        Notification.Builder notificationBuilder = getNotificationBuilder();
        notificationBuilder.setContentTitle(this.mContext.getString(R$string.appcenter_distribute_downloading_update)).setSmallIcon(this.mContext.getApplicationInfo().icon).setProgress((int) (j2 / 1024), (int) (j / 1024), j2 <= 0);
        getNotificationManager().notify(getNotificationId(), notificationBuilder.build());
    }

    @Override // com.microsoft.appcenter.distribute.download.AbstractReleaseDownloader, com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public synchronized void cancel() {
        try {
            if (isCancelled()) {
                return;
            }
            super.cancel();
            HttpConnectionCheckTask httpConnectionCheckTask = this.mCheckTask;
            if (httpConnectionCheckTask != null) {
                httpConnectionCheckTask.cancel(true);
                this.mCheckTask = null;
            }
            HttpConnectionDownloadFileTask httpConnectionDownloadFileTask = this.mDownloadTask;
            if (httpConnectionDownloadFileTask != null) {
                httpConnectionDownloadFileTask.cancel(true);
                this.mDownloadTask = null;
            }
            String downloadedReleaseFilePath = getDownloadedReleaseFilePath();
            if (downloadedReleaseFilePath != null) {
                removeFile(new File(downloadedReleaseFilePath));
                SharedPreferencesManager.remove("Distribute.downloaded_release_file");
            }
            cancelProgressNotification();
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized String getDownloadedReleaseFilePath() {
        return SharedPreferencesManager.getString("Distribute.downloaded_release_file", null);
    }

    Notification.Builder getNotificationBuilder() {
        if (this.mNotificationBuilder == null) {
            this.mNotificationBuilder = new Notification.Builder(this.mContext);
        }
        return this.mNotificationBuilder;
    }

    File getTargetFile() {
        File externalFilesDir;
        if (this.mTargetFile == null && (externalFilesDir = this.mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)) != null) {
            this.mTargetFile = new File(externalFilesDir, this.mReleaseDetails.getReleaseHash() + ".apk");
        }
        return this.mTargetFile;
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public synchronized boolean isDownloading() {
        return this.mDownloadTask != null;
    }

    synchronized void onDownloadComplete(File file) {
        if (isCancelled()) {
            return;
        }
        cancelProgressNotification();
        if (this.mReleaseDetails.getSize() != file.length()) {
            this.mListener.onError("Downloaded file has incorrect size.");
            return;
        }
        String absolutePath = file.getAbsolutePath();
        setDownloadedReleaseFilePath(absolutePath);
        this.mListener.onComplete(Uri.parse("file://" + absolutePath));
    }

    synchronized void onDownloadError(String str) {
        if (isCancelled()) {
            return;
        }
        cancelProgressNotification();
        this.mListener.onError(str);
    }

    synchronized void onDownloadProgress(long j, long j2) {
        if (isCancelled()) {
            return;
        }
        showProgressNotification(j, j2);
        this.mListener.onProgress(j, j2);
    }

    synchronized void onDownloadStarted(long j) {
        if (isCancelled()) {
            return;
        }
        showProgressNotification(0L, 0L);
        this.mListener.onStart(j);
    }

    synchronized void onStart(File file) {
        if (isCancelled()) {
            return;
        }
        downloadFile(file);
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public synchronized void resume() {
        if (isCancelled()) {
            return;
        }
        if (!NetworkStateHelper.getSharedInstance(this.mContext).isNetworkConnected()) {
            this.mListener.onError("No network connection, abort downloading.");
            return;
        }
        if (PermissionUtils.permissionsAreGranted(PermissionUtils.permissionsState(this.mContext, requiredPermissions()))) {
            check();
        } else {
            this.mListener.onError("No external storage permission.");
        }
    }

    synchronized void setDownloadedReleaseFilePath(String str) {
        try {
            if (isCancelled()) {
                return;
            }
            if (str != null) {
                SharedPreferencesManager.putString("Distribute.downloaded_release_file", str);
            } else {
                SharedPreferencesManager.remove("Distribute.downloaded_release_file");
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
