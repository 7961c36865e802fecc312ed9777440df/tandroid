package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$photos_Photos;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda109 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ TLRPC$photos_Photos f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda109(MessagesStorage messagesStorage, long j, TLRPC$photos_Photos tLRPC$photos_Photos, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = tLRPC$photos_Photos;
        this.f$3 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$putDialogPhotos$70(this.f$1, this.f$2, this.f$3);
    }
}