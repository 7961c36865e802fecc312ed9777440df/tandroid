package com.microsoft.appcenter.distribute.download;

import android.content.Context;
import com.microsoft.appcenter.distribute.ReleaseDetails;
import com.microsoft.appcenter.distribute.download.ReleaseDownloader;

/* loaded from: classes.dex */
public abstract class AbstractReleaseDownloader implements ReleaseDownloader {
    private boolean mCancelled;
    protected final Context mContext;
    protected final ReleaseDownloader.Listener mListener;
    protected final ReleaseDetails mReleaseDetails;

    protected AbstractReleaseDownloader(Context context, ReleaseDetails releaseDetails, ReleaseDownloader.Listener listener) {
        this.mContext = context;
        this.mReleaseDetails = releaseDetails;
        this.mListener = listener;
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public void cancel() {
        this.mCancelled = true;
    }

    @Override // com.microsoft.appcenter.distribute.download.ReleaseDownloader
    public ReleaseDetails getReleaseDetails() {
        return this.mReleaseDetails;
    }

    protected boolean isCancelled() {
        return this.mCancelled;
    }
}
