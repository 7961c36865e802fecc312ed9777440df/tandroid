package com.google.android.gms.auth.api.signin.internal;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

/* loaded from: classes.dex */
final class zbh extends zba {
    final /* synthetic */ zbi zba;

    zbh(zbi zbiVar) {
        this.zba = zbiVar;
    }

    @Override // com.google.android.gms.auth.api.signin.internal.zba, com.google.android.gms.auth.api.signin.internal.zbr
    public final void zbc(Status status) {
        this.zba.setResult((Result) status);
    }
}
