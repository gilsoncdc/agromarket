package com.gilson.agromarket.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.gilson.agromarket.R;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.fragment.CarinhoFragment;
import com.gilson.agromarket.fragment.CarteiraFragment;
import com.gilson.agromarket.fragment.HomeFragment;
import com.gilson.agromarket.fragment.PerfilFragment;
import com.gilson.agromarket.model.Produto;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    CarinhoFragment carinhoFragment = new CarinhoFragment();
    CarteiraFragment carteiraFragment = new CarteiraFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    private int countItemProdutoCarinho;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home, carinhoFragment);
        fragmentTransaction.commit();

        bottomNavigationView = findViewById(R.id.botton_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.home, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home,homeFragment).commit();
                        return true;
                    case R.id.navigation_carinho:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home,carinhoFragment).commit();
                        return true;
                    case R.id.navigation_carteira:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home,carteiraFragment).commit();
                        return true;
                    case R.id.navigation_perfil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home,perfilFragment).commit();
                        return true;
                }
                return false;
            }
        });


        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        //configurar toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AgroMarket");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_logout:
                lolout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_favorito:
                Intent intentF = new Intent(MainActivity.this, FavoritoActivity.class);
                startActivity(intentF);
                finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private void lolout() {
        try {

            firebaseAuth.signOut();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setCountItemCart(int count){

        countItemProdutoCarinho = count;

        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.navigation_carinho);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(count);

    }

}