package j$.wrappers;

import j$.util.Spliterator;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
/* renamed from: j$.wrappers.$r8$wrapper$java$util$Spliterator$OfPrimitive$-WRP */
/* loaded from: classes2.dex */
public final /* synthetic */ class C$r8$wrapper$java$util$Spliterator$OfPrimitive$WRP implements Spliterator.OfPrimitive {
    final /* synthetic */ Spliterator.OfPrimitive wrappedValue;

    private /* synthetic */ C$r8$wrapper$java$util$Spliterator$OfPrimitive$WRP(Spliterator.OfPrimitive ofPrimitive) {
        this.wrappedValue = ofPrimitive;
    }

    public static /* synthetic */ Spliterator.OfPrimitive convert(Spliterator.OfPrimitive ofPrimitive) {
        if (ofPrimitive == null) {
            return null;
        }
        return ofPrimitive instanceof C$r8$wrapper$java$util$Spliterator$OfPrimitive$VWRP ? ((C$r8$wrapper$java$util$Spliterator$OfPrimitive$VWRP) ofPrimitive).wrappedValue : new C$r8$wrapper$java$util$Spliterator$OfPrimitive$WRP(ofPrimitive);
    }

    @Override // java.util.Spliterator
    public /* synthetic */ int characteristics() {
        return this.wrappedValue.characteristics();
    }

    @Override // java.util.Spliterator
    public /* synthetic */ long estimateSize() {
        return this.wrappedValue.estimateSize();
    }

    @Override // java.util.Spliterator.OfPrimitive
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.wrappedValue.forEachRemaining((Spliterator.OfPrimitive) obj);
    }

    @Override // java.util.Spliterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.wrappedValue.forEachRemaining(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
    }

    @Override // java.util.Spliterator
    public /* synthetic */ Comparator getComparator() {
        return this.wrappedValue.getComparator();
    }

    @Override // java.util.Spliterator
    public /* synthetic */ long getExactSizeIfKnown() {
        return this.wrappedValue.getExactSizeIfKnown();
    }

    @Override // java.util.Spliterator
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return this.wrappedValue.hasCharacteristics(i);
    }

    @Override // java.util.Spliterator.OfPrimitive
    public /* synthetic */ boolean tryAdvance(Object obj) {
        return this.wrappedValue.tryAdvance((Spliterator.OfPrimitive) obj);
    }

    @Override // java.util.Spliterator
    public /* synthetic */ boolean tryAdvance(Consumer consumer) {
        return this.wrappedValue.tryAdvance(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
    }
}
