package com.google.android.exoplayer2;

import android.content.Context;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer;
import com.google.android.exoplayer2.ext.flac.LibflacAudioRenderer;
import com.google.android.exoplayer2.ext.opus.LibopusAudioRenderer;
import com.google.android.exoplayer2.mediacodec.DefaultMediaCodecAdapterFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.video.spherical.CameraMotionRenderer;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DefaultRenderersFactory implements RenderersFactory {
    private final Context context;
    private boolean enableAudioTrackPlaybackParams;
    private boolean enableDecoderFallback;
    private boolean enableFloatOutput;
    private boolean enableOffload;
    private final DefaultMediaCodecAdapterFactory codecAdapterFactory = new DefaultMediaCodecAdapterFactory();
    private int extensionRendererMode = 0;
    private long allowedVideoJoiningTimeMs = 5000;
    private MediaCodecSelector mediaCodecSelector = MediaCodecSelector.DEFAULT;

    public DefaultRenderersFactory(Context context) {
        this.context = context;
    }

    protected void buildAudioRenderers(Context context, int i, MediaCodecSelector mediaCodecSelector, boolean z, AudioSink audioSink, Handler handler, AudioRendererEventListener audioRendererEventListener, ArrayList arrayList) {
        int i2;
        int i3;
        arrayList.add(new MediaCodecAudioRenderer(context, getCodecAdapterFactory(), mediaCodecSelector, z, handler, audioRendererEventListener, audioSink));
        if (i == 0) {
            return;
        }
        int size = arrayList.size();
        if (i == 2) {
            size--;
        }
        try {
            try {
                i2 = size + 1;
                try {
                    arrayList.add(size, (Renderer) Class.forName("com.google.android.exoplayer2.decoder.midi.MidiRenderer").getConstructor(null).newInstance(null));
                    Log.i("DefaultRenderersFactory", "Loaded MidiRenderer.");
                } catch (ClassNotFoundException unused) {
                    size = i2;
                    i2 = size;
                    try {
                        i3 = i2 + 1;
                        try {
                            arrayList.add(i2, (Renderer) LibopusAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                            Log.i("DefaultRenderersFactory", "Loaded LibopusAudioRenderer.");
                        } catch (ClassNotFoundException unused2) {
                            i2 = i3;
                            i3 = i2;
                            try {
                                int i4 = i3 + 1;
                                arrayList.add(i3, (Renderer) LibflacAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                                Log.i("DefaultRenderersFactory", "Loaded LibflacAudioRenderer.");
                            } catch (ClassNotFoundException unused3) {
                            }
                            arrayList.add(i4, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                            Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
                        }
                    } catch (ClassNotFoundException unused4) {
                    }
                    try {
                        int i42 = i3 + 1;
                        try {
                            arrayList.add(i3, (Renderer) LibflacAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                            Log.i("DefaultRenderersFactory", "Loaded LibflacAudioRenderer.");
                        } catch (ClassNotFoundException unused5) {
                            i3 = i42;
                            i42 = i3;
                            arrayList.add(i42, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                            Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
                        }
                        arrayList.add(i42, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                        Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
                    } catch (Exception e) {
                        throw new RuntimeException("Error instantiating FLAC extension", e);
                    }
                }
            } catch (Exception e2) {
                throw new RuntimeException("Error instantiating MIDI extension", e2);
            }
        } catch (ClassNotFoundException unused6) {
        }
        try {
            i3 = i2 + 1;
            arrayList.add(i2, (Renderer) LibopusAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
            Log.i("DefaultRenderersFactory", "Loaded LibopusAudioRenderer.");
            int i422 = i3 + 1;
            arrayList.add(i3, (Renderer) LibflacAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
            Log.i("DefaultRenderersFactory", "Loaded LibflacAudioRenderer.");
            try {
                arrayList.add(i422, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
            } catch (ClassNotFoundException unused7) {
            } catch (Exception e3) {
                throw new RuntimeException("Error instantiating FFmpeg extension", e3);
            }
        } catch (Exception e4) {
            throw new RuntimeException("Error instantiating Opus extension", e4);
        }
    }

    protected AudioSink buildAudioSink(Context context, boolean z, boolean z2, boolean z3) {
        return new DefaultAudioSink.Builder().setAudioCapabilities(AudioCapabilities.getCapabilities(context)).setEnableFloatOutput(z).setEnableAudioTrackPlaybackParams(z2).setOffloadMode(z3 ? 1 : 0).build();
    }

    protected void buildCameraMotionRenderers(Context context, int i, ArrayList arrayList) {
        arrayList.add(new CameraMotionRenderer());
    }

    protected void buildMetadataRenderers(Context context, MetadataOutput metadataOutput, Looper looper, int i, ArrayList arrayList) {
        arrayList.add(new MetadataRenderer(metadataOutput, looper));
    }

    protected void buildMiscellaneousRenderers(Context context, Handler handler, int i, ArrayList arrayList) {
    }

    protected void buildTextRenderers(Context context, TextOutput textOutput, Looper looper, int i, ArrayList arrayList) {
        arrayList.add(new TextRenderer(textOutput, looper));
    }

    protected void buildVideoRenderers(Context context, EGLContext eGLContext, int i, MediaCodecSelector mediaCodecSelector, boolean z, Handler handler, VideoRendererEventListener videoRendererEventListener, long j, ArrayList arrayList) {
        String str;
        int i2;
        arrayList.add(new MediaCodecVideoRenderer(context, eGLContext, getCodecAdapterFactory(), mediaCodecSelector, j, z, handler, videoRendererEventListener, 50));
        if (i == 0) {
            return;
        }
        int size = arrayList.size();
        if (i == 2) {
            size--;
        }
        try {
            try {
                i2 = size + 1;
                try {
                    arrayList.add(size, (Renderer) Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer").getConstructor(Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(Long.valueOf(j), handler, videoRendererEventListener, 50));
                    str = "DefaultRenderersFactory";
                    try {
                        Log.i(str, "Loaded LibvpxVideoRenderer.");
                    } catch (ClassNotFoundException unused) {
                        size = i2;
                        i2 = size;
                        arrayList.add(i2, (Renderer) Class.forName("com.google.android.exoplayer2.ext.av1.Libgav1VideoRenderer").getConstructor(Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(Long.valueOf(j), handler, videoRendererEventListener, 50));
                        Log.i(str, "Loaded Libgav1VideoRenderer.");
                    }
                } catch (ClassNotFoundException unused2) {
                    str = "DefaultRenderersFactory";
                }
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating VP9 extension", e);
            }
        } catch (ClassNotFoundException unused3) {
            str = "DefaultRenderersFactory";
        }
        try {
            arrayList.add(i2, (Renderer) Class.forName("com.google.android.exoplayer2.ext.av1.Libgav1VideoRenderer").getConstructor(Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(Long.valueOf(j), handler, videoRendererEventListener, 50));
            Log.i(str, "Loaded Libgav1VideoRenderer.");
        } catch (ClassNotFoundException unused4) {
        } catch (Exception e2) {
            throw new RuntimeException("Error instantiating AV1 extension", e2);
        }
    }

    @Override // com.google.android.exoplayer2.RenderersFactory
    public Renderer[] createRenderers(Handler handler, EGLContext eGLContext, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textOutput, MetadataOutput metadataOutput) {
        ArrayList arrayList = new ArrayList();
        buildVideoRenderers(this.context, eGLContext, this.extensionRendererMode, this.mediaCodecSelector, this.enableDecoderFallback, handler, videoRendererEventListener, this.allowedVideoJoiningTimeMs, arrayList);
        AudioSink buildAudioSink = buildAudioSink(this.context, this.enableFloatOutput, this.enableAudioTrackPlaybackParams, this.enableOffload);
        if (buildAudioSink != null) {
            buildAudioRenderers(this.context, this.extensionRendererMode, this.mediaCodecSelector, this.enableDecoderFallback, buildAudioSink, handler, audioRendererEventListener, arrayList);
        }
        buildTextRenderers(this.context, textOutput, handler.getLooper(), this.extensionRendererMode, arrayList);
        buildMetadataRenderers(this.context, metadataOutput, handler.getLooper(), this.extensionRendererMode, arrayList);
        buildCameraMotionRenderers(this.context, this.extensionRendererMode, arrayList);
        buildMiscellaneousRenderers(this.context, handler, this.extensionRendererMode, arrayList);
        return (Renderer[]) arrayList.toArray(new Renderer[0]);
    }

    protected MediaCodecAdapter.Factory getCodecAdapterFactory() {
        return this.codecAdapterFactory;
    }

    public DefaultRenderersFactory setExtensionRendererMode(int i) {
        this.extensionRendererMode = i;
        return this;
    }
}
