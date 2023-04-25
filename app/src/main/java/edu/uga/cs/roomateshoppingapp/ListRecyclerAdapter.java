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

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ItemHolder> {

    public static final String DEBUG_TAG = "ListRecyclerAdapter";

    private List<ShoppingItem> shoppingList;
    private Context context;

    public ListRecyclerAdapter( List<ShoppingItem> shoppingList, Context context) {
        this.shoppingList = shoppingList;
        this.context = context;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView itemNameView;
        TextView itemQuantityView;
        TextView itemCommentsView;

        public ItemHolder(View itemView ) {
            super(itemView);

            itemNameView = itemView.findViewById( R.id.itemName );
            itemQuantityView = itemView.findViewById( R.id.quantity );
            itemCommentsView = itemView.findViewById( R.id.comments );

        }
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext())
                .inflate( R.layout.shopping_item, parent, false);
        return new ItemHolder( view );
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position ) {
        ShoppingItem item = shoppingList.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + item );

        String key = item.getKey();
        String itemName = item.getItemName();
        int itemQuantity = item.getQuantity();
        String itemComments = item.getComments();

        holder.itemNameView.setText( itemName );
        holder.itemQuantityView.setText( itemQuantity );
        holder.itemCommentsView.setText( itemComments );


        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DEBUG_TAG, "onBindViewHolder: getItemID: " + holder.getItemId() );
                Log.d(DEBUG_TAG, "onBindViewHolder: getAdapterPosition: " + holder.getAdapterPosition() );

                EditItemDialogFragment editItemFragment = EditItemDialogFragment.newInstance( holder.getAdapterPosition(), key, itemName, itemQuantity, itemComments);

                editItemFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public int getItemCount() { return shoppingList.size(); }


}