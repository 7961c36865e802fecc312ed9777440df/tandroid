package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.util.UUIDConverter;
import java.nio.ByteBuffer;
import java.util.UUID;
/* loaded from: classes3.dex */
public class CencSampleEncryptionInformationGroupEntry extends GroupEntry {
    public static final String TYPE = "seig";
    private boolean isEncrypted;
    private byte ivSize;
    private UUID kid;

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public String getType() {
        return TYPE;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public void parse(ByteBuffer byteBuffer) {
        boolean z = true;
        if (IsoTypeReader.readUInt24(byteBuffer) != 1) {
            z = false;
        }
        this.isEncrypted = z;
        this.ivSize = (byte) IsoTypeReader.readUInt8(byteBuffer);
        byte[] kid = new byte[16];
        byteBuffer.get(kid);
        this.kid = UUIDConverter.convert(kid);
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public ByteBuffer get() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        IsoTypeWriter.writeUInt24(byteBuffer, this.isEncrypted ? 1 : 0);
        if (this.isEncrypted) {
            IsoTypeWriter.writeUInt8(byteBuffer, this.ivSize);
            byteBuffer.put(UUIDConverter.convert(this.kid));
        } else {
            byteBuffer.put(new byte[17]);
        }
        byteBuffer.rewind();
        return byteBuffer;
    }

    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.isEncrypted = encrypted;
    }

    public byte getIvSize() {
        return this.ivSize;
    }

    public void setIvSize(int ivSize) {
        this.ivSize = (byte) ivSize;
    }

    public UUID getKid() {
        return this.kid;
    }

    public void setKid(UUID kid) {
        this.kid = kid;
    }

    public String toString() {
        return "CencSampleEncryptionInformationGroupEntry{isEncrypted=" + this.isEncrypted + ", ivSize=" + ((int) this.ivSize) + ", kid=" + this.kid + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CencSampleEncryptionInformationGroupEntry that = (CencSampleEncryptionInformationGroupEntry) o;
        if (this.isEncrypted != that.isEncrypted || this.ivSize != that.ivSize) {
            return false;
        }
        UUID uuid = this.kid;
        if (uuid == null ? that.kid == null : uuid.equals(that.kid)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.isEncrypted ? 7 : 19;
        int result2 = ((result * 31) + this.ivSize) * 31;
        UUID uuid = this.kid;
        return result2 + (uuid != null ? uuid.hashCode() : 0);
    }
}
