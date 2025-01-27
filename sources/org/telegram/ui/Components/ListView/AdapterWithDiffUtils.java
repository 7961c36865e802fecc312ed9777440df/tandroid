package org.telegram.ui.Components.ListView;

import androidx.recyclerview.widget.DiffUtil;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;

/* loaded from: classes3.dex */
public abstract class AdapterWithDiffUtils extends RecyclerListView.SelectionAdapter {
    DiffUtilsCallback callback = new DiffUtilsCallback();

    private class DiffUtilsCallback extends DiffUtil.Callback {
        ArrayList newItems;
        ArrayList oldItems;

        private DiffUtilsCallback() {
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return ((Item) this.oldItems.get(i)).compareContents((Item) this.newItems.get(i2));
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            return ((Item) this.oldItems.get(i)).compare((Item) this.newItems.get(i2));
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return this.newItems.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.oldItems.size();
        }

        public void setItems(ArrayList arrayList, ArrayList arrayList2) {
            this.oldItems = arrayList;
            this.newItems = arrayList2;
        }
    }

    public static abstract class Item {
        public boolean selectable;
        public int viewType;

        public Item(int i, boolean z) {
            this.viewType = i;
            this.selectable = z;
        }

        boolean compare(Item item) {
            if (this.viewType != item.viewType) {
                return false;
            }
            return equals(item);
        }

        boolean compareContents(Item item) {
            if (this.viewType != item.viewType) {
                return false;
            }
            return contentsEquals(item);
        }

        protected boolean contentsEquals(Item item) {
            return false;
        }
    }

    public void setItems(ArrayList arrayList, ArrayList arrayList2) {
        if (arrayList2 == null) {
            arrayList2 = new ArrayList();
        }
        this.callback.setItems(arrayList, arrayList2);
        DiffUtil.calculateDiff(this.callback).dispatchUpdatesTo(this);
    }
}
