package org.telegram.ui;

import android.content.DialogInterface;
import android.text.style.URLSpan;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda9 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ URLSpan f$1;
    public final /* synthetic */ TextView f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda9(PhotoViewer photoViewer, URLSpan uRLSpan, TextView textView, int i) {
        this.f$0 = photoViewer;
        this.f$1 = uRLSpan;
        this.f$2 = textView;
        this.f$3 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onLinkLongPress$1(this.f$1, this.f$2, this.f$3, dialogInterface, i);
    }
}