package org.telegram.ui;

import android.widget.FrameLayout;
import androidx.collection.LongSparseArray;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$36$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass36 f$0;
    public final /* synthetic */ FrameLayout f$1;
    public final /* synthetic */ LongSparseArray f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ PhotoViewer$36$$ExternalSyntheticLambda1(PhotoViewer.AnonymousClass36 anonymousClass36, FrameLayout frameLayout, LongSparseArray longSparseArray, int i) {
        this.f$0 = anonymousClass36;
        this.f$1 = frameLayout;
        this.f$2 = longSparseArray;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onSend$0(this.f$1, this.f$2, this.f$3);
    }
}