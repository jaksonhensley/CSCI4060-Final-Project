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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class PurchasedActivity extends AppCompatActivity
        implements MoveToCartDialogFragment.MoveToCartDialogListener {

    public static final String DEBUG_TAG = "PurchaseActivity";
    private RecyclerView recyclerView;
    private purchasedRecyclerAdapter recyclerAdapter;
    private List<PurchasedItem> purchasedList;
    private FirebaseDatabase db;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private NavigationView navigationView;
    private Menu menu;
    private MenuItem logoutButton;
    private MenuItem shopButton;
    private Double total = 0.0, split = 0.0, price = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d( DEBUG_TAG, "onCreate()" );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased);

        recyclerView = findViewById( R.id.purchasedRecyclerView );

        // Find and give function to our Add Item button
        Button bottomShopButton = findViewById( R.id.purchasedScreenHomeButton);
        bottomShopButton.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        // initialize an empty shopping list
        purchasedList = new ArrayList<PurchasedItem>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );


        db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference( "Purchased" );

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchasedList.clear();

                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    PurchasedItem item = postSnapshot.getValue(PurchasedItem.class);
                    item.setKey( postSnapshot.getKey() );
                    String person = postSnapshot.child("firstName").getValue(String.class);
                    item.setPerson(person);
                    purchasedList.add( item );
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

        Button settleCosts = findViewById( R.id.settleCostButton );

        settleCosts.setOnClickListener( new View.OnClickListener () {
            int totalRows = 0;
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                DatabaseReference purchasedReference = db.getReference( "Purchased" );
                FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // iterate through all child nodes and increment totalRows counter
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            totalRows++;
                        }

                        // display total number of rows (optional)
                        Log.d("Total Rows", String.valueOf(totalRows));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // handle error here
                        Log.e("Database Error", "Error occurred while reading data from database: " + databaseError.getMessage());
                    }
                });



                purchasedReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            String itemKey = itemSnapshot.getKey();
                            PurchasedItem item = itemSnapshot.getValue(PurchasedItem.class);

                            // Add price of item to user for tab
                            price = Double.valueOf(item.getPrice());

                            //Add up total prices for split
                            total = (total + price);



                            // remove the item from the "Cart" collection
                            purchasedReference.child(itemKey).removeValue();
                        }

                        FirebaseHelper fbh = new FirebaseHelper();
                        Double users = Double.valueOf(fbh.getTotalUsers());

                        split = (total / totalRows);


                        Toast.makeText(getApplicationContext(), "Total Purchase Price " + split, Toast.LENGTH_SHORT).show();
                        Log.d(DEBUG_TAG,"Split Cost : ( Total: " + total + " )/( Users: " + users + " ) = " + split );
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(DEBUG_TAG, "Error retrieving Cart items: " + databaseError.getMessage());
                        Toast.makeText(getApplicationContext(), "Error retrieving Cart items", Toast.LENGTH_SHORT).show();
                    }
                });

                //Toast.makeText(getApplicationContext(), "Purchase successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });




        recyclerAdapter = new purchasedRecyclerAdapter( purchasedList, PurchasedActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

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
                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
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


    public void updateItem( int position, CartItem item, int action ) {
        if( action == MoveToCartDialogFragment.MOVE ) {
            Log.d( DEBUG_TAG, "Moving item at: " + position + "(" + item.getItemName() + ")" );

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
                            Log.d( DEBUG_TAG, "Updated item at: " + position + "(" + item.getItemName() + ")" );

                            Toast.makeText(getApplicationContext(), item.getItemName() + " updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d( DEBUG_TAG, "failed to update " + item.getItemName());

                    Toast.makeText(getApplicationContext(), "Failed to update " + item.getItemName(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else if( action == EditItemDialogFragment.DELETE ) {
            Log.d( DEBUG_TAG, "Deleting item at: " + position + "(" + item.getItemName() + ")" );

            purchasedList.remove(position);

            recyclerAdapter.notifyItemRemoved( position );

            DatabaseReference reference = db
                    .getReference()
                    .child("Items")
                    .child( item.getKey() );

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
} // HomeActivity Class

