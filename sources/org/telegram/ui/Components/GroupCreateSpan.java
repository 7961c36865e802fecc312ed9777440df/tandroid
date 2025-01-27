package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCreateUserCell;

/* loaded from: classes3.dex */
public class GroupCreateSpan extends View {
    private AvatarDrawable avatarDrawable;
    private int[] colors;
    private ContactsController.Contact currentContact;
    private Drawable deleteDrawable;
    private boolean deleting;
    private boolean drawAvatarBackground;
    private ImageReceiver imageReceiver;
    public boolean isFlag;
    private String key;
    private long lastUpdateTime;
    private StaticLayout nameLayout;
    private float progress;
    private RectF rect;
    private Theme.ResourcesProvider resourcesProvider;
    private boolean small;
    private int textWidth;
    private float textX;
    private long uid;
    private static TextPaint textPaint = new TextPaint(1);
    private static Paint backPaint = new Paint(1);

    public GroupCreateSpan(Context context, Object obj) {
        this(context, obj, null);
    }

    public GroupCreateSpan(Context context, Object obj, ContactsController.Contact contact) {
        this(context, obj, contact, null);
    }

    public GroupCreateSpan(Context context, Object obj, ContactsController.Contact contact, Theme.ResourcesProvider resourcesProvider) {
        this(context, obj, contact, false, resourcesProvider);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:17:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x02df  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x02eb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x035c  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x02e2  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x02d2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public GroupCreateSpan(Context context, Object obj, ContactsController.Contact contact, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        String str;
        ImageLocation forUserOrChat;
        TLRPC.Chat chat;
        int min;
        StaticLayout staticLayout;
        ImageReceiver imageReceiver;
        Drawable makeMiniAppsDrawable;
        char c;
        int i;
        this.rect = new RectF();
        this.colors = new int[8];
        this.drawAvatarBackground = true;
        this.resourcesProvider = resourcesProvider;
        this.small = z;
        this.isFlag = false;
        this.currentContact = contact;
        this.deleteDrawable = getResources().getDrawable(R.drawable.delete);
        textPaint.setTextSize(AndroidUtilities.dp(z ? 13.0f : 14.0f));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        this.avatarDrawable = avatarDrawable;
        avatarDrawable.setTextSize(AndroidUtilities.dp(20.0f));
        boolean z2 = obj instanceof String;
        if (!z2) {
            if (obj instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) obj;
                this.uid = user.id;
                if (UserObject.isReplyUser(user)) {
                    str = LocaleController.getString(R.string.RepliesTitle);
                    this.avatarDrawable.setScaleSize(0.8f);
                    this.avatarDrawable.setAvatarType(12);
                } else if (UserObject.isUserSelf(user)) {
                    str = LocaleController.getString(R.string.SavedMessages);
                    this.avatarDrawable.setScaleSize(0.8f);
                    this.avatarDrawable.setAvatarType(1);
                } else {
                    this.avatarDrawable.setInfo(user);
                    String firstName = UserObject.getFirstName(user);
                    int indexOf = firstName.indexOf(32);
                    firstName = indexOf >= 0 ? firstName.substring(0, indexOf) : firstName;
                    ImageLocation forUserOrChat2 = ImageLocation.getForUserOrChat(user, 1);
                    chat = user;
                    str = firstName;
                    forUserOrChat = forUserOrChat2;
                }
            } else if (obj instanceof TLRPC.Chat) {
                TLRPC.Chat chat2 = (TLRPC.Chat) obj;
                this.avatarDrawable.setInfo(chat2);
                this.uid = -chat2.id;
                str = chat2.title;
                forUserOrChat = ImageLocation.getForUserOrChat(chat2, 1);
                chat = chat2;
            } else if (obj instanceof TLRPC.TL_help_country) {
                TLRPC.TL_help_country tL_help_country = (TLRPC.TL_help_country) obj;
                String languageFlag = LocaleController.getLanguageFlag(tL_help_country.iso2);
                String str2 = tL_help_country.default_name;
                this.avatarDrawable.setAvatarType(17);
                this.avatarDrawable.setTextSize(AndroidUtilities.dp(24.0f));
                this.avatarDrawable.setInfo(0L, languageFlag, null, null);
                this.avatarDrawable.setColor(Theme.multAlpha(Theme.getColor(Theme.key_text_RedRegular, resourcesProvider), 0.7f));
                AvatarDrawable avatarDrawable2 = this.avatarDrawable;
                this.drawAvatarBackground = false;
                avatarDrawable2.setDrawAvatarBackground(false);
                this.uid = tL_help_country.default_name.hashCode();
                str = str2;
            } else {
                this.avatarDrawable.setInfo(0L, contact.first_name, contact.last_name);
                this.uid = contact.contact_id;
                this.key = contact.key;
                str = !TextUtils.isEmpty(contact.first_name) ? contact.first_name : contact.last_name;
            }
            ImageReceiver imageReceiver2 = new ImageReceiver();
            this.imageReceiver = imageReceiver2;
            imageReceiver2.setRoundRadius(AndroidUtilities.dp(16.0f));
            this.imageReceiver.setParentView(this);
            this.imageReceiver.setImageCoords(!this.drawAvatarBackground ? 0.0f : AndroidUtilities.dp(4.0f), 0.0f, AndroidUtilities.dp(!z ? 28.0f : 32.0f), AndroidUtilities.dp(z ? 28.0f : 32.0f));
            if (AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                min = (Math.min(point.x, point.y) - AndroidUtilities.dp((z ? 28 : 32) + NotificationCenter.httpFileDidFailedLoad)) / 2;
            } else {
                min = AndroidUtilities.dp(398 - (z ? 28 : 32)) / 2;
            }
            staticLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(str.replace('\n', ' '), textPaint.getFontMetricsInt(), false), textPaint, min, TextUtils.TruncateAt.END), textPaint, MediaDataController.MAX_STYLE_RUNS_COUNT, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.nameLayout = staticLayout;
            if (staticLayout.getLineCount() > 0) {
                this.textWidth = (int) Math.ceil(this.nameLayout.getLineWidth(0));
                this.textX = -this.nameLayout.getLineLeft(0);
            }
            if (!z2 && "premium".equals((String) obj)) {
                imageReceiver = this.imageReceiver;
                makeMiniAppsDrawable = GroupCreateUserCell.makePremiumUsersDrawable(getContext(), true);
            } else {
                if (z2 || !"miniapps".equals((String) obj)) {
                    this.imageReceiver.setImage(forUserOrChat, "50_50", this.avatarDrawable, 0L, (String) null, chat, 1);
                    updateColors();
                    NotificationCenter.listenEmojiLoading(this);
                }
                imageReceiver = this.imageReceiver;
                makeMiniAppsDrawable = GroupCreateUserCell.makeMiniAppsDrawable(getContext(), true);
            }
            imageReceiver.setImageBitmap(makeMiniAppsDrawable);
            updateColors();
            NotificationCenter.listenEmojiLoading(this);
        }
        String str3 = (String) obj;
        this.avatarDrawable.setScaleSize(0.8f);
        switch (str3.hashCode()) {
            case -1716307998:
                if (str3.equals("archived")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1359418551:
                if (str3.equals("miniapps")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1237460524:
                if (str3.equals("groups")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1197490811:
                if (str3.equals("non_contacts")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -567451565:
                if (str3.equals("contacts")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -318452137:
                if (str3.equals("premium")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -268161860:
                if (str3.equals("new_chats")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 3029900:
                if (str3.equals("bots")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 3496342:
                if (str3.equals("read")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 104264043:
                if (str3.equals("muted")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 151051367:
                if (str3.equals("existing_chats")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1432626128:
                if (str3.equals("channels")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.avatarDrawable.setAvatarType(4);
                this.uid = Long.MIN_VALUE;
                i = R.string.FilterContacts;
                break;
            case 1:
                this.avatarDrawable.setAvatarType(5);
                this.uid = -9223372036854775807L;
                i = R.string.FilterNonContacts;
                break;
            case 2:
                this.avatarDrawable.setAvatarType(6);
                this.uid = -9223372036854775806L;
                i = R.string.FilterGroups;
                break;
            case 3:
                this.avatarDrawable.setAvatarType(7);
                this.uid = -9223372036854775805L;
                i = R.string.FilterChannels;
                break;
            case 4:
                this.avatarDrawable.setAvatarType(8);
                this.uid = -9223372036854775804L;
                i = R.string.FilterBots;
                break;
            case 5:
                this.avatarDrawable.setAvatarType(9);
                this.uid = -9223372036854775803L;
                i = R.string.FilterMuted;
                break;
            case 6:
                this.avatarDrawable.setAvatarType(10);
                this.uid = -9223372036854775802L;
                i = R.string.FilterRead;
                break;
            case 7:
                this.avatarDrawable.setAvatarType(23);
                this.uid = -9223372036854775800L;
                i = R.string.FilterExistingChats;
                break;
            case '\b':
                this.avatarDrawable.setAvatarType(24);
                this.uid = -9223372036854775799L;
                i = R.string.FilterNewChats;
                break;
            case '\t':
                this.isFlag = true;
                this.avatarDrawable.setColor(Theme.getColor(Theme.key_premiumGradientBackground2, resourcesProvider));
                i = R.string.PrivacyPremium;
                break;
            case '\n':
                this.isFlag = true;
                this.avatarDrawable.setColor(Theme.getColor(Theme.key_avatar_backgroundBlue, resourcesProvider), Theme.getColor(Theme.key_avatar_background2Blue, resourcesProvider));
                i = R.string.PrivacyMiniapps;
                break;
            default:
                this.avatarDrawable.setAvatarType(11);
                this.uid = -9223372036854775801L;
                i = R.string.FilterArchived;
                break;
        }
        str = LocaleController.getString(i);
        forUserOrChat = null;
        chat = null;
        ImageReceiver imageReceiver22 = new ImageReceiver();
        this.imageReceiver = imageReceiver22;
        imageReceiver22.setRoundRadius(AndroidUtilities.dp(16.0f));
        this.imageReceiver.setParentView(this);
        this.imageReceiver.setImageCoords(!this.drawAvatarBackground ? 0.0f : AndroidUtilities.dp(4.0f), 0.0f, AndroidUtilities.dp(!z ? 28.0f : 32.0f), AndroidUtilities.dp(z ? 28.0f : 32.0f));
        if (AndroidUtilities.isTablet()) {
        }
        staticLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(str.replace('\n', ' '), textPaint.getFontMetricsInt(), false), textPaint, min, TextUtils.TruncateAt.END), textPaint, MediaDataController.MAX_STYLE_RUNS_COUNT, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.nameLayout = staticLayout;
        if (staticLayout.getLineCount() > 0) {
        }
        if (!z2) {
        }
        if (z2) {
        }
        this.imageReceiver.setImage(forUserOrChat, "50_50", this.avatarDrawable, 0L, (String) null, chat, 1);
        updateColors();
        NotificationCenter.listenEmojiLoading(this);
    }

    public GroupCreateSpan(Context context, ContactsController.Contact contact) {
        this(context, null, contact);
    }

    public void cancelDeleteAnimation() {
        if (this.deleting) {
            this.deleting = false;
            this.lastUpdateTime = System.currentTimeMillis();
            invalidate();
        }
    }

    public ContactsController.Contact getContact() {
        return this.currentContact;
    }

    public String getKey() {
        return this.key;
    }

    public long getUid() {
        return this.uid;
    }

    public boolean isDeleting() {
        return this.deleting;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        boolean z = this.deleting;
        if ((z && this.progress != 1.0f) || (!z && this.progress != 0.0f)) {
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            if (currentTimeMillis < 0 || currentTimeMillis > 17) {
                currentTimeMillis = 17;
            }
            if (this.deleting) {
                float f = this.progress + (currentTimeMillis / 120.0f);
                this.progress = f;
                if (f >= 1.0f) {
                    this.progress = 1.0f;
                }
            } else {
                float f2 = this.progress - (currentTimeMillis / 120.0f);
                this.progress = f2;
                if (f2 < 0.0f) {
                    this.progress = 0.0f;
                }
            }
            invalidate();
        }
        canvas.save();
        this.rect.set(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.dp(this.small ? 28.0f : 32.0f));
        Paint paint = backPaint;
        int[] iArr = this.colors;
        int i = iArr[6];
        float f3 = iArr[7] - i;
        float f4 = this.progress;
        paint.setColor(Color.argb(i + ((int) (f3 * f4)), iArr[0] + ((int) ((iArr[1] - r5) * f4)), iArr[2] + ((int) ((iArr[3] - r7) * f4)), iArr[4] + ((int) ((iArr[5] - r8) * f4))));
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(this.small ? 14.0f : 16.0f), AndroidUtilities.dp(this.small ? 14.0f : 16.0f), backPaint);
        if (this.progress != 1.0f) {
            this.imageReceiver.draw(canvas);
        }
        if (this.progress != 0.0f) {
            backPaint.setColor(this.avatarDrawable.getColor());
            backPaint.setAlpha((int) (this.progress * 255.0f * (Color.alpha(r0) / 255.0f)));
            canvas.drawCircle(AndroidUtilities.dp(this.small ? 14.0f : 16.0f), AndroidUtilities.dp(this.small ? 14.0f : 16.0f), AndroidUtilities.dp(this.small ? 14.0f : 16.0f), backPaint);
            canvas.save();
            canvas.rotate((1.0f - this.progress) * 45.0f, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.deleteDrawable.setBounds(AndroidUtilities.dp(this.small ? 9.0f : 11.0f), AndroidUtilities.dp(this.small ? 9.0f : 11.0f), AndroidUtilities.dp(this.small ? 19.0f : 21.0f), AndroidUtilities.dp(this.small ? 19.0f : 21.0f));
            this.deleteDrawable.setAlpha((int) (this.progress * 255.0f));
            this.deleteDrawable.draw(canvas);
            canvas.restore();
        }
        canvas.translate(this.textX + AndroidUtilities.dp((this.small ? 26 : 32) + 9), AndroidUtilities.dp(this.small ? 6.0f : 8.0f));
        textPaint.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_groupcreate_spanText, this.resourcesProvider), Theme.getColor(Theme.key_avatar_text, this.resourcesProvider), this.progress));
        this.nameLayout.draw(canvas);
        canvas.restore();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        AccessibilityNodeInfo.AccessibilityAction accessibilityAction;
        int id;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setText(this.nameLayout.getText());
        if (!isDeleting() || Build.VERSION.SDK_INT < 21) {
            return;
        }
        accessibilityAction = AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK;
        id = accessibilityAction.getId();
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(id, LocaleController.getString(R.string.Delete)));
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(AndroidUtilities.dp((this.small ? 20 : 32) + 25) + this.textWidth, AndroidUtilities.dp(this.small ? 28.0f : 32.0f));
    }

    public void startDeleteAnimation() {
        if (this.deleting) {
            return;
        }
        this.deleting = true;
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public void updateColors() {
        int color = this.avatarDrawable.getColor();
        int color2 = Theme.getColor(Theme.key_groupcreate_spanBackground, this.resourcesProvider);
        int color3 = Theme.getColor(Theme.key_groupcreate_spanDelete, this.resourcesProvider);
        this.colors[0] = Color.red(color2);
        this.colors[1] = Color.red(color);
        this.colors[2] = Color.green(color2);
        this.colors[3] = Color.green(color);
        this.colors[4] = Color.blue(color2);
        this.colors[5] = Color.blue(color);
        this.colors[6] = Color.alpha(color2);
        this.colors[7] = Color.alpha(color);
        this.deleteDrawable.setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.MULTIPLY));
        backPaint.setColor(color2);
    }
}
