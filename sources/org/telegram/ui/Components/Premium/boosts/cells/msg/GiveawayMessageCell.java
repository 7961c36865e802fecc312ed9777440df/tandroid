package org.telegram.ui.Components.Premium.boosts.cells.msg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.StateSet;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerPack;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.StaticLayoutEx;
/* loaded from: classes4.dex */
public class GiveawayMessageCell {
    private static final Map<Integer, String> monthsToEmoticon;
    private AvatarDrawable[] avatarDrawables;
    private ImageReceiver[] avatarImageReceivers;
    private boolean[] avatarVisible;
    private int bottomHeight;
    private StaticLayout bottomLayout;
    private final Paint chatBgPaint;
    private final RectF chatRect;
    private final TextPaint chatTextPaint;
    private final Paint clipRectPaint;
    private final Rect containerRect;
    private final RectF countRect;
    private final Paint counterBgPaint;
    private String counterStr;
    private final Rect counterTextBounds;
    private final TextPaint counterTextPaint;
    private int countriesHeight;
    private StaticLayout countriesLayout;
    private final TextPaint countriesTextPaint;
    private int diffTextWidth;
    private final ImageReceiver giftReceiver;
    private boolean isButtonPressed;
    private boolean isContainerPressed;
    private MessageObject messageObject;
    private final ChatMessageCell parentView;
    private int pressedPos;
    private final int[] pressedState;
    private final Paint saveLayerPaint;
    private int selectorColor;
    private Drawable selectorDrawable;
    private final TextPaint textPaint;
    private int topHeight;
    private StaticLayout topLayout;
    private CharSequence[] chatTitles = new CharSequence[10];
    private TLRPC$Chat[] chats = new TLRPC$Chat[10];
    private float[] chatTitleWidths = new float[10];
    private boolean[] needNewRow = new boolean[10];
    private Rect[] clickRect = new Rect[10];
    private int measuredHeight = 0;
    private int measuredWidth = 0;

    static {
        HashMap hashMap = new HashMap();
        monthsToEmoticon = hashMap;
        hashMap.put(1, "1⃣");
        hashMap.put(3, "2⃣");
        hashMap.put(6, "3⃣");
        hashMap.put(12, "4⃣");
        hashMap.put(24, "5⃣");
    }

    public GiveawayMessageCell(ChatMessageCell chatMessageCell) {
        TextPaint textPaint = new TextPaint(1);
        this.counterTextPaint = textPaint;
        TextPaint textPaint2 = new TextPaint(1);
        this.chatTextPaint = textPaint2;
        TextPaint textPaint3 = new TextPaint(1);
        this.textPaint = textPaint3;
        TextPaint textPaint4 = new TextPaint(1);
        this.countriesTextPaint = textPaint4;
        this.counterBgPaint = new Paint(1);
        this.chatBgPaint = new Paint(1);
        this.saveLayerPaint = new Paint();
        Paint paint = new Paint();
        this.clipRectPaint = paint;
        this.countRect = new RectF();
        this.chatRect = new RectF();
        this.counterTextBounds = new Rect();
        this.containerRect = new Rect();
        this.pressedState = new int[]{16842910, 16842919};
        this.pressedPos = -1;
        this.isButtonPressed = false;
        this.isContainerPressed = false;
        this.parentView = chatMessageCell;
        ImageReceiver imageReceiver = new ImageReceiver(chatMessageCell);
        this.giftReceiver = imageReceiver;
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textPaint2.setTextSize(AndroidUtilities.dp(13.0f));
        textPaint4.setTextSize(AndroidUtilities.dp(13.0f));
        textPaint3.setTextSize(AndroidUtilities.dp(14.0f));
    }

    public boolean checkMotionEvent(MotionEvent motionEvent) {
        MessageObject messageObject = this.messageObject;
        if (messageObject != null && messageObject.isGiveaway()) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (motionEvent.getAction() == 0) {
                int i = 0;
                while (true) {
                    Rect[] rectArr = this.clickRect;
                    if (i < rectArr.length) {
                        if (rectArr[i].contains(x, y)) {
                            this.pressedPos = i;
                            if (Build.VERSION.SDK_INT >= 21) {
                                this.selectorDrawable.setHotspot(x, y);
                            }
                            this.isButtonPressed = true;
                            setButtonPressed(true);
                            return true;
                        }
                        i++;
                    } else if (this.containerRect.contains(x, y)) {
                        this.isContainerPressed = true;
                        return true;
                    }
                }
            } else if (motionEvent.getAction() == 1) {
                if (this.isButtonPressed) {
                    if (this.parentView.getDelegate() != null) {
                        this.parentView.getDelegate().didPressGiveawayChatButton(this.parentView, this.pressedPos);
                    }
                    this.parentView.playSoundEffect(0);
                    setButtonPressed(false);
                    this.isButtonPressed = false;
                }
                if (this.isContainerPressed) {
                    this.isContainerPressed = false;
                    BoostDialogs.showBulletinAbout(this.messageObject);
                }
            } else if (motionEvent.getAction() != 2 && motionEvent.getAction() == 3) {
                if (this.isButtonPressed) {
                    setButtonPressed(false);
                }
                this.isButtonPressed = false;
                this.isContainerPressed = false;
            }
        }
        return false;
    }

    public void setButtonPressed(boolean z) {
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || !messageObject.isGiveaway()) {
            return;
        }
        if (z) {
            this.selectorDrawable.setCallback(new Drawable.Callback() { // from class: org.telegram.ui.Components.Premium.boosts.cells.msg.GiveawayMessageCell.1
                @Override // android.graphics.drawable.Drawable.Callback
                public void invalidateDrawable(Drawable drawable) {
                    GiveawayMessageCell.this.parentView.invalidate();
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
                    GiveawayMessageCell.this.parentView.invalidate();
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
                    GiveawayMessageCell.this.parentView.invalidate();
                }
            });
            this.selectorDrawable.setState(this.pressedState);
            this.parentView.invalidate();
            return;
        }
        this.selectorDrawable.setState(StateSet.NOTHING);
        this.parentView.invalidate();
    }

    public void setMessageContent(MessageObject messageObject, int i, int i2) {
        int dp;
        this.messageObject = null;
        this.topLayout = null;
        this.bottomLayout = null;
        this.countriesLayout = null;
        this.measuredHeight = 0;
        this.measuredWidth = 0;
        if (messageObject.isGiveaway()) {
            this.messageObject = messageObject;
            createImages();
            setGiftImage(messageObject);
            TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) messageObject.messageOwner.media;
            checkArraysLimits(tLRPC$TL_messageMediaGiveaway.channels.size());
            int dp2 = AndroidUtilities.dp(148.0f);
            if (AndroidUtilities.isTablet()) {
                dp = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(80.0f);
            } else {
                dp = i - AndroidUtilities.dp(80.0f);
            }
            SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(LocaleController.getString("BoostingGiveawayPrizes", R.string.BoostingGiveawayPrizes));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(replaceTags);
            spannableStringBuilder.setSpan(new RelativeSizeSpan(1.05f), 0, replaceTags.length(), 33);
            spannableStringBuilder.append((CharSequence) "\n");
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
            spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("BoostingGiveawayMsgInfoPlural1", tLRPC$TL_messageMediaGiveaway.quantity)));
            spannableStringBuilder2.append((CharSequence) "\n");
            spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatPluralString("BoostingGiveawayMsgInfoPlural2", tLRPC$TL_messageMediaGiveaway.quantity, LocaleController.formatPluralString("BoldMonths", tLRPC$TL_messageMediaGiveaway.months, new Object[0]))));
            spannableStringBuilder.append((CharSequence) spannableStringBuilder2);
            spannableStringBuilder.append((CharSequence) "\n\n");
            spannableStringBuilder.setSpan(new RelativeSizeSpan(0.5f), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
            SpannableStringBuilder replaceTags2 = AndroidUtilities.replaceTags(LocaleController.getString("BoostingGiveawayMsgParticipants", R.string.BoostingGiveawayMsgParticipants));
            spannableStringBuilder.append((CharSequence) replaceTags2);
            spannableStringBuilder.setSpan(new RelativeSizeSpan(1.05f), replaceTags.length() + spannableStringBuilder2.length() + 2, replaceTags.length() + spannableStringBuilder2.length() + 3 + replaceTags2.length(), 33);
            spannableStringBuilder.append((CharSequence) "\n");
            if (tLRPC$TL_messageMediaGiveaway.only_new_subscribers) {
                spannableStringBuilder.append((CharSequence) LocaleController.formatPluralString("BoostingGiveawayMsgNewSubsPlural", tLRPC$TL_messageMediaGiveaway.channels.size(), new Object[0]));
            } else {
                spannableStringBuilder.append((CharSequence) LocaleController.formatPluralString("BoostingGiveawayMsgAllSubsPlural", tLRPC$TL_messageMediaGiveaway.channels.size(), new Object[0]));
            }
            SpannableStringBuilder replaceTags3 = AndroidUtilities.replaceTags(LocaleController.getString("BoostingWinnersDate", R.string.BoostingWinnersDate));
            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(replaceTags3);
            spannableStringBuilder3.setSpan(new RelativeSizeSpan(1.05f), 0, replaceTags3.length(), 33);
            Date date = new Date(tLRPC$TL_messageMediaGiveaway.until_date * 1000);
            String format = LocaleController.getInstance().formatterGiveawayCard.format(date);
            String format2 = LocaleController.getInstance().formatterDay.format(date);
            spannableStringBuilder3.append((CharSequence) "\n");
            spannableStringBuilder3.append((CharSequence) LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, format, format2));
            int i3 = dp;
            int i4 = dp;
            this.topLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder, this.textPaint, i3, Layout.Alignment.ALIGN_CENTER, 1.0f, AndroidUtilities.dp(2.0f), false, TextUtils.TruncateAt.END, i4, 10);
            this.bottomLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder3, this.textPaint, i3, Layout.Alignment.ALIGN_CENTER, 1.0f, AndroidUtilities.dp(2.0f), false, TextUtils.TruncateAt.END, i4, 10);
            int i5 = 0;
            for (int i6 = 0; i6 < this.topLayout.getLineCount(); i6++) {
                i5 = (int) Math.max(i5, Math.ceil(this.topLayout.getLineWidth(i6)));
            }
            for (int i7 = 0; i7 < this.bottomLayout.getLineCount(); i7++) {
                i5 = (int) Math.max(i5, Math.ceil(this.bottomLayout.getLineWidth(i7)));
            }
            if (i5 < AndroidUtilities.dp(180.0f)) {
                i5 = AndroidUtilities.dp(180.0f);
            }
            if (tLRPC$TL_messageMediaGiveaway.countries_iso2.size() > 0) {
                ArrayList arrayList = new ArrayList();
                Iterator<String> it = tLRPC$TL_messageMediaGiveaway.countries_iso2.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    String displayCountry = new Locale("", next).getDisplayCountry(Locale.getDefault());
                    String languageFlag = LocaleController.getLanguageFlag(next);
                    SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder();
                    if (languageFlag != null) {
                        spannableStringBuilder4.append((CharSequence) languageFlag).append((CharSequence) " ");
                    }
                    spannableStringBuilder4.append((CharSequence) displayCountry);
                    arrayList.add(spannableStringBuilder4);
                }
                if (!arrayList.isEmpty()) {
                    this.countriesLayout = StaticLayoutEx.createStaticLayout(Emoji.replaceEmoji(AndroidUtilities.replaceTags(LocaleController.formatString("BoostingGiveAwayFromCountries", R.string.BoostingGiveAwayFromCountries, TextUtils.join(", ", arrayList))), this.countriesTextPaint.getFontMetricsInt(), false), this.countriesTextPaint, i5, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, i5, 10);
                }
            }
            int max = Math.max(i2, Math.min(i5 + AndroidUtilities.dp(38.0f), dp));
            this.diffTextWidth = max - dp;
            float f = max;
            float f2 = dp2;
            float f3 = f2 / 2.0f;
            this.giftReceiver.setImageCoords((f / 2.0f) - f3, AndroidUtilities.dp(42.0f) - f3, f2, f2);
            StaticLayout staticLayout = this.topLayout;
            this.topHeight = staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
            StaticLayout staticLayout2 = this.bottomLayout;
            this.bottomHeight = staticLayout2.getLineBottom(staticLayout2.getLineCount() - 1);
            StaticLayout staticLayout3 = this.countriesLayout;
            int lineBottom = staticLayout3 != null ? staticLayout3.getLineBottom(staticLayout3.getLineCount() - 1) + AndroidUtilities.dp(12.0f) : 0;
            this.countriesHeight = lineBottom;
            int i8 = this.measuredHeight + this.topHeight;
            this.measuredHeight = i8;
            int i9 = i8 + lineBottom;
            this.measuredHeight = i9;
            int i10 = i9 + this.bottomHeight;
            this.measuredHeight = i10;
            this.measuredHeight = i10 + AndroidUtilities.dp(128.0f);
            this.measuredWidth = max;
            String str = "x" + tLRPC$TL_messageMediaGiveaway.quantity;
            this.counterStr = str;
            this.counterTextPaint.getTextBounds(str, 0, str.length(), this.counterTextBounds);
            Arrays.fill(this.avatarVisible, false);
            this.measuredHeight += AndroidUtilities.dp(30.0f);
            ArrayList arrayList2 = new ArrayList(tLRPC$TL_messageMediaGiveaway.channels.size());
            Iterator<Long> it2 = tLRPC$TL_messageMediaGiveaway.channels.iterator();
            while (it2.hasNext()) {
                Long next2 = it2.next();
                if (MessagesController.getInstance(UserConfig.selectedAccount).getChat(next2) != null) {
                    arrayList2.add(next2);
                }
            }
            float f4 = 0.0f;
            for (int i11 = 0; i11 < arrayList2.size(); i11++) {
                long longValue = ((Long) arrayList2.get(i11)).longValue();
                TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(longValue));
                if (chat != null) {
                    this.avatarVisible[i11] = true;
                    this.chats[i11] = chat;
                    this.chatTitles[i11] = TextUtils.ellipsize(Emoji.replaceEmoji((CharSequence) chat.title, this.chatTextPaint.getFontMetricsInt(), AndroidUtilities.dp(14.0f), false), this.chatTextPaint, 0.8f * f, TextUtils.TruncateAt.END);
                    float[] fArr = this.chatTitleWidths;
                    TextPaint textPaint = this.chatTextPaint;
                    CharSequence[] charSequenceArr = this.chatTitles;
                    fArr[i11] = textPaint.measureText(charSequenceArr[i11], 0, charSequenceArr[i11].length());
                    float dp3 = this.chatTitleWidths[i11] + AndroidUtilities.dp(42.0f);
                    f4 += dp3;
                    if (i11 > 0) {
                        boolean[] zArr = this.needNewRow;
                        zArr[i11] = f4 > 0.9f * f;
                        if (zArr[i11]) {
                            this.measuredHeight += AndroidUtilities.dp(30.0f);
                            f4 = dp3;
                        }
                    } else {
                        this.needNewRow[i11] = false;
                    }
                    this.avatarDrawables[i11].setInfo(chat);
                    this.avatarImageReceivers[i11].setForUserOrChat(chat, this.avatarDrawables[i11]);
                    this.avatarImageReceivers[i11].setImageCoords(0.0f, 0.0f, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
                } else {
                    this.chats[i11] = null;
                    this.avatarVisible[i11] = false;
                    this.chatTitles[i11] = "";
                    this.needNewRow[i11] = false;
                    this.chatTitleWidths[i11] = AndroidUtilities.dp(20.0f);
                    this.avatarDrawables[i11].setInfo(longValue, "", "");
                }
            }
        }
    }

    private int getChatColor(TLRPC$Chat tLRPC$Chat, Theme.ResourcesProvider resourcesProvider) {
        int i = (tLRPC$Chat.flags2 & 64) != 0 ? tLRPC$Chat.color : (int) (tLRPC$Chat.id % 7);
        if (i < 7) {
            return Theme.getColor(Theme.keys_avatar_nameInMessage[i], resourcesProvider);
        }
        MessagesController.PeerColors peerColors = MessagesController.getInstance(UserConfig.selectedAccount).peerColors;
        MessagesController.PeerColor color = peerColors == null ? null : peerColors.getColor(i);
        if (color != null) {
            return color.getColor1();
        }
        return Theme.getColor(Theme.keys_avatar_nameInMessage[0], resourcesProvider);
    }

    public void draw(Canvas canvas, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        boolean[] zArr;
        int i3;
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || !messageObject.isGiveaway()) {
            return;
        }
        if (this.selectorDrawable == null) {
            int color = Theme.getColor(Theme.key_listSelector);
            this.selectorColor = color;
            this.selectorDrawable = Theme.createRadSelectorDrawable(color, 12, 12);
        }
        this.textPaint.setColor(Theme.chat_msgTextPaint.getColor());
        this.countriesTextPaint.setColor(Theme.chat_msgTextPaint.getColor());
        if (this.messageObject.isOutOwner()) {
            TextPaint textPaint = this.chatTextPaint;
            int i4 = Theme.key_chat_outPreviewInstantText;
            textPaint.setColor(Theme.getColor(i4, resourcesProvider));
            this.counterBgPaint.setColor(Theme.getColor(i4, resourcesProvider));
            this.chatBgPaint.setColor(Theme.getColor(Theme.key_chat_outReplyLine, resourcesProvider));
        } else {
            TextPaint textPaint2 = this.chatTextPaint;
            int i5 = Theme.key_chat_inPreviewInstantText;
            textPaint2.setColor(Theme.getColor(i5, resourcesProvider));
            this.counterBgPaint.setColor(Theme.getColor(i5, resourcesProvider));
            this.chatBgPaint.setColor(Theme.getColor(Theme.key_chat_inReplyLine, resourcesProvider));
        }
        canvas.save();
        int dp = i2 - AndroidUtilities.dp(4.0f);
        canvas.translate(dp, i);
        this.containerRect.set(dp, i, getMeasuredWidth() + dp, getMeasuredHeight() + i);
        canvas.saveLayer(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.saveLayerPaint, 31);
        this.giftReceiver.draw(canvas);
        float f = 2.0f;
        float measuredWidth = getMeasuredWidth() / 2.0f;
        float dp2 = AndroidUtilities.dp(106.0f);
        int width = this.counterTextBounds.width() + AndroidUtilities.dp(12.0f);
        int height = this.counterTextBounds.height() + AndroidUtilities.dp(10.0f);
        this.countRect.set(measuredWidth - ((AndroidUtilities.dp(2.0f) + width) / 2.0f), dp2 - ((AndroidUtilities.dp(2.0f) + height) / 2.0f), ((width + AndroidUtilities.dp(2.0f)) / 2.0f) + measuredWidth, ((height + AndroidUtilities.dp(2.0f)) / 2.0f) + dp2);
        canvas.drawRoundRect(this.countRect, AndroidUtilities.dp(11.0f), AndroidUtilities.dp(11.0f), this.clipRectPaint);
        float f2 = width / 2.0f;
        float f3 = height / 2.0f;
        this.countRect.set(measuredWidth - f2, dp2 - f3, f2 + measuredWidth, dp2 + f3);
        canvas.drawRoundRect(this.countRect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.counterBgPaint);
        canvas.drawText(this.counterStr, this.countRect.centerX(), this.countRect.centerY() + AndroidUtilities.dp(4.0f), this.counterTextPaint);
        canvas.restore();
        canvas.translate(0.0f, AndroidUtilities.dp(128.0f));
        canvas.save();
        canvas.translate(this.diffTextWidth / 2.0f, 0.0f);
        this.topLayout.draw(canvas);
        canvas.restore();
        canvas.translate(0.0f, this.topHeight + AndroidUtilities.dp(6.0f));
        int i6 = 0;
        int dp3 = AndroidUtilities.dp(128.0f) + i + this.topHeight + AndroidUtilities.dp(6.0f);
        int i7 = 0;
        while (true) {
            boolean[] zArr2 = this.avatarVisible;
            if (i6 >= zArr2.length) {
                break;
            }
            if (zArr2[i6]) {
                canvas.save();
                int i8 = i6;
                float f4 = 0.0f;
                do {
                    f4 += this.chatTitleWidths[i8] + AndroidUtilities.dp(42.0f);
                    i8++;
                    zArr = this.avatarVisible;
                    if (i8 >= zArr.length || this.needNewRow[i8]) {
                        break;
                    }
                } while (zArr[i8]);
                float f5 = measuredWidth - (f4 / f);
                canvas.translate(f5, 0.0f);
                int i9 = i6;
                int i10 = ((int) f5) + dp;
                while (true) {
                    int chatColor = getChatColor(this.chats[i9], resourcesProvider);
                    int i11 = this.pressedPos;
                    i3 = (i11 < 0 || i11 != i9) ? i7 : chatColor;
                    this.chatTextPaint.setColor(chatColor);
                    this.chatBgPaint.setColor(chatColor);
                    this.chatBgPaint.setAlpha(25);
                    this.avatarImageReceivers[i9].draw(canvas);
                    CharSequence[] charSequenceArr = this.chatTitles;
                    int i12 = i10;
                    int i13 = i9;
                    canvas.drawText(charSequenceArr[i9], 0, charSequenceArr[i9].length(), AndroidUtilities.dp(30.0f), AndroidUtilities.dp(16.0f), this.chatTextPaint);
                    this.chatRect.set(0.0f, 0.0f, this.chatTitleWidths[i13] + AndroidUtilities.dp(42.0f), AndroidUtilities.dp(24.0f));
                    canvas.drawRoundRect(this.chatRect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), this.chatBgPaint);
                    float f6 = i12;
                    this.clickRect[i13].set(i12, dp3, (int) (this.chatRect.width() + f6), AndroidUtilities.dp(24.0f) + dp3);
                    canvas.translate(this.chatRect.width() + AndroidUtilities.dp(6.0f), 0.0f);
                    i10 = (int) (f6 + this.chatRect.width() + AndroidUtilities.dp(6.0f));
                    i9 = i13 + 1;
                    boolean[] zArr3 = this.avatarVisible;
                    if (i9 >= zArr3.length || this.needNewRow[i9] || !zArr3[i9]) {
                        break;
                    }
                    i7 = i3;
                }
                canvas.restore();
                canvas.translate(0.0f, AndroidUtilities.dp(30.0f));
                dp3 += AndroidUtilities.dp(30.0f);
                i6 = i9;
                i7 = i3;
            } else {
                i6++;
            }
            f = 2.0f;
        }
        if (this.countriesLayout != null) {
            canvas.save();
            canvas.translate((this.measuredWidth - this.countriesLayout.getWidth()) / 2.0f, AndroidUtilities.dp(4.0f));
            this.countriesLayout.draw(canvas);
            canvas.restore();
            canvas.translate(0.0f, this.countriesHeight);
        }
        canvas.translate(0.0f, AndroidUtilities.dp(6.0f));
        canvas.save();
        canvas.translate(this.diffTextWidth / 2.0f, 0.0f);
        this.bottomLayout.draw(canvas);
        canvas.restore();
        canvas.restore();
        if (this.pressedPos >= 0) {
            int multAlpha = Theme.multAlpha(i7, Theme.isCurrentThemeDark() ? 0.12f : 0.1f);
            if (this.selectorColor != multAlpha) {
                Drawable drawable = this.selectorDrawable;
                this.selectorColor = multAlpha;
                Theme.setSelectorDrawableColor(drawable, multAlpha, true);
            }
            this.selectorDrawable.setBounds(this.clickRect[this.pressedPos]);
            this.selectorDrawable.draw(canvas);
        }
    }

    public void onDetachedFromWindow() {
        this.giftReceiver.onDetachedFromWindow();
        ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
        if (imageReceiverArr != null) {
            for (ImageReceiver imageReceiver : imageReceiverArr) {
                imageReceiver.onDetachedFromWindow();
            }
        }
    }

    public void onAttachedToWindow() {
        this.giftReceiver.onAttachedToWindow();
        ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
        if (imageReceiverArr != null) {
            for (ImageReceiver imageReceiver : imageReceiverArr) {
                imageReceiver.onAttachedToWindow();
            }
        }
    }

    public int getMeasuredHeight() {
        return this.measuredHeight;
    }

    public int getMeasuredWidth() {
        return this.measuredWidth;
    }

    private void createImages() {
        if (this.avatarImageReceivers != null) {
            return;
        }
        this.avatarImageReceivers = new ImageReceiver[10];
        this.avatarDrawables = new AvatarDrawable[10];
        this.avatarVisible = new boolean[10];
        int i = 0;
        while (true) {
            ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
            if (i >= imageReceiverArr.length) {
                return;
            }
            imageReceiverArr[i] = new ImageReceiver(this.parentView);
            this.avatarImageReceivers[i].setAllowLoadingOnAttachedOnly(true);
            this.avatarImageReceivers[i].setRoundRadius(AndroidUtilities.dp(12.0f));
            this.avatarDrawables[i] = new AvatarDrawable();
            this.avatarDrawables[i].setTextSize(AndroidUtilities.dp(18.0f));
            this.clickRect[i] = new Rect();
            i++;
        }
    }

    private void checkArraysLimits(int i) {
        ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
        if (imageReceiverArr.length < i) {
            int length = imageReceiverArr.length;
            this.avatarImageReceivers = (ImageReceiver[]) Arrays.copyOf(imageReceiverArr, i);
            this.avatarDrawables = (AvatarDrawable[]) Arrays.copyOf(this.avatarDrawables, i);
            this.avatarVisible = Arrays.copyOf(this.avatarVisible, i);
            this.chatTitles = (CharSequence[]) Arrays.copyOf(this.chatTitles, i);
            this.chatTitleWidths = Arrays.copyOf(this.chatTitleWidths, i);
            this.needNewRow = Arrays.copyOf(this.needNewRow, i);
            this.clickRect = (Rect[]) Arrays.copyOf(this.clickRect, i);
            this.chats = (TLRPC$Chat[]) Arrays.copyOf(this.chats, i);
            for (int i2 = length - 1; i2 < i; i2++) {
                this.avatarImageReceivers[i2] = new ImageReceiver(this.parentView);
                this.avatarImageReceivers[i2].setAllowLoadingOnAttachedOnly(true);
                this.avatarImageReceivers[i2].setRoundRadius(AndroidUtilities.dp(12.0f));
                this.avatarDrawables[i2] = new AvatarDrawable();
                this.avatarDrawables[i2].setTextSize(AndroidUtilities.dp(18.0f));
                this.clickRect[i2] = new Rect();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x008e, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setGiftImage(MessageObject messageObject) {
        TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) messageObject.messageOwner.media;
        String str = UserConfig.getInstance(UserConfig.selectedAccount).premiumGiftsStickerPack;
        if (str == null) {
            MediaDataController.getInstance(UserConfig.selectedAccount).checkPremiumGiftStickers();
            return;
        }
        TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(UserConfig.selectedAccount).getStickerSetByName(str);
        if (stickerSetByName == null) {
            stickerSetByName = MediaDataController.getInstance(UserConfig.selectedAccount).getStickerSetByEmojiOrName(str);
        }
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSetByName;
        TLRPC$Document tLRPC$Document = null;
        if (tLRPC$TL_messages_stickerSet != null) {
            String str2 = monthsToEmoticon.get(Integer.valueOf(tLRPC$TL_messageMediaGiveaway.months));
            Iterator<TLRPC$TL_stickerPack> it = tLRPC$TL_messages_stickerSet.packs.iterator();
            while (it.hasNext()) {
                TLRPC$TL_stickerPack next = it.next();
                if (Objects.equals(next.emoticon, str2)) {
                    Iterator<Long> it2 = next.documents.iterator();
                    while (it2.hasNext()) {
                        long longValue = it2.next().longValue();
                        Iterator<TLRPC$Document> it3 = tLRPC$TL_messages_stickerSet.documents.iterator();
                        while (true) {
                            if (it3.hasNext()) {
                                TLRPC$Document next2 = it3.next();
                                if (next2.id == longValue) {
                                    tLRPC$Document = next2;
                                    continue;
                                    break;
                                }
                            }
                        }
                        if (tLRPC$Document != null) {
                            break;
                        }
                    }
                    continue;
                }
                if (tLRPC$Document != null) {
                    break;
                }
            }
            if (tLRPC$Document == null && !tLRPC$TL_messages_stickerSet.documents.isEmpty()) {
                tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(0);
            }
        }
        if (tLRPC$Document != null) {
            SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document.thumbs, Theme.key_emptyListPlaceholder, 0.2f);
            if (svgThumb != null) {
                svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
            }
            this.giftReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), "160_160_firstframe", svgThumb, "tgs", tLRPC$TL_messages_stickerSet, 1);
            return;
        }
        MediaDataController.getInstance(UserConfig.selectedAccount).loadStickersByEmojiOrName(str, false, tLRPC$TL_messages_stickerSet == null);
    }
}
