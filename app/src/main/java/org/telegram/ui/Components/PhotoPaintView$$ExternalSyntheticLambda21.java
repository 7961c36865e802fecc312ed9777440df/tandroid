package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$Document;
import org.telegram.ui.Components.StickerMasksAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoPaintView$$ExternalSyntheticLambda21 implements StickerMasksAlert.StickerMasksAlertDelegate {
    public final /* synthetic */ PhotoPaintView f$0;

    public /* synthetic */ PhotoPaintView$$ExternalSyntheticLambda21(PhotoPaintView photoPaintView) {
        this.f$0 = photoPaintView;
    }

    @Override // org.telegram.ui.Components.StickerMasksAlert.StickerMasksAlertDelegate
    public final void onStickerSelected(Object obj, TLRPC$Document tLRPC$Document) {
        this.f$0.lambda$openStickersView$7(obj, tLRPC$Document);
    }
}