package j$.wrappers;

import java.util.function.IntPredicate;
/* renamed from: j$.wrappers.$r8$wrapper$java$util$function$IntPredicate$-WRP */
/* loaded from: classes2.dex */
public final /* synthetic */ class C$r8$wrapper$java$util$function$IntPredicate$WRP implements IntPredicate {
    final /* synthetic */ j$.util.function.IntPredicate wrappedValue;

    private /* synthetic */ C$r8$wrapper$java$util$function$IntPredicate$WRP(j$.util.function.IntPredicate intPredicate) {
        this.wrappedValue = intPredicate;
    }

    public static /* synthetic */ IntPredicate convert(j$.util.function.IntPredicate intPredicate) {
        if (intPredicate == null) {
            return null;
        }
        return intPredicate instanceof C$r8$wrapper$java$util$function$IntPredicate$VWRP ? ((C$r8$wrapper$java$util$function$IntPredicate$VWRP) intPredicate).wrappedValue : new C$r8$wrapper$java$util$function$IntPredicate$WRP(intPredicate);
    }

    @Override // java.util.function.IntPredicate
    public /* synthetic */ IntPredicate and(IntPredicate intPredicate) {
        return convert(this.wrappedValue.and(C$r8$wrapper$java$util$function$IntPredicate$VWRP.convert(intPredicate)));
    }

    @Override // java.util.function.IntPredicate
    public /* synthetic */ IntPredicate negate() {
        return convert(this.wrappedValue.negate());
    }

    @Override // java.util.function.IntPredicate
    public /* synthetic */ IntPredicate or(IntPredicate intPredicate) {
        return convert(this.wrappedValue.or(C$r8$wrapper$java$util$function$IntPredicate$VWRP.convert(intPredicate)));
    }

    @Override // java.util.function.IntPredicate
    public /* synthetic */ boolean test(int i) {
        return this.wrappedValue.test(i);
    }
}
