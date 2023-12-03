package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Property;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebStorage;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Keep;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import j$.util.Comparator$-CC;
import j$.util.Comparator$-EL;
import j$.util.function.Consumer;
import j$.util.function.ToIntFunction;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FlagSecureReason;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LanguageDetector;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.TranslateController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInfo;
import org.telegram.tgnet.TLRPC$ChannelLocation;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$ChatParticipants;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$DecryptedMessageAction;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$PageListItem;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_password;
import org.telegram.tgnet.TLRPC$TL_account_updateEmojiStatus;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channelFull;
import org.telegram.tgnet.TLRPC$TL_channelLocation;
import org.telegram.tgnet.TLRPC$TL_channelParticipant;
import org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsRecent;
import org.telegram.tgnet.TLRPC$TL_channels_channelParticipant;
import org.telegram.tgnet.TLRPC$TL_channels_channelParticipants;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipant;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_chatChannelParticipant;
import org.telegram.tgnet.TLRPC$TL_chatFull;
import org.telegram.tgnet.TLRPC$TL_chatParticipant;
import org.telegram.tgnet.TLRPC$TL_chatParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_chatParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_chatParticipants;
import org.telegram.tgnet.TLRPC$TL_chatParticipantsForbidden;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_encryptedChat;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileLocation_layer97;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_help_dismissSuggestion;
import org.telegram.tgnet.TLRPC$TL_inputGroupCall;
import org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC$TL_inputPhoto;
import org.telegram.tgnet.TLRPC$TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC$TL_messages_getWebPage;
import org.telegram.tgnet.TLRPC$TL_messages_reportReaction;
import org.telegram.tgnet.TLRPC$TL_messages_webPage;
import org.telegram.tgnet.TLRPC$TL_pageBlockAnchor;
import org.telegram.tgnet.TLRPC$TL_pageBlockList;
import org.telegram.tgnet.TLRPC$TL_pageBlockParagraph;
import org.telegram.tgnet.TLRPC$TL_pageListItemText;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photos_photo;
import org.telegram.tgnet.TLRPC$TL_photos_updateProfilePhoto;
import org.telegram.tgnet.TLRPC$TL_photos_uploadProfilePhoto;
import org.telegram.tgnet.TLRPC$TL_userEmpty;
import org.telegram.tgnet.TLRPC$TL_userProfilePhoto;
import org.telegram.tgnet.TLRPC$TL_userProfilePhotoEmpty;
import org.telegram.tgnet.TLRPC$TL_username;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda6;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AboutLinkCell;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.SettingsSearchCell;
import org.telegram.ui.Cells.SettingsSuggestionCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedColor;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.AudioPlayerAlert;
import org.telegram.ui.Components.AutoDeletePopupWrapper;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackButtonMenu;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CanvasButton;
import org.telegram.ui.Components.ChatActivityInterface;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.ChatNotificationsPopupWrapper;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CrossfadeDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DotDividerSpan;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugController;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.IdenticonDrawable;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.InstantCameraView;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.MediaActivity;
import org.telegram.ui.Components.Paint.PersistColorPalette;
import org.telegram.ui.Components.Premium.GiftPremiumBottomSheet;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.Premium.PremiumPreviewBottomSheet;
import org.telegram.ui.Components.Premium.ProfilePremiumCell;
import org.telegram.ui.Components.ProfileGalleryView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SharedMediaLayout;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.TimerDrawable;
import org.telegram.ui.Components.TranslateAlert2;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.VectorAvatarThumbDrawable;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.ContactAddActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupCreateActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.PinchToZoomHelper;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
import org.telegram.ui.Stories.ProfileStoriesView;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.Stories.recorder.DualCameraView;
import org.telegram.ui.Stories.recorder.StoryRecorder;
/* loaded from: classes3.dex */
public class ProfileActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate, SharedMediaLayout.SharedMediaPreloaderDelegate, ImageUpdater.ImageUpdaterDelegate, SharedMediaLayout.Delegate {
    private Property<ActionBar, Float> ACTIONBAR_HEADER_PROGRESS;
    private final Property<ProfileActivity, Float> HEADER_SHADOW;
    private AboutLinkCell aboutLinkCell;
    private int actionBarAnimationColorFrom;
    private Paint actionBarBackgroundPaint;
    private int addMemberRow;
    private int addToContactsRow;
    private int addToGroupButtonRow;
    private int addToGroupInfoRow;
    private int administratorsRow;
    private boolean allowProfileAnimation;
    private boolean allowPullingDown;
    private DrawerProfileCell.AnimatedStatusView animatedStatusView;
    private ActionBarMenuItem animatingItem;
    private ActionBarMenuSubItem autoDeleteItem;
    TimerDrawable autoDeleteItemDrawable;
    AutoDeletePopupWrapper autoDeletePopupWrapper;
    private TLRPC$FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private float avatarAnimationProgress;
    private TLRPC$FileLocation avatarBig;
    private int avatarColor;
    private FrameLayout avatarContainer;
    private FrameLayout avatarContainer2;
    private AvatarDrawable avatarDrawable;
    private AvatarImageView avatarImage;
    private RadialProgressView avatarProgressView;
    private float avatarScale;
    int avatarUploadingRequest;
    private float avatarX;
    private float avatarY;
    private ProfileGalleryView avatarsViewPager;
    private PagerIndicatorView avatarsViewPagerIndicatorView;
    private long banFromGroup;
    private int bioRow;
    private int blockedUsersRow;
    private View blurredView;
    private TLRPC$BotInfo botInfo;
    private int bottomPaddingRow;
    private ActionBarMenuItem callItem;
    private boolean callItemVisible;
    private RLottieDrawable cameraDrawable;
    private boolean canSearchMembers;
    private RLottieDrawable cellCameraDrawable;
    private int channelInfoRow;
    private long chatId;
    private TLRPC$ChatFull chatInfo;
    private int chatRow;
    private int clearLogsRow;
    private NestedFrameLayout contentView;
    private boolean creatingChat;
    private CharSequence currentBio;
    private TLRPC$ChannelParticipant currentChannelParticipant;
    private TLRPC$Chat currentChat;
    private TLRPC$EncryptedChat currentEncryptedChat;
    private float currentExpanAnimatorFracture;
    private float currentExpandAnimatorValue;
    private TLRPC$TL_account_password currentPassword;
    private float customAvatarProgress;
    private float customPhotoOffset;
    private int dataRow;
    private int debugHeaderRow;
    private int devicesRow;
    private int devicesSectionRow;
    private long dialogId;
    private boolean disableProfileAnimation;
    private boolean doNotSetForeground;
    private ActionBarMenuItem editItem;
    private boolean editItemVisible;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] emojiStatusDrawable;
    private int emptyRow;
    private StickerEmptyView emptyView;
    private ValueAnimator expandAnimator;
    private float[] expandAnimatorValues;
    private boolean expandPhoto;
    private float expandProgress;
    private float extraHeight;
    private ImageReceiver fallbackImage;
    private int faqRow;
    private int filtersRow;
    private boolean firstLayout;
    private FlagSecureReason flagSecure;
    RLottieImageView floatingButton;
    FrameLayout floatingButtonContainer;
    float floatingButtonHideProgress;
    boolean floatingHidden;
    private final AccelerateDecelerateInterpolator floatingInterpolator;
    private boolean fragmentOpened;
    private boolean fragmentViewAttached;
    private HintView fwdRestrictedHint;
    private boolean hasCustomPhoto;
    private boolean hasFallbackPhoto;
    private boolean hasVoiceChatItem;
    private AnimatorSet headerAnimatorSet;
    protected float headerShadowAlpha;
    private AnimatorSet headerShadowAnimatorSet;
    private int helpHeaderRow;
    private int helpSectionCell;
    private ImageUpdater imageUpdater;
    private int infoHeaderRow;
    private int infoSectionRow;
    private float initialAnimationExtraHeight;
    private boolean invalidateScroll;
    private boolean isBot;
    public boolean isFragmentOpened;
    private boolean isFragmentPhoneNumber;
    private boolean isInLandscapeMode;
    private boolean[] isOnline;
    private boolean isPulledDown;
    private boolean isQrItemVisible;
    private boolean isTopic;
    private int joinRow;
    private int languageRow;
    private float lastEmojiStatusProgress;
    private int lastMeasuredContentHeight;
    private int lastMeasuredContentWidth;
    private int lastSectionRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private int listContentHeight;
    private RecyclerListView listView;
    private float listViewVelocityY;
    private int liteModeRow;
    private boolean loadingBoostsStats;
    private boolean loadingUsers;
    private int locationRow;
    private Drawable lockIconDrawable;
    private AudioPlayerAlert.ClippingTextViewSwitcher mediaCounterTextView;
    private float mediaHeaderAnimationProgress;
    private boolean mediaHeaderVisible;
    private int membersEndRow;
    private int membersHeaderRow;
    private int membersSectionRow;
    private int membersStartRow;
    private long mergeDialogId;
    private SimpleTextView[] nameTextView;
    private String nameTextViewRightDrawableContentDescription;
    private float nameX;
    private float nameY;
    private int navigationBarAnimationColorFrom;
    private boolean needSendMessage;
    private boolean needTimerImage;
    private int notificationRow;
    private int notificationsDividerRow;
    HashSet<Integer> notificationsExceptionTopics;
    private int notificationsRow;
    private int notificationsSimpleRow;
    private int numberRow;
    private int numberSectionRow;
    private int onlineCount;
    private SimpleTextView[] onlineTextView;
    private float onlineX;
    private float onlineY;
    private boolean openAnimationInProgress;
    private boolean openSimilar;
    private boolean openingAvatar;
    private ActionBarMenuItem otherItem;
    private int overlayCountVisible;
    private OverlaysView overlaysView;
    private LongSparseArray<TLRPC$ChatParticipant> participantsMap;
    private int passwordSuggestionRow;
    private int passwordSuggestionSectionRow;
    private MessagesController.PeerColor peerColor;
    private int phoneRow;
    private int phoneSuggestionRow;
    private int phoneSuggestionSectionRow;
    float photoDescriptionProgress;
    PinchToZoomHelper pinchToZoomHelper;
    private int playProfileAnimation;
    private int policyRow;
    private HashMap<Integer, Integer> positionToOffset;
    private final CrossfadeDrawable[] premiumCrossfadeDrawable;
    private int premiumRow;
    private int premiumSectionsRow;
    private final Drawable[] premiumStarDrawable;
    private ImageLocation prevLoadedImageLocation;
    ChatActivityInterface previousTransitionFragment;
    private int privacyRow;
    boolean profileTransitionInProgress;
    private PhotoViewer.PhotoViewerProvider provider;
    private ActionBarMenuItem qrItem;
    private AnimatorSet qrItemAnimation;
    private int questionRow;
    private boolean recreateMenuAfterAnimation;
    private Rect rect;
    private int reportDividerRow;
    private long reportReactionFromDialogId;
    private int reportReactionMessageId;
    private int reportReactionRow;
    private int reportRow;
    private boolean reportSpam;
    private Theme.ResourcesProvider resourcesProvider;
    private int rowCount;
    int savedScrollOffset;
    int savedScrollPosition;
    boolean savedScrollToSharedMedia;
    private ScamDrawable scamDrawable;
    private AnimatorSet scrimAnimatorSet;
    private Paint scrimPaint;
    private View scrimView;
    private boolean scrolling;
    private SearchAdapter searchAdapter;
    private ActionBarMenuItem searchItem;
    private RecyclerListView searchListView;
    private boolean searchMode;
    private int searchTransitionOffset;
    private float searchTransitionProgress;
    private Animator searchViewTransition;
    private int secretSettingsSectionRow;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;
    private long selectedUser;
    private int sendLastLogsRow;
    private int sendLogsRow;
    private int sendMessageRow;
    private TextCell setAvatarCell;
    private int setAvatarRow;
    private int setAvatarSectionRow;
    private int setUsernameRow;
    private int settingsKeyRow;
    private int settingsRow;
    private int settingsSectionRow;
    private int settingsSectionRow2;
    private int settingsTimerRow;
    private SharedMediaLayout sharedMediaLayout;
    private boolean sharedMediaLayoutAttached;
    private SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader;
    private int sharedMediaRow;
    private boolean showAddToContacts;
    boolean showBoostsAlert;
    private ArrayList<Integer> sortedUsers;
    private int stickersRow;
    private ProfileStoriesView storyView;
    private int subscribersRequestsRow;
    private int subscribersRow;
    private int switchBackendRow;
    private ImageView timeItem;
    private TimerDrawable timerDrawable;
    private float titleAnimationsYDiff;
    private TopView topView;
    private int topicId;
    private boolean transitionAnimationInProress;
    private int transitionIndex;
    private View transitionOnlineText;
    private ImageView ttlIconView;
    private int unblockRow;
    private UndoView undoView;
    private ImageLocation uploadingImageLocation;
    private boolean userBlocked;
    private long userId;
    private TLRPC$UserFull userInfo;
    private int userInfoRow;
    private int usernameRow;
    private boolean usersEndReached;
    private int usersForceShowingIn;
    private String vcardFirstName;
    private String vcardLastName;
    private String vcardPhone;
    private final Drawable[] verifiedCheckDrawable;
    private final CrossfadeDrawable[] verifiedCrossfadeDrawable;
    private final Drawable[] verifiedDrawable;
    private int versionRow;
    private ActionBarMenuItem videoCallItem;
    private boolean videoCallItemVisible;
    private final ArrayList<TLRPC$ChatParticipant> visibleChatParticipants;
    private final ArrayList<Integer> visibleSortedUsers;
    private boolean waitCanSendStoryRequest;
    private Paint whitePaint;
    private RLottieImageView writeButton;
    private AnimatorSet writeButtonAnimation;

    private boolean isQrNeedVisible() {
        return true;
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ boolean canFinishFragment() {
        return ImageUpdater.ImageUpdaterDelegate.-CC.$default$canFinishFragment(this);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ void didUploadFailed() {
        ImageUpdater.ImageUpdaterDelegate.-CC.$default$didUploadFailed(this);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ String getInitialSearchString() {
        return ImageUpdater.ImageUpdaterDelegate.-CC.$default$getInitialSearchString(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void access$31900(ProfileActivity profileActivity, View view) {
        profileActivity.onTextDetailCellImageClicked(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void access$7400(ProfileActivity profileActivity) {
        profileActivity.onWriteButtonClick();
    }

    static /* synthetic */ int access$9512(ProfileActivity profileActivity, int i) {
        int i2 = profileActivity.listContentHeight + i;
        profileActivity.listContentHeight = i2;
        return i2;
    }

    public static ProfileActivity of(long j) {
        Bundle bundle = new Bundle();
        if (j >= 0) {
            bundle.putLong("user_id", j);
        } else {
            bundle.putLong("chat_id", -j);
        }
        return new ProfileActivity(bundle);
    }

    public int getTopicId() {
        return this.topicId;
    }

    /* loaded from: classes3.dex */
    public static class AvatarImageView extends BackupImageView {
        ProfileGalleryView avatarsViewPager;
        public float bounceScale;
        public boolean drawAvatar;
        boolean drawForeground;
        private ImageReceiver.BitmapHolder drawableHolder;
        private float foregroundAlpha;
        private ImageReceiver foregroundImageReceiver;
        private boolean hasStories;
        private final Paint placeholderPaint;
        float progressToExpand;
        private float progressToInsets;
        private final RectF rect;

        public ChatActivityInterface getPrevFragment() {
            return null;
        }

        public void setAvatarsViewPager(ProfileGalleryView profileGalleryView) {
            this.avatarsViewPager = profileGalleryView;
        }

        public AvatarImageView(Context context) {
            super(context);
            this.rect = new RectF();
            this.drawAvatar = true;
            this.bounceScale = 1.0f;
            this.drawForeground = true;
            this.progressToInsets = 1.0f;
            this.foregroundImageReceiver = new ImageReceiver(this);
            Paint paint = new Paint(1);
            this.placeholderPaint = paint;
            paint.setColor(-16777216);
        }

        public void setForegroundImage(ImageLocation imageLocation, String str, Drawable drawable) {
            this.foregroundImageReceiver.setImage(imageLocation, str, drawable, 0L, (String) null, (Object) null, 0);
            ImageReceiver.BitmapHolder bitmapHolder = this.drawableHolder;
            if (bitmapHolder != null) {
                bitmapHolder.release();
                this.drawableHolder = null;
            }
        }

        public void setForegroundImageDrawable(ImageReceiver.BitmapHolder bitmapHolder) {
            if (bitmapHolder != null) {
                this.foregroundImageReceiver.setImageBitmap(bitmapHolder.drawable);
            }
            ImageReceiver.BitmapHolder bitmapHolder2 = this.drawableHolder;
            if (bitmapHolder2 != null) {
                bitmapHolder2.release();
                this.drawableHolder = null;
            }
            this.drawableHolder = bitmapHolder;
        }

        public float getForegroundAlpha() {
            return this.foregroundAlpha;
        }

        public void setForegroundAlpha(float f) {
            this.foregroundAlpha = f;
            invalidate();
        }

        public void clearForeground() {
            AnimatedFileDrawable animation = this.foregroundImageReceiver.getAnimation();
            if (animation != null) {
                animation.removeSecondParentView(this);
            }
            this.foregroundImageReceiver.clearImage();
            ImageReceiver.BitmapHolder bitmapHolder = this.drawableHolder;
            if (bitmapHolder != null) {
                bitmapHolder.release();
                this.drawableHolder = null;
            }
            this.foregroundAlpha = 0.0f;
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.foregroundImageReceiver.onDetachedFromWindow();
            ImageReceiver.BitmapHolder bitmapHolder = this.drawableHolder;
            if (bitmapHolder != null) {
                bitmapHolder.release();
                this.drawableHolder = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.foregroundImageReceiver.onAttachedToWindow();
        }

        @Override // org.telegram.ui.Components.BackupImageView
        public void setRoundRadius(int i) {
            super.setRoundRadius(i);
            this.foregroundImageReceiver.setRoundRadius(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            ImageReceiver imageReceiver = animatedEmojiDrawable != null ? animatedEmojiDrawable.getImageReceiver() : this.imageReceiver;
            canvas.save();
            float f = this.bounceScale;
            canvas.scale(f, f, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
            float dpf2 = (this.hasStories ? (int) AndroidUtilities.dpf2(3.5f) : 0.0f) * (1.0f - this.progressToExpand);
            float f2 = this.progressToInsets;
            float f3 = this.foregroundAlpha;
            float f4 = dpf2 * f2 * (1.0f - f3);
            if (imageReceiver != null && (f3 < 1.0f || !this.drawForeground)) {
                float f5 = f4 * 2.0f;
                imageReceiver.setImageCoords(f4, f4, getMeasuredWidth() - f5, getMeasuredHeight() - f5);
                if (this.drawAvatar) {
                    imageReceiver.draw(canvas);
                }
            }
            if (this.foregroundAlpha > 0.0f && this.drawForeground) {
                if (this.foregroundImageReceiver.getDrawable() != null) {
                    float f6 = 2.0f * f4;
                    this.foregroundImageReceiver.setImageCoords(f4, f4, getMeasuredWidth() - f6, getMeasuredHeight() - f6);
                    this.foregroundImageReceiver.setAlpha(this.foregroundAlpha);
                    this.foregroundImageReceiver.draw(canvas);
                } else {
                    this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    this.placeholderPaint.setAlpha((int) (this.foregroundAlpha * 255.0f));
                    float f7 = this.foregroundImageReceiver.getRoundRadius()[0];
                    canvas.drawRoundRect(this.rect, f7, f7, this.placeholderPaint);
                }
            }
            canvas.restore();
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            ProfileGalleryView profileGalleryView = this.avatarsViewPager;
            if (profileGalleryView != null) {
                profileGalleryView.invalidate();
            }
        }

        public void setProgressToStoriesInsets(float f) {
            if (f == this.progressToInsets) {
                return;
            }
            this.progressToInsets = f;
            invalidate();
        }

        public void drawForeground(boolean z) {
            this.drawForeground = z;
        }

        public void setHasStories(boolean z) {
            if (this.hasStories == z) {
                return;
            }
            this.hasStories = z;
            invalidate();
        }

        public void setProgressToExpand(float f) {
            if (this.progressToExpand == f) {
                return;
            }
            this.progressToExpand = f;
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TopView extends View {
        private LinearGradient backgroundGradient;
        private int backgroundGradientColor1;
        private int backgroundGradientColor2;
        private int backgroundGradientHeight;
        private final Paint backgroundPaint;
        public int color1;
        private final AnimatedColor color1Animated;
        public int color2;
        private final AnimatedColor color2Animated;
        private int currentColor;
        private final AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable emoji;
        private int emojiColor;
        private boolean emojiLoaded;
        public final AnimatedFloat emojiLoadedT;
        private final AnimatedFloat hasColorAnimated;
        private boolean hasColorById;
        private boolean hasEmoji;
        private Paint paint;

        public TopView(Context context) {
            super(context);
            this.paint = new Paint();
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.hasColorAnimated = new AnimatedFloat(this, 350L, cubicBezierInterpolator);
            this.color1Animated = new AnimatedColor(this, 350L, cubicBezierInterpolator);
            this.color2Animated = new AnimatedColor(this, 350L, cubicBezierInterpolator);
            this.backgroundPaint = new Paint(1);
            this.emoji = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, false, AndroidUtilities.dp(20.0f), 13);
            this.emojiLoadedT = new AnimatedFloat(this, 0L, 440L, cubicBezierInterpolator);
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i) + AndroidUtilities.dp(3.0f));
        }

        @Override // android.view.View
        public void setBackgroundColor(int i) {
            if (i != this.currentColor) {
                this.currentColor = i;
                this.paint.setColor(i);
                invalidate();
            }
        }

        public void setBackgroundColorId(MessagesController.PeerColor peerColor, boolean z) {
            if (peerColor != null) {
                this.hasColorById = true;
                this.color1 = peerColor.getBgColor1(Theme.isCurrentThemeDark());
                this.color2 = peerColor.getBgColor2(Theme.isCurrentThemeDark());
                this.emojiColor = PeerColorActivity.adaptProfileEmojiColor(this.color1);
            } else {
                this.hasColorById = false;
                ProfileActivity profileActivity = ProfileActivity.this;
                int i = Theme.key_actionBarDefault;
                if (AndroidUtilities.computePerceivedBrightness(profileActivity.getThemedColor(i)) > 0.8f) {
                    this.emojiColor = ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlueText);
                } else if (AndroidUtilities.computePerceivedBrightness(ProfileActivity.this.getThemedColor(i)) < 0.2f) {
                    this.emojiColor = Theme.multAlpha(ProfileActivity.this.getThemedColor(Theme.key_actionBarDefaultTitle), 0.5f);
                } else {
                    this.emojiColor = PeerColorActivity.adaptProfileEmojiColor(ProfileActivity.this.getThemedColor(i));
                }
            }
            if (!z) {
                this.color1Animated.set(this.color1, true);
                this.color2Animated.set(this.color2, true);
            }
            invalidate();
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.emoji.attach();
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.emoji.detach();
        }

        public void setBackgroundEmojiId(long j, boolean z) {
            this.emoji.set(j, z);
            this.emoji.setColor(Integer.valueOf(this.emojiColor));
            this.hasEmoji = this.hasEmoji || !(j == 0 || j == -1);
            invalidate();
        }

        private boolean isEmojiLoaded() {
            if (this.emojiLoaded) {
                return true;
            }
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emoji;
            if (swapAnimatedEmojiDrawable == null || !(swapAnimatedEmojiDrawable.getDrawable() instanceof AnimatedEmojiDrawable)) {
                return false;
            }
            AnimatedEmojiDrawable animatedEmojiDrawable = (AnimatedEmojiDrawable) this.emoji.getDrawable();
            if (animatedEmojiDrawable.getImageReceiver() == null || !animatedEmojiDrawable.getImageReceiver().hasImageLoaded()) {
                return false;
            }
            this.emojiLoaded = true;
            return true;
        }

        @Override // android.view.View
        protected void onDraw(final Canvas canvas) {
            float currentActionBarHeight = ProfileActivity.this.extraHeight + ActionBar.getCurrentActionBarHeight() + (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ProfileActivity.this.searchTransitionOffset;
            int i = (int) ((1.0f - ProfileActivity.this.mediaHeaderAnimationProgress) * currentActionBarHeight);
            if (i != 0) {
                ChatActivityInterface chatActivityInterface = ProfileActivity.this.previousTransitionFragment;
                if (chatActivityInterface != null && chatActivityInterface.getContentView() != null) {
                    Rect rect = AndroidUtilities.rectTmp2;
                    rect.set(0, 0, getMeasuredWidth(), i);
                    if (ProfileActivity.this.previousTransitionFragment.getActionBar() != null && !ProfileActivity.this.previousTransitionFragment.getContentView().blurWasDrawn() && ProfileActivity.this.previousTransitionFragment.getActionBar().getBackground() == null) {
                        this.paint.setColor(Theme.getColor(Theme.key_actionBarDefault, ProfileActivity.this.previousTransitionFragment.getResourceProvider()));
                        canvas.drawRect(rect, this.paint);
                    } else {
                        ProfileActivity.this.previousTransitionFragment.getContentView().drawBlurRect(canvas, getY(), rect, ProfileActivity.this.previousTransitionFragment.getActionBar().blurScrimPaint, true);
                    }
                }
                this.paint.setColor(this.currentColor);
                int i2 = this.color1Animated.set(this.color1);
                int i3 = this.color2Animated.set(this.color2);
                int dp = AndroidUtilities.statusBarHeight + AndroidUtilities.dp(144.0f);
                if (this.backgroundGradient == null || this.backgroundGradientColor1 != i2 || this.backgroundGradientColor2 != i3 || this.backgroundGradientHeight != dp) {
                    this.backgroundGradientHeight = dp;
                    this.backgroundGradientColor2 = i3;
                    this.backgroundGradientColor1 = i2;
                    LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, dp, new int[]{i3, i2}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                    this.backgroundGradient = linearGradient;
                    this.backgroundPaint.setShader(linearGradient);
                }
                float f = (ProfileActivity.this.playProfileAnimation == 0 ? 1.0f : ProfileActivity.this.avatarAnimationProgress) * this.hasColorAnimated.set(this.hasColorById);
                if (f < 1.0f) {
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), i, this.paint);
                }
                if (f > 0.0f) {
                    this.backgroundPaint.setAlpha((int) (f * 255.0f));
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), i, this.backgroundPaint);
                }
                if (this.hasEmoji && this.emojiLoadedT.set(isEmojiLoaded()) > 0.0f) {
                    canvas.save();
                    canvas.clipRect(0, 0, getMeasuredWidth(), i);
                    PeerColorActivity.drawProfileIconPattern(getMeasuredWidth() - AndroidUtilities.dp(46.0f), ((((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.dp(144.0f)) - ((1.0f - (ProfileActivity.this.extraHeight / AndroidUtilities.dp(88.0f))) * AndroidUtilities.dp(33.0f)), (((ProfileActivity.this.extraHeight / AndroidUtilities.dp(88.0f)) - 1.0f) * 0.2f) + 1.0f, new Utilities.Callback4() { // from class: org.telegram.ui.ProfileActivity$TopView$$ExternalSyntheticLambda0
                        @Override // org.telegram.messenger.Utilities.Callback4
                        public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                            ProfileActivity.TopView.this.lambda$onDraw$0(canvas, (Float) obj, (Float) obj2, (Float) obj3, (Float) obj4);
                        }
                    });
                    canvas.restore();
                }
                ChatActivityInterface chatActivityInterface2 = ProfileActivity.this.previousTransitionFragment;
                if (chatActivityInterface2 != null) {
                    ActionBar actionBar = chatActivityInterface2.getActionBar();
                    ActionBarMenu actionBarMenu = actionBar.menu;
                    int save = canvas.save();
                    canvas.translate(actionBar.getX() + actionBarMenu.getX(), actionBar.getY() + actionBarMenu.getY());
                    canvas.saveLayerAlpha(0.0f, 0.0f, actionBarMenu.getMeasuredWidth(), actionBarMenu.getMeasuredHeight(), (int) ((1.0f - ProfileActivity.this.avatarAnimationProgress) * 255.0f), 31);
                    actionBarMenu.draw(canvas);
                    canvas.restoreToCount(save);
                }
            }
            if (i != currentActionBarHeight) {
                this.paint.setColor(ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                Rect rect2 = AndroidUtilities.rectTmp2;
                rect2.set(0, i, getMeasuredWidth(), (int) currentActionBarHeight);
                ProfileActivity.this.contentView.drawBlurRect(canvas, getY(), rect2, this.paint, true);
            }
            if (((BaseFragment) ProfileActivity.this).parentLayout != null) {
                ((BaseFragment) ProfileActivity.this).parentLayout.drawHeaderShadow(canvas, (int) (ProfileActivity.this.headerShadowAlpha * 255.0f), (int) currentActionBarHeight);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDraw$0(Canvas canvas, Float f, Float f2, Float f3, Float f4) {
            this.emoji.setAlpha((int) (f4.floatValue() * 255.0f * Math.min(1.0f, ProfileActivity.this.extraHeight / AndroidUtilities.dp(88.0f))));
            this.emoji.setBounds((int) (f.floatValue() - (f3.floatValue() * 0.45f)), (int) (f2.floatValue() - (f3.floatValue() * 0.45f)), (int) (f.floatValue() + (f3.floatValue() * 0.45f)), (int) (f2.floatValue() + (f3.floatValue() * 0.45f)));
            this.emoji.draw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class OverlaysView extends View implements ProfileGalleryView.Callback {
        private float alpha;
        private float[] alphas;
        private final ValueAnimator animator;
        private final float[] animatorValues;
        private final Paint backgroundPaint;
        private final Paint barPaint;
        private final GradientDrawable bottomOverlayGradient;
        private final Rect bottomOverlayRect;
        private float currentAnimationValue;
        private int currentLoadingAnimationDirection;
        private float currentLoadingAnimationProgress;
        private float currentProgress;
        private boolean isOverlaysVisible;
        private long lastTime;
        private final float[] pressedOverlayAlpha;
        private final GradientDrawable[] pressedOverlayGradient;
        private final boolean[] pressedOverlayVisible;
        private int previousSelectedPotision;
        private float previousSelectedProgress;
        private final RectF rect;
        private final Paint selectedBarPaint;
        private int selectedPosition;
        private final int statusBarHeight;
        private final GradientDrawable topOverlayGradient;
        private final Rect topOverlayRect;

        public OverlaysView(Context context) {
            super(context);
            this.statusBarHeight = (!((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() || ((BaseFragment) ProfileActivity.this).inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight;
            this.topOverlayRect = new Rect();
            this.bottomOverlayRect = new Rect();
            this.rect = new RectF();
            this.animatorValues = new float[]{0.0f, 1.0f};
            this.pressedOverlayGradient = new GradientDrawable[2];
            this.pressedOverlayVisible = new boolean[2];
            this.pressedOverlayAlpha = new float[2];
            this.alpha = 0.0f;
            this.alphas = null;
            this.previousSelectedPotision = -1;
            this.currentLoadingAnimationDirection = 1;
            setVisibility(8);
            Paint paint = new Paint(1);
            this.barPaint = paint;
            paint.setColor(1442840575);
            Paint paint2 = new Paint(1);
            this.selectedBarPaint = paint2;
            paint2.setColor(-1);
            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{1107296256, 0});
            this.topOverlayGradient = gradientDrawable;
            gradientDrawable.setShape(0);
            GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{1107296256, 0});
            this.bottomOverlayGradient = gradientDrawable2;
            gradientDrawable2.setShape(0);
            int i = 0;
            while (i < 2) {
                this.pressedOverlayGradient[i] = new GradientDrawable(i == 0 ? GradientDrawable.Orientation.LEFT_RIGHT : GradientDrawable.Orientation.RIGHT_LEFT, new int[]{838860800, 0});
                this.pressedOverlayGradient[i].setShape(0);
                i++;
            }
            Paint paint3 = new Paint(1);
            this.backgroundPaint = paint3;
            paint3.setColor(-16777216);
            paint3.setAlpha(66);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.animator = ofFloat;
            ofFloat.setDuration(250L);
            ofFloat.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$OverlaysView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ProfileActivity.OverlaysView.this.lambda$new$0(valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter(ProfileActivity.this) { // from class: org.telegram.ui.ProfileActivity.OverlaysView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (OverlaysView.this.isOverlaysVisible) {
                        return;
                    }
                    OverlaysView.this.setVisibility(8);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    OverlaysView.this.setVisibility(0);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
            float[] fArr = this.animatorValues;
            float animatedFraction = valueAnimator.getAnimatedFraction();
            this.currentAnimationValue = animatedFraction;
            setAlphaValue(AndroidUtilities.lerp(fArr, animatedFraction), true);
        }

        public void saveCurrentPageProgress() {
            this.previousSelectedProgress = this.currentProgress;
            this.previousSelectedPotision = this.selectedPosition;
            this.currentLoadingAnimationProgress = 0.0f;
            this.currentLoadingAnimationDirection = 1;
        }

        public void setAlphaValue(float f, boolean z) {
            if (Build.VERSION.SDK_INT > 18) {
                int i = (int) (255.0f * f);
                this.topOverlayGradient.setAlpha(i);
                this.bottomOverlayGradient.setAlpha(i);
                this.backgroundPaint.setAlpha((int) (66.0f * f));
                this.barPaint.setAlpha((int) (85.0f * f));
                this.selectedBarPaint.setAlpha(i);
                this.alpha = f;
            } else {
                setAlpha(f);
            }
            if (!z) {
                this.currentAnimationValue = f;
            }
            invalidate();
        }

        public boolean isOverlaysVisible() {
            return this.isOverlaysVisible;
        }

        public void setOverlaysVisible() {
            this.isOverlaysVisible = true;
            setVisibility(0);
        }

        public void setOverlaysVisible(boolean z, float f) {
            if (z != this.isOverlaysVisible) {
                this.isOverlaysVisible = z;
                this.animator.cancel();
                float lerp = AndroidUtilities.lerp(this.animatorValues, this.currentAnimationValue);
                if (z) {
                    this.animator.setDuration(((1.0f - lerp) * 250.0f) / f);
                } else {
                    this.animator.setDuration((250.0f * lerp) / f);
                }
                float[] fArr = this.animatorValues;
                fArr[0] = lerp;
                fArr[1] = z ? 1.0f : 0.0f;
                this.animator.start();
            }
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            int currentActionBarHeight = this.statusBarHeight + ActionBar.getCurrentActionBarHeight();
            this.topOverlayRect.set(0, 0, i, (int) (currentActionBarHeight * 0.5f));
            this.bottomOverlayRect.set(0, (int) (i2 - (AndroidUtilities.dp(72.0f) * 0.5f)), i, i2);
            this.topOverlayGradient.setBounds(0, this.topOverlayRect.bottom, i, currentActionBarHeight + AndroidUtilities.dp(16.0f));
            this.bottomOverlayGradient.setBounds(0, (i2 - AndroidUtilities.dp(72.0f)) - AndroidUtilities.dp(24.0f), i, this.bottomOverlayRect.top);
            int i5 = i / 5;
            this.pressedOverlayGradient[0].setBounds(0, 0, i5, i2);
            this.pressedOverlayGradient[1].setBounds(i - i5, 0, i, i2);
        }

        /* JADX WARN: Removed duplicated region for block: B:69:0x021c  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x0241  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x0259  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x025c  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            boolean z;
            int i;
            float f;
            for (int i2 = 0; i2 < 2; i2++) {
                float[] fArr = this.pressedOverlayAlpha;
                if (fArr[i2] > 0.0f) {
                    this.pressedOverlayGradient[i2].setAlpha((int) (fArr[i2] * 255.0f));
                    this.pressedOverlayGradient[i2].draw(canvas);
                }
            }
            this.topOverlayGradient.draw(canvas);
            this.bottomOverlayGradient.draw(canvas);
            canvas.drawRect(this.topOverlayRect, this.backgroundPaint);
            canvas.drawRect(this.bottomOverlayRect, this.backgroundPaint);
            int realCount = ProfileActivity.this.avatarsViewPager.getRealCount();
            this.selectedPosition = ProfileActivity.this.avatarsViewPager.getRealPosition();
            float[] fArr2 = this.alphas;
            if (fArr2 == null || fArr2.length != realCount) {
                float[] fArr3 = new float[realCount];
                this.alphas = fArr3;
                Arrays.fill(fArr3, 0.0f);
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastTime;
            j = (j < 0 || j > 20) ? 17L : 17L;
            this.lastTime = elapsedRealtime;
            float f2 = 1.0f;
            if (realCount <= 1 || realCount > 20) {
                z = false;
            } else {
                if (ProfileActivity.this.overlayCountVisible != 0) {
                    if (ProfileActivity.this.overlayCountVisible == 1) {
                        this.alpha = 0.0f;
                        ProfileActivity.this.overlayCountVisible = 2;
                    }
                } else {
                    this.alpha = 0.0f;
                    ProfileActivity.this.overlayCountVisible = 3;
                }
                float f3 = 85.0f;
                if (ProfileActivity.this.overlayCountVisible == 2) {
                    this.barPaint.setAlpha((int) (this.alpha * 85.0f));
                    this.selectedBarPaint.setAlpha((int) (this.alpha * 255.0f));
                }
                int measuredWidth = ((getMeasuredWidth() - AndroidUtilities.dp(10.0f)) - AndroidUtilities.dp((realCount - 1) * 2)) / realCount;
                int dp = AndroidUtilities.dp(4.0f) + ((Build.VERSION.SDK_INT < 21 || ((BaseFragment) ProfileActivity.this).inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight);
                int i3 = 0;
                z = false;
                while (i3 < realCount) {
                    int dp2 = AndroidUtilities.dp((i3 * 2) + 5) + (measuredWidth * i3);
                    if (i3 == this.previousSelectedPotision && Math.abs(this.previousSelectedProgress - f2) > 1.0E-4f) {
                        f = this.previousSelectedProgress;
                        canvas.save();
                        float f4 = dp2;
                        float f5 = dp;
                        float f6 = dp2 + measuredWidth;
                        canvas.clipRect((measuredWidth * f) + f4, f5, f6, dp + AndroidUtilities.dp(2.0f));
                        this.rect.set(f4, f5, f6, AndroidUtilities.dp(2.0f) + dp);
                        this.barPaint.setAlpha((int) (this.alpha * f3));
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.barPaint);
                        canvas.restore();
                        i = 80;
                    } else {
                        if (i3 == this.selectedPosition) {
                            if (ProfileActivity.this.avatarsViewPager.isCurrentItemVideo()) {
                                f = ProfileActivity.this.avatarsViewPager.getCurrentItemProgress();
                                this.currentProgress = f;
                                if ((f <= 0.0f && ProfileActivity.this.avatarsViewPager.isLoadingCurrentVideo()) || this.currentLoadingAnimationProgress > 0.0f) {
                                    float f7 = this.currentLoadingAnimationProgress;
                                    int i4 = this.currentLoadingAnimationDirection;
                                    float f8 = f7 + (((float) (i4 * j)) / 500.0f);
                                    this.currentLoadingAnimationProgress = f8;
                                    if (f8 > 1.0f) {
                                        this.currentLoadingAnimationProgress = 1.0f;
                                        this.currentLoadingAnimationDirection = i4 * (-1);
                                    } else if (f8 <= 0.0f) {
                                        this.currentLoadingAnimationProgress = 0.0f;
                                        this.currentLoadingAnimationDirection = i4 * (-1);
                                    }
                                }
                                this.rect.set(dp2, dp, dp2 + measuredWidth, AndroidUtilities.dp(2.0f) + dp);
                                this.barPaint.setAlpha((int) (((this.currentLoadingAnimationProgress * 48.0f) + 85.0f) * this.alpha));
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.barPaint);
                                i = 80;
                            } else {
                                this.currentProgress = 1.0f;
                            }
                        }
                        i = 85;
                        f = 1.0f;
                        float f9 = dp2;
                        this.rect.set(f9, dp, (measuredWidth * f) + f9, AndroidUtilities.dp(2.0f) + dp);
                        if (i3 == this.selectedPosition) {
                            if (ProfileActivity.this.overlayCountVisible == 3) {
                                this.barPaint.setAlpha((int) (AndroidUtilities.lerp(i, 255, CubicBezierInterpolator.EASE_BOTH.getInterpolation(this.alphas[i3])) * this.alpha));
                            }
                        } else {
                            this.alphas[i3] = 0.75f;
                        }
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), i3 != this.selectedPosition ? this.selectedBarPaint : this.barPaint);
                        i3++;
                        f2 = 1.0f;
                        f3 = 85.0f;
                    }
                    z = true;
                    float f92 = dp2;
                    this.rect.set(f92, dp, (measuredWidth * f) + f92, AndroidUtilities.dp(2.0f) + dp);
                    if (i3 == this.selectedPosition) {
                    }
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), i3 != this.selectedPosition ? this.selectedBarPaint : this.barPaint);
                    i3++;
                    f2 = 1.0f;
                    f3 = 85.0f;
                }
                if (ProfileActivity.this.overlayCountVisible != 2) {
                    if (ProfileActivity.this.overlayCountVisible == 3) {
                        int i5 = 0;
                        while (true) {
                            float[] fArr4 = this.alphas;
                            if (i5 >= fArr4.length) {
                                break;
                            }
                            if (i5 != this.selectedPosition && fArr4[i5] > 0.0f) {
                                fArr4[i5] = fArr4[i5] - (((float) j) / 500.0f);
                                if (fArr4[i5] <= 0.0f) {
                                    fArr4[i5] = 0.0f;
                                    if (i5 == this.previousSelectedPotision) {
                                        this.previousSelectedPotision = -1;
                                    }
                                }
                                z = true;
                            } else if (i5 == this.previousSelectedPotision) {
                                this.previousSelectedPotision = -1;
                            }
                            i5++;
                        }
                    }
                } else {
                    float f10 = this.alpha;
                    if (f10 >= 1.0f) {
                        ProfileActivity.this.overlayCountVisible = 3;
                    } else {
                        float f11 = f10 + (((float) j) / 180.0f);
                        this.alpha = f11;
                        if (f11 > 1.0f) {
                            this.alpha = 1.0f;
                        }
                        z = true;
                    }
                }
            }
            for (int i6 = 0; i6 < 2; i6++) {
                if (this.pressedOverlayVisible[i6]) {
                    float[] fArr5 = this.pressedOverlayAlpha;
                    if (fArr5[i6] < 1.0f) {
                        fArr5[i6] = fArr5[i6] + (((float) j) / 180.0f);
                        if (fArr5[i6] > 1.0f) {
                            fArr5[i6] = 1.0f;
                        }
                        z = true;
                    }
                } else {
                    float[] fArr6 = this.pressedOverlayAlpha;
                    if (fArr6[i6] > 0.0f) {
                        fArr6[i6] = fArr6[i6] - (((float) j) / 180.0f);
                        if (fArr6[i6] < 0.0f) {
                            fArr6[i6] = 0.0f;
                        }
                        z = true;
                    }
                }
            }
            if (z) {
                postInvalidateOnAnimation();
            }
        }

        @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
        public void onDown(boolean z) {
            this.pressedOverlayVisible[!z ? 1 : 0] = true;
            postInvalidateOnAnimation();
        }

        @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
        public void onRelease() {
            Arrays.fill(this.pressedOverlayVisible, false);
            postInvalidateOnAnimation();
        }

        @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
        public void onPhotosLoaded() {
            ProfileActivity.this.updateProfileData(false);
        }

        @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
        public void onVideoSet() {
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class NestedFrameLayout extends SizeNotifierFrameLayout implements NestedScrollingParent3 {
        private NestedScrollingParentHelper nestedScrollingParentHelper;

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onStopNestedScroll(View view) {
        }

        public NestedFrameLayout(Context context) {
            super(context);
            this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        }

        @Override // androidx.core.view.NestedScrollingParent3
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
            try {
                boolean z = true;
                if (view == ProfileActivity.this.listView && ProfileActivity.this.sharedMediaLayoutAttached) {
                    RecyclerListView currentListView = ProfileActivity.this.sharedMediaLayout.getCurrentListView();
                    if (ProfileActivity.this.sharedMediaLayout.getTop() == 0) {
                        iArr[1] = i4;
                        currentListView.scrollBy(0, i4);
                    }
                }
                if (i2 == 0 || i5 != 0) {
                    return;
                }
                ProfileActivity profileActivity = ProfileActivity.this;
                if (i2 <= 0) {
                    z = false;
                }
                profileActivity.hideFloatingButton(z);
            } catch (Throwable th) {
                FileLog.e(th);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$NestedFrameLayout$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.NestedFrameLayout.this.lambda$onNestedScroll$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNestedScroll$0() {
            try {
                RecyclerListView currentListView = ProfileActivity.this.sharedMediaLayout.getCurrentListView();
                if (currentListView == null || currentListView.getAdapter() == null) {
                    return;
                }
                currentListView.getAdapter().notifyDataSetChanged();
            } catch (Throwable unused) {
            }
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public boolean onNestedPreFling(View view, float f, float f2) {
            return super.onNestedPreFling(view, f, f2);
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
            if (view == ProfileActivity.this.listView) {
                if (ProfileActivity.this.sharedMediaRow == -1 || !ProfileActivity.this.sharedMediaLayoutAttached) {
                    return;
                }
                boolean isSearchFieldVisible = ((BaseFragment) ProfileActivity.this).actionBar.isSearchFieldVisible();
                int top = ProfileActivity.this.sharedMediaLayout.getTop();
                boolean z = false;
                if (i2 >= 0) {
                    if (isSearchFieldVisible) {
                        RecyclerListView currentListView = ProfileActivity.this.sharedMediaLayout.getCurrentListView();
                        iArr[1] = i2;
                        if (top > 0) {
                            iArr[1] = iArr[1] - i2;
                        }
                        if (iArr[1] > 0) {
                            currentListView.scrollBy(0, iArr[1]);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (top <= 0) {
                    RecyclerListView currentListView2 = ProfileActivity.this.sharedMediaLayout.getCurrentListView();
                    int findFirstVisibleItemPosition = ((LinearLayoutManager) currentListView2.getLayoutManager()).findFirstVisibleItemPosition();
                    if (findFirstVisibleItemPosition != -1) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = currentListView2.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                        int top2 = findViewHolderForAdapterPosition != null ? findViewHolderForAdapterPosition.itemView.getTop() : -1;
                        int paddingTop = currentListView2.getPaddingTop();
                        if (top2 != paddingTop || findFirstVisibleItemPosition != 0) {
                            iArr[1] = findFirstVisibleItemPosition != 0 ? i2 : Math.max(i2, top2 - paddingTop);
                            currentListView2.scrollBy(0, i2);
                            z = true;
                        }
                    }
                }
                if (isSearchFieldVisible) {
                    if (!z && top < 0) {
                        iArr[1] = i2 - Math.max(top, i2);
                    } else {
                        iArr[1] = i2;
                    }
                }
            }
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
            return ProfileActivity.this.sharedMediaRow != -1 && i == 2;
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
            this.nestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onStopNestedScroll(View view, int i) {
            this.nestedScrollingParentHelper.onStopNestedScroll(view);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        public void drawList(Canvas canvas, boolean z) {
            super.drawList(canvas, z);
            canvas.save();
            canvas.translate(0.0f, ProfileActivity.this.listView.getY());
            ProfileActivity.this.sharedMediaLayout.drawListForBlur(canvas);
            canvas.restore();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PagerIndicatorView extends View {
        private final PagerAdapter adapter;
        private final ValueAnimator animator;
        private final float[] animatorValues;
        private final Paint backgroundPaint;
        private final RectF indicatorRect;
        private boolean isIndicatorVisible;
        private final TextPaint textPaint;

        public PagerIndicatorView(Context context) {
            super(context);
            this.indicatorRect = new RectF();
            this.animatorValues = new float[]{0.0f, 1.0f};
            PagerAdapter adapter = ProfileActivity.this.avatarsViewPager.getAdapter();
            this.adapter = adapter;
            setVisibility(8);
            TextPaint textPaint = new TextPaint(1);
            this.textPaint = textPaint;
            textPaint.setColor(-1);
            textPaint.setTypeface(Typeface.SANS_SERIF);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(AndroidUtilities.dpf2(15.0f));
            Paint paint = new Paint(1);
            this.backgroundPaint = paint;
            paint.setColor(637534208);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.animator = ofFloat;
            ofFloat.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$PagerIndicatorView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ProfileActivity.PagerIndicatorView.this.lambda$new$0(valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter(ProfileActivity.this, ProfileActivity.this.expandPhoto) { // from class: org.telegram.ui.ProfileActivity.PagerIndicatorView.1
                final /* synthetic */ boolean val$expanded;

                {
                    this.val$expanded = r3;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PagerIndicatorView.this.isIndicatorVisible) {
                        if (ProfileActivity.this.searchItem != null) {
                            ProfileActivity.this.searchItem.setClickable(false);
                        }
                        if (ProfileActivity.this.editItemVisible) {
                            ProfileActivity.this.editItem.setVisibility(8);
                        }
                        if (ProfileActivity.this.callItemVisible) {
                            ProfileActivity.this.callItem.setVisibility(8);
                        }
                        if (ProfileActivity.this.videoCallItemVisible) {
                            ProfileActivity.this.videoCallItem.setVisibility(8);
                        }
                    } else {
                        PagerIndicatorView.this.setVisibility(8);
                    }
                    ProfileActivity.this.updateStoriesViewBounds(false);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (ProfileActivity.this.searchItem != null && !this.val$expanded) {
                        ProfileActivity.this.searchItem.setClickable(true);
                    }
                    if (ProfileActivity.this.editItemVisible) {
                        ProfileActivity.this.editItem.setVisibility(0);
                    }
                    if (ProfileActivity.this.callItemVisible) {
                        ProfileActivity.this.callItem.setVisibility(0);
                    }
                    if (ProfileActivity.this.videoCallItemVisible) {
                        ProfileActivity.this.videoCallItem.setVisibility(0);
                    }
                    PagerIndicatorView.this.setVisibility(0);
                    ProfileActivity.this.updateStoriesViewBounds(false);
                }
            });
            ProfileActivity.this.avatarsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(ProfileActivity.this) { // from class: org.telegram.ui.ProfileActivity.PagerIndicatorView.2
                private int prevPage;

                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageScrollStateChanged(int i) {
                }

                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageScrolled(int i, float f, int i2) {
                }

                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageSelected(int i) {
                    int realPosition = ProfileActivity.this.avatarsViewPager.getRealPosition(i);
                    PagerIndicatorView.this.invalidateIndicatorRect(this.prevPage != realPosition);
                    this.prevPage = realPosition;
                    PagerIndicatorView.this.updateAvatarItems();
                }
            });
            adapter.registerDataSetObserver(new DataSetObserver(ProfileActivity.this) { // from class: org.telegram.ui.ProfileActivity.PagerIndicatorView.3
                @Override // android.database.DataSetObserver
                public void onChanged() {
                    int realCount = ProfileActivity.this.avatarsViewPager.getRealCount();
                    if (ProfileActivity.this.overlayCountVisible == 0 && realCount > 1 && realCount <= 20 && ProfileActivity.this.overlaysView.isOverlaysVisible()) {
                        ProfileActivity.this.overlayCountVisible = 1;
                    }
                    PagerIndicatorView.this.invalidateIndicatorRect(false);
                    PagerIndicatorView.this.refreshVisibility(1.0f);
                    PagerIndicatorView.this.updateAvatarItems();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
            float lerp = AndroidUtilities.lerp(this.animatorValues, valueAnimator.getAnimatedFraction());
            if (ProfileActivity.this.searchItem != null && !ProfileActivity.this.isPulledDown) {
                float f = 1.0f - lerp;
                ProfileActivity.this.searchItem.setScaleX(f);
                ProfileActivity.this.searchItem.setScaleY(f);
                ProfileActivity.this.searchItem.setAlpha(f);
            }
            if (ProfileActivity.this.editItemVisible) {
                float f2 = 1.0f - lerp;
                ProfileActivity.this.editItem.setScaleX(f2);
                ProfileActivity.this.editItem.setScaleY(f2);
                ProfileActivity.this.editItem.setAlpha(f2);
            }
            if (ProfileActivity.this.callItemVisible) {
                float f3 = 1.0f - lerp;
                ProfileActivity.this.callItem.setScaleX(f3);
                ProfileActivity.this.callItem.setScaleY(f3);
                ProfileActivity.this.callItem.setAlpha(f3);
            }
            if (ProfileActivity.this.videoCallItemVisible) {
                float f4 = 1.0f - lerp;
                ProfileActivity.this.videoCallItem.setScaleX(f4);
                ProfileActivity.this.videoCallItem.setScaleY(f4);
                ProfileActivity.this.videoCallItem.setAlpha(f4);
            }
            setScaleX(lerp);
            setScaleY(lerp);
            setAlpha(lerp);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAvatarItemsInternal() {
            if (ProfileActivity.this.otherItem == null || ProfileActivity.this.avatarsViewPager == null || !ProfileActivity.this.isPulledDown) {
                return;
            }
            if (ProfileActivity.this.avatarsViewPager.getRealPosition() == 0) {
                ProfileActivity.this.otherItem.hideSubItem(33);
                ProfileActivity.this.otherItem.showSubItem(36);
                return;
            }
            ProfileActivity.this.otherItem.showSubItem(33);
            ProfileActivity.this.otherItem.hideSubItem(36);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAvatarItems() {
            if (ProfileActivity.this.imageUpdater == null) {
                return;
            }
            if (ProfileActivity.this.otherItem.isSubMenuShowing()) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$PagerIndicatorView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.PagerIndicatorView.this.updateAvatarItemsInternal();
                    }
                }, 500L);
            } else {
                updateAvatarItemsInternal();
            }
        }

        public boolean isIndicatorFullyVisible() {
            return this.isIndicatorVisible && !this.animator.isRunning();
        }

        public void setIndicatorVisible(boolean z, float f) {
            if (z != this.isIndicatorVisible) {
                this.isIndicatorVisible = z;
                this.animator.cancel();
                float lerp = AndroidUtilities.lerp(this.animatorValues, this.animator.getAnimatedFraction());
                if (f <= 0.0f) {
                    this.animator.setDuration(0L);
                } else if (z) {
                    this.animator.setDuration(((1.0f - lerp) * 250.0f) / f);
                } else {
                    this.animator.setDuration((250.0f * lerp) / f);
                }
                float[] fArr = this.animatorValues;
                fArr[0] = lerp;
                fArr[1] = z ? 1.0f : 0.0f;
                this.animator.start();
            }
        }

        public void refreshVisibility(float f) {
            setIndicatorVisible(ProfileActivity.this.isPulledDown && ProfileActivity.this.avatarsViewPager.getRealCount() > 20, f);
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            invalidateIndicatorRect(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invalidateIndicatorRect(boolean z) {
            if (z) {
                ProfileActivity.this.overlaysView.saveCurrentPageProgress();
            }
            ProfileActivity.this.overlaysView.invalidate();
            float measureText = this.textPaint.measureText(getCurrentTitle());
            this.indicatorRect.right = (getMeasuredWidth() - AndroidUtilities.dp(54.0f)) - (ProfileActivity.this.qrItem != null ? AndroidUtilities.dp(48.0f) : 0);
            RectF rectF = this.indicatorRect;
            rectF.left = rectF.right - (measureText + AndroidUtilities.dpf2(16.0f));
            this.indicatorRect.top = (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.dp(15.0f);
            RectF rectF2 = this.indicatorRect;
            rectF2.bottom = rectF2.top + AndroidUtilities.dp(26.0f);
            setPivotX(this.indicatorRect.centerX());
            setPivotY(this.indicatorRect.centerY());
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float dpf2 = AndroidUtilities.dpf2(12.0f);
            canvas.drawRoundRect(this.indicatorRect, dpf2, dpf2, this.backgroundPaint);
            canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
        }

        private String getCurrentTitle() {
            return this.adapter.getPageTitle(ProfileActivity.this.avatarsViewPager.getCurrentItem()).toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ActionBarMenuItem getSecondaryMenuItem() {
            if (ProfileActivity.this.callItemVisible) {
                return ProfileActivity.this.callItem;
            }
            if (ProfileActivity.this.editItemVisible) {
                return ProfileActivity.this.editItem;
            }
            if (ProfileActivity.this.searchItem != null) {
                return ProfileActivity.this.searchItem;
            }
            return null;
        }
    }

    public ProfileActivity(Bundle bundle) {
        this(bundle, null);
    }

    public ProfileActivity(Bundle bundle, SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader) {
        super(bundle);
        this.nameTextView = new SimpleTextView[2];
        this.nameTextViewRightDrawableContentDescription = null;
        this.onlineTextView = new SimpleTextView[4];
        this.verifiedDrawable = new Drawable[2];
        this.premiumStarDrawable = new Drawable[2];
        this.emojiStatusDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[2];
        this.verifiedCheckDrawable = new Drawable[2];
        this.verifiedCrossfadeDrawable = new CrossfadeDrawable[2];
        this.premiumCrossfadeDrawable = new CrossfadeDrawable[2];
        this.scrimView = null;
        this.scrimPaint = new Paint(1) { // from class: org.telegram.ui.ProfileActivity.1
            @Override // android.graphics.Paint
            public void setAlpha(int i) {
                super.setAlpha(i);
                ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
            }
        };
        this.actionBarBackgroundPaint = new Paint(1);
        this.isOnline = new boolean[1];
        this.headerShadowAlpha = 1.0f;
        this.participantsMap = new LongSparseArray<>();
        this.allowProfileAnimation = true;
        this.disableProfileAnimation = false;
        this.positionToOffset = new HashMap<>();
        this.expandAnimatorValues = new float[]{0.0f, 1.0f};
        this.whitePaint = new Paint();
        this.onlineCount = -1;
        this.rect = new Rect();
        this.visibleChatParticipants = new ArrayList<>();
        this.visibleSortedUsers = new ArrayList<>();
        this.usersForceShowingIn = 0;
        this.firstLayout = true;
        this.invalidateScroll = true;
        this.isQrItemVisible = true;
        this.actionBarAnimationColorFrom = 0;
        this.navigationBarAnimationColorFrom = 0;
        this.reportReactionMessageId = 0;
        this.reportReactionFromDialogId = 0L;
        this.notificationsExceptionTopics = new HashSet<>();
        this.HEADER_SHADOW = new AnimationProperties.FloatProperty<ProfileActivity>("headerShadow") { // from class: org.telegram.ui.ProfileActivity.2
            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(ProfileActivity profileActivity, float f) {
                ProfileActivity profileActivity2 = ProfileActivity.this;
                profileActivity2.headerShadowAlpha = f;
                profileActivity2.topView.invalidate();
            }

            @Override // android.util.Property
            public Float get(ProfileActivity profileActivity) {
                return Float.valueOf(ProfileActivity.this.headerShadowAlpha);
            }
        };
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.ProfileActivity.3
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
                TLRPC$Chat chat;
                TLRPC$ChatPhoto tLRPC$ChatPhoto;
                TLRPC$FileLocation tLRPC$FileLocation2;
                TLRPC$User user;
                TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
                if (tLRPC$FileLocation == null) {
                    return null;
                }
                if (ProfileActivity.this.userId == 0 ? ProfileActivity.this.chatId == 0 || (chat = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(ProfileActivity.this.chatId))) == null || (tLRPC$ChatPhoto = chat.photo) == null || (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_big) == null : (user = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId))) == null || (tLRPC$UserProfilePhoto = user.photo) == null || (tLRPC$FileLocation2 = tLRPC$UserProfilePhoto.photo_big) == null) {
                    tLRPC$FileLocation2 = null;
                }
                if (tLRPC$FileLocation2 != null && tLRPC$FileLocation2.local_id == tLRPC$FileLocation.local_id && tLRPC$FileLocation2.volume_id == tLRPC$FileLocation.volume_id && tLRPC$FileLocation2.dc_id == tLRPC$FileLocation.dc_id) {
                    int[] iArr = new int[2];
                    ProfileActivity.this.avatarImage.getLocationInWindow(iArr);
                    PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                    placeProviderObject.viewX = iArr[0];
                    placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
                    placeProviderObject.parentView = ProfileActivity.this.avatarImage;
                    placeProviderObject.imageReceiver = ProfileActivity.this.avatarImage.getImageReceiver();
                    if (ProfileActivity.this.userId != 0) {
                        placeProviderObject.dialogId = ProfileActivity.this.userId;
                    } else if (ProfileActivity.this.chatId != 0) {
                        placeProviderObject.dialogId = -ProfileActivity.this.chatId;
                    }
                    placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
                    placeProviderObject.size = -1L;
                    placeProviderObject.radius = ProfileActivity.this.avatarImage.getImageReceiver().getRoundRadius();
                    placeProviderObject.scale = ProfileActivity.this.avatarContainer.getScaleX();
                    placeProviderObject.canEdit = ProfileActivity.this.userId == ProfileActivity.this.getUserConfig().clientUserId;
                    return placeProviderObject;
                }
                return null;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void willHidePhotoViewer() {
                ProfileActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void openPhotoForEdit(String str, String str2, boolean z) {
                ProfileActivity.this.imageUpdater.openPhotoForEdit(str, str2, 0, z);
            }
        };
        this.floatingInterpolator = new AccelerateDecelerateInterpolator();
        this.ACTIONBAR_HEADER_PROGRESS = new AnimationProperties.FloatProperty<ActionBar>("avatarAnimationProgress") { // from class: org.telegram.ui.ProfileActivity.41
            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(ActionBar actionBar, float f) {
                int themedColor;
                ProfileActivity.this.mediaHeaderAnimationProgress = f;
                if (ProfileActivity.this.storyView != null) {
                    ProfileActivity.this.storyView.setActionBarActionMode(f);
                }
                ProfileActivity.this.topView.invalidate();
                int themedColor2 = ProfileActivity.this.getThemedColor(Theme.key_profile_title);
                ProfileActivity profileActivity = ProfileActivity.this;
                int i = Theme.key_player_actionBarTitle;
                int themedColor3 = profileActivity.getThemedColor(i);
                int offsetColor = AndroidUtilities.getOffsetColor(themedColor2, themedColor3, f, 1.0f);
                ProfileActivity.this.nameTextView[1].setTextColor(offsetColor);
                if (ProfileActivity.this.lockIconDrawable != null) {
                    ProfileActivity.this.lockIconDrawable.setColorFilter(offsetColor, PorterDuff.Mode.MULTIPLY);
                }
                if (ProfileActivity.this.scamDrawable != null) {
                    ProfileActivity.this.scamDrawable.setColor(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor(Theme.key_avatar_subtitleInProfileBlue), themedColor3, f, 1.0f));
                }
                ((BaseFragment) ProfileActivity.this).actionBar.setItemsColor(AndroidUtilities.getOffsetColor(ProfileActivity.this.peerColor != null ? -1 : ProfileActivity.this.getThemedColor(Theme.key_actionBarDefaultIcon), ProfileActivity.this.getThemedColor(Theme.key_actionBarActionModeDefaultIcon), f, 1.0f), false);
                if (ProfileActivity.this.peerColor != null) {
                    themedColor = 1090519039;
                } else {
                    themedColor = ProfileActivity.this.peerColor != null ? 553648127 : ProfileActivity.this.getThemedColor(Theme.key_avatar_actionBarSelectorBlue);
                }
                ((BaseFragment) ProfileActivity.this).actionBar.setItemsBackgroundColor(AndroidUtilities.getOffsetColor(themedColor, ProfileActivity.this.getThemedColor(Theme.key_actionBarActionModeDefaultSelector), f, 1.0f), false);
                ProfileActivity.this.topView.invalidate();
                ProfileActivity.this.otherItem.setIconColor(ProfileActivity.this.peerColor != null ? -1 : ProfileActivity.this.getThemedColor(Theme.key_actionBarDefaultIcon));
                ProfileActivity.this.callItem.setIconColor(ProfileActivity.this.peerColor != null ? -1 : ProfileActivity.this.getThemedColor(Theme.key_actionBarDefaultIcon));
                ProfileActivity.this.videoCallItem.setIconColor(ProfileActivity.this.peerColor != null ? -1 : ProfileActivity.this.getThemedColor(Theme.key_actionBarDefaultIcon));
                ProfileActivity.this.editItem.setIconColor(ProfileActivity.this.peerColor == null ? ProfileActivity.this.getThemedColor(Theme.key_actionBarDefaultIcon) : -1);
                if (ProfileActivity.this.verifiedDrawable[0] != null) {
                    ProfileActivity.this.verifiedDrawable[0].setColorFilter(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor(Theme.key_profile_verifiedBackground), ProfileActivity.this.getThemedColor(i), f, 1.0f), PorterDuff.Mode.MULTIPLY);
                }
                if (ProfileActivity.this.verifiedDrawable[1] != null) {
                    ProfileActivity profileActivity2 = ProfileActivity.this;
                    ProfileActivity.this.verifiedDrawable[1].setColorFilter(AndroidUtilities.getOffsetColor(profileActivity2.applyPeerColor(profileActivity2.getThemedColor(Theme.key_profile_verifiedBackground)), ProfileActivity.this.getThemedColor(i), f, 1.0f), PorterDuff.Mode.MULTIPLY);
                }
                if (ProfileActivity.this.verifiedCheckDrawable[0] != null) {
                    ProfileActivity.this.verifiedCheckDrawable[0].setColorFilter(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor(Theme.key_profile_verifiedCheck), ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhite), f, 1.0f), PorterDuff.Mode.MULTIPLY);
                }
                if (ProfileActivity.this.verifiedCheckDrawable[1] != null) {
                    ProfileActivity profileActivity3 = ProfileActivity.this;
                    ProfileActivity.this.verifiedCheckDrawable[1].setColorFilter(AndroidUtilities.getOffsetColor(profileActivity3.applyPeerColor(profileActivity3.getThemedColor(Theme.key_profile_verifiedCheck)), ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhite), f, 1.0f), PorterDuff.Mode.MULTIPLY);
                }
                if (ProfileActivity.this.premiumStarDrawable[0] != null) {
                    ProfileActivity.this.premiumStarDrawable[0].setColorFilter(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor(Theme.key_profile_verifiedBackground), ProfileActivity.this.getThemedColor(i), f, 1.0f), PorterDuff.Mode.MULTIPLY);
                }
                if (ProfileActivity.this.premiumStarDrawable[1] != null) {
                    ProfileActivity profileActivity4 = ProfileActivity.this;
                    int applyPeerColor = profileActivity4.applyPeerColor(profileActivity4.getThemedColor(Theme.key_profile_verifiedBackground));
                    ProfileActivity profileActivity5 = ProfileActivity.this;
                    ProfileActivity.this.premiumStarDrawable[1].setColorFilter(AndroidUtilities.getOffsetColor(applyPeerColor, profileActivity5.applyPeerColor(profileActivity5.getThemedColor(i)), f, 1.0f), PorterDuff.Mode.MULTIPLY);
                }
                ProfileActivity.this.updateEmojiStatusDrawableColor();
                if (ProfileActivity.this.avatarsViewPagerIndicatorView.getSecondaryMenuItem() != null) {
                    if (ProfileActivity.this.videoCallItemVisible || ProfileActivity.this.editItemVisible || ProfileActivity.this.callItemVisible) {
                        ProfileActivity profileActivity6 = ProfileActivity.this;
                        profileActivity6.needLayoutText(Math.min(1.0f, profileActivity6.extraHeight / AndroidUtilities.dp(88.0f)));
                    }
                }
            }

            @Override // android.util.Property
            public Float get(ActionBar actionBar) {
                return Float.valueOf(ProfileActivity.this.mediaHeaderAnimationProgress);
            }
        };
        this.scrimAnimatorSet = null;
        this.savedScrollPosition = -1;
        this.photoDescriptionProgress = -1.0f;
        this.sharedMediaPreloader = sharedMediaPreloader;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.userId = this.arguments.getLong("user_id", 0L);
        this.chatId = this.arguments.getLong("chat_id", 0L);
        this.topicId = this.arguments.getInt("topic_id", 0);
        this.openSimilar = this.arguments.getBoolean("similar", false);
        this.isTopic = this.topicId != 0;
        this.banFromGroup = this.arguments.getLong("ban_chat_id", 0L);
        this.reportReactionMessageId = this.arguments.getInt("report_reaction_message_id", 0);
        this.reportReactionFromDialogId = this.arguments.getLong("report_reaction_from_dialog_id", 0L);
        this.showAddToContacts = this.arguments.getBoolean("show_add_to_contacts", true);
        this.vcardPhone = PhoneFormat.stripExceptNumbers(this.arguments.getString("vcard_phone"));
        this.vcardFirstName = this.arguments.getString("vcard_first_name");
        this.vcardLastName = this.arguments.getString("vcard_last_name");
        this.reportSpam = this.arguments.getBoolean("reportSpam", false);
        if (!this.expandPhoto) {
            boolean z = this.arguments.getBoolean("expandPhoto", false);
            this.expandPhoto = z;
            if (z) {
                this.currentExpandAnimatorValue = 1.0f;
                this.needSendMessage = true;
            }
        }
        if (this.userId != 0) {
            long j = this.arguments.getLong("dialog_id", 0L);
            this.dialogId = j;
            if (j != 0) {
                this.currentEncryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(this.dialogId)));
            }
            FlagSecureReason flagSecureReason = this.flagSecure;
            if (flagSecureReason != null) {
                flagSecureReason.invalidate();
            }
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user == null) {
                return false;
            }
            getNotificationCenter().addObserver(this, NotificationCenter.contactsDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.newSuggestionsAvailable);
            getNotificationCenter().addObserver(this, NotificationCenter.encryptedChatCreated);
            getNotificationCenter().addObserver(this, NotificationCenter.encryptedChatUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.blockedUsersDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.botInfoDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.userInfoDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.privacyRulesUpdated);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
            this.userBlocked = getMessagesController().blockePeers.indexOfKey(this.userId) >= 0;
            if (user.bot) {
                this.isBot = true;
                MediaDataController mediaDataController = getMediaDataController();
                long j2 = user.id;
                mediaDataController.loadBotInfo(j2, j2, true, this.classGuid);
            }
            this.userInfo = getMessagesController().getUserFull(this.userId);
            getMessagesController().loadFullUser(getMessagesController().getUser(Long.valueOf(this.userId)), this.classGuid, true);
            this.participantsMap = null;
            if (UserObject.isUserSelf(user)) {
                ImageUpdater imageUpdater = new ImageUpdater(true, 0, true);
                this.imageUpdater = imageUpdater;
                imageUpdater.setOpenWithFrontfaceCamera(true);
                ImageUpdater imageUpdater2 = this.imageUpdater;
                imageUpdater2.parentFragment = this;
                imageUpdater2.setDelegate(this);
                getMediaDataController().checkFeaturedStickers();
                getMessagesController().loadSuggestedFilters();
                getMessagesController().loadUserInfo(getUserConfig().getCurrentUser(), true, this.classGuid);
            }
            this.actionBarAnimationColorFrom = this.arguments.getInt("actionBarColor", 0);
        } else if (this.chatId == 0) {
            return false;
        } else {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            this.currentChat = chat;
            if (chat == null) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda39
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.this.lambda$onFragmentCreate$0(countDownLatch);
                    }
                });
                try {
                    countDownLatch.await();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                if (this.currentChat == null) {
                    return false;
                }
                getMessagesController().putChat(this.currentChat, true);
            }
            FlagSecureReason flagSecureReason2 = this.flagSecure;
            if (flagSecureReason2 != null) {
                flagSecureReason2.invalidate();
            }
            if (this.currentChat.megagroup) {
                getChannelParticipants(true);
            } else {
                this.participantsMap = null;
            }
            getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.chatOnlineCountDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.groupCallUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.channelRightsUpdated);
            this.sortedUsers = new ArrayList<>();
            updateOnlineCount(true);
            if (this.chatInfo == null) {
                this.chatInfo = getMessagesController().getChatFull(this.chatId);
            }
            if (ChatObject.isChannel(this.currentChat)) {
                getMessagesController().loadFullChat(this.chatId, this.classGuid, true);
            } else if (this.chatInfo == null) {
                this.chatInfo = getMessagesStorage().loadChatInfo(this.chatId, false, null, false, false);
            }
            updateExceptions();
        }
        if (this.sharedMediaPreloader == null) {
            this.sharedMediaPreloader = new SharedMediaLayout.SharedMediaPreloader(this);
        }
        this.sharedMediaPreloader.addDelegate(this);
        getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
        getNotificationCenter().addObserver(this, NotificationCenter.didReceiveNewMessages);
        getNotificationCenter().addObserver(this, NotificationCenter.closeChats);
        getNotificationCenter().addObserver(this, NotificationCenter.topicsDidLoaded);
        getNotificationCenter().addObserver(this, NotificationCenter.updateSearchSettings);
        getNotificationCenter().addObserver(this, NotificationCenter.reloadDialogPhotos);
        getNotificationCenter().addObserver(this, NotificationCenter.storiesUpdated);
        getNotificationCenter().addObserver(this, NotificationCenter.storiesReadUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        updateRowsIds();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        if (this.arguments.containsKey("preload_messages")) {
            getMessagesController().ensureMessagesLoaded(this.userId, 0, null);
        }
        if (this.userId != 0 && UserObject.isUserSelf(getMessagesController().getUser(Long.valueOf(this.userId)))) {
            getConnectionsManager().sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda58
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    ProfileActivity.this.lambda$onFragmentCreate$1(tLObject, tLRPC$TL_error);
                }
            });
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFragmentCreate$0(CountDownLatch countDownLatch) {
        this.currentChat = getMessagesStorage().getChat(this.chatId);
        countDownLatch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFragmentCreate$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$TL_account_password) {
            this.currentPassword = (TLRPC$TL_account_password) tLObject;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExceptions() {
        if (this.isTopic || !ChatObject.isForum(this.currentChat)) {
            return;
        }
        getNotificationsController().loadTopicsNotificationsExceptions(-this.chatId, new Consumer() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda49
            @Override // j$.util.function.Consumer
            public final void accept(Object obj) {
                ProfileActivity.this.lambda$updateExceptions$2((HashSet) obj);
            }

            @Override // j$.util.function.Consumer
            public /* synthetic */ Consumer andThen(Consumer consumer) {
                return Objects.requireNonNull(consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateExceptions$2(HashSet hashSet) {
        ListAdapter listAdapter;
        ArrayList arrayList = new ArrayList(hashSet);
        int i = 0;
        while (i < arrayList.size()) {
            if (getMessagesController().getTopicsController().findTopic(this.chatId, ((Integer) arrayList.get(i)).intValue()) == null) {
                arrayList.remove(i);
                i--;
            }
            i++;
        }
        this.notificationsExceptionTopics.clear();
        this.notificationsExceptionTopics.addAll(arrayList);
        int i2 = this.notificationsRow;
        if (i2 < 0 || (listAdapter = this.listAdapter) == null) {
            return;
        }
        listAdapter.notifyItemChanged(i2);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            sharedMediaLayout.onDestroy();
        }
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
        if (sharedMediaPreloader != null) {
            sharedMediaPreloader.onDestroy(this);
        }
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader2 = this.sharedMediaPreloader;
        if (sharedMediaPreloader2 != null) {
            sharedMediaPreloader2.removeDelegate(this);
        }
        getNotificationCenter().removeObserver(this, NotificationCenter.updateInterfaces);
        getNotificationCenter().removeObserver(this, NotificationCenter.closeChats);
        getNotificationCenter().removeObserver(this, NotificationCenter.didReceiveNewMessages);
        getNotificationCenter().removeObserver(this, NotificationCenter.topicsDidLoaded);
        getNotificationCenter().removeObserver(this, NotificationCenter.updateSearchSettings);
        getNotificationCenter().removeObserver(this, NotificationCenter.reloadDialogPhotos);
        getNotificationCenter().removeObserver(this, NotificationCenter.storiesUpdated);
        getNotificationCenter().removeObserver(this, NotificationCenter.storiesReadUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        if (profileGalleryView != null) {
            profileGalleryView.onDestroy();
        }
        if (this.userId != 0) {
            getNotificationCenter().removeObserver(this, NotificationCenter.newSuggestionsAvailable);
            getNotificationCenter().removeObserver(this, NotificationCenter.contactsDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.encryptedChatCreated);
            getNotificationCenter().removeObserver(this, NotificationCenter.encryptedChatUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.blockedUsersDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.botInfoDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.userInfoDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.privacyRulesUpdated);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
            getMessagesController().cancelLoadFullUser(this.userId);
        } else if (this.chatId != 0) {
            getNotificationCenter().removeObserver(this, NotificationCenter.chatInfoDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.chatOnlineCountDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.groupCallUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.channelRightsUpdated);
        }
        AvatarImageView avatarImageView = this.avatarImage;
        if (avatarImageView != null) {
            avatarImageView.setImageDrawable(null);
        }
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.clear();
        }
        PinchToZoomHelper pinchToZoomHelper = this.pinchToZoomHelper;
        if (pinchToZoomHelper != null) {
            pinchToZoomHelper.clear();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ActionBar createActionBar(Context context) {
        ChatActivity.ThemeDelegate themeDelegate;
        BaseFragment lastFragment = this.parentLayout.getLastFragment();
        if ((lastFragment instanceof ChatActivity) && (themeDelegate = ((ChatActivity) lastFragment).themeDelegate) != null && themeDelegate.getCurrentTheme() != null) {
            this.resourcesProvider = lastFragment.getResourceProvider();
        }
        ActionBar actionBar = new ActionBar(context, this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.4
            @Override // org.telegram.ui.ActionBar.ActionBar, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                ProfileActivity.this.avatarContainer.getHitRect(ProfileActivity.this.rect);
                if (ProfileActivity.this.rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            public void setItemsColor(int i, boolean z) {
                super.setItemsColor(i, z);
                if (z || ProfileActivity.this.ttlIconView == null) {
                    return;
                }
                ProfileActivity.this.ttlIconView.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.ActionBar.ActionBar, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                ProfileActivity.this.updateStoriesViewBounds(false);
            }
        };
        actionBar.setColorFilterMode(PorterDuff.Mode.SRC_IN);
        boolean z = true;
        actionBar.setForceSkipTouches(true);
        actionBar.setBackgroundColor(0);
        actionBar.setItemsBackgroundColor(this.peerColor != null ? 553648127 : getThemedColor(Theme.key_avatar_actionBarSelectorBlue), false);
        actionBar.setItemsColor(getThemedColor(Theme.key_actionBarDefaultIcon), false);
        actionBar.setBackButtonDrawable(new BackDrawable(false));
        actionBar.setCastShadows(false);
        actionBar.setAddToContainer(false);
        actionBar.setClipContent(true);
        actionBar.setOccupyStatusBar((Build.VERSION.SDK_INT < 21 || AndroidUtilities.isTablet() || this.inBubbleMode) ? false : false);
        final ImageView backButton = actionBar.getBackButton();
        backButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda23
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$createActionBar$4;
                lambda$createActionBar$4 = ProfileActivity.this.lambda$createActionBar$4(backButton, view);
                return lambda$createActionBar$4;
            }
        });
        return actionBar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createActionBar$4(ImageView imageView, View view) {
        ActionBarPopupWindow show = BackButtonMenu.show(this, imageView, getDialogId(), getTopicId(), this.resourcesProvider);
        if (show != null) {
            show.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda24
                @Override // android.widget.PopupWindow.OnDismissListener
                public final void onDismiss() {
                    ProfileActivity.this.lambda$createActionBar$3();
                }
            });
            dimBehindView(imageView, 0.3f);
            UndoView undoView = this.undoView;
            if (undoView != null) {
                undoView.hide(true, 1);
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createActionBar$3() {
        dimBehindView(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setParentLayout(INavigationLayout iNavigationLayout) {
        super.setParentLayout(iNavigationLayout);
        FlagSecureReason flagSecureReason = this.flagSecure;
        if (flagSecureReason != null) {
            flagSecureReason.detach();
            this.flagSecure = null;
        }
        if (iNavigationLayout == null || iNavigationLayout.getParentActivity() == null) {
            return;
        }
        this.flagSecure = new FlagSecureReason(iNavigationLayout.getParentActivity().getWindow(), new FlagSecureReason.FlagSecureCondition() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda51
            @Override // org.telegram.messenger.FlagSecureReason.FlagSecureCondition
            public final boolean run() {
                boolean lambda$setParentLayout$5;
                lambda$setParentLayout$5 = ProfileActivity.this.lambda$setParentLayout$5();
                return lambda$setParentLayout$5;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setParentLayout$5() {
        return this.currentEncryptedChat != null || getMessagesController().isChatNoForwards(this.currentChat);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v2 */
    /* JADX WARN: Type inference failed for: r11v3, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r11v5 */
    /* JADX WARN: Type inference failed for: r11v6 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v4, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r13v6 */
    /* JADX WARN: Type inference failed for: r13v7 */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        ?? r11;
        ?? r13;
        int i;
        Object obj;
        boolean z;
        float f;
        float f2;
        float f3;
        ChatAvatarContainer avatarContainer;
        TLRPC$UserFull tLRPC$UserFull;
        TLRPC$ChatParticipants tLRPC$ChatParticipants;
        ChatActivity.ThemeDelegate themeDelegate;
        Theme.createProfileResources(context);
        Theme.createChatResources(context, false);
        BaseFragment lastFragment = this.parentLayout.getLastFragment();
        if ((lastFragment instanceof ChatActivity) && (themeDelegate = ((ChatActivity) lastFragment).themeDelegate) != null && themeDelegate.getCurrentTheme() != null) {
            this.resourcesProvider = lastFragment.getResourceProvider();
        }
        this.searchTransitionOffset = 0;
        this.searchTransitionProgress = 1.0f;
        this.searchMode = false;
        this.hasOwnBackground = true;
        this.extraHeight = AndroidUtilities.dp(88.0f);
        this.actionBar.setActionBarMenuOnItemClick(new 5());
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            sharedMediaLayout.onDestroy();
        }
        long j = this.dialogId;
        if (j == 0) {
            j = this.userId;
            if (j == 0) {
                j = -this.chatId;
            }
        }
        final long j2 = j;
        this.fragmentView = new 6(context);
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        ArrayList<Integer> arrayList = (tLRPC$ChatFull == null || (tLRPC$ChatParticipants = tLRPC$ChatFull.participants) == null || tLRPC$ChatParticipants.participants.size() <= 5) ? null : this.sortedUsers;
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
        TLRPC$UserFull tLRPC$UserFull2 = this.userInfo;
        SharedMediaLayout sharedMediaLayout2 = new SharedMediaLayout(context, j2, sharedMediaPreloader, tLRPC$UserFull2 != null ? tLRPC$UserFull2.common_chats_count : 0, this.sortedUsers, this.chatInfo, tLRPC$UserFull2, this.openSimilar ? 10 : arrayList != null ? 7 : -1, this, this, 1, this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.7
            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected int getInitialTab() {
                return 8;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected int processColor(int i2) {
                return ProfileActivity.this.applyPeerColor(i2, false);
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void onSelectedTabChanged() {
                ProfileActivity.this.updateSelectedMediaTabText();
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected boolean canShowSearchItem() {
                return ProfileActivity.this.mediaHeaderVisible;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void onSearchStateChanged(boolean z2) {
                AndroidUtilities.removeAdjustResize(ProfileActivity.this.getParentActivity(), ((BaseFragment) ProfileActivity.this).classGuid);
                ProfileActivity.this.listView.stopScroll();
                ProfileActivity.this.avatarContainer2.setPivotY(ProfileActivity.this.avatarContainer.getPivotY() + (ProfileActivity.this.avatarContainer.getMeasuredHeight() / 2.0f));
                ProfileActivity.this.avatarContainer2.setPivotX(ProfileActivity.this.avatarContainer2.getMeasuredWidth() / 2.0f);
                AndroidUtilities.updateViewVisibilityAnimated(ProfileActivity.this.avatarContainer2, !z2, 0.95f, true);
                ProfileActivity.this.callItem.setVisibility((z2 || !ProfileActivity.this.callItemVisible) ? 8 : 4);
                ProfileActivity.this.videoCallItem.setVisibility((z2 || !ProfileActivity.this.videoCallItemVisible) ? 8 : 4);
                ProfileActivity.this.editItem.setVisibility((z2 || !ProfileActivity.this.editItemVisible) ? 8 : 4);
                ProfileActivity.this.otherItem.setVisibility(z2 ? 8 : 4);
                if (ProfileActivity.this.qrItem != null) {
                    ProfileActivity.this.qrItem.setVisibility(z2 ? 8 : 4);
                }
                ProfileActivity.this.updateStoriesViewBounds(false);
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z2, View view) {
                return ProfileActivity.this.onMemberClick(tLRPC$ChatParticipant, z2, view);
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void drawBackgroundWithBlur(Canvas canvas, float f4, Rect rect, Paint paint) {
                ProfileActivity.this.contentView.drawBlurRect(canvas, ProfileActivity.this.listView.getY() + getY() + f4, rect, paint, true);
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void invalidateBlur() {
                if (ProfileActivity.this.contentView != null) {
                    ProfileActivity.this.contentView.invalidateBlur();
                }
            }
        };
        this.sharedMediaLayout = sharedMediaLayout2;
        sharedMediaLayout2.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (this.userId == getUserConfig().clientUserId) {
            ActionBarMenuItem addItem = createMenu.addItem(37, R.drawable.msg_qr_mini, getResourceProvider());
            this.qrItem = addItem;
            addItem.setContentDescription(LocaleController.getString("GetQRCode", R.string.GetQRCode));
            r11 = 0;
            r11 = 0;
            updateQrItemVisibility(false);
            if (ContactsController.getInstance(this.currentAccount).getPrivacyRules(7) == null) {
                ContactsController.getInstance(this.currentAccount).loadPrivacySettings();
            }
        } else {
            r11 = 0;
        }
        if (this.imageUpdater != null) {
            r13 = 1;
            r13 = 1;
            ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(32, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.ProfileActivity.8
                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public Animator getCustomToggleTransition() {
                    ProfileActivity profileActivity = ProfileActivity.this;
                    profileActivity.searchMode = !profileActivity.searchMode;
                    if (!ProfileActivity.this.searchMode) {
                        ProfileActivity.this.searchItem.clearFocusOnSearchView();
                    }
                    if (ProfileActivity.this.searchMode) {
                        ProfileActivity.this.searchItem.getSearchField().setText("");
                    }
                    ProfileActivity profileActivity2 = ProfileActivity.this;
                    return profileActivity2.searchExpandTransition(profileActivity2.searchMode);
                }

                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onTextChanged(EditText editText) {
                    ProfileActivity.this.searchAdapter.search(editText.getText().toString().toLowerCase());
                }
            });
            this.searchItem = actionBarMenuItemSearchListener;
            int i2 = R.string.SearchInSettings;
            actionBarMenuItemSearchListener.setContentDescription(LocaleController.getString("SearchInSettings", i2));
            this.searchItem.setSearchFieldHint(LocaleController.getString("SearchInSettings", i2));
            this.sharedMediaLayout.getSearchItem().setVisibility(8);
            if (this.expandPhoto) {
                this.searchItem.setVisibility(8);
            }
        } else {
            r13 = 1;
        }
        ActionBarMenuItem addItem2 = createMenu.addItem(16, R.drawable.profile_video);
        this.videoCallItem = addItem2;
        addItem2.setContentDescription(LocaleController.getString("VideoCall", R.string.VideoCall));
        if (this.chatId != 0) {
            this.callItem = createMenu.addItem(15, R.drawable.msg_voicechat2);
            if (ChatObject.isChannelOrGiga(this.currentChat)) {
                this.callItem.setContentDescription(LocaleController.getString("VoipChannelVoiceChat", R.string.VoipChannelVoiceChat));
            } else {
                this.callItem.setContentDescription(LocaleController.getString("VoipGroupVoiceChat", R.string.VoipGroupVoiceChat));
            }
        } else {
            ActionBarMenuItem addItem3 = createMenu.addItem(15, R.drawable.ic_call);
            this.callItem = addItem3;
            addItem3.setContentDescription(LocaleController.getString("Call", R.string.Call));
        }
        ActionBarMenuItem addItem4 = createMenu.addItem(12, R.drawable.group_edit_profile);
        this.editItem = addItem4;
        addItem4.setContentDescription(LocaleController.getString("Edit", R.string.Edit));
        this.otherItem = createMenu.addItem(10, R.drawable.ic_ab_other, this.resourcesProvider);
        ImageView imageView = new ImageView(context);
        this.ttlIconView = imageView;
        imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_actionBarDefaultIcon), PorterDuff.Mode.MULTIPLY));
        AndroidUtilities.updateViewVisibilityAnimated(this.ttlIconView, r11, 0.8f, r11);
        this.ttlIconView.setImageResource(R.drawable.msg_mini_autodelete_timer);
        this.otherItem.addView(this.ttlIconView, LayoutHelper.createFrame(12, 12.0f, 19, 8.0f, 2.0f, 0.0f, 0.0f));
        this.otherItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
        if (this.listView == null || this.imageUpdater == null) {
            i = -1;
            obj = null;
        } else {
            int findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
            View findViewByPosition = this.layoutManager.findViewByPosition(findFirstVisibleItemPosition);
            if (findViewByPosition != null) {
                findViewByPosition.getTop();
                this.listView.getPaddingTop();
            } else {
                findFirstVisibleItemPosition = -1;
            }
            obj = this.writeButton.getTag();
            i = findFirstVisibleItemPosition;
        }
        createActionBarMenu(r11);
        this.listAdapter = new ListAdapter(context);
        this.searchAdapter = new SearchAdapter(context);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        this.avatarDrawable = avatarDrawable;
        avatarDrawable.setProfile(r13);
        this.fragmentView.setWillNotDraw(r11);
        View view = this.fragmentView;
        NestedFrameLayout nestedFrameLayout = (NestedFrameLayout) view;
        this.contentView = nestedFrameLayout;
        nestedFrameLayout.needBlur = r13;
        FrameLayout frameLayout = (FrameLayout) view;
        ClippedListView clippedListView = new ClippedListView(context) { // from class: org.telegram.ui.ProfileActivity.9
            private VelocityTracker velocityTracker;

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
            public boolean hasOverlappingRendering() {
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.RecyclerView
            public void requestChildOnScreen(View view2, View view3) {
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView
            public boolean canHighlightChildAt(View view2, float f4, float f5) {
                return !(view2 instanceof AboutLinkCell);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView
            public boolean allowSelectChildAtPosition(View view2) {
                return view2 != ProfileActivity.this.sharedMediaLayout;
            }

            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                if (((BaseFragment) ProfileActivity.this).fragmentView != null) {
                    ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
                }
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                VelocityTracker velocityTracker;
                View findViewByPosition2;
                int action = motionEvent.getAction();
                if (action == 0) {
                    VelocityTracker velocityTracker2 = this.velocityTracker;
                    if (velocityTracker2 == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    } else {
                        velocityTracker2.clear();
                    }
                    this.velocityTracker.addMovement(motionEvent);
                } else if (action == 2) {
                    VelocityTracker velocityTracker3 = this.velocityTracker;
                    if (velocityTracker3 != null) {
                        velocityTracker3.addMovement(motionEvent);
                        this.velocityTracker.computeCurrentVelocity(1000);
                        ProfileActivity.this.listViewVelocityY = this.velocityTracker.getYVelocity(motionEvent.getPointerId(motionEvent.getActionIndex()));
                    }
                } else if ((action == 1 || action == 3) && (velocityTracker = this.velocityTracker) != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                boolean onTouchEvent = super.onTouchEvent(motionEvent);
                if ((action == 1 || action == 3) && ProfileActivity.this.allowPullingDown && (findViewByPosition2 = ProfileActivity.this.layoutManager.findViewByPosition(0)) != null) {
                    if (ProfileActivity.this.isPulledDown) {
                        ProfileActivity.this.listView.smoothScrollBy(0, (findViewByPosition2.getTop() - ProfileActivity.this.listView.getMeasuredWidth()) + ActionBar.getCurrentActionBarHeight() + (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0), CubicBezierInterpolator.EASE_OUT_QUINT);
                    } else {
                        ProfileActivity.this.listView.smoothScrollBy(0, findViewByPosition2.getTop() - AndroidUtilities.dp(88.0f), CubicBezierInterpolator.EASE_OUT_QUINT);
                    }
                }
                return onTouchEvent;
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view2, long j3) {
                if (getItemAnimator().isRunning() && view2.getBackground() == null && view2.getTranslationY() != 0.0f) {
                    boolean z2 = ProfileActivity.this.listView.getChildAdapterPosition(view2) == ProfileActivity.this.sharedMediaRow && view2.getAlpha() != 1.0f;
                    if (z2) {
                        ProfileActivity.this.whitePaint.setAlpha((int) (ProfileActivity.this.listView.getAlpha() * 255.0f * view2.getAlpha()));
                    }
                    canvas.drawRect(ProfileActivity.this.listView.getX(), view2.getY(), ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), view2.getY() + view2.getHeight(), ProfileActivity.this.whitePaint);
                    if (z2) {
                        ProfileActivity.this.whitePaint.setAlpha((int) (ProfileActivity.this.listView.getAlpha() * 255.0f));
                    }
                }
                return super.drawChild(canvas, view2, j3);
            }
        };
        this.listView = clippedListView;
        clippedListView.setVerticalScrollBarEnabled(r11);
        10 r0 = new 10();
        this.listView.setItemAnimator(r0);
        r0.setMoveDelay(0L);
        r0.setMoveDuration(320L);
        r0.setRemoveDuration(320L);
        r0.setAddDuration(320L);
        r0.setSupportsChangeAnimations(r11);
        r0.setDelayAnimations(r11);
        r0.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.listView.setClipToPadding(r11);
        this.listView.setHideIfEmpty(r11);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) { // from class: org.telegram.ui.ProfileActivity.11
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return ProfileActivity.this.imageUpdater != null;
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int scrollVerticallyBy(int i3, RecyclerView.Recycler recycler, RecyclerView.State state) {
                View findViewByPosition2 = ProfileActivity.this.layoutManager.findViewByPosition(0);
                if (findViewByPosition2 != null && !ProfileActivity.this.openingAvatar) {
                    int top = findViewByPosition2.getTop() - AndroidUtilities.dp(88.0f);
                    if (ProfileActivity.this.allowPullingDown || top <= i3) {
                        if (ProfileActivity.this.allowPullingDown) {
                            if (i3 >= top) {
                                ProfileActivity.this.allowPullingDown = false;
                            } else if (ProfileActivity.this.listView.getScrollState() == 1 && !ProfileActivity.this.isPulledDown) {
                                i3 /= 2;
                            }
                        }
                    } else if (ProfileActivity.this.avatarsViewPager.hasImages() && ProfileActivity.this.avatarImage.getImageReceiver().hasNotThumb() && !AndroidUtilities.isAccessibilityScreenReaderEnabled() && !ProfileActivity.this.isInLandscapeMode && !AndroidUtilities.isTablet()) {
                        ProfileActivity profileActivity = ProfileActivity.this;
                        profileActivity.allowPullingDown = profileActivity.avatarBig == null;
                    }
                    i3 = top;
                }
                return super.scrollVerticallyBy(i3, recycler, state);
            }
        };
        this.layoutManager = linearLayoutManager;
        linearLayoutManager.setOrientation(r13);
        LinearLayoutManager linearLayoutManager2 = this.layoutManager;
        linearLayoutManager2.mIgnoreTopPadding = r11;
        this.listView.setLayoutManager(linearLayoutManager2);
        this.listView.setGlowColor(r11);
        this.listView.setAdapter(this.listAdapter);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda64
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view2, int i3) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view2, i3);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view2, int i3, float f4, float f5) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view2, i3, f4, f5);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view2, int i3, float f4, float f5) {
                ProfileActivity.this.lambda$createView$8(j2, context, view2, i3, f4, f5);
            }
        });
        this.listView.setOnItemLongClickListener(new 15(context));
        if (this.openSimilar) {
            updateRowsIds();
            scrollToSharedMedia();
            this.savedScrollToSharedMedia = r13;
            this.savedScrollPosition = this.sharedMediaRow;
            this.savedScrollOffset = r11;
        }
        if (this.searchItem != null) {
            RecyclerListView recyclerListView = new RecyclerListView(context);
            this.searchListView = recyclerListView;
            recyclerListView.setVerticalScrollBarEnabled(r11);
            this.searchListView.setLayoutManager(new LinearLayoutManager(context, r13, r11));
            this.searchListView.setGlowColor(getThemedColor(Theme.key_avatar_backgroundActionBarBlue));
            this.searchListView.setAdapter(this.searchAdapter);
            this.searchListView.setItemAnimator(null);
            this.searchListView.setVisibility(8);
            this.searchListView.setLayoutAnimation(null);
            this.searchListView.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
            frameLayout.addView(this.searchListView, LayoutHelper.createFrame(-1, -1, 51));
            this.searchListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda63
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view2, int i3) {
                    ProfileActivity.this.lambda$createView$9(view2, i3);
                }
            });
            this.searchListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda65
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                public final boolean onItemClick(View view2, int i3) {
                    boolean lambda$createView$11;
                    lambda$createView$11 = ProfileActivity.this.lambda$createView$11(view2, i3);
                    return lambda$createView$11;
                }
            });
            this.searchListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ProfileActivity.16
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i3) {
                    if (i3 == 1) {
                        AndroidUtilities.hideKeyboard(ProfileActivity.this.getParentActivity().getCurrentFocus());
                    }
                }
            });
            this.searchListView.setAnimateEmptyView(r13, r13);
            StickerEmptyView stickerEmptyView = new StickerEmptyView(context, null, r13);
            this.emptyView = stickerEmptyView;
            stickerEmptyView.setAnimateLayoutChange(r13);
            this.emptyView.subtitle.setVisibility(8);
            this.emptyView.setVisibility(8);
            frameLayout.addView(this.emptyView);
            this.searchAdapter.loadFaqWebPage();
        }
        if (this.banFromGroup != 0) {
            final TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.banFromGroup));
            if (this.currentChannelParticipant == null) {
                TLRPC$TL_channels_getParticipant tLRPC$TL_channels_getParticipant = new TLRPC$TL_channels_getParticipant();
                tLRPC$TL_channels_getParticipant.channel = MessagesController.getInputChannel(chat);
                tLRPC$TL_channels_getParticipant.participant = getMessagesController().getInputPeer(this.userId);
                getConnectionsManager().sendRequest(tLRPC$TL_channels_getParticipant, new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda59
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        ProfileActivity.this.lambda$createView$13(tLObject, tLRPC$TL_error);
                    }
                });
            }
            FrameLayout frameLayout2 = new FrameLayout(this, context) { // from class: org.telegram.ui.ProfileActivity.17
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                    Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), intrinsicHeight);
                    Theme.chat_composeShadowDrawable.draw(canvas);
                    canvas.drawRect(0.0f, intrinsicHeight, getMeasuredWidth(), getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
                }
            };
            frameLayout2.setWillNotDraw(r11);
            frameLayout.addView(frameLayout2, LayoutHelper.createFrame(-1, 51, 83));
            frameLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda18
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ProfileActivity.this.lambda$createView$14(chat, view2);
                }
            });
            TextView textView = new TextView(context);
            textView.setTextColor(getThemedColor(Theme.key_text_RedRegular));
            textView.setTextSize(r13, 15.0f);
            textView.setGravity(17);
            textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            textView.setText(LocaleController.getString("BanFromTheGroup", R.string.BanFromTheGroup));
            frameLayout2.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 1.0f, 0.0f, 0.0f));
            this.listView.setPadding(r11, AndroidUtilities.dp(88.0f), r11, AndroidUtilities.dp(48.0f));
            this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
        } else {
            this.listView.setPadding(r11, AndroidUtilities.dp(88.0f), r11, r11);
        }
        TopView topView = new TopView(context);
        this.topView = topView;
        topView.setBackgroundColorId(this.peerColor, r11);
        this.topView.setBackgroundColor(getThemedColor(Theme.key_avatar_backgroundActionBarBlue));
        frameLayout.addView(this.topView);
        this.contentView.blurBehindViews.add(this.topView);
        DrawerProfileCell.AnimatedStatusView animatedStatusView = new DrawerProfileCell.AnimatedStatusView(context, 20, 60);
        this.animatedStatusView = animatedStatusView;
        animatedStatusView.setPivotX(AndroidUtilities.dp(30.0f));
        this.animatedStatusView.setPivotY(AndroidUtilities.dp(30.0f));
        this.avatarContainer = new FrameLayout(context);
        this.avatarContainer2 = new 19(context);
        ImageReceiver imageReceiver = new ImageReceiver(this.avatarContainer2);
        this.fallbackImage = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.dp(11.0f));
        AndroidUtilities.updateViewVisibilityAnimated(this.avatarContainer2, r13, 1.0f, r11);
        frameLayout.addView(this.avatarContainer2, LayoutHelper.createFrame(-1, -1.0f, 8388611, 0.0f, 0.0f, 0.0f, 0.0f));
        this.avatarContainer.setPivotX(0.0f);
        this.avatarContainer.setPivotY(0.0f);
        this.avatarContainer2.addView(this.avatarContainer, LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
        AvatarImageView avatarImageView = new AvatarImageView(this, context) { // from class: org.telegram.ui.ProfileActivity.20
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                if (getImageReceiver().hasNotThumb()) {
                    accessibilityNodeInfo.setText(LocaleController.getString("AccDescrProfilePicture", R.string.AccDescrProfilePicture));
                    if (Build.VERSION.SDK_INT >= 21) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString("Open", R.string.Open)));
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, LocaleController.getString("AccDescrOpenInPhotoViewer", R.string.AccDescrOpenInPhotoViewer)));
                        return;
                    }
                    return;
                }
                accessibilityNodeInfo.setVisibleToUser(false);
            }

            @Override // android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
                if (animatedEmojiDrawable == null || animatedEmojiDrawable.getImageReceiver() == null) {
                    return;
                }
                this.animatedEmojiDrawable.getImageReceiver().startAnimation();
            }
        };
        this.avatarImage = avatarImageView;
        avatarImageView.getImageReceiver().setAllowDecodeSingleFrame(r13);
        this.avatarImage.setRoundRadius(getSmallAvatarRoundRadius());
        this.avatarImage.setPivotX(0.0f);
        this.avatarImage.setPivotY(0.0f);
        this.avatarContainer.addView(this.avatarImage, LayoutHelper.createFrame(-1, -1.0f));
        this.avatarImage.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ProfileActivity.this.lambda$createView$16(view2);
            }
        });
        this.avatarImage.setHasStories(needInsetForStories());
        this.avatarImage.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda22
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view2) {
                boolean lambda$createView$17;
                lambda$createView$17 = ProfileActivity.this.lambda$createView$17(view2);
                return lambda$createView$17;
            }
        });
        RadialProgressView radialProgressView = new RadialProgressView(context) { // from class: org.telegram.ui.ProfileActivity.21
            private Paint paint;

            {
                Paint paint = new Paint(1);
                this.paint = paint;
                paint.setColor(1426063360);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RadialProgressView, android.view.View
            public void onDraw(Canvas canvas) {
                if (ProfileActivity.this.avatarImage != null && ProfileActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                    this.paint.setAlpha((int) (ProfileActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
                    canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() / 2.0f, this.paint);
                }
                super.onDraw(canvas);
            }
        };
        this.avatarProgressView = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(26.0f));
        this.avatarProgressView.setProgressColor(-1);
        this.avatarProgressView.setNoProgress(r11);
        this.avatarContainer.addView(this.avatarProgressView, LayoutHelper.createFrame(-1, -1.0f));
        ImageView imageView2 = new ImageView(context);
        this.timeItem = imageView2;
        imageView2.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
        this.timeItem.setScaleType(ImageView.ScaleType.CENTER);
        this.timeItem.setAlpha(0.0f);
        ImageView imageView3 = this.timeItem;
        TimerDrawable timerDrawable = new TimerDrawable(context, null);
        this.timerDrawable = timerDrawable;
        imageView3.setImageDrawable(timerDrawable);
        this.timeItem.setTranslationY(-1.0f);
        frameLayout.addView(this.timeItem, LayoutHelper.createFrame(34, 34, 51));
        showAvatarProgress(r11, r11);
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        if (profileGalleryView != null) {
            profileGalleryView.onDestroy();
        }
        this.overlaysView = new OverlaysView(context);
        long j3 = this.userId;
        if (j3 == 0) {
            j3 = -this.chatId;
        }
        int i3 = i;
        this.avatarsViewPager = new ProfileGalleryView(context, j3, this.actionBar, this.listView, this.avatarImage, getClassGuid(), this.overlaysView) { // from class: org.telegram.ui.ProfileActivity.22
            @Override // org.telegram.ui.Components.ProfileGalleryView
            protected void setCustomAvatarProgress(float f4) {
                ProfileActivity.this.customAvatarProgress = f4;
                ProfileActivity.this.checkPhotoDescriptionAlpha();
            }
        };
        if (this.userId != getUserConfig().clientUserId && (tLRPC$UserFull = this.userInfo) != null) {
            this.customAvatarProgress = tLRPC$UserFull.profile_photo == null ? 0.0f : 1.0f;
        }
        if (!this.isTopic) {
            this.avatarsViewPager.setChatInfo(this.chatInfo);
        }
        this.avatarContainer2.addView(this.avatarsViewPager);
        this.avatarContainer2.addView(this.overlaysView);
        this.avatarImage.setAvatarsViewPager(this.avatarsViewPager);
        PagerIndicatorView pagerIndicatorView = new PagerIndicatorView(context);
        this.avatarsViewPagerIndicatorView = pagerIndicatorView;
        this.avatarContainer2.addView(pagerIndicatorView, LayoutHelper.createFrame(-1, -1.0f));
        frameLayout.addView(this.actionBar);
        float f4 = 54 + ((!this.callItemVisible || this.userId == 0) ? 0 : 54);
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout == null || !(iNavigationLayout.getLastFragment() instanceof ChatActivity) || (avatarContainer = ((ChatActivity) this.parentLayout.getLastFragment()).getAvatarContainer()) == null) {
            z = false;
        } else {
            z = avatarContainer.getTitleTextView().getPaddingRight() != 0;
            if (avatarContainer.getLayoutParams() != null && avatarContainer.getTitleTextView() != null) {
                f4 = (((ViewGroup.MarginLayoutParams) avatarContainer.getLayoutParams()).rightMargin + (avatarContainer.getWidth() - avatarContainer.getTitleTextView().getRight())) / AndroidUtilities.density;
            }
        }
        int i4 = 0;
        while (true) {
            SimpleTextView[] simpleTextViewArr = this.nameTextView;
            if (i4 >= simpleTextViewArr.length) {
                break;
            }
            if (this.playProfileAnimation != 0 || i4 != 0) {
                simpleTextViewArr[i4] = new SimpleTextView(context) { // from class: org.telegram.ui.ProfileActivity.23
                    @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View
                    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                        if (!isFocusable() || ProfileActivity.this.nameTextViewRightDrawableContentDescription == null) {
                            return;
                        }
                        accessibilityNodeInfo.setText(((Object) getText()) + ", " + ProfileActivity.this.nameTextViewRightDrawableContentDescription);
                    }
                };
                if (i4 == 1) {
                    this.nameTextView[i4].setTextColor(getThemedColor(Theme.key_profile_title));
                } else {
                    this.nameTextView[i4].setTextColor(getThemedColor(Theme.key_actionBarDefaultTitle));
                }
                this.nameTextView[i4].setPadding(0, AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(i4 == 0 ? 12.0f : 4.0f));
                this.nameTextView[i4].setTextSize(18);
                this.nameTextView[i4].setGravity(3);
                this.nameTextView[i4].setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.nameTextView[i4].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
                this.nameTextView[i4].setPivotX(0.0f);
                this.nameTextView[i4].setPivotY(0.0f);
                this.nameTextView[i4].setAlpha(i4 == 0 ? 0.0f : 1.0f);
                if (i4 == 1) {
                    this.nameTextView[i4].setScrollNonFitText(true);
                    this.nameTextView[i4].setImportantForAccessibility(2);
                }
                this.nameTextView[i4].setFocusable(i4 == 0);
                this.nameTextView[i4].setEllipsizeByGradient(true);
                this.nameTextView[i4].setRightDrawableOutside(i4 == 0);
                FrameLayout frameLayout3 = this.avatarContainer2;
                SimpleTextView simpleTextView = this.nameTextView[i4];
                if (i4 == 0) {
                    f3 = f4 - (z ? 10 : 0);
                } else {
                    f3 = 0.0f;
                }
                frameLayout3.addView(simpleTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, -6.0f, f3, 0.0f));
            }
            i4++;
        }
        int i5 = 0;
        while (true) {
            SimpleTextView[] simpleTextViewArr2 = this.onlineTextView;
            if (i5 >= simpleTextViewArr2.length) {
                break;
            }
            if (i5 == 1) {
                simpleTextViewArr2[i5] = new LinkSpanDrawable.ClickableSmallTextView(context) { // from class: org.telegram.ui.ProfileActivity.24
                    @Override // android.view.View
                    public void setAlpha(float f5) {
                        super.setAlpha(f5);
                        ProfileActivity.this.checkPhotoDescriptionAlpha();
                    }

                    @Override // android.view.View
                    public void setTranslationY(float f5) {
                        super.setTranslationY(f5);
                        ProfileActivity.this.onlineTextView[2].setTranslationY(f5);
                        ProfileActivity.this.onlineTextView[3].setTranslationY(f5);
                    }

                    @Override // android.view.View
                    public void setTranslationX(float f5) {
                        super.setTranslationX(f5);
                        ProfileActivity.this.onlineTextView[2].setTranslationX(f5);
                        ProfileActivity.this.onlineTextView[3].setTranslationX(f5);
                    }

                    @Override // org.telegram.ui.ActionBar.SimpleTextView
                    public void setTextColor(int i6) {
                        super.setTextColor(i6);
                        if (ProfileActivity.this.onlineTextView[2] != null) {
                            ProfileActivity.this.onlineTextView[2].setTextColor(i6);
                            ProfileActivity.this.onlineTextView[3].setTextColor(i6);
                        }
                    }
                };
            } else {
                simpleTextViewArr2[i5] = new LinkSpanDrawable.ClickableSmallTextView(context);
            }
            this.onlineTextView[i5].setEllipsizeByGradient(true);
            this.onlineTextView[i5].setTextColor(applyPeerColor(getThemedColor(Theme.key_avatar_subtitleInProfileBlue), true));
            this.onlineTextView[i5].setTextSize(14);
            this.onlineTextView[i5].setGravity(3);
            this.onlineTextView[i5].setAlpha(i5 == 0 ? 0.0f : 1.0f);
            if (i5 == 1 || i5 == 2 || i5 == 3) {
                this.onlineTextView[i5].setPadding(AndroidUtilities.dp(f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(f), AndroidUtilities.dp(2.0f));
            }
            if (i5 > 0) {
                this.onlineTextView[i5].setImportantForAccessibility(2);
            }
            this.onlineTextView[i5].setFocusable(i5 == 0);
            FrameLayout frameLayout4 = this.avatarContainer2;
            SimpleTextView simpleTextView2 = this.onlineTextView[i5];
            float f5 = 118 - ((i5 == 1 || i5 == 2 || i5 == 3) ? 4 : 0);
            float f6 = (i5 == 1 || i5 == 2 || i5 == 3) ? -2 : 0;
            if (i5 == 0) {
                f2 = f4 - (z ? 10 : 0);
            } else {
                f2 = 8.0f;
            }
            frameLayout4.addView(simpleTextView2, LayoutHelper.createFrame(-2, -2.0f, 51, f5, f6, f2 - ((i5 == 1 || i5 == 2 || i5 == 3) ? 4 : 0), 0.0f));
            i5++;
            f = 4.0f;
        }
        checkPhotoDescriptionAlpha();
        this.avatarContainer2.addView(this.animatedStatusView);
        AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher = new AudioPlayerAlert.ClippingTextViewSwitcher(context) { // from class: org.telegram.ui.ProfileActivity.25
            @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
            protected TextView createTextView() {
                TextView textView2 = new TextView(context);
                textView2.setTextColor(ProfileActivity.this.getThemedColor(Theme.key_player_actionBarSubtitle));
                textView2.setTextSize(1, 14.0f);
                textView2.setSingleLine(true);
                textView2.setEllipsize(TextUtils.TruncateAt.END);
                textView2.setGravity(3);
                return textView2;
            }
        };
        this.mediaCounterTextView = clippingTextViewSwitcher;
        clippingTextViewSwitcher.setAlpha(0.0f);
        this.avatarContainer2.addView(this.mediaCounterTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, 8.0f, 0.0f));
        this.storyView = new ProfileStoriesView(context, this.currentAccount, getDialogId(), this.avatarContainer, this.avatarImage, this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.26
            @Override // org.telegram.ui.Stories.ProfileStoriesView
            protected void onTap(StoryViewer.PlaceProvider placeProvider) {
                long dialogId = ProfileActivity.this.getDialogId();
                StoriesController storiesController = ProfileActivity.this.getMessagesController().getStoriesController();
                if (!storiesController.hasStories(dialogId) && !storiesController.hasUploadingStories(dialogId) && !storiesController.isLastUploadingFailed(dialogId)) {
                    if (ProfileActivity.this.userInfo == null || ProfileActivity.this.userInfo.stories == null || ProfileActivity.this.userInfo.stories.stories.isEmpty() || ProfileActivity.this.userId == ProfileActivity.this.getUserConfig().clientUserId) {
                        if (ProfileActivity.this.chatInfo == null || ProfileActivity.this.chatInfo.stories == null || ProfileActivity.this.chatInfo.stories.stories.isEmpty()) {
                            ProfileActivity.this.expandAvatar();
                            return;
                        } else {
                            ProfileActivity.this.getOrCreateStoryViewer().open(context, ProfileActivity.this.chatInfo.stories, placeProvider);
                            return;
                        }
                    }
                    ProfileActivity.this.getOrCreateStoryViewer().open(context, ProfileActivity.this.userInfo.stories, placeProvider);
                    return;
                }
                ProfileActivity.this.getOrCreateStoryViewer().open(context, dialogId, placeProvider);
            }

            @Override // org.telegram.ui.Stories.ProfileStoriesView
            protected void onLongPress() {
                ProfileActivity.this.openAvatar();
            }
        };
        updateStoriesViewBounds(false);
        TLRPC$UserFull tLRPC$UserFull3 = this.userInfo;
        if (tLRPC$UserFull3 != null) {
            this.storyView.setStories(tLRPC$UserFull3.stories);
        } else {
            TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
            if (tLRPC$ChatFull2 != null) {
                this.storyView.setStories(tLRPC$ChatFull2.stories);
            }
        }
        AvatarImageView avatarImageView2 = this.avatarImage;
        if (avatarImageView2 != null) {
            avatarImageView2.setHasStories(needInsetForStories());
        }
        this.avatarContainer2.addView(this.storyView, LayoutHelper.createFrame(-1, -1.0f));
        updateProfileData(true);
        this.writeButton = new RLottieImageView(context);
        writeButtonSetBackground();
        if (this.userId != 0) {
            if (this.imageUpdater != null) {
                int i6 = R.raw.camera_outline;
                this.cameraDrawable = new RLottieDrawable(i6, String.valueOf(i6), AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f), false, null);
                this.cellCameraDrawable = new RLottieDrawable(i6, i6 + "_cell", AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f), false, null);
                this.writeButton.setAnimation(this.cameraDrawable);
                this.writeButton.setContentDescription(LocaleController.getString("AccDescrChangeProfilePicture", R.string.AccDescrChangeProfilePicture));
                this.writeButton.setPadding(AndroidUtilities.dp(2.0f), 0, 0, AndroidUtilities.dp(2.0f));
            } else {
                this.writeButton.setImageResource(R.drawable.profile_newmsg);
                this.writeButton.setContentDescription(LocaleController.getString("AccDescrOpenChat", R.string.AccDescrOpenChat));
            }
        } else {
            this.writeButton.setImageResource(R.drawable.profile_discuss);
            this.writeButton.setContentDescription(LocaleController.getString("ViewDiscussion", R.string.ViewDiscussion));
        }
        this.writeButton.setScaleType(ImageView.ScaleType.CENTER);
        frameLayout.addView(this.writeButton, LayoutHelper.createFrame(60, 60.0f, 53, 0.0f, 0.0f, 16.0f, 0.0f));
        this.writeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ProfileActivity.this.lambda$createView$18(view2);
            }
        });
        needLayout(false);
        if (i3 != -1 && obj != null) {
            this.writeButton.setTag(0);
            this.writeButton.setScaleX(0.2f);
            this.writeButton.setScaleY(0.2f);
            this.writeButton.setAlpha(0.0f);
        }
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ProfileActivity.27
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i7) {
                boolean z2 = true;
                if (i7 == 1) {
                    AndroidUtilities.hideKeyboard(ProfileActivity.this.getParentActivity().getCurrentFocus());
                }
                if (ProfileActivity.this.openingAvatar && i7 != 2) {
                    ProfileActivity.this.openingAvatar = false;
                }
                if (ProfileActivity.this.searchItem != null) {
                    ProfileActivity.this.scrolling = i7 != 0;
                    ProfileActivity.this.searchItem.setEnabled((ProfileActivity.this.scrolling || ProfileActivity.this.isPulledDown) ? false : false);
                }
                ProfileActivity.this.sharedMediaLayout.scrollingByUser = ProfileActivity.this.listView.scrollingByUser;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i7, int i8) {
                if (ProfileActivity.this.fwdRestrictedHint != null) {
                    ProfileActivity.this.fwdRestrictedHint.hide();
                }
                ProfileActivity.this.checkListViewScroll();
                if (ProfileActivity.this.participantsMap != null && !ProfileActivity.this.usersEndReached && ProfileActivity.this.layoutManager.findLastVisibleItemPosition() > ProfileActivity.this.membersEndRow - 8) {
                    ProfileActivity.this.getChannelParticipants(false);
                }
                ProfileActivity.this.sharedMediaLayout.setPinnedToTop(ProfileActivity.this.sharedMediaLayout.getY() == 0.0f);
            }
        });
        UndoView undoView = new UndoView(context, null, false, this.resourcesProvider);
        this.undoView = undoView;
        frameLayout.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.expandAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProfileActivity.this.lambda$createView$19(valueAnimator);
            }
        });
        this.expandAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.expandAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.28
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                int themedColor;
                ActionBar actionBar = ((BaseFragment) ProfileActivity.this).actionBar;
                if (ProfileActivity.this.isPulledDown) {
                    themedColor = 1090519039;
                } else {
                    themedColor = ProfileActivity.this.peerColor != null ? 553648127 : ProfileActivity.this.getThemedColor(Theme.key_avatar_actionBarSelectorBlue);
                }
                actionBar.setItemsBackgroundColor(themedColor, false);
                ProfileActivity.this.avatarImage.clearForeground();
                ProfileActivity.this.doNotSetForeground = false;
                ProfileActivity.this.updateStoriesViewBounds(false);
            }
        });
        updateRowsIds();
        updateSelectedMediaTabText();
        HintView hintView = new HintView(getParentActivity(), 9);
        this.fwdRestrictedHint = hintView;
        hintView.setAlpha(0.0f);
        frameLayout.addView(this.fwdRestrictedHint, LayoutHelper.createFrame(-2, -2.0f, 51, 12.0f, 0.0f, 12.0f, 0.0f));
        this.sharedMediaLayout.setForwardRestrictedHint(this.fwdRestrictedHint);
        int i7 = Build.VERSION.SDK_INT;
        PinchToZoomHelper pinchToZoomHelper = new PinchToZoomHelper(i7 >= 21 ? (ViewGroup) getParentActivity().getWindow().getDecorView() : frameLayout, frameLayout) { // from class: org.telegram.ui.ProfileActivity.29
            @Override // org.telegram.ui.PinchToZoomHelper
            protected void invalidateViews() {
                super.invalidateViews();
                ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
                for (int i8 = 0; i8 < ProfileActivity.this.avatarsViewPager.getChildCount(); i8++) {
                    ProfileActivity.this.avatarsViewPager.getChildAt(i8).invalidate();
                }
                if (ProfileActivity.this.writeButton != null) {
                    ProfileActivity.this.writeButton.invalidate();
                }
            }

            @Override // org.telegram.ui.PinchToZoomHelper
            protected void drawOverlays(Canvas canvas, float f7, float f8, float f9, float f10, float f11) {
                if (f7 > 0.0f) {
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, ProfileActivity.this.avatarsViewPager.getMeasuredWidth(), ProfileActivity.this.avatarsViewPager.getMeasuredHeight() + AndroidUtilities.dp(30.0f));
                    canvas.saveLayerAlpha(rectF, (int) (255.0f * f7), 31);
                    ProfileActivity.this.avatarContainer2.draw(canvas);
                    ((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar();
                    canvas.save();
                    canvas.translate(((BaseFragment) ProfileActivity.this).actionBar.getX(), ((BaseFragment) ProfileActivity.this).actionBar.getY());
                    ((BaseFragment) ProfileActivity.this).actionBar.draw(canvas);
                    canvas.restore();
                    if (ProfileActivity.this.writeButton != null && ProfileActivity.this.writeButton.getVisibility() == 0 && ProfileActivity.this.writeButton.getAlpha() > 0.0f) {
                        canvas.save();
                        float f12 = (f7 * 0.5f) + 0.5f;
                        canvas.scale(f12, f12, ProfileActivity.this.writeButton.getX() + (ProfileActivity.this.writeButton.getMeasuredWidth() / 2.0f), ProfileActivity.this.writeButton.getY() + (ProfileActivity.this.writeButton.getMeasuredHeight() / 2.0f));
                        canvas.translate(ProfileActivity.this.writeButton.getX(), ProfileActivity.this.writeButton.getY());
                        ProfileActivity.this.writeButton.draw(canvas);
                        canvas.restore();
                    }
                    canvas.restore();
                }
            }

            @Override // org.telegram.ui.PinchToZoomHelper
            protected boolean zoomEnabled(View view2, ImageReceiver imageReceiver2) {
                return super.zoomEnabled(view2, imageReceiver2) && ProfileActivity.this.listView.getScrollState() != 1;
            }
        };
        this.pinchToZoomHelper = pinchToZoomHelper;
        pinchToZoomHelper.setCallback(new PinchToZoomHelper.Callback() { // from class: org.telegram.ui.ProfileActivity.30
            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public /* synthetic */ TextureView getCurrentTextureView() {
                return PinchToZoomHelper.Callback.-CC.$default$getCurrentTextureView(this);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public /* synthetic */ void onZoomFinished(MessageObject messageObject) {
                PinchToZoomHelper.Callback.-CC.$default$onZoomFinished(this, messageObject);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomStarted(MessageObject messageObject) {
                ProfileActivity.this.listView.cancelClickRunnables(true);
                if (ProfileActivity.this.sharedMediaLayout != null && ProfileActivity.this.sharedMediaLayout.getCurrentListView() != null) {
                    ProfileActivity.this.sharedMediaLayout.getCurrentListView().cancelClickRunnables(true);
                }
                TopView topView2 = ProfileActivity.this.topView;
                ProfileActivity profileActivity = ProfileActivity.this;
                topView2.setBackgroundColor(ColorUtils.blendARGB(profileActivity.getAverageColor(profileActivity.pinchToZoomHelper.getPhotoImage()), ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhite), 0.1f));
            }
        });
        this.avatarsViewPager.setPinchToZoomHelper(this.pinchToZoomHelper);
        this.scrimPaint.setAlpha(0);
        this.actionBarBackgroundPaint.setColor(getThemedColor(Theme.key_listSelector));
        this.contentView.blurBehindViews.add(this.sharedMediaLayout);
        updateTtlIcon();
        View view2 = new View(context) { // from class: org.telegram.ui.ProfileActivity.31
            @Override // android.view.View
            public void setAlpha(float f7) {
                super.setAlpha(f7);
                if (((BaseFragment) ProfileActivity.this).fragmentView != null) {
                    ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
                }
            }
        };
        this.blurredView = view2;
        if (i7 >= 23) {
            view2.setForeground(new ColorDrawable(ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundWhite), 100)));
        }
        this.blurredView.setFocusable(false);
        this.blurredView.setImportantForAccessibility(2);
        this.blurredView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                ProfileActivity.this.lambda$createView$20(view3);
            }
        });
        this.blurredView.setVisibility(8);
        this.blurredView.setFitsSystemWindows(true);
        this.contentView.addView(this.blurredView, LayoutHelper.createFrame(-1, -1.0f));
        createFloatingActionButton(getContext());
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 5 extends ActionBar.ActionBarMenuOnItemClick {
        5() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            String format;
            long j;
            if (ProfileActivity.this.getParentActivity() == null) {
                return;
            }
            if (i == -1) {
                ProfileActivity.this.finishFragment();
                return;
            }
            String str = null;
            String str2 = null;
            if (i == 2) {
                final TLRPC$User user = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                if (user == null) {
                    return;
                }
                if (!ProfileActivity.this.isBot || MessagesController.isSupportUser(user)) {
                    if (ProfileActivity.this.userBlocked) {
                        ProfileActivity.this.getMessagesController().unblockPeer(ProfileActivity.this.userId);
                        if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                            BulletinFactory.createBanBulletin(ProfileActivity.this, false).show();
                        }
                    } else if (ProfileActivity.this.reportSpam) {
                        ProfileActivity profileActivity = ProfileActivity.this;
                        AlertsCreator.showBlockReportSpamAlert(profileActivity, profileActivity.userId, user, null, ProfileActivity.this.currentEncryptedChat, false, null, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda10
                            @Override // org.telegram.messenger.MessagesStorage.IntCallback
                            public final void run(int i2) {
                                ProfileActivity.5.this.lambda$onItemClick$0(i2);
                            }
                        }, ProfileActivity.this.resourcesProvider);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                        builder.setTitle(LocaleController.getString("BlockUser", R.string.BlockUser));
                        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureBlockContact2", R.string.AreYouSureBlockContact2, ContactsController.formatName(user.first_name, user.last_name))));
                        builder.setPositiveButton(LocaleController.getString("BlockContact", R.string.BlockContact), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda1
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i2) {
                                ProfileActivity.5.this.lambda$onItemClick$1(dialogInterface, i2);
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        AlertDialog create = builder.create();
                        ProfileActivity.this.showDialog(create);
                        TextView textView = (TextView) create.getButton(-1);
                        if (textView != null) {
                            textView.setTextColor(ProfileActivity.this.getThemedColor(Theme.key_text_RedBold));
                        }
                    }
                } else if (ProfileActivity.this.userBlocked) {
                    ProfileActivity.this.getMessagesController().unblockPeer(ProfileActivity.this.userId, new Runnable() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            ProfileActivity.5.this.lambda$onItemClick$3();
                        }
                    });
                    ProfileActivity.this.finishFragment();
                } else {
                    ProfileActivity profileActivity2 = ProfileActivity.this;
                    AlertsCreator.createClearOrDeleteDialogAlert(profileActivity2, false, profileActivity2.currentChat, user, ProfileActivity.this.currentEncryptedChat != null, true, true, new MessagesStorage.BooleanCallback() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda9
                        @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
                        public final void run(boolean z) {
                            ProfileActivity.5.this.lambda$onItemClick$2(user, z);
                        }
                    }, ProfileActivity.this.getResourceProvider());
                }
            } else if (i == 1) {
                TLRPC$User user2 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", user2.id);
                bundle.putBoolean("addContact", true);
                ProfileActivity.this.openAddToContact(user2, bundle);
            } else if (i == 3) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("onlySelect", true);
                bundle2.putInt("dialogsType", 3);
                bundle2.putString("selectAlertString", LocaleController.getString("SendContactToText", R.string.SendContactToText));
                bundle2.putString("selectAlertStringGroup", LocaleController.getString("SendContactToGroupText", R.string.SendContactToGroupText));
                DialogsActivity dialogsActivity = new DialogsActivity(bundle2);
                dialogsActivity.setDelegate(ProfileActivity.this);
                ProfileActivity.this.presentFragment(dialogsActivity);
            } else if (i == 4) {
                Bundle bundle3 = new Bundle();
                bundle3.putLong("user_id", ProfileActivity.this.userId);
                ProfileActivity profileActivity3 = ProfileActivity.this;
                profileActivity3.presentFragment(new ContactAddActivity(bundle3, profileActivity3.resourcesProvider));
            } else if (i == 5) {
                final TLRPC$User user3 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                if (user3 == null || ProfileActivity.this.getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                builder2.setTitle(LocaleController.getString("DeleteContact", R.string.DeleteContact));
                builder2.setMessage(LocaleController.getString("AreYouSureDeleteContact", R.string.AreYouSureDeleteContact));
                builder2.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        ProfileActivity.5.this.lambda$onItemClick$4(user3, dialogInterface, i2);
                    }
                });
                builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create2 = builder2.create();
                ProfileActivity.this.showDialog(create2);
                TextView textView2 = (TextView) create2.getButton(-1);
                if (textView2 != null) {
                    textView2.setTextColor(ProfileActivity.this.getThemedColor(Theme.key_text_RedBold));
                }
            } else if (i == 7) {
                ProfileActivity.this.leaveChatPressed();
            } else if (i == 23) {
                AlertDialog.Builder builder3 = new AlertDialog.Builder(ProfileActivity.this.getContext());
                builder3.setTitle(LocaleController.getPluralString("DeleteTopics", 1));
                TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getTopicsController().findTopic(ProfileActivity.this.chatId, ProfileActivity.this.topicId);
                int i2 = R.string.DeleteSelectedTopic;
                Object[] objArr = new Object[1];
                objArr[0] = findTopic == null ? "topic" : findTopic.title;
                builder3.setMessage(LocaleController.formatString("DeleteSelectedTopic", i2, objArr));
                builder3.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity.5.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        arrayList.add(Integer.valueOf(ProfileActivity.this.topicId));
                        ProfileActivity.this.getMessagesController().getTopicsController().deleteTopics(ProfileActivity.this.chatId, arrayList);
                        ProfileActivity.this.playProfileAnimation = 0;
                        if (((BaseFragment) ProfileActivity.this).parentLayout != null && ((BaseFragment) ProfileActivity.this).parentLayout.getFragmentStack() != null) {
                            for (int i4 = 0; i4 < ((BaseFragment) ProfileActivity.this).parentLayout.getFragmentStack().size(); i4++) {
                                BaseFragment baseFragment = ((BaseFragment) ProfileActivity.this).parentLayout.getFragmentStack().get(i4);
                                if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getTopicId() == ProfileActivity.this.topicId) {
                                    baseFragment.removeSelfFromStack();
                                }
                            }
                        }
                        ProfileActivity.this.finishFragment();
                        Context context = ProfileActivity.this.getContext();
                        if (context != null) {
                            BulletinFactory.of(Bulletin.BulletinWindow.make(context), ProfileActivity.this.resourcesProvider).createSimpleBulletin(R.raw.ic_delete, LocaleController.getPluralString("TopicsDeleted", 1)).show();
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener(this) { // from class: org.telegram.ui.ProfileActivity.5.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog create3 = builder3.create();
                create3.show();
                TextView textView3 = (TextView) create3.getButton(-1);
                if (textView3 != null) {
                    textView3.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                }
            } else if (i == 12) {
                if (ProfileActivity.this.isTopic) {
                    new Bundle().putLong("chat_id", ProfileActivity.this.chatId);
                    ProfileActivity.this.presentFragment(TopicCreateFragment.create(ProfileActivity.this.chatId, ProfileActivity.this.topicId));
                    return;
                }
                Bundle bundle4 = new Bundle();
                if (ProfileActivity.this.chatId != 0) {
                    bundle4.putLong("chat_id", ProfileActivity.this.chatId);
                } else if (ProfileActivity.this.isBot) {
                    bundle4.putLong("user_id", ProfileActivity.this.userId);
                }
                ChatEditActivity chatEditActivity = new ChatEditActivity(bundle4);
                if (ProfileActivity.this.chatInfo != null) {
                    chatEditActivity.setInfo(ProfileActivity.this.chatInfo);
                } else {
                    chatEditActivity.setInfo(ProfileActivity.this.userInfo);
                }
                ProfileActivity.this.presentFragment(chatEditActivity);
            } else if (i == 9) {
                final TLRPC$User user4 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                if (user4 == null) {
                    return;
                }
                Bundle bundle5 = new Bundle();
                bundle5.putBoolean("onlySelect", true);
                bundle5.putInt("dialogsType", 2);
                bundle5.putBoolean("resetDelegate", false);
                bundle5.putBoolean("closeFragment", false);
                final DialogsActivity dialogsActivity2 = new DialogsActivity(bundle5);
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda13
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                        boolean lambda$onItemClick$8;
                        lambda$onItemClick$8 = ProfileActivity.5.this.lambda$onItemClick$8(user4, dialogsActivity2, dialogsActivity3, arrayList, charSequence, z, topicsFragment);
                        return lambda$onItemClick$8;
                    }
                });
                ProfileActivity.this.presentFragment(dialogsActivity2);
            } else if (i == 10) {
                try {
                    if (ProfileActivity.this.userId != 0) {
                        TLRPC$User user5 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                        if (user5 == null) {
                            return;
                        }
                        if (ProfileActivity.this.botInfo != null && ProfileActivity.this.userInfo != null && !TextUtils.isEmpty(ProfileActivity.this.userInfo.about)) {
                            format = String.format("%s https://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", ProfileActivity.this.userInfo.about, UserObject.getPublicUsername(user5));
                        } else {
                            format = String.format("https://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", UserObject.getPublicUsername(user5));
                        }
                        str = format;
                    } else if (ProfileActivity.this.chatId != 0) {
                        TLRPC$Chat chat = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(ProfileActivity.this.chatId));
                        if (chat == null) {
                            return;
                        }
                        if (ProfileActivity.this.chatInfo != null && !TextUtils.isEmpty(ProfileActivity.this.chatInfo.about)) {
                            str = String.format("%s\nhttps://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", ProfileActivity.this.chatInfo.about, ChatObject.getPublicUsername(chat));
                        } else {
                            str = String.format("https://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", ChatObject.getPublicUsername(chat));
                        }
                    }
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", str);
                    ProfileActivity.this.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("BotShare", R.string.BotShare)), 500);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else if (i == 14) {
                try {
                    if (ProfileActivity.this.currentEncryptedChat != null) {
                        j = DialogObject.makeEncryptedDialogId(ProfileActivity.this.currentEncryptedChat.id);
                    } else if (ProfileActivity.this.userId != 0) {
                        j = ProfileActivity.this.userId;
                    } else if (ProfileActivity.this.chatId == 0) {
                        return;
                    } else {
                        j = -ProfileActivity.this.chatId;
                    }
                    ProfileActivity.this.getMediaDataController().installShortcut(j);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            } else if (i == 15 || i == 16) {
                if (ProfileActivity.this.userId != 0) {
                    TLRPC$User user6 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                    if (user6 != null) {
                        VoIPHelper.startCall(user6, i == 16, ProfileActivity.this.userInfo != null && ProfileActivity.this.userInfo.video_calls_available, ProfileActivity.this.getParentActivity(), ProfileActivity.this.userInfo, ProfileActivity.this.getAccountInstance());
                    }
                } else if (ProfileActivity.this.chatId != 0) {
                    if (ProfileActivity.this.getMessagesController().getGroupCall(ProfileActivity.this.chatId, false) != null) {
                        TLRPC$Chat tLRPC$Chat = ProfileActivity.this.currentChat;
                        Activity parentActivity = ProfileActivity.this.getParentActivity();
                        ProfileActivity profileActivity4 = ProfileActivity.this;
                        VoIPHelper.startCall(tLRPC$Chat, null, null, false, parentActivity, profileActivity4, profileActivity4.getAccountInstance());
                        return;
                    }
                    ProfileActivity profileActivity5 = ProfileActivity.this;
                    VoIPHelper.showGroupCallAlert(profileActivity5, profileActivity5.currentChat, null, false, ProfileActivity.this.getAccountInstance());
                }
            } else if (i == 17) {
                Bundle bundle6 = new Bundle();
                bundle6.putLong("chat_id", ProfileActivity.this.chatId);
                bundle6.putInt("type", 2);
                bundle6.putBoolean("open_search", true);
                ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle6);
                chatUsersActivity.setInfo(ProfileActivity.this.chatInfo);
                ProfileActivity.this.presentFragment(chatUsersActivity);
            } else if (i == 18) {
                ProfileActivity.this.openAddMember();
            } else if (i == 19) {
                TLRPC$Chat chat2 = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(ProfileActivity.this.chatId));
                Bundle bundle7 = new Bundle();
                bundle7.putLong("chat_id", ProfileActivity.this.chatId);
                bundle7.putBoolean("is_megagroup", chat2.megagroup);
                if (!ProfileActivity.this.chatInfo.can_view_stats) {
                    bundle7.putBoolean("only_boosts", true);
                }
                ProfileActivity.this.presentFragment(new StatisticActivity(bundle7));
            } else if (i == 22) {
                ProfileActivity.this.openDiscussion();
            } else if (i == 38) {
                ProfileActivity profileActivity6 = ProfileActivity.this;
                ProfileActivity profileActivity7 = ProfileActivity.this;
                profileActivity6.showDialog(new GiftPremiumBottomSheet(profileActivity7, profileActivity7.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId))));
            } else if (i == 39) {
                Bundle bundle8 = new Bundle();
                bundle8.putInt("type", 2);
                bundle8.putLong("dialog_id", -ProfileActivity.this.chatId);
                MediaActivity mediaActivity = new MediaActivity(bundle8, null);
                mediaActivity.setChatInfo(ProfileActivity.this.chatInfo);
                ProfileActivity.this.presentFragment(mediaActivity);
            } else if (i == 20) {
                AlertDialog.Builder builder4 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                builder4.setTitle(LocaleController.getString("AreYouSureSecretChatTitle", R.string.AreYouSureSecretChatTitle));
                builder4.setMessage(LocaleController.getString("AreYouSureSecretChat", R.string.AreYouSureSecretChat));
                builder4.setPositiveButton(LocaleController.getString("Start", R.string.Start), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        ProfileActivity.5.this.lambda$onItemClick$9(dialogInterface, i3);
                    }
                });
                builder4.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                ProfileActivity.this.showDialog(builder4.create());
            } else if (i == 21) {
                if (ProfileActivity.this.getParentActivity() == null) {
                    return;
                }
                int i3 = Build.VERSION.SDK_INT;
                if (i3 < 23 || ((i3 > 28 && !BuildVars.NO_SCOPED_STORAGE) || ProfileActivity.this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
                    ImageLocation imageLocation = ProfileActivity.this.avatarsViewPager.getImageLocation(ProfileActivity.this.avatarsViewPager.getRealPosition());
                    if (imageLocation == null) {
                        return;
                    }
                    final boolean z = imageLocation.imageType == 2;
                    File pathToAttach = FileLoader.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getPathToAttach(imageLocation.location, z ? "mp4" : null, true);
                    if (pathToAttach.exists()) {
                        MediaController.saveFile(pathToAttach.toString(), ProfileActivity.this.getParentActivity(), 0, null, null, new Utilities.Callback() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda11
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                ProfileActivity.5.this.lambda$onItemClick$10(z, (Uri) obj);
                            }
                        });
                        return;
                    }
                    return;
                }
                ProfileActivity.this.getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
            } else if (i == 30) {
                ProfileActivity profileActivity8 = ProfileActivity.this;
                profileActivity8.presentFragment(new ChangeNameActivity(profileActivity8.resourcesProvider));
            } else if (i == 40) {
                ProfileActivity.this.presentFragment(new PeerColorActivity(0L).startOnProfile().setOnApplied(ProfileActivity.this));
            } else if (i == 31) {
                ProfileActivity.this.presentFragment(new LogoutActivity());
            } else if (i == 33) {
                int realPosition = ProfileActivity.this.avatarsViewPager.getRealPosition();
                final TLRPC$Photo photo = ProfileActivity.this.avatarsViewPager.getPhoto(realPosition);
                if (photo == null) {
                    return;
                }
                ProfileActivity.this.avatarsViewPager.startMovePhotoToBegin(realPosition);
                TLRPC$TL_photos_updateProfilePhoto tLRPC$TL_photos_updateProfilePhoto = new TLRPC$TL_photos_updateProfilePhoto();
                TLRPC$TL_inputPhoto tLRPC$TL_inputPhoto = new TLRPC$TL_inputPhoto();
                tLRPC$TL_photos_updateProfilePhoto.id = tLRPC$TL_inputPhoto;
                tLRPC$TL_inputPhoto.id = photo.id;
                tLRPC$TL_inputPhoto.access_hash = photo.access_hash;
                tLRPC$TL_inputPhoto.file_reference = photo.file_reference;
                final UserConfig userConfig = ProfileActivity.this.getUserConfig();
                ProfileActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_photos_updateProfilePhoto, new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda12
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        ProfileActivity.5.this.lambda$onItemClick$12(userConfig, photo, tLObject, tLRPC$TL_error);
                    }
                });
                ProfileActivity.this.undoView.showWithAction(ProfileActivity.this.userId, 22, photo.video_sizes.isEmpty() ? null : 1);
                TLRPC$User user7 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(userConfig.clientUserId));
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 800);
                if (user7 != null) {
                    TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 90);
                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = user7.photo;
                    tLRPC$UserProfilePhoto.photo_id = photo.id;
                    tLRPC$UserProfilePhoto.photo_small = closestPhotoSizeWithSize2.location;
                    tLRPC$UserProfilePhoto.photo_big = closestPhotoSizeWithSize.location;
                    userConfig.setCurrentUser(user7);
                    userConfig.saveConfig(true);
                    NotificationCenter.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
                    ProfileActivity.this.updateProfileData(true);
                }
                ProfileActivity.this.avatarsViewPager.commitMoveToBegin();
            } else if (i == 34) {
                int realPosition2 = ProfileActivity.this.avatarsViewPager.getRealPosition();
                ImageLocation imageLocation2 = ProfileActivity.this.avatarsViewPager.getImageLocation(realPosition2);
                if (imageLocation2 == null) {
                    return;
                }
                File pathToAttach2 = FileLoader.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getPathToAttach(PhotoViewer.getFileLocation(imageLocation2), PhotoViewer.getFileLocationExt(imageLocation2), true);
                boolean z2 = imageLocation2.imageType == 2;
                if (z2) {
                    ImageLocation realImageLocation = ProfileActivity.this.avatarsViewPager.getRealImageLocation(realPosition2);
                    str2 = FileLoader.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getPathToAttach(PhotoViewer.getFileLocation(realImageLocation), PhotoViewer.getFileLocationExt(realImageLocation), true).getAbsolutePath();
                }
                ProfileActivity.this.imageUpdater.openPhotoForEdit(pathToAttach2.getAbsolutePath(), str2, 0, z2);
            } else if (i != 35) {
                if (i == 36) {
                    ProfileActivity.this.onWriteButtonClick();
                } else if (i != 37 || ProfileActivity.this.qrItem == null || ProfileActivity.this.qrItem.getAlpha() <= 0.0f) {
                } else {
                    Bundle bundle9 = new Bundle();
                    bundle9.putLong("chat_id", ProfileActivity.this.chatId);
                    bundle9.putLong("user_id", ProfileActivity.this.userId);
                    ProfileActivity.this.presentFragment(new QrActivity(bundle9));
                }
            } else {
                AlertDialog.Builder builder5 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                ImageLocation imageLocation3 = ProfileActivity.this.avatarsViewPager.getImageLocation(ProfileActivity.this.avatarsViewPager.getRealPosition());
                if (imageLocation3 == null) {
                    return;
                }
                if (imageLocation3.imageType == 2) {
                    builder5.setTitle(LocaleController.getString("AreYouSureDeleteVideoTitle", R.string.AreYouSureDeleteVideoTitle));
                    builder5.setMessage(LocaleController.formatString("AreYouSureDeleteVideo", R.string.AreYouSureDeleteVideo, new Object[0]));
                } else {
                    builder5.setTitle(LocaleController.getString("AreYouSureDeletePhotoTitle", R.string.AreYouSureDeletePhotoTitle));
                    builder5.setMessage(LocaleController.formatString("AreYouSureDeletePhoto", R.string.AreYouSureDeletePhoto, new Object[0]));
                }
                builder5.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        ProfileActivity.5.this.lambda$onItemClick$13(dialogInterface, i4);
                    }
                });
                builder5.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create4 = builder5.create();
                ProfileActivity.this.showDialog(create4);
                TextView textView4 = (TextView) create4.getButton(-1);
                if (textView4 != null) {
                    textView4.setTextColor(ProfileActivity.this.getThemedColor(Theme.key_text_RedBold));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(int i) {
            if (i == 1) {
                NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
                ProfileActivity profileActivity = ProfileActivity.this;
                int i2 = NotificationCenter.closeChats;
                notificationCenter.removeObserver(profileActivity, i2);
                ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i2, new Object[0]);
                ProfileActivity.this.playProfileAnimation = 0;
                ProfileActivity.this.finishFragment();
                return;
            }
            ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.peerSettingsDidLoad, Long.valueOf(ProfileActivity.this.userId));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$1(DialogInterface dialogInterface, int i) {
            ProfileActivity.this.getMessagesController().blockPeer(ProfileActivity.this.userId);
            if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                BulletinFactory.createBanBulletin(ProfileActivity.this, true).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(TLRPC$User tLRPC$User, boolean z) {
            List<BaseFragment> fragmentStack = ProfileActivity.this.getParentLayout().getFragmentStack();
            if (((fragmentStack == null || fragmentStack.size() < 2) ? null : fragmentStack.get(fragmentStack.size() - 2)) instanceof ChatActivity) {
                ProfileActivity.this.getParentLayout().removeFragmentFromStack(fragmentStack.size() - 2);
            }
            ProfileActivity.this.finishFragment();
            ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needDeleteDialog, Long.valueOf(ProfileActivity.this.dialogId), tLRPC$User, ProfileActivity.this.currentChat, Boolean.valueOf(z));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$3() {
            ProfileActivity.this.getSendMessagesHelper().sendMessage(SendMessagesHelper.SendMessageParams.of("/start", ProfileActivity.this.userId, null, null, null, false, null, null, null, true, 0, null, false));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$4(TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
            ArrayList<TLRPC$User> arrayList = new ArrayList<>();
            arrayList.add(tLRPC$User);
            ProfileActivity.this.getContactsController().deleteContact(arrayList, true);
            if (tLRPC$User != null) {
                tLRPC$User.contact = false;
                ProfileActivity.this.updateListAnimated(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onItemClick$8(final TLRPC$User tLRPC$User, final DialogsActivity dialogsActivity, final DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
            final long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
            TLRPC$Chat chat = MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getChat(Long.valueOf(-j));
            if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
                ProfileActivity.this.getMessagesController().checkIsInChat(false, chat, tLRPC$User, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda8
                    @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                    public final void run(boolean z2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, String str) {
                        ProfileActivity.5.this.lambda$onItemClick$6(j, dialogsActivity, z2, tLRPC$TL_chatAdminRights2, str);
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                int i = R.string.AddBot;
                builder.setTitle(LocaleController.getString("AddBot", i));
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, UserObject.getUserName(tLRPC$User), chat == null ? "" : chat.title)));
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                builder.setPositiveButton(LocaleController.getString("AddBot", i), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        ProfileActivity.5.this.lambda$onItemClick$7(j, dialogsActivity2, tLRPC$User, dialogInterface, i2);
                    }
                });
                ProfileActivity.this.showDialog(builder.create());
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$6(final long j, final DialogsActivity dialogsActivity, final boolean z, final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.5.this.lambda$onItemClick$5(j, tLRPC$TL_chatAdminRights, str, z, dialogsActivity);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$5(long j, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, String str, boolean z, final DialogsActivity dialogsActivity) {
            ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(ProfileActivity.this.userId, -j, tLRPC$TL_chatAdminRights, null, null, str, 2, true, !z, null);
            chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ProfileActivity.5.3
                @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                public void didChangeOwner(TLRPC$User tLRPC$User) {
                }

                @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str2) {
                    ProfileActivity.this.disableProfileAnimation = true;
                    dialogsActivity.removeSelfFromStack();
                    NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
                    ProfileActivity profileActivity = ProfileActivity.this;
                    int i2 = NotificationCenter.closeChats;
                    notificationCenter.removeObserver(profileActivity, i2);
                    ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i2, new Object[0]);
                }
            });
            ProfileActivity.this.presentFragment(chatRightsEditActivity);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$7(long j, DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
            ProfileActivity.this.disableProfileAnimation = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            long j2 = -j;
            bundle.putLong("chat_id", j2);
            if (ProfileActivity.this.getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
                ChatActivity chatActivity = new ChatActivity(bundle);
                NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
                ProfileActivity profileActivity = ProfileActivity.this;
                int i2 = NotificationCenter.closeChats;
                notificationCenter.removeObserver(profileActivity, i2);
                ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i2, new Object[0]);
                ProfileActivity.this.getMessagesController().addUserToChat(j2, tLRPC$User, 0, null, chatActivity, true, null, null);
                ProfileActivity.this.presentFragment(chatActivity, true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$9(DialogInterface dialogInterface, int i) {
            ProfileActivity.this.creatingChat = true;
            ProfileActivity.this.getSecretChatHelper().startSecretChat(ProfileActivity.this.getParentActivity(), ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$10(boolean z, Uri uri) {
            if (ProfileActivity.this.getParentActivity() == null) {
                return;
            }
            BulletinFactory.createSaveToGalleryBulletin(ProfileActivity.this, z, (Theme.ResourcesProvider) null).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$12(final UserConfig userConfig, final TLRPC$Photo tLRPC$Photo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$5$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.5.this.lambda$onItemClick$11(tLObject, userConfig, tLRPC$Photo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$11(TLObject tLObject, UserConfig userConfig, TLRPC$Photo tLRPC$Photo) {
            ProfileActivity.this.avatarsViewPager.finishSettingMainPhoto();
            if (tLObject instanceof TLRPC$TL_photos_photo) {
                TLRPC$TL_photos_photo tLRPC$TL_photos_photo = (TLRPC$TL_photos_photo) tLObject;
                ProfileActivity.this.getMessagesController().putUsers(tLRPC$TL_photos_photo.users, false);
                TLRPC$User user = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(userConfig.clientUserId));
                if (tLRPC$TL_photos_photo.photo instanceof TLRPC$TL_photo) {
                    ProfileActivity.this.avatarsViewPager.replaceFirstPhoto(tLRPC$Photo, tLRPC$TL_photos_photo.photo);
                    if (user != null) {
                        user.photo.photo_id = tLRPC$TL_photos_photo.photo.id;
                        userConfig.setCurrentUser(user);
                        userConfig.saveConfig(true);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$13(DialogInterface dialogInterface, int i) {
            TLRPC$Photo tLRPC$Photo;
            int realPosition = ProfileActivity.this.avatarsViewPager.getRealPosition();
            TLRPC$Photo photo = ProfileActivity.this.avatarsViewPager.getPhoto(realPosition);
            TLRPC$UserFull userInfo = ProfileActivity.this.getUserInfo();
            if (ProfileActivity.this.avatar == null || realPosition != 0) {
                if (ProfileActivity.this.hasFallbackPhoto && photo != null && userInfo != null && (tLRPC$Photo = userInfo.fallback_photo) != null && tLRPC$Photo.id == photo.id) {
                    userInfo.fallback_photo = null;
                    userInfo.flags &= -4194305;
                    ProfileActivity.this.getMessagesStorage().updateUserInfo(userInfo, true);
                    ProfileActivity.this.updateProfileData(false);
                }
                if (ProfileActivity.this.avatarsViewPager.getRealCount() == 1) {
                    ProfileActivity.this.setForegroundImage(true);
                }
                if (photo == null || ProfileActivity.this.avatarsViewPager.getRealPosition() == 0) {
                    TLRPC$Photo photo2 = ProfileActivity.this.avatarsViewPager.getPhoto(1);
                    if (photo2 != null) {
                        ProfileActivity.this.getUserConfig().getCurrentUser().photo = new TLRPC$TL_userProfilePhoto();
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, 90);
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, 1000);
                        if (closestPhotoSizeWithSize != null && closestPhotoSizeWithSize2 != null) {
                            ProfileActivity.this.getUserConfig().getCurrentUser().photo.photo_small = closestPhotoSizeWithSize.location;
                            ProfileActivity.this.getUserConfig().getCurrentUser().photo.photo_big = closestPhotoSizeWithSize2.location;
                        }
                    } else {
                        ProfileActivity.this.getUserConfig().getCurrentUser().photo = new TLRPC$TL_userProfilePhotoEmpty();
                    }
                    ProfileActivity.this.getMessagesController().deleteUserPhoto(null);
                } else {
                    TLRPC$TL_inputPhoto tLRPC$TL_inputPhoto = new TLRPC$TL_inputPhoto();
                    tLRPC$TL_inputPhoto.id = photo.id;
                    tLRPC$TL_inputPhoto.access_hash = photo.access_hash;
                    byte[] bArr = photo.file_reference;
                    tLRPC$TL_inputPhoto.file_reference = bArr;
                    if (bArr == null) {
                        tLRPC$TL_inputPhoto.file_reference = new byte[0];
                    }
                    ProfileActivity.this.getMessagesController().deleteUserPhoto(tLRPC$TL_inputPhoto);
                    ProfileActivity.this.getMessagesStorage().clearUserPhoto(ProfileActivity.this.userId, photo.id);
                }
                if (ProfileActivity.this.avatarsViewPager.removePhotoAtIndex(realPosition)) {
                    ProfileActivity.this.avatarsViewPager.setVisibility(8);
                    ProfileActivity.this.avatarImage.setForegroundAlpha(1.0f);
                    ProfileActivity.this.avatarContainer.setVisibility(0);
                    ProfileActivity.this.doNotSetForeground = true;
                    View findViewByPosition = ProfileActivity.this.layoutManager.findViewByPosition(0);
                    if (findViewByPosition != null) {
                        ProfileActivity.this.listView.smoothScrollBy(0, findViewByPosition.getTop() - AndroidUtilities.dp(88.0f), CubicBezierInterpolator.EASE_OUT_QUINT);
                        return;
                    }
                    return;
                }
                return;
            }
            ProfileActivity.this.imageUpdater.cancel();
            ProfileActivity profileActivity = ProfileActivity.this;
            if (profileActivity.avatarUploadingRequest != 0) {
                profileActivity.getConnectionsManager().cancelRequest(ProfileActivity.this.avatarUploadingRequest, true);
            }
            ProfileActivity.this.allowPullingDown = (AndroidUtilities.isTablet() || ProfileActivity.this.isInLandscapeMode || !ProfileActivity.this.avatarImage.getImageReceiver().hasNotThumb() || AndroidUtilities.isAccessibilityScreenReaderEnabled()) ? false : true;
            ProfileActivity.this.avatar = null;
            ProfileActivity.this.avatarBig = null;
            ProfileActivity.this.avatarsViewPager.scrolledByUser = true;
            ProfileActivity.this.avatarsViewPager.removeUploadingImage(ProfileActivity.this.uploadingImageLocation);
            ProfileActivity.this.avatarsViewPager.setCreateThumbFromParent(false);
            ProfileActivity.this.updateProfileData(true);
            ProfileActivity.this.showAvatarProgress(false, true);
            ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_ALL));
            ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
            ProfileActivity.this.getUserConfig().saveConfig(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 6 extends NestedFrameLayout {
        private Paint grayPaint;
        private boolean ignoreLayout;
        private final ArrayList<View> sortedChildren;
        private final Comparator<View> viewComparator;
        private boolean wasPortrait;

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        6(Context context) {
            super(context);
            this.grayPaint = new Paint();
            this.sortedChildren = new ArrayList<>();
            this.viewComparator = ProfileActivity$6$$ExternalSyntheticLambda1.INSTANCE;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!ProfileActivity.this.pinchToZoomHelper.isInOverlayMode()) {
                if (ProfileActivity.this.sharedMediaLayout == null || !ProfileActivity.this.sharedMediaLayout.isInFastScroll() || !ProfileActivity.this.sharedMediaLayout.isPinnedToTop()) {
                    if (ProfileActivity.this.sharedMediaLayout == null || !ProfileActivity.this.sharedMediaLayout.checkPinchToZoom(motionEvent)) {
                        return super.dispatchTouchEvent(motionEvent);
                    }
                    return true;
                }
                return ProfileActivity.this.sharedMediaLayout.dispatchFastScrollEvent(motionEvent);
            }
            return ProfileActivity.this.pinchToZoomHelper.onTouchEvent(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            boolean z;
            int dp;
            int i3;
            View view;
            int i4;
            boolean z2;
            int measuredWidth;
            int max;
            int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);
            if (ProfileActivity.this.listView != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ProfileActivity.this.listView.getLayoutParams();
                if (layoutParams.topMargin != currentActionBarHeight) {
                    layoutParams.topMargin = currentActionBarHeight;
                }
            }
            if (ProfileActivity.this.searchListView != null) {
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) ProfileActivity.this.searchListView.getLayoutParams();
                if (layoutParams2.topMargin != currentActionBarHeight) {
                    layoutParams2.topMargin = currentActionBarHeight;
                }
            }
            int size = View.MeasureSpec.getSize(i2);
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            if (ProfileActivity.this.lastMeasuredContentWidth == getMeasuredWidth() && ProfileActivity.this.lastMeasuredContentHeight == getMeasuredHeight()) {
                z = false;
            } else {
                z = (ProfileActivity.this.lastMeasuredContentWidth == 0 || ProfileActivity.this.lastMeasuredContentWidth == getMeasuredWidth()) ? false : true;
                ProfileActivity.this.listContentHeight = 0;
                int itemCount = ProfileActivity.this.listAdapter.getItemCount();
                ProfileActivity.this.lastMeasuredContentWidth = getMeasuredWidth();
                ProfileActivity.this.lastMeasuredContentHeight = getMeasuredHeight();
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
                int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(ProfileActivity.this.listView.getMeasuredHeight(), 0);
                ProfileActivity.this.positionToOffset.clear();
                for (int i5 = 0; i5 < itemCount; i5++) {
                    int itemViewType = ProfileActivity.this.listAdapter.getItemViewType(i5);
                    ProfileActivity.this.positionToOffset.put(Integer.valueOf(i5), Integer.valueOf(ProfileActivity.this.listContentHeight));
                    if (itemViewType != 13) {
                        RecyclerView.ViewHolder createViewHolder = ProfileActivity.this.listAdapter.createViewHolder(null, itemViewType);
                        ProfileActivity.this.listAdapter.onBindViewHolder(createViewHolder, i5);
                        createViewHolder.itemView.measure(makeMeasureSpec, makeMeasureSpec2);
                        ProfileActivity.access$9512(ProfileActivity.this, createViewHolder.itemView.getMeasuredHeight());
                    } else {
                        ProfileActivity profileActivity = ProfileActivity.this;
                        ProfileActivity.access$9512(profileActivity, profileActivity.listView.getMeasuredHeight());
                    }
                }
                if (ProfileActivity.this.emptyView != null) {
                    ((FrameLayout.LayoutParams) ProfileActivity.this.emptyView.getLayoutParams()).topMargin = AndroidUtilities.dp(88.0f) + AndroidUtilities.statusBarHeight;
                }
            }
            ProfileActivity profileActivity2 = ProfileActivity.this;
            if (profileActivity2.previousTransitionFragment != null) {
                profileActivity2.nameTextView[0].setRightPadding(ProfileActivity.this.nameTextView[0].getMeasuredWidth() - ProfileActivity.this.previousTransitionFragment.getAvatarContainer().getTitleTextView().getMeasuredWidth());
            }
            if (ProfileActivity.this.fragmentOpened || (!ProfileActivity.this.expandPhoto && (!ProfileActivity.this.openAnimationInProgress || ProfileActivity.this.playProfileAnimation != 2))) {
                if (ProfileActivity.this.fragmentOpened && !ProfileActivity.this.openAnimationInProgress && !ProfileActivity.this.firstLayout) {
                    boolean z3 = true;
                    this.ignoreLayout = true;
                    if (!ProfileActivity.this.isInLandscapeMode && !AndroidUtilities.isTablet()) {
                        dp = ProfileActivity.this.listView.getMeasuredWidth();
                        i3 = Math.max(0, getMeasuredHeight() - ((ProfileActivity.this.listContentHeight + AndroidUtilities.dp(88.0f)) + currentActionBarHeight));
                    } else {
                        dp = AndroidUtilities.dp(88.0f);
                        i3 = 0;
                    }
                    if (ProfileActivity.this.banFromGroup == 0) {
                        ProfileActivity.this.listView.setBottomGlowOffset(0);
                    } else {
                        i3 += AndroidUtilities.dp(48.0f);
                        ProfileActivity.this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
                    }
                    int paddingTop = ProfileActivity.this.listView.getPaddingTop();
                    int i6 = 0;
                    while (true) {
                        if (i6 >= ProfileActivity.this.listView.getChildCount()) {
                            view = null;
                            i4 = -1;
                            break;
                        }
                        int childAdapterPosition = ProfileActivity.this.listView.getChildAdapterPosition(ProfileActivity.this.listView.getChildAt(i6));
                        if (childAdapterPosition != -1) {
                            View childAt = ProfileActivity.this.listView.getChildAt(i6);
                            i4 = childAdapterPosition;
                            view = childAt;
                            break;
                        }
                        i6++;
                    }
                    if (view == null && (view = ProfileActivity.this.listView.getChildAt(0)) != null) {
                        RecyclerView.ViewHolder findContainingViewHolder = ProfileActivity.this.listView.findContainingViewHolder(view);
                        int adapterPosition = findContainingViewHolder.getAdapterPosition();
                        i4 = adapterPosition == -1 ? findContainingViewHolder.getPosition() : adapterPosition;
                    }
                    int top = view != null ? view.getTop() : dp;
                    if ((((BaseFragment) ProfileActivity.this).actionBar.isSearchFieldVisible() || ProfileActivity.this.openSimilar) && ProfileActivity.this.sharedMediaRow >= 0) {
                        ProfileActivity.this.layoutManager.scrollToPositionWithOffset(ProfileActivity.this.sharedMediaRow, -dp);
                    } else {
                        if (ProfileActivity.this.invalidateScroll || paddingTop != dp) {
                            ProfileActivity profileActivity3 = ProfileActivity.this;
                            if (profileActivity3.savedScrollPosition >= 0) {
                                LinearLayoutManager linearLayoutManager = profileActivity3.layoutManager;
                                ProfileActivity profileActivity4 = ProfileActivity.this;
                                linearLayoutManager.scrollToPositionWithOffset(profileActivity4.savedScrollPosition, profileActivity4.savedScrollOffset - dp);
                            } else if ((!z || !profileActivity3.allowPullingDown) && view != null) {
                                if (i4 == 0 && !ProfileActivity.this.allowPullingDown && top > AndroidUtilities.dp(88.0f)) {
                                    top = AndroidUtilities.dp(88.0f);
                                }
                                ProfileActivity.this.layoutManager.scrollToPositionWithOffset(i4, top - dp);
                                z3 = true;
                            } else {
                                ProfileActivity.this.layoutManager.scrollToPositionWithOffset(0, AndroidUtilities.dp(88.0f) - dp);
                            }
                        }
                        z3 = false;
                    }
                    if (paddingTop == dp && ProfileActivity.this.listView.getPaddingBottom() == i3) {
                        z2 = z3;
                    } else {
                        ProfileActivity.this.listView.setPadding(0, dp, 0, i3);
                        z2 = true;
                    }
                    if (z2) {
                        measureChildWithMargins(ProfileActivity.this.listView, i, 0, i2, 0);
                        try {
                            ProfileActivity.this.listView.layout(0, currentActionBarHeight, ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getMeasuredHeight() + currentActionBarHeight);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    this.ignoreLayout = false;
                }
            } else {
                this.ignoreLayout = true;
                if (ProfileActivity.this.expandPhoto) {
                    if (ProfileActivity.this.searchItem != null) {
                        ProfileActivity.this.searchItem.setAlpha(0.0f);
                        ProfileActivity.this.searchItem.setEnabled(false);
                        ProfileActivity.this.searchItem.setVisibility(8);
                    }
                    ProfileActivity.this.nameTextView[1].setTextColor(-1);
                    ProfileActivity.this.nameTextView[1].setPivotY(ProfileActivity.this.nameTextView[1].getMeasuredHeight());
                    ProfileActivity.this.nameTextView[1].setScaleX(1.67f);
                    ProfileActivity.this.nameTextView[1].setScaleY(1.67f);
                    if (ProfileActivity.this.scamDrawable != null) {
                        ProfileActivity.this.scamDrawable.setColor(Color.argb(179, 255, 255, 255));
                    }
                    if (ProfileActivity.this.lockIconDrawable != null) {
                        ProfileActivity.this.lockIconDrawable.setColorFilter(-1, PorterDuff.Mode.MULTIPLY);
                    }
                    if (ProfileActivity.this.verifiedCrossfadeDrawable[0] != null) {
                        ProfileActivity.this.verifiedCrossfadeDrawable[0].setProgress(1.0f);
                    }
                    if (ProfileActivity.this.verifiedCrossfadeDrawable[1] != null) {
                        ProfileActivity.this.verifiedCrossfadeDrawable[1].setProgress(1.0f);
                    }
                    if (ProfileActivity.this.premiumCrossfadeDrawable[0] != null) {
                        ProfileActivity.this.premiumCrossfadeDrawable[0].setProgress(1.0f);
                    }
                    if (ProfileActivity.this.premiumCrossfadeDrawable[1] != null) {
                        ProfileActivity.this.premiumCrossfadeDrawable[1].setProgress(1.0f);
                    }
                    ProfileActivity.this.updateEmojiStatusDrawableColor(1.0f);
                    ProfileActivity.this.onlineTextView[1].setTextColor(-1275068417);
                    ((BaseFragment) ProfileActivity.this).actionBar.setItemsBackgroundColor(1090519039, false);
                    ((BaseFragment) ProfileActivity.this).actionBar.setItemsColor(-1, false);
                    ProfileActivity.this.overlaysView.setOverlaysVisible();
                    ProfileActivity.this.overlaysView.setAlphaValue(1.0f, false);
                    ProfileActivity.this.avatarImage.setForegroundAlpha(1.0f);
                    ProfileActivity.this.avatarContainer.setVisibility(8);
                    ProfileActivity.this.avatarsViewPager.resetCurrentItem();
                    ProfileActivity.this.avatarsViewPager.setVisibility(0);
                    if (ProfileActivity.this.storyView != null) {
                        ProfileActivity.this.storyView.setExpandProgress(1.0f);
                    }
                    ProfileActivity.this.expandPhoto = false;
                }
                ProfileActivity.this.allowPullingDown = true;
                ProfileActivity.this.isPulledDown = true;
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
                if (ProfileActivity.this.otherItem != null) {
                    if (!ProfileActivity.this.getMessagesController().isChatNoForwards(ProfileActivity.this.currentChat)) {
                        ProfileActivity.this.otherItem.showSubItem(21);
                    } else {
                        ProfileActivity.this.otherItem.hideSubItem(21);
                    }
                    if (ProfileActivity.this.imageUpdater != null) {
                        ProfileActivity.this.otherItem.showSubItem(34);
                        ProfileActivity.this.otherItem.showSubItem(35);
                        ProfileActivity.this.otherItem.hideSubItem(31);
                    }
                }
                ProfileActivity.this.currentExpanAnimatorFracture = 1.0f;
                if (!ProfileActivity.this.isInLandscapeMode) {
                    measuredWidth = ProfileActivity.this.listView.getMeasuredWidth();
                    max = Math.max(0, getMeasuredHeight() - ((ProfileActivity.this.listContentHeight + AndroidUtilities.dp(88.0f)) + currentActionBarHeight));
                } else {
                    measuredWidth = AndroidUtilities.dp(88.0f);
                    max = 0;
                }
                if (ProfileActivity.this.banFromGroup == 0) {
                    ProfileActivity.this.listView.setBottomGlowOffset(0);
                } else {
                    max += AndroidUtilities.dp(48.0f);
                    ProfileActivity.this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
                }
                ProfileActivity.this.initialAnimationExtraHeight = measuredWidth - currentActionBarHeight;
                if (ProfileActivity.this.playProfileAnimation == 0) {
                    ProfileActivity profileActivity5 = ProfileActivity.this;
                    profileActivity5.extraHeight = profileActivity5.initialAnimationExtraHeight;
                }
                ProfileActivity.this.layoutManager.scrollToPositionWithOffset(0, -currentActionBarHeight);
                ProfileActivity.this.listView.setPadding(0, measuredWidth, 0, max);
                measureChildWithMargins(ProfileActivity.this.listView, i, 0, i2, 0);
                ProfileActivity.this.listView.layout(0, currentActionBarHeight, ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getMeasuredHeight() + currentActionBarHeight);
                this.ignoreLayout = false;
            }
            boolean z4 = size > View.MeasureSpec.getSize(i);
            if (z4 != this.wasPortrait) {
                post(new Runnable() { // from class: org.telegram.ui.ProfileActivity$6$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.6.this.lambda$onMeasure$0();
                    }
                });
                this.wasPortrait = z4;
            }
            if (ProfileActivity.this.searchItem == null || ProfileActivity.this.qrItem == null) {
                return;
            }
            AndroidUtilities.dp(48.0f);
            float unused = ProfileActivity.this.currentExpandAnimatorValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$0() {
            if (ProfileActivity.this.selectAnimatedEmojiDialog != null) {
                ProfileActivity.this.selectAnimatedEmojiDialog.dismiss();
                ProfileActivity.this.selectAnimatedEmojiDialog = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ProfileActivity profileActivity = ProfileActivity.this;
            profileActivity.savedScrollPosition = -1;
            profileActivity.firstLayout = false;
            ProfileActivity.this.invalidateScroll = false;
            ProfileActivity.this.checkListViewScroll();
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$$1(View view, View view2) {
            return (int) (view.getY() - view2.getY());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            int top;
            FragmentContextView fragmentContextView;
            boolean z;
            int i;
            ProfileActivity.this.whitePaint.setColor(ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
            if (ProfileActivity.this.listView.getVisibility() != 0) {
                canvas.drawRect(0.0f, ProfileActivity.this.searchListView.getTop() + ProfileActivity.this.extraHeight + ProfileActivity.this.searchTransitionOffset, getMeasuredWidth(), top + getMeasuredHeight(), ProfileActivity.this.whitePaint);
            } else {
                this.grayPaint.setColor(ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundGray));
                if (ProfileActivity.this.transitionAnimationInProress) {
                    ProfileActivity.this.whitePaint.setAlpha((int) (ProfileActivity.this.listView.getAlpha() * 255.0f));
                }
                if (ProfileActivity.this.transitionAnimationInProress) {
                    this.grayPaint.setAlpha((int) (ProfileActivity.this.listView.getAlpha() * 255.0f));
                }
                int childCount = ProfileActivity.this.listView.getChildCount();
                this.sortedChildren.clear();
                boolean z2 = false;
                for (int i2 = 0; i2 < childCount; i2++) {
                    if (ProfileActivity.this.listView.getChildAdapterPosition(ProfileActivity.this.listView.getChildAt(i2)) != -1) {
                        this.sortedChildren.add(ProfileActivity.this.listView.getChildAt(i2));
                    } else {
                        z2 = true;
                    }
                }
                Collections.sort(this.sortedChildren, this.viewComparator);
                float y = ProfileActivity.this.listView.getY();
                int size = this.sortedChildren.size();
                if (!ProfileActivity.this.openAnimationInProgress && size > 0 && !z2) {
                    y += this.sortedChildren.get(0).getY();
                }
                float f = y;
                boolean z3 = false;
                float f2 = 1.0f;
                for (int i3 = 0; i3 < size; i3++) {
                    View view = this.sortedChildren.get(i3);
                    boolean z4 = view.getBackground() != null;
                    int y2 = (int) (ProfileActivity.this.listView.getY() + view.getY());
                    if (z3 == z4) {
                        if (view.getAlpha() == 1.0f) {
                            f2 = 1.0f;
                        }
                    } else {
                        if (z3) {
                            z = z4;
                            canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), y2, this.grayPaint);
                            i = y2;
                        } else {
                            z = z4;
                            if (f2 != 1.0f) {
                                float f3 = y2;
                                i = y2;
                                canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), f3, this.grayPaint);
                                ProfileActivity.this.whitePaint.setAlpha((int) (f2 * 255.0f));
                                canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), f3, ProfileActivity.this.whitePaint);
                                ProfileActivity.this.whitePaint.setAlpha(255);
                            } else {
                                i = y2;
                                canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), i, ProfileActivity.this.whitePaint);
                            }
                        }
                        f = i;
                        f2 = view.getAlpha();
                        z3 = z;
                    }
                }
                if (z3) {
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), this.grayPaint);
                } else if (f2 != 1.0f) {
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), this.grayPaint);
                    ProfileActivity.this.whitePaint.setAlpha((int) (f2 * 255.0f));
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), ProfileActivity.this.whitePaint);
                    ProfileActivity.this.whitePaint.setAlpha(255);
                } else {
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), ProfileActivity.this.whitePaint);
                }
            }
            super.dispatchDraw(canvas);
            ProfileActivity profileActivity = ProfileActivity.this;
            if (profileActivity.profileTransitionInProgress && ((BaseFragment) profileActivity).parentLayout.getFragmentStack().size() > 1) {
                BaseFragment baseFragment = ((BaseFragment) ProfileActivity.this).parentLayout.getFragmentStack().get(((BaseFragment) ProfileActivity.this).parentLayout.getFragmentStack().size() - 2);
                if ((baseFragment instanceof ChatActivity) && (fragmentContextView = ((ChatActivity) baseFragment).getFragmentContextView()) != null && fragmentContextView.isCallStyle()) {
                    float dpf2 = ProfileActivity.this.extraHeight / AndroidUtilities.dpf2(fragmentContextView.getStyleHeight());
                    if (dpf2 > 1.0f) {
                        dpf2 = 1.0f;
                    }
                    canvas.save();
                    canvas.translate(fragmentContextView.getX(), fragmentContextView.getY());
                    fragmentContextView.setDrawOverlay(true);
                    fragmentContextView.setCollapseTransition(true, ProfileActivity.this.extraHeight, dpf2);
                    fragmentContextView.draw(canvas);
                    fragmentContextView.setCollapseTransition(false, ProfileActivity.this.extraHeight, dpf2);
                    fragmentContextView.setDrawOverlay(false);
                    canvas.restore();
                }
            }
            if (ProfileActivity.this.scrimPaint.getAlpha() > 0) {
                canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), ProfileActivity.this.scrimPaint);
            }
            if (ProfileActivity.this.scrimView != null) {
                int save = canvas.save();
                canvas.translate(ProfileActivity.this.scrimView.getLeft(), ProfileActivity.this.scrimView.getTop());
                if (ProfileActivity.this.scrimView == ((BaseFragment) ProfileActivity.this).actionBar.getBackButton()) {
                    int alpha = ProfileActivity.this.actionBarBackgroundPaint.getAlpha();
                    ProfileActivity.this.actionBarBackgroundPaint.setAlpha((int) ((alpha * (ProfileActivity.this.scrimPaint.getAlpha() / 255.0f)) / 0.3f));
                    float max = Math.max(ProfileActivity.this.scrimView.getMeasuredWidth(), ProfileActivity.this.scrimView.getMeasuredHeight()) / 2;
                    canvas.drawCircle(max, max, 0.7f * max, ProfileActivity.this.actionBarBackgroundPaint);
                    ProfileActivity.this.actionBarBackgroundPaint.setAlpha(alpha);
                }
                ProfileActivity.this.scrimView.draw(canvas);
                canvas.restoreToCount(save);
            }
            if (ProfileActivity.this.blurredView == null || ProfileActivity.this.blurredView.getVisibility() != 0) {
                return;
            }
            if (ProfileActivity.this.blurredView.getAlpha() != 1.0f) {
                if (ProfileActivity.this.blurredView.getAlpha() != 0.0f) {
                    canvas.saveLayerAlpha(ProfileActivity.this.blurredView.getLeft(), ProfileActivity.this.blurredView.getTop(), ProfileActivity.this.blurredView.getRight(), ProfileActivity.this.blurredView.getBottom(), (int) (ProfileActivity.this.blurredView.getAlpha() * 255.0f), 31);
                    canvas.translate(ProfileActivity.this.blurredView.getLeft(), ProfileActivity.this.blurredView.getTop());
                    ProfileActivity.this.blurredView.draw(canvas);
                    canvas.restore();
                    return;
                }
                return;
            }
            ProfileActivity.this.blurredView.draw(canvas);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if ((ProfileActivity.this.pinchToZoomHelper.isInOverlayMode() && (view == ProfileActivity.this.avatarContainer2 || view == ((BaseFragment) ProfileActivity.this).actionBar || view == ProfileActivity.this.writeButton)) || view == ProfileActivity.this.blurredView) {
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            ProfileActivity.this.fragmentViewAttached = true;
            for (int i = 0; i < ProfileActivity.this.emojiStatusDrawable.length; i++) {
                if (ProfileActivity.this.emojiStatusDrawable[i] != null) {
                    ProfileActivity.this.emojiStatusDrawable[i].attach();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            ProfileActivity.this.fragmentViewAttached = false;
            for (int i = 0; i < ProfileActivity.this.emojiStatusDrawable.length; i++) {
                if (ProfileActivity.this.emojiStatusDrawable[i] != null) {
                    ProfileActivity.this.emojiStatusDrawable[i].detach();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 10 extends DefaultItemAnimator {
        int animationIndex = -1;

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getAddAnimationDelay(long j, long j2, long j3) {
            return 0L;
        }

        10() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        public void onAllAnimationsDone() {
            super.onAllAnimationsDone();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$10$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.10.this.lambda$onAllAnimationsDone$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAllAnimationsDone$0() {
            ProfileActivity.this.getNotificationCenter().onAnimationFinish(this.animationIndex);
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public void runPendingAnimations() {
            boolean z = !this.mPendingRemovals.isEmpty();
            boolean z2 = !this.mPendingMoves.isEmpty();
            boolean z3 = !this.mPendingChanges.isEmpty();
            boolean z4 = !this.mPendingAdditions.isEmpty();
            if (z || z2 || z4 || z3) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$10$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ProfileActivity.10.this.lambda$runPendingAnimations$1(valueAnimator);
                    }
                });
                ofFloat.setDuration(getMoveDuration());
                ofFloat.start();
                this.animationIndex = ProfileActivity.this.getNotificationCenter().setAnimationInProgress(this.animationIndex, null);
            }
            super.runPendingAnimations();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$runPendingAnimations$1(ValueAnimator valueAnimator) {
            ProfileActivity.this.listView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(final long j, Context context, View view, int i, float f, float f2) {
        TLRPC$ChatParticipant tLRPC$ChatParticipant;
        float f3;
        float f4;
        ListAdapter listAdapter;
        if (getParentActivity() == null) {
            return;
        }
        this.listView.stopScroll();
        if (i == this.notificationsSimpleRow) {
            boolean isDialogMuted = getMessagesController().isDialogMuted(j, this.topicId);
            getNotificationsController().muteDialog(j, this.topicId, !isDialogMuted);
            BulletinFactory.createMuteBulletin(this, !isDialogMuted, null).show();
            updateExceptions();
            int i2 = this.notificationsSimpleRow;
            if (i2 >= 0 && (listAdapter = this.listAdapter) != null) {
                listAdapter.notifyItemChanged(i2);
            }
        } else if (i == this.addToContactsRow) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", user.id);
            bundle.putBoolean("addContact", true);
            bundle.putString("phone", this.vcardPhone);
            bundle.putString("first_name_card", this.vcardFirstName);
            bundle.putString("last_name_card", this.vcardLastName);
            openAddToContact(user, bundle);
        } else if (i == this.reportReactionRow) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
            builder.setTitle(LocaleController.getString("ReportReaction", R.string.ReportReaction));
            builder.setMessage(LocaleController.getString("ReportAlertReaction", R.string.ReportAlertReaction));
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.reportReactionFromDialogId));
            final CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[1];
            if (chat != null && ChatObject.canBlockUsers(chat)) {
                LinearLayout linearLayout = new LinearLayout(getParentActivity());
                linearLayout.setOrientation(1);
                checkBoxCellArr[0] = new CheckBoxCell(getParentActivity(), 1, this.resourcesProvider);
                checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                checkBoxCellArr[0].setText(LocaleController.getString("BanUser", R.string.BanUser), "", true, false);
                checkBoxCellArr[0].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                linearLayout.addView(checkBoxCellArr[0], LayoutHelper.createLinear(-1, -2));
                checkBoxCellArr[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda20
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ProfileActivity.lambda$createView$6(checkBoxCellArr, view2);
                    }
                });
                builder.setView(linearLayout);
            }
            builder.setPositiveButton(LocaleController.getString("ReportChat", R.string.ReportChat), new 12(checkBoxCellArr));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener(this) { // from class: org.telegram.ui.ProfileActivity.13
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    dialogInterface.dismiss();
                }
            });
            TextView textView = (TextView) builder.show().getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }
        if (i == this.settingsKeyRow) {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("chat_id", DialogObject.getEncryptedChatId(this.dialogId));
            presentFragment(new IdenticonActivity(bundle2));
        } else if (i == this.settingsTimerRow) {
            showDialog(AlertsCreator.createTTLAlert(getParentActivity(), this.currentEncryptedChat, this.resourcesProvider).create());
        } else if (i == this.notificationsRow) {
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                boolean z = !notificationsCheckCell.isChecked();
                boolean isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(j);
                String sharedPrefKey = NotificationsController.getSharedPrefKey(j, this.topicId);
                long j2 = 0;
                if (z) {
                    SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    if (isGlobalNotificationsEnabled) {
                        edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
                    } else {
                        edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 0);
                    }
                    if (this.topicId == 0) {
                        getMessagesStorage().setDialogFlags(j, 0L);
                        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
                        if (tLRPC$Dialog != null) {
                            tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
                        }
                    }
                    edit.apply();
                } else {
                    SharedPreferences.Editor edit2 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    if (!isGlobalNotificationsEnabled) {
                        edit2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
                    } else {
                        edit2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 2);
                        j2 = 1;
                    }
                    getNotificationsController().removeNotificationsForDialog(j);
                    if (this.topicId == 0) {
                        getMessagesStorage().setDialogFlags(j, j2);
                        TLRPC$Dialog tLRPC$Dialog2 = getMessagesController().dialogs_dict.get(j);
                        if (tLRPC$Dialog2 != null) {
                            TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                            tLRPC$Dialog2.notify_settings = tLRPC$TL_peerNotifySettings;
                            if (isGlobalNotificationsEnabled) {
                                tLRPC$TL_peerNotifySettings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            }
                        }
                    }
                    edit2.apply();
                }
                updateExceptions();
                getNotificationsController().updateServerNotificationsSettings(j, this.topicId);
                notificationsCheckCell.setChecked(z);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findViewHolderForPosition(this.notificationsRow);
                if (holder != null) {
                    this.listAdapter.onBindViewHolder(holder, this.notificationsRow);
                    return;
                }
                return;
            }
            ChatNotificationsPopupWrapper chatNotificationsPopupWrapper = new ChatNotificationsPopupWrapper(context, this.currentAccount, null, true, true, new ChatNotificationsPopupWrapper.Callback() { // from class: org.telegram.ui.ProfileActivity.14
                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public /* synthetic */ void dismiss() {
                    ChatNotificationsPopupWrapper.Callback.-CC.$default$dismiss(this);
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void toggleSound() {
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileActivity.this).currentAccount);
                    boolean z2 = !notificationsSettings.getBoolean("sound_enabled_" + NotificationsController.getSharedPrefKey(j, ProfileActivity.this.topicId), true);
                    notificationsSettings.edit().putBoolean("sound_enabled_" + NotificationsController.getSharedPrefKey(j, ProfileActivity.this.topicId), z2).apply();
                    if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                        ProfileActivity profileActivity = ProfileActivity.this;
                        BulletinFactory.createSoundEnabledBulletin(profileActivity, !z2, profileActivity.getResourceProvider()).show();
                    }
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void muteFor(int i3) {
                    if (i3 == 0) {
                        if (ProfileActivity.this.getMessagesController().isDialogMuted(j, ProfileActivity.this.topicId)) {
                            toggleMute();
                        }
                        if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                            ProfileActivity profileActivity = ProfileActivity.this;
                            BulletinFactory.createMuteBulletin(profileActivity, 4, i3, profileActivity.getResourceProvider()).show();
                            return;
                        }
                        return;
                    }
                    ProfileActivity.this.getNotificationsController().muteUntil(j, ProfileActivity.this.topicId, i3);
                    if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                        ProfileActivity profileActivity2 = ProfileActivity.this;
                        BulletinFactory.createMuteBulletin(profileActivity2, 5, i3, profileActivity2.getResourceProvider()).show();
                    }
                    ProfileActivity.this.updateExceptions();
                    if (ProfileActivity.this.notificationsRow < 0 || ProfileActivity.this.listAdapter == null) {
                        return;
                    }
                    ProfileActivity.this.listAdapter.notifyItemChanged(ProfileActivity.this.notificationsRow);
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void showCustomize() {
                    if (j != 0) {
                        Bundle bundle3 = new Bundle();
                        bundle3.putLong("dialog_id", j);
                        bundle3.putInt("topic_id", ProfileActivity.this.topicId);
                        ProfileActivity profileActivity = ProfileActivity.this;
                        profileActivity.presentFragment(new ProfileNotificationsActivity(bundle3, profileActivity.resourcesProvider));
                    }
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void toggleMute() {
                    boolean isDialogMuted2 = ProfileActivity.this.getMessagesController().isDialogMuted(j, ProfileActivity.this.topicId);
                    ProfileActivity.this.getNotificationsController().muteDialog(j, ProfileActivity.this.topicId, !isDialogMuted2);
                    if (((BaseFragment) ProfileActivity.this).fragmentView != null) {
                        BulletinFactory.createMuteBulletin(ProfileActivity.this, !isDialogMuted2, null).show();
                    }
                    ProfileActivity.this.updateExceptions();
                    if (ProfileActivity.this.notificationsRow < 0 || ProfileActivity.this.listAdapter == null) {
                        return;
                    }
                    ProfileActivity.this.listAdapter.notifyItemChanged(ProfileActivity.this.notificationsRow);
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void openExceptions() {
                    Bundle bundle3 = new Bundle();
                    bundle3.putLong("dialog_id", j);
                    TopicsNotifySettingsFragments topicsNotifySettingsFragments = new TopicsNotifySettingsFragments(bundle3);
                    topicsNotifySettingsFragments.setExceptions(ProfileActivity.this.notificationsExceptionTopics);
                    ProfileActivity.this.presentFragment(topicsNotifySettingsFragments);
                }
            }, getResourceProvider());
            chatNotificationsPopupWrapper.lambda$update$11(j, this.topicId, this.notificationsExceptionTopics);
            if (AndroidUtilities.isTablet()) {
                ViewGroup view2 = this.parentLayout.getView();
                f3 = view2.getX() + view2.getPaddingLeft() + f;
                f4 = f2 + view2.getY() + view2.getPaddingTop();
            } else {
                f3 = f;
                f4 = f2;
            }
            chatNotificationsPopupWrapper.showAsOptions(this, view, f3, f4);
        } else if (i == this.unblockRow) {
            getMessagesController().unblockPeer(this.userId);
            if (BulletinFactory.canShowBulletin(this)) {
                BulletinFactory.createBanBulletin(this, false).show();
            }
        } else if (i == this.addToGroupButtonRow) {
            try {
                this.actionBar.getActionBarMenuOnItemClick().onItemClick(9);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (i == this.sendMessageRow) {
            onWriteButtonClick();
        } else if (i == this.reportRow) {
            AlertsCreator.createReportAlert(getParentActivity(), getDialogId(), 0, 0, this, this.resourcesProvider, null);
        } else if (i >= this.membersStartRow && i < this.membersEndRow) {
            if (!this.sortedUsers.isEmpty()) {
                tLRPC$ChatParticipant = this.chatInfo.participants.participants.get(this.sortedUsers.get(i - this.membersStartRow).intValue());
            } else {
                tLRPC$ChatParticipant = this.chatInfo.participants.participants.get(i - this.membersStartRow);
            }
            onMemberClick(tLRPC$ChatParticipant, false, view);
        } else if (i == this.addMemberRow) {
            openAddMember();
        } else if (i == this.usernameRow) {
            processOnClickOrPress(i, view, f, f2);
        } else if (i == this.locationRow) {
            if (this.chatInfo.location instanceof TLRPC$TL_channelLocation) {
                LocationActivity locationActivity = new LocationActivity(5);
                locationActivity.setChatLocation(this.chatId, (TLRPC$TL_channelLocation) this.chatInfo.location);
                presentFragment(locationActivity);
            }
        } else if (i == this.joinRow) {
            getMessagesController().addUserToChat(this.currentChat.id, getUserConfig().getCurrentUser(), 0, null, this, null);
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeSearchByActiveAction, new Object[0]);
        } else if (i == this.subscribersRow) {
            Bundle bundle3 = new Bundle();
            bundle3.putLong("chat_id", this.chatId);
            bundle3.putInt("type", 2);
            ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle3);
            chatUsersActivity.setInfo(this.chatInfo);
            presentFragment(chatUsersActivity);
        } else if (i == this.subscribersRequestsRow) {
            presentFragment(new MemberRequestsActivity(this.chatId));
        } else if (i == this.administratorsRow) {
            Bundle bundle4 = new Bundle();
            bundle4.putLong("chat_id", this.chatId);
            bundle4.putInt("type", 1);
            ChatUsersActivity chatUsersActivity2 = new ChatUsersActivity(bundle4);
            chatUsersActivity2.setInfo(this.chatInfo);
            presentFragment(chatUsersActivity2);
        } else if (i == this.settingsRow) {
            this.editItem.performClick();
        } else if (i == this.blockedUsersRow) {
            Bundle bundle5 = new Bundle();
            bundle5.putLong("chat_id", this.chatId);
            bundle5.putInt("type", 0);
            ChatUsersActivity chatUsersActivity3 = new ChatUsersActivity(bundle5);
            chatUsersActivity3.setInfo(this.chatInfo);
            presentFragment(chatUsersActivity3);
        } else if (i == this.notificationRow) {
            presentFragment(new NotificationsSettingsActivity());
        } else if (i == this.privacyRow) {
            presentFragment(new PrivacySettingsActivity().setCurrentPassword(this.currentPassword));
        } else if (i == this.dataRow) {
            presentFragment(new DataSettingsActivity());
        } else if (i == this.chatRow) {
            presentFragment(new ThemeActivity(0));
        } else if (i == this.filtersRow) {
            presentFragment(new FiltersSetupActivity());
        } else if (i == this.stickersRow) {
            presentFragment(new StickersActivity(0, null));
        } else if (i == this.liteModeRow) {
            presentFragment(new LiteModeSettingsActivity());
        } else if (i == this.devicesRow) {
            presentFragment(new SessionsActivity(0));
        } else if (i == this.questionRow) {
            showDialog(AlertsCreator.createSupportAlert(this, this.resourcesProvider));
        } else if (i == this.faqRow) {
            Browser.openUrl(getParentActivity(), LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
        } else if (i == this.policyRow) {
            Browser.openUrl(getParentActivity(), LocaleController.getString("PrivacyPolicyUrl", R.string.PrivacyPolicyUrl));
        } else if (i == this.sendLogsRow) {
            sendLogs(getParentActivity(), false);
        } else if (i == this.sendLastLogsRow) {
            sendLogs(getParentActivity(), true);
        } else if (i == this.clearLogsRow) {
            FileLog.cleanupLogs();
        } else if (i == this.switchBackendRow) {
            if (getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
            builder2.setMessage(LocaleController.getString("AreYouSure", R.string.AreYouSure));
            builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    ProfileActivity.this.lambda$createView$7(dialogInterface, i3);
                }
            });
            builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder2.create());
        } else if (i == this.languageRow) {
            presentFragment(new LanguageSelectActivity());
        } else if (i == this.setUsernameRow) {
            presentFragment(new ChangeUsernameActivity());
        } else if (i == this.bioRow) {
            if (this.userInfo != null) {
                presentFragment(new ChangeBioActivity());
            }
        } else if (i == this.numberRow) {
            presentFragment(new ActionIntroActivity(3));
        } else if (i == this.setAvatarRow) {
            onWriteButtonClick();
        } else if (i == this.premiumRow) {
            presentFragment(new PremiumPreviewFragment("settings"));
        } else {
            processOnClickOrPress(i, view, f, f2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$6(CheckBoxCell[] checkBoxCellArr, View view) {
        checkBoxCellArr[0].setChecked(!checkBoxCellArr[0].isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 12 implements DialogInterface.OnClickListener {
        final /* synthetic */ CheckBoxCell[] val$cells;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onClick$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        12(CheckBoxCell[] checkBoxCellArr) {
            this.val$cells = checkBoxCellArr;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            TLRPC$TL_messages_reportReaction tLRPC$TL_messages_reportReaction = new TLRPC$TL_messages_reportReaction();
            tLRPC$TL_messages_reportReaction.user_id = ProfileActivity.this.getMessagesController().getInputUser(ProfileActivity.this.userId);
            tLRPC$TL_messages_reportReaction.peer = ProfileActivity.this.getMessagesController().getInputPeer(ProfileActivity.this.reportReactionFromDialogId);
            tLRPC$TL_messages_reportReaction.id = ProfileActivity.this.reportReactionMessageId;
            ConnectionsManager.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).sendRequest(tLRPC$TL_messages_reportReaction, ProfileActivity$12$$ExternalSyntheticLambda0.INSTANCE);
            CheckBoxCell[] checkBoxCellArr = this.val$cells;
            if (checkBoxCellArr[0] != null && checkBoxCellArr[0].isChecked()) {
                ProfileActivity.this.getMessagesController().deleteParticipantFromChat(-ProfileActivity.this.reportReactionFromDialogId, ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId)));
            }
            ProfileActivity.this.reportReactionMessageId = 0;
            ProfileActivity.this.updateListAnimated(false);
            BulletinFactory.of(ProfileActivity.this).createReportSent(ProfileActivity.this.resourcesProvider).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(DialogInterface dialogInterface, int i) {
        SharedConfig.pushAuthKey = null;
        SharedConfig.pushAuthKeyId = null;
        SharedConfig.saveConfig();
        getConnectionsManager().switchBackend(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 15 implements RecyclerListView.OnItemLongClickListener {
        private int pressCount = 0;
        final /* synthetic */ Context val$context;

        15(Context context) {
            this.val$context = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
        public boolean onItemClick(View view, int i) {
            int i2;
            String str;
            String string;
            int i3;
            String str2;
            String str3;
            String str4;
            if (i != ProfileActivity.this.versionRow) {
                if (i < ProfileActivity.this.membersStartRow || i >= ProfileActivity.this.membersEndRow) {
                    return ProfileActivity.this.processOnClickOrPress(i, view, view.getWidth() / 2.0f, (int) (view.getHeight() * 0.75f));
                }
                return ProfileActivity.this.onMemberClick(!ProfileActivity.this.sortedUsers.isEmpty() ? (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(((Integer) ProfileActivity.this.sortedUsers.get(i - ProfileActivity.this.membersStartRow)).intValue()) : (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(i - ProfileActivity.this.membersStartRow), true, view);
            }
            int i4 = this.pressCount + 1;
            this.pressCount = i4;
            if (i4 >= 2 || BuildVars.DEBUG_PRIVATE_VERSION) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                builder.setTitle(LocaleController.getString("DebugMenu", R.string.DebugMenu));
                CharSequence[] charSequenceArr = new CharSequence[27];
                charSequenceArr[0] = LocaleController.getString("DebugMenuImportContacts", R.string.DebugMenuImportContacts);
                charSequenceArr[1] = LocaleController.getString("DebugMenuReloadContacts", R.string.DebugMenuReloadContacts);
                charSequenceArr[2] = LocaleController.getString("DebugMenuResetContacts", R.string.DebugMenuResetContacts);
                charSequenceArr[3] = LocaleController.getString("DebugMenuResetDialogs", R.string.DebugMenuResetDialogs);
                if (BuildVars.DEBUG_VERSION) {
                    string = null;
                } else {
                    if (BuildVars.LOGS_ENABLED) {
                        i2 = R.string.DebugMenuDisableLogs;
                        str = "DebugMenuDisableLogs";
                    } else {
                        i2 = R.string.DebugMenuEnableLogs;
                        str = "DebugMenuEnableLogs";
                    }
                    string = LocaleController.getString(str, i2);
                }
                charSequenceArr[4] = string;
                if (SharedConfig.inappCamera) {
                    i3 = R.string.DebugMenuDisableCamera;
                    str2 = "DebugMenuDisableCamera";
                } else {
                    i3 = R.string.DebugMenuEnableCamera;
                    str2 = "DebugMenuEnableCamera";
                }
                charSequenceArr[5] = LocaleController.getString(str2, i3);
                charSequenceArr[6] = LocaleController.getString("DebugMenuClearMediaCache", R.string.DebugMenuClearMediaCache);
                charSequenceArr[7] = LocaleController.getString("DebugMenuCallSettings", R.string.DebugMenuCallSettings);
                charSequenceArr[8] = null;
                charSequenceArr[9] = (BuildVars.DEBUG_PRIVATE_VERSION || ApplicationLoader.isStandaloneBuild()) ? LocaleController.getString("DebugMenuCheckAppUpdate", R.string.DebugMenuCheckAppUpdate) : null;
                charSequenceArr[10] = LocaleController.getString("DebugMenuReadAllDialogs", R.string.DebugMenuReadAllDialogs);
                charSequenceArr[11] = BuildVars.DEBUG_PRIVATE_VERSION ? SharedConfig.disableVoiceAudioEffects ? "Enable voip audio effects" : "Disable voip audio effects" : null;
                boolean z = BuildVars.DEBUG_PRIVATE_VERSION;
                charSequenceArr[12] = z ? "Clean app update" : null;
                charSequenceArr[13] = z ? "Reset suggestions" : null;
                charSequenceArr[14] = z ? LocaleController.getString(R.string.DebugMenuClearWebViewCache) : null;
                if (Build.VERSION.SDK_INT >= 19) {
                    str3 = LocaleController.getString(SharedConfig.debugWebView ? R.string.DebugMenuDisableWebViewDebug : R.string.DebugMenuEnableWebViewDebug);
                } else {
                    str3 = null;
                }
                charSequenceArr[15] = str3;
                charSequenceArr[16] = (AndroidUtilities.isTabletInternal() && BuildVars.DEBUG_PRIVATE_VERSION) ? SharedConfig.forceDisableTabletMode ? "Enable tablet mode" : "Disable tablet mode" : null;
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    str4 = LocaleController.getString(SharedConfig.isFloatingDebugActive ? R.string.FloatingDebugDisable : R.string.FloatingDebugEnable);
                } else {
                    str4 = null;
                }
                charSequenceArr[17] = str4;
                boolean z2 = BuildVars.DEBUG_PRIVATE_VERSION;
                charSequenceArr[18] = z2 ? "Force remove premium suggestions" : null;
                charSequenceArr[19] = z2 ? "Share device info" : null;
                charSequenceArr[20] = z2 ? "Force performance class" : null;
                charSequenceArr[21] = (!z2 || InstantCameraView.allowBigSizeCameraDebug()) ? null : !SharedConfig.bigCameraForRound ? "Force big camera for round" : "Disable big camera for round";
                charSequenceArr[22] = LocaleController.getString(DualCameraView.dualAvailableStatic(ProfileActivity.this.getContext()) ? "DebugMenuDualOff" : "DebugMenuDualOn");
                charSequenceArr[23] = BuildVars.DEBUG_VERSION ? SharedConfig.useSurfaceInStories ? "back to TextureView in stories" : "use SurfaceView in stories" : null;
                charSequenceArr[24] = BuildVars.DEBUG_PRIVATE_VERSION ? SharedConfig.photoViewerBlur ? "do not blur in photoviewer" : "blur in photoviewer" : null;
                charSequenceArr[25] = !SharedConfig.payByInvoice ? "Enable Invoice Payment" : "Disable Invoice Payment";
                charSequenceArr[26] = BuildVars.DEBUG_PRIVATE_VERSION ? "Update Attach Bots" : null;
                final Context context = this.val$context;
                builder.setItems(charSequenceArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$15$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        ProfileActivity.15.this.lambda$onItemClick$3(context, dialogInterface, i5);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                ProfileActivity.this.showDialog(builder.create());
            } else {
                try {
                    Toast.makeText(ProfileActivity.this.getParentActivity(), LocaleController.getString("DebugMenuLongPress", R.string.DebugMenuLongPress), 0).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$3(Context context, DialogInterface dialogInterface, int i) {
            Long l;
            Long l2;
            String str;
            int i2 = 0;
            if (i == 0) {
                ProfileActivity.this.getUserConfig().syncContacts = true;
                ProfileActivity.this.getUserConfig().saveConfig(false);
                ProfileActivity.this.getContactsController().forceImportContacts();
            } else if (i == 1) {
                ProfileActivity.this.getContactsController().loadContacts(false, 0L);
            } else if (i == 2) {
                ProfileActivity.this.getContactsController().resetImportedContacts();
            } else if (i == 3) {
                ProfileActivity.this.getMessagesController().forceResetDialogs();
            } else if (i == 4) {
                BuildVars.LOGS_ENABLED = !BuildVars.LOGS_ENABLED;
                ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).edit().putBoolean("logsEnabled", BuildVars.LOGS_ENABLED).commit();
                ProfileActivity.this.updateRowsIds();
                ProfileActivity.this.listAdapter.notifyDataSetChanged();
            } else if (i == 5) {
                SharedConfig.toggleInappCamera();
            } else if (i == 6) {
                ProfileActivity.this.getMessagesStorage().clearSentMedia();
                SharedConfig.setNoSoundHintShowed(false);
                MessagesController.getGlobalMainSettings().edit().remove("archivehint").remove("proximityhint").remove("archivehint_l").remove("speedhint").remove("gifhint").remove("reminderhint").remove("soundHint").remove("themehint").remove("bganimationhint").remove("filterhint").remove("n_0").remove("storyprvhint").remove("storyhint").remove("storyhint2").remove("storydualhint").remove("storysvddualhint").remove("stories_camera").remove("dualcam").remove("dualmatrix").remove("dual_available").remove("archivehint").remove("askNotificationsAfter").remove("askNotificationsDuration").remove("viewoncehint").remove("taptostorysoundhint").commit();
                MessagesController.getEmojiSettings(((BaseFragment) ProfileActivity.this).currentAccount).edit().remove("featured_hidden").remove("emoji_featured_hidden").commit();
                SharedConfig.textSelectionHintShows = 0;
                SharedConfig.lockRecordAudioVideoHint = 0;
                SharedConfig.stickersReorderingHintUsed = false;
                SharedConfig.forwardingOptionsHintShown = false;
                SharedConfig.replyingOptionsHintShown = false;
                SharedConfig.messageSeenHintCount = 3;
                SharedConfig.emojiInteractionsHintCount = 3;
                SharedConfig.dayNightThemeSwitchHintCount = 3;
                SharedConfig.fastScrollHintCount = 3;
                SharedConfig.stealthModeSendMessageConfirm = 2;
                SharedConfig.updateStealthModeSendMessageConfirm(2);
                SharedConfig.setStoriesReactionsLongPressHintUsed(false);
                SharedConfig.setStoriesIntroShown(false);
                SharedConfig.setMultipleReactionsPromoShowed(false);
                ChatThemeController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).clearCache();
                ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.newSuggestionsAvailable, new Object[0]);
                RestrictedLanguagesSelectActivity.cleanup();
                PersistColorPalette.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).cleanup();
            } else if (i == 7) {
                VoIPHelper.showCallDebugSettings(ProfileActivity.this.getParentActivity());
            } else if (i == 8) {
                SharedConfig.toggleRoundCamera16to9();
            } else if (i == 9) {
                ((LaunchActivity) ProfileActivity.this.getParentActivity()).checkAppUpdate(true);
            } else if (i == 10) {
                ProfileActivity.this.getMessagesStorage().readAllDialogs(-1);
            } else if (i == 11) {
                SharedConfig.toggleDisableVoiceAudioEffects();
            } else if (i == 12) {
                SharedConfig.pendingAppUpdate = null;
                SharedConfig.saveConfig();
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
            } else if (i == 13) {
                Set<String> set = ProfileActivity.this.getMessagesController().pendingSuggestions;
                set.add("VALIDATE_PHONE_NUMBER");
                set.add("VALIDATE_PASSWORD");
                ProfileActivity.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.newSuggestionsAvailable, new Object[0]);
            } else if (i == 14) {
                ApplicationLoader.applicationContext.deleteDatabase("webview.db");
                ApplicationLoader.applicationContext.deleteDatabase("webviewCache.db");
                WebStorage.getInstance().deleteAllData();
            } else if (i == 15) {
                SharedConfig.toggleDebugWebView();
                Toast.makeText(ProfileActivity.this.getParentActivity(), LocaleController.getString(SharedConfig.debugWebView ? R.string.DebugMenuWebViewDebugEnabled : R.string.DebugMenuWebViewDebugDisabled), 0).show();
            } else if (i == 16) {
                SharedConfig.toggleForceDisableTabletMode();
                Activity findActivity = AndroidUtilities.findActivity(context);
                Intent launchIntentForPackage = findActivity.getPackageManager().getLaunchIntentForPackage(findActivity.getPackageName());
                findActivity.finishAffinity();
                findActivity.startActivity(launchIntentForPackage);
                System.exit(0);
            } else if (i == 17) {
                FloatingDebugController.setActive((LaunchActivity) ProfileActivity.this.getParentActivity(), true ^ FloatingDebugController.isActive());
            } else if (i == 18) {
                ProfileActivity.this.getMessagesController().loadAppConfig();
                TLRPC$TL_help_dismissSuggestion tLRPC$TL_help_dismissSuggestion = new TLRPC$TL_help_dismissSuggestion();
                tLRPC$TL_help_dismissSuggestion.suggestion = "VALIDATE_PHONE_NUMBER";
                tLRPC$TL_help_dismissSuggestion.peer = new TLRPC$TL_inputPeerEmpty();
                ProfileActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_help_dismissSuggestion, new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$15$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        ProfileActivity.15.this.lambda$onItemClick$1(tLObject, tLRPC$TL_error);
                    }
                });
            } else if (i != 19) {
                if (i == 20) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                    builder.setTitle("Force performance class");
                    int devicePerformanceClass = SharedConfig.getDevicePerformanceClass();
                    final int measureDevicePerformanceClass = SharedConfig.measureDevicePerformanceClass();
                    CharSequence[] charSequenceArr = new CharSequence[3];
                    StringBuilder sb = new StringBuilder();
                    sb.append(devicePerformanceClass == 2 ? "**HIGH**" : "HIGH");
                    sb.append(measureDevicePerformanceClass == 2 ? " (measured)" : "");
                    charSequenceArr[0] = AndroidUtilities.replaceTags(sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(devicePerformanceClass == 1 ? "**AVERAGE**" : "AVERAGE");
                    sb2.append(measureDevicePerformanceClass == 1 ? " (measured)" : "");
                    charSequenceArr[1] = AndroidUtilities.replaceTags(sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(devicePerformanceClass == 0 ? "**LOW**" : "LOW");
                    sb3.append(measureDevicePerformanceClass != 0 ? "" : " (measured)");
                    charSequenceArr[2] = AndroidUtilities.replaceTags(sb3.toString());
                    builder.setItems(charSequenceArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$15$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface2, int i3) {
                            ProfileActivity.15.lambda$onItemClick$2(measureDevicePerformanceClass, dialogInterface2, i3);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.show();
                } else if (i == 21) {
                    SharedConfig.toggleRoundCamera();
                } else if (i == 22) {
                    boolean dualAvailableStatic = DualCameraView.dualAvailableStatic(ProfileActivity.this.getContext());
                    MessagesController.getGlobalMainSettings().edit().putBoolean("dual_available", !dualAvailableStatic).apply();
                    try {
                        Toast.makeText(ProfileActivity.this.getParentActivity(), LocaleController.getString(!dualAvailableStatic ? R.string.DebugMenuDualOnToast : R.string.DebugMenuDualOffToast), 0).show();
                    } catch (Exception unused) {
                    }
                } else if (i == 23) {
                    SharedConfig.toggleSurfaceInStories();
                    while (i2 < ProfileActivity.this.getParentLayout().getFragmentStack().size()) {
                        ProfileActivity.this.getParentLayout().getFragmentStack().get(i2).storyViewer = null;
                        i2++;
                    }
                } else if (i == 24) {
                    SharedConfig.togglePhotoViewerBlur();
                } else if (i == 25) {
                    SharedConfig.togglePaymentByInvoice();
                } else if (i == 26) {
                    ProfileActivity.this.getMediaDataController().loadAttachMenuBots(false, true);
                }
            } else {
                int i3 = ConnectionsManager.CPU_COUNT;
                String str2 = "activity";
                int memoryClass = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
                StringBuilder sb4 = new StringBuilder();
                long j = 0;
                long j2 = 0;
                long j3 = 0;
                long j4 = 0;
                long j5 = 0;
                long j6 = 0;
                long j7 = 0;
                long j8 = 0;
                while (i2 < i3) {
                    Long sysInfoLong = AndroidUtilities.getSysInfoLong("/sys/devices/system/cpu/cpu" + i2 + "/cpufreq/cpuinfo_min_freq");
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("/sys/devices/system/cpu/cpu");
                    sb5.append(i2);
                    int i4 = i3;
                    sb5.append("/cpufreq/cpuinfo_cur_freq");
                    Long sysInfoLong2 = AndroidUtilities.getSysInfoLong(sb5.toString());
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("/sys/devices/system/cpu/cpu");
                    sb6.append(i2);
                    int i5 = memoryClass;
                    sb6.append("/cpufreq/cpuinfo_max_freq");
                    Long sysInfoLong3 = AndroidUtilities.getSysInfoLong(sb6.toString());
                    Long sysInfoLong4 = AndroidUtilities.getSysInfoLong("/sys/devices/system/cpu/cpu" + i2 + "/cpu_capacity");
                    sb4.append("#");
                    sb4.append(i2);
                    sb4.append(" ");
                    if (sysInfoLong != null) {
                        str = str2;
                        sb4.append("min=");
                        l2 = sysInfoLong4;
                        sb4.append(sysInfoLong.longValue() / 1000);
                        sb4.append(" ");
                        j += sysInfoLong.longValue() / 1000;
                        j2++;
                    } else {
                        l2 = sysInfoLong4;
                        str = str2;
                    }
                    if (sysInfoLong2 != null) {
                        sb4.append("cur=");
                        sb4.append(sysInfoLong2.longValue() / 1000);
                        sb4.append(" ");
                        j3 += sysInfoLong2.longValue() / 1000;
                        j4++;
                    }
                    if (sysInfoLong3 != null) {
                        sb4.append("max=");
                        sb4.append(sysInfoLong3.longValue() / 1000);
                        sb4.append(" ");
                        j5 += sysInfoLong3.longValue() / 1000;
                        j6++;
                    }
                    if (l2 != null) {
                        sb4.append("cpc=");
                        Long l3 = l2;
                        sb4.append(l3);
                        sb4.append(" ");
                        j7 += l3.longValue();
                        j8++;
                    }
                    sb4.append("\n");
                    i2++;
                    i3 = i4;
                    memoryClass = i5;
                    str2 = str;
                }
                int i6 = i3;
                int i7 = memoryClass;
                String str3 = str2;
                StringBuilder sb7 = new StringBuilder();
                sb7.append(Build.MANUFACTURER);
                sb7.append(", ");
                sb7.append(Build.MODEL);
                sb7.append(" (");
                sb7.append(Build.PRODUCT);
                sb7.append(", ");
                sb7.append(Build.DEVICE);
                sb7.append(") ");
                sb7.append(" (android ");
                int i8 = Build.VERSION.SDK_INT;
                sb7.append(i8);
                sb7.append(")\n");
                if (i8 >= 31) {
                    sb7.append("SoC: ");
                    sb7.append(Build.SOC_MANUFACTURER);
                    sb7.append(", ");
                    sb7.append(Build.SOC_MODEL);
                    sb7.append("\n");
                }
                String sysInfoString = AndroidUtilities.getSysInfoString("/sys/kernel/gpu/gpu_model");
                if (sysInfoString != null) {
                    sb7.append("GPU: ");
                    sb7.append(sysInfoString);
                    Long sysInfoLong5 = AndroidUtilities.getSysInfoLong("/sys/kernel/gpu/gpu_min_clock");
                    Long sysInfoLong6 = AndroidUtilities.getSysInfoLong("/sys/kernel/gpu/gpu_mm_min_clock");
                    Long sysInfoLong7 = AndroidUtilities.getSysInfoLong("/sys/kernel/gpu/gpu_max_clock");
                    if (sysInfoLong5 != null) {
                        sb7.append(", min=");
                        l = sysInfoLong6;
                        sb7.append(sysInfoLong5.longValue() / 1000);
                    } else {
                        l = sysInfoLong6;
                    }
                    if (l != null) {
                        sb7.append(", mmin=");
                        sb7.append(l.longValue() / 1000);
                    }
                    if (sysInfoLong7 != null) {
                        sb7.append(", max=");
                        sb7.append(sysInfoLong7.longValue() / 1000);
                    }
                    sb7.append("\n");
                }
                ConfigurationInfo deviceConfigurationInfo = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService(str3)).getDeviceConfigurationInfo();
                sb7.append("GLES Version: ");
                sb7.append(deviceConfigurationInfo.getGlEsVersion());
                sb7.append("\n");
                sb7.append("Memory: class=");
                sb7.append(AndroidUtilities.formatFileSize(i7 * 1024 * 1024));
                ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                ((ActivityManager) ApplicationLoader.applicationContext.getSystemService(str3)).getMemoryInfo(memoryInfo);
                sb7.append(", total=");
                sb7.append(AndroidUtilities.formatFileSize(memoryInfo.totalMem));
                sb7.append(", avail=");
                sb7.append(AndroidUtilities.formatFileSize(memoryInfo.availMem));
                sb7.append(", low?=");
                sb7.append(memoryInfo.lowMemory);
                sb7.append(" (threshold=");
                sb7.append(AndroidUtilities.formatFileSize(memoryInfo.threshold));
                sb7.append(")");
                sb7.append("\n");
                sb7.append("Current class: ");
                sb7.append(SharedConfig.performanceClassName(SharedConfig.getDevicePerformanceClass()));
                sb7.append(", measured: ");
                sb7.append(SharedConfig.performanceClassName(SharedConfig.measureDevicePerformanceClass()));
                if (i8 >= 31) {
                    sb7.append(", suggest=");
                    sb7.append(Build.VERSION.MEDIA_PERFORMANCE_CLASS);
                }
                sb7.append("\n");
                sb7.append(i6);
                sb7.append(" CPUs");
                if (j2 > 0) {
                    sb7.append(", avgMinFreq=");
                    sb7.append(j / j2);
                }
                if (j4 > 0) {
                    sb7.append(", avgCurFreq=");
                    sb7.append(j3 / j4);
                }
                if (j6 > 0) {
                    sb7.append(", avgMaxFreq=");
                    sb7.append(j5 / j6);
                }
                if (j8 > 0) {
                    sb7.append(", avgCapacity=");
                    sb7.append(j7 / j8);
                }
                sb7.append("\n");
                sb7.append((CharSequence) sb4);
                ProfileActivity.this.listCodecs(MediaController.VIDEO_MIME_TYPE, sb7);
                ProfileActivity.this.listCodecs("video/hevc", sb7);
                ProfileActivity.this.showDialog(new 1(ProfileActivity.this.getParentActivity(), null, sb7.toString(), false, null, false));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            TLRPC$TL_help_dismissSuggestion tLRPC$TL_help_dismissSuggestion = new TLRPC$TL_help_dismissSuggestion();
            tLRPC$TL_help_dismissSuggestion.suggestion = "VALIDATE_PASSWORD";
            tLRPC$TL_help_dismissSuggestion.peer = new TLRPC$TL_inputPeerEmpty();
            ProfileActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_help_dismissSuggestion, new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$15$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    ProfileActivity.15.this.lambda$onItemClick$0(tLObject2, tLRPC$TL_error2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            ProfileActivity.this.getMessagesController().loadAppConfig();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 1 extends ShareAlert {
            1(Context context, ArrayList arrayList, String str, boolean z, String str2, boolean z2) {
                super(context, arrayList, str, z, str2, z2);
            }

            @Override // org.telegram.ui.Components.ShareAlert
            protected void onSend(final LongSparseArray<TLRPC$Dialog> longSparseArray, final int i, TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$15$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.15.1.this.lambda$onSend$0(longSparseArray, i);
                    }
                }, 250L);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onSend$0(LongSparseArray longSparseArray, int i) {
                BulletinFactory.createInviteSentBulletin(ProfileActivity.this.getParentActivity(), ProfileActivity.this.contentView, longSparseArray.size(), longSparseArray.size() == 1 ? ((TLRPC$Dialog) longSparseArray.valueAt(0)).id : 0L, i, getThemedColor(Theme.key_undo_background), getThemedColor(Theme.key_undo_infoColor)).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onItemClick$2(int i, DialogInterface dialogInterface, int i2) {
            int i3 = 2 - i2;
            if (i3 == i) {
                SharedConfig.overrideDevicePerformanceClass(-1);
            } else {
                SharedConfig.overrideDevicePerformanceClass(i3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$9(View view, int i) {
        boolean z;
        if (i < 0) {
            return;
        }
        Object valueOf = Integer.valueOf(this.numberRow);
        if (this.searchAdapter.searchWas) {
            if (i < this.searchAdapter.searchResults.size()) {
                valueOf = this.searchAdapter.searchResults.get(i);
            } else {
                int size = i - (this.searchAdapter.searchResults.size() + 1);
                if (size >= 0 && size < this.searchAdapter.faqSearchResults.size()) {
                    valueOf = this.searchAdapter.faqSearchResults.get(size);
                }
            }
        } else {
            if (!this.searchAdapter.recentSearches.isEmpty()) {
                i--;
            }
            if (i < 0 || i >= this.searchAdapter.recentSearches.size()) {
                int size2 = i - (this.searchAdapter.recentSearches.size() + 1);
                if (size2 >= 0 && size2 < this.searchAdapter.faqSearchArray.size()) {
                    valueOf = this.searchAdapter.faqSearchArray.get(size2);
                    z = false;
                    if (valueOf instanceof SearchAdapter.SearchResult) {
                        if (valueOf instanceof MessagesController.FaqSearchResult) {
                            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.openArticle, this.searchAdapter.faqWebPage, ((MessagesController.FaqSearchResult) valueOf).url);
                        }
                    } else {
                        ((SearchAdapter.SearchResult) valueOf).open();
                    }
                    if (z || valueOf == null) {
                    }
                    this.searchAdapter.addRecent(valueOf);
                    return;
                }
            } else {
                valueOf = this.searchAdapter.recentSearches.get(i);
            }
        }
        z = true;
        if (valueOf instanceof SearchAdapter.SearchResult) {
        }
        if (z) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$11(View view, int i) {
        if (this.searchAdapter.isSearchWas() || this.searchAdapter.recentSearches.isEmpty()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.ClearSearchAlertTitle));
        builder.setMessage(LocaleController.getString(R.string.ClearSearchAlert));
        builder.setPositiveButton(LocaleController.getString("ClearButton", R.string.ClearButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                ProfileActivity.this.lambda$createView$10(dialogInterface, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(DialogInterface dialogInterface, int i) {
        this.searchAdapter.clearRecent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(TLObject tLObject) {
        this.currentChannelParticipant = ((TLRPC$TL_channels_channelParticipant) tLObject).participant;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.this.lambda$createView$12(tLObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(TLRPC$Chat tLRPC$Chat, View view) {
        long j = this.userId;
        long j2 = this.banFromGroup;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights = tLRPC$Chat.default_banned_rights;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant = this.currentChannelParticipant;
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, j2, null, tLRPC$TL_chatBannedRights, tLRPC$ChannelParticipant != null ? tLRPC$ChannelParticipant.banned_rights : null, "", 1, true, false, null);
        chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ProfileActivity.18
            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str) {
                ProfileActivity.this.removeSelfFromStack();
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC$User tLRPC$User) {
                ProfileActivity.this.undoView.showWithAction(-ProfileActivity.this.chatId, ProfileActivity.this.currentChat.megagroup ? 10 : 9, tLRPC$User);
            }
        });
        presentFragment(chatRightsEditActivity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 19 extends FrameLayout {
        CanvasButton canvasButton;

        19(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (ProfileActivity.this.transitionOnlineText != null) {
                canvas.save();
                canvas.translate(ProfileActivity.this.onlineTextView[0].getX(), ProfileActivity.this.onlineTextView[0].getY());
                canvas.saveLayerAlpha(0.0f, 0.0f, ProfileActivity.this.transitionOnlineText.getMeasuredWidth(), ProfileActivity.this.transitionOnlineText.getMeasuredHeight(), (int) ((1.0f - ProfileActivity.this.avatarAnimationProgress) * 255.0f), 31);
                ProfileActivity.this.transitionOnlineText.draw(canvas);
                canvas.restore();
                canvas.restore();
                invalidate();
            }
            if (ProfileActivity.this.hasFallbackPhoto) {
                ProfileActivity profileActivity = ProfileActivity.this;
                if (profileActivity.photoDescriptionProgress == 0.0f || profileActivity.customAvatarProgress == 1.0f) {
                    return;
                }
                float y = ProfileActivity.this.onlineTextView[1].getY() + (ProfileActivity.this.onlineTextView[1].getMeasuredHeight() / 2.0f);
                float dp = AndroidUtilities.dp(22.0f);
                float dp2 = ((AndroidUtilities.dp(28.0f) - ProfileActivity.this.customPhotoOffset) + ProfileActivity.this.onlineTextView[1].getX()) - dp;
                ProfileActivity.this.fallbackImage.setImageCoords(dp2, y - (dp / 2.0f), dp, dp);
                ProfileActivity.this.fallbackImage.setAlpha(ProfileActivity.this.photoDescriptionProgress);
                canvas.save();
                ProfileActivity profileActivity2 = ProfileActivity.this;
                float f = profileActivity2.photoDescriptionProgress;
                canvas.scale(f, f, profileActivity2.fallbackImage.getCenterX(), ProfileActivity.this.fallbackImage.getCenterY());
                ProfileActivity.this.fallbackImage.draw(canvas);
                canvas.restore();
                if (ProfileActivity.this.customAvatarProgress == 0.0f) {
                    if (this.canvasButton == null) {
                        CanvasButton canvasButton = new CanvasButton(this);
                        this.canvasButton = canvasButton;
                        canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.ProfileActivity$19$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                ProfileActivity.19.this.lambda$dispatchDraw$0();
                            }
                        });
                    }
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(dp2 - AndroidUtilities.dp(4.0f), y - AndroidUtilities.dp(14.0f), dp2 + ProfileActivity.this.onlineTextView[2].getTextWidth() + (AndroidUtilities.dp(28.0f) * (1.0f - ProfileActivity.this.customAvatarProgress)) + AndroidUtilities.dp(4.0f), y + AndroidUtilities.dp(14.0f));
                    this.canvasButton.setRect(rectF);
                    this.canvasButton.setRounded(true);
                    this.canvasButton.setColor(0, ColorUtils.setAlphaComponent(-1, 50));
                    this.canvasButton.draw(canvas);
                    return;
                }
                CanvasButton canvasButton2 = this.canvasButton;
                if (canvasButton2 != null) {
                    canvasButton2.cancelRipple();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchDraw$0() {
            if (ProfileActivity.this.customAvatarProgress != 1.0f) {
                ProfileActivity.this.avatarsViewPager.scrollToLastItem();
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            CanvasButton canvasButton = this.canvasButton;
            return (canvasButton != null && canvasButton.checkTouchEvent(motionEvent)) || super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            CanvasButton canvasButton = this.canvasButton;
            return (canvasButton != null && canvasButton.checkTouchEvent(motionEvent)) || super.onTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            ProfileActivity.this.fallbackImage.onAttachedToWindow();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            ProfileActivity.this.fallbackImage.onDetachedFromWindow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$16(View view) {
        TLRPC$Document findDocument;
        Bulletin createContainsEmojiBulletin;
        if (this.avatarBig != null) {
            return;
        }
        if (this.isTopic && !getMessagesController().premiumLocked) {
            ArrayList<TLRPC$TL_forumTopic> topics = getMessagesController().getTopicsController().getTopics(this.chatId);
            if (topics != null) {
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = null;
                for (int i = 0; tLRPC$TL_forumTopic == null && i < topics.size(); i++) {
                    TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = topics.get(i);
                    if (tLRPC$TL_forumTopic2 != null && tLRPC$TL_forumTopic2.id == this.topicId) {
                        tLRPC$TL_forumTopic = tLRPC$TL_forumTopic2;
                    }
                }
                if (tLRPC$TL_forumTopic != null) {
                    long j = tLRPC$TL_forumTopic.icon_emoji_id;
                    if (j == 0 || (findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, j)) == null || (createContainsEmojiBulletin = BulletinFactory.of(this).createContainsEmojiBulletin(findDocument, 1, new Utilities.Callback() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda55
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            ProfileActivity.this.lambda$createView$15((TLRPC$InputStickerSet) obj);
                        }
                    })) == null) {
                        return;
                    }
                    createContainsEmojiBulletin.show();
                }
            }
        } else if (!expandAvatar()) {
            openAvatar();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$15(TLRPC$InputStickerSet tLRPC$InputStickerSet) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(tLRPC$InputStickerSet);
        showDialog(new EmojiPacksAlert(this, getParentActivity(), this.resourcesProvider, arrayList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$17(View view) {
        if (this.avatarBig == null && !this.isTopic) {
            openAvatar();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$18(View view) {
        if (this.writeButton.getTag() != null) {
            return;
        }
        onWriteButtonClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$19(ValueAnimator valueAnimator) {
        setAvatarExpandProgress(valueAnimator.getAnimatedFraction());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$20(View view) {
        finishPreviewFragment();
    }

    private void createFloatingActionButton(Context context) {
        if (getMessagesController().storiesEnabled() && getDialogId() <= 0 && ChatObject.isChannelAndNotMegaGroup(-getDialogId(), this.currentAccount)) {
            StoriesController storiesController = getMessagesController().getStoriesController();
            if (storiesController.canPostStories(getDialogId())) {
                this.waitCanSendStoryRequest = true;
                storiesController.canSendStoryFor(getDialogId(), new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda28
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        ProfileActivity.this.lambda$createFloatingActionButton$21((Boolean) obj);
                    }
                }, false, this.resourcesProvider);
                final long dialogId = getDialogId();
                FrameLayout frameLayout = new FrameLayout(context);
                this.floatingButtonContainer = frameLayout;
                frameLayout.setVisibility(0);
                NestedFrameLayout nestedFrameLayout = this.contentView;
                FrameLayout frameLayout2 = this.floatingButtonContainer;
                int i = Build.VERSION.SDK_INT;
                int i2 = i >= 21 ? 56 : 60;
                float f = i >= 21 ? 56 : 60;
                boolean z = LocaleController.isRTL;
                nestedFrameLayout.addView(frameLayout2, LayoutHelper.createFrame(i2, f, (z ? 3 : 5) | 80, z ? 14.0f : 0.0f, 0.0f, z ? 0.0f : 14.0f, 14.0f));
                this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda15
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ProfileActivity.this.lambda$createFloatingActionButton$24(dialogId, view);
                    }
                });
                RLottieImageView rLottieImageView = new RLottieImageView(context);
                this.floatingButton = rLottieImageView;
                rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
                this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
                if (i >= 21) {
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    FrameLayout frameLayout3 = this.floatingButtonContainer;
                    Property property = View.TRANSLATION_Z;
                    stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(frameLayout3, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonContainer, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                    this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                    this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.ProfileActivity.33
                        @Override // android.view.ViewOutlineProvider
                        @SuppressLint({"NewApi"})
                        public void getOutline(View view, Outline outline) {
                            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        }
                    });
                }
                this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(-1, -1.0f));
                this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon_camera, 56, 56);
                this.floatingButtonContainer.setContentDescription(LocaleController.getString("AccDescrCaptureStory", R.string.AccDescrCaptureStory));
                updateFloatingButtonColor();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createFloatingActionButton$21(Boolean bool) {
        this.waitCanSendStoryRequest = false;
        this.showBoostsAlert = !bool.booleanValue();
        hideFloatingButton(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createFloatingActionButton$24(final long j, View view) {
        if (this.showBoostsAlert) {
            if (this.loadingBoostsStats) {
                return;
            }
            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            this.loadingBoostsStats = true;
            messagesController.getBoostsController().getBoostsStats(j, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda29
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    ProfileActivity.this.lambda$createFloatingActionButton$23(j, (TL_stories$TL_premium_boostsStatus) obj);
                }
            });
            return;
        }
        StoryRecorder.getInstance(getParentActivity(), this.currentAccount).selectedPeerId(getDialogId()).canChangePeer(false).closeToWhenSent(new StoryRecorder.ClosingViewProvider() { // from class: org.telegram.ui.ProfileActivity.32
            @Override // org.telegram.ui.Stories.recorder.StoryRecorder.ClosingViewProvider
            public void preLayout(long j2, Runnable runnable) {
                ProfileActivity.this.avatarImage.setHasStories(ProfileActivity.this.needInsetForStories());
                if (j2 == ProfileActivity.this.getDialogId()) {
                    ProfileActivity.this.collapseAvatarInstant();
                }
                AndroidUtilities.runOnUIThread(runnable, 30L);
            }

            @Override // org.telegram.ui.Stories.recorder.StoryRecorder.ClosingViewProvider
            public StoryRecorder.SourceView getView(long j2) {
                if (j2 != ProfileActivity.this.getDialogId()) {
                    return null;
                }
                return StoryRecorder.SourceView.fromAvatarImage(ProfileActivity.this.avatarImage);
            }
        }).open(StoryRecorder.SourceView.fromFloatingButton(this.floatingButtonContainer), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createFloatingActionButton$23(long j, TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        this.loadingBoostsStats = false;
        if (tL_stories$TL_premium_boostsStatus == null) {
            return;
        }
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(lastFragment, lastFragment.getContext(), 18, this.currentAccount, this.resourcesProvider);
        limitReachedBottomSheet.setBoostsStats(tL_stories$TL_premium_boostsStatus, false);
        limitReachedBottomSheet.setDialogId(j);
        limitReachedBottomSheet.showStatisticButtonInLink(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.lambda$createFloatingActionButton$22();
            }
        });
        limitReachedBottomSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createFloatingActionButton$22() {
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putBoolean("is_megagroup", chat.megagroup);
        bundle.putBoolean("start_from_boosts", true);
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if (tLRPC$ChatFull == null || !tLRPC$ChatFull.can_view_stats) {
            bundle.putBoolean("only_boosts", true);
        }
        presentFragment(new StatisticActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void collapseAvatarInstant() {
        if (!this.allowPullingDown || this.currentExpandAnimatorValue <= 0.0f) {
            return;
        }
        this.layoutManager.scrollToPositionWithOffset(0, AndroidUtilities.dp(88.0f) - this.listView.getPaddingTop());
        this.listView.post(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.lambda$collapseAvatarInstant$25();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collapseAvatarInstant$25() {
        needLayout(true);
        if (this.expandAnimator.isRunning()) {
            this.expandAnimator.cancel();
        }
        setAvatarExpandProgress(1.0f);
    }

    private void updateFloatingButtonColor() {
        if (getParentActivity() == null || this.floatingButtonContainer == null) {
            return;
        }
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), applyPeerColor(Theme.getColor(Theme.key_chats_actionBackground), false), applyPeerColor(Theme.getColor(Theme.key_chats_actionPressedBackground), false));
        if (Build.VERSION.SDK_INT < 21) {
            Drawable mutate = ContextCompat.getDrawable(getParentActivity(), R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFloatingButton(boolean z) {
        if (this.floatingHidden == z || this.floatingButtonContainer == null || this.waitCanSendStoryRequest) {
            return;
        }
        this.floatingHidden = z;
        AnimatorSet animatorSet = new AnimatorSet();
        float[] fArr = new float[2];
        fArr[0] = this.floatingButtonHideProgress;
        fArr[1] = this.floatingHidden ? 1.0f : 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProfileActivity.this.lambda$hideFloatingButton$26(valueAnimator);
            }
        });
        animatorSet.playTogether(ofFloat);
        animatorSet.setDuration(300L);
        animatorSet.setInterpolator(this.floatingInterpolator);
        this.floatingButtonContainer.setClickable(!z);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideFloatingButton$26(ValueAnimator valueAnimator) {
        this.floatingButtonHideProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateFloatingButtonOffset();
    }

    private void updateFloatingButtonOffset() {
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            frameLayout.setTranslationY(AndroidUtilities.dp(100.0f) * this.floatingButtonHideProgress);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean expandAvatar() {
        RecyclerView.ViewHolder findContainingViewHolder;
        Integer num;
        if (!AndroidUtilities.isTablet() && !this.isInLandscapeMode && this.avatarImage.getImageReceiver().hasNotThumb() && !AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
            this.openingAvatar = true;
            this.allowPullingDown = true;
            View view = null;
            int i = 0;
            while (true) {
                if (i >= this.listView.getChildCount()) {
                    break;
                }
                RecyclerListView recyclerListView = this.listView;
                if (recyclerListView.getChildAdapterPosition(recyclerListView.getChildAt(i)) == 0) {
                    view = this.listView.getChildAt(i);
                    break;
                }
                i++;
            }
            if (view != null && (findContainingViewHolder = this.listView.findContainingViewHolder(view)) != null && (num = this.positionToOffset.get(Integer.valueOf(findContainingViewHolder.getAdapterPosition()))) != null) {
                this.listView.smoothScrollBy(0, -(num.intValue() + ((this.listView.getPaddingTop() - view.getTop()) - this.actionBar.getMeasuredHeight())), CubicBezierInterpolator.EASE_OUT_QUINT);
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0279  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x028e  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0290  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setAvatarExpandProgress(float f) {
        int themedColor;
        boolean z;
        SimpleTextView[] simpleTextViewArr;
        int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);
        float[] fArr = this.expandAnimatorValues;
        this.currentExpanAnimatorFracture = f;
        float lerp = AndroidUtilities.lerp(fArr, f);
        this.currentExpandAnimatorValue = lerp;
        checkPhotoDescriptionAlpha();
        this.avatarContainer.setScaleX(this.avatarScale);
        this.avatarContainer.setScaleY(this.avatarScale);
        this.avatarContainer.setTranslationX(AndroidUtilities.lerp(this.avatarX, 0.0f, lerp));
        this.avatarContainer.setTranslationY(AndroidUtilities.lerp((float) Math.ceil(this.avatarY), 0.0f, lerp));
        this.avatarImage.setRoundRadius((int) AndroidUtilities.lerp(getSmallAvatarRoundRadius(), 0.0f, lerp));
        ProfileStoriesView profileStoriesView = this.storyView;
        if (profileStoriesView != null) {
            profileStoriesView.setExpandProgress(lerp);
        }
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (actionBarMenuItem != null) {
            float f2 = 1.0f - lerp;
            actionBarMenuItem.setAlpha(f2);
            this.searchItem.setScaleY(f2);
            this.searchItem.setVisibility(0);
            ActionBarMenuItem actionBarMenuItem2 = this.searchItem;
            actionBarMenuItem2.setClickable(actionBarMenuItem2.getAlpha() > 0.5f);
            if (this.qrItem != null) {
                float dp = AndroidUtilities.dp(48.0f) * lerp;
                this.qrItem.setTranslationX(dp);
                this.avatarsViewPagerIndicatorView.setTranslationX(dp - AndroidUtilities.dp(48.0f));
            }
        }
        if (this.extraHeight > AndroidUtilities.dp(88.0f) && this.expandProgress < 0.33f) {
            refreshNameAndOnlineXY();
        }
        ScamDrawable scamDrawable = this.scamDrawable;
        if (scamDrawable != null) {
            scamDrawable.setColor(ColorUtils.blendARGB(getThemedColor(Theme.key_avatar_subtitleInProfileBlue), Color.argb(179, 255, 255, 255), lerp));
        }
        Drawable drawable = this.lockIconDrawable;
        if (drawable != null) {
            drawable.setColorFilter(ColorUtils.blendARGB(getThemedColor(Theme.key_chat_lockIcon), -1, lerp), PorterDuff.Mode.MULTIPLY);
        }
        CrossfadeDrawable[] crossfadeDrawableArr = this.verifiedCrossfadeDrawable;
        if (crossfadeDrawableArr[0] != null) {
            crossfadeDrawableArr[0].setProgress(lerp);
        }
        CrossfadeDrawable[] crossfadeDrawableArr2 = this.verifiedCrossfadeDrawable;
        if (crossfadeDrawableArr2[1] != null) {
            crossfadeDrawableArr2[1].setProgress(lerp);
        }
        CrossfadeDrawable[] crossfadeDrawableArr3 = this.premiumCrossfadeDrawable;
        if (crossfadeDrawableArr3[0] != null) {
            crossfadeDrawableArr3[0].setProgress(lerp);
        }
        CrossfadeDrawable[] crossfadeDrawableArr4 = this.premiumCrossfadeDrawable;
        if (crossfadeDrawableArr4[1] != null) {
            crossfadeDrawableArr4[1].setProgress(lerp);
        }
        updateEmojiStatusDrawableColor(lerp);
        float dpf2 = AndroidUtilities.dpf2(8.0f);
        float dpf22 = AndroidUtilities.dpf2(18.0f) - this.nameTextView[1].getLeft();
        float f3 = currentActionBarHeight;
        float dpf23 = ((this.extraHeight + f3) - AndroidUtilities.dpf2(38.0f)) - this.nameTextView[1].getBottom();
        float f4 = this.nameX;
        float f5 = this.nameY;
        float f6 = 1.0f - lerp;
        float f7 = f6 * f6;
        float f8 = f6 * 2.0f * lerp;
        float f9 = lerp * lerp;
        float f10 = (f4 * f7) + ((dpf2 + f4 + ((dpf22 - f4) / 2.0f)) * f8) + (dpf22 * f9);
        float f11 = (f5 * f7) + ((dpf2 + f5 + ((dpf23 - f5) / 2.0f)) * f8) + (dpf23 * f9);
        float dpf24 = AndroidUtilities.dpf2(16.0f) - this.onlineTextView[1].getLeft();
        float dpf25 = ((this.extraHeight + f3) - AndroidUtilities.dpf2(18.0f)) - this.onlineTextView[1].getBottom();
        float f12 = this.onlineX;
        float f13 = this.onlineY;
        float f14 = (f12 * f7) + ((dpf2 + f12 + ((dpf24 - f12) / 2.0f)) * f8) + (dpf24 * f9);
        float f15 = (f7 * f13) + (f8 * (dpf2 + f13 + ((dpf25 - f13) / 2.0f))) + (f9 * dpf25);
        this.nameTextView[1].setTranslationX(f10);
        this.nameTextView[1].setTranslationY(f11);
        this.onlineTextView[1].setTranslationX(this.customPhotoOffset + f14);
        this.onlineTextView[1].setTranslationY(f15);
        this.mediaCounterTextView.setTranslationX(f14);
        this.mediaCounterTextView.setTranslationY(f15);
        Object tag = this.onlineTextView[1].getTag();
        if (tag instanceof Integer) {
            Integer num = (Integer) tag;
            themedColor = getThemedColor(num.intValue());
            if (num.intValue() == Theme.key_profile_status) {
                z = true;
                this.onlineTextView[1].setTextColor(ColorUtils.blendARGB(applyPeerColor(themedColor, true, Boolean.valueOf(z)), -1275068417, lerp));
                if (this.extraHeight > AndroidUtilities.dp(88.0f)) {
                    this.nameTextView[1].setPivotY(AndroidUtilities.lerp(0, simpleTextViewArr[1].getMeasuredHeight(), lerp));
                    this.nameTextView[1].setScaleX(AndroidUtilities.lerp(1.12f, 1.67f, lerp));
                    this.nameTextView[1].setScaleY(AndroidUtilities.lerp(1.12f, 1.67f, lerp));
                }
                needLayoutText(Math.min(1.0f, this.extraHeight / AndroidUtilities.dp(88.0f)));
                this.nameTextView[1].setTextColor(ColorUtils.blendARGB(this.peerColor == null ? -1 : getThemedColor(Theme.key_profile_title), -1, this.currentExpandAnimatorValue));
                this.actionBar.setItemsColor(ColorUtils.blendARGB(this.peerColor == null ? -1 : getThemedColor(Theme.key_actionBarDefaultIcon), -1, lerp), false);
                this.actionBar.setMenuOffsetSuppressed(true);
                this.avatarImage.setForegroundAlpha(lerp);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.avatarContainer.getLayoutParams();
                layoutParams.width = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), this.listView.getMeasuredWidth() / this.avatarScale, lerp);
                layoutParams.height = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), (this.extraHeight + f3) / this.avatarScale, lerp);
                layoutParams.leftMargin = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(64.0f), 0.0f, lerp);
                this.avatarContainer.requestLayout();
            }
        } else {
            themedColor = getThemedColor(Theme.key_avatar_subtitleInProfileBlue);
        }
        z = false;
        this.onlineTextView[1].setTextColor(ColorUtils.blendARGB(applyPeerColor(themedColor, true, Boolean.valueOf(z)), -1275068417, lerp));
        if (this.extraHeight > AndroidUtilities.dp(88.0f)) {
        }
        needLayoutText(Math.min(1.0f, this.extraHeight / AndroidUtilities.dp(88.0f)));
        this.nameTextView[1].setTextColor(ColorUtils.blendARGB(this.peerColor == null ? -1 : getThemedColor(Theme.key_profile_title), -1, this.currentExpandAnimatorValue));
        this.actionBar.setItemsColor(ColorUtils.blendARGB(this.peerColor == null ? -1 : getThemedColor(Theme.key_actionBarDefaultIcon), -1, lerp), false);
        this.actionBar.setMenuOffsetSuppressed(true);
        this.avatarImage.setForegroundAlpha(lerp);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.avatarContainer.getLayoutParams();
        layoutParams2.width = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), this.listView.getMeasuredWidth() / this.avatarScale, lerp);
        layoutParams2.height = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), (this.extraHeight + f3) / this.avatarScale, lerp);
        layoutParams2.leftMargin = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(64.0f), 0.0f, lerp);
        this.avatarContainer.requestLayout();
    }

    private int getSmallAvatarRoundRadius() {
        if (this.chatId != 0 && ChatObject.isForum(getMessagesController().getChat(Long.valueOf(this.chatId)))) {
            return AndroidUtilities.dp(16.0f);
        }
        return AndroidUtilities.dp(21.0f);
    }

    private void updateTtlIcon() {
        TLRPC$UserFull tLRPC$UserFull;
        if (this.ttlIconView == null) {
            return;
        }
        boolean z = false;
        if (this.currentEncryptedChat == null && (((tLRPC$UserFull = this.userInfo) != null && tLRPC$UserFull.ttl_period > 0) || (this.chatInfo != null && ChatObject.canUserDoAdminAction(this.currentChat, 13) && this.chatInfo.ttl_period > 0))) {
            z = true;
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.ttlIconView, z, 0.8f, this.fragmentOpened);
    }

    public long getDialogId() {
        long j = this.dialogId;
        if (j != 0) {
            return j;
        }
        long j2 = this.userId;
        return j2 != 0 ? j2 : -this.chatId;
    }

    public void getEmojiStatusLocation(Rect rect) {
        SimpleTextView[] simpleTextViewArr = this.nameTextView;
        if (simpleTextViewArr[1] == null) {
            return;
        }
        if (simpleTextViewArr[1].getRightDrawable() == null) {
            rect.set(this.nameTextView[1].getWidth() - 1, (this.nameTextView[1].getHeight() / 2) - 1, this.nameTextView[1].getWidth() + 1, (this.nameTextView[1].getHeight() / 2) + 1);
            return;
        }
        rect.set(this.nameTextView[1].getRightDrawable().getBounds());
        rect.offset((int) (rect.centerX() * (this.nameTextView[1].getScaleX() - 1.0f)), 0);
        rect.offset((int) this.nameTextView[1].getX(), (int) this.nameTextView[1].getY());
    }

    public void goToForum() {
        if (getParentLayout() != null && getParentLayout().getFragmentStack() != null) {
            int i = 0;
            while (i < getParentLayout().getFragmentStack().size()) {
                BaseFragment baseFragment = getParentLayout().getFragmentStack().get(i);
                if (baseFragment instanceof DialogsActivity) {
                    DialogsActivity dialogsActivity = (DialogsActivity) baseFragment;
                    RightSlidingDialogContainer rightSlidingDialogContainer = dialogsActivity.rightSlidingDialogContainer;
                    if (rightSlidingDialogContainer != null) {
                        BaseFragment fragment = rightSlidingDialogContainer.getFragment();
                        if ((fragment instanceof TopicsFragment) && ((TopicsFragment) fragment).getDialogId() == getDialogId()) {
                            dialogsActivity.rightSlidingDialogContainer.lambda$presentFragment$1();
                        }
                    }
                } else if (baseFragment instanceof ChatActivity) {
                    if (((ChatActivity) baseFragment).getDialogId() == getDialogId()) {
                        getParentLayout().removeFragmentFromStack(baseFragment);
                        i--;
                    }
                } else if (baseFragment instanceof TopicsFragment) {
                    if (((TopicsFragment) baseFragment).getDialogId() == getDialogId()) {
                        getParentLayout().removeFragmentFromStack(baseFragment);
                        i--;
                    }
                } else if ((baseFragment instanceof ProfileActivity) && baseFragment != this) {
                    ProfileActivity profileActivity = (ProfileActivity) baseFragment;
                    if (profileActivity.getDialogId() == getDialogId() && profileActivity.isTopic) {
                        getParentLayout().removeFragmentFromStack(baseFragment);
                        i--;
                    }
                }
                i++;
            }
        }
        this.playProfileAnimation = 0;
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        presentFragment(TopicsFragment.getTopicsOrChat(this, bundle));
    }

    public void showStatusSelect() {
        if (this.selectAnimatedEmojiDialog != null) {
            return;
        }
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
        Rect rect = AndroidUtilities.rectTmp2;
        getEmojiStatusLocation(rect);
        int i = this.nameTextView[1].getScaleX() < 1.5f ? 16 : 32;
        int dp = (-(this.avatarContainer2.getHeight() - rect.centerY())) - AndroidUtilities.dp(i);
        int min = (int) Math.min(AndroidUtilities.dp(324.0f), AndroidUtilities.displaySize.x * 0.95f);
        int centerX = rect.centerX();
        int clamp = MathUtils.clamp(centerX - (min / 2), 0, AndroidUtilities.displaySize.x - min);
        int i2 = centerX - clamp;
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] swapAnimatedEmojiDrawableArr = this.emojiStatusDrawable;
        if (swapAnimatedEmojiDrawableArr[1] != null) {
            boolean z = swapAnimatedEmojiDrawableArr[1].getDrawable() instanceof AnimatedEmojiDrawable;
        }
        34 r8 = new 34(this, getContext(), true, Integer.valueOf(Math.max(0, i2)), 0, true, this.resourcesProvider, i, selectAnimatedEmojiDialogWindowArr);
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
        if (user != null) {
            TLRPC$EmojiStatus tLRPC$EmojiStatus = user.emoji_status;
            if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000))) {
                r8.setExpireDateHint(((TLRPC$TL_emojiStatusUntil) user.emoji_status).until);
            }
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] swapAnimatedEmojiDrawableArr2 = this.emojiStatusDrawable;
        r8.setSelected((swapAnimatedEmojiDrawableArr2[1] == null || !(swapAnimatedEmojiDrawableArr2[1].getDrawable() instanceof AnimatedEmojiDrawable)) ? null : Long.valueOf(((AnimatedEmojiDrawable) this.emojiStatusDrawable[1].getDrawable()).getDocumentId()));
        r8.setSaveState(3);
        r8.setScrimDrawable(this.emojiStatusDrawable[1], this.nameTextView[1]);
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(r8, -2, -2) { // from class: org.telegram.ui.ProfileActivity.35
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                ProfileActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
        selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
        int[] iArr = new int[2];
        SimpleTextView[] simpleTextViewArr = this.nameTextView;
        if (simpleTextViewArr[1] != null) {
            simpleTextViewArr[1].getLocationOnScreen(iArr);
        }
        selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(this.fragmentView, clamp, dp, 51);
        selectAnimatedEmojiDialogWindowArr[0].dimBehind();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 34 extends SelectAnimatedEmojiDialog {
        final /* synthetic */ SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] val$popup;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        34(BaseFragment baseFragment, Context context, boolean z, Integer num, int i, boolean z2, Theme.ResourcesProvider resourcesProvider, int i2, SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr) {
            super(baseFragment, context, z, num, i, z2, resourcesProvider, i2);
            this.val$popup = selectAnimatedEmojiDialogWindowArr;
        }

        @Override // org.telegram.ui.SelectAnimatedEmojiDialog
        protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
            TLRPC$TL_account_updateEmojiStatus tLRPC$TL_account_updateEmojiStatus = new TLRPC$TL_account_updateEmojiStatus();
            if (l == null) {
                tLRPC$TL_account_updateEmojiStatus.emoji_status = new TLRPC$TL_emojiStatusEmpty();
            } else if (num != null) {
                TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil = new TLRPC$TL_emojiStatusUntil();
                tLRPC$TL_account_updateEmojiStatus.emoji_status = tLRPC$TL_emojiStatusUntil;
                tLRPC$TL_emojiStatusUntil.document_id = l.longValue();
                ((TLRPC$TL_emojiStatusUntil) tLRPC$TL_account_updateEmojiStatus.emoji_status).until = num.intValue();
            } else {
                TLRPC$TL_emojiStatus tLRPC$TL_emojiStatus = new TLRPC$TL_emojiStatus();
                tLRPC$TL_account_updateEmojiStatus.emoji_status = tLRPC$TL_emojiStatus;
                tLRPC$TL_emojiStatus.document_id = l.longValue();
            }
            TLRPC$User user = MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getUser(Long.valueOf(UserConfig.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getClientUserId()));
            if (user != null) {
                user.emoji_status = tLRPC$TL_account_updateEmojiStatus.emoji_status;
                MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).updateEmojiStatusUntilUpdate(user.id, user.emoji_status);
                NotificationCenter.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userEmojiStatusUpdated, user);
            }
            for (int i = 0; i < 2; i++) {
                if (ProfileActivity.this.emojiStatusDrawable[i] != null) {
                    if (l == null) {
                        ProfileActivity.this.emojiStatusDrawable[i].set(ProfileActivity.this.getPremiumCrossfadeDrawable(i), true);
                    } else {
                        ProfileActivity.this.emojiStatusDrawable[i].set(l.longValue(), true);
                    }
                }
            }
            if (l != null) {
                ProfileActivity.this.animatedStatusView.animateChange(ReactionsLayoutInBubble.VisibleReaction.fromCustomEmoji(l));
            }
            ProfileActivity.this.updateEmojiStatusDrawableColor();
            ProfileActivity.this.updateEmojiStatusEffectPosition();
            ConnectionsManager.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).sendRequest(tLRPC$TL_account_updateEmojiStatus, ProfileActivity$34$$ExternalSyntheticLambda0.INSTANCE);
            if (this.val$popup[0] != null) {
                ProfileActivity.this.selectAnimatedEmojiDialog = null;
                this.val$popup[0].dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onEmojiSelected$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            boolean z = tLObject instanceof TLRPC$TL_boolTrue;
        }
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public TLRPC$Chat getCurrentChat() {
        return this.currentChat;
    }

    public TLRPC$UserFull getUserInfo() {
        return this.userInfo;
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public boolean isFragmentOpened() {
        return this.isFragmentOpened;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openAvatar() {
        TLRPC$Chat chat;
        TLRPC$ChatPhoto tLRPC$ChatPhoto;
        ImageLocation imageLocation;
        if (this.listView.getScrollState() == 1) {
            return;
        }
        if (this.userId != 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = user.photo;
            if (tLRPC$UserProfilePhoto == null || tLRPC$UserProfilePhoto.photo_big == null) {
                return;
            }
            PhotoViewer.getInstance().setParentActivity(this);
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2 = user.photo;
            int i = tLRPC$UserProfilePhoto2.dc_id;
            if (i != 0) {
                tLRPC$UserProfilePhoto2.photo_big.dc_id = i;
            }
            PhotoViewer.getInstance().openPhoto(user.photo.photo_big, this.provider);
        } else if (this.chatId == 0 || (tLRPC$ChatPhoto = (chat = getMessagesController().getChat(Long.valueOf(this.chatId))).photo) == null || tLRPC$ChatPhoto.photo_big == null) {
        } else {
            PhotoViewer.getInstance().setParentActivity(this);
            TLRPC$ChatPhoto tLRPC$ChatPhoto2 = chat.photo;
            int i2 = tLRPC$ChatPhoto2.dc_id;
            if (i2 != 0) {
                tLRPC$ChatPhoto2.photo_big.dc_id = i2;
            }
            TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
            if (tLRPC$ChatFull != null) {
                TLRPC$Photo tLRPC$Photo = tLRPC$ChatFull.chat_photo;
                if ((tLRPC$Photo instanceof TLRPC$TL_photo) && !tLRPC$Photo.video_sizes.isEmpty()) {
                    imageLocation = ImageLocation.getForPhoto(this.chatInfo.chat_photo.video_sizes.get(0), this.chatInfo.chat_photo);
                    PhotoViewer.getInstance().openPhotoWithVideo(chat.photo.photo_big, imageLocation, this.provider);
                }
            }
            imageLocation = null;
            PhotoViewer.getInstance().openPhotoWithVideo(chat.photo.photo_big, imageLocation, this.provider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWriteButtonClick() {
        if (this.userId != 0) {
            boolean z = true;
            if (this.imageUpdater != null) {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId()));
                if (user == null) {
                    user = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                }
                if (user == null) {
                    return;
                }
                ImageUpdater imageUpdater = this.imageUpdater;
                TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = user.photo;
                imageUpdater.openMenu((tLRPC$UserProfilePhoto == null || tLRPC$UserProfilePhoto.photo_big == null || (tLRPC$UserProfilePhoto instanceof TLRPC$TL_userProfilePhotoEmpty)) ? false : false, new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda34
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.this.lambda$onWriteButtonClick$27();
                    }
                }, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda9
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        ProfileActivity.this.lambda$onWriteButtonClick$28(dialogInterface);
                    }
                }, 0);
                this.cameraDrawable.setCurrentFrame(0);
                this.cameraDrawable.setCustomEndFrame(43);
                this.cellCameraDrawable.setCurrentFrame(0);
                this.cellCameraDrawable.setCustomEndFrame(43);
                this.writeButton.playAnimation();
                TextCell textCell = this.setAvatarCell;
                if (textCell != null) {
                    textCell.getImageView().playAnimation();
                    return;
                }
                return;
            } else if (this.playProfileAnimation != 0 && (this.parentLayout.getFragmentStack().get(this.parentLayout.getFragmentStack().size() - 2) instanceof ChatActivity)) {
                finishFragment();
                return;
            } else {
                TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(this.userId));
                if (user2 == null || (user2 instanceof TLRPC$TL_userEmpty)) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", this.userId);
                if (getMessagesController().checkCanOpenChat(bundle, this)) {
                    boolean z2 = this.arguments.getBoolean("removeFragmentOnChatOpen", true);
                    if (!AndroidUtilities.isTablet() && z2) {
                        NotificationCenter notificationCenter = getNotificationCenter();
                        int i = NotificationCenter.closeChats;
                        notificationCenter.removeObserver(this, i);
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, new Object[0]);
                    }
                    int i2 = getArguments().getInt("nearby_distance", -1);
                    if (i2 >= 0) {
                        bundle.putInt("nearby_distance", i2);
                    }
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    chatActivity.setPreloadedSticker(getMediaDataController().getGreetingsSticker(), false);
                    presentFragment(chatActivity, z2);
                    if (AndroidUtilities.isTablet()) {
                        finishFragment();
                        return;
                    }
                    return;
                }
                return;
            }
        }
        openDiscussion();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWriteButtonClick$27() {
        MessagesController.getInstance(this.currentAccount).deleteUserPhoto(null);
        this.cameraDrawable.setCurrentFrame(0);
        this.cellCameraDrawable.setCurrentFrame(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWriteButtonClick$28(DialogInterface dialogInterface) {
        if (!this.imageUpdater.isUploadingImage()) {
            this.cameraDrawable.setCustomEndFrame(86);
            this.cellCameraDrawable.setCustomEndFrame(86);
            this.writeButton.playAnimation();
            TextCell textCell = this.setAvatarCell;
            if (textCell != null) {
                textCell.getImageView().playAnimation();
                return;
            }
            return;
        }
        this.cameraDrawable.setCurrentFrame(0, false);
        this.cellCameraDrawable.setCurrentFrame(0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openDiscussion() {
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if (tLRPC$ChatFull == null || tLRPC$ChatFull.linked_chat_id == 0) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatInfo.linked_chat_id);
        if (getMessagesController().checkCanOpenChat(bundle, this)) {
            presentFragment(new ChatActivity(bundle));
        }
    }

    public boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, View view) {
        return onMemberClick(tLRPC$ChatParticipant, z, false, view);
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public boolean onMemberClick(final TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, boolean z2, View view) {
        boolean z3;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant;
        boolean z4;
        boolean z5;
        boolean z6;
        int i;
        String str;
        boolean z7 = false;
        if (getParentActivity() == null) {
            return false;
        }
        if (z) {
            final TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant.user_id));
            if (user != null && tLRPC$ChatParticipant.user_id != getUserConfig().getClientUserId()) {
                this.selectedUser = tLRPC$ChatParticipant.user_id;
                if (ChatObject.isChannel(this.currentChat)) {
                    TLRPC$ChannelParticipant tLRPC$ChannelParticipant2 = ((TLRPC$TL_chatChannelParticipant) tLRPC$ChatParticipant).channelParticipant;
                    getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant.user_id));
                    boolean canAddAdmins = ChatObject.canAddAdmins(this.currentChat);
                    if (canAddAdmins && ((tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator) || ((tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin) && !tLRPC$ChannelParticipant2.can_edit))) {
                        canAddAdmins = false;
                    }
                    boolean z8 = ChatObject.canBlockUsers(this.currentChat) && (!((tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin) || (tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) || tLRPC$ChannelParticipant2.can_edit);
                    z3 = tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin;
                    tLRPC$ChannelParticipant = tLRPC$ChannelParticipant2;
                    z4 = canAddAdmins;
                    z6 = z8;
                    z5 = this.currentChat.gigagroup ? false : z8;
                } else {
                    TLRPC$Chat tLRPC$Chat = this.currentChat;
                    boolean z9 = tLRPC$Chat.creator || ((tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipant) && (ChatObject.canBlockUsers(tLRPC$Chat) || tLRPC$ChatParticipant.inviter_id == getUserConfig().getClientUserId()));
                    z3 = tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin;
                    tLRPC$ChannelParticipant = null;
                    z4 = this.currentChat.creator;
                    z5 = z4;
                    z6 = z9;
                }
                final boolean z10 = z3;
                z7 = (z4 || z5 || z6) ? true : true;
                if (!z2 && z7) {
                    final TLRPC$ChannelParticipant tLRPC$ChannelParticipant3 = tLRPC$ChannelParticipant;
                    final Utilities.Callback callback = new Utilities.Callback() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda56
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            ProfileActivity.this.lambda$onMemberClick$29(tLRPC$ChannelParticipant3, user, tLRPC$ChatParticipant, z10, (Integer) obj);
                        }
                    };
                    ItemOptions scrimViewBackground = ItemOptions.makeOptions(this, view).setScrimViewBackground(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundWhite)));
                    int i2 = R.drawable.msg_admins;
                    if (z10) {
                        i = R.string.EditAdminRights;
                        str = "EditAdminRights";
                    } else {
                        i = R.string.SetAsAdmin;
                        str = "SetAsAdmin";
                    }
                    final TLRPC$ChannelParticipant tLRPC$ChannelParticipant4 = tLRPC$ChannelParticipant;
                    scrimViewBackground.addIf(z4, i2, LocaleController.getString(str, i), new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda31
                        @Override // java.lang.Runnable
                        public final void run() {
                            ProfileActivity.lambda$onMemberClick$30(Utilities.Callback.this);
                        }
                    }).addIf(z5, R.drawable.msg_permissions, LocaleController.getString("ChangePermissions", R.string.ChangePermissions), new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda41
                        @Override // java.lang.Runnable
                        public final void run() {
                            ProfileActivity.this.lambda$onMemberClick$32(tLRPC$ChannelParticipant4, tLRPC$ChatParticipant, user, callback);
                        }
                    }).addIf(z6, R.drawable.msg_remove, LocaleController.getString("KickFromGroup", R.string.KickFromGroup), true, new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda42
                        @Override // java.lang.Runnable
                        public final void run() {
                            ProfileActivity.this.lambda$onMemberClick$33(tLRPC$ChatParticipant);
                        }
                    }).setMinWidth(190).show();
                }
            }
            return z7;
        } else if (tLRPC$ChatParticipant.user_id == getUserConfig().getClientUserId()) {
            return false;
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", tLRPC$ChatParticipant.user_id);
            bundle.putBoolean("preload_messages", true);
            presentFragment(new ProfileActivity(bundle));
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMemberClick$29(TLRPC$ChannelParticipant tLRPC$ChannelParticipant, TLRPC$User tLRPC$User, TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, Integer num) {
        if (tLRPC$ChannelParticipant != null) {
            openRightsEdit(num.intValue(), tLRPC$User, tLRPC$ChatParticipant, tLRPC$ChannelParticipant.admin_rights, tLRPC$ChannelParticipant.banned_rights, tLRPC$ChannelParticipant.rank, z);
        } else {
            openRightsEdit(num.intValue(), tLRPC$User, tLRPC$ChatParticipant, null, null, "", z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onMemberClick$30(Utilities.Callback callback) {
        callback.run(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMemberClick$32(TLRPC$ChannelParticipant tLRPC$ChannelParticipant, TLRPC$ChatParticipant tLRPC$ChatParticipant, TLRPC$User tLRPC$User, final Utilities.Callback callback) {
        if ((tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) || (tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin)) {
            showDialog(new AlertDialog.Builder(getParentActivity(), this.resourcesProvider).setTitle(LocaleController.getString("AppName", R.string.AppName)).setMessage(LocaleController.formatString("AdminWillBeRemoved", R.string.AdminWillBeRemoved, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name))).setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ProfileActivity.lambda$onMemberClick$31(Utilities.Callback.this, dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create());
        } else {
            callback.run(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onMemberClick$31(Utilities.Callback callback, DialogInterface dialogInterface, int i) {
        callback.run(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMemberClick$33(TLRPC$ChatParticipant tLRPC$ChatParticipant) {
        kickUser(this.selectedUser, tLRPC$ChatParticipant);
    }

    private void openRightsEdit(final int i, final TLRPC$User tLRPC$User, final TLRPC$ChatParticipant tLRPC$ChatParticipant, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, final boolean z) {
        final boolean[] zArr = new boolean[1];
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(tLRPC$User.id, this.chatId, tLRPC$TL_chatAdminRights, this.currentChat.default_banned_rights, tLRPC$TL_chatBannedRights, str, i, true, false, null) { // from class: org.telegram.ui.ProfileActivity.36
            @Override // org.telegram.ui.ActionBar.BaseFragment
            public void onTransitionAnimationEnd(boolean z2, boolean z3) {
                if (!z2 && z3 && zArr[0] && BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                    BulletinFactory.createPromoteToAdminBulletin(ProfileActivity.this, tLRPC$User.first_name).show();
                }
            }
        };
        chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ProfileActivity.37
            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int i2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str2) {
                boolean z2;
                TLRPC$ChatParticipant tLRPC$TL_chatParticipant;
                int i3 = i;
                int i4 = 0;
                if (i3 != 0) {
                    if (i3 == 1 && i2 == 0 && ProfileActivity.this.currentChat.megagroup && ProfileActivity.this.chatInfo != null && ProfileActivity.this.chatInfo.participants != null) {
                        int i5 = 0;
                        while (true) {
                            if (i5 >= ProfileActivity.this.chatInfo.participants.participants.size()) {
                                z2 = false;
                                break;
                            } else if (MessageObject.getPeerId(((TLRPC$TL_chatChannelParticipant) ProfileActivity.this.chatInfo.participants.participants.get(i5)).channelParticipant.peer) == tLRPC$ChatParticipant.user_id) {
                                ProfileActivity.this.chatInfo.participants_count--;
                                ProfileActivity.this.chatInfo.participants.participants.remove(i5);
                                z2 = true;
                                break;
                            } else {
                                i5++;
                            }
                        }
                        if (ProfileActivity.this.chatInfo != null && ProfileActivity.this.chatInfo.participants != null) {
                            while (true) {
                                if (i4 >= ProfileActivity.this.chatInfo.participants.participants.size()) {
                                    break;
                                } else if (ProfileActivity.this.chatInfo.participants.participants.get(i4).user_id == tLRPC$ChatParticipant.user_id) {
                                    ProfileActivity.this.chatInfo.participants.participants.remove(i4);
                                    z2 = true;
                                    break;
                                } else {
                                    i4++;
                                }
                            }
                        }
                        if (z2) {
                            ProfileActivity.this.updateOnlineCount(true);
                            ProfileActivity.this.updateRowsIds();
                            ProfileActivity.this.listAdapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    }
                    return;
                }
                TLRPC$ChatParticipant tLRPC$ChatParticipant2 = tLRPC$ChatParticipant;
                if (tLRPC$ChatParticipant2 instanceof TLRPC$TL_chatChannelParticipant) {
                    TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant = (TLRPC$TL_chatChannelParticipant) tLRPC$ChatParticipant2;
                    if (i2 == 1) {
                        TLRPC$TL_channelParticipantAdmin tLRPC$TL_channelParticipantAdmin = new TLRPC$TL_channelParticipantAdmin();
                        tLRPC$TL_chatChannelParticipant.channelParticipant = tLRPC$TL_channelParticipantAdmin;
                        tLRPC$TL_channelParticipantAdmin.flags |= 4;
                    } else {
                        tLRPC$TL_chatChannelParticipant.channelParticipant = new TLRPC$TL_channelParticipant();
                    }
                    tLRPC$TL_chatChannelParticipant.channelParticipant.inviter_id = ProfileActivity.this.getUserConfig().getClientUserId();
                    tLRPC$TL_chatChannelParticipant.channelParticipant.peer = new TLRPC$TL_peerUser();
                    TLRPC$ChannelParticipant tLRPC$ChannelParticipant = tLRPC$TL_chatChannelParticipant.channelParticipant;
                    TLRPC$Peer tLRPC$Peer = tLRPC$ChannelParticipant.peer;
                    TLRPC$ChatParticipant tLRPC$ChatParticipant3 = tLRPC$ChatParticipant;
                    tLRPC$Peer.user_id = tLRPC$ChatParticipant3.user_id;
                    tLRPC$ChannelParticipant.date = tLRPC$ChatParticipant3.date;
                    tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights2;
                    tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights2;
                    tLRPC$ChannelParticipant.rank = str2;
                } else if (tLRPC$ChatParticipant2 != null) {
                    if (i2 == 1) {
                        tLRPC$TL_chatParticipant = new TLRPC$TL_chatParticipantAdmin();
                    } else {
                        tLRPC$TL_chatParticipant = new TLRPC$TL_chatParticipant();
                    }
                    TLRPC$ChatParticipant tLRPC$ChatParticipant4 = tLRPC$ChatParticipant;
                    tLRPC$TL_chatParticipant.user_id = tLRPC$ChatParticipant4.user_id;
                    tLRPC$TL_chatParticipant.date = tLRPC$ChatParticipant4.date;
                    tLRPC$TL_chatParticipant.inviter_id = tLRPC$ChatParticipant4.inviter_id;
                    int indexOf = ProfileActivity.this.chatInfo.participants.participants.indexOf(tLRPC$ChatParticipant);
                    if (indexOf >= 0) {
                        ProfileActivity.this.chatInfo.participants.participants.set(indexOf, tLRPC$TL_chatParticipant);
                    }
                }
                if (i2 != 1 || z) {
                    return;
                }
                zArr[0] = true;
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC$User tLRPC$User2) {
                ProfileActivity.this.undoView.showWithAction(-ProfileActivity.this.chatId, ProfileActivity.this.currentChat.megagroup ? 10 : 9, tLRPC$User2);
            }
        });
        presentFragment(chatRightsEditActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean processOnClickOrPress(final int i, final View view, final float f, final float f2) {
        TLRPC$Chat chat;
        String publicUsername;
        String sb;
        String str;
        TLRPC$UserFull tLRPC$UserFull;
        View view2 = view;
        if (i == this.usernameRow || i == this.setUsernameRow) {
            if (this.userId != 0) {
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
                publicUsername = UserObject.getPublicUsername(user);
                if (user == null || publicUsername == null) {
                    return false;
                }
            } else if (this.chatId == 0 || (chat = getMessagesController().getChat(Long.valueOf(this.chatId))) == null || (this.topicId == 0 && !ChatObject.isPublic(chat))) {
                return false;
            } else {
                publicUsername = ChatObject.getPublicUsername(chat);
            }
            if (this.userId == 0) {
                TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(this.chatId));
                if (ChatObject.isPublic(chat2)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("https://");
                    sb2.append(getMessagesController().linkPrefix);
                    sb2.append("/");
                    sb2.append(ChatObject.getPublicUsername(chat2));
                    sb2.append(this.topicId != 0 ? "/" + this.topicId : "");
                    sb = sb2.toString();
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("https://");
                    sb3.append(getMessagesController().linkPrefix);
                    sb3.append("/c/");
                    sb3.append(chat2.id);
                    sb3.append(this.topicId != 0 ? "/" + this.topicId : "");
                    sb = sb3.toString();
                }
                String str2 = sb;
                showDialog(new 38(getParentActivity(), null, str2, false, str2, false));
                return true;
            }
            try {
                BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("UsernameCopied", R.string.UsernameCopied), this.resourcesProvider).show();
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", "@" + publicUsername));
                return true;
            } catch (Exception e) {
                FileLog.e(e);
                return true;
            }
        } else if (i == this.phoneRow || i == this.numberRow) {
            final TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user2 == null || (str = user2.phone) == null || str.length() == 0 || getParentActivity() == null) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            if (i == this.phoneRow) {
                TLRPC$UserFull tLRPC$UserFull2 = this.userInfo;
                if (tLRPC$UserFull2 != null && tLRPC$UserFull2.phone_calls_available) {
                    arrayList3.add(Integer.valueOf(R.drawable.msg_calls));
                    arrayList.add(LocaleController.getString("CallViaTelegram", R.string.CallViaTelegram));
                    arrayList2.add(2);
                    if (Build.VERSION.SDK_INT >= 18 && this.userInfo.video_calls_available) {
                        arrayList3.add(Integer.valueOf(R.drawable.msg_videocall));
                        arrayList.add(LocaleController.getString("VideoCallViaTelegram", R.string.VideoCallViaTelegram));
                        arrayList2.add(3);
                    }
                }
                if (!this.isFragmentPhoneNumber) {
                    arrayList3.add(Integer.valueOf(R.drawable.msg_calls_regular));
                    arrayList.add(LocaleController.getString("Call", R.string.Call));
                    arrayList2.add(0);
                }
            }
            arrayList3.add(Integer.valueOf(R.drawable.msg_copy));
            arrayList.add(LocaleController.getString("Copy", R.string.Copy));
            arrayList2.add(1);
            final AtomicReference atomicReference = new AtomicReference();
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this, getContext(), R.drawable.popup_fixed_alert, this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.39
                Path path = new Path();

                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view3, long j) {
                    canvas.save();
                    this.path.rewind();
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(view3.getLeft(), view3.getTop(), view3.getRight(), view3.getBottom());
                    this.path.addRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), Path.Direction.CW);
                    canvas.clipPath(this.path);
                    boolean drawChild = super.drawChild(canvas, view3, j);
                    canvas.restore();
                    return drawChild;
                }
            };
            actionBarPopupWindowLayout.setFitItems(true);
            for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                final int intValue = ((Integer) arrayList2.get(i2)).intValue();
                ActionBarMenuItem.addItem(actionBarPopupWindowLayout, ((Integer) arrayList3.get(i2)).intValue(), (CharSequence) arrayList.get(i2), false, this.resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda17
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        ProfileActivity.this.lambda$processOnClickOrPress$34(atomicReference, intValue, user2, view3);
                    }
                });
            }
            if (this.isFragmentPhoneNumber) {
                FrameLayout frameLayout = new FrameLayout(getContext());
                frameLayout.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuSeparator, this.resourcesProvider));
                actionBarPopupWindowLayout.addView((View) frameLayout, LayoutHelper.createLinear(-1, 8));
                TextView textView = new TextView(getContext());
                textView.setPadding(AndroidUtilities.dp(13.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(8.0f));
                textView.setTextSize(1, 13.0f);
                textView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem, this.resourcesProvider));
                textView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText, this.resourcesProvider));
                textView.setBackground(Theme.createRadSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector, this.resourcesProvider), 0, 6));
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.getString(R.string.AnonymousNumberNotice)));
                int indexOf = TextUtils.indexOf((CharSequence) spannableStringBuilder, '*');
                int lastIndexOf = TextUtils.lastIndexOf(spannableStringBuilder, '*');
                if (indexOf != -1 && lastIndexOf != -1 && indexOf != lastIndexOf) {
                    spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                    spannableStringBuilder.replace(indexOf, indexOf + 1, (CharSequence) "");
                    int i3 = lastIndexOf - 1;
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), indexOf, i3, 33);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(textView.getLinkTextColors().getDefaultColor()), indexOf, i3, 33);
                }
                textView.setText(spannableStringBuilder);
                textView.setOnClickListener(ProfileActivity$$ExternalSyntheticLambda21.INSTANCE);
                int i4 = R.id.fit_width_tag;
                frameLayout.setTag(i4, 1);
                textView.setTag(i4, 1);
                actionBarPopupWindowLayout.addView((View) textView, LayoutHelper.createLinear(-2, -2));
            }
            ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
            actionBarPopupWindow.setPauseNotifications(true);
            actionBarPopupWindow.setDismissAnimationDuration(220);
            actionBarPopupWindow.setOutsideTouchable(true);
            actionBarPopupWindow.setClippingEnabled(true);
            actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
            actionBarPopupWindow.setFocusable(true);
            actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
            actionBarPopupWindow.setInputMethodMode(2);
            actionBarPopupWindow.getContentView().setFocusableInTouchMode(true);
            atomicReference.set(actionBarPopupWindow);
            float f3 = f;
            float f4 = f2;
            while (view2 != getFragmentView() && view2 != null) {
                f3 += view2.getX();
                f4 += view2.getY();
                view2 = (View) view2.getParent();
            }
            if (AndroidUtilities.isTablet()) {
                ViewGroup view3 = this.parentLayout.getView();
                f3 += view3.getX() + view3.getPaddingLeft();
                f4 += view3.getY() + view3.getPaddingTop();
            }
            actionBarPopupWindow.showAtLocation(getFragmentView(), 0, (int) (f3 - (actionBarPopupWindowLayout.getMeasuredWidth() / 2.0f)), (int) f4);
            actionBarPopupWindow.dimBehind();
            return true;
        } else if (i == this.channelInfoRow || i == this.userInfoRow || i == this.locationRow || i == this.bioRow) {
            if (i == this.bioRow && ((tLRPC$UserFull = this.userInfo) == null || TextUtils.isEmpty(tLRPC$UserFull.about))) {
                return false;
            }
            if ((view2 instanceof AboutLinkCell) && ((AboutLinkCell) view2).onClick()) {
                return false;
            }
            String str3 = null;
            if (i == this.locationRow) {
                TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
                if (tLRPC$ChatFull != null) {
                    TLRPC$ChannelLocation tLRPC$ChannelLocation = tLRPC$ChatFull.location;
                    if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocation) {
                        str3 = ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address;
                    }
                }
            } else if (i == this.channelInfoRow) {
                TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
                if (tLRPC$ChatFull2 != null) {
                    str3 = tLRPC$ChatFull2.about;
                }
            } else {
                TLRPC$UserFull tLRPC$UserFull3 = this.userInfo;
                if (tLRPC$UserFull3 != null) {
                    str3 = tLRPC$UserFull3.about;
                }
            }
            final String str4 = str3;
            if (TextUtils.isEmpty(str4)) {
                return false;
            }
            final String[] strArr = {TranslateController.UNKNOWN_LANGUAGE};
            final boolean isContextTranslateEnabled = MessagesController.getInstance(this.currentAccount).getTranslateController().isContextTranslateEnabled();
            final boolean[] zArr = new boolean[1];
            zArr[0] = i == this.bioRow || i == this.channelInfoRow || i == this.userInfoRow;
            final String language = LocaleController.getInstance().getCurrentLocale().getLanguage();
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.this.lambda$processOnClickOrPress$38(zArr, str4, i, strArr, language, f, f2, view);
                }
            };
            if (zArr[0]) {
                if (LanguageDetector.hasSupport()) {
                    LanguageDetector.detectLanguage(str4, new LanguageDetector.StringCallback() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda53
                        @Override // org.telegram.messenger.LanguageDetector.StringCallback
                        public final void run(String str5) {
                            ProfileActivity.this.lambda$processOnClickOrPress$39(strArr, zArr, language, isContextTranslateEnabled, runnable, str5);
                        }
                    }, new LanguageDetector.ExceptionCallback() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda52
                        @Override // org.telegram.messenger.LanguageDetector.ExceptionCallback
                        public final void run(Exception exc) {
                            ProfileActivity.lambda$processOnClickOrPress$40(runnable, exc);
                        }
                    });
                } else {
                    runnable.run();
                }
            } else {
                runnable.run();
            }
            return true;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 38 extends ShareAlert {
        38(Context context, ArrayList arrayList, String str, boolean z, String str2, boolean z2) {
            super(context, arrayList, str, z, str2, z2);
        }

        @Override // org.telegram.ui.Components.ShareAlert
        protected void onSend(final LongSparseArray<TLRPC$Dialog> longSparseArray, final int i, TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$38$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.38.this.lambda$onSend$0(longSparseArray, i);
                }
            }, 250L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSend$0(LongSparseArray longSparseArray, int i) {
            BulletinFactory.createInviteSentBulletin(ProfileActivity.this.getParentActivity(), ProfileActivity.this.contentView, longSparseArray.size(), longSparseArray.size() == 1 ? ((TLRPC$Dialog) longSparseArray.valueAt(0)).id : 0L, i, getThemedColor(Theme.key_undo_background), getThemedColor(Theme.key_undo_infoColor)).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processOnClickOrPress$34(AtomicReference atomicReference, int i, TLRPC$User tLRPC$User, View view) {
        ((ActionBarPopupWindow) atomicReference.get()).dismiss();
        if (i == 0) {
            try {
                Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:+" + tLRPC$User.phone));
                intent.addFlags(268435456);
                getParentActivity().startActivityForResult(intent, 500);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (i == 1) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", "+" + tLRPC$User.phone));
                if (AndroidUtilities.shouldShowClipboardToast()) {
                    BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("PhoneCopied", R.string.PhoneCopied)).show();
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        } else if ((i == 2 || i == 3) && getParentActivity() != null) {
            boolean z = i == 3;
            TLRPC$UserFull tLRPC$UserFull = this.userInfo;
            VoIPHelper.startCall(tLRPC$User, z, tLRPC$UserFull != null && tLRPC$UserFull.video_calls_available, getParentActivity(), this.userInfo, getAccountInstance());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processOnClickOrPress$35(View view) {
        try {
            view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://fragment.com")));
        } catch (ActivityNotFoundException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processOnClickOrPress$38(boolean[] zArr, final String str, final int i, final String[] strArr, final String str2, float f, float f2, View view) {
        if (getParentActivity() == null) {
            return;
        }
        boolean z = false;
        CharSequence[] charSequenceArr = zArr[0] ? new CharSequence[]{LocaleController.getString("Copy", R.string.Copy), LocaleController.getString("TranslateMessage", R.string.TranslateMessage)} : new CharSequence[]{LocaleController.getString("Copy", R.string.Copy)};
        int[] iArr = zArr[0] ? new int[]{R.drawable.msg_copy, R.drawable.msg_translate} : new int[]{R.drawable.msg_copy};
        final AtomicReference atomicReference = new AtomicReference();
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this, getContext(), R.drawable.popup_fixed_alert, this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.40
            Path path = new Path();

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view2, long j) {
                canvas.save();
                this.path.rewind();
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                this.path.addRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), Path.Direction.CW);
                canvas.clipPath(this.path);
                boolean drawChild = super.drawChild(canvas, view2, j);
                canvas.restore();
                return drawChild;
            }
        };
        actionBarPopupWindowLayout.setFitItems(true);
        int i2 = 0;
        while (i2 < iArr.length) {
            final int i3 = i2;
            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, iArr[i2], charSequenceArr[i2], z, this.resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda16
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ProfileActivity.this.lambda$processOnClickOrPress$37(atomicReference, i3, str, i, strArr, str2, view2);
                }
            });
            i2++;
            z = false;
        }
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindow.setDismissAnimationDuration(220);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.getContentView().setFocusableInTouchMode(true);
        atomicReference.set(actionBarPopupWindow);
        float f3 = f;
        float f4 = f2;
        for (View view2 = view; view2 != getFragmentView(); view2 = (View) view2.getParent()) {
            f3 += view2.getX();
            f4 += view2.getY();
        }
        if (AndroidUtilities.isTablet()) {
            ViewGroup view3 = this.parentLayout.getView();
            f3 += view3.getX() + view3.getPaddingLeft();
            f4 += view3.getY() + view3.getPaddingTop();
        }
        actionBarPopupWindow.showAtLocation(getFragmentView(), 0, (int) (f3 - (actionBarPopupWindowLayout.getMeasuredWidth() / 2.0f)), (int) f4);
        actionBarPopupWindow.dimBehind();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processOnClickOrPress$37(AtomicReference atomicReference, int i, String str, int i2, String[] strArr, String str2, View view) {
        ((ActionBarPopupWindow) atomicReference.get()).dismiss();
        try {
            if (i == 0) {
                AndroidUtilities.addToClipboard(str);
                if (i2 == this.bioRow) {
                    BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("BioCopied", R.string.BioCopied)).show();
                } else {
                    BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("TextCopied", R.string.TextCopied)).show();
                }
            } else if (i != 1) {
            } else {
                TranslateAlert2.showAlert(this.fragmentView.getContext(), this, this.currentAccount, strArr[0], str2, str, null, false, new Utilities.CallbackReturn() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda57
                    @Override // org.telegram.messenger.Utilities.CallbackReturn
                    public final Object run(Object obj) {
                        Boolean lambda$processOnClickOrPress$36;
                        lambda$processOnClickOrPress$36 = ProfileActivity.this.lambda$processOnClickOrPress$36((URLSpan) obj);
                        return lambda$processOnClickOrPress$36;
                    }
                }, null);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$processOnClickOrPress$36(URLSpan uRLSpan) {
        if (uRLSpan != null) {
            openUrl(uRLSpan.getURL(), null);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processOnClickOrPress$39(String[] strArr, boolean[] zArr, String str, boolean z, Runnable runnable, String str2) {
        TLRPC$Chat tLRPC$Chat;
        strArr[0] = str2;
        zArr[0] = str2 != null && (!str2.equals(str) || str2.equals(TranslateController.UNKNOWN_LANGUAGE)) && ((z && !RestrictedLanguagesSelectActivity.getRestrictedLanguages().contains(str2)) || ((tLRPC$Chat = this.currentChat) != null && ((tLRPC$Chat.has_link || ChatObject.isPublic(tLRPC$Chat)) && ("uk".equals(str2) || "ru".equals(str2)))));
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processOnClickOrPress$40(Runnable runnable, Exception exc) {
        FileLog.e("mlkit: failed to detect language in selection", exc);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void leaveChatPressed() {
        boolean isForum = ChatObject.isForum(this.currentChat);
        AlertsCreator.createClearOrDeleteDialogAlert(this, false, this.currentChat, null, false, isForum, !isForum, new MessagesStorage.BooleanCallback() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda54
            @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
            public final void run(boolean z) {
                ProfileActivity.this.lambda$leaveChatPressed$41(z);
            }
        }, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$leaveChatPressed$41(boolean z) {
        this.playProfileAnimation = 0;
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.closeChats;
        notificationCenter.removeObserver(this, i);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, new Object[0]);
        finishFragment();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needDeleteDialog, Long.valueOf(-this.currentChat.id), null, this.currentChat, Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getChannelParticipants(boolean z) {
        LongSparseArray<TLRPC$ChatParticipant> longSparseArray;
        if (this.loadingUsers || (longSparseArray = this.participantsMap) == null || this.chatInfo == null) {
            return;
        }
        this.loadingUsers = true;
        final int i = (longSparseArray.size() == 0 || !z) ? 0 : 300;
        final TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants = new TLRPC$TL_channels_getParticipants();
        tLRPC$TL_channels_getParticipants.channel = getMessagesController().getInputChannel(this.chatId);
        tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsRecent();
        tLRPC$TL_channels_getParticipants.offset = z ? 0 : this.participantsMap.size();
        tLRPC$TL_channels_getParticipants.limit = 200;
        getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_channels_getParticipants, new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda61
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                ProfileActivity.this.lambda$getChannelParticipants$44(tLRPC$TL_channels_getParticipants, i, tLObject, tLRPC$TL_error);
            }
        }), this.classGuid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getChannelParticipants$43(final TLRPC$TL_error tLRPC$TL_error, final TLObject tLObject, final TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants) {
        getNotificationCenter().doOnIdle(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.lambda$getChannelParticipants$42(tLRPC$TL_error, tLObject, tLRPC$TL_channels_getParticipants);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getChannelParticipants$44(final TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants, int i, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.lambda$getChannelParticipants$43(tLRPC$TL_error, tLObject, tLRPC$TL_channels_getParticipants);
            }
        }, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getChannelParticipants$42(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_channels_channelParticipants tLRPC$TL_channels_channelParticipants = (TLRPC$TL_channels_channelParticipants) tLObject;
            getMessagesController().putUsers(tLRPC$TL_channels_channelParticipants.users, false);
            getMessagesController().putChats(tLRPC$TL_channels_channelParticipants.chats, false);
            if (tLRPC$TL_channels_channelParticipants.users.size() < 200) {
                this.usersEndReached = true;
            }
            if (tLRPC$TL_channels_getParticipants.offset == 0) {
                this.participantsMap.clear();
                this.chatInfo.participants = new TLRPC$TL_chatParticipants();
                getMessagesStorage().putUsersAndChats(tLRPC$TL_channels_channelParticipants.users, tLRPC$TL_channels_channelParticipants.chats, true, true);
                getMessagesStorage().updateChannelUsers(this.chatId, tLRPC$TL_channels_channelParticipants.participants);
            }
            for (int i = 0; i < tLRPC$TL_channels_channelParticipants.participants.size(); i++) {
                TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant = new TLRPC$TL_chatChannelParticipant();
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = tLRPC$TL_channels_channelParticipants.participants.get(i);
                tLRPC$TL_chatChannelParticipant.channelParticipant = tLRPC$ChannelParticipant;
                tLRPC$TL_chatChannelParticipant.inviter_id = tLRPC$ChannelParticipant.inviter_id;
                long peerId = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
                tLRPC$TL_chatChannelParticipant.user_id = peerId;
                tLRPC$TL_chatChannelParticipant.date = tLRPC$TL_chatChannelParticipant.channelParticipant.date;
                if (this.participantsMap.indexOfKey(peerId) < 0) {
                    TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
                    if (tLRPC$ChatFull.participants == null) {
                        tLRPC$ChatFull.participants = new TLRPC$TL_chatParticipants();
                    }
                    this.chatInfo.participants.participants.add(tLRPC$TL_chatChannelParticipant);
                    this.participantsMap.put(tLRPC$TL_chatChannelParticipant.user_id, tLRPC$TL_chatChannelParticipant);
                }
            }
        }
        this.loadingUsers = false;
        saveScrollPosition();
        updateListAnimated(true);
    }

    private void setMediaHeaderVisible(boolean z) {
        if (this.mediaHeaderVisible == z) {
            return;
        }
        this.mediaHeaderVisible = z;
        AnimatorSet animatorSet = this.headerAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = this.headerShadowAnimatorSet;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
        }
        final ActionBarMenuItem searchItem = this.sharedMediaLayout.getSearchItem();
        if (!this.mediaHeaderVisible) {
            if (this.callItemVisible) {
                this.callItem.setVisibility(0);
            }
            if (this.videoCallItemVisible) {
                this.videoCallItem.setVisibility(0);
            }
            if (this.editItemVisible) {
                this.editItem.setVisibility(0);
            }
            this.otherItem.setVisibility(0);
        } else {
            if (this.sharedMediaLayout.isSearchItemVisible()) {
                searchItem.setVisibility(0);
            }
            if (this.sharedMediaLayout.isCalendarItemVisible()) {
                this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(0);
            } else {
                this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(4);
            }
        }
        updateStoriesViewBounds(false);
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.createMenu().requestLayout();
        }
        ArrayList arrayList = new ArrayList();
        ActionBarMenuItem actionBarMenuItem = this.callItem;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 0.0f : 1.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem, property, fArr));
        ActionBarMenuItem actionBarMenuItem2 = this.videoCallItem;
        Property property2 = View.ALPHA;
        float[] fArr2 = new float[1];
        fArr2[0] = z ? 0.0f : 1.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem2, property2, fArr2));
        ActionBarMenuItem actionBarMenuItem3 = this.otherItem;
        Property property3 = View.ALPHA;
        float[] fArr3 = new float[1];
        fArr3[0] = z ? 0.0f : 1.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem3, property3, fArr3));
        ActionBarMenuItem actionBarMenuItem4 = this.editItem;
        Property property4 = View.ALPHA;
        float[] fArr4 = new float[1];
        fArr4[0] = z ? 0.0f : 1.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem4, property4, fArr4));
        ActionBarMenuItem actionBarMenuItem5 = this.callItem;
        Property property5 = View.TRANSLATION_Y;
        float[] fArr5 = new float[1];
        fArr5[0] = z ? -AndroidUtilities.dp(10.0f) : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem5, property5, fArr5));
        ActionBarMenuItem actionBarMenuItem6 = this.videoCallItem;
        Property property6 = View.TRANSLATION_Y;
        float[] fArr6 = new float[1];
        fArr6[0] = z ? -AndroidUtilities.dp(10.0f) : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem6, property6, fArr6));
        ActionBarMenuItem actionBarMenuItem7 = this.otherItem;
        Property property7 = View.TRANSLATION_Y;
        float[] fArr7 = new float[1];
        fArr7[0] = z ? -AndroidUtilities.dp(10.0f) : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem7, property7, fArr7));
        ActionBarMenuItem actionBarMenuItem8 = this.editItem;
        Property property8 = View.TRANSLATION_Y;
        float[] fArr8 = new float[1];
        fArr8[0] = z ? -AndroidUtilities.dp(10.0f) : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem8, property8, fArr8));
        Property property9 = View.ALPHA;
        float[] fArr9 = new float[1];
        fArr9[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(searchItem, property9, fArr9));
        Property property10 = View.TRANSLATION_Y;
        float[] fArr10 = new float[1];
        fArr10[0] = z ? 0.0f : AndroidUtilities.dp(10.0f);
        arrayList.add(ObjectAnimator.ofFloat(searchItem, property10, fArr10));
        ImageView imageView = this.sharedMediaLayout.photoVideoOptionsItem;
        Property property11 = View.ALPHA;
        float[] fArr11 = new float[1];
        fArr11[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(imageView, property11, fArr11));
        ImageView imageView2 = this.sharedMediaLayout.photoVideoOptionsItem;
        Property property12 = View.TRANSLATION_Y;
        float[] fArr12 = new float[1];
        fArr12[0] = z ? 0.0f : AndroidUtilities.dp(10.0f);
        arrayList.add(ObjectAnimator.ofFloat(imageView2, property12, fArr12));
        ActionBar actionBar2 = this.actionBar;
        Property<ActionBar, Float> property13 = this.ACTIONBAR_HEADER_PROGRESS;
        float[] fArr13 = new float[1];
        fArr13[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBar2, property13, fArr13));
        SimpleTextView simpleTextView = this.onlineTextView[1];
        Property property14 = View.ALPHA;
        float[] fArr14 = new float[1];
        fArr14[0] = z ? 0.0f : 1.0f;
        arrayList.add(ObjectAnimator.ofFloat(simpleTextView, property14, fArr14));
        AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher = this.mediaCounterTextView;
        Property property15 = View.ALPHA;
        float[] fArr15 = new float[1];
        fArr15[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(clippingTextViewSwitcher, property15, fArr15));
        if (z) {
            arrayList.add(ObjectAnimator.ofFloat(this, this.HEADER_SHADOW, 0.0f));
        }
        if (this.storyView != null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ProfileActivity.this.lambda$setMediaHeaderVisible$45(valueAnimator);
                }
            });
            arrayList.add(ofFloat);
        }
        AnimatorSet animatorSet3 = new AnimatorSet();
        this.headerAnimatorSet = animatorSet3;
        animatorSet3.playTogether(arrayList);
        this.headerAnimatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.headerAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.42
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ProfileActivity.this.headerAnimatorSet != null) {
                    if (ProfileActivity.this.mediaHeaderVisible) {
                        if (ProfileActivity.this.callItemVisible) {
                            ProfileActivity.this.callItem.setVisibility(8);
                        }
                        if (ProfileActivity.this.videoCallItemVisible) {
                            ProfileActivity.this.videoCallItem.setVisibility(8);
                        }
                        if (ProfileActivity.this.editItemVisible) {
                            ProfileActivity.this.editItem.setVisibility(8);
                        }
                        ProfileActivity.this.otherItem.setVisibility(8);
                    } else {
                        if (ProfileActivity.this.sharedMediaLayout.isSearchItemVisible()) {
                            searchItem.setVisibility(0);
                        }
                        ProfileActivity.this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(4);
                        ProfileActivity.this.headerShadowAnimatorSet = new AnimatorSet();
                        AnimatorSet animatorSet4 = ProfileActivity.this.headerShadowAnimatorSet;
                        ProfileActivity profileActivity = ProfileActivity.this;
                        animatorSet4.playTogether(ObjectAnimator.ofFloat(profileActivity, profileActivity.HEADER_SHADOW, 1.0f));
                        ProfileActivity.this.headerShadowAnimatorSet.setDuration(100L);
                        ProfileActivity.this.headerShadowAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.42.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator2) {
                                ProfileActivity.this.headerShadowAnimatorSet = null;
                            }
                        });
                        ProfileActivity.this.headerShadowAnimatorSet.start();
                    }
                }
                ProfileActivity.this.updateStoriesViewBounds(false);
                ProfileActivity.this.headerAnimatorSet = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                ProfileActivity.this.headerAnimatorSet = null;
            }
        });
        this.headerAnimatorSet.setDuration(150L);
        this.headerAnimatorSet.start();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setMediaHeaderVisible$45(ValueAnimator valueAnimator) {
        updateStoriesViewBounds(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openAddMember() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("addToGroup", true);
        bundle.putLong("chatId", this.currentChat.id);
        GroupCreateActivity groupCreateActivity = new GroupCreateActivity(bundle);
        groupCreateActivity.setInfo(this.chatInfo);
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if (tLRPC$ChatFull != null && tLRPC$ChatFull.participants != null) {
            LongSparseArray<TLObject> longSparseArray = new LongSparseArray<>();
            for (int i = 0; i < this.chatInfo.participants.participants.size(); i++) {
                longSparseArray.put(this.chatInfo.participants.participants.get(i).user_id, null);
            }
            groupCreateActivity.setIgnoreUsers(longSparseArray);
        }
        groupCreateActivity.setDelegate(new GroupCreateActivity.ContactsAddActivityDelegate() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda67
            @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
            public final void didSelectUsers(ArrayList arrayList, int i2) {
                ProfileActivity.this.lambda$openAddMember$49(arrayList, i2);
            }

            @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
            public /* synthetic */ void needAddBot(TLRPC$User tLRPC$User) {
                GroupCreateActivity.ContactsAddActivityDelegate.-CC.$default$needAddBot(this, tLRPC$User);
            }
        });
        presentFragment(groupCreateActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openAddMember$49(ArrayList arrayList, int i) {
        TLRPC$ChatParticipants tLRPC$ChatParticipants;
        final HashSet hashSet = new HashSet();
        final ArrayList arrayList2 = new ArrayList();
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if (tLRPC$ChatFull != null && (tLRPC$ChatParticipants = tLRPC$ChatFull.participants) != null && tLRPC$ChatParticipants.participants != null) {
            for (int i2 = 0; i2 < this.chatInfo.participants.participants.size(); i2++) {
                hashSet.add(Long.valueOf(this.chatInfo.participants.participants.get(i2).user_id));
            }
        }
        getMessagesController().addUsersToChat(this.currentChat, this, arrayList, i, new androidx.core.util.Consumer() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda25
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                arrayList2.add((TLRPC$User) obj);
            }
        }, new androidx.core.util.Consumer() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda26
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                ProfileActivity.this.lambda$openAddMember$47((TLRPC$User) obj);
            }
        }, new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.lambda$openAddMember$48(arrayList2, hashSet);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openAddMember$47(TLRPC$User tLRPC$User) {
        for (int i = 0; i < this.chatInfo.participants.participants.size(); i++) {
            if (this.chatInfo.participants.participants.get(i).user_id == tLRPC$User.id) {
                this.chatInfo.participants.participants.remove(i);
                updateListAnimated(true);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openAddMember$48(ArrayList arrayList, HashSet hashSet) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$User tLRPC$User = (TLRPC$User) arrayList.get(i);
            if (!hashSet.contains(Long.valueOf(tLRPC$User.id))) {
                TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
                if (tLRPC$ChatFull.participants == null) {
                    tLRPC$ChatFull.participants = new TLRPC$TL_chatParticipants();
                }
                if (ChatObject.isChannel(this.currentChat)) {
                    TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant = new TLRPC$TL_chatChannelParticipant();
                    TLRPC$TL_channelParticipant tLRPC$TL_channelParticipant = new TLRPC$TL_channelParticipant();
                    tLRPC$TL_chatChannelParticipant.channelParticipant = tLRPC$TL_channelParticipant;
                    tLRPC$TL_channelParticipant.inviter_id = getUserConfig().getClientUserId();
                    tLRPC$TL_chatChannelParticipant.channelParticipant.peer = new TLRPC$TL_peerUser();
                    TLRPC$ChannelParticipant tLRPC$ChannelParticipant = tLRPC$TL_chatChannelParticipant.channelParticipant;
                    tLRPC$ChannelParticipant.peer.user_id = tLRPC$User.id;
                    tLRPC$ChannelParticipant.date = getConnectionsManager().getCurrentTime();
                    tLRPC$TL_chatChannelParticipant.user_id = tLRPC$User.id;
                    this.chatInfo.participants.participants.add(tLRPC$TL_chatChannelParticipant);
                } else {
                    TLRPC$TL_chatParticipant tLRPC$TL_chatParticipant = new TLRPC$TL_chatParticipant();
                    tLRPC$TL_chatParticipant.user_id = tLRPC$User.id;
                    tLRPC$TL_chatParticipant.inviter_id = getAccountInstance().getUserConfig().clientUserId;
                    this.chatInfo.participants.participants.add(tLRPC$TL_chatParticipant);
                }
                this.chatInfo.participants_count++;
                getMessagesController().putUser(tLRPC$User, false);
            }
        }
        updateListAnimated(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkListViewScroll() {
        View view;
        if (this.listView.getVisibility() != 0) {
            return;
        }
        if (this.sharedMediaLayoutAttached) {
            this.sharedMediaLayout.setVisibleHeight(this.listView.getMeasuredHeight() - this.sharedMediaLayout.getTop());
        }
        if (this.listView.getChildCount() <= 0 || this.openAnimationInProgress) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.listView.getChildCount()) {
                view = null;
                break;
            }
            RecyclerListView recyclerListView = this.listView;
            if (recyclerListView.getChildAdapterPosition(recyclerListView.getChildAt(i)) == 0) {
                view = this.listView.getChildAt(i);
                break;
            }
            i++;
        }
        RecyclerListView.Holder holder = view != null ? (RecyclerListView.Holder) this.listView.findContainingViewHolder(view) : null;
        int i2 = ((view == null ? 0 : view.getTop()) < 0 || (holder != null ? holder.getAdapterPosition() : -1) != 0) ? 0 : 0;
        boolean z = this.imageUpdater == null && this.actionBar.isSearchFieldVisible();
        int i3 = this.sharedMediaRow;
        if (i3 != -1 && !z) {
            RecyclerListView.Holder holder2 = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(i3);
            z = holder2 != null && holder2.itemView.getTop() <= 0;
        }
        setMediaHeaderVisible(z);
        float f = i2;
        if (this.extraHeight == f || this.transitionAnimationInProress) {
            return;
        }
        this.extraHeight = f;
        this.topView.invalidate();
        if (this.playProfileAnimation != 0) {
            this.allowProfileAnimation = this.extraHeight != 0.0f;
        }
        needLayout(true);
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public void updateSelectedMediaTabText() {
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout == null || this.mediaCounterTextView == null) {
            return;
        }
        int closestTab = sharedMediaLayout.getClosestTab();
        int[] lastMediaCount = this.sharedMediaPreloader.getLastMediaCount();
        if (closestTab == 0) {
            if (lastMediaCount[7] <= 0 && lastMediaCount[6] <= 0) {
                if (lastMediaCount[0] <= 0) {
                    this.mediaCounterTextView.setText(LocaleController.getString(R.string.SharedMedia));
                } else {
                    this.mediaCounterTextView.setText(LocaleController.formatPluralString("Media", lastMediaCount[0], new Object[0]));
                }
            } else if (this.sharedMediaLayout.getPhotosVideosTypeFilter() == 1 || lastMediaCount[7] <= 0) {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Photos", lastMediaCount[6], new Object[0]));
            } else if (this.sharedMediaLayout.getPhotosVideosTypeFilter() == 2 || lastMediaCount[6] <= 0) {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Videos", lastMediaCount[7], new Object[0]));
            } else {
                this.mediaCounterTextView.setText(String.format("%s, %s", LocaleController.formatPluralString("Photos", lastMediaCount[6], new Object[0]), LocaleController.formatPluralString("Videos", lastMediaCount[7], new Object[0])));
            }
        } else if (closestTab == 1) {
            if (lastMediaCount[1] <= 0) {
                this.mediaCounterTextView.setText(LocaleController.getString(R.string.Files));
            } else {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Files", lastMediaCount[1], new Object[0]));
            }
        } else if (closestTab == 2) {
            if (lastMediaCount[2] <= 0) {
                this.mediaCounterTextView.setText(LocaleController.getString(R.string.Voice));
            } else {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Voice", lastMediaCount[2], new Object[0]));
            }
        } else if (closestTab == 3) {
            if (lastMediaCount[3] <= 0) {
                this.mediaCounterTextView.setText(LocaleController.getString(R.string.SharedLinks));
            } else {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Links", lastMediaCount[3], new Object[0]));
            }
        } else if (closestTab == 4) {
            if (lastMediaCount[4] <= 0) {
                this.mediaCounterTextView.setText(LocaleController.getString(R.string.Music));
            } else {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("MusicFiles", lastMediaCount[4], new Object[0]));
            }
        } else if (closestTab == 5) {
            if (lastMediaCount[5] <= 0) {
                this.mediaCounterTextView.setText(LocaleController.getString(R.string.AccDescrGIFs));
            } else {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("GIFs", lastMediaCount[5], new Object[0]));
            }
        } else if (closestTab == 6) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("CommonGroups", this.userInfo.common_chats_count, new Object[0]));
        } else if (closestTab == 7) {
            this.mediaCounterTextView.setText(this.onlineTextView[1].getText());
        } else if (closestTab == 8) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("ProfileStoriesCount", this.sharedMediaLayout.getStoriesCount(closestTab), new Object[0]));
        } else if (closestTab == 10) {
            MessagesController.ChannelRecommendations channelRecommendations = MessagesController.getInstance(this.currentAccount).getChannelRecommendations(this.chatId);
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("Channels", channelRecommendations == null ? 0 : channelRecommendations.chats.size() + channelRecommendations.more, new Object[0]));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needLayout(boolean z) {
        OverlaysView overlaysView;
        BackupImageView currentItemView;
        ValueAnimator valueAnimator;
        SimpleTextView[] simpleTextViewArr;
        TLRPC$ChatFull tLRPC$ChatFull;
        int i = 0;
        int currentActionBarHeight = (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight();
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null && !this.openAnimationInProgress) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) recyclerListView.getLayoutParams();
            if (layoutParams.topMargin != currentActionBarHeight) {
                layoutParams.topMargin = currentActionBarHeight;
                this.listView.setLayoutParams(layoutParams);
            }
        }
        if (this.avatarContainer != null) {
            float min = Math.min(1.0f, this.extraHeight / AndroidUtilities.dp(88.0f));
            this.listView.setTopGlowOffset((int) this.extraHeight);
            this.listView.setOverScrollMode((this.extraHeight <= ((float) AndroidUtilities.dp(88.0f)) || this.extraHeight >= ((float) (this.listView.getMeasuredWidth() - currentActionBarHeight))) ? 0 : 2);
            RLottieImageView rLottieImageView = this.writeButton;
            if (rLottieImageView != null) {
                rLottieImageView.setTranslationY(((((this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight()) + this.extraHeight) + this.searchTransitionOffset) - AndroidUtilities.dp(29.5f));
                boolean z2 = min > 0.2f && !this.searchMode && (this.imageUpdater == null || this.setAvatarRow == -1);
                if (z2 && this.chatId != 0) {
                    z2 = (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup || (tLRPC$ChatFull = this.chatInfo) == null || tLRPC$ChatFull.linked_chat_id == 0 || this.infoHeaderRow == -1) ? false : true;
                }
                if (!this.openAnimationInProgress) {
                    if (z2 != (this.writeButton.getTag() == null)) {
                        if (z2) {
                            this.writeButton.setTag(null);
                        } else {
                            this.writeButton.setTag(0);
                        }
                        AnimatorSet animatorSet = this.writeButtonAnimation;
                        if (animatorSet != null) {
                            this.writeButtonAnimation = null;
                            animatorSet.cancel();
                        }
                        if (z) {
                            AnimatorSet animatorSet2 = new AnimatorSet();
                            this.writeButtonAnimation = animatorSet2;
                            if (z2) {
                                animatorSet2.setInterpolator(new DecelerateInterpolator());
                                this.writeButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, 1.0f));
                            } else {
                                animatorSet2.setInterpolator(new AccelerateInterpolator());
                                this.writeButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_X, 0.2f), ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, 0.2f), ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, 0.0f));
                            }
                            this.writeButtonAnimation.setDuration(150L);
                            this.writeButtonAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.43
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    if (ProfileActivity.this.writeButtonAnimation == null || !ProfileActivity.this.writeButtonAnimation.equals(animator)) {
                                        return;
                                    }
                                    ProfileActivity.this.writeButtonAnimation = null;
                                }
                            });
                            this.writeButtonAnimation.start();
                        } else {
                            this.writeButton.setScaleX(z2 ? 1.0f : 0.2f);
                            this.writeButton.setScaleY(z2 ? 1.0f : 0.2f);
                            this.writeButton.setAlpha(z2 ? 1.0f : 0.0f);
                        }
                    }
                    if (this.qrItem != null) {
                        updateQrItemVisibility(z);
                        if (!z) {
                            float dp = AndroidUtilities.dp(48.0f) * this.qrItem.getAlpha();
                            this.qrItem.setTranslationX(dp);
                            PagerIndicatorView pagerIndicatorView = this.avatarsViewPagerIndicatorView;
                            if (pagerIndicatorView != null) {
                                pagerIndicatorView.setTranslationX(dp - AndroidUtilities.dp(48.0f));
                            }
                        }
                    }
                }
                ProfileStoriesView profileStoriesView = this.storyView;
                if (profileStoriesView != null) {
                    profileStoriesView.setExpandCoords(this.avatarContainer2.getMeasuredWidth() - AndroidUtilities.dp(40.0f), z2, (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight() + this.extraHeight + this.searchTransitionOffset);
                }
            }
            this.avatarX = (-AndroidUtilities.dpf2(47.0f)) * min;
            float f = AndroidUtilities.density;
            this.avatarY = (((this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ((ActionBar.getCurrentActionBarHeight() / 2.0f) * (min + 1.0f))) - (f * 21.0f)) + (f * 27.0f * min) + this.actionBar.getTranslationY();
            float f2 = this.openAnimationInProgress ? this.initialAnimationExtraHeight : this.extraHeight;
            if (f2 > AndroidUtilities.dp(88.0f) || this.isPulledDown) {
                float max = Math.max(0.0f, Math.min(1.0f, (f2 - AndroidUtilities.dp(88.0f)) / ((this.listView.getMeasuredWidth() - currentActionBarHeight) - AndroidUtilities.dp(88.0f))));
                this.expandProgress = max;
                this.avatarScale = AndroidUtilities.lerp(1.4285715f, 2.4285715f, Math.min(1.0f, max * 3.0f));
                ProfileStoriesView profileStoriesView2 = this.storyView;
                if (profileStoriesView2 != null) {
                    profileStoriesView2.invalidate();
                }
                float min2 = Math.min(AndroidUtilities.dpf2(2000.0f), Math.max(AndroidUtilities.dpf2(1100.0f), Math.abs(this.listViewVelocityY))) / AndroidUtilities.dpf2(1100.0f);
                if (this.allowPullingDown && (this.openingAvatar || this.expandProgress >= 0.33f)) {
                    if (!this.isPulledDown) {
                        if (this.otherItem != null) {
                            if (!getMessagesController().isChatNoForwards(this.currentChat)) {
                                this.otherItem.showSubItem(21);
                            } else {
                                this.otherItem.hideSubItem(21);
                            }
                            if (this.imageUpdater != null) {
                                this.otherItem.showSubItem(36);
                                this.otherItem.showSubItem(34);
                                this.otherItem.showSubItem(35);
                                this.otherItem.hideSubItem(33);
                                this.otherItem.hideSubItem(31);
                            }
                        }
                        ActionBarMenuItem actionBarMenuItem = this.searchItem;
                        if (actionBarMenuItem != null) {
                            actionBarMenuItem.setEnabled(false);
                        }
                        this.isPulledDown = true;
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
                        this.overlaysView.setOverlaysVisible(true, min2);
                        this.avatarsViewPagerIndicatorView.refreshVisibility(min2);
                        this.avatarsViewPager.setCreateThumbFromParent(true);
                        this.avatarsViewPager.getAdapter().notifyDataSetChanged();
                        this.expandAnimator.cancel();
                        float lerp = AndroidUtilities.lerp(this.expandAnimatorValues, this.currentExpanAnimatorFracture);
                        float[] fArr = this.expandAnimatorValues;
                        fArr[0] = lerp;
                        fArr[1] = 1.0f;
                        ProfileStoriesView profileStoriesView3 = this.storyView;
                        if (profileStoriesView3 != null && !profileStoriesView3.isEmpty()) {
                            this.expandAnimator.setInterpolator(new FastOutSlowInInterpolator());
                            this.expandAnimator.setDuration((((1.0f - lerp) * 1.3f) * 250.0f) / min2);
                        } else {
                            this.expandAnimator.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                            this.expandAnimator.setDuration(((1.0f - lerp) * 250.0f) / min2);
                        }
                        this.expandAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.44
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationStart(Animator animator) {
                                ProfileActivity.this.setForegroundImage(false);
                                ProfileActivity.this.avatarsViewPager.setAnimatedFileMaybe(ProfileActivity.this.avatarImage.getImageReceiver().getAnimation());
                                ProfileActivity.this.avatarsViewPager.resetCurrentItem();
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                ProfileActivity.this.expandAnimator.removeListener(this);
                                ProfileActivity.this.topView.setBackgroundColor(-16777216);
                                ProfileActivity.this.avatarContainer.setVisibility(8);
                                ProfileActivity.this.avatarsViewPager.setVisibility(0);
                            }
                        });
                        this.expandAnimator.start();
                    }
                    ViewGroup.LayoutParams layoutParams2 = this.avatarsViewPager.getLayoutParams();
                    layoutParams2.width = this.listView.getMeasuredWidth();
                    float f3 = f2 + currentActionBarHeight;
                    layoutParams2.height = (int) f3;
                    this.avatarsViewPager.requestLayout();
                    if (!this.expandAnimator.isRunning()) {
                        float dp2 = (this.openAnimationInProgress && this.playProfileAnimation == 2) ? (-(1.0f - this.avatarAnimationProgress)) * AndroidUtilities.dp(50.0f) : 0.0f;
                        this.onlineX = AndroidUtilities.dpf2(16.0f) - this.onlineTextView[1].getLeft();
                        this.nameTextView[1].setTranslationX(AndroidUtilities.dpf2(18.0f) - this.nameTextView[1].getLeft());
                        this.nameTextView[1].setTranslationY(((f3 - AndroidUtilities.dpf2(38.0f)) - this.nameTextView[1].getBottom()) + dp2);
                        this.onlineTextView[1].setTranslationX(this.onlineX + this.customPhotoOffset);
                        this.onlineTextView[1].setTranslationY(((f3 - AndroidUtilities.dpf2(18.0f)) - this.onlineTextView[1].getBottom()) + dp2);
                        this.mediaCounterTextView.setTranslationX(this.onlineTextView[1].getTranslationX());
                        this.mediaCounterTextView.setTranslationY(this.onlineTextView[1].getTranslationY());
                    }
                } else {
                    if (this.isPulledDown) {
                        this.isPulledDown = false;
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
                        ActionBarMenuItem actionBarMenuItem2 = this.otherItem;
                        if (actionBarMenuItem2 != null) {
                            actionBarMenuItem2.hideSubItem(21);
                            if (this.imageUpdater != null) {
                                this.otherItem.hideSubItem(33);
                                this.otherItem.hideSubItem(34);
                                this.otherItem.hideSubItem(35);
                                this.otherItem.showSubItem(36);
                                this.otherItem.showSubItem(31);
                            }
                        }
                        ActionBarMenuItem actionBarMenuItem3 = this.searchItem;
                        if (actionBarMenuItem3 != null) {
                            actionBarMenuItem3.setEnabled(!this.scrolling);
                        }
                        this.overlaysView.setOverlaysVisible(false, min2);
                        this.avatarsViewPagerIndicatorView.refreshVisibility(min2);
                        this.expandAnimator.cancel();
                        this.avatarImage.getImageReceiver().setAllowStartAnimation(true);
                        this.avatarImage.getImageReceiver().startAnimation();
                        float lerp2 = AndroidUtilities.lerp(this.expandAnimatorValues, this.currentExpanAnimatorFracture);
                        float[] fArr2 = this.expandAnimatorValues;
                        fArr2[0] = lerp2;
                        fArr2[1] = 0.0f;
                        this.expandAnimator.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                        if (!this.isInLandscapeMode) {
                            this.expandAnimator.setDuration((lerp2 * 250.0f) / min2);
                        } else {
                            this.expandAnimator.setDuration(0L);
                        }
                        this.topView.setBackgroundColor(getThemedColor(Theme.key_avatar_backgroundActionBarBlue));
                        if (!this.doNotSetForeground && (currentItemView = this.avatarsViewPager.getCurrentItemView()) != null) {
                            if (currentItemView.getImageReceiver().getDrawable() instanceof VectorAvatarThumbDrawable) {
                                this.avatarImage.drawForeground(false);
                            } else {
                                this.avatarImage.drawForeground(true);
                                this.avatarImage.setForegroundImageDrawable(currentItemView.getImageReceiver().getDrawableSafe());
                            }
                        }
                        this.avatarImage.setForegroundAlpha(1.0f);
                        this.avatarContainer.setVisibility(0);
                        this.avatarsViewPager.setVisibility(8);
                        this.expandAnimator.start();
                    }
                    this.avatarContainer.setScaleX(this.avatarScale);
                    this.avatarContainer.setScaleY(this.avatarScale);
                    ValueAnimator valueAnimator2 = this.expandAnimator;
                    if (valueAnimator2 == null || !valueAnimator2.isRunning()) {
                        refreshNameAndOnlineXY();
                        this.nameTextView[1].setTranslationX(this.nameX);
                        this.nameTextView[1].setTranslationY(this.nameY);
                        this.onlineTextView[1].setTranslationX(this.onlineX + this.customPhotoOffset);
                        this.onlineTextView[1].setTranslationY(this.onlineY);
                        this.mediaCounterTextView.setTranslationX(this.onlineX);
                        this.mediaCounterTextView.setTranslationY(this.onlineY);
                    }
                }
            }
            if (this.openAnimationInProgress && this.playProfileAnimation == 2) {
                float currentActionBarHeight2 = (((this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + (ActionBar.getCurrentActionBarHeight() / 2.0f)) - (AndroidUtilities.density * 21.0f)) + this.actionBar.getTranslationY();
                this.nameTextView[0].setTranslationX(0.0f);
                double d = currentActionBarHeight2;
                this.nameTextView[0].setTranslationY(((float) Math.floor(d)) + AndroidUtilities.dp(1.3f));
                this.onlineTextView[0].setTranslationX(0.0f);
                this.onlineTextView[0].setTranslationY(((float) Math.floor(d)) + AndroidUtilities.dp(24.0f));
                this.nameTextView[0].setScaleX(1.0f);
                this.nameTextView[0].setScaleY(1.0f);
                this.nameTextView[1].setPivotY(simpleTextViewArr[1].getMeasuredHeight());
                this.nameTextView[1].setScaleX(1.67f);
                this.nameTextView[1].setScaleY(1.67f);
                this.avatarScale = AndroidUtilities.lerp(1.0f, 2.4285715f, this.avatarAnimationProgress);
                ProfileStoriesView profileStoriesView4 = this.storyView;
                if (profileStoriesView4 != null) {
                    profileStoriesView4.setExpandProgress(1.0f);
                }
                this.avatarImage.setRoundRadius((int) AndroidUtilities.lerp(getSmallAvatarRoundRadius(), 0.0f, this.avatarAnimationProgress));
                this.avatarContainer.setTranslationX(AndroidUtilities.lerp(0.0f, 0.0f, this.avatarAnimationProgress));
                this.avatarContainer.setTranslationY(AndroidUtilities.lerp((float) Math.ceil(d), 0.0f, this.avatarAnimationProgress));
                float measuredWidth = (this.avatarContainer.getMeasuredWidth() - AndroidUtilities.dp(42.0f)) * this.avatarScale;
                this.timeItem.setTranslationX(this.avatarContainer.getX() + AndroidUtilities.dp(16.0f) + measuredWidth);
                this.timeItem.setTranslationY(this.avatarContainer.getY() + AndroidUtilities.dp(15.0f) + measuredWidth);
                this.avatarContainer.setScaleX(this.avatarScale);
                this.avatarContainer.setScaleY(this.avatarScale);
                this.overlaysView.setAlphaValue(this.avatarAnimationProgress, false);
                this.actionBar.setItemsColor(ColorUtils.blendARGB(this.peerColor != null ? -1 : getThemedColor(Theme.key_actionBarDefaultIcon), -1, this.avatarAnimationProgress), false);
                ScamDrawable scamDrawable = this.scamDrawable;
                if (scamDrawable != null) {
                    scamDrawable.setColor(ColorUtils.blendARGB(getThemedColor(Theme.key_avatar_subtitleInProfileBlue), Color.argb(179, 255, 255, 255), this.avatarAnimationProgress));
                }
                Drawable drawable = this.lockIconDrawable;
                if (drawable != null) {
                    drawable.setColorFilter(ColorUtils.blendARGB(getThemedColor(Theme.key_chat_lockIcon), -1, this.avatarAnimationProgress), PorterDuff.Mode.MULTIPLY);
                }
                CrossfadeDrawable[] crossfadeDrawableArr = this.verifiedCrossfadeDrawable;
                if (crossfadeDrawableArr[1] != null) {
                    crossfadeDrawableArr[1].setProgress(this.avatarAnimationProgress);
                    this.nameTextView[1].invalidate();
                }
                CrossfadeDrawable[] crossfadeDrawableArr2 = this.premiumCrossfadeDrawable;
                if (crossfadeDrawableArr2[1] != null) {
                    crossfadeDrawableArr2[1].setProgress(this.avatarAnimationProgress);
                    this.nameTextView[1].invalidate();
                }
                updateEmojiStatusDrawableColor(this.avatarAnimationProgress);
                FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.avatarContainer.getLayoutParams();
                int lerp3 = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), (this.extraHeight + currentActionBarHeight) / this.avatarScale, this.avatarAnimationProgress);
                layoutParams3.height = lerp3;
                layoutParams3.width = lerp3;
                layoutParams3.leftMargin = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(64.0f), 0.0f, this.avatarAnimationProgress);
                this.avatarContainer.requestLayout();
            } else if (this.extraHeight <= AndroidUtilities.dp(88.0f)) {
                this.avatarScale = ((18.0f * min) + 42.0f) / 42.0f;
                ProfileStoriesView profileStoriesView5 = this.storyView;
                if (profileStoriesView5 != null) {
                    profileStoriesView5.invalidate();
                }
                float f4 = (0.12f * min) + 1.0f;
                ValueAnimator valueAnimator3 = this.expandAnimator;
                if (valueAnimator3 == null || !valueAnimator3.isRunning()) {
                    this.avatarContainer.setScaleX(this.avatarScale);
                    this.avatarContainer.setScaleY(this.avatarScale);
                    this.avatarContainer.setTranslationX(this.avatarX);
                    this.avatarContainer.setTranslationY((float) Math.ceil(this.avatarY));
                    float dp3 = (AndroidUtilities.dp(42.0f) * this.avatarScale) - AndroidUtilities.dp(42.0f);
                    this.timeItem.setTranslationX(this.avatarContainer.getX() + AndroidUtilities.dp(16.0f) + dp3);
                    this.timeItem.setTranslationY(this.avatarContainer.getY() + AndroidUtilities.dp(15.0f) + dp3);
                }
                this.nameX = AndroidUtilities.density * (-21.0f) * min;
                this.nameY = ((float) Math.floor(this.avatarY)) + AndroidUtilities.dp(1.3f) + (AndroidUtilities.dp(7.0f) * min) + (this.titleAnimationsYDiff * (1.0f - this.avatarAnimationProgress));
                this.onlineX = AndroidUtilities.density * (-21.0f) * min;
                this.onlineY = ((float) Math.floor(this.avatarY)) + AndroidUtilities.dp(24.0f) + (((float) Math.floor(AndroidUtilities.density * 11.0f)) * min);
                while (true) {
                    SimpleTextView[] simpleTextViewArr2 = this.nameTextView;
                    if (i >= simpleTextViewArr2.length) {
                        break;
                    }
                    if (simpleTextViewArr2[i] != null) {
                        ValueAnimator valueAnimator4 = this.expandAnimator;
                        if (valueAnimator4 == null || !valueAnimator4.isRunning()) {
                            this.nameTextView[i].setTranslationX(this.nameX);
                            this.nameTextView[i].setTranslationY(this.nameY);
                            this.onlineTextView[i].setTranslationX(this.onlineX + this.customPhotoOffset);
                            this.onlineTextView[i].setTranslationY(this.onlineY);
                            if (i == 1) {
                                this.mediaCounterTextView.setTranslationX(this.onlineX);
                                this.mediaCounterTextView.setTranslationY(this.onlineY);
                            }
                        }
                        this.nameTextView[i].setScaleX(f4);
                        this.nameTextView[i].setScaleY(f4);
                    }
                    i++;
                }
            }
            if (!this.openAnimationInProgress && ((valueAnimator = this.expandAnimator) == null || !valueAnimator.isRunning())) {
                needLayoutText(min);
            }
        }
        if (this.isPulledDown || ((overlaysView = this.overlaysView) != null && overlaysView.animator != null && this.overlaysView.animator.isRunning())) {
            ViewGroup.LayoutParams layoutParams4 = this.overlaysView.getLayoutParams();
            layoutParams4.width = this.listView.getMeasuredWidth();
            layoutParams4.height = (int) (this.extraHeight + currentActionBarHeight);
            this.overlaysView.requestLayout();
        }
        updateEmojiStatusEffectPosition();
    }

    public void updateQrItemVisibility(boolean z) {
        if (this.qrItem == null) {
            return;
        }
        boolean z2 = isQrNeedVisible() && Math.min(1.0f, this.extraHeight / ((float) AndroidUtilities.dp(88.0f))) > 0.5f && this.searchTransitionProgress > 0.5f;
        if (z) {
            if (z2 != this.isQrItemVisible) {
                this.isQrItemVisible = z2;
                AnimatorSet animatorSet = this.qrItemAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.qrItemAnimation = null;
                }
                this.qrItem.setClickable(this.isQrItemVisible);
                this.qrItemAnimation = new AnimatorSet();
                if (this.qrItem.getVisibility() != 8 || z2) {
                    this.qrItem.setVisibility(0);
                }
                if (z2) {
                    this.qrItemAnimation.setInterpolator(new DecelerateInterpolator());
                    this.qrItemAnimation.playTogether(ObjectAnimator.ofFloat(this.qrItem, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.qrItem, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.avatarsViewPagerIndicatorView, View.TRANSLATION_X, -AndroidUtilities.dp(48.0f)));
                } else {
                    this.qrItemAnimation.setInterpolator(new AccelerateInterpolator());
                    this.qrItemAnimation.playTogether(ObjectAnimator.ofFloat(this.qrItem, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.qrItem, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.avatarsViewPagerIndicatorView, View.TRANSLATION_X, 0.0f));
                }
                this.qrItemAnimation.setDuration(150L);
                this.qrItemAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.45
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ProfileActivity.this.qrItemAnimation = null;
                    }
                });
                this.qrItemAnimation.start();
                return;
            }
            return;
        }
        AnimatorSet animatorSet2 = this.qrItemAnimation;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
            this.qrItemAnimation = null;
        }
        this.isQrItemVisible = z2;
        this.qrItem.setClickable(z2);
        this.qrItem.setAlpha(z2 ? 1.0f : 0.0f);
        this.qrItem.setVisibility(z2 ? 0 : 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setForegroundImage(boolean z) {
        Drawable drawable = this.avatarImage.getImageReceiver().getDrawable();
        String str = null;
        if (drawable instanceof VectorAvatarThumbDrawable) {
            this.avatarImage.setForegroundImage(null, null, drawable);
        } else if (drawable instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
            this.avatarImage.setForegroundImage(null, null, animatedFileDrawable);
            if (z) {
                animatedFileDrawable.addSecondParentView(this.avatarImage);
            }
        } else {
            ImageLocation imageLocation = this.avatarsViewPager.getImageLocation(0);
            if (imageLocation != null && imageLocation.imageType == 2) {
                str = "avatar";
            }
            this.avatarImage.setForegroundImage(imageLocation, str, drawable);
        }
    }

    private void refreshNameAndOnlineXY() {
        this.nameX = AndroidUtilities.dp(-21.0f) + (this.avatarContainer.getMeasuredWidth() * (this.avatarScale - 1.4285715f));
        this.nameY = ((float) Math.floor(this.avatarY)) + AndroidUtilities.dp(1.3f) + AndroidUtilities.dp(7.0f) + ((this.avatarContainer.getMeasuredHeight() * (this.avatarScale - 1.4285715f)) / 2.0f);
        this.onlineX = AndroidUtilities.dp(-21.0f) + (this.avatarContainer.getMeasuredWidth() * (this.avatarScale - 1.4285715f));
        this.onlineY = ((float) Math.floor(this.avatarY)) + AndroidUtilities.dp(24.0f) + ((float) Math.floor(AndroidUtilities.density * 11.0f)) + ((this.avatarContainer.getMeasuredHeight() * (this.avatarScale - 1.4285715f)) / 2.0f);
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public RecyclerListView getListView() {
        return this.listView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needLayoutText(float f) {
        float scaleX = this.nameTextView[1].getScaleX();
        float f2 = this.extraHeight > ((float) AndroidUtilities.dp(88.0f)) ? 1.67f : 1.12f;
        if (this.extraHeight <= AndroidUtilities.dp(88.0f) || scaleX == f2) {
            int dp = AndroidUtilities.isTablet() ? AndroidUtilities.dp(490.0f) : AndroidUtilities.displaySize.x;
            this.avatarsViewPagerIndicatorView.getSecondaryMenuItem();
            int i = this.editItemVisible ? 48 : 0;
            if (this.callItemVisible) {
                i += 48;
            }
            if (this.videoCallItemVisible) {
                i += 48;
            }
            if (this.searchItem != null) {
                i += 48;
            }
            int dp2 = AndroidUtilities.dp((i * (1.0f - this.mediaHeaderAnimationProgress)) + 40.0f + 126.0f);
            int i2 = dp - dp2;
            float f3 = dp;
            int max = (int) ((f3 - (dp2 * Math.max(0.0f, 1.0f - (f != 1.0f ? (0.15f * f) / (1.0f - f) : 1.0f)))) - this.nameTextView[1].getTranslationX());
            float measureText = (this.nameTextView[1].getPaint().measureText(this.nameTextView[1].getText().toString()) * scaleX) + this.nameTextView[1].getSideDrawablesSize();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.nameTextView[1].getLayoutParams();
            int i3 = layoutParams.width;
            float f4 = max;
            if (f4 < measureText) {
                layoutParams.width = Math.max(i2, (int) Math.ceil((max - AndroidUtilities.dp(24.0f)) / (((f2 - scaleX) * 7.0f) + scaleX)));
            } else {
                layoutParams.width = (int) Math.ceil(measureText);
            }
            int min = (int) Math.min(((f3 - this.nameTextView[1].getX()) / scaleX) - AndroidUtilities.dp(8.0f), layoutParams.width);
            layoutParams.width = min;
            if (min != i3) {
                this.nameTextView[1].requestLayout();
            }
            float measureText2 = this.onlineTextView[1].getPaint().measureText(this.onlineTextView[1].getText().toString());
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.onlineTextView[1].getLayoutParams();
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mediaCounterTextView.getLayoutParams();
            int i4 = layoutParams2.width;
            int ceil = (int) Math.ceil(this.onlineTextView[1].getTranslationX() + AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(40.0f) * (1.0f - f)));
            layoutParams2.rightMargin = ceil;
            layoutParams3.rightMargin = ceil;
            if (f4 < measureText2) {
                int ceil2 = (int) Math.ceil(max);
                layoutParams2.width = ceil2;
                layoutParams3.width = ceil2;
            } else {
                layoutParams2.width = -2;
                layoutParams3.width = -2;
            }
            if (i4 != layoutParams2.width) {
                this.onlineTextView[2].getLayoutParams().width = layoutParams2.width;
                this.onlineTextView[2].requestLayout();
                this.onlineTextView[3].getLayoutParams().width = layoutParams2.width;
                this.onlineTextView[3].requestLayout();
                this.onlineTextView[1].requestLayout();
                this.mediaCounterTextView.requestLayout();
            }
        }
    }

    private void fixLayout() {
        View view = this.fragmentView;
        if (view == null) {
            return;
        }
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.ProfileActivity.46
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                if (((BaseFragment) ProfileActivity.this).fragmentView != null) {
                    ProfileActivity.this.checkListViewScroll();
                    ProfileActivity.this.needLayout(true);
                    ((BaseFragment) ProfileActivity.this).fragmentView.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        View findViewByPosition;
        super.onConfigurationChanged(configuration);
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            sharedMediaLayout.onConfigurationChanged(configuration);
        }
        invalidateIsInLandscapeMode();
        if (this.isInLandscapeMode && this.isPulledDown && (findViewByPosition = this.layoutManager.findViewByPosition(0)) != null) {
            this.listView.scrollBy(0, findViewByPosition.getTop() - AndroidUtilities.dp(88.0f));
        }
        fixLayout();
    }

    private void invalidateIsInLandscapeMode() {
        Point point = new Point();
        getParentActivity().getWindowManager().getDefaultDisplay().getSize(point);
        this.isInLandscapeMode = point.x > point.y;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, final Object... objArr) {
        AvatarImageView avatarImageView;
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$ChatFull tLRPC$ChatFull2;
        TLRPC$TL_inputGroupCall tLRPC$TL_inputGroupCall;
        TLRPC$Chat tLRPC$Chat;
        RecyclerListView recyclerListView;
        RecyclerListView recyclerListView2;
        RecyclerListView.Holder holder;
        int i3 = 0;
        if (i == NotificationCenter.updateInterfaces) {
            int intValue = ((Integer) objArr[0]).intValue();
            boolean z = ((MessagesController.UPDATE_MASK_AVATAR & intValue) == 0 && (MessagesController.UPDATE_MASK_NAME & intValue) == 0 && (MessagesController.UPDATE_MASK_STATUS & intValue) == 0 && (MessagesController.UPDATE_MASK_EMOJI_STATUS & intValue) == 0) ? false : true;
            if (this.userId != 0) {
                if (z) {
                    updateProfileData(true);
                }
                if ((intValue & MessagesController.UPDATE_MASK_PHONE) == 0 || (recyclerListView2 = this.listView) == null || (holder = (RecyclerListView.Holder) recyclerListView2.findViewHolderForPosition(this.phoneRow)) == null) {
                    return;
                }
                this.listAdapter.onBindViewHolder(holder, this.phoneRow);
            } else if (this.chatId != 0) {
                if ((MessagesController.UPDATE_MASK_CHAT & intValue) != 0 || (MessagesController.UPDATE_MASK_CHAT_AVATAR & intValue) != 0 || (MessagesController.UPDATE_MASK_CHAT_NAME & intValue) != 0 || (MessagesController.UPDATE_MASK_CHAT_MEMBERS & intValue) != 0 || (MessagesController.UPDATE_MASK_STATUS & intValue) != 0) {
                    if ((MessagesController.UPDATE_MASK_CHAT & intValue) != 0) {
                        updateListAnimated(true);
                    } else {
                        updateOnlineCount(true);
                    }
                    updateProfileData(true);
                }
                if (!z || (recyclerListView = this.listView) == null) {
                    return;
                }
                int childCount = recyclerListView.getChildCount();
                while (i3 < childCount) {
                    View childAt = this.listView.getChildAt(i3);
                    if (childAt instanceof UserCell) {
                        ((UserCell) childAt).update(intValue);
                    }
                    i3++;
                }
            }
        } else if (i == NotificationCenter.chatOnlineCountDidLoad) {
            Long l = (Long) objArr[0];
            if (this.chatInfo == null || (tLRPC$Chat = this.currentChat) == null || tLRPC$Chat.id != l.longValue()) {
                return;
            }
            this.chatInfo.online_count = ((Integer) objArr[1]).intValue();
            updateOnlineCount(true);
            updateProfileData(false);
        } else if (i == NotificationCenter.contactsDidLoad || i == NotificationCenter.channelRightsUpdated) {
            createActionBarMenu(true);
        } else if (i == NotificationCenter.encryptedChatCreated) {
            if (this.creatingChat) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda47
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.this.lambda$didReceivedNotification$50(objArr);
                    }
                });
            }
        } else if (i == NotificationCenter.encryptedChatUpdated) {
            TLRPC$EncryptedChat tLRPC$EncryptedChat = (TLRPC$EncryptedChat) objArr[0];
            TLRPC$EncryptedChat tLRPC$EncryptedChat2 = this.currentEncryptedChat;
            if (tLRPC$EncryptedChat2 == null || tLRPC$EncryptedChat.id != tLRPC$EncryptedChat2.id) {
                return;
            }
            this.currentEncryptedChat = tLRPC$EncryptedChat;
            updateListAnimated(false);
            FlagSecureReason flagSecureReason = this.flagSecure;
            if (flagSecureReason != null) {
                flagSecureReason.invalidate();
            }
        } else if (i == NotificationCenter.blockedUsersDidLoad) {
            boolean z2 = this.userBlocked;
            boolean z3 = getMessagesController().blockePeers.indexOfKey(this.userId) >= 0;
            this.userBlocked = z3;
            if (z2 != z3) {
                createActionBarMenu(true);
                updateListAnimated(false);
            }
        } else if (i == NotificationCenter.groupCallUpdated) {
            Long l2 = (Long) objArr[0];
            if (this.currentChat != null) {
                long longValue = l2.longValue();
                TLRPC$Chat tLRPC$Chat2 = this.currentChat;
                if (longValue == tLRPC$Chat2.id && ChatObject.canManageCalls(tLRPC$Chat2)) {
                    TLRPC$ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(l2.longValue());
                    if (chatFull != null) {
                        TLRPC$ChatFull tLRPC$ChatFull3 = this.chatInfo;
                        if (tLRPC$ChatFull3 != null) {
                            chatFull.participants = tLRPC$ChatFull3.participants;
                        }
                        this.chatInfo = chatFull;
                    }
                    SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
                    if (sharedMediaLayout != null) {
                        sharedMediaLayout.setChatInfo(this.chatInfo);
                    }
                    TLRPC$ChatFull tLRPC$ChatFull4 = this.chatInfo;
                    if (tLRPC$ChatFull4 != null && (((tLRPC$TL_inputGroupCall = tLRPC$ChatFull4.call) == null && !this.hasVoiceChatItem) || (tLRPC$TL_inputGroupCall != null && this.hasVoiceChatItem))) {
                        createActionBarMenu(false);
                    }
                    ProfileStoriesView profileStoriesView = this.storyView;
                    if (profileStoriesView != null && (tLRPC$ChatFull2 = this.chatInfo) != null) {
                        profileStoriesView.setStories(tLRPC$ChatFull2.stories);
                    }
                    AvatarImageView avatarImageView2 = this.avatarImage;
                    if (avatarImageView2 != null) {
                        avatarImageView2.setHasStories(needInsetForStories());
                    }
                }
            }
        } else if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC$ChatFull tLRPC$ChatFull5 = (TLRPC$ChatFull) objArr[0];
            if (tLRPC$ChatFull5.id == this.chatId) {
                boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                TLRPC$ChatFull tLRPC$ChatFull6 = this.chatInfo;
                if ((tLRPC$ChatFull6 instanceof TLRPC$TL_channelFull) && tLRPC$ChatFull5.participants == null) {
                    tLRPC$ChatFull5.participants = tLRPC$ChatFull6.participants;
                }
                if (tLRPC$ChatFull6 == null && (tLRPC$ChatFull5 instanceof TLRPC$TL_channelFull)) {
                    i3 = 1;
                }
                this.chatInfo = tLRPC$ChatFull5;
                if (this.mergeDialogId == 0) {
                    long j = tLRPC$ChatFull5.migrated_from_chat_id;
                    if (j != 0) {
                        this.mergeDialogId = -j;
                        getMediaDataController().getMediaCount(this.mergeDialogId, this.topicId, 0, this.classGuid, true);
                    }
                }
                fetchUsersFromChannelInfo();
                ProfileGalleryView profileGalleryView = this.avatarsViewPager;
                if (profileGalleryView != null && !this.isTopic) {
                    profileGalleryView.setChatInfo(this.chatInfo);
                }
                updateListAnimated(true);
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
                if (chat != null) {
                    this.currentChat = chat;
                    createActionBarMenu(true);
                }
                FlagSecureReason flagSecureReason2 = this.flagSecure;
                if (flagSecureReason2 != null) {
                    flagSecureReason2.invalidate();
                }
                if (this.currentChat.megagroup && (i3 != 0 || !booleanValue)) {
                    getChannelParticipants(true);
                }
                updateAutoDeleteItem();
                updateTtlIcon();
                ProfileStoriesView profileStoriesView2 = this.storyView;
                if (profileStoriesView2 != null && (tLRPC$ChatFull = this.chatInfo) != null) {
                    profileStoriesView2.setStories(tLRPC$ChatFull.stories);
                }
                AvatarImageView avatarImageView3 = this.avatarImage;
                if (avatarImageView3 != null) {
                    avatarImageView3.setHasStories(needInsetForStories());
                }
                SharedMediaLayout sharedMediaLayout2 = this.sharedMediaLayout;
                if (sharedMediaLayout2 != null) {
                    sharedMediaLayout2.setChatInfo(this.chatInfo);
                }
            }
        } else if (i == NotificationCenter.closeChats) {
            removeSelfFromStack(true);
        } else if (i == NotificationCenter.botInfoDidLoad) {
            TLRPC$BotInfo tLRPC$BotInfo = (TLRPC$BotInfo) objArr[0];
            if (tLRPC$BotInfo.user_id == this.userId) {
                this.botInfo = tLRPC$BotInfo;
                updateListAnimated(false);
            }
        } else if (i == NotificationCenter.userInfoDidLoad) {
            if (((Long) objArr[0]).longValue() == this.userId) {
                TLRPC$UserFull tLRPC$UserFull = (TLRPC$UserFull) objArr[1];
                this.userInfo = tLRPC$UserFull;
                ProfileStoriesView profileStoriesView3 = this.storyView;
                if (profileStoriesView3 != null) {
                    profileStoriesView3.setStories(tLRPC$UserFull.stories);
                }
                AvatarImageView avatarImageView4 = this.avatarImage;
                if (avatarImageView4 != null) {
                    avatarImageView4.setHasStories(needInsetForStories());
                }
                SharedMediaLayout sharedMediaLayout3 = this.sharedMediaLayout;
                if (sharedMediaLayout3 != null) {
                    sharedMediaLayout3.setUserInfo(this.userInfo);
                }
                if (this.imageUpdater != null) {
                    if (this.listAdapter != null && !TextUtils.equals(this.userInfo.about, this.currentBio)) {
                        this.listAdapter.notifyItemChanged(this.bioRow);
                    }
                } else {
                    if (!this.openAnimationInProgress && !this.callItemVisible) {
                        createActionBarMenu(true);
                    } else {
                        this.recreateMenuAfterAnimation = true;
                    }
                    updateListAnimated(false);
                    SharedMediaLayout sharedMediaLayout4 = this.sharedMediaLayout;
                    if (sharedMediaLayout4 != null) {
                        sharedMediaLayout4.setCommonGroupsCount(this.userInfo.common_chats_count);
                        updateSelectedMediaTabText();
                        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
                        if (sharedMediaPreloader == null || sharedMediaPreloader.isMediaWasLoaded()) {
                            resumeDelayedFragmentAnimation();
                            needLayout(true);
                        }
                    }
                }
                updateAutoDeleteItem();
                updateTtlIcon();
            }
        } else if (i == NotificationCenter.privacyRulesUpdated) {
            if (this.qrItem != null) {
                updateQrItemVisibility(true);
            }
        } else if (i == NotificationCenter.didReceiveNewMessages) {
            if (((Boolean) objArr[2]).booleanValue()) {
                return;
            }
            long dialogId = getDialogId();
            if (dialogId == ((Long) objArr[0]).longValue()) {
                DialogObject.isEncryptedDialog(dialogId);
                ArrayList arrayList = (ArrayList) objArr[1];
                while (i3 < arrayList.size()) {
                    MessageObject messageObject = (MessageObject) arrayList.get(i3);
                    if (this.currentEncryptedChat != null) {
                        TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                            TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                            if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL) {
                                TLRPC$TL_decryptedMessageActionSetMessageTTL tLRPC$TL_decryptedMessageActionSetMessageTTL = (TLRPC$TL_decryptedMessageActionSetMessageTTL) tLRPC$DecryptedMessageAction;
                                ListAdapter listAdapter = this.listAdapter;
                                if (listAdapter != null) {
                                    listAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    i3++;
                }
            }
        } else if (i == NotificationCenter.emojiLoaded) {
            RecyclerListView recyclerListView3 = this.listView;
            if (recyclerListView3 != null) {
                recyclerListView3.invalidateViews();
            }
        } else if (i == NotificationCenter.reloadInterface) {
            updateListAnimated(false);
        } else if (i == NotificationCenter.newSuggestionsAvailable) {
            int i4 = this.passwordSuggestionRow;
            int i5 = this.phoneSuggestionRow;
            updateRowsIds();
            if (i4 == this.passwordSuggestionRow && i5 == this.phoneSuggestionRow) {
                return;
            }
            this.listAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.topicsDidLoaded) {
            if (this.isTopic) {
                updateProfileData(false);
            }
        } else if (i == NotificationCenter.updateSearchSettings) {
            SearchAdapter searchAdapter = this.searchAdapter;
            if (searchAdapter != null) {
                searchAdapter.searchArray = searchAdapter.onCreateSearchArray();
                this.searchAdapter.recentSearches.clear();
                this.searchAdapter.updateSearchArray();
                SearchAdapter searchAdapter2 = this.searchAdapter;
                searchAdapter2.search(searchAdapter2.lastSearchString);
            }
        } else if (i == NotificationCenter.reloadDialogPhotos) {
            updateProfileData(false);
        } else if ((i == NotificationCenter.storiesUpdated || i == NotificationCenter.storiesReadUpdated) && (avatarImageView = this.avatarImage) != null) {
            avatarImageView.setHasStories(needInsetForStories());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$50(Object[] objArr) {
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.closeChats;
        notificationCenter.removeObserver(this, i);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putInt("enc_id", ((TLRPC$EncryptedChat) objArr[0]).id);
        presentFragment(new ChatActivity(bundle), true);
    }

    private void updateAutoDeleteItem() {
        if (this.autoDeleteItem == null || this.autoDeletePopupWrapper == null) {
            return;
        }
        int i = 0;
        TLRPC$UserFull tLRPC$UserFull = this.userInfo;
        if (tLRPC$UserFull != null || this.chatInfo != null) {
            i = tLRPC$UserFull != null ? tLRPC$UserFull.ttl_period : this.chatInfo.ttl_period;
        }
        this.autoDeleteItemDrawable.setTime(i);
        this.autoDeletePopupWrapper.lambda$updateItems$7(i);
    }

    private void updateTimeItem() {
        TimerDrawable timerDrawable = this.timerDrawable;
        if (timerDrawable == null) {
            return;
        }
        TLRPC$EncryptedChat tLRPC$EncryptedChat = this.currentEncryptedChat;
        if (tLRPC$EncryptedChat != null) {
            timerDrawable.setTime(tLRPC$EncryptedChat.ttl);
            this.timeItem.setTag(1);
            this.timeItem.setVisibility(0);
            return;
        }
        TLRPC$UserFull tLRPC$UserFull = this.userInfo;
        if (tLRPC$UserFull != null) {
            timerDrawable.setTime(tLRPC$UserFull.ttl_period);
            if (this.needTimerImage && this.userInfo.ttl_period != 0) {
                this.timeItem.setTag(1);
                this.timeItem.setVisibility(0);
                return;
            }
            this.timeItem.setTag(null);
            this.timeItem.setVisibility(8);
            return;
        }
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if (tLRPC$ChatFull != null) {
            timerDrawable.setTime(tLRPC$ChatFull.ttl_period);
            if (this.needTimerImage && this.chatInfo.ttl_period != 0) {
                this.timeItem.setTag(1);
                this.timeItem.setVisibility(0);
                return;
            }
            this.timeItem.setTag(null);
            this.timeItem.setVisibility(8);
            return;
        }
        this.timeItem.setTag(null);
        this.timeItem.setVisibility(8);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean needDelayOpenAnimation() {
        return this.playProfileAnimation == 0;
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.SharedMediaPreloaderDelegate
    public void mediaCountUpdated() {
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader;
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null && (sharedMediaPreloader = this.sharedMediaPreloader) != null) {
            sharedMediaLayout.setNewMediaCounts(sharedMediaPreloader.getLastMediaCount());
        }
        updateSharedMediaRows();
        updateSelectedMediaTabText();
        if (this.userInfo != null) {
            resumeDelayedFragmentAnimation();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        TLRPC$User user;
        View view;
        super.onResume();
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            sharedMediaLayout.onResume();
        }
        invalidateIsInLandscapeMode();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            this.firstLayout = true;
            listAdapter.notifyDataSetChanged();
        }
        if (!this.parentLayout.isInPreviewMode() && (view = this.blurredView) != null && view.getVisibility() == 0) {
            this.blurredView.setVisibility(8);
            this.blurredView.setBackground(null);
        }
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onResume();
            setParentActivityTitle(LocaleController.getString("Settings", R.string.Settings));
        }
        updateProfileData(true);
        fixLayout();
        SimpleTextView[] simpleTextViewArr = this.nameTextView;
        if (simpleTextViewArr[1] != null) {
            setParentActivityTitle(simpleTextViewArr[1].getText());
        }
        if (this.userId != 0 && (user = getMessagesController().getUser(Long.valueOf(this.userId))) != null && user.photo == null && this.extraHeight >= AndroidUtilities.dp(88.0f)) {
            this.expandAnimator.cancel();
            float[] fArr = this.expandAnimatorValues;
            fArr[0] = 1.0f;
            fArr[1] = 0.0f;
            setAvatarExpandProgress(1.0f);
            this.avatarsViewPager.setVisibility(8);
            this.extraHeight = AndroidUtilities.dp(88.0f);
            this.allowPullingDown = false;
            this.layoutManager.scrollToPositionWithOffset(0, AndroidUtilities.dp(88.0f) - this.listView.getPaddingTop());
        }
        FlagSecureReason flagSecureReason = this.flagSecure;
        if (flagSecureReason != null) {
            flagSecureReason.attach();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onPause();
        }
        FlagSecureReason flagSecureReason = this.flagSecure;
        if (flagSecureReason != null) {
            flagSecureReason.detach();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        SharedMediaLayout sharedMediaLayout;
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        if (profileGalleryView != null && profileGalleryView.getVisibility() == 0 && this.avatarsViewPager.getRealCount() > 1) {
            this.avatarsViewPager.getHitRect(this.rect);
            if (motionEvent != null && this.rect.contains((int) motionEvent.getX(), ((int) motionEvent.getY()) - this.actionBar.getMeasuredHeight())) {
                return false;
            }
        }
        if (this.sharedMediaRow == -1 || (sharedMediaLayout = this.sharedMediaLayout) == null) {
            return true;
        }
        if (sharedMediaLayout.isSwipeBackEnabled()) {
            this.sharedMediaLayout.getHitRect(this.rect);
            if (this.rect.contains((int) motionEvent.getX(), ((int) motionEvent.getY()) - this.actionBar.getMeasuredHeight())) {
                return this.sharedMediaLayout.isCurrentTabFirst();
            }
            return true;
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        if (this.sharedMediaLayout.isSwipeBackEnabled()) {
            return super.canBeginSlide();
        }
        return false;
    }

    public UndoView getUndoView() {
        return this.undoView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        SharedMediaLayout sharedMediaLayout;
        if (!closeStoryViewer() && this.actionBar.isEnabled()) {
            return this.sharedMediaRow == -1 || (sharedMediaLayout = this.sharedMediaLayout) == null || !sharedMediaLayout.closeActionMode();
        }
        return false;
    }

    public boolean isSettings() {
        return this.imageUpdater != null;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
        super.onBecomeFullyHidden();
    }

    public void setPlayProfileAnimation(int i) {
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (AndroidUtilities.isTablet()) {
            return;
        }
        this.needTimerImage = i != 0;
        if (globalMainSettings.getBoolean("view_animations", true)) {
            this.playProfileAnimation = i;
        } else if (i == 2) {
            this.expandPhoto = true;
        }
    }

    private void updateSharedMediaRows() {
        if (this.listAdapter == null) {
            return;
        }
        updateListAnimated(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationStart(boolean z, boolean z2) {
        super.onTransitionAnimationStart(z, z2);
        this.isFragmentOpened = z;
        if (((!z && z2) || (z && !z2)) && this.playProfileAnimation != 0 && this.allowProfileAnimation && !this.isPulledDown) {
            this.openAnimationInProgress = true;
        }
        if (z) {
            if (this.imageUpdater != null) {
                this.transitionIndex = getNotificationCenter().setAnimationInProgress(this.transitionIndex, new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.mediaCountDidLoad, NotificationCenter.mediaCountsDidLoad, NotificationCenter.userInfoDidLoad});
            } else {
                this.transitionIndex = getNotificationCenter().setAnimationInProgress(this.transitionIndex, new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.mediaCountDidLoad, NotificationCenter.mediaCountsDidLoad});
            }
            if (Build.VERSION.SDK_INT >= 21 && !z2 && getParentActivity() != null) {
                this.navigationBarAnimationColorFrom = getParentActivity().getWindow().getNavigationBarColor();
            }
        }
        this.transitionAnimationInProress = true;
        checkPhotoDescriptionAlpha();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            if (!z2) {
                int i = this.playProfileAnimation;
                if (i != 0 && this.allowProfileAnimation) {
                    if (i == 1) {
                        this.currentExpandAnimatorValue = 0.0f;
                    }
                    this.openAnimationInProgress = false;
                    checkListViewScroll();
                    if (this.recreateMenuAfterAnimation) {
                        createActionBarMenu(true);
                    }
                }
                if (!this.fragmentOpened) {
                    this.fragmentOpened = true;
                    this.invalidateScroll = true;
                    this.fragmentView.requestLayout();
                }
            }
            getNotificationCenter().onAnimationFinish(this.transitionIndex);
            View view = this.blurredView;
            if (view != null && view.getVisibility() == 0) {
                this.blurredView.setVisibility(8);
                this.blurredView.setBackground(null);
            }
        }
        this.transitionAnimationInProress = false;
        checkPhotoDescriptionAlpha();
    }

    @Keep
    public float getAvatarAnimationProgress() {
        return this.avatarAnimationProgress;
    }

    @Keep
    public void setAvatarAnimationProgress(float f) {
        int profileBackColorForId;
        int profileTextColorForId;
        this.currentExpandAnimatorValue = f;
        this.avatarAnimationProgress = f;
        checkPhotoDescriptionAlpha();
        if (this.playProfileAnimation == 2) {
            this.avatarImage.setProgressToExpand(f);
        }
        this.listView.setAlpha(f);
        this.listView.setTranslationX(AndroidUtilities.dp(48.0f) - (AndroidUtilities.dp(48.0f) * f));
        long j = 5;
        if (this.playProfileAnimation != 2 || (profileBackColorForId = this.avatarColor) == 0) {
            profileBackColorForId = AvatarDrawable.getProfileBackColorForId((this.userId != 0 || (ChatObject.isChannel(this.chatId, this.currentAccount) && !this.currentChat.megagroup)) ? 5L : this.chatId, this.resourcesProvider);
        }
        int i = this.actionBarAnimationColorFrom;
        if (i == 0) {
            i = getThemedColor(Theme.key_actionBarDefault);
        }
        this.topView.setBackgroundColor(ColorUtils.blendARGB(SharedConfig.chatBlurEnabled() ? ColorUtils.setAlphaComponent(i, 0) : i, profileBackColorForId, f));
        this.timerDrawable.setBackgroundColor(ColorUtils.blendARGB(i, profileBackColorForId, f));
        this.actionBar.setItemsColor(ColorUtils.blendARGB(this.peerColor != null ? -1 : getThemedColor(Theme.key_actionBarDefaultIcon), AvatarDrawable.getIconColorForId((this.userId != 0 || (ChatObject.isChannel(this.chatId, this.currentAccount) && !this.currentChat.megagroup)) ? 5L : this.chatId, this.resourcesProvider), this.avatarAnimationProgress), false);
        int themedColor = getThemedColor(Theme.key_profile_title);
        int themedColor2 = getThemedColor(Theme.key_actionBarDefaultTitle);
        for (int i2 = 0; i2 < 2; i2++) {
            SimpleTextView[] simpleTextViewArr = this.nameTextView;
            if (simpleTextViewArr[i2] != null && (i2 != 1 || this.playProfileAnimation != 2)) {
                simpleTextViewArr[i2].setTextColor(ColorUtils.blendARGB(themedColor2, themedColor, f));
            }
        }
        if (this.isOnline[0]) {
            profileTextColorForId = getThemedColor(Theme.key_profile_status);
        } else {
            if (this.userId == 0 && (!ChatObject.isChannel(this.chatId, this.currentAccount) || this.currentChat.megagroup)) {
                j = this.chatId;
            }
            profileTextColorForId = AvatarDrawable.getProfileTextColorForId(j, this.resourcesProvider);
        }
        int themedColor3 = getThemedColor(this.isOnline[0] ? Theme.key_chat_status : Theme.key_actionBarDefaultSubtitle);
        int i3 = 0;
        while (i3 < 3) {
            SimpleTextView[] simpleTextViewArr2 = this.onlineTextView;
            if (simpleTextViewArr2[i3] != null && i3 != 1 && (i3 != 2 || this.playProfileAnimation != 2)) {
                simpleTextViewArr2[i3].setTextColor(ColorUtils.blendARGB(i3 == 0 ? themedColor3 : applyPeerColor(themedColor3, true, Boolean.valueOf(this.isOnline[0])), i3 == 0 ? profileTextColorForId : applyPeerColor(profileTextColorForId, true, Boolean.valueOf(this.isOnline[0])), f));
            }
            i3++;
        }
        this.extraHeight = this.initialAnimationExtraHeight * f;
        long j2 = this.userId;
        if (j2 == 0) {
            j2 = this.chatId;
        }
        int profileColorForId = AvatarDrawable.getProfileColorForId(j2, this.resourcesProvider);
        long j3 = this.userId;
        if (j3 == 0) {
            j3 = this.chatId;
        }
        int colorForId = AvatarDrawable.getColorForId(j3);
        if (profileColorForId != colorForId) {
            this.avatarDrawable.setColor(ColorUtils.blendARGB(colorForId, profileColorForId, f));
            this.avatarImage.invalidate();
        }
        int i4 = this.navigationBarAnimationColorFrom;
        if (i4 != 0) {
            setNavigationBarColor(ColorUtils.blendARGB(i4, getNavigationBarColor(), f));
        }
        this.topView.invalidate();
        needLayout(true);
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
        AboutLinkCell aboutLinkCell = this.aboutLinkCell;
        if (aboutLinkCell != null) {
            aboutLinkCell.invalidate();
        }
        if (getDialogId() > 0) {
            AvatarImageView avatarImageView = this.avatarImage;
            if (avatarImageView != null) {
                avatarImageView.setProgressToStoriesInsets(this.avatarAnimationProgress);
            }
            ProfileStoriesView profileStoriesView = this.storyView;
            if (profileStoriesView != null) {
                profileStoriesView.setProgressToStoriesInsets(this.avatarAnimationProgress);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:174:0x0554  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AnimatorSet onCustomTransitionAnimation(boolean z, final Runnable runnable) {
        boolean z2;
        boolean z3;
        ChatAvatarContainer avatarContainer;
        float f;
        RightSlidingDialogContainer rightSlidingDialogContainer;
        if (this.playProfileAnimation == 0 || !this.allowProfileAnimation || this.isPulledDown || this.disableProfileAnimation) {
            return null;
        }
        ImageView imageView = this.timeItem;
        if (imageView != null) {
            imageView.setAlpha(1.0f);
        }
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout != null && iNavigationLayout.getFragmentStack().size() >= 2) {
            BaseFragment baseFragment = this.parentLayout.getFragmentStack().get(this.parentLayout.getFragmentStack().size() - 2);
            if (baseFragment instanceof ChatActivityInterface) {
                this.previousTransitionFragment = (ChatActivityInterface) baseFragment;
            }
            if ((baseFragment instanceof DialogsActivity) && (rightSlidingDialogContainer = ((DialogsActivity) baseFragment).rightSlidingDialogContainer) != null) {
                BaseFragment baseFragment2 = rightSlidingDialogContainer.currentFragment;
                if (baseFragment2 instanceof ChatActivityInterface) {
                    this.previousTransitionFragment = (ChatActivityInterface) baseFragment2;
                }
            }
        }
        if (this.previousTransitionFragment != null) {
            updateTimeItem();
        }
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(this.playProfileAnimation == 2 ? 250L : 180L);
        this.listView.setLayerType(2, null);
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (createMenu.getItem(10) == null && this.animatingItem == null) {
            this.animatingItem = createMenu.addItem(10, R.drawable.ic_ab_other);
        }
        if (z) {
            int i = 0;
            while (i < 2) {
                i++;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.onlineTextView[i].getLayoutParams();
                layoutParams.rightMargin = (int) ((AndroidUtilities.density * (-21.0f)) + AndroidUtilities.dp(8.0f));
                this.onlineTextView[i].setLayoutParams(layoutParams);
            }
            if (this.playProfileAnimation != 2) {
                int ceil = (int) Math.ceil((AndroidUtilities.displaySize.x - AndroidUtilities.dp(126.0f)) + (AndroidUtilities.density * 21.0f));
                float measureText = (this.nameTextView[1].getPaint().measureText(this.nameTextView[1].getText().toString()) * 1.12f) + this.nameTextView[1].getSideDrawablesSize();
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.nameTextView[1].getLayoutParams();
                if (ceil < measureText) {
                    layoutParams2.width = (int) Math.ceil(f / 1.12f);
                } else {
                    layoutParams2.width = -2;
                }
                this.nameTextView[1].setLayoutParams(layoutParams2);
                this.initialAnimationExtraHeight = AndroidUtilities.dp(88.0f);
            } else {
                FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.nameTextView[1].getLayoutParams();
                layoutParams3.width = (int) ((AndroidUtilities.displaySize.x - AndroidUtilities.dp(32.0f)) / 1.67f);
                this.nameTextView[1].setLayoutParams(layoutParams3);
            }
            this.fragmentView.setBackgroundColor(0);
            setAvatarAnimationProgress(0.0f);
            ArrayList arrayList = new ArrayList();
            arrayList.add(ObjectAnimator.ofFloat(this, "avatarAnimationProgress", 0.0f, 1.0f));
            RLottieImageView rLottieImageView = this.writeButton;
            if (rLottieImageView != null && rLottieImageView.getTag() == null) {
                this.writeButton.setScaleX(0.2f);
                this.writeButton.setScaleY(0.2f);
                this.writeButton.setAlpha(0.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_X, 1.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, 1.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, 1.0f));
            }
            if (this.playProfileAnimation == 2) {
                this.avatarColor = getAverageColor(this.avatarImage.getImageReceiver());
                this.nameTextView[1].setTextColor(-1);
                this.onlineTextView[1].setTextColor(-1275068417);
                this.actionBar.setItemsBackgroundColor(1090519039, false);
                this.overlaysView.setOverlaysVisible();
            }
            int i2 = 0;
            while (i2 < 2) {
                this.nameTextView[i2].setAlpha(i2 == 0 ? 1.0f : 0.0f);
                SimpleTextView simpleTextView = this.nameTextView[i2];
                Property property = View.ALPHA;
                float[] fArr = new float[1];
                fArr[0] = i2 == 0 ? 0.0f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(simpleTextView, property, fArr));
                i2++;
            }
            if (this.storyView != null) {
                if (getDialogId() > 0) {
                    this.storyView.setAlpha(0.0f);
                    arrayList.add(ObjectAnimator.ofFloat(this.storyView, View.ALPHA, 1.0f));
                } else {
                    this.storyView.setAlpha(1.0f);
                    this.storyView.setFragmentTransitionProgress(0.0f);
                    arrayList.add(ObjectAnimator.ofFloat(this.storyView, "fragmentTransitionProgress", 1.0f));
                }
            }
            if (this.timeItem.getTag() != null) {
                arrayList.add(ObjectAnimator.ofFloat(this.timeItem, View.ALPHA, 1.0f, 0.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_X, 1.0f, 0.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_Y, 1.0f, 0.0f));
            }
            ActionBarMenuItem actionBarMenuItem = this.animatingItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setAlpha(1.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.animatingItem, View.ALPHA, 0.0f));
            }
            if (this.callItemVisible && this.chatId != 0) {
                this.callItem.setAlpha(0.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.callItem, View.ALPHA, 1.0f));
            }
            if (this.videoCallItemVisible) {
                this.videoCallItem.setAlpha(0.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.videoCallItem, View.ALPHA, 1.0f));
            }
            if (this.editItemVisible) {
                this.editItem.setAlpha(0.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.editItem, View.ALPHA, 1.0f));
            }
            if (this.ttlIconView.getTag() != null) {
                this.ttlIconView.setAlpha(0.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.ttlIconView, View.ALPHA, 1.0f));
            }
            FrameLayout frameLayout = this.floatingButtonContainer;
            if (frameLayout != null) {
                frameLayout.setAlpha(0.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.floatingButtonContainer, View.ALPHA, 1.0f));
            }
            ChatActivityInterface chatActivityInterface = this.previousTransitionFragment;
            if (chatActivityInterface == null || (((avatarContainer = chatActivityInterface.getAvatarContainer()) == null || avatarContainer.getSubtitleTextView().getLeftDrawable() == null) && !avatarContainer.statusMadeShorter[0])) {
                z3 = false;
            } else {
                this.transitionOnlineText = avatarContainer.getSubtitleTextView();
                this.avatarContainer2.invalidate();
                this.onlineTextView[0].setAlpha(0.0f);
                this.onlineTextView[1].setAlpha(0.0f);
                arrayList.add(ObjectAnimator.ofFloat(this.onlineTextView[1], View.ALPHA, 1.0f));
                z3 = true;
            }
            if (!z3) {
                int i3 = 0;
                while (i3 < 2) {
                    this.onlineTextView[i3].setAlpha(i3 == 0 ? 1.0f : 0.0f);
                    SimpleTextView simpleTextView2 = this.onlineTextView[i3];
                    Property property2 = View.ALPHA;
                    float[] fArr2 = new float[1];
                    fArr2[0] = i3 == 0 ? 0.0f : 1.0f;
                    arrayList.add(ObjectAnimator.ofFloat(simpleTextView2, property2, fArr2));
                    i3++;
                }
            }
            animatorSet.playTogether(arrayList);
        } else {
            this.initialAnimationExtraHeight = this.extraHeight;
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(ObjectAnimator.ofFloat(this, "avatarAnimationProgress", 1.0f, 0.0f));
            RLottieImageView rLottieImageView2 = this.writeButton;
            if (rLottieImageView2 != null) {
                arrayList2.add(ObjectAnimator.ofFloat(rLottieImageView2, View.SCALE_X, 0.2f));
                arrayList2.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, 0.2f));
                arrayList2.add(ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, 0.0f));
            }
            int i4 = 0;
            while (i4 < 2) {
                SimpleTextView simpleTextView3 = this.nameTextView[i4];
                Property property3 = View.ALPHA;
                float[] fArr3 = new float[1];
                fArr3[0] = i4 == 0 ? 1.0f : 0.0f;
                arrayList2.add(ObjectAnimator.ofFloat(simpleTextView3, property3, fArr3));
                i4++;
            }
            ProfileStoriesView profileStoriesView = this.storyView;
            if (profileStoriesView != null) {
                if (this.dialogId > 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(profileStoriesView, View.ALPHA, 0.0f));
                } else {
                    arrayList2.add(ObjectAnimator.ofFloat(profileStoriesView, "fragmentTransitionProgress", 0.0f));
                }
            }
            if (this.timeItem.getTag() != null) {
                this.timeItem.setAlpha(0.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.timeItem, View.ALPHA, 0.0f, 1.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_X, 0.0f, 1.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_Y, 0.0f, 1.0f));
            }
            ActionBarMenuItem actionBarMenuItem2 = this.animatingItem;
            if (actionBarMenuItem2 != null) {
                actionBarMenuItem2.setAlpha(0.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.animatingItem, View.ALPHA, 1.0f));
            }
            if (this.callItemVisible && this.chatId != 0) {
                this.callItem.setAlpha(1.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.callItem, View.ALPHA, 0.0f));
            }
            if (this.videoCallItemVisible) {
                this.videoCallItem.setAlpha(1.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.videoCallItem, View.ALPHA, 0.0f));
            }
            if (this.editItemVisible) {
                this.editItem.setAlpha(1.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.editItem, View.ALPHA, 0.0f));
            }
            ImageView imageView2 = this.ttlIconView;
            if (imageView2 != null) {
                arrayList2.add(ObjectAnimator.ofFloat(imageView2, View.ALPHA, imageView2.getAlpha(), 0.0f));
            }
            FrameLayout frameLayout2 = this.floatingButtonContainer;
            if (frameLayout2 != null) {
                arrayList2.add(ObjectAnimator.ofFloat(frameLayout2, View.ALPHA, 0.0f));
            }
            BaseFragment baseFragment3 = this.parentLayout.getFragmentStack().size() > 1 ? this.parentLayout.getFragmentStack().get(this.parentLayout.getFragmentStack().size() - 2) : null;
            if (baseFragment3 instanceof ChatActivity) {
                ChatAvatarContainer avatarContainer2 = ((ChatActivity) baseFragment3).getAvatarContainer();
                if (avatarContainer2.getSubtitleTextView().getLeftDrawable() != null || avatarContainer2.statusMadeShorter[0]) {
                    this.transitionOnlineText = avatarContainer2.getSubtitleTextView();
                    this.avatarContainer2.invalidate();
                    arrayList2.add(ObjectAnimator.ofFloat(this.onlineTextView[0], View.ALPHA, 0.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.onlineTextView[1], View.ALPHA, 0.0f));
                    z2 = true;
                    if (!z2) {
                        int i5 = 0;
                        while (i5 < 2) {
                            SimpleTextView simpleTextView4 = this.onlineTextView[i5];
                            Property property4 = View.ALPHA;
                            float[] fArr4 = new float[1];
                            fArr4[0] = i5 == 0 ? 1.0f : 0.0f;
                            arrayList2.add(ObjectAnimator.ofFloat(simpleTextView4, property4, fArr4));
                            i5++;
                        }
                    }
                    animatorSet.playTogether(arrayList2);
                }
            }
            z2 = false;
            if (!z2) {
            }
            animatorSet.playTogether(arrayList2);
        }
        this.profileTransitionInProgress = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProfileActivity.this.lambda$onCustomTransitionAnimation$51(valueAnimator);
            }
        });
        animatorSet.playTogether(ofFloat);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.47
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (((BaseFragment) ProfileActivity.this).fragmentView != null) {
                    ProfileActivity.this.avatarImage.setProgressToExpand(0.0f);
                    ProfileActivity.this.listView.setLayerType(0, null);
                    if (ProfileActivity.this.animatingItem != null) {
                        ((BaseFragment) ProfileActivity.this).actionBar.createMenu().clearItems();
                        ProfileActivity.this.animatingItem = null;
                    }
                    runnable.run();
                    if (ProfileActivity.this.playProfileAnimation == 2) {
                        ProfileActivity.this.playProfileAnimation = 1;
                        ProfileActivity.this.avatarImage.setForegroundAlpha(1.0f);
                        ProfileActivity.this.avatarContainer.setVisibility(8);
                        ProfileActivity.this.avatarsViewPager.resetCurrentItem();
                        ProfileActivity.this.avatarsViewPager.setVisibility(0);
                    }
                    ProfileActivity.this.transitionOnlineText = null;
                    ProfileActivity.this.avatarContainer2.invalidate();
                    ProfileActivity profileActivity = ProfileActivity.this;
                    profileActivity.profileTransitionInProgress = false;
                    profileActivity.previousTransitionFragment = null;
                    ((BaseFragment) profileActivity).fragmentView.invalidate();
                    return;
                }
                runnable.run();
            }
        });
        animatorSet.setInterpolator(this.playProfileAnimation == 2 ? CubicBezierInterpolator.DEFAULT : new DecelerateInterpolator());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                animatorSet.start();
            }
        }, 50L);
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCustomTransitionAnimation$51(ValueAnimator valueAnimator) {
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
        updateStoriesViewBounds(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAverageColor(ImageReceiver imageReceiver) {
        if (imageReceiver.getDrawable() instanceof VectorAvatarThumbDrawable) {
            return ((VectorAvatarThumbDrawable) imageReceiver.getDrawable()).gradientTools.getAverageColor();
        }
        return AndroidUtilities.calcBitmapColor(this.avatarImage.getImageReceiver().getBitmap());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOnlineCount(boolean z) {
        TLRPC$UserStatus tLRPC$UserStatus;
        this.onlineCount = 0;
        final int currentTime = getConnectionsManager().getCurrentTime();
        this.sortedUsers.clear();
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if ((tLRPC$ChatFull instanceof TLRPC$TL_chatFull) || ((tLRPC$ChatFull instanceof TLRPC$TL_channelFull) && tLRPC$ChatFull.participants_count <= 200 && tLRPC$ChatFull.participants != null)) {
            for (int i = 0; i < this.chatInfo.participants.participants.size(); i++) {
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.chatInfo.participants.participants.get(i).user_id));
                if (user != null && (tLRPC$UserStatus = user.status) != null && ((tLRPC$UserStatus.expires > currentTime || user.id == getUserConfig().getClientUserId()) && user.status.expires > 10000)) {
                    this.onlineCount++;
                }
                this.sortedUsers.add(Integer.valueOf(i));
            }
            try {
                Collections.sort(this.sortedUsers, Comparator$-EL.reversed(Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda50
                    @Override // j$.util.function.ToIntFunction
                    public final int applyAsInt(Object obj) {
                        int lambda$updateOnlineCount$52;
                        lambda$updateOnlineCount$52 = ProfileActivity.this.lambda$updateOnlineCount$52(currentTime, obj);
                        return lambda$updateOnlineCount$52;
                    }
                })));
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (z && this.listAdapter != null && this.membersStartRow > 0) {
                AndroidUtilities.updateVisibleRows(this.listView);
            }
            if (this.sharedMediaLayout == null || this.sharedMediaRow == -1) {
                return;
            }
            if ((this.sortedUsers.size() > 5 || this.usersForceShowingIn == 2) && this.usersForceShowingIn != 1) {
                this.sharedMediaLayout.setChatUsers(this.sortedUsers, this.chatInfo);
            }
        } else if ((tLRPC$ChatFull instanceof TLRPC$TL_channelFull) && tLRPC$ChatFull.participants_count > 200) {
            this.onlineCount = tLRPC$ChatFull.online_count;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$updateOnlineCount$52(int i, Object obj) {
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.chatInfo.participants.participants.get(((Integer) obj).intValue()).user_id));
        if (user != null) {
            if (user.bot) {
                return -110;
            }
            if (user.self) {
                return i + 50000;
            }
            TLRPC$UserStatus tLRPC$UserStatus = user.status;
            if (tLRPC$UserStatus != null) {
                return tLRPC$UserStatus.expires;
            }
            return Integer.MIN_VALUE;
        }
        return Integer.MIN_VALUE;
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        TLRPC$ChatFull tLRPC$ChatFull2;
        this.chatInfo = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            long j = tLRPC$ChatFull.migrated_from_chat_id;
            if (j != 0 && this.mergeDialogId == 0) {
                this.mergeDialogId = -j;
                getMediaDataController().getMediaCounts(this.mergeDialogId, this.topicId, this.classGuid);
            }
        }
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            sharedMediaLayout.setChatInfo(this.chatInfo);
        }
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        if (profileGalleryView != null && !this.isTopic) {
            profileGalleryView.setChatInfo(this.chatInfo);
        }
        ProfileStoriesView profileStoriesView = this.storyView;
        if (profileStoriesView != null && (tLRPC$ChatFull2 = this.chatInfo) != null) {
            profileStoriesView.setStories(tLRPC$ChatFull2.stories);
        }
        AvatarImageView avatarImageView = this.avatarImage;
        if (avatarImageView != null) {
            avatarImageView.setHasStories(needInsetForStories());
        }
        fetchUsersFromChannelInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needInsetForStories() {
        return getMessagesController().getStoriesController().hasStories(getDialogId());
    }

    public void setUserInfo(TLRPC$UserFull tLRPC$UserFull) {
        this.userInfo = tLRPC$UserFull;
        ProfileStoriesView profileStoriesView = this.storyView;
        if (profileStoriesView != null) {
            profileStoriesView.setStories(tLRPC$UserFull.stories);
        }
        AvatarImageView avatarImageView = this.avatarImage;
        if (avatarImageView != null) {
            avatarImageView.setHasStories(needInsetForStories());
        }
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            sharedMediaLayout.setUserInfo(this.userInfo);
        }
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public boolean canSearchMembers() {
        return this.canSearchMembers;
    }

    private void fetchUsersFromChannelInfo() {
        TLRPC$Chat tLRPC$Chat = this.currentChat;
        if (tLRPC$Chat == null || !tLRPC$Chat.megagroup) {
            return;
        }
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if (!(tLRPC$ChatFull instanceof TLRPC$TL_channelFull) || tLRPC$ChatFull.participants == null) {
            return;
        }
        for (int i = 0; i < this.chatInfo.participants.participants.size(); i++) {
            TLRPC$ChatParticipant tLRPC$ChatParticipant = this.chatInfo.participants.participants.get(i);
            this.participantsMap.put(tLRPC$ChatParticipant.user_id, tLRPC$ChatParticipant);
        }
    }

    private void kickUser(long j, TLRPC$ChatParticipant tLRPC$ChatParticipant) {
        if (j != 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
            getMessagesController().deleteParticipantFromChat(this.chatId, user);
            if (this.currentChat != null && user != null && BulletinFactory.canShowBulletin(this)) {
                BulletinFactory.createRemoveFromChatBulletin(this, user, this.currentChat.title).show();
            }
            if (this.chatInfo.participants.participants.remove(tLRPC$ChatParticipant)) {
                updateListAnimated(true);
                return;
            }
            return;
        }
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.closeChats;
        notificationCenter.removeObserver(this, i);
        if (AndroidUtilities.isTablet()) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, Long.valueOf(-this.chatId));
        } else {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, new Object[0]);
        }
        getMessagesController().deleteParticipantFromChat(this.chatId, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())));
        this.playProfileAnimation = 0;
        finishFragment();
    }

    public boolean isChat() {
        return this.chatId != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x010a, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated) == false) goto L38;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRowsIds() {
        boolean z;
        TLRPC$ChatParticipants tLRPC$ChatParticipants;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$ChatFull tLRPC$ChatFull2;
        TLRPC$ChatParticipants tLRPC$ChatParticipants2;
        ArrayList<TLRPC$ChatParticipant> arrayList;
        TLRPC$ChatFull tLRPC$ChatFull3;
        TLRPC$ChatFull tLRPC$ChatFull4;
        int i;
        boolean z2;
        TLRPC$UserFull tLRPC$UserFull;
        TLRPC$ChatFull tLRPC$ChatFull5;
        TLRPC$UserFull tLRPC$UserFull2;
        int i2 = this.rowCount;
        this.rowCount = 0;
        this.setAvatarRow = -1;
        this.setAvatarSectionRow = -1;
        this.numberSectionRow = -1;
        this.numberRow = -1;
        this.setUsernameRow = -1;
        this.bioRow = -1;
        this.phoneSuggestionSectionRow = -1;
        this.phoneSuggestionRow = -1;
        this.passwordSuggestionSectionRow = -1;
        this.passwordSuggestionRow = -1;
        this.settingsSectionRow = -1;
        this.settingsSectionRow2 = -1;
        this.notificationRow = -1;
        this.languageRow = -1;
        this.premiumRow = -1;
        this.premiumSectionsRow = -1;
        this.privacyRow = -1;
        this.dataRow = -1;
        this.chatRow = -1;
        this.filtersRow = -1;
        this.liteModeRow = -1;
        this.stickersRow = -1;
        this.devicesRow = -1;
        this.devicesSectionRow = -1;
        this.helpHeaderRow = -1;
        this.questionRow = -1;
        this.faqRow = -1;
        this.policyRow = -1;
        this.helpSectionCell = -1;
        this.debugHeaderRow = -1;
        this.sendLogsRow = -1;
        this.sendLastLogsRow = -1;
        this.clearLogsRow = -1;
        this.switchBackendRow = -1;
        this.versionRow = -1;
        this.sendMessageRow = -1;
        this.reportRow = -1;
        this.reportReactionRow = -1;
        this.addToContactsRow = -1;
        this.emptyRow = -1;
        this.infoHeaderRow = -1;
        this.phoneRow = -1;
        this.userInfoRow = -1;
        this.locationRow = -1;
        this.channelInfoRow = -1;
        this.usernameRow = -1;
        this.settingsTimerRow = -1;
        this.settingsKeyRow = -1;
        this.notificationsDividerRow = -1;
        this.reportDividerRow = -1;
        this.notificationsRow = -1;
        this.infoSectionRow = -1;
        this.secretSettingsSectionRow = -1;
        this.bottomPaddingRow = -1;
        this.addToGroupButtonRow = -1;
        this.addToGroupInfoRow = -1;
        this.membersHeaderRow = -1;
        this.membersStartRow = -1;
        this.membersEndRow = -1;
        this.addMemberRow = -1;
        this.subscribersRow = -1;
        this.subscribersRequestsRow = -1;
        this.administratorsRow = -1;
        this.blockedUsersRow = -1;
        this.membersSectionRow = -1;
        this.sharedMediaRow = -1;
        this.notificationsSimpleRow = -1;
        this.settingsRow = -1;
        this.unblockRow = -1;
        this.joinRow = -1;
        this.lastSectionRow = -1;
        this.visibleChatParticipants.clear();
        this.visibleSortedUsers.clear();
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
        boolean z3 = true;
        if (sharedMediaPreloader != null) {
            for (int i3 : sharedMediaPreloader.getLastMediaCount()) {
                if (i3 > 0) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (!z && (tLRPC$UserFull2 = this.userInfo) != null) {
            z = tLRPC$UserFull2.stories_pinned_available;
        }
        if (!z && (tLRPC$ChatFull5 = this.chatInfo) != null) {
            z = tLRPC$ChatFull5.stories_pinned_available;
        }
        if (!z && MessagesController.ChannelRecommendations.hasRecommendations(this.currentAccount, this.chatId)) {
            z = true;
        }
        if (this.userId != 0) {
            if (LocaleController.isRTL) {
                int i4 = this.rowCount;
                this.rowCount = i4 + 1;
                this.emptyRow = i4;
            }
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            if (UserObject.isUserSelf(user)) {
                if (this.avatarBig == null) {
                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = user.photo;
                    if (tLRPC$UserProfilePhoto != null) {
                        TLRPC$FileLocation tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_big;
                        if (!(tLRPC$FileLocation instanceof TLRPC$TL_fileLocation_layer97)) {
                        }
                    }
                    ProfileGalleryView profileGalleryView = this.avatarsViewPager;
                    if (profileGalleryView == null || profileGalleryView.getRealCount() == 0) {
                        int i5 = this.rowCount;
                        int i6 = i5 + 1;
                        this.rowCount = i6;
                        this.setAvatarRow = i5;
                        this.rowCount = i6 + 1;
                        this.setAvatarSectionRow = i6;
                    }
                }
                int i7 = this.rowCount;
                int i8 = i7 + 1;
                this.rowCount = i8;
                this.numberSectionRow = i7;
                int i9 = i8 + 1;
                this.rowCount = i9;
                this.numberRow = i8;
                int i10 = i9 + 1;
                this.rowCount = i10;
                this.setUsernameRow = i9;
                int i11 = i10 + 1;
                this.rowCount = i11;
                this.bioRow = i10;
                this.rowCount = i11 + 1;
                this.settingsSectionRow = i11;
                Set<String> set = getMessagesController().pendingSuggestions;
                if (set.contains("VALIDATE_PHONE_NUMBER")) {
                    int i12 = this.rowCount;
                    int i13 = i12 + 1;
                    this.rowCount = i13;
                    this.phoneSuggestionRow = i12;
                    this.rowCount = i13 + 1;
                    this.phoneSuggestionSectionRow = i13;
                }
                if (set.contains("VALIDATE_PASSWORD")) {
                    int i14 = this.rowCount;
                    int i15 = i14 + 1;
                    this.rowCount = i15;
                    this.passwordSuggestionRow = i14;
                    this.rowCount = i15 + 1;
                    this.passwordSuggestionSectionRow = i15;
                }
                int i16 = this.rowCount;
                int i17 = i16 + 1;
                this.rowCount = i17;
                this.settingsSectionRow2 = i16;
                int i18 = i17 + 1;
                this.rowCount = i18;
                this.chatRow = i17;
                int i19 = i18 + 1;
                this.rowCount = i19;
                this.privacyRow = i18;
                int i20 = i19 + 1;
                this.rowCount = i20;
                this.notificationRow = i19;
                int i21 = i20 + 1;
                this.rowCount = i21;
                this.dataRow = i20;
                this.rowCount = i21 + 1;
                this.liteModeRow = i21;
                if (!getMessagesController().filtersEnabled) {
                    getMessagesController().dialogFilters.isEmpty();
                }
                int i22 = this.rowCount;
                int i23 = i22 + 1;
                this.rowCount = i23;
                this.filtersRow = i22;
                int i24 = i23 + 1;
                this.rowCount = i24;
                this.devicesRow = i23;
                int i25 = i24 + 1;
                this.rowCount = i25;
                this.languageRow = i24;
                this.rowCount = i25 + 1;
                this.devicesSectionRow = i25;
                if (!getMessagesController().premiumLocked) {
                    int i26 = this.rowCount;
                    int i27 = i26 + 1;
                    this.rowCount = i27;
                    this.premiumRow = i26;
                    this.rowCount = i27 + 1;
                    this.premiumSectionsRow = i27;
                }
                int i28 = this.rowCount;
                int i29 = i28 + 1;
                this.rowCount = i29;
                this.helpHeaderRow = i28;
                int i30 = i29 + 1;
                this.rowCount = i30;
                this.questionRow = i29;
                int i31 = i30 + 1;
                this.rowCount = i31;
                this.faqRow = i30;
                int i32 = i31 + 1;
                this.rowCount = i32;
                this.policyRow = i31;
                if (BuildVars.LOGS_ENABLED || BuildVars.DEBUG_PRIVATE_VERSION) {
                    int i33 = i32 + 1;
                    this.rowCount = i33;
                    this.helpSectionCell = i32;
                    this.rowCount = i33 + 1;
                    this.debugHeaderRow = i33;
                }
                if (BuildVars.LOGS_ENABLED) {
                    int i34 = this.rowCount;
                    int i35 = i34 + 1;
                    this.rowCount = i35;
                    this.sendLogsRow = i34;
                    int i36 = i35 + 1;
                    this.rowCount = i36;
                    this.sendLastLogsRow = i35;
                    this.rowCount = i36 + 1;
                    this.clearLogsRow = i36;
                }
                if (BuildVars.DEBUG_VERSION) {
                    int i37 = this.rowCount;
                    this.rowCount = i37 + 1;
                    this.switchBackendRow = i37;
                }
                int i38 = this.rowCount;
                this.rowCount = i38 + 1;
                this.versionRow = i38;
            } else {
                String publicUsername = UserObject.getPublicUsername(user);
                TLRPC$UserFull tLRPC$UserFull3 = this.userInfo;
                boolean z4 = ((tLRPC$UserFull3 == null || TextUtils.isEmpty(tLRPC$UserFull3.about)) && (user == null || TextUtils.isEmpty(publicUsername))) ? false : true;
                boolean z5 = (user == null || (TextUtils.isEmpty(user.phone) && TextUtils.isEmpty(this.vcardPhone))) ? false : true;
                int i39 = this.rowCount;
                int i40 = i39 + 1;
                this.rowCount = i40;
                this.infoHeaderRow = i39;
                if (!this.isBot && (z5 || !z4)) {
                    this.rowCount = i40 + 1;
                    this.phoneRow = i40;
                }
                TLRPC$UserFull tLRPC$UserFull4 = this.userInfo;
                if (tLRPC$UserFull4 != null && !TextUtils.isEmpty(tLRPC$UserFull4.about)) {
                    int i41 = this.rowCount;
                    this.rowCount = i41 + 1;
                    this.userInfoRow = i41;
                }
                if (user != null && publicUsername != null) {
                    int i42 = this.rowCount;
                    this.rowCount = i42 + 1;
                    this.usernameRow = i42;
                }
                if (this.phoneRow != -1 || this.userInfoRow != -1 || this.usernameRow != -1) {
                    int i43 = this.rowCount;
                    this.rowCount = i43 + 1;
                    this.notificationsDividerRow = i43;
                }
                if (this.userId != getUserConfig().getClientUserId()) {
                    int i44 = this.rowCount;
                    this.rowCount = i44 + 1;
                    this.notificationsRow = i44;
                }
                int i45 = this.rowCount;
                int i46 = i45 + 1;
                this.rowCount = i46;
                this.infoSectionRow = i45;
                TLRPC$EncryptedChat tLRPC$EncryptedChat = this.currentEncryptedChat;
                if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                    int i47 = i46 + 1;
                    this.rowCount = i47;
                    this.settingsTimerRow = i46;
                    int i48 = i47 + 1;
                    this.rowCount = i48;
                    this.settingsKeyRow = i47;
                    this.rowCount = i48 + 1;
                    this.secretSettingsSectionRow = i48;
                }
                if (user != null && !this.isBot && tLRPC$EncryptedChat == null && user.id != getUserConfig().getClientUserId() && this.userBlocked) {
                    int i49 = this.rowCount;
                    int i50 = i49 + 1;
                    this.rowCount = i50;
                    this.unblockRow = i49;
                    this.rowCount = i50 + 1;
                    this.lastSectionRow = i50;
                }
                if (user != null && this.isBot && !user.bot_nochats) {
                    int i51 = this.rowCount;
                    int i52 = i51 + 1;
                    this.rowCount = i52;
                    this.addToGroupButtonRow = i51;
                    this.rowCount = i52 + 1;
                    this.addToGroupInfoRow = i52;
                }
                if (!this.showAddToContacts || user == null || user.contact || user.bot || UserObject.isService(user.id)) {
                    z2 = false;
                } else {
                    int i53 = this.rowCount;
                    this.rowCount = i53 + 1;
                    this.addToContactsRow = i53;
                    z2 = true;
                }
                if (this.reportReactionMessageId == 0 || ContactsController.getInstance(this.currentAccount).isContact(this.userId)) {
                    z3 = z2;
                } else {
                    int i54 = this.rowCount;
                    this.rowCount = i54 + 1;
                    this.reportReactionRow = i54;
                }
                if (z3) {
                    int i55 = this.rowCount;
                    this.rowCount = i55 + 1;
                    this.reportDividerRow = i55;
                }
                if (z || ((tLRPC$UserFull = this.userInfo) != null && tLRPC$UserFull.common_chats_count != 0)) {
                    int i56 = this.rowCount;
                    this.rowCount = i56 + 1;
                    this.sharedMediaRow = i56;
                } else if (this.lastSectionRow == -1 && this.needSendMessage) {
                    int i57 = this.rowCount;
                    int i58 = i57 + 1;
                    this.rowCount = i58;
                    this.sendMessageRow = i57;
                    int i59 = i58 + 1;
                    this.rowCount = i59;
                    this.reportRow = i58;
                    this.rowCount = i59 + 1;
                    this.lastSectionRow = i59;
                }
            }
        } else if (this.isTopic) {
            int i60 = this.rowCount;
            int i61 = i60 + 1;
            this.rowCount = i61;
            this.infoHeaderRow = i60;
            int i62 = i61 + 1;
            this.rowCount = i62;
            this.usernameRow = i61;
            int i63 = i62 + 1;
            this.rowCount = i63;
            this.notificationsSimpleRow = i62;
            int i64 = i63 + 1;
            this.rowCount = i64;
            this.infoSectionRow = i63;
            if (z) {
                this.rowCount = i64 + 1;
                this.sharedMediaRow = i64;
            }
        } else if (this.chatId != 0) {
            TLRPC$ChatFull tLRPC$ChatFull6 = this.chatInfo;
            if ((tLRPC$ChatFull6 != null && (!TextUtils.isEmpty(tLRPC$ChatFull6.about) || (this.chatInfo.location instanceof TLRPC$TL_channelLocation))) || ChatObject.isPublic(this.currentChat)) {
                if (LocaleController.isRTL && ChatObject.isChannel(this.currentChat) && (tLRPC$ChatFull4 = this.chatInfo) != null && !this.currentChat.megagroup && tLRPC$ChatFull4.linked_chat_id != 0) {
                    int i65 = this.rowCount;
                    this.rowCount = i65 + 1;
                    this.emptyRow = i65;
                }
                int i66 = this.rowCount;
                this.rowCount = i66 + 1;
                this.infoHeaderRow = i66;
                TLRPC$ChatFull tLRPC$ChatFull7 = this.chatInfo;
                if (tLRPC$ChatFull7 != null) {
                    if (!TextUtils.isEmpty(tLRPC$ChatFull7.about)) {
                        int i67 = this.rowCount;
                        this.rowCount = i67 + 1;
                        this.channelInfoRow = i67;
                    }
                    if (this.chatInfo.location instanceof TLRPC$TL_channelLocation) {
                        int i68 = this.rowCount;
                        this.rowCount = i68 + 1;
                        this.locationRow = i68;
                    }
                }
                if (ChatObject.isPublic(this.currentChat)) {
                    int i69 = this.rowCount;
                    this.rowCount = i69 + 1;
                    this.usernameRow = i69;
                }
            }
            if (this.infoHeaderRow != -1) {
                int i70 = this.rowCount;
                this.rowCount = i70 + 1;
                this.notificationsDividerRow = i70;
            }
            int i71 = this.rowCount;
            int i72 = i71 + 1;
            this.rowCount = i72;
            this.notificationsRow = i71;
            this.rowCount = i72 + 1;
            this.infoSectionRow = i72;
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC$Chat tLRPC$Chat = this.currentChat;
                if (!tLRPC$Chat.megagroup && (tLRPC$ChatFull3 = this.chatInfo) != null && (tLRPC$Chat.creator || tLRPC$ChatFull3.can_view_participants)) {
                    int i73 = this.rowCount;
                    int i74 = i73 + 1;
                    this.rowCount = i74;
                    this.membersHeaderRow = i73;
                    int i75 = i74 + 1;
                    this.rowCount = i75;
                    this.subscribersRow = i74;
                    if (tLRPC$ChatFull3.requests_pending > 0) {
                        this.rowCount = i75 + 1;
                        this.subscribersRequestsRow = i75;
                    }
                    int i76 = this.rowCount;
                    int i77 = i76 + 1;
                    this.rowCount = i77;
                    this.administratorsRow = i76;
                    if (tLRPC$ChatFull3.banned_count != 0 || tLRPC$ChatFull3.kicked_count != 0) {
                        this.rowCount = i77 + 1;
                        this.blockedUsersRow = i77;
                    }
                    int i78 = this.rowCount;
                    int i79 = i78 + 1;
                    this.rowCount = i79;
                    this.settingsRow = i78;
                    this.rowCount = i79 + 1;
                    this.membersSectionRow = i79;
                }
            }
            if (ChatObject.isChannel(this.currentChat)) {
                if (!this.isTopic && (tLRPC$ChatFull2 = this.chatInfo) != null && this.currentChat.megagroup && (tLRPC$ChatParticipants2 = tLRPC$ChatFull2.participants) != null && (arrayList = tLRPC$ChatParticipants2.participants) != null && !arrayList.isEmpty()) {
                    if (!ChatObject.isNotInChat(this.currentChat) && ChatObject.canAddUsers(this.currentChat) && this.chatInfo.participants_count < getMessagesController().maxMegagroupCount) {
                        int i80 = this.rowCount;
                        this.rowCount = i80 + 1;
                        this.addMemberRow = i80;
                    }
                    int size = this.chatInfo.participants.participants.size();
                    if ((size <= 5 || !z || this.usersForceShowingIn == 1) && this.usersForceShowingIn != 2) {
                        if (this.addMemberRow == -1) {
                            int i81 = this.rowCount;
                            this.rowCount = i81 + 1;
                            this.membersHeaderRow = i81;
                        }
                        int i82 = this.rowCount;
                        this.membersStartRow = i82;
                        int i83 = i82 + size;
                        this.rowCount = i83;
                        this.membersEndRow = i83;
                        this.rowCount = i83 + 1;
                        this.membersSectionRow = i83;
                        this.visibleChatParticipants.addAll(this.chatInfo.participants.participants);
                        ArrayList<Integer> arrayList2 = this.sortedUsers;
                        if (arrayList2 != null) {
                            this.visibleSortedUsers.addAll(arrayList2);
                        }
                        this.usersForceShowingIn = 1;
                        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
                        if (sharedMediaLayout != null) {
                            sharedMediaLayout.setChatUsers(null, null);
                        }
                    } else {
                        if (this.addMemberRow != -1) {
                            int i84 = this.rowCount;
                            this.rowCount = i84 + 1;
                            this.membersSectionRow = i84;
                        }
                        if (this.sharedMediaLayout != null) {
                            if (!this.sortedUsers.isEmpty()) {
                                this.usersForceShowingIn = 2;
                            }
                            this.sharedMediaLayout.setChatUsers(this.sortedUsers, this.chatInfo);
                        }
                    }
                } else {
                    if (!ChatObject.isNotInChat(this.currentChat) && ChatObject.canAddUsers(this.currentChat) && (tLRPC$ChatFull = this.chatInfo) != null && tLRPC$ChatFull.participants_hidden) {
                        int i85 = this.rowCount;
                        int i86 = i85 + 1;
                        this.rowCount = i86;
                        this.addMemberRow = i85;
                        this.rowCount = i86 + 1;
                        this.membersSectionRow = i86;
                    }
                    SharedMediaLayout sharedMediaLayout2 = this.sharedMediaLayout;
                    if (sharedMediaLayout2 != null) {
                        sharedMediaLayout2.updateAdapters();
                    }
                }
                if (this.lastSectionRow == -1) {
                    TLRPC$Chat tLRPC$Chat2 = this.currentChat;
                    if (tLRPC$Chat2.left && !tLRPC$Chat2.kicked) {
                        int i87 = this.rowCount;
                        int i88 = i87 + 1;
                        this.rowCount = i88;
                        this.joinRow = i87;
                        this.rowCount = i88 + 1;
                        this.lastSectionRow = i88;
                    }
                }
            } else {
                TLRPC$ChatFull tLRPC$ChatFull8 = this.chatInfo;
                if (tLRPC$ChatFull8 != null) {
                    if (!this.isTopic && (tLRPC$ChatParticipants = tLRPC$ChatFull8.participants) != null && tLRPC$ChatParticipants.participants != null && !(tLRPC$ChatParticipants instanceof TLRPC$TL_chatParticipantsForbidden)) {
                        if (ChatObject.canAddUsers(this.currentChat) || (tLRPC$TL_chatBannedRights = this.currentChat.default_banned_rights) == null || !tLRPC$TL_chatBannedRights.invite_users) {
                            int i89 = this.rowCount;
                            this.rowCount = i89 + 1;
                            this.addMemberRow = i89;
                        }
                        if (this.chatInfo.participants.participants.size() <= 5 || !z) {
                            if (this.addMemberRow == -1) {
                                int i90 = this.rowCount;
                                this.rowCount = i90 + 1;
                                this.membersHeaderRow = i90;
                            }
                            int i91 = this.rowCount;
                            this.membersStartRow = i91;
                            int size2 = i91 + this.chatInfo.participants.participants.size();
                            this.rowCount = size2;
                            this.membersEndRow = size2;
                            this.rowCount = size2 + 1;
                            this.membersSectionRow = size2;
                            this.visibleChatParticipants.addAll(this.chatInfo.participants.participants);
                            ArrayList<Integer> arrayList3 = this.sortedUsers;
                            if (arrayList3 != null) {
                                this.visibleSortedUsers.addAll(arrayList3);
                            }
                            SharedMediaLayout sharedMediaLayout3 = this.sharedMediaLayout;
                            if (sharedMediaLayout3 != null) {
                                sharedMediaLayout3.setChatUsers(null, null);
                            }
                        } else {
                            if (this.addMemberRow != -1) {
                                int i92 = this.rowCount;
                                this.rowCount = i92 + 1;
                                this.membersSectionRow = i92;
                            }
                            SharedMediaLayout sharedMediaLayout4 = this.sharedMediaLayout;
                            if (sharedMediaLayout4 != null) {
                                sharedMediaLayout4.setChatUsers(this.sortedUsers, this.chatInfo);
                            }
                        }
                    } else {
                        if (!ChatObject.isNotInChat(this.currentChat) && ChatObject.canAddUsers(this.currentChat) && this.chatInfo.participants_hidden) {
                            int i93 = this.rowCount;
                            int i94 = i93 + 1;
                            this.rowCount = i94;
                            this.addMemberRow = i93;
                            this.rowCount = i94 + 1;
                            this.membersSectionRow = i94;
                        }
                        SharedMediaLayout sharedMediaLayout5 = this.sharedMediaLayout;
                        if (sharedMediaLayout5 != null) {
                            sharedMediaLayout5.updateAdapters();
                        }
                    }
                }
            }
            if (z) {
                int i95 = this.rowCount;
                this.rowCount = i95 + 1;
                this.sharedMediaRow = i95;
            }
        }
        if (this.sharedMediaRow == -1) {
            int i96 = this.rowCount;
            this.rowCount = i96 + 1;
            this.bottomPaddingRow = i96;
        }
        int currentActionBarHeight = this.actionBar != null ? ActionBar.getCurrentActionBarHeight() + (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) : 0;
        if (this.listView == null || i2 > this.rowCount || ((i = this.listContentHeight) != 0 && i + currentActionBarHeight + AndroidUtilities.dp(88.0f) < this.listView.getMeasuredHeight())) {
            this.lastMeasuredContentWidth = 0;
        }
    }

    private Drawable getScamDrawable(int i) {
        if (this.scamDrawable == null) {
            ScamDrawable scamDrawable = new ScamDrawable(11, i);
            this.scamDrawable = scamDrawable;
            scamDrawable.setColor(getThemedColor(Theme.key_avatar_subtitleInProfileBlue));
        }
        return this.scamDrawable;
    }

    private Drawable getLockIconDrawable() {
        if (this.lockIconDrawable == null) {
            this.lockIconDrawable = Theme.chat_lockIconDrawable.getConstantState().newDrawable().mutate();
        }
        return this.lockIconDrawable;
    }

    private Drawable getVerifiedCrossfadeDrawable(int i) {
        if (this.verifiedCrossfadeDrawable[i] == null) {
            this.verifiedDrawable[i] = Theme.profile_verifiedDrawable.getConstantState().newDrawable().mutate();
            this.verifiedCheckDrawable[i] = Theme.profile_verifiedCheckDrawable.getConstantState().newDrawable().mutate();
            this.verifiedCrossfadeDrawable[i] = new CrossfadeDrawable(new CombinedDrawable(this.verifiedDrawable[i], this.verifiedCheckDrawable[i]), ContextCompat.getDrawable(getParentActivity(), R.drawable.verified_profile));
        }
        return this.verifiedCrossfadeDrawable[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getPremiumCrossfadeDrawable(int i) {
        if (this.premiumCrossfadeDrawable[i] == null) {
            this.premiumStarDrawable[i] = ContextCompat.getDrawable(getParentActivity(), R.drawable.msg_premium_liststar).mutate();
            int themedColor = getThemedColor(Theme.key_profile_verifiedBackground);
            if (i == 1) {
                themedColor = applyPeerColor(themedColor);
            }
            this.premiumStarDrawable[i].setColorFilter(themedColor, PorterDuff.Mode.MULTIPLY);
            this.premiumCrossfadeDrawable[i] = new CrossfadeDrawable(this.premiumStarDrawable[i], ContextCompat.getDrawable(getParentActivity(), R.drawable.msg_premium_prolfilestar).mutate());
        }
        return this.premiumCrossfadeDrawable[i];
    }

    private Drawable getEmojiStatusDrawable(TLRPC$EmojiStatus tLRPC$EmojiStatus, boolean z, boolean z2, int i) {
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] swapAnimatedEmojiDrawableArr = this.emojiStatusDrawable;
        if (swapAnimatedEmojiDrawableArr[i] == null) {
            swapAnimatedEmojiDrawableArr[i] = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this.nameTextView[i], AndroidUtilities.dp(24.0f), i == 0 ? 7 : 2);
            if (this.fragmentViewAttached) {
                this.emojiStatusDrawable[i].attach();
            }
        }
        if (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatus) {
            this.emojiStatusDrawable[i].set(((TLRPC$TL_emojiStatus) tLRPC$EmojiStatus).document_id, z2);
        } else {
            if (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) {
                TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil = (TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus;
                if (tLRPC$TL_emojiStatusUntil.until > ((int) (System.currentTimeMillis() / 1000)) && !this.reportSpam) {
                    this.emojiStatusDrawable[i].set(tLRPC$TL_emojiStatusUntil.document_id, z2);
                }
            }
            this.emojiStatusDrawable[i].set(getPremiumCrossfadeDrawable(i), z2);
        }
        updateEmojiStatusDrawableColor();
        return this.emojiStatusDrawable[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmojiStatusDrawableColor() {
        updateEmojiStatusDrawableColor(this.lastEmojiStatusProgress);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmojiStatusDrawableColor(float f) {
        int offsetColor;
        for (int i = 0; i < 2; i++) {
            MessagesController.PeerColor peerColor = this.peerColor;
            if (peerColor != null && i == 1) {
                offsetColor = ColorUtils.blendARGB(peerColor.getColor2(), this.peerColor.hasColor6(Theme.isCurrentThemeDark()) ? this.peerColor.getColor5() : this.peerColor.getColor3(), 0.5f);
            } else {
                offsetColor = AndroidUtilities.getOffsetColor(getThemedColor(Theme.key_profile_verifiedBackground), getThemedColor(Theme.key_player_actionBarTitle), this.mediaHeaderAnimationProgress, 1.0f);
            }
            int blendARGB = ColorUtils.blendARGB(offsetColor, -1, f);
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] swapAnimatedEmojiDrawableArr = this.emojiStatusDrawable;
            if (swapAnimatedEmojiDrawableArr[i] != null) {
                swapAnimatedEmojiDrawableArr[i].setColor(Integer.valueOf(blendARGB));
            }
            if (i == 1) {
                this.animatedStatusView.setColor(blendARGB);
            }
        }
        this.lastEmojiStatusProgress = f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmojiStatusEffectPosition() {
        this.animatedStatusView.setScaleX(this.nameTextView[1].getScaleX());
        this.animatedStatusView.setScaleY(this.nameTextView[1].getScaleY());
        this.animatedStatusView.translate(this.nameTextView[1].getX() + (this.nameTextView[1].getRightDrawableX() * this.nameTextView[1].getScaleX()), this.nameTextView[1].getY() + (this.nameTextView[1].getHeight() - ((this.nameTextView[1].getHeight() - this.nameTextView[1].getRightDrawableY()) * this.nameTextView[1].getScaleY())));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(46:16|(1:272)(1:20)|(1:22)(1:271)|23|(1:25)(1:270)|26|(1:28)|29|(1:31)|32|(4:34|(1:268)(1:38)|39|(31:41|42|(1:44)|45|(1:(1:48)(1:(1:53)(1:52)))|54|(2:58|59)|(1:65)|66|(2:68|(3:70|(1:72)|73)(1:233))(2:234|(1:263)(2:240|(1:242)(2:243|(1:245)(4:246|(1:248)(1:262)|249|(4:253|(1:261)(1:257)|258|(1:260))))))|74|(1:232)(1:78)|79|80|81|82|(4:85|(11:87|(2:91|(8:105|(1:188)(1:108)|109|(1:111)(1:187)|112|(1:114)(1:186)|115|(10:117|(1:165)(2:121|(1:123)(2:145|(9:163|164|125|126|(1:130)|131|(1:135)|136|(2:140|141))(2:151|(8:153|154|126|(1:130)|131|(2:133|135)|136|(1:144)(3:138|140|141))(4:155|(1:157)|158|(1:160)(2:161|162)))))|124|125|126|(0)|131|(0)|136|(0)(0))(11:166|(10:168|(1:185)(2:172|(1:174)(2:175|(9:184|164|125|126|(0)|131|(0)|136|(0)(0))(2:181|(8:183|154|126|(0)|131|(0)|136|(0)(0)))))|124|125|126|(0)|131|(0)|136|(0)(0))|162|124|125|126|(0)|131|(0)|136|(0)(0))))|189|(0)|188|109|(0)(0)|112|(0)(0)|115|(0)(0))(1:190)|142|83)|191|192|(1:194)(2:222|(10:229|196|(1:198)|199|(1:201)|202|(1:221)(1:210)|211|(1:213)(1:220)|214)(1:228))|195|196|(0)|199|(0)|202|(3:204|206|208)|221|211|(0)(0)|214))(1:269)|267|42|(0)|45|(0)|54|(28:56|58|59|(2:61|65)|66|(0)(0)|74|(1:76)|232|79|80|81|82|(1:83)|191|192|(0)(0)|195|196|(0)|199|(0)|202|(0)|221|211|(0)(0)|214)|265|58|59|(0)|66|(0)(0)|74|(0)|232|79|80|81|82|(1:83)|191|192|(0)(0)|195|196|(0)|199|(0)|202|(0)|221|211|(0)(0)|214) */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0619, code lost:
        if (r36.chatInfo.can_view_participants != false) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x0844, code lost:
        if (r36.nameTextView[r10].setText(r1) != false) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x086e, code lost:
        if (r36.nameTextView[r10].setText(r1) != false) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x0870, code lost:
        r14 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x028c  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0338  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x033e  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x03df  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0441 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0450  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0497  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0557  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0566  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0581  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x0583  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x07f1  */
    /* JADX WARN: Removed duplicated region for block: B:462:0x0981  */
    /* JADX WARN: Removed duplicated region for block: B:463:0x098e  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x099e  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x0a16  */
    /* JADX WARN: Removed duplicated region for block: B:479:0x0a55  */
    /* JADX WARN: Removed duplicated region for block: B:480:0x0a58  */
    /* JADX WARN: Removed duplicated region for block: B:483:0x0a61  */
    /* JADX WARN: Removed duplicated region for block: B:486:0x0a67  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0a74  */
    /* JADX WARN: Removed duplicated region for block: B:491:0x0aa3  */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0aab  */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0ab7  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0ac5  */
    /* JADX WARN: Removed duplicated region for block: B:504:0x0ae8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:509:0x0af7  */
    /* JADX WARN: Removed duplicated region for block: B:512:0x0b0d  */
    /* JADX WARN: Removed duplicated region for block: B:518:0x0b1b  */
    /* JADX WARN: Removed duplicated region for block: B:522:0x0b30  */
    /* JADX WARN: Removed duplicated region for block: B:529:0x0b59  */
    /* JADX WARN: Removed duplicated region for block: B:539:0x0b74  */
    /* JADX WARN: Removed duplicated region for block: B:540:0x0b76  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x0484 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:567:0x0a84 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0153  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01d3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateProfileData(boolean z) {
        String string;
        String str;
        String str2;
        String formatPluralString;
        TLRPC$ChatParticipants tLRPC$ChatParticipants;
        SpannableStringBuilder spannableStringBuilder;
        boolean z2;
        int i;
        boolean z3;
        int i2;
        ImageLocation forUserOrChat;
        ImageLocation forUserOrChat2;
        ImageLocation currentVideoLocation;
        View findViewByPosition;
        String str3;
        StoryViewer storyViewer;
        ImageLocation imageLocation;
        String str4;
        String str5;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        String str6;
        String str7;
        String str8;
        String str9;
        TLRPC$Chat tLRPC$Chat;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$Chat tLRPC$Chat2;
        String formatShortNumber;
        int i3;
        int i4;
        int i5;
        int i6;
        String formatString;
        TLRPC$VideoSize tLRPC$VideoSize;
        VectorAvatarThumbDrawable vectorAvatarThumbDrawable;
        String string2;
        int i7;
        char c;
        int i8;
        ChatActivityInterface chatActivityInterface;
        StoryViewer storyViewer2;
        Drawable scamDrawable;
        Drawable emojiStatusDrawable;
        boolean z4;
        boolean z5;
        String str15;
        ImageLocation imageLocation2;
        if (this.avatarContainer == null || this.nameTextView == null || getParentActivity() == null) {
            return;
        }
        int connectionState = getConnectionsManager().getConnectionState();
        if (connectionState == 2) {
            string = LocaleController.getString("WaitingForNetwork", R.string.WaitingForNetwork);
        } else if (connectionState == 1) {
            string = LocaleController.getString("Connecting", R.string.Connecting);
        } else if (connectionState == 5) {
            string = LocaleController.getString("Updating", R.string.Updating);
        } else {
            string = connectionState == 4 ? LocaleController.getString("ConnectingToProxy", R.string.ConnectingToProxy) : null;
        }
        this.hasFallbackPhoto = false;
        this.hasCustomPhoto = false;
        String str16 = "AccDescrVerified";
        String str17 = "ScamMessage";
        if (this.userId != 0) {
            final TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user == null) {
                return;
            }
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = user.photo;
            boolean z6 = tLRPC$UserProfilePhoto != null && tLRPC$UserProfilePhoto.personal;
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$UserProfilePhoto != null ? tLRPC$UserProfilePhoto.photo_big : null;
            this.avatarDrawable.setInfo(this.currentAccount, user);
            int profileColorId = UserObject.getProfileColorId(user);
            MessagesController.PeerColors peerColors = MessagesController.getInstance(this.currentAccount).profilePeerColors;
            MessagesController.PeerColor peerColor = this.peerColor;
            MessagesController.PeerColor color = peerColors == null ? null : peerColors.getColor(profileColorId);
            this.peerColor = color;
            if (peerColor != color) {
                updatedPeerColor();
            }
            TopView topView = this.topView;
            TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$FileLocation;
            if (topView != null) {
                topView.setBackgroundEmojiId(UserObject.getProfileEmojiId(user), true);
            }
            ImageLocation forUserOrChat3 = ImageLocation.getForUserOrChat(user, 0);
            ImageLocation forUserOrChat4 = ImageLocation.getForUserOrChat(user, 1);
            ImageLocation forUserOrChat5 = ImageLocation.getForUserOrChat(user, 4);
            TLRPC$UserFull tLRPC$UserFull = this.userInfo;
            if (tLRPC$UserFull != null) {
                TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2 = user.photo;
                tLRPC$VideoSize = FileLoader.getVectorMarkupVideoSize((tLRPC$UserProfilePhoto2 == null || !tLRPC$UserProfilePhoto2.personal) ? tLRPC$UserFull.profile_photo : tLRPC$UserFull.personal_photo);
                if (tLRPC$VideoSize != null) {
                    vectorAvatarThumbDrawable = new VectorAvatarThumbDrawable(tLRPC$VideoSize, user.premium, 2);
                    ImageLocation currentVideoLocation2 = this.avatarsViewPager.getCurrentVideoLocation(forUserOrChat4, forUserOrChat3);
                    if (this.avatar == null) {
                        this.avatarsViewPager.initIfEmpty(vectorAvatarThumbDrawable, forUserOrChat3, forUserOrChat4, z);
                    }
                    if (this.avatarBig == null) {
                        if (tLRPC$VideoSize != null) {
                            this.avatarImage.setImageDrawable(vectorAvatarThumbDrawable);
                        } else if (forUserOrChat5 != null && !user.photo.personal) {
                            this.avatarImage.getImageReceiver().setVideoThumbIsSame(true);
                            this.avatarImage.setImage(forUserOrChat5, "avatar", forUserOrChat4, "50_50", this.avatarDrawable, user);
                        } else {
                            this.avatarImage.setImage(currentVideoLocation2, ImageLoader.AUTOPLAY_FILTER, forUserOrChat4, "50_50", this.avatarDrawable, user);
                        }
                    }
                    if ((forUserOrChat4 != null && this.setAvatarRow != -1) || (forUserOrChat4 == null && this.setAvatarRow == -1)) {
                        updateListAnimated(false);
                        needLayout(true);
                    }
                    if (forUserOrChat3 != null && ((imageLocation2 = this.prevLoadedImageLocation) == null || forUserOrChat3.photoId != imageLocation2.photoId)) {
                        this.prevLoadedImageLocation = forUserOrChat3;
                        getFileLoader().loadFile(forUserOrChat3, user, null, 0, 1);
                    }
                    CharSequence userName = UserObject.getUserName(user);
                    if (user.id == getUserConfig().getClientUserId()) {
                        if (UserObject.hasFallbackPhoto(getUserInfo())) {
                            this.hasFallbackPhoto = true;
                            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(getUserInfo().fallback_photo.sizes, 1000);
                            if (closestPhotoSizeWithSize != null) {
                                this.fallbackImage.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, getUserInfo().fallback_photo), "50_50", (Drawable) null, 0L, (String) null, UserConfig.getInstance(this.currentAccount).getCurrentUser(), 0);
                            }
                            string2 = "";
                        } else {
                            string2 = LocaleController.getString("Online", R.string.Online);
                        }
                    } else {
                        long j = user.id;
                        if (j == 333000 || j == 777000 || j == 42777) {
                            string2 = LocaleController.getString("ServiceNotifications", R.string.ServiceNotifications);
                        } else if (MessagesController.isSupportUser(user)) {
                            string2 = LocaleController.getString("SupportStatus", R.string.SupportStatus);
                        } else if (this.isBot) {
                            string2 = LocaleController.getString("Bot", R.string.Bot);
                        } else {
                            boolean[] zArr = this.isOnline;
                            zArr[0] = false;
                            string2 = LocaleController.formatUserStatus(this.currentAccount, user, zArr, z6 ? new boolean[1] : null);
                            SimpleTextView[] simpleTextViewArr = this.onlineTextView;
                            if (simpleTextViewArr[1] != null && !this.mediaHeaderVisible) {
                                int i9 = (this.isOnline[0] && this.peerColor == null) ? Theme.key_profile_status : Theme.key_avatar_subtitleInProfileBlue;
                                simpleTextViewArr[1].setTag(Integer.valueOf(i9));
                                if (!this.isPulledDown) {
                                    this.onlineTextView[1].setTextColor(applyPeerColor(getThemedColor(i9), true, Boolean.valueOf(this.isOnline[0])));
                                }
                            }
                        }
                    }
                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3 = user.photo;
                    this.hasCustomPhoto = tLRPC$UserProfilePhoto3 == null && tLRPC$UserProfilePhoto3.personal;
                    userName = Emoji.replaceEmoji(userName, this.nameTextView[1].getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
                    i7 = 0;
                    while (i7 < 2) {
                        if (this.nameTextView[i7] != null) {
                            if (i7 == 0 && user.id != getUserConfig().getClientUserId()) {
                                long j2 = user.id;
                                if (j2 / 1000 != 777 && j2 / 1000 != 333 && (str15 = user.phone) != null && str15.length() != 0 && getContactsController().contactsDict.get(Long.valueOf(user.id)) == null && (getContactsController().contactsDict.size() != 0 || !getContactsController().isLoadingContacts())) {
                                    PhoneFormat phoneFormat = PhoneFormat.getInstance();
                                    this.nameTextView[i7].setText(phoneFormat.format("+" + user.phone));
                                    if (i7 != 0 && string != null) {
                                        this.onlineTextView[i7].setText(string);
                                    } else {
                                        this.onlineTextView[i7].setText(string2);
                                    }
                                    Drawable lockIconDrawable = this.currentEncryptedChat == null ? getLockIconDrawable() : null;
                                    this.nameTextView[i7].setRightDrawableOutside(i7 != 0);
                                    if (i7 != 0) {
                                        boolean z7 = user.scam;
                                        if (z7 || user.fake) {
                                            scamDrawable = getScamDrawable(!z7 ? 1 : 0);
                                            this.nameTextViewRightDrawableContentDescription = LocaleController.getString("ScamMessage", R.string.ScamMessage);
                                        } else if (user.verified) {
                                            scamDrawable = getVerifiedCrossfadeDrawable(i7);
                                            this.nameTextViewRightDrawableContentDescription = LocaleController.getString("AccDescrVerified", R.string.AccDescrVerified);
                                        } else {
                                            TLRPC$EmojiStatus tLRPC$EmojiStatus = user.emoji_status;
                                            if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatus) || ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000)))) {
                                                scamDrawable = getEmojiStatusDrawable(user.emoji_status, false, false, i7);
                                                this.nameTextViewRightDrawableContentDescription = LocaleController.getString("AccDescrPremium", R.string.AccDescrPremium);
                                                z4 = true;
                                                z5 = false;
                                                this.nameTextView[i7].setLeftDrawable(lockIconDrawable);
                                                this.nameTextView[i7].setRightDrawable(scamDrawable);
                                                if (i7 == 1 && (z4 || z5)) {
                                                    this.nameTextView[i7].setRightDrawableOutside(true);
                                                }
                                                if (user.self && getMessagesController().isPremiumUser(user)) {
                                                    SimpleTextView[] simpleTextViewArr2 = this.nameTextView;
                                                    SimpleTextView simpleTextView = simpleTextViewArr2[i7];
                                                    simpleTextViewArr2[i7].setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda14
                                                        @Override // android.view.View.OnClickListener
                                                        public final void onClick(View view) {
                                                            ProfileActivity.this.lambda$updateProfileData$53(view);
                                                        }
                                                    });
                                                }
                                                if (user.self && getMessagesController().isPremiumUser(user)) {
                                                    SimpleTextView[] simpleTextViewArr3 = this.nameTextView;
                                                    final SimpleTextView simpleTextView2 = simpleTextViewArr3[i7];
                                                    simpleTextViewArr3[i7].setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda19
                                                        @Override // android.view.View.OnClickListener
                                                        public final void onClick(View view) {
                                                            ProfileActivity.this.lambda$updateProfileData$54(user, simpleTextView2, view);
                                                        }
                                                    });
                                                }
                                            } else if (getMessagesController().isPremiumUser(user)) {
                                                emojiStatusDrawable = getEmojiStatusDrawable(null, false, false, i7);
                                                this.nameTextViewRightDrawableContentDescription = LocaleController.getString("AccDescrPremium", R.string.AccDescrPremium);
                                                scamDrawable = emojiStatusDrawable;
                                                z4 = false;
                                                z5 = true;
                                                this.nameTextView[i7].setLeftDrawable(lockIconDrawable);
                                                this.nameTextView[i7].setRightDrawable(scamDrawable);
                                                if (i7 == 1) {
                                                    this.nameTextView[i7].setRightDrawableOutside(true);
                                                }
                                                if (user.self) {
                                                    SimpleTextView[] simpleTextViewArr22 = this.nameTextView;
                                                    SimpleTextView simpleTextView3 = simpleTextViewArr22[i7];
                                                    simpleTextViewArr22[i7].setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda14
                                                        @Override // android.view.View.OnClickListener
                                                        public final void onClick(View view) {
                                                            ProfileActivity.this.lambda$updateProfileData$53(view);
                                                        }
                                                    });
                                                }
                                                if (user.self) {
                                                    SimpleTextView[] simpleTextViewArr32 = this.nameTextView;
                                                    final SimpleTextView simpleTextView22 = simpleTextViewArr32[i7];
                                                    simpleTextViewArr32[i7].setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda19
                                                        @Override // android.view.View.OnClickListener
                                                        public final void onClick(View view) {
                                                            ProfileActivity.this.lambda$updateProfileData$54(user, simpleTextView22, view);
                                                        }
                                                    });
                                                }
                                            } else {
                                                MessagesController messagesController = getMessagesController();
                                                long j3 = this.dialogId;
                                                if (j3 == 0) {
                                                    j3 = this.userId;
                                                }
                                                if (messagesController.isDialogMuted(j3, this.topicId)) {
                                                    scamDrawable = getThemedDrawable("drawableMuteIcon");
                                                    this.nameTextViewRightDrawableContentDescription = LocaleController.getString("NotificationsMuted", R.string.NotificationsMuted);
                                                } else {
                                                    this.nameTextViewRightDrawableContentDescription = null;
                                                    scamDrawable = null;
                                                }
                                            }
                                        }
                                        z4 = false;
                                        z5 = false;
                                        this.nameTextView[i7].setLeftDrawable(lockIconDrawable);
                                        this.nameTextView[i7].setRightDrawable(scamDrawable);
                                        if (i7 == 1) {
                                        }
                                        if (user.self) {
                                        }
                                        if (user.self) {
                                        }
                                    } else {
                                        if (i7 == 1) {
                                            boolean z8 = user.scam;
                                            if (z8 || user.fake) {
                                                scamDrawable = getScamDrawable(!z8 ? 1 : 0);
                                            } else if (user.verified) {
                                                scamDrawable = getVerifiedCrossfadeDrawable(i7);
                                            } else {
                                                TLRPC$EmojiStatus tLRPC$EmojiStatus2 = user.emoji_status;
                                                if ((tLRPC$EmojiStatus2 instanceof TLRPC$TL_emojiStatus) || ((tLRPC$EmojiStatus2 instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus2).until > ((int) (System.currentTimeMillis() / 1000)))) {
                                                    scamDrawable = getEmojiStatusDrawable(user.emoji_status, true, true, i7);
                                                    z4 = true;
                                                    z5 = false;
                                                    this.nameTextView[i7].setLeftDrawable(lockIconDrawable);
                                                    this.nameTextView[i7].setRightDrawable(scamDrawable);
                                                    if (i7 == 1) {
                                                    }
                                                    if (user.self) {
                                                    }
                                                    if (user.self) {
                                                    }
                                                } else if (getMessagesController().isPremiumUser(user)) {
                                                    emojiStatusDrawable = getEmojiStatusDrawable(null, true, true, i7);
                                                    scamDrawable = emojiStatusDrawable;
                                                    z4 = false;
                                                    z5 = true;
                                                    this.nameTextView[i7].setLeftDrawable(lockIconDrawable);
                                                    this.nameTextView[i7].setRightDrawable(scamDrawable);
                                                    if (i7 == 1) {
                                                    }
                                                    if (user.self) {
                                                    }
                                                    if (user.self) {
                                                    }
                                                }
                                            }
                                            z4 = false;
                                            z5 = false;
                                            this.nameTextView[i7].setLeftDrawable(lockIconDrawable);
                                            this.nameTextView[i7].setRightDrawable(scamDrawable);
                                            if (i7 == 1) {
                                            }
                                            if (user.self) {
                                            }
                                            if (user.self) {
                                            }
                                        }
                                        scamDrawable = null;
                                        z4 = false;
                                        z5 = false;
                                        this.nameTextView[i7].setLeftDrawable(lockIconDrawable);
                                        this.nameTextView[i7].setRightDrawable(scamDrawable);
                                        if (i7 == 1) {
                                        }
                                        if (user.self) {
                                        }
                                        if (user.self) {
                                        }
                                    }
                                }
                            }
                            this.nameTextView[i7].setText(userName);
                            if (i7 != 0) {
                            }
                            this.onlineTextView[i7].setText(string2);
                            if (this.currentEncryptedChat == null) {
                            }
                            this.nameTextView[i7].setRightDrawableOutside(i7 != 0);
                            if (i7 != 0) {
                            }
                        }
                        i7++;
                    }
                    if (this.userId == UserConfig.getInstance(this.currentAccount).clientUserId) {
                        this.onlineTextView[2].setText(LocaleController.getString("FallbackTooltip", R.string.FallbackTooltip));
                        this.onlineTextView[3].setText(LocaleController.getString("Online", R.string.Online));
                    } else {
                        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = user.photo;
                        if (tLRPC$UserProfilePhoto4 != null && tLRPC$UserProfilePhoto4.personal && tLRPC$UserProfilePhoto4.has_video) {
                            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(string2);
                            spannableStringBuilder2.setSpan(new EmptyStubSpan(), 0, string2.length(), 0);
                            spannableStringBuilder2.append((CharSequence) " d ");
                            spannableStringBuilder2.append((CharSequence) LocaleController.getString("CustomAvatarTooltipVideo", R.string.CustomAvatarTooltipVideo));
                            spannableStringBuilder2.setSpan(new DotDividerSpan(), string2.length() + 1, string2.length() + 2, 0);
                            this.onlineTextView[2].setText(spannableStringBuilder2);
                        } else {
                            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(string2);
                            spannableStringBuilder3.setSpan(new EmptyStubSpan(), 0, string2.length(), 0);
                            spannableStringBuilder3.append((CharSequence) " d ");
                            spannableStringBuilder3.append((CharSequence) LocaleController.getString("CustomAvatarTooltip", R.string.CustomAvatarTooltip));
                            c = 2;
                            i8 = 0;
                            spannableStringBuilder3.setSpan(new DotDividerSpan(), string2.length() + 1, string2.length() + 2, 0);
                            this.onlineTextView[2].setText(spannableStringBuilder3);
                            this.onlineTextView[c].setVisibility(i8);
                            if (!this.searchMode) {
                                this.onlineTextView[3].setVisibility(i8);
                            }
                            chatActivityInterface = this.previousTransitionFragment;
                            if (chatActivityInterface != null) {
                                chatActivityInterface.checkAndUpdateAvatar();
                            }
                            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation2) && !((storyViewer2 = this.storyViewer) != null && storyViewer2.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
                        }
                    }
                    c = 2;
                    i8 = 0;
                    this.onlineTextView[c].setVisibility(i8);
                    if (!this.searchMode) {
                    }
                    chatActivityInterface = this.previousTransitionFragment;
                    if (chatActivityInterface != null) {
                    }
                    this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation2) && !((storyViewer2 = this.storyViewer) != null && storyViewer2.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
                }
            } else {
                tLRPC$VideoSize = null;
            }
            vectorAvatarThumbDrawable = null;
            ImageLocation currentVideoLocation22 = this.avatarsViewPager.getCurrentVideoLocation(forUserOrChat4, forUserOrChat3);
            if (this.avatar == null) {
            }
            if (this.avatarBig == null) {
            }
            if (forUserOrChat4 != null) {
                updateListAnimated(false);
                needLayout(true);
                if (forUserOrChat3 != null) {
                    this.prevLoadedImageLocation = forUserOrChat3;
                    getFileLoader().loadFile(forUserOrChat3, user, null, 0, 1);
                }
                CharSequence userName2 = UserObject.getUserName(user);
                if (user.id == getUserConfig().getClientUserId()) {
                }
                TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto32 = user.photo;
                this.hasCustomPhoto = tLRPC$UserProfilePhoto32 == null && tLRPC$UserProfilePhoto32.personal;
                userName2 = Emoji.replaceEmoji(userName2, this.nameTextView[1].getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
                i7 = 0;
                while (i7 < 2) {
                }
                if (this.userId == UserConfig.getInstance(this.currentAccount).clientUserId) {
                }
                c = 2;
                i8 = 0;
                this.onlineTextView[c].setVisibility(i8);
                if (!this.searchMode) {
                }
                chatActivityInterface = this.previousTransitionFragment;
                if (chatActivityInterface != null) {
                }
                this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation2) && !((storyViewer2 = this.storyViewer) != null && storyViewer2.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
            }
            updateListAnimated(false);
            needLayout(true);
            if (forUserOrChat3 != null) {
            }
            CharSequence userName22 = UserObject.getUserName(user);
            if (user.id == getUserConfig().getClientUserId()) {
            }
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto322 = user.photo;
            this.hasCustomPhoto = tLRPC$UserProfilePhoto322 == null && tLRPC$UserProfilePhoto322.personal;
            userName22 = Emoji.replaceEmoji(userName22, this.nameTextView[1].getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
            i7 = 0;
            while (i7 < 2) {
            }
            if (this.userId == UserConfig.getInstance(this.currentAccount).clientUserId) {
            }
            c = 2;
            i8 = 0;
            this.onlineTextView[c].setVisibility(i8);
            if (!this.searchMode) {
            }
            chatActivityInterface = this.previousTransitionFragment;
            if (chatActivityInterface != null) {
            }
            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation2) && !((storyViewer2 = this.storyViewer) != null && storyViewer2.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
        } else if (this.chatId != 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            if (chat != null) {
                this.currentChat = chat;
            } else {
                chat = this.currentChat;
            }
            FlagSecureReason flagSecureReason = this.flagSecure;
            if (flagSecureReason != null) {
                flagSecureReason.invalidate();
            }
            ChatObject.getProfileColorId(chat);
            MessagesController.PeerColors peerColors2 = MessagesController.getInstance(this.currentAccount).profilePeerColors;
            MessagesController.PeerColor peerColor2 = this.peerColor;
            this.peerColor = null;
            if (peerColor2 != null) {
                updatedPeerColor();
            }
            TopView topView2 = this.topView;
            if (topView2 != null) {
                topView2.setBackgroundEmojiId(ChatObject.getProfileEmojiId(chat), true);
            }
            TLRPC$TL_forumTopic findTopic = this.isTopic ? getMessagesController().getTopicsController().findTopic(this.chatId, this.topicId) : null;
            String str18 = "MegaPublic";
            String str19 = "MegaPrivate";
            String str20 = "Members";
            if (ChatObject.isChannel(chat)) {
                if (this.isTopic) {
                    str = string;
                } else {
                    TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
                    if (tLRPC$ChatFull2 != null) {
                        TLRPC$Chat tLRPC$Chat3 = this.currentChat;
                        str = string;
                        if (!tLRPC$Chat3.megagroup) {
                            if (tLRPC$ChatFull2.participants_count != 0) {
                                if (!ChatObject.hasAdminRights(tLRPC$Chat3)) {
                                }
                            }
                        }
                    } else {
                        str = string;
                    }
                    if (this.currentChat.megagroup) {
                        formatPluralString = LocaleController.getString("Loading", R.string.Loading).toLowerCase();
                    } else if (ChatObject.isPublic(chat)) {
                        formatPluralString = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                    } else {
                        formatPluralString = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                    }
                    spannableStringBuilder = formatPluralString;
                    str2 = "drawableMuteIcon";
                    z2 = false;
                    i = 0;
                    z3 = false;
                    i2 = 2;
                    while (i < i2) {
                        SimpleTextView[] simpleTextViewArr4 = this.nameTextView;
                        if (simpleTextViewArr4[i] == null) {
                            str4 = formatPluralString;
                            str5 = spannableStringBuilder;
                            tLRPC$TL_forumTopic = findTopic;
                            str12 = str20;
                            str13 = str18;
                            str14 = str19;
                            String str21 = str;
                            str10 = str16;
                            str8 = str2;
                            str11 = str17;
                            str9 = str21;
                        } else {
                            str4 = formatPluralString;
                            if (this.isTopic) {
                                String str22 = findTopic == null ? "" : findTopic.title;
                                try {
                                    str5 = spannableStringBuilder;
                                    try {
                                        tLRPC$TL_forumTopic = findTopic;
                                        try {
                                            str22 = Emoji.replaceEmoji(str22, simpleTextViewArr4[i].getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
                                        } catch (Exception unused) {
                                        }
                                    } catch (Exception unused2) {
                                        tLRPC$TL_forumTopic = findTopic;
                                    }
                                } catch (Exception unused3) {
                                    str5 = spannableStringBuilder;
                                }
                            } else {
                                str5 = spannableStringBuilder;
                                tLRPC$TL_forumTopic = findTopic;
                                CharSequence charSequence = chat.title;
                                if (charSequence != null) {
                                    try {
                                        charSequence = Emoji.replaceEmoji(charSequence, simpleTextViewArr4[i].getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
                                    } catch (Exception unused4) {
                                    }
                                }
                                this.nameTextView[i].setLeftDrawable((Drawable) null);
                                this.nameTextView[i].setRightDrawableOutside(i == 0);
                                if (i != 0) {
                                    boolean z9 = chat.scam;
                                    if (z9 || chat.fake) {
                                        this.nameTextView[i].setRightDrawable(getScamDrawable(!z9 ? 1 : 0));
                                        this.nameTextViewRightDrawableContentDescription = LocaleController.getString(str17, R.string.ScamMessage);
                                    } else if (chat.verified) {
                                        this.nameTextView[i].setRightDrawable(getVerifiedCrossfadeDrawable(i));
                                        this.nameTextViewRightDrawableContentDescription = LocaleController.getString(str16, R.string.AccDescrVerified);
                                    } else {
                                        this.nameTextView[i].setRightDrawable((Drawable) null);
                                        this.nameTextViewRightDrawableContentDescription = null;
                                    }
                                } else {
                                    boolean z10 = chat.scam;
                                    if (z10 || chat.fake) {
                                        str6 = str16;
                                        str7 = str17;
                                        str8 = str2;
                                        this.nameTextView[i].setRightDrawable(getScamDrawable(!z10 ? 1 : 0));
                                    } else if (chat.verified) {
                                        this.nameTextView[i].setRightDrawable(getVerifiedCrossfadeDrawable(i));
                                    } else {
                                        str6 = str16;
                                        str7 = str17;
                                        if (getMessagesController().isDialogMuted(-this.chatId, this.topicId)) {
                                            str8 = str2;
                                            this.nameTextView[i].setRightDrawable(getThemedDrawable(str8));
                                        } else {
                                            str8 = str2;
                                            this.nameTextView[i].setRightDrawable((Drawable) null);
                                        }
                                    }
                                    if (i != 0 && str != null) {
                                        str9 = str;
                                        this.onlineTextView[i].setText(str9);
                                    } else {
                                        str9 = str;
                                        tLRPC$Chat = this.currentChat;
                                        if ((!tLRPC$Chat.megagroup && this.chatInfo != null && this.onlineCount > 0) || this.isTopic) {
                                            this.onlineTextView[i].setText(i == 0 ? str4 : str5);
                                        } else {
                                            if (i == 0 && ChatObject.isChannel(tLRPC$Chat) && (tLRPC$ChatFull = this.chatInfo) != null && tLRPC$ChatFull.participants_count != 0) {
                                                tLRPC$Chat2 = this.currentChat;
                                                if (!tLRPC$Chat2.megagroup || tLRPC$Chat2.broadcast) {
                                                    int[] iArr = new int[1];
                                                    if (AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                                                        formatShortNumber = LocaleController.formatShortNumber(this.chatInfo.participants_count, iArr);
                                                    } else {
                                                        int i10 = this.chatInfo.participants_count;
                                                        iArr[0] = i10;
                                                        formatShortNumber = String.valueOf(i10);
                                                    }
                                                    str10 = str6;
                                                    if (!this.currentChat.megagroup) {
                                                        if (this.chatInfo.participants_count == 0) {
                                                            if (chat.has_geo) {
                                                                this.onlineTextView[i].setText(LocaleController.getString("MegaLocation", R.string.MegaLocation).toLowerCase());
                                                            } else if (ChatObject.isPublic(chat)) {
                                                                this.onlineTextView[i].setText(LocaleController.getString(str18, R.string.MegaPublic).toLowerCase());
                                                            } else {
                                                                this.onlineTextView[i].setText(LocaleController.getString(str19, R.string.MegaPrivate).toLowerCase());
                                                            }
                                                            str11 = str7;
                                                            str12 = str20;
                                                            str13 = str18;
                                                            str14 = str19;
                                                        } else {
                                                            str11 = str7;
                                                            str13 = str18;
                                                            str14 = str19;
                                                            str12 = str20;
                                                            this.onlineTextView[i].setText(LocaleController.formatPluralString(str20, iArr[0], new Object[0]).replace(String.format("%d", Integer.valueOf(iArr[0])), formatShortNumber));
                                                        }
                                                    } else {
                                                        str11 = str7;
                                                        str12 = str20;
                                                        str13 = str18;
                                                        str14 = str19;
                                                        this.onlineTextView[i].setText(LocaleController.formatPluralString("Subscribers", iArr[0], new Object[0]).replace(String.format("%d", Integer.valueOf(iArr[0])), formatShortNumber));
                                                    }
                                                    if (i != 1 && this.isTopic) {
                                                        if (!z2) {
                                                            this.onlineTextView[i].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda10
                                                                @Override // android.view.View.OnClickListener
                                                                public final void onClick(View view) {
                                                                    ProfileActivity.this.lambda$updateProfileData$55(view);
                                                                }
                                                            });
                                                        } else {
                                                            this.onlineTextView[i].setOnClickListener(null);
                                                            this.onlineTextView[i].setClickable(false);
                                                        }
                                                    }
                                                }
                                            }
                                            str10 = str6;
                                            str11 = str7;
                                            str12 = str20;
                                            str13 = str18;
                                            str14 = str19;
                                            this.onlineTextView[i].setText(i != 0 ? str4 : str5);
                                            if (i != 1) {
                                                if (!z2) {
                                                }
                                            }
                                        }
                                    }
                                    str10 = str6;
                                    str11 = str7;
                                    str12 = str20;
                                    str13 = str18;
                                    str14 = str19;
                                    if (i != 1) {
                                    }
                                }
                                str6 = str16;
                                str7 = str17;
                                str8 = str2;
                                if (i != 0) {
                                }
                                str9 = str;
                                tLRPC$Chat = this.currentChat;
                                if (!tLRPC$Chat.megagroup) {
                                }
                                if (i == 0) {
                                    tLRPC$Chat2 = this.currentChat;
                                    if (!tLRPC$Chat2.megagroup) {
                                    }
                                    int[] iArr2 = new int[1];
                                    if (AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                                    }
                                    str10 = str6;
                                    if (!this.currentChat.megagroup) {
                                    }
                                    if (i != 1) {
                                    }
                                }
                                str10 = str6;
                                str11 = str7;
                                str12 = str20;
                                str13 = str18;
                                str14 = str19;
                                this.onlineTextView[i].setText(i != 0 ? str4 : str5);
                                if (i != 1) {
                                }
                            }
                        }
                        i++;
                        formatPluralString = str4;
                        spannableStringBuilder = str5;
                        findTopic = tLRPC$TL_forumTopic;
                        str18 = str13;
                        str19 = str14;
                        str20 = str12;
                        i2 = 2;
                        String str23 = str11;
                        str2 = str8;
                        str16 = str10;
                        str = str9;
                        str17 = str23;
                    }
                    TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = findTopic;
                    if (z3) {
                        needLayout(true);
                    }
                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                    TLRPC$FileLocation tLRPC$FileLocation3 = (tLRPC$ChatPhoto != null || this.isTopic) ? null : tLRPC$ChatPhoto.photo_big;
                    if (!this.isTopic) {
                        ForumUtilities.setTopicIcon(this.avatarImage, tLRPC$TL_forumTopic2, true, true, this.resourcesProvider);
                        forUserOrChat2 = null;
                        forUserOrChat = null;
                        currentVideoLocation = null;
                    } else {
                        this.avatarDrawable.setInfo(this.currentAccount, chat);
                        forUserOrChat = ImageLocation.getForUserOrChat(chat, 0);
                        forUserOrChat2 = ImageLocation.getForUserOrChat(chat, 1);
                        currentVideoLocation = this.avatarsViewPager.getCurrentVideoLocation(forUserOrChat2, forUserOrChat);
                    }
                    boolean initIfEmpty = this.avatarsViewPager.initIfEmpty(null, forUserOrChat, forUserOrChat2, z);
                    if ((forUserOrChat != null || initIfEmpty) && this.isPulledDown) {
                        findViewByPosition = this.layoutManager.findViewByPosition(0);
                        if (findViewByPosition != null) {
                            this.listView.smoothScrollBy(0, findViewByPosition.getTop() - AndroidUtilities.dp(88.0f), CubicBezierInterpolator.EASE_OUT_QUINT);
                        }
                    }
                    str3 = (currentVideoLocation == null && currentVideoLocation.imageType == 2) ? ImageLoader.AUTOPLAY_FILTER : null;
                    if (this.avatarBig == null && !this.isTopic) {
                        this.avatarImage.setImage(currentVideoLocation, str3, forUserOrChat2, "50_50", this.avatarDrawable, chat);
                    }
                    if (forUserOrChat != null && ((imageLocation = this.prevLoadedImageLocation) == null || forUserOrChat.photoId != imageLocation.photoId)) {
                        this.prevLoadedImageLocation = forUserOrChat;
                        getFileLoader().loadFile(forUserOrChat, chat, null, 0, 1);
                    }
                    this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation3) && !((storyViewer = this.storyViewer) != null && storyViewer.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
                }
                if (this.isTopic) {
                    if (findTopic != null) {
                        i4 = 1;
                        i5 = findTopic.totalMessagesCount - 1;
                    } else {
                        i4 = 1;
                        i5 = 0;
                    }
                    if (i5 > 0) {
                        Object[] objArr = new Object[i4];
                        i6 = 0;
                        objArr[0] = Integer.valueOf(i5);
                        formatString = LocaleController.formatPluralString("messages", i5, objArr);
                    } else {
                        i6 = 0;
                        int i11 = R.string.TopicProfileStatus;
                        Object[] objArr2 = new Object[i4];
                        objArr2[0] = chat.title;
                        formatString = LocaleController.formatString("TopicProfileStatus", i11, objArr2);
                    }
                    SpannableString spannableString = new SpannableString(">");
                    spannableString.setSpan(new ColoredImageSpan(R.drawable.arrow_newchat), i6, 1, 33);
                    spannableStringBuilder = new SpannableStringBuilder(chat.title).append(' ').append((CharSequence) spannableString);
                    formatPluralString = formatString;
                    z2 = true;
                    str2 = "drawableMuteIcon";
                    i = 0;
                    z3 = false;
                    i2 = 2;
                    while (i < i2) {
                    }
                    TLRPC$TL_forumTopic tLRPC$TL_forumTopic22 = findTopic;
                    if (z3) {
                    }
                    TLRPC$ChatPhoto tLRPC$ChatPhoto2 = chat.photo;
                    if (tLRPC$ChatPhoto2 != null) {
                    }
                    if (!this.isTopic) {
                    }
                    boolean initIfEmpty2 = this.avatarsViewPager.initIfEmpty(null, forUserOrChat, forUserOrChat2, z);
                    if (forUserOrChat != null) {
                    }
                    findViewByPosition = this.layoutManager.findViewByPosition(0);
                    if (findViewByPosition != null) {
                    }
                    if (currentVideoLocation == null) {
                    }
                    if (this.avatarBig == null) {
                        this.avatarImage.setImage(currentVideoLocation, str3, forUserOrChat2, "50_50", this.avatarDrawable, chat);
                    }
                    if (forUserOrChat != null) {
                        this.prevLoadedImageLocation = forUserOrChat;
                        getFileLoader().loadFile(forUserOrChat, chat, null, 0, 1);
                    }
                    this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation3) && !((storyViewer = this.storyViewer) != null && storyViewer.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
                } else {
                    if (this.currentChat.megagroup) {
                        if (this.onlineCount <= 1 || (i3 = this.chatInfo.participants_count) == 0) {
                            str2 = "drawableMuteIcon";
                            int i12 = this.chatInfo.participants_count;
                            if (i12 == 0) {
                                if (chat.has_geo) {
                                    formatPluralString = LocaleController.getString("MegaLocation", R.string.MegaLocation).toLowerCase();
                                } else if (ChatObject.isPublic(chat)) {
                                    formatPluralString = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                } else {
                                    formatPluralString = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                }
                            } else {
                                formatPluralString = LocaleController.formatPluralString("Members", i12, new Object[0]);
                                spannableStringBuilder = LocaleController.formatPluralStringComma("Members", this.chatInfo.participants_count);
                            }
                        } else {
                            str2 = "drawableMuteIcon";
                            formatPluralString = String.format("%s, %s", LocaleController.formatPluralString("Members", i3, new Object[0]), LocaleController.formatPluralString("OnlineCount", Math.min(this.onlineCount, this.chatInfo.participants_count), new Object[0]));
                            spannableStringBuilder = String.format("%s, %s", LocaleController.formatPluralStringComma("Members", this.chatInfo.participants_count), LocaleController.formatPluralStringComma("OnlineCount", Math.min(this.onlineCount, this.chatInfo.participants_count)));
                        }
                    } else {
                        str2 = "drawableMuteIcon";
                        LocaleController.formatShortNumber(this.chatInfo.participants_count, new int[1]);
                        if (this.currentChat.megagroup) {
                            formatPluralString = LocaleController.formatPluralString("Members", this.chatInfo.participants_count, new Object[0]);
                            spannableStringBuilder = LocaleController.formatPluralStringComma("Members", this.chatInfo.participants_count);
                        } else {
                            formatPluralString = LocaleController.formatPluralString("Subscribers", this.chatInfo.participants_count, new Object[0]);
                            spannableStringBuilder = LocaleController.formatPluralStringComma("Subscribers", this.chatInfo.participants_count);
                        }
                    }
                    z2 = false;
                    i = 0;
                    z3 = false;
                    i2 = 2;
                    while (i < i2) {
                    }
                    TLRPC$TL_forumTopic tLRPC$TL_forumTopic222 = findTopic;
                    if (z3) {
                    }
                    TLRPC$ChatPhoto tLRPC$ChatPhoto22 = chat.photo;
                    if (tLRPC$ChatPhoto22 != null) {
                    }
                    if (!this.isTopic) {
                    }
                    boolean initIfEmpty22 = this.avatarsViewPager.initIfEmpty(null, forUserOrChat, forUserOrChat2, z);
                    if (forUserOrChat != null) {
                    }
                    findViewByPosition = this.layoutManager.findViewByPosition(0);
                    if (findViewByPosition != null) {
                    }
                    if (currentVideoLocation == null) {
                    }
                    if (this.avatarBig == null) {
                    }
                    if (forUserOrChat != null) {
                    }
                    this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation3) && !((storyViewer = this.storyViewer) != null && storyViewer.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
                }
            } else {
                str = string;
                str2 = "drawableMuteIcon";
                if (ChatObject.isKickedFromChat(chat)) {
                    formatPluralString = LocaleController.getString("YouWereKicked", R.string.YouWereKicked);
                } else if (ChatObject.isLeftFromChat(chat)) {
                    formatPluralString = LocaleController.getString("YouLeft", R.string.YouLeft);
                } else {
                    int i13 = chat.participants_count;
                    TLRPC$ChatFull tLRPC$ChatFull3 = this.chatInfo;
                    if (tLRPC$ChatFull3 != null && (tLRPC$ChatParticipants = tLRPC$ChatFull3.participants) != null) {
                        i13 = tLRPC$ChatParticipants.participants.size();
                    }
                    formatPluralString = (i13 == 0 || this.onlineCount <= 1) ? LocaleController.formatPluralString("Members", i13, new Object[0]) : String.format("%s, %s", LocaleController.formatPluralString("Members", i13, new Object[0]), LocaleController.formatPluralString("OnlineCount", this.onlineCount, new Object[0]));
                }
            }
            spannableStringBuilder = formatPluralString;
            z2 = false;
            i = 0;
            z3 = false;
            i2 = 2;
            while (i < i2) {
            }
            TLRPC$TL_forumTopic tLRPC$TL_forumTopic2222 = findTopic;
            if (z3) {
            }
            TLRPC$ChatPhoto tLRPC$ChatPhoto222 = chat.photo;
            if (tLRPC$ChatPhoto222 != null) {
            }
            if (!this.isTopic) {
            }
            boolean initIfEmpty222 = this.avatarsViewPager.initIfEmpty(null, forUserOrChat, forUserOrChat2, z);
            if (forUserOrChat != null) {
            }
            findViewByPosition = this.layoutManager.findViewByPosition(0);
            if (findViewByPosition != null) {
            }
            if (currentVideoLocation == null) {
            }
            if (this.avatarBig == null) {
            }
            if (forUserOrChat != null) {
            }
            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(tLRPC$FileLocation3) && !((storyViewer = this.storyViewer) != null && storyViewer.isShown() && this.storyViewer.transitionViewHolder.view == this.avatarImage), this.storyView == null);
        }
        if (this.qrItem != null) {
            updateQrItemVisibility(true);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.updateEmojiStatusEffectPosition();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateProfileData$53(View view) {
        showStatusSelect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateProfileData$54(TLRPC$User tLRPC$User, SimpleTextView simpleTextView, View view) {
        ImageLocation forDocument;
        PremiumPreviewBottomSheet premiumPreviewBottomSheet = new PremiumPreviewBottomSheet(this, this.currentAccount, tLRPC$User, this.resourcesProvider);
        simpleTextView.getLocationOnScreen(new int[2]);
        premiumPreviewBottomSheet.startEnterFromX = simpleTextView.rightDrawableX;
        premiumPreviewBottomSheet.startEnterFromY = simpleTextView.rightDrawableY;
        premiumPreviewBottomSheet.startEnterFromScale = simpleTextView.getScaleX();
        premiumPreviewBottomSheet.startEnterFromX1 = simpleTextView.getLeft();
        premiumPreviewBottomSheet.startEnterFromY1 = simpleTextView.getTop();
        premiumPreviewBottomSheet.startEnterFromView = simpleTextView;
        Drawable rightDrawable = simpleTextView.getRightDrawable();
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] swapAnimatedEmojiDrawableArr = this.emojiStatusDrawable;
        if (rightDrawable == swapAnimatedEmojiDrawableArr[1] && swapAnimatedEmojiDrawableArr[1] != null && (swapAnimatedEmojiDrawableArr[1].getDrawable() instanceof AnimatedEmojiDrawable)) {
            premiumPreviewBottomSheet.startEnterFromScale *= 0.98f;
            TLRPC$Document document = ((AnimatedEmojiDrawable) this.emojiStatusDrawable[1].getDrawable()).getDocument();
            if (document != null) {
                BackupImageView backupImageView = new BackupImageView(getContext());
                String str = "160_160";
                SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(document.thumbs, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                if ("video/webm".equals(document.mime_type)) {
                    forDocument = ImageLocation.getForDocument(document);
                    str = "160_160_" + ImageLoader.AUTOPLAY_FILTER;
                    if (svgThumb != null) {
                        svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                    }
                } else {
                    if (svgThumb != null && MessageObject.isAnimatedStickerDocument(document, false)) {
                        svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                    }
                    forDocument = ImageLocation.getForDocument(document);
                }
                String str2 = str;
                backupImageView.setLayerNum(7);
                backupImageView.setRoundRadius(AndroidUtilities.dp(4.0f));
                backupImageView.setImage(forDocument, str2, ImageLocation.getForDocument(closestPhotoSizeWithSize, document), "140_140", svgThumb, document);
                if (((AnimatedEmojiDrawable) this.emojiStatusDrawable[1].getDrawable()).canOverrideColor()) {
                    backupImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhiteBlueIcon), PorterDuff.Mode.SRC_IN));
                    premiumPreviewBottomSheet.statusStickerSet = MessageObject.getInputStickerSet(document);
                } else {
                    premiumPreviewBottomSheet.statusStickerSet = MessageObject.getInputStickerSet(document);
                }
                premiumPreviewBottomSheet.overrideTitleIcon = backupImageView;
                premiumPreviewBottomSheet.isEmojiStatus = true;
            }
        }
        showDialog(premiumPreviewBottomSheet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateProfileData$55(View view) {
        goToForum();
    }

    private void updatedPeerColor() {
        SharedMediaLayout.ScrollSlidingTextTabStripInner scrollSlidingTextTabStripInner;
        TextView textView;
        int themedColor;
        TopView topView = this.topView;
        if (topView != null) {
            topView.setBackgroundColorId(this.peerColor, true);
        }
        SimpleTextView[] simpleTextViewArr = this.onlineTextView;
        if (simpleTextViewArr[1] != null) {
            if (simpleTextViewArr[1].getTag() instanceof Integer) {
                themedColor = getThemedColor(((Integer) this.onlineTextView[1].getTag()).intValue());
            } else {
                themedColor = getThemedColor(Theme.key_avatar_subtitleInProfileBlue);
            }
            this.onlineTextView[1].setTextColor(ColorUtils.blendARGB(applyPeerColor(themedColor, true, Boolean.valueOf(this.isOnline[0])), -1275068417, this.currentExpandAnimatorValue));
        }
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.setItemsBackgroundColor(this.peerColor != null ? 553648127 : getThemedColor(Theme.key_avatar_actionBarSelectorBlue), false);
            this.actionBar.setItemsColor(ColorUtils.blendARGB(this.peerColor != null ? -1 : getThemedColor(Theme.key_actionBarDefaultIcon), AvatarDrawable.getIconColorForId((this.userId != 0 || (ChatObject.isChannel(this.chatId, this.currentAccount) && !this.currentChat.megagroup)) ? 5L : this.chatId, this.resourcesProvider), this.avatarAnimationProgress), false);
        }
        SimpleTextView[] simpleTextViewArr2 = this.nameTextView;
        if (simpleTextViewArr2[1] != null) {
            simpleTextViewArr2[1].setTextColor(ColorUtils.blendARGB(this.peerColor != null ? -1 : getThemedColor(Theme.key_profile_title), -1, this.currentExpandAnimatorValue));
        }
        AutoDeletePopupWrapper autoDeletePopupWrapper = this.autoDeletePopupWrapper;
        if (autoDeletePopupWrapper != null && (textView = autoDeletePopupWrapper.textView) != null) {
            textView.invalidate();
        }
        AndroidUtilities.forEachViews((RecyclerView) this.listView, (com.google.android.exoplayer2.util.Consumer<View>) new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda27
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ProfileActivity.this.lambda$updatedPeerColor$56((View) obj);
            }
        });
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null && (scrollSlidingTextTabStripInner = sharedMediaLayout.scrollSlidingTextTabStrip) != null) {
            scrollSlidingTextTabStripInner.updateColors();
        }
        writeButtonSetBackground();
        updateEmojiStatusDrawableColor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatedPeerColor$56(View view) {
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).setTextColor(applyPeerColor(getThemedColor(Theme.key_windowBackgroundWhiteBlueHeader), false));
        } else if (view instanceof TextDetailCell) {
            ((TextDetailCell) view).valueTextView.setTextColor(applyPeerColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2), false));
        } else if (view instanceof TextCell) {
            ((TextCell) view).updateColors();
        } else if (view instanceof NotificationsCheckCell) {
            ((NotificationsCheckCell) view).getCheckBox().invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int applyPeerColor(int i) {
        return applyPeerColor(i, true, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int applyPeerColor(int i, boolean z) {
        return applyPeerColor(i, z, null);
    }

    private int applyPeerColor(int i, boolean z, Boolean bool) {
        if (z && this.peerColor != null) {
            int themedColor = getThemedColor(z ? Theme.key_actionBarDefault : Theme.key_windowBackgroundWhiteBlueIcon);
            int blendARGB = ColorUtils.blendARGB(this.peerColor.getStoryColor1(Theme.isCurrentThemeDark()), this.peerColor.getStoryColor2(Theme.isCurrentThemeDark()), 0.5f);
            int bgColor1 = z ? blendARGB : this.peerColor.getBgColor1(Theme.isCurrentThemeDark());
            if (!Theme.hasHue(themedColor)) {
                return (bool == null || bool.booleanValue()) ? blendARGB : Theme.adaptHSV(Theme.multAlpha(blendARGB, 0.7f), -0.2f, 0.2f);
            }
            boolean isCurrentThemeDark = Theme.isCurrentThemeDark();
            if (bool != null && !bool.booleanValue()) {
                blendARGB = Theme.multAlpha(blendARGB, 0.7f);
            }
            return Theme.changeColorAccent(themedColor, bgColor1, i, isCurrentThemeDark, blendARGB);
        }
        return i;
    }

    private int applyPeerColor2(int i) {
        MessagesController.PeerColor peerColor = this.peerColor;
        if (peerColor != null) {
            int bgColor2 = peerColor.getBgColor2(Theme.isCurrentThemeDark());
            return Theme.changeColorAccent(getThemedColor(Theme.key_windowBackgroundWhiteBlueIcon), bgColor2, i, Theme.isCurrentThemeDark(), bgColor2);
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:231:0x0463  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x049e  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x04b7  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x04be  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x04f6  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x05ac  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x05c3  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x05da  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x05f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createActionBarMenu(boolean z) {
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        int i;
        PagerIndicatorView pagerIndicatorView;
        SharedMediaLayout sharedMediaLayout;
        int i2;
        int i3;
        ActionBar actionBar = this.actionBar;
        if (actionBar == null || this.otherItem == null) {
            return;
        }
        Context context = actionBar.getContext();
        this.otherItem.removeAllSubItems();
        this.animatingItem = null;
        this.editItemVisible = false;
        this.callItemVisible = false;
        this.videoCallItemVisible = false;
        this.canSearchMembers = false;
        boolean z2 = true;
        if (this.userId != 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user == null) {
                return;
            }
            if (UserObject.isUserSelf(user)) {
                this.otherItem.addSubItem(30, R.drawable.msg_edit, LocaleController.getString(R.string.EditName));
                this.otherItem.addSubItem(40, R.drawable.msg_colors, LocaleController.getString(R.string.EditProfileColor));
                if (this.imageUpdater == null) {
                    this.otherItem.addSubItem(36, R.drawable.msg_addphoto, LocaleController.getString("AddPhoto", R.string.AddPhoto));
                    this.otherItem.addSubItem(33, R.drawable.msg_openprofile, LocaleController.getString("SetAsMain", R.string.SetAsMain));
                    this.otherItem.addSubItem(21, R.drawable.msg_gallery, LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                    this.otherItem.addSubItem(35, R.drawable.msg_delete, LocaleController.getString("Delete", R.string.Delete));
                } else {
                    this.otherItem.addSubItem(21, R.drawable.msg_gallery, LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                }
                if (getMessagesController().isChatNoForwards(this.currentChat)) {
                    this.otherItem.hideSubItem(21);
                }
                if (z2) {
                    this.otherItem.addSubItem(31, R.drawable.msg_leave, LocaleController.getString("LogOut", R.string.LogOut));
                }
                if (!this.isPulledDown) {
                    this.otherItem.hideSubItem(21);
                    this.otherItem.hideSubItem(33);
                    this.otherItem.showSubItem(36);
                    this.otherItem.hideSubItem(34);
                    this.otherItem.hideSubItem(35);
                }
                if (!this.mediaHeaderVisible) {
                    if (this.callItemVisible) {
                        if (this.callItem.getVisibility() != 0) {
                            this.callItem.setVisibility(0);
                            if (z) {
                                this.callItem.setAlpha(0.0f);
                                this.callItem.animate().alpha(1.0f).setDuration(150L).start();
                            }
                        }
                    } else if (this.callItem.getVisibility() != 8) {
                        this.callItem.setVisibility(8);
                    }
                    if (this.videoCallItemVisible) {
                        if (this.videoCallItem.getVisibility() != 0) {
                            this.videoCallItem.setVisibility(0);
                            if (z) {
                                this.videoCallItem.setAlpha(0.0f);
                                this.videoCallItem.animate().alpha(1.0f).setDuration(150L).start();
                            }
                        }
                    } else if (this.videoCallItem.getVisibility() != 8) {
                        this.videoCallItem.setVisibility(8);
                    }
                    if (this.editItemVisible) {
                        if (this.editItem.getVisibility() != 0) {
                            this.editItem.setVisibility(0);
                            if (z) {
                                this.editItem.setAlpha(0.0f);
                                this.editItem.animate().alpha(1.0f).setDuration(150L).start();
                            }
                        }
                    } else if (this.editItem.getVisibility() != 8) {
                        this.editItem.setVisibility(8);
                    }
                }
                pagerIndicatorView = this.avatarsViewPagerIndicatorView;
                if (pagerIndicatorView != null && pagerIndicatorView.isIndicatorFullyVisible()) {
                    if (this.editItemVisible) {
                        this.editItem.setVisibility(8);
                        this.editItem.animate().cancel();
                        this.editItem.setAlpha(1.0f);
                    }
                    if (this.callItemVisible) {
                        this.callItem.setVisibility(8);
                        this.callItem.animate().cancel();
                        this.callItem.setAlpha(1.0f);
                    }
                    if (this.videoCallItemVisible) {
                        this.videoCallItem.setVisibility(8);
                        this.videoCallItem.animate().cancel();
                        this.videoCallItem.setAlpha(1.0f);
                    }
                }
                sharedMediaLayout = this.sharedMediaLayout;
                if (sharedMediaLayout != null) {
                    sharedMediaLayout.getSearchItem().requestLayout();
                }
                updateStoriesViewBounds(false);
            }
            if (user.bot && user.bot_can_edit) {
                this.editItemVisible = true;
            }
            TLRPC$UserFull tLRPC$UserFull = this.userInfo;
            if (tLRPC$UserFull != null && tLRPC$UserFull.phone_calls_available) {
                this.callItemVisible = true;
                this.videoCallItemVisible = Build.VERSION.SDK_INT >= 18 && tLRPC$UserFull.video_calls_available;
            }
            String str = "Unblock";
            if (this.isBot || getContactsController().contactsDict.get(Long.valueOf(this.userId)) == null) {
                if (MessagesController.isSupportUser(user)) {
                    if (this.userBlocked) {
                        this.otherItem.addSubItem(2, R.drawable.msg_block, LocaleController.getString("Unblock", R.string.Unblock));
                    }
                    this.otherItem.addSubItem(14, R.drawable.msg_home, LocaleController.getString("AddShortcut", R.string.AddShortcut));
                } else {
                    if (this.currentEncryptedChat == null) {
                        createAutoDeleteItem(context);
                    }
                    this.otherItem.addSubItem(14, R.drawable.msg_home, LocaleController.getString("AddShortcut", R.string.AddShortcut));
                    if (this.isBot) {
                        this.otherItem.addSubItem(10, R.drawable.msg_share, LocaleController.getString("BotShare", R.string.BotShare));
                    } else {
                        this.otherItem.addSubItem(1, R.drawable.msg_addcontact, LocaleController.getString("AddContact", R.string.AddContact));
                    }
                    if (!TextUtils.isEmpty(user.phone)) {
                        this.otherItem.addSubItem(3, R.drawable.msg_share, LocaleController.getString("ShareContact", R.string.ShareContact));
                    }
                    if (this.isBot) {
                        if (!this.userBlocked) {
                            ActionBarMenuSubItem addSubItem = this.otherItem.addSubItem(2, R.drawable.msg_block2, LocaleController.getString(R.string.DeleteAndBlock));
                            int i4 = Theme.key_text_RedRegular;
                            addSubItem.setColors(getThemedColor(i4), getThemedColor(i4));
                        } else {
                            this.otherItem.addSubItem(2, R.drawable.msg_retry, LocaleController.getString("BotRestart", R.string.BotRestart));
                        }
                    } else {
                        ActionBarMenuItem actionBarMenuItem = this.otherItem;
                        boolean z3 = this.userBlocked;
                        int i5 = R.drawable.msg_block;
                        if (z3) {
                            i2 = R.string.Unblock;
                        } else {
                            i2 = R.string.BlockContact;
                            str = "BlockContact";
                        }
                        actionBarMenuItem.addSubItem(2, i5, LocaleController.getString(str, i2));
                    }
                }
            } else {
                if (this.currentEncryptedChat == null) {
                    createAutoDeleteItem(context);
                }
                if (!TextUtils.isEmpty(user.phone)) {
                    this.otherItem.addSubItem(3, R.drawable.msg_share, LocaleController.getString("ShareContact", R.string.ShareContact));
                }
                ActionBarMenuItem actionBarMenuItem2 = this.otherItem;
                boolean z4 = this.userBlocked;
                int i6 = R.drawable.msg_block;
                if (z4) {
                    i3 = R.string.Unblock;
                } else {
                    i3 = R.string.BlockContact;
                    str = "BlockContact";
                }
                actionBarMenuItem2.addSubItem(2, i6, LocaleController.getString(str, i3));
                this.otherItem.addSubItem(4, R.drawable.msg_edit, LocaleController.getString("EditContact", R.string.EditContact));
                this.otherItem.addSubItem(5, R.drawable.msg_delete, LocaleController.getString("DeleteContact", R.string.DeleteContact));
            }
            if (!UserObject.isDeleted(user) && !this.isBot && this.currentEncryptedChat == null && !this.userBlocked) {
                long j = this.userId;
                if (j != 333000 && j != 777000 && j != 42777) {
                    if (!user.premium && !BuildVars.IS_BILLING_UNAVAILABLE && !user.self && this.userInfo != null && !getMessagesController().premiumLocked && !this.userInfo.premium_gifts.isEmpty()) {
                        this.otherItem.addSubItem(38, R.drawable.msg_gift_premium, LocaleController.getString(R.string.GiftPremium));
                    }
                    this.otherItem.addSubItem(20, R.drawable.msg_secret, LocaleController.getString("StartEncryptedChat", R.string.StartEncryptedChat));
                }
            }
            if (!this.isBot && getContactsController().contactsDict.get(Long.valueOf(this.userId)) != null) {
                this.otherItem.addSubItem(14, R.drawable.msg_home, LocaleController.getString("AddShortcut", R.string.AddShortcut));
            }
        } else if (this.chatId != 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            this.hasVoiceChatItem = false;
            if (this.topicId == 0 && ChatObject.canChangeChatInfo(chat)) {
                createAutoDeleteItem(context);
            }
            String str2 = "StartVoipChat";
            if (ChatObject.isChannel(chat)) {
                if (this.isTopic) {
                    if (ChatObject.canManageTopic(this.currentAccount, chat, this.topicId)) {
                        this.editItemVisible = true;
                    }
                } else if (ChatObject.hasAdminRights(chat) || (chat.megagroup && ChatObject.canChangeChatInfo(chat))) {
                    this.editItemVisible = true;
                }
                if (this.chatInfo != null) {
                    if (ChatObject.canManageCalls(chat) && this.chatInfo.call == null) {
                        ActionBarMenuItem actionBarMenuItem3 = this.otherItem;
                        int i7 = R.drawable.msg_voicechat;
                        if (!chat.megagroup || chat.gigagroup) {
                            i = R.string.StartVoipChannel;
                            str2 = "StartVoipChannel";
                        } else {
                            i = R.string.StartVoipChat;
                        }
                        actionBarMenuItem3.addSubItem(15, i7, LocaleController.getString(str2, i));
                        this.hasVoiceChatItem = true;
                    }
                    if ((this.chatInfo.can_view_stats || getMessagesController().getStoriesController().canPostStories(getDialogId())) && this.topicId == 0) {
                        this.otherItem.addSubItem(19, R.drawable.msg_stats, LocaleController.getString("Statistics", R.string.Statistics));
                    }
                    this.callItemVisible = getMessagesController().getGroupCall(this.chatId, false) != null;
                }
                if (chat.megagroup) {
                    TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
                    if (tLRPC$ChatFull2 == null || !tLRPC$ChatFull2.participants_hidden || ChatObject.hasAdminRights(chat)) {
                        this.canSearchMembers = true;
                        this.otherItem.addSubItem(17, R.drawable.msg_search, LocaleController.getString("SearchMembers", R.string.SearchMembers));
                    }
                    if (!chat.creator && !chat.left && !chat.kicked && !this.isTopic) {
                        this.otherItem.addSubItem(7, R.drawable.msg_leave, LocaleController.getString("LeaveMegaMenu", R.string.LeaveMegaMenu));
                    }
                    if (this.isTopic && ChatObject.canDeleteTopic(this.currentAccount, chat, this.topicId)) {
                        this.otherItem.addSubItem(23, R.drawable.msg_delete, LocaleController.getPluralString("DeleteTopics", 1));
                    }
                } else {
                    if (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.edit_stories)) {
                        this.otherItem.addSubItem(39, R.drawable.msg_archive, LocaleController.getString(R.string.OpenChannelArchiveStories));
                    }
                    if (ChatObject.isPublic(chat)) {
                        this.otherItem.addSubItem(10, R.drawable.msg_share, LocaleController.getString("BotShare", R.string.BotShare));
                    }
                    TLRPC$ChatFull tLRPC$ChatFull3 = this.chatInfo;
                    if (tLRPC$ChatFull3 != null && tLRPC$ChatFull3.linked_chat_id != 0) {
                        this.otherItem.addSubItem(22, R.drawable.msg_discussion, LocaleController.getString("ViewDiscussion", R.string.ViewDiscussion));
                    }
                    TLRPC$Chat tLRPC$Chat = this.currentChat;
                    if (!tLRPC$Chat.creator && !tLRPC$Chat.left && !tLRPC$Chat.kicked) {
                        this.otherItem.addSubItem(7, R.drawable.msg_leave, LocaleController.getString("LeaveChannelMenu", R.string.LeaveChannelMenu));
                    }
                }
            } else {
                if (this.chatInfo != null) {
                    if (ChatObject.canManageCalls(chat) && this.chatInfo.call == null) {
                        this.otherItem.addSubItem(15, R.drawable.msg_voicechat, LocaleController.getString("StartVoipChat", R.string.StartVoipChat));
                        this.hasVoiceChatItem = true;
                    }
                    this.callItemVisible = getMessagesController().getGroupCall(this.chatId, false) != null;
                }
                if (ChatObject.canChangeChatInfo(chat)) {
                    this.editItemVisible = true;
                }
                if (!ChatObject.isKickedFromChat(chat) && !ChatObject.isLeftFromChat(chat) && ((tLRPC$ChatFull = this.chatInfo) == null || !tLRPC$ChatFull.participants_hidden || ChatObject.hasAdminRights(chat))) {
                    this.canSearchMembers = true;
                    this.otherItem.addSubItem(17, R.drawable.msg_search, LocaleController.getString("SearchMembers", R.string.SearchMembers));
                }
                this.otherItem.addSubItem(7, R.drawable.msg_leave, LocaleController.getString("DeleteAndExit", R.string.DeleteAndExit));
            }
            if (this.topicId == 0) {
                this.otherItem.addSubItem(14, R.drawable.msg_home, LocaleController.getString("AddShortcut", R.string.AddShortcut));
            }
        }
        z2 = false;
        if (this.imageUpdater == null) {
        }
        if (getMessagesController().isChatNoForwards(this.currentChat)) {
        }
        if (z2) {
        }
        if (!this.isPulledDown) {
        }
        if (!this.mediaHeaderVisible) {
        }
        pagerIndicatorView = this.avatarsViewPagerIndicatorView;
        if (pagerIndicatorView != null) {
            if (this.editItemVisible) {
            }
            if (this.callItemVisible) {
            }
            if (this.videoCallItemVisible) {
            }
        }
        sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
        }
        updateStoriesViewBounds(false);
    }

    private void createAutoDeleteItem(Context context) {
        int i;
        this.autoDeletePopupWrapper = new AutoDeletePopupWrapper(context, this.otherItem.getPopupLayout().getSwipeBack(), new AutoDeletePopupWrapper.Callback() { // from class: org.telegram.ui.ProfileActivity.48
            @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
            public void dismiss() {
                ProfileActivity.this.otherItem.toggleSubMenu();
            }

            @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
            public void setAutoDeleteHistory(int i2, int i3) {
                ProfileActivity.this.setAutoDeleteHistory(i2, i3);
            }

            @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
            public void showGlobalAutoDeleteScreen() {
                ProfileActivity.this.presentFragment(new AutoDeleteMessagesActivity());
                dismiss();
            }
        }, false, 0, this.resourcesProvider);
        if (this.dialogId > 0 || this.userId > 0) {
            this.autoDeletePopupWrapper.allowExtendedHint(applyPeerColor(getThemedColor(Theme.key_windowBackgroundWhiteBlueText), false));
        }
        TLRPC$UserFull tLRPC$UserFull = this.userInfo;
        if (tLRPC$UserFull == null && this.chatInfo == null) {
            i = 0;
        } else {
            i = tLRPC$UserFull != null ? tLRPC$UserFull.ttl_period : this.chatInfo.ttl_period;
        }
        TimerDrawable ttlIcon = TimerDrawable.getTtlIcon(i);
        this.autoDeleteItemDrawable = ttlIcon;
        this.autoDeleteItem = this.otherItem.addSwipeBackItem(0, ttlIcon, LocaleController.getString("AutoDeletePopupTitle", R.string.AutoDeletePopupTitle), this.autoDeletePopupWrapper.windowLayout);
        this.otherItem.addColoredGap();
        updateAutoDeleteItem();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Drawable getThemedDrawable(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Drawable drawable = resourcesProvider != null ? resourcesProvider.getDrawable(str) : null;
        return drawable != null ? drawable : super.getThemedDrawable(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAutoDeleteHistory(int i, int i2) {
        long dialogId = getDialogId();
        getMessagesController().setDialogHistoryTTL(dialogId, i);
        if (this.userInfo == null && this.chatInfo == null) {
            return;
        }
        UndoView undoView = this.undoView;
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(dialogId));
        TLRPC$UserFull tLRPC$UserFull = this.userInfo;
        undoView.showWithAction(dialogId, i2, user, Integer.valueOf(tLRPC$UserFull != null ? tLRPC$UserFull.ttl_period : this.chatInfo.ttl_period), (Runnable) null, (Runnable) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            recyclerListView.invalidateViews();
        }
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public boolean didSelectDialogs(DialogsActivity dialogsActivity, ArrayList<MessagesStorage.TopicKey> arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        long j = arrayList.get(0).dialogId;
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(j)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
        } else if (DialogObject.isUserDialog(j)) {
            bundle.putLong("user_id", j);
        } else if (DialogObject.isChatDialog(j)) {
            bundle.putLong("chat_id", -j);
        }
        if (getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter notificationCenter = getNotificationCenter();
            int i = NotificationCenter.closeChats;
            notificationCenter.removeObserver(this, i);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, new Object[0]);
            presentFragment(new ChatActivity(bundle), true);
            removeSelfFromStack();
            getSendMessagesHelper().sendMessage(SendMessagesHelper.SendMessageParams.of(getMessagesController().getUser(Long.valueOf(this.userId)), j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, true, 0));
            if (!TextUtils.isEmpty(charSequence)) {
                SendMessagesHelper.prepareSendingText(AccountInstance.getInstance(this.currentAccount), charSequence.toString(), j, true, 0);
            }
            return true;
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        boolean z;
        boolean z2;
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onRequestPermissionsResultFragment(i, strArr, iArr);
        }
        if (i == 101 || i == 102) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user == null) {
                return;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= iArr.length) {
                    z = true;
                    break;
                } else if (iArr[i2] != 0) {
                    z = false;
                    break;
                } else {
                    i2++;
                }
            }
            if (iArr.length > 0 && z) {
                boolean z3 = i == 102;
                TLRPC$UserFull tLRPC$UserFull = this.userInfo;
                VoIPHelper.startCall(user, z3, tLRPC$UserFull != null && tLRPC$UserFull.video_calls_available, getParentActivity(), this.userInfo, getAccountInstance());
                return;
            }
            VoIPHelper.permissionDenied(getParentActivity(), null, i);
        } else if (i != 103 || this.currentChat == null) {
        } else {
            int i3 = 0;
            while (true) {
                if (i3 >= iArr.length) {
                    z2 = true;
                    break;
                } else if (iArr[i3] != 0) {
                    z2 = false;
                    break;
                } else {
                    i3++;
                }
            }
            if (iArr.length > 0 && z2) {
                VoIPHelper.startCall(this.currentChat, null, null, getMessagesController().getGroupCall(this.chatId, false) == null, getParentActivity(), this, getAccountInstance());
                return;
            }
            VoIPHelper.permissionDenied(getParentActivity(), null, i);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void dismissCurrentDialog() {
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater == null || !imageUpdater.dismissCurrentDialog(this.visibleDialog)) {
            super.dismissCurrentDialog();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean dismissDialogOnPause(Dialog dialog) {
        ImageUpdater imageUpdater = this.imageUpdater;
        return (imageUpdater == null || imageUpdater.dismissDialogOnPause(dialog)) && super.dismissDialogOnPause(dialog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Animator searchExpandTransition(final boolean z) {
        if (z) {
            AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
            AndroidUtilities.setAdjustResizeToNothing(getParentActivity(), this.classGuid);
        }
        Animator animator = this.searchViewTransition;
        if (animator != null) {
            animator.removeAllListeners();
            this.searchViewTransition.cancel();
        }
        float[] fArr = new float[2];
        fArr[0] = this.searchTransitionProgress;
        fArr[1] = z ? 0.0f : 1.0f;
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        final float f = this.extraHeight;
        this.searchListView.setTranslationY(f);
        this.searchListView.setVisibility(0);
        this.searchItem.setVisibility(0);
        this.listView.setVisibility(0);
        needLayout(true);
        this.avatarContainer.setVisibility(0);
        this.nameTextView[1].setVisibility(0);
        this.onlineTextView[1].setVisibility(0);
        this.onlineTextView[3].setVisibility(0);
        this.actionBar.onSearchFieldVisibilityChanged(this.searchTransitionProgress > 0.5f);
        int i = this.searchTransitionProgress > 0.5f ? 0 : 8;
        ActionBarMenuItem actionBarMenuItem = this.otherItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setVisibility(i);
        }
        if (this.qrItem != null) {
            updateQrItemVisibility(false);
        }
        this.searchItem.setVisibility(i);
        this.searchItem.getSearchContainer().setVisibility(this.searchTransitionProgress <= 0.5f ? 0 : 8);
        this.searchListView.setEmptyView(this.emptyView);
        this.avatarContainer.setClickable(false);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProfileActivity.this.lambda$searchExpandTransition$57(ofFloat, f, z, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.49
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                ProfileActivity.this.updateSearchViewState(z);
                ProfileActivity.this.avatarContainer.setClickable(true);
                if (z) {
                    ProfileActivity.this.searchItem.requestFocusOnSearchView();
                }
                ProfileActivity.this.needLayout(true);
                ProfileActivity.this.searchViewTransition = null;
                ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
                if (z) {
                    ProfileActivity.this.invalidateScroll = true;
                    ProfileActivity.this.saveScrollPosition();
                    AndroidUtilities.requestAdjustResize(ProfileActivity.this.getParentActivity(), ((BaseFragment) ProfileActivity.this).classGuid);
                    ProfileActivity.this.emptyView.setPreventMoving(false);
                }
            }
        });
        if (!z) {
            this.invalidateScroll = true;
            saveScrollPosition();
            AndroidUtilities.requestAdjustNothing(getParentActivity(), this.classGuid);
            this.emptyView.setPreventMoving(true);
        }
        ofFloat.setDuration(220L);
        ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.searchViewTransition = ofFloat;
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchExpandTransition$57(ValueAnimator valueAnimator, float f, boolean z, ValueAnimator valueAnimator2) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.searchTransitionProgress = floatValue;
        float f2 = (floatValue - 0.5f) / 0.5f;
        float f3 = (0.5f - floatValue) / 0.5f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        float f4 = -f;
        this.searchTransitionOffset = (int) ((1.0f - floatValue) * f4);
        this.searchListView.setTranslationY(floatValue * f);
        this.emptyView.setTranslationY(f * this.searchTransitionProgress);
        this.listView.setTranslationY(f4 * (1.0f - this.searchTransitionProgress));
        this.listView.setScaleX(1.0f - ((1.0f - this.searchTransitionProgress) * 0.01f));
        this.listView.setScaleY(1.0f - ((1.0f - this.searchTransitionProgress) * 0.01f));
        this.listView.setAlpha(this.searchTransitionProgress);
        needLayout(true);
        this.listView.setAlpha(f2);
        this.searchListView.setAlpha(1.0f - this.searchTransitionProgress);
        this.searchListView.setScaleX((this.searchTransitionProgress * 0.05f) + 1.0f);
        this.searchListView.setScaleY((this.searchTransitionProgress * 0.05f) + 1.0f);
        this.emptyView.setAlpha(1.0f - f2);
        this.avatarContainer.setAlpha(f2);
        ProfileStoriesView profileStoriesView = this.storyView;
        if (profileStoriesView != null) {
            profileStoriesView.setAlpha(f2);
        }
        this.nameTextView[1].setAlpha(f2);
        this.onlineTextView[1].setAlpha(f2);
        this.onlineTextView[3].setAlpha(f2);
        this.searchItem.getSearchField().setAlpha(f3);
        if (z && this.searchTransitionProgress < 0.7f) {
            this.searchItem.requestFocusOnSearchView();
        }
        this.searchItem.getSearchContainer().setVisibility(this.searchTransitionProgress < 0.5f ? 0 : 8);
        int i = this.searchTransitionProgress > 0.5f ? 0 : 8;
        ActionBarMenuItem actionBarMenuItem = this.otherItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setVisibility(i);
            this.otherItem.setAlpha(f2);
        }
        ActionBarMenuItem actionBarMenuItem2 = this.qrItem;
        if (actionBarMenuItem2 != null) {
            actionBarMenuItem2.setAlpha(f2);
            updateQrItemVisibility(false);
        }
        this.searchItem.setVisibility(i);
        this.actionBar.onSearchFieldVisibilityChanged(this.searchTransitionProgress < 0.5f);
        ActionBarMenuItem actionBarMenuItem3 = this.otherItem;
        if (actionBarMenuItem3 != null) {
            actionBarMenuItem3.setAlpha(f2);
        }
        ActionBarMenuItem actionBarMenuItem4 = this.qrItem;
        if (actionBarMenuItem4 != null) {
            actionBarMenuItem4.setAlpha(f2);
        }
        this.searchItem.setAlpha(f2);
        this.topView.invalidate();
        this.fragmentView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSearchViewState(boolean z) {
        int i = 0;
        int i2 = z ? 8 : 0;
        this.listView.setVisibility(i2);
        this.searchListView.setVisibility(z ? 0 : 8);
        this.searchItem.getSearchContainer().setVisibility(z ? 0 : 8);
        this.actionBar.onSearchFieldVisibilityChanged(z);
        this.avatarContainer.setVisibility(i2);
        ProfileStoriesView profileStoriesView = this.storyView;
        if (profileStoriesView != null) {
            profileStoriesView.setVisibility(i2);
        }
        this.nameTextView[1].setVisibility(i2);
        this.onlineTextView[1].setVisibility(i2);
        this.onlineTextView[3].setVisibility(i2);
        ActionBarMenuItem actionBarMenuItem = this.otherItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setAlpha(1.0f);
            this.otherItem.setVisibility(i2);
        }
        ActionBarMenuItem actionBarMenuItem2 = this.qrItem;
        if (actionBarMenuItem2 != null) {
            actionBarMenuItem2.setAlpha(1.0f);
            this.qrItem.setVisibility((z || !isQrNeedVisible()) ? 8 : 8);
        }
        this.searchItem.setVisibility(i2);
        this.avatarContainer.setAlpha(1.0f);
        ProfileStoriesView profileStoriesView2 = this.storyView;
        if (profileStoriesView2 != null) {
            profileStoriesView2.setAlpha(1.0f);
        }
        this.nameTextView[1].setAlpha(1.0f);
        this.onlineTextView[1].setAlpha(1.0f);
        this.searchItem.setAlpha(1.0f);
        this.listView.setAlpha(1.0f);
        this.searchListView.setAlpha(1.0f);
        this.emptyView.setAlpha(1.0f);
        if (z) {
            this.searchListView.setEmptyView(this.emptyView);
        } else {
            this.emptyView.setVisibility(8);
        }
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void onUploadProgressChanged(float f) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(f);
        this.avatarsViewPager.setUploadProgress(this.uploadingImageLocation, f);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didStartUpload(boolean z) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(0.0f);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didUploadPhoto(final TLRPC$InputFile tLRPC$InputFile, final TLRPC$InputFile tLRPC$InputFile2, final double d, final String str, final TLRPC$PhotoSize tLRPC$PhotoSize, final TLRPC$PhotoSize tLRPC$PhotoSize2, boolean z, final TLRPC$VideoSize tLRPC$VideoSize) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.lambda$didUploadPhoto$60(tLRPC$InputFile, tLRPC$InputFile2, tLRPC$VideoSize, d, str, tLRPC$PhotoSize2, tLRPC$PhotoSize);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$59(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.this.lambda$didUploadPhoto$58(tLRPC$TL_error, tLObject, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$58(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str) {
        if (tLRPC$TL_error == null) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
            if (user == null) {
                user = getUserConfig().getCurrentUser();
                if (user == null) {
                    return;
                }
                getMessagesController().putUser(user, false);
            } else {
                getUserConfig().setCurrentUser(user);
            }
            TLRPC$TL_photos_photo tLRPC$TL_photos_photo = (TLRPC$TL_photos_photo) tLObject;
            ArrayList<TLRPC$PhotoSize> arrayList = tLRPC$TL_photos_photo.photo.sizes;
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, 800);
            TLRPC$VideoSize closestVideoSizeWithSize = tLRPC$TL_photos_photo.photo.video_sizes.isEmpty() ? null : FileLoader.getClosestVideoSizeWithSize(tLRPC$TL_photos_photo.photo.video_sizes, 1000);
            TLRPC$TL_userProfilePhoto tLRPC$TL_userProfilePhoto = new TLRPC$TL_userProfilePhoto();
            user.photo = tLRPC$TL_userProfilePhoto;
            tLRPC$TL_userProfilePhoto.photo_id = tLRPC$TL_photos_photo.photo.id;
            if (closestPhotoSizeWithSize != null) {
                tLRPC$TL_userProfilePhoto.photo_small = closestPhotoSizeWithSize.location;
            }
            if (closestPhotoSizeWithSize2 != null) {
                tLRPC$TL_userProfilePhoto.photo_big = closestPhotoSizeWithSize2.location;
            }
            if (closestPhotoSizeWithSize != null && this.avatar != null) {
                FileLoader.getInstance(this.currentAccount).getPathToAttach(this.avatar, true).renameTo(FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true));
                ImageLoader.getInstance().replaceImageInCache(this.avatar.volume_id + "_" + this.avatar.local_id + "@50_50", closestPhotoSizeWithSize.location.volume_id + "_" + closestPhotoSizeWithSize.location.local_id + "@50_50", ImageLocation.getForUserOrChat(user, 1), false);
            }
            if (closestVideoSizeWithSize != null && str != null) {
                new File(str).renameTo(FileLoader.getInstance(this.currentAccount).getPathToAttach(closestVideoSizeWithSize, "mp4", true));
            } else if (closestPhotoSizeWithSize2 != null && this.avatarBig != null) {
                FileLoader.getInstance(this.currentAccount).getPathToAttach(this.avatarBig, true).renameTo(FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize2, true));
            }
            getMessagesController().getDialogPhotos(user.id).addPhotoAtStart(tLRPC$TL_photos_photo.photo);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(user);
            getMessagesStorage().putUsersAndChats(arrayList2, null, false, true);
            TLRPC$UserFull userFull = getMessagesController().getUserFull(this.userId);
            userFull.profile_photo = tLRPC$TL_photos_photo.photo;
            getMessagesStorage().updateUserInfo(userFull, false);
        }
        this.allowPullingDown = (AndroidUtilities.isTablet() || this.isInLandscapeMode || !this.avatarImage.getImageReceiver().hasNotThumb() || AndroidUtilities.isAccessibilityScreenReaderEnabled()) ? false : true;
        this.avatar = null;
        this.avatarBig = null;
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        profileGalleryView.scrolledByUser = true;
        profileGalleryView.removeUploadingImage(this.uploadingImageLocation);
        this.avatarsViewPager.setCreateThumbFromParent(false);
        updateProfileData(true);
        showAvatarProgress(false, true);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_ALL));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
        getUserConfig().saveConfig(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$60(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, TLRPC$VideoSize tLRPC$VideoSize, double d, final String str, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        if (tLRPC$InputFile != null || tLRPC$InputFile2 != null || tLRPC$VideoSize != null) {
            if (this.avatar == null) {
                return;
            }
            TLRPC$TL_photos_uploadProfilePhoto tLRPC$TL_photos_uploadProfilePhoto = new TLRPC$TL_photos_uploadProfilePhoto();
            if (tLRPC$InputFile != null) {
                tLRPC$TL_photos_uploadProfilePhoto.file = tLRPC$InputFile;
                tLRPC$TL_photos_uploadProfilePhoto.flags |= 1;
            }
            if (tLRPC$InputFile2 != null) {
                tLRPC$TL_photos_uploadProfilePhoto.video = tLRPC$InputFile2;
                int i = tLRPC$TL_photos_uploadProfilePhoto.flags | 2;
                tLRPC$TL_photos_uploadProfilePhoto.flags = i;
                tLRPC$TL_photos_uploadProfilePhoto.video_start_ts = d;
                tLRPC$TL_photos_uploadProfilePhoto.flags = i | 4;
            }
            if (tLRPC$VideoSize != null) {
                tLRPC$TL_photos_uploadProfilePhoto.video_emoji_markup = tLRPC$VideoSize;
                tLRPC$TL_photos_uploadProfilePhoto.flags |= 16;
            }
            this.avatarUploadingRequest = getConnectionsManager().sendRequest(tLRPC$TL_photos_uploadProfilePhoto, new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda60
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    ProfileActivity.this.lambda$didUploadPhoto$59(str, tLObject, tLRPC$TL_error);
                }
            });
        } else {
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            this.avatar = tLRPC$FileLocation;
            this.avatarBig = tLRPC$PhotoSize2.location;
            this.avatarImage.setImage(ImageLocation.getForLocal(tLRPC$FileLocation), "50_50", this.avatarDrawable, (Object) null);
            if (this.setAvatarRow != -1) {
                updateRowsIds();
                ListAdapter listAdapter = this.listAdapter;
                if (listAdapter != null) {
                    listAdapter.notifyDataSetChanged();
                }
                needLayout(true);
            }
            ProfileGalleryView profileGalleryView = this.avatarsViewPager;
            ImageLocation forLocal = ImageLocation.getForLocal(this.avatarBig);
            this.uploadingImageLocation = forLocal;
            profileGalleryView.addUploadingImage(forLocal, ImageLocation.getForLocal(this.avatar));
            showAvatarProgress(true, false);
        }
        this.actionBar.createMenu().requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAvatarProgress(final boolean z, boolean z2) {
        if (this.avatarProgressView == null) {
            return;
        }
        AnimatorSet animatorSet = this.avatarAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.avatarAnimation = null;
        }
        if (!z2) {
            if (z) {
                this.avatarProgressView.setAlpha(1.0f);
                this.avatarProgressView.setVisibility(0);
                return;
            }
            this.avatarProgressView.setAlpha(0.0f);
            this.avatarProgressView.setVisibility(4);
            return;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.avatarAnimation = animatorSet2;
        if (z) {
            this.avatarProgressView.setVisibility(0);
            this.avatarAnimation.playTogether(ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 1.0f));
        } else {
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 0.0f));
        }
        this.avatarAnimation.setDuration(180L);
        this.avatarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.50
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ProfileActivity.this.avatarAnimation == null || ProfileActivity.this.avatarProgressView == null) {
                    return;
                }
                if (!z) {
                    ProfileActivity.this.avatarProgressView.setVisibility(4);
                }
                ProfileActivity.this.avatarAnimation = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                ProfileActivity.this.avatarAnimation = null;
            }
        });
        this.avatarAnimation.start();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onActivityResult(i, i2, intent);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void saveSelfArgs(Bundle bundle) {
        String str;
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater == null || (str = imageUpdater.currentPicturePath) == null) {
            return;
        }
        bundle.putString("path", str);
    }

    public void restoreSelfArgs(Bundle bundle) {
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.currentPicturePath = bundle.getString("path");
        }
    }

    public static void sendLogs(final Activity activity, final boolean z) {
        if (activity == null) {
            return;
        }
        final AlertDialog alertDialog = new AlertDialog(activity, 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                ProfileActivity.lambda$sendLogs$62(AlertDialog.this, z, activity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendLogs$62(final AlertDialog alertDialog, boolean z, final Activity activity) {
        ZipOutputStream zipOutputStream;
        try {
            File logsDir = AndroidUtilities.getLogsDir();
            if (logsDir == null) {
                Objects.requireNonNull(alertDialog);
                AndroidUtilities.runOnUIThread(new AlertDialog$$ExternalSyntheticLambda6(alertDialog));
                return;
            }
            final File file = new File(logsDir, "logs.zip");
            if (file.exists()) {
                file.delete();
            }
            ArrayList arrayList = new ArrayList();
            for (File file2 : logsDir.listFiles()) {
                arrayList.add(file2);
            }
            File file3 = new File(ApplicationLoader.getFilesDirFixed(), "malformed_database/");
            if (file3.exists() && file3.isDirectory()) {
                for (File file4 : file3.listFiles()) {
                    arrayList.add(file4);
                }
            }
            final boolean[] zArr = new boolean[1];
            long currentTimeMillis = System.currentTimeMillis();
            BufferedInputStream bufferedInputStream = null;
            try {
                zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                try {
                    try {
                        byte[] bArr = new byte[65536];
                        for (int i = 0; i < arrayList.size(); i++) {
                            File file5 = (File) arrayList.get(i);
                            if ((file5.getName().contains("cache4") || ((!z && !file5.getName().contains("_mtproto")) || currentTimeMillis - file5.lastModified() <= 86400000)) && file5.exists()) {
                                BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file5), 65536);
                                try {
                                    zipOutputStream.putNextEntry(new ZipEntry(file5.getName()));
                                    while (true) {
                                        int read = bufferedInputStream2.read(bArr, 0, 65536);
                                        if (read == -1) {
                                            break;
                                        }
                                        zipOutputStream.write(bArr, 0, read);
                                    }
                                    bufferedInputStream2.close();
                                } catch (Exception e) {
                                    e = e;
                                    bufferedInputStream = bufferedInputStream2;
                                    e.printStackTrace();
                                    if (bufferedInputStream != null) {
                                        bufferedInputStream.close();
                                    }
                                    if (zipOutputStream != null) {
                                        zipOutputStream.close();
                                    }
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda33
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            ProfileActivity.lambda$sendLogs$61(AlertDialog.this, zArr, activity, file);
                                        }
                                    });
                                } catch (Throwable th) {
                                    th = th;
                                    bufferedInputStream = bufferedInputStream2;
                                    if (bufferedInputStream != null) {
                                        bufferedInputStream.close();
                                    }
                                    if (zipOutputStream != null) {
                                        zipOutputStream.close();
                                    }
                                    throw th;
                                }
                            }
                        }
                        zArr[0] = true;
                    } catch (Exception e2) {
                        e = e2;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                e = e3;
                zipOutputStream = null;
            } catch (Throwable th3) {
                th = th3;
                zipOutputStream = null;
            }
            zipOutputStream.close();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.lambda$sendLogs$61(AlertDialog.this, zArr, activity, file);
                }
            });
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendLogs$61(AlertDialog alertDialog, boolean[] zArr, Activity activity, File file) {
        Uri fromFile;
        try {
            alertDialog.dismiss();
        } catch (Exception unused) {
        }
        if (!zArr[0]) {
            if (activity != null) {
                Toast.makeText(activity, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred), 0).show();
                return;
            }
            return;
        }
        int i = Build.VERSION.SDK_INT;
        if (i >= 24) {
            fromFile = FileProvider.getUriForFile(activity, ApplicationLoader.getApplicationId() + ".provider", file);
        } else {
            fromFile = Uri.fromFile(file);
        }
        Intent intent = new Intent("android.intent.action.SEND");
        if (i >= 24) {
            intent.addFlags(1);
        }
        intent.setType("message/rfc822");
        intent.putExtra("android.intent.extra.EMAIL", "");
        intent.putExtra("android.intent.extra.SUBJECT", "Logs from " + LocaleController.getInstance().formatterStats.format(System.currentTimeMillis()));
        intent.putExtra("android.intent.extra.STREAM", fromFile);
        if (activity != null) {
            try {
                activity.startActivityForResult(Intent.createChooser(intent, "Select email application."), 500);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View headerCell;
            TextDetailCell textDetailCell;
            String str;
            switch (i) {
                case 1:
                    headerCell = new HeaderCell(this.mContext, 23, ProfileActivity.this.resourcesProvider);
                    break;
                case 2:
                case 19:
                    TextDetailCell textDetailCell2 = new TextDetailCell(this.mContext, ProfileActivity.this.resourcesProvider, i == 19);
                    textDetailCell2.setContentDescriptionValueFirst(true);
                    textDetailCell = textDetailCell2;
                    headerCell = textDetailCell;
                    break;
                case 3:
                    ProfileActivity profileActivity = ProfileActivity.this;
                    Context context = this.mContext;
                    ProfileActivity profileActivity2 = ProfileActivity.this;
                    headerCell = profileActivity.aboutLinkCell = new AboutLinkCell(context, profileActivity2, profileActivity2.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.ListAdapter.1
                        @Override // org.telegram.ui.Cells.AboutLinkCell
                        protected void didPressUrl(String str2, Browser.Progress progress) {
                            ProfileActivity.this.openUrl(str2, progress);
                        }

                        @Override // org.telegram.ui.Cells.AboutLinkCell
                        protected void didResizeEnd() {
                            ProfileActivity.this.layoutManager.mIgnoreTopPadding = false;
                        }

                        @Override // org.telegram.ui.Cells.AboutLinkCell
                        protected void didResizeStart() {
                            ProfileActivity.this.layoutManager.mIgnoreTopPadding = true;
                        }
                    };
                    break;
                case 4:
                    headerCell = new TextCell(this.mContext, ProfileActivity.this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.ListAdapter.2
                        @Override // org.telegram.ui.Cells.TextCell
                        protected int processColor(int i2) {
                            return ProfileActivity.this.applyPeerColor(i2, false);
                        }
                    };
                    break;
                case 5:
                    headerCell = new DividerCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    headerCell.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(4.0f), 0, 0);
                    break;
                case 6:
                    headerCell = new NotificationsCheckCell(this.mContext, 23, 70, false, ProfileActivity.this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.ListAdapter.3
                        @Override // org.telegram.ui.Cells.NotificationsCheckCell
                        protected int processColor(int i2) {
                            return ProfileActivity.this.applyPeerColor(i2, false);
                        }
                    };
                    break;
                case 7:
                    headerCell = new ShadowSectionCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
                case 8:
                    headerCell = new UserCell(this.mContext, ProfileActivity.this.addMemberRow == -1 ? 9 : 6, 0, true, ProfileActivity.this.resourcesProvider);
                    break;
                case 9:
                case 10:
                case 14:
                case 16:
                default:
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext, 10, ProfileActivity.this.resourcesProvider);
                    textInfoPrivacyCell.getTextView().setGravity(1);
                    textInfoPrivacyCell.getTextView().setTextColor(ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteGrayText3));
                    textInfoPrivacyCell.getTextView().setMovementMethod(null);
                    try {
                        PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                        int i2 = packageInfo.versionCode;
                        int i3 = i2 / 10;
                        int i4 = i2 % 10;
                        if (i4 == 1 || i4 == 2) {
                            str = "store bundled " + Build.CPU_ABI + " " + Build.CPU_ABI2;
                        } else if (ApplicationLoader.isStandaloneBuild()) {
                            str = "direct " + Build.CPU_ABI + " " + Build.CPU_ABI2;
                        } else {
                            str = "universal " + Build.CPU_ABI + " " + Build.CPU_ABI2;
                        }
                        textInfoPrivacyCell.setText(LocaleController.formatString("TelegramVersion", R.string.TelegramVersion, String.format(Locale.US, "v%s (%d) %s", packageInfo.versionName, Integer.valueOf(i3), str)));
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    textInfoPrivacyCell.getTextView().setPadding(0, AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundGrayShadow)));
                    textDetailCell = textInfoPrivacyCell;
                    headerCell = textDetailCell;
                    break;
                case 11:
                    headerCell = new View(this, this.mContext) { // from class: org.telegram.ui.ProfileActivity.ListAdapter.4
                        @Override // android.view.View
                        protected void onMeasure(int i5, int i6) {
                            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i5), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                        }
                    };
                    break;
                case 12:
                    headerCell = new View(this.mContext) { // from class: org.telegram.ui.ProfileActivity.ListAdapter.5
                        private int lastPaddingHeight = 0;
                        private int lastListViewHeight = 0;

                        @Override // android.view.View
                        protected void onMeasure(int i5, int i6) {
                            if (this.lastListViewHeight != ProfileActivity.this.listView.getMeasuredHeight()) {
                                this.lastPaddingHeight = 0;
                            }
                            this.lastListViewHeight = ProfileActivity.this.listView.getMeasuredHeight();
                            int childCount = ProfileActivity.this.listView.getChildCount();
                            if (childCount != ProfileActivity.this.listAdapter.getItemCount()) {
                                setMeasuredDimension(ProfileActivity.this.listView.getMeasuredWidth(), this.lastPaddingHeight);
                                return;
                            }
                            int i7 = 0;
                            for (int i8 = 0; i8 < childCount; i8++) {
                                int childAdapterPosition = ProfileActivity.this.listView.getChildAdapterPosition(ProfileActivity.this.listView.getChildAt(i8));
                                if (childAdapterPosition >= 0 && childAdapterPosition != ProfileActivity.this.bottomPaddingRow) {
                                    i7 += ProfileActivity.this.listView.getChildAt(i8).getMeasuredHeight();
                                }
                            }
                            int measuredHeight = (((((BaseFragment) ProfileActivity.this).fragmentView == null ? 0 : ((BaseFragment) ProfileActivity.this).fragmentView.getMeasuredHeight()) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) - i7;
                            if (measuredHeight > AndroidUtilities.dp(88.0f)) {
                                measuredHeight = 0;
                            }
                            int i9 = measuredHeight > 0 ? measuredHeight : 0;
                            int measuredWidth = ProfileActivity.this.listView.getMeasuredWidth();
                            this.lastPaddingHeight = i9;
                            setMeasuredDimension(measuredWidth, i9);
                        }
                    };
                    headerCell.setBackground(new ColorDrawable(0));
                    break;
                case 13:
                    if (ProfileActivity.this.sharedMediaLayout.getParent() != null) {
                        ((ViewGroup) ProfileActivity.this.sharedMediaLayout.getParent()).removeView(ProfileActivity.this.sharedMediaLayout);
                    }
                    headerCell = ProfileActivity.this.sharedMediaLayout;
                    break;
                case 15:
                    headerCell = new SettingsSuggestionCell(this.mContext, ProfileActivity.this.resourcesProvider) { // from class: org.telegram.ui.ProfileActivity.ListAdapter.6
                        @Override // org.telegram.ui.Cells.SettingsSuggestionCell
                        protected void onYesClick(int i5) {
                            NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
                            ProfileActivity profileActivity3 = ProfileActivity.this;
                            int i6 = NotificationCenter.newSuggestionsAvailable;
                            notificationCenter.removeObserver(profileActivity3, i6);
                            ProfileActivity.this.getMessagesController().removeSuggestion(0L, i5 == 0 ? "VALIDATE_PHONE_NUMBER" : "VALIDATE_PASSWORD");
                            ProfileActivity.this.getNotificationCenter().addObserver(ProfileActivity.this, i6);
                            if (i5 == 0) {
                                int unused = ProfileActivity.this.phoneSuggestionRow;
                            } else {
                                int unused2 = ProfileActivity.this.passwordSuggestionRow;
                            }
                            ProfileActivity.this.updateListAnimated(false);
                        }

                        @Override // org.telegram.ui.Cells.SettingsSuggestionCell
                        protected void onNoClick(int i5) {
                            if (i5 == 0) {
                                ProfileActivity.this.presentFragment(new ActionIntroActivity(3));
                            } else {
                                ProfileActivity.this.presentFragment(new TwoStepVerificationSetupActivity(8, null));
                            }
                        }
                    };
                    break;
                case 17:
                    headerCell = new TextInfoPrivacyCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
                case 18:
                    headerCell = new ProfilePremiumCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
                case 20:
                    headerCell = new TextCheckCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
            }
            if (i != 13) {
                headerCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            }
            return new RecyclerListView.Holder(headerCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.itemView == ProfileActivity.this.sharedMediaLayout) {
                ProfileActivity.this.sharedMediaLayoutAttached = true;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.itemView == ProfileActivity.this.sharedMediaLayout) {
                ProfileActivity.this.sharedMediaLayoutAttached = false;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:123:0x0325 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:126:0x0332  */
        /* JADX WARN: Removed duplicated region for block: B:127:0x0337  */
        /* JADX WARN: Removed duplicated region for block: B:490:0x0d8f  */
        /* JADX WARN: Removed duplicated region for block: B:491:0x0dc5  */
        /* JADX WARN: Type inference failed for: r0v28, types: [org.telegram.tgnet.TLRPC$ChatParticipant] */
        /* JADX WARN: Type inference failed for: r0v33, types: [org.telegram.tgnet.TLRPC$ChatParticipant] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            String str;
            String string2;
            String str2;
            String str3;
            String str4;
            ArrayList<TLRPC$TL_username> arrayList;
            String str5;
            String sb;
            boolean z;
            String string3;
            String str6;
            String formatTTLString;
            boolean z2;
            int i2;
            String str7;
            String string4;
            String formatString;
            String string5;
            TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant;
            String string6;
            String string7;
            int itemViewType = viewHolder.getItemViewType();
            boolean z3 = true;
            r6 = true;
            boolean z4 = true;
            boolean z5 = true;
            boolean z6 = true;
            r6 = true;
            boolean z7 = true;
            if (itemViewType == 1) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == ProfileActivity.this.infoHeaderRow) {
                    if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup && ProfileActivity.this.channelInfoRow != -1) {
                        headerCell.setText(LocaleController.getString("ReportChatDescription", R.string.ReportChatDescription));
                    } else {
                        headerCell.setText(LocaleController.getString("Info", R.string.Info));
                    }
                } else if (i != ProfileActivity.this.membersHeaderRow) {
                    if (i != ProfileActivity.this.settingsSectionRow2) {
                        if (i != ProfileActivity.this.numberSectionRow) {
                            if (i != ProfileActivity.this.helpHeaderRow) {
                                if (i == ProfileActivity.this.debugHeaderRow) {
                                    headerCell.setText(LocaleController.getString("SettingsDebug", R.string.SettingsDebug));
                                }
                            } else {
                                headerCell.setText(LocaleController.getString("SettingsHelp", R.string.SettingsHelp));
                            }
                        } else {
                            headerCell.setText(LocaleController.getString("Account", R.string.Account));
                        }
                    } else {
                        headerCell.setText(LocaleController.getString("SETTINGS", R.string.SETTINGS));
                    }
                } else {
                    headerCell.setText(LocaleController.getString("ChannelMembers", R.string.ChannelMembers));
                }
                ProfileActivity profileActivity = ProfileActivity.this;
                headerCell.setTextColor(profileActivity.applyPeerColor(profileActivity.getThemedColor(Theme.key_windowBackgroundWhiteBlueHeader), false));
                return;
            }
            if (itemViewType != 2) {
                if (itemViewType == 3) {
                    AboutLinkCell aboutLinkCell = (AboutLinkCell) viewHolder.itemView;
                    if (i == ProfileActivity.this.userInfoRow) {
                        TLRPC$User user = ProfileActivity.this.userInfo.user != null ? ProfileActivity.this.userInfo.user : ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userInfo.id));
                        if (!ProfileActivity.this.isBot && (user == null || !user.premium || ProfileActivity.this.userInfo.about == null)) {
                            z4 = false;
                        }
                        aboutLinkCell.setTextAndValue(ProfileActivity.this.userInfo.about, LocaleController.getString("UserBio", R.string.UserBio), z4);
                        return;
                    } else if (i == ProfileActivity.this.channelInfoRow) {
                        String str8 = ProfileActivity.this.chatInfo.about;
                        while (str8.contains("\n\n\n")) {
                            str8 = str8.replace("\n\n\n", "\n\n");
                        }
                        aboutLinkCell.setText(str8, (!ChatObject.isChannel(ProfileActivity.this.currentChat) || ProfileActivity.this.currentChat.megagroup) ? false : false);
                        return;
                    } else if (i == ProfileActivity.this.bioRow) {
                        if (ProfileActivity.this.userInfo == null || !TextUtils.isEmpty(ProfileActivity.this.userInfo.about)) {
                            aboutLinkCell.setTextAndValue(ProfileActivity.this.userInfo == null ? LocaleController.getString("Loading", R.string.Loading) : ProfileActivity.this.userInfo.about, LocaleController.getString("UserBio", R.string.UserBio), ProfileActivity.this.getUserConfig().isPremium());
                            ProfileActivity profileActivity2 = ProfileActivity.this;
                            profileActivity2.currentBio = profileActivity2.userInfo != null ? ProfileActivity.this.userInfo.about : null;
                        } else {
                            aboutLinkCell.setTextAndValue(LocaleController.getString("UserBio", R.string.UserBio), LocaleController.getString("UserBioDetail", R.string.UserBioDetail), false);
                            ProfileActivity.this.currentBio = null;
                        }
                        aboutLinkCell.setMoreButtonDisabled(true);
                        return;
                    } else {
                        return;
                    }
                }
                if (itemViewType != 4) {
                    if (itemViewType == 6) {
                        NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                        if (i == ProfileActivity.this.notificationsRow) {
                            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileActivity.this).currentAccount);
                            long j = ProfileActivity.this.dialogId != 0 ? ProfileActivity.this.dialogId : ProfileActivity.this.userId != 0 ? ProfileActivity.this.userId : -ProfileActivity.this.chatId;
                            String sharedPrefKey = NotificationsController.getSharedPrefKey(j, ProfileActivity.this.topicId);
                            boolean z8 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey, false);
                            boolean contains = notificationsSettings.contains(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
                            int i3 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 0);
                            int i4 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + sharedPrefKey, 0);
                            if (i3 == 3 && i4 != Integer.MAX_VALUE) {
                                int currentTime = i4 - ProfileActivity.this.getConnectionsManager().getCurrentTime();
                                if (currentTime <= 0) {
                                    if (z8) {
                                        string5 = LocaleController.getString("NotificationsCustom", R.string.NotificationsCustom);
                                    } else {
                                        string5 = LocaleController.getString("NotificationsOn", R.string.NotificationsOn);
                                    }
                                    string4 = string5;
                                    z2 = true;
                                } else {
                                    if (currentTime < 3600) {
                                        formatString = LocaleController.formatString("WillUnmuteIn", R.string.WillUnmuteIn, LocaleController.formatPluralString("Minutes", currentTime / 60, new Object[0]));
                                    } else if (currentTime < 86400) {
                                        formatString = LocaleController.formatString("WillUnmuteIn", R.string.WillUnmuteIn, LocaleController.formatPluralString("Hours", (int) Math.ceil((currentTime / 60.0f) / 60.0f), new Object[0]));
                                    } else if (currentTime < 31536000) {
                                        formatString = LocaleController.formatString("WillUnmuteIn", R.string.WillUnmuteIn, LocaleController.formatPluralString("Days", (int) Math.ceil(((currentTime / 60.0f) / 60.0f) / 24.0f), new Object[0]));
                                    } else {
                                        z2 = false;
                                        string4 = null;
                                    }
                                    string4 = formatString;
                                    z2 = false;
                                }
                            } else if (i3 == 0) {
                                if (!contains) {
                                    z2 = ProfileActivity.this.getNotificationsController().isGlobalNotificationsEnabled(j);
                                    if (z2 || !z8) {
                                        if (z2) {
                                            i2 = R.string.NotificationsOff;
                                            str7 = "NotificationsOff";
                                        } else {
                                            i2 = R.string.NotificationsOn;
                                            str7 = "NotificationsOn";
                                        }
                                        string4 = LocaleController.getString(str7, i2);
                                    } else {
                                        string4 = LocaleController.getString("NotificationsCustom", R.string.NotificationsCustom);
                                    }
                                }
                                z2 = true;
                                if (z2) {
                                }
                                if (z2) {
                                }
                                string4 = LocaleController.getString(str7, i2);
                            } else {
                                if (i3 != 1) {
                                    z2 = false;
                                    if (z2) {
                                    }
                                    if (z2) {
                                    }
                                    string4 = LocaleController.getString(str7, i2);
                                }
                                z2 = true;
                                if (z2) {
                                }
                                if (z2) {
                                }
                                string4 = LocaleController.getString(str7, i2);
                            }
                            if (string4 == null) {
                                string4 = LocaleController.getString("NotificationsOff", R.string.NotificationsOff);
                            }
                            HashSet<Integer> hashSet = ProfileActivity.this.notificationsExceptionTopics;
                            if (hashSet != null && !hashSet.isEmpty()) {
                                string4 = String.format(Locale.US, LocaleController.getPluralString("NotificationTopicExceptionsDesctription", ProfileActivity.this.notificationsExceptionTopics.size()), string4, Integer.valueOf(ProfileActivity.this.notificationsExceptionTopics.size()));
                            }
                            notificationsCheckCell.setAnimationsEnabled(ProfileActivity.this.fragmentOpened);
                            notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("Notifications", R.string.Notifications), string4, z2, false);
                            return;
                        }
                        return;
                    } else if (itemViewType == 7) {
                        View view = viewHolder.itemView;
                        view.setTag(Integer.valueOf(i));
                        if ((i == ProfileActivity.this.infoSectionRow && ProfileActivity.this.lastSectionRow == -1 && ProfileActivity.this.secretSettingsSectionRow == -1 && ProfileActivity.this.sharedMediaRow == -1 && ProfileActivity.this.membersSectionRow == -1) || i == ProfileActivity.this.secretSettingsSectionRow || i == ProfileActivity.this.lastSectionRow || (i == ProfileActivity.this.membersSectionRow && ProfileActivity.this.lastSectionRow == -1 && ProfileActivity.this.sharedMediaRow == -1)) {
                            view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundGrayShadow)));
                            return;
                        } else {
                            view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundGrayShadow)));
                            return;
                        }
                    } else if (itemViewType == 8) {
                        UserCell userCell = (UserCell) viewHolder.itemView;
                        try {
                            tLRPC$TL_chatChannelParticipant = !ProfileActivity.this.visibleSortedUsers.isEmpty() ? (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(((Integer) ProfileActivity.this.visibleSortedUsers.get(i - ProfileActivity.this.membersStartRow)).intValue()) : (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(i - ProfileActivity.this.membersStartRow);
                        } catch (Exception e) {
                            FileLog.e(e);
                            tLRPC$TL_chatChannelParticipant = null;
                        }
                        if (tLRPC$TL_chatChannelParticipant != null) {
                            if (tLRPC$TL_chatChannelParticipant instanceof TLRPC$TL_chatChannelParticipant) {
                                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = tLRPC$TL_chatChannelParticipant.channelParticipant;
                                if (!TextUtils.isEmpty(tLRPC$ChannelParticipant.rank)) {
                                    string7 = tLRPC$ChannelParticipant.rank;
                                } else if (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator) {
                                    string7 = LocaleController.getString("ChannelCreator", R.string.ChannelCreator);
                                } else {
                                    if (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) {
                                        string7 = LocaleController.getString("ChannelAdmin", R.string.ChannelAdmin);
                                    }
                                    string6 = null;
                                }
                                string6 = string7;
                            } else if (tLRPC$TL_chatChannelParticipant instanceof TLRPC$TL_chatParticipantCreator) {
                                string6 = LocaleController.getString("ChannelCreator", R.string.ChannelCreator);
                            } else {
                                if (tLRPC$TL_chatChannelParticipant instanceof TLRPC$TL_chatParticipantAdmin) {
                                    string6 = LocaleController.getString("ChannelAdmin", R.string.ChannelAdmin);
                                }
                                string6 = null;
                            }
                            userCell.setAdminRole(string6);
                            userCell.setData(ProfileActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$TL_chatChannelParticipant.user_id)), null, null, 0, i != ProfileActivity.this.membersEndRow - 1);
                            return;
                        }
                        return;
                    } else if (itemViewType == 12) {
                        viewHolder.itemView.requestLayout();
                        return;
                    } else if (itemViewType == 15) {
                        ((SettingsSuggestionCell) viewHolder.itemView).setType(i != ProfileActivity.this.passwordSuggestionRow ? 0 : 1);
                        return;
                    } else {
                        switch (itemViewType) {
                            case 17:
                                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                                textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundGrayShadow)));
                                textInfoPrivacyCell.setText(LocaleController.getString("BotAddToGroupOrChannelInfo", R.string.BotAddToGroupOrChannelInfo));
                                return;
                            case 18:
                                break;
                            case 19:
                                break;
                            case 20:
                                ((TextCheckCell) viewHolder.itemView).setTextAndCheck(LocaleController.getString("Notifications", R.string.Notifications), !ProfileActivity.this.getMessagesController().isDialogMuted(ProfileActivity.this.getDialogId(), ProfileActivity.this.topicId), false);
                                return;
                            default:
                                return;
                        }
                    }
                }
                TextCell textCell = (TextCell) viewHolder.itemView;
                int i5 = Theme.key_windowBackgroundWhiteGrayIcon;
                int i6 = Theme.key_windowBackgroundWhiteBlackText;
                textCell.setColors(i5, i6);
                textCell.setTag(Integer.valueOf(i6));
                if (i != ProfileActivity.this.settingsTimerRow) {
                    if (i != ProfileActivity.this.unblockRow) {
                        if (i != ProfileActivity.this.settingsKeyRow) {
                            if (i != ProfileActivity.this.joinRow) {
                                if (i == ProfileActivity.this.subscribersRow) {
                                    if (ProfileActivity.this.chatInfo != null) {
                                        if (!ChatObject.isChannel(ProfileActivity.this.currentChat) || ProfileActivity.this.currentChat.megagroup) {
                                            textCell.setTextAndValueAndIcon(LocaleController.getString("ChannelMembers", R.string.ChannelMembers), String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.participants_count)), R.drawable.msg_groups, i != ProfileActivity.this.membersSectionRow - 1);
                                        } else {
                                            textCell.setTextAndValueAndIcon(LocaleController.getString("ChannelSubscribers", R.string.ChannelSubscribers), String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.participants_count)), R.drawable.msg_groups, i != ProfileActivity.this.membersSectionRow - 1);
                                        }
                                    } else if (!ChatObject.isChannel(ProfileActivity.this.currentChat) || ProfileActivity.this.currentChat.megagroup) {
                                        textCell.setTextAndIcon(LocaleController.getString("ChannelMembers", R.string.ChannelMembers), R.drawable.msg_groups, i != ProfileActivity.this.membersSectionRow - 1);
                                    } else {
                                        textCell.setTextAndIcon(LocaleController.getString("ChannelSubscribers", R.string.ChannelSubscribers), R.drawable.msg_groups, i != ProfileActivity.this.membersSectionRow - 1);
                                    }
                                } else if (i == ProfileActivity.this.subscribersRequestsRow) {
                                    if (ProfileActivity.this.chatInfo != null) {
                                        textCell.setTextAndValueAndIcon(LocaleController.getString("SubscribeRequests", R.string.SubscribeRequests), String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.requests_pending)), R.drawable.msg_requests, i != ProfileActivity.this.membersSectionRow - 1);
                                    }
                                } else if (i == ProfileActivity.this.administratorsRow) {
                                    if (ProfileActivity.this.chatInfo != null) {
                                        textCell.setTextAndValueAndIcon(LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators), String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.admins_count)), R.drawable.msg_admins, i != ProfileActivity.this.membersSectionRow - 1);
                                    } else {
                                        textCell.setTextAndIcon(LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators), R.drawable.msg_admins, i != ProfileActivity.this.membersSectionRow - 1);
                                    }
                                } else if (i == ProfileActivity.this.settingsRow) {
                                    textCell.setTextAndIcon(LocaleController.getString("ChannelAdminSettings", R.string.ChannelAdminSettings), R.drawable.msg_customize, i != ProfileActivity.this.membersSectionRow - 1);
                                } else if (i == ProfileActivity.this.blockedUsersRow) {
                                    if (ProfileActivity.this.chatInfo != null) {
                                        textCell.setTextAndValueAndIcon(LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist), String.format("%d", Integer.valueOf(Math.max(ProfileActivity.this.chatInfo.banned_count, ProfileActivity.this.chatInfo.kicked_count))), R.drawable.msg_user_remove, i != ProfileActivity.this.membersSectionRow - 1);
                                    } else {
                                        textCell.setTextAndIcon(LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist), R.drawable.msg_user_remove, i != ProfileActivity.this.membersSectionRow - 1);
                                    }
                                } else if (i != ProfileActivity.this.addMemberRow) {
                                    if (i != ProfileActivity.this.sendMessageRow) {
                                        if (i != ProfileActivity.this.addToContactsRow) {
                                            if (i == ProfileActivity.this.reportReactionRow) {
                                                TLRPC$Chat chat = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(-ProfileActivity.this.reportReactionFromDialogId));
                                                if (chat != null && ChatObject.canBlockUsers(chat)) {
                                                    textCell.setTextAndIcon(LocaleController.getString("ReportReactionAndBan", R.string.ReportReactionAndBan), R.drawable.msg_block2, false);
                                                } else {
                                                    textCell.setTextAndIcon(LocaleController.getString("ReportReaction", R.string.ReportReaction), R.drawable.msg_report, false);
                                                }
                                                int i7 = Theme.key_text_RedBold;
                                                int i8 = Theme.key_text_RedRegular;
                                                textCell.setColors(i7, i8);
                                                textCell.setColors(i7, i8);
                                            } else if (i != ProfileActivity.this.reportRow) {
                                                if (i != ProfileActivity.this.languageRow) {
                                                    if (i != ProfileActivity.this.notificationRow) {
                                                        if (i != ProfileActivity.this.privacyRow) {
                                                            if (i != ProfileActivity.this.dataRow) {
                                                                if (i != ProfileActivity.this.chatRow) {
                                                                    if (i != ProfileActivity.this.filtersRow) {
                                                                        if (i != ProfileActivity.this.stickersRow) {
                                                                            if (i != ProfileActivity.this.liteModeRow) {
                                                                                if (i != ProfileActivity.this.questionRow) {
                                                                                    if (i != ProfileActivity.this.faqRow) {
                                                                                        if (i != ProfileActivity.this.policyRow) {
                                                                                            if (i != ProfileActivity.this.sendLogsRow) {
                                                                                                if (i != ProfileActivity.this.sendLastLogsRow) {
                                                                                                    if (i == ProfileActivity.this.clearLogsRow) {
                                                                                                        textCell.setText(LocaleController.getString("DebugClearLogs", R.string.DebugClearLogs), ProfileActivity.this.switchBackendRow != -1);
                                                                                                    } else if (i != ProfileActivity.this.switchBackendRow) {
                                                                                                        if (i != ProfileActivity.this.devicesRow) {
                                                                                                            if (i == ProfileActivity.this.setAvatarRow) {
                                                                                                                ProfileActivity.this.cellCameraDrawable.setCustomEndFrame(86);
                                                                                                                ProfileActivity.this.cellCameraDrawable.setCurrentFrame(85, false);
                                                                                                                textCell.setTextAndIcon(LocaleController.getString("SetProfilePhoto", R.string.SetProfilePhoto), (Drawable) ProfileActivity.this.cellCameraDrawable, false);
                                                                                                                textCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                                                                                                                textCell.getImageView().setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
                                                                                                                textCell.setImageLeft(12);
                                                                                                                ProfileActivity.this.setAvatarCell = textCell;
                                                                                                            } else if (i != ProfileActivity.this.addToGroupButtonRow) {
                                                                                                                if (i == ProfileActivity.this.premiumRow) {
                                                                                                                    textCell.setTextAndIcon(LocaleController.getString("TelegramPremium", R.string.TelegramPremium), (Drawable) new AnimatedEmojiDrawable.WrapSizeDrawable(PremiumGradient.getInstance().premiumStarMenuDrawable, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f)), false);
                                                                                                                    textCell.setImageLeft(23);
                                                                                                                }
                                                                                                            } else {
                                                                                                                textCell.setTextAndIcon(LocaleController.getString("AddToGroupOrChannel", R.string.AddToGroupOrChannel), R.drawable.msg_groups_create, false);
                                                                                                                textCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                                                                                                            }
                                                                                                        } else {
                                                                                                            textCell.setTextAndIcon(LocaleController.getString("Devices", R.string.Devices), R.drawable.msg2_devices, true);
                                                                                                        }
                                                                                                    } else {
                                                                                                        textCell.setText("Switch Backend", false);
                                                                                                    }
                                                                                                } else {
                                                                                                    textCell.setText(LocaleController.getString("DebugSendLastLogs", R.string.DebugSendLastLogs), true);
                                                                                                }
                                                                                            } else {
                                                                                                textCell.setText(LocaleController.getString("DebugSendLogs", R.string.DebugSendLogs), true);
                                                                                            }
                                                                                        } else {
                                                                                            textCell.setTextAndIcon(LocaleController.getString("PrivacyPolicy", R.string.PrivacyPolicy), R.drawable.msg2_policy, false);
                                                                                        }
                                                                                    } else {
                                                                                        textCell.setTextAndIcon(LocaleController.getString("TelegramFAQ", R.string.TelegramFAQ), R.drawable.msg2_help, true);
                                                                                    }
                                                                                } else {
                                                                                    textCell.setTextAndIcon(LocaleController.getString("AskAQuestion", R.string.AskAQuestion), R.drawable.msg2_ask_question, true);
                                                                                }
                                                                            } else {
                                                                                textCell.setTextAndIcon(LocaleController.getString(R.string.PowerUsage), R.drawable.msg2_battery, true);
                                                                            }
                                                                        } else {
                                                                            textCell.setTextAndIcon(LocaleController.getString(R.string.StickersName), R.drawable.msg2_sticker, true);
                                                                        }
                                                                    } else {
                                                                        textCell.setTextAndIcon(LocaleController.getString("Filters", R.string.Filters), R.drawable.msg2_folder, true);
                                                                    }
                                                                } else {
                                                                    textCell.setTextAndIcon(LocaleController.getString("ChatSettings", R.string.ChatSettings), R.drawable.msg2_discussion, true);
                                                                }
                                                            } else {
                                                                textCell.setTextAndIcon(LocaleController.getString("DataSettings", R.string.DataSettings), R.drawable.msg2_data, true);
                                                            }
                                                        } else {
                                                            textCell.setTextAndIcon(LocaleController.getString("PrivacySettings", R.string.PrivacySettings), R.drawable.msg2_secret, true);
                                                        }
                                                    } else {
                                                        textCell.setTextAndIcon(LocaleController.getString("NotificationsAndSounds", R.string.NotificationsAndSounds), R.drawable.msg2_notifications, true);
                                                    }
                                                } else {
                                                    textCell.setTextAndValueAndIcon(LocaleController.getString("Language", R.string.Language), LocaleController.getCurrentLanguageName(), false, R.drawable.msg2_language, false);
                                                    textCell.setImageLeft(23);
                                                }
                                            } else {
                                                textCell.setText(LocaleController.getString("ReportUserLocation", R.string.ReportUserLocation), false);
                                                int i9 = Theme.key_text_RedRegular;
                                                textCell.setColors(-1, i9);
                                                textCell.setColors(-1, i9);
                                            }
                                        } else {
                                            textCell.setTextAndIcon(LocaleController.getString(R.string.AddToContacts), R.drawable.msg_contact_add, false);
                                            textCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                                        }
                                    } else {
                                        textCell.setText(LocaleController.getString("SendMessageLocation", R.string.SendMessageLocation), true);
                                    }
                                } else {
                                    textCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                                    textCell.setTextAndIcon(LocaleController.getString("AddMember", R.string.AddMember), R.drawable.msg_contact_add, ProfileActivity.this.membersSectionRow == -1);
                                }
                            } else {
                                textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText2);
                                if (ProfileActivity.this.currentChat.megagroup) {
                                    textCell.setText(LocaleController.getString("ProfileJoinGroup", R.string.ProfileJoinGroup), false);
                                } else {
                                    textCell.setText(LocaleController.getString("ProfileJoinChannel", R.string.ProfileJoinChannel), false);
                                }
                            }
                        } else {
                            IdenticonDrawable identiconDrawable = new IdenticonDrawable();
                            identiconDrawable.setEncryptedChat(ProfileActivity.this.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(ProfileActivity.this.dialogId))));
                            textCell.setTextAndValueDrawable(LocaleController.getString("EncryptionKey", R.string.EncryptionKey), identiconDrawable, false);
                        }
                    } else {
                        textCell.setText(LocaleController.getString("Unblock", R.string.Unblock), false);
                        textCell.setColors(-1, Theme.key_text_RedRegular);
                    }
                } else {
                    int i10 = ProfileActivity.this.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(ProfileActivity.this.dialogId))).ttl;
                    if (i10 == 0) {
                        formatTTLString = LocaleController.getString("ShortMessageLifetimeForever", R.string.ShortMessageLifetimeForever);
                    } else {
                        formatTTLString = LocaleController.formatTTLString(i10);
                    }
                    textCell.setTextAndValue(LocaleController.getString("MessageLifetime", R.string.MessageLifetime), formatTTLString, false, false);
                }
                AnimatedTextView animatedTextView = textCell.valueTextView;
                ProfileActivity profileActivity3 = ProfileActivity.this;
                animatedTextView.setTextColor(profileActivity3.applyPeerColor(profileActivity3.getThemedColor(Theme.key_windowBackgroundWhiteValueText), false));
                return;
            }
            TextDetailCell textDetailCell = (TextDetailCell) viewHolder.itemView;
            if (i == ProfileActivity.this.phoneRow) {
                TLRPC$User user2 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                if (user2 != null && !TextUtils.isEmpty(ProfileActivity.this.vcardPhone)) {
                    string3 = PhoneFormat.getInstance().format("+" + ProfileActivity.this.vcardPhone);
                    str6 = ProfileActivity.this.vcardPhone;
                } else if (user2 != null && !TextUtils.isEmpty(user2.phone)) {
                    string3 = PhoneFormat.getInstance().format("+" + user2.phone);
                    str6 = user2.phone;
                } else {
                    string3 = LocaleController.getString("PhoneHidden", R.string.PhoneHidden);
                    str6 = null;
                }
                ProfileActivity.this.isFragmentPhoneNumber = (str6 == null || !str6.matches("888\\d{8}")) ? false : false;
                textDetailCell.setTextAndValue(string3, LocaleController.getString(ProfileActivity.this.isFragmentPhoneNumber ? R.string.AnonymousNumber : R.string.PhoneMobile), false);
            } else if (i != ProfileActivity.this.usernameRow) {
                if (i == ProfileActivity.this.locationRow) {
                    if (ProfileActivity.this.chatInfo != null && (ProfileActivity.this.chatInfo.location instanceof TLRPC$TL_channelLocation)) {
                        textDetailCell.setTextAndValue(((TLRPC$TL_channelLocation) ProfileActivity.this.chatInfo.location).address, LocaleController.getString("AttachLocation", R.string.AttachLocation), false);
                    }
                } else if (i == ProfileActivity.this.numberRow) {
                    TLRPC$User currentUser = UserConfig.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getCurrentUser();
                    if (currentUser != null && (str2 = currentUser.phone) != null && str2.length() != 0) {
                        string2 = PhoneFormat.getInstance().format("+" + currentUser.phone);
                    } else {
                        string2 = LocaleController.getString("NumberUnknown", R.string.NumberUnknown);
                    }
                    textDetailCell.setTextAndValue(string2, LocaleController.getString("TapToChangePhone", R.string.TapToChangePhone), true);
                    textDetailCell.setContentDescriptionValueFirst(false);
                } else if (i == ProfileActivity.this.setUsernameRow) {
                    TLRPC$User currentUser2 = UserConfig.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getCurrentUser();
                    CharSequence string8 = LocaleController.getString("Username", R.string.Username);
                    if (currentUser2 != null && currentUser2.usernames.size() > 0) {
                        int i11 = 0;
                        while (true) {
                            if (i11 < currentUser2.usernames.size()) {
                                TLRPC$TL_username tLRPC$TL_username = currentUser2.usernames.get(i11);
                                if (tLRPC$TL_username == null || !tLRPC$TL_username.active || TextUtils.isEmpty(tLRPC$TL_username.username)) {
                                    i11++;
                                } else {
                                    str = tLRPC$TL_username.username;
                                }
                            } else {
                                str = null;
                            }
                        }
                        if (str == null) {
                            str = currentUser2.username;
                        }
                        if (str == null || TextUtils.isEmpty(str)) {
                            string = LocaleController.getString("UsernameEmpty", R.string.UsernameEmpty);
                        } else {
                            string = "@" + str;
                        }
                        string8 = alsoUsernamesString(str, currentUser2.usernames, string8);
                    } else {
                        String publicUsername = UserObject.getPublicUsername(currentUser2);
                        if (currentUser2 != null && !TextUtils.isEmpty(publicUsername)) {
                            string = "@" + publicUsername;
                        } else {
                            string = LocaleController.getString("UsernameEmpty", R.string.UsernameEmpty);
                        }
                    }
                    textDetailCell.setTextAndValue(string, string8, true);
                    textDetailCell.setContentDescriptionValueFirst(true);
                }
            } else {
                ArrayList<TLRPC$TL_username> arrayList2 = new ArrayList<>();
                String str9 = "";
                if (ProfileActivity.this.userId != 0) {
                    TLRPC$User user3 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                    if (user3 != null) {
                        arrayList2.addAll(user3.usernames);
                    }
                    str5 = (user3 == null || TextUtils.isEmpty(user3.username)) ? null : user3.username;
                    arrayList = user3 == null ? new ArrayList<>() : new ArrayList<>(user3.usernames);
                    if (TextUtils.isEmpty(str5)) {
                        int i12 = 0;
                        while (true) {
                            if (i12 < arrayList.size()) {
                                TLRPC$TL_username tLRPC$TL_username2 = arrayList.get(i12);
                                if (tLRPC$TL_username2 == null || !tLRPC$TL_username2.active || TextUtils.isEmpty(tLRPC$TL_username2.username)) {
                                    i12++;
                                } else {
                                    str5 = tLRPC$TL_username2.username;
                                }
                            }
                        }
                    }
                    str3 = LocaleController.getString("Username", R.string.Username);
                    if (str5 != null) {
                        str4 = "@" + str5;
                    } else {
                        str4 = "—";
                    }
                } else if (ProfileActivity.this.currentChat != null) {
                    TLRPC$Chat chat2 = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(ProfileActivity.this.chatId));
                    String publicUsername2 = ChatObject.getPublicUsername(chat2);
                    arrayList2.addAll(chat2.usernames);
                    if (ChatObject.isPublic(chat2)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(ProfileActivity.this.getMessagesController().linkPrefix);
                        sb2.append("/");
                        sb2.append(publicUsername2);
                        if (ProfileActivity.this.topicId != 0) {
                            str9 = "/" + ProfileActivity.this.topicId;
                        }
                        sb2.append(str9);
                        sb = sb2.toString();
                        str3 = LocaleController.getString("InviteLink", R.string.InviteLink);
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(ProfileActivity.this.getMessagesController().linkPrefix);
                        sb3.append("/c/");
                        sb3.append(chat2.id);
                        if (ProfileActivity.this.topicId != 0) {
                            str9 = "/" + ProfileActivity.this.topicId;
                        }
                        sb3.append(str9);
                        sb = sb3.toString();
                        str3 = LocaleController.getString("InviteLinkPrivate", R.string.InviteLinkPrivate);
                        z3 = false;
                    }
                    String str10 = sb;
                    arrayList = arrayList2;
                    str5 = publicUsername2;
                    str4 = str10;
                    z7 = z3;
                } else {
                    str3 = "";
                    str4 = str3;
                    z7 = false;
                    arrayList = new ArrayList<>();
                    str5 = null;
                }
                textDetailCell.setTextAndValue(str4, alsoUsernamesString(str5, arrayList, str3), ProfileActivity.this.isTopic);
                z = z7;
                if (!z) {
                    Drawable drawable = ContextCompat.getDrawable(textDetailCell.getContext(), R.drawable.msg_qr_mini);
                    ProfileActivity profileActivity4 = ProfileActivity.this;
                    drawable.setColorFilter(new PorterDuffColorFilter(profileActivity4.applyPeerColor(profileActivity4.getThemedColor(Theme.key_switch2TrackChecked), false), PorterDuff.Mode.MULTIPLY));
                    textDetailCell.setImage(drawable, LocaleController.getString("GetQRCode", R.string.GetQRCode));
                    final ProfileActivity profileActivity5 = ProfileActivity.this;
                    textDetailCell.setImageClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProfileActivity$ListAdapter$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ProfileActivity.access$31900(ProfileActivity.this, view2);
                        }
                    });
                } else {
                    textDetailCell.setImage(null);
                    textDetailCell.setImageClickListener(null);
                }
                textDetailCell.setTag(Integer.valueOf(i));
                LinkSpanDrawable.LinksTextView linksTextView = textDetailCell.valueTextView;
                ProfileActivity profileActivity6 = ProfileActivity.this;
                linksTextView.setTextColor(profileActivity6.applyPeerColor(profileActivity6.getThemedColor(Theme.key_windowBackgroundWhiteGrayText2), false));
            }
            z = false;
            if (!z) {
            }
            textDetailCell.setTag(Integer.valueOf(i));
            LinkSpanDrawable.LinksTextView linksTextView2 = textDetailCell.valueTextView;
            ProfileActivity profileActivity62 = ProfileActivity.this;
            linksTextView2.setTextColor(profileActivity62.applyPeerColor(profileActivity62.getThemedColor(Theme.key_windowBackgroundWhiteGrayText2), false));
        }

        private CharSequence alsoUsernamesString(String str, ArrayList<TLRPC$TL_username> arrayList, CharSequence charSequence) {
            if (arrayList == null) {
                return charSequence;
            }
            ArrayList arrayList2 = new ArrayList(arrayList);
            int i = 0;
            while (i < arrayList2.size()) {
                if (!((TLRPC$TL_username) arrayList2.get(i)).active || (str != null && str.equals(((TLRPC$TL_username) arrayList2.get(i)).username))) {
                    arrayList2.remove(i);
                    i--;
                }
                i++;
            }
            SpannableStringBuilder spannableStringBuilder = charSequence;
            if (arrayList2.size() > 0) {
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    final String str2 = ((TLRPC$TL_username) arrayList2.get(i2)).username;
                    SpannableString spannableString = new SpannableString("@" + str2);
                    spannableString.setSpan(new ClickableSpan() { // from class: org.telegram.ui.ProfileActivity.ListAdapter.7
                        @Override // android.text.style.ClickableSpan
                        public void onClick(View view) {
                            String str3 = ProfileActivity.this.getMessagesController().linkPrefix + "/" + str2;
                            if (ProfileActivity.this.currentChat == null || !ProfileActivity.this.currentChat.noforwards) {
                                AndroidUtilities.addToClipboard(str3);
                                ProfileActivity.this.undoView.showWithAction(0L, 56, (Runnable) null);
                            }
                        }

                        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                        public void updateDrawState(TextPaint textPaint) {
                            textPaint.setUnderlineText(false);
                        }
                    }, 0, spannableString.length(), 33);
                    ProfileActivity profileActivity = ProfileActivity.this;
                    spannableString.setSpan(new ForegroundColorSpan(profileActivity.applyPeerColor(profileActivity.getThemedColor(Theme.key_chat_messageLinkIn), false)), 0, spannableString.length(), 33);
                    spannableStringBuilder2.append((CharSequence) spannableString);
                    if (i2 < arrayList2.size() - 1) {
                        spannableStringBuilder2.append((CharSequence) ", ");
                    }
                }
                String string = LocaleController.getString("UsernameAlso", R.string.UsernameAlso);
                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(string);
                int indexOf = string.indexOf("%1$s");
                spannableStringBuilder = spannableStringBuilder3;
                if (indexOf >= 0) {
                    spannableStringBuilder3.replace(indexOf, indexOf + 4, (CharSequence) spannableStringBuilder2);
                    spannableStringBuilder = spannableStringBuilder3;
                }
            }
            return spannableStringBuilder;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getAdapterPosition() == ProfileActivity.this.setAvatarRow) {
                ProfileActivity.this.setAvatarCell = null;
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            if (ProfileActivity.this.notificationRow != -1) {
                int adapterPosition = viewHolder.getAdapterPosition();
                return adapterPosition == ProfileActivity.this.notificationRow || adapterPosition == ProfileActivity.this.numberRow || adapterPosition == ProfileActivity.this.privacyRow || adapterPosition == ProfileActivity.this.languageRow || adapterPosition == ProfileActivity.this.setUsernameRow || adapterPosition == ProfileActivity.this.bioRow || adapterPosition == ProfileActivity.this.versionRow || adapterPosition == ProfileActivity.this.dataRow || adapterPosition == ProfileActivity.this.chatRow || adapterPosition == ProfileActivity.this.questionRow || adapterPosition == ProfileActivity.this.devicesRow || adapterPosition == ProfileActivity.this.filtersRow || adapterPosition == ProfileActivity.this.stickersRow || adapterPosition == ProfileActivity.this.faqRow || adapterPosition == ProfileActivity.this.policyRow || adapterPosition == ProfileActivity.this.sendLogsRow || adapterPosition == ProfileActivity.this.sendLastLogsRow || adapterPosition == ProfileActivity.this.clearLogsRow || adapterPosition == ProfileActivity.this.switchBackendRow || adapterPosition == ProfileActivity.this.setAvatarRow || adapterPosition == ProfileActivity.this.addToGroupButtonRow || adapterPosition == ProfileActivity.this.premiumRow || adapterPosition == ProfileActivity.this.liteModeRow;
            }
            View view = viewHolder.itemView;
            if (view instanceof UserCell) {
                Object currentObject = ((UserCell) view).getCurrentObject();
                if ((currentObject instanceof TLRPC$User) && UserObject.isUserSelf((TLRPC$User) currentObject)) {
                    return false;
                }
            }
            int itemViewType = viewHolder.getItemViewType();
            return (itemViewType == 1 || itemViewType == 5 || itemViewType == 7 || itemViewType == 11 || itemViewType == 12 || itemViewType == 13 || itemViewType == 9 || itemViewType == 10) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ProfileActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == ProfileActivity.this.infoHeaderRow || i == ProfileActivity.this.membersHeaderRow || i == ProfileActivity.this.settingsSectionRow2 || i == ProfileActivity.this.numberSectionRow || i == ProfileActivity.this.helpHeaderRow || i == ProfileActivity.this.debugHeaderRow) {
                return 1;
            }
            if (i == ProfileActivity.this.phoneRow || i == ProfileActivity.this.locationRow || i == ProfileActivity.this.numberRow) {
                return 2;
            }
            if (i == ProfileActivity.this.usernameRow || i == ProfileActivity.this.setUsernameRow) {
                return 19;
            }
            if (i == ProfileActivity.this.userInfoRow || i == ProfileActivity.this.channelInfoRow || i == ProfileActivity.this.bioRow) {
                return 3;
            }
            if (i == ProfileActivity.this.settingsTimerRow || i == ProfileActivity.this.settingsKeyRow || i == ProfileActivity.this.reportRow || i == ProfileActivity.this.reportReactionRow || i == ProfileActivity.this.subscribersRow || i == ProfileActivity.this.subscribersRequestsRow || i == ProfileActivity.this.administratorsRow || i == ProfileActivity.this.settingsRow || i == ProfileActivity.this.blockedUsersRow || i == ProfileActivity.this.addMemberRow || i == ProfileActivity.this.joinRow || i == ProfileActivity.this.unblockRow || i == ProfileActivity.this.sendMessageRow || i == ProfileActivity.this.notificationRow || i == ProfileActivity.this.privacyRow || i == ProfileActivity.this.languageRow || i == ProfileActivity.this.dataRow || i == ProfileActivity.this.chatRow || i == ProfileActivity.this.questionRow || i == ProfileActivity.this.devicesRow || i == ProfileActivity.this.filtersRow || i == ProfileActivity.this.stickersRow || i == ProfileActivity.this.faqRow || i == ProfileActivity.this.policyRow || i == ProfileActivity.this.sendLogsRow || i == ProfileActivity.this.sendLastLogsRow || i == ProfileActivity.this.clearLogsRow || i == ProfileActivity.this.switchBackendRow || i == ProfileActivity.this.setAvatarRow || i == ProfileActivity.this.addToGroupButtonRow || i == ProfileActivity.this.addToContactsRow || i == ProfileActivity.this.liteModeRow) {
                return 4;
            }
            if (i == ProfileActivity.this.notificationsDividerRow) {
                return 5;
            }
            if (i == ProfileActivity.this.notificationsRow) {
                return 6;
            }
            if (i == ProfileActivity.this.notificationsSimpleRow) {
                return 20;
            }
            if (i == ProfileActivity.this.infoSectionRow || i == ProfileActivity.this.lastSectionRow || i == ProfileActivity.this.membersSectionRow || i == ProfileActivity.this.secretSettingsSectionRow || i == ProfileActivity.this.settingsSectionRow || i == ProfileActivity.this.devicesSectionRow || i == ProfileActivity.this.helpSectionCell || i == ProfileActivity.this.setAvatarSectionRow || i == ProfileActivity.this.passwordSuggestionSectionRow || i == ProfileActivity.this.phoneSuggestionSectionRow || i == ProfileActivity.this.premiumSectionsRow || i == ProfileActivity.this.reportDividerRow) {
                return 7;
            }
            if (i < ProfileActivity.this.membersStartRow || i >= ProfileActivity.this.membersEndRow) {
                if (i == ProfileActivity.this.emptyRow) {
                    return 11;
                }
                if (i == ProfileActivity.this.bottomPaddingRow) {
                    return 12;
                }
                if (i == ProfileActivity.this.sharedMediaRow) {
                    return 13;
                }
                if (i == ProfileActivity.this.versionRow) {
                    return 14;
                }
                if (i == ProfileActivity.this.passwordSuggestionRow || i == ProfileActivity.this.phoneSuggestionRow) {
                    return 15;
                }
                if (i == ProfileActivity.this.addToGroupInfoRow) {
                    return 17;
                }
                return i == ProfileActivity.this.premiumRow ? 18 : 0;
            }
            return 8;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private TLRPC$WebPage faqWebPage;
        private String lastSearchString;
        private boolean loadingFaqPage;
        private Context mContext;
        private Runnable searchRunnable;
        private boolean searchWas;
        private SearchResult[] searchArray = onCreateSearchArray();
        private ArrayList<MessagesController.FaqSearchResult> faqSearchArray = new ArrayList<>();
        private ArrayList<CharSequence> resultNames = new ArrayList<>();
        private ArrayList<SearchResult> searchResults = new ArrayList<>();
        private ArrayList<MessagesController.FaqSearchResult> faqSearchResults = new ArrayList<>();
        private ArrayList<Object> recentSearches = new ArrayList<>();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class SearchResult {
            private int guid;
            private int iconResId;
            private int num;
            private Runnable openRunnable;
            private String[] path;
            private String rowName;
            private String searchTitle;

            public SearchResult(SearchAdapter searchAdapter, int i, String str, int i2, Runnable runnable) {
                this(i, str, null, null, null, i2, runnable);
            }

            public SearchResult(SearchAdapter searchAdapter, int i, String str, String str2, int i2, Runnable runnable) {
                this(i, str, null, str2, null, i2, runnable);
            }

            public SearchResult(SearchAdapter searchAdapter, int i, String str, String str2, String str3, int i2, Runnable runnable) {
                this(i, str, str2, str3, null, i2, runnable);
            }

            public SearchResult(int i, String str, String str2, String str3, String str4, int i2, Runnable runnable) {
                this.guid = i;
                this.searchTitle = str;
                this.rowName = str2;
                this.openRunnable = runnable;
                this.iconResId = i2;
                if (str3 != null && str4 != null) {
                    this.path = new String[]{str3, str4};
                } else if (str3 != null) {
                    this.path = new String[]{str3};
                }
            }

            public boolean equals(Object obj) {
                return (obj instanceof SearchResult) && this.guid == ((SearchResult) obj).guid;
            }

            public String toString() {
                SerializedData serializedData = new SerializedData();
                serializedData.writeInt32(this.num);
                serializedData.writeInt32(1);
                serializedData.writeInt32(this.guid);
                return Utilities.bytesToHex(serializedData.toByteArray());
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void open() {
                this.openRunnable.run();
                AndroidUtilities.scrollToFragmentRow(((BaseFragment) ProfileActivity.this).parentLayout, this.rowName);
            }
        }

        public SearchAdapter(Context context) {
            this.mContext = context;
            updateSearchArray();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateSearchArray() {
            String[] strArr;
            SearchResult searchResult;
            HashMap hashMap = new HashMap();
            int i = 0;
            while (true) {
                SearchResult[] searchResultArr = this.searchArray;
                if (i >= searchResultArr.length) {
                    break;
                }
                if (searchResultArr[i] != null) {
                    hashMap.put(Integer.valueOf(searchResultArr[i].guid), this.searchArray[i]);
                }
                i++;
            }
            Set<String> stringSet = MessagesController.getGlobalMainSettings().getStringSet("settingsSearchRecent2", null);
            if (stringSet != null) {
                for (String str : stringSet) {
                    try {
                        SerializedData serializedData = new SerializedData(Utilities.hexToBytes(str));
                        int readInt32 = serializedData.readInt32(false);
                        int readInt322 = serializedData.readInt32(false);
                        if (readInt322 == 0) {
                            String readString = serializedData.readString(false);
                            int readInt323 = serializedData.readInt32(false);
                            if (readInt323 > 0) {
                                strArr = new String[readInt323];
                                for (int i2 = 0; i2 < readInt323; i2++) {
                                    strArr[i2] = serializedData.readString(false);
                                }
                            } else {
                                strArr = null;
                            }
                            MessagesController.FaqSearchResult faqSearchResult = new MessagesController.FaqSearchResult(readString, strArr, serializedData.readString(false));
                            faqSearchResult.num = readInt32;
                            this.recentSearches.add(faqSearchResult);
                        } else if (readInt322 == 1 && (searchResult = (SearchResult) hashMap.get(Integer.valueOf(serializedData.readInt32(false)))) != null) {
                            searchResult.num = readInt32;
                            this.recentSearches.add(searchResult);
                        }
                    } catch (Exception unused) {
                    }
                }
            }
            Collections.sort(this.recentSearches, new Comparator() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda146
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$updateSearchArray$0;
                    lambda$updateSearchArray$0 = ProfileActivity.SearchAdapter.this.lambda$updateSearchArray$0(obj, obj2);
                    return lambda$updateSearchArray$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int lambda$updateSearchArray$0(Object obj, Object obj2) {
            int num = getNum(obj);
            int num2 = getNum(obj2);
            if (num < num2) {
                return -1;
            }
            return num > num2 ? 1 : 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public SearchResult[] onCreateSearchArray() {
            SearchResult[] searchResultArr = new SearchResult[143];
            searchResultArr[0] = new SearchResult(this, 500, LocaleController.getString("EditName", R.string.EditName), 0, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda59
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$1();
                }
            });
            searchResultArr[1] = new SearchResult(this, 501, LocaleController.getString("ChangePhoneNumber", R.string.ChangePhoneNumber), 0, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda109
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$2();
                }
            });
            searchResultArr[2] = new SearchResult(this, 502, LocaleController.getString("AddAnotherAccount", R.string.AddAnotherAccount), 0, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda66
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$3();
                }
            });
            searchResultArr[3] = new SearchResult(this, 503, LocaleController.getString("UserBio", R.string.UserBio), 0, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$4();
                }
            });
            String string = LocaleController.getString(R.string.AddPhoto);
            final ProfileActivity profileActivity = ProfileActivity.this;
            searchResultArr[4] = new SearchResult(this, 504, string, 0, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda145
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.access$7400(ProfileActivity.this);
                }
            });
            int i = R.string.NotificationsAndSounds;
            String string2 = LocaleController.getString("NotificationsAndSounds", i);
            int i2 = R.drawable.msg_notifications;
            searchResultArr[5] = new SearchResult(this, 1, string2, i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$6();
                }
            });
            searchResultArr[6] = new SearchResult(this, 2, LocaleController.getString("NotificationsPrivateChats", R.string.NotificationsPrivateChats), LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda46
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$7();
                }
            });
            searchResultArr[7] = new SearchResult(this, 3, LocaleController.getString("NotificationsGroups", R.string.NotificationsGroups), LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda140
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$8();
                }
            });
            searchResultArr[8] = new SearchResult(this, 4, LocaleController.getString("NotificationsChannels", R.string.NotificationsChannels), LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$9();
                }
            });
            searchResultArr[9] = new SearchResult(this, 5, LocaleController.getString("VoipNotificationSettings", R.string.VoipNotificationSettings), "callsSectionRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda139
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$10();
                }
            });
            searchResultArr[10] = new SearchResult(this, 6, LocaleController.getString("BadgeNumber", R.string.BadgeNumber), "badgeNumberSection", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda141
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$11();
                }
            });
            searchResultArr[11] = new SearchResult(this, 7, LocaleController.getString("InAppNotifications", R.string.InAppNotifications), "inappSectionRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda79
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$12();
                }
            });
            searchResultArr[12] = new SearchResult(this, 8, LocaleController.getString("ContactJoined", R.string.ContactJoined), "contactJoinedRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda95
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$13();
                }
            });
            searchResultArr[13] = new SearchResult(this, 9, LocaleController.getString("PinnedMessages", R.string.PinnedMessages), "pinnedMessageRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda55
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$14();
                }
            });
            searchResultArr[14] = new SearchResult(this, 10, LocaleController.getString("ResetAllNotifications", R.string.ResetAllNotifications), "resetNotificationsRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda120
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$15();
                }
            });
            searchResultArr[15] = new SearchResult(this, 11, LocaleController.getString(R.string.NotificationsService), "notificationsServiceRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda116
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$16();
                }
            });
            searchResultArr[16] = new SearchResult(this, 12, LocaleController.getString(R.string.NotificationsServiceConnection), "notificationsServiceConnectionRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$17();
                }
            });
            searchResultArr[17] = new SearchResult(this, 13, LocaleController.getString(R.string.RepeatNotifications), "repeatRow", LocaleController.getString("NotificationsAndSounds", i), i2, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda115
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$18();
                }
            });
            int i3 = R.string.PrivacySettings;
            String string3 = LocaleController.getString("PrivacySettings", i3);
            int i4 = R.drawable.msg_secret;
            searchResultArr[18] = new SearchResult(this, 100, string3, i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$19();
                }
            });
            String string4 = LocaleController.getString("TwoStepVerification", R.string.TwoStepVerification);
            String string5 = LocaleController.getString("PrivacySettings", i3);
            int i5 = R.drawable.msg2_secret;
            searchResultArr[19] = new SearchResult(this, 109, string4, string5, i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$20();
                }
            });
            searchResultArr[20] = new SearchResult(this, 124, LocaleController.getString(R.string.AutoDeleteMessages), LocaleController.getString(i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda82
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$21();
                }
            });
            searchResultArr[21] = new SearchResult(this, 108, LocaleController.getString("Passcode", R.string.Passcode), LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda65
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$22();
                }
            });
            searchResultArr[22] = SharedConfig.hasEmailLogin ? new SearchResult(this, 125, LocaleController.getString(R.string.EmailLogin), "emailLoginRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$23();
                }
            }) : null;
            searchResultArr[23] = new SearchResult(this, FileLoader.MEDIA_DIR_VIDEO_PUBLIC, LocaleController.getString("BlockedUsers", R.string.BlockedUsers), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda111
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$24();
                }
            });
            searchResultArr[24] = new SearchResult(this, 110, LocaleController.getString("SessionsTitle", R.string.SessionsTitle), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$25();
                }
            });
            searchResultArr[25] = new SearchResult(this, 105, LocaleController.getString("PrivacyPhone", R.string.PrivacyPhone), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$26();
                }
            });
            searchResultArr[26] = new SearchResult(this, 102, LocaleController.getString("PrivacyLastSeen", R.string.PrivacyLastSeen), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda113
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$27();
                }
            });
            searchResultArr[27] = new SearchResult(this, 103, LocaleController.getString("PrivacyProfilePhoto", R.string.PrivacyProfilePhoto), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$28();
                }
            });
            searchResultArr[28] = new SearchResult(this, 104, LocaleController.getString("PrivacyForwards", R.string.PrivacyForwards), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda102
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$29();
                }
            });
            searchResultArr[29] = new SearchResult(this, 122, LocaleController.getString("PrivacyP2P", R.string.PrivacyP2P), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$30();
                }
            });
            int i6 = R.string.Calls;
            searchResultArr[30] = new SearchResult(this, 106, LocaleController.getString("Calls", i6), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda117
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$31();
                }
            });
            searchResultArr[31] = new SearchResult(this, 107, LocaleController.getString("GroupsAndChannels", R.string.GroupsAndChannels), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$32();
                }
            });
            searchResultArr[32] = new SearchResult(this, 123, LocaleController.getString("PrivacyVoiceMessages", R.string.PrivacyVoiceMessages), LocaleController.getString("PrivacySettings", i3), i4, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$33();
                }
            });
            searchResultArr[33] = ProfileActivity.this.getMessagesController().autoarchiveAvailable ? new SearchResult(this, 121, LocaleController.getString("ArchiveAndMute", R.string.ArchiveAndMute), "newChatsRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$34();
                }
            }) : null;
            searchResultArr[34] = new SearchResult(this, 112, LocaleController.getString("DeleteAccountIfAwayFor2", R.string.DeleteAccountIfAwayFor2), "deleteAccountRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda81
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$35();
                }
            });
            searchResultArr[35] = new SearchResult(this, 113, LocaleController.getString("PrivacyPaymentsClear", R.string.PrivacyPaymentsClear), "paymentsClearRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda122
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$36();
                }
            });
            searchResultArr[36] = new SearchResult(this, 114, LocaleController.getString("WebSessionsTitle", R.string.WebSessionsTitle), LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda133
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$37();
                }
            });
            searchResultArr[37] = new SearchResult(this, 115, LocaleController.getString("SyncContactsDelete", R.string.SyncContactsDelete), "contactsDeleteRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$38();
                }
            });
            searchResultArr[38] = new SearchResult(this, 116, LocaleController.getString("SyncContacts", R.string.SyncContacts), "contactsSyncRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$39();
                }
            });
            searchResultArr[39] = new SearchResult(this, 117, LocaleController.getString("SuggestContacts", R.string.SuggestContacts), "contactsSuggestRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$40();
                }
            });
            searchResultArr[40] = new SearchResult(this, 118, LocaleController.getString("MapPreviewProvider", R.string.MapPreviewProvider), "secretMapRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda63
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$41();
                }
            });
            searchResultArr[41] = new SearchResult(this, 119, LocaleController.getString("SecretWebPage", R.string.SecretWebPage), "secretWebpageRow", LocaleController.getString("PrivacySettings", i3), i5, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$42();
                }
            });
            int i7 = R.string.Devices;
            String string6 = LocaleController.getString(i7);
            int i8 = R.drawable.msg2_devices;
            searchResultArr[42] = new SearchResult(this, 120, string6, i8, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$43();
                }
            });
            searchResultArr[43] = new SearchResult(this, 121, LocaleController.getString(R.string.TerminateAllSessions), "terminateAllSessionsRow", LocaleController.getString(i7), i8, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda86
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$44();
                }
            });
            searchResultArr[44] = new SearchResult(this, 122, LocaleController.getString(R.string.LinkDesktopDevice), LocaleController.getString(i7), i8, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda83
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$45();
                }
            });
            int i9 = R.string.DataSettings;
            String string7 = LocaleController.getString("DataSettings", i9);
            int i10 = R.drawable.msg2_data;
            searchResultArr[45] = new SearchResult(this, 200, string7, i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda87
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$46();
                }
            });
            searchResultArr[46] = new SearchResult(this, 201, LocaleController.getString("DataUsage", R.string.DataUsage), "usageSectionRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda90
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$47();
                }
            });
            int i11 = R.string.StorageUsage;
            searchResultArr[47] = new SearchResult(this, 202, LocaleController.getString("StorageUsage", i11), LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$48();
                }
            });
            searchResultArr[48] = new SearchResult(203, LocaleController.getString("KeepMedia", R.string.KeepMedia), "keepMediaRow", LocaleController.getString("DataSettings", i9), LocaleController.getString("StorageUsage", i11), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda137
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$49();
                }
            });
            searchResultArr[49] = new SearchResult(204, LocaleController.getString("ClearMediaCache", R.string.ClearMediaCache), "cacheRow", LocaleController.getString("DataSettings", i9), LocaleController.getString("StorageUsage", i11), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda73
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$50();
                }
            });
            searchResultArr[50] = new SearchResult(205, LocaleController.getString("LocalDatabase", R.string.LocalDatabase), "databaseRow", LocaleController.getString("DataSettings", i9), LocaleController.getString("StorageUsage", i11), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$51();
                }
            });
            searchResultArr[51] = new SearchResult(this, 206, LocaleController.getString("NetworkUsage", R.string.NetworkUsage), LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda126
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$52();
                }
            });
            searchResultArr[52] = new SearchResult(this, 207, LocaleController.getString("AutomaticMediaDownload", R.string.AutomaticMediaDownload), "mediaDownloadSectionRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda67
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$53();
                }
            });
            searchResultArr[53] = new SearchResult(this, 208, LocaleController.getString("WhenUsingMobileData", R.string.WhenUsingMobileData), LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$54();
                }
            });
            searchResultArr[54] = new SearchResult(this, 209, LocaleController.getString("WhenConnectedOnWiFi", R.string.WhenConnectedOnWiFi), LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda64
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$55();
                }
            });
            searchResultArr[55] = new SearchResult(this, 210, LocaleController.getString("WhenRoaming", R.string.WhenRoaming), LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda74
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$56();
                }
            });
            searchResultArr[56] = new SearchResult(this, 211, LocaleController.getString("ResetAutomaticMediaDownload", R.string.ResetAutomaticMediaDownload), "resetDownloadRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$57();
                }
            });
            searchResultArr[57] = new SearchResult(this, 215, LocaleController.getString("Streaming", R.string.Streaming), "streamSectionRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$58();
                }
            });
            searchResultArr[58] = new SearchResult(this, 216, LocaleController.getString("EnableStreaming", R.string.EnableStreaming), "enableStreamRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda107
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$59();
                }
            });
            searchResultArr[59] = new SearchResult(this, 217, LocaleController.getString("Calls", i6), "callsSectionRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda62
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$60();
                }
            });
            searchResultArr[60] = new SearchResult(this, 218, LocaleController.getString("VoipUseLessData", R.string.VoipUseLessData), "useLessDataForCallsRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda101
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$61();
                }
            });
            searchResultArr[61] = new SearchResult(this, 219, LocaleController.getString("VoipQuickReplies", R.string.VoipQuickReplies), "quickRepliesRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$62();
                }
            });
            int i12 = R.string.ProxySettings;
            searchResultArr[62] = new SearchResult(this, 220, LocaleController.getString("ProxySettings", i12), LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda127
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$63();
                }
            });
            searchResultArr[63] = new SearchResult(221, LocaleController.getString("UseProxyForCalls", R.string.UseProxyForCalls), "callsRow", LocaleController.getString("DataSettings", i9), LocaleController.getString("ProxySettings", i12), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$64();
                }
            });
            searchResultArr[64] = new SearchResult(this, 111, LocaleController.getString("PrivacyDeleteCloudDrafts", R.string.PrivacyDeleteCloudDrafts), "clearDraftsRow", LocaleController.getString("DataSettings", i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda68
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$65();
                }
            });
            int i13 = R.string.SaveToGallery;
            searchResultArr[65] = new SearchResult(this, 222, LocaleController.getString(i13), "saveToGallerySectionRow", LocaleController.getString(i9), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda98
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$66();
                }
            });
            searchResultArr[66] = new SearchResult(223, LocaleController.getString(R.string.SaveToGalleryPrivate), "saveToGalleryPeerRow", LocaleController.getString(i9), LocaleController.getString(i13), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda70
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$67();
                }
            });
            searchResultArr[67] = new SearchResult(224, LocaleController.getString(R.string.SaveToGalleryGroups), "saveToGalleryGroupsRow", LocaleController.getString(i9), LocaleController.getString(i13), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda42
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$68();
                }
            });
            searchResultArr[68] = new SearchResult(225, LocaleController.getString(R.string.SaveToGalleryChannels), "saveToGalleryChannelsRow", LocaleController.getString(i9), LocaleController.getString(i13), i10, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda78
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$69();
                }
            });
            int i14 = R.string.ChatSettings;
            String string8 = LocaleController.getString("ChatSettings", i14);
            int i15 = R.drawable.msg2_discussion;
            searchResultArr[69] = new SearchResult(this, 300, string8, i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda72
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$70();
                }
            });
            searchResultArr[70] = new SearchResult(this, 301, LocaleController.getString("TextSizeHeader", R.string.TextSizeHeader), "textSizeHeaderRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda112
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$71();
                }
            });
            searchResultArr[71] = new SearchResult(this, 302, LocaleController.getString(R.string.ChangeChatBackground), LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda96
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$72();
                }
            });
            String string9 = LocaleController.getString("SetColor", R.string.SetColor);
            String string10 = LocaleController.getString("ChatSettings", i14);
            int i16 = R.string.ChatBackground;
            searchResultArr[72] = new SearchResult(303, string9, null, string10, LocaleController.getString("ChatBackground", i16), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$73();
                }
            });
            searchResultArr[73] = new SearchResult(304, LocaleController.getString("ResetChatBackgrounds", R.string.ResetChatBackgrounds), "resetRow", LocaleController.getString("ChatSettings", i14), LocaleController.getString("ChatBackground", i16), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$74();
                }
            });
            searchResultArr[74] = new SearchResult(this, 306, LocaleController.getString("ColorTheme", R.string.ColorTheme), "themeHeaderRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda134
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$75();
                }
            });
            int i17 = R.string.BrowseThemes;
            searchResultArr[75] = new SearchResult(this, 319, LocaleController.getString(i17), null, LocaleController.getString(i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda88
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$76();
                }
            });
            searchResultArr[76] = new SearchResult(320, LocaleController.getString(R.string.CreateNewTheme), "createNewThemeRow", LocaleController.getString(i14), LocaleController.getString(i17), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda108
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$77();
                }
            });
            searchResultArr[77] = new SearchResult(this, 321, LocaleController.getString(R.string.BubbleRadius), "bubbleRadiusHeaderRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda114
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$78();
                }
            });
            searchResultArr[78] = new SearchResult(this, 322, LocaleController.getString(R.string.ChatList), "chatListHeaderRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda43
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$79();
                }
            });
            searchResultArr[79] = new SearchResult(this, 323, LocaleController.getString(R.string.ChatListSwipeGesture), "swipeGestureHeaderRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda61
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$80();
                }
            });
            searchResultArr[80] = new SearchResult(this, 324, LocaleController.getString(R.string.AppIcon), "appIconHeaderRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$81();
                }
            });
            searchResultArr[81] = new SearchResult(this, 305, LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme), LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda131
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$82();
                }
            });
            searchResultArr[82] = new SearchResult(this, 307, LocaleController.getString("ChromeCustomTabs", R.string.ChromeCustomTabs), "customTabsRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$83();
                }
            });
            searchResultArr[83] = new SearchResult(this, 328, LocaleController.getString(R.string.NextMediaTap), "nextMediaTapRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda97
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$84();
                }
            });
            searchResultArr[84] = new SearchResult(this, 327, LocaleController.getString(R.string.RaiseToListen), "raiseToListenRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda85
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$85();
                }
            });
            searchResultArr[85] = new SearchResult(this, 310, LocaleController.getString("RaiseToSpeak", R.string.RaiseToSpeak), "raiseToSpeakRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$86();
                }
            });
            searchResultArr[86] = new SearchResult(this, 326, LocaleController.getString(R.string.PauseMusicOnMedia), "pauseOnMediaRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda71
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$87();
                }
            });
            searchResultArr[87] = new SearchResult(this, 325, LocaleController.getString(R.string.MicrophoneForVoiceMessages), "bluetoothScoRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda130
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$88();
                }
            });
            searchResultArr[88] = new SearchResult(this, 308, LocaleController.getString("DirectShare", R.string.DirectShare), "directShareRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda94
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$89();
                }
            });
            searchResultArr[89] = new SearchResult(this, 311, LocaleController.getString("SendByEnter", R.string.SendByEnter), "sendByEnterRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda136
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$90();
                }
            });
            searchResultArr[90] = new SearchResult(this, 318, LocaleController.getString("DistanceUnits", R.string.DistanceUnits), "distanceRow", LocaleController.getString("ChatSettings", i14), i15, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$91();
                }
            });
            int i18 = R.string.StickersName;
            String string11 = LocaleController.getString(i18);
            int i19 = R.drawable.msg2_sticker;
            searchResultArr[91] = new SearchResult(this, 600, string11, i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$92();
                }
            });
            searchResultArr[92] = new SearchResult(this, 601, LocaleController.getString("SuggestStickers", R.string.SuggestStickers), "suggestRow", LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda52
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$93();
                }
            });
            searchResultArr[93] = new SearchResult(this, 602, LocaleController.getString("FeaturedStickers", R.string.FeaturedStickers), "featuredStickersHeaderRow", LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda84
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$94();
                }
            });
            searchResultArr[94] = new SearchResult(this, 603, LocaleController.getString("Masks", R.string.Masks), null, LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda91
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$95();
                }
            });
            searchResultArr[95] = new SearchResult(this, 604, LocaleController.getString("ArchivedStickers", R.string.ArchivedStickers), null, LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda105
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$96();
                }
            });
            searchResultArr[96] = new SearchResult(this, 605, LocaleController.getString("ArchivedMasks", R.string.ArchivedMasks), null, LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda110
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$97();
                }
            });
            searchResultArr[97] = new SearchResult(this, 606, LocaleController.getString(R.string.LargeEmoji), "largeEmojiRow", LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda103
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$98();
                }
            });
            searchResultArr[98] = new SearchResult(this, 607, LocaleController.getString(R.string.LoopAnimatedStickers), "loopRow", LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$99();
                }
            });
            int i20 = R.string.Emoji;
            String string12 = LocaleController.getString(i20);
            String string13 = LocaleController.getString(i18);
            int i21 = R.drawable.input_smile;
            searchResultArr[99] = new SearchResult(this, 608, string12, null, string13, i21, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$100();
                }
            });
            searchResultArr[100] = new SearchResult(609, LocaleController.getString(R.string.SuggestAnimatedEmoji), "suggestAnimatedEmojiRow", LocaleController.getString(i18), LocaleController.getString(i20), i21, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$101();
                }
            });
            searchResultArr[101] = new SearchResult(610, LocaleController.getString(R.string.FeaturedEmojiPacks), "featuredStickersHeaderRow", LocaleController.getString(i18), LocaleController.getString(i20), i21, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda69
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$102();
                }
            });
            searchResultArr[102] = new SearchResult(this, 611, LocaleController.getString(R.string.DoubleTapSetting), null, LocaleController.getString(i18), i19, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda80
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$103();
                }
            });
            int i22 = R.string.Filters;
            String string14 = LocaleController.getString(i22);
            int i23 = R.drawable.msg2_folder;
            searchResultArr[103] = new SearchResult(this, 700, string14, null, i23, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$104();
                }
            });
            searchResultArr[104] = new SearchResult(this, 701, LocaleController.getString(R.string.CreateNewFilter), "createFilterRow", LocaleController.getString(i22), i23, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda93
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$105();
                }
            });
            searchResultArr[105] = isPremiumFeatureAvailable(-1) ? new SearchResult(this, 800, LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$106();
                }
            }) : null;
            searchResultArr[106] = isPremiumFeatureAvailable(0) ? new SearchResult(this, 801, LocaleController.getString(R.string.PremiumPreviewLimits), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda124
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$107();
                }
            }) : null;
            searchResultArr[107] = isPremiumFeatureAvailable(11) ? new SearchResult(this, 802, LocaleController.getString(R.string.PremiumPreviewEmoji), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda123
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$108();
                }
            }) : null;
            searchResultArr[108] = isPremiumFeatureAvailable(1) ? new SearchResult(this, 803, LocaleController.getString(R.string.PremiumPreviewUploads), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$109();
                }
            }) : null;
            searchResultArr[109] = isPremiumFeatureAvailable(2) ? new SearchResult(this, 804, LocaleController.getString(R.string.PremiumPreviewDownloadSpeed), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$110();
                }
            }) : null;
            searchResultArr[110] = isPremiumFeatureAvailable(8) ? new SearchResult(this, 805, LocaleController.getString(R.string.PremiumPreviewVoiceToText), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda129
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$111();
                }
            }) : null;
            searchResultArr[111] = isPremiumFeatureAvailable(3) ? new SearchResult(this, 806, LocaleController.getString(R.string.PremiumPreviewNoAds), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$112();
                }
            }) : null;
            searchResultArr[112] = isPremiumFeatureAvailable(4) ? new SearchResult(this, 807, LocaleController.getString(R.string.PremiumPreviewReactions), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$113();
                }
            }) : null;
            searchResultArr[113] = isPremiumFeatureAvailable(5) ? new SearchResult(this, 808, LocaleController.getString(R.string.PremiumPreviewStickers), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda128
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$114();
                }
            }) : null;
            searchResultArr[114] = isPremiumFeatureAvailable(9) ? new SearchResult(this, 809, LocaleController.getString(R.string.PremiumPreviewAdvancedChatManagement), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$115();
                }
            }) : null;
            searchResultArr[115] = isPremiumFeatureAvailable(6) ? new SearchResult(this, 810, LocaleController.getString(R.string.PremiumPreviewProfileBadge), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$116();
                }
            }) : null;
            searchResultArr[116] = isPremiumFeatureAvailable(7) ? new SearchResult(this, 811, LocaleController.getString(R.string.PremiumPreviewAnimatedProfiles), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda106
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$117();
                }
            }) : null;
            searchResultArr[117] = isPremiumFeatureAvailable(10) ? new SearchResult(this, 812, LocaleController.getString(R.string.PremiumPreviewAppIcon), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$118();
                }
            }) : null;
            searchResultArr[118] = isPremiumFeatureAvailable(12) ? new SearchResult(this, 813, LocaleController.getString(R.string.PremiumPreviewEmojiStatus), LocaleController.getString(R.string.TelegramPremium), R.drawable.msg_settings_premium, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda89
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$119();
                }
            }) : null;
            int i24 = R.string.PowerUsage;
            String string15 = LocaleController.getString(i24);
            int i25 = R.drawable.msg2_battery;
            searchResultArr[119] = new SearchResult(this, 900, string15, null, i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda118
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$120();
                }
            });
            int i26 = R.string.LiteOptionsStickers;
            searchResultArr[120] = new SearchResult(this, 901, LocaleController.getString(i26), LocaleController.getString(i24), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda77
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$121();
                }
            });
            int i27 = R.string.LiteOptionsAutoplayKeyboard;
            searchResultArr[121] = new SearchResult(902, LocaleController.getString(i27), null, LocaleController.getString(i24), LocaleController.getString(i26), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$122();
                }
            });
            int i28 = R.string.LiteOptionsAutoplayChat;
            searchResultArr[122] = new SearchResult(903, LocaleController.getString(i28), null, LocaleController.getString(i24), LocaleController.getString(i26), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda100
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$123();
                }
            });
            int i29 = R.string.LiteOptionsEmoji;
            searchResultArr[123] = new SearchResult(this, 904, LocaleController.getString(i29), LocaleController.getString(i24), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda119
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$124();
                }
            });
            searchResultArr[124] = new SearchResult(905, LocaleController.getString(i27), null, LocaleController.getString(i24), LocaleController.getString(i29), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda75
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$125();
                }
            });
            searchResultArr[125] = new SearchResult(906, LocaleController.getString(R.string.LiteOptionsAutoplayReactions), null, LocaleController.getString(i24), LocaleController.getString(i29), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$126();
                }
            });
            searchResultArr[126] = new SearchResult(907, LocaleController.getString(i28), null, LocaleController.getString(i24), LocaleController.getString(i29), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda99
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$127();
                }
            });
            int i30 = R.string.LiteOptionsChat;
            searchResultArr[127] = new SearchResult(this, 908, LocaleController.getString(i30), LocaleController.getString(i24), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$128();
                }
            });
            searchResultArr[128] = new SearchResult(909, LocaleController.getString(R.string.LiteOptionsBackground), null, LocaleController.getString(i24), LocaleController.getString(i30), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda76
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$129();
                }
            });
            searchResultArr[129] = new SearchResult(910, LocaleController.getString(R.string.LiteOptionsTopics), null, LocaleController.getString(i24), LocaleController.getString(i30), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda92
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$130();
                }
            });
            searchResultArr[130] = new SearchResult(911, LocaleController.getString(R.string.LiteOptionsSpoiler), null, LocaleController.getString(i24), LocaleController.getString(i30), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda125
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$131();
                }
            });
            searchResultArr[131] = SharedConfig.getDevicePerformanceClass() >= 1 ? new SearchResult(326, LocaleController.getString(R.string.LiteOptionsBlur), null, LocaleController.getString(i24), LocaleController.getString(i30), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda132
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$132();
                }
            }) : null;
            searchResultArr[132] = new SearchResult(912, LocaleController.getString(R.string.LiteOptionsScale), null, LocaleController.getString(i24), LocaleController.getString(i30), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$133();
                }
            });
            searchResultArr[133] = new SearchResult(this, 913, LocaleController.getString(R.string.LiteOptionsCalls), LocaleController.getString(i24), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$134();
                }
            });
            searchResultArr[134] = new SearchResult(this, 214, LocaleController.getString(R.string.LiteOptionsAutoplayVideo), LocaleController.getString(i24), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$135();
                }
            });
            searchResultArr[135] = new SearchResult(this, 213, LocaleController.getString(R.string.LiteOptionsAutoplayGifs), LocaleController.getString(i24), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$136();
                }
            });
            searchResultArr[136] = new SearchResult(this, 914, LocaleController.getString(R.string.LiteSmoothTransitions), LocaleController.getString(i24), i25, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda138
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$137();
                }
            });
            int i31 = R.string.Language;
            String string16 = LocaleController.getString("Language", i31);
            int i32 = R.drawable.msg2_language;
            searchResultArr[137] = new SearchResult(this, 400, string16, i32, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda121
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$138();
                }
            });
            searchResultArr[138] = new SearchResult(this, 405, LocaleController.getString(R.string.ShowTranslateButton), LocaleController.getString(i31), i32, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$139();
                }
            });
            searchResultArr[139] = MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getTranslateController().isContextTranslateEnabled() ? new SearchResult(this, 406, LocaleController.getString(R.string.DoNotTranslate), LocaleController.getString(i31), i32, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$140();
                }
            }) : null;
            String string17 = LocaleController.getString("AskAQuestion", R.string.AskAQuestion);
            int i33 = R.string.SettingsHelp;
            String string18 = LocaleController.getString("SettingsHelp", i33);
            int i34 = R.drawable.msg2_help;
            searchResultArr[140] = new SearchResult(this, 402, string17, string18, i34, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda104
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$141();
                }
            });
            searchResultArr[141] = new SearchResult(this, 403, LocaleController.getString("TelegramFAQ", R.string.TelegramFAQ), LocaleController.getString("SettingsHelp", i33), i34, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda135
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$142();
                }
            });
            searchResultArr[142] = new SearchResult(this, 404, LocaleController.getString("PrivacyPolicy", R.string.PrivacyPolicy), LocaleController.getString("SettingsHelp", i33), i34, new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$onCreateSearchArray$143();
                }
            });
            return searchResultArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$1() {
            ProfileActivity profileActivity = ProfileActivity.this;
            profileActivity.presentFragment(new ChangeNameActivity(profileActivity.resourcesProvider));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$2() {
            ProfileActivity.this.presentFragment(new ActionIntroActivity(3));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$3() {
            int i = 0;
            while (true) {
                if (i >= 4) {
                    i = -1;
                    break;
                } else if (!UserConfig.getInstance(i).isClientActivated()) {
                    break;
                } else {
                    i++;
                }
            }
            if (i >= 0) {
                ProfileActivity.this.presentFragment(new LoginActivity(i));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$4() {
            if (ProfileActivity.this.userInfo != null) {
                ProfileActivity.this.presentFragment(new ChangeBioActivity());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$6() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$7() {
            ProfileActivity.this.presentFragment(new NotificationsCustomSettingsActivity(1, new ArrayList(), null, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$8() {
            ProfileActivity.this.presentFragment(new NotificationsCustomSettingsActivity(0, new ArrayList(), null, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$9() {
            ProfileActivity.this.presentFragment(new NotificationsCustomSettingsActivity(2, new ArrayList(), null, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$10() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$11() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$12() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$13() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$14() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$15() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$16() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$17() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$18() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$19() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$20() {
            ProfileActivity.this.presentFragment(new TwoStepVerificationActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$21() {
            if (ProfileActivity.this.getUserConfig().getGlobalTTl() >= 0) {
                ProfileActivity.this.presentFragment(new AutoDeleteMessagesActivity());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$22() {
            ProfileActivity.this.presentFragment(PasscodeActivity.determineOpenFragment());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$23() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$24() {
            ProfileActivity.this.presentFragment(new PrivacyUsersActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$25() {
            ProfileActivity.this.presentFragment(new SessionsActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$26() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(6, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$27() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(0, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$28() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(4, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$29() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(5, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$30() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(3, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$31() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(2, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$32() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(1, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$33() {
            if (!ProfileActivity.this.getUserConfig().isPremium()) {
                try {
                    ((BaseFragment) ProfileActivity.this).fragmentView.performHapticFeedback(3, 2);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                BulletinFactory.of(ProfileActivity.this).createRestrictVoiceMessagesPremiumBulletin().show();
                return;
            }
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(8, true));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$34() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$35() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$36() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$37() {
            ProfileActivity.this.presentFragment(new SessionsActivity(1));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$38() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$39() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$40() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$41() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$42() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$43() {
            ProfileActivity.this.presentFragment(new SessionsActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$44() {
            ProfileActivity.this.presentFragment(new SessionsActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$45() {
            ProfileActivity.this.presentFragment(new SessionsActivity(0).setHighlightLinkDesktopDevice());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$46() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$47() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$48() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$49() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$50() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$51() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$52() {
            ProfileActivity.this.presentFragment(new DataUsage2Activity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$53() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$54() {
            ProfileActivity.this.presentFragment(new DataAutoDownloadActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$55() {
            ProfileActivity.this.presentFragment(new DataAutoDownloadActivity(1));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$56() {
            ProfileActivity.this.presentFragment(new DataAutoDownloadActivity(2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$57() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$58() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$59() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$60() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$61() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$62() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$63() {
            ProfileActivity.this.presentFragment(new ProxyListActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$64() {
            ProfileActivity.this.presentFragment(new ProxyListActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$65() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$66() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$67() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$68() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$69() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$70() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$71() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$72() {
            ProfileActivity.this.presentFragment(new WallpapersListActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$73() {
            ProfileActivity.this.presentFragment(new WallpapersListActivity(1));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$74() {
            ProfileActivity.this.presentFragment(new WallpapersListActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$75() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$76() {
            ProfileActivity.this.presentFragment(new ThemeActivity(3));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$77() {
            ProfileActivity.this.presentFragment(new ThemeActivity(3));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$78() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$79() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$80() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$81() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$82() {
            ProfileActivity.this.presentFragment(new ThemeActivity(1));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$83() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$84() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$85() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$86() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$87() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$88() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$89() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$90() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$91() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$92() {
            ProfileActivity.this.presentFragment(new StickersActivity(0, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$93() {
            ProfileActivity.this.presentFragment(new StickersActivity(0, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$94() {
            ProfileActivity.this.presentFragment(new StickersActivity(0, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$95() {
            ProfileActivity.this.presentFragment(new StickersActivity(1, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$96() {
            ProfileActivity.this.presentFragment(new ArchivedStickersActivity(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$97() {
            ProfileActivity.this.presentFragment(new ArchivedStickersActivity(1));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$98() {
            ProfileActivity.this.presentFragment(new StickersActivity(0, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$99() {
            ProfileActivity.this.presentFragment(new StickersActivity(0, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$100() {
            ProfileActivity.this.presentFragment(new StickersActivity(5, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$101() {
            ProfileActivity.this.presentFragment(new StickersActivity(5, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$102() {
            ProfileActivity.this.presentFragment(new StickersActivity(5, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$103() {
            ProfileActivity.this.presentFragment(new ReactionsDoubleTapManageActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$104() {
            ProfileActivity.this.presentFragment(new FiltersSetupActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$105() {
            ProfileActivity.this.presentFragment(new FiltersSetupActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$106() {
            ProfileActivity.this.presentFragment(new PremiumPreviewFragment("settings"));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$107() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 0, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$108() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 11, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$109() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 1, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$110() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 2, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$111() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 8, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$112() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 3, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$113() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 4, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$114() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 5, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$115() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 9, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$116() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 6, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$117() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 7, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$118() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 10, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$119() {
            ProfileActivity.this.showDialog(new PremiumFeatureBottomSheet(ProfileActivity.this, 12, false).setForceAbout());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$120() {
            ProfileActivity.this.presentFragment(new LiteModeSettingsActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$121() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.scrollToFlags(3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$122() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(3, true);
            liteModeSettingsActivity.scrollToFlags(1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$123() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(3, true);
            liteModeSettingsActivity.scrollToFlags(2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$124() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAGS_ANIMATED_EMOJI);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$125() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_ANIMATED_EMOJI, true);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$126() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_ANIMATED_EMOJI, true);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$127() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_ANIMATED_EMOJI, true);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAG_ANIMATED_EMOJI_CHAT);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$128() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAGS_CHAT);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$129() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_CHAT, true);
            liteModeSettingsActivity.scrollToFlags(32);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$130() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_CHAT, true);
            liteModeSettingsActivity.scrollToFlags(64);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$131() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_CHAT, true);
            liteModeSettingsActivity.scrollToFlags(128);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$132() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_CHAT, true);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAG_CHAT_BLUR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$133() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.setExpanded(LiteMode.FLAGS_CHAT, true);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAG_CHAT_SCALE);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$134() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAG_CALLS_ANIMATIONS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$135() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.scrollToFlags(1024);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$136() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.scrollToFlags(LiteMode.FLAG_AUTOPLAY_GIFS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$137() {
            LiteModeSettingsActivity liteModeSettingsActivity = new LiteModeSettingsActivity();
            ProfileActivity.this.presentFragment(liteModeSettingsActivity);
            liteModeSettingsActivity.scrollToType(1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$138() {
            ProfileActivity.this.presentFragment(new LanguageSelectActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$139() {
            ProfileActivity.this.presentFragment(new LanguageSelectActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$140() {
            ProfileActivity.this.presentFragment(new LanguageSelectActivity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$141() {
            ProfileActivity profileActivity = ProfileActivity.this;
            profileActivity.showDialog(AlertsCreator.createSupportAlert(profileActivity, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$142() {
            Browser.openUrl(ProfileActivity.this.getParentActivity(), LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateSearchArray$143() {
            Browser.openUrl(ProfileActivity.this.getParentActivity(), LocaleController.getString("PrivacyPolicyUrl", R.string.PrivacyPolicyUrl));
        }

        private boolean isPremiumFeatureAvailable(int i) {
            if (!ProfileActivity.this.getMessagesController().premiumLocked || ProfileActivity.this.getUserConfig().isPremium()) {
                return i == -1 || ProfileActivity.this.getMessagesController().premiumFeaturesTypesToPosition.get(i, -1) != -1;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void loadFaqWebPage() {
            TLRPC$WebPage tLRPC$WebPage = ProfileActivity.this.getMessagesController().faqWebPage;
            this.faqWebPage = tLRPC$WebPage;
            if (tLRPC$WebPage != null) {
                this.faqSearchArray.addAll(ProfileActivity.this.getMessagesController().faqSearchArray);
            }
            if (this.faqWebPage != null || this.loadingFaqPage) {
                return;
            }
            this.loadingFaqPage = true;
            TLRPC$TL_messages_getWebPage tLRPC$TL_messages_getWebPage = new TLRPC$TL_messages_getWebPage();
            tLRPC$TL_messages_getWebPage.url = LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl);
            tLRPC$TL_messages_getWebPage.hash = 0;
            ProfileActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_messages_getWebPage, new RequestDelegate() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda147
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    ProfileActivity.SearchAdapter.this.lambda$loadFaqWebPage$145(tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFaqWebPage$145(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject instanceof TLRPC$TL_messages_webPage) {
                TLRPC$TL_messages_webPage tLRPC$TL_messages_webPage = (TLRPC$TL_messages_webPage) tLObject;
                MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).putUsers(tLRPC$TL_messages_webPage.users, false);
                MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).putChats(tLRPC$TL_messages_webPage.chats, false);
                tLObject = tLRPC$TL_messages_webPage.webpage;
            }
            if (tLObject instanceof TLRPC$WebPage) {
                final ArrayList arrayList = new ArrayList();
                TLRPC$WebPage tLRPC$WebPage = (TLRPC$WebPage) tLObject;
                TLRPC$Page tLRPC$Page = tLRPC$WebPage.cached_page;
                if (tLRPC$Page != null) {
                    int size = tLRPC$Page.blocks.size();
                    for (int i = 0; i < size; i++) {
                        TLRPC$PageBlock tLRPC$PageBlock = tLRPC$WebPage.cached_page.blocks.get(i);
                        if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockList) {
                            String str = null;
                            if (i != 0) {
                                TLRPC$PageBlock tLRPC$PageBlock2 = tLRPC$WebPage.cached_page.blocks.get(i - 1);
                                if (tLRPC$PageBlock2 instanceof TLRPC$TL_pageBlockParagraph) {
                                    str = ArticleViewer.getPlainText(((TLRPC$TL_pageBlockParagraph) tLRPC$PageBlock2).text).toString();
                                }
                            }
                            TLRPC$TL_pageBlockList tLRPC$TL_pageBlockList = (TLRPC$TL_pageBlockList) tLRPC$PageBlock;
                            int size2 = tLRPC$TL_pageBlockList.items.size();
                            for (int i2 = 0; i2 < size2; i2++) {
                                TLRPC$PageListItem tLRPC$PageListItem = tLRPC$TL_pageBlockList.items.get(i2);
                                if (tLRPC$PageListItem instanceof TLRPC$TL_pageListItemText) {
                                    TLRPC$TL_pageListItemText tLRPC$TL_pageListItemText = (TLRPC$TL_pageListItemText) tLRPC$PageListItem;
                                    String url = ArticleViewer.getUrl(tLRPC$TL_pageListItemText.text);
                                    String charSequence = ArticleViewer.getPlainText(tLRPC$TL_pageListItemText.text).toString();
                                    if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(charSequence)) {
                                        arrayList.add(new MessagesController.FaqSearchResult(charSequence, str != null ? new String[]{LocaleController.getString("SettingsSearchFaq", R.string.SettingsSearchFaq), str} : new String[]{LocaleController.getString("SettingsSearchFaq", R.string.SettingsSearchFaq)}, url));
                                    }
                                }
                            }
                        } else if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockAnchor) {
                            break;
                        }
                    }
                    this.faqWebPage = tLRPC$WebPage;
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda144
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileActivity.SearchAdapter.this.lambda$loadFaqWebPage$144(arrayList);
                    }
                });
            }
            this.loadingFaqPage = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFaqWebPage$144(ArrayList arrayList) {
            this.faqSearchArray.addAll(arrayList);
            ProfileActivity.this.getMessagesController().faqSearchArray = arrayList;
            ProfileActivity.this.getMessagesController().faqWebPage = this.faqWebPage;
            if (this.searchWas) {
                return;
            }
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (this.searchWas) {
                return this.searchResults.size() + (this.faqSearchResults.isEmpty() ? 0 : this.faqSearchResults.size() + 1);
            }
            return (this.recentSearches.isEmpty() ? 0 : this.recentSearches.size() + 1) + (this.faqSearchArray.isEmpty() ? 0 : this.faqSearchArray.size() + 1);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString("SettingsFaqSearchTitle", R.string.SettingsFaqSearchTitle));
                    return;
                } else if (itemViewType != 2) {
                    return;
                } else {
                    ((HeaderCell) viewHolder.itemView).setText(LocaleController.getString("SettingsRecent", R.string.SettingsRecent));
                    return;
                }
            }
            SettingsSearchCell settingsSearchCell = (SettingsSearchCell) viewHolder.itemView;
            if (this.searchWas) {
                if (i < this.searchResults.size()) {
                    SearchResult searchResult = this.searchResults.get(i);
                    SearchResult searchResult2 = i > 0 ? this.searchResults.get(i - 1) : null;
                    settingsSearchCell.setTextAndValueAndIcon(this.resultNames.get(i), searchResult.path, (searchResult2 == null || searchResult2.iconResId != searchResult.iconResId) ? searchResult.iconResId : 0, i < this.searchResults.size() - 1);
                    return;
                }
                int size = i - (this.searchResults.size() + 1);
                settingsSearchCell.setTextAndValue(this.resultNames.get(this.searchResults.size() + size), this.faqSearchResults.get(size).path, true, size < this.searchResults.size() - 1);
                return;
            }
            if (!this.recentSearches.isEmpty()) {
                i--;
            }
            if (i < this.recentSearches.size()) {
                Object obj = this.recentSearches.get(i);
                if (obj instanceof SearchResult) {
                    SearchResult searchResult3 = (SearchResult) obj;
                    settingsSearchCell.setTextAndValue(searchResult3.searchTitle, searchResult3.path, false, i < this.recentSearches.size() - 1);
                    return;
                } else if (obj instanceof MessagesController.FaqSearchResult) {
                    MessagesController.FaqSearchResult faqSearchResult = (MessagesController.FaqSearchResult) obj;
                    settingsSearchCell.setTextAndValue(faqSearchResult.title, faqSearchResult.path, true, i < this.recentSearches.size() - 1);
                    return;
                } else {
                    return;
                }
            }
            int size2 = i - (this.recentSearches.size() + 1);
            MessagesController.FaqSearchResult faqSearchResult2 = this.faqSearchArray.get(size2);
            settingsSearchCell.setTextAndValue(faqSearchResult2.title, faqSearchResult2.path, true, size2 < this.recentSearches.size() - 1);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View settingsSearchCell;
            if (i == 0) {
                settingsSearchCell = new SettingsSearchCell(this.mContext);
            } else if (i == 1) {
                settingsSearchCell = new GraySectionCell(this.mContext);
            } else {
                settingsSearchCell = new HeaderCell(this.mContext, 16);
            }
            settingsSearchCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(settingsSearchCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (this.searchWas) {
                if (i >= this.searchResults.size() && i == this.searchResults.size()) {
                    return 1;
                }
            } else if (i == 0) {
                return !this.recentSearches.isEmpty() ? 2 : 1;
            } else if (!this.recentSearches.isEmpty() && i == this.recentSearches.size() + 1) {
                return 1;
            }
            return 0;
        }

        public void addRecent(Object obj) {
            int indexOf = this.recentSearches.indexOf(obj);
            if (indexOf >= 0) {
                this.recentSearches.remove(indexOf);
            }
            this.recentSearches.add(0, obj);
            if (!this.searchWas) {
                notifyDataSetChanged();
            }
            if (this.recentSearches.size() > 20) {
                ArrayList<Object> arrayList = this.recentSearches;
                arrayList.remove(arrayList.size() - 1);
            }
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            int size = this.recentSearches.size();
            for (int i = 0; i < size; i++) {
                Object obj2 = this.recentSearches.get(i);
                if (obj2 instanceof SearchResult) {
                    ((SearchResult) obj2).num = i;
                } else if (obj2 instanceof MessagesController.FaqSearchResult) {
                    ((MessagesController.FaqSearchResult) obj2).num = i;
                }
                linkedHashSet.add(obj2.toString());
            }
            MessagesController.getGlobalMainSettings().edit().putStringSet("settingsSearchRecent2", linkedHashSet).commit();
        }

        public void clearRecent() {
            this.recentSearches.clear();
            MessagesController.getGlobalMainSettings().edit().remove("settingsSearchRecent2").commit();
            notifyDataSetChanged();
        }

        private int getNum(Object obj) {
            if (obj instanceof SearchResult) {
                return ((SearchResult) obj).num;
            }
            if (obj instanceof MessagesController.FaqSearchResult) {
                return ((MessagesController.FaqSearchResult) obj).num;
            }
            return 0;
        }

        public void search(final String str) {
            this.lastSearchString = str;
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (TextUtils.isEmpty(str)) {
                this.searchWas = false;
                this.searchResults.clear();
                this.faqSearchResults.clear();
                this.resultNames.clear();
                ProfileActivity.this.emptyView.stickerView.getImageReceiver().startAnimation();
                ProfileActivity.this.emptyView.title.setText(LocaleController.getString("SettingsNoRecent", R.string.SettingsNoRecent));
                notifyDataSetChanged();
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda142
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$search$147(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable, 300L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$search$147(final String str) {
            SpannableStringBuilder spannableStringBuilder;
            String str2;
            int i;
            String str3;
            final ArrayList arrayList = new ArrayList();
            final ArrayList arrayList2 = new ArrayList();
            final ArrayList arrayList3 = new ArrayList();
            String str4 = " ";
            String[] split = str.split(" ");
            String[] strArr = new String[split.length];
            int i2 = 0;
            while (true) {
                spannableStringBuilder = null;
                if (i2 >= split.length) {
                    break;
                }
                strArr[i2] = LocaleController.getInstance().getTranslitString(split[i2]);
                if (strArr[i2].equals(split[i2])) {
                    strArr[i2] = null;
                }
                i2++;
            }
            int i3 = 0;
            while (true) {
                SearchResult[] searchResultArr = this.searchArray;
                if (i3 >= searchResultArr.length) {
                    break;
                }
                SearchResult searchResult = searchResultArr[i3];
                if (searchResult != null) {
                    String str5 = " " + searchResult.searchTitle.toLowerCase();
                    SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilder;
                    int i4 = 0;
                    while (i4 < split.length) {
                        if (split[i4].length() != 0) {
                            String str6 = split[i4];
                            int indexOf = str5.indexOf(" " + str6);
                            if (indexOf < 0 && strArr[i4] != null) {
                                str6 = strArr[i4];
                                indexOf = str5.indexOf(" " + str6);
                            }
                            if (indexOf >= 0) {
                                if (spannableStringBuilder2 == null) {
                                    spannableStringBuilder2 = new SpannableStringBuilder(searchResult.searchTitle);
                                }
                                str3 = str5;
                                spannableStringBuilder2.setSpan(new ForegroundColorSpan(ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlueText4)), indexOf, str6.length() + indexOf, 33);
                            }
                        } else {
                            str3 = str5;
                        }
                        if (spannableStringBuilder2 != null && i4 == split.length - 1) {
                            if (searchResult.guid == 502) {
                                int i5 = -1;
                                int i6 = 0;
                                while (true) {
                                    if (i6 >= 4) {
                                        break;
                                    } else if (!UserConfig.getInstance(i6).isClientActivated()) {
                                        i5 = i6;
                                        break;
                                    } else {
                                        i6++;
                                    }
                                }
                                if (i5 < 0) {
                                }
                            }
                            arrayList.add(searchResult);
                            arrayList3.add(spannableStringBuilder2);
                        }
                        i4++;
                        str5 = str3;
                    }
                }
                i3++;
                spannableStringBuilder = null;
            }
            if (this.faqWebPage != null) {
                int size = this.faqSearchArray.size();
                int i7 = 0;
                while (i7 < size) {
                    MessagesController.FaqSearchResult faqSearchResult = this.faqSearchArray.get(i7);
                    String str7 = str4 + faqSearchResult.title.toLowerCase();
                    int i8 = 0;
                    SpannableStringBuilder spannableStringBuilder3 = null;
                    while (i8 < split.length) {
                        if (split[i8].length() != 0) {
                            String str8 = split[i8];
                            int indexOf2 = str7.indexOf(str4 + str8);
                            if (indexOf2 < 0 && strArr[i8] != null) {
                                str8 = strArr[i8];
                                indexOf2 = str7.indexOf(str4 + str8);
                            }
                            if (indexOf2 >= 0) {
                                if (spannableStringBuilder3 == null) {
                                    spannableStringBuilder3 = new SpannableStringBuilder(faqSearchResult.title);
                                }
                                str2 = str4;
                                i = size;
                                spannableStringBuilder3.setSpan(new ForegroundColorSpan(ProfileActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlueText4)), indexOf2, str8.length() + indexOf2, 33);
                            }
                        } else {
                            str2 = str4;
                            i = size;
                        }
                        if (spannableStringBuilder3 != null && i8 == split.length - 1) {
                            arrayList2.add(faqSearchResult);
                            arrayList3.add(spannableStringBuilder3);
                        }
                        i8++;
                        str4 = str2;
                        size = i;
                    }
                    i7++;
                    str4 = str4;
                    size = size;
                }
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ProfileActivity$SearchAdapter$$ExternalSyntheticLambda143
                @Override // java.lang.Runnable
                public final void run() {
                    ProfileActivity.SearchAdapter.this.lambda$search$146(str, arrayList, arrayList2, arrayList3);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$search$146(String str, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
            if (str.equals(this.lastSearchString)) {
                if (!this.searchWas) {
                    ProfileActivity.this.emptyView.stickerView.getImageReceiver().startAnimation();
                    ProfileActivity.this.emptyView.title.setText(LocaleController.getString("SettingsNoResults", R.string.SettingsNoResults));
                }
                this.searchWas = true;
                this.searchResults = arrayList;
                this.faqSearchResults = arrayList2;
                this.resultNames = arrayList3;
                notifyDataSetChanged();
                ProfileActivity.this.emptyView.stickerView.getImageReceiver().startAnimation();
            }
        }

        public boolean isSearchWas() {
            return this.searchWas;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openUrl(String str, Browser.Progress progress) {
        if (str.startsWith("@")) {
            getMessagesController().openByUserName(str.substring(1), this, 0, progress);
        } else if (str.startsWith("#")) {
            DialogsActivity dialogsActivity = new DialogsActivity(null);
            dialogsActivity.setSearchString(str);
            presentFragment(dialogsActivity);
        } else if (!str.startsWith("/") || this.parentLayout.getFragmentStack().size() <= 1) {
        } else {
            BaseFragment baseFragment = this.parentLayout.getFragmentStack().get(this.parentLayout.getFragmentStack().size() - 2);
            if (baseFragment instanceof ChatActivity) {
                finishFragment();
                ((ChatActivity) baseFragment).chatActivityEnterView.setCommand(null, str, false, false);
            }
        }
    }

    private void dimBehindView(View view, float f) {
        this.scrimView = view;
        dimBehindView(f);
    }

    private void dimBehindView(boolean z) {
        dimBehindView(z ? 0.2f : 0.0f);
    }

    private void dimBehindView(float f) {
        ValueAnimator ofFloat;
        boolean z = f > 0.0f;
        this.fragmentView.invalidate();
        AnimatorSet animatorSet = this.scrimAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.scrimAnimatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        if (z) {
            ofFloat = ValueAnimator.ofFloat(0.0f, f);
            arrayList.add(ofFloat);
        } else {
            ofFloat = ValueAnimator.ofFloat(this.scrimPaint.getAlpha() / 255.0f, 0.0f);
            arrayList.add(ofFloat);
        }
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProfileActivity.this.lambda$dimBehindView$63(valueAnimator);
            }
        });
        this.scrimAnimatorSet.playTogether(arrayList);
        this.scrimAnimatorSet.setDuration(z ? 150L : 220L);
        if (!z) {
            this.scrimAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileActivity.51
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ProfileActivity.this.scrimView = null;
                    ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
                }
            });
        }
        this.scrimAnimatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dimBehindView$63(ValueAnimator valueAnimator) {
        this.scrimPaint.setAlpha((int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        if (this.resourcesProvider != null) {
            return null;
        }
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda62
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ProfileActivity.this.lambda$getThemeDescriptions$64();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            arrayList.addAll(sharedMediaLayout.getThemeDescriptions());
        }
        RecyclerListView recyclerListView = this.listView;
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(recyclerListView, 0, null, null, null, null, i));
        arrayList.add(new ThemeDescription(this.searchListView, 0, null, null, null, null, i));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_actionBarSelectorBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_chat_lockIcon));
        int i2 = Theme.key_avatar_subtitleInProfileBlue;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i2));
        int i3 = Theme.key_avatar_backgroundActionBarBlue;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i3));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_profile_title));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_profile_status));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i2));
        if (this.mediaCounterTextView != null) {
            TextView textView = this.mediaCounterTextView.getTextView();
            int i4 = ThemeDescription.FLAG_TEXTCOLOR;
            int i5 = Theme.key_player_actionBarSubtitle;
            arrayList.add(new ThemeDescription(textView, i4, null, null, null, themeDescriptionDelegate, i5));
            arrayList.add(new ThemeDescription(this.mediaCounterTextView.getNextTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, themeDescriptionDelegate, i5));
        }
        arrayList.add(new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        AvatarImageView avatarImageView = this.avatarImage;
        Drawable[] drawableArr = Theme.avatarDrawables;
        int i6 = Theme.key_avatar_text;
        arrayList.add(new ThemeDescription(avatarImageView, 0, null, null, drawableArr, null, i6));
        arrayList.add(new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfileBlue));
        arrayList.add(new ThemeDescription(this.writeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_profile_actionIcon));
        arrayList.add(new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_profile_actionBackground));
        arrayList.add(new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_profile_actionPressedBackground));
        int i7 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGreenText2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_text_RedRegular));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueButton));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        int i8 = Theme.key_windowBackgroundWhiteGrayIcon;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueIcon));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        int i9 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i9));
        int i10 = Theme.key_windowBackgroundWhiteBlueHeader;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i10));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i10));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"detailTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i9));
        int i11 = Theme.key_windowBackgroundWhiteLinkText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{SettingsSuggestionCell.class}, new String[]{"detailTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
        int i12 = Theme.key_featuredStickers_buttonText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"yesButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        int i13 = Theme.key_featuredStickers_addButton;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{SettingsSuggestionCell.class}, new String[]{"yesButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
        int i14 = Theme.key_featuredStickers_addButtonPressed;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{SettingsSuggestionCell.class}, new String[]{"yesButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i14));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"noButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{SettingsSuggestionCell.class}, new String[]{"noButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{SettingsSuggestionCell.class}, new String[]{"noButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i14));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i9));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{UserCell.class}, new String[]{"adminTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_profile_creatorIcon));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, i6));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_undo_background));
        int i15 = Theme.key_undo_cancelColor;
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i15));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i15));
        int i16 = Theme.key_undo_infoColor;
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i16));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i16));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i16));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{UndoView.class}, new String[]{"leftImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i16));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AboutLinkCell.class}, Theme.profile_aboutTextPaint, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AboutLinkCell.class}, Theme.profile_aboutTextPaint, null, null, i11));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AboutLinkCell.class}, Theme.linkSelectionPaint, null, null, Theme.key_windowBackgroundWhiteLinkSelection));
        int i17 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i17));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i17));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i10));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_graySectionText));
        arrayList.add(new ThemeDescription(this.searchListView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i9));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        if (this.mediaHeaderVisible) {
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, this.verifiedCheckDrawable, null, Theme.key_player_actionBarTitle));
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, this.verifiedDrawable, null, i));
        } else {
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, this.verifiedCheckDrawable, null, Theme.key_profile_verifiedCheck));
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, this.verifiedDrawable, null, Theme.key_profile_verifiedBackground));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$64() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                }
            }
        }
        if (!this.isPulledDown) {
            SimpleTextView[] simpleTextViewArr = this.onlineTextView;
            if (simpleTextViewArr[1] != null) {
                Object tag = simpleTextViewArr[1].getTag();
                for (int i2 = 0; i2 < 2; i2++) {
                    if (tag instanceof Integer) {
                        this.onlineTextView[i2 + 1].setTextColor(applyPeerColor(getThemedColor(((Integer) tag).intValue()), true, Boolean.valueOf(this.isOnline[0])));
                    } else {
                        this.onlineTextView[i2 + 1].setTextColor(applyPeerColor(getThemedColor(Theme.key_avatar_subtitleInProfileBlue), true, Boolean.TRUE));
                    }
                }
            }
            Drawable drawable = this.lockIconDrawable;
            if (drawable != null) {
                drawable.setColorFilter(getThemedColor(Theme.key_chat_lockIcon), PorterDuff.Mode.MULTIPLY);
            }
            ScamDrawable scamDrawable = this.scamDrawable;
            if (scamDrawable != null) {
                scamDrawable.setColor(getThemedColor(Theme.key_avatar_subtitleInProfileBlue));
            }
            SimpleTextView[] simpleTextViewArr2 = this.nameTextView;
            if (simpleTextViewArr2[1] != null) {
                simpleTextViewArr2[1].setTextColor(getThemedColor(Theme.key_profile_title));
            }
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.setItemsColor(this.peerColor != null ? -1 : getThemedColor(Theme.key_actionBarDefaultIcon), false);
                this.actionBar.setItemsBackgroundColor(this.peerColor != null ? 553648127 : getThemedColor(Theme.key_avatar_actionBarSelectorBlue), false);
            }
        }
        updateEmojiStatusDrawableColor();
    }

    public void updateListAnimated(boolean z) {
        if (this.listAdapter == null) {
            if (z) {
                updateOnlineCount(false);
            }
            updateRowsIds();
            return;
        }
        DiffCallback diffCallback = new DiffCallback();
        diffCallback.oldRowCount = this.rowCount;
        diffCallback.fillPositions(diffCallback.oldPositionToItem);
        diffCallback.oldChatParticipant.clear();
        diffCallback.oldChatParticipantSorted.clear();
        diffCallback.oldChatParticipant.addAll(this.visibleChatParticipants);
        diffCallback.oldChatParticipantSorted.addAll(this.visibleSortedUsers);
        diffCallback.oldMembersStartRow = this.membersStartRow;
        diffCallback.oldMembersEndRow = this.membersEndRow;
        if (z) {
            updateOnlineCount(false);
        }
        saveScrollPosition();
        updateRowsIds();
        diffCallback.fillPositions(diffCallback.newPositionToItem);
        try {
            DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this.listAdapter);
        } catch (Exception unused) {
            this.listAdapter.notifyDataSetChanged();
        }
        int i = this.savedScrollPosition;
        if (i >= 0) {
            this.layoutManager.scrollToPositionWithOffset(i, this.savedScrollOffset - this.listView.getPaddingTop());
        }
        AndroidUtilities.updateVisibleRows(this.listView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveScrollPosition() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null || this.layoutManager == null || recyclerListView.getChildCount() <= 0 || this.savedScrollToSharedMedia) {
            return;
        }
        View view = null;
        int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int i2 = -1;
        for (int i3 = 0; i3 < this.listView.getChildCount(); i3++) {
            RecyclerListView recyclerListView2 = this.listView;
            int childAdapterPosition = recyclerListView2.getChildAdapterPosition(recyclerListView2.getChildAt(i3));
            View childAt = this.listView.getChildAt(i3);
            if (childAdapterPosition != -1 && childAt.getTop() < i) {
                i = childAt.getTop();
                i2 = childAdapterPosition;
                view = childAt;
            }
        }
        if (view != null) {
            this.savedScrollPosition = i2;
            int top = view.getTop();
            this.savedScrollOffset = top;
            if (this.savedScrollPosition == 0 && !this.allowPullingDown && top > AndroidUtilities.dp(88.0f)) {
                this.savedScrollOffset = AndroidUtilities.dp(88.0f);
            }
            this.layoutManager.scrollToPositionWithOffset(i2, view.getTop() - this.listView.getPaddingTop());
        }
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public void scrollToSharedMedia() {
        this.layoutManager.scrollToPositionWithOffset(this.sharedMediaRow, -this.listView.getPaddingTop());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTextDetailCellImageClicked(View view) {
        View view2 = (View) view.getParent();
        if (view2.getTag() == null || ((Integer) view2.getTag()).intValue() != this.usernameRow) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putLong("user_id", this.userId);
        presentFragment(new QrActivity(bundle));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        writeButtonSetBackground();
    }

    private void writeButtonSetBackground() {
        if (this.writeButton == null) {
            return;
        }
        try {
            Drawable mutate = this.fragmentView.getContext().getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            int themedColor = getThemedColor(Theme.key_profile_actionBackground);
            int themedColor2 = getThemedColor(Theme.key_profile_actionPressedBackground);
            int themedColor3 = getThemedColor(Theme.key_profile_actionIcon);
            if (this.peerColor != null && Theme.hasHue(themedColor)) {
                themedColor = Theme.adaptHSV(this.peerColor.getBgColor1(false), 0.05f, -0.04f);
                themedColor2 = applyPeerColor2(themedColor2);
                themedColor3 = -1;
            }
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), themedColor, themedColor2), 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            this.writeButton.setBackground(combinedDrawable);
            this.writeButton.setColorFilter(new PorterDuffColorFilter(themedColor3, PorterDuff.Mode.MULTIPLY));
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"NotifyDataSetChanged"})
    public void openAddToContact(final TLRPC$User tLRPC$User, Bundle bundle) {
        ContactAddActivity contactAddActivity = new ContactAddActivity(bundle, this.resourcesProvider);
        contactAddActivity.setDelegate(new ContactAddActivity.ContactAddActivityDelegate() { // from class: org.telegram.ui.ProfileActivity$$ExternalSyntheticLambda66
            @Override // org.telegram.ui.ContactAddActivity.ContactAddActivityDelegate
            public final void didAddToContacts() {
                ProfileActivity.this.lambda$openAddToContact$65(tLRPC$User);
            }
        });
        presentFragment(contactAddActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openAddToContact$65(TLRPC$User tLRPC$User) {
        if (this.addToContactsRow >= 0) {
            if (this.sharedMediaRow == -1) {
                updateRowsIds();
                this.listAdapter.notifyDataSetChanged();
            } else {
                updateListAnimated(false);
            }
        }
        if (this.sharedMediaRow == -1) {
            if (this.isInLandscapeMode || AndroidUtilities.isTablet()) {
                this.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, 0);
                this.expandAnimator.cancel();
                float[] fArr = this.expandAnimatorValues;
                fArr[0] = 1.0f;
                fArr[1] = 0.0f;
                setAvatarExpandProgress(1.0f);
                this.extraHeight = AndroidUtilities.dp(88.0f);
            } else {
                int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.listView.getMeasuredWidth(), 1073741824);
                int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.listView.getMeasuredHeight(), 0);
                int i = 0;
                for (int i2 = 0; i2 < this.listAdapter.getItemCount(); i2++) {
                    ListAdapter listAdapter = this.listAdapter;
                    RecyclerView.ViewHolder createViewHolder = listAdapter.createViewHolder(null, listAdapter.getItemViewType(i2));
                    this.listAdapter.onBindViewHolder(createViewHolder, i2);
                    createViewHolder.itemView.measure(makeMeasureSpec, makeMeasureSpec2);
                    i += createViewHolder.itemView.getMeasuredHeight();
                }
                int max = Math.max(0, this.fragmentView.getMeasuredHeight() - ((i + AndroidUtilities.dp(88.0f)) + currentActionBarHeight));
                RecyclerListView recyclerListView = this.listView;
                recyclerListView.setPadding(0, recyclerListView.getPaddingTop(), 0, max);
            }
        }
        this.undoView.showWithAction(this.dialogId, 8, tLRPC$User);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DiffCallback extends DiffUtil.Callback {
        SparseIntArray newPositionToItem;
        ArrayList<TLRPC$ChatParticipant> oldChatParticipant;
        ArrayList<Integer> oldChatParticipantSorted;
        int oldMembersEndRow;
        int oldMembersStartRow;
        SparseIntArray oldPositionToItem;
        int oldRowCount;

        private DiffCallback() {
            this.oldPositionToItem = new SparseIntArray();
            this.newPositionToItem = new SparseIntArray();
            this.oldChatParticipant = new ArrayList<>();
            this.oldChatParticipantSorted = new ArrayList<>();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.oldRowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return ProfileActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            TLRPC$ChatParticipant tLRPC$ChatParticipant;
            if (i2 >= ProfileActivity.this.membersStartRow && i2 < ProfileActivity.this.membersEndRow && i >= this.oldMembersStartRow && i < this.oldMembersEndRow) {
                if (!this.oldChatParticipantSorted.isEmpty()) {
                    tLRPC$ChatParticipant = this.oldChatParticipant.get(this.oldChatParticipantSorted.get(i - this.oldMembersStartRow).intValue());
                } else {
                    tLRPC$ChatParticipant = this.oldChatParticipant.get(i - this.oldMembersStartRow);
                }
                return tLRPC$ChatParticipant.user_id == (!ProfileActivity.this.sortedUsers.isEmpty() ? (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(((Integer) ProfileActivity.this.visibleSortedUsers.get(i2 - ProfileActivity.this.membersStartRow)).intValue()) : (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(i2 - ProfileActivity.this.membersStartRow)).user_id;
            }
            int i3 = this.oldPositionToItem.get(i, -1);
            return i3 == this.newPositionToItem.get(i2, -1) && i3 >= 0;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return areItemsTheSame(i, i2);
        }

        public void fillPositions(SparseIntArray sparseIntArray) {
            sparseIntArray.clear();
            put(1, ProfileActivity.this.setAvatarRow, sparseIntArray);
            put(2, ProfileActivity.this.setAvatarSectionRow, sparseIntArray);
            put(3, ProfileActivity.this.numberSectionRow, sparseIntArray);
            put(4, ProfileActivity.this.numberRow, sparseIntArray);
            put(5, ProfileActivity.this.setUsernameRow, sparseIntArray);
            put(6, ProfileActivity.this.bioRow, sparseIntArray);
            put(7, ProfileActivity.this.phoneSuggestionRow, sparseIntArray);
            put(8, ProfileActivity.this.phoneSuggestionSectionRow, sparseIntArray);
            put(9, ProfileActivity.this.passwordSuggestionRow, sparseIntArray);
            put(10, ProfileActivity.this.passwordSuggestionSectionRow, sparseIntArray);
            put(11, ProfileActivity.this.settingsSectionRow, sparseIntArray);
            put(12, ProfileActivity.this.settingsSectionRow2, sparseIntArray);
            put(13, ProfileActivity.this.notificationRow, sparseIntArray);
            put(14, ProfileActivity.this.languageRow, sparseIntArray);
            put(15, ProfileActivity.this.premiumRow, sparseIntArray);
            put(16, ProfileActivity.this.premiumSectionsRow, sparseIntArray);
            put(17, ProfileActivity.this.privacyRow, sparseIntArray);
            put(18, ProfileActivity.this.dataRow, sparseIntArray);
            put(19, ProfileActivity.this.liteModeRow, sparseIntArray);
            put(20, ProfileActivity.this.chatRow, sparseIntArray);
            put(21, ProfileActivity.this.filtersRow, sparseIntArray);
            put(22, ProfileActivity.this.stickersRow, sparseIntArray);
            put(23, ProfileActivity.this.devicesRow, sparseIntArray);
            put(24, ProfileActivity.this.devicesSectionRow, sparseIntArray);
            put(25, ProfileActivity.this.helpHeaderRow, sparseIntArray);
            put(26, ProfileActivity.this.questionRow, sparseIntArray);
            put(27, ProfileActivity.this.faqRow, sparseIntArray);
            put(28, ProfileActivity.this.policyRow, sparseIntArray);
            put(29, ProfileActivity.this.helpSectionCell, sparseIntArray);
            put(30, ProfileActivity.this.debugHeaderRow, sparseIntArray);
            put(31, ProfileActivity.this.sendLogsRow, sparseIntArray);
            put(32, ProfileActivity.this.sendLastLogsRow, sparseIntArray);
            put(33, ProfileActivity.this.clearLogsRow, sparseIntArray);
            put(34, ProfileActivity.this.switchBackendRow, sparseIntArray);
            put(35, ProfileActivity.this.versionRow, sparseIntArray);
            put(36, ProfileActivity.this.emptyRow, sparseIntArray);
            put(37, ProfileActivity.this.bottomPaddingRow, sparseIntArray);
            put(38, ProfileActivity.this.infoHeaderRow, sparseIntArray);
            put(39, ProfileActivity.this.phoneRow, sparseIntArray);
            put(40, ProfileActivity.this.locationRow, sparseIntArray);
            put(41, ProfileActivity.this.userInfoRow, sparseIntArray);
            put(42, ProfileActivity.this.channelInfoRow, sparseIntArray);
            put(43, ProfileActivity.this.usernameRow, sparseIntArray);
            put(44, ProfileActivity.this.notificationsDividerRow, sparseIntArray);
            put(45, ProfileActivity.this.reportDividerRow, sparseIntArray);
            put(46, ProfileActivity.this.notificationsRow, sparseIntArray);
            put(47, ProfileActivity.this.infoSectionRow, sparseIntArray);
            put(48, ProfileActivity.this.sendMessageRow, sparseIntArray);
            put(49, ProfileActivity.this.reportRow, sparseIntArray);
            put(50, ProfileActivity.this.reportReactionRow, sparseIntArray);
            put(51, ProfileActivity.this.addToContactsRow, sparseIntArray);
            put(52, ProfileActivity.this.settingsTimerRow, sparseIntArray);
            put(53, ProfileActivity.this.settingsKeyRow, sparseIntArray);
            put(54, ProfileActivity.this.secretSettingsSectionRow, sparseIntArray);
            put(55, ProfileActivity.this.membersHeaderRow, sparseIntArray);
            put(56, ProfileActivity.this.addMemberRow, sparseIntArray);
            put(57, ProfileActivity.this.subscribersRow, sparseIntArray);
            put(58, ProfileActivity.this.subscribersRequestsRow, sparseIntArray);
            put(59, ProfileActivity.this.administratorsRow, sparseIntArray);
            put(60, ProfileActivity.this.settingsRow, sparseIntArray);
            put(61, ProfileActivity.this.blockedUsersRow, sparseIntArray);
            put(62, ProfileActivity.this.membersSectionRow, sparseIntArray);
            put(63, ProfileActivity.this.sharedMediaRow, sparseIntArray);
            put(64, ProfileActivity.this.unblockRow, sparseIntArray);
            put(65, ProfileActivity.this.addToGroupButtonRow, sparseIntArray);
            put(66, ProfileActivity.this.addToGroupInfoRow, sparseIntArray);
            put(67, ProfileActivity.this.joinRow, sparseIntArray);
            put(68, ProfileActivity.this.lastSectionRow, sparseIntArray);
            put(69, ProfileActivity.this.notificationsSimpleRow, sparseIntArray);
        }

        private void put(int i, int i2, SparseIntArray sparseIntArray) {
            if (i2 >= 0) {
                sparseIntArray.put(i2, i);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        int themedColor;
        if (this.isPulledDown) {
            return false;
        }
        if (this.actionBar.isActionModeShowed()) {
            themedColor = getThemedColor(Theme.key_actionBarActionModeDefault);
        } else if (this.mediaHeaderVisible) {
            themedColor = getThemedColor(Theme.key_windowBackgroundWhite);
        } else {
            MessagesController.PeerColor peerColor = this.peerColor;
            if (peerColor != null) {
                themedColor = peerColor.getBgColor2(Theme.isCurrentThemeDark());
            } else {
                themedColor = getThemedColor(Theme.key_actionBarDefault);
            }
        }
        return ColorUtils.calculateLuminance(themedColor) > 0.699999988079071d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPhotoDescriptionAlpha() {
        int i = this.playProfileAnimation;
        if (i == 1 && (!this.fragmentOpened || this.openAnimationInProgress)) {
            this.photoDescriptionProgress = 0.0f;
        } else if (i == 2 && (!this.fragmentOpened || this.openAnimationInProgress)) {
            this.photoDescriptionProgress = this.onlineTextView[1].getAlpha();
        } else if (this.userId == UserConfig.getInstance(this.currentAccount).clientUserId) {
            this.photoDescriptionProgress = this.currentExpandAnimatorValue * (1.0f - this.customAvatarProgress);
        } else {
            this.photoDescriptionProgress = this.currentExpandAnimatorValue * this.customAvatarProgress;
        }
        if (this.userId == UserConfig.getInstance(this.currentAccount).clientUserId) {
            if (this.hasFallbackPhoto) {
                this.customPhotoOffset = AndroidUtilities.dp(28.0f) * this.photoDescriptionProgress;
                SimpleTextView[] simpleTextViewArr = this.onlineTextView;
                if (simpleTextViewArr[2] != null) {
                    simpleTextViewArr[2].setAlpha(this.currentExpandAnimatorValue);
                    this.onlineTextView[3].setAlpha(1.0f - this.currentExpandAnimatorValue);
                    this.onlineTextView[1].setTranslationX(this.onlineX + this.customPhotoOffset);
                    this.avatarContainer2.invalidate();
                    return;
                }
                return;
            }
            SimpleTextView[] simpleTextViewArr2 = this.onlineTextView;
            if (simpleTextViewArr2[2] != null) {
                simpleTextViewArr2[2].setAlpha(0.0f);
                this.onlineTextView[3].setAlpha(0.0f);
            }
        } else if (this.hasCustomPhoto) {
            SimpleTextView[] simpleTextViewArr3 = this.onlineTextView;
            if (simpleTextViewArr3[2] != null) {
                simpleTextViewArr3[2].setAlpha(this.photoDescriptionProgress);
            }
        } else {
            SimpleTextView[] simpleTextViewArr4 = this.onlineTextView;
            if (simpleTextViewArr4[2] != null) {
                simpleTextViewArr4[2].setAlpha(0.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStoriesViewBounds(boolean z) {
        ActionBar actionBar;
        if (this.storyView == null || (actionBar = this.actionBar) == null) {
            return;
        }
        float f = actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0.0f;
        float width = this.actionBar.getWidth();
        float max = this.actionBar.getBackButton() != null ? Math.max(0.0f, this.actionBar.getBackButton().getRight()) : 0.0f;
        if (this.actionBar.menu != null) {
            for (int i = 0; i < this.actionBar.menu.getChildCount(); i++) {
                View childAt = this.actionBar.menu.getChildAt(i);
                if (childAt.getAlpha() > 0.0f && childAt.getVisibility() == 0) {
                    float left = this.actionBar.menu.getLeft() + ((int) childAt.getX());
                    if (left < width) {
                        width = AndroidUtilities.lerp(width, left, childAt.getAlpha());
                    }
                }
            }
        }
        this.storyView.setBounds(max, width, f + ((this.actionBar.getHeight() - f) / 2.0f), !z);
    }

    /* loaded from: classes3.dex */
    private class ClippedListView extends RecyclerListView implements StoriesListPlaceProvider.ClippedView {
        public ClippedListView(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Stories.StoriesListPlaceProvider.ClippedView
        public void updateClip(int[] iArr) {
            iArr[0] = ((BaseFragment) ProfileActivity.this).actionBar.getMeasuredHeight();
            iArr[1] = getMeasuredHeight() - getPaddingBottom();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void listCodecs(String str, StringBuilder sb) {
        String[] supportedTypes;
        boolean z;
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        try {
            int codecCount = MediaCodecList.getCodecCount();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < codecCount; i++) {
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
                if (codecInfoAt != null && (supportedTypes = codecInfoAt.getSupportedTypes()) != null) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= supportedTypes.length) {
                            z = false;
                            break;
                        } else if (supportedTypes[i2].equals(str)) {
                            z = true;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (z) {
                        (codecInfoAt.isEncoder() ? arrayList2 : arrayList).add(Integer.valueOf(i));
                    }
                }
            }
            if (arrayList.isEmpty() && arrayList2.isEmpty()) {
                return;
            }
            sb.append("\n");
            sb.append(arrayList.size());
            sb.append("+");
            sb.append(arrayList2.size());
            sb.append(" ");
            sb.append(str.substring(6));
            sb.append(" codecs:\n");
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (i3 > 0) {
                    sb.append("\n");
                }
                MediaCodecInfo codecInfoAt2 = MediaCodecList.getCodecInfoAt(((Integer) arrayList.get(i3)).intValue());
                sb.append("{d} ");
                sb.append(codecInfoAt2.getName());
                sb.append(" (");
                if (Build.VERSION.SDK_INT >= 29) {
                    if (codecInfoAt2.isHardwareAccelerated()) {
                        sb.append("gpu");
                    }
                    if (codecInfoAt2.isSoftwareOnly()) {
                        sb.append("cpu");
                    }
                    if (codecInfoAt2.isVendor()) {
                        sb.append(", v");
                    }
                }
                MediaCodecInfo.CodecCapabilities capabilitiesForType = codecInfoAt2.getCapabilitiesForType(str);
                sb.append("; mi=");
                sb.append(capabilitiesForType.getMaxSupportedInstances());
                sb.append(")");
            }
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                if (i4 > 0 || !arrayList.isEmpty()) {
                    sb.append("\n");
                }
                MediaCodecInfo codecInfoAt3 = MediaCodecList.getCodecInfoAt(((Integer) arrayList2.get(i4)).intValue());
                sb.append("{e} ");
                sb.append(codecInfoAt3.getName());
                sb.append(" (");
                if (Build.VERSION.SDK_INT >= 29) {
                    if (codecInfoAt3.isHardwareAccelerated()) {
                        sb.append("gpu");
                    }
                    if (codecInfoAt3.isSoftwareOnly()) {
                        sb.append("cpu");
                    }
                    if (codecInfoAt3.isVendor()) {
                        sb.append(", v");
                    }
                }
                MediaCodecInfo.CodecCapabilities capabilitiesForType2 = codecInfoAt3.getCapabilitiesForType(str);
                sb.append("; mi=");
                sb.append(capabilitiesForType2.getMaxSupportedInstances());
                sb.append(")");
            }
            sb.append("\n");
        } catch (Exception unused) {
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationProgress(boolean z, float f) {
        super.onTransitionAnimationProgress(z, f);
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

    public void prepareBlurBitmap() {
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
}
