package edu.uga.cs.roomateshoppingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class MoveToCartDialogFragment extends DialogFragment {

    final private String DEBUG_TAG = "MoveToCartDialog";
    public static final int MOVE = 1;
    public static final int DELETE = 2;

    private TextView itemNameView;
    private EditText itemQuantityView;
    private EditText itemPriceView;

    int position;
    String key;
    String itemName;
    String itemQuantity;
    String itemPrice;

    public interface MoveToCartDialogListener {

        void updateItem(int position, CartItem item, int action);
    }


    public static MoveToCartDialogFragment newInstance(int position, String key, String itemName, String itemQuantity, String itemPrice) {

        MoveToCartDialogFragment dialog = new MoveToCartDialogFragment();

        Bundle args = new Bundle();
        args.putString("key", key);
        args.putString("itemName", itemName);
        args.putString("itemPrice", itemPrice);
        args.putString("itemQuantity", itemQuantity);
        args.putInt("position", position);
        dialog.setArguments(args);

        return dialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {

        key = getArguments().getString("key");
        position = getArguments().getInt("position");
        itemName = getArguments().getString("itemName");
        Log.d(DEBUG_TAG, "Item name is: " + itemName);
        itemQuantity = getArguments().getString("itemQuantity");
        itemPrice = getArguments().getString("itemPrice");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate( R.layout.move_to_cart_dialog, getActivity().findViewById(R.id.root));

        itemNameView = layout.findViewById( R.id.itemNameView );
        itemQuantityView = layout.findViewById( R.id.quantityView );
        itemPriceView = layout.findViewById( R.id.priceView);


        itemNameView.setText(itemName);
        itemQuantityView.setText(itemQuantity);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setView(layout);

        builder.setTitle("Move item to cart");

        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("MOVE", new MoveButtonClickListener() );

        builder.setNeutralButton("DELETE", new DeleteButtonClickListener() );

        return builder.create();

    }


    private class MoveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
         String itemName = itemNameView.getText().toString();
         String itemQuantity = itemQuantityView.getText().toString();
         String itemPrice = itemPriceView.getText().toString();

         CartItem item = new CartItem(key, itemName, itemQuantity, itemPrice);
         item.setKey( key );

         MoveToCartDialogListener listener = (MoveToCartDialogFragment.MoveToCartDialogListener) getActivity();

         listener.updateItem( position, item, MOVE );

         dismiss();

        }
    }


    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick( DialogInterface dialog, int which) {

            String itemName = itemNameView.getText().toString();
            String itemQuantity = itemQuantityView.getText().toString();
            String itemPrice = itemPriceView.getText().toString();

            CartItem item = new CartItem(itemName, itemQuantity, itemPrice);

            item.setKey( key );

            MoveToCartDialogFragment.MoveToCartDialogListener listener = (MoveToCartDialogFragment.MoveToCartDialogListener) getActivity();

            listener.updateItem(position, item, DELETE);
            dismiss();

        }
    }
}