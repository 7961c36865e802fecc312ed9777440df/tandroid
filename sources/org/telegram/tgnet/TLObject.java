package org.telegram.tgnet;

/* loaded from: classes.dex */
public class TLObject {
    private static final ThreadLocal<NativeByteBuffer> sizeCalculator = new ThreadLocal<NativeByteBuffer>() { // from class: org.telegram.tgnet.TLObject.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public NativeByteBuffer initialValue() {
            return new NativeByteBuffer(true);
        }
    };
    public boolean disableFree = false;
    public int networkType;

    public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
        return null;
    }

    public void freeResources() {
    }

    public int getObjectSize() {
        ThreadLocal<NativeByteBuffer> threadLocal = sizeCalculator;
        NativeByteBuffer nativeByteBuffer = threadLocal.get();
        nativeByteBuffer.rewind();
        serializeToStream(threadLocal.get());
        return nativeByteBuffer.length();
    }

    public void readParams(InputSerializedData inputSerializedData, boolean z) {
    }

    public void serializeToStream(OutputSerializedData outputSerializedData) {
    }
}
