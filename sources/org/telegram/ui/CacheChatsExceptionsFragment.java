package org.telegram.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.CacheByChatsController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.KeepMediaPopupView;
/* loaded from: classes3.dex */
public class CacheChatsExceptionsFragment extends BaseFragment {
    Adapter adapter;
    int currentType;
    RecyclerListView recyclerListView;
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<CacheByChatsController.KeepMediaException> exceptionsDialogs = new ArrayList<>();

    public CacheChatsExceptionsFragment(Bundle bundle) {
        super(bundle);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.CacheChatsExceptionsFragment.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    CacheChatsExceptionsFragment.this.finishFragment();
                }
            }
        });
        this.actionBar.setTitle(LocaleController.getString(R.string.NotificationsExceptions));
        this.recyclerListView = new RecyclerListView(context);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.recyclerListView.setItemAnimator(defaultItemAnimator);
        this.recyclerListView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerListView recyclerListView = this.recyclerListView;
        Adapter adapter = new Adapter();
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.CacheChatsExceptionsFragment$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                CacheChatsExceptionsFragment.this.lambda$createView$3(view, i, f, f2);
            }
        });
        frameLayout.addView(this.recyclerListView);
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        updateRows();
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view, int i, float f, float f2) {
        if (this.items.get(i).viewType == 1) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putBoolean("onlySelect", true);
            bundle.putBoolean("checkCanWrite", false);
            int i2 = this.currentType;
            if (i2 == 1) {
                bundle.putInt("dialogsType", 6);
            } else if (i2 == 2) {
                bundle.putInt("dialogsType", 5);
            } else {
                bundle.putInt("dialogsType", 4);
            }
            final DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.CacheChatsExceptionsFragment$$ExternalSyntheticLambda3
                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                public final void didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z) {
                    CacheChatsExceptionsFragment.this.lambda$createView$0(dialogsActivity, dialogsActivity2, arrayList, charSequence, z);
                }
            });
            presentFragment(dialogsActivity);
        } else if (this.items.get(i).viewType == 2) {
            final CacheByChatsController.KeepMediaException keepMediaException = this.items.get(i).exception;
            KeepMediaPopupView keepMediaPopupView = new KeepMediaPopupView(this, view.getContext());
            keepMediaPopupView.updateForDialog(false);
            keepMediaPopupView.setParentWindow(AlertsCreator.createSimplePopup(this, keepMediaPopupView, view, f, f2));
            keepMediaPopupView.setCallback(new KeepMediaPopupView.Callback() { // from class: org.telegram.ui.CacheChatsExceptionsFragment$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.KeepMediaPopupView.Callback
                public final void onKeepMediaChange(int i3, int i4) {
                    CacheChatsExceptionsFragment.this.lambda$createView$1(keepMediaException, i3, i4);
                }
            });
        } else if (this.items.get(i).viewType != 4) {
        } else {
            AlertDialog create = AlertsCreator.createSimpleAlert(getContext(), LocaleController.getString("NotificationsDeleteAllExceptionTitle", R.string.NotificationsDeleteAllExceptionTitle), LocaleController.getString("NotificationsDeleteAllExceptionAlert", R.string.NotificationsDeleteAllExceptionAlert), LocaleController.getString("Delete", R.string.Delete), new Runnable() { // from class: org.telegram.ui.CacheChatsExceptionsFragment$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CacheChatsExceptionsFragment.this.lambda$createView$2();
                }
            }, null).create();
            create.show();
            create.redPositive();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z) {
        boolean z2;
        dialogsActivity.finishFragment();
        int i = 0;
        CacheByChatsController.KeepMediaException keepMediaException = null;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= this.exceptionsDialogs.size()) {
                    z2 = false;
                    break;
                } else if (this.exceptionsDialogs.get(i3).dialogId == ((MessagesStorage.TopicKey) arrayList.get(i2)).dialogId) {
                    keepMediaException = this.exceptionsDialogs.get(i3);
                    z2 = true;
                    break;
                } else {
                    i3++;
                }
            }
            if (!z2) {
                int i4 = CacheByChatsController.KEEP_MEDIA_FOREVER;
                if (getMessagesController().getCacheByChatsController().getKeepMedia(this.currentType) == CacheByChatsController.KEEP_MEDIA_FOREVER) {
                    i4 = CacheByChatsController.KEEP_MEDIA_ONE_DAY;
                }
                ArrayList<CacheByChatsController.KeepMediaException> arrayList2 = this.exceptionsDialogs;
                CacheByChatsController.KeepMediaException keepMediaException2 = new CacheByChatsController.KeepMediaException(((MessagesStorage.TopicKey) arrayList.get(i2)).dialogId, i4);
                arrayList2.add(keepMediaException2);
                keepMediaException = keepMediaException2;
            }
        }
        getMessagesController().getCacheByChatsController().saveKeepMediaExceptions(this.currentType, this.exceptionsDialogs);
        updateRows();
        if (keepMediaException != null) {
            int i5 = 0;
            while (true) {
                if (i5 >= this.items.size()) {
                    break;
                }
                if (this.items.get(i5).exception != null && this.items.get(i5).exception.dialogId == keepMediaException.dialogId) {
                    i = i5;
                    break;
                }
                i5++;
            }
            this.recyclerListView.scrollToPosition(i);
            showPopupFor(keepMediaException);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(CacheByChatsController.KeepMediaException keepMediaException, int i, int i2) {
        if (i2 == CacheByChatsController.KEEP_MEDIA_DELETE) {
            this.exceptionsDialogs.remove(keepMediaException);
            updateRows();
        } else {
            keepMediaException.keepMedia = i2;
            AndroidUtilities.updateVisibleRows(this.recyclerListView);
        }
        getMessagesController().getCacheByChatsController().saveKeepMediaExceptions(this.currentType, this.exceptionsDialogs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        this.exceptionsDialogs.clear();
        getMessagesController().getCacheByChatsController().saveKeepMediaExceptions(this.currentType, this.exceptionsDialogs);
        updateRows();
        finishFragment();
    }

    public void showPopupFor(final CacheByChatsController.KeepMediaException keepMediaException) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheChatsExceptionsFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CacheChatsExceptionsFragment.this.lambda$showPopupFor$5(keepMediaException);
            }
        }, 150L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPopupFor$5(final CacheByChatsController.KeepMediaException keepMediaException) {
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 < this.items.size()) {
                if (this.items.get(i2).exception != null && this.items.get(i2).exception.dialogId == keepMediaException.dialogId) {
                    i = i2;
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.recyclerListView.findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null) {
            KeepMediaPopupView keepMediaPopupView = new KeepMediaPopupView(this, getContext());
            keepMediaPopupView.updateForDialog(true);
            View view = findViewHolderForAdapterPosition.itemView;
            keepMediaPopupView.setParentWindow(AlertsCreator.createSimplePopup(this, keepMediaPopupView, view, view.getMeasuredWidth() / 2.0f, findViewHolderForAdapterPosition.itemView.getMeasuredHeight() / 2.0f));
            keepMediaPopupView.setCallback(new KeepMediaPopupView.Callback() { // from class: org.telegram.ui.CacheChatsExceptionsFragment$$ExternalSyntheticLambda5
                @Override // org.telegram.ui.KeepMediaPopupView.Callback
                public final void onKeepMediaChange(int i3, int i4) {
                    CacheChatsExceptionsFragment.this.lambda$showPopupFor$4(keepMediaException, i3, i4);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPopupFor$4(CacheByChatsController.KeepMediaException keepMediaException, int i, int i2) {
        keepMediaException.keepMedia = i2;
        getMessagesController().getCacheByChatsController().saveKeepMediaExceptions(this.currentType, this.exceptionsDialogs);
        AndroidUtilities.updateVisibleRows(this.recyclerListView);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.currentType = getArguments().getInt("type");
        updateRows();
        return super.onFragmentCreate();
    }

    private void updateRows() {
        ArrayList<? extends AdapterWithDiffUtils.Item> arrayList;
        boolean z = false;
        if (!this.isPaused && this.adapter != null) {
            arrayList = new ArrayList<>();
            arrayList.addAll(this.items);
        } else {
            arrayList = null;
        }
        this.items.clear();
        this.items.add(new Item(1, null));
        Iterator<CacheByChatsController.KeepMediaException> it = this.exceptionsDialogs.iterator();
        while (it.hasNext()) {
            this.items.add(new Item(2, it.next()));
            z = true;
        }
        if (z) {
            this.items.add(new Item(3, null));
            this.items.add(new Item(4, null));
        }
        this.items.add(new Item(3, null));
        Adapter adapter = this.adapter;
        if (adapter != null) {
            if (arrayList != null) {
                adapter.setItems(arrayList, this.items);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setExceptions(ArrayList<CacheByChatsController.KeepMediaException> arrayList) {
        this.exceptionsDialogs = arrayList;
        updateRows();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Adapter extends AdapterWithDiffUtils {
        private Adapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextCell textCell = null;
            if (i == 1) {
                TextCell textCell2 = new TextCell(viewGroup.getContext());
                textCell2.setTextAndIcon(LocaleController.getString("NotificationsAddAnException", R.string.NotificationsAddAnException), R.drawable.msg_contact_add, true);
                textCell2.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                textCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textCell = textCell2;
            } else if (i == 2) {
                UserCell userCell = new UserCell(viewGroup.getContext(), 4, 0, false, false);
                userCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textCell = userCell;
            } else if (i == 3) {
                textCell = new ShadowSectionCell(viewGroup.getContext());
            } else if (i == 4) {
                TextCell textCell3 = new TextCell(viewGroup.getContext());
                textCell3.setText(LocaleController.getString("NotificationsDeleteAllException", R.string.NotificationsDeleteAllException), false);
                textCell3.setColors(null, "windowBackgroundWhiteRedText5");
                textCell3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textCell = textCell3;
            }
            textCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(textCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (CacheChatsExceptionsFragment.this.items.get(i).viewType == 2) {
                UserCell userCell = (UserCell) viewHolder.itemView;
                CacheByChatsController.KeepMediaException keepMediaException = CacheChatsExceptionsFragment.this.items.get(i).exception;
                TLObject userOrChat = CacheChatsExceptionsFragment.this.getMessagesController().getUserOrChat(keepMediaException.dialogId);
                String str = null;
                if (userOrChat instanceof TLRPC$User) {
                    TLRPC$User tLRPC$User = (TLRPC$User) userOrChat;
                    str = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                } else if (userOrChat instanceof TLRPC$Chat) {
                    str = ((TLRPC$Chat) userOrChat).title;
                }
                userCell.setData(userOrChat, str, CacheByChatsController.getKeepMediaString(keepMediaException.keepMedia), 0, i == CacheChatsExceptionsFragment.this.items.size() - 1 || CacheChatsExceptionsFragment.this.items.get(i + 1).viewType == 2);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return CacheChatsExceptionsFragment.this.items.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return CacheChatsExceptionsFragment.this.items.get(i).viewType;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 2 || viewHolder.getItemViewType() == 4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Item extends AdapterWithDiffUtils.Item {
        final CacheByChatsController.KeepMediaException exception;

        private Item(CacheChatsExceptionsFragment cacheChatsExceptionsFragment, int i, CacheByChatsController.KeepMediaException keepMediaException) {
            super(i, false);
            this.exception = keepMediaException;
        }

        public boolean equals(Object obj) {
            CacheByChatsController.KeepMediaException keepMediaException;
            if (this == obj) {
                return true;
            }
            if (obj == null || Item.class != obj.getClass()) {
                return false;
            }
            Item item = (Item) obj;
            if (this.viewType != item.viewType) {
                return false;
            }
            CacheByChatsController.KeepMediaException keepMediaException2 = this.exception;
            return keepMediaException2 == null || (keepMediaException = item.exception) == null || keepMediaException2.dialogId == keepMediaException.dialogId;
        }
    }
}