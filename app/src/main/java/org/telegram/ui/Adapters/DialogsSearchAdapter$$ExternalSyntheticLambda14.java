package org.telegram.ui.Adapters;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsSearchAdapter$$ExternalSyntheticLambda14 implements Runnable {
    public final /* synthetic */ DialogsSearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ DialogsSearchAdapter$$ExternalSyntheticLambda14(DialogsSearchAdapter dialogsSearchAdapter, String str, int i, String str2) {
        this.f$0 = dialogsSearchAdapter;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = str2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchDialogs$14(this.f$1, this.f$2, this.f$3);
    }
}