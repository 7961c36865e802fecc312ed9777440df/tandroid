package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_pageBlockPullquote extends TLRPC$PageBlock {
    public TLRPC$RichText caption;
    public TLRPC$RichText text;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = TLRPC$RichText.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.caption = TLRPC$RichText.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1329878739);
        this.text.serializeToStream(abstractSerializedData);
        this.caption.serializeToStream(abstractSerializedData);
    }
}
