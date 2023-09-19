package org.telegram.ui.Stories;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.AnimatedFloat;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes4.dex */
public class StoriesVolumeContorl extends View {
    float currentProgress;
    Runnable hideRunnuble;
    boolean isVisible;
    Paint paint;
    AnimatedFloat progressToVisible;
    AnimatedFloat volumeProgress;

    public StoriesVolumeContorl(Context context) {
        super(context);
        this.paint = new Paint(1);
        this.hideRunnuble = new Runnable() { // from class: org.telegram.ui.Stories.StoriesVolumeContorl.1
            @Override // java.lang.Runnable
            public void run() {
                StoriesVolumeContorl storiesVolumeContorl = StoriesVolumeContorl.this;
                storiesVolumeContorl.isVisible = false;
                storiesVolumeContorl.invalidate();
            }
        };
        this.progressToVisible = new AnimatedFloat(this);
        this.volumeProgress = new AnimatedFloat(this);
        this.paint.setColor(-1);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 24) {
            adjustVolume(true);
            return true;
        } else if (i == 25) {
            adjustVolume(false);
            return true;
        } else {
            return super.onKeyDown(i, keyEvent);
        }
    }

    public void unmute() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        int streamMaxVolume = audioManager.getStreamMaxVolume(3);
        int streamMinVolume = Build.VERSION.SDK_INT >= 28 ? audioManager.getStreamMinVolume(3) : 0;
        int streamVolume = audioManager.getStreamVolume(3);
        if (streamVolume <= streamMinVolume) {
            adjustVolume(true);
        } else if (this.isVisible) {
        } else {
            float f = streamVolume / streamMaxVolume;
            this.currentProgress = f;
            this.volumeProgress.set(f, true);
            this.isVisible = true;
            invalidate();
            AndroidUtilities.cancelRunOnUIThread(this.hideRunnuble);
            AndroidUtilities.runOnUIThread(this.hideRunnuble, 2000L);
        }
    }

    private void adjustVolume(boolean z) {
        int i;
        AudioManager audioManager = (AudioManager) getContext().getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        int streamMaxVolume = audioManager.getStreamMaxVolume(3);
        int streamVolume = audioManager.getStreamVolume(3);
        if (z) {
            i = streamVolume + 1;
            if (i > streamMaxVolume) {
                i = streamMaxVolume;
            }
        } else {
            i = streamVolume - 1;
            if (i < 0) {
                i = 0;
            }
        }
        audioManager.setStreamVolume(3, i, 0);
        float f = i / streamMaxVolume;
        this.currentProgress = f;
        if (!this.isVisible) {
            this.volumeProgress.set(f, true);
        }
        invalidate();
        this.isVisible = true;
        AndroidUtilities.cancelRunOnUIThread(this.hideRunnuble);
        AndroidUtilities.runOnUIThread(this.hideRunnuble, 2000L);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.volumeProgress.set(this.currentProgress);
        this.progressToVisible.set(this.isVisible ? 1.0f : 0.0f);
        if (this.progressToVisible.get() != 0.0f) {
            float measuredHeight = getMeasuredHeight() / 2.0f;
            this.paint.setAlpha((int) (this.progressToVisible.get() * 255.0f));
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, getMeasuredWidth() * this.volumeProgress.get(), getMeasuredHeight());
            canvas.drawRoundRect(rectF, measuredHeight, measuredHeight, this.paint);
        }
    }

    public void hide() {
        AndroidUtilities.cancelRunOnUIThread(this.hideRunnuble);
        this.hideRunnuble.run();
    }
}
