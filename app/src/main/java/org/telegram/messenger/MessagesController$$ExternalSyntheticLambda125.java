package org.telegram.messenger;

import androidx.collection.LongSparseArray;
import java.util.HashMap;
import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda125 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ HashMap f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ TLObject f$3;
    public final /* synthetic */ LongSparseArray f$4;
    public final /* synthetic */ long f$5;
    public final /* synthetic */ boolean f$6;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda125(MessagesController messagesController, HashMap hashMap, String str, TLObject tLObject, LongSparseArray longSparseArray, long j, boolean z) {
        this.f$0 = messagesController;
        this.f$1 = hashMap;
        this.f$2 = str;
        this.f$3 = tLObject;
        this.f$4 = longSparseArray;
        this.f$5 = j;
        this.f$6 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$reloadWebPages$152(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}