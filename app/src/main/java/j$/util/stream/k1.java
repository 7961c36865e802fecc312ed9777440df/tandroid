package j$.util.stream;
/* loaded from: classes2.dex */
enum k1 {
    ANY(true, true),
    ALL(false, false),
    NONE(true, false);
    
    private final boolean a;
    private final boolean b;

    k1(boolean z, boolean z2) {
        this.a = z;
        this.b = z2;
    }
}