package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.Components.InviteMembersBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditWidgetActivity$$ExternalSyntheticLambda1 implements InviteMembersBottomSheet.InviteMembersBottomSheetDelegate {
    public final /* synthetic */ EditWidgetActivity f$0;

    public /* synthetic */ EditWidgetActivity$$ExternalSyntheticLambda1(EditWidgetActivity editWidgetActivity) {
        this.f$0 = editWidgetActivity;
    }

    @Override // org.telegram.ui.Components.InviteMembersBottomSheet.InviteMembersBottomSheetDelegate
    public final void didSelectDialogs(ArrayList arrayList) {
        this.f$0.lambda$createView$0(arrayList);
    }
}