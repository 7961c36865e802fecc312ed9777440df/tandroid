package org.telegram.messenger;

import android.view.Window;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class AndroidUtilities$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ ArrayList f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ Window f$2;

    public /* synthetic */ AndroidUtilities$$ExternalSyntheticLambda10(ArrayList arrayList, long j, Window window) {
        this.f$0 = arrayList;
        this.f$1 = j;
        this.f$2 = window;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AndroidUtilities.lambda$registerFlagSecure$11(this.f$0, this.f$1, this.f$2);
    }
}