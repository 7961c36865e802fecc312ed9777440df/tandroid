package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzeh implements ObjectEncoder {
    static final zzeh zza = new zzeh();

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("errorCode");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("isColdCall");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("imageInfo");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        builder3.withProperty(zzciVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("detectorOptions");
        zzci zzciVar4 = new zzci();
        zzciVar4.zza(4);
        builder4.withProperty(zzciVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("contourDetectedFaces");
        zzci zzciVar5 = new zzci();
        zzciVar5.zza(5);
        builder5.withProperty(zzciVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("nonContourDetectedFaces");
        zzci zzciVar6 = new zzci();
        zzciVar6.zza(6);
        builder6.withProperty(zzciVar6.zzb()).build();
    }

    private zzeh() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zzdc zzdcVar = (zzdc) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        throw null;
    }
}
