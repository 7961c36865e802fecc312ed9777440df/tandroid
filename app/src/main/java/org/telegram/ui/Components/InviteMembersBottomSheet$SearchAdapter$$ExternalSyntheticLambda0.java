package org.telegram.ui.Components;

import org.telegram.ui.Components.InviteMembersBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ InviteMembersBottomSheet.SearchAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda0(InviteMembersBottomSheet.SearchAdapter searchAdapter, String str) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchDialogs$2(this.f$1);
    }
}