package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacyUsersActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PrivacyUsersActivity f$0;
    public final /* synthetic */ Long f$1;

    public /* synthetic */ PrivacyUsersActivity$$ExternalSyntheticLambda0(PrivacyUsersActivity privacyUsersActivity, Long l) {
        this.f$0 = privacyUsersActivity;
        this.f$1 = l;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showUnblockAlert$3(this.f$1, dialogInterface, i);
    }
}