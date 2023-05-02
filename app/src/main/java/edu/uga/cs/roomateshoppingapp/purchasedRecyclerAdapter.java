package edu.uga.cs.roomateshoppingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class purchasedRecyclerAdapter extends RecyclerView.Adapter<purchasedRecyclerAdapter.ItemHolder> {

    public static final String DEBUG_TAG = "purchaseRecyclerAdapter";

    private List<PurchasedItem> purchasedList;
    private Context context;

    public purchasedRecyclerAdapter(List<PurchasedItem> purchasedList, Context context) {
        this.purchasedList = purchasedList;
        this.context = context;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView itemNameView;
        TextView itemQuantityView;
        TextView itemPersonView;

        public ItemHolder(View itemView ) {
            super(itemView);

            itemNameView = itemView.findViewById( R.id.itemName );
            itemQuantityView = itemView.findViewById( R.id.quantity );
            itemPersonView = itemView.findViewById( R.id.person);

        }
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext())
                .inflate( R.layout.cart_item, parent, false);
        return new ItemHolder( view );
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position ) {
        PurchasedItem item = purchasedList.get(position);

        Log.d( DEBUG_TAG, "onBindViewHolder: " + item );

        String key = item.getKey();
        String itemName = item.getItemName();
        String itemQuantity = item.getQuantity();
        String itemPerson = item.getPerson();

        holder.itemNameView.setText( itemName );
        holder.itemQuantityView.setText( itemQuantity );
        holder.itemPersonView.setText( itemPerson );


        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DEBUG_TAG, "onBindViewHolder: getItemID: " + holder.getItemId() );
                Log.d(DEBUG_TAG, "onBindViewHolder: getAdapterPosition: " + holder.getAdapterPosition() );


                Log.d(DEBUG_TAG, "Item name is: " + itemName);
                MoveToCartDialogFragment moveItemFragment = MoveToCartDialogFragment.newInstance( holder.getAdapterPosition(), key, itemName, itemQuantity, itemPerson);

                moveItemFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public int getItemCount() { return purchasedList.size(); }


}
