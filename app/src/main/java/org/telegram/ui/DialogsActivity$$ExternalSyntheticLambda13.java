package org.telegram.ui;

import android.content.DialogInterface;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda13 implements DialogInterface.OnClickListener {
    public final /* synthetic */ DialogsActivity f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda13(DialogsActivity dialogsActivity, ArrayList arrayList, int i) {
        this.f$0 = dialogsActivity;
        this.f$1 = arrayList;
        this.f$2 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$performSelectedDialogsAction$34(this.f$1, this.f$2, dialogInterface, i);
    }
}