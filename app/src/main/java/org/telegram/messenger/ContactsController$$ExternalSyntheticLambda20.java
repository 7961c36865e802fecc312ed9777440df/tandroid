package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda20 implements Runnable {
    public final /* synthetic */ ContactsController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda20(ContactsController contactsController, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = contactsController;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$applyContactsUpdates$48(this.f$1, this.f$2);
    }
}