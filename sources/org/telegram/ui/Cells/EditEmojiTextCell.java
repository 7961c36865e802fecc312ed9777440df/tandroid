package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedColor;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.TypefaceSpan;

/* loaded from: classes4.dex */
public abstract class EditEmojiTextCell extends FrameLayout {
    private boolean allowEntities;
    public boolean autofocused;
    public final EditTextEmoji editTextEmoji;
    private boolean focused;
    private boolean ignoreEditText;
    final AnimatedTextView.AnimatedTextDrawable limit;
    final AnimatedColor limitColor;
    private int limitCount;
    private int maxLength;
    private boolean needDivider;
    private boolean showLimitWhenEmpty;
    private boolean showLimitWhenFocused;
    private int showLimitWhenNear;

    public EditEmojiTextCell(Context context, SizeNotifierFrameLayout sizeNotifierFrameLayout, String str, final boolean z, final int i, int i2, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        float f;
        this.showLimitWhenNear = -1;
        this.allowEntities = true;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.limit = animatedTextDrawable;
        animatedTextDrawable.setAnimationProperties(0.2f, 0L, 160L, CubicBezierInterpolator.EASE_OUT_QUINT);
        animatedTextDrawable.setTextSize(AndroidUtilities.dp(15.33f));
        animatedTextDrawable.setGravity(5);
        this.maxLength = i;
        EditTextEmoji editTextEmoji = new EditTextEmoji(context, sizeNotifierFrameLayout, null, i2, true) { // from class: org.telegram.ui.Cells.EditEmojiTextCell.2
            @Override // org.telegram.ui.Components.EditTextEmoji
            protected boolean allowEntities() {
                return EditEmojiTextCell.this.allowEntities && super.allowEntities();
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            public int emojiCacheType() {
                return EditEmojiTextCell.this.emojiCacheType();
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected void extendActionMode(ActionMode actionMode, Menu menu) {
                int i3 = R.id.menu_bold;
                if (menu.findItem(i3) != null) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    menu.removeItem(android.R.id.shareText);
                }
                int i4 = R.id.menu_groupbolditalic;
                menu.add(i4, R.id.menu_spoiler, 6, LocaleController.getString(R.string.Spoiler));
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.Bold));
                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableStringBuilder.length(), 33);
                menu.add(i4, i3, 7, spannableStringBuilder);
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(LocaleController.getString(R.string.Italic));
                spannableStringBuilder2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM_ITALIC)), 0, spannableStringBuilder2.length(), 33);
                menu.add(i4, R.id.menu_italic, 8, spannableStringBuilder2);
                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(LocaleController.getString(R.string.Strike));
                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                textStyleRun.flags = 8 | textStyleRun.flags;
                spannableStringBuilder3.setSpan(new TextStyleSpan(textStyleRun), 0, spannableStringBuilder3.length(), 33);
                menu.add(i4, R.id.menu_strike, 9, spannableStringBuilder3);
                menu.add(i4, R.id.menu_regular, 10, LocaleController.getString(R.string.Regular));
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                canvas.save();
                canvas.clipRect(getScrollX() + getPaddingLeft(), 0, (getScrollX() + getWidth()) - getPaddingRight(), getHeight());
                super.onDraw(canvas);
                canvas.restore();
                EditEmojiTextCell editEmojiTextCell = EditEmojiTextCell.this;
                AnimatedColor animatedColor = editEmojiTextCell.limitColor;
                if (animatedColor != null) {
                    editEmojiTextCell.limit.setTextColor(animatedColor.set(Theme.getColor(editEmojiTextCell.limitCount <= 0 ? Theme.key_text_RedRegular : Theme.key_dialogSearchHint, resourcesProvider)));
                }
                int min = Math.min(AndroidUtilities.dp(48.0f), getHeight());
                float f2 = z ? 0.0f : -AndroidUtilities.dp(1.0f);
                EditEmojiTextCell.this.limit.setBounds(getScrollX(), (getHeight() + f2) - min, (getScrollX() + getWidth()) - AndroidUtilities.dp((z ? 0 : 44) + 12), f2 + getHeight());
                EditEmojiTextCell.this.limit.draw(canvas);
            }

            @Override // android.view.View
            protected boolean verifyDrawable(Drawable drawable) {
                return drawable == EditEmojiTextCell.this.limit || super.verifyDrawable(drawable);
            }
        };
        this.editTextEmoji = editTextEmoji;
        final EditTextCaption editText = editTextEmoji.getEditText();
        editText.setDelegate(new EditTextCaption.EditTextCaptionDelegate() { // from class: org.telegram.ui.Cells.EditEmojiTextCell.3
            @Override // org.telegram.ui.Components.EditTextCaption.EditTextCaptionDelegate
            public void onSpansChanged() {
                EditEmojiTextCell.this.onTextChanged(editText.getText());
            }
        });
        editTextEmoji.setWillNotDraw(false);
        this.limitColor = new AnimatedColor(editTextEmoji);
        animatedTextDrawable.setCallback(editTextEmoji);
        editText.setTextSize(1, 17.0f);
        editText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText, resourcesProvider));
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        editText.setTextColor(Theme.getColor(i3, resourcesProvider));
        editText.setBackground(null);
        if (z) {
            editText.setMaxLines(5);
            editText.setSingleLine(false);
        } else {
            editText.setMaxLines(1);
            editText.setSingleLine(true);
        }
        int paddingLeft = editText.getPaddingLeft();
        int paddingTop = editText.getPaddingTop();
        if (i2 == 4) {
            f = 0.0f;
        } else {
            f = (i > 0 ? 42 : 0) + 21;
        }
        editText.setPadding(paddingLeft, paddingTop, AndroidUtilities.dp(f), editText.getPaddingBottom());
        editText.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        editText.setInputType((z ? 131072 : 0) | 573441);
        editText.setRawInputType(573441);
        editText.setHint(str);
        editText.setCursorColor(Theme.getColor(i3, resourcesProvider));
        editText.setCursorSize(AndroidUtilities.dp(19.0f));
        editText.setCursorWidth(1.5f);
        editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Cells.EditEmojiTextCell.4
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (!EditEmojiTextCell.this.ignoreEditText) {
                    if (i > 0 && editable != null && editable.length() > i) {
                        EditEmojiTextCell.this.ignoreEditText = true;
                        editText.setText(editable.subSequence(0, i));
                        EditTextCaption editTextCaption = editText;
                        editTextCaption.setSelection(editTextCaption.length());
                        EditEmojiTextCell.this.ignoreEditText = false;
                    }
                    EditEmojiTextCell.this.onTextChanged(editable);
                }
                if (z) {
                    while (true) {
                        int indexOf = editable.toString().indexOf("\n");
                        if (indexOf < 0) {
                            break;
                        } else {
                            editable.delete(indexOf, indexOf + 1);
                        }
                    }
                }
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = EditEmojiTextCell.this.limit;
                if (animatedTextDrawable2 == null || i <= 0) {
                    return;
                }
                animatedTextDrawable2.cancelAnimation();
                EditEmojiTextCell.this.updateLimitText();
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                if (EditEmojiTextCell.this.ignoreEditText) {
                    return;
                }
                EditEmojiTextCell.this.autofocused = false;
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.Cells.EditEmojiTextCell.5
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z2) {
                EditEmojiTextCell.this.focused = z2;
                if (EditEmojiTextCell.this.showLimitWhenFocused) {
                    EditEmojiTextCell.this.updateLimitText();
                }
                EditEmojiTextCell.this.onFocusChanged(z2);
            }
        });
        addView(editTextEmoji, LayoutHelper.createFrame(-1, -1, 48));
        updateLimitText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideKeyboardOnEnter$0() {
        AndroidUtilities.hideKeyboard(this.editTextEmoji.getEditText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLimitText() {
        int i;
        EditTextEmoji editTextEmoji = this.editTextEmoji;
        if (editTextEmoji == null || editTextEmoji.getEditText() == null) {
            return;
        }
        this.limitCount = this.maxLength - getText().length();
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.limit;
        String str = "";
        if ((!TextUtils.isEmpty(getText()) || this.showLimitWhenEmpty) && ((!this.showLimitWhenFocused || (this.focused && !this.autofocused)) && ((i = this.showLimitWhenNear) == -1 || this.limitCount <= i))) {
            str = "" + this.limitCount;
        }
        animatedTextDrawable.setText(str);
    }

    public int emojiCacheType() {
        return AnimatedEmojiDrawable.getCacheTypeForEnterView();
    }

    public CharSequence getText() {
        return this.editTextEmoji.getText();
    }

    public void hideKeyboardOnEnter() {
        whenHitEnter(new Runnable() { // from class: org.telegram.ui.Cells.EditEmojiTextCell$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                EditEmojiTextCell.this.lambda$hideKeyboardOnEnter$0();
            }
        });
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(22.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(22.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    protected void onFocusChanged(boolean z) {
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
    }

    protected void onTextChanged(CharSequence charSequence) {
    }

    public EditEmojiTextCell setAllowEntities(boolean z) {
        this.allowEntities = z;
        return this;
    }

    public void setDivider(boolean z) {
        this.needDivider = z;
        setWillNotDraw(!z);
    }

    public void setEmojiViewCacheType(int i) {
        this.editTextEmoji.setEmojiViewCacheType(i);
    }

    public void setShowLimitOnFocus(boolean z) {
        this.showLimitWhenFocused = z;
    }

    public void setShowLimitWhenEmpty(boolean z) {
        this.showLimitWhenEmpty = z;
        if (z) {
            updateLimitText();
        }
    }

    public void setShowLimitWhenNear(int i) {
        this.showLimitWhenNear = i;
        updateLimitText();
    }

    public void setText(CharSequence charSequence) {
        this.ignoreEditText = true;
        this.editTextEmoji.setText(charSequence);
        EditTextEmoji editTextEmoji = this.editTextEmoji;
        editTextEmoji.setSelection(editTextEmoji.getText().length());
        this.ignoreEditText = false;
    }

    public void whenHitEnter(final Runnable runnable) {
        this.editTextEmoji.getEditText().setImeOptions(6);
        this.editTextEmoji.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Cells.EditEmojiTextCell.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return false;
                }
                runnable.run();
                return true;
            }
        });
    }
}
