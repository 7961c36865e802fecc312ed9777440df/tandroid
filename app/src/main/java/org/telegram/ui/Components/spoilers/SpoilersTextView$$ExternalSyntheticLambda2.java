package org.telegram.ui.Components.spoilers;

import org.telegram.ui.Components.spoilers.SpoilersClickDetector;
/* loaded from: classes3.dex */
public final /* synthetic */ class SpoilersTextView$$ExternalSyntheticLambda2 implements SpoilersClickDetector.OnSpoilerClickedListener {
    public final /* synthetic */ SpoilersTextView f$0;

    public /* synthetic */ SpoilersTextView$$ExternalSyntheticLambda2(SpoilersTextView spoilersTextView) {
        this.f$0 = spoilersTextView;
    }

    @Override // org.telegram.ui.Components.spoilers.SpoilersClickDetector.OnSpoilerClickedListener
    public final void onSpoilerClicked(SpoilerEffect spoilerEffect, float f, float f2) {
        this.f$0.lambda$new$2(spoilerEffect, f, f2);
    }
}