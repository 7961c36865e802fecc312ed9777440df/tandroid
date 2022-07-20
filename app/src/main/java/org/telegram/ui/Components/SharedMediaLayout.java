package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.Property;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$InputUser;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_chatChannelParticipant;
import org.telegram.tgnet.TLRPC$TL_chatParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_chatParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterDocument;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterMusic;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPhotoVideo;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPhotos;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterRoundVoice;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterUrl;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterVideo;
import org.telegram.tgnet.TLRPC$TL_inputUserEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_getCommonChats;
import org.telegram.tgnet.TLRPC$TL_messages_getSearchResultsPositions;
import org.telegram.tgnet.TLRPC$TL_messages_search;
import org.telegram.tgnet.TLRPC$TL_messages_searchResultsPositions;
import org.telegram.tgnet.TLRPC$TL_searchResultPosition;
import org.telegram.tgnet.TLRPC$TL_webPageEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.TLRPC$messages_Chats;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.ArticleViewer;
import org.telegram.ui.CalendarActivity;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.SharedAudioCell;
import org.telegram.ui.Cells.SharedDocumentCell;
import org.telegram.ui.Cells.SharedLinkCell;
import org.telegram.ui.Cells.SharedMediaSectionCell;
import org.telegram.ui.Cells.SharedPhotoVideoCell;
import org.telegram.ui.Cells.SharedPhotoVideoCell2;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public class SharedMediaLayout extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private ActionBar actionBar;
    private AnimatorSet actionModeAnimation;
    private LinearLayout actionModeLayout;
    private float additionalFloatingTranslation;
    private int animateToColumnsCount;
    private boolean animatingForward;
    int animationIndex;
    private SharedPhotoVideoAdapter animationSupportingPhotoVideoAdapter;
    private SharedDocumentsAdapter audioAdapter;
    private MediaSearchAdapter audioSearchAdapter;
    private boolean backAnimation;
    private BackDrawable backDrawable;
    private int cantDeleteMessagesCount;
    private boolean changeTypeAnimation;
    private ChatUsersAdapter chatUsersAdapter;
    private ImageView closeButton;
    private CommonGroupsAdapter commonGroupsAdapter;
    final Delegate delegate;
    private ActionBarMenuItem deleteItem;
    private long dialog_id;
    private SharedDocumentsAdapter documentsAdapter;
    private MediaSearchAdapter documentsSearchAdapter;
    private AnimatorSet floatingDateAnimation;
    private ChatActionCell floatingDateView;
    private ActionBarMenuItem forwardItem;
    private FragmentContextView fragmentContextView;
    private HintView fwdRestrictedHint;
    private GifAdapter gifAdapter;
    FlickerLoadingView globalGradientView;
    private ActionBarMenuItem gotoItem;
    private GroupUsersSearchAdapter groupUsersSearchAdapter;
    private int[] hasMedia;
    private boolean ignoreSearchCollapse;
    private TLRPC$ChatFull info;
    private int initialTab;
    private boolean isActionModeShowed;
    boolean isInPinchToZoomTouchMode;
    boolean isPinnedToTop;
    Runnable jumpToRunnable;
    int lastMeasuredTopPadding;
    private SharedLinksAdapter linksAdapter;
    private MediaSearchAdapter linksSearchAdapter;
    private int maximumVelocity;
    boolean maybePinchToZoomTouchMode;
    boolean maybePinchToZoomTouchMode2;
    private boolean maybeStartTracking;
    private int mediaColumnsCount;
    private long mergeDialogId;
    ActionBarPopupWindow optionsWindow;
    private SharedPhotoVideoAdapter photoVideoAdapter;
    private boolean photoVideoChangeColumnsAnimation;
    private float photoVideoChangeColumnsProgress;
    public ImageView photoVideoOptionsItem;
    int pinchCenterOffset;
    int pinchCenterPosition;
    int pinchCenterX;
    int pinchCenterY;
    float pinchScale;
    boolean pinchScaleUp;
    float pinchStartDistance;
    private Drawable pinnedHeaderShadowDrawable;
    private int pointerId1;
    private int pointerId2;
    private BaseFragment profileActivity;
    private Theme.ResourcesProvider resourcesProvider;
    private ScrollSlidingTextTabStripInner scrollSlidingTextTabStrip;
    private boolean scrolling;
    public boolean scrollingByUser;
    private ActionBarMenuItem searchItem;
    private int searchItemState;
    private boolean searchWas;
    private boolean searching;
    private NumberTextView selectedMessagesCountTextView;
    private View shadowLine;
    private SharedMediaPreloader sharedMediaPreloader;
    private boolean startedTracking;
    private int startedTrackingPointerId;
    private int startedTrackingX;
    private int startedTrackingY;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    int topPadding;
    private VelocityTracker velocityTracker;
    private final int viewType;
    private SharedDocumentsAdapter voiceAdapter;
    private static final int[] supportedFastScrollTypes = {0, 1, 2, 4};
    private static final Interpolator interpolator = SharedMediaLayout$$ExternalSyntheticLambda5.INSTANCE;
    android.graphics.Rect rect = new android.graphics.Rect();
    private MediaPage[] mediaPages = new MediaPage[2];
    private ArrayList<SharedPhotoVideoCell> cellCache = new ArrayList<>(10);
    private ArrayList<SharedPhotoVideoCell> cache = new ArrayList<>(10);
    private ArrayList<SharedAudioCell> audioCellCache = new ArrayList<>(10);
    private ArrayList<SharedAudioCell> audioCache = new ArrayList<>(10);
    private Runnable hideFloatingDateRunnable = new SharedMediaLayout$$ExternalSyntheticLambda8(this);
    private ArrayList<View> actionModeViews = new ArrayList<>();
    private Paint backgroundPaint = new Paint();
    private SparseArray<MessageObject>[] selectedFiles = {new SparseArray<>(), new SparseArray<>()};
    private ArrayList<SharedPhotoVideoCell2> animationSupportingSortedCells = new ArrayList<>();
    private PhotoViewer.PhotoViewerProvider provider = new AnonymousClass1();
    private SharedMediaData[] sharedMediaData = new SharedMediaData[6];
    SparseArray<Float> messageAlphaEnter = new SparseArray<>();
    SharedLinkCell.SharedLinkCellDelegate sharedLinkCellDelegate = new AnonymousClass30();

    /* loaded from: classes3.dex */
    public interface Delegate {
        boolean canSearchMembers();

        TLRPC$Chat getCurrentChat();

        RecyclerListView getListView();

        boolean isFragmentOpened();

        boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, boolean z2);

        void scrollToSharedMedia();

        void updateSelectedMediaTabText();
    }

    /* loaded from: classes3.dex */
    public interface SharedMediaPreloaderDelegate {
        void mediaCountUpdated();
    }

    public static /* synthetic */ boolean lambda$new$8(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ float lambda$static$1(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    public void showFloatingDateView() {
    }

    protected boolean canShowSearchItem() {
        return true;
    }

    public int getNextMediaColumnsCount(int i, boolean z) {
        if (!z) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i != 5) {
                            if (i != 6) {
                                return i;
                            }
                            return 9;
                        }
                        return 6;
                    }
                    return 5;
                }
                return 4;
            }
            return 3;
        }
        if (i != 9) {
            if (i != 6) {
                if (i != 5) {
                    if (i != 4) {
                        if (i != 3) {
                            return i;
                        }
                        return 2;
                    }
                    return 3;
                }
                return 4;
            }
            return 5;
        }
        return 6;
    }

    protected void invalidateBlur() {
    }

    protected boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z) {
        return false;
    }

    protected void onSearchStateChanged(boolean z) {
    }

    protected void onSelectedTabChanged() {
    }

    public boolean isInFastScroll() {
        MediaPage[] mediaPageArr = this.mediaPages;
        return (mediaPageArr[0] == null || mediaPageArr[0].listView.getFastScroll() == null || !this.mediaPages[0].listView.getFastScroll().isPressed()) ? false : true;
    }

    public boolean dispatchFastScrollEvent(MotionEvent motionEvent) {
        View view = (View) getParent();
        motionEvent.offsetLocation(((-view.getX()) - getX()) - this.mediaPages[0].listView.getFastScroll().getX(), (((-view.getY()) - getY()) - this.mediaPages[0].getY()) - this.mediaPages[0].listView.getFastScroll().getY());
        return this.mediaPages[0].listView.getFastScroll().dispatchTouchEvent(motionEvent);
    }

    public boolean checkPinchToZoom(MotionEvent motionEvent) {
        if (this.mediaPages[0].selectedType != 0 || getParent() == null) {
            return false;
        }
        if (this.photoVideoChangeColumnsAnimation && !this.isInPinchToZoomTouchMode) {
            return true;
        }
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
            if (this.maybePinchToZoomTouchMode && !this.isInPinchToZoomTouchMode && motionEvent.getPointerCount() == 2) {
                this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                this.pinchScale = 1.0f;
                this.pointerId1 = motionEvent.getPointerId(0);
                this.pointerId2 = motionEvent.getPointerId(1);
                this.mediaPages[0].listView.cancelClickRunnables(false);
                this.mediaPages[0].listView.cancelLongPress();
                this.mediaPages[0].listView.dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0));
                View view = (View) getParent();
                this.pinchCenterX = (int) (((((int) ((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f)) - view.getX()) - getX()) - this.mediaPages[0].getX());
                int y = (int) (((((int) ((motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f)) - view.getY()) - getY()) - this.mediaPages[0].getY());
                this.pinchCenterY = y;
                selectPinchPosition(this.pinchCenterX, y);
                this.maybePinchToZoomTouchMode2 = true;
            }
            if (motionEvent.getActionMasked() == 0 && ((motionEvent.getY() - ((View) getParent()).getY()) - getY()) - this.mediaPages[0].getY() > 0.0f) {
                this.maybePinchToZoomTouchMode = true;
            }
        } else if (motionEvent.getActionMasked() == 2 && (this.isInPinchToZoomTouchMode || this.maybePinchToZoomTouchMode2)) {
            int i = -1;
            int i2 = -1;
            for (int i3 = 0; i3 < motionEvent.getPointerCount(); i3++) {
                if (this.pointerId1 == motionEvent.getPointerId(i3)) {
                    i = i3;
                }
                if (this.pointerId2 == motionEvent.getPointerId(i3)) {
                    i2 = i3;
                }
            }
            if (i == -1 || i2 == -1) {
                this.maybePinchToZoomTouchMode = false;
                this.maybePinchToZoomTouchMode2 = false;
                this.isInPinchToZoomTouchMode = false;
                finishPinchToMediaColumnsCount();
                return false;
            }
            float hypot = ((float) Math.hypot(motionEvent.getX(i2) - motionEvent.getX(i), motionEvent.getY(i2) - motionEvent.getY(i))) / this.pinchStartDistance;
            this.pinchScale = hypot;
            if (!this.isInPinchToZoomTouchMode && (hypot > 1.01f || hypot < 0.99f)) {
                this.isInPinchToZoomTouchMode = true;
                boolean z = hypot > 1.0f;
                this.pinchScaleUp = z;
                startPinchToMediaColumnsCount(z);
            }
            if (this.isInPinchToZoomTouchMode) {
                boolean z2 = this.pinchScaleUp;
                if ((!z2 || this.pinchScale >= 1.0f) && (z2 || this.pinchScale <= 1.0f)) {
                    this.photoVideoChangeColumnsProgress = Math.max(0.0f, Math.min(1.0f, z2 ? 1.0f - ((2.0f - this.pinchScale) / 1.0f) : (1.0f - this.pinchScale) / 0.5f));
                } else {
                    this.photoVideoChangeColumnsProgress = 0.0f;
                }
                float f = this.photoVideoChangeColumnsProgress;
                if (f == 1.0f || f == 0.0f) {
                    if (f == 1.0f) {
                        int ceil = (int) Math.ceil(this.pinchCenterPosition / this.animateToColumnsCount);
                        float measuredWidth = this.startedTrackingX / (this.mediaPages[0].listView.getMeasuredWidth() - ((int) (this.mediaPages[0].listView.getMeasuredWidth() / this.animateToColumnsCount)));
                        int i4 = this.animateToColumnsCount;
                        int i5 = (ceil * i4) + ((int) (measuredWidth * (i4 - 1)));
                        if (i5 >= this.photoVideoAdapter.getItemCount()) {
                            i5 = this.photoVideoAdapter.getItemCount() - 1;
                        }
                        this.pinchCenterPosition = i5;
                    }
                    finishPinchToMediaColumnsCount();
                    if (this.photoVideoChangeColumnsProgress == 0.0f) {
                        this.pinchScaleUp = !this.pinchScaleUp;
                    }
                    startPinchToMediaColumnsCount(this.pinchScaleUp);
                    this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                }
                this.mediaPages[0].listView.invalidate();
                MediaPage[] mediaPageArr = this.mediaPages;
                if (mediaPageArr[0].fastScrollHintView != null) {
                    mediaPageArr[0].invalidate();
                }
            }
        } else if ((motionEvent.getActionMasked() == 1 || ((motionEvent.getActionMasked() == 6 && checkPointerIds(motionEvent)) || motionEvent.getActionMasked() == 3)) && this.isInPinchToZoomTouchMode) {
            this.maybePinchToZoomTouchMode2 = false;
            this.maybePinchToZoomTouchMode = false;
            this.isInPinchToZoomTouchMode = false;
            finishPinchToMediaColumnsCount();
        }
        return this.isInPinchToZoomTouchMode;
    }

    private void selectPinchPosition(int i, int i2) {
        this.pinchCenterPosition = -1;
        int i3 = i2 + this.mediaPages[0].listView.blurTopPadding;
        if (getY() != 0.0f && this.viewType == 1) {
            i3 = 0;
        }
        for (int i4 = 0; i4 < this.mediaPages[0].listView.getChildCount(); i4++) {
            View childAt = this.mediaPages[0].listView.getChildAt(i4);
            childAt.getHitRect(this.rect);
            if (this.rect.contains(i, i3)) {
                this.pinchCenterPosition = this.mediaPages[0].listView.getChildLayoutPosition(childAt);
                this.pinchCenterOffset = childAt.getTop();
            }
        }
        if (!this.delegate.canSearchMembers() || this.pinchCenterPosition != -1) {
            return;
        }
        this.pinchCenterPosition = (int) (this.mediaPages[0].layoutManager.findFirstVisibleItemPosition() + ((this.mediaColumnsCount - 1) * Math.min(1.0f, Math.max(i / this.mediaPages[0].listView.getMeasuredWidth(), 0.0f))));
        this.pinchCenterOffset = 0;
    }

    private boolean checkPointerIds(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() < 2) {
            return false;
        }
        if (this.pointerId1 == motionEvent.getPointerId(0) && this.pointerId2 == motionEvent.getPointerId(1)) {
            return true;
        }
        return this.pointerId1 == motionEvent.getPointerId(1) && this.pointerId2 == motionEvent.getPointerId(0);
    }

    public boolean isSwipeBackEnabled() {
        return !this.photoVideoChangeColumnsAnimation && !this.tabsAnimationInProgress;
    }

    public int getPhotosVideosTypeFilter() {
        return this.sharedMediaData[0].filterType;
    }

    public boolean isPinnedToTop() {
        return this.isPinnedToTop;
    }

    public void setPinnedToTop(boolean z) {
        if (this.isPinnedToTop != z) {
            this.isPinnedToTop = z;
            int i = 0;
            while (true) {
                MediaPage[] mediaPageArr = this.mediaPages;
                if (i >= mediaPageArr.length) {
                    return;
                }
                updateFastScrollVisibility(mediaPageArr[i], true);
                i++;
            }
        }
    }

    public void drawListForBlur(Canvas canvas) {
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i < mediaPageArr.length) {
                if (mediaPageArr[i] != null && mediaPageArr[i].getVisibility() == 0) {
                    for (int i2 = 0; i2 < this.mediaPages[i].listView.getChildCount(); i2++) {
                        View childAt = this.mediaPages[i].listView.getChildAt(i2);
                        if (childAt.getY() < this.mediaPages[i].listView.blurTopPadding + AndroidUtilities.dp(100.0f)) {
                            int save = canvas.save();
                            canvas.translate(this.mediaPages[i].getX() + childAt.getX(), getY() + this.mediaPages[i].getY() + this.mediaPages[i].listView.getY() + childAt.getY());
                            childAt.draw(canvas);
                            canvas.restoreToCount(save);
                        }
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class MediaPage extends FrameLayout {
        private ClippingImageView animatingImageView;
        private GridLayoutManager animationSupportingLayoutManager;
        private BlurredRecyclerView animationSupportingListView;
        private StickerEmptyView emptyView;
        public ObjectAnimator fastScrollAnimator;
        public boolean fastScrollEnabled;
        public Runnable fastScrollHideHintRunnable;
        public boolean fastScrollHinWasShown;
        public SharedMediaFastScrollTooltip fastScrollHintView;
        public boolean highlightAnimation;
        public int highlightMessageId;
        public float highlightProgress;
        public long lastCheckScrollTime;
        private ExtendedGridLayoutManager layoutManager;
        private BlurredRecyclerView listView;
        private FlickerLoadingView progressView;
        private RecyclerAnimationScrollHelper scrollHelper;
        private int selectedType;

        public MediaPage(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == this.animationSupportingListView) {
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip = this.fastScrollHintView;
            if (sharedMediaFastScrollTooltip == null || sharedMediaFastScrollTooltip.getVisibility() != 0) {
                return;
            }
            RecyclerListView.FastScroll fastScroll = this.listView.getFastScroll();
            if (fastScroll != null) {
                SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip2 = this.fastScrollHintView;
                sharedMediaFastScrollTooltip2.setPivotX(sharedMediaFastScrollTooltip2.getMeasuredWidth());
                this.fastScrollHintView.setPivotY(0.0f);
                this.fastScrollHintView.setTranslationX((getMeasuredWidth() - this.fastScrollHintView.getMeasuredWidth()) - AndroidUtilities.dp(16.0f));
                this.fastScrollHintView.setTranslationY(fastScroll.getScrollBarY() + AndroidUtilities.dp(36.0f));
            }
            if (fastScroll.getProgress() <= 0.85f) {
                return;
            }
            SharedMediaLayout.showFastScrollHint(this, null, false);
        }
    }

    public void updateFastScrollVisibility(MediaPage mediaPage, boolean z) {
        Integer num = 1;
        int i = 0;
        boolean z2 = mediaPage.fastScrollEnabled && this.isPinnedToTop;
        RecyclerListView.FastScroll fastScroll = mediaPage.listView.getFastScroll();
        ObjectAnimator objectAnimator = mediaPage.fastScrollAnimator;
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            mediaPage.fastScrollAnimator.cancel();
        }
        if (!z) {
            fastScroll.animate().setListener(null).cancel();
            if (!z2) {
                i = 8;
            }
            fastScroll.setVisibility(i);
            if (!z2) {
                num = null;
            }
            fastScroll.setTag(num);
            fastScroll.setAlpha(1.0f);
            fastScroll.setScaleX(1.0f);
            fastScroll.setScaleY(1.0f);
        } else if (z2 && fastScroll.getTag() == null) {
            fastScroll.animate().setListener(null).cancel();
            if (fastScroll.getVisibility() != 0) {
                fastScroll.setVisibility(0);
                fastScroll.setAlpha(0.0f);
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(fastScroll, View.ALPHA, fastScroll.getAlpha(), 1.0f);
            mediaPage.fastScrollAnimator = ofFloat;
            ofFloat.setDuration(150L).start();
            fastScroll.setTag(num);
        } else if (z2 || fastScroll.getTag() == null) {
        } else {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(fastScroll, View.ALPHA, fastScroll.getAlpha(), 0.0f);
            ofFloat2.addListener(new HideViewAfterAnimation(fastScroll));
            mediaPage.fastScrollAnimator = ofFloat2;
            ofFloat2.setDuration(150L).start();
            fastScroll.animate().setListener(null).cancel();
            fastScroll.setTag(null);
        }
    }

    public /* synthetic */ void lambda$new$0() {
        hideFloatingDateView(true);
    }

    /* loaded from: classes3.dex */
    public static class SharedMediaPreloader implements NotificationCenter.NotificationCenterDelegate {
        private long dialogId;
        private boolean mediaWasLoaded;
        private long mergeDialogId;
        private BaseFragment parentFragment;
        private SharedMediaData[] sharedMediaData;
        private int[] mediaCount = {-1, -1, -1, -1, -1, -1, -1, -1};
        private int[] mediaMergeCount = {-1, -1, -1, -1, -1, -1, -1, -1};
        private int[] lastMediaCount = {-1, -1, -1, -1, -1, -1, -1, -1};
        private int[] lastLoadMediaCount = {-1, -1, -1, -1, -1, -1, -1, -1};
        private ArrayList<SharedMediaPreloaderDelegate> delegates = new ArrayList<>();

        public SharedMediaPreloader(BaseFragment baseFragment) {
            this.parentFragment = baseFragment;
            if (baseFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) baseFragment;
                this.dialogId = chatActivity.getDialogId();
                this.mergeDialogId = chatActivity.getMergeDialogId();
            } else if (baseFragment instanceof ProfileActivity) {
                this.dialogId = ((ProfileActivity) baseFragment).getDialogId();
            } else if (baseFragment instanceof MediaActivity) {
                this.dialogId = ((MediaActivity) baseFragment).getDialogId();
            }
            this.sharedMediaData = new SharedMediaData[6];
            int i = 0;
            while (true) {
                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                if (i < sharedMediaDataArr.length) {
                    sharedMediaDataArr[i] = new SharedMediaData();
                    this.sharedMediaData[i].setMaxId(0, DialogObject.isEncryptedDialog(this.dialogId) ? Integer.MIN_VALUE : Integer.MAX_VALUE);
                    i++;
                } else {
                    loadMediaCounts();
                    NotificationCenter notificationCenter = this.parentFragment.getNotificationCenter();
                    notificationCenter.addObserver(this, NotificationCenter.mediaCountsDidLoad);
                    notificationCenter.addObserver(this, NotificationCenter.mediaCountDidLoad);
                    notificationCenter.addObserver(this, NotificationCenter.didReceiveNewMessages);
                    notificationCenter.addObserver(this, NotificationCenter.messageReceivedByServer);
                    notificationCenter.addObserver(this, NotificationCenter.mediaDidLoad);
                    notificationCenter.addObserver(this, NotificationCenter.messagesDeleted);
                    notificationCenter.addObserver(this, NotificationCenter.replaceMessagesObjects);
                    notificationCenter.addObserver(this, NotificationCenter.chatInfoDidLoad);
                    notificationCenter.addObserver(this, NotificationCenter.fileLoaded);
                    return;
                }
            }
        }

        public void addDelegate(SharedMediaPreloaderDelegate sharedMediaPreloaderDelegate) {
            this.delegates.add(sharedMediaPreloaderDelegate);
        }

        public void removeDelegate(SharedMediaPreloaderDelegate sharedMediaPreloaderDelegate) {
            this.delegates.remove(sharedMediaPreloaderDelegate);
        }

        public void onDestroy(BaseFragment baseFragment) {
            if (baseFragment != this.parentFragment) {
                return;
            }
            this.delegates.clear();
            NotificationCenter notificationCenter = this.parentFragment.getNotificationCenter();
            notificationCenter.removeObserver(this, NotificationCenter.mediaCountsDidLoad);
            notificationCenter.removeObserver(this, NotificationCenter.mediaCountDidLoad);
            notificationCenter.removeObserver(this, NotificationCenter.didReceiveNewMessages);
            notificationCenter.removeObserver(this, NotificationCenter.messageReceivedByServer);
            notificationCenter.removeObserver(this, NotificationCenter.mediaDidLoad);
            notificationCenter.removeObserver(this, NotificationCenter.messagesDeleted);
            notificationCenter.removeObserver(this, NotificationCenter.replaceMessagesObjects);
            notificationCenter.removeObserver(this, NotificationCenter.chatInfoDidLoad);
            notificationCenter.removeObserver(this, NotificationCenter.fileLoaded);
        }

        public int[] getLastMediaCount() {
            return this.lastMediaCount;
        }

        public SharedMediaData[] getSharedMediaData() {
            return this.sharedMediaData;
        }

        /* JADX WARN: Removed duplicated region for block: B:40:0x009b  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x00a0  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x015b A[LOOP:2: B:68:0x0159->B:69:0x015b, LOOP_END] */
        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            int mediaType;
            int size;
            int[] iArr;
            int i3;
            int i4;
            int i5 = 1;
            int i6 = 0;
            if (i == NotificationCenter.mediaCountsDidLoad) {
                long longValue = ((Long) objArr[0]).longValue();
                long j = this.dialogId;
                if (longValue != j && longValue != this.mergeDialogId) {
                    return;
                }
                int[] iArr2 = (int[]) objArr[1];
                if (longValue == j) {
                    this.mediaCount = iArr2;
                } else {
                    this.mediaMergeCount = iArr2;
                }
                int i7 = 0;
                while (i7 < iArr2.length) {
                    int[] iArr3 = this.mediaCount;
                    if (iArr3[i7] >= 0) {
                        int[] iArr4 = this.mediaMergeCount;
                        if (iArr4[i7] >= 0) {
                            this.lastMediaCount[i7] = iArr3[i7] + iArr4[i7];
                            if (longValue == this.dialogId || this.lastMediaCount[i7] == 0 || this.lastLoadMediaCount[i7] == this.mediaCount[i7]) {
                                i3 = i7;
                                iArr = iArr2;
                            } else {
                                if (i7 == 0) {
                                    SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                                    if (sharedMediaDataArr[0].filterType == 1) {
                                        i4 = 6;
                                    } else if (sharedMediaDataArr[0].filterType == 2) {
                                        i4 = 7;
                                    }
                                    i3 = i7;
                                    iArr = iArr2;
                                    this.parentFragment.getMediaDataController().loadMedia(longValue, this.lastLoadMediaCount[i7] != -1 ? 30 : 20, 0, 0, i4, 2, this.parentFragment.getClassGuid(), 0);
                                    this.lastLoadMediaCount[i3] = this.mediaCount[i3];
                                }
                                i4 = i7;
                                i3 = i7;
                                iArr = iArr2;
                                this.parentFragment.getMediaDataController().loadMedia(longValue, this.lastLoadMediaCount[i7] != -1 ? 30 : 20, 0, 0, i4, 2, this.parentFragment.getClassGuid(), 0);
                                this.lastLoadMediaCount[i3] = this.mediaCount[i3];
                            }
                            i7 = i3 + 1;
                            iArr2 = iArr;
                        }
                    }
                    if (iArr3[i7] >= 0) {
                        this.lastMediaCount[i7] = iArr3[i7];
                    } else {
                        this.lastMediaCount[i7] = Math.max(this.mediaMergeCount[i7], 0);
                    }
                    if (longValue == this.dialogId) {
                    }
                    i3 = i7;
                    iArr = iArr2;
                    i7 = i3 + 1;
                    iArr2 = iArr;
                }
                this.mediaWasLoaded = true;
                int size2 = this.delegates.size();
                while (i6 < size2) {
                    this.delegates.get(i6).mediaCountUpdated();
                    i6++;
                }
            } else if (i == NotificationCenter.mediaCountDidLoad) {
                long longValue2 = ((Long) objArr[0]).longValue();
                if (longValue2 != this.dialogId && longValue2 != this.mergeDialogId) {
                    return;
                }
                int intValue = ((Integer) objArr[3]).intValue();
                int intValue2 = ((Integer) objArr[1]).intValue();
                if (longValue2 == this.dialogId) {
                    this.mediaCount[intValue] = intValue2;
                } else {
                    this.mediaMergeCount[intValue] = intValue2;
                }
                int[] iArr5 = this.mediaCount;
                if (iArr5[intValue] >= 0) {
                    int[] iArr6 = this.mediaMergeCount;
                    if (iArr6[intValue] >= 0) {
                        this.lastMediaCount[intValue] = iArr5[intValue] + iArr6[intValue];
                        size = this.delegates.size();
                        while (i6 < size) {
                            this.delegates.get(i6).mediaCountUpdated();
                            i6++;
                        }
                    }
                }
                if (iArr5[intValue] >= 0) {
                    this.lastMediaCount[intValue] = iArr5[intValue];
                } else {
                    this.lastMediaCount[intValue] = Math.max(this.mediaMergeCount[intValue], 0);
                }
                size = this.delegates.size();
                while (i6 < size) {
                }
            } else if (i == NotificationCenter.didReceiveNewMessages) {
                if (((Boolean) objArr[2]).booleanValue() || this.dialogId != ((Long) objArr[0]).longValue()) {
                    return;
                }
                boolean isEncryptedDialog = DialogObject.isEncryptedDialog(this.dialogId);
                ArrayList arrayList = (ArrayList) objArr[1];
                for (int i8 = 0; i8 < arrayList.size(); i8++) {
                    MessageObject messageObject = (MessageObject) arrayList.get(i8);
                    if (messageObject.messageOwner.media != null && !messageObject.needDrawBluredPreview() && (mediaType = MediaDataController.getMediaType(messageObject.messageOwner)) != -1 && ((mediaType != 0 || this.sharedMediaData[0].filterType != 2 || messageObject.isVideo()) && (mediaType != 0 || this.sharedMediaData[0].filterType != 1 || !messageObject.isVideo()))) {
                        SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                        if (sharedMediaDataArr2[mediaType].startReached) {
                            sharedMediaDataArr2[mediaType].addMessage(messageObject, 0, true, isEncryptedDialog);
                        }
                        this.sharedMediaData[mediaType].totalCount++;
                        for (int i9 = 0; i9 < this.sharedMediaData[mediaType].fastScrollPeriods.size(); i9++) {
                            this.sharedMediaData[mediaType].fastScrollPeriods.get(i9).startOffset++;
                        }
                    }
                }
                loadMediaCounts();
            } else if (i == NotificationCenter.messageReceivedByServer) {
                if (((Boolean) objArr[6]).booleanValue()) {
                    return;
                }
                Integer num = (Integer) objArr[0];
                Integer num2 = (Integer) objArr[1];
                while (true) {
                    SharedMediaData[] sharedMediaDataArr3 = this.sharedMediaData;
                    if (i6 >= sharedMediaDataArr3.length) {
                        return;
                    }
                    sharedMediaDataArr3[i6].replaceMid(num.intValue(), num2.intValue());
                    i6++;
                }
            } else if (i == NotificationCenter.mediaDidLoad) {
                long longValue3 = ((Long) objArr[0]).longValue();
                if (((Integer) objArr[3]).intValue() != this.parentFragment.getClassGuid()) {
                    return;
                }
                int intValue3 = ((Integer) objArr[4]).intValue();
                if (intValue3 != 0 && intValue3 != 6 && intValue3 != 7 && intValue3 != 1 && intValue3 != 2 && intValue3 != 4) {
                    this.sharedMediaData[intValue3].setTotalCount(((Integer) objArr[1]).intValue());
                }
                ArrayList arrayList2 = (ArrayList) objArr[2];
                boolean isEncryptedDialog2 = DialogObject.isEncryptedDialog(longValue3);
                if (longValue3 == this.dialogId) {
                    i5 = 0;
                }
                if (intValue3 == 0 || intValue3 == 6 || intValue3 == 7) {
                    if (intValue3 != this.sharedMediaData[0].filterType) {
                        return;
                    }
                    intValue3 = 0;
                }
                if (!arrayList2.isEmpty()) {
                    this.sharedMediaData[intValue3].setEndReached(i5, ((Boolean) objArr[5]).booleanValue());
                }
                for (int i10 = 0; i10 < arrayList2.size(); i10++) {
                    this.sharedMediaData[intValue3].addMessage((MessageObject) arrayList2.get(i10), i5, false, isEncryptedDialog2);
                }
            } else if (i == NotificationCenter.messagesDeleted) {
                if (((Boolean) objArr[2]).booleanValue()) {
                    return;
                }
                long longValue4 = ((Long) objArr[1]).longValue();
                TLRPC$Chat chat = DialogObject.isChatDialog(this.dialogId) ? this.parentFragment.getMessagesController().getChat(Long.valueOf(-this.dialogId)) : null;
                if (ChatObject.isChannel(chat)) {
                    if ((longValue4 != 0 || this.mergeDialogId == 0) && longValue4 != chat.id) {
                        return;
                    }
                } else if (longValue4 != 0) {
                    return;
                }
                ArrayList arrayList3 = (ArrayList) objArr[0];
                int size3 = arrayList3.size();
                boolean z = false;
                for (int i11 = 0; i11 < size3; i11++) {
                    int i12 = 0;
                    while (true) {
                        SharedMediaData[] sharedMediaDataArr4 = this.sharedMediaData;
                        if (i12 < sharedMediaDataArr4.length) {
                            MessageObject deleteMessage = sharedMediaDataArr4[i12].deleteMessage(((Integer) arrayList3.get(i11)).intValue(), 0);
                            if (deleteMessage != null) {
                                if (deleteMessage.getDialogId() == this.dialogId) {
                                    int[] iArr7 = this.mediaCount;
                                    if (iArr7[i12] > 0) {
                                        iArr7[i12] = iArr7[i12] - 1;
                                    }
                                } else {
                                    int[] iArr8 = this.mediaMergeCount;
                                    if (iArr8[i12] > 0) {
                                        iArr8[i12] = iArr8[i12] - 1;
                                    }
                                }
                                z = true;
                            }
                            i12++;
                        }
                    }
                }
                if (z) {
                    int i13 = 0;
                    while (true) {
                        int[] iArr9 = this.mediaCount;
                        if (i13 >= iArr9.length) {
                            break;
                        }
                        if (iArr9[i13] >= 0) {
                            int[] iArr10 = this.mediaMergeCount;
                            if (iArr10[i13] >= 0) {
                                this.lastMediaCount[i13] = iArr9[i13] + iArr10[i13];
                                i13++;
                            }
                        }
                        if (iArr9[i13] >= 0) {
                            this.lastMediaCount[i13] = iArr9[i13];
                        } else {
                            this.lastMediaCount[i13] = Math.max(this.mediaMergeCount[i13], 0);
                        }
                        i13++;
                    }
                    int size4 = this.delegates.size();
                    while (i6 < size4) {
                        this.delegates.get(i6).mediaCountUpdated();
                        i6++;
                    }
                }
                loadMediaCounts();
            } else if (i == NotificationCenter.replaceMessagesObjects) {
                long longValue5 = ((Long) objArr[0]).longValue();
                long j2 = this.dialogId;
                if (longValue5 != j2 && longValue5 != this.mergeDialogId) {
                    return;
                }
                int i14 = longValue5 == j2 ? 0 : 1;
                ArrayList arrayList4 = (ArrayList) objArr[1];
                int size5 = arrayList4.size();
                for (int i15 = 0; i15 < size5; i15++) {
                    MessageObject messageObject2 = (MessageObject) arrayList4.get(i15);
                    int id = messageObject2.getId();
                    int mediaType2 = MediaDataController.getMediaType(messageObject2.messageOwner);
                    int i16 = 0;
                    while (true) {
                        SharedMediaData[] sharedMediaDataArr5 = this.sharedMediaData;
                        if (i16 >= sharedMediaDataArr5.length) {
                            break;
                        }
                        MessageObject messageObject3 = sharedMediaDataArr5[i16].messagesDict[i14].get(id);
                        if (messageObject3 != null) {
                            int mediaType3 = MediaDataController.getMediaType(messageObject2.messageOwner);
                            if (mediaType2 == -1 || mediaType3 != mediaType2) {
                                this.sharedMediaData[i16].deleteMessage(id, i14);
                                if (i14 == 0) {
                                    int[] iArr11 = this.mediaCount;
                                    if (iArr11[i16] > 0) {
                                        iArr11[i16] = iArr11[i16] - 1;
                                    }
                                } else {
                                    int[] iArr12 = this.mediaMergeCount;
                                    if (iArr12[i16] > 0) {
                                        iArr12[i16] = iArr12[i16] - 1;
                                    }
                                }
                            } else {
                                int indexOf = this.sharedMediaData[i16].messages.indexOf(messageObject3);
                                if (indexOf >= 0) {
                                    this.sharedMediaData[i16].messagesDict[i14].put(id, messageObject2);
                                    this.sharedMediaData[i16].messages.set(indexOf, messageObject2);
                                }
                            }
                        } else {
                            i16++;
                        }
                    }
                }
            } else if (i == NotificationCenter.chatInfoDidLoad) {
                TLRPC$ChatFull tLRPC$ChatFull = (TLRPC$ChatFull) objArr[0];
                long j3 = this.dialogId;
                if (j3 >= 0 || tLRPC$ChatFull.id != (-j3)) {
                    return;
                }
                setChatInfo(tLRPC$ChatFull);
            } else if (i == NotificationCenter.fileLoaded) {
                ArrayList arrayList5 = new ArrayList();
                while (true) {
                    SharedMediaData[] sharedMediaDataArr6 = this.sharedMediaData;
                    if (i6 < sharedMediaDataArr6.length) {
                        arrayList5.addAll(sharedMediaDataArr6[i6].messages);
                        i6++;
                    } else {
                        Utilities.globalQueue.postRunnable(new AnonymousClass1(this, i2, arrayList5));
                        return;
                    }
                }
            }
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$SharedMediaPreloader$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ int val$account;
            final /* synthetic */ ArrayList val$allMessages;

            AnonymousClass1(SharedMediaPreloader sharedMediaPreloader, int i, ArrayList arrayList) {
                this.val$account = i;
                this.val$allMessages = arrayList;
            }

            @Override // java.lang.Runnable
            public void run() {
                FileLoader.getInstance(this.val$account).checkMediaExistance(this.val$allMessages);
            }
        }

        private void loadMediaCounts() {
            this.parentFragment.getMediaDataController().getMediaCounts(this.dialogId, this.parentFragment.getClassGuid());
            if (this.mergeDialogId != 0) {
                this.parentFragment.getMediaDataController().getMediaCounts(this.mergeDialogId, this.parentFragment.getClassGuid());
            }
        }

        private void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
            if (tLRPC$ChatFull != null) {
                long j = tLRPC$ChatFull.migrated_from_chat_id;
                if (j == 0 || this.mergeDialogId != 0) {
                    return;
                }
                this.mergeDialogId = -j;
                this.parentFragment.getMediaDataController().getMediaCounts(this.mergeDialogId, this.parentFragment.getClassGuid());
            }
        }

        public boolean isMediaWasLoaded() {
            return this.mediaWasLoaded;
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends PhotoViewer.EmptyPhotoViewerProvider {
        AnonymousClass1() {
            SharedMediaLayout.this = r1;
        }

        /* JADX WARN: Removed duplicated region for block: B:101:0x0241 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:99:0x0154 A[SYNTHETIC] */
        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
            ImageReceiver imageReceiver;
            View pinnedHeader;
            SharedLinkCell sharedLinkCell;
            MessageObject message;
            if (messageObject != null && (SharedMediaLayout.this.mediaPages[0].selectedType == 0 || SharedMediaLayout.this.mediaPages[0].selectedType == 1 || SharedMediaLayout.this.mediaPages[0].selectedType == 3 || SharedMediaLayout.this.mediaPages[0].selectedType == 5)) {
                BlurredRecyclerView blurredRecyclerView = SharedMediaLayout.this.mediaPages[0].listView;
                int childCount = blurredRecyclerView.getChildCount();
                int i2 = -1;
                int i3 = -1;
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = blurredRecyclerView.getChildAt(i4);
                    int measuredHeight = SharedMediaLayout.this.mediaPages[0].listView.getMeasuredHeight();
                    View view = (View) SharedMediaLayout.this.getParent();
                    if (view != null && SharedMediaLayout.this.getY() + SharedMediaLayout.this.getMeasuredHeight() > view.getMeasuredHeight()) {
                        measuredHeight -= SharedMediaLayout.this.getBottom() - view.getMeasuredHeight();
                    }
                    if (childAt.getTop() < measuredHeight) {
                        int childAdapterPosition = blurredRecyclerView.getChildAdapterPosition(childAt);
                        if (childAdapterPosition < i2 || i2 == -1) {
                            i2 = childAdapterPosition;
                        }
                        if (childAdapterPosition > i3 || i3 == -1) {
                            i3 = childAdapterPosition;
                        }
                        int[] iArr = new int[2];
                        if (childAt instanceof SharedPhotoVideoCell2) {
                            SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) childAt;
                            MessageObject messageObject2 = sharedPhotoVideoCell2.getMessageObject();
                            if (messageObject2 != null) {
                                if (messageObject2.getId() == messageObject.getId()) {
                                    imageReceiver = sharedPhotoVideoCell2.imageReceiver;
                                    sharedPhotoVideoCell2.getLocationInWindow(iArr);
                                    iArr[0] = iArr[0] + Math.round(sharedPhotoVideoCell2.imageReceiver.getImageX());
                                    iArr[1] = iArr[1] + Math.round(sharedPhotoVideoCell2.imageReceiver.getImageY());
                                    if (imageReceiver == null) {
                                        PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                                        placeProviderObject.viewX = iArr[0];
                                        placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
                                        placeProviderObject.parentView = blurredRecyclerView;
                                        placeProviderObject.animatingImageView = SharedMediaLayout.this.mediaPages[0].animatingImageView;
                                        SharedMediaLayout.this.mediaPages[0].listView.getLocationInWindow(iArr);
                                        placeProviderObject.animatingImageViewYOffset = -iArr[1];
                                        placeProviderObject.imageReceiver = imageReceiver;
                                        placeProviderObject.allowTakeAnimation = false;
                                        placeProviderObject.radius = imageReceiver.getRoundRadius();
                                        placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
                                        placeProviderObject.parentView.getLocationInWindow(iArr);
                                        placeProviderObject.clipTopAddition = 0;
                                        placeProviderObject.starOffset = SharedMediaLayout.this.sharedMediaData[0].startOffset;
                                        if (SharedMediaLayout.this.fragmentContextView != null && SharedMediaLayout.this.fragmentContextView.getVisibility() == 0) {
                                            placeProviderObject.clipTopAddition += AndroidUtilities.dp(36.0f);
                                        }
                                        if (PhotoViewer.isShowingImage(messageObject) && (pinnedHeader = blurredRecyclerView.getPinnedHeader()) != null) {
                                            int height = (SharedMediaLayout.this.fragmentContextView == null || SharedMediaLayout.this.fragmentContextView.getVisibility() != 0) ? 0 : (SharedMediaLayout.this.fragmentContextView.getHeight() - AndroidUtilities.dp(2.5f)) + 0;
                                            boolean z2 = childAt instanceof SharedDocumentCell;
                                            if (z2) {
                                                height += AndroidUtilities.dp(8.0f);
                                            }
                                            int i5 = height - placeProviderObject.viewY;
                                            if (i5 > childAt.getHeight()) {
                                                blurredRecyclerView.scrollBy(0, -(i5 + pinnedHeader.getHeight()));
                                            } else {
                                                int height2 = placeProviderObject.viewY - blurredRecyclerView.getHeight();
                                                if (z2) {
                                                    height2 -= AndroidUtilities.dp(8.0f);
                                                }
                                                if (height2 >= 0) {
                                                    blurredRecyclerView.scrollBy(0, height2 + childAt.getHeight());
                                                }
                                            }
                                        }
                                        return placeProviderObject;
                                    }
                                }
                                imageReceiver = null;
                                if (imageReceiver == null) {
                                }
                            } else {
                                continue;
                            }
                        } else if (childAt instanceof SharedDocumentCell) {
                            SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) childAt;
                            if (sharedDocumentCell.getMessage().getId() == messageObject.getId()) {
                                BackupImageView imageView = sharedDocumentCell.getImageView();
                                imageReceiver = imageView.getImageReceiver();
                                imageView.getLocationInWindow(iArr);
                                if (imageReceiver == null) {
                                }
                            }
                            imageReceiver = null;
                            if (imageReceiver == null) {
                            }
                        } else if (childAt instanceof ContextLinkCell) {
                            ContextLinkCell contextLinkCell = (ContextLinkCell) childAt;
                            MessageObject messageObject3 = (MessageObject) contextLinkCell.getParentObject();
                            if (messageObject3 != null && messageObject3.getId() == messageObject.getId()) {
                                imageReceiver = contextLinkCell.getPhotoImage();
                                contextLinkCell.getLocationInWindow(iArr);
                                if (imageReceiver == null) {
                                }
                            }
                            imageReceiver = null;
                            if (imageReceiver == null) {
                            }
                        } else {
                            if ((childAt instanceof SharedLinkCell) && (message = (sharedLinkCell = (SharedLinkCell) childAt).getMessage()) != null && message.getId() == messageObject.getId()) {
                                imageReceiver = sharedLinkCell.getLinkImageView();
                                sharedLinkCell.getLocationInWindow(iArr);
                                if (imageReceiver == null) {
                                }
                            }
                            imageReceiver = null;
                            if (imageReceiver == null) {
                            }
                        }
                    }
                }
                if (SharedMediaLayout.this.mediaPages[0].selectedType == 0 && i2 >= 0 && i3 >= 0) {
                    int positionForIndex = SharedMediaLayout.this.photoVideoAdapter.getPositionForIndex(i);
                    if (positionForIndex <= i2) {
                        SharedMediaLayout.this.mediaPages[0].layoutManager.scrollToPositionWithOffset(positionForIndex, 0);
                        SharedMediaLayout.this.delegate.scrollToSharedMedia();
                    } else if (positionForIndex >= i3 && i3 >= 0) {
                        SharedMediaLayout.this.mediaPages[0].layoutManager.scrollToPositionWithOffset(positionForIndex, 0, true);
                        SharedMediaLayout.this.delegate.scrollToSharedMedia();
                    }
                }
            }
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static class SharedMediaData {
        private int endLoadingStubs;
        public boolean fastScrollDataLoaded;
        public int frozenEndLoadingStubs;
        public int frozenStartOffset;
        private boolean hasPhotos;
        private boolean hasVideos;
        public boolean isFrozen;
        public boolean loading;
        public boolean loadingAfterFastScroll;
        public int min_id;
        public int requestIndex;
        private int startOffset;
        public int totalCount;
        public ArrayList<MessageObject> messages = new ArrayList<>();
        public SparseArray<MessageObject>[] messagesDict = {new SparseArray<>(), new SparseArray<>()};
        public ArrayList<String> sections = new ArrayList<>();
        public HashMap<String, ArrayList<MessageObject>> sectionArrays = new HashMap<>();
        public ArrayList<Period> fastScrollPeriods = new ArrayList<>();
        public boolean[] endReached = {false, true};
        public int[] max_id = {0, 0};
        public boolean startReached = true;
        public int filterType = 0;
        public ArrayList<MessageObject> frozenMessages = new ArrayList<>();
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

        static /* synthetic */ int access$7010(SharedMediaData sharedMediaData) {
            int i = sharedMediaData.endLoadingStubs;
            sharedMediaData.endLoadingStubs = i - 1;
            return i;
        }

        static /* synthetic */ int access$710(SharedMediaData sharedMediaData) {
            int i = sharedMediaData.startOffset;
            sharedMediaData.startOffset = i - 1;
            return i;
        }

        public void setTotalCount(int i) {
            this.totalCount = i;
        }

        public void setMaxId(int i, int i2) {
            this.max_id[i] = i2;
        }

        public void setEndReached(int i, boolean z) {
            this.endReached[i] = z;
        }

        public boolean addMessage(MessageObject messageObject, int i, boolean z, boolean z2) {
            if (this.messagesDict[i].indexOfKey(messageObject.getId()) >= 0) {
                return false;
            }
            ArrayList<MessageObject> arrayList = this.sectionArrays.get(messageObject.monthKey);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.sectionArrays.put(messageObject.monthKey, arrayList);
                if (z) {
                    this.sections.add(0, messageObject.monthKey);
                } else {
                    this.sections.add(messageObject.monthKey);
                }
            }
            if (z) {
                arrayList.add(0, messageObject);
                this.messages.add(0, messageObject);
            } else {
                arrayList.add(messageObject);
                this.messages.add(messageObject);
            }
            this.messagesDict[i].put(messageObject.getId(), messageObject);
            if (!z2) {
                if (messageObject.getId() > 0) {
                    this.max_id[i] = Math.min(messageObject.getId(), this.max_id[i]);
                    this.min_id = Math.max(messageObject.getId(), this.min_id);
                }
            } else {
                this.max_id[i] = Math.max(messageObject.getId(), this.max_id[i]);
                this.min_id = Math.min(messageObject.getId(), this.min_id);
            }
            if (!this.hasVideos && messageObject.isVideo()) {
                this.hasVideos = true;
            }
            if (!this.hasPhotos && messageObject.isPhoto()) {
                this.hasPhotos = true;
            }
            return true;
        }

        public MessageObject deleteMessage(int i, int i2) {
            ArrayList<MessageObject> arrayList;
            MessageObject messageObject = this.messagesDict[i2].get(i);
            if (messageObject == null || (arrayList = this.sectionArrays.get(messageObject.monthKey)) == null) {
                return null;
            }
            arrayList.remove(messageObject);
            this.messages.remove(messageObject);
            this.messagesDict[i2].remove(messageObject.getId());
            if (arrayList.isEmpty()) {
                this.sectionArrays.remove(messageObject.monthKey);
                this.sections.remove(messageObject.monthKey);
            }
            this.totalCount--;
            return messageObject;
        }

        public void replaceMid(int i, int i2) {
            MessageObject messageObject = this.messagesDict[0].get(i);
            if (messageObject != null) {
                this.messagesDict[0].remove(i);
                this.messagesDict[0].put(i2, messageObject);
                messageObject.messageOwner.id = i2;
                int[] iArr = this.max_id;
                iArr[0] = Math.min(i2, iArr[0]);
            }
        }

        public ArrayList<MessageObject> getMessages() {
            return this.isFrozen ? this.frozenMessages : this.messages;
        }

        public int getStartOffset() {
            return this.isFrozen ? this.frozenStartOffset : this.startOffset;
        }

        public void setListFrozen(boolean z) {
            if (this.isFrozen == z) {
                return;
            }
            this.isFrozen = z;
            if (!z) {
                return;
            }
            this.frozenStartOffset = this.startOffset;
            this.frozenEndLoadingStubs = this.endLoadingStubs;
            this.frozenMessages.clear();
            this.frozenMessages.addAll(this.messages);
        }

        public int getEndLoadingStubs() {
            return this.isFrozen ? this.frozenEndLoadingStubs : this.endLoadingStubs;
        }
    }

    /* loaded from: classes3.dex */
    public static class Period {
        int date;
        public String formatedDate;
        int maxId;
        public int startOffset;

        public Period(TLRPC$TL_searchResultPosition tLRPC$TL_searchResultPosition) {
            int i = tLRPC$TL_searchResultPosition.date;
            this.date = i;
            this.maxId = tLRPC$TL_searchResultPosition.msg_id;
            this.startOffset = tLRPC$TL_searchResultPosition.offset;
            this.formatedDate = LocaleController.formatYearMont(i, true);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SharedMediaLayout(Context context, long j, SharedMediaPreloader sharedMediaPreloader, int i, ArrayList<Integer> arrayList, TLRPC$ChatFull tLRPC$ChatFull, boolean z, BaseFragment baseFragment, Delegate delegate, int i2, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        String str;
        String str2;
        RecyclerListView.Holder holder;
        TLRPC$ChatFull tLRPC$ChatFull2;
        TLRPC$ChatFull tLRPC$ChatFull3 = tLRPC$ChatFull;
        this.mediaColumnsCount = 3;
        this.viewType = i2;
        this.resourcesProvider = resourcesProvider;
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        this.globalGradientView = flickerLoadingView;
        flickerLoadingView.setIsSingleCell(true);
        this.sharedMediaPreloader = sharedMediaPreloader;
        this.delegate = delegate;
        int[] lastMediaCount = sharedMediaPreloader.getLastMediaCount();
        this.hasMedia = new int[]{lastMediaCount[0], lastMediaCount[1], lastMediaCount[2], lastMediaCount[3], lastMediaCount[4], lastMediaCount[5], i};
        if (z) {
            this.initialTab = 7;
        } else {
            int i3 = 0;
            while (true) {
                int[] iArr = this.hasMedia;
                if (i3 >= iArr.length) {
                    break;
                } else if (iArr[i3] == -1 || iArr[i3] > 0) {
                    break;
                } else {
                    i3++;
                }
            }
            this.initialTab = i3;
        }
        this.info = tLRPC$ChatFull3;
        if (tLRPC$ChatFull3 != null) {
            this.mergeDialogId = -tLRPC$ChatFull3.migrated_from_chat_id;
        }
        this.dialog_id = j;
        int i4 = 0;
        while (true) {
            SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
            if (i4 >= sharedMediaDataArr.length) {
                break;
            }
            sharedMediaDataArr[i4] = new SharedMediaData();
            this.sharedMediaData[i4].max_id[0] = DialogObject.isEncryptedDialog(this.dialog_id) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            fillMediaData(i4);
            if (this.mergeDialogId != 0 && (tLRPC$ChatFull2 = this.info) != null) {
                SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                sharedMediaDataArr2[i4].max_id[1] = tLRPC$ChatFull2.migrated_from_max_id;
                sharedMediaDataArr2[i4].endReached[1] = false;
            }
            i4++;
        }
        this.profileActivity = baseFragment;
        this.actionBar = baseFragment.getActionBar();
        this.mediaColumnsCount = SharedConfig.mediaColumnsCount;
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.mediaDidLoad);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagesDeleted);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.didReceiveNewMessages);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByServer);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagePlayingDidReset);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagePlayingDidStart);
        for (int i5 = 0; i5 < 10; i5++) {
            if (this.initialTab == 4) {
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
                anonymousClass2.initStreamingIcons();
                this.audioCellCache.add(anonymousClass2);
            }
        }
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.searching = false;
        this.searchWas = false;
        Drawable drawable = context.getResources().getDrawable(2131166050);
        this.pinnedHeaderShadowDrawable = drawable;
        drawable.setColorFilter(new PorterDuffColorFilter(getThemedColor("windowBackgroundGrayShadow"), PorterDuff.Mode.MULTIPLY));
        ScrollSlidingTextTabStripInner scrollSlidingTextTabStripInner = this.scrollSlidingTextTabStrip;
        if (scrollSlidingTextTabStripInner != null) {
            this.initialTab = scrollSlidingTextTabStripInner.getCurrentTabId();
        }
        this.scrollSlidingTextTabStrip = createScrollingTextTabStrip(context);
        for (int i6 = 1; i6 >= 0; i6--) {
            this.selectedFiles[i6].clear();
        }
        this.cantDeleteMessagesCount = 0;
        this.actionModeViews.clear();
        ActionBarMenu createMenu = this.actionBar.createMenu();
        createMenu.addOnLayoutChangeListener(new AnonymousClass3());
        ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass4());
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setTranslationY(AndroidUtilities.dp(10.0f));
        this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131628092));
        this.searchItem.setContentDescription(LocaleController.getString("Search", 2131628092));
        this.searchItem.setVisibility(4);
        ImageView imageView = new ImageView(context);
        this.photoVideoOptionsItem = imageView;
        imageView.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
        this.photoVideoOptionsItem.setTranslationY(AndroidUtilities.dp(10.0f));
        this.photoVideoOptionsItem.setVisibility(4);
        Drawable mutate = ContextCompat.getDrawable(context, 2131165453).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor("windowBackgroundWhiteGrayText2"), PorterDuff.Mode.MULTIPLY));
        this.photoVideoOptionsItem.setImageDrawable(mutate);
        this.photoVideoOptionsItem.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.actionBar.addView(this.photoVideoOptionsItem, LayoutHelper.createFrame(48, 56, 85));
        this.photoVideoOptionsItem.setOnClickListener(new AnonymousClass5(context));
        EditTextBoldCursor searchField = this.searchItem.getSearchField();
        searchField.setTextColor(getThemedColor("windowBackgroundWhiteBlackText"));
        searchField.setHintTextColor(getThemedColor("player_time"));
        searchField.setCursorColor(getThemedColor("windowBackgroundWhiteBlackText"));
        this.searchItemState = 0;
        BaseFragment baseFragment2 = this.profileActivity;
        BlurredLinearLayout blurredLinearLayout = new BlurredLinearLayout(context, (baseFragment2 == null || !(baseFragment2.getFragmentView() instanceof SizeNotifierFrameLayout)) ? null : (SizeNotifierFrameLayout) this.profileActivity.getFragmentView());
        this.actionModeLayout = blurredLinearLayout;
        blurredLinearLayout.setBackgroundColor(getThemedColor("windowBackgroundWhite"));
        this.actionModeLayout.setAlpha(0.0f);
        this.actionModeLayout.setClickable(true);
        this.actionModeLayout.setVisibility(4);
        ImageView imageView2 = new ImageView(context);
        this.closeButton = imageView2;
        imageView2.setScaleType(ImageView.ScaleType.CENTER);
        ImageView imageView3 = this.closeButton;
        BackDrawable backDrawable = new BackDrawable(true);
        this.backDrawable = backDrawable;
        imageView3.setImageDrawable(backDrawable);
        this.backDrawable.setColor(getThemedColor("windowBackgroundWhiteGrayText2"));
        this.closeButton.setBackground(Theme.createSelectorDrawable(getThemedColor("actionBarActionModeDefaultSelector"), 1));
        this.closeButton.setContentDescription(LocaleController.getString("Close", 2131625167));
        this.actionModeLayout.addView(this.closeButton, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
        this.actionModeViews.add(this.closeButton);
        this.closeButton.setOnClickListener(new SharedMediaLayout$$ExternalSyntheticLambda2(this));
        NumberTextView numberTextView = new NumberTextView(context);
        this.selectedMessagesCountTextView = numberTextView;
        numberTextView.setTextSize(18);
        this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.selectedMessagesCountTextView.setTextColor(getThemedColor("windowBackgroundWhiteGrayText2"));
        this.actionModeLayout.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 18, 0, 0, 0));
        this.actionModeViews.add(this.selectedMessagesCountTextView);
        if (!DialogObject.isEncryptedDialog(this.dialog_id)) {
            str = "actionBarActionModeDefaultSelector";
            str2 = "windowBackgroundWhiteGrayText2";
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, getThemedColor("actionBarActionModeDefaultSelector"), getThemedColor("windowBackgroundWhiteGrayText2"), false);
            this.gotoItem = actionBarMenuItem;
            actionBarMenuItem.setIcon(2131165800);
            this.gotoItem.setContentDescription(LocaleController.getString("AccDescrGoToMessage", 2131623988));
            this.gotoItem.setDuplicateParentStateEnabled(false);
            this.actionModeLayout.addView(this.gotoItem, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
            this.actionModeViews.add(this.gotoItem);
            this.gotoItem.setOnClickListener(new SharedMediaLayout$$ExternalSyntheticLambda0(this));
            ActionBarMenuItem actionBarMenuItem2 = new ActionBarMenuItem(context, (ActionBarMenu) null, getThemedColor(str), getThemedColor(str2), false);
            this.forwardItem = actionBarMenuItem2;
            actionBarMenuItem2.setIcon(2131165741);
            this.forwardItem.setContentDescription(LocaleController.getString("Forward", 2131625940));
            this.forwardItem.setDuplicateParentStateEnabled(false);
            this.actionModeLayout.addView(this.forwardItem, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
            this.actionModeViews.add(this.forwardItem);
            this.forwardItem.setOnClickListener(new SharedMediaLayout$$ExternalSyntheticLambda3(this));
            updateForwardItem();
        } else {
            str = "actionBarActionModeDefaultSelector";
            str2 = "windowBackgroundWhiteGrayText2";
        }
        ActionBarMenuItem actionBarMenuItem3 = new ActionBarMenuItem(context, (ActionBarMenu) null, getThemedColor(str), getThemedColor(str2), false);
        this.deleteItem = actionBarMenuItem3;
        actionBarMenuItem3.setIcon(2131165702);
        this.deleteItem.setContentDescription(LocaleController.getString("Delete", 2131625368));
        this.deleteItem.setDuplicateParentStateEnabled(false);
        this.actionModeLayout.addView(this.deleteItem, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
        this.actionModeViews.add(this.deleteItem);
        this.deleteItem.setOnClickListener(new SharedMediaLayout$$ExternalSyntheticLambda1(this));
        this.photoVideoAdapter = new AnonymousClass6(context);
        this.animationSupportingPhotoVideoAdapter = new SharedPhotoVideoAdapter(context);
        this.documentsAdapter = new SharedDocumentsAdapter(context, 1);
        this.voiceAdapter = new SharedDocumentsAdapter(context, 2);
        this.audioAdapter = new SharedDocumentsAdapter(context, 4);
        this.gifAdapter = new GifAdapter(context);
        this.documentsSearchAdapter = new MediaSearchAdapter(context, 1);
        this.audioSearchAdapter = new MediaSearchAdapter(context, 4);
        this.linksSearchAdapter = new MediaSearchAdapter(context, 3);
        this.groupUsersSearchAdapter = new GroupUsersSearchAdapter(context);
        this.commonGroupsAdapter = new CommonGroupsAdapter(context);
        ChatUsersAdapter chatUsersAdapter = new ChatUsersAdapter(context);
        this.chatUsersAdapter = chatUsersAdapter;
        chatUsersAdapter.sortedUsers = arrayList;
        this.chatUsersAdapter.chatInfo = !z ? null : tLRPC$ChatFull3;
        this.linksAdapter = new SharedLinksAdapter(context);
        setWillNotDraw(false);
        int i7 = 0;
        int i8 = -1;
        int i9 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i7 >= mediaPageArr.length) {
                break;
            }
            if (i7 == 0 && mediaPageArr[i7] != null && mediaPageArr[i7].layoutManager != null) {
                i8 = this.mediaPages[i7].layoutManager.findFirstVisibleItemPosition();
                if (i8 == this.mediaPages[i7].layoutManager.getItemCount() - 1 || (holder = (RecyclerListView.Holder) this.mediaPages[i7].listView.findViewHolderForAdapterPosition(i8)) == null) {
                    i8 = -1;
                } else {
                    i9 = holder.itemView.getTop();
                }
            }
            AnonymousClass7 anonymousClass7 = new AnonymousClass7(context);
            addView(anonymousClass7, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
            MediaPage[] mediaPageArr2 = this.mediaPages;
            mediaPageArr2[i7] = anonymousClass7;
            ExtendedGridLayoutManager extendedGridLayoutManager = mediaPageArr2[i7].layoutManager = new AnonymousClass8(context, 100, anonymousClass7);
            extendedGridLayoutManager.setSpanSizeLookup(new AnonymousClass9(anonymousClass7));
            this.mediaPages[i7].listView = new AnonymousClass10(context, anonymousClass7, extendedGridLayoutManager);
            this.mediaPages[i7].listView.setFastScrollEnabled(1);
            this.mediaPages[i7].listView.setScrollingTouchSlop(1);
            this.mediaPages[i7].listView.setPinnedSectionOffsetY(-AndroidUtilities.dp(2.0f));
            this.mediaPages[i7].listView.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
            this.mediaPages[i7].listView.setItemAnimator(null);
            this.mediaPages[i7].listView.setClipToPadding(false);
            this.mediaPages[i7].listView.setSectionsType(2);
            this.mediaPages[i7].listView.setLayoutManager(extendedGridLayoutManager);
            MediaPage[] mediaPageArr3 = this.mediaPages;
            mediaPageArr3[i7].addView(mediaPageArr3[i7].listView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i7].animationSupportingListView = new BlurredRecyclerView(context);
            this.mediaPages[i7].animationSupportingListView.setLayoutManager(this.mediaPages[i7].animationSupportingLayoutManager = new AnonymousClass11(context, 3));
            MediaPage[] mediaPageArr4 = this.mediaPages;
            mediaPageArr4[i7].addView(mediaPageArr4[i7].animationSupportingListView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i7].animationSupportingListView.setVisibility(8);
            this.mediaPages[i7].listView.addItemDecoration(new AnonymousClass12(anonymousClass7));
            this.mediaPages[i7].listView.setOnItemClickListener(new SharedMediaLayout$$ExternalSyntheticLambda15(this, anonymousClass7));
            this.mediaPages[i7].listView.setOnScrollListener(new AnonymousClass13(anonymousClass7, extendedGridLayoutManager));
            this.mediaPages[i7].listView.setOnItemLongClickListener(new SharedMediaLayout$$ExternalSyntheticLambda16(this, anonymousClass7));
            if (i7 == 0 && i8 != -1) {
                extendedGridLayoutManager.scrollToPositionWithOffset(i8, i9);
            }
            this.mediaPages[i7].animatingImageView = new AnonymousClass14(this, context, this.mediaPages[i7].listView);
            this.mediaPages[i7].animatingImageView.setVisibility(8);
            this.mediaPages[i7].listView.addOverlayView(this.mediaPages[i7].animatingImageView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i7].progressView = new AnonymousClass15(context, anonymousClass7);
            this.mediaPages[i7].progressView.showDate(false);
            if (i7 != 0) {
                this.mediaPages[i7].setVisibility(8);
            }
            MediaPage[] mediaPageArr5 = this.mediaPages;
            mediaPageArr5[i7].emptyView = new StickerEmptyView(context, mediaPageArr5[i7].progressView, 1);
            this.mediaPages[i7].emptyView.setVisibility(8);
            this.mediaPages[i7].emptyView.setAnimateLayoutChange(true);
            MediaPage[] mediaPageArr6 = this.mediaPages;
            mediaPageArr6[i7].addView(mediaPageArr6[i7].emptyView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i7].emptyView.setOnTouchListener(SharedMediaLayout$$ExternalSyntheticLambda4.INSTANCE);
            this.mediaPages[i7].emptyView.showProgress(true, false);
            this.mediaPages[i7].emptyView.title.setText(LocaleController.getString("NoResult", 2131626858));
            this.mediaPages[i7].emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", 2131628098));
            this.mediaPages[i7].emptyView.addView(this.mediaPages[i7].progressView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i7].listView.setEmptyView(this.mediaPages[i7].emptyView);
            this.mediaPages[i7].listView.setAnimateEmptyView(true, 0);
            MediaPage[] mediaPageArr7 = this.mediaPages;
            mediaPageArr7[i7].scrollHelper = new RecyclerAnimationScrollHelper(mediaPageArr7[i7].listView, this.mediaPages[i7].layoutManager);
            i7++;
        }
        ChatActionCell chatActionCell = new ChatActionCell(context);
        this.floatingDateView = chatActionCell;
        chatActionCell.setCustomDate((int) (System.currentTimeMillis() / 1000), false, false);
        this.floatingDateView.setAlpha(0.0f);
        this.floatingDateView.setOverrideColor("chat_mediaTimeBackground", "chat_mediaTimeText");
        this.floatingDateView.setTranslationY(-AndroidUtilities.dp(48.0f));
        addView(this.floatingDateView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 52.0f, 0.0f, 0.0f));
        FragmentContextView fragmentContextView = new FragmentContextView(context, baseFragment, this, false, resourcesProvider);
        this.fragmentContextView = fragmentContextView;
        addView(fragmentContextView, LayoutHelper.createFrame(-1, 38.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
        this.fragmentContextView.setDelegate(new SharedMediaLayout$$ExternalSyntheticLambda14(this));
        addView(this.scrollSlidingTextTabStrip, LayoutHelper.createFrame(-1, 48, 51));
        addView(this.actionModeLayout, LayoutHelper.createFrame(-1, 48, 51));
        View view = new View(context);
        this.shadowLine = view;
        view.setBackgroundColor(getThemedColor("divider"));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, 1);
        layoutParams.topMargin = AndroidUtilities.dp(48.0f) - 1;
        addView(this.shadowLine, layoutParams);
        updateTabs(false);
        switchToCurrentSelectedMode(false);
        if (this.hasMedia[0] >= 0) {
            loadFastScrollData(false);
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends SharedAudioCell {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            SharedMediaLayout.this = r1;
        }

        @Override // org.telegram.ui.Cells.SharedAudioCell
        public boolean needPlayMessage(MessageObject messageObject) {
            if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                boolean playMessage = MediaController.getInstance().playMessage(messageObject);
                MediaController.getInstance().setVoiceMessagesPlaylist(playMessage ? SharedMediaLayout.this.sharedMediaData[4].messages : null, false);
                return playMessage;
            } else if (!messageObject.isMusic()) {
                return false;
            } else {
                return MediaController.getInstance().setPlaylist(SharedMediaLayout.this.sharedMediaData[4].messages, messageObject, SharedMediaLayout.this.mergeDialogId);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements View.OnLayoutChangeListener {
        AnonymousClass3() {
            SharedMediaLayout.this = r1;
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            if (SharedMediaLayout.this.searchItem == null) {
                return;
            }
            SharedMediaLayout.this.searchItem.setTranslationX(((View) SharedMediaLayout.this.searchItem.getParent()).getMeasuredWidth() - SharedMediaLayout.this.searchItem.getRight());
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass4() {
            SharedMediaLayout.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            SharedMediaLayout.this.searching = true;
            SharedMediaLayout.this.onSearchStateChanged(true);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            SharedMediaLayout.this.searching = false;
            SharedMediaLayout.this.searchWas = false;
            SharedMediaLayout.this.documentsSearchAdapter.search(null, true);
            SharedMediaLayout.this.linksSearchAdapter.search(null, true);
            SharedMediaLayout.this.audioSearchAdapter.search(null, true);
            SharedMediaLayout.this.groupUsersSearchAdapter.search(null, true);
            SharedMediaLayout.this.onSearchStateChanged(false);
            if (SharedMediaLayout.this.ignoreSearchCollapse) {
                SharedMediaLayout.this.ignoreSearchCollapse = false;
            } else {
                SharedMediaLayout.this.switchToCurrentSelectedMode(false);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            String obj = editText.getText().toString();
            if (obj.length() != 0) {
                SharedMediaLayout.this.searchWas = true;
            } else {
                SharedMediaLayout.this.searchWas = false;
            }
            SharedMediaLayout.this.switchToCurrentSelectedMode(false);
            if (SharedMediaLayout.this.mediaPages[0].selectedType == 1) {
                if (SharedMediaLayout.this.documentsSearchAdapter == null) {
                    return;
                }
                SharedMediaLayout.this.documentsSearchAdapter.search(obj, true);
            } else if (SharedMediaLayout.this.mediaPages[0].selectedType == 3) {
                if (SharedMediaLayout.this.linksSearchAdapter == null) {
                    return;
                }
                SharedMediaLayout.this.linksSearchAdapter.search(obj, true);
            } else if (SharedMediaLayout.this.mediaPages[0].selectedType == 4) {
                if (SharedMediaLayout.this.audioSearchAdapter == null) {
                    return;
                }
                SharedMediaLayout.this.audioSearchAdapter.search(obj, true);
            } else if (SharedMediaLayout.this.mediaPages[0].selectedType != 7 || SharedMediaLayout.this.groupUsersSearchAdapter == null) {
            } else {
                SharedMediaLayout.this.groupUsersSearchAdapter.search(obj, true);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onLayout(int i, int i2, int i3, int i4) {
            SharedMediaLayout.this.searchItem.setTranslationX(((View) SharedMediaLayout.this.searchItem.getParent()).getMeasuredWidth() - SharedMediaLayout.this.searchItem.getRight());
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        final /* synthetic */ Context val$context;

        AnonymousClass5(Context context) {
            SharedMediaLayout.this = r1;
            this.val$context = context;
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$5$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends ActionBarPopupWindow.ActionBarPopupWindowLayout {
            final /* synthetic */ View val$dividerView;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(AnonymousClass5 anonymousClass5, Context context, View view) {
                super(context);
                this.val$dividerView = view;
            }

            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.ActionBarPopupWindowLayout, android.widget.FrameLayout, android.view.View
            public void onMeasure(int i, int i2) {
                if (this.val$dividerView.getParent() != null) {
                    this.val$dividerView.setVisibility(8);
                    super.onMeasure(i, i2);
                    this.val$dividerView.getLayoutParams().width = getMeasuredWidth() - AndroidUtilities.dp(16.0f);
                    this.val$dividerView.setVisibility(0);
                    super.onMeasure(i, i2);
                    return;
                }
                super.onMeasure(i, i2);
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DividerCell dividerCell = new DividerCell(this.val$context);
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, this.val$context, dividerCell);
            boolean z = true;
            ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(this.val$context, true, false);
            ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(this.val$context, false, false);
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("MediaZoomIn", 2131626571), 2131165990);
            actionBarMenuSubItem.setOnClickListener(new SharedMediaLayout$5$$ExternalSyntheticLambda0(this, actionBarMenuSubItem, actionBarMenuSubItem2));
            anonymousClass1.addView(actionBarMenuSubItem);
            actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("MediaZoomOut", 2131626572), 2131165991);
            actionBarMenuSubItem2.setOnClickListener(new AnonymousClass2(actionBarMenuSubItem2, actionBarMenuSubItem));
            if (SharedMediaLayout.this.mediaColumnsCount != 2) {
                if (SharedMediaLayout.this.mediaColumnsCount == 9) {
                    actionBarMenuSubItem2.setEnabled(false);
                    actionBarMenuSubItem2.setAlpha(0.5f);
                }
            } else {
                actionBarMenuSubItem.setEnabled(false);
                actionBarMenuSubItem.setAlpha(0.5f);
            }
            anonymousClass1.addView(actionBarMenuSubItem2);
            boolean z2 = (SharedMediaLayout.this.sharedMediaData[0].hasPhotos && SharedMediaLayout.this.sharedMediaData[0].hasVideos) || !SharedMediaLayout.this.sharedMediaData[0].endReached[0] || !SharedMediaLayout.this.sharedMediaData[0].endReached[1] || !SharedMediaLayout.this.sharedMediaData[0].startReached;
            if (!DialogObject.isEncryptedDialog(SharedMediaLayout.this.dialog_id)) {
                ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(this.val$context, false, false);
                actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("Calendar", 2131624771), 2131165662);
                anonymousClass1.addView(actionBarMenuSubItem3);
                actionBarMenuSubItem3.setOnClickListener(new AnonymousClass3());
                if (z2) {
                    anonymousClass1.addView(dividerCell);
                    ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(this.val$context, true, false, false);
                    ActionBarMenuSubItem actionBarMenuSubItem5 = new ActionBarMenuSubItem(this.val$context, true, false, true);
                    actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString("MediaShowPhotos", 2131626569), 0);
                    actionBarMenuSubItem4.setChecked(SharedMediaLayout.this.sharedMediaData[0].filterType == 0 || SharedMediaLayout.this.sharedMediaData[0].filterType == 1);
                    actionBarMenuSubItem4.setOnClickListener(new AnonymousClass4(actionBarMenuSubItem5, actionBarMenuSubItem4));
                    anonymousClass1.addView(actionBarMenuSubItem4);
                    actionBarMenuSubItem5.setTextAndIcon(LocaleController.getString("MediaShowVideos", 2131626570), 0);
                    if (SharedMediaLayout.this.sharedMediaData[0].filterType != 0 && SharedMediaLayout.this.sharedMediaData[0].filterType != 2) {
                        z = false;
                    }
                    actionBarMenuSubItem5.setChecked(z);
                    actionBarMenuSubItem5.setOnClickListener(new View$OnClickListenerC00295(actionBarMenuSubItem4, actionBarMenuSubItem5));
                    anonymousClass1.addView(actionBarMenuSubItem5);
                }
            }
            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
            sharedMediaLayout.optionsWindow = AlertsCreator.showPopupMenu(anonymousClass1, sharedMediaLayout.photoVideoOptionsItem, 0, -AndroidUtilities.dp(56.0f));
        }

        public /* synthetic */ void lambda$onClick$0(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2, View view) {
            if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                return;
            }
            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
            int nextMediaColumnsCount = sharedMediaLayout.getNextMediaColumnsCount(sharedMediaLayout.mediaColumnsCount, true);
            if (nextMediaColumnsCount == SharedMediaLayout.this.getNextMediaColumnsCount(nextMediaColumnsCount, true)) {
                actionBarMenuSubItem.setEnabled(false);
                actionBarMenuSubItem.animate().alpha(0.5f).start();
            }
            if (SharedMediaLayout.this.mediaColumnsCount == nextMediaColumnsCount) {
                return;
            }
            if (!actionBarMenuSubItem2.isEnabled()) {
                actionBarMenuSubItem2.setEnabled(true);
                actionBarMenuSubItem2.animate().alpha(1.0f).start();
            }
            SharedConfig.setMediaColumnsCount(nextMediaColumnsCount);
            SharedMediaLayout.this.animateToMediaColumnsCount(nextMediaColumnsCount);
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$5$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 implements View.OnClickListener {
            final /* synthetic */ ActionBarMenuSubItem val$mediaZoomInItem;
            final /* synthetic */ ActionBarMenuSubItem val$mediaZoomOutItem;

            AnonymousClass2(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2) {
                AnonymousClass5.this = r1;
                this.val$mediaZoomOutItem = actionBarMenuSubItem;
                this.val$mediaZoomInItem = actionBarMenuSubItem2;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                    return;
                }
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                int nextMediaColumnsCount = sharedMediaLayout.getNextMediaColumnsCount(sharedMediaLayout.mediaColumnsCount, false);
                if (nextMediaColumnsCount == SharedMediaLayout.this.getNextMediaColumnsCount(nextMediaColumnsCount, false)) {
                    this.val$mediaZoomOutItem.setEnabled(false);
                    this.val$mediaZoomOutItem.animate().alpha(0.5f).start();
                }
                if (SharedMediaLayout.this.mediaColumnsCount == nextMediaColumnsCount) {
                    return;
                }
                if (!this.val$mediaZoomInItem.isEnabled()) {
                    this.val$mediaZoomInItem.setEnabled(true);
                    this.val$mediaZoomInItem.animate().alpha(1.0f).start();
                }
                SharedConfig.setMediaColumnsCount(nextMediaColumnsCount);
                SharedMediaLayout.this.animateToMediaColumnsCount(nextMediaColumnsCount);
            }
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$5$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
                AnonymousClass5.this = r1;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SharedMediaLayout.this.showMediaCalendar(false);
                ActionBarPopupWindow actionBarPopupWindow = SharedMediaLayout.this.optionsWindow;
                if (actionBarPopupWindow != null) {
                    actionBarPopupWindow.dismiss();
                }
            }
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$5$4 */
        /* loaded from: classes3.dex */
        class AnonymousClass4 implements View.OnClickListener {
            final /* synthetic */ ActionBarMenuSubItem val$showPhotosItem;
            final /* synthetic */ ActionBarMenuSubItem val$showVideosItem;

            AnonymousClass4(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2) {
                AnonymousClass5.this = r1;
                this.val$showVideosItem = actionBarMenuSubItem;
                this.val$showPhotosItem = actionBarMenuSubItem2;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SharedMediaLayout.this.changeTypeAnimation) {
                    return;
                }
                if (!this.val$showVideosItem.getCheckView().isChecked() && this.val$showPhotosItem.getCheckView().isChecked()) {
                    return;
                }
                ActionBarMenuSubItem actionBarMenuSubItem = this.val$showPhotosItem;
                actionBarMenuSubItem.setChecked(!actionBarMenuSubItem.getCheckView().isChecked());
                if (!this.val$showPhotosItem.getCheckView().isChecked() || !this.val$showVideosItem.getCheckView().isChecked()) {
                    SharedMediaLayout.this.sharedMediaData[0].filterType = 2;
                } else {
                    SharedMediaLayout.this.sharedMediaData[0].filterType = 0;
                }
                SharedMediaLayout.this.changeMediaFilterType();
            }
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$5$5 */
        /* loaded from: classes3.dex */
        class View$OnClickListenerC00295 implements View.OnClickListener {
            final /* synthetic */ ActionBarMenuSubItem val$showPhotosItem;
            final /* synthetic */ ActionBarMenuSubItem val$showVideosItem;

            View$OnClickListenerC00295(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2) {
                AnonymousClass5.this = r1;
                this.val$showPhotosItem = actionBarMenuSubItem;
                this.val$showVideosItem = actionBarMenuSubItem2;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SharedMediaLayout.this.changeTypeAnimation) {
                    return;
                }
                if (!this.val$showPhotosItem.getCheckView().isChecked() && this.val$showVideosItem.getCheckView().isChecked()) {
                    return;
                }
                ActionBarMenuSubItem actionBarMenuSubItem = this.val$showVideosItem;
                actionBarMenuSubItem.setChecked(!actionBarMenuSubItem.getCheckView().isChecked());
                if (!this.val$showPhotosItem.getCheckView().isChecked() || !this.val$showVideosItem.getCheckView().isChecked()) {
                    SharedMediaLayout.this.sharedMediaData[0].filterType = 1;
                } else {
                    SharedMediaLayout.this.sharedMediaData[0].filterType = 0;
                }
                SharedMediaLayout.this.changeMediaFilterType();
            }
        }
    }

    public /* synthetic */ void lambda$new$2(View view) {
        closeActionMode();
    }

    public /* synthetic */ void lambda$new$3(View view) {
        onActionBarItemClick(view, 102);
    }

    public /* synthetic */ void lambda$new$4(View view) {
        onActionBarItemClick(view, 100);
    }

    public /* synthetic */ void lambda$new$5(View view) {
        onActionBarItemClick(view, 101);
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends SharedPhotoVideoAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            SharedMediaLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            MediaPage mediaPage = SharedMediaLayout.this.getMediaPage(0);
            if (mediaPage == null || mediaPage.animationSupportingListView.getVisibility() != 0) {
                return;
            }
            SharedMediaLayout.this.animationSupportingPhotoVideoAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends MediaPage {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context) {
            super(context);
            SharedMediaLayout.this = r1;
        }

        @Override // android.view.View
        public void setTranslationX(float f) {
            super.setTranslationX(f);
            if (SharedMediaLayout.this.tabsAnimationInProgress) {
                int i = 0;
                if (SharedMediaLayout.this.mediaPages[0] == this) {
                    float abs = Math.abs(SharedMediaLayout.this.mediaPages[0].getTranslationX()) / SharedMediaLayout.this.mediaPages[0].getMeasuredWidth();
                    SharedMediaLayout.this.scrollSlidingTextTabStrip.selectTabWithId(SharedMediaLayout.this.mediaPages[1].selectedType, abs);
                    if (SharedMediaLayout.this.canShowSearchItem()) {
                        if (SharedMediaLayout.this.searchItemState == 2) {
                            SharedMediaLayout.this.searchItem.setAlpha(1.0f - abs);
                        } else if (SharedMediaLayout.this.searchItemState == 1) {
                            SharedMediaLayout.this.searchItem.setAlpha(abs);
                        }
                        float f2 = (SharedMediaLayout.this.mediaPages[1] == null || SharedMediaLayout.this.mediaPages[1].selectedType != 0) ? 0.0f : abs;
                        if (SharedMediaLayout.this.mediaPages[0].selectedType == 0) {
                            f2 = 1.0f - abs;
                        }
                        SharedMediaLayout.this.photoVideoOptionsItem.setAlpha(f2);
                        SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                        ImageView imageView = sharedMediaLayout.photoVideoOptionsItem;
                        if (f2 == 0.0f || !sharedMediaLayout.canShowSearchItem()) {
                            i = 4;
                        }
                        imageView.setVisibility(i);
                    } else {
                        SharedMediaLayout.this.searchItem.setAlpha(0.0f);
                    }
                }
            }
            SharedMediaLayout.this.invalidateBlur();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends ExtendedGridLayoutManager {
        private Size size = new Size();
        final /* synthetic */ MediaPage val$mediaPage;

        @Override // org.telegram.ui.Components.ExtendedGridLayoutManager, androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(Context context, int i, MediaPage mediaPage) {
            super(context, i);
            SharedMediaLayout.this = r1;
            this.val$mediaPage = mediaPage;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
            super.calculateExtraLayoutSpace(state, iArr);
            if (this.val$mediaPage.selectedType != 0) {
                if (this.val$mediaPage.selectedType != 1) {
                    return;
                }
                iArr[1] = Math.max(iArr[1], AndroidUtilities.dp(56.0f) * 2);
                return;
            }
            iArr[1] = Math.max(iArr[1], SharedPhotoVideoCell.getItemSize(1) * 2);
        }

        @Override // org.telegram.ui.Components.ExtendedGridLayoutManager
        protected Size getSizeForItem(int i) {
            int i2;
            int i3;
            TLRPC$Document document = (this.val$mediaPage.listView.getAdapter() != SharedMediaLayout.this.gifAdapter || SharedMediaLayout.this.sharedMediaData[5].messages.isEmpty()) ? null : SharedMediaLayout.this.sharedMediaData[5].messages.get(i).getDocument();
            Size size = this.size;
            size.height = 100.0f;
            size.width = 100.0f;
            if (document != null) {
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                if (closestPhotoSizeWithSize != null && (i2 = closestPhotoSizeWithSize.w) != 0 && (i3 = closestPhotoSizeWithSize.h) != 0) {
                    Size size2 = this.size;
                    size2.width = i2;
                    size2.height = i3;
                }
                ArrayList<TLRPC$DocumentAttribute> arrayList = document.attributes;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = arrayList.get(i4);
                    if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                        Size size3 = this.size;
                        size3.width = tLRPC$DocumentAttribute.w;
                        size3.height = tLRPC$DocumentAttribute.h;
                        break;
                    }
                }
            }
            return this.size;
        }

        @Override // org.telegram.ui.Components.ExtendedGridLayoutManager
        public int getFlowItemCount() {
            if (this.val$mediaPage.listView.getAdapter() != SharedMediaLayout.this.gifAdapter) {
                return 0;
            }
            return getItemCount();
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfoForItem(recycler, state, view, accessibilityNodeInfoCompat);
            AccessibilityNodeInfoCompat.CollectionItemInfoCompat collectionItemInfo = accessibilityNodeInfoCompat.getCollectionItemInfo();
            if (collectionItemInfo == null || !collectionItemInfo.isHeading()) {
                return;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(collectionItemInfo.getRowIndex(), collectionItemInfo.getRowSpan(), collectionItemInfo.getColumnIndex(), collectionItemInfo.getColumnSpan(), false));
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends GridLayoutManager.SpanSizeLookup {
        final /* synthetic */ MediaPage val$mediaPage;

        AnonymousClass9(MediaPage mediaPage) {
            SharedMediaLayout.this = r1;
            this.val$mediaPage = mediaPage;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            if (this.val$mediaPage.listView.getAdapter() == SharedMediaLayout.this.photoVideoAdapter) {
                if (SharedMediaLayout.this.photoVideoAdapter.getItemViewType(i) != 2) {
                    return 1;
                }
                return SharedMediaLayout.this.mediaColumnsCount;
            } else if (this.val$mediaPage.listView.getAdapter() != SharedMediaLayout.this.gifAdapter) {
                return this.val$mediaPage.layoutManager.getSpanCount();
            } else {
                return (this.val$mediaPage.listView.getAdapter() != SharedMediaLayout.this.gifAdapter || !SharedMediaLayout.this.sharedMediaData[5].messages.isEmpty()) ? this.val$mediaPage.layoutManager.getSpanSizeForItem(i) : this.val$mediaPage.layoutManager.getSpanCount();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends BlurredRecyclerView {
        final /* synthetic */ ExtendedGridLayoutManager val$layoutManager;
        final /* synthetic */ MediaPage val$mediaPage;
        HashSet<SharedPhotoVideoCell2> excludeDrawViews = new HashSet<>();
        ArrayList<SharedPhotoVideoCell2> drawingViews = new ArrayList<>();
        ArrayList<SharedPhotoVideoCell2> drawingViews2 = new ArrayList<>();
        ArrayList<SharedPhotoVideoCell2> drawingViews3 = new ArrayList<>();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(Context context, MediaPage mediaPage, ExtendedGridLayoutManager extendedGridLayoutManager) {
            super(context);
            SharedMediaLayout.this = r1;
            this.val$mediaPage = mediaPage;
            this.val$layoutManager = extendedGridLayoutManager;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
            MediaPage mediaPage = this.val$mediaPage;
            sharedMediaLayout.checkLoadMoreScroll(mediaPage, mediaPage.listView, this.val$layoutManager);
            if (this.val$mediaPage.selectedType == 0) {
                PhotoViewer.getInstance().checkCurrentImageVisibility();
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:128:0x0434  */
        /* JADX WARN: Removed duplicated region for block: B:181:0x07db  */
        /* JADX WARN: Removed duplicated region for block: B:182:0x07e1  */
        /* JADX WARN: Removed duplicated region for block: B:210:0x047c A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:59:0x01a2  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x0218  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x021b  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x022e  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x0231  */
        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void dispatchDraw(Canvas canvas) {
            float f;
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            float f2;
            float f3;
            int i6;
            boolean z;
            float f4;
            int i7;
            int i8;
            int i9;
            RecyclerListView.FastScroll fastScroll;
            SharedMediaLayout sharedMediaLayout;
            RecyclerView.Adapter adapter = getAdapter();
            SharedPhotoVideoAdapter sharedPhotoVideoAdapter = SharedMediaLayout.this.photoVideoAdapter;
            float f5 = 1.0f;
            Float valueOf = Float.valueOf(1.0f);
            if (adapter == sharedPhotoVideoAdapter) {
                float measuredHeight = getMeasuredHeight();
                boolean z2 = true;
                if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                    int i10 = -1;
                    int i11 = -1;
                    for (int i12 = 0; i12 < this.val$mediaPage.listView.getChildCount(); i12++) {
                        int childAdapterPosition = this.val$mediaPage.listView.getChildAdapterPosition(this.val$mediaPage.listView.getChildAt(i12));
                        if (childAdapterPosition >= 0 && (childAdapterPosition > i11 || i11 == -1)) {
                            i11 = childAdapterPosition;
                        }
                        if (childAdapterPosition >= 0 && (childAdapterPosition < i10 || i10 == -1)) {
                            i10 = childAdapterPosition;
                        }
                    }
                    int i13 = -1;
                    int i14 = -1;
                    for (int i15 = 0; i15 < this.val$mediaPage.animationSupportingListView.getChildCount(); i15++) {
                        int childAdapterPosition2 = this.val$mediaPage.animationSupportingListView.getChildAdapterPosition(this.val$mediaPage.animationSupportingListView.getChildAt(i15));
                        if (childAdapterPosition2 >= 0 && (childAdapterPosition2 > i14 || i14 == -1)) {
                            i14 = childAdapterPosition2;
                        }
                        if (childAdapterPosition2 >= 0 && (childAdapterPosition2 < i13 || i13 == -1)) {
                            i13 = childAdapterPosition2;
                        }
                    }
                    if (i10 >= 0 && i13 >= 0) {
                        if (SharedMediaLayout.this.pinchCenterPosition >= 0) {
                            int ceil = (int) Math.ceil(sharedMediaLayout.photoVideoAdapter.getItemCount() / SharedMediaLayout.this.mediaColumnsCount);
                            int ceil2 = (int) Math.ceil(SharedMediaLayout.this.photoVideoAdapter.getItemCount() / SharedMediaLayout.this.animateToColumnsCount);
                            SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
                            int i16 = (sharedMediaLayout2.pinchCenterPosition / sharedMediaLayout2.animateToColumnsCount) - (i13 / SharedMediaLayout.this.animateToColumnsCount);
                            SharedMediaLayout sharedMediaLayout3 = SharedMediaLayout.this;
                            i7 = i16 - ((sharedMediaLayout3.pinchCenterPosition / sharedMediaLayout3.mediaColumnsCount) - (i10 / SharedMediaLayout.this.mediaColumnsCount));
                            if (((i10 / SharedMediaLayout.this.mediaColumnsCount) - i7 < 0 && SharedMediaLayout.this.animateToColumnsCount < SharedMediaLayout.this.mediaColumnsCount) || ((i13 / SharedMediaLayout.this.animateToColumnsCount) + i7 < 0 && SharedMediaLayout.this.animateToColumnsCount > SharedMediaLayout.this.mediaColumnsCount)) {
                                i7 = 0;
                            }
                            if (((i14 / SharedMediaLayout.this.mediaColumnsCount) + i7 >= ceil && SharedMediaLayout.this.animateToColumnsCount > SharedMediaLayout.this.mediaColumnsCount) || ((i11 / SharedMediaLayout.this.animateToColumnsCount) - i7 >= ceil2 && SharedMediaLayout.this.animateToColumnsCount < SharedMediaLayout.this.mediaColumnsCount)) {
                                i7 = 0;
                            }
                            SharedMediaLayout sharedMediaLayout4 = SharedMediaLayout.this;
                            i8 = (int) ((SharedMediaLayout.this.animateToColumnsCount - SharedMediaLayout.this.mediaColumnsCount) * ((sharedMediaLayout4.pinchCenterPosition % sharedMediaLayout4.mediaColumnsCount) / (SharedMediaLayout.this.mediaColumnsCount - 1)));
                            SharedMediaLayout.this.animationSupportingSortedCells.clear();
                            this.excludeDrawViews.clear();
                            this.drawingViews.clear();
                            this.drawingViews2.clear();
                            this.drawingViews3.clear();
                            for (i9 = 0; i9 < this.val$mediaPage.animationSupportingListView.getChildCount(); i9++) {
                                View childAt = this.val$mediaPage.animationSupportingListView.getChildAt(i9);
                                if (childAt.getTop() <= getMeasuredHeight() && childAt.getBottom() >= 0 && (childAt instanceof SharedPhotoVideoCell2)) {
                                    SharedMediaLayout.this.animationSupportingSortedCells.add((SharedPhotoVideoCell2) childAt);
                                }
                            }
                            this.drawingViews.addAll(SharedMediaLayout.this.animationSupportingSortedCells);
                            fastScroll = getFastScroll();
                            if (fastScroll != null && fastScroll.getTag() != null) {
                                float scrollProgress = SharedMediaLayout.this.photoVideoAdapter.getScrollProgress(this.val$mediaPage.listView);
                                float scrollProgress2 = SharedMediaLayout.this.animationSupportingPhotoVideoAdapter.getScrollProgress(this.val$mediaPage.animationSupportingListView);
                                float f6 = !SharedMediaLayout.this.photoVideoAdapter.fastScrollIsVisible(this.val$mediaPage.listView) ? 1.0f : 0.0f;
                                float f7 = !SharedMediaLayout.this.animationSupportingPhotoVideoAdapter.fastScrollIsVisible(this.val$mediaPage.animationSupportingListView) ? 1.0f : 0.0f;
                                fastScroll.setProgress((scrollProgress * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (scrollProgress2 * SharedMediaLayout.this.photoVideoChangeColumnsProgress));
                                fastScroll.setVisibilityAlpha((f6 * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (f7 * SharedMediaLayout.this.photoVideoChangeColumnsProgress));
                            }
                            i3 = i10;
                            i2 = i13;
                            i = i7;
                            i4 = i8;
                        }
                    }
                    i8 = 0;
                    i7 = 0;
                    SharedMediaLayout.this.animationSupportingSortedCells.clear();
                    this.excludeDrawViews.clear();
                    this.drawingViews.clear();
                    this.drawingViews2.clear();
                    this.drawingViews3.clear();
                    while (i9 < this.val$mediaPage.animationSupportingListView.getChildCount()) {
                    }
                    this.drawingViews.addAll(SharedMediaLayout.this.animationSupportingSortedCells);
                    fastScroll = getFastScroll();
                    if (fastScroll != null) {
                        float scrollProgress3 = SharedMediaLayout.this.photoVideoAdapter.getScrollProgress(this.val$mediaPage.listView);
                        float scrollProgress22 = SharedMediaLayout.this.animationSupportingPhotoVideoAdapter.getScrollProgress(this.val$mediaPage.animationSupportingListView);
                        if (!SharedMediaLayout.this.photoVideoAdapter.fastScrollIsVisible(this.val$mediaPage.listView)) {
                        }
                        if (!SharedMediaLayout.this.animationSupportingPhotoVideoAdapter.fastScrollIsVisible(this.val$mediaPage.animationSupportingListView)) {
                        }
                        fastScroll.setProgress((scrollProgress3 * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (scrollProgress22 * SharedMediaLayout.this.photoVideoChangeColumnsProgress));
                        fastScroll.setVisibilityAlpha((f6 * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (f7 * SharedMediaLayout.this.photoVideoChangeColumnsProgress));
                    }
                    i3 = i10;
                    i2 = i13;
                    i = i7;
                    i4 = i8;
                } else {
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                    i = 0;
                }
                float f8 = measuredHeight;
                int i17 = 0;
                while (i17 < getChildCount()) {
                    View childAt2 = getChildAt(i17);
                    if (childAt2.getTop() > getMeasuredHeight() || childAt2.getBottom() < 0) {
                        if (childAt2 instanceof SharedPhotoVideoCell2) {
                            SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) getChildAt(i17);
                            sharedPhotoVideoCell2.setCrossfadeView(null, 0.0f, 0);
                            sharedPhotoVideoCell2.setTranslationX(0.0f);
                            sharedPhotoVideoCell2.setTranslationY(0.0f);
                            sharedPhotoVideoCell2.setImageScale(1.0f, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                        }
                    } else if (childAt2 instanceof SharedPhotoVideoCell2) {
                        SharedPhotoVideoCell2 sharedPhotoVideoCell22 = (SharedPhotoVideoCell2) getChildAt(i17);
                        if (sharedPhotoVideoCell22.getMessageId() == this.val$mediaPage.highlightMessageId && sharedPhotoVideoCell22.imageReceiver.hasBitmapImage()) {
                            MediaPage mediaPage = this.val$mediaPage;
                            if (!mediaPage.highlightAnimation) {
                                mediaPage.highlightProgress = 0.0f;
                                mediaPage.highlightAnimation = true;
                            }
                            float f9 = mediaPage.highlightProgress;
                            if (f9 >= 0.3f) {
                                if (f9 > 0.7f) {
                                    f9 = f5 - f9;
                                } else {
                                    f4 = 1.0f;
                                    sharedPhotoVideoCell22.setHighlightProgress(f4);
                                }
                            }
                            f4 = f9 / 0.3f;
                            sharedPhotoVideoCell22.setHighlightProgress(f4);
                        } else {
                            sharedPhotoVideoCell22.setHighlightProgress(0.0f);
                        }
                        MessageObject messageObject = sharedPhotoVideoCell22.getMessageObject();
                        sharedPhotoVideoCell22.setImageAlpha((messageObject == null || SharedMediaLayout.this.messageAlphaEnter.get(messageObject.getId(), null) == null) ? 1.0f : SharedMediaLayout.this.messageAlphaEnter.get(messageObject.getId(), valueOf).floatValue(), !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                        if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                            int viewAdapterPosition = (((GridLayoutManager.LayoutParams) sharedPhotoVideoCell22.getLayoutParams()).getViewAdapterPosition() % SharedMediaLayout.this.mediaColumnsCount) + i4;
                            int viewAdapterPosition2 = ((((((GridLayoutManager.LayoutParams) sharedPhotoVideoCell22.getLayoutParams()).getViewAdapterPosition() - i3) / SharedMediaLayout.this.mediaColumnsCount) + i) * SharedMediaLayout.this.animateToColumnsCount) + viewAdapterPosition;
                            if (viewAdapterPosition >= 0 && viewAdapterPosition < SharedMediaLayout.this.animateToColumnsCount && viewAdapterPosition2 >= 0 && viewAdapterPosition2 < SharedMediaLayout.this.animationSupportingSortedCells.size()) {
                                float measuredWidth = ((f5 - SharedMediaLayout.this.photoVideoChangeColumnsProgress) * f5) + (((((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2)).getMeasuredWidth() - AndroidUtilities.dpf2(2.0f)) / (sharedPhotoVideoCell22.getMeasuredWidth() - AndroidUtilities.dpf2(2.0f))) * SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                float left = ((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2)).getLeft();
                                float top = ((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2)).getTop();
                                sharedPhotoVideoCell22.setPivotX(0.0f);
                                sharedPhotoVideoCell22.setPivotY(0.0f);
                                sharedPhotoVideoCell22.setImageScale(measuredWidth, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                                sharedPhotoVideoCell22.setTranslationX((left - sharedPhotoVideoCell22.getLeft()) * SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                sharedPhotoVideoCell22.setTranslationY((top - sharedPhotoVideoCell22.getTop()) * SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                sharedPhotoVideoCell22.setCrossfadeView((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2), SharedMediaLayout.this.photoVideoChangeColumnsProgress, SharedMediaLayout.this.animateToColumnsCount);
                                this.excludeDrawViews.add((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2));
                                this.drawingViews3.add(sharedPhotoVideoCell22);
                                canvas.save();
                                canvas.translate(sharedPhotoVideoCell22.getX(), sharedPhotoVideoCell22.getY());
                                sharedPhotoVideoCell22.draw(canvas);
                                canvas.restore();
                                if (sharedPhotoVideoCell22.getY() < f8) {
                                    f8 = sharedPhotoVideoCell22.getY();
                                }
                                z = true;
                                if (!z) {
                                    if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                                        this.drawingViews2.add(sharedPhotoVideoCell22);
                                    }
                                    sharedPhotoVideoCell22.setCrossfadeView(null, 0.0f, 0);
                                    sharedPhotoVideoCell22.setTranslationX(0.0f);
                                    sharedPhotoVideoCell22.setTranslationY(0.0f);
                                    sharedPhotoVideoCell22.setImageScale(1.0f, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                                }
                            }
                        }
                        z = false;
                        if (!z) {
                        }
                    }
                    i17++;
                    f5 = 1.0f;
                }
                float f10 = 255.0f;
                if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation && !this.drawingViews.isEmpty()) {
                    float f11 = ((SharedMediaLayout.this.animateToColumnsCount / SharedMediaLayout.this.mediaColumnsCount) * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + SharedMediaLayout.this.photoVideoChangeColumnsProgress;
                    float measuredWidth2 = ((((getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount) - AndroidUtilities.dpf2(2.0f)) / ((getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount) - AndroidUtilities.dpf2(2.0f))) * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + SharedMediaLayout.this.photoVideoChangeColumnsProgress;
                    float measuredWidth3 = getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount;
                    float measuredWidth4 = getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount;
                    double ceil3 = Math.ceil(getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount);
                    double dpf2 = AndroidUtilities.dpf2(2.0f);
                    Double.isNaN(dpf2);
                    double d = ceil3 - dpf2;
                    double d2 = measuredWidth2;
                    Double.isNaN(d2);
                    double d3 = d * d2;
                    double dpf22 = AndroidUtilities.dpf2(2.0f);
                    Double.isNaN(dpf22);
                    float f12 = (float) (d3 + dpf22);
                    int i18 = 0;
                    while (i18 < this.drawingViews.size()) {
                        SharedPhotoVideoCell2 sharedPhotoVideoCell23 = this.drawingViews.get(i18);
                        if (this.excludeDrawViews.contains(sharedPhotoVideoCell23)) {
                            i6 = i18;
                            f3 = f12;
                            f2 = measuredWidth2;
                        } else {
                            sharedPhotoVideoCell23.setCrossfadeView(null, 0.0f, 0);
                            int viewAdapterPosition3 = ((GridLayoutManager.LayoutParams) sharedPhotoVideoCell23.getLayoutParams()).getViewAdapterPosition() % SharedMediaLayout.this.animateToColumnsCount;
                            int i19 = viewAdapterPosition3 - i4;
                            canvas.save();
                            canvas.translate((i19 * measuredWidth3 * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (viewAdapterPosition3 * measuredWidth4 * SharedMediaLayout.this.photoVideoChangeColumnsProgress), f8 + ((((((GridLayoutManager.LayoutParams) sharedPhotoVideoCell23.getLayoutParams()).getViewAdapterPosition() - i2) / SharedMediaLayout.this.animateToColumnsCount) - i) * f12));
                            sharedPhotoVideoCell23.setImageScale(measuredWidth2, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                            if (i19 < SharedMediaLayout.this.mediaColumnsCount) {
                                i6 = i18;
                                f3 = f12;
                                f2 = measuredWidth2;
                                canvas.saveLayerAlpha(0.0f, 0.0f, sharedPhotoVideoCell23.getMeasuredWidth() * f11, sharedPhotoVideoCell23.getMeasuredWidth() * f11, (int) (SharedMediaLayout.this.photoVideoChangeColumnsProgress * f10), 31);
                                sharedPhotoVideoCell23.draw(canvas);
                                canvas.restore();
                            } else {
                                i6 = i18;
                                f3 = f12;
                                f2 = measuredWidth2;
                                sharedPhotoVideoCell23.draw(canvas);
                            }
                            canvas.restore();
                        }
                        i18 = i6 + 1;
                        f12 = f3;
                        measuredWidth2 = f2;
                        f10 = 255.0f;
                    }
                }
                super.dispatchDraw(canvas);
                if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                    float f13 = ((SharedMediaLayout.this.mediaColumnsCount / SharedMediaLayout.this.animateToColumnsCount) * SharedMediaLayout.this.photoVideoChangeColumnsProgress) + (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                    float measuredWidth5 = (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress) + ((((getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount) - AndroidUtilities.dpf2(2.0f)) / ((getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount) - AndroidUtilities.dpf2(2.0f))) * SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                    double ceil4 = Math.ceil(getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount);
                    double dpf23 = AndroidUtilities.dpf2(2.0f);
                    Double.isNaN(dpf23);
                    double d4 = ceil4 - dpf23;
                    double d5 = measuredWidth5;
                    Double.isNaN(d5);
                    double d6 = d4 * d5;
                    double dpf24 = AndroidUtilities.dpf2(2.0f);
                    Double.isNaN(dpf24);
                    float f14 = (float) (d6 + dpf24);
                    float measuredWidth6 = getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount;
                    float measuredWidth7 = getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount;
                    int i20 = 0;
                    while (i20 < this.drawingViews2.size()) {
                        SharedPhotoVideoCell2 sharedPhotoVideoCell24 = this.drawingViews2.get(i20);
                        int viewAdapterPosition4 = ((GridLayoutManager.LayoutParams) sharedPhotoVideoCell24.getLayoutParams()).getViewAdapterPosition() % SharedMediaLayout.this.mediaColumnsCount;
                        int i21 = viewAdapterPosition4 + i4;
                        canvas.save();
                        sharedPhotoVideoCell24.setImageScale(measuredWidth5, SharedMediaLayout.this.photoVideoChangeColumnsAnimation ^ z2);
                        canvas.translate((viewAdapterPosition4 * measuredWidth6 * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (i21 * measuredWidth7 * SharedMediaLayout.this.photoVideoChangeColumnsProgress), f8 + ((((((GridLayoutManager.LayoutParams) sharedPhotoVideoCell24.getLayoutParams()).getViewAdapterPosition() - i3) / SharedMediaLayout.this.mediaColumnsCount) + i) * f14));
                        if (i21 < SharedMediaLayout.this.animateToColumnsCount) {
                            i5 = i20;
                            canvas.saveLayerAlpha(0.0f, 0.0f, sharedPhotoVideoCell24.getMeasuredWidth() * f13, sharedPhotoVideoCell24.getMeasuredWidth() * f13, (int) ((1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress) * 255.0f), 31);
                            sharedPhotoVideoCell24.draw(canvas);
                            canvas.restore();
                        } else {
                            i5 = i20;
                            sharedPhotoVideoCell24.draw(canvas);
                        }
                        canvas.restore();
                        i20 = i5 + 1;
                        z2 = true;
                    }
                    if (!this.drawingViews3.isEmpty()) {
                        canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) (SharedMediaLayout.this.photoVideoChangeColumnsProgress * 255.0f), 31);
                        for (int i22 = 0; i22 < this.drawingViews3.size(); i22++) {
                            this.drawingViews3.get(i22).drawCrossafadeImage(canvas);
                        }
                        canvas.restore();
                    }
                }
            } else {
                for (int i23 = 0; i23 < getChildCount(); i23++) {
                    View childAt3 = getChildAt(i23);
                    int messageId = SharedMediaLayout.this.getMessageId(childAt3);
                    if (messageId != 0 && SharedMediaLayout.this.messageAlphaEnter.get(messageId, null) != null) {
                        f = SharedMediaLayout.this.messageAlphaEnter.get(messageId, valueOf).floatValue();
                        if (!(childAt3 instanceof SharedDocumentCell)) {
                            ((SharedDocumentCell) childAt3).setEnterAnimationAlpha(f);
                        } else if (childAt3 instanceof SharedAudioCell) {
                            ((SharedAudioCell) childAt3).setEnterAnimationAlpha(f);
                        }
                    }
                    f = 1.0f;
                    if (!(childAt3 instanceof SharedDocumentCell)) {
                    }
                }
                super.dispatchDraw(canvas);
            }
            MediaPage mediaPage2 = this.val$mediaPage;
            if (mediaPage2.highlightAnimation) {
                float f15 = mediaPage2.highlightProgress + 0.010666667f;
                mediaPage2.highlightProgress = f15;
                if (f15 >= 1.0f) {
                    mediaPage2.highlightProgress = 0.0f;
                    mediaPage2.highlightAnimation = false;
                    mediaPage2.highlightMessageId = 0;
                }
                invalidate();
            }
        }

        @Override // org.telegram.ui.Components.BlurredRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            if (getAdapter() != SharedMediaLayout.this.photoVideoAdapter || !SharedMediaLayout.this.photoVideoChangeColumnsAnimation || !(view instanceof SharedPhotoVideoCell2)) {
                return super.drawChild(canvas, view, j);
            }
            return true;
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends GridLayoutManager {
        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass11(Context context, int i) {
            super(context, i);
            SharedMediaLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                i = 0;
            }
            return super.scrollVerticallyBy(i, recycler, state);
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends RecyclerView.ItemDecoration {
        final /* synthetic */ MediaPage val$mediaPage;

        AnonymousClass12(MediaPage mediaPage) {
            SharedMediaLayout.this = r1;
            this.val$mediaPage = mediaPage;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int i = 0;
            if (this.val$mediaPage.listView.getAdapter() == SharedMediaLayout.this.gifAdapter) {
                int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                rect.left = 0;
                rect.bottom = 0;
                if (!this.val$mediaPage.layoutManager.isFirstRow(childAdapterPosition)) {
                    rect.top = AndroidUtilities.dp(2.0f);
                } else {
                    rect.top = 0;
                }
                if (!this.val$mediaPage.layoutManager.isLastInRow(childAdapterPosition)) {
                    i = AndroidUtilities.dp(2.0f);
                }
                rect.right = i;
                return;
            }
            rect.left = 0;
            rect.top = 0;
            rect.bottom = 0;
            rect.right = 0;
        }
    }

    public /* synthetic */ void lambda$new$6(MediaPage mediaPage, View view, int i) {
        MessageObject messageObject;
        long j;
        if (mediaPage.selectedType != 7) {
            if (mediaPage.selectedType != 6 || !(view instanceof ProfileSearchCell)) {
                if (mediaPage.selectedType != 1 || !(view instanceof SharedDocumentCell)) {
                    if (mediaPage.selectedType != 3 || !(view instanceof SharedLinkCell)) {
                        if ((mediaPage.selectedType == 2 || mediaPage.selectedType == 4) && (view instanceof SharedAudioCell)) {
                            onItemClick(i, view, ((SharedAudioCell) view).getMessage(), 0, mediaPage.selectedType);
                            return;
                        } else if (mediaPage.selectedType != 5 || !(view instanceof ContextLinkCell)) {
                            if (mediaPage.selectedType != 0 || !(view instanceof SharedPhotoVideoCell2) || (messageObject = ((SharedPhotoVideoCell2) view).getMessageObject()) == null) {
                                return;
                            }
                            onItemClick(i, view, messageObject, 0, mediaPage.selectedType);
                            return;
                        } else {
                            onItemClick(i, view, (MessageObject) ((ContextLinkCell) view).getParentObject(), 0, mediaPage.selectedType);
                            return;
                        }
                    }
                    onItemClick(i, view, ((SharedLinkCell) view).getMessage(), 0, mediaPage.selectedType);
                    return;
                }
                onItemClick(i, view, ((SharedDocumentCell) view).getMessage(), 0, mediaPage.selectedType);
                return;
            }
            TLRPC$Chat chat = ((ProfileSearchCell) view).getChat();
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", chat.id);
            if (!this.profileActivity.getMessagesController().checkCanOpenChat(bundle, this.profileActivity)) {
                return;
            }
            this.profileActivity.presentFragment(new ChatActivity(bundle));
        } else if (!(view instanceof UserCell)) {
            RecyclerView.Adapter adapter = mediaPage.listView.getAdapter();
            GroupUsersSearchAdapter groupUsersSearchAdapter = this.groupUsersSearchAdapter;
            if (adapter != groupUsersSearchAdapter) {
                return;
            }
            TLObject item = groupUsersSearchAdapter.getItem(i);
            if (item instanceof TLRPC$ChannelParticipant) {
                j = MessageObject.getPeerId(((TLRPC$ChannelParticipant) item).peer);
            } else if (!(item instanceof TLRPC$ChatParticipant)) {
                return;
            } else {
                j = ((TLRPC$ChatParticipant) item).user_id;
            }
            if (j == 0 || j == this.profileActivity.getUserConfig().getClientUserId()) {
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putLong("user_id", j);
            this.profileActivity.presentFragment(new ProfileActivity(bundle2));
        } else {
            onMemberClick(!this.chatUsersAdapter.sortedUsers.isEmpty() ? this.chatUsersAdapter.chatInfo.participants.participants.get(((Integer) this.chatUsersAdapter.sortedUsers.get(i)).intValue()) : this.chatUsersAdapter.chatInfo.participants.participants.get(i), false);
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends RecyclerView.OnScrollListener {
        final /* synthetic */ ExtendedGridLayoutManager val$layoutManager;
        final /* synthetic */ MediaPage val$mediaPage;

        AnonymousClass13(MediaPage mediaPage, ExtendedGridLayoutManager extendedGridLayoutManager) {
            SharedMediaLayout.this = r1;
            this.val$mediaPage = mediaPage;
            this.val$layoutManager = extendedGridLayoutManager;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            SharedMediaLayout.this.scrolling = i != 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            SharedMediaLayout.this.checkLoadMoreScroll(this.val$mediaPage, (RecyclerListView) recyclerView, this.val$layoutManager);
            if (i2 != 0 && ((SharedMediaLayout.this.mediaPages[0].selectedType == 0 || SharedMediaLayout.this.mediaPages[0].selectedType == 5) && !SharedMediaLayout.this.sharedMediaData[0].messages.isEmpty())) {
                SharedMediaLayout.this.showFloatingDateView();
            }
            if (i2 != 0 && this.val$mediaPage.selectedType == 0) {
                SharedMediaLayout.showFastScrollHint(this.val$mediaPage, SharedMediaLayout.this.sharedMediaData, true);
            }
            this.val$mediaPage.listView.checkSection(true);
            MediaPage mediaPage = this.val$mediaPage;
            if (mediaPage.fastScrollHintView != null) {
                mediaPage.invalidate();
            }
            SharedMediaLayout.this.invalidateBlur();
        }
    }

    public /* synthetic */ boolean lambda$new$7(MediaPage mediaPage, View view, int i) {
        MessageObject messageObject;
        if (this.photoVideoChangeColumnsAnimation) {
            return false;
        }
        if (this.isActionModeShowed) {
            mediaPage.listView.getOnItemClickListener().onItemClick(view, i);
            return true;
        } else if (mediaPage.selectedType != 7 || !(view instanceof UserCell)) {
            if (mediaPage.selectedType != 1 || !(view instanceof SharedDocumentCell)) {
                if (mediaPage.selectedType != 3 || !(view instanceof SharedLinkCell)) {
                    if ((mediaPage.selectedType != 2 && mediaPage.selectedType != 4) || !(view instanceof SharedAudioCell)) {
                        if (mediaPage.selectedType != 5 || !(view instanceof ContextLinkCell)) {
                            if (mediaPage.selectedType == 0 && (view instanceof SharedPhotoVideoCell2) && (messageObject = ((SharedPhotoVideoCell2) view).getMessageObject()) != null) {
                                return onItemLongClick(messageObject, view, 0);
                            }
                            return false;
                        }
                        return onItemLongClick((MessageObject) ((ContextLinkCell) view).getParentObject(), view, 0);
                    }
                    return onItemLongClick(((SharedAudioCell) view).getMessage(), view, 0);
                }
                return onItemLongClick(((SharedLinkCell) view).getMessage(), view, 0);
            }
            return onItemLongClick(((SharedDocumentCell) view).getMessage(), view, 0);
        } else {
            return onMemberClick(!this.chatUsersAdapter.sortedUsers.isEmpty() ? this.chatUsersAdapter.chatInfo.participants.participants.get(((Integer) this.chatUsersAdapter.sortedUsers.get(i)).intValue()) : this.chatUsersAdapter.chatInfo.participants.participants.get(i), true);
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 extends ClippingImageView {
        final /* synthetic */ RecyclerListView val$listView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass14(SharedMediaLayout sharedMediaLayout, Context context, RecyclerListView recyclerListView) {
            super(context);
            this.val$listView = recyclerListView;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.val$listView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 extends FlickerLoadingView {
        final /* synthetic */ MediaPage val$mediaPage;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass15(Context context, MediaPage mediaPage) {
            super(context);
            SharedMediaLayout.this = r1;
            this.val$mediaPage = mediaPage;
        }

        @Override // org.telegram.ui.Components.FlickerLoadingView
        public int getColumnsCount() {
            return SharedMediaLayout.this.mediaColumnsCount;
        }

        @Override // org.telegram.ui.Components.FlickerLoadingView
        public int getViewType() {
            setIsSingleCell(false);
            if (this.val$mediaPage.selectedType == 0 || this.val$mediaPage.selectedType == 5) {
                return 2;
            }
            if (this.val$mediaPage.selectedType == 1) {
                return 3;
            }
            if (this.val$mediaPage.selectedType == 2 || this.val$mediaPage.selectedType == 4) {
                return 4;
            }
            if (this.val$mediaPage.selectedType == 3) {
                return 5;
            }
            if (this.val$mediaPage.selectedType == 7) {
                return 6;
            }
            if (this.val$mediaPage.selectedType == 6 && SharedMediaLayout.this.scrollSlidingTextTabStrip.getTabsCount() == 1) {
                setIsSingleCell(true);
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.FlickerLoadingView, android.view.View
        public void onDraw(Canvas canvas) {
            SharedMediaLayout.this.backgroundPaint.setColor(SharedMediaLayout.this.getThemedColor("windowBackgroundWhite"));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), SharedMediaLayout.this.backgroundPaint);
            super.onDraw(canvas);
        }
    }

    public /* synthetic */ void lambda$new$9(boolean z, boolean z2) {
        if (!z) {
            requestLayout();
        }
    }

    public void setForwardRestrictedHint(HintView hintView) {
        this.fwdRestrictedHint = hintView;
    }

    public int getMessageId(View view) {
        if (view instanceof SharedPhotoVideoCell2) {
            return ((SharedPhotoVideoCell2) view).getMessageId();
        }
        if (view instanceof SharedDocumentCell) {
            return ((SharedDocumentCell) view).getMessage().getId();
        }
        if (!(view instanceof SharedAudioCell)) {
            return 0;
        }
        return ((SharedAudioCell) view).getMessage().getId();
    }

    private void updateForwardItem() {
        if (this.forwardItem == null) {
            return;
        }
        boolean z = this.profileActivity.getMessagesController().isChatNoForwards(-this.dialog_id) || hasNoforwardsMessage();
        this.forwardItem.setAlpha(z ? 0.5f : 1.0f);
        if (z && this.forwardItem.getBackground() != null) {
            this.forwardItem.setBackground(null);
        } else if (z || this.forwardItem.getBackground() != null) {
        } else {
            this.forwardItem.setBackground(Theme.createSelectorDrawable(getThemedColor("actionBarActionModeDefaultSelector"), 5));
        }
    }

    private boolean hasNoforwardsMessage() {
        MessageObject messageObject;
        TLRPC$Message tLRPC$Message;
        boolean z = false;
        for (int i = 1; i >= 0; i--) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < this.selectedFiles[i].size(); i2++) {
                arrayList.add(Integer.valueOf(this.selectedFiles[i].keyAt(i2)));
            }
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Integer num = (Integer) it.next();
                if (num.intValue() > 0 && (messageObject = this.selectedFiles[i].get(num.intValue())) != null && (tLRPC$Message = messageObject.messageOwner) != null && tLRPC$Message.noforwards) {
                    z = true;
                    break;
                }
            }
            if (z) {
                break;
            }
        }
        return z;
    }

    public void changeMediaFilterType() {
        MediaPage mediaPage = getMediaPage(0);
        if (mediaPage != null && mediaPage.getMeasuredHeight() > 0 && mediaPage.getMeasuredWidth() > 0) {
            Bitmap bitmap = null;
            try {
                bitmap = Bitmap.createBitmap(mediaPage.getMeasuredWidth(), mediaPage.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (bitmap != null) {
                this.changeTypeAnimation = true;
                mediaPage.listView.draw(new Canvas(bitmap));
                View view = new View(mediaPage.getContext());
                view.setBackground(new BitmapDrawable(bitmap));
                mediaPage.addView(view);
                view.animate().alpha(0.0f).setDuration(200L).setListener(new AnonymousClass16(view, mediaPage, bitmap)).start();
                mediaPage.listView.setAlpha(0.0f);
                mediaPage.listView.animate().alpha(1.0f).setDuration(200L).start();
            }
        }
        int[] lastMediaCount = this.sharedMediaPreloader.getLastMediaCount();
        ArrayList<MessageObject> arrayList = this.sharedMediaPreloader.getSharedMediaData()[0].messages;
        SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
        if (sharedMediaDataArr[0].filterType == 0) {
            sharedMediaDataArr[0].setTotalCount(lastMediaCount[0]);
        } else if (sharedMediaDataArr[0].filterType == 1) {
            sharedMediaDataArr[0].setTotalCount(lastMediaCount[6]);
        } else {
            sharedMediaDataArr[0].setTotalCount(lastMediaCount[7]);
        }
        this.sharedMediaData[0].fastScrollDataLoaded = false;
        jumpToDate(0, DialogObject.isEncryptedDialog(this.dialog_id) ? Integer.MIN_VALUE : Integer.MAX_VALUE, 0, true);
        loadFastScrollData(false);
        this.delegate.updateSelectedMediaTabText();
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(this.dialog_id);
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = arrayList.get(i);
            SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
            if (sharedMediaDataArr2[0].filterType == 0) {
                sharedMediaDataArr2[0].addMessage(messageObject, 0, false, isEncryptedDialog);
            } else if (sharedMediaDataArr2[0].filterType == 1) {
                if (messageObject.isPhoto()) {
                    this.sharedMediaData[0].addMessage(messageObject, 0, false, isEncryptedDialog);
                }
            } else if (!messageObject.isPhoto()) {
                this.sharedMediaData[0].addMessage(messageObject, 0, false, isEncryptedDialog);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends AnimatorListenerAdapter {
        final /* synthetic */ Bitmap val$finalBitmap;
        final /* synthetic */ MediaPage val$mediaPage;
        final /* synthetic */ View val$view;

        AnonymousClass16(View view, MediaPage mediaPage, Bitmap bitmap) {
            SharedMediaLayout.this = r1;
            this.val$view = view;
            this.val$mediaPage = mediaPage;
            this.val$finalBitmap = bitmap;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            SharedMediaLayout.this.changeTypeAnimation = false;
            if (this.val$view.getParent() != null) {
                this.val$mediaPage.removeView(this.val$view);
                this.val$finalBitmap.recycle();
            }
        }
    }

    public MediaPage getMediaPage(int i) {
        int i2 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i2 < mediaPageArr.length) {
                if (mediaPageArr[i2].selectedType == 0) {
                    return this.mediaPages[i2];
                }
                i2++;
            } else {
                return null;
            }
        }
    }

    public void showMediaCalendar(boolean z) {
        int i;
        MediaPage mediaPage;
        if (!z || getY() == 0.0f || this.viewType != 1) {
            Bundle bundle = new Bundle();
            bundle.putLong("dialog_id", this.dialog_id);
            if (z && (mediaPage = getMediaPage(0)) != null) {
                ArrayList<Period> arrayList = this.sharedMediaData[0].fastScrollPeriods;
                Period period = null;
                int findFirstVisibleItemPosition = mediaPage.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition >= 0) {
                    if (arrayList != null) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= arrayList.size()) {
                                break;
                            } else if (findFirstVisibleItemPosition <= arrayList.get(i2).startOffset) {
                                period = arrayList.get(i2);
                                break;
                            } else {
                                i2++;
                            }
                        }
                        if (period == null) {
                            period = arrayList.get(arrayList.size() - 1);
                        }
                    }
                    if (period != null) {
                        i = period.date;
                        bundle.putInt("type", 1);
                        CalendarActivity calendarActivity = new CalendarActivity(bundle, this.sharedMediaData[0].filterType, i);
                        calendarActivity.setCallback(new AnonymousClass17());
                        this.profileActivity.presentFragment(calendarActivity);
                    }
                }
            }
            i = 0;
            bundle.putInt("type", 1);
            CalendarActivity calendarActivity2 = new CalendarActivity(bundle, this.sharedMediaData[0].filterType, i);
            calendarActivity2.setCallback(new AnonymousClass17());
            this.profileActivity.presentFragment(calendarActivity2);
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$17 */
    /* loaded from: classes3.dex */
    public class AnonymousClass17 implements CalendarActivity.Callback {
        AnonymousClass17() {
            SharedMediaLayout.this = r1;
        }

        @Override // org.telegram.ui.CalendarActivity.Callback
        public void onDateSelected(int i, int i2) {
            int i3 = -1;
            for (int i4 = 0; i4 < SharedMediaLayout.this.sharedMediaData[0].messages.size(); i4++) {
                if (SharedMediaLayout.this.sharedMediaData[0].messages.get(i4).getId() == i) {
                    i3 = i4;
                }
            }
            MediaPage mediaPage = SharedMediaLayout.this.getMediaPage(0);
            if (i3 < 0 || mediaPage == null) {
                SharedMediaLayout.this.jumpToDate(0, i, i2, true);
            } else {
                mediaPage.layoutManager.scrollToPositionWithOffset(i3, 0);
            }
            if (mediaPage != null) {
                mediaPage.highlightMessageId = i;
                mediaPage.highlightAnimation = false;
            }
        }
    }

    private void startPinchToMediaColumnsCount(boolean z) {
        if (this.photoVideoChangeColumnsAnimation) {
            return;
        }
        MediaPage mediaPage = null;
        int i = 0;
        int i2 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i2 >= mediaPageArr.length) {
                break;
            } else if (mediaPageArr[i2].selectedType == 0) {
                mediaPage = this.mediaPages[i2];
                break;
            } else {
                i2++;
            }
        }
        if (mediaPage == null) {
            return;
        }
        int nextMediaColumnsCount = getNextMediaColumnsCount(this.mediaColumnsCount, z);
        this.animateToColumnsCount = nextMediaColumnsCount;
        if (nextMediaColumnsCount == this.mediaColumnsCount) {
            return;
        }
        mediaPage.animationSupportingListView.setVisibility(0);
        mediaPage.animationSupportingListView.setAdapter(this.animationSupportingPhotoVideoAdapter);
        mediaPage.animationSupportingLayoutManager.setSpanCount(nextMediaColumnsCount);
        AndroidUtilities.updateVisibleRows(mediaPage.listView);
        this.photoVideoChangeColumnsAnimation = true;
        this.sharedMediaData[0].setListFrozen(true);
        this.photoVideoChangeColumnsProgress = 0.0f;
        if (this.pinchCenterPosition < 0) {
            saveScrollPosition();
            return;
        }
        while (true) {
            MediaPage[] mediaPageArr2 = this.mediaPages;
            if (i >= mediaPageArr2.length) {
                return;
            }
            if (mediaPageArr2[i].selectedType == 0) {
                this.mediaPages[i].animationSupportingLayoutManager.scrollToPositionWithOffset(this.pinchCenterPosition, this.pinchCenterOffset - this.mediaPages[i].animationSupportingListView.getPaddingTop());
            }
            i++;
        }
    }

    private void finishPinchToMediaColumnsCount() {
        if (this.photoVideoChangeColumnsAnimation) {
            MediaPage mediaPage = null;
            int i = 0;
            int i2 = 0;
            while (true) {
                MediaPage[] mediaPageArr = this.mediaPages;
                if (i2 >= mediaPageArr.length) {
                    break;
                } else if (mediaPageArr[i2].selectedType == 0) {
                    mediaPage = this.mediaPages[i2];
                    break;
                } else {
                    i2++;
                }
            }
            if (mediaPage == null) {
                return;
            }
            float f = this.photoVideoChangeColumnsProgress;
            float f2 = 1.0f;
            if (f != 1.0f) {
                if (f == 0.0f) {
                    this.photoVideoChangeColumnsAnimation = false;
                    this.sharedMediaData[0].setListFrozen(false);
                    mediaPage.animationSupportingListView.setVisibility(8);
                    mediaPage.listView.invalidate();
                    return;
                }
                boolean z = f > 0.2f;
                float[] fArr = new float[2];
                fArr[0] = f;
                if (!z) {
                    f2 = 0.0f;
                }
                fArr[1] = f2;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                ofFloat.addUpdateListener(new AnonymousClass18(mediaPage));
                ofFloat.addListener(new AnonymousClass19(z, mediaPage));
                ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ofFloat.setDuration(200L);
                ofFloat.start();
                return;
            }
            int itemCount = this.photoVideoAdapter.getItemCount();
            this.photoVideoChangeColumnsAnimation = false;
            this.sharedMediaData[0].setListFrozen(false);
            mediaPage.animationSupportingListView.setVisibility(8);
            int i3 = this.animateToColumnsCount;
            this.mediaColumnsCount = i3;
            SharedConfig.setMediaColumnsCount(i3);
            mediaPage.layoutManager.setSpanCount(this.mediaColumnsCount);
            mediaPage.listView.invalidate();
            if (this.photoVideoAdapter.getItemCount() == itemCount) {
                AndroidUtilities.updateVisibleRows(mediaPage.listView);
            } else {
                this.photoVideoAdapter.notifyDataSetChanged();
            }
            if (this.pinchCenterPosition < 0) {
                saveScrollPosition();
                return;
            }
            while (true) {
                MediaPage[] mediaPageArr2 = this.mediaPages;
                if (i >= mediaPageArr2.length) {
                    return;
                }
                if (mediaPageArr2[i].selectedType == 0) {
                    View findViewByPosition = this.mediaPages[i].animationSupportingLayoutManager.findViewByPosition(this.pinchCenterPosition);
                    if (findViewByPosition != null) {
                        this.pinchCenterOffset = findViewByPosition.getTop();
                    }
                    this.mediaPages[i].layoutManager.scrollToPositionWithOffset(this.pinchCenterPosition, (-this.mediaPages[i].listView.getPaddingTop()) + this.pinchCenterOffset);
                }
                i++;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$18 */
    /* loaded from: classes3.dex */
    public class AnonymousClass18 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ MediaPage val$finalMediaPage;

        AnonymousClass18(MediaPage mediaPage) {
            SharedMediaLayout.this = r1;
            this.val$finalMediaPage = mediaPage;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SharedMediaLayout.this.photoVideoChangeColumnsProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.val$finalMediaPage.listView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$19 */
    /* loaded from: classes3.dex */
    public class AnonymousClass19 extends AnimatorListenerAdapter {
        final /* synthetic */ MediaPage val$finalMediaPage;
        final /* synthetic */ boolean val$forward;

        AnonymousClass19(boolean z, MediaPage mediaPage) {
            SharedMediaLayout.this = r1;
            this.val$forward = z;
            this.val$finalMediaPage = mediaPage;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            View findViewByPosition;
            int itemCount = SharedMediaLayout.this.photoVideoAdapter.getItemCount();
            SharedMediaLayout.this.photoVideoChangeColumnsAnimation = false;
            SharedMediaLayout.this.sharedMediaData[0].setListFrozen(false);
            if (this.val$forward) {
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                sharedMediaLayout.mediaColumnsCount = sharedMediaLayout.animateToColumnsCount;
                SharedConfig.setMediaColumnsCount(SharedMediaLayout.this.animateToColumnsCount);
                this.val$finalMediaPage.layoutManager.setSpanCount(SharedMediaLayout.this.mediaColumnsCount);
            }
            if (this.val$forward) {
                if (SharedMediaLayout.this.photoVideoAdapter.getItemCount() != itemCount) {
                    SharedMediaLayout.this.photoVideoAdapter.notifyDataSetChanged();
                } else {
                    AndroidUtilities.updateVisibleRows(this.val$finalMediaPage.listView);
                }
            }
            this.val$finalMediaPage.animationSupportingListView.setVisibility(8);
            SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
            if (sharedMediaLayout2.pinchCenterPosition >= 0) {
                for (int i = 0; i < SharedMediaLayout.this.mediaPages.length; i++) {
                    if (SharedMediaLayout.this.mediaPages[i].selectedType == 0) {
                        if (this.val$forward && (findViewByPosition = SharedMediaLayout.this.mediaPages[i].animationSupportingLayoutManager.findViewByPosition(SharedMediaLayout.this.pinchCenterPosition)) != null) {
                            SharedMediaLayout.this.pinchCenterOffset = findViewByPosition.getTop();
                        }
                        ExtendedGridLayoutManager extendedGridLayoutManager = SharedMediaLayout.this.mediaPages[i].layoutManager;
                        SharedMediaLayout sharedMediaLayout3 = SharedMediaLayout.this;
                        extendedGridLayoutManager.scrollToPositionWithOffset(sharedMediaLayout3.pinchCenterPosition, (-sharedMediaLayout3.mediaPages[i].listView.getPaddingTop()) + SharedMediaLayout.this.pinchCenterOffset);
                    }
                }
            } else {
                sharedMediaLayout2.saveScrollPosition();
            }
            super.onAnimationEnd(animator);
        }
    }

    public void animateToMediaColumnsCount(int i) {
        MediaPage mediaPage = getMediaPage(0);
        this.pinchCenterPosition = -1;
        if (mediaPage != null) {
            mediaPage.listView.stopScroll();
            this.animateToColumnsCount = i;
            mediaPage.animationSupportingListView.setVisibility(0);
            mediaPage.animationSupportingListView.setAdapter(this.animationSupportingPhotoVideoAdapter);
            mediaPage.animationSupportingLayoutManager.setSpanCount(i);
            AndroidUtilities.updateVisibleRows(mediaPage.listView);
            this.photoVideoChangeColumnsAnimation = true;
            this.sharedMediaData[0].setListFrozen(true);
            this.photoVideoChangeColumnsProgress = 0.0f;
            saveScrollPosition();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.animationIndex = NotificationCenter.getInstance(this.profileActivity.getCurrentAccount()).setAnimationInProgress(this.animationIndex, null);
            ofFloat.addUpdateListener(new AnonymousClass20(mediaPage));
            ofFloat.addListener(new AnonymousClass21(i, mediaPage));
            ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
            ofFloat.setStartDelay(100L);
            ofFloat.setDuration(350L);
            ofFloat.start();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$20 */
    /* loaded from: classes3.dex */
    public class AnonymousClass20 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ MediaPage val$finalMediaPage;

        AnonymousClass20(MediaPage mediaPage) {
            SharedMediaLayout.this = r1;
            this.val$finalMediaPage = mediaPage;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SharedMediaLayout.this.photoVideoChangeColumnsProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.val$finalMediaPage.listView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$21 */
    /* loaded from: classes3.dex */
    public class AnonymousClass21 extends AnimatorListenerAdapter {
        final /* synthetic */ MediaPage val$finalMediaPage;
        final /* synthetic */ int val$newColumnsCount;

        AnonymousClass21(int i, MediaPage mediaPage) {
            SharedMediaLayout.this = r1;
            this.val$newColumnsCount = i;
            this.val$finalMediaPage = mediaPage;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(SharedMediaLayout.this.profileActivity.getCurrentAccount()).onAnimationFinish(SharedMediaLayout.this.animationIndex);
            int itemCount = SharedMediaLayout.this.photoVideoAdapter.getItemCount();
            SharedMediaLayout.this.photoVideoChangeColumnsAnimation = false;
            SharedMediaLayout.this.sharedMediaData[0].setListFrozen(false);
            SharedMediaLayout.this.mediaColumnsCount = this.val$newColumnsCount;
            this.val$finalMediaPage.layoutManager.setSpanCount(SharedMediaLayout.this.mediaColumnsCount);
            if (SharedMediaLayout.this.photoVideoAdapter.getItemCount() != itemCount) {
                SharedMediaLayout.this.photoVideoAdapter.notifyDataSetChanged();
            } else {
                AndroidUtilities.updateVisibleRows(this.val$finalMediaPage.listView);
            }
            this.val$finalMediaPage.animationSupportingListView.setVisibility(8);
            SharedMediaLayout.this.saveScrollPosition();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.scrollSlidingTextTabStrip != null) {
            canvas.save();
            canvas.translate(this.scrollSlidingTextTabStrip.getX(), this.scrollSlidingTextTabStrip.getY());
            this.scrollSlidingTextTabStrip.drawBackground(canvas);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
        FragmentContextView fragmentContextView = this.fragmentContextView;
        if (fragmentContextView == null || !fragmentContextView.isCallStyle()) {
            return;
        }
        canvas.save();
        canvas.translate(this.fragmentContextView.getX(), this.fragmentContextView.getY());
        this.fragmentContextView.setDrawOverlay(true);
        this.fragmentContextView.draw(canvas);
        this.fragmentContextView.setDrawOverlay(false);
        canvas.restore();
    }

    private ScrollSlidingTextTabStripInner createScrollingTextTabStrip(Context context) {
        ScrollSlidingTextTabStripInner scrollSlidingTextTabStripInner = new ScrollSlidingTextTabStripInner(context, this.resourcesProvider);
        int i = this.initialTab;
        if (i != -1) {
            scrollSlidingTextTabStripInner.setInitialTabId(i);
            this.initialTab = -1;
        }
        scrollSlidingTextTabStripInner.setBackgroundColor(getThemedColor("windowBackgroundWhite"));
        scrollSlidingTextTabStripInner.setColors("profile_tabSelectedLine", "profile_tabSelectedText", "profile_tabText", "profile_tabSelector");
        scrollSlidingTextTabStripInner.setDelegate(new AnonymousClass22());
        return scrollSlidingTextTabStripInner;
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$22 */
    /* loaded from: classes3.dex */
    public class AnonymousClass22 implements ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate {
        AnonymousClass22() {
            SharedMediaLayout.this = r1;
        }

        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
        public void onPageSelected(int i, boolean z) {
            if (SharedMediaLayout.this.mediaPages[0].selectedType == i) {
                return;
            }
            SharedMediaLayout.this.mediaPages[1].selectedType = i;
            SharedMediaLayout.this.mediaPages[1].setVisibility(0);
            SharedMediaLayout.this.hideFloatingDateView(true);
            SharedMediaLayout.this.switchToCurrentSelectedMode(true);
            SharedMediaLayout.this.animatingForward = z;
            SharedMediaLayout.this.onSelectedTabChanged();
        }

        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
        public void onSamePageSelected() {
            SharedMediaLayout.this.scrollToTop();
        }

        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
        public void onPageScrolled(float f) {
            if (f != 1.0f || SharedMediaLayout.this.mediaPages[1].getVisibility() == 0) {
                if (SharedMediaLayout.this.animatingForward) {
                    SharedMediaLayout.this.mediaPages[0].setTranslationX((-f) * SharedMediaLayout.this.mediaPages[0].getMeasuredWidth());
                    SharedMediaLayout.this.mediaPages[1].setTranslationX(SharedMediaLayout.this.mediaPages[0].getMeasuredWidth() - (SharedMediaLayout.this.mediaPages[0].getMeasuredWidth() * f));
                } else {
                    SharedMediaLayout.this.mediaPages[0].setTranslationX(SharedMediaLayout.this.mediaPages[0].getMeasuredWidth() * f);
                    SharedMediaLayout.this.mediaPages[1].setTranslationX((SharedMediaLayout.this.mediaPages[0].getMeasuredWidth() * f) - SharedMediaLayout.this.mediaPages[0].getMeasuredWidth());
                }
                float f2 = SharedMediaLayout.this.mediaPages[0].selectedType == 0 ? 1.0f - f : 0.0f;
                if (SharedMediaLayout.this.mediaPages[1].selectedType == 0) {
                    f2 = f;
                }
                SharedMediaLayout.this.photoVideoOptionsItem.setAlpha(f2);
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                sharedMediaLayout.photoVideoOptionsItem.setVisibility((f2 == 0.0f || !sharedMediaLayout.canShowSearchItem()) ? 4 : 0);
                if (SharedMediaLayout.this.canShowSearchItem()) {
                    if (SharedMediaLayout.this.searchItemState == 1) {
                        SharedMediaLayout.this.searchItem.setAlpha(f);
                    } else if (SharedMediaLayout.this.searchItemState == 2) {
                        SharedMediaLayout.this.searchItem.setAlpha(1.0f - f);
                    }
                } else {
                    SharedMediaLayout.this.searchItem.setVisibility(4);
                    SharedMediaLayout.this.searchItem.setAlpha(0.0f);
                }
                if (f != 1.0f) {
                    return;
                }
                MediaPage mediaPage = SharedMediaLayout.this.mediaPages[0];
                SharedMediaLayout.this.mediaPages[0] = SharedMediaLayout.this.mediaPages[1];
                SharedMediaLayout.this.mediaPages[1] = mediaPage;
                SharedMediaLayout.this.mediaPages[1].setVisibility(8);
                if (SharedMediaLayout.this.searchItemState == 2) {
                    SharedMediaLayout.this.searchItem.setVisibility(4);
                }
                SharedMediaLayout.this.searchItemState = 0;
                SharedMediaLayout.this.startStopVisibleGifs();
            }
        }
    }

    protected void drawBackgroundWithBlur(Canvas canvas, float f, android.graphics.Rect rect, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    private boolean fillMediaData(int i) {
        SharedMediaData[] sharedMediaData = this.sharedMediaPreloader.getSharedMediaData();
        if (sharedMediaData == null) {
            return false;
        }
        if (i == 0) {
            SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
            if (!sharedMediaDataArr[i].fastScrollDataLoaded) {
                sharedMediaDataArr[i].totalCount = sharedMediaData[i].totalCount;
            }
        } else {
            this.sharedMediaData[i].totalCount = sharedMediaData[i].totalCount;
        }
        this.sharedMediaData[i].messages.addAll(sharedMediaData[i].messages);
        this.sharedMediaData[i].sections.addAll(sharedMediaData[i].sections);
        for (Map.Entry<String, ArrayList<MessageObject>> entry : sharedMediaData[i].sectionArrays.entrySet()) {
            this.sharedMediaData[i].sectionArrays.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        for (int i2 = 0; i2 < 2; i2++) {
            this.sharedMediaData[i].messagesDict[i2] = sharedMediaData[i].messagesDict[i2].clone();
            SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
            sharedMediaDataArr2[i].max_id[i2] = sharedMediaData[i].max_id[i2];
            sharedMediaDataArr2[i].endReached[i2] = sharedMediaData[i].endReached[i2];
        }
        this.sharedMediaData[i].fastScrollPeriods.addAll(sharedMediaData[i].fastScrollPeriods);
        return !sharedMediaData[i].messages.isEmpty();
    }

    public void hideFloatingDateView(boolean z) {
        AndroidUtilities.cancelRunOnUIThread(this.hideFloatingDateRunnable);
        if (this.floatingDateView.getTag() == null) {
            return;
        }
        this.floatingDateView.setTag(null);
        AnimatorSet animatorSet = this.floatingDateAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.floatingDateAnimation = null;
        }
        if (z) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.floatingDateAnimation = animatorSet2;
            animatorSet2.setDuration(180L);
            this.floatingDateAnimation.playTogether(ObjectAnimator.ofFloat(this.floatingDateView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.floatingDateView, View.TRANSLATION_Y, (-AndroidUtilities.dp(48.0f)) + this.additionalFloatingTranslation));
            this.floatingDateAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.floatingDateAnimation.addListener(new AnonymousClass23());
            this.floatingDateAnimation.start();
            return;
        }
        this.floatingDateView.setAlpha(0.0f);
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$23 */
    /* loaded from: classes3.dex */
    public class AnonymousClass23 extends AnimatorListenerAdapter {
        AnonymousClass23() {
            SharedMediaLayout.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            SharedMediaLayout.this.floatingDateAnimation = null;
        }
    }

    public void scrollToTop() {
        int i;
        int i2 = this.mediaPages[0].selectedType;
        if (i2 == 0) {
            i = SharedPhotoVideoCell.getItemSize(1);
        } else {
            if (i2 != 1 && i2 != 2) {
                if (i2 == 3) {
                    i = AndroidUtilities.dp(100.0f);
                } else if (i2 != 4) {
                    if (i2 == 5) {
                        i = AndroidUtilities.dp(60.0f);
                    } else {
                        i = AndroidUtilities.dp(58.0f);
                    }
                }
            }
            i = AndroidUtilities.dp(56.0f);
        }
        if ((this.mediaPages[0].selectedType == 0 ? this.mediaPages[0].layoutManager.findFirstVisibleItemPosition() / this.mediaColumnsCount : this.mediaPages[0].layoutManager.findFirstVisibleItemPosition()) * i >= this.mediaPages[0].listView.getMeasuredHeight() * 1.2f) {
            this.mediaPages[0].scrollHelper.setScrollDirection(1);
            this.mediaPages[0].scrollHelper.scrollToPosition(0, 0, false, true);
            return;
        }
        this.mediaPages[0].listView.smoothScrollToPosition(0);
    }

    public void checkLoadMoreScroll(MediaPage mediaPage, RecyclerListView recyclerListView, LinearLayoutManager linearLayoutManager) {
        int i;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        int i2;
        int i3;
        if (this.photoVideoChangeColumnsAnimation || this.jumpToRunnable != null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (recyclerListView.getFastScroll() != null && recyclerListView.getFastScroll().isPressed() && currentTimeMillis - mediaPage.lastCheckScrollTime < 300) {
            return;
        }
        mediaPage.lastCheckScrollTime = currentTimeMillis;
        if ((this.searching && this.searchWas) || mediaPage.selectedType == 7) {
            return;
        }
        int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(linearLayoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
        int itemCount = recyclerListView.getAdapter().getItemCount();
        if (mediaPage.selectedType == 0 || mediaPage.selectedType == 1 || mediaPage.selectedType == 2 || mediaPage.selectedType == 4) {
            int i4 = mediaPage.selectedType;
            int startOffset = this.sharedMediaData[i4].getStartOffset() + this.sharedMediaData[i4].messages.size();
            SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
            if (sharedMediaDataArr[i4].fastScrollDataLoaded && sharedMediaDataArr[i4].fastScrollPeriods.size() > 2 && mediaPage.selectedType == 0 && this.sharedMediaData[i4].messages.size() != 0) {
                float f = i4 == 0 ? this.mediaColumnsCount : 1;
                int measuredHeight = (int) ((recyclerListView.getMeasuredHeight() / (recyclerListView.getMeasuredWidth() / f)) * f * 1.5f);
                if (measuredHeight < 100) {
                    measuredHeight = 100;
                }
                if (measuredHeight < this.sharedMediaData[i4].fastScrollPeriods.get(1).startOffset) {
                    measuredHeight = this.sharedMediaData[i4].fastScrollPeriods.get(1).startOffset;
                }
                if ((findFirstVisibleItemPosition > startOffset && findFirstVisibleItemPosition - startOffset > measuredHeight) || ((i3 = findFirstVisibleItemPosition + abs) < this.sharedMediaData[i4].startOffset && this.sharedMediaData[0].startOffset - i3 > measuredHeight)) {
                    SharedMediaLayout$$ExternalSyntheticLambda9 sharedMediaLayout$$ExternalSyntheticLambda9 = new SharedMediaLayout$$ExternalSyntheticLambda9(this, i4, recyclerListView);
                    this.jumpToRunnable = sharedMediaLayout$$ExternalSyntheticLambda9;
                    AndroidUtilities.runOnUIThread(sharedMediaLayout$$ExternalSyntheticLambda9);
                    return;
                }
            }
            itemCount = startOffset;
        }
        if (mediaPage.selectedType == 7) {
            return;
        }
        if (mediaPage.selectedType != 6) {
            if (mediaPage.selectedType == 0) {
                i = 3;
            } else {
                i = mediaPage.selectedType == 5 ? 10 : 6;
            }
            if ((abs + findFirstVisibleItemPosition > itemCount - i || this.sharedMediaData[mediaPage.selectedType].loadingAfterFastScroll) && !this.sharedMediaData[mediaPage.selectedType].loading) {
                if (mediaPage.selectedType != 0) {
                    if (mediaPage.selectedType == 1) {
                        i2 = 1;
                    } else if (mediaPage.selectedType == 2) {
                        i2 = 2;
                    } else if (mediaPage.selectedType == 4) {
                        i2 = 4;
                    } else {
                        i2 = mediaPage.selectedType == 5 ? 5 : 3;
                    }
                } else {
                    SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                    if (sharedMediaDataArr2[0].filterType == 1) {
                        i2 = 6;
                    } else {
                        i2 = sharedMediaDataArr2[0].filterType == 2 ? 7 : 0;
                    }
                }
                if (!this.sharedMediaData[mediaPage.selectedType].endReached[0]) {
                    this.sharedMediaData[mediaPage.selectedType].loading = true;
                    this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, this.sharedMediaData[mediaPage.selectedType].max_id[0], 0, i2, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[mediaPage.selectedType].requestIndex);
                } else if (this.mergeDialogId != 0 && !this.sharedMediaData[mediaPage.selectedType].endReached[1]) {
                    this.sharedMediaData[mediaPage.selectedType].loading = true;
                    this.profileActivity.getMediaDataController().loadMedia(this.mergeDialogId, 50, this.sharedMediaData[mediaPage.selectedType].max_id[1], 0, i2, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[mediaPage.selectedType].requestIndex);
                }
            }
            int i5 = this.sharedMediaData[mediaPage.selectedType].startOffset;
            if (mediaPage.selectedType == 0) {
                i5 = this.photoVideoAdapter.getPositionForIndex(0);
            }
            if (findFirstVisibleItemPosition - i5 < i + 1 && !this.sharedMediaData[mediaPage.selectedType].loading && !this.sharedMediaData[mediaPage.selectedType].startReached && !this.sharedMediaData[mediaPage.selectedType].loadingAfterFastScroll) {
                loadFromStart(mediaPage.selectedType);
            }
            if (this.mediaPages[0].listView != recyclerListView) {
                return;
            }
            if ((this.mediaPages[0].selectedType != 0 && this.mediaPages[0].selectedType != 5) || findFirstVisibleItemPosition == -1 || (findViewHolderForAdapterPosition = recyclerListView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition)) == null || findViewHolderForAdapterPosition.getItemViewType() != 0) {
                return;
            }
            View view = findViewHolderForAdapterPosition.itemView;
            if (view instanceof SharedPhotoVideoCell) {
                MessageObject messageObject = ((SharedPhotoVideoCell) view).getMessageObject(0);
                if (messageObject == null) {
                    return;
                }
                this.floatingDateView.setCustomDate(messageObject.messageOwner.date, false, true);
            } else if (!(view instanceof ContextLinkCell)) {
            } else {
                this.floatingDateView.setCustomDate(((ContextLinkCell) view).getDate(), false, true);
            }
        } else if (abs <= 0 || this.commonGroupsAdapter.endReached || this.commonGroupsAdapter.loading || this.commonGroupsAdapter.chats.isEmpty() || findFirstVisibleItemPosition + abs < itemCount - 5) {
        } else {
            CommonGroupsAdapter commonGroupsAdapter = this.commonGroupsAdapter;
            commonGroupsAdapter.getChats(((TLRPC$Chat) commonGroupsAdapter.chats.get(this.commonGroupsAdapter.chats.size() - 1)).id, 100);
        }
    }

    public /* synthetic */ void lambda$checkLoadMoreScroll$10(int i, RecyclerListView recyclerListView) {
        findPeriodAndJumpToDate(i, recyclerListView, false);
        this.jumpToRunnable = null;
    }

    private void loadFromStart(int i) {
        int i2;
        if (i == 0) {
            SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
            if (sharedMediaDataArr[0].filterType == 1) {
                i2 = 6;
            } else {
                i2 = sharedMediaDataArr[0].filterType == 2 ? 7 : 0;
            }
        } else {
            i2 = i == 1 ? 1 : i == 2 ? 2 : i == 4 ? 4 : i == 5 ? 5 : 3;
        }
        this.sharedMediaData[i].loading = true;
        this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, 0, this.sharedMediaData[i].min_id, i2, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[i].requestIndex);
    }

    public ActionBarMenuItem getSearchItem() {
        return this.searchItem;
    }

    public boolean isSearchItemVisible() {
        if (this.mediaPages[0].selectedType == 7) {
            return this.delegate.canSearchMembers();
        }
        return (this.mediaPages[0].selectedType == 0 || this.mediaPages[0].selectedType == 2 || this.mediaPages[0].selectedType == 5 || this.mediaPages[0].selectedType == 6) ? false : true;
    }

    public boolean isCalendarItemVisible() {
        return this.mediaPages[0].selectedType == 0;
    }

    public int getSelectedTab() {
        return this.scrollSlidingTextTabStrip.getCurrentTabId();
    }

    public int getClosestTab() {
        MediaPage[] mediaPageArr = this.mediaPages;
        if (mediaPageArr[1] == null || mediaPageArr[1].getVisibility() != 0 || ((!this.tabsAnimationInProgress || this.backAnimation) && Math.abs(this.mediaPages[1].getTranslationX()) >= this.mediaPages[1].getMeasuredWidth() / 2.0f)) {
            return this.scrollSlidingTextTabStrip.getCurrentTabId();
        }
        return this.mediaPages[1].selectedType;
    }

    public void onDestroy() {
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.mediaDidLoad);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.didReceiveNewMessages);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagesDeleted);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByServer);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagePlayingDidReset);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagePlayingDidStart);
    }

    private void checkCurrentTabValid() {
        if (!this.scrollSlidingTextTabStrip.hasTab(this.scrollSlidingTextTabStrip.getCurrentTabId())) {
            int firstTabId = this.scrollSlidingTextTabStrip.getFirstTabId();
            this.scrollSlidingTextTabStrip.setInitialTabId(firstTabId);
            this.mediaPages[0].selectedType = firstTabId;
            switchToCurrentSelectedMode(false);
        }
    }

    public void setNewMediaCounts(int[] iArr) {
        boolean z;
        int i = 0;
        while (true) {
            if (i >= 6) {
                z = false;
                break;
            } else if (this.hasMedia[i] >= 0) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        System.arraycopy(iArr, 0, this.hasMedia, 0, 6);
        updateTabs(true);
        if (!z && this.scrollSlidingTextTabStrip.getCurrentTabId() == 6) {
            this.scrollSlidingTextTabStrip.resetTab();
        }
        checkCurrentTabValid();
        if (this.hasMedia[0] >= 0) {
            loadFastScrollData(false);
        }
    }

    private void loadFastScrollData(boolean z) {
        int i = 0;
        while (true) {
            int[] iArr = supportedFastScrollTypes;
            if (i < iArr.length) {
                int i2 = iArr[i];
                if ((this.sharedMediaData[i2].fastScrollDataLoaded && !z) || DialogObject.isEncryptedDialog(this.dialog_id)) {
                    return;
                }
                this.sharedMediaData[i2].fastScrollDataLoaded = false;
                TLRPC$TL_messages_getSearchResultsPositions tLRPC$TL_messages_getSearchResultsPositions = new TLRPC$TL_messages_getSearchResultsPositions();
                if (i2 == 0) {
                    SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                    if (sharedMediaDataArr[i2].filterType == 1) {
                        tLRPC$TL_messages_getSearchResultsPositions.filter = new TLRPC$TL_inputMessagesFilterPhotos();
                    } else if (sharedMediaDataArr[i2].filterType == 2) {
                        tLRPC$TL_messages_getSearchResultsPositions.filter = new TLRPC$TL_inputMessagesFilterVideo();
                    } else {
                        tLRPC$TL_messages_getSearchResultsPositions.filter = new TLRPC$TL_inputMessagesFilterPhotoVideo();
                    }
                } else if (i2 == 1) {
                    tLRPC$TL_messages_getSearchResultsPositions.filter = new TLRPC$TL_inputMessagesFilterDocument();
                } else if (i2 == 2) {
                    tLRPC$TL_messages_getSearchResultsPositions.filter = new TLRPC$TL_inputMessagesFilterRoundVoice();
                } else {
                    tLRPC$TL_messages_getSearchResultsPositions.filter = new TLRPC$TL_inputMessagesFilterMusic();
                }
                tLRPC$TL_messages_getSearchResultsPositions.limit = 100;
                tLRPC$TL_messages_getSearchResultsPositions.peer = MessagesController.getInstance(this.profileActivity.getCurrentAccount()).getInputPeer(this.dialog_id);
                ConnectionsManager.getInstance(this.profileActivity.getCurrentAccount()).bindRequestToGuid(ConnectionsManager.getInstance(this.profileActivity.getCurrentAccount()).sendRequest(tLRPC$TL_messages_getSearchResultsPositions, new SharedMediaLayout$$ExternalSyntheticLambda12(this, this.sharedMediaData[i2].requestIndex, i2)), this.profileActivity.getClassGuid());
                i++;
            } else {
                return;
            }
        }
    }

    public /* synthetic */ void lambda$loadFastScrollData$13(int i, int i2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new SharedMediaLayout$$ExternalSyntheticLambda10(this, tLRPC$TL_error, i, i2, tLObject));
    }

    public /* synthetic */ void lambda$loadFastScrollData$12(TLRPC$TL_error tLRPC$TL_error, int i, int i2, TLObject tLObject) {
        if (tLRPC$TL_error != null) {
            return;
        }
        SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
        if (i != sharedMediaDataArr[i2].requestIndex) {
            return;
        }
        TLRPC$TL_messages_searchResultsPositions tLRPC$TL_messages_searchResultsPositions = (TLRPC$TL_messages_searchResultsPositions) tLObject;
        sharedMediaDataArr[i2].fastScrollPeriods.clear();
        int size = tLRPC$TL_messages_searchResultsPositions.positions.size();
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            TLRPC$TL_searchResultPosition tLRPC$TL_searchResultPosition = tLRPC$TL_messages_searchResultsPositions.positions.get(i4);
            if (tLRPC$TL_searchResultPosition.date != 0) {
                this.sharedMediaData[i2].fastScrollPeriods.add(new Period(tLRPC$TL_searchResultPosition));
            }
        }
        Collections.sort(this.sharedMediaData[i2].fastScrollPeriods, SharedMediaLayout$$ExternalSyntheticLambda11.INSTANCE);
        this.sharedMediaData[i2].setTotalCount(tLRPC$TL_messages_searchResultsPositions.count);
        SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
        sharedMediaDataArr2[i2].fastScrollDataLoaded = true;
        if (!sharedMediaDataArr2[i2].fastScrollPeriods.isEmpty()) {
            while (true) {
                MediaPage[] mediaPageArr = this.mediaPages;
                if (i3 >= mediaPageArr.length) {
                    break;
                }
                if (mediaPageArr[i3].selectedType == i2) {
                    MediaPage[] mediaPageArr2 = this.mediaPages;
                    mediaPageArr2[i3].fastScrollEnabled = true;
                    updateFastScrollVisibility(mediaPageArr2[i3], true);
                }
                i3++;
            }
        }
        this.photoVideoAdapter.notifyDataSetChanged();
    }

    public static /* synthetic */ int lambda$loadFastScrollData$11(Period period, Period period2) {
        return period2.date - period.date;
    }

    public static void showFastScrollHint(MediaPage mediaPage, SharedMediaData[] sharedMediaDataArr, boolean z) {
        Runnable runnable;
        if (z) {
            if (SharedConfig.fastScrollHintCount <= 0 || mediaPage.fastScrollHintView != null || mediaPage.fastScrollHinWasShown || mediaPage.listView.getFastScroll() == null || !mediaPage.listView.getFastScroll().isVisible || mediaPage.listView.getFastScroll().getVisibility() != 0 || sharedMediaDataArr[0].totalCount < 50) {
                return;
            }
            SharedConfig.setFastScrollHintCount(SharedConfig.fastScrollHintCount - 1);
            mediaPage.fastScrollHinWasShown = true;
            SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip = new SharedMediaFastScrollTooltip(mediaPage.getContext());
            mediaPage.fastScrollHintView = sharedMediaFastScrollTooltip;
            mediaPage.addView(sharedMediaFastScrollTooltip, LayoutHelper.createFrame(-2, -2.0f));
            mediaPage.fastScrollHintView.setAlpha(0.0f);
            mediaPage.fastScrollHintView.setScaleX(0.8f);
            mediaPage.fastScrollHintView.setScaleY(0.8f);
            mediaPage.fastScrollHintView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L).start();
            mediaPage.invalidate();
            SharedMediaLayout$$ExternalSyntheticLambda6 sharedMediaLayout$$ExternalSyntheticLambda6 = new SharedMediaLayout$$ExternalSyntheticLambda6(mediaPage, sharedMediaFastScrollTooltip);
            mediaPage.fastScrollHideHintRunnable = sharedMediaLayout$$ExternalSyntheticLambda6;
            AndroidUtilities.runOnUIThread(sharedMediaLayout$$ExternalSyntheticLambda6, 4000L);
        } else if (mediaPage.fastScrollHintView == null || (runnable = mediaPage.fastScrollHideHintRunnable) == null) {
        } else {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            mediaPage.fastScrollHideHintRunnable.run();
            mediaPage.fastScrollHideHintRunnable = null;
            mediaPage.fastScrollHintView = null;
        }
    }

    public static /* synthetic */ void lambda$showFastScrollHint$14(MediaPage mediaPage, SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip) {
        mediaPage.fastScrollHintView = null;
        mediaPage.fastScrollHideHintRunnable = null;
        sharedMediaFastScrollTooltip.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setDuration(220L).setListener(new AnonymousClass24(sharedMediaFastScrollTooltip)).start();
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$24 */
    /* loaded from: classes3.dex */
    public class AnonymousClass24 extends AnimatorListenerAdapter {
        final /* synthetic */ SharedMediaFastScrollTooltip val$tooltip;

        AnonymousClass24(SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip) {
            this.val$tooltip = sharedMediaFastScrollTooltip;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$tooltip.getParent() != null) {
                ((ViewGroup) this.val$tooltip.getParent()).removeView(this.val$tooltip);
            }
        }
    }

    public void setCommonGroupsCount(int i) {
        this.hasMedia[6] = i;
        updateTabs(true);
        checkCurrentTabValid();
    }

    public void onActionBarItemClick(View view, int i) {
        TLRPC$EncryptedChat tLRPC$EncryptedChat;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        if (i == 101) {
            if (DialogObject.isEncryptedDialog(this.dialog_id)) {
                tLRPC$EncryptedChat = this.profileActivity.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(this.dialog_id)));
                tLRPC$User = null;
                tLRPC$Chat = null;
            } else if (DialogObject.isUserDialog(this.dialog_id)) {
                tLRPC$User = this.profileActivity.getMessagesController().getUser(Long.valueOf(this.dialog_id));
                tLRPC$Chat = null;
                tLRPC$EncryptedChat = null;
            } else {
                tLRPC$Chat = this.profileActivity.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
                tLRPC$User = null;
                tLRPC$EncryptedChat = null;
            }
            AlertsCreator.createDeleteMessagesAlert(this.profileActivity, tLRPC$User, tLRPC$Chat, tLRPC$EncryptedChat, null, this.mergeDialogId, null, this.selectedFiles, null, false, 1, new SharedMediaLayout$$ExternalSyntheticLambda7(this), null, this.resourcesProvider);
            return;
        }
        char c = 1;
        if (i == 100) {
            if (this.info != null) {
                TLRPC$Chat chat = this.profileActivity.getMessagesController().getChat(Long.valueOf(this.info.id));
                if (this.profileActivity.getMessagesController().isChatNoForwards(chat)) {
                    HintView hintView = this.fwdRestrictedHint;
                    if (hintView == null) {
                        return;
                    }
                    hintView.setText((!ChatObject.isChannel(chat) || chat.megagroup) ? LocaleController.getString("ForwardsRestrictedInfoGroup", 2131626023) : LocaleController.getString("ForwardsRestrictedInfoChannel", 2131626022));
                    this.fwdRestrictedHint.showForView(view, true);
                    return;
                }
            }
            if (hasNoforwardsMessage()) {
                HintView hintView2 = this.fwdRestrictedHint;
                if (hintView2 == null) {
                    return;
                }
                hintView2.setText(LocaleController.getString("ForwardsRestrictedInfoBot", 2131626021));
                this.fwdRestrictedHint.showForView(view, true);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putInt("dialogsType", 3);
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            dialogsActivity.setDelegate(new SharedMediaLayout$$ExternalSyntheticLambda17(this));
            this.profileActivity.presentFragment(dialogsActivity);
        } else if (i != 102 || this.selectedFiles[0].size() + this.selectedFiles[1].size() != 1) {
        } else {
            SparseArray<MessageObject>[] sparseArrayArr = this.selectedFiles;
            if (sparseArrayArr[0].size() == 1) {
                c = 0;
            }
            MessageObject valueAt = sparseArrayArr[c].valueAt(0);
            Bundle bundle2 = new Bundle();
            long dialogId = valueAt.getDialogId();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                bundle2.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
            } else if (DialogObject.isUserDialog(dialogId)) {
                bundle2.putLong("user_id", dialogId);
            } else {
                TLRPC$Chat chat2 = this.profileActivity.getMessagesController().getChat(Long.valueOf(-dialogId));
                if (chat2 != null && chat2.migrated_to != null) {
                    bundle2.putLong("migrated_to", dialogId);
                    dialogId = -chat2.migrated_to.channel_id;
                }
                bundle2.putLong("chat_id", -dialogId);
            }
            bundle2.putInt("message_id", valueAt.getId());
            bundle2.putBoolean("need_remove_previous_same_chat_activity", false);
            this.profileActivity.presentFragment(new ChatActivity(bundle2), false);
        }
    }

    public /* synthetic */ void lambda$onActionBarItemClick$15() {
        showActionMode(false);
        this.actionBar.closeSearchField();
        this.cantDeleteMessagesCount = 0;
    }

    public /* synthetic */ void lambda$onActionBarItemClick$16(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        ArrayList<MessageObject> arrayList2 = new ArrayList<>();
        int i = 1;
        while (true) {
            if (i < 0) {
                break;
            }
            ArrayList arrayList3 = new ArrayList();
            for (int i2 = 0; i2 < this.selectedFiles[i].size(); i2++) {
                arrayList3.add(Integer.valueOf(this.selectedFiles[i].keyAt(i2)));
            }
            Collections.sort(arrayList3);
            Iterator it = arrayList3.iterator();
            while (it.hasNext()) {
                Integer num = (Integer) it.next();
                if (num.intValue() > 0) {
                    arrayList2.add(this.selectedFiles[i].get(num.intValue()));
                }
            }
            this.selectedFiles[i].clear();
            i--;
        }
        this.cantDeleteMessagesCount = 0;
        showActionMode(false);
        if (arrayList.size() > 1 || ((Long) arrayList.get(0)).longValue() == this.profileActivity.getUserConfig().getClientUserId() || charSequence != null) {
            updateRowsSelection();
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                long longValue = ((Long) arrayList.get(i3)).longValue();
                if (charSequence != null) {
                    this.profileActivity.getSendMessagesHelper().sendMessage(charSequence.toString(), longValue, null, null, null, true, null, null, null, true, 0, null);
                }
                this.profileActivity.getSendMessagesHelper().sendMessage(arrayList2, longValue, false, false, true, 0);
            }
            dialogsActivity.finishFragment();
            UndoView undoView = null;
            BaseFragment baseFragment = this.profileActivity;
            if (baseFragment instanceof ProfileActivity) {
                undoView = ((ProfileActivity) baseFragment).getUndoView();
            }
            if (undoView == null) {
                return;
            }
            if (arrayList.size() == 1) {
                undoView.showWithAction(((Long) arrayList.get(0)).longValue(), 53, Integer.valueOf(arrayList2.size()));
                return;
            }
            undoView.showWithAction(0L, 53, Integer.valueOf(arrayList2.size()), Integer.valueOf(arrayList.size()), (Runnable) null, (Runnable) null);
            return;
        }
        long longValue2 = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue2)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue2));
        } else {
            if (DialogObject.isUserDialog(longValue2)) {
                bundle.putLong("user_id", longValue2);
            } else {
                bundle.putLong("chat_id", -longValue2);
            }
            if (!this.profileActivity.getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
                return;
            }
        }
        this.profileActivity.getNotificationCenter().postNotificationName(NotificationCenter.closeChats, new Object[0]);
        ChatActivity chatActivity = new ChatActivity(bundle);
        dialogsActivity.presentFragment(chatActivity, true);
        chatActivity.showFieldPanelForForward(true, arrayList2);
    }

    private boolean prepareForMoving(MotionEvent motionEvent, boolean z) {
        int nextPageId = this.scrollSlidingTextTabStrip.getNextPageId(z);
        if (nextPageId < 0) {
            return false;
        }
        if (canShowSearchItem()) {
            int i = this.searchItemState;
            if (i != 0) {
                if (i == 2) {
                    this.searchItem.setAlpha(1.0f);
                } else if (i == 1) {
                    this.searchItem.setAlpha(0.0f);
                    this.searchItem.setVisibility(4);
                }
                this.searchItemState = 0;
            }
        } else {
            this.searchItem.setVisibility(4);
            this.searchItem.setAlpha(0.0f);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        hideFloatingDateView(true);
        this.maybeStartTracking = false;
        this.startedTracking = true;
        this.startedTrackingX = (int) motionEvent.getX();
        this.actionBar.setEnabled(false);
        this.scrollSlidingTextTabStrip.setEnabled(false);
        this.mediaPages[1].selectedType = nextPageId;
        this.mediaPages[1].setVisibility(0);
        this.animatingForward = z;
        switchToCurrentSelectedMode(true);
        if (z) {
            MediaPage[] mediaPageArr = this.mediaPages;
            mediaPageArr[1].setTranslationX(mediaPageArr[0].getMeasuredWidth());
        } else {
            MediaPage[] mediaPageArr2 = this.mediaPages;
            mediaPageArr2[1].setTranslationX(-mediaPageArr2[0].getMeasuredWidth());
        }
        return true;
    }

    @Override // android.view.View
    public void forceHasOverlappingRendering(boolean z) {
        super.forceHasOverlappingRendering(z);
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        this.topPadding = i2;
        int i5 = 0;
        int i6 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i6 >= mediaPageArr.length) {
                break;
            }
            mediaPageArr[i6].setTranslationY(this.topPadding - this.lastMeasuredTopPadding);
            i6++;
        }
        this.fragmentContextView.setTranslationY(AndroidUtilities.dp(48.0f) + i2);
        this.additionalFloatingTranslation = i2;
        ChatActionCell chatActionCell = this.floatingDateView;
        if (chatActionCell.getTag() == null) {
            i5 = -AndroidUtilities.dp(48.0f);
        }
        chatActionCell.setTranslationY(i5 + this.additionalFloatingTranslation);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int height = this.delegate.getListView() != null ? this.delegate.getListView().getHeight() : 0;
        if (height == 0) {
            height = View.MeasureSpec.getSize(i2);
        }
        setMeasuredDimension(size, height);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt != null && childAt.getVisibility() != 8) {
                if (childAt instanceof MediaPage) {
                    measureChildWithMargins(childAt, i, 0, View.MeasureSpec.makeMeasureSpec(height, 1073741824), 0);
                    ((MediaPage) childAt).listView.setPadding(0, 0, 0, this.topPadding);
                } else {
                    measureChildWithMargins(childAt, i, 0, i2, 0);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean checkTabsAnimationInProgress() {
        if (this.tabsAnimationInProgress) {
            int i = -1;
            boolean z = true;
            if (this.backAnimation) {
                if (Math.abs(this.mediaPages[0].getTranslationX()) < 1.0f) {
                    this.mediaPages[0].setTranslationX(0.0f);
                    MediaPage[] mediaPageArr = this.mediaPages;
                    MediaPage mediaPage = mediaPageArr[1];
                    int measuredWidth = mediaPageArr[0].getMeasuredWidth();
                    if (this.animatingForward) {
                        i = 1;
                    }
                    mediaPage.setTranslationX(measuredWidth * i);
                    if (z) {
                        AnimatorSet animatorSet = this.tabsAnimation;
                        if (animatorSet != null) {
                            animatorSet.cancel();
                            this.tabsAnimation = null;
                        }
                        this.tabsAnimationInProgress = false;
                    }
                    return this.tabsAnimationInProgress;
                }
                z = false;
                if (z) {
                }
                return this.tabsAnimationInProgress;
            }
            if (Math.abs(this.mediaPages[1].getTranslationX()) < 1.0f) {
                MediaPage[] mediaPageArr2 = this.mediaPages;
                MediaPage mediaPage2 = mediaPageArr2[0];
                int measuredWidth2 = mediaPageArr2[0].getMeasuredWidth();
                if (!this.animatingForward) {
                    i = 1;
                }
                mediaPage2.setTranslationX(measuredWidth2 * i);
                this.mediaPages[1].setTranslationX(0.0f);
                if (z) {
                }
                return this.tabsAnimationInProgress;
            }
            z = false;
            if (z) {
            }
            return this.tabsAnimationInProgress;
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return checkTabsAnimationInProgress() || this.scrollSlidingTextTabStrip.isAnimatingIndicator() || onTouchEvent(motionEvent);
    }

    public boolean isCurrentTabFirst() {
        return this.scrollSlidingTextTabStrip.getCurrentTabId() == this.scrollSlidingTextTabStrip.getFirstTabId();
    }

    public RecyclerListView getCurrentListView() {
        return this.mediaPages[0].listView;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        float f3;
        int i;
        MediaPage[] mediaPageArr;
        MediaPage[] mediaPageArr2;
        MediaPage[] mediaPageArr3;
        MediaPage[] mediaPageArr4;
        MediaPage[] mediaPageArr5;
        MediaPage[] mediaPageArr6;
        boolean z;
        MediaPage[] mediaPageArr7;
        int i2 = 0;
        boolean z2 = false;
        if (this.profileActivity.getParentLayout() == null || this.profileActivity.getParentLayout().checkTransitionAnimation() || checkTabsAnimationInProgress() || this.isInPinchToZoomTouchMode) {
            return false;
        }
        if (motionEvent != null) {
            if (this.velocityTracker == null) {
                this.velocityTracker = VelocityTracker.obtain();
            }
            this.velocityTracker.addMovement(motionEvent);
            HintView hintView = this.fwdRestrictedHint;
            if (hintView != null) {
                hintView.hide();
            }
        }
        if (motionEvent != null && motionEvent.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking && motionEvent.getY() >= AndroidUtilities.dp(48.0f)) {
            this.startedTrackingPointerId = motionEvent.getPointerId(0);
            this.maybeStartTracking = true;
            this.startedTrackingX = (int) motionEvent.getX();
            this.startedTrackingY = (int) motionEvent.getY();
            this.velocityTracker.clear();
        } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
            int x = (int) (motionEvent.getX() - this.startedTrackingX);
            int abs = Math.abs(((int) motionEvent.getY()) - this.startedTrackingY);
            if (this.startedTracking && (((z = this.animatingForward) && x > 0) || (!z && x < 0))) {
                if (!prepareForMoving(motionEvent, x < 0)) {
                    this.maybeStartTracking = true;
                    this.startedTracking = false;
                    this.mediaPages[0].setTranslationX(0.0f);
                    this.mediaPages[1].setTranslationX(this.animatingForward ? mediaPageArr7[0].getMeasuredWidth() : -mediaPageArr7[0].getMeasuredWidth());
                    this.scrollSlidingTextTabStrip.selectTabWithId(this.mediaPages[1].selectedType, 0.0f);
                }
            }
            if (this.maybeStartTracking && !this.startedTracking) {
                if (Math.abs(x) >= AndroidUtilities.getPixelsInCM(0.3f, true) && Math.abs(x) > abs) {
                    if (x < 0) {
                        z2 = true;
                    }
                    prepareForMoving(motionEvent, z2);
                }
            } else if (this.startedTracking) {
                this.mediaPages[0].setTranslationX(x);
                if (this.animatingForward) {
                    this.mediaPages[1].setTranslationX(mediaPageArr6[0].getMeasuredWidth() + x);
                } else {
                    this.mediaPages[1].setTranslationX(x - mediaPageArr5[0].getMeasuredWidth());
                }
                float abs2 = Math.abs(x) / this.mediaPages[0].getMeasuredWidth();
                if (canShowSearchItem()) {
                    int i3 = this.searchItemState;
                    if (i3 == 2) {
                        this.searchItem.setAlpha(1.0f - abs2);
                    } else if (i3 == 1) {
                        this.searchItem.setAlpha(abs2);
                    }
                    MediaPage[] mediaPageArr8 = this.mediaPages;
                    float f4 = (mediaPageArr8[1] == null || mediaPageArr8[1].selectedType != 0) ? 0.0f : abs2;
                    if (this.mediaPages[0].selectedType == 0) {
                        f4 = 1.0f - abs2;
                    }
                    this.photoVideoOptionsItem.setAlpha(f4);
                    ImageView imageView = this.photoVideoOptionsItem;
                    if (f4 == 0.0f || !canShowSearchItem()) {
                        i2 = 4;
                    }
                    imageView.setVisibility(i2);
                } else {
                    this.searchItem.setAlpha(0.0f);
                }
                this.scrollSlidingTextTabStrip.selectTabWithId(this.mediaPages[1].selectedType, abs2);
                onSelectedTabChanged();
            }
        } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
            this.velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
            if (motionEvent == null || motionEvent.getAction() == 3) {
                f2 = 0.0f;
                f = 0.0f;
            } else {
                f2 = this.velocityTracker.getXVelocity();
                f = this.velocityTracker.getYVelocity();
                if (!this.startedTracking && Math.abs(f2) >= 3000.0f && Math.abs(f2) > Math.abs(f)) {
                    prepareForMoving(motionEvent, f2 < 0.0f);
                }
            }
            if (this.startedTracking) {
                float x2 = this.mediaPages[0].getX();
                this.tabsAnimation = new AnimatorSet();
                boolean z3 = Math.abs(x2) < ((float) this.mediaPages[0].getMeasuredWidth()) / 3.0f && (Math.abs(f2) < 3500.0f || Math.abs(f2) < Math.abs(f));
                this.backAnimation = z3;
                if (z3) {
                    f3 = Math.abs(x2);
                    if (this.animatingForward) {
                        this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, mediaPageArr4[1].getMeasuredWidth()));
                    } else {
                        this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, -mediaPageArr3[1].getMeasuredWidth()));
                    }
                } else {
                    f3 = this.mediaPages[0].getMeasuredWidth() - Math.abs(x2);
                    if (this.animatingForward) {
                        this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, -mediaPageArr2[0].getMeasuredWidth()), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, 0.0f));
                    } else {
                        this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, mediaPageArr[0].getMeasuredWidth()), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, 0.0f));
                    }
                }
                this.tabsAnimation.setInterpolator(interpolator);
                int measuredWidth = getMeasuredWidth();
                float f5 = measuredWidth / 2;
                float distanceInfluenceForSnapDuration = f5 + (AndroidUtilities.distanceInfluenceForSnapDuration(Math.min(1.0f, (f3 * 1.0f) / measuredWidth)) * f5);
                float abs3 = Math.abs(f2);
                if (abs3 > 0.0f) {
                    i = Math.round(Math.abs(distanceInfluenceForSnapDuration / abs3) * 1000.0f) * 4;
                } else {
                    i = (int) (((f3 / getMeasuredWidth()) + 1.0f) * 100.0f);
                }
                this.tabsAnimation.setDuration(Math.max(150, Math.min(i, 600)));
                this.tabsAnimation.addListener(new AnonymousClass25());
                this.tabsAnimation.start();
                this.tabsAnimationInProgress = true;
                this.startedTracking = false;
                onSelectedTabChanged();
            } else {
                this.maybeStartTracking = false;
                this.actionBar.setEnabled(true);
                this.scrollSlidingTextTabStrip.setEnabled(true);
            }
            VelocityTracker velocityTracker = this.velocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.velocityTracker = null;
            }
        }
        return this.startedTracking;
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$25 */
    /* loaded from: classes3.dex */
    public class AnonymousClass25 extends AnimatorListenerAdapter {
        AnonymousClass25() {
            SharedMediaLayout.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            SharedMediaLayout.this.tabsAnimation = null;
            if (SharedMediaLayout.this.backAnimation) {
                SharedMediaLayout.this.mediaPages[1].setVisibility(8);
                if (SharedMediaLayout.this.canShowSearchItem()) {
                    if (SharedMediaLayout.this.searchItemState == 2) {
                        SharedMediaLayout.this.searchItem.setAlpha(1.0f);
                    } else if (SharedMediaLayout.this.searchItemState == 1) {
                        SharedMediaLayout.this.searchItem.setAlpha(0.0f);
                        SharedMediaLayout.this.searchItem.setVisibility(4);
                    }
                } else {
                    SharedMediaLayout.this.searchItem.setVisibility(4);
                    SharedMediaLayout.this.searchItem.setAlpha(0.0f);
                }
                SharedMediaLayout.this.searchItemState = 0;
            } else {
                MediaPage mediaPage = SharedMediaLayout.this.mediaPages[0];
                SharedMediaLayout.this.mediaPages[0] = SharedMediaLayout.this.mediaPages[1];
                SharedMediaLayout.this.mediaPages[1] = mediaPage;
                SharedMediaLayout.this.mediaPages[1].setVisibility(8);
                if (SharedMediaLayout.this.searchItemState == 2) {
                    SharedMediaLayout.this.searchItem.setVisibility(4);
                }
                SharedMediaLayout.this.searchItemState = 0;
                SharedMediaLayout.this.scrollSlidingTextTabStrip.selectTabWithId(SharedMediaLayout.this.mediaPages[0].selectedType, 1.0f);
                SharedMediaLayout.this.onSelectedTabChanged();
                SharedMediaLayout.this.startStopVisibleGifs();
            }
            SharedMediaLayout.this.tabsAnimationInProgress = false;
            SharedMediaLayout.this.maybeStartTracking = false;
            SharedMediaLayout.this.startedTracking = false;
            SharedMediaLayout.this.actionBar.setEnabled(true);
            SharedMediaLayout.this.scrollSlidingTextTabStrip.setEnabled(true);
        }
    }

    public boolean closeActionMode() {
        if (this.isActionModeShowed) {
            for (int i = 1; i >= 0; i--) {
                this.selectedFiles[i].clear();
            }
            this.cantDeleteMessagesCount = 0;
            showActionMode(false);
            updateRowsSelection();
            return true;
        }
        return false;
    }

    public void setVisibleHeight(int i) {
        int max = Math.max(i, AndroidUtilities.dp(120.0f));
        for (int i2 = 0; i2 < this.mediaPages.length; i2++) {
            float f = (-(getMeasuredHeight() - max)) / 2.0f;
            this.mediaPages[i2].emptyView.setTranslationY(f);
            this.mediaPages[i2].progressView.setTranslationY(-f);
        }
    }

    private void showActionMode(boolean z) {
        if (this.isActionModeShowed == z) {
            return;
        }
        this.isActionModeShowed = z;
        AnimatorSet animatorSet = this.actionModeAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z) {
            this.actionModeLayout.setVisibility(0);
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.actionModeAnimation = animatorSet2;
        Animator[] animatorArr = new Animator[1];
        LinearLayout linearLayout = this.actionModeLayout;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        animatorArr[0] = ObjectAnimator.ofFloat(linearLayout, property, fArr);
        animatorSet2.playTogether(animatorArr);
        this.actionModeAnimation.setDuration(180L);
        this.actionModeAnimation.addListener(new AnonymousClass26(z));
        this.actionModeAnimation.start();
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$26 */
    /* loaded from: classes3.dex */
    public class AnonymousClass26 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass26(boolean z) {
            SharedMediaLayout.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            SharedMediaLayout.this.actionModeAnimation = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (SharedMediaLayout.this.actionModeAnimation == null) {
                return;
            }
            SharedMediaLayout.this.actionModeAnimation = null;
            if (this.val$show) {
                return;
            }
            SharedMediaLayout.this.actionModeLayout.setVisibility(4);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:178:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0498  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x04b9 A[SYNTHETIC] */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        RecyclerView.Adapter adapter;
        int i3;
        int size;
        int i4;
        boolean z;
        RecyclerView.Adapter adapter2;
        RecyclerView.Adapter adapter3;
        int i5;
        int i6 = 0;
        if (i == NotificationCenter.mediaDidLoad) {
            long longValue = ((Long) objArr[0]).longValue();
            int intValue = ((Integer) objArr[3]).intValue();
            int intValue2 = ((Integer) objArr[7]).intValue();
            int intValue3 = ((Integer) objArr[4]).intValue();
            boolean booleanValue = ((Boolean) objArr[6]).booleanValue();
            if (intValue3 == 6 || intValue3 == 7) {
                intValue3 = 0;
            }
            if (intValue == this.profileActivity.getClassGuid()) {
                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                if (intValue2 == sharedMediaDataArr[intValue3].requestIndex) {
                    if (intValue3 != 0 && intValue3 != 1 && intValue3 != 2 && intValue3 != 4) {
                        sharedMediaDataArr[intValue3].totalCount = ((Integer) objArr[1]).intValue();
                    }
                    ArrayList arrayList = (ArrayList) objArr[2];
                    boolean isEncryptedDialog = DialogObject.isEncryptedDialog(this.dialog_id);
                    int i7 = longValue == this.dialog_id ? 0 : 1;
                    if (intValue3 == 0) {
                        adapter3 = this.photoVideoAdapter;
                    } else if (intValue3 == 1) {
                        adapter3 = this.documentsAdapter;
                    } else if (intValue3 == 2) {
                        adapter3 = this.voiceAdapter;
                    } else if (intValue3 == 3) {
                        adapter3 = this.linksAdapter;
                    } else if (intValue3 == 4) {
                        adapter3 = this.audioAdapter;
                    } else {
                        adapter3 = intValue3 == 5 ? this.gifAdapter : null;
                    }
                    int size2 = this.sharedMediaData[intValue3].messages.size();
                    if (adapter3 != null) {
                        i5 = adapter3.getItemCount();
                        if (adapter3 instanceof RecyclerListView.SectionsAdapter) {
                            ((RecyclerListView.SectionsAdapter) adapter3).notifySectionsChanged();
                        }
                    } else {
                        i5 = 0;
                    }
                    this.sharedMediaData[intValue3].loading = false;
                    SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                    if (booleanValue) {
                        for (int size3 = arrayList.size() - 1; size3 >= 0; size3--) {
                            MessageObject messageObject = (MessageObject) arrayList.get(size3);
                            if (this.sharedMediaData[intValue3].addMessage(messageObject, i7, true, isEncryptedDialog)) {
                                sparseBooleanArray.put(messageObject.getId(), true);
                                SharedMediaData.access$710(this.sharedMediaData[intValue3]);
                                if (this.sharedMediaData[intValue3].startOffset < 0) {
                                    this.sharedMediaData[intValue3].startOffset = 0;
                                }
                            }
                        }
                        this.sharedMediaData[intValue3].startReached = ((Boolean) objArr[5]).booleanValue();
                        SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                        if (sharedMediaDataArr2[intValue3].startReached) {
                            sharedMediaDataArr2[intValue3].startOffset = 0;
                        }
                    } else {
                        for (int i8 = 0; i8 < arrayList.size(); i8++) {
                            MessageObject messageObject2 = (MessageObject) arrayList.get(i8);
                            if (this.sharedMediaData[intValue3].addMessage(messageObject2, i7, false, isEncryptedDialog)) {
                                sparseBooleanArray.put(messageObject2.getId(), true);
                                SharedMediaData.access$7010(this.sharedMediaData[intValue3]);
                                if (this.sharedMediaData[intValue3].endLoadingStubs < 0) {
                                    this.sharedMediaData[intValue3].endLoadingStubs = 0;
                                }
                            }
                        }
                        SharedMediaData[] sharedMediaDataArr3 = this.sharedMediaData;
                        if (sharedMediaDataArr3[intValue3].loadingAfterFastScroll && sharedMediaDataArr3[intValue3].messages.size() > 0) {
                            SharedMediaData[] sharedMediaDataArr4 = this.sharedMediaData;
                            sharedMediaDataArr4[intValue3].min_id = sharedMediaDataArr4[intValue3].messages.get(0).getId();
                        }
                        this.sharedMediaData[intValue3].endReached[i7] = ((Boolean) objArr[5]).booleanValue();
                        SharedMediaData[] sharedMediaDataArr5 = this.sharedMediaData;
                        if (sharedMediaDataArr5[intValue3].endReached[i7]) {
                            sharedMediaDataArr5[intValue3].totalCount = sharedMediaDataArr5[intValue3].messages.size() + this.sharedMediaData[intValue3].startOffset;
                        }
                    }
                    if (!booleanValue && i7 == 0) {
                        SharedMediaData[] sharedMediaDataArr6 = this.sharedMediaData;
                        if (sharedMediaDataArr6[intValue3].endReached[i7] && this.mergeDialogId != 0) {
                            sharedMediaDataArr6[intValue3].loading = true;
                            this.profileActivity.getMediaDataController().loadMedia(this.mergeDialogId, 50, this.sharedMediaData[intValue3].max_id[1], 0, intValue3, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[intValue3].requestIndex);
                        }
                    }
                    if (adapter3 != null) {
                        BlurredRecyclerView blurredRecyclerView = null;
                        int i9 = 0;
                        while (true) {
                            MediaPage[] mediaPageArr = this.mediaPages;
                            if (i9 >= mediaPageArr.length) {
                                break;
                            }
                            if (mediaPageArr[i9].listView.getAdapter() == adapter3) {
                                blurredRecyclerView = this.mediaPages[i9].listView;
                                this.mediaPages[i9].listView.stopScroll();
                            }
                            i9++;
                        }
                        int itemCount = adapter3.getItemCount();
                        SharedPhotoVideoAdapter sharedPhotoVideoAdapter = this.photoVideoAdapter;
                        if (adapter3 == sharedPhotoVideoAdapter) {
                            if (sharedPhotoVideoAdapter.getItemCount() == i5) {
                                AndroidUtilities.updateVisibleRows(blurredRecyclerView);
                            } else {
                                this.photoVideoAdapter.notifyDataSetChanged();
                            }
                        } else {
                            adapter3.notifyDataSetChanged();
                        }
                        if (!this.sharedMediaData[intValue3].messages.isEmpty() || this.sharedMediaData[intValue3].loading) {
                            if (blurredRecyclerView != null && (adapter3 == this.photoVideoAdapter || itemCount >= i5)) {
                                animateItemsEnter(blurredRecyclerView, i5, sparseBooleanArray);
                            }
                        } else if (blurredRecyclerView != null) {
                            animateItemsEnter(blurredRecyclerView, i5, sparseBooleanArray);
                        }
                        if (blurredRecyclerView != null && !this.sharedMediaData[intValue3].loadingAfterFastScroll) {
                            if (size2 == 0) {
                                for (int i10 = 0; i10 < 2; i10++) {
                                    if (this.mediaPages[i10].selectedType == 0) {
                                        ((LinearLayoutManager) blurredRecyclerView.getLayoutManager()).scrollToPositionWithOffset(this.photoVideoAdapter.getPositionForIndex(0), 0);
                                    }
                                }
                            } else {
                                saveScrollPosition();
                            }
                        }
                    }
                    SharedMediaData[] sharedMediaDataArr7 = this.sharedMediaData;
                    if (sharedMediaDataArr7[intValue3].loadingAfterFastScroll) {
                        if (sharedMediaDataArr7[intValue3].messages.size() == 0) {
                            loadFromStart(intValue3);
                        } else {
                            this.sharedMediaData[intValue3].loadingAfterFastScroll = false;
                        }
                    }
                    this.scrolling = true;
                    return;
                }
            }
            if (this.sharedMediaPreloader == null || !this.sharedMediaData[intValue3].messages.isEmpty() || this.sharedMediaData[intValue3].loadingAfterFastScroll || !fillMediaData(intValue3)) {
                return;
            }
            if (intValue3 == 0) {
                adapter2 = this.photoVideoAdapter;
            } else if (intValue3 == 1) {
                adapter2 = this.documentsAdapter;
            } else if (intValue3 == 2) {
                adapter2 = this.voiceAdapter;
            } else if (intValue3 == 3) {
                adapter2 = this.linksAdapter;
            } else if (intValue3 == 4) {
                adapter2 = this.audioAdapter;
            } else {
                adapter2 = intValue3 == 5 ? this.gifAdapter : null;
            }
            if (adapter2 != null) {
                while (true) {
                    MediaPage[] mediaPageArr2 = this.mediaPages;
                    if (i6 >= mediaPageArr2.length) {
                        break;
                    }
                    if (mediaPageArr2[i6].listView.getAdapter() == adapter2) {
                        this.mediaPages[i6].listView.stopScroll();
                    }
                    i6++;
                }
                adapter2.notifyDataSetChanged();
            }
            this.scrolling = true;
            return;
        }
        int i11 = -1;
        if (i == NotificationCenter.messagesDeleted) {
            if (((Boolean) objArr[2]).booleanValue()) {
                return;
            }
            TLRPC$Chat chat = DialogObject.isChatDialog(this.dialog_id) ? this.profileActivity.getMessagesController().getChat(Long.valueOf(-this.dialog_id)) : null;
            long longValue2 = ((Long) objArr[1]).longValue();
            if (ChatObject.isChannel(chat)) {
                if (longValue2 != 0 || this.mergeDialogId == 0) {
                    if (longValue2 != chat.id) {
                        return;
                    }
                } else {
                    i3 = 1;
                    ArrayList arrayList2 = (ArrayList) objArr[0];
                    size = arrayList2.size();
                    z = false;
                    for (i4 = 0; i4 < size; i4++) {
                        int i12 = 0;
                        while (true) {
                            SharedMediaData[] sharedMediaDataArr8 = this.sharedMediaData;
                            if (i12 < sharedMediaDataArr8.length) {
                                if (sharedMediaDataArr8[i12].deleteMessage(((Integer) arrayList2.get(i4)).intValue(), i3) != null) {
                                    i11 = i12;
                                    z = true;
                                }
                                i12++;
                            }
                        }
                    }
                    if (z) {
                        this.scrolling = true;
                        SharedPhotoVideoAdapter sharedPhotoVideoAdapter2 = this.photoVideoAdapter;
                        if (sharedPhotoVideoAdapter2 != null) {
                            sharedPhotoVideoAdapter2.notifyDataSetChanged();
                        }
                        SharedDocumentsAdapter sharedDocumentsAdapter = this.documentsAdapter;
                        if (sharedDocumentsAdapter != null) {
                            sharedDocumentsAdapter.notifyDataSetChanged();
                        }
                        SharedDocumentsAdapter sharedDocumentsAdapter2 = this.voiceAdapter;
                        if (sharedDocumentsAdapter2 != null) {
                            sharedDocumentsAdapter2.notifyDataSetChanged();
                        }
                        SharedLinksAdapter sharedLinksAdapter = this.linksAdapter;
                        if (sharedLinksAdapter != null) {
                            sharedLinksAdapter.notifyDataSetChanged();
                        }
                        SharedDocumentsAdapter sharedDocumentsAdapter3 = this.audioAdapter;
                        if (sharedDocumentsAdapter3 != null) {
                            sharedDocumentsAdapter3.notifyDataSetChanged();
                        }
                        GifAdapter gifAdapter = this.gifAdapter;
                        if (gifAdapter != null) {
                            gifAdapter.notifyDataSetChanged();
                        }
                        if (i11 == 0 || i11 == 1 || i11 == 2 || i11 == 4) {
                            loadFastScrollData(true);
                        }
                    }
                    getMediaPage(i11);
                }
            } else if (longValue2 != 0) {
                return;
            }
            i3 = 0;
            ArrayList arrayList22 = (ArrayList) objArr[0];
            size = arrayList22.size();
            z = false;
            while (i4 < size) {
            }
            if (z) {
            }
            getMediaPage(i11);
        } else if (i == NotificationCenter.didReceiveNewMessages) {
            if (((Boolean) objArr[2]).booleanValue()) {
                return;
            }
            long longValue3 = ((Long) objArr[0]).longValue();
            long j = this.dialog_id;
            if (longValue3 != j) {
                return;
            }
            ArrayList arrayList3 = (ArrayList) objArr[1];
            boolean isEncryptedDialog2 = DialogObject.isEncryptedDialog(j);
            int i13 = 0;
            boolean z2 = false;
            while (i13 < arrayList3.size()) {
                MessageObject messageObject3 = (MessageObject) arrayList3.get(i13);
                if (messageObject3.messageOwner.media != null && !messageObject3.needDrawBluredPreview()) {
                    int mediaType = MediaDataController.getMediaType(messageObject3.messageOwner);
                    if (mediaType == i11) {
                        return;
                    }
                    SharedMediaData[] sharedMediaDataArr9 = this.sharedMediaData;
                    if (sharedMediaDataArr9[mediaType].startReached) {
                        if (sharedMediaDataArr9[mediaType].addMessage(messageObject3, messageObject3.getDialogId() == this.dialog_id ? 0 : 1, true, isEncryptedDialog2)) {
                            this.hasMedia[mediaType] = 1;
                            z2 = true;
                        }
                    }
                }
                i13++;
                i11 = -1;
            }
            if (!z2) {
                return;
            }
            this.scrolling = true;
            while (true) {
                MediaPage[] mediaPageArr3 = this.mediaPages;
                if (i6 < mediaPageArr3.length) {
                    if (mediaPageArr3[i6].selectedType != 0) {
                        if (this.mediaPages[i6].selectedType != 1) {
                            if (this.mediaPages[i6].selectedType != 2) {
                                if (this.mediaPages[i6].selectedType != 3) {
                                    if (this.mediaPages[i6].selectedType == 4) {
                                        adapter = this.audioAdapter;
                                    } else {
                                        adapter = this.mediaPages[i6].selectedType == 5 ? this.gifAdapter : null;
                                        if (adapter != null) {
                                            adapter.getItemCount();
                                            this.photoVideoAdapter.notifyDataSetChanged();
                                            this.documentsAdapter.notifyDataSetChanged();
                                            this.voiceAdapter.notifyDataSetChanged();
                                            this.linksAdapter.notifyDataSetChanged();
                                            this.audioAdapter.notifyDataSetChanged();
                                            this.gifAdapter.notifyDataSetChanged();
                                        }
                                        i6++;
                                    }
                                } else {
                                    adapter = this.linksAdapter;
                                }
                            } else {
                                adapter = this.voiceAdapter;
                            }
                            if (adapter != null) {
                            }
                            i6++;
                        } else {
                            adapter = this.documentsAdapter;
                        }
                    } else {
                        adapter = this.photoVideoAdapter;
                    }
                    if (adapter != null) {
                    }
                    i6++;
                } else {
                    updateTabs(true);
                    return;
                }
            }
        } else if (i == NotificationCenter.messageReceivedByServer) {
            if (((Boolean) objArr[6]).booleanValue()) {
                return;
            }
            Integer num = (Integer) objArr[0];
            Integer num2 = (Integer) objArr[1];
            while (true) {
                SharedMediaData[] sharedMediaDataArr10 = this.sharedMediaData;
                if (i6 >= sharedMediaDataArr10.length) {
                    return;
                }
                sharedMediaDataArr10[i6].replaceMid(num.intValue(), num2.intValue());
                i6++;
            }
        } else if (i == NotificationCenter.messagePlayingDidStart || i == NotificationCenter.messagePlayingPlayStateChanged || i == NotificationCenter.messagePlayingDidReset) {
            if (i == NotificationCenter.messagePlayingDidReset || i == NotificationCenter.messagePlayingPlayStateChanged) {
                int i14 = 0;
                while (true) {
                    MediaPage[] mediaPageArr4 = this.mediaPages;
                    if (i14 >= mediaPageArr4.length) {
                        return;
                    }
                    int childCount = mediaPageArr4[i14].listView.getChildCount();
                    for (int i15 = 0; i15 < childCount; i15++) {
                        View childAt = this.mediaPages[i14].listView.getChildAt(i15);
                        if (childAt instanceof SharedAudioCell) {
                            SharedAudioCell sharedAudioCell = (SharedAudioCell) childAt;
                            if (sharedAudioCell.getMessage() != null) {
                                sharedAudioCell.updateButtonState(false, true);
                            }
                        }
                    }
                    i14++;
                }
            } else if (((MessageObject) objArr[0]).eventId == 0) {
                int i16 = 0;
                while (true) {
                    MediaPage[] mediaPageArr5 = this.mediaPages;
                    if (i16 >= mediaPageArr5.length) {
                        return;
                    }
                    int childCount2 = mediaPageArr5[i16].listView.getChildCount();
                    for (int i17 = 0; i17 < childCount2; i17++) {
                        View childAt2 = this.mediaPages[i16].listView.getChildAt(i17);
                        if (childAt2 instanceof SharedAudioCell) {
                            SharedAudioCell sharedAudioCell2 = (SharedAudioCell) childAt2;
                            if (sharedAudioCell2.getMessage() != null) {
                                sharedAudioCell2.updateButtonState(false, true);
                            }
                        }
                    }
                    i16++;
                }
            }
        }
    }

    public void saveScrollPosition() {
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i < mediaPageArr.length) {
                BlurredRecyclerView blurredRecyclerView = mediaPageArr[i].listView;
                if (blurredRecyclerView != null) {
                    int i2 = 0;
                    int i3 = 0;
                    for (int i4 = 0; i4 < blurredRecyclerView.getChildCount(); i4++) {
                        View childAt = blurredRecyclerView.getChildAt(i4);
                        if (childAt instanceof SharedPhotoVideoCell2) {
                            SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) childAt;
                            int messageId = sharedPhotoVideoCell2.getMessageId();
                            i3 = sharedPhotoVideoCell2.getTop();
                            i2 = messageId;
                        }
                        if (childAt instanceof SharedDocumentCell) {
                            SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) childAt;
                            int id = sharedDocumentCell.getMessage().getId();
                            i3 = sharedDocumentCell.getTop();
                            i2 = id;
                        }
                        if (childAt instanceof SharedAudioCell) {
                            SharedAudioCell sharedAudioCell = (SharedAudioCell) childAt;
                            i2 = sharedAudioCell.getMessage().getId();
                            i3 = sharedAudioCell.getTop();
                        }
                        if (i2 != 0) {
                            break;
                        }
                    }
                    if (i2 != 0) {
                        int i5 = -1;
                        if (this.mediaPages[i].selectedType >= 0 && this.mediaPages[i].selectedType < this.sharedMediaData.length) {
                            int i6 = 0;
                            while (true) {
                                if (i6 >= this.sharedMediaData[this.mediaPages[i].selectedType].messages.size()) {
                                    break;
                                } else if (i2 == this.sharedMediaData[this.mediaPages[i].selectedType].messages.get(i6).getId()) {
                                    i5 = i6;
                                    break;
                                } else {
                                    i6++;
                                }
                            }
                            int i7 = this.sharedMediaData[this.mediaPages[i].selectedType].startOffset + i5;
                            if (i5 >= 0) {
                                ((LinearLayoutManager) blurredRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i7, (-this.mediaPages[i].listView.getPaddingTop()) + i3);
                                if (this.photoVideoChangeColumnsAnimation) {
                                    this.mediaPages[i].animationSupportingLayoutManager.scrollToPositionWithOffset(i7, (-this.mediaPages[i].listView.getPaddingTop()) + i3);
                                }
                            }
                        }
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void animateItemsEnter(RecyclerListView recyclerListView, int i, SparseBooleanArray sparseBooleanArray) {
        int childCount = recyclerListView.getChildCount();
        View view = null;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = recyclerListView.getChildAt(i2);
            if (childAt instanceof FlickerLoadingView) {
                view = childAt;
            }
        }
        if (view != null) {
            recyclerListView.removeView(view);
        }
        getViewTreeObserver().addOnPreDrawListener(new AnonymousClass27(recyclerListView, sparseBooleanArray, view, i));
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$27 */
    /* loaded from: classes3.dex */
    public class AnonymousClass27 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ SparseBooleanArray val$addedMesages;
        final /* synthetic */ RecyclerListView val$finalListView;
        final /* synthetic */ View val$finalProgressView;
        final /* synthetic */ int val$oldItemCount;

        AnonymousClass27(RecyclerListView recyclerListView, SparseBooleanArray sparseBooleanArray, View view, int i) {
            SharedMediaLayout.this = r1;
            this.val$finalListView = recyclerListView;
            this.val$addedMesages = sparseBooleanArray;
            this.val$finalProgressView = view;
            this.val$oldItemCount = i;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            SharedMediaLayout.this.getViewTreeObserver().removeOnPreDrawListener(this);
            RecyclerView.Adapter adapter = this.val$finalListView.getAdapter();
            if (adapter == SharedMediaLayout.this.photoVideoAdapter || adapter == SharedMediaLayout.this.documentsAdapter || adapter == SharedMediaLayout.this.audioAdapter || adapter == SharedMediaLayout.this.voiceAdapter) {
                if (this.val$addedMesages != null) {
                    int childCount = this.val$finalListView.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childAt = this.val$finalListView.getChildAt(i);
                        int messageId = SharedMediaLayout.this.getMessageId(childAt);
                        if (messageId != 0 && this.val$addedMesages.get(messageId, false)) {
                            SharedMediaLayout.this.messageAlphaEnter.put(messageId, Float.valueOf(0.0f));
                            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                            ofFloat.addUpdateListener(new SharedMediaLayout$27$$ExternalSyntheticLambda0(this, messageId, this.val$finalListView));
                            ofFloat.addListener(new AnonymousClass1(messageId));
                            ofFloat.setStartDelay((int) ((Math.min(this.val$finalListView.getMeasuredHeight(), Math.max(0, childAt.getTop())) / this.val$finalListView.getMeasuredHeight()) * 100.0f));
                            ofFloat.setDuration(250L);
                            ofFloat.start();
                        }
                        this.val$finalListView.invalidate();
                    }
                }
            } else {
                int childCount2 = this.val$finalListView.getChildCount();
                AnimatorSet animatorSet = new AnimatorSet();
                for (int i2 = 0; i2 < childCount2; i2++) {
                    View childAt2 = this.val$finalListView.getChildAt(i2);
                    if (childAt2 != this.val$finalProgressView && this.val$finalListView.getChildAdapterPosition(childAt2) >= this.val$oldItemCount - 1) {
                        childAt2.setAlpha(0.0f);
                        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(childAt2, View.ALPHA, 0.0f, 1.0f);
                        ofFloat2.setStartDelay((int) ((Math.min(this.val$finalListView.getMeasuredHeight(), Math.max(0, childAt2.getTop())) / this.val$finalListView.getMeasuredHeight()) * 100.0f));
                        ofFloat2.setDuration(200L);
                        animatorSet.playTogether(ofFloat2);
                    }
                    View view = this.val$finalProgressView;
                    if (view != null && view.getParent() == null) {
                        this.val$finalListView.addView(this.val$finalProgressView);
                        RecyclerView.LayoutManager layoutManager = this.val$finalListView.getLayoutManager();
                        if (layoutManager != null) {
                            layoutManager.ignoreView(this.val$finalProgressView);
                            View view2 = this.val$finalProgressView;
                            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view2, View.ALPHA, view2.getAlpha(), 0.0f);
                            ofFloat3.addListener(new AnonymousClass2(layoutManager));
                            ofFloat3.start();
                        }
                    }
                }
                animatorSet.start();
            }
            return true;
        }

        public /* synthetic */ void lambda$onPreDraw$0(int i, RecyclerListView recyclerListView, ValueAnimator valueAnimator) {
            SharedMediaLayout.this.messageAlphaEnter.put(i, (Float) valueAnimator.getAnimatedValue());
            recyclerListView.invalidate();
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$27$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ int val$messageId;

            AnonymousClass1(int i) {
                AnonymousClass27.this = r1;
                this.val$messageId = i;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SharedMediaLayout.this.messageAlphaEnter.remove(this.val$messageId);
                AnonymousClass27.this.val$finalListView.invalidate();
            }
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$27$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 extends AnimatorListenerAdapter {
            final /* synthetic */ RecyclerView.LayoutManager val$layoutManager;

            AnonymousClass2(RecyclerView.LayoutManager layoutManager) {
                AnonymousClass27.this = r1;
                this.val$layoutManager = layoutManager;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AnonymousClass27.this.val$finalProgressView.setAlpha(1.0f);
                this.val$layoutManager.stopIgnoringView(AnonymousClass27.this.val$finalProgressView);
                AnonymousClass27 anonymousClass27 = AnonymousClass27.this;
                anonymousClass27.val$finalListView.removeView(anonymousClass27.val$finalProgressView);
            }
        }
    }

    public void onResume() {
        this.scrolling = true;
        SharedPhotoVideoAdapter sharedPhotoVideoAdapter = this.photoVideoAdapter;
        if (sharedPhotoVideoAdapter != null) {
            sharedPhotoVideoAdapter.notifyDataSetChanged();
        }
        SharedDocumentsAdapter sharedDocumentsAdapter = this.documentsAdapter;
        if (sharedDocumentsAdapter != null) {
            sharedDocumentsAdapter.notifyDataSetChanged();
        }
        SharedLinksAdapter sharedLinksAdapter = this.linksAdapter;
        if (sharedLinksAdapter != null) {
            sharedLinksAdapter.notifyDataSetChanged();
        }
        for (int i = 0; i < this.mediaPages.length; i++) {
            fixLayoutInternal(i);
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i < mediaPageArr.length) {
                if (mediaPageArr[i].listView != null) {
                    this.mediaPages[i].listView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass28(i));
                }
                i++;
            } else {
                return;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$28 */
    /* loaded from: classes3.dex */
    public class AnonymousClass28 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ int val$num;

        AnonymousClass28(int i) {
            SharedMediaLayout.this = r1;
            this.val$num = i;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            SharedMediaLayout.this.mediaPages[this.val$num].getViewTreeObserver().removeOnPreDrawListener(this);
            SharedMediaLayout.this.fixLayoutInternal(this.val$num);
            return true;
        }
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            long j = tLRPC$ChatFull.migrated_from_chat_id;
            if (j == 0 || this.mergeDialogId != 0) {
                return;
            }
            this.mergeDialogId = -j;
            int i = 0;
            while (true) {
                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                if (i >= sharedMediaDataArr.length) {
                    return;
                }
                sharedMediaDataArr[i].max_id[1] = this.info.migrated_from_max_id;
                sharedMediaDataArr[i].endReached[1] = false;
                i++;
            }
        }
    }

    public void setChatUsers(ArrayList<Integer> arrayList, TLRPC$ChatFull tLRPC$ChatFull) {
        this.chatUsersAdapter.chatInfo = tLRPC$ChatFull;
        this.chatUsersAdapter.sortedUsers = arrayList;
        updateTabs(true);
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i < mediaPageArr.length) {
                if (mediaPageArr[i].selectedType == 7) {
                    this.mediaPages[i].listView.getAdapter().notifyDataSetChanged();
                }
                i++;
            } else {
                return;
            }
        }
    }

    private void updateRowsSelection() {
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i < mediaPageArr.length) {
                int childCount = mediaPageArr[i].listView.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    View childAt = this.mediaPages[i].listView.getChildAt(i2);
                    if (childAt instanceof SharedDocumentCell) {
                        ((SharedDocumentCell) childAt).setChecked(false, true);
                    } else if (childAt instanceof SharedPhotoVideoCell2) {
                        ((SharedPhotoVideoCell2) childAt).setChecked(false, true);
                    } else if (childAt instanceof SharedLinkCell) {
                        ((SharedLinkCell) childAt).setChecked(false, true);
                    } else if (childAt instanceof SharedAudioCell) {
                        ((SharedAudioCell) childAt).setChecked(false, true);
                    } else if (childAt instanceof ContextLinkCell) {
                        ((ContextLinkCell) childAt).setChecked(false, true);
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void setMergeDialogId(long j) {
        this.mergeDialogId = j;
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x014d, code lost:
        if (r0[6] != 0) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0155, code lost:
        if (r12.chatUsersAdapter.chatInfo != null) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0157, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(0, org.telegram.messenger.LocaleController.getString("SharedMediaTabFull2", 2131628305), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0166, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(0, org.telegram.messenger.LocaleController.getString("SharedMediaTab2", 2131628304), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0178, code lost:
        if (r12.hasMedia[1] <= 0) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0180, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(1) != false) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0182, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(1, org.telegram.messenger.LocaleController.getString("SharedFilesTab2", 2131628297), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x019b, code lost:
        if (org.telegram.messenger.DialogObject.isEncryptedDialog(r12.dialog_id) != false) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01a1, code lost:
        if (r12.hasMedia[3] <= 0) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x01a9, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(3) != false) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01ab, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(3, org.telegram.messenger.LocaleController.getString("SharedLinksTab2", 2131628301), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x01bd, code lost:
        if (r12.hasMedia[4] <= 0) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01c5, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(4) != false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x01c7, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(4, org.telegram.messenger.LocaleController.getString("SharedMusicTab2", 2131628306), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x01d5, code lost:
        if (r12.hasMedia[4] <= 0) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x01dd, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(4) != false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x01df, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(4, org.telegram.messenger.LocaleController.getString("SharedMusicTab2", 2131628306), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x01ec, code lost:
        if (r12.hasMedia[2] <= 0) goto L133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x01f4, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(2) != false) goto L133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x01f6, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(2, org.telegram.messenger.LocaleController.getString("SharedVoiceTab2", 2131628310), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0208, code lost:
        if (r12.hasMedia[5] <= 0) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0210, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(5) != false) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0212, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(5, org.telegram.messenger.LocaleController.getString("SharedGIFsTab2", 2131628298), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0224, code lost:
        if (r12.hasMedia[6] <= 0) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x022c, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(6) != false) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x022e, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(6, org.telegram.messenger.LocaleController.getString("SharedGroupsTab2", 2131628299), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x023c, code lost:
        r13 = r12.scrollSlidingTextTabStrip.getCurrentTabId();
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0242, code lost:
        if (r13 < 0) goto L146;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x0244, code lost:
        r12.mediaPages[0].selectedType = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x024b, code lost:
        r12.scrollSlidingTextTabStrip.finishAddingTabs();
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0250, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0079, code lost:
        if ((r12.hasMedia[4] <= 0) == r12.scrollSlidingTextTabStrip.hasTab(4)) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x008b, code lost:
        if ((r12.hasMedia[4] <= 0) == r12.scrollSlidingTextTabStrip.hasTab(4)) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x008d, code lost:
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0094, code lost:
        if (r12.hasMedia[2] > 0) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0096, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0098, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x009f, code lost:
        if (r3 != r12.scrollSlidingTextTabStrip.hasTab(2)) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00a1, code lost:
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00a8, code lost:
        if (r12.hasMedia[5] > 0) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00aa, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00ac, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b3, code lost:
        if (r3 != r12.scrollSlidingTextTabStrip.hasTab(5)) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b5, code lost:
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00bc, code lost:
        if (r12.hasMedia[6] > 0) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00be, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00c0, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00c7, code lost:
        if (r3 != r12.scrollSlidingTextTabStrip.hasTab(6)) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00c9, code lost:
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x00cb, code lost:
        if (r0 <= 0) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00cd, code lost:
        if (r13 == false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00d3, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00d5, code lost:
        r13 = new android.transition.TransitionSet();
        r13.setOrdering(0);
        r13.addTransition(new android.transition.ChangeBounds());
        r13.addTransition(new org.telegram.ui.Components.SharedMediaLayout.AnonymousClass29(r12));
        r13.setDuration(200L);
        android.transition.TransitionManager.beginDelayedTransition(r12.scrollSlidingTextTabStrip.getTabsContainer(), r13);
        r12.scrollSlidingTextTabStrip.recordIndicatorParams();
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0100, code lost:
        r13 = r12.scrollSlidingTextTabStrip.removeTabs();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0106, code lost:
        if (r0 <= 3) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0108, code lost:
        r13 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x010f, code lost:
        if (r12.chatUsersAdapter.chatInfo == null) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0117, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(7) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0119, code lost:
        r12.scrollSlidingTextTabStrip.addTextTab(7, org.telegram.messenger.LocaleController.getString("GroupMembers", 2131626097), r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x012b, code lost:
        if (r12.hasMedia[0] <= 0) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0133, code lost:
        if (r12.scrollSlidingTextTabStrip.hasTab(0) != false) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0135, code lost:
        r0 = r12.hasMedia;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0139, code lost:
        if (r0[1] != 0) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x013d, code lost:
        if (r0[2] != 0) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0141, code lost:
        if (r0[3] != 0) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0145, code lost:
        if (r0[4] != 0) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0149, code lost:
        if (r0[5] != 0) goto L105;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateTabs(boolean z) {
        if (this.scrollSlidingTextTabStrip == null) {
            return;
        }
        if (!this.delegate.isFragmentOpened()) {
            z = false;
        }
        int i = (this.chatUsersAdapter.chatInfo == null) == this.scrollSlidingTextTabStrip.hasTab(7) ? 1 : 0;
        if ((this.hasMedia[0] <= 0) == this.scrollSlidingTextTabStrip.hasTab(0)) {
            i++;
        }
        if ((this.hasMedia[1] <= 0) == this.scrollSlidingTextTabStrip.hasTab(1)) {
            i++;
        }
        if (!DialogObject.isEncryptedDialog(this.dialog_id)) {
            if ((this.hasMedia[3] <= 0) == this.scrollSlidingTextTabStrip.hasTab(3)) {
                i++;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$29 */
    /* loaded from: classes3.dex */
    public class AnonymousClass29 extends Visibility {
        AnonymousClass29(SharedMediaLayout sharedMediaLayout) {
        }

        @Override // android.transition.Visibility
        public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_X, 0.5f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.5f, 1.0f));
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            return animatorSet;
        }

        @Override // android.transition.Visibility
        public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 0.0f), ObjectAnimator.ofFloat(view, View.SCALE_X, view.getScaleX(), 0.5f), ObjectAnimator.ofFloat(view, View.SCALE_Y, view.getScaleX(), 0.5f));
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            return animatorSet;
        }
    }

    public void startStopVisibleGifs() {
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i < mediaPageArr.length) {
                int childCount = mediaPageArr[i].listView.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    View childAt = this.mediaPages[i].listView.getChildAt(i2);
                    if (childAt instanceof ContextLinkCell) {
                        ImageReceiver photoImage = ((ContextLinkCell) childAt).getPhotoImage();
                        if (i == 0) {
                            photoImage.setAllowStartAnimation(true);
                            photoImage.startAnimation();
                        } else {
                            photoImage.setAllowStartAnimation(false);
                            photoImage.stopAnimation();
                        }
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void switchToCurrentSelectedMode(boolean z) {
        MediaPage[] mediaPageArr;
        boolean z2;
        int i;
        int i2;
        GroupUsersSearchAdapter groupUsersSearchAdapter;
        int i3 = 0;
        while (true) {
            mediaPageArr = this.mediaPages;
            if (i3 >= mediaPageArr.length) {
                break;
            }
            mediaPageArr[i3].listView.stopScroll();
            i3++;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mediaPageArr[z ? 1 : 0].getLayoutParams();
        RecyclerView.Adapter adapter = this.mediaPages[z].listView.getAdapter();
        RecyclerView.RecycledViewPool recycledViewPool = null;
        int i4 = 100;
        if (!this.searching || !this.searchWas) {
            this.mediaPages[z].listView.setPinnedHeaderShadowDrawable(null);
            if (this.mediaPages[z].selectedType != 0) {
                if (this.mediaPages[z].selectedType != 1) {
                    if (this.mediaPages[z].selectedType != 2) {
                        if (this.mediaPages[z].selectedType != 3) {
                            if (this.mediaPages[z].selectedType != 4) {
                                if (this.mediaPages[z].selectedType != 5) {
                                    if (this.mediaPages[z].selectedType != 6) {
                                        if (this.mediaPages[z].selectedType == 7 && adapter != this.chatUsersAdapter) {
                                            recycleAdapter(adapter);
                                            this.mediaPages[z].listView.setAdapter(this.chatUsersAdapter);
                                        }
                                    } else if (adapter != this.commonGroupsAdapter) {
                                        recycleAdapter(adapter);
                                        this.mediaPages[z].listView.setAdapter(this.commonGroupsAdapter);
                                    }
                                } else if (adapter != this.gifAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z].listView.setAdapter(this.gifAdapter);
                                }
                            } else {
                                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                                z2 = sharedMediaDataArr[4].fastScrollDataLoaded && !sharedMediaDataArr[4].fastScrollPeriods.isEmpty();
                                if (adapter != this.audioAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z].listView.setAdapter(this.audioAdapter);
                                }
                            }
                        } else if (adapter != this.linksAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z].listView.setAdapter(this.linksAdapter);
                        }
                        z2 = false;
                    } else {
                        SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                        z2 = sharedMediaDataArr2[2].fastScrollDataLoaded && !sharedMediaDataArr2[2].fastScrollPeriods.isEmpty();
                        if (adapter != this.voiceAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z].listView.setAdapter(this.voiceAdapter);
                        }
                    }
                } else {
                    SharedMediaData[] sharedMediaDataArr3 = this.sharedMediaData;
                    z2 = sharedMediaDataArr3[1].fastScrollDataLoaded && !sharedMediaDataArr3[1].fastScrollPeriods.isEmpty();
                    if (adapter != this.documentsAdapter) {
                        recycleAdapter(adapter);
                        this.mediaPages[z].listView.setAdapter(this.documentsAdapter);
                    }
                }
                i = 100;
            } else {
                if (adapter != this.photoVideoAdapter) {
                    recycleAdapter(adapter);
                    this.mediaPages[z].listView.setAdapter(this.photoVideoAdapter);
                }
                int i5 = -AndroidUtilities.dp(1.0f);
                layoutParams.rightMargin = i5;
                layoutParams.leftMargin = i5;
                SharedMediaData[] sharedMediaDataArr4 = this.sharedMediaData;
                z2 = sharedMediaDataArr4[0].fastScrollDataLoaded && !sharedMediaDataArr4[0].fastScrollPeriods.isEmpty();
                i = this.mediaColumnsCount;
                this.mediaPages[z].listView.setPinnedHeaderShadowDrawable(this.pinnedHeaderShadowDrawable);
                SharedMediaData[] sharedMediaDataArr5 = this.sharedMediaData;
                if (sharedMediaDataArr5[0].recycledViewPool == null) {
                    sharedMediaDataArr5[0].recycledViewPool = new RecyclerView.RecycledViewPool();
                }
                recycledViewPool = this.sharedMediaData[0].recycledViewPool;
            }
            if (this.mediaPages[z].selectedType == 0 || this.mediaPages[z].selectedType == 2 || this.mediaPages[z].selectedType == 5 || this.mediaPages[z].selectedType == 6 || (this.mediaPages[z].selectedType == 7 && !this.delegate.canSearchMembers())) {
                if (z != 0) {
                    this.searchItemState = 2;
                } else {
                    this.searchItemState = 0;
                    this.searchItem.setVisibility(4);
                }
            } else if (z != 0) {
                if (this.searchItem.getVisibility() == 4 && !this.actionBar.isSearchFieldVisible()) {
                    if (canShowSearchItem()) {
                        this.searchItemState = 1;
                        this.searchItem.setVisibility(0);
                    } else {
                        this.searchItem.setVisibility(4);
                    }
                    this.searchItem.setAlpha(0.0f);
                } else {
                    this.searchItemState = 0;
                }
            } else if (this.searchItem.getVisibility() == 4) {
                if (canShowSearchItem()) {
                    this.searchItemState = 0;
                    this.searchItem.setAlpha(1.0f);
                    this.searchItem.setVisibility(0);
                } else {
                    this.searchItem.setVisibility(4);
                    this.searchItem.setAlpha(0.0f);
                }
            }
            if (this.mediaPages[z].selectedType != 6) {
                if (this.mediaPages[z].selectedType != 7 && !this.sharedMediaData[this.mediaPages[z].selectedType].loading && !this.sharedMediaData[this.mediaPages[z].selectedType].endReached[0] && this.sharedMediaData[this.mediaPages[z].selectedType].messages.isEmpty()) {
                    this.sharedMediaData[this.mediaPages[z].selectedType].loading = true;
                    this.documentsAdapter.notifyDataSetChanged();
                    int i6 = this.mediaPages[z].selectedType;
                    if (i6 == 0) {
                        SharedMediaData[] sharedMediaDataArr6 = this.sharedMediaData;
                        if (sharedMediaDataArr6[0].filterType == 1) {
                            i2 = 6;
                        } else if (sharedMediaDataArr6[0].filterType == 2) {
                            i2 = 7;
                        }
                        this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, 0, 0, i2, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[this.mediaPages[z].selectedType].requestIndex);
                    }
                    i2 = i6;
                    this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, 0, 0, i2, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[this.mediaPages[z].selectedType].requestIndex);
                }
            } else if (!this.commonGroupsAdapter.loading && !this.commonGroupsAdapter.endReached && this.commonGroupsAdapter.chats.isEmpty()) {
                this.commonGroupsAdapter.getChats(0L, 100);
            }
            this.mediaPages[z].listView.setVisibility(0);
            i4 = i;
        } else {
            if (z != 0) {
                if (this.mediaPages[z].selectedType == 0 || this.mediaPages[z].selectedType == 2 || this.mediaPages[z].selectedType == 5 || this.mediaPages[z].selectedType == 6 || (this.mediaPages[z].selectedType == 7 && !this.delegate.canSearchMembers())) {
                    this.searching = false;
                    this.searchWas = false;
                    switchToCurrentSelectedMode(true);
                    return;
                }
                String obj = this.searchItem.getSearchField().getText().toString();
                if (this.mediaPages[z].selectedType != 1) {
                    if (this.mediaPages[z].selectedType != 3) {
                        if (this.mediaPages[z].selectedType != 4) {
                            if (this.mediaPages[z].selectedType == 7 && (groupUsersSearchAdapter = this.groupUsersSearchAdapter) != null) {
                                groupUsersSearchAdapter.search(obj, false);
                                if (adapter != this.groupUsersSearchAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z].listView.setAdapter(this.groupUsersSearchAdapter);
                                }
                            }
                        } else {
                            MediaSearchAdapter mediaSearchAdapter = this.audioSearchAdapter;
                            if (mediaSearchAdapter != null) {
                                mediaSearchAdapter.search(obj, false);
                                if (adapter != this.audioSearchAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z].listView.setAdapter(this.audioSearchAdapter);
                                }
                            }
                        }
                    } else {
                        MediaSearchAdapter mediaSearchAdapter2 = this.linksSearchAdapter;
                        if (mediaSearchAdapter2 != null) {
                            mediaSearchAdapter2.search(obj, false);
                            if (adapter != this.linksSearchAdapter) {
                                recycleAdapter(adapter);
                                this.mediaPages[z].listView.setAdapter(this.linksSearchAdapter);
                            }
                        }
                    }
                } else {
                    MediaSearchAdapter mediaSearchAdapter3 = this.documentsSearchAdapter;
                    if (mediaSearchAdapter3 != null) {
                        mediaSearchAdapter3.search(obj, false);
                        if (adapter != this.documentsSearchAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z].listView.setAdapter(this.documentsSearchAdapter);
                        }
                    }
                }
            } else if (this.mediaPages[z].listView != null) {
                if (this.mediaPages[z].selectedType != 1) {
                    if (this.mediaPages[z].selectedType != 3) {
                        if (this.mediaPages[z].selectedType != 4) {
                            if (this.mediaPages[z].selectedType == 7) {
                                if (adapter != this.groupUsersSearchAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z].listView.setAdapter(this.groupUsersSearchAdapter);
                                }
                                this.groupUsersSearchAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (adapter != this.audioSearchAdapter) {
                                recycleAdapter(adapter);
                                this.mediaPages[z].listView.setAdapter(this.audioSearchAdapter);
                            }
                            this.audioSearchAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (adapter != this.linksSearchAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z].listView.setAdapter(this.linksSearchAdapter);
                        }
                        this.linksSearchAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (adapter != this.documentsSearchAdapter) {
                        recycleAdapter(adapter);
                        this.mediaPages[z].listView.setAdapter(this.documentsSearchAdapter);
                    }
                    this.documentsSearchAdapter.notifyDataSetChanged();
                }
            }
            z2 = false;
        }
        MediaPage[] mediaPageArr2 = this.mediaPages;
        mediaPageArr2[z].fastScrollEnabled = z2;
        updateFastScrollVisibility(mediaPageArr2[z], false);
        this.mediaPages[z].layoutManager.setSpanCount(i4);
        this.mediaPages[z].listView.setRecycledViewPool(recycledViewPool);
        this.mediaPages[z].animationSupportingListView.setRecycledViewPool(recycledViewPool);
        if (this.searchItemState != 2 || !this.actionBar.isSearchFieldVisible()) {
            return;
        }
        this.ignoreSearchCollapse = true;
        this.actionBar.closeSearchField();
        this.searchItemState = 0;
        this.searchItem.setAlpha(0.0f);
        this.searchItem.setVisibility(4);
    }

    private boolean onItemLongClick(MessageObject messageObject, View view, int i) {
        if (this.isActionModeShowed || this.profileActivity.getParentActivity() == null || messageObject == null) {
            return false;
        }
        AndroidUtilities.hideKeyboard(this.profileActivity.getParentActivity().getCurrentFocus());
        this.selectedFiles[messageObject.getDialogId() == this.dialog_id ? (char) 0 : (char) 1].put(messageObject.getId(), messageObject);
        if (!messageObject.canDeleteMessage(false, null)) {
            this.cantDeleteMessagesCount++;
        }
        this.deleteItem.setVisibility(this.cantDeleteMessagesCount == 0 ? 0 : 8);
        ActionBarMenuItem actionBarMenuItem = this.gotoItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setVisibility(0);
        }
        this.selectedMessagesCountTextView.setNumber(1, false);
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.actionModeViews.size(); i2++) {
            View view2 = this.actionModeViews.get(i2);
            AndroidUtilities.clearDrawableAnimation(view2);
            arrayList.add(ObjectAnimator.ofFloat(view2, View.SCALE_Y, 0.1f, 1.0f));
        }
        animatorSet.playTogether(arrayList);
        animatorSet.setDuration(250L);
        animatorSet.start();
        this.scrolling = false;
        if (view instanceof SharedDocumentCell) {
            ((SharedDocumentCell) view).setChecked(true, true);
        } else if (view instanceof SharedPhotoVideoCell) {
            ((SharedPhotoVideoCell) view).setChecked(i, true, true);
        } else if (view instanceof SharedLinkCell) {
            ((SharedLinkCell) view).setChecked(true, true);
        } else if (view instanceof SharedAudioCell) {
            ((SharedAudioCell) view).setChecked(true, true);
        } else if (view instanceof ContextLinkCell) {
            ((ContextLinkCell) view).setChecked(true, true);
        } else if (view instanceof SharedPhotoVideoCell2) {
            ((SharedPhotoVideoCell2) view).setChecked(true, true);
        }
        if (!this.isActionModeShowed) {
            showActionMode(true);
        }
        updateForwardItem();
        return true;
    }

    private void onItemClick(int i, View view, MessageObject messageObject, int i2, int i3) {
        int i4;
        if (messageObject == null || this.photoVideoChangeColumnsAnimation) {
            return;
        }
        String str = null;
        boolean z = false;
        if (this.isActionModeShowed) {
            char c = messageObject.getDialogId() == this.dialog_id ? (char) 0 : (char) 1;
            if (this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0) {
                this.selectedFiles[c].remove(messageObject.getId());
                if (!messageObject.canDeleteMessage(false, null)) {
                    this.cantDeleteMessagesCount--;
                }
            } else if (this.selectedFiles[0].size() + this.selectedFiles[1].size() >= 100) {
                return;
            } else {
                this.selectedFiles[c].put(messageObject.getId(), messageObject);
                if (!messageObject.canDeleteMessage(false, null)) {
                    this.cantDeleteMessagesCount++;
                }
            }
            if (this.selectedFiles[0].size() == 0 && this.selectedFiles[1].size() == 0) {
                showActionMode(false);
            } else {
                this.selectedMessagesCountTextView.setNumber(this.selectedFiles[0].size() + this.selectedFiles[1].size(), true);
                int i5 = 8;
                this.deleteItem.setVisibility(this.cantDeleteMessagesCount == 0 ? 0 : 8);
                ActionBarMenuItem actionBarMenuItem = this.gotoItem;
                if (actionBarMenuItem != null) {
                    if (this.selectedFiles[0].size() == 1) {
                        i5 = 0;
                    }
                    actionBarMenuItem.setVisibility(i5);
                }
            }
            this.scrolling = false;
            if (view instanceof SharedDocumentCell) {
                SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) view;
                if (this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0) {
                    z = true;
                }
                sharedDocumentCell.setChecked(z, true);
            } else if (view instanceof SharedPhotoVideoCell) {
                SharedPhotoVideoCell sharedPhotoVideoCell = (SharedPhotoVideoCell) view;
                if (this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0) {
                    i4 = i2;
                    z = true;
                } else {
                    i4 = i2;
                }
                sharedPhotoVideoCell.setChecked(i4, z, true);
            } else if (view instanceof SharedLinkCell) {
                SharedLinkCell sharedLinkCell = (SharedLinkCell) view;
                if (this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0) {
                    z = true;
                }
                sharedLinkCell.setChecked(z, true);
            } else if (view instanceof SharedAudioCell) {
                SharedAudioCell sharedAudioCell = (SharedAudioCell) view;
                if (this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0) {
                    z = true;
                }
                sharedAudioCell.setChecked(z, true);
            } else if (view instanceof ContextLinkCell) {
                ContextLinkCell contextLinkCell = (ContextLinkCell) view;
                if (this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0) {
                    z = true;
                }
                contextLinkCell.setChecked(z, true);
            } else if (view instanceof SharedPhotoVideoCell2) {
                SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) view;
                if (this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0) {
                    z = true;
                }
                sharedPhotoVideoCell2.setChecked(z, true);
            }
        } else if (i3 == 0) {
            int i6 = i - this.sharedMediaData[i3].startOffset;
            if (i6 >= 0 && i6 < this.sharedMediaData[i3].messages.size()) {
                PhotoViewer.getInstance().setParentActivity(this.profileActivity.getParentActivity());
                PhotoViewer.getInstance().openPhoto(this.sharedMediaData[i3].messages, i6, this.dialog_id, this.mergeDialogId, this.provider);
            }
        } else if (i3 == 2 || i3 == 4) {
            if (view instanceof SharedAudioCell) {
                ((SharedAudioCell) view).didPressedButton();
            }
        } else if (i3 == 5) {
            PhotoViewer.getInstance().setParentActivity(this.profileActivity.getParentActivity());
            int indexOf = this.sharedMediaData[i3].messages.indexOf(messageObject);
            if (indexOf < 0) {
                ArrayList<MessageObject> arrayList = new ArrayList<>();
                arrayList.add(messageObject);
                PhotoViewer.getInstance().openPhoto(arrayList, 0, 0L, 0L, this.provider);
            } else {
                PhotoViewer.getInstance().openPhoto(this.sharedMediaData[i3].messages, indexOf, this.dialog_id, this.mergeDialogId, this.provider);
            }
        } else if (i3 == 1) {
            if (view instanceof SharedDocumentCell) {
                SharedDocumentCell sharedDocumentCell2 = (SharedDocumentCell) view;
                TLRPC$Document document = messageObject.getDocument();
                if (sharedDocumentCell2.isLoaded()) {
                    if (messageObject.canPreviewDocument()) {
                        PhotoViewer.getInstance().setParentActivity(this.profileActivity.getParentActivity());
                        int indexOf2 = this.sharedMediaData[i3].messages.indexOf(messageObject);
                        if (indexOf2 < 0) {
                            ArrayList<MessageObject> arrayList2 = new ArrayList<>();
                            arrayList2.add(messageObject);
                            PhotoViewer.getInstance().openPhoto(arrayList2, 0, 0L, 0L, this.provider);
                            return;
                        }
                        PhotoViewer.getInstance().openPhoto(this.sharedMediaData[i3].messages, indexOf2, this.dialog_id, this.mergeDialogId, this.provider);
                        return;
                    }
                    AndroidUtilities.openDocument(messageObject, this.profileActivity.getParentActivity(), this.profileActivity);
                } else if (!sharedDocumentCell2.isLoading()) {
                    MessageObject message = sharedDocumentCell2.getMessage();
                    message.putInDownloadsStore = true;
                    this.profileActivity.getFileLoader().loadFile(document, message, 0, 0);
                    sharedDocumentCell2.updateFileExistIcon(true);
                } else {
                    this.profileActivity.getFileLoader().cancelLoadFile(document);
                    sharedDocumentCell2.updateFileExistIcon(true);
                }
            }
        } else if (i3 == 3) {
            try {
                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                TLRPC$WebPage tLRPC$WebPage = tLRPC$MessageMedia != null ? tLRPC$MessageMedia.webpage : null;
                if (tLRPC$WebPage != null && !(tLRPC$WebPage instanceof TLRPC$TL_webPageEmpty)) {
                    if (tLRPC$WebPage.cached_page != null) {
                        ArticleViewer.getInstance().setParentActivity(this.profileActivity.getParentActivity(), this.profileActivity);
                        ArticleViewer.getInstance().open(messageObject);
                        return;
                    }
                    String str2 = tLRPC$WebPage.embed_url;
                    if (str2 != null && str2.length() != 0) {
                        openWebView(tLRPC$WebPage, messageObject);
                        return;
                    }
                    str = tLRPC$WebPage.url;
                }
                if (str == null) {
                    str = ((SharedLinkCell) view).getLink(0);
                }
                if (str != null) {
                    openUrl(str);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        updateForwardItem();
    }

    public void openUrl(String str) {
        if (AndroidUtilities.shouldShowUrlInAlert(str)) {
            AlertsCreator.showOpenUrlAlert(this.profileActivity, str, true, true);
        } else {
            Browser.openUrl(this.profileActivity.getParentActivity(), str);
        }
    }

    public void openWebView(TLRPC$WebPage tLRPC$WebPage, MessageObject messageObject) {
        EmbedBottomSheet.show(this.profileActivity.getParentActivity(), messageObject, this.provider, tLRPC$WebPage.site_name, tLRPC$WebPage.description, tLRPC$WebPage.url, tLRPC$WebPage.embed_url, tLRPC$WebPage.embed_width, tLRPC$WebPage.embed_height, false);
    }

    private void recycleAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof SharedPhotoVideoAdapter) {
            this.cellCache.addAll(this.cache);
            this.cache.clear();
        } else if (adapter != this.audioAdapter) {
        } else {
            this.audioCellCache.addAll(this.audioCache);
            this.audioCache.clear();
        }
    }

    public void fixLayoutInternal(int i) {
        ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
        if (i == 0) {
            if (!AndroidUtilities.isTablet() && ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 2) {
                this.selectedMessagesCountTextView.setTextSize(18);
            } else {
                this.selectedMessagesCountTextView.setTextSize(20);
            }
        }
        if (i == 0) {
            this.photoVideoAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: org.telegram.ui.Components.SharedMediaLayout$30 */
    /* loaded from: classes3.dex */
    public class AnonymousClass30 implements SharedLinkCell.SharedLinkCellDelegate {
        AnonymousClass30() {
            SharedMediaLayout.this = r1;
        }

        @Override // org.telegram.ui.Cells.SharedLinkCell.SharedLinkCellDelegate
        public void needOpenWebView(TLRPC$WebPage tLRPC$WebPage, MessageObject messageObject) {
            SharedMediaLayout.this.openWebView(tLRPC$WebPage, messageObject);
        }

        @Override // org.telegram.ui.Cells.SharedLinkCell.SharedLinkCellDelegate
        public boolean canPerformActions() {
            return !SharedMediaLayout.this.isActionModeShowed;
        }

        @Override // org.telegram.ui.Cells.SharedLinkCell.SharedLinkCellDelegate
        public void onLinkPress(String str, boolean z) {
            if (!z) {
                SharedMediaLayout.this.openUrl(str);
                return;
            }
            BottomSheet.Builder builder = new BottomSheet.Builder(SharedMediaLayout.this.profileActivity.getParentActivity());
            builder.setTitle(str);
            builder.setItems(new CharSequence[]{LocaleController.getString("Open", 2131627090), LocaleController.getString("Copy", 2131625256)}, new SharedMediaLayout$30$$ExternalSyntheticLambda0(this, str));
            SharedMediaLayout.this.profileActivity.showDialog(builder.create());
        }

        public /* synthetic */ void lambda$onLinkPress$0(String str, DialogInterface dialogInterface, int i) {
            if (i == 0) {
                SharedMediaLayout.this.openUrl(str);
            } else if (i != 1) {
            } else {
                if (str.startsWith("mailto:")) {
                    str = str.substring(7);
                } else if (str.startsWith("tel:")) {
                    str = str.substring(4);
                }
                AndroidUtilities.addToClipboard(str);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class SharedLinksAdapter extends RecyclerListView.SectionsAdapter {
        private Context mContext;

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public Object getItem(int i, int i2) {
            return null;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            return null;
        }

        public SharedLinksAdapter(Context context) {
            SharedMediaLayout.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder, int i, int i2) {
            if (SharedMediaLayout.this.sharedMediaData[3].sections.size() != 0 || SharedMediaLayout.this.sharedMediaData[3].loading) {
                return i == 0 || i2 != 0;
            }
            return false;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getSectionCount() {
            int i = 1;
            if (SharedMediaLayout.this.sharedMediaData[3].sections.size() != 0 || SharedMediaLayout.this.sharedMediaData[3].loading) {
                int size = SharedMediaLayout.this.sharedMediaData[3].sections.size();
                if (SharedMediaLayout.this.sharedMediaData[3].sections.isEmpty() || (SharedMediaLayout.this.sharedMediaData[3].endReached[0] && SharedMediaLayout.this.sharedMediaData[3].endReached[1])) {
                    i = 0;
                }
                return size + i;
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getCountForSection(int i) {
            int i2 = 1;
            if ((SharedMediaLayout.this.sharedMediaData[3].sections.size() != 0 || SharedMediaLayout.this.sharedMediaData[3].loading) && i < SharedMediaLayout.this.sharedMediaData[3].sections.size()) {
                int size = SharedMediaLayout.this.sharedMediaData[3].sectionArrays.get(SharedMediaLayout.this.sharedMediaData[3].sections.get(i)).size();
                if (i == 0) {
                    i2 = 0;
                }
                return size + i2;
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public View getSectionHeaderView(int i, View view) {
            if (view == null) {
                view = new GraySectionCell(this.mContext);
                view.setBackgroundColor(SharedMediaLayout.this.getThemedColor("graySection") & (-218103809));
            }
            if (i != 0) {
                if (i < SharedMediaLayout.this.sharedMediaData[3].sections.size()) {
                    view.setAlpha(1.0f);
                    ((GraySectionCell) view).setText(LocaleController.formatSectionDate(SharedMediaLayout.this.sharedMediaData[3].sectionArrays.get(SharedMediaLayout.this.sharedMediaData[3].sections.get(i)).get(0).messageOwner.date));
                }
            } else {
                view.setAlpha(0.0f);
            }
            return view;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            GraySectionCell graySectionCell;
            if (i == 0) {
                graySectionCell = new GraySectionCell(this.mContext, SharedMediaLayout.this.resourcesProvider);
            } else if (i == 1) {
                SharedLinkCell sharedLinkCell = new SharedLinkCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
                sharedLinkCell.setDelegate(SharedMediaLayout.this.sharedLinkCellDelegate);
                graySectionCell = sharedLinkCell;
            } else if (i == 3) {
                View createEmptyStubView = SharedMediaLayout.createEmptyStubView(this.mContext, 3, SharedMediaLayout.this.dialog_id, SharedMediaLayout.this.resourcesProvider);
                createEmptyStubView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                return new RecyclerListView.Holder(createEmptyStubView);
            } else {
                FlickerLoadingView flickerLoadingView = new FlickerLoadingView(this.mContext, SharedMediaLayout.this.resourcesProvider);
                flickerLoadingView.setIsSingleCell(true);
                flickerLoadingView.showDate(false);
                flickerLoadingView.setViewType(5);
                graySectionCell = flickerLoadingView;
            }
            graySectionCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(graySectionCell);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public void onBindViewHolder(int i, int i2, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 2 || viewHolder.getItemViewType() == 3) {
                return;
            }
            ArrayList<MessageObject> arrayList = SharedMediaLayout.this.sharedMediaData[3].sectionArrays.get(SharedMediaLayout.this.sharedMediaData[3].sections.get(i));
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            if (itemViewType == 0) {
                ((GraySectionCell) viewHolder.itemView).setText(LocaleController.formatSectionDate(arrayList.get(0).messageOwner.date));
            } else if (itemViewType != 1) {
            } else {
                if (i != 0) {
                    i2--;
                }
                SharedLinkCell sharedLinkCell = (SharedLinkCell) viewHolder.itemView;
                MessageObject messageObject = arrayList.get(i2);
                sharedLinkCell.setLink(messageObject, i2 != arrayList.size() - 1 || (i == SharedMediaLayout.this.sharedMediaData[3].sections.size() - 1 && SharedMediaLayout.this.sharedMediaData[3].loading));
                if (SharedMediaLayout.this.isActionModeShowed) {
                    if (SharedMediaLayout.this.selectedFiles[messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0) {
                        z = true;
                    }
                    sharedLinkCell.setChecked(z, !SharedMediaLayout.this.scrolling);
                    return;
                }
                sharedLinkCell.setChecked(false, !SharedMediaLayout.this.scrolling);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getItemViewType(int i, int i2) {
            if (SharedMediaLayout.this.sharedMediaData[3].sections.size() != 0 || SharedMediaLayout.this.sharedMediaData[3].loading) {
                if (i >= SharedMediaLayout.this.sharedMediaData[3].sections.size()) {
                    return 2;
                }
                return (i == 0 || i2 != 0) ? 1 : 0;
            }
            return 3;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            iArr[0] = 0;
            iArr[1] = 0;
        }
    }

    /* loaded from: classes3.dex */
    public class SharedDocumentsAdapter extends RecyclerListView.FastScrollAdapter {
        private int currentType;
        private boolean inFastScrollMode;
        private Context mContext;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public SharedDocumentsAdapter(Context context, int i) {
            SharedMediaLayout.this = r1;
            this.mContext = context;
            this.currentType = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].loadingAfterFastScroll) {
                return SharedMediaLayout.this.sharedMediaData[this.currentType].totalCount;
            }
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].messages.size() == 0 && !SharedMediaLayout.this.sharedMediaData[this.currentType].loading) {
                return 1;
            }
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].messages.size() == 0 && ((!SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[0] || !SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[1]) && SharedMediaLayout.this.sharedMediaData[this.currentType].startReached)) {
                return 0;
            }
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].totalCount == 0) {
                int startOffset = SharedMediaLayout.this.sharedMediaData[this.currentType].getStartOffset() + SharedMediaLayout.this.sharedMediaData[this.currentType].getMessages().size();
                return startOffset != 0 ? (!SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[0] || !SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[1]) ? SharedMediaLayout.this.sharedMediaData[this.currentType].getEndLoadingStubs() != 0 ? startOffset + SharedMediaLayout.this.sharedMediaData[this.currentType].getEndLoadingStubs() : startOffset + 1 : startOffset : startOffset;
            }
            return SharedMediaLayout.this.sharedMediaData[this.currentType].totalCount;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SharedAudioCell sharedAudioCell;
            AnonymousClass1 anonymousClass1;
            if (i == 1) {
                SharedDocumentCell sharedDocumentCell = new SharedDocumentCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
                sharedDocumentCell.setGlobalGradientView(SharedMediaLayout.this.globalGradientView);
                sharedAudioCell = sharedDocumentCell;
            } else if (i == 2) {
                FlickerLoadingView flickerLoadingView = new FlickerLoadingView(this.mContext, SharedMediaLayout.this.resourcesProvider);
                if (this.currentType == 2) {
                    flickerLoadingView.setViewType(4);
                } else {
                    flickerLoadingView.setViewType(3);
                }
                flickerLoadingView.showDate(false);
                flickerLoadingView.setIsSingleCell(true);
                flickerLoadingView.setGlobalGradientView(SharedMediaLayout.this.globalGradientView);
                sharedAudioCell = flickerLoadingView;
            } else if (i == 4) {
                View createEmptyStubView = SharedMediaLayout.createEmptyStubView(this.mContext, this.currentType, SharedMediaLayout.this.dialog_id, SharedMediaLayout.this.resourcesProvider);
                createEmptyStubView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                return new RecyclerListView.Holder(createEmptyStubView);
            } else {
                if (this.currentType == 4 && !SharedMediaLayout.this.audioCellCache.isEmpty()) {
                    View view = (View) SharedMediaLayout.this.audioCellCache.get(0);
                    SharedMediaLayout.this.audioCellCache.remove(0);
                    ViewGroup viewGroup2 = (ViewGroup) view.getParent();
                    anonymousClass1 = view;
                    if (viewGroup2 != null) {
                        viewGroup2.removeView(view);
                        anonymousClass1 = view;
                    }
                } else {
                    anonymousClass1 = new AnonymousClass1(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
                }
                SharedAudioCell sharedAudioCell2 = anonymousClass1;
                sharedAudioCell2.setGlobalGradientView(SharedMediaLayout.this.globalGradientView);
                sharedAudioCell = anonymousClass1;
                if (this.currentType == 4) {
                    SharedMediaLayout.this.audioCache.add(sharedAudioCell2);
                    sharedAudioCell = anonymousClass1;
                }
            }
            sharedAudioCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(sharedAudioCell);
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$SharedDocumentsAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends SharedAudioCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
                super(context, i, resourcesProvider);
                SharedDocumentsAdapter.this = r1;
            }

            @Override // org.telegram.ui.Cells.SharedAudioCell
            public boolean needPlayMessage(MessageObject messageObject) {
                if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                    boolean playMessage = MediaController.getInstance().playMessage(messageObject);
                    MediaController.getInstance().setVoiceMessagesPlaylist(playMessage ? SharedMediaLayout.this.sharedMediaData[SharedDocumentsAdapter.this.currentType].messages : null, false);
                    return playMessage;
                } else if (!messageObject.isMusic()) {
                    return false;
                } else {
                    return MediaController.getInstance().setPlaylist(SharedMediaLayout.this.sharedMediaData[SharedDocumentsAdapter.this.currentType].messages, messageObject, SharedMediaLayout.this.mergeDialogId);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ArrayList<MessageObject> arrayList = SharedMediaLayout.this.sharedMediaData[this.currentType].messages;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            if (itemViewType == 1) {
                SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) viewHolder.itemView;
                MessageObject messageObject = arrayList.get(i - SharedMediaLayout.this.sharedMediaData[this.currentType].startOffset);
                sharedDocumentCell.setDocument(messageObject, i != arrayList.size() - 1);
                if (SharedMediaLayout.this.isActionModeShowed) {
                    if (SharedMediaLayout.this.selectedFiles[messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0) {
                        z = true;
                    }
                    sharedDocumentCell.setChecked(z, !SharedMediaLayout.this.scrolling);
                    return;
                }
                sharedDocumentCell.setChecked(false, !SharedMediaLayout.this.scrolling);
            } else if (itemViewType != 3) {
            } else {
                SharedAudioCell sharedAudioCell = (SharedAudioCell) viewHolder.itemView;
                MessageObject messageObject2 = arrayList.get(i - SharedMediaLayout.this.sharedMediaData[this.currentType].startOffset);
                sharedAudioCell.setMessageObject(messageObject2, i != arrayList.size() - 1);
                if (SharedMediaLayout.this.isActionModeShowed) {
                    if (SharedMediaLayout.this.selectedFiles[messageObject2.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(messageObject2.getId()) >= 0) {
                        z = true;
                    }
                    sharedAudioCell.setChecked(z, !SharedMediaLayout.this.scrolling);
                    return;
                }
                sharedAudioCell.setChecked(false, !SharedMediaLayout.this.scrolling);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].sections.size() != 0 || SharedMediaLayout.this.sharedMediaData[this.currentType].loading) {
                if (i < SharedMediaLayout.this.sharedMediaData[this.currentType].startOffset || i >= SharedMediaLayout.this.sharedMediaData[this.currentType].startOffset + SharedMediaLayout.this.sharedMediaData[this.currentType].messages.size()) {
                    return 2;
                }
                int i2 = this.currentType;
                return (i2 == 2 || i2 == 4) ? 3 : 1;
            }
            return 4;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].fastScrollPeriods == null) {
                return "";
            }
            ArrayList<Period> arrayList = SharedMediaLayout.this.sharedMediaData[this.currentType].fastScrollPeriods;
            if (arrayList.isEmpty()) {
                return "";
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (i <= arrayList.get(i2).startOffset) {
                    return arrayList.get(i2).formatedDate;
                }
            }
            return arrayList.get(arrayList.size() - 1).formatedDate;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            int measuredHeight = recyclerListView.getChildAt(0).getMeasuredHeight();
            float totalItemsCount = f * ((getTotalItemsCount() * measuredHeight) - (recyclerListView.getMeasuredHeight() - recyclerListView.getPaddingTop()));
            iArr[0] = (int) (totalItemsCount / measuredHeight);
            iArr[1] = ((int) totalItemsCount) % measuredHeight;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void onStartFastScroll() {
            this.inFastScrollMode = true;
            MediaPage mediaPage = SharedMediaLayout.this.getMediaPage(this.currentType);
            if (mediaPage != null) {
                SharedMediaLayout.showFastScrollHint(mediaPage, null, false);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void onFinishFastScroll(RecyclerListView recyclerListView) {
            if (this.inFastScrollMode) {
                this.inFastScrollMode = false;
                if (recyclerListView == null) {
                    return;
                }
                int i = 0;
                for (int i2 = 0; i2 < recyclerListView.getChildCount(); i2++) {
                    i = SharedMediaLayout.this.getMessageId(recyclerListView.getChildAt(i2));
                    if (i != 0) {
                        break;
                    }
                }
                if (i != 0) {
                    return;
                }
                SharedMediaLayout.this.findPeriodAndJumpToDate(this.currentType, recyclerListView, true);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public int getTotalItemsCount() {
            return SharedMediaLayout.this.sharedMediaData[this.currentType].totalCount;
        }
    }

    public static View createEmptyStubView(Context context, int i, long j, Theme.ResourcesProvider resourcesProvider) {
        EmptyStubView emptyStubView = new EmptyStubView(context, resourcesProvider);
        if (i == 0) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoMediaSecret", 2131626834));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoMedia", 2131626832));
            }
        } else if (i == 1) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedFilesSecret", 2131626866));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedFiles", 2131626865));
            }
        } else if (i == 2) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedVoiceSecret", 2131626871));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedVoice", 2131626870));
            }
        } else if (i == 3) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedLinksSecret", 2131626869));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedLinks", 2131626868));
            }
        } else if (i == 4) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedAudioSecret", 2131626864));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedAudio", 2131626863));
            }
        } else if (i == 5) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedGifSecret", 2131626867));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoGIFs", 2131626825));
            }
        } else if (i == 6) {
            emptyStubView.emptyImageView.setImageDrawable(null);
            emptyStubView.emptyTextView.setText(LocaleController.getString("NoGroupsInCommon", 2131626827));
        } else if (i == 7) {
            emptyStubView.emptyImageView.setImageDrawable(null);
            emptyStubView.emptyTextView.setText("");
        }
        return emptyStubView;
    }

    /* loaded from: classes3.dex */
    public static class EmptyStubView extends LinearLayout {
        final ImageView emptyImageView;
        final TextView emptyTextView;
        boolean ignoreRequestLayout;

        public EmptyStubView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            TextView textView = new TextView(context);
            this.emptyTextView = textView;
            ImageView imageView = new ImageView(context);
            this.emptyImageView = imageView;
            setOrientation(1);
            setGravity(17);
            addView(imageView, LayoutHelper.createLinear(-2, -2));
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2", resourcesProvider));
            textView.setGravity(17);
            textView.setTextSize(1, 17.0f);
            textView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(128.0f));
            addView(textView, LayoutHelper.createLinear(-2, -2, 17, 0, 24, 0, 0));
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
            this.ignoreRequestLayout = true;
            if (AndroidUtilities.isTablet()) {
                this.emptyTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(128.0f));
            } else if (rotation == 3 || rotation == 1) {
                this.emptyTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
            } else {
                this.emptyTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(128.0f));
            }
            this.ignoreRequestLayout = false;
            super.onMeasure(i, i2);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreRequestLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* loaded from: classes3.dex */
    public class SharedPhotoVideoAdapter extends RecyclerListView.FastScrollAdapter {
        private boolean inFastScrollMode;
        private Context mContext;
        SharedPhotoVideoCell2.SharedResources sharedResources;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        public SharedPhotoVideoAdapter(Context context) {
            SharedMediaLayout.this = r1;
            this.mContext = context;
        }

        public int getPositionForIndex(int i) {
            return SharedMediaLayout.this.sharedMediaData[0].startOffset + i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (DialogObject.isEncryptedDialog(SharedMediaLayout.this.dialog_id)) {
                if (SharedMediaLayout.this.sharedMediaData[0].messages.size() == 0 && !SharedMediaLayout.this.sharedMediaData[0].loading) {
                    return 1;
                }
                if (SharedMediaLayout.this.sharedMediaData[0].messages.size() == 0 && (!SharedMediaLayout.this.sharedMediaData[0].endReached[0] || !SharedMediaLayout.this.sharedMediaData[0].endReached[1])) {
                    return 0;
                }
                int startOffset = SharedMediaLayout.this.sharedMediaData[0].getStartOffset() + SharedMediaLayout.this.sharedMediaData[0].getMessages().size();
                return startOffset != 0 ? (!SharedMediaLayout.this.sharedMediaData[0].endReached[0] || !SharedMediaLayout.this.sharedMediaData[0].endReached[1]) ? startOffset + 1 : startOffset : startOffset;
            } else if (SharedMediaLayout.this.sharedMediaData[0].loadingAfterFastScroll) {
                return SharedMediaLayout.this.sharedMediaData[0].totalCount;
            } else {
                if (SharedMediaLayout.this.sharedMediaData[0].messages.size() == 0 && !SharedMediaLayout.this.sharedMediaData[0].loading) {
                    return 1;
                }
                if (SharedMediaLayout.this.sharedMediaData[0].messages.size() == 0 && ((!SharedMediaLayout.this.sharedMediaData[0].endReached[0] || !SharedMediaLayout.this.sharedMediaData[0].endReached[1]) && SharedMediaLayout.this.sharedMediaData[0].startReached)) {
                    return 0;
                }
                if (SharedMediaLayout.this.sharedMediaData[0].totalCount == 0) {
                    int startOffset2 = SharedMediaLayout.this.sharedMediaData[0].getStartOffset() + SharedMediaLayout.this.sharedMediaData[0].getMessages().size();
                    return startOffset2 != 0 ? (!SharedMediaLayout.this.sharedMediaData[0].endReached[0] || !SharedMediaLayout.this.sharedMediaData[0].endReached[1]) ? SharedMediaLayout.this.sharedMediaData[0].getEndLoadingStubs() != 0 ? startOffset2 + SharedMediaLayout.this.sharedMediaData[0].getEndLoadingStubs() : startOffset2 + 1 : startOffset2 : startOffset2;
                }
                return SharedMediaLayout.this.sharedMediaData[0].totalCount;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i != 0) {
                View createEmptyStubView = SharedMediaLayout.createEmptyStubView(this.mContext, 0, SharedMediaLayout.this.dialog_id, SharedMediaLayout.this.resourcesProvider);
                createEmptyStubView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                return new RecyclerListView.Holder(createEmptyStubView);
            }
            if (this.sharedResources == null) {
                this.sharedResources = new SharedPhotoVideoCell2.SharedResources(viewGroup.getContext(), SharedMediaLayout.this.resourcesProvider);
            }
            SharedPhotoVideoCell2 sharedPhotoVideoCell2 = new SharedPhotoVideoCell2(this.mContext, this.sharedResources, SharedMediaLayout.this.profileActivity.getCurrentAccount());
            sharedPhotoVideoCell2.setGradientView(SharedMediaLayout.this.globalGradientView);
            sharedPhotoVideoCell2.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(sharedPhotoVideoCell2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                boolean z = false;
                ArrayList<MessageObject> messages = SharedMediaLayout.this.sharedMediaData[0].getMessages();
                int startOffset = i - SharedMediaLayout.this.sharedMediaData[0].getStartOffset();
                SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) viewHolder.itemView;
                int messageId = sharedPhotoVideoCell2.getMessageId();
                int i2 = this == SharedMediaLayout.this.photoVideoAdapter ? SharedMediaLayout.this.mediaColumnsCount : SharedMediaLayout.this.animateToColumnsCount;
                if (startOffset >= 0 && startOffset < messages.size()) {
                    MessageObject messageObject = messages.get(startOffset);
                    boolean z2 = messageObject.getId() == messageId;
                    if (SharedMediaLayout.this.isActionModeShowed) {
                        if (SharedMediaLayout.this.selectedFiles[messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0) {
                            z = true;
                        }
                        sharedPhotoVideoCell2.setChecked(z, z2);
                    } else {
                        sharedPhotoVideoCell2.setChecked(false, z2);
                    }
                    sharedPhotoVideoCell2.setMessageObject(messageObject, i2);
                    return;
                }
                sharedPhotoVideoCell2.setMessageObject(null, i2);
                sharedPhotoVideoCell2.setChecked(false, false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (this.inFastScrollMode || SharedMediaLayout.this.sharedMediaData[0].getMessages().size() != 0 || SharedMediaLayout.this.sharedMediaData[0].loading || !SharedMediaLayout.this.sharedMediaData[0].startReached) {
                SharedMediaLayout.this.sharedMediaData[0].getStartOffset();
                SharedMediaLayout.this.sharedMediaData[0].getMessages().size();
                SharedMediaLayout.this.sharedMediaData[0].getStartOffset();
                return 0;
            }
            return 2;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            if (SharedMediaLayout.this.sharedMediaData[0].fastScrollPeriods == null) {
                return "";
            }
            ArrayList<Period> arrayList = SharedMediaLayout.this.sharedMediaData[0].fastScrollPeriods;
            if (arrayList.isEmpty()) {
                return "";
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (i <= arrayList.get(i2).startOffset) {
                    return arrayList.get(i2).formatedDate;
                }
            }
            return arrayList.get(arrayList.size() - 1).formatedDate;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            double d;
            int measuredHeight = recyclerListView.getChildAt(0).getMeasuredHeight();
            double ceil = Math.ceil(getTotalItemsCount() / SharedMediaLayout.this.mediaColumnsCount);
            Double.isNaN(measuredHeight);
            float measuredHeight2 = f * (((int) (ceil * d)) - (recyclerListView.getMeasuredHeight() - recyclerListView.getPaddingTop()));
            iArr[0] = ((int) (measuredHeight2 / measuredHeight)) * SharedMediaLayout.this.mediaColumnsCount;
            iArr[1] = ((int) measuredHeight2) % measuredHeight;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void onStartFastScroll() {
            this.inFastScrollMode = true;
            MediaPage mediaPage = SharedMediaLayout.this.getMediaPage(0);
            if (mediaPage != null) {
                SharedMediaLayout.showFastScrollHint(mediaPage, null, false);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void onFinishFastScroll(RecyclerListView recyclerListView) {
            if (this.inFastScrollMode) {
                this.inFastScrollMode = false;
                if (recyclerListView == null) {
                    return;
                }
                int i = 0;
                for (int i2 = 0; i2 < recyclerListView.getChildCount(); i2++) {
                    View childAt = recyclerListView.getChildAt(i2);
                    if (childAt instanceof SharedPhotoVideoCell2) {
                        i = ((SharedPhotoVideoCell2) childAt).getMessageId();
                    }
                    if (i != 0) {
                        break;
                    }
                }
                if (i != 0) {
                    return;
                }
                SharedMediaLayout.this.findPeriodAndJumpToDate(0, recyclerListView, true);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public int getTotalItemsCount() {
            return SharedMediaLayout.this.sharedMediaData[0].totalCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public float getScrollProgress(RecyclerListView recyclerListView) {
            int i = this == SharedMediaLayout.this.photoVideoAdapter ? SharedMediaLayout.this.mediaColumnsCount : SharedMediaLayout.this.animateToColumnsCount;
            int ceil = (int) Math.ceil(getTotalItemsCount() / i);
            if (recyclerListView.getChildCount() == 0) {
                return 0.0f;
            }
            int measuredHeight = recyclerListView.getChildAt(0).getMeasuredHeight();
            View childAt = recyclerListView.getChildAt(0);
            int childAdapterPosition = recyclerListView.getChildAdapterPosition(childAt);
            if (childAdapterPosition < 0) {
                return 0.0f;
            }
            return (((childAdapterPosition / i) * measuredHeight) - (childAt.getTop() - recyclerListView.getPaddingTop())) / ((ceil * measuredHeight) - (recyclerListView.getMeasuredHeight() - recyclerListView.getPaddingTop()));
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public boolean fastScrollIsVisible(RecyclerListView recyclerListView) {
            return recyclerListView.getChildCount() != 0 && ((int) Math.ceil((double) (((float) getTotalItemsCount()) / ((float) (this == SharedMediaLayout.this.photoVideoAdapter ? SharedMediaLayout.this.mediaColumnsCount : SharedMediaLayout.this.animateToColumnsCount))))) * recyclerListView.getChildAt(0).getMeasuredHeight() > recyclerListView.getMeasuredHeight();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void onFastScrollSingleTap() {
            SharedMediaLayout.this.showMediaCalendar(true);
        }
    }

    public void findPeriodAndJumpToDate(int i, RecyclerListView recyclerListView, boolean z) {
        ArrayList<Period> arrayList = this.sharedMediaData[i].fastScrollPeriods;
        int findFirstVisibleItemPosition = ((LinearLayoutManager) recyclerListView.getLayoutManager()).findFirstVisibleItemPosition();
        if (findFirstVisibleItemPosition >= 0) {
            Period period = null;
            if (arrayList != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= arrayList.size()) {
                        break;
                    } else if (findFirstVisibleItemPosition <= arrayList.get(i2).startOffset) {
                        period = arrayList.get(i2);
                        break;
                    } else {
                        i2++;
                    }
                }
                if (period == null) {
                    period = arrayList.get(arrayList.size() - 1);
                }
            }
            if (period == null) {
                return;
            }
            jumpToDate(i, period.maxId, period.startOffset + 1, z);
        }
    }

    public void jumpToDate(int i, int i2, int i3, boolean z) {
        this.sharedMediaData[i].messages.clear();
        this.sharedMediaData[i].messagesDict[0].clear();
        this.sharedMediaData[i].messagesDict[1].clear();
        this.sharedMediaData[i].setMaxId(0, i2);
        this.sharedMediaData[i].setEndReached(0, false);
        SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
        sharedMediaDataArr[i].startReached = false;
        sharedMediaDataArr[i].startOffset = i3;
        SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
        sharedMediaDataArr2[i].endLoadingStubs = (sharedMediaDataArr2[i].totalCount - i3) - 1;
        if (this.sharedMediaData[i].endLoadingStubs < 0) {
            this.sharedMediaData[i].endLoadingStubs = 0;
        }
        SharedMediaData[] sharedMediaDataArr3 = this.sharedMediaData;
        sharedMediaDataArr3[i].min_id = i2;
        sharedMediaDataArr3[i].loadingAfterFastScroll = true;
        sharedMediaDataArr3[i].loading = false;
        sharedMediaDataArr3[i].requestIndex++;
        MediaPage mediaPage = getMediaPage(i);
        if (mediaPage != null && mediaPage.listView.getAdapter() != null) {
            mediaPage.listView.getAdapter().notifyDataSetChanged();
        }
        if (z) {
            int i4 = 0;
            while (true) {
                MediaPage[] mediaPageArr = this.mediaPages;
                if (i4 >= mediaPageArr.length) {
                    return;
                }
                if (mediaPageArr[i4].selectedType == i) {
                    ExtendedGridLayoutManager extendedGridLayoutManager = this.mediaPages[i4].layoutManager;
                    SharedMediaData[] sharedMediaDataArr4 = this.sharedMediaData;
                    extendedGridLayoutManager.scrollToPositionWithOffset(Math.min(sharedMediaDataArr4[i].totalCount - 1, sharedMediaDataArr4[i].startOffset), 0);
                }
                i4++;
            }
        }
    }

    /* loaded from: classes3.dex */
    public class MediaSearchAdapter extends RecyclerListView.SelectionAdapter {
        private int currentType;
        private int lastReqId;
        private Context mContext;
        private Runnable searchRunnable;
        private int searchesInProgress;
        private ArrayList<MessageObject> searchResult = new ArrayList<>();
        protected ArrayList<MessageObject> globalSearch = new ArrayList<>();
        private int reqId = 0;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        public MediaSearchAdapter(Context context, int i) {
            SharedMediaLayout.this = r1;
            this.mContext = context;
            this.currentType = i;
        }

        public void queryServerSearch(String str, int i, long j) {
            if (DialogObject.isEncryptedDialog(j)) {
                return;
            }
            if (this.reqId != 0) {
                SharedMediaLayout.this.profileActivity.getConnectionsManager().cancelRequest(this.reqId, true);
                this.reqId = 0;
                this.searchesInProgress--;
            }
            if (str == null || str.length() == 0) {
                this.globalSearch.clear();
                this.lastReqId = 0;
                notifyDataSetChanged();
                return;
            }
            TLRPC$TL_messages_search tLRPC$TL_messages_search = new TLRPC$TL_messages_search();
            tLRPC$TL_messages_search.limit = 50;
            tLRPC$TL_messages_search.offset_id = i;
            int i2 = this.currentType;
            if (i2 == 1) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterDocument();
            } else if (i2 == 3) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterUrl();
            } else if (i2 == 4) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterMusic();
            }
            tLRPC$TL_messages_search.q = str;
            TLRPC$InputPeer inputPeer = SharedMediaLayout.this.profileActivity.getMessagesController().getInputPeer(j);
            tLRPC$TL_messages_search.peer = inputPeer;
            if (inputPeer == null) {
                return;
            }
            int i3 = this.lastReqId + 1;
            this.lastReqId = i3;
            this.searchesInProgress++;
            this.reqId = SharedMediaLayout.this.profileActivity.getConnectionsManager().sendRequest(tLRPC$TL_messages_search, new SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda4(this, i, i3, str), 2);
            SharedMediaLayout.this.profileActivity.getConnectionsManager().bindRequestToGuid(this.reqId, SharedMediaLayout.this.profileActivity.getClassGuid());
        }

        public /* synthetic */ void lambda$queryServerSearch$1(int i, int i2, String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            ArrayList arrayList = new ArrayList();
            if (tLRPC$TL_error == null) {
                TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                for (int i3 = 0; i3 < tLRPC$messages_Messages.messages.size(); i3++) {
                    TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i3);
                    if (i == 0 || tLRPC$Message.id <= i) {
                        arrayList.add(new MessageObject(SharedMediaLayout.this.profileActivity.getCurrentAccount(), tLRPC$Message, false, true));
                    }
                }
            }
            AndroidUtilities.runOnUIThread(new SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda0(this, i2, arrayList, str));
        }

        public /* synthetic */ void lambda$queryServerSearch$0(int i, ArrayList arrayList, String str) {
            if (this.reqId != 0) {
                if (i == this.lastReqId) {
                    int itemCount = getItemCount();
                    this.globalSearch = arrayList;
                    this.searchesInProgress--;
                    int itemCount2 = getItemCount();
                    if (this.searchesInProgress == 0 || itemCount2 != 0) {
                        SharedMediaLayout.this.switchToCurrentSelectedMode(false);
                    }
                    for (int i2 = 0; i2 < SharedMediaLayout.this.mediaPages.length; i2++) {
                        if (SharedMediaLayout.this.mediaPages[i2].selectedType == this.currentType) {
                            if (this.searchesInProgress == 0 && itemCount2 == 0) {
                                SharedMediaLayout.this.mediaPages[i2].emptyView.title.setText(LocaleController.formatString(2131626859, "NoResultFoundFor", str));
                                SharedMediaLayout.this.mediaPages[i2].emptyView.showProgress(false, true);
                            } else if (itemCount == 0) {
                                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                                sharedMediaLayout.animateItemsEnter(sharedMediaLayout.mediaPages[i2].listView, 0, null);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
                this.reqId = 0;
            }
        }

        public void search(String str, boolean z) {
            Runnable runnable = this.searchRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.searchRunnable = null;
            }
            if (!this.searchResult.isEmpty() || !this.globalSearch.isEmpty()) {
                this.searchResult.clear();
                this.globalSearch.clear();
                notifyDataSetChanged();
            }
            if (!TextUtils.isEmpty(str)) {
                for (int i = 0; i < SharedMediaLayout.this.mediaPages.length; i++) {
                    if (SharedMediaLayout.this.mediaPages[i].selectedType == this.currentType) {
                        SharedMediaLayout.this.mediaPages[i].emptyView.showProgress(true, z);
                    }
                }
                SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda1 sharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda1 = new SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda1(this, str);
                this.searchRunnable = sharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda1;
                AndroidUtilities.runOnUIThread(sharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda1, 300L);
            } else if (!this.searchResult.isEmpty() || !this.globalSearch.isEmpty() || this.searchesInProgress != 0) {
                this.searchResult.clear();
                this.globalSearch.clear();
                if (this.reqId == 0) {
                    return;
                }
                SharedMediaLayout.this.profileActivity.getConnectionsManager().cancelRequest(this.reqId, true);
                this.reqId = 0;
                this.searchesInProgress--;
            }
        }

        public /* synthetic */ void lambda$search$3(String str) {
            int i;
            if (!SharedMediaLayout.this.sharedMediaData[this.currentType].messages.isEmpty() && ((i = this.currentType) == 1 || i == 4)) {
                MessageObject messageObject = SharedMediaLayout.this.sharedMediaData[this.currentType].messages.get(SharedMediaLayout.this.sharedMediaData[this.currentType].messages.size() - 1);
                queryServerSearch(str, messageObject.getId(), messageObject.getDialogId());
            } else if (this.currentType == 3) {
                queryServerSearch(str, 0, SharedMediaLayout.this.dialog_id);
            }
            int i2 = this.currentType;
            if (i2 == 1 || i2 == 4) {
                ArrayList arrayList = new ArrayList(SharedMediaLayout.this.sharedMediaData[this.currentType].messages);
                this.searchesInProgress++;
                Utilities.searchQueue.postRunnable(new SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda2(this, str, arrayList));
            }
        }

        public /* synthetic */ void lambda$search$2(String str, ArrayList arrayList) {
            TLRPC$Document tLRPC$Document;
            boolean z;
            String str2;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList<MessageObject> arrayList2 = new ArrayList<>();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                int i3 = 0;
                while (true) {
                    if (i3 < i) {
                        String str3 = strArr[i3];
                        String documentName = messageObject.getDocumentName();
                        if (documentName != null && documentName.length() != 0) {
                            if (documentName.toLowerCase().contains(str3)) {
                                arrayList2.add(messageObject);
                                break;
                            } else if (this.currentType == 4) {
                                if (messageObject.type == 0) {
                                    tLRPC$Document = messageObject.messageOwner.media.webpage.document;
                                } else {
                                    tLRPC$Document = messageObject.messageOwner.media.document;
                                }
                                int i4 = 0;
                                while (true) {
                                    if (i4 >= tLRPC$Document.attributes.size()) {
                                        z = false;
                                        break;
                                    }
                                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i4);
                                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                                        String str4 = tLRPC$DocumentAttribute.performer;
                                        z = str4 != null ? str4.toLowerCase().contains(str3) : false;
                                        if (!z && (str2 = tLRPC$DocumentAttribute.title) != null) {
                                            z = str2.toLowerCase().contains(str3);
                                        }
                                    } else {
                                        i4++;
                                    }
                                }
                                if (z) {
                                    arrayList2.add(messageObject);
                                    break;
                                }
                            } else {
                                continue;
                            }
                        }
                        i3++;
                    }
                }
            }
            updateSearchResults(arrayList2);
        }

        private void updateSearchResults(ArrayList<MessageObject> arrayList) {
            AndroidUtilities.runOnUIThread(new SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda3(this, arrayList));
        }

        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList) {
            if (!SharedMediaLayout.this.searching) {
                return;
            }
            this.searchesInProgress--;
            int itemCount = getItemCount();
            this.searchResult = arrayList;
            int itemCount2 = getItemCount();
            if (this.searchesInProgress == 0 || itemCount2 != 0) {
                SharedMediaLayout.this.switchToCurrentSelectedMode(false);
            }
            for (int i = 0; i < SharedMediaLayout.this.mediaPages.length; i++) {
                if (SharedMediaLayout.this.mediaPages[i].selectedType == this.currentType) {
                    if (this.searchesInProgress == 0 && itemCount2 == 0) {
                        SharedMediaLayout.this.mediaPages[i].emptyView.title.setText(LocaleController.getString("NoResult", 2131626858));
                        SharedMediaLayout.this.mediaPages[i].emptyView.showProgress(false, true);
                    } else if (itemCount == 0) {
                        SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                        sharedMediaLayout.animateItemsEnter(sharedMediaLayout.mediaPages[i].listView, 0, null);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != this.searchResult.size() + this.globalSearch.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.searchResult.size();
            int size2 = this.globalSearch.size();
            return size2 != 0 ? size + size2 : size;
        }

        public MessageObject getItem(int i) {
            if (i < this.searchResult.size()) {
                return this.searchResult.get(i);
            }
            return this.globalSearch.get(i - this.searchResult.size());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SharedDocumentCell sharedDocumentCell;
            int i2 = this.currentType;
            if (i2 == 1) {
                sharedDocumentCell = new SharedDocumentCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
            } else if (i2 == 4) {
                sharedDocumentCell = new AnonymousClass1(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
            } else {
                SharedLinkCell sharedLinkCell = new SharedLinkCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
                sharedLinkCell.setDelegate(SharedMediaLayout.this.sharedLinkCellDelegate);
                sharedDocumentCell = sharedLinkCell;
            }
            sharedDocumentCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(sharedDocumentCell);
        }

        /* renamed from: org.telegram.ui.Components.SharedMediaLayout$MediaSearchAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends SharedAudioCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
                super(context, i, resourcesProvider);
                MediaSearchAdapter.this = r1;
            }

            @Override // org.telegram.ui.Cells.SharedAudioCell
            public boolean needPlayMessage(MessageObject messageObject) {
                if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                    boolean playMessage = MediaController.getInstance().playMessage(messageObject);
                    MediaController.getInstance().setVoiceMessagesPlaylist(playMessage ? MediaSearchAdapter.this.searchResult : null, false);
                    if (messageObject.isRoundVideo()) {
                        MediaController.getInstance().setCurrentVideoVisible(false);
                    }
                    return playMessage;
                } else if (!messageObject.isMusic()) {
                    return false;
                } else {
                    return MediaController.getInstance().setPlaylist(MediaSearchAdapter.this.searchResult, messageObject, SharedMediaLayout.this.mergeDialogId);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2 = this.currentType;
            boolean z = false;
            if (i2 == 1) {
                SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) viewHolder.itemView;
                MessageObject item = getItem(i);
                sharedDocumentCell.setDocument(item, i != getItemCount() - 1);
                if (SharedMediaLayout.this.isActionModeShowed) {
                    if (SharedMediaLayout.this.selectedFiles[item.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(item.getId()) >= 0) {
                        z = true;
                    }
                    sharedDocumentCell.setChecked(z, !SharedMediaLayout.this.scrolling);
                    return;
                }
                sharedDocumentCell.setChecked(false, !SharedMediaLayout.this.scrolling);
            } else if (i2 == 3) {
                SharedLinkCell sharedLinkCell = (SharedLinkCell) viewHolder.itemView;
                MessageObject item2 = getItem(i);
                sharedLinkCell.setLink(item2, i != getItemCount() - 1);
                if (SharedMediaLayout.this.isActionModeShowed) {
                    if (SharedMediaLayout.this.selectedFiles[item2.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(item2.getId()) >= 0) {
                        z = true;
                    }
                    sharedLinkCell.setChecked(z, !SharedMediaLayout.this.scrolling);
                    return;
                }
                sharedLinkCell.setChecked(false, !SharedMediaLayout.this.scrolling);
            } else if (i2 != 4) {
            } else {
                SharedAudioCell sharedAudioCell = (SharedAudioCell) viewHolder.itemView;
                MessageObject item3 = getItem(i);
                sharedAudioCell.setMessageObject(item3, i != getItemCount() - 1);
                if (SharedMediaLayout.this.isActionModeShowed) {
                    if (SharedMediaLayout.this.selectedFiles[item3.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(item3.getId()) >= 0) {
                        z = true;
                    }
                    sharedAudioCell.setChecked(z, !SharedMediaLayout.this.scrolling);
                    return;
                }
                sharedAudioCell.setChecked(false, !SharedMediaLayout.this.scrolling);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class GifAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return i;
        }

        public GifAdapter(Context context) {
            SharedMediaLayout.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return SharedMediaLayout.this.sharedMediaData[5].messages.size() != 0 || SharedMediaLayout.this.sharedMediaData[5].loading;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (SharedMediaLayout.this.sharedMediaData[5].messages.size() != 0 || SharedMediaLayout.this.sharedMediaData[5].loading) {
                return SharedMediaLayout.this.sharedMediaData[5].messages.size();
            }
            return 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (SharedMediaLayout.this.sharedMediaData[5].messages.size() != 0 || SharedMediaLayout.this.sharedMediaData[5].loading) ? 0 : 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 1) {
                View createEmptyStubView = SharedMediaLayout.createEmptyStubView(this.mContext, 5, SharedMediaLayout.this.dialog_id, SharedMediaLayout.this.resourcesProvider);
                createEmptyStubView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                return new RecyclerListView.Holder(createEmptyStubView);
            }
            ContextLinkCell contextLinkCell = new ContextLinkCell(this.mContext, true, SharedMediaLayout.this.resourcesProvider);
            contextLinkCell.setCanPreviewGif(true);
            return new RecyclerListView.Holder(contextLinkCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            MessageObject messageObject;
            TLRPC$Document document;
            if (viewHolder.getItemViewType() == 1 || (document = (messageObject = SharedMediaLayout.this.sharedMediaData[5].messages.get(i)).getDocument()) == null) {
                return;
            }
            ContextLinkCell contextLinkCell = (ContextLinkCell) viewHolder.itemView;
            boolean z = false;
            contextLinkCell.setGif(document, messageObject, messageObject.messageOwner.date, false);
            if (SharedMediaLayout.this.isActionModeShowed) {
                if (SharedMediaLayout.this.selectedFiles[messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0) {
                    z = true;
                }
                contextLinkCell.setChecked(z, !SharedMediaLayout.this.scrolling);
                return;
            }
            contextLinkCell.setChecked(false, !SharedMediaLayout.this.scrolling);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ContextLinkCell) {
                ImageReceiver photoImage = ((ContextLinkCell) view).getPhotoImage();
                if (SharedMediaLayout.this.mediaPages[0].selectedType == 5) {
                    photoImage.setAllowStartAnimation(true);
                    photoImage.startAnimation();
                    return;
                }
                photoImage.setAllowStartAnimation(false);
                photoImage.stopAnimation();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class CommonGroupsAdapter extends RecyclerListView.SelectionAdapter {
        private ArrayList<TLRPC$Chat> chats = new ArrayList<>();
        private boolean endReached;
        private boolean firstLoaded;
        private boolean loading;
        private Context mContext;

        public CommonGroupsAdapter(Context context) {
            SharedMediaLayout.this = r1;
            this.mContext = context;
        }

        public void getChats(long j, int i) {
            long j2;
            if (this.loading) {
                return;
            }
            TLRPC$TL_messages_getCommonChats tLRPC$TL_messages_getCommonChats = new TLRPC$TL_messages_getCommonChats();
            if (!DialogObject.isEncryptedDialog(SharedMediaLayout.this.dialog_id)) {
                j2 = SharedMediaLayout.this.dialog_id;
            } else {
                j2 = SharedMediaLayout.this.profileActivity.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(SharedMediaLayout.this.dialog_id))).user_id;
            }
            TLRPC$InputUser inputUser = SharedMediaLayout.this.profileActivity.getMessagesController().getInputUser(j2);
            tLRPC$TL_messages_getCommonChats.user_id = inputUser;
            if (inputUser instanceof TLRPC$TL_inputUserEmpty) {
                return;
            }
            tLRPC$TL_messages_getCommonChats.limit = i;
            tLRPC$TL_messages_getCommonChats.max_id = j;
            this.loading = true;
            notifyDataSetChanged();
            SharedMediaLayout.this.profileActivity.getConnectionsManager().bindRequestToGuid(SharedMediaLayout.this.profileActivity.getConnectionsManager().sendRequest(tLRPC$TL_messages_getCommonChats, new SharedMediaLayout$CommonGroupsAdapter$$ExternalSyntheticLambda1(this, i)), SharedMediaLayout.this.profileActivity.getClassGuid());
        }

        public /* synthetic */ void lambda$getChats$1(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new SharedMediaLayout$CommonGroupsAdapter$$ExternalSyntheticLambda0(this, tLRPC$TL_error, tLObject, i));
        }

        public /* synthetic */ void lambda$getChats$0(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
            int itemCount = getItemCount();
            if (tLRPC$TL_error == null) {
                TLRPC$messages_Chats tLRPC$messages_Chats = (TLRPC$messages_Chats) tLObject;
                SharedMediaLayout.this.profileActivity.getMessagesController().putChats(tLRPC$messages_Chats.chats, false);
                this.endReached = tLRPC$messages_Chats.chats.isEmpty() || tLRPC$messages_Chats.chats.size() != i;
                this.chats.addAll(tLRPC$messages_Chats.chats);
            } else {
                this.endReached = true;
            }
            for (int i2 = 0; i2 < SharedMediaLayout.this.mediaPages.length; i2++) {
                if (SharedMediaLayout.this.mediaPages[i2].selectedType == 6 && SharedMediaLayout.this.mediaPages[i2].listView != null) {
                    BlurredRecyclerView blurredRecyclerView = SharedMediaLayout.this.mediaPages[i2].listView;
                    if (this.firstLoaded || itemCount == 0) {
                        SharedMediaLayout.this.animateItemsEnter(blurredRecyclerView, 0, null);
                    }
                }
            }
            this.loading = false;
            this.firstLoaded = true;
            notifyDataSetChanged();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() != this.chats.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (!this.chats.isEmpty() || this.loading) {
                int size = this.chats.size();
                return (this.chats.isEmpty() || this.endReached) ? size : size + 1;
            }
            return 1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ProfileSearchCell profileSearchCell;
            if (i == 0) {
                profileSearchCell = new ProfileSearchCell(this.mContext, SharedMediaLayout.this.resourcesProvider);
            } else if (i == 2) {
                View createEmptyStubView = SharedMediaLayout.createEmptyStubView(this.mContext, 6, SharedMediaLayout.this.dialog_id, SharedMediaLayout.this.resourcesProvider);
                createEmptyStubView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                return new RecyclerListView.Holder(createEmptyStubView);
            } else {
                FlickerLoadingView flickerLoadingView = new FlickerLoadingView(this.mContext, SharedMediaLayout.this.resourcesProvider);
                flickerLoadingView.setIsSingleCell(true);
                flickerLoadingView.showDate(false);
                flickerLoadingView.setViewType(1);
                profileSearchCell = flickerLoadingView;
            }
            profileSearchCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(profileSearchCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                ProfileSearchCell profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
                profileSearchCell.setData(this.chats.get(i), null, null, null, false, false);
                boolean z = true;
                if (i == this.chats.size() - 1 && this.endReached) {
                    z = false;
                }
                profileSearchCell.useSeparator = z;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (!this.chats.isEmpty() || this.loading) {
                return i < this.chats.size() ? 0 : 1;
            }
            return 2;
        }
    }

    /* loaded from: classes3.dex */
    public class ChatUsersAdapter extends RecyclerListView.SelectionAdapter {
        private TLRPC$ChatFull chatInfo;
        private Context mContext;
        private ArrayList<Integer> sortedUsers;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public ChatUsersAdapter(Context context) {
            SharedMediaLayout.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
            if (tLRPC$ChatFull == null || !tLRPC$ChatFull.participants.participants.isEmpty()) {
                TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
                if (tLRPC$ChatFull2 == null) {
                    return 0;
                }
                return tLRPC$ChatFull2.participants.participants.size();
            }
            return 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 1) {
                View createEmptyStubView = SharedMediaLayout.createEmptyStubView(this.mContext, 7, SharedMediaLayout.this.dialog_id, SharedMediaLayout.this.resourcesProvider);
                createEmptyStubView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                return new RecyclerListView.Holder(createEmptyStubView);
            }
            UserCell userCell = new UserCell(this.mContext, 9, 0, true, false, SharedMediaLayout.this.resourcesProvider);
            userCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(userCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$ChatParticipant tLRPC$ChatParticipant;
            String string;
            UserCell userCell = (UserCell) viewHolder.itemView;
            if (!this.sortedUsers.isEmpty()) {
                tLRPC$ChatParticipant = this.chatInfo.participants.participants.get(this.sortedUsers.get(i).intValue());
            } else {
                tLRPC$ChatParticipant = this.chatInfo.participants.participants.get(i);
            }
            if (tLRPC$ChatParticipant != null) {
                String str = null;
                if (tLRPC$ChatParticipant instanceof TLRPC$TL_chatChannelParticipant) {
                    TLRPC$ChannelParticipant tLRPC$ChannelParticipant = ((TLRPC$TL_chatChannelParticipant) tLRPC$ChatParticipant).channelParticipant;
                    if (!TextUtils.isEmpty(tLRPC$ChannelParticipant.rank)) {
                        string = tLRPC$ChannelParticipant.rank;
                    } else if (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator) {
                        string = LocaleController.getString("ChannelCreator", 2131624894);
                    } else if (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) {
                        string = LocaleController.getString("ChannelAdmin", 2131624875);
                    }
                    str = string;
                } else if (tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantCreator) {
                    str = LocaleController.getString("ChannelCreator", 2131624894);
                } else if (tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin) {
                    str = LocaleController.getString("ChannelAdmin", 2131624875);
                }
                userCell.setAdminRole(str);
                TLRPC$User user = SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant.user_id));
                boolean z = true;
                if (i == this.chatInfo.participants.participants.size() - 1) {
                    z = false;
                }
                userCell.setData(user, null, null, 0, z);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
            return (tLRPC$ChatFull == null || !tLRPC$ChatFull.participants.participants.isEmpty()) ? 0 : 1;
        }
    }

    /* loaded from: classes3.dex */
    public class GroupUsersSearchAdapter extends RecyclerListView.SelectionAdapter {
        private TLRPC$Chat currentChat;
        private Context mContext;
        private SearchAdapterHelper searchAdapterHelper;
        private Runnable searchRunnable;
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private int totalCount = 0;
        int searchCount = 0;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        public GroupUsersSearchAdapter(Context context) {
            SharedMediaLayout.this = r2;
            this.mContext = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda4(this));
            this.currentChat = r2.delegate.getCurrentChat();
        }

        public /* synthetic */ void lambda$new$0(int i) {
            notifyDataSetChanged();
            if (i == 1) {
                int i2 = this.searchCount - 1;
                this.searchCount = i2;
                if (i2 != 0) {
                    return;
                }
                for (int i3 = 0; i3 < SharedMediaLayout.this.mediaPages.length; i3++) {
                    if (SharedMediaLayout.this.mediaPages[i3].selectedType == 7) {
                        if (getItemCount() == 0) {
                            SharedMediaLayout.this.mediaPages[i3].emptyView.showProgress(false, true);
                        } else {
                            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                            sharedMediaLayout.animateItemsEnter(sharedMediaLayout.mediaPages[i3].listView, 0, null);
                        }
                    }
                }
            }
        }

        private boolean createMenuForParticipant(TLObject tLObject, boolean z) {
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
                TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant = new TLRPC$TL_chatChannelParticipant();
                tLRPC$TL_chatChannelParticipant.channelParticipant = tLRPC$ChannelParticipant;
                tLRPC$TL_chatChannelParticipant.user_id = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
                tLRPC$TL_chatChannelParticipant.inviter_id = tLRPC$ChannelParticipant.inviter_id;
                tLRPC$TL_chatChannelParticipant.date = tLRPC$ChannelParticipant.date;
                tLObject = tLRPC$TL_chatChannelParticipant;
            }
            return SharedMediaLayout.this.delegate.onMemberClick((TLRPC$ChatParticipant) tLObject, true, z);
        }

        public void search(String str, boolean z) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults(null);
            this.searchAdapterHelper.queryServerSearch(null, true, false, true, false, false, ChatObject.isChannel(this.currentChat) ? this.currentChat.id : 0L, false, 2, 0);
            notifyDataSetChanged();
            for (int i = 0; i < SharedMediaLayout.this.mediaPages.length; i++) {
                if (SharedMediaLayout.this.mediaPages[i].selectedType == 7 && !TextUtils.isEmpty(str)) {
                    SharedMediaLayout.this.mediaPages[i].emptyView.showProgress(true, z);
                }
            }
            if (!TextUtils.isEmpty(str)) {
                DispatchQueue dispatchQueue = Utilities.searchQueue;
                SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda0 sharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda0 = new SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda0(this, str);
                this.searchRunnable = sharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda0;
                dispatchQueue.postRunnable(sharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda0, 300L);
            }
        }

        /* renamed from: processSearch */
        public void lambda$search$1(String str) {
            AndroidUtilities.runOnUIThread(new SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda1(this, str));
        }

        public /* synthetic */ void lambda$processSearch$3(String str) {
            ArrayList arrayList = null;
            this.searchRunnable = null;
            if (!ChatObject.isChannel(this.currentChat) && SharedMediaLayout.this.info != null) {
                arrayList = new ArrayList(SharedMediaLayout.this.info.participants.participants);
            }
            this.searchCount = 2;
            if (arrayList != null) {
                Utilities.searchQueue.postRunnable(new SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda2(this, str, arrayList));
            } else {
                this.searchCount = 2 - 1;
            }
            this.searchAdapterHelper.queryServerSearch(str, false, false, true, false, false, ChatObject.isChannel(this.currentChat) ? this.currentChat.id : 0L, false, 2, 1);
        }

        /* JADX WARN: Code restructure failed: missing block: B:41:0x00f1, code lost:
            if (r14.contains(" " + r3) != false) goto L48;
         */
        /* JADX WARN: Removed duplicated region for block: B:55:0x0147 A[LOOP:1: B:32:0x00b5->B:55:0x0147, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:63:0x0108 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$2(String str, ArrayList arrayList) {
            long j;
            char c;
            Object obj;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), new ArrayList<>());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList<CharSequence> arrayList2 = new ArrayList<>();
            ArrayList<TLObject> arrayList3 = new ArrayList<>();
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                TLObject tLObject = (TLObject) arrayList.get(i2);
                if (tLObject instanceof TLRPC$ChatParticipant) {
                    j = ((TLRPC$ChatParticipant) tLObject).user_id;
                } else {
                    if (tLObject instanceof TLRPC$ChannelParticipant) {
                        j = MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer);
                    }
                }
                TLRPC$User user = SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(j));
                if (user.id != SharedMediaLayout.this.profileActivity.getUserConfig().getClientUserId()) {
                    String lowerCase2 = UserObject.getUserName(user).toLowerCase();
                    String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase2);
                    if (lowerCase2.equals(translitString2)) {
                        translitString2 = null;
                    }
                    int i3 = 0;
                    char c2 = 0;
                    while (i3 < i) {
                        String str2 = strArr[i3];
                        if (!lowerCase2.startsWith(str2)) {
                            if (!lowerCase2.contains(" " + str2)) {
                                if (translitString2 != null) {
                                    if (!translitString2.startsWith(str2)) {
                                    }
                                }
                                String str3 = user.username;
                                c = (str3 == null || !str3.startsWith(str2)) ? c2 : (char) 2;
                                if (c == 0) {
                                    if (c == 1) {
                                        arrayList2.add(AndroidUtilities.generateSearchName(user.first_name, user.last_name, str2));
                                        obj = null;
                                    } else {
                                        obj = null;
                                        arrayList2.add(AndroidUtilities.generateSearchName("@" + user.username, null, "@" + str2));
                                    }
                                    arrayList3.add(tLObject);
                                } else {
                                    i3++;
                                    c2 = c;
                                }
                            }
                        }
                        c = 1;
                        if (c == 0) {
                        }
                    }
                }
            }
            updateSearchResults(arrayList2, arrayList3);
        }

        private void updateSearchResults(ArrayList<CharSequence> arrayList, ArrayList<TLObject> arrayList2) {
            AndroidUtilities.runOnUIThread(new SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda3(this, arrayList, arrayList2));
        }

        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList, ArrayList arrayList2) {
            if (!SharedMediaLayout.this.searching) {
                return;
            }
            this.searchResultNames = arrayList;
            this.searchCount--;
            if (!ChatObject.isChannel(this.currentChat)) {
                ArrayList<TLObject> groupSearch = this.searchAdapterHelper.getGroupSearch();
                groupSearch.clear();
                groupSearch.addAll(arrayList2);
            }
            if (this.searchCount == 0) {
                for (int i = 0; i < SharedMediaLayout.this.mediaPages.length; i++) {
                    if (SharedMediaLayout.this.mediaPages[i].selectedType == 7) {
                        if (getItemCount() == 0) {
                            SharedMediaLayout.this.mediaPages[i].emptyView.showProgress(false, true);
                        } else {
                            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                            sharedMediaLayout.animateItemsEnter(sharedMediaLayout.mediaPages[i].listView, 0, null);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.totalCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            int size = this.searchAdapterHelper.getGroupSearch().size();
            this.totalCount = size;
            if (size > 0 && SharedMediaLayout.this.searching && SharedMediaLayout.this.mediaPages[0].selectedType == 7 && SharedMediaLayout.this.mediaPages[0].listView.getAdapter() != this) {
                SharedMediaLayout.this.switchToCurrentSelectedMode(false);
            }
            super.notifyDataSetChanged();
        }

        public TLObject getItem(int i) {
            int size = this.searchAdapterHelper.getGroupSearch().size();
            if (i < 0 || i >= size) {
                return null;
            }
            return this.searchAdapterHelper.getGroupSearch().get(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ManageChatUserCell manageChatUserCell = new ManageChatUserCell(this.mContext, 9, 5, true, SharedMediaLayout.this.resourcesProvider);
            manageChatUserCell.setBackgroundColor(SharedMediaLayout.this.getThemedColor("windowBackgroundWhite"));
            manageChatUserCell.setDelegate(new SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda5(this));
            return new RecyclerListView.Holder(manageChatUserCell);
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$5(ManageChatUserCell manageChatUserCell, boolean z) {
            TLObject item = getItem(((Integer) manageChatUserCell.getTag()).intValue());
            if (item instanceof TLRPC$ChannelParticipant) {
                return createMenuForParticipant((TLRPC$ChannelParticipant) item, !z);
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$User tLRPC$User;
            SpannableStringBuilder spannableStringBuilder;
            TLObject item = getItem(i);
            if (item instanceof TLRPC$ChannelParticipant) {
                tLRPC$User = SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(MessageObject.getPeerId(((TLRPC$ChannelParticipant) item).peer)));
            } else if (!(item instanceof TLRPC$ChatParticipant)) {
                return;
            } else {
                tLRPC$User = SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(((TLRPC$ChatParticipant) item).user_id));
            }
            String str = tLRPC$User.username;
            this.searchAdapterHelper.getGroupSearch().size();
            String lastFoundChannel = this.searchAdapterHelper.getLastFoundChannel();
            if (lastFoundChannel != null) {
                String userName = UserObject.getUserName(tLRPC$User);
                spannableStringBuilder = new SpannableStringBuilder(userName);
                int indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(userName, lastFoundChannel);
                if (indexOfIgnoreCase != -1) {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(SharedMediaLayout.this.getThemedColor("windowBackgroundWhiteBlueText4")), indexOfIgnoreCase, lastFoundChannel.length() + indexOfIgnoreCase, 33);
                }
            } else {
                spannableStringBuilder = null;
            }
            ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
            manageChatUserCell.setTag(Integer.valueOf(i));
            manageChatUserCell.setData(tLRPC$User, spannableStringBuilder, null, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ManageChatUserCell) {
                ((ManageChatUserCell) view).recycle();
            }
        }
    }

    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.selectedMessagesCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.shadowLine, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.deleteItem.getIconView(), ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.deleteItem, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "actionBarActionModeDefaultSelector"));
        if (this.gotoItem != null) {
            arrayList.add(new ThemeDescription(this.gotoItem.getIconView(), ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
            arrayList.add(new ThemeDescription(this.gotoItem, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "actionBarActionModeDefaultSelector"));
        }
        if (this.forwardItem != null) {
            arrayList.add(new ThemeDescription(this.forwardItem.getIconView(), ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
            arrayList.add(new ThemeDescription(this.forwardItem, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "actionBarActionModeDefaultSelector"));
        }
        arrayList.add(new ThemeDescription(this.closeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, new Drawable[]{this.backDrawable}, null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.closeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "actionBarActionModeDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionModeLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.floatingDateView, 0, null, null, null, null, "chat_mediaTimeBackground"));
        arrayList.add(new ThemeDescription(this.floatingDateView, 0, null, null, null, null, "chat_mediaTimeText"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip, 0, new Class[]{ScrollSlidingTextTabStrip.class}, new String[]{"selectorDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "profile_tabSelectedLine"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, "profile_tabSelectedText"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, "profile_tabText"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{TextView.class}, null, null, null, "profile_tabSelector"));
        arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerBackground"));
        arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerPlayPause"));
        arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerTitle"));
        arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerPerformer"));
        arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerClose"));
        arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "returnToCallBackground"));
        arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "returnToCallText"));
        for (int i = 0; i < this.mediaPages.length; i++) {
            SharedMediaLayout$$ExternalSyntheticLambda13 sharedMediaLayout$$ExternalSyntheticLambda13 = new SharedMediaLayout$$ExternalSyntheticLambda13(this, i);
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].progressView, 0, null, null, null, null, "windowBackgroundWhite"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_SECTIONS, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_SECTIONS, new Class[]{GraySectionCell.class}, null, null, null, "graySection"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "progressCircle"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{UserCell.class}, new String[]{"adminTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "profile_creatorIcon"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, sharedMediaLayout$$ExternalSyntheticLambda13, "windowBackgroundWhiteGrayText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, sharedMediaLayout$$ExternalSyntheticLambda13, "windowBackgroundWhiteBlueText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
            TextPaint[] textPaintArr = Theme.dialogs_namePaint;
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr[0], textPaintArr[1], Theme.dialogs_searchNamePaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_name"));
            TextPaint[] textPaintArr2 = Theme.dialogs_nameEncryptedPaint;
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr2[0], textPaintArr2[1], Theme.dialogs_searchNameEncryptedPaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_secretName"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{ProfileSearchCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "avatar_backgroundRed"));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "avatar_backgroundOrange"));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "avatar_backgroundViolet"));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "avatar_backgroundGreen"));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "avatar_backgroundCyan"));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "avatar_backgroundBlue"));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "avatar_backgroundPink"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EmptyStubView.class}, new String[]{"emptyTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"dateTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{SharedDocumentCell.class}, new String[]{"progressView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "sharedMedia_startStopLoadIcon"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"statusImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "sharedMedia_startStopLoadIcon"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkbox"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"thumbImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "files_folderIcon"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"extTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "files_iconText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "progressCircle"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedAudioCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkbox"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedAudioCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedAudioCell.class}, Theme.chat_contextResult_titleTextPaint, null, null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedAudioCell.class}, Theme.chat_contextResult_descriptionTextPaint, null, null, "windowBackgroundWhiteGrayText2"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedLinkCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkbox"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedLinkCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{SharedLinkCell.class}, new String[]{"titleTextPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{SharedLinkCell.class}, null, null, null, "windowBackgroundWhiteLinkText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{SharedLinkCell.class}, Theme.linkSelectionPaint, null, null, "windowBackgroundWhiteLinkSelection"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{SharedLinkCell.class}, new String[]{"letterDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "sharedMedia_linkPlaceholderText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{SharedLinkCell.class}, new String[]{"letterDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "sharedMedia_linkPlaceholder"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_SECTIONS, new Class[]{SharedMediaSectionCell.class}, null, null, null, "windowBackgroundWhite"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_SECTIONS, new Class[]{SharedMediaSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{SharedMediaSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{SharedPhotoVideoCell.class}, new String[]{"backgroundPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "sharedMedia_photoPlaceholder"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedPhotoVideoCell.class}, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "checkbox"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedPhotoVideoCell.class}, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "checkboxCheck"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, new Class[]{ContextLinkCell.class}, new String[]{"backgroundPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "sharedMedia_photoPlaceholder"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{ContextLinkCell.class}, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "checkbox"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{ContextLinkCell.class}, null, null, sharedMediaLayout$$ExternalSyntheticLambda13, "checkboxCheck"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].listView, 0, null, null, new Drawable[]{this.pinnedHeaderShadowDrawable}, null, "windowBackgroundGrayShadow"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].emptyView.title, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.mediaPages[i].emptyView.subtitle, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText"));
        }
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$17(int i) {
        if (this.mediaPages[i].listView != null) {
            int childCount = this.mediaPages[i].listView.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = this.mediaPages[i].listView.getChildAt(i2);
                if (childAt instanceof SharedPhotoVideoCell) {
                    ((SharedPhotoVideoCell) childAt).updateCheckboxColor();
                } else if (childAt instanceof ProfileSearchCell) {
                    ((ProfileSearchCell) childAt).update(0);
                } else if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                }
            }
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.fragmentContextView) {
            canvas.save();
            canvas.clipRect(0, this.mediaPages[0].getTop(), view.getMeasuredWidth(), this.mediaPages[0].getTop() + view.getMeasuredHeight() + AndroidUtilities.dp(12.0f));
            boolean drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild;
        }
        return super.drawChild(canvas, view, j);
    }

    /* loaded from: classes3.dex */
    public class ScrollSlidingTextTabStripInner extends ScrollSlidingTextTabStrip {
        public int backgroundColor = 0;
        protected Paint backgroundPaint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ScrollSlidingTextTabStripInner(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            SharedMediaLayout.this = r1;
        }

        protected void drawBackground(Canvas canvas) {
            if (!SharedConfig.chatBlurEnabled() || this.backgroundColor == 0) {
                return;
            }
            if (this.backgroundPaint == null) {
                this.backgroundPaint = new Paint();
            }
            this.backgroundPaint.setColor(this.backgroundColor);
            android.graphics.Rect rect = AndroidUtilities.rectTmp2;
            rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
            SharedMediaLayout.this.drawBackgroundWithBlur(canvas, getY(), rect, this.backgroundPaint);
        }

        @Override // android.view.View
        public void setBackgroundColor(int i) {
            this.backgroundColor = i;
            invalidate();
        }
    }

    public int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}