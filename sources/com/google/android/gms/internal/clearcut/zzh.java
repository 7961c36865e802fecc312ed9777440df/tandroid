package com.google.android.gms.internal.clearcut;

import android.util.Log;
import com.google.android.gms.clearcut.ClearcutLogger;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation$ApiMethodImpl;

/* loaded from: classes.dex */
final class zzh extends BaseImplementation$ApiMethodImpl {
    private final com.google.android.gms.clearcut.zze zzao;

    zzh(com.google.android.gms.clearcut.zze zzeVar, GoogleApiClient googleApiClient) {
        super(ClearcutLogger.API, googleApiClient);
        this.zzao = zzeVar;
    }

    @Override // com.google.android.gms.common.api.internal.BasePendingResult
    protected final /* synthetic */ Result createFailedResult(Status status) {
        return status;
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation$ApiMethodImpl
    protected final /* synthetic */ void doExecute(Api.AnyClient anyClient) {
        zzj zzjVar = (zzj) anyClient;
        zzi zziVar = new zzi(this);
        try {
            com.google.android.gms.clearcut.zze zzeVar = this.zzao;
            zzeVar.getClass();
            zzha zzhaVar = zzeVar.zzaa;
            int zzas = zzhaVar.zzas();
            byte[] bArr = new byte[zzas];
            zzfz.zza(zzhaVar, bArr, 0, zzas);
            zzeVar.zzah = bArr;
            ((zzn) zzjVar.getService()).zza(zziVar, this.zzao);
        } catch (RuntimeException e) {
            Log.e("ClearcutLoggerApiImpl", "derived ClearcutLogger.MessageProducer ", e);
            setFailedResult(new Status(10, "MessageProducer"));
        }
    }
}
