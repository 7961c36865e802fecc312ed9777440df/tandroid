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
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
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
import androidx.collection.LongSparseArray;
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
import org.telegram.messenger.AnimationNotificationsLocker;
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
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
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
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
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
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.TLRPC$messages_Chats;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_stories_storyViews;
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
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;
import org.telegram.ui.Components.SharedMediaLayout;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.UserListPoller;
import org.telegram.ui.Stories.ViewsForPeerStoriesRequester;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.TopicsFragment;
/* loaded from: classes4.dex */
public class SharedMediaLayout extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private ActionBar actionBar;
    private AnimatorSet actionModeAnimation;
    private LinearLayout actionModeLayout;
    private ArrayList<View> actionModeViews;
    private float additionalFloatingTranslation;
    private boolean allowStoriesSingleColumn;
    private int animateToColumnsCount;
    private boolean animatingForward;
    private StoriesAdapter animationSupportingArchivedStoriesAdapter;
    private SharedPhotoVideoAdapter animationSupportingPhotoVideoAdapter;
    private ArrayList<SharedPhotoVideoCell2> animationSupportingSortedCells;
    private int animationSupportingSortedCellsOffset;
    private StoriesAdapter animationSupportingStoriesAdapter;
    private StoriesAdapter archivedStoriesAdapter;
    private SharedDocumentsAdapter audioAdapter;
    private ArrayList<SharedAudioCell> audioCache;
    private ArrayList<SharedAudioCell> audioCellCache;
    private MediaSearchAdapter audioSearchAdapter;
    private boolean backAnimation;
    private BackDrawable backDrawable;
    private Paint backgroundPaint;
    private ArrayList<SharedPhotoVideoCell> cache;
    private int cantDeleteMessagesCount;
    private ArrayList<SharedPhotoVideoCell> cellCache;
    private int changeColumnsTab;
    private boolean changeTypeAnimation;
    private ChannelRecommendationsAdapter channelRecommendationsAdapter;
    private ChatUsersAdapter chatUsersAdapter;
    private ImageView closeButton;
    private CommonGroupsAdapter commonGroupsAdapter;
    final Delegate delegate;
    private ActionBarMenuItem deleteItem;
    private long dialog_id;
    private boolean disableScrolling;
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
    private Runnable hideFloatingDateRunnable;
    private boolean ignoreSearchCollapse;
    private TLRPC$ChatFull info;
    private int initialTab;
    protected boolean isActionModeShowed;
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
    private int[] mediaColumnsCount;
    private MediaPage[] mediaPages;
    private ActionBarMenuSubItem mediaZoomInItem;
    private ActionBarMenuSubItem mediaZoomOutItem;
    private long mergeDialogId;
    SparseArray<Float> messageAlphaEnter;
    AnimationNotificationsLocker notificationsLocker;
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
    private PhotoViewer.PhotoViewerProvider provider;
    android.graphics.Rect rect;
    private Theme.ResourcesProvider resourcesProvider;
    public ScrollSlidingTextTabStripInner scrollSlidingTextTabStrip;
    private boolean scrolling;
    public boolean scrollingByUser;
    private ActionBarMenuItem searchItem;
    private int searchItemState;
    private boolean searchWas;
    private boolean searching;
    private SparseArray<MessageObject>[] selectedFiles;
    private NumberTextView selectedMessagesCountTextView;
    private View shadowLine;
    SharedLinkCell.SharedLinkCellDelegate sharedLinkCellDelegate;
    private SharedMediaData[] sharedMediaData;
    private SharedMediaPreloader sharedMediaPreloader;
    private float shiftDp;
    private boolean startedTracking;
    private int startedTrackingPointerId;
    private int startedTrackingX;
    private int startedTrackingY;
    private StoriesAdapter storiesAdapter;
    private boolean storiesColumnsCountSet;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    int topPadding;
    private int topicId;
    private TLRPC$UserFull userInfo;
    private VelocityTracker velocityTracker;
    private final int viewType;
    private SharedDocumentsAdapter voiceAdapter;
    private static final int[] supportedFastScrollTypes = {0, 1, 2, 4};
    private static final Interpolator interpolator = SharedMediaLayout$$ExternalSyntheticLambda6.INSTANCE;

    /* loaded from: classes4.dex */
    public interface Delegate {
        boolean canSearchMembers();

        TLRPC$Chat getCurrentChat();

        RecyclerListView getListView();

        boolean isFragmentOpened();

        boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, boolean z2, View view);

        void scrollToSharedMedia();

        void updateSelectedMediaTabText();
    }

    /* loaded from: classes4.dex */
    public interface SharedMediaPreloaderDelegate {
        void mediaCountUpdated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$7(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ float lambda$static$1(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFloatingDateView() {
    }

    protected boolean canShowSearchItem() {
        return true;
    }

    protected int getInitialTab() {
        return 0;
    }

    protected boolean includeStories() {
        return true;
    }

    protected void invalidateBlur() {
    }

    protected boolean isArchivedOnlyStoriesView() {
        return false;
    }

    protected boolean isStoriesView() {
        return false;
    }

    public boolean isTabZoomable(int i) {
        return i == 0 || i == 8 || i == 9;
    }

    protected void onActionModeSelectedUpdate(SparseArray<MessageObject> sparseArray) {
    }

    protected boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, View view) {
        return false;
    }

    protected void onSearchStateChanged(boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onTabProgress(float f) {
    }

    protected void onTabScroll(boolean z) {
    }

    protected int processColor(int i) {
        return i;
    }

    static /* synthetic */ int access$5208(SharedMediaLayout sharedMediaLayout) {
        int i = sharedMediaLayout.animationSupportingSortedCellsOffset;
        sharedMediaLayout.animationSupportingSortedCellsOffset = i + 1;
        return i;
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
        RecyclerView.Adapter adapter;
        int i = this.mediaPages[0].selectedType;
        if ((i == 0 || i == 8 || i == 9) && getParent() != null) {
            if (!this.photoVideoChangeColumnsAnimation || this.isInPinchToZoomTouchMode) {
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
                    int i2 = -1;
                    int i3 = -1;
                    for (int i4 = 0; i4 < motionEvent.getPointerCount(); i4++) {
                        if (this.pointerId1 == motionEvent.getPointerId(i4)) {
                            i2 = i4;
                        }
                        if (this.pointerId2 == motionEvent.getPointerId(i4)) {
                            i3 = i4;
                        }
                    }
                    if (i2 == -1 || i3 == -1) {
                        this.maybePinchToZoomTouchMode = false;
                        this.maybePinchToZoomTouchMode2 = false;
                        this.isInPinchToZoomTouchMode = false;
                        finishPinchToMediaColumnsCount();
                        return false;
                    }
                    float hypot = ((float) Math.hypot(motionEvent.getX(i3) - motionEvent.getX(i2), motionEvent.getY(i3) - motionEvent.getY(i2))) / this.pinchStartDistance;
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
                            int i5 = this.changeColumnsTab;
                            if (i5 == 8) {
                                adapter = this.storiesAdapter;
                            } else if (i5 == 9) {
                                adapter = this.archivedStoriesAdapter;
                            } else {
                                adapter = this.photoVideoAdapter;
                            }
                            if (f == 1.0f) {
                                int ceil = (int) Math.ceil(this.pinchCenterPosition / this.animateToColumnsCount);
                                float measuredWidth = this.startedTrackingX / (this.mediaPages[0].listView.getMeasuredWidth() - ((int) (this.mediaPages[0].listView.getMeasuredWidth() / this.animateToColumnsCount)));
                                int i6 = this.animateToColumnsCount;
                                int i7 = (ceil * i6) + ((int) (measuredWidth * (i6 - 1)));
                                if (i7 >= adapter.getItemCount()) {
                                    i7 = adapter.getItemCount() - 1;
                                }
                                this.pinchCenterPosition = i7;
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
            return true;
        }
        return false;
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
        if (this.delegate.canSearchMembers() && this.pinchCenterPosition == -1) {
            this.pinchCenterPosition = (int) (this.mediaPages[0].layoutManager.findFirstVisibleItemPosition() + ((this.mediaColumnsCount[(this.mediaPages[0].selectedType == 8 || this.mediaPages[0].selectedType == 9) ? (char) 1 : (char) 0] - 1) * Math.min(1.0f, Math.max(i / this.mediaPages[0].listView.getMeasuredWidth(), 0.0f))));
            this.pinchCenterOffset = 0;
        }
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
        return (this.photoVideoChangeColumnsAnimation || this.tabsAnimationInProgress) ? false : true;
    }

    public int getPhotosVideosTypeFilter() {
        return this.sharedMediaData[0].filterType;
    }

    public boolean isPinnedToTop() {
        return this.isPinnedToTop;
    }

    public void setPinnedToTop(boolean z) {
        if (this.isPinnedToTop == z) {
            return;
        }
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

    public void drawListForBlur(Canvas canvas) {
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i >= mediaPageArr.length) {
                return;
            }
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
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class MediaPage extends FrameLayout {
        private ClippingImageView animatingImageView;
        private GridLayoutManager animationSupportingLayoutManager;
        private InternalListView animationSupportingListView;
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
        private InternalListView listView;
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
            SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip;
            super.dispatchDraw(canvas);
            SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip2 = this.fastScrollHintView;
            if (sharedMediaFastScrollTooltip2 == null || sharedMediaFastScrollTooltip2.getVisibility() != 0) {
                return;
            }
            RecyclerListView.FastScroll fastScroll = this.listView.getFastScroll();
            if (fastScroll != null) {
                float scrollBarY = fastScroll.getScrollBarY() + AndroidUtilities.dp(36.0f);
                if (this.selectedType == 9) {
                    scrollBarY += AndroidUtilities.dp(64.0f);
                }
                this.fastScrollHintView.setPivotX(sharedMediaFastScrollTooltip.getMeasuredWidth());
                this.fastScrollHintView.setPivotY(0.0f);
                this.fastScrollHintView.setTranslationX((getMeasuredWidth() - this.fastScrollHintView.getMeasuredWidth()) - AndroidUtilities.dp(16.0f));
                this.fastScrollHintView.setTranslationY(scrollBarY);
            }
            if (fastScroll.getProgress() > 0.85f) {
                SharedMediaLayout.showFastScrollHint(this, null, false);
            }
        }
    }

    public float getPhotoVideoOptionsAlpha(float f) {
        float f2 = 0.0f;
        if (isArchivedOnlyStoriesView()) {
            return 0.0f;
        }
        MediaPage[] mediaPageArr = this.mediaPages;
        if (mediaPageArr[1] != null && (mediaPageArr[1].selectedType == 0 || this.mediaPages[1].selectedType == 8 || this.mediaPages[1].selectedType == 9)) {
            f2 = 0.0f + f;
        }
        MediaPage[] mediaPageArr2 = this.mediaPages;
        return mediaPageArr2[0] != null ? (mediaPageArr2[0].selectedType == 0 || this.mediaPages[0].selectedType == 8 || this.mediaPages[0].selectedType == 9) ? f2 + (1.0f - f) : f2 : f2;
    }

    public void updateFastScrollVisibility(MediaPage mediaPage, boolean z) {
        boolean z2 = mediaPage.fastScrollEnabled && this.isPinnedToTop;
        RecyclerListView.FastScroll fastScroll = mediaPage.listView.getFastScroll();
        ObjectAnimator objectAnimator = mediaPage.fastScrollAnimator;
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            mediaPage.fastScrollAnimator.cancel();
        }
        if (!z) {
            fastScroll.animate().setListener(null).cancel();
            fastScroll.setVisibility(z2 ? 0 : 8);
            fastScroll.setTag(z2 ? 1 : null);
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
            fastScroll.setTag(r2);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        hideFloatingDateView(true);
    }

    /* loaded from: classes4.dex */
    public static class SharedMediaPreloader implements NotificationCenter.NotificationCenterDelegate {
        private long dialogId;
        private boolean mediaWasLoaded;
        private long mergeDialogId;
        private BaseFragment parentFragment;
        private SharedMediaData[] sharedMediaData;
        private int topicId;
        private int[] mediaCount = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int[] mediaMergeCount = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int[] lastMediaCount = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int[] lastLoadMediaCount = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private ArrayList<SharedMediaPreloaderDelegate> delegates = new ArrayList<>();

        public SharedMediaPreloader(BaseFragment baseFragment) {
            this.parentFragment = baseFragment;
            if (baseFragment instanceof ChatActivityInterface) {
                ChatActivityInterface chatActivityInterface = (ChatActivityInterface) baseFragment;
                this.dialogId = chatActivityInterface.getDialogId();
                this.mergeDialogId = chatActivityInterface.getMergeDialogId();
                this.topicId = chatActivityInterface.getTopicId();
            } else if (baseFragment instanceof ProfileActivity) {
                ProfileActivity profileActivity = (ProfileActivity) baseFragment;
                this.dialogId = profileActivity.getDialogId();
                this.topicId = profileActivity.getTopicId();
            } else if (baseFragment instanceof MediaActivity) {
                this.dialogId = ((MediaActivity) baseFragment).getDialogId();
            }
            this.sharedMediaData = new SharedMediaData[6];
            int i = 0;
            while (true) {
                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                if (i < sharedMediaDataArr.length) {
                    sharedMediaDataArr[i] = new SharedMediaData();
                    this.sharedMediaData[i].setMaxId(0, DialogObject.isEncryptedDialog(this.dialogId) ? Integer.MIN_VALUE : ConnectionsManager.DEFAULT_DATACENTER_ID);
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
                    notificationCenter.addObserver(this, NotificationCenter.storiesListUpdated);
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
            notificationCenter.removeObserver(this, NotificationCenter.storiesListUpdated);
        }

        public int[] getLastMediaCount() {
            return this.lastMediaCount;
        }

        public SharedMediaData[] getSharedMediaData() {
            return this.sharedMediaData;
        }

        /* JADX WARN: Removed duplicated region for block: B:42:0x00a7  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x00ac  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x0182 A[LOOP:2: B:72:0x0180->B:73:0x0182, LOOP_END] */
        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void didReceivedNotification(int i, final int i2, Object... objArr) {
            int mediaType;
            int size;
            int i3;
            int[] iArr;
            int i4;
            int i5;
            int i6 = 0;
            if (i == NotificationCenter.mediaCountsDidLoad) {
                long longValue = ((Long) objArr[0]).longValue();
                int intValue = ((Integer) objArr[1]).intValue();
                if (this.topicId == intValue) {
                    long j = this.dialogId;
                    if (longValue == j || longValue == this.mergeDialogId) {
                        int[] iArr2 = (int[]) objArr[2];
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
                                        i4 = intValue;
                                    } else {
                                        if (i7 == 0) {
                                            SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                                            if (sharedMediaDataArr[0].filterType == 1) {
                                                i5 = 6;
                                            } else if (sharedMediaDataArr[0].filterType == 2) {
                                                i5 = 7;
                                            }
                                            i3 = i7;
                                            iArr = iArr2;
                                            i4 = intValue;
                                            this.parentFragment.getMediaDataController().loadMedia(longValue, this.lastLoadMediaCount[i7] != -1 ? 30 : 20, 0, 0, i5, i4, 2, this.parentFragment.getClassGuid(), 0);
                                            this.lastLoadMediaCount[i3] = this.mediaCount[i3];
                                        }
                                        i5 = i7;
                                        i3 = i7;
                                        iArr = iArr2;
                                        i4 = intValue;
                                        this.parentFragment.getMediaDataController().loadMedia(longValue, this.lastLoadMediaCount[i7] != -1 ? 30 : 20, 0, 0, i5, i4, 2, this.parentFragment.getClassGuid(), 0);
                                        this.lastLoadMediaCount[i3] = this.mediaCount[i3];
                                    }
                                    i7 = i3 + 1;
                                    iArr2 = iArr;
                                    intValue = i4;
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
                            i4 = intValue;
                            i7 = i3 + 1;
                            iArr2 = iArr;
                            intValue = i4;
                        }
                        this.mediaWasLoaded = true;
                        int size2 = this.delegates.size();
                        while (i6 < size2) {
                            this.delegates.get(i6).mediaCountUpdated();
                            i6++;
                        }
                    }
                }
            } else if (i == NotificationCenter.mediaCountDidLoad) {
                long longValue2 = ((Long) objArr[0]).longValue();
                long intValue2 = ((Integer) objArr[1]).intValue();
                if ((longValue2 == this.dialogId || longValue2 == this.mergeDialogId) && this.topicId == intValue2) {
                    int intValue3 = ((Integer) objArr[4]).intValue();
                    int intValue4 = ((Integer) objArr[2]).intValue();
                    if (longValue2 == this.dialogId) {
                        this.mediaCount[intValue3] = intValue4;
                    } else {
                        this.mediaMergeCount[intValue3] = intValue4;
                    }
                    int[] iArr5 = this.mediaCount;
                    if (iArr5[intValue3] >= 0) {
                        int[] iArr6 = this.mediaMergeCount;
                        if (iArr6[intValue3] >= 0) {
                            this.lastMediaCount[intValue3] = iArr5[intValue3] + iArr6[intValue3];
                            size = this.delegates.size();
                            while (i6 < size) {
                                this.delegates.get(i6).mediaCountUpdated();
                                i6++;
                            }
                        }
                    }
                    if (iArr5[intValue3] >= 0) {
                        this.lastMediaCount[intValue3] = iArr5[intValue3];
                    } else {
                        this.lastMediaCount[intValue3] = Math.max(this.mediaMergeCount[intValue3], 0);
                    }
                    size = this.delegates.size();
                    while (i6 < size) {
                    }
                }
            } else if (i == NotificationCenter.didReceiveNewMessages) {
                if (!((Boolean) objArr[2]).booleanValue() && this.dialogId == ((Long) objArr[0]).longValue()) {
                    boolean isEncryptedDialog = DialogObject.isEncryptedDialog(this.dialogId);
                    ArrayList arrayList = (ArrayList) objArr[1];
                    for (int i8 = 0; i8 < arrayList.size(); i8++) {
                        MessageObject messageObject = (MessageObject) arrayList.get(i8);
                        int i9 = this.topicId;
                        if ((i9 == 0 || i9 == MessageObject.getTopicId(messageObject.messageOwner, true)) && MessageObject.getMedia(messageObject.messageOwner) != null && !messageObject.needDrawBluredPreview() && (mediaType = MediaDataController.getMediaType(messageObject.messageOwner)) != -1 && ((mediaType != 0 || this.sharedMediaData[0].filterType != 2 || messageObject.isVideo()) && (mediaType != 0 || this.sharedMediaData[0].filterType != 1 || !messageObject.isVideo()))) {
                            SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                            if (sharedMediaDataArr2[mediaType].startReached) {
                                sharedMediaDataArr2[mediaType].addMessage(messageObject, 0, true, isEncryptedDialog);
                            }
                            if (this.topicId == 0) {
                                this.sharedMediaData[mediaType].totalCount++;
                            }
                            for (int i10 = 0; i10 < this.sharedMediaData[mediaType].fastScrollPeriods.size(); i10++) {
                                this.sharedMediaData[mediaType].fastScrollPeriods.get(i10).startOffset++;
                            }
                        }
                    }
                    loadMediaCounts();
                }
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
                if (((Integer) objArr[3]).intValue() == this.parentFragment.getClassGuid()) {
                    int intValue5 = ((Integer) objArr[4]).intValue();
                    if (intValue5 != 0 && intValue5 != 6 && intValue5 != 7 && intValue5 != 1 && intValue5 != 2 && intValue5 != 4) {
                        this.sharedMediaData[intValue5].setTotalCount(((Integer) objArr[1]).intValue());
                    }
                    ArrayList arrayList2 = (ArrayList) objArr[2];
                    boolean isEncryptedDialog2 = DialogObject.isEncryptedDialog(longValue3);
                    int i11 = longValue3 == this.dialogId ? 0 : 1;
                    if (intValue5 == 0 || intValue5 == 6 || intValue5 == 7) {
                        if (intValue5 != this.sharedMediaData[0].filterType) {
                            return;
                        }
                        intValue5 = 0;
                    }
                    if (!arrayList2.isEmpty()) {
                        this.sharedMediaData[intValue5].setEndReached(i11, ((Boolean) objArr[5]).booleanValue());
                    }
                    for (int i12 = 0; i12 < arrayList2.size(); i12++) {
                        this.sharedMediaData[intValue5].addMessage((MessageObject) arrayList2.get(i12), i11, false, isEncryptedDialog2);
                    }
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
                for (int i13 = 0; i13 < size3; i13++) {
                    int i14 = 0;
                    while (true) {
                        SharedMediaData[] sharedMediaDataArr4 = this.sharedMediaData;
                        if (i14 < sharedMediaDataArr4.length) {
                            MessageObject deleteMessage = sharedMediaDataArr4[i14].deleteMessage(((Integer) arrayList3.get(i13)).intValue(), 0);
                            if (deleteMessage != null) {
                                if (deleteMessage.getDialogId() == this.dialogId && (this.topicId == 0 || MessageObject.getTopicId(deleteMessage.messageOwner, true) == this.topicId)) {
                                    int[] iArr7 = this.mediaCount;
                                    if (iArr7[i14] > 0) {
                                        iArr7[i14] = iArr7[i14] - 1;
                                    }
                                } else {
                                    int[] iArr8 = this.mediaMergeCount;
                                    if (iArr8[i14] > 0) {
                                        iArr8[i14] = iArr8[i14] - 1;
                                    }
                                }
                                z = true;
                            }
                            i14++;
                        }
                    }
                }
                if (z) {
                    int i15 = 0;
                    while (true) {
                        int[] iArr9 = this.mediaCount;
                        if (i15 >= iArr9.length) {
                            break;
                        }
                        if (iArr9[i15] >= 0) {
                            int[] iArr10 = this.mediaMergeCount;
                            if (iArr10[i15] >= 0) {
                                this.lastMediaCount[i15] = iArr9[i15] + iArr10[i15];
                                i15++;
                            }
                        }
                        if (iArr9[i15] >= 0) {
                            this.lastMediaCount[i15] = iArr9[i15];
                        } else {
                            this.lastMediaCount[i15] = Math.max(this.mediaMergeCount[i15], 0);
                        }
                        i15++;
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
                if (longValue5 == j2 || longValue5 == this.mergeDialogId) {
                    int i16 = longValue5 == j2 ? 0 : 1;
                    ArrayList arrayList4 = (ArrayList) objArr[1];
                    int size5 = arrayList4.size();
                    for (int i17 = 0; i17 < size5; i17++) {
                        MessageObject messageObject2 = (MessageObject) arrayList4.get(i17);
                        int id = messageObject2.getId();
                        int topicId = MessageObject.getTopicId(messageObject2.messageOwner, true);
                        int mediaType2 = MediaDataController.getMediaType(messageObject2.messageOwner);
                        int i18 = this.topicId;
                        if (i18 == 0 || topicId == i18) {
                            int i19 = 0;
                            while (true) {
                                SharedMediaData[] sharedMediaDataArr5 = this.sharedMediaData;
                                if (i19 >= sharedMediaDataArr5.length) {
                                    break;
                                }
                                MessageObject messageObject3 = sharedMediaDataArr5[i19].messagesDict[i16].get(id);
                                if (messageObject3 != null) {
                                    int mediaType3 = MediaDataController.getMediaType(messageObject2.messageOwner);
                                    if (mediaType2 == -1 || mediaType3 != mediaType2) {
                                        this.sharedMediaData[i19].deleteMessage(id, i16);
                                        if (i16 == 0) {
                                            int[] iArr11 = this.mediaCount;
                                            if (iArr11[i19] > 0) {
                                                iArr11[i19] = iArr11[i19] - 1;
                                            }
                                        } else {
                                            int[] iArr12 = this.mediaMergeCount;
                                            if (iArr12[i19] > 0) {
                                                iArr12[i19] = iArr12[i19] - 1;
                                            }
                                        }
                                    } else {
                                        int indexOf = this.sharedMediaData[i19].messages.indexOf(messageObject3);
                                        if (indexOf >= 0) {
                                            this.sharedMediaData[i19].messagesDict[i16].put(id, messageObject2);
                                            this.sharedMediaData[i19].messages.set(indexOf, messageObject2);
                                        }
                                    }
                                } else {
                                    i19++;
                                }
                            }
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
                final ArrayList arrayList5 = new ArrayList();
                int i20 = 0;
                while (true) {
                    SharedMediaData[] sharedMediaDataArr6 = this.sharedMediaData;
                    if (i20 >= sharedMediaDataArr6.length) {
                        break;
                    }
                    arrayList5.addAll(sharedMediaDataArr6[i20].messages);
                    i20++;
                }
                final String str = (String) objArr[0];
                if (str != null) {
                    Utilities.globalQueue.postRunnable(new Runnable(this) { // from class: org.telegram.ui.Components.SharedMediaLayout.SharedMediaPreloader.1
                        @Override // java.lang.Runnable
                        public void run() {
                            int i21 = 0;
                            while (i21 < arrayList5.size()) {
                                if (!str.equals(((MessageObject) arrayList5.get(i21)).getFileName())) {
                                    arrayList5.remove(i21);
                                    i21--;
                                }
                                i21++;
                            }
                            if (arrayList5.size() > 0) {
                                FileLoader.getInstance(i2).checkMediaExistance(arrayList5);
                            }
                        }
                    });
                }
            }
        }

        private void loadMediaCounts() {
            this.parentFragment.getMediaDataController().getMediaCounts(this.dialogId, this.topicId, this.parentFragment.getClassGuid());
            if (this.mergeDialogId != 0) {
                this.parentFragment.getMediaDataController().getMediaCounts(this.mergeDialogId, this.topicId, this.parentFragment.getClassGuid());
            }
        }

        private void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
            if (tLRPC$ChatFull != null) {
                long j = tLRPC$ChatFull.migrated_from_chat_id;
                if (j == 0 || this.mergeDialogId != 0) {
                    return;
                }
                this.mergeDialogId = -j;
                this.parentFragment.getMediaDataController().getMediaCounts(this.mergeDialogId, this.topicId, this.parentFragment.getClassGuid());
            }
        }

        public boolean isMediaWasLoaded() {
            return this.mediaWasLoaded;
        }
    }

    /* loaded from: classes4.dex */
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

        static /* synthetic */ int access$710(SharedMediaData sharedMediaData) {
            int i = sharedMediaData.startOffset;
            sharedMediaData.startOffset = i - 1;
            return i;
        }

        static /* synthetic */ int access$8410(SharedMediaData sharedMediaData) {
            int i = sharedMediaData.endLoadingStubs;
            sharedMediaData.endLoadingStubs = i - 1;
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
            int i3 = this.totalCount - 1;
            this.totalCount = i3;
            if (i3 < 0) {
                this.totalCount = 0;
            }
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
            if (z) {
                this.frozenStartOffset = this.startOffset;
                this.frozenEndLoadingStubs = this.endLoadingStubs;
                this.frozenMessages.clear();
                this.frozenMessages.addAll(this.messages);
            }
        }

        public int getEndLoadingStubs() {
            return this.isFrozen ? this.frozenEndLoadingStubs : this.endLoadingStubs;
        }
    }

    /* loaded from: classes4.dex */
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

    public boolean hasInternet() {
        return this.profileActivity.getConnectionsManager().getConnectionState() == 3;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SharedMediaLayout(Context context, long j, SharedMediaPreloader sharedMediaPreloader, int i, ArrayList<Integer> arrayList, TLRPC$ChatFull tLRPC$ChatFull, TLRPC$UserFull tLRPC$UserFull, int i2, BaseFragment baseFragment, Delegate delegate, int i3, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i4;
        int i5;
        int i6;
        RecyclerListView.Holder holder;
        TLRPC$ChatFull tLRPC$ChatFull2;
        TLRPC$ChatFull tLRPC$ChatFull3 = tLRPC$ChatFull;
        this.rect = new android.graphics.Rect();
        this.mediaPages = new MediaPage[2];
        this.cellCache = new ArrayList<>(10);
        this.cache = new ArrayList<>(10);
        this.audioCellCache = new ArrayList<>(10);
        this.audioCache = new ArrayList<>(10);
        this.hideFloatingDateRunnable = new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                SharedMediaLayout.this.lambda$new$0();
            }
        };
        this.actionModeViews = new ArrayList<>();
        this.backgroundPaint = new Paint();
        this.selectedFiles = new SparseArray[]{new SparseArray<>(), new SparseArray<>()};
        this.allowStoriesSingleColumn = false;
        this.storiesColumnsCountSet = false;
        this.mediaColumnsCount = new int[]{3, 3};
        this.animationSupportingSortedCells = new ArrayList<>();
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.Components.SharedMediaLayout.1
            /* JADX WARN: Removed duplicated region for block: B:102:0x0154 A[SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:104:0x0241 A[SYNTHETIC] */
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i7, boolean z) {
                SharedLinkCell sharedLinkCell;
                MessageObject message;
                ImageReceiver linkImageView;
                View pinnedHeader;
                if (messageObject != null && (SharedMediaLayout.this.mediaPages[0].selectedType == 0 || SharedMediaLayout.this.mediaPages[0].selectedType == 1 || SharedMediaLayout.this.mediaPages[0].selectedType == 3 || SharedMediaLayout.this.mediaPages[0].selectedType == 5)) {
                    InternalListView internalListView = SharedMediaLayout.this.mediaPages[0].listView;
                    int childCount = internalListView.getChildCount();
                    int i8 = -1;
                    int i9 = -1;
                    for (int i10 = 0; i10 < childCount; i10++) {
                        View childAt = internalListView.getChildAt(i10);
                        int measuredHeight = SharedMediaLayout.this.mediaPages[0].listView.getMeasuredHeight();
                        View view = (View) SharedMediaLayout.this.getParent();
                        if (view != null && SharedMediaLayout.this.getY() + SharedMediaLayout.this.getMeasuredHeight() > view.getMeasuredHeight()) {
                            measuredHeight -= SharedMediaLayout.this.getBottom() - view.getMeasuredHeight();
                        }
                        if (childAt.getTop() < measuredHeight) {
                            int childAdapterPosition = internalListView.getChildAdapterPosition(childAt);
                            if (childAdapterPosition < i8 || i8 == -1) {
                                i8 = childAdapterPosition;
                            }
                            if (childAdapterPosition > i9 || i9 == -1) {
                                i9 = childAdapterPosition;
                            }
                            int[] iArr = new int[2];
                            if (childAt instanceof SharedPhotoVideoCell2) {
                                SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) childAt;
                                MessageObject messageObject2 = sharedPhotoVideoCell2.getMessageObject();
                                if (messageObject2 != null) {
                                    if (messageObject2.getId() == messageObject.getId()) {
                                        linkImageView = sharedPhotoVideoCell2.imageReceiver;
                                        sharedPhotoVideoCell2.getLocationInWindow(iArr);
                                        iArr[0] = iArr[0] + Math.round(sharedPhotoVideoCell2.imageReceiver.getImageX());
                                        iArr[1] = iArr[1] + Math.round(sharedPhotoVideoCell2.imageReceiver.getImageY());
                                        if (linkImageView == null) {
                                            PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                                            placeProviderObject.viewX = iArr[0];
                                            placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
                                            placeProviderObject.parentView = internalListView;
                                            placeProviderObject.animatingImageView = SharedMediaLayout.this.mediaPages[0].animatingImageView;
                                            SharedMediaLayout.this.mediaPages[0].listView.getLocationInWindow(iArr);
                                            placeProviderObject.animatingImageViewYOffset = -iArr[1];
                                            placeProviderObject.imageReceiver = linkImageView;
                                            placeProviderObject.allowTakeAnimation = true;
                                            placeProviderObject.radius = linkImageView.getRoundRadius();
                                            placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
                                            placeProviderObject.parentView.getLocationInWindow(iArr);
                                            placeProviderObject.clipTopAddition = 0;
                                            placeProviderObject.starOffset = SharedMediaLayout.this.sharedMediaData[0].startOffset;
                                            if (SharedMediaLayout.this.fragmentContextView != null && SharedMediaLayout.this.fragmentContextView.getVisibility() == 0) {
                                                placeProviderObject.clipTopAddition += AndroidUtilities.dp(36.0f);
                                            }
                                            if (PhotoViewer.isShowingImage(messageObject) && (pinnedHeader = internalListView.getPinnedHeader()) != null) {
                                                int height = (SharedMediaLayout.this.fragmentContextView == null || SharedMediaLayout.this.fragmentContextView.getVisibility() != 0) ? 0 : (SharedMediaLayout.this.fragmentContextView.getHeight() - AndroidUtilities.dp(2.5f)) + 0;
                                                boolean z2 = childAt instanceof SharedDocumentCell;
                                                if (z2) {
                                                    height += AndroidUtilities.dp(8.0f);
                                                }
                                                int i11 = height - placeProviderObject.viewY;
                                                if (i11 > childAt.getHeight()) {
                                                    internalListView.scrollBy(0, -(i11 + pinnedHeader.getHeight()));
                                                } else {
                                                    int height2 = placeProviderObject.viewY - internalListView.getHeight();
                                                    if (z2) {
                                                        height2 -= AndroidUtilities.dp(8.0f);
                                                    }
                                                    if (height2 >= 0) {
                                                        internalListView.scrollBy(0, height2 + childAt.getHeight());
                                                    }
                                                }
                                            }
                                            return placeProviderObject;
                                        }
                                    }
                                    linkImageView = null;
                                    if (linkImageView == null) {
                                    }
                                } else {
                                    continue;
                                }
                            } else if (childAt instanceof SharedDocumentCell) {
                                SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) childAt;
                                if (sharedDocumentCell.getMessage().getId() == messageObject.getId()) {
                                    BackupImageView imageView = sharedDocumentCell.getImageView();
                                    linkImageView = imageView.getImageReceiver();
                                    imageView.getLocationInWindow(iArr);
                                    if (linkImageView == null) {
                                    }
                                }
                                linkImageView = null;
                                if (linkImageView == null) {
                                }
                            } else if (childAt instanceof ContextLinkCell) {
                                ContextLinkCell contextLinkCell = (ContextLinkCell) childAt;
                                MessageObject messageObject3 = (MessageObject) contextLinkCell.getParentObject();
                                if (messageObject3 != null && messageObject3.getId() == messageObject.getId()) {
                                    linkImageView = contextLinkCell.getPhotoImage();
                                    contextLinkCell.getLocationInWindow(iArr);
                                    if (linkImageView == null) {
                                    }
                                }
                                linkImageView = null;
                                if (linkImageView == null) {
                                }
                            } else {
                                if ((childAt instanceof SharedLinkCell) && (message = (sharedLinkCell = (SharedLinkCell) childAt).getMessage()) != null && message.getId() == messageObject.getId()) {
                                    linkImageView = sharedLinkCell.getLinkImageView();
                                    sharedLinkCell.getLocationInWindow(iArr);
                                    if (linkImageView == null) {
                                    }
                                }
                                linkImageView = null;
                                if (linkImageView == null) {
                                }
                            }
                        }
                    }
                    if (SharedMediaLayout.this.mediaPages[0].selectedType == 0 && i8 >= 0 && i9 >= 0) {
                        int positionForIndex = SharedMediaLayout.this.photoVideoAdapter.getPositionForIndex(i7);
                        if (positionForIndex <= i8) {
                            SharedMediaLayout.this.mediaPages[0].layoutManager.scrollToPositionWithOffset(positionForIndex, 0);
                            SharedMediaLayout.this.delegate.scrollToSharedMedia();
                        } else if (positionForIndex >= i9 && i9 >= 0) {
                            SharedMediaLayout.this.mediaPages[0].layoutManager.scrollToPositionWithOffset(positionForIndex, 0, true);
                            SharedMediaLayout.this.delegate.scrollToSharedMedia();
                        }
                    }
                }
                return null;
            }
        };
        this.shiftDp = -5.0f;
        this.sharedMediaData = new SharedMediaData[6];
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.messageAlphaEnter = new SparseArray<>();
        this.sharedLinkCellDelegate = new 36();
        this.viewType = i3;
        this.resourcesProvider = resourcesProvider;
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        this.globalGradientView = flickerLoadingView;
        flickerLoadingView.setIsSingleCell(true);
        this.sharedMediaPreloader = sharedMediaPreloader;
        this.delegate = delegate;
        int[] lastMediaCount = sharedMediaPreloader.getLastMediaCount();
        int i7 = this.sharedMediaPreloader.topicId;
        this.topicId = i7;
        int[] iArr = new int[7];
        iArr[0] = lastMediaCount[0];
        iArr[1] = lastMediaCount[1];
        iArr[2] = lastMediaCount[2];
        iArr[3] = lastMediaCount[3];
        iArr[4] = lastMediaCount[4];
        iArr[5] = lastMediaCount[5];
        iArr[6] = i7 == 0 ? i : 0;
        this.hasMedia = iArr;
        if (i2 == 10) {
            this.initialTab = i2;
        } else if ((tLRPC$UserFull != null && tLRPC$UserFull.stories_pinned_available) || ((tLRPC$ChatFull3 != null && tLRPC$ChatFull3.stories_pinned_available) || isStoriesView())) {
            this.initialTab = getInitialTab();
        } else if (i2 != -1 && this.topicId == 0) {
            this.initialTab = i2;
        } else {
            int i8 = 0;
            while (true) {
                int[] iArr2 = this.hasMedia;
                if (i8 >= iArr2.length) {
                    break;
                } else if (iArr2[i8] == -1 || iArr2[i8] > 0) {
                    break;
                } else {
                    i8++;
                }
            }
            this.initialTab = i8;
        }
        this.info = tLRPC$ChatFull3;
        this.userInfo = tLRPC$UserFull;
        if (tLRPC$ChatFull3 != null) {
            this.mergeDialogId = -tLRPC$ChatFull3.migrated_from_chat_id;
        }
        this.dialog_id = j;
        int i9 = 0;
        while (true) {
            SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
            if (i9 >= sharedMediaDataArr.length) {
                break;
            }
            sharedMediaDataArr[i9] = new SharedMediaData();
            this.sharedMediaData[i9].max_id[0] = DialogObject.isEncryptedDialog(this.dialog_id) ? Integer.MIN_VALUE : ConnectionsManager.DEFAULT_DATACENTER_ID;
            fillMediaData(i9);
            if (this.mergeDialogId != 0 && (tLRPC$ChatFull2 = this.info) != null) {
                SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                sharedMediaDataArr2[i9].max_id[1] = tLRPC$ChatFull2.migrated_from_max_id;
                sharedMediaDataArr2[i9].endReached[1] = false;
            }
            i9++;
        }
        this.profileActivity = baseFragment;
        this.actionBar = baseFragment.getActionBar();
        int[] iArr3 = this.mediaColumnsCount;
        iArr3[0] = SharedConfig.mediaColumnsCount;
        iArr3[1] = SharedConfig.storiesColumnsCount;
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.mediaDidLoad);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagesDeleted);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.didReceiveNewMessages);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByServer);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagePlayingDidReset);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.messagePlayingDidStart);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.storiesListUpdated);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.storiesUpdated);
        this.profileActivity.getNotificationCenter().addObserver(this, NotificationCenter.channelRecommendationsLoaded);
        for (int i10 = 0; i10 < 10; i10++) {
            if (i2 == 4) {
                SharedAudioCell sharedAudioCell = new SharedAudioCell(context) { // from class: org.telegram.ui.Components.SharedMediaLayout.2
                    @Override // org.telegram.ui.Cells.SharedAudioCell
                    public boolean needPlayMessage(MessageObject messageObject) {
                        if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                            boolean playMessage = MediaController.getInstance().playMessage(messageObject);
                            MediaController.getInstance().setVoiceMessagesPlaylist(playMessage ? SharedMediaLayout.this.sharedMediaData[4].messages : null, false);
                            return playMessage;
                        } else if (messageObject.isMusic()) {
                            return MediaController.getInstance().setPlaylist(SharedMediaLayout.this.sharedMediaData[4].messages, messageObject, SharedMediaLayout.this.mergeDialogId);
                        } else {
                            return false;
                        }
                    }
                };
                sharedAudioCell.initStreamingIcons();
                this.audioCellCache.add(sharedAudioCell);
            }
        }
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.searching = false;
        this.searchWas = false;
        Drawable drawable = context.getResources().getDrawable(R.drawable.photos_header_shadow);
        this.pinnedHeaderShadowDrawable = drawable;
        drawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundGrayShadow), PorterDuff.Mode.MULTIPLY));
        ScrollSlidingTextTabStripInner scrollSlidingTextTabStripInner = this.scrollSlidingTextTabStrip;
        int currentTabId = scrollSlidingTextTabStripInner != null ? scrollSlidingTextTabStripInner.getCurrentTabId() : i2;
        this.scrollSlidingTextTabStrip = createScrollingTextTabStrip(context);
        for (int i11 = 1; i11 >= 0; i11--) {
            this.selectedFiles[i11].clear();
        }
        this.cantDeleteMessagesCount = 0;
        this.actionModeViews.clear();
        ActionBarMenu createMenu = this.actionBar.createMenu();
        createMenu.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.3
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19) {
                if (SharedMediaLayout.this.searchItem == null) {
                    return;
                }
                SharedMediaLayout.this.searchItem.setTranslationX(((View) SharedMediaLayout.this.searchItem.getParent()).getMeasuredWidth() - SharedMediaLayout.this.searchItem.getRight());
            }
        });
        ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.4
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
            public void onLayout(int i12, int i13, int i14, int i15) {
                SharedMediaLayout.this.searchItem.setTranslationX(((View) SharedMediaLayout.this.searchItem.getParent()).getMeasuredWidth() - SharedMediaLayout.this.searchItem.getRight());
            }
        });
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setTranslationY(AndroidUtilities.dp(10.0f));
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        int i12 = R.string.Search;
        actionBarMenuItem.setSearchFieldHint(LocaleController.getString("Search", i12));
        this.searchItem.setContentDescription(LocaleController.getString("Search", i12));
        this.searchItem.setVisibility(isStoriesView() ? 8 : 4);
        ImageView imageView = new ImageView(context);
        this.photoVideoOptionsItem = imageView;
        imageView.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
        this.photoVideoOptionsItem.setTranslationY(AndroidUtilities.dp(10.0f));
        this.photoVideoOptionsItem.setVisibility(4);
        Drawable mutate = ContextCompat.getDrawable(context, R.drawable.ic_ab_other).mutate();
        int i13 = Theme.key_actionBarActionModeDefaultIcon;
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(i13), PorterDuff.Mode.MULTIPLY));
        this.photoVideoOptionsItem.setImageDrawable(mutate);
        this.photoVideoOptionsItem.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (!isArchivedOnlyStoriesView()) {
            this.actionBar.addView(this.photoVideoOptionsItem, LayoutHelper.createFrame(48, 56, 85));
        }
        this.photoVideoOptionsItem.setOnClickListener(new 5(context, resourcesProvider));
        EditTextBoldCursor searchField = this.searchItem.getSearchField();
        int i14 = Theme.key_windowBackgroundWhiteBlackText;
        searchField.setTextColor(getThemedColor(i14));
        searchField.setHintTextColor(getThemedColor(Theme.key_player_time));
        searchField.setCursorColor(getThemedColor(i14));
        this.searchItemState = 0;
        BaseFragment baseFragment2 = this.profileActivity;
        BlurredLinearLayout blurredLinearLayout = new BlurredLinearLayout(context, (baseFragment2 == null || !(baseFragment2.getFragmentView() instanceof SizeNotifierFrameLayout)) ? null : (SizeNotifierFrameLayout) this.profileActivity.getFragmentView());
        this.actionModeLayout = blurredLinearLayout;
        blurredLinearLayout.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
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
        this.backDrawable.setColor(getThemedColor(i13));
        ImageView imageView4 = this.closeButton;
        int i15 = Theme.key_actionBarActionModeDefaultSelector;
        imageView4.setBackground(Theme.createSelectorDrawable(getThemedColor(i15), 1));
        this.closeButton.setContentDescription(LocaleController.getString("Close", R.string.Close));
        this.actionModeLayout.addView(this.closeButton, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
        this.actionModeViews.add(this.closeButton);
        this.closeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SharedMediaLayout.this.lambda$new$2(view);
            }
        });
        NumberTextView numberTextView = new NumberTextView(context);
        this.selectedMessagesCountTextView = numberTextView;
        numberTextView.setTextSize(18);
        this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.selectedMessagesCountTextView.setTextColor(getThemedColor(i13));
        this.actionModeLayout.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 18, 0, 0, 0));
        this.actionModeViews.add(this.selectedMessagesCountTextView);
        if (DialogObject.isEncryptedDialog(this.dialog_id)) {
            i4 = i15;
            i5 = i13;
            i6 = currentTabId;
        } else {
            i4 = i15;
            i6 = currentTabId;
            ActionBarMenuItem actionBarMenuItem2 = new ActionBarMenuItem(context, (ActionBarMenu) null, getThemedColor(i15), getThemedColor(i13), false);
            this.gotoItem = actionBarMenuItem2;
            actionBarMenuItem2.setIcon(R.drawable.msg_message);
            this.gotoItem.setContentDescription(LocaleController.getString("AccDescrGoToMessage", R.string.AccDescrGoToMessage));
            this.gotoItem.setDuplicateParentStateEnabled(false);
            this.actionModeLayout.addView(this.gotoItem, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
            this.actionModeViews.add(this.gotoItem);
            this.gotoItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SharedMediaLayout.this.lambda$new$3(view);
                }
            });
            i5 = i13;
            ActionBarMenuItem actionBarMenuItem3 = new ActionBarMenuItem(context, (ActionBarMenu) null, getThemedColor(i4), getThemedColor(i5), false);
            this.forwardItem = actionBarMenuItem3;
            actionBarMenuItem3.setIcon(R.drawable.msg_forward);
            this.forwardItem.setContentDescription(LocaleController.getString("Forward", R.string.Forward));
            this.forwardItem.setDuplicateParentStateEnabled(false);
            this.actionModeLayout.addView(this.forwardItem, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
            this.actionModeViews.add(this.forwardItem);
            this.forwardItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SharedMediaLayout.this.lambda$new$4(view);
                }
            });
            updateForwardItem();
        }
        ActionBarMenuItem actionBarMenuItem4 = new ActionBarMenuItem(context, (ActionBarMenu) null, getThemedColor(i4), getThemedColor(i5), false);
        this.deleteItem = actionBarMenuItem4;
        actionBarMenuItem4.setIcon(R.drawable.msg_delete);
        this.deleteItem.setContentDescription(LocaleController.getString("Delete", R.string.Delete));
        this.deleteItem.setDuplicateParentStateEnabled(false);
        this.actionModeLayout.addView(this.deleteItem, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
        this.actionModeViews.add(this.deleteItem);
        this.deleteItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SharedMediaLayout.this.lambda$new$5(view);
            }
        });
        this.photoVideoAdapter = new SharedPhotoVideoAdapter(context) { // from class: org.telegram.ui.Components.SharedMediaLayout.6
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                MediaPage mediaPage = SharedMediaLayout.this.getMediaPage(0);
                if (mediaPage == null || mediaPage.animationSupportingListView.getVisibility() != 0) {
                    return;
                }
                SharedMediaLayout.this.animationSupportingPhotoVideoAdapter.notifyDataSetChanged();
            }
        };
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
        this.channelRecommendationsAdapter = new ChannelRecommendationsAdapter(context);
        ChatUsersAdapter chatUsersAdapter = new ChatUsersAdapter(context);
        this.chatUsersAdapter = chatUsersAdapter;
        if (this.topicId == 0) {
            chatUsersAdapter.sortedUsers = arrayList;
            this.chatUsersAdapter.chatInfo = i6 != 7 ? null : tLRPC$ChatFull3;
        }
        this.storiesAdapter = new StoriesAdapter(context, false) { // from class: org.telegram.ui.Components.SharedMediaLayout.7
            @Override // org.telegram.ui.Components.SharedMediaLayout.StoriesAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                MediaPage mediaPage = SharedMediaLayout.this.getMediaPage(8);
                if (mediaPage != null && mediaPage.animationSupportingListView.getVisibility() == 0) {
                    SharedMediaLayout.this.animationSupportingStoriesAdapter.notifyDataSetChanged();
                }
                if (mediaPage != null) {
                    StickerEmptyView stickerEmptyView = mediaPage.emptyView;
                    StoriesController.StoriesList storiesList = this.storiesList;
                    stickerEmptyView.showProgress(storiesList != null && (storiesList.isLoading() || (SharedMediaLayout.this.hasInternet() && this.storiesList.getCount() > 0)));
                }
            }
        };
        this.animationSupportingStoriesAdapter = new StoriesAdapter(context, false);
        this.archivedStoriesAdapter = new StoriesAdapter(context, true) { // from class: org.telegram.ui.Components.SharedMediaLayout.8
            @Override // org.telegram.ui.Components.SharedMediaLayout.StoriesAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                MediaPage mediaPage = SharedMediaLayout.this.getMediaPage(9);
                if (mediaPage != null && mediaPage.animationSupportingListView.getVisibility() == 0) {
                    SharedMediaLayout.this.animationSupportingArchivedStoriesAdapter.notifyDataSetChanged();
                }
                if (mediaPage != null) {
                    StickerEmptyView stickerEmptyView = mediaPage.emptyView;
                    StoriesController.StoriesList storiesList = this.storiesList;
                    stickerEmptyView.showProgress(storiesList != null && (storiesList.isLoading() || (SharedMediaLayout.this.hasInternet() && this.storiesList.getCount() > 0)));
                }
            }
        };
        this.animationSupportingArchivedStoriesAdapter = new StoriesAdapter(context, true);
        this.linksAdapter = new SharedLinksAdapter(context);
        setWillNotDraw(false);
        int i16 = 0;
        int i17 = -1;
        int i18 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i16 >= mediaPageArr.length) {
                break;
            }
            if (i16 == 0 && mediaPageArr[i16] != null && mediaPageArr[i16].layoutManager != null) {
                i17 = this.mediaPages[i16].layoutManager.findFirstVisibleItemPosition();
                if (i17 == this.mediaPages[i16].layoutManager.getItemCount() - 1 || (holder = (RecyclerListView.Holder) this.mediaPages[i16].listView.findViewHolderForAdapterPosition(i17)) == null) {
                    i17 = -1;
                } else {
                    i18 = holder.itemView.getTop();
                }
            }
            final MediaPage mediaPage = new MediaPage(context) { // from class: org.telegram.ui.Components.SharedMediaLayout.9
                @Override // android.view.View
                public void setTranslationX(float f) {
                    super.setTranslationX(f);
                    if (SharedMediaLayout.this.tabsAnimationInProgress) {
                        int i19 = 0;
                        if (SharedMediaLayout.this.mediaPages[0] == this) {
                            float abs = Math.abs(SharedMediaLayout.this.mediaPages[0].getTranslationX()) / SharedMediaLayout.this.mediaPages[0].getMeasuredWidth();
                            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                            sharedMediaLayout.scrollSlidingTextTabStrip.selectTabWithId(sharedMediaLayout.mediaPages[1].selectedType, abs);
                            if (SharedMediaLayout.this.canShowSearchItem()) {
                                if (SharedMediaLayout.this.searchItemState == 2) {
                                    SharedMediaLayout.this.searchItem.setAlpha(1.0f - abs);
                                } else if (SharedMediaLayout.this.searchItemState == 1) {
                                    SharedMediaLayout.this.searchItem.setAlpha(abs);
                                }
                                float photoVideoOptionsAlpha = SharedMediaLayout.this.getPhotoVideoOptionsAlpha(abs);
                                SharedMediaLayout.this.photoVideoOptionsItem.setAlpha(photoVideoOptionsAlpha);
                                SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
                                sharedMediaLayout2.photoVideoOptionsItem.setVisibility((photoVideoOptionsAlpha == 0.0f || !sharedMediaLayout2.canShowSearchItem() || SharedMediaLayout.this.isArchivedOnlyStoriesView()) ? 4 : 4);
                            } else {
                                SharedMediaLayout.this.searchItem.setAlpha(0.0f);
                            }
                        }
                    }
                    SharedMediaLayout.this.invalidateBlur();
                }
            };
            addView(mediaPage, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, isStoriesView() ? 0.0f : 48.0f, 0.0f, 0.0f));
            if (i16 == 1) {
                mediaPage.setTranslationX(AndroidUtilities.displaySize.x);
            }
            MediaPage[] mediaPageArr2 = this.mediaPages;
            mediaPageArr2[i16] = mediaPage;
            final ExtendedGridLayoutManager extendedGridLayoutManager = mediaPageArr2[i16].layoutManager = new ExtendedGridLayoutManager(context, 100) { // from class: org.telegram.ui.Components.SharedMediaLayout.10
                private Size size = new Size();

                @Override // org.telegram.ui.Components.ExtendedGridLayoutManager, androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.LinearLayoutManager
                public void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr4) {
                    super.calculateExtraLayoutSpace(state, iArr4);
                    if (mediaPage.selectedType != 0 && mediaPage.selectedType != 8 && mediaPage.selectedType != 9) {
                        if (mediaPage.selectedType == 1) {
                            iArr4[1] = Math.max(iArr4[1], AndroidUtilities.dp(56.0f) * 2);
                            return;
                        }
                        return;
                    }
                    iArr4[1] = Math.max(iArr4[1], SharedPhotoVideoCell.getItemSize(1) * 2);
                }

                @Override // org.telegram.ui.Components.ExtendedGridLayoutManager
                protected Size getSizeForItem(int i19) {
                    int i20;
                    int i21;
                    TLRPC$Document document = (mediaPage.listView.getAdapter() != SharedMediaLayout.this.gifAdapter || SharedMediaLayout.this.sharedMediaData[5].messages.isEmpty()) ? null : SharedMediaLayout.this.sharedMediaData[5].messages.get(i19).getDocument();
                    Size size = this.size;
                    size.height = 100.0f;
                    size.width = 100.0f;
                    if (document != null) {
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                        if (closestPhotoSizeWithSize != null && (i20 = closestPhotoSizeWithSize.w) != 0 && (i21 = closestPhotoSizeWithSize.h) != 0) {
                            Size size2 = this.size;
                            size2.width = i20;
                            size2.height = i21;
                        }
                        ArrayList<TLRPC$DocumentAttribute> arrayList2 = document.attributes;
                        for (int i22 = 0; i22 < arrayList2.size(); i22++) {
                            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = arrayList2.get(i22);
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

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.ExtendedGridLayoutManager
                public int getFlowItemCount() {
                    if (mediaPage.listView.getAdapter() != SharedMediaLayout.this.gifAdapter) {
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
            };
            extendedGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.SharedMediaLayout.11
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i19) {
                    int i20 = SharedMediaLayout.this.mediaColumnsCount[(mediaPage.selectedType == 8 || mediaPage.selectedType == 9) ? (char) 1 : (char) 0];
                    if (mediaPage.listView.getAdapter() == SharedMediaLayout.this.photoVideoAdapter) {
                        if (SharedMediaLayout.this.photoVideoAdapter.getItemViewType(i19) == 2) {
                            return i20;
                        }
                        return 1;
                    } else if (mediaPage.listView.getAdapter() == SharedMediaLayout.this.storiesAdapter) {
                        if (SharedMediaLayout.this.storiesAdapter.getItemViewType(i19) == 2) {
                            return i20;
                        }
                        return 1;
                    } else if (mediaPage.listView.getAdapter() == SharedMediaLayout.this.archivedStoriesAdapter) {
                        if (SharedMediaLayout.this.storiesAdapter.getItemViewType(i19) == 2) {
                            return i20;
                        }
                        return 1;
                    } else if (mediaPage.listView.getAdapter() != SharedMediaLayout.this.gifAdapter) {
                        return mediaPage.layoutManager.getSpanCount();
                    } else {
                        return (mediaPage.listView.getAdapter() == SharedMediaLayout.this.gifAdapter && SharedMediaLayout.this.sharedMediaData[5].messages.isEmpty()) ? mediaPage.layoutManager.getSpanCount() : mediaPage.layoutManager.getSpanSizeForItem(i19);
                    }
                }
            });
            this.mediaPages[i16].listView = new InternalListView(context) { // from class: org.telegram.ui.Components.SharedMediaLayout.12
                private StaticLayout archivedHintLayout;
                private float archivedHintLayoutLeft;
                private float archivedHintLayoutWidth;
                private TextPaint archivedHintPaint;
                float lastY;
                UserListPoller poller;
                float startY;
                final HashSet<SharedPhotoVideoCell2> excludeDrawViews = new HashSet<>();
                final ArrayList<SharedPhotoVideoCell2> drawingViews = new ArrayList<>();
                final ArrayList<SharedPhotoVideoCell2> drawingViews2 = new ArrayList<>();
                final ArrayList<SharedPhotoVideoCell2> drawingViews3 = new ArrayList<>();

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                public void onLayout(boolean z, int i19, int i20, int i21, int i22) {
                    super.onLayout(z, i19, i20, i21, i22);
                    SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                    MediaPage mediaPage2 = mediaPage;
                    sharedMediaLayout.checkLoadMoreScroll(mediaPage2, mediaPage2.listView, extendedGridLayoutManager);
                    if (mediaPage.selectedType == 0) {
                        PhotoViewer.getInstance().checkCurrentImageVisibility();
                    }
                }

                @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    if (SharedMediaLayout.this.profileActivity != null && SharedMediaLayout.this.profileActivity.isInPreviewMode()) {
                        this.lastY = motionEvent.getY();
                        if (motionEvent.getAction() == 1) {
                            SharedMediaLayout.this.profileActivity.finishPreviewFragment();
                        } else if (motionEvent.getAction() == 2) {
                            float f = this.startY - this.lastY;
                            SharedMediaLayout.this.profileActivity.movePreviewFragment(f);
                            if (f < 0.0f) {
                                this.startY = this.lastY;
                            }
                        }
                        return true;
                    }
                    return super.dispatchTouchEvent(motionEvent);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Removed duplicated region for block: B:178:0x05b0  */
                /* JADX WARN: Removed duplicated region for block: B:233:0x0989  */
                /* JADX WARN: Removed duplicated region for block: B:242:0x09bc  */
                /* JADX WARN: Removed duplicated region for block: B:243:0x09c2  */
                /* JADX WARN: Removed duplicated region for block: B:250:0x09d7  */
                /* JADX WARN: Removed duplicated region for block: B:256:0x09f4  */
                /* JADX WARN: Removed duplicated region for block: B:276:0x05ff A[SYNTHETIC] */
                @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void dispatchDraw(Canvas canvas) {
                    SharedPhotoVideoAdapter sharedPhotoVideoAdapter;
                    SharedPhotoVideoAdapter sharedPhotoVideoAdapter2;
                    char c;
                    int i19;
                    float f;
                    MediaPage mediaPage2;
                    int i20;
                    int i21;
                    int i22;
                    int i23;
                    int i24;
                    int i25;
                    float f2;
                    int i26;
                    boolean z;
                    int i27;
                    int i28;
                    View childAt;
                    View childAt2;
                    boolean z2 = true;
                    if (getAdapter() == SharedMediaLayout.this.archivedStoriesAdapter && getChildCount() > 0 && (childAt = getChildAt(0)) != null && getChildAdapterPosition(childAt) == 0) {
                        int top = childAt.getTop();
                        if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation && SharedMediaLayout.this.changeColumnsTab == 9 && mediaPage.animationSupportingListView.getChildCount() > 0 && (childAt2 = mediaPage.animationSupportingListView.getChildAt(0)) != null && mediaPage.animationSupportingListView.getChildAdapterPosition(childAt2) == 0) {
                            top = AndroidUtilities.lerp(top, childAt2.getTop(), SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                        }
                        if (this.archivedHintPaint == null) {
                            TextPaint textPaint = new TextPaint(1);
                            this.archivedHintPaint = textPaint;
                            textPaint.setTextSize(AndroidUtilities.dp(14.0f));
                            this.archivedHintPaint.setColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
                        }
                        int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(60.0f);
                        StaticLayout staticLayout = this.archivedHintLayout;
                        if (staticLayout == null || staticLayout.getWidth() != measuredWidth) {
                            this.archivedHintLayout = new StaticLayout(LocaleController.getString(SharedMediaLayout.this.isArchivedOnlyStoriesView() ? R.string.ProfileStoriesArchiveChannelHint : R.string.ProfileStoriesArchiveHint), this.archivedHintPaint, measuredWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                            this.archivedHintLayoutWidth = 0.0f;
                            this.archivedHintLayoutLeft = measuredWidth;
                            for (int i29 = 0; i29 < this.archivedHintLayout.getLineCount(); i29++) {
                                this.archivedHintLayoutWidth = Math.max(this.archivedHintLayoutWidth, this.archivedHintLayout.getLineWidth(i29));
                                this.archivedHintLayoutLeft = Math.min(this.archivedHintLayoutLeft, this.archivedHintLayout.getLineLeft(i29));
                            }
                        }
                        canvas.save();
                        canvas.translate(((getWidth() - this.archivedHintLayoutWidth) / 2.0f) - this.archivedHintLayoutLeft, top - ((AndroidUtilities.dp(64.0f) + this.archivedHintLayout.getHeight()) / 2.0f));
                        this.archivedHintLayout.draw(canvas);
                        canvas.restore();
                    }
                    if (SharedMediaLayout.this.changeColumnsTab == 8) {
                        sharedPhotoVideoAdapter = SharedMediaLayout.this.storiesAdapter;
                        sharedPhotoVideoAdapter2 = SharedMediaLayout.this.animationSupportingStoriesAdapter;
                    } else if (SharedMediaLayout.this.changeColumnsTab == 9) {
                        sharedPhotoVideoAdapter = SharedMediaLayout.this.archivedStoriesAdapter;
                        sharedPhotoVideoAdapter2 = SharedMediaLayout.this.animationSupportingArchivedStoriesAdapter;
                    } else {
                        sharedPhotoVideoAdapter = SharedMediaLayout.this.photoVideoAdapter;
                        sharedPhotoVideoAdapter2 = SharedMediaLayout.this.animationSupportingPhotoVideoAdapter;
                        c = 0;
                        if (this != mediaPage.listView && getAdapter() == sharedPhotoVideoAdapter) {
                            float measuredHeight = getMeasuredHeight();
                            if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                                int i30 = -1;
                                int i31 = -1;
                                for (int i32 = 0; i32 < mediaPage.listView.getChildCount(); i32++) {
                                    int childAdapterPosition = mediaPage.listView.getChildAdapterPosition(mediaPage.listView.getChildAt(i32));
                                    if (childAdapterPosition >= 0 && (childAdapterPosition > i31 || i31 == -1)) {
                                        i31 = childAdapterPosition;
                                    }
                                    if (childAdapterPosition >= 0 && (childAdapterPosition < i30 || i30 == -1)) {
                                        i30 = childAdapterPosition;
                                    }
                                }
                                int i33 = -1;
                                int i34 = -1;
                                for (int i35 = 0; i35 < mediaPage.animationSupportingListView.getChildCount(); i35++) {
                                    int childAdapterPosition2 = mediaPage.animationSupportingListView.getChildAdapterPosition(mediaPage.animationSupportingListView.getChildAt(i35));
                                    if (childAdapterPosition2 >= 0 && (childAdapterPosition2 > i34 || i34 == -1)) {
                                        i34 = childAdapterPosition2;
                                    }
                                    if (childAdapterPosition2 >= 0 && (childAdapterPosition2 < i33 || i33 == -1)) {
                                        i33 = childAdapterPosition2;
                                    }
                                }
                                if (i30 < 0 || i33 < 0 || SharedMediaLayout.this.pinchCenterPosition < 0) {
                                    i27 = 0;
                                    i28 = 0;
                                } else {
                                    int ceil = (int) Math.ceil(sharedPhotoVideoAdapter.getItemCount() / SharedMediaLayout.this.mediaColumnsCount[c]);
                                    int ceil2 = (int) Math.ceil(sharedPhotoVideoAdapter.getItemCount() / SharedMediaLayout.this.animateToColumnsCount);
                                    SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                                    i28 = ((sharedMediaLayout.pinchCenterPosition / sharedMediaLayout.animateToColumnsCount) - (i33 / SharedMediaLayout.this.animateToColumnsCount)) - (((SharedMediaLayout.this.pinchCenterPosition - sharedPhotoVideoAdapter.getTopOffset()) / SharedMediaLayout.this.mediaColumnsCount[c]) - (i30 / SharedMediaLayout.this.mediaColumnsCount[c]));
                                    if (((i30 / SharedMediaLayout.this.mediaColumnsCount[c]) - i28 < 0 && SharedMediaLayout.this.animateToColumnsCount < SharedMediaLayout.this.mediaColumnsCount[c]) || ((i33 / SharedMediaLayout.this.animateToColumnsCount) + i28 < 0 && SharedMediaLayout.this.animateToColumnsCount > SharedMediaLayout.this.mediaColumnsCount[c])) {
                                        i28 = 0;
                                    }
                                    if (((i34 / SharedMediaLayout.this.mediaColumnsCount[c]) + i28 >= ceil && SharedMediaLayout.this.animateToColumnsCount > SharedMediaLayout.this.mediaColumnsCount[c]) || ((i31 / SharedMediaLayout.this.animateToColumnsCount) - i28 >= ceil2 && SharedMediaLayout.this.animateToColumnsCount < SharedMediaLayout.this.mediaColumnsCount[c])) {
                                        i28 = 0;
                                    }
                                    SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
                                    i27 = (int) ((SharedMediaLayout.this.animateToColumnsCount - SharedMediaLayout.this.mediaColumnsCount[c]) * ((sharedMediaLayout2.pinchCenterPosition % sharedMediaLayout2.mediaColumnsCount[c]) / (SharedMediaLayout.this.mediaColumnsCount[c] - 1)));
                                }
                                SharedMediaLayout.this.animationSupportingSortedCells.clear();
                                this.excludeDrawViews.clear();
                                this.drawingViews.clear();
                                this.drawingViews2.clear();
                                this.drawingViews3.clear();
                                SharedMediaLayout.this.animationSupportingSortedCellsOffset = 0;
                                for (int i36 = 0; i36 < mediaPage.animationSupportingListView.getChildCount(); i36++) {
                                    View childAt3 = mediaPage.animationSupportingListView.getChildAt(i36);
                                    if (childAt3.getTop() <= getMeasuredHeight() && childAt3.getBottom() >= 0) {
                                        if (childAt3 instanceof SharedPhotoVideoCell2) {
                                            SharedMediaLayout.this.animationSupportingSortedCells.add((SharedPhotoVideoCell2) childAt3);
                                        } else if (childAt3 instanceof TextView) {
                                            SharedMediaLayout.access$5208(SharedMediaLayout.this);
                                        }
                                    }
                                }
                                this.drawingViews.addAll(SharedMediaLayout.this.animationSupportingSortedCells);
                                RecyclerListView.FastScroll fastScroll = getFastScroll();
                                if (fastScroll != null && fastScroll.getTag() != null) {
                                    float scrollProgress = sharedPhotoVideoAdapter.getScrollProgress(mediaPage.listView);
                                    float scrollProgress2 = sharedPhotoVideoAdapter2.getScrollProgress(mediaPage.animationSupportingListView);
                                    float f3 = sharedPhotoVideoAdapter.fastScrollIsVisible(mediaPage.listView) ? 1.0f : 0.0f;
                                    float f4 = sharedPhotoVideoAdapter2.fastScrollIsVisible(mediaPage.animationSupportingListView) ? 1.0f : 0.0f;
                                    fastScroll.setProgress((scrollProgress * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (scrollProgress2 * SharedMediaLayout.this.photoVideoChangeColumnsProgress));
                                    fastScroll.setVisibilityAlpha((f3 * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (f4 * SharedMediaLayout.this.photoVideoChangeColumnsProgress));
                                }
                                i21 = i28;
                                i22 = i33;
                                i23 = i30;
                                i20 = i27;
                            } else {
                                i20 = 0;
                                i21 = 0;
                                i22 = 0;
                                i23 = 0;
                            }
                            float f5 = measuredHeight;
                            int i37 = 0;
                            while (i37 < getChildCount()) {
                                View childAt4 = getChildAt(i37);
                                if (childAt4.getTop() > getMeasuredHeight() || childAt4.getBottom() < 0) {
                                    i26 = i23;
                                    if (childAt4 instanceof SharedPhotoVideoCell2) {
                                        SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) getChildAt(i37);
                                        sharedPhotoVideoCell2.setCrossfadeView(null, 0.0f, 0);
                                        sharedPhotoVideoCell2.setTranslationX(0.0f);
                                        sharedPhotoVideoCell2.setTranslationY(0.0f);
                                        sharedPhotoVideoCell2.setImageScale(1.0f, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                                    }
                                } else if (childAt4 instanceof SharedPhotoVideoCell2) {
                                    SharedPhotoVideoCell2 sharedPhotoVideoCell22 = (SharedPhotoVideoCell2) getChildAt(i37);
                                    if (sharedPhotoVideoCell22.getMessageId() == mediaPage.highlightMessageId && sharedPhotoVideoCell22.imageReceiver.hasBitmapImage()) {
                                        MediaPage mediaPage3 = mediaPage;
                                        if (!mediaPage3.highlightAnimation) {
                                            mediaPage3.highlightProgress = 0.0f;
                                            mediaPage3.highlightAnimation = z2;
                                        }
                                        float f6 = mediaPage3.highlightProgress;
                                        sharedPhotoVideoCell22.setHighlightProgress(f6 < 0.3f ? f6 / 0.3f : f6 > 0.7f ? (1.0f - f6) / 0.3f : 1.0f);
                                    } else {
                                        sharedPhotoVideoCell22.setHighlightProgress(0.0f);
                                    }
                                    MessageObject messageObject = sharedPhotoVideoCell22.getMessageObject();
                                    sharedPhotoVideoCell22.setImageAlpha((messageObject == null || SharedMediaLayout.this.messageAlphaEnter.get(messageObject.getId(), null) == null) ? 1.0f : SharedMediaLayout.this.messageAlphaEnter.get(messageObject.getId(), Float.valueOf(1.0f)).floatValue(), SharedMediaLayout.this.photoVideoChangeColumnsAnimation ^ z2);
                                    if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                                        int viewAdapterPosition = (((GridLayoutManager.LayoutParams) sharedPhotoVideoCell22.getLayoutParams()).getViewAdapterPosition() % SharedMediaLayout.this.mediaColumnsCount[c]) + i20;
                                        int viewAdapterPosition2 = ((((((GridLayoutManager.LayoutParams) sharedPhotoVideoCell22.getLayoutParams()).getViewAdapterPosition() - i23) / SharedMediaLayout.this.mediaColumnsCount[c]) + i21) * SharedMediaLayout.this.animateToColumnsCount) + viewAdapterPosition + SharedMediaLayout.this.animationSupportingSortedCellsOffset;
                                        if (viewAdapterPosition >= 0 && viewAdapterPosition < SharedMediaLayout.this.animateToColumnsCount && viewAdapterPosition2 >= 0 && viewAdapterPosition2 < SharedMediaLayout.this.animationSupportingSortedCells.size()) {
                                            float measuredWidth2 = ((1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress) * 1.0f) + (((((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2)).getMeasuredWidth() - AndroidUtilities.dpf2(2.0f)) / (sharedPhotoVideoCell22.getMeasuredWidth() - AndroidUtilities.dpf2(2.0f))) * SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                            float left = ((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2)).getLeft();
                                            float top2 = ((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2)).getTop();
                                            i26 = i23;
                                            sharedPhotoVideoCell22.setPivotX(0.0f);
                                            sharedPhotoVideoCell22.setPivotY(0.0f);
                                            sharedPhotoVideoCell22.setImageScale(measuredWidth2, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                                            sharedPhotoVideoCell22.setTranslationX((left - sharedPhotoVideoCell22.getLeft()) * SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                            sharedPhotoVideoCell22.setTranslationY((top2 - sharedPhotoVideoCell22.getTop()) * SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                            sharedPhotoVideoCell22.setCrossfadeView((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2), SharedMediaLayout.this.photoVideoChangeColumnsProgress, SharedMediaLayout.this.animateToColumnsCount);
                                            this.excludeDrawViews.add((SharedPhotoVideoCell2) SharedMediaLayout.this.animationSupportingSortedCells.get(viewAdapterPosition2));
                                            this.drawingViews3.add(sharedPhotoVideoCell22);
                                            canvas.save();
                                            canvas.translate(sharedPhotoVideoCell22.getX(), sharedPhotoVideoCell22.getY());
                                            sharedPhotoVideoCell22.draw(canvas);
                                            canvas.restore();
                                            if (sharedPhotoVideoCell22.getY() < f5) {
                                                f5 = sharedPhotoVideoCell22.getY();
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
                                    i26 = i23;
                                    z = false;
                                    if (!z) {
                                    }
                                } else {
                                    i26 = i23;
                                }
                                i37++;
                                i23 = i26;
                                z2 = true;
                            }
                            int i38 = i23;
                            float f7 = 255.0f;
                            if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation && !this.drawingViews.isEmpty()) {
                                float f8 = ((SharedMediaLayout.this.animateToColumnsCount / SharedMediaLayout.this.mediaColumnsCount[c]) * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + SharedMediaLayout.this.photoVideoChangeColumnsProgress;
                                float measuredWidth3 = ((((getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount[c]) - AndroidUtilities.dpf2(2.0f)) / ((getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount) - AndroidUtilities.dpf2(2.0f))) * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + SharedMediaLayout.this.photoVideoChangeColumnsProgress;
                                float measuredWidth4 = getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount[c];
                                float measuredWidth5 = getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount;
                                double ceil3 = Math.ceil(getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount);
                                double dpf2 = AndroidUtilities.dpf2(2.0f);
                                Double.isNaN(dpf2);
                                double d = ceil3 - dpf2;
                                double d2 = measuredWidth3;
                                Double.isNaN(d2);
                                double dpf22 = AndroidUtilities.dpf2(2.0f);
                                Double.isNaN(dpf22);
                                float f9 = (float) ((d * d2) + dpf22);
                                if (SharedMediaLayout.this.changeColumnsTab == 8 || SharedMediaLayout.this.changeColumnsTab == 9) {
                                    f9 *= 1.25f;
                                }
                                float f10 = f9;
                                int i39 = 0;
                                while (i39 < this.drawingViews.size()) {
                                    SharedPhotoVideoCell2 sharedPhotoVideoCell23 = this.drawingViews.get(i39);
                                    if (this.excludeDrawViews.contains(sharedPhotoVideoCell23)) {
                                        i25 = i39;
                                        f2 = measuredWidth3;
                                    } else {
                                        sharedPhotoVideoCell23.setCrossfadeView(null, 0.0f, 0);
                                        int viewAdapterPosition3 = ((GridLayoutManager.LayoutParams) sharedPhotoVideoCell23.getLayoutParams()).getViewAdapterPosition() % SharedMediaLayout.this.animateToColumnsCount;
                                        int i40 = viewAdapterPosition3 - i20;
                                        canvas.save();
                                        canvas.translate((i40 * measuredWidth4 * (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (viewAdapterPosition3 * measuredWidth5 * SharedMediaLayout.this.photoVideoChangeColumnsProgress), ((((((GridLayoutManager.LayoutParams) sharedPhotoVideoCell23.getLayoutParams()).getViewAdapterPosition() - i22) / SharedMediaLayout.this.animateToColumnsCount) - i21) * f10) + f5);
                                        sharedPhotoVideoCell23.setImageScale(measuredWidth3, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                                        if (i40 < SharedMediaLayout.this.mediaColumnsCount[c]) {
                                            i25 = i39;
                                            f2 = measuredWidth3;
                                            canvas.saveLayerAlpha(0.0f, 0.0f, sharedPhotoVideoCell23.getMeasuredWidth() * f8, sharedPhotoVideoCell23.getMeasuredHeight() * f8, (int) (SharedMediaLayout.this.photoVideoChangeColumnsProgress * f7), 31);
                                            sharedPhotoVideoCell23.draw(canvas);
                                            canvas.restore();
                                        } else {
                                            i25 = i39;
                                            f2 = measuredWidth3;
                                            sharedPhotoVideoCell23.draw(canvas);
                                        }
                                        canvas.restore();
                                    }
                                    i39 = i25 + 1;
                                    measuredWidth3 = f2;
                                    f7 = 255.0f;
                                }
                            }
                            float f11 = 1.0f;
                            super.dispatchDraw(canvas);
                            if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                                float f12 = ((SharedMediaLayout.this.mediaColumnsCount[c] / SharedMediaLayout.this.animateToColumnsCount) * SharedMediaLayout.this.photoVideoChangeColumnsProgress) + (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                float measuredWidth6 = ((((getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount) - AndroidUtilities.dpf2(2.0f)) / ((getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount[c]) - AndroidUtilities.dpf2(2.0f))) * SharedMediaLayout.this.photoVideoChangeColumnsProgress) + (1.0f - SharedMediaLayout.this.photoVideoChangeColumnsProgress);
                                double ceil4 = Math.ceil(getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount[c]);
                                double dpf23 = AndroidUtilities.dpf2(2.0f);
                                Double.isNaN(dpf23);
                                double d3 = ceil4 - dpf23;
                                double d4 = measuredWidth6;
                                Double.isNaN(d4);
                                double dpf24 = AndroidUtilities.dpf2(2.0f);
                                Double.isNaN(dpf24);
                                float f13 = (float) ((d3 * d4) + dpf24);
                                if (SharedMediaLayout.this.changeColumnsTab == 8 || SharedMediaLayout.this.changeColumnsTab == 9) {
                                    f13 *= 1.25f;
                                }
                                float f14 = f13;
                                float measuredWidth7 = getMeasuredWidth() / SharedMediaLayout.this.mediaColumnsCount[c];
                                float measuredWidth8 = getMeasuredWidth() / SharedMediaLayout.this.animateToColumnsCount;
                                int i41 = 0;
                                while (i41 < this.drawingViews2.size()) {
                                    SharedPhotoVideoCell2 sharedPhotoVideoCell24 = this.drawingViews2.get(i41);
                                    int viewAdapterPosition4 = ((GridLayoutManager.LayoutParams) sharedPhotoVideoCell24.getLayoutParams()).getViewAdapterPosition() % SharedMediaLayout.this.mediaColumnsCount[c];
                                    int i42 = viewAdapterPosition4 + i20;
                                    canvas.save();
                                    sharedPhotoVideoCell24.setImageScale(measuredWidth6, !SharedMediaLayout.this.photoVideoChangeColumnsAnimation);
                                    canvas.translate((viewAdapterPosition4 * measuredWidth7 * (f11 - SharedMediaLayout.this.photoVideoChangeColumnsProgress)) + (i42 * measuredWidth8 * SharedMediaLayout.this.photoVideoChangeColumnsProgress), ((((((GridLayoutManager.LayoutParams) sharedPhotoVideoCell24.getLayoutParams()).getViewAdapterPosition() - i38) / SharedMediaLayout.this.mediaColumnsCount[c]) + i21) * f14) + f5);
                                    if (i42 < SharedMediaLayout.this.animateToColumnsCount) {
                                        i24 = i41;
                                        canvas.saveLayerAlpha(0.0f, 0.0f, sharedPhotoVideoCell24.getMeasuredWidth() * f12, sharedPhotoVideoCell24.getMeasuredHeight() * f12, (int) ((f11 - SharedMediaLayout.this.photoVideoChangeColumnsProgress) * 255.0f), 31);
                                        sharedPhotoVideoCell24.draw(canvas);
                                        canvas.restore();
                                    } else {
                                        i24 = i41;
                                        sharedPhotoVideoCell24.draw(canvas);
                                    }
                                    canvas.restore();
                                    i41 = i24 + 1;
                                    f11 = 1.0f;
                                }
                                if (!this.drawingViews3.isEmpty()) {
                                    canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) (SharedMediaLayout.this.photoVideoChangeColumnsProgress * 255.0f), 31);
                                    for (int i43 = 0; i43 < this.drawingViews3.size(); i43++) {
                                        this.drawingViews3.get(i43).drawCrossafadeImage(canvas);
                                    }
                                    canvas.restore();
                                }
                            }
                        } else {
                            for (i19 = 0; i19 < getChildCount(); i19++) {
                                View childAt5 = getChildAt(i19);
                                int messageId = SharedMediaLayout.this.getMessageId(childAt5);
                                if (messageId != 0 && SharedMediaLayout.this.messageAlphaEnter.get(messageId, null) != null) {
                                    f = SharedMediaLayout.this.messageAlphaEnter.get(messageId, Float.valueOf(1.0f)).floatValue();
                                    if (!(childAt5 instanceof SharedDocumentCell)) {
                                        ((SharedDocumentCell) childAt5).setEnterAnimationAlpha(f);
                                    } else if (childAt5 instanceof SharedAudioCell) {
                                        ((SharedAudioCell) childAt5).setEnterAnimationAlpha(f);
                                    }
                                }
                                f = 1.0f;
                                if (!(childAt5 instanceof SharedDocumentCell)) {
                                }
                            }
                            super.dispatchDraw(canvas);
                        }
                        mediaPage2 = mediaPage;
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
                        if (this.poller == null) {
                            this.poller = UserListPoller.getInstance(SharedMediaLayout.this.profileActivity.getCurrentAccount());
                        }
                        this.poller.checkList(this);
                    }
                    c = 1;
                    if (this != mediaPage.listView) {
                    }
                    while (i19 < getChildCount()) {
                    }
                    super.dispatchDraw(canvas);
                    mediaPage2 = mediaPage;
                    if (mediaPage2.highlightAnimation) {
                    }
                    if (this.poller == null) {
                    }
                    this.poller.checkList(this);
                }

                @Override // org.telegram.ui.Components.BlurredRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                public boolean drawChild(Canvas canvas, View view, long j2) {
                    RecyclerView.Adapter adapter = SharedMediaLayout.this.changeColumnsTab == 8 ? SharedMediaLayout.this.storiesAdapter : SharedMediaLayout.this.changeColumnsTab == 9 ? SharedMediaLayout.this.archivedStoriesAdapter : SharedMediaLayout.this.photoVideoAdapter;
                    if (mediaPage.listView == this && getAdapter() == adapter && SharedMediaLayout.this.photoVideoChangeColumnsAnimation && (view instanceof SharedPhotoVideoCell2)) {
                        return true;
                    }
                    return super.drawChild(canvas, view, j2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView
                public Integer getSelectorColor(int i19) {
                    if (getAdapter() == SharedMediaLayout.this.channelRecommendationsAdapter && SharedMediaLayout.this.channelRecommendationsAdapter.more > 0 && i19 == SharedMediaLayout.this.channelRecommendationsAdapter.getItemCount() - 1) {
                        return 0;
                    }
                    return super.getSelectorColor(i19);
                }
            };
            this.mediaPages[i16].listView.setFastScrollEnabled(1);
            this.mediaPages[i16].listView.setScrollingTouchSlop(1);
            this.mediaPages[i16].listView.setPinnedSectionOffsetY(-AndroidUtilities.dp(2.0f));
            this.mediaPages[i16].listView.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
            this.mediaPages[i16].listView.setItemAnimator(null);
            this.mediaPages[i16].listView.setClipToPadding(false);
            this.mediaPages[i16].listView.setSectionsType(2);
            this.mediaPages[i16].listView.setLayoutManager(extendedGridLayoutManager);
            MediaPage[] mediaPageArr3 = this.mediaPages;
            mediaPageArr3[i16].addView(mediaPageArr3[i16].listView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i16].animationSupportingListView = new InternalListView(context);
            this.mediaPages[i16].animationSupportingListView.setLayoutManager(this.mediaPages[i16].animationSupportingLayoutManager = new GridLayoutManager(context, 3) { // from class: org.telegram.ui.Components.SharedMediaLayout.13
                @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }

                @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public int scrollVerticallyBy(int i19, RecyclerView.Recycler recycler, RecyclerView.State state) {
                    if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                        i19 = 0;
                    }
                    return super.scrollVerticallyBy(i19, recycler, state);
                }
            });
            MediaPage[] mediaPageArr4 = this.mediaPages;
            mediaPageArr4[i16].addView(mediaPageArr4[i16].animationSupportingListView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i16].animationSupportingListView.setVisibility(8);
            this.mediaPages[i16].animationSupportingListView.addItemDecoration(new RecyclerView.ItemDecoration(this) { // from class: org.telegram.ui.Components.SharedMediaLayout.14
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    if (view instanceof SharedPhotoVideoCell2) {
                        SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) view;
                        int childAdapterPosition = mediaPage.animationSupportingListView.getChildAdapterPosition(sharedPhotoVideoCell2);
                        int spanCount = mediaPage.animationSupportingLayoutManager.getSpanCount();
                        int i19 = childAdapterPosition % spanCount;
                        sharedPhotoVideoCell2.isFirst = i19 == 0;
                        sharedPhotoVideoCell2.isLast = i19 == spanCount - 1;
                        rect.left = 0;
                        rect.top = 0;
                        rect.bottom = 0;
                        rect.right = 0;
                        return;
                    }
                    rect.left = 0;
                    rect.top = 0;
                    rect.bottom = 0;
                    rect.right = 0;
                }
            });
            this.mediaPages[i16].listView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.SharedMediaLayout.15
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    if (mediaPage.listView.getAdapter() == SharedMediaLayout.this.gifAdapter) {
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                        rect.left = 0;
                        rect.bottom = 0;
                        if (!mediaPage.layoutManager.isFirstRow(childAdapterPosition)) {
                            rect.top = AndroidUtilities.dp(2.0f);
                        } else {
                            rect.top = 0;
                        }
                        rect.right = mediaPage.layoutManager.isLastInRow(childAdapterPosition) ? 0 : AndroidUtilities.dp(2.0f);
                    } else if (view instanceof SharedPhotoVideoCell2) {
                        SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) view;
                        int childAdapterPosition2 = mediaPage.listView.getChildAdapterPosition(sharedPhotoVideoCell2);
                        int spanCount = mediaPage.layoutManager.getSpanCount();
                        int i19 = childAdapterPosition2 % spanCount;
                        sharedPhotoVideoCell2.isFirst = i19 == 0;
                        sharedPhotoVideoCell2.isLast = i19 == spanCount - 1;
                        rect.left = 0;
                        rect.top = 0;
                        rect.bottom = 0;
                        rect.right = 0;
                    } else {
                        rect.left = 0;
                        rect.top = 0;
                        rect.bottom = 0;
                        rect.right = 0;
                    }
                }
            });
            this.mediaPages[i16].listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda17
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ boolean hasDoubleTap(View view, int i19) {
                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i19);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ void onDoubleTap(View view, int i19, float f, float f2) {
                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i19, f, f2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public final void onItemClick(View view, int i19, float f, float f2) {
                    SharedMediaLayout.this.lambda$new$6(mediaPage, view, i19, f, f2);
                }
            });
            this.mediaPages[i16].listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.16
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i19) {
                    SharedMediaLayout.this.scrolling = i19 != 0;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i19, int i20) {
                    SharedMediaLayout.this.checkLoadMoreScroll(mediaPage, (RecyclerListView) recyclerView, extendedGridLayoutManager);
                    if (i20 != 0 && ((SharedMediaLayout.this.mediaPages[0].selectedType == 0 || SharedMediaLayout.this.mediaPages[0].selectedType == 5) && !SharedMediaLayout.this.sharedMediaData[0].messages.isEmpty())) {
                        SharedMediaLayout.this.showFloatingDateView();
                    }
                    if (i20 != 0 && (mediaPage.selectedType == 0 || mediaPage.selectedType == 8 || mediaPage.selectedType == 9)) {
                        SharedMediaLayout.showFastScrollHint(mediaPage, SharedMediaLayout.this.sharedMediaData, true);
                    }
                    mediaPage.listView.checkSection(true);
                    MediaPage mediaPage2 = mediaPage;
                    if (mediaPage2.fastScrollHintView != null) {
                        mediaPage2.invalidate();
                    }
                    SharedMediaLayout.this.invalidateBlur();
                }
            });
            this.mediaPages[i16].listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: org.telegram.ui.Components.SharedMediaLayout.17
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public boolean onItemClick(View view, int i19, float f, float f2) {
                    int i20;
                    int i21 = 0;
                    if (SharedMediaLayout.this.photoVideoChangeColumnsAnimation) {
                        return false;
                    }
                    if (SharedMediaLayout.this.isActionModeShowed) {
                        mediaPage.listView.clickItem(view, i19);
                        return true;
                    } else if (mediaPage.selectedType != 7 || !(view instanceof UserCell)) {
                        if (mediaPage.selectedType == 1 && (view instanceof SharedDocumentCell)) {
                            return SharedMediaLayout.this.onItemLongClick(((SharedDocumentCell) view).getMessage(), view, 0);
                        }
                        if (mediaPage.selectedType == 3 && (view instanceof SharedLinkCell)) {
                            return SharedMediaLayout.this.onItemLongClick(((SharedLinkCell) view).getMessage(), view, 0);
                        }
                        if ((mediaPage.selectedType == 2 || mediaPage.selectedType == 4) && (view instanceof SharedAudioCell)) {
                            return SharedMediaLayout.this.onItemLongClick(((SharedAudioCell) view).getMessage(), view, 0);
                        }
                        if (mediaPage.selectedType == 5 && (view instanceof ContextLinkCell)) {
                            return SharedMediaLayout.this.onItemLongClick((MessageObject) ((ContextLinkCell) view).getParentObject(), view, 0);
                        }
                        if ((mediaPage.selectedType != 0 && mediaPage.selectedType != 9 && (mediaPage.selectedType != 8 || !SharedMediaLayout.this.isStoriesView())) || !(view instanceof SharedPhotoVideoCell2)) {
                            if (mediaPage.selectedType == 10) {
                                SharedMediaLayout.this.channelRecommendationsAdapter.openPreview(i19);
                                return true;
                            }
                        } else {
                            MessageObject messageObject = ((SharedPhotoVideoCell2) view).getMessageObject();
                            if (messageObject != null) {
                                return SharedMediaLayout.this.onItemLongClick(messageObject, view, mediaPage.selectedType);
                            }
                        }
                        return false;
                    } else {
                        if (SharedMediaLayout.this.chatUsersAdapter.sortedUsers.isEmpty()) {
                            i20 = i19;
                        } else if (i19 >= SharedMediaLayout.this.chatUsersAdapter.sortedUsers.size()) {
                            return false;
                        } else {
                            i20 = ((Integer) SharedMediaLayout.this.chatUsersAdapter.sortedUsers.get(i19)).intValue();
                        }
                        if (i20 < 0 || i20 >= SharedMediaLayout.this.chatUsersAdapter.chatInfo.participants.participants.size()) {
                            return false;
                        }
                        TLRPC$ChatParticipant tLRPC$ChatParticipant = SharedMediaLayout.this.chatUsersAdapter.chatInfo.participants.participants.get(i20);
                        RecyclerListView recyclerListView = (RecyclerListView) view.getParent();
                        while (true) {
                            if (i21 >= recyclerListView.getChildCount()) {
                                break;
                            }
                            View childAt = recyclerListView.getChildAt(i21);
                            if (recyclerListView.getChildAdapterPosition(childAt) == i19) {
                                view = childAt;
                                break;
                            }
                            i21++;
                        }
                        return SharedMediaLayout.this.onMemberClick(tLRPC$ChatParticipant, true, view);
                    }
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public void onMove(float f, float f2) {
                    if (SharedMediaLayout.this.profileActivity != null) {
                        android.graphics.Point point = AndroidUtilities.displaySize;
                        if (point.x > point.y) {
                            SharedMediaLayout.this.profileActivity.movePreviewFragment(f2);
                        }
                    }
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public void onLongClickRelease() {
                    if (SharedMediaLayout.this.profileActivity != null) {
                        android.graphics.Point point = AndroidUtilities.displaySize;
                        if (point.x > point.y) {
                            SharedMediaLayout.this.profileActivity.finishPreviewFragment();
                        }
                    }
                }
            });
            if (i16 == 0 && i17 != -1) {
                extendedGridLayoutManager.scrollToPositionWithOffset(i17, i18);
            }
            final InternalListView internalListView = this.mediaPages[i16].listView;
            this.mediaPages[i16].animatingImageView = new ClippingImageView(this, context) { // from class: org.telegram.ui.Components.SharedMediaLayout.18
                @Override // android.view.View
                public void invalidate() {
                    super.invalidate();
                    internalListView.invalidate();
                }
            };
            this.mediaPages[i16].animatingImageView.setVisibility(8);
            this.mediaPages[i16].listView.addOverlayView(this.mediaPages[i16].animatingImageView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i16].progressView = new FlickerLoadingView(context) { // from class: org.telegram.ui.Components.SharedMediaLayout.19
                @Override // org.telegram.ui.Components.FlickerLoadingView
                public int getColumnsCount() {
                    return SharedMediaLayout.this.mediaColumnsCount[(mediaPage.selectedType == 8 || mediaPage.selectedType == 9) ? (char) 1 : (char) 0];
                }

                @Override // org.telegram.ui.Components.FlickerLoadingView
                public int getViewType() {
                    setIsSingleCell(false);
                    if (mediaPage.selectedType == 0 || mediaPage.selectedType == 5) {
                        return 2;
                    }
                    if (mediaPage.selectedType == 1) {
                        return 3;
                    }
                    if (mediaPage.selectedType == 2 || mediaPage.selectedType == 4) {
                        return 6;
                    }
                    if (mediaPage.selectedType == 3) {
                        return 5;
                    }
                    if (mediaPage.selectedType == 7) {
                        return 6;
                    }
                    if (mediaPage.selectedType != 6) {
                        return (mediaPage.selectedType == 8 || mediaPage.selectedType == 9) ? 27 : 1;
                    }
                    if (SharedMediaLayout.this.scrollSlidingTextTabStrip.getTabsCount() == 1) {
                        setIsSingleCell(true);
                    }
                    return 1;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.FlickerLoadingView, android.view.View
                public void onDraw(Canvas canvas) {
                    SharedMediaLayout.this.backgroundPaint.setColor(SharedMediaLayout.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), SharedMediaLayout.this.backgroundPaint);
                    super.onDraw(canvas);
                }
            };
            this.mediaPages[i16].progressView.showDate(false);
            if (i16 != 0) {
                this.mediaPages[i16].setVisibility(8);
            }
            MediaPage[] mediaPageArr5 = this.mediaPages;
            mediaPageArr5[i16].emptyView = new StickerEmptyView(context, mediaPageArr5[i16].progressView, 1);
            this.mediaPages[i16].emptyView.setVisibility(8);
            this.mediaPages[i16].emptyView.setAnimateLayoutChange(true);
            MediaPage[] mediaPageArr6 = this.mediaPages;
            mediaPageArr6[i16].addView(mediaPageArr6[i16].emptyView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i16].emptyView.setOnTouchListener(SharedMediaLayout$$ExternalSyntheticLambda5.INSTANCE);
            this.mediaPages[i16].emptyView.showProgress(true, false);
            this.mediaPages[i16].emptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
            this.mediaPages[i16].emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", R.string.SearchEmptyViewFilteredSubtitle2));
            this.mediaPages[i16].emptyView.addView(this.mediaPages[i16].progressView, LayoutHelper.createFrame(-1, -1.0f));
            this.mediaPages[i16].listView.setEmptyView(this.mediaPages[i16].emptyView);
            this.mediaPages[i16].listView.setAnimateEmptyView(true, 0);
            MediaPage[] mediaPageArr7 = this.mediaPages;
            mediaPageArr7[i16].scrollHelper = new RecyclerAnimationScrollHelper(mediaPageArr7[i16].listView, this.mediaPages[i16].layoutManager);
            i16++;
        }
        ChatActionCell chatActionCell = new ChatActionCell(context);
        this.floatingDateView = chatActionCell;
        chatActionCell.setCustomDate((int) (System.currentTimeMillis() / 1000), false, false);
        this.floatingDateView.setAlpha(0.0f);
        this.floatingDateView.setOverrideColor(Theme.key_chat_mediaTimeBackground, Theme.key_chat_mediaTimeText);
        this.floatingDateView.setTranslationY(-AndroidUtilities.dp(48.0f));
        addView(this.floatingDateView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 52.0f, 0.0f, 0.0f));
        if (!isStoriesView()) {
            FragmentContextView fragmentContextView = new FragmentContextView(context, baseFragment, this, false, resourcesProvider);
            this.fragmentContextView = fragmentContextView;
            addView(fragmentContextView, LayoutHelper.createFrame(-1, 38.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
            this.fragmentContextView.setDelegate(new FragmentContextView.FragmentContextViewDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda16
                @Override // org.telegram.ui.Components.FragmentContextView.FragmentContextViewDelegate
                public final void onAnimation(boolean z, boolean z2) {
                    SharedMediaLayout.this.lambda$new$8(z, z2);
                }
            });
            addView(this.scrollSlidingTextTabStrip, LayoutHelper.createFrame(-1, 48, 51));
            addView(this.actionModeLayout, LayoutHelper.createFrame(-1, 48, 51));
        }
        View view = new View(context);
        this.shadowLine = view;
        view.setBackgroundColor(getThemedColor(Theme.key_divider));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, 1);
        layoutParams.topMargin = isStoriesView() ? 0 : AndroidUtilities.dp(48.0f) - 1;
        addView(this.shadowLine, layoutParams);
        updateTabs(false);
        switchToCurrentSelectedMode(false);
        if (this.hasMedia[0] >= 0) {
            loadFastScrollData(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 5 implements View.OnClickListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        5(Context context, Theme.ResourcesProvider resourcesProvider) {
            this.val$context = context;
            this.val$resourcesProvider = resourcesProvider;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TLRPC$Chat chat;
            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
            final DividerCell dividerCell = new DividerCell(this.val$context);
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this, this.val$context, this.val$resourcesProvider) { // from class: org.telegram.ui.Components.SharedMediaLayout.5.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.ActionBarPopupWindowLayout, android.widget.FrameLayout, android.view.View
                public void onMeasure(int i, int i2) {
                    if (dividerCell.getParent() != null) {
                        dividerCell.setVisibility(8);
                        super.onMeasure(i, i2);
                        dividerCell.getLayoutParams().width = getMeasuredWidth() - AndroidUtilities.dp(16.0f);
                        dividerCell.setVisibility(0);
                        super.onMeasure(i, i2);
                        return;
                    }
                    super.onMeasure(i, i2);
                }
            };
            final int closestTab = SharedMediaLayout.this.getClosestTab();
            boolean z = true;
            char c = (closestTab == 8 || closestTab == 9) ? (char) 1 : (char) 0;
            SharedMediaLayout.this.mediaZoomInItem = new ActionBarMenuSubItem(this.val$context, true, false, this.val$resourcesProvider);
            SharedMediaLayout.this.mediaZoomOutItem = new ActionBarMenuSubItem(this.val$context, false, false, this.val$resourcesProvider);
            SharedMediaLayout.this.mediaZoomInItem.setTextAndIcon(LocaleController.getString("MediaZoomIn", R.string.MediaZoomIn), R.drawable.msg_zoomin);
            SharedMediaLayout.this.mediaZoomInItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$5$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    SharedMediaLayout.5.this.lambda$onClick$0(view2);
                }
            });
            actionBarPopupWindowLayout.addView(SharedMediaLayout.this.mediaZoomInItem);
            SharedMediaLayout.this.mediaZoomOutItem.setTextAndIcon(LocaleController.getString("MediaZoomOut", R.string.MediaZoomOut), R.drawable.msg_zoomout);
            SharedMediaLayout.this.mediaZoomOutItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$5$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    SharedMediaLayout.5.this.lambda$onClick$1(view2);
                }
            });
            actionBarPopupWindowLayout.addView(SharedMediaLayout.this.mediaZoomOutItem);
            if (c == 0 || !SharedMediaLayout.this.allowStoriesSingleColumn) {
                if (SharedMediaLayout.this.mediaColumnsCount[c] == 2) {
                    SharedMediaLayout.this.mediaZoomInItem.setEnabled(false);
                    SharedMediaLayout.this.mediaZoomInItem.setAlpha(0.5f);
                } else if (SharedMediaLayout.this.mediaColumnsCount[c] == 9) {
                    SharedMediaLayout.this.mediaZoomOutItem.setEnabled(false);
                    SharedMediaLayout.this.mediaZoomOutItem.setAlpha(0.5f);
                }
            } else {
                SharedMediaLayout.this.mediaZoomInItem.setEnabled(false);
                SharedMediaLayout.this.mediaZoomInItem.setAlpha(0.5f);
                SharedMediaLayout.this.mediaZoomOutItem.setEnabled(false);
                SharedMediaLayout.this.mediaZoomOutItem.setAlpha(0.5f);
            }
            boolean z2 = (c == 0 && (!SharedMediaLayout.this.sharedMediaData[0].hasPhotos || !SharedMediaLayout.this.sharedMediaData[0].hasVideos) && SharedMediaLayout.this.sharedMediaData[0].endReached[0] && SharedMediaLayout.this.sharedMediaData[0].endReached[1] && SharedMediaLayout.this.sharedMediaData[0].startReached) ? false : true;
            if (!DialogObject.isEncryptedDialog(SharedMediaLayout.this.dialog_id)) {
                ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(this.val$context, false, false, this.val$resourcesProvider);
                actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("Calendar", R.string.Calendar), R.drawable.msg_calendar2);
                actionBarPopupWindowLayout.addView(actionBarMenuSubItem);
                actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.5.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        SharedMediaLayout.this.showMediaCalendar(closestTab, false);
                        ActionBarPopupWindow actionBarPopupWindow = SharedMediaLayout.this.optionsWindow;
                        if (actionBarPopupWindow != null) {
                            actionBarPopupWindow.dismiss();
                        }
                    }
                });
                if (SharedMediaLayout.this.info != null && !SharedMediaLayout.this.isStoriesView() && (chat = MessagesController.getInstance(SharedMediaLayout.this.profileActivity.getCurrentAccount()).getChat(Long.valueOf(SharedMediaLayout.this.info.id))) != null && (tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.edit_stories) {
                    ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(this.val$context, false, true, this.val$resourcesProvider);
                    actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString(R.string.OpenChannelArchiveStories), R.drawable.msg_archive);
                    actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$5$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            SharedMediaLayout.5.this.lambda$onClick$2(view2);
                        }
                    });
                    actionBarPopupWindowLayout.addView(actionBarMenuSubItem2);
                }
                if (z2) {
                    actionBarPopupWindowLayout.addView(dividerCell);
                    final ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(this.val$context, true, false, false, this.val$resourcesProvider);
                    final ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(this.val$context, true, false, true, this.val$resourcesProvider);
                    actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("MediaShowPhotos", R.string.MediaShowPhotos), 0);
                    actionBarPopupWindowLayout.addView(actionBarMenuSubItem3);
                    actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString("MediaShowVideos", R.string.MediaShowVideos), 0);
                    actionBarPopupWindowLayout.addView(actionBarMenuSubItem4);
                    if (c != 0) {
                        final StoriesAdapter storiesAdapter = closestTab == 8 ? SharedMediaLayout.this.storiesAdapter : SharedMediaLayout.this.archivedStoriesAdapter;
                        StoriesController.StoriesList storiesList = storiesAdapter.storiesList;
                        if (storiesList != null) {
                            actionBarMenuSubItem3.setChecked(storiesList.showPhotos());
                            actionBarMenuSubItem4.setChecked(storiesAdapter.storiesList.showVideos());
                        }
                        actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$5$$ExternalSyntheticLambda3
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                SharedMediaLayout.5.this.lambda$onClick$3(actionBarMenuSubItem4, actionBarMenuSubItem3, storiesAdapter, view2);
                            }
                        });
                        actionBarMenuSubItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$5$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                SharedMediaLayout.5.this.lambda$onClick$4(actionBarMenuSubItem3, actionBarMenuSubItem4, storiesAdapter, view2);
                            }
                        });
                    } else {
                        actionBarMenuSubItem3.setChecked(SharedMediaLayout.this.sharedMediaData[0].filterType == 0 || SharedMediaLayout.this.sharedMediaData[0].filterType == 1);
                        actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.5.3
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                if (SharedMediaLayout.this.changeTypeAnimation) {
                                    return;
                                }
                                if (!actionBarMenuSubItem4.getCheckView().isChecked() && actionBarMenuSubItem3.getCheckView().isChecked()) {
                                    ActionBarMenuSubItem actionBarMenuSubItem5 = actionBarMenuSubItem3;
                                    SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                                    AndroidUtilities.shakeViewSpring(actionBarMenuSubItem5, sharedMediaLayout.shiftDp = -sharedMediaLayout.shiftDp);
                                    return;
                                }
                                ActionBarMenuSubItem actionBarMenuSubItem6 = actionBarMenuSubItem3;
                                actionBarMenuSubItem6.setChecked(!actionBarMenuSubItem6.getCheckView().isChecked());
                                if (!actionBarMenuSubItem3.getCheckView().isChecked() || !actionBarMenuSubItem4.getCheckView().isChecked()) {
                                    SharedMediaLayout.this.sharedMediaData[0].filterType = 2;
                                } else {
                                    SharedMediaLayout.this.sharedMediaData[0].filterType = 0;
                                }
                                SharedMediaLayout.this.changeMediaFilterType();
                            }
                        });
                        if (SharedMediaLayout.this.sharedMediaData[0].filterType != 0 && SharedMediaLayout.this.sharedMediaData[0].filterType != 2) {
                            z = false;
                        }
                        actionBarMenuSubItem4.setChecked(z);
                        actionBarMenuSubItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.5.4
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                if (SharedMediaLayout.this.changeTypeAnimation) {
                                    return;
                                }
                                if (!actionBarMenuSubItem3.getCheckView().isChecked() && actionBarMenuSubItem4.getCheckView().isChecked()) {
                                    ActionBarMenuSubItem actionBarMenuSubItem5 = actionBarMenuSubItem4;
                                    SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                                    AndroidUtilities.shakeViewSpring(actionBarMenuSubItem5, sharedMediaLayout.shiftDp = -sharedMediaLayout.shiftDp);
                                    return;
                                }
                                ActionBarMenuSubItem actionBarMenuSubItem6 = actionBarMenuSubItem4;
                                actionBarMenuSubItem6.setChecked(!actionBarMenuSubItem6.getCheckView().isChecked());
                                if (!actionBarMenuSubItem3.getCheckView().isChecked() || !actionBarMenuSubItem4.getCheckView().isChecked()) {
                                    SharedMediaLayout.this.sharedMediaData[0].filterType = 1;
                                } else {
                                    SharedMediaLayout.this.sharedMediaData[0].filterType = 0;
                                }
                                SharedMediaLayout.this.changeMediaFilterType();
                            }
                        });
                    }
                }
            }
            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
            sharedMediaLayout.optionsWindow = AlertsCreator.showPopupMenu(actionBarPopupWindowLayout, sharedMediaLayout.photoVideoOptionsItem, 0, -AndroidUtilities.dp(56.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$0(View view) {
            SharedMediaLayout.this.zoomIn();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$1(View view) {
            SharedMediaLayout.this.zoomOut();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$2(View view) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 2);
            bundle.putLong("dialog_id", -SharedMediaLayout.this.info.id);
            MediaActivity mediaActivity = new MediaActivity(bundle, null);
            mediaActivity.setChatInfo(SharedMediaLayout.this.info);
            SharedMediaLayout.this.profileActivity.presentFragment(mediaActivity);
            ActionBarPopupWindow actionBarPopupWindow = SharedMediaLayout.this.optionsWindow;
            if (actionBarPopupWindow != null) {
                actionBarPopupWindow.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$3(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2, StoriesAdapter storiesAdapter, View view) {
            if (SharedMediaLayout.this.changeTypeAnimation) {
                return;
            }
            if (!actionBarMenuSubItem.getCheckView().isChecked() && actionBarMenuSubItem2.getCheckView().isChecked()) {
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                AndroidUtilities.shakeViewSpring(view, sharedMediaLayout.shiftDp = -sharedMediaLayout.shiftDp);
                return;
            }
            actionBarMenuSubItem2.getCheckView().setChecked(!actionBarMenuSubItem2.getCheckView().isChecked(), true);
            StoriesController.StoriesList storiesList = storiesAdapter.storiesList;
            if (storiesList == null) {
                return;
            }
            storiesList.updateFilters(actionBarMenuSubItem2.getCheckView().isChecked(), actionBarMenuSubItem.getCheckView().isChecked());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$4(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2, StoriesAdapter storiesAdapter, View view) {
            if (SharedMediaLayout.this.changeTypeAnimation) {
                return;
            }
            if (!actionBarMenuSubItem.getCheckView().isChecked() && actionBarMenuSubItem2.getCheckView().isChecked()) {
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                AndroidUtilities.shakeViewSpring(view, sharedMediaLayout.shiftDp = -sharedMediaLayout.shiftDp);
                return;
            }
            actionBarMenuSubItem2.getCheckView().setChecked(!actionBarMenuSubItem2.getCheckView().isChecked(), true);
            StoriesController.StoriesList storiesList = storiesAdapter.storiesList;
            if (storiesList == null) {
                return;
            }
            storiesList.updateFilters(actionBarMenuSubItem.getCheckView().isChecked(), actionBarMenuSubItem2.getCheckView().isChecked());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        closeActionMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        onActionBarItemClick(view, R.styleable.AppCompatTheme_textAppearanceLargePopupMenu);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(View view) {
        onActionBarItemClick(view, 100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(View view) {
        onActionBarItemClick(view, 101);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(MediaPage mediaPage, View view, int i, float f, float f2) {
        long j;
        if (mediaPage.selectedType != 7) {
            if (mediaPage.selectedType != 6 || !(view instanceof ProfileSearchCell)) {
                if (mediaPage.selectedType != 1 || !(view instanceof SharedDocumentCell)) {
                    if (mediaPage.selectedType != 3 || !(view instanceof SharedLinkCell)) {
                        if ((mediaPage.selectedType == 2 || mediaPage.selectedType == 4) && (view instanceof SharedAudioCell)) {
                            onItemClick(i, view, ((SharedAudioCell) view).getMessage(), 0, mediaPage.selectedType);
                            return;
                        } else if (mediaPage.selectedType != 5 || !(view instanceof ContextLinkCell)) {
                            if (mediaPage.selectedType != 0 || !(view instanceof SharedPhotoVideoCell2)) {
                                if ((mediaPage.selectedType != 8 && mediaPage.selectedType != 9) || !(view instanceof SharedPhotoVideoCell2)) {
                                    if (mediaPage.selectedType == 10) {
                                        if (((view instanceof ProfileSearchCell) || f2 < AndroidUtilities.dp(60.0f)) && i >= 0 && i < this.channelRecommendationsAdapter.chats.size()) {
                                            Bundle bundle = new Bundle();
                                            bundle.putLong("chat_id", ((TLRPC$Chat) this.channelRecommendationsAdapter.chats.get(i)).id);
                                            this.profileActivity.presentFragment(new ChatActivity(bundle));
                                            return;
                                        }
                                        return;
                                    }
                                    return;
                                }
                                MessageObject messageObject = ((SharedPhotoVideoCell2) view).getMessageObject();
                                if (messageObject != null) {
                                    onItemClick(i, view, messageObject, 0, mediaPage.selectedType);
                                    return;
                                }
                                return;
                            }
                            SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) view;
                            if (sharedPhotoVideoCell2.canRevealSpoiler()) {
                                sharedPhotoVideoCell2.startRevealMedia(f, f2);
                                return;
                            }
                            MessageObject messageObject2 = sharedPhotoVideoCell2.getMessageObject();
                            if (messageObject2 != null) {
                                onItemClick(i, view, messageObject2, 0, mediaPage.selectedType);
                                return;
                            }
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
            Bundle bundle2 = new Bundle();
            bundle2.putLong("chat_id", chat.id);
            if (this.profileActivity.getMessagesController().checkCanOpenChat(bundle2, this.profileActivity)) {
                if (chat.forum) {
                    BaseFragment baseFragment = this.profileActivity;
                    baseFragment.presentFragment(TopicsFragment.getTopicsOrChat(baseFragment, bundle2));
                    return;
                }
                this.profileActivity.presentFragment(new ChatActivity(bundle2));
            }
        } else if (!(view instanceof UserCell)) {
            RecyclerView.Adapter adapter = mediaPage.listView.getAdapter();
            GroupUsersSearchAdapter groupUsersSearchAdapter = this.groupUsersSearchAdapter;
            if (adapter == groupUsersSearchAdapter) {
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
                Bundle bundle3 = new Bundle();
                bundle3.putLong("user_id", j);
                this.profileActivity.presentFragment(new ProfileActivity(bundle3));
            }
        } else {
            if (!this.chatUsersAdapter.sortedUsers.isEmpty()) {
                i = ((Integer) this.chatUsersAdapter.sortedUsers.get(i)).intValue();
            }
            TLRPC$ChatParticipant tLRPC$ChatParticipant = this.chatUsersAdapter.chatInfo.participants.participants.get(i);
            if (i < 0 || i >= this.chatUsersAdapter.chatInfo.participants.participants.size()) {
                return;
            }
            onMemberClick(tLRPC$ChatParticipant, false, view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(boolean z, boolean z2) {
        if (z) {
            return;
        }
        requestLayout();
    }

    public void setStoriesFilter(boolean z, boolean z2) {
        StoriesController.StoriesList storiesList;
        StoriesController.StoriesList storiesList2;
        StoriesAdapter storiesAdapter = this.storiesAdapter;
        if (storiesAdapter != null && (storiesList2 = storiesAdapter.storiesList) != null) {
            storiesList2.updateFilters(z, z2);
        }
        StoriesAdapter storiesAdapter2 = this.archivedStoriesAdapter;
        if (storiesAdapter2 == null || (storiesList = storiesAdapter2.storiesList) == null) {
            return;
        }
        storiesList.updateFilters(z, z2);
    }

    public void setForwardRestrictedHint(HintView hintView) {
        this.fwdRestrictedHint = hintView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMessageId(View view) {
        if (view instanceof SharedPhotoVideoCell2) {
            return ((SharedPhotoVideoCell2) view).getMessageId();
        }
        if (view instanceof SharedDocumentCell) {
            return ((SharedDocumentCell) view).getMessage().getId();
        }
        if (view instanceof SharedAudioCell) {
            return ((SharedAudioCell) view).getMessage().getId();
        }
        return 0;
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
            this.forwardItem.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_actionBarActionModeDefaultSelector), 5));
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

    /* JADX INFO: Access modifiers changed from: private */
    public void changeMediaFilterType() {
        final MediaPage mediaPage = getMediaPage(0);
        if (mediaPage != null && mediaPage.getMeasuredHeight() > 0 && mediaPage.getMeasuredWidth() > 0) {
            final Bitmap bitmap = null;
            try {
                bitmap = Bitmap.createBitmap(mediaPage.getMeasuredWidth(), mediaPage.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (bitmap != null) {
                this.changeTypeAnimation = true;
                mediaPage.listView.draw(new Canvas(bitmap));
                final View view = new View(mediaPage.getContext());
                view.setBackground(new BitmapDrawable(bitmap));
                mediaPage.addView(view);
                view.animate().alpha(0.0f).setDuration(200L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.20
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SharedMediaLayout.this.changeTypeAnimation = false;
                        if (view.getParent() != null) {
                            mediaPage.removeView(view);
                            bitmap.recycle();
                        }
                    }
                }).start();
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
        jumpToDate(0, DialogObject.isEncryptedDialog(this.dialog_id) ? Integer.MIN_VALUE : ConnectionsManager.DEFAULT_DATACENTER_ID, 0, true);
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

    /* JADX INFO: Access modifiers changed from: private */
    public MediaPage getMediaPage(int i) {
        int i2 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i2 >= mediaPageArr.length) {
                return null;
            }
            if (mediaPageArr[i2] != null && mediaPageArr[i2].selectedType == i) {
                return this.mediaPages[i2];
            }
            i2++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showMediaCalendar(int i, boolean z) {
        int i2;
        MediaPage mediaPage;
        if (z && getY() != 0.0f && this.viewType == 1) {
            return;
        }
        if (!z || (!(i == 8 || i == 9) || getStoriesCount(i) > 0)) {
            Bundle bundle = new Bundle();
            bundle.putLong("dialog_id", this.dialog_id);
            bundle.putInt("topic_id", this.topicId);
            if (z && (mediaPage = getMediaPage(0)) != null) {
                ArrayList<Period> arrayList = this.sharedMediaData[0].fastScrollPeriods;
                Period period = null;
                int findFirstVisibleItemPosition = mediaPage.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition >= 0) {
                    if (arrayList != null) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= arrayList.size()) {
                                break;
                            } else if (findFirstVisibleItemPosition <= arrayList.get(i3).startOffset) {
                                period = arrayList.get(i3);
                                break;
                            } else {
                                i3++;
                            }
                        }
                        if (period == null) {
                            period = arrayList.get(arrayList.size() - 1);
                        }
                    }
                    if (period != null) {
                        i2 = period.date;
                        if (i != 9) {
                            bundle.putInt("type", 3);
                        } else if (i == 8) {
                            bundle.putInt("type", 2);
                        } else {
                            bundle.putInt("type", 1);
                        }
                        CalendarActivity calendarActivity = new CalendarActivity(bundle, this.sharedMediaData[0].filterType, i2);
                        calendarActivity.setCallback(new CalendarActivity.Callback() { // from class: org.telegram.ui.Components.SharedMediaLayout.21
                            @Override // org.telegram.ui.CalendarActivity.Callback
                            public void onDateSelected(int i4, int i5) {
                                int i6 = -1;
                                for (int i7 = 0; i7 < SharedMediaLayout.this.sharedMediaData[0].messages.size(); i7++) {
                                    if (SharedMediaLayout.this.sharedMediaData[0].messages.get(i7).getId() == i4) {
                                        i6 = i7;
                                    }
                                }
                                MediaPage mediaPage2 = SharedMediaLayout.this.getMediaPage(0);
                                if (i6 < 0 || mediaPage2 == null) {
                                    SharedMediaLayout.this.jumpToDate(0, i4, i5, true);
                                } else {
                                    mediaPage2.layoutManager.scrollToPositionWithOffset(i6, 0);
                                }
                                if (mediaPage2 != null) {
                                    mediaPage2.highlightMessageId = i4;
                                    mediaPage2.highlightAnimation = false;
                                }
                            }
                        });
                        this.profileActivity.presentFragment(calendarActivity);
                    }
                }
            }
            i2 = 0;
            if (i != 9) {
            }
            CalendarActivity calendarActivity2 = new CalendarActivity(bundle, this.sharedMediaData[0].filterType, i2);
            calendarActivity2.setCallback(new CalendarActivity.Callback() { // from class: org.telegram.ui.Components.SharedMediaLayout.21
                @Override // org.telegram.ui.CalendarActivity.Callback
                public void onDateSelected(int i4, int i5) {
                    int i6 = -1;
                    for (int i7 = 0; i7 < SharedMediaLayout.this.sharedMediaData[0].messages.size(); i7++) {
                        if (SharedMediaLayout.this.sharedMediaData[0].messages.get(i7).getId() == i4) {
                            i6 = i7;
                        }
                    }
                    MediaPage mediaPage2 = SharedMediaLayout.this.getMediaPage(0);
                    if (i6 < 0 || mediaPage2 == null) {
                        SharedMediaLayout.this.jumpToDate(0, i4, i5, true);
                    } else {
                        mediaPage2.layoutManager.scrollToPositionWithOffset(i6, 0);
                    }
                    if (mediaPage2 != null) {
                        mediaPage2.highlightMessageId = i4;
                        mediaPage2.highlightAnimation = false;
                    }
                }
            });
            this.profileActivity.presentFragment(calendarActivity2);
        }
    }

    private void startPinchToMediaColumnsCount(boolean z) {
        if (this.photoVideoChangeColumnsAnimation) {
            return;
        }
        final MediaPage mediaPage = null;
        int i = 0;
        int i2 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i2 >= mediaPageArr.length) {
                break;
            } else if (mediaPageArr[i2].selectedType == 0 || this.mediaPages[i2].selectedType == 8 || this.mediaPages[i2].selectedType == 9) {
                break;
            } else {
                i2++;
            }
        }
        if (mediaPage == null) {
            return;
        }
        int i3 = mediaPage.selectedType;
        this.changeColumnsTab = i3;
        int i4 = (i3 == 8 || i3 == 9) ? 1 : 0;
        int nextMediaColumnsCount = getNextMediaColumnsCount(i4, this.mediaColumnsCount[i4], z);
        this.animateToColumnsCount = nextMediaColumnsCount;
        if (nextMediaColumnsCount == this.mediaColumnsCount[i4] || this.allowStoriesSingleColumn) {
            return;
        }
        mediaPage.animationSupportingListView.setVisibility(0);
        int i5 = this.changeColumnsTab;
        if (i5 == 8) {
            mediaPage.animationSupportingListView.setAdapter(this.animationSupportingStoriesAdapter);
        } else if (i5 == 9) {
            mediaPage.animationSupportingListView.setAdapter(this.animationSupportingArchivedStoriesAdapter);
        } else {
            mediaPage.animationSupportingListView.setAdapter(this.animationSupportingPhotoVideoAdapter);
        }
        mediaPage.animationSupportingListView.setPadding(mediaPage.animationSupportingListView.getPaddingLeft(), AndroidUtilities.dp(this.changeColumnsTab == 9 ? 66.0f : 2.0f), mediaPage.animationSupportingListView.getPaddingRight(), mediaPage.animationSupportingListView.getPaddingBottom());
        mediaPage.animationSupportingLayoutManager.setSpanCount(nextMediaColumnsCount);
        mediaPage.animationSupportingListView.invalidateItemDecorations();
        mediaPage.animationSupportingLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.SharedMediaLayout.22
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i6) {
                if (mediaPage.animationSupportingListView.getAdapter() == SharedMediaLayout.this.animationSupportingPhotoVideoAdapter) {
                    if (SharedMediaLayout.this.animationSupportingPhotoVideoAdapter.getItemViewType(i6) == 2) {
                        return mediaPage.animationSupportingLayoutManager.getSpanCount();
                    }
                    return 1;
                } else if (mediaPage.animationSupportingListView.getAdapter() == SharedMediaLayout.this.animationSupportingStoriesAdapter) {
                    if (SharedMediaLayout.this.animationSupportingStoriesAdapter.getItemViewType(i6) == 2) {
                        return mediaPage.animationSupportingLayoutManager.getSpanCount();
                    }
                    return 1;
                } else if (mediaPage.animationSupportingListView.getAdapter() == SharedMediaLayout.this.animationSupportingArchivedStoriesAdapter && SharedMediaLayout.this.animationSupportingArchivedStoriesAdapter.getItemViewType(i6) == 2) {
                    return mediaPage.animationSupportingLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        AndroidUtilities.updateVisibleRows(mediaPage.listView);
        this.photoVideoChangeColumnsAnimation = true;
        if (this.changeColumnsTab == 0) {
            this.sharedMediaData[0].setListFrozen(true);
        }
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
            if (mediaPageArr2[i].selectedType == this.changeColumnsTab) {
                this.mediaPages[i].animationSupportingLayoutManager.scrollToPositionWithOffset(this.pinchCenterPosition, this.pinchCenterOffset - this.mediaPages[i].animationSupportingListView.getPaddingTop());
            }
            i++;
        }
    }

    private void finishPinchToMediaColumnsCount() {
        RecyclerView.Adapter adapter;
        if (!this.photoVideoChangeColumnsAnimation) {
            return;
        }
        final MediaPage mediaPage = null;
        int i = 0;
        int i2 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i2 >= mediaPageArr.length) {
                break;
            } else if (mediaPageArr[i2].selectedType == this.changeColumnsTab) {
                mediaPage = this.mediaPages[i2];
                break;
            } else {
                i2++;
            }
        }
        if (mediaPage == null) {
            return;
        }
        final int i3 = (mediaPage.selectedType == 8 || mediaPage.selectedType == 9) ? 1 : 0;
        float f = this.photoVideoChangeColumnsProgress;
        if (f != 1.0f) {
            if (f == 0.0f) {
                this.photoVideoChangeColumnsAnimation = false;
                if (this.changeColumnsTab == 0) {
                    this.sharedMediaData[0].setListFrozen(false);
                }
                mediaPage.animationSupportingListView.setVisibility(8);
                mediaPage.listView.invalidate();
                return;
            }
            final boolean z = f > 0.2f;
            float[] fArr = new float[2];
            fArr[0] = f;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.23
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SharedMediaLayout.this.photoVideoChangeColumnsProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    mediaPage.listView.invalidate();
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.24
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    View findViewByPosition;
                    RecyclerView.Adapter adapter2;
                    SharedMediaLayout.this.photoVideoChangeColumnsAnimation = false;
                    if (z) {
                        SharedMediaLayout.this.mediaColumnsCount[i3] = SharedMediaLayout.this.animateToColumnsCount;
                        if (i3 == 0) {
                            SharedConfig.setMediaColumnsCount(SharedMediaLayout.this.animateToColumnsCount);
                        } else if (SharedMediaLayout.this.getStoriesCount(mediaPage.selectedType) >= 5) {
                            SharedConfig.setStoriesColumnsCount(SharedMediaLayout.this.animateToColumnsCount);
                        }
                    }
                    for (int i4 = 0; i4 < SharedMediaLayout.this.mediaPages.length; i4++) {
                        if (SharedMediaLayout.this.mediaPages[i4] != null && SharedMediaLayout.this.mediaPages[i4].listView != null) {
                            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                            if (sharedMediaLayout.isTabZoomable(sharedMediaLayout.mediaPages[i4].selectedType) && (adapter2 = SharedMediaLayout.this.mediaPages[i4].listView.getAdapter()) != null) {
                                int itemCount = adapter2.getItemCount();
                                if (i4 == 0) {
                                    SharedMediaLayout.this.sharedMediaData[0].setListFrozen(false);
                                }
                                if (z) {
                                    SharedMediaLayout.this.mediaPages[i4].layoutManager.setSpanCount(SharedMediaLayout.this.mediaColumnsCount[i3]);
                                    SharedMediaLayout.this.mediaPages[i4].listView.invalidateItemDecorations();
                                    if (adapter2.getItemCount() == itemCount) {
                                        AndroidUtilities.updateVisibleRows(SharedMediaLayout.this.mediaPages[i4].listView);
                                    } else {
                                        adapter2.notifyDataSetChanged();
                                    }
                                }
                                SharedMediaLayout.this.mediaPages[i4].animationSupportingListView.setVisibility(8);
                            }
                        }
                    }
                    SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
                    if (sharedMediaLayout2.pinchCenterPosition >= 0) {
                        for (int i5 = 0; i5 < SharedMediaLayout.this.mediaPages.length; i5++) {
                            if (SharedMediaLayout.this.mediaPages[i5].selectedType == SharedMediaLayout.this.changeColumnsTab) {
                                if (z && (findViewByPosition = SharedMediaLayout.this.mediaPages[i5].animationSupportingLayoutManager.findViewByPosition(SharedMediaLayout.this.pinchCenterPosition)) != null) {
                                    SharedMediaLayout.this.pinchCenterOffset = findViewByPosition.getTop();
                                }
                                ExtendedGridLayoutManager extendedGridLayoutManager = SharedMediaLayout.this.mediaPages[i5].layoutManager;
                                SharedMediaLayout sharedMediaLayout3 = SharedMediaLayout.this;
                                extendedGridLayoutManager.scrollToPositionWithOffset(sharedMediaLayout3.pinchCenterPosition, (-sharedMediaLayout3.mediaPages[i5].listView.getPaddingTop()) + SharedMediaLayout.this.pinchCenterOffset);
                            }
                        }
                    } else {
                        sharedMediaLayout2.saveScrollPosition();
                    }
                    super.onAnimationEnd(animator);
                }
            });
            ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
            ofFloat.setDuration(200L);
            ofFloat.start();
            return;
        }
        this.photoVideoChangeColumnsAnimation = false;
        int[] iArr = this.mediaColumnsCount;
        int i4 = this.animateToColumnsCount;
        iArr[i3] = i4;
        if (i3 != 0) {
            if (getStoriesCount(mediaPage.selectedType) >= 5) {
                SharedConfig.setStoriesColumnsCount(this.animateToColumnsCount);
            }
        } else {
            SharedConfig.setMediaColumnsCount(i4);
        }
        int i5 = 0;
        while (true) {
            MediaPage[] mediaPageArr2 = this.mediaPages;
            if (i5 >= mediaPageArr2.length) {
                break;
            }
            if (mediaPageArr2[i5] != null && mediaPageArr2[i5].listView != null && isTabZoomable(this.mediaPages[i5].selectedType) && (adapter = this.mediaPages[i5].listView.getAdapter()) != null) {
                int itemCount = adapter.getItemCount();
                if (i5 == 0) {
                    this.sharedMediaData[0].setListFrozen(false);
                }
                this.mediaPages[i5].animationSupportingListView.setVisibility(8);
                this.mediaPages[i5].layoutManager.setSpanCount(this.mediaColumnsCount[i3]);
                this.mediaPages[i5].listView.invalidateItemDecorations();
                this.mediaPages[i5].listView.invalidate();
                if (adapter.getItemCount() == itemCount) {
                    AndroidUtilities.updateVisibleRows(this.mediaPages[i5].listView);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            i5++;
        }
        if (this.pinchCenterPosition < 0) {
            saveScrollPosition();
            return;
        }
        while (true) {
            MediaPage[] mediaPageArr3 = this.mediaPages;
            if (i >= mediaPageArr3.length) {
                return;
            }
            if (mediaPageArr3[i].selectedType == this.changeColumnsTab) {
                View findViewByPosition = this.mediaPages[i].animationSupportingLayoutManager.findViewByPosition(this.pinchCenterPosition);
                if (findViewByPosition != null) {
                    this.pinchCenterOffset = findViewByPosition.getTop();
                }
                this.mediaPages[i].layoutManager.scrollToPositionWithOffset(this.pinchCenterPosition, (-this.mediaPages[i].listView.getPaddingTop()) + this.pinchCenterOffset);
            }
            i++;
        }
    }

    private void animateToMediaColumnsCount(final int i) {
        final MediaPage mediaPage = getMediaPage(this.changeColumnsTab);
        this.pinchCenterPosition = -1;
        if (mediaPage != null) {
            mediaPage.listView.stopScroll();
            this.animateToColumnsCount = i;
            final int i2 = 0;
            mediaPage.animationSupportingListView.setVisibility(0);
            int i3 = this.changeColumnsTab;
            if (i3 == 8) {
                mediaPage.animationSupportingListView.setAdapter(this.animationSupportingStoriesAdapter);
            } else if (i3 == 9) {
                mediaPage.animationSupportingListView.setAdapter(this.animationSupportingArchivedStoriesAdapter);
            } else {
                mediaPage.animationSupportingListView.setAdapter(this.animationSupportingPhotoVideoAdapter);
            }
            InternalListView internalListView = mediaPage.animationSupportingListView;
            int paddingLeft = mediaPage.animationSupportingListView.getPaddingLeft();
            int dp = AndroidUtilities.dp(2.0f);
            InternalListView internalListView2 = mediaPage.animationSupportingListView;
            int dp2 = this.changeColumnsTab == 9 ? AndroidUtilities.dp(64.0f) : 0;
            internalListView2.hintPaddingTop = dp2;
            internalListView.setPadding(paddingLeft, dp + dp2, mediaPage.animationSupportingListView.getPaddingRight(), mediaPage.animationSupportingListView.getPaddingBottom());
            mediaPage.animationSupportingLayoutManager.setSpanCount(i);
            mediaPage.animationSupportingListView.invalidateItemDecorations();
            int i4 = 0;
            while (true) {
                MediaPage[] mediaPageArr = this.mediaPages;
                if (i4 >= mediaPageArr.length) {
                    break;
                }
                if (mediaPageArr[i4] != null && isTabZoomable(mediaPageArr[i4].selectedType)) {
                    AndroidUtilities.updateVisibleRows(this.mediaPages[i4].listView);
                }
                i4++;
            }
            this.photoVideoChangeColumnsAnimation = true;
            if (this.changeColumnsTab == 0) {
                this.sharedMediaData[0].setListFrozen(true);
            }
            this.photoVideoChangeColumnsProgress = 0.0f;
            saveScrollPosition();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.notificationsLocker.lock();
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.25
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SharedMediaLayout.this.photoVideoChangeColumnsProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    mediaPage.listView.invalidate();
                }
            });
            i2 = (mediaPage.selectedType == 8 || mediaPage.selectedType == 9) ? 1 : 1;
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.26
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    RecyclerView.Adapter adapter;
                    SharedMediaLayout.this.notificationsLocker.unlock();
                    SharedMediaLayout.this.photoVideoChangeColumnsAnimation = false;
                    SharedMediaLayout.this.mediaColumnsCount[i2] = i;
                    for (int i5 = 0; i5 < SharedMediaLayout.this.mediaPages.length; i5++) {
                        if (SharedMediaLayout.this.mediaPages[i5] != null && SharedMediaLayout.this.mediaPages[i5].listView != null) {
                            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                            if (sharedMediaLayout.isTabZoomable(sharedMediaLayout.mediaPages[i5].selectedType) && (adapter = SharedMediaLayout.this.mediaPages[i5].listView.getAdapter()) != null) {
                                int itemCount = adapter.getItemCount();
                                if (i5 == 0) {
                                    SharedMediaLayout.this.sharedMediaData[0].setListFrozen(false);
                                }
                                SharedMediaLayout.this.mediaPages[i5].layoutManager.setSpanCount(SharedMediaLayout.this.mediaColumnsCount[i2]);
                                SharedMediaLayout.this.mediaPages[i5].listView.invalidateItemDecorations();
                                if (adapter.getItemCount() == itemCount) {
                                    AndroidUtilities.updateVisibleRows(SharedMediaLayout.this.mediaPages[i5].listView);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                                SharedMediaLayout.this.mediaPages[i5].animationSupportingListView.setVisibility(8);
                            }
                        }
                    }
                    SharedMediaLayout.this.saveScrollPosition();
                }
            });
            ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
            ofFloat.setStartDelay(100L);
            ofFloat.setDuration(350L);
            ofFloat.start();
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
        ScrollSlidingTextTabStripInner scrollSlidingTextTabStripInner = new ScrollSlidingTextTabStripInner(context, this.resourcesProvider) { // from class: org.telegram.ui.Components.SharedMediaLayout.27
            @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip
            protected int processColor(int i) {
                return SharedMediaLayout.this.processColor(i);
            }
        };
        int i = this.initialTab;
        if (i != -1) {
            scrollSlidingTextTabStripInner.setInitialTabId(i);
            this.initialTab = -1;
        }
        scrollSlidingTextTabStripInner.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        scrollSlidingTextTabStripInner.setColors(Theme.key_profile_tabSelectedLine, Theme.key_profile_tabSelectedText, Theme.key_profile_tabText, Theme.key_profile_tabSelector);
        scrollSlidingTextTabStripInner.setDelegate(new ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout.28
            @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
            public void onPageSelected(int i2, boolean z) {
                if (SharedMediaLayout.this.mediaPages[0].selectedType == i2) {
                    return;
                }
                SharedMediaLayout.this.mediaPages[1].selectedType = i2;
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
                    SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                    sharedMediaLayout.onTabProgress(sharedMediaLayout.getTabProgress());
                    float photoVideoOptionsAlpha = SharedMediaLayout.this.getPhotoVideoOptionsAlpha(f);
                    SharedMediaLayout.this.photoVideoOptionsItem.setAlpha(photoVideoOptionsAlpha);
                    SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
                    sharedMediaLayout2.photoVideoOptionsItem.setVisibility((photoVideoOptionsAlpha == 0.0f || !sharedMediaLayout2.canShowSearchItem() || SharedMediaLayout.this.isArchivedOnlyStoriesView()) ? 4 : 0);
                    if (SharedMediaLayout.this.canShowSearchItem()) {
                        if (SharedMediaLayout.this.searchItemState == 1) {
                            SharedMediaLayout.this.searchItem.setAlpha(f);
                        } else if (SharedMediaLayout.this.searchItemState == 2) {
                            SharedMediaLayout.this.searchItem.setAlpha(1.0f - f);
                        }
                    } else {
                        SharedMediaLayout.this.searchItem.setVisibility(SharedMediaLayout.this.isStoriesView() ? 8 : 4);
                        SharedMediaLayout.this.searchItem.setAlpha(0.0f);
                    }
                    if (f == 1.0f) {
                        MediaPage mediaPage = SharedMediaLayout.this.mediaPages[0];
                        SharedMediaLayout.this.mediaPages[0] = SharedMediaLayout.this.mediaPages[1];
                        SharedMediaLayout.this.mediaPages[1] = mediaPage;
                        SharedMediaLayout.this.mediaPages[1].setVisibility(8);
                        if (SharedMediaLayout.this.searchItemState == 2) {
                            SharedMediaLayout.this.searchItem.setVisibility(SharedMediaLayout.this.isStoriesView() ? 8 : 4);
                        }
                        SharedMediaLayout.this.searchItemState = 0;
                        SharedMediaLayout.this.startStopVisibleGifs();
                    }
                }
            }
        });
        return scrollSlidingTextTabStripInner;
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

    /* JADX INFO: Access modifiers changed from: private */
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
            this.floatingDateAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.29
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SharedMediaLayout.this.floatingDateAnimation = null;
                }
            });
            this.floatingDateAnimation.start();
            return;
        }
        this.floatingDateView.setAlpha(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollToTop() {
        int itemSize;
        int i = this.mediaPages[0].selectedType;
        if (i == 0) {
            itemSize = SharedPhotoVideoCell.getItemSize(1);
        } else {
            if (i != 1 && i != 2) {
                if (i == 3) {
                    itemSize = AndroidUtilities.dp(100.0f);
                } else if (i != 4) {
                    if (i == 5) {
                        itemSize = AndroidUtilities.dp(60.0f);
                    } else {
                        itemSize = AndroidUtilities.dp(58.0f);
                    }
                }
            }
            itemSize = AndroidUtilities.dp(56.0f);
        }
        if ((this.mediaPages[0].selectedType == 0 ? this.mediaPages[0].layoutManager.findFirstVisibleItemPosition() / this.mediaColumnsCount[0] : this.mediaPages[0].layoutManager.findFirstVisibleItemPosition()) * itemSize >= this.mediaPages[0].listView.getMeasuredHeight() * 1.2f) {
            this.mediaPages[0].scrollHelper.setScrollDirection(1);
            this.mediaPages[0].scrollHelper.scrollToPosition(0, 0, false, true);
            return;
        }
        this.mediaPages[0].listView.smoothScrollToPosition(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLoadMoreScroll(MediaPage mediaPage, final RecyclerListView recyclerListView, LinearLayoutManager linearLayoutManager) {
        int i;
        int i2;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        if (this.photoVideoChangeColumnsAnimation || this.jumpToRunnable != null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (recyclerListView.getFastScroll() == null || !recyclerListView.getFastScroll().isPressed() || currentTimeMillis - mediaPage.lastCheckScrollTime >= 300) {
            mediaPage.lastCheckScrollTime = currentTimeMillis;
            if ((this.searching && this.searchWas) || mediaPage.selectedType == 7) {
                return;
            }
            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(linearLayoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
            int itemCount = recyclerListView.getAdapter().getItemCount();
            if (mediaPage.selectedType == 0 || mediaPage.selectedType == 1 || mediaPage.selectedType == 2 || mediaPage.selectedType == 4) {
                final int i3 = mediaPage.selectedType;
                int startOffset = this.sharedMediaData[i3].getStartOffset() + this.sharedMediaData[i3].messages.size();
                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                if (sharedMediaDataArr[i3].fastScrollDataLoaded && sharedMediaDataArr[i3].fastScrollPeriods.size() > 2 && mediaPage.selectedType == 0 && this.sharedMediaData[i3].messages.size() != 0) {
                    float f = i3 == 0 ? this.mediaColumnsCount[0] : 1;
                    int measuredHeight = (int) ((recyclerListView.getMeasuredHeight() / (recyclerListView.getMeasuredWidth() / f)) * f * 1.5f);
                    if (measuredHeight < 100) {
                        measuredHeight = 100;
                    }
                    if (measuredHeight < this.sharedMediaData[i3].fastScrollPeriods.get(1).startOffset) {
                        measuredHeight = this.sharedMediaData[i3].fastScrollPeriods.get(1).startOffset;
                    }
                    if ((findFirstVisibleItemPosition > startOffset && findFirstVisibleItemPosition - startOffset > measuredHeight) || ((i = findFirstVisibleItemPosition + abs) < this.sharedMediaData[i3].startOffset && this.sharedMediaData[0].startOffset - i > measuredHeight)) {
                        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() {
                                SharedMediaLayout.this.lambda$checkLoadMoreScroll$9(i3, recyclerListView);
                            }
                        };
                        this.jumpToRunnable = runnable;
                        AndroidUtilities.runOnUIThread(runnable);
                        return;
                    }
                }
                itemCount = startOffset;
            }
            if (mediaPage.selectedType == 7) {
                return;
            }
            if (mediaPage.selectedType != 8) {
                if (mediaPage.selectedType != 9) {
                    if (mediaPage.selectedType != 6) {
                        int i4 = 10;
                        if (mediaPage.selectedType != 10) {
                            if (mediaPage.selectedType == 0) {
                                i4 = 3;
                            } else if (mediaPage.selectedType != 5) {
                                i4 = 6;
                            }
                            if ((abs + findFirstVisibleItemPosition > itemCount - i4 || this.sharedMediaData[mediaPage.selectedType].loadingAfterFastScroll) && !this.sharedMediaData[mediaPage.selectedType].loading) {
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
                                    this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, this.sharedMediaData[mediaPage.selectedType].max_id[0], 0, i2, this.topicId, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[mediaPage.selectedType].requestIndex);
                                } else if (this.mergeDialogId != 0 && !this.sharedMediaData[mediaPage.selectedType].endReached[1]) {
                                    this.sharedMediaData[mediaPage.selectedType].loading = true;
                                    this.profileActivity.getMediaDataController().loadMedia(this.mergeDialogId, 50, this.sharedMediaData[mediaPage.selectedType].max_id[1], 0, i2, this.topicId, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[mediaPage.selectedType].requestIndex);
                                }
                            }
                            int i5 = this.sharedMediaData[mediaPage.selectedType].startOffset;
                            if (mediaPage.selectedType == 0) {
                                i5 = this.photoVideoAdapter.getPositionForIndex(0);
                            }
                            if (findFirstVisibleItemPosition - i5 < i4 + 1 && !this.sharedMediaData[mediaPage.selectedType].loading && !this.sharedMediaData[mediaPage.selectedType].startReached && !this.sharedMediaData[mediaPage.selectedType].loadingAfterFastScroll) {
                                loadFromStart(mediaPage.selectedType);
                            }
                            if (this.mediaPages[0].listView == recyclerListView) {
                                if ((this.mediaPages[0].selectedType == 0 || this.mediaPages[0].selectedType == 5) && findFirstVisibleItemPosition != -1 && (findViewHolderForAdapterPosition = recyclerListView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition)) != null && findViewHolderForAdapterPosition.getItemViewType() == 0) {
                                    View view = findViewHolderForAdapterPosition.itemView;
                                    if (view instanceof SharedPhotoVideoCell) {
                                        MessageObject messageObject = ((SharedPhotoVideoCell) view).getMessageObject(0);
                                        if (messageObject != null) {
                                            this.floatingDateView.setCustomDate(messageObject.messageOwner.date, false, true);
                                            return;
                                        }
                                        return;
                                    } else if (view instanceof ContextLinkCell) {
                                        this.floatingDateView.setCustomDate(((ContextLinkCell) view).getDate(), false, true);
                                        return;
                                    } else {
                                        return;
                                    }
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    } else if (abs <= 0 || this.commonGroupsAdapter.endReached || this.commonGroupsAdapter.loading || this.commonGroupsAdapter.chats.isEmpty() || findFirstVisibleItemPosition + abs < itemCount - 5) {
                        return;
                    } else {
                        CommonGroupsAdapter commonGroupsAdapter = this.commonGroupsAdapter;
                        commonGroupsAdapter.getChats(((TLRPC$Chat) commonGroupsAdapter.chats.get(this.commonGroupsAdapter.chats.size() - 1)).id, 100);
                        return;
                    }
                }
                StoriesController.StoriesList storiesList = this.archivedStoriesAdapter.storiesList;
                if (storiesList == null || findFirstVisibleItemPosition + abs <= storiesList.getLoadedCount() - this.mediaColumnsCount[1]) {
                    return;
                }
                this.archivedStoriesAdapter.load(false);
                return;
            }
            StoriesController.StoriesList storiesList2 = this.storiesAdapter.storiesList;
            if (storiesList2 == null || findFirstVisibleItemPosition + abs <= storiesList2.getLoadedCount() - this.mediaColumnsCount[1]) {
                return;
            }
            this.storiesAdapter.load(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkLoadMoreScroll$9(int i, RecyclerListView recyclerListView) {
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
        this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, 0, this.sharedMediaData[i].min_id, i2, this.topicId, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[i].requestIndex);
    }

    public ActionBarMenuItem getSearchItem() {
        return this.searchItem;
    }

    public boolean isSearchItemVisible() {
        if (this.mediaPages[0].selectedType == 7) {
            return this.delegate.canSearchMembers();
        }
        return (this.mediaPages[0].selectedType == 0 || this.mediaPages[0].selectedType == 8 || this.mediaPages[0].selectedType == 9 || this.mediaPages[0].selectedType == 2 || this.mediaPages[0].selectedType == 5 || this.mediaPages[0].selectedType == 6 || this.mediaPages[0].selectedType == 10) ? false : true;
    }

    public boolean isCalendarItemVisible() {
        return this.mediaPages[0].selectedType == 0 || this.mediaPages[0].selectedType == 8 || this.mediaPages[0].selectedType == 9;
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSelectedTabChanged() {
        boolean z = false;
        boolean z2 = isStoriesView() || isArchivedOnlyStoriesView();
        if (this.archivedStoriesAdapter.poller != null) {
            this.archivedStoriesAdapter.poller.start(z2 && getClosestTab() == 9);
        }
        if (this.storiesAdapter.poller != null) {
            ViewsForPeerStoriesRequester viewsForPeerStoriesRequester = this.storiesAdapter.poller;
            if (z2 && getClosestTab() == 8) {
                z = true;
            }
            viewsForPeerStoriesRequester.start(z);
        }
    }

    public void onDestroy() {
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.mediaDidLoad);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.didReceiveNewMessages);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagesDeleted);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByServer);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagePlayingDidReset);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.messagePlayingDidStart);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.storiesListUpdated);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.storiesUpdated);
        this.profileActivity.getNotificationCenter().removeObserver(this, NotificationCenter.channelRecommendationsLoaded);
        StoriesAdapter storiesAdapter = this.storiesAdapter;
        if (storiesAdapter != null && storiesAdapter.storiesList != null) {
            storiesAdapter.destroy();
        }
        StoriesAdapter storiesAdapter2 = this.archivedStoriesAdapter;
        if (storiesAdapter2 == null || storiesAdapter2.storiesList == null) {
            return;
        }
        storiesAdapter2.destroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCurrentTabValid() {
        if (this.scrollSlidingTextTabStrip.hasTab(this.scrollSlidingTextTabStrip.getCurrentTabId())) {
            return;
        }
        int firstTabId = this.scrollSlidingTextTabStrip.getFirstTabId();
        this.scrollSlidingTextTabStrip.setInitialTabId(firstTabId);
        this.mediaPages[0].selectedType = firstTabId;
        switchToCurrentSelectedMode(false);
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
        if (this.topicId != 0) {
            return;
        }
        int i = 0;
        while (true) {
            int[] iArr = supportedFastScrollTypes;
            if (i >= iArr.length) {
                return;
            }
            final int i2 = iArr[i];
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
            final int i3 = this.sharedMediaData[i2].requestIndex;
            ConnectionsManager.getInstance(this.profileActivity.getCurrentAccount()).bindRequestToGuid(ConnectionsManager.getInstance(this.profileActivity.getCurrentAccount()).sendRequest(tLRPC$TL_messages_getSearchResultsPositions, new RequestDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    SharedMediaLayout.this.lambda$loadFastScrollData$13(i3, i2, tLObject, tLRPC$TL_error);
                }
            }), this.profileActivity.getClassGuid());
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFastScrollData$13(final int i, final int i2, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                SharedMediaLayout.this.lambda$loadFastScrollData$12(tLRPC$TL_error, i, i2, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFastScrollData$12(final TLRPC$TL_error tLRPC$TL_error, final int i, final int i2, final TLObject tLObject) {
        NotificationCenter.getInstance(this.profileActivity.getCurrentAccount()).doOnIdle(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                SharedMediaLayout.this.lambda$loadFastScrollData$11(tLRPC$TL_error, i, i2, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFastScrollData$11(TLRPC$TL_error tLRPC$TL_error, int i, int i2, TLObject tLObject) {
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
        Collections.sort(this.sharedMediaData[i2].fastScrollPeriods, SharedMediaLayout$$ExternalSyntheticLambda13.INSTANCE);
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadFastScrollData$10(Period period, Period period2) {
        return period2.date - period.date;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showFastScrollHint(final MediaPage mediaPage, SharedMediaData[] sharedMediaDataArr, boolean z) {
        Runnable runnable;
        if (z) {
            if (SharedConfig.fastScrollHintCount <= 0 || mediaPage.fastScrollHintView != null || mediaPage.fastScrollHinWasShown || mediaPage.listView.getFastScroll() == null || !mediaPage.listView.getFastScroll().isVisible || mediaPage.listView.getFastScroll().getVisibility() != 0 || sharedMediaDataArr[0].totalCount < 50) {
                return;
            }
            SharedConfig.setFastScrollHintCount(SharedConfig.fastScrollHintCount - 1);
            mediaPage.fastScrollHinWasShown = true;
            final SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip = new SharedMediaFastScrollTooltip(mediaPage.getContext());
            mediaPage.fastScrollHintView = sharedMediaFastScrollTooltip;
            mediaPage.addView(sharedMediaFastScrollTooltip, LayoutHelper.createFrame(-2, -2.0f));
            mediaPage.fastScrollHintView.setAlpha(0.0f);
            mediaPage.fastScrollHintView.setScaleX(0.8f);
            mediaPage.fastScrollHintView.setScaleY(0.8f);
            mediaPage.fastScrollHintView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L).start();
            mediaPage.invalidate();
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.lambda$showFastScrollHint$14(SharedMediaLayout.MediaPage.this, sharedMediaFastScrollTooltip);
                }
            };
            mediaPage.fastScrollHideHintRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 4000L);
        } else if (mediaPage.fastScrollHintView == null || (runnable = mediaPage.fastScrollHideHintRunnable) == null) {
        } else {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            mediaPage.fastScrollHideHintRunnable.run();
            mediaPage.fastScrollHideHintRunnable = null;
            mediaPage.fastScrollHintView = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showFastScrollHint$14(MediaPage mediaPage, final SharedMediaFastScrollTooltip sharedMediaFastScrollTooltip) {
        mediaPage.fastScrollHintView = null;
        mediaPage.fastScrollHideHintRunnable = null;
        sharedMediaFastScrollTooltip.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setDuration(220L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.30
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (SharedMediaFastScrollTooltip.this.getParent() != null) {
                    ((ViewGroup) SharedMediaFastScrollTooltip.this.getParent()).removeView(SharedMediaFastScrollTooltip.this);
                }
            }
        }).start();
    }

    public void setCommonGroupsCount(int i) {
        if (this.topicId == 0) {
            this.hasMedia[6] = i;
        }
        updateTabs(true);
        checkCurrentTabValid();
    }

    public void onActionBarItemClick(View view, int i) {
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User;
        TLRPC$EncryptedChat tLRPC$EncryptedChat;
        if (i == 101) {
            if (DialogObject.isEncryptedDialog(this.dialog_id)) {
                tLRPC$EncryptedChat = this.profileActivity.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(this.dialog_id)));
                tLRPC$User = null;
                chat = null;
            } else if (DialogObject.isUserDialog(this.dialog_id)) {
                tLRPC$User = this.profileActivity.getMessagesController().getUser(Long.valueOf(this.dialog_id));
                chat = null;
                tLRPC$EncryptedChat = null;
            } else {
                chat = this.profileActivity.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
                tLRPC$User = null;
                tLRPC$EncryptedChat = null;
            }
            AlertsCreator.createDeleteMessagesAlert(this.profileActivity, tLRPC$User, chat, tLRPC$EncryptedChat, null, this.mergeDialogId, null, this.selectedFiles, null, false, 1, new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.this.lambda$onActionBarItemClick$15();
                }
            }, null, this.resourcesProvider);
            return;
        }
        if (i == 100) {
            if (this.info != null) {
                TLRPC$Chat chat2 = this.profileActivity.getMessagesController().getChat(Long.valueOf(this.info.id));
                if (this.profileActivity.getMessagesController().isChatNoForwards(chat2)) {
                    HintView hintView = this.fwdRestrictedHint;
                    if (hintView != null) {
                        hintView.setText((!ChatObject.isChannel(chat2) || chat2.megagroup) ? LocaleController.getString("ForwardsRestrictedInfoGroup", R.string.ForwardsRestrictedInfoGroup) : LocaleController.getString("ForwardsRestrictedInfoChannel", R.string.ForwardsRestrictedInfoChannel));
                        this.fwdRestrictedHint.showForView(view, true);
                        return;
                    }
                    return;
                }
            }
            if (hasNoforwardsMessage()) {
                HintView hintView2 = this.fwdRestrictedHint;
                if (hintView2 != null) {
                    hintView2.setText(LocaleController.getString("ForwardsRestrictedInfoBot", R.string.ForwardsRestrictedInfoBot));
                    this.fwdRestrictedHint.showForView(view, true);
                    return;
                }
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putBoolean("canSelectTopics", true);
            bundle.putInt("dialogsType", 3);
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda18
                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                    boolean lambda$onActionBarItemClick$16;
                    lambda$onActionBarItemClick$16 = SharedMediaLayout.this.lambda$onActionBarItemClick$16(dialogsActivity2, arrayList, charSequence, z, topicsFragment);
                    return lambda$onActionBarItemClick$16;
                }
            });
            this.profileActivity.presentFragment(dialogsActivity);
        } else if (i == 102 && this.selectedFiles[0].size() + this.selectedFiles[1].size() == 1) {
            SparseArray<MessageObject>[] sparseArrayArr = this.selectedFiles;
            MessageObject valueAt = sparseArrayArr[sparseArrayArr[0].size() == 1 ? (char) 0 : (char) 1].valueAt(0);
            Bundle bundle2 = new Bundle();
            long dialogId = valueAt.getDialogId();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                bundle2.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
            } else if (DialogObject.isUserDialog(dialogId)) {
                bundle2.putLong("user_id", dialogId);
            } else {
                TLRPC$Chat chat3 = this.profileActivity.getMessagesController().getChat(Long.valueOf(-dialogId));
                if (chat3 != null && chat3.migrated_to != null) {
                    bundle2.putLong("migrated_to", dialogId);
                    dialogId = -chat3.migrated_to.channel_id;
                }
                bundle2.putLong("chat_id", -dialogId);
            }
            bundle2.putInt("message_id", valueAt.getId());
            bundle2.putBoolean("need_remove_previous_same_chat_activity", false);
            ChatActivity chatActivity = new ChatActivity(bundle2);
            chatActivity.highlightMessageId = valueAt.getId();
            int i2 = this.topicId;
            if (i2 != 0) {
                ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(dialogId, i2));
                bundle2.putInt("message_id", valueAt.getId());
            }
            this.profileActivity.presentFragment(chatActivity, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActionBarItemClick$15() {
        showActionMode(false);
        this.actionBar.closeSearchField();
        this.cantDeleteMessagesCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onActionBarItemClick$16(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
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
        if (arrayList.size() > 1 || ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId == this.profileActivity.getUserConfig().getClientUserId() || charSequence != null) {
            updateRowsSelection(true);
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                long j = ((MessagesStorage.TopicKey) arrayList.get(i3)).dialogId;
                if (charSequence != null) {
                    this.profileActivity.getSendMessagesHelper().sendMessage(SendMessagesHelper.SendMessageParams.of(charSequence.toString(), j, null, null, null, true, null, null, null, true, 0, null, false));
                }
                this.profileActivity.getSendMessagesHelper().sendMessage(arrayList2, j, false, false, true, 0);
            }
            dialogsActivity.finishFragment();
            BaseFragment baseFragment = this.profileActivity;
            UndoView undoView = baseFragment instanceof ProfileActivity ? ((ProfileActivity) baseFragment).getUndoView() : null;
            if (undoView != null) {
                if (arrayList.size() == 1) {
                    undoView.showWithAction(((MessagesStorage.TopicKey) arrayList.get(0)).dialogId, 53, Integer.valueOf(arrayList2.size()));
                } else {
                    undoView.showWithAction(0L, 53, Integer.valueOf(arrayList2.size()), Integer.valueOf(arrayList.size()), (Runnable) null, (Runnable) null);
                }
            }
        } else {
            long j2 = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (DialogObject.isEncryptedDialog(j2)) {
                bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j2));
            } else {
                if (DialogObject.isUserDialog(j2)) {
                    bundle.putLong("user_id", j2);
                } else {
                    bundle.putLong("chat_id", -j2);
                }
                if (!this.profileActivity.getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
                    return true;
                }
            }
            this.profileActivity.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            ChatActivity chatActivity = new ChatActivity(bundle);
            ForumUtilities.applyTopic(chatActivity, (MessagesStorage.TopicKey) arrayList.get(0));
            dialogsActivity.presentFragment(chatActivity, true);
            chatActivity.showFieldPanelForForward(true, arrayList2);
        }
        return true;
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
                    this.searchItem.setVisibility(isStoriesView() ? 8 : 4);
                }
                this.searchItemState = 0;
            }
        } else {
            this.searchItem.setVisibility(isStoriesView() ? 8 : 4);
            this.searchItem.setAlpha(0.0f);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        hideFloatingDateView(true);
        this.maybeStartTracking = false;
        this.startedTracking = true;
        onTabScroll(true);
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
        onTabProgress(getTabProgress());
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
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i5 >= mediaPageArr.length) {
                break;
            }
            mediaPageArr[i5].setTranslationY(this.topPadding - this.lastMeasuredTopPadding);
            i5++;
        }
        FragmentContextView fragmentContextView = this.fragmentContextView;
        if (fragmentContextView != null) {
            fragmentContextView.setTranslationY(AndroidUtilities.dp(48.0f) + i2);
        }
        this.additionalFloatingTranslation = i2;
        ChatActionCell chatActionCell = this.floatingDateView;
        chatActionCell.setTranslationY((chatActionCell.getTag() == null ? -AndroidUtilities.dp(48.0f) : 0) + this.additionalFloatingTranslation);
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
                    MediaPage mediaPage = (MediaPage) childAt;
                    mediaPage.listView.setPadding(0, mediaPage.listView.topPadding, 0, this.topPadding);
                } else {
                    measureChildWithMargins(childAt, i, 0, i2, 0);
                }
            }
        }
    }

    public boolean checkTabsAnimationInProgress() {
        if (this.tabsAnimationInProgress) {
            boolean z = true;
            if (this.backAnimation) {
                if (Math.abs(this.mediaPages[0].getTranslationX()) < 1.0f) {
                    this.mediaPages[0].setTranslationX(0.0f);
                    MediaPage[] mediaPageArr = this.mediaPages;
                    mediaPageArr[1].setTranslationX(mediaPageArr[0].getMeasuredWidth() * (this.animatingForward ? 1 : -1));
                }
                z = false;
            } else {
                if (Math.abs(this.mediaPages[1].getTranslationX()) < 1.0f) {
                    MediaPage[] mediaPageArr2 = this.mediaPages;
                    mediaPageArr2[0].setTranslationX(mediaPageArr2[0].getMeasuredWidth() * (this.animatingForward ? -1 : 1));
                    this.mediaPages[1].setTranslationX(0.0f);
                }
                z = false;
            }
            if (z) {
                AnimatorSet animatorSet = this.tabsAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.tabsAnimation = null;
                }
                this.tabsAnimationInProgress = false;
            }
            onTabProgress(getTabProgress());
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
        boolean z;
        int i = 0;
        if (this.disableScrolling || this.profileActivity.getParentLayout() == null || this.profileActivity.getParentLayout().checkTransitionAnimation() || checkTabsAnimationInProgress() || this.isInPinchToZoomTouchMode) {
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
                    onTabScroll(false);
                    this.mediaPages[0].setTranslationX(0.0f);
                    MediaPage[] mediaPageArr = this.mediaPages;
                    mediaPageArr[1].setTranslationX(this.animatingForward ? mediaPageArr[0].getMeasuredWidth() : -mediaPageArr[0].getMeasuredWidth());
                    this.scrollSlidingTextTabStrip.selectTabWithId(this.mediaPages[1].selectedType, 0.0f);
                    onTabProgress(getTabProgress());
                }
            }
            if (this.maybeStartTracking && !this.startedTracking) {
                if (Math.abs(x) >= AndroidUtilities.getPixelsInCM(0.3f, true) && Math.abs(x) > abs) {
                    prepareForMoving(motionEvent, x < 0);
                }
            } else if (this.startedTracking) {
                this.mediaPages[0].setTranslationX(x);
                if (this.animatingForward) {
                    MediaPage[] mediaPageArr2 = this.mediaPages;
                    mediaPageArr2[1].setTranslationX(mediaPageArr2[0].getMeasuredWidth() + x);
                } else {
                    MediaPage[] mediaPageArr3 = this.mediaPages;
                    mediaPageArr3[1].setTranslationX(x - mediaPageArr3[0].getMeasuredWidth());
                }
                float abs2 = Math.abs(x) / this.mediaPages[0].getMeasuredWidth();
                if (canShowSearchItem()) {
                    int i2 = this.searchItemState;
                    if (i2 == 2) {
                        this.searchItem.setAlpha(1.0f - abs2);
                    } else if (i2 == 1) {
                        this.searchItem.setAlpha(abs2);
                    }
                    float photoVideoOptionsAlpha = getPhotoVideoOptionsAlpha(abs2);
                    this.photoVideoOptionsItem.setAlpha(photoVideoOptionsAlpha);
                    this.photoVideoOptionsItem.setVisibility((photoVideoOptionsAlpha == 0.0f || !canShowSearchItem() || isArchivedOnlyStoriesView()) ? 4 : 4);
                } else {
                    this.searchItem.setAlpha(0.0f);
                }
                this.scrollSlidingTextTabStrip.selectTabWithId(this.mediaPages[1].selectedType, abs2);
                onTabProgress(getTabProgress());
                onSelectedTabChanged();
            }
        } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
            stopScroll(motionEvent);
        }
        return this.startedTracking;
    }

    public void scrollToPage(int i) {
        ScrollSlidingTextTabStripInner scrollSlidingTextTabStripInner;
        if (this.disableScrolling || (scrollSlidingTextTabStripInner = this.scrollSlidingTextTabStrip) == null) {
            return;
        }
        scrollSlidingTextTabStripInner.scrollTo(i);
    }

    private void stopScroll(MotionEvent motionEvent) {
        float f;
        float f2;
        float measuredWidth;
        MediaPage[] mediaPageArr;
        MediaPage[] mediaPageArr2;
        int measuredWidth2;
        MediaPage[] mediaPageArr3;
        MediaPage[] mediaPageArr4;
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            return;
        }
        velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        if (motionEvent == null || motionEvent.getAction() == 3) {
            f = 0.0f;
            f2 = 0.0f;
        } else {
            f = this.velocityTracker.getXVelocity();
            f2 = this.velocityTracker.getYVelocity();
            if (!this.startedTracking && Math.abs(f) >= 3000.0f && Math.abs(f) > Math.abs(f2)) {
                prepareForMoving(motionEvent, f < 0.0f);
            }
        }
        if (this.startedTracking) {
            float x = this.mediaPages[0].getX();
            this.tabsAnimation = new AnimatorSet();
            this.backAnimation = Math.abs(x) < ((float) this.mediaPages[0].getMeasuredWidth()) / 3.0f && (Math.abs(f) < 3500.0f || Math.abs(f) < Math.abs(f2));
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SharedMediaLayout.this.lambda$stopScroll$17(valueAnimator);
                }
            });
            if (this.backAnimation) {
                measuredWidth = Math.abs(x);
                if (this.animatingForward) {
                    this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, mediaPageArr4[1].getMeasuredWidth()), ofFloat);
                } else {
                    this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, -mediaPageArr3[1].getMeasuredWidth()), ofFloat);
                }
            } else {
                measuredWidth = this.mediaPages[0].getMeasuredWidth() - Math.abs(x);
                if (this.animatingForward) {
                    this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, -mediaPageArr2[0].getMeasuredWidth()), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, 0.0f), ofFloat);
                } else {
                    this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(this.mediaPages[0], View.TRANSLATION_X, mediaPageArr[0].getMeasuredWidth()), ObjectAnimator.ofFloat(this.mediaPages[1], View.TRANSLATION_X, 0.0f), ofFloat);
                }
            }
            this.tabsAnimation.setInterpolator(interpolator);
            int measuredWidth3 = getMeasuredWidth();
            float f3 = measuredWidth3 / 2;
            float distanceInfluenceForSnapDuration = f3 + (AndroidUtilities.distanceInfluenceForSnapDuration(Math.min(1.0f, (measuredWidth * 1.0f) / measuredWidth3)) * f3);
            float abs = Math.abs(f);
            if (abs > 0.0f) {
                measuredWidth2 = Math.round(Math.abs(distanceInfluenceForSnapDuration / abs) * 1000.0f) * 4;
            } else {
                measuredWidth2 = (int) (((measuredWidth / getMeasuredWidth()) + 1.0f) * 100.0f);
            }
            this.tabsAnimation.setDuration(Math.max((int) ImageReceiver.DEFAULT_CROSSFADE_DURATION, Math.min(measuredWidth2, 600)));
            this.tabsAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.31
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
                                SharedMediaLayout.this.searchItem.setVisibility(SharedMediaLayout.this.isStoriesView() ? 8 : 4);
                            }
                        } else {
                            SharedMediaLayout.this.searchItem.setVisibility(SharedMediaLayout.this.isStoriesView() ? 8 : 4);
                            SharedMediaLayout.this.searchItem.setAlpha(0.0f);
                        }
                        SharedMediaLayout.this.searchItemState = 0;
                    } else {
                        MediaPage mediaPage = SharedMediaLayout.this.mediaPages[0];
                        SharedMediaLayout.this.mediaPages[0] = SharedMediaLayout.this.mediaPages[1];
                        SharedMediaLayout.this.mediaPages[1] = mediaPage;
                        SharedMediaLayout.this.mediaPages[1].setVisibility(8);
                        if (SharedMediaLayout.this.searchItemState == 2) {
                            SharedMediaLayout.this.searchItem.setVisibility(SharedMediaLayout.this.isStoriesView() ? 8 : 4);
                        }
                        SharedMediaLayout.this.searchItemState = 0;
                        SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                        sharedMediaLayout.scrollSlidingTextTabStrip.selectTabWithId(sharedMediaLayout.mediaPages[0].selectedType, 1.0f);
                        SharedMediaLayout.this.onSelectedTabChanged();
                        SharedMediaLayout.this.startStopVisibleGifs();
                    }
                    SharedMediaLayout.this.tabsAnimationInProgress = false;
                    SharedMediaLayout.this.maybeStartTracking = false;
                    SharedMediaLayout.this.startedTracking = false;
                    SharedMediaLayout.this.onTabScroll(false);
                    SharedMediaLayout.this.actionBar.setEnabled(true);
                    SharedMediaLayout.this.scrollSlidingTextTabStrip.setEnabled(true);
                }
            });
            this.tabsAnimation.start();
            this.tabsAnimationInProgress = true;
            this.startedTracking = false;
            onSelectedTabChanged();
        } else {
            this.maybeStartTracking = false;
            this.actionBar.setEnabled(true);
            this.scrollSlidingTextTabStrip.setEnabled(true);
        }
        VelocityTracker velocityTracker2 = this.velocityTracker;
        if (velocityTracker2 != null) {
            velocityTracker2.recycle();
            this.velocityTracker = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopScroll$17(ValueAnimator valueAnimator) {
        onTabProgress(getTabProgress());
    }

    public void disableScroll(boolean z) {
        if (z) {
            stopScroll(null);
        }
        this.disableScrolling = z;
    }

    public boolean closeActionMode() {
        return closeActionMode(true);
    }

    public boolean closeActionMode(boolean z) {
        if (this.isActionModeShowed) {
            for (int i = 1; i >= 0; i--) {
                this.selectedFiles[i].clear();
            }
            this.cantDeleteMessagesCount = 0;
            showActionMode(false);
            updateRowsSelection(z);
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

    public boolean isActionModeShown() {
        return this.isActionModeShowed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showActionMode(final boolean z) {
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
        this.actionModeAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.32
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
                if (z) {
                    return;
                }
                SharedMediaLayout.this.actionModeLayout.setVisibility(4);
            }
        });
        this.actionModeAnimation.start();
    }

    /* JADX WARN: Removed duplicated region for block: B:181:0x0372  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x04a0  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x04c1 A[SYNTHETIC] */
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
        int i7 = 0;
        int i8 = 0;
        if (i == NotificationCenter.mediaDidLoad) {
            long longValue = ((Long) objArr[0]).longValue();
            int intValue = ((Integer) objArr[3]).intValue();
            int intValue2 = ((Integer) objArr[7]).intValue();
            int intValue3 = ((Integer) objArr[4]).intValue();
            boolean booleanValue = ((Boolean) objArr[6]).booleanValue();
            intValue3 = (intValue3 == 6 || intValue3 == 7) ? 0 : 0;
            if (intValue == this.profileActivity.getClassGuid()) {
                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                if (intValue2 == sharedMediaDataArr[intValue3].requestIndex) {
                    if (intValue3 != 0 && intValue3 != 1 && intValue3 != 2 && intValue3 != 4) {
                        sharedMediaDataArr[intValue3].totalCount = ((Integer) objArr[1]).intValue();
                    }
                    ArrayList arrayList = (ArrayList) objArr[2];
                    boolean isEncryptedDialog = DialogObject.isEncryptedDialog(this.dialog_id);
                    int i9 = longValue == this.dialog_id ? 0 : 1;
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
                            if (this.sharedMediaData[intValue3].addMessage(messageObject, i9, true, isEncryptedDialog)) {
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
                        for (int i10 = 0; i10 < arrayList.size(); i10++) {
                            MessageObject messageObject2 = (MessageObject) arrayList.get(i10);
                            if (this.sharedMediaData[intValue3].addMessage(messageObject2, i9, false, isEncryptedDialog)) {
                                sparseBooleanArray.put(messageObject2.getId(), true);
                                SharedMediaData.access$8410(this.sharedMediaData[intValue3]);
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
                        this.sharedMediaData[intValue3].endReached[i9] = ((Boolean) objArr[5]).booleanValue();
                        SharedMediaData[] sharedMediaDataArr5 = this.sharedMediaData;
                        if (sharedMediaDataArr5[intValue3].endReached[i9]) {
                            sharedMediaDataArr5[intValue3].totalCount = sharedMediaDataArr5[intValue3].messages.size() + this.sharedMediaData[intValue3].startOffset;
                        }
                    }
                    if (!booleanValue && i9 == 0) {
                        SharedMediaData[] sharedMediaDataArr6 = this.sharedMediaData;
                        if (sharedMediaDataArr6[intValue3].endReached[i9] && this.mergeDialogId != 0) {
                            sharedMediaDataArr6[intValue3].loading = true;
                            this.profileActivity.getMediaDataController().loadMedia(this.mergeDialogId, 50, this.sharedMediaData[intValue3].max_id[1], 0, intValue3, this.topicId, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[intValue3].requestIndex);
                        }
                    }
                    if (adapter3 != null) {
                        InternalListView internalListView = null;
                        int i11 = 0;
                        while (true) {
                            MediaPage[] mediaPageArr = this.mediaPages;
                            if (i11 >= mediaPageArr.length) {
                                break;
                            }
                            if (mediaPageArr[i11].listView.getAdapter() == adapter3) {
                                internalListView = this.mediaPages[i11].listView;
                                this.mediaPages[i11].listView.stopScroll();
                            }
                            i11++;
                        }
                        int itemCount = adapter3.getItemCount();
                        SharedPhotoVideoAdapter sharedPhotoVideoAdapter = this.photoVideoAdapter;
                        if (adapter3 == sharedPhotoVideoAdapter) {
                            if (sharedPhotoVideoAdapter.getItemCount() == i5) {
                                AndroidUtilities.updateVisibleRows(internalListView);
                            } else {
                                this.photoVideoAdapter.notifyDataSetChanged();
                            }
                        } else {
                            try {
                                adapter3.notifyDataSetChanged();
                            } catch (Throwable unused) {
                            }
                        }
                        if (!this.sharedMediaData[intValue3].messages.isEmpty() || this.sharedMediaData[intValue3].loading) {
                            if (internalListView != null && (adapter3 == this.photoVideoAdapter || itemCount >= i5)) {
                                animateItemsEnter(internalListView, i5, sparseBooleanArray);
                            }
                        } else if (internalListView != null) {
                            animateItemsEnter(internalListView, i5, sparseBooleanArray);
                        }
                        if (internalListView != null && !this.sharedMediaData[intValue3].loadingAfterFastScroll) {
                            if (size2 == 0) {
                                for (int i12 = 0; i12 < 2; i12++) {
                                    if (this.mediaPages[i12].selectedType == 0) {
                                        ((LinearLayoutManager) internalListView.getLayoutManager()).scrollToPositionWithOffset(this.photoVideoAdapter.getPositionForIndex(0), 0);
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
        int i13 = -1;
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
                        int i14 = 0;
                        while (true) {
                            SharedMediaData[] sharedMediaDataArr8 = this.sharedMediaData;
                            if (i14 < sharedMediaDataArr8.length) {
                                if (sharedMediaDataArr8[i14].deleteMessage(((Integer) arrayList2.get(i4)).intValue(), i3) != null) {
                                    i13 = i14;
                                    z = true;
                                }
                                i14++;
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
                        if (i13 == 0 || i13 == 1 || i13 == 2 || i13 == 4) {
                            loadFastScrollData(true);
                        }
                    }
                    getMediaPage(i13);
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
            getMediaPage(i13);
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
            int i15 = 0;
            boolean z2 = false;
            while (i15 < arrayList3.size()) {
                MessageObject messageObject3 = (MessageObject) arrayList3.get(i15);
                if (MessageObject.getMedia(messageObject3.messageOwner) != null && !messageObject3.needDrawBluredPreview()) {
                    int mediaType = MediaDataController.getMediaType(messageObject3.messageOwner);
                    if (mediaType == i13) {
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
                i15++;
                i13 = -1;
            }
            if (!z2) {
                return;
            }
            this.scrolling = true;
            while (true) {
                MediaPage[] mediaPageArr3 = this.mediaPages;
                if (i7 < mediaPageArr3.length) {
                    if (mediaPageArr3[i7].selectedType != 0) {
                        if (this.mediaPages[i7].selectedType != 1) {
                            if (this.mediaPages[i7].selectedType != 2) {
                                if (this.mediaPages[i7].selectedType != 3) {
                                    if (this.mediaPages[i7].selectedType == 4) {
                                        adapter = this.audioAdapter;
                                    } else {
                                        adapter = this.mediaPages[i7].selectedType == 5 ? this.gifAdapter : null;
                                        if (adapter != null) {
                                            adapter.getItemCount();
                                            this.photoVideoAdapter.notifyDataSetChanged();
                                            this.documentsAdapter.notifyDataSetChanged();
                                            this.voiceAdapter.notifyDataSetChanged();
                                            this.linksAdapter.notifyDataSetChanged();
                                            this.audioAdapter.notifyDataSetChanged();
                                            this.gifAdapter.notifyDataSetChanged();
                                        }
                                        i7++;
                                    }
                                } else {
                                    adapter = this.linksAdapter;
                                }
                            } else {
                                adapter = this.voiceAdapter;
                            }
                            if (adapter != null) {
                            }
                            i7++;
                        } else {
                            adapter = this.documentsAdapter;
                        }
                    } else {
                        adapter = this.photoVideoAdapter;
                    }
                    if (adapter != null) {
                    }
                    i7++;
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
                if (i8 >= sharedMediaDataArr10.length) {
                    return;
                }
                sharedMediaDataArr10[i8].replaceMid(num.intValue(), num2.intValue());
                i8++;
            }
        } else if (i == NotificationCenter.messagePlayingDidStart || i == NotificationCenter.messagePlayingPlayStateChanged || i == NotificationCenter.messagePlayingDidReset) {
            if (i == NotificationCenter.messagePlayingDidReset || i == NotificationCenter.messagePlayingPlayStateChanged) {
                int i16 = 0;
                while (true) {
                    MediaPage[] mediaPageArr4 = this.mediaPages;
                    if (i16 >= mediaPageArr4.length) {
                        return;
                    }
                    int childCount = mediaPageArr4[i16].listView.getChildCount();
                    for (int i17 = 0; i17 < childCount; i17++) {
                        View childAt = this.mediaPages[i16].listView.getChildAt(i17);
                        if (childAt instanceof SharedAudioCell) {
                            SharedAudioCell sharedAudioCell = (SharedAudioCell) childAt;
                            if (sharedAudioCell.getMessage() != null) {
                                sharedAudioCell.updateButtonState(false, true);
                            }
                        }
                    }
                    i16++;
                }
            } else if (((MessageObject) objArr[0]).eventId == 0) {
                int i18 = 0;
                while (true) {
                    MediaPage[] mediaPageArr5 = this.mediaPages;
                    if (i18 >= mediaPageArr5.length) {
                        return;
                    }
                    int childCount2 = mediaPageArr5[i18].listView.getChildCount();
                    for (int i19 = 0; i19 < childCount2; i19++) {
                        View childAt2 = this.mediaPages[i18].listView.getChildAt(i19);
                        if (childAt2 instanceof SharedAudioCell) {
                            SharedAudioCell sharedAudioCell2 = (SharedAudioCell) childAt2;
                            if (sharedAudioCell2.getMessage() != null) {
                                sharedAudioCell2.updateButtonState(false, true);
                            }
                        }
                    }
                    i18++;
                }
            }
        } else if (i == NotificationCenter.storiesListUpdated) {
            StoriesController.StoriesList storiesList = (StoriesController.StoriesList) objArr[0];
            StoriesAdapter storiesAdapter = this.storiesAdapter;
            if (storiesAdapter != null && storiesList == storiesAdapter.storiesList) {
                MediaPage mediaPage = getMediaPage(8);
                if (mediaPage != null) {
                    if (mediaPage.fastScrollEnabled != (storiesList.getCount() > 0)) {
                        mediaPage.fastScrollEnabled = storiesList.getCount() > 0;
                        updateFastScrollVisibility(mediaPage, true);
                    }
                }
                this.storiesAdapter.notifyDataSetChanged();
                Delegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.updateSelectedMediaTabText();
                }
            }
            StoriesAdapter storiesAdapter2 = this.archivedStoriesAdapter;
            if (storiesAdapter2 == null || storiesList != storiesAdapter2.storiesList) {
                return;
            }
            MediaPage mediaPage2 = getMediaPage(9);
            if (mediaPage2 != null) {
                if (mediaPage2.fastScrollEnabled != (storiesList.getCount() > 0)) {
                    mediaPage2.fastScrollEnabled = storiesList.getCount() > 0;
                    updateFastScrollVisibility(mediaPage2, true);
                }
            }
            this.archivedStoriesAdapter.notifyDataSetChanged();
            Delegate delegate2 = this.delegate;
            if (delegate2 != null) {
                delegate2.updateSelectedMediaTabText();
            }
        } else if (i != NotificationCenter.storiesUpdated) {
            if (i == NotificationCenter.channelRecommendationsLoaded && ((Long) objArr[0]).longValue() == (-this.dialog_id)) {
                this.channelRecommendationsAdapter.update(true);
                updateTabs(true);
                checkCurrentTabValid();
            }
        } else {
            int i20 = 0;
            while (true) {
                MediaPage[] mediaPageArr6 = this.mediaPages;
                if (i20 >= mediaPageArr6.length) {
                    return;
                }
                if (mediaPageArr6[i20] != null && mediaPageArr6[i20].listView != null && (this.mediaPages[i20].selectedType == 8 || this.mediaPages[i20].selectedType == 9)) {
                    for (int i21 = 0; i21 < this.mediaPages[i20].listView.getChildCount(); i21++) {
                        View childAt3 = this.mediaPages[i20].listView.getChildAt(i21);
                        if (childAt3 instanceof SharedPhotoVideoCell2) {
                            ((SharedPhotoVideoCell2) childAt3).updateViews();
                        }
                    }
                }
                i20++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveScrollPosition() {
        int i;
        int i2 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i2 >= mediaPageArr.length) {
                return;
            }
            InternalListView internalListView = mediaPageArr[i2].listView;
            if (internalListView != null) {
                int i3 = 0;
                int i4 = 0;
                for (int i5 = 0; i5 < internalListView.getChildCount(); i5++) {
                    View childAt = internalListView.getChildAt(i5);
                    if (childAt instanceof SharedPhotoVideoCell2) {
                        SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) childAt;
                        int messageId = sharedPhotoVideoCell2.getMessageId();
                        i4 = sharedPhotoVideoCell2.getTop();
                        i3 = messageId;
                    }
                    if (childAt instanceof SharedDocumentCell) {
                        SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) childAt;
                        int id = sharedDocumentCell.getMessage().getId();
                        i4 = sharedDocumentCell.getTop();
                        i3 = id;
                    }
                    if (childAt instanceof SharedAudioCell) {
                        SharedAudioCell sharedAudioCell = (SharedAudioCell) childAt;
                        i3 = sharedAudioCell.getMessage().getId();
                        i4 = sharedAudioCell.getTop();
                    }
                    if (i3 != 0) {
                        break;
                    }
                }
                if (i3 != 0) {
                    int i6 = -1;
                    int i7 = this.mediaPages[i2].selectedType;
                    if (i7 == 8 || i7 == 9) {
                        StoriesAdapter storiesAdapter = i7 == 8 ? this.storiesAdapter : this.archivedStoriesAdapter;
                        if (storiesAdapter.storiesList != null) {
                            int i8 = 0;
                            while (true) {
                                if (i8 >= storiesAdapter.storiesList.messageObjects.size()) {
                                    break;
                                } else if (i3 == storiesAdapter.storiesList.messageObjects.get(i8).getId()) {
                                    i6 = i8;
                                    break;
                                } else {
                                    i8++;
                                }
                            }
                        }
                        i = i6;
                    } else if (i7 >= 0 && i7 < this.sharedMediaData.length) {
                        int i9 = 0;
                        while (true) {
                            if (i9 >= this.sharedMediaData[i7].messages.size()) {
                                break;
                            } else if (i3 == this.sharedMediaData[i7].messages.get(i9).getId()) {
                                i6 = i9;
                                break;
                            } else {
                                i9++;
                            }
                        }
                        i = this.sharedMediaData[i7].startOffset + i6;
                    }
                    if (i6 >= 0) {
                        ((LinearLayoutManager) internalListView.getLayoutManager()).scrollToPositionWithOffset(i, (-this.mediaPages[i2].listView.getPaddingTop()) + i4);
                        if (this.photoVideoChangeColumnsAnimation) {
                            this.mediaPages[i2].animationSupportingLayoutManager.scrollToPositionWithOffset(i, (-this.mediaPages[i2].listView.getPaddingTop()) + i4);
                        }
                    }
                }
            }
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        getViewTreeObserver().addOnPreDrawListener(new 33(recyclerListView, sparseBooleanArray, view, i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 33 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ SparseBooleanArray val$addedMesages;
        final /* synthetic */ RecyclerListView val$finalListView;
        final /* synthetic */ View val$finalProgressView;
        final /* synthetic */ int val$oldItemCount;

        33(RecyclerListView recyclerListView, SparseBooleanArray sparseBooleanArray, View view, int i) {
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
                        final int messageId = SharedMediaLayout.this.getMessageId(childAt);
                        if (messageId != 0 && this.val$addedMesages.get(messageId, false)) {
                            SharedMediaLayout.this.messageAlphaEnter.put(messageId, Float.valueOf(0.0f));
                            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                            final RecyclerListView recyclerListView = this.val$finalListView;
                            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$33$$ExternalSyntheticLambda0
                                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    SharedMediaLayout.33.this.lambda$onPreDraw$0(messageId, recyclerListView, valueAnimator);
                                }
                            });
                            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.33.1
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    SharedMediaLayout.this.messageAlphaEnter.remove(messageId);
                                    33.this.val$finalListView.invalidate();
                                }
                            });
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
                        final RecyclerView.LayoutManager layoutManager = this.val$finalListView.getLayoutManager();
                        if (layoutManager != null) {
                            layoutManager.ignoreView(this.val$finalProgressView);
                            View view2 = this.val$finalProgressView;
                            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view2, View.ALPHA, view2.getAlpha(), 0.0f);
                            ofFloat3.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SharedMediaLayout.33.2
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    33.this.val$finalProgressView.setAlpha(1.0f);
                                    layoutManager.stopIgnoringView(33.this.val$finalProgressView);
                                    33 r2 = 33.this;
                                    r2.val$finalListView.removeView(r2.val$finalProgressView);
                                }
                            });
                            ofFloat3.start();
                        }
                    }
                }
                animatorSet.start();
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPreDraw$0(int i, RecyclerListView recyclerListView, ValueAnimator valueAnimator) {
            SharedMediaLayout.this.messageAlphaEnter.put(i, (Float) valueAnimator.getAnimatedValue());
            recyclerListView.invalidate();
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
        final int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i >= mediaPageArr.length) {
                return;
            }
            if (mediaPageArr[i].listView != null) {
                this.mediaPages[i].listView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.Components.SharedMediaLayout.34
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public boolean onPreDraw() {
                        SharedMediaLayout.this.mediaPages[i].getViewTreeObserver().removeOnPreDrawListener(this);
                        SharedMediaLayout.this.fixLayoutInternal(i);
                        return true;
                    }
                });
            }
            i++;
        }
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
        boolean z = tLRPC$ChatFull2 != null && tLRPC$ChatFull2.stories_pinned_available;
        this.info = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            long j = tLRPC$ChatFull.migrated_from_chat_id;
            if (j != 0 && this.mergeDialogId == 0) {
                this.mergeDialogId = -j;
                int i = 0;
                while (true) {
                    SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                    if (i >= sharedMediaDataArr.length) {
                        break;
                    }
                    sharedMediaDataArr[i].max_id[1] = this.info.migrated_from_max_id;
                    sharedMediaDataArr[i].endReached[1] = false;
                    i++;
                }
            }
        }
        TLRPC$ChatFull tLRPC$ChatFull3 = this.info;
        if (tLRPC$ChatFull3 == null || z == tLRPC$ChatFull3.stories_pinned_available) {
            return;
        }
        ScrollSlidingTextTabStripInner scrollSlidingTextTabStripInner = this.scrollSlidingTextTabStrip;
        if (scrollSlidingTextTabStripInner != null) {
            scrollSlidingTextTabStripInner.setInitialTabId(isArchivedOnlyStoriesView() ? 9 : 8);
        }
        updateTabs(true);
        switchToCurrentSelectedMode(false);
    }

    public void setUserInfo(TLRPC$UserFull tLRPC$UserFull) {
        TLRPC$UserFull tLRPC$UserFull2 = this.userInfo;
        boolean z = tLRPC$UserFull2 != null && tLRPC$UserFull2.stories_pinned_available;
        this.userInfo = tLRPC$UserFull;
        updateTabs(true);
        if (tLRPC$UserFull == null || z == tLRPC$UserFull.stories_pinned_available) {
            return;
        }
        scrollToPage(8);
    }

    public void setChatUsers(ArrayList<Integer> arrayList, TLRPC$ChatFull tLRPC$ChatFull) {
        int i = 0;
        int i2 = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i2 < mediaPageArr.length) {
                if (mediaPageArr[i2].selectedType == 7 && this.mediaPages[i2].listView.getAdapter().getItemCount() != 0 && this.profileActivity.getMessagesController().getStoriesController().hasLoadingStories()) {
                    return;
                }
                i2++;
            } else {
                if (this.topicId == 0) {
                    this.chatUsersAdapter.chatInfo = tLRPC$ChatFull;
                    this.chatUsersAdapter.sortedUsers = arrayList;
                }
                updateTabs(true);
                while (true) {
                    MediaPage[] mediaPageArr2 = this.mediaPages;
                    if (i >= mediaPageArr2.length) {
                        return;
                    }
                    if (mediaPageArr2[i].selectedType == 7) {
                        this.mediaPages[i].listView.getAdapter().notifyDataSetChanged();
                    }
                    i++;
                }
            }
        }
    }

    public void updateAdapters() {
        SharedPhotoVideoAdapter sharedPhotoVideoAdapter = this.photoVideoAdapter;
        if (sharedPhotoVideoAdapter != null) {
            sharedPhotoVideoAdapter.notifyDataSetChanged();
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
        StoriesAdapter storiesAdapter = this.storiesAdapter;
        if (storiesAdapter != null) {
            storiesAdapter.notifyDataSetChanged();
        }
    }

    private void updateRowsSelection(boolean z) {
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i >= mediaPageArr.length) {
                return;
            }
            int childCount = mediaPageArr[i].listView.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = this.mediaPages[i].listView.getChildAt(i2);
                if (childAt instanceof SharedDocumentCell) {
                    ((SharedDocumentCell) childAt).setChecked(false, z);
                } else if (childAt instanceof SharedPhotoVideoCell2) {
                    ((SharedPhotoVideoCell2) childAt).setChecked(false, z);
                } else if (childAt instanceof SharedLinkCell) {
                    ((SharedLinkCell) childAt).setChecked(false, z);
                } else if (childAt instanceof SharedAudioCell) {
                    ((SharedAudioCell) childAt).setChecked(false, z);
                } else if (childAt instanceof ContextLinkCell) {
                    ((ContextLinkCell) childAt).setChecked(false, z);
                }
            }
            i++;
        }
    }

    public void setMergeDialogId(long j) {
        this.mergeDialogId = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x011b, code lost:
        r5 = !r16.channelRecommendationsAdapter.chats.isEmpty();
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x012c, code lost:
        if (r5 == r16.scrollSlidingTextTabStrip.hasTab(10)) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x012e, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00cc, code lost:
        if ((r16.hasMedia[4] <= 0) == r16.scrollSlidingTextTabStrip.hasTab(4)) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00de, code lost:
        if ((r16.hasMedia[4] <= 0) == r16.scrollSlidingTextTabStrip.hasTab(4)) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x00e0, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x00e6, code lost:
        if (r16.hasMedia[2] > 0) goto L186;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x00e8, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x00ea, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x00f1, code lost:
        if (r5 != r16.scrollSlidingTextTabStrip.hasTab(2)) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x00f3, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x00f9, code lost:
        if (r16.hasMedia[5] > 0) goto L185;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x00fb, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x00fd, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0104, code lost:
        if (r5 != r16.scrollSlidingTextTabStrip.hasTab(5)) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0106, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x010c, code lost:
        if (r16.hasMedia[6] > 0) goto L184;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x010e, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0110, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0117, code lost:
        if (r5 != r16.scrollSlidingTextTabStrip.hasTab(6)) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0119, code lost:
        r3 = r3 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateTabs(boolean z) {
        TLRPC$UserFull tLRPC$UserFull;
        TLRPC$ChatFull tLRPC$ChatFull;
        boolean z2;
        TLRPC$UserFull tLRPC$UserFull2;
        TLRPC$ChatFull tLRPC$ChatFull2;
        if (this.scrollSlidingTextTabStrip == null) {
            return;
        }
        boolean z3 = !this.delegate.isFragmentOpened() ? false : z;
        int i = ((DialogObject.isUserDialog(this.dialog_id) || DialogObject.isChatDialog(this.dialog_id)) && !DialogObject.isEncryptedDialog(this.dialog_id) && ((((tLRPC$UserFull = this.userInfo) != null && tLRPC$UserFull.stories_pinned_available) || (((tLRPC$ChatFull = this.info) != null && tLRPC$ChatFull.stories_pinned_available) || isStoriesView())) && includeStories())) != this.scrollSlidingTextTabStrip.hasTab(8) ? 1 : 0;
        if (isStoriesView()) {
            z2 = false;
        } else {
            if ((this.chatUsersAdapter.chatInfo == null) == this.scrollSlidingTextTabStrip.hasTab(7)) {
                i++;
            }
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
        if (i > 0) {
            if (z3 && Build.VERSION.SDK_INT >= 19) {
                TransitionSet transitionSet = new TransitionSet();
                transitionSet.setOrdering(0);
                transitionSet.addTransition(new ChangeBounds());
                transitionSet.addTransition(new Visibility(this) { // from class: org.telegram.ui.Components.SharedMediaLayout.35
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
                });
                transitionSet.setDuration(200L);
                TransitionManager.beginDelayedTransition(this.scrollSlidingTextTabStrip.getTabsContainer(), transitionSet);
                this.scrollSlidingTextTabStrip.recordIndicatorParams();
            }
            SparseArray<View> removeTabs = this.scrollSlidingTextTabStrip.removeTabs();
            if (i > 3) {
                removeTabs = null;
            }
            if ((DialogObject.isUserDialog(this.dialog_id) || DialogObject.isChatDialog(this.dialog_id)) && !DialogObject.isEncryptedDialog(this.dialog_id) && ((((tLRPC$UserFull2 = this.userInfo) != null && tLRPC$UserFull2.stories_pinned_available) || (((tLRPC$ChatFull2 = this.info) != null && tLRPC$ChatFull2.stories_pinned_available) || isStoriesView())) && includeStories())) {
                if (isArchivedOnlyStoriesView()) {
                    if (!this.scrollSlidingTextTabStrip.hasTab(9)) {
                        this.scrollSlidingTextTabStrip.addTextTab(9, LocaleController.getString("ProfileStories", R.string.ProfileStories), removeTabs);
                    }
                    this.scrollSlidingTextTabStrip.animationDuration = 420L;
                } else {
                    if (!this.scrollSlidingTextTabStrip.hasTab(8)) {
                        this.scrollSlidingTextTabStrip.addTextTab(8, LocaleController.getString("ProfileStories", R.string.ProfileStories), removeTabs);
                    }
                    if (isStoriesView()) {
                        if (!this.scrollSlidingTextTabStrip.hasTab(9)) {
                            this.scrollSlidingTextTabStrip.addTextTab(9, LocaleController.getString("ProfileStories", R.string.ProfileStories), removeTabs);
                        }
                        this.scrollSlidingTextTabStrip.animationDuration = 420L;
                    }
                }
            }
            if (!isStoriesView()) {
                if (this.chatUsersAdapter.chatInfo != null && !this.scrollSlidingTextTabStrip.hasTab(7)) {
                    this.scrollSlidingTextTabStrip.addTextTab(7, LocaleController.getString("GroupMembers", R.string.GroupMembers), removeTabs);
                }
                if (this.hasMedia[0] > 0 && !this.scrollSlidingTextTabStrip.hasTab(0)) {
                    int[] iArr = this.hasMedia;
                    if (iArr[1] == 0 && iArr[2] == 0 && iArr[3] == 0 && iArr[4] == 0 && iArr[5] == 0 && iArr[6] == 0 && this.chatUsersAdapter.chatInfo == null) {
                        this.scrollSlidingTextTabStrip.addTextTab(0, LocaleController.getString("SharedMediaTabFull2", R.string.SharedMediaTabFull2), removeTabs);
                    } else {
                        this.scrollSlidingTextTabStrip.addTextTab(0, LocaleController.getString("SharedMediaTab2", R.string.SharedMediaTab2), removeTabs);
                    }
                }
                if (this.hasMedia[1] > 0 && !this.scrollSlidingTextTabStrip.hasTab(1)) {
                    this.scrollSlidingTextTabStrip.addTextTab(1, LocaleController.getString("SharedFilesTab2", R.string.SharedFilesTab2), removeTabs);
                }
                if (!DialogObject.isEncryptedDialog(this.dialog_id)) {
                    if (this.hasMedia[3] > 0 && !this.scrollSlidingTextTabStrip.hasTab(3)) {
                        this.scrollSlidingTextTabStrip.addTextTab(3, LocaleController.getString("SharedLinksTab2", R.string.SharedLinksTab2), removeTabs);
                    }
                    if (this.hasMedia[4] > 0 && !this.scrollSlidingTextTabStrip.hasTab(4)) {
                        this.scrollSlidingTextTabStrip.addTextTab(4, LocaleController.getString("SharedMusicTab2", R.string.SharedMusicTab2), removeTabs);
                    }
                } else if (this.hasMedia[4] > 0 && !this.scrollSlidingTextTabStrip.hasTab(4)) {
                    this.scrollSlidingTextTabStrip.addTextTab(4, LocaleController.getString("SharedMusicTab2", R.string.SharedMusicTab2), removeTabs);
                }
                if (this.hasMedia[2] > 0 && !this.scrollSlidingTextTabStrip.hasTab(2)) {
                    this.scrollSlidingTextTabStrip.addTextTab(2, LocaleController.getString("SharedVoiceTab2", R.string.SharedVoiceTab2), removeTabs);
                }
                if (this.hasMedia[5] > 0 && !this.scrollSlidingTextTabStrip.hasTab(5)) {
                    this.scrollSlidingTextTabStrip.addTextTab(5, LocaleController.getString("SharedGIFsTab2", R.string.SharedGIFsTab2), removeTabs);
                }
                if (this.hasMedia[6] > 0 && !this.scrollSlidingTextTabStrip.hasTab(6)) {
                    this.scrollSlidingTextTabStrip.addTextTab(6, LocaleController.getString("SharedGroupsTab2", R.string.SharedGroupsTab2), removeTabs);
                }
                if (z2 && !this.scrollSlidingTextTabStrip.hasTab(10)) {
                    this.scrollSlidingTextTabStrip.addTextTab(10, LocaleController.getString(R.string.SimilarChannelsTab), removeTabs);
                }
            }
        }
        int currentTabId = this.scrollSlidingTextTabStrip.getCurrentTabId();
        if (currentTabId >= 0) {
            this.mediaPages[0].selectedType = currentTabId;
        }
        this.scrollSlidingTextTabStrip.finishAddingTabs();
        onSelectedTabChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startStopVisibleGifs() {
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i >= mediaPageArr.length) {
                return;
            }
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
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x057e, code lost:
        if (r5.getCount() > 0) goto L181;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x0580, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x05c1, code lost:
        if (r5.getCount() > 0) goto L181;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void switchToCurrentSelectedMode(boolean z) {
        MediaPage[] mediaPageArr;
        int i;
        boolean z2;
        RecyclerView.RecycledViewPool recycledViewPool;
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
        RecyclerView.Adapter adapter = this.mediaPages[z ? 1 : 0].listView.getAdapter();
        RecyclerView.RecycledViewPool recycledViewPool2 = null;
        if (this.searching && this.searchWas) {
            if (z) {
                if (this.mediaPages[z ? 1 : 0].selectedType == 0 || this.mediaPages[z ? 1 : 0].selectedType == 2 || this.mediaPages[z ? 1 : 0].selectedType == 5 || this.mediaPages[z ? 1 : 0].selectedType == 6 || (this.mediaPages[z ? 1 : 0].selectedType == 7 && !this.delegate.canSearchMembers())) {
                    this.searching = false;
                    this.searchWas = false;
                    switchToCurrentSelectedMode(true);
                    return;
                }
                String obj = this.searchItem.getSearchField().getText().toString();
                if (this.mediaPages[z ? 1 : 0].selectedType != 1) {
                    if (this.mediaPages[z ? 1 : 0].selectedType != 3) {
                        if (this.mediaPages[z ? 1 : 0].selectedType != 4) {
                            if (this.mediaPages[z ? 1 : 0].selectedType == 7 && (groupUsersSearchAdapter = this.groupUsersSearchAdapter) != null) {
                                groupUsersSearchAdapter.search(obj, false);
                                if (adapter != this.groupUsersSearchAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z ? 1 : 0].listView.setAdapter(this.groupUsersSearchAdapter);
                                }
                            }
                        } else {
                            MediaSearchAdapter mediaSearchAdapter = this.audioSearchAdapter;
                            if (mediaSearchAdapter != null) {
                                mediaSearchAdapter.search(obj, false);
                                if (adapter != this.audioSearchAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z ? 1 : 0].listView.setAdapter(this.audioSearchAdapter);
                                }
                            }
                        }
                    } else {
                        MediaSearchAdapter mediaSearchAdapter2 = this.linksSearchAdapter;
                        if (mediaSearchAdapter2 != null) {
                            mediaSearchAdapter2.search(obj, false);
                            if (adapter != this.linksSearchAdapter) {
                                recycleAdapter(adapter);
                                this.mediaPages[z ? 1 : 0].listView.setAdapter(this.linksSearchAdapter);
                            }
                        }
                    }
                } else {
                    MediaSearchAdapter mediaSearchAdapter3 = this.documentsSearchAdapter;
                    if (mediaSearchAdapter3 != null) {
                        mediaSearchAdapter3.search(obj, false);
                        if (adapter != this.documentsSearchAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z ? 1 : 0].listView.setAdapter(this.documentsSearchAdapter);
                        }
                    }
                }
            } else if (this.mediaPages[z ? 1 : 0].listView != null) {
                if (this.mediaPages[z ? 1 : 0].selectedType != 1) {
                    if (this.mediaPages[z ? 1 : 0].selectedType != 3) {
                        if (this.mediaPages[z ? 1 : 0].selectedType != 4) {
                            if (this.mediaPages[z ? 1 : 0].selectedType == 7) {
                                if (adapter != this.groupUsersSearchAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z ? 1 : 0].listView.setAdapter(this.groupUsersSearchAdapter);
                                }
                                this.groupUsersSearchAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (adapter != this.audioSearchAdapter) {
                                recycleAdapter(adapter);
                                this.mediaPages[z ? 1 : 0].listView.setAdapter(this.audioSearchAdapter);
                            }
                            this.audioSearchAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (adapter != this.linksSearchAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z ? 1 : 0].listView.setAdapter(this.linksSearchAdapter);
                        }
                        this.linksSearchAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (adapter != this.documentsSearchAdapter) {
                        recycleAdapter(adapter);
                        this.mediaPages[z ? 1 : 0].listView.setAdapter(this.documentsSearchAdapter);
                    }
                    this.documentsSearchAdapter.notifyDataSetChanged();
                }
            }
            z2 = false;
            i = 100;
        } else {
            this.mediaPages[z ? 1 : 0].listView.setPinnedHeaderShadowDrawable(null);
            InternalListView internalListView = this.mediaPages[z ? 1 : 0].listView;
            int paddingLeft = this.mediaPages[z ? 1 : 0].listView.getPaddingLeft();
            int dp = AndroidUtilities.dp(2.0f);
            InternalListView internalListView2 = this.mediaPages[z ? 1 : 0].listView;
            int dp2 = this.mediaPages[z ? 1 : 0].selectedType == 9 ? AndroidUtilities.dp(64.0f) : 0;
            internalListView2.hintPaddingTop = dp2;
            internalListView.setPadding(paddingLeft, dp + dp2, this.mediaPages[z ? 1 : 0].listView.getPaddingRight(), this.mediaPages[z ? 1 : 0].listView.getPaddingBottom());
            if (this.mediaPages[z ? 1 : 0].selectedType != 0) {
                if (this.mediaPages[z ? 1 : 0].selectedType != 1) {
                    if (this.mediaPages[z ? 1 : 0].selectedType != 2) {
                        if (this.mediaPages[z ? 1 : 0].selectedType != 3) {
                            if (this.mediaPages[z ? 1 : 0].selectedType != 4) {
                                if (this.mediaPages[z ? 1 : 0].selectedType != 5) {
                                    if (this.mediaPages[z ? 1 : 0].selectedType != 6) {
                                        if (this.mediaPages[z ? 1 : 0].selectedType != 7) {
                                            if (this.mediaPages[z ? 1 : 0].selectedType != 8) {
                                                if (this.mediaPages[z ? 1 : 0].selectedType != 9) {
                                                    if (this.mediaPages[z ? 1 : 0].selectedType == 10 && adapter != this.channelRecommendationsAdapter) {
                                                        recycleAdapter(adapter);
                                                        this.mediaPages[z ? 1 : 0].listView.setAdapter(this.channelRecommendationsAdapter);
                                                    }
                                                } else {
                                                    if (adapter != this.archivedStoriesAdapter) {
                                                        recycleAdapter(adapter);
                                                        this.mediaPages[z ? 1 : 0].listView.setAdapter(this.archivedStoriesAdapter);
                                                    }
                                                    i = this.mediaColumnsCount[1];
                                                }
                                            } else {
                                                if (adapter != this.storiesAdapter) {
                                                    recycleAdapter(adapter);
                                                    this.mediaPages[z ? 1 : 0].listView.setAdapter(this.storiesAdapter);
                                                }
                                                i = this.mediaColumnsCount[1];
                                            }
                                            z2 = false;
                                            recycledViewPool = null;
                                        } else if (adapter != this.chatUsersAdapter) {
                                            recycleAdapter(adapter);
                                            this.mediaPages[z ? 1 : 0].listView.setAdapter(this.chatUsersAdapter);
                                        }
                                    } else if (adapter != this.commonGroupsAdapter) {
                                        recycleAdapter(adapter);
                                        this.mediaPages[z ? 1 : 0].listView.setAdapter(this.commonGroupsAdapter);
                                    }
                                } else if (adapter != this.gifAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z ? 1 : 0].listView.setAdapter(this.gifAdapter);
                                }
                            } else {
                                SharedMediaData[] sharedMediaDataArr = this.sharedMediaData;
                                z2 = sharedMediaDataArr[4].fastScrollDataLoaded && !sharedMediaDataArr[4].fastScrollPeriods.isEmpty();
                                if (adapter != this.audioAdapter) {
                                    recycleAdapter(adapter);
                                    this.mediaPages[z ? 1 : 0].listView.setAdapter(this.audioAdapter);
                                }
                            }
                        } else if (adapter != this.linksAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z ? 1 : 0].listView.setAdapter(this.linksAdapter);
                        }
                        z2 = false;
                    } else {
                        SharedMediaData[] sharedMediaDataArr2 = this.sharedMediaData;
                        z2 = sharedMediaDataArr2[2].fastScrollDataLoaded && !sharedMediaDataArr2[2].fastScrollPeriods.isEmpty();
                        if (adapter != this.voiceAdapter) {
                            recycleAdapter(adapter);
                            this.mediaPages[z ? 1 : 0].listView.setAdapter(this.voiceAdapter);
                        }
                    }
                } else {
                    SharedMediaData[] sharedMediaDataArr3 = this.sharedMediaData;
                    z2 = sharedMediaDataArr3[1].fastScrollDataLoaded && !sharedMediaDataArr3[1].fastScrollPeriods.isEmpty();
                    if (adapter != this.documentsAdapter) {
                        recycleAdapter(adapter);
                        this.mediaPages[z ? 1 : 0].listView.setAdapter(this.documentsAdapter);
                    }
                }
                i = 100;
                recycledViewPool = null;
            } else {
                if (adapter != this.photoVideoAdapter) {
                    recycleAdapter(adapter);
                    this.mediaPages[z ? 1 : 0].listView.setAdapter(this.photoVideoAdapter);
                }
                int i4 = -AndroidUtilities.dp(1.0f);
                layoutParams.rightMargin = i4;
                layoutParams.leftMargin = i4;
                SharedMediaData[] sharedMediaDataArr4 = this.sharedMediaData;
                z2 = sharedMediaDataArr4[0].fastScrollDataLoaded && !sharedMediaDataArr4[0].fastScrollPeriods.isEmpty();
                i = this.mediaColumnsCount[0];
                this.mediaPages[z ? 1 : 0].listView.setPinnedHeaderShadowDrawable(this.pinnedHeaderShadowDrawable);
                SharedMediaData[] sharedMediaDataArr5 = this.sharedMediaData;
                if (sharedMediaDataArr5[0].recycledViewPool == null) {
                    sharedMediaDataArr5[0].recycledViewPool = new RecyclerView.RecycledViewPool();
                }
                recycledViewPool = this.sharedMediaData[0].recycledViewPool;
            }
            if (this.mediaPages[z ? 1 : 0].selectedType == 0 || this.mediaPages[z ? 1 : 0].selectedType == 8 || this.mediaPages[z ? 1 : 0].selectedType == 9 || this.mediaPages[z ? 1 : 0].selectedType == 2 || this.mediaPages[z ? 1 : 0].selectedType == 5 || this.mediaPages[z ? 1 : 0].selectedType == 6 || ((this.mediaPages[z ? 1 : 0].selectedType == 7 && !this.delegate.canSearchMembers()) || this.mediaPages[z ? 1 : 0].selectedType == 10)) {
                if (z) {
                    this.searchItemState = 2;
                } else {
                    this.searchItemState = 0;
                    this.searchItem.setVisibility(isStoriesView() ? 8 : 4);
                }
            } else if (z) {
                if (this.searchItem.getVisibility() == 4 && !this.actionBar.isSearchFieldVisible()) {
                    if (canShowSearchItem()) {
                        this.searchItemState = 1;
                        this.searchItem.setVisibility(0);
                    } else {
                        this.searchItem.setVisibility(isStoriesView() ? 8 : 4);
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
                    this.searchItem.setVisibility(isStoriesView() ? 8 : 4);
                    this.searchItem.setAlpha(0.0f);
                }
            }
            if (this.mediaPages[z ? 1 : 0].selectedType != 6) {
                if (this.mediaPages[z ? 1 : 0].selectedType != 7) {
                    if (this.mediaPages[z ? 1 : 0].selectedType != 8) {
                        if (this.mediaPages[z ? 1 : 0].selectedType != 9) {
                            if (this.mediaPages[z ? 1 : 0].selectedType != 10 && !this.sharedMediaData[this.mediaPages[z ? 1 : 0].selectedType].loading && !this.sharedMediaData[this.mediaPages[z ? 1 : 0].selectedType].endReached[0] && this.sharedMediaData[this.mediaPages[z ? 1 : 0].selectedType].messages.isEmpty()) {
                                this.sharedMediaData[this.mediaPages[z ? 1 : 0].selectedType].loading = true;
                                this.documentsAdapter.notifyDataSetChanged();
                                int i5 = this.mediaPages[z ? 1 : 0].selectedType;
                                if (i5 == 0) {
                                    SharedMediaData[] sharedMediaDataArr6 = this.sharedMediaData;
                                    if (sharedMediaDataArr6[0].filterType == 1) {
                                        i2 = 6;
                                    } else if (sharedMediaDataArr6[0].filterType == 2) {
                                        i2 = 7;
                                    }
                                    this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, 0, 0, i2, this.topicId, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[this.mediaPages[z ? 1 : 0].selectedType].requestIndex);
                                }
                                i2 = i5;
                                this.profileActivity.getMediaDataController().loadMedia(this.dialog_id, 50, 0, 0, i2, this.topicId, 1, this.profileActivity.getClassGuid(), this.sharedMediaData[this.mediaPages[z ? 1 : 0].selectedType].requestIndex);
                            }
                        } else {
                            StoriesAdapter storiesAdapter = this.archivedStoriesAdapter;
                            StoriesController.StoriesList storiesList = storiesAdapter.storiesList;
                            storiesAdapter.load(false);
                            this.mediaPages[z ? 1 : 0].emptyView.showProgress(storiesList != null && (storiesList.isLoading() || (hasInternet() && storiesList.getCount() > 0)), z);
                            if (storiesList != null) {
                            }
                            z2 = false;
                        }
                    } else {
                        StoriesAdapter storiesAdapter2 = this.storiesAdapter;
                        StoriesController.StoriesList storiesList2 = storiesAdapter2.storiesList;
                        storiesAdapter2.load(false);
                        this.mediaPages[z ? 1 : 0].emptyView.showProgress(storiesList2 != null && (storiesList2.isLoading() || (hasInternet() && storiesList2.getCount() > 0)), z);
                        if (storiesList2 != null) {
                        }
                        z2 = false;
                    }
                }
            } else if (!this.commonGroupsAdapter.loading && !this.commonGroupsAdapter.endReached && this.commonGroupsAdapter.chats.isEmpty()) {
                this.commonGroupsAdapter.getChats(0L, 100);
            }
            if (this.mediaPages[z ? 1 : 0].selectedType == 8) {
                this.mediaPages[z ? 1 : 0].emptyView.setStickerType(11);
                this.mediaPages[z ? 1 : 0].emptyView.title.setText(LocaleController.getString(isStoriesView() ? R.string.NoPublicStoriesTitle : R.string.NoStoriesTitle));
                this.mediaPages[z ? 1 : 0].emptyView.subtitle.setText(isStoriesView() ? LocaleController.getString("NoStoriesSubtitle") : "");
            } else if (this.mediaPages[z ? 1 : 0].selectedType == 9) {
                this.mediaPages[z ? 1 : 0].emptyView.setStickerType(11);
                this.mediaPages[z ? 1 : 0].emptyView.title.setText(LocaleController.getString("NoArchivedStoriesTitle"));
                this.mediaPages[z ? 1 : 0].emptyView.subtitle.setText(isStoriesView() ? LocaleController.getString("NoArchivedStoriesSubtitle") : "");
            } else {
                this.mediaPages[z ? 1 : 0].emptyView.setStickerType(1);
                this.mediaPages[z ? 1 : 0].emptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
                this.mediaPages[z ? 1 : 0].emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", R.string.SearchEmptyViewFilteredSubtitle2));
            }
            this.mediaPages[z ? 1 : 0].listView.setVisibility(0);
            recycledViewPool2 = recycledViewPool;
        }
        MediaPage[] mediaPageArr2 = this.mediaPages;
        mediaPageArr2[z ? 1 : 0].fastScrollEnabled = z2;
        updateFastScrollVisibility(mediaPageArr2[z ? 1 : 0], false);
        this.mediaPages[z ? 1 : 0].layoutManager.setSpanCount(i);
        this.mediaPages[z ? 1 : 0].listView.invalidateItemDecorations();
        this.mediaPages[z ? 1 : 0].listView.setRecycledViewPool(recycledViewPool2);
        this.mediaPages[z ? 1 : 0].animationSupportingListView.setRecycledViewPool(recycledViewPool2);
        if (this.searchItemState == 2 && this.actionBar.isSearchFieldVisible()) {
            this.ignoreSearchCollapse = true;
            this.actionBar.closeSearchField();
            this.searchItemState = 0;
            this.searchItem.setAlpha(0.0f);
            this.searchItem.setVisibility(isStoriesView() ? 8 : 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onItemLongClick(MessageObject messageObject, View view, int i) {
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
        onActionModeSelectedUpdate(this.selectedFiles[0]);
        updateForwardItem();
        return true;
    }

    private void onItemClick(int i, View view, MessageObject messageObject, int i2, int i3) {
        if (messageObject == null || this.photoVideoChangeColumnsAnimation) {
            return;
        }
        String str = null;
        if (this.isActionModeShowed) {
            if (i3 == 8 && !isStoriesView()) {
                return;
            }
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
            onActionModeSelectedUpdate(this.selectedFiles[0]);
            if (this.selectedFiles[0].size() == 0 && this.selectedFiles[1].size() == 0) {
                showActionMode(false);
            } else {
                this.selectedMessagesCountTextView.setNumber(this.selectedFiles[0].size() + this.selectedFiles[1].size(), true);
                this.deleteItem.setVisibility(this.cantDeleteMessagesCount == 0 ? 0 : 8);
                ActionBarMenuItem actionBarMenuItem = this.gotoItem;
                if (actionBarMenuItem != null) {
                    actionBarMenuItem.setVisibility(this.selectedFiles[0].size() == 1 ? 0 : 8);
                }
            }
            this.scrolling = false;
            if (view instanceof SharedDocumentCell) {
                ((SharedDocumentCell) view).setChecked(this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0, true);
            } else if (view instanceof SharedPhotoVideoCell) {
                ((SharedPhotoVideoCell) view).setChecked(i2, this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0, true);
            } else if (view instanceof SharedLinkCell) {
                ((SharedLinkCell) view).setChecked(this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0, true);
            } else if (view instanceof SharedAudioCell) {
                ((SharedAudioCell) view).setChecked(this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0, true);
            } else if (view instanceof ContextLinkCell) {
                ((ContextLinkCell) view).setChecked(this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0, true);
            } else if (view instanceof SharedPhotoVideoCell2) {
                ((SharedPhotoVideoCell2) view).setChecked(this.selectedFiles[c].indexOfKey(messageObject.getId()) >= 0, true);
            }
        } else if (i3 == 0) {
            int i4 = i - this.sharedMediaData[i3].startOffset;
            if (i4 >= 0 && i4 < this.sharedMediaData[i3].messages.size()) {
                PhotoViewer.getInstance().setParentActivity(this.profileActivity);
                PhotoViewer.getInstance().openPhoto(this.sharedMediaData[i3].messages, i4, this.dialog_id, this.mergeDialogId, this.topicId, this.provider);
            }
        } else if (i3 == 2 || i3 == 4) {
            if (view instanceof SharedAudioCell) {
                ((SharedAudioCell) view).didPressedButton();
            }
        } else if (i3 == 5) {
            PhotoViewer.getInstance().setParentActivity(this.profileActivity);
            int indexOf = this.sharedMediaData[i3].messages.indexOf(messageObject);
            if (indexOf < 0) {
                ArrayList<MessageObject> arrayList = new ArrayList<>();
                arrayList.add(messageObject);
                PhotoViewer.getInstance().openPhoto(arrayList, 0, 0L, 0L, 0, this.provider);
            } else {
                PhotoViewer.getInstance().openPhoto(this.sharedMediaData[i3].messages, indexOf, this.dialog_id, this.mergeDialogId, this.topicId, this.provider);
            }
        } else if (i3 == 1) {
            if (view instanceof SharedDocumentCell) {
                SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) view;
                TLRPC$Document document = messageObject.getDocument();
                if (sharedDocumentCell.isLoaded()) {
                    if (messageObject.canPreviewDocument()) {
                        PhotoViewer.getInstance().setParentActivity(this.profileActivity);
                        int indexOf2 = this.sharedMediaData[i3].messages.indexOf(messageObject);
                        if (indexOf2 < 0) {
                            ArrayList<MessageObject> arrayList2 = new ArrayList<>();
                            arrayList2.add(messageObject);
                            PhotoViewer.getInstance().openPhoto(arrayList2, 0, 0L, 0L, 0, this.provider);
                            return;
                        }
                        PhotoViewer.getInstance().openPhoto(this.sharedMediaData[i3].messages, indexOf2, this.dialog_id, this.mergeDialogId, this.topicId, this.provider);
                        return;
                    }
                    AndroidUtilities.openDocument(messageObject, this.profileActivity.getParentActivity(), this.profileActivity);
                } else if (!sharedDocumentCell.isLoading()) {
                    MessageObject message = sharedDocumentCell.getMessage();
                    message.putInDownloadsStore = true;
                    this.profileActivity.getFileLoader().loadFile(document, message, 0, 0);
                    sharedDocumentCell.updateFileExistIcon(true);
                } else {
                    this.profileActivity.getFileLoader().cancelLoadFile(document);
                    sharedDocumentCell.updateFileExistIcon(true);
                }
            }
        } else if (i3 == 3) {
            try {
                TLRPC$WebPage tLRPC$WebPage = MessageObject.getMedia(messageObject.messageOwner) != null ? MessageObject.getMedia(messageObject.messageOwner).webpage : null;
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
        } else if (i3 == 8 || i3 == 9) {
            final StoriesController.StoriesList storiesList = (i3 == 8 ? this.storiesAdapter : this.archivedStoriesAdapter).storiesList;
            if (storiesList == null) {
                return;
            }
            this.profileActivity.getOrCreateStoryViewer().open(getContext(), messageObject.getId(), storiesList, StoriesListPlaceProvider.of(this.mediaPages[i2].listView).with(new StoriesListPlaceProvider.LoadNextInterface() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda19
                @Override // org.telegram.ui.Stories.StoriesListPlaceProvider.LoadNextInterface
                public final void loadNext(boolean z) {
                    SharedMediaLayout.lambda$onItemClick$18(StoriesController.StoriesList.this, z);
                }
            }));
        }
        updateForwardItem();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onItemClick$18(StoriesController.StoriesList storiesList, boolean z) {
        if (z) {
            storiesList.load(false, 30);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openUrl(String str) {
        if (AndroidUtilities.shouldShowUrlInAlert(str)) {
            AlertsCreator.showOpenUrlAlert(this.profileActivity, str, true, true);
        } else {
            Browser.openUrl(this.profileActivity.getParentActivity(), str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openWebView(TLRPC$WebPage tLRPC$WebPage, MessageObject messageObject) {
        EmbedBottomSheet.show(this.profileActivity, messageObject, this.provider, tLRPC$WebPage.site_name, tLRPC$WebPage.description, tLRPC$WebPage.url, tLRPC$WebPage.embed_url, tLRPC$WebPage.embed_width, tLRPC$WebPage.embed_height, false);
    }

    private void recycleAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof SharedPhotoVideoAdapter) {
            this.cellCache.addAll(this.cache);
            this.cache.clear();
        } else if (adapter == this.audioAdapter) {
            this.audioCellCache.addAll(this.audioCache);
            this.audioCache.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 36 implements SharedLinkCell.SharedLinkCellDelegate {
        36() {
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
        public void onLinkPress(final String str, boolean z) {
            if (!z) {
                SharedMediaLayout.this.openUrl(str);
                return;
            }
            BottomSheet.Builder builder = new BottomSheet.Builder(SharedMediaLayout.this.profileActivity.getParentActivity());
            builder.setTitle(str);
            builder.setItems(new CharSequence[]{LocaleController.getString("Open", R.string.Open), LocaleController.getString("Copy", R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$36$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SharedMediaLayout.36.this.lambda$onLinkPress$0(str, dialogInterface, i);
                }
            });
            SharedMediaLayout.this.profileActivity.showDialog(builder.create());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLinkPress$0(String str, DialogInterface dialogInterface, int i) {
            if (i == 0) {
                SharedMediaLayout.this.openUrl(str);
            } else if (i == 1) {
                if (str.startsWith("mailto:")) {
                    str = str.substring(7);
                } else if (str.startsWith("tel:")) {
                    str = str.substring(4);
                }
                AndroidUtilities.addToClipboard(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
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
            if ((SharedMediaLayout.this.sharedMediaData[3].sections.size() != 0 || SharedMediaLayout.this.sharedMediaData[3].loading) && i < SharedMediaLayout.this.sharedMediaData[3].sections.size()) {
                return SharedMediaLayout.this.sharedMediaData[3].sectionArrays.get(SharedMediaLayout.this.sharedMediaData[3].sections.get(i)).size() + (i == 0 ? 0 : 1);
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public View getSectionHeaderView(int i, View view) {
            if (view == null) {
                view = new GraySectionCell(this.mContext);
                view.setBackgroundColor(SharedMediaLayout.this.getThemedColor(Theme.key_graySection) & (-218103809));
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
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                if (sharedMediaLayout.isActionModeShowed) {
                    sharedLinkCell.setChecked(sharedMediaLayout.selectedFiles[(messageObject.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0, !SharedMediaLayout.this.scrolling);
                } else {
                    sharedLinkCell.setChecked(false, !sharedMediaLayout.scrolling);
                }
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getItemViewType(int i, int i2) {
            if (SharedMediaLayout.this.sharedMediaData[3].sections.size() != 0 || SharedMediaLayout.this.sharedMediaData[3].loading) {
                if (i < SharedMediaLayout.this.sharedMediaData[3].sections.size()) {
                    return (i == 0 || i2 != 0) ? 1 : 0;
                }
                return 2;
            }
            return 3;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            iArr[0] = 0;
            iArr[1] = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SharedDocumentsAdapter extends RecyclerListView.FastScrollAdapter {
        private int currentType;
        private boolean inFastScrollMode;
        private Context mContext;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public SharedDocumentsAdapter(Context context, int i) {
            this.mContext = context;
            this.currentType = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].loadingAfterFastScroll) {
                return SharedMediaLayout.this.sharedMediaData[this.currentType].totalCount;
            }
            if (SharedMediaLayout.this.sharedMediaData[this.currentType].messages.size() != 0 || SharedMediaLayout.this.sharedMediaData[this.currentType].loading) {
                if (SharedMediaLayout.this.sharedMediaData[this.currentType].messages.size() != 0 || ((SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[0] && SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[1]) || !SharedMediaLayout.this.sharedMediaData[this.currentType].startReached)) {
                    if (SharedMediaLayout.this.sharedMediaData[this.currentType].totalCount == 0) {
                        int startOffset = SharedMediaLayout.this.sharedMediaData[this.currentType].getStartOffset() + SharedMediaLayout.this.sharedMediaData[this.currentType].getMessages().size();
                        return startOffset != 0 ? (SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[0] && SharedMediaLayout.this.sharedMediaData[this.currentType].endReached[1]) ? startOffset : SharedMediaLayout.this.sharedMediaData[this.currentType].getEndLoadingStubs() != 0 ? startOffset + SharedMediaLayout.this.sharedMediaData[this.currentType].getEndLoadingStubs() : startOffset + 1 : startOffset;
                    }
                    return SharedMediaLayout.this.sharedMediaData[this.currentType].totalCount;
                }
                return 0;
            }
            return 1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            if (i == 1) {
                SharedDocumentCell sharedDocumentCell = new SharedDocumentCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
                sharedDocumentCell.setGlobalGradientView(SharedMediaLayout.this.globalGradientView);
                view = sharedDocumentCell;
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
                view = flickerLoadingView;
            } else if (i == 4) {
                View createEmptyStubView = SharedMediaLayout.createEmptyStubView(this.mContext, this.currentType, SharedMediaLayout.this.dialog_id, SharedMediaLayout.this.resourcesProvider);
                createEmptyStubView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                return new RecyclerListView.Holder(createEmptyStubView);
            } else {
                if (this.currentType == 4 && !SharedMediaLayout.this.audioCellCache.isEmpty()) {
                    View view3 = (View) SharedMediaLayout.this.audioCellCache.get(0);
                    SharedMediaLayout.this.audioCellCache.remove(0);
                    ViewGroup viewGroup2 = (ViewGroup) view3.getParent();
                    view2 = view3;
                    if (viewGroup2 != null) {
                        viewGroup2.removeView(view3);
                        view2 = view3;
                    }
                } else {
                    view2 = new SharedAudioCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider) { // from class: org.telegram.ui.Components.SharedMediaLayout.SharedDocumentsAdapter.1
                        @Override // org.telegram.ui.Cells.SharedAudioCell
                        public boolean needPlayMessage(MessageObject messageObject) {
                            if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                                boolean playMessage = MediaController.getInstance().playMessage(messageObject);
                                MediaController.getInstance().setVoiceMessagesPlaylist(playMessage ? SharedMediaLayout.this.sharedMediaData[SharedDocumentsAdapter.this.currentType].messages : null, false);
                                return playMessage;
                            } else if (messageObject.isMusic()) {
                                return MediaController.getInstance().setPlaylist(SharedMediaLayout.this.sharedMediaData[SharedDocumentsAdapter.this.currentType].messages, messageObject, SharedMediaLayout.this.mergeDialogId);
                            } else {
                                return false;
                            }
                        }
                    };
                }
                SharedAudioCell sharedAudioCell = (SharedAudioCell) view2;
                sharedAudioCell.setGlobalGradientView(SharedMediaLayout.this.globalGradientView);
                view = view2;
                if (this.currentType == 4) {
                    SharedMediaLayout.this.audioCache.add(sharedAudioCell);
                    view = view2;
                }
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ArrayList<MessageObject> arrayList = SharedMediaLayout.this.sharedMediaData[this.currentType].messages;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 1) {
                SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) viewHolder.itemView;
                MessageObject messageObject = arrayList.get(i - SharedMediaLayout.this.sharedMediaData[this.currentType].startOffset);
                sharedDocumentCell.setDocument(messageObject, i != arrayList.size() - 1);
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                if (sharedMediaLayout.isActionModeShowed) {
                    sharedDocumentCell.setChecked(sharedMediaLayout.selectedFiles[(messageObject.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0, !SharedMediaLayout.this.scrolling);
                } else {
                    sharedDocumentCell.setChecked(false, !sharedMediaLayout.scrolling);
                }
            } else if (itemViewType != 3) {
            } else {
                SharedAudioCell sharedAudioCell = (SharedAudioCell) viewHolder.itemView;
                MessageObject messageObject2 = arrayList.get(i - SharedMediaLayout.this.sharedMediaData[this.currentType].startOffset);
                sharedAudioCell.setMessageObject(messageObject2, i != arrayList.size() - 1);
                SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
                if (sharedMediaLayout2.isActionModeShowed) {
                    sharedAudioCell.setChecked(sharedMediaLayout2.selectedFiles[(messageObject2.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (messageObject2.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(messageObject2.getId()) >= 0, !SharedMediaLayout.this.scrolling);
                } else {
                    sharedAudioCell.setChecked(false, !sharedMediaLayout2.scrolling);
                }
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
                if (recyclerListView != null) {
                    int i = 0;
                    for (int i2 = 0; i2 < recyclerListView.getChildCount(); i2++) {
                        i = SharedMediaLayout.this.getMessageId(recyclerListView.getChildAt(i2));
                        if (i != 0) {
                            break;
                        }
                    }
                    if (i == 0) {
                        SharedMediaLayout.this.findPeriodAndJumpToDate(this.currentType, recyclerListView, true);
                    }
                }
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
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoMediaSecret", R.string.NoMediaSecret));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoMedia", R.string.NoMedia));
            }
        } else if (i == 1) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedFilesSecret", R.string.NoSharedFilesSecret));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedFiles", R.string.NoSharedFiles));
            }
        } else if (i == 2) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedVoiceSecret", R.string.NoSharedVoiceSecret));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedVoice", R.string.NoSharedVoice));
            }
        } else if (i == 3) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedLinksSecret", R.string.NoSharedLinksSecret));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedLinks", R.string.NoSharedLinks));
            }
        } else if (i == 4) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedAudioSecret", R.string.NoSharedAudioSecret));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedAudio", R.string.NoSharedAudio));
            }
        } else if (i == 5) {
            if (DialogObject.isEncryptedDialog(j)) {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoSharedGifSecret", R.string.NoSharedGifSecret));
            } else {
                emptyStubView.emptyTextView.setText(LocaleController.getString("NoGIFs", R.string.NoGIFs));
            }
        } else if (i == 6) {
            emptyStubView.emptyImageView.setImageDrawable(null);
            emptyStubView.emptyTextView.setText(LocaleController.getString("NoGroupsInCommon", R.string.NoGroupsInCommon));
        } else if (i == 7) {
            emptyStubView.emptyImageView.setImageDrawable(null);
            emptyStubView.emptyTextView.setText("");
        }
        return emptyStubView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
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
            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SharedPhotoVideoAdapter extends RecyclerListView.FastScrollAdapter {
        protected boolean inFastScrollMode;
        protected Context mContext;
        SharedPhotoVideoCell2.SharedResources sharedResources;

        public int getTopOffset() {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        public SharedPhotoVideoAdapter(Context context) {
            this.mContext = context;
        }

        public int getPositionForIndex(int i) {
            return SharedMediaLayout.this.sharedMediaData[0].startOffset + i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (DialogObject.isEncryptedDialog(SharedMediaLayout.this.dialog_id)) {
                if (SharedMediaLayout.this.sharedMediaData[0].messages.size() != 0 || SharedMediaLayout.this.sharedMediaData[0].loading) {
                    if (SharedMediaLayout.this.sharedMediaData[0].messages.size() != 0 || (SharedMediaLayout.this.sharedMediaData[0].endReached[0] && SharedMediaLayout.this.sharedMediaData[0].endReached[1])) {
                        int startOffset = SharedMediaLayout.this.sharedMediaData[0].getStartOffset() + SharedMediaLayout.this.sharedMediaData[0].getMessages().size();
                        return startOffset != 0 ? (SharedMediaLayout.this.sharedMediaData[0].endReached[0] && SharedMediaLayout.this.sharedMediaData[0].endReached[1]) ? startOffset : startOffset + 1 : startOffset;
                    }
                    return 0;
                }
                return 1;
            } else if (SharedMediaLayout.this.sharedMediaData[0].loadingAfterFastScroll) {
                return SharedMediaLayout.this.sharedMediaData[0].totalCount;
            } else {
                if (SharedMediaLayout.this.sharedMediaData[0].messages.size() != 0 || SharedMediaLayout.this.sharedMediaData[0].loading) {
                    if (SharedMediaLayout.this.sharedMediaData[0].messages.size() != 0 || ((SharedMediaLayout.this.sharedMediaData[0].endReached[0] && SharedMediaLayout.this.sharedMediaData[0].endReached[1]) || !SharedMediaLayout.this.sharedMediaData[0].startReached)) {
                        if (SharedMediaLayout.this.sharedMediaData[0].totalCount == 0) {
                            int startOffset2 = SharedMediaLayout.this.sharedMediaData[0].getStartOffset() + SharedMediaLayout.this.sharedMediaData[0].getMessages().size();
                            return startOffset2 != 0 ? (SharedMediaLayout.this.sharedMediaData[0].endReached[0] && SharedMediaLayout.this.sharedMediaData[0].endReached[1]) ? startOffset2 : SharedMediaLayout.this.sharedMediaData[0].getEndLoadingStubs() != 0 ? startOffset2 + SharedMediaLayout.this.sharedMediaData[0].getEndLoadingStubs() : startOffset2 + 1 : startOffset2;
                        }
                        return SharedMediaLayout.this.sharedMediaData[0].totalCount;
                    }
                    return 0;
                }
                return 1;
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
            if (this == SharedMediaLayout.this.storiesAdapter || this == SharedMediaLayout.this.archivedStoriesAdapter) {
                sharedPhotoVideoCell2.isStory = true;
            }
            sharedPhotoVideoCell2.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(sharedPhotoVideoCell2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                ArrayList<MessageObject> messages = SharedMediaLayout.this.sharedMediaData[0].getMessages();
                int startOffset = i - SharedMediaLayout.this.sharedMediaData[0].getStartOffset();
                SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) viewHolder.itemView;
                int messageId = sharedPhotoVideoCell2.getMessageId();
                int i2 = this == SharedMediaLayout.this.photoVideoAdapter ? SharedMediaLayout.this.mediaColumnsCount[0] : (this == SharedMediaLayout.this.storiesAdapter || this == SharedMediaLayout.this.archivedStoriesAdapter) ? SharedMediaLayout.this.mediaColumnsCount[1] : SharedMediaLayout.this.animateToColumnsCount;
                if (startOffset >= 0 && startOffset < messages.size()) {
                    MessageObject messageObject = messages.get(startOffset);
                    boolean z = messageObject.getId() == messageId;
                    SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                    if (sharedMediaLayout.isActionModeShowed) {
                        sharedPhotoVideoCell2.setChecked(sharedMediaLayout.selectedFiles[(messageObject.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0, z);
                    } else {
                        sharedPhotoVideoCell2.setChecked(false, z);
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
            int measuredHeight = recyclerListView.getChildAt(0).getMeasuredHeight();
            int i = (this == SharedMediaLayout.this.animationSupportingPhotoVideoAdapter || this == SharedMediaLayout.this.animationSupportingStoriesAdapter || this == SharedMediaLayout.this.animationSupportingArchivedStoriesAdapter) ? SharedMediaLayout.this.animateToColumnsCount : (this == SharedMediaLayout.this.storiesAdapter || this == SharedMediaLayout.this.archivedStoriesAdapter) ? SharedMediaLayout.this.mediaColumnsCount[1] : SharedMediaLayout.this.mediaColumnsCount[0];
            double ceil = Math.ceil(getTotalItemsCount() / i);
            double d = measuredHeight;
            Double.isNaN(d);
            int i2 = (int) (ceil * d);
            int measuredHeight2 = recyclerListView.getMeasuredHeight() - recyclerListView.getPaddingTop();
            if (measuredHeight == 0) {
                iArr[1] = 0;
                iArr[0] = 0;
                return;
            }
            float f2 = f * (i2 - measuredHeight2);
            iArr[0] = ((int) (f2 / measuredHeight)) * i;
            iArr[1] = ((int) f2) % measuredHeight;
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
                if (recyclerListView != null) {
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
                    if (i == 0) {
                        SharedMediaLayout.this.findPeriodAndJumpToDate(0, recyclerListView, true);
                    }
                }
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public int getTotalItemsCount() {
            return SharedMediaLayout.this.sharedMediaData[0].totalCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public float getScrollProgress(RecyclerListView recyclerListView) {
            int i = (this == SharedMediaLayout.this.animationSupportingPhotoVideoAdapter || this == SharedMediaLayout.this.animationSupportingStoriesAdapter || this == SharedMediaLayout.this.animationSupportingArchivedStoriesAdapter) ? SharedMediaLayout.this.animateToColumnsCount : (this == SharedMediaLayout.this.storiesAdapter || this == SharedMediaLayout.this.archivedStoriesAdapter) ? SharedMediaLayout.this.mediaColumnsCount[1] : SharedMediaLayout.this.mediaColumnsCount[0];
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
            return recyclerListView.getChildCount() != 0 && ((int) Math.ceil((double) (((float) getTotalItemsCount()) / ((float) ((this == SharedMediaLayout.this.photoVideoAdapter || this == SharedMediaLayout.this.storiesAdapter || this == SharedMediaLayout.this.archivedStoriesAdapter) ? SharedMediaLayout.this.mediaColumnsCount[0] : SharedMediaLayout.this.animateToColumnsCount))))) * recyclerListView.getChildAt(0).getMeasuredHeight() > recyclerListView.getMeasuredHeight();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void onFastScrollSingleTap() {
            SharedMediaLayout.this.showMediaCalendar(0, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
            if (period != null) {
                jumpToDate(i, period.maxId, period.startOffset + 1, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        if (!z) {
            return;
        }
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

    /* loaded from: classes4.dex */
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
            this.mContext = context;
            this.currentType = i;
        }

        public void queryServerSearch(final String str, final int i, long j) {
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
            final int i3 = this.lastReqId + 1;
            this.lastReqId = i3;
            this.searchesInProgress++;
            this.reqId = SharedMediaLayout.this.profileActivity.getConnectionsManager().sendRequest(tLRPC$TL_messages_search, new RequestDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda4
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    SharedMediaLayout.MediaSearchAdapter.this.lambda$queryServerSearch$1(i, i3, str, tLObject, tLRPC$TL_error);
                }
            }, 2);
            SharedMediaLayout.this.profileActivity.getConnectionsManager().bindRequestToGuid(this.reqId, SharedMediaLayout.this.profileActivity.getClassGuid());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$queryServerSearch$1(int i, final int i2, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            final ArrayList arrayList = new ArrayList();
            if (tLRPC$TL_error == null) {
                TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                for (int i3 = 0; i3 < tLRPC$messages_Messages.messages.size(); i3++) {
                    TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i3);
                    if (i == 0 || tLRPC$Message.id <= i) {
                        arrayList.add(new MessageObject(SharedMediaLayout.this.profileActivity.getCurrentAccount(), tLRPC$Message, false, true));
                    }
                }
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.MediaSearchAdapter.this.lambda$queryServerSearch$0(i2, arrayList, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
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
                                SharedMediaLayout.this.mediaPages[i2].emptyView.title.setText(LocaleController.formatString("NoResultFoundFor", R.string.NoResultFoundFor, str));
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

        public void search(final String str, boolean z) {
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
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SharedMediaLayout.MediaSearchAdapter.this.lambda$search$3(str);
                    }
                };
                this.searchRunnable = runnable2;
                AndroidUtilities.runOnUIThread(runnable2, 300L);
            } else if (!this.searchResult.isEmpty() || !this.globalSearch.isEmpty() || this.searchesInProgress != 0) {
                this.searchResult.clear();
                this.globalSearch.clear();
                if (this.reqId != 0) {
                    SharedMediaLayout.this.profileActivity.getConnectionsManager().cancelRequest(this.reqId, true);
                    this.reqId = 0;
                    this.searchesInProgress--;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$search$3(final String str) {
            int i;
            if (!SharedMediaLayout.this.sharedMediaData[this.currentType].messages.isEmpty() && ((i = this.currentType) == 1 || i == 4)) {
                MessageObject messageObject = SharedMediaLayout.this.sharedMediaData[this.currentType].messages.get(SharedMediaLayout.this.sharedMediaData[this.currentType].messages.size() - 1);
                queryServerSearch(str, messageObject.getId(), messageObject.getDialogId());
            } else if (this.currentType == 3) {
                queryServerSearch(str, 0, SharedMediaLayout.this.dialog_id);
            }
            int i2 = this.currentType;
            if (i2 == 1 || i2 == 4) {
                final ArrayList arrayList = new ArrayList(SharedMediaLayout.this.sharedMediaData[this.currentType].messages);
                this.searchesInProgress++;
                Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        SharedMediaLayout.MediaSearchAdapter.this.lambda$search$2(str, arrayList);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
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
            translitString = (lowerCase.equals(translitString) || translitString.length() == 0) ? null : null;
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
                                    tLRPC$Document = MessageObject.getMedia(messageObject.messageOwner).webpage.document;
                                } else {
                                    tLRPC$Document = MessageObject.getMedia(messageObject.messageOwner).document;
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

        private void updateSearchResults(final ArrayList<MessageObject> arrayList) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.MediaSearchAdapter.this.lambda$updateSearchResults$4(arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList) {
            if (SharedMediaLayout.this.searching) {
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
                            SharedMediaLayout.this.mediaPages[i].emptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
                            SharedMediaLayout.this.mediaPages[i].emptyView.showProgress(false, true);
                        } else if (itemCount == 0) {
                            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                            sharedMediaLayout.animateItemsEnter(sharedMediaLayout.mediaPages[i].listView, 0, null);
                        }
                    }
                }
                notifyDataSetChanged();
            }
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
            FrameLayout frameLayout;
            int i2 = this.currentType;
            if (i2 == 1) {
                frameLayout = new SharedDocumentCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
            } else if (i2 == 4) {
                frameLayout = new SharedAudioCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider) { // from class: org.telegram.ui.Components.SharedMediaLayout.MediaSearchAdapter.1
                    @Override // org.telegram.ui.Cells.SharedAudioCell
                    public boolean needPlayMessage(MessageObject messageObject) {
                        if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                            boolean playMessage = MediaController.getInstance().playMessage(messageObject);
                            MediaController.getInstance().setVoiceMessagesPlaylist(playMessage ? MediaSearchAdapter.this.searchResult : null, false);
                            if (messageObject.isRoundVideo()) {
                                MediaController.getInstance().setCurrentVideoVisible(false);
                            }
                            return playMessage;
                        } else if (messageObject.isMusic()) {
                            return MediaController.getInstance().setPlaylist(MediaSearchAdapter.this.searchResult, messageObject, SharedMediaLayout.this.mergeDialogId);
                        } else {
                            return false;
                        }
                    }
                };
            } else {
                SharedLinkCell sharedLinkCell = new SharedLinkCell(this.mContext, 0, SharedMediaLayout.this.resourcesProvider);
                sharedLinkCell.setDelegate(SharedMediaLayout.this.sharedLinkCellDelegate);
                frameLayout = sharedLinkCell;
            }
            frameLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(frameLayout);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2 = this.currentType;
            if (i2 == 1) {
                SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) viewHolder.itemView;
                MessageObject item = getItem(i);
                sharedDocumentCell.setDocument(item, i != getItemCount() - 1);
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                if (sharedMediaLayout.isActionModeShowed) {
                    sharedDocumentCell.setChecked(sharedMediaLayout.selectedFiles[(item.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (item.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(item.getId()) >= 0, !SharedMediaLayout.this.scrolling);
                } else {
                    sharedDocumentCell.setChecked(false, !sharedMediaLayout.scrolling);
                }
            } else if (i2 == 3) {
                SharedLinkCell sharedLinkCell = (SharedLinkCell) viewHolder.itemView;
                MessageObject item2 = getItem(i);
                sharedLinkCell.setLink(item2, i != getItemCount() - 1);
                SharedMediaLayout sharedMediaLayout2 = SharedMediaLayout.this;
                if (sharedMediaLayout2.isActionModeShowed) {
                    sharedLinkCell.setChecked(sharedMediaLayout2.selectedFiles[(item2.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (item2.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(item2.getId()) >= 0, !SharedMediaLayout.this.scrolling);
                } else {
                    sharedLinkCell.setChecked(false, !sharedMediaLayout2.scrolling);
                }
            } else if (i2 == 4) {
                SharedAudioCell sharedAudioCell = (SharedAudioCell) viewHolder.itemView;
                MessageObject item3 = getItem(i);
                sharedAudioCell.setMessageObject(item3, i != getItemCount() - 1);
                SharedMediaLayout sharedMediaLayout3 = SharedMediaLayout.this;
                if (sharedMediaLayout3.isActionModeShowed) {
                    sharedAudioCell.setChecked(sharedMediaLayout3.selectedFiles[(item3.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (item3.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(item3.getId()) >= 0, !SharedMediaLayout.this.scrolling);
                } else {
                    sharedAudioCell.setChecked(false, !sharedMediaLayout3.scrolling);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class GifAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return i;
        }

        public GifAdapter(Context context) {
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
            contextLinkCell.setGif(document, messageObject, messageObject.messageOwner.date, false);
            SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
            if (sharedMediaLayout.isActionModeShowed) {
                contextLinkCell.setChecked(sharedMediaLayout.selectedFiles[(messageObject.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0, !SharedMediaLayout.this.scrolling);
            } else {
                contextLinkCell.setChecked(false, !sharedMediaLayout.scrolling);
            }
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ChannelRecommendationsAdapter extends RecyclerListView.SelectionAdapter {
        private final ArrayList<TLRPC$Chat> chats = new ArrayList<>();
        private final Context mContext;
        private int more;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public ChannelRecommendationsAdapter(Context context) {
            this.mContext = context;
            update(false);
        }

        public void update(boolean z) {
            TLRPC$Chat chat;
            if (SharedMediaLayout.this.profileActivity == null || !DialogObject.isChatDialog(SharedMediaLayout.this.dialog_id) || (chat = MessagesController.getInstance(SharedMediaLayout.this.profileActivity.getCurrentAccount()).getChat(Long.valueOf(-SharedMediaLayout.this.dialog_id))) == null || !ChatObject.isChannelAndNotMegaGroup(chat)) {
                return;
            }
            MessagesController.ChannelRecommendations channelRecommendations = MessagesController.getInstance(SharedMediaLayout.this.profileActivity.getCurrentAccount()).getChannelRecommendations(chat.id);
            this.chats.clear();
            int i = 0;
            if (channelRecommendations != null) {
                for (int i2 = 0; i2 < channelRecommendations.chats.size(); i2++) {
                    TLRPC$Chat tLRPC$Chat = channelRecommendations.chats.get(i2);
                    if (tLRPC$Chat != null && ChatObject.isNotInChat(tLRPC$Chat)) {
                        this.chats.add(tLRPC$Chat);
                    }
                }
            }
            if (!this.chats.isEmpty() && !UserConfig.getInstance(SharedMediaLayout.this.profileActivity.getCurrentAccount()).isPremium()) {
                i = channelRecommendations.more;
            }
            this.more = i;
            if (z) {
                notifyDataSetChanged();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.chats.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View profileSearchCell;
            if (i == 1) {
                profileSearchCell = new MoreRecommendationsCell(SharedMediaLayout.this.profileActivity == null ? UserConfig.selectedAccount : SharedMediaLayout.this.profileActivity.getCurrentAccount(), this.mContext, SharedMediaLayout.this.resourcesProvider, new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$ChannelRecommendationsAdapter$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        SharedMediaLayout.ChannelRecommendationsAdapter.this.lambda$onCreateViewHolder$0();
                    }
                });
            } else {
                profileSearchCell = new ProfileSearchCell(this.mContext, SharedMediaLayout.this.resourcesProvider);
            }
            profileSearchCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(profileSearchCell);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0() {
            if (SharedMediaLayout.this.profileActivity != null) {
                SharedMediaLayout.this.profileActivity.presentFragment(new PremiumPreviewFragment("similar_channels"));
            }
        }

        public void openPreview(final int i) {
            if (i < 0 || i >= this.chats.size()) {
                return;
            }
            final TLRPC$Chat tLRPC$Chat = this.chats.get(i);
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", tLRPC$Chat.id);
            ChatActivity chatActivity = new ChatActivity(bundle);
            if (SharedMediaLayout.this.profileActivity instanceof ProfileActivity) {
                ((ProfileActivity) SharedMediaLayout.this.profileActivity).prepareBlurBitmap();
            }
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(SharedMediaLayout.this.getContext(), R.drawable.popup_fixed_alert, SharedMediaLayout.this.resourcesProvider, 2);
            actionBarPopupWindowLayout.setBackgroundColor(SharedMediaLayout.this.getThemedColor(Theme.key_actionBarDefaultSubmenuBackground));
            ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(SharedMediaLayout.this.getContext(), false, false);
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.OpenChannel2), R.drawable.msg_channel);
            actionBarMenuSubItem.setMinimumWidth(160);
            actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$ChannelRecommendationsAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SharedMediaLayout.ChannelRecommendationsAdapter.this.lambda$openPreview$1(view);
                }
            });
            actionBarPopupWindowLayout.addView(actionBarMenuSubItem);
            ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(SharedMediaLayout.this.getContext(), false, false);
            actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString(R.string.ProfileJoinChannel), R.drawable.msg_addbot);
            actionBarMenuSubItem2.setMinimumWidth(160);
            actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$ChannelRecommendationsAdapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SharedMediaLayout.ChannelRecommendationsAdapter.this.lambda$openPreview$3(tLRPC$Chat, i, view);
                }
            });
            actionBarPopupWindowLayout.addView(actionBarMenuSubItem2);
            SharedMediaLayout.this.profileActivity.presentFragmentAsPreviewWithMenu(chatActivity, actionBarPopupWindowLayout);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$openPreview$1(View view) {
            if (SharedMediaLayout.this.profileActivity == null || SharedMediaLayout.this.profileActivity.getParentLayout() == null) {
                return;
            }
            SharedMediaLayout.this.profileActivity.getParentLayout().expandPreviewFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$openPreview$3(final TLRPC$Chat tLRPC$Chat, int i, View view) {
            SharedMediaLayout.this.profileActivity.finishPreviewFragment();
            tLRPC$Chat.left = false;
            update(false);
            notifyItemRemoved(i);
            if (this.chats.isEmpty()) {
                SharedMediaLayout.this.updateTabs(true);
                SharedMediaLayout.this.checkCurrentTabValid();
            }
            SharedMediaLayout.this.profileActivity.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.channelRecommendationsLoaded, Long.valueOf(-SharedMediaLayout.this.dialog_id));
            SharedMediaLayout.this.profileActivity.getMessagesController().addUserToChat(tLRPC$Chat.id, SharedMediaLayout.this.profileActivity.getUserConfig().getCurrentUser(), 0, null, SharedMediaLayout.this.profileActivity, new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$ChannelRecommendationsAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.ChannelRecommendationsAdapter.this.lambda$openPreview$2(tLRPC$Chat);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$openPreview$2(TLRPC$Chat tLRPC$Chat) {
            BulletinFactory of = BulletinFactory.of(SharedMediaLayout.this.profileActivity);
            int i = R.raw.contact_check;
            int i2 = R.string.YouJoinedChannel;
            Object[] objArr = new Object[1];
            objArr[0] = tLRPC$Chat == null ? "" : tLRPC$Chat.title;
            of.createSimpleBulletin(i, LocaleController.formatString(i2, objArr)).show();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ProfileSearchCell profileSearchCell;
            if (viewHolder.getItemViewType() == 0) {
                profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
            } else {
                profileSearchCell = viewHolder.getItemViewType() == 1 ? ((MoreRecommendationsCell) viewHolder.itemView).channelCell : null;
            }
            if (profileSearchCell != null) {
                profileSearchCell.setData(this.chats.get(i), null, null, null, false, false);
                profileSearchCell.useSeparator = i != this.chats.size() - 1;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (this.more <= 0 || i != getItemCount() - 1) ? 0 : 1;
        }
    }

    /* loaded from: classes4.dex */
    private static class MoreRecommendationsCell extends FrameLayout {
        private final ButtonWithCounterView button;
        public final ProfileSearchCell channelCell;
        private final View gradientView;
        private final Theme.ResourcesProvider resourcesProvider;
        private final LinkSpanDrawable.LinksTextView textView;

        public MoreRecommendationsCell(int i, Context context, Theme.ResourcesProvider resourcesProvider, final Runnable runnable) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            ProfileSearchCell profileSearchCell = new ProfileSearchCell(context, resourcesProvider);
            this.channelCell = profileSearchCell;
            profileSearchCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), 2));
            addView(profileSearchCell, LayoutHelper.createFrame(-1, -2.0f));
            View view = new View(context);
            this.gradientView = view;
            GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
            int i2 = Theme.key_windowBackgroundWhite;
            view.setBackground(new GradientDrawable(orientation, new int[]{Theme.multAlpha(Theme.getColor(i2, resourcesProvider), 0.4f), Theme.getColor(i2, resourcesProvider)}));
            addView(view, LayoutHelper.createFrame(-1, 60.0f));
            ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
            this.button = buttonWithCounterView;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.MoreSimilarButton));
            spannableStringBuilder.append((CharSequence) " ");
            SpannableString spannableString = new SpannableString("l");
            spannableString.setSpan(new ColoredImageSpan(R.drawable.msg_mini_lock2), 0, 1, 33);
            spannableStringBuilder.append((CharSequence) spannableString);
            buttonWithCounterView.setText(spannableStringBuilder, false);
            addView(buttonWithCounterView, LayoutHelper.createFrame(-1, 48.0f, 48, 14.0f, 38.0f, 14.0f, 0.0f));
            buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.SharedMediaLayout$MoreRecommendationsCell$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    SharedMediaLayout.MoreRecommendationsCell.lambda$new$0(runnable, view2);
                }
            });
            LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            this.textView = linksTextView;
            linksTextView.setTextSize(1, 13.0f);
            linksTextView.setTextAlignment(4);
            linksTextView.setGravity(17);
            linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            linksTextView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText, resourcesProvider));
            linksTextView.setLineSpacing(AndroidUtilities.dp(3.0f), 1.0f);
            SpannableStringBuilder premiumText = AndroidUtilities.premiumText(LocaleController.getString(R.string.MoreSimilarText), new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$MoreRecommendationsCell$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.MoreRecommendationsCell.lambda$new$1(runnable);
                }
            });
            SpannableString spannableString2 = new SpannableString("" + MessagesController.getInstance(i).recommendedChannelsLimitPremium);
            spannableString2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, spannableString2.length(), 33);
            linksTextView.setText(AndroidUtilities.replaceCharSequence("%s", premiumText, spannableString2));
            addView(linksTextView, LayoutHelper.createFrame(-1, -2.0f, 49, 24.0f, 96.0f, 24.0f, 12.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$new$0(Runnable runnable, View view) {
            if (runnable != null) {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$new$1(Runnable runnable) {
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(145.0f), 1073741824));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class CommonGroupsAdapter extends RecyclerListView.SelectionAdapter {
        private ArrayList<TLRPC$Chat> chats = new ArrayList<>();
        private boolean endReached;
        private boolean firstLoaded;
        private boolean loading;
        private Context mContext;

        public CommonGroupsAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getChats(long j, final int i) {
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
            SharedMediaLayout.this.profileActivity.getConnectionsManager().bindRequestToGuid(SharedMediaLayout.this.profileActivity.getConnectionsManager().sendRequest(tLRPC$TL_messages_getCommonChats, new RequestDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$CommonGroupsAdapter$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    SharedMediaLayout.CommonGroupsAdapter.this.lambda$getChats$1(i, tLObject, tLRPC$TL_error);
                }
            }), SharedMediaLayout.this.profileActivity.getClassGuid());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getChats$1(final int i, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$CommonGroupsAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.CommonGroupsAdapter.this.lambda$getChats$0(tLRPC$TL_error, tLObject, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
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
                    InternalListView internalListView = SharedMediaLayout.this.mediaPages[i2].listView;
                    if (this.firstLoaded || itemCount == 0) {
                        SharedMediaLayout.this.animateItemsEnter(internalListView, 0, null);
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

    public int getStoriesCount(int i) {
        StoriesController.StoriesList storiesList;
        if (i != 8) {
            if (i == 9) {
                storiesList = this.archivedStoriesAdapter.storiesList;
            }
            return 0;
        }
        storiesList = this.storiesAdapter.storiesList;
        if (storiesList != null) {
            return storiesList.getCount();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class StoriesAdapter extends SharedPhotoVideoAdapter {
        private int id;
        private final boolean isArchive;
        private ViewsForPeerStoriesRequester poller;
        public final StoriesController.StoriesList storiesList;
        private StoriesAdapter supportingAdapter;

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter
        public int getTopOffset() {
            return 0;
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        public StoriesAdapter(Context context, boolean z) {
            super(context);
            this.isArchive = z;
            if ((!z || SharedMediaLayout.this.isStoriesView()) && (z || !SharedMediaLayout.this.isArchivedOnlyStoriesView())) {
                this.storiesList = SharedMediaLayout.this.profileActivity.getMessagesController().getStoriesController().getStoriesList(SharedMediaLayout.this.dialog_id, z ? 1 : 0);
            } else {
                this.storiesList = null;
            }
            StoriesController.StoriesList storiesList = this.storiesList;
            if (storiesList != null) {
                this.id = storiesList.link();
                this.poller = new ViewsForPeerStoriesRequester(SharedMediaLayout.this.profileActivity.getMessagesController().getStoriesController(), SharedMediaLayout.this.dialog_id, this.storiesList.currentAccount, SharedMediaLayout.this) { // from class: org.telegram.ui.Components.SharedMediaLayout.StoriesAdapter.1
                    @Override // org.telegram.ui.Stories.ViewsForPeerStoriesRequester
                    protected void getStoryIds(ArrayList<Integer> arrayList) {
                        InternalListView internalListView;
                        MessageObject messageObject;
                        int i = 0;
                        while (true) {
                            if (i >= SharedMediaLayout.this.mediaPages.length) {
                                internalListView = null;
                                break;
                            }
                            if (SharedMediaLayout.this.mediaPages[i].listView != null) {
                                RecyclerView.Adapter adapter = SharedMediaLayout.this.mediaPages[i].listView.getAdapter();
                                StoriesAdapter storiesAdapter = StoriesAdapter.this;
                                if (adapter == storiesAdapter) {
                                    internalListView = SharedMediaLayout.this.mediaPages[i].listView;
                                    break;
                                }
                            }
                            i++;
                        }
                        if (internalListView != null) {
                            for (int i2 = 0; i2 < internalListView.getChildCount(); i2++) {
                                View childAt = internalListView.getChildAt(i2);
                                if ((childAt instanceof SharedPhotoVideoCell2) && (messageObject = ((SharedPhotoVideoCell2) childAt).getMessageObject()) != null && messageObject.isStory()) {
                                    arrayList.add(Integer.valueOf(messageObject.storyItem.id));
                                }
                            }
                        }
                    }

                    @Override // org.telegram.ui.Stories.ViewsForPeerStoriesRequester
                    protected boolean updateStories(ArrayList<Integer> arrayList, TL_stories$TL_stories_storyViews tL_stories$TL_stories_storyViews) {
                        StoriesAdapter.this.storiesList.updateStoryViews(arrayList, tL_stories$TL_stories_storyViews.views);
                        return true;
                    }
                };
            }
            checkColumns();
        }

        public void destroy() {
            StoriesController.StoriesList storiesList = this.storiesList;
            if (storiesList != null) {
                storiesList.unlink(this.id);
            }
        }

        private void checkColumns() {
            if (this.storiesList == null || this.isArchive) {
                return;
            }
            if ((!SharedMediaLayout.this.storiesColumnsCountSet || (SharedMediaLayout.this.allowStoriesSingleColumn && this.storiesList.getCount() > 1)) && this.storiesList.getCount() > 0 && !SharedMediaLayout.this.isStoriesView()) {
                if (this.storiesList.getCount() < 5) {
                    SharedMediaLayout.this.mediaColumnsCount[1] = this.storiesList.getCount();
                    if (SharedMediaLayout.this.mediaPages != null && SharedMediaLayout.this.mediaPages[0] != null && SharedMediaLayout.this.mediaPages[1] != null && SharedMediaLayout.this.mediaPages[0].listView != null && SharedMediaLayout.this.mediaPages[1].listView != null) {
                        SharedMediaLayout.this.switchToCurrentSelectedMode(false);
                    }
                    SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                    sharedMediaLayout.allowStoriesSingleColumn = sharedMediaLayout.mediaColumnsCount[1] == 1;
                } else if (SharedMediaLayout.this.allowStoriesSingleColumn) {
                    SharedMediaLayout.this.allowStoriesSingleColumn = false;
                    SharedMediaLayout.this.mediaColumnsCount[1] = Math.max(2, SharedConfig.storiesColumnsCount);
                    if (SharedMediaLayout.this.mediaPages != null && SharedMediaLayout.this.mediaPages[0] != null && SharedMediaLayout.this.mediaPages[1] != null && SharedMediaLayout.this.mediaPages[0].listView != null && SharedMediaLayout.this.mediaPages[1].listView != null) {
                        SharedMediaLayout.this.switchToCurrentSelectedMode(false);
                    }
                }
                SharedMediaLayout.this.storiesColumnsCountSet = true;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            StoriesAdapter storiesAdapter = this.supportingAdapter;
            if (storiesAdapter != null) {
                storiesAdapter.notifyDataSetChanged();
            }
            checkColumns();
        }

        public int columnsCount() {
            if (this == SharedMediaLayout.this.photoVideoAdapter) {
                return SharedMediaLayout.this.mediaColumnsCount[0];
            }
            return (this == SharedMediaLayout.this.storiesAdapter || this == SharedMediaLayout.this.archivedStoriesAdapter) ? SharedMediaLayout.this.mediaColumnsCount[1] : SharedMediaLayout.this.animateToColumnsCount;
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            StoriesController.StoriesList storiesList = this.storiesList;
            if (storiesList == null) {
                return 0;
            }
            if (storiesList.isOnlyCache() && SharedMediaLayout.this.hasInternet()) {
                return 0;
            }
            return this.storiesList.getCount();
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public int getTotalItemsCount() {
            return getItemCount();
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter
        public int getPositionForIndex(int i) {
            return this.isArchive ? getTopOffset() + i : i;
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            RecyclerView.ViewHolder onCreateViewHolder = super.onCreateViewHolder(viewGroup, i);
            View view = onCreateViewHolder.itemView;
            if (view instanceof SharedPhotoVideoCell2) {
                ((SharedPhotoVideoCell2) view).isStory = true;
            }
            return onCreateViewHolder;
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (this.storiesList != null && viewHolder.getItemViewType() == 0) {
                SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) viewHolder.itemView;
                sharedPhotoVideoCell2.isStory = true;
                int topOffset = i - getTopOffset();
                if (topOffset < 0 || topOffset >= this.storiesList.messageObjects.size()) {
                    sharedPhotoVideoCell2.setMessageObject(null, columnsCount());
                    sharedPhotoVideoCell2.isStory = true;
                    return;
                }
                MessageObject messageObject = this.storiesList.messageObjects.get(topOffset);
                sharedPhotoVideoCell2.setMessageObject(messageObject, columnsCount());
                SharedMediaLayout sharedMediaLayout = SharedMediaLayout.this;
                if (!sharedMediaLayout.isActionModeShowed || messageObject == null) {
                    sharedPhotoVideoCell2.setChecked(false, false);
                } else {
                    sharedPhotoVideoCell2.setChecked(sharedMediaLayout.selectedFiles[(messageObject.getDialogId() > SharedMediaLayout.this.dialog_id ? 1 : (messageObject.getDialogId() == SharedMediaLayout.this.dialog_id ? 0 : -1)) == 0 ? (char) 0 : (char) 1].indexOfKey(messageObject.getId()) >= 0, false);
                }
            }
        }

        public void load(boolean z) {
            if (this.storiesList == null) {
                return;
            }
            int columnsCount = columnsCount();
            this.storiesList.load(z, Math.min(100, Math.max(1, columnsCount / 2) * columnsCount * columnsCount));
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            int topOffset;
            MessageObject messageObject;
            TL_stories$StoryItem tL_stories$StoryItem;
            if (this.storiesList == null || (topOffset = i - getTopOffset()) < 0 || topOffset >= this.storiesList.messageObjects.size() || (messageObject = this.storiesList.messageObjects.get(topOffset)) == null || (tL_stories$StoryItem = messageObject.storyItem) == null) {
                return null;
            }
            return LocaleController.formatYearMont(tL_stories$StoryItem.date, true);
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout.SharedPhotoVideoAdapter, org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void onFastScrollSingleTap() {
            SharedMediaLayout.this.showMediaCalendar(this.isArchive ? 9 : 8, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ChatUsersAdapter extends RecyclerListView.SelectionAdapter {
        private TLRPC$ChatFull chatInfo;
        private Context mContext;
        private ArrayList<Integer> sortedUsers;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public ChatUsersAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
            if (tLRPC$ChatFull == null || !tLRPC$ChatFull.participants.participants.isEmpty()) {
                TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
                if (tLRPC$ChatFull2 != null) {
                    return tLRPC$ChatFull2.participants.participants.size();
                }
                return 0;
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
            View view = viewHolder.itemView;
            if (view instanceof UserCell) {
                UserCell userCell = (UserCell) view;
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
                            string = LocaleController.getString("ChannelCreator", R.string.ChannelCreator);
                        } else if (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) {
                            string = LocaleController.getString("ChannelAdmin", R.string.ChannelAdmin);
                        }
                        str = string;
                    } else if (tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantCreator) {
                        str = LocaleController.getString("ChannelCreator", R.string.ChannelCreator);
                    } else if (tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin) {
                        str = LocaleController.getString("ChannelAdmin", R.string.ChannelAdmin);
                    }
                    userCell.setAdminRole(str);
                    userCell.setData(SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant.user_id)), null, null, 0, i != this.chatInfo.participants.participants.size() - 1);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
            return (tLRPC$ChatFull == null || !tLRPC$ChatFull.participants.participants.isEmpty()) ? 0 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
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
            this.mContext = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ boolean canApplySearchResults(int i) {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeUsers() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public final void onDataSetChanged(int i) {
                    SharedMediaLayout.GroupUsersSearchAdapter.this.lambda$new$0(i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                }
            });
            this.currentChat = SharedMediaLayout.this.delegate.getCurrentChat();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int i) {
            notifyDataSetChanged();
            if (i == 1) {
                int i2 = this.searchCount - 1;
                this.searchCount = i2;
                if (i2 == 0) {
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
        }

        private boolean createMenuForParticipant(TLObject tLObject, boolean z, View view) {
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
                TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant = new TLRPC$TL_chatChannelParticipant();
                tLRPC$TL_chatChannelParticipant.channelParticipant = tLRPC$ChannelParticipant;
                tLRPC$TL_chatChannelParticipant.user_id = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
                tLRPC$TL_chatChannelParticipant.inviter_id = tLRPC$ChannelParticipant.inviter_id;
                tLRPC$TL_chatChannelParticipant.date = tLRPC$ChannelParticipant.date;
                tLObject = tLRPC$TL_chatChannelParticipant;
            }
            return SharedMediaLayout.this.delegate.onMemberClick((TLRPC$ChatParticipant) tLObject, true, z, view);
        }

        public void search(final String str, boolean z) {
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
            if (TextUtils.isEmpty(str)) {
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.GroupUsersSearchAdapter.this.lambda$search$1(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable, 300L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: processSearch */
        public void lambda$search$1(final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.GroupUsersSearchAdapter.this.lambda$processSearch$3(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$3(final String str) {
            final ArrayList arrayList = null;
            this.searchRunnable = null;
            if (!ChatObject.isChannel(this.currentChat) && SharedMediaLayout.this.info != null) {
                arrayList = new ArrayList(SharedMediaLayout.this.info.participants.participants);
            }
            this.searchCount = 2;
            if (arrayList != null) {
                Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        SharedMediaLayout.GroupUsersSearchAdapter.this.lambda$processSearch$2(str, arrayList);
                    }
                });
            } else {
                this.searchCount = 2 - 1;
            }
            this.searchAdapterHelper.queryServerSearch(str, false, false, true, false, false, ChatObject.isChannel(this.currentChat) ? this.currentChat.id : 0L, false, 2, 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x00f1, code lost:
            if (r14.contains(" " + r3) != false) goto L53;
         */
        /* JADX WARN: Removed duplicated region for block: B:57:0x014b A[LOOP:1: B:33:0x00b5->B:57:0x014b, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:65:0x010a A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$2(String str, ArrayList arrayList) {
            long peerId;
            char c;
            Object obj;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), new ArrayList<>());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            translitString = (lowerCase.equals(translitString) || translitString.length() == 0) ? null : null;
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
                    peerId = ((TLRPC$ChatParticipant) tLObject).user_id;
                } else {
                    if (tLObject instanceof TLRPC$ChannelParticipant) {
                        peerId = MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer);
                    }
                }
                TLRPC$User user = SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(peerId));
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
                                String publicUsername = UserObject.getPublicUsername(user);
                                c = (publicUsername == null || !publicUsername.startsWith(str2)) ? c2 : (char) 2;
                                if (c == 0) {
                                    if (c == 1) {
                                        arrayList2.add(AndroidUtilities.generateSearchName(user.first_name, user.last_name, str2));
                                        obj = null;
                                    } else {
                                        obj = null;
                                        arrayList2.add(AndroidUtilities.generateSearchName("@" + UserObject.getPublicUsername(user), null, "@" + str2));
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

        private void updateSearchResults(final ArrayList<CharSequence> arrayList, final ArrayList<TLObject> arrayList2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    SharedMediaLayout.GroupUsersSearchAdapter.this.lambda$updateSearchResults$4(arrayList, arrayList2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList, ArrayList arrayList2) {
            if (SharedMediaLayout.this.searching) {
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
            manageChatUserCell.setBackgroundColor(SharedMediaLayout.this.getThemedColor(Theme.key_windowBackgroundWhite));
            manageChatUserCell.setDelegate(new ManageChatUserCell.ManageChatUserCellDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda5
                @Override // org.telegram.ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate
                public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell2, boolean z) {
                    boolean lambda$onCreateViewHolder$5;
                    lambda$onCreateViewHolder$5 = SharedMediaLayout.GroupUsersSearchAdapter.this.lambda$onCreateViewHolder$5(manageChatUserCell2, z);
                    return lambda$onCreateViewHolder$5;
                }
            });
            return new RecyclerListView.Holder(manageChatUserCell);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$5(ManageChatUserCell manageChatUserCell, boolean z) {
            TLObject item = getItem(((Integer) manageChatUserCell.getTag()).intValue());
            if (item instanceof TLRPC$ChannelParticipant) {
                return createMenuForParticipant((TLRPC$ChannelParticipant) item, !z, manageChatUserCell);
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$User user;
            SpannableStringBuilder spannableStringBuilder;
            TLObject item = getItem(i);
            if (item instanceof TLRPC$ChannelParticipant) {
                user = SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(MessageObject.getPeerId(((TLRPC$ChannelParticipant) item).peer)));
            } else if (!(item instanceof TLRPC$ChatParticipant)) {
                return;
            } else {
                user = SharedMediaLayout.this.profileActivity.getMessagesController().getUser(Long.valueOf(((TLRPC$ChatParticipant) item).user_id));
            }
            UserObject.getPublicUsername(user);
            this.searchAdapterHelper.getGroupSearch().size();
            String lastFoundChannel = this.searchAdapterHelper.getLastFoundChannel();
            if (lastFoundChannel != null) {
                String userName = UserObject.getUserName(user);
                spannableStringBuilder = new SpannableStringBuilder(userName);
                int indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(userName, lastFoundChannel);
                if (indexOfIgnoreCase != -1) {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(SharedMediaLayout.this.getThemedColor(Theme.key_windowBackgroundWhiteBlueText4)), indexOfIgnoreCase, lastFoundChannel.length() + indexOfIgnoreCase, 33);
                }
            } else {
                spannableStringBuilder = null;
            }
            ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
            manageChatUserCell.setTag(Integer.valueOf(i));
            manageChatUserCell.setData(user, spannableStringBuilder, null, false);
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
        arrayList.add(new ThemeDescription(this.selectedMessagesCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.shadowLine, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_divider));
        RLottieImageView iconView = this.deleteItem.getIconView();
        int i = ThemeDescription.FLAG_IMAGECOLOR;
        int i2 = Theme.key_actionBarActionModeDefaultIcon;
        arrayList.add(new ThemeDescription(iconView, i, null, null, null, null, i2));
        ActionBarMenuItem actionBarMenuItem = this.deleteItem;
        int i3 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        int i4 = Theme.key_actionBarActionModeDefaultSelector;
        arrayList.add(new ThemeDescription(actionBarMenuItem, i3, null, null, null, null, i4));
        if (this.gotoItem != null) {
            arrayList.add(new ThemeDescription(this.gotoItem.getIconView(), ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, i2));
            arrayList.add(new ThemeDescription(this.gotoItem, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i4));
        }
        if (this.forwardItem != null) {
            arrayList.add(new ThemeDescription(this.forwardItem.getIconView(), ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, i2));
            arrayList.add(new ThemeDescription(this.forwardItem, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i4));
        }
        char c = 0;
        arrayList.add(new ThemeDescription(this.closeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, new Drawable[]{this.backDrawable}, null, i2));
        arrayList.add(new ThemeDescription(this.closeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i4));
        LinearLayout linearLayout = this.actionModeLayout;
        int i5 = ThemeDescription.FLAG_BACKGROUND;
        int i6 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(linearLayout, i5, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.floatingDateView, 0, null, null, null, null, Theme.key_chat_mediaTimeBackground));
        arrayList.add(new ThemeDescription(this.floatingDateView, 0, null, null, null, null, Theme.key_chat_mediaTimeText));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip, 0, new Class[]{ScrollSlidingTextTabStrip.class}, new String[]{"selectorDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_profile_tabSelectedLine));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, Theme.key_profile_tabSelectedText));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, Theme.key_profile_tabText));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{TextView.class}, null, null, null, Theme.key_profile_tabSelector));
        if (this.fragmentContextView != null) {
            arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerBackground));
            arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerPlayPause));
            arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerTitle));
            arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerPerformer));
            arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerClose));
            arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_returnToCallBackground));
            arrayList.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_returnToCallText));
        }
        final int i7 = 0;
        while (i7 < this.mediaPages.length) {
            ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.Components.SharedMediaLayout$$ExternalSyntheticLambda15
                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public final void didSetColor() {
                    SharedMediaLayout.this.lambda$getThemeDescriptions$19(i7);
                }

                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public /* synthetic */ void onAnimationProgress(float f) {
                    ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
                }
            };
            InternalListView internalListView = this.mediaPages[i7].listView;
            Class[] clsArr = new Class[1];
            clsArr[c] = View.class;
            arrayList.add(new ThemeDescription(internalListView, 0, clsArr, Theme.dividerPaint, null, null, Theme.key_divider));
            FlickerLoadingView flickerLoadingView = this.mediaPages[i7].progressView;
            int i8 = Theme.key_windowBackgroundWhite;
            arrayList.add(new ThemeDescription(flickerLoadingView, 0, null, null, null, null, i8));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder));
            InternalListView internalListView2 = this.mediaPages[i7].listView;
            int i9 = ThemeDescription.FLAG_SECTIONS;
            Class[] clsArr2 = new Class[1];
            clsArr2[c] = GraySectionCell.class;
            arrayList.add(new ThemeDescription(internalListView2, i9, clsArr2, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_graySectionText));
            InternalListView internalListView3 = this.mediaPages[i7].listView;
            int i10 = ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_SECTIONS;
            Class[] clsArr3 = new Class[1];
            clsArr3[c] = GraySectionCell.class;
            arrayList.add(new ThemeDescription(internalListView3, i10, clsArr3, null, null, null, Theme.key_graySection));
            InternalListView internalListView4 = this.mediaPages[i7].listView;
            Class[] clsArr4 = new Class[1];
            clsArr4[c] = LoadingCell.class;
            int i11 = Theme.key_progressCircle;
            arrayList.add(new ThemeDescription(internalListView4, 0, clsArr4, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
            InternalListView internalListView5 = this.mediaPages[i7].listView;
            int i12 = ThemeDescription.FLAG_TEXTCOLOR;
            Class[] clsArr5 = new Class[1];
            clsArr5[c] = UserCell.class;
            arrayList.add(new ThemeDescription(internalListView5, i12, clsArr5, new String[]{"adminTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_profile_creatorIcon));
            InternalListView internalListView6 = this.mediaPages[i7].listView;
            Class[] clsArr6 = new Class[1];
            clsArr6[c] = UserCell.class;
            arrayList.add(new ThemeDescription(internalListView6, 0, clsArr6, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayIcon));
            InternalListView internalListView7 = this.mediaPages[i7].listView;
            Class[] clsArr7 = new Class[1];
            clsArr7[c] = UserCell.class;
            int i13 = Theme.key_windowBackgroundWhiteBlackText;
            arrayList.add(new ThemeDescription(internalListView7, 0, clsArr7, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
            InternalListView internalListView8 = this.mediaPages[i7].listView;
            Class[] clsArr8 = new Class[1];
            clsArr8[c] = UserCell.class;
            int i14 = Theme.key_windowBackgroundWhiteGrayText;
            arrayList.add(new ThemeDescription(internalListView8, 0, clsArr8, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, i14));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText));
            Drawable[] drawableArr = Theme.avatarDrawables;
            int i15 = Theme.key_avatar_text;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{UserCell.class}, null, drawableArr, null, i15));
            TextPaint[] textPaintArr = Theme.dialogs_namePaint;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr[0], textPaintArr[1], Theme.dialogs_searchNamePaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_name));
            TextPaint[] textPaintArr2 = Theme.dialogs_nameEncryptedPaint;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr2[0], textPaintArr2[1], Theme.dialogs_searchNameEncryptedPaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_secretName));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{ProfileSearchCell.class}, null, Theme.avatarDrawables, null, i15));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
            arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
            int i16 = Theme.key_windowBackgroundWhiteGrayText2;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EmptyStubView.class}, new String[]{"emptyTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i16));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"dateTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText3));
            int i17 = Theme.key_sharedMedia_startStopLoadIcon;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{SharedDocumentCell.class}, new String[]{"progressView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i17));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"statusImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i17));
            int i18 = Theme.key_checkbox;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i18));
            int i19 = Theme.key_checkboxCheck;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i19));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"thumbImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_files_folderIcon));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"extTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_files_iconText));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedAudioCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i18));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedAudioCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i19));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedAudioCell.class}, Theme.chat_contextResult_titleTextPaint, null, null, i13));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedAudioCell.class}, Theme.chat_contextResult_descriptionTextPaint, null, null, i16));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedLinkCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i18));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedLinkCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i19));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{SharedLinkCell.class}, new String[]{"titleTextPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{SharedLinkCell.class}, null, null, null, Theme.key_windowBackgroundWhiteLinkText));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{SharedLinkCell.class}, Theme.linkSelectionPaint, null, null, Theme.key_windowBackgroundWhiteLinkSelection));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{SharedLinkCell.class}, new String[]{"letterDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_sharedMedia_linkPlaceholderText));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{SharedLinkCell.class}, new String[]{"letterDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_sharedMedia_linkPlaceholder));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_SECTIONS | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{SharedMediaSectionCell.class}, null, null, null, i8));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_SECTIONS, new Class[]{SharedMediaSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{SharedMediaSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
            int i20 = Theme.key_sharedMedia_photoPlaceholder;
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{SharedPhotoVideoCell.class}, new String[]{"backgroundPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i20));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedPhotoVideoCell.class}, null, null, themeDescriptionDelegate, i18));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedPhotoVideoCell.class}, null, null, themeDescriptionDelegate, i19));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, new Class[]{ContextLinkCell.class}, new String[]{"backgroundPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i20));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{ContextLinkCell.class}, null, null, themeDescriptionDelegate, i18));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{ContextLinkCell.class}, null, null, themeDescriptionDelegate, i19));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].listView, 0, null, null, new Drawable[]{this.pinnedHeaderShadowDrawable}, null, Theme.key_windowBackgroundGrayShadow));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].emptyView.title, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i13));
            arrayList.add(new ThemeDescription(this.mediaPages[i7].emptyView.subtitle, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i14));
            i7++;
            c = 0;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$19(int i) {
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

    public int getNextMediaColumnsCount(int i, int i2, boolean z) {
        int i3 = 1;
        int i4 = i2 + (!z ? 1 : -1);
        if (i4 > 6) {
            i4 = !z ? 9 : 6;
        }
        return Utilities.clamp(i4, 9, (this.allowStoriesSingleColumn && i == 1) ? 2 : 2);
    }

    public Boolean zoomIn() {
        if (this.photoVideoChangeColumnsAnimation) {
            return null;
        }
        MediaPage[] mediaPageArr = this.mediaPages;
        if (mediaPageArr[0] == null) {
            return null;
        }
        int i = mediaPageArr[0].selectedType;
        this.changeColumnsTab = i;
        int i2 = (i == 8 || i == 9) ? 1 : 0;
        int nextMediaColumnsCount = getNextMediaColumnsCount(i2, this.mediaColumnsCount[i2], true);
        if (this.mediaZoomInItem != null && nextMediaColumnsCount == getNextMediaColumnsCount(i2, nextMediaColumnsCount, true)) {
            this.mediaZoomInItem.setEnabled(false);
            this.mediaZoomInItem.animate().alpha(0.5f).start();
        }
        if (this.mediaColumnsCount[i2] != nextMediaColumnsCount) {
            ActionBarMenuSubItem actionBarMenuSubItem = this.mediaZoomOutItem;
            if (actionBarMenuSubItem != null && !actionBarMenuSubItem.isEnabled()) {
                this.mediaZoomOutItem.setEnabled(true);
                this.mediaZoomOutItem.animate().alpha(1.0f).start();
            }
            if (i2 != 0) {
                if (getStoriesCount(this.mediaPages[0].selectedType) >= 5) {
                    SharedConfig.setStoriesColumnsCount(nextMediaColumnsCount);
                }
            } else {
                SharedConfig.setMediaColumnsCount(nextMediaColumnsCount);
            }
            animateToMediaColumnsCount(nextMediaColumnsCount);
        }
        return Boolean.valueOf(nextMediaColumnsCount != getNextMediaColumnsCount(i2, nextMediaColumnsCount, true));
    }

    public Boolean zoomOut() {
        if (this.photoVideoChangeColumnsAnimation) {
            return null;
        }
        MediaPage[] mediaPageArr = this.mediaPages;
        if (mediaPageArr[0] == null || this.allowStoriesSingleColumn) {
            return null;
        }
        int i = mediaPageArr[0].selectedType;
        this.changeColumnsTab = i;
        int i2 = (i == 8 || i == 9) ? 1 : 0;
        int nextMediaColumnsCount = getNextMediaColumnsCount(i2, this.mediaColumnsCount[i2], false);
        if (this.mediaZoomOutItem != null && nextMediaColumnsCount == getNextMediaColumnsCount(i2, nextMediaColumnsCount, false)) {
            this.mediaZoomOutItem.setEnabled(false);
            this.mediaZoomOutItem.animate().alpha(0.5f).start();
        }
        if (this.mediaColumnsCount[i2] != nextMediaColumnsCount) {
            ActionBarMenuSubItem actionBarMenuSubItem = this.mediaZoomInItem;
            if (actionBarMenuSubItem != null && !actionBarMenuSubItem.isEnabled()) {
                this.mediaZoomInItem.setEnabled(true);
                this.mediaZoomInItem.animate().alpha(1.0f).start();
            }
            if (i2 != 0) {
                if (getStoriesCount(this.mediaPages[0].selectedType) >= 5) {
                    SharedConfig.setStoriesColumnsCount(nextMediaColumnsCount);
                }
            } else {
                SharedConfig.setMediaColumnsCount(nextMediaColumnsCount);
            }
            animateToMediaColumnsCount(nextMediaColumnsCount);
        }
        return Boolean.valueOf(nextMediaColumnsCount != getNextMediaColumnsCount(i2, nextMediaColumnsCount, false));
    }

    public boolean canZoomIn() {
        MediaPage[] mediaPageArr = this.mediaPages;
        if (mediaPageArr == null || mediaPageArr[0] == null) {
            return false;
        }
        int i = (mediaPageArr[0].selectedType == 8 || this.mediaPages[0].selectedType == 9) ? 1 : 0;
        int[] iArr = this.mediaColumnsCount;
        return iArr[i] != getNextMediaColumnsCount(i, iArr[i], true);
    }

    public boolean canZoomOut() {
        MediaPage[] mediaPageArr = this.mediaPages;
        if (mediaPageArr == null || mediaPageArr[0] == null || this.allowStoriesSingleColumn) {
            return false;
        }
        int i = (mediaPageArr[0].selectedType == 8 || this.mediaPages[0].selectedType == 9) ? 1 : 0;
        int[] iArr = this.mediaColumnsCount;
        return iArr[i] != getNextMediaColumnsCount(i, iArr[i], false);
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

    /* loaded from: classes4.dex */
    public class ScrollSlidingTextTabStripInner extends ScrollSlidingTextTabStrip {
        public int backgroundColor;
        protected Paint backgroundPaint;

        public ScrollSlidingTextTabStripInner(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            this.backgroundColor = 0;
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

    /* JADX INFO: Access modifiers changed from: private */
    public int getThemedColor(int i) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        if (resourcesProvider != null) {
            return resourcesProvider.getColor(i);
        }
        return Theme.getColor(i);
    }

    public float getTabProgress() {
        float f = 0.0f;
        int i = 0;
        while (true) {
            MediaPage[] mediaPageArr = this.mediaPages;
            if (i >= mediaPageArr.length) {
                return f;
            }
            if (mediaPageArr[i] != null) {
                f += mediaPageArr[i].selectedType * (1.0f - Math.abs(this.mediaPages[i].getTranslationX() / getWidth()));
            }
            i++;
        }
    }

    /* loaded from: classes4.dex */
    public static class InternalListView extends BlurredRecyclerView implements StoriesListPlaceProvider.ClippedView {
        public int hintPaddingTop;

        public InternalListView(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Stories.StoriesListPlaceProvider.ClippedView
        public void updateClip(int[] iArr) {
            iArr[0] = (getPaddingTop() - AndroidUtilities.dp(2.0f)) - this.hintPaddingTop;
            iArr[1] = getMeasuredHeight() - getPaddingBottom();
        }
    }
}
