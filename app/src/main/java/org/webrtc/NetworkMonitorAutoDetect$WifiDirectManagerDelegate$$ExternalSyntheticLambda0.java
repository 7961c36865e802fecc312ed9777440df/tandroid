package org.webrtc;

import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import org.webrtc.NetworkMonitorAutoDetect;
/* loaded from: classes3.dex */
public final /* synthetic */ class NetworkMonitorAutoDetect$WifiDirectManagerDelegate$$ExternalSyntheticLambda0 implements WifiP2pManager.GroupInfoListener {
    public final /* synthetic */ NetworkMonitorAutoDetect.WifiDirectManagerDelegate f$0;

    public /* synthetic */ NetworkMonitorAutoDetect$WifiDirectManagerDelegate$$ExternalSyntheticLambda0(NetworkMonitorAutoDetect.WifiDirectManagerDelegate wifiDirectManagerDelegate) {
        this.f$0 = wifiDirectManagerDelegate;
    }

    @Override // android.net.wifi.p2p.WifiP2pManager.GroupInfoListener
    public final void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
        this.f$0.lambda$new$0(wifiP2pGroup);
    }
}