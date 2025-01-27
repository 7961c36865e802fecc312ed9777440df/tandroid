package org.webrtc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.voip.JNIUtilities$$ExternalSyntheticApiModelOutline3;
import org.webrtc.NetworkChangeDetector;
import org.webrtc.NetworkMonitorAutoDetect;

/* loaded from: classes5.dex */
public class NetworkMonitorAutoDetect extends BroadcastReceiver implements NetworkChangeDetector {
    private static final long INVALID_NET_ID = -1;
    private static final String TAG = "NetworkMonitorAutoDetect";
    private final ConnectivityManager.NetworkCallback allNetworkCallback;
    private NetworkChangeDetector.ConnectionType connectionType;
    private ConnectivityManagerDelegate connectivityManagerDelegate;
    private final Context context;
    private final IntentFilter intentFilter;
    private boolean isRegistered;
    private final ConnectivityManager.NetworkCallback mobileNetworkCallback;
    private final NetworkChangeDetector.Observer observer;
    private WifiDirectManagerDelegate wifiDirectManagerDelegate;
    private WifiManagerDelegate wifiManagerDelegate;
    private String wifiSSID;

    static class ConnectivityManagerDelegate {
        private final ConnectivityManager connectivityManager;

        ConnectivityManagerDelegate() {
            this.connectivityManager = null;
        }

        ConnectivityManagerDelegate(Context context) {
            this.connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        }

        private NetworkState getNetworkState(NetworkInfo networkInfo) {
            return (networkInfo == null || !networkInfo.isConnected()) ? new NetworkState(false, -1, -1, -1, -1) : new NetworkState(true, networkInfo.getType(), networkInfo.getSubtype(), -1, -1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public NetworkChangeDetector.NetworkInformation networkToInfo(Network network) {
            ConnectivityManager connectivityManager;
            LinkProperties linkProperties;
            String interfaceName;
            String network2;
            String interfaceName2;
            String network3;
            String network4;
            String network5;
            if (network == null || (connectivityManager = this.connectivityManager) == null) {
                return null;
            }
            linkProperties = connectivityManager.getLinkProperties(network);
            if (linkProperties == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Detected unknown network: ");
                network5 = network.toString();
                sb.append(network5);
                Logging.w(NetworkMonitorAutoDetect.TAG, sb.toString());
                return null;
            }
            interfaceName = linkProperties.getInterfaceName();
            if (interfaceName == null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Null interface name for network ");
                network4 = network.toString();
                sb2.append(network4);
                Logging.w(NetworkMonitorAutoDetect.TAG, sb2.toString());
                return null;
            }
            NetworkState networkState = getNetworkState(network);
            NetworkChangeDetector.ConnectionType connectionType = NetworkMonitorAutoDetect.getConnectionType(networkState);
            if (connectionType == NetworkChangeDetector.ConnectionType.CONNECTION_NONE) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Network ");
                network3 = network.toString();
                sb3.append(network3);
                sb3.append(" is disconnected");
                Logging.d(NetworkMonitorAutoDetect.TAG, sb3.toString());
                return null;
            }
            if (connectionType == NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN || connectionType == NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN_CELLULAR) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Network ");
                network2 = network.toString();
                sb4.append(network2);
                sb4.append(" connection type is ");
                sb4.append(connectionType);
                sb4.append(" because it has type ");
                sb4.append(networkState.getNetworkType());
                sb4.append(" and subtype ");
                sb4.append(networkState.getNetworkSubType());
                Logging.d(NetworkMonitorAutoDetect.TAG, sb4.toString());
            }
            NetworkChangeDetector.ConnectionType underlyingConnectionTypeForVpn = NetworkMonitorAutoDetect.getUnderlyingConnectionTypeForVpn(networkState);
            interfaceName2 = linkProperties.getInterfaceName();
            return new NetworkChangeDetector.NetworkInformation(interfaceName2, connectionType, underlyingConnectionTypeForVpn, NetworkMonitorAutoDetect.networkToNetId(network), getIPAddresses(linkProperties));
        }

        List<NetworkChangeDetector.NetworkInformation> getActiveNetworkList() {
            if (!supportNetworkCallback()) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (Network network : getAllNetworks()) {
                NetworkChangeDetector.NetworkInformation networkToInfo = networkToInfo(network);
                if (networkToInfo != null) {
                    arrayList.add(networkToInfo);
                }
            }
            return arrayList;
        }

        Network[] getAllNetworks() {
            Network[] allNetworks;
            ConnectivityManager connectivityManager = this.connectivityManager;
            if (connectivityManager == null) {
                return new Network[0];
            }
            allNetworks = connectivityManager.getAllNetworks();
            return allNetworks;
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x0024, code lost:
        
            r9 = r11.connectivityManager.getNetworkInfo(r8);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        long getDefaultNetId() {
            NetworkInfo activeNetworkInfo;
            NetworkInfo networkInfo;
            if (!supportNetworkCallback() || (activeNetworkInfo = this.connectivityManager.getActiveNetworkInfo()) == null) {
                return -1L;
            }
            Network[] allNetworks = getAllNetworks();
            int length = allNetworks.length;
            long j = -1;
            for (int i = 0; i < length; i++) {
                Network network = allNetworks[i];
                if (hasInternetCapability(network) && networkInfo != null && networkInfo.getType() == activeNetworkInfo.getType()) {
                    if (j != -1) {
                        throw new RuntimeException("Multiple connected networks of same type are not supported.");
                    }
                    j = NetworkMonitorAutoDetect.networkToNetId(network);
                }
            }
            return j;
        }

        NetworkChangeDetector.IPAddress[] getIPAddresses(LinkProperties linkProperties) {
            List linkAddresses;
            List linkAddresses2;
            InetAddress address;
            linkAddresses = linkProperties.getLinkAddresses();
            NetworkChangeDetector.IPAddress[] iPAddressArr = new NetworkChangeDetector.IPAddress[linkAddresses.size()];
            linkAddresses2 = linkProperties.getLinkAddresses();
            Iterator it = linkAddresses2.iterator();
            int i = 0;
            while (it.hasNext()) {
                address = JNIUtilities$$ExternalSyntheticApiModelOutline3.m(it.next()).getAddress();
                iPAddressArr[i] = new NetworkChangeDetector.IPAddress(address.getAddress());
                i++;
            }
            return iPAddressArr;
        }

        NetworkState getNetworkState() {
            ConnectivityManager connectivityManager = this.connectivityManager;
            return connectivityManager == null ? new NetworkState(false, -1, -1, -1, -1) : getNetworkState(connectivityManager.getActiveNetworkInfo());
        }

        NetworkState getNetworkState(Network network) {
            ConnectivityManager connectivityManager;
            NetworkInfo networkInfo;
            Network activeNetwork;
            boolean equals;
            NetworkInfo activeNetworkInfo;
            NetworkCapabilities networkCapabilities;
            boolean hasTransport;
            String network2;
            if (network == null || (connectivityManager = this.connectivityManager) == null) {
                return new NetworkState(false, -1, -1, -1, -1);
            }
            networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Couldn't retrieve information from network ");
                network2 = network.toString();
                sb.append(network2);
                Logging.w(NetworkMonitorAutoDetect.TAG, sb.toString());
                return new NetworkState(false, -1, -1, -1, -1);
            }
            if (networkInfo.getType() != 17) {
                networkCapabilities = this.connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    hasTransport = networkCapabilities.hasTransport(4);
                    if (hasTransport) {
                        return new NetworkState(networkInfo.isConnected(), 17, -1, networkInfo.getType(), networkInfo.getSubtype());
                    }
                }
                return getNetworkState(networkInfo);
            }
            if (networkInfo.getType() != 17) {
                return getNetworkState(networkInfo);
            }
            if (Build.VERSION.SDK_INT >= 23) {
                activeNetwork = this.connectivityManager.getActiveNetwork();
                equals = network.equals(activeNetwork);
                if (equals && (activeNetworkInfo = this.connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.getType() != 17) {
                    return new NetworkState(networkInfo.isConnected(), 17, -1, activeNetworkInfo.getType(), activeNetworkInfo.getSubtype());
                }
            }
            return new NetworkState(networkInfo.isConnected(), 17, -1, -1, -1);
        }

        /* JADX WARN: Code restructure failed: missing block: B:5:0x0006, code lost:
        
            r3 = r0.getNetworkCapabilities(r3);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        boolean hasInternetCapability(Network network) {
            NetworkCapabilities networkCapabilities;
            boolean hasCapability;
            ConnectivityManager connectivityManager = this.connectivityManager;
            if (connectivityManager == null || networkCapabilities == null) {
                return false;
            }
            hasCapability = networkCapabilities.hasCapability(12);
            return hasCapability;
        }

        public void registerNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
            NetworkRequest.Builder addCapability;
            NetworkRequest build;
            ConnectivityManager connectivityManager = this.connectivityManager;
            addCapability = new NetworkRequest.Builder().addCapability(12);
            build = addCapability.build();
            connectivityManager.registerNetworkCallback(build, networkCallback);
        }

        public void releaseCallback(ConnectivityManager.NetworkCallback networkCallback) {
            if (supportNetworkCallback()) {
                Logging.d(NetworkMonitorAutoDetect.TAG, "Unregister network callback");
                this.connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }

        public void requestMobileNetwork(ConnectivityManager.NetworkCallback networkCallback) {
            NetworkRequest.Builder addCapability;
            NetworkRequest build;
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            addCapability = builder.addCapability(12);
            addCapability.addTransportType(0);
            ConnectivityManager connectivityManager = this.connectivityManager;
            build = builder.build();
            connectivityManager.requestNetwork(build, networkCallback);
        }

        public boolean supportNetworkCallback() {
            return Build.VERSION.SDK_INT >= 21 && this.connectivityManager != null;
        }
    }

    static class NetworkState {
        private final boolean connected;
        private final int subtype;
        private final int type;
        private final int underlyingNetworkSubtypeForVpn;
        private final int underlyingNetworkTypeForVpn;

        public NetworkState(boolean z, int i, int i2, int i3, int i4) {
            this.connected = z;
            this.type = i;
            this.subtype = i2;
            this.underlyingNetworkTypeForVpn = i3;
            this.underlyingNetworkSubtypeForVpn = i4;
        }

        public int getNetworkSubType() {
            return this.subtype;
        }

        public int getNetworkType() {
            return this.type;
        }

        public int getUnderlyingNetworkSubtypeForVpn() {
            return this.underlyingNetworkSubtypeForVpn;
        }

        public int getUnderlyingNetworkTypeForVpn() {
            return this.underlyingNetworkTypeForVpn;
        }

        public boolean isConnected() {
            return this.connected;
        }
    }

    private class SimpleNetworkCallback extends ConnectivityManager.NetworkCallback {
        private SimpleNetworkCallback() {
        }

        private void onNetworkChanged(Network network) {
            NetworkChangeDetector.NetworkInformation networkToInfo = NetworkMonitorAutoDetect.this.connectivityManagerDelegate.networkToInfo(network);
            if (networkToInfo != null) {
                NetworkMonitorAutoDetect.this.observer.onNetworkConnect(networkToInfo);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            String network2;
            StringBuilder sb = new StringBuilder();
            sb.append("Network becomes available: ");
            network2 = network.toString();
            sb.append(network2);
            Logging.d(NetworkMonitorAutoDetect.TAG, sb.toString());
            onNetworkChanged(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            String networkCapabilities2;
            StringBuilder sb = new StringBuilder();
            sb.append("capabilities changed: ");
            networkCapabilities2 = networkCapabilities.toString();
            sb.append(networkCapabilities2);
            Logging.d(NetworkMonitorAutoDetect.TAG, sb.toString());
            onNetworkChanged(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            Logging.d(NetworkMonitorAutoDetect.TAG, "link properties changed");
            onNetworkChanged(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLosing(Network network, int i) {
            String network2;
            StringBuilder sb = new StringBuilder();
            sb.append("Network ");
            network2 = network.toString();
            sb.append(network2);
            sb.append(" is about to lose in ");
            sb.append(i);
            sb.append("ms");
            Logging.d(NetworkMonitorAutoDetect.TAG, sb.toString());
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            String network2;
            StringBuilder sb = new StringBuilder();
            sb.append("Network ");
            network2 = network.toString();
            sb.append(network2);
            sb.append(" is disconnected");
            Logging.d(NetworkMonitorAutoDetect.TAG, sb.toString());
            NetworkMonitorAutoDetect.this.observer.onNetworkDisconnect(NetworkMonitorAutoDetect.networkToNetId(network));
        }
    }

    static class WifiDirectManagerDelegate extends BroadcastReceiver {
        private static final int WIFI_P2P_NETWORK_HANDLE = 0;
        private final Context context;
        private final NetworkChangeDetector.Observer observer;
        private NetworkChangeDetector.NetworkInformation wifiP2pNetworkInfo;

        WifiDirectManagerDelegate(NetworkChangeDetector.Observer observer, Context context) {
            this.context = context;
            this.observer = observer;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
            intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
            int i = Build.VERSION.SDK_INT;
            if (i >= 33) {
                context.registerReceiver(this, intentFilter, 4);
            } else {
                context.registerReceiver(this, intentFilter);
            }
            if (i > 28) {
                WifiP2pManager wifiP2pManager = (WifiP2pManager) context.getSystemService("wifip2p");
                wifiP2pManager.requestGroupInfo(wifiP2pManager.initialize(context, context.getMainLooper(), null), new WifiP2pManager.GroupInfoListener() { // from class: org.webrtc.NetworkMonitorAutoDetect$WifiDirectManagerDelegate$$ExternalSyntheticLambda0
                    @Override // android.net.wifi.p2p.WifiP2pManager.GroupInfoListener
                    public final void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                        NetworkMonitorAutoDetect.WifiDirectManagerDelegate.this.lambda$new$0(wifiP2pGroup);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: onWifiP2pGroupChange, reason: merged with bridge method [inline-methods] */
        public void lambda$new$0(WifiP2pGroup wifiP2pGroup) {
            if (wifiP2pGroup == null || wifiP2pGroup.getInterface() == null) {
                return;
            }
            try {
                ArrayList list = Collections.list(NetworkInterface.getByName(wifiP2pGroup.getInterface()).getInetAddresses());
                NetworkChangeDetector.IPAddress[] iPAddressArr = new NetworkChangeDetector.IPAddress[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    iPAddressArr[i] = new NetworkChangeDetector.IPAddress(((InetAddress) list.get(i)).getAddress());
                }
                NetworkChangeDetector.NetworkInformation networkInformation = new NetworkChangeDetector.NetworkInformation(wifiP2pGroup.getInterface(), NetworkChangeDetector.ConnectionType.CONNECTION_WIFI, NetworkChangeDetector.ConnectionType.CONNECTION_NONE, 0L, iPAddressArr);
                this.wifiP2pNetworkInfo = networkInformation;
                this.observer.onNetworkConnect(networkInformation);
            } catch (SocketException e) {
                Logging.e(NetworkMonitorAutoDetect.TAG, "Unable to get WifiP2p network interface", e);
            }
        }

        private void onWifiP2pStateChange(int i) {
            if (i == 1) {
                this.wifiP2pNetworkInfo = null;
                this.observer.onNetworkDisconnect(0L);
            }
        }

        public List<NetworkChangeDetector.NetworkInformation> getActiveNetworkList() {
            NetworkChangeDetector.NetworkInformation networkInformation = this.wifiP2pNetworkInfo;
            return networkInformation != null ? Collections.singletonList(networkInformation) : Collections.emptyList();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(intent.getAction())) {
                lambda$new$0((WifiP2pGroup) intent.getParcelableExtra("p2pGroupInfo"));
            } else if ("android.net.wifi.p2p.STATE_CHANGED".equals(intent.getAction())) {
                onWifiP2pStateChange(intent.getIntExtra("wifi_p2p_state", 0));
            }
        }

        public void release() {
            this.context.unregisterReceiver(this);
        }
    }

    static class WifiManagerDelegate {
        private final Context context;

        WifiManagerDelegate() {
            this.context = null;
        }

        WifiManagerDelegate(Context context) {
            this.context = context;
        }

        String getWifiSSID() {
            WifiInfo wifiInfo;
            String ssid;
            Intent registerReceiver = Build.VERSION.SDK_INT >= 33 ? this.context.registerReceiver(null, new IntentFilter("android.net.wifi.STATE_CHANGE"), 4) : this.context.registerReceiver(null, new IntentFilter("android.net.wifi.STATE_CHANGE"));
            return (registerReceiver == null || (wifiInfo = (WifiInfo) registerReceiver.getParcelableExtra("wifiInfo")) == null || (ssid = wifiInfo.getSSID()) == null) ? "" : ssid;
        }
    }

    public NetworkMonitorAutoDetect(NetworkChangeDetector.Observer observer, Context context) {
        this.observer = observer;
        this.context = context;
        this.connectivityManagerDelegate = new ConnectivityManagerDelegate(context);
        this.wifiManagerDelegate = new WifiManagerDelegate(context);
        NetworkState networkState = this.connectivityManagerDelegate.getNetworkState();
        this.connectionType = getConnectionType(networkState);
        this.wifiSSID = getWifiSSID(networkState);
        this.intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        if (PeerConnectionFactory.fieldTrialsFindFullName("IncludeWifiDirect").equals(PeerConnectionFactory.TRIAL_ENABLED)) {
            this.wifiDirectManagerDelegate = new WifiDirectManagerDelegate(observer, context);
        }
        registerReceiver();
        if (!this.connectivityManagerDelegate.supportNetworkCallback()) {
            this.mobileNetworkCallback = null;
            this.allNetworkCallback = null;
            return;
        }
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback();
        try {
            this.connectivityManagerDelegate.requestMobileNetwork(networkCallback);
        } catch (SecurityException unused) {
            Logging.w(TAG, "Unable to obtain permission to request a cellular network.");
            networkCallback = null;
        }
        this.mobileNetworkCallback = networkCallback;
        SimpleNetworkCallback simpleNetworkCallback = new SimpleNetworkCallback();
        this.allNetworkCallback = simpleNetworkCallback;
        this.connectivityManagerDelegate.registerNetworkCallback(simpleNetworkCallback);
    }

    private void connectionTypeChanged(NetworkState networkState) {
        NetworkChangeDetector.ConnectionType connectionType = getConnectionType(networkState);
        String wifiSSID = getWifiSSID(networkState);
        if (connectionType == this.connectionType && wifiSSID.equals(this.wifiSSID)) {
            return;
        }
        this.connectionType = connectionType;
        this.wifiSSID = wifiSSID;
        Logging.d(TAG, "Network connectivity changed, type is: " + this.connectionType);
        this.observer.onConnectionTypeChanged(connectionType);
    }

    public static NetworkChangeDetector.ConnectionType getConnectionType(NetworkState networkState) {
        return getConnectionType(networkState.isConnected(), networkState.getNetworkType(), networkState.getNetworkSubType());
    }

    private static NetworkChangeDetector.ConnectionType getConnectionType(boolean z, int i, int i2) {
        if (!z) {
            return NetworkChangeDetector.ConnectionType.CONNECTION_NONE;
        }
        if (i != 0) {
            return i != 1 ? i != 6 ? i != 7 ? i != 9 ? i != 17 ? NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN : NetworkChangeDetector.ConnectionType.CONNECTION_VPN : NetworkChangeDetector.ConnectionType.CONNECTION_ETHERNET : NetworkChangeDetector.ConnectionType.CONNECTION_BLUETOOTH : NetworkChangeDetector.ConnectionType.CONNECTION_4G : NetworkChangeDetector.ConnectionType.CONNECTION_WIFI;
        }
        switch (i2) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return NetworkChangeDetector.ConnectionType.CONNECTION_2G;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return NetworkChangeDetector.ConnectionType.CONNECTION_3G;
            case 13:
            case 18:
                return NetworkChangeDetector.ConnectionType.CONNECTION_4G;
            case 19:
            default:
                return NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN_CELLULAR;
            case 20:
                return NetworkChangeDetector.ConnectionType.CONNECTION_5G;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NetworkChangeDetector.ConnectionType getUnderlyingConnectionTypeForVpn(NetworkState networkState) {
        return networkState.getNetworkType() != 17 ? NetworkChangeDetector.ConnectionType.CONNECTION_NONE : getConnectionType(networkState.isConnected(), networkState.getUnderlyingNetworkTypeForVpn(), networkState.getUnderlyingNetworkSubtypeForVpn());
    }

    private String getWifiSSID(NetworkState networkState) {
        return getConnectionType(networkState) != NetworkChangeDetector.ConnectionType.CONNECTION_WIFI ? "" : this.wifiManagerDelegate.getWifiSSID();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long networkToNetId(Network network) {
        String network2;
        long networkHandle;
        if (Build.VERSION.SDK_INT >= 23) {
            networkHandle = network.getNetworkHandle();
            return networkHandle;
        }
        network2 = network.toString();
        return Integer.parseInt(network2);
    }

    private void registerReceiver() {
        if (this.isRegistered) {
            return;
        }
        this.isRegistered = true;
        if (Build.VERSION.SDK_INT >= 33) {
            this.context.registerReceiver(this, this.intentFilter, 4);
        } else {
            this.context.registerReceiver(this, this.intentFilter);
        }
    }

    private void unregisterReceiver() {
        if (this.isRegistered) {
            this.isRegistered = false;
            this.context.unregisterReceiver(this);
        }
    }

    @Override // org.webrtc.NetworkChangeDetector
    public void destroy() {
        ConnectivityManager.NetworkCallback networkCallback = this.allNetworkCallback;
        if (networkCallback != null) {
            this.connectivityManagerDelegate.releaseCallback(networkCallback);
        }
        ConnectivityManager.NetworkCallback networkCallback2 = this.mobileNetworkCallback;
        if (networkCallback2 != null) {
            this.connectivityManagerDelegate.releaseCallback(networkCallback2);
        }
        WifiDirectManagerDelegate wifiDirectManagerDelegate = this.wifiDirectManagerDelegate;
        if (wifiDirectManagerDelegate != null) {
            wifiDirectManagerDelegate.release();
        }
        unregisterReceiver();
    }

    @Override // org.webrtc.NetworkChangeDetector
    public List<NetworkChangeDetector.NetworkInformation> getActiveNetworkList() {
        List<NetworkChangeDetector.NetworkInformation> activeNetworkList = this.connectivityManagerDelegate.getActiveNetworkList();
        if (activeNetworkList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(activeNetworkList);
        WifiDirectManagerDelegate wifiDirectManagerDelegate = this.wifiDirectManagerDelegate;
        if (wifiDirectManagerDelegate != null) {
            arrayList.addAll(wifiDirectManagerDelegate.getActiveNetworkList());
        }
        return arrayList;
    }

    @Override // org.webrtc.NetworkChangeDetector
    public NetworkChangeDetector.ConnectionType getCurrentConnectionType() {
        return getConnectionType(getCurrentNetworkState());
    }

    public NetworkState getCurrentNetworkState() {
        return this.connectivityManagerDelegate.getNetworkState();
    }

    public long getDefaultNetId() {
        return this.connectivityManagerDelegate.getDefaultNetId();
    }

    boolean isReceiverRegisteredForTesting() {
        return this.isRegistered;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        NetworkState currentNetworkState = getCurrentNetworkState();
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            connectionTypeChanged(currentNetworkState);
        }
    }

    void setConnectivityManagerDelegateForTests(ConnectivityManagerDelegate connectivityManagerDelegate) {
        this.connectivityManagerDelegate = connectivityManagerDelegate;
    }

    void setWifiManagerDelegateForTests(WifiManagerDelegate wifiManagerDelegate) {
        this.wifiManagerDelegate = wifiManagerDelegate;
    }

    @Override // org.webrtc.NetworkChangeDetector
    public boolean supportNetworkCallback() {
        return this.connectivityManagerDelegate.supportNetworkCallback();
    }
}
