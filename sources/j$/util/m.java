package j$.util;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class m implements j$.util.function.f {
    public final /* synthetic */ Consumer a;

    @Override // j$.util.function.f
    public final void accept(double d) {
        this.a.accept(Double.valueOf(d));
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }
}