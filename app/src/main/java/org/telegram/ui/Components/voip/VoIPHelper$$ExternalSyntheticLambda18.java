package org.telegram.ui.Components.voip;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_phone_setCallRating;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPHelper$$ExternalSyntheticLambda18 implements RequestDelegate {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ boolean[] f$1;
    public final /* synthetic */ File f$2;
    public final /* synthetic */ TLRPC$TL_phone_setCallRating f$3;
    public final /* synthetic */ ArrayList f$4;
    public final /* synthetic */ Context f$5;

    public /* synthetic */ VoIPHelper$$ExternalSyntheticLambda18(int i, boolean[] zArr, File file, TLRPC$TL_phone_setCallRating tLRPC$TL_phone_setCallRating, ArrayList arrayList, Context context) {
        this.f$0 = i;
        this.f$1 = zArr;
        this.f$2 = file;
        this.f$3 = tLRPC$TL_phone_setCallRating;
        this.f$4 = arrayList;
        this.f$5 = context;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        VoIPHelper.lambda$showRateAlert$15(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, tLObject, tLRPC$TL_error);
    }
}