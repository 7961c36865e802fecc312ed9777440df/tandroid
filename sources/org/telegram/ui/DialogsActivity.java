package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.LongSparseArray;
import android.util.Property;
import android.util.StateSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerCustom;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BirthdayController;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FilesMigrationService;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatInvite;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$InputDialogPeer;
import org.telegram.tgnet.TLRPC$MessagesFilter;
import org.telegram.tgnet.TLRPC$RequestPeerType;
import org.telegram.tgnet.TLRPC$StarsSubscription;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_account_updateBirthday;
import org.telegram.tgnet.TLRPC$TL_birthday;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channels_sendAsPeers;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_dialog;
import org.telegram.tgnet.TLRPC$TL_dialogFolder;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_help_premiumPromo;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_messages_checkHistoryImportPeer;
import org.telegram.tgnet.TLRPC$TL_messages_checkedHistoryImportPeer;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_updateDialogFilter;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlChat;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlChatInvite;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlStickerSet;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlUnknown;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlUser;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeBroadcast;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeChat;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeUser;
import org.telegram.tgnet.TLRPC$TL_userEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.tl.TL_chatlists$TL_chatlists_chatlistUpdates;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_storiesStealthMode;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.ArchiveHintInnerCell;
import org.telegram.ui.Cells.BaseCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.DialogsEmptyCell;
import org.telegram.ui.Cells.DialogsHintCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.RequestPeerRequirementsCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UnconfirmedAuthHintCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.ArchiveHelp;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BlurredRecyclerView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DialogsItemAnimator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FilterTabsView;
import org.telegram.ui.Components.FiltersListBottomSheet;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugController;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider;
import org.telegram.ui.Components.FolderBottomSheet;
import org.telegram.ui.Components.FolderDrawable;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MediaActivity;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.PacmanAnimation;
import org.telegram.ui.Components.PopupSwipeBackLayout;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.Premium.boosts.UserSelectorBottomSheet;
import org.telegram.ui.Components.ProxyDrawable;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerAnimationScrollHelper;
import org.telegram.ui.Components.RecyclerItemsEnterAnimator;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SearchViewPager;
import org.telegram.ui.Components.SharedMediaLayout;
import org.telegram.ui.Components.SimpleThemeDescription;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.ViewPagerFixed;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.FilterCreateActivity;
import org.telegram.ui.FilteredSearchView;
import org.telegram.ui.GroupCreateFinalActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.DialogStoriesCell;
import org.telegram.ui.Stories.StealthModeAlert;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.UserListPoller;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.bots.BotWebViewSheet;
/* loaded from: classes4.dex */
public class DialogsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, FloatingDebugProvider {
    public static boolean[] dialogsLoaded = new boolean[4];
    private static final Interpolator interpolator = new Interpolator() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda59
        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            float lambda$static$0;
            lambda$static$0 = DialogsActivity.lambda$static$0(f);
            return lambda$static$0;
        }
    };
    public static float viewOffset = 0.0f;
    public final Property<DialogsActivity, Float> SCROLL_Y;
    public final Property<View, Float> SEARCH_TRANSLATION_Y;
    private ValueAnimator actionBarColorAnimator;
    private Paint actionBarDefaultPaint;
    private int actionModeAdditionalHeight;
    private boolean actionModeFullyShowed;
    private final ArrayList<View> actionModeViews;
    private ActionBarMenuSubItem addToFolderItem;
    private String addToGroupAlertString;
    private float additionalFloatingTranslation;
    private float additionalFloatingTranslation2;
    private float additionalOffset;
    private boolean afterSignup;
    public boolean allowBots;
    public boolean allowChannels;
    private boolean allowGlobalSearch;
    public boolean allowGroups;
    public boolean allowLegacyGroups;
    public boolean allowMegagroups;
    private boolean allowMoving;
    private boolean allowSwipeDuringCurrentTouch;
    private boolean allowSwitchAccount;
    public boolean allowUsers;
    private boolean animateToHasStories;
    private DrawerProfileCell.AnimatedStatusView animatedStatusView;
    private boolean animatingForward;
    private ActionBarMenuItem archive2Item;
    private ActionBarMenuSubItem archiveItem;
    private boolean askAboutContacts;
    private boolean askingForPermissions;
    private UnconfirmedAuthHintCell authHintCell;
    private boolean authHintCellAnimating;
    private ValueAnimator authHintCellAnimator;
    private float authHintCellProgress;
    private boolean authHintCellVisible;
    private ChatAvatarContainer avatarContainer;
    private boolean backAnimation;
    private BackDrawable backDrawable;
    private ActionBarMenuSubItem blockItem;
    private View blurredView;
    private ArrayList<TLRPC$Dialog> botShareDialogs;
    private Long cacheSize;
    private int canClearCacheCount;
    private boolean canDeletePsaSelected;
    private int canMuteCount;
    private int canPinCount;
    private int canReadCount;
    private int canReportSpamCount;
    private boolean canSelectTopics;
    private boolean canShowFilterTabsView;
    private boolean canShowHiddenArchive;
    private boolean canShowStoryHint;
    private int canUnarchiveCount;
    private int canUnmuteCount;
    private boolean cantSendToChannels;
    private boolean checkCanWrite;
    private boolean checkPermission;
    private boolean checkingImportDialog;
    private ActionBarMenuSubItem clearItem;
    private boolean closeFragment;
    private boolean closeSearchFieldOnHide;
    private ChatActivityEnterView commentView;
    private AnimatorSet commentViewAnimator;
    private View commentViewBg;
    private float contactsAlpha;
    private ValueAnimator contactsAlphaAnimator;
    private int currentConnectionState;
    View databaseMigrationHint;
    private int debugLastUpdateAction;
    private DialogsActivityDelegate delegate;
    private ActionBarMenuItem deleteItem;
    private Long deviceSize;
    public DialogStoriesCell dialogStoriesCell;
    public boolean dialogStoriesCellVisible;
    private DialogsHintCell dialogsHintCell;
    private boolean dialogsHintCellVisible;
    private boolean dialogsListFrozen;
    private boolean disableActionBarScrolling;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimator;
    private ActionBarMenuItem downloadsItem;
    private boolean downloadsItemVisible;
    private ItemOptions filterOptions;
    private float filterTabsMoveFrom;
    private float filterTabsProgress;
    private FilterTabsView filterTabsView;
    private boolean filterTabsViewIsVisible;
    private ValueAnimator filtersTabAnimator;
    private FiltersView filtersView;
    private boolean fixScrollYAfterArchiveOpened;
    private RadialProgressView floating2ProgressView;
    private RLottieImageView floatingButton;
    private RLottieImageView floatingButton2;
    private FrameLayout floatingButton2Container;
    private FrameLayout floatingButtonContainer;
    private float floatingButtonHideProgress;
    private float floatingButtonPanOffset;
    private float floatingButtonTranslation;
    private boolean floatingForceVisible;
    private boolean floatingHidden;
    private final AccelerateDecelerateInterpolator floatingInterpolator;
    private AnimatorSet floatingProgressAnimator;
    private boolean floatingProgressVisible;
    private int folderId;
    private int forumCount;
    private int fragmentContextTopPadding;
    private FragmentContextView fragmentContextView;
    private FragmentContextView fragmentLocationContextView;
    private ArrayList<TLRPC$Dialog> frozenDialogsList;
    private boolean hasInvoice;
    public boolean hasOnlySlefStories;
    private int hasPoll;
    public boolean hasStories;
    private int initialDialogsType;
    private String initialSearchString;
    private int initialSearchType;
    private boolean invalidateScrollY;
    boolean isDrawerTransition;
    private boolean isFirstTab;
    private boolean isNextButton;
    private boolean isPremiumHintUpgrade;
    public boolean isQuote;
    public boolean isReplyTo;
    boolean isSlideBackTransition;
    private int lastMeasuredTopPadding;
    private int maximumVelocity;
    private boolean maybeStartTracking;
    private MenuDrawable menuDrawable;
    private int messagesCount;
    private ArrayList<MessagesController.DialogFilter> movingDialogFilters;
    private DialogCell movingView;
    private boolean movingWas;
    private ActionBarMenuItem muteItem;
    private AnimationNotificationsLocker notificationsLocker;
    public boolean notify;
    private boolean onlySelect;
    private MessagesStorage.TopicKey openedDialogId;
    private ActionBarMenuItem optionsItem;
    private PacmanAnimation pacmanAnimation;
    private Paint paint;
    float panTranslationY;
    private RLottieDrawable passcodeDrawable;
    private ActionBarMenuItem passcodeItem;
    private boolean passcodeItemVisible;
    private AlertDialog permissionDialog;
    private ActionBarMenuSubItem pin2Item;
    private ActionBarMenuItem pinItem;
    private Drawable premiumStar;
    private int prevPosition;
    private int prevTop;
    private float progressToActionMode;
    public float progressToDialogStoriesCell;
    public float progressToShowStories;
    private ProxyDrawable proxyDrawable;
    private ActionBarMenuItem proxyItem;
    private boolean proxyItemVisible;
    private ActionBarMenuSubItem readItem;
    private RectF rect;
    private ActionBarMenuSubItem removeFromFolderItem;
    private long requestPeerBotId;
    private TLRPC$RequestPeerType requestPeerType;
    private boolean resetDelegate;
    private boolean rightFragmentTransitionInProgress;
    private boolean rightFragmentTransitionIsOpen;
    public RightSlidingDialogContainer rightSlidingDialogContainer;
    private float scrollAdditionalOffset;
    private boolean scrollBarVisible;
    private boolean scrollUpdated;
    private float scrollYOffset;
    private boolean scrollingManually;
    private float searchAnimationProgress;
    private boolean searchAnimationTabsDelayedCrossfade;
    private AnimatorSet searchAnimator;
    private long searchDialogId;
    private boolean searchFiltersWasShowed;
    private boolean searchIsShowed;
    private ActionBarMenuItem searchItem;
    private TLObject searchObject;
    private String searchString;
    private ViewPagerFixed.TabsView searchTabsView;
    private SearchViewPager searchViewPager;
    private int searchViewPagerIndex;
    float searchViewPagerTranslationY;
    private boolean searchWas;
    private boolean searchWasFullyShowed;
    private boolean searching;
    private String selectAlertString;
    private String selectAlertStringGroup;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;
    private View selectedCountView;
    private ArrayList<Long> selectedDialogs;
    private NumberTextView selectedDialogsCountTextView;
    private ActionBarPopupWindow sendPopupWindow;
    private SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader;
    private int shiftDp;
    private boolean showSetPasswordConfirm;
    private String showingSuggestion;
    private RecyclerView sideMenu;
    ValueAnimator slideBackTransitionAnimator;
    boolean slideFragmentLite;
    float slideFragmentProgress;
    private DialogCell slidingView;
    private boolean slowedReloadAfterDialogClick;
    private AnimatorSet speedAnimator;
    private ActionBarMenuItem speedItem;
    private long startArchivePullingTime;
    private boolean startedTracking;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable statusDrawable;
    private Bulletin storiesBulletin;
    public boolean storiesEnabled;
    private float storiesOverscroll;
    private boolean storiesOverscrollCalled;
    ValueAnimator storiesVisibilityAnimator;
    ValueAnimator storiesVisibilityAnimator2;
    private float storiesYOffset;
    private HintView2 storyHint;
    private boolean storyHintShown;
    private ActionBarMenuItem switchItem;
    private Animator tabsAlphaAnimator;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    private float tabsYOffset;
    private TextPaint textPaint;
    private Bulletin topBulletin;
    private int topPadding;
    private UndoView[] undoView;
    private int undoViewIndex;
    private FrameLayout updateLayout;
    private AnimatorSet updateLayoutAnimator;
    private RadialProgress2 updateLayoutIcon;
    private boolean updatePullAfterScroll;
    private TextView updateTextView;
    private ViewPage[] viewPages;
    private boolean waitingForScrollFinished;
    private boolean wasDrawn;
    public boolean whiteActionBar;
    private ImageView[] writeButton;
    private FrameLayout writeButtonContainer;

    /* loaded from: classes4.dex */
    public interface DialogsActivityDelegate {
        boolean didSelectDialogs(DialogsActivity dialogsActivity, ArrayList<MessagesStorage.TopicKey> arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createActionMode$62(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCommentView() {
    }

    public boolean shouldShowNextButton(DialogsActivity dialogsActivity, ArrayList<Long> arrayList, CharSequence charSequence, boolean z) {
        return false;
    }

    static /* synthetic */ float access$4724(DialogsActivity dialogsActivity, float f) {
        float f2 = dialogsActivity.tabsYOffset - f;
        dialogsActivity.tabsYOffset = f2;
        return f2;
    }

    public MessagesStorage.TopicKey getOpenedDialogId() {
        return this.openedDialogId;
    }

    /* loaded from: classes4.dex */
    public class ViewPage extends FrameLayout {
        public boolean animateStoriesView;
        private DialogsAdapter animationSupportDialogsAdapter;
        private RecyclerListView animationSupportListView;
        private int archivePullViewState;
        private DialogsAdapter dialogsAdapter;
        private DialogsItemAnimator dialogsItemAnimator;
        private int dialogsType;
        private boolean isLocked;
        private ItemTouchHelper itemTouchhelper;
        private int lastItemsCount;
        private LinearLayoutManager layoutManager;
        public DialogsRecyclerView listView;
        public int pageAdditionalOffset;
        private FlickerLoadingView progressView;
        private PullForegroundDrawable pullForegroundDrawable;
        private RecyclerItemsEnterAnimator recyclerItemsEnterAnimator;
        Runnable saveScrollPositionRunnable;
        private RecyclerAnimationScrollHelper scrollHelper;
        public RecyclerListViewScroller scroller;
        private int selectedType;
        private SwipeController swipeController;
        Runnable updateListRunnable;
        boolean updating;

        public ViewPage(Context context) {
            super(context);
            this.saveScrollPositionRunnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$ViewPage$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.ViewPage.this.lambda$new$0();
                }
            };
            this.updateListRunnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$ViewPage$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.ViewPage.this.lambda$new$1();
                }
            };
        }

        public boolean isDefaultDialogType() {
            int i = this.dialogsType;
            return i == 0 || i == 7 || i == 8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            DialogsRecyclerView dialogsRecyclerView = this.listView;
            if (dialogsRecyclerView == null || dialogsRecyclerView.getScrollState() != 0 || this.listView.getChildCount() <= 0 || this.listView.getLayoutManager() == null) {
                return;
            }
            int i = 1;
            boolean z = this.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive() && this.archivePullViewState == 2;
            float f = DialogsActivity.this.scrollYOffset;
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.listView.getLayoutManager();
            View view = null;
            int i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int i3 = -1;
            for (int i4 = 0; i4 < this.listView.getChildCount(); i4++) {
                DialogsRecyclerView dialogsRecyclerView2 = this.listView;
                int childAdapterPosition = dialogsRecyclerView2.getChildAdapterPosition(dialogsRecyclerView2.getChildAt(i4));
                View childAt = this.listView.getChildAt(i4);
                if (childAdapterPosition != -1 && childAt != null && childAt.getTop() < i2) {
                    i2 = childAt.getTop();
                    i3 = childAdapterPosition;
                    view = childAt;
                }
            }
            if (view != null) {
                float top = view.getTop() - this.listView.getPaddingTop();
                if (DialogsActivity.this.hasStories) {
                    f = 0.0f;
                }
                if (this.listView.getScrollState() != 1) {
                    if (z && i3 == 0 && ((this.listView.getPaddingTop() - view.getTop()) - view.getMeasuredHeight()) + f < 0.0f) {
                        top = f;
                    } else {
                        i = i3;
                    }
                    linearLayoutManager.scrollToPositionWithOffset(i, (int) top);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            this.dialogsAdapter.updateList(this.saveScrollPositionRunnable);
            DialogsActivity.this.invalidateScrollY = true;
            this.listView.updateDialogsOnNextDraw = true;
            this.updating = false;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.listView.getLayoutParams();
            if (this.animateStoriesView) {
                layoutParams.bottomMargin = -AndroidUtilities.dp(85.0f);
            } else {
                layoutParams.bottomMargin = 0;
            }
            super.onMeasure(i, i2);
        }

        public void updateList(boolean z) {
            if (((BaseFragment) DialogsActivity.this).isPaused) {
                return;
            }
            if (z) {
                AndroidUtilities.cancelRunOnUIThread(this.updateListRunnable);
                this.listView.setItemAnimator(this.dialogsItemAnimator);
                this.updateListRunnable.run();
            } else if (this.updating) {
            } else {
                this.updating = true;
                if (!this.dialogsItemAnimator.isRunning()) {
                    this.listView.setItemAnimator(null);
                }
                AndroidUtilities.runOnUIThread(this.updateListRunnable, 36L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ContentView extends SizeNotifierFrameLayout {
        private Paint actionBarSearchPaint;
        private Rect blurBounds;
        private int inputFieldHeight;
        private int startedTrackingPointerId;
        private int startedTrackingX;
        private int startedTrackingY;
        private VelocityTracker velocityTracker;
        private boolean wasPortrait;
        private Paint windowBackgroundPaint;

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected boolean invalidateOptimized() {
            return true;
        }

        public ContentView(Context context) {
            super(context);
            this.actionBarSearchPaint = new Paint(1);
            this.windowBackgroundPaint = new Paint();
            this.blurBounds = new Rect();
            this.needBlur = true;
            this.blurBehindViews.add(this);
        }

        private boolean prepareForMoving(MotionEvent motionEvent, boolean z) {
            int nextPageId = DialogsActivity.this.filterTabsView.getNextPageId(z);
            if (nextPageId < 0) {
                return false;
            }
            getParent().requestDisallowInterceptTouchEvent(true);
            DialogsActivity.this.maybeStartTracking = false;
            DialogsActivity.this.startedTracking = true;
            this.startedTrackingX = (int) (motionEvent.getX() + DialogsActivity.this.additionalOffset);
            ((BaseFragment) DialogsActivity.this).actionBar.setEnabled(false);
            DialogsActivity.this.filterTabsView.setEnabled(false);
            DialogsActivity.this.viewPages[1].selectedType = nextPageId;
            DialogsActivity.this.viewPages[1].setVisibility(0);
            DialogsActivity.this.animatingForward = z;
            DialogsActivity.this.showScrollbars(false);
            DialogsActivity.this.switchToCurrentSelectedMode(true);
            if (z) {
                DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth());
            } else {
                DialogsActivity.this.viewPages[1].setTranslationX(-DialogsActivity.this.viewPages[0].getMeasuredWidth());
            }
            return true;
        }

        @Override // android.view.View
        public void setPadding(int i, int i2, int i3, int i4) {
            DialogsActivity.this.fragmentContextTopPadding = i2;
            DialogsActivity.this.updateTopPadding();
        }

        public boolean checkTabsAnimationInProgress() {
            boolean z;
            if (DialogsActivity.this.tabsAnimationInProgress) {
                if (DialogsActivity.this.backAnimation) {
                    if (Math.abs(DialogsActivity.this.viewPages[0].getTranslationX()) < 1.0f) {
                        DialogsActivity.this.viewPages[0].setTranslationX(0.0f);
                        DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() * (DialogsActivity.this.animatingForward ? 1 : -1));
                        z = true;
                    }
                    z = false;
                } else {
                    if (Math.abs(DialogsActivity.this.viewPages[1].getTranslationX()) < 1.0f) {
                        DialogsActivity.this.viewPages[0].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() * (DialogsActivity.this.animatingForward ? -1 : 1));
                        DialogsActivity.this.viewPages[1].setTranslationX(0.0f);
                        z = true;
                    }
                    z = false;
                }
                if (z) {
                    DialogsActivity.this.showScrollbars(true);
                    if (DialogsActivity.this.tabsAnimation != null) {
                        DialogsActivity.this.tabsAnimation.cancel();
                        DialogsActivity.this.tabsAnimation = null;
                    }
                    DialogsActivity.this.tabsAnimationInProgress = false;
                }
                return DialogsActivity.this.tabsAnimationInProgress;
            }
            return false;
        }

        public int getActionBarFullHeight() {
            float f = 0.0f;
            float height = ((BaseFragment) DialogsActivity.this).actionBar.getHeight() + (((DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() == 8) ? 0.0f : DialogsActivity.this.filterTabsView.getMeasuredHeight() - ((1.0f - DialogsActivity.this.filterTabsProgress) * DialogsActivity.this.filterTabsView.getMeasuredHeight())) * (1.0f - DialogsActivity.this.searchAnimationProgress)) + (((DialogsActivity.this.searchTabsView == null || DialogsActivity.this.searchTabsView.getVisibility() == 8) ? 0.0f : DialogsActivity.this.searchTabsView.getMeasuredHeight()) * DialogsActivity.this.searchAnimationProgress);
            RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
                f = DialogsActivity.this.rightSlidingDialogContainer.openedProgress;
            }
            if (DialogsActivity.this.hasStories) {
                height += AndroidUtilities.dp(81.0f) * (1.0f - DialogsActivity.this.searchAnimationProgress) * (1.0f - f) * (1.0f - DialogsActivity.this.progressToActionMode);
            }
            return (int) (height + DialogsActivity.this.storiesOverscroll);
        }

        public int getActionBarTop() {
            float f = DialogsActivity.this.scrollYOffset;
            DialogsActivity dialogsActivity = DialogsActivity.this;
            if (dialogsActivity.hasStories) {
                float f2 = 0.0f;
                RightSlidingDialogContainer rightSlidingDialogContainer = dialogsActivity.rightSlidingDialogContainer;
                if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
                    f2 = DialogsActivity.this.rightSlidingDialogContainer.openedProgress;
                }
                f *= (1.0f - DialogsActivity.this.progressToActionMode) * (1.0f - f2);
            }
            return (int) ((-getY()) + (f * (1.0f - DialogsActivity.this.searchAnimationProgress)));
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if ((view == DialogsActivity.this.fragmentContextView && DialogsActivity.this.fragmentContextView.isCallStyle()) || view == DialogsActivity.this.blurredView) {
                return true;
            }
            if (!SizeNotifierFrameLayout.drawingBlur) {
                if (view != DialogsActivity.this.viewPages[0] && ((DialogsActivity.this.viewPages.length <= 1 || view != DialogsActivity.this.viewPages[1]) && view != DialogsActivity.this.fragmentContextView && view != DialogsActivity.this.fragmentLocationContextView && view != DialogsActivity.this.dialogsHintCell && view != DialogsActivity.this.authHintCell)) {
                    if (view == ((BaseFragment) DialogsActivity.this).actionBar && DialogsActivity.this.slideFragmentProgress != 1.0f) {
                        canvas.save();
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        if (dialogsActivity.slideFragmentLite) {
                            canvas.translate((dialogsActivity.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                        } else {
                            float f = 1.0f - ((1.0f - dialogsActivity.slideFragmentProgress) * 0.05f);
                            canvas.translate((dialogsActivity.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                            canvas.scale(f, f, DialogsActivity.this.isDrawerTransition ? getMeasuredWidth() : 0.0f, (((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + (ActionBar.getCurrentActionBarHeight() / 2.0f));
                        }
                        boolean drawChild = super.drawChild(canvas, view, j);
                        canvas.restore();
                        return drawChild;
                    }
                    return super.drawChild(canvas, view, j);
                }
                canvas.save();
                canvas.clipRect(0.0f, (-getY()) + getActionBarTop() + getActionBarFullHeight(), getMeasuredWidth(), getMeasuredHeight());
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                float f2 = dialogsActivity2.slideFragmentProgress;
                if (f2 != 1.0f) {
                    if (dialogsActivity2.slideFragmentLite) {
                        canvas.translate((dialogsActivity2.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                    } else {
                        float f3 = 1.0f - ((1.0f - f2) * 0.05f);
                        canvas.translate((dialogsActivity2.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                        canvas.scale(f3, f3, DialogsActivity.this.isDrawerTransition ? getMeasuredWidth() : 0.0f, (-getY()) + DialogsActivity.this.scrollYOffset + getActionBarFullHeight());
                    }
                }
                boolean drawChild2 = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild2;
            }
            return super.drawChild(canvas, view, j);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00ae, code lost:
            if (r0 == 1) goto L18;
         */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void dispatchDraw(Canvas canvas) {
            int actionBarTop;
            int i;
            float clamp;
            float f;
            Paint paint;
            int i2;
            DialogsActivity dialogsActivity;
            if (DialogsActivity.this.invalidateScrollY && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                if (dialogsActivity2.hasStories && dialogsActivity2.progressToActionMode == 0.0f) {
                    DialogsActivity.this.invalidateScrollY = false;
                    int i3 = (DialogsActivity.this.hasHiddenArchive() && DialogsActivity.this.viewPages[0].dialogsType == 0) ? 1 : 0;
                    DialogsRecyclerView dialogsRecyclerView = DialogsActivity.this.viewPages[0].listView;
                    if (DialogsActivity.this.fixScrollYAfterArchiveOpened) {
                        if (!DialogsActivity.this.waitingForScrollFinished) {
                            if (i3 == 0) {
                                DialogsActivity.this.fixScrollYAfterArchiveOpened = false;
                            }
                            if (DialogsActivity.this.fixScrollYAfterArchiveOpened) {
                                RecyclerView.ViewHolder findViewHolderForLayoutPosition = dialogsRecyclerView.findViewHolderForLayoutPosition(0);
                                if (findViewHolderForLayoutPosition == null) {
                                    DialogsActivity.this.fixScrollYAfterArchiveOpened = false;
                                } else if (findViewHolderForLayoutPosition.itemView.getBottom() <= dialogsRecyclerView.getPaddingTop() - AndroidUtilities.dp(81.0f)) {
                                    DialogsActivity.this.fixScrollYAfterArchiveOpened = false;
                                } else if (findViewHolderForLayoutPosition.itemView.getTop() >= dialogsRecyclerView.getPaddingTop()) {
                                    DialogsActivity.this.fixScrollYAfterArchiveOpened = false;
                                }
                                if (DialogsActivity.this.fixScrollYAfterArchiveOpened) {
                                }
                            }
                        }
                        i3 = 0;
                    }
                    RecyclerView.ViewHolder findViewHolderForLayoutPosition2 = dialogsRecyclerView.findViewHolderForLayoutPosition(i3);
                    if (findViewHolderForLayoutPosition2 != null) {
                        float paddingTop = dialogsRecyclerView.getPaddingTop() - findViewHolderForLayoutPosition2.itemView.getY();
                        if (paddingTop >= 0.0f) {
                            float f2 = -paddingTop;
                            float f3 = -DialogsActivity.this.getMaxScrollYOffset();
                            if (f2 < f3) {
                                f2 = f3;
                            } else if (f2 > 0.0f) {
                                f2 = 0.0f;
                            }
                            DialogsActivity.this.setScrollY(f2);
                        } else {
                            DialogsActivity.this.setScrollY(0.0f);
                        }
                    } else {
                        DialogsActivity.this.setScrollY(-dialogsActivity.getMaxScrollYOffset());
                    }
                }
            }
            int actionBarFullHeight = getActionBarFullHeight();
            if (((BaseFragment) DialogsActivity.this).inPreviewMode) {
                actionBarTop = AndroidUtilities.statusBarHeight;
            } else {
                actionBarTop = getActionBarTop();
            }
            int i4 = actionBarTop;
            int i5 = i4 + actionBarFullHeight;
            DialogsActivity.this.rightSlidingDialogContainer.setCurrentTop(i5);
            DialogsActivity dialogsActivity3 = DialogsActivity.this;
            if (dialogsActivity3.whiteActionBar) {
                if (dialogsActivity3.searchAnimationProgress != 1.0f) {
                    if (DialogsActivity.this.searchAnimationProgress == 0.0f && DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                        DialogsActivity.this.filterTabsView.setTranslationY(DialogsActivity.this.scrollYOffset);
                    }
                } else {
                    this.actionBarSearchPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    if (DialogsActivity.this.searchTabsView != null) {
                        DialogsActivity.this.searchTabsView.setTranslationY(0.0f);
                        DialogsActivity.this.searchTabsView.setAlpha(1.0f);
                        if (DialogsActivity.this.filtersView != null) {
                            DialogsActivity.this.filtersView.setTranslationY(0.0f);
                            DialogsActivity.this.filtersView.setAlpha(1.0f);
                        }
                    }
                }
                this.blurBounds.set(0, i4, getMeasuredWidth(), i5);
                drawBlurRect(canvas, 0.0f, this.blurBounds, DialogsActivity.this.searchAnimationProgress == 1.0f ? this.actionBarSearchPaint : DialogsActivity.this.actionBarDefaultPaint, true);
                if (DialogsActivity.this.searchAnimationProgress <= 0.0f || DialogsActivity.this.searchAnimationProgress >= 1.0f) {
                    i = i5;
                } else {
                    this.actionBarSearchPaint.setColor(ColorUtils.blendARGB(Theme.getColor(DialogsActivity.this.folderId == 0 ? Theme.key_actionBarDefault : Theme.key_actionBarDefaultArchived), Theme.getColor(Theme.key_windowBackgroundWhite), DialogsActivity.this.searchAnimationProgress));
                    if (DialogsActivity.this.searchIsShowed || !DialogsActivity.this.searchWasFullyShowed) {
                        canvas.save();
                        canvas.clipRect(0, i4, getMeasuredWidth(), i5);
                        i = i5;
                        drawBlurCircle(canvas, 0.0f, getMeasuredWidth() - AndroidUtilities.dp(24.0f), (((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ((((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() - i2) / 2.0f), getMeasuredWidth() * 1.3f * DialogsActivity.this.searchAnimationProgress, this.actionBarSearchPaint, true);
                        canvas.restore();
                    } else {
                        this.blurBounds.set(0, i4, getMeasuredWidth(), i5);
                        drawBlurRect(canvas, 0.0f, this.blurBounds, this.actionBarSearchPaint, true);
                        i = i5;
                    }
                    if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                        DialogsActivity.this.filterTabsView.setTranslationY(actionBarFullHeight - (((BaseFragment) DialogsActivity.this).actionBar.getHeight() + DialogsActivity.this.filterTabsView.getMeasuredHeight()));
                    }
                    if (DialogsActivity.this.searchTabsView != null) {
                        float measuredHeight = (i - DialogsActivity.this.searchTabsView.getMeasuredHeight()) - DialogsActivity.this.searchTabsView.getTop();
                        float f4 = DialogsActivity.this.searchAnimationTabsDelayedCrossfade ? DialogsActivity.this.searchAnimationProgress < 0.5f ? 0.0f : (DialogsActivity.this.searchAnimationProgress - 0.5f) / 0.5f : DialogsActivity.this.searchAnimationProgress;
                        DialogsActivity.this.searchTabsView.setTranslationY(measuredHeight);
                        DialogsActivity.this.searchTabsView.setAlpha(f4);
                        if (DialogsActivity.this.filtersView != null) {
                            DialogsActivity.this.filtersView.setTranslationY(measuredHeight);
                            DialogsActivity.this.filtersView.setAlpha(f4);
                        }
                    }
                }
            } else {
                i = i5;
                if (!((BaseFragment) dialogsActivity3).inPreviewMode) {
                    if (DialogsActivity.this.progressToActionMode > 0.0f) {
                        this.actionBarSearchPaint.setColor(ColorUtils.blendARGB(Theme.getColor(DialogsActivity.this.folderId == 0 ? Theme.key_actionBarDefault : Theme.key_actionBarDefaultArchived), Theme.getColor(Theme.key_windowBackgroundWhite), DialogsActivity.this.progressToActionMode));
                        this.blurBounds.set(0, i4, getMeasuredWidth(), i);
                        drawBlurRect(canvas, 0.0f, this.blurBounds, this.actionBarSearchPaint, true);
                    } else {
                        this.blurBounds.set(0, i4, getMeasuredWidth(), i);
                        drawBlurRect(canvas, 0.0f, this.blurBounds, DialogsActivity.this.actionBarDefaultPaint, true);
                    }
                }
            }
            DialogsActivity.this.tabsYOffset = 0.0f;
            DialogsActivity.this.storiesYOffset = 0.0f;
            DialogsActivity dialogsActivity4 = DialogsActivity.this;
            if (dialogsActivity4.hasStories) {
                DialogsActivity.access$4724(dialogsActivity4, Math.min(AndroidUtilities.dp(81.0f) + DialogsActivity.this.scrollYOffset, DialogsActivity.this.progressToActionMode * AndroidUtilities.dp(81.0f)));
                DialogsActivity dialogsActivity5 = DialogsActivity.this;
                dialogsActivity5.storiesYOffset = dialogsActivity5.tabsYOffset;
            }
            if (DialogsActivity.this.filtersTabAnimator == null && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                    DialogsActivity.this.filterTabsView.setTranslationY(DialogsActivity.this.scrollYOffset + DialogsActivity.this.tabsYOffset + DialogsActivity.this.storiesOverscroll);
                    DialogsActivity.this.filterTabsView.setAlpha(1.0f);
                }
                clamp = 1.0f;
            } else {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                float f5 = (rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment()) ? 0.0f : DialogsActivity.this.rightSlidingDialogContainer.openedProgress;
                DialogsActivity dialogsActivity6 = DialogsActivity.this;
                if (dialogsActivity6.hasStories) {
                    DialogsActivity.access$4724(dialogsActivity6, (dialogsActivity6.getMaxScrollYOffset() + DialogsActivity.this.scrollYOffset) * f5);
                    DialogsActivity dialogsActivity7 = DialogsActivity.this;
                    dialogsActivity7.storiesYOffset = dialogsActivity7.tabsYOffset;
                }
                clamp = DialogsActivity.this.dialogStoriesCellVisible ? 1.0f - Utilities.clamp(f5 / 0.5f, 1.0f, 0.0f) : 1.0f;
                if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                    DialogsActivity dialogsActivity8 = DialogsActivity.this;
                    DialogsActivity.access$4724(dialogsActivity8, (1.0f - dialogsActivity8.filterTabsProgress) * DialogsActivity.this.filterTabsView.getMeasuredHeight());
                    DialogsActivity.this.filterTabsView.setTranslationY(DialogsActivity.this.scrollYOffset + DialogsActivity.this.tabsYOffset);
                    DialogsActivity.this.filterTabsView.setAlpha(DialogsActivity.this.filterTabsProgress);
                }
                if (DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                    if (DialogsActivity.this.rightFragmentTransitionInProgress) {
                        float f6 = DialogsActivity.this.rightFragmentTransitionIsOpen ? 0.0f : DialogsActivity.this.scrollYOffset;
                        f = -AndroidUtilities.lerp(-f6, f6, DialogsActivity.this.rightSlidingDialogContainer.openedProgress);
                    } else {
                        f = 0.0f;
                    }
                    DialogsActivity.this.viewPages[0].setTranslationY(((-(1.0f - DialogsActivity.this.filterTabsProgress)) * DialogsActivity.this.filterTabsMoveFrom) + f);
                } else {
                    DialogsActivity.this.viewPages[0].setTranslationY(DialogsActivity.this.getActionBarMoveFrom(false) - AndroidUtilities.lerp(DialogsActivity.this.getActionBarMoveFrom(false), DialogsActivity.this.filterTabsMoveFrom, 1.0f - DialogsActivity.this.filterTabsProgress));
                }
            }
            DialogsActivity.this.updateContextViewPosition();
            DialogsActivity.this.updateStoriesViewAlpha(clamp);
            super.dispatchDraw(canvas);
            DialogsActivity dialogsActivity9 = DialogsActivity.this;
            if (dialogsActivity9.whiteActionBar && dialogsActivity9.searchAnimationProgress > 0.0f && DialogsActivity.this.searchAnimationProgress < 1.0f && DialogsActivity.this.searchTabsView != null) {
                this.windowBackgroundPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                this.windowBackgroundPaint.setAlpha((int) (paint.getAlpha() * DialogsActivity.this.searchAnimationProgress));
                float measuredHeight2 = i4 + ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() + DialogsActivity.this.searchTabsView.getMeasuredHeight();
                float f7 = i;
                if (measuredHeight2 > f7) {
                    canvas.drawRect(0.0f, f7, getMeasuredWidth(), measuredHeight2, this.windowBackgroundPaint);
                }
            }
            if (((BaseFragment) DialogsActivity.this).parentLayout != null && ((BaseFragment) DialogsActivity.this).actionBar != null && !((BaseFragment) DialogsActivity.this).actionBar.getCastShadows()) {
                ((BaseFragment) DialogsActivity.this).parentLayout.drawHeaderShadow(canvas, (int) ((1.0f - DialogsActivity.this.searchAnimationProgress) * 255.0f), i);
                if (DialogsActivity.this.searchAnimationProgress > 0.0f) {
                    if (DialogsActivity.this.searchAnimationProgress < 1.0f) {
                        int alpha = Theme.dividerPaint.getAlpha();
                        Theme.dividerPaint.setAlpha((int) (alpha * DialogsActivity.this.searchAnimationProgress));
                        float f8 = i;
                        canvas.drawLine(0.0f, f8, getMeasuredWidth(), f8, Theme.dividerPaint);
                        Theme.dividerPaint.setAlpha(alpha);
                    } else {
                        float f9 = i;
                        canvas.drawLine(0.0f, f9, getMeasuredWidth(), f9, Theme.dividerPaint);
                    }
                }
            }
            if (((BaseFragment) DialogsActivity.this).parentLayout != null && DialogsActivity.this.authHintCell != null && DialogsActivity.this.authHintCell.getVisibility() == 0) {
                ((BaseFragment) DialogsActivity.this).parentLayout.drawHeaderShadow(canvas, (int) ((1.0f - DialogsActivity.this.searchAnimationProgress) * 255.0f * DialogsActivity.this.authHintCell.getAlpha()), (int) (DialogsActivity.this.authHintCell.getBottom() + DialogsActivity.this.authHintCell.getTranslationY()));
            }
            if (DialogsActivity.this.fragmentContextView != null && DialogsActivity.this.fragmentContextView.isCallStyle()) {
                canvas.save();
                canvas.translate(DialogsActivity.this.fragmentContextView.getX(), DialogsActivity.this.fragmentContextView.getY());
                DialogsActivity dialogsActivity10 = DialogsActivity.this;
                float f10 = dialogsActivity10.slideFragmentProgress;
                if (f10 != 1.0f) {
                    if (dialogsActivity10.slideFragmentLite) {
                        canvas.translate((dialogsActivity10.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                    } else {
                        float f11 = 1.0f - ((1.0f - f10) * 0.05f);
                        canvas.translate((dialogsActivity10.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                        canvas.scale(f11, 1.0f, DialogsActivity.this.isDrawerTransition ? getMeasuredWidth() : 0.0f, DialogsActivity.this.fragmentContextView.getY());
                    }
                }
                DialogsActivity.this.fragmentContextView.setDrawOverlay(true);
                DialogsActivity.this.fragmentContextView.draw(canvas);
                DialogsActivity.this.fragmentContextView.setDrawOverlay(false);
                canvas.restore();
            }
            if (DialogsActivity.this.blurredView != null && DialogsActivity.this.blurredView.getVisibility() == 0) {
                if (DialogsActivity.this.blurredView.getAlpha() != 1.0f) {
                    if (DialogsActivity.this.blurredView.getAlpha() != 0.0f) {
                        canvas.saveLayerAlpha(DialogsActivity.this.blurredView.getLeft(), DialogsActivity.this.blurredView.getTop(), DialogsActivity.this.blurredView.getRight(), DialogsActivity.this.blurredView.getBottom(), (int) (DialogsActivity.this.blurredView.getAlpha() * 255.0f), 31);
                        canvas.translate(DialogsActivity.this.blurredView.getLeft(), DialogsActivity.this.blurredView.getTop());
                        DialogsActivity.this.blurredView.draw(canvas);
                        canvas.restore();
                    }
                } else {
                    DialogsActivity.this.blurredView.draw(canvas);
                }
            }
            DialogsActivity.this.wasDrawn = true;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            boolean z = size2 > size;
            setMeasuredDimension(size, size2);
            int paddingTop = size2 - getPaddingTop();
            if (DialogsActivity.this.doneItem != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) DialogsActivity.this.doneItem.getLayoutParams();
                layoutParams.topMargin = ((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0;
                layoutParams.height = ActionBar.getCurrentActionBarHeight();
            }
            measureChildWithMargins(((BaseFragment) DialogsActivity.this).actionBar, i, 0, i2, 0);
            int measureKeyboardHeight = measureKeyboardHeight();
            int childCount = getChildCount();
            if (DialogsActivity.this.commentView != null) {
                measureChildWithMargins(DialogsActivity.this.commentView, i, 0, i2, 0);
                Object tag = DialogsActivity.this.commentView.getTag();
                if (tag != null && tag.equals(2)) {
                    if (measureKeyboardHeight <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow) {
                        paddingTop -= DialogsActivity.this.commentView.getEmojiPadding();
                    }
                    this.inputFieldHeight = DialogsActivity.this.commentView.getMeasuredHeight();
                } else {
                    this.inputFieldHeight = 0;
                }
                if (DialogsActivity.this.commentView.isPopupShowing()) {
                    DialogsActivity.this.fragmentView.setTranslationY(0.0f);
                    for (int i3 = 0; i3 < DialogsActivity.this.viewPages.length; i3++) {
                        if (DialogsActivity.this.viewPages[i3] != null) {
                            DialogsActivity.this.viewPages[i3].setTranslationY(0.0f);
                        }
                    }
                    if (!DialogsActivity.this.onlySelect) {
                        ((BaseFragment) DialogsActivity.this).actionBar.setTranslationY(0.0f);
                        if (DialogsActivity.this.topBulletin != null) {
                            DialogsActivity.this.topBulletin.updatePosition();
                        }
                    }
                    if (DialogsActivity.this.searchViewPager != null) {
                        DialogsActivity.this.searchViewPager.setTranslationY(DialogsActivity.this.searchViewPagerTranslationY);
                    }
                }
            }
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                if (childAt != null && childAt.getVisibility() != 8 && childAt != DialogsActivity.this.commentView && childAt != ((BaseFragment) DialogsActivity.this).actionBar) {
                    if (childAt instanceof DatabaseMigrationHint) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), (((View.MeasureSpec.getSize(i2) + measureKeyboardHeight) - this.inputFieldHeight) + AndroidUtilities.dp(2.0f)) - ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight()), 1073741824));
                    } else if (!(childAt instanceof ViewPage)) {
                        if (childAt == DialogsActivity.this.searchViewPager) {
                            DialogsActivity.this.searchViewPager.setTranslationY(DialogsActivity.this.searchViewPagerTranslationY);
                            childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), ((((View.MeasureSpec.getSize(i2) + measureKeyboardHeight) - this.inputFieldHeight) + AndroidUtilities.dp(2.0f)) - ((!DialogsActivity.this.onlySelect || DialogsActivity.this.initialDialogsType == 3) ? ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() : 0)) - DialogsActivity.this.topPadding) - (DialogsActivity.this.searchTabsView == null ? 0 : AndroidUtilities.dp(44.0f)), 1073741824));
                            childAt.setPivotX(childAt.getMeasuredWidth() / 2);
                        } else if (DialogsActivity.this.commentView != null && DialogsActivity.this.commentView.isPopupView(childAt)) {
                            if (AndroidUtilities.isInMultiwindow) {
                                if (AndroidUtilities.isTablet()) {
                                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0f), ((paddingTop - this.inputFieldHeight) - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                                } else {
                                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(((paddingTop - this.inputFieldHeight) - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                                }
                            } else {
                                childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                            }
                        } else {
                            if (childAt == DialogsActivity.this.rightSlidingDialogContainer) {
                                int size3 = View.MeasureSpec.getSize(i2);
                                DialogsActivity dialogsActivity = DialogsActivity.this;
                                int i5 = (dialogsActivity.isSlideBackTransition || dialogsActivity.isDrawerTransition) ? (int) (size3 * 0.05f) : 0;
                                dialogsActivity.rightSlidingDialogContainer.setTransitionPaddingBottom(i5);
                                childAt.measure(i, View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), size3 + i5), 1073741824));
                            } else {
                                measureChildWithMargins(childAt, i, 0, i2, 0);
                            }
                        }
                    } else {
                        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                        int dp = ((paddingTop - this.inputFieldHeight) + AndroidUtilities.dp(2.0f)) - DialogsActivity.this.topPadding;
                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                        if (dialogsActivity2.hasStories || (dialogsActivity2.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0)) {
                            if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                                dp -= AndroidUtilities.dp(44.0f);
                            }
                            if (DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                                if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                                    dp += AndroidUtilities.dp(44.0f);
                                }
                                if (DialogsActivity.this.hasStories) {
                                    dp += AndroidUtilities.dp(81.0f);
                                }
                                if (DialogsActivity.this.dialogsHintCell != null && DialogsActivity.this.dialogsHintCell.getVisibility() == 0) {
                                    dp += DialogsActivity.this.dialogsHintCell.getMeasuredHeight();
                                }
                                if (DialogsActivity.this.authHintCell != null && DialogsActivity.this.authHintCell.getVisibility() == 0) {
                                    dp += DialogsActivity.this.authHintCell.getMeasuredHeight();
                                }
                            }
                        } else if (!DialogsActivity.this.onlySelect || DialogsActivity.this.initialDialogsType == 3) {
                            dp -= ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                        }
                        if (DialogsActivity.this.dialogsHintCell != null) {
                            dp -= DialogsActivity.this.dialogsHintCell.height();
                        }
                        int i6 = dp + DialogsActivity.this.actionModeAdditionalHeight;
                        if (DialogsActivity.this.filtersTabAnimator != null) {
                            DialogsActivity dialogsActivity3 = DialogsActivity.this;
                            if (dialogsActivity3.hasStories || (dialogsActivity3.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0)) {
                                i6 = (int) (i6 + DialogsActivity.this.filterTabsMoveFrom);
                                DialogsActivity dialogsActivity4 = DialogsActivity.this;
                                int i7 = (!dialogsActivity4.isSlideBackTransition || dialogsActivity4.isDrawerTransition) ? (int) (i6 * 0.05f) : 0;
                                childAt.setPadding(childAt.getPaddingLeft(), childAt.getPaddingTop(), childAt.getPaddingRight(), i7);
                                childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), i6 + i7), 1073741824));
                                childAt.setPivotX(childAt.getMeasuredWidth() / 2);
                            }
                        }
                        childAt.setTranslationY(0.0f);
                        DialogsActivity dialogsActivity42 = DialogsActivity.this;
                        if (dialogsActivity42.isSlideBackTransition) {
                        }
                        childAt.setPadding(childAt.getPaddingLeft(), childAt.getPaddingTop(), childAt.getPaddingRight(), i7);
                        childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), i6 + i7), 1073741824));
                        childAt.setPivotX(childAt.getMeasuredWidth() / 2);
                    }
                }
            }
            if (z != this.wasPortrait) {
                post(new Runnable() { // from class: org.telegram.ui.DialogsActivity$ContentView$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.ContentView.this.lambda$onMeasure$0();
                    }
                });
                this.wasPortrait = z;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$0() {
            if (DialogsActivity.this.selectAnimatedEmojiDialog != null) {
                DialogsActivity.this.selectAnimatedEmojiDialog.dismiss();
                DialogsActivity.this.selectAnimatedEmojiDialog = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Removed duplicated region for block: B:123:0x0231  */
        /* JADX WARN: Removed duplicated region for block: B:128:0x0244  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00a3  */
        /* JADX WARN: Removed duplicated region for block: B:45:0x00be  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x00d3  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x010f  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0127  */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            DialogsActivity dialogsActivity;
            DialogStoriesCell dialogStoriesCell;
            DialogsActivity dialogsActivity2;
            int measuredHeight;
            int childCount = getChildCount();
            Object tag = DialogsActivity.this.commentView != null ? DialogsActivity.this.commentView.getTag() : null;
            int measureKeyboardHeight = measureKeyboardHeight();
            int i11 = 2;
            int i12 = 0;
            int emojiPadding = (tag == null || !tag.equals(2) || measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow) ? 0 : DialogsActivity.this.commentView.getEmojiPadding();
            setBottomClip(emojiPadding);
            DialogsActivity dialogsActivity3 = DialogsActivity.this;
            dialogsActivity3.lastMeasuredTopPadding = dialogsActivity3.topPadding;
            int i13 = -1;
            while (i13 < childCount) {
                View childAt = i13 == -1 ? DialogsActivity.this.commentView : getChildAt(i13);
                if (childAt != null && childAt.getVisibility() != 8) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight2 = childAt.getMeasuredHeight();
                    int i14 = layoutParams.gravity;
                    if (i14 == -1) {
                        i14 = 51;
                    }
                    int i15 = i14 & 7;
                    int i16 = i14 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
                    int i17 = i15 & 7;
                    if (i17 == 1) {
                        i5 = (((i3 - i) - measuredWidth) / i11) + layoutParams.leftMargin;
                        i6 = layoutParams.rightMargin;
                    } else if (i17 == 5) {
                        i5 = i3 - measuredWidth;
                        i6 = layoutParams.rightMargin;
                    } else {
                        i7 = layoutParams.leftMargin;
                        if (i16 == 16) {
                            if (i16 == 48) {
                                i10 = layoutParams.topMargin + getPaddingTop();
                            } else if (i16 == 80) {
                                i8 = ((i4 - emojiPadding) - i2) - measuredHeight2;
                                i9 = layoutParams.bottomMargin;
                            } else {
                                i10 = layoutParams.topMargin;
                            }
                            if (DialogsActivity.this.commentView != null || !DialogsActivity.this.commentView.isPopupView(childAt)) {
                                if (childAt != DialogsActivity.this.filterTabsView && childAt != DialogsActivity.this.searchTabsView && childAt != DialogsActivity.this.filtersView) {
                                    dialogsActivity2 = DialogsActivity.this;
                                    if (childAt != dialogsActivity2.dialogStoriesCell) {
                                        if (childAt == dialogsActivity2.searchViewPager) {
                                            i10 = ((!DialogsActivity.this.onlySelect || DialogsActivity.this.initialDialogsType == 3) ? ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() : 0) + DialogsActivity.this.topPadding;
                                            measuredHeight = DialogsActivity.this.searchTabsView == null ? 0 : AndroidUtilities.dp(44.0f);
                                        } else {
                                            if (childAt instanceof DatabaseMigrationHint) {
                                                i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                            } else if (childAt instanceof ViewPage) {
                                                if (!DialogsActivity.this.onlySelect || DialogsActivity.this.initialDialogsType == 3) {
                                                    DialogsActivity dialogsActivity4 = DialogsActivity.this;
                                                    if (!dialogsActivity4.hasStories && (dialogsActivity4.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0)) {
                                                        i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                                    } else {
                                                        i10 = (DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) ? 0 : AndroidUtilities.dp(44.0f) + i12;
                                                    }
                                                }
                                                i10 += DialogsActivity.this.topPadding;
                                                if (DialogsActivity.this.dialogsHintCell != null) {
                                                    measuredHeight = DialogsActivity.this.dialogsHintCell.height();
                                                }
                                            } else if (childAt == DialogsActivity.this.dialogsHintCell || (childAt instanceof FragmentContextView) || childAt == DialogsActivity.this.authHintCell) {
                                                measuredHeight = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                            } else {
                                                DialogStoriesCell dialogStoriesCell2 = DialogsActivity.this.dialogStoriesCell;
                                                if (dialogStoriesCell2 == null || dialogStoriesCell2.getPremiumHint() != childAt) {
                                                    if (childAt == DialogsActivity.this.floatingButtonContainer && DialogsActivity.this.selectAnimatedEmojiDialog != null) {
                                                        i10 += measureKeyboardHeight;
                                                    }
                                                }
                                            }
                                            childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                                        }
                                        i10 += measuredHeight;
                                        childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                                    }
                                }
                                i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                dialogsActivity = DialogsActivity.this;
                                if (dialogsActivity.hasStories && childAt == dialogsActivity.filterTabsView) {
                                    i10 += AndroidUtilities.dp(81.0f);
                                }
                                dialogStoriesCell = DialogsActivity.this.dialogStoriesCell;
                                if (childAt == dialogStoriesCell && dialogStoriesCell.getPremiumHint() != null) {
                                    DialogsActivity.this.dialogStoriesCell.getPremiumHint().layout(i7, (i10 - AndroidUtilities.dp(54.0f)) + measuredHeight2, i7 + measuredWidth, (i10 - AndroidUtilities.dp(54.0f)) + measuredHeight2 + DialogsActivity.this.dialogStoriesCell.getPremiumHint().getMeasuredHeight());
                                }
                                childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                            } else if (AndroidUtilities.isInMultiwindow) {
                                i10 = DialogsActivity.this.commentView.getTop() - childAt.getMeasuredHeight();
                                measuredHeight = AndroidUtilities.dp(1.0f);
                                i10 += measuredHeight;
                                childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                            } else {
                                i10 = DialogsActivity.this.commentView.getBottom();
                                childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                            }
                        } else {
                            i8 = ((((i4 - emojiPadding) - i2) - measuredHeight2) / i11) + layoutParams.topMargin;
                            i9 = layoutParams.bottomMargin;
                        }
                        i10 = i8 - i9;
                        if (DialogsActivity.this.commentView != null) {
                        }
                        if (childAt != DialogsActivity.this.filterTabsView) {
                            dialogsActivity2 = DialogsActivity.this;
                            if (childAt != dialogsActivity2.dialogStoriesCell) {
                            }
                        }
                        i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                        dialogsActivity = DialogsActivity.this;
                        if (dialogsActivity.hasStories) {
                            i10 += AndroidUtilities.dp(81.0f);
                        }
                        dialogStoriesCell = DialogsActivity.this.dialogStoriesCell;
                        if (childAt == dialogStoriesCell) {
                            DialogsActivity.this.dialogStoriesCell.getPremiumHint().layout(i7, (i10 - AndroidUtilities.dp(54.0f)) + measuredHeight2, i7 + measuredWidth, (i10 - AndroidUtilities.dp(54.0f)) + measuredHeight2 + DialogsActivity.this.dialogStoriesCell.getPremiumHint().getMeasuredHeight());
                        }
                        childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                    }
                    i7 = i5 - i6;
                    if (i16 == 16) {
                    }
                    i10 = i8 - i9;
                    if (DialogsActivity.this.commentView != null) {
                    }
                    if (childAt != DialogsActivity.this.filterTabsView) {
                    }
                    i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                    dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories) {
                    }
                    dialogStoriesCell = DialogsActivity.this.dialogStoriesCell;
                    if (childAt == dialogStoriesCell) {
                    }
                    childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                }
                i13++;
                i11 = 2;
                i12 = 0;
            }
            if (DialogsActivity.this.searchViewPager != null) {
                DialogsActivity.this.searchViewPager.setKeyboardHeight(measureKeyboardHeight);
            }
            notifyHeightChanged();
            DialogsActivity.this.updateContextViewPosition();
            DialogsActivity.this.updateCommentView();
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if ((actionMasked == 1 || actionMasked == 3) && ((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed()) {
                DialogsActivity.this.allowMoving = true;
            }
            if (checkTabsAnimationInProgress()) {
                return true;
            }
            return (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.isAnimatingIndicator()) || onTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z) {
            if (DialogsActivity.this.maybeStartTracking && !DialogsActivity.this.startedTracking) {
                onTouchEvent(null);
            }
            super.requestDisallowInterceptTouchEvent(z);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            float f;
            float f2;
            float measuredWidth;
            int measuredWidth2;
            if (((BaseFragment) DialogsActivity.this).parentLayout == null || DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.isEditing() || DialogsActivity.this.searching || DialogsActivity.this.rightSlidingDialogContainer.hasFragment() || ((BaseFragment) DialogsActivity.this).parentLayout.checkTransitionAnimation() || ((BaseFragment) DialogsActivity.this).parentLayout.isInPreviewMode() || ((BaseFragment) DialogsActivity.this).parentLayout.isPreviewOpenAnimationInProgress() || ((((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer() != null && ((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer().isDrawerOpened()) || !((motionEvent == null || DialogsActivity.this.startedTracking || motionEvent.getY() > getActionBarTop() + getActionBarFullHeight()) && (DialogsActivity.this.initialDialogsType == 3 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 5 || (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 2 && DialogsActivity.this.viewPages[0] != null && (DialogsActivity.this.viewPages[0].dialogsAdapter.getDialogsType() == 7 || DialogsActivity.this.viewPages[0].dialogsAdapter.getDialogsType() == 8)))))) {
                return false;
            }
            if (motionEvent != null) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.addMovement(motionEvent);
            }
            if (motionEvent != null && motionEvent.getAction() == 0 && checkTabsAnimationInProgress()) {
                DialogsActivity.this.startedTracking = true;
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                this.startedTrackingX = (int) motionEvent.getX();
                if (((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer() != null) {
                    ((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer().setAllowOpenDrawerBySwipe(false);
                }
                if (DialogsActivity.this.animatingForward) {
                    if (this.startedTrackingX >= DialogsActivity.this.viewPages[0].getMeasuredWidth() + DialogsActivity.this.viewPages[0].getTranslationX()) {
                        ViewPage viewPage = DialogsActivity.this.viewPages[0];
                        DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                        DialogsActivity.this.viewPages[1] = viewPage;
                        DialogsActivity.this.animatingForward = false;
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        dialogsActivity.additionalOffset = dialogsActivity.viewPages[0].getTranslationX();
                        DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[0].selectedType, 1.0f);
                        DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, DialogsActivity.this.additionalOffset / DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        DialogsActivity.this.switchToCurrentSelectedMode(true);
                        DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                        DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                    } else {
                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                        dialogsActivity2.additionalOffset = dialogsActivity2.viewPages[0].getTranslationX();
                    }
                } else if (this.startedTrackingX < DialogsActivity.this.viewPages[1].getMeasuredWidth() + DialogsActivity.this.viewPages[1].getTranslationX()) {
                    ViewPage viewPage2 = DialogsActivity.this.viewPages[0];
                    DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                    DialogsActivity.this.viewPages[1] = viewPage2;
                    DialogsActivity.this.animatingForward = true;
                    DialogsActivity dialogsActivity3 = DialogsActivity.this;
                    dialogsActivity3.additionalOffset = dialogsActivity3.viewPages[0].getTranslationX();
                    DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[0].selectedType, 1.0f);
                    DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, (-DialogsActivity.this.additionalOffset) / DialogsActivity.this.viewPages[0].getMeasuredWidth());
                    DialogsActivity.this.switchToCurrentSelectedMode(true);
                    DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                    DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                } else {
                    DialogsActivity dialogsActivity4 = DialogsActivity.this;
                    dialogsActivity4.additionalOffset = dialogsActivity4.viewPages[0].getTranslationX();
                }
                DialogsActivity.this.tabsAnimation.removeAllListeners();
                DialogsActivity.this.tabsAnimation.cancel();
                DialogsActivity.this.tabsAnimationInProgress = false;
            } else if (motionEvent != null && motionEvent.getAction() == 0) {
                DialogsActivity.this.additionalOffset = 0.0f;
            }
            if (motionEvent != null && motionEvent.getAction() == 0 && !DialogsActivity.this.startedTracking && !DialogsActivity.this.maybeStartTracking && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                DialogsActivity.this.maybeStartTracking = true;
                this.startedTrackingX = (int) motionEvent.getX();
                this.startedTrackingY = (int) motionEvent.getY();
                this.velocityTracker.clear();
            } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                int x = (int) ((motionEvent.getX() - this.startedTrackingX) + DialogsActivity.this.additionalOffset);
                int abs = Math.abs(((int) motionEvent.getY()) - this.startedTrackingY);
                if (DialogsActivity.this.startedTracking && ((DialogsActivity.this.animatingForward && x > 0) || (!DialogsActivity.this.animatingForward && x < 0))) {
                    if (!prepareForMoving(motionEvent, x < 0)) {
                        DialogsActivity.this.maybeStartTracking = true;
                        DialogsActivity.this.startedTracking = false;
                        DialogsActivity.this.viewPages[0].setTranslationX(0.0f);
                        DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.animatingForward ? DialogsActivity.this.viewPages[0].getMeasuredWidth() : -DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, 0.0f);
                    }
                }
                if (!DialogsActivity.this.maybeStartTracking || DialogsActivity.this.startedTracking) {
                    if (DialogsActivity.this.startedTracking) {
                        DialogsActivity.this.viewPages[0].setTranslationX(x);
                        if (DialogsActivity.this.animatingForward) {
                            DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() + x);
                        } else {
                            DialogsActivity.this.viewPages[1].setTranslationX(x - DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        }
                        float abs2 = Math.abs(x) / DialogsActivity.this.viewPages[0].getMeasuredWidth();
                        if (!DialogsActivity.this.viewPages[1].isLocked || abs2 <= 0.3f) {
                            DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, abs2);
                        } else {
                            dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0));
                            DialogsActivity.this.filterTabsView.shakeLock(DialogsActivity.this.viewPages[1].selectedType);
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$ContentView$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.ContentView.this.lambda$onTouchEvent$1();
                                }
                            }, 200L);
                            return false;
                        }
                    }
                } else {
                    float pixelsInCM = AndroidUtilities.getPixelsInCM(0.3f, true);
                    int x2 = (int) (motionEvent.getX() - this.startedTrackingX);
                    if (Math.abs(x2) >= pixelsInCM && Math.abs(x2) > abs) {
                        prepareForMoving(motionEvent, x < 0);
                    }
                }
            } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                this.velocityTracker.computeCurrentVelocity(1000, DialogsActivity.this.maximumVelocity);
                if (motionEvent == null || motionEvent.getAction() == 3) {
                    f = 0.0f;
                    f2 = 0.0f;
                } else {
                    f = this.velocityTracker.getXVelocity();
                    f2 = this.velocityTracker.getYVelocity();
                    if (!DialogsActivity.this.startedTracking && Math.abs(f) >= 3000.0f && Math.abs(f) > Math.abs(f2)) {
                        prepareForMoving(motionEvent, f < 0.0f);
                    }
                }
                if (DialogsActivity.this.startedTracking) {
                    float x3 = DialogsActivity.this.viewPages[0].getX();
                    DialogsActivity.this.tabsAnimation = new AnimatorSet();
                    if (DialogsActivity.this.viewPages[1].isLocked) {
                        DialogsActivity.this.backAnimation = true;
                    } else if (DialogsActivity.this.additionalOffset != 0.0f) {
                        if (Math.abs(f) <= 1500.0f) {
                            if (DialogsActivity.this.animatingForward) {
                                DialogsActivity dialogsActivity5 = DialogsActivity.this;
                                dialogsActivity5.backAnimation = dialogsActivity5.viewPages[1].getX() > ((float) (DialogsActivity.this.viewPages[0].getMeasuredWidth() >> 1));
                            } else {
                                DialogsActivity dialogsActivity6 = DialogsActivity.this;
                                dialogsActivity6.backAnimation = dialogsActivity6.viewPages[0].getX() < ((float) (DialogsActivity.this.viewPages[0].getMeasuredWidth() >> 1));
                            }
                        } else {
                            DialogsActivity dialogsActivity7 = DialogsActivity.this;
                            dialogsActivity7.backAnimation = !dialogsActivity7.animatingForward ? f >= 0.0f : f <= 0.0f;
                        }
                    } else {
                        DialogsActivity.this.backAnimation = Math.abs(x3) < ((float) DialogsActivity.this.viewPages[0].getMeasuredWidth()) / 3.0f && (Math.abs(f) < 3500.0f || Math.abs(f) < Math.abs(f2));
                    }
                    if (!DialogsActivity.this.backAnimation) {
                        measuredWidth = DialogsActivity.this.viewPages[0].getMeasuredWidth() - Math.abs(x3);
                        if (DialogsActivity.this.animatingForward) {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, -DialogsActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, 0.0f));
                        } else {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, DialogsActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, 0.0f));
                        }
                    } else {
                        measuredWidth = Math.abs(x3);
                        if (DialogsActivity.this.animatingForward) {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, DialogsActivity.this.viewPages[1].getMeasuredWidth()));
                        } else {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, -DialogsActivity.this.viewPages[1].getMeasuredWidth()));
                        }
                    }
                    DialogsActivity.this.tabsAnimation.setInterpolator(DialogsActivity.interpolator);
                    int measuredWidth3 = getMeasuredWidth();
                    float f3 = measuredWidth3 / 2;
                    float distanceInfluenceForSnapDuration = f3 + (AndroidUtilities.distanceInfluenceForSnapDuration(Math.min(1.0f, (measuredWidth * 1.0f) / measuredWidth3)) * f3);
                    float abs3 = Math.abs(f);
                    if (abs3 > 0.0f) {
                        measuredWidth2 = Math.round(Math.abs(distanceInfluenceForSnapDuration / abs3) * 1000.0f) * 4;
                    } else {
                        measuredWidth2 = (int) (((measuredWidth / getMeasuredWidth()) + 1.0f) * 100.0f);
                    }
                    DialogsActivity.this.tabsAnimation.setDuration(Math.max((int) ImageReceiver.DEFAULT_CROSSFADE_DURATION, Math.min(measuredWidth2, 600)));
                    DialogsActivity.this.tabsAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.ContentView.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            DialogsActivity.this.tabsAnimation = null;
                            if (!DialogsActivity.this.backAnimation) {
                                ViewPage viewPage3 = DialogsActivity.this.viewPages[0];
                                DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                                DialogsActivity.this.viewPages[1] = viewPage3;
                                DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[0].selectedType, 1.0f);
                                DialogsActivity.this.updateCounters(false);
                                DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                                DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                            }
                            DialogsActivity dialogsActivity8 = DialogsActivity.this;
                            dialogsActivity8.isFirstTab = dialogsActivity8.viewPages[0].selectedType == DialogsActivity.this.filterTabsView.getFirstTabId();
                            DialogsActivity.this.updateDrawerSwipeEnabled();
                            DialogsActivity.this.viewPages[1].setVisibility(8);
                            DialogsActivity.this.showScrollbars(true);
                            DialogsActivity.this.tabsAnimationInProgress = false;
                            DialogsActivity.this.maybeStartTracking = false;
                            ((BaseFragment) DialogsActivity.this).actionBar.setEnabled(true);
                            DialogsActivity.this.filterTabsView.setEnabled(true);
                            DialogsActivity dialogsActivity9 = DialogsActivity.this;
                            dialogsActivity9.checkListLoad(dialogsActivity9.viewPages[0]);
                        }
                    });
                    DialogsActivity.this.tabsAnimation.start();
                    DialogsActivity.this.tabsAnimationInProgress = true;
                    DialogsActivity.this.startedTracking = false;
                } else {
                    DialogsActivity dialogsActivity8 = DialogsActivity.this;
                    dialogsActivity8.isFirstTab = dialogsActivity8.viewPages[0].selectedType == DialogsActivity.this.filterTabsView.getFirstTabId();
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                    DialogsActivity.this.maybeStartTracking = false;
                    ((BaseFragment) DialogsActivity.this).actionBar.setEnabled(true);
                    DialogsActivity.this.filterTabsView.setEnabled(true);
                }
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
            }
            return DialogsActivity.this.startedTracking;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$1() {
            DialogsActivity.this.showDialog(new LimitReachedBottomSheet(DialogsActivity.this, getContext(), 3, ((BaseFragment) DialogsActivity.this).currentAccount, null));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        public void drawList(Canvas canvas, boolean z, ArrayList<SizeNotifierFrameLayout.IViewWithInvalidateCallback> arrayList) {
            if (DialogsActivity.this.searchIsShowed) {
                if (DialogsActivity.this.searchViewPager == null || DialogsActivity.this.searchViewPager.getVisibility() != 0) {
                    return;
                }
                DialogsActivity.this.searchViewPager.drawForBlur(canvas);
                return;
            }
            for (int i = 0; i < DialogsActivity.this.viewPages.length; i++) {
                if (DialogsActivity.this.viewPages[i] != null && DialogsActivity.this.viewPages[i].getVisibility() == 0) {
                    for (int i2 = 0; i2 < DialogsActivity.this.viewPages[i].listView.getChildCount(); i2++) {
                        View childAt = DialogsActivity.this.viewPages[i].listView.getChildAt(i2);
                        if (childAt instanceof BaseCell) {
                            ((BaseCell) childAt).setCaching(z, false);
                        }
                    }
                }
            }
            for (int i3 = 0; i3 < DialogsActivity.this.viewPages.length; i3++) {
                if (DialogsActivity.this.viewPages[i3] != null && DialogsActivity.this.viewPages[i3].getVisibility() == 0) {
                    for (int i4 = 0; i4 < DialogsActivity.this.viewPages[i3].listView.getChildCount(); i4++) {
                        View childAt2 = DialogsActivity.this.viewPages[i3].listView.getChildAt(i4);
                        if (childAt2.getY() < DialogsActivity.this.viewPages[i3].listView.blurTopPadding + AndroidUtilities.dp(100.0f) + ((DialogsActivity.this.authHintCell == null || DialogsActivity.this.authHintCell.getVisibility() != 0) ? 0 : AndroidUtilities.dp(200.0f))) {
                            int save = canvas.save();
                            canvas.translate(DialogsActivity.this.viewPages[i3].getX(), DialogsActivity.this.viewPages[i3].getY() + DialogsActivity.this.viewPages[i3].listView.getY());
                            if (childAt2 instanceof DialogCell) {
                                DialogCell dialogCell = (DialogCell) childAt2;
                                if (!dialogCell.isFolderCell() || !SharedConfig.archiveHidden) {
                                    canvas.translate(dialogCell.getX(), dialogCell.getY());
                                    dialogCell.setCaching(z, true);
                                    dialogCell.drawCached(canvas);
                                    if (arrayList != null) {
                                        arrayList.add(dialogCell);
                                    }
                                }
                            } else {
                                canvas.translate(childAt2.getX(), childAt2.getY());
                                childAt2.draw(canvas);
                                if (arrayList != null && (childAt2 instanceof SizeNotifierFrameLayout.IViewWithInvalidateCallback)) {
                                    arrayList.add((SizeNotifierFrameLayout.IViewWithInvalidateCallback) childAt2);
                                }
                            }
                            canvas.restoreToCount(save);
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (DialogsActivity.this.statusDrawable != null) {
                DialogsActivity.this.statusDrawable.attach();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (DialogsActivity.this.statusDrawable != null) {
                DialogsActivity.this.statusDrawable.detach();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTopPadding() {
        SearchViewPager searchViewPager;
        this.topPadding = this.fragmentContextTopPadding;
        updateContextViewPosition();
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null) {
            rightSlidingDialogContainer.setFragmentViewPadding(this.topPadding);
        }
        if (this.whiteActionBar && (searchViewPager = this.searchViewPager) != null) {
            searchViewPager.setTranslationY((this.topPadding - this.lastMeasuredTopPadding) + this.searchViewPagerTranslationY);
        } else {
            this.fragmentView.requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStoriesViewAlpha(float f) {
        float f2;
        float f3;
        this.dialogStoriesCell.setAlpha((1.0f - this.progressToActionMode) * f * this.progressToDialogStoriesCell * (1.0f - Utilities.clamp(this.searchAnimationProgress / 0.5f, 1.0f, 0.0f)));
        if (this.hasStories || this.animateToHasStories) {
            float clamp = Utilities.clamp((-this.scrollYOffset) / AndroidUtilities.dp(81.0f), 1.0f, 0.0f);
            if (this.progressToActionMode == 1.0f) {
                clamp = 1.0f;
            }
            float clamp2 = Utilities.clamp(clamp / 0.5f, 1.0f, 0.0f);
            this.dialogStoriesCell.setClipTop(0);
            if (!this.hasStories && this.animateToHasStories) {
                this.dialogStoriesCell.setTranslationY((-AndroidUtilities.dp(81.0f)) - AndroidUtilities.dp(8.0f));
                this.dialogStoriesCell.setProgressToCollapse(1.0f);
                f3 = this.progressToDialogStoriesCell;
            } else {
                this.dialogStoriesCell.setTranslationY(((this.scrollYOffset + this.storiesYOffset) + (this.storiesOverscroll / 2.0f)) - AndroidUtilities.dp(8.0f));
                this.dialogStoriesCell.setProgressToCollapse(clamp, !this.rightSlidingDialogContainer.hasFragment());
                if (!this.animateToHasStories) {
                    f3 = this.progressToDialogStoriesCell;
                } else {
                    f2 = 1.0f - clamp2;
                    this.actionBar.setTranslationY(0.0f);
                }
            }
            f2 = 1.0f - f3;
            this.actionBar.setTranslationY(0.0f);
        } else {
            if (this.hasOnlySlefStories) {
                this.dialogStoriesCell.setTranslationY(((-AndroidUtilities.dp(81.0f)) + this.scrollYOffset) - AndroidUtilities.dp(8.0f));
                this.dialogStoriesCell.setProgressToCollapse(1.0f);
                DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
                dialogStoriesCell.setClipTop((int) (AndroidUtilities.statusBarHeight - dialogStoriesCell.getY()));
            }
            f2 = 1.0f - this.progressToDialogStoriesCell;
            this.actionBar.setTranslationY(this.scrollYOffset);
        }
        if (f2 != 1.0f) {
            this.actionBar.getTitlesContainer().setPivotY(AndroidUtilities.statusBarHeight + (ActionBar.getCurrentActionBarHeight() / 2.0f));
            this.actionBar.getTitlesContainer().setPivotX(AndroidUtilities.dp(72.0f));
            float f4 = (0.2f * f2) + 0.8f;
            this.actionBar.getTitlesContainer().setScaleY(f4);
            this.actionBar.getTitlesContainer().setScaleX(f4);
            this.actionBar.getTitlesContainer().setAlpha(f2 * (1.0f - this.progressToActionMode));
            return;
        }
        this.actionBar.getTitlesContainer().setScaleY(1.0f);
        this.actionBar.getTitlesContainer().setScaleY(1.0f);
        this.actionBar.getTitlesContainer().setScaleX(1.0f);
        this.actionBar.getTitlesContainer().setAlpha(1.0f - this.progressToActionMode);
    }

    /* loaded from: classes4.dex */
    public class DialogsRecyclerView extends BlurredRecyclerView implements StoriesListPlaceProvider.ClippedView {
        public int additionalPadding;
        float animateFromSelectorPosition;
        boolean animateSwitchingSelector;
        private RecyclerListView animationSupportListView;
        LongSparseArray<View> animationSupportViewsByDialogId;
        private int appliedPaddingTop;
        private boolean firstLayout;
        private boolean ignoreLayout;
        float lastDrawSelectorY;
        private int lastListPadding;
        Paint paint;
        private final ViewPage parentPage;
        UserListPoller poller;
        RectF rectF;
        private float rightFragmentOpenedProgress;
        private Paint selectorPaint;
        float selectorPositionProgress;
        public boolean updateDialogsOnNextDraw;

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean updateEmptyViewAnimated() {
            return true;
        }

        public DialogsRecyclerView(Context context, ViewPage viewPage) {
            super(context);
            this.firstLayout = true;
            this.paint = new Paint();
            this.rectF = new RectF();
            this.selectorPositionProgress = 1.0f;
            this.parentPage = viewPage;
            this.additionalClipBottom = AndroidUtilities.dp(200.0f);
        }

        public void prepareSelectorForAnimation() {
            this.selectorPositionProgress = 0.0f;
            this.animateFromSelectorPosition = this.lastDrawSelectorY;
            this.animateSwitchingSelector = this.rightFragmentOpenedProgress != 0.0f;
        }

        public void setViewsOffset(float f) {
            View findViewByPosition;
            DialogsActivity.viewOffset = f;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).setTranslationY(f);
            }
            if (this.selectorPosition != -1 && (findViewByPosition = getLayoutManager().findViewByPosition(this.selectorPosition)) != null) {
                this.selectorRect.set(findViewByPosition.getLeft(), (int) (findViewByPosition.getTop() + f), findViewByPosition.getRight(), (int) (findViewByPosition.getBottom() + f));
                this.selectorDrawable.setBounds(this.selectorRect);
            }
            invalidate();
        }

        public float getViewOffset() {
            return DialogsActivity.viewOffset;
        }

        @Override // android.view.ViewGroup
        public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
            super.addView(view, i, layoutParams);
            view.setTranslationY(DialogsActivity.viewOffset);
            view.setTranslationX(0.0f);
            view.setAlpha(1.0f);
        }

        @Override // android.view.ViewGroup, android.view.ViewManager
        public void removeView(View view) {
            super.removeView(view);
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            view.setAlpha(1.0f);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
        public void onDraw(Canvas canvas) {
            if (this.parentPage.pullForegroundDrawable != null && DialogsActivity.viewOffset != 0.0f) {
                int paddingTop = getPaddingTop();
                if (paddingTop != 0) {
                    canvas.save();
                    canvas.translate(0.0f, paddingTop);
                }
                this.parentPage.pullForegroundDrawable.drawOverScroll(canvas);
                if (paddingTop != 0) {
                    canvas.restore();
                }
            }
            super.onDraw(canvas);
        }

        /* JADX WARN: Removed duplicated region for block: B:100:0x02bc  */
        /* JADX WARN: Removed duplicated region for block: B:103:0x02fb  */
        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            boolean z;
            float f;
            float f2;
            float f3;
            int i;
            int i2;
            DialogCell dialogCell;
            float f4;
            float f5;
            DialogCell dialogCell2;
            int i3;
            int i4;
            DialogCell dialogCell3;
            View view;
            int childAdapterPosition;
            Paint paint;
            canvas.save();
            if (this.rightFragmentOpenedProgress > 0.0f) {
                canvas.clipRect(0, 0, AndroidUtilities.lerp(getMeasuredWidth(), AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()), this.rightFragmentOpenedProgress), getMeasuredHeight());
                this.paint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay));
                this.paint.setAlpha((int) (paint.getAlpha() * this.rightFragmentOpenedProgress));
                canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()), getMeasuredHeight(), this.paint);
                int alpha = Theme.dividerPaint.getAlpha();
                Theme.dividerPaint.setAlpha((int) (this.rightFragmentOpenedProgress * alpha));
                canvas.drawRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()), 0.0f, AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()) - 1, getMeasuredHeight(), Theme.dividerPaint);
                Theme.dividerPaint.setAlpha(alpha);
            }
            int i5 = Integer.MIN_VALUE;
            int i6 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            if (this.animationSupportListView != null) {
                if (this.animationSupportViewsByDialogId == null) {
                    this.animationSupportViewsByDialogId = new LongSparseArray<>();
                }
                for (int i7 = 0; i7 < this.animationSupportListView.getChildCount(); i7++) {
                    View childAt = this.animationSupportListView.getChildAt(i7);
                    if ((childAt instanceof DialogCell) && childAt.getBottom() > 0) {
                        this.animationSupportViewsByDialogId.put(((DialogCell) childAt).getDialogId(), childAt);
                    }
                }
            }
            float f6 = DialogsActivity.this.rightFragmentTransitionIsOpen ? 0.0f : DialogsActivity.this.scrollYOffset;
            DialogCell dialogCell4 = null;
            float f7 = 2.14748365E9f;
            float f8 = -2.14748365E9f;
            int i8 = 0;
            while (i8 < getChildCount()) {
                View childAt2 = getChildAt(i8);
                if (childAt2 instanceof DialogCell) {
                    dialogCell = (DialogCell) childAt2;
                    dialogCell.setRightFragmentOpenedProgress(this.rightFragmentOpenedProgress);
                    if (AndroidUtilities.isTablet()) {
                        dialogCell.setDialogSelected(dialogCell.getDialogId() == DialogsActivity.this.openedDialogId.dialogId);
                    }
                    LongSparseArray<View> longSparseArray = this.animationSupportViewsByDialogId;
                    if (longSparseArray == null || this.animationSupportListView == null) {
                        f3 = f6;
                    } else {
                        f3 = f6;
                        View view2 = longSparseArray.get(dialogCell.getDialogId());
                        this.animationSupportViewsByDialogId.delete(dialogCell.getDialogId());
                        if (view2 != null) {
                            int childLayoutPosition = this.animationSupportListView.getChildLayoutPosition(view2);
                            if (childLayoutPosition > i5) {
                                i5 = childLayoutPosition;
                            }
                            if (childLayoutPosition < i6) {
                                i6 = childLayoutPosition;
                            }
                            dialogCell.collapseOffset = (view2.getTop() - dialogCell.getTop()) * this.rightFragmentOpenedProgress;
                            if (dialogCell.getTop() + dialogCell.collapseOffset < f7) {
                                f7 = (dialogCell.getTop() + dialogCell.collapseOffset) - f3;
                            }
                            float top = dialogCell.getTop() + AndroidUtilities.lerp(dialogCell.getMeasuredHeight(), view2.getMeasuredHeight(), this.rightFragmentOpenedProgress);
                            float f9 = dialogCell.collapseOffset;
                            if (top + f9 > f8) {
                                f8 = (top + f9) - f3;
                            }
                        }
                    }
                    if (this.updateDialogsOnNextDraw && dialogCell.update(0, true) && (childAdapterPosition = getChildAdapterPosition(dialogCell)) >= 0) {
                        getAdapter().notifyItemChanged(childAdapterPosition);
                    }
                    if (dialogCell.getDialogId() == DialogsActivity.this.rightSlidingDialogContainer.getCurrentFragmetDialogId()) {
                        i = i5;
                        i2 = i6;
                        f4 = f7;
                        f5 = f8;
                        dialogCell2 = dialogCell;
                    } else {
                        i = i5;
                        i2 = i6;
                        f4 = f7;
                        f5 = f8;
                        dialogCell2 = dialogCell;
                        dialogCell = dialogCell4;
                    }
                } else {
                    f3 = f6;
                    i = i5;
                    i2 = i6;
                    dialogCell = dialogCell4;
                    f4 = f7;
                    f5 = f8;
                    dialogCell2 = null;
                }
                if (this.animationSupportListView != null) {
                    int save = canvas.save();
                    canvas.translate(childAt2.getX(), childAt2.getY());
                    if (dialogCell2 != null) {
                        dialogCell2.rightFragmentOffset = -f3;
                        i4 = save;
                        dialogCell3 = dialogCell2;
                        view = childAt2;
                        i3 = i8;
                    } else {
                        i4 = save;
                        dialogCell3 = dialogCell2;
                        view = childAt2;
                        i3 = i8;
                        canvas.saveLayerAlpha(0.0f, 0.0f, childAt2.getMeasuredWidth(), childAt2.getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                    }
                    view.draw(canvas);
                    DialogCell dialogCell5 = dialogCell3;
                    if (dialogCell5 != null && dialogCell5 != dialogCell) {
                        dialogCell5.collapseOffset = 0.0f;
                        dialogCell5.rightFragmentOffset = 0.0f;
                    }
                    canvas.restoreToCount(i4);
                } else {
                    i3 = i8;
                }
                i8 = i3 + 1;
                i5 = i;
                i6 = i2;
                f7 = f4;
                f8 = f5;
                dialogCell4 = dialogCell;
                f6 = f3;
            }
            if (dialogCell4 != null) {
                canvas.save();
                this.lastDrawSelectorY = dialogCell4.getY() + dialogCell4.collapseOffset + dialogCell4.avatarImage.getImageY();
                dialogCell4.collapseOffset = 0.0f;
                dialogCell4.rightFragmentOffset = 0.0f;
                float f10 = this.selectorPositionProgress;
                if (f10 != 1.0f) {
                    float f11 = f10 + 0.08f;
                    this.selectorPositionProgress = f11;
                    this.selectorPositionProgress = Utilities.clamp(f11, 1.0f, 0.0f);
                    invalidate();
                }
                float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.selectorPositionProgress);
                if (interpolation != 1.0f) {
                    float f12 = this.animateFromSelectorPosition;
                    if (f12 != -2.14748365E9f) {
                        if (Math.abs(f12 - this.lastDrawSelectorY) < getMeasuredHeight() * 0.4f) {
                            this.lastDrawSelectorY = AndroidUtilities.lerp(this.animateFromSelectorPosition, this.lastDrawSelectorY, interpolation);
                        } else {
                            z = true;
                            if (this.animateSwitchingSelector || !(z || this.animateFromSelectorPosition == -2.14748365E9f)) {
                                f = 1.0f;
                                interpolation = this.rightFragmentOpenedProgress;
                            } else {
                                f = 1.0f;
                            }
                            f2 = f - interpolation;
                            if (f2 == f) {
                                this.lastDrawSelectorY = -2.14748365E9f;
                            }
                            float f13 = (-AndroidUtilities.dp(5.0f)) * f2;
                            RectF rectF = AndroidUtilities.rectTmp;
                            rectF.set((-AndroidUtilities.dp(4.0f)) + f13, this.lastDrawSelectorY - AndroidUtilities.dp(1.0f), AndroidUtilities.dp(4.0f) + f13, this.lastDrawSelectorY + dialogCell4.avatarImage.getImageHeight() + AndroidUtilities.dp(1.0f));
                            if (this.selectorPaint == null) {
                                this.selectorPaint = new Paint(1);
                            }
                            this.selectorPaint.setColor(Theme.getColor(Theme.key_featuredStickers_addButton));
                            canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.selectorPaint);
                            canvas.restore();
                        }
                    }
                }
                z = false;
                if (this.animateSwitchingSelector) {
                }
                f = 1.0f;
                interpolation = this.rightFragmentOpenedProgress;
                f2 = f - interpolation;
                if (f2 == f) {
                }
                float f132 = (-AndroidUtilities.dp(5.0f)) * f2;
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set((-AndroidUtilities.dp(4.0f)) + f132, this.lastDrawSelectorY - AndroidUtilities.dp(1.0f), AndroidUtilities.dp(4.0f) + f132, this.lastDrawSelectorY + dialogCell4.avatarImage.getImageHeight() + AndroidUtilities.dp(1.0f));
                if (this.selectorPaint == null) {
                }
                this.selectorPaint.setColor(Theme.getColor(Theme.key_featuredStickers_addButton));
                canvas.drawRoundRect(rectF2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.selectorPaint);
                canvas.restore();
            } else {
                this.lastDrawSelectorY = -2.14748365E9f;
            }
            if (this.animationSupportViewsByDialogId != null) {
                float f14 = 2.14748365E9f;
                float f15 = -2.14748365E9f;
                for (int i9 = 0; i9 < this.animationSupportViewsByDialogId.size(); i9++) {
                    View valueAt = this.animationSupportViewsByDialogId.valueAt(i9);
                    int childLayoutPosition2 = this.animationSupportListView.getChildLayoutPosition(valueAt);
                    if (childLayoutPosition2 < i6 && valueAt.getTop() > f15) {
                        f15 = valueAt.getTop();
                    }
                    if (childLayoutPosition2 > i5 && valueAt.getBottom() < f14) {
                        f14 = valueAt.getBottom();
                    }
                }
                for (int i10 = 0; i10 < this.animationSupportViewsByDialogId.size(); i10++) {
                    View valueAt2 = this.animationSupportViewsByDialogId.valueAt(i10);
                    if (valueAt2 instanceof DialogCell) {
                        int childLayoutPosition3 = this.animationSupportListView.getChildLayoutPosition(valueAt2);
                        DialogCell dialogCell6 = (DialogCell) valueAt2;
                        dialogCell6.isTransitionSupport = false;
                        dialogCell6.buildLayout();
                        dialogCell6.isTransitionSupport = true;
                        dialogCell6.setRightFragmentOpenedProgress(this.rightFragmentOpenedProgress);
                        int save2 = canvas.save();
                        if (childLayoutPosition3 > i5) {
                            canvas.translate(valueAt2.getX(), (valueAt2.getBottom() + f8) - f14);
                        } else {
                            canvas.translate(valueAt2.getX(), (valueAt2.getTop() + f8) - f15);
                        }
                        valueAt2.draw(canvas);
                        canvas.restoreToCount(save2);
                    }
                }
                this.animationSupportViewsByDialogId.clear();
            }
            this.updateDialogsOnNextDraw = false;
            if (this.animationSupportListView != null) {
                invalidate();
            }
            if (this.animationSupportListView == null) {
                super.dispatchDraw(canvas);
            }
            if (drawMovingViewsOverlayed()) {
                this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                for (int i11 = 0; i11 < getChildCount(); i11++) {
                    View childAt3 = getChildAt(i11);
                    if (((childAt3 instanceof DialogCell) && ((DialogCell) childAt3).isMoving()) || ((childAt3 instanceof DialogsAdapter.LastEmptyView) && ((DialogsAdapter.LastEmptyView) childAt3).moving)) {
                        if (childAt3.getAlpha() != 1.0f) {
                            this.rectF.set(childAt3.getX(), childAt3.getY(), childAt3.getX() + childAt3.getMeasuredWidth(), childAt3.getY() + childAt3.getMeasuredHeight());
                            canvas.saveLayerAlpha(this.rectF, (int) (childAt3.getAlpha() * 255.0f), 31);
                        } else {
                            canvas.save();
                        }
                        canvas.translate(childAt3.getX(), childAt3.getY());
                        canvas.drawRect(0.0f, 0.0f, childAt3.getMeasuredWidth(), childAt3.getMeasuredHeight(), this.paint);
                        childAt3.draw(canvas);
                        canvas.restore();
                    }
                }
                invalidate();
            }
            if (DialogsActivity.this.slidingView != null && DialogsActivity.this.pacmanAnimation != null) {
                DialogsActivity.this.pacmanAnimation.draw(canvas, DialogsActivity.this.slidingView.getTop() + (DialogsActivity.this.slidingView.getMeasuredHeight() / 2));
            }
            if (this.poller == null) {
                this.poller = UserListPoller.getInstance(((BaseFragment) DialogsActivity.this).currentAccount);
            }
            this.poller.checkList(this);
        }

        private boolean drawMovingViewsOverlayed() {
            return getItemAnimator() != null && getItemAnimator().isRunning();
        }

        @Override // org.telegram.ui.Components.BlurredRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            if (drawMovingViewsOverlayed() && (view instanceof DialogCell) && ((DialogCell) view).isMoving()) {
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView
        public void setAdapter(RecyclerView.Adapter adapter) {
            super.setAdapter(adapter);
            this.firstLayout = true;
        }

        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        protected void onMeasure(int i, int i2) {
            int currentActionBarHeight;
            int findFirstVisibleItemPosition = this.parentPage.layoutManager.findFirstVisibleItemPosition();
            if (findFirstVisibleItemPosition != -1 && this.parentPage.itemTouchhelper.isIdle() && !this.parentPage.layoutManager.hasPendingScrollPosition() && this.parentPage.listView.getScrollState() != 1) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.parentPage.listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                if (findViewHolderForAdapterPosition != null) {
                    int top = findViewHolderForAdapterPosition.itemView.getTop();
                    if (this.parentPage.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive() && this.parentPage.archivePullViewState == 2) {
                        findFirstVisibleItemPosition = Math.max(1, findFirstVisibleItemPosition);
                    }
                    this.ignoreLayout = true;
                    this.parentPage.layoutManager.scrollToPositionWithOffset(findFirstVisibleItemPosition, (int) ((top - this.lastListPadding) + DialogsActivity.this.scrollAdditionalOffset + this.parentPage.pageAdditionalOffset));
                    this.ignoreLayout = false;
                }
            } else if (findFirstVisibleItemPosition == -1 && this.firstLayout) {
                this.parentPage.layoutManager.scrollToPositionWithOffset((this.parentPage.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive()) ? 1 : 0, (int) DialogsActivity.this.scrollYOffset);
            }
            if (!DialogsActivity.this.onlySelect || DialogsActivity.this.initialDialogsType == 3) {
                this.ignoreLayout = true;
                DialogsActivity dialogsActivity = DialogsActivity.this;
                currentActionBarHeight = (dialogsActivity.hasStories || (dialogsActivity.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0)) ? ActionBar.getCurrentActionBarHeight() + (((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) : (!((BaseFragment) DialogsActivity.this).inPreviewMode || Build.VERSION.SDK_INT < 21) ? 0 : AndroidUtilities.statusBarHeight;
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                if (dialogsActivity2.hasStories && !dialogsActivity2.actionModeFullyShowed) {
                    currentActionBarHeight += AndroidUtilities.dp(81.0f);
                }
                this.additionalPadding = 0;
                if (DialogsActivity.this.authHintCell != null && DialogsActivity.this.authHintCellProgress != 0.0f && !DialogsActivity.this.authHintCellAnimating) {
                    currentActionBarHeight += DialogsActivity.this.authHintCell.getMeasuredHeight();
                    this.additionalPadding += DialogsActivity.this.authHintCell.getMeasuredHeight();
                }
                if (currentActionBarHeight != getPaddingTop()) {
                    setTopGlowOffset(currentActionBarHeight);
                    setPadding(0, currentActionBarHeight, 0, 0);
                    if (DialogsActivity.this.hasStories) {
                        this.parentPage.progressView.setPaddingTop(currentActionBarHeight - AndroidUtilities.dp(81.0f));
                    } else {
                        this.parentPage.progressView.setPaddingTop(currentActionBarHeight);
                    }
                    for (int i3 = 0; i3 < getChildCount(); i3++) {
                        if (getChildAt(i3) instanceof DialogsAdapter.LastEmptyView) {
                            getChildAt(i3).requestLayout();
                        }
                    }
                }
                this.ignoreLayout = false;
            } else {
                currentActionBarHeight = 0;
            }
            if (this.firstLayout && DialogsActivity.this.getMessagesController().dialogsLoaded) {
                if (this.parentPage.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive()) {
                    this.ignoreLayout = true;
                    ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(1, (int) DialogsActivity.this.scrollYOffset);
                    this.ignoreLayout = false;
                }
                this.firstLayout = false;
            }
            super.onMeasure(i, i2);
            if (DialogsActivity.this.onlySelect || this.appliedPaddingTop == currentActionBarHeight || DialogsActivity.this.viewPages == null || DialogsActivity.this.viewPages.length <= 1 || DialogsActivity.this.startedTracking) {
                return;
            }
            if ((DialogsActivity.this.tabsAnimation != null && DialogsActivity.this.tabsAnimation.isRunning()) || DialogsActivity.this.tabsAnimationInProgress || DialogsActivity.this.filterTabsView == null) {
                return;
            }
            DialogsActivity.this.filterTabsView.isAnimatingIndicator();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            this.lastListPadding = getPaddingTop();
            DialogsActivity.this.scrollAdditionalOffset = 0.0f;
            this.parentPage.pageAdditionalOffset = 0;
        }

        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void toggleArchiveHidden(boolean z, DialogCell dialogCell) {
            SharedConfig.toggleArchiveHidden();
            UndoView undoView = DialogsActivity.this.getUndoView();
            if (SharedConfig.archiveHidden) {
                if (dialogCell != null) {
                    DialogsActivity.this.disableActionBarScrolling = true;
                    DialogsActivity.this.waitingForScrollFinished = true;
                    int measuredHeight = dialogCell.getMeasuredHeight() + (dialogCell.getTop() - getPaddingTop());
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories && !dialogsActivity.dialogStoriesCell.isExpanded()) {
                        DialogsActivity.this.fixScrollYAfterArchiveOpened = true;
                        measuredHeight += AndroidUtilities.dp(81.0f);
                    }
                    smoothScrollBy(0, measuredHeight, CubicBezierInterpolator.EASE_OUT);
                    if (z) {
                        DialogsActivity.this.updatePullAfterScroll = true;
                    } else {
                        updatePullState();
                    }
                }
                undoView.showWithAction(0L, 6, null, null);
                return;
            }
            undoView.showWithAction(0L, 7, null, null);
            updatePullState();
            if (!z || dialogCell == null) {
                return;
            }
            dialogCell.resetPinnedArchiveState();
            dialogCell.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePullState() {
            this.parentPage.archivePullViewState = SharedConfig.archiveHidden ? 2 : 0;
            if (this.parentPage.pullForegroundDrawable != null) {
                this.parentPage.pullForegroundDrawable.setWillDraw(this.parentPage.archivePullViewState != 0);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (this.fastScrollAnimationRunning || DialogsActivity.this.waitingForScrollFinished || DialogsActivity.this.rightFragmentTransitionInProgress) {
                return false;
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                setOverScrollMode(0);
            }
            if ((action == 1 || action == 3) && !this.parentPage.itemTouchhelper.isIdle() && this.parentPage.swipeController.swipingFolder) {
                this.parentPage.swipeController.swipeFolderBack = true;
                if (this.parentPage.itemTouchhelper.checkHorizontalSwipe(null, 4) != 0 && this.parentPage.swipeController.currentItemViewHolder != null) {
                    View view = this.parentPage.swipeController.currentItemViewHolder.itemView;
                    if (view instanceof DialogCell) {
                        DialogCell dialogCell = (DialogCell) view;
                        long dialogId = dialogCell.getDialogId();
                        if (DialogObject.isFolderDialogId(dialogId)) {
                            toggleArchiveHidden(false, dialogCell);
                        } else {
                            TLRPC$Dialog tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId);
                            if (tLRPC$Dialog != null) {
                                if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 1) {
                                    if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 3) {
                                        if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 0) {
                                            if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 4) {
                                                ArrayList arrayList = new ArrayList();
                                                arrayList.add(Long.valueOf(dialogId));
                                                DialogsActivity.this.performSelectedDialogsAction(arrayList, R.styleable.AppCompatTheme_textAppearanceLargePopupMenu, true, false);
                                            }
                                        } else {
                                            ArrayList arrayList2 = new ArrayList();
                                            arrayList2.add(Long.valueOf(dialogId));
                                            DialogsActivity.this.canPinCount = !DialogsActivity.this.isDialogPinned(tLRPC$Dialog) ? 1 : 0;
                                            DialogsActivity.this.performSelectedDialogsAction(arrayList2, 100, true, false);
                                        }
                                    } else if (!DialogsActivity.this.getMessagesController().isDialogMuted(dialogId, 0L)) {
                                        NotificationsController.getInstance(UserConfig.selectedAccount).setDialogNotificationsSettings(dialogId, 0L, 3);
                                        if (BulletinFactory.canShowBulletin(DialogsActivity.this)) {
                                            BulletinFactory.createMuteBulletin(DialogsActivity.this, 3).show();
                                        }
                                    } else {
                                        ArrayList arrayList3 = new ArrayList();
                                        arrayList3.add(Long.valueOf(dialogId));
                                        DialogsActivity dialogsActivity = DialogsActivity.this;
                                        dialogsActivity.canMuteCount = !MessagesController.getInstance(((BaseFragment) dialogsActivity).currentAccount).isDialogMuted(dialogId, 0L);
                                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                                        dialogsActivity2.canUnmuteCount = dialogsActivity2.canMuteCount > 0 ? 0 : 1;
                                        DialogsActivity.this.performSelectedDialogsAction(arrayList3, R.styleable.AppCompatTheme_textAppearanceListItemSecondary, true, false);
                                    }
                                } else {
                                    ArrayList arrayList4 = new ArrayList();
                                    arrayList4.add(Long.valueOf(dialogId));
                                    DialogsActivity.this.canReadCount = (tLRPC$Dialog.unread_count > 0 || tLRPC$Dialog.unread_mark) ? 1 : 0;
                                    DialogsActivity.this.performSelectedDialogsAction(arrayList4, 101, true, false);
                                }
                            }
                        }
                    }
                }
            }
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (this.parentPage.dialogsType == 0 && ((action == 1 || action == 3) && this.parentPage.archivePullViewState == 2 && DialogsActivity.this.hasHiddenArchive() && ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() == 0)) {
                int paddingTop = getPaddingTop();
                DialogCell findArchiveDialogCell = DialogsActivity.this.findArchiveDialogCell(this.parentPage);
                if (findArchiveDialogCell != null) {
                    int dp = (int) (AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f) * 0.85f);
                    int top = (findArchiveDialogCell.getTop() - paddingTop) + findArchiveDialogCell.getMeasuredHeight();
                    long currentTimeMillis = System.currentTimeMillis() - DialogsActivity.this.startArchivePullingTime;
                    if (top < dp || currentTimeMillis < 200) {
                        DialogsActivity.this.disableActionBarScrolling = true;
                        smoothScrollBy(0, top, CubicBezierInterpolator.EASE_OUT_QUINT);
                        this.parentPage.archivePullViewState = 2;
                    } else if (this.parentPage.archivePullViewState != 1) {
                        if (getViewOffset() == 0.0f) {
                            DialogsActivity.this.disableActionBarScrolling = true;
                            smoothScrollBy(0, findArchiveDialogCell.getTop() - paddingTop, CubicBezierInterpolator.EASE_OUT_QUINT);
                        }
                        if (!DialogsActivity.this.canShowHiddenArchive) {
                            DialogsActivity.this.canShowHiddenArchive = true;
                            performHapticFeedback(3, 2);
                            if (this.parentPage.pullForegroundDrawable != null) {
                                this.parentPage.pullForegroundDrawable.colorize(true);
                            }
                        }
                        findArchiveDialogCell.startOutAnimation();
                        this.parentPage.archivePullViewState = 1;
                        if (AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                            AndroidUtilities.makeAccessibilityAnnouncement(LocaleController.getString(R.string.AccDescrArchivedChatsShown));
                        }
                    }
                    if (getViewOffset() != 0.0f) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(getViewOffset(), 0.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$DialogsRecyclerView$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                DialogsActivity.DialogsRecyclerView.this.lambda$onTouchEvent$0(valueAnimator);
                            }
                        });
                        ofFloat.setDuration(Math.max(100L, 350.0f - ((getViewOffset() / PullForegroundDrawable.getMaxOverscroll()) * 120.0f)));
                        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                        setScrollEnabled(false);
                        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.DialogsRecyclerView.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                super.onAnimationEnd(animator);
                                DialogsRecyclerView.this.setScrollEnabled(true);
                            }
                        });
                        ofFloat.start();
                    }
                }
            }
            return onTouchEvent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$0(ValueAnimator valueAnimator) {
            setViewsOffset(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (this.fastScrollAnimationRunning || DialogsActivity.this.waitingForScrollFinished || this.parentPage.dialogsItemAnimator.isRunning()) {
                return false;
            }
            if (motionEvent.getAction() == 0) {
                DialogsActivity dialogsActivity = DialogsActivity.this;
                dialogsActivity.allowSwipeDuringCurrentTouch = !((BaseFragment) dialogsActivity).actionBar.isActionModeShowed();
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView
        public boolean allowSelectChildAtPosition(View view) {
            return !(view instanceof HeaderCell) || view.isClickable();
        }

        public void setOpenRightFragmentProgress(float f) {
            this.rightFragmentOpenedProgress = f;
            invalidate();
        }

        public void setAnimationSupportView(RecyclerListView recyclerListView, float f, boolean z, boolean z2) {
            RecyclerListView recyclerListView2 = recyclerListView == null ? this.animationSupportListView : this;
            if (recyclerListView2 == null) {
                this.animationSupportListView = recyclerListView;
                return;
            }
            int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            DialogCell dialogCell = null;
            DialogCell dialogCell2 = null;
            for (int i2 = 0; i2 < recyclerListView2.getChildCount(); i2++) {
                View childAt = recyclerListView2.getChildAt(i2);
                if (childAt instanceof DialogCell) {
                    DialogCell dialogCell3 = (DialogCell) childAt;
                    if (dialogCell3.getDialogId() == DialogsActivity.this.rightSlidingDialogContainer.getCurrentFragmetDialogId()) {
                        dialogCell = dialogCell3;
                    }
                    if (childAt.getTop() >= 0 && dialogCell3.getDialogId() != 0 && childAt.getTop() < i) {
                        DialogCell dialogCell4 = (DialogCell) childAt;
                        i = dialogCell4.getTop();
                        dialogCell2 = dialogCell4;
                    }
                }
            }
            DialogCell dialogCell5 = (dialogCell == null || getAdapter().getItemCount() * AndroidUtilities.dp(70.0f) <= getMeasuredHeight() || ((float) (dialogCell2.getTop() - getPaddingTop())) <= ((float) (getMeasuredHeight() - getPaddingTop())) / 2.0f) ? dialogCell2 : dialogCell;
            this.animationSupportListView = recyclerListView;
            if (dialogCell5 != null) {
                if (recyclerListView != null) {
                    recyclerListView.setPadding(getPaddingLeft(), this.topPadding, getPaddingLeft(), getPaddingBottom());
                    DialogsAdapter dialogsAdapter = (DialogsAdapter) recyclerListView.getAdapter();
                    int findDialogPosition = dialogsAdapter.findDialogPosition(dialogCell5.getDialogId());
                    int top = (int) ((dialogCell5.getTop() - recyclerListView2.getPaddingTop()) + f);
                    if (findDialogPosition >= 0) {
                        boolean z3 = this.parentPage.dialogsType == 0 && this.parentPage.archivePullViewState == 2 && DialogsActivity.this.hasHiddenArchive();
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        ((LinearLayoutManager) recyclerListView.getLayoutManager()).scrollToPositionWithOffset(findDialogPosition, dialogsAdapter.fixScrollGap(this, findDialogPosition, top, z3, dialogsActivity.hasStories, dialogsActivity.canShowFilterTabsView, z));
                    }
                }
                int findDialogPosition2 = ((DialogsAdapter) getAdapter()).findDialogPosition(dialogCell5.getDialogId());
                int top2 = dialogCell5.getTop() - getPaddingTop();
                if (z2 && DialogsActivity.this.hasStories) {
                    top2 += AndroidUtilities.dp(81.0f);
                }
                if (findDialogPosition2 >= 0) {
                    ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(findDialogPosition2, top2);
                }
            }
        }

        @Override // org.telegram.ui.Stories.StoriesListPlaceProvider.ClippedView
        public void updateClip(int[] iArr) {
            int paddingTop = (int) (getPaddingTop() + DialogsActivity.this.scrollYOffset);
            iArr[0] = paddingTop;
            iArr[1] = paddingTop + getMeasuredHeight();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StoriesController getStoriesController() {
        return getMessagesController().getStoriesController();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SwipeController extends ItemTouchHelper.Callback {
        private RecyclerView.ViewHolder currentItemViewHolder;
        private ViewPage parentPage;
        private boolean swipeFolderBack;
        private boolean swipingFolder;

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public float getSwipeEscapeVelocity(float f) {
            return 3500.0f;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.45f;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public float getSwipeVelocityThreshold(float f) {
            return Float.MAX_VALUE;
        }

        public SwipeController(ViewPage viewPage) {
            this.parentPage = viewPage;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            TLRPC$Dialog tLRPC$Dialog;
            if (!DialogsActivity.this.waitingForDialogsAnimationEnd(this.parentPage) && ((((BaseFragment) DialogsActivity.this).parentLayout == null || !((BaseFragment) DialogsActivity.this).parentLayout.isInPreviewMode()) && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment())) {
                if (!this.swipingFolder || !this.swipeFolderBack) {
                    if (!DialogsActivity.this.onlySelect && this.parentPage.isDefaultDialogType() && DialogsActivity.this.slidingView == null) {
                        View view = viewHolder.itemView;
                        if (view instanceof DialogCell) {
                            DialogCell dialogCell = (DialogCell) view;
                            long dialogId = dialogCell.getDialogId();
                            MessagesController.DialogFilter dialogFilter = null;
                            if (!((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed(null)) {
                                int i = DialogsActivity.this.initialDialogsType;
                                try {
                                    i = this.parentPage.dialogsAdapter.getDialogsType();
                                } catch (Exception unused) {
                                }
                                if ((DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 5) && DialogsActivity.this.allowSwipeDuringCurrentTouch && (((dialogId != DialogsActivity.this.getUserConfig().clientUserId && dialogId != 777000 && i != 7 && i != 8) || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 2) && (!DialogsActivity.this.getMessagesController().isPromoDialog(dialogId, false) || DialogsActivity.this.getMessagesController().promoDialogType == MessagesController.PROMO_TYPE_PSA))) {
                                    boolean z = DialogsActivity.this.folderId == 0 && (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 3 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 1 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 0 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 4) && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment();
                                    if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 1) {
                                        if (DialogsActivity.this.viewPages[0].dialogsType == 7 || DialogsActivity.this.viewPages[0].dialogsType == 8) {
                                            dialogFilter = DialogsActivity.this.getMessagesController().selectedDialogFilter[DialogsActivity.this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
                                        }
                                        if (dialogFilter != null && (dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ) != 0 && (tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId)) != null && !dialogFilter.alwaysShow(((BaseFragment) DialogsActivity.this).currentAccount, tLRPC$Dialog) && (tLRPC$Dialog.unread_count > 0 || tLRPC$Dialog.unread_mark)) {
                                            z = false;
                                        }
                                    }
                                    this.swipeFolderBack = false;
                                    this.swipingFolder = (z && !DialogObject.isFolderDialogId(dialogCell.getDialogId())) || (SharedConfig.archiveHidden && DialogObject.isFolderDialogId(dialogCell.getDialogId()));
                                    dialogCell.setSliding(true);
                                    return ItemTouchHelper.Callback.makeMovementFlags(0, 4);
                                }
                            } else {
                                TLRPC$Dialog tLRPC$Dialog2 = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId);
                                if (!DialogsActivity.this.allowMoving || tLRPC$Dialog2 == null || !DialogsActivity.this.isDialogPinned(tLRPC$Dialog2) || DialogObject.isFolderDialogId(dialogId)) {
                                    return 0;
                                }
                                DialogsActivity.this.movingView = (DialogCell) viewHolder.itemView;
                                DialogsActivity.this.movingView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                                this.swipeFolderBack = false;
                                return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
                            }
                        }
                    }
                } else {
                    View view2 = viewHolder.itemView;
                    if (view2 instanceof DialogCell) {
                        ((DialogCell) view2).swipeCanceled = true;
                    }
                    this.swipingFolder = false;
                    return 0;
                }
            }
            return 0;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            long dialogId;
            TLRPC$Dialog tLRPC$Dialog;
            View view = viewHolder2.itemView;
            if ((view instanceof DialogCell) && (tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get((dialogId = ((DialogCell) view).getDialogId()))) != null && DialogsActivity.this.isDialogPinned(tLRPC$Dialog) && !DialogObject.isFolderDialogId(dialogId)) {
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = viewHolder2.getAdapterPosition();
                if (this.parentPage.listView.getItemAnimator() == null) {
                    ViewPage viewPage = this.parentPage;
                    viewPage.listView.setItemAnimator(viewPage.dialogsItemAnimator);
                }
                this.parentPage.dialogsAdapter.moveDialogs(this.parentPage.listView, adapterPosition, adapterPosition2);
                if (DialogsActivity.this.viewPages[0].dialogsType != 7 && DialogsActivity.this.viewPages[0].dialogsType != 8) {
                    DialogsActivity.this.movingWas = true;
                } else {
                    MessagesController.DialogFilter dialogFilter = DialogsActivity.this.getMessagesController().selectedDialogFilter[DialogsActivity.this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
                    if (!DialogsActivity.this.movingDialogFilters.contains(dialogFilter)) {
                        DialogsActivity.this.movingDialogFilters.add(dialogFilter);
                    }
                }
                return true;
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int convertToAbsoluteDirection(int i, int i2) {
            if (this.swipeFolderBack) {
                return 0;
            }
            return super.convertToAbsoluteDirection(i, i2);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder == null) {
                DialogsActivity.this.slidingView = null;
                return;
            }
            DialogCell dialogCell = (DialogCell) viewHolder.itemView;
            long dialogId = dialogCell.getDialogId();
            if (DialogObject.isFolderDialogId(dialogId)) {
                this.parentPage.listView.toggleArchiveHidden(false, dialogCell);
                return;
            }
            final TLRPC$Dialog tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId);
            if (tLRPC$Dialog == null) {
                return;
            }
            if (DialogsActivity.this.getMessagesController().isPromoDialog(dialogId, false) || DialogsActivity.this.folderId != 0 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 1) {
                DialogsActivity.this.slidingView = dialogCell;
                final int adapterPosition = viewHolder.getAdapterPosition();
                final int itemCount = this.parentPage.dialogsAdapter.getItemCount();
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$SwipeController$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.SwipeController.this.lambda$onSwiped$2(tLRPC$Dialog, itemCount, adapterPosition);
                    }
                };
                DialogsActivity.this.setDialogsListFrozen(true);
                if (Utilities.random.nextInt(1000) == 1) {
                    if (DialogsActivity.this.pacmanAnimation == null) {
                        DialogsActivity.this.pacmanAnimation = new PacmanAnimation(this.parentPage.listView);
                    }
                    DialogsActivity.this.pacmanAnimation.setFinishRunnable(runnable);
                    DialogsActivity.this.pacmanAnimation.start();
                    return;
                }
                runnable.run();
                return;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(Long.valueOf(dialogId));
            DialogsActivity.this.canReadCount = (tLRPC$Dialog.unread_count > 0 || tLRPC$Dialog.unread_mark) ? 1 : 0;
            DialogsActivity.this.performSelectedDialogsAction(arrayList, 101, true, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSwiped$2(final TLRPC$Dialog tLRPC$Dialog, int i, int i2) {
            if (DialogsActivity.this.frozenDialogsList == null) {
                return;
            }
            DialogsActivity.this.frozenDialogsList.remove(tLRPC$Dialog);
            final int i3 = tLRPC$Dialog.pinnedNum;
            DialogsActivity.this.slidingView = null;
            this.parentPage.listView.invalidate();
            int findLastVisibleItemPosition = this.parentPage.layoutManager.findLastVisibleItemPosition();
            if (findLastVisibleItemPosition == i - 1) {
                this.parentPage.layoutManager.findViewByPosition(findLastVisibleItemPosition).requestLayout();
            }
            boolean z = false;
            if (!DialogsActivity.this.getMessagesController().isPromoDialog(tLRPC$Dialog.id, false)) {
                int addDialogToFolder = DialogsActivity.this.getMessagesController().addDialogToFolder(tLRPC$Dialog.id, DialogsActivity.this.folderId == 0 ? 1 : 0, -1, 0L);
                if (addDialogToFolder != 2 || i2 != 0) {
                    this.parentPage.dialogsItemAnimator.prepareForRemove();
                    this.parentPage.updateList(true);
                }
                if (DialogsActivity.this.folderId == 0) {
                    if (addDialogToFolder == 2) {
                        this.parentPage.dialogsItemAnimator.prepareForRemove();
                        if (i2 == 0) {
                            DialogsActivity.this.setDialogsListFrozen(true);
                            this.parentPage.updateList(true);
                            DialogsActivity.this.checkAnimationFinished();
                        } else {
                            this.parentPage.updateList(true);
                            if (!SharedConfig.archiveHidden && this.parentPage.layoutManager.findFirstVisibleItemPosition() == 0) {
                                DialogsActivity.this.disableActionBarScrolling = true;
                                this.parentPage.listView.smoothScrollBy(0, -AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f));
                            }
                        }
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        DialogsActivity.this.frozenDialogsList.add(0, dialogsActivity.getDialogsArray(((BaseFragment) dialogsActivity).currentAccount, this.parentPage.dialogsType, DialogsActivity.this.folderId, false).get(0));
                    } else if (addDialogToFolder == 1) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.parentPage.listView.findViewHolderForAdapterPosition(0);
                        if (findViewHolderForAdapterPosition != null) {
                            View view = findViewHolderForAdapterPosition.itemView;
                            if (view instanceof DialogCell) {
                                DialogCell dialogCell = (DialogCell) view;
                                dialogCell.checkCurrentDialogIndex(true);
                                dialogCell.animateArchiveAvatar();
                            }
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$SwipeController$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogsActivity.SwipeController.this.lambda$onSwiped$0();
                            }
                        }, 300L);
                    }
                    SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                    z = (globalMainSettings.getBoolean("archivehint_l", false) || SharedConfig.archiveHidden) ? true : true;
                    if (!z) {
                        globalMainSettings.edit().putBoolean("archivehint_l", true).commit();
                    }
                    UndoView undoView = DialogsActivity.this.getUndoView();
                    if (undoView != null) {
                        undoView.showWithAction(tLRPC$Dialog.id, z ? 2 : 3, null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$SwipeController$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogsActivity.SwipeController.this.lambda$onSwiped$1(tLRPC$Dialog, i3);
                            }
                        });
                    }
                }
                if (DialogsActivity.this.folderId == 0 || !DialogsActivity.this.frozenDialogsList.isEmpty()) {
                    return;
                }
                this.parentPage.listView.setEmptyView(null);
                this.parentPage.progressView.setVisibility(4);
                return;
            }
            DialogsActivity.this.getMessagesController().hidePromoDialog();
            this.parentPage.dialogsItemAnimator.prepareForRemove();
            this.parentPage.updateList(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSwiped$0() {
            DialogsActivity.this.setDialogsListFrozen(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSwiped$1(TLRPC$Dialog tLRPC$Dialog, int i) {
            DialogsActivity.this.dialogsListFrozen = true;
            DialogsActivity.this.getMessagesController().addDialogToFolder(tLRPC$Dialog.id, 0, i, 0L);
            DialogsActivity.this.dialogsListFrozen = false;
            ArrayList<TLRPC$Dialog> dialogs = DialogsActivity.this.getMessagesController().getDialogs(0);
            int indexOf = dialogs.indexOf(tLRPC$Dialog);
            if (indexOf >= 0) {
                ArrayList<TLRPC$Dialog> dialogs2 = DialogsActivity.this.getMessagesController().getDialogs(1);
                if (!dialogs2.isEmpty() || indexOf != 1) {
                    DialogsActivity.this.setDialogsListFrozen(true);
                    this.parentPage.dialogsItemAnimator.prepareForRemove();
                    this.parentPage.updateList(true);
                    DialogsActivity.this.checkAnimationFinished();
                }
                if (dialogs2.isEmpty()) {
                    dialogs.remove(0);
                    if (indexOf == 1) {
                        DialogsActivity.this.setDialogsListFrozen(true);
                        this.parentPage.updateList(true);
                        DialogsActivity.this.checkAnimationFinished();
                        return;
                    }
                    if (!DialogsActivity.this.frozenDialogsList.isEmpty()) {
                        DialogsActivity.this.frozenDialogsList.remove(0);
                    }
                    this.parentPage.dialogsItemAnimator.prepareForRemove();
                    this.parentPage.updateList(true);
                    return;
                }
                return;
            }
            this.parentPage.updateList(false);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder != null) {
                this.parentPage.listView.hideSelector(false);
            }
            this.currentItemViewHolder = viewHolder;
            if (viewHolder != null) {
                View view = viewHolder.itemView;
                if (view instanceof DialogCell) {
                    ((DialogCell) view).swipeCanceled = false;
                }
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public long getAnimationDuration(RecyclerView recyclerView, int i, float f, float f2) {
            if (i == 4) {
                return 200L;
            }
            if (i == 8 && DialogsActivity.this.movingView != null) {
                final DialogCell dialogCell = DialogsActivity.this.movingView;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$SwipeController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        dialogCell.setBackgroundDrawable(null);
                    }
                }, this.parentPage.dialogsItemAnimator.getMoveDuration());
                DialogsActivity.this.movingView = null;
            }
            return super.getAnimationDuration(recyclerView, i, f, f2);
        }
    }

    public DialogsActivity(Bundle bundle) {
        super(bundle);
        this.initialSearchType = -1;
        this.isFirstTab = true;
        this.allowGlobalSearch = true;
        this.hasStories = false;
        this.hasOnlySlefStories = false;
        this.animateToHasStories = false;
        this.invalidateScrollY = true;
        this.contactsAlpha = 1.0f;
        this.undoView = new UndoView[2];
        this.movingDialogFilters = new ArrayList<>();
        this.actionBarDefaultPaint = new Paint();
        this.actionModeViews = new ArrayList<>();
        this.rect = new RectF();
        this.paint = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.askAboutContacts = true;
        this.floatingInterpolator = new AccelerateDecelerateInterpolator();
        this.checkPermission = true;
        this.resetDelegate = true;
        this.openedDialogId = new MessagesStorage.TopicKey();
        this.selectedDialogs = new ArrayList<>();
        this.notify = true;
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.debugLastUpdateAction = -1;
        this.SCROLL_Y = new AnimationProperties.FloatProperty<DialogsActivity>("animationValue") { // from class: org.telegram.ui.DialogsActivity.1
            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(DialogsActivity dialogsActivity, float f) {
                dialogsActivity.setScrollY(f);
            }

            @Override // android.util.Property
            public Float get(DialogsActivity dialogsActivity) {
                return Float.valueOf(DialogsActivity.this.scrollYOffset);
            }
        };
        this.SEARCH_TRANSLATION_Y = new AnimationProperties.FloatProperty<View>("viewPagerTranslation") { // from class: org.telegram.ui.DialogsActivity.2
            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(View view, float f) {
                DialogsActivity dialogsActivity = DialogsActivity.this;
                dialogsActivity.searchViewPagerTranslationY = f;
                view.setTranslationY(dialogsActivity.panTranslationY + f);
            }

            @Override // android.util.Property
            public Float get(View view) {
                return Float.valueOf(DialogsActivity.this.searchViewPagerTranslationY);
            }
        };
        this.shiftDp = -4;
        this.scrollBarVisible = true;
        this.storiesEnabled = true;
        this.isNextButton = false;
        this.slideFragmentProgress = 1.0f;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        Bundle bundle = this.arguments;
        if (bundle != null) {
            this.onlySelect = bundle.getBoolean("onlySelect", false);
            this.canSelectTopics = this.arguments.getBoolean("canSelectTopics", false);
            this.cantSendToChannels = this.arguments.getBoolean("cantSendToChannels", false);
            this.initialDialogsType = this.arguments.getInt("dialogsType", 0);
            this.isQuote = this.arguments.getBoolean("quote", false);
            this.isReplyTo = this.arguments.getBoolean("reply_to", false);
            this.selectAlertString = this.arguments.getString("selectAlertString");
            this.selectAlertStringGroup = this.arguments.getString("selectAlertStringGroup");
            this.addToGroupAlertString = this.arguments.getString("addToGroupAlertString");
            this.allowSwitchAccount = this.arguments.getBoolean("allowSwitchAccount");
            this.checkCanWrite = this.arguments.getBoolean("checkCanWrite", true);
            this.afterSignup = this.arguments.getBoolean("afterSignup", false);
            this.folderId = this.arguments.getInt("folderId", 0);
            this.resetDelegate = this.arguments.getBoolean("resetDelegate", true);
            this.messagesCount = this.arguments.getInt("messagesCount", 0);
            this.hasPoll = this.arguments.getInt("hasPoll", 0);
            this.hasInvoice = this.arguments.getBoolean("hasInvoice", false);
            this.showSetPasswordConfirm = this.arguments.getBoolean("showSetPasswordConfirm", this.showSetPasswordConfirm);
            this.arguments.getInt("otherwiseRelogin");
            this.allowGroups = this.arguments.getBoolean("allowGroups", true);
            this.allowMegagroups = this.arguments.getBoolean("allowMegagroups", true);
            this.allowLegacyGroups = this.arguments.getBoolean("allowLegacyGroups", true);
            this.allowChannels = this.arguments.getBoolean("allowChannels", true);
            this.allowUsers = this.arguments.getBoolean("allowUsers", true);
            this.allowBots = this.arguments.getBoolean("allowBots", true);
            this.closeFragment = this.arguments.getBoolean("closeFragment", true);
            this.allowGlobalSearch = this.arguments.getBoolean("allowGlobalSearch", true);
            byte[] byteArray = this.arguments.getByteArray("requestPeerType");
            if (byteArray != null) {
                try {
                    SerializedData serializedData = new SerializedData(byteArray);
                    this.requestPeerType = TLRPC$RequestPeerType.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                    serializedData.cleanup();
                } catch (Exception unused) {
                }
            }
            this.requestPeerBotId = this.arguments.getLong("requestPeerBotId", 0L);
        }
        if (this.initialDialogsType == 0) {
            this.askAboutContacts = MessagesController.getGlobalNotificationsSettings().getBoolean("askAboutContacts", true);
            SharedConfig.loadProxyList();
        }
        if (this.searchString == null) {
            this.currentConnectionState = getConnectionsManager().getConnectionState();
            getNotificationCenter().addObserver(this, NotificationCenter.dialogsNeedReload);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
            if (!this.onlySelect) {
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeSearchByActiveAction);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);
                getNotificationCenter().addObserver(this, NotificationCenter.filterSettingsUpdated);
                getNotificationCenter().addObserver(this, NotificationCenter.dialogFiltersUpdated);
                getNotificationCenter().addObserver(this, NotificationCenter.dialogsUnreadCounterChanged);
            }
            getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
            getNotificationCenter().addObserver(this, NotificationCenter.encryptedChatUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.contactsDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.appDidLogout);
            getNotificationCenter().addObserver(this, NotificationCenter.openedChatChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.notificationsSettingsUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByAck);
            getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByServer);
            getNotificationCenter().addObserver(this, NotificationCenter.messageSendError);
            getNotificationCenter().addObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
            getNotificationCenter().addObserver(this, NotificationCenter.replyMessagesDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.reloadHints);
            getNotificationCenter().addObserver(this, NotificationCenter.didUpdateConnectionState);
            getNotificationCenter().addObserver(this, NotificationCenter.onDownloadingFilesChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.needDeleteDialog);
            getNotificationCenter().addObserver(this, NotificationCenter.folderBecomeEmpty);
            getNotificationCenter().addObserver(this, NotificationCenter.newSuggestionsAvailable);
            getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
            getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
            getNotificationCenter().addObserver(this, NotificationCenter.fileLoadProgressChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.dialogsUnreadReactionsCounterChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.forceImportContactsStart);
            getNotificationCenter().addObserver(this, NotificationCenter.userEmojiStatusUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.appUpdateAvailable);
        }
        getNotificationCenter().addObserver(this, NotificationCenter.messagesDeleted);
        getNotificationCenter().addObserver(this, NotificationCenter.onDatabaseMigration);
        getNotificationCenter().addObserver(this, NotificationCenter.onDatabaseOpened);
        getNotificationCenter().addObserver(this, NotificationCenter.didClearDatabase);
        getNotificationCenter().addObserver(this, NotificationCenter.onDatabaseReset);
        getNotificationCenter().addObserver(this, NotificationCenter.storiesUpdated);
        getNotificationCenter().addObserver(this, NotificationCenter.storiesEnabledUpdate);
        getNotificationCenter().addObserver(this, NotificationCenter.unconfirmedAuthUpdate);
        getNotificationCenter().addObserver(this, NotificationCenter.premiumPromoUpdated);
        if (this.initialDialogsType == 0) {
            getNotificationCenter().addObserver(this, NotificationCenter.chatlistFolderUpdate);
            getNotificationCenter().addObserver(this, NotificationCenter.dialogTranslate);
        }
        getNotificationCenter().addObserver(this, NotificationCenter.starBalanceUpdated);
        getNotificationCenter().addObserver(this, NotificationCenter.starSubscriptionsLoaded);
        loadDialogs(getAccountInstance());
        getMessagesController().getStoriesController().loadAllStories();
        getMessagesController().loadPinnedDialogs(this.folderId, 0L, null);
        if (this.databaseMigrationHint != null && !getMessagesStorage().isDatabaseMigrationInProgress()) {
            View view = this.databaseMigrationHint;
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            this.databaseMigrationHint = null;
        }
        if (isArchive()) {
            getMessagesController().getStoriesController().loadHiddenStories();
        } else {
            getMessagesController().getStoriesController().loadStories();
        }
        getContactsController().loadGlobalPrivacySetting();
        if (getMessagesController().savedViewAsChats) {
            getMessagesController().getSavedMessagesController().preloadDialogs(true);
        }
        BirthdayController.getInstance(this.currentAccount).check();
        return true;
    }

    public static void loadDialogs(final AccountInstance accountInstance) {
        int currentAccount = accountInstance.getCurrentAccount();
        if (dialogsLoaded[currentAccount]) {
            return;
        }
        MessagesController messagesController = accountInstance.getMessagesController();
        messagesController.loadGlobalNotificationsSettings();
        messagesController.loadDialogs(0, 0, 100, true);
        messagesController.loadHintDialogs();
        messagesController.loadUserInfo(accountInstance.getUserConfig().getCurrentUser(), false, 0);
        accountInstance.getContactsController().checkInviteText();
        accountInstance.getMediaDataController().checkAllMedia(false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.lambda$loadDialogs$1(AccountInstance.this);
            }
        }, 200L);
        Iterator<String> it = messagesController.diceEmojies.iterator();
        while (it.hasNext()) {
            accountInstance.getMediaDataController().loadStickersByEmojiOrName(it.next(), true, true);
        }
        dialogsLoaded[currentAccount] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadDialogs$1(AccountInstance accountInstance) {
        accountInstance.getDownloadController().loadDownloadingFiles();
    }

    public void updateStatus(TLRPC$User tLRPC$User, boolean z) {
        if (this.statusDrawable == null || this.actionBar == null) {
            return;
        }
        Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(tLRPC$User);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = null;
        if (emojiStatusDocumentId != null) {
            this.statusDrawable.set(emojiStatusDocumentId.longValue(), z);
            this.actionBar.setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda43
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$updateStatus$2(view);
                }
            });
            SelectAnimatedEmojiDialog.preload(this.currentAccount);
        } else if (tLRPC$User != null && MessagesController.getInstance(this.currentAccount).isPremiumUser(tLRPC$User)) {
            if (this.premiumStar == null) {
                this.premiumStar = getContext().getResources().getDrawable(R.drawable.msg_premium_liststar).mutate();
                this.premiumStar = new AnimatedEmojiDrawable.WrapSizeDrawable(this, this.premiumStar, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f)) { // from class: org.telegram.ui.DialogsActivity.3
                    @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.WrapSizeDrawable, android.graphics.drawable.Drawable
                    public void draw(Canvas canvas) {
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(-2.0f), AndroidUtilities.dp(1.0f));
                        super.draw(canvas);
                        canvas.restore();
                    }
                };
            }
            this.premiumStar.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_profile_verifiedBackground), PorterDuff.Mode.MULTIPLY));
            this.statusDrawable.set(this.premiumStar, z);
            this.actionBar.setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda28
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$updateStatus$3(view);
                }
            });
            SelectAnimatedEmojiDialog.preload(this.currentAccount);
        } else {
            this.statusDrawable.set((Drawable) null, z);
            this.actionBar.setRightDrawableOnClick(null);
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.statusDrawable;
        int i = Theme.key_profile_verifiedBackground;
        swapAnimatedEmojiDrawable2.setColor(Integer.valueOf(Theme.getColor(i)));
        DrawerProfileCell.AnimatedStatusView animatedStatusView = this.animatedStatusView;
        if (animatedStatusView != null) {
            animatedStatusView.setColor(Theme.getColor(i));
        }
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = this.selectAnimatedEmojiDialog;
        if (selectAnimatedEmojiDialogWindow == null || !(selectAnimatedEmojiDialogWindow.getContentView() instanceof SelectAnimatedEmojiDialog)) {
            return;
        }
        SimpleTextView titleTextView = this.actionBar.getTitleTextView();
        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = (SelectAnimatedEmojiDialog) this.selectAnimatedEmojiDialog.getContentView();
        if (titleTextView != null) {
            Drawable rightDrawable = titleTextView.getRightDrawable();
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable3 = this.statusDrawable;
            if (rightDrawable == swapAnimatedEmojiDrawable3) {
                swapAnimatedEmojiDrawable = swapAnimatedEmojiDrawable3;
            }
        }
        selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable, titleTextView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStatus$2(View view) {
        DialogStoriesCell dialogStoriesCell;
        if (this.dialogStoriesCellVisible && (dialogStoriesCell = this.dialogStoriesCell) != null && !dialogStoriesCell.isExpanded()) {
            scrollToTop(true, true);
        } else {
            showSelectStatusDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStatus$3(View view) {
        DialogStoriesCell dialogStoriesCell;
        if (this.dialogStoriesCellVisible && (dialogStoriesCell = this.dialogStoriesCell) != null && !dialogStoriesCell.isExpanded()) {
            scrollToTop(true, true);
        } else {
            showSelectStatusDialog();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.searchString == null) {
            getNotificationCenter().removeObserver(this, NotificationCenter.dialogsNeedReload);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
            if (!this.onlySelect) {
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeSearchByActiveAction);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
                getNotificationCenter().removeObserver(this, NotificationCenter.filterSettingsUpdated);
                getNotificationCenter().removeObserver(this, NotificationCenter.dialogFiltersUpdated);
                getNotificationCenter().removeObserver(this, NotificationCenter.dialogsUnreadCounterChanged);
            }
            getNotificationCenter().removeObserver(this, NotificationCenter.updateInterfaces);
            getNotificationCenter().removeObserver(this, NotificationCenter.encryptedChatUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.contactsDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.appDidLogout);
            getNotificationCenter().removeObserver(this, NotificationCenter.openedChatChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByAck);
            getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByServer);
            getNotificationCenter().removeObserver(this, NotificationCenter.messageSendError);
            getNotificationCenter().removeObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
            getNotificationCenter().removeObserver(this, NotificationCenter.replyMessagesDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.reloadHints);
            getNotificationCenter().removeObserver(this, NotificationCenter.didUpdateConnectionState);
            getNotificationCenter().removeObserver(this, NotificationCenter.onDownloadingFilesChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.needDeleteDialog);
            getNotificationCenter().removeObserver(this, NotificationCenter.folderBecomeEmpty);
            getNotificationCenter().removeObserver(this, NotificationCenter.newSuggestionsAvailable);
            getNotificationCenter().removeObserver(this, NotificationCenter.fileLoaded);
            getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadFailed);
            getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.dialogsUnreadReactionsCounterChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.forceImportContactsStart);
            getNotificationCenter().removeObserver(this, NotificationCenter.userEmojiStatusUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.appUpdateAvailable);
        }
        getNotificationCenter().removeObserver(this, NotificationCenter.messagesDeleted);
        getNotificationCenter().removeObserver(this, NotificationCenter.onDatabaseMigration);
        getNotificationCenter().removeObserver(this, NotificationCenter.onDatabaseOpened);
        getNotificationCenter().removeObserver(this, NotificationCenter.didClearDatabase);
        getNotificationCenter().removeObserver(this, NotificationCenter.onDatabaseReset);
        getNotificationCenter().removeObserver(this, NotificationCenter.storiesUpdated);
        getNotificationCenter().removeObserver(this, NotificationCenter.storiesEnabledUpdate);
        getNotificationCenter().removeObserver(this, NotificationCenter.unconfirmedAuthUpdate);
        getNotificationCenter().removeObserver(this, NotificationCenter.premiumPromoUpdated);
        if (this.initialDialogsType == 0) {
            getNotificationCenter().removeObserver(this, NotificationCenter.chatlistFolderUpdate);
            getNotificationCenter().removeObserver(this, NotificationCenter.dialogTranslate);
        }
        getNotificationCenter().removeObserver(this, NotificationCenter.starBalanceUpdated);
        getNotificationCenter().removeObserver(this, NotificationCenter.starSubscriptionsLoaded);
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onDestroy();
        }
        UndoView[] undoViewArr = this.undoView;
        if (undoViewArr[0] != null) {
            undoViewArr[0].hide(true, 0);
        }
        this.notificationsLocker.unlock();
        this.delegate = null;
        SuggestClearDatabaseBottomSheet.dismissDialog();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean dismissDialogOnPause(Dialog dialog) {
        return !(dialog instanceof BotWebViewSheet) && super.dismissDialogOnPause(dialog);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ActionBar createActionBar(Context context) {
        ActionBar actionBar = new ActionBar(context) { // from class: org.telegram.ui.DialogsActivity.4
            @Override // org.telegram.ui.ActionBar.ActionBar, android.view.View
            public void setTranslationY(float f) {
                View view;
                if (f != getTranslationY() && (view = DialogsActivity.this.fragmentView) != null) {
                    view.invalidate();
                }
                super.setTranslationY(f);
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            protected boolean shouldClipChild(View view) {
                return super.shouldClipChild(view) || view == DialogsActivity.this.doneItem;
            }

            @Override // org.telegram.ui.ActionBar.ActionBar, android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (!((BaseFragment) DialogsActivity.this).inPreviewMode || DialogsActivity.this.avatarContainer == null || view == DialogsActivity.this.avatarContainer) {
                    return super.drawChild(canvas, view, j);
                }
                return false;
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            public void setTitleOverlayText(String str, int i, Runnable runnable) {
                super.setTitleOverlayText(str, i, runnable);
                if (DialogsActivity.this.selectAnimatedEmojiDialog != null && (DialogsActivity.this.selectAnimatedEmojiDialog.getContentView() instanceof SelectAnimatedEmojiDialog)) {
                    SimpleTextView titleTextView = getTitleTextView();
                    ((SelectAnimatedEmojiDialog) DialogsActivity.this.selectAnimatedEmojiDialog.getContentView()).setScrimDrawable((titleTextView == null || titleTextView.getRightDrawable() != DialogsActivity.this.statusDrawable) ? null : DialogsActivity.this.statusDrawable, titleTextView);
                }
                DialogStoriesCell dialogStoriesCell = DialogsActivity.this.dialogStoriesCell;
                if (dialogStoriesCell != null) {
                    dialogStoriesCell.setTitleOverlayText(str, i);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            protected boolean onSearchChangedIgnoreTitles() {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                return rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment();
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            public void onSearchFieldVisibilityChanged(boolean z) {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
                    getBackButton().animate().alpha(z ? 1.0f : 0.0f).start();
                }
                super.onSearchFieldVisibilityChanged(z);
            }
        };
        actionBar.setUseContainerForTitles();
        actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSelector), false);
        actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarActionModeDefaultSelector), true);
        actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarDefaultIcon), false);
        actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon), true);
        if (this.inPreviewMode || (AndroidUtilities.isTablet() && this.folderId != 0)) {
            actionBar.setOccupyStatusBar(false);
        }
        return actionBar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0ccd  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0d03  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x0d14  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x0d3a  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x0d7a  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x0e24  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x0eca  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x0ecd  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x0eda  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x0f5f  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x0f6a  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x0f94  */
    /* JADX WARN: Type inference failed for: r0v100, types: [org.telegram.ui.ActionBar.ActionBar] */
    /* JADX WARN: Type inference failed for: r0v17, types: [org.telegram.ui.ActionBar.ActionBar] */
    /* JADX WARN: Type inference failed for: r0v221, types: [org.telegram.ui.DialogsActivity$DialogsRecyclerView, org.telegram.ui.Components.RecyclerListView] */
    /* JADX WARN: Type inference failed for: r0v30, types: [org.telegram.ui.ActionBar.ActionBar] */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r13v4 */
    /* JADX WARN: Type inference failed for: r2v102, types: [org.telegram.ui.DialogsActivity$DialogsRecyclerView, org.telegram.ui.Components.RecyclerListView] */
    /* JADX WARN: Type inference failed for: r2v103, types: [org.telegram.ui.DialogsActivity$DialogsRecyclerView, org.telegram.ui.Components.RecyclerListView] */
    /* JADX WARN: Type inference failed for: r2v104, types: [org.telegram.ui.DialogsActivity$DialogsRecyclerView, org.telegram.ui.Components.RecyclerListView] */
    /* JADX WARN: Type inference failed for: r2v105, types: [org.telegram.ui.DialogsActivity$DialogsRecyclerView, androidx.recyclerview.widget.RecyclerView] */
    /* JADX WARN: Type inference failed for: r2v109, types: [org.telegram.ui.DialogsActivity$DialogsRecyclerView, org.telegram.ui.Components.RecyclerListView] */
    /* JADX WARN: Type inference failed for: r2v110, types: [org.telegram.ui.DialogsActivity$DialogsRecyclerView, org.telegram.ui.Components.RecyclerListView] */
    /* JADX WARN: Type inference failed for: r2v112, types: [androidx.recyclerview.widget.LinearLayoutManager] */
    /* JADX WARN: Type inference failed for: r3v64, types: [android.graphics.drawable.BitmapDrawable] */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r7v4 */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        int i;
        CombinedDrawable combinedDrawable;
        int i2;
        int i3;
        FilterTabsView filterTabsView;
        int i4;
        FrameLayout.LayoutParams createFrame;
        float f;
        int i5;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        ?? r3;
        ?? r13 = 0;
        this.searching = false;
        this.searchWas = false;
        this.wasDrawn = false;
        this.pacmanAnimation = null;
        this.filterTabsView = null;
        this.selectedDialogs.clear();
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                Theme.createChatResources(context, false);
            }
        });
        this.authHintCellVisible = false;
        float f2 = 0.0f;
        this.authHintCellProgress = 0.0f;
        this.authHintCell = null;
        this.dialogsHintCell = null;
        this.dialogsHintCellVisible = false;
        ActionBarMenu createMenu = this.actionBar.createMenu();
        int i6 = 8;
        ?? r7 = 1;
        if (!this.onlySelect && this.searchString == null && this.folderId == 0) {
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, Theme.getColor(Theme.key_actionBarDefaultSelector), Theme.getColor(Theme.key_actionBarDefaultIcon), true);
            this.doneItem = actionBarMenuItem;
            actionBarMenuItem.setText(LocaleController.getString("Done", R.string.Done).toUpperCase());
            this.actionBar.addView(this.doneItem, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 0.0f, 10.0f, 0.0f));
            this.doneItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda36
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$createView$5(view);
                }
            });
            this.doneItem.setAlpha(0.0f);
            this.doneItem.setVisibility(8);
            ProxyDrawable proxyDrawable = new ProxyDrawable(context);
            this.proxyDrawable = proxyDrawable;
            ActionBarMenuItem addItem = createMenu.addItem(2, proxyDrawable);
            this.proxyItem = addItem;
            addItem.setContentDescription(LocaleController.getString("ProxySettings", R.string.ProxySettings));
            RLottieDrawable rLottieDrawable = new RLottieDrawable(R.raw.passcode_lock, "passcode_lock", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            this.passcodeDrawable = rLottieDrawable;
            ActionBarMenuItem addItem2 = createMenu.addItem(1, rLottieDrawable);
            this.passcodeItem = addItem2;
            addItem2.setContentDescription(LocaleController.getString(R.string.AccDescrPasscodeLock));
            ActionBarMenuItem addItem3 = createMenu.addItem(3, new ColorDrawable(0));
            this.downloadsItem = addItem3;
            addItem3.addView(new DownloadProgressIcon(this.currentAccount, context));
            this.downloadsItem.setContentDescription(LocaleController.getString("DownloadsTabs", R.string.DownloadsTabs));
            this.downloadsItem.setVisibility(8);
            updatePasscodeButton();
            updateProxyButton(false, false);
        }
        this.searchItem = createMenu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true, false).setActionBarMenuItemSearchListener(new 5(context, createMenu));
        if (this.initialDialogsType == 2 || (isArchive() && getDialogsArray(this.currentAccount, this.initialDialogsType, this.folderId, false).isEmpty())) {
            this.searchItem.setVisibility(8);
        }
        if (isArchive()) {
            ActionBarMenuItem addItem4 = createMenu.addItem(4, R.drawable.ic_ab_other);
            this.optionsItem = addItem4;
            ActionBarMenuSubItem addSubItem = addItem4.addSubItem(5, R.drawable.msg_customize, LocaleController.getString("ArchiveSettings"));
            int i7 = Theme.key_actionBarDefaultSubmenuItem;
            addSubItem.setColors(getThemedColor(i7), getThemedColor(i7));
            this.optionsItem.addSubItem(6, R.drawable.msg_help, LocaleController.getString("HowDoesItWork")).setColors(getThemedColor(i7), getThemedColor(i7));
            this.optionsItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda38
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$createView$6(view);
                }
            });
        }
        ActionBarMenuItem actionBarMenuItem2 = this.searchItem;
        int i8 = R.string.Search;
        actionBarMenuItem2.setSearchFieldHint(LocaleController.getString("Search", i8));
        this.searchItem.setContentDescription(LocaleController.getString("Search", i8));
        if (this.onlySelect) {
            this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            if (this.isReplyTo) {
                this.actionBar.setTitle(LocaleController.getString(R.string.ReplyToDialog));
            } else if (this.isQuote) {
                this.actionBar.setTitle(LocaleController.getString(R.string.QuoteTo));
            } else {
                int i9 = this.initialDialogsType;
                if (i9 == 3 && this.selectAlertString == null) {
                    this.actionBar.setTitle(LocaleController.getString("ForwardTo", R.string.ForwardTo));
                } else if (i9 == 10) {
                    this.actionBar.setTitle(LocaleController.getString("SelectChats", R.string.SelectChats));
                } else if (i9 == 14) {
                    boolean z = this.allowBots;
                    if (z && !this.allowUsers && !this.allowGroups && !this.allowChannels) {
                        this.actionBar.setTitle(LocaleController.getString("ChooseBot", R.string.ChooseBot));
                    } else {
                        boolean z2 = this.allowUsers;
                        if (z2 && !z && !this.allowGroups && !this.allowChannels) {
                            this.actionBar.setTitle(LocaleController.getString("ChooseUser", R.string.ChooseUser));
                        } else {
                            boolean z3 = this.allowGroups;
                            if (z3 && !z2 && !z && !this.allowChannels) {
                                this.actionBar.setTitle(LocaleController.getString("ChooseGroup", R.string.ChooseGroup));
                            } else if (this.allowChannels && !z2 && !z && !z3) {
                                this.actionBar.setTitle(LocaleController.getString("ChooseChannel", R.string.ChooseChannel));
                            } else {
                                this.actionBar.setTitle(LocaleController.getString("SelectChat", R.string.SelectChat));
                            }
                        }
                    }
                } else {
                    TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
                    if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeUser) {
                        if (((TLRPC$TL_requestPeerTypeUser) tLRPC$RequestPeerType).bot != null) {
                            if (((TLRPC$TL_requestPeerTypeUser) tLRPC$RequestPeerType).bot.booleanValue()) {
                                this.actionBar.setTitle(LocaleController.getString("ChooseBot", R.string.ChooseBot));
                            } else {
                                this.actionBar.setTitle(LocaleController.getString("ChooseUser", R.string.ChooseUser));
                            }
                        } else {
                            this.actionBar.setTitle(LocaleController.getString("ChooseUser", R.string.ChooseUser));
                        }
                    } else if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) {
                        this.actionBar.setTitle(LocaleController.getString("ChooseChannel", R.string.ChooseChannel));
                    } else if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeChat) {
                        this.actionBar.setTitle(LocaleController.getString("ChooseGroup", R.string.ChooseGroup));
                    } else {
                        this.actionBar.setTitle(LocaleController.getString("SelectChat", R.string.SelectChat));
                    }
                }
            }
            this.actionBar.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
        } else {
            if (this.searchString != null || this.folderId != 0) {
                ?? r0 = this.actionBar;
                BackDrawable backDrawable = new BackDrawable(false);
                this.backDrawable = backDrawable;
                r0.setBackButtonDrawable(backDrawable);
            } else {
                ?? r02 = this.actionBar;
                MenuDrawable menuDrawable = new MenuDrawable();
                this.menuDrawable = menuDrawable;
                r02.setBackButtonDrawable(menuDrawable);
                this.menuDrawable.setRoundCap();
                this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", R.string.AccDescrOpenMenu));
            }
            if (this.folderId != 0) {
                this.actionBar.setTitle(LocaleController.getString("ArchivedChats", R.string.ArchivedChats));
            } else {
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(null, AndroidUtilities.dp(26.0f));
                this.statusDrawable = swapAnimatedEmojiDrawable;
                swapAnimatedEmojiDrawable.center = true;
                if (BuildVars.DEBUG_VERSION) {
                    this.actionBar.setTitle(LocaleController.getString("AppNameBeta", R.string.AppNameBeta), this.statusDrawable);
                } else {
                    this.actionBar.setTitle(LocaleController.getString("AppName", R.string.AppName), this.statusDrawable);
                }
                updateStatus(UserConfig.getInstance(this.currentAccount).getCurrentUser(), false);
            }
            if (this.folderId == 0) {
                this.actionBar.setSupportsHolidayImage(true);
            }
        }
        if (!this.onlySelect || this.initialDialogsType == 3) {
            this.actionBar.setAddToContainer(false);
            this.actionBar.setCastShadows(false);
            this.actionBar.setClipContent(true);
        }
        this.actionBar.setTitleActionRunnable(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda80
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$createView$7();
            }
        });
        int i10 = this.initialDialogsType;
        if (((i10 == 0 && !this.onlySelect) || i10 == 3) && this.folderId == 0 && TextUtils.isEmpty(this.searchString)) {
            6 r03 = new 6(context);
            this.filterTabsView = r03;
            r03.setVisibility(8);
            this.canShowFilterTabsView = false;
            this.filterTabsView.setDelegate(new 7(context));
        }
        if (this.allowSwitchAccount && UserConfig.getActivatedAccountsCount() > 1) {
            this.switchItem = createMenu.addItemWithWidth(1, 0, AndroidUtilities.dp(56.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setRoundRadius(AndroidUtilities.dp(18.0f));
            this.switchItem.addView(backupImageView, LayoutHelper.createFrame(36, 36, 17));
            TLRPC$User currentUser = getUserConfig().getCurrentUser();
            avatarDrawable.setInfo(this.currentAccount, currentUser);
            backupImageView.getImageReceiver().setCurrentAccount(this.currentAccount);
            backupImageView.setImage(ImageLocation.getForUserOrChat(currentUser, 1), "50_50", ImageLocation.getForUserOrChat(currentUser, 2), "50_50", (currentUser == null || (tLRPC$UserProfilePhoto = currentUser.photo) == null || (r3 = tLRPC$UserProfilePhoto.strippedBitmap) == null) ? avatarDrawable : r3, currentUser);
            for (int i11 = 0; i11 < 4; i11++) {
                if (AccountInstance.getInstance(i11).getUserConfig().getCurrentUser() != null) {
                    AccountSelectCell accountSelectCell = new AccountSelectCell(context, false);
                    accountSelectCell.setAccount(i11, true);
                    this.switchItem.addSubItem(i11 + 10, accountSelectCell, AndroidUtilities.dp(230.0f), AndroidUtilities.dp(48.0f));
                }
            }
        }
        this.actionBar.setAllowOverlayTitle(true);
        RecyclerView recyclerView = this.sideMenu;
        if (recyclerView != null) {
            int i12 = Theme.key_chats_menuBackground;
            recyclerView.setBackgroundColor(Theme.getColor(i12));
            this.sideMenu.setGlowColor(Theme.getColor(i12));
            this.sideMenu.getAdapter().notifyDataSetChanged();
        }
        this.actionBar.setActionBarMenuOnItemClick(new 8());
        final ContentView contentView = new ContentView(context);
        this.fragmentView = contentView;
        int i13 = (this.folderId != 0 || (((i5 = this.initialDialogsType) != 0 || this.onlySelect) && i5 != 3)) ? 1 : 2;
        this.viewPages = new ViewPage[i13];
        int i14 = 0;
        while (i14 < i13) {
            final ViewPage viewPage = new ViewPage(context) { // from class: org.telegram.ui.DialogsActivity.9
                @Override // android.view.View
                public void setTranslationX(float f3) {
                    if (getTranslationX() != f3) {
                        super.setTranslationX(f3);
                        if (DialogsActivity.this.tabsAnimationInProgress && DialogsActivity.this.viewPages[0] == this) {
                            DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, Math.abs(DialogsActivity.this.viewPages[0].getTranslationX()) / DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        }
                        contentView.invalidateBlur();
                    }
                }
            };
            contentView.addView(viewPage, LayoutHelper.createFrame(-1, -1.0f));
            viewPage.dialogsType = this.initialDialogsType;
            this.viewPages[i14] = viewPage;
            viewPage.progressView = new FlickerLoadingView(context);
            viewPage.progressView.setViewType(7);
            viewPage.progressView.setVisibility(i6);
            viewPage.addView(viewPage.progressView, LayoutHelper.createFrame(-2, -2, 17));
            DialogsRecyclerView dialogsRecyclerView = new DialogsRecyclerView(context, viewPage);
            viewPage.listView = dialogsRecyclerView;
            viewPage.scroller = new RecyclerListViewScroller(dialogsRecyclerView);
            viewPage.listView.setAllowStopHeaveOperations(r7);
            viewPage.listView.setAccessibilityEnabled(r13);
            viewPage.listView.setAnimateEmptyView(r7, r13);
            viewPage.listView.setClipToPadding(r13);
            viewPage.listView.setPivotY(f2);
            if (this.initialDialogsType == 15) {
                viewPage.listView.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));
            }
            viewPage.dialogsItemAnimator = new DialogsItemAnimator(this, viewPage.listView) { // from class: org.telegram.ui.DialogsActivity.10
                @Override // androidx.recyclerview.widget.SimpleItemAnimator
                public void onRemoveStarting(RecyclerView.ViewHolder viewHolder) {
                    super.onRemoveStarting(viewHolder);
                    if (viewPage.layoutManager.findFirstVisibleItemPosition() == 0) {
                        View findViewByPosition = viewPage.layoutManager.findViewByPosition(0);
                        if (findViewByPosition != null) {
                            findViewByPosition.invalidate();
                        }
                        if (viewPage.archivePullViewState == 2) {
                            viewPage.archivePullViewState = 1;
                        }
                        if (viewPage.pullForegroundDrawable != null) {
                            viewPage.pullForegroundDrawable.doNotShow();
                        }
                    }
                }
            };
            viewPage.listView.setVerticalScrollBarEnabled(r7);
            viewPage.listView.setInstantClick(r7);
            viewPage.layoutManager = new 11(context, viewPage);
            viewPage.layoutManager.setOrientation(r7);
            viewPage.listView.setLayoutManager(viewPage.layoutManager);
            viewPage.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
            viewPage.addView(viewPage.listView, LayoutHelper.createFrame(-1, -1.0f));
            viewPage.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda136
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ boolean hasDoubleTap(View view, int i15) {
                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i15);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ void onDoubleTap(View view, int i15, float f3, float f4) {
                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i15, f3, f4);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public final void onItemClick(View view, int i15, float f3, float f4) {
                    DialogsActivity.this.lambda$createView$8(viewPage, view, i15, f3, f4);
                }
            });
            viewPage.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity.13
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public boolean onItemClick(View view, int i15, float f3, float f4) {
                    if (view instanceof DialogCell) {
                        DialogCell dialogCell = (DialogCell) view;
                        if (dialogCell.isBlocked()) {
                            DialogsActivity.this.showPremiumBlockedToast(view, dialogCell.getDialogId());
                            return true;
                        }
                    }
                    if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0 && DialogsActivity.this.filterTabsView.isEditing()) {
                        return false;
                    }
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    ViewPage viewPage2 = viewPage;
                    return dialogsActivity.onItemLongClick(viewPage2.listView, view, i15, f3, f4, viewPage2.dialogsType, viewPage.dialogsAdapter);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public void onMove(float f3, float f4) {
                    Point point = AndroidUtilities.displaySize;
                    if (point.x > point.y) {
                        DialogsActivity.this.movePreviewFragment(f4);
                    }
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public void onLongClickRelease() {
                    Point point = AndroidUtilities.displaySize;
                    if (point.x > point.y) {
                        DialogsActivity.this.finishPreviewFragment();
                    }
                }
            });
            viewPage.swipeController = new SwipeController(viewPage);
            viewPage.recyclerItemsEnterAnimator = new RecyclerItemsEnterAnimator(viewPage.listView, r13);
            viewPage.itemTouchhelper = new ItemTouchHelper(viewPage.swipeController);
            viewPage.itemTouchhelper.attachToRecyclerView(viewPage.listView);
            viewPage.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.DialogsActivity.14
                private boolean wasManualScroll;

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView2, int i15) {
                    if (i15 != 1) {
                        DialogsActivity.this.scrollingManually = false;
                    } else {
                        this.wasManualScroll = true;
                        DialogsActivity.this.scrollingManually = true;
                        DialogsActivity.this.viewPages[0].scroller.cancel();
                    }
                    if (i15 == 0) {
                        this.wasManualScroll = false;
                        DialogsActivity.this.disableActionBarScrolling = false;
                        if (DialogsActivity.this.waitingForScrollFinished) {
                            DialogsActivity.this.waitingForScrollFinished = false;
                            if (DialogsActivity.this.updatePullAfterScroll) {
                                viewPage.listView.updatePullState();
                                DialogsActivity.this.updatePullAfterScroll = false;
                            }
                            viewPage.dialogsAdapter.notifyDataSetChanged();
                        }
                        DialogsActivity.this.checkAutoscrollToStories(viewPage);
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView2, int i15, int i16) {
                    DialogsRecyclerView dialogsRecyclerView2;
                    View childAt;
                    boolean z4;
                    boolean z5;
                    ContentView contentView2 = contentView;
                    if (contentView2 != null) {
                        contentView2.updateBlurContent();
                    }
                    viewPage.dialogsItemAnimator.onListScroll(-i16);
                    boolean z6 = false;
                    int i17 = -1;
                    int i18 = -1;
                    for (int i19 = 0; i19 < recyclerView2.getChildCount(); i19++) {
                        int childAdapterPosition = recyclerView2.getChildAdapterPosition(recyclerView2.getChildAt(i19));
                        if (childAdapterPosition >= 0) {
                            if (i17 == -1 || childAdapterPosition > i17) {
                                i17 = childAdapterPosition;
                            }
                            if (i18 == -1 || childAdapterPosition < i18) {
                                i18 = childAdapterPosition;
                            }
                        }
                    }
                    DialogsActivity.this.checkListLoad(viewPage);
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories) {
                        dialogsActivity.invalidateScrollY = true;
                        View view = DialogsActivity.this.fragmentView;
                        if (view != null) {
                            view.invalidate();
                        }
                    }
                    if (DialogsActivity.this.initialDialogsType != 10 && this.wasManualScroll && ((DialogsActivity.this.floatingButtonContainer.getVisibility() != 8 || !DialogsActivity.this.storiesEnabled) && recyclerView2.getChildCount() > 0 && i18 != -1)) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView2.findViewHolderForAdapterPosition(i18);
                        if (!DialogsActivity.this.hasHiddenArchive() || (findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.getAdapterPosition() >= 0)) {
                            int top = findViewHolderForAdapterPosition != null ? findViewHolderForAdapterPosition.itemView.getTop() : 0;
                            if (DialogsActivity.this.prevPosition == i18) {
                                int i20 = DialogsActivity.this.prevTop - top;
                                z4 = top < DialogsActivity.this.prevTop;
                                if (Math.abs(i20) <= 1) {
                                    z5 = false;
                                    if (z5 && DialogsActivity.this.scrollUpdated && (z4 || DialogsActivity.this.scrollingManually)) {
                                        DialogsActivity.this.hideFloatingButton(z4);
                                    }
                                    DialogsActivity.this.prevPosition = i18;
                                    DialogsActivity.this.prevTop = top;
                                    DialogsActivity.this.scrollUpdated = true;
                                }
                            } else {
                                z4 = i18 > DialogsActivity.this.prevPosition;
                            }
                            z5 = true;
                            if (z5) {
                                DialogsActivity.this.hideFloatingButton(z4);
                            }
                            DialogsActivity.this.prevPosition = i18;
                            DialogsActivity.this.prevTop = top;
                            DialogsActivity.this.scrollUpdated = true;
                        }
                    }
                    DialogsActivity dialogsActivity2 = DialogsActivity.this;
                    if (!dialogsActivity2.hasStories && dialogsActivity2.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0 && DialogsActivity.this.filterTabsViewIsVisible && recyclerView2 == DialogsActivity.this.viewPages[0].listView && !DialogsActivity.this.searching && !((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed() && !DialogsActivity.this.disableActionBarScrolling && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                        if (i16 > 0 && DialogsActivity.this.hasHiddenArchive() && DialogsActivity.this.viewPages[0].dialogsType == 0 && (childAt = recyclerView2.getChildAt(0)) != null && recyclerView2.getChildViewHolder(childAt).getAdapterPosition() == 0) {
                            int measuredHeight = childAt.getMeasuredHeight() + (childAt.getTop() - recyclerView2.getPaddingTop());
                            if (measuredHeight + i16 > 0) {
                                if (measuredHeight >= 0) {
                                    return;
                                }
                                i16 = -measuredHeight;
                            }
                        }
                        float f3 = DialogsActivity.this.scrollYOffset;
                        float f4 = f3 - i16;
                        DialogsActivity dialogsActivity3 = DialogsActivity.this;
                        if (dialogsActivity3.hasStories) {
                            dialogsActivity3.invalidateScrollY = true;
                            View view2 = DialogsActivity.this.fragmentView;
                            if (view2 != null) {
                                view2.invalidate();
                            }
                        } else {
                            z6 = true;
                        }
                        if (z6) {
                            float f5 = -DialogsActivity.this.getMaxScrollYOffset();
                            if (f4 < f5) {
                                f4 = f5;
                            } else if (f4 > 0.0f) {
                                f4 = 0.0f;
                            }
                            if (f4 != f3) {
                                DialogsActivity.this.setScrollY(f4);
                            }
                        }
                    }
                    View view3 = DialogsActivity.this.fragmentView;
                    if (view3 != null) {
                        ((SizeNotifierFrameLayout) view3).invalidateBlur();
                    }
                    RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                    if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment() && (dialogsRecyclerView2 = viewPage.listView) != null) {
                        dialogsRecyclerView2.invalidate();
                    }
                    DialogStoriesCell dialogStoriesCell = DialogsActivity.this.dialogStoriesCell;
                    if (dialogStoriesCell == null || dialogStoriesCell.getPremiumHint() == null || !DialogsActivity.this.dialogStoriesCell.getPremiumHint().shown()) {
                        return;
                    }
                    DialogsActivity.this.dialogStoriesCell.getPremiumHint().hide();
                }
            });
            viewPage.archivePullViewState = SharedConfig.archiveHidden ? 2 : 0;
            if (viewPage.pullForegroundDrawable == null && this.folderId == 0) {
                viewPage.pullForegroundDrawable = new PullForegroundDrawable(this, LocaleController.getString("AccSwipeForArchive", R.string.AccSwipeForArchive), LocaleController.getString("AccReleaseForArchive", R.string.AccReleaseForArchive)) { // from class: org.telegram.ui.DialogsActivity.15
                    @Override // org.telegram.ui.Components.PullForegroundDrawable
                    protected float getViewOffset() {
                        return viewPage.listView.getViewOffset();
                    }
                };
                if (hasHiddenArchive()) {
                    viewPage.pullForegroundDrawable.showHidden();
                } else {
                    viewPage.pullForegroundDrawable.doNotShow();
                }
                viewPage.pullForegroundDrawable.setWillDraw(viewPage.archivePullViewState != 0);
            }
            int i15 = i14;
            int i16 = i13;
            ContentView contentView2 = contentView;
            viewPage.dialogsAdapter = new DialogsAdapter(this, context, viewPage.dialogsType, this.folderId, this.onlySelect, this.selectedDialogs, this.currentAccount, this.requestPeerType) { // from class: org.telegram.ui.DialogsActivity.16
                @Override // org.telegram.ui.Adapters.DialogsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
                public void notifyDataSetChanged() {
                    viewPage.lastItemsCount = getItemCount();
                    try {
                        super.notifyDataSetChanged();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (DialogsActivity.this.initialDialogsType == 15) {
                        DialogsActivity.this.searchItem.setVisibility(this.isEmpty ? 8 : 0);
                    }
                }

                @Override // org.telegram.ui.Adapters.DialogsAdapter, org.telegram.ui.Cells.DialogCell.DialogCellDelegate
                public void onButtonClicked(DialogCell dialogCell) {
                    TLRPC$TL_forumTopic findTopic;
                    if (dialogCell.getMessage() == null || (findTopic = DialogsActivity.this.getMessagesController().getTopicsController().findTopic(-dialogCell.getDialogId(), MessageObject.getTopicId(((BaseFragment) DialogsActivity.this).currentAccount, dialogCell.getMessage().messageOwner, true))) == null) {
                        return;
                    }
                    if (DialogsActivity.this.onlySelect) {
                        DialogsActivity.this.didSelectResult(dialogCell.getDialogId(), findTopic.id, false, false);
                    } else {
                        ForumUtilities.openTopic(DialogsActivity.this, -dialogCell.getDialogId(), findTopic, 0);
                    }
                }

                @Override // org.telegram.ui.Adapters.DialogsAdapter, org.telegram.ui.Cells.DialogCell.DialogCellDelegate
                public void onButtonLongPress(DialogCell dialogCell) {
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    DialogsRecyclerView dialogsRecyclerView2 = viewPage.listView;
                    dialogsActivity.onItemLongClick(dialogsRecyclerView2, dialogCell, dialogsRecyclerView2.getChildAdapterPosition(dialogCell), 0.0f, 0.0f, viewPage.dialogsType, viewPage.dialogsAdapter);
                }

                @Override // org.telegram.ui.Adapters.DialogsAdapter
                public void onCreateGroupForThisClick() {
                    DialogsActivity.this.createGroupForThis();
                }

                @Override // org.telegram.ui.Adapters.DialogsAdapter
                protected void onArchiveSettingsClick() {
                    DialogsActivity.this.presentFragment(new ArchiveSettingsActivity());
                }
            };
            viewPage.dialogsAdapter.setRecyclerListView(viewPage.listView);
            viewPage.dialogsAdapter.setForceShowEmptyCell(this.afterSignup);
            if (AndroidUtilities.isTablet() && this.openedDialogId.dialogId != 0) {
                viewPage.dialogsAdapter.setOpenedDialogId(this.openedDialogId.dialogId);
            }
            viewPage.dialogsAdapter.setArchivedPullDrawable(viewPage.pullForegroundDrawable);
            viewPage.listView.setAdapter(viewPage.dialogsAdapter);
            viewPage.listView.setEmptyView(this.folderId == 0 ? viewPage.progressView : null);
            viewPage.scrollHelper = new RecyclerAnimationScrollHelper(viewPage.listView, viewPage.layoutManager);
            viewPage.scrollHelper.forceUseStableId = true;
            viewPage.scrollHelper.isDialogs = true;
            viewPage.scrollHelper.setScrollListener(new RecyclerAnimationScrollHelper.ScrollListener() { // from class: org.telegram.ui.DialogsActivity.17
                @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.ScrollListener
                public void onScroll() {
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories) {
                        dialogsActivity.invalidateScrollY = true;
                        DialogsActivity.this.fragmentView.invalidate();
                    }
                }
            });
            if (i15 != 0) {
                this.viewPages[i15].setVisibility(8);
            }
            i14 = i15 + 1;
            i13 = i16;
            contentView = contentView2;
            r7 = 1;
            i6 = 8;
            r13 = 0;
            f2 = 0.0f;
        }
        final ContentView contentView3 = contentView;
        this.searchViewPagerIndex = contentView3.getChildCount();
        FiltersView filtersView = new FiltersView(getParentActivity(), null);
        this.filtersView = filtersView;
        filtersView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda132
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i17) {
                DialogsActivity.this.lambda$createView$9(view, i17);
            }
        });
        contentView3.addView(this.filtersView, LayoutHelper.createFrame(-1, -2, 48));
        this.filtersView.setVisibility(8);
        if (this.initialDialogsType != 10) {
            FrameLayout frameLayout = new FrameLayout(context);
            this.floatingButton2Container = frameLayout;
            frameLayout.setVisibility(((!this.onlySelect || this.initialDialogsType == 10) && this.folderId == 0 && this.storiesEnabled) ? 0 : 8);
            FrameLayout frameLayout2 = this.floatingButton2Container;
            int i17 = Build.VERSION.SDK_INT;
            int i18 = i17 >= 21 ? 36 : 40;
            float f3 = i17 >= 21 ? 36 : 40;
            boolean z4 = LocaleController.isRTL;
            contentView3.addView(frameLayout2, LayoutHelper.createFrame(i18, f3, (z4 ? 3 : 5) | 80, z4 ? 24.0f : 0.0f, 0.0f, z4 ? 0.0f : 24.0f, 82.0f));
            this.floatingButton2Container.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda35
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$createView$10(view);
                }
            });
            if (i17 >= 21) {
                StateListAnimator stateListAnimator = new StateListAnimator();
                FrameLayout frameLayout3 = this.floatingButton2Container;
                Property property = View.TRANSLATION_Z;
                i = 2;
                stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(frameLayout3, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton2Container, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                this.floatingButton2Container.setStateListAnimator(stateListAnimator);
                this.floatingButton2Container.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.DialogsActivity.18
                    @Override // android.view.ViewOutlineProvider
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view, Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
                    }
                });
            } else {
                i = 2;
            }
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.floatingButton2 = rLottieImageView;
            rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
            RLottieImageView rLottieImageView2 = this.floatingButton2;
            int i19 = Theme.key_windowBackgroundWhiteGrayIcon;
            rLottieImageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i19), PorterDuff.Mode.MULTIPLY));
            this.floatingButton2.setImageResource(R.drawable.fab_compose_small);
            this.floatingButton2Container.setContentDescription(LocaleController.getString("NewMessageTitle", R.string.NewMessageTitle));
            this.floatingButton2Container.addView(this.floatingButton2, LayoutHelper.createFrame(-1, -1.0f));
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.floating2ProgressView = radialProgressView;
            radialProgressView.setProgressColor(Theme.getColor(i19));
            this.floating2ProgressView.setScaleX(0.1f);
            this.floating2ProgressView.setScaleY(0.1f);
            this.floating2ProgressView.setAlpha(0.0f);
            this.floating2ProgressView.setVisibility(8);
            this.floating2ProgressView.setSize(AndroidUtilities.dp(22.0f));
            this.floatingButton2Container.addView(this.floating2ProgressView, LayoutHelper.createFrame(-1, -1.0f));
        } else {
            i = 2;
        }
        FrameLayout frameLayout4 = new FrameLayout(context);
        this.floatingButtonContainer = frameLayout4;
        frameLayout4.setVisibility(((!this.onlySelect || this.initialDialogsType == 10) && this.folderId == 0) ? 0 : 8);
        FrameLayout frameLayout5 = this.floatingButtonContainer;
        int i20 = Build.VERSION.SDK_INT;
        int i21 = i20 >= 21 ? 56 : 60;
        float f4 = i20 >= 21 ? 56 : 60;
        boolean z5 = LocaleController.isRTL;
        contentView3.addView(frameLayout5, LayoutHelper.createFrame(i21, f4, (z5 ? 3 : 5) | 80, z5 ? 14.0f : 0.0f, 0.0f, z5 ? 0.0f : 14.0f, 14.0f));
        this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda37
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DialogsActivity.this.lambda$createView$11(view);
            }
        });
        if (!isArchive() && this.initialDialogsType == 0 && MessagesController.getInstance(this.currentAccount).getMainSettings().getBoolean("storyhint", true)) {
            HintView2 onHiddenListener = new HintView2(context, i).setRounding(8.0f).setDuration(8000L).setCloseButton(true).setMaxWidth(165.0f).setMultilineText(true).setText(AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.StoryCameraHint), StoryRecorder.cameraBtnSpan(context))).setJoint(1.0f, -40.0f).setBgColor(getThemedColor(Theme.key_undo_background)).setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda69
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$createView$12();
                }
            });
            this.storyHint = onHiddenListener;
            contentView3.addView(onHiddenListener, LayoutHelper.createFrame(-1, 160.0f, 87, 0.0f, 0.0f, 80.0f, 0.0f));
        }
        RLottieImageView rLottieImageView3 = new RLottieImageView(context);
        this.floatingButton = rLottieImageView3;
        rLottieImageView3.setScaleType(ImageView.ScaleType.CENTER);
        this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
        if (i20 >= 21) {
            StateListAnimator stateListAnimator2 = new StateListAnimator();
            FrameLayout frameLayout6 = this.floatingButtonContainer;
            Property property2 = View.TRANSLATION_Z;
            float[] fArr = new float[i];
            fArr[0] = AndroidUtilities.dp(2.0f);
            fArr[1] = AndroidUtilities.dp(4.0f);
            stateListAnimator2.addState(new int[]{16842919}, ObjectAnimator.ofFloat(frameLayout6, property2, fArr).setDuration(200L));
            stateListAnimator2.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonContainer, property2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            this.floatingButtonContainer.setStateListAnimator(stateListAnimator2);
            this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.DialogsActivity.20
                @Override // android.view.ViewOutlineProvider
                @SuppressLint({"NewApi"})
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        if (i20 < 21) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable2 = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable2.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            combinedDrawable = combinedDrawable2;
        } else {
            combinedDrawable = createSimpleSelectorCircleDrawable;
        }
        this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(-1, -1.0f));
        updateFloatingButtonColor();
        updateStoriesPosting();
        this.searchTabsView = null;
        if (!this.onlySelect && this.initialDialogsType == 0) {
            FragmentContextView fragmentContextView = new FragmentContextView(context, this, true);
            this.fragmentLocationContextView = fragmentContextView;
            fragmentContextView.setLayoutParams(LayoutHelper.createFrame(-1, 38.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            contentView3.addView(this.fragmentLocationContextView);
            FragmentContextView fragmentContextView2 = new FragmentContextView(context, this, false);
            this.fragmentContextView = fragmentContextView2;
            fragmentContextView2.setLayoutParams(LayoutHelper.createFrame(-1, 38.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            contentView3.addView(this.fragmentContextView);
            this.fragmentContextView.setAdditionalContextView(this.fragmentLocationContextView);
            this.fragmentLocationContextView.setAdditionalContextView(this.fragmentContextView);
            DialogsHintCell dialogsHintCell = new DialogsHintCell(context, contentView3);
            this.dialogsHintCell = dialogsHintCell;
            dialogsHintCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourceProvider));
            lambda$updateDialogsHint$29();
            CacheControlActivity.calculateTotalSize(new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda117
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.this.lambda$createView$13((Long) obj);
                }
            });
            CacheControlActivity.getDeviceTotalSize(new Utilities.Callback2() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda113
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    DialogsActivity.this.lambda$createView$14((Long) obj, (Long) obj2);
                }
            });
            contentView3.addView(this.dialogsHintCell);
        } else if (this.initialDialogsType == 3) {
            ChatActivityEnterView chatActivityEnterView = this.commentView;
            if (chatActivityEnterView != null) {
                chatActivityEnterView.onDestroy();
            }
            i2 = -1;
            this.commentView = new ChatActivityEnterView(getParentActivity(), contentView3, null, false) { // from class: org.telegram.ui.DialogsActivity.21
                @Override // android.view.ViewGroup, android.view.View
                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0) {
                        AndroidUtilities.requestAdjustResize(DialogsActivity.this.getParentActivity(), ((BaseFragment) DialogsActivity.this).classGuid);
                    }
                    return super.dispatchTouchEvent(motionEvent);
                }

                @Override // android.view.View
                public void setTranslationY(float f5) {
                    super.setTranslationY(f5);
                }
            };
            contentView3.setClipChildren(false);
            contentView3.setClipToPadding(false);
            ChatActivityEnterView chatActivityEnterView2 = this.commentView;
            chatActivityEnterView2.allowBlur = false;
            chatActivityEnterView2.forceSmoothKeyboard(true);
            this.commentView.setAllowStickersAndGifs(true, false, false);
            this.commentView.setForceShowSendButton(true, false);
            this.commentView.setPadding(0, 0, AndroidUtilities.dp(20.0f), 0);
            this.commentView.setVisibility(8);
            this.commentView.getSendButton().setAlpha(0.0f);
            View view = new View(getParentActivity());
            this.commentViewBg = view;
            view.setBackgroundColor(getThemedColor(Theme.key_chat_messagePanelBackground));
            contentView3.addView(this.commentViewBg, LayoutHelper.createFrame(-1, 1600.0f, 87, 0.0f, 0.0f, 0.0f, -1600.0f));
            contentView3.addView(this.commentView, LayoutHelper.createFrame(-1, -2, 83));
            this.commentView.setDelegate(new ChatActivityEnterView.ChatActivityEnterViewDelegate() { // from class: org.telegram.ui.DialogsActivity.22
                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void bottomPanelTranslationYChanged(float f5) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ boolean checkCanRemoveRestrictionsByBoosts() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$checkCanRemoveRestrictionsByBoosts(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void didPressAttachButton() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ int getContentViewHeight() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getContentViewHeight(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ ChatActivity.ReplyQuote getReplyQuote() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getReplyQuote(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ TL_stories$StoryItem getReplyToStory() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getReplyToStory(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ TLRPC$TL_channels_sendAsPeers getSendAsPeers() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getSendAsPeers(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ boolean hasForwardingMessages() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$hasForwardingMessages(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ boolean hasScheduledMessages() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$hasScheduledMessages(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ int measureKeyboardHeight() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$measureKeyboardHeight(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needChangeVideoPreviewState(int i22, float f5) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needSendTyping() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needShowMediaBanHint() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needStartRecordAudio(int i22) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needStartRecordVideo(int i22, boolean z6, int i23, int i24, long j) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onAttachButtonHidden() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onAttachButtonShow() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onAudioVideoInterfaceUpdated() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onContextMenuClose() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onContextMenuClose(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onContextMenuOpen() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onContextMenuOpen(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onEditTextScroll() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onEditTextScroll(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onKeyboardRequested() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onKeyboardRequested(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onMessageEditEnd(boolean z6) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onPreAudioVideoRecord() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onSendLongClick() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onStickersExpandedChange() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onStickersTab(boolean z6) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onSwitchRecordMode(boolean z6) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onTextChanged(CharSequence charSequence, boolean z6, boolean z7) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onTextSelectionChanged(int i22, int i23) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onTextSpansChanged(CharSequence charSequence) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onTrendingStickersShowed(boolean z6) {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onTrendingStickersShowed(this, z6);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onUpdateSlowModeButton(View view2, boolean z6, CharSequence charSequence) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onWindowSizeChanged(int i22) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ boolean onceVoiceAvailable() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onceVoiceAvailable(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void openScheduledMessages() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$openScheduledMessages(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void prepareMessageSending() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$prepareMessageSending(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void scrollToSendingMessage() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$scrollToSendingMessage(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void toggleVideoRecordingPause() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onMessageSend(CharSequence charSequence, boolean z6, int i22) {
                    if (DialogsActivity.this.delegate == null || DialogsActivity.this.selectedDialogs.isEmpty()) {
                        return;
                    }
                    ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                    for (int i23 = 0; i23 < DialogsActivity.this.selectedDialogs.size(); i23++) {
                        arrayList.add(MessagesStorage.TopicKey.of(((Long) DialogsActivity.this.selectedDialogs.get(i23)).longValue(), 0L));
                    }
                    DialogsActivity.this.delegate.didSelectDialogs(DialogsActivity.this, arrayList, charSequence, false, null);
                }
            });
            FrameLayout frameLayout7 = new FrameLayout(context) { // from class: org.telegram.ui.DialogsActivity.23
                @Override // android.view.View
                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                    accessibilityNodeInfo.setText(LocaleController.formatPluralString("AccDescrShareInChats", DialogsActivity.this.selectedDialogs.size(), new Object[0]));
                    accessibilityNodeInfo.setClassName(Button.class.getName());
                    accessibilityNodeInfo.setLongClickable(true);
                    accessibilityNodeInfo.setClickable(true);
                }
            };
            this.writeButtonContainer = frameLayout7;
            frameLayout7.setFocusable(true);
            this.writeButtonContainer.setFocusableInTouchMode(true);
            i3 = 4;
            this.writeButtonContainer.setVisibility(4);
            this.writeButtonContainer.setScaleX(0.2f);
            this.writeButtonContainer.setScaleY(0.2f);
            this.writeButtonContainer.setAlpha(0.0f);
            contentView3.addView(this.writeButtonContainer, LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 6.0f, 10.0f));
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            this.textPaint.setTypeface(AndroidUtilities.bold());
            View view2 = new View(context) { // from class: org.telegram.ui.DialogsActivity.24
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    String format = String.format("%d", Integer.valueOf(Math.max(1, DialogsActivity.this.selectedDialogs.size())));
                    int ceil = (int) Math.ceil(DialogsActivity.this.textPaint.measureText(format));
                    int max = Math.max(AndroidUtilities.dp(16.0f) + ceil, AndroidUtilities.dp(24.0f));
                    int measuredWidth = getMeasuredWidth() / 2;
                    int measuredHeight = getMeasuredHeight() / 2;
                    DialogsActivity.this.textPaint.setColor(DialogsActivity.this.getThemedColor(Theme.key_dialogRoundCheckBoxCheck));
                    DialogsActivity.this.paint.setColor(DialogsActivity.this.getThemedColor(Theme.isCurrentThemeDark() ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogBackground));
                    int i22 = max / 2;
                    int i23 = measuredWidth - i22;
                    int i24 = i22 + measuredWidth;
                    DialogsActivity.this.rect.set(i23, 0.0f, i24, getMeasuredHeight());
                    canvas.drawRoundRect(DialogsActivity.this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), DialogsActivity.this.paint);
                    DialogsActivity.this.paint.setColor(DialogsActivity.this.getThemedColor(Theme.key_dialogRoundCheckBox));
                    DialogsActivity.this.rect.set(i23 + AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), i24 - AndroidUtilities.dp(2.0f), getMeasuredHeight() - AndroidUtilities.dp(2.0f));
                    canvas.drawRoundRect(DialogsActivity.this.rect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), DialogsActivity.this.paint);
                    canvas.drawText(format, measuredWidth - (ceil / 2), AndroidUtilities.dp(16.2f), DialogsActivity.this.textPaint);
                }
            };
            this.selectedCountView = view2;
            view2.setAlpha(0.0f);
            this.selectedCountView.setScaleX(0.2f);
            this.selectedCountView.setScaleY(0.2f);
            contentView3.addView(this.selectedCountView, LayoutHelper.createFrame(42, 24.0f, 85, 0.0f, 0.0f, -8.0f, 9.0f));
            final FrameLayout frameLayout8 = new FrameLayout(context);
            int dp = AndroidUtilities.dp(56.0f);
            int i22 = Theme.key_dialogFloatingButton;
            int themedColor = getThemedColor(i22);
            if (i20 >= 21) {
                i22 = Theme.key_dialogFloatingButtonPressed;
            }
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(dp, themedColor, getThemedColor(i22));
            if (i20 < 21) {
                Drawable mutate2 = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                mutate2.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable3 = new CombinedDrawable(mutate2, combinedDrawable, 0, 0);
                combinedDrawable3.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                createSimpleSelectorCircleDrawable2 = combinedDrawable3;
            }
            frameLayout8.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
            frameLayout8.setImportantForAccessibility(2);
            if (i20 >= 21) {
                frameLayout8.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.DialogsActivity.25
                    @Override // android.view.ViewOutlineProvider
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view3, Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    }
                });
            }
            frameLayout8.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda26
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    DialogsActivity.this.lambda$createView$15(view3);
                }
            });
            frameLayout8.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda57
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view3) {
                    boolean lambda$createView$16;
                    lambda$createView$16 = DialogsActivity.this.lambda$createView$16(frameLayout8, view3);
                    return lambda$createView$16;
                }
            });
            this.writeButton = new ImageView[2];
            int i23 = 0;
            for (int i24 = 2; i23 < i24; i24 = 2) {
                this.writeButton[i23] = new ImageView(context);
                this.writeButton[i23].setImageResource(i23 == 1 ? R.drawable.msg_arrow_forward : R.drawable.attach_send);
                this.writeButton[i23].setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogFloatingIcon), PorterDuff.Mode.MULTIPLY));
                this.writeButton[i23].setScaleType(ImageView.ScaleType.CENTER);
                ImageView imageView = this.writeButton[i23];
                int i25 = Build.VERSION.SDK_INT;
                frameLayout8.addView(imageView, LayoutHelper.createFrame(i25 >= 21 ? 56 : 60, i25 >= 21 ? 56 : 60, 17));
                i23++;
            }
            AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[0], true, 0.5f, false);
            AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[1], false, 0.5f, false);
            FrameLayout frameLayout9 = this.writeButtonContainer;
            int i26 = Build.VERSION.SDK_INT;
            frameLayout9.addView(frameLayout8, LayoutHelper.createFrame(i26 >= 21 ? 56 : 60, i26 >= 21 ? 56.0f : 60.0f, 51, i26 >= 21 ? 2.0f : 0.0f, 0.0f, 0.0f, 0.0f));
            filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                contentView3.addView(filterTabsView, LayoutHelper.createFrame(i2, 44.0f));
            }
            26 r6 = new 26(context, this, this.currentAccount, isArchive() ? 1 : 0);
            this.dialogStoriesCell = r6;
            r6.setActionBar(this.actionBar);
            DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
            dialogStoriesCell.allowGlobalUpdates = false;
            dialogStoriesCell.setVisibility(8);
            this.animateToHasStories = false;
            this.hasOnlySlefStories = false;
            this.hasStories = false;
            if (this.onlySelect) {
                i4 = 3;
            } else {
                i4 = 3;
                if (this.initialDialogsType == 3) {
                    MessagesController.getInstance(this.currentAccount).getSavedReactionTags(0L);
                }
            }
            if (this.onlySelect || this.initialDialogsType == i4) {
                createFrame = LayoutHelper.createFrame(i2, -2.0f);
                if (this.inPreviewMode && Build.VERSION.SDK_INT >= 21) {
                    createFrame.topMargin = AndroidUtilities.statusBarHeight;
                }
                contentView3.addView(this.actionBar, createFrame);
            }
            if (!this.onlySelect) {
                DrawerProfileCell.AnimatedStatusView animatedStatusView = new DrawerProfileCell.AnimatedStatusView(context, 20, 60);
                this.animatedStatusView = animatedStatusView;
                contentView3.addView(animatedStatusView, LayoutHelper.createFrame(20, 20, 51));
            }
            if (this.searchString == null && this.initialDialogsType == 0) {
                FrameLayout frameLayout10 = new FrameLayout(context) { // from class: org.telegram.ui.DialogsActivity.27
                    private int lastGradientWidth;
                    private LinearGradient updateGradient;
                    private Paint paint = new Paint();
                    private Matrix matrix = new Matrix();

                    @Override // android.view.View
                    public void draw(Canvas canvas) {
                        if (this.updateGradient != null) {
                            this.paint.setColor(-1);
                            this.paint.setShader(this.updateGradient);
                            this.updateGradient.setLocalMatrix(this.matrix);
                            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.paint);
                            DialogsActivity.this.updateLayoutIcon.setBackgroundGradientDrawable(this.updateGradient);
                            DialogsActivity.this.updateLayoutIcon.draw(canvas);
                        }
                        super.draw(canvas);
                    }

                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i27, int i28) {
                        super.onMeasure(i27, i28);
                        int size = View.MeasureSpec.getSize(i27);
                        if (this.lastGradientWidth != size) {
                            this.updateGradient = new LinearGradient(0.0f, 0.0f, size, 0.0f, new int[]{-9846926, -11291731}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                            this.lastGradientWidth = size;
                        }
                        int measuredWidth = (getMeasuredWidth() - DialogsActivity.this.updateTextView.getMeasuredWidth()) / 2;
                        DialogsActivity.this.updateLayoutIcon.setProgressRect(measuredWidth, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(22.0f) + measuredWidth, AndroidUtilities.dp(35.0f));
                    }

                    @Override // android.view.View
                    public void setTranslationY(float f5) {
                        super.setTranslationY(f5);
                        DialogsActivity.this.additionalFloatingTranslation2 = AndroidUtilities.dp(48.0f) - f5;
                        if (DialogsActivity.this.additionalFloatingTranslation2 < 0.0f) {
                            DialogsActivity.this.additionalFloatingTranslation2 = 0.0f;
                        }
                        if (DialogsActivity.this.floatingHidden) {
                            return;
                        }
                        DialogsActivity.this.updateFloatingButtonOffset();
                    }
                };
                this.updateLayout = frameLayout10;
                frameLayout10.setWillNotDraw(false);
                this.updateLayout.setVisibility(i3);
                this.updateLayout.setTranslationY(AndroidUtilities.dp(48.0f));
                if (Build.VERSION.SDK_INT >= 21) {
                    this.updateLayout.setBackground(Theme.getSelectorDrawable(1090519039, false));
                }
                contentView3.addView(this.updateLayout, LayoutHelper.createFrame(i2, 48, 83));
                this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda32
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        DialogsActivity.this.lambda$createView$17(view3);
                    }
                });
                RadialProgress2 radialProgress2 = new RadialProgress2(this.updateLayout);
                this.updateLayoutIcon = radialProgress2;
                radialProgress2.setColors(i2, i2, i2, i2);
                this.updateLayoutIcon.setCircleRadius(AndroidUtilities.dp(11.0f));
                this.updateLayoutIcon.setAsMini();
                this.updateLayoutIcon.setIcon(15, true, false);
                TextView textView = new TextView(context);
                this.updateTextView = textView;
                textView.setTextSize(1, 15.0f);
                this.updateTextView.setTypeface(AndroidUtilities.bold());
                this.updateTextView.setText(LocaleController.getString("AppUpdateNow", R.string.AppUpdateNow).toUpperCase());
                this.updateTextView.setTextColor(i2);
                this.updateTextView.setPadding(AndroidUtilities.dp(30.0f), 0, 0, 0);
                this.updateLayout.addView(this.updateTextView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
            }
            this.undoViewIndex = contentView3.getChildCount();
            UndoView[] undoViewArr = this.undoView;
            undoViewArr[0] = null;
            undoViewArr[1] = null;
            if (this.folderId != 0) {
                this.viewPages[0].listView.setGlowColor(Theme.getColor(Theme.key_actionBarDefaultArchived));
                this.actionBar.setTitleColor(Theme.getColor(Theme.key_actionBarDefaultArchivedTitle));
                this.actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarDefaultArchivedIcon), false);
                this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultArchivedSelector), false);
                this.actionBar.setSearchTextColor(Theme.getColor(Theme.key_actionBarDefaultArchivedSearch), false);
                this.actionBar.setSearchTextColor(Theme.getColor(Theme.key_actionBarDefaultArchivedSearchPlaceholder), true);
            }
            if (this.onlySelect && this.initialDialogsType == 0) {
                View view3 = new View(context) { // from class: org.telegram.ui.DialogsActivity.28
                    @Override // android.view.View
                    public void setAlpha(float f5) {
                        super.setAlpha(f5);
                        View view4 = DialogsActivity.this.fragmentView;
                        if (view4 != null) {
                            view4.invalidate();
                        }
                    }
                };
                this.blurredView = view3;
                if (Build.VERSION.SDK_INT >= 23) {
                    view3.setForeground(new ColorDrawable(ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundWhite), 100)));
                }
                this.blurredView.setFocusable(false);
                this.blurredView.setImportantForAccessibility(2);
                this.blurredView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda45
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view4) {
                        DialogsActivity.this.lambda$createView$18(view4);
                    }
                });
                this.blurredView.setVisibility(8);
                this.blurredView.setFitsSystemWindows(true);
                f = -1.0f;
                contentView3.addView(this.blurredView, LayoutHelper.createFrame(i2, -1.0f));
            } else {
                f = -1.0f;
            }
            this.actionBarDefaultPaint.setColor(Theme.getColor(this.folderId != 0 ? Theme.key_actionBarDefault : Theme.key_actionBarDefaultArchived));
            if (this.inPreviewMode) {
                TLRPC$User currentUser2 = getUserConfig().getCurrentUser();
                ChatAvatarContainer chatAvatarContainer = new ChatAvatarContainer(this.actionBar.getContext(), null, false);
                this.avatarContainer = chatAvatarContainer;
                chatAvatarContainer.setTitle(UserObject.getUserName(currentUser2));
                this.avatarContainer.setSubtitle(LocaleController.formatUserStatus(this.currentAccount, currentUser2));
                this.avatarContainer.setUserAvatar(currentUser2, true);
                this.avatarContainer.setOccupyStatusBar(false);
                this.avatarContainer.setLeftPadding(AndroidUtilities.dp(10.0f));
                this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, 51, 0.0f, 0.0f, 40.0f, 0.0f));
                this.floatingButton.setVisibility(i3);
                this.actionBar.setOccupyStatusBar(false);
                this.actionBar.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
                FragmentContextView fragmentContextView3 = this.fragmentContextView;
                if (fragmentContextView3 != null) {
                    contentView3.removeView(fragmentContextView3);
                }
                FragmentContextView fragmentContextView4 = this.fragmentLocationContextView;
                if (fragmentContextView4 != null) {
                    contentView3.removeView(fragmentContextView4);
                }
            }
            this.searchIsShowed = false;
            updateFilterTabs(true, false);
            if (this.searchString == null) {
                showSearch(true, false, false);
                this.actionBar.openSearchField(this.searchString, false);
            } else if (this.initialSearchString != null) {
                showSearch(true, false, false, true);
                this.actionBar.openSearchField(this.initialSearchString, false);
                this.initialSearchString = null;
                FilterTabsView filterTabsView2 = this.filterTabsView;
                if (filterTabsView2 != null) {
                    filterTabsView2.setTranslationY(-AndroidUtilities.dp(44.0f));
                }
            } else {
                showSearch(false, false, false);
            }
            if (Build.VERSION.SDK_INT >= 30) {
                FilesMigrationService.checkBottomSheet(this);
            }
            updateMenuButton(false);
            this.actionBar.setDrawBlurBackground(contentView3);
            RightSlidingDialogContainer rightSlidingDialogContainer = new RightSlidingDialogContainer(context) { // from class: org.telegram.ui.DialogsActivity.29
                boolean anotherFragmentOpened;
                float fromScrollYProperty;
                ViewPage transitionPage;

                @Override // org.telegram.ui.RightSlidingDialogContainer
                boolean getOccupyStatusbar() {
                    return ((BaseFragment) DialogsActivity.this).actionBar != null && ((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar();
                }

                @Override // org.telegram.ui.RightSlidingDialogContainer
                public void openAnimationStarted(boolean z6) {
                    if (!DialogsActivity.this.isArchive()) {
                        ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonDrawable(DialogsActivity.this.menuDrawable = new MenuDrawable());
                        DialogsActivity.this.menuDrawable.setRoundCap();
                    }
                    DialogsActivity.this.rightFragmentTransitionInProgress = true;
                    DialogsActivity.this.rightFragmentTransitionIsOpen = z6;
                    contentView3.requestLayout();
                    this.fromScrollYProperty = DialogsActivity.this.scrollYOffset;
                    if (DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                        DialogsActivity.this.filterTabsView.setVisibility(0);
                    }
                    ViewPage viewPage2 = DialogsActivity.this.viewPages[0];
                    this.transitionPage = viewPage2;
                    if (viewPage2.animationSupportListView == null) {
                        this.transitionPage.animationSupportListView = new BlurredRecyclerView(this, context) { // from class: org.telegram.ui.DialogsActivity.29.1
                            @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                            protected void dispatchDraw(Canvas canvas) {
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                                return false;
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                                return false;
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                            public boolean onTouchEvent(MotionEvent motionEvent) {
                                return false;
                            }
                        };
                        final ViewPage viewPage3 = this.transitionPage;
                        this.transitionPage.animationSupportListView.setLayoutManager(new LinearLayoutManager(context) { // from class: org.telegram.ui.DialogsActivity.29.2
                            @Override // androidx.recyclerview.widget.LinearLayoutManager
                            protected int firstPosition() {
                                return (viewPage3.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive() && viewPage3.archivePullViewState == 2) ? 1 : 0;
                            }
                        });
                        this.transitionPage.animationSupportDialogsAdapter = new DialogsAdapter(DialogsActivity.this, context, this.transitionPage.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.onlySelect, DialogsActivity.this.selectedDialogs, ((BaseFragment) DialogsActivity.this).currentAccount, DialogsActivity.this.requestPeerType);
                        this.transitionPage.animationSupportDialogsAdapter.setIsTransitionSupport();
                        this.transitionPage.animationSupportListView.setAdapter(this.transitionPage.animationSupportDialogsAdapter);
                        ViewPage viewPage4 = this.transitionPage;
                        viewPage4.addView(viewPage4.animationSupportListView);
                    }
                    if (!z6) {
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        if (dialogsActivity.hasStories) {
                            dialogsActivity.invalidateScrollY = false;
                            DialogsActivity dialogsActivity2 = DialogsActivity.this;
                            dialogsActivity2.setScrollY(-dialogsActivity2.getMaxScrollYOffset());
                        }
                    }
                    this.transitionPage.listView.stopScroll();
                    this.transitionPage.animationSupportDialogsAdapter.setDialogsType(this.transitionPage.dialogsType);
                    this.transitionPage.dialogsAdapter.setCollapsedView(false, this.transitionPage.listView);
                    this.transitionPage.dialogsAdapter.setDialogsListFrozen(true);
                    this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(true);
                    this.transitionPage.layoutManager.setNeedFixEndGap(false);
                    DialogsActivity.this.setDialogsListFrozen(true);
                    DialogsActivity.this.hideFloatingButton(this.anotherFragmentOpened);
                    this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                    this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                    DialogsActivity dialogsActivity3 = DialogsActivity.this;
                    float f5 = (!dialogsActivity3.hasStories || z6) ? -dialogsActivity3.scrollYOffset : dialogsActivity3.scrollYOffset;
                    ViewPage viewPage5 = this.transitionPage;
                    viewPage5.listView.setAnimationSupportView(viewPage5.animationSupportListView, f5, z6, false);
                    this.transitionPage.listView.setClipChildren(false);
                    ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(false);
                    this.transitionPage.listView.stopScroll();
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                }

                @Override // org.telegram.ui.RightSlidingDialogContainer
                public void openAnimationFinished(boolean z6) {
                    if (!DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                        DialogsActivity.this.filterTabsView.setVisibility(8);
                    }
                    this.transitionPage.layoutManager.setNeedFixGap(true);
                    this.transitionPage.dialogsAdapter.setCollapsedView(hasFragment(), this.transitionPage.listView);
                    this.transitionPage.dialogsAdapter.setDialogsListFrozen(false);
                    this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(false);
                    DialogsActivity.this.setDialogsListFrozen(false);
                    this.transitionPage.listView.setClipChildren(true);
                    this.transitionPage.listView.invalidate();
                    this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                    this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                    this.transitionPage.listView.setAnimationSupportView(null, 0.0f, hasFragment(), z6);
                    DialogsActivity.this.rightFragmentTransitionInProgress = false;
                    ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(!hasFragment());
                    contentView3.requestLayout();
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    if (!dialogsActivity.hasStories) {
                        dialogsActivity.setScrollY(0.0f);
                    }
                    if (!hasFragment()) {
                        DialogsActivity.this.invalidateScrollY = true;
                        DialogsActivity.this.fixScrollYAfterArchiveOpened = true;
                        DialogsActivity.this.fragmentView.invalidate();
                    }
                    if (DialogsActivity.this.searchViewPager != null) {
                        DialogsActivity.this.searchViewPager.updateTabs();
                    }
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                    DialogsActivity.this.updateFilterTabs(false, true);
                }

                @Override // org.telegram.ui.RightSlidingDialogContainer
                void setOpenProgress(float f5) {
                    boolean z6 = f5 > 0.0f;
                    if (this.anotherFragmentOpened != z6) {
                        this.anotherFragmentOpened = z6;
                    }
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    dialogsActivity.filterTabsMoveFrom = dialogsActivity.getActionBarMoveFrom(dialogsActivity.canShowFilterTabsView);
                    DialogsActivity dialogsActivity2 = DialogsActivity.this;
                    dialogsActivity2.filterTabsProgress = (dialogsActivity2.canShowFilterTabsView || DialogsActivity.this.hasStories) ? 1.0f - f5 : 0.0f;
                    View view4 = DialogsActivity.this.fragmentView;
                    if (view4 != null) {
                        view4.invalidate();
                    }
                    DialogsActivity dialogsActivity3 = DialogsActivity.this;
                    if (!dialogsActivity3.hasStories) {
                        dialogsActivity3.setScrollY(AndroidUtilities.lerp(this.fromScrollYProperty, 0.0f, f5));
                    }
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                    if (DialogsActivity.this.menuDrawable != null && hasFragment()) {
                        DialogsActivity.this.menuDrawable.setRotation(f5, false);
                    }
                    if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView() != null) {
                        ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setAlpha(1.0f - f5);
                        if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().getAlpha() > 0.0f) {
                            ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setVisibility(0);
                        }
                    }
                    if (DialogsActivity.this.proxyItem != null) {
                        DialogsActivity.this.proxyItem.setAlpha(1.0f - f5);
                    }
                    if (DialogsActivity.this.downloadsItem != null) {
                        DialogsActivity.this.downloadsItem.setAlpha(1.0f - f5);
                    }
                    if (DialogsActivity.this.passcodeItem != null) {
                        DialogsActivity.this.passcodeItem.setAlpha(1.0f - f5);
                    }
                    if (DialogsActivity.this.searchItem != null) {
                        DialogsActivity.this.searchItem.setAlpha(this.anotherFragmentOpened ? 0.0f : 1.0f);
                    }
                    if (((BaseFragment) DialogsActivity.this).actionBar.getBackButton() != null) {
                        ((BaseFragment) DialogsActivity.this).actionBar.getBackButton().setAlpha(f5 != 1.0f ? 1.0f : 0.0f);
                    }
                    if (DialogsActivity.this.folderId != 0) {
                        DialogsActivity.this.actionBarDefaultPaint.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_actionBarDefaultArchived), Theme.getColor(Theme.key_actionBarDefault), f5));
                    }
                    ViewPage viewPage2 = this.transitionPage;
                    if (viewPage2 != null) {
                        viewPage2.listView.setOpenRightFragmentProgress(f5);
                    }
                }
            };
            this.rightSlidingDialogContainer = rightSlidingDialogContainer;
            rightSlidingDialogContainer.setOpenProgress(0.0f);
            contentView3.addView(this.rightSlidingDialogContainer, LayoutHelper.createFrame(i2, f));
            contentView3.addView(this.dialogStoriesCell, LayoutHelper.createFrame(i2, 81.0f));
            updateStoriesVisibility(false);
            return this.fragmentView;
        }
        i2 = -1;
        i3 = 4;
        filterTabsView = this.filterTabsView;
        if (filterTabsView != null) {
        }
        26 r62 = new 26(context, this, this.currentAccount, isArchive() ? 1 : 0);
        this.dialogStoriesCell = r62;
        r62.setActionBar(this.actionBar);
        DialogStoriesCell dialogStoriesCell2 = this.dialogStoriesCell;
        dialogStoriesCell2.allowGlobalUpdates = false;
        dialogStoriesCell2.setVisibility(8);
        this.animateToHasStories = false;
        this.hasOnlySlefStories = false;
        this.hasStories = false;
        if (this.onlySelect) {
        }
        if (this.onlySelect) {
        }
        createFrame = LayoutHelper.createFrame(i2, -2.0f);
        if (this.inPreviewMode) {
            createFrame.topMargin = AndroidUtilities.statusBarHeight;
        }
        contentView3.addView(this.actionBar, createFrame);
        if (!this.onlySelect) {
        }
        if (this.searchString == null) {
            FrameLayout frameLayout102 = new FrameLayout(context) { // from class: org.telegram.ui.DialogsActivity.27
                private int lastGradientWidth;
                private LinearGradient updateGradient;
                private Paint paint = new Paint();
                private Matrix matrix = new Matrix();

                @Override // android.view.View
                public void draw(Canvas canvas) {
                    if (this.updateGradient != null) {
                        this.paint.setColor(-1);
                        this.paint.setShader(this.updateGradient);
                        this.updateGradient.setLocalMatrix(this.matrix);
                        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.paint);
                        DialogsActivity.this.updateLayoutIcon.setBackgroundGradientDrawable(this.updateGradient);
                        DialogsActivity.this.updateLayoutIcon.draw(canvas);
                    }
                    super.draw(canvas);
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i27, int i28) {
                    super.onMeasure(i27, i28);
                    int size = View.MeasureSpec.getSize(i27);
                    if (this.lastGradientWidth != size) {
                        this.updateGradient = new LinearGradient(0.0f, 0.0f, size, 0.0f, new int[]{-9846926, -11291731}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                        this.lastGradientWidth = size;
                    }
                    int measuredWidth = (getMeasuredWidth() - DialogsActivity.this.updateTextView.getMeasuredWidth()) / 2;
                    DialogsActivity.this.updateLayoutIcon.setProgressRect(measuredWidth, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(22.0f) + measuredWidth, AndroidUtilities.dp(35.0f));
                }

                @Override // android.view.View
                public void setTranslationY(float f5) {
                    super.setTranslationY(f5);
                    DialogsActivity.this.additionalFloatingTranslation2 = AndroidUtilities.dp(48.0f) - f5;
                    if (DialogsActivity.this.additionalFloatingTranslation2 < 0.0f) {
                        DialogsActivity.this.additionalFloatingTranslation2 = 0.0f;
                    }
                    if (DialogsActivity.this.floatingHidden) {
                        return;
                    }
                    DialogsActivity.this.updateFloatingButtonOffset();
                }
            };
            this.updateLayout = frameLayout102;
            frameLayout102.setWillNotDraw(false);
            this.updateLayout.setVisibility(i3);
            this.updateLayout.setTranslationY(AndroidUtilities.dp(48.0f));
            if (Build.VERSION.SDK_INT >= 21) {
            }
            contentView3.addView(this.updateLayout, LayoutHelper.createFrame(i2, 48, 83));
            this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda32
                @Override // android.view.View.OnClickListener
                public final void onClick(View view32) {
                    DialogsActivity.this.lambda$createView$17(view32);
                }
            });
            RadialProgress2 radialProgress22 = new RadialProgress2(this.updateLayout);
            this.updateLayoutIcon = radialProgress22;
            radialProgress22.setColors(i2, i2, i2, i2);
            this.updateLayoutIcon.setCircleRadius(AndroidUtilities.dp(11.0f));
            this.updateLayoutIcon.setAsMini();
            this.updateLayoutIcon.setIcon(15, true, false);
            TextView textView2 = new TextView(context);
            this.updateTextView = textView2;
            textView2.setTextSize(1, 15.0f);
            this.updateTextView.setTypeface(AndroidUtilities.bold());
            this.updateTextView.setText(LocaleController.getString("AppUpdateNow", R.string.AppUpdateNow).toUpperCase());
            this.updateTextView.setTextColor(i2);
            this.updateTextView.setPadding(AndroidUtilities.dp(30.0f), 0, 0, 0);
            this.updateLayout.addView(this.updateTextView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        this.undoViewIndex = contentView3.getChildCount();
        UndoView[] undoViewArr2 = this.undoView;
        undoViewArr2[0] = null;
        undoViewArr2[1] = null;
        if (this.folderId != 0) {
        }
        if (this.onlySelect) {
        }
        f = -1.0f;
        this.actionBarDefaultPaint.setColor(Theme.getColor(this.folderId != 0 ? Theme.key_actionBarDefault : Theme.key_actionBarDefaultArchived));
        if (this.inPreviewMode) {
        }
        this.searchIsShowed = false;
        updateFilterTabs(true, false);
        if (this.searchString == null) {
        }
        if (Build.VERSION.SDK_INT >= 30) {
        }
        updateMenuButton(false);
        this.actionBar.setDrawBlurBackground(contentView3);
        RightSlidingDialogContainer rightSlidingDialogContainer2 = new RightSlidingDialogContainer(context) { // from class: org.telegram.ui.DialogsActivity.29
            boolean anotherFragmentOpened;
            float fromScrollYProperty;
            ViewPage transitionPage;

            @Override // org.telegram.ui.RightSlidingDialogContainer
            boolean getOccupyStatusbar() {
                return ((BaseFragment) DialogsActivity.this).actionBar != null && ((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar();
            }

            @Override // org.telegram.ui.RightSlidingDialogContainer
            public void openAnimationStarted(boolean z6) {
                if (!DialogsActivity.this.isArchive()) {
                    ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonDrawable(DialogsActivity.this.menuDrawable = new MenuDrawable());
                    DialogsActivity.this.menuDrawable.setRoundCap();
                }
                DialogsActivity.this.rightFragmentTransitionInProgress = true;
                DialogsActivity.this.rightFragmentTransitionIsOpen = z6;
                contentView3.requestLayout();
                this.fromScrollYProperty = DialogsActivity.this.scrollYOffset;
                if (DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                    DialogsActivity.this.filterTabsView.setVisibility(0);
                }
                ViewPage viewPage2 = DialogsActivity.this.viewPages[0];
                this.transitionPage = viewPage2;
                if (viewPage2.animationSupportListView == null) {
                    this.transitionPage.animationSupportListView = new BlurredRecyclerView(this, context) { // from class: org.telegram.ui.DialogsActivity.29.1
                        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                        protected void dispatchDraw(Canvas canvas) {
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                            return false;
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                            return false;
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            return false;
                        }
                    };
                    final ViewPage viewPage3 = this.transitionPage;
                    this.transitionPage.animationSupportListView.setLayoutManager(new LinearLayoutManager(context) { // from class: org.telegram.ui.DialogsActivity.29.2
                        @Override // androidx.recyclerview.widget.LinearLayoutManager
                        protected int firstPosition() {
                            return (viewPage3.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive() && viewPage3.archivePullViewState == 2) ? 1 : 0;
                        }
                    });
                    this.transitionPage.animationSupportDialogsAdapter = new DialogsAdapter(DialogsActivity.this, context, this.transitionPage.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.onlySelect, DialogsActivity.this.selectedDialogs, ((BaseFragment) DialogsActivity.this).currentAccount, DialogsActivity.this.requestPeerType);
                    this.transitionPage.animationSupportDialogsAdapter.setIsTransitionSupport();
                    this.transitionPage.animationSupportListView.setAdapter(this.transitionPage.animationSupportDialogsAdapter);
                    ViewPage viewPage4 = this.transitionPage;
                    viewPage4.addView(viewPage4.animationSupportListView);
                }
                if (!z6) {
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories) {
                        dialogsActivity.invalidateScrollY = false;
                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                        dialogsActivity2.setScrollY(-dialogsActivity2.getMaxScrollYOffset());
                    }
                }
                this.transitionPage.listView.stopScroll();
                this.transitionPage.animationSupportDialogsAdapter.setDialogsType(this.transitionPage.dialogsType);
                this.transitionPage.dialogsAdapter.setCollapsedView(false, this.transitionPage.listView);
                this.transitionPage.dialogsAdapter.setDialogsListFrozen(true);
                this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(true);
                this.transitionPage.layoutManager.setNeedFixEndGap(false);
                DialogsActivity.this.setDialogsListFrozen(true);
                DialogsActivity.this.hideFloatingButton(this.anotherFragmentOpened);
                this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                DialogsActivity dialogsActivity3 = DialogsActivity.this;
                float f5 = (!dialogsActivity3.hasStories || z6) ? -dialogsActivity3.scrollYOffset : dialogsActivity3.scrollYOffset;
                ViewPage viewPage5 = this.transitionPage;
                viewPage5.listView.setAnimationSupportView(viewPage5.animationSupportListView, f5, z6, false);
                this.transitionPage.listView.setClipChildren(false);
                ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(false);
                this.transitionPage.listView.stopScroll();
                DialogsActivity.this.updateDrawerSwipeEnabled();
            }

            @Override // org.telegram.ui.RightSlidingDialogContainer
            public void openAnimationFinished(boolean z6) {
                if (!DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                    DialogsActivity.this.filterTabsView.setVisibility(8);
                }
                this.transitionPage.layoutManager.setNeedFixGap(true);
                this.transitionPage.dialogsAdapter.setCollapsedView(hasFragment(), this.transitionPage.listView);
                this.transitionPage.dialogsAdapter.setDialogsListFrozen(false);
                this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(false);
                DialogsActivity.this.setDialogsListFrozen(false);
                this.transitionPage.listView.setClipChildren(true);
                this.transitionPage.listView.invalidate();
                this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                this.transitionPage.listView.setAnimationSupportView(null, 0.0f, hasFragment(), z6);
                DialogsActivity.this.rightFragmentTransitionInProgress = false;
                ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(!hasFragment());
                contentView3.requestLayout();
                DialogsActivity dialogsActivity = DialogsActivity.this;
                if (!dialogsActivity.hasStories) {
                    dialogsActivity.setScrollY(0.0f);
                }
                if (!hasFragment()) {
                    DialogsActivity.this.invalidateScrollY = true;
                    DialogsActivity.this.fixScrollYAfterArchiveOpened = true;
                    DialogsActivity.this.fragmentView.invalidate();
                }
                if (DialogsActivity.this.searchViewPager != null) {
                    DialogsActivity.this.searchViewPager.updateTabs();
                }
                DialogsActivity.this.updateDrawerSwipeEnabled();
                DialogsActivity.this.updateFilterTabs(false, true);
            }

            @Override // org.telegram.ui.RightSlidingDialogContainer
            void setOpenProgress(float f5) {
                boolean z6 = f5 > 0.0f;
                if (this.anotherFragmentOpened != z6) {
                    this.anotherFragmentOpened = z6;
                }
                DialogsActivity dialogsActivity = DialogsActivity.this;
                dialogsActivity.filterTabsMoveFrom = dialogsActivity.getActionBarMoveFrom(dialogsActivity.canShowFilterTabsView);
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                dialogsActivity2.filterTabsProgress = (dialogsActivity2.canShowFilterTabsView || DialogsActivity.this.hasStories) ? 1.0f - f5 : 0.0f;
                View view4 = DialogsActivity.this.fragmentView;
                if (view4 != null) {
                    view4.invalidate();
                }
                DialogsActivity dialogsActivity3 = DialogsActivity.this;
                if (!dialogsActivity3.hasStories) {
                    dialogsActivity3.setScrollY(AndroidUtilities.lerp(this.fromScrollYProperty, 0.0f, f5));
                }
                DialogsActivity.this.updateDrawerSwipeEnabled();
                if (DialogsActivity.this.menuDrawable != null && hasFragment()) {
                    DialogsActivity.this.menuDrawable.setRotation(f5, false);
                }
                if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView() != null) {
                    ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setAlpha(1.0f - f5);
                    if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().getAlpha() > 0.0f) {
                        ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setVisibility(0);
                    }
                }
                if (DialogsActivity.this.proxyItem != null) {
                    DialogsActivity.this.proxyItem.setAlpha(1.0f - f5);
                }
                if (DialogsActivity.this.downloadsItem != null) {
                    DialogsActivity.this.downloadsItem.setAlpha(1.0f - f5);
                }
                if (DialogsActivity.this.passcodeItem != null) {
                    DialogsActivity.this.passcodeItem.setAlpha(1.0f - f5);
                }
                if (DialogsActivity.this.searchItem != null) {
                    DialogsActivity.this.searchItem.setAlpha(this.anotherFragmentOpened ? 0.0f : 1.0f);
                }
                if (((BaseFragment) DialogsActivity.this).actionBar.getBackButton() != null) {
                    ((BaseFragment) DialogsActivity.this).actionBar.getBackButton().setAlpha(f5 != 1.0f ? 1.0f : 0.0f);
                }
                if (DialogsActivity.this.folderId != 0) {
                    DialogsActivity.this.actionBarDefaultPaint.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_actionBarDefaultArchived), Theme.getColor(Theme.key_actionBarDefault), f5));
                }
                ViewPage viewPage2 = this.transitionPage;
                if (viewPage2 != null) {
                    viewPage2.listView.setOpenRightFragmentProgress(f5);
                }
            }
        };
        this.rightSlidingDialogContainer = rightSlidingDialogContainer2;
        rightSlidingDialogContainer2.setOpenProgress(0.0f);
        contentView3.addView(this.rightSlidingDialogContainer, LayoutHelper.createFrame(i2, f));
        contentView3.addView(this.dialogStoriesCell, LayoutHelper.createFrame(i2, 81.0f));
        updateStoriesVisibility(false);
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        this.filterTabsView.setIsEditing(false);
        showDoneItem(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 5 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        boolean isSpeedItemCreated = false;
        final /* synthetic */ Context val$context;
        final /* synthetic */ ActionBarMenu val$menu;

        5(Context context, ActionBarMenu actionBarMenu) {
            this.val$context = context;
            this.val$menu = actionBarMenu;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onPreToggleSearch() {
            if (this.isSpeedItemCreated) {
                return;
            }
            boolean z = true;
            this.isSpeedItemCreated = true;
            DialogsActivity dialogsActivity = DialogsActivity.this;
            Context context = this.val$context;
            ActionBarMenu actionBarMenu = this.val$menu;
            int color = Theme.getColor(Theme.key_actionBarActionModeDefaultSelector);
            int i = Theme.key_actionBarActionModeDefaultIcon;
            dialogsActivity.speedItem = new ActionBarMenuItem(context, actionBarMenu, color, Theme.getColor(i));
            DialogsActivity.this.speedItem.setIcon(R.drawable.avd_speed);
            DialogsActivity.this.speedItem.getIconView().setColorFilter(new PorterDuffColorFilter(Theme.getColor(i), PorterDuff.Mode.SRC_IN));
            DialogsActivity.this.speedItem.setTranslationX(AndroidUtilities.dp(32.0f));
            DialogsActivity.this.speedItem.setAlpha(0.0f);
            DialogsActivity.this.speedItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$5$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.5.this.lambda$onPreToggleSearch$0(view);
                }
            });
            DialogsActivity.this.speedItem.setClickable(false);
            DialogsActivity.this.speedItem.setFixBackground(true);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtilities.dp(42.0f), -1);
            int dp = AndroidUtilities.dp(38.0f);
            layoutParams.rightMargin = dp;
            layoutParams.leftMargin = dp;
            layoutParams.gravity = 5;
            ((FrameLayout) DialogsActivity.this.searchItem.getSearchClearButton().getParent()).addView(DialogsActivity.this.speedItem, layoutParams);
            DialogsActivity.this.searchItem.setSearchAdditionalButton(DialogsActivity.this.speedItem);
            DialogsActivity dialogsActivity2 = DialogsActivity.this;
            dialogsActivity2.updateSpeedItem((dialogsActivity2.searchViewPager == null || DialogsActivity.this.searchViewPager.getCurrentPosition() != 2) ? false : false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPreToggleSearch$0(View view) {
            DialogsActivity.this.showDialog(new PremiumFeatureBottomSheet(DialogsActivity.this, 2, true));
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            DialogsActivity.this.searching = true;
            if (DialogsActivity.this.switchItem != null) {
                DialogsActivity.this.switchItem.setVisibility(8);
            }
            if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisible) {
                DialogsActivity.this.proxyItem.setVisibility(8);
            }
            if (DialogsActivity.this.downloadsItem != null && DialogsActivity.this.downloadsItemVisible) {
                DialogsActivity.this.downloadsItem.setVisibility(8);
            }
            if (DialogsActivity.this.viewPages[0] != null) {
                if (DialogsActivity.this.searchString != null) {
                    DialogsActivity.this.viewPages[0].listView.hide();
                    if (DialogsActivity.this.searchViewPager != null) {
                        DialogsActivity.this.searchViewPager.searchListView.show();
                    }
                }
                if (!DialogsActivity.this.onlySelect) {
                    DialogsActivity.this.floatingButtonContainer.setVisibility(8);
                    if (DialogsActivity.this.floatingButton2Container != null) {
                        DialogsActivity.this.floatingButton2Container.setVisibility(8);
                    }
                    if (DialogsActivity.this.storyHint != null) {
                        DialogsActivity.this.storyHint.hide();
                    }
                }
            }
            DialogStoriesCell dialogStoriesCell = DialogsActivity.this.dialogStoriesCell;
            if (dialogStoriesCell != null && dialogStoriesCell.getPremiumHint() != null) {
                DialogsActivity.this.dialogStoriesCell.getPremiumHint().hide();
            }
            DialogsActivity dialogsActivity = DialogsActivity.this;
            if (!dialogsActivity.hasStories) {
                dialogsActivity.setScrollY(0.0f);
            }
            DialogsActivity.this.updatePasscodeButton();
            DialogsActivity.this.updateProxyButton(false, false);
            ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", R.string.AccDescrGoBack));
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, new Object[0]);
            ((SizeNotifierFrameLayout) DialogsActivity.this.fragmentView).invalidateBlur();
            if (DialogsActivity.this.optionsItem != null) {
                DialogsActivity.this.optionsItem.setVisibility(8);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public boolean canCollapseSearch() {
            if (DialogsActivity.this.switchItem != null) {
                DialogsActivity.this.switchItem.setVisibility(0);
            }
            if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisible) {
                DialogsActivity.this.proxyItem.setVisibility(0);
            }
            if (DialogsActivity.this.downloadsItem != null && DialogsActivity.this.downloadsItemVisible) {
                DialogsActivity.this.downloadsItem.setVisibility(0);
            }
            if (DialogsActivity.this.searchString != null) {
                DialogsActivity.this.finishFragment();
                return false;
            }
            return true;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            DialogsActivity.this.searching = false;
            DialogsActivity.this.searchWas = false;
            if (DialogsActivity.this.viewPages[0] != null) {
                DialogsActivity.this.viewPages[0].listView.setEmptyView(DialogsActivity.this.folderId == 0 ? DialogsActivity.this.viewPages[0].progressView : null);
                if (!DialogsActivity.this.onlySelect) {
                    DialogsActivity.this.floatingButtonContainer.setVisibility(0);
                    if (DialogsActivity.this.floatingButton2Container != null) {
                        DialogsActivity.this.floatingButton2Container.setVisibility(DialogsActivity.this.storiesEnabled ? 0 : 8);
                    }
                    DialogsActivity.this.floatingHidden = true;
                    DialogsActivity.this.floatingButtonTranslation = AndroidUtilities.dp(100.0f);
                    DialogsActivity.this.floatingButtonHideProgress = 1.0f;
                    DialogsActivity.this.updateFloatingButtonOffset();
                }
                DialogsActivity.this.showSearch(false, false, true);
            }
            DialogsActivity.this.updateProxyButton(false, false);
            DialogsActivity.this.updatePasscodeButton();
            if (DialogsActivity.this.menuDrawable != null) {
                if (((BaseFragment) DialogsActivity.this).actionBar.getBackButton().getDrawable() != DialogsActivity.this.menuDrawable) {
                    ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonDrawable(DialogsActivity.this.menuDrawable);
                    DialogsActivity.this.menuDrawable.setRotation(0.0f, true);
                }
                ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", R.string.AccDescrOpenMenu));
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
            ((SizeNotifierFrameLayout) DialogsActivity.this.fragmentView).invalidateBlur();
            if (DialogsActivity.this.optionsItem != null) {
                DialogsActivity.this.optionsItem.setVisibility(0);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            String obj = editText.getText().toString();
            if (obj.length() != 0 || ((DialogsActivity.this.searchViewPager != null && DialogsActivity.this.searchViewPager.dialogsSearchAdapter != null && DialogsActivity.this.searchViewPager.dialogsSearchAdapter.hasRecentSearch()) || DialogsActivity.this.searchFiltersWasShowed || DialogsActivity.this.hasStories)) {
                DialogsActivity.this.searchWas = true;
                if (!DialogsActivity.this.searchIsShowed) {
                    DialogsActivity.this.showSearch(true, false, true);
                }
            }
            if (DialogsActivity.this.searchViewPager != null) {
                DialogsActivity.this.searchViewPager.onTextChanged(obj);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchFilterCleared(FiltersView.MediaFilterData mediaFilterData) {
            if (DialogsActivity.this.searchIsShowed) {
                if (DialogsActivity.this.searchViewPager != null) {
                    DialogsActivity.this.searchViewPager.removeSearchFilter(mediaFilterData);
                    DialogsActivity.this.searchViewPager.onTextChanged(DialogsActivity.this.searchItem.getSearchField().getText().toString());
                }
                DialogsActivity.this.updateFiltersView(true, null, null, false, true);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public boolean canToggleSearch() {
            return !((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed() && DialogsActivity.this.databaseMigrationHint == null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(View view) {
        getContactsController().loadGlobalPrivacySetting();
        this.optionsItem.toggleSubMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7() {
        if (this.initialDialogsType != 10) {
            hideFloatingButton(false);
        }
        if (this.hasOnlySlefStories && getStoriesController().hasOnlySelfStories()) {
            this.dialogStoriesCell.openSelfStories();
        } else {
            scrollToTop(true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 6 extends FilterTabsView {
        6(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            getParent().requestDisallowInterceptTouchEvent(true);
            DialogsActivity.this.maybeStartTracking = false;
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            if (getTranslationY() != f) {
                super.setTranslationY(f);
                DialogsActivity.this.updateContextViewPosition();
                View view = DialogsActivity.this.fragmentView;
                if (view != null) {
                    view.invalidate();
                }
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView
        protected void onDefaultTabMoved() {
            if (DialogsActivity.this.getMessagesController().premiumFeaturesBlocked()) {
                return;
            }
            try {
                performHapticFeedback(3, 1);
            } catch (Exception unused) {
            }
            DialogsActivity dialogsActivity = DialogsActivity.this;
            dialogsActivity.topBulletin = BulletinFactory.of(dialogsActivity).createSimpleBulletin(R.raw.filter_reorder, AndroidUtilities.replaceTags(LocaleController.formatString("LimitReachedReorderFolder", R.string.LimitReachedReorderFolder, LocaleController.getString(R.string.FilterAllChats))), LocaleController.getString("PremiumMore", R.string.PremiumMore), 5000, new Runnable() { // from class: org.telegram.ui.DialogsActivity$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.6.this.lambda$onDefaultTabMoved$0();
                }
            }).show(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDefaultTabMoved$0() {
            DialogsActivity.this.showDialog(new PremiumFeatureBottomSheet(DialogsActivity.this, 9, true));
            DialogsActivity.this.filterTabsView.setIsEditing(false);
            DialogsActivity.this.showDoneItem(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 7 implements FilterTabsView.FilterTabsViewDelegate {
        final /* synthetic */ Context val$context;

        7(Context context) {
            this.val$context = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: showDeleteAlert */
        public void lambda$didSelectTab$6(final MessagesController.DialogFilter dialogFilter) {
            if (dialogFilter.isChatlist()) {
                FolderBottomSheet.showForDeletion(DialogsActivity.this, dialogFilter.id, null);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("FilterDelete", R.string.FilterDelete));
            builder.setMessage(LocaleController.getString("FilterDeleteAlert", R.string.FilterDeleteAlert));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DialogsActivity.7.this.lambda$showDeleteAlert$0(dialogFilter, dialogInterface, i);
                }
            });
            AlertDialog create = builder.create();
            DialogsActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showDeleteAlert$0(MessagesController.DialogFilter dialogFilter, DialogInterface dialogInterface, int i) {
            TLRPC$TL_messages_updateDialogFilter tLRPC$TL_messages_updateDialogFilter = new TLRPC$TL_messages_updateDialogFilter();
            tLRPC$TL_messages_updateDialogFilter.id = dialogFilter.id;
            DialogsActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_messages_updateDialogFilter, null);
            DialogsActivity.this.getMessagesController().removeFilter(dialogFilter);
            DialogsActivity.this.getMessagesStorage().deleteDialogFilter(dialogFilter);
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onSamePageSelected() {
            DialogsActivity.this.scrollToTop(true, false);
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onPageReorder(int i, int i2) {
            for (int i3 = 0; i3 < DialogsActivity.this.viewPages.length; i3++) {
                if (DialogsActivity.this.viewPages[i3].selectedType == i) {
                    DialogsActivity.this.viewPages[i3].selectedType = i2;
                } else if (DialogsActivity.this.viewPages[i3].selectedType == i2) {
                    DialogsActivity.this.viewPages[i3].selectedType = i;
                }
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onPageSelected(FilterTabsView.Tab tab, boolean z) {
            int i;
            if (DialogsActivity.this.viewPages[0].selectedType == tab.id) {
                return;
            }
            if (tab.isLocked) {
                DialogsActivity.this.filterTabsView.shakeLock(tab.id);
                DialogsActivity dialogsActivity = DialogsActivity.this;
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                dialogsActivity.showDialog(new LimitReachedBottomSheet(dialogsActivity2, this.val$context, 3, ((BaseFragment) dialogsActivity2).currentAccount, null));
                return;
            }
            ArrayList<MessagesController.DialogFilter> dialogFilters = DialogsActivity.this.getMessagesController().getDialogFilters();
            if (tab.isDefault || ((i = tab.id) >= 0 && i < dialogFilters.size())) {
                DialogsActivity dialogsActivity3 = DialogsActivity.this;
                dialogsActivity3.isFirstTab = tab.id == dialogsActivity3.filterTabsView.getFirstTabId();
                DialogsActivity.this.updateDrawerSwipeEnabled();
                DialogsActivity.this.viewPages[1].selectedType = tab.id;
                DialogsActivity.this.viewPages[1].setVisibility(0);
                DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth());
                DialogsActivity.this.showScrollbars(false);
                DialogsActivity.this.switchToCurrentSelectedMode(true);
                DialogsActivity.this.animatingForward = z;
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public boolean canPerformActions() {
            return !DialogsActivity.this.searching;
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onPageScrolled(float f) {
            if (f != 1.0f || DialogsActivity.this.viewPages[1].getVisibility() == 0 || DialogsActivity.this.searching) {
                if (DialogsActivity.this.animatingForward) {
                    DialogsActivity.this.viewPages[0].setTranslationX((-f) * DialogsActivity.this.viewPages[0].getMeasuredWidth());
                    DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() - (DialogsActivity.this.viewPages[0].getMeasuredWidth() * f));
                } else {
                    DialogsActivity.this.viewPages[0].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() * f);
                    DialogsActivity.this.viewPages[1].setTranslationX((DialogsActivity.this.viewPages[0].getMeasuredWidth() * f) - DialogsActivity.this.viewPages[0].getMeasuredWidth());
                }
                if (f == 1.0f) {
                    ViewPage viewPage = DialogsActivity.this.viewPages[0];
                    DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                    DialogsActivity.this.viewPages[1] = viewPage;
                    DialogsActivity.this.viewPages[1].setVisibility(8);
                    DialogsActivity.this.showScrollbars(true);
                    DialogsActivity.this.updateCounters(false);
                    DialogsActivity.this.filterTabsView.stopAnimatingIndicator();
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    dialogsActivity.checkListLoad(dialogsActivity.viewPages[0]);
                    DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                    DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                }
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public int getTabCounter(int i) {
            if (DialogsActivity.this.initialDialogsType == 3) {
                return 0;
            }
            if (i == DialogsActivity.this.filterTabsView.getDefaultTabId()) {
                return DialogsActivity.this.getMessagesStorage().getMainUnreadCount();
            }
            ArrayList<MessagesController.DialogFilter> dialogFilters = DialogsActivity.this.getMessagesController().getDialogFilters();
            if (i < 0 || i >= dialogFilters.size()) {
                return 0;
            }
            return DialogsActivity.this.getMessagesController().getDialogFilters().get(i).unreadCount;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:101:0x01f2  */
        /* JADX WARN: Removed duplicated region for block: B:102:0x01f5  */
        /* JADX WARN: Removed duplicated region for block: B:104:0x01f9  */
        /* JADX WARN: Removed duplicated region for block: B:105:0x01fe  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0080  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0082  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0091  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x009c  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00a6  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x0166  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x0170  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x01b5  */
        /* JADX WARN: Removed duplicated region for block: B:89:0x01b7  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x01cf  */
        /* JADX WARN: Removed duplicated region for block: B:93:0x01d4  */
        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean didSelectTab(FilterTabsView.TabView tabView, boolean z) {
            final MessagesController.DialogFilter dialogFilter;
            final boolean z2;
            final ArrayList arrayList;
            final boolean z3;
            boolean z4;
            int i;
            int i2;
            String str;
            int i3;
            String str2;
            MessagesController.DialogFilter dialogFilter2;
            boolean z5;
            TLRPC$Chat chat;
            if (DialogsActivity.this.initialDialogsType == 0 && !((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed() && DialogsActivity.this.storiesOverscroll == 0.0f) {
                final MessagesController.DialogFilter dialogFilter3 = null;
                if (DialogsActivity.this.filterOptions == null || !DialogsActivity.this.filterOptions.isShown()) {
                    if (tabView.getId() != DialogsActivity.this.filterTabsView.getDefaultTabId()) {
                        ArrayList<MessagesController.DialogFilter> dialogFilters = DialogsActivity.this.getMessagesController().getDialogFilters();
                        int id = tabView.getId();
                        if (dialogFilters != null && id >= 0 && id < dialogFilters.size()) {
                            dialogFilter = dialogFilters.get(tabView.getId());
                            z2 = dialogFilter != null;
                            final boolean[] zArr = {true};
                            MessagesController messagesController = DialogsActivity.this.getMessagesController();
                            arrayList = new ArrayList(!z2 ? messagesController.getDialogs(DialogsActivity.this.folderId) : messagesController.getAllDialogs());
                            if (dialogFilter == null) {
                                dialogFilter3 = DialogsActivity.this.getMessagesController().getDialogFilters().get(tabView.getId());
                                int i4 = 0;
                                if (dialogFilter3 != null) {
                                    while (i4 < arrayList.size()) {
                                        if (!dialogFilter3.includesDialog(DialogsActivity.this.getAccountInstance(), ((TLRPC$Dialog) arrayList.get(i4)).id)) {
                                            arrayList.remove(i4);
                                            i4--;
                                        }
                                        i4++;
                                    }
                                    i4 = (dialogFilter3.isChatlist() || (dialogFilter3.neverShow.isEmpty() && (dialogFilter3.flags & ((MessagesController.DIALOG_FILTER_FLAG_CHATLIST | MessagesController.DIALOG_FILTER_FLAG_CHATLIST_ADMIN) ^ (-1))) == 0)) ? 1 : 0;
                                    if (i4 != 0) {
                                        int i5 = 0;
                                        while (true) {
                                            if (i5 >= dialogFilter3.alwaysShow.size()) {
                                                break;
                                            }
                                            long longValue = dialogFilter3.alwaysShow.get(i5).longValue();
                                            if (longValue < 0 && (chat = DialogsActivity.this.getMessagesController().getChat(Long.valueOf(-longValue))) != null && FilterCreateActivity.canAddToFolder(chat)) {
                                                zArr[0] = false;
                                                break;
                                            }
                                            i5++;
                                        }
                                    }
                                }
                                if (arrayList.isEmpty()) {
                                    z3 = false;
                                    z4 = i4;
                                } else {
                                    int i6 = 0;
                                    while (true) {
                                        if (i6 >= arrayList.size()) {
                                            dialogFilter2 = dialogFilter3;
                                            z5 = true;
                                            break;
                                        }
                                        dialogFilter2 = dialogFilter3;
                                        if (!DialogsActivity.this.getMessagesController().isDialogMuted(((TLRPC$Dialog) arrayList.get(i6)).id, 0L)) {
                                            z5 = false;
                                            break;
                                        }
                                        i6++;
                                        dialogFilter3 = dialogFilter2;
                                    }
                                    z3 = !z5;
                                    dialogFilter3 = dialogFilter2;
                                    z4 = i4;
                                }
                            } else {
                                z3 = false;
                                z4 = false;
                            }
                            boolean z6 = false;
                            for (i = 0; i < arrayList.size(); i++) {
                                if (((TLRPC$Dialog) arrayList.get(i)).unread_mark || ((TLRPC$Dialog) arrayList.get(i)).unread_count > 0) {
                                    z6 = true;
                                }
                            }
                            DialogsActivity dialogsActivity = DialogsActivity.this;
                            ItemOptions addIf = ItemOptions.makeOptions(dialogsActivity, tabView).setScrimViewBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, Theme.getColor(Theme.key_actionBarDefault))).addIf(DialogsActivity.this.getMessagesController().getDialogFilters().size() <= 1, R.drawable.tabs_reorder, LocaleController.getString("FilterReorder", R.string.FilterReorder), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.7.this.lambda$didSelectTab$1();
                                }
                            });
                            int i7 = R.drawable.msg_edit;
                            if (z2) {
                                i2 = R.string.FilterEdit;
                                str = "FilterEdit";
                            } else {
                                i2 = R.string.FilterEditAll;
                                str = "FilterEditAll";
                            }
                            ItemOptions add = addIf.add(i7, LocaleController.getString(str, i2), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda5
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.7.this.lambda$didSelectTab$2(z2, dialogFilter);
                                }
                            });
                            boolean z7 = dialogFilter == null && !arrayList.isEmpty();
                            int i8 = !z3 ? R.drawable.msg_mute : R.drawable.msg_unmute;
                            if (z3) {
                                i3 = R.string.FilterUnmuteAll;
                                str2 = "FilterUnmuteAll";
                            } else {
                                i3 = R.string.FilterMuteAll;
                                str2 = "FilterMuteAll";
                            }
                            dialogsActivity.filterOptions = add.addIf(z7, i8, LocaleController.getString(str2, i3), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda3
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.7.this.lambda$didSelectTab$3(arrayList, z3);
                                }
                            }).addIf(z6, R.drawable.msg_markread, LocaleController.getString("MarkAllAsRead", R.string.MarkAllAsRead), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.7.this.lambda$didSelectTab$4(arrayList);
                                }
                            }).addIf(z4, R.drawable.msg_share, FilterCreateActivity.withNew((dialogFilter3 == null && dialogFilter3.isMyChatlist()) ? -1 : 0, LocaleController.getString("LinkActionShare", R.string.LinkActionShare), true), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.7.this.lambda$didSelectTab$5(zArr, dialogFilter3);
                                }
                            }).addIf(!z2, R.drawable.msg_delete, (CharSequence) LocaleController.getString("FilterDeleteItem", R.string.FilterDeleteItem), true, new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda4
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.7.this.lambda$didSelectTab$6(dialogFilter);
                                }
                            }).setGravity(3).translate(AndroidUtilities.dp(-8.0f), AndroidUtilities.dp(-10.0f)).show();
                            return true;
                        }
                    }
                    dialogFilter = null;
                    if (dialogFilter != null) {
                    }
                    final boolean[] zArr2 = {true};
                    MessagesController messagesController2 = DialogsActivity.this.getMessagesController();
                    arrayList = new ArrayList(!z2 ? messagesController2.getDialogs(DialogsActivity.this.folderId) : messagesController2.getAllDialogs());
                    if (dialogFilter == null) {
                    }
                    boolean z62 = false;
                    while (i < arrayList.size()) {
                    }
                    DialogsActivity dialogsActivity2 = DialogsActivity.this;
                    ItemOptions addIf2 = ItemOptions.makeOptions(dialogsActivity2, tabView).setScrimViewBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, Theme.getColor(Theme.key_actionBarDefault))).addIf(DialogsActivity.this.getMessagesController().getDialogFilters().size() <= 1, R.drawable.tabs_reorder, LocaleController.getString("FilterReorder", R.string.FilterReorder), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.7.this.lambda$didSelectTab$1();
                        }
                    });
                    int i72 = R.drawable.msg_edit;
                    if (z2) {
                    }
                    ItemOptions add2 = addIf2.add(i72, LocaleController.getString(str, i2), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.7.this.lambda$didSelectTab$2(z2, dialogFilter);
                        }
                    });
                    if (dialogFilter == null) {
                    }
                    if (!z3) {
                    }
                    if (z3) {
                    }
                    dialogsActivity2.filterOptions = add2.addIf(z7, i8, LocaleController.getString(str2, i3), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.7.this.lambda$didSelectTab$3(arrayList, z3);
                        }
                    }).addIf(z62, R.drawable.msg_markread, LocaleController.getString("MarkAllAsRead", R.string.MarkAllAsRead), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.7.this.lambda$didSelectTab$4(arrayList);
                        }
                    }).addIf(z4, R.drawable.msg_share, FilterCreateActivity.withNew((dialogFilter3 == null && dialogFilter3.isMyChatlist()) ? -1 : 0, LocaleController.getString("LinkActionShare", R.string.LinkActionShare), true), new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.7.this.lambda$didSelectTab$5(zArr2, dialogFilter3);
                        }
                    }).addIf(!z2, R.drawable.msg_delete, (CharSequence) LocaleController.getString("FilterDeleteItem", R.string.FilterDeleteItem), true, new Runnable() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.7.this.lambda$didSelectTab$6(dialogFilter);
                        }
                    }).setGravity(3).translate(AndroidUtilities.dp(-8.0f), AndroidUtilities.dp(-10.0f)).show();
                    return true;
                }
                DialogsActivity.this.filterOptions.dismiss();
                DialogsActivity.this.filterOptions = null;
                return false;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectTab$1() {
            DialogsActivity.this.resetScroll();
            DialogsActivity.this.filterTabsView.setIsEditing(true);
            DialogsActivity.this.showDoneItem(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectTab$2(boolean z, MessagesController.DialogFilter dialogFilter) {
            DialogsActivity.this.presentFragment(z ? new FiltersSetupActivity() : new FilterCreateActivity(dialogFilter));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectTab$3(ArrayList arrayList, boolean z) {
            int i = 0;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) arrayList.get(i2);
                if (tLRPC$Dialog != null) {
                    DialogsActivity.this.getNotificationsController().setDialogNotificationsSettings(tLRPC$Dialog.id, 0L, z ? 3 : 4);
                    i++;
                }
            }
            BulletinFactory.createMuteBulletin(DialogsActivity.this, z, i, (Theme.ResourcesProvider) null).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectTab$4(ArrayList arrayList) {
            DialogsActivity.this.markDialogsAsRead(arrayList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectTab$5(boolean[] zArr, MessagesController.DialogFilter dialogFilter) {
            if (zArr[0]) {
                DialogsActivity.this.presentFragment(new FilterChatlistActivity(dialogFilter, null));
            } else {
                FilterCreateActivity.FilterInvitesBottomSheet.show(DialogsActivity.this, dialogFilter, null);
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public boolean isTabMenuVisible() {
            return DialogsActivity.this.filterOptions != null && DialogsActivity.this.filterOptions.isShown();
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onDeletePressed(int i) {
            lambda$didSelectTab$6(DialogsActivity.this.getMessagesController().getDialogFilters().get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 8 extends ActionBar.ActionBarMenuOnItemClick {
        8() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if ((i == 201 || i == 200 || i == 202 || i == 203) && DialogsActivity.this.searchViewPager != null) {
                DialogsActivity.this.searchViewPager.onActionBarItemClick(i);
            } else if (i == -1) {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                if (rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment()) {
                    if (DialogsActivity.this.filterTabsView == null || !DialogsActivity.this.filterTabsView.isEditing()) {
                        if (((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed()) {
                            if (DialogsActivity.this.searchViewPager == null || DialogsActivity.this.searchViewPager.getVisibility() != 0 || !DialogsActivity.this.searchViewPager.actionModeShowing()) {
                                DialogsActivity.this.hideActionMode(true);
                                return;
                            } else {
                                DialogsActivity.this.searchViewPager.hideActionMode();
                                return;
                            }
                        } else if (!DialogsActivity.this.onlySelect && DialogsActivity.this.folderId == 0) {
                            if (((BaseFragment) DialogsActivity.this).parentLayout == null || ((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer() == null) {
                                return;
                            }
                            ((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer().openDrawer(false);
                            return;
                        } else {
                            DialogsActivity.this.finishFragment();
                            return;
                        }
                    }
                    DialogsActivity.this.filterTabsView.setIsEditing(false);
                    DialogsActivity.this.showDoneItem(false);
                } else if (((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed()) {
                    if (DialogsActivity.this.searchViewPager == null || DialogsActivity.this.searchViewPager.getVisibility() != 0 || !DialogsActivity.this.searchViewPager.actionModeShowing()) {
                        DialogsActivity.this.hideActionMode(true);
                    } else {
                        DialogsActivity.this.searchViewPager.hideActionMode();
                    }
                } else {
                    DialogsActivity.this.rightSlidingDialogContainer.lambda$presentFragment$1();
                    if (DialogsActivity.this.searchViewPager != null) {
                        DialogsActivity.this.searchViewPager.updateTabs();
                    }
                }
            } else if (i == 1) {
                if (DialogsActivity.this.getParentActivity() == null) {
                    return;
                }
                SharedConfig.appLocked = true;
                SharedConfig.saveConfig();
                int[] iArr = new int[2];
                DialogsActivity.this.passcodeItem.getLocationInWindow(iArr);
                ((LaunchActivity) DialogsActivity.this.getParentActivity()).showPasscodeActivity(false, true, iArr[0] + (DialogsActivity.this.passcodeItem.getMeasuredWidth() / 2), iArr[1] + (DialogsActivity.this.passcodeItem.getMeasuredHeight() / 2), new Runnable() { // from class: org.telegram.ui.DialogsActivity$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.8.this.lambda$onItemClick$0();
                    }
                }, new Runnable() { // from class: org.telegram.ui.DialogsActivity$8$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.8.this.lambda$onItemClick$1();
                    }
                });
                DialogsActivity.this.getNotificationsController().showNotifications();
                DialogsActivity.this.updatePasscodeButton();
            } else if (i == 2) {
                DialogsActivity.this.presentFragment(new ProxyListActivity());
            } else if (i == 3) {
                DialogsActivity.this.showSearch(true, true, true);
                ((BaseFragment) DialogsActivity.this).actionBar.openSearchField(true);
            } else if (i == 5) {
                DialogsActivity.this.presentFragment(new ArchiveSettingsActivity());
            } else if (i == 6) {
                DialogsActivity.this.showArchiveHelp();
            } else if (i >= 10 && i < 14) {
                if (DialogsActivity.this.getParentActivity() == null) {
                    return;
                }
                DialogsActivityDelegate dialogsActivityDelegate = DialogsActivity.this.delegate;
                LaunchActivity launchActivity = (LaunchActivity) DialogsActivity.this.getParentActivity();
                launchActivity.switchToAccount(i - 10, true);
                DialogsActivity dialogsActivity = new DialogsActivity(((BaseFragment) DialogsActivity.this).arguments);
                dialogsActivity.setDelegate(dialogsActivityDelegate);
                launchActivity.presentFragment(dialogsActivity, false, true);
            } else if (i == 109) {
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                FiltersListBottomSheet filtersListBottomSheet = new FiltersListBottomSheet(dialogsActivity2, dialogsActivity2.selectedDialogs);
                filtersListBottomSheet.setDelegate(new FiltersListBottomSheet.FiltersListBottomSheetDelegate() { // from class: org.telegram.ui.DialogsActivity$8$$ExternalSyntheticLambda2
                    @Override // org.telegram.ui.Components.FiltersListBottomSheet.FiltersListBottomSheetDelegate
                    public final void didSelectFilter(MessagesController.DialogFilter dialogFilter, boolean z) {
                        DialogsActivity.8.this.lambda$onItemClick$2(dialogFilter, z);
                    }
                });
                DialogsActivity.this.showDialog(filtersListBottomSheet);
            } else if (i != 110) {
                if (i == 100 || i == 101 || i == 102 || i == 103 || i == 104 || i == 105 || i == 106 || i == 107 || i == 108) {
                    DialogsActivity dialogsActivity3 = DialogsActivity.this;
                    dialogsActivity3.performSelectedDialogsAction(dialogsActivity3.selectedDialogs, i, true, false);
                }
            } else {
                MessagesController.DialogFilter dialogFilter = DialogsActivity.this.getMessagesController().getDialogFilters().get(DialogsActivity.this.viewPages[0].selectedType);
                DialogsActivity dialogsActivity4 = DialogsActivity.this;
                ArrayList<Long> dialogsCount = FiltersListBottomSheet.getDialogsCount(dialogsActivity4, dialogFilter, dialogsActivity4.selectedDialogs, false, false);
                if ((dialogFilter != null ? dialogFilter.neverShow.size() : 0) + dialogsCount.size() > 100) {
                    DialogsActivity dialogsActivity5 = DialogsActivity.this;
                    dialogsActivity5.showDialog(AlertsCreator.createSimpleAlert(dialogsActivity5.getParentActivity(), LocaleController.getString("FilterAddToAlertFullTitle", R.string.FilterAddToAlertFullTitle), LocaleController.getString("FilterAddToAlertFullText", R.string.FilterAddToAlertFullText)).create());
                    return;
                }
                if (!dialogsCount.isEmpty()) {
                    dialogFilter.neverShow.addAll(dialogsCount);
                    for (int i2 = 0; i2 < dialogsCount.size(); i2++) {
                        Long l = dialogsCount.get(i2);
                        dialogFilter.alwaysShow.remove(l);
                        dialogFilter.pinnedDialogs.delete(l.longValue());
                    }
                    if (dialogFilter.isChatlist()) {
                        dialogFilter.neverShow.clear();
                    }
                    FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, false, false, DialogsActivity.this, null);
                }
                long longValue = dialogsCount.size() == 1 ? dialogsCount.get(0).longValue() : 0L;
                UndoView undoView = DialogsActivity.this.getUndoView();
                if (undoView != null) {
                    undoView.showWithAction(longValue, 21, Integer.valueOf(dialogsCount.size()), dialogFilter, (Runnable) null, (Runnable) null);
                }
                DialogsActivity.this.hideActionMode(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0() {
            DialogsActivity.this.passcodeItem.setAlpha(1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$1() {
            DialogsActivity.this.passcodeItem.setAlpha(0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(MessagesController.DialogFilter dialogFilter, boolean z) {
            boolean z2;
            ArrayList<Long> arrayList;
            int i;
            ArrayList<Long> arrayList2;
            long j;
            DialogsActivity dialogsActivity = DialogsActivity.this;
            ArrayList<Long> dialogsCount = FiltersListBottomSheet.getDialogsCount(dialogsActivity, dialogFilter, dialogsActivity.selectedDialogs, true, false);
            if (!z) {
                int size = (dialogFilter != null ? dialogFilter.alwaysShow.size() : 0) + dialogsCount.size();
                if ((size > DialogsActivity.this.getMessagesController().dialogFiltersChatsLimitDefault && !DialogsActivity.this.getUserConfig().isPremium()) || size > DialogsActivity.this.getMessagesController().dialogFiltersChatsLimitPremium) {
                    DialogsActivity dialogsActivity2 = DialogsActivity.this;
                    DialogsActivity dialogsActivity3 = DialogsActivity.this;
                    dialogsActivity2.showDialog(new LimitReachedBottomSheet(dialogsActivity3, dialogsActivity3.fragmentView.getContext(), 4, ((BaseFragment) DialogsActivity.this).currentAccount, null));
                    return;
                }
            }
            if (dialogFilter == null) {
                z2 = true;
                DialogsActivity.this.presentFragment(new FilterCreateActivity(null, dialogsCount));
            } else if (z) {
                for (int i2 = 0; i2 < DialogsActivity.this.selectedDialogs.size(); i2++) {
                    dialogFilter.neverShow.add((Long) DialogsActivity.this.selectedDialogs.get(i2));
                    dialogFilter.alwaysShow.remove(DialogsActivity.this.selectedDialogs.get(i2));
                }
                FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, DialogsActivity.this, null);
                long longValue = DialogsActivity.this.selectedDialogs.size() == 1 ? ((Long) DialogsActivity.this.selectedDialogs.get(0)).longValue() : 0L;
                UndoView undoView = DialogsActivity.this.getUndoView();
                if (undoView != null) {
                    undoView.showWithAction(longValue, 21, Integer.valueOf(DialogsActivity.this.selectedDialogs.size()), dialogFilter, (Runnable) null, (Runnable) null);
                }
                z2 = true;
            } else {
                if (dialogsCount.isEmpty()) {
                    arrayList = dialogsCount;
                    i = 0;
                } else {
                    for (int i3 = 0; i3 < dialogsCount.size(); i3++) {
                        dialogFilter.neverShow.remove(dialogsCount.get(i3));
                    }
                    dialogFilter.alwaysShow.addAll(dialogsCount);
                    arrayList = dialogsCount;
                    i = 0;
                    FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, DialogsActivity.this, null);
                }
                z2 = true;
                if (arrayList.size() == 1) {
                    arrayList2 = arrayList;
                    j = arrayList2.get(i).longValue();
                } else {
                    arrayList2 = arrayList;
                    j = 0;
                }
                UndoView undoView2 = DialogsActivity.this.getUndoView();
                if (undoView2 != null) {
                    undoView2.showWithAction(j, 20, Integer.valueOf(arrayList2.size()), dialogFilter, (Runnable) null, (Runnable) null);
                }
            }
            DialogsActivity.this.hideActionMode(z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 11 extends LinearLayoutManager {
        private boolean fixOffset;
        boolean lastDragging;
        ValueAnimator storiesOverscrollAnimator;
        final /* synthetic */ ViewPage val$viewPage;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        11(Context context, ViewPage viewPage) {
            super(context);
            this.val$viewPage = viewPage;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        protected int firstPosition() {
            return (this.val$viewPage.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive() && this.val$viewPage.archivePullViewState == 2) ? 1 : 0;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void scrollToPositionWithOffset(int i, int i2) {
            if (this.fixOffset) {
                i2 -= this.val$viewPage.listView.getPaddingTop();
            }
            super.scrollToPositionWithOffset(i, i2);
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.ItemTouchHelper.ViewDropHandler
        public void prepareForDrop(View view, View view2, int i, int i2) {
            this.fixOffset = true;
            super.prepareForDrop(view, view2, i, i2);
            this.fixOffset = false;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
            if (DialogsActivity.this.hasHiddenArchive() && i == 1) {
                super.smoothScrollToPosition(recyclerView, state, i);
                return;
            }
            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 0);
            linearSmoothScrollerCustom.setTargetPosition(i);
            startSmoothScroll(linearSmoothScrollerCustom);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void onScrollStateChanged(int i) {
            super.onScrollStateChanged(i);
            ValueAnimator valueAnimator = this.storiesOverscrollAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.storiesOverscrollAnimator.cancel();
            }
            if (this.val$viewPage.listView.getScrollState() != 1) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(DialogsActivity.this.storiesOverscroll, 0.0f);
                this.storiesOverscrollAnimator = ofFloat;
                final ViewPage viewPage = this.val$viewPage;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$11$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        DialogsActivity.11.this.lambda$onScrollStateChanged$0(viewPage, valueAnimator2);
                    }
                });
                this.storiesOverscrollAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.11.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        11 r3 = 11.this;
                        DialogsActivity.this.setStoriesOvercroll(r3.val$viewPage, 0.0f);
                    }
                });
                this.storiesOverscrollAnimator.setDuration(200L);
                this.storiesOverscrollAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.storiesOverscrollAnimator.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onScrollStateChanged$0(ViewPage viewPage, ValueAnimator valueAnimator) {
            DialogsActivity.this.setStoriesOvercroll(viewPage, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        /* JADX WARN: Code restructure failed: missing block: B:117:0x01ff, code lost:
            if (r5 > (-1)) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x00c9, code lost:
            if (r15.rightSlidingDialogContainer.hasFragment() == false) goto L161;
         */
        /* JADX WARN: Removed duplicated region for block: B:123:0x020d  */
        /* JADX WARN: Removed duplicated region for block: B:129:0x022e  */
        /* JADX WARN: Removed duplicated region for block: B:130:0x0232  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x0245  */
        /* JADX WARN: Removed duplicated region for block: B:220:0x0433 A[ADDED_TO_REGION] */
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int i2;
            View findViewByPosition;
            int scrollVerticallyBy;
            DialogsActivity dialogsActivity;
            float viewOffset;
            DialogsRecyclerView dialogsRecyclerView = this.val$viewPage.listView;
            int i3 = 0;
            if (dialogsRecyclerView.fastScrollAnimationRunning) {
                return 0;
            }
            boolean z = dialogsRecyclerView.getScrollState() == 1;
            if (z != this.lastDragging) {
                this.lastDragging = z;
                if (!z && DialogsActivity.this.checkAutoscrollToStories(this.val$viewPage)) {
                    return 0;
                }
            }
            float f = 0.0f;
            if (i > 0 && DialogsActivity.this.storiesOverscroll != 0.0f) {
                float f2 = DialogsActivity.this.storiesOverscroll - i;
                if (f2 < 0.0f) {
                    i3 = (int) (-f2);
                } else {
                    f = f2;
                }
                DialogsActivity.this.setStoriesOvercroll(this.val$viewPage, f);
                return super.scrollVerticallyBy(i3, recycler, state);
            }
            int paddingTop = this.val$viewPage.listView.getPaddingTop();
            DialogsActivity dialogsActivity2 = DialogsActivity.this;
            int dp = (!dialogsActivity2.hasStories || dialogsActivity2.rightSlidingDialogContainer.hasFragment() || DialogsActivity.this.fixScrollYAfterArchiveOpened) ? paddingTop : paddingTop - AndroidUtilities.dp(81.0f);
            int i4 = (!DialogsActivity.this.fixScrollYAfterArchiveOpened && this.val$viewPage.dialogsType == 0 && !DialogsActivity.this.onlySelect && DialogsActivity.this.folderId == 0 && DialogsActivity.this.getMessagesController().hasHiddenArchive() && this.val$viewPage.archivePullViewState == 2) ? 1 : 0;
            if (i4 == 0) {
                DialogsActivity dialogsActivity3 = DialogsActivity.this;
                if (dialogsActivity3.hasStories) {
                }
                r13 = i;
                if (this.val$viewPage.dialogsType == 0 && this.val$viewPage.listView.getViewOffset() != 0.0f && i > 0 && z) {
                    viewOffset = ((int) this.val$viewPage.listView.getViewOffset()) - i;
                    if (viewOffset >= 0.0f) {
                        r13 = (int) viewOffset;
                        viewOffset = 0.0f;
                    } else {
                        r13 = 0;
                    }
                    this.val$viewPage.listView.setViewsOffset(viewOffset);
                }
                if (this.val$viewPage.dialogsType != 0 && this.val$viewPage.archivePullViewState != 0 && DialogsActivity.this.hasHiddenArchive() && !DialogsActivity.this.fixScrollYAfterArchiveOpened) {
                    int scrollVerticallyBy2 = super.scrollVerticallyBy(r13, recycler, state);
                    if (this.val$viewPage.pullForegroundDrawable != null) {
                        this.val$viewPage.pullForegroundDrawable.scrollDy = scrollVerticallyBy2;
                    }
                    int findFirstVisibleItemPosition = this.val$viewPage.layoutManager.findFirstVisibleItemPosition();
                    View findViewByPosition2 = findFirstVisibleItemPosition == 0 ? this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition) : null;
                    if (findFirstVisibleItemPosition != 0 || findViewByPosition2 == null || findViewByPosition2.getBottom() - dp < AndroidUtilities.dp(4.0f)) {
                        DialogsActivity.this.startArchivePullingTime = 0L;
                        DialogsActivity.this.canShowHiddenArchive = false;
                        boolean z2 = this.val$viewPage.archivePullViewState != 2;
                        this.val$viewPage.archivePullViewState = 2;
                        if (z2 && AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                            AndroidUtilities.makeAccessibilityAnnouncement(LocaleController.getString(R.string.AccDescrArchivedChatsHidden));
                        }
                        if (this.val$viewPage.pullForegroundDrawable != null) {
                            this.val$viewPage.pullForegroundDrawable.resetText();
                            this.val$viewPage.pullForegroundDrawable.pullProgress = 0.0f;
                            this.val$viewPage.pullForegroundDrawable.setListView(this.val$viewPage.listView);
                        }
                    } else {
                        if (DialogsActivity.this.startArchivePullingTime == 0) {
                            DialogsActivity.this.startArchivePullingTime = System.currentTimeMillis();
                        }
                        if (this.val$viewPage.archivePullViewState == 2 && this.val$viewPage.pullForegroundDrawable != null) {
                            this.val$viewPage.pullForegroundDrawable.showHidden();
                        }
                        DialogsActivity dialogsActivity4 = DialogsActivity.this;
                        if (dialogsActivity4.hasStories && !dialogsActivity4.rightSlidingDialogContainer.hasFragment() && !DialogsActivity.this.fixScrollYAfterArchiveOpened) {
                            dp += AndroidUtilities.dp(81.0f);
                        }
                        float top = ((findViewByPosition2.getTop() - dp) / findViewByPosition2.getMeasuredHeight()) + 1.0f;
                        if (top > 1.0f) {
                            top = 1.0f;
                        }
                        boolean z3 = top > 0.85f && System.currentTimeMillis() - DialogsActivity.this.startArchivePullingTime > 220;
                        if (DialogsActivity.this.canShowHiddenArchive != z3) {
                            DialogsActivity.this.canShowHiddenArchive = z3;
                            if (this.val$viewPage.archivePullViewState == 2) {
                                this.val$viewPage.listView.performHapticFeedback(3, 2);
                                if (this.val$viewPage.pullForegroundDrawable != null) {
                                    this.val$viewPage.pullForegroundDrawable.colorize(z3);
                                }
                            }
                        }
                        if (this.val$viewPage.archivePullViewState == 2 && r13 - scrollVerticallyBy2 != 0 && i < 0 && z) {
                            this.val$viewPage.listView.setViewsOffset(this.val$viewPage.listView.getViewOffset() - ((i * 0.2f) * (1.0f - (this.val$viewPage.listView.getViewOffset() / PullForegroundDrawable.getMaxOverscroll()))));
                        }
                        if (this.val$viewPage.pullForegroundDrawable != null) {
                            this.val$viewPage.pullForegroundDrawable.pullProgress = top;
                            this.val$viewPage.pullForegroundDrawable.setListView(this.val$viewPage.listView);
                        }
                    }
                    if (findViewByPosition2 != null) {
                        findViewByPosition2.invalidate();
                    }
                    if (this.val$viewPage.archivePullViewState == 1 && scrollVerticallyBy2 == 0 && i < 0 && z && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                        DialogsActivity dialogsActivity5 = DialogsActivity.this;
                        if (dialogsActivity5.hasStories && dialogsActivity5.progressToActionMode == 0.0f) {
                            DialogsActivity.this.setStoriesOvercroll(this.val$viewPage, DialogsActivity.this.storiesOverscroll - (i * AndroidUtilities.lerp(0.2f, 0.5f, DialogsActivity.this.dialogStoriesCell.overscrollProgress())));
                        }
                    }
                    return scrollVerticallyBy2;
                }
                scrollVerticallyBy = super.scrollVerticallyBy(r13, recycler, state);
                if (scrollVerticallyBy == 0 && i < 0 && z && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                    dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories && dialogsActivity.progressToActionMode == 0.0f) {
                        DialogsActivity.this.setStoriesOvercroll(this.val$viewPage, DialogsActivity.this.storiesOverscroll - (i * AndroidUtilities.lerp(0.2f, 0.5f, DialogsActivity.this.dialogStoriesCell.overscrollProgress())));
                    }
                }
                return scrollVerticallyBy;
            }
            if (i < 0) {
                this.val$viewPage.listView.setOverScrollMode(0);
                int findFirstVisibleItemPosition2 = this.val$viewPage.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition2 == 0 && (findViewByPosition = this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition2)) != null && findViewByPosition.getBottom() - dp <= AndroidUtilities.dp(1.0f)) {
                    findFirstVisibleItemPosition2 = 1;
                }
                if (!z) {
                    View findViewByPosition3 = this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition2);
                    if (findViewByPosition3 != null && findFirstVisibleItemPosition2 < 10) {
                        AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f);
                        int i5 = 0;
                        while (i4 < findFirstVisibleItemPosition2) {
                            i5 += this.val$viewPage.dialogsAdapter.getItemHeight(i4);
                            i4++;
                        }
                        int i6 = (-(findViewByPosition3.getTop() - dp)) + i5;
                        if (DialogsActivity.this.hasStories && ((this.val$viewPage.scroller.isRunning() || DialogsActivity.this.dialogStoriesCell.isExpanded()) && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment() && !DialogsActivity.this.fixScrollYAfterArchiveOpened)) {
                            i6 += AndroidUtilities.dp(81.0f);
                        }
                        if (i6 < Math.abs(i)) {
                            i2 = -i6;
                            r13 = i2;
                        }
                    }
                } else if (findFirstVisibleItemPosition2 == 0 && i4 != 0) {
                    View findViewByPosition4 = this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition2);
                    float top2 = ((findViewByPosition4.getTop() - paddingTop) / findViewByPosition4.getMeasuredHeight()) + 1.0f;
                    if (top2 > 1.0f) {
                        top2 = 1.0f;
                    }
                    this.val$viewPage.listView.setOverScrollMode(2);
                    int i7 = (int) (i * (0.45f - (top2 * 0.25f)));
                    r13 = i7 <= -1 ? i7 : -1;
                    if (DialogsActivity.this.undoView[0] != null && DialogsActivity.this.undoView[0].getVisibility() == 0) {
                        DialogsActivity.this.undoView[0].hide(true, 1);
                    }
                } else if ((findFirstVisibleItemPosition2 == 1 && i4 != 0) || findFirstVisibleItemPosition2 == 0) {
                    DialogsActivity dialogsActivity6 = DialogsActivity.this;
                    if (dialogsActivity6.hasStories && z && !dialogsActivity6.rightSlidingDialogContainer.hasFragment()) {
                        if (DialogsActivity.this.scrollYOffset == 0.0f) {
                            this.val$viewPage.listView.setOverScrollMode(0);
                        } else {
                            this.val$viewPage.listView.setOverScrollMode(2);
                        }
                        i2 = (int) (i * 0.3f);
                    }
                }
                if (this.val$viewPage.dialogsType == 0) {
                    viewOffset = ((int) this.val$viewPage.listView.getViewOffset()) - i;
                    if (viewOffset >= 0.0f) {
                    }
                    this.val$viewPage.listView.setViewsOffset(viewOffset);
                }
                if (this.val$viewPage.dialogsType != 0) {
                }
                scrollVerticallyBy = super.scrollVerticallyBy(r13, recycler, state);
                if (scrollVerticallyBy == 0) {
                    dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories) {
                        DialogsActivity.this.setStoriesOvercroll(this.val$viewPage, DialogsActivity.this.storiesOverscroll - (i * AndroidUtilities.lerp(0.2f, 0.5f, DialogsActivity.this.dialogStoriesCell.overscrollProgress())));
                    }
                }
                return scrollVerticallyBy;
            }
            r13 = i;
            if (this.val$viewPage.dialogsType == 0) {
            }
            if (this.val$viewPage.dialogsType != 0) {
            }
            scrollVerticallyBy = super.scrollVerticallyBy(r13, recycler, state);
            if (scrollVerticallyBy == 0) {
            }
            return scrollVerticallyBy;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (BuildVars.DEBUG_PRIVATE_VERSION) {
                try {
                    super.onLayoutChildren(recycler, state);
                    return;
                } catch (IndexOutOfBoundsException unused) {
                    throw new RuntimeException("Inconsistency detected. dialogsListIsFrozen=" + DialogsActivity.this.dialogsListFrozen + " lastUpdateAction=" + DialogsActivity.this.debugLastUpdateAction);
                }
            }
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                FileLog.e(e);
                final ViewPage viewPage = this.val$viewPage;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$11$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.11.lambda$onLayoutChildren$1(DialogsActivity.ViewPage.this);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onLayoutChildren$1(ViewPage viewPage) {
            viewPage.dialogsAdapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(ViewPage viewPage, View view, int i, float f, float f2) {
        boolean z = view instanceof DialogCell;
        if (z) {
            DialogCell dialogCell = (DialogCell) view;
            if (dialogCell.isBlocked()) {
                showPremiumBlockedToast(view, dialogCell.getDialogId());
                return;
            }
        }
        int i2 = this.initialDialogsType;
        if (i2 == 15 && (view instanceof TextCell)) {
            viewPage.dialogsAdapter.onCreateGroupForThisClick();
        } else if (i2 == 10) {
            onItemLongClick(viewPage.listView, view, i, 0.0f, 0.0f, viewPage.dialogsType, viewPage.dialogsAdapter);
        } else if ((i2 == 11 || i2 == 13) && i == 1) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("forImport", true);
            bundle.putLongArray("result", new long[]{getUserConfig().getClientUserId()});
            bundle.putInt("chatType", 4);
            String string = this.arguments.getString("importTitle");
            if (string != null) {
                bundle.putString("title", string);
            }
            GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle);
            groupCreateFinalActivity.setDelegate(new GroupCreateFinalActivity.GroupCreateFinalActivityDelegate() { // from class: org.telegram.ui.DialogsActivity.12
                @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
                public void didFailChatCreation() {
                }

                @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
                public void didStartChatCreation() {
                }

                @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
                public void didFinishChatCreation(GroupCreateFinalActivity groupCreateFinalActivity2, long j) {
                    ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                    arrayList.add(MessagesStorage.TopicKey.of(-j, 0L));
                    DialogsActivityDelegate dialogsActivityDelegate = DialogsActivity.this.delegate;
                    if (DialogsActivity.this.closeFragment) {
                        DialogsActivity.this.removeSelfFromStack();
                    }
                    dialogsActivityDelegate.didSelectDialogs(DialogsActivity.this, arrayList, null, true, null);
                }
            });
            presentFragment(groupCreateFinalActivity);
        } else {
            if ((view instanceof DialogsHintCell) && (viewPage.dialogsType == 7 || viewPage.dialogsType == 8)) {
                TL_chatlists$TL_chatlists_chatlistUpdates chatlistUpdate = viewPage.dialogsAdapter.getChatlistUpdate();
                if (chatlistUpdate != null) {
                    MessagesController.DialogFilter dialogFilter = getMessagesController().selectedDialogFilter[viewPage.dialogsType - 7];
                    if (dialogFilter != null) {
                        showDialog(new FolderBottomSheet(this, dialogFilter.id, chatlistUpdate));
                        return;
                    }
                    return;
                }
            } else if (z && !this.actionBar.isActionModeShowed() && !this.rightSlidingDialogContainer.hasFragment()) {
                DialogCell dialogCell2 = (DialogCell) view;
                AndroidUtilities.rectTmp.set(dialogCell2.avatarImage.getImageX(), dialogCell2.avatarImage.getImageY(), dialogCell2.avatarImage.getImageX2(), dialogCell2.avatarImage.getImageY2());
            }
            onItemClick(view, i, viewPage.dialogsAdapter, f, f2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(View view, int i) {
        this.filtersView.cancelClickRunnables(true);
        addSearchFilter(this.filtersView.getFilterAt(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(View view) {
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout != null && iNavigationLayout.isInPreviewMode()) {
            finishPreviewFragment();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("destroyAfterSelect", true);
        presentFragment(new ContactsActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(View view) {
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout != null && iNavigationLayout.isInPreviewMode()) {
            finishPreviewFragment();
        } else if (this.initialDialogsType == 10) {
            if (this.delegate == null || this.selectedDialogs.isEmpty()) {
                return;
            }
            ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
            for (int i = 0; i < this.selectedDialogs.size(); i++) {
                arrayList.add(MessagesStorage.TopicKey.of(this.selectedDialogs.get(i).longValue(), 0L));
            }
            this.delegate.didSelectDialogs(this, arrayList, null, false, null);
        } else if (this.floatingButton.getVisibility() == 0) {
            if (!this.storiesEnabled) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("destroyAfterSelect", true);
                presentFragment(new ContactsActivity(bundle));
                return;
            }
            HintView2 hintView2 = this.storyHint;
            if (hintView2 != null) {
                hintView2.hide();
            }
            StoriesController.StoryLimit checkStoryLimit = MessagesController.getInstance(this.currentAccount).getStoriesController().checkStoryLimit();
            if (checkStoryLimit != null) {
                showDialog(new LimitReachedBottomSheet(this, getContext(), checkStoryLimit.getLimitReachedType(), this.currentAccount, null));
            } else {
                StoryRecorder.getInstance(getParentActivity(), this.currentAccount).closeToWhenSent(new StoryRecorder.ClosingViewProvider() { // from class: org.telegram.ui.DialogsActivity.19
                    @Override // org.telegram.ui.Stories.recorder.StoryRecorder.ClosingViewProvider
                    public void preLayout(long j, final Runnable runnable) {
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        if (dialogsActivity.dialogStoriesCell != null) {
                            dialogsActivity.scrollToTop(false, true);
                            DialogsActivity.this.invalidateScrollY = true;
                            DialogsActivity.this.fragmentView.invalidate();
                            if (j == 0 || j == DialogsActivity.this.getUserConfig().getClientUserId()) {
                                DialogsActivity.this.dialogStoriesCell.scrollToFirstCell();
                            } else {
                                DialogsActivity.this.dialogStoriesCell.scrollTo(j);
                            }
                            DialogsActivity.this.viewPages[0].listView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.DialogsActivity.19.1
                                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                                public boolean onPreDraw() {
                                    DialogsActivity.this.viewPages[0].listView.getViewTreeObserver().removeOnPreDrawListener(this);
                                    AndroidUtilities.runOnUIThread(runnable, 100L);
                                    return false;
                                }
                            });
                            return;
                        }
                        runnable.run();
                    }

                    @Override // org.telegram.ui.Stories.recorder.StoryRecorder.ClosingViewProvider
                    public StoryRecorder.SourceView getView(long j) {
                        DialogStoriesCell dialogStoriesCell = DialogsActivity.this.dialogStoriesCell;
                        return StoryRecorder.SourceView.fromStoryCell(dialogStoriesCell != null ? dialogStoriesCell.findStoryCell(j) : null);
                    }
                }).open(StoryRecorder.SourceView.fromFloatingButton(this.floatingButtonContainer), true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12() {
        MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putBoolean("storyhint", false).commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(Long l) {
        this.cacheSize = l;
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(Long l, Long l2) {
        this.deviceSize = l;
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$15(View view) {
        if (this.delegate == null || this.selectedDialogs.isEmpty()) {
            return;
        }
        ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
        for (int i = 0; i < this.selectedDialogs.size(); i++) {
            arrayList.add(MessagesStorage.TopicKey.of(this.selectedDialogs.get(i).longValue(), 0L));
        }
        this.delegate.didSelectDialogs(this, arrayList, this.commentView.getFieldText(), false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$16(FrameLayout frameLayout, View view) {
        if (this.isNextButton) {
            return false;
        }
        onSendLongClick(frameLayout);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 26 extends DialogStoriesCell {
        26(Context context, BaseFragment baseFragment, int i, int i2) {
            super(context, baseFragment, i, i2);
        }

        @Override // org.telegram.ui.Stories.DialogStoriesCell
        public void onUserLongPressed(final View view, final long j) {
            long j2;
            CombinedDrawable combinedDrawable;
            Drawable drawable;
            DialogsActivity dialogsActivity = DialogsActivity.this;
            dialogsActivity.filterOptions = ItemOptions.makeOptions(dialogsActivity, view).setViewAdditionalOffsets(0, AndroidUtilities.dp(8.0f), 0, 0).setScrimViewBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), DialogsActivity.this.canShowFilterTabsView ? AndroidUtilities.dp(6.0f) : 0, Theme.getColor(DialogsActivity.this.isArchive() ? Theme.key_actionBarDefaultArchived : Theme.key_actionBarDefault)));
            if (UserObject.isService(j)) {
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
                return;
            }
            view.performHapticFeedback(0);
            if (j == UserConfig.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getClientUserId()) {
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                if (dialogsActivity2.storiesEnabled) {
                    ItemOptions itemOptions = dialogsActivity2.filterOptions;
                    int i = R.drawable.msg_stories_add;
                    String string = LocaleController.getString("AddStory", R.string.AddStory);
                    int i2 = Theme.key_actionBarDefaultSubmenuItemIcon;
                    int i3 = Theme.key_actionBarDefaultSubmenuItem;
                    itemOptions.add(i, string, i2, i3, new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.26.this.lambda$onUserLongPressed$0();
                        }
                    });
                    DialogsActivity.this.filterOptions.add(R.drawable.msg_stories_archive, LocaleController.getString("ArchivedStories", R.string.ArchivedStories), i2, i3, new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.26.this.lambda$onUserLongPressed$1();
                        }
                    });
                    DialogsActivity.this.filterOptions.add(R.drawable.msg_stories_saved, LocaleController.getString("SavedStories", R.string.SavedStories), i2, i3, new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.26.this.lambda$onUserLongPressed$2();
                        }
                    });
                } else {
                    DialogStoriesCell dialogStoriesCell = dialogsActivity2.dialogStoriesCell;
                    if (dialogStoriesCell != null) {
                        dialogStoriesCell.showPremiumHint();
                        return;
                    }
                    return;
                }
            } else {
                TLRPC$Chat chat = DialogsActivity.this.getMessagesController().getChat(Long.valueOf(-j));
                final String sharedPrefKey = NotificationsController.getSharedPrefKey(j, 0L);
                boolean z = !NotificationsCustomSettingsActivity.areStoriesNotMuted(((BaseFragment) DialogsActivity.this).currentAccount, j);
                boolean premiumFeaturesBlocked = MessagesController.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).premiumFeaturesBlocked();
                boolean isPremium = UserConfig.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).isPremium();
                boolean hasUnreadStories = MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController().hasUnreadStories(j);
                if (premiumFeaturesBlocked || j <= 0 || isPremium || (drawable = ContextCompat.getDrawable(getContext(), R.drawable.msg_gallery_locked2)) == null) {
                    j2 = 0;
                    combinedDrawable = null;
                } else {
                    Drawable drawable2 = ContextCompat.getDrawable(getContext(), R.drawable.msg_stealth_locked);
                    if (drawable2 != null) {
                        drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultSubmenuItemIcon, ((BaseFragment) DialogsActivity.this).resourceProvider), PorterDuff.Mode.MULTIPLY));
                    }
                    drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(-1, -16777216, 0.5f), PorterDuff.Mode.MULTIPLY));
                    combinedDrawable = new CombinedDrawable(drawable2, drawable);
                    j2 = 0;
                }
                if (j < j2 && DialogsActivity.this.getStoriesController().canPostStories(j)) {
                    DialogsActivity.this.filterOptions.add(R.drawable.msg_stories_add, LocaleController.getString("AddStory", R.string.AddStory), Theme.key_actionBarDefaultSubmenuItemIcon, Theme.key_actionBarDefaultSubmenuItem, new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.26.this.lambda$onUserLongPressed$3(j);
                        }
                    });
                }
                ItemOptions makeMultiline = DialogsActivity.this.filterOptions.addIf(j > 0, R.drawable.msg_discussion, LocaleController.getString("SendMessage", R.string.SendMessage), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$4(j);
                    }
                }).addIf(j > 0, R.drawable.msg_openprofile, LocaleController.getString("OpenProfile", R.string.OpenProfile), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$5(j);
                    }
                }).addIf(j < 0, R.drawable.msg_channel, LocaleController.getString(ChatObject.isChannelAndNotMegaGroup(chat) ? R.string.OpenChannel2 : R.string.OpenGroup2), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$6(j);
                    }
                }).addIf(!z && j > 0, R.drawable.msg_mute, LocaleController.getString("NotificationsStoryMute2", R.string.NotificationsStoryMute2), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$7(sharedPrefKey, j);
                    }
                }).makeMultiline(false).addIf(z && j > 0, R.drawable.msg_unmute, LocaleController.getString("NotificationsStoryUnmute2", R.string.NotificationsStoryUnmute2), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$8(sharedPrefKey, j);
                    }
                }).makeMultiline(false);
                boolean z2 = !premiumFeaturesBlocked && j > 0 && isPremium && hasUnreadStories;
                int i4 = R.drawable.msg_stories_stealth2;
                int i5 = R.string.ViewAnonymously;
                makeMultiline.addIf(z2, i4, LocaleController.getString(i5), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$10(view);
                    }
                }).makeMultiline(false).addIf(!premiumFeaturesBlocked && j > 0 && !isPremium && hasUnreadStories, i4, combinedDrawable, LocaleController.getString(i5), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$12(view);
                    }
                }).makeMultiline(false).addIf(!DialogsActivity.this.isArchive(), R.drawable.msg_archive, LocaleController.getString("ArchivePeerStories", R.string.ArchivePeerStories), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$13(j);
                    }
                }).makeMultiline(false).addIf(DialogsActivity.this.isArchive(), R.drawable.msg_unarchive, LocaleController.getString("UnarchiveStories", R.string.UnarchiveStories), new Runnable() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.26.this.lambda$onUserLongPressed$14(j);
                    }
                }).makeMultiline(false);
            }
            DialogsActivity.this.filterOptions.setGravity(3).translate(AndroidUtilities.dp(-8.0f), AndroidUtilities.dp(-10.0f)).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$0() {
            DialogsActivity.this.dialogStoriesCell.openStoryRecorder();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$1() {
            Bundle bundle = new Bundle();
            bundle.putLong("dialog_id", UserConfig.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getClientUserId());
            bundle.putInt("type", 1);
            bundle.putInt("start_from", 9);
            DialogsActivity.this.presentFragment(new MediaActivity(bundle, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$2() {
            Bundle bundle = new Bundle();
            bundle.putLong("dialog_id", UserConfig.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getClientUserId());
            bundle.putInt("type", 1);
            DialogsActivity.this.presentFragment(new MediaActivity(bundle, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$3(long j) {
            DialogsActivity.this.dialogStoriesCell.openStoryRecorder(j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$4(long j) {
            DialogsActivity.this.presentFragment(ChatActivity.of(j));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$5(long j) {
            DialogsActivity.this.presentFragment(ProfileActivity.of(j));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$6(long j) {
            DialogsActivity.this.presentFragment(ChatActivity.of(j));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$7(String str, long j) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(((BaseFragment) DialogsActivity.this).currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, false).apply();
            DialogsActivity.this.getNotificationsController().updateServerNotificationsSettings(j, 0L);
            TLRPC$User user = MessagesController.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getUser(Long.valueOf(j));
            String trim = user == null ? "" : user.first_name.trim();
            int indexOf = trim.indexOf(" ");
            if (indexOf > 0) {
                trim = trim.substring(0, indexOf);
            }
            BulletinFactory.of(DialogsActivity.this).createUsersBulletin(Arrays.asList(user), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryMutedHint", R.string.NotificationsStoryMutedHint, trim))).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$8(String str, long j) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(((BaseFragment) DialogsActivity.this).currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, true).apply();
            DialogsActivity.this.getNotificationsController().updateServerNotificationsSettings(j, 0L);
            TLRPC$User user = MessagesController.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getUser(Long.valueOf(j));
            String trim = user == null ? "" : user.first_name.trim();
            int indexOf = trim.indexOf(" ");
            if (indexOf > 0) {
                trim = trim.substring(0, indexOf);
            }
            BulletinFactory.of(DialogsActivity.this).createUsersBulletin(Arrays.asList(user), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryUnmutedHint", R.string.NotificationsStoryUnmutedHint, trim))).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$10(final View view) {
            TL_stories$TL_storiesStealthMode stealthMode = MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController().getStealthMode();
            if (stealthMode != null && ConnectionsManager.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getCurrentTime() < stealthMode.active_until_date) {
                if (view instanceof DialogStoriesCell.StoryCell) {
                    DialogsActivity.this.dialogStoriesCell.openStoryForCell((DialogStoriesCell.StoryCell) view);
                    return;
                }
                return;
            }
            StealthModeAlert stealthModeAlert = new StealthModeAlert(getContext(), 0.0f, 1, ((BaseFragment) DialogsActivity.this).resourceProvider);
            stealthModeAlert.setListener(new StealthModeAlert.Listener() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda15
                @Override // org.telegram.ui.Stories.StealthModeAlert.Listener
                public final void onButtonClicked(boolean z) {
                    DialogsActivity.26.this.lambda$onUserLongPressed$9(view, z);
                }
            });
            DialogsActivity.this.showDialog(stealthModeAlert);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$9(View view, boolean z) {
            if (view instanceof DialogStoriesCell.StoryCell) {
                DialogsActivity.this.dialogStoriesCell.openStoryForCell((DialogStoriesCell.StoryCell) view);
                if (z) {
                    AndroidUtilities.runOnUIThread(DialogsActivity$26$$ExternalSyntheticLambda13.INSTANCE, 500L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$12(final View view) {
            StealthModeAlert stealthModeAlert = new StealthModeAlert(getContext(), 0.0f, 1, ((BaseFragment) DialogsActivity.this).resourceProvider);
            stealthModeAlert.setListener(new StealthModeAlert.Listener() { // from class: org.telegram.ui.DialogsActivity$26$$ExternalSyntheticLambda14
                @Override // org.telegram.ui.Stories.StealthModeAlert.Listener
                public final void onButtonClicked(boolean z) {
                    DialogsActivity.26.this.lambda$onUserLongPressed$11(view, z);
                }
            });
            DialogsActivity.this.showDialog(stealthModeAlert);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$11(View view, boolean z) {
            if (view instanceof DialogStoriesCell.StoryCell) {
                DialogsActivity.this.dialogStoriesCell.openStoryForCell((DialogStoriesCell.StoryCell) view);
                if (z) {
                    AndroidUtilities.runOnUIThread(DialogsActivity$26$$ExternalSyntheticLambda13.INSTANCE, 500L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$13(long j) {
            DialogsActivity.this.toggleArciveForStory(j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserLongPressed$14(long j) {
            DialogsActivity.this.toggleArciveForStory(j);
        }

        @Override // org.telegram.ui.Stories.DialogStoriesCell
        public void onMiniListClicked() {
            DialogsActivity dialogsActivity = DialogsActivity.this;
            if (dialogsActivity.hasOnlySlefStories && dialogsActivity.getStoriesController().hasOnlySelfStories()) {
                DialogsActivity.this.dialogStoriesCell.openSelfStories();
            } else {
                DialogsActivity.this.scrollToTop(true, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$17(View view) {
        if (SharedConfig.isAppUpdateAvailable()) {
            AndroidUtilities.openForView(SharedConfig.pendingAppUpdate.document, true, getParentActivity());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$18(View view) {
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStoriesOvercroll(ViewPage viewPage, float f) {
        if (this.storiesOverscroll == f) {
            return;
        }
        this.storiesOverscroll = f;
        if (f == 0.0f) {
            this.storiesOverscrollCalled = false;
        }
        this.dialogStoriesCell.setOverscoll(f);
        viewPage.listView.setViewsOffset(f);
        viewPage.listView.setOverScrollMode(f != 0.0f ? 2 : 0);
        this.fragmentView.invalidate();
        if (f <= AndroidUtilities.dp(90.0f) || this.storiesOverscrollCalled) {
            return;
        }
        this.storiesOverscrollCalled = true;
        getOrCreateStoryViewer().doOnAnimationReady(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda83
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$setStoriesOvercroll$19();
            }
        });
        this.dialogStoriesCell.openOverscrollSelectedStory();
        this.dialogStoriesCell.performHapticFeedback(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStoriesOvercroll$19() {
        this.fragmentView.dispatchTouchEvent(AndroidUtilities.emptyMotionEvent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleArciveForStory(final long j) {
        final boolean z = !isArchive();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda89
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$toggleArciveForStory$22(j, z);
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleArciveForStory$22(final long j, final boolean z) {
        String str;
        TLRPC$Chat tLRPC$Chat;
        getMessagesController().getStoriesController().toggleHidden(j, z, false, true);
        BulletinFactory.UndoObject undoObject = new BulletinFactory.UndoObject();
        undoObject.onUndo = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda90
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$toggleArciveForStory$20(j, z);
            }
        };
        undoObject.onAction = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda92
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$toggleArciveForStory$21(j, z);
            }
        };
        if (j >= 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
            str = ContactsController.formatName(user.first_name, null, 15);
            tLRPC$Chat = user;
        } else {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j));
            str = chat.title;
            tLRPC$Chat = chat;
        }
        this.storiesBulletin = BulletinFactory.global().createUsersBulletin(Collections.singletonList(tLRPC$Chat), isArchive() ? AndroidUtilities.replaceTags(LocaleController.formatString("StoriesMovedToDialogs", R.string.StoriesMovedToDialogs, str)) : AndroidUtilities.replaceTags(LocaleController.formatString("StoriesMovedToContacts", R.string.StoriesMovedToContacts, ContactsController.formatName(str, null, 15))), null, undoObject).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleArciveForStory$20(long j, boolean z) {
        getMessagesController().getStoriesController().toggleHidden(j, !z, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleArciveForStory$21(long j, boolean z) {
        getMessagesController().getStoriesController().toggleHidden(j, z, true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkAutoscrollToStories(ViewPage viewPage) {
        FilterTabsView filterTabsView;
        if ((this.hasStories || ((filterTabsView = this.filterTabsView) != null && filterTabsView.getVisibility() == 0)) && !this.rightSlidingDialogContainer.hasFragment()) {
            int i = (int) (-this.scrollYOffset);
            int maxScrollYOffset = getMaxScrollYOffset();
            if (i == 0 || i == maxScrollYOffset) {
                return false;
            }
            if (i < maxScrollYOffset / 2) {
                if (viewPage.listView.canScrollVertically(-1)) {
                    viewPage.scroller.smoothScrollBy(-i);
                    return true;
                }
                return false;
            } else if (viewPage.listView.canScrollVertically(1)) {
                viewPage.scroller.smoothScrollBy(maxScrollYOffset - i);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getActionBarMoveFrom(boolean z) {
        float dp = this.hasStories ? 0.0f + AndroidUtilities.dp(81.0f) : 0.0f;
        if (z) {
            dp += AndroidUtilities.dp(44.0f);
        }
        DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null && dialogsHintCell.getVisibility() == 0) {
            dp += this.dialogsHintCell.getMeasuredHeight();
        }
        UnconfirmedAuthHintCell unconfirmedAuthHintCell = this.authHintCell;
        return (unconfirmedAuthHintCell == null || !this.authHintCellVisible) ? dp : dp + unconfirmedAuthHintCell.getMeasuredHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMaxScrollYOffset() {
        if (this.hasStories) {
            return AndroidUtilities.dp(81.0f);
        }
        return ActionBar.getCurrentActionBarHeight();
    }

    public boolean isStarsSubscriptionHintVisible() {
        if (this.folderId == 0 && MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("STARS_SUBSCRIPTION_LOW_BALANCE")) {
            StarsController starsController = StarsController.getInstance(this.currentAccount);
            if (!starsController.hasInsufficientSubscriptions()) {
                starsController.loadInsufficientSubscriptions();
                return false;
            }
            long j = -starsController.balance;
            for (int i = 0; i < starsController.insufficientSubscriptions.size(); i++) {
                TLRPC$StarsSubscription tLRPC$StarsSubscription = starsController.insufficientSubscriptions.get(i);
                if (getMessagesController().getChat(Long.valueOf(-DialogObject.getPeerDialogId(tLRPC$StarsSubscription.peer))) != null) {
                    j += tLRPC$StarsSubscription.pricing.amount;
                }
            }
            return j > 0;
        }
        return false;
    }

    public boolean isPremiumRestoreHintVisible() {
        return (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || this.folderId != 0 || !MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_RESTORE") || getUserConfig().isPremium() || MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(false) == null) ? false : true;
    }

    public boolean isPremiumChristmasHintVisible() {
        if (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || this.folderId != 0) {
            return false;
        }
        return MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_CHRISTMAS");
    }

    public boolean isPremiumHintVisible() {
        if (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || this.folderId != 0 || ((!(MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_UPGRADE") && getUserConfig().isPremium()) && (!MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_ANNUAL") || getUserConfig().isPremium())) || (!UserConfig.getInstance(this.currentAccount).isPremium() ? MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(false) != null : !(BuildVars.useInvoiceBilling() || MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(true) == null)))) {
            return false;
        }
        this.isPremiumHintUpgrade = MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_UPGRADE");
        return true;
    }

    private boolean isCacheHintVisible() {
        Long l = this.cacheSize;
        if (l == null || this.deviceSize == null) {
            return false;
        }
        if (((float) l.longValue()) / ((float) this.deviceSize.longValue()) >= 0.3f) {
            return System.currentTimeMillis() > MessagesController.getGlobalMainSettings().getLong("cache_hint_showafter", 0L);
        }
        clearCacheHintVisible();
        return false;
    }

    private void resetCacheHintVisible() {
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        long j = globalMainSettings.getLong("cache_hint_period", 604800000L);
        if (j <= 604800000) {
            j = 2592000000L;
        }
        globalMainSettings.edit().putLong("cache_hint_showafter", System.currentTimeMillis() + j).putLong("cache_hint_period", j).apply();
    }

    private void clearCacheHintVisible() {
        MessagesController.getGlobalMainSettings().edit().remove("cache_hint_showafter").remove("cache_hint_period").apply();
    }

    public void showSelectStatusDialog() {
        int i;
        int i2;
        if (this.selectAnimatedEmojiDialog != null || SharedConfig.appLocked) {
            return;
        }
        if (!this.hasStories || this.dialogStoriesCell.isExpanded()) {
            final SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
            TLRPC$User currentUser = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
            SimpleTextView titleTextView = this.actionBar.getTitleTextView();
            if (titleTextView == null || titleTextView.getRightDrawable() == null) {
                i = 0;
                i2 = 0;
            } else {
                this.statusDrawable.play();
                boolean z = this.statusDrawable.getDrawable() instanceof AnimatedEmojiDrawable;
                Rect rect = AndroidUtilities.rectTmp2;
                rect.set(titleTextView.getRightDrawable().getBounds());
                rect.offset((int) titleTextView.getX(), (int) titleTextView.getY());
                int dp = (-(this.actionBar.getHeight() - rect.centerY())) - AndroidUtilities.dp(16.0f);
                i = rect.centerX() - AndroidUtilities.dp(16.0f);
                DrawerProfileCell.AnimatedStatusView animatedStatusView = this.animatedStatusView;
                if (animatedStatusView != null) {
                    animatedStatusView.translate(rect.centerX(), rect.centerY());
                }
                i2 = dp;
            }
            int i3 = i2;
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = new SelectAnimatedEmojiDialog(this, getContext(), true, Integer.valueOf(i), 0, getResourceProvider()) { // from class: org.telegram.ui.DialogsActivity.30
                @Override // org.telegram.ui.SelectAnimatedEmojiDialog
                protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
                    TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil;
                    if (l == null) {
                        tLRPC$TL_emojiStatusUntil = new TLRPC$TL_emojiStatusEmpty();
                    } else if (num != null) {
                        TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil2 = new TLRPC$TL_emojiStatusUntil();
                        tLRPC$TL_emojiStatusUntil2.document_id = l.longValue();
                        tLRPC$TL_emojiStatusUntil2.until = num.intValue();
                        tLRPC$TL_emojiStatusUntil = tLRPC$TL_emojiStatusUntil2;
                    } else {
                        TLRPC$TL_emojiStatus tLRPC$TL_emojiStatus = new TLRPC$TL_emojiStatus();
                        tLRPC$TL_emojiStatus.document_id = l.longValue();
                        tLRPC$TL_emojiStatusUntil = tLRPC$TL_emojiStatus;
                    }
                    DialogsActivity.this.getMessagesController().updateEmojiStatus(tLRPC$TL_emojiStatusUntil);
                    if (l != null) {
                        DialogsActivity.this.animatedStatusView.animateChange(ReactionsLayoutInBubble.VisibleReaction.fromCustomEmoji(l));
                    }
                    if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                        DialogsActivity.this.selectAnimatedEmojiDialog = null;
                        selectAnimatedEmojiDialogWindowArr[0].dismiss();
                    }
                }
            };
            if (currentUser != null && DialogObject.getEmojiStatusUntil(currentUser.emoji_status) > 0) {
                selectAnimatedEmojiDialog.setExpireDateHint(DialogObject.getEmojiStatusUntil(currentUser.emoji_status));
            }
            selectAnimatedEmojiDialog.setSelected(this.statusDrawable.getDrawable() instanceof AnimatedEmojiDrawable ? Long.valueOf(((AnimatedEmojiDrawable) this.statusDrawable.getDrawable()).getDocumentId()) : null);
            selectAnimatedEmojiDialog.setSaveState(1);
            selectAnimatedEmojiDialog.setScrimDrawable(this.statusDrawable, titleTextView);
            SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, -2, -2) { // from class: org.telegram.ui.DialogsActivity.31
                @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
                public void dismiss() {
                    super.dismiss();
                    DialogsActivity.this.selectAnimatedEmojiDialog = null;
                }
            };
            this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
            selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
            selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(this.actionBar, AndroidUtilities.dp(16.0f), i3, 48);
            selectAnimatedEmojiDialogWindowArr[0].dimBehind();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPremiumBlockedToast(View view, long j) {
        Bulletin createSimpleBulletin;
        int i = -this.shiftDp;
        this.shiftDp = i;
        AndroidUtilities.shakeViewSpring(view, i);
        BotWebViewVibrationEffect.APP_ERROR.vibrate();
        String userName = j >= 0 ? UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j))) : "";
        if (getMessagesController().premiumFeaturesBlocked()) {
            createSimpleBulletin = BulletinFactory.of(this).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, userName)));
        } else {
            createSimpleBulletin = BulletinFactory.of(this).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, userName)), LocaleController.getString(R.string.UserBlockedNonPremiumButton), new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda75
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$showPremiumBlockedToast$23();
                }
            });
        }
        createSimpleBulletin.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$23() {
        if (LaunchActivity.getLastFragment() != null) {
            presentFragment(new PremiumPreviewFragment("noncontacts"));
        }
    }

    private void updateAuthHintCellVisibility(final boolean z) {
        final int i;
        if (this.authHintCellVisible != z) {
            this.authHintCellVisible = z;
            if (this.authHintCell == null) {
                return;
            }
            ValueAnimator valueAnimator = this.authHintCellAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.authHintCellAnimator = null;
            }
            if (z) {
                this.authHintCell.setVisibility(0);
            }
            this.authHintCell.setAlpha(1.0f);
            this.viewPages[0].listView.requestLayout();
            this.fragmentView.requestLayout();
            this.notificationsLocker.lock();
            this.authHintCellAnimating = true;
            float[] fArr = new float[2];
            fArr[0] = this.authHintCellProgress;
            fArr[1] = z ? 1.0f : 0.0f;
            final ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            final int findFirstVisibleItemPosition = this.viewPages[0].layoutManager.findFirstVisibleItemPosition();
            if (findFirstVisibleItemPosition != -1) {
                i = (z ? 0 : -this.authHintCell.getMeasuredHeight()) + this.viewPages[0].layoutManager.findViewByPosition(findFirstVisibleItemPosition).getTop();
            } else {
                i = 0;
            }
            AndroidUtilities.doOnLayout(this.fragmentView, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda101
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$updateAuthHintCellVisibility$26(z, findFirstVisibleItemPosition, i, ofFloat);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAuthHintCellVisibility$26(final boolean z, int i, int i2, ValueAnimator valueAnimator) {
        View findViewByPosition;
        final float measuredHeight = this.authHintCell.getMeasuredHeight();
        if (!z && (findViewByPosition = this.viewPages[0].layoutManager.findViewByPosition(i)) != null) {
            measuredHeight += i2 - findViewByPosition.getTop();
        }
        this.viewPages[0].listView.setTranslationY(this.authHintCellProgress * measuredHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                DialogsActivity.this.lambda$updateAuthHintCellVisibility$25(measuredHeight, valueAnimator2);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.32
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                DialogsActivity.this.notificationsLocker.unlock();
                DialogsActivity.this.authHintCellAnimating = false;
                DialogsActivity.this.authHintCellProgress = z ? 1.0f : 0.0f;
                View view = DialogsActivity.this.fragmentView;
                if (view != null) {
                    view.requestLayout();
                }
                DialogsActivity.this.viewPages[0].listView.requestLayout();
                DialogsActivity.this.viewPages[0].listView.setTranslationY(0.0f);
                if (z) {
                    return;
                }
                DialogsActivity.this.authHintCell.setVisibility(8);
            }
        });
        valueAnimator.setDuration(250L);
        valueAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        valueAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAuthHintCellVisibility$25(float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.authHintCellProgress = floatValue;
        this.viewPages[0].listView.setTranslationY(f * floatValue);
        updateContextViewPosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0420  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0477  */
    /* renamed from: updateDialogsHint */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$updateDialogsHint$29() {
        ApplicationLoader applicationLoader;
        final String str;
        boolean z;
        CharSequence charSequence;
        String formatPluralString;
        if (this.dialogsHintCell == null || this.fragmentView == null || getContext() == null) {
            return;
        }
        DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null) {
            dialogsHintCell.setCompact(false);
            this.dialogsHintCell.setAvatars(this.currentAccount, null);
        }
        if (isInPreviewMode()) {
            this.dialogsHintCellVisible = false;
            this.dialogsHintCell.setVisibility(8);
            updateAuthHintCellVisibility(false);
        } else if (!getMessagesController().getUnconfirmedAuthController().auths.isEmpty() && this.folderId == 0 && this.initialDialogsType == 0) {
            this.dialogsHintCellVisible = false;
            this.dialogsHintCell.setVisibility(8);
            if (this.authHintCell == null) {
                Context context = getContext();
                View view = this.fragmentView;
                UnconfirmedAuthHintCell unconfirmedAuthHintCell = new UnconfirmedAuthHintCell(context, view instanceof SizeNotifierFrameLayout ? (SizeNotifierFrameLayout) view : null);
                this.authHintCell = unconfirmedAuthHintCell;
                ((ContentView) this.fragmentView).addView(unconfirmedAuthHintCell);
            }
            this.authHintCell.set(this, this.currentAccount);
            updateAuthHintCellVisibility(true);
        } else if (this.folderId == 0 && MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_GRACE")) {
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(true);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda29
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$27(view2);
                }
            });
            this.dialogsHintCell.setText(Emoji.replaceWithRestrictedEmoji(LocaleController.getString(R.string.GraceTitle), this.dialogsHintCell.titleView, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda79
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$updateDialogsHint$29();
                }
            }), LocaleController.getString(R.string.GraceMessage));
            this.dialogsHintCell.setOnCloseListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda40
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$28(view2);
                }
            });
            updateAuthHintCellVisibility(false);
        } else if (isStarsSubscriptionHintVisible()) {
            StarsController starsController = StarsController.getInstance(this.currentAccount);
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(true);
            StringBuilder sb = new StringBuilder();
            final long j = 0;
            if (starsController.hasInsufficientSubscriptions()) {
                for (int i = 0; i < starsController.insufficientSubscriptions.size(); i++) {
                    TLRPC$StarsSubscription tLRPC$StarsSubscription = starsController.insufficientSubscriptions.get(i);
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-DialogObject.getPeerDialogId(tLRPC$StarsSubscription.peer)));
                    if (chat != null) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(chat.title);
                        j += tLRPC$StarsSubscription.pricing.amount;
                    }
                }
            }
            final String sb2 = sb.toString();
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda47
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$30(j, sb2, view2);
                }
            });
            DialogsHintCell dialogsHintCell2 = this.dialogsHintCell;
            int i2 = R.string.StarsSubscriptionExpiredHintTitle;
            Object[] objArr = new Object[2];
            long j2 = starsController.balance;
            if (j - j2 > 0) {
                j -= j2;
            }
            objArr[0] = Long.valueOf(j);
            objArr[1] = sb2;
            dialogsHintCell2.setText(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatString(i2, objArr), 0.72f), LocaleController.getString(R.string.StarsSubscriptionExpiredHintText));
            this.dialogsHintCell.setOnCloseListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda34
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$31(view2);
                }
            });
            updateAuthHintCellVisibility(false);
        } else if (this.folderId == 0 && !getMessagesController().premiumPurchaseBlocked() && BirthdayController.getInstance(this.currentAccount).contains() && !getMessagesController().dismissedSuggestions.contains("BIRTHDAY_CONTACTS_TODAY")) {
            final BirthdayController.BirthdayState state = BirthdayController.getInstance(this.currentAccount).getState();
            ArrayList<TLRPC$User> arrayList = state.today;
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(true);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda25
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    UserSelectorBottomSheet.open(0L, BirthdayController.BirthdayState.this);
                }
            });
            this.dialogsHintCell.setAvatars(this.currentAccount, arrayList);
            DialogsHintCell dialogsHintCell3 = this.dialogsHintCell;
            if (arrayList.size() == 1) {
                formatPluralString = LocaleController.formatString(R.string.BirthdayTodaySingleTitle, UserObject.getForcedFirstName(arrayList.get(0)));
            } else {
                formatPluralString = LocaleController.formatPluralString("BirthdayTodayMultipleTitle", arrayList.size(), new Object[0]);
            }
            dialogsHintCell3.setText(Emoji.replaceWithRestrictedEmoji(AndroidUtilities.replaceSingleTag(formatPluralString, Theme.key_windowBackgroundWhiteValueText, 2, null), this.dialogsHintCell.titleView, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda79
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$updateDialogsHint$29();
                }
            }), LocaleController.formatString(arrayList.size() == 1 ? R.string.BirthdayTodaySingleMessage : R.string.BirthdayTodayMultipleMessage, new Object[0]));
            this.dialogsHintCell.setOnCloseListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda30
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$33(view2);
                }
            });
            updateAuthHintCellVisibility(false);
        } else if (this.folderId == 0 && MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("BIRTHDAY_SETUP") && getMessagesController().getUserFull(getUserConfig().getClientUserId()) != null && getMessagesController().getUserFull(getUserConfig().getClientUserId()).birthday == null) {
            ContactsController.getInstance(this.currentAccount).loadPrivacySettings();
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(true);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda42
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$38(view2);
                }
            });
            this.dialogsHintCell.setText(Emoji.replaceWithRestrictedEmoji(LocaleController.getString(R.string.BirthdaySetupTitle), this.dialogsHintCell.titleView, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda79
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$updateDialogsHint$29();
                }
            }), LocaleController.formatString(R.string.BirthdaySetupMessage, new Object[0]));
            this.dialogsHintCell.setOnCloseListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda31
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$40(view2);
                }
            });
            updateAuthHintCellVisibility(false);
        } else if (isPremiumChristmasHintVisible()) {
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(false);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda55
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    UserSelectorBottomSheet.open();
                }
            });
            this.dialogsHintCell.setText(Emoji.replaceEmoji(AndroidUtilities.replaceSingleTag(LocaleController.getString("GiftPremiumEventAdsTitle", R.string.GiftPremiumEventAdsTitle), Theme.key_windowBackgroundWhiteValueText, 2, null), null, false), LocaleController.formatString("BoostingPremiumChristmasSubTitle", R.string.BoostingPremiumChristmasSubTitle, new Object[0]));
            this.dialogsHintCell.setOnCloseListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda27
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$42(view2);
                }
            });
            updateAuthHintCellVisibility(false);
        } else if (isPremiumRestoreHintVisible()) {
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(false);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda44
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$44(view2);
                }
            });
            this.dialogsHintCell.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatString(R.string.RestorePremiumHintTitle, MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(false)), Theme.key_windowBackgroundWhiteValueText, 2, null), LocaleController.getString(R.string.RestorePremiumHintMessage));
            updateAuthHintCellVisibility(false);
        } else if (isPremiumHintVisible()) {
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(false);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda41
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$46(view2);
                }
            });
            this.dialogsHintCell.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatString(this.isPremiumHintUpgrade ? R.string.SaveOnAnnualPremiumTitle : R.string.UpgradePremiumTitle, MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(false)), Theme.key_windowBackgroundWhiteValueText, 2, null), LocaleController.getString(this.isPremiumHintUpgrade ? R.string.UpgradePremiumMessage : R.string.SaveOnAnnualPremiumMessage));
            updateAuthHintCellVisibility(false);
        } else if (isCacheHintVisible()) {
            this.dialogsHintCellVisible = true;
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setCompact(false);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda39
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$updateDialogsHint$48(view2);
                }
            });
            this.dialogsHintCell.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatString(R.string.ClearStorageHintTitle, AndroidUtilities.formatFileSize(this.cacheSize.longValue())), Theme.key_windowBackgroundWhiteValueText, 2, null), LocaleController.getString(R.string.ClearStorageHintMessage));
            updateAuthHintCellVisibility(false);
        } else if (this.folderId == 0 && (applicationLoader = ApplicationLoader.applicationLoaderInstance) != null) {
            CharSequence[] charSequenceArr = new CharSequence[2];
            boolean[] zArr = new boolean[1];
            if (!applicationLoader.onSuggestionFill(null, charSequenceArr, zArr)) {
                Iterator<String> it = MessagesController.getInstance(this.currentAccount).pendingSuggestions.iterator();
                while (it.hasNext()) {
                    str = it.next();
                    if (ApplicationLoader.applicationLoaderInstance.onSuggestionFill(str, charSequenceArr, zArr)) {
                    }
                }
                str = null;
                z = false;
                if (!z) {
                    this.dialogsHintCellVisible = true;
                    this.dialogsHintCell.setVisibility(0);
                    this.dialogsHintCell.setCompact(false);
                    this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda24
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            DialogsActivity.lambda$updateDialogsHint$49(str, view2);
                        }
                    });
                    DialogsHintCell dialogsHintCell4 = this.dialogsHintCell;
                    if (charSequenceArr[0] instanceof String) {
                        charSequence = AndroidUtilities.replaceSingleTag(charSequenceArr[0].toString(), Theme.key_windowBackgroundWhiteValueText, 2, null);
                    } else {
                        charSequence = charSequenceArr[0];
                    }
                    dialogsHintCell4.setText(charSequence, charSequenceArr[1] instanceof String ? AndroidUtilities.replaceTags(charSequenceArr[1].toString()) : charSequenceArr[1]);
                    if (zArr[0] && str != null) {
                        this.dialogsHintCell.setOnCloseListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda48
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                DialogsActivity.this.lambda$updateDialogsHint$51(str, view2);
                            }
                        });
                    }
                    updateAuthHintCellVisibility(false);
                    return;
                }
                this.dialogsHintCellVisible = false;
                this.dialogsHintCell.setVisibility(8);
                updateAuthHintCellVisibility(false);
                return;
            }
            str = null;
            z = true;
            if (!z) {
            }
        } else {
            this.dialogsHintCellVisible = false;
            this.dialogsHintCell.setVisibility(8);
            updateAuthHintCellVisibility(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$27(View view) {
        Browser.openUrl(getContext(), getMessagesController().premiumManageSubscriptionUrl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$28(View view) {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, "PREMIUM_GRACE");
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200L);
        TransitionManager.beginDelayedTransition((ViewGroup) this.dialogsHintCell.getParent(), changeBounds);
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$30(long j, String str, View view) {
        new StarsIntroActivity.StarsNeededSheet(getContext(), getResourceProvider(), j, 2, str, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda77
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$29();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$31(View view) {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, "STARS_SUBSCRIPTION_LOW_BALANCE");
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200L);
        TransitionManager.beginDelayedTransition((ViewGroup) this.dialogsHintCell.getParent(), changeBounds);
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$33(View view) {
        BirthdayController.getInstance(this.currentAccount).hide();
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, "BIRTHDAY_CONTACTS_TODAY");
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200L);
        TransitionManager.beginDelayedTransition((ViewGroup) this.dialogsHintCell.getParent(), changeBounds);
        lambda$updateDialogsHint$29();
        BulletinFactory.of(this).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString("BoostingPremiumChristmasToast", R.string.BoostingPremiumChristmasToast), 4).setDuration(5000).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$38(View view) {
        showDialog(AlertsCreator.createBirthdayPickerDialog(getContext(), LocaleController.getString(R.string.EditProfileBirthdayTitle), LocaleController.getString(R.string.EditProfileBirthdayButton), null, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda118
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$updateDialogsHint$36((TLRPC$TL_birthday) obj);
            }
        }, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$37();
            }
        }, getResourceProvider()).create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$36(TLRPC$TL_birthday tLRPC$TL_birthday) {
        TLRPC$TL_account_updateBirthday tLRPC$TL_account_updateBirthday = new TLRPC$TL_account_updateBirthday();
        tLRPC$TL_account_updateBirthday.flags |= 1;
        tLRPC$TL_account_updateBirthday.birthday = tLRPC$TL_birthday;
        final TLRPC$UserFull userFull = getMessagesController().getUserFull(getUserConfig().getClientUserId());
        final TLRPC$TL_birthday tLRPC$TL_birthday2 = userFull != null ? userFull.birthday : null;
        if (userFull != null) {
            userFull.flags2 |= 32;
            userFull.birthday = tLRPC$TL_birthday;
        }
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateBirthday, new RequestDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda124
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                DialogsActivity.this.lambda$updateDialogsHint$35(userFull, tLRPC$TL_birthday2, tLObject, tLRPC$TL_error);
            }
        }, 1024);
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, "BIRTHDAY_SETUP");
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$35(final TLRPC$UserFull tLRPC$UserFull, final TLRPC$TL_birthday tLRPC$TL_birthday, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda96
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$34(tLObject, tLRPC$UserFull, tLRPC$TL_birthday, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$34(TLObject tLObject, TLRPC$UserFull tLRPC$UserFull, TLRPC$TL_birthday tLRPC$TL_birthday, TLRPC$TL_error tLRPC$TL_error) {
        String str;
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            BulletinFactory.of(this).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.PrivacyBirthdaySetDone)).setDuration(5000).show();
            return;
        }
        if (tLRPC$UserFull != null) {
            if (tLRPC$TL_birthday == null) {
                tLRPC$UserFull.flags2 &= -33;
            } else {
                tLRPC$UserFull.flags2 |= 32;
            }
            tLRPC$UserFull.birthday = tLRPC$TL_birthday;
            getMessagesStorage().updateUserInfo(tLRPC$UserFull, false);
        }
        if (tLRPC$TL_error != null && (str = tLRPC$TL_error.text) != null && str.startsWith("FLOOD_WAIT_")) {
            if (getContext() != null) {
                showDialog(new AlertDialog.Builder(getContext(), this.resourceProvider).setTitle(LocaleController.getString(R.string.PrivacyBirthdayTooOftenTitle)).setMessage(LocaleController.getString(R.string.PrivacyBirthdayTooOftenMessage)).setPositiveButton(LocaleController.getString(R.string.OK), null).create());
                return;
            }
            return;
        }
        BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.UnknownError)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$37() {
        BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
        bottomSheetParams.transitionFromLeft = true;
        bottomSheetParams.allowNestedScroll = false;
        showAsSheet(new PrivacyControlActivity(11), bottomSheetParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$40(View view) {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, "BIRTHDAY_SETUP");
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200L);
        TransitionManager.beginDelayedTransition((ViewGroup) this.dialogsHintCell.getParent(), changeBounds);
        lambda$updateDialogsHint$29();
        BulletinFactory.of(this).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString(R.string.BirthdaySetupLater), LocaleController.getString(R.string.Settings), new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda81
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$39();
            }
        }).setDuration(5000).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$39() {
        presentFragment(new UserInfoActivity());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$42(View view) {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, "PREMIUM_CHRISTMAS");
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200L);
        TransitionManager.beginDelayedTransition((ViewGroup) this.dialogsHintCell.getParent(), changeBounds);
        lambda$updateDialogsHint$29();
        BulletinFactory.of(this).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString("BoostingPremiumChristmasToast", R.string.BoostingPremiumChristmasToast), 4).setDuration(5000).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$44(View view) {
        presentFragment(new PremiumPreviewFragment("dialogs_hint").setSelectAnnualByDefault());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$43();
            }
        }, 250L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$43() {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, "PREMIUM_RESTORE");
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$46(View view) {
        presentFragment(new PremiumPreviewFragment("dialogs_hint").setSelectAnnualByDefault());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda68
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$45();
            }
        }, 250L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$45() {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, this.isPremiumHintUpgrade ? "PREMIUM_UPGRADE" : "PREMIUM_ANNUAL");
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$48(View view) {
        presentFragment(new CacheControlActivity());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$47();
            }
        }, 250L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$47() {
        resetCacheHintVisible();
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateDialogsHint$49(String str, View view) {
        ApplicationLoader applicationLoader = ApplicationLoader.applicationLoaderInstance;
        if (applicationLoader != null) {
            applicationLoader.onSuggestionClick(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$51(final String str, View view) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda93
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$50(str);
            }
        }, 250L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$50(String str) {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, str);
        lambda$updateDialogsHint$29();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createGroupForThis() {
        final AlertDialog alertDialog = new AlertDialog(getContext(), 3);
        TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
        if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) {
            Bundle bundle = new Bundle();
            bundle.putInt("step", 0);
            Boolean bool = this.requestPeerType.has_username;
            if (bool != null) {
                bundle.putBoolean("forcePublic", bool.booleanValue());
            }
            final ChannelCreateActivity channelCreateActivity = new ChannelCreateActivity(bundle);
            channelCreateActivity.setOnFinishListener(new Utilities.Callback2() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda114
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    DialogsActivity.this.lambda$createGroupForThis$61(channelCreateActivity, alertDialog, (BaseFragment) obj, (Long) obj2);
                }
            });
            presentFragment(channelCreateActivity);
        } else if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeChat) {
            Bundle bundle2 = new Bundle();
            Boolean bool2 = this.requestPeerType.bot_participant;
            bundle2.putLongArray("result", (bool2 == null || !bool2.booleanValue()) ? new long[]{getUserConfig().getClientUserId()} : new long[]{getUserConfig().getClientUserId(), this.requestPeerBotId});
            Boolean bool3 = this.requestPeerType.forum;
            bundle2.putInt("chatType", (bool3 == null || !bool3.booleanValue()) ? 4 : 5);
            bundle2.putBoolean("canToggleTopics", false);
            GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle2);
            groupCreateFinalActivity.setDelegate(new 33(alertDialog));
            presentFragment(groupCreateFinalActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$61(final ChannelCreateActivity channelCreateActivity, final AlertDialog alertDialog, final BaseFragment baseFragment, final Long l) {
        Utilities.doCallbacks(new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda121
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$53(l, channelCreateActivity, baseFragment, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda122
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$55(alertDialog, l, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda120
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$57(l, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda119
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$59(l, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda123
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$60(alertDialog, l, channelCreateActivity, baseFragment, (Runnable) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$53(Long l, final ChannelCreateActivity channelCreateActivity, final BaseFragment baseFragment, Runnable runnable) {
        showSendToBotAlert(getMessagesController().getChat(l), runnable, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda99
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$createGroupForThis$52(channelCreateActivity, baseFragment);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$52(ChannelCreateActivity channelCreateActivity, BaseFragment baseFragment) {
        removeSelfFromStack();
        channelCreateActivity.removeSelfFromStack();
        baseFragment.finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$55(AlertDialog alertDialog, Long l, final Runnable runnable) {
        alertDialog.showDelayed(150L);
        Boolean bool = this.requestPeerType.bot_participant;
        if (bool != null && bool.booleanValue()) {
            getMessagesController().addUserToChat(l.longValue(), getMessagesController().getUser(Long.valueOf(this.requestPeerBotId)), 0, null, this, false, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda109
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean lambda$createGroupForThis$54;
                    lambda$createGroupForThis$54 = DialogsActivity.lambda$createGroupForThis$54(runnable, tLRPC$TL_error);
                    return lambda$createGroupForThis$54;
                }
            });
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createGroupForThis$54(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
        runnable.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$57(Long l, final Runnable runnable) {
        if (this.requestPeerType.bot_admin_rights != null) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.requestPeerBotId));
            MessagesController messagesController = getMessagesController();
            long longValue = l.longValue();
            TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$RequestPeerType.bot_admin_rights;
            Boolean bool = tLRPC$RequestPeerType.bot_participant;
            messagesController.setUserAdminRole(longValue, user, tLRPC$TL_chatAdminRights, null, false, this, bool == null || !bool.booleanValue(), true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda106
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean lambda$createGroupForThis$56;
                    lambda$createGroupForThis$56 = DialogsActivity.lambda$createGroupForThis$56(runnable, tLRPC$TL_error);
                    return lambda$createGroupForThis$56;
                }
            });
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createGroupForThis$56(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
        runnable.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$59(Long l, final Runnable runnable) {
        if (this.requestPeerType.user_admin_rights != null) {
            getMessagesController().setUserAdminRole(l.longValue(), getAccountInstance().getUserConfig().getCurrentUser(), ChatRightsEditActivity.rightsOR(getMessagesController().getChat(l).admin_rights, this.requestPeerType.user_admin_rights), null, true, this, false, true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda108
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean lambda$createGroupForThis$58;
                    lambda$createGroupForThis$58 = DialogsActivity.lambda$createGroupForThis$58(runnable, tLRPC$TL_error);
                    return lambda$createGroupForThis$58;
                }
            });
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createGroupForThis$58(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
        runnable.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$60(AlertDialog alertDialog, Long l, ChannelCreateActivity channelCreateActivity, BaseFragment baseFragment, Runnable runnable) {
        alertDialog.dismiss();
        getMessagesController().loadChannelParticipants(l);
        DialogsActivityDelegate dialogsActivityDelegate = this.delegate;
        removeSelfFromStack();
        channelCreateActivity.removeSelfFromStack();
        baseFragment.finishFragment();
        if (dialogsActivityDelegate != null) {
            ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
            arrayList.add(MessagesStorage.TopicKey.of(-l.longValue(), 0L));
            dialogsActivityDelegate.didSelectDialogs(this, arrayList, null, false, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 33 implements GroupCreateFinalActivity.GroupCreateFinalActivityDelegate {
        final /* synthetic */ AlertDialog val$progress;

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didFailChatCreation() {
        }

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didStartChatCreation() {
        }

        33(AlertDialog alertDialog) {
            this.val$progress = alertDialog;
        }

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didFinishChatCreation(GroupCreateFinalActivity groupCreateFinalActivity, final long j) {
            final BaseFragment[] baseFragmentArr = {groupCreateFinalActivity, null};
            final AlertDialog alertDialog = this.val$progress;
            final AlertDialog alertDialog2 = this.val$progress;
            Utilities.doCallbacks(new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda7
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.33.this.lambda$didFinishChatCreation$1(j, baseFragmentArr, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda8
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.33.this.lambda$didFinishChatCreation$3(j, baseFragmentArr, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda9
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.33.this.lambda$didFinishChatCreation$5(alertDialog, j, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.33.this.lambda$didFinishChatCreation$7(j, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda5
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.33.this.lambda$didFinishChatCreation$9(j, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda10
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.33.this.lambda$didFinishChatCreation$10(alertDialog2, j, baseFragmentArr, (Runnable) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$1(long j, BaseFragment[] baseFragmentArr, final Runnable runnable) {
            if (DialogsActivity.this.requestPeerType.has_username != null && DialogsActivity.this.requestPeerType.has_username.booleanValue()) {
                Bundle bundle = new Bundle();
                bundle.putInt("step", 1);
                bundle.putLong("chat_id", j);
                bundle.putBoolean("forcePublic", DialogsActivity.this.requestPeerType.has_username.booleanValue());
                ChannelCreateActivity channelCreateActivity = new ChannelCreateActivity(bundle);
                channelCreateActivity.setOnFinishListener(new Utilities.Callback2() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda4
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        BaseFragment baseFragment = (BaseFragment) obj;
                        Long l = (Long) obj2;
                        runnable.run();
                    }
                });
                DialogsActivity.this.presentFragment(channelCreateActivity);
                baseFragmentArr[1] = channelCreateActivity;
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$3(long j, final BaseFragment[] baseFragmentArr, Runnable runnable) {
            DialogsActivity.this.showSendToBotAlert(DialogsActivity.this.getMessagesController().getChat(Long.valueOf(j)), runnable, new Runnable() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.33.this.lambda$didFinishChatCreation$2(baseFragmentArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$2(BaseFragment[] baseFragmentArr) {
            DialogsActivity.this.removeSelfFromStack();
            if (baseFragmentArr[1] != null) {
                baseFragmentArr[0].removeSelfFromStack();
                baseFragmentArr[1].finishFragment();
                return;
            }
            baseFragmentArr[0].finishFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$5(AlertDialog alertDialog, long j, final Runnable runnable) {
            alertDialog.showDelayed(150L);
            if (DialogsActivity.this.requestPeerType.bot_participant != null && DialogsActivity.this.requestPeerType.bot_participant.booleanValue()) {
                DialogsActivity.this.getMessagesController().addUserToChat(j, DialogsActivity.this.getMessagesController().getUser(Long.valueOf(DialogsActivity.this.requestPeerBotId)), 0, null, DialogsActivity.this, false, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                        boolean lambda$didFinishChatCreation$4;
                        lambda$didFinishChatCreation$4 = DialogsActivity.33.lambda$didFinishChatCreation$4(runnable, tLRPC$TL_error);
                        return lambda$didFinishChatCreation$4;
                    }
                });
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$didFinishChatCreation$4(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
            runnable.run();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$7(long j, final Runnable runnable) {
            if (DialogsActivity.this.requestPeerType.bot_admin_rights != null) {
                TLRPC$User user = DialogsActivity.this.getMessagesController().getUser(Long.valueOf(DialogsActivity.this.requestPeerBotId));
                MessagesController messagesController = DialogsActivity.this.getMessagesController();
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = DialogsActivity.this.requestPeerType.bot_admin_rights;
                DialogsActivity dialogsActivity = DialogsActivity.this;
                messagesController.setUserAdminRole(j, user, tLRPC$TL_chatAdminRights, null, false, dialogsActivity, dialogsActivity.requestPeerType.bot_participant == null || !DialogsActivity.this.requestPeerType.bot_participant.booleanValue(), true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                        boolean lambda$didFinishChatCreation$6;
                        lambda$didFinishChatCreation$6 = DialogsActivity.33.lambda$didFinishChatCreation$6(runnable, tLRPC$TL_error);
                        return lambda$didFinishChatCreation$6;
                    }
                });
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$didFinishChatCreation$6(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
            runnable.run();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$9(long j, final Runnable runnable) {
            if (DialogsActivity.this.requestPeerType.user_admin_rights != null) {
                DialogsActivity.this.getMessagesController().setUserAdminRole(j, DialogsActivity.this.getAccountInstance().getUserConfig().getCurrentUser(), ChatRightsEditActivity.rightsOR(DialogsActivity.this.getMessagesController().getChat(Long.valueOf(j)).admin_rights, DialogsActivity.this.requestPeerType.user_admin_rights), null, false, DialogsActivity.this, false, true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$33$$ExternalSyntheticLambda3
                    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                        boolean lambda$didFinishChatCreation$8;
                        lambda$didFinishChatCreation$8 = DialogsActivity.33.lambda$didFinishChatCreation$8(runnable, tLRPC$TL_error);
                        return lambda$didFinishChatCreation$8;
                    }
                });
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$didFinishChatCreation$8(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
            runnable.run();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$10(AlertDialog alertDialog, long j, BaseFragment[] baseFragmentArr, Runnable runnable) {
            alertDialog.dismiss();
            DialogsActivity.this.getMessagesController().loadChannelParticipants(Long.valueOf(j));
            DialogsActivityDelegate dialogsActivityDelegate = DialogsActivity.this.delegate;
            DialogsActivity.this.removeSelfFromStack();
            if (baseFragmentArr[1] != null) {
                baseFragmentArr[0].removeSelfFromStack();
                baseFragmentArr[1].finishFragment();
            } else {
                baseFragmentArr[0].finishFragment();
            }
            if (dialogsActivityDelegate != null) {
                ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                arrayList.add(MessagesStorage.TopicKey.of(-j, 0L));
                dialogsActivityDelegate.didSelectDialogs(DialogsActivity.this, arrayList, null, false, null);
            }
        }
    }

    private void updateAppUpdateViews(boolean z) {
        boolean z2;
        if (this.updateLayout == null) {
            return;
        }
        if (SharedConfig.isAppUpdateAvailable()) {
            FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            z2 = getFileLoader().getPathToAttach(SharedConfig.pendingAppUpdate.document, true).exists();
        } else {
            z2 = false;
        }
        if (z2) {
            if (this.updateLayout.getTag() != null) {
                return;
            }
            AnimatorSet animatorSet = this.updateLayoutAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.updateLayout.setVisibility(0);
            this.updateLayout.setTag(1);
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.updateLayoutAnimator = animatorSet2;
                animatorSet2.setDuration(180L);
                this.updateLayoutAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                this.updateLayoutAnimator.playTogether(ObjectAnimator.ofFloat(this.updateLayout, View.TRANSLATION_Y, 0.0f));
                this.updateLayoutAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.34
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogsActivity.this.updateLayoutAnimator = null;
                    }
                });
                this.updateLayoutAnimator.start();
                return;
            }
            this.updateLayout.setTranslationY(0.0f);
        } else if (this.updateLayout.getTag() == null) {
        } else {
            this.updateLayout.setTag(null);
            if (z) {
                AnimatorSet animatorSet3 = new AnimatorSet();
                this.updateLayoutAnimator = animatorSet3;
                animatorSet3.setDuration(180L);
                this.updateLayoutAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                this.updateLayoutAnimator.playTogether(ObjectAnimator.ofFloat(this.updateLayout, View.TRANSLATION_Y, AndroidUtilities.dp(48.0f)));
                this.updateLayoutAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.35
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (DialogsActivity.this.updateLayout.getTag() == null) {
                            DialogsActivity.this.updateLayout.setVisibility(4);
                        }
                        DialogsActivity.this.updateLayoutAnimator = null;
                    }
                });
                this.updateLayoutAnimator.start();
                return;
            }
            this.updateLayout.setTranslationY(AndroidUtilities.dp(48.0f));
            this.updateLayout.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateContextViewPosition() {
        float f;
        FilterTabsView filterTabsView = this.filterTabsView;
        float f2 = 0.0f;
        float measuredHeight = (filterTabsView == null || filterTabsView.getVisibility() == 8) ? 0.0f : this.filterTabsView.getMeasuredHeight();
        ViewPagerFixed.TabsView tabsView = this.searchTabsView;
        float measuredHeight2 = (tabsView == null || tabsView.getVisibility() == 8) ? 0.0f : this.searchTabsView.getMeasuredHeight();
        float dp = this.hasStories ? AndroidUtilities.dp(81.0f) : 0.0f;
        if (this.hasStories) {
            float f3 = this.scrollYOffset;
            float f4 = this.searchAnimationProgress;
            f = (f3 * (1.0f - f4)) + (dp * (1.0f - f4)) + (measuredHeight * (1.0f - f4)) + (measuredHeight2 * f4) + this.tabsYOffset;
        } else {
            float f5 = this.scrollYOffset;
            float f6 = this.searchAnimationProgress;
            f = f5 + (measuredHeight * (1.0f - f6)) + (measuredHeight2 * f6) + this.tabsYOffset;
        }
        float f7 = f + this.storiesOverscroll;
        DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null && dialogsHintCell.getVisibility() == 0) {
            RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
                f7 -= this.dialogsHintCell.getMeasuredHeight() * this.rightSlidingDialogContainer.openedProgress;
            }
            this.dialogsHintCell.setTranslationY(f7);
            f7 += this.dialogsHintCell.getMeasuredHeight() * (1.0f - this.searchAnimationProgress);
        }
        UnconfirmedAuthHintCell unconfirmedAuthHintCell = this.authHintCell;
        if (unconfirmedAuthHintCell != null && unconfirmedAuthHintCell.getVisibility() == 0) {
            RightSlidingDialogContainer rightSlidingDialogContainer2 = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer2 != null && rightSlidingDialogContainer2.hasFragment()) {
                f7 -= this.authHintCell.getMeasuredHeight() * this.rightSlidingDialogContainer.openedProgress;
            }
            float measuredHeight3 = this.authHintCell.getMeasuredHeight() * (1.0f - this.authHintCellProgress);
            this.authHintCell.setTranslationY((-measuredHeight3) + f7);
            f7 += this.authHintCell.getMeasuredHeight() - measuredHeight3;
        }
        if (this.fragmentContextView != null) {
            FragmentContextView fragmentContextView = this.fragmentLocationContextView;
            float dp2 = (fragmentContextView == null || fragmentContextView.getVisibility() != 0) ? 0.0f : AndroidUtilities.dp(36.0f) + 0.0f;
            FragmentContextView fragmentContextView2 = this.fragmentContextView;
            fragmentContextView2.setTranslationY(dp2 + fragmentContextView2.getTopPadding() + f7);
        }
        if (this.fragmentLocationContextView != null) {
            FragmentContextView fragmentContextView3 = this.fragmentContextView;
            if (fragmentContextView3 != null && fragmentContextView3.getVisibility() == 0) {
                f2 = 0.0f + AndroidUtilities.dp(this.fragmentContextView.getStyleHeight()) + this.fragmentContextView.getTopPadding();
            }
            FragmentContextView fragmentContextView4 = this.fragmentLocationContextView;
            fragmentContextView4.setTranslationY(f2 + fragmentContextView4.getTopPadding() + f7);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateFiltersView(boolean z, ArrayList<Object> arrayList, ArrayList<FiltersView.DateData> arrayList2, boolean z2, boolean z3) {
        SearchViewPager searchViewPager;
        boolean z4;
        ViewPagerFixed.TabsView tabsView;
        if (!this.searchIsShowed || this.onlySelect || (searchViewPager = this.searchViewPager) == null) {
            return;
        }
        ArrayList<FiltersView.MediaFilterData> currentSearchFilters = searchViewPager.getCurrentSearchFilters();
        boolean z5 = false;
        boolean z6 = false;
        boolean z7 = false;
        boolean z8 = false;
        for (int i = 0; i < currentSearchFilters.size(); i++) {
            if (currentSearchFilters.get(i).isMedia()) {
                z6 = true;
            } else if (currentSearchFilters.get(i).filterType == 4) {
                z7 = true;
            } else if (currentSearchFilters.get(i).filterType == 6) {
                z8 = true;
            } else if (currentSearchFilters.get(i).filterType == 7) {
                z5 = true;
            }
        }
        if (z5) {
            z2 = false;
        }
        boolean z9 = !(arrayList == null || arrayList.isEmpty()) || !(arrayList2 == null || arrayList2.isEmpty()) || z2;
        if ((z6 || z9 || !z) && z9) {
            arrayList = (arrayList == null || arrayList.isEmpty() || z7) ? null : null;
            arrayList2 = (arrayList2 == null || arrayList2.isEmpty() || z8) ? null : null;
            if (arrayList != null || arrayList2 != null || z2) {
                this.filtersView.setUsersAndDates(arrayList, arrayList2, z2);
                z4 = true;
                if (!z4) {
                    this.filtersView.setUsersAndDates(null, null, false);
                }
                if (!z3) {
                    this.filtersView.getAdapter().notifyDataSetChanged();
                }
                tabsView = this.searchTabsView;
                if (tabsView != null) {
                    tabsView.hide(z4, true);
                }
                this.filtersView.setEnabled(z4);
                this.filtersView.setVisibility(0);
            }
        }
        z4 = false;
        if (!z4) {
        }
        if (!z3) {
        }
        tabsView = this.searchTabsView;
        if (tabsView != null) {
        }
        this.filtersView.setEnabled(z4);
        this.filtersView.setVisibility(0);
    }

    private void addSearchFilter(FiltersView.MediaFilterData mediaFilterData) {
        SearchViewPager searchViewPager;
        if (!this.searchIsShowed || (searchViewPager = this.searchViewPager) == null) {
            return;
        }
        ArrayList<FiltersView.MediaFilterData> currentSearchFilters = searchViewPager.getCurrentSearchFilters();
        if (!currentSearchFilters.isEmpty()) {
            for (int i = 0; i < currentSearchFilters.size(); i++) {
                if (mediaFilterData.isSameType(currentSearchFilters.get(i))) {
                    return;
                }
            }
        }
        currentSearchFilters.add(mediaFilterData);
        this.actionBar.setSearchFilter(mediaFilterData);
        this.actionBar.setSearchFieldText("");
        updateFiltersView(true, null, null, false, true);
    }

    public void updateSpeedItem(boolean z) {
        boolean z2;
        if (this.speedItem == null) {
            return;
        }
        Iterator<MessageObject> it = getDownloadController().downloadingFiles.iterator();
        while (true) {
            if (!it.hasNext()) {
                z2 = false;
                break;
            }
            MessageObject next = it.next();
            if (next.getDocument() != null && next.getDocument().size >= 157286400) {
                z2 = true;
                break;
            }
        }
        Iterator<MessageObject> it2 = getDownloadController().recentDownloadingFiles.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            MessageObject next2 = it2.next();
            if (next2.getDocument() != null && next2.getDocument().size >= 157286400) {
                z2 = true;
                break;
            }
        }
        final boolean z3 = !getUserConfig().isPremium() && !getMessagesController().premiumFeaturesBlocked() && z2 && z;
        if (z3 != (this.speedItem.getTag() != null)) {
            this.speedItem.setTag(z3 ? Boolean.TRUE : null);
            this.speedItem.setClickable(z3);
            AnimatorSet animatorSet = this.speedAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.setDuration(180L);
            Animator[] animatorArr = new Animator[3];
            ActionBarMenuItem actionBarMenuItem = this.speedItem;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z3 ? 1.0f : 0.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(actionBarMenuItem, property, fArr);
            ActionBarMenuItem actionBarMenuItem2 = this.speedItem;
            Property property2 = View.SCALE_X;
            float[] fArr2 = new float[1];
            fArr2[0] = z3 ? 1.0f : 0.5f;
            animatorArr[1] = ObjectAnimator.ofFloat(actionBarMenuItem2, property2, fArr2);
            ActionBarMenuItem actionBarMenuItem3 = this.speedItem;
            Property property3 = View.SCALE_Y;
            float[] fArr3 = new float[1];
            fArr3[0] = z3 ? 1.0f : 0.5f;
            animatorArr[2] = ObjectAnimator.ofFloat(actionBarMenuItem3, property3, fArr3);
            animatorSet2.playTogether(animatorArr);
            animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.36
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    int i = Build.VERSION.SDK_INT;
                    if (i >= 21) {
                        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) DialogsActivity.this.speedItem.getIconView().getDrawable();
                        if (!z3) {
                            if (i >= 23) {
                                animatedVectorDrawable.reset();
                                return;
                            } else {
                                animatedVectorDrawable.setVisible(false, true);
                                return;
                            }
                        }
                        animatedVectorDrawable.start();
                        if (SharedConfig.getDevicePerformanceClass() != 0) {
                            TLRPC$TL_help_premiumPromo premiumPromo = MediaDataController.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getPremiumPromo();
                            String featureTypeToServerString = PremiumPreviewFragment.featureTypeToServerString(2);
                            if (premiumPromo != null) {
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= premiumPromo.video_sections.size()) {
                                        i2 = -1;
                                        break;
                                    } else if (premiumPromo.video_sections.get(i2).equals(featureTypeToServerString)) {
                                        break;
                                    } else {
                                        i2++;
                                    }
                                }
                                if (i2 != -1) {
                                    FileLoader.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).loadFile(premiumPromo.videos.get(i2), premiumPromo, 3, 0);
                                }
                            }
                        }
                    }
                }
            });
            animatorSet2.start();
            this.speedAnimator = animatorSet2;
        }
    }

    private void createActionMode(String str) {
        if (this.actionBar.actionModeIsExist(str)) {
            return;
        }
        ActionBarMenu createActionMode = this.actionBar.createActionMode(false, str);
        createActionMode.setBackgroundColor(0);
        createActionMode.drawBlur = false;
        NumberTextView numberTextView = new NumberTextView(createActionMode.getContext());
        this.selectedDialogsCountTextView = numberTextView;
        numberTextView.setTextSize(18);
        this.selectedDialogsCountTextView.setTypeface(AndroidUtilities.bold());
        this.selectedDialogsCountTextView.setTextColor(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon));
        createActionMode.addView(this.selectedDialogsCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
        this.selectedDialogsCountTextView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda58
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createActionMode$62;
                lambda$createActionMode$62 = DialogsActivity.lambda$createActionMode$62(view, motionEvent);
                return lambda$createActionMode$62;
            }
        });
        int i = R.drawable.msg_pin;
        this.pinItem = createActionMode.addItemWithWidth(100, i, AndroidUtilities.dp(54.0f));
        this.muteItem = createActionMode.addItemWithWidth(R.styleable.AppCompatTheme_textAppearanceListItemSecondary, R.drawable.msg_mute, AndroidUtilities.dp(54.0f));
        int i2 = R.drawable.msg_archive;
        this.archive2Item = createActionMode.addItemWithWidth(R.styleable.AppCompatTheme_textAppearanceSearchResultSubtitle, i2, AndroidUtilities.dp(54.0f));
        this.deleteItem = createActionMode.addItemWithWidth(R.styleable.AppCompatTheme_textAppearanceLargePopupMenu, R.drawable.msg_delete, AndroidUtilities.dp(54.0f), LocaleController.getString("Delete", R.string.Delete));
        ActionBarMenuItem addItemWithWidth = createActionMode.addItemWithWidth(0, R.drawable.ic_ab_other, AndroidUtilities.dp(54.0f), LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
        this.archiveItem = addItemWithWidth.addSubItem(R.styleable.AppCompatTheme_textAppearanceListItemSmall, i2, LocaleController.getString("Archive", R.string.Archive));
        this.pin2Item = addItemWithWidth.addSubItem(R.styleable.AppCompatTheme_textAppearanceSearchResultTitle, i, LocaleController.getString("DialogPin", R.string.DialogPin));
        this.addToFolderItem = addItemWithWidth.addSubItem(R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu, R.drawable.msg_addfolder, LocaleController.getString("FilterAddTo", R.string.FilterAddTo));
        this.removeFromFolderItem = addItemWithWidth.addSubItem(R.styleable.AppCompatTheme_textColorAlertDialogListItem, R.drawable.msg_removefolder, LocaleController.getString("FilterRemoveFrom", R.string.FilterRemoveFrom));
        this.readItem = addItemWithWidth.addSubItem(101, R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead));
        this.clearItem = addItemWithWidth.addSubItem(R.styleable.AppCompatTheme_textAppearanceListItem, R.drawable.msg_clear, LocaleController.getString("ClearHistory", R.string.ClearHistory));
        this.blockItem = addItemWithWidth.addSubItem(R.styleable.AppCompatTheme_textAppearancePopupMenuHeader, R.drawable.msg_block, LocaleController.getString("BlockUser", R.string.BlockUser));
        this.muteItem.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda56
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$createActionMode$63;
                lambda$createActionMode$63 = DialogsActivity.this.lambda$createActionMode$63(view);
                return lambda$createActionMode$63;
            }
        });
        this.actionModeViews.add(this.pinItem);
        this.actionModeViews.add(this.archive2Item);
        this.actionModeViews.add(this.muteItem);
        this.actionModeViews.add(this.deleteItem);
        this.actionModeViews.add(addItemWithWidth);
        updateCounters(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createActionMode$63(View view) {
        performSelectedDialogsAction(this.selectedDialogs, R.styleable.AppCompatTheme_textAppearanceListItemSecondary, true, true);
        return true;
    }

    public void closeSearching() {
        ActionBar actionBar = this.actionBar;
        if (actionBar == null || !actionBar.isSearchFieldVisible()) {
            return;
        }
        this.actionBar.closeSearchField();
        this.searchIsShowed = false;
        updateFilterTabs(true, true);
    }

    public void scrollToFolder(int i) {
        if (this.filterTabsView == null) {
            updateFilterTabs(true, true);
            if (this.filterTabsView == null) {
                return;
            }
        }
        int tabsCount = this.filterTabsView.getTabsCount() - 1;
        ArrayList<MessagesController.DialogFilter> dialogFilters = getMessagesController().getDialogFilters();
        int i2 = 0;
        while (true) {
            if (i2 >= dialogFilters.size()) {
                break;
            } else if (dialogFilters.get(i2).id == i) {
                tabsCount = i2;
                break;
            } else {
                i2++;
            }
        }
        FilterTabsView.Tab tab = this.filterTabsView.getTab(tabsCount);
        if (tab != null) {
            this.filterTabsView.scrollToTab(tab, tabsCount);
        } else {
            this.filterTabsView.selectLastTab();
        }
    }

    public void switchToCurrentSelectedMode(boolean z) {
        ViewPage[] viewPageArr;
        int i = 0;
        int i2 = 0;
        while (true) {
            viewPageArr = this.viewPages;
            if (i2 >= viewPageArr.length) {
                break;
            }
            viewPageArr[i2].listView.stopScroll();
            i2++;
        }
        if (viewPageArr[z ? 1 : 0].selectedType < 0 || this.viewPages[z ? 1 : 0].selectedType >= getMessagesController().getDialogFilters().size()) {
            return;
        }
        MessagesController.DialogFilter dialogFilter = getMessagesController().getDialogFilters().get(this.viewPages[z ? 1 : 0].selectedType);
        if (dialogFilter.isDefault()) {
            this.viewPages[z ? 1 : 0].dialogsType = this.initialDialogsType;
            this.viewPages[z ? 1 : 0].listView.updatePullState();
        } else {
            if (this.viewPages[!z ? 1 : 0].dialogsType == 7) {
                this.viewPages[z ? 1 : 0].dialogsType = 8;
            } else {
                this.viewPages[z ? 1 : 0].dialogsType = 7;
            }
            this.viewPages[z ? 1 : 0].listView.setScrollEnabled(true);
            getMessagesController().selectDialogFilter(dialogFilter, this.viewPages[z ? 1 : 0].dialogsType == 8 ? 1 : 0);
        }
        this.viewPages[1].isLocked = dialogFilter.locked;
        this.viewPages[z ? 1 : 0].dialogsAdapter.setDialogsType(this.viewPages[z ? 1 : 0].dialogsType);
        LinearLayoutManager linearLayoutManager = this.viewPages[z ? 1 : 0].layoutManager;
        if (this.viewPages[z ? 1 : 0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[z ? 1 : 0].archivePullViewState == 2) {
            i = 1;
        }
        linearLayoutManager.scrollToPositionWithOffset(i, (int) this.scrollYOffset);
        checkListLoad(this.viewPages[z ? 1 : 0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showScrollbars(boolean z) {
        if (this.viewPages == null || this.scrollBarVisible == z) {
            return;
        }
        this.scrollBarVisible = z;
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            if (z) {
                viewPageArr[i].listView.setScrollbarFadingEnabled(false);
            }
            this.viewPages[i].listView.setVerticalScrollBarEnabled(z);
            if (z) {
                this.viewPages[i].listView.setScrollbarFadingEnabled(true);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFilterTabs(boolean z, boolean z2) {
        int findFirstVisibleItemPosition;
        ViewPage[] viewPageArr;
        MessagesController.DialogFilter dialogFilter;
        boolean z3;
        int i;
        if (this.filterTabsView == null || this.inPreviewMode || this.searchIsShowed) {
            return;
        }
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment()) {
            ItemOptions itemOptions = this.filterOptions;
            if (itemOptions != null) {
                itemOptions.dismiss();
                this.filterOptions = null;
            }
            ArrayList<MessagesController.DialogFilter> dialogFilters = getMessagesController().getDialogFilters();
            boolean z4 = true;
            if (dialogFilters.size() > 1) {
                if (z || this.filterTabsView.getVisibility() != 0) {
                    boolean z5 = this.filterTabsView.getVisibility() != 0 ? false : z2;
                    this.canShowFilterTabsView = true;
                    boolean isEmpty = this.filterTabsView.isEmpty();
                    updateFilterTabsVisibility(z2);
                    int currentTabId = this.filterTabsView.getCurrentTabId();
                    int currentTabStableId = this.filterTabsView.getCurrentTabStableId();
                    if (currentTabId == this.filterTabsView.getDefaultTabId() || currentTabId < dialogFilters.size()) {
                        z3 = false;
                    } else {
                        this.filterTabsView.resetTabId();
                        z3 = true;
                    }
                    this.filterTabsView.removeTabs();
                    int size = dialogFilters.size();
                    int i2 = 0;
                    while (i2 < size) {
                        if (dialogFilters.get(i2).isDefault()) {
                            i = i2;
                            this.filterTabsView.addTab(i2, 0, LocaleController.getString("FilterAllChats", R.string.FilterAllChats), true, dialogFilters.get(i2).locked);
                        } else {
                            i = i2;
                            this.filterTabsView.addTab(i, dialogFilters.get(i).localId, dialogFilters.get(i).name, false, dialogFilters.get(i).locked);
                        }
                        i2 = i + 1;
                    }
                    if (currentTabStableId >= 0) {
                        if (z3 && !this.filterTabsView.selectTabWithStableId(currentTabStableId)) {
                            while (currentTabId >= 0) {
                                FilterTabsView filterTabsView = this.filterTabsView;
                                if (filterTabsView.selectTabWithStableId(filterTabsView.getStableId(currentTabId))) {
                                    break;
                                }
                                currentTabId--;
                            }
                            if (currentTabId < 0) {
                                currentTabId = 0;
                            }
                        }
                        if (this.filterTabsView.getStableId(this.viewPages[0].selectedType) != currentTabStableId) {
                            this.viewPages[0].selectedType = currentTabId;
                            isEmpty = true;
                        }
                    }
                    int i3 = 0;
                    while (true) {
                        ViewPage[] viewPageArr2 = this.viewPages;
                        if (i3 >= viewPageArr2.length) {
                            break;
                        }
                        if (viewPageArr2[i3].selectedType >= dialogFilters.size()) {
                            this.viewPages[i3].selectedType = dialogFilters.size() - 1;
                        }
                        this.viewPages[i3].listView.setScrollingTouchSlop(1);
                        i3++;
                    }
                    this.filterTabsView.finishAddingTabs(z5);
                    if (isEmpty) {
                        switchToCurrentSelectedMode(false);
                    }
                    this.isFirstTab = currentTabId == this.filterTabsView.getFirstTabId();
                    updateDrawerSwipeEnabled();
                    FilterTabsView filterTabsView2 = this.filterTabsView;
                    if (filterTabsView2.isLocked(filterTabsView2.getCurrentTabId())) {
                        this.filterTabsView.selectFirstTab();
                    }
                }
            } else {
                if (this.filterTabsView.getVisibility() != 8) {
                    this.filterTabsView.setIsEditing(false);
                    showDoneItem(false);
                    this.maybeStartTracking = false;
                    if (this.startedTracking) {
                        this.startedTracking = false;
                        this.viewPages[0].setTranslationX(0.0f);
                        this.viewPages[1].setTranslationX(viewPageArr[0].getMeasuredWidth());
                    }
                    if (this.viewPages[0].selectedType != this.filterTabsView.getDefaultTabId()) {
                        this.viewPages[0].selectedType = this.filterTabsView.getDefaultTabId();
                        this.viewPages[0].dialogsAdapter.setDialogsType(0);
                        this.viewPages[0].dialogsType = this.initialDialogsType;
                        this.viewPages[0].dialogsAdapter.notifyDataSetChanged();
                    }
                    this.viewPages[1].setVisibility(8);
                    this.viewPages[1].selectedType = 0;
                    this.viewPages[1].dialogsAdapter.setDialogsType(0);
                    this.viewPages[1].dialogsType = this.initialDialogsType;
                    this.viewPages[1].dialogsAdapter.notifyDataSetChanged();
                    this.canShowFilterTabsView = false;
                    updateFilterTabsVisibility(z2);
                    int i4 = 0;
                    while (true) {
                        ViewPage[] viewPageArr3 = this.viewPages;
                        if (i4 >= viewPageArr3.length) {
                            break;
                        }
                        if (viewPageArr3[i4].dialogsType == 0 && this.viewPages[i4].archivePullViewState == 2 && hasHiddenArchive() && ((findFirstVisibleItemPosition = this.viewPages[i4].layoutManager.findFirstVisibleItemPosition()) == 0 || findFirstVisibleItemPosition == 1)) {
                            this.viewPages[i4].layoutManager.scrollToPositionWithOffset(1, (int) this.scrollYOffset);
                        }
                        this.viewPages[i4].listView.setScrollingTouchSlop(0);
                        this.viewPages[i4].listView.requestLayout();
                        this.viewPages[i4].requestLayout();
                        i4++;
                    }
                    this.filterTabsView.resetTabId();
                }
                updateDrawerSwipeEnabled();
            }
            updateCounters(false);
            int i5 = this.viewPages[0].dialogsType;
            if ((i5 == 7 || i5 == 8) && (dialogFilter = getMessagesController().selectedDialogFilter[i5 - 7]) != null) {
                int i6 = 0;
                while (true) {
                    if (i6 >= dialogFilters.size()) {
                        z4 = false;
                        break;
                    }
                    MessagesController.DialogFilter dialogFilter2 = dialogFilters.get(i6);
                    if (dialogFilter2 != null && dialogFilter2.id == dialogFilter.id) {
                        break;
                    }
                    i6++;
                }
                if (z4) {
                    return;
                }
                switchToCurrentSelectedMode(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDrawerSwipeEnabled() {
        RightSlidingDialogContainer rightSlidingDialogContainer;
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout == null || iNavigationLayout.getDrawerLayoutContainer() == null) {
            return;
        }
        this.parentLayout.getDrawerLayoutContainer().setAllowOpenDrawerBySwipe(((this.isFirstTab && SharedConfig.getChatSwipeAction(this.currentAccount) == 5) || SharedConfig.getChatSwipeAction(this.currentAccount) != 5) && !this.searchIsShowed && ((rightSlidingDialogContainer = this.rightSlidingDialogContainer) == null || !rightSlidingDialogContainer.hasFragment()));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void finishFragment() {
        super.finishFragment();
        ItemOptions itemOptions = this.filterOptions;
        if (itemOptions != null) {
            itemOptions.dismiss();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        int i;
        View view;
        super.onResume();
        DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
        if (dialogStoriesCell != null) {
            dialogStoriesCell.onResume();
        }
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null) {
            rightSlidingDialogContainer.onResume();
        }
        if (!this.parentLayout.isInPreviewMode() && (view = this.blurredView) != null && view.getVisibility() == 0) {
            this.blurredView.setVisibility(8);
            this.blurredView.setBackground(null);
        }
        updateDrawerSwipeEnabled();
        if (this.viewPages != null) {
            int i2 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i2 >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i2].dialogsAdapter.notifyDataSetChanged();
                i2++;
            }
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onResume();
        }
        if (!this.onlySelect && this.folderId == 0) {
            getMediaDataController().checkStickers(4);
        }
        SearchViewPager searchViewPager = this.searchViewPager;
        if (searchViewPager != null) {
            searchViewPager.onResume();
        }
        if ((this.afterSignup || getUserConfig().unacceptedTermsOfService == null) && this.folderId == 0 && this.checkPermission && !this.onlySelect && (i = Build.VERSION.SDK_INT) >= 23) {
            final Activity parentActivity = getParentActivity();
            if (parentActivity != null) {
                this.checkPermission = false;
                boolean z = parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0;
                final boolean z2 = (i <= 28 || BuildVars.NO_SCOPED_STORAGE) && parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0;
                boolean z3 = i >= 33 && parentActivity.checkSelfPermission("android.permission.POST_NOTIFICATIONS") != 0;
                final boolean z4 = z3;
                final boolean z5 = z;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda102
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.this.lambda$onResume$66(z4, z5, z2, parentActivity);
                    }
                }, (this.afterSignup && (z || z3)) ? 4000L : 0L);
            }
        } else if (!this.onlySelect && XiaomiUtilities.isMIUI() && Build.VERSION.SDK_INT >= 19 && !XiaomiUtilities.isCustomPermissionGranted(XiaomiUtilities.OP_SHOW_WHEN_LOCKED)) {
            if (getParentActivity() == null || MessagesController.getGlobalNotificationsSettings().getBoolean("askedAboutMiuiLockscreen", false)) {
                return;
            }
            showDialog(new AlertDialog.Builder(getParentActivity()).setTopAnimation(R.raw.permission_request_apk, 72, false, Theme.getColor(Theme.key_dialogTopBackground)).setMessage(LocaleController.getString("PermissionXiaomiLockscreen", R.string.PermissionXiaomiLockscreen)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda12
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    DialogsActivity.this.lambda$onResume$67(dialogInterface, i3);
                }
            }).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", R.string.ContactsPermissionAlertNotNow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda20
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    DialogsActivity.lambda$onResume$68(dialogInterface, i3);
                }
            }).create());
        }
        showFiltersHint();
        if (this.viewPages != null) {
            int i3 = 0;
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i3 >= viewPageArr2.length) {
                    break;
                }
                if (viewPageArr2[i3].dialogsType == 0 && this.viewPages[i3].archivePullViewState == 2 && this.viewPages[i3].layoutManager.findFirstVisibleItemPosition() == 0 && hasHiddenArchive()) {
                    this.viewPages[i3].layoutManager.scrollToPositionWithOffset(1, (int) this.scrollYOffset);
                }
                if (i3 == 0) {
                    this.viewPages[i3].dialogsAdapter.resume();
                } else {
                    this.viewPages[i3].dialogsAdapter.pause();
                }
                i3++;
            }
        }
        showNextSupportedSuggestion();
        Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.DialogsActivity.37
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean bottomOffsetAnimated() {
                return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean clipWithGradient(int i4) {
                return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i4);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getBottomOffset(int i4) {
                return Bulletin.Delegate.-CC.$default$getBottomOffset(this, i4);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public void onBottomOffsetChange(float f) {
                if (DialogsActivity.this.undoView[0] == null || DialogsActivity.this.undoView[0].getVisibility() != 0) {
                    DialogsActivity.this.additionalFloatingTranslation = f;
                    if (DialogsActivity.this.additionalFloatingTranslation < 0.0f) {
                        DialogsActivity.this.additionalFloatingTranslation = 0.0f;
                    }
                    if (DialogsActivity.this.floatingHidden) {
                        return;
                    }
                    DialogsActivity.this.updateFloatingButtonOffset();
                }
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public void onShow(Bulletin bulletin) {
                if (DialogsActivity.this.undoView[0] == null || DialogsActivity.this.undoView[0].getVisibility() != 0) {
                    return;
                }
                DialogsActivity.this.undoView[0].hide(true, 2);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getTopOffset(int i4) {
                int i5 = 0;
                int measuredHeight = (((BaseFragment) DialogsActivity.this).actionBar != null ? ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() : 0) + ((DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) ? 0 : DialogsActivity.this.filterTabsView.getMeasuredHeight()) + ((DialogsActivity.this.fragmentContextView == null || !DialogsActivity.this.fragmentContextView.isCallTypeVisible()) ? 0 : AndroidUtilities.dp(DialogsActivity.this.fragmentContextView.getStyleHeight())) + ((DialogsActivity.this.dialogsHintCell == null || DialogsActivity.this.dialogsHintCell.getVisibility() != 0) ? 0 : DialogsActivity.this.dialogsHintCell.getHeight()) + ((DialogsActivity.this.authHintCell == null || !DialogsActivity.this.authHintCellVisible) ? 0 : DialogsActivity.this.authHintCell.getHeight());
                DialogsActivity dialogsActivity = DialogsActivity.this;
                DialogStoriesCell dialogStoriesCell2 = dialogsActivity.dialogStoriesCell;
                if (dialogStoriesCell2 != null && dialogsActivity.dialogStoriesCellVisible) {
                    i5 = (int) ((1.0f - dialogStoriesCell2.getCollapsedProgress()) * AndroidUtilities.dp(81.0f));
                }
                return measuredHeight + i5;
            }
        });
        if (this.searchIsShowed) {
            AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        }
        updateVisibleRows(0, false);
        updateProxyButton(false, true);
        updateStoriesVisibility(false);
        checkSuggestClearDatabase();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$66(boolean z, boolean z2, boolean z3, final Activity activity) {
        if (getParentActivity() == null) {
            return;
        }
        this.afterSignup = false;
        if (z || z2 || z3) {
            this.askingForPermissions = true;
            if (z && NotificationPermissionDialog.shouldAsk(activity)) {
                Dialog notificationPermissionDialog = new NotificationPermissionDialog(activity, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda115
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        DialogsActivity.lambda$onResume$64(activity, (Boolean) obj);
                    }
                });
                if (showDialog(notificationPermissionDialog) == null) {
                    try {
                        notificationPermissionDialog.show();
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
            } else if (z2 && this.askAboutContacts && getUserConfig().syncContacts && activity.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
                AlertDialog create = AlertsCreator.createContactsPermissionDialog(activity, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda111
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i) {
                        DialogsActivity.this.lambda$onResume$65(i);
                    }
                }).create();
                this.permissionDialog = create;
                showDialog(create);
            } else if (z3 && activity.shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                if (activity instanceof BasePermissionsActivity) {
                    AlertDialog createPermissionErrorAlert = ((BasePermissionsActivity) activity).createPermissionErrorAlert(R.raw.permission_request_folder, LocaleController.getString(R.string.PermissionStorageWithHint));
                    this.permissionDialog = createPermissionErrorAlert;
                    showDialog(createPermissionErrorAlert);
                }
            } else {
                askForPermissons(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onResume$64(Activity activity, Boolean bool) {
        if (bool.booleanValue()) {
            activity.requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$65(int i) {
        this.askAboutContacts = i != 0;
        MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", this.askAboutContacts).apply();
        askForPermissons(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$67(DialogInterface dialogInterface, int i) {
        Intent permissionManagerIntent = XiaomiUtilities.getPermissionManagerIntent();
        if (permissionManagerIntent != null) {
            try {
                try {
                    getParentActivity().startActivity(permissionManagerIntent);
                } catch (Exception unused) {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
                    getParentActivity().startActivity(intent);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onResume$68(DialogInterface dialogInterface, int i) {
        MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askedAboutMiuiLockscreen", true).commit();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean presentFragment(BaseFragment baseFragment) {
        boolean presentFragment = super.presentFragment(baseFragment);
        if (presentFragment && this.viewPages != null) {
            int i = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i].dialogsAdapter.pause();
                i++;
            }
        }
        HintView2 hintView2 = this.storyHint;
        if (hintView2 != null) {
            hintView2.hide();
        }
        Bulletin.hideVisible();
        return presentFragment;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        Bulletin bulletin = this.storiesBulletin;
        if (bulletin != null) {
            bulletin.hide();
            this.storiesBulletin = null;
        }
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null) {
            rightSlidingDialogContainer.onPause();
        }
        ItemOptions itemOptions = this.filterOptions;
        if (itemOptions != null) {
            itemOptions.dismiss();
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onPause();
        }
        UndoView[] undoViewArr = this.undoView;
        int i = 0;
        if (undoViewArr[0] != null) {
            undoViewArr[0].hide(true, 0);
        }
        Bulletin.removeDelegate(this);
        if (this.viewPages == null) {
            return;
        }
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            viewPageArr[i].dialogsAdapter.pause();
            i++;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (closeSheet()) {
            return false;
        }
        if (this.rightSlidingDialogContainer.hasFragment()) {
            if (this.rightSlidingDialogContainer.getFragment().onBackPressed()) {
                this.rightSlidingDialogContainer.lambda$presentFragment$1();
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager != null) {
                    searchViewPager.updateTabs();
                }
            }
            return false;
        }
        ItemOptions itemOptions = this.filterOptions;
        if (itemOptions != null) {
            itemOptions.dismiss();
            this.filterOptions = null;
            return false;
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null && filterTabsView.isEditing()) {
            this.filterTabsView.setIsEditing(false);
            showDoneItem(false);
            return false;
        }
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.isActionModeShowed()) {
            SearchViewPager searchViewPager2 = this.searchViewPager;
            if (searchViewPager2 != null && searchViewPager2.getVisibility() == 0) {
                this.searchViewPager.hideActionMode();
                hideActionMode(true);
            } else {
                hideActionMode(true);
            }
            return false;
        }
        FilterTabsView filterTabsView2 = this.filterTabsView;
        if (filterTabsView2 != null && filterTabsView2.getVisibility() == 0 && !this.tabsAnimationInProgress && !this.filterTabsView.isAnimatingIndicator() && !this.startedTracking && !this.filterTabsView.isFirstTabSelected()) {
            this.filterTabsView.selectFirstTab();
            return false;
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null && chatActivityEnterView.isPopupShowing()) {
            this.commentView.hidePopup(true);
            return false;
        } else if (this.dialogStoriesCell.isFullExpanded() && this.dialogStoriesCell.scrollToFirst()) {
            return false;
        } else {
            return super.onBackPressed();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        View view;
        FilterTabsView filterTabsView;
        if (this.closeSearchFieldOnHide) {
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
            TLObject tLObject = this.searchObject;
            if (tLObject != null) {
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager != null) {
                    searchViewPager.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, tLObject);
                }
                this.searchObject = null;
            }
            this.closeSearchFieldOnHide = false;
        }
        if (!this.hasStories && (filterTabsView = this.filterTabsView) != null && filterTabsView.getVisibility() == 0 && this.filterTabsViewIsVisible) {
            int i = (int) (-this.scrollYOffset);
            int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
            if (i != 0 && i != currentActionBarHeight) {
                if (i < currentActionBarHeight / 2) {
                    setScrollY(0.0f);
                } else if (this.viewPages[0].listView.canScrollVertically(1)) {
                    setScrollY(-currentActionBarHeight);
                }
            }
        }
        UndoView[] undoViewArr = this.undoView;
        if (undoViewArr[0] != null) {
            undoViewArr[0].hide(true, 0);
        }
        if (!isInPreviewMode() && (view = this.blurredView) != null && view.getVisibility() == 0) {
            this.blurredView.setVisibility(8);
            this.blurredView.setBackground(null);
        }
        super.onBecomeFullyHidden();
        this.canShowStoryHint = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        HintView2 hintView2;
        super.onBecomeFullyVisible();
        if (isArchive()) {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            boolean z = globalMainSettings.getBoolean("archivehint", true);
            boolean isEmpty = getDialogsArray(this.currentAccount, this.initialDialogsType, this.folderId, false).isEmpty();
            if (z && isEmpty) {
                MessagesController.getGlobalMainSettings().edit().putBoolean("archivehint", false).commit();
                z = false;
            }
            if (z) {
                globalMainSettings.edit().putBoolean("archivehint", false).commit();
                showArchiveHelp();
            }
            ActionBarMenuItem actionBarMenuItem = this.optionsItem;
            if (actionBarMenuItem != null) {
                if (isEmpty) {
                    actionBarMenuItem.hideSubItem(6);
                } else {
                    actionBarMenuItem.showSubItem(6);
                }
            }
        }
        updateFloatingButtonOffset();
        if (this.canShowStoryHint && !this.storyHintShown && (hintView2 = this.storyHint) != null && this.storiesEnabled) {
            this.storyHintShown = true;
            this.canShowStoryHint = false;
            hintView2.show();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.createSearchViewPager();
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showArchiveHelp() {
        getContactsController().loadGlobalPrivacySetting();
        final BottomSheet[] bottomSheetArr = {new BottomSheet.Builder(getContext(), false, getResourceProvider()).setCustomView(new ArchiveHelp(getContext(), this.currentAccount, getResourceProvider(), new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda104
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$showArchiveHelp$70(bottomSheetArr);
            }
        }, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda105
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.lambda$showArchiveHelp$71(bottomSheetArr);
            }
        }), 49).show()};
        bottomSheetArr[0].fixNavigationBar(Theme.getColor(Theme.key_dialogBackground));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showArchiveHelp$70(BottomSheet[] bottomSheetArr) {
        if (bottomSheetArr[0] != null) {
            bottomSheetArr[0].dismiss();
            bottomSheetArr[0] = null;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda84
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$showArchiveHelp$69();
            }
        }, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showArchiveHelp$69() {
        presentFragment(new ArchiveSettingsActivity());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showArchiveHelp$71(BottomSheet[] bottomSheetArr) {
        if (bottomSheetArr[0] != null) {
            bottomSheetArr[0].dismiss();
            bottomSheetArr[0] = null;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setInPreviewMode(boolean z) {
        ActionBarMenuItem actionBarMenuItem;
        super.setInPreviewMode(z);
        if (!z && this.avatarContainer != null) {
            this.actionBar.setBackground(null);
            ((ViewGroup.MarginLayoutParams) this.actionBar.getLayoutParams()).topMargin = 0;
            this.actionBar.removeView(this.avatarContainer);
            this.avatarContainer = null;
            updateFilterTabs(false, false);
            this.floatingButton.setVisibility(0);
            ContentView contentView = (ContentView) this.fragmentView;
            FragmentContextView fragmentContextView = this.fragmentContextView;
            if (fragmentContextView != null) {
                contentView.addView(fragmentContextView);
            }
            FragmentContextView fragmentContextView2 = this.fragmentLocationContextView;
            if (fragmentContextView2 != null) {
                contentView.addView(fragmentContextView2);
            }
        }
        DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
        if (dialogStoriesCell != null) {
            if (this.dialogStoriesCellVisible && !z) {
                dialogStoriesCell.setVisibility(0);
            } else {
                dialogStoriesCell.setVisibility(8);
            }
        }
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            frameLayout.setVisibility(((!this.onlySelect || this.initialDialogsType == 10) && this.folderId == 0 && !z) ? 0 : 8);
        }
        FrameLayout frameLayout2 = this.floatingButton2Container;
        if (frameLayout2 != null) {
            frameLayout2.setVisibility(((this.onlySelect && this.initialDialogsType != 10) || this.folderId != 0 || !this.storiesEnabled || ((actionBarMenuItem = this.searchItem) != null && actionBarMenuItem.isSearchFieldVisible()) || z) ? 8 : 0);
        }
        lambda$updateDialogsHint$29();
    }

    public boolean addOrRemoveSelectedDialog(long j, View view) {
        if (this.onlySelect && getMessagesController().isForum(j)) {
            return false;
        }
        if (this.selectedDialogs.contains(Long.valueOf(j))) {
            this.selectedDialogs.remove(Long.valueOf(j));
            if (view instanceof DialogCell) {
                ((DialogCell) view).setChecked(false, true);
            } else if (view instanceof ProfileSearchCell) {
                ((ProfileSearchCell) view).setChecked(false, true);
            }
            return false;
        }
        this.selectedDialogs.add(Long.valueOf(j));
        if (view instanceof DialogCell) {
            ((DialogCell) view).setChecked(true, true);
        } else if (view instanceof ProfileSearchCell) {
            ((ProfileSearchCell) view).setChecked(true, true);
        }
        return true;
    }

    public void search(String str, boolean z) {
        showSearch(true, false, z);
        this.actionBar.openSearchField(str, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSearch(boolean z, boolean z2, boolean z3) {
        showSearch(z, z2, z3, false);
    }

    private void showSearch(final boolean z, boolean z2, boolean z3, boolean z4) {
        SearchViewPager searchViewPager;
        SearchViewPager searchViewPager2;
        DialogStoriesCell dialogStoriesCell;
        FilterTabsView filterTabsView;
        SearchViewPager searchViewPager3;
        RightSlidingDialogContainer rightSlidingDialogContainer;
        SearchViewPager searchViewPager4;
        int i;
        if (!z) {
            updateSpeedItem(false);
        } else {
            createSearchViewPager();
        }
        int i2 = this.initialDialogsType;
        boolean z5 = (i2 == 0 || i2 == 3) ? z3 : false;
        AnimatorSet animatorSet = this.searchAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.searchAnimator = null;
        }
        Animator animator = this.tabsAlphaAnimator;
        if (animator != null) {
            animator.cancel();
            this.tabsAlphaAnimator = null;
        }
        this.searchIsShowed = z;
        ((SizeNotifierFrameLayout) this.fragmentView).invalidateBlur();
        if (z) {
            boolean onlyDialogsAdapter = (this.searchFiltersWasShowed || z4) ? false : onlyDialogsAdapter();
            SearchViewPager searchViewPager5 = this.searchViewPager;
            if (searchViewPager5 != null) {
                searchViewPager5.showOnlyDialogsAdapter(onlyDialogsAdapter);
            }
            boolean z6 = !onlyDialogsAdapter || this.hasStories;
            this.whiteActionBar = z6;
            if (z6) {
                this.searchFiltersWasShowed = true;
            }
            ContentView contentView = (ContentView) this.fragmentView;
            ViewPagerFixed.TabsView tabsView = this.searchTabsView;
            if (tabsView == null && (searchViewPager4 = this.searchViewPager) != null && !onlyDialogsAdapter) {
                this.searchTabsView = searchViewPager4.createTabsView(false, 8);
                if (this.filtersView != null) {
                    i = 0;
                    while (i < contentView.getChildCount()) {
                        if (contentView.getChildAt(i) == this.filtersView) {
                            break;
                        }
                        i++;
                    }
                }
                i = -1;
                if (i > 0) {
                    contentView.addView(this.searchTabsView, i, LayoutHelper.createFrame(-1, 44.0f));
                } else {
                    contentView.addView(this.searchTabsView, LayoutHelper.createFrame(-1, 44.0f));
                }
            } else if (tabsView != null && onlyDialogsAdapter) {
                ViewParent parent = tabsView.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(this.searchTabsView);
                }
                this.searchTabsView = null;
            }
            EditTextBoldCursor searchField = this.searchItem.getSearchField();
            if (this.whiteActionBar) {
                searchField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                searchField.setHintTextColor(Theme.getColor(Theme.key_player_time));
                searchField.setCursorColor(Theme.getColor(Theme.key_chat_messagePanelCursor));
            } else {
                int i3 = Theme.key_actionBarDefaultSearch;
                searchField.setCursorColor(Theme.getColor(i3));
                searchField.setHintTextColor(Theme.getColor(Theme.key_actionBarDefaultSearchPlaceholder));
                searchField.setTextColor(Theme.getColor(i3));
            }
            updateDrawerSwipeEnabled();
            SearchViewPager searchViewPager6 = this.searchViewPager;
            if (searchViewPager6 != null) {
                searchViewPager6.setKeyboardHeight(((ContentView) this.fragmentView).getKeyboardHeight());
                this.searchViewPager.clear();
            }
            if (this.folderId != 0 && ((rightSlidingDialogContainer = this.rightSlidingDialogContainer) == null || !rightSlidingDialogContainer.hasFragment())) {
                addSearchFilter(new FiltersView.MediaFilterData(R.drawable.chats_archive, R.string.ArchiveSearchFilter, (TLRPC$MessagesFilter) null, 7));
            }
        } else {
            updateDrawerSwipeEnabled();
        }
        if (z5 && (searchViewPager3 = this.searchViewPager) != null && searchViewPager3.dialogsSearchAdapter.hasRecentSearch()) {
            AndroidUtilities.setAdjustResizeToNothing(getParentActivity(), this.classGuid);
        } else {
            AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        }
        if (!z && (filterTabsView = this.filterTabsView) != null && this.canShowFilterTabsView) {
            filterTabsView.setVisibility(0);
        }
        if (!z && (dialogStoriesCell = this.dialogStoriesCell) != null && this.dialogStoriesCellVisible) {
            dialogStoriesCell.setVisibility(0);
        }
        boolean z7 = SharedConfig.getDevicePerformanceClass() == 0 || !LiteMode.isEnabled(LiteMode.FLAG_CHAT_SCALE);
        if (z5) {
            if (z) {
                SearchViewPager searchViewPager7 = this.searchViewPager;
                if (searchViewPager7 != null) {
                    searchViewPager7.setVisibility(0);
                    this.searchViewPager.reset();
                }
                updateFiltersView(true, null, null, false, false);
                ViewPagerFixed.TabsView tabsView2 = this.searchTabsView;
                if (tabsView2 != null) {
                    tabsView2.hide(false, false);
                    this.searchTabsView.setVisibility(0);
                }
            } else {
                this.viewPages[0].listView.setVisibility(0);
                this.viewPages[0].setVisibility(0);
            }
            setDialogsListFrozen(true);
            this.viewPages[0].listView.setVerticalScrollBarEnabled(false);
            SearchViewPager searchViewPager8 = this.searchViewPager;
            if (searchViewPager8 != null) {
                searchViewPager8.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            this.searchAnimator = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            ViewPage viewPage = this.viewPages[0];
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z ? 0.0f : 1.0f;
            arrayList.add(ObjectAnimator.ofFloat(viewPage, property, fArr));
            if (!z7) {
                ViewPage viewPage2 = this.viewPages[0];
                Property property2 = View.SCALE_X;
                float[] fArr2 = new float[1];
                fArr2[0] = z ? 0.9f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(viewPage2, property2, fArr2));
                ViewPage viewPage3 = this.viewPages[0];
                Property property3 = View.SCALE_Y;
                float[] fArr3 = new float[1];
                fArr3[0] = z ? 0.9f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(viewPage3, property3, fArr3));
            } else {
                this.viewPages[0].setScaleX(1.0f);
                this.viewPages[0].setScaleY(1.0f);
            }
            RightSlidingDialogContainer rightSlidingDialogContainer2 = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer2 != null) {
                rightSlidingDialogContainer2.setVisibility(0);
                RightSlidingDialogContainer rightSlidingDialogContainer3 = this.rightSlidingDialogContainer;
                Property property4 = View.ALPHA;
                float[] fArr4 = new float[1];
                fArr4[0] = z ? 0.0f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(rightSlidingDialogContainer3, property4, fArr4));
            }
            SearchViewPager searchViewPager9 = this.searchViewPager;
            if (searchViewPager9 != null) {
                Property property5 = View.ALPHA;
                float[] fArr5 = new float[1];
                fArr5[0] = z ? 1.0f : 0.0f;
                arrayList.add(ObjectAnimator.ofFloat(searchViewPager9, property5, fArr5));
                if (this.hasStories) {
                    float dp = AndroidUtilities.dp(81.0f) + this.scrollYOffset;
                    SearchViewPager searchViewPager10 = this.searchViewPager;
                    Property<View, Float> property6 = this.SEARCH_TRANSLATION_Y;
                    float[] fArr6 = new float[1];
                    if (z) {
                        dp = 0.0f;
                    }
                    fArr6[0] = dp;
                    arrayList.add(ObjectAnimator.ofFloat(searchViewPager10, (Property<SearchViewPager, Float>) property6, fArr6));
                }
                if (!z7) {
                    SearchViewPager searchViewPager11 = this.searchViewPager;
                    Property property7 = View.SCALE_X;
                    float[] fArr7 = new float[1];
                    fArr7[0] = z ? 1.0f : 1.05f;
                    arrayList.add(ObjectAnimator.ofFloat(searchViewPager11, property7, fArr7));
                    SearchViewPager searchViewPager12 = this.searchViewPager;
                    Property property8 = View.SCALE_Y;
                    float[] fArr8 = new float[1];
                    fArr8[0] = z ? 1.0f : 1.05f;
                    arrayList.add(ObjectAnimator.ofFloat(searchViewPager12, property8, fArr8));
                } else {
                    this.searchViewPager.setScaleX(1.0f);
                    this.searchViewPager.setScaleY(1.0f);
                }
            }
            ActionBarMenuItem actionBarMenuItem = this.passcodeItem;
            if (actionBarMenuItem != null) {
                RLottieImageView iconView = actionBarMenuItem.getIconView();
                Property property9 = View.ALPHA;
                float[] fArr9 = new float[1];
                fArr9[0] = z ? 0.0f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(iconView, property9, fArr9));
            }
            ActionBarMenuItem actionBarMenuItem2 = this.downloadsItem;
            if (actionBarMenuItem2 != null) {
                if (z) {
                    actionBarMenuItem2.setAlpha(0.0f);
                } else {
                    arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem2, View.ALPHA, 1.0f));
                }
                updateProxyButton(false, false);
            }
            FilterTabsView filterTabsView2 = this.filterTabsView;
            if (filterTabsView2 != null && filterTabsView2.getVisibility() == 0) {
                RecyclerListView tabsContainer = this.filterTabsView.getTabsContainer();
                Property property10 = View.ALPHA;
                float[] fArr10 = new float[1];
                fArr10[0] = z ? 0.0f : 1.0f;
                ObjectAnimator duration = ObjectAnimator.ofFloat(tabsContainer, property10, fArr10).setDuration(100L);
                this.tabsAlphaAnimator = duration;
                duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.38
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator2) {
                        DialogsActivity.this.tabsAlphaAnimator = null;
                    }
                });
            }
            float[] fArr11 = new float[2];
            fArr11[0] = this.searchAnimationProgress;
            fArr11[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr11);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    DialogsActivity.this.lambda$showSearch$72(valueAnimator);
                }
            });
            arrayList.add(ofFloat);
            this.searchAnimator.playTogether(arrayList);
            this.searchAnimator.setDuration(z ? 200L : 180L);
            this.searchAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            if (this.filterTabsViewIsVisible) {
                int color = Theme.getColor(this.folderId == 0 ? Theme.key_actionBarDefault : Theme.key_actionBarDefaultArchived);
                int color2 = Theme.getColor(Theme.key_windowBackgroundWhite);
                this.searchAnimationTabsDelayedCrossfade = ((float) ((Math.abs(Color.red(color) - Color.red(color2)) + Math.abs(Color.green(color) - Color.green(color2))) + Math.abs(Color.blue(color) - Color.blue(color2)))) / 255.0f > 0.3f;
            } else {
                this.searchAnimationTabsDelayedCrossfade = true;
            }
            if (!z) {
                this.searchAnimator.setStartDelay(20L);
                Animator animator2 = this.tabsAlphaAnimator;
                if (animator2 != null) {
                    if (this.searchAnimationTabsDelayedCrossfade) {
                        animator2.setStartDelay(80L);
                        this.tabsAlphaAnimator.setDuration(100L);
                    } else {
                        animator2.setDuration(z ? 200L : 180L);
                    }
                }
            }
            FragmentContextView fragmentContextView = this.fragmentContextView;
            if (fragmentContextView != null && Build.VERSION.SDK_INT >= 21) {
                fragmentContextView.setTranslationZ(1.0f);
            }
            this.searchAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.39
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator3) {
                    if (DialogsActivity.this.fragmentContextView != null && Build.VERSION.SDK_INT >= 21) {
                        DialogsActivity.this.fragmentContextView.setTranslationZ(0.0f);
                    }
                    DialogsActivity.this.notificationsLocker.unlock();
                    if (DialogsActivity.this.searchAnimator != animator3) {
                        return;
                    }
                    DialogsActivity.this.setDialogsListFrozen(false);
                    if (z) {
                        DialogsActivity.this.viewPages[0].listView.hide();
                        if (DialogsActivity.this.filterTabsView != null) {
                            DialogsActivity.this.filterTabsView.setVisibility(8);
                        }
                        DialogStoriesCell dialogStoriesCell2 = DialogsActivity.this.dialogStoriesCell;
                        if (dialogStoriesCell2 != null) {
                            dialogStoriesCell2.setVisibility(8);
                        }
                        DialogsActivity.this.searchWasFullyShowed = true;
                        AndroidUtilities.requestAdjustResize(DialogsActivity.this.getParentActivity(), ((BaseFragment) DialogsActivity.this).classGuid);
                        DialogsActivity.this.searchItem.setVisibility(8);
                        RightSlidingDialogContainer rightSlidingDialogContainer4 = DialogsActivity.this.rightSlidingDialogContainer;
                        if (rightSlidingDialogContainer4 != null) {
                            rightSlidingDialogContainer4.setVisibility(8);
                        }
                    } else {
                        DialogsActivity.this.searchItem.collapseSearchFilters();
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        dialogsActivity.whiteActionBar = false;
                        if (dialogsActivity.searchViewPager != null) {
                            DialogsActivity.this.searchViewPager.setVisibility(8);
                        }
                        if (DialogsActivity.this.searchTabsView != null) {
                            DialogsActivity.this.searchTabsView.setVisibility(8);
                        }
                        DialogsActivity.this.searchItem.clearSearchFilters();
                        if (DialogsActivity.this.searchViewPager != null) {
                            DialogsActivity.this.searchViewPager.clear();
                        }
                        DialogsActivity.this.filtersView.setVisibility(8);
                        DialogsActivity.this.viewPages[0].listView.show();
                        if (!DialogsActivity.this.onlySelect) {
                            DialogsActivity.this.hideFloatingButton(false);
                        }
                        DialogsActivity.this.searchWasFullyShowed = false;
                        RightSlidingDialogContainer rightSlidingDialogContainer5 = DialogsActivity.this.rightSlidingDialogContainer;
                        if (rightSlidingDialogContainer5 != null) {
                            rightSlidingDialogContainer5.setVisibility(0);
                        }
                    }
                    View view = DialogsActivity.this.fragmentView;
                    if (view != null) {
                        view.requestLayout();
                    }
                    DialogsActivity.this.setSearchAnimationProgress(z ? 1.0f : 0.0f, false);
                    DialogsActivity.this.viewPages[0].listView.setVerticalScrollBarEnabled(true);
                    if (DialogsActivity.this.searchViewPager != null) {
                        DialogsActivity.this.searchViewPager.setBackground(null);
                    }
                    DialogsActivity.this.searchAnimator = null;
                    if (DialogsActivity.this.downloadsItem != null) {
                        DialogsActivity.this.downloadsItem.setAlpha(z ? 0.0f : 1.0f);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator3) {
                    DialogsActivity.this.notificationsLocker.unlock();
                    if (DialogsActivity.this.searchAnimator == animator3) {
                        if (z) {
                            DialogsActivity.this.viewPages[0].listView.hide();
                        } else {
                            DialogsActivity.this.viewPages[0].listView.show();
                        }
                        DialogsActivity.this.searchAnimator = null;
                    }
                }
            });
            this.notificationsLocker.lock();
            this.searchAnimator.start();
            Animator animator3 = this.tabsAlphaAnimator;
            if (animator3 != null) {
                animator3.start();
            }
        } else {
            setDialogsListFrozen(false);
            if (z) {
                this.viewPages[0].listView.hide();
            } else {
                this.viewPages[0].listView.show();
            }
            this.viewPages[0].setAlpha(z ? 0.0f : 1.0f);
            if (!z7) {
                this.viewPages[0].setScaleX(z ? 0.9f : 1.0f);
                this.viewPages[0].setScaleY(z ? 0.9f : 1.0f);
            } else {
                this.viewPages[0].setScaleX(1.0f);
                this.viewPages[0].setScaleY(1.0f);
            }
            this.filtersView.setAlpha(z ? 1.0f : 0.0f);
            SearchViewPager searchViewPager13 = this.searchViewPager;
            if (searchViewPager13 != null) {
                searchViewPager13.setAlpha(z ? 1.0f : 0.0f);
                if (!z7) {
                    this.searchViewPager.setScaleX(z ? 1.0f : 1.1f);
                    this.searchViewPager.setScaleY(z ? 1.0f : 1.1f);
                } else {
                    this.searchViewPager.setScaleX(1.0f);
                    this.searchViewPager.setScaleY(1.0f);
                }
                this.searchViewPager.setVisibility(z ? 0 : 8);
            }
            FilterTabsView filterTabsView3 = this.filterTabsView;
            if (filterTabsView3 != null && filterTabsView3.getVisibility() == 0) {
                this.filterTabsView.setTranslationY(z ? -AndroidUtilities.dp(44.0f) : 0.0f);
                this.filterTabsView.getTabsContainer().setAlpha(z ? 0.0f : 1.0f);
            }
            FilterTabsView filterTabsView4 = this.filterTabsView;
            if (filterTabsView4 != null) {
                if (this.canShowFilterTabsView && !z) {
                    filterTabsView4.setVisibility(0);
                } else {
                    filterTabsView4.setVisibility(8);
                }
            }
            if (this.dialogStoriesCell != null) {
                if (this.dialogStoriesCellVisible && !isInPreviewMode() && !z) {
                    this.dialogStoriesCell.setVisibility(0);
                } else {
                    this.dialogStoriesCell.setVisibility(8);
                }
            }
            setSearchAnimationProgress(z ? 1.0f : 0.0f, false);
            this.fragmentView.invalidate();
            ActionBarMenuItem actionBarMenuItem3 = this.downloadsItem;
            if (actionBarMenuItem3 != null) {
                actionBarMenuItem3.setAlpha(z ? 0.0f : 1.0f);
            }
        }
        int i4 = this.initialSearchType;
        if (i4 >= 0 && (searchViewPager2 = this.searchViewPager) != null) {
            searchViewPager2.setPosition(searchViewPager2.getPositionForType(i4));
        }
        if (!z) {
            this.initialSearchType = -1;
        }
        if (z && z2 && (searchViewPager = this.searchViewPager) != null) {
            searchViewPager.showDownloads();
            updateSpeedItem(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSearch$72(ValueAnimator valueAnimator) {
        setSearchAnimationProgress(((Float) valueAnimator.getAnimatedValue()).floatValue(), false);
    }

    public boolean onlyDialogsAdapter() {
        SearchViewPager searchViewPager;
        return this.onlySelect || !((searchViewPager = this.searchViewPager) == null || searchViewPager.dialogsSearchAdapter.hasRecentSearch()) || (getMessagesController().getTotalDialogsCount() <= 10 && !this.hasStories);
    }

    private void updateFilterTabsVisibility(boolean z) {
        if (this.fragmentView == null) {
            return;
        }
        z = (this.isPaused || this.databaseMigrationHint != null) ? false : false;
        if (this.searchIsShowed) {
            ValueAnimator valueAnimator = this.filtersTabAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            boolean z2 = this.canShowFilterTabsView;
            this.filterTabsViewIsVisible = z2;
            this.filterTabsProgress = z2 ? 1.0f : 0.0f;
            return;
        }
        final boolean z3 = this.canShowFilterTabsView;
        if (this.filterTabsViewIsVisible != z3) {
            ValueAnimator valueAnimator2 = this.filtersTabAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.cancel();
            }
            this.filterTabsViewIsVisible = z3;
            if (z) {
                if (z3) {
                    if (this.filterTabsView.getVisibility() != 0) {
                        this.filterTabsView.setVisibility(0);
                    }
                    this.filtersTabAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                } else {
                    this.filtersTabAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                }
                this.filterTabsMoveFrom = getActionBarMoveFrom(true);
                final float f = this.scrollYOffset;
                this.filtersTabAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.40
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogsActivity.this.filtersTabAnimator = null;
                        DialogsActivity.this.scrollAdditionalOffset = 0.0f;
                        if (!z3) {
                            DialogsActivity.this.filterTabsView.setVisibility(8);
                        }
                        View view = DialogsActivity.this.fragmentView;
                        if (view != null) {
                            view.requestLayout();
                        }
                        DialogsActivity.this.notificationsLocker.unlock();
                    }
                });
                this.filtersTabAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda7
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        DialogsActivity.this.lambda$updateFilterTabsVisibility$73(z3, f, valueAnimator3);
                    }
                });
                this.filtersTabAnimator.setDuration(220L);
                this.filtersTabAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.notificationsLocker.lock();
                this.filtersTabAnimator.start();
                this.fragmentView.requestLayout();
                return;
            }
            this.filterTabsProgress = z3 ? 1.0f : 0.0f;
            this.filterTabsView.setVisibility(z3 ? 0 : 8);
            View view = this.fragmentView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFilterTabsVisibility$73(boolean z, float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.filterTabsProgress = floatValue;
        if (!z && !this.hasStories) {
            setScrollY(f * floatValue);
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setSearchAnimationProgress(float f, boolean z) {
        this.searchAnimationProgress = f;
        boolean z2 = true;
        if (this.whiteActionBar) {
            int color = Theme.getColor(this.folderId != 0 ? Theme.key_actionBarDefaultArchivedIcon : Theme.key_actionBarDefaultIcon);
            ActionBar actionBar = this.actionBar;
            int i = Theme.key_actionBarActionModeDefaultIcon;
            actionBar.setItemsColor(ColorUtils.blendARGB(color, Theme.getColor(i), this.searchAnimationProgress), false);
            this.actionBar.setItemsColor(ColorUtils.blendARGB(Theme.getColor(i), Theme.getColor(i), this.searchAnimationProgress), true);
            this.actionBar.setItemsBackgroundColor(ColorUtils.blendARGB(Theme.getColor(this.folderId != 0 ? Theme.key_actionBarDefaultArchivedSelector : Theme.key_actionBarDefaultSelector), Theme.getColor(Theme.key_actionBarActionModeDefaultSelector), this.searchAnimationProgress), false);
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
        DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null) {
            dialogsHintCell.setAlpha(1.0f - f);
            if (this.dialogsHintCellVisible) {
                if (this.dialogsHintCell.getAlpha() == 0.0f) {
                    this.dialogsHintCell.setVisibility(4);
                } else {
                    this.dialogsHintCell.setVisibility(0);
                    ViewParent parent = this.dialogsHintCell.getParent();
                    if (parent != null) {
                        parent.requestLayout();
                    }
                }
            }
        }
        UnconfirmedAuthHintCell unconfirmedAuthHintCell = this.authHintCell;
        if (unconfirmedAuthHintCell != null) {
            unconfirmedAuthHintCell.setAlpha(1.0f - f);
            if (this.authHintCellVisible) {
                if (this.authHintCell.getAlpha() == 0.0f) {
                    this.authHintCell.setVisibility(4);
                } else {
                    this.authHintCell.setVisibility(0);
                }
            }
        }
        if (SharedConfig.getDevicePerformanceClass() != 0 && LiteMode.isEnabled(LiteMode.FLAG_CHAT_SCALE)) {
            z2 = false;
        }
        if (z) {
            ViewPage[] viewPageArr = this.viewPages;
            if (viewPageArr[0] != null) {
                if (f < 1.0f) {
                    viewPageArr[0].setVisibility(0);
                }
                this.viewPages[0].setAlpha(1.0f - f);
                if (!z2) {
                    float f2 = (0.1f * f) + 0.9f;
                    this.viewPages[0].setScaleX(f2);
                    this.viewPages[0].setScaleY(f2);
                }
            }
            RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer != null) {
                if (f >= 1.0f) {
                    rightSlidingDialogContainer.setVisibility(8);
                } else {
                    rightSlidingDialogContainer.setVisibility(0);
                    this.rightSlidingDialogContainer.setAlpha(1.0f - f);
                }
            }
            SearchViewPager searchViewPager = this.searchViewPager;
            if (searchViewPager != null) {
                searchViewPager.setAlpha(f);
                if (!z2) {
                    float f3 = ((1.0f - f) * 0.05f) + 1.0f;
                    this.searchViewPager.setScaleX(f3);
                    this.searchViewPager.setScaleY(f3);
                }
            }
            ActionBarMenuItem actionBarMenuItem = this.passcodeItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.getIconView().setAlpha(1.0f - f);
            }
            ActionBarMenuItem actionBarMenuItem2 = this.downloadsItem;
            if (actionBarMenuItem2 != null) {
                actionBarMenuItem2.setAlpha(1.0f - f);
            }
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null && filterTabsView.getVisibility() == 0) {
                this.filterTabsView.getTabsContainer().setAlpha(1.0f - f);
            }
        }
        updateContextViewPosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void findAndUpdateCheckBox(long j, boolean z) {
        if (this.viewPages == null) {
            return;
        }
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            int childCount = viewPageArr[i].listView.getChildCount();
            int i2 = 0;
            while (true) {
                if (i2 < childCount) {
                    View childAt = this.viewPages[i].listView.getChildAt(i2);
                    if (childAt instanceof DialogCell) {
                        DialogCell dialogCell = (DialogCell) childAt;
                        if (dialogCell.getDialogId() == j) {
                            dialogCell.setChecked(z, true);
                            break;
                        }
                    }
                    i2++;
                }
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkListLoad(ViewPage viewPage) {
        checkListLoad(viewPage, viewPage.layoutManager.findFirstVisibleItemPosition(), viewPage.layoutManager.findLastVisibleItemPosition());
    }

    private void checkListLoad(ViewPage viewPage, int i, int i2) {
        final boolean z;
        final boolean z2;
        final boolean z3;
        final boolean z4;
        if (this.tabsAnimationInProgress || this.startedTracking) {
            return;
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null && filterTabsView.getVisibility() == 0 && this.filterTabsView.isAnimatingIndicator()) {
            return;
        }
        int abs = Math.abs(i2 - i) + 1;
        if (i2 != -1) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = viewPage.listView.findViewHolderForAdapterPosition(i2);
            boolean z5 = findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.getItemViewType() == 11;
            this.floatingForceVisible = z5;
            if (z5) {
                hideFloatingButton(false);
            }
        } else {
            this.floatingForceVisible = false;
        }
        if (viewPage.dialogsType == 7 || viewPage.dialogsType == 8) {
            ArrayList<MessagesController.DialogFilter> dialogFilters = getMessagesController().getDialogFilters();
            if (viewPage.selectedType >= 0 && viewPage.selectedType < dialogFilters.size() && (dialogFilters.get(viewPage.selectedType).flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED) == 0 && ((abs > 0 && i2 >= getDialogsArray(this.currentAccount, viewPage.dialogsType, 1, this.dialogsListFrozen).size() - 10) || (abs == 0 && !getMessagesController().isDialogsEndReached(1)))) {
                boolean z6 = !getMessagesController().isDialogsEndReached(1);
                if (z6 || !getMessagesController().isServerDialogsEndReached(1)) {
                    z = z6;
                    z2 = true;
                } else {
                    z = z6;
                    z2 = false;
                }
                if ((abs > 0 || i2 < getDialogsArray(this.currentAccount, viewPage.dialogsType, this.folderId, this.dialogsListFrozen).size() - 10) && (abs != 0 || (!(viewPage.dialogsType == 7 || viewPage.dialogsType == 8) || getMessagesController().isDialogsEndReached(this.folderId)))) {
                    z3 = false;
                    z4 = false;
                } else {
                    boolean z7 = !getMessagesController().isDialogsEndReached(this.folderId);
                    if (z7 || !getMessagesController().isServerDialogsEndReached(this.folderId)) {
                        z4 = z7;
                        z3 = true;
                    } else {
                        z4 = z7;
                        z3 = false;
                    }
                }
                if (!z3 || z2) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda103
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.this.lambda$checkListLoad$74(z3, z4, z2, z);
                        }
                    });
                }
                return;
            }
        }
        z2 = false;
        z = false;
        if (abs > 0) {
        }
        z3 = false;
        z4 = false;
        if (z3) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda103
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$checkListLoad$74(z3, z4, z2, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkListLoad$74(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z) {
            getMessagesController().loadDialogs(this.folderId, -1, 100, z2);
        }
        if (z3) {
            getMessagesController().loadDialogs(1, -1, 100, z4);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0334  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x033a  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0358  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0466  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x048a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onItemClick(View view, int i, RecyclerView.Adapter adapter, float f, float f2) {
        MessageObject messageObject;
        boolean z;
        long j;
        int i2;
        long j2;
        int i3;
        int i4;
        long j3;
        DialogsSearchAdapter dialogsSearchAdapter;
        RightSlidingDialogContainer rightSlidingDialogContainer;
        MessageObject messageObject2;
        int id;
        int i5;
        long j4;
        SearchViewPager searchViewPager;
        TLRPC$Document greetingsSticker;
        SearchViewPager searchViewPager2;
        int i6;
        long j5;
        long j6;
        if (getParentActivity() == null) {
            return;
        }
        boolean z2 = adapter instanceof DialogsAdapter;
        if (z2) {
            DialogsAdapter dialogsAdapter = (DialogsAdapter) adapter;
            int dialogsType = dialogsAdapter.getDialogsType();
            if (dialogsType == 7 || dialogsType == 8) {
                i6 = getMessagesController().selectedDialogFilter[dialogsType == 7 ? (char) 0 : (char) 1].id;
            } else {
                i6 = 0;
            }
            TLObject item = dialogsAdapter.getItem(i);
            if (item instanceof TLRPC$User) {
                j6 = ((TLRPC$User) item).id;
            } else if (item instanceof TLRPC$Dialog) {
                TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) item;
                i2 = tLRPC$Dialog.folder_id;
                if (tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) {
                    if (this.actionBar.isActionModeShowed(null)) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("folderId", ((TLRPC$TL_dialogFolder) tLRPC$Dialog).folder.id);
                    presentFragment(new DialogsActivity(bundle));
                    return;
                }
                j6 = tLRPC$Dialog.id;
                if (this.actionBar.isActionModeShowed(null)) {
                    showOrUpdateActionMode(j6, view);
                    return;
                }
                i3 = i6;
                j2 = j6;
                z = false;
                j = 0;
                i4 = 0;
                j3 = 0;
                messageObject = null;
            } else {
                if (item instanceof TLRPC$TL_recentMeUrlChat) {
                    j5 = ((TLRPC$TL_recentMeUrlChat) item).chat_id;
                } else if (item instanceof TLRPC$TL_recentMeUrlUser) {
                    j6 = ((TLRPC$TL_recentMeUrlUser) item).user_id;
                } else if (item instanceof TLRPC$TL_recentMeUrlChatInvite) {
                    TLRPC$TL_recentMeUrlChatInvite tLRPC$TL_recentMeUrlChatInvite = (TLRPC$TL_recentMeUrlChatInvite) item;
                    TLRPC$ChatInvite tLRPC$ChatInvite = tLRPC$TL_recentMeUrlChatInvite.chat_invite;
                    TLRPC$Chat tLRPC$Chat = tLRPC$ChatInvite.chat;
                    if ((tLRPC$Chat == null && (!tLRPC$ChatInvite.channel || tLRPC$ChatInvite.megagroup)) || (tLRPC$Chat != null && (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$ChatInvite.chat.megagroup))) {
                        String str = tLRPC$TL_recentMeUrlChatInvite.url;
                        int indexOf = str.indexOf(47);
                        if (indexOf > 0) {
                            str = str.substring(indexOf + 1);
                        }
                        showDialog(new JoinGroupAlert(getParentActivity(), tLRPC$ChatInvite, str, this, null));
                        return;
                    }
                    TLRPC$Chat tLRPC$Chat2 = tLRPC$ChatInvite.chat;
                    if (tLRPC$Chat2 == null) {
                        return;
                    }
                    j5 = tLRPC$Chat2.id;
                } else if (item instanceof TLRPC$TL_recentMeUrlStickerSet) {
                    TLRPC$StickerSet tLRPC$StickerSet = ((TLRPC$TL_recentMeUrlStickerSet) item).set.set;
                    TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
                    tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
                    tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
                    showDialog(new StickersAlert(getParentActivity(), this, tLRPC$TL_inputStickerSetID, (TLRPC$TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null));
                    return;
                } else {
                    boolean z3 = item instanceof TLRPC$TL_recentMeUrlUnknown;
                    return;
                }
                j6 = -j5;
            }
            i2 = 0;
            i3 = i6;
            j2 = j6;
            z = false;
            j = 0;
            i4 = 0;
            j3 = 0;
            messageObject = null;
        } else {
            SearchViewPager searchViewPager3 = this.searchViewPager;
            if (searchViewPager3 == null || adapter != (dialogsSearchAdapter = searchViewPager3.dialogsSearchAdapter)) {
                messageObject = null;
                z = false;
                j = 0;
                i2 = 0;
                j2 = 0;
                i3 = 0;
                i4 = 0;
                j3 = 0;
            } else {
                Object item2 = dialogsSearchAdapter.getItem(i);
                z = this.searchViewPager.dialogsSearchAdapter.isGlobalSearch(i);
                if (item2 instanceof TLRPC$User) {
                    TLRPC$User tLRPC$User = (TLRPC$User) item2;
                    j2 = tLRPC$User.id;
                    if (!this.onlySelect) {
                        this.searchDialogId = j2;
                        this.searchObject = tLRPC$User;
                    }
                } else if (item2 instanceof TLRPC$Chat) {
                    TLRPC$Chat tLRPC$Chat3 = (TLRPC$Chat) item2;
                    j2 = -tLRPC$Chat3.id;
                    if (!this.onlySelect) {
                        this.searchDialogId = j2;
                        this.searchObject = tLRPC$Chat3;
                    }
                } else if (item2 instanceof TLRPC$EncryptedChat) {
                    TLRPC$EncryptedChat tLRPC$EncryptedChat = (TLRPC$EncryptedChat) item2;
                    j2 = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                    if (!this.onlySelect) {
                        this.searchDialogId = j2;
                        this.searchObject = tLRPC$EncryptedChat;
                    }
                } else if (item2 instanceof MessageObject) {
                    messageObject2 = (MessageObject) item2;
                    j2 = messageObject2.getDialogId();
                    id = messageObject2.getId();
                    j = ChatObject.isForum(getMessagesController().getChat(Long.valueOf(-j2))) ? MessageObject.getTopicId(messageObject2.currentAccount, messageObject2.messageOwner, true) : 0L;
                    SearchViewPager searchViewPager4 = this.searchViewPager;
                    if (searchViewPager4 != null) {
                        DialogsSearchAdapter dialogsSearchAdapter2 = searchViewPager4.dialogsSearchAdapter;
                        dialogsSearchAdapter2.addHashtagsFromMessage(dialogsSearchAdapter2.getLastSearchString());
                    }
                    if (j2 == 0 && this.actionBar.isActionModeShowed()) {
                        if (this.actionBar.isActionModeShowed("search_dialogs_action_mode") && id == 0 && !z) {
                            showOrUpdateActionMode(j2, view);
                            return;
                        }
                        return;
                    }
                    i4 = id;
                    i3 = 0;
                    j3 = 0;
                    messageObject = messageObject2;
                    i2 = 0;
                } else {
                    if (item2 instanceof String) {
                        String str2 = (String) item2;
                        SearchViewPager searchViewPager5 = this.searchViewPager;
                        if (searchViewPager5 != null && searchViewPager5.dialogsSearchAdapter.isHashtagSearch()) {
                            this.actionBar.openSearchField(str2, false);
                        } else if (!str2.equals("section")) {
                            NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(this, getContext());
                            newContactBottomSheet.setInitialPhoneNumber(str2, true);
                            newContactBottomSheet.show();
                        }
                    } else if (item2 instanceof ContactsController.Contact) {
                        ContactsController.Contact contact = (ContactsController.Contact) item2;
                        AlertsCreator.createContactInviteDialog(this, contact.first_name, contact.last_name, contact.phones.get(0));
                    } else if ((item2 instanceof TLRPC$TL_forumTopic) && (rightSlidingDialogContainer = this.rightSlidingDialogContainer) != null && (rightSlidingDialogContainer.getFragment() instanceof TopicsFragment)) {
                        j2 = ((TopicsFragment) this.rightSlidingDialogContainer.getFragment()).getDialogId();
                        j = ((TLRPC$TL_forumTopic) item2).id;
                        messageObject2 = null;
                        id = 0;
                        if (j2 == 0) {
                        }
                        i4 = id;
                        i3 = 0;
                        j3 = 0;
                        messageObject = messageObject2;
                        i2 = 0;
                    }
                    messageObject2 = null;
                    j = 0;
                    j2 = 0;
                    id = 0;
                    if (j2 == 0) {
                    }
                    i4 = id;
                    i3 = 0;
                    j3 = 0;
                    messageObject = messageObject2;
                    i2 = 0;
                }
                messageObject2 = null;
                j = 0;
                id = 0;
                if (j2 == 0) {
                }
                i4 = id;
                i3 = 0;
                j3 = 0;
                messageObject = messageObject2;
                i2 = 0;
            }
        }
        if (j2 == j3) {
            return;
        }
        if (this.onlySelect) {
            if (validateSlowModeDialog(j2)) {
                if (!getMessagesController().isForum(j2) && (!this.selectedDialogs.isEmpty() || (this.initialDialogsType == 3 && this.selectAlertString != null))) {
                    if (this.selectedDialogs.contains(Long.valueOf(j2)) || checkCanWrite(j2)) {
                        boolean addOrRemoveSelectedDialog = addOrRemoveSelectedDialog(j2, view);
                        SearchViewPager searchViewPager6 = this.searchViewPager;
                        if (searchViewPager6 != null && adapter == searchViewPager6.dialogsSearchAdapter) {
                            this.actionBar.closeSearchField();
                            findAndUpdateCheckBox(j2, addOrRemoveSelectedDialog);
                        }
                        updateSelectedCount();
                        return;
                    }
                    return;
                } else if (this.canSelectTopics && getMessagesController().isForum(j2)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("chat_id", -j2);
                    bundle2.putBoolean("for_select", true);
                    bundle2.putBoolean("forward_to", true);
                    bundle2.putBoolean("quote", this.isQuote);
                    bundle2.putBoolean("reply_to", this.isReplyTo);
                    TopicsFragment topicsFragment = new TopicsFragment(bundle2);
                    topicsFragment.setForwardFromDialogFragment(this);
                    presentFragment(topicsFragment);
                    return;
                } else {
                    didSelectResult(j2, 0, true, false);
                    return;
                }
            }
            return;
        }
        Bundle bundle3 = new Bundle();
        if (DialogObject.isEncryptedDialog(j2)) {
            bundle3.putInt("enc_id", DialogObject.getEncryptedChatId(j2));
        } else if (DialogObject.isUserDialog(j2)) {
            bundle3.putLong("user_id", j2);
        } else {
            if (i4 != 0) {
                i5 = i2;
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j2));
                if (chat != null && chat.migrated_to != null) {
                    bundle3.putLong("migrated_to", j2);
                    j4 = -chat.migrated_to.channel_id;
                    bundle3.putLong("chat_id", -j4);
                    if (i4 != 0) {
                        bundle3.putInt("message_id", i4);
                    } else if (!z) {
                        closeSearch();
                    } else {
                        TLObject tLObject = this.searchObject;
                        if (tLObject != null) {
                            SearchViewPager searchViewPager7 = this.searchViewPager;
                            if (searchViewPager7 != null) {
                                searchViewPager7.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, tLObject);
                            }
                            this.searchObject = null;
                        }
                    }
                    boolean z4 = LocaleController.isRTL && !this.searching && (!AndroidUtilities.isTablet() || i5 == 0) && LiteMode.isEnabled(64);
                    bundle3.putInt("dialog_folder_id", i5);
                    bundle3.putInt("dialog_filter_id", i3);
                    if (!AndroidUtilities.isTablet() && ((!getMessagesController().isForum(j2) || !z4) && this.openedDialogId.dialogId == j2 && ((searchViewPager2 = this.searchViewPager) == null || adapter != searchViewPager2.dialogsSearchAdapter))) {
                        if (getParentActivity() instanceof LaunchActivity) {
                            LaunchActivity launchActivity = (LaunchActivity) getParentActivity();
                            List<BaseFragment> fragmentStack = launchActivity.getRightActionBarLayout().getFragmentStack();
                            if (fragmentStack.isEmpty()) {
                                return;
                            }
                            if (fragmentStack.size() == 1 && (fragmentStack.get(fragmentStack.size() - 1) instanceof ChatActivity)) {
                                ((ChatActivity) fragmentStack.get(fragmentStack.size() - 1)).onPageDownClicked();
                                return;
                            } else if (fragmentStack.size() == 2) {
                                launchActivity.getRightActionBarLayout().closeLastFragment();
                                return;
                            } else if (getParentActivity() instanceof LaunchActivity) {
                                fragmentStack.clear();
                                fragmentStack.add(fragmentStack.get(0));
                                launchActivity.getRightActionBarLayout().rebuildFragments(1);
                                return;
                            } else {
                                return;
                            }
                        }
                        return;
                    }
                    searchViewPager = this.searchViewPager;
                    if (searchViewPager != null && searchViewPager.actionModeShowing()) {
                        this.searchViewPager.hideActionMode();
                    }
                    if (j2 != getUserConfig().getClientUserId() && getMessagesController().savedViewAsChats) {
                        Bundle bundle4 = new Bundle();
                        bundle4.putLong("dialog_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                        bundle4.putInt("type", 0);
                        bundle4.putInt("start_from", 11);
                        if (this.sharedMediaPreloader == null) {
                            this.sharedMediaPreloader = new SharedMediaLayout.SharedMediaPreloader(this);
                        }
                        presentFragment(new MediaActivity(bundle4, this.sharedMediaPreloader));
                        return;
                    } else if (this.searchString != null) {
                        if (getMessagesController().checkCanOpenChat(bundle3, this)) {
                            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                            presentFragment(highlightFoundQuote(new ChatActivity(bundle3), messageObject));
                            return;
                        }
                        return;
                    } else {
                        this.slowedReloadAfterDialogClick = true;
                        if (getMessagesController().checkCanOpenChat(bundle3, this)) {
                            TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(-j2));
                            TLRPC$Dialog dialog = getMessagesController().getDialog(j2);
                            boolean z5 = dialog != null && dialog.view_forum_as_messages;
                            if (chat2 != null && chat2.forum && j == 0) {
                                if (!LiteMode.isEnabled(64)) {
                                    if (z5) {
                                        presentFragment(highlightFoundQuote(new ChatActivity(bundle3), messageObject));
                                        return;
                                    } else {
                                        presentFragment(new TopicsFragment(bundle3));
                                        return;
                                    }
                                } else if (!z4) {
                                    if (z5) {
                                        presentFragment(highlightFoundQuote(new ChatActivity(bundle3), messageObject));
                                        return;
                                    } else {
                                        presentFragment(new TopicsFragment(bundle3));
                                        return;
                                    }
                                } else if (this.searching) {
                                    return;
                                } else {
                                    if (z5) {
                                        presentFragment(highlightFoundQuote(new ChatActivity(bundle3), messageObject));
                                        return;
                                    }
                                    BaseFragment baseFragment = this.rightSlidingDialogContainer.currentFragment;
                                    if (baseFragment != null && ((TopicsFragment) baseFragment).getDialogId() == j2) {
                                        this.rightSlidingDialogContainer.lambda$presentFragment$1();
                                    } else {
                                        this.viewPages[0].listView.prepareSelectorForAnimation();
                                        TopicsFragment topicsFragment2 = new TopicsFragment(bundle3);
                                        topicsFragment2.parentDialogsActivity = this;
                                        this.rightSlidingDialogContainer.presentFragment(getParentLayout(), topicsFragment2);
                                    }
                                    SearchViewPager searchViewPager8 = this.searchViewPager;
                                    if (searchViewPager8 != null) {
                                        searchViewPager8.updateTabs();
                                        return;
                                    }
                                    return;
                                }
                            }
                            ChatActivity chatActivity = new ChatActivity(bundle3);
                            if (j != 0) {
                                ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(j2, j));
                            }
                            if (z2 && DialogObject.isUserDialog(j2) && getMessagesController().dialogs_dict.get(j2) == null && (greetingsSticker = getMediaDataController().getGreetingsSticker()) != null) {
                                chatActivity.setPreloadedSticker(greetingsSticker, true);
                            }
                            if (AndroidUtilities.isTablet()) {
                                RightSlidingDialogContainer rightSlidingDialogContainer2 = this.rightSlidingDialogContainer;
                                if (rightSlidingDialogContainer2.currentFragment != null) {
                                    rightSlidingDialogContainer2.lambda$presentFragment$1();
                                }
                            }
                            presentFragment(highlightFoundQuote(chatActivity, messageObject));
                            return;
                        }
                        return;
                    }
                }
            } else {
                i5 = i2;
            }
            j4 = j2;
            bundle3.putLong("chat_id", -j4);
            if (i4 != 0) {
            }
            if (LocaleController.isRTL) {
            }
            bundle3.putInt("dialog_folder_id", i5);
            bundle3.putInt("dialog_filter_id", i3);
            if (!AndroidUtilities.isTablet()) {
            }
            searchViewPager = this.searchViewPager;
            if (searchViewPager != null) {
                this.searchViewPager.hideActionMode();
            }
            if (j2 != getUserConfig().getClientUserId()) {
            }
            if (this.searchString != null) {
            }
        }
        i5 = i2;
        if (i4 != 0) {
        }
        if (LocaleController.isRTL) {
        }
        bundle3.putInt("dialog_folder_id", i5);
        bundle3.putInt("dialog_filter_id", i3);
        if (!AndroidUtilities.isTablet()) {
        }
        searchViewPager = this.searchViewPager;
        if (searchViewPager != null) {
        }
        if (j2 != getUserConfig().getClientUserId()) {
        }
        if (this.searchString != null) {
        }
    }

    public static ChatActivity highlightFoundQuote(ChatActivity chatActivity, MessageObject messageObject) {
        CharSequence charSequence;
        boolean z;
        if (messageObject != null && messageObject.hasHighlightedWords()) {
            try {
                if (!TextUtils.isEmpty(messageObject.caption)) {
                    charSequence = messageObject.caption;
                } else {
                    charSequence = messageObject.messageText;
                }
                CharSequence highlightText = AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                if (highlightText instanceof SpannableStringBuilder) {
                    SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) highlightText;
                    ForegroundColorSpanThemable[] foregroundColorSpanThemableArr = (ForegroundColorSpanThemable[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ForegroundColorSpanThemable.class);
                    if (foregroundColorSpanThemableArr.length > 0) {
                        int spanStart = spannableStringBuilder.getSpanStart(foregroundColorSpanThemableArr[0]);
                        int spanEnd = spannableStringBuilder.getSpanEnd(foregroundColorSpanThemableArr[0]);
                        for (int i = 1; i < foregroundColorSpanThemableArr.length; i++) {
                            int spanStart2 = spannableStringBuilder.getSpanStart(foregroundColorSpanThemableArr[i]);
                            int spanStart3 = spannableStringBuilder.getSpanStart(foregroundColorSpanThemableArr[i]);
                            if (spanStart2 != spanEnd) {
                                if (spanStart2 > spanEnd) {
                                    int i2 = spanEnd;
                                    while (true) {
                                        if (i2 > spanStart2) {
                                            z = true;
                                            break;
                                        } else if (!Character.isWhitespace(spannableStringBuilder.charAt(i2))) {
                                            z = false;
                                            break;
                                        } else {
                                            i2++;
                                        }
                                    }
                                    if (!z) {
                                    }
                                }
                            }
                            spanEnd = spanStart3;
                        }
                        chatActivity.setHighlightQuote(messageObject.getRealId(), charSequence.subSequence(spanStart, spanEnd).toString(), spanStart);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return chatActivity;
    }

    public void setOpenedDialogId(long j, long j2) {
        MessagesStorage.TopicKey topicKey = this.openedDialogId;
        topicKey.dialogId = j;
        topicKey.topicId = j2;
        ViewPage[] viewPageArr = this.viewPages;
        if (viewPageArr == null) {
            return;
        }
        for (ViewPage viewPage : viewPageArr) {
            if (viewPage.isDefaultDialogType() && AndroidUtilities.isTablet()) {
                viewPage.dialogsAdapter.setOpenedDialogId(this.openedDialogId.dialogId);
            }
        }
        updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onItemLongClick(RecyclerListView recyclerListView, View view, int i, float f, float f2, int i2, RecyclerView.Adapter adapter) {
        TLRPC$Dialog tLRPC$Dialog;
        DialogsSearchAdapter dialogsSearchAdapter;
        DialogsSearchAdapter dialogsSearchAdapter2;
        final long makeEncryptedDialogId;
        if (getParentActivity() != null && !(view instanceof DialogsHintCell)) {
            if (!this.actionBar.isActionModeShowed() && !AndroidUtilities.isTablet() && !this.onlySelect && (view instanceof DialogCell)) {
                DialogCell dialogCell = (DialogCell) view;
                if (!getMessagesController().isForum(dialogCell.getDialogId()) && !this.rightSlidingDialogContainer.hasFragment() && dialogCell.isPointInsideAvatar(f, f2)) {
                    return showChatPreview(dialogCell);
                }
            }
            RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment()) {
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager != null && adapter == (dialogsSearchAdapter2 = searchViewPager.dialogsSearchAdapter)) {
                    Object item = dialogsSearchAdapter2.getItem(i);
                    if (!this.searchViewPager.dialogsSearchAdapter.isSearchWas()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString("ClearSearchSingleAlertTitle", R.string.ClearSearchSingleAlertTitle));
                        if (item instanceof TLRPC$Chat) {
                            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) item;
                            builder.setMessage(LocaleController.formatString("ClearSearchSingleChatAlertText", R.string.ClearSearchSingleChatAlertText, tLRPC$Chat.title));
                            makeEncryptedDialogId = -tLRPC$Chat.id;
                        } else if (item instanceof TLRPC$User) {
                            TLRPC$User tLRPC$User = (TLRPC$User) item;
                            if (tLRPC$User.id == getUserConfig().clientUserId) {
                                builder.setMessage(LocaleController.formatString("ClearSearchSingleChatAlertText", R.string.ClearSearchSingleChatAlertText, LocaleController.getString("SavedMessages", R.string.SavedMessages)));
                            } else {
                                builder.setMessage(LocaleController.formatString("ClearSearchSingleUserAlertText", R.string.ClearSearchSingleUserAlertText, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name)));
                            }
                            makeEncryptedDialogId = tLRPC$User.id;
                        } else if (!(item instanceof TLRPC$EncryptedChat)) {
                            return false;
                        } else {
                            TLRPC$EncryptedChat tLRPC$EncryptedChat = (TLRPC$EncryptedChat) item;
                            TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$EncryptedChat.user_id));
                            builder.setMessage(LocaleController.formatString("ClearSearchSingleUserAlertText", R.string.ClearSearchSingleUserAlertText, ContactsController.formatName(user.first_name, user.last_name)));
                            makeEncryptedDialogId = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                        }
                        builder.setPositiveButton(LocaleController.getString("ClearSearchRemove", R.string.ClearSearchRemove), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda16
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i3) {
                                DialogsActivity.this.lambda$onItemLongClick$75(makeEncryptedDialogId, dialogInterface, i3);
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        AlertDialog create = builder.create();
                        showDialog(create);
                        TextView textView = (TextView) create.getButton(-1);
                        if (textView != null) {
                            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                        }
                        return true;
                    }
                }
                SearchViewPager searchViewPager2 = this.searchViewPager;
                if (searchViewPager2 != null && adapter == (dialogsSearchAdapter = searchViewPager2.dialogsSearchAdapter)) {
                    if (this.onlySelect) {
                        onItemClick(view, i, adapter, f, f2);
                        return false;
                    }
                    long dialogId = (!(view instanceof ProfileSearchCell) || dialogsSearchAdapter.isGlobalSearch(i)) ? 0L : ((ProfileSearchCell) view).getDialogId();
                    if (dialogId != 0) {
                        showOrUpdateActionMode(dialogId, view);
                        return true;
                    }
                    return false;
                }
                ArrayList<TLRPC$Dialog> dialogsArray = getDialogsArray(this.currentAccount, i2, this.folderId, this.dialogsListFrozen);
                int fixPosition = ((DialogsAdapter) adapter).fixPosition(i);
                if (fixPosition < 0 || fixPosition >= dialogsArray.size() || (tLRPC$Dialog = dialogsArray.get(fixPosition)) == null) {
                    return false;
                }
                if (this.onlySelect) {
                    int i3 = this.initialDialogsType;
                    if ((i3 == 3 || i3 == 10) && validateSlowModeDialog(tLRPC$Dialog.id)) {
                        addOrRemoveSelectedDialog(tLRPC$Dialog.id, view);
                        updateSelectedCount();
                        return true;
                    }
                    return false;
                } else if (tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) {
                    onArchiveLongPress(view);
                    return false;
                } else if (this.actionBar.isActionModeShowed() && isDialogPinned(tLRPC$Dialog)) {
                    return false;
                } else {
                    showOrUpdateActionMode(tLRPC$Dialog.id, view);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemLongClick$75(long j, DialogInterface dialogInterface, int i) {
        this.searchViewPager.dialogsSearchAdapter.removeRecentSearch(j);
    }

    private void onArchiveLongPress(View view) {
        int i;
        String str;
        view.performHapticFeedback(0, 2);
        BottomSheet.Builder builder = new BottomSheet.Builder(getParentActivity());
        boolean z = getMessagesStorage().getArchiveUnreadCount() != 0;
        int[] iArr = new int[2];
        iArr[0] = z ? R.drawable.msg_markread : 0;
        iArr[1] = SharedConfig.archiveHidden ? R.drawable.chats_pin : R.drawable.chats_unpin;
        CharSequence[] charSequenceArr = new CharSequence[2];
        charSequenceArr[0] = z ? LocaleController.getString("MarkAllAsRead", R.string.MarkAllAsRead) : null;
        if (SharedConfig.archiveHidden) {
            i = R.string.PinInTheList;
            str = "PinInTheList";
        } else {
            i = R.string.HideAboveTheList;
            str = "HideAboveTheList";
        }
        charSequenceArr[1] = LocaleController.getString(str, i);
        builder.setItems(charSequenceArr, iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda15
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                DialogsActivity.this.lambda$onArchiveLongPress$76(dialogInterface, i2);
            }
        });
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onArchiveLongPress$76(DialogInterface dialogInterface, int i) {
        if (i == 0) {
            getMessagesStorage().readAllDialogs(1);
        } else if (i != 1 || this.viewPages == null) {
        } else {
            int i2 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i2 >= viewPageArr.length) {
                    return;
                }
                if (viewPageArr[i2].dialogsType == 0 && this.viewPages[i2].getVisibility() == 0) {
                    this.viewPages[i2].listView.toggleArchiveHidden(true, findArchiveDialogCell(this.viewPages[i2]));
                }
                i2++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DialogCell findArchiveDialogCell(ViewPage viewPage) {
        DialogsRecyclerView dialogsRecyclerView = viewPage.listView;
        for (int i = 0; i < dialogsRecyclerView.getChildCount(); i++) {
            View childAt = dialogsRecyclerView.getChildAt(i);
            if (childAt instanceof DialogCell) {
                DialogCell dialogCell = (DialogCell) childAt;
                if (dialogCell.isFolderCell()) {
                    return dialogCell;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0106, code lost:
        if ((r6.alwaysShow.size() + r22.size()) > 100) goto L32;
     */
    /* JADX WARN: Type inference failed for: r0v103 */
    /* JADX WARN: Type inference failed for: r0v20 */
    /* JADX WARN: Type inference failed for: r0v21, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean showChatPreview(final DialogCell dialogCell) {
        long j;
        TLRPC$Chat chat;
        int[] iArr;
        Bundle bundle;
        ArrayList arrayList;
        int i;
        View view;
        MessagesController.DialogFilter dialogFilter;
        ChatActivity[] chatActivityArr;
        ?? r0;
        boolean z;
        int i2;
        int i3;
        int i4;
        int i5;
        boolean z2;
        boolean z3;
        int i6;
        ScrollView scrollView;
        int i7;
        boolean z4;
        LinearLayout linearLayout;
        Bundle bundle2;
        int i8;
        ActionBarMenuSubItem actionBarMenuSubItem;
        ScrollView scrollView2;
        ArrayList arrayList2;
        LinearLayout linearLayout2;
        int[] iArr2;
        boolean z5 = false;
        if (dialogCell.isDialogFolder()) {
            if (dialogCell.getCurrentDialogFolderId() == 1) {
                onArchiveLongPress(dialogCell);
            }
            return false;
        }
        final long dialogId = dialogCell.getDialogId();
        Bundle bundle3 = new Bundle();
        int messageId = dialogCell.getMessageId();
        if (DialogObject.isEncryptedDialog(dialogId)) {
            return false;
        }
        if (DialogObject.isUserDialog(dialogId)) {
            bundle3.putLong("user_id", dialogId);
        } else {
            if (messageId == 0 || (chat = getMessagesController().getChat(Long.valueOf(-dialogId))) == null || chat.migrated_to == null) {
                j = dialogId;
            } else {
                bundle3.putLong("migrated_to", dialogId);
                j = -chat.migrated_to.channel_id;
            }
            bundle3.putLong("chat_id", -j);
        }
        if (messageId != 0) {
            bundle3.putInt("message_id", messageId);
        }
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(Long.valueOf(dialogId));
        boolean z6 = getMessagesController().filtersEnabled && getMessagesController().dialogFiltersLoaded && getMessagesController().dialogFilters != null && getMessagesController().dialogFilters.size() > 0;
        final ActionBarPopupWindow.ActionBarPopupWindowLayout[] actionBarPopupWindowLayoutArr = new ActionBarPopupWindow.ActionBarPopupWindowLayout[1];
        int[] iArr3 = new int[1];
        if (z6) {
            LinearLayout linearLayout3 = new LinearLayout(getParentActivity());
            linearLayout3.setOrientation(1);
            ScrollView scrollView3 = new ScrollView(this, getParentActivity()) { // from class: org.telegram.ui.DialogsActivity.41
                @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i9, int i10) {
                    super.onMeasure(i9, View.MeasureSpec.makeMeasureSpec((int) Math.min(View.MeasureSpec.getSize(i10), Math.min(AndroidUtilities.displaySize.y * 0.35f, AndroidUtilities.dp(400.0f))), View.MeasureSpec.getMode(i10)));
                }
            };
            LinearLayout linearLayout4 = new LinearLayout(getParentActivity());
            linearLayout4.setOrientation(1);
            scrollView3.addView(linearLayout4);
            int size = getMessagesController().dialogFilters.size();
            int i9 = 0;
            ActionBarMenuSubItem actionBarMenuSubItem2 = null;
            while (i9 < size) {
                final MessagesController.DialogFilter dialogFilter2 = getMessagesController().dialogFilters.get(i9);
                if (dialogFilter2.isDefault()) {
                    scrollView = scrollView3;
                } else {
                    final boolean includesDialog = dialogFilter2.includesDialog(AccountInstance.getInstance(this.currentAccount), dialogId);
                    scrollView = scrollView3;
                    final ArrayList<Long> dialogsCount = FiltersListBottomSheet.getDialogsCount(this, dialogFilter2, arrayList3, true, z5);
                    if (includesDialog) {
                    }
                    ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem((Context) getParentActivity(), 2, false, false, (Theme.ResourcesProvider) null);
                    actionBarMenuSubItem3.setChecked(includesDialog);
                    i7 = i9;
                    actionBarMenuSubItem3.setTextAndIcon(Emoji.replaceEmoji(dialogFilter2.name, null, false), 0, new FolderDrawable(getContext(), R.drawable.msg_folders, dialogFilter2.color));
                    actionBarMenuSubItem3.setMinimumWidth(160);
                    z4 = z6;
                    linearLayout = linearLayout3;
                    bundle2 = bundle3;
                    i8 = size;
                    actionBarMenuSubItem = actionBarMenuSubItem3;
                    scrollView2 = scrollView;
                    arrayList2 = arrayList3;
                    linearLayout2 = linearLayout4;
                    iArr2 = iArr3;
                    actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda52
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            DialogsActivity.this.lambda$showChatPreview$77(includesDialog, dialogsCount, dialogFilter2, dialogId, view2);
                        }
                    });
                    linearLayout2.addView(actionBarMenuSubItem);
                    i9 = i7 + 1;
                    actionBarMenuSubItem2 = actionBarMenuSubItem;
                    linearLayout4 = linearLayout2;
                    linearLayout3 = linearLayout;
                    z6 = z4;
                    size = i8;
                    bundle3 = bundle2;
                    arrayList3 = arrayList2;
                    scrollView3 = scrollView2;
                    iArr3 = iArr2;
                    z5 = false;
                }
                i8 = size;
                iArr2 = iArr3;
                arrayList2 = arrayList3;
                z4 = z6;
                actionBarMenuSubItem = actionBarMenuSubItem2;
                scrollView2 = scrollView;
                linearLayout = linearLayout3;
                i7 = i9;
                bundle2 = bundle3;
                linearLayout2 = linearLayout4;
                i9 = i7 + 1;
                actionBarMenuSubItem2 = actionBarMenuSubItem;
                linearLayout4 = linearLayout2;
                linearLayout3 = linearLayout;
                z6 = z4;
                size = i8;
                bundle3 = bundle2;
                arrayList3 = arrayList2;
                scrollView3 = scrollView2;
                iArr3 = iArr2;
                z5 = false;
            }
            ScrollView scrollView4 = scrollView3;
            iArr = iArr3;
            bundle = bundle3;
            arrayList = arrayList3;
            boolean z7 = z6;
            ActionBarMenuSubItem actionBarMenuSubItem4 = actionBarMenuSubItem2;
            i = 8;
            LinearLayout linearLayout5 = linearLayout3;
            LinearLayout linearLayout6 = linearLayout4;
            if (actionBarMenuSubItem4 != null) {
                actionBarMenuSubItem4.updateSelectorBackground(false, true);
            }
            if (linearLayout6.getChildCount() <= 0) {
                view = linearLayout5;
                z6 = false;
            } else {
                ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(getParentActivity(), getResourceProvider(), Theme.key_actionBarDefaultSubmenuSeparator);
                gapView.setTag(R.id.fit_width_tag, 1);
                ActionBarMenuSubItem actionBarMenuSubItem5 = new ActionBarMenuSubItem(getParentActivity(), true, false);
                actionBarMenuSubItem5.setTextAndIcon(LocaleController.getString("Back", R.string.Back), R.drawable.ic_ab_back);
                actionBarMenuSubItem5.setMinimumWidth(160);
                actionBarMenuSubItem5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda53
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        DialogsActivity.lambda$showChatPreview$78(actionBarPopupWindowLayoutArr, view2);
                    }
                });
                linearLayout5.addView(actionBarMenuSubItem5);
                linearLayout5.addView(gapView, LayoutHelper.createLinear(-1, 8));
                linearLayout5.addView(scrollView4);
                view = linearLayout5;
                z6 = z7;
            }
        } else {
            iArr = iArr3;
            bundle = bundle3;
            arrayList = arrayList3;
            i = 8;
            view = null;
        }
        final ChatActivity[] chatActivityArr2 = new ChatActivity[1];
        actionBarPopupWindowLayoutArr[0] = new ActionBarPopupWindow.ActionBarPopupWindowLayout(getParentActivity(), R.drawable.popup_fixed_alert2, getResourceProvider(), z6 ? 3 : 2);
        if (z6) {
            final int[] iArr4 = iArr;
            iArr4[0] = actionBarPopupWindowLayoutArr[0].addViewToSwipeBack(view);
            ActionBarMenuSubItem actionBarMenuSubItem6 = new ActionBarMenuSubItem(getParentActivity(), true, false);
            actionBarMenuSubItem6.setTextAndIcon(LocaleController.getString("FilterAddTo", R.string.FilterAddTo), R.drawable.msg_addfolder);
            actionBarMenuSubItem6.setMinimumWidth(160);
            actionBarMenuSubItem6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda54
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.lambda$showChatPreview$79(actionBarPopupWindowLayoutArr, iArr4, view2);
                }
            });
            actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem6);
            actionBarPopupWindowLayoutArr[0].getSwipeBack().setOnHeightUpdateListener(new PopupSwipeBackLayout.IntCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda131
                @Override // org.telegram.ui.Components.PopupSwipeBackLayout.IntCallback
                public final void run(int i10) {
                    DialogsActivity.lambda$showChatPreview$80(chatActivityArr2, i10);
                }
            });
        }
        ActionBarMenuSubItem actionBarMenuSubItem7 = new ActionBarMenuSubItem(getParentActivity(), true, false);
        if (dialogCell.getHasUnread()) {
            actionBarMenuSubItem7.setTextAndIcon(LocaleController.getString("MarkAsRead", R.string.MarkAsRead), R.drawable.msg_markread);
        } else {
            actionBarMenuSubItem7.setTextAndIcon(LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread), R.drawable.msg_markunread);
        }
        actionBarMenuSubItem7.setMinimumWidth(160);
        actionBarMenuSubItem7.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda51
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                DialogsActivity.this.lambda$showChatPreview$81(dialogCell, dialogId, view2);
            }
        });
        actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem7);
        boolean[] zArr = new boolean[1];
        zArr[0] = true;
        final TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(dialogId);
        boolean z8 = (this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == i) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null));
        if (z8) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == i ? (char) 1 : (char) 0];
        } else {
            dialogFilter = null;
        }
        if (isDialogPinned(tLRPC$Dialog)) {
            chatActivityArr = chatActivityArr2;
            r0 = 0;
        } else {
            ArrayList<TLRPC$Dialog> dialogs = getMessagesController().getDialogs(this.folderId);
            int size2 = dialogs.size();
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            while (true) {
                if (i10 >= size2) {
                    chatActivityArr = chatActivityArr2;
                    break;
                }
                TLRPC$Dialog tLRPC$Dialog2 = dialogs.get(i10);
                if (!(tLRPC$Dialog2 instanceof TLRPC$TL_dialogFolder)) {
                    if (isDialogPinned(tLRPC$Dialog2)) {
                        if (DialogObject.isEncryptedDialog(tLRPC$Dialog2.id)) {
                            i12++;
                        } else {
                            i11++;
                        }
                    } else {
                        i6 = size2;
                        chatActivityArr = chatActivityArr2;
                        if (!getMessagesController().isPromoDialog(tLRPC$Dialog2.id, false)) {
                            break;
                        }
                        i10++;
                        size2 = i6;
                        chatActivityArr2 = chatActivityArr;
                    }
                }
                i6 = size2;
                chatActivityArr = chatActivityArr2;
                i10++;
                size2 = i6;
                chatActivityArr2 = chatActivityArr;
            }
            if (tLRPC$Dialog == null || isDialogPinned(tLRPC$Dialog)) {
                i2 = 0;
                i3 = 0;
                i4 = 0;
            } else {
                boolean isEncryptedDialog = DialogObject.isEncryptedDialog(dialogId);
                int i13 = !isEncryptedDialog ? 1 : 0;
                if (dialogFilter == null || !dialogFilter.alwaysShow.contains(Long.valueOf(dialogId))) {
                    i4 = i13;
                    i3 = isEncryptedDialog ? 1 : 0;
                    i2 = 0;
                } else {
                    i4 = i13;
                    i3 = isEncryptedDialog ? 1 : 0;
                    i2 = 1;
                }
            }
            if (z8 && dialogFilter != null) {
                i5 = 100 - dialogFilter.alwaysShow.size();
            } else if (this.folderId != 0 || dialogFilter != null) {
                if (getUserConfig().isPremium()) {
                    i5 = getMessagesController().maxFolderPinnedDialogsCountPremium;
                } else {
                    i5 = getMessagesController().maxFolderPinnedDialogsCountDefault;
                }
            } else if (getUserConfig().isPremium()) {
                i5 = getMessagesController().maxPinnedDialogsCountPremium;
            } else {
                i5 = getMessagesController().maxPinnedDialogsCountDefault;
            }
            if (i3 + i12 > i5 || (i4 + i11) - i2 > i5) {
                z2 = false;
                z3 = false;
            } else {
                z2 = false;
                z3 = true;
            }
            zArr[z2 ? 1 : 0] = z3;
            r0 = z2;
        }
        if (zArr[r0]) {
            ActionBarMenuSubItem actionBarMenuSubItem8 = new ActionBarMenuSubItem(getParentActivity(), r0, r0);
            if (isDialogPinned(tLRPC$Dialog)) {
                actionBarMenuSubItem8.setTextAndIcon(LocaleController.getString("UnpinMessage", R.string.UnpinMessage), R.drawable.msg_unpin);
            } else {
                actionBarMenuSubItem8.setTextAndIcon(LocaleController.getString("PinMessage", R.string.PinMessage), R.drawable.msg_pin);
            }
            actionBarMenuSubItem8.setMinimumWidth(160);
            final MessagesController.DialogFilter dialogFilter3 = dialogFilter;
            actionBarMenuSubItem8.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda50
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$showChatPreview$83(dialogFilter3, tLRPC$Dialog, dialogId, view2);
                }
            });
            actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem8);
        }
        if (DialogObject.isUserDialog(dialogId) && UserObject.isUserSelf(getMessagesController().getUser(Long.valueOf(dialogId)))) {
            z = false;
        } else {
            ActionBarMenuSubItem actionBarMenuSubItem9 = new ActionBarMenuSubItem(getParentActivity(), false, false);
            if (!getMessagesController().isDialogMuted(dialogId, 0L)) {
                actionBarMenuSubItem9.setTextAndIcon(LocaleController.getString("Mute", R.string.Mute), R.drawable.msg_mute);
            } else {
                actionBarMenuSubItem9.setTextAndIcon(LocaleController.getString("Unmute", R.string.Unmute), R.drawable.msg_unmute);
            }
            actionBarMenuSubItem9.setMinimumWidth(160);
            actionBarMenuSubItem9.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda46
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    DialogsActivity.this.lambda$showChatPreview$84(dialogId, view2);
                }
            });
            z = false;
            actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem9);
        }
        ActionBarMenuSubItem actionBarMenuSubItem10 = new ActionBarMenuSubItem(getParentActivity(), z, true);
        actionBarMenuSubItem10.setIconColor(getThemedColor(Theme.key_text_RedRegular));
        int i14 = Theme.key_text_RedBold;
        actionBarMenuSubItem10.setTextColor(getThemedColor(i14));
        actionBarMenuSubItem10.setSelectorColor(Theme.multAlpha(getThemedColor(i14), 0.12f));
        actionBarMenuSubItem10.setTextAndIcon(LocaleController.getString("Delete", R.string.Delete), R.drawable.msg_delete);
        actionBarMenuSubItem10.setMinimumWidth(160);
        final ArrayList arrayList4 = arrayList;
        actionBarMenuSubItem10.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda49
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                DialogsActivity.this.lambda$showChatPreview$85(arrayList4, view2);
            }
        });
        actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem10);
        Bundle bundle4 = bundle;
        if (getMessagesController().checkCanOpenChat(bundle4, this)) {
            if (this.searchString != null) {
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            }
            prepareBlurBitmap();
            this.parentLayout.setHighlightActionButtons(true);
            Point point = AndroidUtilities.displaySize;
            if (point.x > point.y) {
                BaseFragment chatActivity = new ChatActivity(bundle4);
                chatActivityArr[0] = chatActivity;
                presentFragmentAsPreview(chatActivity);
                return true;
            }
            BaseFragment chatActivity2 = new ChatActivity(bundle4);
            chatActivityArr[0] = chatActivity2;
            presentFragmentAsPreviewWithMenu(chatActivity2, actionBarPopupWindowLayoutArr[0]);
            if (chatActivityArr[0] != null) {
                chatActivityArr[0].allowExpandPreviewByClick = true;
                try {
                    chatActivityArr[0].getAvatarContainer().getAvatarImageView().performAccessibilityAction(64, null);
                    return true;
                } catch (Exception unused) {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$77(boolean z, ArrayList arrayList, MessagesController.DialogFilter dialogFilter, long j, View view) {
        if (!z) {
            if (!arrayList.isEmpty()) {
                for (int i = 0; i < arrayList.size(); i++) {
                    dialogFilter.neverShow.remove(arrayList.get(i));
                }
                dialogFilter.alwaysShow.addAll(arrayList);
                FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
            }
            getUndoView().showWithAction(j, 20, Integer.valueOf(arrayList.size()), dialogFilter, (Runnable) null, (Runnable) null);
        } else {
            dialogFilter.alwaysShow.remove(Long.valueOf(j));
            dialogFilter.neverShow.add(Long.valueOf(j));
            FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
            getUndoView().showWithAction(j, 21, Integer.valueOf(arrayList.size()), dialogFilter, (Runnable) null, (Runnable) null);
        }
        hideActionMode(true);
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showChatPreview$78(ActionBarPopupWindow.ActionBarPopupWindowLayout[] actionBarPopupWindowLayoutArr, View view) {
        if (actionBarPopupWindowLayoutArr[0] != null) {
            actionBarPopupWindowLayoutArr[0].getSwipeBack().closeForeground();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showChatPreview$79(ActionBarPopupWindow.ActionBarPopupWindowLayout[] actionBarPopupWindowLayoutArr, int[] iArr, View view) {
        actionBarPopupWindowLayoutArr[0].getSwipeBack().openForeground(iArr[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showChatPreview$80(ChatActivity[] chatActivityArr, int i) {
        if (chatActivityArr[0] == null || chatActivityArr[0].getFragmentView() == null || !chatActivityArr[0].isInPreviewMode()) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = chatActivityArr[0].getFragmentView().getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = AndroidUtilities.dp(48.0f) + i;
            chatActivityArr[0].getFragmentView().setLayoutParams(layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$81(DialogCell dialogCell, long j, View view) {
        if (dialogCell.getHasUnread()) {
            markAsRead(j);
        } else {
            markAsUnread(j);
        }
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$83(final MessagesController.DialogFilter dialogFilter, final TLRPC$Dialog tLRPC$Dialog, final long j, View view) {
        finishPreviewFragment();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda95
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$showChatPreview$82(dialogFilter, tLRPC$Dialog, j);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$82(MessagesController.DialogFilter dialogFilter, TLRPC$Dialog tLRPC$Dialog, long j) {
        int i;
        ArrayList<TLRPC$InputDialogPeer> arrayList;
        int i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (dialogFilter == null || !isDialogPinned(tLRPC$Dialog)) {
            i = ConnectionsManager.DEFAULT_DATACENTER_ID;
        } else {
            int size = dialogFilter.pinnedDialogs.size();
            for (int i3 = 0; i3 < size; i3++) {
                i2 = Math.min(i2, dialogFilter.pinnedDialogs.valueAt(i3));
            }
            i = i2 - this.canPinCount;
        }
        TLRPC$EncryptedChat encryptedChat = DialogObject.isEncryptedDialog(j) ? getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) : null;
        UndoView undoView = getUndoView();
        if (undoView == null) {
            return;
        }
        if (!isDialogPinned(tLRPC$Dialog)) {
            pinDialog(j, true, dialogFilter, i, true);
            undoView.showWithAction(0L, 78, (Object) 1, (Object) 1600, (Runnable) null, (Runnable) null);
            if (dialogFilter != null) {
                if (encryptedChat != null) {
                    if (!dialogFilter.alwaysShow.contains(Long.valueOf(encryptedChat.user_id))) {
                        dialogFilter.alwaysShow.add(Long.valueOf(encryptedChat.user_id));
                    }
                } else if (!dialogFilter.alwaysShow.contains(Long.valueOf(j))) {
                    dialogFilter.alwaysShow.add(Long.valueOf(j));
                }
            }
        } else {
            pinDialog(j, false, dialogFilter, i, true);
            undoView.showWithAction(0L, 79, (Object) 1, (Object) 1600, (Runnable) null, (Runnable) null);
        }
        if (dialogFilter != null) {
            arrayList = null;
            FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
        } else {
            arrayList = null;
        }
        getMessagesController().reorderPinnedDialogs(this.folderId, arrayList, 0L);
        updateCounters(true);
        if (this.viewPages != null) {
            int i4 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i4 >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i4].dialogsAdapter.onReorderStateChanged(false);
                i4++;
            }
        }
        updateVisibleRows(MessagesController.UPDATE_MASK_REORDER | MessagesController.UPDATE_MASK_CHECK);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$84(long j, View view) {
        boolean isDialogMuted = getMessagesController().isDialogMuted(j, 0L);
        if (!isDialogMuted) {
            getNotificationsController().setDialogNotificationsSettings(j, 0L, 3);
        } else {
            getNotificationsController().setDialogNotificationsSettings(j, 0L, 4);
        }
        BulletinFactory.createMuteBulletin(this, !isDialogMuted, null).show();
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$85(ArrayList arrayList, View view) {
        performSelectedDialogsAction(arrayList, R.styleable.AppCompatTheme_textAppearanceLargePopupMenu, false, false);
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFloatingButtonOffset() {
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            frameLayout.setTranslationY((this.floatingButtonTranslation - this.floatingButtonPanOffset) - (Math.max(this.additionalFloatingTranslation, this.additionalFloatingTranslation2) * (1.0f - this.floatingButtonHideProgress)));
            HintView2 hintView2 = this.storyHint;
            if (hintView2 != null) {
                hintView2.setTranslationY(this.floatingButtonContainer.getTranslationY());
            }
        }
        FrameLayout frameLayout2 = this.floatingButton2Container;
        if (frameLayout2 != null) {
            frameLayout2.setTranslationY(((this.floatingButtonTranslation - this.floatingButtonPanOffset) - (Math.max(this.additionalFloatingTranslation, this.additionalFloatingTranslation2) * (1.0f - this.floatingButtonHideProgress))) + (AndroidUtilities.dp(44.0f) * this.floatingButtonHideProgress));
        }
    }

    private void updateStoriesPosting() {
        HintView2 hintView2;
        ActionBarMenuItem actionBarMenuItem;
        boolean storiesEnabled = getMessagesController().storiesEnabled();
        if (this.storiesEnabled != storiesEnabled) {
            FrameLayout frameLayout = this.floatingButton2Container;
            if (frameLayout != null) {
                frameLayout.setVisibility(((!this.onlySelect || this.initialDialogsType == 10) && this.folderId == 0 && storiesEnabled && ((actionBarMenuItem = this.searchItem) == null || !actionBarMenuItem.isSearchFieldVisible()) && !isInPreviewMode()) ? 0 : 8);
            }
            updateFloatingButtonOffset();
            if (!this.storiesEnabled && storiesEnabled && (hintView2 = this.storyHint) != null) {
                hintView2.show();
            }
            this.storiesEnabled = storiesEnabled;
        }
        RLottieImageView rLottieImageView = this.floatingButton;
        if (rLottieImageView == null || this.floatingButtonContainer == null) {
            return;
        }
        if (this.initialDialogsType == 10) {
            rLottieImageView.setImageResource(R.drawable.floating_check);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("Done", R.string.Done));
        } else if (storiesEnabled) {
            rLottieImageView.setAnimation(R.raw.write_contacts_fab_icon_camera, 56, 56);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("AccDescrCaptureStory", R.string.AccDescrCaptureStory));
        } else {
            rLottieImageView.setAnimation(R.raw.write_contacts_fab_icon, 52, 52);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("NewMessageTitle", R.string.NewMessageTitle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasHiddenArchive() {
        return !this.onlySelect && this.initialDialogsType == 0 && this.folderId == 0 && getMessagesController().hasHiddenArchive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean waitingForDialogsAnimationEnd(ViewPage viewPage) {
        return viewPage.dialogsItemAnimator.isRunning();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAnimationFinished() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$checkAnimationFinished$86();
            }
        }, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAnimationFinished$86() {
        setDialogsListFrozen(false);
        updateDialogIndices();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollY(float f) {
        ViewPage[] viewPageArr = this.viewPages;
        if (viewPageArr != null) {
            int i = 0;
            int paddingTop = viewPageArr[0].listView.getPaddingTop() + ((int) f);
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i >= viewPageArr2.length) {
                    break;
                }
                viewPageArr2[i].listView.setTopGlowOffset(paddingTop);
                i++;
            }
        }
        if (this.fragmentView == null || f == this.scrollYOffset) {
            return;
        }
        this.scrollYOffset = f;
        Bulletin bulletin = this.topBulletin;
        if (bulletin != null) {
            bulletin.updatePosition();
        }
        DrawerProfileCell.AnimatedStatusView animatedStatusView = this.animatedStatusView;
        if (animatedStatusView != null) {
            animatedStatusView.translateY2((int) f);
            this.animatedStatusView.setAlpha(1.0f - ((-f) / ActionBar.getCurrentActionBarHeight()));
        }
        this.fragmentView.invalidate();
    }

    private void prepareBlurBitmap() {
        if (this.blurredView == null) {
            return;
        }
        int measuredWidth = (int) (this.fragmentView.getMeasuredWidth() / 6.0f);
        int measuredHeight = (int) (this.fragmentView.getMeasuredHeight() / 6.0f);
        Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(0.16666667f, 0.16666667f);
        this.fragmentView.draw(canvas);
        Utilities.stackBlurBitmap(createBitmap, Math.max(7, Math.max(measuredWidth, measuredHeight) / 180));
        this.blurredView.setBackground(new BitmapDrawable(createBitmap));
        this.blurredView.setAlpha(0.0f);
        this.blurredView.setVisibility(0);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationProgress(boolean z, float f) {
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
            this.rightSlidingDialogContainer.getFragment().onTransitionAnimationProgress(z, f);
            return;
        }
        View view = this.blurredView;
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        if (z) {
            this.blurredView.setAlpha(1.0f - f);
        } else {
            this.blurredView.setAlpha(f);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        View view;
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
            this.rightSlidingDialogContainer.getFragment().onTransitionAnimationEnd(z, z2);
            return;
        }
        if (z && (view = this.blurredView) != null && view.getVisibility() == 0) {
            this.blurredView.setVisibility(8);
            this.blurredView.setBackground(null);
        }
        if (z && this.afterSignup) {
            try {
                this.fragmentView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (getParentActivity() instanceof LaunchActivity) {
                ((LaunchActivity) getParentActivity()).getFireworksOverlay().start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetScroll() {
        if (this.scrollYOffset == 0.0f || this.hasStories) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this, this.SCROLL_Y, 0.0f));
        animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
        animatorSet.setDuration(250L);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideActionMode(boolean z) {
        final float f;
        ArrayList<TLRPC$InputDialogPeer> arrayList;
        this.actionBar.hideActionMode();
        if (this.menuDrawable != null) {
            this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", R.string.AccDescrOpenMenu));
        }
        this.selectedDialogs.clear();
        MenuDrawable menuDrawable = this.menuDrawable;
        if (menuDrawable != null) {
            menuDrawable.setRotation(0.0f, true);
        } else {
            BackDrawable backDrawable = this.backDrawable;
            if (backDrawable != null) {
                backDrawable.setRotation(0.0f, true);
            }
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null) {
            filterTabsView.animateColorsTo(Theme.key_actionBarTabLine, Theme.key_actionBarTabActiveText, Theme.key_actionBarTabUnactiveText, Theme.key_actionBarTabSelector, Theme.key_actionBarDefault);
        }
        ValueAnimator valueAnimator = this.actionBarColorAnimator;
        ArrayList<TLRPC$InputDialogPeer> arrayList2 = null;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.actionBarColorAnimator = null;
        }
        if (this.progressToActionMode == 0.0f) {
            return;
        }
        if (this.hasStories) {
            setScrollY(-getMaxScrollYOffset());
            int i = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i >= viewPageArr.length) {
                    break;
                }
                if (viewPageArr[i] != null) {
                    viewPageArr[i].listView.cancelClickRunnables(true);
                }
                i++;
            }
            f = Math.max(0.0f, AndroidUtilities.dp(81.0f) + this.scrollYOffset);
        } else {
            f = 0.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.progressToActionMode, 0.0f);
        this.actionBarColorAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                DialogsActivity.this.lambda$hideActionMode$87(f, valueAnimator2);
            }
        });
        this.actionBarColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.42
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                DialogsActivity.this.actionBarColorAnimator = null;
                DialogsActivity.this.actionModeFullyShowed = false;
                DialogsActivity dialogsActivity = DialogsActivity.this;
                if (dialogsActivity.hasStories) {
                    dialogsActivity.invalidateScrollY = true;
                    DialogsActivity.this.fixScrollYAfterArchiveOpened = true;
                    DialogsActivity.this.fragmentView.invalidate();
                    DialogsActivity.this.scrollAdditionalOffset = -(AndroidUtilities.dp(81.0f) - f);
                    DialogsActivity.this.viewPages[0].setTranslationY(0.0f);
                    for (int i2 = 0; i2 < DialogsActivity.this.viewPages.length; i2++) {
                        if (DialogsActivity.this.viewPages[i2] != null) {
                            DialogsActivity.this.viewPages[i2].listView.requestLayout();
                        }
                    }
                    DialogsActivity.this.fragmentView.requestLayout();
                }
            }
        });
        this.actionBarColorAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.actionBarColorAnimator.setDuration(200L);
        this.actionBarColorAnimator.start();
        this.allowMoving = false;
        if (this.movingDialogFilters.isEmpty()) {
            arrayList = null;
        } else {
            int i2 = 0;
            for (int size = this.movingDialogFilters.size(); i2 < size; size = size) {
                MessagesController.DialogFilter dialogFilter = this.movingDialogFilters.get(i2);
                FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
                i2++;
                arrayList2 = arrayList2;
            }
            arrayList = arrayList2;
            this.movingDialogFilters.clear();
        }
        if (this.movingWas) {
            getMessagesController().reorderPinnedDialogs(this.folderId, arrayList, 0L);
            this.movingWas = false;
        }
        updateCounters(true);
        if (this.viewPages != null) {
            int i3 = 0;
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i3 >= viewPageArr2.length) {
                    break;
                }
                viewPageArr2[i3].dialogsAdapter.onReorderStateChanged(false);
                i3++;
            }
        }
        updateVisibleRows(MessagesController.UPDATE_MASK_REORDER | MessagesController.UPDATE_MASK_CHECK | (z ? MessagesController.UPDATE_MASK_CHAT : 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideActionMode$87(float f, ValueAnimator valueAnimator) {
        if (this.hasStories) {
            this.viewPages[0].setTranslationY(f * (1.0f - this.progressToActionMode));
        }
        this.progressToActionMode = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        for (int i = 0; i < this.actionBar.getChildCount(); i++) {
            if (this.actionBar.getChildAt(i).getVisibility() == 0 && this.actionBar.getChildAt(i) != this.actionBar.getActionMode() && this.actionBar.getChildAt(i) != this.actionBar.getBackButton()) {
                this.actionBar.getChildAt(i).setAlpha(1.0f - this.progressToActionMode);
            }
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
    }

    private int getPinnedCount() {
        ArrayList<TLRPC$Dialog> dialogs;
        if ((this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null))) {
            dialogs = getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, this.dialogsListFrozen);
        } else {
            dialogs = getMessagesController().getDialogs(this.folderId);
        }
        int size = dialogs.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC$Dialog tLRPC$Dialog = dialogs.get(i2);
            if (!(tLRPC$Dialog instanceof TLRPC$TL_dialogFolder)) {
                if (isDialogPinned(tLRPC$Dialog)) {
                    i++;
                } else if (!getMessagesController().isPromoDialog(tLRPC$Dialog.id, false)) {
                    break;
                }
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDialogPinned(TLRPC$Dialog tLRPC$Dialog) {
        if (tLRPC$Dialog == null) {
            return false;
        }
        MessagesController.DialogFilter dialogFilter = null;
        if ((this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null))) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
        }
        if (dialogFilter != null) {
            return dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog.id) >= 0;
        }
        return tLRPC$Dialog.pinned;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performSelectedDialogsAction(ArrayList<Long> arrayList, final int i, boolean z, boolean z2) {
        MessagesController.DialogFilter dialogFilter;
        boolean z3;
        int i2;
        int i3;
        String str;
        int i4;
        String str2;
        long j;
        Theme.ResourcesProvider resourcesProvider;
        boolean z4;
        TLRPC$Chat chat;
        TLRPC$EncryptedChat tLRPC$EncryptedChat;
        TLRPC$User tLRPC$User;
        int i5;
        boolean z5;
        String str3;
        TLRPC$User tLRPC$TL_userEmpty;
        String string;
        final ArrayList<Long> arrayList2 = arrayList;
        if (getParentActivity() == null) {
            return;
        }
        boolean z6 = (this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null));
        if (z6) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
        } else {
            dialogFilter = null;
        }
        int size = arrayList.size();
        if (i == 105 || i == 107) {
            final ArrayList<Long> arrayList3 = new ArrayList<>(arrayList);
            getMessagesController().addDialogToFolder(arrayList3, this.canUnarchiveCount == 0 ? 1 : 0, -1, null, 0L);
            if (this.canUnarchiveCount == 0) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                z3 = false;
                boolean z7 = globalMainSettings.getBoolean("archivehint_l", false) || SharedConfig.archiveHidden;
                if (z7) {
                    i2 = 1;
                } else {
                    i2 = 1;
                    globalMainSettings.edit().putBoolean("archivehint_l", true).commit();
                }
                if (z7) {
                    i3 = arrayList3.size() > i2 ? 4 : 2;
                } else {
                    i3 = arrayList3.size() > i2 ? 5 : 3;
                }
                int i6 = i3;
                UndoView undoView = getUndoView();
                if (undoView != null) {
                    undoView.showWithAction(0L, i6, null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda94
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.this.lambda$performSelectedDialogsAction$88(arrayList3);
                        }
                    });
                }
            } else {
                z3 = false;
                ArrayList<TLRPC$Dialog> dialogs = getMessagesController().getDialogs(this.folderId);
                if (this.viewPages != null && dialogs.isEmpty() && !this.hasStories) {
                    this.viewPages[0].listView.setEmptyView(null);
                    this.viewPages[0].progressView.setVisibility(4);
                    finishFragment();
                }
            }
            hideActionMode(z3);
            return;
        }
        String str4 = "Cancel";
        if ((i == 100 || i == 108) && this.canPinCount != 0) {
            ArrayList<TLRPC$Dialog> dialogs2 = getMessagesController().getDialogs(this.folderId);
            int size2 = dialogs2.size();
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            while (true) {
                if (i7 >= size2) {
                    str = str4;
                    break;
                }
                TLRPC$Dialog tLRPC$Dialog = dialogs2.get(i7);
                if (!(tLRPC$Dialog instanceof TLRPC$TL_dialogFolder)) {
                    if (isDialogPinned(tLRPC$Dialog)) {
                        if (DialogObject.isEncryptedDialog(tLRPC$Dialog.id)) {
                            i9++;
                        } else {
                            i8++;
                        }
                    } else {
                        str = str4;
                        if (!getMessagesController().isPromoDialog(tLRPC$Dialog.id, false)) {
                            break;
                        }
                        i7++;
                        str4 = str;
                    }
                }
                str = str4;
                i7++;
                str4 = str;
            }
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            for (int i13 = 0; i13 < size; i13++) {
                long longValue = arrayList2.get(i13).longValue();
                TLRPC$Dialog tLRPC$Dialog2 = getMessagesController().dialogs_dict.get(longValue);
                if (tLRPC$Dialog2 != null && !isDialogPinned(tLRPC$Dialog2)) {
                    if (DialogObject.isEncryptedDialog(longValue)) {
                        i11++;
                    } else {
                        i10++;
                    }
                    if (dialogFilter != null && dialogFilter.alwaysShow.contains(Long.valueOf(longValue))) {
                        i12++;
                    }
                }
            }
            if (z6) {
                i4 = 100 - dialogFilter.alwaysShow.size();
            } else if (this.folderId != 0 || dialogFilter != null) {
                if (UserConfig.getInstance(this.currentAccount).isPremium()) {
                    i4 = getMessagesController().maxFolderPinnedDialogsCountPremium;
                } else {
                    i4 = getMessagesController().maxFolderPinnedDialogsCountDefault;
                }
            } else {
                i4 = getUserConfig().isPremium() ? getMessagesController().dialogFiltersPinnedLimitPremium : getMessagesController().dialogFiltersPinnedLimitDefault;
            }
            if (i11 + i9 > i4 || (i10 + i8) - i12 > i4) {
                if (this.folderId == 0 && dialogFilter == null) {
                    showDialog(new LimitReachedBottomSheet(this, getParentActivity(), 0, this.currentAccount, null));
                    return;
                } else {
                    AlertsCreator.showSimpleAlert(this, LocaleController.formatString("PinFolderLimitReached", R.string.PinFolderLimitReached, LocaleController.formatPluralString("Chats", i4, new Object[0])));
                    return;
                }
            }
            str2 = str;
        } else if ((i == 102 || i == 103) && size > 1 && z) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            if (i == 102) {
                builder.setTitle(LocaleController.formatString("DeleteFewChatsTitle", R.string.DeleteFewChatsTitle, LocaleController.formatPluralString("ChatsSelected", size, new Object[0])));
                builder.setMessage(LocaleController.getString("AreYouSureDeleteFewChats", R.string.AreYouSureDeleteFewChats));
            } else if (this.canClearCacheCount != 0) {
                builder.setTitle(LocaleController.formatString("ClearCacheFewChatsTitle", R.string.ClearCacheFewChatsTitle, LocaleController.formatPluralString("ChatsSelectedClearCache", size, new Object[0])));
                builder.setMessage(LocaleController.getString("AreYouSureClearHistoryCacheFewChats", R.string.AreYouSureClearHistoryCacheFewChats));
            } else {
                builder.setTitle(LocaleController.formatString("ClearFewChatsTitle", R.string.ClearFewChatsTitle, LocaleController.formatPluralString("ChatsSelectedClear", size, new Object[0])));
                builder.setMessage(LocaleController.getString("AreYouSureClearHistoryFewChats", R.string.AreYouSureClearHistoryFewChats));
            }
            if (i == 102) {
                string = LocaleController.getString("Delete", R.string.Delete);
            } else {
                string = this.canClearCacheCount != 0 ? LocaleController.getString("ClearHistoryCache", R.string.ClearHistoryCache) : LocaleController.getString("ClearHistory", R.string.ClearHistory);
            }
            builder.setPositiveButton(string, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda18
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i14) {
                    DialogsActivity.this.lambda$performSelectedDialogsAction$90(arrayList2, i, dialogInterface, i14);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                return;
            }
            return;
        } else {
            str2 = "Cancel";
            if (i == 106 && z) {
                AlertsCreator.createBlockDialogAlert(this, size, this.canReportSpamCount != 0, size == 1 ? getMessagesController().getUser(Long.valueOf(arrayList2.get(0).longValue())) : null, new AlertsCreator.BlockDialogCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda130
                    @Override // org.telegram.ui.Components.AlertsCreator.BlockDialogCallback
                    public final void run(boolean z8, boolean z9) {
                        DialogsActivity.this.lambda$performSelectedDialogsAction$91(arrayList2, z8, z9);
                    }
                });
                return;
            }
        }
        int i14 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (dialogFilter != null && ((i == 100 || i == 108) && this.canPinCount != 0)) {
            int size3 = dialogFilter.pinnedDialogs.size();
            for (int i15 = 0; i15 < size3; i15++) {
                i14 = Math.min(i14, dialogFilter.pinnedDialogs.valueAt(i15));
            }
            i14 -= this.canPinCount;
        }
        int i16 = i14;
        int i17 = 0;
        int i18 = 0;
        while (i17 < size) {
            final long longValue2 = arrayList2.get(i17).longValue();
            TLRPC$Dialog tLRPC$Dialog3 = getMessagesController().dialogs_dict.get(longValue2);
            if (tLRPC$Dialog3 != null) {
                if (DialogObject.isEncryptedDialog(longValue2)) {
                    TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(longValue2)));
                    if (encryptedChat != null) {
                        tLRPC$TL_userEmpty = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                    } else {
                        tLRPC$TL_userEmpty = new TLRPC$TL_userEmpty();
                    }
                    tLRPC$User = tLRPC$TL_userEmpty;
                    chat = null;
                    tLRPC$EncryptedChat = encryptedChat;
                } else if (DialogObject.isUserDialog(longValue2)) {
                    tLRPC$User = getMessagesController().getUser(Long.valueOf(longValue2));
                    tLRPC$EncryptedChat = null;
                    chat = null;
                } else {
                    chat = getMessagesController().getChat(Long.valueOf(-longValue2));
                    tLRPC$EncryptedChat = null;
                    tLRPC$User = null;
                }
                if (chat != null || tLRPC$User != null) {
                    if (tLRPC$User == null || !tLRPC$User.bot || MessagesController.isSupportUser(tLRPC$User)) {
                        i5 = 100;
                        z5 = false;
                    } else {
                        i5 = 100;
                        z5 = true;
                    }
                    if (i == i5 || i == 108) {
                        str3 = str2;
                        if (this.canPinCount != 0) {
                            if (!isDialogPinned(tLRPC$Dialog3)) {
                                i18++;
                                TLRPC$EncryptedChat tLRPC$EncryptedChat2 = tLRPC$EncryptedChat;
                                pinDialog(longValue2, true, dialogFilter, i16, size == 1);
                                if (dialogFilter != null) {
                                    i16++;
                                    if (tLRPC$EncryptedChat2 != null) {
                                        if (!dialogFilter.alwaysShow.contains(Long.valueOf(tLRPC$EncryptedChat2.user_id))) {
                                            dialogFilter.alwaysShow.add(Long.valueOf(tLRPC$EncryptedChat2.user_id));
                                        }
                                    } else if (!dialogFilter.alwaysShow.contains(Long.valueOf(tLRPC$Dialog3.id))) {
                                        dialogFilter.alwaysShow.add(Long.valueOf(tLRPC$Dialog3.id));
                                    }
                                }
                            }
                        } else if (isDialogPinned(tLRPC$Dialog3)) {
                            i18++;
                            pinDialog(longValue2, false, dialogFilter, i16, size == 1);
                        }
                    } else if (i == 101) {
                        if (this.canReadCount != 0) {
                            markAsRead(longValue2);
                        } else {
                            markAsUnread(longValue2);
                        }
                    } else if (i == 102 || i == 103) {
                        if (size == 1) {
                            if (i == 102 && this.canDeletePsaSelected) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                                builder2.setTitle(LocaleController.getString("PsaHideChatAlertTitle", R.string.PsaHideChatAlertTitle));
                                builder2.setMessage(LocaleController.getString("PsaHideChatAlertText", R.string.PsaHideChatAlertText));
                                builder2.setPositiveButton(LocaleController.getString("PsaHide", R.string.PsaHide), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda14
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i19) {
                                        DialogsActivity.this.lambda$performSelectedDialogsAction$92(dialogInterface, i19);
                                    }
                                });
                                builder2.setNegativeButton(LocaleController.getString(str2, R.string.Cancel), null);
                                showDialog(builder2.create());
                                return;
                            }
                            final TLRPC$Chat tLRPC$Chat = chat;
                            final boolean z8 = z5;
                            AlertsCreator.createClearOrDeleteDialogAlert(this, i == 103, chat, tLRPC$User, DialogObject.isEncryptedDialog(tLRPC$Dialog3.id), i == 102, new MessagesStorage.BooleanCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda110
                                @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
                                public final void run(boolean z9) {
                                    DialogsActivity.this.lambda$performSelectedDialogsAction$94(i, tLRPC$Chat, longValue2, z8, z9);
                                }
                            });
                            return;
                        } else if (getMessagesController().isPromoDialog(longValue2, true)) {
                            getMessagesController().hidePromoDialog();
                        } else if (i == 103 && this.canClearCacheCount != 0) {
                            str3 = str2;
                            getMessagesController().deleteDialog(longValue2, 2, false);
                        } else {
                            str3 = str2;
                            lambda$performSelectedDialogsAction$93(i, longValue2, chat, z5, false);
                        }
                    } else if (i == 104) {
                        if (size == 1 && this.canMuteCount == 1) {
                            showDialog(AlertsCreator.createMuteAlert(this, longValue2, 0L, (Theme.ResourcesProvider) null), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda21
                                @Override // android.content.DialogInterface.OnDismissListener
                                public final void onDismiss(DialogInterface dialogInterface) {
                                    DialogsActivity.this.lambda$performSelectedDialogsAction$95(dialogInterface);
                                }
                            });
                            return;
                        } else if (this.canUnmuteCount != 0) {
                            if (getMessagesController().isDialogMuted(longValue2, 0L)) {
                                getNotificationsController().setDialogNotificationsSettings(longValue2, 0L, 4);
                            }
                        } else if (z2) {
                            showDialog(AlertsCreator.createMuteAlert(this, arrayList2, 0, (Theme.ResourcesProvider) null), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda23
                                @Override // android.content.DialogInterface.OnDismissListener
                                public final void onDismiss(DialogInterface dialogInterface) {
                                    DialogsActivity.this.lambda$performSelectedDialogsAction$96(dialogInterface);
                                }
                            });
                            return;
                        } else if (!getMessagesController().isDialogMuted(longValue2, 0L)) {
                            getNotificationsController().setDialogNotificationsSettings(longValue2, 0L, 3);
                        }
                    }
                    i17++;
                    arrayList2 = arrayList;
                    str2 = str3;
                }
            }
            str3 = str2;
            i17++;
            arrayList2 = arrayList;
            str2 = str3;
        }
        if (i == 104 && (size != 1 || this.canMuteCount != 1)) {
            if (this.canUnmuteCount == 0) {
                resourcesProvider = null;
                z4 = true;
            } else {
                resourcesProvider = null;
                z4 = false;
            }
            BulletinFactory.createMuteBulletin(this, z4, resourcesProvider).show();
        }
        if (i == 100 || i == 108) {
            if (dialogFilter != null) {
                FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.color, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
                j = 0;
            } else {
                j = 0;
                getMessagesController().reorderPinnedDialogs(this.folderId, null, 0L);
            }
            UndoView undoView2 = getUndoView();
            if (this.searchIsShowed && undoView2 != null) {
                undoView2.showWithAction(j, this.canPinCount != 0 ? 78 : 79, Integer.valueOf(i18));
            }
        }
        hideActionMode((i == 108 || i == 100 || i == 102) ? false : true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$88(ArrayList arrayList) {
        getMessagesController().addDialogToFolder(arrayList, this.folderId == 0 ? 0 : 1, -1, null, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$90(ArrayList arrayList, final int i, DialogInterface dialogInterface, int i2) {
        if (arrayList.isEmpty()) {
            return;
        }
        final ArrayList<Long> arrayList2 = new ArrayList<>(arrayList);
        UndoView undoView = getUndoView();
        if (undoView != null) {
            undoView.showWithAction(arrayList2, i == 102 ? 27 : 26, (Object) null, (Object) null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda86
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$performSelectedDialogsAction$89(i, arrayList2);
                }
            }, (Runnable) null);
        }
        hideActionMode(i == 103);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$89(int i, ArrayList arrayList) {
        if (i == 102) {
            getMessagesController().setDialogsInTransaction(true);
            performSelectedDialogsAction(arrayList, i, false, false);
            getMessagesController().setDialogsInTransaction(false);
            getMessagesController().checkIfFolderEmpty(this.folderId);
            if (this.folderId == 0 || getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false).size() != 0) {
                return;
            }
            this.viewPages[0].listView.setEmptyView(null);
            this.viewPages[0].progressView.setVisibility(4);
            finishFragment();
            return;
        }
        performSelectedDialogsAction(arrayList, i, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$91(ArrayList arrayList, boolean z, boolean z2) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            long longValue = ((Long) arrayList.get(i)).longValue();
            if (z) {
                getMessagesController().reportSpam(longValue, getMessagesController().getUser(Long.valueOf(longValue)), null, null, false);
            }
            if (z2) {
                getMessagesController().deleteDialog(longValue, 0, true);
            }
            getMessagesController().blockPeer(longValue);
        }
        hideActionMode(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$92(DialogInterface dialogInterface, int i) {
        getMessagesController().hidePromoDialog();
        hideActionMode(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$94(final int i, final TLRPC$Chat tLRPC$Chat, final long j, final boolean z, final boolean z2) {
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList<TLRPC$Dialog> arrayList;
        int i6;
        hideActionMode(false);
        if (i == 103 && ChatObject.isChannel(tLRPC$Chat)) {
            if (!tLRPC$Chat.megagroup || ChatObject.isPublic(tLRPC$Chat)) {
                getMessagesController().deleteDialog(j, 2, z2);
                return;
            }
        }
        if (i == 102 && this.folderId != 0 && getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false).size() == 1) {
            this.viewPages[0].progressView.setVisibility(4);
        }
        this.debugLastUpdateAction = 3;
        if (i == 102) {
            setDialogsListFrozen(true);
            if (this.frozenDialogsList != null) {
                i6 = 0;
                while (i6 < this.frozenDialogsList.size()) {
                    if (this.frozenDialogsList.get(i6).id == j) {
                        break;
                    }
                    i6++;
                }
            }
            i6 = -1;
            checkAnimationFinished();
            i2 = i6;
        } else {
            i2 = -1;
        }
        UndoView undoView = getUndoView();
        if (undoView != null) {
            i3 = i2;
            undoView.showWithAction(j, i == 103 ? 0 : 1, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda85
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$performSelectedDialogsAction$93(i, j, tLRPC$Chat, z, z2);
                }
            });
        } else {
            i3 = i2;
        }
        ArrayList arrayList2 = new ArrayList(getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false));
        int i7 = 0;
        while (true) {
            if (i7 >= arrayList2.size()) {
                i4 = R.styleable.AppCompatTheme_textAppearanceLargePopupMenu;
                i5 = -1;
                break;
            } else if (((TLRPC$Dialog) arrayList2.get(i7)).id == j) {
                i5 = i7;
                i4 = R.styleable.AppCompatTheme_textAppearanceLargePopupMenu;
                break;
            } else {
                i7++;
            }
        }
        if (i == i4) {
            int i8 = i3;
            if (i8 >= 0 && i5 < 0 && (arrayList = this.frozenDialogsList) != null) {
                arrayList.remove(i8);
                this.viewPages[0].dialogsItemAnimator.prepareForRemove();
                this.viewPages[0].updateList(true);
                return;
            }
            setDialogsListFrozen(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$95(DialogInterface dialogInterface) {
        hideActionMode(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$96(DialogInterface dialogInterface) {
        hideActionMode(true);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void markAsRead(long j) {
        int i;
        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
        MessagesController.DialogFilter dialogFilter = null;
        if ((this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null))) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
        }
        this.debugLastUpdateAction = 2;
        int i2 = -1;
        if (dialogFilter != null && (dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ) != 0 && !dialogFilter.alwaysShow(this.currentAccount, tLRPC$Dialog)) {
            setDialogsListFrozen(true);
            checkAnimationFinished();
            if (this.frozenDialogsList != null) {
                int i3 = 0;
                while (true) {
                    if (i3 >= this.frozenDialogsList.size()) {
                        break;
                    } else if (this.frozenDialogsList.get(i3).id == j) {
                        i2 = i3;
                        break;
                    } else {
                        i3++;
                    }
                }
                if (i2 < 0) {
                    setDialogsListFrozen(false, false);
                }
                i = i2;
                if (getMessagesController().isForum(j)) {
                    getMessagesController().markAllTopicsAsRead(j);
                }
                getMessagesController().markMentionsAsRead(j, 0L);
                MessagesController messagesController = getMessagesController();
                int i4 = tLRPC$Dialog.top_message;
                messagesController.markDialogAsRead(j, i4, i4, tLRPC$Dialog.last_message_date, false, 0L, 0, true, 0);
                if (i < 0) {
                    this.frozenDialogsList.remove(i);
                    this.viewPages[0].dialogsItemAnimator.prepareForRemove();
                    this.viewPages[0].updateList(true);
                    return;
                }
                return;
            }
        }
        i = -1;
        if (getMessagesController().isForum(j)) {
        }
        getMessagesController().markMentionsAsRead(j, 0L);
        MessagesController messagesController2 = getMessagesController();
        int i42 = tLRPC$Dialog.top_message;
        messagesController2.markDialogAsRead(j, i42, i42, tLRPC$Dialog.last_message_date, false, 0L, 0, true, 0);
        if (i < 0) {
        }
    }

    private void markAsUnread(long j) {
        getMessagesController().markDialogAsUnread(j, null, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markDialogsAsRead(ArrayList<TLRPC$Dialog> arrayList) {
        this.debugLastUpdateAction = 2;
        setDialogsListFrozen(true);
        checkAnimationFinished();
        for (int i = 0; i < arrayList.size(); i++) {
            long j = arrayList.get(i).id;
            TLRPC$Dialog tLRPC$Dialog = arrayList.get(i);
            if (getMessagesController().isForum(j)) {
                getMessagesController().markAllTopicsAsRead(j);
            }
            getMessagesController().markMentionsAsRead(j, 0L);
            MessagesController messagesController = getMessagesController();
            int i2 = tLRPC$Dialog.top_message;
            messagesController.markDialogAsRead(j, i2, i2, tLRPC$Dialog.last_message_date, false, 0L, 0, true, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: performDeleteOrClearDialogAction */
    public void lambda$performSelectedDialogsAction$93(int i, long j, TLRPC$Chat tLRPC$Chat, boolean z, boolean z2) {
        if (i == 103) {
            getMessagesController().deleteDialog(j, 1, z2);
            return;
        }
        if (tLRPC$Chat != null) {
            if (ChatObject.isNotInChat(tLRPC$Chat)) {
                getMessagesController().deleteDialog(j, 0, z2);
            } else {
                getMessagesController().deleteParticipantFromChat(-j, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), (TLRPC$Chat) null, z2, false);
            }
        } else {
            getMessagesController().deleteDialog(j, 0, z2);
            if (z && z2) {
                getMessagesController().blockPeer(j);
            }
        }
        if (AndroidUtilities.isTablet()) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, Long.valueOf(j));
        }
        getMessagesController().checkIfFolderEmpty(this.folderId);
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:83:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void pinDialog(long j, boolean z, MessagesController.DialogFilter dialogFilter, int i, boolean z2) {
        int i2;
        boolean z3;
        boolean pinDialog;
        boolean z4 = true;
        int i3 = (this.viewPages[0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[0].archivePullViewState == 2) ? 1 : 0;
        int findFirstVisibleItemPosition = this.viewPages[0].layoutManager.findFirstVisibleItemPosition();
        if (dialogFilter != null) {
            int i4 = dialogFilter.pinnedDialogs.get(j, Integer.MIN_VALUE);
            if (!z && i4 == Integer.MIN_VALUE) {
                return;
            }
        }
        this.debugLastUpdateAction = z ? 4 : 5;
        int i5 = -1;
        if (findFirstVisibleItemPosition > i3 || !z2) {
            i2 = -1;
            z3 = true;
        } else {
            setDialogsListFrozen(true);
            checkAnimationFinished();
            if (this.frozenDialogsList != null) {
                for (int i6 = 0; i6 < this.frozenDialogsList.size(); i6++) {
                    if (this.frozenDialogsList.get(i6).id == j) {
                        i2 = i6;
                        break;
                    }
                }
            }
            i2 = -1;
            z3 = false;
        }
        if (dialogFilter != null) {
            if (z) {
                dialogFilter.pinnedDialogs.put(j, i);
            } else {
                dialogFilter.pinnedDialogs.delete(j);
            }
            if (z2) {
                getMessagesController().onFilterUpdate(dialogFilter);
            }
            pinDialog = true;
        } else {
            pinDialog = getMessagesController().pinDialog(j, z, null, -1L);
        }
        if (pinDialog) {
            if (!z3) {
                ArrayList<TLRPC$Dialog> dialogsArray = getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false);
                int i7 = 0;
                while (true) {
                    if (i7 >= dialogsArray.size()) {
                        break;
                    } else if (dialogsArray.get(i7).id == j) {
                        i5 = i7;
                        break;
                    } else {
                        i7++;
                    }
                }
            } else {
                if (this.initialDialogsType != 10) {
                    hideFloatingButton(false);
                }
                scrollToTop(true, false);
            }
        }
        if (z3) {
            return;
        }
        if (i2 >= 0) {
            ArrayList<TLRPC$Dialog> arrayList = this.frozenDialogsList;
            if (arrayList != null && i5 >= 0 && i2 != i5) {
                arrayList.add(i5, arrayList.remove(i2));
                this.viewPages[0].dialogsItemAnimator.prepareForRemove();
                this.viewPages[0].updateList(true);
                this.viewPages[0].layoutManager.scrollToPositionWithOffset((this.viewPages[0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[0].archivePullViewState == 2) ? 1 : 0, (int) this.scrollYOffset);
            } else if (i5 >= 0 && i2 == i5) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda73
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.this.lambda$pinDialog$97();
                    }
                }, 200L);
            }
            if (z4) {
                setDialogsListFrozen(false);
                return;
            }
            return;
        }
        z4 = false;
        if (z4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pinDialog$97() {
        setDialogsListFrozen(false);
    }

    public void scrollToTop(boolean z, boolean z2) {
        int i = (this.viewPages[0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[0].archivePullViewState == 2) ? 1 : 0;
        int i2 = (!this.hasStories || z2 || this.dialogStoriesCell.isExpanded()) ? 0 : -AndroidUtilities.dp(81.0f);
        if (z) {
            this.viewPages[0].scrollHelper.setScrollDirection(1);
            this.viewPages[0].scrollHelper.scrollToPosition(i, i2, false, true);
            resetScroll();
            return;
        }
        this.viewPages[0].layoutManager.scrollToPositionWithOffset(i, i2);
        resetScroll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:147:0x02f5  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0302  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x034f  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x037d  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x03aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateCounters(boolean z) {
        int i;
        ActionBarMenuSubItem actionBarMenuSubItem;
        ActionBarMenuItem actionBarMenuItem;
        ActionBarMenuSubItem actionBarMenuSubItem2;
        ActionBarMenuItem actionBarMenuItem2;
        int i2;
        long j;
        int i3;
        TLRPC$User user;
        this.canDeletePsaSelected = false;
        this.canUnarchiveCount = 0;
        this.canUnmuteCount = 0;
        this.canMuteCount = 0;
        this.canPinCount = 0;
        this.canReadCount = 0;
        this.forumCount = 0;
        this.canClearCacheCount = 0;
        this.canReportSpamCount = 0;
        if (z) {
            return;
        }
        int size = this.selectedDialogs.size();
        long clientUserId = getUserConfig().getClientUserId();
        SharedPreferences notificationsSettings = getNotificationsSettings();
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i4 < size) {
            TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(this.selectedDialogs.get(i4).longValue());
            if (tLRPC$Dialog == null) {
                i2 = size;
                j = clientUserId;
            } else {
                long j2 = tLRPC$Dialog.id;
                boolean isDialogPinned = isDialogPinned(tLRPC$Dialog);
                boolean z2 = tLRPC$Dialog.unread_count != 0 || tLRPC$Dialog.unread_mark;
                if (getMessagesController().isForum(j2)) {
                    i2 = size;
                    this.forumCount++;
                } else {
                    i2 = size;
                }
                j = clientUserId;
                if (getMessagesController().isDialogMuted(j2, 0L)) {
                    i3 = 1;
                    this.canUnmuteCount++;
                } else {
                    i3 = 1;
                    this.canMuteCount++;
                }
                if (z2) {
                    this.canReadCount += i3;
                }
                if (this.folderId == i3 || tLRPC$Dialog.folder_id == i3) {
                    this.canUnarchiveCount++;
                } else if (j2 != j && j2 != 777000 && !getMessagesController().isPromoDialog(j2, false)) {
                    i7++;
                }
                if (!DialogObject.isUserDialog(j2) || j2 == j || MessagesController.isSupportUser(getMessagesController().getUser(Long.valueOf(j2)))) {
                    i9++;
                } else {
                    if (notificationsSettings.getBoolean("dialog_bar_report" + j2, true)) {
                        this.canReportSpamCount++;
                    }
                }
                if (DialogObject.isChannel(tLRPC$Dialog)) {
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j2));
                    if (getMessagesController().isPromoDialog(tLRPC$Dialog.id, true)) {
                        this.canClearCacheCount++;
                        if (getMessagesController().promoDialogType == MessagesController.PROMO_TYPE_PSA) {
                            i5++;
                            this.canDeletePsaSelected = true;
                        }
                    } else {
                        if (isDialogPinned) {
                            i8++;
                        } else {
                            this.canPinCount++;
                        }
                        if (chat != null && chat.megagroup) {
                            if (ChatObject.isPublic(chat)) {
                                this.canClearCacheCount++;
                            }
                        } else {
                            this.canClearCacheCount++;
                        }
                        i5++;
                    }
                } else {
                    boolean isChatDialog = DialogObject.isChatDialog(tLRPC$Dialog.id);
                    if (isChatDialog) {
                        getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog.id));
                    }
                    if (DialogObject.isEncryptedDialog(tLRPC$Dialog.id)) {
                        TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(tLRPC$Dialog.id)));
                        if (encryptedChat != null) {
                            user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                        } else {
                            user = new TLRPC$TL_userEmpty();
                        }
                    } else {
                        user = (isChatDialog || !DialogObject.isUserDialog(tLRPC$Dialog.id)) ? null : getMessagesController().getUser(Long.valueOf(tLRPC$Dialog.id));
                    }
                    if (user != null && user.bot) {
                        MessagesController.isSupportUser(user);
                    }
                    if (isDialogPinned) {
                        i8++;
                    } else {
                        this.canPinCount++;
                    }
                }
                i6++;
                i5++;
            }
            i4++;
            size = i2;
            clientUserId = j;
        }
        int i10 = size;
        ActionBarMenuItem actionBarMenuItem3 = this.deleteItem;
        if (actionBarMenuItem3 != null) {
            if (i5 != i10) {
                actionBarMenuItem3.setVisibility(8);
            } else {
                actionBarMenuItem3.setVisibility(0);
            }
        }
        ActionBarMenuSubItem actionBarMenuSubItem3 = this.clearItem;
        if (actionBarMenuSubItem3 != null) {
            int i11 = this.canClearCacheCount;
            if ((i11 != 0 && i11 != i10) || (i6 != 0 && i6 != i10)) {
                actionBarMenuSubItem3.setVisibility(8);
            } else {
                actionBarMenuSubItem3.setVisibility(0);
                if (this.canClearCacheCount != 0) {
                    this.clearItem.setText(LocaleController.getString("ClearHistoryCache", R.string.ClearHistoryCache));
                } else {
                    this.clearItem.setText(LocaleController.getString("ClearHistory", R.string.ClearHistory));
                }
            }
        }
        ActionBarMenuSubItem actionBarMenuSubItem4 = this.archiveItem;
        if (actionBarMenuSubItem4 != null && this.archive2Item != null) {
            if (this.canUnarchiveCount != 0) {
                String string = LocaleController.getString("Unarchive", R.string.Unarchive);
                ActionBarMenuSubItem actionBarMenuSubItem5 = this.archiveItem;
                int i12 = R.drawable.msg_unarchive;
                actionBarMenuSubItem5.setTextAndIcon(string, i12);
                this.archive2Item.setIcon(i12);
                this.archive2Item.setContentDescription(string);
                FilterTabsView filterTabsView = this.filterTabsView;
                if (filterTabsView != null && filterTabsView.getVisibility() == 0) {
                    this.archive2Item.setVisibility(0);
                    this.archiveItem.setVisibility(8);
                } else {
                    this.archiveItem.setVisibility(0);
                    this.archive2Item.setVisibility(8);
                }
            } else if (i7 != 0) {
                String string2 = LocaleController.getString("Archive", R.string.Archive);
                ActionBarMenuSubItem actionBarMenuSubItem6 = this.archiveItem;
                int i13 = R.drawable.msg_archive;
                actionBarMenuSubItem6.setTextAndIcon(string2, i13);
                this.archive2Item.setIcon(i13);
                this.archive2Item.setContentDescription(string2);
                FilterTabsView filterTabsView2 = this.filterTabsView;
                if (filterTabsView2 != null && filterTabsView2.getVisibility() == 0) {
                    this.archive2Item.setVisibility(0);
                    this.archiveItem.setVisibility(8);
                } else {
                    this.archiveItem.setVisibility(0);
                    this.archive2Item.setVisibility(8);
                }
            } else {
                actionBarMenuSubItem4.setVisibility(8);
                this.archive2Item.setVisibility(8);
            }
        }
        ActionBarMenuItem actionBarMenuItem4 = this.pinItem;
        if (actionBarMenuItem4 != null && this.pin2Item != null) {
            if (this.canPinCount + i8 != i10) {
                actionBarMenuItem4.setVisibility(8);
                this.pin2Item.setVisibility(8);
            } else {
                FilterTabsView filterTabsView3 = this.filterTabsView;
                if (filterTabsView3 != null && filterTabsView3.getVisibility() == 0) {
                    i = 0;
                    this.pin2Item.setVisibility(0);
                    this.pinItem.setVisibility(8);
                } else {
                    i = 0;
                    this.pinItem.setVisibility(0);
                    this.pin2Item.setVisibility(8);
                }
                actionBarMenuSubItem = this.blockItem;
                if (actionBarMenuSubItem != null) {
                    if (i9 != 0) {
                        actionBarMenuSubItem.setVisibility(8);
                    } else {
                        actionBarMenuSubItem.setVisibility(i);
                    }
                }
                if (this.removeFromFolderItem != null) {
                    FilterTabsView filterTabsView4 = this.filterTabsView;
                    boolean z3 = filterTabsView4 == null || filterTabsView4.getVisibility() != 0 || this.filterTabsView.currentTabIsDefault();
                    if (!z3) {
                        try {
                            z3 = i10 >= getDialogsArray(this.currentAccount, this.viewPages[0].dialogsAdapter.getDialogsType(), this.folderId, this.dialogsListFrozen).size();
                        } catch (Exception unused) {
                        }
                    }
                    if (z3) {
                        this.removeFromFolderItem.setVisibility(8);
                    } else {
                        this.removeFromFolderItem.setVisibility(0);
                    }
                }
                if (this.addToFolderItem != null) {
                    FilterTabsView filterTabsView5 = this.filterTabsView;
                    if (filterTabsView5 != null && filterTabsView5.getVisibility() == 0 && this.filterTabsView.currentTabIsDefault() && !FiltersListBottomSheet.getCanAddDialogFilters(this, this.selectedDialogs).isEmpty()) {
                        this.addToFolderItem.setVisibility(0);
                    } else {
                        this.addToFolderItem.setVisibility(8);
                    }
                }
                actionBarMenuItem = this.muteItem;
                if (actionBarMenuItem != null) {
                    if (this.canUnmuteCount != 0) {
                        actionBarMenuItem.setIcon(R.drawable.msg_unmute);
                        this.muteItem.setContentDescription(LocaleController.getString("ChatsUnmute", R.string.ChatsUnmute));
                    } else {
                        actionBarMenuItem.setIcon(R.drawable.msg_mute);
                        this.muteItem.setContentDescription(LocaleController.getString("ChatsMute", R.string.ChatsMute));
                    }
                }
                actionBarMenuSubItem2 = this.readItem;
                if (actionBarMenuSubItem2 != null) {
                    if (this.canReadCount != 0) {
                        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("MarkAsRead", R.string.MarkAsRead), R.drawable.msg_markread);
                        this.readItem.setVisibility(0);
                    } else if (this.forumCount == 0) {
                        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread), R.drawable.msg_markunread);
                        this.readItem.setVisibility(0);
                    } else {
                        actionBarMenuSubItem2.setVisibility(8);
                    }
                }
                actionBarMenuItem2 = this.pinItem;
                if (actionBarMenuItem2 != null || this.pin2Item == null) {
                }
                if (this.canPinCount != 0) {
                    actionBarMenuItem2.setIcon(R.drawable.msg_pin);
                    this.pinItem.setContentDescription(LocaleController.getString("PinToTop", R.string.PinToTop));
                    this.pin2Item.setText(LocaleController.getString("DialogPin", R.string.DialogPin));
                    return;
                }
                actionBarMenuItem2.setIcon(R.drawable.msg_unpin);
                this.pinItem.setContentDescription(LocaleController.getString("UnpinFromTop", R.string.UnpinFromTop));
                this.pin2Item.setText(LocaleController.getString("DialogUnpin", R.string.DialogUnpin));
                return;
            }
        }
        i = 0;
        actionBarMenuSubItem = this.blockItem;
        if (actionBarMenuSubItem != null) {
        }
        if (this.removeFromFolderItem != null) {
        }
        if (this.addToFolderItem != null) {
        }
        actionBarMenuItem = this.muteItem;
        if (actionBarMenuItem != null) {
        }
        actionBarMenuSubItem2 = this.readItem;
        if (actionBarMenuSubItem2 != null) {
        }
        actionBarMenuItem2 = this.pinItem;
        if (actionBarMenuItem2 != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateSlowModeDialog(long j) {
        TLRPC$Chat chat;
        ChatActivityEnterView chatActivityEnterView;
        if ((this.messagesCount > 1 || !((chatActivityEnterView = this.commentView) == null || chatActivityEnterView.getVisibility() != 0 || TextUtils.isEmpty(this.commentView.getFieldText()))) && DialogObject.isChatDialog(j) && (chat = getMessagesController().getChat(Long.valueOf(-j))) != null && !ChatObject.hasAdminRights(chat) && chat.slowmode_enabled) {
            AlertsCreator.showSimpleAlert(this, LocaleController.getString("Slowmode", R.string.Slowmode), LocaleController.getString("SlowmodeSendError", R.string.SlowmodeSendError));
            return false;
        }
        return true;
    }

    private void showOrUpdateActionMode(long j, View view) {
        addOrRemoveSelectedDialog(j, view);
        boolean z = true;
        if (this.actionBar.isActionModeShowed()) {
            if (this.selectedDialogs.isEmpty()) {
                hideActionMode(true);
                return;
            }
        } else {
            if (this.searchIsShowed) {
                createActionMode("search_dialogs_action_mode");
                if (this.actionBar.getBackButton().getDrawable() instanceof MenuDrawable) {
                    this.actionBar.setBackButtonDrawable(new BackDrawable(false));
                }
            } else {
                createActionMode(null);
            }
            AndroidUtilities.hideKeyboard(this.fragmentView.findFocus());
            this.actionBar.setActionModeOverrideColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            this.actionBar.showActionMode();
            if (!this.hasStories) {
                resetScroll();
            }
            if (this.menuDrawable != null) {
                this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", R.string.AccDescrGoBack));
            }
            if (getPinnedCount() > 1) {
                if (this.viewPages != null) {
                    int i = 0;
                    while (true) {
                        ViewPage[] viewPageArr = this.viewPages;
                        if (i >= viewPageArr.length) {
                            break;
                        }
                        viewPageArr[i].dialogsAdapter.onReorderStateChanged(true);
                        i++;
                    }
                }
                updateVisibleRows(MessagesController.UPDATE_MASK_REORDER);
            }
            if (!this.searchIsShowed) {
                AnimatorSet animatorSet = new AnimatorSet();
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < this.actionModeViews.size(); i2++) {
                    View view2 = this.actionModeViews.get(i2);
                    view2.setPivotY(ActionBar.getCurrentActionBarHeight() / 2);
                    AndroidUtilities.clearDrawableAnimation(view2);
                    arrayList.add(ObjectAnimator.ofFloat(view2, View.SCALE_Y, 0.1f, 1.0f));
                }
                animatorSet.playTogether(arrayList);
                animatorSet.setDuration(200L);
                animatorSet.start();
            }
            ValueAnimator valueAnimator = this.actionBarColorAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.actionBarColorAnimator = ValueAnimator.ofFloat(this.progressToActionMode, 1.0f);
            final float f = 0.0f;
            if (this.hasStories) {
                int i3 = 0;
                while (true) {
                    ViewPage[] viewPageArr2 = this.viewPages;
                    if (i3 >= viewPageArr2.length) {
                        break;
                    }
                    if (viewPageArr2[i3] != null) {
                        viewPageArr2[i3].listView.cancelClickRunnables(true);
                    }
                    i3++;
                }
                float max = Math.max(0.0f, AndroidUtilities.dp(81.0f) + this.scrollYOffset);
                if (max != 0.0f) {
                    this.actionModeAdditionalHeight = (int) max;
                    this.fragmentView.requestLayout();
                }
                f = max;
            }
            this.actionBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda6
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    DialogsActivity.this.lambda$showOrUpdateActionMode$98(f, valueAnimator2);
                }
            });
            this.actionBarColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.43
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    DialogsActivity.this.actionBarColorAnimator = null;
                    DialogsActivity.this.actionModeAdditionalHeight = 0;
                    DialogsActivity.this.actionModeFullyShowed = true;
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.hasStories) {
                        dialogsActivity.scrollAdditionalOffset = AndroidUtilities.dp(81.0f) - f;
                        DialogsActivity.this.viewPages[0].setTranslationY(0.0f);
                        for (int i4 = 0; i4 < DialogsActivity.this.viewPages.length; i4++) {
                            if (DialogsActivity.this.viewPages[i4] != null) {
                                DialogsActivity.this.viewPages[i4].listView.requestLayout();
                            }
                        }
                        DialogsActivity.this.dialogStoriesCell.setProgressToCollapse(1.0f, false);
                        DialogsActivity.this.fragmentView.requestLayout();
                    }
                }
            });
            this.actionBarColorAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.actionBarColorAnimator.setDuration(200L);
            this.actionBarColorAnimator.start();
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                filterTabsView.animateColorsTo(Theme.key_profile_tabSelectedLine, Theme.key_profile_tabSelectedText, Theme.key_profile_tabText, Theme.key_profile_tabSelector, Theme.key_actionBarActionModeDefault);
            }
            MenuDrawable menuDrawable = this.menuDrawable;
            if (menuDrawable != null) {
                menuDrawable.setRotateToBack(false);
                this.menuDrawable.setRotation(1.0f, true);
            } else {
                BackDrawable backDrawable = this.backDrawable;
                if (backDrawable != null) {
                    backDrawable.setRotation(1.0f, true);
                }
            }
            z = false;
        }
        updateCounters(false);
        this.selectedDialogsCountTextView.setNumber(this.selectedDialogs.size(), z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showOrUpdateActionMode$98(float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.progressToActionMode = floatValue;
        if (this.hasStories) {
            this.viewPages[0].setTranslationY((-f) * floatValue);
        }
        for (int i = 0; i < this.actionBar.getChildCount(); i++) {
            if (this.actionBar.getChildAt(i).getVisibility() == 0 && this.actionBar.getChildAt(i) != this.actionBar.getActionMode() && this.actionBar.getChildAt(i) != this.actionBar.getBackButton()) {
                this.actionBar.getChildAt(i).setAlpha(1.0f - this.progressToActionMode);
            }
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSearch() {
        if (AndroidUtilities.isTablet()) {
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
            TLObject tLObject = this.searchObject;
            if (tLObject != null) {
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager != null) {
                    searchViewPager.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, tLObject);
                }
                this.searchObject = null;
                return;
            }
            return;
        }
        this.closeSearchFieldOnHide = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RecyclerListView getListView() {
        return this.viewPages[0].listView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RecyclerListView getSearchListView() {
        createSearchViewPager();
        SearchViewPager searchViewPager = this.searchViewPager;
        if (searchViewPager != null) {
            return searchViewPager.searchListView;
        }
        return null;
    }

    public void createUndoView() {
        Context context;
        if (this.undoView[0] == null && (context = getContext()) != null) {
            for (int i = 0; i < 2; i++) {
                this.undoView[i] = new 44(context);
                UndoView undoView = this.undoView[i];
                int i2 = this.undoViewIndex + 1;
                this.undoViewIndex = i2;
                ((ContentView) this.fragmentView).addView(undoView, i2, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 44 extends UndoView {
        44(Context context) {
            super(context);
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            if (this == DialogsActivity.this.undoView[0]) {
                if (DialogsActivity.this.undoView[1] == null || DialogsActivity.this.undoView[1].getVisibility() != 0) {
                    DialogsActivity.this.additionalFloatingTranslation = (getMeasuredHeight() + AndroidUtilities.dp(8.0f)) - f;
                    if (DialogsActivity.this.additionalFloatingTranslation < 0.0f) {
                        DialogsActivity.this.additionalFloatingTranslation = 0.0f;
                    }
                    if (DialogsActivity.this.floatingHidden) {
                        return;
                    }
                    DialogsActivity.this.updateFloatingButtonOffset();
                }
            }
        }

        @Override // org.telegram.ui.Components.UndoView
        protected boolean canUndo() {
            for (int i = 0; i < DialogsActivity.this.viewPages.length; i++) {
                if (DialogsActivity.this.viewPages[i].dialogsItemAnimator.isRunning()) {
                    return false;
                }
            }
            return true;
        }

        @Override // org.telegram.ui.Components.UndoView
        protected void onRemoveDialogAction(long j, int i) {
            if (i == 1 || i == 27) {
                DialogsActivity.this.debugLastUpdateAction = 1;
                DialogsActivity.this.setDialogsListFrozen(true);
                if (DialogsActivity.this.frozenDialogsList != null) {
                    final int i2 = -1;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= DialogsActivity.this.frozenDialogsList.size()) {
                            break;
                        } else if (((TLRPC$Dialog) DialogsActivity.this.frozenDialogsList.get(i3)).id == j) {
                            i2 = i3;
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (i2 >= 0) {
                        final TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) DialogsActivity.this.frozenDialogsList.remove(i2);
                        DialogsActivity.this.viewPages[0].dialogsAdapter.notifyDataSetChanged();
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$44$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogsActivity.44.this.lambda$onRemoveDialogAction$0(i2, tLRPC$Dialog);
                            }
                        });
                    } else {
                        DialogsActivity.this.setDialogsListFrozen(false);
                    }
                }
                DialogsActivity.this.checkAnimationFinished();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRemoveDialogAction$0(int i, TLRPC$Dialog tLRPC$Dialog) {
            if (DialogsActivity.this.frozenDialogsList == null || i < 0 || i >= DialogsActivity.this.frozenDialogsList.size()) {
                return;
            }
            DialogsActivity.this.frozenDialogsList.add(i, tLRPC$Dialog);
            DialogsActivity.this.viewPages[0].updateList(true);
        }
    }

    public UndoView getUndoView() {
        createUndoView();
        UndoView[] undoViewArr = this.undoView;
        if (undoViewArr[0] != null && undoViewArr[0].getVisibility() == 0) {
            UndoView[] undoViewArr2 = this.undoView;
            UndoView undoView = undoViewArr2[0];
            undoViewArr2[0] = undoViewArr2[1];
            undoViewArr2[1] = undoView;
            undoView.hide(true, 2);
            ContentView contentView = (ContentView) this.fragmentView;
            contentView.removeView(this.undoView[0]);
            contentView.addView(this.undoView[0]);
        }
        return this.undoView[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProxyButton(boolean z, boolean z2) {
        boolean z3;
        ActionBarMenuItem actionBarMenuItem;
        if (this.proxyDrawable != null) {
            ActionBarMenuItem actionBarMenuItem2 = this.doneItem;
            if (actionBarMenuItem2 == null || actionBarMenuItem2.getVisibility() != 0) {
                boolean z4 = false;
                int i = 0;
                while (true) {
                    if (i >= getDownloadController().downloadingFiles.size()) {
                        z3 = false;
                        break;
                    } else if (getFileLoader().isLoadingFile(getDownloadController().downloadingFiles.get(i).getFileName())) {
                        z3 = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!this.searching && (getDownloadController().hasUnviewedDownloads() || z3 || (this.downloadsItem.getVisibility() == 0 && this.downloadsItem.getAlpha() == 1.0f && !z2))) {
                    this.downloadsItemVisible = true;
                    this.downloadsItem.setVisibility(0);
                } else {
                    this.downloadsItem.setVisibility(8);
                    this.downloadsItemVisible = false;
                }
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                String string = sharedPreferences.getString("proxy_ip", "");
                boolean z5 = sharedPreferences.getBoolean("proxy_enabled", false);
                if ((!this.downloadsItemVisible && !this.searching && z5 && !TextUtils.isEmpty(string)) || (getMessagesController().blockedCountry && !SharedConfig.proxyList.isEmpty())) {
                    if (!this.actionBar.isSearchFieldVisible() && ((actionBarMenuItem = this.doneItem) == null || actionBarMenuItem.getVisibility() != 0)) {
                        this.proxyItem.setVisibility(0);
                    }
                    this.proxyItemVisible = true;
                    ProxyDrawable proxyDrawable = this.proxyDrawable;
                    int i2 = this.currentConnectionState;
                    proxyDrawable.setConnected(z5, (i2 == 3 || i2 == 5) ? true : true, z);
                    return;
                }
                this.proxyItemVisible = false;
                this.proxyItem.setVisibility(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDoneItem(final boolean z) {
        if (this.doneItem == null) {
            return;
        }
        AnimatorSet animatorSet = this.doneItemAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.doneItemAnimator = null;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.doneItemAnimator = animatorSet2;
        animatorSet2.setDuration(180L);
        if (z) {
            this.doneItem.setVisibility(0);
        } else {
            this.doneItem.setSelected(false);
            Drawable background = this.doneItem.getBackground();
            if (background != null) {
                background.setState(StateSet.NOTHING);
                background.jumpToCurrentState();
            }
            ActionBarMenuItem actionBarMenuItem = this.searchItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setVisibility(0);
            }
            ActionBarMenuItem actionBarMenuItem2 = this.proxyItem;
            if (actionBarMenuItem2 != null && this.proxyItemVisible) {
                actionBarMenuItem2.setVisibility(0);
            }
            ActionBarMenuItem actionBarMenuItem3 = this.passcodeItem;
            if (actionBarMenuItem3 != null && this.passcodeItemVisible) {
                actionBarMenuItem3.setVisibility(0);
            }
            ActionBarMenuItem actionBarMenuItem4 = this.downloadsItem;
            if (actionBarMenuItem4 != null && this.downloadsItemVisible) {
                actionBarMenuItem4.setVisibility(0);
            }
        }
        ArrayList arrayList = new ArrayList();
        ActionBarMenuItem actionBarMenuItem5 = this.doneItem;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem5, property, fArr));
        if (this.proxyItemVisible) {
            ActionBarMenuItem actionBarMenuItem6 = this.proxyItem;
            Property property2 = View.ALPHA;
            float[] fArr2 = new float[1];
            fArr2[0] = z ? 0.0f : 1.0f;
            arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem6, property2, fArr2));
        }
        if (this.passcodeItemVisible) {
            ActionBarMenuItem actionBarMenuItem7 = this.passcodeItem;
            Property property3 = View.ALPHA;
            float[] fArr3 = new float[1];
            fArr3[0] = z ? 0.0f : 1.0f;
            arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem7, property3, fArr3));
        }
        ActionBarMenuItem actionBarMenuItem8 = this.searchItem;
        Property property4 = View.ALPHA;
        float[] fArr4 = new float[1];
        fArr4[0] = z ? 0.0f : 1.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem8, property4, fArr4));
        this.doneItemAnimator.playTogether(arrayList);
        this.doneItemAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.45
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                DialogsActivity.this.doneItemAnimator = null;
                if (z) {
                    if (DialogsActivity.this.searchItem != null) {
                        DialogsActivity.this.searchItem.setVisibility(4);
                    }
                    if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisible) {
                        DialogsActivity.this.proxyItem.setVisibility(4);
                    }
                    if (DialogsActivity.this.passcodeItem != null && DialogsActivity.this.passcodeItemVisible) {
                        DialogsActivity.this.passcodeItem.setVisibility(4);
                    }
                    if (DialogsActivity.this.downloadsItem == null || !DialogsActivity.this.downloadsItemVisible) {
                        return;
                    }
                    DialogsActivity.this.downloadsItem.setVisibility(4);
                } else if (DialogsActivity.this.doneItem != null) {
                    DialogsActivity.this.doneItem.setVisibility(8);
                }
            }
        });
        this.doneItemAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelectedCount() {
        if (this.commentView != null) {
            if (this.selectedDialogs.isEmpty()) {
                if (this.initialDialogsType == 3 && this.selectAlertString == null) {
                    this.actionBar.setTitle(LocaleController.getString("ForwardTo", R.string.ForwardTo));
                } else {
                    this.actionBar.setTitle(LocaleController.getString("SelectChat", R.string.SelectChat));
                }
                if (this.commentView.getTag() != null) {
                    this.commentView.hidePopup(false);
                    this.commentView.closeKeyboard();
                    AnimatorSet animatorSet = this.commentViewAnimator;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                    }
                    this.commentViewAnimator = new AnimatorSet();
                    this.commentView.setTranslationY(0.0f);
                    AnimatorSet animatorSet2 = this.commentViewAnimator;
                    ChatActivityEnterView chatActivityEnterView = this.commentView;
                    animatorSet2.playTogether(ObjectAnimator.ofFloat(chatActivityEnterView, View.TRANSLATION_Y, chatActivityEnterView.getMeasuredHeight()), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_X, 0.2f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_Y, 0.2f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_X, 0.2f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_Y, 0.2f), ObjectAnimator.ofFloat(this.selectedCountView, View.ALPHA, 0.0f));
                    this.commentViewAnimator.setDuration(180L);
                    this.commentViewAnimator.setInterpolator(new DecelerateInterpolator());
                    this.commentViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.46
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            DialogsActivity.this.commentView.setVisibility(8);
                            DialogsActivity.this.writeButtonContainer.setVisibility(8);
                        }
                    });
                    this.commentViewAnimator.start();
                    this.commentView.setTag(null);
                    this.fragmentView.requestLayout();
                }
            } else {
                this.selectedCountView.invalidate();
                if (this.commentView.getTag() == null) {
                    this.commentView.setFieldText("");
                    AnimatorSet animatorSet3 = this.commentViewAnimator;
                    if (animatorSet3 != null) {
                        animatorSet3.cancel();
                    }
                    this.commentView.setVisibility(0);
                    this.writeButtonContainer.setVisibility(0);
                    AnimatorSet animatorSet4 = new AnimatorSet();
                    this.commentViewAnimator = animatorSet4;
                    ChatActivityEnterView chatActivityEnterView2 = this.commentView;
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(chatActivityEnterView2, View.TRANSLATION_Y, chatActivityEnterView2.getMeasuredHeight(), 0.0f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.ALPHA, 1.0f));
                    this.commentViewAnimator.setDuration(180L);
                    this.commentViewAnimator.setInterpolator(new DecelerateInterpolator());
                    this.commentViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.47
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            DialogsActivity.this.commentView.setTag(2);
                            DialogsActivity.this.commentView.requestLayout();
                        }
                    });
                    this.commentViewAnimator.start();
                    this.commentView.setTag(1);
                }
                this.actionBar.setTitle(LocaleController.formatPluralString("Recipient", this.selectedDialogs.size(), new Object[0]));
            }
        } else if (this.initialDialogsType == 10) {
            hideFloatingButton(this.selectedDialogs.isEmpty());
        }
        ArrayList<Long> arrayList = this.selectedDialogs;
        ChatActivityEnterView chatActivityEnterView3 = this.commentView;
        boolean shouldShowNextButton = shouldShowNextButton(this, arrayList, chatActivityEnterView3 != null ? chatActivityEnterView3.getFieldText() : "", false);
        this.isNextButton = shouldShowNextButton;
        AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[0], !shouldShowNextButton, 0.5f, true);
        AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[1], this.isNextButton, 0.5f, true);
    }

    @TargetApi(23)
    private void askForPermissons(boolean z) {
        final Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i = Build.VERSION.SDK_INT;
        if (i >= 33 && NotificationPermissionDialog.shouldAsk(parentActivity)) {
            if (z) {
                showDialog(new NotificationPermissionDialog(parentActivity, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda116
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        DialogsActivity.lambda$askForPermissons$99(parentActivity, (Boolean) obj);
                    }
                }));
                return;
            }
            arrayList.add("android.permission.POST_NOTIFICATIONS");
        }
        if (getUserConfig().syncContacts && this.askAboutContacts && parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
            if (z) {
                AlertDialog create = AlertsCreator.createContactsPermissionDialog(parentActivity, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda112
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i2) {
                        DialogsActivity.this.lambda$askForPermissons$100(i2);
                    }
                }).create();
                this.permissionDialog = create;
                showDialog(create);
                return;
            }
            arrayList.add("android.permission.READ_CONTACTS");
            arrayList.add("android.permission.WRITE_CONTACTS");
            arrayList.add("android.permission.GET_ACCOUNTS");
        }
        if (i >= 33) {
            if (parentActivity.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") != 0) {
                arrayList.add("android.permission.READ_MEDIA_IMAGES");
            }
            if (parentActivity.checkSelfPermission("android.permission.READ_MEDIA_VIDEO") != 0) {
                arrayList.add("android.permission.READ_MEDIA_VIDEO");
            }
            if (parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
            }
        } else if ((i <= 28 || BuildVars.NO_SCOPED_STORAGE) && parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (arrayList.isEmpty()) {
            if (this.askingForPermissions) {
                this.askingForPermissions = false;
                showFiltersHint();
                return;
            }
            return;
        }
        try {
            parentActivity.requestPermissions((String[]) arrayList.toArray(new String[0]), 1);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$askForPermissons$99(Activity activity, Boolean bool) {
        if (bool.booleanValue()) {
            activity.requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$askForPermissons$100(int i) {
        this.askAboutContacts = i != 0;
        MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", this.askAboutContacts).commit();
        askForPermissons(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        super.onDialogDismiss(dialog);
        AlertDialog alertDialog = this.permissionDialog;
        if (alertDialog == null || dialog != alertDialog || getParentActivity() == null) {
            return;
        }
        askForPermissons(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        FrameLayout frameLayout;
        super.onConfigurationChanged(configuration);
        ItemOptions itemOptions = this.filterOptions;
        if (itemOptions != null) {
            itemOptions.dismiss();
        }
        if (this.onlySelect || (frameLayout = this.floatingButtonContainer) == null) {
            return;
        }
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.DialogsActivity.48
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                DialogsActivity dialogsActivity = DialogsActivity.this;
                dialogsActivity.floatingButtonTranslation = dialogsActivity.floatingHidden ? AndroidUtilities.dp(100.0f) : 0.0f;
                DialogsActivity.this.updateFloatingButtonOffset();
                DialogsActivity.this.floatingButtonContainer.setClickable(!DialogsActivity.this.floatingHidden);
                if (DialogsActivity.this.floatingButtonContainer != null) {
                    DialogsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        FilesMigrationService.FilesMigrationBottomSheet filesMigrationBottomSheet;
        boolean z = true;
        if (i != 1) {
            if (i == 4) {
                int i2 = 0;
                while (true) {
                    if (i2 >= iArr.length) {
                        break;
                    } else if (iArr[i2] != 0) {
                        z = false;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (!z || Build.VERSION.SDK_INT < 30 || (filesMigrationBottomSheet = FilesMigrationService.filesMigrationBottomSheet) == null) {
                    return;
                }
                filesMigrationBottomSheet.migrateOldFolder();
                return;
            }
            return;
        }
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (iArr.length > i3) {
                String str = strArr[i3];
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1925850455:
                        if (str.equals("android.permission.POST_NOTIFICATIONS")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 1365911975:
                        if (str.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1977429404:
                        if (str.equals("android.permission.READ_CONTACTS")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        if (iArr[i3] == 0) {
                            NotificationsController.getInstance(this.currentAccount).showNotifications();
                            break;
                        } else {
                            NotificationPermissionDialog.askLater();
                            continue;
                        }
                    case 1:
                        if (iArr[i3] == 0) {
                            ImageLoader.getInstance().checkMediaPaths();
                            break;
                        } else {
                            continue;
                        }
                    case 2:
                        if (iArr[i3] == 0) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda78
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.this.lambda$onRequestPermissionsResultFragment$101();
                                }
                            });
                            getContactsController().forceImportContacts();
                            continue;
                        } else {
                            SharedPreferences.Editor edit = MessagesController.getGlobalNotificationsSettings().edit();
                            this.askAboutContacts = false;
                            edit.putBoolean("askAboutContacts", false).commit();
                            break;
                        }
                }
            }
        }
        if (this.askingForPermissions) {
            this.askingForPermissions = false;
            showFiltersHint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestPermissionsResultFragment$101() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.forceImportContactsStart, new Object[0]);
    }

    private void reloadViewPageDialogs(ViewPage viewPage, boolean z) {
        int i;
        int i2;
        if (viewPage.getVisibility() != 0) {
            return;
        }
        int currentCount = viewPage.dialogsAdapter.getCurrentCount();
        if (viewPage.dialogsType == 0 && hasHiddenArchive() && viewPage.listView.getChildCount() == 0 && viewPage.archivePullViewState == 2) {
            ((LinearLayoutManager) viewPage.listView.getLayoutManager()).scrollToPositionWithOffset(1, (int) this.scrollYOffset);
        }
        if (viewPage.dialogsAdapter.isDataSetChanged() || z) {
            viewPage.dialogsAdapter.updateHasHints();
            int itemCount = viewPage.dialogsAdapter.getItemCount();
            if (itemCount == 1 && currentCount == 1 && viewPage.dialogsAdapter.getItemViewType(0) == 5) {
                viewPage.updateList(true);
            } else {
                viewPage.updateList(false);
                if (itemCount > currentCount && (i = this.initialDialogsType) != 11 && i != 12 && i != 13) {
                    viewPage.recyclerItemsEnterAnimator.showItemsAnimated(currentCount);
                }
            }
        } else {
            updateVisibleRows(MessagesController.UPDATE_MASK_NEW_MESSAGE);
            if (viewPage.dialogsAdapter.getItemCount() > currentCount && (i2 = this.initialDialogsType) != 11 && i2 != 12 && i2 != 13) {
                viewPage.recyclerItemsEnterAnimator.showItemsAnimated(currentCount);
            }
        }
        try {
            viewPage.listView.setEmptyView(this.folderId == 0 ? viewPage.progressView : null);
        } catch (Exception e) {
            FileLog.e(e);
        }
        checkListLoad(viewPage);
    }

    public void setPanTranslationOffset(float f) {
        this.floatingButtonPanOffset = f;
        updateFloatingButtonOffset();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, final Object... objArr) {
        MessagesController.DialogFilter dialogFilter;
        final boolean booleanValue;
        final boolean z;
        DialogsSearchAdapter dialogsSearchAdapter;
        DialogsSearchAdapter dialogsSearchAdapter2;
        MessagesController.DialogFilter dialogFilter2;
        int i3 = 0;
        if (i == NotificationCenter.dialogsNeedReload) {
            if (this.viewPages == null || this.dialogsListFrozen) {
                return;
            }
            int i4 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i4 >= viewPageArr.length) {
                    break;
                }
                final ViewPage viewPage = viewPageArr[i4];
                if (viewPageArr[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) {
                    dialogFilter2 = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
                } else {
                    dialogFilter2 = null;
                }
                boolean z2 = (dialogFilter2 == null || (dialogFilter2.flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ) == 0) ? false : true;
                if (this.slowedReloadAfterDialogClick && z2) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda100
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.this.lambda$didReceivedNotification$102(viewPage, objArr);
                        }
                    }, 160L);
                } else {
                    reloadViewPageDialogs(viewPage, objArr.length > 0);
                }
                i4++;
            }
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null && filterTabsView.getVisibility() == 0) {
                this.filterTabsView.checkTabsCounter();
            }
            this.slowedReloadAfterDialogClick = false;
        } else if (i == NotificationCenter.dialogsUnreadCounterChanged) {
            FilterTabsView filterTabsView2 = this.filterTabsView;
            if (filterTabsView2 == null || filterTabsView2.getVisibility() != 0) {
                return;
            }
            FilterTabsView filterTabsView3 = this.filterTabsView;
            filterTabsView3.notifyTabCounterChanged(filterTabsView3.getDefaultTabId());
        } else if (i == NotificationCenter.dialogsUnreadReactionsCounterChanged) {
            updateVisibleRows(0);
        } else if (i == NotificationCenter.emojiLoaded) {
            if (this.viewPages != null) {
                int i5 = 0;
                while (true) {
                    ViewPage[] viewPageArr2 = this.viewPages;
                    if (i5 >= viewPageArr2.length) {
                        break;
                    }
                    DialogsRecyclerView dialogsRecyclerView = viewPageArr2[i5].listView;
                    if (dialogsRecyclerView != null) {
                        for (int i6 = 0; i6 < dialogsRecyclerView.getChildCount(); i6++) {
                            View childAt = dialogsRecyclerView.getChildAt(i6);
                            if (childAt != null) {
                                childAt.invalidate();
                            }
                        }
                    }
                    i5++;
                }
            }
            FilterTabsView filterTabsView4 = this.filterTabsView;
            if (filterTabsView4 != null) {
                filterTabsView4.getTabsContainer().invalidateViews();
            }
        } else if (i == NotificationCenter.closeSearchByActiveAction) {
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
        } else if (i == NotificationCenter.proxySettingsChanged) {
            updateProxyButton(false, false);
        } else if (i == NotificationCenter.updateInterfaces) {
            Integer num = (Integer) objArr[0];
            updateVisibleRows(num.intValue());
            FilterTabsView filterTabsView5 = this.filterTabsView;
            if (filterTabsView5 != null && filterTabsView5.getVisibility() == 0 && (num.intValue() & MessagesController.UPDATE_MASK_READ_DIALOG_MESSAGE) != 0) {
                this.filterTabsView.checkTabsCounter();
            }
            if (this.viewPages != null) {
                while (i3 < this.viewPages.length) {
                    if ((num.intValue() & MessagesController.UPDATE_MASK_STATUS) != 0) {
                        this.viewPages[i3].dialogsAdapter.sortOnlineContacts(true);
                    }
                    i3++;
                }
            }
            updateStatus(UserConfig.getInstance(i2).getCurrentUser(), true);
        } else if (i == NotificationCenter.appDidLogout) {
            dialogsLoaded[this.currentAccount] = false;
        } else if (i == NotificationCenter.encryptedChatUpdated) {
            updateVisibleRows(0);
        } else if (i == NotificationCenter.contactsDidLoad) {
            if (this.viewPages == null || this.dialogsListFrozen) {
                return;
            }
            boolean z3 = this.floatingProgressVisible;
            setFloatingProgressVisible(false, true);
            for (ViewPage viewPage2 : this.viewPages) {
                viewPage2.dialogsAdapter.setForceUpdatingContacts(false);
            }
            if (z3) {
                setContactsAlpha(0.0f);
                animateContactsAlpha(1.0f);
            }
            int i7 = 0;
            boolean z4 = false;
            while (true) {
                ViewPage[] viewPageArr3 = this.viewPages;
                if (i7 >= viewPageArr3.length) {
                    break;
                }
                if (!viewPageArr3[i7].isDefaultDialogType() || getMessagesController().getAllFoldersDialogsCount() > 10) {
                    z4 = true;
                } else {
                    this.viewPages[i7].dialogsAdapter.notifyDataSetChanged();
                }
                i7++;
            }
            if (z4) {
                updateVisibleRows(0);
            }
        } else if (i == NotificationCenter.openedChatChanged) {
            if (this.viewPages == null) {
                return;
            }
            int i8 = 0;
            while (true) {
                ViewPage[] viewPageArr4 = this.viewPages;
                if (i8 < viewPageArr4.length) {
                    if (viewPageArr4[i8].isDefaultDialogType() && AndroidUtilities.isTablet()) {
                        boolean booleanValue2 = ((Boolean) objArr[2]).booleanValue();
                        long longValue = ((Long) objArr[0]).longValue();
                        long longValue2 = ((Long) objArr[1]).longValue();
                        if (booleanValue2) {
                            MessagesStorage.TopicKey topicKey = this.openedDialogId;
                            if (longValue == topicKey.dialogId && longValue2 == topicKey.topicId) {
                                topicKey.dialogId = 0L;
                                topicKey.topicId = 0L;
                            }
                        } else {
                            MessagesStorage.TopicKey topicKey2 = this.openedDialogId;
                            topicKey2.dialogId = longValue;
                            topicKey2.topicId = longValue2;
                        }
                        this.viewPages[i8].dialogsAdapter.setOpenedDialogId(this.openedDialogId.dialogId);
                    }
                    i8++;
                } else {
                    updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
                    return;
                }
            }
        } else if (i == NotificationCenter.notificationsSettingsUpdated) {
            updateVisibleRows(0);
        } else if (i == NotificationCenter.messageReceivedByAck || i == NotificationCenter.messageReceivedByServer || i == NotificationCenter.messageSendError) {
            updateVisibleRows(MessagesController.UPDATE_MASK_SEND_STATE);
        } else if (i == NotificationCenter.didSetPasscode) {
            updatePasscodeButton();
        } else if (i == NotificationCenter.needReloadRecentDialogsSearch) {
            SearchViewPager searchViewPager = this.searchViewPager;
            if (searchViewPager == null || (dialogsSearchAdapter2 = searchViewPager.dialogsSearchAdapter) == null) {
                return;
            }
            dialogsSearchAdapter2.loadRecentSearch();
        } else if (i == NotificationCenter.replyMessagesDidLoad) {
            updateVisibleRows(MessagesController.UPDATE_MASK_MESSAGE_TEXT);
        } else if (i == NotificationCenter.reloadHints) {
            SearchViewPager searchViewPager2 = this.searchViewPager;
            if (searchViewPager2 == null || (dialogsSearchAdapter = searchViewPager2.dialogsSearchAdapter) == null) {
                return;
            }
            dialogsSearchAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.didUpdateConnectionState) {
            int connectionState = AccountInstance.getInstance(i2).getConnectionsManager().getConnectionState();
            if (this.currentConnectionState != connectionState) {
                this.currentConnectionState = connectionState;
                updateProxyButton(true, false);
            }
        } else if (i == NotificationCenter.onDownloadingFilesChanged) {
            updateProxyButton(true, false);
            SearchViewPager searchViewPager3 = this.searchViewPager;
            if (searchViewPager3 != null) {
                updateSpeedItem(searchViewPager3.getCurrentPosition() == 2);
            }
        } else if (i == NotificationCenter.needDeleteDialog) {
            if (this.fragmentView == null || this.isPaused) {
                return;
            }
            final long longValue3 = ((Long) objArr[0]).longValue();
            final TLRPC$User tLRPC$User = (TLRPC$User) objArr[1];
            final TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) objArr[2];
            if (tLRPC$User != null && tLRPC$User.bot) {
                z = ((Boolean) objArr[3]).booleanValue();
                booleanValue = false;
            } else {
                booleanValue = ((Boolean) objArr[3]).booleanValue();
                z = false;
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda97
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$didReceivedNotification$103(tLRPC$Chat, longValue3, booleanValue, tLRPC$User, z);
                }
            };
            createUndoView();
            if (this.undoView[0] != null) {
                if (!ChatObject.isForum(tLRPC$Chat)) {
                    UndoView undoView = getUndoView();
                    if (undoView != null) {
                        undoView.showWithAction(longValue3, 1, runnable);
                        return;
                    }
                    return;
                }
                runnable.run();
                return;
            }
            runnable.run();
        } else if (i == NotificationCenter.folderBecomeEmpty) {
            int intValue = ((Integer) objArr[0]).intValue();
            int i9 = this.folderId;
            if (i9 != intValue || i9 == 0) {
                return;
            }
            finishFragment();
        } else if (i == NotificationCenter.dialogFiltersUpdated) {
            updateFilterTabs(true, true);
        } else if (i == NotificationCenter.filterSettingsUpdated) {
            showFiltersHint();
        } else if (i == NotificationCenter.newSuggestionsAvailable) {
            showNextSupportedSuggestion();
            lambda$updateDialogsHint$29();
        } else if (i == NotificationCenter.forceImportContactsStart) {
            setFloatingProgressVisible(true, true);
            ViewPage[] viewPageArr5 = this.viewPages;
            if (viewPageArr5 != null) {
                for (ViewPage viewPage3 : viewPageArr5) {
                    viewPage3.dialogsAdapter.setForceShowEmptyCell(false);
                    viewPage3.dialogsAdapter.setForceUpdatingContacts(true);
                    viewPage3.dialogsAdapter.notifyDataSetChanged();
                }
            }
        } else if (i == NotificationCenter.messagesDeleted) {
            if (!this.searchIsShowed || this.searchViewPager == null) {
                return;
            }
            this.searchViewPager.messagesDeleted(((Long) objArr[1]).longValue(), (ArrayList) objArr[0]);
        } else if (i == NotificationCenter.didClearDatabase) {
            if (this.viewPages != null) {
                while (true) {
                    ViewPage[] viewPageArr6 = this.viewPages;
                    if (i3 >= viewPageArr6.length) {
                        break;
                    }
                    viewPageArr6[i3].dialogsAdapter.didDatabaseCleared();
                    i3++;
                }
            }
            SuggestClearDatabaseBottomSheet.dismissDialog();
        } else if (i == NotificationCenter.appUpdateAvailable) {
            updateMenuButton(true);
        } else if (i == NotificationCenter.fileLoaded || i == NotificationCenter.fileLoadFailed || i == NotificationCenter.fileLoadProgressChanged) {
            String str = (String) objArr[0];
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str)) {
                updateMenuButton(true);
            }
        } else if (i == NotificationCenter.onDatabaseMigration) {
            boolean booleanValue3 = ((Boolean) objArr[0]).booleanValue();
            if (this.fragmentView != null) {
                if (booleanValue3) {
                    if (this.databaseMigrationHint == null) {
                        DatabaseMigrationHint databaseMigrationHint = new DatabaseMigrationHint(this.fragmentView.getContext(), this.currentAccount);
                        this.databaseMigrationHint = databaseMigrationHint;
                        databaseMigrationHint.setAlpha(0.0f);
                        ((ContentView) this.fragmentView).addView(this.databaseMigrationHint);
                        this.databaseMigrationHint.animate().alpha(1.0f).setDuration(300L).setStartDelay(1000L).start();
                    }
                    this.databaseMigrationHint.setTag(1);
                    return;
                }
                View view = this.databaseMigrationHint;
                if (view == null || view.getTag() == null) {
                    return;
                }
                final View view2 = this.databaseMigrationHint;
                view2.animate().setListener(null).cancel();
                view2.animate().setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.49
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (view2.getParent() != null) {
                            ((ViewGroup) view2.getParent()).removeView(view2);
                        }
                        DialogsActivity.this.databaseMigrationHint = null;
                    }
                }).alpha(0.0f).setStartDelay(0L).setDuration(150L).start();
                this.databaseMigrationHint.setTag(null);
            }
        } else if (i == NotificationCenter.onDatabaseOpened) {
            checkSuggestClearDatabase();
        } else if (i == NotificationCenter.userEmojiStatusUpdated) {
            updateStatus((TLRPC$User) objArr[0], true);
        } else if (i == NotificationCenter.currentUserPremiumStatusChanged) {
            updateStatus(UserConfig.getInstance(i2).getCurrentUser(), true);
            updateStoriesPosting();
        } else if (i == NotificationCenter.onDatabaseReset) {
            dialogsLoaded[this.currentAccount] = false;
            loadDialogs(getAccountInstance());
            getMessagesController().loadPinnedDialogs(this.folderId, 0L, null);
        } else if (i == NotificationCenter.chatlistFolderUpdate) {
            int intValue2 = ((Integer) objArr[0]).intValue();
            while (true) {
                ViewPage[] viewPageArr7 = this.viewPages;
                if (i3 >= viewPageArr7.length) {
                    return;
                }
                ViewPage viewPage4 = viewPageArr7[i3];
                if (viewPage4 != null && ((viewPage4.dialogsType == 7 || viewPage4.dialogsType == 8) && (dialogFilter = getMessagesController().selectedDialogFilter[viewPage4.dialogsType - 7]) != null && intValue2 == dialogFilter.id)) {
                    viewPage4.updateList(true);
                    return;
                }
                i3++;
            }
        } else if (i == NotificationCenter.dialogTranslate) {
            long longValue4 = ((Long) objArr[0]).longValue();
            int i10 = 0;
            while (true) {
                ViewPage[] viewPageArr8 = this.viewPages;
                if (i10 >= viewPageArr8.length) {
                    return;
                }
                ViewPage viewPage5 = viewPageArr8[i10];
                if (viewPage5.listView != null) {
                    int i11 = 0;
                    while (true) {
                        if (i11 < viewPage5.listView.getChildCount()) {
                            View childAt2 = viewPage5.listView.getChildAt(i11);
                            if (childAt2 instanceof DialogCell) {
                                DialogCell dialogCell = (DialogCell) childAt2;
                                if (longValue4 == dialogCell.getDialogId()) {
                                    dialogCell.buildLayout();
                                    break;
                                }
                            }
                            i11++;
                        }
                    }
                }
                i10++;
            }
        } else if (i == NotificationCenter.storiesUpdated) {
            updateStoriesVisibility(this.wasDrawn);
            updateVisibleRows(0);
        } else if (i == NotificationCenter.storiesEnabledUpdate) {
            updateStoriesPosting();
        } else if (i == NotificationCenter.unconfirmedAuthUpdate) {
            lambda$updateDialogsHint$29();
        } else if (i == NotificationCenter.premiumPromoUpdated) {
            lambda$updateDialogsHint$29();
        } else if (i == NotificationCenter.starBalanceUpdated || i == NotificationCenter.starSubscriptionsLoaded) {
            lambda$updateDialogsHint$29();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$102(ViewPage viewPage, Object[] objArr) {
        reloadViewPageDialogs(viewPage, objArr.length > 0);
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView == null || filterTabsView.getVisibility() != 0) {
            return;
        }
        this.filterTabsView.checkTabsCounter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$103(TLRPC$Chat tLRPC$Chat, long j, boolean z, TLRPC$User tLRPC$User, boolean z2) {
        if (tLRPC$Chat != null) {
            if (ChatObject.isNotInChat(tLRPC$Chat)) {
                getMessagesController().deleteDialog(j, 0, z);
            } else {
                getMessagesController().deleteParticipantFromChat(-j, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), (TLRPC$Chat) null, z, z);
            }
        } else {
            getMessagesController().deleteDialog(j, 0, z);
            if (tLRPC$User != null && tLRPC$User.bot && z2) {
                getMessagesController().blockPeer(tLRPC$User.id);
            }
        }
        getMessagesController().checkIfFolderEmpty(this.folderId);
    }

    private void checkSuggestClearDatabase() {
        if (getMessagesStorage().showClearDatabaseAlert) {
            getMessagesStorage().showClearDatabaseAlert = false;
            SuggestClearDatabaseBottomSheet.show(this);
        }
    }

    private void updateMenuButton(boolean z) {
        int i;
        if (this.menuDrawable == null || this.updateLayout == null) {
            return;
        }
        float f = 0.0f;
        if (SharedConfig.isAppUpdateAvailable()) {
            String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            if (getFileLoader().isLoadingFile(attachFileName)) {
                i = MenuDrawable.TYPE_UDPATE_DOWNLOADING;
                Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                if (fileProgress != null) {
                    f = fileProgress.floatValue();
                }
            } else {
                i = MenuDrawable.TYPE_UDPATE_AVAILABLE;
            }
        } else {
            i = MenuDrawable.TYPE_DEFAULT;
        }
        updateAppUpdateViews(z);
        this.menuDrawable.setType(i, z);
        this.menuDrawable.setUpdateDownloadProgress(f, z);
    }

    private void showNextSupportedSuggestion() {
        if (this.showingSuggestion != null) {
            return;
        }
        for (String str : getMessagesController().pendingSuggestions) {
            if (showSuggestion(str)) {
                this.showingSuggestion = str;
                return;
            }
        }
    }

    private void onSuggestionDismiss() {
        if (this.showingSuggestion == null) {
            return;
        }
        getMessagesController().removeSuggestion(0L, this.showingSuggestion);
        this.showingSuggestion = null;
        showNextSupportedSuggestion();
    }

    private boolean showSuggestion(String str) {
        if ("AUTOARCHIVE_POPULAR".equals(str)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("HideNewChatsAlertTitle", R.string.HideNewChatsAlertTitle));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("HideNewChatsAlertText", R.string.HideNewChatsAlertText)));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            builder.setPositiveButton(LocaleController.getString("GoToSettings", R.string.GoToSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DialogsActivity.this.lambda$showSuggestion$104(dialogInterface, i);
                }
            });
            showDialog(builder.create(), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda22
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    DialogsActivity.this.lambda$showSuggestion$105(dialogInterface);
                }
            });
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuggestion$104(DialogInterface dialogInterface, int i) {
        presentFragment(new PrivacySettingsActivity());
        AndroidUtilities.scrollToFragmentRow(this.parentLayout, "newChatsRow");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuggestion$105(DialogInterface dialogInterface) {
        onSuggestionDismiss();
    }

    private void showFiltersHint() {
        if (this.askingForPermissions || !getMessagesController().dialogFiltersLoaded || !getMessagesController().showFiltersTooltip || this.filterTabsView == null || !getMessagesController().getDialogFilters().isEmpty() || this.isPaused || !getUserConfig().filtersLoaded || this.inPreviewMode) {
            return;
        }
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (globalMainSettings.getBoolean("filterhint", false)) {
            return;
        }
        globalMainSettings.edit().putBoolean("filterhint", true).apply();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda72
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$showFiltersHint$107();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showFiltersHint$107() {
        UndoView undoView = getUndoView();
        if (undoView != null) {
            undoView.showWithAction(0L, 15, null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda74
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$showFiltersHint$106();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showFiltersHint$106() {
        presentFragment(new FiltersSetupActivity());
    }

    private void setDialogsListFrozen(boolean z, boolean z2) {
        if (this.viewPages == null || this.dialogsListFrozen == z) {
            return;
        }
        if (z) {
            this.frozenDialogsList = new ArrayList<>(getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false));
        } else {
            this.frozenDialogsList = null;
        }
        this.dialogsListFrozen = z;
        this.viewPages[0].dialogsAdapter.setDialogsListFrozen(z);
        if (z || !z2) {
            return;
        }
        if (!this.viewPages[0].listView.isComputingLayout()) {
            this.viewPages[0].dialogsAdapter.notifyDataSetChanged();
        } else {
            this.viewPages[0].listView.post(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda67
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$setDialogsListFrozen$108();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDialogsListFrozen$108() {
        this.viewPages[0].dialogsAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDialogsListFrozen(boolean z) {
        setDialogsListFrozen(z, true);
    }

    /* loaded from: classes4.dex */
    public class DialogsHeader extends TLRPC$Dialog {
        public int headerType;

        public DialogsHeader(DialogsActivity dialogsActivity, int i) {
            this.headerType = i;
        }
    }

    public ArrayList<TLRPC$Dialog> getDialogsArray(int i, int i2, int i3, boolean z) {
        boolean z2;
        ArrayList<TLRPC$Dialog> arrayList;
        if (!z || (arrayList = this.frozenDialogsList) == null) {
            MessagesController messagesController = AccountInstance.getInstance(i).getMessagesController();
            if (i2 == 0) {
                return messagesController.getDialogs(i3);
            }
            if (i2 == 10 || i2 == 13) {
                return messagesController.dialogsServerOnly;
            }
            boolean z3 = true;
            if (i2 == 2) {
                ArrayList<TLRPC$Dialog> arrayList2 = new ArrayList<>(messagesController.dialogsCanAddUsers.size() + messagesController.dialogsMyChannels.size() + messagesController.dialogsMyGroups.size() + 2);
                if (messagesController.dialogsMyChannels.size() > 0 && this.allowChannels) {
                    arrayList2.add(new DialogsHeader(this, 0));
                    arrayList2.addAll(messagesController.dialogsMyChannels);
                }
                if (messagesController.dialogsMyGroups.size() > 0 && this.allowGroups) {
                    arrayList2.add(new DialogsHeader(this, 1));
                    arrayList2.addAll(messagesController.dialogsMyGroups);
                }
                if (messagesController.dialogsCanAddUsers.size() > 0) {
                    int size = messagesController.dialogsCanAddUsers.size();
                    for (int i4 = 0; i4 < size; i4++) {
                        TLRPC$Dialog tLRPC$Dialog = messagesController.dialogsCanAddUsers.get(i4);
                        if ((this.allowChannels && ChatObject.isChannelAndNotMegaGroup(-tLRPC$Dialog.id, i)) || (this.allowGroups && (ChatObject.isMegagroup(i, -tLRPC$Dialog.id) || !ChatObject.isChannel(-tLRPC$Dialog.id, i)))) {
                            if (z3) {
                                arrayList2.add(new DialogsHeader(this, 2));
                                z3 = false;
                            }
                            arrayList2.add(tLRPC$Dialog);
                        }
                    }
                }
                return arrayList2;
            } else if (i2 == 3) {
                return messagesController.dialogsForward;
            } else {
                if (i2 == 4 || i2 == 12) {
                    return messagesController.dialogsUsersOnly;
                }
                if (i2 == 5) {
                    return messagesController.dialogsChannelsOnly;
                }
                if (i2 == 6 || i2 == 11) {
                    return messagesController.dialogsGroupsOnly;
                }
                if (i2 == 7 || i2 == 8) {
                    MessagesController.DialogFilter dialogFilter = messagesController.selectedDialogFilter[i2 != 7 ? (char) 1 : (char) 0];
                    if (dialogFilter == null) {
                        return messagesController.getDialogs(i3);
                    }
                    if (this.initialDialogsType == 3) {
                        return dialogFilter.dialogsForward;
                    }
                    return dialogFilter.dialogs;
                } else if (i2 == 9) {
                    return messagesController.dialogsForBlock;
                } else {
                    if (i2 == 1 || i2 == 14) {
                        ArrayList<TLRPC$Dialog> arrayList3 = this.botShareDialogs;
                        if (arrayList3 != null) {
                            return arrayList3;
                        }
                        this.botShareDialogs = new ArrayList<>();
                        if (this.allowUsers || this.allowBots) {
                            Iterator<TLRPC$Dialog> it = messagesController.dialogsUsersOnly.iterator();
                            while (it.hasNext()) {
                                TLRPC$Dialog next = it.next();
                                TLRPC$User user = messagesController.getUser(Long.valueOf(next.id));
                                if (user != null && !UserObject.isUserSelf(user)) {
                                    if (user.bot) {
                                        if (this.allowBots) {
                                            this.botShareDialogs.add(next);
                                        }
                                    } else if (this.allowUsers) {
                                        this.botShareDialogs.add(next);
                                    }
                                }
                            }
                        }
                        if (this.allowGroups || ((z2 = this.allowLegacyGroups) && this.allowMegagroups)) {
                            Iterator<TLRPC$Dialog> it2 = messagesController.dialogsGroupsOnly.iterator();
                            while (it2.hasNext()) {
                                TLRPC$Dialog next2 = it2.next();
                                TLRPC$Chat chat = messagesController.getChat(Long.valueOf(-next2.id));
                                if (chat != null && !ChatObject.isChannelAndNotMegaGroup(chat) && messagesController.canAddToForward(next2)) {
                                    this.botShareDialogs.add(next2);
                                }
                            }
                        } else if (z2 || this.allowMegagroups) {
                            Iterator<TLRPC$Dialog> it3 = messagesController.dialogsGroupsOnly.iterator();
                            while (it3.hasNext()) {
                                TLRPC$Dialog next3 = it3.next();
                                TLRPC$Chat chat2 = messagesController.getChat(Long.valueOf(-next3.id));
                                if (chat2 != null && !ChatObject.isChannelAndNotMegaGroup(chat2) && messagesController.canAddToForward(next3) && ((this.allowLegacyGroups && !ChatObject.isMegagroup(chat2)) || (this.allowMegagroups && ChatObject.isMegagroup(chat2)))) {
                                    this.botShareDialogs.add(next3);
                                }
                            }
                        }
                        if (this.allowChannels) {
                            Iterator<TLRPC$Dialog> it4 = messagesController.dialogsChannelsOnly.iterator();
                            while (it4.hasNext()) {
                                TLRPC$Dialog next4 = it4.next();
                                if (messagesController.canAddToForward(next4)) {
                                    this.botShareDialogs.add(next4);
                                }
                            }
                        }
                        getMessagesController().sortDialogsList(this.botShareDialogs);
                        return this.botShareDialogs;
                    } else if (i2 == 15) {
                        ArrayList<TLRPC$Dialog> arrayList4 = new ArrayList<>();
                        TLRPC$User user2 = messagesController.getUser(Long.valueOf(this.requestPeerBotId));
                        TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
                        if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeUser) {
                            ConcurrentHashMap<Long, TLRPC$User> users = messagesController.getUsers();
                            Iterator<TLRPC$Dialog> it5 = messagesController.dialogsUsersOnly.iterator();
                            while (it5.hasNext()) {
                                TLRPC$Dialog next5 = it5.next();
                                if (meetRequestPeerRequirements(getMessagesController().getUser(Long.valueOf(next5.id)))) {
                                    arrayList4.add(next5);
                                }
                            }
                            for (TLRPC$User tLRPC$User : users.values()) {
                                if (tLRPC$User != null && !messagesController.dialogs_dict.containsKey(tLRPC$User.id) && meetRequestPeerRequirements(tLRPC$User)) {
                                    TLRPC$TL_dialog tLRPC$TL_dialog = new TLRPC$TL_dialog();
                                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                    tLRPC$TL_dialog.peer = tLRPC$TL_peerUser;
                                    long j = tLRPC$User.id;
                                    tLRPC$TL_peerUser.user_id = j;
                                    tLRPC$TL_dialog.id = j;
                                    arrayList4.add(tLRPC$TL_dialog);
                                }
                            }
                        } else if ((tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeChat) || (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast)) {
                            ConcurrentHashMap<Long, TLRPC$Chat> chats = messagesController.getChats();
                            Iterator<TLRPC$Dialog> it6 = (this.requestPeerType instanceof TLRPC$TL_requestPeerTypeChat ? messagesController.dialogsGroupsOnly : messagesController.dialogsChannelsOnly).iterator();
                            while (it6.hasNext()) {
                                TLRPC$Dialog next6 = it6.next();
                                if (meetRequestPeerRequirements(user2, getMessagesController().getChat(Long.valueOf(-next6.id)))) {
                                    arrayList4.add(next6);
                                }
                            }
                            for (TLRPC$Chat tLRPC$Chat : chats.values()) {
                                if (tLRPC$Chat != null && !messagesController.dialogs_dict.containsKey(-tLRPC$Chat.id) && meetRequestPeerRequirements(user2, tLRPC$Chat)) {
                                    TLRPC$TL_dialog tLRPC$TL_dialog2 = new TLRPC$TL_dialog();
                                    if (ChatObject.isChannel(tLRPC$Chat)) {
                                        TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                                        tLRPC$TL_dialog2.peer = tLRPC$TL_peerChannel;
                                        tLRPC$TL_peerChannel.channel_id = tLRPC$Chat.id;
                                    } else {
                                        TLRPC$TL_peerChat tLRPC$TL_peerChat = new TLRPC$TL_peerChat();
                                        tLRPC$TL_dialog2.peer = tLRPC$TL_peerChat;
                                        tLRPC$TL_peerChat.chat_id = tLRPC$Chat.id;
                                    }
                                    tLRPC$TL_dialog2.id = -tLRPC$Chat.id;
                                    arrayList4.add(tLRPC$TL_dialog2);
                                }
                            }
                        }
                        return arrayList4;
                    } else {
                        return new ArrayList<>();
                    }
                }
            }
        }
        return arrayList;
    }

    private boolean meetRequestPeerRequirements(TLRPC$User tLRPC$User) {
        Boolean bool;
        Boolean bool2;
        TLRPC$TL_requestPeerTypeUser tLRPC$TL_requestPeerTypeUser = (TLRPC$TL_requestPeerTypeUser) this.requestPeerType;
        return (tLRPC$User == null || UserObject.isReplyUser(tLRPC$User) || UserObject.isDeleted(tLRPC$User) || ((bool = tLRPC$TL_requestPeerTypeUser.bot) != null && bool.booleanValue() != tLRPC$User.bot) || ((bool2 = tLRPC$TL_requestPeerTypeUser.premium) != null && bool2.booleanValue() != tLRPC$User.premium)) ? false : true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x004b, code lost:
        if (r2.booleanValue() == (org.telegram.messenger.ChatObject.getPublicUsername(r7) != null)) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean meetRequestPeerRequirements(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        Boolean bool;
        Boolean bool2;
        if (tLRPC$Chat != null) {
            boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
            TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
            if (isChannelAndNotMegaGroup == (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) && (((bool = tLRPC$RequestPeerType.creator) == null || !bool.booleanValue() || tLRPC$Chat.creator) && ((bool2 = this.requestPeerType.bot_participant) == null || !bool2.booleanValue() || getMessagesController().isInChatCached(tLRPC$Chat, tLRPC$User) || ChatObject.canAddBotsToChat(tLRPC$Chat)))) {
                Boolean bool3 = this.requestPeerType.has_username;
                if (bool3 != null) {
                }
                Boolean bool4 = this.requestPeerType.forum;
                if ((bool4 == null || bool4.booleanValue() == ChatObject.isForum(tLRPC$Chat)) && ((this.requestPeerType.user_admin_rights == null || getMessagesController().matchesAdminRights(tLRPC$Chat, getUserConfig().getCurrentUser(), this.requestPeerType.user_admin_rights)) && (this.requestPeerType.bot_admin_rights == null || getMessagesController().matchesAdminRights(tLRPC$Chat, tLRPC$User, this.requestPeerType.bot_admin_rights) || ChatObject.canAddAdmins(tLRPC$Chat)))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSideMenu(RecyclerView recyclerView) {
        this.sideMenu = recyclerView;
        int i = Theme.key_chats_menuBackground;
        recyclerView.setBackgroundColor(Theme.getColor(i));
        this.sideMenu.setGlowColor(Theme.getColor(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePasscodeButton() {
        if (this.passcodeItem == null) {
            return;
        }
        if (SharedConfig.passcodeHash.length() != 0 && !this.searching) {
            ActionBarMenuItem actionBarMenuItem = this.doneItem;
            if (actionBarMenuItem == null || actionBarMenuItem.getVisibility() != 0) {
                this.passcodeItem.setVisibility(0);
            }
            this.passcodeItem.setIcon(this.passcodeDrawable);
            this.passcodeItemVisible = true;
            return;
        }
        this.passcodeItem.setVisibility(8);
        this.passcodeItemVisible = false;
    }

    private void setFloatingProgressVisible(final boolean z, boolean z2) {
        if (this.floatingButton2 == null || this.floating2ProgressView == null) {
            return;
        }
        if (z2) {
            if (z == this.floatingProgressVisible) {
                return;
            }
            AnimatorSet animatorSet = this.floatingProgressAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.floatingProgressVisible = z;
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.floatingProgressAnimator = animatorSet2;
            Animator[] animatorArr = new Animator[6];
            RLottieImageView rLottieImageView = this.floatingButton2;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z ? 0.0f : 1.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(rLottieImageView, property, fArr);
            RLottieImageView rLottieImageView2 = this.floatingButton2;
            Property property2 = View.SCALE_X;
            float[] fArr2 = new float[1];
            fArr2[0] = z ? 0.1f : 1.0f;
            animatorArr[1] = ObjectAnimator.ofFloat(rLottieImageView2, property2, fArr2);
            RLottieImageView rLottieImageView3 = this.floatingButton2;
            Property property3 = View.SCALE_Y;
            float[] fArr3 = new float[1];
            fArr3[0] = z ? 0.1f : 1.0f;
            animatorArr[2] = ObjectAnimator.ofFloat(rLottieImageView3, property3, fArr3);
            RadialProgressView radialProgressView = this.floating2ProgressView;
            Property property4 = View.ALPHA;
            float[] fArr4 = new float[1];
            fArr4[0] = z ? 1.0f : 0.0f;
            animatorArr[3] = ObjectAnimator.ofFloat(radialProgressView, property4, fArr4);
            RadialProgressView radialProgressView2 = this.floating2ProgressView;
            Property property5 = View.SCALE_X;
            float[] fArr5 = new float[1];
            fArr5[0] = z ? 1.0f : 0.1f;
            animatorArr[4] = ObjectAnimator.ofFloat(radialProgressView2, property5, fArr5);
            RadialProgressView radialProgressView3 = this.floating2ProgressView;
            Property property6 = View.SCALE_Y;
            float[] fArr6 = new float[1];
            fArr6[0] = z ? 1.0f : 0.1f;
            animatorArr[5] = ObjectAnimator.ofFloat(radialProgressView3, property6, fArr6);
            animatorSet2.playTogether(animatorArr);
            this.floatingProgressAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.50
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    DialogsActivity.this.floating2ProgressView.setVisibility(0);
                    DialogsActivity.this.floatingButton2.setVisibility(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator == DialogsActivity.this.floatingProgressAnimator) {
                        if (z) {
                            if (DialogsActivity.this.floatingButton2 != null) {
                                DialogsActivity.this.floatingButton2.setVisibility(8);
                            }
                        } else if (DialogsActivity.this.floatingButton2 != null) {
                            DialogsActivity.this.floating2ProgressView.setVisibility(8);
                        }
                        DialogsActivity.this.floatingProgressAnimator = null;
                    }
                }
            });
            this.floatingProgressAnimator.setDuration(150L);
            this.floatingProgressAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.floatingProgressAnimator.start();
            return;
        }
        AnimatorSet animatorSet3 = this.floatingProgressAnimator;
        if (animatorSet3 != null) {
            animatorSet3.cancel();
        }
        this.floatingProgressVisible = z;
        if (z) {
            this.floatingButton2.setAlpha(0.0f);
            this.floatingButton2.setScaleX(0.1f);
            this.floatingButton2.setScaleY(0.1f);
            this.floatingButton2.setVisibility(8);
            this.floating2ProgressView.setAlpha(1.0f);
            this.floating2ProgressView.setScaleX(1.0f);
            this.floating2ProgressView.setScaleY(1.0f);
            this.floating2ProgressView.setVisibility(0);
            return;
        }
        this.floatingButton2.setAlpha(1.0f);
        this.floatingButton2.setScaleX(1.0f);
        this.floatingButton2.setScaleY(1.0f);
        this.floatingButton2.setVisibility(0);
        this.floating2ProgressView.setAlpha(0.0f);
        this.floating2ProgressView.setScaleX(0.1f);
        this.floating2ProgressView.setScaleY(0.1f);
        this.floating2ProgressView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFloatingButton(boolean z) {
        HintView2 hintView2;
        if (this.rightSlidingDialogContainer.hasFragment()) {
            z = true;
        }
        if (this.floatingHidden != z) {
            if (z && this.floatingForceVisible) {
                return;
            }
            this.floatingHidden = z;
            AnimatorSet animatorSet = new AnimatorSet();
            float[] fArr = new float[2];
            fArr[0] = this.floatingButtonHideProgress;
            fArr[1] = this.floatingHidden ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    DialogsActivity.this.lambda$hideFloatingButton$109(valueAnimator);
                }
            });
            animatorSet.playTogether(ofFloat);
            animatorSet.setDuration(300L);
            animatorSet.setInterpolator(this.floatingInterpolator);
            this.floatingButtonContainer.setClickable(!z);
            animatorSet.start();
            if (!z || (hintView2 = this.storyHint) == null) {
                return;
            }
            hintView2.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideFloatingButton$109(ValueAnimator valueAnimator) {
        this.floatingButtonHideProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingButtonTranslation = AndroidUtilities.dp(100.0f) * this.floatingButtonHideProgress;
        updateFloatingButtonOffset();
    }

    public void animateContactsAlpha(float f) {
        ValueAnimator valueAnimator = this.contactsAlphaAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator duration = ValueAnimator.ofFloat(this.contactsAlpha, f).setDuration(250L);
        this.contactsAlphaAnimator = duration;
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.contactsAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                DialogsActivity.this.lambda$animateContactsAlpha$110(valueAnimator2);
            }
        });
        this.contactsAlphaAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateContactsAlpha$110(ValueAnimator valueAnimator) {
        setContactsAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void setContactsAlpha(float f) {
        ViewPage[] viewPageArr;
        this.contactsAlpha = f;
        for (ViewPage viewPage : this.viewPages) {
            DialogsRecyclerView dialogsRecyclerView = viewPage.listView;
            for (int i = 0; i < dialogsRecyclerView.getChildCount(); i++) {
                View childAt = dialogsRecyclerView.getChildAt(i);
                if (childAt != null && dialogsRecyclerView.getChildAdapterPosition(childAt) >= viewPage.dialogsAdapter.getDialogsCount() + 1) {
                    childAt.setAlpha(f);
                }
            }
        }
    }

    public void setScrollDisabled(boolean z) {
        for (ViewPage viewPage : this.viewPages) {
            ((LinearLayoutManager) viewPage.listView.getLayoutManager()).setScrollDisabled(z);
        }
    }

    private void updateDialogIndices() {
        if (this.viewPages == null) {
            return;
        }
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            if (viewPageArr[i].getVisibility() == 0 && !this.viewPages[i].dialogsAdapter.getDialogsListIsFrozen()) {
                this.viewPages[i].updateList(false);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVisibleRows(int i) {
        updateVisibleRows(i, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:81:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0128  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateVisibleRows(int i, boolean z) {
        RecyclerListView recyclerListView;
        int i2;
        int childCount;
        int i3;
        if ((this.dialogsListFrozen && (MessagesController.UPDATE_MASK_REORDER & i) == 0) || this.isPaused) {
            return;
        }
        int i4 = 0;
        while (i4 < 3) {
            if (i4 == 2) {
                SearchViewPager searchViewPager = this.searchViewPager;
                recyclerListView = searchViewPager != null ? searchViewPager.searchListView : null;
            } else {
                ViewPage[] viewPageArr = this.viewPages;
                if (viewPageArr != null) {
                    DialogsRecyclerView dialogsRecyclerView = i4 < viewPageArr.length ? viewPageArr[i4].listView : null;
                    if (dialogsRecyclerView == null || viewPageArr[i4].getVisibility() == 0) {
                        r5 = dialogsRecyclerView != null ? this.viewPages[i4] : null;
                        recyclerListView = dialogsRecyclerView;
                    }
                }
                i4++;
            }
            if (recyclerListView != null && recyclerListView.getAdapter() != null) {
                if (((MessagesController.UPDATE_MASK_NEW_MESSAGE & i) != 0 || i == 0) && r5 != null) {
                    r5.updateList(false);
                } else {
                    int childCount2 = recyclerListView.getChildCount();
                    while (i2 < childCount2) {
                        View childAt = recyclerListView.getChildAt(i2);
                        if ((childAt instanceof DialogCell) && (this.searchViewPager == null || recyclerListView.getAdapter() != this.searchViewPager.dialogsSearchAdapter)) {
                            DialogCell dialogCell = (DialogCell) childAt;
                            if ((MessagesController.UPDATE_MASK_REORDER & i) != 0) {
                                dialogCell.onReorderStateChanged(this.actionBar.isActionModeShowed(), true);
                                i2 = this.dialogsListFrozen ? i2 + 1 : 0;
                            }
                            if ((MessagesController.UPDATE_MASK_CHECK & i) != 0) {
                                dialogCell.setChecked(false, (MessagesController.UPDATE_MASK_CHAT & i) != 0);
                            } else {
                                if ((MessagesController.UPDATE_MASK_SELECT_DIALOG & i) != 0) {
                                    if (this.viewPages[i4].isDefaultDialogType() && AndroidUtilities.isTablet()) {
                                        dialogCell.setDialogSelected(dialogCell.getDialogId() == this.openedDialogId.dialogId);
                                    }
                                } else if (dialogCell.update(i, z)) {
                                    r5.updateList(false);
                                    break;
                                }
                                ArrayList<Long> arrayList = this.selectedDialogs;
                                if (arrayList != null) {
                                    dialogCell.setChecked(arrayList.contains(Long.valueOf(dialogCell.getDialogId())), false);
                                }
                                if (!(childAt instanceof UserCell)) {
                                    ((UserCell) childAt).update(i);
                                } else if (childAt instanceof ProfileSearchCell) {
                                    ProfileSearchCell profileSearchCell = (ProfileSearchCell) childAt;
                                    profileSearchCell.update(i);
                                    ArrayList<Long> arrayList2 = this.selectedDialogs;
                                    if (arrayList2 != null) {
                                        profileSearchCell.setChecked(arrayList2.contains(Long.valueOf(profileSearchCell.getDialogId())), false);
                                    }
                                }
                                if (!this.dialogsListFrozen && (childAt instanceof RecyclerListView)) {
                                    RecyclerListView recyclerListView2 = (RecyclerListView) childAt;
                                    childCount = recyclerListView2.getChildCount();
                                    for (i3 = 0; i3 < childCount; i3++) {
                                        View childAt2 = recyclerListView2.getChildAt(i3);
                                        if (childAt2 instanceof HintDialogCell) {
                                            ((HintDialogCell) childAt2).update(i);
                                        }
                                    }
                                }
                            }
                        }
                        if (!(childAt instanceof UserCell)) {
                        }
                        if (!this.dialogsListFrozen) {
                            RecyclerListView recyclerListView22 = (RecyclerListView) childAt;
                            childCount = recyclerListView22.getChildCount();
                            while (i3 < childCount) {
                            }
                        }
                    }
                }
            }
            i4++;
        }
    }

    public void setDelegate(DialogsActivityDelegate dialogsActivityDelegate) {
        this.delegate = dialogsActivityDelegate;
    }

    public void setSearchString(String str) {
        this.searchString = str;
    }

    public void setInitialSearchString(String str) {
        this.initialSearchString = str;
    }

    public boolean isMainDialogList() {
        return this.delegate == null && this.searchString == null;
    }

    public boolean isArchive() {
        return this.folderId == 1;
    }

    public void setInitialSearchType(int i) {
        this.initialSearchType = i;
    }

    private boolean checkCanWrite(long j) {
        if (this.addToGroupAlertString == null && this.initialDialogsType != 15 && this.checkCanWrite) {
            if (DialogObject.isChatDialog(j)) {
                long j2 = -j;
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j2));
                if (!ChatObject.isChannel(chat) || chat.megagroup) {
                    return true;
                }
                if (this.cantSendToChannels || !ChatObject.isCanWriteToChannel(j2, this.currentAccount) || this.hasPoll == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTitle(LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle));
                    if (this.hasPoll == 2) {
                        builder.setMessage(LocaleController.getString("PublicPollCantForward", R.string.PublicPollCantForward));
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelCantSendMessage", R.string.ChannelCantSendMessage));
                    }
                    builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                    showDialog(builder.create());
                    return false;
                }
                return true;
            } else if (DialogObject.isEncryptedDialog(j)) {
                if (this.hasPoll != 0 || this.hasInvoice) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                    builder2.setTitle(LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle));
                    if (this.hasPoll != 0) {
                        builder2.setMessage(LocaleController.getString("PollCantForwardSecretChat", R.string.PollCantForwardSecretChat));
                    } else {
                        builder2.setMessage(LocaleController.getString("InvoiceCantForwardSecretChat", R.string.InvoiceCantForwardSecretChat));
                    }
                    builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                    showDialog(builder2.create());
                    return false;
                }
                return true;
            } else {
                return true;
            }
        }
        return true;
    }

    public void didSelectResult(long j, int i, boolean z, boolean z2) {
        didSelectResult(j, i, z, z2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:103:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0190  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didSelectResult(final long j, final int i, boolean z, final boolean z2, final TopicsFragment topicsFragment) {
        final TLRPC$Chat tLRPC$Chat;
        final TLRPC$User tLRPC$User;
        AlertDialog.Builder builder;
        String string;
        String formatStringSimple;
        String string2;
        AlertDialog.Builder builder2;
        TLRPC$TL_forumTopic findTopic;
        AlertDialog create;
        if (!checkCanWrite(j)) {
            return;
        }
        int i2 = this.initialDialogsType;
        if (i2 == 11 || i2 == 12 || i2 == 13) {
            if (this.checkingImportDialog) {
                return;
            }
            if (DialogObject.isUserDialog(j)) {
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
                if (!user.mutual_contact) {
                    UndoView undoView = getUndoView();
                    if (undoView != null) {
                        undoView.showWithAction(j, 45, (Runnable) null);
                        return;
                    }
                    return;
                }
                tLRPC$User = user;
                tLRPC$Chat = null;
            } else {
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j));
                if (!ChatObject.hasAdminRights(chat) || !ChatObject.canChangeChatInfo(chat)) {
                    UndoView undoView2 = getUndoView();
                    if (undoView2 != null) {
                        undoView2.showWithAction(j, 46, (Runnable) null);
                        return;
                    }
                    return;
                }
                tLRPC$Chat = chat;
                tLRPC$User = null;
            }
            final AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
            final TLRPC$TL_messages_checkHistoryImportPeer tLRPC$TL_messages_checkHistoryImportPeer = new TLRPC$TL_messages_checkHistoryImportPeer();
            tLRPC$TL_messages_checkHistoryImportPeer.peer = getMessagesController().getInputPeer(j);
            getConnectionsManager().sendRequest(tLRPC$TL_messages_checkHistoryImportPeer, new RequestDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda125
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    DialogsActivity.this.lambda$didSelectResult$113(alertDialog, tLRPC$User, tLRPC$Chat, j, z2, tLRPC$TL_messages_checkHistoryImportPeer, tLObject, tLRPC$TL_error);
                }
            });
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } else if (!z || ((this.selectAlertString == null || this.selectAlertStringGroup == null) && this.addToGroupAlertString == null)) {
            if (i2 == 15) {
                final Runnable runnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda87
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.this.lambda$didSelectResult$115(j, i, z2, topicsFragment);
                    }
                };
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda88
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.this.lambda$didSelectResult$117(j, runnable);
                    }
                };
                if (j < 0) {
                    showSendToBotAlert(getMessagesController().getChat(Long.valueOf(-j)), runnable2, (Runnable) null);
                } else {
                    showSendToBotAlert(getMessagesController().getUser(Long.valueOf(j)), runnable2, (Runnable) null);
                }
            } else if (this.delegate != null) {
                ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                arrayList.add(MessagesStorage.TopicKey.of(j, i));
                if (this.delegate.didSelectDialogs(this, arrayList, null, z2, topicsFragment) && this.resetDelegate) {
                    this.delegate = null;
                }
            } else {
                finishFragment();
            }
        } else if (getParentActivity() == null) {
        } else {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(getParentActivity());
            if (DialogObject.isEncryptedDialog(j)) {
                TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))).user_id));
                if (user2 == null) {
                    return;
                }
                string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                formatStringSimple = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user2));
                string2 = LocaleController.getString("Send", R.string.Send);
            } else if (DialogObject.isUserDialog(j)) {
                if (j == getUserConfig().getClientUserId()) {
                    string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                    formatStringSimple = LocaleController.formatStringSimple(this.selectAlertStringGroup, LocaleController.getString("SavedMessages", R.string.SavedMessages));
                    string2 = LocaleController.getString("Send", R.string.Send);
                } else {
                    TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j));
                    if (user3 == null || this.selectAlertString == null) {
                        return;
                    }
                    string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                    formatStringSimple = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user3));
                    string2 = LocaleController.getString("Send", R.string.Send);
                }
            } else {
                TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(-j));
                if (chat2 == null) {
                    return;
                }
                String str = chat2.title;
                if (i != 0) {
                    builder = builder3;
                    if (getMessagesController().getTopicsController().findTopic(chat2.id, i) != null) {
                        str = ((Object) str) + " " + findTopic.title;
                    }
                } else {
                    builder = builder3;
                }
                if (this.addToGroupAlertString != null) {
                    string = LocaleController.getString("AddToTheGroupAlertTitle", R.string.AddToTheGroupAlertTitle);
                    formatStringSimple = LocaleController.formatStringSimple(this.addToGroupAlertString, str);
                    string2 = LocaleController.getString("Add", R.string.Add);
                } else {
                    string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                    formatStringSimple = LocaleController.formatStringSimple(this.selectAlertStringGroup, str);
                    string2 = LocaleController.getString("Send", R.string.Send);
                }
                builder2 = builder;
                String str2 = string2;
                builder2.setTitle(string);
                builder2.setMessage(AndroidUtilities.replaceTags(formatStringSimple));
                builder2.setPositiveButton(str2, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda17
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        DialogsActivity.this.lambda$didSelectResult$114(j, i, topicsFragment, dialogInterface, i3);
                    }
                });
                builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                create = builder2.create();
                if (showDialog(create) != null) {
                    create.show();
                    return;
                }
                return;
            }
            builder2 = builder3;
            String str22 = string2;
            builder2.setTitle(string);
            builder2.setMessage(AndroidUtilities.replaceTags(formatStringSimple));
            builder2.setPositiveButton(str22, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda17
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    DialogsActivity.this.lambda$didSelectResult$114(j, i, topicsFragment, dialogInterface, i3);
                }
            });
            builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            create = builder2.create();
            if (showDialog(create) != null) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$113(final AlertDialog alertDialog, final TLRPC$User tLRPC$User, final TLRPC$Chat tLRPC$Chat, final long j, final boolean z, final TLRPC$TL_messages_checkHistoryImportPeer tLRPC$TL_messages_checkHistoryImportPeer, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda98
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$didSelectResult$112(alertDialog, tLObject, tLRPC$User, tLRPC$Chat, j, z, tLRPC$TL_error, tLRPC$TL_messages_checkHistoryImportPeer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$112(AlertDialog alertDialog, TLObject tLObject, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, final long j, final boolean z, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_messages_checkHistoryImportPeer tLRPC$TL_messages_checkHistoryImportPeer) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.checkingImportDialog = false;
        if (tLObject != null) {
            AlertsCreator.createImportDialogAlert(this, this.arguments.getString("importTitle"), ((TLRPC$TL_messages_checkedHistoryImportPeer) tLObject).confirm_text, tLRPC$User, tLRPC$Chat, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda91
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$didSelectResult$111(j, z);
                }
            });
            return;
        }
        AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_messages_checkHistoryImportPeer, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(j), tLRPC$TL_messages_checkHistoryImportPeer, tLRPC$TL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$111(long j, boolean z) {
        setDialogsListFrozen(true);
        ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
        arrayList.add(MessagesStorage.TopicKey.of(j, 0L));
        this.delegate.didSelectDialogs(this, arrayList, null, z, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$114(long j, int i, TopicsFragment topicsFragment, DialogInterface dialogInterface, int i2) {
        didSelectResult(j, i, false, false, topicsFragment);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$115(long j, int i, boolean z, TopicsFragment topicsFragment) {
        if (this.delegate != null) {
            ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
            arrayList.add(MessagesStorage.TopicKey.of(j, i));
            this.delegate.didSelectDialogs(this, arrayList, null, z, topicsFragment);
            if (this.resetDelegate) {
                this.delegate = null;
                return;
            }
            return;
        }
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$117(long j, final Runnable runnable) {
        if (this.requestPeerType.bot_admin_rights != null) {
            getMessagesController().setUserAdminRole(-j, getMessagesController().getUser(Long.valueOf(this.requestPeerBotId)), this.requestPeerType.bot_admin_rights, null, false, this, true, true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda107
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean lambda$didSelectResult$116;
                    lambda$didSelectResult$116 = DialogsActivity.lambda$didSelectResult$116(runnable, tLRPC$TL_error);
                    return lambda$didSelectResult$116;
                }
            });
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$didSelectResult$116(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
        runnable.run();
        return true;
    }

    private void showSendToBotAlert(TLRPC$User tLRPC$User, final Runnable runnable, final Runnable runnable2) {
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.requestPeerBotId));
        showDialog(new AlertDialog.Builder(getContext()).setTitle(LocaleController.formatString(R.string.AreYouSureSendChatToBotTitle, UserObject.getFirstName(tLRPC$User), UserObject.getFirstName(user))).setMessage(TextUtils.concat(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AreYouSureSendChatToBotMessage, UserObject.getFirstName(tLRPC$User), UserObject.getFirstName(user))))).setPositiveButton(LocaleController.formatString("Send", R.string.Send, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                runnable.run();
            }
        }).setNegativeButton(LocaleController.formatString("Cancel", R.string.Cancel, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                DialogsActivity.lambda$showSendToBotAlert$119(runnable2, dialogInterface, i);
            }
        }).create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSendToBotAlert$119(Runnable runnable, DialogInterface dialogInterface, int i) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSendToBotAlert(TLRPC$Chat tLRPC$Chat, final Runnable runnable, final Runnable runnable2) {
        CharSequence charSequence;
        String formatString;
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.requestPeerBotId));
        boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
        AlertDialog.Builder title = new AlertDialog.Builder(getContext()).setTitle(LocaleController.formatString(R.string.AreYouSureSendChatToBotTitle, tLRPC$Chat.title, UserObject.getFirstName(user)));
        CharSequence[] charSequenceArr = new CharSequence[2];
        charSequenceArr[0] = AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AreYouSureSendChatToBotMessage, tLRPC$Chat.title, UserObject.getFirstName(user)));
        Boolean bool = this.requestPeerType.bot_participant;
        if ((bool == null || !bool.booleanValue() || getMessagesController().isInChatCached(tLRPC$Chat, user)) && this.requestPeerType.bot_admin_rights == null) {
            charSequence = "";
        } else {
            CharSequence[] charSequenceArr2 = new CharSequence[2];
            charSequenceArr2[0] = "\n\n";
            if (this.requestPeerType.bot_admin_rights == null) {
                formatString = LocaleController.formatString(R.string.AreYouSureSendChatToBotAdd, UserObject.getFirstName(user), tLRPC$Chat.title);
            } else {
                formatString = LocaleController.formatString(R.string.AreYouSureSendChatToBotAddRights, UserObject.getFirstName(user), tLRPC$Chat.title, RequestPeerRequirementsCell.rightsToString(this.requestPeerType.bot_admin_rights, isChannelAndNotMegaGroup));
            }
            charSequenceArr2[1] = AndroidUtilities.replaceTags(formatString);
            charSequence = TextUtils.concat(charSequenceArr2);
        }
        charSequenceArr[1] = charSequence;
        showDialog(title.setMessage(TextUtils.concat(charSequenceArr)).setPositiveButton(LocaleController.formatString("Send", R.string.Send, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda10
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                runnable.run();
            }
        }).setNegativeButton(LocaleController.formatString("Cancel", R.string.Cancel, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                DialogsActivity.lambda$showSendToBotAlert$121(runnable2, dialogInterface, i);
            }
        }).create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSendToBotAlert$121(Runnable runnable, DialogInterface dialogInterface, int i) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public RLottieImageView getFloatingButton() {
        return this.floatingButton;
    }

    private boolean onSendLongClick(View view) {
        Activity parentActivity = getParentActivity();
        Theme.ResourcesProvider resourceProvider = getResourceProvider();
        if (parentActivity == null) {
            return false;
        }
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(parentActivity, resourceProvider);
        actionBarPopupWindowLayout.setAnimationEnabled(false);
        actionBarPopupWindowLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.DialogsActivity.51
            private Rect popupRect = new Rect();

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0 && DialogsActivity.this.sendPopupWindow != null && DialogsActivity.this.sendPopupWindow.isShowing()) {
                    view2.getHitRect(this.popupRect);
                    if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        return false;
                    }
                    DialogsActivity.this.sendPopupWindow.dismiss();
                    return false;
                }
                return false;
            }
        });
        actionBarPopupWindowLayout.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda126
            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
            public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                DialogsActivity.this.lambda$onSendLongClick$122(keyEvent);
            }
        });
        actionBarPopupWindowLayout.setShownFromBottom(false);
        actionBarPopupWindowLayout.setupRadialSelectors(getThemedColor(Theme.key_dialogButtonSelector));
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem((Context) parentActivity, true, true, resourceProvider);
        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("SendWithoutSound", R.string.SendWithoutSound), R.drawable.input_notify_off);
        actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda33
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                DialogsActivity.this.lambda$onSendLongClick$123(view2);
            }
        });
        linearLayout.addView(actionBarPopupWindowLayout, LayoutHelper.createLinear(-1, -2));
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(linearLayout, -2, -2);
        this.sendPopupWindow = actionBarPopupWindow;
        actionBarPopupWindow.setAnimationEnabled(false);
        this.sendPopupWindow.setAnimationStyle(R.style.PopupContextAnimation2);
        this.sendPopupWindow.setOutsideTouchable(true);
        this.sendPopupWindow.setClippingEnabled(true);
        this.sendPopupWindow.setInputMethodMode(2);
        this.sendPopupWindow.setSoftInputMode(0);
        this.sendPopupWindow.getContentView().setFocusableInTouchMode(true);
        SharedConfig.removeScheduledOrNoSoundHint();
        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.sendPopupWindow.setFocusable(true);
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        this.sendPopupWindow.showAtLocation(view, 51, ((iArr[0] + view.getMeasuredWidth()) - linearLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), (iArr[1] - linearLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f));
        this.sendPopupWindow.dimBehind();
        view.performHapticFeedback(3, 2);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$122(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && (actionBarPopupWindow = this.sendPopupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$123(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        this.notify = false;
        if (this.delegate == null || this.selectedDialogs.isEmpty()) {
            return;
        }
        ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
        for (int i = 0; i < this.selectedDialogs.size(); i++) {
            arrayList.add(MessagesStorage.TopicKey.of(this.selectedDialogs.get(i).longValue(), 0L));
        }
        this.delegate.didSelectDialogs(this, arrayList, this.commentView.getFieldText(), false, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x0893 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x03fe  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        char c;
        RecyclerListView recyclerListView;
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda128
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                DialogsActivity.this.lambda$getThemeDescriptions$124();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        View view = this.fragmentView;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(view, i, null, null, null, null, i2));
        if (this.movingView != null) {
            arrayList.add(new ThemeDescription(this.movingView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i2));
        }
        if (this.doneItem != null) {
            arrayList.add(new ThemeDescription(this.doneItem, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_actionBarDefaultSelector));
        }
        if (this.folderId == 0) {
            if (this.onlySelect) {
                arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
            }
            View view2 = this.fragmentView;
            Paint paint = this.actionBarDefaultPaint;
            int i3 = Theme.key_actionBarDefault;
            arrayList.add(new ThemeDescription(view2, 0, null, paint, null, null, i3));
            if (this.searchViewPager != null) {
                arrayList.add(new ThemeDescription(this.searchViewPager.searchListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i3));
            }
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultIcon));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, new Drawable[]{Theme.dialogs_holidayDrawable}, null, Theme.key_actionBarDefaultTitle));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        } else {
            View view3 = this.fragmentView;
            Paint paint2 = this.actionBarDefaultPaint;
            int i4 = Theme.key_actionBarDefaultArchived;
            arrayList.add(new ThemeDescription(view3, 0, null, paint2, null, null, i4));
            if (this.searchViewPager != null) {
                arrayList.add(new ThemeDescription(this.searchViewPager.searchListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i4));
            }
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultArchivedIcon));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, new Drawable[]{Theme.dialogs_holidayDrawable}, null, Theme.key_actionBarDefaultArchivedTitle));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultArchivedSelector));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultArchivedSearch));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultArchivedSearchPlaceholder));
        }
        ActionBar actionBar = this.actionBar;
        int i5 = ThemeDescription.FLAG_AB_AM_ITEMSCOLOR;
        int i6 = Theme.key_actionBarActionModeDefaultIcon;
        arrayList.add(new ThemeDescription(actionBar, i5, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, null, null, null, null, Theme.key_actionBarActionModeDefaultTop));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarActionModeDefaultSelector));
        arrayList.add(new ThemeDescription(this.selectedDialogsCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_dialogButtonSelector));
        if (this.filterTabsView != null) {
            if (this.actionBar.isActionModeShowed()) {
                arrayList.add(new ThemeDescription(this.filterTabsView, 0, new Class[]{FilterTabsView.class}, new String[]{"selectorDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_profile_tabSelectedLine));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, Theme.key_profile_tabSelectedText));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, Theme.key_profile_tabText));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{FilterTabsView.TabView.class}, null, null, null, Theme.key_profile_tabSelector));
            } else {
                arrayList.add(new ThemeDescription(this.filterTabsView, 0, new Class[]{FilterTabsView.class}, new String[]{"selectorDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_actionBarTabLine));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, Theme.key_actionBarTabActiveText));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, Theme.key_actionBarTabUnactiveText));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_actionBarTabSelector));
            }
            arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), 0, new Class[]{FilterTabsView.TabView.class}, null, null, null, Theme.key_chats_tabUnreadActiveBackground));
            arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), 0, new Class[]{FilterTabsView.TabView.class}, null, null, null, Theme.key_chats_tabUnreadUnactiveBackground));
        }
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionIcon));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionBackground));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionPressedBackground));
        arrayList.addAll(SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda127
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                DialogsActivity.this.lambda$getThemeDescriptions$125();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        }, Theme.key_actionBarActionModeDefault, i6));
        int i7 = 0;
        while (true) {
            if (i7 >= 3) {
                break;
            } else if (i7 == 2) {
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager == null) {
                    i7++;
                } else {
                    recyclerListView = searchViewPager.searchListView;
                    if (recyclerListView == null) {
                        RecyclerListView recyclerListView2 = recyclerListView;
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, Theme.avatarDrawables, null, Theme.key_avatar_text));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_countPaint, null, null, Theme.key_chats_unreadCounter));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_countGrayPaint, null, null, Theme.key_chats_unreadCounterMuted));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_countTextPaint, null, null, Theme.key_chats_unreadCounterText));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_lockDrawable}, null, Theme.key_chats_secretIcon));
                        Drawable[] drawableArr = {Theme.dialogs_scamDrawable, Theme.dialogs_fakeDrawable};
                        int i8 = Theme.key_chats_draft;
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, drawableArr, null, i8));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_pinnedDrawable, Theme.dialogs_reorderDrawable}, null, Theme.key_chats_pinnedIcon));
                        TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr[0], textPaintArr[1], Theme.dialogs_searchNamePaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_name));
                        TextPaint[] textPaintArr2 = Theme.dialogs_nameEncryptedPaint;
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr2[0], textPaintArr2[1], Theme.dialogs_searchNameEncryptedPaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_secretName));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_messagePaint[1], null, null, Theme.key_chats_message_threeLines));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_messagePaint[0], null, null, Theme.key_chats_message));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_messageNamePaint, null, null, Theme.key_chats_nameMessage_threeLines));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, i8));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, (String[]) null, Theme.dialogs_messagePrintingPaint, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_actionMessage));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_timePaint, null, null, Theme.key_chats_date));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_pinnedPaint, null, null, Theme.key_chats_pinnedOverlay));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_tabletSeletedPaint, null, null, Theme.key_chats_tabletSelectedOverlay));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_checkDrawable}, null, Theme.key_chats_sentCheck));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_checkReadDrawable, Theme.dialogs_halfCheckDrawable}, null, Theme.key_chats_sentReadCheck));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_clockDrawable}, null, Theme.key_chats_sentClock));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_errorPaint, null, null, Theme.key_chats_sentError));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_errorDrawable}, null, Theme.key_chats_sentErrorIcon));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedCheckDrawable}, null, Theme.key_chats_verifiedCheck));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedDrawable}, null, Theme.key_chats_verifiedBackground));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_muteDrawable}, null, Theme.key_chats_muteIcon));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_mentionDrawable}, null, Theme.key_chats_mentionIcon));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, Theme.key_chats_archivePinBackground));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, Theme.key_chats_archiveBackground));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, Theme.key_chats_onlineCircle));
                        int i9 = Theme.key_windowBackgroundWhite;
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, i9));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_CHECKBOX, new Class[]{DialogCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i9));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{DialogCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkboxCheck));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_progressCircle));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_offlinePaint, null, null, Theme.key_windowBackgroundWhiteGrayText3));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_onlinePaint, null, null, Theme.key_windowBackgroundWhiteBlueText3));
                        GraySectionCell.createThemeDescriptions(arrayList, recyclerListView);
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{HashtagSearchCell.class}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                        int i10 = Theme.key_windowBackgroundGrayShadow;
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i10));
                        int i11 = Theme.key_windowBackgroundGray;
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, i11));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i10));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i11));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText2));
                    }
                    i7++;
                }
            } else {
                ViewPage[] viewPageArr = this.viewPages;
                if (viewPageArr == null) {
                    i7++;
                } else {
                    recyclerListView = i7 < viewPageArr.length ? viewPageArr[i7].listView : null;
                    if (recyclerListView == null) {
                    }
                    i7++;
                }
            }
        }
        int i12 = Theme.key_avatar_backgroundRed;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i12));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundSaved));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i12));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Red));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Orange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Violet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Green));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Cyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Blue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Pink));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_background2Saved));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundArchived));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundArchivedHidden));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_nameMessage));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_draft));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_attachMessage));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_nameArchived));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_nameMessageArchived));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_nameMessageArchived_threeLines));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_messageArchived));
        if (this.viewPages != null) {
            int i13 = 0;
            while (i13 < this.viewPages.length) {
                if (this.folderId == 0) {
                    arrayList.add(new ThemeDescription(this.viewPages[i13].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
                } else {
                    arrayList.add(new ThemeDescription(this.viewPages[i13].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefaultArchived));
                }
                int i14 = Theme.key_chats_nameMessage_threeLines;
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DialogsEmptyCell.class}, new String[]{"emptyTextView1"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i14));
                int i15 = Theme.key_chats_message;
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DialogsEmptyCell.class}, new String[]{"emptyTextView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i15));
                if (SharedConfig.archiveHidden) {
                    RLottieDrawable[] rLottieDrawableArr = {Theme.dialogs_archiveAvatarDrawable};
                    int i16 = Theme.key_avatar_backgroundArchivedHidden;
                    arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, rLottieDrawableArr, "Arrow1", i16));
                    arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_archiveAvatarDrawable}, "Arrow2", i16));
                } else {
                    RLottieDrawable[] rLottieDrawableArr2 = {Theme.dialogs_archiveAvatarDrawable};
                    int i17 = Theme.key_avatar_backgroundArchived;
                    arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, rLottieDrawableArr2, "Arrow1", i17));
                    arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_archiveAvatarDrawable}, "Arrow2", i17));
                }
                RLottieDrawable[] rLottieDrawableArr3 = {Theme.dialogs_archiveAvatarDrawable};
                int i18 = Theme.key_avatar_text;
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, rLottieDrawableArr3, "Box2", i18));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_archiveAvatarDrawable}, "Box1", i18));
                RLottieDrawable[] rLottieDrawableArr4 = {Theme.dialogs_pinArchiveDrawable};
                int i19 = Theme.key_chats_archiveIcon;
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, rLottieDrawableArr4, "Arrow", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_pinArchiveDrawable}, "Line", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_unpinArchiveDrawable}, "Arrow", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_unpinArchiveDrawable}, "Line", i19));
                RLottieDrawable[] rLottieDrawableArr5 = {Theme.dialogs_archiveDrawable};
                int i20 = Theme.key_chats_archiveBackground;
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, rLottieDrawableArr5, "Arrow", i20));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_archiveDrawable}, "Box2", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_archiveDrawable}, "Box1", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_hidePsaDrawable}, "Line 1", i20));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_hidePsaDrawable}, "Line 2", i20));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_hidePsaDrawable}, "Line 3", i20));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_hidePsaDrawable}, "Cup Red", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_hidePsaDrawable}, "Box", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_unarchiveDrawable}, "Arrow1", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_unarchiveDrawable}, "Arrow2", Theme.key_chats_archivePinBackground));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_unarchiveDrawable}, "Box2", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{DialogCell.class}, new RLottieDrawable[]{Theme.dialogs_unarchiveDrawable}, "Box1", i19));
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
                int i21 = i13;
                arrayList.add(new ThemeDescription(this.viewPages[i13].listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText));
                arrayList.add(new ThemeDescription(this.viewPages[i21].listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText));
                int i22 = Theme.key_windowBackgroundWhiteBlueText4;
                arrayList.add(new ThemeDescription(this.viewPages[i21].listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
                arrayList.add(new ThemeDescription(this.viewPages[i21].listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
                arrayList.add(new ThemeDescription(this.viewPages[i21].progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
                ViewPager archiveHintCellPager = this.viewPages[i21].dialogsAdapter.getArchiveHintCellPager();
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i14));
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"imageView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_unreadCounter));
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"headerTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i14));
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"messageTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i15));
                arrayList.add(new ThemeDescription(archiveHintCellPager, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefaultArchived));
                i13 = i21 + 1;
            }
        }
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_archivePullDownBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chats_archivePullDownBackgroundActive));
        RecyclerView recyclerView = this.sideMenu;
        int i23 = ThemeDescription.FLAG_BACKGROUND;
        int i24 = Theme.key_chats_menuBackground;
        arrayList.add(new ThemeDescription(recyclerView, i23, null, null, null, null, i24));
        int i25 = Theme.key_chats_menuName;
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, i25));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuPhone));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuPhoneCats));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chat_serviceBackground));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuTopShadow));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuTopShadowCats));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerProfileCell.class}, new String[]{"darkThemeView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i25));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{DrawerProfileCell.class}, null, null, themeDescriptionDelegate, Theme.key_chats_menuTopBackgroundCats));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{DrawerProfileCell.class}, null, null, themeDescriptionDelegate, Theme.key_chats_menuTopBackground));
        int i26 = Theme.key_chats_menuItemIcon;
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i26));
        int i27 = Theme.key_chats_menuItemText;
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i27));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerUserCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i27));
        int i28 = Theme.key_chats_unreadCounterText;
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i28));
        int i29 = Theme.key_chats_unreadCounter;
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i29));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i24));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i26));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i27));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DividerCell.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        if (this.searchViewPager != null) {
            DialogsSearchAdapter dialogsSearchAdapter = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter != null ? dialogsSearchAdapter.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countPaint, null, null, i29));
            DialogsSearchAdapter dialogsSearchAdapter2 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter2 != null ? dialogsSearchAdapter2.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countGrayPaint, null, null, Theme.key_chats_unreadCounterMuted));
            DialogsSearchAdapter dialogsSearchAdapter3 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter3 != null ? dialogsSearchAdapter3.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countTextPaint, null, null, i28));
            DialogsSearchAdapter dialogsSearchAdapter4 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter4 != null ? dialogsSearchAdapter4.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_archiveTextPaint, null, null, Theme.key_chats_archiveText));
            DialogsSearchAdapter dialogsSearchAdapter5 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter5 != null ? dialogsSearchAdapter5.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
            DialogsSearchAdapter dialogsSearchAdapter6 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter6 != null ? dialogsSearchAdapter6.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, null, null, null, Theme.key_chats_onlineCircle));
        }
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerBackground));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerPlayPause));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerTitle));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerPerformer));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_inappPlayerClose));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_returnToCallBackground));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_returnToCallText));
        for (int i30 = 0; i30 < this.undoView.length; i30++) {
            UndoView undoView = this.undoView[i30];
            int i31 = ThemeDescription.FLAG_BACKGROUNDFILTER;
            int i32 = Theme.key_undo_background;
            arrayList.add(new ThemeDescription(undoView, i31, null, null, null, null, i32));
            int i33 = Theme.key_undo_cancelColor;
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i33));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i33));
            int i34 = Theme.key_undo_infoColor;
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"subinfoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "info1", i32));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "info2", i32));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc12", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc11", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc10", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc9", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc8", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc7", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc6", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc5", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc4", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc3", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc2", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc1", i34));
            arrayList.add(new ThemeDescription(this.undoView[i30], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Oval", i34));
        }
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogBackgroundGray));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlack));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextLink));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogLinkSelection));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlue2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlue4));
        int i35 = Theme.key_text_RedBold;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, i35));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray3));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray4));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogIcon));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_text_RedRegular));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextHint));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogInputField));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogInputFieldActivated));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareCheck));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareUnchecked));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareDisabled));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRadioBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRadioBackgroundChecked));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogButton));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogButtonSelector));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogScrollGlow));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRoundCheckBox));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRoundCheckBoxCheck));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogLineProgress));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogLineProgressBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogGrayLine));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialog_inlineProgressBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialog_inlineProgress));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogSearchBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogSearchHint));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogSearchIcon));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogSearchText));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogFloatingButton));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogFloatingIcon));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogShadowLine));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_sheet_scrollUp));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_sheet_other));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarSelector));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarTitle));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarSubtitle));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarItems));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_background));
        int i36 = Theme.key_player_time;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, i36));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_progressBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_progressCachedBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_progress));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_button));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_buttonActive));
        if (this.commentView != null) {
            arrayList.add(new ThemeDescription(this.commentView, 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground));
            arrayList.add(new ThemeDescription(this.commentView, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow));
            arrayList.add(new ThemeDescription(this.commentView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chat_messagePanelText));
            arrayList.add(new ThemeDescription(this.commentView, ThemeDescription.FLAG_CURSORCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chat_messagePanelCursor));
            arrayList.add(new ThemeDescription(this.commentView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chat_messagePanelHint));
        }
        int i37 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i37));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i36));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chat_messagePanelCursor));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_actionBarIconBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_groupcreate_spanBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayGreen1));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayGreen2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayBlue1));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayBlue2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_topPanelGreen1));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_topPanelGreen2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_topPanelBlue1));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_topPanelBlue2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_topPanelGray));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayAlertGradientMuted));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayAlertGradientMuted2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayAlertGradientUnmuted));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayAlertGradientUnmuted2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_mutedByAdminGradient));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_mutedByAdminGradient2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_mutedByAdminGradient3));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayAlertMutedByAdmin));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_voipgroup_overlayAlertMutedByAdmin2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_stories_circle_dialog1));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_stories_circle_dialog2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_stories_circle_closeFriends1));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_stories_circle_closeFriends2));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_stories_circle1));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_stories_circle2));
        FiltersView filtersView = this.filtersView;
        if (filtersView != null) {
            arrayList.addAll(filtersView.getThemeDescriptions());
            this.filtersView.updateColors();
        }
        SearchViewPager searchViewPager2 = this.searchViewPager;
        if (searchViewPager2 != null) {
            searchViewPager2.getThemeDescriptions(arrayList);
        }
        if (this.speedItem != null) {
            arrayList.addAll(SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda129
                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public final void didSetColor() {
                    DialogsActivity.this.lambda$getThemeDescriptions$126();
                }

                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public /* synthetic */ void onAnimationProgress(float f) {
                    ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
                }
            }, Theme.key_actionBarActionModeDefaultIcon, Theme.key_actionBarActionModeDefaultSelector));
        }
        final DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null) {
            Objects.requireNonNull(dialogsHintCell);
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda62
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsHintCell.this.updateColors();
                }
            };
            c = 3;
            SimpleThemeDescription.add(arrayList, runnable, Theme.key_windowBackgroundWhite, i37, Theme.key_windowBackgroundWhiteGrayText);
        } else {
            c = 3;
        }
        final UnconfirmedAuthHintCell unconfirmedAuthHintCell = this.authHintCell;
        if (unconfirmedAuthHintCell != null) {
            Objects.requireNonNull(unconfirmedAuthHintCell);
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda63
                @Override // java.lang.Runnable
                public final void run() {
                    UnconfirmedAuthHintCell.this.updateColors();
                }
            };
            int[] iArr = new int[5];
            iArr[0] = Theme.key_windowBackgroundWhite;
            iArr[1] = i37;
            iArr[2] = Theme.key_windowBackgroundWhiteGrayText;
            iArr[c] = Theme.key_windowBackgroundWhiteValueText;
            iArr[4] = i35;
            SimpleThemeDescription.add(arrayList, runnable2, iArr);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:105:0x004b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0020  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$getThemeDescriptions$124() {
        DialogsSearchAdapter dialogsSearchAdapter;
        RecyclerListView innerListView;
        ViewGroup viewGroup;
        int i = 0;
        while (i < 3) {
            if (i == 2) {
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager == null) {
                    i++;
                } else {
                    viewGroup = searchViewPager.searchListView;
                    if (viewGroup == null) {
                        int childCount = viewGroup.getChildCount();
                        for (int i2 = 0; i2 < childCount; i2++) {
                            View childAt = viewGroup.getChildAt(i2);
                            if (childAt instanceof ProfileSearchCell) {
                                ((ProfileSearchCell) childAt).update(0);
                            } else if (childAt instanceof DialogCell) {
                                ((DialogCell) childAt).update(0);
                            } else if (childAt instanceof UserCell) {
                                ((UserCell) childAt).update(0);
                            }
                        }
                    }
                    i++;
                }
            } else {
                ViewPage[] viewPageArr = this.viewPages;
                if (viewPageArr == null) {
                    i++;
                } else {
                    viewGroup = i < viewPageArr.length ? viewPageArr[i].listView : null;
                    if (viewGroup == null) {
                    }
                    i++;
                }
            }
        }
        SearchViewPager searchViewPager2 = this.searchViewPager;
        if (searchViewPager2 != null && (dialogsSearchAdapter = searchViewPager2.dialogsSearchAdapter) != null && (innerListView = dialogsSearchAdapter.getInnerListView()) != null) {
            int childCount2 = innerListView.getChildCount();
            for (int i3 = 0; i3 < childCount2; i3++) {
                View childAt2 = innerListView.getChildAt(i3);
                if (childAt2 instanceof HintDialogCell) {
                    ((HintDialogCell) childAt2).update();
                }
            }
        }
        RecyclerView recyclerView = this.sideMenu;
        if (recyclerView != null) {
            View childAt3 = recyclerView.getChildAt(0);
            if (childAt3 instanceof DrawerProfileCell) {
                DrawerProfileCell drawerProfileCell = (DrawerProfileCell) childAt3;
                drawerProfileCell.applyBackground(true);
                drawerProfileCell.updateColors();
            }
        }
        if (this.viewPages != null) {
            int i4 = 0;
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i4 >= viewPageArr2.length) {
                    break;
                }
                if (viewPageArr2[i4].pullForegroundDrawable != null) {
                    this.viewPages[i4].pullForegroundDrawable.updateColors();
                }
                i4++;
            }
        }
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.setPopupBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground), true);
            this.actionBar.setPopupItemsColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem), false, true);
            this.actionBar.setPopupItemsColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItemIcon), true, true);
            this.actionBar.setPopupItemsSelectorColor(Theme.getColor(Theme.key_dialogButtonSelector), true);
        }
        if (this.statusDrawable != null) {
            updateStatus(UserConfig.getInstance(this.currentAccount).getCurrentUser(), false);
        }
        DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null) {
            dialogsHintCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourceProvider));
        }
        ItemOptions itemOptions = this.filterOptions;
        if (itemOptions != null) {
            itemOptions.updateColors();
        }
        ActionBarMenuItem actionBarMenuItem = this.doneItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setIconColor(Theme.getColor(Theme.key_actionBarDefaultIcon));
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.updateColors();
        }
        FiltersView filtersView = this.filtersView;
        if (filtersView != null) {
            filtersView.updateColors();
        }
        SearchViewPager searchViewPager3 = this.searchViewPager;
        if (searchViewPager3 != null) {
            searchViewPager3.updateColors();
        }
        ViewPagerFixed.TabsView tabsView = this.searchTabsView;
        if (tabsView != null) {
            tabsView.updateColors();
        }
        View view = this.blurredView;
        if (view != null && Build.VERSION.SDK_INT >= 23) {
            view.setForeground(new ColorDrawable(ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundWhite), 100)));
        }
        ActionBarMenuItem actionBarMenuItem2 = this.searchItem;
        if (actionBarMenuItem2 != null) {
            EditTextBoldCursor searchField = actionBarMenuItem2.getSearchField();
            if (this.whiteActionBar) {
                searchField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                searchField.setHintTextColor(Theme.getColor(Theme.key_player_time));
                searchField.setCursorColor(Theme.getColor(Theme.key_chat_messagePanelCursor));
            } else {
                int i5 = Theme.key_actionBarDefaultSearch;
                searchField.setCursorColor(Theme.getColor(i5));
                searchField.setHintTextColor(Theme.getColor(Theme.key_actionBarDefaultSearchPlaceholder));
                searchField.setTextColor(Theme.getColor(i5));
            }
            this.searchItem.updateColor();
        }
        updateFloatingButtonColor();
        setSearchAnimationProgress(this.searchAnimationProgress, false);
        DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
        if (dialogStoriesCell != null) {
            dialogStoriesCell.updateColors();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$125() {
        SearchViewPager searchViewPager = this.searchViewPager;
        if (searchViewPager != null) {
            ActionBarMenu actionMode = searchViewPager.getActionMode();
            if (actionMode != null) {
                actionMode.setBackgroundColor(getThemedColor(Theme.key_actionBarActionModeDefault));
            }
            ActionBarMenuItem speedItem = this.searchViewPager.getSpeedItem();
            if (speedItem != null) {
                speedItem.getIconView().setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_actionBarActionModeDefaultIcon), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$126() {
        this.speedItem.getIconView().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon), PorterDuff.Mode.SRC_IN));
        this.speedItem.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_actionBarActionModeDefaultSelector)));
    }

    private void updateFloatingButtonColor() {
        if (getParentActivity() == null) {
            return;
        }
        if (this.floatingButtonContainer != null) {
            Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
            if (Build.VERSION.SDK_INT < 21) {
                Drawable mutate = ContextCompat.getDrawable(getParentActivity(), R.drawable.floating_shadow).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                createSimpleSelectorCircleDrawable = combinedDrawable;
            }
            this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
        }
        if (this.floatingButton2Container != null) {
            int dp = AndroidUtilities.dp(36.0f);
            int i = Theme.key_windowBackgroundWhite;
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(dp, ColorUtils.blendARGB(Theme.getColor(i), -1, 0.1f), Theme.blendOver(Theme.getColor(i), Theme.getColor(Theme.key_listSelector)));
            if (Build.VERSION.SDK_INT < 21) {
                Drawable mutate2 = ContextCompat.getDrawable(getParentActivity(), R.drawable.floating_shadow).mutate();
                mutate2.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable2 = new CombinedDrawable(mutate2, createSimpleSelectorCircleDrawable2, 0, 0);
                combinedDrawable2.setIconSize(AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
                createSimpleSelectorCircleDrawable2 = combinedDrawable2;
            }
            this.floatingButton2Container.setBackground(createSimpleSelectorCircleDrawable2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Animator getCustomSlideTransition(boolean z, boolean z2, float f) {
        if (z2) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.slideFragmentProgress, 1.0f);
            this.slideBackTransitionAnimator = ofFloat;
            return ofFloat;
        }
        int i = ImageReceiver.DEFAULT_CROSSFADE_DURATION;
        if (getLayoutContainer() != null) {
            i = (int) (Math.max((int) ((200.0f / getLayoutContainer().getMeasuredWidth()) * f), 80) * 1.2f);
        }
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.slideFragmentProgress, 1.0f);
        this.slideBackTransitionAnimator = ofFloat2;
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogsActivity.this.lambda$getCustomSlideTransition$127(valueAnimator);
            }
        });
        this.slideBackTransitionAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        this.slideBackTransitionAnimator.setDuration(i);
        this.slideBackTransitionAnimator.start();
        return this.slideBackTransitionAnimator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCustomSlideTransition$127(ValueAnimator valueAnimator) {
        setSlideTransitionProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void prepareFragmentToSlide(boolean z, boolean z2) {
        if (!z && z2) {
            this.isSlideBackTransition = true;
            setFragmentIsSliding(true);
            return;
        }
        this.slideBackTransitionAnimator = null;
        this.isSlideBackTransition = false;
        setFragmentIsSliding(false);
        setSlideTransitionProgress(1.0f);
    }

    private void setFragmentIsSliding(boolean z) {
        if (SharedConfig.getDevicePerformanceClass() <= 1 || !LiteMode.isEnabled(LiteMode.FLAG_CHAT_SCALE)) {
            return;
        }
        if (z) {
            ViewPage[] viewPageArr = this.viewPages;
            if (viewPageArr != null && viewPageArr[0] != null) {
                viewPageArr[0].setLayerType(2, null);
                this.viewPages[0].setClipChildren(false);
                this.viewPages[0].setClipToPadding(false);
                this.viewPages[0].listView.setClipChildren(false);
            }
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.setLayerType(2, null);
            }
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                filterTabsView.getListView().setLayerType(2, null);
            }
            View view = this.fragmentView;
            if (view != null) {
                ((ViewGroup) view).setClipChildren(false);
                this.fragmentView.requestLayout();
                return;
            }
            return;
        }
        if (this.viewPages != null) {
            int i = 0;
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i >= viewPageArr2.length) {
                    break;
                }
                ViewPage viewPage = viewPageArr2[i];
                if (viewPage != null) {
                    viewPage.setLayerType(0, null);
                    viewPage.setClipChildren(true);
                    viewPage.setClipToPadding(true);
                    viewPage.listView.setClipChildren(true);
                }
                i++;
            }
        }
        ActionBar actionBar2 = this.actionBar;
        if (actionBar2 != null) {
            actionBar2.setLayerType(0, null);
        }
        FilterTabsView filterTabsView2 = this.filterTabsView;
        if (filterTabsView2 != null) {
            filterTabsView2.getListView().setLayerType(0, null);
        }
        DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
        if (dialogStoriesCell != null) {
            dialogStoriesCell.setLayerType(0, null);
        }
        View view2 = this.fragmentView;
        if (view2 != null) {
            ((ViewGroup) view2).setClipChildren(true);
            this.fragmentView.requestLayout();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onSlideProgress(boolean z, float f) {
        if (SharedConfig.getDevicePerformanceClass() > 0 && this.isSlideBackTransition && this.slideBackTransitionAnimator == null) {
            setSlideTransitionProgress(f);
        }
    }

    private void setSlideTransitionProgress(float f) {
        if (SharedConfig.getDevicePerformanceClass() <= 0 || this.slideFragmentProgress == f) {
            return;
        }
        this.slideFragmentLite = SharedConfig.getDevicePerformanceClass() <= 1 || !LiteMode.isEnabled(LiteMode.FLAG_CHAT_SCALE);
        this.slideFragmentProgress = f;
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
        if (this.slideFragmentLite) {
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                filterTabsView.getListView().setTranslationX((this.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - this.slideFragmentProgress));
                this.filterTabsView.invalidate();
            }
            DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
            if (dialogStoriesCell != null) {
                dialogStoriesCell.setTranslationX((this.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - this.slideFragmentProgress));
            }
            RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer == null || rightSlidingDialogContainer.getFragmentView() == null || this.rightFragmentTransitionInProgress) {
                return;
            }
            this.rightSlidingDialogContainer.getFragmentView().setTranslationX((this.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - this.slideFragmentProgress));
            return;
        }
        float f2 = 1.0f - ((1.0f - this.slideFragmentProgress) * 0.05f);
        FilterTabsView filterTabsView2 = this.filterTabsView;
        if (filterTabsView2 != null) {
            filterTabsView2.getListView().setScaleX(f2);
            this.filterTabsView.getListView().setScaleY(f2);
            this.filterTabsView.getListView().setTranslationX((this.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - this.slideFragmentProgress));
            this.filterTabsView.getListView().setPivotX(this.isDrawerTransition ? this.filterTabsView.getMeasuredWidth() : 0.0f);
            this.filterTabsView.getListView().setPivotY(0.0f);
            this.filterTabsView.invalidate();
        }
        DialogStoriesCell dialogStoriesCell2 = this.dialogStoriesCell;
        if (dialogStoriesCell2 != null) {
            dialogStoriesCell2.setScaleX(f2);
            this.dialogStoriesCell.setScaleY(f2);
            this.dialogStoriesCell.setTranslationX((this.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - this.slideFragmentProgress));
            DialogStoriesCell dialogStoriesCell3 = this.dialogStoriesCell;
            dialogStoriesCell3.setPivotX(this.isDrawerTransition ? dialogStoriesCell3.getMeasuredWidth() : 0.0f);
            this.dialogStoriesCell.setPivotY(0.0f);
        }
        RightSlidingDialogContainer rightSlidingDialogContainer2 = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer2 == null || rightSlidingDialogContainer2.getFragmentView() == null) {
            return;
        }
        if (!this.rightFragmentTransitionInProgress) {
            this.rightSlidingDialogContainer.getFragmentView().setScaleX(f2);
            this.rightSlidingDialogContainer.getFragmentView().setScaleY(f2);
            this.rightSlidingDialogContainer.getFragmentView().setTranslationX((this.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - this.slideFragmentProgress));
        }
        this.rightSlidingDialogContainer.getFragmentView().setPivotX(this.isDrawerTransition ? this.rightSlidingDialogContainer.getMeasuredWidth() : 0.0f);
        this.rightSlidingDialogContainer.getFragmentView().setPivotY(0.0f);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setProgressToDrawerOpened(float f) {
        if (SharedConfig.getDevicePerformanceClass() <= 0 || this.isSlideBackTransition) {
            return;
        }
        boolean z = f > 0.0f;
        if (this.searchIsShowed) {
            f = 0.0f;
            z = false;
        }
        if (z != this.isDrawerTransition) {
            this.isDrawerTransition = z;
            if (z) {
                setFragmentIsSliding(true);
            } else {
                setFragmentIsSliding(false);
            }
            View view = this.fragmentView;
            if (view != null) {
                view.requestLayout();
            }
        }
        setSlideTransitionProgress(1.0f - f);
    }

    public void setShowSearch(String str, int i) {
        int positionForType;
        if (!this.searching) {
            this.initialSearchType = i;
            this.actionBar.openSearchField(str, false);
            return;
        }
        if (!this.searchItem.getSearchField().getText().toString().equals(str)) {
            this.searchItem.getSearchField().setText(str);
        }
        SearchViewPager searchViewPager = this.searchViewPager;
        if (searchViewPager == null || (positionForType = searchViewPager.getPositionForType(i)) < 0 || this.searchViewPager.getTabsView().getCurrentTabId() == positionForType) {
            return;
        }
        this.searchViewPager.getTabsView().scrollToTab(positionForType, positionForType);
    }

    public ActionBarMenuItem getSearchItem() {
        return this.searchItem;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        RightSlidingDialogContainer rightSlidingDialogContainer;
        if (!this.searching && (rightSlidingDialogContainer = this.rightSlidingDialogContainer) != null && rightSlidingDialogContainer.getFragment() != null) {
            return this.rightSlidingDialogContainer.getFragment().isLightStatusBar();
        }
        int color = Theme.getColor((this.searching && this.whiteActionBar) ? Theme.key_windowBackgroundWhite : this.folderId == 0 ? Theme.key_actionBarDefault : Theme.key_actionBarDefaultArchived);
        if (this.actionBar.isActionModeShowed()) {
            color = Theme.getColor(Theme.key_actionBarActionModeDefault);
        }
        return ColorUtils.calculateLuminance(color) > 0.699999988079071d;
    }

    @Override // org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider
    public List<FloatingDebugController.DebugItem> onGetDebugItems() {
        return Arrays.asList(new FloatingDebugController.DebugItem(LocaleController.getString(R.string.DebugDialogsActivity)), new FloatingDebugController.DebugItem(LocaleController.getString(R.string.ClearLocalDatabase), new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda76
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$onGetDebugItems$128();
            }
        }), new FloatingDebugController.DebugItem(LocaleController.getString(R.string.DebugClearSendMessageAsPeers), new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$onGetDebugItems$129();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onGetDebugItems$128() {
        getMessagesStorage().clearLocalDatabase();
        Toast.makeText(getContext(), LocaleController.getString(R.string.DebugClearLocalDatabaseSuccess), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onGetDebugItems$129() {
        getMessagesController().clearSendAsPeers();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean closeLastFragment() {
        if (this.rightSlidingDialogContainer.hasFragment()) {
            this.rightSlidingDialogContainer.lambda$presentFragment$1();
            SearchViewPager searchViewPager = this.searchViewPager;
            if (searchViewPager != null) {
                searchViewPager.updateTabs();
                return true;
            }
            return true;
        }
        return super.closeLastFragment();
    }

    public boolean getAllowGlobalSearch() {
        return this.allowGlobalSearch;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        FilterTabsView filterTabsView;
        if (this.rightSlidingDialogContainer.hasFragment()) {
            return false;
        }
        if (this.initialDialogsType == 3 && (filterTabsView = this.filterTabsView) != null && filterTabsView.getVisibility() == 0) {
            return this.filterTabsView.isFirstTab();
        }
        return true;
    }

    public void updateStoriesVisibility(boolean z) {
        final boolean z2;
        if (this.dialogStoriesCell == null || this.storiesVisibilityAnimator != null) {
            return;
        }
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if ((rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) || this.searchIsShowed || this.actionBar.isActionModeShowed() || this.onlySelect) {
            return;
        }
        int i = 0;
        if (StoryRecorder.isVisible() || (getLastStoryViewer() != null && getLastStoryViewer().isFullyVisible())) {
            z = false;
        }
        boolean z3 = !isArchive() && getStoriesController().hasOnlySelfStories();
        if (isArchive()) {
            z2 = !getStoriesController().getHiddenList().isEmpty();
        } else {
            z2 = !z3 && getStoriesController().hasStories();
            z3 = getStoriesController().hasOnlySelfStories();
        }
        this.hasOnlySlefStories = z3;
        boolean z4 = this.dialogStoriesCellVisible;
        boolean z5 = z3 || z2;
        this.dialogStoriesCellVisible = z5;
        if (z2 || z5) {
            this.dialogStoriesCell.updateItems(z, z5 != z4);
        }
        boolean z6 = this.dialogStoriesCellVisible;
        int i2 = 8;
        if (z6 != z4) {
            if (z) {
                ValueAnimator valueAnimator = this.storiesVisibilityAnimator2;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                if (this.dialogStoriesCellVisible && !isInPreviewMode()) {
                    this.dialogStoriesCell.setVisibility(0);
                }
                float[] fArr = new float[2];
                fArr[0] = this.progressToDialogStoriesCell;
                fArr[1] = this.dialogStoriesCellVisible ? 1.0f : 0.0f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                this.storiesVisibilityAnimator2 = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity.52
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        DialogsActivity.this.progressToDialogStoriesCell = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                        View view = DialogsActivity.this.fragmentView;
                        if (view != null) {
                            view.invalidate();
                        }
                    }
                });
                this.storiesVisibilityAnimator2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.53
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        boolean z7 = dialogsActivity.dialogStoriesCellVisible;
                        dialogsActivity.progressToDialogStoriesCell = z7 ? 1.0f : 0.0f;
                        if (!z7) {
                            dialogsActivity.dialogStoriesCell.setVisibility(8);
                        }
                        View view = DialogsActivity.this.fragmentView;
                        if (view != null) {
                            view.invalidate();
                        }
                    }
                });
                this.storiesVisibilityAnimator2.setDuration(200L);
                this.storiesVisibilityAnimator2.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.storiesVisibilityAnimator2.start();
            } else {
                this.dialogStoriesCell.setVisibility((!z6 || isInPreviewMode()) ? 8 : 0);
                this.progressToDialogStoriesCell = this.dialogStoriesCellVisible ? 1.0f : 0.0f;
                View view = this.fragmentView;
                if (view != null) {
                    view.invalidate();
                }
            }
        }
        if (z2 == this.animateToHasStories) {
            return;
        }
        this.animateToHasStories = z2;
        if (z2) {
            this.dialogStoriesCell.setProgressToCollapse(1.0f, false);
        }
        if (z && !isInPreviewMode()) {
            this.dialogStoriesCell.setVisibility(0);
            float f = -this.scrollYOffset;
            float maxScrollYOffset = z2 ? 0.0f : getMaxScrollYOffset();
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.storiesVisibilityAnimator = ofFloat2;
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(f, z2, maxScrollYOffset) { // from class: org.telegram.ui.DialogsActivity.54
                int currentValue;
                final /* synthetic */ float val$fromScrollY;
                final /* synthetic */ boolean val$newVisibility;
                final /* synthetic */ float val$toScrollY;

                {
                    this.val$fromScrollY = f;
                    this.val$newVisibility = z2;
                    this.val$toScrollY = maxScrollYOffset;
                    this.currentValue = (int) f;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    DialogsActivity.this.progressToShowStories = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                    if (!this.val$newVisibility) {
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        dialogsActivity.progressToShowStories = 1.0f - dialogsActivity.progressToShowStories;
                    }
                    int lerp = (int) AndroidUtilities.lerp(this.val$fromScrollY, this.val$toScrollY, ((Float) valueAnimator2.getAnimatedValue()).floatValue());
                    int i3 = lerp - this.currentValue;
                    this.currentValue = lerp;
                    DialogsActivity.this.viewPages[0].listView.scrollBy(0, i3);
                    View view2 = DialogsActivity.this.fragmentView;
                    if (view2 != null) {
                        view2.invalidate();
                    }
                }
            });
            this.storiesVisibilityAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.55
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    dialogsActivity.storiesVisibilityAnimator = null;
                    boolean z7 = z2;
                    dialogsActivity.hasStories = z7;
                    if (!z7 && !dialogsActivity.hasOnlySlefStories) {
                        dialogsActivity.dialogStoriesCell.setVisibility(8);
                    }
                    if (!z2) {
                        DialogsActivity.this.setScrollY(0.0f);
                        DialogsActivity.this.scrollAdditionalOffset = AndroidUtilities.dp(81.0f);
                    } else {
                        DialogsActivity.this.scrollAdditionalOffset = -AndroidUtilities.dp(81.0f);
                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                        dialogsActivity2.setScrollY(-dialogsActivity2.getMaxScrollYOffset());
                    }
                    for (int i3 = 0; i3 < DialogsActivity.this.viewPages.length; i3++) {
                        if (DialogsActivity.this.viewPages[i3] != null) {
                            DialogsActivity.this.viewPages[i3].listView.requestLayout();
                        }
                    }
                    View view2 = DialogsActivity.this.fragmentView;
                    if (view2 != null) {
                        view2.requestLayout();
                    }
                }
            });
            this.storiesVisibilityAnimator.setDuration(200L);
            this.storiesVisibilityAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.storiesVisibilityAnimator.start();
            return;
        }
        this.progressToShowStories = z2 ? 1.0f : 0.0f;
        this.hasStories = z2;
        DialogStoriesCell dialogStoriesCell = this.dialogStoriesCell;
        if ((z2 || this.hasOnlySlefStories) && !isInPreviewMode()) {
            i2 = 0;
        }
        dialogStoriesCell.setVisibility(i2);
        if (!z2) {
            setScrollY(0.0f);
        } else {
            this.scrollAdditionalOffset = -AndroidUtilities.dp(81.0f);
            setScrollY(-getMaxScrollYOffset());
        }
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                break;
            }
            if (viewPageArr[i] != null) {
                viewPageArr[i].listView.requestLayout();
            }
            i++;
        }
        View view2 = this.fragmentView;
        if (view2 != null) {
            view2.requestLayout();
            this.fragmentView.invalidate();
        }
    }

    public void createSearchViewPager() {
        int i;
        SearchViewPager searchViewPager = this.searchViewPager;
        if ((searchViewPager != null && searchViewPager.getParent() == this.fragmentView) || this.fragmentView == null || getContext() == null) {
            return;
        }
        if (this.searchString != null) {
            i = 2;
        } else {
            i = !this.onlySelect ? 1 : 0;
        }
        SearchViewPager searchViewPager2 = new SearchViewPager(getContext(), this, i, this.initialDialogsType, this.folderId, new SearchViewPager.ChatPreviewDelegate() { // from class: org.telegram.ui.DialogsActivity.56
            @Override // org.telegram.ui.Components.SearchViewPager.ChatPreviewDelegate
            public void startChatPreview(RecyclerListView recyclerListView, DialogCell dialogCell) {
                DialogsActivity.this.showChatPreview(dialogCell);
            }

            @Override // org.telegram.ui.Components.SearchViewPager.ChatPreviewDelegate
            public void move(float f) {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.movePreviewFragment(f);
                }
            }

            @Override // org.telegram.ui.Components.SearchViewPager.ChatPreviewDelegate
            public void finish() {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.finishPreviewFragment();
                }
            }
        }) { // from class: org.telegram.ui.DialogsActivity.57
            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected boolean onBackProgress(float f) {
                return false;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected void onTabPageSelected(int i2) {
                DialogsActivity.this.updateSpeedItem(i2 == 2);
            }

            @Override // org.telegram.ui.Components.SearchViewPager
            protected boolean includeDownloads() {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                return rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment();
            }
        };
        this.searchViewPager = searchViewPager2;
        ((ContentView) this.fragmentView).addView(searchViewPager2, this.searchViewPagerIndex);
        this.searchViewPager.dialogsSearchAdapter.setDelegate(new 58());
        this.searchViewPager.channelsSearchListView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda133
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i2) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i2, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i2, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i2, float f, float f2) {
                DialogsActivity.this.lambda$createSearchViewPager$130(view, i2, f, f2);
            }
        });
        this.searchViewPager.botsSearchListView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda134
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i2) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i2, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i2, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i2, float f, float f2) {
                DialogsActivity.this.lambda$createSearchViewPager$131(view, i2, f, f2);
            }
        });
        this.searchViewPager.botsSearchListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda137
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i2) {
                boolean lambda$createSearchViewPager$133;
                lambda$createSearchViewPager$133 = DialogsActivity.this.lambda$createSearchViewPager$133(view, i2);
                return lambda$createSearchViewPager$133;
            }
        });
        this.searchViewPager.searchListView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda135
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i2) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i2, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i2, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i2, float f, float f2) {
                DialogsActivity.this.lambda$createSearchViewPager$134(view, i2, f, f2);
            }
        });
        this.searchViewPager.searchListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity.59
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
            public boolean onItemClick(View view, int i2, float f, float f2) {
                if (view instanceof ProfileSearchCell) {
                    ProfileSearchCell profileSearchCell = (ProfileSearchCell) view;
                    if (profileSearchCell.isBlocked()) {
                        DialogsActivity.this.showPremiumBlockedToast(view, profileSearchCell.getDialogId());
                        return true;
                    }
                }
                DialogsActivity dialogsActivity = DialogsActivity.this;
                return dialogsActivity.onItemLongClick(dialogsActivity.searchViewPager.searchListView, view, i2, f, f2, -1, DialogsActivity.this.searchViewPager.dialogsSearchAdapter);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
            public void onMove(float f, float f2) {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.movePreviewFragment(f2);
                }
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
            public void onLongClickRelease() {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.finishPreviewFragment();
                }
            }
        });
        this.searchViewPager.setFilteredSearchViewDelegate(new FilteredSearchView.Delegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda138
            @Override // org.telegram.ui.FilteredSearchView.Delegate
            public final void updateFiltersView(boolean z, ArrayList arrayList, ArrayList arrayList2, boolean z2) {
                DialogsActivity.this.lambda$createSearchViewPager$135(z, arrayList, arrayList2, z2);
            }
        });
        this.searchViewPager.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 58 implements DialogsSearchAdapter.DialogsSearchAdapterDelegate {
        58() {
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void searchStateChanged(boolean z, boolean z2) {
            if (DialogsActivity.this.searchViewPager.emptyView.getVisibility() == 0) {
                z2 = true;
            }
            if (DialogsActivity.this.searching && DialogsActivity.this.searchWas && DialogsActivity.this.searchViewPager.emptyView != null) {
                if (z || DialogsActivity.this.searchViewPager.dialogsSearchAdapter.getItemCount() != 0) {
                    DialogsActivity.this.searchViewPager.emptyView.showProgress(true, z2);
                } else {
                    DialogsActivity.this.searchViewPager.emptyView.showProgress(false, z2);
                }
            }
            if (z && DialogsActivity.this.searchViewPager.dialogsSearchAdapter.getItemCount() == 0) {
                DialogsActivity.this.searchViewPager.cancelEnterAnimation();
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void didPressedBlockedDialog(View view, long j) {
            DialogsActivity.this.showPremiumBlockedToast(view, j);
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void didPressedOnSubDialog(long j) {
            if (DialogsActivity.this.onlySelect) {
                if (DialogsActivity.this.validateSlowModeDialog(j)) {
                    if (!DialogsActivity.this.selectedDialogs.isEmpty()) {
                        DialogsActivity.this.findAndUpdateCheckBox(j, DialogsActivity.this.addOrRemoveSelectedDialog(j, null));
                        DialogsActivity.this.updateSelectedCount();
                        ((BaseFragment) DialogsActivity.this).actionBar.closeSearchField();
                        return;
                    }
                    DialogsActivity.this.didSelectResult(j, 0, true, false);
                    return;
                }
                return;
            }
            Bundle bundle = new Bundle();
            if (DialogObject.isUserDialog(j)) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            DialogsActivity.this.closeSearch();
            if (AndroidUtilities.isTablet() && DialogsActivity.this.viewPages != null) {
                for (int i = 0; i < DialogsActivity.this.viewPages.length; i++) {
                    DialogsAdapter dialogsAdapter = DialogsActivity.this.viewPages[i].dialogsAdapter;
                    DialogsActivity.this.openedDialogId.dialogId = j;
                    dialogsAdapter.setOpenedDialogId(j);
                }
                DialogsActivity.this.updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
            }
            if (DialogsActivity.this.searchString != null) {
                if (DialogsActivity.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                    DialogsActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                    DialogsActivity.this.presentFragment(new ChatActivity(bundle));
                }
            } else if (DialogsActivity.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                DialogsActivity.this.presentFragment(new ChatActivity(bundle));
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void needRemoveHint(final long j) {
            TLRPC$User user;
            if (DialogsActivity.this.getParentActivity() == null || (user = DialogsActivity.this.getMessagesController().getUser(Long.valueOf(j))) == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("ChatHintsDeleteAlertTitle", R.string.ChatHintsDeleteAlertTitle));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ChatHintsDeleteAlert", R.string.ChatHintsDeleteAlert, ContactsController.formatName(user.first_name, user.last_name))));
            builder.setPositiveButton(LocaleController.getString("StickersRemove", R.string.StickersRemove), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$58$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DialogsActivity.58.this.lambda$needRemoveHint$0(j, dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            DialogsActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$needRemoveHint$0(long j, DialogInterface dialogInterface, int i) {
            DialogsActivity.this.getMediaDataController().removePeer(j);
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void needClearList() {
            AlertDialog.Builder builder = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
            if (DialogsActivity.this.searchViewPager.dialogsSearchAdapter.isSearchWas() && DialogsActivity.this.searchViewPager.dialogsSearchAdapter.isRecentSearchDisplayed()) {
                builder.setTitle(LocaleController.getString("ClearSearchAlertPartialTitle", R.string.ClearSearchAlertPartialTitle));
                builder.setMessage(LocaleController.formatPluralString("ClearSearchAlertPartial", DialogsActivity.this.searchViewPager.dialogsSearchAdapter.getRecentResultsCount(), new Object[0]));
                builder.setPositiveButton(LocaleController.getString("Clear", R.string.Clear), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$58$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DialogsActivity.58.this.lambda$needClearList$1(dialogInterface, i);
                    }
                });
            } else {
                builder.setTitle(LocaleController.getString("ClearSearchAlertTitle", R.string.ClearSearchAlertTitle));
                builder.setMessage(LocaleController.getString("ClearSearchAlert", R.string.ClearSearchAlert));
                builder.setPositiveButton(LocaleController.getString("ClearButton", R.string.ClearButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$58$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DialogsActivity.58.this.lambda$needClearList$2(dialogInterface, i);
                    }
                });
            }
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            DialogsActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$needClearList$1(DialogInterface dialogInterface, int i) {
            DialogsActivity.this.searchViewPager.dialogsSearchAdapter.clearRecentSearch();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$needClearList$2(DialogInterface dialogInterface, int i) {
            if (DialogsActivity.this.searchViewPager.dialogsSearchAdapter.isRecentSearchDisplayed()) {
                DialogsActivity.this.searchViewPager.dialogsSearchAdapter.clearRecentSearch();
            } else {
                DialogsActivity.this.searchViewPager.dialogsSearchAdapter.clearRecentHashtags();
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void runResultsEnterAnimation() {
            if (DialogsActivity.this.searchViewPager != null) {
                DialogsActivity.this.searchViewPager.runResultsEnterAnimation();
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public boolean isSelected(long j) {
            return DialogsActivity.this.selectedDialogs.contains(Long.valueOf(j));
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public long getSearchForumDialogId() {
            RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer == null || !(rightSlidingDialogContainer.getFragment() instanceof TopicsFragment)) {
                return 0L;
            }
            return ((TopicsFragment) DialogsActivity.this.rightSlidingDialogContainer.getFragment()).getDialogId();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSearchViewPager$130(View view, int i, float f, float f2) {
        Object object = this.searchViewPager.channelsSearchAdapter.getObject(i);
        if (object instanceof TLRPC$Chat) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", ((TLRPC$Chat) object).id);
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setNextChannels(this.searchViewPager.channelsSearchAdapter.getNextChannels(i));
            presentFragment(chatActivity);
        } else if (object instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) object;
            Bundle bundle2 = new Bundle();
            if (messageObject.getDialogId() >= 0) {
                bundle2.putLong("user_id", messageObject.getDialogId());
            } else {
                bundle2.putLong("chat_id", -messageObject.getDialogId());
            }
            bundle2.putInt("message_id", messageObject.getId());
            presentFragment(highlightFoundQuote(new ChatActivity(bundle2), messageObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSearchViewPager$131(View view, int i, float f, float f2) {
        Object topPeerObject = this.searchViewPager.botsSearchAdapter.getTopPeerObject(i);
        if (topPeerObject instanceof TLRPC$User) {
            getMessagesController().openApp((TLRPC$User) topPeerObject, getClassGuid());
            return;
        }
        Object object = this.searchViewPager.botsSearchAdapter.getObject(i);
        if (object instanceof TLRPC$User) {
            presentFragment(ProfileActivity.of(((TLRPC$User) object).id));
        } else if (object instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) object;
            Bundle bundle = new Bundle();
            if (messageObject.getDialogId() >= 0) {
                bundle.putLong("user_id", messageObject.getDialogId());
            } else {
                bundle.putLong("chat_id", -messageObject.getDialogId());
            }
            bundle.putInt("message_id", messageObject.getId());
            presentFragment(highlightFoundQuote(new ChatActivity(bundle), messageObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createSearchViewPager$133(View view, int i) {
        Object topPeerObject = this.searchViewPager.botsSearchAdapter.getTopPeerObject(i);
        if (topPeerObject instanceof TLRPC$User) {
            final TLRPC$User tLRPC$User = (TLRPC$User) topPeerObject;
            AlertDialog.Builder title = new AlertDialog.Builder(getContext(), this.resourceProvider).setTitle(LocaleController.getString(R.string.AppsClearSearch));
            int i2 = R.string.AppsClearSearchAlert;
            title.setMessage(LocaleController.formatString(i2, "\"" + UserObject.getUserName(tLRPC$User) + "\"")).setNegativeButton(LocaleController.getString(R.string.Cancel), null).setPositiveButton(LocaleController.getString(R.string.Remove), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda19
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    DialogsActivity.this.lambda$createSearchViewPager$132(tLRPC$User, dialogInterface, i3);
                }
            }).makeRed(-1).show();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSearchViewPager$132(TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
        getMediaDataController().removeWebapp(tLRPC$User.id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSearchViewPager$134(View view, int i, float f, float f2) {
        if (view instanceof ProfileSearchCell) {
            ProfileSearchCell profileSearchCell = (ProfileSearchCell) view;
            if (profileSearchCell.isBlocked()) {
                showPremiumBlockedToast(view, profileSearchCell.getDialogId());
                return;
            }
        }
        if (this.initialDialogsType == 10) {
            SearchViewPager searchViewPager = this.searchViewPager;
            onItemLongClick(searchViewPager.searchListView, view, i, f, f2, -1, searchViewPager.dialogsSearchAdapter);
            return;
        }
        onItemClick(view, i, this.searchViewPager.dialogsSearchAdapter, f, f2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSearchViewPager$135(boolean z, ArrayList arrayList, ArrayList arrayList2, boolean z2) {
        updateFiltersView(z, arrayList, arrayList2, z2, true);
    }
}
