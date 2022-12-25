package com.google.android.exoplayer2;

import android.annotation.SuppressLint;
/* loaded from: classes.dex */
public interface RendererCapabilities {
    int getTrackType();

    int supportsFormat(Format format) throws ExoPlaybackException;

    int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException;

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC {
        @SuppressLint({"WrongConstant"})
        public static int create(int i, int i2, int i3) {
            return i | i2 | i3;
        }

        @SuppressLint({"WrongConstant"})
        public static int getFormatSupport(int i) {
            return i & 7;
        }

        @SuppressLint({"WrongConstant"})
        public static int getTunnelingSupport(int i) {
            return i & 32;
        }

        public static int create(int i) {
            return create(i, 0, 0);
        }

        public static String getFormatSupportString(int i) {
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i == 4) {
                                return "YES";
                            }
                            throw new IllegalStateException();
                        }
                        return "NO_EXCEEDS_CAPABILITIES";
                    }
                    return "NO_UNSUPPORTED_DRM";
                }
                return "NO_UNSUPPORTED_TYPE";
            }
            return "NO";
        }
    }
}
