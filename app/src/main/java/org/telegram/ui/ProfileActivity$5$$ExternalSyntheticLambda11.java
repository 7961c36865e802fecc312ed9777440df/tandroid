package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$5$$ExternalSyntheticLambda11 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ ProfileActivity.AnonymousClass5 f$0;
    public final /* synthetic */ TLRPC$User f$1;
    public final /* synthetic */ DialogsActivity f$2;

    public /* synthetic */ ProfileActivity$5$$ExternalSyntheticLambda11(ProfileActivity.AnonymousClass5 anonymousClass5, TLRPC$User tLRPC$User, DialogsActivity dialogsActivity) {
        this.f$0 = anonymousClass5;
        this.f$1 = tLRPC$User;
        this.f$2 = dialogsActivity;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onItemClick$6(this.f$1, this.f$2, dialogsActivity, arrayList, charSequence, z);
    }
}