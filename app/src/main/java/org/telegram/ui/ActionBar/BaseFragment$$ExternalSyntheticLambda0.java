package org.telegram.ui.ActionBar;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class BaseFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ BaseFragment f$0;
    public final /* synthetic */ DialogInterface.OnDismissListener f$1;

    public /* synthetic */ BaseFragment$$ExternalSyntheticLambda0(BaseFragment baseFragment, DialogInterface.OnDismissListener onDismissListener) {
        this.f$0 = baseFragment;
        this.f$1 = onDismissListener;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showDialog$0(this.f$1, dialogInterface);
    }
}