package org.telegram.ui.Components;

import android.graphics.Bitmap;
import org.telegram.ui.Components.InstantCameraView;
/* loaded from: classes3.dex */
public final /* synthetic */ class InstantCameraView$VideoRecorder$GenerateKeyframeThumbTask$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ InstantCameraView.VideoRecorder.GenerateKeyframeThumbTask f$0;
    public final /* synthetic */ Bitmap f$1;

    public /* synthetic */ InstantCameraView$VideoRecorder$GenerateKeyframeThumbTask$$ExternalSyntheticLambda0(InstantCameraView.VideoRecorder.GenerateKeyframeThumbTask generateKeyframeThumbTask, Bitmap bitmap) {
        this.f$0 = generateKeyframeThumbTask;
        this.f$1 = bitmap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1);
    }
}