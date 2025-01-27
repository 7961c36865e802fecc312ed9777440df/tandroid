package com.microsoft.appcenter.distribute.download.manager;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import com.microsoft.appcenter.distribute.ReleaseDetails;
import com.microsoft.appcenter.distribute.download.AbstractReleaseDownloader;
import com.microsoft.appcenter.distribute.download.ReleaseDownloader;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.AsyncTaskUtils;
import com.microsoft.appcenter.utils.HandlerUtils;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;

/* loaded from: classes.dex */
public class DownloadManagerReleaseDownloader extends AbstractReleaseDownloader {
    private long mDownloadId;
    private DownloadManagerRequestTask mRequestTask;
    private DownloadManagerUpdateTask mUpdateTask;

    public DownloadManagerReleaseDownloader(Context context, ReleaseDetails releaseDetails, ReleaseDownloader.Listener listener) {
        super(context, releaseDetails, listener);
        this.mDownloadId = -1L;
    }

    private static Uri getFileUriOnOldDevices(Cursor cursor) {
        return Uri.parse("file://" + cursor.getString(cursor.getColumnIndexOrThrow("local_filename")));
    }

    private void remove(long j) {
        AppCenterLog.debug("AppCenterDistribute", "Removing download and notification id=" + j);
        AsyncTaskUtils.execute("AppCenterDistribute", new DownloadManagerRemoveTask(this.mContext, j), new Void[0]);
    }

    private synchronized void request() {
        if (isCancelled()) {
            return;
        }
        if (this.mRequestTask != null) {
            AppCenterLog.debug("AppCenterDistribute", "Downloading is already in progress.");
        } else {
            this.mRequestTask = (DownloadManagerRequestTask) AsyncTaskUtils.execute("AppCenterDistribute", new DownloadManagerRequestTask(this), new Void[0]);
        }
    }

    private synchronized void setDownloadId(long j) {
        try {
            this.mDownloadId = j;
            if (j != -1) {
                SharedPreferencesManager.putLong("Distribute.download_id", j);
            } else {
                SharedPreferencesManager.remove("Distribute.download_id");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void update() {
        if (isCancelled()) {
            return;
        }
        this.mUpdateTask = (DownloadManagerUpdateTask) AsyncTaskUtils.execute("AppCenterDistribute", new DownloadManagerUpdateTask(this), new Void[0]);
    }

    @Override // com.microsoft.appcenter.distribute.download.AbstractReleaseDownloader, com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public synchronized void cancel() {
        try {
            if (isCancelled()) {
                return;
            }
            super.cancel();
            DownloadManagerRequestTask downloadManagerRequestTask = this.mRequestTask;
            if (downloadManagerRequestTask != null) {
                downloadManagerRequestTask.cancel(true);
                this.mRequestTask = null;
            }
            DownloadManagerUpdateTask downloadManagerUpdateTask = this.mUpdateTask;
            if (downloadManagerUpdateTask != null) {
                downloadManagerUpdateTask.cancel(true);
                this.mUpdateTask = null;
            }
            long downloadId = getDownloadId();
            if (downloadId != -1) {
                remove(downloadId);
                setDownloadId(-1L);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized long getDownloadId() {
        try {
            if (this.mDownloadId == -1) {
                this.mDownloadId = SharedPreferencesManager.getLong("Distribute.download_id", -1L);
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.mDownloadId;
    }

    DownloadManager getDownloadManager() {
        return (DownloadManager) this.mContext.getSystemService("download");
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public synchronized boolean isDownloading() {
        return this.mDownloadId != -1;
    }

    synchronized void onDownloadComplete(Cursor cursor) {
        try {
            if (isCancelled()) {
                return;
            }
            AppCenterLog.debug("AppCenterDistribute", "Download was successful for id=" + this.mDownloadId);
            if (!(!this.mListener.onComplete(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow("local_uri")))) ? Build.VERSION.SDK_INT < 24 ? this.mListener.onComplete(getFileUriOnOldDevices(cursor)) : false : true)) {
                this.mListener.onError("Installer not found");
            }
        } finally {
        }
    }

    synchronized void onDownloadError(RuntimeException runtimeException) {
        if (isCancelled()) {
            return;
        }
        AppCenterLog.error("AppCenterDistribute", "Failed to download update id=" + this.mDownloadId, runtimeException);
        this.mListener.onError(runtimeException.getMessage());
    }

    synchronized void onDownloadProgress(Cursor cursor) {
        if (isCancelled()) {
            return;
        }
        long j = cursor.getLong(cursor.getColumnIndexOrThrow("total_size"));
        if (this.mListener.onProgress(cursor.getLong(cursor.getColumnIndexOrThrow("bytes_so_far")), j)) {
            HandlerUtils.getMainHandler().postAtTime(new Runnable() { // from class: com.microsoft.appcenter.distribute.download.manager.DownloadManagerReleaseDownloader.1
                @Override // java.lang.Runnable
                public void run() {
                    DownloadManagerReleaseDownloader.this.update();
                }
            }, "Distribute.handler_token_check_progress", SystemClock.uptimeMillis() + 500);
        }
    }

    synchronized void onDownloadStarted(long j, long j2) {
        if (isCancelled()) {
            return;
        }
        setDownloadId(j);
        this.mListener.onStart(j2);
        if (this.mReleaseDetails.isMandatoryUpdate()) {
            update();
        }
    }

    synchronized void onStart() {
        request();
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public synchronized void resume() {
        update();
    }
}
