package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FileUploadOperation$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ FileUploadOperation f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ FileUploadOperation$$ExternalSyntheticLambda4(FileUploadOperation fileUploadOperation, boolean z) {
        this.f$0 = fileUploadOperation;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onNetworkChanged$1(this.f$1);
    }
}