package org.telegram.messenger;

import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$MediaLoader$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ MediaController.MediaLoader f$0;
    public final /* synthetic */ MessageObject f$1;

    public /* synthetic */ MediaController$MediaLoader$$ExternalSyntheticLambda9(MediaController.MediaLoader mediaLoader, MessageObject messageObject) {
        this.f$0 = mediaLoader;
        this.f$1 = messageObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$addMessageToLoad$5(this.f$1);
    }
}