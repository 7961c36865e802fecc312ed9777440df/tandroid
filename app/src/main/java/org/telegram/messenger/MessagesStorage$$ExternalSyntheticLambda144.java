package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda144 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ long f$4;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda144(MessagesStorage messagesStorage, ArrayList arrayList, ArrayList arrayList2, int i, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
        this.f$3 = i;
        this.f$4 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setDialogsFolderId$183(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}