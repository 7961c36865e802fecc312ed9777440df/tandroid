package org.telegram.messenger.voip;

import android.media.AudioManager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Utilities;
import org.webrtc.MediaStreamTrack;

/* loaded from: classes3.dex */
public class VoipAudioManager {
    private Boolean isSpeakerphoneOn;

    private static final class InstanceHolder {
        static final VoipAudioManager instance = new VoipAudioManager();

        private InstanceHolder() {
        }
    }

    private VoipAudioManager() {
    }

    public static VoipAudioManager get() {
        return InstanceHolder.instance;
    }

    private AudioManager getAudioManager() {
        return (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$isBluetoothAndSpeakerOnAsync$1(Utilities.Callback2 callback2, boolean z, boolean z2) {
        callback2.run(Boolean.valueOf(z), Boolean.valueOf(z2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isBluetoothAndSpeakerOnAsync$2(final Utilities.Callback2 callback2) {
        AudioManager audioManager = getAudioManager();
        final boolean isBluetoothScoOn = audioManager.isBluetoothScoOn();
        final boolean isSpeakerphoneOn = audioManager.isSpeakerphoneOn();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoipAudioManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                VoipAudioManager.lambda$isBluetoothAndSpeakerOnAsync$1(Utilities.Callback2.this, isBluetoothScoOn, isSpeakerphoneOn);
            }
        });
    }

    public void isBluetoothAndSpeakerOnAsync(final Utilities.Callback2<Boolean, Boolean> callback2) {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoipAudioManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VoipAudioManager.this.lambda$isBluetoothAndSpeakerOnAsync$2(callback2);
            }
        });
    }

    public boolean isSpeakerphoneOn() {
        Boolean bool = this.isSpeakerphoneOn;
        return bool == null ? getAudioManager().isSpeakerphoneOn() : bool.booleanValue();
    }

    public void setSpeakerphoneOn(final boolean z) {
        this.isSpeakerphoneOn = Boolean.valueOf(z);
        final AudioManager audioManager = getAudioManager();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoipAudioManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                audioManager.setSpeakerphoneOn(z);
            }
        });
    }
}
