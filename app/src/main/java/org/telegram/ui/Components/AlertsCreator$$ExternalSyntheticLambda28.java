package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda28 implements DialogInterface.OnClickListener {
    public final /* synthetic */ BaseFragment f$0;
    public final /* synthetic */ TLRPC$User f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda28(BaseFragment baseFragment, TLRPC$User tLRPC$User, boolean z) {
        this.f$0 = baseFragment;
        this.f$1 = tLRPC$User;
        this.f$2 = z;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createCallDialogAlert$28(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}