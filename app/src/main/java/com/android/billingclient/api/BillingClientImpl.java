package com.android.billingclient.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zzu;
import com.google.android.gms.internal.play_billing.zzz;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public class BillingClientImpl extends BillingClient {
    private volatile int zza;
    private final String zzb;
    private final Handler zzc;
    private volatile zzo zzd;
    private Context zze;
    private volatile com.google.android.gms.internal.play_billing.zze zzf;
    private volatile zzap zzg;
    private boolean zzh;
    private boolean zzi;
    private int zzj;
    private boolean zzk;
    private boolean zzl;
    private boolean zzm;
    private boolean zzn;
    private boolean zzo;
    private boolean zzp;
    private boolean zzq;
    private boolean zzr;
    private boolean zzs;
    private boolean zzt;
    private boolean zzu;
    private ExecutorService zzv;

    private void initialize(Context context, PurchasesUpdatedListener purchasesUpdatedListener, boolean z, zzc zzcVar) {
        Context applicationContext = context.getApplicationContext();
        this.zze = applicationContext;
        this.zzd = new zzo(applicationContext, purchasesUpdatedListener, zzcVar);
        this.zzt = z;
        this.zzu = zzcVar != null;
    }

    public final Handler zzF() {
        return Looper.myLooper() == null ? this.zzc : new Handler(Looper.myLooper());
    }

    private final BillingResult zzG(BillingResult billingResult) {
        if (Thread.interrupted()) {
            return billingResult;
        }
        this.zzc.post(new zzag(this, billingResult));
        return billingResult;
    }

    public final BillingResult zzH() {
        if (this.zza == 0 || this.zza == 3) {
            return zzbb.zzm;
        }
        return zzbb.zzj;
    }

    @SuppressLint({"PrivateApi"})
    private static String zzI() {
        try {
            return (String) Class.forName("com.android.billingclient.ktx.BuildConfig").getField("VERSION_NAME").get(null);
        } catch (Exception unused) {
            return "5.0.0";
        }
    }

    public final Future zzJ(Callable callable, long j, Runnable runnable, Handler handler) {
        double d = j;
        Double.isNaN(d);
        long j2 = (long) (d * 0.95d);
        if (this.zzv == null) {
            this.zzv = Executors.newFixedThreadPool(zzb.zza, new zzal(this));
        }
        try {
            Future submit = this.zzv.submit(callable);
            handler.postDelayed(new zzaf(submit, runnable), j2);
            return submit;
        } catch (Exception e) {
            zzb.zzp("BillingClient", "Async task throws exception!", e);
            return null;
        }
    }

    private final void zzM(String str, PurchasesResponseListener purchasesResponseListener) {
        if (!isReady()) {
            purchasesResponseListener.onQueryPurchasesResponse(zzbb.zzm, zzu.zzl());
        } else if (TextUtils.isEmpty(str)) {
            zzb.zzo("BillingClient", "Please provide a valid product type.");
            purchasesResponseListener.onQueryPurchasesResponse(zzbb.zzg, zzu.zzl());
        } else if (zzJ(new zzai(this, str, purchasesResponseListener), 30000L, new zzad(purchasesResponseListener), zzF()) != null) {
        } else {
            purchasesResponseListener.onQueryPurchasesResponse(zzH(), zzu.zzl());
        }
    }

    public static /* bridge */ /* synthetic */ zzbh zzi(BillingClientImpl billingClientImpl, String str) {
        Bundle bundle;
        zzb.zzn("BillingClient", "Querying owned items, item type: ".concat(String.valueOf(str)));
        ArrayList arrayList = new ArrayList();
        Bundle zzh = zzb.zzh(billingClientImpl.zzm, billingClientImpl.zzt, billingClientImpl.zzb);
        String str2 = null;
        do {
            try {
                if (billingClientImpl.zzm) {
                    bundle = billingClientImpl.zzf.zzj(9, billingClientImpl.zze.getPackageName(), str, str2, zzh);
                } else {
                    bundle = billingClientImpl.zzf.zzi(3, billingClientImpl.zze.getPackageName(), str, str2);
                }
                BillingResult zza = zzbi.zza(bundle, "BillingClient", "getPurchase()");
                if (zza != zzbb.zzl) {
                    return new zzbh(zza, null);
                }
                ArrayList<String> stringArrayList = bundle.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> stringArrayList2 = bundle.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> stringArrayList3 = bundle.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                for (int i = 0; i < stringArrayList2.size(); i++) {
                    String str3 = stringArrayList2.get(i);
                    String str4 = stringArrayList3.get(i);
                    zzb.zzn("BillingClient", "Sku is owned: ".concat(String.valueOf(stringArrayList.get(i))));
                    try {
                        Purchase purchase = new Purchase(str3, str4);
                        if (TextUtils.isEmpty(purchase.getPurchaseToken())) {
                            zzb.zzo("BillingClient", "BUG: empty/null token!");
                        }
                        arrayList.add(purchase);
                    } catch (JSONException e) {
                        zzb.zzp("BillingClient", "Got an exception trying to decode the purchase!", e);
                        return new zzbh(zzbb.zzj, null);
                    }
                }
                str2 = bundle.getString("INAPP_CONTINUATION_TOKEN");
                zzb.zzn("BillingClient", "Continuation token: ".concat(String.valueOf(str2)));
            } catch (Exception e2) {
                zzb.zzp("BillingClient", "Got exception trying to get purchasesm try to reconnect", e2);
                return new zzbh(zzbb.zzm, null);
            }
        } while (!TextUtils.isEmpty(str2));
        return new zzbh(zzbb.zzl, arrayList);
    }

    @Override // com.android.billingclient.api.BillingClient
    public final boolean isReady() {
        return (this.zza != 2 || this.zzf == null || this.zzg == null) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x02b0  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x02bb  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0307 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0312  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0317  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0360 A[Catch: Exception -> 0x03a0, CancellationException -> 0x03ac, TimeoutException -> 0x03ae, TryCatch #4 {CancellationException -> 0x03ac, TimeoutException -> 0x03ae, Exception -> 0x03a0, blocks: (B:131:0x034e, B:133:0x0360, B:135:0x0386), top: B:150:0x034e }] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0386 A[Catch: Exception -> 0x03a0, CancellationException -> 0x03ac, TimeoutException -> 0x03ae, TRY_LEAVE, TryCatch #4 {CancellationException -> 0x03ac, TimeoutException -> 0x03ae, Exception -> 0x03a0, blocks: (B:131:0x034e, B:133:0x0360, B:135:0x0386), top: B:150:0x034e }] */
    @Override // com.android.billingclient.api.BillingClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final BillingResult launchBillingFlow(Activity activity, BillingFlowParams billingFlowParams) {
        String str;
        String str2;
        String str3;
        String str4;
        Future future;
        Throwable e;
        int zzb;
        SkuDetails skuDetails;
        BillingFlowParams.ProductDetailsParams productDetailsParams;
        String str5;
        String str6;
        String str7;
        String str8;
        boolean z;
        String str9;
        Intent intent;
        int i;
        String str10;
        String str11;
        if (!isReady()) {
            BillingResult billingResult = zzbb.zzm;
            zzG(billingResult);
            return billingResult;
        }
        ArrayList<SkuDetails> zze = billingFlowParams.zze();
        List zzf = billingFlowParams.zzf();
        SkuDetails skuDetails2 = (SkuDetails) zzz.zza(zze, null);
        BillingFlowParams.ProductDetailsParams productDetailsParams2 = (BillingFlowParams.ProductDetailsParams) zzz.zza(zzf, null);
        if (skuDetails2 != null) {
            str2 = skuDetails2.getSku();
            str = skuDetails2.getType();
        } else {
            str2 = productDetailsParams2.zza().getProductId();
            str = productDetailsParams2.zza().getProductType();
        }
        String str12 = "BillingClient";
        if (!str.equals("subs") || this.zzh) {
            if (!billingFlowParams.zzo() || this.zzk) {
                if (zze.size() <= 1 || this.zzr) {
                    if (zzf.isEmpty() || this.zzs) {
                        if (this.zzk) {
                            Bundle zzf2 = zzb.zzf(billingFlowParams, this.zzm, this.zzt, this.zzu, this.zzb);
                            str3 = "BUY_INTENT";
                            if (!zze.isEmpty()) {
                                ArrayList<String> arrayList = new ArrayList<>();
                                ArrayList<String> arrayList2 = new ArrayList<>();
                                str8 = str;
                                ArrayList<String> arrayList3 = new ArrayList<>();
                                str7 = str2;
                                ArrayList<Integer> arrayList4 = new ArrayList<>();
                                str6 = "proxyPackageVersion";
                                ArrayList<String> arrayList5 = new ArrayList<>();
                                boolean z2 = false;
                                boolean z3 = false;
                                boolean z4 = false;
                                boolean z5 = false;
                                for (SkuDetails skuDetails3 : zze) {
                                    if (!skuDetails3.zzf().isEmpty()) {
                                        str11 = str12;
                                        arrayList.add(skuDetails3.zzf());
                                    } else {
                                        str11 = str12;
                                    }
                                    String zzc = skuDetails3.zzc();
                                    BillingFlowParams.ProductDetailsParams productDetailsParams3 = productDetailsParams2;
                                    String zzb2 = skuDetails3.zzb();
                                    int zza = skuDetails3.zza();
                                    SkuDetails skuDetails4 = skuDetails2;
                                    String zze2 = skuDetails3.zze();
                                    arrayList2.add(zzc);
                                    z2 |= !TextUtils.isEmpty(zzc);
                                    arrayList3.add(zzb2);
                                    z3 |= !TextUtils.isEmpty(zzb2);
                                    arrayList4.add(Integer.valueOf(zza));
                                    z4 |= zza != 0;
                                    z5 |= !TextUtils.isEmpty(zze2);
                                    arrayList5.add(zze2);
                                    str12 = str11;
                                    productDetailsParams2 = productDetailsParams3;
                                    skuDetails2 = skuDetails4;
                                }
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams2;
                                str5 = str12;
                                if (!arrayList.isEmpty()) {
                                    zzf2.putStringArrayList("skuDetailsTokens", arrayList);
                                }
                                if (z2) {
                                    zzf2.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList2);
                                }
                                if (z3) {
                                    zzf2.putStringArrayList("SKU_OFFER_ID_LIST", arrayList3);
                                }
                                if (z4) {
                                    zzf2.putIntegerArrayList("SKU_OFFER_TYPE_LIST", arrayList4);
                                }
                                if (z5) {
                                    zzf2.putStringArrayList("SKU_SERIALIZED_DOCID_LIST", arrayList5);
                                }
                                if (zze.size() > 1) {
                                    ArrayList<String> arrayList6 = new ArrayList<>(zze.size() - 1);
                                    ArrayList<String> arrayList7 = new ArrayList<>(zze.size() - 1);
                                    for (int i2 = 1; i2 < zze.size(); i2++) {
                                        arrayList6.add(((SkuDetails) zze.get(i2)).getSku());
                                        arrayList7.add(((SkuDetails) zze.get(i2)).getType());
                                    }
                                    zzf2.putStringArrayList("additionalSkus", arrayList6);
                                    zzf2.putStringArrayList("additionalSkuTypes", arrayList7);
                                }
                            } else {
                                str6 = "proxyPackageVersion";
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams2;
                                str7 = str2;
                                str8 = str;
                                str5 = str12;
                                ArrayList<String> arrayList8 = new ArrayList<>(zzf.size() - 1);
                                ArrayList<String> arrayList9 = new ArrayList<>(zzf.size() - 1);
                                ArrayList<String> arrayList10 = new ArrayList<>();
                                ArrayList<String> arrayList11 = new ArrayList<>();
                                for (int i3 = 0; i3 < zzf.size(); i3++) {
                                    BillingFlowParams.ProductDetailsParams productDetailsParams4 = (BillingFlowParams.ProductDetailsParams) zzf.get(i3);
                                    ProductDetails zza2 = productDetailsParams4.zza();
                                    if (!zza2.zzb().isEmpty()) {
                                        arrayList10.add(zza2.zzb());
                                    }
                                    arrayList11.add(productDetailsParams4.zzb());
                                    if (i3 > 0) {
                                        arrayList8.add(((BillingFlowParams.ProductDetailsParams) zzf.get(i3)).zza().getProductId());
                                        arrayList9.add(((BillingFlowParams.ProductDetailsParams) zzf.get(i3)).zza().getProductType());
                                    }
                                }
                                zzf2.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList11);
                                if (!arrayList10.isEmpty()) {
                                    zzf2.putStringArrayList("skuDetailsTokens", arrayList10);
                                }
                                if (!arrayList8.isEmpty()) {
                                    zzf2.putStringArrayList("additionalSkus", arrayList8);
                                    zzf2.putStringArrayList("additionalSkuTypes", arrayList9);
                                }
                            }
                            if (!zzf2.containsKey("SKU_OFFER_ID_TOKEN_LIST") || this.zzp) {
                                if (skuDetails == null || TextUtils.isEmpty(skuDetails.zzd())) {
                                    if (productDetailsParams != null && !TextUtils.isEmpty(productDetailsParams.zza().zza())) {
                                        zzf2.putString("skuPackageName", productDetailsParams.zza().zza());
                                    } else {
                                        str9 = null;
                                        z = false;
                                        if (!TextUtils.isEmpty(str9)) {
                                            zzf2.putString("accountName", str9);
                                        }
                                        intent = activity.getIntent();
                                        if (intent != null) {
                                            str4 = str5;
                                            zzb.zzo(str4, "Activity's intent is null.");
                                        } else {
                                            str4 = str5;
                                            if (!TextUtils.isEmpty(intent.getStringExtra("PROXY_PACKAGE"))) {
                                                String stringExtra = intent.getStringExtra("PROXY_PACKAGE");
                                                zzf2.putString("proxyPackage", stringExtra);
                                                try {
                                                    str10 = str6;
                                                    try {
                                                        zzf2.putString(str10, this.zze.getPackageManager().getPackageInfo(stringExtra, 0).versionName);
                                                    } catch (PackageManager.NameNotFoundException unused) {
                                                        zzf2.putString(str10, "package not found");
                                                        if (this.zzs) {
                                                        }
                                                        if (this.zzq) {
                                                        }
                                                        future = zzJ(new zzab(this, i, str7, str8, billingFlowParams, zzf2), 5000L, null, this.zzc);
                                                        Bundle bundle = (Bundle) future.get(5000L, TimeUnit.MILLISECONDS);
                                                        zzb = zzb.zzb(bundle, str4);
                                                        String zzk = zzb.zzk(bundle, str4);
                                                        if (zzb == 0) {
                                                        }
                                                    }
                                                } catch (PackageManager.NameNotFoundException unused2) {
                                                    str10 = str6;
                                                }
                                            }
                                        }
                                        if (this.zzs || zzf.isEmpty()) {
                                            i = (this.zzq || !z) ? this.zzm ? 9 : 6 : 15;
                                        } else {
                                            i = 17;
                                        }
                                        future = zzJ(new zzab(this, i, str7, str8, billingFlowParams, zzf2), 5000L, null, this.zzc);
                                    }
                                } else {
                                    zzf2.putString("skuPackageName", skuDetails.zzd());
                                }
                                str9 = null;
                                z = true;
                                if (!TextUtils.isEmpty(str9)) {
                                }
                                intent = activity.getIntent();
                                if (intent != null) {
                                }
                                if (this.zzs) {
                                }
                                if (this.zzq) {
                                }
                                future = zzJ(new zzab(this, i, str7, str8, billingFlowParams, zzf2), 5000L, null, this.zzc);
                            } else {
                                BillingResult billingResult2 = zzbb.zzu;
                                zzG(billingResult2);
                                return billingResult2;
                            }
                        } else {
                            str3 = "BUY_INTENT";
                            str4 = str12;
                            future = zzJ(new zzac(this, str2, str), 5000L, null, this.zzc);
                        }
                        try {
                            Bundle bundle2 = (Bundle) future.get(5000L, TimeUnit.MILLISECONDS);
                            zzb = zzb.zzb(bundle2, str4);
                            String zzk2 = zzb.zzk(bundle2, str4);
                            if (zzb == 0) {
                                zzb.zzo(str4, "Unable to buy item, Error response code: " + zzb);
                                BillingResult.Builder newBuilder = BillingResult.newBuilder();
                                newBuilder.setResponseCode(zzb);
                                newBuilder.setDebugMessage(zzk2);
                                BillingResult build = newBuilder.build();
                                zzG(build);
                                return build;
                            }
                            Intent intent2 = new Intent(activity, ProxyBillingActivity.class);
                            String str13 = str3;
                            intent2.putExtra(str13, (PendingIntent) bundle2.getParcelable(str13));
                            activity.startActivity(intent2);
                            return zzbb.zzl;
                        } catch (CancellationException e2) {
                            e = e2;
                            zzb.zzp(str4, "Time out while launching billing flow. Try to reconnect", e);
                            BillingResult billingResult3 = zzbb.zzn;
                            zzG(billingResult3);
                            return billingResult3;
                        } catch (TimeoutException e3) {
                            e = e3;
                            zzb.zzp(str4, "Time out while launching billing flow. Try to reconnect", e);
                            BillingResult billingResult32 = zzbb.zzn;
                            zzG(billingResult32);
                            return billingResult32;
                        } catch (Exception e4) {
                            zzb.zzp(str4, "Exception while launching billing flow. Try to reconnect", e4);
                            BillingResult billingResult4 = zzbb.zzm;
                            zzG(billingResult4);
                            return billingResult4;
                        }
                    }
                    zzb.zzo(str12, "Current client doesn't support purchases with ProductDetails.");
                    BillingResult billingResult5 = zzbb.zzv;
                    zzG(billingResult5);
                    return billingResult5;
                }
                zzb.zzo(str12, "Current client doesn't support multi-item purchases.");
                BillingResult billingResult6 = zzbb.zzt;
                zzG(billingResult6);
                return billingResult6;
            }
            zzb.zzo(str12, "Current client doesn't support extra params for buy intent.");
            BillingResult billingResult7 = zzbb.zzh;
            zzG(billingResult7);
            return billingResult7;
        }
        zzb.zzo(str12, "Current client doesn't support subscriptions.");
        BillingResult billingResult8 = zzbb.zzo;
        zzG(billingResult8);
        return billingResult8;
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryProductDetailsAsync(QueryProductDetailsParams queryProductDetailsParams, ProductDetailsResponseListener productDetailsResponseListener) {
        if (!isReady()) {
            productDetailsResponseListener.onProductDetailsResponse(zzbb.zzm, new ArrayList());
        } else if (!this.zzs) {
            zzb.zzo("BillingClient", "Querying product details is not supported.");
            productDetailsResponseListener.onProductDetailsResponse(zzbb.zzv, new ArrayList());
        } else if (zzJ(new zzs(this, queryProductDetailsParams, productDetailsResponseListener), 30000L, new zzt(productDetailsResponseListener), zzF()) != null) {
        } else {
            productDetailsResponseListener.onProductDetailsResponse(zzH(), new ArrayList());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryPurchasesAsync(QueryPurchasesParams queryPurchasesParams, PurchasesResponseListener purchasesResponseListener) {
        zzM(queryPurchasesParams.zza(), purchasesResponseListener);
    }

    public final /* synthetic */ void zzE(BillingResult billingResult) {
        if (this.zzd.zzc() != null) {
            this.zzd.zzc().onPurchasesUpdated(billingResult, null);
            return;
        }
        this.zzd.zzb();
        zzb.zzo("BillingClient", "No valid listener is set in BroadcastManager");
    }

    public final /* synthetic */ Bundle zzc(int i, String str, String str2, BillingFlowParams billingFlowParams, Bundle bundle) throws Exception {
        return this.zzf.zzg(i, this.zze.getPackageName(), str, str2, null, bundle);
    }

    public final /* synthetic */ Bundle zzd(String str, String str2) throws Exception {
        return this.zzf.zzf(3, this.zze.getPackageName(), str, str2, null);
    }

    public final /* synthetic */ Object zzm(QueryProductDetailsParams queryProductDetailsParams, ProductDetailsResponseListener productDetailsResponseListener) throws Exception {
        String str;
        ArrayList arrayList = new ArrayList();
        String zzb = queryProductDetailsParams.zzb();
        zzu zza = queryProductDetailsParams.zza();
        int size = zza.size();
        int i = 0;
        int i2 = 0;
        while (true) {
            str = "Item is unavailable for purchase.";
            if (i2 >= size) {
                str = "";
                break;
            }
            int i3 = i2 + 20;
            ArrayList arrayList2 = new ArrayList(zza.subList(i2, i3 > size ? size : i3));
            ArrayList<String> arrayList3 = new ArrayList<>();
            int size2 = arrayList2.size();
            for (int i4 = 0; i4 < size2; i4++) {
                arrayList3.add(((QueryProductDetailsParams.Product) arrayList2.get(i4)).zza());
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("ITEM_ID_LIST", arrayList3);
            bundle.putString("playBillingLibraryVersion", this.zzb);
            try {
                Bundle zzl = this.zzf.zzl(17, this.zze.getPackageName(), zzb, bundle, zzb.zzg(this.zzb, arrayList2, null));
                if (zzl != null) {
                    if (zzl.containsKey("DETAILS_LIST")) {
                        ArrayList<String> stringArrayList = zzl.getStringArrayList("DETAILS_LIST");
                        if (stringArrayList != null) {
                            for (int i5 = 0; i5 < stringArrayList.size(); i5++) {
                                try {
                                    ProductDetails productDetails = new ProductDetails(stringArrayList.get(i5));
                                    zzb.zzn("BillingClient", "Got product details: ".concat(productDetails.toString()));
                                    arrayList.add(productDetails);
                                } catch (JSONException e) {
                                    zzb.zzp("BillingClient", "Got a JSON exception trying to decode ProductDetails. \n Exception: ", e);
                                    str = "Error trying to decode SkuDetails.";
                                    i = 6;
                                    BillingResult.Builder newBuilder = BillingResult.newBuilder();
                                    newBuilder.setResponseCode(i);
                                    newBuilder.setDebugMessage(str);
                                    productDetailsResponseListener.onProductDetailsResponse(newBuilder.build(), arrayList);
                                    return null;
                                }
                            }
                            i2 = i3;
                        } else {
                            zzb.zzo("BillingClient", "queryProductDetailsAsync got null response list");
                            break;
                        }
                    } else {
                        i = zzb.zzb(zzl, "BillingClient");
                        str = zzb.zzk(zzl, "BillingClient");
                        if (i != 0) {
                            zzb.zzo("BillingClient", "getSkuDetails() failed for queryProductDetailsAsync. Response code: " + i);
                        } else {
                            zzb.zzo("BillingClient", "getSkuDetails() returned a bundle with neither an error nor a product detail list for queryProductDetailsAsync.");
                        }
                    }
                } else {
                    zzb.zzo("BillingClient", "queryProductDetailsAsync got empty product details response.");
                    break;
                }
            } catch (Exception e2) {
                zzb.zzp("BillingClient", "queryProductDetailsAsync got a remote exception (try to reconnect).", e2);
                str = "An internal error occurred.";
            }
        }
        i = 4;
        BillingResult.Builder newBuilder2 = BillingResult.newBuilder();
        newBuilder2.setResponseCode(i);
        newBuilder2.setDebugMessage(str);
        productDetailsResponseListener.onProductDetailsResponse(newBuilder2.build(), arrayList);
        return null;
    }

    private BillingClientImpl(Context context, boolean z, PurchasesUpdatedListener purchasesUpdatedListener, String str, String str2, zzc zzcVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzj = 0;
        this.zzb = str;
        initialize(context, purchasesUpdatedListener, z, null);
    }

    public BillingClientImpl(String str, boolean z, Context context, zzbe zzbeVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzj = 0;
        this.zzb = zzI();
        Context applicationContext = context.getApplicationContext();
        this.zze = applicationContext;
        this.zzd = new zzo(applicationContext, null);
        this.zzt = z;
    }

    public BillingClientImpl(String str, boolean z, Context context, PurchasesUpdatedListener purchasesUpdatedListener, zzc zzcVar) {
        this(context, z, purchasesUpdatedListener, zzI(), null, null);
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void startConnection(BillingClientStateListener billingClientStateListener) {
        ServiceInfo serviceInfo;
        if (isReady()) {
            zzb.zzn("BillingClient", "Service connection is valid. No need to re-initialize.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzl);
        } else if (this.zza == 1) {
            zzb.zzo("BillingClient", "Client is already in the process of connecting to billing service.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzd);
        } else if (this.zza == 3) {
            zzb.zzo("BillingClient", "Client was already closed and can't be reused. Please create another instance.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzm);
        } else {
            this.zza = 1;
            this.zzd.zze();
            zzb.zzn("BillingClient", "Starting in-app billing setup.");
            this.zzg = new zzap(this, billingClientStateListener, null);
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            List<ResolveInfo> queryIntentServices = this.zze.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices != null && !queryIntentServices.isEmpty() && (serviceInfo = queryIntentServices.get(0).serviceInfo) != null) {
                String str = serviceInfo.packageName;
                String str2 = serviceInfo.name;
                if (!"com.android.vending".equals(str) || str2 == null) {
                    zzb.zzo("BillingClient", "The device doesn't have valid Play Store.");
                } else {
                    ComponentName componentName = new ComponentName(str, str2);
                    Intent intent2 = new Intent(intent);
                    intent2.setComponent(componentName);
                    intent2.putExtra("playBillingLibraryVersion", this.zzb);
                    if (this.zze.bindService(intent2, this.zzg, 1)) {
                        zzb.zzn("BillingClient", "Service was bonded successfully.");
                        return;
                    }
                    zzb.zzo("BillingClient", "Connection to Billing service is blocked.");
                }
            }
            this.zza = 0;
            zzb.zzn("BillingClient", "Billing service unavailable on device.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzc);
        }
    }
}