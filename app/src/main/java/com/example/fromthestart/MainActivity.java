package com.example.fromthestart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private NavigationView navigationView;
    private NavController navController;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_container);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.loginFragment, R.id.homeFragment, R.id.searchPageFragment,
                R.id.requestFeedFragment, R.id.createPostFragment).setOpenableLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Menu menuNav = navigationView.getMenu();
        if(user == null){
            MenuItem homeItem = menuNav.findItem(R.id.homeFragment);
            MenuItem createPostItem = menuNav.findItem(R.id.createPostFragment);
            MenuItem feedItem = menuNav.findItem(R.id.requestFeedFragment);
            MenuItem searchItem = menuNav.findItem(R.id.searchPageFragment);
            homeItem.setVisible(false);
            createPostItem.setVisible(false);
            feedItem.setVisible(false);
            searchItem.setVisible(false);
        }
        if(user != null){
            menuNav.findItem(R.id.loginFragment).setVisible(false);
            navController.navigate(R.id.homeFragment);
        }


        /*if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
        }*/
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }*/

    public boolean onSupportNavigateUp() {
        //navController.navigateUp();
        return  NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        if(user != null){
            MenuItem menuItem = menu.findItem(R.id.toolbar_username);
            menuItem.setTitle(user.getDisplayName());
        }
        else{
            MenuItem menuItem = menu.findItem(R.id.toolbar_username);
            menuItem.setTitle("");
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                navController.popBackStack(R.id.loginFragment, false);

        }
        return(super.onOptionsItemSelected(item));
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

}