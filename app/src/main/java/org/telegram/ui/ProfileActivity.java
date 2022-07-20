package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Property;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebStorage;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Keep;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
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
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LanguageDetector;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
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
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$PageListItem;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$PrivacyRule;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
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
import org.telegram.tgnet.TLRPC$TL_encryptedChat;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_fileLocation_layer97;
import org.telegram.tgnet.TLRPC$TL_inputGroupCall;
import org.telegram.tgnet.TLRPC$TL_inputPhoto;
import org.telegram.tgnet.TLRPC$TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC$TL_messages_getWebPage;
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
import org.telegram.tgnet.TLRPC$TL_privacyValueAllowAll;
import org.telegram.tgnet.TLRPC$TL_privacyValueAllowContacts;
import org.telegram.tgnet.TLRPC$TL_privacyValueDisallowAll;
import org.telegram.tgnet.TLRPC$TL_userEmpty;
import org.telegram.tgnet.TLRPC$TL_userProfilePhoto;
import org.telegram.tgnet.TLRPC$TL_userProfilePhotoEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AboutLinkCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.SettingsSearchCell;
import org.telegram.ui.Cells.SettingsSuggestionCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.AudioPlayerAlert;
import org.telegram.ui.Components.AutoDeletePopupWrapper;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackButtonMenu;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.ChatNotificationsPopupWrapper;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CrossfadeDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.IdenticonDrawable;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.Premium.PremiumPreviewBottomSheet;
import org.telegram.ui.Components.Premium.ProfilePremiumCell;
import org.telegram.ui.Components.ProfileGalleryView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.SharedMediaLayout;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.TimerDrawable;
import org.telegram.ui.Components.TranslateAlert;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.PinchToZoomHelper;
/* loaded from: classes3.dex */
public class ProfileActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate, SharedMediaLayout.SharedMediaPreloaderDelegate, ImageUpdater.ImageUpdaterDelegate, SharedMediaLayout.Delegate {
    private Property<ActionBar, Float> ACTIONBAR_HEADER_PROGRESS;
    private final Property<ProfileActivity, Float> HEADER_SHADOW;
    private AboutLinkCell aboutLinkCell;
    private int actionBarAnimationColorFrom;
    private Paint actionBarBackgroundPaint;
    private int addMemberRow;
    private int addToGroupButtonRow;
    private int addToGroupInfoRow;
    private int administratorsRow;
    private boolean allowProfileAnimation;
    private boolean allowPullingDown;
    private ActionBarMenuItem animatingItem;
    private float animationProgress;
    private ActionBarMenuSubItem autoDeleteItem;
    TimerDrawable autoDeleteItemDrawable;
    AutoDeletePopupWrapper autoDeletePopupWrapper;
    private TLRPC$FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private TLRPC$FileLocation avatarBig;
    private int avatarColor;
    private FrameLayout avatarContainer;
    private FrameLayout avatarContainer2;
    private AvatarDrawable avatarDrawable;
    private AvatarImageView avatarImage;
    private RadialProgressView avatarProgressView;
    private float avatarScale;
    private float avatarX;
    private float avatarY;
    private ProfileGalleryView avatarsViewPager;
    private PagerIndicatorView avatarsViewPagerIndicatorView;
    private long banFromGroup;
    private int bioRow;
    private int blockedUsersRow;
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
    private int dataRow;
    private int debugHeaderRow;
    private int devicesRow;
    private int devicesSectionRow;
    private long dialogId;
    private boolean disableProfileAnimation;
    private boolean doNotSetForeground;
    private ActionBarMenuItem editItem;
    private boolean editItemVisible;
    private int emptyRow;
    private StickerEmptyView emptyView;
    private ValueAnimator expandAnimator;
    private float[] expandAnimatorValues;
    private boolean expandPhoto;
    private float expandProgress;
    private float extraHeight;
    private int faqRow;
    private int filtersRow;
    private boolean firstLayout;
    private boolean fragmentOpened;
    private HintView fwdRestrictedHint;
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
    private boolean isInLandscapeMode;
    private boolean[] isOnline;
    private boolean isPulledDown;
    private boolean isQrItemVisible;
    private int joinRow;
    private int languageRow;
    private int lastMeasuredContentHeight;
    private int lastMeasuredContentWidth;
    private int lastSectionRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private int listContentHeight;
    private RecyclerListView listView;
    private float listViewVelocityY;
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
    private int notificationsRow;
    private int numberRow;
    private int numberSectionRow;
    private int onlineCount;
    private SimpleTextView[] onlineTextView;
    private float onlineX;
    private float onlineY;
    private boolean openAnimationInProgress;
    private boolean openingAvatar;
    private ActionBarMenuItem otherItem;
    private int overlayCountVisible;
    private OverlaysView overlaysView;
    private LongSparseArray<TLRPC$ChatParticipant> participantsMap;
    private int passwordSuggestionRow;
    private int passwordSuggestionSectionRow;
    private int phoneRow;
    private int phoneSuggestionRow;
    private int phoneSuggestionSectionRow;
    PinchToZoomHelper pinchToZoomHelper;
    private int playProfileAnimation;
    private int policyRow;
    private HashMap<Integer, Integer> positionToOffset;
    private int premiumRow;
    private int premiumSectionsRow;
    private Drawable premiumStarDrawable;
    private CrossfadeDrawable premuimCrossfadeDrawable;
    private ImageLocation prevLoadedImageLocation;
    ChatActivity previousTransitionFragment;
    private int privacyRow;
    boolean profileTransitionInProgress;
    private PhotoViewer.PhotoViewerProvider provider;
    private ActionBarMenuItem qrItem;
    private AnimatorSet qrItemAnimation;
    private int questionRow;
    private boolean recreateMenuAfterAnimation;
    private Rect rect;
    private int reportRow;
    private boolean reportSpam;
    private Theme.ResourcesProvider resourcesProvider;
    private int rowCount;
    int savedScrollOffset;
    int savedScrollPosition;
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
    private long selectedUser;
    private int sendLastLogsRow;
    private int sendLogsRow;
    private int sendMessageRow;
    private TextCell setAvatarCell;
    private int setAvatarRow;
    private int setAvatarSectionRow;
    private int setUsernameRow;
    private int settingsKeyRow;
    private int settingsSectionRow;
    private int settingsSectionRow2;
    private int settingsTimerRow;
    private SharedMediaLayout sharedMediaLayout;
    private boolean sharedMediaLayoutAttached;
    private SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader;
    private int sharedMediaRow;
    private ArrayList<Integer> sortedUsers;
    private int stickersRow;
    private int subscribersRequestsRow;
    private int subscribersRow;
    private int switchBackendRow;
    private ImageView timeItem;
    private TimerDrawable timerDrawable;
    private TopView topView;
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
    private Drawable verifiedCheckDrawable;
    private CrossfadeDrawable verifiedCrossfadeDrawable;
    private Drawable verifiedDrawable;
    private int versionRow;
    private ActionBarMenuItem videoCallItem;
    private boolean videoCallItemVisible;
    private final ArrayList<TLRPC$ChatParticipant> visibleChatParticipants;
    private final ArrayList<Integer> visibleSortedUsers;
    private Paint whitePaint;
    private RLottieImageView writeButton;
    private AnimatorSet writeButtonAnimation;

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ String getInitialSearchString() {
        return ImageUpdater.ImageUpdaterDelegate.CC.$default$getInitialSearchString(this);
    }

    public static /* synthetic */ void access$25500(ProfileActivity profileActivity, View view) {
        profileActivity.onTextDetailCellImageClicked(view);
    }

    static /* synthetic */ int access$7512(ProfileActivity profileActivity, int i) {
        int i2 = profileActivity.listContentHeight + i;
        profileActivity.listContentHeight = i2;
        return i2;
    }

    /* renamed from: org.telegram.ui.ProfileActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends Paint {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(int i) {
            super(i);
            ProfileActivity.this = r1;
        }

        @Override // android.graphics.Paint
        public void setAlpha(int i) {
            super.setAlpha(i);
            ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends AnimationProperties.FloatProperty<ProfileActivity> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(String str) {
            super(str);
            ProfileActivity.this = r1;
        }

        public void setValue(ProfileActivity profileActivity, float f) {
            ProfileActivity profileActivity2 = ProfileActivity.this;
            profileActivity2.headerShadowAlpha = f;
            profileActivity2.topView.invalidate();
        }

        public Float get(ProfileActivity profileActivity) {
            return Float.valueOf(ProfileActivity.this.headerShadowAlpha);
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends PhotoViewer.EmptyPhotoViewerProvider {
        AnonymousClass3() {
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
            TLRPC$FileLocation tLRPC$FileLocation2;
            TLRPC$Chat chat;
            TLRPC$ChatPhoto tLRPC$ChatPhoto;
            TLRPC$User user;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
            if (tLRPC$FileLocation == null) {
                return null;
            }
            if (ProfileActivity.this.userId == 0 ? ProfileActivity.this.chatId == 0 || (chat = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(ProfileActivity.this.chatId))) == null || (tLRPC$ChatPhoto = chat.photo) == null || (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_big) == null : (user = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId))) == null || (tLRPC$UserProfilePhoto = user.photo) == null || (tLRPC$FileLocation2 = tLRPC$UserProfilePhoto.photo_big) == null) {
                tLRPC$FileLocation2 = null;
            }
            if (tLRPC$FileLocation2 == null || tLRPC$FileLocation2.local_id != tLRPC$FileLocation.local_id || tLRPC$FileLocation2.volume_id != tLRPC$FileLocation.volume_id || tLRPC$FileLocation2.dc_id != tLRPC$FileLocation.dc_id) {
                return null;
            }
            int[] iArr = new int[2];
            ProfileActivity.this.avatarImage.getLocationInWindow(iArr);
            PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
            boolean z2 = false;
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
            if (ProfileActivity.this.userId == ProfileActivity.this.getUserConfig().clientUserId) {
                z2 = true;
            }
            placeProviderObject.canEdit = z2;
            return placeProviderObject;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void willHidePhotoViewer() {
            ProfileActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void openPhotoForEdit(String str, String str2, boolean z) {
            ProfileActivity.this.imageUpdater.openPhotoForEdit(str, str2, 0, z);
        }
    }

    /* loaded from: classes3.dex */
    public static class AvatarImageView extends BackupImageView {
        ProfileGalleryView avatarsViewPager;
        private ImageReceiver.BitmapHolder drawableHolder;
        private float foregroundAlpha;
        private final Paint placeholderPaint;
        private final RectF rect = new RectF();
        private ImageReceiver foregroundImageReceiver = new ImageReceiver(this);

        public void setAvatarsViewPager(ProfileGalleryView profileGalleryView) {
            this.avatarsViewPager = profileGalleryView;
        }

        public AvatarImageView(Context context) {
            super(context);
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

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            if (this.foregroundAlpha < 1.0f) {
                this.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.imageReceiver.draw(canvas);
            }
            if (this.foregroundAlpha > 0.0f) {
                if (this.foregroundImageReceiver.getDrawable() != null) {
                    this.foregroundImageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    this.foregroundImageReceiver.setAlpha(this.foregroundAlpha);
                    this.foregroundImageReceiver.draw(canvas);
                    return;
                }
                this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.placeholderPaint.setAlpha((int) (this.foregroundAlpha * 255.0f));
                float f = this.foregroundImageReceiver.getRoundRadius()[0];
                canvas.drawRoundRect(this.rect, f, f, this.placeholderPaint);
            }
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            ProfileGalleryView profileGalleryView = this.avatarsViewPager;
            if (profileGalleryView != null) {
                profileGalleryView.invalidate();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class TopView extends View {
        private int currentColor;
        private Paint paint = new Paint();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TopView(Context context) {
            super(context);
            ProfileActivity.this = r1;
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

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float currentActionBarHeight = ProfileActivity.this.extraHeight + ActionBar.getCurrentActionBarHeight() + (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ProfileActivity.this.searchTransitionOffset;
            int i = (int) ((1.0f - ProfileActivity.this.mediaHeaderAnimationProgress) * currentActionBarHeight);
            if (i != 0) {
                if (ProfileActivity.this.previousTransitionFragment != null) {
                    Rect rect = AndroidUtilities.rectTmp2;
                    rect.set(0, 0, getMeasuredWidth(), i);
                    ProfileActivity.this.previousTransitionFragment.contentView.drawBlurRect(canvas, getY(), rect, ProfileActivity.this.previousTransitionFragment.getActionBar().blurScrimPaint, true);
                }
                this.paint.setColor(this.currentColor);
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), i, this.paint);
            }
            if (i != currentActionBarHeight) {
                this.paint.setColor(ProfileActivity.this.getThemedColor("windowBackgroundWhite"));
                Rect rect2 = AndroidUtilities.rectTmp2;
                rect2.set(0, i, getMeasuredWidth(), (int) currentActionBarHeight);
                ProfileActivity.this.contentView.drawBlurRect(canvas, getY(), rect2, this.paint, true);
            }
            if (((BaseFragment) ProfileActivity.this).parentLayout != null) {
                ((BaseFragment) ProfileActivity.this).parentLayout.drawHeaderShadow(canvas, (int) (ProfileActivity.this.headerShadowAlpha * 255.0f), (int) currentActionBarHeight);
            }
        }
    }

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

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OverlaysView(Context context) {
            super(context);
            ProfileActivity.this = r8;
            this.statusBarHeight = (!((BaseFragment) r8).actionBar.getOccupyStatusBar() || ((BaseFragment) r8).inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight;
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
            ofFloat.addUpdateListener(new ProfileActivity$OverlaysView$$ExternalSyntheticLambda0(this));
            ofFloat.addListener(new AnonymousClass1(r8));
        }

        public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
            float[] fArr = this.animatorValues;
            float animatedFraction = valueAnimator.getAnimatedFraction();
            this.currentAnimationValue = animatedFraction;
            setAlphaValue(AndroidUtilities.lerp(fArr, animatedFraction), true);
        }

        /* renamed from: org.telegram.ui.ProfileActivity$OverlaysView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1(ProfileActivity profileActivity) {
                OverlaysView.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!OverlaysView.this.isOverlaysVisible) {
                    OverlaysView.this.setVisibility(8);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                OverlaysView.this.setVisibility(0);
            }
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
                float f2 = 1.0f;
                if (z) {
                    this.animator.setDuration(((1.0f - lerp) * 250.0f) / f);
                } else {
                    this.animator.setDuration((250.0f * lerp) / f);
                }
                float[] fArr = this.animatorValues;
                fArr[0] = lerp;
                if (!z) {
                    f2 = 0.0f;
                }
                fArr[1] = f2;
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
            float f;
            int i;
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
            if (j < 0 || j > 20) {
                j = 17;
            }
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

    /* loaded from: classes3.dex */
    public class NestedFrameLayout extends SizeNotifierFrameLayout implements NestedScrollingParent3 {
        private NestedScrollingParentHelper nestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onStopNestedScroll(View view) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public NestedFrameLayout(Context context) {
            super(context);
            ProfileActivity.this = r1;
        }

        @Override // androidx.core.view.NestedScrollingParent3
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
            if (view != ProfileActivity.this.listView || !ProfileActivity.this.sharedMediaLayoutAttached) {
                return;
            }
            RecyclerListView currentListView = ProfileActivity.this.sharedMediaLayout.getCurrentListView();
            if (ProfileActivity.this.sharedMediaLayout.getTop() != 0) {
                return;
            }
            iArr[1] = i4;
            currentListView.scrollBy(0, i4);
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public boolean onNestedPreFling(View view, float f, float f2) {
            return super.onNestedPreFling(view, f, f2);
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
            if (view == ProfileActivity.this.listView) {
                int i4 = -1;
                if (ProfileActivity.this.sharedMediaRow == -1 || !ProfileActivity.this.sharedMediaLayoutAttached) {
                    return;
                }
                boolean isSearchFieldVisible = ((BaseFragment) ProfileActivity.this).actionBar.isSearchFieldVisible();
                int top = ProfileActivity.this.sharedMediaLayout.getTop();
                boolean z = false;
                if (i2 >= 0) {
                    if (!isSearchFieldVisible) {
                        return;
                    }
                    RecyclerListView currentListView = ProfileActivity.this.sharedMediaLayout.getCurrentListView();
                    iArr[1] = i2;
                    if (top > 0) {
                        iArr[1] = iArr[1] - i2;
                    }
                    if (iArr[1] <= 0) {
                        return;
                    }
                    currentListView.scrollBy(0, iArr[1]);
                    return;
                }
                if (top <= 0) {
                    RecyclerListView currentListView2 = ProfileActivity.this.sharedMediaLayout.getCurrentListView();
                    int findFirstVisibleItemPosition = ((LinearLayoutManager) currentListView2.getLayoutManager()).findFirstVisibleItemPosition();
                    if (findFirstVisibleItemPosition != -1) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = currentListView2.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                        if (findViewHolderForAdapterPosition != null) {
                            i4 = findViewHolderForAdapterPosition.itemView.getTop();
                        }
                        int paddingTop = currentListView2.getPaddingTop();
                        if (i4 != paddingTop || findFirstVisibleItemPosition != 0) {
                            iArr[1] = findFirstVisibleItemPosition != 0 ? i2 : Math.max(i2, i4 - paddingTop);
                            currentListView2.scrollBy(0, i2);
                            z = true;
                        }
                    }
                }
                if (!isSearchFieldVisible) {
                    return;
                }
                if (!z && top < 0) {
                    iArr[1] = i2 - Math.max(top, i2);
                } else {
                    iArr[1] = i2;
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

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        public void drawList(Canvas canvas, boolean z) {
            super.drawList(canvas, z);
            canvas.save();
            canvas.translate(0.0f, ProfileActivity.this.listView.getY());
            ProfileActivity.this.sharedMediaLayout.drawListForBlur(canvas);
            canvas.restore();
        }
    }

    /* loaded from: classes3.dex */
    public class PagerIndicatorView extends View {
        private final PagerAdapter adapter;
        private final ValueAnimator animator;
        private final Paint backgroundPaint;
        private boolean isIndicatorVisible;
        private final TextPaint textPaint;
        private final RectF indicatorRect = new RectF();
        private final float[] animatorValues = {0.0f, 1.0f};

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PagerIndicatorView(Context context) {
            super(context);
            ProfileActivity.this = r5;
            PagerAdapter adapter = r5.avatarsViewPager.getAdapter();
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
            ofFloat.addUpdateListener(new ProfileActivity$PagerIndicatorView$$ExternalSyntheticLambda0(this));
            ofFloat.addListener(new AnonymousClass1(r5, r5.expandPhoto));
            r5.avatarsViewPager.addOnPageChangeListener(new AnonymousClass2(r5));
            adapter.registerDataSetObserver(new AnonymousClass3(r5));
        }

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

        /* renamed from: org.telegram.ui.ProfileActivity$PagerIndicatorView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ boolean val$expanded;

            AnonymousClass1(ProfileActivity profileActivity, boolean z) {
                PagerIndicatorView.this = r1;
                this.val$expanded = z;
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
                    if (!ProfileActivity.this.videoCallItemVisible) {
                        return;
                    }
                    ProfileActivity.this.videoCallItem.setVisibility(8);
                    return;
                }
                PagerIndicatorView.this.setVisibility(8);
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
            }
        }

        /* renamed from: org.telegram.ui.ProfileActivity$PagerIndicatorView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 implements ViewPager.OnPageChangeListener {
            private int prevPage;

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            AnonymousClass2(ProfileActivity profileActivity) {
                PagerIndicatorView.this = r1;
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                int realPosition = ProfileActivity.this.avatarsViewPager.getRealPosition(i);
                PagerIndicatorView.this.invalidateIndicatorRect(this.prevPage != realPosition);
                this.prevPage = realPosition;
                PagerIndicatorView.this.updateAvatarItems();
            }
        }

        /* renamed from: org.telegram.ui.ProfileActivity$PagerIndicatorView$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends DataSetObserver {
            AnonymousClass3(ProfileActivity profileActivity) {
                PagerIndicatorView.this = r1;
            }

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
        }

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

        public void updateAvatarItems() {
            if (ProfileActivity.this.imageUpdater == null) {
                return;
            }
            if (ProfileActivity.this.otherItem.isSubMenuShowing()) {
                AndroidUtilities.runOnUIThread(new ProfileActivity$PagerIndicatorView$$ExternalSyntheticLambda1(this), 500L);
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
                float f2 = 1.0f;
                if (f <= 0.0f) {
                    this.animator.setDuration(0L);
                } else if (z) {
                    this.animator.setDuration(((1.0f - lerp) * 250.0f) / f);
                } else {
                    this.animator.setDuration((250.0f * lerp) / f);
                }
                float[] fArr = this.animatorValues;
                fArr[0] = lerp;
                if (!z) {
                    f2 = 0.0f;
                }
                fArr[1] = f2;
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

        public void invalidateIndicatorRect(boolean z) {
            if (z) {
                ProfileActivity.this.overlaysView.saveCurrentPageProgress();
            }
            ProfileActivity.this.overlaysView.invalidate();
            float measureText = this.textPaint.measureText(getCurrentTitle());
            int i = 0;
            this.indicatorRect.right = (getMeasuredWidth() - AndroidUtilities.dp(54.0f)) - (ProfileActivity.this.qrItem != null ? AndroidUtilities.dp(48.0f) : 0);
            RectF rectF = this.indicatorRect;
            rectF.left = rectF.right - (measureText + AndroidUtilities.dpf2(16.0f));
            RectF rectF2 = this.indicatorRect;
            if (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar()) {
                i = AndroidUtilities.statusBarHeight;
            }
            rectF2.top = i + AndroidUtilities.dp(15.0f);
            RectF rectF3 = this.indicatorRect;
            rectF3.bottom = rectF3.top + AndroidUtilities.dp(26.0f);
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

        public ActionBarMenuItem getSecondaryMenuItem() {
            if (ProfileActivity.this.callItemVisible) {
                return ProfileActivity.this.callItem;
            }
            if (ProfileActivity.this.editItemVisible) {
                return ProfileActivity.this.editItem;
            }
            if (ProfileActivity.this.searchItem == null) {
                return null;
            }
            return ProfileActivity.this.searchItem;
        }
    }

    public ProfileActivity(Bundle bundle) {
        this(bundle, null);
    }

    public ProfileActivity(Bundle bundle, SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader) {
        super(bundle);
        this.nameTextView = new SimpleTextView[2];
        this.nameTextViewRightDrawableContentDescription = null;
        this.onlineTextView = new SimpleTextView[2];
        this.scrimView = null;
        this.scrimPaint = new AnonymousClass1(1);
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
        this.HEADER_SHADOW = new AnonymousClass2("headerShadow");
        this.provider = new AnonymousClass3();
        this.ACTIONBAR_HEADER_PROGRESS = new AnonymousClass28("animationProgress");
        this.scrimAnimatorSet = null;
        this.savedScrollPosition = -1;
        this.sharedMediaPreloader = sharedMediaPreloader;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.userId = this.arguments.getLong("user_id", 0L);
        this.chatId = this.arguments.getLong("chat_id", 0L);
        this.banFromGroup = this.arguments.getLong("ban_chat_id", 0L);
        this.reportSpam = this.arguments.getBoolean("reportSpam", false);
        if (!this.expandPhoto) {
            boolean z = this.arguments.getBoolean("expandPhoto", false);
            this.expandPhoto = z;
            if (z) {
                this.needSendMessage = true;
            }
        }
        if (this.userId != 0) {
            long j = this.arguments.getLong("dialog_id", 0L);
            this.dialogId = j;
            if (j != 0) {
                this.currentEncryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(this.dialogId)));
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
                ImageUpdater imageUpdater = new ImageUpdater(true);
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
                CountDownLatch countDownLatch = new CountDownLatch(1);
                getMessagesStorage().getStorageQueue().postRunnable(new ProfileActivity$$ExternalSyntheticLambda20(this, countDownLatch));
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
            if (this.currentChat.megagroup) {
                getChannelParticipants(true);
            } else {
                this.participantsMap = null;
            }
            getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.chatOnlineCountDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.groupCallUpdated);
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
        }
        if (this.sharedMediaPreloader == null) {
            this.sharedMediaPreloader = new SharedMediaLayout.SharedMediaPreloader(this);
        }
        this.sharedMediaPreloader.addDelegate(this);
        getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
        getNotificationCenter().addObserver(this, NotificationCenter.didReceiveNewMessages);
        getNotificationCenter().addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        updateRowsIds();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        if (this.arguments.containsKey("preload_messages")) {
            getMessagesController().ensureMessagesLoaded(this.userId, 0, null);
        }
        return true;
    }

    public /* synthetic */ void lambda$onFragmentCreate$0(CountDownLatch countDownLatch) {
        this.currentChat = getMessagesStorage().getChat(this.chatId);
        countDownLatch.countDown();
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
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, this.resourcesProvider);
        boolean z = true;
        anonymousClass4.setForceSkipTouches(true);
        anonymousClass4.setBackgroundColor(0);
        anonymousClass4.setItemsBackgroundColor(getThemedColor("avatar_actionBarSelectorBlue"), false);
        anonymousClass4.setItemsColor(getThemedColor("actionBarDefaultIcon"), false);
        anonymousClass4.setBackButtonDrawable(new BackDrawable(false));
        anonymousClass4.setCastShadows(false);
        anonymousClass4.setAddToContainer(false);
        anonymousClass4.setClipContent(true);
        if (Build.VERSION.SDK_INT < 21 || AndroidUtilities.isTablet() || this.inBubbleMode) {
            z = false;
        }
        anonymousClass4.setOccupyStatusBar(z);
        ImageView backButton = anonymousClass4.getBackButton();
        backButton.setOnLongClickListener(new ProfileActivity$$ExternalSyntheticLambda16(this, backButton));
        return anonymousClass4;
    }

    /* renamed from: org.telegram.ui.ProfileActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends ActionBar {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            ProfileActivity.this = r1;
        }

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
    }

    public /* synthetic */ boolean lambda$createActionBar$2(ImageView imageView, View view) {
        ActionBarPopupWindow show = BackButtonMenu.show(this, imageView, getDialogId(), this.resourcesProvider);
        if (show != null) {
            show.setOnDismissListener(new ProfileActivity$$ExternalSyntheticLambda17(this));
            dimBehindView(imageView, 0.3f);
            UndoView undoView = this.undoView;
            if (undoView != null) {
                undoView.hide(true, 1);
            }
            return true;
        }
        return false;
    }

    public /* synthetic */ void lambda$createActionBar$1() {
        dimBehindView(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        Object obj;
        int i;
        Theme.ResourcesProvider resourcesProvider;
        ChatAvatarContainer avatarContainer;
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
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass5());
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
        long j2 = j;
        this.fragmentView = new AnonymousClass6(context);
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        ArrayList<Integer> arrayList = (tLRPC$ChatFull == null || (tLRPC$ChatParticipants = tLRPC$ChatFull.participants) == null || tLRPC$ChatParticipants.participants.size() <= 5) ? null : this.sortedUsers;
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
        TLRPC$UserFull tLRPC$UserFull = this.userInfo;
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(context, j2, sharedMediaPreloader, tLRPC$UserFull != null ? tLRPC$UserFull.common_chats_count : 0, this.sortedUsers, this.chatInfo, arrayList != null, this, this, 1, this.resourcesProvider);
        this.sharedMediaLayout = anonymousClass7;
        anonymousClass7.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (this.userId == getUserConfig().clientUserId) {
            ActionBarMenuItem addItem = createMenu.addItem(37, 2131165884, getResourceProvider());
            this.qrItem = addItem;
            addItem.setContentDescription(LocaleController.getString("GetQRCode", 2131626056));
            updateQrItemVisibility(false);
            if (ContactsController.getInstance(this.currentAccount).getPrivacyRules(7) == null) {
                ContactsController.getInstance(this.currentAccount).loadPrivacySettings();
            }
        }
        if (this.imageUpdater != null) {
            ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(32, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass8());
            this.searchItem = actionBarMenuItemSearchListener;
            actionBarMenuItemSearchListener.setContentDescription(LocaleController.getString("SearchInSettings", 2131628116));
            this.searchItem.setSearchFieldHint(LocaleController.getString("SearchInSettings", 2131628116));
            this.sharedMediaLayout.getSearchItem().setVisibility(8);
            if (this.expandPhoto) {
                this.searchItem.setVisibility(8);
            }
        }
        ActionBarMenuItem addItem2 = createMenu.addItem(16, 2131166097);
        this.videoCallItem = addItem2;
        addItem2.setContentDescription(LocaleController.getString("VideoCall", 2131628881));
        if (this.chatId != 0) {
            this.callItem = createMenu.addItem(15, 2131165985);
            if (ChatObject.isChannelOrGiga(this.currentChat)) {
                this.callItem.setContentDescription(LocaleController.getString("VoipChannelVoiceChat", 2131629000));
            } else {
                this.callItem.setContentDescription(LocaleController.getString("VoipGroupVoiceChat", 2131629130));
            }
        } else {
            ActionBarMenuItem addItem3 = createMenu.addItem(15, 2131165464);
            this.callItem = addItem3;
            addItem3.setContentDescription(LocaleController.getString("Call", 2131624779));
        }
        ActionBarMenuItem addItem4 = createMenu.addItem(12, 2131165440);
        this.editItem = addItem4;
        addItem4.setContentDescription(LocaleController.getString("Edit", 2131625537));
        this.otherItem = createMenu.addItem(10, 2131165453, this.resourcesProvider);
        ImageView imageView = new ImageView(context);
        this.ttlIconView = imageView;
        imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("actionBarDefaultIcon"), PorterDuff.Mode.MULTIPLY));
        AndroidUtilities.updateViewVisibilityAnimated(this.ttlIconView, false, 0.8f, false);
        this.ttlIconView.setImageResource(2131165803);
        this.otherItem.addView(this.ttlIconView, LayoutHelper.createFrame(12, 12.0f, 19, 8.0f, 2.0f, 0.0f, 0.0f));
        this.otherItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
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
            i = findFirstVisibleItemPosition;
            obj = this.writeButton.getTag();
        }
        createActionBarMenu(false);
        this.listAdapter = new ListAdapter(context);
        this.searchAdapter = new SearchAdapter(context);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        this.avatarDrawable = avatarDrawable;
        avatarDrawable.setProfile(true);
        this.fragmentView.setWillNotDraw(false);
        View view = this.fragmentView;
        NestedFrameLayout nestedFrameLayout = (NestedFrameLayout) view;
        this.contentView = nestedFrameLayout;
        nestedFrameLayout.needBlur = true;
        FrameLayout frameLayout = (FrameLayout) view;
        AnonymousClass9 anonymousClass9 = new AnonymousClass9(context);
        this.listView = anonymousClass9;
        anonymousClass9.setVerticalScrollBarEnabled(false);
        AnonymousClass10 anonymousClass10 = new AnonymousClass10();
        this.listView.setItemAnimator(anonymousClass10);
        anonymousClass10.setSupportsChangeAnimations(false);
        anonymousClass10.setDelayAnimations(false);
        this.listView.setClipToPadding(false);
        this.listView.setHideIfEmpty(false);
        AnonymousClass11 anonymousClass11 = new AnonymousClass11(context);
        this.layoutManager = anonymousClass11;
        anonymousClass11.setOrientation(1);
        LinearLayoutManager linearLayoutManager = this.layoutManager;
        linearLayoutManager.mIgnoreTopPadding = false;
        this.listView.setLayoutManager(linearLayoutManager);
        this.listView.setGlowColor(0);
        this.listView.setAdapter(this.listAdapter);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setOnItemClickListener(new ProfileActivity$$ExternalSyntheticLambda38(this, j2, context));
        this.listView.setOnItemLongClickListener(new AnonymousClass13());
        if (this.searchItem != null) {
            RecyclerListView recyclerListView = new RecyclerListView(context);
            this.searchListView = recyclerListView;
            recyclerListView.setVerticalScrollBarEnabled(false);
            this.searchListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
            this.searchListView.setGlowColor(getThemedColor("avatar_backgroundActionBarBlue"));
            this.searchListView.setAdapter(this.searchAdapter);
            resourcesProvider = null;
            this.searchListView.setItemAnimator(null);
            this.searchListView.setVisibility(8);
            this.searchListView.setLayoutAnimation(null);
            this.searchListView.setBackgroundColor(getThemedColor("windowBackgroundWhite"));
            frameLayout.addView(this.searchListView, LayoutHelper.createFrame(-1, -1, 51));
            this.searchListView.setOnItemClickListener(new ProfileActivity$$ExternalSyntheticLambda37(this));
            this.searchListView.setOnItemLongClickListener(new ProfileActivity$$ExternalSyntheticLambda39(this));
            this.searchListView.setOnScrollListener(new AnonymousClass14());
            this.searchListView.setAnimateEmptyView(true, 1);
            StickerEmptyView stickerEmptyView = new StickerEmptyView(context, null, 1);
            this.emptyView = stickerEmptyView;
            stickerEmptyView.setAnimateLayoutChange(true);
            this.emptyView.subtitle.setVisibility(8);
            this.emptyView.setVisibility(8);
            frameLayout.addView(this.emptyView);
            this.searchAdapter.loadFaqWebPage();
        } else {
            resourcesProvider = null;
        }
        if (this.banFromGroup != 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.banFromGroup));
            if (this.currentChannelParticipant == null) {
                TLRPC$TL_channels_getParticipant tLRPC$TL_channels_getParticipant = new TLRPC$TL_channels_getParticipant();
                tLRPC$TL_channels_getParticipant.channel = MessagesController.getInputChannel(chat);
                tLRPC$TL_channels_getParticipant.participant = getMessagesController().getInputPeer(this.userId);
                getConnectionsManager().sendRequest(tLRPC$TL_channels_getParticipant, new ProfileActivity$$ExternalSyntheticLambda33(this));
            }
            AnonymousClass15 anonymousClass15 = new AnonymousClass15(this, context);
            anonymousClass15.setWillNotDraw(false);
            frameLayout.addView(anonymousClass15, LayoutHelper.createFrame(-1, 51, 83));
            anonymousClass15.setOnClickListener(new ProfileActivity$$ExternalSyntheticLambda13(this, chat));
            TextView textView = new TextView(context);
            textView.setTextColor(getThemedColor("windowBackgroundWhiteRedText"));
            textView.setTextSize(1, 15.0f);
            textView.setGravity(17);
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView.setText(LocaleController.getString("BanFromTheGroup", 2131624664));
            anonymousClass15.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 1.0f, 0.0f, 0.0f));
            this.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, AndroidUtilities.dp(48.0f));
            this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
        } else {
            this.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, 0);
        }
        TopView topView = new TopView(context);
        this.topView = topView;
        topView.setBackgroundColor(getThemedColor("avatar_backgroundActionBarBlue"));
        frameLayout.addView(this.topView);
        this.contentView.blurBehindViews.add(this.topView);
        this.avatarContainer = new FrameLayout(context);
        AnonymousClass17 anonymousClass17 = new AnonymousClass17(context);
        this.avatarContainer2 = anonymousClass17;
        AndroidUtilities.updateViewVisibilityAnimated(anonymousClass17, true, 1.0f, false);
        frameLayout.addView(this.avatarContainer2, LayoutHelper.createFrame(-1, -1.0f, 8388611, 0.0f, 0.0f, 0.0f, 0.0f));
        this.avatarContainer.setPivotX(0.0f);
        this.avatarContainer.setPivotY(0.0f);
        this.avatarContainer2.addView(this.avatarContainer, LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
        AnonymousClass18 anonymousClass18 = new AnonymousClass18(this, context);
        this.avatarImage = anonymousClass18;
        anonymousClass18.getImageReceiver().setAllowDecodeSingleFrame(true);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0f));
        this.avatarImage.setPivotX(0.0f);
        this.avatarImage.setPivotY(0.0f);
        this.avatarContainer.addView(this.avatarImage, LayoutHelper.createFrame(-1, -1.0f));
        this.avatarImage.setOnClickListener(new ProfileActivity$$ExternalSyntheticLambda12(this));
        this.avatarImage.setOnLongClickListener(new ProfileActivity$$ExternalSyntheticLambda15(this));
        AnonymousClass19 anonymousClass19 = new AnonymousClass19(context);
        this.avatarProgressView = anonymousClass19;
        anonymousClass19.setSize(AndroidUtilities.dp(26.0f));
        this.avatarProgressView.setProgressColor(-1);
        this.avatarProgressView.setNoProgress(false);
        this.avatarContainer.addView(this.avatarProgressView, LayoutHelper.createFrame(-1, -1.0f));
        ImageView imageView2 = new ImageView(context);
        this.timeItem = imageView2;
        imageView2.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
        this.timeItem.setScaleType(ImageView.ScaleType.CENTER);
        this.timeItem.setAlpha(0.0f);
        ImageView imageView3 = this.timeItem;
        TimerDrawable timerDrawable = new TimerDrawable(context, resourcesProvider);
        this.timerDrawable = timerDrawable;
        imageView3.setImageDrawable(timerDrawable);
        frameLayout.addView(this.timeItem, LayoutHelper.createFrame(34, 34, 51));
        showAvatarProgress(false, false);
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        if (profileGalleryView != null) {
            profileGalleryView.onDestroy();
        }
        this.overlaysView = new OverlaysView(context);
        long j3 = this.userId;
        if (j3 == 0) {
            j3 = -this.chatId;
        }
        ProfileGalleryView profileGalleryView2 = new ProfileGalleryView(context, j3, this.actionBar, this.listView, this.avatarImage, getClassGuid(), this.overlaysView);
        this.avatarsViewPager = profileGalleryView2;
        profileGalleryView2.setChatInfo(this.chatInfo);
        this.avatarContainer2.addView(this.avatarsViewPager);
        this.avatarContainer2.addView(this.overlaysView);
        this.avatarImage.setAvatarsViewPager(this.avatarsViewPager);
        PagerIndicatorView pagerIndicatorView = new PagerIndicatorView(context);
        this.avatarsViewPagerIndicatorView = pagerIndicatorView;
        this.avatarContainer2.addView(pagerIndicatorView, LayoutHelper.createFrame(-1, -1.0f));
        frameLayout.addView(this.actionBar);
        float f = 54 + ((!this.callItemVisible || this.userId == 0) ? 0 : 54);
        int i2 = -2;
        ActionBarLayout actionBarLayout = this.parentLayout;
        if (actionBarLayout != null && (actionBarLayout.getLastFragment() instanceof ChatActivity) && (avatarContainer = ((ChatActivity) this.parentLayout.getLastFragment()).getAvatarContainer()) != null && avatarContainer.getLayoutParams() != null && avatarContainer.getTitleTextView() != null) {
            f = (((ViewGroup.MarginLayoutParams) avatarContainer.getLayoutParams()).rightMargin + (avatarContainer.getWidth() - avatarContainer.getTitleTextView().getRight())) / AndroidUtilities.density;
            i2 = (int) (avatarContainer.getTitleTextView().getWidth() / AndroidUtilities.density);
        }
        float f2 = f;
        int i3 = 0;
        while (true) {
            SimpleTextView[] simpleTextViewArr = this.nameTextView;
            if (i3 >= simpleTextViewArr.length) {
                break;
            }
            if (this.playProfileAnimation != 0 || i3 != 0) {
                simpleTextViewArr[i3] = new AnonymousClass20(context);
                if (i3 == 1) {
                    this.nameTextView[i3].setTextColor(getThemedColor("profile_title"));
                } else {
                    this.nameTextView[i3].setTextColor(getThemedColor("actionBarDefaultTitle"));
                }
                this.nameTextView[i3].setTextSize(18);
                this.nameTextView[i3].setGravity(3);
                this.nameTextView[i3].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.nameTextView[i3].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
                this.nameTextView[i3].setPivotX(0.0f);
                this.nameTextView[i3].setPivotY(0.0f);
                this.nameTextView[i3].setAlpha(i3 == 0 ? 0.0f : 1.0f);
                if (i3 == 1) {
                    this.nameTextView[i3].setScrollNonFitText(true);
                    this.nameTextView[i3].setImportantForAccessibility(2);
                }
                this.nameTextView[i3].setFocusable(i3 == 0);
                this.avatarContainer2.addView(this.nameTextView[i3], LayoutHelper.createFrame(i3 == 0 ? i2 : -2, -2.0f, 51, 118.0f, 0.0f, i3 == 0 ? f2 : 0.0f, 0.0f));
            }
            i3++;
        }
        int i4 = 0;
        while (true) {
            SimpleTextView[] simpleTextViewArr2 = this.onlineTextView;
            if (i4 >= simpleTextViewArr2.length) {
                break;
            }
            simpleTextViewArr2[i4] = new SimpleTextView(context);
            this.onlineTextView[i4].setTextColor(getThemedColor("avatar_subtitleInProfileBlue"));
            this.onlineTextView[i4].setTextSize(14);
            this.onlineTextView[i4].setGravity(3);
            this.onlineTextView[i4].setAlpha((i4 == 0 || i4 == 2) ? 0.0f : 1.0f);
            if (i4 > 0) {
                this.onlineTextView[i4].setImportantForAccessibility(2);
            }
            this.onlineTextView[i4].setFocusable(i4 == 0);
            this.avatarContainer2.addView(this.onlineTextView[i4], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, i4 == 0 ? f2 : 8.0f, 0.0f));
            i4++;
        }
        AnonymousClass21 anonymousClass21 = new AnonymousClass21(context, context);
        this.mediaCounterTextView = anonymousClass21;
        anonymousClass21.setAlpha(0.0f);
        this.avatarContainer2.addView(this.mediaCounterTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, 8.0f, 0.0f));
        updateProfileData(true);
        this.writeButton = new RLottieImageView(context);
        Drawable mutate = context.getResources().getDrawable(2131165415).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
        CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), getThemedColor("profile_actionBackground"), getThemedColor("profile_actionPressedBackground")), 0, 0);
        combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        this.writeButton.setBackground(combinedDrawable);
        if (this.userId != 0) {
            if (this.imageUpdater != null) {
                this.cameraDrawable = new RLottieDrawable(2131558415, String.valueOf(2131558415), AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f), false, null);
                this.cellCameraDrawable = new RLottieDrawable(2131558415, "2131558415_cell", AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f), false, null);
                this.writeButton.setAnimation(this.cameraDrawable);
                this.writeButton.setContentDescription(LocaleController.getString("AccDescrChangeProfilePicture", 2131623974));
                this.writeButton.setPadding(AndroidUtilities.dp(2.0f), 0, 0, AndroidUtilities.dp(2.0f));
            } else {
                this.writeButton.setImageResource(2131166095);
                this.writeButton.setContentDescription(LocaleController.getString("AccDescrOpenChat", 2131624028));
            }
        } else {
            this.writeButton.setImageResource(2131166094);
            this.writeButton.setContentDescription(LocaleController.getString("ViewDiscussion", 2131628914));
        }
        this.writeButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("profile_actionIcon"), PorterDuff.Mode.MULTIPLY));
        this.writeButton.setScaleType(ImageView.ScaleType.CENTER);
        frameLayout.addView(this.writeButton, LayoutHelper.createFrame(60, 60.0f, 53, 0.0f, 0.0f, 16.0f, 0.0f));
        this.writeButton.setOnClickListener(new ProfileActivity$$ExternalSyntheticLambda11(this));
        needLayout(false);
        if (i != -1 && obj != null) {
            this.writeButton.setTag(0);
            this.writeButton.setScaleX(0.2f);
            this.writeButton.setScaleY(0.2f);
            this.writeButton.setAlpha(0.0f);
        }
        this.listView.setOnScrollListener(new AnonymousClass22());
        UndoView undoView = new UndoView(context, null, false, this.resourcesProvider);
        this.undoView = undoView;
        frameLayout.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.expandAnimator = ofFloat;
        ofFloat.addUpdateListener(new ProfileActivity$$ExternalSyntheticLambda1(this));
        this.expandAnimator.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
        this.expandAnimator.addListener(new AnonymousClass23());
        updateRowsIds();
        updateSelectedMediaTabText();
        HintView hintView = new HintView(getParentActivity(), 9);
        this.fwdRestrictedHint = hintView;
        hintView.setAlpha(0.0f);
        frameLayout.addView(this.fwdRestrictedHint, LayoutHelper.createFrame(-2, -2.0f, 51, 12.0f, 0.0f, 12.0f, 0.0f));
        this.sharedMediaLayout.setForwardRestrictedHint(this.fwdRestrictedHint);
        AnonymousClass24 anonymousClass24 = new AnonymousClass24(Build.VERSION.SDK_INT >= 21 ? (ViewGroup) getParentActivity().getWindow().getDecorView() : frameLayout, frameLayout);
        this.pinchToZoomHelper = anonymousClass24;
        anonymousClass24.setCallback(new AnonymousClass25());
        this.avatarsViewPager.setPinchToZoomHelper(this.pinchToZoomHelper);
        this.scrimPaint.setAlpha(0);
        this.actionBarBackgroundPaint.setColor(getThemedColor("listSelectorSDK21"));
        this.contentView.blurBehindViews.add(this.sharedMediaLayout);
        updateTtlIcon();
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ProfileActivity$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass5() {
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            String str;
            long j;
            if (ProfileActivity.this.getParentActivity() == null) {
                return;
            }
            if (i == -1) {
                ProfileActivity.this.finishFragment();
                return;
            }
            String str2 = null;
            String str3 = null;
            Integer num = null;
            String str4 = null;
            boolean z = false;
            if (i == 2) {
                TLRPC$User user = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                if (user == null) {
                    return;
                }
                if (!ProfileActivity.this.isBot || MessagesController.isSupportUser(user)) {
                    if (ProfileActivity.this.userBlocked) {
                        ProfileActivity.this.getMessagesController().unblockPeer(ProfileActivity.this.userId);
                        if (!BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                            return;
                        }
                        BulletinFactory.createBanBulletin(ProfileActivity.this, false).show();
                    } else if (ProfileActivity.this.reportSpam) {
                        ProfileActivity profileActivity = ProfileActivity.this;
                        AlertsCreator.showBlockReportSpamAlert(profileActivity, profileActivity.userId, user, null, ProfileActivity.this.currentEncryptedChat, false, null, new ProfileActivity$5$$ExternalSyntheticLambda9(this), ProfileActivity.this.resourcesProvider);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                        builder.setTitle(LocaleController.getString("BlockUser", 2131624679));
                        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureBlockContact2", 2131624422, ContactsController.formatName(user.first_name, user.last_name))));
                        builder.setPositiveButton(LocaleController.getString("BlockContact", 2131624678), new ProfileActivity$5$$ExternalSyntheticLambda0(this));
                        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                        AlertDialog create = builder.create();
                        ProfileActivity.this.showDialog(create);
                        TextView textView = (TextView) create.getButton(-1);
                        if (textView == null) {
                            return;
                        }
                        textView.setTextColor(ProfileActivity.this.getThemedColor("dialogTextRed2"));
                    }
                } else if (!ProfileActivity.this.userBlocked) {
                    ProfileActivity.this.getMessagesController().blockPeer(ProfileActivity.this.userId);
                } else {
                    ProfileActivity.this.getMessagesController().unblockPeer(ProfileActivity.this.userId);
                    ProfileActivity.this.getSendMessagesHelper().sendMessage("/start", ProfileActivity.this.userId, null, null, null, false, null, null, null, true, 0, null);
                    ProfileActivity.this.finishFragment();
                }
            } else if (i == 1) {
                TLRPC$User user2 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", user2.id);
                bundle.putBoolean("addContact", true);
                ProfileActivity profileActivity2 = ProfileActivity.this;
                profileActivity2.presentFragment(new ContactAddActivity(bundle, profileActivity2.resourcesProvider));
            } else if (i == 3) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("onlySelect", true);
                bundle2.putInt("dialogsType", 3);
                bundle2.putString("selectAlertString", LocaleController.getString("SendContactToText", 2131628182));
                bundle2.putString("selectAlertStringGroup", LocaleController.getString("SendContactToGroupText", 2131628181));
                DialogsActivity dialogsActivity = new DialogsActivity(bundle2);
                dialogsActivity.setDelegate(ProfileActivity.this);
                ProfileActivity.this.presentFragment(dialogsActivity);
            } else if (i == 4) {
                Bundle bundle3 = new Bundle();
                bundle3.putLong("user_id", ProfileActivity.this.userId);
                ProfileActivity profileActivity3 = ProfileActivity.this;
                profileActivity3.presentFragment(new ContactAddActivity(bundle3, profileActivity3.resourcesProvider));
            } else if (i == 5) {
                TLRPC$User user3 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                if (user3 == null || ProfileActivity.this.getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                builder2.setTitle(LocaleController.getString("DeleteContact", 2131625398));
                builder2.setMessage(LocaleController.getString("AreYouSureDeleteContact", 2131624438));
                builder2.setPositiveButton(LocaleController.getString("Delete", 2131625368), new ProfileActivity$5$$ExternalSyntheticLambda4(this, user3));
                builder2.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                AlertDialog create2 = builder2.create();
                ProfileActivity.this.showDialog(create2);
                TextView textView2 = (TextView) create2.getButton(-1);
                if (textView2 == null) {
                    return;
                }
                textView2.setTextColor(ProfileActivity.this.getThemedColor("dialogTextRed2"));
            } else if (i == 7) {
                ProfileActivity.this.leaveChatPressed();
            } else if (i == 12) {
                Bundle bundle4 = new Bundle();
                bundle4.putLong("chat_id", ProfileActivity.this.chatId);
                ChatEditActivity chatEditActivity = new ChatEditActivity(bundle4);
                chatEditActivity.setInfo(ProfileActivity.this.chatInfo);
                ProfileActivity.this.presentFragment(chatEditActivity);
            } else if (i == 9) {
                TLRPC$User user4 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                if (user4 == null) {
                    return;
                }
                Bundle bundle5 = new Bundle();
                bundle5.putBoolean("onlySelect", true);
                bundle5.putInt("dialogsType", 2);
                bundle5.putBoolean("resetDelegate", false);
                bundle5.putBoolean("closeFragment", false);
                DialogsActivity dialogsActivity2 = new DialogsActivity(bundle5);
                dialogsActivity2.setDelegate(new ProfileActivity$5$$ExternalSyntheticLambda11(this, user4, dialogsActivity2));
                ProfileActivity.this.presentFragment(dialogsActivity2);
            } else if (i == 10) {
                try {
                    if (ProfileActivity.this.userId != 0) {
                        TLRPC$User user5 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                        if (user5 == null) {
                            return;
                        }
                        if (ProfileActivity.this.botInfo != null && ProfileActivity.this.userInfo != null && !TextUtils.isEmpty(ProfileActivity.this.userInfo.about)) {
                            str = String.format("%s https://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", ProfileActivity.this.userInfo.about, user5.username);
                        } else {
                            str = String.format("https://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", user5.username);
                        }
                        str2 = str;
                    } else if (ProfileActivity.this.chatId != 0) {
                        TLRPC$Chat chat = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(ProfileActivity.this.chatId));
                        if (chat == null) {
                            return;
                        }
                        if (ProfileActivity.this.chatInfo != null && !TextUtils.isEmpty(ProfileActivity.this.chatInfo.about)) {
                            str2 = String.format("%s\nhttps://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", ProfileActivity.this.chatInfo.about, chat.username);
                        } else {
                            str2 = String.format("https://" + ProfileActivity.this.getMessagesController().linkPrefix + "/%s", chat.username);
                        }
                    }
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", str2);
                    ProfileActivity.this.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("BotShare", 2131624734)), 500);
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
                    if (user6 == null) {
                        return;
                    }
                    VoIPHelper.startCall(user6, i == 16, ProfileActivity.this.userInfo != null && ProfileActivity.this.userInfo.video_calls_available, ProfileActivity.this.getParentActivity(), ProfileActivity.this.userInfo, ProfileActivity.this.getAccountInstance());
                } else if (ProfileActivity.this.chatId == 0) {
                } else {
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
                ProfileActivity.this.presentFragment(new StatisticActivity(bundle7));
            } else if (i == 22) {
                ProfileActivity.this.openDiscussion();
            } else if (i == 20) {
                AlertDialog.Builder builder3 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                builder3.setTitle(LocaleController.getString("AreYouSureSecretChatTitle", 2131624462));
                builder3.setMessage(LocaleController.getString("AreYouSureSecretChat", 2131624461));
                builder3.setPositiveButton(LocaleController.getString("Start", 2131628413), new ProfileActivity$5$$ExternalSyntheticLambda2(this));
                builder3.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                ProfileActivity.this.showDialog(builder3.create());
            } else if (i == 21) {
                if (ProfileActivity.this.getParentActivity() == null) {
                    return;
                }
                int i2 = Build.VERSION.SDK_INT;
                if (i2 < 23 || ((i2 > 28 && !BuildVars.NO_SCOPED_STORAGE) || ProfileActivity.this.getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
                    ImageLocation imageLocation = ProfileActivity.this.avatarsViewPager.getImageLocation(ProfileActivity.this.avatarsViewPager.getRealPosition());
                    if (imageLocation == null) {
                        return;
                    }
                    if (imageLocation.imageType == 2) {
                        z = true;
                    }
                    FileLoader fileLoader = FileLoader.getInstance(((BaseFragment) ProfileActivity.this).currentAccount);
                    TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation.location;
                    if (z) {
                        str3 = "mp4";
                    }
                    File pathToAttach = fileLoader.getPathToAttach(tLRPC$TL_fileLocationToBeDeprecated, str3, true);
                    if (!pathToAttach.exists()) {
                        return;
                    }
                    MediaController.saveFile(pathToAttach.toString(), ProfileActivity.this.getParentActivity(), 0, null, null, new ProfileActivity$5$$ExternalSyntheticLambda7(this, z));
                    return;
                }
                ProfileActivity.this.getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
            } else if (i == 30) {
                ProfileActivity profileActivity6 = ProfileActivity.this;
                profileActivity6.presentFragment(new ChangeNameActivity(profileActivity6.resourcesProvider));
            } else if (i == 31) {
                ProfileActivity.this.presentFragment(new LogoutActivity());
            } else if (i == 33) {
                int realPosition = ProfileActivity.this.avatarsViewPager.getRealPosition();
                TLRPC$Photo photo = ProfileActivity.this.avatarsViewPager.getPhoto(realPosition);
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
                UserConfig userConfig = ProfileActivity.this.getUserConfig();
                ProfileActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_photos_updateProfilePhoto, new ProfileActivity$5$$ExternalSyntheticLambda10(this, userConfig, photo));
                UndoView undoView = ProfileActivity.this.undoView;
                long j2 = ProfileActivity.this.userId;
                if (!photo.video_sizes.isEmpty()) {
                    num = 1;
                }
                undoView.showWithAction(j2, 22, num);
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
                    NotificationCenter.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
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
                    str4 = FileLoader.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getPathToAttach(PhotoViewer.getFileLocation(realImageLocation), PhotoViewer.getFileLocationExt(realImageLocation), true).getAbsolutePath();
                }
                ProfileActivity.this.imageUpdater.openPhotoForEdit(pathToAttach2.getAbsolutePath(), str4, 0, z2);
            } else if (i != 35) {
                if (i == 36) {
                    ProfileActivity.this.onWriteButtonClick();
                } else if (i != 37 || ProfileActivity.this.qrItem == null || ProfileActivity.this.qrItem.getAlpha() <= 0.0f) {
                } else {
                    Bundle bundle8 = new Bundle();
                    bundle8.putLong("chat_id", ProfileActivity.this.chatId);
                    bundle8.putLong("user_id", ProfileActivity.this.userId);
                    ProfileActivity.this.presentFragment(new QrActivity(bundle8));
                }
            } else {
                AlertDialog.Builder builder4 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                ImageLocation imageLocation3 = ProfileActivity.this.avatarsViewPager.getImageLocation(ProfileActivity.this.avatarsViewPager.getRealPosition());
                if (imageLocation3 == null) {
                    return;
                }
                if (imageLocation3.imageType == 2) {
                    builder4.setTitle(LocaleController.getString("AreYouSureDeleteVideoTitle", 2131624458));
                    builder4.setMessage(LocaleController.formatString("AreYouSureDeleteVideo", 2131624456, new Object[0]));
                } else {
                    builder4.setTitle(LocaleController.getString("AreYouSureDeletePhotoTitle", 2131624447));
                    builder4.setMessage(LocaleController.formatString("AreYouSureDeletePhoto", 2131624445, new Object[0]));
                }
                builder4.setPositiveButton(LocaleController.getString("Delete", 2131625368), new ProfileActivity$5$$ExternalSyntheticLambda1(this));
                builder4.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                AlertDialog create3 = builder4.create();
                ProfileActivity.this.showDialog(create3);
                TextView textView3 = (TextView) create3.getButton(-1);
                if (textView3 == null) {
                    return;
                }
                textView3.setTextColor(ProfileActivity.this.getThemedColor("dialogTextRed2"));
            }
        }

        public /* synthetic */ void lambda$onItemClick$0(int i) {
            if (i == 1) {
                NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
                ProfileActivity profileActivity = ProfileActivity.this;
                int i2 = NotificationCenter.closeChats;
                notificationCenter.removeObserver(profileActivity, i2);
                ProfileActivity.this.getNotificationCenter().postNotificationName(i2, new Object[0]);
                ProfileActivity.this.playProfileAnimation = 0;
                ProfileActivity.this.finishFragment();
                return;
            }
            ProfileActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.peerSettingsDidLoad, Long.valueOf(ProfileActivity.this.userId));
        }

        public /* synthetic */ void lambda$onItemClick$1(DialogInterface dialogInterface, int i) {
            ProfileActivity.this.getMessagesController().blockPeer(ProfileActivity.this.userId);
            if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                BulletinFactory.createBanBulletin(ProfileActivity.this, true).show();
            }
        }

        public /* synthetic */ void lambda$onItemClick$2(TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
            ArrayList<TLRPC$User> arrayList = new ArrayList<>();
            arrayList.add(tLRPC$User);
            ProfileActivity.this.getContactsController().deleteContact(arrayList, true);
        }

        public /* synthetic */ void lambda$onItemClick$6(TLRPC$User tLRPC$User, DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z) {
            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
            long longValue = ((Long) arrayList.get(0)).longValue();
            TLRPC$Chat chat = MessagesController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getChat(Long.valueOf(-longValue));
            if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
                ProfileActivity.this.getMessagesController().checkIsInChat(chat, tLRPC$User, new ProfileActivity$5$$ExternalSyntheticLambda8(this, longValue, dialogsActivity));
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
            builder.setTitle(LocaleController.getString("AddBot", 2131624250));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", 2131624271, UserObject.getUserName(tLRPC$User), chat == null ? "" : chat.title)));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            builder.setPositiveButton(LocaleController.getString("AddBot", 2131624250), new ProfileActivity$5$$ExternalSyntheticLambda3(this, longValue, dialogsActivity2, tLRPC$User));
            ProfileActivity.this.showDialog(builder.create());
        }

        public /* synthetic */ void lambda$onItemClick$4(long j, DialogsActivity dialogsActivity, boolean z, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, String str) {
            AndroidUtilities.runOnUIThread(new ProfileActivity$5$$ExternalSyntheticLambda5(this, j, tLRPC$TL_chatAdminRights, str, z, dialogsActivity));
        }

        /* renamed from: org.telegram.ui.ProfileActivity$5$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
            final /* synthetic */ DialogsActivity val$fragment;

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC$User tLRPC$User) {
            }

            AnonymousClass1(DialogsActivity dialogsActivity) {
                AnonymousClass5.this = r1;
                this.val$fragment = dialogsActivity;
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
                ProfileActivity.this.disableProfileAnimation = true;
                this.val$fragment.removeSelfFromStack();
                NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
                ProfileActivity profileActivity = ProfileActivity.this;
                int i2 = NotificationCenter.closeChats;
                notificationCenter.removeObserver(profileActivity, i2);
                ProfileActivity.this.getNotificationCenter().postNotificationName(i2, new Object[0]);
            }
        }

        public /* synthetic */ void lambda$onItemClick$3(long j, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, String str, boolean z, DialogsActivity dialogsActivity) {
            ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(ProfileActivity.this.userId, -j, tLRPC$TL_chatAdminRights, null, null, str, 2, true, !z, null);
            chatRightsEditActivity.setDelegate(new AnonymousClass1(dialogsActivity));
            ProfileActivity.this.presentFragment(chatRightsEditActivity);
        }

        public /* synthetic */ void lambda$onItemClick$5(long j, DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
            ProfileActivity.this.disableProfileAnimation = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            long j2 = -j;
            bundle.putLong("chat_id", j2);
            if (!ProfileActivity.this.getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
                return;
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
            ProfileActivity profileActivity = ProfileActivity.this;
            int i2 = NotificationCenter.closeChats;
            notificationCenter.removeObserver(profileActivity, i2);
            ProfileActivity.this.getNotificationCenter().postNotificationName(i2, new Object[0]);
            ProfileActivity.this.getMessagesController().addUserToChat(j2, tLRPC$User, 0, null, chatActivity, true, null, null);
            ProfileActivity.this.presentFragment(chatActivity, true);
        }

        public /* synthetic */ void lambda$onItemClick$7(DialogInterface dialogInterface, int i) {
            ProfileActivity.this.creatingChat = true;
            ProfileActivity.this.getSecretChatHelper().startSecretChat(ProfileActivity.this.getParentActivity(), ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId)));
        }

        public /* synthetic */ void lambda$onItemClick$8(boolean z) {
            if (ProfileActivity.this.getParentActivity() == null) {
                return;
            }
            BulletinFactory.createSaveToGalleryBulletin(ProfileActivity.this, z, null).show();
        }

        public /* synthetic */ void lambda$onItemClick$10(UserConfig userConfig, TLRPC$Photo tLRPC$Photo, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new ProfileActivity$5$$ExternalSyntheticLambda6(this, tLObject, userConfig, tLRPC$Photo));
        }

        public /* synthetic */ void lambda$onItemClick$9(TLObject tLObject, UserConfig userConfig, TLRPC$Photo tLRPC$Photo) {
            ProfileActivity.this.avatarsViewPager.finishSettingMainPhoto();
            if (tLObject instanceof TLRPC$TL_photos_photo) {
                TLRPC$TL_photos_photo tLRPC$TL_photos_photo = (TLRPC$TL_photos_photo) tLObject;
                ProfileActivity.this.getMessagesController().putUsers(tLRPC$TL_photos_photo.users, false);
                TLRPC$User user = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(userConfig.clientUserId));
                if (!(tLRPC$TL_photos_photo.photo instanceof TLRPC$TL_photo)) {
                    return;
                }
                ProfileActivity.this.avatarsViewPager.replaceFirstPhoto(tLRPC$Photo, tLRPC$TL_photos_photo.photo);
                if (user == null) {
                    return;
                }
                user.photo.photo_id = tLRPC$TL_photos_photo.photo.id;
                userConfig.setCurrentUser(user);
                userConfig.saveConfig(true);
            }
        }

        public /* synthetic */ void lambda$onItemClick$11(DialogInterface dialogInterface, int i) {
            int realPosition = ProfileActivity.this.avatarsViewPager.getRealPosition();
            TLRPC$Photo photo = ProfileActivity.this.avatarsViewPager.getPhoto(realPosition);
            if (ProfileActivity.this.avatarsViewPager.getRealCount() == 1) {
                ProfileActivity.this.setForegroundImage(true);
            }
            if (photo == null || ProfileActivity.this.avatarsViewPager.getRealPosition() == 0) {
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
                if (findViewByPosition == null) {
                    return;
                }
                ProfileActivity.this.listView.smoothScrollBy(0, findViewByPosition.getTop() - AndroidUtilities.dp(88.0f), CubicBezierInterpolator.EASE_OUT_QUINT);
            }
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends NestedFrameLayout {
        private boolean ignoreLayout;
        private Paint grayPaint = new Paint();
        private final ArrayList<View> sortedChildren = new ArrayList<>();
        private final Comparator<View> viewComparator = ProfileActivity$6$$ExternalSyntheticLambda0.INSTANCE;

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            ProfileActivity.this = r1;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!ProfileActivity.this.pinchToZoomHelper.isInOverlayMode()) {
                if (ProfileActivity.this.sharedMediaLayout == null || !ProfileActivity.this.sharedMediaLayout.isInFastScroll() || !ProfileActivity.this.sharedMediaLayout.isPinnedToTop()) {
                    if (ProfileActivity.this.sharedMediaLayout != null && ProfileActivity.this.sharedMediaLayout.checkPinchToZoom(motionEvent)) {
                        return true;
                    }
                    return super.dispatchTouchEvent(motionEvent);
                }
                return ProfileActivity.this.sharedMediaLayout.dispatchFastScrollEvent(motionEvent);
            }
            return ProfileActivity.this.pinchToZoomHelper.onTouchEvent(motionEvent);
        }

        /* JADX WARN: Removed duplicated region for block: B:133:0x049b  */
        @Override // android.widget.FrameLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            boolean z;
            int i3;
            int i4;
            int i5;
            boolean z2;
            RecyclerView.ViewHolder findContainingViewHolder;
            int i6;
            int i7;
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
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
            View view = null;
            boolean z3 = true;
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
                for (int i8 = 0; i8 < itemCount; i8++) {
                    int itemViewType = ProfileActivity.this.listAdapter.getItemViewType(i8);
                    ProfileActivity.this.positionToOffset.put(Integer.valueOf(i8), Integer.valueOf(ProfileActivity.this.listContentHeight));
                    if (itemViewType != 13) {
                        RecyclerView.ViewHolder createViewHolder = ProfileActivity.this.listAdapter.createViewHolder(null, itemViewType);
                        ProfileActivity.this.listAdapter.onBindViewHolder(createViewHolder, i8);
                        createViewHolder.itemView.measure(makeMeasureSpec, makeMeasureSpec2);
                        ProfileActivity.access$7512(ProfileActivity.this, createViewHolder.itemView.getMeasuredHeight());
                    } else {
                        ProfileActivity profileActivity = ProfileActivity.this;
                        ProfileActivity.access$7512(profileActivity, profileActivity.listView.getMeasuredHeight());
                    }
                }
                if (ProfileActivity.this.emptyView != null) {
                    ((FrameLayout.LayoutParams) ProfileActivity.this.emptyView.getLayoutParams()).topMargin = AndroidUtilities.dp(88.0f) + AndroidUtilities.statusBarHeight;
                }
            }
            if (ProfileActivity.this.fragmentOpened || (!ProfileActivity.this.expandPhoto && (!ProfileActivity.this.openAnimationInProgress || ProfileActivity.this.playProfileAnimation != 2))) {
                if (!ProfileActivity.this.fragmentOpened || ProfileActivity.this.openAnimationInProgress || ProfileActivity.this.firstLayout) {
                    return;
                }
                this.ignoreLayout = true;
                if (!ProfileActivity.this.isInLandscapeMode && !AndroidUtilities.isTablet()) {
                    i4 = ProfileActivity.this.listView.getMeasuredWidth();
                    i3 = Math.max(0, getMeasuredHeight() - ((ProfileActivity.this.listContentHeight + AndroidUtilities.dp(88.0f)) + currentActionBarHeight));
                } else {
                    i4 = AndroidUtilities.dp(88.0f);
                    i3 = 0;
                }
                if (ProfileActivity.this.banFromGroup == 0) {
                    ProfileActivity.this.listView.setBottomGlowOffset(0);
                } else {
                    i3 += AndroidUtilities.dp(48.0f);
                    ProfileActivity.this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
                }
                int paddingTop = ProfileActivity.this.listView.getPaddingTop();
                int i9 = 0;
                while (true) {
                    if (i9 >= ProfileActivity.this.listView.getChildCount()) {
                        i5 = -1;
                        break;
                    }
                    i5 = ProfileActivity.this.listView.getChildAdapterPosition(ProfileActivity.this.listView.getChildAt(i9));
                    if (i5 != -1) {
                        view = ProfileActivity.this.listView.getChildAt(i9);
                        break;
                    }
                    i9++;
                }
                if (view == null && (view = ProfileActivity.this.listView.getChildAt(0)) != null && (i5 = (findContainingViewHolder = ProfileActivity.this.listView.findContainingViewHolder(view)).getAdapterPosition()) == -1) {
                    i5 = findContainingViewHolder.getPosition();
                }
                int top = view != null ? view.getTop() : i4;
                if (!((BaseFragment) ProfileActivity.this).actionBar.isSearchFieldVisible() || ProfileActivity.this.sharedMediaRow < 0) {
                    if (ProfileActivity.this.invalidateScroll || paddingTop != i4) {
                        ProfileActivity profileActivity2 = ProfileActivity.this;
                        if (profileActivity2.savedScrollPosition >= 0) {
                            LinearLayoutManager linearLayoutManager = profileActivity2.layoutManager;
                            ProfileActivity profileActivity3 = ProfileActivity.this;
                            linearLayoutManager.scrollToPositionWithOffset(profileActivity3.savedScrollPosition, profileActivity3.savedScrollOffset - i4);
                        } else if ((!z || !profileActivity2.allowPullingDown) && view != null) {
                            if (i5 == 0 && !ProfileActivity.this.allowPullingDown && top > AndroidUtilities.dp(88.0f)) {
                                top = AndroidUtilities.dp(88.0f);
                            }
                            ProfileActivity.this.layoutManager.scrollToPositionWithOffset(i5, top - i4);
                        } else {
                            ProfileActivity.this.layoutManager.scrollToPositionWithOffset(0, AndroidUtilities.dp(88.0f) - i4);
                        }
                    }
                    z2 = false;
                    if (paddingTop == i4 || ProfileActivity.this.listView.getPaddingBottom() != i3) {
                        ProfileActivity.this.listView.setPadding(0, i4, 0, i3);
                    } else {
                        z3 = z2;
                    }
                    if (z3) {
                        measureChildWithMargins(ProfileActivity.this.listView, i, 0, i2, 0);
                        try {
                            ProfileActivity.this.listView.layout(0, currentActionBarHeight, ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getMeasuredHeight() + currentActionBarHeight);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    this.ignoreLayout = false;
                    return;
                }
                ProfileActivity.this.layoutManager.scrollToPositionWithOffset(ProfileActivity.this.sharedMediaRow, -i4);
                z2 = true;
                if (paddingTop == i4) {
                }
                ProfileActivity.this.listView.setPadding(0, i4, 0, i3);
                if (z3) {
                }
                this.ignoreLayout = false;
                return;
            }
            this.ignoreLayout = true;
            if (ProfileActivity.this.expandPhoto) {
                if (ProfileActivity.this.searchItem != null) {
                    ProfileActivity.this.searchItem.setAlpha(0.0f);
                    ProfileActivity.this.searchItem.setEnabled(false);
                    ProfileActivity.this.searchItem.setVisibility(8);
                }
                ProfileActivity.this.nameTextView[1].setTextColor(-1);
                ProfileActivity.this.onlineTextView[1].setTextColor(Color.argb(179, 255, 255, 255));
                ((BaseFragment) ProfileActivity.this).actionBar.setItemsBackgroundColor(1090519039, false);
                ((BaseFragment) ProfileActivity.this).actionBar.setItemsColor(-1, false);
                ProfileActivity.this.overlaysView.setOverlaysVisible();
                ProfileActivity.this.overlaysView.setAlphaValue(1.0f, false);
                ProfileActivity.this.avatarImage.setForegroundAlpha(1.0f);
                ProfileActivity.this.avatarContainer.setVisibility(8);
                ProfileActivity.this.avatarsViewPager.resetCurrentItem();
                ProfileActivity.this.avatarsViewPager.setVisibility(0);
                ProfileActivity.this.expandPhoto = false;
            }
            ProfileActivity.this.allowPullingDown = true;
            ProfileActivity.this.isPulledDown = true;
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
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
                i7 = ProfileActivity.this.listView.getMeasuredWidth();
                i6 = Math.max(0, getMeasuredHeight() - ((ProfileActivity.this.listContentHeight + AndroidUtilities.dp(88.0f)) + currentActionBarHeight));
            } else {
                i7 = AndroidUtilities.dp(88.0f);
                i6 = 0;
            }
            if (ProfileActivity.this.banFromGroup == 0) {
                ProfileActivity.this.listView.setBottomGlowOffset(0);
            } else {
                i6 += AndroidUtilities.dp(48.0f);
                ProfileActivity.this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
            }
            ProfileActivity.this.initialAnimationExtraHeight = i7 - currentActionBarHeight;
            ProfileActivity.this.layoutManager.scrollToPositionWithOffset(0, -currentActionBarHeight);
            ProfileActivity.this.listView.setPadding(0, i7, 0, i6);
            measureChildWithMargins(ProfileActivity.this.listView, i, 0, i2, 0);
            ProfileActivity.this.listView.layout(0, currentActionBarHeight, ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getMeasuredHeight() + currentActionBarHeight);
            this.ignoreLayout = false;
        }

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

        public static /* synthetic */ int lambda$$0(View view, View view2) {
            return (int) (view.getY() - view2.getY());
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            FragmentContextView fragmentContextView;
            int top;
            boolean z;
            int i;
            ProfileActivity.this.whitePaint.setColor(ProfileActivity.this.getThemedColor("windowBackgroundWhite"));
            float f = 1.0f;
            if (ProfileActivity.this.listView.getVisibility() != 0) {
                canvas.drawRect(0.0f, ProfileActivity.this.searchListView.getTop() + ProfileActivity.this.extraHeight + ProfileActivity.this.searchTransitionOffset, getMeasuredWidth(), top + getMeasuredHeight(), ProfileActivity.this.whitePaint);
            } else {
                this.grayPaint.setColor(ProfileActivity.this.getThemedColor("windowBackgroundGray"));
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
                float f2 = y;
                boolean z3 = false;
                float f3 = 1.0f;
                for (int i3 = 0; i3 < size; i3++) {
                    View view = this.sortedChildren.get(i3);
                    boolean z4 = view.getBackground() != null;
                    int y2 = (int) (ProfileActivity.this.listView.getY() + view.getY());
                    if (z3 == z4) {
                        if (view.getAlpha() == 1.0f) {
                            f3 = 1.0f;
                        }
                    } else {
                        if (z3) {
                            z = z4;
                            canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), y2, this.grayPaint);
                            i = y2;
                        } else {
                            z = z4;
                            if (f3 != 1.0f) {
                                float f4 = y2;
                                i = y2;
                                canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), f4, this.grayPaint);
                                ProfileActivity.this.whitePaint.setAlpha((int) (f3 * 255.0f));
                                canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), f4, ProfileActivity.this.whitePaint);
                                ProfileActivity.this.whitePaint.setAlpha(255);
                            } else {
                                i = y2;
                                canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), i, ProfileActivity.this.whitePaint);
                            }
                        }
                        f2 = i;
                        f3 = view.getAlpha();
                        z3 = z;
                    }
                }
                if (z3) {
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), this.grayPaint);
                } else if (f3 != 1.0f) {
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), this.grayPaint);
                    ProfileActivity.this.whitePaint.setAlpha((int) (f3 * 255.0f));
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), ProfileActivity.this.whitePaint);
                    ProfileActivity.this.whitePaint.setAlpha(255);
                } else {
                    canvas.drawRect(ProfileActivity.this.listView.getX(), f2, ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), ProfileActivity.this.listView.getBottom(), ProfileActivity.this.whitePaint);
                }
            }
            super.dispatchDraw(canvas);
            ProfileActivity profileActivity = ProfileActivity.this;
            if (profileActivity.profileTransitionInProgress && ((BaseFragment) profileActivity).parentLayout.fragmentsStack.size() > 1) {
                BaseFragment baseFragment = ((BaseFragment) ProfileActivity.this).parentLayout.fragmentsStack.get(((BaseFragment) ProfileActivity.this).parentLayout.fragmentsStack.size() - 2);
                if ((baseFragment instanceof ChatActivity) && (fragmentContextView = ((ChatActivity) baseFragment).getFragmentContextView()) != null && fragmentContextView.isCallStyle()) {
                    float dpf2 = ProfileActivity.this.extraHeight / AndroidUtilities.dpf2(fragmentContextView.getStyleHeight());
                    if (dpf2 <= 1.0f) {
                        f = dpf2;
                    }
                    canvas.save();
                    canvas.translate(fragmentContextView.getX(), fragmentContextView.getY());
                    fragmentContextView.setDrawOverlay(true);
                    fragmentContextView.setCollapseTransition(true, ProfileActivity.this.extraHeight, f);
                    fragmentContextView.draw(canvas);
                    fragmentContextView.setCollapseTransition(false, ProfileActivity.this.extraHeight, f);
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
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (!ProfileActivity.this.pinchToZoomHelper.isInOverlayMode() || !(view == ProfileActivity.this.avatarContainer2 || view == ((BaseFragment) ProfileActivity.this).actionBar || view == ProfileActivity.this.writeButton)) {
                return super.drawChild(canvas, view, j);
            }
            return true;
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends SharedMediaLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context, long j, SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader, int i, ArrayList arrayList, TLRPC$ChatFull tLRPC$ChatFull, boolean z, BaseFragment baseFragment, SharedMediaLayout.Delegate delegate, int i2, Theme.ResourcesProvider resourcesProvider) {
            super(context, j, sharedMediaPreloader, i, arrayList, tLRPC$ChatFull, z, baseFragment, delegate, i2, resourcesProvider);
            ProfileActivity.this = r15;
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
        protected void onSearchStateChanged(boolean z) {
            if (SharedConfig.smoothKeyboard) {
                AndroidUtilities.removeAdjustResize(ProfileActivity.this.getParentActivity(), ((BaseFragment) ProfileActivity.this).classGuid);
            }
            ProfileActivity.this.listView.stopScroll();
            ProfileActivity.this.avatarContainer2.setPivotY(ProfileActivity.this.avatarContainer.getPivotY() + (ProfileActivity.this.avatarContainer.getMeasuredHeight() / 2.0f));
            ProfileActivity.this.avatarContainer2.setPivotX(ProfileActivity.this.avatarContainer2.getMeasuredWidth() / 2.0f);
            AndroidUtilities.updateViewVisibilityAnimated(ProfileActivity.this.avatarContainer2, !z, 0.95f, true);
            int i = 4;
            ProfileActivity.this.callItem.setVisibility((z || !ProfileActivity.this.callItemVisible) ? 8 : 4);
            ProfileActivity.this.videoCallItem.setVisibility((z || !ProfileActivity.this.videoCallItemVisible) ? 8 : 4);
            ProfileActivity.this.editItem.setVisibility((z || !ProfileActivity.this.editItemVisible) ? 8 : 4);
            ProfileActivity.this.otherItem.setVisibility(z ? 8 : 4);
            if (ProfileActivity.this.qrItem != null) {
                ActionBarMenuItem actionBarMenuItem = ProfileActivity.this.qrItem;
                if (z) {
                    i = 8;
                }
                actionBarMenuItem.setVisibility(i);
            }
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout
        protected boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z) {
            return ProfileActivity.this.onMemberClick(tLRPC$ChatParticipant, z);
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout
        protected void drawBackgroundWithBlur(Canvas canvas, float f, Rect rect, Paint paint) {
            ProfileActivity.this.contentView.drawBlurRect(canvas, ProfileActivity.this.listView.getY() + getY() + f, rect, paint, true);
        }

        @Override // org.telegram.ui.Components.SharedMediaLayout
        protected void invalidateBlur() {
            ProfileActivity.this.contentView.invalidateBlur();
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass8() {
            ProfileActivity.this = r1;
        }

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
    }

    /* renamed from: org.telegram.ui.ProfileActivity$9 */
    /* loaded from: classes3.dex */
    class AnonymousClass9 extends RecyclerListView {
        private VelocityTracker velocityTracker;

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView
        public void requestChildOnScreen(View view, View view2) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(Context context) {
            super(context);
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        public boolean canHighlightChildAt(View view, float f, float f2) {
            return !(view instanceof AboutLinkCell);
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        public boolean allowSelectChildAtPosition(View view) {
            return view != ProfileActivity.this.sharedMediaLayout;
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
            View findViewByPosition;
            VelocityTracker velocityTracker;
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
            if ((action == 1 || action == 3) && ProfileActivity.this.allowPullingDown && (findViewByPosition = ProfileActivity.this.layoutManager.findViewByPosition(0)) != null) {
                if (ProfileActivity.this.isPulledDown) {
                    ProfileActivity.this.listView.smoothScrollBy(0, (findViewByPosition.getTop() - ProfileActivity.this.listView.getMeasuredWidth()) + ActionBar.getCurrentActionBarHeight() + (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0), CubicBezierInterpolator.EASE_OUT_QUINT);
                } else {
                    ProfileActivity.this.listView.smoothScrollBy(0, findViewByPosition.getTop() - AndroidUtilities.dp(88.0f), CubicBezierInterpolator.EASE_OUT_QUINT);
                }
            }
            return onTouchEvent;
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            if (getItemAnimator().isRunning() && view.getBackground() == null && view.getTranslationY() != 0.0f) {
                boolean z = ProfileActivity.this.listView.getChildAdapterPosition(view) == ProfileActivity.this.sharedMediaRow && view.getAlpha() != 1.0f;
                if (z) {
                    ProfileActivity.this.whitePaint.setAlpha((int) (ProfileActivity.this.listView.getAlpha() * 255.0f * view.getAlpha()));
                }
                canvas.drawRect(ProfileActivity.this.listView.getX(), view.getY(), ProfileActivity.this.listView.getX() + ProfileActivity.this.listView.getMeasuredWidth(), view.getY() + view.getHeight(), ProfileActivity.this.whitePaint);
                if (z) {
                    ProfileActivity.this.whitePaint.setAlpha((int) (ProfileActivity.this.listView.getAlpha() * 255.0f));
                }
            }
            return super.drawChild(canvas, view, j);
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends DefaultItemAnimator {
        int animationIndex = -1;

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getAddAnimationDelay(long j, long j2, long j3) {
            return 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getAddDuration() {
            return 220L;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getMoveAnimationDelay() {
            return 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getMoveDuration() {
            return 220L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getRemoveDuration() {
            return 220L;
        }

        AnonymousClass10() {
            ProfileActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        public void onAllAnimationsDone() {
            super.onAllAnimationsDone();
            AndroidUtilities.runOnUIThread(new ProfileActivity$10$$ExternalSyntheticLambda1(this));
        }

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
                ofFloat.addUpdateListener(new ProfileActivity$10$$ExternalSyntheticLambda0(this));
                ofFloat.setDuration(getMoveDuration());
                ofFloat.start();
                this.animationIndex = ProfileActivity.this.getNotificationCenter().setAnimationInProgress(this.animationIndex, null);
            }
            super.runPendingAnimations();
        }

        public /* synthetic */ void lambda$runPendingAnimations$1(ValueAnimator valueAnimator) {
            ProfileActivity.this.listView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$11 */
    /* loaded from: classes3.dex */
    class AnonymousClass11 extends LinearLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass11(Context context) {
            super(context);
            ProfileActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return ProfileActivity.this.imageUpdater != null;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
            boolean z = false;
            View findViewByPosition = ProfileActivity.this.layoutManager.findViewByPosition(0);
            if (findViewByPosition != null && !ProfileActivity.this.openingAvatar) {
                int top = findViewByPosition.getTop() - AndroidUtilities.dp(88.0f);
                if (ProfileActivity.this.allowPullingDown || top <= i) {
                    if (ProfileActivity.this.allowPullingDown) {
                        if (i >= top) {
                            ProfileActivity.this.allowPullingDown = false;
                        } else if (ProfileActivity.this.listView.getScrollState() == 1 && !ProfileActivity.this.isPulledDown) {
                            i /= 2;
                        }
                    }
                } else if (ProfileActivity.this.avatarsViewPager.hasImages() && ProfileActivity.this.avatarImage.getImageReceiver().hasNotThumb() && !AndroidUtilities.isAccessibilityScreenReaderEnabled() && !ProfileActivity.this.isInLandscapeMode && !AndroidUtilities.isTablet()) {
                    ProfileActivity profileActivity = ProfileActivity.this;
                    if (profileActivity.avatarBig == null) {
                        z = true;
                    }
                    profileActivity.allowPullingDown = z;
                }
                i = top;
            }
            return super.scrollVerticallyBy(i, recycler, state);
        }
    }

    public /* synthetic */ void lambda$createView$4(long j, Context context, View view, int i, float f, float f2) {
        TLRPC$ChatParticipant tLRPC$ChatParticipant;
        if (getParentActivity() == null) {
            return;
        }
        this.listView.stopScroll();
        if (i == this.settingsKeyRow) {
            Bundle bundle = new Bundle();
            bundle.putInt("chat_id", DialogObject.getEncryptedChatId(this.dialogId));
            presentFragment(new IdenticonActivity(bundle));
        } else if (i == this.settingsTimerRow) {
            showDialog(AlertsCreator.createTTLAlert(getParentActivity(), this.currentEncryptedChat, this.resourcesProvider).create());
        } else if (i == this.notificationsRow) {
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                boolean z = !notificationsCheckCell.isChecked();
                boolean isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(j);
                long j2 = 0;
                if (z) {
                    SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    if (isGlobalNotificationsEnabled) {
                        edit.remove("notify2_" + j);
                    } else {
                        edit.putInt("notify2_" + j, 0);
                    }
                    getMessagesStorage().setDialogFlags(j, 0L);
                    edit.commit();
                    TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
                    if (tLRPC$Dialog != null) {
                        tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
                    }
                } else {
                    SharedPreferences.Editor edit2 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    if (!isGlobalNotificationsEnabled) {
                        edit2.remove("notify2_" + j);
                    } else {
                        edit2.putInt("notify2_" + j, 2);
                        j2 = 1;
                    }
                    getNotificationsController().removeNotificationsForDialog(j);
                    getMessagesStorage().setDialogFlags(j, j2);
                    edit2.commit();
                    TLRPC$Dialog tLRPC$Dialog2 = getMessagesController().dialogs_dict.get(j);
                    if (tLRPC$Dialog2 != null) {
                        TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                        tLRPC$Dialog2.notify_settings = tLRPC$TL_peerNotifySettings;
                        if (isGlobalNotificationsEnabled) {
                            tLRPC$TL_peerNotifySettings.mute_until = Integer.MAX_VALUE;
                        }
                    }
                }
                getNotificationsController().updateServerNotificationsSettings(j);
                notificationsCheckCell.setChecked(z);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findViewHolderForPosition(this.notificationsRow);
                if (holder == null) {
                    return;
                }
                this.listAdapter.onBindViewHolder(holder, this.notificationsRow);
                return;
            }
            ChatNotificationsPopupWrapper chatNotificationsPopupWrapper = new ChatNotificationsPopupWrapper(context, this.currentAccount, null, true, true, new AnonymousClass12(j), getResourceProvider());
            chatNotificationsPopupWrapper.lambda$update$10(j);
            chatNotificationsPopupWrapper.showAsOptions(this, view, f, f2);
        } else if (i == this.unblockRow) {
            getMessagesController().unblockPeer(this.userId);
            if (!BulletinFactory.canShowBulletin(this)) {
                return;
            }
            BulletinFactory.createBanBulletin(this, false).show();
        } else if (i == this.addToGroupButtonRow) {
            try {
                this.actionBar.getActionBarMenuOnItemClick().onItemClick(9);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (i == this.sendMessageRow) {
            onWriteButtonClick();
        } else if (i == this.reportRow) {
            AlertsCreator.createReportAlert(getParentActivity(), getDialogId(), 0, this, this.resourcesProvider, null);
        } else if (i >= this.membersStartRow && i < this.membersEndRow) {
            if (!this.sortedUsers.isEmpty()) {
                tLRPC$ChatParticipant = this.chatInfo.participants.participants.get(this.sortedUsers.get(i - this.membersStartRow).intValue());
            } else {
                tLRPC$ChatParticipant = this.chatInfo.participants.participants.get(i - this.membersStartRow);
            }
            onMemberClick(tLRPC$ChatParticipant, false);
        } else if (i == this.addMemberRow) {
            openAddMember();
        } else if (i == this.usernameRow) {
            processOnClickOrPress(i, view);
        } else if (i == this.locationRow) {
            if (!(this.chatInfo.location instanceof TLRPC$TL_channelLocation)) {
                return;
            }
            LocationActivity locationActivity = new LocationActivity(5);
            locationActivity.setChatLocation(this.chatId, (TLRPC$TL_channelLocation) this.chatInfo.location);
            presentFragment(locationActivity);
        } else if (i == this.joinRow) {
            getMessagesController().addUserToChat(this.currentChat.id, getUserConfig().getCurrentUser(), 0, null, this, null);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeSearchByActiveAction, new Object[0]);
        } else if (i == this.subscribersRow) {
            Bundle bundle2 = new Bundle();
            bundle2.putLong("chat_id", this.chatId);
            bundle2.putInt("type", 2);
            ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle2);
            chatUsersActivity.setInfo(this.chatInfo);
            presentFragment(chatUsersActivity);
        } else if (i == this.subscribersRequestsRow) {
            presentFragment(new MemberRequestsActivity(this.chatId));
        } else if (i == this.administratorsRow) {
            Bundle bundle3 = new Bundle();
            bundle3.putLong("chat_id", this.chatId);
            bundle3.putInt("type", 1);
            ChatUsersActivity chatUsersActivity2 = new ChatUsersActivity(bundle3);
            chatUsersActivity2.setInfo(this.chatInfo);
            presentFragment(chatUsersActivity2);
        } else if (i == this.blockedUsersRow) {
            Bundle bundle4 = new Bundle();
            bundle4.putLong("chat_id", this.chatId);
            bundle4.putInt("type", 0);
            ChatUsersActivity chatUsersActivity3 = new ChatUsersActivity(bundle4);
            chatUsersActivity3.setInfo(this.chatInfo);
            presentFragment(chatUsersActivity3);
        } else if (i == this.notificationRow) {
            presentFragment(new NotificationsSettingsActivity());
        } else if (i == this.privacyRow) {
            presentFragment(new PrivacySettingsActivity());
        } else if (i == this.dataRow) {
            presentFragment(new DataSettingsActivity());
        } else if (i == this.chatRow) {
            presentFragment(new ThemeActivity(0));
        } else if (i == this.filtersRow) {
            presentFragment(new FiltersSetupActivity());
        } else if (i == this.stickersRow) {
            presentFragment(new StickersActivity(0));
        } else if (i == this.devicesRow) {
            presentFragment(new SessionsActivity(0));
        } else if (i == this.questionRow) {
            showDialog(AlertsCreator.createSupportAlert(this, this.resourcesProvider));
        } else if (i == this.faqRow) {
            Browser.openUrl(getParentActivity(), LocaleController.getString("TelegramFaqUrl", 2131628563));
        } else if (i == this.policyRow) {
            Browser.openUrl(getParentActivity(), LocaleController.getString("PrivacyPolicyUrl", 2131627730));
        } else if (i == this.sendLogsRow) {
            sendLogs(false);
        } else if (i == this.sendLastLogsRow) {
            sendLogs(true);
        } else if (i == this.clearLogsRow) {
            FileLog.cleanupLogs();
        } else if (i == this.switchBackendRow) {
            if (getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
            builder.setMessage(LocaleController.getString("AreYouSure", 2131624421));
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new ProfileActivity$$ExternalSyntheticLambda4(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            showDialog(builder.create());
        } else if (i == this.languageRow) {
            presentFragment(new LanguageSelectActivity());
        } else if (i == this.setUsernameRow) {
            presentFragment(new ChangeUsernameActivity());
        } else if (i == this.bioRow) {
            if (this.userInfo == null) {
                return;
            }
            presentFragment(new ChangeBioActivity());
        } else if (i == this.numberRow) {
            presentFragment(new ActionIntroActivity(3));
        } else if (i == this.setAvatarRow) {
            onWriteButtonClick();
        } else if (i == this.premiumRow) {
            presentFragment(new PremiumPreviewFragment("settings"));
        } else {
            processOnClickOrPress(i, view);
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 implements ChatNotificationsPopupWrapper.Callback {
        final /* synthetic */ long val$did;

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public /* synthetic */ void dismiss() {
            ChatNotificationsPopupWrapper.Callback.CC.$default$dismiss(this);
        }

        AnonymousClass12(long j) {
            ProfileActivity.this = r1;
            this.val$did = j;
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void toggleSound() {
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileActivity.this).currentAccount);
            boolean z = !notificationsSettings.getBoolean("sound_enabled_" + this.val$did, true);
            notificationsSettings.edit().putBoolean("sound_enabled_" + this.val$did, z).apply();
            if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                ProfileActivity profileActivity = ProfileActivity.this;
                BulletinFactory.createSoundEnabledBulletin(profileActivity, !z, profileActivity.getResourceProvider()).show();
            }
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void muteFor(int i) {
            if (i != 0) {
                ProfileActivity.this.getNotificationsController().muteUntil(this.val$did, i);
                if (BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                    ProfileActivity profileActivity = ProfileActivity.this;
                    BulletinFactory.createMuteBulletin(profileActivity, 5, i, profileActivity.getResourceProvider()).show();
                }
                if (ProfileActivity.this.notificationsRow < 0) {
                    return;
                }
                ProfileActivity.this.listAdapter.notifyItemChanged(ProfileActivity.this.notificationsRow);
                return;
            }
            if (ProfileActivity.this.getMessagesController().isDialogMuted(this.val$did)) {
                toggleMute();
            }
            if (!BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                return;
            }
            ProfileActivity profileActivity2 = ProfileActivity.this;
            BulletinFactory.createMuteBulletin(profileActivity2, 4, i, profileActivity2.getResourceProvider()).show();
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void showCustomize() {
            if (this.val$did != 0) {
                Bundle bundle = new Bundle();
                bundle.putLong("dialog_id", this.val$did);
                ProfileActivity profileActivity = ProfileActivity.this;
                profileActivity.presentFragment(new ProfileNotificationsActivity(bundle, profileActivity.resourcesProvider));
            }
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void toggleMute() {
            boolean isDialogMuted = ProfileActivity.this.getMessagesController().isDialogMuted(this.val$did);
            ProfileActivity.this.getNotificationsController().muteDialog(this.val$did, !isDialogMuted);
            BulletinFactory.createMuteBulletin(ProfileActivity.this, !isDialogMuted, null).show();
            if (ProfileActivity.this.notificationsRow >= 0) {
                ProfileActivity.this.listAdapter.notifyItemChanged(ProfileActivity.this.notificationsRow);
            }
        }
    }

    public /* synthetic */ void lambda$createView$3(DialogInterface dialogInterface, int i) {
        SharedConfig.pushAuthKey = null;
        SharedConfig.pushAuthKeyId = null;
        SharedConfig.saveConfig();
        getConnectionsManager().switchBackend(true);
    }

    /* renamed from: org.telegram.ui.ProfileActivity$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 implements RecyclerListView.OnItemLongClickListener {
        private int pressCount = 0;

        AnonymousClass13() {
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
        public boolean onItemClick(View view, int i) {
            String str;
            String str2;
            int i2;
            String str3;
            int i3;
            String str4;
            String str5;
            String str6;
            String str7;
            int i4;
            String str8;
            int i5;
            if (i != ProfileActivity.this.versionRow) {
                if (i < ProfileActivity.this.membersStartRow || i >= ProfileActivity.this.membersEndRow) {
                    return ProfileActivity.this.processOnClickOrPress(i, view);
                }
                return ProfileActivity.this.onMemberClick(!ProfileActivity.this.sortedUsers.isEmpty() ? (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(((Integer) ProfileActivity.this.sortedUsers.get(i - ProfileActivity.this.membersStartRow)).intValue()) : (TLRPC$ChatParticipant) ProfileActivity.this.visibleChatParticipants.get(i - ProfileActivity.this.membersStartRow), true);
            }
            int i6 = this.pressCount + 1;
            this.pressCount = i6;
            if (i6 >= 2 || BuildVars.DEBUG_PRIVATE_VERSION) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this.getParentActivity(), ProfileActivity.this.resourcesProvider);
                builder.setTitle(LocaleController.getString("DebugMenu", 2131625336));
                CharSequence[] charSequenceArr = new CharSequence[20];
                charSequenceArr[0] = LocaleController.getString("DebugMenuImportContacts", 2131625353);
                charSequenceArr[1] = LocaleController.getString("DebugMenuReloadContacts", 2131625355);
                charSequenceArr[2] = LocaleController.getString("DebugMenuResetContacts", 2131625356);
                charSequenceArr[3] = LocaleController.getString("DebugMenuResetDialogs", 2131625357);
                if (BuildVars.DEBUG_VERSION) {
                    str = null;
                } else {
                    if (BuildVars.LOGS_ENABLED) {
                        i5 = 2131625343;
                        str8 = "DebugMenuDisableLogs";
                    } else {
                        i5 = 2131625349;
                        str8 = "DebugMenuEnableLogs";
                    }
                    str = LocaleController.getString(str8, i5);
                }
                charSequenceArr[4] = str;
                if (SharedConfig.inappCamera) {
                    i2 = 2131625341;
                    str2 = "DebugMenuDisableCamera";
                } else {
                    i2 = 2131625347;
                    str2 = "DebugMenuEnableCamera";
                }
                charSequenceArr[5] = LocaleController.getString(str2, i2);
                charSequenceArr[6] = LocaleController.getString("DebugMenuClearMediaCache", 2131625339);
                charSequenceArr[7] = LocaleController.getString("DebugMenuCallSettings", 2131625337);
                charSequenceArr[8] = null;
                charSequenceArr[9] = (BuildVars.DEBUG_PRIVATE_VERSION || BuildVars.isStandaloneApp()) ? LocaleController.getString("DebugMenuCheckAppUpdate", 2131625338) : null;
                charSequenceArr[10] = LocaleController.getString("DebugMenuReadAllDialogs", 2131625354);
                if (SharedConfig.pauseMusicOnRecord) {
                    i3 = 2131625344;
                    str3 = "DebugMenuDisablePauseMusic";
                } else {
                    i3 = 2131625350;
                    str3 = "DebugMenuEnablePauseMusic";
                }
                charSequenceArr[11] = LocaleController.getString(str3, i3);
                if (!BuildVars.DEBUG_VERSION || AndroidUtilities.isTablet() || Build.VERSION.SDK_INT < 23) {
                    str4 = null;
                } else {
                    if (SharedConfig.smoothKeyboard) {
                        i4 = 2131625345;
                        str7 = "DebugMenuDisableSmoothKeyboard";
                    } else {
                        i4 = 2131625351;
                        str7 = "DebugMenuEnableSmoothKeyboard";
                    }
                    str4 = LocaleController.getString(str7, i4);
                }
                charSequenceArr[12] = str4;
                charSequenceArr[13] = BuildVars.DEBUG_PRIVATE_VERSION ? SharedConfig.disableVoiceAudioEffects ? "Enable voip audio effects" : "Disable voip audio effects" : null;
                int i7 = Build.VERSION.SDK_INT;
                charSequenceArr[14] = i7 >= 21 ? SharedConfig.noStatusBar ? "Show status bar background" : "Hide status bar background" : null;
                boolean z = BuildVars.DEBUG_PRIVATE_VERSION;
                charSequenceArr[15] = z ? "Clean app update" : null;
                charSequenceArr[16] = z ? "Reset suggestions" : null;
                if (z) {
                    str5 = LocaleController.getString(SharedConfig.forceRtmpStream ? 2131625342 : 2131625348);
                } else {
                    str5 = null;
                }
                charSequenceArr[17] = str5;
                charSequenceArr[18] = BuildVars.DEBUG_PRIVATE_VERSION ? LocaleController.getString(2131625340) : null;
                if (i7 >= 19) {
                    str6 = LocaleController.getString(SharedConfig.debugWebView ? 2131625346 : 2131625352);
                } else {
                    str6 = null;
                }
                charSequenceArr[19] = str6;
                builder.setItems(charSequenceArr, new ProfileActivity$13$$ExternalSyntheticLambda0(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                ProfileActivity.this.showDialog(builder.create());
            } else {
                try {
                    Toast.makeText(ProfileActivity.this.getParentActivity(), "¯\\_(ツ)_/¯", 0).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return true;
        }

        public /* synthetic */ void lambda$onItemClick$0(DialogInterface dialogInterface, int i) {
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
                BuildVars.LOGS_ENABLED = true ^ BuildVars.LOGS_ENABLED;
                ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).edit().putBoolean("logsEnabled", BuildVars.LOGS_ENABLED).commit();
                ProfileActivity.this.updateRowsIds();
                ProfileActivity.this.listAdapter.notifyDataSetChanged();
            } else if (i == 5) {
                SharedConfig.toggleInappCamera();
            } else if (i == 6) {
                ProfileActivity.this.getMessagesStorage().clearSentMedia();
                SharedConfig.setNoSoundHintShowed(false);
                MessagesController.getGlobalMainSettings().edit().remove("archivehint").remove("proximityhint").remove("archivehint_l").remove("gifhint").remove("reminderhint").remove("soundHint").remove("themehint").remove("bganimationhint").remove("filterhint").commit();
                MessagesController.getEmojiSettings(((BaseFragment) ProfileActivity.this).currentAccount).edit().remove("featured_hidden").commit();
                SharedConfig.textSelectionHintShows = 0;
                SharedConfig.lockRecordAudioVideoHint = 0;
                SharedConfig.stickersReorderingHintUsed = false;
                SharedConfig.forwardingOptionsHintShown = false;
                SharedConfig.messageSeenHintCount = 3;
                SharedConfig.emojiInteractionsHintCount = 3;
                SharedConfig.dayNightThemeSwitchHintCount = 3;
                SharedConfig.fastScrollHintCount = 3;
                ChatThemeController.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).clearCache();
            } else if (i == 7) {
                VoIPHelper.showCallDebugSettings(ProfileActivity.this.getParentActivity());
            } else if (i == 8) {
                SharedConfig.toggleRoundCamera16to9();
            } else if (i == 9) {
                ((LaunchActivity) ProfileActivity.this.getParentActivity()).checkAppUpdate(true);
            } else if (i == 10) {
                ProfileActivity.this.getMessagesStorage().readAllDialogs(-1);
            } else if (i == 11) {
                SharedConfig.togglePauseMusicOnRecord();
            } else if (i == 12) {
                SharedConfig.toggleSmoothKeyboard();
                if (!SharedConfig.smoothKeyboard || ProfileActivity.this.getParentActivity() == null) {
                    return;
                }
                ProfileActivity.this.getParentActivity().getWindow().setSoftInputMode(16);
            } else if (i == 13) {
                SharedConfig.toggleDisableVoiceAudioEffects();
            } else if (i == 14) {
                SharedConfig.toggleNoStatusBar();
                if (ProfileActivity.this.getParentActivity() == null || Build.VERSION.SDK_INT < 21) {
                    return;
                }
                if (SharedConfig.noStatusBar) {
                    ProfileActivity.this.getParentActivity().getWindow().setStatusBarColor(0);
                } else {
                    ProfileActivity.this.getParentActivity().getWindow().setStatusBarColor(855638016);
                }
            } else if (i == 15) {
                SharedConfig.pendingAppUpdate = null;
                SharedConfig.saveConfig();
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.appUpdateAvailable, new Object[0]);
            } else if (i == 16) {
                Set<String> set = ProfileActivity.this.getMessagesController().pendingSuggestions;
                set.add("VALIDATE_PHONE_NUMBER");
                set.add("VALIDATE_PASSWORD");
                ProfileActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.newSuggestionsAvailable, new Object[0]);
            } else if (i == 17) {
                SharedConfig.toggleForceRTMPStream();
            } else if (i == 18) {
                ApplicationLoader.applicationContext.deleteDatabase("webview.db");
                ApplicationLoader.applicationContext.deleteDatabase("webviewCache.db");
                WebStorage.getInstance().deleteAllData();
            } else if (i != 19) {
            } else {
                SharedConfig.toggleDebugWebView();
                Toast.makeText(ProfileActivity.this.getParentActivity(), LocaleController.getString(SharedConfig.debugWebView ? 2131625359 : 2131625358), 0).show();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$5(View view, int i) {
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
                            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.openArticle, this.searchAdapter.faqWebPage, ((MessagesController.FaqSearchResult) valueOf).url);
                        }
                    } else {
                        ((SearchAdapter.SearchResult) valueOf).open();
                    }
                    if (!z || valueOf == null) {
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
        if (!z) {
        }
    }

    public /* synthetic */ boolean lambda$createView$7(View view, int i) {
        if (this.searchAdapter.isSearchWas() || this.searchAdapter.recentSearches.isEmpty()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString("AppName", 2131624375));
        builder.setMessage(LocaleController.getString("ClearSearch", 2131625153));
        builder.setPositiveButton(LocaleController.getString("ClearButton", 2131625132).toUpperCase(), new ProfileActivity$$ExternalSyntheticLambda5(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        showDialog(builder.create());
        return true;
    }

    public /* synthetic */ void lambda$createView$6(DialogInterface dialogInterface, int i) {
        this.searchAdapter.clearRecent();
    }

    /* renamed from: org.telegram.ui.ProfileActivity$14 */
    /* loaded from: classes3.dex */
    class AnonymousClass14 extends RecyclerView.OnScrollListener {
        AnonymousClass14() {
            ProfileActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(ProfileActivity.this.getParentActivity().getCurrentFocus());
            }
        }
    }

    public /* synthetic */ void lambda$createView$8(TLObject tLObject) {
        this.currentChannelParticipant = ((TLRPC$TL_channels_channelParticipant) tLObject).participant;
    }

    public /* synthetic */ void lambda$createView$9(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda21(this, tLObject));
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$15 */
    /* loaded from: classes3.dex */
    class AnonymousClass15 extends FrameLayout {
        AnonymousClass15(ProfileActivity profileActivity, Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), intrinsicHeight);
            Theme.chat_composeShadowDrawable.draw(canvas);
            canvas.drawRect(0.0f, intrinsicHeight, getMeasuredWidth(), getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        AnonymousClass16() {
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            ProfileActivity.this.removeSelfFromStack();
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ProfileActivity.this.undoView.showWithAction(-ProfileActivity.this.chatId, ProfileActivity.this.currentChat.megagroup ? 10 : 9, tLRPC$User);
        }
    }

    public /* synthetic */ void lambda$createView$10(TLRPC$Chat tLRPC$Chat, View view) {
        long j = this.userId;
        long j2 = this.banFromGroup;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights = tLRPC$Chat.default_banned_rights;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant = this.currentChannelParticipant;
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, j2, null, tLRPC$TL_chatBannedRights, tLRPC$ChannelParticipant != null ? tLRPC$ChannelParticipant.banned_rights : null, "", 1, true, false, null);
        chatRightsEditActivity.setDelegate(new AnonymousClass16());
        presentFragment(chatRightsEditActivity);
    }

    /* renamed from: org.telegram.ui.ProfileActivity$17 */
    /* loaded from: classes3.dex */
    class AnonymousClass17 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass17(Context context) {
            super(context);
            ProfileActivity.this = r1;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (ProfileActivity.this.transitionOnlineText != null) {
                canvas.save();
                canvas.translate(ProfileActivity.this.onlineTextView[0].getX(), ProfileActivity.this.onlineTextView[0].getY());
                canvas.saveLayerAlpha(0.0f, 0.0f, ProfileActivity.this.transitionOnlineText.getMeasuredWidth(), ProfileActivity.this.transitionOnlineText.getMeasuredHeight(), (int) ((1.0f - ProfileActivity.this.animationProgress) * 255.0f), 31);
                ProfileActivity.this.transitionOnlineText.draw(canvas);
                canvas.restore();
                canvas.restore();
                invalidate();
            }
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$18 */
    /* loaded from: classes3.dex */
    class AnonymousClass18 extends AvatarImageView {
        AnonymousClass18(ProfileActivity profileActivity, Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (getImageReceiver().hasNotThumb()) {
                accessibilityNodeInfo.setText(LocaleController.getString("AccDescrProfilePicture", 2131624046));
                if (Build.VERSION.SDK_INT < 21) {
                    return;
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString("Open", 2131627090)));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, LocaleController.getString("AccDescrOpenInPhotoViewer", 2131624029)));
                return;
            }
            accessibilityNodeInfo.setVisibleToUser(false);
        }
    }

    public /* synthetic */ void lambda$createView$11(View view) {
        RecyclerView.ViewHolder findContainingViewHolder;
        Integer num;
        if (this.avatarBig != null) {
            return;
        }
        if (!AndroidUtilities.isTablet() && !this.isInLandscapeMode && this.avatarImage.getImageReceiver().hasNotThumb() && !AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
            this.openingAvatar = true;
            this.allowPullingDown = true;
            View view2 = null;
            int i = 0;
            while (true) {
                if (i >= this.listView.getChildCount()) {
                    break;
                }
                RecyclerListView recyclerListView = this.listView;
                if (recyclerListView.getChildAdapterPosition(recyclerListView.getChildAt(i)) == 0) {
                    view2 = this.listView.getChildAt(i);
                    break;
                }
                i++;
            }
            if (view2 != null && (findContainingViewHolder = this.listView.findContainingViewHolder(view2)) != null && (num = this.positionToOffset.get(Integer.valueOf(findContainingViewHolder.getAdapterPosition()))) != null) {
                this.listView.smoothScrollBy(0, -(num.intValue() + ((this.listView.getPaddingTop() - view2.getTop()) - this.actionBar.getMeasuredHeight())), CubicBezierInterpolator.EASE_OUT_QUINT);
                return;
            }
        }
        openAvatar();
    }

    public /* synthetic */ boolean lambda$createView$12(View view) {
        if (this.avatarBig != null) {
            return false;
        }
        openAvatar();
        return false;
    }

    /* renamed from: org.telegram.ui.ProfileActivity$19 */
    /* loaded from: classes3.dex */
    class AnonymousClass19 extends RadialProgressView {
        private Paint paint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass19(Context context) {
            super(context);
            ProfileActivity.this = r1;
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setColor(1426063360);
        }

        @Override // org.telegram.ui.Components.RadialProgressView, android.view.View
        public void onDraw(Canvas canvas) {
            if (ProfileActivity.this.avatarImage != null && ProfileActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                this.paint.setAlpha((int) (ProfileActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
                canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() / 2.0f, this.paint);
            }
            super.onDraw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$20 */
    /* loaded from: classes3.dex */
    class AnonymousClass20 extends SimpleTextView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass20(Context context) {
            super(context);
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (!isFocusable() || ProfileActivity.this.nameTextViewRightDrawableContentDescription == null) {
                return;
            }
            accessibilityNodeInfo.setText(((Object) getText()) + ", " + ProfileActivity.this.nameTextViewRightDrawableContentDescription);
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$21 */
    /* loaded from: classes3.dex */
    class AnonymousClass21 extends AudioPlayerAlert.ClippingTextViewSwitcher {
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass21(Context context, Context context2) {
            super(context);
            ProfileActivity.this = r1;
            this.val$context = context2;
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
        protected TextView createTextView() {
            TextView textView = new TextView(this.val$context);
            textView.setTextColor(ProfileActivity.this.getThemedColor("player_actionBarSubtitle"));
            textView.setTextSize(1, 14.0f);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(3);
            return textView;
        }
    }

    public /* synthetic */ void lambda$createView$13(View view) {
        if (this.writeButton.getTag() != null) {
            return;
        }
        onWriteButtonClick();
    }

    /* renamed from: org.telegram.ui.ProfileActivity$22 */
    /* loaded from: classes3.dex */
    class AnonymousClass22 extends RecyclerView.OnScrollListener {
        AnonymousClass22() {
            ProfileActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            boolean z = true;
            if (i == 1) {
                AndroidUtilities.hideKeyboard(ProfileActivity.this.getParentActivity().getCurrentFocus());
            }
            if (ProfileActivity.this.openingAvatar && i != 2) {
                ProfileActivity.this.openingAvatar = false;
            }
            if (ProfileActivity.this.searchItem != null) {
                ProfileActivity.this.scrolling = i != 0;
                ActionBarMenuItem actionBarMenuItem = ProfileActivity.this.searchItem;
                if (ProfileActivity.this.scrolling || ProfileActivity.this.isPulledDown) {
                    z = false;
                }
                actionBarMenuItem.setEnabled(z);
            }
            ProfileActivity.this.sharedMediaLayout.scrollingByUser = ProfileActivity.this.listView.scrollingByUser;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (ProfileActivity.this.fwdRestrictedHint != null) {
                ProfileActivity.this.fwdRestrictedHint.hide();
            }
            ProfileActivity.this.checkListViewScroll();
            boolean z = false;
            if (ProfileActivity.this.participantsMap != null && !ProfileActivity.this.usersEndReached && ProfileActivity.this.layoutManager.findLastVisibleItemPosition() > ProfileActivity.this.membersEndRow - 8) {
                ProfileActivity.this.getChannelParticipants(false);
            }
            SharedMediaLayout sharedMediaLayout = ProfileActivity.this.sharedMediaLayout;
            if (ProfileActivity.this.sharedMediaLayout.getY() == 0.0f) {
                z = true;
            }
            sharedMediaLayout.setPinnedToTop(z);
        }
    }

    public /* synthetic */ void lambda$createView$14(ValueAnimator valueAnimator) {
        int i;
        SimpleTextView[] simpleTextViewArr;
        int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);
        float[] fArr = this.expandAnimatorValues;
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.currentExpanAnimatorFracture = animatedFraction;
        float lerp = AndroidUtilities.lerp(fArr, animatedFraction);
        this.avatarContainer.setScaleX(this.avatarScale);
        this.avatarContainer.setScaleY(this.avatarScale);
        this.avatarContainer.setTranslationX(AndroidUtilities.lerp(this.avatarX, 0.0f, lerp));
        this.avatarContainer.setTranslationY(AndroidUtilities.lerp((float) Math.ceil(this.avatarY), 0.0f, lerp));
        this.avatarImage.setRoundRadius((int) AndroidUtilities.lerp(AndroidUtilities.dpf2(21.0f), 0.0f, lerp));
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (actionBarMenuItem != null) {
            float f = 1.0f - lerp;
            actionBarMenuItem.setAlpha(f);
            this.searchItem.setScaleY(f);
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
            scamDrawable.setColor(ColorUtils.blendARGB(getThemedColor("avatar_subtitleInProfileBlue"), Color.argb(179, 255, 255, 255), lerp));
        }
        Drawable drawable = this.lockIconDrawable;
        if (drawable != null) {
            drawable.setColorFilter(ColorUtils.blendARGB(getThemedColor("chat_lockIcon"), -1, lerp), PorterDuff.Mode.MULTIPLY);
        }
        CrossfadeDrawable crossfadeDrawable = this.verifiedCrossfadeDrawable;
        if (crossfadeDrawable != null) {
            crossfadeDrawable.setProgress(lerp);
        }
        CrossfadeDrawable crossfadeDrawable2 = this.premuimCrossfadeDrawable;
        if (crossfadeDrawable2 != null) {
            crossfadeDrawable2.setProgress(lerp);
        }
        float dpf2 = AndroidUtilities.dpf2(8.0f);
        float dpf22 = AndroidUtilities.dpf2(16.0f) - this.nameTextView[1].getLeft();
        float f2 = currentActionBarHeight;
        float dpf23 = ((this.extraHeight + f2) - AndroidUtilities.dpf2(38.0f)) - this.nameTextView[1].getBottom();
        float f3 = this.nameX;
        float f4 = this.nameY;
        float f5 = 1.0f - lerp;
        float f6 = f5 * f5;
        float f7 = f5 * 2.0f * lerp;
        float f8 = lerp * lerp;
        float f9 = (f3 * f6) + ((dpf2 + f3 + ((dpf22 - f3) / 2.0f)) * f7) + (dpf22 * f8);
        float f10 = (f4 * f6) + ((dpf2 + f4 + ((dpf23 - f4) / 2.0f)) * f7) + (dpf23 * f8);
        float dpf24 = AndroidUtilities.dpf2(16.0f) - this.onlineTextView[1].getLeft();
        float dpf25 = ((this.extraHeight + f2) - AndroidUtilities.dpf2(18.0f)) - this.onlineTextView[1].getBottom();
        float f11 = this.onlineX;
        float f12 = this.onlineY;
        float f13 = (f11 * f6) + ((dpf2 + f11 + ((dpf24 - f11) / 2.0f)) * f7) + (dpf24 * f8);
        float f14 = (f6 * f12) + (f7 * (dpf2 + f12 + ((dpf25 - f12) / 2.0f))) + (f8 * dpf25);
        this.nameTextView[1].setTranslationX(f9);
        this.nameTextView[1].setTranslationY(f10);
        this.onlineTextView[1].setTranslationX(f13);
        this.onlineTextView[1].setTranslationY(f14);
        this.mediaCounterTextView.setTranslationX(f13);
        this.mediaCounterTextView.setTranslationY(f14);
        Object tag = this.onlineTextView[1].getTag();
        if (tag instanceof String) {
            i = getThemedColor((String) tag);
        } else {
            i = getThemedColor("avatar_subtitleInProfileBlue");
        }
        this.onlineTextView[1].setTextColor(ColorUtils.blendARGB(i, Color.argb(179, 255, 255, 255), lerp));
        if (this.extraHeight > AndroidUtilities.dp(88.0f)) {
            this.nameTextView[1].setPivotY(AndroidUtilities.lerp(0, simpleTextViewArr[1].getMeasuredHeight(), lerp));
            this.nameTextView[1].setScaleX(AndroidUtilities.lerp(1.12f, 1.67f, lerp));
            this.nameTextView[1].setScaleY(AndroidUtilities.lerp(1.12f, 1.67f, lerp));
        }
        needLayoutText(Math.min(1.0f, this.extraHeight / AndroidUtilities.dp(88.0f)));
        this.nameTextView[1].setTextColor(ColorUtils.blendARGB(getThemedColor("profile_title"), -1, lerp));
        this.actionBar.setItemsColor(ColorUtils.blendARGB(getThemedColor("actionBarDefaultIcon"), -1, lerp), false);
        this.avatarImage.setForegroundAlpha(lerp);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.avatarContainer.getLayoutParams();
        layoutParams.width = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), this.listView.getMeasuredWidth() / this.avatarScale, lerp);
        layoutParams.height = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), (this.extraHeight + f2) / this.avatarScale, lerp);
        layoutParams.leftMargin = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(64.0f), 0.0f, lerp);
        this.avatarContainer.requestLayout();
    }

    /* renamed from: org.telegram.ui.ProfileActivity$23 */
    /* loaded from: classes3.dex */
    class AnonymousClass23 extends AnimatorListenerAdapter {
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        AnonymousClass23() {
            ProfileActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ((BaseFragment) ProfileActivity.this).actionBar.setItemsBackgroundColor(ProfileActivity.this.isPulledDown ? 1090519039 : ProfileActivity.this.getThemedColor("avatar_actionBarSelectorBlue"), false);
            ProfileActivity.this.avatarImage.clearForeground();
            ProfileActivity.this.doNotSetForeground = false;
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$24 */
    /* loaded from: classes3.dex */
    class AnonymousClass24 extends PinchToZoomHelper {
        Paint statusBarPaint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass24(ViewGroup viewGroup, ViewGroup viewGroup2) {
            super(viewGroup, viewGroup2);
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.PinchToZoomHelper
        public void invalidateViews() {
            super.invalidateViews();
            ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
            for (int i = 0; i < ProfileActivity.this.avatarsViewPager.getChildCount(); i++) {
                ProfileActivity.this.avatarsViewPager.getChildAt(i).invalidate();
            }
            if (ProfileActivity.this.writeButton != null) {
                ProfileActivity.this.writeButton.invalidate();
            }
        }

        @Override // org.telegram.ui.PinchToZoomHelper
        protected void drawOverlays(Canvas canvas, float f, float f2, float f3, float f4, float f5) {
            if (f > 0.0f) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, ProfileActivity.this.avatarsViewPager.getMeasuredWidth(), ProfileActivity.this.avatarsViewPager.getMeasuredHeight() + AndroidUtilities.dp(30.0f));
                canvas.saveLayerAlpha(rectF, (int) (255.0f * f), 31);
                ProfileActivity.this.avatarContainer2.draw(canvas);
                if (((BaseFragment) ProfileActivity.this).actionBar.getOccupyStatusBar() && !SharedConfig.noStatusBar) {
                    if (this.statusBarPaint == null) {
                        Paint paint = new Paint();
                        this.statusBarPaint = paint;
                        paint.setColor(ColorUtils.setAlphaComponent(-16777216, 51));
                    }
                    canvas.drawRect(((BaseFragment) ProfileActivity.this).actionBar.getX(), ((BaseFragment) ProfileActivity.this).actionBar.getY(), ((BaseFragment) ProfileActivity.this).actionBar.getX() + ((BaseFragment) ProfileActivity.this).actionBar.getMeasuredWidth(), ((BaseFragment) ProfileActivity.this).actionBar.getY() + AndroidUtilities.statusBarHeight, this.statusBarPaint);
                }
                canvas.save();
                canvas.translate(((BaseFragment) ProfileActivity.this).actionBar.getX(), ((BaseFragment) ProfileActivity.this).actionBar.getY());
                ((BaseFragment) ProfileActivity.this).actionBar.draw(canvas);
                canvas.restore();
                if (ProfileActivity.this.writeButton != null && ProfileActivity.this.writeButton.getVisibility() == 0 && ProfileActivity.this.writeButton.getAlpha() > 0.0f) {
                    canvas.save();
                    float f6 = (f * 0.5f) + 0.5f;
                    canvas.scale(f6, f6, ProfileActivity.this.writeButton.getX() + (ProfileActivity.this.writeButton.getMeasuredWidth() / 2.0f), ProfileActivity.this.writeButton.getY() + (ProfileActivity.this.writeButton.getMeasuredHeight() / 2.0f));
                    canvas.translate(ProfileActivity.this.writeButton.getX(), ProfileActivity.this.writeButton.getY());
                    ProfileActivity.this.writeButton.draw(canvas);
                    canvas.restore();
                }
                canvas.restore();
            }
        }

        @Override // org.telegram.ui.PinchToZoomHelper
        public boolean zoomEnabled(View view, ImageReceiver imageReceiver) {
            return super.zoomEnabled(view, imageReceiver) && ProfileActivity.this.listView.getScrollState() != 1;
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$25 */
    /* loaded from: classes3.dex */
    class AnonymousClass25 implements PinchToZoomHelper.Callback {
        @Override // org.telegram.ui.PinchToZoomHelper.Callback
        public /* synthetic */ TextureView getCurrentTextureView() {
            return PinchToZoomHelper.Callback.CC.$default$getCurrentTextureView(this);
        }

        @Override // org.telegram.ui.PinchToZoomHelper.Callback
        public /* synthetic */ void onZoomFinished(MessageObject messageObject) {
            PinchToZoomHelper.Callback.CC.$default$onZoomFinished(this, messageObject);
        }

        AnonymousClass25() {
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.PinchToZoomHelper.Callback
        public void onZoomStarted(MessageObject messageObject) {
            ProfileActivity.this.listView.cancelClickRunnables(true);
            if (ProfileActivity.this.sharedMediaLayout != null && ProfileActivity.this.sharedMediaLayout.getCurrentListView() != null) {
                ProfileActivity.this.sharedMediaLayout.getCurrentListView().cancelClickRunnables(true);
            }
            Bitmap bitmap = ProfileActivity.this.pinchToZoomHelper.getPhotoImage() == null ? null : ProfileActivity.this.pinchToZoomHelper.getPhotoImage().getBitmap();
            if (bitmap != null) {
                ProfileActivity.this.topView.setBackgroundColor(ColorUtils.blendARGB(AndroidUtilities.calcBitmapColor(bitmap), ProfileActivity.this.getThemedColor("windowBackgroundWhite"), 0.1f));
            }
        }
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

    private void openAvatar() {
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
            PhotoViewer.getInstance().setParentActivity(getParentActivity());
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2 = user.photo;
            int i = tLRPC$UserProfilePhoto2.dc_id;
            if (i != 0) {
                tLRPC$UserProfilePhoto2.photo_big.dc_id = i;
            }
            PhotoViewer.getInstance().openPhoto(user.photo.photo_big, this.provider);
        } else if (this.chatId == 0 || (tLRPC$ChatPhoto = (chat = getMessagesController().getChat(Long.valueOf(this.chatId))).photo) == null || tLRPC$ChatPhoto.photo_big == null) {
        } else {
            PhotoViewer.getInstance().setParentActivity(getParentActivity());
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
                if (tLRPC$UserProfilePhoto == null || tLRPC$UserProfilePhoto.photo_big == null || (tLRPC$UserProfilePhoto instanceof TLRPC$TL_userProfilePhotoEmpty)) {
                    z = false;
                }
                imageUpdater.openMenu(z, new ProfileActivity$$ExternalSyntheticLambda19(this), new ProfileActivity$$ExternalSyntheticLambda10(this));
                this.cameraDrawable.setCurrentFrame(0);
                this.cameraDrawable.setCustomEndFrame(43);
                this.cellCameraDrawable.setCurrentFrame(0);
                this.cellCameraDrawable.setCustomEndFrame(43);
                this.writeButton.playAnimation();
                TextCell textCell = this.setAvatarCell;
                if (textCell == null) {
                    return;
                }
                textCell.getImageView().playAnimation();
                return;
            }
            if (this.playProfileAnimation != 0) {
                ArrayList<BaseFragment> arrayList = this.parentLayout.fragmentsStack;
                if (arrayList.get(arrayList.size() - 2) instanceof ChatActivity) {
                    finishFragment();
                    return;
                }
            }
            TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user2 == null || (user2 instanceof TLRPC$TL_userEmpty)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", this.userId);
            if (!getMessagesController().checkCanOpenChat(bundle, this)) {
                return;
            }
            boolean z2 = this.arguments.getBoolean("removeFragmentOnChatOpen", true);
            if (!AndroidUtilities.isTablet() && z2) {
                NotificationCenter notificationCenter = getNotificationCenter();
                int i = NotificationCenter.closeChats;
                notificationCenter.removeObserver(this, i);
                getNotificationCenter().postNotificationName(i, new Object[0]);
            }
            int i2 = getArguments().getInt("nearby_distance", -1);
            if (i2 >= 0) {
                bundle.putInt("nearby_distance", i2);
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setPreloadedSticker(getMediaDataController().getGreetingsSticker(), false);
            presentFragment(chatActivity, z2);
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            finishFragment();
            return;
        }
        openDiscussion();
    }

    public /* synthetic */ void lambda$onWriteButtonClick$15() {
        MessagesController.getInstance(this.currentAccount).deleteUserPhoto(null);
        this.cameraDrawable.setCurrentFrame(0);
        this.cellCameraDrawable.setCurrentFrame(0);
    }

    public /* synthetic */ void lambda$onWriteButtonClick$16(DialogInterface dialogInterface) {
        if (!this.imageUpdater.isUploadingImage()) {
            this.cameraDrawable.setCustomEndFrame(86);
            this.cellCameraDrawable.setCustomEndFrame(86);
            this.writeButton.playAnimation();
            TextCell textCell = this.setAvatarCell;
            if (textCell == null) {
                return;
            }
            textCell.getImageView().playAnimation();
            return;
        }
        this.cameraDrawable.setCurrentFrame(0, false);
        this.cellCameraDrawable.setCurrentFrame(0, false);
    }

    public void openDiscussion() {
        TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
        if (tLRPC$ChatFull == null || tLRPC$ChatFull.linked_chat_id == 0) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatInfo.linked_chat_id);
        if (!getMessagesController().checkCanOpenChat(bundle, this)) {
            return;
        }
        presentFragment(new ChatActivity(bundle));
    }

    public boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z) {
        return onMemberClick(tLRPC$ChatParticipant, z, false);
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, boolean z2) {
        boolean z3;
        boolean z4;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant;
        boolean z5;
        boolean z6;
        boolean z7;
        String str;
        int i;
        if (getParentActivity() == null) {
            return false;
        }
        if (z) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant.user_id));
            if (user == null || tLRPC$ChatParticipant.user_id == getUserConfig().getClientUserId()) {
                return false;
            }
            this.selectedUser = tLRPC$ChatParticipant.user_id;
            ArrayList arrayList = null;
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant2 = ((TLRPC$TL_chatChannelParticipant) tLRPC$ChatParticipant).channelParticipant;
                getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant.user_id));
                z5 = ChatObject.canAddAdmins(this.currentChat);
                if (z5 && ((tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator) || ((tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin) && !tLRPC$ChannelParticipant2.can_edit))) {
                    z5 = false;
                }
                boolean z8 = ChatObject.canBlockUsers(this.currentChat) && ((!(tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin) && !(tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) || tLRPC$ChannelParticipant2.can_edit);
                z4 = this.currentChat.gigagroup ? false : z8;
                z3 = tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin;
                boolean z9 = z8;
                tLRPC$ChannelParticipant = tLRPC$ChannelParticipant2;
                z6 = z9;
            } else {
                TLRPC$Chat tLRPC$Chat = this.currentChat;
                boolean z10 = tLRPC$Chat.creator || ((tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipant) && (ChatObject.canBlockUsers(tLRPC$Chat) || tLRPC$ChatParticipant.inviter_id == getUserConfig().getClientUserId()));
                z5 = this.currentChat.creator;
                z3 = tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin;
                z4 = z5;
                z6 = z10;
                tLRPC$ChannelParticipant = null;
            }
            ArrayList arrayList2 = z2 ? null : new ArrayList();
            ArrayList arrayList3 = z2 ? null : new ArrayList();
            if (!z2) {
                arrayList = new ArrayList();
            }
            if (z5) {
                if (z2) {
                    return true;
                }
                if (z3) {
                    i = 2131625557;
                    str = "EditAdminRights";
                } else {
                    i = 2131628238;
                    str = "SetAsAdmin";
                }
                arrayList2.add(LocaleController.getString(str, i));
                arrayList3.add(2131165635);
                arrayList.add(0);
            }
            if (z4) {
                if (z2) {
                    return true;
                }
                arrayList2.add(LocaleController.getString("ChangePermissions", 2131624856));
                arrayList3.add(2131165841);
                arrayList.add(1);
            }
            if (!z6) {
                z7 = false;
            } else if (z2) {
                return true;
            } else {
                arrayList2.add(LocaleController.getString("KickFromGroup", 2131626344));
                arrayList3.add(2131165892);
                arrayList.add(2);
                z7 = true;
            }
            if (z2 || arrayList2.isEmpty()) {
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
            builder.setItems((CharSequence[]) arrayList2.toArray(new CharSequence[0]), AndroidUtilities.toIntArray(arrayList3), new ProfileActivity$$ExternalSyntheticLambda7(this, arrayList, tLRPC$ChatParticipant, tLRPC$ChannelParticipant, user, z3));
            AlertDialog create = builder.create();
            showDialog(create);
            if (z7) {
                create.setItemColor(arrayList2.size() - 1, getThemedColor("dialogTextRed2"), getThemedColor("dialogRedIcon"));
            }
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

    public /* synthetic */ void lambda$onMemberClick$18(ArrayList arrayList, TLRPC$ChatParticipant tLRPC$ChatParticipant, TLRPC$ChannelParticipant tLRPC$ChannelParticipant, TLRPC$User tLRPC$User, boolean z, DialogInterface dialogInterface, int i) {
        if (((Integer) arrayList.get(i)).intValue() == 2) {
            kickUser(this.selectedUser, tLRPC$ChatParticipant);
            return;
        }
        int intValue = ((Integer) arrayList.get(i)).intValue();
        if (intValue != 1 || (!(tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) && !(tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin))) {
            if (tLRPC$ChannelParticipant != null) {
                openRightsEdit(intValue, tLRPC$User, tLRPC$ChatParticipant, tLRPC$ChannelParticipant.admin_rights, tLRPC$ChannelParticipant.banned_rights, tLRPC$ChannelParticipant.rank, z);
                return;
            } else {
                openRightsEdit(intValue, tLRPC$User, tLRPC$ChatParticipant, null, null, "", z);
                return;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString("AppName", 2131624375));
        builder.setMessage(LocaleController.formatString("AdminWillBeRemoved", 2131624306, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name)));
        builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new ProfileActivity$$ExternalSyntheticLambda9(this, tLRPC$ChannelParticipant, intValue, tLRPC$User, tLRPC$ChatParticipant, z));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$onMemberClick$17(TLRPC$ChannelParticipant tLRPC$ChannelParticipant, int i, TLRPC$User tLRPC$User, TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, DialogInterface dialogInterface, int i2) {
        if (tLRPC$ChannelParticipant != null) {
            openRightsEdit(i, tLRPC$User, tLRPC$ChatParticipant, tLRPC$ChannelParticipant.admin_rights, tLRPC$ChannelParticipant.banned_rights, tLRPC$ChannelParticipant.rank, z);
        } else {
            openRightsEdit(i, tLRPC$User, tLRPC$ChatParticipant, null, null, "", z);
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$26 */
    /* loaded from: classes3.dex */
    public class AnonymousClass26 extends ChatRightsEditActivity {
        final /* synthetic */ boolean[] val$needShowBulletin;
        final /* synthetic */ TLRPC$User val$user;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass26(long j, long j2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str, int i, boolean z, boolean z2, String str2, boolean[] zArr, TLRPC$User tLRPC$User) {
            super(j, j2, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, tLRPC$TL_chatBannedRights2, str, i, z, z2, str2);
            ProfileActivity.this = r15;
            this.val$needShowBulletin = zArr;
            this.val$user = tLRPC$User;
        }

        @Override // org.telegram.ui.ActionBar.BaseFragment
        public void onTransitionAnimationEnd(boolean z, boolean z2) {
            if (z || !z2 || !this.val$needShowBulletin[0] || !BulletinFactory.canShowBulletin(ProfileActivity.this)) {
                return;
            }
            BulletinFactory.createPromoteToAdminBulletin(ProfileActivity.this, this.val$user.first_name).show();
        }
    }

    private void openRightsEdit(int i, TLRPC$User tLRPC$User, TLRPC$ChatParticipant tLRPC$ChatParticipant, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z) {
        boolean[] zArr = new boolean[1];
        AnonymousClass26 anonymousClass26 = new AnonymousClass26(tLRPC$User.id, this.chatId, tLRPC$TL_chatAdminRights, this.currentChat.default_banned_rights, tLRPC$TL_chatBannedRights, str, i, true, false, null, zArr, tLRPC$User);
        anonymousClass26.setDelegate(new AnonymousClass27(i, tLRPC$ChatParticipant, z, zArr));
        presentFragment(anonymousClass26);
    }

    /* renamed from: org.telegram.ui.ProfileActivity$27 */
    /* loaded from: classes3.dex */
    public class AnonymousClass27 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ int val$action;
        final /* synthetic */ boolean val$editingAdmin;
        final /* synthetic */ boolean[] val$needShowBulletin;
        final /* synthetic */ TLRPC$ChatParticipant val$participant;

        AnonymousClass27(int i, TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z, boolean[] zArr) {
            ProfileActivity.this = r1;
            this.val$action = i;
            this.val$participant = tLRPC$ChatParticipant;
            this.val$editingAdmin = z;
            this.val$needShowBulletin = zArr;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            boolean z;
            TLRPC$ChatParticipant tLRPC$ChatParticipant;
            int i2 = this.val$action;
            int i3 = 0;
            if (i2 != 0) {
                if (i2 != 1 || i != 0 || !ProfileActivity.this.currentChat.megagroup || ProfileActivity.this.chatInfo == null || ProfileActivity.this.chatInfo.participants == null) {
                    return;
                }
                int i4 = 0;
                while (true) {
                    if (i4 >= ProfileActivity.this.chatInfo.participants.participants.size()) {
                        z = false;
                        break;
                    } else if (MessageObject.getPeerId(((TLRPC$TL_chatChannelParticipant) ProfileActivity.this.chatInfo.participants.participants.get(i4)).channelParticipant.peer) == this.val$participant.user_id) {
                        ProfileActivity.this.chatInfo.participants_count--;
                        ProfileActivity.this.chatInfo.participants.participants.remove(i4);
                        z = true;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (ProfileActivity.this.chatInfo != null && ProfileActivity.this.chatInfo.participants != null) {
                    while (true) {
                        if (i3 >= ProfileActivity.this.chatInfo.participants.participants.size()) {
                            break;
                        } else if (ProfileActivity.this.chatInfo.participants.participants.get(i3).user_id == this.val$participant.user_id) {
                            ProfileActivity.this.chatInfo.participants.participants.remove(i3);
                            z = true;
                            break;
                        } else {
                            i3++;
                        }
                    }
                }
                if (!z) {
                    return;
                }
                ProfileActivity.this.updateOnlineCount(true);
                ProfileActivity.this.updateRowsIds();
                ProfileActivity.this.listAdapter.notifyDataSetChanged();
                return;
            }
            TLRPC$ChatParticipant tLRPC$ChatParticipant2 = this.val$participant;
            if (tLRPC$ChatParticipant2 instanceof TLRPC$TL_chatChannelParticipant) {
                TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant = (TLRPC$TL_chatChannelParticipant) tLRPC$ChatParticipant2;
                if (i == 1) {
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
                TLRPC$ChatParticipant tLRPC$ChatParticipant3 = this.val$participant;
                tLRPC$Peer.user_id = tLRPC$ChatParticipant3.user_id;
                tLRPC$ChannelParticipant.date = tLRPC$ChatParticipant3.date;
                tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights;
                tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights;
                tLRPC$ChannelParticipant.rank = str;
            } else if (tLRPC$ChatParticipant2 != null) {
                if (i == 1) {
                    tLRPC$ChatParticipant = new TLRPC$TL_chatParticipantAdmin();
                } else {
                    tLRPC$ChatParticipant = new TLRPC$TL_chatParticipant();
                }
                TLRPC$ChatParticipant tLRPC$ChatParticipant4 = this.val$participant;
                tLRPC$ChatParticipant.user_id = tLRPC$ChatParticipant4.user_id;
                tLRPC$ChatParticipant.date = tLRPC$ChatParticipant4.date;
                tLRPC$ChatParticipant.inviter_id = tLRPC$ChatParticipant4.inviter_id;
                int indexOf = ProfileActivity.this.chatInfo.participants.participants.indexOf(this.val$participant);
                if (indexOf >= 0) {
                    ProfileActivity.this.chatInfo.participants.participants.set(indexOf, tLRPC$ChatParticipant);
                }
            }
            if (i != 1 || this.val$editingAdmin) {
                return;
            }
            this.val$needShowBulletin[0] = true;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ProfileActivity.this.undoView.showWithAction(-ProfileActivity.this.chatId, ProfileActivity.this.currentChat.megagroup ? 10 : 9, tLRPC$User);
        }
    }

    public boolean processOnClickOrPress(int i, View view) {
        String str;
        String str2;
        TLRPC$Chat chat;
        String str3;
        TLRPC$UserFull tLRPC$UserFull;
        if (i == this.usernameRow || i == this.setUsernameRow) {
            if (this.userId != 0) {
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
                if (user == null || (str = user.username) == null) {
                    return false;
                }
            } else if (this.chatId == 0 || (chat = getMessagesController().getChat(Long.valueOf(this.chatId))) == null || (str = chat.username) == null) {
                return false;
            }
            try {
                ClipboardManager clipboardManager = (ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard");
                if (this.userId != 0) {
                    str2 = "@" + str;
                    BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("UsernameCopied", 2131628846), this.resourcesProvider).show();
                } else {
                    str2 = "https://" + MessagesController.getInstance(UserConfig.selectedAccount).linkPrefix + "/" + str;
                    BulletinFactory.of(this).createCopyLinkBulletin(LocaleController.getString("LinkCopied", 2131626433), this.resourcesProvider).show();
                }
                clipboardManager.setPrimaryClip(ClipData.newPlainText("label", str2));
            } catch (Exception e) {
                FileLog.e(e);
            }
            return true;
        } else if (i == this.phoneRow || i == this.numberRow) {
            TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user2 == null || (str3 = user2.phone) == null || str3.length() == 0 || getParentActivity() == null) {
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (i == this.phoneRow) {
                TLRPC$UserFull tLRPC$UserFull2 = this.userInfo;
                if (tLRPC$UserFull2 != null && tLRPC$UserFull2.phone_calls_available) {
                    arrayList.add(LocaleController.getString("CallViaTelegram", 2131624804));
                    arrayList2.add(2);
                    if (Build.VERSION.SDK_INT >= 18 && this.userInfo.video_calls_available) {
                        arrayList.add(LocaleController.getString("VideoCallViaTelegram", 2131628884));
                        arrayList2.add(3);
                    }
                }
                arrayList.add(LocaleController.getString("Call", 2131624779));
                arrayList2.add(0);
            }
            arrayList.add(LocaleController.getString("Copy", 2131625256));
            arrayList2.add(1);
            builder.setItems((CharSequence[]) arrayList.toArray(new CharSequence[0]), new ProfileActivity$$ExternalSyntheticLambda8(this, arrayList2, user2));
            showDialog(builder.create());
            return true;
        } else if (i != this.channelInfoRow && i != this.userInfoRow && i != this.locationRow && i != this.bioRow) {
            return false;
        } else {
            if (i == this.bioRow && ((tLRPC$UserFull = this.userInfo) == null || TextUtils.isEmpty(tLRPC$UserFull.about))) {
                return false;
            }
            boolean z = view instanceof AboutLinkCell;
            if (z && ((AboutLinkCell) view).onClick()) {
                return false;
            }
            String str4 = null;
            if (i == this.locationRow) {
                TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
                if (tLRPC$ChatFull != null) {
                    TLRPC$ChannelLocation tLRPC$ChannelLocation = tLRPC$ChatFull.location;
                    if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocation) {
                        str4 = ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address;
                    }
                }
            } else if (i == this.channelInfoRow) {
                TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
                if (tLRPC$ChatFull2 != null) {
                    str4 = tLRPC$ChatFull2.about;
                }
            } else {
                TLRPC$UserFull tLRPC$UserFull3 = this.userInfo;
                if (tLRPC$UserFull3 != null) {
                    str4 = tLRPC$UserFull3.about;
                }
            }
            String str5 = str4;
            if (TextUtils.isEmpty(str5)) {
                return false;
            }
            String[] strArr = {"und"};
            boolean z2 = MessagesController.getGlobalMainSettings().getBoolean("translate_button", false);
            boolean[] zArr = new boolean[1];
            zArr[0] = i == this.bioRow || i == this.channelInfoRow || i == this.userInfoRow;
            String language = LocaleController.getInstance().getCurrentLocale().getLanguage();
            ProfileActivity$$ExternalSyntheticLambda28 profileActivity$$ExternalSyntheticLambda28 = new ProfileActivity$$ExternalSyntheticLambda28(this, zArr, str5, i, strArr, language);
            if (zArr[0]) {
                if (LanguageDetector.hasSupport()) {
                    LanguageDetector.detectLanguage(str5, new ProfileActivity$$ExternalSyntheticLambda31(this, strArr, zArr, language, z2, profileActivity$$ExternalSyntheticLambda28), new ProfileActivity$$ExternalSyntheticLambda30(profileActivity$$ExternalSyntheticLambda28));
                } else {
                    profileActivity$$ExternalSyntheticLambda28.run();
                }
            } else {
                profileActivity$$ExternalSyntheticLambda28.run();
            }
            return !z;
        }
    }

    public /* synthetic */ void lambda$processOnClickOrPress$19(ArrayList arrayList, TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
        int intValue = ((Integer) arrayList.get(i)).intValue();
        if (intValue == 0) {
            try {
                Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:+" + tLRPC$User.phone));
                intent.addFlags(268435456);
                getParentActivity().startActivityForResult(intent, 500);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (intValue != 1) {
            if (intValue != 2 && intValue != 3) {
                return;
            }
            boolean z = intValue == 3;
            TLRPC$UserFull tLRPC$UserFull = this.userInfo;
            VoIPHelper.startCall(tLRPC$User, z, tLRPC$UserFull != null && tLRPC$UserFull.video_calls_available, getParentActivity(), this.userInfo, getAccountInstance());
        } else {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", "+" + tLRPC$User.phone));
                if (Build.VERSION.SDK_INT >= 31) {
                    return;
                }
                BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("PhoneCopied", 2131627488)).show();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
    }

    public /* synthetic */ void lambda$processOnClickOrPress$22(boolean[] zArr, String str, int i, String[] strArr, String str2) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
        builder.setItems(zArr[0] ? new CharSequence[]{LocaleController.getString("Copy", 2131625256), LocaleController.getString("TranslateMessage", 2131628691)} : new CharSequence[]{LocaleController.getString("Copy", 2131625256)}, new ProfileActivity$$ExternalSyntheticLambda6(this, str, i, strArr, str2));
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$processOnClickOrPress$21(String str, int i, String[] strArr, String str2, DialogInterface dialogInterface, int i2) {
        try {
            if (i2 == 0) {
                AndroidUtilities.addToClipboard(str);
                if (i == this.bioRow) {
                    BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("BioCopied", 2131624672)).show();
                } else {
                    BulletinFactory.of(this).createCopyBulletin(LocaleController.getString("TextCopied", 2131628595)).show();
                }
            } else if (i2 != 1) {
            } else {
                TranslateAlert.showAlert(this.fragmentView.getContext(), this, strArr[0], str2, str, false, new ProfileActivity$$ExternalSyntheticLambda40(this), null);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ boolean lambda$processOnClickOrPress$20(URLSpan uRLSpan) {
        if (uRLSpan != null) {
            openUrl(uRLSpan.getURL());
            return true;
        }
        return false;
    }

    public /* synthetic */ void lambda$processOnClickOrPress$23(String[] strArr, boolean[] zArr, String str, boolean z, Runnable runnable, String str2) {
        TLRPC$Chat tLRPC$Chat;
        strArr[0] = str2;
        zArr[0] = str2 != null && (!str2.equals(str) || str2.equals("und")) && ((z && !RestrictedLanguagesSelectActivity.getRestrictedLanguages().contains(str2)) || ((tLRPC$Chat = this.currentChat) != null && ((tLRPC$Chat.has_link || tLRPC$Chat.username != null) && ("uk".equals(str2) || "ru".equals(str2)))));
        runnable.run();
    }

    public static /* synthetic */ void lambda$processOnClickOrPress$24(Runnable runnable, Exception exc) {
        FileLog.e("mlkit: failed to detect language in selection", exc);
        runnable.run();
    }

    public void leaveChatPressed() {
        AlertsCreator.createClearOrDeleteDialogAlert(this, false, this.currentChat, null, false, false, true, new ProfileActivity$$ExternalSyntheticLambda32(this), this.resourcesProvider);
    }

    public /* synthetic */ void lambda$leaveChatPressed$25(boolean z) {
        this.playProfileAnimation = 0;
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.closeChats;
        notificationCenter.removeObserver(this, i);
        getNotificationCenter().postNotificationName(i, new Object[0]);
        finishFragment();
        getNotificationCenter().postNotificationName(NotificationCenter.needDeleteDialog, Long.valueOf(-this.currentChat.id), null, this.currentChat, Boolean.valueOf(z));
    }

    public void getChannelParticipants(boolean z) {
        LongSparseArray<TLRPC$ChatParticipant> longSparseArray;
        if (this.loadingUsers || (longSparseArray = this.participantsMap) == null || this.chatInfo == null) {
            return;
        }
        this.loadingUsers = true;
        int i = 0;
        int i2 = (longSparseArray.size() == 0 || !z) ? 0 : 300;
        TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants = new TLRPC$TL_channels_getParticipants();
        tLRPC$TL_channels_getParticipants.channel = getMessagesController().getInputChannel(this.chatId);
        tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsRecent();
        if (!z) {
            i = this.participantsMap.size();
        }
        tLRPC$TL_channels_getParticipants.offset = i;
        tLRPC$TL_channels_getParticipants.limit = 200;
        getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_channels_getParticipants, new ProfileActivity$$ExternalSyntheticLambda35(this, tLRPC$TL_channels_getParticipants, i2)), this.classGuid);
    }

    public /* synthetic */ void lambda$getChannelParticipants$27(TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants, int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda24(this, tLRPC$TL_error, tLObject, tLRPC$TL_channels_getParticipants), i);
    }

    public /* synthetic */ void lambda$getChannelParticipants$26(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants) {
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
        updateListAnimated(true);
    }

    /* renamed from: org.telegram.ui.ProfileActivity$28 */
    /* loaded from: classes3.dex */
    public class AnonymousClass28 extends AnimationProperties.FloatProperty<ActionBar> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass28(String str) {
            super(str);
            ProfileActivity.this = r1;
        }

        public void setValue(ActionBar actionBar, float f) {
            ProfileActivity.this.mediaHeaderAnimationProgress = f;
            ProfileActivity.this.topView.invalidate();
            int themedColor = ProfileActivity.this.getThemedColor("profile_title");
            int themedColor2 = ProfileActivity.this.getThemedColor("player_actionBarTitle");
            int offsetColor = AndroidUtilities.getOffsetColor(themedColor, themedColor2, f, 1.0f);
            ProfileActivity.this.nameTextView[1].setTextColor(offsetColor);
            if (ProfileActivity.this.lockIconDrawable != null) {
                ProfileActivity.this.lockIconDrawable.setColorFilter(offsetColor, PorterDuff.Mode.MULTIPLY);
            }
            if (ProfileActivity.this.scamDrawable != null) {
                ProfileActivity.this.scamDrawable.setColor(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor("avatar_subtitleInProfileBlue"), themedColor2, f, 1.0f));
            }
            ((BaseFragment) ProfileActivity.this).actionBar.setItemsColor(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor("actionBarDefaultIcon"), ProfileActivity.this.getThemedColor("windowBackgroundWhiteGrayText2"), f, 1.0f), false);
            ((BaseFragment) ProfileActivity.this).actionBar.setItemsBackgroundColor(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor("avatar_actionBarSelectorBlue"), ProfileActivity.this.getThemedColor("actionBarActionModeDefaultSelector"), f, 1.0f), false);
            ProfileActivity.this.topView.invalidate();
            ProfileActivity.this.otherItem.setIconColor(ProfileActivity.this.getThemedColor("actionBarDefaultIcon"));
            ProfileActivity.this.callItem.setIconColor(ProfileActivity.this.getThemedColor("actionBarDefaultIcon"));
            ProfileActivity.this.videoCallItem.setIconColor(ProfileActivity.this.getThemedColor("actionBarDefaultIcon"));
            ProfileActivity.this.editItem.setIconColor(ProfileActivity.this.getThemedColor("actionBarDefaultIcon"));
            if (ProfileActivity.this.verifiedDrawable != null) {
                ProfileActivity.this.verifiedDrawable.setColorFilter(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor("profile_verifiedBackground"), ProfileActivity.this.getThemedColor("player_actionBarTitle"), f, 1.0f), PorterDuff.Mode.MULTIPLY);
            }
            if (ProfileActivity.this.verifiedCheckDrawable != null) {
                ProfileActivity.this.verifiedCheckDrawable.setColorFilter(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor("profile_verifiedCheck"), ProfileActivity.this.getThemedColor("windowBackgroundWhite"), f, 1.0f), PorterDuff.Mode.MULTIPLY);
            }
            if (ProfileActivity.this.premiumStarDrawable != null) {
                ProfileActivity.this.premiumStarDrawable.setColorFilter(AndroidUtilities.getOffsetColor(ProfileActivity.this.getThemedColor("profile_verifiedBackground"), ProfileActivity.this.getThemedColor("player_actionBarTitle"), f, 1.0f), PorterDuff.Mode.MULTIPLY);
            }
            if (ProfileActivity.this.avatarsViewPagerIndicatorView.getSecondaryMenuItem() != null) {
                if (!ProfileActivity.this.videoCallItemVisible && !ProfileActivity.this.editItemVisible && !ProfileActivity.this.callItemVisible) {
                    return;
                }
                ProfileActivity profileActivity = ProfileActivity.this;
                profileActivity.needLayoutText(Math.min(1.0f, profileActivity.extraHeight / AndroidUtilities.dp(88.0f)));
            }
        }

        public Float get(ActionBar actionBar) {
            return Float.valueOf(ProfileActivity.this.mediaHeaderAnimationProgress);
        }
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
        ActionBarMenuItem searchItem = this.sharedMediaLayout.getSearchItem();
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
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.createMenu().requestLayout();
        }
        ArrayList arrayList = new ArrayList();
        ActionBarMenuItem actionBarMenuItem = this.callItem;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        float f = 1.0f;
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
        if (!z) {
            f = 0.0f;
        }
        fArr15[0] = f;
        arrayList.add(ObjectAnimator.ofFloat(clippingTextViewSwitcher, property15, fArr15));
        if (z) {
            arrayList.add(ObjectAnimator.ofFloat(this, this.HEADER_SHADOW, 0.0f));
        }
        AnimatorSet animatorSet3 = new AnimatorSet();
        this.headerAnimatorSet = animatorSet3;
        animatorSet3.playTogether(arrayList);
        this.headerAnimatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.headerAnimatorSet.addListener(new AnonymousClass29(searchItem));
        this.headerAnimatorSet.setDuration(150L);
        this.headerAnimatorSet.start();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
    }

    /* renamed from: org.telegram.ui.ProfileActivity$29 */
    /* loaded from: classes3.dex */
    public class AnonymousClass29 extends AnimatorListenerAdapter {
        final /* synthetic */ ActionBarMenuItem val$mediaSearchItem;

        AnonymousClass29(ActionBarMenuItem actionBarMenuItem) {
            ProfileActivity.this = r1;
            this.val$mediaSearchItem = actionBarMenuItem;
        }

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
                        this.val$mediaSearchItem.setVisibility(0);
                    }
                    ProfileActivity.this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(4);
                    ProfileActivity.this.headerShadowAnimatorSet = new AnimatorSet();
                    AnimatorSet animatorSet = ProfileActivity.this.headerShadowAnimatorSet;
                    ProfileActivity profileActivity = ProfileActivity.this;
                    animatorSet.playTogether(ObjectAnimator.ofFloat(profileActivity, profileActivity.HEADER_SHADOW, 1.0f));
                    ProfileActivity.this.headerShadowAnimatorSet.setDuration(100L);
                    ProfileActivity.this.headerShadowAnimatorSet.addListener(new AnonymousClass1());
                    ProfileActivity.this.headerShadowAnimatorSet.start();
                }
            }
            ProfileActivity.this.headerAnimatorSet = null;
        }

        /* renamed from: org.telegram.ui.ProfileActivity$29$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                AnonymousClass29.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ProfileActivity.this.headerShadowAnimatorSet = null;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ProfileActivity.this.headerAnimatorSet = null;
        }
    }

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
        groupCreateActivity.setDelegate(new ProfileActivity$$ExternalSyntheticLambda41(this));
        presentFragment(groupCreateActivity);
    }

    public /* synthetic */ void lambda$openAddMember$28(ArrayList arrayList, int i) {
        HashSet hashSet = new HashSet();
        if (this.chatInfo.participants.participants != null) {
            for (int i2 = 0; i2 < this.chatInfo.participants.participants.size(); i2++) {
                hashSet.add(Long.valueOf(this.chatInfo.participants.participants.get(i2).user_id));
            }
        }
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            TLRPC$User tLRPC$User = (TLRPC$User) arrayList.get(i3);
            getMessagesController().addUserToChat(this.chatId, tLRPC$User, i, null, this, null);
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

    public void checkListViewScroll() {
        RecyclerListView.Holder holder;
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
        boolean z = false;
        int i = 0;
        while (true) {
            holder = null;
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
        if (view != null) {
            holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(view);
        }
        int top = view == null ? 0 : view.getTop();
        int adapterPosition = holder != null ? holder.getAdapterPosition() : -1;
        if (top < 0 || adapterPosition != 0) {
            top = 0;
        }
        boolean z2 = this.imageUpdater == null && this.actionBar.isSearchFieldVisible();
        int i2 = this.sharedMediaRow;
        if (i2 != -1 && !z2) {
            RecyclerListView.Holder holder2 = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(i2);
            z2 = holder2 != null && holder2.itemView.getTop() <= 0;
        }
        setMediaHeaderVisible(z2);
        float f = top;
        if (this.extraHeight == f) {
            return;
        }
        this.extraHeight = f;
        this.topView.invalidate();
        if (this.playProfileAnimation != 0) {
            if (this.extraHeight != 0.0f) {
                z = true;
            }
            this.allowProfileAnimation = z;
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
            if (lastMediaCount[7] == 0 && lastMediaCount[6] == 0) {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Media", lastMediaCount[0], new Object[0]));
            } else if (this.sharedMediaLayout.getPhotosVideosTypeFilter() == 1 || lastMediaCount[7] == 0) {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Photos", lastMediaCount[6], new Object[0]));
            } else if (this.sharedMediaLayout.getPhotosVideosTypeFilter() == 2 || lastMediaCount[6] == 0) {
                this.mediaCounterTextView.setText(LocaleController.formatPluralString("Videos", lastMediaCount[7], new Object[0]));
            } else {
                this.mediaCounterTextView.setText(String.format("%s, %s", LocaleController.formatPluralString("Photos", lastMediaCount[6], new Object[0]), LocaleController.formatPluralString("Videos", lastMediaCount[7], new Object[0])));
            }
        } else if (closestTab == 1) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("Files", lastMediaCount[1], new Object[0]));
        } else if (closestTab == 2) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("Voice", lastMediaCount[2], new Object[0]));
        } else if (closestTab == 3) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("Links", lastMediaCount[3], new Object[0]));
        } else if (closestTab == 4) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("MusicFiles", lastMediaCount[4], new Object[0]));
        } else if (closestTab == 5) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("GIFs", lastMediaCount[5], new Object[0]));
        } else if (closestTab == 6) {
            this.mediaCounterTextView.setText(LocaleController.formatPluralString("CommonGroups", this.userInfo.common_chats_count, new Object[0]));
        } else if (closestTab != 7) {
        } else {
            this.mediaCounterTextView.setText(this.onlineTextView[1].getText());
        }
    }

    public void needLayout(boolean z) {
        OverlaysView overlaysView;
        ValueAnimator valueAnimator;
        SimpleTextView[] simpleTextViewArr;
        BackupImageView currentItemView;
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
                if (!this.openAnimationInProgress) {
                    float f = 0.2f;
                    boolean z2 = min > 0.2f && !this.searchMode && (this.imageUpdater == null || this.setAvatarRow == -1);
                    if (z2 && this.chatId != 0) {
                        z2 = (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup || (tLRPC$ChatFull = this.chatInfo) == null || tLRPC$ChatFull.linked_chat_id == 0 || this.infoHeaderRow == -1) ? false : true;
                    }
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
                            this.writeButtonAnimation.addListener(new AnonymousClass30());
                            this.writeButtonAnimation.start();
                        } else {
                            this.writeButton.setScaleX(z2 ? 1.0f : 0.2f);
                            RLottieImageView rLottieImageView2 = this.writeButton;
                            if (z2) {
                                f = 1.0f;
                            }
                            rLottieImageView2.setScaleY(f);
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
            }
            this.avatarX = (-AndroidUtilities.dpf2(47.0f)) * min;
            float f2 = AndroidUtilities.density;
            this.avatarY = (((this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ((ActionBar.getCurrentActionBarHeight() / 2.0f) * (min + 1.0f))) - (f2 * 21.0f)) + (f2 * 27.0f * min) + this.actionBar.getTranslationY();
            float f3 = this.openAnimationInProgress ? this.initialAnimationExtraHeight : this.extraHeight;
            if (f3 > AndroidUtilities.dp(88.0f) || this.isPulledDown) {
                float max = Math.max(0.0f, Math.min(1.0f, (f3 - AndroidUtilities.dp(88.0f)) / ((this.listView.getMeasuredWidth() - currentActionBarHeight) - AndroidUtilities.dp(88.0f))));
                this.expandProgress = max;
                this.avatarScale = AndroidUtilities.lerp(1.4285715f, 2.4285715f, Math.min(1.0f, max * 3.0f));
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
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
                        this.overlaysView.setOverlaysVisible(true, min2);
                        this.avatarsViewPagerIndicatorView.refreshVisibility(min2);
                        this.avatarsViewPager.setCreateThumbFromParent(true);
                        this.avatarsViewPager.getAdapter().notifyDataSetChanged();
                        this.expandAnimator.cancel();
                        float lerp = AndroidUtilities.lerp(this.expandAnimatorValues, this.currentExpanAnimatorFracture);
                        float[] fArr = this.expandAnimatorValues;
                        fArr[0] = lerp;
                        fArr[1] = 1.0f;
                        this.expandAnimator.setDuration(((1.0f - lerp) * 250.0f) / min2);
                        this.expandAnimator.addListener(new AnonymousClass31());
                        this.expandAnimator.start();
                    }
                    ViewGroup.LayoutParams layoutParams2 = this.avatarsViewPager.getLayoutParams();
                    layoutParams2.width = this.listView.getMeasuredWidth();
                    float f4 = f3 + currentActionBarHeight;
                    layoutParams2.height = (int) f4;
                    this.avatarsViewPager.requestLayout();
                    if (!this.expandAnimator.isRunning()) {
                        float dp2 = (!this.openAnimationInProgress || this.playProfileAnimation != 2) ? 0.0f : (-(1.0f - this.animationProgress)) * AndroidUtilities.dp(50.0f);
                        this.nameTextView[1].setTranslationX(AndroidUtilities.dpf2(16.0f) - this.nameTextView[1].getLeft());
                        this.nameTextView[1].setTranslationY(((f4 - AndroidUtilities.dpf2(38.0f)) - this.nameTextView[1].getBottom()) + dp2);
                        this.onlineTextView[1].setTranslationX(AndroidUtilities.dpf2(16.0f) - this.onlineTextView[1].getLeft());
                        this.onlineTextView[1].setTranslationY(((f4 - AndroidUtilities.dpf2(18.0f)) - this.onlineTextView[1].getBottom()) + dp2);
                        this.mediaCounterTextView.setTranslationX(this.onlineTextView[1].getTranslationX());
                        this.mediaCounterTextView.setTranslationY(this.onlineTextView[1].getTranslationY());
                    }
                } else {
                    if (this.isPulledDown) {
                        this.isPulledDown = false;
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
                        ActionBarMenuItem actionBarMenuItem2 = this.otherItem;
                        if (actionBarMenuItem2 != null) {
                            actionBarMenuItem2.hideSubItem(21);
                            if (this.imageUpdater != null) {
                                this.otherItem.hideSubItem(33);
                                this.otherItem.hideSubItem(34);
                                this.otherItem.hideSubItem(35);
                                this.otherItem.showSubItem(36);
                                this.otherItem.showSubItem(31);
                                this.otherItem.showSubItem(30);
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
                        if (!this.isInLandscapeMode) {
                            this.expandAnimator.setDuration((lerp2 * 250.0f) / min2);
                        } else {
                            this.expandAnimator.setDuration(0L);
                        }
                        this.topView.setBackgroundColor(getThemedColor("avatar_backgroundActionBarBlue"));
                        if (!this.doNotSetForeground && (currentItemView = this.avatarsViewPager.getCurrentItemView()) != null) {
                            this.avatarImage.setForegroundImageDrawable(currentItemView.getImageReceiver().getDrawableSafe());
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
                        this.onlineTextView[1].setTranslationX(this.onlineX);
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
                this.avatarScale = AndroidUtilities.lerp(1.0f, 2.4285715f, this.animationProgress);
                this.avatarImage.setRoundRadius((int) AndroidUtilities.lerp(AndroidUtilities.dpf2(21.0f), 0.0f, this.animationProgress));
                this.avatarContainer.setTranslationX(AndroidUtilities.lerp(0.0f, 0.0f, this.animationProgress));
                this.avatarContainer.setTranslationY(AndroidUtilities.lerp((float) Math.ceil(d), 0.0f, this.animationProgress));
                float measuredWidth = (this.avatarContainer.getMeasuredWidth() - AndroidUtilities.dp(42.0f)) * this.avatarScale;
                this.timeItem.setTranslationX(this.avatarContainer.getX() + AndroidUtilities.dp(16.0f) + measuredWidth);
                this.timeItem.setTranslationY(this.avatarContainer.getY() + AndroidUtilities.dp(15.0f) + measuredWidth);
                this.avatarContainer.setScaleX(this.avatarScale);
                this.avatarContainer.setScaleY(this.avatarScale);
                this.overlaysView.setAlphaValue(this.animationProgress, false);
                this.actionBar.setItemsColor(ColorUtils.blendARGB(getThemedColor("actionBarDefaultIcon"), -1, this.animationProgress), false);
                ScamDrawable scamDrawable = this.scamDrawable;
                if (scamDrawable != null) {
                    scamDrawable.setColor(ColorUtils.blendARGB(getThemedColor("avatar_subtitleInProfileBlue"), Color.argb(179, 255, 255, 255), this.animationProgress));
                }
                Drawable drawable = this.lockIconDrawable;
                if (drawable != null) {
                    drawable.setColorFilter(ColorUtils.blendARGB(getThemedColor("chat_lockIcon"), -1, this.animationProgress), PorterDuff.Mode.MULTIPLY);
                }
                CrossfadeDrawable crossfadeDrawable = this.verifiedCrossfadeDrawable;
                if (crossfadeDrawable != null) {
                    crossfadeDrawable.setProgress(this.animationProgress);
                    this.nameTextView[1].invalidate();
                }
                CrossfadeDrawable crossfadeDrawable2 = this.premuimCrossfadeDrawable;
                if (crossfadeDrawable2 != null) {
                    crossfadeDrawable2.setProgress(this.animationProgress);
                    this.nameTextView[1].invalidate();
                }
                FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.avatarContainer.getLayoutParams();
                int lerp3 = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(42.0f), (this.extraHeight + currentActionBarHeight) / this.avatarScale, this.animationProgress);
                layoutParams3.height = lerp3;
                layoutParams3.width = lerp3;
                layoutParams3.leftMargin = (int) AndroidUtilities.lerp(AndroidUtilities.dpf2(64.0f), 0.0f, this.animationProgress);
                this.avatarContainer.requestLayout();
            } else if (this.extraHeight <= AndroidUtilities.dp(88.0f)) {
                this.avatarScale = ((min * 18.0f) + 42.0f) / 42.0f;
                float f5 = (0.12f * min) + 1.0f;
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
                this.nameY = ((float) Math.floor(this.avatarY)) + AndroidUtilities.dp(1.3f) + (AndroidUtilities.dp(7.0f) * min);
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
                            this.onlineTextView[i].setTranslationX(this.onlineX);
                            this.onlineTextView[i].setTranslationY(this.onlineY);
                            if (i == 1) {
                                this.mediaCounterTextView.setTranslationX(this.onlineX);
                                this.mediaCounterTextView.setTranslationY(this.onlineY);
                            }
                        }
                        this.nameTextView[i].setScaleX(f5);
                        this.nameTextView[i].setScaleY(f5);
                    }
                    i++;
                }
            }
            if (!this.openAnimationInProgress && ((valueAnimator = this.expandAnimator) == null || !valueAnimator.isRunning())) {
                needLayoutText(min);
            }
        }
        if (this.isPulledDown || !((overlaysView = this.overlaysView) == null || overlaysView.animator == null || !this.overlaysView.animator.isRunning())) {
            ViewGroup.LayoutParams layoutParams4 = this.overlaysView.getLayoutParams();
            layoutParams4.width = this.listView.getMeasuredWidth();
            layoutParams4.height = (int) (this.extraHeight + currentActionBarHeight);
            this.overlaysView.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$30 */
    /* loaded from: classes3.dex */
    public class AnonymousClass30 extends AnimatorListenerAdapter {
        AnonymousClass30() {
            ProfileActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ProfileActivity.this.writeButtonAnimation == null || !ProfileActivity.this.writeButtonAnimation.equals(animator)) {
                return;
            }
            ProfileActivity.this.writeButtonAnimation = null;
        }
    }

    /* renamed from: org.telegram.ui.ProfileActivity$31 */
    /* loaded from: classes3.dex */
    public class AnonymousClass31 extends AnimatorListenerAdapter {
        AnonymousClass31() {
            ProfileActivity.this = r1;
        }

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
    }

    public void updateQrItemVisibility(boolean z) {
        if (this.qrItem == null) {
            return;
        }
        float f = 1.0f;
        int i = 0;
        boolean z2 = isQrNeedVisible() && Math.min(1.0f, this.extraHeight / ((float) AndroidUtilities.dp(88.0f))) > 0.5f && this.searchTransitionProgress > 0.5f;
        if (z) {
            if (z2 == this.isQrItemVisible) {
                return;
            }
            this.isQrItemVisible = z2;
            AnimatorSet animatorSet = this.qrItemAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.qrItemAnimation = null;
            }
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
            this.qrItemAnimation.addListener(new AnonymousClass32());
            this.qrItemAnimation.start();
            return;
        }
        AnimatorSet animatorSet2 = this.qrItemAnimation;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
            this.qrItemAnimation = null;
        }
        this.isQrItemVisible = z2;
        ActionBarMenuItem actionBarMenuItem = this.qrItem;
        if (!z2) {
            f = 0.0f;
        }
        actionBarMenuItem.setAlpha(f);
        ActionBarMenuItem actionBarMenuItem2 = this.qrItem;
        if (!z2) {
            i = 8;
        }
        actionBarMenuItem2.setVisibility(i);
    }

    /* renamed from: org.telegram.ui.ProfileActivity$32 */
    /* loaded from: classes3.dex */
    public class AnonymousClass32 extends AnimatorListenerAdapter {
        AnonymousClass32() {
            ProfileActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ProfileActivity.this.qrItemAnimation = null;
        }
    }

    public void setForegroundImage(boolean z) {
        Drawable drawable = this.avatarImage.getImageReceiver().getDrawable();
        String str = null;
        if (drawable instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
            this.avatarImage.setForegroundImage(null, null, animatedFileDrawable);
            if (!z) {
                return;
            }
            animatedFileDrawable.addSecondParentView(this.avatarImage);
            return;
        }
        ImageLocation imageLocation = this.avatarsViewPager.getImageLocation(0);
        if (imageLocation != null && imageLocation.imageType == 2) {
            str = "avatar";
        }
        this.avatarImage.setForegroundImage(imageLocation, str, drawable);
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

    public void needLayoutText(float f) {
        float scaleX = this.nameTextView[1].getScaleX();
        float f2 = this.extraHeight > ((float) AndroidUtilities.dp(88.0f)) ? 1.67f : 1.12f;
        if (this.extraHeight <= AndroidUtilities.dp(88.0f) || scaleX == f2) {
            int dp = AndroidUtilities.isTablet() ? AndroidUtilities.dp(490.0f) : AndroidUtilities.displaySize.x;
            this.avatarsViewPagerIndicatorView.getSecondaryMenuItem();
            int i = 0;
            if (this.editItemVisible) {
                i = 48;
            }
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
            if (i4 == layoutParams2.width) {
                return;
            }
            this.onlineTextView[1].requestLayout();
            this.mediaCounterTextView.requestLayout();
        }
    }

    private void fixLayout() {
        View view = this.fragmentView;
        if (view == null) {
            return;
        }
        view.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass33());
    }

    /* renamed from: org.telegram.ui.ProfileActivity$33 */
    /* loaded from: classes3.dex */
    public class AnonymousClass33 implements ViewTreeObserver.OnPreDrawListener {
        AnonymousClass33() {
            ProfileActivity.this = r1;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            if (((BaseFragment) ProfileActivity.this).fragmentView != null) {
                ProfileActivity.this.checkListViewScroll();
                ProfileActivity.this.needLayout(true);
                ((BaseFragment) ProfileActivity.this).fragmentView.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            return true;
        }
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
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$Chat tLRPC$Chat;
        RecyclerListView recyclerListView;
        RecyclerListView recyclerListView2;
        RecyclerListView.Holder holder;
        int i3 = 0;
        if (i == NotificationCenter.updateInterfaces) {
            int intValue = ((Integer) objArr[0]).intValue();
            boolean z = ((MessagesController.UPDATE_MASK_AVATAR & intValue) == 0 && (MessagesController.UPDATE_MASK_NAME & intValue) == 0 && (MessagesController.UPDATE_MASK_STATUS & intValue) == 0) ? false : true;
            if (this.userId != 0) {
                if (z) {
                    updateProfileData(true);
                }
                if ((intValue & MessagesController.UPDATE_MASK_PHONE) == 0 || (recyclerListView2 = this.listView) == null || (holder = (RecyclerListView.Holder) recyclerListView2.findViewHolderForPosition(this.phoneRow)) == null) {
                    return;
                }
                this.listAdapter.onBindViewHolder(holder, this.phoneRow);
            } else if (this.chatId == 0) {
            } else {
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
        } else if (i == NotificationCenter.contactsDidLoad) {
            createActionBarMenu(true);
        } else if (i == NotificationCenter.encryptedChatCreated) {
            if (!this.creatingChat) {
                return;
            }
            AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda27(this, objArr));
        } else if (i == NotificationCenter.encryptedChatUpdated) {
            TLRPC$EncryptedChat tLRPC$EncryptedChat = (TLRPC$EncryptedChat) objArr[0];
            TLRPC$EncryptedChat tLRPC$EncryptedChat2 = this.currentEncryptedChat;
            if (tLRPC$EncryptedChat2 == null || tLRPC$EncryptedChat.id != tLRPC$EncryptedChat2.id) {
                return;
            }
            this.currentEncryptedChat = tLRPC$EncryptedChat;
            updateListAnimated(false);
        } else if (i == NotificationCenter.blockedUsersDidLoad) {
            boolean z2 = this.userBlocked;
            boolean z3 = getMessagesController().blockePeers.indexOfKey(this.userId) >= 0;
            this.userBlocked = z3;
            if (z2 == z3) {
                return;
            }
            createActionBarMenu(true);
            updateListAnimated(false);
        } else if (i == NotificationCenter.groupCallUpdated) {
            Long l2 = (Long) objArr[0];
            if (this.currentChat == null) {
                return;
            }
            long longValue = l2.longValue();
            TLRPC$Chat tLRPC$Chat2 = this.currentChat;
            if (longValue != tLRPC$Chat2.id || !ChatObject.canManageCalls(tLRPC$Chat2)) {
                return;
            }
            TLRPC$ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(l2.longValue());
            if (chatFull != null) {
                TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
                if (tLRPC$ChatFull != null) {
                    chatFull.participants = tLRPC$ChatFull.participants;
                }
                this.chatInfo = chatFull;
            }
            TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
            if (tLRPC$ChatFull2 == null) {
                return;
            }
            TLRPC$TL_inputGroupCall tLRPC$TL_inputGroupCall = tLRPC$ChatFull2.call;
            if ((tLRPC$TL_inputGroupCall != null || this.hasVoiceChatItem) && (tLRPC$TL_inputGroupCall == null || !this.hasVoiceChatItem)) {
                return;
            }
            createActionBarMenu(false);
        } else if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC$ChatFull tLRPC$ChatFull3 = (TLRPC$ChatFull) objArr[0];
            if (tLRPC$ChatFull3.id != this.chatId) {
                return;
            }
            boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            TLRPC$ChatFull tLRPC$ChatFull4 = this.chatInfo;
            if ((tLRPC$ChatFull4 instanceof TLRPC$TL_channelFull) && tLRPC$ChatFull3.participants == null) {
                tLRPC$ChatFull3.participants = tLRPC$ChatFull4.participants;
            }
            if (tLRPC$ChatFull4 == null && (tLRPC$ChatFull3 instanceof TLRPC$TL_channelFull)) {
                i3 = 1;
            }
            this.chatInfo = tLRPC$ChatFull3;
            if (this.mergeDialogId == 0) {
                long j = tLRPC$ChatFull3.migrated_from_chat_id;
                if (j != 0) {
                    this.mergeDialogId = -j;
                    getMediaDataController().getMediaCount(this.mergeDialogId, 0, this.classGuid, true);
                }
            }
            fetchUsersFromChannelInfo();
            ProfileGalleryView profileGalleryView = this.avatarsViewPager;
            if (profileGalleryView != null) {
                profileGalleryView.setChatInfo(this.chatInfo);
            }
            updateListAnimated(true);
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            if (chat != null) {
                this.currentChat = chat;
                createActionBarMenu(true);
            }
            if (this.currentChat.megagroup && (i3 != 0 || !booleanValue)) {
                getChannelParticipants(true);
            }
            updateAutoDeleteItem();
            updateTtlIcon();
        } else if (i == NotificationCenter.closeChats) {
            removeSelfFromStack();
        } else if (i == NotificationCenter.botInfoDidLoad) {
            TLRPC$BotInfo tLRPC$BotInfo = (TLRPC$BotInfo) objArr[0];
            if (tLRPC$BotInfo.user_id != this.userId) {
                return;
            }
            this.botInfo = tLRPC$BotInfo;
            updateListAnimated(false);
        } else if (i == NotificationCenter.userInfoDidLoad) {
            if (((Long) objArr[0]).longValue() != this.userId) {
                return;
            }
            TLRPC$UserFull tLRPC$UserFull = (TLRPC$UserFull) objArr[1];
            this.userInfo = tLRPC$UserFull;
            if (this.imageUpdater != null) {
                if (!TextUtils.equals(tLRPC$UserFull.about, this.currentBio)) {
                    this.listAdapter.notifyItemChanged(this.bioRow);
                }
            } else {
                if (!this.openAnimationInProgress && !this.callItemVisible) {
                    createActionBarMenu(true);
                } else {
                    this.recreateMenuAfterAnimation = true;
                }
                updateListAnimated(false);
                this.sharedMediaLayout.setCommonGroupsCount(this.userInfo.common_chats_count);
                updateSelectedMediaTabText();
                SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
                if (sharedMediaPreloader == null || sharedMediaPreloader.isMediaWasLoaded()) {
                    resumeDelayedFragmentAnimation();
                    needLayout(true);
                }
            }
            updateAutoDeleteItem();
            updateTtlIcon();
        } else if (i == NotificationCenter.privacyRulesUpdated) {
            if (this.qrItem == null) {
                return;
            }
            updateQrItemVisibility(true);
        } else if (i == NotificationCenter.didReceiveNewMessages) {
            if (((Boolean) objArr[2]).booleanValue()) {
                return;
            }
            long dialogId = getDialogId();
            if (dialogId != ((Long) objArr[0]).longValue()) {
                return;
            }
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
        } else if (i == NotificationCenter.emojiLoaded) {
            RecyclerListView recyclerListView3 = this.listView;
            if (recyclerListView3 == null) {
                return;
            }
            recyclerListView3.invalidateViews();
        } else if (i == NotificationCenter.reloadInterface) {
            updateListAnimated(false);
        } else if (i != NotificationCenter.newSuggestionsAvailable) {
        } else {
            int i4 = this.passwordSuggestionRow;
            int i5 = this.phoneSuggestionRow;
            updateRowsIds();
            if (i4 == this.passwordSuggestionRow && i5 == this.phoneSuggestionRow) {
                return;
            }
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$29(Object[] objArr) {
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.closeChats;
        notificationCenter.removeObserver(this, i);
        getNotificationCenter().postNotificationName(i, new Object[0]);
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
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onResume();
            setParentActivityTitle(LocaleController.getString("Settings", 2131628259));
        }
        updateProfileData(true);
        fixLayout();
        SimpleTextView[] simpleTextViewArr = this.nameTextView;
        if (simpleTextViewArr[1] != null) {
            setParentActivityTitle(simpleTextViewArr[1].getText());
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
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        SharedMediaLayout sharedMediaLayout;
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        if (profileGalleryView != null && profileGalleryView.getVisibility() == 0 && this.avatarsViewPager.getRealCount() > 1) {
            this.avatarsViewPager.getHitRect(this.rect);
            if (this.rect.contains((int) motionEvent.getX(), ((int) motionEvent.getY()) - this.actionBar.getMeasuredHeight())) {
                return false;
            }
        }
        if (this.sharedMediaRow == -1 || (sharedMediaLayout = this.sharedMediaLayout) == null) {
            return true;
        }
        if (!sharedMediaLayout.isSwipeBackEnabled()) {
            return false;
        }
        this.sharedMediaLayout.getHitRect(this.rect);
        if (this.rect.contains((int) motionEvent.getX(), ((int) motionEvent.getY()) - this.actionBar.getMeasuredHeight())) {
            return this.sharedMediaLayout.isCurrentTabFirst();
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        if (!this.sharedMediaLayout.isSwipeBackEnabled()) {
            return false;
        }
        return super.canBeginSlide();
    }

    public UndoView getUndoView() {
        return this.undoView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        SharedMediaLayout sharedMediaLayout;
        return this.actionBar.isEnabled() && (this.sharedMediaRow == -1 || (sharedMediaLayout = this.sharedMediaLayout) == null || !sharedMediaLayout.closeActionMode());
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
    }

    public void setPlayProfileAnimation(int i) {
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (!AndroidUtilities.isTablet()) {
            this.needTimerImage = i != 0;
            if (globalMainSettings.getBoolean("view_animations", true)) {
                this.playProfileAnimation = i;
            } else if (i != 2) {
            } else {
                this.expandPhoto = true;
            }
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
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            if (!z2) {
                if (this.playProfileAnimation != 0 && this.allowProfileAnimation) {
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
        }
        this.transitionAnimationInProress = false;
    }

    @Keep
    public float getAnimationProgress() {
        return this.animationProgress;
    }

    @Keep
    public void setAnimationProgress(float f) {
        int i;
        int i2;
        this.animationProgress = f;
        this.listView.setAlpha(f);
        this.listView.setTranslationX(AndroidUtilities.dp(48.0f) - (AndroidUtilities.dp(48.0f) * f));
        long j = 5;
        if (this.playProfileAnimation != 2 || (i = this.avatarColor) == 0) {
            i = AvatarDrawable.getProfileBackColorForId((this.userId != 0 || (ChatObject.isChannel(this.chatId, this.currentAccount) && !this.currentChat.megagroup)) ? 5L : this.chatId, this.resourcesProvider);
        }
        int i3 = this.actionBarAnimationColorFrom;
        if (i3 == 0) {
            i3 = getThemedColor("actionBarDefault");
        }
        this.topView.setBackgroundColor(ColorUtils.blendARGB(SharedConfig.chatBlurEnabled() ? ColorUtils.setAlphaComponent(i3, 0) : i3, i, f));
        this.timerDrawable.setBackgroundColor(ColorUtils.blendARGB(i3, i, f));
        this.actionBar.setItemsColor(ColorUtils.blendARGB(getThemedColor("actionBarDefaultIcon"), AvatarDrawable.getIconColorForId((this.userId != 0 || (ChatObject.isChannel(this.chatId, this.currentAccount) && !this.currentChat.megagroup)) ? 5L : this.chatId, this.resourcesProvider), f), false);
        int themedColor = getThemedColor("profile_title");
        int themedColor2 = getThemedColor("actionBarDefaultTitle");
        for (int i4 = 0; i4 < 2; i4++) {
            SimpleTextView[] simpleTextViewArr = this.nameTextView;
            if (simpleTextViewArr[i4] != null && (i4 != 1 || this.playProfileAnimation != 2)) {
                simpleTextViewArr[i4].setTextColor(ColorUtils.blendARGB(themedColor2, themedColor, f));
            }
        }
        if (this.isOnline[0]) {
            i2 = getThemedColor("profile_status");
        } else {
            if (this.userId == 0 && (!ChatObject.isChannel(this.chatId, this.currentAccount) || this.currentChat.megagroup)) {
                j = this.chatId;
            }
            i2 = AvatarDrawable.getProfileTextColorForId(j, this.resourcesProvider);
        }
        int themedColor3 = getThemedColor(this.isOnline[0] ? "chat_status" : "actionBarDefaultSubtitle");
        for (int i5 = 0; i5 < 2; i5++) {
            SimpleTextView[] simpleTextViewArr2 = this.onlineTextView;
            if (simpleTextViewArr2[i5] != null && (i5 != 1 || this.playProfileAnimation != 2)) {
                simpleTextViewArr2[i5].setTextColor(ColorUtils.blendARGB(themedColor3, i2, f));
            }
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
        int i6 = this.navigationBarAnimationColorFrom;
        if (i6 != 0) {
            setNavigationBarColor(ColorUtils.blendARGB(i6, getNavigationBarColor(), f));
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
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getNavigationBarColor() {
        return Theme.getColor("windowBackgroundGray", this.resourcesProvider);
    }

    /* JADX WARN: Removed duplicated region for block: B:147:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0316  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AnimatorSet onCustomTransitionAnimation(boolean z, Runnable runnable) {
        boolean z2;
        boolean z3;
        float ceil;
        BaseFragment baseFragment = null;
        if (this.playProfileAnimation == 0 || !this.allowProfileAnimation || this.isPulledDown || this.disableProfileAnimation) {
            return null;
        }
        ImageView imageView = this.timeItem;
        if (imageView != null) {
            imageView.setAlpha(1.0f);
        }
        ActionBarLayout actionBarLayout = this.parentLayout;
        if (actionBarLayout != null && actionBarLayout.fragmentsStack.size() >= 2) {
            ArrayList<BaseFragment> arrayList = this.parentLayout.fragmentsStack;
            BaseFragment baseFragment2 = arrayList.get(arrayList.size() - 2);
            if (baseFragment2 instanceof ChatActivity) {
                this.previousTransitionFragment = (ChatActivity) baseFragment2;
            }
        }
        if (this.previousTransitionFragment != null) {
            updateTimeItem();
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(this.playProfileAnimation == 2 ? 250L : 180L);
        this.listView.setLayerType(2, null);
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (createMenu.getItem(10) == null && this.animatingItem == null) {
            this.animatingItem = createMenu.addItem(10, 2131165453);
        }
        if (z) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.onlineTextView[1].getLayoutParams();
            layoutParams.rightMargin = (int) ((AndroidUtilities.density * (-21.0f)) + AndroidUtilities.dp(8.0f));
            this.onlineTextView[1].setLayoutParams(layoutParams);
            if (this.playProfileAnimation != 2) {
                float measureText = (this.nameTextView[1].getPaint().measureText(this.nameTextView[1].getText().toString()) * 1.12f) + this.nameTextView[1].getSideDrawablesSize();
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.nameTextView[1].getLayoutParams();
                if (((int) Math.ceil((AndroidUtilities.displaySize.x - AndroidUtilities.dp(126.0f)) + (AndroidUtilities.density * 21.0f))) < measureText) {
                    layoutParams2.width = (int) Math.ceil(ceil / 1.12f);
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
            setAnimationProgress(0.0f);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(ObjectAnimator.ofFloat(this, "animationProgress", 0.0f, 1.0f));
            RLottieImageView rLottieImageView = this.writeButton;
            if (rLottieImageView != null && rLottieImageView.getTag() == null) {
                this.writeButton.setScaleX(0.2f);
                this.writeButton.setScaleY(0.2f);
                this.writeButton.setAlpha(0.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_X, 1.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, 1.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, 1.0f));
            }
            if (this.playProfileAnimation == 2) {
                this.avatarColor = AndroidUtilities.calcBitmapColor(this.avatarImage.getImageReceiver().getBitmap());
                this.nameTextView[1].setTextColor(-1);
                this.onlineTextView[1].setTextColor(Color.argb(179, 255, 255, 255));
                this.actionBar.setItemsBackgroundColor(1090519039, false);
                this.overlaysView.setOverlaysVisible();
            }
            int i = 0;
            while (i < 2) {
                this.nameTextView[i].setAlpha(i == 0 ? 1.0f : 0.0f);
                SimpleTextView simpleTextView = this.nameTextView[i];
                Property property = View.ALPHA;
                float[] fArr = new float[1];
                fArr[0] = i == 0 ? 0.0f : 1.0f;
                arrayList2.add(ObjectAnimator.ofFloat(simpleTextView, property, fArr));
                i++;
            }
            if (this.timeItem.getTag() != null) {
                arrayList2.add(ObjectAnimator.ofFloat(this.timeItem, View.ALPHA, 1.0f, 0.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_X, 1.0f, 0.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_Y, 1.0f, 0.0f));
            }
            ActionBarMenuItem actionBarMenuItem = this.animatingItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setAlpha(1.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.animatingItem, View.ALPHA, 0.0f));
            }
            if (this.callItemVisible && this.chatId != 0) {
                this.callItem.setAlpha(0.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.callItem, View.ALPHA, 1.0f));
            }
            if (this.videoCallItemVisible) {
                this.videoCallItem.setAlpha(0.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.videoCallItem, View.ALPHA, 1.0f));
            }
            if (this.editItemVisible) {
                this.editItem.setAlpha(0.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.editItem, View.ALPHA, 1.0f));
            }
            if (this.ttlIconView.getTag() != null) {
                this.ttlIconView.setAlpha(0.0f);
                arrayList2.add(ObjectAnimator.ofFloat(this.ttlIconView, View.ALPHA, 1.0f));
            }
            if (this.parentLayout.fragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList3 = this.parentLayout.fragmentsStack;
                baseFragment = arrayList3.get(arrayList3.size() - 2);
            }
            if (baseFragment instanceof ChatActivity) {
                ChatAvatarContainer avatarContainer = ((ChatActivity) baseFragment).getAvatarContainer();
                if (avatarContainer.getSubtitleTextView().getLeftDrawable() != null || avatarContainer.statusMadeShorter[0]) {
                    this.transitionOnlineText = avatarContainer.getSubtitleTextView();
                    this.avatarContainer2.invalidate();
                    this.onlineTextView[0].setAlpha(0.0f);
                    this.onlineTextView[1].setAlpha(0.0f);
                    arrayList2.add(ObjectAnimator.ofFloat(this.onlineTextView[1], View.ALPHA, 1.0f));
                    z3 = true;
                    if (!z3) {
                        int i2 = 0;
                        while (i2 < 2) {
                            this.onlineTextView[i2].setAlpha(i2 == 0 ? 1.0f : 0.0f);
                            SimpleTextView simpleTextView2 = this.onlineTextView[i2];
                            Property property2 = View.ALPHA;
                            float[] fArr2 = new float[1];
                            fArr2[0] = i2 == 0 ? 0.0f : 1.0f;
                            arrayList2.add(ObjectAnimator.ofFloat(simpleTextView2, property2, fArr2));
                            i2++;
                        }
                    }
                    animatorSet.playTogether(arrayList2);
                }
            }
            z3 = false;
            if (!z3) {
            }
            animatorSet.playTogether(arrayList2);
        } else {
            this.initialAnimationExtraHeight = this.extraHeight;
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add(ObjectAnimator.ofFloat(this, "animationProgress", 1.0f, 0.0f));
            RLottieImageView rLottieImageView2 = this.writeButton;
            if (rLottieImageView2 != null) {
                arrayList4.add(ObjectAnimator.ofFloat(rLottieImageView2, View.SCALE_X, 0.2f));
                arrayList4.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, 0.2f));
                arrayList4.add(ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, 0.0f));
            }
            int i3 = 0;
            while (i3 < 2) {
                SimpleTextView simpleTextView3 = this.nameTextView[i3];
                Property property3 = View.ALPHA;
                float[] fArr3 = new float[1];
                fArr3[0] = i3 == 0 ? 1.0f : 0.0f;
                arrayList4.add(ObjectAnimator.ofFloat(simpleTextView3, property3, fArr3));
                i3++;
            }
            if (this.timeItem.getTag() != null) {
                this.timeItem.setAlpha(0.0f);
                arrayList4.add(ObjectAnimator.ofFloat(this.timeItem, View.ALPHA, 0.0f, 1.0f));
                arrayList4.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_X, 0.0f, 1.0f));
                arrayList4.add(ObjectAnimator.ofFloat(this.timeItem, View.SCALE_Y, 0.0f, 1.0f));
            }
            ActionBarMenuItem actionBarMenuItem2 = this.animatingItem;
            if (actionBarMenuItem2 != null) {
                actionBarMenuItem2.setAlpha(0.0f);
                arrayList4.add(ObjectAnimator.ofFloat(this.animatingItem, View.ALPHA, 1.0f));
            }
            if (this.callItemVisible && this.chatId != 0) {
                this.callItem.setAlpha(1.0f);
                arrayList4.add(ObjectAnimator.ofFloat(this.callItem, View.ALPHA, 0.0f));
            }
            if (this.videoCallItemVisible) {
                this.videoCallItem.setAlpha(1.0f);
                arrayList4.add(ObjectAnimator.ofFloat(this.videoCallItem, View.ALPHA, 0.0f));
            }
            if (this.editItemVisible) {
                this.editItem.setAlpha(1.0f);
                arrayList4.add(ObjectAnimator.ofFloat(this.editItem, View.ALPHA, 0.0f));
            }
            ImageView imageView2 = this.ttlIconView;
            if (imageView2 != null) {
                arrayList4.add(ObjectAnimator.ofFloat(imageView2, View.ALPHA, imageView2.getAlpha(), 0.0f));
            }
            if (this.parentLayout.fragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList5 = this.parentLayout.fragmentsStack;
                baseFragment = arrayList5.get(arrayList5.size() - 2);
            }
            if (baseFragment instanceof ChatActivity) {
                ChatAvatarContainer avatarContainer2 = ((ChatActivity) baseFragment).getAvatarContainer();
                if (avatarContainer2.getSubtitleTextView().getLeftDrawable() != null || avatarContainer2.statusMadeShorter[0]) {
                    this.transitionOnlineText = avatarContainer2.getSubtitleTextView();
                    this.avatarContainer2.invalidate();
                    arrayList4.add(ObjectAnimator.ofFloat(this.onlineTextView[0], View.ALPHA, 0.0f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.onlineTextView[1], View.ALPHA, 0.0f));
                    z2 = true;
                    if (!z2) {
                        int i4 = 0;
                        while (i4 < 2) {
                            SimpleTextView simpleTextView4 = this.onlineTextView[i4];
                            Property property4 = View.ALPHA;
                            float[] fArr4 = new float[1];
                            fArr4[0] = i4 == 0 ? 1.0f : 0.0f;
                            arrayList4.add(ObjectAnimator.ofFloat(simpleTextView4, property4, fArr4));
                            i4++;
                        }
                    }
                    animatorSet.playTogether(arrayList4);
                }
            }
            z2 = false;
            if (!z2) {
            }
            animatorSet.playTogether(arrayList4);
        }
        this.profileTransitionInProgress = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ProfileActivity$$ExternalSyntheticLambda0(this));
        animatorSet.playTogether(ofFloat);
        animatorSet.addListener(new AnonymousClass34(runnable));
        animatorSet.setInterpolator(this.playProfileAnimation == 2 ? CubicBezierInterpolator.DEFAULT : new DecelerateInterpolator());
        AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda18(animatorSet), 50L);
        return animatorSet;
    }

    public /* synthetic */ void lambda$onCustomTransitionAnimation$30(ValueAnimator valueAnimator) {
        this.fragmentView.invalidate();
    }

    /* renamed from: org.telegram.ui.ProfileActivity$34 */
    /* loaded from: classes3.dex */
    class AnonymousClass34 extends AnimatorListenerAdapter {
        final /* synthetic */ Runnable val$callback;

        AnonymousClass34(Runnable runnable) {
            ProfileActivity.this = r1;
            this.val$callback = runnable;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ProfileActivity.this.listView.setLayerType(0, null);
            if (ProfileActivity.this.animatingItem != null) {
                ((BaseFragment) ProfileActivity.this).actionBar.createMenu().clearItems();
                ProfileActivity.this.animatingItem = null;
            }
            this.val$callback.run();
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
        }
    }

    public void updateOnlineCount(boolean z) {
        TLRPC$UserStatus tLRPC$UserStatus;
        this.onlineCount = 0;
        int currentTime = getConnectionsManager().getCurrentTime();
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
                Collections.sort(this.sortedUsers, new ProfileActivity$$ExternalSyntheticLambda29(this, currentTime));
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (z && this.listAdapter != null && this.membersStartRow > 0) {
                AndroidUtilities.updateVisibleRows(this.listView);
            }
            if (this.sharedMediaLayout == null || this.sharedMediaRow == -1) {
                return;
            }
            if ((this.sortedUsers.size() <= 5 && this.usersForceShowingIn != 2) || this.usersForceShowingIn == 1) {
                return;
            }
            this.sharedMediaLayout.setChatUsers(this.sortedUsers, this.chatInfo);
        } else if ((tLRPC$ChatFull instanceof TLRPC$TL_channelFull) && tLRPC$ChatFull.participants_count > 200) {
            this.onlineCount = tLRPC$ChatFull.online_count;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0074 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x007f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x008a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0093 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ int lambda$updateOnlineCount$31(int i, Integer num, Integer num2) {
        int i2;
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.chatInfo.participants.participants.get(num2.intValue()).user_id));
        TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(this.chatInfo.participants.participants.get(num.intValue()).user_id));
        int i3 = -110;
        if (user != null) {
            if (user.bot) {
                i2 = -110;
            } else if (user.self) {
                i2 = i + 50000;
            } else {
                TLRPC$UserStatus tLRPC$UserStatus = user.status;
                if (tLRPC$UserStatus != null) {
                    i2 = tLRPC$UserStatus.expires;
                }
            }
            if (user2 != null) {
                if (!user2.bot) {
                    if (user2.self) {
                        i3 = i + 50000;
                    } else {
                        TLRPC$UserStatus tLRPC$UserStatus2 = user2.status;
                        if (tLRPC$UserStatus2 != null) {
                            i3 = tLRPC$UserStatus2.expires;
                        }
                    }
                }
                if (i2 <= 0 && i3 > 0) {
                    if (i2 > i3) {
                        return 1;
                    }
                    return i2 < i3 ? -1 : 0;
                } else if (i2 >= 0 && i3 < 0) {
                    if (i2 > i3) {
                        return 1;
                    }
                    return i2 < i3 ? -1 : 0;
                } else if ((i2 >= 0 && i3 > 0) || (i2 == 0 && i3 != 0)) {
                    return -1;
                } else {
                    return ((i3 < 0 || i2 <= 0) && (i3 != 0 || i2 == 0)) ? 0 : 1;
                }
            }
            i3 = 0;
            if (i2 <= 0) {
            }
            if (i2 >= 0) {
            }
            if (i2 >= 0) {
            }
            if (i3 < 0) {
            }
        }
        i2 = 0;
        if (user2 != null) {
        }
        i3 = 0;
        if (i2 <= 0) {
        }
        if (i2 >= 0) {
        }
        if (i2 >= 0) {
        }
        if (i3 < 0) {
        }
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.chatInfo = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            long j = tLRPC$ChatFull.migrated_from_chat_id;
            if (j != 0 && this.mergeDialogId == 0) {
                this.mergeDialogId = -j;
                getMediaDataController().getMediaCounts(this.mergeDialogId, this.classGuid);
            }
        }
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            sharedMediaLayout.setChatInfo(this.chatInfo);
        }
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        if (profileGalleryView != null) {
            profileGalleryView.setChatInfo(this.chatInfo);
        }
        fetchUsersFromChannelInfo();
    }

    public void setUserInfo(TLRPC$UserFull tLRPC$UserFull) {
        this.userInfo = tLRPC$UserFull;
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
            getMessagesController().deleteParticipantFromChat(this.chatId, user, this.chatInfo);
            if (this.currentChat != null && user != null && BulletinFactory.canShowBulletin(this)) {
                BulletinFactory.createRemoveFromChatBulletin(this, user, this.currentChat.title).show();
            }
            if (!this.chatInfo.participants.participants.remove(tLRPC$ChatParticipant)) {
                return;
            }
            updateListAnimated(true);
            return;
        }
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.closeChats;
        notificationCenter.removeObserver(this, i);
        if (AndroidUtilities.isTablet()) {
            getNotificationCenter().postNotificationName(i, Long.valueOf(-this.chatId));
        } else {
            getNotificationCenter().postNotificationName(i, new Object[0]);
        }
        getMessagesController().deleteParticipantFromChat(this.chatId, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), this.chatInfo);
        this.playProfileAnimation = 0;
        finishFragment();
    }

    public boolean isChat() {
        return this.chatId != 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x00e1, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated) == false) goto L27;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRowsIds() {
        boolean z;
        int i;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$ChatParticipants tLRPC$ChatParticipants;
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$ChatFull tLRPC$ChatFull2;
        TLRPC$UserFull tLRPC$UserFull;
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
        this.unblockRow = -1;
        this.joinRow = -1;
        this.lastSectionRow = -1;
        this.visibleChatParticipants.clear();
        this.visibleSortedUsers.clear();
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
        boolean z2 = true;
        if (sharedMediaPreloader != null) {
            for (int i3 : sharedMediaPreloader.getLastMediaCount()) {
                if (i3 > 0) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
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
                this.notificationRow = i17;
                int i19 = i18 + 1;
                this.rowCount = i19;
                this.privacyRow = i18;
                int i20 = i19 + 1;
                this.rowCount = i20;
                this.dataRow = i19;
                int i21 = i20 + 1;
                this.rowCount = i21;
                this.chatRow = i20;
                this.rowCount = i21 + 1;
                this.stickersRow = i21;
                if (getMessagesController().filtersEnabled || !getMessagesController().dialogFilters.isEmpty()) {
                    int i22 = this.rowCount;
                    this.rowCount = i22 + 1;
                    this.filtersRow = i22;
                }
                int i23 = this.rowCount;
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
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    int i37 = this.rowCount;
                    this.rowCount = i37 + 1;
                    this.switchBackendRow = i37;
                }
                int i38 = this.rowCount;
                this.rowCount = i38 + 1;
                this.versionRow = i38;
            } else {
                TLRPC$UserFull tLRPC$UserFull2 = this.userInfo;
                boolean z3 = (tLRPC$UserFull2 != null && !TextUtils.isEmpty(tLRPC$UserFull2.about)) || (user != null && !TextUtils.isEmpty(user.username));
                if (user == null || TextUtils.isEmpty(user.phone)) {
                    z2 = false;
                }
                int i39 = this.rowCount;
                int i40 = i39 + 1;
                this.rowCount = i40;
                this.infoHeaderRow = i39;
                if (!this.isBot && (z2 || !z3)) {
                    this.rowCount = i40 + 1;
                    this.phoneRow = i40;
                }
                TLRPC$UserFull tLRPC$UserFull3 = this.userInfo;
                if (tLRPC$UserFull3 != null && !TextUtils.isEmpty(tLRPC$UserFull3.about)) {
                    int i41 = this.rowCount;
                    this.rowCount = i41 + 1;
                    this.userInfoRow = i41;
                }
                if (user != null && !TextUtils.isEmpty(user.username)) {
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
                if (z || ((tLRPC$UserFull = this.userInfo) != null && tLRPC$UserFull.common_chats_count != 0)) {
                    int i53 = this.rowCount;
                    this.rowCount = i53 + 1;
                    this.sharedMediaRow = i53;
                } else if (this.lastSectionRow == -1 && this.needSendMessage) {
                    int i54 = this.rowCount;
                    int i55 = i54 + 1;
                    this.rowCount = i55;
                    this.sendMessageRow = i54;
                    int i56 = i55 + 1;
                    this.rowCount = i56;
                    this.reportRow = i55;
                    this.rowCount = i56 + 1;
                    this.lastSectionRow = i56;
                }
            }
        } else if (this.chatId != 0) {
            TLRPC$ChatFull tLRPC$ChatFull3 = this.chatInfo;
            if ((tLRPC$ChatFull3 != null && (!TextUtils.isEmpty(tLRPC$ChatFull3.about) || (this.chatInfo.location instanceof TLRPC$TL_channelLocation))) || !TextUtils.isEmpty(this.currentChat.username)) {
                if (LocaleController.isRTL && ChatObject.isChannel(this.currentChat) && (tLRPC$ChatFull2 = this.chatInfo) != null && !this.currentChat.megagroup && tLRPC$ChatFull2.linked_chat_id != 0) {
                    int i57 = this.rowCount;
                    this.rowCount = i57 + 1;
                    this.emptyRow = i57;
                }
                int i58 = this.rowCount;
                this.rowCount = i58 + 1;
                this.infoHeaderRow = i58;
                TLRPC$ChatFull tLRPC$ChatFull4 = this.chatInfo;
                if (tLRPC$ChatFull4 != null) {
                    if (!TextUtils.isEmpty(tLRPC$ChatFull4.about)) {
                        int i59 = this.rowCount;
                        this.rowCount = i59 + 1;
                        this.channelInfoRow = i59;
                    }
                    if (this.chatInfo.location instanceof TLRPC$TL_channelLocation) {
                        int i60 = this.rowCount;
                        this.rowCount = i60 + 1;
                        this.locationRow = i60;
                    }
                }
                if (!TextUtils.isEmpty(this.currentChat.username)) {
                    int i61 = this.rowCount;
                    this.rowCount = i61 + 1;
                    this.usernameRow = i61;
                }
            }
            if (this.infoHeaderRow != -1) {
                int i62 = this.rowCount;
                this.rowCount = i62 + 1;
                this.notificationsDividerRow = i62;
            }
            int i63 = this.rowCount;
            int i64 = i63 + 1;
            this.rowCount = i64;
            this.notificationsRow = i63;
            this.rowCount = i64 + 1;
            this.infoSectionRow = i64;
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC$Chat tLRPC$Chat = this.currentChat;
                if (!tLRPC$Chat.megagroup && (tLRPC$ChatFull = this.chatInfo) != null && (tLRPC$Chat.creator || tLRPC$ChatFull.can_view_participants)) {
                    int i65 = this.rowCount;
                    int i66 = i65 + 1;
                    this.rowCount = i66;
                    this.membersHeaderRow = i65;
                    int i67 = i66 + 1;
                    this.rowCount = i67;
                    this.subscribersRow = i66;
                    if (tLRPC$ChatFull.requests_pending > 0) {
                        this.rowCount = i67 + 1;
                        this.subscribersRequestsRow = i67;
                    }
                    int i68 = this.rowCount;
                    int i69 = i68 + 1;
                    this.rowCount = i69;
                    this.administratorsRow = i68;
                    if (tLRPC$ChatFull.banned_count != 0 || tLRPC$ChatFull.kicked_count != 0) {
                        this.rowCount = i69 + 1;
                        this.blockedUsersRow = i69;
                    }
                    int i70 = this.rowCount;
                    this.rowCount = i70 + 1;
                    this.membersSectionRow = i70;
                }
            }
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC$ChatFull tLRPC$ChatFull5 = this.chatInfo;
                if (tLRPC$ChatFull5 != null && this.currentChat.megagroup && (tLRPC$ChatParticipants = tLRPC$ChatFull5.participants) != null && !tLRPC$ChatParticipants.participants.isEmpty()) {
                    if (!ChatObject.isNotInChat(this.currentChat) && ChatObject.canAddUsers(this.currentChat) && this.chatInfo.participants_count < getMessagesController().maxMegagroupCount) {
                        int i71 = this.rowCount;
                        this.rowCount = i71 + 1;
                        this.addMemberRow = i71;
                    }
                    int size = this.chatInfo.participants.participants.size();
                    if ((size <= 5 || !z || this.usersForceShowingIn == 1) && this.usersForceShowingIn != 2) {
                        if (this.addMemberRow == -1) {
                            int i72 = this.rowCount;
                            this.rowCount = i72 + 1;
                            this.membersHeaderRow = i72;
                        }
                        int i73 = this.rowCount;
                        this.membersStartRow = i73;
                        int i74 = i73 + size;
                        this.rowCount = i74;
                        this.membersEndRow = i74;
                        this.rowCount = i74 + 1;
                        this.membersSectionRow = i74;
                        this.visibleChatParticipants.addAll(this.chatInfo.participants.participants);
                        ArrayList<Integer> arrayList = this.sortedUsers;
                        if (arrayList != null) {
                            this.visibleSortedUsers.addAll(arrayList);
                        }
                        this.usersForceShowingIn = 1;
                        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
                        if (sharedMediaLayout != null) {
                            sharedMediaLayout.setChatUsers(null, null);
                        }
                    } else {
                        if (this.addMemberRow != -1) {
                            int i75 = this.rowCount;
                            this.rowCount = i75 + 1;
                            this.membersSectionRow = i75;
                        }
                        if (this.sharedMediaLayout != null) {
                            if (!this.sortedUsers.isEmpty()) {
                                this.usersForceShowingIn = 2;
                            }
                            this.sharedMediaLayout.setChatUsers(this.sortedUsers, this.chatInfo);
                        }
                    }
                }
                if (this.lastSectionRow == -1) {
                    TLRPC$Chat tLRPC$Chat2 = this.currentChat;
                    if (tLRPC$Chat2.left && !tLRPC$Chat2.kicked) {
                        int i76 = this.rowCount;
                        int i77 = i76 + 1;
                        this.rowCount = i77;
                        this.joinRow = i76;
                        this.rowCount = i77 + 1;
                        this.lastSectionRow = i77;
                    }
                }
            } else {
                TLRPC$ChatFull tLRPC$ChatFull6 = this.chatInfo;
                if (tLRPC$ChatFull6 != null && !(tLRPC$ChatFull6.participants instanceof TLRPC$TL_chatParticipantsForbidden)) {
                    if (ChatObject.canAddUsers(this.currentChat) || (tLRPC$TL_chatBannedRights = this.currentChat.default_banned_rights) == null || !tLRPC$TL_chatBannedRights.invite_users) {
                        int i78 = this.rowCount;
                        this.rowCount = i78 + 1;
                        this.addMemberRow = i78;
                    }
                    if (this.chatInfo.participants.participants.size() <= 5 || !z) {
                        if (this.addMemberRow == -1) {
                            int i79 = this.rowCount;
                            this.rowCount = i79 + 1;
                            this.membersHeaderRow = i79;
                        }
                        int i80 = this.rowCount;
                        this.membersStartRow = i80;
                        int size2 = i80 + this.chatInfo.participants.participants.size();
                        this.rowCount = size2;
                        this.membersEndRow = size2;
                        this.rowCount = size2 + 1;
                        this.membersSectionRow = size2;
                        this.visibleChatParticipants.addAll(this.chatInfo.participants.participants);
                        ArrayList<Integer> arrayList2 = this.sortedUsers;
                        if (arrayList2 != null) {
                            this.visibleSortedUsers.addAll(arrayList2);
                        }
                        SharedMediaLayout sharedMediaLayout2 = this.sharedMediaLayout;
                        if (sharedMediaLayout2 != null) {
                            sharedMediaLayout2.setChatUsers(null, null);
                        }
                    } else {
                        if (this.addMemberRow != -1) {
                            int i81 = this.rowCount;
                            this.rowCount = i81 + 1;
                            this.membersSectionRow = i81;
                        }
                        SharedMediaLayout sharedMediaLayout3 = this.sharedMediaLayout;
                        if (sharedMediaLayout3 != null) {
                            sharedMediaLayout3.setChatUsers(this.sortedUsers, this.chatInfo);
                        }
                    }
                }
            }
            if (z) {
                int i82 = this.rowCount;
                this.rowCount = i82 + 1;
                this.sharedMediaRow = i82;
            }
        }
        if (this.sharedMediaRow == -1) {
            int i83 = this.rowCount;
            this.rowCount = i83 + 1;
            this.bottomPaddingRow = i83;
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
            scamDrawable.setColor(getThemedColor("avatar_subtitleInProfileBlue"));
        }
        return this.scamDrawable;
    }

    private Drawable getLockIconDrawable() {
        if (this.lockIconDrawable == null) {
            this.lockIconDrawable = Theme.chat_lockIconDrawable.getConstantState().newDrawable().mutate();
        }
        return this.lockIconDrawable;
    }

    private Drawable getVerifiedCrossfadeDrawable() {
        if (this.verifiedCrossfadeDrawable == null) {
            this.verifiedDrawable = Theme.profile_verifiedDrawable.getConstantState().newDrawable().mutate();
            this.verifiedCheckDrawable = Theme.profile_verifiedCheckDrawable.getConstantState().newDrawable().mutate();
            this.verifiedCrossfadeDrawable = new CrossfadeDrawable(new CombinedDrawable(this.verifiedDrawable, this.verifiedCheckDrawable), ContextCompat.getDrawable(getParentActivity(), 2131166192));
        }
        return this.verifiedCrossfadeDrawable;
    }

    private Drawable getPremiumCrossfadeDrawable() {
        if (this.premuimCrossfadeDrawable == null) {
            Drawable mutate = ContextCompat.getDrawable(getParentActivity(), 2131165872).mutate();
            this.premiumStarDrawable = mutate;
            mutate.setColorFilter(getThemedColor("profile_verifiedBackground"), PorterDuff.Mode.MULTIPLY);
            this.premuimCrossfadeDrawable = new CrossfadeDrawable(this.premiumStarDrawable, ContextCompat.getDrawable(getParentActivity(), 2131165876).mutate());
        }
        return this.premuimCrossfadeDrawable;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x024e  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02cf  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x030f  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x031a  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0515  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0609 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0624  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x063f  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0664  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x06e3  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x071f  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0722  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0738  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x073f  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0742  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x0794  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x07de  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x0332 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:358:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateProfileData(boolean z) {
        CharSequence charSequence;
        boolean z2;
        CharSequence charSequence2;
        String str;
        String str2;
        boolean z3;
        int i;
        ImageLocation forUserOrChat;
        ImageLocation imageLocation;
        View findViewByPosition;
        String str3;
        String str4;
        String str5;
        String str6;
        boolean z4;
        CharSequence charSequence3;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$ChatFull tLRPC$ChatFull;
        int i2;
        TLRPC$Chat tLRPC$Chat2;
        String str7;
        String str8;
        String lowerCase;
        int i3;
        String str9;
        String str10;
        String str11;
        TLRPC$User tLRPC$User;
        Drawable drawable;
        Drawable drawable2;
        TLRPC$User tLRPC$User2;
        Drawable drawable3;
        String str12;
        if (this.avatarContainer == null || this.nameTextView == null) {
            return;
        }
        int connectionState = getConnectionsManager().getConnectionState();
        boolean z5 = true;
        if (connectionState == 2) {
            charSequence = LocaleController.getString("WaitingForNetwork", 2131629221);
        } else if (connectionState == 1) {
            charSequence = LocaleController.getString("Connecting", 2131625225);
        } else if (connectionState == 5) {
            charSequence = LocaleController.getString("Updating", 2131628769);
        } else {
            charSequence = connectionState == 4 ? LocaleController.getString("ConnectingToProxy", 2131625227) : null;
        }
        String str13 = "drawableMuteIcon";
        if (this.userId != 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user == null) {
                return;
            }
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = user.photo;
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$UserProfilePhoto != null ? tLRPC$UserProfilePhoto.photo_big : null;
            this.avatarDrawable.setInfo(user);
            ImageLocation forUserOrChat2 = ImageLocation.getForUserOrChat(user, 0);
            ImageLocation forUserOrChat3 = ImageLocation.getForUserOrChat(user, 1);
            ImageLocation forUserOrChat4 = ImageLocation.getForUserOrChat(user, 3);
            ImageLocation currentVideoLocation = this.avatarsViewPager.getCurrentVideoLocation(forUserOrChat3, forUserOrChat2);
            this.avatarsViewPager.initIfEmpty(forUserOrChat2, forUserOrChat3, z);
            if (this.avatarBig == null) {
                if (forUserOrChat4 != null) {
                    this.avatarImage.getImageReceiver().setVideoThumbIsSame(true);
                    this.avatarImage.setImage(currentVideoLocation, "g", forUserOrChat4, "avatar", this.avatarDrawable, user);
                } else {
                    this.avatarImage.setImage(currentVideoLocation, "g", forUserOrChat3, "50_50", this.avatarDrawable, user);
                }
            }
            if ((forUserOrChat3 != null && this.setAvatarRow != -1) || (forUserOrChat3 == null && this.setAvatarRow == -1)) {
                updateListAnimated(false);
                needLayout(true);
            }
            if (forUserOrChat2 != null) {
                ImageLocation imageLocation2 = this.prevLoadedImageLocation;
                str9 = "AccDescrVerified";
                if (imageLocation2 == null || forUserOrChat2.photoId != imageLocation2.photoId) {
                    this.prevLoadedImageLocation = forUserOrChat2;
                    getFileLoader().loadFile(forUserOrChat2, user, null, 0, 1);
                }
            } else {
                str9 = "AccDescrVerified";
            }
            CharSequence userName = UserObject.getUserName(user);
            if (user.id == getUserConfig().getClientUserId()) {
                str10 = LocaleController.getString("Online", 2131627080);
            } else {
                long j = user.id;
                if (j == 333000 || j == 777000 || j == 42777) {
                    str10 = LocaleController.getString("ServiceNotifications", 2131628229);
                } else if (MessagesController.isSupportUser(user)) {
                    str10 = LocaleController.getString("SupportStatus", 2131628507);
                } else if (this.isBot) {
                    str10 = LocaleController.getString("Bot", 2131624704);
                } else {
                    boolean[] zArr = this.isOnline;
                    zArr[0] = false;
                    str10 = LocaleController.formatUserStatus(this.currentAccount, user, zArr);
                    SimpleTextView[] simpleTextViewArr = this.onlineTextView;
                    if (simpleTextViewArr[1] != null && !this.mediaHeaderVisible) {
                        String str14 = this.isOnline[0] ? "profile_status" : "avatar_subtitleInProfileBlue";
                        simpleTextViewArr[1].setTag(str14);
                        if (!this.isPulledDown) {
                            this.onlineTextView[1].setTextColor(getThemedColor(str14));
                        }
                    }
                }
            }
            try {
                userName = Emoji.replaceEmoji(userName, this.nameTextView[1].getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
            } catch (Exception unused) {
            }
            int i4 = 2;
            int i5 = 0;
            while (i5 < i4) {
                if (this.nameTextView[i5] == null) {
                    tLRPC$User = user;
                    str11 = str9;
                } else {
                    if (i5 == 0 && user.id != getUserConfig().getClientUserId()) {
                        long j2 = user.id;
                        if (j2 / 1000 != 777 && j2 / 1000 != 333 && (str12 = user.phone) != null && str12.length() != 0 && getContactsController().contactsDict.get(Long.valueOf(user.id)) == null && (getContactsController().contactsDict.size() != 0 || !getContactsController().isLoadingContacts())) {
                            PhoneFormat phoneFormat = PhoneFormat.getInstance();
                            this.nameTextView[i5].setText(phoneFormat.format("+" + user.phone));
                            if (i5 != 0 && charSequence != null) {
                                this.onlineTextView[i5].setText(charSequence);
                            } else {
                                this.onlineTextView[i5].setText(str10);
                            }
                            Drawable lockIconDrawable = this.currentEncryptedChat == null ? getLockIconDrawable() : null;
                            if (i5 != 0) {
                                boolean z6 = user.scam;
                                if (z6 || user.fake) {
                                    tLRPC$User2 = user;
                                    str11 = str9;
                                    drawable3 = getScamDrawable(!z6 ? 1 : 0);
                                    this.nameTextViewRightDrawableContentDescription = LocaleController.getString("ScamMessage", 2131628080);
                                } else if (user.verified) {
                                    drawable = getVerifiedCrossfadeDrawable();
                                    str11 = str9;
                                    this.nameTextViewRightDrawableContentDescription = LocaleController.getString(str11, 2131624102);
                                    tLRPC$User = user;
                                    z5 = false;
                                    this.nameTextView[i5].setLeftDrawable(lockIconDrawable);
                                    this.nameTextView[i5].setRightDrawable(drawable);
                                    if (i5 == 0) {
                                        this.nameTextView[i5].setCanHideRightDrawable(z5);
                                    }
                                    if (tLRPC$User.self && getMessagesController().isPremiumUser(tLRPC$User)) {
                                        SimpleTextView[] simpleTextViewArr2 = this.nameTextView;
                                        simpleTextViewArr2[i5].setRightDrawableOnClick(new ProfileActivity$$ExternalSyntheticLambda14(this, tLRPC$User, simpleTextViewArr2[i5]));
                                    }
                                } else {
                                    str11 = str9;
                                    if (getMessagesController().isPremiumUser(user)) {
                                        drawable = getPremiumCrossfadeDrawable();
                                        this.nameTextViewRightDrawableContentDescription = LocaleController.getString("AccDescrPremium", 2131624044);
                                        tLRPC$User = user;
                                        this.nameTextView[i5].setLeftDrawable(lockIconDrawable);
                                        this.nameTextView[i5].setRightDrawable(drawable);
                                        if (i5 == 0) {
                                        }
                                        if (tLRPC$User.self) {
                                            SimpleTextView[] simpleTextViewArr22 = this.nameTextView;
                                            simpleTextViewArr22[i5].setRightDrawableOnClick(new ProfileActivity$$ExternalSyntheticLambda14(this, tLRPC$User, simpleTextViewArr22[i5]));
                                        }
                                    } else {
                                        MessagesController messagesController = getMessagesController();
                                        tLRPC$User2 = user;
                                        long j3 = this.dialogId;
                                        if (j3 == 0) {
                                            j3 = this.userId;
                                        }
                                        if (messagesController.isDialogMuted(j3)) {
                                            drawable3 = getThemedDrawable(str13);
                                            this.nameTextViewRightDrawableContentDescription = LocaleController.getString("NotificationsMuted", 2131627045);
                                        } else {
                                            this.nameTextViewRightDrawableContentDescription = null;
                                            tLRPC$User = tLRPC$User2;
                                            z5 = false;
                                            drawable = null;
                                            this.nameTextView[i5].setLeftDrawable(lockIconDrawable);
                                            this.nameTextView[i5].setRightDrawable(drawable);
                                            if (i5 == 0) {
                                            }
                                            if (tLRPC$User.self) {
                                            }
                                        }
                                    }
                                }
                                drawable = drawable3;
                                tLRPC$User = tLRPC$User2;
                                z5 = false;
                                this.nameTextView[i5].setLeftDrawable(lockIconDrawable);
                                this.nameTextView[i5].setRightDrawable(drawable);
                                if (i5 == 0) {
                                }
                                if (tLRPC$User.self) {
                                }
                            } else {
                                tLRPC$User = user;
                                str11 = str9;
                                boolean z7 = tLRPC$User.scam;
                                if (z7 || tLRPC$User.fake) {
                                    drawable2 = getScamDrawable(!z7 ? 1 : 0);
                                } else if (tLRPC$User.verified) {
                                    drawable2 = getVerifiedCrossfadeDrawable();
                                } else {
                                    if (getMessagesController().isPremiumUser(tLRPC$User)) {
                                        drawable = getPremiumCrossfadeDrawable();
                                        z5 = true;
                                        this.nameTextView[i5].setLeftDrawable(lockIconDrawable);
                                        this.nameTextView[i5].setRightDrawable(drawable);
                                        if (i5 == 0) {
                                        }
                                        if (tLRPC$User.self) {
                                        }
                                    }
                                    z5 = false;
                                    drawable = null;
                                    this.nameTextView[i5].setLeftDrawable(lockIconDrawable);
                                    this.nameTextView[i5].setRightDrawable(drawable);
                                    if (i5 == 0) {
                                    }
                                    if (tLRPC$User.self) {
                                    }
                                }
                                drawable = drawable2;
                                z5 = false;
                                this.nameTextView[i5].setLeftDrawable(lockIconDrawable);
                                this.nameTextView[i5].setRightDrawable(drawable);
                                if (i5 == 0) {
                                }
                                if (tLRPC$User.self) {
                                }
                            }
                        }
                    }
                    this.nameTextView[i5].setText(userName);
                    if (i5 != 0) {
                    }
                    this.onlineTextView[i5].setText(str10);
                    if (this.currentEncryptedChat == null) {
                    }
                    if (i5 != 0) {
                    }
                }
                i5++;
                user = tLRPC$User;
                str9 = str11;
                i4 = 2;
                z5 = true;
            }
            this.avatarImage.getImageReceiver().setVisible(!PhotoViewer.isShowingImage(tLRPC$FileLocation), false);
        } else if (this.chatId != 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            if (chat != null) {
                this.currentChat = chat;
            } else {
                chat = this.currentChat;
            }
            String str15 = "MegaPublic";
            if (ChatObject.isChannel(chat)) {
                TLRPC$ChatFull tLRPC$ChatFull2 = this.chatInfo;
                if (tLRPC$ChatFull2 != null) {
                    TLRPC$Chat tLRPC$Chat3 = this.currentChat;
                    if (tLRPC$Chat3.megagroup || (tLRPC$ChatFull2.participants_count != 0 && !ChatObject.hasAdminRights(tLRPC$Chat3) && !this.chatInfo.can_view_participants)) {
                        if (this.currentChat.megagroup) {
                            if (this.onlineCount <= 1 || (i3 = this.chatInfo.participants_count) == 0) {
                                charSequence2 = charSequence;
                                int i6 = this.chatInfo.participants_count;
                                if (i6 == 0) {
                                    if (chat.has_geo) {
                                        lowerCase = LocaleController.getString("MegaLocation", 2131626585).toLowerCase();
                                    } else if (!TextUtils.isEmpty(chat.username)) {
                                        lowerCase = LocaleController.getString(str15, 2131626589).toLowerCase();
                                    } else {
                                        lowerCase = LocaleController.getString("MegaPrivate", 2131626586).toLowerCase();
                                    }
                                    str2 = lowerCase;
                                } else {
                                    str8 = LocaleController.formatPluralString("Members", i6, new Object[0]);
                                    str7 = LocaleController.formatPluralStringComma("Members", this.chatInfo.participants_count);
                                }
                            } else {
                                charSequence2 = charSequence;
                                str8 = String.format("%s, %s", LocaleController.formatPluralString("Members", i3, new Object[0]), LocaleController.formatPluralString("OnlineCount", Math.min(this.onlineCount, this.chatInfo.participants_count), new Object[0]));
                                str7 = String.format("%s, %s", LocaleController.formatPluralStringComma("Members", this.chatInfo.participants_count), LocaleController.formatPluralStringComma("OnlineCount", Math.min(this.onlineCount, this.chatInfo.participants_count)));
                            }
                            str = str7;
                            str2 = str8;
                        } else {
                            charSequence2 = charSequence;
                            LocaleController.formatShortNumber(this.chatInfo.participants_count, new int[1]);
                            if (this.currentChat.megagroup) {
                                str2 = LocaleController.formatPluralString("Members", this.chatInfo.participants_count, new Object[0]);
                                str = LocaleController.formatPluralStringComma("Members", this.chatInfo.participants_count);
                            } else {
                                str2 = LocaleController.formatPluralString("Subscribers", this.chatInfo.participants_count, new Object[0]);
                                str = LocaleController.formatPluralStringComma("Subscribers", this.chatInfo.participants_count);
                            }
                        }
                        z3 = false;
                        i = 0;
                        while (i < 2) {
                            SimpleTextView[] simpleTextViewArr3 = this.nameTextView;
                            if (simpleTextViewArr3[i] == null) {
                                str5 = str2;
                                str4 = str;
                                str3 = str15;
                                str6 = str13;
                            } else {
                                CharSequence charSequence4 = chat.title;
                                if (charSequence4 != null) {
                                    try {
                                        str5 = str2;
                                        try {
                                            str4 = str;
                                            try {
                                                charSequence4 = Emoji.replaceEmoji(charSequence4, simpleTextViewArr3[i].getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
                                            } catch (Exception unused2) {
                                            }
                                        } catch (Exception unused3) {
                                            str4 = str;
                                        }
                                    } catch (Exception unused4) {
                                        str5 = str2;
                                        str4 = str;
                                    }
                                    if (this.nameTextView[i].setText(charSequence4)) {
                                        z3 = true;
                                    }
                                } else {
                                    str5 = str2;
                                    str4 = str;
                                }
                                this.nameTextView[i].setLeftDrawable((Drawable) null);
                                if (i != 0) {
                                    boolean z8 = chat.scam;
                                    if (z8 || chat.fake) {
                                        this.nameTextView[i].setRightDrawable(getScamDrawable(!z8 ? 1 : 0));
                                        this.nameTextViewRightDrawableContentDescription = LocaleController.getString("ScamMessage", 2131628080);
                                    } else if (chat.verified) {
                                        this.nameTextView[i].setRightDrawable(getVerifiedCrossfadeDrawable());
                                        this.nameTextViewRightDrawableContentDescription = LocaleController.getString("AccDescrVerified", 2131624102);
                                    } else {
                                        this.nameTextView[i].setRightDrawable((Drawable) null);
                                        this.nameTextViewRightDrawableContentDescription = null;
                                        if (i != 0 && charSequence2 != null) {
                                            this.onlineTextView[i].setText(charSequence2);
                                        } else {
                                            charSequence3 = charSequence2;
                                            tLRPC$Chat = this.currentChat;
                                            if (!tLRPC$Chat.megagroup && this.chatInfo != null && this.onlineCount > 0) {
                                                this.onlineTextView[i].setText(i == 0 ? str5 : str4);
                                            } else {
                                                if (i == 0 && ChatObject.isChannel(tLRPC$Chat) && (tLRPC$ChatFull = this.chatInfo) != null && (i2 = tLRPC$ChatFull.participants_count) != 0) {
                                                    tLRPC$Chat2 = this.currentChat;
                                                    if (!tLRPC$Chat2.megagroup || tLRPC$Chat2.broadcast) {
                                                        int[] iArr = new int[1];
                                                        String formatShortNumber = LocaleController.formatShortNumber(i2, iArr);
                                                        if (!this.currentChat.megagroup) {
                                                            if (this.chatInfo.participants_count == 0) {
                                                                if (chat.has_geo) {
                                                                    this.onlineTextView[i].setText(LocaleController.getString("MegaLocation", 2131626585).toLowerCase());
                                                                } else if (!TextUtils.isEmpty(chat.username)) {
                                                                    this.onlineTextView[i].setText(LocaleController.getString(str15, 2131626589).toLowerCase());
                                                                } else {
                                                                    this.onlineTextView[i].setText(LocaleController.getString("MegaPrivate", 2131626586).toLowerCase());
                                                                }
                                                            } else {
                                                                charSequence2 = charSequence3;
                                                                str3 = str15;
                                                                z4 = z3;
                                                                str6 = str13;
                                                                this.onlineTextView[i].setText(LocaleController.formatPluralString("Members", iArr[0], new Object[0]).replace(String.format("%d", Integer.valueOf(iArr[0])), formatShortNumber));
                                                            }
                                                        } else {
                                                            charSequence2 = charSequence3;
                                                            str3 = str15;
                                                            z4 = z3;
                                                            str6 = str13;
                                                            this.onlineTextView[i].setText(LocaleController.formatPluralString("Subscribers", iArr[0], new Object[0]).replace(String.format("%d", Integer.valueOf(iArr[0])), formatShortNumber));
                                                        }
                                                        z3 = z4;
                                                    }
                                                }
                                                charSequence2 = charSequence3;
                                                str3 = str15;
                                                z4 = z3;
                                                str6 = str13;
                                                this.onlineTextView[i].setText(i == 0 ? str5 : str4);
                                                z3 = z4;
                                            }
                                            charSequence2 = charSequence3;
                                        }
                                        str3 = str15;
                                        z4 = z3;
                                        str6 = str13;
                                        z3 = z4;
                                    }
                                    if (i != 0) {
                                    }
                                    charSequence3 = charSequence2;
                                    tLRPC$Chat = this.currentChat;
                                    if (!tLRPC$Chat.megagroup) {
                                    }
                                    if (i == 0) {
                                        tLRPC$Chat2 = this.currentChat;
                                        if (!tLRPC$Chat2.megagroup) {
                                        }
                                        int[] iArr2 = new int[1];
                                        String formatShortNumber2 = LocaleController.formatShortNumber(i2, iArr2);
                                        if (!this.currentChat.megagroup) {
                                        }
                                        z3 = z4;
                                    }
                                    charSequence2 = charSequence3;
                                    str3 = str15;
                                    z4 = z3;
                                    str6 = str13;
                                    this.onlineTextView[i].setText(i == 0 ? str5 : str4);
                                    z3 = z4;
                                } else {
                                    boolean z9 = chat.scam;
                                    if (z9 || chat.fake) {
                                        this.nameTextView[i].setRightDrawable(getScamDrawable(!z9 ? 1 : 0));
                                    } else if (chat.verified) {
                                        this.nameTextView[i].setRightDrawable(getVerifiedCrossfadeDrawable());
                                    } else if (getMessagesController().isDialogMuted(-this.chatId)) {
                                        this.nameTextView[i].setRightDrawable(getThemedDrawable(str13));
                                    } else {
                                        this.nameTextView[i].setRightDrawable((Drawable) null);
                                    }
                                    if (i != 0) {
                                    }
                                    charSequence3 = charSequence2;
                                    tLRPC$Chat = this.currentChat;
                                    if (!tLRPC$Chat.megagroup) {
                                    }
                                    if (i == 0) {
                                    }
                                    charSequence2 = charSequence3;
                                    str3 = str15;
                                    z4 = z3;
                                    str6 = str13;
                                    this.onlineTextView[i].setText(i == 0 ? str5 : str4);
                                    z3 = z4;
                                }
                            }
                            i++;
                            str13 = str6;
                            str2 = str5;
                            str = str4;
                            str15 = str3;
                        }
                        if (z3) {
                            needLayout(true);
                        }
                        TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                        TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$ChatPhoto == null ? tLRPC$ChatPhoto.photo_big : null;
                        this.avatarDrawable.setInfo(chat);
                        forUserOrChat = ImageLocation.getForUserOrChat(chat, 0);
                        ImageLocation forUserOrChat5 = ImageLocation.getForUserOrChat(chat, 1);
                        ImageLocation currentVideoLocation2 = this.avatarsViewPager.getCurrentVideoLocation(forUserOrChat5, forUserOrChat);
                        boolean initIfEmpty = this.avatarsViewPager.initIfEmpty(forUserOrChat, forUserOrChat5, z);
                        if ((forUserOrChat != null || initIfEmpty) && this.isPulledDown && (findViewByPosition = this.layoutManager.findViewByPosition(0)) != null) {
                            this.listView.smoothScrollBy(0, findViewByPosition.getTop() - AndroidUtilities.dp(88.0f), CubicBezierInterpolator.EASE_OUT_QUINT);
                        }
                        String str16 = (currentVideoLocation2 == null && currentVideoLocation2.imageType == 2) ? "g" : null;
                        if (this.avatarBig == null) {
                            this.avatarImage.setImage(currentVideoLocation2, str16, forUserOrChat5, "50_50", this.avatarDrawable, chat);
                        }
                        if (forUserOrChat != null && ((imageLocation = this.prevLoadedImageLocation) == null || forUserOrChat.photoId != imageLocation.photoId)) {
                            this.prevLoadedImageLocation = forUserOrChat;
                            getFileLoader().loadFile(forUserOrChat, chat, null, 0, 1);
                        }
                        z2 = true;
                        this.avatarImage.getImageReceiver().setVisible(!PhotoViewer.isShowingImage(tLRPC$FileLocation2), false);
                        if (this.qrItem == null) {
                            return;
                        }
                        updateQrItemVisibility(z2);
                        return;
                    }
                }
                charSequence2 = charSequence;
                if (this.currentChat.megagroup) {
                    str2 = LocaleController.getString("Loading", 2131626473).toLowerCase();
                } else if ((chat.flags & 64) != 0) {
                    str2 = LocaleController.getString("ChannelPublic", 2131624949).toLowerCase();
                } else {
                    str2 = LocaleController.getString("ChannelPrivate", 2131624946).toLowerCase();
                }
            } else {
                charSequence2 = charSequence;
                if (ChatObject.isKickedFromChat(chat)) {
                    str2 = LocaleController.getString("YouWereKicked", 2131629273);
                } else if (ChatObject.isLeftFromChat(chat)) {
                    str2 = LocaleController.getString("YouLeft", 2131629270);
                } else {
                    int i7 = chat.participants_count;
                    TLRPC$ChatFull tLRPC$ChatFull3 = this.chatInfo;
                    if (tLRPC$ChatFull3 != null) {
                        i7 = tLRPC$ChatFull3.participants.participants.size();
                    }
                    str2 = (i7 == 0 || this.onlineCount <= 1) ? LocaleController.formatPluralString("Members", i7, new Object[0]) : String.format("%s, %s", LocaleController.formatPluralString("Members", i7, new Object[0]), LocaleController.formatPluralString("OnlineCount", this.onlineCount, new Object[0]));
                }
            }
            str = str2;
            z3 = false;
            i = 0;
            while (i < 2) {
            }
            if (z3) {
            }
            TLRPC$ChatPhoto tLRPC$ChatPhoto2 = chat.photo;
            if (tLRPC$ChatPhoto2 == null) {
            }
            this.avatarDrawable.setInfo(chat);
            forUserOrChat = ImageLocation.getForUserOrChat(chat, 0);
            ImageLocation forUserOrChat52 = ImageLocation.getForUserOrChat(chat, 1);
            ImageLocation currentVideoLocation22 = this.avatarsViewPager.getCurrentVideoLocation(forUserOrChat52, forUserOrChat);
            boolean initIfEmpty2 = this.avatarsViewPager.initIfEmpty(forUserOrChat, forUserOrChat52, z);
            if (forUserOrChat != null) {
            }
            this.listView.smoothScrollBy(0, findViewByPosition.getTop() - AndroidUtilities.dp(88.0f), CubicBezierInterpolator.EASE_OUT_QUINT);
            if (currentVideoLocation22 == null) {
            }
            if (this.avatarBig == null) {
            }
            if (forUserOrChat != null) {
                this.prevLoadedImageLocation = forUserOrChat;
                getFileLoader().loadFile(forUserOrChat, chat, null, 0, 1);
            }
            z2 = true;
            this.avatarImage.getImageReceiver().setVisible(!PhotoViewer.isShowingImage(tLRPC$FileLocation2), false);
            if (this.qrItem == null) {
            }
        }
        z2 = true;
        if (this.qrItem == null) {
        }
    }

    public /* synthetic */ void lambda$updateProfileData$32(TLRPC$User tLRPC$User, SimpleTextView simpleTextView, View view) {
        PremiumPreviewBottomSheet premiumPreviewBottomSheet = new PremiumPreviewBottomSheet(this, this.currentAccount, tLRPC$User);
        simpleTextView.getLocationOnScreen(new int[2]);
        premiumPreviewBottomSheet.startEnterFromX = simpleTextView.rightDrawableX;
        premiumPreviewBottomSheet.startEnterFromY = simpleTextView.rightDrawableY;
        premiumPreviewBottomSheet.startEnterFromScale = simpleTextView.getScaleX();
        premiumPreviewBottomSheet.startEnterFromX1 = simpleTextView.getLeft();
        premiumPreviewBottomSheet.startEnterFromY1 = simpleTextView.getTop();
        premiumPreviewBottomSheet.startEnterFromView = simpleTextView;
        showDialog(premiumPreviewBottomSheet);
    }

    /* JADX WARN: Removed duplicated region for block: B:169:0x0387  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x03dd  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0414  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x04e1  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x04f8  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x050f  */
    /* JADX WARN: Removed duplicated region for block: B:229:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createActionBarMenu(boolean z) {
        PagerIndicatorView pagerIndicatorView;
        SharedMediaLayout sharedMediaLayout;
        String str;
        int i;
        String str2;
        int i2;
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
                this.otherItem.addSubItem(30, 2131165714, LocaleController.getString("EditName", 2131625587));
                if (this.imageUpdater == null) {
                    this.otherItem.addSubItem(36, 2131165633, LocaleController.getString("AddPhoto", 2131624278));
                    this.otherItem.addSubItem(33, 2131165832, LocaleController.getString("SetAsMain", 2131628240));
                    this.otherItem.addSubItem(21, 2131165744, LocaleController.getString("SaveToGallery", 2131628069));
                    this.otherItem.addSubItem(35, 2131165702, LocaleController.getString("Delete", 2131625368));
                } else {
                    this.otherItem.addSubItem(21, 2131165744, LocaleController.getString("SaveToGallery", 2131628069));
                }
                if (getMessagesController().isChatNoForwards(this.currentChat)) {
                    this.otherItem.hideSubItem(21);
                }
                if (z2) {
                    this.otherItem.addSubItem(31, 2131165774, LocaleController.getString("LogOut", 2131626498));
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
                    return;
                }
                sharedMediaLayout.getSearchItem().requestLayout();
                return;
            }
            TLRPC$UserFull tLRPC$UserFull = this.userInfo;
            if (tLRPC$UserFull != null && tLRPC$UserFull.phone_calls_available) {
                this.callItemVisible = true;
                this.videoCallItemVisible = Build.VERSION.SDK_INT >= 18 && tLRPC$UserFull.video_calls_available;
            }
            int i3 = 2131165651;
            if (this.isBot || getContactsController().contactsDict.get(Long.valueOf(this.userId)) == null) {
                if (MessagesController.isSupportUser(user)) {
                    if (this.userBlocked) {
                        this.otherItem.addSubItem(2, 2131165651, LocaleController.getString("Unblock", 2131628725));
                    }
                } else {
                    if (this.currentEncryptedChat == null) {
                        createAutoDeleteItem(context);
                    }
                    if (this.isBot) {
                        this.otherItem.addSubItem(10, 2131165937, LocaleController.getString("BotShare", 2131624734));
                    } else {
                        this.otherItem.addSubItem(1, 2131165631, LocaleController.getString("AddContact", 2131624259));
                    }
                    if (!TextUtils.isEmpty(user.phone)) {
                        this.otherItem.addSubItem(3, 2131165937, LocaleController.getString("ShareContact", 2131628272));
                    }
                    if (this.isBot) {
                        ActionBarMenuItem actionBarMenuItem = this.otherItem;
                        boolean z3 = this.userBlocked;
                        if (z3) {
                            i3 = 2131165907;
                        }
                        if (!z3) {
                            i2 = 2131624738;
                            str2 = "BotStop";
                        } else {
                            i2 = 2131624730;
                            str2 = "BotRestart";
                        }
                        actionBarMenuItem.addSubItem(2, i3, LocaleController.getString(str2, i2));
                    } else {
                        this.otherItem.addSubItem(2, 2131165651, !this.userBlocked ? LocaleController.getString("BlockContact", 2131624678) : LocaleController.getString("Unblock", 2131628725));
                    }
                }
            } else {
                if (this.currentEncryptedChat == null) {
                    createAutoDeleteItem(context);
                }
                if (!TextUtils.isEmpty(user.phone)) {
                    this.otherItem.addSubItem(3, 2131165937, LocaleController.getString("ShareContact", 2131628272));
                }
                this.otherItem.addSubItem(2, 2131165651, !this.userBlocked ? LocaleController.getString("BlockContact", 2131624678) : LocaleController.getString("Unblock", 2131628725));
                this.otherItem.addSubItem(4, 2131165714, LocaleController.getString("EditContact", 2131625574));
                this.otherItem.addSubItem(5, 2131165702, LocaleController.getString("DeleteContact", 2131625398));
            }
            if (!UserObject.isDeleted(user) && !this.isBot && this.currentEncryptedChat == null && !this.userBlocked) {
                long j = this.userId;
                if (j != 333000 && j != 777000 && j != 42777) {
                    this.otherItem.addSubItem(20, 2131165923, LocaleController.getString("StartEncryptedChat", 2131628416));
                }
            }
            this.otherItem.addSubItem(14, 2131165761, LocaleController.getString("AddShortcut", 2131624281));
        } else if (this.chatId != 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            this.hasVoiceChatItem = false;
            if (ChatObject.canUserDoAdminAction(chat, 13)) {
                createAutoDeleteItem(context);
            }
            if (ChatObject.isChannel(chat)) {
                if (ChatObject.hasAdminRights(chat) || (chat.megagroup && ChatObject.canChangeChatInfo(chat))) {
                    this.editItemVisible = true;
                }
                if (this.chatInfo != null) {
                    if (ChatObject.canManageCalls(chat) && this.chatInfo.call == null) {
                        ActionBarMenuItem actionBarMenuItem2 = this.otherItem;
                        if (!chat.megagroup || chat.gigagroup) {
                            i = 2131628423;
                            str = "StartVoipChannel";
                        } else {
                            i = 2131628426;
                            str = "StartVoipChat";
                        }
                        actionBarMenuItem2.addSubItem(15, 2131165984, LocaleController.getString(str, i));
                        this.hasVoiceChatItem = true;
                    }
                    if (this.chatInfo.can_view_stats) {
                        this.otherItem.addSubItem(19, 2131165947, LocaleController.getString("Statistics", 2131628437));
                    }
                    this.callItemVisible = getMessagesController().getGroupCall(this.chatId, false) != null;
                }
                if (chat.megagroup) {
                    this.canSearchMembers = true;
                    this.otherItem.addSubItem(17, 2131165922, LocaleController.getString("SearchMembers", 2131628118));
                    if (!chat.creator && !chat.left && !chat.kicked) {
                        this.otherItem.addSubItem(7, 2131165774, LocaleController.getString("LeaveMegaMenu", 2131626395));
                    }
                } else {
                    if (!TextUtils.isEmpty(chat.username)) {
                        this.otherItem.addSubItem(10, 2131165937, LocaleController.getString("BotShare", 2131624734));
                    }
                    TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
                    if (tLRPC$ChatFull != null && tLRPC$ChatFull.linked_chat_id != 0) {
                        this.otherItem.addSubItem(22, 2131165708, LocaleController.getString("ViewDiscussion", 2131628914));
                    }
                    TLRPC$Chat tLRPC$Chat = this.currentChat;
                    if (!tLRPC$Chat.creator && !tLRPC$Chat.left && !tLRPC$Chat.kicked) {
                        this.otherItem.addSubItem(7, 2131165774, LocaleController.getString("LeaveChannelMenu", 2131626387));
                    }
                }
            } else {
                if (this.chatInfo != null) {
                    if (ChatObject.canManageCalls(chat) && this.chatInfo.call == null) {
                        this.otherItem.addSubItem(15, 2131165984, LocaleController.getString("StartVoipChat", 2131628426));
                        this.hasVoiceChatItem = true;
                    }
                    this.callItemVisible = getMessagesController().getGroupCall(this.chatId, false) != null;
                }
                if (ChatObject.canChangeChatInfo(chat)) {
                    this.editItemVisible = true;
                }
                if (!ChatObject.isKickedFromChat(chat) && !ChatObject.isLeftFromChat(chat)) {
                    this.canSearchMembers = true;
                    this.otherItem.addSubItem(17, 2131165922, LocaleController.getString("SearchMembers", 2131628118));
                }
                this.otherItem.addSubItem(7, 2131165774, LocaleController.getString("DeleteAndExit", 2131625382));
            }
            this.otherItem.addSubItem(14, 2131165761, LocaleController.getString("AddShortcut", 2131624281));
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
    }

    /* renamed from: org.telegram.ui.ProfileActivity$35 */
    /* loaded from: classes3.dex */
    public class AnonymousClass35 implements AutoDeletePopupWrapper.Callback {
        AnonymousClass35() {
            ProfileActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
        public void dismiss() {
            ProfileActivity.this.otherItem.toggleSubMenu();
        }

        @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
        public void setAutoDeleteHistory(int i, int i2) {
            ProfileActivity.this.setAutoDeleteHistory(i, i2);
        }
    }

    private void createAutoDeleteItem(Context context) {
        int i;
        this.autoDeletePopupWrapper = new AutoDeletePopupWrapper(context, this.otherItem.getPopupLayout().getSwipeBack(), new AnonymousClass35(), false, this.resourcesProvider);
        TLRPC$UserFull tLRPC$UserFull = this.userInfo;
        if (tLRPC$UserFull == null && this.chatInfo == null) {
            i = 0;
        } else {
            i = tLRPC$UserFull != null ? tLRPC$UserFull.ttl_period : this.chatInfo.ttl_period;
        }
        TimerDrawable ttlIcon = TimerDrawable.getTtlIcon(i);
        this.autoDeleteItemDrawable = ttlIcon;
        this.autoDeleteItem = this.otherItem.addSwipeBackItem(0, ttlIcon, LocaleController.getString("AutoDeletePopupTitle", 2131624558), this.autoDeletePopupWrapper.windowLayout);
        this.otherItem.addColoredGap();
        updateAutoDeleteItem();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getThemedColor(String str) {
        return Theme.getColor(str, this.resourcesProvider);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Drawable getThemedDrawable(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Drawable drawable = resourcesProvider != null ? resourcesProvider.getDrawable(str) : null;
        return drawable != null ? drawable : super.getThemedDrawable(str);
    }

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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            recyclerListView.invalidateViews();
        }
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList<Long> arrayList, CharSequence charSequence, boolean z) {
        long longValue = arrayList.get(0).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
        } else if (DialogObject.isUserDialog(longValue)) {
            bundle.putLong("user_id", longValue);
        } else if (DialogObject.isChatDialog(longValue)) {
            bundle.putLong("chat_id", -longValue);
        }
        if (!getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
            return;
        }
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.closeChats;
        notificationCenter.removeObserver(this, i);
        getNotificationCenter().postNotificationName(i, new Object[0]);
        presentFragment(new ChatActivity(bundle), true);
        removeSelfFromStack();
        getSendMessagesHelper().sendMessage(getMessagesController().getUser(Long.valueOf(this.userId)), longValue, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, true, 0);
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        SendMessagesHelper.prepareSendingText(AccountInstance.getInstance(this.currentAccount), charSequence.toString(), longValue, true, 0);
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

    public Animator searchExpandTransition(boolean z) {
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
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        float f = this.extraHeight;
        this.searchListView.setTranslationY(f);
        this.searchListView.setVisibility(0);
        this.searchItem.setVisibility(0);
        this.listView.setVisibility(0);
        needLayout(true);
        this.avatarContainer.setVisibility(0);
        this.nameTextView[1].setVisibility(0);
        this.onlineTextView[1].setVisibility(0);
        this.actionBar.onSearchFieldVisibilityChanged(this.searchTransitionProgress > 0.5f);
        int i = 8;
        int i2 = this.searchTransitionProgress > 0.5f ? 0 : 8;
        ActionBarMenuItem actionBarMenuItem = this.otherItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setVisibility(i2);
        }
        if (this.qrItem != null) {
            updateQrItemVisibility(false);
        }
        this.searchItem.setVisibility(i2);
        FrameLayout searchContainer = this.searchItem.getSearchContainer();
        if (this.searchTransitionProgress <= 0.5f) {
            i = 0;
        }
        searchContainer.setVisibility(i);
        this.searchListView.setEmptyView(this.emptyView);
        this.avatarContainer.setClickable(false);
        ofFloat.addUpdateListener(new ProfileActivity$$ExternalSyntheticLambda3(this, ofFloat, f, z));
        ofFloat.addListener(new AnonymousClass36(z));
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

    public /* synthetic */ void lambda$searchExpandTransition$33(ValueAnimator valueAnimator, float f, boolean z, ValueAnimator valueAnimator2) {
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
        boolean z2 = true;
        needLayout(true);
        this.listView.setAlpha(f2);
        this.searchListView.setAlpha(1.0f - this.searchTransitionProgress);
        this.searchListView.setScaleX((this.searchTransitionProgress * 0.05f) + 1.0f);
        this.searchListView.setScaleY((this.searchTransitionProgress * 0.05f) + 1.0f);
        this.emptyView.setAlpha(1.0f - f2);
        this.avatarContainer.setAlpha(f2);
        this.nameTextView[1].setAlpha(f2);
        this.onlineTextView[1].setAlpha(f2);
        this.searchItem.getSearchField().setAlpha(f3);
        if (z && this.searchTransitionProgress < 0.7f) {
            this.searchItem.requestFocusOnSearchView();
        }
        int i = 8;
        this.searchItem.getSearchContainer().setVisibility(this.searchTransitionProgress < 0.5f ? 0 : 8);
        if (this.searchTransitionProgress > 0.5f) {
            i = 0;
        }
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
        ActionBar actionBar = this.actionBar;
        if (this.searchTransitionProgress >= 0.5f) {
            z2 = false;
        }
        actionBar.onSearchFieldVisibilityChanged(z2);
        ActionBarMenuItem actionBarMenuItem3 = this.otherItem;
        if (actionBarMenuItem3 != null) {
            actionBarMenuItem3.setAlpha(f2);
        }
        this.searchItem.setAlpha(f2);
        this.topView.invalidate();
        this.fragmentView.invalidate();
    }

    /* renamed from: org.telegram.ui.ProfileActivity$36 */
    /* loaded from: classes3.dex */
    public class AnonymousClass36 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$enter;

        AnonymousClass36(boolean z) {
            ProfileActivity.this = r1;
            this.val$enter = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ProfileActivity.this.updateSearchViewState(this.val$enter);
            ProfileActivity.this.avatarContainer.setClickable(true);
            if (this.val$enter) {
                ProfileActivity.this.searchItem.requestFocusOnSearchView();
            }
            ProfileActivity.this.needLayout(true);
            ProfileActivity.this.searchViewTransition = null;
            ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
            if (this.val$enter) {
                ProfileActivity.this.invalidateScroll = true;
                ProfileActivity.this.saveScrollPosition();
                AndroidUtilities.requestAdjustResize(ProfileActivity.this.getParentActivity(), ((BaseFragment) ProfileActivity.this).classGuid);
                ProfileActivity.this.emptyView.setPreventMoving(false);
            }
        }
    }

    public void updateSearchViewState(boolean z) {
        int i = 0;
        int i2 = z ? 8 : 0;
        this.listView.setVisibility(i2);
        this.searchListView.setVisibility(z ? 0 : 8);
        this.searchItem.getSearchContainer().setVisibility(z ? 0 : 8);
        this.actionBar.onSearchFieldVisibilityChanged(z);
        this.avatarContainer.setVisibility(i2);
        this.nameTextView[1].setVisibility(i2);
        this.onlineTextView[1].setVisibility(i2);
        ActionBarMenuItem actionBarMenuItem = this.otherItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setAlpha(1.0f);
            this.otherItem.setVisibility(i2);
        }
        ActionBarMenuItem actionBarMenuItem2 = this.qrItem;
        if (actionBarMenuItem2 != null) {
            actionBarMenuItem2.setAlpha(1.0f);
            ActionBarMenuItem actionBarMenuItem3 = this.qrItem;
            if (z || !isQrNeedVisible()) {
                i = 8;
            }
            actionBarMenuItem3.setVisibility(i);
        }
        this.searchItem.setVisibility(i2);
        this.avatarContainer.setAlpha(1.0f);
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
    public void didUploadPhoto(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda22(this, tLRPC$InputFile, tLRPC$InputFile2, d, str, tLRPC$PhotoSize2, tLRPC$PhotoSize));
    }

    public /* synthetic */ void lambda$didUploadPhoto$35(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda23(this, tLRPC$TL_error, tLObject, str));
    }

    public /* synthetic */ void lambda$didUploadPhoto$34(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str) {
        this.avatarsViewPager.removeUploadingImage(this.uploadingImageLocation);
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
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 150);
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, 800);
            TLRPC$VideoSize tLRPC$VideoSize = tLRPC$TL_photos_photo.photo.video_sizes.isEmpty() ? null : tLRPC$TL_photos_photo.photo.video_sizes.get(0);
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
            if (closestPhotoSizeWithSize2 != null && this.avatarBig != null) {
                FileLoader.getInstance(this.currentAccount).getPathToAttach(this.avatarBig, true).renameTo(FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize2, true));
            }
            if (tLRPC$VideoSize != null && str != null) {
                new File(str).renameTo(FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$VideoSize, "mp4", true));
            }
            getMessagesStorage().clearUserPhotos(user.id);
            ArrayList<TLRPC$User> arrayList2 = new ArrayList<>();
            arrayList2.add(user);
            getMessagesStorage().putUsersAndChats(arrayList2, null, false, true);
        }
        this.allowPullingDown = !AndroidUtilities.isTablet() && !this.isInLandscapeMode && this.avatarImage.getImageReceiver().hasNotThumb() && !AndroidUtilities.isAccessibilityScreenReaderEnabled();
        this.avatar = null;
        this.avatarBig = null;
        this.avatarsViewPager.setCreateThumbFromParent(false);
        updateProfileData(true);
        showAvatarProgress(false, true);
        getNotificationCenter().postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_ALL));
        getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
        getUserConfig().saveConfig(true);
    }

    public /* synthetic */ void lambda$didUploadPhoto$36(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        if (tLRPC$InputFile != null || tLRPC$InputFile2 != null) {
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
            getConnectionsManager().sendRequest(tLRPC$TL_photos_uploadProfilePhoto, new ProfileActivity$$ExternalSyntheticLambda34(this, str));
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

    private void showAvatarProgress(boolean z, boolean z2) {
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
        this.avatarAnimation.addListener(new AnonymousClass37(z));
        this.avatarAnimation.start();
    }

    /* renamed from: org.telegram.ui.ProfileActivity$37 */
    /* loaded from: classes3.dex */
    public class AnonymousClass37 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass37(boolean z) {
            ProfileActivity.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ProfileActivity.this.avatarAnimation == null || ProfileActivity.this.avatarProgressView == null) {
                return;
            }
            if (!this.val$show) {
                ProfileActivity.this.avatarProgressView.setVisibility(4);
            }
            ProfileActivity.this.avatarAnimation = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ProfileActivity.this.avatarAnimation = null;
        }
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

    private void sendLogs(boolean z) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        Utilities.globalQueue.postRunnable(new ProfileActivity$$ExternalSyntheticLambda26(this, z, alertDialog));
    }

    public /* synthetic */ void lambda$sendLogs$38(boolean z, AlertDialog alertDialog) {
        Exception e;
        ZipOutputStream zipOutputStream;
        Throwable th;
        Exception e2;
        try {
            BufferedInputStream bufferedInputStream = null;
            File externalFilesDir = ApplicationLoader.applicationContext.getExternalFilesDir(null);
            File file = new File(externalFilesDir.getAbsolutePath() + "/logs");
            File file2 = new File(file, "logs.zip");
            if (file2.exists()) {
                file2.delete();
            }
            try {
                try {
                    File[] listFiles = file.listFiles();
                    boolean[] zArr = new boolean[1];
                    long currentTimeMillis = System.currentTimeMillis();
                    try {
                        zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file2)));
                        try {
                            byte[] bArr = new byte[65536];
                            for (int i = 0; i < listFiles.length; i++) {
                                if (!z || currentTimeMillis - listFiles[i].lastModified() <= 86400000) {
                                    BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(listFiles[i]), 65536);
                                    try {
                                        zipOutputStream.putNextEntry(new ZipEntry(listFiles[i].getName()));
                                        while (true) {
                                            int read = bufferedInputStream2.read(bArr, 0, 65536);
                                            if (read == -1) {
                                                break;
                                            }
                                            zipOutputStream.write(bArr, 0, read);
                                        }
                                        bufferedInputStream2.close();
                                    } catch (Exception e3) {
                                        e2 = e3;
                                        bufferedInputStream = bufferedInputStream2;
                                        e2.printStackTrace();
                                        if (bufferedInputStream != null) {
                                            bufferedInputStream.close();
                                        }
                                        if (zipOutputStream != null) {
                                            zipOutputStream.close();
                                        }
                                        AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda25(this, alertDialog, zArr, file2));
                                    } catch (Throwable th2) {
                                        th = th2;
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
                        } catch (Exception e4) {
                            e2 = e4;
                        }
                    } catch (Exception e5) {
                        e2 = e5;
                        zipOutputStream = null;
                    } catch (Throwable th3) {
                        th = th3;
                        zipOutputStream = null;
                    }
                    zipOutputStream.close();
                    AndroidUtilities.runOnUIThread(new ProfileActivity$$ExternalSyntheticLambda25(this, alertDialog, zArr, file2));
                } catch (Throwable th4) {
                    th = th4;
                }
            } catch (Exception e6) {
                e = e6;
                e.printStackTrace();
            }
        } catch (Exception e7) {
            e = e7;
        }
    }

    public /* synthetic */ void lambda$sendLogs$37(AlertDialog alertDialog, boolean[] zArr, File file) {
        Uri uri;
        try {
            alertDialog.dismiss();
        } catch (Exception unused) {
        }
        if (zArr[0]) {
            int i = Build.VERSION.SDK_INT;
            if (i >= 24) {
                uri = FileProvider.getUriForFile(getParentActivity(), "org.telegram.messenger.beta.provider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            Intent intent = new Intent("android.intent.action.SEND");
            if (i >= 24) {
                intent.addFlags(1);
            }
            intent.setType("message/rfc822");
            intent.putExtra("android.intent.extra.EMAIL", "");
            intent.putExtra("android.intent.extra.SUBJECT", "Logs from " + LocaleController.getInstance().formatterStats.format(System.currentTimeMillis()));
            intent.putExtra("android.intent.extra.STREAM", uri);
            if (getParentActivity() == null) {
                return;
            }
            try {
                getParentActivity().startActivityForResult(Intent.createChooser(intent, "Select email application."), 500);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (getParentActivity() == null) {
        } else {
            Toast.makeText(getParentActivity(), LocaleController.getString("ErrorOccurred", 2131625657), 0).show();
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            ProfileActivity.this = r1;
            this.mContext = context;
        }

        /* renamed from: org.telegram.ui.ProfileActivity$ListAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AboutLinkCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
                super(context, baseFragment, resourcesProvider);
                ListAdapter.this = r1;
            }

            @Override // org.telegram.ui.Cells.AboutLinkCell
            protected void didPressUrl(String str) {
                ProfileActivity.this.openUrl(str);
            }

            @Override // org.telegram.ui.Cells.AboutLinkCell
            protected void didResizeEnd() {
                ProfileActivity.this.layoutManager.mIgnoreTopPadding = false;
            }

            @Override // org.telegram.ui.Cells.AboutLinkCell
            protected void didResizeStart() {
                ProfileActivity.this.layoutManager.mIgnoreTopPadding = true;
            }
        }

        /* renamed from: org.telegram.ui.ProfileActivity$ListAdapter$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 extends View {
            AnonymousClass2(ListAdapter listAdapter, Context context) {
                super(context);
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
            }
        }

        /* renamed from: org.telegram.ui.ProfileActivity$ListAdapter$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 extends View {
            private int lastPaddingHeight = 0;
            private int lastListViewHeight = 0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(Context context) {
                super(context);
                ListAdapter.this = r1;
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                int i3 = 0;
                if (this.lastListViewHeight != ProfileActivity.this.listView.getMeasuredHeight()) {
                    this.lastPaddingHeight = 0;
                }
                this.lastListViewHeight = ProfileActivity.this.listView.getMeasuredHeight();
                int childCount = ProfileActivity.this.listView.getChildCount();
                if (childCount == ProfileActivity.this.listAdapter.getItemCount()) {
                    int i4 = 0;
                    for (int i5 = 0; i5 < childCount; i5++) {
                        int childAdapterPosition = ProfileActivity.this.listView.getChildAdapterPosition(ProfileActivity.this.listView.getChildAt(i5));
                        if (childAdapterPosition >= 0 && childAdapterPosition != ProfileActivity.this.bottomPaddingRow) {
                            i4 += ProfileActivity.this.listView.getChildAt(i5).getMeasuredHeight();
                        }
                    }
                    int measuredHeight = ((((BaseFragment) ProfileActivity.this).fragmentView.getMeasuredHeight() - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) - i4;
                    if (measuredHeight > AndroidUtilities.dp(88.0f)) {
                        measuredHeight = 0;
                    }
                    if (measuredHeight > 0) {
                        i3 = measuredHeight;
                    }
                    int measuredWidth = ProfileActivity.this.listView.getMeasuredWidth();
                    this.lastPaddingHeight = i3;
                    setMeasuredDimension(measuredWidth, i3);
                    return;
                }
                setMeasuredDimension(ProfileActivity.this.listView.getMeasuredWidth(), this.lastPaddingHeight);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            String str;
            switch (i) {
                case 1:
                    view = new HeaderCell(this.mContext, 23, ProfileActivity.this.resourcesProvider);
                    break;
                case 2:
                    TextDetailCell textDetailCell = new TextDetailCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    textDetailCell.setContentDescriptionValueFirst(true);
                    textDetailCell.setImageClickListener(new ProfileActivity$ListAdapter$$ExternalSyntheticLambda2(ProfileActivity.this));
                    view = textDetailCell;
                    break;
                case 3:
                    ProfileActivity profileActivity = ProfileActivity.this;
                    Context context = this.mContext;
                    ProfileActivity profileActivity2 = ProfileActivity.this;
                    view = profileActivity.aboutLinkCell = new AnonymousClass1(context, profileActivity2, profileActivity2.resourcesProvider);
                    break;
                case 4:
                    view = new TextCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
                case 5:
                    view = new DividerCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    view.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(4.0f), 0, 0);
                    break;
                case 6:
                    view = new NotificationsCheckCell(this.mContext, 23, 70, false, ProfileActivity.this.resourcesProvider);
                    break;
                case 7:
                    view = new ShadowSectionCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
                case 8:
                    view = new UserCell(this.mContext, ProfileActivity.this.addMemberRow == -1 ? 9 : 6, 0, true, ProfileActivity.this.resourcesProvider);
                    break;
                case 9:
                case 10:
                case 14:
                case 16:
                default:
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext, 10, ProfileActivity.this.resourcesProvider);
                    textInfoPrivacyCell.getTextView().setGravity(1);
                    textInfoPrivacyCell.getTextView().setTextColor(ProfileActivity.this.getThemedColor("windowBackgroundWhiteGrayText3"));
                    textInfoPrivacyCell.getTextView().setMovementMethod(null);
                    try {
                        PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                        int i2 = packageInfo.versionCode;
                        int i3 = i2 / 10;
                        int i4 = i2 % 10;
                        if (i4 == 1 || i4 == 2) {
                            str = "store bundled " + Build.CPU_ABI + " " + Build.CPU_ABI2;
                        } else if (BuildVars.isStandaloneApp()) {
                            str = "direct " + Build.CPU_ABI + " " + Build.CPU_ABI2;
                        } else {
                            str = "universal " + Build.CPU_ABI + " " + Build.CPU_ABI2;
                        }
                        textInfoPrivacyCell.setText(LocaleController.formatString("TelegramVersion", 2131628579, String.format(Locale.US, "v%s (%d) %s", packageInfo.versionName, Integer.valueOf(i3), str)));
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    textInfoPrivacyCell.getTextView().setPadding(0, AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, ProfileActivity.this.getThemedColor("windowBackgroundGrayShadow")));
                    view = textInfoPrivacyCell;
                    break;
                case 11:
                    view = new AnonymousClass2(this, this.mContext);
                    break;
                case 12:
                    view = new AnonymousClass3(this.mContext);
                    view.setBackground(new ColorDrawable(0));
                    break;
                case 13:
                    if (ProfileActivity.this.sharedMediaLayout.getParent() != null) {
                        ((ViewGroup) ProfileActivity.this.sharedMediaLayout.getParent()).removeView(ProfileActivity.this.sharedMediaLayout);
                    }
                    view = ProfileActivity.this.sharedMediaLayout;
                    break;
                case 15:
                    view = new AnonymousClass4(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
                case 17:
                    view = new TextInfoPrivacyCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
                case 18:
                    view = new ProfilePremiumCell(this.mContext, ProfileActivity.this.resourcesProvider);
                    break;
            }
            if (i != 13) {
                view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            }
            return new RecyclerListView.Holder(view);
        }

        /* renamed from: org.telegram.ui.ProfileActivity$ListAdapter$4 */
        /* loaded from: classes3.dex */
        class AnonymousClass4 extends SettingsSuggestionCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass4(Context context, Theme.ResourcesProvider resourcesProvider) {
                super(context, resourcesProvider);
                ListAdapter.this = r1;
            }

            @Override // org.telegram.ui.Cells.SettingsSuggestionCell
            protected void onYesClick(int i) {
                NotificationCenter notificationCenter = ProfileActivity.this.getNotificationCenter();
                ProfileActivity profileActivity = ProfileActivity.this;
                int i2 = NotificationCenter.newSuggestionsAvailable;
                notificationCenter.removeObserver(profileActivity, i2);
                ProfileActivity.this.getMessagesController().removeSuggestion(0L, i == 0 ? "VALIDATE_PHONE_NUMBER" : "VALIDATE_PASSWORD");
                ProfileActivity.this.getNotificationCenter().addObserver(ProfileActivity.this, i2);
                if (i == 0) {
                    int unused = ProfileActivity.this.phoneSuggestionRow;
                } else {
                    int unused2 = ProfileActivity.this.passwordSuggestionRow;
                }
                ProfileActivity.this.updateListAnimated(false);
            }

            @Override // org.telegram.ui.Cells.SettingsSuggestionCell
            protected void onNoClick(int i) {
                if (i == 0) {
                    ProfileActivity.this.presentFragment(new ActionIntroActivity(3));
                } else {
                    ProfileActivity.this.presentFragment(new TwoStepVerificationSetupActivity(8, null));
                }
            }
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

        /* JADX WARN: Removed duplicated region for block: B:124:0x031b  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            String str2;
            String str3;
            String str4;
            TLRPC$User user;
            String str5;
            String str6;
            String str7;
            int i2;
            String string;
            TLRPC$TL_chatChannelParticipant tLRPC$TL_chatChannelParticipant;
            String string2;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            int i3 = 1;
            r7 = true;
            r7 = true;
            r7 = true;
            boolean z2 = true;
            boolean z3 = true;
            boolean z4 = true;
            boolean z5 = true;
            boolean z6 = true;
            boolean z7 = true;
            boolean z8 = true;
            boolean z9 = true;
            boolean z10 = true;
            boolean z11 = true;
            boolean z12 = true;
            boolean z13 = true;
            r7 = true;
            boolean z14 = true;
            if (itemViewType == 1) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == ProfileActivity.this.infoHeaderRow) {
                    if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup && ProfileActivity.this.channelInfoRow != -1) {
                        headerCell.setText(LocaleController.getString("ReportChatDescription", 2131627932));
                        return;
                    } else {
                        headerCell.setText(LocaleController.getString("Info", 2131626233));
                        return;
                    }
                } else if (i != ProfileActivity.this.membersHeaderRow) {
                    if (i != ProfileActivity.this.settingsSectionRow2) {
                        if (i != ProfileActivity.this.numberSectionRow) {
                            if (i != ProfileActivity.this.helpHeaderRow) {
                                if (i != ProfileActivity.this.debugHeaderRow) {
                                    return;
                                }
                                headerCell.setText(LocaleController.getString("SettingsDebug", 2131628261));
                                return;
                            }
                            headerCell.setText(LocaleController.getString("SettingsHelp", 2131628263));
                            return;
                        }
                        headerCell.setText(LocaleController.getString("Account", 2131624127));
                        return;
                    }
                    headerCell.setText(LocaleController.getString("SETTINGS", 2131628057));
                    return;
                } else {
                    headerCell.setText(LocaleController.getString("ChannelMembers", 2131624917));
                    return;
                }
            }
            String str8 = null;
            if (itemViewType == 2) {
                TextDetailCell textDetailCell = (TextDetailCell) viewHolder.itemView;
                if (i == ProfileActivity.this.usernameRow) {
                    Drawable drawable = ContextCompat.getDrawable(textDetailCell.getContext(), 2131165884);
                    drawable.setColorFilter(new PorterDuffColorFilter(ProfileActivity.this.getThemedColor("switch2TrackChecked"), PorterDuff.Mode.MULTIPLY));
                    textDetailCell.setImage(drawable, LocaleController.getString("GetQRCode", 2131626056));
                } else {
                    textDetailCell.setImage(null);
                }
                if (i == ProfileActivity.this.phoneRow) {
                    if (!TextUtils.isEmpty(ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId)).phone)) {
                        str5 = PhoneFormat.getInstance().format("+" + user.phone);
                    } else {
                        str5 = LocaleController.getString("PhoneHidden", 2131627489);
                    }
                    textDetailCell.setTextAndValue(str5, LocaleController.getString("PhoneMobile", 2131627492), false);
                } else if (i == ProfileActivity.this.usernameRow) {
                    if (ProfileActivity.this.userId != 0) {
                        TLRPC$User user2 = ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userId));
                        if (user2 == null || TextUtils.isEmpty(user2.username)) {
                            str4 = "-";
                        } else {
                            str4 = "@" + user2.username;
                        }
                        textDetailCell.setTextAndValue(str4, LocaleController.getString("Username", 2131628843), false);
                    } else if (ProfileActivity.this.currentChat != null) {
                        TLRPC$Chat chat = ProfileActivity.this.getMessagesController().getChat(Long.valueOf(ProfileActivity.this.chatId));
                        textDetailCell.setTextAndValue(ProfileActivity.this.getMessagesController().linkPrefix + "/" + chat.username, LocaleController.getString("InviteLink", 2131626263), false);
                    }
                } else if (i == ProfileActivity.this.locationRow) {
                    if (ProfileActivity.this.chatInfo != null && (ProfileActivity.this.chatInfo.location instanceof TLRPC$TL_channelLocation)) {
                        textDetailCell.setTextAndValue(((TLRPC$TL_channelLocation) ProfileActivity.this.chatInfo.location).address, LocaleController.getString("AttachLocation", 2131624492), false);
                    }
                } else if (i == ProfileActivity.this.numberRow) {
                    TLRPC$User currentUser = UserConfig.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getCurrentUser();
                    if (currentUser != null && (str3 = currentUser.phone) != null && str3.length() != 0) {
                        str2 = PhoneFormat.getInstance().format("+" + currentUser.phone);
                    } else {
                        str2 = LocaleController.getString("NumberUnknown", 2131627074);
                    }
                    textDetailCell.setTextAndValue(str2, LocaleController.getString("TapToChangePhone", 2131628546), true);
                    textDetailCell.setContentDescriptionValueFirst(false);
                } else if (i == ProfileActivity.this.setUsernameRow) {
                    TLRPC$User currentUser2 = UserConfig.getInstance(((BaseFragment) ProfileActivity.this).currentAccount).getCurrentUser();
                    if (currentUser2 != null && !TextUtils.isEmpty(currentUser2.username)) {
                        str = "@" + currentUser2.username;
                    } else {
                        str = LocaleController.getString("UsernameEmpty", 2131628847);
                    }
                    textDetailCell.setTextAndValue(str, LocaleController.getString("Username", 2131628843), true);
                    textDetailCell.setContentDescriptionValueFirst(true);
                }
                textDetailCell.setTag(Integer.valueOf(i));
            } else if (itemViewType == 3) {
                AboutLinkCell aboutLinkCell = (AboutLinkCell) viewHolder.itemView;
                if (i == ProfileActivity.this.userInfoRow) {
                    TLRPC$User user3 = ProfileActivity.this.userInfo.user != null ? ProfileActivity.this.userInfo.user : ProfileActivity.this.getMessagesController().getUser(Long.valueOf(ProfileActivity.this.userInfo.id));
                    if (!ProfileActivity.this.isBot && (user3 == null || !user3.premium || ProfileActivity.this.userInfo.about == null)) {
                        z14 = false;
                    }
                    aboutLinkCell.setTextAndValue(ProfileActivity.this.userInfo.about, LocaleController.getString("UserBio", 2131628802), z14);
                } else if (i == ProfileActivity.this.channelInfoRow) {
                    String str9 = ProfileActivity.this.chatInfo.about;
                    while (str9.contains("\n\n\n")) {
                        str9 = str9.replace("\n\n\n", "\n\n");
                    }
                    if (!ChatObject.isChannel(ProfileActivity.this.currentChat) || ProfileActivity.this.currentChat.megagroup) {
                        z = false;
                    }
                    aboutLinkCell.setText(str9, z);
                } else if (i == ProfileActivity.this.bioRow) {
                    if (ProfileActivity.this.userInfo == null || !TextUtils.isEmpty(ProfileActivity.this.userInfo.about)) {
                        aboutLinkCell.setTextAndValue(ProfileActivity.this.userInfo == null ? LocaleController.getString("Loading", 2131626473) : ProfileActivity.this.userInfo.about, LocaleController.getString("UserBio", 2131628802), ProfileActivity.this.getUserConfig().isPremium());
                        ProfileActivity profileActivity = ProfileActivity.this;
                        if (profileActivity.userInfo != null) {
                            str8 = ProfileActivity.this.userInfo.about;
                        }
                        profileActivity.currentBio = str8;
                    } else {
                        aboutLinkCell.setTextAndValue(LocaleController.getString("UserBio", 2131628802), LocaleController.getString("UserBioDetail", 2131628803), false);
                        ProfileActivity.this.currentBio = null;
                    }
                    aboutLinkCell.setMoreButtonDisabled(true);
                }
                if (i == ProfileActivity.this.bioRow) {
                    aboutLinkCell.setOnClickListener(new ProfileActivity$ListAdapter$$ExternalSyntheticLambda0(this));
                } else {
                    aboutLinkCell.setOnClickListener(new ProfileActivity$ListAdapter$$ExternalSyntheticLambda1(this, i, aboutLinkCell));
                }
            } else {
                if (itemViewType != 4) {
                    if (itemViewType == 6) {
                        NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                        if (i != ProfileActivity.this.notificationsRow) {
                            return;
                        }
                        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileActivity.this).currentAccount);
                        long j = ProfileActivity.this.dialogId != 0 ? ProfileActivity.this.dialogId : ProfileActivity.this.userId != 0 ? ProfileActivity.this.userId : -ProfileActivity.this.chatId;
                        boolean z15 = notificationsSettings.getBoolean("custom_" + j, false);
                        boolean contains = notificationsSettings.contains("notify2_" + j);
                        int i4 = notificationsSettings.getInt("notify2_" + j, 0);
                        int i5 = notificationsSettings.getInt("notifyuntil_" + j, 0);
                        if (i4 == 3 && i5 != Integer.MAX_VALUE) {
                            int currentTime = i5 - ProfileActivity.this.getConnectionsManager().getCurrentTime();
                            if (currentTime <= 0) {
                                if (z15) {
                                    string = LocaleController.getString("NotificationsCustom", 2131627022);
                                } else {
                                    string = LocaleController.getString("NotificationsOn", 2131627051);
                                }
                                str8 = string;
                                if (str8 == null) {
                                }
                                notificationsCheckCell.setAnimationsEnabled(ProfileActivity.this.fragmentOpened);
                                notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("Notifications", 2131627016), str8, z2, false);
                                return;
                            }
                            if (currentTime < 3600) {
                                str8 = LocaleController.formatString("WillUnmuteIn", 2131629249, LocaleController.formatPluralString("Minutes", currentTime / 60, new Object[0]));
                            } else if (currentTime < 86400) {
                                str8 = LocaleController.formatString("WillUnmuteIn", 2131629249, LocaleController.formatPluralString("Hours", (int) Math.ceil((currentTime / 60.0f) / 60.0f), new Object[0]));
                            } else if (currentTime < 31536000) {
                                str8 = LocaleController.formatString("WillUnmuteIn", 2131629249, LocaleController.formatPluralString("Days", (int) Math.ceil(((currentTime / 60.0f) / 60.0f) / 24.0f), new Object[0]));
                            }
                            z2 = false;
                            if (str8 == null) {
                            }
                            notificationsCheckCell.setAnimationsEnabled(ProfileActivity.this.fragmentOpened);
                            notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("Notifications", 2131627016), str8, z2, false);
                            return;
                        }
                        if (i4 == 0) {
                            if (!contains) {
                                z2 = ProfileActivity.this.getNotificationsController().isGlobalNotificationsEnabled(j);
                            }
                        } else if (i4 != 1) {
                            z2 = false;
                        }
                        if (z2 && z15) {
                            str8 = LocaleController.getString("NotificationsCustom", 2131627022);
                            if (str8 == null) {
                            }
                            notificationsCheckCell.setAnimationsEnabled(ProfileActivity.this.fragmentOpened);
                            notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("Notifications", 2131627016), str8, z2, false);
                            return;
                        }
                        if (z2) {
                            i2 = 2131627051;
                            str7 = "NotificationsOn";
                        } else {
                            i2 = 2131627049;
                            str7 = "NotificationsOff";
                        }
                        string = LocaleController.getString(str7, i2);
                        str8 = string;
                        if (str8 == null) {
                            str8 = LocaleController.getString("NotificationsOff", 2131627049);
                        }
                        notificationsCheckCell.setAnimationsEnabled(ProfileActivity.this.fragmentOpened);
                        notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("Notifications", 2131627016), str8, z2, false);
                        return;
                    } else if (itemViewType == 7) {
                        View view = viewHolder.itemView;
                        view.setTag(Integer.valueOf(i));
                        if ((i == ProfileActivity.this.infoSectionRow && ProfileActivity.this.lastSectionRow == -1 && ProfileActivity.this.secretSettingsSectionRow == -1 && ProfileActivity.this.sharedMediaRow == -1 && ProfileActivity.this.membersSectionRow == -1) || i == ProfileActivity.this.secretSettingsSectionRow || i == ProfileActivity.this.lastSectionRow || (i == ProfileActivity.this.membersSectionRow && ProfileActivity.this.lastSectionRow == -1 && ProfileActivity.this.sharedMediaRow == -1)) {
                            view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, ProfileActivity.this.getThemedColor("windowBackgroundGrayShadow")));
                            return;
                        } else {
                            view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, ProfileActivity.this.getThemedColor("windowBackgroundGrayShadow")));
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
                        if (tLRPC$TL_chatChannelParticipant == null) {
                            return;
                        }
                        if (tLRPC$TL_chatChannelParticipant instanceof TLRPC$TL_chatChannelParticipant) {
                            TLRPC$ChannelParticipant tLRPC$ChannelParticipant = tLRPC$TL_chatChannelParticipant.channelParticipant;
                            if (!TextUtils.isEmpty(tLRPC$ChannelParticipant.rank)) {
                                string2 = tLRPC$ChannelParticipant.rank;
                            } else if (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator) {
                                string2 = LocaleController.getString("ChannelCreator", 2131624894);
                            } else if (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) {
                                string2 = LocaleController.getString("ChannelAdmin", 2131624875);
                            }
                            str8 = string2;
                        } else if (tLRPC$TL_chatChannelParticipant instanceof TLRPC$TL_chatParticipantCreator) {
                            str8 = LocaleController.getString("ChannelCreator", 2131624894);
                        } else if (tLRPC$TL_chatChannelParticipant instanceof TLRPC$TL_chatParticipantAdmin) {
                            str8 = LocaleController.getString("ChannelAdmin", 2131624875);
                        }
                        userCell.setAdminRole(str8);
                        userCell.setData(ProfileActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$TL_chatChannelParticipant.user_id)), null, null, 0, i != ProfileActivity.this.membersEndRow - 1);
                        return;
                    } else if (itemViewType == 12) {
                        viewHolder.itemView.requestLayout();
                        return;
                    } else if (itemViewType == 15) {
                        SettingsSuggestionCell settingsSuggestionCell = (SettingsSuggestionCell) viewHolder.itemView;
                        if (i != ProfileActivity.this.passwordSuggestionRow) {
                            i3 = 0;
                        }
                        settingsSuggestionCell.setType(i3);
                        return;
                    } else if (itemViewType == 17) {
                        TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.mContext, 2131165435, ProfileActivity.this.getThemedColor("windowBackgroundGrayShadow")));
                        textInfoPrivacyCell.setText(LocaleController.getString("BotAddToGroupOrChannelInfo", 2131624705));
                        return;
                    } else if (itemViewType != 18) {
                        return;
                    }
                }
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
                textCell.setTag("windowBackgroundWhiteBlackText");
                if (i != ProfileActivity.this.settingsTimerRow) {
                    if (i != ProfileActivity.this.unblockRow) {
                        if (i != ProfileActivity.this.settingsKeyRow) {
                            if (i != ProfileActivity.this.joinRow) {
                                if (i == ProfileActivity.this.subscribersRow) {
                                    if (ProfileActivity.this.chatInfo != null) {
                                        if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup) {
                                            String string3 = LocaleController.getString("ChannelSubscribers", 2131624968);
                                            String format = String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.participants_count));
                                            if (i == ProfileActivity.this.membersSectionRow - 1) {
                                                z3 = false;
                                            }
                                            textCell.setTextAndValueAndIcon(string3, format, 2131165749, z3);
                                            return;
                                        }
                                        String string4 = LocaleController.getString("ChannelMembers", 2131624917);
                                        String format2 = String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.participants_count));
                                        if (i == ProfileActivity.this.membersSectionRow - 1) {
                                            z4 = false;
                                        }
                                        textCell.setTextAndValueAndIcon(string4, format2, 2131165749, z4);
                                        return;
                                    } else if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup) {
                                        String string5 = LocaleController.getString("ChannelSubscribers", 2131624968);
                                        if (i == ProfileActivity.this.membersSectionRow - 1) {
                                            z5 = false;
                                        }
                                        textCell.setTextAndIcon(string5, 2131165749, z5);
                                        return;
                                    } else {
                                        String string6 = LocaleController.getString("ChannelMembers", 2131624917);
                                        if (i == ProfileActivity.this.membersSectionRow - 1) {
                                            z6 = false;
                                        }
                                        textCell.setTextAndIcon(string6, 2131165749, z6);
                                        return;
                                    }
                                } else if (i == ProfileActivity.this.subscribersRequestsRow) {
                                    if (ProfileActivity.this.chatInfo == null) {
                                        return;
                                    }
                                    String string7 = LocaleController.getString("SubscribeRequests", 2131628484);
                                    String format3 = String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.requests_pending));
                                    if (i == ProfileActivity.this.membersSectionRow - 1) {
                                        z7 = false;
                                    }
                                    textCell.setTextAndValueAndIcon(string7, format3, 2131165905, z7);
                                    return;
                                } else if (i == ProfileActivity.this.administratorsRow) {
                                    if (ProfileActivity.this.chatInfo != null) {
                                        String string8 = LocaleController.getString("ChannelAdministrators", 2131624877);
                                        String format4 = String.format("%d", Integer.valueOf(ProfileActivity.this.chatInfo.admins_count));
                                        if (i == ProfileActivity.this.membersSectionRow - 1) {
                                            z8 = false;
                                        }
                                        textCell.setTextAndValueAndIcon(string8, format4, 2131165635, z8);
                                        return;
                                    }
                                    String string9 = LocaleController.getString("ChannelAdministrators", 2131624877);
                                    if (i == ProfileActivity.this.membersSectionRow - 1) {
                                        z9 = false;
                                    }
                                    textCell.setTextAndIcon(string9, 2131165635, z9);
                                    return;
                                } else if (i == ProfileActivity.this.blockedUsersRow) {
                                    if (ProfileActivity.this.chatInfo != null) {
                                        String string10 = LocaleController.getString("ChannelBlacklist", 2131624882);
                                        String format5 = String.format("%d", Integer.valueOf(Math.max(ProfileActivity.this.chatInfo.banned_count, ProfileActivity.this.chatInfo.kicked_count)));
                                        if (i == ProfileActivity.this.membersSectionRow - 1) {
                                            z10 = false;
                                        }
                                        textCell.setTextAndValueAndIcon(string10, format5, 2131165970, z10);
                                        return;
                                    }
                                    String string11 = LocaleController.getString("ChannelBlacklist", 2131624882);
                                    if (i == ProfileActivity.this.membersSectionRow - 1) {
                                        z11 = false;
                                    }
                                    textCell.setTextAndIcon(string11, 2131165970, z11);
                                    return;
                                } else if (i != ProfileActivity.this.addMemberRow) {
                                    if (i != ProfileActivity.this.sendMessageRow) {
                                        if (i != ProfileActivity.this.reportRow) {
                                            if (i != ProfileActivity.this.languageRow) {
                                                if (i != ProfileActivity.this.notificationRow) {
                                                    if (i != ProfileActivity.this.privacyRow) {
                                                        if (i != ProfileActivity.this.dataRow) {
                                                            if (i != ProfileActivity.this.chatRow) {
                                                                if (i != ProfileActivity.this.filtersRow) {
                                                                    if (i != ProfileActivity.this.stickersRow) {
                                                                        if (i != ProfileActivity.this.questionRow) {
                                                                            if (i != ProfileActivity.this.faqRow) {
                                                                                if (i != ProfileActivity.this.policyRow) {
                                                                                    if (i != ProfileActivity.this.sendLogsRow) {
                                                                                        if (i != ProfileActivity.this.sendLastLogsRow) {
                                                                                            if (i != ProfileActivity.this.clearLogsRow) {
                                                                                                if (i != ProfileActivity.this.switchBackendRow) {
                                                                                                    if (i != ProfileActivity.this.devicesRow) {
                                                                                                        if (i == ProfileActivity.this.setAvatarRow) {
                                                                                                            ProfileActivity.this.cellCameraDrawable.setCustomEndFrame(86);
                                                                                                            ProfileActivity.this.cellCameraDrawable.setCurrentFrame(85, false);
                                                                                                            textCell.setTextAndIcon(LocaleController.getString("SetProfilePhoto", 2131628246), ProfileActivity.this.cellCameraDrawable, false);
                                                                                                            textCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                                                                                                            textCell.getImageView().setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
                                                                                                            textCell.setImageLeft(12);
                                                                                                            ProfileActivity.this.setAvatarCell = textCell;
                                                                                                            return;
                                                                                                        } else if (i != ProfileActivity.this.addToGroupButtonRow) {
                                                                                                            if (i != ProfileActivity.this.premiumRow) {
                                                                                                                return;
                                                                                                            }
                                                                                                            textCell.setTextAndIcon(LocaleController.getString("TelegramPremium", 2131628572), PremiumGradient.getInstance().premiumStarMenuDrawable, false);
                                                                                                            textCell.setImageLeft(23);
                                                                                                            return;
                                                                                                        } else {
                                                                                                            textCell.setTextAndIcon(LocaleController.getString("AddToGroupOrChannel", 2131624294), 2131165751, false);
                                                                                                            textCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                                                                                                            return;
                                                                                                        }
                                                                                                    }
                                                                                                    textCell.setTextAndIcon(LocaleController.getString("Devices", 2131625465), 2131165613, true);
                                                                                                    return;
                                                                                                }
                                                                                                textCell.setText("Switch Backend", false);
                                                                                                return;
                                                                                            }
                                                                                            String string12 = LocaleController.getString("DebugClearLogs", 2131625335);
                                                                                            if (ProfileActivity.this.switchBackendRow == -1) {
                                                                                                z13 = false;
                                                                                            }
                                                                                            textCell.setText(string12, z13);
                                                                                            return;
                                                                                        }
                                                                                        textCell.setText(LocaleController.getString("DebugSendLastLogs", 2131625360), true);
                                                                                        return;
                                                                                    }
                                                                                    textCell.setText(LocaleController.getString("DebugSendLogs", 2131625361), true);
                                                                                    return;
                                                                                }
                                                                                textCell.setTextAndIcon(LocaleController.getString("PrivacyPolicy", 2131627728), 2131165865, false);
                                                                                return;
                                                                            }
                                                                            textCell.setTextAndIcon(LocaleController.getString("TelegramFAQ", 2131628561), 2131165757, true);
                                                                            return;
                                                                        }
                                                                        textCell.setTextAndIcon(LocaleController.getString("AskAQuestion", 2131624473), 2131165642, true);
                                                                        return;
                                                                    }
                                                                    textCell.setTextAndIcon(LocaleController.getString(2131628450), 2131165948, true);
                                                                    return;
                                                                }
                                                                textCell.setTextAndIcon(LocaleController.getString("Filters", 2131625899), 2131165732, true);
                                                                return;
                                                            }
                                                            textCell.setTextAndIcon(LocaleController.getString("ChatSettings", 2131625032), 2131165815, true);
                                                            return;
                                                        }
                                                        textCell.setTextAndIcon(LocaleController.getString("DataSettings", 2131625308), 2131165701, true);
                                                        return;
                                                    }
                                                    textCell.setTextAndIcon(LocaleController.getString("PrivacySettings", 2131627735), 2131165923, true);
                                                    return;
                                                }
                                                textCell.setTextAndIcon(LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, true);
                                                return;
                                            }
                                            textCell.setTextAndIcon(LocaleController.getString("Language", 2131626346), 2131165773, false);
                                            textCell.setImageLeft(23);
                                            return;
                                        }
                                        textCell.setText(LocaleController.getString("ReportUserLocation", 2131627964), false);
                                        textCell.setColors(null, "windowBackgroundWhiteRedText5");
                                        return;
                                    }
                                    textCell.setText(LocaleController.getString("SendMessageLocation", 2131628199), true);
                                    return;
                                } else {
                                    textCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                                    String string13 = LocaleController.getString("AddMember", 2131624269);
                                    if (ProfileActivity.this.membersSectionRow != -1) {
                                        z12 = false;
                                    }
                                    textCell.setTextAndIcon(string13, 2131165690, z12);
                                    return;
                                }
                            }
                            textCell.setColors(null, "windowBackgroundWhiteBlueText2");
                            if (ProfileActivity.this.currentChat.megagroup) {
                                textCell.setText(LocaleController.getString("ProfileJoinGroup", 2131627740), false);
                                return;
                            } else {
                                textCell.setText(LocaleController.getString("ProfileJoinChannel", 2131627739), false);
                                return;
                            }
                        }
                        IdenticonDrawable identiconDrawable = new IdenticonDrawable();
                        identiconDrawable.setEncryptedChat(ProfileActivity.this.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(ProfileActivity.this.dialogId))));
                        textCell.setTextAndValueDrawable(LocaleController.getString("EncryptionKey", 2131625636), identiconDrawable, false);
                        return;
                    }
                    textCell.setText(LocaleController.getString("Unblock", 2131628725), false);
                    textCell.setColors(null, "windowBackgroundWhiteRedText5");
                    return;
                }
                int i6 = ProfileActivity.this.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(ProfileActivity.this.dialogId))).ttl;
                if (i6 == 0) {
                    str6 = LocaleController.getString("ShortMessageLifetimeForever", 2131628328);
                } else {
                    str6 = LocaleController.formatTTLString(i6);
                }
                textCell.setTextAndValue(LocaleController.getString("MessageLifetime", 2131626624), str6, false);
            }
        }

        public /* synthetic */ void lambda$onBindViewHolder$1(View view) {
            if (ProfileActivity.this.userInfo != null) {
                ProfileActivity.this.presentFragment(new ChangeBioActivity());
            }
        }

        public /* synthetic */ void lambda$onBindViewHolder$2(int i, AboutLinkCell aboutLinkCell, View view) {
            ProfileActivity.this.processOnClickOrPress(i, aboutLinkCell);
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
                return adapterPosition == ProfileActivity.this.notificationRow || adapterPosition == ProfileActivity.this.numberRow || adapterPosition == ProfileActivity.this.privacyRow || adapterPosition == ProfileActivity.this.languageRow || adapterPosition == ProfileActivity.this.setUsernameRow || adapterPosition == ProfileActivity.this.bioRow || adapterPosition == ProfileActivity.this.versionRow || adapterPosition == ProfileActivity.this.dataRow || adapterPosition == ProfileActivity.this.chatRow || adapterPosition == ProfileActivity.this.questionRow || adapterPosition == ProfileActivity.this.devicesRow || adapterPosition == ProfileActivity.this.filtersRow || adapterPosition == ProfileActivity.this.stickersRow || adapterPosition == ProfileActivity.this.faqRow || adapterPosition == ProfileActivity.this.policyRow || adapterPosition == ProfileActivity.this.sendLogsRow || adapterPosition == ProfileActivity.this.sendLastLogsRow || adapterPosition == ProfileActivity.this.clearLogsRow || adapterPosition == ProfileActivity.this.switchBackendRow || adapterPosition == ProfileActivity.this.setAvatarRow || adapterPosition == ProfileActivity.this.addToGroupButtonRow || adapterPosition == ProfileActivity.this.premiumRow;
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
            if (i == ProfileActivity.this.phoneRow || i == ProfileActivity.this.usernameRow || i == ProfileActivity.this.locationRow || i == ProfileActivity.this.numberRow || i == ProfileActivity.this.setUsernameRow) {
                return 2;
            }
            if (i == ProfileActivity.this.userInfoRow || i == ProfileActivity.this.channelInfoRow || i == ProfileActivity.this.bioRow) {
                return 3;
            }
            if (i == ProfileActivity.this.settingsTimerRow || i == ProfileActivity.this.settingsKeyRow || i == ProfileActivity.this.reportRow || i == ProfileActivity.this.subscribersRow || i == ProfileActivity.this.subscribersRequestsRow || i == ProfileActivity.this.administratorsRow || i == ProfileActivity.this.blockedUsersRow || i == ProfileActivity.this.addMemberRow || i == ProfileActivity.this.joinRow || i == ProfileActivity.this.unblockRow || i == ProfileActivity.this.sendMessageRow || i == ProfileActivity.this.notificationRow || i == ProfileActivity.this.privacyRow || i == ProfileActivity.this.languageRow || i == ProfileActivity.this.dataRow || i == ProfileActivity.this.chatRow || i == ProfileActivity.this.questionRow || i == ProfileActivity.this.devicesRow || i == ProfileActivity.this.filtersRow || i == ProfileActivity.this.stickersRow || i == ProfileActivity.this.faqRow || i == ProfileActivity.this.policyRow || i == ProfileActivity.this.sendLogsRow || i == ProfileActivity.this.sendLastLogsRow || i == ProfileActivity.this.clearLogsRow || i == ProfileActivity.this.switchBackendRow || i == ProfileActivity.this.setAvatarRow || i == ProfileActivity.this.addToGroupButtonRow) {
                return 4;
            }
            if (i == ProfileActivity.this.notificationsDividerRow) {
                return 5;
            }
            if (i == ProfileActivity.this.notificationsRow) {
                return 6;
            }
            if (i == ProfileActivity.this.infoSectionRow || i == ProfileActivity.this.lastSectionRow || i == ProfileActivity.this.membersSectionRow || i == ProfileActivity.this.secretSettingsSectionRow || i == ProfileActivity.this.settingsSectionRow || i == ProfileActivity.this.devicesSectionRow || i == ProfileActivity.this.helpSectionCell || i == ProfileActivity.this.setAvatarSectionRow || i == ProfileActivity.this.passwordSuggestionSectionRow || i == ProfileActivity.this.phoneSuggestionSectionRow || i == ProfileActivity.this.premiumSectionsRow) {
                return 7;
            }
            if (i >= ProfileActivity.this.membersStartRow && i < ProfileActivity.this.membersEndRow) {
                return 8;
            }
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
    }

    /* loaded from: classes3.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private ArrayList<MessagesController.FaqSearchResult> faqSearchArray;
        private ArrayList<MessagesController.FaqSearchResult> faqSearchResults;
        private TLRPC$WebPage faqWebPage;
        private String lastSearchString;
        private boolean loadingFaqPage;
        private Context mContext;
        private ArrayList<Object> recentSearches;
        private ArrayList<CharSequence> resultNames;
        private SearchResult[] searchArray;
        private ArrayList<SearchResult> searchResults;
        private Runnable searchRunnable;
        private boolean searchWas;

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
                SearchAdapter.this = r1;
                this.guid = i;
                this.searchTitle = str;
                this.rowName = str2;
                this.openRunnable = runnable;
                this.iconResId = i2;
                if (str3 != null && str4 != null) {
                    this.path = new String[]{str3, str4};
                } else if (str3 == null) {
                } else {
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

            public void open() {
                this.openRunnable.run();
                AndroidUtilities.scrollToFragmentRow(((BaseFragment) ProfileActivity.this).parentLayout, this.rowName);
            }
        }

        public /* synthetic */ void lambda$new$0() {
            ProfileActivity profileActivity = ProfileActivity.this;
            profileActivity.presentFragment(new ChangeNameActivity(profileActivity.resourcesProvider));
        }

        public /* synthetic */ void lambda$new$1() {
            ProfileActivity.this.presentFragment(new ActionIntroActivity(3));
        }

        public /* synthetic */ void lambda$new$2() {
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

        public /* synthetic */ void lambda$new$3() {
            if (ProfileActivity.this.userInfo != null) {
                ProfileActivity.this.presentFragment(new ChangeBioActivity());
            }
        }

        public /* synthetic */ void lambda$new$4() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        public /* synthetic */ void lambda$new$5() {
            ProfileActivity.this.presentFragment(new NotificationsCustomSettingsActivity(1, new ArrayList(), true));
        }

        public /* synthetic */ void lambda$new$6() {
            ProfileActivity.this.presentFragment(new NotificationsCustomSettingsActivity(0, new ArrayList(), true));
        }

        public /* synthetic */ void lambda$new$7() {
            ProfileActivity.this.presentFragment(new NotificationsCustomSettingsActivity(2, new ArrayList(), true));
        }

        public /* synthetic */ void lambda$new$8() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        public /* synthetic */ void lambda$new$9() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        public /* synthetic */ void lambda$new$10() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        public /* synthetic */ void lambda$new$11() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        public /* synthetic */ void lambda$new$12() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        public /* synthetic */ void lambda$new$13() {
            ProfileActivity.this.presentFragment(new NotificationsSettingsActivity());
        }

        public /* synthetic */ void lambda$new$14() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$15() {
            ProfileActivity.this.presentFragment(new PrivacyUsersActivity());
        }

        public /* synthetic */ void lambda$new$16() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(6, true));
        }

        public /* synthetic */ void lambda$new$17() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(0, true));
        }

        public /* synthetic */ void lambda$new$18() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(4, true));
        }

        public /* synthetic */ void lambda$new$19() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(5, true));
        }

        public /* synthetic */ void lambda$new$20() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(3, true));
        }

        public /* synthetic */ void lambda$new$21() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(2, true));
        }

        public /* synthetic */ void lambda$new$22() {
            ProfileActivity.this.presentFragment(new PrivacyControlActivity(1, true));
        }

        public /* synthetic */ void lambda$new$23() {
            ProfileActivity.this.presentFragment(PasscodeActivity.determineOpenFragment());
        }

        public /* synthetic */ void lambda$new$24() {
            ProfileActivity.this.presentFragment(new TwoStepVerificationActivity());
        }

        public /* synthetic */ void lambda$new$25() {
            ProfileActivity.this.presentFragment(new SessionsActivity(0));
        }

        public /* synthetic */ void lambda$new$26() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$27() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$28() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$29() {
            ProfileActivity.this.presentFragment(new SessionsActivity(1));
        }

        public /* synthetic */ void lambda$new$30() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$31() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$32() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$33() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$34() {
            ProfileActivity.this.presentFragment(new PrivacySettingsActivity());
        }

        public /* synthetic */ void lambda$new$35() {
            ProfileActivity.this.presentFragment(new SessionsActivity(0));
        }

        public /* synthetic */ void lambda$new$36() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$37() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$38() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        public /* synthetic */ void lambda$new$39() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        public /* synthetic */ void lambda$new$40() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        public /* synthetic */ void lambda$new$41() {
            ProfileActivity.this.presentFragment(new CacheControlActivity());
        }

        public /* synthetic */ void lambda$new$42() {
            ProfileActivity.this.presentFragment(new DataUsageActivity());
        }

        public /* synthetic */ void lambda$new$43() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$44() {
            ProfileActivity.this.presentFragment(new DataAutoDownloadActivity(0));
        }

        public /* synthetic */ void lambda$new$45() {
            ProfileActivity.this.presentFragment(new DataAutoDownloadActivity(1));
        }

        public /* synthetic */ void lambda$new$46() {
            ProfileActivity.this.presentFragment(new DataAutoDownloadActivity(2));
        }

        public /* synthetic */ void lambda$new$47() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$48() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$49() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$50() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$51() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$52() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$53() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$54() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$55() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$56() {
            ProfileActivity.this.presentFragment(new ProxyListActivity());
        }

        public /* synthetic */ void lambda$new$57() {
            ProfileActivity.this.presentFragment(new ProxyListActivity());
        }

        public /* synthetic */ void lambda$new$58() {
            ProfileActivity.this.presentFragment(new DataSettingsActivity());
        }

        public /* synthetic */ void lambda$new$59() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$60() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$61() {
            ProfileActivity.this.presentFragment(new WallpapersListActivity(0));
        }

        public /* synthetic */ void lambda$new$62() {
            ProfileActivity.this.presentFragment(new WallpapersListActivity(1));
        }

        public /* synthetic */ void lambda$new$63() {
            ProfileActivity.this.presentFragment(new WallpapersListActivity(0));
        }

        public /* synthetic */ void lambda$new$64() {
            ProfileActivity.this.presentFragment(new ThemeActivity(1));
        }

        public /* synthetic */ void lambda$new$65() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$66() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$67() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$68() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$69() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$70() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$71() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$72() {
            ProfileActivity.this.presentFragment(new ThemeActivity(0));
        }

        public /* synthetic */ void lambda$new$73() {
            ProfileActivity.this.presentFragment(new StickersActivity(0));
        }

        public /* synthetic */ void lambda$new$74() {
            ProfileActivity.this.presentFragment(new StickersActivity(0));
        }

        public /* synthetic */ void lambda$new$75() {
            ProfileActivity.this.presentFragment(new FeaturedStickersActivity());
        }

        public /* synthetic */ void lambda$new$76() {
            ProfileActivity.this.presentFragment(new StickersActivity(1));
        }

        public /* synthetic */ void lambda$new$77() {
            ProfileActivity.this.presentFragment(new ArchivedStickersActivity(0));
        }

        public /* synthetic */ void lambda$new$78() {
            ProfileActivity.this.presentFragment(new ArchivedStickersActivity(1));
        }

        public /* synthetic */ void lambda$new$79() {
            ProfileActivity.this.presentFragment(new LanguageSelectActivity());
        }

        public /* synthetic */ void lambda$new$80() {
            ProfileActivity profileActivity = ProfileActivity.this;
            profileActivity.showDialog(AlertsCreator.createSupportAlert(profileActivity, null));
        }

        public /* synthetic */ void lambda$new$81() {
            Browser.openUrl(ProfileActivity.this.getParentActivity(), LocaleController.getString("TelegramFaqUrl", 2131628563));
        }

        public /* synthetic */ void lambda$new$82() {
            Browser.openUrl(ProfileActivity.this.getParentActivity(), LocaleController.getString("PrivacyPolicyUrl", 2131627730));
        }

        public SearchAdapter(Context context) {
            SerializedData serializedData;
            boolean z;
            int readInt32;
            int readInt322;
            String[] strArr;
            ProfileActivity.this = r22;
            SearchResult[] searchResultArr = new SearchResult[83];
            searchResultArr[0] = new SearchResult(this, 500, LocaleController.getString("EditName", 2131625587), 0, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda10(this));
            searchResultArr[1] = new SearchResult(this, 501, LocaleController.getString("ChangePhoneNumber", 2131624859), 0, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda11(this));
            searchResultArr[2] = new SearchResult(this, 502, LocaleController.getString("AddAnotherAccount", 2131624246), 0, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda32(this));
            searchResultArr[3] = new SearchResult(this, 503, LocaleController.getString("UserBio", 2131628802), 0, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda62(this));
            searchResultArr[4] = new SearchResult(this, 1, LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda18(this));
            searchResultArr[5] = new SearchResult(this, 2, LocaleController.getString("NotificationsPrivateChats", 2131627058), LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda7(this));
            searchResultArr[6] = new SearchResult(this, 3, LocaleController.getString("NotificationsGroups", 2131627039), LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda22(this));
            searchResultArr[7] = new SearchResult(this, 4, LocaleController.getString("NotificationsChannels", 2131627019), LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda41(this));
            searchResultArr[8] = new SearchResult(this, 5, LocaleController.getString("VoipNotificationSettings", 2131629157), "callsSectionRow", LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda0(this));
            searchResultArr[9] = new SearchResult(this, 6, LocaleController.getString("BadgeNumber", 2131624660), "badgeNumberSection", LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda3(this));
            searchResultArr[10] = new SearchResult(this, 7, LocaleController.getString("InAppNotifications", 2131626221), "inappSectionRow", LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda63(this));
            searchResultArr[11] = new SearchResult(this, 8, LocaleController.getString("ContactJoined", 2131625233), "contactJoinedRow", LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda51(this));
            searchResultArr[12] = new SearchResult(this, 9, LocaleController.getString("PinnedMessages", 2131627548), "pinnedMessageRow", LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda61(this));
            searchResultArr[13] = new SearchResult(this, 10, LocaleController.getString("ResetAllNotifications", 2131627986), "resetNotificationsRow", LocaleController.getString("NotificationsAndSounds", 2131627018), 2131165830, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda13(this));
            searchResultArr[14] = new SearchResult(this, 100, LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda24(this));
            searchResultArr[15] = new SearchResult(this, 101, LocaleController.getString("BlockedUsers", 2131624690), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda74(this));
            searchResultArr[16] = new SearchResult(this, 105, LocaleController.getString("PrivacyPhone", 2131627721), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda71(this));
            searchResultArr[17] = new SearchResult(this, 102, LocaleController.getString("PrivacyLastSeen", 2131627710), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda26(this));
            searchResultArr[18] = new SearchResult(this, 103, LocaleController.getString("PrivacyProfilePhoto", 2131627731), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda54(this));
            searchResultArr[19] = new SearchResult(this, 104, LocaleController.getString("PrivacyForwards", 2131627702), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda77(this));
            searchResultArr[20] = new SearchResult(this, 122, LocaleController.getString("PrivacyP2P", 2131627711), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda60(this));
            searchResultArr[21] = new SearchResult(this, 106, LocaleController.getString("Calls", 2131624809), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda5(this));
            searchResultArr[22] = new SearchResult(this, 107, LocaleController.getString("GroupsAndChannels", 2131626113), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda29(this));
            searchResultArr[23] = new SearchResult(this, 108, LocaleController.getString("Passcode", 2131627162), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda23(this));
            searchResultArr[24] = new SearchResult(this, 109, LocaleController.getString("TwoStepVerification", 2131628701), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda69(this));
            searchResultArr[25] = new SearchResult(this, 110, LocaleController.getString("SessionsTitle", 2131628233), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda35(this));
            searchResultArr[26] = r22.getMessagesController().autoarchiveAvailable ? new SearchResult(this, 121, LocaleController.getString("ArchiveAndMute", 2131624393), "newChatsRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda17(this)) : null;
            searchResultArr[27] = new SearchResult(this, 112, LocaleController.getString("DeleteAccountIfAwayFor2", 2131625370), "deleteAccountRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda45(this));
            searchResultArr[28] = new SearchResult(this, 113, LocaleController.getString("PrivacyPaymentsClear", 2131627714), "paymentsClearRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda78(this));
            searchResultArr[29] = new SearchResult(this, 114, LocaleController.getString("WebSessionsTitle", 2131629225), LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda76(this));
            searchResultArr[30] = new SearchResult(this, 115, LocaleController.getString("SyncContactsDelete", 2131628527), "contactsDeleteRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda57(this));
            searchResultArr[31] = new SearchResult(this, 116, LocaleController.getString("SyncContacts", 2131628525), "contactsSyncRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda19(this));
            searchResultArr[32] = new SearchResult(this, 117, LocaleController.getString("SuggestContacts", 2131628498), "contactsSuggestRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda73(this));
            searchResultArr[33] = new SearchResult(this, 118, LocaleController.getString("MapPreviewProvider", 2131626534), "secretMapRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda55(this));
            searchResultArr[34] = new SearchResult(this, 119, LocaleController.getString("SecretWebPage", 2131628155), "secretWebpageRow", LocaleController.getString("PrivacySettings", 2131627735), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda40(this));
            searchResultArr[35] = new SearchResult(this, 120, LocaleController.getString("Devices", 2131625465), 2131165923, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda8(this));
            searchResultArr[36] = new SearchResult(this, 200, LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda6(this));
            searchResultArr[37] = new SearchResult(this, 201, LocaleController.getString("DataUsage", 2131625309), "usageSectionRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda46(this));
            searchResultArr[38] = new SearchResult(this, 202, LocaleController.getString("StorageUsage", 2131628481), LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda27(this));
            searchResultArr[39] = new SearchResult(203, LocaleController.getString("KeepMedia", 2131626340), "keepMediaRow", LocaleController.getString("DataSettings", 2131625308), LocaleController.getString("StorageUsage", 2131628481), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda64(this));
            searchResultArr[40] = new SearchResult(204, LocaleController.getString("ClearMediaCache", 2131625146), "cacheRow", LocaleController.getString("DataSettings", 2131625308), LocaleController.getString("StorageUsage", 2131628481), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda15(this));
            searchResultArr[41] = new SearchResult(205, LocaleController.getString("LocalDatabase", 2131626479), "databaseRow", LocaleController.getString("DataSettings", 2131625308), LocaleController.getString("StorageUsage", 2131628481), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda1(this));
            searchResultArr[42] = new SearchResult(this, 206, LocaleController.getString("NetworkUsage", 2131626763), LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda75(this));
            searchResultArr[43] = new SearchResult(this, 207, LocaleController.getString("AutomaticMediaDownload", 2131624626), "mediaDownloadSectionRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda48(this));
            searchResultArr[44] = new SearchResult(this, 208, LocaleController.getString("WhenUsingMobileData", 2131629234), LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda21(this));
            searchResultArr[45] = new SearchResult(this, 209, LocaleController.getString("WhenConnectedOnWiFi", 2131629232), LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda16(this));
            searchResultArr[46] = new SearchResult(this, 210, LocaleController.getString("WhenRoaming", 2131629233), LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda28(this));
            searchResultArr[47] = new SearchResult(this, 211, LocaleController.getString("ResetAutomaticMediaDownload", 2131627987), "resetDownloadRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda65(this));
            searchResultArr[48] = new SearchResult(this, 212, LocaleController.getString("AutoplayMedia", 2131624629), "autoplayHeaderRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda2(this));
            searchResultArr[49] = new SearchResult(this, 213, LocaleController.getString("AutoplayGIF", 2131624628), "autoplayGifsRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda39(this));
            searchResultArr[50] = new SearchResult(this, 214, LocaleController.getString("AutoplayVideo", 2131624630), "autoplayVideoRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda59(this));
            searchResultArr[51] = new SearchResult(this, 215, LocaleController.getString("Streaming", 2131628482), "streamSectionRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda70(this));
            searchResultArr[52] = new SearchResult(this, 216, LocaleController.getString("EnableStreaming", 2131625624), "enableStreamRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda4(this));
            searchResultArr[53] = new SearchResult(this, 217, LocaleController.getString("Calls", 2131624809), "callsSectionRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda52(this));
            searchResultArr[54] = new SearchResult(this, 218, LocaleController.getString("VoipUseLessData", 2131629201), "useLessDataForCallsRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda43(this));
            searchResultArr[55] = new SearchResult(this, 219, LocaleController.getString("VoipQuickReplies", 2131629176), "quickRepliesRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda44(this));
            searchResultArr[56] = new SearchResult(this, 220, LocaleController.getString("ProxySettings", 2131627752), LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda33(this));
            searchResultArr[57] = new SearchResult(221, LocaleController.getString("UseProxyForCalls", 2131628783), "callsRow", LocaleController.getString("DataSettings", 2131625308), LocaleController.getString("ProxySettings", 2131627752), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda36(this));
            searchResultArr[58] = new SearchResult(this, 111, LocaleController.getString("PrivacyDeleteCloudDrafts", 2131627699), "clearDraftsRow", LocaleController.getString("DataSettings", 2131625308), 2131165701, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda80(this));
            searchResultArr[59] = new SearchResult(this, 300, LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda20(this));
            searchResultArr[60] = new SearchResult(this, 301, LocaleController.getString("TextSizeHeader", 2131628597), "textSizeHeaderRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda14(this));
            searchResultArr[61] = new SearchResult(this, 302, LocaleController.getString("ChatBackground", 2131625002), LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda81(this));
            searchResultArr[62] = new SearchResult(303, LocaleController.getString("SetColor", 2131628242), null, LocaleController.getString("ChatSettings", 2131625032), LocaleController.getString("ChatBackground", 2131625002), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda12(this));
            searchResultArr[63] = new SearchResult(304, LocaleController.getString("ResetChatBackgrounds", 2131627990), "resetRow", LocaleController.getString("ChatSettings", 2131625032), LocaleController.getString("ChatBackground", 2131625002), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda53(this));
            searchResultArr[64] = new SearchResult(this, 305, LocaleController.getString("AutoNightTheme", 2131624615), LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda50(this));
            searchResultArr[65] = new SearchResult(this, 306, LocaleController.getString("ColorTheme", 2131625185), "themeHeaderRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda56(this));
            searchResultArr[66] = new SearchResult(this, 307, LocaleController.getString("ChromeCustomTabs", 2131625129), "customTabsRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda34(this));
            searchResultArr[67] = new SearchResult(this, 308, LocaleController.getString("DirectShare", 2131625476), "directShareRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda49(this));
            searchResultArr[68] = new SearchResult(this, 309, LocaleController.getString("EnableAnimations", 2131625618), "enableAnimationsRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda9(this));
            searchResultArr[69] = new SearchResult(this, 310, LocaleController.getString("RaiseToSpeak", 2131627828), "raiseToSpeakRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda38(this));
            searchResultArr[70] = new SearchResult(this, 311, LocaleController.getString("SendByEnter", 2131628180), "sendByEnterRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda82(this));
            searchResultArr[71] = new SearchResult(this, 312, LocaleController.getString("SaveToGallerySettings", 2131628073), "saveToGalleryRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda79(this));
            searchResultArr[72] = new SearchResult(this, 318, LocaleController.getString("DistanceUnits", 2131625517), "distanceRow", LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda25(this));
            searchResultArr[73] = new SearchResult(this, 313, LocaleController.getString("StickersAndMasks", 2131628444), LocaleController.getString("ChatSettings", 2131625032), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda58(this));
            searchResultArr[74] = new SearchResult(314, LocaleController.getString("SuggestStickers", 2131628502), "suggestRow", LocaleController.getString("ChatSettings", 2131625032), LocaleController.getString("StickersAndMasks", 2131628444), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda37(this));
            searchResultArr[75] = new SearchResult(315, LocaleController.getString("FeaturedStickers", 2131625801), null, LocaleController.getString("ChatSettings", 2131625032), LocaleController.getString("StickersAndMasks", 2131628444), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda30(this));
            searchResultArr[76] = new SearchResult(316, LocaleController.getString("Masks", 2131626544), null, LocaleController.getString("ChatSettings", 2131625032), LocaleController.getString("StickersAndMasks", 2131628444), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda68(this));
            searchResultArr[77] = new SearchResult(317, LocaleController.getString("ArchivedStickers", 2131624414), null, LocaleController.getString("ChatSettings", 2131625032), LocaleController.getString("StickersAndMasks", 2131628444), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda66(this));
            searchResultArr[78] = new SearchResult(317, LocaleController.getString("ArchivedMasks", 2131624409), null, LocaleController.getString("ChatSettings", 2131625032), LocaleController.getString("StickersAndMasks", 2131628444), 2131165815, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda72(this));
            searchResultArr[79] = new SearchResult(this, 400, LocaleController.getString("Language", 2131626346), 2131165773, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda67(this));
            searchResultArr[80] = new SearchResult(this, 402, LocaleController.getString("AskAQuestion", 2131624473), LocaleController.getString("SettingsHelp", 2131628263), 2131165757, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda47(this));
            searchResultArr[81] = new SearchResult(this, 403, LocaleController.getString("TelegramFAQ", 2131628561), LocaleController.getString("SettingsHelp", 2131628263), 2131165757, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda42(this));
            searchResultArr[82] = new SearchResult(this, 404, LocaleController.getString("PrivacyPolicy", 2131627728), LocaleController.getString("SettingsHelp", 2131628263), 2131165757, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda31(this));
            this.searchArray = searchResultArr;
            this.faqSearchArray = new ArrayList<>();
            this.resultNames = new ArrayList<>();
            this.searchResults = new ArrayList<>();
            this.faqSearchResults = new ArrayList<>();
            this.recentSearches = new ArrayList<>();
            this.mContext = context;
            HashMap hashMap = new HashMap();
            int i = 0;
            while (true) {
                SearchResult[] searchResultArr2 = this.searchArray;
                if (i >= searchResultArr2.length) {
                    break;
                }
                if (searchResultArr2[i] != null) {
                    hashMap.put(Integer.valueOf(searchResultArr2[i].guid), this.searchArray[i]);
                }
                i++;
            }
            Set<String> stringSet = MessagesController.getGlobalMainSettings().getStringSet("settingsSearchRecent2", null);
            if (stringSet != null) {
                for (String str : stringSet) {
                    try {
                        serializedData = new SerializedData(Utilities.hexToBytes(str));
                        z = false;
                        readInt32 = serializedData.readInt32(false);
                        readInt322 = serializedData.readInt32(false);
                    } catch (Exception unused) {
                    }
                    if (readInt322 == 0) {
                        String readString = serializedData.readString(false);
                        int readInt323 = serializedData.readInt32(false);
                        if (readInt323 > 0) {
                            strArr = new String[readInt323];
                            int i2 = 0;
                            while (i2 < readInt323) {
                                strArr[i2] = serializedData.readString(z);
                                i2++;
                                z = false;
                            }
                        } else {
                            strArr = null;
                        }
                        MessagesController.FaqSearchResult faqSearchResult = new MessagesController.FaqSearchResult(readString, strArr, serializedData.readString(z));
                        faqSearchResult.num = readInt32;
                        this.recentSearches.add(faqSearchResult);
                    } else if (readInt322 == 1) {
                        try {
                            SearchResult searchResult = (SearchResult) hashMap.get(Integer.valueOf(serializedData.readInt32(false)));
                            if (searchResult != null) {
                                searchResult.num = readInt32;
                                this.recentSearches.add(searchResult);
                            }
                        } catch (Exception unused2) {
                        }
                    }
                }
            }
            Collections.sort(this.recentSearches, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda86(this));
        }

        public /* synthetic */ int lambda$new$83(Object obj, Object obj2) {
            int num = getNum(obj);
            int num2 = getNum(obj2);
            if (num < num2) {
                return -1;
            }
            return num > num2 ? 1 : 0;
        }

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
            tLRPC$TL_messages_getWebPage.url = LocaleController.getString("TelegramFaqUrl", 2131628563);
            tLRPC$TL_messages_getWebPage.hash = 0;
            ProfileActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_messages_getWebPage, new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda87(this));
        }

        public /* synthetic */ void lambda$loadFaqWebPage$85(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject instanceof TLRPC$WebPage) {
                ArrayList arrayList = new ArrayList();
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
                                        arrayList.add(new MessagesController.FaqSearchResult(charSequence, str != null ? new String[]{LocaleController.getString("SettingsSearchFaq", 2131628267), str} : new String[]{LocaleController.getString("SettingsSearchFaq", 2131628267)}, url));
                                    }
                                }
                            }
                        } else if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockAnchor) {
                            break;
                        }
                    }
                    this.faqWebPage = tLRPC$WebPage;
                }
                AndroidUtilities.runOnUIThread(new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda85(this, arrayList));
            }
            this.loadingFaqPage = false;
        }

        public /* synthetic */ void lambda$loadFaqWebPage$84(ArrayList arrayList) {
            this.faqSearchArray.addAll(arrayList);
            ProfileActivity.this.getMessagesController().faqSearchArray = arrayList;
            ProfileActivity.this.getMessagesController().faqWebPage = this.faqWebPage;
            if (!this.searchWas) {
                notifyDataSetChanged();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i = 0;
            if (this.searchWas) {
                int size = this.searchResults.size();
                if (!this.faqSearchResults.isEmpty()) {
                    i = this.faqSearchResults.size() + 1;
                }
                return size + i;
            }
            int size2 = this.recentSearches.isEmpty() ? 0 : this.recentSearches.size() + 1;
            if (!this.faqSearchArray.isEmpty()) {
                i = this.faqSearchArray.size() + 1;
            }
            return size2 + i;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString("SettingsFaqSearchTitle", 2131628262));
                    return;
                } else if (itemViewType != 2) {
                    return;
                } else {
                    ((HeaderCell) viewHolder.itemView).setText(LocaleController.getString("SettingsRecent", 2131628266));
                    return;
                }
            }
            SettingsSearchCell settingsSearchCell = (SettingsSearchCell) viewHolder.itemView;
            boolean z2 = false;
            if (this.searchWas) {
                if (i < this.searchResults.size()) {
                    SearchResult searchResult = this.searchResults.get(i);
                    SearchResult searchResult2 = i > 0 ? this.searchResults.get(i - 1) : null;
                    int i2 = (searchResult2 == null || searchResult2.iconResId != searchResult.iconResId) ? searchResult.iconResId : 0;
                    CharSequence charSequence = this.resultNames.get(i);
                    String[] strArr = searchResult.path;
                    if (i >= this.searchResults.size() - 1) {
                        z = false;
                    }
                    settingsSearchCell.setTextAndValueAndIcon(charSequence, strArr, i2, z);
                    return;
                }
                int size = i - (this.searchResults.size() + 1);
                CharSequence charSequence2 = this.resultNames.get(this.searchResults.size() + size);
                String[] strArr2 = this.faqSearchResults.get(size).path;
                if (size < this.searchResults.size() - 1) {
                    z2 = true;
                }
                settingsSearchCell.setTextAndValue(charSequence2, strArr2, true, z2);
                return;
            }
            if (!this.recentSearches.isEmpty()) {
                i--;
            }
            if (i < this.recentSearches.size()) {
                Object obj = this.recentSearches.get(i);
                if (obj instanceof SearchResult) {
                    SearchResult searchResult3 = (SearchResult) obj;
                    String str = searchResult3.searchTitle;
                    String[] strArr3 = searchResult3.path;
                    if (i >= this.recentSearches.size() - 1) {
                        z = false;
                    }
                    settingsSearchCell.setTextAndValue(str, strArr3, false, z);
                    return;
                } else if (!(obj instanceof MessagesController.FaqSearchResult)) {
                    return;
                } else {
                    MessagesController.FaqSearchResult faqSearchResult = (MessagesController.FaqSearchResult) obj;
                    String str2 = faqSearchResult.title;
                    String[] strArr4 = faqSearchResult.path;
                    if (i < this.recentSearches.size() - 1) {
                        z2 = true;
                    }
                    settingsSearchCell.setTextAndValue(str2, strArr4, true, z2);
                    return;
                }
            }
            int size2 = i - (this.recentSearches.size() + 1);
            MessagesController.FaqSearchResult faqSearchResult2 = this.faqSearchArray.get(size2);
            String str3 = faqSearchResult2.title;
            String[] strArr5 = faqSearchResult2.path;
            if (size2 < this.recentSearches.size() - 1) {
                z2 = true;
            }
            settingsSearchCell.setTextAndValue(str3, strArr5, true, z2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new SettingsSearchCell(this.mContext);
            } else if (i == 1) {
                view = new GraySectionCell(this.mContext);
            } else {
                view = new HeaderCell(this.mContext, 16);
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
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
            if (!(obj instanceof MessagesController.FaqSearchResult)) {
                return 0;
            }
            return ((MessagesController.FaqSearchResult) obj).num;
        }

        public void search(String str) {
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
                ProfileActivity.this.emptyView.title.setText(LocaleController.getString("SettingsNoRecent", 2131628264));
                notifyDataSetChanged();
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            ProfileActivity$SearchAdapter$$ExternalSyntheticLambda83 profileActivity$SearchAdapter$$ExternalSyntheticLambda83 = new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda83(this, str);
            this.searchRunnable = profileActivity$SearchAdapter$$ExternalSyntheticLambda83;
            dispatchQueue.postRunnable(profileActivity$SearchAdapter$$ExternalSyntheticLambda83, 300L);
        }

        public /* synthetic */ void lambda$search$87(String str) {
            SpannableStringBuilder spannableStringBuilder;
            int i;
            String str2;
            String str3;
            SpannableStringBuilder spannableStringBuilder2;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            String str4 = " ";
            String[] split = str.split(str4);
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
                    String str5 = str4 + searchResult.searchTitle.toLowerCase();
                    SpannableStringBuilder spannableStringBuilder3 = spannableStringBuilder;
                    int i4 = 0;
                    while (i4 < split.length) {
                        if (split[i4].length() != 0) {
                            String str6 = split[i4];
                            int indexOf = str5.indexOf(str4 + str6);
                            if (indexOf < 0 && strArr[i4] != null) {
                                str6 = strArr[i4];
                                indexOf = str5.indexOf(str4 + str6);
                            }
                            if (indexOf >= 0) {
                                spannableStringBuilder2 = spannableStringBuilder3 == null ? new SpannableStringBuilder(searchResult.searchTitle) : spannableStringBuilder3;
                                str3 = str5;
                                spannableStringBuilder2.setSpan(new ForegroundColorSpan(ProfileActivity.this.getThemedColor("windowBackgroundWhiteBlueText4")), indexOf, str6.length() + indexOf, 33);
                            }
                        } else {
                            str3 = str5;
                            spannableStringBuilder2 = spannableStringBuilder3;
                        }
                        if (spannableStringBuilder2 != null && i4 == split.length - 1) {
                            if (searchResult.guid == 502) {
                                int i5 = -1;
                                int i6 = 0;
                                while (true) {
                                    if (i6 >= 4) {
                                        break;
                                    } else if (!UserConfig.getInstance(i3).isClientActivated()) {
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
                        spannableStringBuilder3 = spannableStringBuilder2;
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
                    SpannableStringBuilder spannableStringBuilder4 = null;
                    while (i8 < split.length) {
                        if (split[i8].length() != 0) {
                            String str8 = split[i8];
                            int indexOf2 = str7.indexOf(str4 + str8);
                            if (indexOf2 < 0 && strArr[i8] != null) {
                                str8 = strArr[i8];
                                indexOf2 = str7.indexOf(str4 + str8);
                            }
                            if (indexOf2 >= 0) {
                                if (spannableStringBuilder4 == null) {
                                    str2 = str4;
                                    spannableStringBuilder4 = new SpannableStringBuilder(faqSearchResult.title);
                                } else {
                                    str2 = str4;
                                }
                                i = size;
                                spannableStringBuilder4.setSpan(new ForegroundColorSpan(ProfileActivity.this.getThemedColor("windowBackgroundWhiteBlueText4")), indexOf2, str8.length() + indexOf2, 33);
                            }
                        } else {
                            str2 = str4;
                            i = size;
                        }
                        if (spannableStringBuilder4 != null && i8 == split.length - 1) {
                            arrayList2.add(faqSearchResult);
                            arrayList3.add(spannableStringBuilder4);
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
            AndroidUtilities.runOnUIThread(new ProfileActivity$SearchAdapter$$ExternalSyntheticLambda84(this, str, arrayList, arrayList2, arrayList3));
        }

        public /* synthetic */ void lambda$search$86(String str, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
            if (!str.equals(this.lastSearchString)) {
                return;
            }
            if (!this.searchWas) {
                ProfileActivity.this.emptyView.stickerView.getImageReceiver().startAnimation();
                ProfileActivity.this.emptyView.title.setText(LocaleController.getString("SettingsNoResults", 2131628265));
            }
            this.searchWas = true;
            this.searchResults = arrayList;
            this.faqSearchResults = arrayList2;
            this.resultNames = arrayList3;
            notifyDataSetChanged();
            ProfileActivity.this.emptyView.stickerView.getImageReceiver().startAnimation();
        }

        public boolean isSearchWas() {
            return this.searchWas;
        }
    }

    public void openUrl(String str) {
        if (str.startsWith("@")) {
            getMessagesController().openByUserName(str.substring(1), this, 0);
        } else if (str.startsWith("#")) {
            DialogsActivity dialogsActivity = new DialogsActivity(null);
            dialogsActivity.setSearchString(str);
            presentFragment(dialogsActivity);
        } else if (!str.startsWith("/") || this.parentLayout.fragmentsStack.size() <= 1) {
        } else {
            ArrayList<BaseFragment> arrayList = this.parentLayout.fragmentsStack;
            BaseFragment baseFragment = arrayList.get(arrayList.size() - 2);
            if (!(baseFragment instanceof ChatActivity)) {
                return;
            }
            finishFragment();
            ((ChatActivity) baseFragment).chatActivityEnterView.setCommand(null, str, false, false);
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
        ValueAnimator valueAnimator;
        boolean z = f > 0.0f;
        this.fragmentView.invalidate();
        AnimatorSet animatorSet = this.scrimAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.scrimAnimatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        if (z) {
            valueAnimator = ValueAnimator.ofFloat(0.0f, f);
            arrayList.add(valueAnimator);
        } else {
            valueAnimator = ValueAnimator.ofFloat(this.scrimPaint.getAlpha() / 255.0f, 0.0f);
            arrayList.add(valueAnimator);
        }
        valueAnimator.addUpdateListener(new ProfileActivity$$ExternalSyntheticLambda2(this));
        this.scrimAnimatorSet.playTogether(arrayList);
        this.scrimAnimatorSet.setDuration(z ? 150L : 220L);
        if (!z) {
            this.scrimAnimatorSet.addListener(new AnonymousClass38());
        }
        this.scrimAnimatorSet.start();
    }

    public /* synthetic */ void lambda$dimBehindView$39(ValueAnimator valueAnimator) {
        this.scrimPaint.setAlpha((int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f));
    }

    /* renamed from: org.telegram.ui.ProfileActivity$38 */
    /* loaded from: classes3.dex */
    public class AnonymousClass38 extends AnimatorListenerAdapter {
        AnonymousClass38() {
            ProfileActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ProfileActivity.this.scrimView = null;
            ((BaseFragment) ProfileActivity.this).fragmentView.invalidate();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        if (this.resourcesProvider != null) {
            return null;
        }
        ProfileActivity$$ExternalSyntheticLambda36 profileActivity$$ExternalSyntheticLambda36 = new ProfileActivity$$ExternalSyntheticLambda36(this);
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null) {
            arrayList.addAll(sharedMediaLayout.getThemeDescriptions());
        }
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_actionBarSelectorBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "chat_lockIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_subtitleInProfileBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundActionBarBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "profile_title"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "profile_status"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_subtitleInProfileBlue"));
        if (this.mediaCounterTextView != null) {
            arrayList.add(new ThemeDescription(this.mediaCounterTextView.getTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, profileActivity$$ExternalSyntheticLambda36, "player_actionBarSubtitle"));
            arrayList.add(new ThemeDescription(this.mediaCounterTextView.getNextTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, profileActivity$$ExternalSyntheticLambda36, "player_actionBarSubtitle"));
        }
        arrayList.add(new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "avatar_backgroundActionBarBlue"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.avatarImage, 0, null, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, "avatar_backgroundInProfileBlue"));
        arrayList.add(new ThemeDescription(this.writeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "profile_actionIcon"));
        arrayList.add(new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "profile_actionBackground"));
        arrayList.add(new ThemeDescription(this.writeButton, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "profile_actionPressedBackground"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGreenText2"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText2"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueButton"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"detailTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{SettingsSuggestionCell.class}, new String[]{"detailTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteLinkText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"yesButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_buttonText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{SettingsSuggestionCell.class}, new String[]{"yesButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{SettingsSuggestionCell.class}, new String[]{"yesButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButtonPressed"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SettingsSuggestionCell.class}, new String[]{"noButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_buttonText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{SettingsSuggestionCell.class}, new String[]{"noButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{SettingsSuggestionCell.class}, new String[]{"noButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButtonPressed"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{UserCell.class}, new String[]{"adminTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "profile_creatorIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, profileActivity$$ExternalSyntheticLambda36, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, profileActivity$$ExternalSyntheticLambda36, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileActivity$$ExternalSyntheticLambda36, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "undo_background"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{UndoView.class}, new String[]{"leftImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AboutLinkCell.class}, Theme.profile_aboutTextPaint, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AboutLinkCell.class}, Theme.profile_aboutTextPaint, null, null, "windowBackgroundWhiteLinkText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AboutLinkCell.class}, Theme.linkSelectionPaint, null, null, "windowBackgroundWhiteLinkSelection"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
        arrayList.add(new ThemeDescription(this.searchListView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, "graySection"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        if (this.mediaHeaderVisible) {
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[]{this.verifiedCheckDrawable}, null, "player_actionBarTitle"));
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[]{this.verifiedDrawable}, null, "windowBackgroundWhite"));
        } else {
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[]{this.verifiedCheckDrawable}, null, "profile_verifiedCheck"));
            arrayList.add(new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[]{this.verifiedDrawable}, null, "profile_verifiedBackground"));
        }
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$40() {
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
                if (tag instanceof String) {
                    this.onlineTextView[1].setTextColor(getThemedColor((String) tag));
                } else {
                    this.onlineTextView[1].setTextColor(getThemedColor("avatar_subtitleInProfileBlue"));
                }
            }
            Drawable drawable = this.lockIconDrawable;
            if (drawable != null) {
                drawable.setColorFilter(getThemedColor("chat_lockIcon"), PorterDuff.Mode.MULTIPLY);
            }
            ScamDrawable scamDrawable = this.scamDrawable;
            if (scamDrawable != null) {
                scamDrawable.setColor(getThemedColor("avatar_subtitleInProfileBlue"));
            }
            SimpleTextView[] simpleTextViewArr2 = this.nameTextView;
            if (simpleTextViewArr2[1] != null) {
                simpleTextViewArr2[1].setTextColor(getThemedColor("profile_title"));
            }
            ActionBar actionBar = this.actionBar;
            if (actionBar == null) {
                return;
            }
            actionBar.setItemsColor(getThemedColor("actionBarDefaultIcon"), false);
            this.actionBar.setItemsBackgroundColor(getThemedColor("avatar_actionBarSelectorBlue"), false);
        }
    }

    public void updateListAnimated(boolean z) {
        if (this.listAdapter == null) {
            if (z) {
                updateOnlineCount(false);
            }
            updateRowsIds();
            return;
        }
        DiffCallback diffCallback = new DiffCallback(this, null);
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

    public void saveScrollPosition() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null || this.layoutManager == null || recyclerListView.getChildCount() <= 0) {
            return;
        }
        View view = null;
        int i = Integer.MAX_VALUE;
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
        if (view == null) {
            return;
        }
        this.savedScrollPosition = i2;
        int top = view.getTop();
        this.savedScrollOffset = top;
        if (this.savedScrollPosition == 0 && !this.allowPullingDown && top > AndroidUtilities.dp(88.0f)) {
            this.savedScrollOffset = AndroidUtilities.dp(88.0f);
        }
        this.layoutManager.scrollToPositionWithOffset(i2, view.getTop() - this.listView.getPaddingTop());
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
    public void scrollToSharedMedia() {
        this.layoutManager.scrollToPositionWithOffset(this.sharedMediaRow, -this.listView.getPaddingTop());
    }

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
        try {
            Drawable mutate = this.fragmentView.getContext().getResources().getDrawable(2131165415).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), getThemedColor("profile_actionBackground"), getThemedColor("profile_actionPressedBackground")), 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            this.writeButton.setBackground(combinedDrawable);
        } catch (Exception unused) {
        }
    }

    private boolean isQrNeedVisible() {
        char c;
        if (!TextUtils.isEmpty(getUserConfig().getCurrentUser().username)) {
            return true;
        }
        ArrayList<TLRPC$PrivacyRule> privacyRules = ContactsController.getInstance(this.currentAccount).getPrivacyRules(6);
        if (privacyRules == null) {
            return false;
        }
        int i = 0;
        while (true) {
            c = 2;
            if (i >= privacyRules.size()) {
                break;
            }
            TLRPC$PrivacyRule tLRPC$PrivacyRule = privacyRules.get(i);
            if (!(tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueAllowAll)) {
                if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueDisallowAll) {
                    break;
                } else if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueAllowContacts) {
                    c = 1;
                    break;
                } else {
                    i++;
                }
            } else {
                c = 0;
                break;
            }
        }
        return c == 0 || c == 1;
    }

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
            ProfileActivity.this = r1;
            this.oldPositionToItem = new SparseIntArray();
            this.newPositionToItem = new SparseIntArray();
            this.oldChatParticipant = new ArrayList<>();
            this.oldChatParticipantSorted = new ArrayList<>();
        }

        /* synthetic */ DiffCallback(ProfileActivity profileActivity, AnonymousClass1 anonymousClass1) {
            this();
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
            put(19, ProfileActivity.this.chatRow, sparseIntArray);
            put(20, ProfileActivity.this.filtersRow, sparseIntArray);
            put(21, ProfileActivity.this.stickersRow, sparseIntArray);
            put(22, ProfileActivity.this.devicesRow, sparseIntArray);
            put(23, ProfileActivity.this.devicesSectionRow, sparseIntArray);
            put(24, ProfileActivity.this.helpHeaderRow, sparseIntArray);
            put(25, ProfileActivity.this.questionRow, sparseIntArray);
            put(26, ProfileActivity.this.faqRow, sparseIntArray);
            put(27, ProfileActivity.this.policyRow, sparseIntArray);
            put(28, ProfileActivity.this.helpSectionCell, sparseIntArray);
            put(29, ProfileActivity.this.debugHeaderRow, sparseIntArray);
            put(30, ProfileActivity.this.sendLogsRow, sparseIntArray);
            put(31, ProfileActivity.this.sendLastLogsRow, sparseIntArray);
            put(32, ProfileActivity.this.clearLogsRow, sparseIntArray);
            put(33, ProfileActivity.this.switchBackendRow, sparseIntArray);
            put(34, ProfileActivity.this.versionRow, sparseIntArray);
            put(35, ProfileActivity.this.emptyRow, sparseIntArray);
            put(36, ProfileActivity.this.bottomPaddingRow, sparseIntArray);
            put(37, ProfileActivity.this.infoHeaderRow, sparseIntArray);
            put(38, ProfileActivity.this.phoneRow, sparseIntArray);
            put(39, ProfileActivity.this.locationRow, sparseIntArray);
            put(40, ProfileActivity.this.userInfoRow, sparseIntArray);
            put(41, ProfileActivity.this.channelInfoRow, sparseIntArray);
            put(42, ProfileActivity.this.usernameRow, sparseIntArray);
            put(43, ProfileActivity.this.notificationsDividerRow, sparseIntArray);
            put(44, ProfileActivity.this.notificationsRow, sparseIntArray);
            put(45, ProfileActivity.this.infoSectionRow, sparseIntArray);
            put(46, ProfileActivity.this.sendMessageRow, sparseIntArray);
            put(47, ProfileActivity.this.reportRow, sparseIntArray);
            put(48, ProfileActivity.this.settingsTimerRow, sparseIntArray);
            put(49, ProfileActivity.this.settingsKeyRow, sparseIntArray);
            put(50, ProfileActivity.this.secretSettingsSectionRow, sparseIntArray);
            put(51, ProfileActivity.this.membersHeaderRow, sparseIntArray);
            put(52, ProfileActivity.this.addMemberRow, sparseIntArray);
            put(53, ProfileActivity.this.subscribersRow, sparseIntArray);
            put(54, ProfileActivity.this.subscribersRequestsRow, sparseIntArray);
            put(55, ProfileActivity.this.administratorsRow, sparseIntArray);
            put(56, ProfileActivity.this.blockedUsersRow, sparseIntArray);
            put(57, ProfileActivity.this.membersSectionRow, sparseIntArray);
            put(58, ProfileActivity.this.sharedMediaRow, sparseIntArray);
            put(59, ProfileActivity.this.unblockRow, sparseIntArray);
            put(60, ProfileActivity.this.addToGroupButtonRow, sparseIntArray);
            put(61, ProfileActivity.this.addToGroupInfoRow, sparseIntArray);
            put(62, ProfileActivity.this.joinRow, sparseIntArray);
            put(63, ProfileActivity.this.lastSectionRow, sparseIntArray);
        }

        private void put(int i, int i2, SparseIntArray sparseIntArray) {
            if (i2 >= 0) {
                sparseIntArray.put(i2, i);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        int i;
        if (this.isPulledDown) {
            return false;
        }
        if (this.actionBar.isActionModeShowed()) {
            i = getThemedColor("actionBarActionModeDefault");
        } else if (this.mediaHeaderVisible) {
            i = getThemedColor("windowBackgroundWhite");
        } else {
            i = getThemedColor("actionBarDefault");
        }
        return ColorUtils.calculateLuminance(i) > 0.699999988079071d;
    }
}