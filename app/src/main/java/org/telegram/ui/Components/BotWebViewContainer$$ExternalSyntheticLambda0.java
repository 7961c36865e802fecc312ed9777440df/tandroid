package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.net.Uri;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewContainer$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ BotWebViewContainer f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ BotWebViewContainer$$ExternalSyntheticLambda0(BotWebViewContainer botWebViewContainer, Uri uri) {
        this.f$0 = botWebViewContainer;
        this.f$1 = uri;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onOpenUri$1(this.f$1, dialogInterface, i);
    }
}