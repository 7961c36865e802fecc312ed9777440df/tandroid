package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.ui.Components.BotKeyboardView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$$ExternalSyntheticLambda57 implements BotKeyboardView.BotKeyboardViewDelegate {
    public final /* synthetic */ ChatActivityEnterView f$0;

    public /* synthetic */ ChatActivityEnterView$$ExternalSyntheticLambda57(ChatActivityEnterView chatActivityEnterView) {
        this.f$0 = chatActivityEnterView;
    }

    @Override // org.telegram.ui.Components.BotKeyboardView.BotKeyboardViewDelegate
    public final void didPressedButton(TLRPC$KeyboardButton tLRPC$KeyboardButton) {
        this.f$0.lambda$setButtons$46(tLRPC$KeyboardButton);
    }
}