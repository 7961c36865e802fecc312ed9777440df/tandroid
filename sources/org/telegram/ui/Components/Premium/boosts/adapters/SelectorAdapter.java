package org.telegram.ui.Components.Premium.boosts.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$TL_help_country;
import org.telegram.tgnet.TLRPC$TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC$TL_inputPeerChat;
import org.telegram.tgnet.TLRPC$TL_inputPeerSelf;
import org.telegram.tgnet.TLRPC$TL_inputPeerUser;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorCountryCell;
import org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorLetterCell;
import org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorUserCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerEmptyView;
/* loaded from: classes3.dex */
public class SelectorAdapter extends AdapterWithDiffUtils {
    private HashMap<Long, Integer> chatsParticipantsCount = new HashMap<>();
    private final Context context;
    private boolean isGreenSelector;
    private List<Item> items;
    private final Theme.ResourcesProvider resourcesProvider;
    private GraySectionCell topSectionCell;

    public SelectorAdapter(Context context, Theme.ResourcesProvider resourcesProvider) {
        this.context = context;
        this.resourcesProvider = resourcesProvider;
        BoostRepository.loadParticipantsCount(new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.SelectorAdapter$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                SelectorAdapter.this.lambda$new$0((HashMap) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(HashMap hashMap) {
        this.chatsParticipantsCount.clear();
        this.chatsParticipantsCount.putAll(hashMap);
    }

    public void setData(List<Item> list, RecyclerListView recyclerListView) {
        this.items = list;
    }

    public void setTopSectionClickListener(View.OnClickListener onClickListener) {
        GraySectionCell graySectionCell = this.topSectionCell;
        if (graySectionCell != null) {
            if (onClickListener == null) {
                graySectionCell.setRightText(null);
            } else {
                graySectionCell.setRightText(LocaleController.getString(R.string.UsersDeselectAll), true, onClickListener);
            }
        }
    }

    public void setGreenSelector(boolean z) {
        this.isGreenSelector = z;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getItemViewType() == 3 || viewHolder.getItemViewType() == 6 || viewHolder.getItemViewType() == 9;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        SelectorUserCell selectorUserCell;
        if (i == -1) {
            selectorUserCell = new View(this.context);
        } else if (i == 3) {
            selectorUserCell = new SelectorUserCell(this.context, this.resourcesProvider, this.isGreenSelector);
        } else if (i == 5) {
            StickerEmptyView stickerEmptyView = new StickerEmptyView(this.context, null, 1, this.resourcesProvider);
            stickerEmptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
            stickerEmptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", R.string.SearchEmptyViewFilteredSubtitle2));
            stickerEmptyView.linearLayout.setTranslationY(AndroidUtilities.dp(24.0f));
            selectorUserCell = stickerEmptyView;
        } else if (i == 7) {
            selectorUserCell = new SelectorLetterCell(this.context, this.resourcesProvider);
        } else if (i == 6) {
            selectorUserCell = new SelectorCountryCell(this.context, this.resourcesProvider);
        } else if (i == 8) {
            selectorUserCell = new GraySectionCell(this.context, this.resourcesProvider);
        } else if (i == 9) {
            TextCell textCell = new TextCell(this.context, this.resourcesProvider);
            textCell.leftPadding = 16;
            textCell.imageLeft = 19;
            selectorUserCell = textCell;
        } else {
            selectorUserCell = new View(this.context);
        }
        return new RecyclerListView.Holder(selectorUserCell);
    }

    public int getParticipantsCount(TLRPC$Chat tLRPC$Chat) {
        Integer num;
        int i;
        TLRPC$ChatFull chatFull = MessagesController.getInstance(UserConfig.selectedAccount).getChatFull(tLRPC$Chat.id);
        if (chatFull == null || (i = chatFull.participants_count) <= 0) {
            if (!this.chatsParticipantsCount.isEmpty() && (num = this.chatsParticipantsCount.get(Long.valueOf(tLRPC$Chat.id))) != null) {
                return num.intValue();
            }
            return tLRPC$Chat.participants_count;
        }
        return i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int i2;
        List<Item> list = this.items;
        if (list == null || i < 0) {
            return;
        }
        Item item = list.get(i);
        int itemViewType = viewHolder.getItemViewType();
        boolean z = true;
        if (itemViewType == 3) {
            SelectorUserCell selectorUserCell = (SelectorUserCell) viewHolder.itemView;
            TLRPC$User tLRPC$User = item.user;
            if (tLRPC$User != null) {
                selectorUserCell.setUser(tLRPC$User);
            } else {
                TLRPC$Chat tLRPC$Chat = item.chat;
                if (tLRPC$Chat != null) {
                    selectorUserCell.setChat(tLRPC$Chat, getParticipantsCount(tLRPC$Chat));
                } else {
                    TLRPC$InputPeer tLRPC$InputPeer = item.peer;
                    if (tLRPC$InputPeer != null) {
                        if (tLRPC$InputPeer instanceof TLRPC$TL_inputPeerSelf) {
                            selectorUserCell.setUser(UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser());
                        } else if (tLRPC$InputPeer instanceof TLRPC$TL_inputPeerUser) {
                            selectorUserCell.setUser(MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(tLRPC$InputPeer.user_id)));
                        } else if (tLRPC$InputPeer instanceof TLRPC$TL_inputPeerChat) {
                            TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(tLRPC$InputPeer.chat_id));
                            selectorUserCell.setChat(chat, getParticipantsCount(chat));
                        } else if (tLRPC$InputPeer instanceof TLRPC$TL_inputPeerChannel) {
                            TLRPC$Chat chat2 = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(tLRPC$InputPeer.channel_id));
                            selectorUserCell.setChat(chat2, getParticipantsCount(chat2));
                        }
                    }
                }
            }
            selectorUserCell.setChecked(item.checked, false);
            selectorUserCell.setCheckboxAlpha(1.0f, false);
            int i3 = i + 1;
            if (i3 < this.items.size() && this.items.get(i3).viewType != itemViewType) {
                z = false;
            }
            selectorUserCell.setDivider(z);
            if (i3 < this.items.size() && this.items.get(i3).viewType == 7) {
                selectorUserCell.setDivider(false);
            }
            selectorUserCell.setOptions(item.options);
        } else if (itemViewType == 6) {
            SelectorCountryCell selectorCountryCell = (SelectorCountryCell) viewHolder.itemView;
            selectorCountryCell.setCountry(item.country, (i >= this.items.size() - 1 || (i2 = i + 1) >= this.items.size() - 1 || this.items.get(i2).viewType == 7) ? false : false);
            selectorCountryCell.setChecked(item.checked, false);
        } else if (itemViewType == -1) {
            int i4 = item.padHeight;
            if (i4 < 0) {
                i4 = (int) (AndroidUtilities.displaySize.y * 0.3f);
            }
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(-1, i4));
        } else if (itemViewType == 7) {
            ((SelectorLetterCell) viewHolder.itemView).setLetter(item.text);
        } else if (itemViewType == 5) {
            try {
                ((StickerEmptyView) viewHolder.itemView).stickerView.getImageReceiver().startAnimation();
            } catch (Exception unused) {
            }
        } else if (itemViewType != 8) {
            if (itemViewType == 9) {
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                textCell.setTextAndIcon(item.text, item.resId, false);
            }
        } else {
            GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
            if (TextUtils.equals(graySectionCell.getText(), item.text)) {
                CharSequence charSequence = item.subtext;
                if (charSequence == null) {
                    charSequence = "";
                }
                graySectionCell.setRightText(charSequence, true, item.callback);
            } else {
                graySectionCell.setText(Emoji.replaceWithRestrictedEmoji(item.text, graySectionCell.getTextView(), (Runnable) null));
                if (!TextUtils.isEmpty(item.subtext)) {
                    graySectionCell.setRightText(item.subtext, item.callback);
                }
            }
            this.topSectionCell = graySectionCell;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        List<Item> list = this.items;
        if (list == null || i < 0) {
            return -1;
        }
        return list.get(i).viewType;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<Item> list = this.items;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void notifyChangedLast() {
        List<Item> list = this.items;
        if (list == null || list.isEmpty()) {
            return;
        }
        notifyItemChanged(this.items.size() - 1);
    }

    /* loaded from: classes3.dex */
    public static class Item extends AdapterWithDiffUtils.Item {
        public View.OnClickListener callback;
        public TLRPC$Chat chat;
        public boolean checked;
        public TLRPC$TL_help_country country;
        public int id;
        public View.OnClickListener options;
        public int padHeight;
        public TLRPC$InputPeer peer;
        public int resId;
        public CharSequence subtext;
        public CharSequence text;
        public int type;
        public TLRPC$User user;

        private Item(int i, boolean z) {
            super(i, z);
            this.padHeight = -1;
        }

        public static Item asPad(int i) {
            Item item = new Item(-1, false);
            item.padHeight = i;
            return item;
        }

        public static Item asButton(int i, int i2, String str) {
            Item item = new Item(9, false);
            item.id = i;
            item.resId = i2;
            item.text = str;
            return item;
        }

        public static Item asUser(TLRPC$User tLRPC$User, boolean z) {
            Item item = new Item(3, true);
            item.user = tLRPC$User;
            item.peer = null;
            item.chat = null;
            item.checked = z;
            return item;
        }

        public Item withOptions(View.OnClickListener onClickListener) {
            this.options = onClickListener;
            return this;
        }

        public static Item asLetter(String str) {
            Item item = new Item(7, false);
            item.text = str;
            return item;
        }

        public static Item asTopSection(CharSequence charSequence) {
            Item item = new Item(8, false);
            item.text = charSequence;
            return item;
        }

        public Item withRightText(String str, View.OnClickListener onClickListener) {
            this.subtext = str;
            this.callback = onClickListener;
            return this;
        }

        public static Item asCountry(TLRPC$TL_help_country tLRPC$TL_help_country, boolean z) {
            Item item = new Item(6, true);
            item.country = tLRPC$TL_help_country;
            item.checked = z;
            return item;
        }

        public static Item asPeer(TLRPC$InputPeer tLRPC$InputPeer, boolean z) {
            Item item = new Item(3, true);
            item.peer = tLRPC$InputPeer;
            item.user = null;
            item.chat = null;
            item.checked = z;
            return item;
        }

        public long getDialogId() {
            TLRPC$User tLRPC$User = this.user;
            if (tLRPC$User != null) {
                return tLRPC$User.id;
            }
            TLRPC$Chat tLRPC$Chat = this.chat;
            if (tLRPC$Chat != null) {
                return -tLRPC$Chat.id;
            }
            TLRPC$InputPeer tLRPC$InputPeer = this.peer;
            if (tLRPC$InputPeer != null) {
                return DialogObject.getPeerDialogId(tLRPC$InputPeer);
            }
            return 0L;
        }

        public static Item asNoUsers() {
            return new Item(5, false);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || Item.class != obj.getClass()) {
                return false;
            }
            Item item = (Item) obj;
            int i = this.viewType;
            if (i != item.viewType) {
                return false;
            }
            if (i != -1 || this.padHeight == item.padHeight) {
                if (i != 3 || (getDialogId() == item.getDialogId() && this.type == item.type)) {
                    int i2 = this.viewType;
                    if (i2 != 6 || this.country == item.country) {
                        if (i2 != 7 || TextUtils.equals(this.text, item.text)) {
                            if (this.viewType != 8 || TextUtils.equals(this.text, item.text)) {
                                return this.viewType != 9 || (TextUtils.equals(this.text, item.text) && this.id == item.id && this.resId == item.resId);
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.ListView.AdapterWithDiffUtils.Item
        public boolean contentsEquals(AdapterWithDiffUtils.Item item) {
            if (this == item) {
                return true;
            }
            if (item == null || Item.class != item.getClass()) {
                return false;
            }
            Item item2 = (Item) item;
            if (this.checked != item2.checked) {
                return false;
            }
            if (this.viewType == 8) {
                if (TextUtils.equals(this.subtext, item2.subtext)) {
                    if ((this.callback == null) == (item2.callback == null)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
    }
}