package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoadOperation$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ FileLoadOperation f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ FileLoadOperation$$ExternalSyntheticLambda1(FileLoadOperation fileLoadOperation, int i) {
        this.f$0 = fileLoadOperation;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFail$10(this.f$1);
    }
}