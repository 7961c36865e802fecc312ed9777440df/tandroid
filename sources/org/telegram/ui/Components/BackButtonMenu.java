package org.telegram.ui.Components;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.lang.reflect.GenericDeclaration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public class BackButtonMenu {

    /* loaded from: classes3.dex */
    public static class PulledDialog<T> {
        Class<T> activity;
        TLRPC$Chat chat;
        long dialogId;
        int filterId;
        int folderId;
        int stackIndex;
        TLRPC$User user;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v4, types: [android.graphics.drawable.BitmapDrawable] */
    /* JADX WARN: Type inference failed for: r15v0, types: [android.widget.FrameLayout, android.view.View] */
    /* JADX WARN: Type inference failed for: r2v1, types: [org.telegram.ui.ActionBar.ActionBarPopupWindow$ActionBarPopupWindowLayout, android.widget.FrameLayout, android.view.View] */
    public static ActionBarPopupWindow show(final BaseFragment baseFragment, View view, long j, Theme.ResourcesProvider resourcesProvider) {
        View view2;
        Drawable drawable;
        View view3;
        android.graphics.Rect rect;
        String userName;
        ?? r14;
        if (baseFragment == null) {
            return null;
        }
        final INavigationLayout parentLayout = baseFragment.getParentLayout();
        Activity parentActivity = baseFragment.getParentActivity();
        View fragmentView = baseFragment.getFragmentView();
        if (parentLayout == null || parentActivity == null || fragmentView == null) {
            return null;
        }
        ArrayList<PulledDialog> stackedHistoryDialogs = getStackedHistoryDialogs(baseFragment, j);
        if (stackedHistoryDialogs.size() <= 0) {
            return null;
        }
        ?? actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(parentActivity, resourcesProvider);
        android.graphics.Rect rect2 = new android.graphics.Rect();
        baseFragment.getParentActivity().getResources().getDrawable(R.drawable.popup_fixed_alert).mutate().getPadding(rect2);
        actionBarPopupWindowLayout.setBackgroundColor(Theme.getColor("actionBarDefaultSubmenuBackground", resourcesProvider));
        final AtomicReference atomicReference = new AtomicReference();
        int i = 0;
        while (i < stackedHistoryDialogs.size()) {
            final PulledDialog pulledDialog = stackedHistoryDialogs.get(i);
            TLRPC$Chat tLRPC$Chat = pulledDialog.chat;
            TLRPC$User tLRPC$User = pulledDialog.user;
            ?? frameLayout = new FrameLayout(parentActivity);
            frameLayout.setMinimumWidth(AndroidUtilities.dp(200.0f));
            BackupImageView backupImageView = new BackupImageView(parentActivity);
            backupImageView.setRoundRadius(AndroidUtilities.dp(32.0f));
            frameLayout.addView(backupImageView, LayoutHelper.createFrameRelatively(32.0f, 32.0f, 8388627, 13.0f, 0.0f, 0.0f, 0.0f));
            TextView textView = new TextView(parentActivity);
            Activity activity = parentActivity;
            textView.setLines(1);
            ArrayList<PulledDialog> arrayList = stackedHistoryDialogs;
            textView.setTextSize(1, 16.0f);
            textView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem", resourcesProvider));
            textView.setEllipsize(TextUtils.TruncateAt.END);
            frameLayout.addView(textView, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 8388627, 59.0f, 0.0f, 12.0f, 0.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setSmallSize(true);
            if (tLRPC$Chat != null) {
                avatarDrawable.setInfo(tLRPC$Chat);
                TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat.photo;
                if (tLRPC$ChatPhoto != null && (r14 = tLRPC$ChatPhoto.strippedBitmap) != 0) {
                    avatarDrawable = r14;
                }
                backupImageView.setImage(ImageLocation.getForChat(tLRPC$Chat, 1), "50_50", avatarDrawable, tLRPC$Chat);
                textView.setText(tLRPC$Chat.title);
            } else if (tLRPC$User != null) {
                TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
                if (tLRPC$UserProfilePhoto == null || (drawable = tLRPC$UserProfilePhoto.strippedBitmap) == null) {
                    drawable = avatarDrawable;
                }
                view3 = fragmentView;
                rect = rect2;
                if (pulledDialog.activity == ChatActivity.class && UserObject.isUserSelf(tLRPC$User)) {
                    userName = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                    avatarDrawable.setAvatarType(1);
                    backupImageView.setImageDrawable(avatarDrawable);
                } else if (UserObject.isReplyUser(tLRPC$User)) {
                    userName = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                    avatarDrawable.setAvatarType(12);
                    backupImageView.setImageDrawable(avatarDrawable);
                } else if (UserObject.isDeleted(tLRPC$User)) {
                    userName = LocaleController.getString("HiddenName", R.string.HiddenName);
                    avatarDrawable.setInfo(tLRPC$User);
                    backupImageView.setImage(ImageLocation.getForUser(tLRPC$User, 1), "50_50", avatarDrawable, tLRPC$User);
                } else {
                    userName = UserObject.getUserName(tLRPC$User);
                    avatarDrawable.setInfo(tLRPC$User);
                    backupImageView.setImage(ImageLocation.getForUser(tLRPC$User, 1), "50_50", drawable, tLRPC$User);
                }
                textView.setText(userName);
                frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor("listSelectorSDK21", resourcesProvider), false));
                frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view4) {
                        BackButtonMenu.lambda$show$0(atomicReference, pulledDialog, parentLayout, baseFragment, view4);
                    }
                });
                actionBarPopupWindowLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 48));
                i++;
                parentActivity = activity;
                stackedHistoryDialogs = arrayList;
                fragmentView = view3;
                rect2 = rect;
            }
            view3 = fragmentView;
            rect = rect2;
            frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor("listSelectorSDK21", resourcesProvider), false));
            frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view4) {
                    BackButtonMenu.lambda$show$0(atomicReference, pulledDialog, parentLayout, baseFragment, view4);
                }
            });
            actionBarPopupWindowLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 48));
            i++;
            parentActivity = activity;
            stackedHistoryDialogs = arrayList;
            fragmentView = view3;
            rect2 = rect;
        }
        View view4 = fragmentView;
        android.graphics.Rect rect3 = rect2;
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        atomicReference.set(actionBarPopupWindow);
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindow.setDismissAnimationDuration(220);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.setSoftInputMode(0);
        actionBarPopupWindow.getContentView().setFocusableInTouchMode(true);
        actionBarPopupWindowLayout.setFitItems(true);
        int dp = AndroidUtilities.dp(8.0f) - rect3.left;
        if (AndroidUtilities.isTablet()) {
            int[] iArr = new int[2];
            view2 = view4;
            view2.getLocationInWindow(iArr);
            dp += iArr[0];
        } else {
            view2 = view4;
        }
        actionBarPopupWindow.showAtLocation(view2, 51, dp, (view.getBottom() - rect3.top) - AndroidUtilities.dp(8.0f));
        return actionBarPopupWindow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$show$0(AtomicReference atomicReference, PulledDialog pulledDialog, INavigationLayout iNavigationLayout, BaseFragment baseFragment, View view) {
        int i;
        Long l = null;
        if (atomicReference.get() != null) {
            ((ActionBarPopupWindow) atomicReference.getAndSet(null)).dismiss();
        }
        if (pulledDialog.stackIndex >= 0) {
            if (iNavigationLayout != null && iNavigationLayout.getFragmentStack() != null && pulledDialog.stackIndex < iNavigationLayout.getFragmentStack().size()) {
                BaseFragment baseFragment2 = iNavigationLayout.getFragmentStack().get(pulledDialog.stackIndex);
                if (baseFragment2 instanceof ChatActivity) {
                    l = Long.valueOf(((ChatActivity) baseFragment2).getDialogId());
                } else if (baseFragment2 instanceof ProfileActivity) {
                    l = Long.valueOf(((ProfileActivity) baseFragment2).getDialogId());
                }
            }
            if (l != null && l.longValue() != pulledDialog.dialogId) {
                for (int size = iNavigationLayout.getFragmentStack().size() - 2; size > pulledDialog.stackIndex; size--) {
                    iNavigationLayout.removeFragmentFromStack(size);
                }
            } else if (iNavigationLayout != null && iNavigationLayout.getFragmentStack() != null) {
                int size2 = iNavigationLayout.getFragmentStack().size() - 2;
                while (true) {
                    i = pulledDialog.stackIndex;
                    if (size2 <= i) {
                        break;
                    }
                    if (size2 >= 0 && size2 < iNavigationLayout.getFragmentStack().size()) {
                        iNavigationLayout.removeFragmentFromStack(size2);
                    }
                    size2--;
                }
                if (i < iNavigationLayout.getFragmentStack().size()) {
                    iNavigationLayout.bringToFront(pulledDialog.stackIndex);
                    iNavigationLayout.closeLastFragment(true);
                    return;
                }
            }
        }
        goToPulledDialog(baseFragment, pulledDialog);
    }

    public static void goToPulledDialog(BaseFragment baseFragment, PulledDialog pulledDialog) {
        if (pulledDialog == null) {
            return;
        }
        GenericDeclaration genericDeclaration = pulledDialog.activity;
        if (genericDeclaration == ChatActivity.class) {
            Bundle bundle = new Bundle();
            TLRPC$Chat tLRPC$Chat = pulledDialog.chat;
            if (tLRPC$Chat != null) {
                bundle.putLong("chat_id", tLRPC$Chat.id);
            } else {
                TLRPC$User tLRPC$User = pulledDialog.user;
                if (tLRPC$User != null) {
                    bundle.putLong("user_id", tLRPC$User.id);
                }
            }
            bundle.putInt("dialog_folder_id", pulledDialog.folderId);
            bundle.putInt("dialog_filter_id", pulledDialog.filterId);
            baseFragment.presentFragment(new ChatActivity(bundle), true);
        } else if (genericDeclaration != ProfileActivity.class) {
        } else {
            Bundle bundle2 = new Bundle();
            bundle2.putLong("dialog_id", pulledDialog.dialogId);
            baseFragment.presentFragment(new ProfileActivity(bundle2), true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00bf A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x009d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ArrayList<PulledDialog> getStackedHistoryDialogs(BaseFragment baseFragment, long j) {
        INavigationLayout parentLayout;
        boolean z;
        long dialogId;
        int i;
        int i2;
        TLRPC$User tLRPC$User;
        Class<T> cls;
        TLRPC$Chat tLRPC$Chat;
        int i3;
        int i4;
        boolean z2;
        ArrayList<PulledDialog> arrayList = new ArrayList<>();
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return arrayList;
        }
        List<BaseFragment> fragmentStack = parentLayout.getFragmentStack();
        List<PulledDialog> pulledDialogs = parentLayout.getPulledDialogs();
        if (fragmentStack != null) {
            int size = fragmentStack.size();
            int i5 = 0;
            while (i5 < size) {
                BaseFragment baseFragment2 = fragmentStack.get(i5);
                TLRPC$User tLRPC$User2 = null;
                if (baseFragment2 instanceof ChatActivity) {
                    Class<ChatActivity> cls2 = ChatActivity.class;
                    ChatActivity chatActivity = (ChatActivity) baseFragment2;
                    if (chatActivity.getChatMode() == 0 && !chatActivity.isReport()) {
                        tLRPC$Chat = chatActivity.getCurrentChat();
                        tLRPC$User = chatActivity.getCurrentUser();
                        dialogId = chatActivity.getDialogId();
                        i2 = chatActivity.getDialogFolderId();
                        i = chatActivity.getDialogFilterId();
                        cls = cls2;
                        if (dialogId != j && (j != 0 || !UserObject.isUserSelf(tLRPC$User))) {
                            i4 = 0;
                            while (true) {
                                if (i4 < arrayList.size()) {
                                    i3 = size;
                                    z2 = false;
                                    break;
                                }
                                i3 = size;
                                if (arrayList.get(i4).dialogId == dialogId) {
                                    z2 = true;
                                    break;
                                }
                                i4++;
                                size = i3;
                            }
                            if (z2) {
                                PulledDialog pulledDialog = new PulledDialog();
                                pulledDialog.activity = cls;
                                pulledDialog.stackIndex = i5;
                                pulledDialog.chat = tLRPC$Chat;
                                pulledDialog.user = tLRPC$User;
                                pulledDialog.dialogId = dialogId;
                                pulledDialog.folderId = i2;
                                pulledDialog.filterId = i;
                                if (tLRPC$Chat != null || tLRPC$User != null) {
                                    arrayList.add(pulledDialog);
                                }
                            }
                        }
                    }
                    i3 = size;
                } else {
                    if (baseFragment2 instanceof ProfileActivity) {
                        ProfileActivity profileActivity = (ProfileActivity) baseFragment2;
                        TLRPC$Chat currentChat = profileActivity.getCurrentChat();
                        try {
                            tLRPC$User2 = profileActivity.getUserInfo().user;
                        } catch (Exception unused) {
                        }
                        dialogId = profileActivity.getDialogId();
                        i = 0;
                        i2 = 0;
                        tLRPC$User = tLRPC$User2;
                        cls = ProfileActivity.class;
                        tLRPC$Chat = currentChat;
                        if (dialogId != j) {
                            i4 = 0;
                            while (true) {
                                if (i4 < arrayList.size()) {
                                }
                                i4++;
                                size = i3;
                            }
                            if (z2) {
                            }
                        }
                    }
                    i3 = size;
                }
                i5++;
                size = i3;
            }
        }
        if (pulledDialogs != null) {
            for (int size2 = pulledDialogs.size() - 1; size2 >= 0; size2--) {
                PulledDialog pulledDialog2 = pulledDialogs.get(size2);
                if (pulledDialog2.dialogId != j) {
                    int i6 = 0;
                    while (true) {
                        if (i6 >= arrayList.size()) {
                            z = false;
                            break;
                        } else if (arrayList.get(i6).dialogId == pulledDialog2.dialogId) {
                            z = true;
                            break;
                        } else {
                            i6++;
                        }
                    }
                    if (!z) {
                        arrayList.add(pulledDialog2);
                    }
                }
            }
        }
        Collections.sort(arrayList, BackButtonMenu$$ExternalSyntheticLambda1.INSTANCE);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getStackedHistoryDialogs$1(PulledDialog pulledDialog, PulledDialog pulledDialog2) {
        return pulledDialog2.stackIndex - pulledDialog.stackIndex;
    }

    public static void addToPulledDialogs(BaseFragment baseFragment, int i, TLRPC$Chat tLRPC$Chat, TLRPC$User tLRPC$User, long j, int i2, int i3) {
        INavigationLayout parentLayout;
        if ((tLRPC$Chat == null && tLRPC$User == null) || baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return;
        }
        if (parentLayout.getPulledDialogs() == null) {
            parentLayout.setPulledDialogs(new ArrayList());
        }
        boolean z = false;
        Iterator<PulledDialog> it = parentLayout.getPulledDialogs().iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().dialogId == j) {
                    z = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (z) {
            return;
        }
        PulledDialog pulledDialog = new PulledDialog();
        pulledDialog.activity = ChatActivity.class;
        pulledDialog.stackIndex = i;
        pulledDialog.dialogId = j;
        pulledDialog.filterId = i3;
        pulledDialog.folderId = i2;
        pulledDialog.chat = tLRPC$Chat;
        pulledDialog.user = tLRPC$User;
        parentLayout.getPulledDialogs().add(pulledDialog);
    }

    public static void clearPulledDialogs(BaseFragment baseFragment, int i) {
        INavigationLayout parentLayout;
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null || parentLayout.getPulledDialogs() == null) {
            return;
        }
        int i2 = 0;
        while (i2 < parentLayout.getPulledDialogs().size()) {
            if (parentLayout.getPulledDialogs().get(i2).stackIndex > i) {
                parentLayout.getPulledDialogs().remove(i2);
                i2--;
            }
            i2++;
        }
    }
}