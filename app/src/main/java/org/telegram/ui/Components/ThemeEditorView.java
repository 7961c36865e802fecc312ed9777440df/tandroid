package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.TextColorThemeCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.WallpaperUpdater;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class ThemeEditorView {
    @SuppressLint({"StaticFieldLeak"})
    private static volatile ThemeEditorView Instance;
    private ArrayList<ThemeDescription> currentThemeDesription;
    private int currentThemeDesriptionPosition;
    private DecelerateInterpolator decelerateInterpolator;
    private EditorAlert editorAlert;
    private Activity parentActivity;
    private SharedPreferences preferences;
    private Theme.ThemeInfo themeInfo;
    private WallpaperUpdater wallpaperUpdater;
    private WindowManager.LayoutParams windowLayoutParams;
    private WindowManager windowManager;
    private FrameLayout windowView;
    private final int editorWidth = AndroidUtilities.dp(54.0f);
    private final int editorHeight = AndroidUtilities.dp(54.0f);

    public static ThemeEditorView getInstance() {
        return Instance;
    }

    public void destroy() {
        FrameLayout frameLayout;
        this.wallpaperUpdater.cleanup();
        if (this.parentActivity == null || (frameLayout = this.windowView) == null) {
            return;
        }
        try {
            this.windowManager.removeViewImmediate(frameLayout);
            this.windowView = null;
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            EditorAlert editorAlert = this.editorAlert;
            if (editorAlert != null) {
                editorAlert.dismiss();
                this.editorAlert = null;
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        this.parentActivity = null;
        Instance = null;
    }

    /* loaded from: classes3.dex */
    public class EditorAlert extends BottomSheet {
        private boolean animationInProgress;
        private FrameLayout bottomLayout;
        private FrameLayout bottomSaveLayout;
        private AnimatorSet colorChangeAnimation;
        private ColorPicker colorPicker;
        private FrameLayout frameLayout;
        private boolean ignoreTextChange;
        private LinearLayoutManager layoutManager;
        private ListAdapter listAdapter;
        private RecyclerListView listView;
        private int previousScrollPosition;
        private int scrollOffsetY;
        private SearchAdapter searchAdapter;
        private EmptyTextProgressView searchEmptyView;
        private SearchField searchField;
        private View[] shadow = new View[2];
        private AnimatorSet[] shadowAnimation = new AnimatorSet[2];
        private Drawable shadowDrawable;
        private boolean startedColorChange;
        private int topBeforeSwitch;

        @Override // org.telegram.ui.ActionBar.BottomSheet
        protected boolean canDismissWithSwipe() {
            return false;
        }

        /* loaded from: classes3.dex */
        public class SearchField extends FrameLayout {
            private ImageView clearSearchImageView;
            private EditTextBoldCursor searchEditText;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public SearchField(Context context) {
                super(context);
                EditorAlert.this = r12;
                View view = new View(context);
                view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), -854795));
                addView(view, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 11.0f, 14.0f, 0.0f));
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageResource(2131166148);
                imageView.setColorFilter(new PorterDuffColorFilter(-6182737, PorterDuff.Mode.MULTIPLY));
                addView(imageView, LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 11.0f, 0.0f, 0.0f));
                ImageView imageView2 = new ImageView(context);
                this.clearSearchImageView = imageView2;
                imageView2.setScaleType(ImageView.ScaleType.CENTER);
                ImageView imageView3 = this.clearSearchImageView;
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, r12);
                imageView3.setImageDrawable(anonymousClass1);
                anonymousClass1.setSide(AndroidUtilities.dp(7.0f));
                this.clearSearchImageView.setScaleX(0.1f);
                this.clearSearchImageView.setScaleY(0.1f);
                this.clearSearchImageView.setAlpha(0.0f);
                addView(this.clearSearchImageView, LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 11.0f, 14.0f, 0.0f));
                this.clearSearchImageView.setOnClickListener(new ThemeEditorView$EditorAlert$SearchField$$ExternalSyntheticLambda0(this));
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, r12);
                this.searchEditText = anonymousClass2;
                anonymousClass2.setTextSize(1, 16.0f);
                this.searchEditText.setHintTextColor(-6774617);
                this.searchEditText.setTextColor(-14540254);
                this.searchEditText.setBackgroundDrawable(null);
                this.searchEditText.setPadding(0, 0, 0, 0);
                this.searchEditText.setMaxLines(1);
                this.searchEditText.setLines(1);
                this.searchEditText.setSingleLine(true);
                this.searchEditText.setImeOptions(268435459);
                this.searchEditText.setHint(LocaleController.getString("Search", 2131628092));
                this.searchEditText.setCursorColor(-11491093);
                this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
                this.searchEditText.setCursorWidth(1.5f);
                addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 9.0f, 46.0f, 0.0f));
                this.searchEditText.addTextChangedListener(new AnonymousClass3(r12));
                this.searchEditText.setOnEditorActionListener(new ThemeEditorView$EditorAlert$SearchField$$ExternalSyntheticLambda1(this));
            }

            /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$SearchField$1 */
            /* loaded from: classes3.dex */
            public class AnonymousClass1 extends CloseProgressDrawable2 {
                @Override // org.telegram.ui.Components.CloseProgressDrawable2
                public int getCurrentColor() {
                    return -6182737;
                }

                AnonymousClass1(SearchField searchField, EditorAlert editorAlert) {
                }
            }

            public /* synthetic */ void lambda$new$0(View view) {
                this.searchEditText.setText("");
                AndroidUtilities.showKeyboard(this.searchEditText);
            }

            /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$SearchField$2 */
            /* loaded from: classes3.dex */
            public class AnonymousClass2 extends EditTextBoldCursor {
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                AnonymousClass2(Context context, EditorAlert editorAlert) {
                    super(context);
                    SearchField.this = r1;
                }

                @Override // org.telegram.ui.Components.EditTextEffects, android.view.View
                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    MotionEvent obtain = MotionEvent.obtain(motionEvent);
                    obtain.setLocation(obtain.getRawX(), obtain.getRawY() - ((BottomSheet) EditorAlert.this).containerView.getTranslationY());
                    EditorAlert.this.listView.dispatchTouchEvent(obtain);
                    obtain.recycle();
                    return super.dispatchTouchEvent(motionEvent);
                }
            }

            /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$SearchField$3 */
            /* loaded from: classes3.dex */
            public class AnonymousClass3 implements TextWatcher {
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                AnonymousClass3(EditorAlert editorAlert) {
                    SearchField.this = r1;
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    boolean z = true;
                    boolean z2 = SearchField.this.searchEditText.length() > 0;
                    float f = 0.0f;
                    if (SearchField.this.clearSearchImageView.getAlpha() == 0.0f) {
                        z = false;
                    }
                    if (z2 != z) {
                        ViewPropertyAnimator animate = SearchField.this.clearSearchImageView.animate();
                        float f2 = 1.0f;
                        if (z2) {
                            f = 1.0f;
                        }
                        ViewPropertyAnimator scaleX = animate.alpha(f).setDuration(150L).scaleX(z2 ? 1.0f : 0.1f);
                        if (!z2) {
                            f2 = 0.1f;
                        }
                        scaleX.scaleY(f2).start();
                    }
                    String obj = SearchField.this.searchEditText.getText().toString();
                    if (obj.length() != 0) {
                        if (EditorAlert.this.searchEmptyView != null) {
                            EditorAlert.this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131626858));
                        }
                    } else if (EditorAlert.this.listView.getAdapter() != EditorAlert.this.listAdapter) {
                        int currentTop = EditorAlert.this.getCurrentTop();
                        EditorAlert.this.searchEmptyView.setText(LocaleController.getString("NoChats", 2131626811));
                        EditorAlert.this.searchEmptyView.showTextView();
                        EditorAlert.this.listView.setAdapter(EditorAlert.this.listAdapter);
                        EditorAlert.this.listAdapter.notifyDataSetChanged();
                        if (currentTop > 0) {
                            EditorAlert.this.layoutManager.scrollToPositionWithOffset(0, -currentTop);
                        }
                    }
                    if (EditorAlert.this.searchAdapter != null) {
                        EditorAlert.this.searchAdapter.searchDialogs(obj);
                    }
                }
            }

            public /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null) {
                    if ((keyEvent.getAction() != 1 || keyEvent.getKeyCode() != 84) && (keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 66)) {
                        return false;
                    }
                    AndroidUtilities.hideKeyboard(this.searchEditText);
                    return false;
                }
                return false;
            }

            public void showKeyboard() {
                this.searchEditText.requestFocus();
                AndroidUtilities.showKeyboard(this.searchEditText);
            }

            @Override // android.view.ViewGroup, android.view.ViewParent
            public void requestDisallowInterceptTouchEvent(boolean z) {
                super.requestDisallowInterceptTouchEvent(z);
            }
        }

        /* loaded from: classes3.dex */
        public class ColorPicker extends FrameLayout {
            private LinearGradient alphaGradient;
            private boolean alphaPressed;
            private Drawable circleDrawable;
            private boolean circlePressed;
            private LinearGradient colorGradient;
            private boolean colorPressed;
            private Bitmap colorWheelBitmap;
            private Paint colorWheelPaint;
            private int colorWheelRadius;
            private LinearLayout linearLayout;
            private Paint valueSliderPaint;
            private final int paramValueSliderWidth = AndroidUtilities.dp(20.0f);
            private EditTextBoldCursor[] colorEditText = new EditTextBoldCursor[4];
            private float[] colorHSV = {0.0f, 0.0f, 1.0f};
            private float alpha = 1.0f;
            private float[] hsvTemp = new float[3];
            private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
            private Paint circlePaint = new Paint(1);

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public ColorPicker(Context context) {
                super(context);
                EditorAlert.this = r18;
                setWillNotDraw(false);
                this.circleDrawable = context.getResources().getDrawable(2131165570).mutate();
                Paint paint = new Paint();
                this.colorWheelPaint = paint;
                paint.setAntiAlias(true);
                this.colorWheelPaint.setDither(true);
                Paint paint2 = new Paint();
                this.valueSliderPaint = paint2;
                paint2.setAntiAlias(true);
                this.valueSliderPaint.setDither(true);
                LinearLayout linearLayout = new LinearLayout(context);
                this.linearLayout = linearLayout;
                linearLayout.setOrientation(0);
                addView(this.linearLayout, LayoutHelper.createFrame(-2, -2, 49));
                int i = 0;
                while (i < 4) {
                    this.colorEditText[i] = new EditTextBoldCursor(context);
                    this.colorEditText[i].setInputType(2);
                    this.colorEditText[i].setTextColor(-14606047);
                    this.colorEditText[i].setCursorColor(-14606047);
                    this.colorEditText[i].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.colorEditText[i].setCursorWidth(1.5f);
                    this.colorEditText[i].setTextSize(1, 18.0f);
                    this.colorEditText[i].setBackground(null);
                    this.colorEditText[i].setLineColors(Theme.getColor("dialogInputField"), Theme.getColor("dialogInputFieldActivated"), Theme.getColor("dialogTextRed2"));
                    this.colorEditText[i].setMaxLines(1);
                    this.colorEditText[i].setTag(Integer.valueOf(i));
                    this.colorEditText[i].setGravity(17);
                    if (i == 0) {
                        this.colorEditText[i].setHint("red");
                    } else if (i == 1) {
                        this.colorEditText[i].setHint("green");
                    } else if (i == 2) {
                        this.colorEditText[i].setHint("blue");
                    } else if (i == 3) {
                        this.colorEditText[i].setHint("alpha");
                    }
                    this.colorEditText[i].setImeOptions((i == 3 ? 6 : 5) | 268435456);
                    this.colorEditText[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                    this.linearLayout.addView(this.colorEditText[i], LayoutHelper.createLinear(55, 36, 0.0f, 0.0f, i != 3 ? 16.0f : 0.0f, 0.0f));
                    this.colorEditText[i].addTextChangedListener(new AnonymousClass1(r18, i));
                    this.colorEditText[i].setOnEditorActionListener(ThemeEditorView$EditorAlert$ColorPicker$$ExternalSyntheticLambda0.INSTANCE);
                    i++;
                }
            }

            /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$ColorPicker$1 */
            /* loaded from: classes3.dex */
            public class AnonymousClass1 implements TextWatcher {
                final /* synthetic */ int val$num;

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                AnonymousClass1(EditorAlert editorAlert, int i) {
                    ColorPicker.this = r1;
                    this.val$num = i;
                }

                /* JADX WARN: Removed duplicated region for block: B:24:0x00df A[LOOP:0: B:22:0x00cf->B:24:0x00df, LOOP_END] */
                @Override // android.text.TextWatcher
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void afterTextChanged(Editable editable) {
                    int i;
                    int i2;
                    int i3;
                    if (EditorAlert.this.ignoreTextChange) {
                        return;
                    }
                    EditorAlert.this.ignoreTextChange = true;
                    int intValue = Utilities.parseInt((CharSequence) editable.toString()).intValue();
                    if (intValue < 0) {
                        EditTextBoldCursor editTextBoldCursor = ColorPicker.this.colorEditText[this.val$num];
                        editTextBoldCursor.setText("0");
                        ColorPicker.this.colorEditText[this.val$num].setSelection(ColorPicker.this.colorEditText[this.val$num].length());
                        intValue = 0;
                    } else if (intValue > 255) {
                        EditTextBoldCursor editTextBoldCursor2 = ColorPicker.this.colorEditText[this.val$num];
                        editTextBoldCursor2.setText("255");
                        ColorPicker.this.colorEditText[this.val$num].setSelection(ColorPicker.this.colorEditText[this.val$num].length());
                        intValue = 255;
                    }
                    int color = ColorPicker.this.getColor();
                    int i4 = this.val$num;
                    if (i4 == 2) {
                        i2 = color & (-256);
                        i3 = intValue & 255;
                    } else if (i4 == 1) {
                        i2 = color & (-65281);
                        i3 = (intValue & 255) << 8;
                    } else if (i4 != 0) {
                        if (i4 == 3) {
                            i2 = color & 16777215;
                            i3 = (intValue & 255) << 24;
                        }
                        ColorPicker.this.setColor(color);
                        for (i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); i++) {
                            ((ThemeDescription) ThemeEditorView.this.currentThemeDesription.get(i)).setColor(ColorPicker.this.getColor(), false);
                        }
                        EditorAlert.this.ignoreTextChange = false;
                    } else {
                        i2 = color & (-16711681);
                        i3 = (intValue & 255) << 16;
                    }
                    color = i2 | i3;
                    ColorPicker.this.setColor(color);
                    while (i < ThemeEditorView.this.currentThemeDesription.size()) {
                    }
                    EditorAlert.this.ignoreTextChange = false;
                }
            }

            public static /* synthetic */ boolean lambda$new$0(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 6) {
                    AndroidUtilities.hideKeyboard(textView);
                    return true;
                }
                return false;
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                int min = Math.min(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
                measureChild(this.linearLayout, i, i2);
                setMeasuredDimension(min, min);
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                float f;
                int width = (getWidth() / 2) - (this.paramValueSliderWidth * 2);
                int height = (getHeight() / 2) - AndroidUtilities.dp(8.0f);
                Bitmap bitmap = this.colorWheelBitmap;
                int i = this.colorWheelRadius;
                canvas.drawBitmap(bitmap, width - i, height - i, (Paint) null);
                double radians = (float) Math.toRadians(this.colorHSV[0]);
                double d = this.colorHSV[1];
                Double.isNaN(d);
                double d2 = (-Math.cos(radians)) * d;
                double d3 = this.colorWheelRadius;
                Double.isNaN(d3);
                float[] fArr = this.colorHSV;
                double d4 = fArr[1];
                Double.isNaN(d4);
                double d5 = (-Math.sin(radians)) * d4;
                double d6 = this.colorWheelRadius;
                Double.isNaN(d6);
                float[] fArr2 = this.hsvTemp;
                fArr2[0] = fArr[0];
                fArr2[1] = fArr[1];
                fArr2[2] = 1.0f;
                drawPointerArrow(canvas, ((int) (d2 * d3)) + width, ((int) (d5 * d6)) + height, Color.HSVToColor(fArr2));
                int i2 = this.colorWheelRadius;
                int i3 = width + i2 + this.paramValueSliderWidth;
                int i4 = height - i2;
                int dp = AndroidUtilities.dp(9.0f);
                int i5 = this.colorWheelRadius * 2;
                if (this.colorGradient == null) {
                    this.colorGradient = new LinearGradient(i3, i4, i3 + dp, i4 + i5, new int[]{-16777216, Color.HSVToColor(this.hsvTemp)}, (float[]) null, Shader.TileMode.CLAMP);
                }
                this.valueSliderPaint.setShader(this.colorGradient);
                float f2 = i4;
                float f3 = i4 + i5;
                canvas.drawRect(i3, f2, i3 + dp, f3, this.valueSliderPaint);
                int i6 = dp / 2;
                float[] fArr3 = this.colorHSV;
                float f4 = i5;
                drawPointerArrow(canvas, i3 + i6, (int) ((fArr3[2] * f4) + f2), Color.HSVToColor(fArr3));
                int i7 = i3 + (this.paramValueSliderWidth * 2);
                if (this.alphaGradient == null) {
                    int HSVToColor = Color.HSVToColor(this.hsvTemp);
                    f = f3;
                    this.alphaGradient = new LinearGradient(i7, f2, i7 + dp, f, new int[]{HSVToColor, HSVToColor & 16777215}, (float[]) null, Shader.TileMode.CLAMP);
                } else {
                    f = f3;
                }
                this.valueSliderPaint.setShader(this.alphaGradient);
                canvas.drawRect(i7, f2, dp + i7, f, this.valueSliderPaint);
                drawPointerArrow(canvas, i7 + i6, (int) (f2 + ((1.0f - this.alpha) * f4)), (Color.HSVToColor(this.colorHSV) & 16777215) | (((int) (this.alpha * 255.0f)) << 24));
            }

            private void drawPointerArrow(Canvas canvas, int i, int i2, int i3) {
                int dp = AndroidUtilities.dp(13.0f);
                this.circleDrawable.setBounds(i - dp, i2 - dp, i + dp, dp + i2);
                this.circleDrawable.draw(canvas);
                this.circlePaint.setColor(-1);
                float f = i;
                float f2 = i2;
                canvas.drawCircle(f, f2, AndroidUtilities.dp(11.0f), this.circlePaint);
                this.circlePaint.setColor(i3);
                canvas.drawCircle(f, f2, AndroidUtilities.dp(9.0f), this.circlePaint);
            }

            @Override // android.view.View
            protected void onSizeChanged(int i, int i2, int i3, int i4) {
                int max = Math.max(1, ((i / 2) - (this.paramValueSliderWidth * 2)) - AndroidUtilities.dp(20.0f));
                this.colorWheelRadius = max;
                this.colorWheelBitmap = createColorWheelBitmap(max * 2, max * 2);
                this.colorGradient = null;
                this.alphaGradient = null;
            }

            private Bitmap createColorWheelBitmap(int i, int i2) {
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                int[] iArr = new int[13];
                float[] fArr = {0.0f, 1.0f, 1.0f};
                for (int i3 = 0; i3 < 13; i3++) {
                    fArr[0] = ((i3 * 30) + 180) % 360;
                    iArr[i3] = Color.HSVToColor(fArr);
                }
                iArr[12] = iArr[0];
                float f = i / 2;
                float f2 = i2 / 2;
                this.colorWheelPaint.setShader(new ComposeShader(new SweepGradient(f, f2, iArr, (float[]) null), new RadialGradient(f, f2, this.colorWheelRadius, -1, 16777215, Shader.TileMode.CLAMP), PorterDuff.Mode.SRC_OVER));
                new Canvas(createBitmap).drawCircle(f, f2, this.colorWheelRadius, this.colorWheelPaint);
                return createBitmap;
            }

            private void startColorChange(boolean z) {
                if (EditorAlert.this.startedColorChange == z) {
                    return;
                }
                if (EditorAlert.this.colorChangeAnimation != null) {
                    EditorAlert.this.colorChangeAnimation.cancel();
                }
                EditorAlert.this.startedColorChange = z;
                EditorAlert.this.colorChangeAnimation = new AnimatorSet();
                AnimatorSet animatorSet = EditorAlert.this.colorChangeAnimation;
                Animator[] animatorArr = new Animator[2];
                ColorDrawable colorDrawable = ((BottomSheet) EditorAlert.this).backDrawable;
                Property<ColorDrawable, Integer> property = AnimationProperties.COLOR_DRAWABLE_ALPHA;
                int[] iArr = new int[1];
                iArr[0] = z ? 0 : 51;
                animatorArr[0] = ObjectAnimator.ofInt(colorDrawable, property, iArr);
                ViewGroup viewGroup = ((BottomSheet) EditorAlert.this).containerView;
                Property property2 = View.ALPHA;
                float[] fArr = new float[1];
                fArr[0] = z ? 0.2f : 1.0f;
                animatorArr[1] = ObjectAnimator.ofFloat(viewGroup, property2, fArr);
                animatorSet.playTogether(animatorArr);
                EditorAlert.this.colorChangeAnimation.setDuration(150L);
                EditorAlert.this.colorChangeAnimation.setInterpolator(this.decelerateInterpolator);
                EditorAlert.this.colorChangeAnimation.start();
            }

            /* JADX WARN: Code restructure failed: missing block: B:34:0x00bd, code lost:
                if (r5 <= (r8 + r7)) goto L35;
             */
            /* JADX WARN: Code restructure failed: missing block: B:55:0x0102, code lost:
                if (r5 <= (r8 + r7)) goto L56;
             */
            /* JADX WARN: Code restructure failed: missing block: B:5:0x000d, code lost:
                if (r1 != 2) goto L7;
             */
            /* JADX WARN: Removed duplicated region for block: B:44:0x00e4  */
            /* JADX WARN: Removed duplicated region for block: B:58:0x0118  */
            /* JADX WARN: Removed duplicated region for block: B:59:0x011b  */
            /* JADX WARN: Removed duplicated region for block: B:65:0x0127  */
            /* JADX WARN: Removed duplicated region for block: B:72:0x0145  */
            /* JADX WARN: Removed duplicated region for block: B:90:0x01b8  */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean onTouchEvent(MotionEvent motionEvent) {
                int i;
                int i2;
                float f;
                int i3;
                int action = motionEvent.getAction();
                if (action != 0) {
                    if (action == 1) {
                        this.alphaPressed = false;
                        this.colorPressed = false;
                        this.circlePressed = false;
                        startColorChange(false);
                    }
                    return super.onTouchEvent(motionEvent);
                }
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int width = (getWidth() / 2) - (this.paramValueSliderWidth * 2);
                int height = (getHeight() / 2) - AndroidUtilities.dp(8.0f);
                int i4 = x - width;
                int i5 = y - height;
                double sqrt = Math.sqrt((i4 * i4) + (i5 * i5));
                if (this.circlePressed || (!this.alphaPressed && !this.colorPressed && sqrt <= this.colorWheelRadius)) {
                    int i6 = this.colorWheelRadius;
                    if (sqrt > i6) {
                        sqrt = i6;
                    }
                    this.circlePressed = true;
                    this.colorHSV[0] = (float) (Math.toDegrees(Math.atan2(i5, i4)) + 180.0d);
                    float[] fArr = this.colorHSV;
                    double d = this.colorWheelRadius;
                    Double.isNaN(d);
                    fArr[1] = Math.max(0.0f, Math.min(1.0f, (float) (sqrt / d)));
                    this.colorGradient = null;
                    this.alphaGradient = null;
                }
                if (!this.colorPressed) {
                    if (!this.circlePressed && !this.alphaPressed) {
                        int i7 = this.colorWheelRadius;
                        int i8 = this.paramValueSliderWidth;
                        if (x >= width + i7 + i8) {
                            if (x <= width + i7 + (i8 * 2)) {
                                if (y >= height - i7) {
                                }
                            }
                        }
                    }
                    if (!this.alphaPressed) {
                        if (!this.circlePressed && !this.colorPressed) {
                            int i9 = this.colorWheelRadius;
                            int i10 = this.paramValueSliderWidth;
                            if (x >= width + i9 + (i10 * 3)) {
                                if (x <= width + i9 + (i10 * 4)) {
                                    if (y >= height - i9) {
                                    }
                                }
                            }
                        }
                        if (!this.alphaPressed || this.colorPressed || this.circlePressed) {
                            startColorChange(true);
                            int color = getColor();
                            for (i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); i++) {
                                String currentKey = ((ThemeDescription) ThemeEditorView.this.currentThemeDesription.get(i)).getCurrentKey();
                                if ((i == 0 && currentKey.equals("chat_wallpaper")) || currentKey.equals("chat_wallpaper_gradient_to") || currentKey.equals("key_chat_wallpaper_gradient_to2") || currentKey.equals("key_chat_wallpaper_gradient_to3") || currentKey.equals("windowBackgroundWhite") || currentKey.equals("windowBackgroundGray")) {
                                    color |= -16777216;
                                }
                                ((ThemeDescription) ThemeEditorView.this.currentThemeDesription.get(i)).setColor(color, false);
                            }
                            int red = Color.red(color);
                            int green = Color.green(color);
                            int blue = Color.blue(color);
                            int alpha = Color.alpha(color);
                            if (!EditorAlert.this.ignoreTextChange) {
                                EditorAlert.this.ignoreTextChange = true;
                                this.colorEditText[0].setText("" + red);
                                this.colorEditText[1].setText("" + green);
                                this.colorEditText[2].setText("" + blue);
                                this.colorEditText[3].setText("" + alpha);
                                for (int i11 = 0; i11 < 4; i11++) {
                                    EditTextBoldCursor[] editTextBoldCursorArr = this.colorEditText;
                                    editTextBoldCursorArr[i11].setSelection(editTextBoldCursorArr[i11].length());
                                }
                                EditorAlert.this.ignoreTextChange = false;
                            }
                            invalidate();
                        }
                        return true;
                    }
                    f = 1.0f - ((y - (height - i2)) / (this.colorWheelRadius * 2.0f));
                    this.alpha = f;
                    if (f >= 0.0f) {
                        this.alpha = 0.0f;
                    } else if (f > 1.0f) {
                        this.alpha = 1.0f;
                    }
                    this.alphaPressed = true;
                    if (!this.alphaPressed) {
                    }
                    startColorChange(true);
                    int color2 = getColor();
                    while (i < ThemeEditorView.this.currentThemeDesription.size()) {
                    }
                    int red2 = Color.red(color2);
                    int green2 = Color.green(color2);
                    int blue2 = Color.blue(color2);
                    int alpha2 = Color.alpha(color2);
                    if (!EditorAlert.this.ignoreTextChange) {
                    }
                    invalidate();
                    return true;
                }
                float f2 = (y - (height - i3)) / (this.colorWheelRadius * 2.0f);
                if (f2 < 0.0f) {
                    f2 = 0.0f;
                } else if (f2 > 1.0f) {
                    f2 = 1.0f;
                }
                this.colorHSV[2] = f2;
                this.colorPressed = true;
                if (!this.alphaPressed) {
                }
                f = 1.0f - ((y - (height - i2)) / (this.colorWheelRadius * 2.0f));
                this.alpha = f;
                if (f >= 0.0f) {
                }
                this.alphaPressed = true;
                if (!this.alphaPressed) {
                }
                startColorChange(true);
                int color22 = getColor();
                while (i < ThemeEditorView.this.currentThemeDesription.size()) {
                }
                int red22 = Color.red(color22);
                int green22 = Color.green(color22);
                int blue22 = Color.blue(color22);
                int alpha22 = Color.alpha(color22);
                if (!EditorAlert.this.ignoreTextChange) {
                }
                invalidate();
                return true;
            }

            public void setColor(int i) {
                int red = Color.red(i);
                int green = Color.green(i);
                int blue = Color.blue(i);
                int alpha = Color.alpha(i);
                if (!EditorAlert.this.ignoreTextChange) {
                    EditorAlert.this.ignoreTextChange = true;
                    EditTextBoldCursor editTextBoldCursor = this.colorEditText[0];
                    editTextBoldCursor.setText("" + red);
                    EditTextBoldCursor editTextBoldCursor2 = this.colorEditText[1];
                    editTextBoldCursor2.setText("" + green);
                    EditTextBoldCursor editTextBoldCursor3 = this.colorEditText[2];
                    editTextBoldCursor3.setText("" + blue);
                    EditTextBoldCursor editTextBoldCursor4 = this.colorEditText[3];
                    editTextBoldCursor4.setText("" + alpha);
                    for (int i2 = 0; i2 < 4; i2++) {
                        EditTextBoldCursor[] editTextBoldCursorArr = this.colorEditText;
                        editTextBoldCursorArr[i2].setSelection(editTextBoldCursorArr[i2].length());
                    }
                    EditorAlert.this.ignoreTextChange = false;
                }
                this.alphaGradient = null;
                this.colorGradient = null;
                this.alpha = alpha / 255.0f;
                Color.colorToHSV(i, this.colorHSV);
                invalidate();
            }

            public int getColor() {
                return (Color.HSVToColor(this.colorHSV) & 16777215) | (((int) (this.alpha * 255.0f)) << 24);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EditorAlert(Context context, ArrayList<ThemeDescription> arrayList) {
            super(context, true);
            ThemeEditorView.this = r17;
            this.shadowDrawable = context.getResources().getDrawable(2131166138).mutate();
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, r17);
            this.containerView = anonymousClass1;
            anonymousClass1.setWillNotDraw(false);
            ViewGroup viewGroup = this.containerView;
            int i = this.backgroundPaddingLeft;
            viewGroup.setPadding(i, 0, i, 0);
            FrameLayout frameLayout = new FrameLayout(context);
            this.frameLayout = frameLayout;
            frameLayout.setBackgroundColor(-1);
            SearchField searchField = new SearchField(context);
            this.searchField = searchField;
            this.frameLayout.addView(searchField, LayoutHelper.createFrame(-1, -1, 51));
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, r17);
            this.listView = anonymousClass2;
            anonymousClass2.setSelectorDrawableColor(251658240);
            this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
            this.listView.setClipToPadding(false);
            RecyclerListView recyclerListView = this.listView;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            this.layoutManager = linearLayoutManager;
            recyclerListView.setLayoutManager(linearLayoutManager);
            this.listView.setHorizontalScrollBarEnabled(false);
            this.listView.setVerticalScrollBarEnabled(false);
            this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
            RecyclerListView recyclerListView2 = this.listView;
            ListAdapter listAdapter = new ListAdapter(this, context, arrayList);
            this.listAdapter = listAdapter;
            recyclerListView2.setAdapter(listAdapter);
            this.searchAdapter = new SearchAdapter(context);
            this.listView.setGlowColor(-657673);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation(null);
            this.listView.setOnItemClickListener(new ThemeEditorView$EditorAlert$$ExternalSyntheticLambda5(this));
            this.listView.setOnScrollListener(new AnonymousClass3(r17));
            EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
            this.searchEmptyView = emptyTextProgressView;
            emptyTextProgressView.setShowAtCenter(true);
            this.searchEmptyView.showTextView();
            this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131626858));
            this.listView.setEmptyView(this.searchEmptyView);
            this.containerView.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 52.0f, 0.0f, 0.0f));
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
            layoutParams.topMargin = AndroidUtilities.dp(58.0f);
            this.shadow[0] = new View(context);
            this.shadow[0].setBackgroundColor(301989888);
            this.shadow[0].setAlpha(0.0f);
            this.shadow[0].setTag(1);
            this.containerView.addView(this.shadow[0], layoutParams);
            this.containerView.addView(this.frameLayout, LayoutHelper.createFrame(-1, 58, 51));
            ColorPicker colorPicker = new ColorPicker(context);
            this.colorPicker = colorPicker;
            colorPicker.setVisibility(8);
            this.containerView.addView(this.colorPicker, LayoutHelper.createFrame(-1, -1, 1));
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
            layoutParams2.bottomMargin = AndroidUtilities.dp(48.0f);
            this.shadow[1] = new View(context);
            this.shadow[1].setBackgroundColor(301989888);
            this.containerView.addView(this.shadow[1], layoutParams2);
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.bottomSaveLayout = frameLayout2;
            frameLayout2.setBackgroundColor(-1);
            this.containerView.addView(this.bottomSaveLayout, LayoutHelper.createFrame(-1, 48, 83));
            TextView textView = new TextView(context);
            textView.setTextSize(1, 14.0f);
            textView.setTextColor(-15095832);
            textView.setGravity(17);
            textView.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView.setText(LocaleController.getString("CloseEditor", 2131625168).toUpperCase());
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.bottomSaveLayout.addView(textView, LayoutHelper.createFrame(-2, -1, 51));
            textView.setOnClickListener(new ThemeEditorView$EditorAlert$$ExternalSyntheticLambda1(this));
            TextView textView2 = new TextView(context);
            textView2.setTextSize(1, 14.0f);
            textView2.setTextColor(-15095832);
            textView2.setGravity(17);
            textView2.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView2.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView2.setText(LocaleController.getString("SaveTheme", 2131628066).toUpperCase());
            textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.bottomSaveLayout.addView(textView2, LayoutHelper.createFrame(-2, -1, 53));
            textView2.setOnClickListener(new ThemeEditorView$EditorAlert$$ExternalSyntheticLambda3(this));
            FrameLayout frameLayout3 = new FrameLayout(context);
            this.bottomLayout = frameLayout3;
            frameLayout3.setVisibility(8);
            this.bottomLayout.setBackgroundColor(-1);
            this.containerView.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 83));
            TextView textView3 = new TextView(context);
            textView3.setTextSize(1, 14.0f);
            textView3.setTextColor(-15095832);
            textView3.setGravity(17);
            textView3.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView3.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView3.setText(LocaleController.getString("Cancel", 2131624819).toUpperCase());
            textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.bottomLayout.addView(textView3, LayoutHelper.createFrame(-2, -1, 51));
            textView3.setOnClickListener(new ThemeEditorView$EditorAlert$$ExternalSyntheticLambda4(this));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            this.bottomLayout.addView(linearLayout, LayoutHelper.createFrame(-2, -1, 53));
            TextView textView4 = new TextView(context);
            textView4.setTextSize(1, 14.0f);
            textView4.setTextColor(-15095832);
            textView4.setGravity(17);
            textView4.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView4.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView4.setText(LocaleController.getString("Default", 2131625366).toUpperCase());
            textView4.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView(textView4, LayoutHelper.createFrame(-2, -1, 51));
            textView4.setOnClickListener(new ThemeEditorView$EditorAlert$$ExternalSyntheticLambda0(this));
            TextView textView5 = new TextView(context);
            textView5.setTextSize(1, 14.0f);
            textView5.setTextColor(-15095832);
            textView5.setGravity(17);
            textView5.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView5.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView5.setText(LocaleController.getString("Save", 2131628060).toUpperCase());
            textView5.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView(textView5, LayoutHelper.createFrame(-2, -1, 51));
            textView5.setOnClickListener(new ThemeEditorView$EditorAlert$$ExternalSyntheticLambda2(this));
        }

        /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends FrameLayout {
            private boolean ignoreLayout = false;
            private RectF rect1 = new RectF();

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, ThemeEditorView themeEditorView) {
                super(context);
                EditorAlert.this = r1;
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && EditorAlert.this.scrollOffsetY != 0 && motionEvent.getY() < EditorAlert.this.scrollOffsetY) {
                    EditorAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !EditorAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                int size = View.MeasureSpec.getSize(i);
                int size2 = View.MeasureSpec.getSize(i2);
                int i3 = Build.VERSION.SDK_INT;
                if (i3 >= 21 && !((BottomSheet) EditorAlert.this).isFullscreen) {
                    this.ignoreLayout = true;
                    setPadding(((BottomSheet) EditorAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) EditorAlert.this).backgroundPaddingLeft, 0);
                    this.ignoreLayout = false;
                }
                int dp = ((size2 - (i3 >= 21 ? AndroidUtilities.statusBarHeight : 0)) + AndroidUtilities.dp(8.0f)) - Math.min(size, size2 - (i3 >= 21 ? AndroidUtilities.statusBarHeight : 0));
                if (EditorAlert.this.listView.getPaddingTop() != dp) {
                    this.ignoreLayout = true;
                    EditorAlert.this.listView.getPaddingTop();
                    EditorAlert.this.listView.setPadding(0, dp, 0, AndroidUtilities.dp(48.0f));
                    if (EditorAlert.this.colorPicker.getVisibility() == 0) {
                        EditorAlert editorAlert = EditorAlert.this;
                        editorAlert.setScrollOffsetY(editorAlert.listView.getPaddingTop());
                        EditorAlert.this.previousScrollPosition = 0;
                    }
                    this.ignoreLayout = false;
                }
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                EditorAlert.this.updateLayout();
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x00b4  */
            /* JADX WARN: Removed duplicated region for block: B:20:0x0154  */
            /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onDraw(Canvas canvas) {
                int i;
                float f;
                int dp = (EditorAlert.this.scrollOffsetY - ((BottomSheet) EditorAlert.this).backgroundPaddingTop) + AndroidUtilities.dp(6.0f);
                int dp2 = (EditorAlert.this.scrollOffsetY - ((BottomSheet) EditorAlert.this).backgroundPaddingTop) - AndroidUtilities.dp(13.0f);
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(30.0f) + ((BottomSheet) EditorAlert.this).backgroundPaddingTop;
                if (((BottomSheet) EditorAlert.this).isFullscreen || Build.VERSION.SDK_INT < 21) {
                    f = 1.0f;
                } else {
                    int i2 = AndroidUtilities.statusBarHeight;
                    dp2 += i2;
                    dp += i2;
                    measuredHeight -= i2;
                    int i3 = ((BottomSheet) EditorAlert.this).backgroundPaddingTop + dp2;
                    int i4 = AndroidUtilities.statusBarHeight;
                    if (i3 < i4 * 2) {
                        int min = Math.min(i4, ((i4 * 2) - dp2) - ((BottomSheet) EditorAlert.this).backgroundPaddingTop);
                        dp2 -= min;
                        measuredHeight += min;
                        f = 1.0f - Math.min(1.0f, (min * 2) / AndroidUtilities.statusBarHeight);
                    } else {
                        f = 1.0f;
                    }
                    int i5 = ((BottomSheet) EditorAlert.this).backgroundPaddingTop + dp2;
                    int i6 = AndroidUtilities.statusBarHeight;
                    if (i5 < i6) {
                        i = Math.min(i6, (i6 - dp2) - ((BottomSheet) EditorAlert.this).backgroundPaddingTop);
                        EditorAlert.this.shadowDrawable.setBounds(0, dp2, getMeasuredWidth(), measuredHeight);
                        EditorAlert.this.shadowDrawable.draw(canvas);
                        if (f != 1.0f) {
                            Theme.dialogs_onlineCirclePaint.setColor(-1);
                            this.rect1.set(((BottomSheet) EditorAlert.this).backgroundPaddingLeft, ((BottomSheet) EditorAlert.this).backgroundPaddingTop + dp2, getMeasuredWidth() - ((BottomSheet) EditorAlert.this).backgroundPaddingLeft, ((BottomSheet) EditorAlert.this).backgroundPaddingTop + dp2 + AndroidUtilities.dp(24.0f));
                            canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                        }
                        int dp3 = AndroidUtilities.dp(36.0f);
                        this.rect1.set((getMeasuredWidth() - dp3) / 2, dp, (getMeasuredWidth() + dp3) / 2, dp + AndroidUtilities.dp(4.0f));
                        Theme.dialogs_onlineCirclePaint.setColor(-1973016);
                        Theme.dialogs_onlineCirclePaint.setAlpha((int) (EditorAlert.this.listView.getAlpha() * 255.0f));
                        canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                        if (i > 0) {
                            return;
                        }
                        Theme.dialogs_onlineCirclePaint.setColor(Color.argb(255, (int) (Color.red(-1) * 0.8f), (int) (Color.green(-1) * 0.8f), (int) (Color.blue(-1) * 0.8f)));
                        canvas.drawRect(((BottomSheet) EditorAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight - i, getMeasuredWidth() - ((BottomSheet) EditorAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
                        return;
                    }
                }
                i = 0;
                EditorAlert.this.shadowDrawable.setBounds(0, dp2, getMeasuredWidth(), measuredHeight);
                EditorAlert.this.shadowDrawable.draw(canvas);
                if (f != 1.0f) {
                }
                int dp32 = AndroidUtilities.dp(36.0f);
                this.rect1.set((getMeasuredWidth() - dp32) / 2, dp, (getMeasuredWidth() + dp32) / 2, dp + AndroidUtilities.dp(4.0f));
                Theme.dialogs_onlineCirclePaint.setColor(-1973016);
                Theme.dialogs_onlineCirclePaint.setAlpha((int) (EditorAlert.this.listView.getAlpha() * 255.0f));
                canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                if (i > 0) {
                }
            }
        }

        /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends RecyclerListView {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(Context context, ThemeEditorView themeEditorView) {
                super(context);
                EditorAlert.this = r1;
            }

            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(float f, float f2) {
                return f2 >= ((float) ((EditorAlert.this.scrollOffsetY + AndroidUtilities.dp(48.0f)) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
            }
        }

        public /* synthetic */ void lambda$new$0(View view, int i) {
            if (i == 0) {
                return;
            }
            RecyclerView.Adapter adapter = this.listView.getAdapter();
            ListAdapter listAdapter = this.listAdapter;
            if (adapter == listAdapter) {
                ThemeEditorView.this.currentThemeDesription = listAdapter.getItem(i - 1);
            } else {
                ThemeEditorView.this.currentThemeDesription = this.searchAdapter.getItem(i - 1);
            }
            ThemeEditorView.this.currentThemeDesriptionPosition = i;
            for (int i2 = 0; i2 < ThemeEditorView.this.currentThemeDesription.size(); i2++) {
                ThemeDescription themeDescription = (ThemeDescription) ThemeEditorView.this.currentThemeDesription.get(i2);
                if (themeDescription.getCurrentKey().equals("chat_wallpaper")) {
                    ThemeEditorView.this.wallpaperUpdater.showAlert(true);
                    return;
                }
                themeDescription.startEditing();
                if (i2 == 0) {
                    this.colorPicker.setColor(themeDescription.getCurrentColor());
                }
            }
            setColorPickerVisible(true);
        }

        /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends RecyclerView.OnScrollListener {
            AnonymousClass3(ThemeEditorView themeEditorView) {
                EditorAlert.this = r1;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                EditorAlert.this.updateLayout();
            }
        }

        public /* synthetic */ void lambda$new$1(View view) {
            dismiss();
        }

        public /* synthetic */ void lambda$new$2(View view) {
            Theme.saveCurrentTheme(ThemeEditorView.this.themeInfo, true, false, false);
            setOnDismissListener(null);
            dismiss();
            ThemeEditorView.this.close();
        }

        public /* synthetic */ void lambda$new$3(View view) {
            for (int i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); i++) {
                ((ThemeDescription) ThemeEditorView.this.currentThemeDesription.get(i)).setPreviousColor();
            }
            setColorPickerVisible(false);
        }

        public /* synthetic */ void lambda$new$4(View view) {
            for (int i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); i++) {
                ((ThemeDescription) ThemeEditorView.this.currentThemeDesription.get(i)).setDefaultColor();
            }
            setColorPickerVisible(false);
        }

        public /* synthetic */ void lambda$new$5(View view) {
            setColorPickerVisible(false);
        }

        private void runShadowAnimation(int i, boolean z) {
            if ((!z || this.shadow[i].getTag() == null) && (z || this.shadow[i].getTag() != null)) {
                return;
            }
            this.shadow[i].setTag(z ? null : 1);
            if (z) {
                this.shadow[i].setVisibility(0);
            }
            AnimatorSet[] animatorSetArr = this.shadowAnimation;
            if (animatorSetArr[i] != null) {
                animatorSetArr[i].cancel();
            }
            this.shadowAnimation[i] = new AnimatorSet();
            AnimatorSet animatorSet = this.shadowAnimation[i];
            Animator[] animatorArr = new Animator[1];
            View view = this.shadow[i];
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z ? 1.0f : 0.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
            animatorSet.playTogether(animatorArr);
            this.shadowAnimation[i].setDuration(150L);
            this.shadowAnimation[i].addListener(new AnonymousClass4(i, z));
            this.shadowAnimation[i].start();
        }

        /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$4 */
        /* loaded from: classes3.dex */
        public class AnonymousClass4 extends AnimatorListenerAdapter {
            final /* synthetic */ int val$num;
            final /* synthetic */ boolean val$show;

            AnonymousClass4(int i, boolean z) {
                EditorAlert.this = r1;
                this.val$num = i;
                this.val$show = z;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (EditorAlert.this.shadowAnimation[this.val$num] == null || !EditorAlert.this.shadowAnimation[this.val$num].equals(animator)) {
                    return;
                }
                if (!this.val$show) {
                    EditorAlert.this.shadow[this.val$num].setVisibility(4);
                }
                EditorAlert.this.shadowAnimation[this.val$num] = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (EditorAlert.this.shadowAnimation[this.val$num] == null || !EditorAlert.this.shadowAnimation[this.val$num].equals(animator)) {
                    return;
                }
                EditorAlert.this.shadowAnimation[this.val$num] = null;
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        public void dismissInternal() {
            super.dismissInternal();
            if (this.searchField.searchEditText.isFocused()) {
                AndroidUtilities.hideKeyboard(this.searchField.searchEditText);
            }
        }

        public void setColorPickerVisible(boolean z) {
            float f = 0.0f;
            if (!z) {
                if (ThemeEditorView.this.parentActivity != null) {
                    ((LaunchActivity) ThemeEditorView.this.parentActivity).rebuildAllFragments(false);
                }
                Theme.saveCurrentTheme(ThemeEditorView.this.themeInfo, false, false, false);
                if (this.listView.getAdapter() == this.listAdapter) {
                    AndroidUtilities.hideKeyboard(getCurrentFocus());
                }
                this.animationInProgress = true;
                this.listView.setVisibility(0);
                this.bottomSaveLayout.setVisibility(0);
                this.searchField.setVisibility(0);
                this.listView.setAlpha(0.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                Animator[] animatorArr = new Animator[8];
                animatorArr[0] = ObjectAnimator.ofFloat(this.colorPicker, View.ALPHA, 0.0f);
                animatorArr[1] = ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, 0.0f);
                animatorArr[2] = ObjectAnimator.ofFloat(this.listView, View.ALPHA, 1.0f);
                animatorArr[3] = ObjectAnimator.ofFloat(this.frameLayout, View.ALPHA, 1.0f);
                View[] viewArr = this.shadow;
                View view = viewArr[0];
                Property property = View.ALPHA;
                float[] fArr = new float[1];
                if (viewArr[0].getTag() == null) {
                    f = 1.0f;
                }
                fArr[0] = f;
                animatorArr[4] = ObjectAnimator.ofFloat(view, property, fArr);
                animatorArr[5] = ObjectAnimator.ofFloat(this.searchEmptyView, View.ALPHA, 1.0f);
                animatorArr[6] = ObjectAnimator.ofFloat(this.bottomSaveLayout, View.ALPHA, 1.0f);
                animatorArr[7] = ObjectAnimator.ofInt(this, "scrollOffsetY", this.previousScrollPosition);
                animatorSet.playTogether(animatorArr);
                animatorSet.setDuration(150L);
                animatorSet.setInterpolator(ThemeEditorView.this.decelerateInterpolator);
                animatorSet.addListener(new AnonymousClass6());
                animatorSet.start();
                this.listView.getAdapter().notifyItemChanged(ThemeEditorView.this.currentThemeDesriptionPosition);
                return;
            }
            this.animationInProgress = true;
            this.colorPicker.setVisibility(0);
            this.bottomLayout.setVisibility(0);
            this.colorPicker.setAlpha(0.0f);
            this.bottomLayout.setAlpha(0.0f);
            this.previousScrollPosition = this.scrollOffsetY;
            AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.colorPicker, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.listView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.frameLayout, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.shadow[0], View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.searchEmptyView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.bottomSaveLayout, View.ALPHA, 0.0f), ObjectAnimator.ofInt(this, "scrollOffsetY", this.listView.getPaddingTop()));
            animatorSet2.setDuration(150L);
            animatorSet2.setInterpolator(ThemeEditorView.this.decelerateInterpolator);
            animatorSet2.addListener(new AnonymousClass5());
            animatorSet2.start();
        }

        /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$5 */
        /* loaded from: classes3.dex */
        public class AnonymousClass5 extends AnimatorListenerAdapter {
            AnonymousClass5() {
                EditorAlert.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                EditorAlert.this.listView.setVisibility(4);
                EditorAlert.this.searchField.setVisibility(4);
                EditorAlert.this.bottomSaveLayout.setVisibility(4);
                EditorAlert.this.animationInProgress = false;
            }
        }

        /* renamed from: org.telegram.ui.Components.ThemeEditorView$EditorAlert$6 */
        /* loaded from: classes3.dex */
        public class AnonymousClass6 extends AnimatorListenerAdapter {
            AnonymousClass6() {
                EditorAlert.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (EditorAlert.this.listView.getAdapter() == EditorAlert.this.searchAdapter) {
                    EditorAlert.this.searchField.showKeyboard();
                }
                EditorAlert.this.colorPicker.setVisibility(8);
                EditorAlert.this.bottomLayout.setVisibility(8);
                EditorAlert.this.animationInProgress = false;
            }
        }

        public int getCurrentTop() {
            if (this.listView.getChildCount() != 0) {
                int i = 0;
                View childAt = this.listView.getChildAt(0);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
                if (holder == null) {
                    return -1000;
                }
                int paddingTop = this.listView.getPaddingTop();
                if (holder.getAdapterPosition() == 0 && childAt.getTop() >= 0) {
                    i = childAt.getTop();
                }
                return paddingTop - i;
            }
            return -1000;
        }

        @SuppressLint({"NewApi"})
        public void updateLayout() {
            int i;
            if (this.listView.getChildCount() <= 0 || this.listView.getVisibility() != 0 || this.animationInProgress) {
                return;
            }
            int i2 = 0;
            View childAt = this.listView.getChildAt(0);
            RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
            if (this.listView.getVisibility() != 0 || this.animationInProgress) {
                i = this.listView.getPaddingTop();
            } else {
                i = childAt.getTop() - AndroidUtilities.dp(8.0f);
            }
            if (i > (-AndroidUtilities.dp(1.0f)) && holder != null && holder.getAdapterPosition() == 0) {
                runShadowAnimation(0, false);
                i2 = i;
            } else {
                runShadowAnimation(0, true);
            }
            if (this.scrollOffsetY == i2) {
                return;
            }
            setScrollOffsetY(i2);
        }

        @Keep
        public int getScrollOffsetY() {
            return this.scrollOffsetY;
        }

        @Keep
        public void setScrollOffsetY(int i) {
            RecyclerListView recyclerListView = this.listView;
            this.scrollOffsetY = i;
            recyclerListView.setTopGlowOffset(i);
            this.frameLayout.setTranslationY(this.scrollOffsetY);
            this.colorPicker.setTranslationY(this.scrollOffsetY);
            this.searchEmptyView.setTranslationY(this.scrollOffsetY);
            this.containerView.invalidate();
        }

        /* loaded from: classes3.dex */
        public class SearchAdapter extends RecyclerListView.SelectionAdapter {
            private Context context;
            private int lastSearchId;
            private String lastSearchText;
            private Runnable searchRunnable;
            private ArrayList<ArrayList<ThemeDescription>> searchResult = new ArrayList<>();
            private ArrayList<CharSequence> searchNames = new ArrayList<>();

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                return i == 0 ? 1 : 0;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            public SearchAdapter(Context context) {
                EditorAlert.this = r1;
                this.context = context;
            }

            public CharSequence generateSearchName(String str, String str2) {
                if (TextUtils.isEmpty(str)) {
                    return "";
                }
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                String trim = str.trim();
                String lowerCase = trim.toLowerCase();
                int i = 0;
                while (true) {
                    int indexOf = lowerCase.indexOf(str2, i);
                    if (indexOf == -1) {
                        break;
                    }
                    int length = str2.length() + indexOf;
                    if (i != 0 && i != indexOf + 1) {
                        spannableStringBuilder.append((CharSequence) trim.substring(i, indexOf));
                    } else if (i == 0 && indexOf != 0) {
                        spannableStringBuilder.append((CharSequence) trim.substring(0, indexOf));
                    }
                    String substring = trim.substring(indexOf, Math.min(trim.length(), length));
                    if (substring.startsWith(" ")) {
                        spannableStringBuilder.append((CharSequence) " ");
                    }
                    String trim2 = substring.trim();
                    int length2 = spannableStringBuilder.length();
                    spannableStringBuilder.append((CharSequence) trim2);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(-11697229), length2, trim2.length() + length2, 33);
                    i = length;
                }
                if (i != -1 && i < trim.length()) {
                    spannableStringBuilder.append((CharSequence) trim.substring(i));
                }
                return spannableStringBuilder;
            }

            /* renamed from: searchDialogsInternal */
            public void lambda$searchDialogs$1(String str, int i) {
                try {
                    String lowerCase = str.trim().toLowerCase();
                    if (lowerCase.length() == 0) {
                        this.lastSearchId = -1;
                        updateSearchResults(new ArrayList<>(), new ArrayList<>(), this.lastSearchId);
                        return;
                    }
                    String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                    if (lowerCase.equals(translitString) || translitString.length() == 0) {
                        translitString = null;
                    }
                    int i2 = (translitString != null ? 1 : 0) + 1;
                    String[] strArr = new String[i2];
                    strArr[0] = lowerCase;
                    if (translitString != null) {
                        strArr[1] = translitString;
                    }
                    ArrayList<ArrayList<ThemeDescription>> arrayList = new ArrayList<>();
                    ArrayList<CharSequence> arrayList2 = new ArrayList<>();
                    int size = EditorAlert.this.listAdapter.items.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        ArrayList<ThemeDescription> arrayList3 = (ArrayList) EditorAlert.this.listAdapter.items.get(i3);
                        String currentKey = arrayList3.get(0).getCurrentKey();
                        String lowerCase2 = currentKey.toLowerCase();
                        int i4 = 0;
                        while (true) {
                            if (i4 < i2) {
                                String str2 = strArr[i4];
                                if (lowerCase2.contains(str2)) {
                                    arrayList.add(arrayList3);
                                    arrayList2.add(generateSearchName(currentKey, str2));
                                    break;
                                }
                                i4++;
                            }
                        }
                    }
                    updateSearchResults(arrayList, arrayList2, i);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }

            private void updateSearchResults(ArrayList<ArrayList<ThemeDescription>> arrayList, ArrayList<CharSequence> arrayList2, int i) {
                AndroidUtilities.runOnUIThread(new ThemeEditorView$EditorAlert$SearchAdapter$$ExternalSyntheticLambda0(this, i, arrayList, arrayList2));
            }

            public /* synthetic */ void lambda$updateSearchResults$0(int i, ArrayList arrayList, ArrayList arrayList2) {
                if (i != this.lastSearchId) {
                    return;
                }
                if (EditorAlert.this.listView.getAdapter() != EditorAlert.this.searchAdapter) {
                    EditorAlert editorAlert = EditorAlert.this;
                    editorAlert.topBeforeSwitch = editorAlert.getCurrentTop();
                    EditorAlert.this.listView.setAdapter(EditorAlert.this.searchAdapter);
                    EditorAlert.this.searchAdapter.notifyDataSetChanged();
                }
                boolean z = true;
                boolean z2 = !this.searchResult.isEmpty() && arrayList.isEmpty();
                if (!this.searchResult.isEmpty() || !arrayList.isEmpty()) {
                    z = false;
                }
                if (z2) {
                    EditorAlert editorAlert2 = EditorAlert.this;
                    editorAlert2.topBeforeSwitch = editorAlert2.getCurrentTop();
                }
                this.searchResult = arrayList;
                this.searchNames = arrayList2;
                notifyDataSetChanged();
                if (!z && !z2 && EditorAlert.this.topBeforeSwitch > 0) {
                    EditorAlert.this.layoutManager.scrollToPositionWithOffset(0, -EditorAlert.this.topBeforeSwitch);
                    EditorAlert.this.topBeforeSwitch = -1000;
                }
                EditorAlert.this.searchEmptyView.showTextView();
            }

            public void searchDialogs(String str) {
                if (str == null || !str.equals(this.lastSearchText)) {
                    this.lastSearchText = str;
                    if (this.searchRunnable != null) {
                        Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                        this.searchRunnable = null;
                    }
                    if (str == null || str.length() == 0) {
                        this.searchResult.clear();
                        EditorAlert editorAlert = EditorAlert.this;
                        editorAlert.topBeforeSwitch = editorAlert.getCurrentTop();
                        this.lastSearchId = -1;
                        notifyDataSetChanged();
                        return;
                    }
                    int i = this.lastSearchId + 1;
                    this.lastSearchId = i;
                    this.searchRunnable = new ThemeEditorView$EditorAlert$SearchAdapter$$ExternalSyntheticLambda1(this, str, i);
                    Utilities.searchQueue.postRunnable(this.searchRunnable, 300L);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                if (this.searchResult.isEmpty()) {
                    return 0;
                }
                return this.searchResult.size() + 1;
            }

            public ArrayList<ThemeDescription> getItem(int i) {
                if (i < 0 || i >= this.searchResult.size()) {
                    return null;
                }
                return this.searchResult.get(i);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view;
                if (i == 0) {
                    view = new TextColorThemeCell(this.context);
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                } else {
                    view = new View(this.context);
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
                }
                return new RecyclerListView.Holder(view);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                if (viewHolder.getItemViewType() == 0) {
                    int i2 = i - 1;
                    int i3 = 0;
                    ThemeDescription themeDescription = this.searchResult.get(i2).get(0);
                    if (!themeDescription.getCurrentKey().equals("chat_wallpaper")) {
                        i3 = themeDescription.getSetColor();
                    }
                    ((TextColorThemeCell) viewHolder.itemView).setTextAndColor(this.searchNames.get(i2), i3);
                }
            }
        }

        /* loaded from: classes3.dex */
        public class ListAdapter extends RecyclerListView.SelectionAdapter {
            private Context context;
            private ArrayList<ArrayList<ThemeDescription>> items = new ArrayList<>();

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                return i == 0 ? 1 : 0;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            public ListAdapter(EditorAlert editorAlert, Context context, ArrayList<ThemeDescription> arrayList) {
                this.context = context;
                HashMap hashMap = new HashMap();
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ThemeDescription themeDescription = arrayList.get(i);
                    String currentKey = themeDescription.getCurrentKey();
                    ArrayList<ThemeDescription> arrayList2 = (ArrayList) hashMap.get(currentKey);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                        hashMap.put(currentKey, arrayList2);
                        this.items.add(arrayList2);
                    }
                    arrayList2.add(themeDescription);
                }
                if (Build.VERSION.SDK_INT < 26 || hashMap.containsKey("windowBackgroundGray")) {
                    return;
                }
                ArrayList<ThemeDescription> arrayList3 = new ArrayList<>();
                arrayList3.add(new ThemeDescription(null, 0, null, null, null, null, "windowBackgroundGray"));
                this.items.add(arrayList3);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                if (this.items.isEmpty()) {
                    return 0;
                }
                return this.items.size() + 1;
            }

            public ArrayList<ThemeDescription> getItem(int i) {
                if (i < 0 || i >= this.items.size()) {
                    return null;
                }
                return this.items.get(i);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view;
                if (i == 0) {
                    view = new TextColorThemeCell(this.context);
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                } else {
                    view = new View(this.context);
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
                }
                return new RecyclerListView.Holder(view);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                if (viewHolder.getItemViewType() == 0) {
                    int i2 = 0;
                    ThemeDescription themeDescription = this.items.get(i - 1).get(0);
                    if (!themeDescription.getCurrentKey().equals("chat_wallpaper")) {
                        i2 = themeDescription.getSetColor();
                    }
                    ((TextColorThemeCell) viewHolder.itemView).setTextAndColor(themeDescription.getTitle(), i2);
                }
            }
        }
    }

    public void show(Activity activity, Theme.ThemeInfo themeInfo) {
        if (Instance != null) {
            Instance.destroy();
        }
        this.themeInfo = themeInfo;
        this.windowView = new AnonymousClass1(activity);
        this.windowManager = (WindowManager) activity.getSystemService("window");
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        this.preferences = sharedPreferences;
        int i = sharedPreferences.getInt("sidex", 1);
        int i2 = this.preferences.getInt("sidey", 0);
        float f = this.preferences.getFloat("px", 0.0f);
        float f2 = this.preferences.getFloat("py", 0.0f);
        try {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            this.windowLayoutParams = layoutParams;
            int i3 = this.editorWidth;
            layoutParams.width = i3;
            layoutParams.height = this.editorHeight;
            layoutParams.x = getSideCoord(true, i, f, i3);
            this.windowLayoutParams.y = getSideCoord(false, i2, f2, this.editorHeight);
            WindowManager.LayoutParams layoutParams2 = this.windowLayoutParams;
            layoutParams2.format = -3;
            layoutParams2.gravity = 51;
            layoutParams2.type = 99;
            layoutParams2.flags = 16777736;
            this.windowManager.addView(this.windowView, layoutParams2);
            this.wallpaperUpdater = new WallpaperUpdater(activity, null, new AnonymousClass2());
            Instance = this;
            this.parentActivity = activity;
            showWithAnimation();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* renamed from: org.telegram.ui.Components.ThemeEditorView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        private boolean dragging;
        private float startX;
        private float startY;

        public static /* synthetic */ void lambda$onTouchEvent$0(DialogInterface dialogInterface) {
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            ThemeEditorView.this = r1;
        }

        /* JADX WARN: Code restructure failed: missing block: B:31:0x0088, code lost:
            if (r6.fragmentsStack.isEmpty() != false) goto L32;
         */
        /* JADX WARN: Removed duplicated region for block: B:34:0x008d  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0093  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            WindowManager.LayoutParams layoutParams;
            WindowManager.LayoutParams layoutParams2;
            ActionBarLayout actionBarLayout;
            ArrayList<ThemeDescription> themeDescriptions;
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            if (motionEvent.getAction() == 0) {
                this.startX = rawX;
                this.startY = rawY;
            } else if (motionEvent.getAction() == 2 && !this.dragging) {
                if (Math.abs(this.startX - rawX) >= AndroidUtilities.getPixelsInCM(0.3f, true) || Math.abs(this.startY - rawY) >= AndroidUtilities.getPixelsInCM(0.3f, false)) {
                    this.dragging = true;
                    this.startX = rawX;
                    this.startY = rawY;
                }
            } else if (motionEvent.getAction() == 1 && !this.dragging && ThemeEditorView.this.editorAlert == null) {
                LaunchActivity launchActivity = (LaunchActivity) ThemeEditorView.this.parentActivity;
                BaseFragment baseFragment = null;
                if (AndroidUtilities.isTablet()) {
                    actionBarLayout = launchActivity.getLayersActionBarLayout();
                    if (actionBarLayout != null && actionBarLayout.fragmentsStack.isEmpty()) {
                        actionBarLayout = null;
                    }
                    if (actionBarLayout == null) {
                        actionBarLayout = launchActivity.getRightActionBarLayout();
                        if (actionBarLayout != null) {
                        }
                    }
                    if (actionBarLayout == null) {
                        actionBarLayout = launchActivity.getActionBarLayout();
                    }
                    if (actionBarLayout != null) {
                        if (!actionBarLayout.fragmentsStack.isEmpty()) {
                            ArrayList<BaseFragment> arrayList = actionBarLayout.fragmentsStack;
                            baseFragment = arrayList.get(arrayList.size() - 1);
                        }
                        if (baseFragment != null && (themeDescriptions = baseFragment.getThemeDescriptions()) != null) {
                            ThemeEditorView themeEditorView = ThemeEditorView.this;
                            ThemeEditorView themeEditorView2 = ThemeEditorView.this;
                            themeEditorView.editorAlert = new EditorAlert(themeEditorView2.parentActivity, themeDescriptions);
                            ThemeEditorView.this.editorAlert.setOnDismissListener(ThemeEditorView$1$$ExternalSyntheticLambda1.INSTANCE);
                            ThemeEditorView.this.editorAlert.setOnDismissListener(new ThemeEditorView$1$$ExternalSyntheticLambda0(this));
                            ThemeEditorView.this.editorAlert.show();
                            ThemeEditorView.this.hide();
                        }
                    }
                }
                actionBarLayout = null;
                if (actionBarLayout == null) {
                }
                if (actionBarLayout != null) {
                }
            }
            if (this.dragging) {
                if (motionEvent.getAction() == 2) {
                    ThemeEditorView.this.windowLayoutParams.x = (int) (layoutParams.x + (rawX - this.startX));
                    ThemeEditorView.this.windowLayoutParams.y = (int) (layoutParams2.y + (rawY - this.startY));
                    int i = ThemeEditorView.this.editorWidth / 2;
                    int i2 = -i;
                    if (ThemeEditorView.this.windowLayoutParams.x < i2) {
                        ThemeEditorView.this.windowLayoutParams.x = i2;
                    } else if (ThemeEditorView.this.windowLayoutParams.x > (AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width) + i) {
                        ThemeEditorView.this.windowLayoutParams.x = (AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width) + i;
                    }
                    float f = 1.0f;
                    if (ThemeEditorView.this.windowLayoutParams.x < 0) {
                        f = 1.0f + ((ThemeEditorView.this.windowLayoutParams.x / i) * 0.5f);
                    } else if (ThemeEditorView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width) {
                        f = 1.0f - ((((ThemeEditorView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x) + ThemeEditorView.this.windowLayoutParams.width) / i) * 0.5f);
                    }
                    if (ThemeEditorView.this.windowView.getAlpha() != f) {
                        ThemeEditorView.this.windowView.setAlpha(f);
                    }
                    if (ThemeEditorView.this.windowLayoutParams.y < 0) {
                        ThemeEditorView.this.windowLayoutParams.y = 0;
                    } else if (ThemeEditorView.this.windowLayoutParams.y > (AndroidUtilities.displaySize.y - ThemeEditorView.this.windowLayoutParams.height) + 0) {
                        ThemeEditorView.this.windowLayoutParams.y = (AndroidUtilities.displaySize.y - ThemeEditorView.this.windowLayoutParams.height) + 0;
                    }
                    ThemeEditorView.this.windowManager.updateViewLayout(ThemeEditorView.this.windowView, ThemeEditorView.this.windowLayoutParams);
                    this.startX = rawX;
                    this.startY = rawY;
                } else if (motionEvent.getAction() == 1) {
                    this.dragging = false;
                    ThemeEditorView.this.animateToBoundsMaybe();
                }
            }
            return true;
        }

        public /* synthetic */ void lambda$onTouchEvent$1(DialogInterface dialogInterface) {
            ThemeEditorView.this.editorAlert = null;
            ThemeEditorView.this.show();
        }
    }

    /* renamed from: org.telegram.ui.Components.ThemeEditorView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements WallpaperUpdater.WallpaperUpdaterDelegate {
        AnonymousClass2() {
            ThemeEditorView.this = r1;
        }

        @Override // org.telegram.ui.Components.WallpaperUpdater.WallpaperUpdaterDelegate
        public void didSelectWallpaper(File file, Bitmap bitmap, boolean z) {
            Theme.setThemeWallpaper(ThemeEditorView.this.themeInfo, bitmap, file);
        }

        @Override // org.telegram.ui.Components.WallpaperUpdater.WallpaperUpdaterDelegate
        public void needOpenColorPicker() {
            for (int i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); i++) {
                ThemeDescription themeDescription = (ThemeDescription) ThemeEditorView.this.currentThemeDesription.get(i);
                themeDescription.startEditing();
                if (i == 0) {
                    ThemeEditorView.this.editorAlert.colorPicker.setColor(themeDescription.getCurrentColor());
                }
            }
            ThemeEditorView.this.editorAlert.setColorPickerVisible(true);
        }
    }

    private void showWithAnimation() {
        this.windowView.setBackgroundResource(2131166181);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.windowView, View.SCALE_X, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.windowView, View.SCALE_Y, 0.0f, 1.0f));
        animatorSet.setInterpolator(this.decelerateInterpolator);
        animatorSet.setDuration(150L);
        animatorSet.start();
    }

    private static int getSideCoord(boolean z, int i, float f, int i2) {
        int i3;
        int i4;
        if (z) {
            i3 = AndroidUtilities.displaySize.x;
        } else {
            i3 = AndroidUtilities.displaySize.y - i2;
            i2 = ActionBar.getCurrentActionBarHeight();
        }
        int i5 = i3 - i2;
        if (i == 0) {
            i4 = AndroidUtilities.dp(10.0f);
        } else if (i == 1) {
            i4 = i5 - AndroidUtilities.dp(10.0f);
        } else {
            i4 = Math.round((i5 - AndroidUtilities.dp(20.0f)) * f) + AndroidUtilities.dp(10.0f);
        }
        return !z ? i4 + ActionBar.getCurrentActionBarHeight() : i4;
    }

    public void hide() {
        if (this.parentActivity == null) {
            return;
        }
        try {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, 1.0f, 0.0f), ObjectAnimator.ofFloat(this.windowView, View.SCALE_X, 1.0f, 0.0f), ObjectAnimator.ofFloat(this.windowView, View.SCALE_Y, 1.0f, 0.0f));
            animatorSet.setInterpolator(this.decelerateInterpolator);
            animatorSet.setDuration(150L);
            animatorSet.addListener(new AnonymousClass3());
            animatorSet.start();
        } catch (Exception unused) {
        }
    }

    /* renamed from: org.telegram.ui.Components.ThemeEditorView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        AnonymousClass3() {
            ThemeEditorView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ThemeEditorView.this.windowView != null) {
                ThemeEditorView.this.windowView.setBackground(null);
                ThemeEditorView.this.windowManager.removeView(ThemeEditorView.this.windowView);
            }
        }
    }

    public void show() {
        if (this.parentActivity == null) {
            return;
        }
        try {
            this.windowManager.addView(this.windowView, this.windowLayoutParams);
            showWithAnimation();
        } catch (Exception unused) {
        }
    }

    public void close() {
        try {
            this.windowManager.removeView(this.windowView);
        } catch (Exception unused) {
        }
        this.parentActivity = null;
    }

    public void onConfigurationChanged() {
        int i = this.preferences.getInt("sidex", 1);
        int i2 = this.preferences.getInt("sidey", 0);
        float f = this.preferences.getFloat("px", 0.0f);
        float f2 = this.preferences.getFloat("py", 0.0f);
        this.windowLayoutParams.x = getSideCoord(true, i, f, this.editorWidth);
        this.windowLayoutParams.y = getSideCoord(false, i2, f2, this.editorHeight);
        try {
            if (this.windowView.getParent() == null) {
                return;
            }
            this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        WallpaperUpdater wallpaperUpdater = this.wallpaperUpdater;
        if (wallpaperUpdater != null) {
            wallpaperUpdater.onActivityResult(i, i2, intent);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x010e  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void animateToBoundsMaybe() {
        boolean z;
        ArrayList arrayList;
        int i;
        int sideCoord = getSideCoord(true, 0, 0.0f, this.editorWidth);
        int sideCoord2 = getSideCoord(true, 1, 0.0f, this.editorWidth);
        int sideCoord3 = getSideCoord(false, 0, 0.0f, this.editorHeight);
        int sideCoord4 = getSideCoord(false, 1, 0.0f, this.editorHeight);
        SharedPreferences.Editor edit = this.preferences.edit();
        int dp = AndroidUtilities.dp(20.0f);
        if (Math.abs(sideCoord - this.windowLayoutParams.x) <= dp || ((i = this.windowLayoutParams.x) < 0 && i > (-this.editorWidth) / 4)) {
            ArrayList arrayList2 = new ArrayList();
            edit.putInt("sidex", 0);
            if (this.windowView.getAlpha() != 1.0f) {
                arrayList2.add(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, 1.0f));
            }
            arrayList2.add(ObjectAnimator.ofInt(this, "x", sideCoord));
            arrayList = arrayList2;
        } else {
            if (Math.abs(sideCoord2 - i) > dp) {
                int i2 = this.windowLayoutParams.x;
                int i3 = AndroidUtilities.displaySize.x;
                int i4 = this.editorWidth;
                if (i2 <= i3 - i4 || i2 >= i3 - ((i4 / 4) * 3)) {
                    if (this.windowView.getAlpha() != 1.0f) {
                        arrayList = new ArrayList();
                        if (this.windowLayoutParams.x < 0) {
                            arrayList.add(ObjectAnimator.ofInt(this, "x", -this.editorWidth));
                        } else {
                            arrayList.add(ObjectAnimator.ofInt(this, "x", AndroidUtilities.displaySize.x));
                        }
                        z = true;
                        if (!z) {
                            if (Math.abs(sideCoord3 - this.windowLayoutParams.y) <= dp || this.windowLayoutParams.y <= ActionBar.getCurrentActionBarHeight()) {
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                edit.putInt("sidey", 0);
                                arrayList.add(ObjectAnimator.ofInt(this, "y", sideCoord3));
                            } else if (Math.abs(sideCoord4 - this.windowLayoutParams.y) <= dp) {
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                edit.putInt("sidey", 1);
                                arrayList.add(ObjectAnimator.ofInt(this, "y", sideCoord4));
                            } else {
                                edit.putFloat("py", (this.windowLayoutParams.y - sideCoord3) / (sideCoord4 - sideCoord3));
                                edit.putInt("sidey", 2);
                            }
                            edit.commit();
                        }
                        if (arrayList != null) {
                            return;
                        }
                        if (this.decelerateInterpolator == null) {
                            this.decelerateInterpolator = new DecelerateInterpolator();
                        }
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.setInterpolator(this.decelerateInterpolator);
                        animatorSet.setDuration(150L);
                        if (z) {
                            arrayList.add(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, 0.0f));
                            animatorSet.addListener(new AnonymousClass4());
                        }
                        animatorSet.playTogether(arrayList);
                        animatorSet.start();
                        return;
                    }
                    edit.putFloat("px", (this.windowLayoutParams.x - sideCoord) / (sideCoord2 - sideCoord));
                    edit.putInt("sidex", 2);
                    arrayList = null;
                }
            }
            arrayList = new ArrayList();
            edit.putInt("sidex", 1);
            if (this.windowView.getAlpha() != 1.0f) {
                arrayList.add(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, 1.0f));
            }
            arrayList.add(ObjectAnimator.ofInt(this, "x", sideCoord2));
        }
        z = false;
        if (!z) {
        }
        if (arrayList != null) {
        }
    }

    /* renamed from: org.telegram.ui.Components.ThemeEditorView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        AnonymousClass4() {
            ThemeEditorView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            Theme.saveCurrentTheme(ThemeEditorView.this.themeInfo, true, false, false);
            ThemeEditorView.this.destroy();
        }
    }

    @Keep
    public int getX() {
        return this.windowLayoutParams.x;
    }

    @Keep
    public int getY() {
        return this.windowLayoutParams.y;
    }

    @Keep
    public void setX(int i) {
        WindowManager.LayoutParams layoutParams = this.windowLayoutParams;
        layoutParams.x = i;
        this.windowManager.updateViewLayout(this.windowView, layoutParams);
    }

    @Keep
    public void setY(int i) {
        WindowManager.LayoutParams layoutParams = this.windowLayoutParams;
        layoutParams.y = i;
        this.windowManager.updateViewLayout(this.windowView, layoutParams);
    }
}