package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoader$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ FileLoader f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ long f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ boolean f$6;

    public /* synthetic */ FileLoader$$ExternalSyntheticLambda11(FileLoader fileLoader, boolean z, String str, long j, int i, boolean z2, boolean z3) {
        this.f$0 = fileLoader;
        this.f$1 = z;
        this.f$2 = str;
        this.f$3 = j;
        this.f$4 = i;
        this.f$5 = z2;
        this.f$6 = z3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$uploadFile$5(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}