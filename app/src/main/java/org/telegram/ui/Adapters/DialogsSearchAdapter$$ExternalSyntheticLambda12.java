package org.telegram.ui.Adapters;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsSearchAdapter$$ExternalSyntheticLambda12 implements Runnable {
    public final /* synthetic */ DialogsSearchAdapter f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ DialogsSearchAdapter$$ExternalSyntheticLambda12(DialogsSearchAdapter dialogsSearchAdapter, long j) {
        this.f$0 = dialogsSearchAdapter;
        this.f$1 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$putRecentSearch$6(this.f$1);
    }
}