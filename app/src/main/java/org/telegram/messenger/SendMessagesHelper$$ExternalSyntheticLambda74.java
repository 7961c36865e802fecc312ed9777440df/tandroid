package org.telegram.messenger;

import android.graphics.Bitmap;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_game;
import org.telegram.tgnet.TLRPC$TL_photo;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda74 implements Runnable {
    public final /* synthetic */ TLRPC$TL_document f$0;
    public final /* synthetic */ Bitmap[] f$1;
    public final /* synthetic */ boolean f$10;
    public final /* synthetic */ int f$11;
    public final /* synthetic */ TLRPC$TL_photo f$12;
    public final /* synthetic */ TLRPC$TL_game f$13;
    public final /* synthetic */ String[] f$2;
    public final /* synthetic */ AccountInstance f$3;
    public final /* synthetic */ String f$4;
    public final /* synthetic */ long f$5;
    public final /* synthetic */ MessageObject f$6;
    public final /* synthetic */ MessageObject f$7;
    public final /* synthetic */ TLRPC$BotInlineResult f$8;
    public final /* synthetic */ HashMap f$9;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda74(TLRPC$TL_document tLRPC$TL_document, Bitmap[] bitmapArr, String[] strArr, AccountInstance accountInstance, String str, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC$BotInlineResult tLRPC$BotInlineResult, HashMap hashMap, boolean z, int i, TLRPC$TL_photo tLRPC$TL_photo, TLRPC$TL_game tLRPC$TL_game) {
        this.f$0 = tLRPC$TL_document;
        this.f$1 = bitmapArr;
        this.f$2 = strArr;
        this.f$3 = accountInstance;
        this.f$4 = str;
        this.f$5 = j;
        this.f$6 = messageObject;
        this.f$7 = messageObject2;
        this.f$8 = tLRPC$BotInlineResult;
        this.f$9 = hashMap;
        this.f$10 = z;
        this.f$11 = i;
        this.f$12 = tLRPC$TL_photo;
        this.f$13 = tLRPC$TL_game;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SendMessagesHelper.lambda$prepareSendingBotContextResult$79(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13);
    }
}