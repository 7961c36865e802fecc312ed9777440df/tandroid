package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_pageBlockDivider extends TLRPC$PageBlock {
    public static int constructor = -618614392;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}