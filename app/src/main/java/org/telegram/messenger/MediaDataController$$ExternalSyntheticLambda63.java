package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda63 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda63(MediaDataController mediaDataController, String str, boolean z) {
        this.f$0 = mediaDataController;
        this.f$1 = str;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadStickersByEmojiOrName$57(this.f$1, this.f$2);
    }
}