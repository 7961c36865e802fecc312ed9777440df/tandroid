package org.telegram.ui.Adapters;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsSearchAdapter$$ExternalSyntheticLambda16 implements Runnable {
    public final /* synthetic */ DialogsSearchAdapter f$0;
    public final /* synthetic */ StringBuilder f$1;

    public /* synthetic */ DialogsSearchAdapter$$ExternalSyntheticLambda16(DialogsSearchAdapter dialogsSearchAdapter, StringBuilder sb) {
        this.f$0 = dialogsSearchAdapter;
        this.f$1 = sb;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$clearRecentSearch$7(this.f$1);
    }
}