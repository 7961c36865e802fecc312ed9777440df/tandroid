package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient;

/* loaded from: classes.dex */
public final class zzf extends zza {
    public final IBinder zze;
    final /* synthetic */ BaseGmsClient zzf;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzf(BaseGmsClient baseGmsClient, int i, IBinder iBinder, Bundle bundle) {
        super(baseGmsClient, i, bundle);
        this.zzf = baseGmsClient;
        this.zze = iBinder;
    }

    @Override // com.google.android.gms.common.internal.zza
    protected final void zzb(ConnectionResult connectionResult) {
        if (this.zzf.zzx != null) {
            this.zzf.zzx.onConnectionFailed(connectionResult);
        }
        this.zzf.onConnectionFailed(connectionResult);
    }

    @Override // com.google.android.gms.common.internal.zza
    protected final boolean zzd() {
        String str;
        String interfaceDescriptor;
        BaseGmsClient.BaseConnectionCallbacks baseConnectionCallbacks;
        BaseGmsClient.BaseConnectionCallbacks baseConnectionCallbacks2;
        try {
            IBinder iBinder = this.zze;
            Preconditions.checkNotNull(iBinder);
            interfaceDescriptor = iBinder.getInterfaceDescriptor();
        } catch (RemoteException unused) {
            str = "service probably died";
        }
        if (!this.zzf.getServiceDescriptor().equals(interfaceDescriptor)) {
            str = "service descriptor mismatch: " + this.zzf.getServiceDescriptor() + " vs. " + interfaceDescriptor;
            Log.w("GmsClient", str);
            return false;
        }
        IInterface createServiceInterface = this.zzf.createServiceInterface(this.zze);
        if (createServiceInterface == null || !(BaseGmsClient.zzn(this.zzf, 2, 4, createServiceInterface) || BaseGmsClient.zzn(this.zzf, 3, 4, createServiceInterface))) {
            return false;
        }
        this.zzf.zzB = null;
        Bundle connectionHint = this.zzf.getConnectionHint();
        BaseGmsClient baseGmsClient = this.zzf;
        baseConnectionCallbacks = baseGmsClient.zzw;
        if (baseConnectionCallbacks == null) {
            return true;
        }
        baseConnectionCallbacks2 = baseGmsClient.zzw;
        baseConnectionCallbacks2.onConnected(connectionHint);
        return true;
    }
}
