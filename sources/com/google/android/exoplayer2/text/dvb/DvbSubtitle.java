package com.google.android.exoplayer2.text.dvb;

import com.google.android.exoplayer2.text.Subtitle;
import java.util.List;

/* loaded from: classes.dex */
final class DvbSubtitle implements Subtitle {
    private final List cues;

    public DvbSubtitle(List list) {
        this.cues = list;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public List getCues(long j) {
        return this.cues;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public long getEventTime(int i) {
        return 0L;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getEventTimeCount() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getNextEventTimeIndex(long j) {
        return -1;
    }
}
