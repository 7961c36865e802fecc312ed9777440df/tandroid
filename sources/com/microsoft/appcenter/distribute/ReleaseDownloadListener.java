package com.microsoft.appcenter.distribute;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.microsoft.appcenter.distribute.download.ReleaseDownloader;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.HandlerUtils;
import java.text.NumberFormat;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ReleaseDownloadListener implements ReleaseDownloader.Listener {
    private final Context mContext;
    private ProgressDialog mProgressDialog;
    private final ReleaseDetails mReleaseDetails;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReleaseDownloadListener(Context context, ReleaseDetails releaseDetails) {
        this.mContext = context;
        this.mReleaseDetails = releaseDetails;
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader.Listener
    public void onStart(long j) {
        AppCenterLog.debug("AppCenterDistribute", String.format(Locale.ENGLISH, "Start download %s (%d) update.", this.mReleaseDetails.getShortVersion(), Integer.valueOf(this.mReleaseDetails.getVersion())));
        Distribute.getInstance().setDownloading(this.mReleaseDetails, j);
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader.Listener
    public synchronized boolean onProgress(final long j, final long j2) {
        AppCenterLog.verbose("AppCenterDistribute", String.format(Locale.ENGLISH, "Downloading %s (%d) update: %d KiB / %d KiB", this.mReleaseDetails.getShortVersion(), Integer.valueOf(this.mReleaseDetails.getVersion()), Long.valueOf(j / 1024), Long.valueOf(j2 / 1024)));
        HandlerUtils.runOnUiThread(new Runnable() { // from class: com.microsoft.appcenter.distribute.ReleaseDownloadListener.1
            @Override // java.lang.Runnable
            public void run() {
                ReleaseDownloadListener.this.updateProgressDialog(j, j2);
            }
        });
        return this.mProgressDialog != null;
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader.Listener
    public boolean onComplete(Uri uri) {
        Intent installIntent = InstallerUtils.getInstallIntent(uri);
        if (installIntent.resolveActivity(this.mContext.getPackageManager()) == null) {
            AppCenterLog.debug("AppCenterDistribute", "Cannot resolve install intent for " + uri);
            return false;
        }
        AppCenterLog.debug("AppCenterDistribute", String.format(Locale.ENGLISH, "Download %s (%d) update completed.", this.mReleaseDetails.getShortVersion(), Integer.valueOf(this.mReleaseDetails.getVersion())));
        if (!Distribute.getInstance().notifyDownload(this.mReleaseDetails, installIntent)) {
            AppCenterLog.info("AppCenterDistribute", "Show install UI for " + uri);
            this.mContext.startActivity(installIntent);
            Distribute.getInstance().setInstalling(this.mReleaseDetails);
        }
        return true;
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader.Listener
    public void onError(String str) {
        AppCenterLog.error("AppCenterDistribute", String.format(Locale.ENGLISH, "Failed to download %s (%d) update: %s", this.mReleaseDetails.getShortVersion(), Integer.valueOf(this.mReleaseDetails.getVersion()), str));
        HandlerUtils.runOnUiThread(new Runnable() { // from class: com.microsoft.appcenter.distribute.ReleaseDownloadListener.2
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(ReleaseDownloadListener.this.mContext, R$string.appcenter_distribute_downloading_error, 0).show();
            }
        });
        Distribute.getInstance().completeWorkflow(this.mReleaseDetails);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ProgressDialog showDownloadProgress(Activity activity) {
        if (this.mReleaseDetails.isMandatoryUpdate()) {
            ProgressDialog progressDialog = new ProgressDialog(activity);
            this.mProgressDialog = progressDialog;
            progressDialog.setTitle(R$string.appcenter_distribute_downloading_update);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setProgressStyle(1);
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.setProgressNumberFormat(null);
            this.mProgressDialog.setProgressPercentFormat(null);
            return this.mProgressDialog;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void hideProgressDialog() {
        final ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            this.mProgressDialog = null;
            HandlerUtils.runOnUiThread(new Runnable(this) { // from class: com.microsoft.appcenter.distribute.ReleaseDownloadListener.3
                @Override // java.lang.Runnable
                public void run() {
                    progressDialog.hide();
                }
            });
            HandlerUtils.getMainHandler().removeCallbacksAndMessages("Distribute.handler_token_check_progress");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateProgressDialog(long j, long j2) {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && j2 >= 0) {
            if (progressDialog.isIndeterminate()) {
                this.mProgressDialog.setProgressPercentFormat(NumberFormat.getPercentInstance());
                this.mProgressDialog.setProgressNumberFormat(this.mContext.getString(R$string.appcenter_distribute_download_progress_number_format));
                this.mProgressDialog.setIndeterminate(false);
                this.mProgressDialog.setMax((int) (j2 / 1048576));
            }
            this.mProgressDialog.setProgress((int) (j / 1048576));
        }
    }
}