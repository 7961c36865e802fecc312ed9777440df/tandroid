package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes3.dex */
public class UserCell2 extends FrameLayout {
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private CheckBox checkBox;
    private CheckBoxSquare checkBoxBig;
    private int currentAccount;
    private int currentDrawable;
    private int currentId;
    private CharSequence currentName;
    private TLObject currentObject;
    private CharSequence currentStatus;
    private ImageView imageView;
    private TLRPC$FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private SimpleTextView nameTextView;
    private Theme.ResourcesProvider resourcesProvider;
    private int statusColor;
    private int statusOnlineColor;
    private SimpleTextView statusTextView;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public UserCell2(Context context, int i, int i2) {
        this(context, i, i2, null);
    }

    public UserCell2(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i3;
        float f;
        this.currentAccount = UserConfig.selectedAccount;
        this.resourcesProvider = resourcesProvider;
        this.statusColor = Theme.getColor("windowBackgroundWhiteGrayText", resourcesProvider);
        this.statusOnlineColor = Theme.getColor("windowBackgroundWhiteBlueText", resourcesProvider);
        this.avatarDrawable = new AvatarDrawable();
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(24.0f));
        View view = this.avatarImageView;
        boolean z = LocaleController.isRTL;
        addView(view, LayoutHelper.createFrame(48, 48.0f, (z ? 5 : 3) | 48, z ? 0.0f : i + 7, 11.0f, z ? i + 7 : 0.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.nameTextView = simpleTextView;
        simpleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText", resourcesProvider));
        this.nameTextView.setTextSize(17);
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        View view2 = this.nameTextView;
        boolean z2 = LocaleController.isRTL;
        int i4 = (z2 ? 5 : 3) | 48;
        int i5 = 18;
        if (z2) {
            i3 = (i2 == 2 ? 18 : 0) + 28;
        } else {
            i3 = i + 68;
        }
        float f2 = i3;
        if (z2) {
            f = i + 68;
        } else {
            f = (i2 != 2 ? 0 : i5) + 28;
        }
        addView(view2, LayoutHelper.createFrame(-1, 20.0f, i4, f2, 14.5f, f, 0.0f));
        SimpleTextView simpleTextView2 = new SimpleTextView(context);
        this.statusTextView = simpleTextView2;
        simpleTextView2.setTextSize(14);
        this.statusTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        View view3 = this.statusTextView;
        boolean z3 = LocaleController.isRTL;
        addView(view3, LayoutHelper.createFrame(-1, 20.0f, (z3 ? 5 : 3) | 48, z3 ? 28.0f : i + 68, 37.5f, z3 ? i + 68 : 28.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.imageView = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon", resourcesProvider), PorterDuff.Mode.MULTIPLY));
        this.imageView.setVisibility(8);
        View view4 = this.imageView;
        boolean z4 = LocaleController.isRTL;
        addView(view4, LayoutHelper.createFrame(-2, -2.0f, (z4 ? 5 : 3) | 16, z4 ? 0.0f : 16.0f, 0.0f, z4 ? 16.0f : 0.0f, 0.0f));
        if (i2 == 2) {
            CheckBoxSquare checkBoxSquare = new CheckBoxSquare(context, false, resourcesProvider);
            this.checkBoxBig = checkBoxSquare;
            boolean z5 = LocaleController.isRTL;
            addView(checkBoxSquare, LayoutHelper.createFrame(18, 18.0f, (z5 ? 3 : 5) | 16, z5 ? 19.0f : 0.0f, 0.0f, z5 ? 0.0f : 19.0f, 0.0f));
        } else if (i2 != 1) {
        } else {
            CheckBox checkBox = new CheckBox(context, 2131166113);
            this.checkBox = checkBox;
            checkBox.setVisibility(4);
            this.checkBox.setColor(Theme.getColor("checkbox", resourcesProvider), Theme.getColor("checkboxCheck", resourcesProvider));
            View view5 = this.checkBox;
            boolean z6 = LocaleController.isRTL;
            addView(view5, LayoutHelper.createFrame(22, 22.0f, (z6 ? 5 : 3) | 48, z6 ? 0.0f : i + 37, 41.0f, z6 ? i + 37 : 0.0f, 0.0f));
        }
    }

    public void setData(TLObject tLObject, CharSequence charSequence, CharSequence charSequence2, int i) {
        if (tLObject == null && charSequence == null && charSequence2 == null) {
            this.currentStatus = null;
            this.currentName = null;
            this.currentObject = null;
            this.nameTextView.setText("");
            this.statusTextView.setText("");
            this.avatarImageView.setImageDrawable(null);
            return;
        }
        this.currentStatus = charSequence2;
        this.currentName = charSequence;
        this.currentObject = tLObject;
        this.currentDrawable = i;
        update(0);
    }

    public void setNameTypeface(Typeface typeface) {
        this.nameTextView.setTypeface(typeface);
    }

    public void setCurrentId(int i) {
        this.currentId = i;
    }

    public void setCheckDisabled(boolean z) {
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            checkBoxSquare.setDisabled(z);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(70.0f), 1073741824));
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            checkBoxSquare.invalidate();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x0087, code lost:
        if (r13.equals(r12.lastName) == false) goto L58;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v14, types: [org.telegram.ui.Components.BackupImageView] */
    /* JADX WARN: Type inference failed for: r1v1, types: [org.telegram.tgnet.TLRPC$Chat, org.telegram.tgnet.TLObject] */
    /* JADX WARN: Type inference failed for: r1v34 */
    /* JADX WARN: Type inference failed for: r1v35 */
    /* JADX WARN: Type inference failed for: r3v7, types: [org.telegram.ui.Components.AvatarDrawable] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int i) {
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        String str;
        TLRPC$UserStatus tLRPC$UserStatus;
        TLRPC$FileLocation tLRPC$FileLocation2;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLObject tLObject = this.currentObject;
        if (tLObject instanceof TLRPC$User) {
            tLRPC$User = (TLRPC$User) tLObject;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
            if (tLRPC$UserProfilePhoto != null) {
                tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small;
                tLRPC$Chat = 0;
            } else {
                tLRPC$FileLocation3 = null;
                tLRPC$FileLocation = tLRPC$FileLocation3;
                tLRPC$Chat = tLRPC$FileLocation3;
            }
        } else if (tLObject instanceof TLRPC$Chat) {
            TLRPC$Chat tLRPC$Chat2 = (TLRPC$Chat) tLObject;
            TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat2.photo;
            if (tLRPC$ChatPhoto != null) {
                tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small;
                tLRPC$Chat = tLRPC$Chat2;
                tLRPC$User = null;
            } else {
                tLRPC$Chat = tLRPC$Chat2;
                tLRPC$User = null;
                tLRPC$FileLocation = null;
            }
        } else {
            tLRPC$User = null;
            tLRPC$FileLocation3 = null;
            tLRPC$FileLocation = tLRPC$FileLocation3;
            tLRPC$Chat = tLRPC$FileLocation3;
        }
        int i2 = 0;
        if (i != 0) {
            boolean z = true;
            boolean z2 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((tLRPC$FileLocation2 = this.lastAvatar) != null && tLRPC$FileLocation == null) || ((tLRPC$FileLocation2 == null && tLRPC$FileLocation != null) || !(tLRPC$FileLocation2 == null || tLRPC$FileLocation == null || (tLRPC$FileLocation2.volume_id == tLRPC$FileLocation.volume_id && tLRPC$FileLocation2.local_id == tLRPC$FileLocation.local_id))));
            if (tLRPC$User != null && !z2 && (MessagesController.UPDATE_MASK_STATUS & i) != 0) {
                TLRPC$UserStatus tLRPC$UserStatus2 = tLRPC$User.status;
                if ((tLRPC$UserStatus2 != null ? tLRPC$UserStatus2.expires : 0) != this.lastStatus) {
                    z2 = true;
                }
            }
            if (z2 || this.currentName != null || this.lastName == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                str = null;
            } else if (tLRPC$User != null) {
                str = UserObject.getUserName(tLRPC$User);
            } else {
                str = tLRPC$Chat.title;
            }
            z = z2;
            if (!z) {
                return;
            }
        } else {
            str = null;
        }
        this.lastAvatar = tLRPC$FileLocation;
        if (tLRPC$User != null) {
            this.avatarDrawable.setInfo(tLRPC$User);
            TLRPC$UserStatus tLRPC$UserStatus3 = tLRPC$User.status;
            if (tLRPC$UserStatus3 != null) {
                this.lastStatus = tLRPC$UserStatus3.expires;
            } else {
                this.lastStatus = 0;
            }
        } else if (tLRPC$Chat != 0) {
            this.avatarDrawable.setInfo(tLRPC$Chat);
        } else {
            CharSequence charSequence = this.currentName;
            if (charSequence != null) {
                this.avatarDrawable.setInfo(this.currentId, charSequence.toString(), null);
            } else {
                this.avatarDrawable.setInfo(this.currentId, "#", null);
            }
        }
        CharSequence charSequence2 = this.currentName;
        if (charSequence2 != null) {
            this.lastName = null;
            this.nameTextView.setText(charSequence2);
        } else {
            if (tLRPC$User != null) {
                if (str == null) {
                    str = UserObject.getUserName(tLRPC$User);
                }
                this.lastName = str;
            } else {
                if (str == null) {
                    str = tLRPC$Chat.title;
                }
                this.lastName = str;
            }
            this.nameTextView.setText(this.lastName);
        }
        if (this.currentStatus != null) {
            this.statusTextView.setTextColor(this.statusColor);
            this.statusTextView.setText(this.currentStatus);
            BackupImageView backupImageView = this.avatarImageView;
            if (backupImageView != null) {
                backupImageView.setForUserOrChat(tLRPC$User, this.avatarDrawable);
            }
        } else if (tLRPC$User != null) {
            if (tLRPC$User.bot) {
                this.statusTextView.setTextColor(this.statusColor);
                if (tLRPC$User.bot_chat_history) {
                    this.statusTextView.setText(LocaleController.getString("BotStatusRead", 2131624737));
                } else {
                    this.statusTextView.setText(LocaleController.getString("BotStatusCantRead", 2131624736));
                }
            } else if (tLRPC$User.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || (((tLRPC$UserStatus = tLRPC$User.status) != null && tLRPC$UserStatus.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(tLRPC$User.id)))) {
                this.statusTextView.setTextColor(this.statusOnlineColor);
                this.statusTextView.setText(LocaleController.getString("Online", 2131627080));
            } else {
                this.statusTextView.setTextColor(this.statusColor);
                this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, tLRPC$User));
            }
            this.avatarImageView.setForUserOrChat(tLRPC$User, this.avatarDrawable);
        } else if (tLRPC$Chat != 0) {
            this.statusTextView.setTextColor(this.statusColor);
            if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                int i3 = tLRPC$Chat.participants_count;
                if (i3 != 0) {
                    this.statusTextView.setText(LocaleController.formatPluralString("Subscribers", i3, new Object[0]));
                } else if (TextUtils.isEmpty(tLRPC$Chat.username)) {
                    this.statusTextView.setText(LocaleController.getString("ChannelPrivate", 2131624946));
                } else {
                    this.statusTextView.setText(LocaleController.getString("ChannelPublic", 2131624949));
                }
            } else {
                int i4 = tLRPC$Chat.participants_count;
                if (i4 != 0) {
                    this.statusTextView.setText(LocaleController.formatPluralString("Members", i4, new Object[0]));
                } else if (tLRPC$Chat.has_geo) {
                    this.statusTextView.setText(LocaleController.getString("MegaLocation", 2131626585));
                } else if (TextUtils.isEmpty(tLRPC$Chat.username)) {
                    this.statusTextView.setText(LocaleController.getString("MegaPrivate", 2131626586));
                } else {
                    this.statusTextView.setText(LocaleController.getString("MegaPublic", 2131626589));
                }
            }
            this.avatarImageView.setForUserOrChat(tLRPC$Chat, this.avatarDrawable);
        } else {
            this.avatarImageView.setImageDrawable(this.avatarDrawable);
        }
        if (!(this.imageView.getVisibility() == 0 && this.currentDrawable == 0) && (this.imageView.getVisibility() != 8 || this.currentDrawable == 0)) {
            return;
        }
        ImageView imageView = this.imageView;
        if (this.currentDrawable == 0) {
            i2 = 8;
        }
        imageView.setVisibility(i2);
        this.imageView.setImageResource(this.currentDrawable);
    }
}