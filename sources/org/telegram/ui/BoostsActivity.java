package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_payments_checkedGiftCode;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.tl.TL_stories$TL_boost;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsList;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.tgnet.tl.TL_stories$TL_premium_getBoostsList;
import org.telegram.tgnet.tl.TL_stories$TL_prepaidGiveaway;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BoostsActivity;
import org.telegram.ui.Cells.FixedHeightEmptyCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Charts.view_data.ChartHeaderView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.FillLastLinearLayoutManager;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkActionView;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.Premium.LimitPreviewView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.Premium.boosts.BoostPagerBottomSheet;
import org.telegram.ui.Components.Premium.boosts.GiftInfoBottomSheet;
import org.telegram.ui.Components.Premium.boosts.cells.statistics.GiftedUserCell;
import org.telegram.ui.Components.Premium.boosts.cells.statistics.GiveawayCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;
import org.telegram.ui.StatisticActivity;
/* loaded from: classes4.dex */
public class BoostsActivity extends GradientHeaderActivity implements NotificationCenter.NotificationCenterDelegate {
    private TL_stories$TL_premium_boostsStatus boostsStatus;
    private ScrollSlidingTextTabStrip boostsTabs;
    private ChannelBoostsController.CanApplyBoost canApplyBoost;
    private final TLRPC$Chat currentChat;
    private final long dialogId;
    private boolean hasBoostsNext;
    private boolean hasGiftsNext;
    private LimitPreviewView limitPreviewView;
    private int nextBoostRemaining;
    private int nextGiftsRemaining;
    private LinearLayout progressLayout;
    private int totalBoosts;
    private int totalGifts;
    boolean usersLoading;
    int currentAccount = UserConfig.selectedAccount;
    private final ArrayList<TL_stories$TL_boost> boosters = new ArrayList<>();
    private final ArrayList<TL_stories$TL_boost> gifts = new ArrayList<>();
    private final ArrayList<ItemInternal> items = new ArrayList<>();
    private int selectedTab = 0;
    AdapterWithDiffUtils adapter = new AdapterWithDiffUtils() { // from class: org.telegram.ui.BoostsActivity.1
        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return ((ItemInternal) BoostsActivity.this.items.get(viewHolder.getAdapterPosition())).selectable;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View shadowSectionCell;
            FrameLayout frameLayout;
            switch (i) {
                case 0:
                    frameLayout = new StatisticActivity.OverviewCell(BoostsActivity.this.getContext());
                    break;
                case 1:
                case 16:
                    View chartHeaderView = new ChartHeaderView(BoostsActivity.this.getContext());
                    chartHeaderView.setPadding(chartHeaderView.getPaddingLeft(), AndroidUtilities.dp(16.0f), chartHeaderView.getRight(), AndroidUtilities.dp(16.0f));
                    frameLayout = chartHeaderView;
                    break;
                case 2:
                    shadowSectionCell = new ShadowSectionCell(viewGroup.getContext(), 12, Theme.getColor(Theme.key_windowBackgroundGray));
                    frameLayout = shadowSectionCell;
                    break;
                case 3:
                    LinkActionView linkActionView = new LinkActionView(BoostsActivity.this.getContext(), BoostsActivity.this, null, 0L, false, false);
                    linkActionView.hideOptions();
                    linkActionView.setPadding(AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f), AndroidUtilities.dp(24.0f));
                    frameLayout = linkActionView;
                    break;
                case 4:
                default:
                    throw new UnsupportedOperationException();
                case 5:
                    frameLayout = new GiftedUserCell(BoostsActivity.this.getContext(), 0, 0, false);
                    break;
                case 6:
                    shadowSectionCell = new TextInfoPrivacyCell(viewGroup.getContext(), 20, ((BaseFragment) BoostsActivity.this).resourceProvider);
                    frameLayout = shadowSectionCell;
                    break;
                case 7:
                    frameLayout = new FixedHeightEmptyCell(BoostsActivity.this.getContext(), 8);
                    break;
                case 8:
                    FrameLayout frameLayout2 = new FrameLayout(this, BoostsActivity.this.getContext()) { // from class: org.telegram.ui.BoostsActivity.1.4
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
                        }
                    };
                    TextView textView = new TextView(BoostsActivity.this.getContext());
                    textView.setText(LocaleController.getString(BoostsActivity.this.isChannel() ? R.string.NoBoostersHint : R.string.NoBoostersGroupHint));
                    textView.setTextSize(1, 14.0f);
                    textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
                    textView.setGravity(17);
                    frameLayout2.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 16.0f, 0.0f, 0.0f));
                    frameLayout = frameLayout2;
                    break;
                case 9:
                    ManageChatTextCell manageChatTextCell = new ManageChatTextCell(this, BoostsActivity.this.getContext()) { // from class: org.telegram.ui.BoostsActivity.1.5
                        @Override // org.telegram.ui.Cells.ManageChatTextCell
                        protected int getFullHeight() {
                            return AndroidUtilities.dp(50.0f);
                        }
                    };
                    manageChatTextCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                    frameLayout = manageChatTextCell;
                    break;
                case 10:
                    TextCell textCell = new TextCell(BoostsActivity.this.getContext());
                    textCell.setTextAndIcon((CharSequence) LocaleController.formatString("BoostingGetBoostsViaGifts", R.string.BoostingGetBoostsViaGifts, new Object[0]), R.drawable.msg_gift_premium, false);
                    textCell.offsetFromImage = 64;
                    int i2 = Theme.key_windowBackgroundWhiteBlueText4;
                    textCell.setColors(i2, i2);
                    frameLayout = textCell;
                    break;
                case 11:
                    frameLayout = new GiveawayCell(BoostsActivity.this.getContext(), 0, 0, false);
                    break;
                case 12:
                    View chartHeaderView2 = new ChartHeaderView(BoostsActivity.this.getContext());
                    chartHeaderView2.setPadding(chartHeaderView2.getPaddingLeft(), AndroidUtilities.dp(16.0f), chartHeaderView2.getRight(), AndroidUtilities.dp(8.0f));
                    frameLayout = chartHeaderView2;
                    break;
                case 13:
                    BoostsActivity.this.boostsTabs = new ScrollSlidingTextTabStrip(BoostsActivity.this.getContext(), ((BaseFragment) BoostsActivity.this).resourceProvider);
                    BoostsActivity.this.boostsTabs.setColors(Theme.key_profile_tabSelectedLine, Theme.key_profile_tabSelectedText, Theme.key_profile_tabText, Theme.key_profile_tabSelector);
                    FrameLayout frameLayout3 = new FrameLayout(BoostsActivity.this.getContext()) { // from class: org.telegram.ui.BoostsActivity.1.2
                        private final Paint dividerPaint = new Paint(1);

                        @Override // android.view.ViewGroup, android.view.View
                        protected void dispatchDraw(Canvas canvas) {
                            super.dispatchDraw(canvas);
                            this.dividerPaint.setColor(Theme.getColor(Theme.key_windowBackgroundGray, ((BaseFragment) BoostsActivity.this).resourceProvider));
                            canvas.drawRect(0.0f, getHeight() - 2, getWidth(), getHeight(), this.dividerPaint);
                        }
                    };
                    BoostsActivity.this.boostsTabs.setDelegate(new ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate() { // from class: org.telegram.ui.BoostsActivity.1.3
                        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
                        public void onPageScrolled(float f) {
                        }

                        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
                        public void onSamePageSelected() {
                        }

                        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
                        public void onPageSelected(int i3, boolean z) {
                            BoostsActivity.this.selectedTab = i3;
                            BoostsActivity.this.updateRows(true);
                        }
                    });
                    frameLayout3.addView(BoostsActivity.this.boostsTabs, LayoutHelper.createFrame(-2, 48.0f));
                    frameLayout = frameLayout3;
                    break;
                case 14:
                    BoostsActivity boostsActivity = BoostsActivity.this;
                    frameLayout = boostsActivity.getHeader(boostsActivity.getContext());
                    break;
                case 15:
                    View view = new View(BoostsActivity.this.getContext()) { // from class: org.telegram.ui.BoostsActivity.1.1
                        @Override // android.view.View
                        protected void onMeasure(int i3, int i4) {
                            LinearLayoutManager linearLayoutManager = BoostsActivity.this.layoutManager;
                            super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(Math.max(0, linearLayoutManager instanceof FillLastLinearLayoutManager ? ((FillLastLinearLayoutManager) linearLayoutManager).getLastItemHeight() : 0), 1073741824));
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
                    frameLayout = view;
                    break;
            }
            frameLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(frameLayout);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ItemInternal itemInternal;
            String formatString;
            if (viewHolder.getItemViewType() == 4 || viewHolder.getItemViewType() == 14 || viewHolder.getItemViewType() == 15) {
                return;
            }
            if (viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 12 || viewHolder.getItemViewType() == 16) {
                ChartHeaderView chartHeaderView = (ChartHeaderView) viewHolder.itemView;
                chartHeaderView.setTitle(((ItemInternal) BoostsActivity.this.items.get(i)).title);
                chartHeaderView.showDate(false);
                if (viewHolder.getItemViewType() == 12) {
                    chartHeaderView.setPadding(AndroidUtilities.dp(3.0f), chartHeaderView.getPaddingTop(), chartHeaderView.getPaddingRight(), chartHeaderView.getPaddingBottom());
                }
                if (viewHolder.getItemViewType() == 16) {
                    chartHeaderView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), chartHeaderView.getPaddingBottom());
                }
            } else if (viewHolder.getItemViewType() == 0) {
                StatisticActivity.OverviewCell overviewCell = (StatisticActivity.OverviewCell) viewHolder.itemView;
                overviewCell.setData(0, Integer.toString(BoostsActivity.this.boostsStatus.level), null, LocaleController.getString("BoostsLevel2", R.string.BoostsLevel2));
                if (BoostsActivity.this.boostsStatus.premium_audience == null || BoostsActivity.this.boostsStatus.premium_audience.total == 0.0d) {
                    overviewCell.setData(1, "~0", "0%", LocaleController.getString(BoostsActivity.this.isChannel() ? R.string.PremiumSubscribers : R.string.PremiumMembers));
                } else {
                    overviewCell.setData(1, "~" + ((int) BoostsActivity.this.boostsStatus.premium_audience.part), String.format(Locale.US, "%.1f", Float.valueOf((((float) BoostsActivity.this.boostsStatus.premium_audience.part) / ((float) BoostsActivity.this.boostsStatus.premium_audience.total)) * 100.0f)) + "%", LocaleController.getString(BoostsActivity.this.isChannel() ? R.string.PremiumSubscribers : R.string.PremiumMembers));
                }
                overviewCell.setData(2, String.valueOf(BoostsActivity.this.boostsStatus.boosts), null, LocaleController.getString("BoostsExisting", R.string.BoostsExisting));
                overviewCell.setData(3, String.valueOf(Math.max(0, BoostsActivity.this.boostsStatus.next_level_boosts - BoostsActivity.this.boostsStatus.boosts)), null, LocaleController.getString("BoostsToLevel", R.string.BoostsToLevel));
                overviewCell.setPadding(AndroidUtilities.dp(23.0f), overviewCell.getPaddingTop(), AndroidUtilities.dp(23.0f), overviewCell.getPaddingBottom());
            } else if (viewHolder.getItemViewType() == 5) {
                TL_stories$TL_boost tL_stories$TL_boost = ((ItemInternal) BoostsActivity.this.items.get(i)).booster;
                TLRPC$User user = MessagesController.getInstance(BoostsActivity.this.currentAccount).getUser(Long.valueOf(tL_stories$TL_boost.user_id));
                GiftedUserCell giftedUserCell = (GiftedUserCell) viewHolder.itemView;
                if (tL_stories$TL_boost.multiplier > 1) {
                    formatString = LocaleController.formatString("BoostsExpireOn", R.string.BoostsExpireOn, LocaleController.formatDate(tL_stories$TL_boost.expires));
                } else {
                    formatString = LocaleController.formatString("BoostExpireOn", R.string.BoostExpireOn, LocaleController.formatDate(tL_stories$TL_boost.expires));
                }
                giftedUserCell.setData(user, ContactsController.formatName(user), formatString, 0, !((ItemInternal) BoostsActivity.this.items.get(i)).isLast);
                giftedUserCell.setStatus(tL_stories$TL_boost);
                giftedUserCell.setAvatarPadding(5);
            } else if (viewHolder.getItemViewType() == 6) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.setText(((ItemInternal) BoostsActivity.this.items.get(i)).title);
                CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawable(BoostsActivity.this.getContext(), i == BoostsActivity.this.items.size() - 2 ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.getColor(Theme.key_windowBackgroundGrayShadow, ((BaseFragment) BoostsActivity.this).resourceProvider)), 0, 0);
                combinedDrawable.setFullsize(true);
                textInfoPrivacyCell.setBackground(combinedDrawable);
            } else if (viewHolder.getItemViewType() == 9) {
                ManageChatTextCell manageChatTextCell = (ManageChatTextCell) viewHolder.itemView;
                if (BoostsActivity.this.selectedTab == 0) {
                    manageChatTextCell.setText(LocaleController.formatPluralString("BoostingShowMoreBoosts", BoostsActivity.this.nextBoostRemaining, new Object[0]), null, R.drawable.arrow_more, false);
                } else {
                    manageChatTextCell.setText(LocaleController.formatPluralString("BoostingShowMoreGifts", BoostsActivity.this.nextGiftsRemaining, new Object[0]), null, R.drawable.arrow_more, false);
                }
            } else if (viewHolder.getItemViewType() == 3) {
                ((LinkActionView) viewHolder.itemView).setLink(((ItemInternal) BoostsActivity.this.items.get(i)).title);
            } else if (viewHolder.getItemViewType() == 11) {
                TL_stories$TL_prepaidGiveaway tL_stories$TL_prepaidGiveaway = ((ItemInternal) BoostsActivity.this.items.get(i)).prepaidGiveaway;
                GiveawayCell giveawayCell = (GiveawayCell) viewHolder.itemView;
                giveawayCell.setData(tL_stories$TL_prepaidGiveaway, LocaleController.formatPluralString("BoostingTelegramPremiumCountPlural", tL_stories$TL_prepaidGiveaway.quantity, new Object[0]), LocaleController.formatPluralString("BoostingSubscriptionsCountPlural", tL_stories$TL_prepaidGiveaway.quantity, LocaleController.formatPluralString("PrepaidGiveawayMonths", tL_stories$TL_prepaidGiveaway.months, new Object[0])), 0, !itemInternal.isLast);
                giveawayCell.setImage(tL_stories$TL_prepaidGiveaway);
                giveawayCell.setAvatarPadding(5);
            } else if (viewHolder.getItemViewType() == 13) {
                if (BoostsActivity.this.boostsTabs.getTag() == null || ((Integer) BoostsActivity.this.boostsTabs.getTag()).intValue() != Objects.hash(Integer.valueOf(BoostsActivity.this.totalBoosts), Integer.valueOf(BoostsActivity.this.totalGifts))) {
                    BoostsActivity.this.boostsTabs.setTag(Integer.valueOf(Objects.hash(Integer.valueOf(BoostsActivity.this.totalBoosts), Integer.valueOf(BoostsActivity.this.totalGifts))));
                    BoostsActivity.this.boostsTabs.removeTabs();
                    BoostsActivity.this.boostsTabs.addTextTab(0, LocaleController.formatPluralString("BoostingBoostsCount", BoostsActivity.this.totalBoosts, new Object[0]));
                    if (MessagesController.getInstance(BoostsActivity.this.currentAccount).giveawayGiftsPurchaseAvailable && BoostsActivity.this.totalGifts > 0 && BoostsActivity.this.totalGifts != BoostsActivity.this.totalBoosts) {
                        BoostsActivity.this.boostsTabs.addTextTab(1, LocaleController.formatPluralString("BoostingGiftsCount", BoostsActivity.this.totalGifts, new Object[0]));
                    }
                    BoostsActivity.this.boostsTabs.setInitialTabId(BoostsActivity.this.selectedTab);
                    BoostsActivity.this.boostsTabs.finishAddingTabs();
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return BoostsActivity.this.items.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return ((ItemInternal) BoostsActivity.this.items.get(i)).viewType;
        }
    };
    private String lastBoostsOffset = "";
    private String lastGiftsOffset = "";
    private int limitGifts = 5;
    private int limitBoosts = 5;

    public void setBoostsStatus(TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        this.boostsStatus = tL_stories$TL_premium_boostsStatus;
        loadCanApplyBoosts();
    }

    public BoostsActivity(long j) {
        this.dialogId = j;
        this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
    }

    public void updateRows(boolean z) {
        ArrayList<? extends AdapterWithDiffUtils.Item> arrayList = new ArrayList<>(this.items);
        this.items.clear();
        this.items.add(new ItemInternal(this, 14, false));
        if (this.boostsStatus != null) {
            this.items.add(new ItemInternal(this, 16, LocaleController.getString("StatisticOverview", R.string.StatisticOverview)));
            this.items.add(new ItemInternal(this, 0, false));
            this.items.add(new ItemInternal(this, 2, false));
            if (this.boostsStatus.prepaid_giveaways.size() > 0) {
                this.items.add(new ItemInternal(this, 12, LocaleController.getString("BoostingPreparedGiveaways", R.string.BoostingPreparedGiveaways)));
                int i = 0;
                while (i < this.boostsStatus.prepaid_giveaways.size()) {
                    this.items.add(new ItemInternal(this, 11, this.boostsStatus.prepaid_giveaways.get(i), i == this.boostsStatus.prepaid_giveaways.size() - 1));
                    i++;
                }
                this.items.add(new ItemInternal(this, 6, LocaleController.getString("BoostingSelectPaidGiveaway", R.string.BoostingSelectPaidGiveaway)));
            }
            this.items.add(new ItemInternal(this, 13, LocaleController.getString("Boosters", R.string.Boosters)));
            if (this.selectedTab == 0) {
                if (this.boosters.isEmpty()) {
                    this.items.add(new ItemInternal(this, 8, false));
                    this.items.add(new ItemInternal(this, 2, false));
                } else {
                    int i2 = 0;
                    while (i2 < this.boosters.size()) {
                        this.items.add(new ItemInternal(this, 5, this.boosters.get(i2), i2 == this.boosters.size() - 1 && !this.hasBoostsNext, this.selectedTab));
                        i2++;
                    }
                    if (this.hasBoostsNext) {
                        this.items.add(new ItemInternal(this, 9, true));
                    } else {
                        this.items.add(new ItemInternal(this, 7, false));
                    }
                    this.items.add(new ItemInternal(this, 6, LocaleController.getString(isChannel() ? R.string.BoostersInfoDescription : R.string.BoostersInfoGroupDescription)));
                }
            } else if (this.gifts.isEmpty()) {
                this.items.add(new ItemInternal(this, 8, false));
                this.items.add(new ItemInternal(this, 2, false));
            } else {
                int i3 = 0;
                while (i3 < this.gifts.size()) {
                    this.items.add(new ItemInternal(this, 5, this.gifts.get(i3), i3 == this.gifts.size() - 1 && !this.hasGiftsNext, this.selectedTab));
                    i3++;
                }
                if (this.hasGiftsNext) {
                    this.items.add(new ItemInternal(this, 9, true));
                } else {
                    this.items.add(new ItemInternal(this, 7, false));
                }
                this.items.add(new ItemInternal(this, 6, LocaleController.getString(isChannel() ? R.string.BoostersInfoDescription : R.string.BoostersInfoGroupDescription)));
            }
            this.items.add(new ItemInternal(this, 1, LocaleController.getString("LinkForBoosting", R.string.LinkForBoosting)));
            this.items.add(new ItemInternal(this, 3, this.boostsStatus.boost_url));
            if (MessagesController.getInstance(this.currentAccount).giveawayGiftsPurchaseAvailable && ChatObject.hasAdminRights(this.currentChat)) {
                this.items.add(new ItemInternal(this, 6, LocaleController.getString(isChannel() ? R.string.BoostingShareThisLink : R.string.BoostingShareThisLinkGroup)));
                this.items.add(new ItemInternal(this, 10, true));
                this.items.add(new ItemInternal(this, 6, LocaleController.getString(isChannel() ? R.string.BoostingGetMoreBoosts : R.string.BoostingGetMoreBoostsGroup)));
            } else {
                this.items.add(new ItemInternal(this, 6, ""));
            }
            this.items.add(new ItemInternal(this, 15, false));
        }
        if (z) {
            this.adapter.setItems(arrayList, this.items);
        } else {
            this.adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChannel() {
        return ChatObject.isChannelAndNotMegaGroup(this.currentChat);
    }

    private void loadStatistic() {
        this.progressLayout.setAlpha(0.0f);
        if (this.boostsStatus == null) {
            this.progressLayout.animate().alpha(1.0f).setDuration(200L).setStartDelay(500L).start();
            getMessagesController().getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda1
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    BoostsActivity.this.lambda$loadStatistic$1((TL_stories$TL_premium_boostsStatus) obj);
                }
            });
            return;
        }
        this.progressLayout.setVisibility(8);
        loadUsers(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStatistic$1(final TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                BoostsActivity.this.lambda$loadStatistic$0(tL_stories$TL_premium_boostsStatus);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStatistic$0(TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        this.boostsStatus = tL_stories$TL_premium_boostsStatus;
        loadCanApplyBoosts();
        this.progressLayout.animate().cancel();
        this.progressLayout.animate().alpha(0.0f).setDuration(100L).setStartDelay(0L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.BoostsActivity.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                BoostsActivity.this.progressLayout.setVisibility(8);
            }
        });
        resetHeader(true);
        updateRows(true);
        loadUsers(null);
    }

    private void loadCanApplyBoosts() {
        if (this.boostsStatus == null) {
            return;
        }
        getMessagesController().getBoostsController().userCanBoostChannel(this.dialogId, this.boostsStatus, new Consumer() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda0
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                BoostsActivity.this.lambda$loadCanApplyBoosts$2((ChannelBoostsController.CanApplyBoost) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCanApplyBoosts$2(ChannelBoostsController.CanApplyBoost canApplyBoost) {
        this.canApplyBoost = canApplyBoost;
    }

    private void loadUsers(Boolean bool) {
        if (this.usersLoading) {
            return;
        }
        this.usersLoading = true;
        if (bool == null) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    BoostsActivity.this.lambda$loadUsers$5();
                }
            });
        } else if (bool.booleanValue()) {
            loadOnlyGifts(null, new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    BoostsActivity.this.lambda$loadUsers$6();
                }
            });
        } else {
            loadOnlyBoosts(null, new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    BoostsActivity.this.lambda$loadUsers$7();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadUsers$5() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        loadOnlyBoosts(countDownLatch, null);
        loadOnlyGifts(countDownLatch, null);
        try {
            countDownLatch.await();
        } catch (InterruptedException unused) {
        }
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                BoostsActivity.this.lambda$loadUsers$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadUsers$4() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BoostsActivity.this.lambda$loadUsers$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadUsers$3() {
        this.usersLoading = false;
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadUsers$6() {
        this.usersLoading = false;
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadUsers$7() {
        this.usersLoading = false;
        updateRows(true);
    }

    private void loadOnlyBoosts(final CountDownLatch countDownLatch, final Runnable runnable) {
        TL_stories$TL_premium_getBoostsList tL_stories$TL_premium_getBoostsList = new TL_stories$TL_premium_getBoostsList();
        tL_stories$TL_premium_getBoostsList.limit = this.limitBoosts;
        tL_stories$TL_premium_getBoostsList.offset = this.lastBoostsOffset;
        tL_stories$TL_premium_getBoostsList.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_premium_getBoostsList, new RequestDelegate() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda11
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BoostsActivity.this.lambda$loadOnlyBoosts$9(countDownLatch, runnable, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOnlyBoosts$9(final CountDownLatch countDownLatch, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                BoostsActivity.this.lambda$loadOnlyBoosts$8(countDownLatch, tLObject, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOnlyBoosts$8(CountDownLatch countDownLatch, TLObject tLObject, Runnable runnable) {
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
        if (tLObject != null) {
            this.limitBoosts = 20;
            TL_stories$TL_premium_boostsList tL_stories$TL_premium_boostsList = (TL_stories$TL_premium_boostsList) tLObject;
            boolean z = false;
            MessagesController.getInstance(this.currentAccount).putUsers(tL_stories$TL_premium_boostsList.users, false);
            this.lastBoostsOffset = tL_stories$TL_premium_boostsList.next_offset;
            this.boosters.addAll(tL_stories$TL_premium_boostsList.boosts);
            Iterator<TL_stories$TL_boost> it = this.boosters.iterator();
            int i = 0;
            while (true) {
                int i2 = 1;
                if (!it.hasNext()) {
                    break;
                }
                int i3 = it.next().multiplier;
                if (i3 > 0) {
                    i2 = i3;
                }
                i += i2;
            }
            this.nextBoostRemaining = Math.max(0, tL_stories$TL_premium_boostsList.count - i);
            if (!TextUtils.isEmpty(tL_stories$TL_premium_boostsList.next_offset) && this.nextBoostRemaining > 0) {
                z = true;
            }
            this.hasBoostsNext = z;
            this.totalBoosts = tL_stories$TL_premium_boostsList.count;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    private void loadOnlyGifts(final CountDownLatch countDownLatch, final Runnable runnable) {
        TL_stories$TL_premium_getBoostsList tL_stories$TL_premium_getBoostsList = new TL_stories$TL_premium_getBoostsList();
        tL_stories$TL_premium_getBoostsList.limit = this.limitGifts;
        tL_stories$TL_premium_getBoostsList.gifts = true;
        tL_stories$TL_premium_getBoostsList.offset = this.lastGiftsOffset;
        tL_stories$TL_premium_getBoostsList.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_premium_getBoostsList, new RequestDelegate() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda10
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BoostsActivity.this.lambda$loadOnlyGifts$11(countDownLatch, runnable, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOnlyGifts$11(final CountDownLatch countDownLatch, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                BoostsActivity.this.lambda$loadOnlyGifts$10(countDownLatch, tLObject, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOnlyGifts$10(CountDownLatch countDownLatch, TLObject tLObject, Runnable runnable) {
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
        if (tLObject != null) {
            this.limitGifts = 20;
            TL_stories$TL_premium_boostsList tL_stories$TL_premium_boostsList = (TL_stories$TL_premium_boostsList) tLObject;
            boolean z = false;
            MessagesController.getInstance(this.currentAccount).putUsers(tL_stories$TL_premium_boostsList.users, false);
            this.lastGiftsOffset = tL_stories$TL_premium_boostsList.next_offset;
            this.gifts.addAll(tL_stories$TL_premium_boostsList.boosts);
            Iterator<TL_stories$TL_boost> it = this.gifts.iterator();
            int i = 0;
            while (true) {
                int i2 = 1;
                if (!it.hasNext()) {
                    break;
                }
                int i3 = it.next().multiplier;
                if (i3 > 0) {
                    i2 = i3;
                }
                i += i2;
            }
            this.nextGiftsRemaining = Math.max(0, tL_stories$TL_premium_boostsList.count - i);
            if (!TextUtils.isEmpty(tL_stories$TL_premium_boostsList.next_offset) && this.nextGiftsRemaining > 0) {
                z = true;
            }
            this.hasGiftsNext = z;
            this.totalGifts = tL_stories$TL_premium_boostsList.count;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.boostByChannelCreated) {
            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) objArr[0];
            boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
            List<BaseFragment> fragmentStack = getParentLayout().getFragmentStack();
            BaseFragment baseFragment = fragmentStack.size() >= 2 ? fragmentStack.get(fragmentStack.size() - 2) : null;
            if (baseFragment instanceof ChatEditActivity) {
                getParentLayout().removeFragmentFromStack(baseFragment);
            }
            List<BaseFragment> fragmentStack2 = getParentLayout().getFragmentStack();
            BaseFragment baseFragment2 = fragmentStack2.size() >= 2 ? fragmentStack2.get(fragmentStack2.size() - 2) : null;
            if (booleanValue) {
                BaseFragment baseFragment3 = fragmentStack2.size() >= 3 ? fragmentStack2.get(fragmentStack2.size() - 3) : null;
                if (baseFragment2 instanceof ProfileActivity) {
                    getParentLayout().removeFragmentFromStack(baseFragment2);
                }
                finishFragment();
                if (baseFragment3 instanceof ChatActivity) {
                    BoostDialogs.showBulletin(baseFragment3, tLRPC$Chat, true);
                }
                if (baseFragment2 instanceof ChatActivity) {
                    BoostDialogs.showBulletin(baseFragment2, tLRPC$Chat, true);
                    return;
                }
                return;
            }
            finishFragment();
            if ((baseFragment2 instanceof ProfileActivity) || (baseFragment2 instanceof ChatActivity)) {
                BoostDialogs.showBulletin(baseFragment2, tLRPC$Chat, false);
            }
        } else if (i == NotificationCenter.chatWasBoostedByUser && this.dialogId == ((Long) objArr[2]).longValue()) {
            this.boostsStatus = (TL_stories$TL_premium_boostsStatus) objArr[0];
            this.canApplyBoost = (ChannelBoostsController.CanApplyBoost) objArr[1];
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        getNotificationCenter().addObserver(this, NotificationCenter.boostByChannelCreated);
        getNotificationCenter().addObserver(this, NotificationCenter.chatWasBoostedByUser);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        getNotificationCenter().removeObserver(this, NotificationCenter.boostByChannelCreated);
        getNotificationCenter().removeObserver(this, NotificationCenter.chatWasBoostedByUser);
        super.onFragmentDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ItemInternal extends AdapterWithDiffUtils.Item {
        TL_stories$TL_boost booster;
        boolean isLast;
        TL_stories$TL_prepaidGiveaway prepaidGiveaway;
        int tab;
        String title;

        public ItemInternal(BoostsActivity boostsActivity, int i, String str) {
            super(i, false);
            this.title = str;
        }

        public ItemInternal(BoostsActivity boostsActivity, int i, TL_stories$TL_boost tL_stories$TL_boost, boolean z, int i2) {
            super(i, true);
            this.booster = tL_stories$TL_boost;
            this.isLast = z;
            this.tab = i2;
        }

        public ItemInternal(BoostsActivity boostsActivity, int i, TL_stories$TL_prepaidGiveaway tL_stories$TL_prepaidGiveaway, boolean z) {
            super(i, true);
            this.prepaidGiveaway = tL_stories$TL_prepaidGiveaway;
            this.isLast = z;
        }

        public ItemInternal(BoostsActivity boostsActivity, int i, boolean z) {
            super(i, z);
        }

        public boolean equals(Object obj) {
            TL_stories$TL_prepaidGiveaway tL_stories$TL_prepaidGiveaway;
            if (this == obj) {
                return true;
            }
            if (obj == null || ItemInternal.class != obj.getClass()) {
                return false;
            }
            ItemInternal itemInternal = (ItemInternal) obj;
            TL_stories$TL_prepaidGiveaway tL_stories$TL_prepaidGiveaway2 = this.prepaidGiveaway;
            if (tL_stories$TL_prepaidGiveaway2 != null && (tL_stories$TL_prepaidGiveaway = itemInternal.prepaidGiveaway) != null) {
                return tL_stories$TL_prepaidGiveaway2.id == tL_stories$TL_prepaidGiveaway.id && this.isLast == itemInternal.isLast;
            }
            TL_stories$TL_boost tL_stories$TL_boost = this.booster;
            if (tL_stories$TL_boost == null || itemInternal.booster == null) {
                return true;
            }
            return tL_stories$TL_boost.id.hashCode() == itemInternal.booster.id.hashCode() && this.isLast == itemInternal.isLast && this.tab == itemInternal.tab;
        }

        public int hashCode() {
            return Objects.hash(this.title, this.booster, this.prepaidGiveaway, Boolean.valueOf(this.isLast), Integer.valueOf(this.tab));
        }
    }

    public void createEmptyView(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        this.progressLayout = linearLayout;
        linearLayout.setOrientation(1);
        this.progressLayout.addView(new View(this, context) { // from class: org.telegram.ui.BoostsActivity.3
            private final CircularProgressDrawable drawable = new CircularProgressDrawable(AndroidUtilities.dp(30.0f), AndroidUtilities.dp(3.0f), Theme.getColor(Theme.key_dialogTextBlue));

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                this.drawable.setBounds(0, 0, getWidth(), getHeight());
                this.drawable.setAlpha(255);
                this.drawable.draw(canvas);
                invalidate();
                super.onDraw(canvas);
            }
        }, LayoutHelper.createLinear(100, 100, 17, 0, 120, 0, 0));
        ((ViewGroup) this.fragmentView).addView(this.progressLayout, LayoutHelper.createFrame(-1, -2, 17));
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected RecyclerView.Adapter<?> createAdapter() {
        return this.adapter;
    }

    private void resetHeader(boolean z) {
        if (getContext() == null) {
            return;
        }
        if (this.limitPreviewView == null) {
            LimitPreviewView limitPreviewView = new LimitPreviewView(getContext(), R.drawable.filled_limit_boost, 0, 0, this.resourceProvider);
            this.limitPreviewView = limitPreviewView;
            limitPreviewView.isStatistic = true;
            limitPreviewView.setDarkGradientProvider(new LimitPreviewView.DarkGradientProvider() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda12
                @Override // org.telegram.ui.Components.Premium.LimitPreviewView.DarkGradientProvider
                public final Paint setDarkGradientLocation(float f, float f2) {
                    return BoostsActivity.this.setDarkGradientLocation(f, f2);
                }
            });
        }
        if (this.limitPreviewView.getParent() != null) {
            ((ViewGroup) this.limitPreviewView.getParent()).removeView(this.limitPreviewView);
        }
        TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = this.boostsStatus;
        if (tL_stories$TL_premium_boostsStatus != null) {
            this.limitPreviewView.setBoosts(tL_stories$TL_premium_boostsStatus, false);
            if (z) {
                this.limitPreviewView.setAlpha(0.0f);
                this.limitPreviewView.animate().alpha(1.0f).start();
            }
        }
        configureHeader(LocaleController.getString(isChannel() ? R.string.BoostingBoostForChannels : R.string.BoostingBoostForGroups), AndroidUtilities.replaceTags(LocaleController.getString(isChannel() ? R.string.BoostingBoostForChannelsInfo : R.string.BoostingBoostForGroupsInfo)), new FrameLayout(getContext()) { // from class: org.telegram.ui.BoostsActivity.4
            {
                addView(BoostsActivity.this.boostsStatus != null ? BoostsActivity.this.limitPreviewView : new View(getContext()), LayoutHelper.createFrame(-1, BoostsActivity.this.boostsStatus != null ? -2.0f : 110.0f, 0, 8.0f, 46.0f, 8.0f, 33.0f));
            }
        }, new 5(getContext()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 5 extends FrameLayout {
        private final HeaderButtonView buttonView1;
        private final HeaderButtonView buttonView2;
        private final HeaderButtonView buttonView3;

        5(Context context) {
            super(context);
            setWillNotDraw(false);
            HeaderButtonView headerButtonView = new HeaderButtonView(getContext());
            this.buttonView1 = headerButtonView;
            HeaderButtonView headerButtonView2 = new HeaderButtonView(getContext());
            this.buttonView2 = headerButtonView2;
            HeaderButtonView headerButtonView3 = new HeaderButtonView(getContext());
            this.buttonView3 = headerButtonView3;
            headerButtonView.setTextAndIcon(LocaleController.getString(R.string.BoostBtn), R.drawable.filled_boost_plus);
            headerButtonView2.setTextAndIcon(LocaleController.getString(R.string.GiveawayBtn), R.drawable.filled_gift_premium);
            headerButtonView3.setTextAndIcon(LocaleController.getString(R.string.FeaturesBtn), R.drawable.filled_info);
            headerButtonView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.BoostsActivity$5$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    BoostsActivity.5.this.lambda$new$0(view);
                }
            });
            headerButtonView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.BoostsActivity$5$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    BoostsActivity.5.this.lambda$new$2(view);
                }
            });
            headerButtonView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.BoostsActivity$5$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    BoostsActivity.5.this.lambda$new$3(view);
                }
            });
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(0);
            linearLayout.addView(headerButtonView, LayoutHelper.createLinear(-2, -2, 6.0f, 0.0f, 6.0f, 0.0f));
            if (MessagesController.getInstance(BoostsActivity.this.currentAccount).giveawayGiftsPurchaseAvailable && ChatObject.hasAdminRights(BoostsActivity.this.currentChat)) {
                linearLayout.addView(headerButtonView2, LayoutHelper.createLinear(-2, -2, 6.0f, 0.0f, 6.0f, 0.0f));
            }
            linearLayout.addView(headerButtonView3, LayoutHelper.createLinear(-2, -2, 6.0f, 0.0f, 6.0f, 0.0f));
            addView(linearLayout, LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 19.0f, 0.0f, 0.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            BoostsActivity boostsActivity = BoostsActivity.this;
            LimitReachedBottomSheet.openBoostsForUsers(boostsActivity, true, boostsActivity.dialogId, BoostsActivity.this.canApplyBoost, BoostsActivity.this.boostsStatus, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(View view) {
            BoostsActivity.this.updateDialogVisibility(true);
            BoostsActivity boostsActivity = BoostsActivity.this;
            BoostPagerBottomSheet.show(boostsActivity, boostsActivity.dialogId, ((BaseFragment) BoostsActivity.this).resourceProvider);
            BoostPagerBottomSheet.getInstance().setOnHideListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.BoostsActivity$5$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    BoostsActivity.5.this.lambda$new$1(dialogInterface);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(DialogInterface dialogInterface) {
            BoostsActivity.this.updateDialogVisibility(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            BoostsActivity boostsActivity = BoostsActivity.this;
            Context context = getContext();
            BoostsActivity boostsActivity2 = BoostsActivity.this;
            LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(boostsActivity, context, 31, boostsActivity2.currentAccount, boostsActivity2.getResourceProvider());
            limitReachedBottomSheet.setBoostsStats(BoostsActivity.this.boostsStatus, true);
            limitReachedBottomSheet.setDialogId(BoostsActivity.this.dialogId);
            BoostsActivity.this.showDialog(limitReachedBottomSheet);
        }
    }

    /* loaded from: classes4.dex */
    private class HeaderButtonView extends FrameLayout {
        private final ImageView imageView;
        private final RectF rect;
        private final TextView textView;

        public HeaderButtonView(Context context) {
            super(context);
            this.rect = new RectF();
            setWillNotDraw(false);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(-1);
            textView.setTypeface(AndroidUtilities.bold());
            textView.setTextSize(1, 12.0f);
            addView(imageView, LayoutHelper.createFrame(-2, -2, 1));
            addView(textView, LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 25.0f, 0.0f, 0.0f));
            setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            setMinimumWidth(AndroidUtilities.dp(100.0f));
            setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(10.0f), 0, ColorUtils.setAlphaComponent(-16777216, 80)));
        }

        public void setTextAndIcon(CharSequence charSequence, int i) {
            this.textView.setText(charSequence);
            this.imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), i));
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            this.rect.set(0.0f, 0.0f, getWidth(), getHeight());
            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), BoostsActivity.this.setDarkGradientLocation(((ViewGroup) getParent()).getX() + getX(), ((ViewGroup) getParent().getParent().getParent()).getY()));
            invalidate();
            super.draw(canvas);
        }
    }

    @Override // org.telegram.ui.GradientHeaderActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        View createView = super.createView(context);
        resetHeader(false);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setSupportsChangeAnimations(false);
        defaultItemAnimator.setDelayAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.BoostsActivity$$ExternalSyntheticLambda13
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                BoostsActivity.this.lambda$createView$12(view, i);
            }
        });
        createEmptyView(getContext());
        loadStatistic();
        updateRows(false);
        return createView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(View view, int i) {
        if (view instanceof GiftedUserCell) {
            GiftedUserCell giftedUserCell = (GiftedUserCell) view;
            TL_stories$TL_boost boost = giftedUserCell.getBoost();
            boolean z = boost.gift;
            if (((z || boost.giveaway) && boost.user_id >= 0) || boost.unclaimed) {
                TLRPC$TL_payments_checkedGiftCode tLRPC$TL_payments_checkedGiftCode = new TLRPC$TL_payments_checkedGiftCode();
                tLRPC$TL_payments_checkedGiftCode.giveaway_msg_id = boost.giveaway_msg_id;
                tLRPC$TL_payments_checkedGiftCode.to_id = boost.user_id;
                tLRPC$TL_payments_checkedGiftCode.from_id = MessagesController.getInstance(UserConfig.selectedAccount).getPeer(-this.currentChat.id);
                int i2 = boost.date;
                tLRPC$TL_payments_checkedGiftCode.date = i2;
                tLRPC$TL_payments_checkedGiftCode.via_giveaway = boost.giveaway;
                tLRPC$TL_payments_checkedGiftCode.months = ((boost.expires - i2) / 30) / 86400;
                if (boost.unclaimed) {
                    tLRPC$TL_payments_checkedGiftCode.to_id = -1L;
                    tLRPC$TL_payments_checkedGiftCode.flags = -1;
                } else {
                    tLRPC$TL_payments_checkedGiftCode.boost = boost;
                }
                new GiftInfoBottomSheet(this, false, true, tLRPC$TL_payments_checkedGiftCode, boost.used_gift_slug).show();
            } else {
                boolean z2 = boost.giveaway;
                if (z2 && boost.user_id == -1) {
                    Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getParentActivity(), getResourceProvider());
                    lottieLayout.setAnimation(R.raw.chats_infotip, 36, 36, new String[0]);
                    lottieLayout.textView.setText(LocaleController.getString("BoostingRecipientWillBeSelected", R.string.BoostingRecipientWillBeSelected));
                    lottieLayout.textView.setSingleLine(false);
                    lottieLayout.textView.setMaxLines(2);
                    Bulletin.make(this, lottieLayout, 2750).show();
                } else if (!z && !z2) {
                    presentFragment(ProfileActivity.of(giftedUserCell.getDialogId()));
                }
            }
        }
        if (view instanceof TextCell) {
            BoostPagerBottomSheet.show(this, this.dialogId, this.resourceProvider);
        }
        if (view instanceof GiveawayCell) {
            BoostPagerBottomSheet.show(this, this.resourceProvider, this.dialogId, ((GiveawayCell) view).getPrepaidGiveaway());
        }
        if (this.items.get(i).viewType == 9) {
            loadUsers(Boolean.valueOf(this.selectedTab == 1));
        }
    }
}