package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.LocaleController;
/* loaded from: classes3.dex */
public final /* synthetic */ class LanguageSelectActivity$$ExternalSyntheticLambda4 implements Comparator {
    public final /* synthetic */ LocaleController.LocaleInfo f$0;

    public /* synthetic */ LanguageSelectActivity$$ExternalSyntheticLambda4(LocaleController.LocaleInfo localeInfo) {
        this.f$0 = localeInfo;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$fillLanguages$5;
        lambda$fillLanguages$5 = LanguageSelectActivity.lambda$fillLanguages$5(this.f$0, (LocaleController.LocaleInfo) obj, (LocaleController.LocaleInfo) obj2);
        return lambda$fillLanguages$5;
    }
}