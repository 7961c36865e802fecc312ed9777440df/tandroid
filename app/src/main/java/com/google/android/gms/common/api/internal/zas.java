package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.signin.SignInOptions;
import com.microsoft.appcenter.Constants;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes3.dex */
public final class zas implements zabo {
    private final Context zaa;
    private final zaar zab;
    private final Looper zac;
    private final zaaz zad;
    private final zaaz zae;
    private final Map<Api.AnyClientKey<?>, zaaz> zaf;
    private final Api.Client zah;
    private Bundle zai;
    private final Lock zam;
    private final Set<SignInConnectionListener> zag = Collections.newSetFromMap(new WeakHashMap());
    private ConnectionResult zaj = null;
    private ConnectionResult zak = null;
    private boolean zal = false;
    private int zan = 0;

    public static zas zaa(Context context, zaar zaarVar, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<Api.AnyClientKey<?>, Api.Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, Api.AbstractClientBuilder<? extends com.google.android.gms.signin.zae, SignInOptions> abstractClientBuilder, ArrayList<zaq> arrayList) {
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        Api.Client client = null;
        for (Map.Entry<Api.AnyClientKey<?>, Api.Client> entry : map.entrySet()) {
            Api.Client value = entry.getValue();
            if (value.providesSignIn()) {
                client = value;
            }
            if (value.requiresSignIn()) {
                arrayMap.put(entry.getKey(), value);
            } else {
                arrayMap2.put(entry.getKey(), value);
            }
        }
        Preconditions.checkState(!arrayMap.isEmpty(), "CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        for (Api<?> api : map2.keySet()) {
            Api.AnyClientKey<?> zac = api.zac();
            if (arrayMap.containsKey(zac)) {
                arrayMap3.put(api, map2.get(api));
            } else if (arrayMap2.containsKey(zac)) {
                arrayMap4.put(api, map2.get(api));
            } else {
                throw new IllegalStateException("Each API in the isOptionalMap must have a corresponding client in the clients map.");
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList<zaq> arrayList4 = arrayList;
        int size = arrayList4.size();
        int i = 0;
        while (i < size) {
            zaq zaqVar = arrayList4.get(i);
            i++;
            zaq zaqVar2 = zaqVar;
            if (arrayMap3.containsKey(zaqVar2.zaa)) {
                arrayList2.add(zaqVar2);
            } else if (arrayMap4.containsKey(zaqVar2.zaa)) {
                arrayList3.add(zaqVar2);
            } else {
                throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the isOptionalMap");
            }
        }
        return new zas(context, zaarVar, lock, looper, googleApiAvailabilityLight, arrayMap, arrayMap2, clientSettings, abstractClientBuilder, client, arrayList2, arrayList3, arrayMap3, arrayMap4);
    }

    private zas(Context context, zaar zaarVar, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<Api.AnyClientKey<?>, Api.Client> map, Map<Api.AnyClientKey<?>, Api.Client> map2, ClientSettings clientSettings, Api.AbstractClientBuilder<? extends com.google.android.gms.signin.zae, SignInOptions> abstractClientBuilder, Api.Client client, ArrayList<zaq> arrayList, ArrayList<zaq> arrayList2, Map<Api<?>, Boolean> map3, Map<Api<?>, Boolean> map4) {
        this.zaa = context;
        this.zab = zaarVar;
        this.zam = lock;
        this.zac = looper;
        this.zah = client;
        this.zad = new zaaz(context, zaarVar, lock, looper, googleApiAvailabilityLight, map2, null, map4, null, arrayList2, new zau(this, null));
        this.zae = new zaaz(context, zaarVar, lock, looper, googleApiAvailabilityLight, map, clientSettings, map3, abstractClientBuilder, arrayList, new zat(this, null));
        ArrayMap arrayMap = new ArrayMap();
        for (Api.AnyClientKey<?> anyClientKey : map2.keySet()) {
            arrayMap.put(anyClientKey, this.zad);
        }
        for (Api.AnyClientKey<?> anyClientKey2 : map.keySet()) {
            arrayMap.put(anyClientKey2, this.zae);
        }
        this.zaf = Collections.unmodifiableMap(arrayMap);
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T zaa(T t) {
        if (zac((BaseImplementation.ApiMethodImpl<? extends Result, ? extends Api.AnyClient>) t)) {
            if (zaj()) {
                t.setFailedResult(new Status(4, (String) null, zak()));
                return t;
            }
            return (T) this.zae.zaa((zaaz) t);
        }
        return (T) this.zad.zaa((zaaz) t);
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T zab(T t) {
        if (zac((BaseImplementation.ApiMethodImpl<? extends Result, ? extends Api.AnyClient>) t)) {
            if (zaj()) {
                t.setFailedResult(new Status(4, (String) null, zak()));
                return t;
            }
            return (T) this.zae.zab((zaaz) t);
        }
        return (T) this.zad.zab((zaaz) t);
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final ConnectionResult zaa(Api<?> api) {
        if (Objects.equal(this.zaf.get(api.zac()), this.zae)) {
            if (zaj()) {
                return new ConnectionResult(4, zak());
            }
            return this.zae.zaa(api);
        }
        return this.zad.zaa(api);
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final void zaa() {
        this.zan = 2;
        this.zal = false;
        this.zak = null;
        this.zaj = null;
        this.zad.zaa();
        this.zae.zaa();
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final ConnectionResult zab() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final ConnectionResult zaa(long j, TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final void zac() {
        this.zak = null;
        this.zaj = null;
        this.zan = 0;
        this.zad.zac();
        this.zae.zac();
        zai();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001e, code lost:
        if (r2.zan == 1) goto L12;
     */
    @Override // com.google.android.gms.common.api.internal.zabo
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean zad() {
        this.zam.lock();
        try {
            boolean z = true;
            if (this.zad.zad()) {
                if (!this.zae.zad() && !zaj()) {
                }
                return z;
            }
            z = false;
            return z;
        } finally {
            this.zam.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final boolean zae() {
        this.zam.lock();
        try {
            return this.zan == 2;
        } finally {
            this.zam.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final boolean zaa(SignInConnectionListener signInConnectionListener) {
        this.zam.lock();
        try {
            if ((zae() || zad()) && !this.zae.zad()) {
                this.zag.add(signInConnectionListener);
                if (this.zan == 0) {
                    this.zan = 1;
                }
                this.zak = null;
                this.zae.zaa();
                return true;
            }
            this.zam.unlock();
            return false;
        } finally {
            this.zam.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final void zaf() {
        this.zad.zaf();
        this.zae.zaf();
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final void zag() {
        this.zam.lock();
        try {
            boolean zae = zae();
            this.zae.zac();
            this.zak = new ConnectionResult(4);
            if (zae) {
                new com.google.android.gms.internal.base.zas(this.zac).post(new zar(this));
            } else {
                zai();
            }
        } finally {
            this.zam.unlock();
        }
    }

    public final void zah() {
        if (zab(this.zaj)) {
            if (zab(this.zak) || zaj()) {
                switch (this.zan) {
                    case 2:
                        ((zaar) Preconditions.checkNotNull(this.zab)).zaa(this.zai);
                    case 1:
                        zai();
                        break;
                    default:
                        Log.wtf("CompositeGAC", "Attempted to call success callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new AssertionError());
                        break;
                }
                this.zan = 0;
                return;
            }
            ConnectionResult connectionResult = this.zak;
            if (connectionResult != null) {
                if (this.zan == 1) {
                    zai();
                    return;
                }
                zaa(connectionResult);
                this.zad.zac();
            }
        } else if (this.zaj != null && zab(this.zak)) {
            this.zae.zac();
            zaa((ConnectionResult) Preconditions.checkNotNull(this.zaj));
        } else {
            ConnectionResult connectionResult2 = this.zaj;
            if (connectionResult2 != null && this.zak != null) {
                if (this.zae.zac < this.zad.zac) {
                    connectionResult2 = this.zak;
                }
                zaa(connectionResult2);
            }
        }
    }

    private final void zaa(ConnectionResult connectionResult) {
        switch (this.zan) {
            case 2:
                this.zab.zaa(connectionResult);
            case 1:
                zai();
                break;
            default:
                Log.wtf("CompositeGAC", "Attempted to call failure callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                break;
        }
        this.zan = 0;
    }

    private final void zai() {
        for (SignInConnectionListener signInConnectionListener : this.zag) {
            signInConnectionListener.onComplete();
        }
        this.zag.clear();
    }

    public final void zaa(int i, boolean z) {
        this.zab.zaa(i, z);
        this.zak = null;
        this.zaj = null;
    }

    private final boolean zaj() {
        ConnectionResult connectionResult = this.zak;
        return connectionResult != null && connectionResult.getErrorCode() == 4;
    }

    private final boolean zac(BaseImplementation.ApiMethodImpl<? extends Result, ? extends Api.AnyClient> apiMethodImpl) {
        zaaz zaazVar = this.zaf.get(apiMethodImpl.getClientKey());
        Preconditions.checkNotNull(zaazVar, "GoogleApiClient is not configured to use the API required for this call.");
        return zaazVar.equals(this.zae);
    }

    private final PendingIntent zak() {
        if (this.zah == null) {
            return null;
        }
        return PendingIntent.getActivity(this.zaa, System.identityHashCode(this.zab), this.zah.getSignInIntent(), 134217728);
    }

    public final void zaa(Bundle bundle) {
        Bundle bundle2 = this.zai;
        if (bundle2 == null) {
            this.zai = bundle;
        } else if (bundle != null) {
            bundle2.putAll(bundle);
        }
    }

    private static boolean zab(ConnectionResult connectionResult) {
        return connectionResult != null && connectionResult.isSuccess();
    }

    @Override // com.google.android.gms.common.api.internal.zabo
    public final void zaa(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append((CharSequence) str).append("authClient").println(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR);
        this.zae.zaa(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
        printWriter.append((CharSequence) str).append("anonClient").println(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR);
        this.zad.zaa(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
    }
}
