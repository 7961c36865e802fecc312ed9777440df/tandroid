package org.telegram.messenger;

import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda22 implements Runnable {
    public final /* synthetic */ ContactsController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ HashMap f$2;
    public final /* synthetic */ HashMap f$3;
    public final /* synthetic */ ArrayList f$4;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda22(ContactsController contactsController, ArrayList arrayList, HashMap hashMap, HashMap hashMap2, ArrayList arrayList2) {
        this.f$0 = contactsController;
        this.f$1 = arrayList;
        this.f$2 = hashMap;
        this.f$3 = hashMap2;
        this.f$4 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$mergePhonebookAndTelegramContacts$41(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}