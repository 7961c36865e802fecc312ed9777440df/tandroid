package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda2(long j, int i, Runnable runnable) {
        this.f$0 = j;
        this.f$1 = i;
        this.f$2 = runnable;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createColorSelectDialog$90(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}