package org.telegram.messenger;

import org.telegram.tgnet.WriteToSocketDelegate;
/* loaded from: classes.dex */
public final /* synthetic */ class FileUploadOperation$$ExternalSyntheticLambda6 implements WriteToSocketDelegate {
    public final /* synthetic */ FileUploadOperation f$0;

    public /* synthetic */ FileUploadOperation$$ExternalSyntheticLambda6(FileUploadOperation fileUploadOperation) {
        this.f$0 = fileUploadOperation;
    }

    @Override // org.telegram.tgnet.WriteToSocketDelegate
    public final void run() {
        this.f$0.lambda$startUploadRequest$6();
    }
}