package org.telegram.messenger;

import android.net.Uri;
import java.util.ArrayList;
import org.telegram.messenger.MessagesStorage;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda76 implements MessagesStorage.LongCallback {
    public final /* synthetic */ SendMessagesHelper f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ MessagesStorage.LongCallback f$3;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda76(SendMessagesHelper sendMessagesHelper, Uri uri, ArrayList arrayList, MessagesStorage.LongCallback longCallback) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = uri;
        this.f$2 = arrayList;
        this.f$3 = longCallback;
    }

    @Override // org.telegram.messenger.MessagesStorage.LongCallback
    public final void run(long j) {
        this.f$0.lambda$prepareImportHistory$64(this.f$1, this.f$2, this.f$3, j);
    }
}