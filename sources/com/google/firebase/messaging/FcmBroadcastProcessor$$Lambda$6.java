package com.google.firebase.messaging;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

/* loaded from: classes.dex */
final /* synthetic */ class FcmBroadcastProcessor$$Lambda$6 implements Continuation {
    static final Continuation $instance = new FcmBroadcastProcessor$$Lambda$6();

    private FcmBroadcastProcessor$$Lambda$6() {
    }

    @Override // com.google.android.gms.tasks.Continuation
    public Object then(Task task) {
        return FcmBroadcastProcessor.lambda$startMessagingService$1$FcmBroadcastProcessor(task);
    }
}
