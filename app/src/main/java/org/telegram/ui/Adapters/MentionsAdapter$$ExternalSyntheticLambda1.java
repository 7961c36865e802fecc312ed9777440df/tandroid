package org.telegram.ui.Adapters;

import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes3.dex */
public final /* synthetic */ class MentionsAdapter$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ MentionsAdapter f$0;
    public final /* synthetic */ boolean[] f$1;
    public final /* synthetic */ TLRPC$User f$2;

    public /* synthetic */ MentionsAdapter$$ExternalSyntheticLambda1(MentionsAdapter mentionsAdapter, boolean[] zArr, TLRPC$User tLRPC$User) {
        this.f$0 = mentionsAdapter;
        this.f$1 = zArr;
        this.f$2 = tLRPC$User;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$processFoundUser$2(this.f$1, this.f$2, dialogInterface, i);
    }
}