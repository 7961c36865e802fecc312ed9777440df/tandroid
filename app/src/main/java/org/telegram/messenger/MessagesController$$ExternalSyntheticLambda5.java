package org.telegram.messenger;

import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda5(Context context, AlertDialog alertDialog) {
        this.f$0 = context;
        this.f$1 = alertDialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MessagesController.lambda$convertToMegaGroup$213(this.f$0, this.f$1);
    }
}