package org.telegram.ui;

import org.telegram.ui.FilterUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterUsersActivity$GroupCreateAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ FilterUsersActivity.GroupCreateAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ FilterUsersActivity$GroupCreateAdapter$$ExternalSyntheticLambda0(FilterUsersActivity.GroupCreateAdapter groupCreateAdapter, String str) {
        this.f$0 = groupCreateAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchDialogs$2(this.f$1);
    }
}