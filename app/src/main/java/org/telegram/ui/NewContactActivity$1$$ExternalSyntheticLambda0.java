package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC$TL_inputPhoneContact;
import org.telegram.ui.NewContactActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class NewContactActivity$1$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ NewContactActivity.AnonymousClass1 f$0;
    public final /* synthetic */ TLRPC$TL_inputPhoneContact f$1;

    public /* synthetic */ NewContactActivity$1$$ExternalSyntheticLambda0(NewContactActivity.AnonymousClass1 anonymousClass1, TLRPC$TL_inputPhoneContact tLRPC$TL_inputPhoneContact) {
        this.f$0 = anonymousClass1;
        this.f$1 = tLRPC$TL_inputPhoneContact;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$0(this.f$1, dialogInterface, i);
    }
}