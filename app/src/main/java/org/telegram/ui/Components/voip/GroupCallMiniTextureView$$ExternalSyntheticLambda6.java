package org.telegram.ui.Components.voip;

import android.graphics.Bitmap;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallMiniTextureView$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ GroupCallMiniTextureView f$0;
    public final /* synthetic */ Bitmap f$1;

    public /* synthetic */ GroupCallMiniTextureView$$ExternalSyntheticLambda6(GroupCallMiniTextureView groupCallMiniTextureView, Bitmap bitmap) {
        this.f$0 = groupCallMiniTextureView;
        this.f$1 = bitmap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$saveThumb$4(this.f$1);
    }
}