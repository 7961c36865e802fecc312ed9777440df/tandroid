package org.telegram.ui.Components.Paint;
/* loaded from: classes3.dex */
public final /* synthetic */ class Painting$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ Painting f$0;
    public final /* synthetic */ Slice f$1;

    public /* synthetic */ Painting$$ExternalSyntheticLambda4(Painting painting, Slice slice) {
        this.f$0 = painting;
        this.f$1 = slice;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$registerUndo$3(this.f$1);
    }
}