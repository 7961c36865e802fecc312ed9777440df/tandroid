package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$3$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PassportActivity.AnonymousClass3 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ Runnable f$4;
    public final /* synthetic */ PassportActivity.ErrorRunnable f$5;

    public /* synthetic */ PassportActivity$3$$ExternalSyntheticLambda1(PassportActivity.AnonymousClass3 anonymousClass3, String str, String str2, String str3, Runnable runnable, PassportActivity.ErrorRunnable errorRunnable) {
        this.f$0 = anonymousClass3;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = str3;
        this.f$4 = runnable;
        this.f$5 = errorRunnable;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onIdentityDone$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, dialogInterface, i);
    }
}