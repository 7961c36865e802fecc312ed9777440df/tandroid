package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda21 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda21(ChatActivity chatActivity, int i) {
        this.f$0 = chatActivity;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$processSelectedOption$204(this.f$1, dialogInterface);
    }
}