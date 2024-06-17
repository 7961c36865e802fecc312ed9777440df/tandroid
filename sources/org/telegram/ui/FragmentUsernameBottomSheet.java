package org.telegram.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BillingController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.tl.TL_fragment$TL_collectibleInfo;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
/* loaded from: classes4.dex */
public class FragmentUsernameBottomSheet {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v0, types: [org.telegram.ui.ActionBar.BottomSheet] */
    /* JADX WARN: Type inference failed for: r8v2, types: [android.widget.LinearLayout] */
    /* JADX WARN: Type inference failed for: r9v9, types: [android.widget.LinearLayout, android.view.View] */
    public static void open(final Context context, final int i, String str, TLObject tLObject, final TL_fragment$TL_collectibleInfo tL_fragment$TL_collectibleInfo, final Theme.ResourcesProvider resourcesProvider) {
        String str2;
        Object obj;
        String formatString;
        String str3;
        String formatString2;
        final String format;
        String str4;
        final ?? bottomSheet = new BottomSheet(context, false, resourcesProvider);
        bottomSheet.fixNavigationBar(Theme.getColor(Theme.key_dialogBackground, resourcesProvider));
        ?? linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(80.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider)));
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(80, 80, 1, 0, 16, 0, 16));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        int i2 = i == 0 ? 70 : 78;
        rLottieImageView.setAnimation(i == 0 ? R.raw.fragment_username : R.raw.fragment_phone, i2, i2);
        rLottieImageView.playAnimation();
        rLottieImageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
        if (i == 0) {
            rLottieImageView.setScaleX(0.86f);
            rLottieImageView.setScaleY(0.86f);
        } else {
            rLottieImageView.setTranslationY(AndroidUtilities.dp(2.0f));
        }
        frameLayout.addView(rLottieImageView, LayoutHelper.createLinear(-1, -1, 17));
        if (tLObject instanceof TLRPC$User) {
            str2 = UserObject.getUserName((TLRPC$User) tLObject);
        } else {
            str2 = tLObject instanceof TLRPC$Chat ? ((TLRPC$Chat) tLObject).title : "";
        }
        String str5 = str2;
        String formatCurrency = BillingController.getInstance().formatCurrency(tL_fragment$TL_collectibleInfo.amount, tL_fragment$TL_collectibleInfo.currency);
        String formatCurrency2 = BillingController.getInstance().formatCurrency(tL_fragment$TL_collectibleInfo.crypto_amount, tL_fragment$TL_collectibleInfo.crypto_currency);
        if (i == 0) {
            String formatString3 = LocaleController.formatString(R.string.FragmentUsernameTitle, "@" + str);
            int i3 = R.string.FragmentUsernameMessage;
            Object[] objArr = new Object[3];
            obj = linearLayout;
            objArr[0] = LocaleController.formatShortDateTime(tL_fragment$TL_collectibleInfo.purchase_date);
            objArr[1] = formatCurrency2;
            if (TextUtils.isEmpty(formatCurrency)) {
                str4 = "";
            } else {
                str4 = "(" + formatCurrency + ")";
            }
            objArr[2] = str4;
            format = MessagesController.getInstance(UserConfig.selectedAccount).linkPrefix + "/" + str;
            formatString2 = LocaleController.formatString(i3, objArr);
            formatString = formatString3;
        } else {
            obj = linearLayout;
            if (i != 1) {
                return;
            }
            formatString = LocaleController.formatString(R.string.FragmentPhoneTitle, PhoneFormat.getInstance().format("+" + str));
            int i4 = R.string.FragmentPhoneMessage;
            Object[] objArr2 = new Object[3];
            objArr2[0] = LocaleController.formatShortDateTime((long) tL_fragment$TL_collectibleInfo.purchase_date);
            objArr2[1] = formatCurrency2;
            if (TextUtils.isEmpty(formatCurrency)) {
                str3 = "";
            } else {
                str3 = "(" + formatCurrency + ")";
            }
            objArr2[2] = str3;
            formatString2 = LocaleController.formatString(i4, objArr2);
            format = PhoneFormat.getInstance().format("+" + str);
        }
        final Runnable runnable = format != null ? new Runnable() { // from class: org.telegram.ui.FragmentUsernameBottomSheet$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FragmentUsernameBottomSheet.lambda$open$0(format, i, bottomSheet, resourcesProvider);
            }
        } : null;
        SpannableStringBuilder replaceSingleTag = AndroidUtilities.replaceSingleTag(formatString, runnable);
        SpannableString spannableString = new SpannableString("TON");
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_ton);
        coloredImageSpan.setWidth(AndroidUtilities.dp(13.0f));
        spannableString.setSpan(coloredImageSpan, 0, spannableString.length(), 33);
        SpannableStringBuilder replaceCharSequence = AndroidUtilities.replaceCharSequence("TON", AndroidUtilities.replaceTags(formatString2), spannableString);
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
        linksTextView.setTypeface(AndroidUtilities.bold());
        linksTextView.setGravity(17);
        int i5 = Theme.key_dialogTextBlack;
        linksTextView.setTextColor(Theme.getColor(i5, resourcesProvider));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText2, resourcesProvider));
        linksTextView.setTextSize(1, 16.0f);
        linksTextView.setText(replaceSingleTag);
        ?? r9 = obj;
        r9.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 1, 42, 0, 42, 0));
        FrameLayout frameLayout2 = new FrameLayout(context);
        frameLayout2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), Theme.getColor(Theme.key_groupcreate_spanBackground, resourcesProvider)));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(28.0f));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setInfo(tLObject);
        backupImageView.setForUserOrChat(tLObject, avatarDrawable);
        frameLayout2.addView(backupImageView, LayoutHelper.createFrame(28, 28, 51));
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(i5, resourcesProvider));
        textView.setTextSize(1, 13.0f);
        textView.setSingleLine();
        textView.setText(Emoji.replaceEmoji(str5, textView.getPaint().getFontMetricsInt(), false));
        frameLayout2.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 19, 37.0f, 0.0f, 10.0f, 0.0f));
        r9.addView(frameLayout2, LayoutHelper.createLinear(-2, 28, 1, 42, 10, 42, 18));
        TextView textView2 = new TextView(context);
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(i5, resourcesProvider));
        textView2.setTextSize(1, 14.0f);
        textView2.setText(replaceCharSequence);
        r9.addView(textView2, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 19));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView.setText(LocaleController.getString(R.string.FragmentUsernameOpen), false);
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.FragmentUsernameBottomSheet$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FragmentUsernameBottomSheet.lambda$open$1(context, tL_fragment$TL_collectibleInfo, view);
            }
        });
        r9.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 6.0f, 0.0f, 6.0f, 0.0f));
        if (runnable != null) {
            ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, false, resourcesProvider);
            buttonWithCounterView2.setText(LocaleController.getString(i == 0 ? R.string.FragmentUsernameCopy : R.string.FragmentPhoneCopy), false);
            buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.FragmentUsernameBottomSheet$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    FragmentUsernameBottomSheet.lambda$open$2(runnable, bottomSheet, view);
                }
            });
            r9.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48, 6.0f, 6.0f, 6.0f, 0.0f));
        }
        bottomSheet.setCustomView(r9);
        bottomSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$open$0(String str, int i, BottomSheet bottomSheet, Theme.ResourcesProvider resourcesProvider) {
        AndroidUtilities.addToClipboard(str);
        if (i == 1) {
            BulletinFactory.of(bottomSheet.getContainer(), resourcesProvider).createCopyBulletin(LocaleController.getString(R.string.PhoneCopied)).show();
        } else {
            BulletinFactory.of(bottomSheet.getContainer(), resourcesProvider).createCopyLinkBulletin().show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$open$1(Context context, TL_fragment$TL_collectibleInfo tL_fragment$TL_collectibleInfo, View view) {
        Browser.openUrl(context, tL_fragment$TL_collectibleInfo.url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$open$2(Runnable runnable, BottomSheet bottomSheet, View view) {
        runnable.run();
        bottomSheet.dismiss();
    }
}