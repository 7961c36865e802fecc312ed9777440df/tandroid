package org.telegram.ui.Adapters;

import java.util.ArrayList;
import org.telegram.ui.Cells.GraySectionCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsSearchAdapter$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ DialogsSearchAdapter f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ GraySectionCell f$3;

    public /* synthetic */ DialogsSearchAdapter$$ExternalSyntheticLambda17(DialogsSearchAdapter dialogsSearchAdapter, ArrayList arrayList, int i, GraySectionCell graySectionCell) {
        this.f$0 = dialogsSearchAdapter;
        this.f$1 = arrayList;
        this.f$2 = i;
        this.f$3 = graySectionCell;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onBindViewHolder$23(this.f$1, this.f$2, this.f$3);
    }
}