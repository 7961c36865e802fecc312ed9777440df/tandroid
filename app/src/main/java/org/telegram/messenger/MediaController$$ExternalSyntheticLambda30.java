package org.telegram.messenger;

import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$$ExternalSyntheticLambda30 implements Runnable {
    public final /* synthetic */ MediaController f$0;
    public final /* synthetic */ MessagesController.EmojiSound f$1;
    public final /* synthetic */ AccountInstance f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ MediaController$$ExternalSyntheticLambda30(MediaController mediaController, MessagesController.EmojiSound emojiSound, AccountInstance accountInstance, boolean z) {
        this.f$0 = mediaController;
        this.f$1 = emojiSound;
        this.f$2 = accountInstance;
        this.f$3 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$playEmojiSound$18(this.f$1, this.f$2, this.f$3);
    }
}