package org.telegram.ui;

import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$$ExternalSyntheticLambda45 implements Runnable {
    public final /* synthetic */ GroupCallActivity f$0;
    public final /* synthetic */ AlertDialog[] f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ GroupCallActivity$$ExternalSyntheticLambda45(GroupCallActivity groupCallActivity, AlertDialog[] alertDialogArr, int i) {
        this.f$0 = groupCallActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$inviteUserToCall$48(this.f$1, this.f$2);
    }
}