package org.telegram.ui;

import java.io.File;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda58 implements Runnable {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ File f$2;
    public final /* synthetic */ File f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ boolean f$6;
    public final /* synthetic */ boolean f$7;
    public final /* synthetic */ boolean f$8;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda58(PhotoViewer photoViewer, int i, File file, File file2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        this.f$0 = photoViewer;
        this.f$1 = i;
        this.f$2 = file;
        this.f$3 = file2;
        this.f$4 = z;
        this.f$5 = z2;
        this.f$6 = z3;
        this.f$7 = z4;
        this.f$8 = z5;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkProgress$69(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
    }
}