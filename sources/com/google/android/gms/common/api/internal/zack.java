package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
final class zack extends RegisterListenerMethod {
    final /* synthetic */ RegistrationMethods.Builder zaa;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zack(RegistrationMethods.Builder builder, ListenerHolder listenerHolder, Feature[] featureArr, boolean z, int i) {
        super(listenerHolder, featureArr, z, i);
        this.zaa = builder;
    }

    @Override // com.google.android.gms.common.api.internal.RegisterListenerMethod
    protected final void registerListener(Api.AnyClient anyClient, TaskCompletionSource taskCompletionSource) {
        RemoteCall remoteCall;
        remoteCall = this.zaa.zaa;
        remoteCall.accept(anyClient, taskCompletionSource);
    }
}
