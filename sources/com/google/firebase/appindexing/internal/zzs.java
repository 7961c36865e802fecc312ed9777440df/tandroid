package com.google.firebase.appindexing.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation$ResultHolder;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.internal.icing.zzaa;
import com.google.android.gms.internal.icing.zzae;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
abstract class zzs extends TaskApiCall implements BaseImplementation$ResultHolder {
    protected TaskCompletionSource zzb;

    zzs() {
        super(null, false, 9004);
    }

    @Override // com.google.android.gms.common.api.internal.TaskApiCall
    protected final /* bridge */ /* synthetic */ void doExecute(Api.AnyClient anyClient, TaskCompletionSource taskCompletionSource) {
        this.zzb = taskCompletionSource;
        zza((zzaa) ((zzae) anyClient).getService());
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation$ResultHolder
    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        Status status = (Status) obj;
        if (status.isSuccess()) {
            this.zzb.setResult(null);
        } else {
            this.zzb.setException(zzaf.zza(status, "User Action indexing error, please try again."));
        }
    }

    protected abstract void zza(zzaa zzaaVar);
}
