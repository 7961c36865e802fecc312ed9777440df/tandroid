package org.telegram.ui;

import android.net.Uri;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$FirstFrameView$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ PhotoViewer.FirstFrameView f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ PhotoViewer$FirstFrameView$$ExternalSyntheticLambda3(PhotoViewer.FirstFrameView firstFrameView, Uri uri, int i) {
        this.f$0 = firstFrameView;
        this.f$1 = uri;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkFromPlayer$2(this.f$1, this.f$2);
    }
}