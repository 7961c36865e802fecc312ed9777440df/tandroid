package com.google.android.gms.internal.location;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import androidx.collection.SimpleArrayMap;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.zzm;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public final class zzda extends GmsClient {
    private final SimpleArrayMap zzf;
    private final SimpleArrayMap zzg;
    private final SimpleArrayMap zzh;

    public zzda(Context context, Looper looper, ClientSettings clientSettings, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 23, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zzf = new SimpleArrayMap();
        this.zzg = new SimpleArrayMap();
        this.zzh = new SimpleArrayMap();
    }

    private final boolean zzE(Feature feature) {
        Feature feature2;
        Feature[] availableFeatures = getAvailableFeatures();
        if (availableFeatures == null) {
            return false;
        }
        int length = availableFeatures.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                feature2 = null;
                break;
            }
            feature2 = availableFeatures[i];
            if (feature.getName().equals(feature2.getName())) {
                break;
            }
            i++;
        }
        return feature2 != null && feature2.getVersion() >= feature.getVersion();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final /* synthetic */ IInterface createServiceInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
        return queryLocalInterface instanceof zzo ? (zzo) queryLocalInterface : new zzn(iBinder);
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final Feature[] getApiFeatures() {
        return zzm.zzl;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient, com.google.android.gms.common.api.Api.Client
    public final int getMinApkVersion() {
        return 11717000;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final String getServiceDescriptor() {
        return "com.google.android.gms.location.internal.IGoogleLocationManagerService";
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    protected final String getStartServiceAction() {
        return "com.google.android.location.internal.GoogleLocationManagerService.START";
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final void onConnectionSuspended(int i) {
        super.onConnectionSuspended(i);
        synchronized (this.zzf) {
            this.zzf.clear();
        }
        synchronized (this.zzg) {
            this.zzg.clear();
        }
        synchronized (this.zzh) {
            this.zzh.clear();
        }
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final boolean usesClientTelemetry() {
        return true;
    }

    public final void zzB(ListenerHolder.ListenerKey listenerKey, boolean z, TaskCompletionSource taskCompletionSource) throws RemoteException {
        synchronized (this.zzg) {
            zzcw zzcwVar = (zzcw) this.zzg.remove(listenerKey);
            if (zzcwVar == null) {
                taskCompletionSource.setResult(Boolean.FALSE);
                return;
            }
            zzcwVar.zzh();
            if (z) {
                if (zzE(zzm.zzj)) {
                    ((zzo) getService()).zzy(zzdb.zzb(null, zzcwVar, null, null), new zzcl(this, Boolean.TRUE, taskCompletionSource));
                } else {
                    ((zzo) getService()).zzz(new zzdf(2, null, null, zzcwVar, null, new zzcn(Boolean.TRUE, taskCompletionSource), null));
                }
            } else {
                taskCompletionSource.setResult(Boolean.TRUE);
            }
        }
    }

    public final void zzt(LastLocationRequest lastLocationRequest, TaskCompletionSource taskCompletionSource) throws RemoteException {
        getContext();
        if (zzE(zzm.zzf)) {
            ((zzo) getService()).zzj(lastLocationRequest, new zzcm(this, taskCompletionSource));
        } else {
            taskCompletionSource.setResult(((zzo) getService()).zzd());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0044 A[Catch: all -> 0x0082, TryCatch #0 {, blocks: (B:4:0x001a, B:8:0x0028, B:10:0x003b, B:12:0x0044, B:14:0x0080, B:13:0x0057, B:9:0x002e), top: B:19:0x001a }] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0057 A[Catch: all -> 0x0082, TryCatch #0 {, blocks: (B:4:0x001a, B:8:0x0028, B:10:0x003b, B:12:0x0044, B:14:0x0080, B:13:0x0057, B:9:0x002e), top: B:19:0x001a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zzu(zzcs zzcsVar, LocationRequest locationRequest, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzcw zzcwVar;
        ListenerHolder zza = zzcsVar.zza();
        ListenerHolder.ListenerKey listenerKey = zza.getListenerKey();
        listenerKey.getClass();
        boolean zzE = zzE(zzm.zzj);
        synchronized (this.zzg) {
            zzcw zzcwVar2 = (zzcw) this.zzg.get(listenerKey);
            if (zzcwVar2 != null && !zzE) {
                zzcwVar2.zzg(zza);
                zzcwVar = zzcwVar2;
                zzcwVar2 = null;
                getContext();
                String idString = listenerKey.toIdString();
                if (!zzE) {
                    ((zzo) getService()).zzk(zzdb.zzb(zzcwVar2, zzcwVar, null, idString), locationRequest, new zzcl(this, null, taskCompletionSource));
                } else {
                    LocationRequest.Builder builder = new LocationRequest.Builder(locationRequest);
                    builder.zzb(null);
                    ((zzo) getService()).zzz(new zzdf(1, zzdd.zza(null, builder.build()), null, zzcwVar, null, new zzcp(taskCompletionSource, zzcwVar), idString));
                }
            }
            zzcw zzcwVar3 = new zzcw(zzcsVar);
            this.zzg.put(listenerKey, zzcwVar3);
            zzcwVar = zzcwVar3;
            getContext();
            String idString2 = listenerKey.toIdString();
            if (!zzE) {
            }
        }
    }
}
