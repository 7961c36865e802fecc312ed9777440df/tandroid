package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_pageBlockSubheader extends TLRPC$PageBlock {
    public TLRPC$RichText text;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = TLRPC$RichText.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-248793375);
        this.text.serializeToStream(abstractSerializedData);
    }
}
