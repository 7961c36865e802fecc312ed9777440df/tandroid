package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class JoinGroupAlert$$ExternalSyntheticLambda0 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ JoinGroupAlert f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ JoinGroupAlert$$ExternalSyntheticLambda0(JoinGroupAlert joinGroupAlert, boolean z) {
        this.f$0 = joinGroupAlert;
        this.f$1 = z;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$new$4(this.f$1, dialogInterface);
    }
}