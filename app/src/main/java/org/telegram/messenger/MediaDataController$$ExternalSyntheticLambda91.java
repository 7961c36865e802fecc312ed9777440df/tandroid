package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$StickerSet;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda91 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$StickerSet f$1;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda91(MediaDataController mediaDataController, TLRPC$StickerSet tLRPC$StickerSet) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$StickerSet;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadGroupStickerSet$27(this.f$1);
    }
}