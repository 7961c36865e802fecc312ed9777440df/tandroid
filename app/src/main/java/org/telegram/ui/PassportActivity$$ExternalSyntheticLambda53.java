package org.telegram.ui;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda53 implements Runnable {
    public final /* synthetic */ PassportActivity f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda53(PassportActivity passportActivity, ArrayList arrayList, int i, boolean z) {
        this.f$0 = passportActivity;
        this.f$1 = arrayList;
        this.f$2 = i;
        this.f$3 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSelectedFiles$72(this.f$1, this.f$2, this.f$3);
    }
}