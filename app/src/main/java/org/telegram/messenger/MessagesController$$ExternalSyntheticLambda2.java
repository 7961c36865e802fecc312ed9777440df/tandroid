package org.telegram.messenger;

import android.content.DialogInterface;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda2 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda2(MessagesController messagesController, int i) {
        this.f$0 = messagesController;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$openByUserName$349(this.f$1, dialogInterface);
    }
}