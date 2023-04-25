package edu.uga.cs.roomateshoppingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddItemDialogFragment extends DialogFragment {

    private EditText itemNameView;
    private EditText itemQuantityView;
    private EditText itemCommentsView;

    public interface AddItemDialogListener {
        void addItem(ShoppingItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_item_dialog, getActivity().findViewById(R.id.root));

        itemNameView = layout.findViewById( R.id.itemNameView);
        itemQuantityView = layout.findViewById( R.id.quantityView);
        itemCommentsView = layout.findViewById( R.id.commentsView);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);

        builder.setView(layout);

        builder.setTitle("New Item");

        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton( android.R.string.ok, new AddItemListener() );

        return builder.create();

    }

    private class AddItemListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String itemName = itemNameView.getText().toString();
            String value = itemQuantityView.getText().toString();
            int itemQuantity = Integer.parseInt(value);
            String itemComments = itemCommentsView.getText().toString();

            ShoppingItem item = new ShoppingItem(itemName, itemQuantity, itemComments);

            AddItemDialogListener listener = (AddItemDialogListener) getActivity();

            listener.addItem(item);

            dismiss();

        }
    }
}