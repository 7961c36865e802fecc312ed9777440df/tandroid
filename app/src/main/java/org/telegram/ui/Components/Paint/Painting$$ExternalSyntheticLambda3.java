package org.telegram.ui.Components.Paint;
/* loaded from: classes3.dex */
public final /* synthetic */ class Painting$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ Painting f$0;
    public final /* synthetic */ Path f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ Runnable f$3;

    public /* synthetic */ Painting$$ExternalSyntheticLambda3(Painting painting, Path path, boolean z, Runnable runnable) {
        this.f$0 = painting;
        this.f$1 = path;
        this.f$2 = z;
        this.f$3 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$paintStroke$0(this.f$1, this.f$2, this.f$3);
    }
}