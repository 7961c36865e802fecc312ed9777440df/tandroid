package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.ChatUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ChatUsersActivity.SearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2(ChatUsersActivity.SearchAdapter searchAdapter, String str, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
        this.f$2 = arrayList;
        this.f$3 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$2(this.f$1, this.f$2, this.f$3);
    }
}