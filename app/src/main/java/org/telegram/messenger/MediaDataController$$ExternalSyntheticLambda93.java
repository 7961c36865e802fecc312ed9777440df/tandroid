package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda93 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$StickerSet f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ TLRPC$TL_messages_stickerSet f$4;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda93(MediaDataController mediaDataController, TLRPC$StickerSet tLRPC$StickerSet, int i, int i2, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$StickerSet;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = tLRPC$TL_messages_stickerSet;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$toggleStickerSet$76(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}