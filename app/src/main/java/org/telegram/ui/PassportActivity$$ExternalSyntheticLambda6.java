package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.messenger.SecureDocument;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda6 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PassportActivity f$0;
    public final /* synthetic */ SecureDocument f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ PassportActivity.SecureDocumentCell f$3;
    public final /* synthetic */ String f$4;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda6(PassportActivity passportActivity, SecureDocument secureDocument, int i, PassportActivity.SecureDocumentCell secureDocumentCell, String str) {
        this.f$0 = passportActivity;
        this.f$1 = secureDocument;
        this.f$2 = i;
        this.f$3 = secureDocumentCell;
        this.f$4 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$addDocumentView$56(this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
    }
}