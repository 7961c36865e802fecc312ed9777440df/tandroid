package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Document;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ImageLoader f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$10;
    public final /* synthetic */ Object f$11;
    public final /* synthetic */ int f$12;
    public final /* synthetic */ TLRPC$Document f$13;
    public final /* synthetic */ boolean f$14;
    public final /* synthetic */ boolean f$15;
    public final /* synthetic */ String f$16;
    public final /* synthetic */ int f$17;
    public final /* synthetic */ long f$18;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ ImageReceiver f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ String f$7;
    public final /* synthetic */ int f$8;
    public final /* synthetic */ ImageLocation f$9;

    public /* synthetic */ ImageLoader$$ExternalSyntheticLambda2(ImageLoader imageLoader, int i, String str, String str2, int i2, ImageReceiver imageReceiver, int i3, String str3, int i4, ImageLocation imageLocation, boolean z, Object obj, int i5, TLRPC$Document tLRPC$Document, boolean z2, boolean z3, String str4, int i6, long j) {
        this.f$0 = imageLoader;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = str2;
        this.f$4 = i2;
        this.f$5 = imageReceiver;
        this.f$6 = i3;
        this.f$7 = str3;
        this.f$8 = i4;
        this.f$9 = imageLocation;
        this.f$10 = z;
        this.f$11 = obj;
        this.f$12 = i5;
        this.f$13 = tLRPC$Document;
        this.f$14 = z2;
        this.f$15 = z3;
        this.f$16 = str4;
        this.f$17 = i6;
        this.f$18 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createLoadOperationForImageReceiver$6(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13, this.f$14, this.f$15, this.f$16, this.f$17, this.f$18);
    }
}