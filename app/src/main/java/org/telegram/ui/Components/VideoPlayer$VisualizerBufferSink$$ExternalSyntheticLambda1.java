package org.telegram.ui.Components;

import org.telegram.ui.Components.VideoPlayer;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoPlayer$VisualizerBufferSink$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ VideoPlayer.VisualizerBufferSink f$0;
    public final /* synthetic */ float[] f$1;

    public /* synthetic */ VideoPlayer$VisualizerBufferSink$$ExternalSyntheticLambda1(VideoPlayer.VisualizerBufferSink visualizerBufferSink, float[] fArr) {
        this.f$0 = visualizerBufferSink;
        this.f$1 = fArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$handleBuffer$1(this.f$1);
    }
}