package j$.wrappers;

import java.util.function.LongPredicate;
/* renamed from: j$.wrappers.$r8$wrapper$java$util$function$LongPredicate$-WRP */
/* loaded from: classes2.dex */
public final /* synthetic */ class C$r8$wrapper$java$util$function$LongPredicate$WRP implements LongPredicate {
    final /* synthetic */ j$.util.function.LongPredicate wrappedValue;

    private /* synthetic */ C$r8$wrapper$java$util$function$LongPredicate$WRP(j$.util.function.LongPredicate longPredicate) {
        this.wrappedValue = longPredicate;
    }

    public static /* synthetic */ LongPredicate convert(j$.util.function.LongPredicate longPredicate) {
        if (longPredicate == null) {
            return null;
        }
        return longPredicate instanceof C$r8$wrapper$java$util$function$LongPredicate$VWRP ? ((C$r8$wrapper$java$util$function$LongPredicate$VWRP) longPredicate).wrappedValue : new C$r8$wrapper$java$util$function$LongPredicate$WRP(longPredicate);
    }

    @Override // java.util.function.LongPredicate
    public /* synthetic */ LongPredicate and(LongPredicate longPredicate) {
        return convert(this.wrappedValue.and(C$r8$wrapper$java$util$function$LongPredicate$VWRP.convert(longPredicate)));
    }

    @Override // java.util.function.LongPredicate
    public /* synthetic */ LongPredicate negate() {
        return convert(this.wrappedValue.negate());
    }

    @Override // java.util.function.LongPredicate
    public /* synthetic */ LongPredicate or(LongPredicate longPredicate) {
        return convert(this.wrappedValue.or(C$r8$wrapper$java$util$function$LongPredicate$VWRP.convert(longPredicate)));
    }

    @Override // java.util.function.LongPredicate
    public /* synthetic */ boolean test(long j) {
        return this.wrappedValue.test(j);
    }
}
