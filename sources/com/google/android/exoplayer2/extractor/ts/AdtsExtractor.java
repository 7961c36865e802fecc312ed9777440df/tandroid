package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;
import org.telegram.messenger.LiteMode;
/* loaded from: classes.dex */
public final class AdtsExtractor implements Extractor {
    private int averageFrameSize;
    private ExtractorOutput extractorOutput;
    private long firstFramePosition;
    private long firstSampleTimestampUs;
    private final int flags;
    private boolean hasCalculatedAverageFrameSize;
    private boolean hasOutputSeekMap;
    private final ParsableByteArray packetBuffer;
    private final AdtsReader reader;
    private final ParsableByteArray scratch;
    private final ParsableBitArray scratchBits;
    private boolean startedPacket;

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    static {
        AdtsExtractor$$ExternalSyntheticLambda0 adtsExtractor$$ExternalSyntheticLambda0 = AdtsExtractor$$ExternalSyntheticLambda0.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new AdtsExtractor()};
    }

    public AdtsExtractor() {
        this(0);
    }

    public AdtsExtractor(int i) {
        this.flags = (i & 2) != 0 ? i | 1 : i;
        this.reader = new AdtsReader(true);
        this.packetBuffer = new ParsableByteArray((int) LiteMode.FLAG_AUTOPLAY_GIFS);
        this.averageFrameSize = -1;
        this.firstFramePosition = -1L;
        ParsableByteArray parsableByteArray = new ParsableByteArray(10);
        this.scratch = parsableByteArray;
        this.scratchBits = new ParsableBitArray(parsableByteArray.getData());
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput extractorInput) throws IOException {
        int peekId3Header = peekId3Header(extractorInput);
        int i = peekId3Header;
        int i2 = 0;
        int i3 = 0;
        do {
            extractorInput.peekFully(this.scratch.getData(), 0, 2);
            this.scratch.setPosition(0);
            if (AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort())) {
                i2++;
                if (i2 >= 4 && i3 > 188) {
                    return true;
                }
                extractorInput.peekFully(this.scratch.getData(), 0, 4);
                this.scratchBits.setPosition(14);
                int readBits = this.scratchBits.readBits(13);
                if (readBits <= 6) {
                    i++;
                    extractorInput.resetPeekPosition();
                    extractorInput.advancePeekPosition(i);
                } else {
                    extractorInput.advancePeekPosition(readBits - 6);
                    i3 += readBits;
                }
            } else {
                i++;
                extractorInput.resetPeekPosition();
                extractorInput.advancePeekPosition(i);
            }
            i2 = 0;
            i3 = 0;
        } while (i - peekId3Header < 8192);
        return false;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.reader.createTracks(extractorOutput, new TsPayloadReader.TrackIdGenerator(0, 1));
        extractorOutput.endTracks();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long j, long j2) {
        this.startedPacket = false;
        this.reader.seek();
        this.firstSampleTimestampUs = j2;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException {
        Assertions.checkStateNotNull(this.extractorOutput);
        long length = extractorInput.getLength();
        int i = this.flags;
        if (((i & 2) == 0 && ((i & 1) == 0 || length == -1)) ? false : true) {
            calculateAverageFrameSize(extractorInput);
        }
        int read = extractorInput.read(this.packetBuffer.getData(), 0, LiteMode.FLAG_AUTOPLAY_GIFS);
        boolean z = read == -1;
        maybeOutputSeekMap(length, z);
        if (z) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(read);
        if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
        }
        this.reader.consume(this.packetBuffer);
        return 0;
    }

    private int peekId3Header(ExtractorInput extractorInput) throws IOException {
        int i = 0;
        while (true) {
            extractorInput.peekFully(this.scratch.getData(), 0, 10);
            this.scratch.setPosition(0);
            if (this.scratch.readUnsignedInt24() != 4801587) {
                break;
            }
            this.scratch.skipBytes(3);
            int readSynchSafeInt = this.scratch.readSynchSafeInt();
            i += readSynchSafeInt + 10;
            extractorInput.advancePeekPosition(readSynchSafeInt);
        }
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(i);
        if (this.firstFramePosition == -1) {
            this.firstFramePosition = i;
        }
        return i;
    }

    private void maybeOutputSeekMap(long j, boolean z) {
        if (this.hasOutputSeekMap) {
            return;
        }
        boolean z2 = (this.flags & 1) != 0 && this.averageFrameSize > 0;
        if (z2 && this.reader.getSampleDurationUs() == -9223372036854775807L && !z) {
            return;
        }
        if (z2 && this.reader.getSampleDurationUs() != -9223372036854775807L) {
            this.extractorOutput.seekMap(getConstantBitrateSeekMap(j, (this.flags & 2) != 0));
        } else {
            this.extractorOutput.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
        }
        this.hasOutputSeekMap = true;
    }

    private void calculateAverageFrameSize(ExtractorInput extractorInput) throws IOException {
        int readBits;
        if (this.hasCalculatedAverageFrameSize) {
            return;
        }
        this.averageFrameSize = -1;
        extractorInput.resetPeekPosition();
        long j = 0;
        if (extractorInput.getPosition() == 0) {
            peekId3Header(extractorInput);
        }
        int i = 0;
        int i2 = 0;
        do {
            try {
                if (!extractorInput.peekFully(this.scratch.getData(), 0, 2, true)) {
                    break;
                }
                this.scratch.setPosition(0);
                if (!AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort())) {
                    break;
                } else if (!extractorInput.peekFully(this.scratch.getData(), 0, 4, true)) {
                    break;
                } else {
                    this.scratchBits.setPosition(14);
                    readBits = this.scratchBits.readBits(13);
                    if (readBits <= 6) {
                        this.hasCalculatedAverageFrameSize = true;
                        throw ParserException.createForMalformedContainer("Malformed ADTS stream", null);
                    }
                    j += readBits;
                    i2++;
                    if (i2 == 1000) {
                        break;
                    }
                }
            } catch (EOFException unused) {
            }
        } while (extractorInput.advancePeekPosition(readBits - 6, true));
        i = i2;
        extractorInput.resetPeekPosition();
        if (i > 0) {
            this.averageFrameSize = (int) (j / i);
        } else {
            this.averageFrameSize = -1;
        }
        this.hasCalculatedAverageFrameSize = true;
    }

    private SeekMap getConstantBitrateSeekMap(long j, boolean z) {
        return new ConstantBitrateSeekMap(j, this.firstFramePosition, getBitrateFromFrameSize(this.averageFrameSize, this.reader.getSampleDurationUs()), this.averageFrameSize, z);
    }

    private static int getBitrateFromFrameSize(int i, long j) {
        return (int) (((i * 8) * 1000000) / j);
    }
}