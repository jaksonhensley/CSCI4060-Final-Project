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

public class cartListRecyclerAdapter extends RecyclerView.Adapter<cartListRecyclerAdapter.ItemHolder> {

    public static final String DEBUG_TAG = "cartListRecyclerAdapter";

    private List<CartItem> cartList;
    private Context context;

    public cartListRecyclerAdapter(List<CartItem> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView itemNameView;
        TextView itemQuantityView;
        TextView itemPriceView;

        public ItemHolder(View itemView ) {
            super(itemView);

            itemNameView = itemView.findViewById( R.id.itemName );
            itemQuantityView = itemView.findViewById( R.id.quantity );
            itemPriceView = itemView.findViewById( R.id.price);

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
        CartItem item = cartList.get(position);

        Log.d( DEBUG_TAG, "onBindViewHolder: " + item );

        String key = item.getKey();
        String itemName = item.getItemName();
        String itemQuantity = item.getQuantity();
        String itemPrice = item.getPrice();

        holder.itemNameView.setText( itemName );
        holder.itemQuantityView.setText( itemQuantity );
        holder.itemPriceView.setText( itemPrice );


        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DEBUG_TAG, "onBindViewHolder: getItemID: " + holder.getItemId() );
                Log.d(DEBUG_TAG, "onBindViewHolder: getAdapterPosition: " + holder.getAdapterPosition() );


                Log.d(DEBUG_TAG, "Item name is: " + itemName);
                EditCartItemDialogFragment editItemFragment = EditCartItemDialogFragment.newInstance( holder.getAdapterPosition(), key, itemName, itemQuantity, itemPrice);

                editItemFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public int getItemCount() { return cartList.size(); }


}
