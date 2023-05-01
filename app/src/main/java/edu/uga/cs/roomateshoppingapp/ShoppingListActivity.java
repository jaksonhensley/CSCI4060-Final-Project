package edu.uga.cs.roomateshoppingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ShoppingListActivity
        extends AppCompatActivity
        implements AddItemDialogFragment.AddItemDialogListener,
        EditItemDialogFragment.EditItemDialogListener {


    public static final String DEBUG_TAG = "ShoppingListActivity";

    //Initialize recycler View and Adapter
    private RecyclerView recyclerView;
    private ListRecyclerAdapter recyclerAdapter;

    //Initialize shopping item list
    private List<ShoppingItem> shoppingList;

    //Initialize database connection;
    private FirebaseDatabase db;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        Log.d( DEBUG_TAG, "onCeate()" );

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shopping_list );

        recyclerView = findViewById( R.id.recyclerView );

        // Find and give function to our Add Item button
        FloatingActionButton addButton = findViewById( R.id.floatingActionButton );
        addButton.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick( View view) {
                DialogFragment newFragment = new AddItemDialogFragment();
                newFragment.show( getSupportFragmentManager(), null );
            }
        });

        // initialize an empty shopping list
        shoppingList = new ArrayList<ShoppingItem>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        recyclerAdapter = new ListRecyclerAdapter( shoppingList, ShoppingListActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

        db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference( "Items" );


        //Listener to update shown shopping list when database changes
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingList.clear();

                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    ShoppingItem item = postSnapshot.getValue(ShoppingItem.class);
                    item.setKey( postSnapshot.getKey() );
                    shoppingList.add( item );
                    Log.d( DEBUG_TAG, "ValueEventListener: Added: " + item );
                    Log.d( DEBUG_TAG, "ValueEventListener: key: " + postSnapshot.getKey() );
                }

                Log.d( DEBUG_TAG, "ValueEventListener: notifying recyclerAdapter" );
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println( "ValueEventListener: reading failed: " + error.getMessage() );
            }
        });
    }

    //Method to add items to the shopping list database
    public void addItem(ShoppingItem item) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("Items");


        reference.push().setValue( item )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        recyclerView.post( new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition( shoppingList.size()-1 );
                            }
                        });

                        Log.d( DEBUG_TAG, "Item saved: " + item );

                        Toast.makeText(getApplicationContext(), "Shopping item created for " + item.getItemName(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( getApplicationContext(), "Failed to create Shopping Item for " + item.getItemName(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Method to update an existing item in the shopping list
    public void updateItem( int position, ShoppingItem item, int action ) {
        if( action == EditItemDialogFragment.SAVE ) {
            Log.d( DEBUG_TAG, "Updating item at: " + position + "(" + item.getItemName() + ")" );

            recyclerAdapter.notifyItemChanged( position );

            DatabaseReference reference = db
                    .getReference()
                    .child("Items")
                    .child( item.getKey() );

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().setValue( item ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d( DEBUG_TAG, "updated item at: " + position + "(" + item.getItemName() + ")" );

                            Toast.makeText(getApplicationContext(), "Item updated for " + item.getItemName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d( DEBUG_TAG, "failed to update item at: " + position + "(" + item.getItemName() + ")" );

                    Toast.makeText(getApplicationContext(), "Failed to update " + item.getItemName(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else if( action == EditItemDialogFragment.DELETE ) {
            Log.d( DEBUG_TAG, "Deleting item at: " + position + "(" + item.getItemName() + ")" );

            shoppingList.remove(position);

            recyclerAdapter.notifyItemRemoved( position );

            DatabaseReference reference = db
                    .getReference()
                    .child("Items" )
                    .child(item.getKey() );

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d( DEBUG_TAG, "deleted item at: " + position + "(" + item.getItemName() + ")" );

                                    Toast.makeText(getApplicationContext(), "Item deleted for " + item.getItemName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(DEBUG_TAG, "failed to delete item at " + position + "(" + item.getItemName() + ")" );

                    Toast.makeText(getApplicationContext(), "Failed to delete " + item.getItemName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
