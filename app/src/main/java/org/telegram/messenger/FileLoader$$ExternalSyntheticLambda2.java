package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoader$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ FileLoader f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ FileLoader$$ExternalSyntheticLambda2(FileLoader fileLoader, String str, int i, int i2) {
        this.f$0 = fileLoader;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkDownloadQueue$11(this.f$1, this.f$2, this.f$3);
    }
}