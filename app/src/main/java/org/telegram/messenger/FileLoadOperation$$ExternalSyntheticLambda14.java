package org.telegram.messenger;

import org.telegram.messenger.FileLoadOperation;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoadOperation$$ExternalSyntheticLambda14 implements RequestDelegate {
    public final /* synthetic */ FileLoadOperation f$0;
    public final /* synthetic */ FileLoadOperation.RequestInfo f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ FileLoadOperation$$ExternalSyntheticLambda14(FileLoadOperation fileLoadOperation, FileLoadOperation.RequestInfo requestInfo, TLObject tLObject) {
        this.f$0 = fileLoadOperation;
        this.f$1 = requestInfo;
        this.f$2 = tLObject;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$startDownloadRequest$13(this.f$1, this.f$2, tLObject, tLRPC$TL_error);
    }
}