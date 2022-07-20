package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AvailableReactionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SimpleThemeDescription;
/* loaded from: classes3.dex */
public class ChatReactionsEditActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private long chatId;
    private LinearLayout contentView;
    private TLRPC$Chat currentChat;
    private TextCheckCell enableReactionsCell;
    private TLRPC$ChatFull info;
    private RecyclerView.Adapter listAdapter;
    private RecyclerListView listView;
    private List<String> chatReactions = new ArrayList();
    private ArrayList<TLRPC$TL_availableReaction> availableReactions = new ArrayList<>();

    public ChatReactionsEditActivity(Bundle bundle) {
        super(bundle);
        this.chatId = bundle.getLong("chat_id", 0L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
        if (r0 == null) goto L10;
     */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onFragmentCreate() {
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        this.currentChat = chat;
        if (chat == null) {
            TLRPC$Chat chatSync = MessagesStorage.getInstance(this.currentAccount).getChatSync(this.chatId);
            this.currentChat = chatSync;
            if (chatSync != null) {
                getMessagesController().putChat(this.currentChat, true);
                if (this.info == null) {
                    TLRPC$ChatFull loadChatInfo = MessagesStorage.getInstance(this.currentAccount).loadChatInfo(this.chatId, ChatObject.isChannel(this.currentChat), new CountDownLatch(1), false, false);
                    this.info = loadChatInfo;
                }
            }
            return false;
        }
        getNotificationCenter().addObserver(this, NotificationCenter.reactionsDidLoad);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setTitle(LocaleController.getString("Reactions", 2131627847));
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        this.availableReactions.addAll(getMediaDataController().getEnabledReactionsList());
        TextCheckCell textCheckCell = new TextCheckCell(context);
        this.enableReactionsCell = textCheckCell;
        textCheckCell.setHeight(56);
        this.enableReactionsCell.setTextAndCheck(LocaleController.getString("EnableReactions", 2131625621), true ^ this.chatReactions.isEmpty(), false);
        TextCheckCell textCheckCell2 = this.enableReactionsCell;
        textCheckCell2.setBackgroundColor(Theme.getColor(textCheckCell2.isChecked() ? "windowBackgroundChecked" : "windowBackgroundUnchecked"));
        this.enableReactionsCell.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.enableReactionsCell.setOnClickListener(new ChatReactionsEditActivity$$ExternalSyntheticLambda0(this));
        linearLayout.addView(this.enableReactionsCell, LayoutHelper.createLinear(-1, -2));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerListView recyclerListView2 = this.listView;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.listAdapter = anonymousClass2;
        recyclerListView2.setAdapter(anonymousClass2);
        this.listView.setOnItemClickListener(new ChatReactionsEditActivity$$ExternalSyntheticLambda2(this));
        linearLayout.addView(this.listView, LayoutHelper.createLinear(-1, 0, 1.0f));
        this.contentView = linearLayout;
        this.fragmentView = linearLayout;
        updateColors();
        return this.contentView;
    }

    /* renamed from: org.telegram.ui.ChatReactionsEditActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ChatReactionsEditActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                ChatReactionsEditActivity.this.finishFragment();
            }
        }
    }

    public /* synthetic */ void lambda$createView$0(View view) {
        setCheckedEnableReactionCell(!this.enableReactionsCell.isChecked());
    }

    /* renamed from: org.telegram.ui.ChatReactionsEditActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends RecyclerView.Adapter {
        final /* synthetic */ Context val$context;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return 0;
            }
            return i == 1 ? 1 : 2;
        }

        AnonymousClass2(Context context) {
            ChatReactionsEditActivity.this = r1;
            this.val$context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i != 0) {
                if (i != 1) {
                    return new RecyclerListView.Holder(new AvailableReactionCell(this.val$context, false));
                }
                return new RecyclerListView.Holder(new HeaderCell(this.val$context, 23));
            }
            return new RecyclerListView.Holder(new TextInfoPrivacyCell(this.val$context));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = getItemViewType(i);
            if (itemViewType == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
                textInfoPrivacyCell.setText(ChatObject.isChannelAndNotMegaGroup(ChatReactionsEditActivity.this.currentChat) ? LocaleController.getString("EnableReactionsChannelInfo", 2131625622) : LocaleController.getString("EnableReactionsGroupInfo", 2131625623));
            } else if (itemViewType == 1) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                headerCell.setText(LocaleController.getString("AvailableReactions", 2131624633));
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (itemViewType != 2) {
            } else {
                TLRPC$TL_availableReaction tLRPC$TL_availableReaction = (TLRPC$TL_availableReaction) ChatReactionsEditActivity.this.availableReactions.get(i - 2);
                ((AvailableReactionCell) viewHolder.itemView).bind(tLRPC$TL_availableReaction, ChatReactionsEditActivity.this.chatReactions.contains(tLRPC$TL_availableReaction.reaction));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return (!ChatReactionsEditActivity.this.chatReactions.isEmpty() ? ChatReactionsEditActivity.this.availableReactions.size() + 1 : 0) + 1;
        }
    }

    public /* synthetic */ void lambda$createView$1(View view, int i) {
        if (i <= 1) {
            return;
        }
        AvailableReactionCell availableReactionCell = (AvailableReactionCell) view;
        TLRPC$TL_availableReaction tLRPC$TL_availableReaction = this.availableReactions.get(i - 2);
        boolean z = !this.chatReactions.contains(tLRPC$TL_availableReaction.reaction);
        if (z) {
            this.chatReactions.add(tLRPC$TL_availableReaction.reaction);
        } else {
            this.chatReactions.remove(tLRPC$TL_availableReaction.reaction);
            if (this.chatReactions.isEmpty()) {
                setCheckedEnableReactionCell(false);
            }
        }
        availableReactionCell.setChecked(z, true);
    }

    private void setCheckedEnableReactionCell(boolean z) {
        if (this.enableReactionsCell.isChecked() == z) {
            return;
        }
        this.enableReactionsCell.setChecked(z);
        int color = Theme.getColor(z ? "windowBackgroundChecked" : "windowBackgroundUnchecked");
        if (z) {
            this.enableReactionsCell.setBackgroundColorAnimated(z, color);
        } else {
            this.enableReactionsCell.setBackgroundColorAnimatedReverse(color);
        }
        if (z) {
            Iterator<TLRPC$TL_availableReaction> it = this.availableReactions.iterator();
            while (it.hasNext()) {
                this.chatReactions.add(it.next().reaction);
            }
            this.listAdapter.notifyItemRangeInserted(1, this.availableReactions.size() + 1);
            return;
        }
        this.chatReactions.clear();
        this.listAdapter.notifyItemRangeRemoved(1, this.availableReactions.size() + 1);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        boolean z = true;
        if (tLRPC$ChatFull != null) {
            z = true ^ tLRPC$ChatFull.available_reactions.equals(this.chatReactions);
        }
        if (z) {
            getMessagesController().setChatReactions(this.chatId, this.chatReactions);
        }
        getNotificationCenter().removeObserver(this, NotificationCenter.reactionsDidLoad);
    }

    public void setInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            if (this.currentChat == null) {
                this.currentChat = getMessagesController().getChat(Long.valueOf(this.chatId));
            }
            this.chatReactions = new ArrayList(tLRPC$ChatFull.available_reactions);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        return SimpleThemeDescription.createThemeDescriptions(new ChatReactionsEditActivity$$ExternalSyntheticLambda1(this), "windowBackgroundWhite", "windowBackgroundWhiteBlackText", "windowBackgroundWhiteGrayText2", "listSelectorSDK21", "windowBackgroundGray", "windowBackgroundWhiteGrayText4", "windowBackgroundWhiteRedText4", "windowBackgroundChecked", "windowBackgroundCheckText", "switchTrackBlue", "switchTrackBlueChecked", "switchTrackBlueThumb", "switchTrackBlueThumbChecked");
    }

    @SuppressLint({"NotifyDataSetChanged"})
    public void updateColors() {
        this.contentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.enableReactionsCell.setColors("windowBackgroundCheckText", "switchTrackBlue", "switchTrackBlueChecked", "switchTrackBlueThumb", "switchTrackBlueThumbChecked");
        this.listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    @SuppressLint({"NotifyDataSetChanged"})
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i2 == this.currentAccount && i == NotificationCenter.reactionsDidLoad) {
            this.availableReactions.clear();
            this.availableReactions.addAll(getMediaDataController().getEnabledReactionsList());
            this.listAdapter.notifyDataSetChanged();
        }
    }
}