package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC$EncryptedChat;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda21 implements DialogInterface.OnClickListener {
    public final /* synthetic */ TLRPC$EncryptedChat f$0;
    public final /* synthetic */ NumberPicker f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda21(TLRPC$EncryptedChat tLRPC$EncryptedChat, NumberPicker numberPicker) {
        this.f$0 = tLRPC$EncryptedChat;
        this.f$1 = numberPicker;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createTTLAlert$108(this.f$0, this.f$1, dialogInterface, i);
    }
}