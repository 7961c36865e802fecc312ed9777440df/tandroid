package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import com.airbnb.lottie.animation.content.ShapeModifierContent;
import com.airbnb.lottie.model.content.ShapeData;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
/* loaded from: classes.dex */
public class ShapeKeyframeAnimation extends BaseKeyframeAnimation<ShapeData, Path> {
    private List<ShapeModifierContent> shapeModifiers;
    private final Path tempPath;
    private final ShapeData tempShapeData;

    public ShapeKeyframeAnimation(List<Keyframe<ShapeData>> list) {
        super(list);
        this.tempShapeData = new ShapeData();
        this.tempPath = new Path();
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public Path getValue(Keyframe<ShapeData> keyframe, float f) {
        ShapeData shapeData = keyframe.startValue;
        ShapeData shapeData2 = keyframe.endValue;
        ShapeData shapeData3 = this.tempShapeData;
        if (shapeData2 == null) {
            shapeData2 = shapeData;
        }
        shapeData3.interpolateBetween(shapeData, shapeData2, f);
        ShapeData shapeData4 = this.tempShapeData;
        List<ShapeModifierContent> list = this.shapeModifiers;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                shapeData4 = this.shapeModifiers.get(size).modifyShape(shapeData4);
            }
        }
        MiscUtils.getPathFromData(shapeData4, this.tempPath);
        return this.tempPath;
    }

    public void setShapeModifiers(List<ShapeModifierContent> list) {
        this.shapeModifiers = list;
    }
}