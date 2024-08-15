package org.telegram.tgnet.tl;

import java.util.ArrayList;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Bool;
import org.telegram.tgnet.TLRPC$InputMedia;
import org.telegram.tgnet.TLRPC$InputUser;
/* loaded from: classes3.dex */
public class TL_bots$deletePreviewMedia extends TLObject {
    public TLRPC$InputUser bot;
    public String lang_code = "";
    public ArrayList<TLRPC$InputMedia> media = new ArrayList<>();

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(755054003);
        this.bot.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.lang_code);
        abstractSerializedData.writeInt32(481674261);
        int size = this.media.size();
        abstractSerializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            this.media.get(i).serializeToStream(abstractSerializedData);
        }
    }
}