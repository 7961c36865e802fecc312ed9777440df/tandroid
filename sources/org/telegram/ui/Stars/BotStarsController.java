package org.telegram.ui.Stars;

import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$StarsTransaction;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_payments_getStarsRevenueStats;
import org.telegram.tgnet.TLRPC$TL_payments_getStarsTransactions;
import org.telegram.tgnet.TLRPC$TL_payments_starsRevenueStats;
import org.telegram.tgnet.TLRPC$TL_payments_starsStatus;
import org.telegram.tgnet.TLRPC$TL_starsRevenueStatus;
import org.telegram.tgnet.TLRPC$TL_updateStarsRevenueStatus;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class BotStarsController {
    private static volatile BotStarsController[] Instance = new BotStarsController[4];
    private static final Object[] lockObjects = new Object[4];
    public final int currentAccount;
    private final HashMap<Long, Long> lastLoadedStats = new HashMap<>();
    private final HashMap<Long, TLRPC$TL_payments_starsRevenueStats> stats = new HashMap<>();
    private final HashMap<Long, TransactionsState> transactions = new HashMap<>();

    static {
        for (int i = 0; i < 4; i++) {
            lockObjects[i] = new Object();
        }
    }

    public static BotStarsController getInstance(int i) {
        BotStarsController botStarsController = Instance[i];
        if (botStarsController == null) {
            synchronized (lockObjects[i]) {
                botStarsController = Instance[i];
                if (botStarsController == null) {
                    BotStarsController[] botStarsControllerArr = Instance;
                    BotStarsController botStarsController2 = new BotStarsController(i);
                    botStarsControllerArr[i] = botStarsController2;
                    botStarsController = botStarsController2;
                }
            }
        }
        return botStarsController;
    }

    private BotStarsController(int i) {
        this.currentAccount = i;
    }

    public long getBalance(long j) {
        TLRPC$TL_payments_starsRevenueStats revenueStats = getRevenueStats(j);
        if (revenueStats == null) {
            return 0L;
        }
        return revenueStats.status.available_balance;
    }

    public boolean isBalanceAvailable(long j) {
        return getRevenueStats(j) != null;
    }

    public TLRPC$TL_payments_starsRevenueStats getRevenueStats(long j) {
        return getRevenueStats(j, false);
    }

    public boolean hasStars(long j) {
        TLRPC$TL_starsRevenueStatus tLRPC$TL_starsRevenueStatus;
        TLRPC$TL_payments_starsRevenueStats revenueStats = getRevenueStats(j);
        return (revenueStats == null || (tLRPC$TL_starsRevenueStatus = revenueStats.status) == null || (tLRPC$TL_starsRevenueStatus.available_balance <= 0 && tLRPC$TL_starsRevenueStatus.overall_revenue <= 0 && tLRPC$TL_starsRevenueStatus.current_balance <= 0)) ? false : true;
    }

    public void preloadRevenueStats(long j) {
        Long l = this.lastLoadedStats.get(Long.valueOf(j));
        this.stats.get(Long.valueOf(j));
        getRevenueStats(j, l == null || System.currentTimeMillis() - l.longValue() > 30000);
    }

    public TLRPC$TL_payments_starsRevenueStats getRevenueStats(final long j, boolean z) {
        Long l = this.lastLoadedStats.get(Long.valueOf(j));
        TLRPC$TL_payments_starsRevenueStats tLRPC$TL_payments_starsRevenueStats = this.stats.get(Long.valueOf(j));
        if (l == null || System.currentTimeMillis() - l.longValue() > 300000 || z) {
            TLRPC$TL_payments_getStarsRevenueStats tLRPC$TL_payments_getStarsRevenueStats = new TLRPC$TL_payments_getStarsRevenueStats();
            tLRPC$TL_payments_getStarsRevenueStats.dark = Theme.isCurrentThemeDark();
            tLRPC$TL_payments_getStarsRevenueStats.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(j);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getStarsRevenueStats, new RequestDelegate() { // from class: org.telegram.ui.Stars.BotStarsController$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    BotStarsController.this.lambda$getRevenueStats$1(j, tLObject, tLRPC$TL_error);
                }
            });
        }
        return tLRPC$TL_payments_starsRevenueStats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRevenueStats$1(final long j, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.BotStarsController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BotStarsController.this.lambda$getRevenueStats$0(tLObject, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRevenueStats$0(TLObject tLObject, long j) {
        if (tLObject instanceof TLRPC$TL_payments_starsRevenueStats) {
            this.stats.put(Long.valueOf(j), (TLRPC$TL_payments_starsRevenueStats) tLObject);
        } else {
            this.stats.put(Long.valueOf(j), null);
        }
        this.lastLoadedStats.put(Long.valueOf(j), Long.valueOf(System.currentTimeMillis()));
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.botStarsUpdated, Long.valueOf(j));
    }

    public void onUpdate(TLRPC$TL_updateStarsRevenueStatus tLRPC$TL_updateStarsRevenueStatus) {
        if (tLRPC$TL_updateStarsRevenueStatus == null) {
            return;
        }
        long peerDialogId = DialogObject.getPeerDialogId(tLRPC$TL_updateStarsRevenueStatus.peer);
        TLRPC$TL_payments_starsRevenueStats revenueStats = getRevenueStats(peerDialogId, true);
        if (revenueStats != null) {
            revenueStats.status = tLRPC$TL_updateStarsRevenueStatus.status;
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.botStarsUpdated, Long.valueOf(peerDialogId));
        }
        invalidateTransactions(peerDialogId, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TransactionsState {
        private final boolean[] endReached;
        private final boolean[] loading;
        private final String[] offset;
        public final ArrayList<TLRPC$StarsTransaction>[] transactions;
        public final boolean[] transactionsExist;

        private TransactionsState(BotStarsController botStarsController) {
            this.transactions = new ArrayList[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
            this.transactionsExist = new boolean[3];
            this.offset = new String[3];
            this.loading = new boolean[3];
            this.endReached = new boolean[3];
        }
    }

    private TransactionsState getTransactionsState(long j) {
        TransactionsState transactionsState = this.transactions.get(Long.valueOf(j));
        if (transactionsState == null) {
            HashMap<Long, TransactionsState> hashMap = this.transactions;
            Long valueOf = Long.valueOf(j);
            TransactionsState transactionsState2 = new TransactionsState();
            hashMap.put(valueOf, transactionsState2);
            return transactionsState2;
        }
        return transactionsState;
    }

    public ArrayList<TLRPC$StarsTransaction> getTransactions(long j, int i) {
        return getTransactionsState(j).transactions[i];
    }

    public void invalidateTransactions(long j, boolean z) {
        TransactionsState transactionsState = getTransactionsState(j);
        for (int i = 0; i < 3; i++) {
            if (!transactionsState.loading[i]) {
                transactionsState.transactions[i].clear();
                transactionsState.offset[i] = null;
                transactionsState.loading[i] = false;
                transactionsState.endReached[i] = false;
                if (z) {
                    loadTransactions(j, i);
                }
            }
        }
    }

    public void loadTransactions(final long j, final int i) {
        final TransactionsState transactionsState = getTransactionsState(j);
        if (transactionsState.loading[i] || transactionsState.endReached[i]) {
            return;
        }
        transactionsState.loading[i] = true;
        TLRPC$TL_payments_getStarsTransactions tLRPC$TL_payments_getStarsTransactions = new TLRPC$TL_payments_getStarsTransactions();
        tLRPC$TL_payments_getStarsTransactions.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(j);
        tLRPC$TL_payments_getStarsTransactions.inbound = i == 1;
        tLRPC$TL_payments_getStarsTransactions.outbound = i == 2;
        String str = transactionsState.offset[i];
        tLRPC$TL_payments_getStarsTransactions.offset = str;
        if (str == null) {
            tLRPC$TL_payments_getStarsTransactions.offset = "";
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getStarsTransactions, new RequestDelegate() { // from class: org.telegram.ui.Stars.BotStarsController$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotStarsController.this.lambda$loadTransactions$3(transactionsState, i, j, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTransactions$3(final TransactionsState transactionsState, final int i, final long j, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.BotStarsController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BotStarsController.this.lambda$loadTransactions$2(transactionsState, i, tLObject, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTransactions$2(TransactionsState transactionsState, int i, TLObject tLObject, long j) {
        transactionsState.loading[i] = false;
        if (tLObject instanceof TLRPC$TL_payments_starsStatus) {
            TLRPC$TL_payments_starsStatus tLRPC$TL_payments_starsStatus = (TLRPC$TL_payments_starsStatus) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$TL_payments_starsStatus.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_payments_starsStatus.chats, false);
            transactionsState.transactions[i].addAll(tLRPC$TL_payments_starsStatus.history);
            transactionsState.transactionsExist[i] = !transactionsState.transactions[i].isEmpty() || transactionsState.transactionsExist[i];
            transactionsState.endReached[i] = (tLRPC$TL_payments_starsStatus.flags & 1) == 0;
            transactionsState.offset[i] = transactionsState.endReached[i] ? null : tLRPC$TL_payments_starsStatus.next_offset;
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.botStarsTransactionsLoaded, Long.valueOf(j));
        }
    }

    public boolean didFullyLoadTransactions(long j, int i) {
        return getTransactionsState(j).endReached[i];
    }

    public boolean hasTransactions(long j, int i) {
        return !getTransactionsState(j).transactions[i].isEmpty();
    }
}