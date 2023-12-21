package com.google.android.gms.internal.mlkit_vision_common;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.mlkit:vision-common@@17.3.0 */
/* loaded from: classes.dex */
final class zzeo implements ObjectEncoder {
    private static final FieldDescriptor zzA;
    private static final FieldDescriptor zzB;
    private static final FieldDescriptor zzC;
    private static final FieldDescriptor zzD;
    private static final FieldDescriptor zzE;
    private static final FieldDescriptor zzF;
    private static final FieldDescriptor zzG;
    private static final FieldDescriptor zzH;
    private static final FieldDescriptor zzI;
    private static final FieldDescriptor zzJ;
    private static final FieldDescriptor zzK;
    private static final FieldDescriptor zzL;
    private static final FieldDescriptor zzM;
    private static final FieldDescriptor zzN;
    private static final FieldDescriptor zzO;
    private static final FieldDescriptor zzP;
    private static final FieldDescriptor zzQ;
    private static final FieldDescriptor zzR;
    private static final FieldDescriptor zzS;
    private static final FieldDescriptor zzT;
    private static final FieldDescriptor zzU;
    private static final FieldDescriptor zzV;
    private static final FieldDescriptor zzW;
    private static final FieldDescriptor zzX;
    private static final FieldDescriptor zzY;
    private static final FieldDescriptor zzZ;
    static final zzeo zza = new zzeo();
    private static final FieldDescriptor zzaA;
    private static final FieldDescriptor zzaB;
    private static final FieldDescriptor zzaC;
    private static final FieldDescriptor zzaD;
    private static final FieldDescriptor zzaE;
    private static final FieldDescriptor zzaF;
    private static final FieldDescriptor zzaG;
    private static final FieldDescriptor zzaH;
    private static final FieldDescriptor zzaI;
    private static final FieldDescriptor zzaJ;
    private static final FieldDescriptor zzaK;
    private static final FieldDescriptor zzaL;
    private static final FieldDescriptor zzaM;
    private static final FieldDescriptor zzaa;
    private static final FieldDescriptor zzab;
    private static final FieldDescriptor zzac;
    private static final FieldDescriptor zzad;
    private static final FieldDescriptor zzae;
    private static final FieldDescriptor zzaf;
    private static final FieldDescriptor zzag;
    private static final FieldDescriptor zzah;
    private static final FieldDescriptor zzai;
    private static final FieldDescriptor zzaj;
    private static final FieldDescriptor zzak;
    private static final FieldDescriptor zzal;
    private static final FieldDescriptor zzam;
    private static final FieldDescriptor zzan;
    private static final FieldDescriptor zzao;
    private static final FieldDescriptor zzap;
    private static final FieldDescriptor zzaq;
    private static final FieldDescriptor zzar;
    private static final FieldDescriptor zzas;
    private static final FieldDescriptor zzat;
    private static final FieldDescriptor zzau;
    private static final FieldDescriptor zzav;
    private static final FieldDescriptor zzaw;
    private static final FieldDescriptor zzax;
    private static final FieldDescriptor zzay;
    private static final FieldDescriptor zzaz;
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;
    private static final FieldDescriptor zze;
    private static final FieldDescriptor zzf;
    private static final FieldDescriptor zzg;
    private static final FieldDescriptor zzh;
    private static final FieldDescriptor zzi;
    private static final FieldDescriptor zzj;
    private static final FieldDescriptor zzk;
    private static final FieldDescriptor zzl;
    private static final FieldDescriptor zzm;
    private static final FieldDescriptor zzn;
    private static final FieldDescriptor zzo;
    private static final FieldDescriptor zzp;
    private static final FieldDescriptor zzq;
    private static final FieldDescriptor zzr;
    private static final FieldDescriptor zzs;
    private static final FieldDescriptor zzt;
    private static final FieldDescriptor zzu;
    private static final FieldDescriptor zzv;
    private static final FieldDescriptor zzw;
    private static final FieldDescriptor zzx;
    private static final FieldDescriptor zzy;
    private static final FieldDescriptor zzz;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("systemInfo");
        zzae zzaeVar = new zzae();
        zzaeVar.zza(1);
        zzb = builder.withProperty(zzaeVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("eventName");
        zzae zzaeVar2 = new zzae();
        zzaeVar2.zza(2);
        zzc = builder2.withProperty(zzaeVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("isThickClient");
        zzae zzaeVar3 = new zzae();
        zzaeVar3.zza(37);
        zzd = builder3.withProperty(zzaeVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("clientType");
        zzae zzaeVar4 = new zzae();
        zzaeVar4.zza(61);
        zze = builder4.withProperty(zzaeVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("modelDownloadLogEvent");
        zzae zzaeVar5 = new zzae();
        zzaeVar5.zza(3);
        zzf = builder5.withProperty(zzaeVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("customModelLoadLogEvent");
        zzae zzaeVar6 = new zzae();
        zzaeVar6.zza(20);
        zzg = builder6.withProperty(zzaeVar6.zzb()).build();
        FieldDescriptor.Builder builder7 = FieldDescriptor.builder("customModelInferenceLogEvent");
        zzae zzaeVar7 = new zzae();
        zzaeVar7.zza(4);
        zzh = builder7.withProperty(zzaeVar7.zzb()).build();
        FieldDescriptor.Builder builder8 = FieldDescriptor.builder("customModelCreateLogEvent");
        zzae zzaeVar8 = new zzae();
        zzaeVar8.zza(29);
        zzi = builder8.withProperty(zzaeVar8.zzb()).build();
        FieldDescriptor.Builder builder9 = FieldDescriptor.builder("onDeviceFaceDetectionLogEvent");
        zzae zzaeVar9 = new zzae();
        zzaeVar9.zza(5);
        zzj = builder9.withProperty(zzaeVar9.zzb()).build();
        FieldDescriptor.Builder builder10 = FieldDescriptor.builder("onDeviceFaceLoadLogEvent");
        zzae zzaeVar10 = new zzae();
        zzaeVar10.zza(59);
        zzk = builder10.withProperty(zzaeVar10.zzb()).build();
        FieldDescriptor.Builder builder11 = FieldDescriptor.builder("onDeviceTextDetectionLogEvent");
        zzae zzaeVar11 = new zzae();
        zzaeVar11.zza(6);
        zzl = builder11.withProperty(zzaeVar11.zzb()).build();
        FieldDescriptor.Builder builder12 = FieldDescriptor.builder("onDeviceTextDetectionLoadLogEvent");
        zzae zzaeVar12 = new zzae();
        zzaeVar12.zza(79);
        zzm = builder12.withProperty(zzaeVar12.zzb()).build();
        FieldDescriptor.Builder builder13 = FieldDescriptor.builder("onDeviceBarcodeDetectionLogEvent");
        zzae zzaeVar13 = new zzae();
        zzaeVar13.zza(7);
        zzn = builder13.withProperty(zzaeVar13.zzb()).build();
        FieldDescriptor.Builder builder14 = FieldDescriptor.builder("onDeviceBarcodeLoadLogEvent");
        zzae zzaeVar14 = new zzae();
        zzaeVar14.zza(58);
        zzo = builder14.withProperty(zzaeVar14.zzb()).build();
        FieldDescriptor.Builder builder15 = FieldDescriptor.builder("onDeviceImageLabelCreateLogEvent");
        zzae zzaeVar15 = new zzae();
        zzaeVar15.zza(48);
        zzp = builder15.withProperty(zzaeVar15.zzb()).build();
        FieldDescriptor.Builder builder16 = FieldDescriptor.builder("onDeviceImageLabelLoadLogEvent");
        zzae zzaeVar16 = new zzae();
        zzaeVar16.zza(49);
        zzq = builder16.withProperty(zzaeVar16.zzb()).build();
        FieldDescriptor.Builder builder17 = FieldDescriptor.builder("onDeviceImageLabelDetectionLogEvent");
        zzae zzaeVar17 = new zzae();
        zzaeVar17.zza(18);
        zzr = builder17.withProperty(zzaeVar17.zzb()).build();
        FieldDescriptor.Builder builder18 = FieldDescriptor.builder("onDeviceObjectCreateLogEvent");
        zzae zzaeVar18 = new zzae();
        zzaeVar18.zza(26);
        zzs = builder18.withProperty(zzaeVar18.zzb()).build();
        FieldDescriptor.Builder builder19 = FieldDescriptor.builder("onDeviceObjectLoadLogEvent");
        zzae zzaeVar19 = new zzae();
        zzaeVar19.zza(27);
        zzt = builder19.withProperty(zzaeVar19.zzb()).build();
        FieldDescriptor.Builder builder20 = FieldDescriptor.builder("onDeviceObjectInferenceLogEvent");
        zzae zzaeVar20 = new zzae();
        zzaeVar20.zza(28);
        zzu = builder20.withProperty(zzaeVar20.zzb()).build();
        FieldDescriptor.Builder builder21 = FieldDescriptor.builder("onDevicePoseDetectionLogEvent");
        zzae zzaeVar21 = new zzae();
        zzaeVar21.zza(44);
        zzv = builder21.withProperty(zzaeVar21.zzb()).build();
        FieldDescriptor.Builder builder22 = FieldDescriptor.builder("onDeviceSegmentationLogEvent");
        zzae zzaeVar22 = new zzae();
        zzaeVar22.zza(45);
        zzw = builder22.withProperty(zzaeVar22.zzb()).build();
        FieldDescriptor.Builder builder23 = FieldDescriptor.builder("onDeviceSmartReplyLogEvent");
        zzae zzaeVar23 = new zzae();
        zzaeVar23.zza(19);
        zzx = builder23.withProperty(zzaeVar23.zzb()).build();
        FieldDescriptor.Builder builder24 = FieldDescriptor.builder("onDeviceLanguageIdentificationLogEvent");
        zzae zzaeVar24 = new zzae();
        zzaeVar24.zza(21);
        zzy = builder24.withProperty(zzaeVar24.zzb()).build();
        FieldDescriptor.Builder builder25 = FieldDescriptor.builder("onDeviceTranslationLogEvent");
        zzae zzaeVar25 = new zzae();
        zzaeVar25.zza(22);
        zzz = builder25.withProperty(zzaeVar25.zzb()).build();
        FieldDescriptor.Builder builder26 = FieldDescriptor.builder("cloudFaceDetectionLogEvent");
        zzae zzaeVar26 = new zzae();
        zzaeVar26.zza(8);
        zzA = builder26.withProperty(zzaeVar26.zzb()).build();
        FieldDescriptor.Builder builder27 = FieldDescriptor.builder("cloudCropHintDetectionLogEvent");
        zzae zzaeVar27 = new zzae();
        zzaeVar27.zza(9);
        zzB = builder27.withProperty(zzaeVar27.zzb()).build();
        FieldDescriptor.Builder builder28 = FieldDescriptor.builder("cloudDocumentTextDetectionLogEvent");
        zzae zzaeVar28 = new zzae();
        zzaeVar28.zza(10);
        zzC = builder28.withProperty(zzaeVar28.zzb()).build();
        FieldDescriptor.Builder builder29 = FieldDescriptor.builder("cloudImagePropertiesDetectionLogEvent");
        zzae zzaeVar29 = new zzae();
        zzaeVar29.zza(11);
        zzD = builder29.withProperty(zzaeVar29.zzb()).build();
        FieldDescriptor.Builder builder30 = FieldDescriptor.builder("cloudImageLabelDetectionLogEvent");
        zzae zzaeVar30 = new zzae();
        zzaeVar30.zza(12);
        zzE = builder30.withProperty(zzaeVar30.zzb()).build();
        FieldDescriptor.Builder builder31 = FieldDescriptor.builder("cloudLandmarkDetectionLogEvent");
        zzae zzaeVar31 = new zzae();
        zzaeVar31.zza(13);
        zzF = builder31.withProperty(zzaeVar31.zzb()).build();
        FieldDescriptor.Builder builder32 = FieldDescriptor.builder("cloudLogoDetectionLogEvent");
        zzae zzaeVar32 = new zzae();
        zzaeVar32.zza(14);
        zzG = builder32.withProperty(zzaeVar32.zzb()).build();
        FieldDescriptor.Builder builder33 = FieldDescriptor.builder("cloudSafeSearchDetectionLogEvent");
        zzae zzaeVar33 = new zzae();
        zzaeVar33.zza(15);
        zzH = builder33.withProperty(zzaeVar33.zzb()).build();
        FieldDescriptor.Builder builder34 = FieldDescriptor.builder("cloudTextDetectionLogEvent");
        zzae zzaeVar34 = new zzae();
        zzaeVar34.zza(16);
        zzI = builder34.withProperty(zzaeVar34.zzb()).build();
        FieldDescriptor.Builder builder35 = FieldDescriptor.builder("cloudWebSearchDetectionLogEvent");
        zzae zzaeVar35 = new zzae();
        zzaeVar35.zza(17);
        zzJ = builder35.withProperty(zzaeVar35.zzb()).build();
        FieldDescriptor.Builder builder36 = FieldDescriptor.builder("automlImageLabelingCreateLogEvent");
        zzae zzaeVar36 = new zzae();
        zzaeVar36.zza(23);
        zzK = builder36.withProperty(zzaeVar36.zzb()).build();
        FieldDescriptor.Builder builder37 = FieldDescriptor.builder("automlImageLabelingLoadLogEvent");
        zzae zzaeVar37 = new zzae();
        zzaeVar37.zza(24);
        zzL = builder37.withProperty(zzaeVar37.zzb()).build();
        FieldDescriptor.Builder builder38 = FieldDescriptor.builder("automlImageLabelingInferenceLogEvent");
        zzae zzaeVar38 = new zzae();
        zzaeVar38.zza(25);
        zzM = builder38.withProperty(zzaeVar38.zzb()).build();
        FieldDescriptor.Builder builder39 = FieldDescriptor.builder("isModelDownloadedLogEvent");
        zzae zzaeVar39 = new zzae();
        zzaeVar39.zza(39);
        zzN = builder39.withProperty(zzaeVar39.zzb()).build();
        FieldDescriptor.Builder builder40 = FieldDescriptor.builder("deleteModelLogEvent");
        zzae zzaeVar40 = new zzae();
        zzaeVar40.zza(40);
        zzO = builder40.withProperty(zzaeVar40.zzb()).build();
        FieldDescriptor.Builder builder41 = FieldDescriptor.builder("aggregatedAutomlImageLabelingInferenceLogEvent");
        zzae zzaeVar41 = new zzae();
        zzaeVar41.zza(30);
        zzP = builder41.withProperty(zzaeVar41.zzb()).build();
        FieldDescriptor.Builder builder42 = FieldDescriptor.builder("aggregatedCustomModelInferenceLogEvent");
        zzae zzaeVar42 = new zzae();
        zzaeVar42.zza(31);
        zzQ = builder42.withProperty(zzaeVar42.zzb()).build();
        FieldDescriptor.Builder builder43 = FieldDescriptor.builder("aggregatedOnDeviceFaceDetectionLogEvent");
        zzae zzaeVar43 = new zzae();
        zzaeVar43.zza(32);
        zzR = builder43.withProperty(zzaeVar43.zzb()).build();
        FieldDescriptor.Builder builder44 = FieldDescriptor.builder("aggregatedOnDeviceBarcodeDetectionLogEvent");
        zzae zzaeVar44 = new zzae();
        zzaeVar44.zza(33);
        zzS = builder44.withProperty(zzaeVar44.zzb()).build();
        FieldDescriptor.Builder builder45 = FieldDescriptor.builder("aggregatedOnDeviceImageLabelDetectionLogEvent");
        zzae zzaeVar45 = new zzae();
        zzaeVar45.zza(34);
        zzT = builder45.withProperty(zzaeVar45.zzb()).build();
        FieldDescriptor.Builder builder46 = FieldDescriptor.builder("aggregatedOnDeviceObjectInferenceLogEvent");
        zzae zzaeVar46 = new zzae();
        zzaeVar46.zza(35);
        zzU = builder46.withProperty(zzaeVar46.zzb()).build();
        FieldDescriptor.Builder builder47 = FieldDescriptor.builder("aggregatedOnDeviceTextDetectionLogEvent");
        zzae zzaeVar47 = new zzae();
        zzaeVar47.zza(36);
        zzV = builder47.withProperty(zzaeVar47.zzb()).build();
        FieldDescriptor.Builder builder48 = FieldDescriptor.builder("aggregatedOnDevicePoseDetectionLogEvent");
        zzae zzaeVar48 = new zzae();
        zzaeVar48.zza(46);
        zzW = builder48.withProperty(zzaeVar48.zzb()).build();
        FieldDescriptor.Builder builder49 = FieldDescriptor.builder("aggregatedOnDeviceSegmentationLogEvent");
        zzae zzaeVar49 = new zzae();
        zzaeVar49.zza(47);
        zzX = builder49.withProperty(zzaeVar49.zzb()).build();
        FieldDescriptor.Builder builder50 = FieldDescriptor.builder("pipelineAccelerationInferenceEvents");
        zzae zzaeVar50 = new zzae();
        zzaeVar50.zza(69);
        zzY = builder50.withProperty(zzaeVar50.zzb()).build();
        FieldDescriptor.Builder builder51 = FieldDescriptor.builder("remoteConfigLogEvent");
        zzae zzaeVar51 = new zzae();
        zzaeVar51.zza(42);
        zzZ = builder51.withProperty(zzaeVar51.zzb()).build();
        FieldDescriptor.Builder builder52 = FieldDescriptor.builder("inputImageConstructionLogEvent");
        zzae zzaeVar52 = new zzae();
        zzaeVar52.zza(50);
        zzaa = builder52.withProperty(zzaeVar52.zzb()).build();
        FieldDescriptor.Builder builder53 = FieldDescriptor.builder("leakedHandleEvent");
        zzae zzaeVar53 = new zzae();
        zzaeVar53.zza(51);
        zzab = builder53.withProperty(zzaeVar53.zzb()).build();
        FieldDescriptor.Builder builder54 = FieldDescriptor.builder("cameraSourceLogEvent");
        zzae zzaeVar54 = new zzae();
        zzaeVar54.zza(52);
        zzac = builder54.withProperty(zzaeVar54.zzb()).build();
        FieldDescriptor.Builder builder55 = FieldDescriptor.builder("imageLabelOptionalModuleLogEvent");
        zzae zzaeVar55 = new zzae();
        zzaeVar55.zza(53);
        zzad = builder55.withProperty(zzaeVar55.zzb()).build();
        FieldDescriptor.Builder builder56 = FieldDescriptor.builder("languageIdentificationOptionalModuleLogEvent");
        zzae zzaeVar56 = new zzae();
        zzaeVar56.zza(54);
        zzae = builder56.withProperty(zzaeVar56.zzb()).build();
        FieldDescriptor.Builder builder57 = FieldDescriptor.builder("faceDetectionOptionalModuleLogEvent");
        zzae zzaeVar57 = new zzae();
        zzaeVar57.zza(60);
        zzaf = builder57.withProperty(zzaeVar57.zzb()).build();
        FieldDescriptor.Builder builder58 = FieldDescriptor.builder("documentDetectionOptionalModuleLogEvent");
        zzae zzaeVar58 = new zzae();
        zzaeVar58.zza(85);
        zzag = builder58.withProperty(zzaeVar58.zzb()).build();
        FieldDescriptor.Builder builder59 = FieldDescriptor.builder("documentCroppingOptionalModuleLogEvent");
        zzae zzaeVar59 = new zzae();
        zzaeVar59.zza(86);
        zzah = builder59.withProperty(zzaeVar59.zzb()).build();
        FieldDescriptor.Builder builder60 = FieldDescriptor.builder("documentEnhancementOptionalModuleLogEvent");
        zzae zzaeVar60 = new zzae();
        zzaeVar60.zza(87);
        zzai = builder60.withProperty(zzaeVar60.zzb()).build();
        FieldDescriptor.Builder builder61 = FieldDescriptor.builder("nlClassifierOptionalModuleLogEvent");
        zzae zzaeVar61 = new zzae();
        zzaeVar61.zza(55);
        zzaj = builder61.withProperty(zzaeVar61.zzb()).build();
        FieldDescriptor.Builder builder62 = FieldDescriptor.builder("nlClassifierClientLibraryLogEvent");
        zzae zzaeVar62 = new zzae();
        zzaeVar62.zza(56);
        zzak = builder62.withProperty(zzaeVar62.zzb()).build();
        FieldDescriptor.Builder builder63 = FieldDescriptor.builder("accelerationAllowlistLogEvent");
        zzae zzaeVar63 = new zzae();
        zzaeVar63.zza(57);
        zzal = builder63.withProperty(zzaeVar63.zzb()).build();
        FieldDescriptor.Builder builder64 = FieldDescriptor.builder("toxicityDetectionCreateEvent");
        zzae zzaeVar64 = new zzae();
        zzaeVar64.zza(62);
        zzam = builder64.withProperty(zzaeVar64.zzb()).build();
        FieldDescriptor.Builder builder65 = FieldDescriptor.builder("toxicityDetectionLoadEvent");
        zzae zzaeVar65 = new zzae();
        zzaeVar65.zza(63);
        zzan = builder65.withProperty(zzaeVar65.zzb()).build();
        FieldDescriptor.Builder builder66 = FieldDescriptor.builder("toxicityDetectionInferenceEvent");
        zzae zzaeVar66 = new zzae();
        zzaeVar66.zza(64);
        zzao = builder66.withProperty(zzaeVar66.zzb()).build();
        FieldDescriptor.Builder builder67 = FieldDescriptor.builder("barcodeDetectionOptionalModuleLogEvent");
        zzae zzaeVar67 = new zzae();
        zzaeVar67.zza(65);
        zzap = builder67.withProperty(zzaeVar67.zzb()).build();
        FieldDescriptor.Builder builder68 = FieldDescriptor.builder("customImageLabelOptionalModuleLogEvent");
        zzae zzaeVar68 = new zzae();
        zzaeVar68.zza(66);
        zzaq = builder68.withProperty(zzaeVar68.zzb()).build();
        FieldDescriptor.Builder builder69 = FieldDescriptor.builder("codeScannerScanApiEvent");
        zzae zzaeVar69 = new zzae();
        zzaeVar69.zza(67);
        zzar = builder69.withProperty(zzaeVar69.zzb()).build();
        FieldDescriptor.Builder builder70 = FieldDescriptor.builder("codeScannerOptionalModuleEvent");
        zzae zzaeVar70 = new zzae();
        zzaeVar70.zza(68);
        zzas = builder70.withProperty(zzaeVar70.zzb()).build();
        FieldDescriptor.Builder builder71 = FieldDescriptor.builder("onDeviceExplicitContentCreateLogEvent");
        zzae zzaeVar71 = new zzae();
        zzaeVar71.zza(70);
        zzat = builder71.withProperty(zzaeVar71.zzb()).build();
        FieldDescriptor.Builder builder72 = FieldDescriptor.builder("onDeviceExplicitContentLoadLogEvent");
        zzae zzaeVar72 = new zzae();
        zzaeVar72.zza(71);
        zzau = builder72.withProperty(zzaeVar72.zzb()).build();
        FieldDescriptor.Builder builder73 = FieldDescriptor.builder("onDeviceExplicitContentInferenceLogEvent");
        zzae zzaeVar73 = new zzae();
        zzaeVar73.zza(72);
        zzav = builder73.withProperty(zzaeVar73.zzb()).build();
        FieldDescriptor.Builder builder74 = FieldDescriptor.builder("aggregatedOnDeviceExplicitContentLogEvent");
        zzae zzaeVar74 = new zzae();
        zzaeVar74.zza(73);
        zzaw = builder74.withProperty(zzaeVar74.zzb()).build();
        FieldDescriptor.Builder builder75 = FieldDescriptor.builder("onDeviceFaceMeshCreateLogEvent");
        zzae zzaeVar75 = new zzae();
        zzaeVar75.zza(74);
        zzax = builder75.withProperty(zzaeVar75.zzb()).build();
        FieldDescriptor.Builder builder76 = FieldDescriptor.builder("onDeviceFaceMeshLoadLogEvent");
        zzae zzaeVar76 = new zzae();
        zzaeVar76.zza(75);
        zzay = builder76.withProperty(zzaeVar76.zzb()).build();
        FieldDescriptor.Builder builder77 = FieldDescriptor.builder("onDeviceFaceMeshLogEvent");
        zzae zzaeVar77 = new zzae();
        zzaeVar77.zza(76);
        zzaz = builder77.withProperty(zzaeVar77.zzb()).build();
        FieldDescriptor.Builder builder78 = FieldDescriptor.builder("aggregatedOnDeviceFaceMeshLogEvent");
        zzae zzaeVar78 = new zzae();
        zzaeVar78.zza(77);
        zzaA = builder78.withProperty(zzaeVar78.zzb()).build();
        FieldDescriptor.Builder builder79 = FieldDescriptor.builder("smartReplyOptionalModuleLogEvent");
        zzae zzaeVar79 = new zzae();
        zzaeVar79.zza(78);
        zzaB = builder79.withProperty(zzaeVar79.zzb()).build();
        FieldDescriptor.Builder builder80 = FieldDescriptor.builder("textDetectionOptionalModuleLogEvent");
        zzae zzaeVar80 = new zzae();
        zzaeVar80.zza(80);
        zzaC = builder80.withProperty(zzaeVar80.zzb()).build();
        FieldDescriptor.Builder builder81 = FieldDescriptor.builder("onDeviceImageQualityAnalysisCreateLogEvent");
        zzae zzaeVar81 = new zzae();
        zzaeVar81.zza(81);
        zzaD = builder81.withProperty(zzaeVar81.zzb()).build();
        FieldDescriptor.Builder builder82 = FieldDescriptor.builder("onDeviceImageQualityAnalysisLoadLogEvent");
        zzae zzaeVar82 = new zzae();
        zzaeVar82.zza(82);
        zzaE = builder82.withProperty(zzaeVar82.zzb()).build();
        FieldDescriptor.Builder builder83 = FieldDescriptor.builder("onDeviceImageQualityAnalysisLogEvent");
        zzae zzaeVar83 = new zzae();
        zzaeVar83.zza(83);
        zzaF = builder83.withProperty(zzaeVar83.zzb()).build();
        FieldDescriptor.Builder builder84 = FieldDescriptor.builder("aggregatedOnDeviceImageQualityAnalysisLogEvent");
        zzae zzaeVar84 = new zzae();
        zzaeVar84.zza(84);
        zzaG = builder84.withProperty(zzaeVar84.zzb()).build();
        FieldDescriptor.Builder builder85 = FieldDescriptor.builder("imageQualityAnalysisOptionalModuleLogEvent");
        zzae zzaeVar85 = new zzae();
        zzaeVar85.zza(88);
        zzaH = builder85.withProperty(zzaeVar85.zzb()).build();
        FieldDescriptor.Builder builder86 = FieldDescriptor.builder("imageCaptioningOptionalModuleLogEvent");
        zzae zzaeVar86 = new zzae();
        zzaeVar86.zza(89);
        zzaI = builder86.withProperty(zzaeVar86.zzb()).build();
        FieldDescriptor.Builder builder87 = FieldDescriptor.builder("onDeviceImageCaptioningCreateLogEvent");
        zzae zzaeVar87 = new zzae();
        zzaeVar87.zza(90);
        zzaJ = builder87.withProperty(zzaeVar87.zzb()).build();
        FieldDescriptor.Builder builder88 = FieldDescriptor.builder("onDeviceImageCaptioningLoadLogEvent");
        zzae zzaeVar88 = new zzae();
        zzaeVar88.zza(91);
        zzaK = builder88.withProperty(zzaeVar88.zzb()).build();
        FieldDescriptor.Builder builder89 = FieldDescriptor.builder("onDeviceImageCaptioningInferenceLogEvent");
        zzae zzaeVar89 = new zzae();
        zzaeVar89.zza(92);
        zzaL = builder89.withProperty(zzaeVar89.zzb()).build();
        FieldDescriptor.Builder builder90 = FieldDescriptor.builder("aggregatedOnDeviceImageCaptioningInferenceLogEvent");
        zzae zzaeVar90 = new zzae();
        zzaeVar90.zza(93);
        zzaM = builder90.withProperty(zzaeVar90.zzb()).build();
    }

    private zzeo() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zziy zziyVar = (zziy) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        objectEncoderContext.add(zzb, zziyVar.zzc());
        objectEncoderContext.add(zzc, zziyVar.zzb());
        objectEncoderContext.add(zzd, (Object) null);
        objectEncoderContext.add(zze, (Object) null);
        objectEncoderContext.add(zzf, (Object) null);
        objectEncoderContext.add(zzg, (Object) null);
        objectEncoderContext.add(zzh, (Object) null);
        objectEncoderContext.add(zzi, (Object) null);
        objectEncoderContext.add(zzj, (Object) null);
        objectEncoderContext.add(zzk, (Object) null);
        objectEncoderContext.add(zzl, (Object) null);
        objectEncoderContext.add(zzm, (Object) null);
        objectEncoderContext.add(zzn, (Object) null);
        objectEncoderContext.add(zzo, (Object) null);
        objectEncoderContext.add(zzp, (Object) null);
        objectEncoderContext.add(zzq, (Object) null);
        objectEncoderContext.add(zzr, (Object) null);
        objectEncoderContext.add(zzs, (Object) null);
        objectEncoderContext.add(zzt, (Object) null);
        objectEncoderContext.add(zzu, (Object) null);
        objectEncoderContext.add(zzv, (Object) null);
        objectEncoderContext.add(zzw, (Object) null);
        objectEncoderContext.add(zzx, (Object) null);
        objectEncoderContext.add(zzy, (Object) null);
        objectEncoderContext.add(zzz, (Object) null);
        objectEncoderContext.add(zzA, (Object) null);
        objectEncoderContext.add(zzB, (Object) null);
        objectEncoderContext.add(zzC, (Object) null);
        objectEncoderContext.add(zzD, (Object) null);
        objectEncoderContext.add(zzE, (Object) null);
        objectEncoderContext.add(zzF, (Object) null);
        objectEncoderContext.add(zzG, (Object) null);
        objectEncoderContext.add(zzH, (Object) null);
        objectEncoderContext.add(zzI, (Object) null);
        objectEncoderContext.add(zzJ, (Object) null);
        objectEncoderContext.add(zzK, (Object) null);
        objectEncoderContext.add(zzL, (Object) null);
        objectEncoderContext.add(zzM, (Object) null);
        objectEncoderContext.add(zzN, (Object) null);
        objectEncoderContext.add(zzO, (Object) null);
        objectEncoderContext.add(zzP, (Object) null);
        objectEncoderContext.add(zzQ, (Object) null);
        objectEncoderContext.add(zzR, (Object) null);
        objectEncoderContext.add(zzS, (Object) null);
        objectEncoderContext.add(zzT, (Object) null);
        objectEncoderContext.add(zzU, (Object) null);
        objectEncoderContext.add(zzV, (Object) null);
        objectEncoderContext.add(zzW, (Object) null);
        objectEncoderContext.add(zzX, (Object) null);
        objectEncoderContext.add(zzY, (Object) null);
        objectEncoderContext.add(zzZ, (Object) null);
        objectEncoderContext.add(zzaa, zziyVar.zza());
        objectEncoderContext.add(zzab, (Object) null);
        objectEncoderContext.add(zzac, (Object) null);
        objectEncoderContext.add(zzad, (Object) null);
        objectEncoderContext.add(zzae, (Object) null);
        objectEncoderContext.add(zzaf, (Object) null);
        objectEncoderContext.add(zzag, (Object) null);
        objectEncoderContext.add(zzah, (Object) null);
        objectEncoderContext.add(zzai, (Object) null);
        objectEncoderContext.add(zzaj, (Object) null);
        objectEncoderContext.add(zzak, (Object) null);
        objectEncoderContext.add(zzal, (Object) null);
        objectEncoderContext.add(zzam, (Object) null);
        objectEncoderContext.add(zzan, (Object) null);
        objectEncoderContext.add(zzao, (Object) null);
        objectEncoderContext.add(zzap, (Object) null);
        objectEncoderContext.add(zzaq, (Object) null);
        objectEncoderContext.add(zzar, (Object) null);
        objectEncoderContext.add(zzas, (Object) null);
        objectEncoderContext.add(zzat, (Object) null);
        objectEncoderContext.add(zzau, (Object) null);
        objectEncoderContext.add(zzav, (Object) null);
        objectEncoderContext.add(zzaw, (Object) null);
        objectEncoderContext.add(zzax, (Object) null);
        objectEncoderContext.add(zzay, (Object) null);
        objectEncoderContext.add(zzaz, (Object) null);
        objectEncoderContext.add(zzaA, (Object) null);
        objectEncoderContext.add(zzaB, (Object) null);
        objectEncoderContext.add(zzaC, (Object) null);
        objectEncoderContext.add(zzaD, (Object) null);
        objectEncoderContext.add(zzaE, (Object) null);
        objectEncoderContext.add(zzaF, (Object) null);
        objectEncoderContext.add(zzaG, (Object) null);
        objectEncoderContext.add(zzaH, (Object) null);
        objectEncoderContext.add(zzaI, (Object) null);
        objectEncoderContext.add(zzaJ, (Object) null);
        objectEncoderContext.add(zzaK, (Object) null);
        objectEncoderContext.add(zzaL, (Object) null);
        objectEncoderContext.add(zzaM, (Object) null);
    }
}
