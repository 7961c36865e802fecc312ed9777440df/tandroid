package org.webrtc;

import org.webrtc.GlGenericDrawer;
/* loaded from: classes3.dex */
public final /* synthetic */ class EglRenderer$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ EglRenderer f$0;
    public final /* synthetic */ GlGenericDrawer.TextureCallback f$1;

    public /* synthetic */ EglRenderer$$ExternalSyntheticLambda6(EglRenderer eglRenderer, GlGenericDrawer.TextureCallback textureCallback) {
        this.f$0 = eglRenderer;
        this.f$1 = textureCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getTexture$7(this.f$1);
    }
}