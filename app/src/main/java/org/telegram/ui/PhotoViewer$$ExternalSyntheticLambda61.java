package org.telegram.ui;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda61 implements Runnable {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ ImageReceiver.BitmapHolder f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda61(PhotoViewer photoViewer, ImageReceiver.BitmapHolder bitmapHolder, int i, String str) {
        this.f$0 = photoViewer;
        this.f$1 = bitmapHolder;
        this.f$2 = i;
        this.f$3 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$detectFaces$56(this.f$1, this.f$2, this.f$3);
    }
}