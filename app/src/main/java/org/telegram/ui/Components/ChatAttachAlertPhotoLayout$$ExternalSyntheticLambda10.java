package org.telegram.ui.Components;

import android.widget.FrameLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ ChatAttachAlertPhotoLayout f$0;
    public final /* synthetic */ FrameLayout.LayoutParams f$1;

    public /* synthetic */ ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda10(ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout, FrameLayout.LayoutParams layoutParams) {
        this.f$0 = chatAttachAlertPhotoLayout;
        this.f$1 = layoutParams;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$applyCameraViewPosition$12(this.f$1);
    }
}