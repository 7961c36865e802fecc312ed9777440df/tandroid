package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$messages_Dialogs;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda194 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$messages_Dialogs f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ TLRPC$messages_Dialogs f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ ArrayList f$5;
    public final /* synthetic */ ArrayList f$6;
    public final /* synthetic */ ArrayList f$7;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda194(MessagesController messagesController, TLRPC$messages_Dialogs tLRPC$messages_Dialogs, ArrayList arrayList, TLRPC$messages_Dialogs tLRPC$messages_Dialogs2, int i, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$messages_Dialogs;
        this.f$2 = arrayList;
        this.f$3 = tLRPC$messages_Dialogs2;
        this.f$4 = i;
        this.f$5 = arrayList2;
        this.f$6 = arrayList3;
        this.f$7 = arrayList4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedDialogFilters$15(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}