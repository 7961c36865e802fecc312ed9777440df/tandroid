package org.telegram.messenger;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes.dex */
public final /* synthetic */ class MusicPlayerService$$ExternalSyntheticLambda1 implements ImageReceiver.ImageReceiverDelegate {
    public final /* synthetic */ MusicPlayerService f$0;

    public /* synthetic */ MusicPlayerService$$ExternalSyntheticLambda1(MusicPlayerService musicPlayerService) {
        this.f$0 = musicPlayerService;
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        this.f$0.lambda$onCreate$0(imageReceiver, z, z2, z3);
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
        ImageReceiver.ImageReceiverDelegate.CC.$default$onAnimationReady(this, imageReceiver);
    }
}