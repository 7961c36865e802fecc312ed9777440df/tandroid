package com.google.android.gms.internal.mlkit_vision_common;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.mlkit:vision-common@@17.3.0 */
/* loaded from: classes.dex */
final class zzdt implements ObjectEncoder {
    static final zzdt zza = new zzdt();

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("sdkVersion");
        zzae zzaeVar = new zzae();
        zzaeVar.zza(1);
        builder.withProperty(zzaeVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("osBuild");
        zzae zzaeVar2 = new zzae();
        zzaeVar2.zza(2);
        builder2.withProperty(zzaeVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("brand");
        zzae zzaeVar3 = new zzae();
        zzaeVar3.zza(3);
        builder3.withProperty(zzaeVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("device");
        zzae zzaeVar4 = new zzae();
        zzaeVar4.zza(4);
        builder4.withProperty(zzaeVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("hardware");
        zzae zzaeVar5 = new zzae();
        zzaeVar5.zza(5);
        builder5.withProperty(zzaeVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("manufacturer");
        zzae zzaeVar6 = new zzae();
        zzaeVar6.zza(6);
        builder6.withProperty(zzaeVar6.zzb()).build();
        FieldDescriptor.Builder builder7 = FieldDescriptor.builder("model");
        zzae zzaeVar7 = new zzae();
        zzaeVar7.zza(7);
        builder7.withProperty(zzaeVar7.zzb()).build();
        FieldDescriptor.Builder builder8 = FieldDescriptor.builder("product");
        zzae zzaeVar8 = new zzae();
        zzaeVar8.zza(8);
        builder8.withProperty(zzaeVar8.zzb()).build();
        FieldDescriptor.Builder builder9 = FieldDescriptor.builder("soc");
        zzae zzaeVar9 = new zzae();
        zzaeVar9.zza(9);
        builder9.withProperty(zzaeVar9.zzb()).build();
        FieldDescriptor.Builder builder10 = FieldDescriptor.builder("socMetaBuildId");
        zzae zzaeVar10 = new zzae();
        zzaeVar10.zza(10);
        builder10.withProperty(zzaeVar10.zzb()).build();
    }

    private zzdt() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zzlh zzlhVar = (zzlh) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        throw null;
    }
}
