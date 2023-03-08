package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.flv.TagPayloadReader;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.video.AvcConfig;
import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
final class VideoTagPayloadReader extends TagPayloadReader {
    private int frameType;
    private boolean hasOutputFormat;
    private boolean hasOutputKeyframe;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private int nalUnitLengthFieldLength;

    public VideoTagPayloadReader(TrackOutput trackOutput) {
        super(trackOutput);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray parsableByteArray) throws TagPayloadReader.UnsupportedFormatException {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int i = (readUnsignedByte >> 4) & 15;
        int i2 = readUnsignedByte & 15;
        if (i2 != 7) {
            throw new TagPayloadReader.UnsupportedFormatException("Video format not supported: " + i2);
        }
        this.frameType = i;
        return i != 5;
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean parsePayload(ParsableByteArray parsableByteArray, long j) throws ParserException {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        long readInt24 = j + (parsableByteArray.readInt24() * 1000);
        if (readUnsignedByte == 0 && !this.hasOutputFormat) {
            ParsableByteArray parsableByteArray2 = new ParsableByteArray(new byte[parsableByteArray.bytesLeft()]);
            parsableByteArray.readBytes(parsableByteArray2.getData(), 0, parsableByteArray.bytesLeft());
            AvcConfig parse = AvcConfig.parse(parsableByteArray2);
            this.nalUnitLengthFieldLength = parse.nalUnitLengthFieldLength;
            this.output.format(new Format.Builder().setSampleMimeType(MediaController.VIDEO_MIME_TYPE).setCodecs(parse.codecs).setWidth(parse.width).setHeight(parse.height).setPixelWidthHeightRatio(parse.pixelWidthHeightRatio).setInitializationData(parse.initializationData).build());
            this.hasOutputFormat = true;
            return false;
        } else if (readUnsignedByte == 1 && this.hasOutputFormat) {
            int i = this.frameType == 1 ? 1 : 0;
            if (this.hasOutputKeyframe || i != 0) {
                byte[] data = this.nalLength.getData();
                data[0] = 0;
                data[1] = 0;
                data[2] = 0;
                int i2 = 4 - this.nalUnitLengthFieldLength;
                int i3 = 0;
                while (parsableByteArray.bytesLeft() > 0) {
                    parsableByteArray.readBytes(this.nalLength.getData(), i2, this.nalUnitLengthFieldLength);
                    this.nalLength.setPosition(0);
                    int readUnsignedIntToInt = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    this.output.sampleData(this.nalStartCode, 4);
                    this.output.sampleData(parsableByteArray, readUnsignedIntToInt);
                    i3 = i3 + 4 + readUnsignedIntToInt;
                }
                this.output.sampleMetadata(readInt24, i, i3, 0, null);
                this.hasOutputKeyframe = true;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}