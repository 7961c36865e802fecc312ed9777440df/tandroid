package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda33 implements Runnable {
    public final /* synthetic */ SendMessagesHelper f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;
    public final /* synthetic */ ArrayList f$4;
    public final /* synthetic */ ArrayList f$5;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda33(SendMessagesHelper sendMessagesHelper, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
        this.f$3 = arrayList3;
        this.f$4 = arrayList4;
        this.f$5 = arrayList5;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processUnsentMessages$63(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}