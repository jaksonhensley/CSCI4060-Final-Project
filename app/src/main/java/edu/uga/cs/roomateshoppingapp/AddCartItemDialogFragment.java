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

public class AddCartItemDialogFragment extends DialogFragment {

    final private String DEBUG_TAG = "AddCartItemDialog";
    private EditText itemNameView;
    private EditText itemQuantityView;
    private EditText itemPriceView;

    public interface AddCartItemDialogListener {
        void addCartItem(CartItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_to_cart_dialog, getActivity().findViewById(R.id.root));

        itemNameView = layout.findViewById( R.id.itemNameView);
        itemQuantityView = layout.findViewById( R.id.quantityView);
        itemPriceView = layout.findViewById( R.id.priceView);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);

        builder.setView(layout);

        builder.setTitle("New Cart Item");

        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton( android.R.string.ok, new AddCartItemListener() );

        return builder.create();

    }

    private class AddCartItemListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String itemName = itemNameView.getText().toString();
            String itemQuantity = itemQuantityView.getText().toString();
            String itemPrice = itemPriceView.getText().toString();

            Log.d(DEBUG_TAG, "Cart Item name is: " + itemName);

            CartItem item = new CartItem(itemName, itemQuantity, itemPrice);

            AddCartItemDialogListener listener = (AddCartItemDialogListener) getActivity();

            listener.addCartItem(item);

            dismiss();

        }
    }
}