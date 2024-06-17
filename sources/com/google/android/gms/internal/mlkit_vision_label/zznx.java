package com.google.android.gms.internal.mlkit_vision_label;

import android.content.Context;
import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Event;
import com.google.android.datatransport.Transformer;
import com.google.android.datatransport.Transport;
import com.google.android.datatransport.TransportFactory;
import com.google.android.datatransport.cct.CCTDestination;
import com.google.android.datatransport.runtime.TransportRuntime;
import com.google.firebase.components.Lazy;
import com.google.firebase.inject.Provider;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zznx implements zzno {
    private Provider zza;
    private final Provider zzb;
    private final zznh zzc;

    public zznx(Context context, zznh zznhVar) {
        this.zzc = zznhVar;
        CCTDestination cCTDestination = CCTDestination.INSTANCE;
        TransportRuntime.initialize(context);
        final TransportFactory newFactory = TransportRuntime.getInstance().newFactory(cCTDestination);
        if (cCTDestination.getSupportedEncodings().contains(Encoding.of("json"))) {
            this.zza = new Lazy(new Provider() { // from class: com.google.android.gms.internal.mlkit_vision_label.zznu
                @Override // com.google.firebase.inject.Provider
                public final Object get() {
                    return TransportFactory.this.getTransport("FIREBASE_ML_SDK", byte[].class, Encoding.of("json"), new Transformer() { // from class: com.google.android.gms.internal.mlkit_vision_label.zznw
                        @Override // com.google.android.datatransport.Transformer
                        public final Object apply(Object obj) {
                            return (byte[]) obj;
                        }
                    });
                }
            });
        }
        this.zzb = new Lazy(new Provider() { // from class: com.google.android.gms.internal.mlkit_vision_label.zznv
            @Override // com.google.firebase.inject.Provider
            public final Object get() {
                return TransportFactory.this.getTransport("FIREBASE_ML_SDK", byte[].class, Encoding.of("proto"), new Transformer() { // from class: com.google.android.gms.internal.mlkit_vision_label.zznt
                    @Override // com.google.android.datatransport.Transformer
                    public final Object apply(Object obj) {
                        return (byte[]) obj;
                    }
                });
            }
        });
    }

    static Event zzb(zznh zznhVar, zznf zznfVar) {
        int zza = zznhVar.zza();
        if (zznfVar.zza() == 0) {
            return Event.ofTelemetry(zznfVar.zze(zza, false));
        }
        return Event.ofData(zznfVar.zze(zza, false));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzno
    public final void zza(zznf zznfVar) {
        if (this.zzc.zza() != 0) {
            ((Transport) this.zzb.get()).send(zzb(this.zzc, zznfVar));
            return;
        }
        Provider provider = this.zza;
        if (provider != null) {
            ((Transport) provider.get()).send(zzb(this.zzc, zznfVar));
        }
    }
}