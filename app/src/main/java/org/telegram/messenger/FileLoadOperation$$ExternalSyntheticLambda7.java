package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoadOperation$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ FileLoadOperation f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ FileLoadOperationStream f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ FileLoadOperation$$ExternalSyntheticLambda7(FileLoadOperation fileLoadOperation, boolean z, long j, FileLoadOperationStream fileLoadOperationStream, boolean z2) {
        this.f$0 = fileLoadOperation;
        this.f$1 = z;
        this.f$2 = j;
        this.f$3 = fileLoadOperationStream;
        this.f$4 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$start$4(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}