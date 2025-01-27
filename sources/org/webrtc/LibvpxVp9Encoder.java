package org.webrtc;

/* loaded from: classes5.dex */
public class LibvpxVp9Encoder extends WrappedNativeVideoEncoder {
    static native long nativeCreateEncoder();

    static native boolean nativeIsSupported();

    @Override // org.webrtc.WrappedNativeVideoEncoder, org.webrtc.VideoEncoder
    public long createNativeVideoEncoder() {
        return nativeCreateEncoder();
    }

    @Override // org.webrtc.WrappedNativeVideoEncoder, org.webrtc.VideoEncoder
    public boolean isHardwareEncoder() {
        return false;
    }
}
