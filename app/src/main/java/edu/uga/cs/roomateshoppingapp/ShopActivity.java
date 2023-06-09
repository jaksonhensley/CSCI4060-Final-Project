package edu.uga.cs.roomateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity
        implements MoveToCartDialogFragment.MoveToCartDialogListener {

    public static final String DEBUG_TAG = "ShopScreenActivity";
    private RecyclerView recyclerView;
    private shoppingListRecyclerAdapter recyclerAdapter;
    private List<ShoppingItem> shoppingList;
    private List<CartItem> cartList;
    private FirebaseDatabase db;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private NavigationView navigationView;
    private Menu menu;
    private MenuItem logoutButton;
    private MenuItem shopButton;
    private MenuItem purchasedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d( DEBUG_TAG, "onCreate()" );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        recyclerView = findViewById( R.id.recyclerView );

        // Find and give function to our Add Item button
        Button viewCart = findViewById( R.id.viewCart);
        viewCart.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        // initialize an empty shopping list
        shoppingList = new ArrayList<ShoppingItem>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        recyclerAdapter = new shoppingListRecyclerAdapter( shoppingList, ShopActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

        db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference( "Items" );

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

        purchasedButton = menu.findItem(R.id.nav_purchased);
        purchasedButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), PurchasedActivity.class);
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
                    .child("Cart")
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

            shoppingList.remove(position);

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

