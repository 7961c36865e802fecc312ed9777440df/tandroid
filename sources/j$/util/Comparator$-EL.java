package j$.util;

import j$.util.function.ToIntFunction;
import java.util.Collections;
import java.util.Comparator;

/* loaded from: classes2.dex */
public final /* synthetic */ class Comparator$-EL {
    public static d a(Comparator comparator, Comparator comparator2) {
        if (!(comparator instanceof e)) {
            comparator2.getClass();
            return new d(comparator, comparator2, 0);
        }
        f fVar = (f) ((e) comparator);
        fVar.getClass();
        comparator2.getClass();
        return new d(fVar, comparator2, 0);
    }

    public static Comparator reversed(Comparator comparator) {
        if (!(comparator instanceof e)) {
            return Collections.reverseOrder(comparator);
        }
        ((f) ((e) comparator)).getClass();
        return Comparator$-CC.reverseOrder();
    }

    public static Comparator thenComparingInt(Comparator comparator, ToIntFunction toIntFunction) {
        if (!(comparator instanceof e)) {
            return a(comparator, Comparator$-CC.comparingInt(toIntFunction));
        }
        f fVar = (f) ((e) comparator);
        fVar.getClass();
        return a(fVar, Comparator$-CC.comparingInt(toIntFunction));
    }
}
