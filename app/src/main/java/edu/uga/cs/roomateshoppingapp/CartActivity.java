package edu.uga.cs.roomateshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity
        implements AddCartItemDialogFragment.AddCartItemDialogListener,
        EditCartItemDialogFragment.EditCartItemDialogListener {

    public static final String DEBUG_TAG = "CartScreenActivity";
    private RecyclerView recyclerView;
    private cartListRecyclerAdapter recyclerAdapter;
    private List<CartItem> cartList;
    private FirebaseDatabase db;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private NavigationView navigationView;
    private Menu menu;
    private MenuItem logoutButton;
    private MenuItem shopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d( DEBUG_TAG, "onCreate()" );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById( R.id.recyclerView );

        // Find and give function to our Add Item button
        FloatingActionButton addButton = findViewById( R.id.floatingActionButton);
        Button viewList = findViewById( R.id.viewList);
        Button checkout = findViewById( R.id.Checkout );

        addButton.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick( View view) {
                DialogFragment newFragment = new AddCartItemDialogFragment();
                newFragment.show( getSupportFragmentManager(), null );
            }
        });

        viewList.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                DatabaseReference cartRef = db.getReference( "Cart" );




                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(intent);
            }
        });

        checkout.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                DatabaseReference cartReference = db.getReference( "Cart" );
                DatabaseReference listReference = db.getReference( "Items" );
                DatabaseReference purchasedReference = db.getReference( "Purchased" );

                cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String userFirstName = currentUser.getDisplayName();

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            String itemKey = itemSnapshot.getKey();
                            Object itemValue = itemSnapshot.getValue();

                            // add the current user's first name to the item
                            if (itemValue instanceof HashMap) {
                                HashMap<String, Object> itemMap = (HashMap<String, Object>) itemValue;
                                itemMap.put("firstName", userFirstName);
                                purchasedReference.child(itemKey).setValue(itemMap);
                            }
                            // remove the item from the "Cart" collection
                            cartReference.child(itemKey).removeValue();
                            // remove the item from the "Items" collection
                            listReference.child(itemKey).removeValue();
                        }
                        Toast.makeText(getApplicationContext(), "Purchase successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(DEBUG_TAG, "Error retrieving Cart items: " + databaseError.getMessage());
                        Toast.makeText(getApplicationContext(), "Error retrieving Cart items", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(getApplicationContext(), "Purchase successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        // initialize an empty shopping list
        cartList = new ArrayList<CartItem>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        recyclerAdapter = new cartListRecyclerAdapter( cartList, CartActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

        db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference( "Cart" );

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();

                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    CartItem item = postSnapshot.getValue(CartItem.class);
                    item.setKey( postSnapshot.getKey() );
                    cartList.add( item );
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

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.home_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.homeScreenMenu);
        menu = navigationView.getMenu();

        logoutButton = menu.findItem(R.id.nav_logout);
        logoutButton.setOnMenuItemClickListener(item -> {
            // Perform logout logic here
            FirebaseAuth.getInstance().signOut();
            // Add any other code you need to handle logout
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            return true;
        });

        shopButton = menu.findItem(R.id.nav_shop);
        shopButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                return true;
            }
        });

    } // onCreate()

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected()

    public void addCartItem(CartItem item) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("Cart");

        reference.push().setValue( item )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d( DEBUG_TAG, "Cart Item saved: " + item );

                        Toast.makeText(getApplicationContext(), item.getItemName() + " added to cart.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( getApplicationContext(), "Failed to create Cart Item for " + item.getItemName(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void updateItem( int position, CartItem item, int action ) {
        if( action == MoveToCartDialogFragment.MOVE ) {
            Log.d( DEBUG_TAG, "Moving item at: " + position + "(" + item.getItemName() + ") to cart" );

            recyclerAdapter.notifyItemChanged( position );

            DatabaseReference reference = db
                    .getReference()
                    .child("Cart")
                    .child( item.getKey() );



            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().setValue( item ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d( DEBUG_TAG, "Moved item at: " + position + "(" + item.getItemName() + ") to cart" );

                            Toast.makeText(getApplicationContext(), item.getItemName() + " moved to cart.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d( DEBUG_TAG, "failed to move " + item.getItemName() + " to cart" );

                    Toast.makeText(getApplicationContext(), "Failed to move " + item.getItemName() + " to cart.", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else if( action == EditItemDialogFragment.DELETE ) {
            Log.d( DEBUG_TAG, "Deleting cart item at: " + position + "(" + item.getItemName() + ")" );

            cartList.remove(position);

            recyclerAdapter.notifyItemRemoved( position );

            DatabaseReference reference = db
                    .getReference()
                    .child("Cart")
                    .child( item.getKey() );

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d( DEBUG_TAG, "deleted cart item at: " + position + "(" + item.getItemName() + ")" );

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
} // HomeActivity Class

