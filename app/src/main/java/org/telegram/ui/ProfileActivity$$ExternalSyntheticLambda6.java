package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda6 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ProfileActivity f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String[] f$3;
    public final /* synthetic */ String f$4;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda6(ProfileActivity profileActivity, String str, int i, String[] strArr, String str2) {
        this.f$0 = profileActivity;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = strArr;
        this.f$4 = str2;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$processOnClickOrPress$21(this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
    }
}