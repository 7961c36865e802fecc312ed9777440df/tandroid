package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$$ExternalSyntheticLambda60 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ ChatActivityEnterView f$0;
    public final /* synthetic */ MessageObject f$1;
    public final /* synthetic */ TLRPC$KeyboardButton f$2;

    public /* synthetic */ ChatActivityEnterView$$ExternalSyntheticLambda60(ChatActivityEnterView chatActivityEnterView, MessageObject messageObject, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
        this.f$0 = chatActivityEnterView;
        this.f$1 = messageObject;
        this.f$2 = tLRPC$KeyboardButton;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$didPressedBotButton$49(this.f$1, this.f$2, dialogsActivity, arrayList, charSequence, z);
    }
}