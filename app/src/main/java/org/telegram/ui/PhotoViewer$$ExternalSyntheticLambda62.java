package org.telegram.ui;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda62 implements Runnable {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ ImageReceiver.BitmapHolder f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda62(PhotoViewer photoViewer, ImageReceiver.BitmapHolder bitmapHolder, String str) {
        this.f$0 = photoViewer;
        this.f$1 = bitmapHolder;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$detectFaces$55(this.f$1, this.f$2);
    }
}