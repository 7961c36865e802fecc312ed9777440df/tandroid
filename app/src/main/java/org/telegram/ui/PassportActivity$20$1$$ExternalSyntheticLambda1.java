package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.messenger.SecureDocument;
import org.telegram.tgnet.TLRPC$TL_account_saveSecureValue;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_secureRequiredType;
import org.telegram.tgnet.TLRPC$TL_secureValue;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$20$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PassportActivity.AnonymousClass20.AnonymousClass1 f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ ArrayList f$10;
    public final /* synthetic */ SecureDocument f$11;
    public final /* synthetic */ SecureDocument f$12;
    public final /* synthetic */ SecureDocument f$13;
    public final /* synthetic */ ArrayList f$14;
    public final /* synthetic */ String f$15;
    public final /* synthetic */ String f$16;
    public final /* synthetic */ int f$17;
    public final /* synthetic */ Runnable f$18;
    public final /* synthetic */ PassportActivity.ErrorRunnable f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ TLRPC$TL_account_saveSecureValue f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ TLRPC$TL_secureRequiredType f$6;
    public final /* synthetic */ TLRPC$TL_secureRequiredType f$7;
    public final /* synthetic */ TLRPC$TL_secureValue f$8;
    public final /* synthetic */ TLRPC$TL_secureValue f$9;

    public /* synthetic */ PassportActivity$20$1$$ExternalSyntheticLambda1(PassportActivity.AnonymousClass20.AnonymousClass1 anonymousClass1, TLRPC$TL_error tLRPC$TL_error, PassportActivity.ErrorRunnable errorRunnable, String str, TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue, boolean z, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, TLRPC$TL_secureValue tLRPC$TL_secureValue, TLRPC$TL_secureValue tLRPC$TL_secureValue2, ArrayList arrayList, SecureDocument secureDocument, SecureDocument secureDocument2, SecureDocument secureDocument3, ArrayList arrayList2, String str2, String str3, int i, Runnable runnable) {
        this.f$0 = anonymousClass1;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = errorRunnable;
        this.f$3 = str;
        this.f$4 = tLRPC$TL_account_saveSecureValue;
        this.f$5 = z;
        this.f$6 = tLRPC$TL_secureRequiredType;
        this.f$7 = tLRPC$TL_secureRequiredType2;
        this.f$8 = tLRPC$TL_secureValue;
        this.f$9 = tLRPC$TL_secureValue2;
        this.f$10 = arrayList;
        this.f$11 = secureDocument;
        this.f$12 = secureDocument2;
        this.f$13 = secureDocument3;
        this.f$14 = arrayList2;
        this.f$15 = str2;
        this.f$16 = str3;
        this.f$17 = i;
        this.f$18 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onResult$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13, this.f$14, this.f$15, this.f$16, this.f$17, this.f$18);
    }
}