package org.telegram.ui;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda54 implements Runnable {
    public final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda54(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onUserLeaveHint();
    }
}