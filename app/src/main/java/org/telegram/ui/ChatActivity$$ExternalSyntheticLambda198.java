package org.telegram.ui;

import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda198 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ AlertDialog[] f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda198(ChatActivity chatActivity, AlertDialog[] alertDialogArr, int i) {
        this.f$0 = chatActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSelectedOption$205(this.f$1, this.f$2);
    }
}