package org.telegram.ui.Components.Premium;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channels_getAdminedPublicChannels;
import org.telegram.tgnet.TLRPC$TL_channels_getInactiveChannels;
import org.telegram.tgnet.TLRPC$TL_channels_updateUsername;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_dialogFolder;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_chats;
import org.telegram.tgnet.TLRPC$TL_messages_inactiveChats;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.tgnet.tl.TL_stories$TL_premium_myBoosts;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.AdminedChannelCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChatEditActivity;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BottomSheetWithRecyclerListView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FireworksOverlay;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.LoginOrView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.Premium.boosts.BoostCounterView;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.Premium.boosts.BoostPagerBottomSheet;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.Components.Premium.boosts.ReassignBoostBottomSheet;
import org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity;
import org.telegram.ui.Components.RecyclerItemsEnterAnimator;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.ChannelBoostUtilities;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
import org.telegram.ui.Stories.recorder.StoryRecorder;
/* loaded from: classes4.dex */
public class LimitReachedBottomSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
    TextView actionBtn;
    ArrayList<BoostFeature> boostFeatures;
    int boostFeaturesStartRow;
    private TL_stories$TL_premium_boostsStatus boostsStatus;
    int bottomRow;
    private ChannelBoostsController.CanApplyBoost canApplyBoost;
    private boolean canSendLink;
    int chatEndRow;
    int chatStartRow;
    ArrayList<TLRPC$Chat> chats;
    int chatsTitleRow;
    private int currentValue;
    private long dialogId;
    View divider;
    int dividerRow;
    int emptyViewDividerRow;
    RecyclerItemsEnterAnimator enterAnimator;
    FireworksOverlay fireworksOverlay;
    private TLRPC$Chat fromChat;
    int headerRow;
    private HeaderView headerView;
    private ArrayList<TLRPC$Chat> inactiveChats;
    private ArrayList<String> inactiveChatsSignatures;
    private boolean isCurrentChat;
    private boolean isVeryLargeFile;
    LimitParams limitParams;
    LimitPreviewView limitPreviewView;
    private int linkRow;
    private boolean loading;
    int loadingRow;
    private boolean lockInvalidation;
    public Runnable onShowPremiumScreenRunnable;
    public Runnable onSuccessRunnable;
    BaseFragment parentFragment;
    public boolean parentIsChannel;
    PremiumButtonView premiumButtonView;
    private int requiredLvl;
    private ArrayList<TLRPC$User> restrictedUsers;
    int rowCount;
    HashSet<Object> selectedChats;
    Runnable statisticClickRunnable;
    final int type;

    /* loaded from: classes4.dex */
    public static class LimitParams {
        int icon = 0;
        String descriptionStr = null;
        String descriptionStrPremium = null;
        String descriptionStrLocked = null;
        int defaultLimit = 0;
        int premiumLimit = 0;
    }

    private static boolean hasFixedSize(int i) {
        return i == 0 || i == 3 || i == 4 || i == 6 || i == 7 || i == 12 || i == 13 || i == 14 || i == 15 || i == 16;
    }

    public static String limitTypeToServerString(int i) {
        switch (i) {
            case 0:
                return "double_limits__dialog_pinned";
            case 1:
            case 7:
            case 11:
            default:
                return null;
            case 2:
                return "double_limits__channels_public";
            case 3:
                return "double_limits__dialog_filters";
            case 4:
                return "double_limits__dialog_filters_chats";
            case 5:
                return "double_limits__channels";
            case 6:
                return "double_limits__upload_max_fileparts";
            case 8:
                return "double_limits__caption_length";
            case 9:
                return "double_limits__saved_gifs";
            case 10:
                return "double_limits__stickers_faved";
            case 12:
                return "double_limits__chatlist_invites";
            case 13:
                return "double_limits__chatlists_joined";
        }
    }

    protected int channelColorLevelMin() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class BoostFeature {
        public final int countPlural;
        public final String countValue;
        public final int iconResId;
        public final int textKey;
        public final String textKeyPlural;

        private BoostFeature(int i, int i2, String str, String str2, int i3) {
            this.iconResId = i;
            this.textKey = i2;
            this.countValue = str;
            this.textKeyPlural = str2;
            this.countPlural = i3;
        }

        public static BoostFeature of(int i, int i2) {
            return new BoostFeature(i, i2, null, null, -1);
        }

        public static BoostFeature of(int i, int i2, String str) {
            return new BoostFeature(i, i2, str, null, -1);
        }

        public static BoostFeature of(int i, String str, int i2) {
            return new BoostFeature(i, -1, null, str, i2);
        }

        public boolean equals(BoostFeature boostFeature) {
            return boostFeature != null && this.iconResId == boostFeature.iconResId && this.textKey == boostFeature.textKey && TextUtils.equals(this.countValue, boostFeature.countValue) && TextUtils.equals(this.textKeyPlural, boostFeature.textKeyPlural) && this.countPlural == boostFeature.countPlural;
        }

        public static boolean arraysEqual(ArrayList<BoostFeature> arrayList, ArrayList<BoostFeature> arrayList2) {
            if (arrayList == null && arrayList2 == null) {
                return true;
            }
            if ((arrayList == null || arrayList2 != null) && ((arrayList != null || arrayList2 == null) && arrayList.size() == arrayList2.size())) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!arrayList.get(i).equals(arrayList2.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        /* loaded from: classes4.dex */
        public static class BoostFeatureLevel extends BoostFeature {
            public final boolean isFirst;
            public final int lvl;

            public BoostFeatureLevel(int i, boolean z) {
                super(-1, -1, null, null, -1);
                this.lvl = i;
                this.isFirst = z;
            }
        }
    }

    public LimitReachedBottomSheet(BaseFragment baseFragment, Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        super(baseFragment, false, hasFixedSize(i), false, resourcesProvider);
        this.linkRow = -1;
        this.lockInvalidation = false;
        this.chats = new ArrayList<>();
        this.headerRow = -1;
        this.dividerRow = -1;
        this.chatsTitleRow = -1;
        this.chatStartRow = -1;
        this.chatEndRow = -1;
        this.loadingRow = -1;
        this.emptyViewDividerRow = -1;
        this.bottomRow = -1;
        this.boostFeaturesStartRow = -1;
        this.currentValue = -1;
        this.selectedChats = new HashSet<>();
        this.inactiveChats = new ArrayList<>();
        this.inactiveChatsSignatures = new ArrayList<>();
        this.restrictedUsers = new ArrayList<>();
        this.loading = false;
        this.requiredLvl = 0;
        fixNavigationBar(Theme.getColor(Theme.key_dialogBackground, this.resourcesProvider));
        this.parentFragment = baseFragment;
        this.currentAccount = i2;
        this.type = i;
        updateTitle();
        updateRows();
        if (i == 2) {
            loadAdminedChannels();
        } else if (i == 5) {
            loadInactiveChannels();
        }
        updatePremiumButtonText();
        if (i == 19) {
            FireworksOverlay fireworksOverlay = new FireworksOverlay(getContext());
            this.fireworksOverlay = fireworksOverlay;
            this.container.addView(fireworksOverlay, LayoutHelper.createFrame(-1, -1.0f));
        }
        if (i == 18 || i == 20 || i == 24 || i == 25 || i == 22 || i == 23 || i == 21 || i == 26 || i == 27) {
            ((ViewGroup) this.premiumButtonView.getParent()).removeView(this.premiumButtonView);
            View view = this.divider;
            if (view != null) {
                ((ViewGroup) view.getParent()).removeView(this.divider);
            }
            this.recyclerListView.setPadding(0, 0, 0, 0);
            TextView textView = new TextView(context);
            this.actionBtn = textView;
            textView.setGravity(17);
            this.actionBtn.setEllipsize(TextUtils.TruncateAt.END);
            this.actionBtn.setSingleLine(true);
            this.actionBtn.setTextSize(1, 14.0f);
            this.actionBtn.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.actionBtn.setText(this.premiumButtonView.getTextView().getText());
            this.actionBtn.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
            this.actionBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    LimitReachedBottomSheet.this.lambda$new$0(view2);
                }
            });
            this.actionBtn.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider), 120)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        AndroidUtilities.addToClipboard(getBoostLink());
        dismiss();
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public void onViewCreated(FrameLayout frameLayout) {
        int i;
        super.onViewCreated(frameLayout);
        final Context context = frameLayout.getContext();
        this.premiumButtonView = new PremiumButtonView(context, true, this.resourcesProvider) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.1
            @Override // android.view.View
            public void invalidate() {
                if (LimitReachedBottomSheet.this.lockInvalidation) {
                    return;
                }
                super.invalidate();
            }
        };
        if (!this.hasFixedSize && (i = this.type) != 18 && i != 20 && i != 24 && i != 25 && i != 22 && i != 23 && i != 21 && i != 26 && i != 27) {
            View view = new View(context) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.2
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    super.onDraw(canvas);
                    LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
                    if (limitReachedBottomSheet.chatEndRow - limitReachedBottomSheet.chatStartRow > 1) {
                        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), 1.0f, Theme.dividerPaint);
                    }
                }
            };
            this.divider = view;
            view.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground, this.resourcesProvider));
            frameLayout.addView(this.divider, LayoutHelper.createFrame(-1, 72.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        frameLayout.addView(this.premiumButtonView, LayoutHelper.createFrame(-1, 48.0f, 80, 16.0f, 0.0f, 16.0f, 12.0f));
        this.recyclerListView.setPadding(0, 0, 0, AndroidUtilities.dp(72.0f));
        this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda20
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i2) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$1(view2, i2);
            }
        });
        this.recyclerListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda21
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view2, int i2) {
                boolean lambda$onViewCreated$2;
                lambda$onViewCreated$2 = LimitReachedBottomSheet.this.lambda$onViewCreated$2(view2, i2);
                return lambda$onViewCreated$2;
            }
        });
        this.premiumButtonView.buttonLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$8(context, view2);
            }
        });
        this.premiumButtonView.overlayTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$10(view2);
            }
        });
        this.enterAnimator = new RecyclerItemsEnterAnimator(this.recyclerListView, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$1(View view, int i) {
        if (view instanceof AdminedChannelCell) {
            AdminedChannelCell adminedChannelCell = (AdminedChannelCell) view;
            TLRPC$Chat currentChannel = adminedChannelCell.getCurrentChannel();
            if (this.selectedChats.contains(currentChannel)) {
                this.selectedChats.remove(currentChannel);
            } else {
                this.selectedChats.add(currentChannel);
            }
            adminedChannelCell.setChecked(this.selectedChats.contains(currentChannel), true);
            updateButton();
        } else if (view instanceof GroupCreateUserCell) {
            if (this.canSendLink || this.type != 11) {
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) view;
                Object object = groupCreateUserCell.getObject();
                if (this.selectedChats.contains(object)) {
                    this.selectedChats.remove(object);
                } else {
                    this.selectedChats.add(object);
                }
                groupCreateUserCell.setChecked(this.selectedChats.contains(object), true);
                updateButton();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onViewCreated$2(View view, int i) {
        this.recyclerListView.getOnItemClickListener().onItemClick(view, i);
        if (this.type != 19) {
            view.performHapticFeedback(0);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$8(Context context, View view) {
        int i = this.type;
        if (i == 11) {
            return;
        }
        if (i != 19) {
            if (i == 18 || i == 20 || i == 24 || i == 25 || i == 22 || i == 23 || i == 21 || i == 26 || i == 27) {
                AndroidUtilities.addToClipboard(getBoostLink());
                dismiss();
                return;
            } else if (UserConfig.getInstance(this.currentAccount).isPremium() || MessagesController.getInstance(this.currentAccount).premiumLocked || this.isVeryLargeFile) {
                dismiss();
                return;
            } else {
                BaseFragment baseFragment = this.parentFragment;
                if (baseFragment == null) {
                    return;
                }
                if (baseFragment.getVisibleDialog() != null) {
                    this.parentFragment.getVisibleDialog().dismiss();
                }
                this.parentFragment.presentFragment(new PremiumPreviewFragment(limitTypeToServerString(this.type)));
                Runnable runnable = this.onShowPremiumScreenRunnable;
                if (runnable != null) {
                    runnable.run();
                }
                dismiss();
                return;
            }
        }
        ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
        if (canApplyBoost.empty) {
            if (UserConfig.getInstance(this.currentAccount).isPremium() && BoostRepository.isMultiBoostsAvailable()) {
                BoostDialogs.showMoreBoostsNeeded(this.dialogId, this);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context, this.resourcesProvider);
            builder.setTitle(LocaleController.getString("PremiumNeeded", R.string.PremiumNeeded));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("PremiumNeededForBoosting", R.string.PremiumNeededForBoosting)));
            builder.setPositiveButton(LocaleController.getString("CheckPhoneNumberYes", R.string.CheckPhoneNumberYes), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    LimitReachedBottomSheet.this.lambda$onViewCreated$3(dialogInterface, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), LimitReachedBottomSheet$$ExternalSyntheticLambda4.INSTANCE);
            builder.show();
            return;
        }
        boolean z = canApplyBoost.canApply;
        if (z && canApplyBoost.replaceDialogId == 0) {
            if (canApplyBoost.needSelector && BoostRepository.isMultiBoostsAvailable()) {
                this.lockInvalidation = true;
                this.limitPreviewView.invalidationEnabled = false;
                BaseFragment baseFragment2 = getBaseFragment();
                ChannelBoostsController.CanApplyBoost canApplyBoost2 = this.canApplyBoost;
                ReassignBoostBottomSheet.show(baseFragment2, canApplyBoost2.myBoosts, canApplyBoost2.currentChat).setOnHideListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda6
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        LimitReachedBottomSheet.this.lambda$onViewCreated$5(dialogInterface);
                    }
                });
                return;
            }
            boostChannel();
        } else if (z) {
            FrameLayout frameLayout = new FrameLayout(getContext());
            BackupImageView backupImageView = new BackupImageView(getContext());
            backupImageView.setRoundRadius(AndroidUtilities.dp(30.0f));
            frameLayout.addView(backupImageView, LayoutHelper.createFrame(60, 60.0f));
            frameLayout.setClipChildren(false);
            final Paint paint = new Paint(1);
            paint.setColor(Theme.getColor(Theme.key_dialogBackground));
            final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.filled_limit_boost);
            frameLayout.addView(new View(this, getContext()) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.3
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    float measuredWidth = getMeasuredWidth() / 2.0f;
                    float measuredHeight = getMeasuredHeight() / 2.0f;
                    canvas.drawCircle(measuredWidth, measuredHeight, getMeasuredWidth() / 2.0f, paint);
                    PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, getMeasuredWidth(), getMeasuredHeight(), -AndroidUtilities.dp(10.0f), 0.0f);
                    canvas.drawCircle(measuredWidth, measuredHeight, (getMeasuredWidth() / 2.0f) - AndroidUtilities.dp(2.0f), PremiumGradient.getInstance().getMainGradientPaint());
                    float dp = AndroidUtilities.dp(18.0f) / 2.0f;
                    drawable.setBounds((int) (measuredWidth - dp), (int) (measuredHeight - dp), (int) (measuredWidth + dp), (int) (measuredHeight + dp));
                    drawable.draw(canvas);
                }
            }, LayoutHelper.createFrame(28, 28.0f, 0, 34.0f, 34.0f, 0.0f, 0.0f));
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.msg_arrow_avatar);
            imageView.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon));
            frameLayout.addView(imageView, LayoutHelper.createFrame(24, 24, 17));
            BackupImageView backupImageView2 = new BackupImageView(getContext());
            backupImageView2.setRoundRadius(AndroidUtilities.dp(30.0f));
            frameLayout.addView(backupImageView2, LayoutHelper.createFrame(60, 60.0f, 0, 96.0f, 0.0f, 0.0f, 0.0f));
            FrameLayout frameLayout2 = new FrameLayout(getContext());
            frameLayout2.addView(frameLayout, LayoutHelper.createFrame(-2, 60, 1));
            frameLayout2.setClipChildren(false);
            TextView textView = new TextView(context);
            if (Build.VERSION.SDK_INT >= 21) {
                textView.setLetterSpacing(0.025f);
            }
            textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
            textView.setTextSize(1, 16.0f);
            frameLayout2.addView(textView, LayoutHelper.createLinear(-1, -2, 0, 24, 80, 24, 0));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.canApplyBoost.replaceDialogId));
            avatarDrawable.setInfo(this.currentAccount, chat);
            backupImageView.setForUserOrChat(chat, avatarDrawable);
            AvatarDrawable avatarDrawable2 = new AvatarDrawable();
            TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
            avatarDrawable2.setInfo(this.currentAccount, chat2);
            backupImageView2.setForUserOrChat(chat2, avatarDrawable2);
            AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
            builder2.setView(frameLayout2);
            textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ReplaceBoostChannelDescription", R.string.ReplaceBoostChannelDescription, chat.title, chat2.title)));
            builder2.setPositiveButton(LocaleController.getString("Replace", R.string.Replace), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    LimitReachedBottomSheet.this.lambda$onViewCreated$6(dialogInterface, i2);
                }
            });
            builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), LimitReachedBottomSheet$$ExternalSyntheticLambda5.INSTANCE);
            builder2.show();
        } else {
            int i2 = canApplyBoost.floodWait;
            if (i2 != 0) {
                BoostDialogs.showFloodWait(i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$3(DialogInterface dialogInterface, int i) {
        this.parentFragment.presentFragment(new PremiumPreviewFragment(null));
        dismiss();
        dialogInterface.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$5(DialogInterface dialogInterface) {
        this.lockInvalidation = false;
        this.limitPreviewView.invalidationEnabled = true;
        this.premiumButtonView.invalidate();
        this.limitPreviewView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$6(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        boostChannel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$10(View view) {
        int i = this.type;
        if (i == 19) {
            ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
            if (canApplyBoost.canApply) {
                this.premiumButtonView.buttonLayout.callOnClick();
                ChannelBoostsController.CanApplyBoost canApplyBoost2 = this.canApplyBoost;
                if (canApplyBoost2.alreadyActive && canApplyBoost2.boostedNow) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda11
                        @Override // java.lang.Runnable
                        public final void run() {
                            LimitReachedBottomSheet.this.lambda$onViewCreated$9();
                        }
                    }, this.canApplyBoost.needSelector ? 300L : 0L);
                }
            } else if (canApplyBoost.alreadyActive && BoostRepository.isMultiBoostsAvailable() && !this.canApplyBoost.isMaxLvl) {
                BoostDialogs.showMoreBoostsNeeded(this.dialogId, this);
            } else {
                dismiss();
            }
        } else if (i == 11) {
            if (this.selectedChats.isEmpty()) {
                dismiss();
            } else {
                sendInviteMessages();
            }
        } else if (this.selectedChats.isEmpty()) {
        } else {
            int i2 = this.type;
            if (i2 == 2) {
                revokeSelectedLinks();
            } else if (i2 == 5) {
                leaveFromSelectedGroups();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$9() {
        this.limitPreviewView.setBoosts(this.boostsStatus, false);
        limitPreviewIncreaseCurrentValue();
    }

    private void limitPreviewIncreaseCurrentValue() {
        LimitPreviewView limitPreviewView = this.limitPreviewView;
        TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = this.boostsStatus;
        int i = tL_stories$TL_premium_boostsStatus.boosts;
        int i2 = tL_stories$TL_premium_boostsStatus.current_level_boosts;
        limitPreviewView.increaseCurrentValue(i, i - i2, tL_stories$TL_premium_boostsStatus.next_level_boosts - i2);
    }

    private void boostChannel() {
        if (this.premiumButtonView.isLoading()) {
            return;
        }
        this.premiumButtonView.setLoading(true);
        MessagesController.getInstance(this.currentAccount).getBoostsController().applyBoost(this.dialogId, this.canApplyBoost.slot, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda16
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                LimitReachedBottomSheet.this.lambda$boostChannel$12((TL_stories$TL_premium_myBoosts) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda15
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                LimitReachedBottomSheet.this.lambda$boostChannel$13((TLRPC$TL_error) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$boostChannel$12(final TL_stories$TL_premium_myBoosts tL_stories$TL_premium_myBoosts) {
        MessagesController.getInstance(this.currentAccount).getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda10
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                LimitReachedBottomSheet.this.lambda$boostChannel$11(tL_stories$TL_premium_myBoosts, (TL_stories$TL_premium_boostsStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$boostChannel$11(TL_stories$TL_premium_myBoosts tL_stories$TL_premium_myBoosts, TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        this.premiumButtonView.setLoading(false);
        if (tL_stories$TL_premium_boostsStatus == null) {
            return;
        }
        this.boostsStatus.boosts++;
        limitPreviewIncreaseCurrentValue();
        setBoostsStats(tL_stories$TL_premium_boostsStatus, this.isCurrentChat);
        ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
        canApplyBoost.isMaxLvl = this.boostsStatus.next_level_boosts <= 0;
        canApplyBoost.boostedNow = true;
        canApplyBoost.setMyBoosts(tL_stories$TL_premium_myBoosts);
        onBoostSuccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$boostChannel$13(TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
            BoostDialogs.showFloodWait(Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue());
        }
        this.premiumButtonView.setLoading(false);
    }

    private void onBoostSuccess() {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new Visibility(this) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.4
            @Override // android.transition.Visibility
            public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, AndroidUtilities.dp(20.0f), 0.0f));
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                return animatorSet;
            }

            @Override // android.transition.Visibility
            public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 0.0f), ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0.0f, -AndroidUtilities.dp(20.0f)));
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                return animatorSet;
            }
        });
        transitionSet.setOrdering(0);
        TransitionManager.beginDelayedTransition(this.headerView, transitionSet);
        this.headerView.recreateTitleAndDescription();
        this.headerView.title.setText(getBoostsTitleString());
        this.headerView.description.setText(AndroidUtilities.replaceTags(getBoostsDescriptionString()));
        updateButton();
        this.fireworksOverlay.start();
        this.fireworksOverlay.performHapticFeedback(3);
        this.headerView.boostCounterView.setCount(this.canApplyBoost.boostCount, true);
    }

    private void sendInviteMessages() {
        String str;
        TLRPC$ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.fromChat.id);
        if (chatFull == null) {
            dismiss();
            return;
        }
        if (this.fromChat.username != null) {
            str = "@" + this.fromChat.username;
        } else {
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = chatFull.exported_invite;
            if (tLRPC$TL_chatInviteExported != null) {
                str = tLRPC$TL_chatInviteExported.link;
            } else {
                dismiss();
                return;
            }
        }
        Iterator<Object> it = this.selectedChats.iterator();
        while (it.hasNext()) {
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(str, ((TLRPC$User) it.next()).id, null, null, null, true, null, null, null, false, 0, null, false));
            str = str;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                LimitReachedBottomSheet.this.lambda$sendInviteMessages$14();
            }
        });
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendInviteMessages$14() {
        BulletinFactory global = BulletinFactory.global();
        if (global != null) {
            if (this.selectedChats.size() == 1) {
                global.createSimpleBulletin(R.raw.voip_invite, AndroidUtilities.replaceTags(LocaleController.formatString("InviteLinkSentSingle", R.string.InviteLinkSentSingle, ContactsController.formatName((TLRPC$User) this.selectedChats.iterator().next())))).show();
            } else {
                global.createSimpleBulletin(R.raw.voip_invite, AndroidUtilities.replaceTags(LocaleController.formatPluralString("InviteLinkSent", this.selectedChats.size(), Integer.valueOf(this.selectedChats.size())))).show();
            }
        }
    }

    public void setRequiredLvl(int i) {
        this.requiredLvl = i;
    }

    public void updatePremiumButtonText() {
        String string;
        int i = this.type;
        if (i == 19) {
            if (BoostRepository.isMultiBoostsAvailable()) {
                AnimatedTextView animatedTextView = this.premiumButtonView.buttonTextView;
                ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
                if (canApplyBoost != null && canApplyBoost.alreadyActive) {
                    string = LocaleController.getString("BoostingBoostAgain", R.string.BoostingBoostAgain);
                } else {
                    string = LocaleController.getString("BoostChannel", R.string.BoostChannel);
                }
                animatedTextView.setText(string);
                ChannelBoostsController.CanApplyBoost canApplyBoost2 = this.canApplyBoost;
                if (canApplyBoost2 == null || !canApplyBoost2.isMaxLvl) {
                    return;
                }
                this.premiumButtonView.buttonTextView.setText(LocaleController.getString("OK", R.string.OK));
                return;
            }
            this.premiumButtonView.buttonTextView.setText(LocaleController.getString("BoostChannel", R.string.BoostChannel));
        } else if (i == 18 || i == 20 || i == 24 || i == 25 || i == 22 || i == 23 || i == 21 || i == 26 || i == 27) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("d ");
            spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.msg_copy_filled), 0, 1, 0);
            spannableStringBuilder.append((CharSequence) LocaleController.getString("CopyLink", R.string.CopyLink));
            this.premiumButtonView.buttonTextView.setText(spannableStringBuilder);
        } else if (UserConfig.getInstance(this.currentAccount).isPremium() || MessagesController.getInstance(this.currentAccount).premiumLocked || this.isVeryLargeFile) {
            this.premiumButtonView.buttonTextView.setText(LocaleController.getString("OK", R.string.OK));
            this.premiumButtonView.hideIcon();
        } else {
            this.premiumButtonView.buttonTextView.setText(LocaleController.getString("IncreaseLimit", R.string.IncreaseLimit));
            LimitParams limitParams = this.limitParams;
            if (limitParams != null) {
                int i2 = limitParams.defaultLimit;
                int i3 = i2 + 1;
                int i4 = limitParams.premiumLimit;
                if (i3 == i4) {
                    this.premiumButtonView.setIcon(R.raw.addone_icon);
                    return;
                } else if (i2 != 0 && i4 != 0 && i4 / i2 >= 1.6f && i4 / i2 <= 2.5f) {
                    this.premiumButtonView.setIcon(R.raw.double_icon);
                    return;
                } else {
                    this.premiumButtonView.hideIcon();
                    return;
                }
            }
            this.premiumButtonView.hideIcon();
        }
    }

    private void leaveFromSelectedGroups() {
        final TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId()));
        final ArrayList arrayList = new ArrayList();
        Iterator<Object> it = this.selectedChats.iterator();
        while (it.hasNext()) {
            arrayList.add((TLRPC$Chat) it.next());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.formatPluralString("LeaveCommunities", arrayList.size(), new Object[0]));
        if (arrayList.size() == 1) {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ChannelLeaveAlertWithName", R.string.ChannelLeaveAlertWithName, ((TLRPC$Chat) arrayList.get(0)).title)));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ChatsLeaveAlert", R.string.ChatsLeaveAlert, new Object[0])));
        }
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("RevokeButton", R.string.RevokeButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                LimitReachedBottomSheet.this.lambda$leaveFromSelectedGroups$15(arrayList, user, dialogInterface, i);
            }
        });
        AlertDialog create = builder.create();
        create.show();
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold, this.resourcesProvider));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$leaveFromSelectedGroups$15(ArrayList arrayList, TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
        dismiss();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) arrayList.get(i2);
            MessagesController.getInstance(this.currentAccount).putChat(tLRPC$Chat, false);
            MessagesController.getInstance(this.currentAccount).deleteParticipantFromChat(tLRPC$Chat.id, tLRPC$User);
        }
    }

    private void updateButton() {
        int i = this.type;
        if (i != 19) {
            if (i == 11) {
                this.premiumButtonView.checkCounterView();
                if (!this.canSendLink) {
                    this.premiumButtonView.setOverlayText(LocaleController.getString("Close", R.string.Close), true, true);
                } else if (this.selectedChats.size() > 0) {
                    this.premiumButtonView.setOverlayText(LocaleController.getString("SendInviteLink", R.string.SendInviteLink), true, true);
                } else {
                    this.premiumButtonView.setOverlayText(LocaleController.getString("ActionSkip", R.string.ActionSkip), true, true);
                }
                this.premiumButtonView.counterView.setCount(this.selectedChats.size(), true);
                this.premiumButtonView.invalidate();
                return;
            } else if (this.selectedChats.size() > 0) {
                String str = null;
                int i2 = this.type;
                if (i2 == 2) {
                    str = LocaleController.formatPluralString("RevokeLinks", this.selectedChats.size(), new Object[0]);
                } else if (i2 == 5) {
                    str = LocaleController.formatPluralString("LeaveCommunities", this.selectedChats.size(), new Object[0]);
                }
                this.premiumButtonView.setOverlayText(str, true, true);
                return;
            } else {
                this.premiumButtonView.clearOverlayText();
                return;
            }
        }
        ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
        boolean z = canApplyBoost.canApply;
        if ((z || canApplyBoost.empty) && !canApplyBoost.boostedNow && !canApplyBoost.alreadyActive) {
            if (canApplyBoost.isMaxLvl) {
                this.premiumButtonView.setOverlayText(LocaleController.getString("OK", R.string.OK), true, true);
            } else {
                this.premiumButtonView.clearOverlayText();
            }
        } else if (z) {
            if (BoostRepository.isMultiBoostsAvailable()) {
                this.premiumButtonView.setOverlayText(LocaleController.getString("BoostingBoostAgain", R.string.BoostingBoostAgain), true, true);
            } else {
                this.premiumButtonView.setOverlayText(LocaleController.getString("BoostChannel", R.string.BoostChannel), true, true);
            }
        } else if (canApplyBoost.isMaxLvl) {
            this.premiumButtonView.setOverlayText(LocaleController.getString("OK", R.string.OK), true, true);
        } else if (BoostRepository.isMultiBoostsAvailable()) {
            this.premiumButtonView.setOverlayText(LocaleController.getString("BoostingBoostAgain", R.string.BoostingBoostAgain), true, true);
        } else {
            this.premiumButtonView.setOverlayText(LocaleController.getString("OK", R.string.OK), true, true);
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public CharSequence getTitle() {
        int i = this.type;
        if (i != 11) {
            switch (i) {
                case 18:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                    return LocaleController.getString(R.string.UnlockBoostChannelFeatures);
                case 19:
                    return LocaleController.getString(R.string.BoostChannel);
                default:
                    return LocaleController.getString(R.string.LimitReached);
            }
        }
        return LocaleController.getString(R.string.ChannelInviteViaLink);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.boostByChannelCreated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.boostedChannelByUser);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didStartedMultiGiftsSelector);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.boostByChannelCreated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.boostedChannelByUser);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didStartedMultiGiftsSelector);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        BaseFragment baseFragment;
        if (i == NotificationCenter.boostByChannelCreated) {
            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) objArr[0];
            boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
            BaseFragment lastFragment = getBaseFragment().getParentLayout().getLastFragment();
            if (lastFragment instanceof ChatCustomReactionsEditActivity) {
                List<BaseFragment> fragmentStack = getBaseFragment().getParentLayout().getFragmentStack();
                BaseFragment baseFragment2 = fragmentStack.size() >= 2 ? fragmentStack.get(fragmentStack.size() - 2) : null;
                BaseFragment baseFragment3 = fragmentStack.size() >= 3 ? fragmentStack.get(fragmentStack.size() - 3) : null;
                baseFragment = fragmentStack.size() >= 4 ? fragmentStack.get(fragmentStack.size() - 4) : null;
                if (baseFragment2 instanceof ChatEditActivity) {
                    getBaseFragment().getParentLayout().removeFragmentFromStack(baseFragment2);
                }
                dismiss();
                if (booleanValue) {
                    if (baseFragment3 instanceof ProfileActivity) {
                        getBaseFragment().getParentLayout().removeFragmentFromStack(baseFragment3);
                    }
                    lastFragment.finishFragment();
                    BoostDialogs.showBulletin(baseFragment, tLRPC$Chat, true);
                    return;
                }
                lastFragment.finishFragment();
                BoostDialogs.showBulletin(baseFragment3, tLRPC$Chat, false);
            } else if (booleanValue) {
                if (StoryRecorder.isVisible()) {
                    ChatActivity of = ChatActivity.of(-tLRPC$Chat.id);
                    LaunchActivity.getLastFragment().presentFragment(of, false, false);
                    StoryRecorder.destroyInstance();
                    dismiss();
                    BoostDialogs.showBulletin(of, tLRPC$Chat, true);
                    return;
                }
                List<BaseFragment> fragmentStack2 = getBaseFragment().getParentLayout().getFragmentStack();
                baseFragment = fragmentStack2.size() >= 2 ? fragmentStack2.get(fragmentStack2.size() - 2) : null;
                getBaseFragment().finishFragment();
                dismiss();
                if (baseFragment instanceof ChatActivity) {
                    BoostDialogs.showBulletin(baseFragment, tLRPC$Chat, true);
                }
            } else if (StoryRecorder.isVisible()) {
                ChatActivity of2 = ChatActivity.of(-tLRPC$Chat.id);
                LaunchActivity.getLastFragment().presentFragment(of2, false, false);
                StoryRecorder.destroyInstance();
                dismiss();
                BoostDialogs.showBulletin(of2, tLRPC$Chat, false);
            } else {
                dismiss();
                BoostDialogs.showBulletin(LaunchActivity.getLastFragment(), tLRPC$Chat, false);
            }
        } else if (i == NotificationCenter.boostedChannelByUser) {
            TL_stories$TL_premium_myBoosts tL_stories$TL_premium_myBoosts = (TL_stories$TL_premium_myBoosts) objArr[0];
            int intValue = ((Integer) objArr[1]).intValue();
            int intValue2 = ((Integer) objArr[2]).intValue();
            TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = (TL_stories$TL_premium_boostsStatus) objArr[3];
            if (tL_stories$TL_premium_boostsStatus == null || this.canApplyBoost == null) {
                return;
            }
            this.boostsStatus.boosts += intValue;
            limitPreviewIncreaseCurrentValue();
            setBoostsStats(tL_stories$TL_premium_boostsStatus, this.isCurrentChat);
            ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
            canApplyBoost.isMaxLvl = this.boostsStatus.next_level_boosts <= 0;
            canApplyBoost.boostedNow = true;
            canApplyBoost.setMyBoosts(tL_stories$TL_premium_myBoosts);
            onBoostSuccess();
            BulletinFactory.of(this.container, this.resourcesProvider).createSimpleBulletinWithIconSize(R.raw.ic_boosts_replace, LocaleController.formatPluralString("BoostingReassignedFromPlural", intValue, LocaleController.formatPluralString("BoostingFromOtherChannel", intValue2, new Object[0])), 30).setDuration(4000).show(true);
        } else if (i == NotificationCenter.didStartedMultiGiftsSelector) {
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 5 extends RecyclerListView.SelectionAdapter {
        5() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
            if (limitReachedBottomSheet.type != 11 || limitReachedBottomSheet.canSendLink) {
                return viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 4;
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LinearLayout linearLayout;
            Context context = viewGroup.getContext();
            switch (i) {
                case 1:
                    linearLayout = new AdminedChannelCell(context, new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.5.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(((AdminedChannelCell) view.getParent()).getCurrentChannel());
                            LimitReachedBottomSheet.this.revokeLinks(arrayList);
                        }
                    }, true, 9);
                    break;
                case 2:
                    linearLayout = new ShadowSectionCell(context, 12, Theme.getColor(Theme.key_windowBackgroundGray, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                    break;
                case 3:
                    View headerCell = new HeaderCell(context);
                    headerCell.setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
                    linearLayout = headerCell;
                    break;
                case 4:
                    linearLayout = new GroupCreateUserCell(context, 1, 8, false);
                    break;
                case 5:
                    FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context, null);
                    flickerLoadingView.setViewType(LimitReachedBottomSheet.this.type == 2 ? 22 : 21);
                    flickerLoadingView.setIsSingleCell(true);
                    flickerLoadingView.setIgnoreHeightCheck(true);
                    flickerLoadingView.setItemsCount(10);
                    linearLayout = flickerLoadingView;
                    break;
                case 6:
                    linearLayout = new View(this, LimitReachedBottomSheet.this.getContext()) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.5.3
                        @Override // android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(16.0f), 1073741824));
                        }
                    };
                    break;
                case 7:
                    FrameLayout frameLayout = new FrameLayout(LimitReachedBottomSheet.this.getContext());
                    frameLayout.setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0);
                    TextView textView = new TextView(context);
                    textView.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(LimitReachedBottomSheet.this.statisticClickRunnable != null ? 50.0f : 18.0f), AndroidUtilities.dp(13.0f));
                    textView.setTextSize(1, 16.0f);
                    textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    textView.setSingleLine(true);
                    frameLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 11.0f, 0.0f, 11.0f, 0.0f));
                    int dp = AndroidUtilities.dp(8.0f);
                    int color = Theme.getColor(Theme.key_graySection, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                    int i2 = Theme.key_listSelector;
                    textView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(dp, color, ColorUtils.setAlphaComponent(Theme.getColor(i2, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider), 76)));
                    textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                    textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$2(view);
                        }
                    });
                    if (LimitReachedBottomSheet.this.statisticClickRunnable != null) {
                        ImageView imageView = new ImageView(LimitReachedBottomSheet.this.getContext());
                        imageView.setImageResource(R.drawable.msg_stats);
                        imageView.setColorFilter(Theme.getColor(Theme.key_dialogTextBlack, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                        imageView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                        imageView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(Theme.getColor(i2, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider), 76)));
                        frameLayout.addView(imageView, LayoutHelper.createFrame(40, 40.0f, 21, 15.0f, 0.0f, 15.0f, 0.0f));
                        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$3(view);
                            }
                        });
                    }
                    textView.setText(LimitReachedBottomSheet.this.getBoostLink());
                    textView.setGravity(17);
                    linearLayout = frameLayout;
                    break;
                case 8:
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0);
                    linearLayout2.setOrientation(1);
                    LoginOrView loginOrView = new LoginOrView(context);
                    final LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
                    SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(LocaleController.getString(R.string.BoostingStoriesByGifting));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.BoostingStoriesByGiftingLink));
                    spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.5.1
                        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                        public void updateDrawState(TextPaint textPaint) {
                            super.updateDrawState(textPaint);
                            textPaint.setUnderlineText(false);
                            textPaint.setColor(Theme.getColor(Theme.key_chat_messageLinkIn, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                        }

                        @Override // android.text.style.ClickableSpan
                        public void onClick(View view) {
                            BoostPagerBottomSheet.show(LimitReachedBottomSheet.this.getBaseFragment(), LimitReachedBottomSheet.this.dialogId, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                        }
                    }, 0, spannableStringBuilder.length(), 33);
                    SpannableString spannableString = new SpannableString(">");
                    Drawable mutate = LimitReachedBottomSheet.this.getContext().getResources().getDrawable(R.drawable.msg_arrowright).mutate();
                    int i3 = Theme.key_chat_messageLinkIn;
                    mutate.setColorFilter(new PorterDuffColorFilter(i3, PorterDuff.Mode.SRC_IN));
                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(mutate);
                    coloredImageSpan.setColorKey(i3);
                    coloredImageSpan.setSize(AndroidUtilities.dp(18.0f));
                    coloredImageSpan.setWidth(AndroidUtilities.dp(11.0f));
                    coloredImageSpan.setTranslateX(-AndroidUtilities.dp(5.0f));
                    spannableString.setSpan(coloredImageSpan, 0, spannableString.length(), 33);
                    linksTextView.setText(TextUtils.concat(replaceTags, " ", AndroidUtilities.replaceCharSequence(">", spannableStringBuilder, spannableString)));
                    linksTextView.setTextSize(1, 14.0f);
                    if (((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider instanceof DarkThemeResourceProvider) {
                        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                    } else {
                        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                    }
                    linksTextView.setGravity(1);
                    linksTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$0(view);
                        }
                    });
                    loginOrView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            linksTextView.performClick();
                        }
                    });
                    linearLayout2.addView(LimitReachedBottomSheet.this.actionBtn, LayoutHelper.createLinear(-1, 48, 12.0f, 12.0f, 12.0f, 8.0f));
                    linearLayout2.addView(loginOrView, LayoutHelper.createLinear(-1, 48, 0.0f, 0.0f, 0.0f, 0.0f));
                    linearLayout2.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 12.0f, 0.0f, 12.0f, 4.0f));
                    linearLayout = linearLayout2;
                    break;
                case 9:
                    LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
                    linearLayout = new BoostFeatureCell(limitReachedBottomSheet, context, ((BottomSheet) limitReachedBottomSheet).resourcesProvider);
                    break;
                default:
                    linearLayout = LimitReachedBottomSheet.this.headerView = new HeaderView(context);
                    break;
            }
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(linearLayout);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            BoostPagerBottomSheet.show(LimitReachedBottomSheet.this.getBaseFragment(), LimitReachedBottomSheet.this.dialogId, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$2(View view) {
            AndroidUtilities.addToClipboard(LimitReachedBottomSheet.this.getBoostLink());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$3(View view) {
            LimitReachedBottomSheet.this.statisticClickRunnable.run();
            LimitReachedBottomSheet.this.dismiss();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 1) {
                LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
                TLRPC$Chat tLRPC$Chat = limitReachedBottomSheet.chats.get(i - limitReachedBottomSheet.chatStartRow);
                AdminedChannelCell adminedChannelCell = (AdminedChannelCell) viewHolder.itemView;
                TLRPC$Chat currentChannel = adminedChannelCell.getCurrentChannel();
                adminedChannelCell.setChannel(tLRPC$Chat, false);
                adminedChannelCell.setChecked(LimitReachedBottomSheet.this.selectedChats.contains(tLRPC$Chat), currentChannel == tLRPC$Chat);
            } else if (itemViewType == 9) {
                LimitReachedBottomSheet limitReachedBottomSheet2 = LimitReachedBottomSheet.this;
                int i2 = i - limitReachedBottomSheet2.boostFeaturesStartRow;
                ArrayList<BoostFeature> arrayList = limitReachedBottomSheet2.boostFeatures;
                if (arrayList == null || i2 < 0 || i2 >= arrayList.size()) {
                    return;
                }
                ((BoostFeatureCell) viewHolder.itemView).set(LimitReachedBottomSheet.this.boostFeatures.get(i2));
            } else if (itemViewType != 3) {
                if (itemViewType != 4) {
                    return;
                }
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
                LimitReachedBottomSheet limitReachedBottomSheet3 = LimitReachedBottomSheet.this;
                int i3 = limitReachedBottomSheet3.type;
                if (i3 == 5) {
                    TLRPC$Chat tLRPC$Chat2 = (TLRPC$Chat) limitReachedBottomSheet3.inactiveChats.get(i - LimitReachedBottomSheet.this.chatStartRow);
                    groupCreateUserCell.setObject(tLRPC$Chat2, tLRPC$Chat2.title, (String) LimitReachedBottomSheet.this.inactiveChatsSignatures.get(i - LimitReachedBottomSheet.this.chatStartRow), ((float) i) != ((float) LimitReachedBottomSheet.this.chatEndRow) - 1.0f);
                    groupCreateUserCell.setChecked(LimitReachedBottomSheet.this.selectedChats.contains(tLRPC$Chat2), false);
                } else if (i3 == 11) {
                    TLRPC$User tLRPC$User = (TLRPC$User) limitReachedBottomSheet3.restrictedUsers.get(i - LimitReachedBottomSheet.this.chatStartRow);
                    groupCreateUserCell.setObject(tLRPC$User, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name), LocaleController.formatUserStatus(((BottomSheet) LimitReachedBottomSheet.this).currentAccount, tLRPC$User, null, null), ((float) i) != ((float) LimitReachedBottomSheet.this.chatEndRow) - 1.0f);
                    groupCreateUserCell.setChecked(LimitReachedBottomSheet.this.selectedChats.contains(tLRPC$User), false);
                }
            } else {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                LimitReachedBottomSheet limitReachedBottomSheet4 = LimitReachedBottomSheet.this;
                int i4 = limitReachedBottomSheet4.type;
                if (i4 != 11) {
                    if (i4 == 2) {
                        headerCell.setText(LocaleController.getString("YourPublicCommunities", R.string.YourPublicCommunities));
                    } else {
                        headerCell.setText(LocaleController.getString("LastActiveCommunities", R.string.LastActiveCommunities));
                    }
                } else if (!limitReachedBottomSheet4.canSendLink) {
                    if (LimitReachedBottomSheet.this.restrictedUsers.size() == 1) {
                        headerCell.setText(LocaleController.getString("ChannelInviteViaLinkRestricted2", R.string.ChannelInviteViaLinkRestricted2));
                    } else {
                        headerCell.setText(LocaleController.getString("ChannelInviteViaLinkRestricted3", R.string.ChannelInviteViaLinkRestricted3));
                    }
                } else {
                    headerCell.setText(LocaleController.getString("ChannelInviteViaLink", R.string.ChannelInviteViaLink));
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            int i2;
            LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
            if (limitReachedBottomSheet.headerRow == i) {
                return 0;
            }
            if (limitReachedBottomSheet.dividerRow == i) {
                return 2;
            }
            if (limitReachedBottomSheet.chatsTitleRow == i) {
                return 3;
            }
            if (limitReachedBottomSheet.loadingRow == i) {
                return 5;
            }
            if (limitReachedBottomSheet.emptyViewDividerRow == i) {
                return 6;
            }
            if (limitReachedBottomSheet.linkRow == i) {
                return 7;
            }
            LimitReachedBottomSheet limitReachedBottomSheet2 = LimitReachedBottomSheet.this;
            if (limitReachedBottomSheet2.bottomRow == i) {
                return 8;
            }
            ArrayList<BoostFeature> arrayList = limitReachedBottomSheet2.boostFeatures;
            if (arrayList == null || i < (i2 = limitReachedBottomSheet2.boostFeaturesStartRow) || i > i2 + arrayList.size()) {
                int i3 = LimitReachedBottomSheet.this.type;
                return (i3 == 5 || i3 == 11) ? 4 : 1;
            }
            return 9;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return LimitReachedBottomSheet.this.rowCount;
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public RecyclerListView.SelectionAdapter createAdapter() {
        return new 5();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBoostLink() {
        return ChannelBoostUtilities.createLink(this.currentAccount, this.dialogId);
    }

    public void setCurrentValue(int i) {
        this.currentValue = i;
    }

    public void setVeryLargeFile(boolean z) {
        this.isVeryLargeFile = z;
        updatePremiumButtonText();
    }

    public void setRestrictedUsers(TLRPC$Chat tLRPC$Chat, ArrayList<TLRPC$User> arrayList) {
        this.fromChat = tLRPC$Chat;
        this.canSendLink = ChatObject.canUserDoAdminAction(tLRPC$Chat, 3);
        this.restrictedUsers = new ArrayList<>(arrayList);
        this.selectedChats.clear();
        if (this.canSendLink) {
            this.selectedChats.addAll(this.restrictedUsers);
        }
        updateRows();
        updateButton();
    }

    public void setDialogId(long j) {
        this.dialogId = j;
    }

    public void setBoostsStats(TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus, boolean z) {
        this.boostsStatus = tL_stories$TL_premium_boostsStatus;
        this.isCurrentChat = z;
        updateRows();
    }

    public void setCanApplyBoost(ChannelBoostsController.CanApplyBoost canApplyBoost) {
        this.canApplyBoost = canApplyBoost;
        updateButton();
        updatePremiumButtonText();
    }

    public void showStatisticButtonInLink(Runnable runnable) {
        this.statisticClickRunnable = runnable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class HeaderView extends LinearLayout {
        BoostCounterView boostCounterView;
        TextView description;
        TextView title;
        LinearLayout titleLinearLayout;

        /* JADX WARN: Removed duplicated region for block: B:124:0x03c8  */
        /* JADX WARN: Removed duplicated region for block: B:125:0x03cb  */
        /* JADX WARN: Removed duplicated region for block: B:128:0x03f1  */
        /* JADX WARN: Removed duplicated region for block: B:137:0x0412  */
        /* JADX WARN: Removed duplicated region for block: B:151:0x046c  */
        /* JADX WARN: Removed duplicated region for block: B:156:0x04a1  */
        /* JADX WARN: Removed duplicated region for block: B:158:0x04b0  */
        /* JADX WARN: Removed duplicated region for block: B:199:0x05b5  */
        /* JADX WARN: Removed duplicated region for block: B:214:0x073d  */
        /* JADX WARN: Removed duplicated region for block: B:221:0x078a  */
        /* JADX WARN: Removed duplicated region for block: B:222:0x079a  */
        /* JADX WARN: Removed duplicated region for block: B:225:0x07ab  */
        /* JADX WARN: Removed duplicated region for block: B:226:0x07bf  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0316  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0326  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0330  */
        @SuppressLint({"SetTextI18n"})
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public HeaderView(Context context) {
            super(context);
            String str;
            String formatPluralString;
            String str2;
            boolean z;
            int i;
            int i2;
            int i3;
            float f;
            int i4;
            boolean z2;
            int i5;
            int i6;
            int i7;
            float f2;
            float f3;
            setOrientation(1);
            setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0);
            LimitParams limitParams = LimitReachedBottomSheet.getLimitParams(LimitReachedBottomSheet.this.type, ((BottomSheet) LimitReachedBottomSheet.this).currentAccount);
            LimitReachedBottomSheet.this.limitParams = limitParams;
            int i8 = limitParams.icon;
            boolean z3 = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).premiumLocked;
            int i9 = LimitReachedBottomSheet.this.type;
            if (i9 == 19) {
                str = LimitReachedBottomSheet.this.getBoostsDescriptionString();
            } else if (i9 == 18) {
                str = LimitReachedBottomSheet.this.boostsStatus.level == 0 ? LocaleController.formatString("ChannelNeedBoostsDescription", R.string.ChannelNeedBoostsDescription, LocaleController.formatPluralString("MoreBoosts", LimitReachedBottomSheet.this.boostsStatus.next_level_boosts, Integer.valueOf(LimitReachedBottomSheet.this.boostsStatus.next_level_boosts))) : LocaleController.formatString("ChannelNeedBoostsDescriptionNextLevel", R.string.ChannelNeedBoostsDescriptionNextLevel, LocaleController.formatPluralString("MoreBoosts", LimitReachedBottomSheet.this.boostsStatus.next_level_boosts - LimitReachedBottomSheet.this.boostsStatus.boosts, Integer.valueOf(LimitReachedBottomSheet.this.boostsStatus.next_level_boosts - LimitReachedBottomSheet.this.boostsStatus.boosts)), LocaleController.formatPluralString("BoostStories", LimitReachedBottomSheet.this.boostsStatus.level + 1, new Object[0]));
            } else if (i9 == 20) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForColorDescription, Integer.valueOf(LimitReachedBottomSheet.this.channelColorLevelMin()));
            } else if (i9 == 24) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForProfileColorDescription, Integer.valueOf(LimitReachedBottomSheet.this.channelColorLevelMin()));
            } else if (i9 == 25) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForProfileIconDescription, Integer.valueOf(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).channelEmojiStatusLevelMin));
            } else if (i9 == 26) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForReplyIconDescription, Integer.valueOf(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).channelBgIconLevelMin));
            } else if (i9 == 27) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForEmojiStatusDescription, Integer.valueOf(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).channelProfileIconLevelMin));
            } else if (i9 == 22) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForWallpaperDescription, Integer.valueOf(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).channelWallpaperLevelMin));
            } else if (i9 == 23) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForCustomWallpaperDescription, Integer.valueOf(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).channelCustomWallpaperLevelMin));
            } else if (i9 == 21) {
                str = LocaleController.formatPluralString("ReactionReachLvlForReaction", LimitReachedBottomSheet.this.requiredLvl, Integer.valueOf(LimitReachedBottomSheet.this.requiredLvl));
            } else if (i9 == 11) {
                if (!LimitReachedBottomSheet.this.canSendLink) {
                    if (ChatObject.isChannelAndNotMegaGroup(LimitReachedBottomSheet.this.fromChat)) {
                        formatPluralString = LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? LocaleController.formatString("InviteChannelRestrictedUsers2One", R.string.InviteChannelRestrictedUsers2One, ContactsController.formatName((TLRPC$User) LimitReachedBottomSheet.this.restrictedUsers.get(0))) : LocaleController.formatPluralString("InviteChannelRestrictedUsers2", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size()));
                    } else {
                        formatPluralString = LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? LocaleController.formatString("InviteRestrictedUsers2One", R.string.InviteRestrictedUsers2One, ContactsController.formatName((TLRPC$User) LimitReachedBottomSheet.this.restrictedUsers.get(0))) : LocaleController.formatPluralString("InviteRestrictedUsers2", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size()));
                    }
                } else if (!ChatObject.isChannelAndNotMegaGroup(LimitReachedBottomSheet.this.fromChat)) {
                    if (LimitReachedBottomSheet.this.restrictedUsers.size() != 1) {
                        formatPluralString = LocaleController.formatPluralString("InviteRestrictedUsers", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size()));
                    } else {
                        formatPluralString = LocaleController.formatString("InviteRestrictedUsersOne", R.string.InviteRestrictedUsersOne, ContactsController.formatName((TLRPC$User) LimitReachedBottomSheet.this.restrictedUsers.get(0)));
                    }
                } else {
                    formatPluralString = LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? LocaleController.formatString("InviteChannelRestrictedUsersOne", R.string.InviteChannelRestrictedUsersOne, ContactsController.formatName((TLRPC$User) LimitReachedBottomSheet.this.restrictedUsers.get(0))) : LocaleController.formatPluralString("InviteChannelRestrictedUsers", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size()));
                }
                str2 = formatPluralString;
                z = true;
                LimitParams limitParams2 = LimitReachedBottomSheet.this.limitParams;
                int i10 = limitParams2.defaultLimit;
                i = limitParams2.premiumLimit;
                i2 = LimitReachedBottomSheet.this.currentValue;
                i3 = LimitReachedBottomSheet.this.type;
                if (i3 != 3) {
                    i2 = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).dialogFilters.size() - 1;
                } else if (i3 == 7) {
                    i2 = UserConfig.getActivatedAccountsCount();
                }
                if (LimitReachedBottomSheet.this.type == 0) {
                    ArrayList<TLRPC$Dialog> dialogs = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getDialogs(0);
                    int size = dialogs.size();
                    int i11 = 0;
                    for (int i12 = 0; i12 < size; i12++) {
                        TLRPC$Dialog tLRPC$Dialog = dialogs.get(i12);
                        if (!(tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) && tLRPC$Dialog.pinned) {
                            i11++;
                        }
                    }
                    i2 = i11;
                }
                if (!UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium() || LimitReachedBottomSheet.this.isVeryLargeFile) {
                    i2 = i;
                    f = 1.0f;
                } else {
                    i2 = i2 < 0 ? i10 : i2;
                    if (LimitReachedBottomSheet.this.type != 7) {
                        f2 = i2;
                        f3 = i;
                    } else if (i2 > i10) {
                        f2 = i2 - i10;
                        f3 = i - i10;
                    } else {
                        f = 0.5f;
                    }
                    f = f2 / f3;
                }
                float f4 = i10 / i;
                i4 = LimitReachedBottomSheet.this.type;
                if (i4 == 18 && i4 != 20 && i4 != 24) {
                    if (i4 != 25 && i4 != 22 && i4 != 23 && i4 != 19) {
                        if (i4 != 21 && i4 != 26) {
                            if (i4 != 27) {
                                z2 = false;
                                LimitPreviewView limitPreviewView = new LimitPreviewView(context, i8, !z2 ? 0 : i2, i, f4, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider, LimitReachedBottomSheet.this) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.HeaderView.1
                                    @Override // android.view.View
                                    public void invalidate() {
                                        if (LimitReachedBottomSheet.this.lockInvalidation) {
                                            return;
                                        }
                                        super.invalidate();
                                    }
                                };
                                LimitReachedBottomSheet.this.limitPreviewView = limitPreviewView;
                                if (!z2) {
                                    if (LimitReachedBottomSheet.this.boostsStatus != null) {
                                        LimitReachedBottomSheet.this.limitPreviewView.setBoosts(LimitReachedBottomSheet.this.boostsStatus, LimitReachedBottomSheet.this.canApplyBoost != null && LimitReachedBottomSheet.this.canApplyBoost.boostedNow);
                                    }
                                } else {
                                    limitPreviewView.setBagePosition(f);
                                    LimitReachedBottomSheet.this.limitPreviewView.setType(LimitReachedBottomSheet.this.type);
                                    LimitReachedBottomSheet.this.limitPreviewView.defaultCount.setVisibility(8);
                                    if (!z) {
                                        if (UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium() || LimitReachedBottomSheet.this.isVeryLargeFile) {
                                            LimitReachedBottomSheet.this.limitPreviewView.premiumCount.setVisibility(8);
                                            if (LimitReachedBottomSheet.this.type == 6) {
                                                LimitReachedBottomSheet.this.limitPreviewView.defaultCount.setText("2 GB");
                                            } else {
                                                LimitReachedBottomSheet.this.limitPreviewView.defaultCount.setText(Integer.toString(i10));
                                            }
                                            LimitReachedBottomSheet.this.limitPreviewView.defaultCount.setVisibility(0);
                                        }
                                    } else {
                                        LimitReachedBottomSheet.this.limitPreviewView.setPremiumLocked();
                                    }
                                }
                                i5 = LimitReachedBottomSheet.this.type;
                                if (i5 != 2 || i5 == 5) {
                                    LimitReachedBottomSheet.this.limitPreviewView.setDelayedAnimation();
                                }
                                addView(LimitReachedBottomSheet.this.limitPreviewView, LayoutHelper.createLinear(-1, -2, 0.0f, 0, -4, 0, -4, 0));
                                TextView textView = new TextView(context);
                                this.title = textView;
                                textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                                i6 = LimitReachedBottomSheet.this.type;
                                if (i6 != 19) {
                                    this.title.setText(LimitReachedBottomSheet.this.getBoostsTitleString());
                                    i7 = 18;
                                } else {
                                    i7 = 18;
                                    if (i6 == 18) {
                                        if (LimitReachedBottomSheet.this.boostsStatus.level == 0) {
                                            this.title.setText(LocaleController.getString("BoostingEnableStories", R.string.BoostingEnableStories));
                                        } else {
                                            this.title.setText(LocaleController.getString("BoostingIncreaseLevel", R.string.BoostingIncreaseLevel));
                                        }
                                    } else if (i6 == 21) {
                                        this.title.setText(LocaleController.getString(R.string.ReactionCustomReactions));
                                    } else if (i6 == 20) {
                                        this.title.setText(LocaleController.getString(R.string.BoostingEnableColor));
                                    } else if (i6 == 24) {
                                        this.title.setText(LocaleController.getString(R.string.BoostingEnableProfileColor));
                                    } else if (i6 == 26) {
                                        this.title.setText(LocaleController.getString(R.string.BoostingEnableLinkIcon));
                                    } else if (i6 == 27) {
                                        this.title.setText(LocaleController.getString(R.string.BoostingEnableProfileIcon));
                                    } else if (i6 == 25) {
                                        this.title.setText(LocaleController.getString(R.string.BoostingEnableEmojiStatus));
                                    } else {
                                        if (i6 == 22 || i6 == 23) {
                                            this.title.setText(LocaleController.getString(R.string.BoostingEnableWallpaper));
                                        } else if (i6 == 11) {
                                            if (LimitReachedBottomSheet.this.canSendLink) {
                                                this.title.setText(LocaleController.getString("ChannelInviteViaLink", R.string.ChannelInviteViaLink));
                                            } else {
                                                this.title.setText(LocaleController.getString("ChannelInviteViaLinkRestricted", R.string.ChannelInviteViaLinkRestricted));
                                            }
                                        } else if (i6 == 6) {
                                            this.title.setText(LocaleController.getString("FileTooLarge", R.string.FileTooLarge));
                                        } else {
                                            this.title.setText(LocaleController.getString("LimitReached", R.string.LimitReached));
                                        }
                                        this.title.setTextSize(1, 20.0f);
                                        TextView textView2 = this.title;
                                        int i13 = Theme.key_windowBackgroundWhiteBlackText;
                                        textView2.setTextColor(Theme.getColor(i13, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                        this.title.setGravity(17);
                                        if (LimitReachedBottomSheet.this.type == 19) {
                                            BoostCounterView boostCounterView = new BoostCounterView(context, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                                            this.boostCounterView = boostCounterView;
                                            boostCounterView.setCount(LimitReachedBottomSheet.this.canApplyBoost.boostCount, false);
                                            if (LimitReachedBottomSheet.this.isCurrentChat) {
                                                LinearLayout linearLayout = new LinearLayout(context);
                                                this.titleLinearLayout = linearLayout;
                                                linearLayout.setOrientation(0);
                                                this.titleLinearLayout.setWeightSum(1.0f);
                                                this.titleLinearLayout.addView(this.title, LayoutHelper.createLinear(-2, -2, 1.0f, 0));
                                                this.titleLinearLayout.addView(this.boostCounterView, LayoutHelper.createLinear(-2, -2, 48, 0, 2, 0, 0));
                                                addView(this.titleLinearLayout, LayoutHelper.createLinear(-2, -2, 1, 12, z ? 8 : 22, 12, 9));
                                            } else {
                                                addView(this.title, LayoutHelper.createLinear(-2, -2, 1, 0, z ? 8 : 22, 0, 0));
                                                LinearLayout linearLayout2 = new LinearLayout(getContext());
                                                linearLayout2.setOrientation(0);
                                                linearLayout2.setClipChildren(false);
                                                FrameLayout frameLayout = new FrameLayout(getContext());
                                                frameLayout.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(14.0f), Theme.getColor(Theme.key_windowBackgroundGray, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider)));
                                                BackupImageView backupImageView = new BackupImageView(getContext());
                                                backupImageView.setRoundRadius(AndroidUtilities.dp(14.0f));
                                                TLRPC$Chat chat = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getChat(Long.valueOf(-LimitReachedBottomSheet.this.dialogId));
                                                AvatarDrawable avatarDrawable = new AvatarDrawable();
                                                avatarDrawable.setInfo(((BottomSheet) LimitReachedBottomSheet.this).currentAccount, chat);
                                                backupImageView.setForUserOrChat(chat, avatarDrawable);
                                                frameLayout.addView(backupImageView, LayoutHelper.createFrame(28, 28.0f));
                                                TextView textView3 = new TextView(getContext());
                                                if (chat != null) {
                                                    textView3.setText(chat.title);
                                                }
                                                textView3.setSingleLine(true);
                                                textView3.setMaxLines(1);
                                                textView3.setEllipsize(TextUtils.TruncateAt.END);
                                                textView3.setTextSize(1, 13.0f);
                                                textView3.setTextColor(Theme.getColor(i13, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                                frameLayout.addView(textView3, LayoutHelper.createFrame(-2, -2.0f, 16, 36.0f, 0.0f, 12.0f, 0.0f));
                                                linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-2, 28, 80, 18, 0, 18, 0));
                                                LayoutTransition layoutTransition = new LayoutTransition();
                                                layoutTransition.setDuration(100L);
                                                layoutTransition.enableTransitionType(4);
                                                linearLayout2.setLayoutTransition(layoutTransition);
                                                linearLayout2.addView(this.boostCounterView, LayoutHelper.createLinear(-2, -2, 48, -30, 2, 18, 0));
                                                addView(linearLayout2, LayoutHelper.createLinear(-2, 38, 17, 0, -4, 0, 12));
                                                ScaleStateListAnimator.apply(linearLayout2);
                                                linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$HeaderView$$ExternalSyntheticLambda0
                                                    @Override // android.view.View.OnClickListener
                                                    public final void onClick(View view) {
                                                        LimitReachedBottomSheet.HeaderView.this.lambda$new$0(view);
                                                    }
                                                });
                                            }
                                        } else {
                                            addView(this.title, LayoutHelper.createLinear(-2, -2, 1, 0, z ? 8 : 22, 0, 10));
                                        }
                                        TextView textView4 = new TextView(context);
                                        this.description = textView4;
                                        textView4.setText(AndroidUtilities.replaceTags(str2));
                                        this.description.setTextSize(1, 14.0f);
                                        this.description.setGravity(1);
                                        TextView textView5 = this.description;
                                        textView5.setLineSpacing(textView5.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
                                        if (LimitReachedBottomSheet.this.type == i7) {
                                            this.description.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                        } else {
                                            this.description.setTextColor(Theme.getColor(i13, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                        }
                                        if (LimitReachedBottomSheet.this.type == 19) {
                                            addView(this.description, LayoutHelper.createLinear(-2, -2, 1, 24, -2, 24, 17));
                                        } else {
                                            addView(this.description, LayoutHelper.createLinear(-2, -2, 1, 24, 0, 24, 24));
                                        }
                                        LimitReachedBottomSheet.this.updatePremiumButtonText();
                                    }
                                }
                                this.title.setTextSize(1, 20.0f);
                                TextView textView22 = this.title;
                                int i132 = Theme.key_windowBackgroundWhiteBlackText;
                                textView22.setTextColor(Theme.getColor(i132, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                this.title.setGravity(17);
                                if (LimitReachedBottomSheet.this.type == 19) {
                                }
                                TextView textView42 = new TextView(context);
                                this.description = textView42;
                                textView42.setText(AndroidUtilities.replaceTags(str2));
                                this.description.setTextSize(1, 14.0f);
                                this.description.setGravity(1);
                                TextView textView52 = this.description;
                                textView52.setLineSpacing(textView52.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
                                if (LimitReachedBottomSheet.this.type == i7) {
                                }
                                if (LimitReachedBottomSheet.this.type == 19) {
                                }
                                LimitReachedBottomSheet.this.updatePremiumButtonText();
                            }
                        }
                        z2 = true;
                        LimitPreviewView limitPreviewView2 = new LimitPreviewView(context, i8, !z2 ? 0 : i2, i, f4, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider, LimitReachedBottomSheet.this) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.HeaderView.1
                            @Override // android.view.View
                            public void invalidate() {
                                if (LimitReachedBottomSheet.this.lockInvalidation) {
                                    return;
                                }
                                super.invalidate();
                            }
                        };
                        LimitReachedBottomSheet.this.limitPreviewView = limitPreviewView2;
                        if (!z2) {
                        }
                        i5 = LimitReachedBottomSheet.this.type;
                        if (i5 != 2) {
                        }
                        LimitReachedBottomSheet.this.limitPreviewView.setDelayedAnimation();
                        addView(LimitReachedBottomSheet.this.limitPreviewView, LayoutHelper.createLinear(-1, -2, 0.0f, 0, -4, 0, -4, 0));
                        TextView textView6 = new TextView(context);
                        this.title = textView6;
                        textView6.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        i6 = LimitReachedBottomSheet.this.type;
                        if (i6 != 19) {
                        }
                        this.title.setTextSize(1, 20.0f);
                        TextView textView222 = this.title;
                        int i1322 = Theme.key_windowBackgroundWhiteBlackText;
                        textView222.setTextColor(Theme.getColor(i1322, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                        this.title.setGravity(17);
                        if (LimitReachedBottomSheet.this.type == 19) {
                        }
                        TextView textView422 = new TextView(context);
                        this.description = textView422;
                        textView422.setText(AndroidUtilities.replaceTags(str2));
                        this.description.setTextSize(1, 14.0f);
                        this.description.setGravity(1);
                        TextView textView522 = this.description;
                        textView522.setLineSpacing(textView522.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
                        if (LimitReachedBottomSheet.this.type == i7) {
                        }
                        if (LimitReachedBottomSheet.this.type == 19) {
                        }
                        LimitReachedBottomSheet.this.updatePremiumButtonText();
                    }
                }
                z2 = true;
                LimitPreviewView limitPreviewView22 = new LimitPreviewView(context, i8, !z2 ? 0 : i2, i, f4, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider, LimitReachedBottomSheet.this) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.HeaderView.1
                    @Override // android.view.View
                    public void invalidate() {
                        if (LimitReachedBottomSheet.this.lockInvalidation) {
                            return;
                        }
                        super.invalidate();
                    }
                };
                LimitReachedBottomSheet.this.limitPreviewView = limitPreviewView22;
                if (!z2) {
                }
                i5 = LimitReachedBottomSheet.this.type;
                if (i5 != 2) {
                }
                LimitReachedBottomSheet.this.limitPreviewView.setDelayedAnimation();
                addView(LimitReachedBottomSheet.this.limitPreviewView, LayoutHelper.createLinear(-1, -2, 0.0f, 0, -4, 0, -4, 0));
                TextView textView62 = new TextView(context);
                this.title = textView62;
                textView62.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                i6 = LimitReachedBottomSheet.this.type;
                if (i6 != 19) {
                }
                this.title.setTextSize(1, 20.0f);
                TextView textView2222 = this.title;
                int i13222 = Theme.key_windowBackgroundWhiteBlackText;
                textView2222.setTextColor(Theme.getColor(i13222, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                this.title.setGravity(17);
                if (LimitReachedBottomSheet.this.type == 19) {
                }
                TextView textView4222 = new TextView(context);
                this.description = textView4222;
                textView4222.setText(AndroidUtilities.replaceTags(str2));
                this.description.setTextSize(1, 14.0f);
                this.description.setGravity(1);
                TextView textView5222 = this.description;
                textView5222.setLineSpacing(textView5222.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
                if (LimitReachedBottomSheet.this.type == i7) {
                }
                if (LimitReachedBottomSheet.this.type == 19) {
                }
                LimitReachedBottomSheet.this.updatePremiumButtonText();
            } else {
                str = z3 ? LimitReachedBottomSheet.this.limitParams.descriptionStrLocked : (UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium() || LimitReachedBottomSheet.this.isVeryLargeFile) ? LimitReachedBottomSheet.this.limitParams.descriptionStrPremium : LimitReachedBottomSheet.this.limitParams.descriptionStr;
            }
            z = z3;
            str2 = str;
            LimitParams limitParams22 = LimitReachedBottomSheet.this.limitParams;
            int i102 = limitParams22.defaultLimit;
            i = limitParams22.premiumLimit;
            i2 = LimitReachedBottomSheet.this.currentValue;
            i3 = LimitReachedBottomSheet.this.type;
            if (i3 != 3) {
            }
            if (LimitReachedBottomSheet.this.type == 0) {
            }
            if (UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium()) {
            }
            i2 = i;
            f = 1.0f;
            float f42 = i102 / i;
            i4 = LimitReachedBottomSheet.this.type;
            if (i4 == 18) {
            }
            z2 = true;
            LimitPreviewView limitPreviewView222 = new LimitPreviewView(context, i8, !z2 ? 0 : i2, i, f42, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider, LimitReachedBottomSheet.this) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.HeaderView.1
                @Override // android.view.View
                public void invalidate() {
                    if (LimitReachedBottomSheet.this.lockInvalidation) {
                        return;
                    }
                    super.invalidate();
                }
            };
            LimitReachedBottomSheet.this.limitPreviewView = limitPreviewView222;
            if (!z2) {
            }
            i5 = LimitReachedBottomSheet.this.type;
            if (i5 != 2) {
            }
            LimitReachedBottomSheet.this.limitPreviewView.setDelayedAnimation();
            addView(LimitReachedBottomSheet.this.limitPreviewView, LayoutHelper.createLinear(-1, -2, 0.0f, 0, -4, 0, -4, 0));
            TextView textView622 = new TextView(context);
            this.title = textView622;
            textView622.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            i6 = LimitReachedBottomSheet.this.type;
            if (i6 != 19) {
            }
            this.title.setTextSize(1, 20.0f);
            TextView textView22222 = this.title;
            int i132222 = Theme.key_windowBackgroundWhiteBlackText;
            textView22222.setTextColor(Theme.getColor(i132222, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
            this.title.setGravity(17);
            if (LimitReachedBottomSheet.this.type == 19) {
            }
            TextView textView42222 = new TextView(context);
            this.description = textView42222;
            textView42222.setText(AndroidUtilities.replaceTags(str2));
            this.description.setTextSize(1, 14.0f);
            this.description.setGravity(1);
            TextView textView52222 = this.description;
            textView52222.setLineSpacing(textView52222.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
            if (LimitReachedBottomSheet.this.type == i7) {
            }
            if (LimitReachedBottomSheet.this.type == 19) {
            }
            LimitReachedBottomSheet.this.updatePremiumButtonText();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            LimitReachedBottomSheet.this.getBaseFragment().presentFragment(ChatActivity.of(LimitReachedBottomSheet.this.dialogId));
            LimitReachedBottomSheet.this.dismiss();
        }

        public void recreateTitleAndDescription() {
            int indexOfChild = indexOfChild(this.description);
            if (LimitReachedBottomSheet.this.isCurrentChat) {
                int indexOfChild2 = indexOfChild(this.titleLinearLayout);
                removeView(this.titleLinearLayout);
                this.titleLinearLayout.removeView(this.title);
                this.titleLinearLayout.removeView(this.boostCounterView);
                LinearLayout linearLayout = new LinearLayout(getContext());
                this.titleLinearLayout = linearLayout;
                linearLayout.setOrientation(0);
                this.titleLinearLayout.setWeightSum(1.0f);
                this.titleLinearLayout.addView(this.title, LayoutHelper.createLinear(-2, -2, 1.0f, 0));
                this.titleLinearLayout.addView(this.boostCounterView, LayoutHelper.createLinear(-2, -2, 48, 0, 2, 0, 0));
                addView(this.titleLinearLayout, indexOfChild2, LayoutHelper.createLinear(-2, -2, 1, 25, 22, 12, 9));
            } else {
                int indexOfChild3 = indexOfChild(this.title);
                removeView(this.title);
                TextView textView = new TextView(getContext());
                this.title = textView;
                textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.title.setTextSize(1, 20.0f);
                this.title.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                this.title.setGravity(17);
                addView(this.title, indexOfChild3, LayoutHelper.createLinear(-2, -2, 1, 0, 22, 0, 0));
            }
            removeView(this.description);
            TextView textView2 = new TextView(getContext());
            this.description = textView2;
            textView2.setTextSize(1, 14.0f);
            TextView textView3 = this.description;
            textView3.setLineSpacing(textView3.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
            this.description.setGravity(1);
            this.description.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
            addView(this.description, indexOfChild, LayoutHelper.createLinear(-2, -2, 1, 24, -2, 24, 17));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBoostsTitleString() {
        TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = this.boostsStatus;
        if (tL_stories$TL_premium_boostsStatus.next_level_boosts == 0) {
            return LocaleController.formatString("BoostsMaxLevelReached", R.string.BoostsMaxLevelReached, new Object[0]);
        }
        if (tL_stories$TL_premium_boostsStatus.level > 0 && !this.canApplyBoost.alreadyActive) {
            return LocaleController.getString(R.string.BoostChannel);
        }
        if (this.isCurrentChat) {
            return this.canApplyBoost.alreadyActive ? LocaleController.formatString("YouBoostedChannel2", R.string.YouBoostedChannel2, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId)).title) : LocaleController.getString(R.string.BoostChannel);
        } else if (this.canApplyBoost.alreadyActive) {
            return LocaleController.getString("YouBoostedChannel", R.string.YouBoostedChannel);
        } else {
            return LocaleController.getString("BoostingEnableStoriesForChannel", R.string.BoostingEnableStoriesForChannel);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBoostsDescriptionString() {
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
        String string = chat == null ? LocaleController.getString(R.string.AccDescrChannel) : chat.title;
        TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = this.boostsStatus;
        int i = tL_stories$TL_premium_boostsStatus.boosts;
        if ((i == tL_stories$TL_premium_boostsStatus.current_level_boosts) && this.canApplyBoost.alreadyActive) {
            int i2 = tL_stories$TL_premium_boostsStatus.level;
            if (i2 == 1) {
                return LocaleController.formatString("ChannelBoostsJustReachedLevel1", R.string.ChannelBoostsJustReachedLevel1, new Object[0]);
            }
            return LocaleController.formatString("ChannelBoostsJustReachedLevelNext", R.string.ChannelBoostsJustReachedLevelNext, Integer.valueOf(i2), LocaleController.formatPluralString("BoostStories", this.boostsStatus.level, new Object[0]));
        } else if (this.canApplyBoost.alreadyActive) {
            int i3 = tL_stories$TL_premium_boostsStatus.level;
            if (i3 == 0) {
                int i4 = R.string.ChannelNeedBoostsDescriptionForNewFeatures;
                int i5 = tL_stories$TL_premium_boostsStatus.next_level_boosts;
                return LocaleController.formatString(i4, string, LocaleController.formatPluralString("MoreBoosts", i5 - i, Integer.valueOf(i5 - i)));
            }
            int i6 = tL_stories$TL_premium_boostsStatus.next_level_boosts;
            if (i6 == 0) {
                return LocaleController.formatString("ChannelBoostsJustReachedLevelNext", R.string.ChannelBoostsJustReachedLevelNext, Integer.valueOf(i3), LocaleController.formatPluralString("BoostStories", this.boostsStatus.level + 1, new Object[0]));
            }
            return LocaleController.formatString(R.string.ChannelNeedBoostsDescriptionForNewFeatures, string, LocaleController.formatPluralString("MoreBoosts", i6 - i, Integer.valueOf(i6 - i)));
        } else {
            int i7 = tL_stories$TL_premium_boostsStatus.level;
            if (i7 == 0) {
                int i8 = R.string.ChannelNeedBoostsDescriptionForNewFeatures;
                int i9 = tL_stories$TL_premium_boostsStatus.next_level_boosts;
                return LocaleController.formatString(i8, string, LocaleController.formatPluralString("MoreBoosts", i9 - i, Integer.valueOf(i9 - i)));
            }
            int i10 = tL_stories$TL_premium_boostsStatus.next_level_boosts;
            if (i10 == 0) {
                return LocaleController.formatString("ChannelBoostsJustReachedLevelNext", R.string.ChannelBoostsJustReachedLevelNext, Integer.valueOf(i7), LocaleController.formatPluralString("BoostStories", this.boostsStatus.level + 1, new Object[0]));
            }
            return LocaleController.formatString(R.string.ChannelNeedBoostsDescriptionForNewFeatures, string, LocaleController.formatPluralString("MoreBoosts", i10 - i, Integer.valueOf(i10 - i)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LimitParams getLimitParams(int i, int i2) {
        LimitParams limitParams = new LimitParams();
        if (i == 0) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).dialogFiltersPinnedLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).dialogFiltersPinnedLimitPremium;
            limitParams.icon = R.drawable.msg_limit_pin;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedPinDialogs", R.string.LimitReachedPinDialogs, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedPinDialogsPremium", R.string.LimitReachedPinDialogsPremium, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedPinDialogsLocked", R.string.LimitReachedPinDialogsLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 2) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).publicLinksLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).publicLinksLimitPremium;
            limitParams.icon = R.drawable.msg_limit_links;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedPublicLinks", R.string.LimitReachedPublicLinks, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedPublicLinksPremium", R.string.LimitReachedPublicLinksPremium, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedPublicLinksLocked", R.string.LimitReachedPublicLinksLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 12) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).chatlistInvitesLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).chatlistInvitesLimitPremium;
            limitParams.icon = R.drawable.msg_limit_links;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedFolderLinks", R.string.LimitReachedFolderLinks, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedFolderLinksPremium", R.string.LimitReachedFolderLinksPremium, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedFolderLinksLocked", R.string.LimitReachedFolderLinksLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 13) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).chatlistJoinedLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).chatlistJoinedLimitPremium;
            limitParams.icon = R.drawable.msg_limit_folder;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedSharedFolders", R.string.LimitReachedSharedFolders, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedSharedFoldersPremium", R.string.LimitReachedSharedFoldersPremium, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedSharedFoldersLocked", R.string.LimitReachedSharedFoldersLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 3) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).dialogFiltersLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).dialogFiltersLimitPremium;
            limitParams.icon = R.drawable.msg_limit_folder;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedFolders", R.string.LimitReachedFolders, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedFoldersPremium", R.string.LimitReachedFoldersPremium, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedFoldersLocked", R.string.LimitReachedFoldersLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 4) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).dialogFiltersChatsLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).dialogFiltersChatsLimitPremium;
            limitParams.icon = R.drawable.msg_limit_chats;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedChatInFolders", R.string.LimitReachedChatInFolders, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedChatInFoldersPremium", R.string.LimitReachedChatInFoldersPremium, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedChatInFoldersLocked", R.string.LimitReachedChatInFoldersLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 5) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).channelsLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).channelsLimitPremium;
            limitParams.icon = R.drawable.msg_limit_groups;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedCommunities", R.string.LimitReachedCommunities, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedCommunitiesPremium", R.string.LimitReachedCommunitiesPremium, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedCommunitiesLocked", R.string.LimitReachedCommunitiesLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 6) {
            limitParams.defaultLimit = 100;
            limitParams.premiumLimit = 200;
            limitParams.icon = R.drawable.msg_limit_folder;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedFileSize", R.string.LimitReachedFileSize, "2 GB", "4 GB");
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedFileSizePremium", R.string.LimitReachedFileSizePremium, "4 GB");
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedFileSizeLocked", R.string.LimitReachedFileSizeLocked, "2 GB");
        } else if (i == 7) {
            limitParams.defaultLimit = 3;
            limitParams.premiumLimit = 4;
            limitParams.icon = R.drawable.msg_limit_accounts;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedAccounts", R.string.LimitReachedAccounts, 3, Integer.valueOf(limitParams.premiumLimit));
            int i3 = R.string.LimitReachedAccountsPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedAccountsPremium", i3, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedAccountsPremium", i3, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 11) {
            limitParams.defaultLimit = 0;
            limitParams.premiumLimit = 0;
            limitParams.icon = R.drawable.msg_limit_links;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedAccounts", R.string.LimitReachedAccounts, 0, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = "";
            limitParams.descriptionStrLocked = "";
        } else if (i == 14) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).storyExpiringLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).storyExpiringLimitPremium;
            limitParams.icon = R.drawable.msg_limit_stories;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesCount", R.string.LimitReachedStoriesCount, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            int i4 = R.string.LimitReachedStoriesCountPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesCountPremium", i4, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedStoriesCountPremium", i4, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 15) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).storiesSentWeeklyLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).storiesSentWeeklyLimitPremium;
            limitParams.icon = R.drawable.msg_limit_stories;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesWeekly", R.string.LimitReachedStoriesWeekly, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            int i5 = R.string.LimitReachedStoriesWeeklyPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesWeeklyPremium", i5, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedStoriesWeeklyPremium", i5, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 16) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitPremium;
            limitParams.icon = R.drawable.msg_limit_stories;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesMonthly", R.string.LimitReachedStoriesMonthly, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            int i6 = R.string.LimitReachedStoriesMonthlyPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i6, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i6, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 18 || i == 20 || i == 24 || i == 26 || i == 27 || i == 25 || i == 22 || i == 23 || i == 19 || i == 21) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitPremium;
            limitParams.icon = R.drawable.filled_limit_boost;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesMonthly", R.string.LimitReachedStoriesMonthly, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            int i7 = R.string.LimitReachedStoriesMonthlyPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i7, Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrLocked = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i7, Integer.valueOf(limitParams.defaultLimit));
        }
        return limitParams;
    }

    private void loadAdminedChannels() {
        this.loading = true;
        updateRows();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_channels_getAdminedPublicChannels(), new RequestDelegate() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda18
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LimitReachedBottomSheet.this.lambda$loadAdminedChannels$17(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAdminedChannels$17(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                LimitReachedBottomSheet.this.lambda$loadAdminedChannels$16(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAdminedChannels$16(TLObject tLObject) {
        int i;
        if (tLObject != null) {
            this.chats.clear();
            this.chats.addAll(((TLRPC$TL_messages_chats) tLObject).chats);
            this.loading = false;
            this.enterAnimator.showItemsAnimated(this.chatsTitleRow + 4);
            int i2 = 0;
            while (true) {
                if (i2 >= this.recyclerListView.getChildCount()) {
                    i = 0;
                    break;
                } else if (this.recyclerListView.getChildAt(i2) instanceof HeaderView) {
                    i = this.recyclerListView.getChildAt(i2).getTop();
                    break;
                } else {
                    i2++;
                }
            }
            updateRows();
            if (this.headerRow >= 0 && i != 0) {
                ((LinearLayoutManager) this.recyclerListView.getLayoutManager()).scrollToPositionWithOffset(this.headerRow + 1, i);
            }
        }
        int max = Math.max(this.chats.size(), this.limitParams.defaultLimit);
        this.limitPreviewView.setIconValue(max, false);
        this.limitPreviewView.setBagePosition(max / this.limitParams.premiumLimit);
        this.limitPreviewView.startDelayedAnimation();
    }

    private void updateRows() {
        this.rowCount = 0;
        this.dividerRow = -1;
        this.chatStartRow = -1;
        this.chatEndRow = -1;
        this.loadingRow = -1;
        this.linkRow = -1;
        this.emptyViewDividerRow = -1;
        this.boostFeaturesStartRow = -1;
        int i = 0 + 1;
        this.rowCount = i;
        this.headerRow = 0;
        int i2 = this.type;
        if (i2 == 19 || i2 == 18 || i2 == 20 || i2 == 24 || i2 == 26 || i2 == 27 || i2 == 22 || i2 == 23 || i2 == 25 || i2 == 21) {
            if (i2 != 19) {
                this.topPadding = 0.24f;
                this.rowCount = i + 1;
                this.linkRow = i;
                if (MessagesController.getInstance(this.currentAccount).giveawayGiftsPurchaseAvailable) {
                    int i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.bottomRow = i3;
                }
            }
            setupBoostFeatures();
            int i4 = this.rowCount;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.boostFeaturesStartRow = i4;
            this.rowCount = i5 + (this.boostFeatures.size() - 1);
        } else if (!hasFixedSize(i2)) {
            int i6 = this.rowCount;
            int i7 = i6 + 1;
            this.rowCount = i7;
            this.dividerRow = i6;
            int i8 = i7 + 1;
            this.rowCount = i8;
            this.chatsTitleRow = i7;
            if (this.loading) {
                this.rowCount = i8 + 1;
                this.loadingRow = i8;
            } else {
                this.chatStartRow = i8;
                int i9 = this.type;
                if (i9 == 11) {
                    this.rowCount = i8 + this.restrictedUsers.size();
                } else if (i9 == 5) {
                    this.rowCount = i8 + this.inactiveChats.size();
                } else {
                    this.rowCount = i8 + this.chats.size();
                }
                int i10 = this.rowCount;
                this.chatEndRow = i10;
                if (i10 - this.chatStartRow > 1) {
                    this.rowCount = i10 + 1;
                    this.emptyViewDividerRow = i10;
                }
            }
        }
        notifyDataSetChanged();
    }

    private void revokeSelectedLinks() {
        ArrayList<TLRPC$Chat> arrayList = new ArrayList<>();
        Iterator<Object> it = this.selectedChats.iterator();
        while (it.hasNext()) {
            this.chats.add((TLRPC$Chat) it.next());
        }
        revokeLinks(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeLinks(final ArrayList<TLRPC$Chat> arrayList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.formatPluralString("RevokeLinks", arrayList.size(), new Object[0]));
        if (arrayList.size() == 1) {
            TLRPC$Chat tLRPC$Chat = arrayList.get(0);
            if (this.parentIsChannel) {
                int i = R.string.RevokeLinkAlertChannel;
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlertChannel", i, MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + ChatObject.getPublicUsername(tLRPC$Chat), tLRPC$Chat.title)));
            } else {
                int i2 = R.string.RevokeLinkAlert;
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlert", i2, MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + ChatObject.getPublicUsername(tLRPC$Chat), tLRPC$Chat.title)));
            }
        } else if (this.parentIsChannel) {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinksAlertChannel", R.string.RevokeLinksAlertChannel, new Object[0])));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinksAlert", R.string.RevokeLinksAlert, new Object[0])));
        }
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("RevokeButton", R.string.RevokeButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                LimitReachedBottomSheet.this.lambda$revokeLinks$19(arrayList, dialogInterface, i3);
            }
        });
        AlertDialog create = builder.create();
        create.show();
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold, this.resourcesProvider));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeLinks$19(ArrayList arrayList, DialogInterface dialogInterface, int i) {
        dismiss();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC$TL_channels_updateUsername tLRPC$TL_channels_updateUsername = new TLRPC$TL_channels_updateUsername();
            tLRPC$TL_channels_updateUsername.channel = MessagesController.getInputChannel((TLRPC$Chat) arrayList.get(i2));
            tLRPC$TL_channels_updateUsername.username = "";
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_updateUsername, new RequestDelegate() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda17
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LimitReachedBottomSheet.this.lambda$revokeLinks$18(tLObject, tLRPC$TL_error);
                }
            }, 64);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeLinks$18(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            AndroidUtilities.runOnUIThread(this.onSuccessRunnable);
        }
    }

    private void loadInactiveChannels() {
        this.loading = true;
        updateRows();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_channels_getInactiveChannels(), new RequestDelegate() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda19
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LimitReachedBottomSheet.this.lambda$loadInactiveChannels$21(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInactiveChannels$21(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        String formatPluralString;
        if (tLRPC$TL_error == null) {
            final TLRPC$TL_messages_inactiveChats tLRPC$TL_messages_inactiveChats = (TLRPC$TL_messages_inactiveChats) tLObject;
            final ArrayList arrayList = new ArrayList();
            final int min = Math.min(tLRPC$TL_messages_inactiveChats.chats.size(), tLRPC$TL_messages_inactiveChats.dates.size());
            for (int i = 0; i < min; i++) {
                TLRPC$Chat tLRPC$Chat = tLRPC$TL_messages_inactiveChats.chats.get(i);
                int currentTime = (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - tLRPC$TL_messages_inactiveChats.dates.get(i).intValue()) / 86400;
                if (currentTime < 30) {
                    formatPluralString = LocaleController.formatPluralString("Days", currentTime, new Object[0]);
                } else if (currentTime < 365) {
                    formatPluralString = LocaleController.formatPluralString("Months", currentTime / 30, new Object[0]);
                } else {
                    formatPluralString = LocaleController.formatPluralString("Years", currentTime / 365, new Object[0]);
                }
                if (ChatObject.isMegagroup(tLRPC$Chat)) {
                    arrayList.add(LocaleController.formatString("InactiveChatSignature", R.string.InactiveChatSignature, LocaleController.formatPluralString("Members", tLRPC$Chat.participants_count, new Object[0]), formatPluralString));
                } else if (ChatObject.isChannel(tLRPC$Chat)) {
                    arrayList.add(LocaleController.formatString("InactiveChannelSignature", R.string.InactiveChannelSignature, formatPluralString));
                } else {
                    arrayList.add(LocaleController.formatString("InactiveChatSignature", R.string.InactiveChatSignature, LocaleController.formatPluralString("Members", tLRPC$Chat.participants_count, new Object[0]), formatPluralString));
                }
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    LimitReachedBottomSheet.this.lambda$loadInactiveChannels$20(arrayList, min, tLRPC$TL_messages_inactiveChats);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInactiveChannels$20(ArrayList arrayList, int i, TLRPC$TL_messages_inactiveChats tLRPC$TL_messages_inactiveChats) {
        int i2;
        this.inactiveChatsSignatures.clear();
        this.inactiveChats.clear();
        this.inactiveChatsSignatures.addAll(arrayList);
        for (int i3 = 0; i3 < i; i3++) {
            this.inactiveChats.add(tLRPC$TL_messages_inactiveChats.chats.get(i3));
        }
        this.loading = false;
        this.enterAnimator.showItemsAnimated(this.chatsTitleRow + 4);
        int i4 = 0;
        while (true) {
            if (i4 >= this.recyclerListView.getChildCount()) {
                i2 = 0;
                break;
            } else if (this.recyclerListView.getChildAt(i4) instanceof HeaderView) {
                i2 = this.recyclerListView.getChildAt(i4).getTop();
                break;
            } else {
                i4++;
            }
        }
        updateRows();
        if (this.headerRow >= 0 && i2 != 0) {
            ((LinearLayoutManager) this.recyclerListView.getLayoutManager()).scrollToPositionWithOffset(this.headerRow + 1, i2);
        }
        if (this.limitParams == null) {
            this.limitParams = getLimitParams(this.type, this.currentAccount);
        }
        int max = Math.max(this.inactiveChats.size(), this.limitParams.defaultLimit);
        LimitPreviewView limitPreviewView = this.limitPreviewView;
        if (limitPreviewView != null) {
            limitPreviewView.setIconValue(max, false);
            this.limitPreviewView.setBagePosition(max / this.limitParams.premiumLimit);
            this.limitPreviewView.startDelayedAnimation();
        }
    }

    private void setupBoostFeatures() {
        this.boostFeatures = new ArrayList<>();
        TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = this.boostsStatus;
        int i = 10;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (messagesController != null) {
            MessagesController.PeerColors peerColors = messagesController.peerColors;
            int max = Math.max(10, peerColors != null ? peerColors.maxLevel() : 0);
            MessagesController.PeerColors peerColors2 = messagesController.profilePeerColors;
            i = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(max, peerColors2 != null ? peerColors2.maxLevel() : 0), messagesController.channelBgIconLevelMin), messagesController.channelProfileIconLevelMin), messagesController.channelEmojiStatusLevelMin), messagesController.channelWallpaperLevelMin), messagesController.channelCustomWallpaperLevelMin);
        }
        ArrayList<BoostFeature> arrayList = null;
        for (int i2 = tL_stories$TL_premium_boostsStatus != null ? 1 + tL_stories$TL_premium_boostsStatus.level : 1; i2 <= i; i2++) {
            ArrayList<BoostFeature> boostFeaturesForLevel = boostFeaturesForLevel(i2);
            if (arrayList == null || !BoostFeature.arraysEqual(arrayList, boostFeaturesForLevel)) {
                ArrayList<BoostFeature> arrayList2 = this.boostFeatures;
                arrayList2.add(new BoostFeature.BoostFeatureLevel(i2, arrayList2.isEmpty()));
                this.boostFeatures.addAll(boostFeaturesForLevel);
                arrayList = boostFeaturesForLevel;
            }
        }
    }

    private ArrayList<BoostFeature> boostFeaturesForLevel(int i) {
        ArrayList<BoostFeature> arrayList = new ArrayList<>();
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (messagesController == null) {
            return arrayList;
        }
        arrayList.add(BoostFeature.of(R.drawable.menu_feature_stories, "BoostFeatureStoriesPerDay", i));
        arrayList.add(BoostFeature.of(R.drawable.menu_feature_reactions, "BoostFeatureCustomReaction", i));
        MessagesController.PeerColors peerColors = messagesController.peerColors;
        int colorsAvailable = peerColors != null ? peerColors.colorsAvailable(i) : 0;
        MessagesController.PeerColors peerColors2 = messagesController.profilePeerColors;
        int colorsAvailable2 = peerColors2 != null ? peerColors2.colorsAvailable(i) : 0;
        if (colorsAvailable > 0) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_color_name, "BoostFeatureNameColor", 7));
        }
        if (colorsAvailable > 0) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_links, "BoostFeatureReplyColor", colorsAvailable));
        }
        if (i >= messagesController.channelBgIconLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_links2, R.string.BoostFeatureReplyIcon));
        }
        if (i >= messagesController.channelEmojiStatusLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_status, R.string.BoostFeatureEmojiStatuses, "1000+"));
        }
        if (colorsAvailable2 > 0) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_color_profile, "BoostFeatureProfileColor", colorsAvailable2));
        }
        if (i >= messagesController.channelProfileIconLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_cover, R.string.BoostFeatureProfileIcon));
        }
        if (i >= messagesController.channelWallpaperLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_wallpaper, "BoostFeatureBackground", 8));
        }
        if (i >= messagesController.channelCustomWallpaperLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_custombg, R.string.BoostFeatureCustomBackground));
        }
        return arrayList;
    }

    /* loaded from: classes4.dex */
    private class BoostFeatureCell extends FrameLayout {
        public BoostFeature feature;
        private final ImageView imageView;
        public BoostFeature.BoostFeatureLevel level;
        private final FrameLayout levelLayout;
        private final SimpleTextView levelTextView;
        private final Theme.ResourcesProvider resourcesProvider;
        private final SimpleTextView textView;

        public BoostFeatureCell(LimitReachedBottomSheet limitReachedBottomSheet, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            setPadding(((BottomSheet) limitReachedBottomSheet).backgroundPaddingLeft, 0, ((BottomSheet) limitReachedBottomSheet).backgroundPaddingLeft, 0);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_premiumGradient1, resourcesProvider), PorterDuff.Mode.SRC_IN));
            addView(imageView, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 16, 24.0f, 0.0f, 24.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.textView = simpleTextView;
            simpleTextView.setWidthWrapContent(true);
            simpleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
            simpleTextView.setTextSize(14);
            boolean z = LocaleController.isRTL;
            addView(simpleTextView, LayoutHelper.createFrame(-2, -2.0f, (z ? 5 : 3) | 16, z ? 30.0f : 60.0f, 0.0f, z ? 60.0f : 30.0f, 0.0f));
            SimpleTextView simpleTextView2 = new SimpleTextView(context);
            this.levelTextView = simpleTextView2;
            simpleTextView2.setTextColor(-1);
            simpleTextView2.setWidthWrapContent(true);
            simpleTextView2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            simpleTextView2.setTextSize(14);
            FrameLayout frameLayout = new FrameLayout(context, limitReachedBottomSheet, resourcesProvider) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.BoostFeatureCell.1
                private final Paint dividerPaint;
                private final PremiumGradient.PremiumGradientTools gradientTools;
                final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

                {
                    this.val$resourcesProvider = resourcesProvider;
                    this.gradientTools = new PremiumGradient.PremiumGradientTools(Theme.key_premiumGradient1, Theme.key_premiumGradient2, -1, -1, -1, resourcesProvider);
                    Paint paint = new Paint(1);
                    this.dividerPaint = paint;
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(1.0f);
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    this.dividerPaint.setColor(Theme.getColor(Theme.key_divider, this.val$resourcesProvider));
                    canvas.drawLine(AndroidUtilities.dp(18.0f), getHeight() / 2.0f, BoostFeatureCell.this.levelTextView.getLeft() - AndroidUtilities.dp(20.0f), getHeight() / 2.0f, this.dividerPaint);
                    canvas.drawLine(BoostFeatureCell.this.levelTextView.getRight() + AndroidUtilities.dp(20.0f), getHeight() / 2.0f, getWidth() - AndroidUtilities.dp(18.0f), getHeight() / 2.0f, this.dividerPaint);
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(BoostFeatureCell.this.levelTextView.getLeft() - AndroidUtilities.dp(15.0f), ((BoostFeatureCell.this.levelTextView.getTop() + BoostFeatureCell.this.levelTextView.getBottom()) - AndroidUtilities.dp(30.0f)) / 2.0f, BoostFeatureCell.this.levelTextView.getRight() + AndroidUtilities.dp(15.0f), ((BoostFeatureCell.this.levelTextView.getTop() + BoostFeatureCell.this.levelTextView.getBottom()) + AndroidUtilities.dp(30.0f)) / 2.0f);
                    canvas.save();
                    canvas.translate(rectF.left, rectF.top);
                    rectF.set(0.0f, 0.0f, rectF.width(), rectF.height());
                    this.gradientTools.gradientMatrix(rectF);
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), this.gradientTools.paint);
                    canvas.restore();
                    super.dispatchDraw(canvas);
                }
            };
            this.levelLayout = frameLayout;
            frameLayout.setWillNotDraw(false);
            frameLayout.addView(simpleTextView2, LayoutHelper.createFrame(-2, -2, 17));
            addView(frameLayout, LayoutHelper.createFrame(-1, -1.0f));
        }

        public void set(BoostFeature boostFeature) {
            if (boostFeature instanceof BoostFeature.BoostFeatureLevel) {
                this.level = (BoostFeature.BoostFeatureLevel) boostFeature;
                this.feature = null;
                this.imageView.setVisibility(8);
                this.textView.setVisibility(8);
                this.levelLayout.setVisibility(0);
                SimpleTextView simpleTextView = this.levelTextView;
                BoostFeature.BoostFeatureLevel boostFeatureLevel = this.level;
                simpleTextView.setText(LocaleController.formatPluralString(boostFeatureLevel.isFirst ? "BoostLevelUnlocks" : "BoostLevel", boostFeatureLevel.lvl, new Object[0]));
            } else if (boostFeature != null) {
                this.level = null;
                this.feature = boostFeature;
                this.imageView.setVisibility(0);
                this.imageView.setImageResource(this.feature.iconResId);
                this.textView.setVisibility(0);
                BoostFeature boostFeature2 = this.feature;
                if (boostFeature2.textKeyPlural != null) {
                    String string = LocaleController.getString(this.feature.textKeyPlural + "_" + LocaleController.getStringParamForNumber(this.feature.countPlural));
                    if (string == null || string.startsWith("LOC_ERR")) {
                        string = LocaleController.getString(this.feature.textKeyPlural + "_other");
                    }
                    if (string == null) {
                        string = "";
                    }
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                    int indexOf = string.indexOf("%d");
                    if (indexOf >= 0) {
                        spannableStringBuilder = new SpannableStringBuilder(string);
                        SpannableString spannableString = new SpannableString(this.feature.countPlural + "");
                        spannableString.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, spannableString.length(), 33);
                        spannableStringBuilder.replace(indexOf, indexOf + 2, (CharSequence) spannableString);
                    }
                    this.textView.setText(spannableStringBuilder);
                } else {
                    String string2 = LocaleController.getString(boostFeature2.textKey);
                    String str = string2 != null ? string2 : "";
                    if (this.feature.countValue != null) {
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str);
                        int indexOf2 = str.indexOf("%s");
                        if (indexOf2 >= 0) {
                            spannableStringBuilder2 = new SpannableStringBuilder(str);
                            SpannableString spannableString2 = new SpannableString(this.feature.countValue);
                            spannableString2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, spannableString2.length(), 33);
                            spannableStringBuilder2.replace(indexOf2, indexOf2 + 2, (CharSequence) spannableString2);
                        }
                        this.textView.setText(spannableStringBuilder2);
                    } else {
                        this.textView.setText(str);
                    }
                }
                this.levelLayout.setVisibility(8);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.level != null ? 49.0f : 36.0f), 1073741824));
        }
    }
}
