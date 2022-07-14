package org.telegram.ui.Cells;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.exoplayer2.C;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;
/* loaded from: classes4.dex */
public class PhotoEditRadioCell extends FrameLayout {
    private int currentColor;
    private int currentType;
    private TextView nameTextView;
    private View.OnClickListener onClickListener;
    private LinearLayout tintButtonsContainer;
    private final int[] tintShadowColors = {0, -45747, -753630, -13056, -8269183, -9321002, -16747844, -10080879};
    private final int[] tintHighlighsColors = {0, -1076602, -1388894, -859780, -5968466, -7742235, -13726776, -3303195};

    public PhotoEditRadioCell(Context context) {
        super(context);
        TextView textView = new TextView(context);
        this.nameTextView = textView;
        textView.setGravity(5);
        this.nameTextView.setTextColor(-1);
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        addView(this.nameTextView, LayoutHelper.createFrame(80, -2.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.tintButtonsContainer = linearLayout;
        linearLayout.setOrientation(0);
        for (int a = 0; a < this.tintShadowColors.length; a++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setSize(AndroidUtilities.dp(20.0f));
            radioButton.setTag(Integer.valueOf(a));
            this.tintButtonsContainer.addView(radioButton, LayoutHelper.createLinear(0, -1, 1.0f / this.tintShadowColors.length));
            radioButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Cells.PhotoEditRadioCell$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoEditRadioCell.this.m1661lambda$new$0$orgtelegramuiCellsPhotoEditRadioCell(view);
                }
            });
        }
        addView(this.tintButtonsContainer, LayoutHelper.createFrame(-1, 40.0f, 51, 96.0f, 0.0f, 24.0f, 0.0f));
    }

    /* renamed from: lambda$new$0$org-telegram-ui-Cells-PhotoEditRadioCell */
    public /* synthetic */ void m1661lambda$new$0$orgtelegramuiCellsPhotoEditRadioCell(View v) {
        RadioButton radioButton1 = (RadioButton) v;
        if (this.currentType == 0) {
            this.currentColor = this.tintShadowColors[((Integer) radioButton1.getTag()).intValue()];
        } else {
            this.currentColor = this.tintHighlighsColors[((Integer) radioButton1.getTag()).intValue()];
        }
        updateSelectedTintButton(true);
        this.onClickListener.onClick(this);
    }

    public int getCurrentColor() {
        return this.currentColor;
    }

    private void updateSelectedTintButton(boolean animated) {
        int childCount = this.tintButtonsContainer.getChildCount();
        for (int a = 0; a < childCount; a++) {
            View child = this.tintButtonsContainer.getChildAt(a);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                int num = ((Integer) radioButton.getTag()).intValue();
                int color2 = this.currentType == 0 ? this.tintShadowColors[num] : this.tintHighlighsColors[num];
                radioButton.setChecked(this.currentColor == color2, animated);
                int i = -1;
                int i2 = num == 0 ? -1 : this.currentType == 0 ? this.tintShadowColors[num] : this.tintHighlighsColors[num];
                if (num != 0) {
                    i = this.currentType == 0 ? this.tintShadowColors[num] : this.tintHighlighsColors[num];
                }
                radioButton.setColor(i2, i);
            }
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        this.onClickListener = l;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), C.BUFFER_FLAG_ENCRYPTED));
    }

    public void setIconAndTextAndValue(String text, int type, int value) {
        this.currentType = type;
        this.currentColor = value;
        TextView textView = this.nameTextView;
        textView.setText(text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase());
        updateSelectedTintButton(false);
    }
}
