package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
final class zzkz implements ObjectEncoder {
    static final zzkz zza = new zzkz();

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("deviceInfo");
        zzbz zzbzVar = new zzbz();
        zzbzVar.zza(1);
        builder.withProperty(zzbzVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("nnapiInfo");
        zzbz zzbzVar2 = new zzbz();
        zzbzVar2.zza(2);
        builder2.withProperty(zzbzVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("gpuInfo");
        zzbz zzbzVar3 = new zzbz();
        zzbzVar3.zza(3);
        builder3.withProperty(zzbzVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("pipelineIdentifier");
        zzbz zzbzVar4 = new zzbz();
        zzbzVar4.zza(4);
        builder4.withProperty(zzbzVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("acceptedConfigurations");
        zzbz zzbzVar5 = new zzbz();
        zzbzVar5.zza(5);
        builder5.withProperty(zzbzVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("action");
        zzbz zzbzVar6 = new zzbz();
        zzbzVar6.zza(6);
        builder6.withProperty(zzbzVar6.zzb()).build();
        FieldDescriptor.Builder builder7 = FieldDescriptor.builder("status");
        zzbz zzbzVar7 = new zzbz();
        zzbzVar7.zza(7);
        builder7.withProperty(zzbzVar7.zzb()).build();
        FieldDescriptor.Builder builder8 = FieldDescriptor.builder("customErrors");
        zzbz zzbzVar8 = new zzbz();
        zzbzVar8.zza(8);
        builder8.withProperty(zzbzVar8.zzb()).build();
        FieldDescriptor.Builder builder9 = FieldDescriptor.builder("benchmarkStatus");
        zzbz zzbzVar9 = new zzbz();
        zzbzVar9.zza(9);
        builder9.withProperty(zzbzVar9.zzb()).build();
        FieldDescriptor.Builder builder10 = FieldDescriptor.builder("validationTestResult");
        zzbz zzbzVar10 = new zzbz();
        zzbzVar10.zza(10);
        builder10.withProperty(zzbzVar10.zzb()).build();
        FieldDescriptor.Builder builder11 = FieldDescriptor.builder("timestampUs");
        zzbz zzbzVar11 = new zzbz();
        zzbzVar11.zza(11);
        builder11.withProperty(zzbzVar11.zzb()).build();
        FieldDescriptor.Builder builder12 = FieldDescriptor.builder("elapsedUs");
        zzbz zzbzVar12 = new zzbz();
        zzbzVar12.zza(12);
        builder12.withProperty(zzbzVar12.zzb()).build();
    }

    private zzkz() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zzsq zzsqVar = (zzsq) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        throw null;
    }
}