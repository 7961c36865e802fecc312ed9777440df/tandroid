package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda56 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda56(MessagesStorage messagesStorage, int i, ArrayList arrayList, int i2, int i3, int i4) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = arrayList;
        this.f$3 = i2;
        this.f$4 = i3;
        this.f$5 = i4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createTaskForSecretChat$87(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}