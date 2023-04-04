package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_chatlists_chatlistInvite;
import org.telegram.tgnet.TLRPC$TL_chatlists_chatlistInviteAlready;
import org.telegram.tgnet.TLRPC$TL_chatlists_chatlistUpdates;
import org.telegram.tgnet.TLRPC$TL_chatlists_getLeaveChatlistSuggestions;
import org.telegram.tgnet.TLRPC$TL_chatlists_hideChatlistUpdates;
import org.telegram.tgnet.TLRPC$TL_chatlists_joinChatlistInvite;
import org.telegram.tgnet.TLRPC$TL_chatlists_joinChatlistUpdates;
import org.telegram.tgnet.TLRPC$TL_chatlists_leaveChatlist;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputChatlistDialogFilter;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_updateDialogFilter;
import org.telegram.tgnet.TLRPC$Update;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$chatlist_ChatlistInvite;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.FolderBottomSheet;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.FilterCreateActivity;
import org.telegram.ui.FiltersSetupActivity;
/* loaded from: classes3.dex */
public class FolderBottomSheet extends BottomSheetWithRecyclerListView {
    private int alreadyHeaderRow;
    private ArrayList<Long> alreadyJoined;
    private ArrayList<TLRPC$Peer> alreadyPeers;
    private int alreadySectionRow;
    private int alreadyUsersEndRow;
    private int alreadyUsersStartRow;
    private FrameLayout bulletinContainer;
    private Button button;
    private View buttonShadow;
    private boolean deleting;
    private String escapedTitle;
    private int filterId;
    private HeaderCell headerCell;
    private int headerRow;
    private TLRPC$chatlist_ChatlistInvite invite;
    private long lastClicked;
    private long lastClickedDialogId;
    private Utilities.Callback<Boolean> onDone;
    private ArrayList<TLRPC$Peer> peers;
    private int rowsCount;
    private int sectionRow;
    private ArrayList<Long> selectedPeers;
    private int shiftDp;
    private String slug;
    private boolean success;
    private String title;
    private TitleCell titleCell;
    private int titleRow;
    private TLRPC$TL_chatlists_chatlistUpdates updates;
    private int usersEndRow;
    private int usersSectionRow;
    private int usersStartRow;

    public static void showForDeletion(final BaseFragment baseFragment, final int i, final Utilities.Callback<Boolean> callback) {
        MessagesController.DialogFilter dialogFilter;
        ArrayList<MessagesController.DialogFilter> arrayList = baseFragment.getMessagesController().dialogFilters;
        if (arrayList != null) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (arrayList.get(i2).id == i) {
                    dialogFilter = arrayList.get(i2);
                    break;
                }
            }
        }
        dialogFilter = null;
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FolderBottomSheet.lambda$showForDeletion$2(i, baseFragment, callback);
            }
        };
        if (dialogFilter != null && dialogFilter.isMyChatlist()) {
            AlertDialog create = new AlertDialog.Builder(baseFragment.getContext()).setTitle(LocaleController.getString("FilterDelete", R.string.FilterDelete)).setMessage(LocaleController.getString("FilterDeleteAlertLinks", R.string.FilterDeleteAlertLinks)).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    FolderBottomSheet.lambda$showForDeletion$3(Utilities.Callback.this, dialogInterface, i3);
                }
            }).setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    runnable.run();
                }
            }).create();
            baseFragment.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("text_RedBold"));
                return;
            }
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showForDeletion$2(final int i, final BaseFragment baseFragment, final Utilities.Callback callback) {
        TLRPC$TL_chatlists_getLeaveChatlistSuggestions tLRPC$TL_chatlists_getLeaveChatlistSuggestions = new TLRPC$TL_chatlists_getLeaveChatlistSuggestions();
        TLRPC$TL_inputChatlistDialogFilter tLRPC$TL_inputChatlistDialogFilter = new TLRPC$TL_inputChatlistDialogFilter();
        tLRPC$TL_chatlists_getLeaveChatlistSuggestions.chatlist = tLRPC$TL_inputChatlistDialogFilter;
        tLRPC$TL_inputChatlistDialogFilter.filter_id = i;
        baseFragment.getConnectionsManager().sendRequest(tLRPC$TL_chatlists_getLeaveChatlistSuggestions, new RequestDelegate() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda15
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                FolderBottomSheet.lambda$showForDeletion$1(BaseFragment.this, i, callback, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showForDeletion$1(final BaseFragment baseFragment, final int i, final Utilities.Callback callback, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                FolderBottomSheet.lambda$showForDeletion$0(TLObject.this, baseFragment, i, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showForDeletion$0(TLObject tLObject, BaseFragment baseFragment, int i, Utilities.Callback callback) {
        FolderBottomSheet folderBottomSheet;
        if (tLObject instanceof TLRPC$Vector) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < ((TLRPC$Vector) tLObject).objects.size(); i2++) {
                try {
                    arrayList.add(Long.valueOf(DialogObject.getPeerDialogId((TLRPC$Peer) ((TLRPC$Vector) tLObject).objects.get(i2))));
                } catch (Exception unused) {
                }
            }
            folderBottomSheet = new FolderBottomSheet(baseFragment, i, arrayList);
        } else {
            folderBottomSheet = new FolderBottomSheet(baseFragment, i, (List<Long>) null);
        }
        folderBottomSheet.setOnDone(callback);
        baseFragment.showDialog(folderBottomSheet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showForDeletion$3(Utilities.Callback callback, DialogInterface dialogInterface, int i) {
        if (callback != null) {
            callback.run(Boolean.FALSE);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FolderBottomSheet(BaseFragment baseFragment, int i, List<Long> list) {
        super(baseFragment, false, false);
        TLRPC$Chat chat;
        this.filterId = -1;
        this.title = "";
        this.escapedTitle = "";
        this.alreadyJoined = new ArrayList<>();
        this.selectedPeers = new ArrayList<>();
        this.shiftDp = -5;
        this.filterId = i;
        this.deleting = true;
        this.peers = new ArrayList<>();
        this.selectedPeers.clear();
        if (list != null) {
            this.selectedPeers.addAll(list);
        }
        ArrayList<MessagesController.DialogFilter> arrayList = baseFragment.getMessagesController().dialogFilters;
        MessagesController.DialogFilter dialogFilter = null;
        if (arrayList != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= arrayList.size()) {
                    break;
                } else if (arrayList.get(i2).id == i) {
                    dialogFilter = arrayList.get(i2);
                    break;
                } else {
                    i2++;
                }
            }
        }
        if (dialogFilter != null) {
            this.title = dialogFilter.name;
            for (int i3 = 0; i3 < this.selectedPeers.size(); i3++) {
                TLRPC$Peer peer = baseFragment.getMessagesController().getPeer(this.selectedPeers.get(i3).longValue());
                if ((peer instanceof TLRPC$TL_peerChat) || (peer instanceof TLRPC$TL_peerChannel)) {
                    this.peers.add(peer);
                }
            }
            for (int i4 = 0; i4 < dialogFilter.alwaysShow.size(); i4++) {
                long longValue = dialogFilter.alwaysShow.get(i4).longValue();
                if (!this.selectedPeers.contains(Long.valueOf(longValue))) {
                    TLRPC$Peer peer2 = baseFragment.getMessagesController().getPeer(longValue);
                    if (((peer2 instanceof TLRPC$TL_peerChat) || (peer2 instanceof TLRPC$TL_peerChannel)) && ((chat = baseFragment.getMessagesController().getChat(Long.valueOf(-longValue))) == null || !ChatObject.isNotInChat(chat))) {
                        this.peers.add(peer2);
                    }
                }
            }
        }
        init();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FolderBottomSheet(BaseFragment baseFragment, int i, TLRPC$TL_chatlists_chatlistUpdates tLRPC$TL_chatlists_chatlistUpdates) {
        super(baseFragment, false, false);
        int i2 = 0;
        this.filterId = -1;
        this.title = "";
        this.escapedTitle = "";
        this.alreadyJoined = new ArrayList<>();
        ArrayList<Long> arrayList = new ArrayList<>();
        this.selectedPeers = arrayList;
        this.shiftDp = -5;
        this.filterId = i;
        this.updates = tLRPC$TL_chatlists_chatlistUpdates;
        arrayList.clear();
        this.peers = tLRPC$TL_chatlists_chatlistUpdates.missing_peers;
        ArrayList<MessagesController.DialogFilter> arrayList2 = baseFragment.getMessagesController().dialogFilters;
        if (arrayList2 != null) {
            while (true) {
                if (i2 >= arrayList2.size()) {
                    break;
                } else if (arrayList2.get(i2).id == i) {
                    this.title = arrayList2.get(i2).name;
                    break;
                } else {
                    i2++;
                }
            }
        }
        init();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FolderBottomSheet(BaseFragment baseFragment, String str, TLRPC$chatlist_ChatlistInvite tLRPC$chatlist_ChatlistInvite) {
        super(baseFragment, false, false);
        int i = 0;
        this.filterId = -1;
        this.title = "";
        this.escapedTitle = "";
        this.alreadyJoined = new ArrayList<>();
        ArrayList<Long> arrayList = new ArrayList<>();
        this.selectedPeers = arrayList;
        this.shiftDp = -5;
        this.slug = str;
        this.invite = tLRPC$chatlist_ChatlistInvite;
        arrayList.clear();
        if (tLRPC$chatlist_ChatlistInvite instanceof TLRPC$TL_chatlists_chatlistInvite) {
            TLRPC$TL_chatlists_chatlistInvite tLRPC$TL_chatlists_chatlistInvite = (TLRPC$TL_chatlists_chatlistInvite) tLRPC$chatlist_ChatlistInvite;
            this.title = tLRPC$TL_chatlists_chatlistInvite.title;
            this.peers = tLRPC$TL_chatlists_chatlistInvite.peers;
        } else if (tLRPC$chatlist_ChatlistInvite instanceof TLRPC$TL_chatlists_chatlistInviteAlready) {
            TLRPC$TL_chatlists_chatlistInviteAlready tLRPC$TL_chatlists_chatlistInviteAlready = (TLRPC$TL_chatlists_chatlistInviteAlready) tLRPC$chatlist_ChatlistInvite;
            this.peers = tLRPC$TL_chatlists_chatlistInviteAlready.missing_peers;
            this.alreadyPeers = tLRPC$TL_chatlists_chatlistInviteAlready.already_peers;
            this.filterId = tLRPC$TL_chatlists_chatlistInviteAlready.filter_id;
            ArrayList<MessagesController.DialogFilter> arrayList2 = baseFragment.getMessagesController().dialogFilters;
            if (arrayList2 != null) {
                while (true) {
                    if (i >= arrayList2.size()) {
                        break;
                    } else if (arrayList2.get(i).id == this.filterId) {
                        this.title = arrayList2.get(i).name;
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        init();
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0077  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void init() {
        long j;
        boolean isNotInChat;
        boolean z;
        this.escapedTitle = this.title.replace('*', (char) 10033);
        if (this.peers != null) {
            for (int i = 0; i < this.peers.size(); i++) {
                TLRPC$Peer tLRPC$Peer = this.peers.get(i);
                if (tLRPC$Peer != null) {
                    if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                        j = tLRPC$Peer.user_id;
                    } else {
                        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                            j = -tLRPC$Peer.chat_id;
                            isNotInChat = ChatObject.isNotInChat(getBaseFragment().getMessagesController().getChat(Long.valueOf(-j)));
                        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                            j = -tLRPC$Peer.channel_id;
                            isNotInChat = ChatObject.isNotInChat(getBaseFragment().getMessagesController().getChat(Long.valueOf(-j)));
                        } else {
                            j = 0;
                        }
                        z = !isNotInChat;
                        if (j != 0 && !this.deleting) {
                            if (z) {
                                this.alreadyJoined.add(Long.valueOf(j));
                            }
                            this.selectedPeers.add(Long.valueOf(j));
                        }
                    }
                    z = false;
                    if (j != 0) {
                        if (z) {
                        }
                        this.selectedPeers.add(Long.valueOf(j));
                    }
                }
            }
        }
        this.rowsCount = 0;
        this.rowsCount = 0 + 1;
        this.titleRow = 0;
        ArrayList<TLRPC$Peer> arrayList = this.peers;
        if (arrayList != null && !arrayList.isEmpty()) {
            int i2 = this.rowsCount;
            int i3 = i2 + 1;
            this.rowsCount = i3;
            this.sectionRow = i2;
            int i4 = i3 + 1;
            this.rowsCount = i4;
            this.headerRow = i3;
            this.usersStartRow = i4;
            int size = i4 + this.peers.size();
            this.rowsCount = size;
            this.usersEndRow = size;
        } else {
            this.sectionRow = -1;
            this.headerRow = -1;
            this.usersStartRow = -1;
            this.usersEndRow = -1;
        }
        int i5 = this.rowsCount;
        this.rowsCount = i5 + 1;
        this.usersSectionRow = i5;
        ArrayList<TLRPC$Peer> arrayList2 = this.alreadyPeers;
        if (arrayList2 != null && !arrayList2.isEmpty()) {
            int i6 = this.rowsCount;
            int i7 = i6 + 1;
            this.rowsCount = i7;
            this.alreadyHeaderRow = i6;
            this.alreadyUsersStartRow = i7;
            int size2 = i7 + this.alreadyPeers.size();
            this.rowsCount = size2;
            this.alreadyUsersEndRow = size2;
            this.rowsCount = size2 + 1;
            this.alreadySectionRow = size2;
        } else {
            this.alreadyHeaderRow = -1;
            this.alreadyUsersStartRow = -1;
            this.alreadyUsersEndRow = -1;
            this.alreadySectionRow = -1;
        }
        Button button = new Button(getContext(), "");
        this.button = button;
        button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FolderBottomSheet.this.lambda$init$5(view);
            }
        });
        this.containerView.addView(this.button, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 10.0f, 16.0f, 10.0f));
        View view = new View(getContext());
        this.buttonShadow = view;
        view.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.containerView.addView(this.buttonShadow, LayoutHelper.createFrame(-1, 1.0f / AndroidUtilities.density, 87, 6.0f, 0.0f, 6.0f, 68.0f));
        this.recyclerListView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(this.button != null ? 68.0f : 0.0f));
        FrameLayout frameLayout = new FrameLayout(getContext());
        this.bulletinContainer = frameLayout;
        this.containerView.addView(frameLayout, LayoutHelper.createFrame(-1, 100.0f, 87, 6.0f, 0.0f, 6.0f, 68.0f));
        fixNavigationBar(Theme.getColor("dialogBackground"));
        updateCount(false);
        this.actionBar.setTitle(getTitle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$5(View view) {
        onJoinButtonClicked();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:64:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onJoinButtonClicked() {
        final TLRPC$TL_chatlists_joinChatlistInvite tLRPC$TL_chatlists_joinChatlistInvite;
        final Utilities.Callback callback;
        boolean z;
        UndoView undoView;
        UndoView undoView2;
        ArrayList<TLRPC$Peer> arrayList = this.peers;
        if (arrayList == null) {
            dismiss();
        } else if (arrayList.isEmpty() && !this.deleting) {
            dismiss();
        } else if (this.selectedPeers.isEmpty() && (this.invite instanceof TLRPC$TL_chatlists_chatlistInvite)) {
            Button button = this.button;
            int i = -this.shiftDp;
            this.shiftDp = i;
            AndroidUtilities.shakeViewSpring(button, i);
            BotWebViewVibrationEffect.APP_ERROR.vibrate();
        } else {
            final ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < this.peers.size(); i2++) {
                long peerDialogId = DialogObject.getPeerDialogId(this.peers.get(i2));
                if (this.selectedPeers.contains(Long.valueOf(peerDialogId))) {
                    arrayList2.add(getBaseFragment().getMessagesController().getInputPeer(peerDialogId));
                }
            }
            if (this.deleting) {
                TLRPC$TL_chatlists_leaveChatlist tLRPC$TL_chatlists_leaveChatlist = new TLRPC$TL_chatlists_leaveChatlist();
                TLRPC$TL_inputChatlistDialogFilter tLRPC$TL_inputChatlistDialogFilter = new TLRPC$TL_inputChatlistDialogFilter();
                tLRPC$TL_chatlists_leaveChatlist.chatlist = tLRPC$TL_inputChatlistDialogFilter;
                tLRPC$TL_inputChatlistDialogFilter.filter_id = this.filterId;
                tLRPC$TL_chatlists_leaveChatlist.peers.addAll(arrayList2);
                tLRPC$TL_chatlists_joinChatlistInvite = tLRPC$TL_chatlists_leaveChatlist;
            } else if (this.updates != null) {
                if (arrayList2.isEmpty()) {
                    TLRPC$TL_chatlists_hideChatlistUpdates tLRPC$TL_chatlists_hideChatlistUpdates = new TLRPC$TL_chatlists_hideChatlistUpdates();
                    TLRPC$TL_inputChatlistDialogFilter tLRPC$TL_inputChatlistDialogFilter2 = new TLRPC$TL_inputChatlistDialogFilter();
                    tLRPC$TL_chatlists_hideChatlistUpdates.chatlist = tLRPC$TL_inputChatlistDialogFilter2;
                    tLRPC$TL_inputChatlistDialogFilter2.filter_id = this.filterId;
                    getBaseFragment().getConnectionsManager().sendRequest(tLRPC$TL_chatlists_hideChatlistUpdates, null);
                    getBaseFragment().getMessagesController().invalidateChatlistFolderUpdate(this.filterId);
                    dismiss();
                    return;
                }
                TLRPC$TL_chatlists_joinChatlistUpdates tLRPC$TL_chatlists_joinChatlistUpdates = new TLRPC$TL_chatlists_joinChatlistUpdates();
                TLRPC$TL_inputChatlistDialogFilter tLRPC$TL_inputChatlistDialogFilter3 = new TLRPC$TL_inputChatlistDialogFilter();
                tLRPC$TL_chatlists_joinChatlistUpdates.chatlist = tLRPC$TL_inputChatlistDialogFilter3;
                tLRPC$TL_inputChatlistDialogFilter3.filter_id = this.filterId;
                tLRPC$TL_chatlists_joinChatlistUpdates.peers.addAll(arrayList2);
                tLRPC$TL_chatlists_joinChatlistInvite = tLRPC$TL_chatlists_joinChatlistUpdates;
            } else if ((this.invite instanceof TLRPC$TL_chatlists_chatlistInviteAlready) && arrayList2.isEmpty()) {
                dismiss();
                return;
            } else {
                TLRPC$TL_chatlists_joinChatlistInvite tLRPC$TL_chatlists_joinChatlistInvite2 = new TLRPC$TL_chatlists_joinChatlistInvite();
                tLRPC$TL_chatlists_joinChatlistInvite2.slug = this.slug;
                tLRPC$TL_chatlists_joinChatlistInvite2.peers.addAll(arrayList2);
                tLRPC$TL_chatlists_joinChatlistInvite = tLRPC$TL_chatlists_joinChatlistInvite2;
            }
            final INavigationLayout parentLayout = getBaseFragment().getParentLayout();
            if (!this.deleting) {
                if (parentLayout != null) {
                    if (this.updates != null || (this.invite instanceof TLRPC$TL_chatlists_chatlistInviteAlready)) {
                        callback = new Utilities.Callback() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda12
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                FolderBottomSheet.this.lambda$onJoinButtonClicked$8(parentLayout, arrayList2, (Integer) obj);
                            }
                        };
                    } else {
                        callback = new Utilities.Callback() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda13
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                FolderBottomSheet.this.lambda$onJoinButtonClicked$11(parentLayout, arrayList2, (Integer) obj);
                            }
                        };
                    }
                    int i3 = 0;
                    while (true) {
                        if (i3 >= arrayList2.size()) {
                            z = false;
                            break;
                        } else if (!this.alreadyJoined.contains(Long.valueOf(DialogObject.getPeerDialogId((TLRPC$InputPeer) arrayList2.get(i3))))) {
                            z = true;
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (z) {
                        boolean[] zArr = new boolean[1];
                        getBaseFragment().getMessagesController().ensureFolderDialogExists(1, zArr);
                        if (zArr[0]) {
                            getBaseFragment().getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        }
                    }
                    getBaseFragment().getConnectionsManager().sendRequest(tLRPC$TL_chatlists_joinChatlistInvite, new RequestDelegate() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda16
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            FolderBottomSheet.this.lambda$onJoinButtonClicked$14(callback, tLObject, tLRPC$TL_error);
                        }
                    });
                }
            } else if (parentLayout != null) {
                BaseFragment lastFragment = parentLayout.getLastFragment();
                if (lastFragment instanceof ChatActivity) {
                    undoView2 = ((ChatActivity) lastFragment).getUndoView();
                } else if (lastFragment instanceof DialogsActivity) {
                    undoView2 = ((DialogsActivity) lastFragment).getUndoView();
                } else if (lastFragment instanceof FiltersSetupActivity) {
                    undoView2 = ((FiltersSetupActivity) lastFragment).getUndoView();
                } else {
                    if (lastFragment instanceof FilterCreateActivity) {
                        List<BaseFragment> fragmentStack = parentLayout.getFragmentStack();
                        if (fragmentStack.size() >= 2 && (fragmentStack.get(fragmentStack.size() - 2) instanceof FiltersSetupActivity)) {
                            lastFragment.finishFragment();
                            undoView2 = ((FiltersSetupActivity) fragmentStack.get(fragmentStack.size() - 2)).getUndoView();
                        }
                    }
                    undoView = null;
                    if (undoView != null) {
                        getBaseFragment().getConnectionsManager().sendRequest(tLRPC$TL_chatlists_joinChatlistInvite, null);
                        BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.ic_delete, LocaleController.formatString("FolderLinkDeletedTitle", R.string.FolderLinkDeletedTitle, this.title), LocaleController.formatPluralString("FolderLinkDeletedSubtitle", arrayList2.size(), new Object[0])).setDuration(5000).show();
                        this.success = true;
                        dismiss();
                        getBaseFragment().getMessagesController().invalidateChatlistFolderUpdate(this.filterId);
                        return;
                    }
                    ArrayList<Long> arrayList3 = new ArrayList<>();
                    for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                        arrayList3.add(Long.valueOf(DialogObject.getPeerDialogId((TLRPC$InputPeer) arrayList2.get(i4))));
                    }
                    final Pair<Runnable, Runnable> removeFolderTemporarily = getBaseFragment().getMessagesController().removeFolderTemporarily(this.filterId, arrayList3);
                    undoView.showWithAction(0L, 88, this.title, Integer.valueOf(arrayList2.size()), new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            FolderBottomSheet.this.lambda$onJoinButtonClicked$7(tLRPC$TL_chatlists_joinChatlistInvite, removeFolderTemporarily);
                        }
                    }, (Runnable) removeFolderTemporarily.second);
                    this.success = true;
                    dismiss();
                    getBaseFragment().getMessagesController().invalidateChatlistFolderUpdate(this.filterId);
                    return;
                }
                undoView = undoView2;
                if (undoView != null) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onJoinButtonClicked$6(Pair pair, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread((Runnable) pair.first);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onJoinButtonClicked$7(TLObject tLObject, final Pair pair) {
        getBaseFragment().getConnectionsManager().sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda14
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                FolderBottomSheet.lambda$onJoinButtonClicked$6(pair, tLObject2, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onJoinButtonClicked$8(INavigationLayout iNavigationLayout, ArrayList arrayList, Integer num) {
        BulletinFactory.of(iNavigationLayout.getLastFragment()).createSimpleBulletin(R.raw.folder_in, AndroidUtilities.replaceTags(LocaleController.formatString("FolderLinkUpdatedTitle", R.string.FolderLinkUpdatedTitle, this.escapedTitle)), LocaleController.formatPluralString("FolderLinkUpdatedSubtitle", arrayList.size(), new Object[0])).setDuration(5000).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onJoinButtonClicked$11(INavigationLayout iNavigationLayout, final ArrayList arrayList, final Integer num) {
        List<BaseFragment> fragmentStack = iNavigationLayout.getFragmentStack();
        boolean z = true;
        final BaseFragment baseFragment = null;
        for (int size = fragmentStack.size() - 1; size >= 0; size--) {
            baseFragment = fragmentStack.get(size);
            if (baseFragment instanceof DialogsActivity) {
                break;
            }
            if (z) {
                baseFragment.finishFragment();
                z = false;
            } else {
                baseFragment.removeSelfFromStack();
            }
        }
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                FolderBottomSheet.this.lambda$onJoinButtonClicked$9(baseFragment, arrayList);
            }
        };
        if (baseFragment instanceof DialogsActivity) {
            final DialogsActivity dialogsActivity = (DialogsActivity) baseFragment;
            dialogsActivity.closeSearching();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    FolderBottomSheet.lambda$onJoinButtonClicked$10(DialogsActivity.this, num, runnable);
                }
            }, 80L);
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onJoinButtonClicked$9(BaseFragment baseFragment, ArrayList arrayList) {
        BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.contact_check, AndroidUtilities.replaceTags(LocaleController.formatString("FolderLinkAddedTitle", R.string.FolderLinkAddedTitle, this.escapedTitle)), LocaleController.formatPluralString("FolderLinkAddedSubtitle", arrayList.size(), new Object[0])).setDuration(5000).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onJoinButtonClicked$10(DialogsActivity dialogsActivity, Integer num, Runnable runnable) {
        dialogsActivity.scrollToFolder(num.intValue());
        AndroidUtilities.runOnUIThread(runnable, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onJoinButtonClicked$14(final Utilities.Callback callback, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FolderBottomSheet.this.lambda$onJoinButtonClicked$13(tLObject, callback, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onJoinButtonClicked$13(TLObject tLObject, final Utilities.Callback callback, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            final int i = -1;
            if (tLObject instanceof TLRPC$Updates) {
                TLRPC$Updates tLRPC$Updates = (TLRPC$Updates) tLObject;
                ArrayList<TLRPC$Update> arrayList = tLRPC$Updates.updates;
                if (arrayList.isEmpty()) {
                    TLRPC$Update tLRPC$Update = tLRPC$Updates.update;
                    if (tLRPC$Update instanceof TLRPC$TL_updateDialogFilter) {
                        i = ((TLRPC$TL_updateDialogFilter) tLRPC$Update).id;
                    }
                } else {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= arrayList.size()) {
                            break;
                        } else if (arrayList.get(i2) instanceof TLRPC$TL_updateDialogFilter) {
                            i = ((TLRPC$TL_updateDialogFilter) arrayList.get(i2)).id;
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            }
            if (this.invite instanceof TLRPC$TL_chatlists_chatlistInvite) {
                getBaseFragment().getMessagesController().loadRemoteFilters(true, new Utilities.Callback() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda11
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        FolderBottomSheet.this.lambda$onJoinButtonClicked$12(callback, i, (Boolean) obj);
                    }
                });
                return;
            }
            if (this.updates != null) {
                getBaseFragment().getMessagesController().checkChatlistFolderUpdate(this.filterId, true);
            }
            this.success = true;
            dismiss();
            callback.run(Integer.valueOf(i));
        } else if (tLRPC$TL_error != null && "CHATLISTS_TOO_MUCH".equals(tLRPC$TL_error.text)) {
            new LimitReachedBottomSheet(getBaseFragment(), getContext(), 13, this.currentAccount).show();
        } else if (tLRPC$TL_error != null && "CHANNELS_TOO_MUCH".equals(tLRPC$TL_error.text)) {
            new LimitReachedBottomSheet(getBaseFragment(), getContext(), 5, this.currentAccount).show();
        } else if (tLRPC$TL_error != null && "FILTER_INCLUDE_TOO_MUCH".equals(tLRPC$TL_error.text)) {
            new LimitReachedBottomSheet(getBaseFragment(), getContext(), 4, this.currentAccount).show();
        } else if (tLRPC$TL_error != null && "DIALOG_FILTERS_TOO_MUCH".equals(tLRPC$TL_error.text)) {
            new LimitReachedBottomSheet(getBaseFragment(), getContext(), 3, this.currentAccount).show();
        } else {
            BulletinFactory.of(this.bulletinContainer, null).createErrorBulletin(LocaleController.getString("UnknownError", R.string.UnknownError)).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onJoinButtonClicked$12(Utilities.Callback callback, int i, Boolean bool) {
        this.success = bool.booleanValue();
        dismiss();
        callback.run(Integer.valueOf(i));
    }

    public void setOnDone(Utilities.Callback<Boolean> callback) {
        this.onDone = callback;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        Utilities.Callback<Boolean> callback = this.onDone;
        if (callback != null) {
            callback.run(Boolean.valueOf(this.success));
            this.onDone = null;
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public void onViewCreated(FrameLayout frameLayout) {
        super.onViewCreated(frameLayout);
        this.recyclerListView.setOverScrollMode(2);
        this.recyclerListView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(this.button != null ? 68.0f : 0.0f));
        this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda17
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                FolderBottomSheet.this.lambda$onViewCreated$15(view, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$15(View view, int i) {
        int i2;
        String string;
        String str;
        if (!(view instanceof GroupCreateUserCell) || (i2 = (i - 1) - this.usersStartRow) < 0 || i2 >= this.peers.size()) {
            return;
        }
        long peerDialogId = DialogObject.getPeerDialogId(this.peers.get(i2));
        if (this.selectedPeers.contains(Long.valueOf(peerDialogId))) {
            if (this.alreadyJoined.contains(Long.valueOf(peerDialogId))) {
                int i3 = -this.shiftDp;
                this.shiftDp = i3;
                AndroidUtilities.shakeViewSpring(view, i3);
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
                ArrayList arrayList = new ArrayList();
                if (peerDialogId >= 0) {
                    arrayList.add(getBaseFragment().getMessagesController().getUser(Long.valueOf(peerDialogId)));
                    str = "beep boop.";
                } else {
                    TLRPC$Chat chat = getBaseFragment().getMessagesController().getChat(Long.valueOf(-peerDialogId));
                    if (ChatObject.isChannelAndNotMegaGroup(chat)) {
                        string = LocaleController.getString("FolderLinkAlreadySubscribed", R.string.FolderLinkAlreadySubscribed);
                    } else {
                        string = LocaleController.getString("FolderLinkAlreadyJoined", R.string.FolderLinkAlreadyJoined);
                    }
                    arrayList.add(chat);
                    str = string;
                }
                if (this.lastClickedDialogId != peerDialogId || System.currentTimeMillis() - this.lastClicked > 1500) {
                    this.lastClickedDialogId = peerDialogId;
                    this.lastClicked = System.currentTimeMillis();
                    BulletinFactory.of(this.bulletinContainer, null).createChatsBulletin(arrayList, str, null).setDuration(1500).show();
                    return;
                }
                return;
            }
            this.selectedPeers.remove(Long.valueOf(peerDialogId));
            ((GroupCreateUserCell) view).setChecked(false, true);
        } else {
            this.selectedPeers.add(Long.valueOf(peerDialogId));
            ((GroupCreateUserCell) view).setChecked(true, true);
        }
        updateCount(true);
        updateHeaderCell(true);
        announceSelection(false);
    }

    public void updateCount(boolean z) {
        int i;
        String str;
        int i2;
        String str2;
        int size = this.selectedPeers.size();
        Button button = this.button;
        if (button != null) {
            if (this.deleting) {
                if (size > 0) {
                    i2 = R.string.FolderLinkButtonRemoveChats;
                    str2 = "FolderLinkButtonRemoveChats";
                } else {
                    i2 = R.string.FolderLinkButtonRemove;
                    str2 = "FolderLinkButtonRemove";
                }
                button.setText(LocaleController.getString(str2, i2), z);
            } else {
                ArrayList<TLRPC$Peer> arrayList = this.peers;
                if (arrayList == null || arrayList.isEmpty()) {
                    this.button.setText(LocaleController.getString("OK", R.string.OK), z);
                } else if (this.invite instanceof TLRPC$TL_chatlists_chatlistInvite) {
                    this.button.setText(LocaleController.formatString("FolderLinkButtonAdd", R.string.FolderLinkButtonAdd, this.title), z);
                } else {
                    Button button2 = this.button;
                    if (size > 0) {
                        i = R.string.FolderLinkButtonJoin;
                        str = "FolderLinkButtonJoin";
                    } else {
                        i = R.string.FolderLinkButtonNone;
                        str = "FolderLinkButtonNone";
                    }
                    button2.setText(LocaleController.getString(str, i), z);
                }
            }
            this.button.setCount(size, z);
            if (this.invite instanceof TLRPC$TL_chatlists_chatlistInvite) {
                this.button.setEnabled(!this.selectedPeers.isEmpty());
            }
        }
        TitleCell titleCell = this.titleCell;
        if (titleCell != null) {
            titleCell.setSelectedCount(size, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Button extends FrameLayout {
        float countAlpha;
        AnimatedFloat countAlphaAnimated;
        private ValueAnimator countAnimator;
        private float countScale;
        AnimatedTextView.AnimatedTextDrawable countText;
        private boolean enabled;
        private ValueAnimator enabledAnimator;
        private float enabledT;
        private int lastCount;
        Paint paint;
        private View rippleView;
        AnimatedTextView.AnimatedTextDrawable text;

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            return false;
        }

        public Button(Context context, String str) {
            super(context);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.countAlphaAnimated = new AnimatedFloat(350L, cubicBezierInterpolator);
            this.countScale = 1.0f;
            this.enabledT = 1.0f;
            this.enabled = true;
            View view = new View(context);
            this.rippleView = view;
            view.setBackground(Theme.AdaptiveRipple.rect(Theme.getColor("featuredStickers_addButton"), 8.0f));
            addView(this.rippleView, LayoutHelper.createFrame(-1, -1.0f));
            setBackground((ShapeDrawable) Theme.createRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor("featuredStickers_addButton")));
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setColor(Theme.getColor("featuredStickers_buttonText"));
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, false);
            this.text = animatedTextDrawable;
            animatedTextDrawable.setAnimationProperties(0.3f, 0L, 250L, cubicBezierInterpolator);
            this.text.setCallback(this);
            this.text.setTextSize(AndroidUtilities.dp(14.0f));
            this.text.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.text.setTextColor(Theme.getColor("featuredStickers_buttonText"));
            this.text.setText(str);
            this.text.setGravity(1);
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = new AnimatedTextView.AnimatedTextDrawable(false, false, true);
            this.countText = animatedTextDrawable2;
            animatedTextDrawable2.setAnimationProperties(0.3f, 0L, 250L, cubicBezierInterpolator);
            this.countText.setCallback(this);
            this.countText.setTextSize(AndroidUtilities.dp(12.0f));
            this.countText.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.countText.setTextColor(Theme.getColor("featuredStickers_addButton"));
            this.countText.setText("");
            this.countText.setGravity(1);
            setWillNotDraw(false);
        }

        public void setText(String str, boolean z) {
            if (z) {
                this.text.cancelAnimation();
            }
            this.text.setText(str, z);
            invalidate();
        }

        private void animateCount() {
            ValueAnimator valueAnimator = this.countAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.countAnimator = null;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.countAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.FolderBottomSheet$Button$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    FolderBottomSheet.Button.this.lambda$animateCount$0(valueAnimator2);
                }
            });
            this.countAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.FolderBottomSheet.Button.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Button.this.countScale = 1.0f;
                    Button.this.invalidate();
                }
            });
            this.countAnimator.setInterpolator(new OvershootInterpolator(2.0f));
            this.countAnimator.setDuration(200L);
            this.countAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateCount$0(ValueAnimator valueAnimator) {
            this.countScale = Math.max(1.0f, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            invalidate();
        }

        public void setCount(int i, boolean z) {
            int i2;
            if (z) {
                this.countText.cancelAnimation();
            }
            if (z && i != (i2 = this.lastCount) && i > 0 && i2 > 0) {
                animateCount();
            }
            this.lastCount = i;
            this.countAlpha = i != 0 ? 1.0f : 0.0f;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.countText;
            animatedTextDrawable.setText("" + i, z);
            invalidate();
        }

        @Override // android.view.View
        public void setEnabled(boolean z) {
            if (this.enabled != z) {
                ValueAnimator valueAnimator = this.enabledAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.enabledAnimator = null;
                }
                float[] fArr = new float[2];
                fArr[0] = this.enabledT;
                this.enabled = z;
                fArr[1] = z ? 1.0f : 0.0f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                this.enabledAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.FolderBottomSheet$Button$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        FolderBottomSheet.Button.this.lambda$setEnabled$1(valueAnimator2);
                    }
                });
                this.enabledAnimator.addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.Components.FolderBottomSheet.Button.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                    }
                });
                this.enabledAnimator.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setEnabled$1(ValueAnimator valueAnimator) {
            this.enabledT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return this.text == drawable || this.countText == drawable || super.verifyDrawable(drawable);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.rippleView.draw(canvas);
            float currentWidth = this.text.getCurrentWidth();
            float f = this.countAlphaAnimated.set(this.countAlpha);
            float dp = ((AndroidUtilities.dp(15.66f) + this.countText.getCurrentWidth()) * f) + currentWidth;
            android.graphics.Rect rect = AndroidUtilities.rectTmp2;
            rect.set((int) (((getMeasuredWidth() - dp) - getWidth()) / 2.0f), (int) (((getMeasuredHeight() - this.text.getHeight()) / 2.0f) - AndroidUtilities.dp(1.0f)), (int) ((((getMeasuredWidth() - dp) + getWidth()) / 2.0f) + currentWidth), (int) (((getMeasuredHeight() + this.text.getHeight()) / 2.0f) - AndroidUtilities.dp(1.0f)));
            this.text.setAlpha((int) (AndroidUtilities.lerp(0.5f, 1.0f, this.enabledT) * 255.0f));
            this.text.setBounds(rect);
            this.text.draw(canvas);
            rect.set((int) (((getMeasuredWidth() - dp) / 2.0f) + currentWidth + AndroidUtilities.dp(5.0f)), (int) ((getMeasuredHeight() - AndroidUtilities.dp(18.0f)) / 2.0f), (int) (((getMeasuredWidth() - dp) / 2.0f) + currentWidth + AndroidUtilities.dp(13.0f) + Math.max(AndroidUtilities.dp(9.0f), this.countText.getCurrentWidth())), (int) ((getMeasuredHeight() + AndroidUtilities.dp(18.0f)) / 2.0f));
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(rect);
            if (this.countScale != 1.0f) {
                canvas.save();
                float f2 = this.countScale;
                canvas.scale(f2, f2, rect.centerX(), rect.centerY());
            }
            float f3 = 255.0f * f;
            this.paint.setAlpha((int) (f * f3));
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.paint);
            rect.offset(-AndroidUtilities.dp(0.3f), -AndroidUtilities.dp(0.4f));
            this.countText.setAlpha((int) f3);
            this.countText.setBounds(rect);
            this.countText.draw(canvas);
            if (this.countScale != 1.0f) {
                canvas.restore();
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            String str;
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("android.widget.Button");
            StringBuilder sb = new StringBuilder();
            sb.append((Object) this.text.getText());
            if (this.lastCount > 0) {
                str = ", " + LocaleController.formatPluralString("Chats", this.lastCount, new Object[0]);
            } else {
                str = "";
            }
            sb.append(str);
            accessibilityNodeInfo.setContentDescription(sb.toString());
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected CharSequence getTitle() {
        if (this.deleting) {
            return LocaleController.getString("FolderLinkTitleRemove", R.string.FolderLinkTitleRemove);
        }
        if (this.invite instanceof TLRPC$TL_chatlists_chatlistInvite) {
            return LocaleController.getString("FolderLinkTitleAdd", R.string.FolderLinkTitleAdd);
        }
        ArrayList<TLRPC$Peer> arrayList = this.peers;
        if (arrayList == null || arrayList.isEmpty()) {
            return LocaleController.getString("FolderLinkTitleAlready", R.string.FolderLinkTitleAlready);
        }
        return LocaleController.getString("FolderLinkTitleAddChats", R.string.FolderLinkTitleAddChats);
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected RecyclerListView.SelectionAdapter createAdapter() {
        return new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.Components.FolderBottomSheet.1
            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return viewHolder.getItemViewType() == 2 && viewHolder.getAdapterPosition() >= FolderBottomSheet.this.usersStartRow && viewHolder.getAdapterPosition() <= FolderBottomSheet.this.usersEndRow;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view;
                boolean z = false;
                if (i == 0) {
                    FolderBottomSheet folderBottomSheet = FolderBottomSheet.this;
                    FolderBottomSheet folderBottomSheet2 = FolderBottomSheet.this;
                    view = folderBottomSheet.titleCell = new TitleCell(folderBottomSheet2.getContext(), ((FolderBottomSheet.this.invite instanceof TLRPC$TL_chatlists_chatlistInviteAlready) || FolderBottomSheet.this.updates != null) ? true : true, FolderBottomSheet.this.escapedTitle);
                } else if (i == 1) {
                    view = new TextInfoPrivacyCell(FolderBottomSheet.this.getContext());
                    view.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
                } else if (i == 2) {
                    GroupCreateUserCell groupCreateUserCell = new GroupCreateUserCell(FolderBottomSheet.this.getContext(), 1, 0, false);
                    groupCreateUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    view = groupCreateUserCell;
                } else if (i == 3) {
                    view = new HeaderCell(FolderBottomSheet.this.getContext());
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                } else {
                    view = null;
                }
                return new RecyclerListView.Holder(view);
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:39:0x00e1  */
            /* JADX WARN: Removed duplicated region for block: B:52:0x013b  */
            /* JADX WARN: Removed duplicated region for block: B:53:0x013e  */
            /* JADX WARN: Type inference failed for: r11v40 */
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                TLRPC$Peer tLRPC$Peer;
                String str;
                String str2;
                String string;
                TLRPC$Chat chat;
                int itemViewType = viewHolder.getItemViewType();
                TLRPC$User tLRPC$User = null;
                if (itemViewType != 2) {
                    if (itemViewType == 3) {
                        HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                        if (i != FolderBottomSheet.this.alreadyHeaderRow) {
                            FolderBottomSheet.this.headerCell = headerCell;
                            FolderBottomSheet.this.updateHeaderCell(false);
                            return;
                        }
                        headerCell.setText(LocaleController.getString("FolderLinkHeaderAlready", R.string.FolderLinkHeaderAlready), false);
                        headerCell.setAction("", null);
                        return;
                    } else if (itemViewType != 1) {
                        if (itemViewType == 0) {
                            FolderBottomSheet.this.titleCell = (TitleCell) viewHolder.itemView;
                            FolderBottomSheet.this.updateCount(false);
                            return;
                        }
                        return;
                    } else {
                        TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                        textInfoPrivacyCell.setForeground(Theme.getThemedDrawable(FolderBottomSheet.this.getContext(), R.drawable.greydivider, "windowBackgroundGrayShadow"));
                        if (i == FolderBottomSheet.this.alreadySectionRow || i == FolderBottomSheet.this.sectionRow || FolderBottomSheet.this.peers == null || FolderBottomSheet.this.peers.isEmpty()) {
                            textInfoPrivacyCell.setFixedSize(12);
                            textInfoPrivacyCell.setText("");
                            return;
                        }
                        textInfoPrivacyCell.setFixedSize(0);
                        if (FolderBottomSheet.this.deleting) {
                            textInfoPrivacyCell.setText(LocaleController.getString("FolderLinkHintRemove", R.string.FolderLinkHintRemove));
                            return;
                        } else {
                            textInfoPrivacyCell.setText(LocaleController.getString("FolderLinkHint", R.string.FolderLinkHint));
                            return;
                        }
                    }
                }
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
                if (i < FolderBottomSheet.this.usersStartRow || i > FolderBottomSheet.this.usersEndRow) {
                    if (i >= FolderBottomSheet.this.alreadyUsersStartRow && i <= FolderBottomSheet.this.alreadyUsersEndRow && FolderBottomSheet.this.alreadyPeers != null) {
                        tLRPC$Peer = (TLRPC$Peer) FolderBottomSheet.this.alreadyPeers.get(i - FolderBottomSheet.this.alreadyUsersStartRow);
                    }
                    tLRPC$Peer = null;
                } else {
                    if (FolderBottomSheet.this.peers != null) {
                        tLRPC$Peer = (TLRPC$Peer) FolderBottomSheet.this.peers.get(i - FolderBottomSheet.this.usersStartRow);
                    }
                    tLRPC$Peer = null;
                }
                long j = 0;
                if (tLRPC$Peer != null) {
                    if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                        j = tLRPC$Peer.user_id;
                        tLRPC$User = FolderBottomSheet.this.getBaseFragment().getMessagesController().getUser(Long.valueOf(tLRPC$Peer.user_id));
                        str = UserObject.getUserName(tLRPC$User);
                        if (tLRPC$User != null && tLRPC$User.bot) {
                            str2 = LocaleController.getString("FilterInviteBot", R.string.FilterInviteBot);
                        } else {
                            str2 = LocaleController.getString("FilterInviteUser", R.string.FilterInviteUser);
                        }
                    } else {
                        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                            j = -tLRPC$Peer.chat_id;
                            chat = FolderBottomSheet.this.getBaseFragment().getMessagesController().getChat(Long.valueOf(tLRPC$Peer.chat_id));
                        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                            j = -tLRPC$Peer.channel_id;
                            chat = FolderBottomSheet.this.getBaseFragment().getMessagesController().getChat(Long.valueOf(tLRPC$Peer.channel_id));
                        }
                        str2 = null;
                        tLRPC$User = chat;
                        str = null;
                    }
                    if (tLRPC$User instanceof TLRPC$Chat) {
                        TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLRPC$User;
                        String str3 = tLRPC$Chat.title;
                        if (tLRPC$Chat.participants_count != 0) {
                            if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                                string = LocaleController.formatPluralStringComma("Subscribers", tLRPC$Chat.participants_count);
                            } else {
                                string = LocaleController.formatPluralStringComma("Members", tLRPC$Chat.participants_count);
                            }
                        } else if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                            string = LocaleController.getString("ChannelPublic", R.string.ChannelPublic);
                        } else {
                            string = LocaleController.getString("MegaPublic", R.string.MegaPublic);
                        }
                        str2 = string;
                        str = str3;
                    }
                    groupCreateUserCell.setTag(Long.valueOf(j));
                    groupCreateUserCell.getCheckBox().getCheckBoxBase().setAlpha(!FolderBottomSheet.this.alreadyJoined.contains(Long.valueOf(j)) ? 0.5f : 1.0f);
                    groupCreateUserCell.setChecked(FolderBottomSheet.this.selectedPeers.contains(Long.valueOf(j)), false);
                    groupCreateUserCell.setObject(tLRPC$User, str, str2);
                }
                str = null;
                str2 = null;
                if (tLRPC$User instanceof TLRPC$Chat) {
                }
                groupCreateUserCell.setTag(Long.valueOf(j));
                groupCreateUserCell.getCheckBox().getCheckBoxBase().setAlpha(!FolderBottomSheet.this.alreadyJoined.contains(Long.valueOf(j)) ? 0.5f : 1.0f);
                groupCreateUserCell.setChecked(FolderBottomSheet.this.selectedPeers.contains(Long.valueOf(j)), false);
                groupCreateUserCell.setObject(tLRPC$User, str, str2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                if (i == FolderBottomSheet.this.titleRow) {
                    return 0;
                }
                if (i == FolderBottomSheet.this.sectionRow || i == FolderBottomSheet.this.usersSectionRow || i == FolderBottomSheet.this.alreadySectionRow) {
                    return 1;
                }
                return (i == FolderBottomSheet.this.headerRow || i == FolderBottomSheet.this.alreadyHeaderRow) ? 3 : 2;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return FolderBottomSheet.this.rowsCount;
            }
        };
    }

    /* loaded from: classes3.dex */
    public static class HeaderCell extends FrameLayout {
        public AnimatedTextView actionTextView;
        public AnimatedTextView textView;

        public HeaderCell(Context context) {
            super(context);
            AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, false);
            this.textView = animatedTextView;
            animatedTextView.setTextSize(AndroidUtilities.dp(15.0f));
            this.textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.textView.setGravity(LocaleController.isRTL ? 5 : 3);
            addView(this.textView, LayoutHelper.createFrame(-1, 20.0f, (LocaleController.isRTL ? 5 : 3) | 80, 21.0f, 15.0f, 21.0f, 2.0f));
            AnimatedTextView animatedTextView2 = new AnimatedTextView(context, true, true, true);
            this.actionTextView = animatedTextView2;
            animatedTextView2.setAnimationProperties(0.45f, 0L, 250L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.actionTextView.setTextSize(AndroidUtilities.dp(15.0f));
            this.actionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.actionTextView.setGravity(LocaleController.isRTL ? 3 : 5);
            addView(this.actionTextView, LayoutHelper.createFrame(-2, 20.0f, (LocaleController.isRTL ? 3 : 5) | 80, 21.0f, 15.0f, 21.0f, 2.0f));
            ViewCompat.setAccessibilityHeading(this, true);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
        }

        public void setText(CharSequence charSequence, boolean z) {
            if (z) {
                this.textView.cancelAnimation();
            }
            this.textView.setText(charSequence, z && !LocaleController.isRTL);
        }

        public void setAction(CharSequence charSequence, final Runnable runnable) {
            this.actionTextView.setText(charSequence, !LocaleController.isRTL);
            this.actionTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.FolderBottomSheet$HeaderCell$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    FolderBottomSheet.HeaderCell.lambda$setAction$0(runnable, view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$setAction$0(Runnable runnable, View view) {
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("android.widget.TextView");
            accessibilityNodeInfo.setText(this.textView.getText());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TitleCell extends FrameLayout {
        private boolean already;
        private FoldersPreview preview;
        private TextView subtitleTextView;
        private String title;
        private TextView titleTextView;

        public TitleCell(Context context, boolean z, String str) {
            super(context);
            this.already = z;
            this.title = str;
            FoldersPreview foldersPreview = new FoldersPreview(this, context, null, LocaleController.getString("FolderLinkPreviewLeft"), str, LocaleController.getString("FolderLinkPreviewRight"), null);
            this.preview = foldersPreview;
            addView(foldersPreview, LayoutHelper.createFrame(-1, 44.0f, 55, 0.0f, 17.33f, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.titleTextView.setTextSize(1, 20.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleTextView.setText(FolderBottomSheet.this.getTitle());
            this.titleTextView.setGravity(17);
            addView(this.titleTextView, LayoutHelper.createFrame(-1, -2.0f, 48, 32.0f, 78.3f, 32.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.subtitleTextView = textView2;
            textView2.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.subtitleTextView.setTextSize(1, 14.0f);
            this.subtitleTextView.setLines(2);
            this.subtitleTextView.setGravity(17);
            this.subtitleTextView.setLineSpacing(0.0f, 1.15f);
            addView(this.subtitleTextView, LayoutHelper.createFrame(-1, -2.0f, 48, 32.0f, 113.0f, 32.0f, 0.0f));
            setSelectedCount(0, false);
        }

        public void setSelectedCount(int i, boolean z) {
            if (FolderBottomSheet.this.deleting) {
                this.subtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FolderLinkSubtitleRemove", R.string.FolderLinkSubtitleRemove, this.title)));
            } else if (this.already) {
                this.preview.setCount(FolderBottomSheet.this.peers != null ? FolderBottomSheet.this.peers.size() : 0, false);
                if (FolderBottomSheet.this.peers == null || FolderBottomSheet.this.peers.isEmpty()) {
                    this.subtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FolderLinkSubtitleAlready", R.string.FolderLinkSubtitleAlready, this.title)));
                } else {
                    this.subtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatPluralString("FolderLinkSubtitleChats", FolderBottomSheet.this.peers != null ? FolderBottomSheet.this.peers.size() : 0, this.title)));
                }
            } else if (FolderBottomSheet.this.peers == null || FolderBottomSheet.this.peers.isEmpty()) {
                this.subtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FolderLinkSubtitleAlready", R.string.FolderLinkSubtitleAlready, this.title)));
            } else {
                this.subtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FolderLinkSubtitle", R.string.FolderLinkSubtitle, this.title)));
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(172.0f), 1073741824));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class FoldersPreview extends View {
            AnimatedTextView.AnimatedTextDrawable countText;
            StaticLayout leftFolder;
            StaticLayout leftFolder2;
            float leftFolder2Width;
            float leftFolderWidth;
            LinearGradient leftGradient;
            Matrix leftMatrix;
            Paint leftPaint;
            StaticLayout middleFolder;
            float middleFolderWidth;
            TextPaint paint;
            Path path;
            float[] radii;
            StaticLayout rightFolder;
            StaticLayout rightFolder2;
            float rightFolder2Width;
            float rightFolderWidth;
            LinearGradient rightGradient;
            Matrix rightMatrix;
            Paint rightPaint;
            Paint selectedPaint;
            TextPaint selectedTextPaint;

            public FoldersPreview(TitleCell titleCell, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5) {
                super(context);
                this.paint = new TextPaint(1);
                this.selectedTextPaint = new TextPaint(1);
                this.selectedPaint = new Paint(1);
                this.path = new Path();
                this.radii = new float[8];
                this.leftPaint = new Paint(1);
                this.rightPaint = new Paint(1);
                this.leftMatrix = new Matrix();
                this.rightMatrix = new Matrix();
                this.paint.setColor(Theme.multAlpha(Theme.getColor("profile_tabText"), 0.8f));
                this.paint.setTextSize(AndroidUtilities.dp(15.33f));
                this.paint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.selectedTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlueText2"));
                this.selectedTextPaint.setTextSize(AndroidUtilities.dp(17.0f));
                this.selectedTextPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.selectedPaint.setColor(Theme.getColor("featuredStickers_unread"));
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
                this.countText = animatedTextDrawable;
                animatedTextDrawable.setAnimationProperties(0.3f, 0L, 250L, CubicBezierInterpolator.EASE_OUT_QUINT);
                this.countText.setCallback(this);
                this.countText.setTextSize(AndroidUtilities.dp(11.66f));
                this.countText.setTextColor(Theme.getColor("windowBackgroundWhite"));
                this.countText.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.countText.setGravity(1);
                if (charSequence != null) {
                    StaticLayout makeLayout = makeLayout(charSequence, false);
                    this.leftFolder2 = makeLayout;
                    this.leftFolder2Width = makeLayout.getLineWidth(0);
                }
                if (charSequence2 != null) {
                    StaticLayout makeLayout2 = makeLayout(charSequence2, false);
                    this.leftFolder = makeLayout2;
                    this.leftFolderWidth = makeLayout2.getLineWidth(0);
                }
                StaticLayout makeLayout3 = makeLayout(charSequence3, true);
                this.middleFolder = makeLayout3;
                this.middleFolderWidth = makeLayout3.getLineWidth(0);
                if (charSequence4 != null) {
                    StaticLayout makeLayout4 = makeLayout(charSequence4, false);
                    this.rightFolder = makeLayout4;
                    this.rightFolderWidth = makeLayout4.getLineWidth(0);
                }
                if (charSequence5 != null) {
                    StaticLayout makeLayout5 = makeLayout(charSequence5, false);
                    this.rightFolder2 = makeLayout5;
                    this.rightFolder2Width = makeLayout5.getLineWidth(0);
                }
                float[] fArr = this.radii;
                float dp = AndroidUtilities.dp(3.0f);
                fArr[3] = dp;
                fArr[2] = dp;
                fArr[1] = dp;
                fArr[0] = dp;
                float[] fArr2 = this.radii;
                float dp2 = AndroidUtilities.dp(1.0f);
                fArr2[7] = dp2;
                fArr2[6] = dp2;
                fArr2[5] = dp2;
                fArr2[4] = dp2;
                LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(80.0f), 0.0f, new int[]{-1, 16777215}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                this.leftGradient = linearGradient;
                this.leftPaint.setShader(linearGradient);
                this.leftPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                LinearGradient linearGradient2 = new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(80.0f), 0.0f, new int[]{16777215, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                this.rightGradient = linearGradient2;
                this.rightPaint.setShader(linearGradient2);
                this.rightPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            }

            private StaticLayout makeLayout(CharSequence charSequence, boolean z) {
                if (charSequence == null || "ALL_CHATS".equals(charSequence.toString())) {
                    charSequence = LocaleController.getString("FilterAllChats", R.string.FilterAllChats);
                }
                return new StaticLayout(charSequence, z ? this.selectedTextPaint : this.paint, AndroidUtilities.displaySize.x, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                float f;
                super.onDraw(canvas);
                canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
                float measuredWidth = getMeasuredWidth() / 2.0f;
                float measuredHeight = getMeasuredHeight() / 2.0f;
                canvas.save();
                float dp = this.middleFolderWidth + (isCountEmpty() ? 0.0f : AndroidUtilities.dp(15.32f) + this.countText.getCurrentWidth());
                float f2 = dp / 2.0f;
                float f3 = measuredWidth - f2;
                canvas.translate(f3, measuredHeight - (this.middleFolder.getHeight() / 2.0f));
                this.middleFolder.draw(canvas);
                canvas.restore();
                if (!isCountEmpty()) {
                    android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                    rect.set((int) (this.middleFolderWidth + f3 + AndroidUtilities.dp(4.66f)), (int) (measuredHeight - AndroidUtilities.dp(9.0f)), (int) (this.middleFolderWidth + f3 + AndroidUtilities.dp(15.32f) + this.countText.getCurrentWidth()), (int) (AndroidUtilities.dp(9.0f) + measuredHeight));
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(rect);
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(9.0f), AndroidUtilities.dp(9.0f), this.selectedPaint);
                    rect.offset(-AndroidUtilities.dp(0.33f), -AndroidUtilities.dp(0.66f));
                    this.countText.setBounds(rect);
                    this.countText.draw(canvas);
                }
                float dp2 = AndroidUtilities.dp(30.0f);
                float f4 = this.leftFolderWidth;
                float f5 = (f3 - dp2) - f4;
                if (this.leftFolder2 == null || f4 >= AndroidUtilities.dp(64.0f)) {
                    f = f5;
                } else {
                    float f6 = f5 - (this.leftFolder2Width + dp2);
                    canvas.save();
                    canvas.translate(f6, (measuredHeight - (this.leftFolder2.getHeight() / 2.0f)) + AndroidUtilities.dp(1.0f));
                    this.leftFolder2.draw(canvas);
                    canvas.restore();
                    f = f6;
                }
                if (this.leftFolder != null) {
                    canvas.save();
                    canvas.translate(f5, (measuredHeight - (this.leftFolder.getHeight() / 2.0f)) + AndroidUtilities.dp(1.0f));
                    this.leftFolder.draw(canvas);
                    canvas.restore();
                }
                float f7 = dp + f3;
                if (this.rightFolder != null) {
                    canvas.save();
                    canvas.translate(f7 + dp2, (measuredHeight - (this.rightFolder.getHeight() / 2.0f)) + AndroidUtilities.dp(1.0f));
                    this.rightFolder.draw(canvas);
                    canvas.restore();
                    f7 += this.rightFolderWidth + dp2;
                }
                if (this.rightFolder2 != null && this.rightFolderWidth < AndroidUtilities.dp(64.0f)) {
                    canvas.save();
                    canvas.translate(f7 + dp2, (measuredHeight - (this.rightFolder2.getHeight() / 2.0f)) + AndroidUtilities.dp(1.0f));
                    this.rightFolder2.draw(canvas);
                    canvas.restore();
                    f7 += dp2 + this.rightFolder2Width;
                }
                float height = measuredHeight + (this.middleFolder.getHeight() / 2.0f) + AndroidUtilities.dp(12.0f);
                canvas.drawRect(0.0f, height, getMeasuredWidth(), height + 1.0f, this.paint);
                this.path.rewind();
                RectF rectF2 = AndroidUtilities.rectTmp;
                float f8 = f2 + measuredWidth;
                rectF2.set(f3 - AndroidUtilities.dp(4.0f), height - AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f) + f8, height);
                this.path.addRoundRect(rectF2, this.radii, Path.Direction.CW);
                canvas.drawPath(this.path, this.selectedPaint);
                canvas.save();
                float max = Math.max(AndroidUtilities.dp(8.0f), f);
                this.leftMatrix.reset();
                this.leftMatrix.postTranslate(Math.min(f3, max + AndroidUtilities.dp(8.0f)), 0.0f);
                this.leftGradient.setLocalMatrix(this.leftMatrix);
                float min = Math.min(getMeasuredWidth() - AndroidUtilities.dp(8.0f), f7);
                this.rightMatrix.reset();
                this.rightMatrix.postTranslate(Math.max(f8, min - AndroidUtilities.dp(88.0f)), 0.0f);
                this.rightGradient.setLocalMatrix(this.rightMatrix);
                canvas.drawRect(0.0f, 0.0f, measuredWidth, getMeasuredHeight(), this.leftPaint);
                canvas.drawRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.rightPaint);
                canvas.restore();
                canvas.restore();
            }

            @Override // android.view.View
            protected boolean verifyDrawable(Drawable drawable) {
                return drawable == this.countText || super.verifyDrawable(drawable);
            }

            private boolean isCountEmpty() {
                return this.countText.getText() == null || this.countText.getText().length() == 0;
            }

            public void setCount(int i, boolean z) {
                String str;
                if (z) {
                    this.countText.cancelAnimation();
                }
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.countText;
                if (i > 0) {
                    str = "+" + i;
                } else {
                    str = "";
                }
                animatedTextDrawable.setText(str, z);
                invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeaderCell(boolean z) {
        HeaderCell headerCell = this.headerCell;
        if (headerCell == null) {
            return;
        }
        if (this.deleting) {
            headerCell.setText(LocaleController.formatPluralString("FolderLinkHeaderChatsQuit", this.peers.size(), new Object[0]), false);
        } else {
            headerCell.setText(LocaleController.formatPluralString("FolderLinkHeaderChatsJoin", this.peers.size(), new Object[0]), false);
        }
        ArrayList<TLRPC$Peer> arrayList = this.peers;
        if (arrayList != null && arrayList.size() - this.alreadyJoined.size() > 1) {
            final boolean z2 = this.selectedPeers.size() >= this.peers.size() - this.alreadyJoined.size();
            this.headerCell.setAction(LocaleController.getString(z2 ? R.string.DeselectAll : R.string.SelectAll), new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    FolderBottomSheet.this.lambda$updateHeaderCell$16(z2);
                }
            });
            return;
        }
        this.headerCell.setAction("", null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateHeaderCell$16(boolean z) {
        deselectAll(this.headerCell, z);
    }

    private void announceSelection(boolean z) {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(LocaleController.formatPluralString("FilterInviteHeaderChats", this.selectedPeers.size(), new Object[0]));
        if (!z || this.headerCell == null) {
            str = "";
        } else {
            str = ", " + ((Object) this.headerCell.actionTextView.getText());
        }
        sb.append(str);
        AndroidUtilities.makeAccessibilityAnnouncement(sb.toString());
    }

    private void deselectAll(final HeaderCell headerCell, final boolean z) {
        this.selectedPeers.clear();
        this.selectedPeers.addAll(this.alreadyJoined);
        if (!z) {
            for (int i = 0; i < this.peers.size(); i++) {
                long peerDialogId = DialogObject.getPeerDialogId(this.peers.get(i));
                if (!this.selectedPeers.contains(Long.valueOf(peerDialogId))) {
                    this.selectedPeers.add(Long.valueOf(peerDialogId));
                }
            }
        }
        updateCount(true);
        headerCell.setAction(LocaleController.getString(z ? R.string.SelectAll : R.string.DeselectAll), new Runnable() { // from class: org.telegram.ui.Components.FolderBottomSheet$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                FolderBottomSheet.this.lambda$deselectAll$17(headerCell, z);
            }
        });
        announceSelection(true);
        for (int i2 = 0; i2 < this.recyclerListView.getChildCount(); i2++) {
            View childAt = this.recyclerListView.getChildAt(i2);
            if (childAt instanceof GroupCreateUserCell) {
                Object tag = childAt.getTag();
                if (tag instanceof Long) {
                    ((GroupCreateUserCell) childAt).setChecked(this.selectedPeers.contains(Long.valueOf(((Long) tag).longValue())), true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deselectAll$17(HeaderCell headerCell, boolean z) {
        deselectAll(headerCell, !z);
    }
}