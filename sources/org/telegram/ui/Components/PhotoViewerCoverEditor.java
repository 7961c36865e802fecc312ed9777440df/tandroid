package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Components.PhotoViewerCoverEditor;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.GallerySheet;
import org.telegram.ui.Stories.recorder.TimelineView;

/* loaded from: classes3.dex */
public class PhotoViewerCoverEditor extends FrameLayout {
    public ActionBar actionBar;
    private float aspectRatio;
    public ButtonWithCounterView button;
    public Runnable close;
    private GallerySheet gallerySheet;
    private Utilities.Callback onGalleryListener;
    public EditCoverButton openGalleryButton;
    private long time;
    public TimelineView timelineView;
    private VideoPlayer videoPlayer;

    class 2 implements TimelineView.TimelineDelegate {
        private Runnable betterSeek = new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerCoverEditor$2$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PhotoViewerCoverEditor.2.this.lambda$$0();
            }
        };

        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$0() {
            PhotoViewerCoverEditor.this.videoPlayer.seekTo(PhotoViewerCoverEditor.this.time, false);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onAudioLeftChange(float f) {
            TimelineView.TimelineDelegate.-CC.$default$onAudioLeftChange(this, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onAudioOffsetChange(long j) {
            TimelineView.TimelineDelegate.-CC.$default$onAudioOffsetChange(this, j);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onAudioRemove() {
            TimelineView.TimelineDelegate.-CC.$default$onAudioRemove(this);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onAudioRightChange(float f) {
            TimelineView.TimelineDelegate.-CC.$default$onAudioRightChange(this, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onAudioVolumeChange(float f) {
            TimelineView.TimelineDelegate.-CC.$default$onAudioVolumeChange(this, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onProgressChange(long j, boolean z) {
            TimelineView.TimelineDelegate.-CC.$default$onProgressChange(this, j, z);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onProgressDragChange(boolean z) {
            TimelineView.TimelineDelegate.-CC.$default$onProgressDragChange(this, z);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onRoundLeftChange(float f) {
            TimelineView.TimelineDelegate.-CC.$default$onRoundLeftChange(this, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onRoundOffsetChange(long j) {
            TimelineView.TimelineDelegate.-CC.$default$onRoundOffsetChange(this, j);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onRoundRemove() {
            TimelineView.TimelineDelegate.-CC.$default$onRoundRemove(this);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onRoundRightChange(float f) {
            TimelineView.TimelineDelegate.-CC.$default$onRoundRightChange(this, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onRoundSelectChange(boolean z) {
            TimelineView.TimelineDelegate.-CC.$default$onRoundSelectChange(this, z);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onRoundVolumeChange(float f) {
            TimelineView.TimelineDelegate.-CC.$default$onRoundVolumeChange(this, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onVideoLeftChange(int i, float f) {
            TimelineView.TimelineDelegate.-CC.$default$onVideoLeftChange(this, i, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public void onVideoLeftChange(boolean z, float f) {
            if (PhotoViewerCoverEditor.this.videoPlayer == null) {
                return;
            }
            float max = 2.8f / Math.max(60L, r0);
            PhotoViewerCoverEditor.this.time = (long) ((f + (max * (f / (1.0f - max)))) * PhotoViewerCoverEditor.this.videoPlayer.getDuration());
            PhotoViewerCoverEditor.this.videoPlayer.seekTo(PhotoViewerCoverEditor.this.time, !z);
            if (z) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.betterSeek);
            AndroidUtilities.runOnUIThread(this.betterSeek, 120L);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onVideoOffsetChange(int i, long j) {
            TimelineView.TimelineDelegate.-CC.$default$onVideoOffsetChange(this, i, j);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onVideoRightChange(int i, float f) {
            TimelineView.TimelineDelegate.-CC.$default$onVideoRightChange(this, i, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onVideoRightChange(boolean z, float f) {
            TimelineView.TimelineDelegate.-CC.$default$onVideoRightChange(this, z, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onVideoSelected(int i) {
            TimelineView.TimelineDelegate.-CC.$default$onVideoSelected(this, i);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onVideoVolumeChange(float f) {
            TimelineView.TimelineDelegate.-CC.$default$onVideoVolumeChange(this, f);
        }

        @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
        public /* synthetic */ void onVideoVolumeChange(int i, float f) {
            TimelineView.TimelineDelegate.-CC.$default$onVideoVolumeChange(this, i, f);
        }
    }

    public PhotoViewerCoverEditor(final Context context, final Theme.ResourcesProvider resourcesProvider, PhotoViewer photoViewer, BlurringShader.BlurManager blurManager) {
        super(context);
        this.time = -1L;
        this.aspectRatio = 1.39f;
        ActionBar actionBar = new ActionBar(context, resourcesProvider);
        this.actionBar = actionBar;
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString(R.string.EditorSetCoverTitle));
        this.actionBar.setItemsColor(-1, false);
        this.actionBar.setItemsBackgroundColor(587202559, false);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.Components.PhotoViewerCoverEditor.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                Runnable runnable;
                if (i != -1 || (runnable = PhotoViewerCoverEditor.this.close) == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(runnable);
            }
        });
        addView(this.actionBar, LayoutHelper.createFrame(-1, -2, 55));
        TimelineView timelineView = new TimelineView(context, null, null, resourcesProvider, blurManager);
        this.timelineView = timelineView;
        timelineView.setCover();
        addView(this.timelineView, LayoutHelper.createFrame(-1, TimelineView.heightDp(), 87, 0.0f, 0.0f, 0.0f, 74.0f));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        this.button = buttonWithCounterView;
        buttonWithCounterView.setText(LocaleController.getString(R.string.EditorSetCoverSave), false);
        addView(this.button, LayoutHelper.createFrame(-1, 48.0f, 87, 10.0f, 10.0f, 10.0f, 10.0f));
        EditCoverButton editCoverButton = new EditCoverButton(context, photoViewer, LocaleController.getString(R.string.EditorSetCoverGallery), true);
        this.openGalleryButton = editCoverButton;
        editCoverButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoViewerCoverEditor$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoViewerCoverEditor.this.lambda$new$1(context, resourcesProvider, view);
            }
        });
        addView(this.openGalleryButton, LayoutHelper.createFrame(-1, 32.0f, 87, 60.0f, 0.0f, 60.0f, 134.0f));
        this.timelineView.setDelegate(new 2());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.gallerySheet = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Context context, Theme.ResourcesProvider resourcesProvider, View view) {
        if (this.gallerySheet == null) {
            GallerySheet gallerySheet = new GallerySheet(context, resourcesProvider, this.aspectRatio);
            this.gallerySheet = gallerySheet;
            gallerySheet.setOnDismissListener(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerCoverEditor$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerCoverEditor.this.lambda$new$0();
                }
            });
            this.gallerySheet.setOnGalleryImage(this.onGalleryListener);
        }
        this.gallerySheet.show();
    }

    public void closeGallery() {
        GallerySheet gallerySheet = this.gallerySheet;
        if (gallerySheet != null) {
            gallerySheet.lambda$new$0();
            this.gallerySheet = null;
        }
    }

    public void destroy() {
        this.videoPlayer = null;
        this.timelineView.setVideo(false, null, 0L, 0.0f);
    }

    public long getTime() {
        return this.time;
    }

    public void set(MediaController.PhotoEntry photoEntry, VideoPlayer videoPlayer, Theme.ResourcesProvider resourcesProvider) {
        int i;
        this.button.updateColors(resourcesProvider);
        int i2 = photoEntry.width;
        if (i2 <= 0 || (i = photoEntry.height) <= 0) {
            this.aspectRatio = 1.39f;
        } else {
            this.aspectRatio = Utilities.clamp(i / i2, 1.39f, 0.85f);
        }
        this.videoPlayer = videoPlayer;
        long j = photoEntry.coverSavedPosition;
        if (j >= 0) {
            this.time = j;
            videoPlayer.seekTo(j, false);
        } else {
            this.time = videoPlayer.getCurrentPosition();
        }
        this.timelineView.setVideo(false, videoPlayer.getCurrentUri().getPath(), videoPlayer.getDuration(), videoPlayer.player.getVolume());
        long duration = videoPlayer.getDuration();
        float max = 2.8f / Math.max(60L, duration);
        float max2 = (this.time / Math.max(1L, videoPlayer.getDuration())) * (1.0f - max);
        this.timelineView.setVideoLeft(max2);
        this.timelineView.setVideoRight(max2 + max);
        this.timelineView.setCoverVideo(0L, duration);
        this.timelineView.normalizeScrollByVideo();
    }

    public void setOnClose(Runnable runnable) {
        this.close = runnable;
    }

    public void setOnGalleryImage(Utilities.Callback<MediaController.PhotoEntry> callback) {
        this.onGalleryListener = callback;
    }
}