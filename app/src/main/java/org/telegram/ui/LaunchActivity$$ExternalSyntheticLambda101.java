package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda101 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ LaunchActivity f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda101(LaunchActivity launchActivity, boolean z, int i, String str) {
        this.f$0 = launchActivity;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = str;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$runLinkRequest$50(this.f$1, this.f$2, this.f$3, dialogsActivity, arrayList, charSequence, z);
    }
}