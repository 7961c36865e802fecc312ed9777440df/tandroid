package org.telegram.ui;

import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class SecretMediaViewer$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SecretMediaViewer f$0;
    public final /* synthetic */ PhotoViewer.PlaceProviderObject f$1;

    public /* synthetic */ SecretMediaViewer$$ExternalSyntheticLambda3(SecretMediaViewer secretMediaViewer, PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.f$0 = secretMediaViewer;
        this.f$1 = placeProviderObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$closePhoto$4(this.f$1);
    }
}