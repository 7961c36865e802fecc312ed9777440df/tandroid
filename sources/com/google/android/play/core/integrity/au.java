package com.google.android.play.core.integrity;

/* loaded from: classes.dex */
public final class au implements com.google.android.play.integrity.internal.ak {
    private final com.google.android.play.integrity.internal.an a;
    private final com.google.android.play.integrity.internal.an b;

    public au(com.google.android.play.integrity.internal.an anVar, com.google.android.play.integrity.internal.an anVar2) {
        this.a = anVar;
        this.b = anVar2;
    }

    @Override // com.google.android.play.integrity.internal.an
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public final at a() {
        return new at(this.a, this.b);
    }
}