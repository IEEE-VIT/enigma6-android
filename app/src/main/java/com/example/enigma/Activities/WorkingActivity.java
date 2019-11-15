package com.example.enigma.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import com.example.enigma.Fragments.GameFragment;
import com.example.enigma.Fragments.ScrollableLeaderboardFragment;
import com.example.enigma.Fragments.ScrollablePofileFragment;
import com.example.enigma.Fragments.ScrollableRulesFragment;
import com.example.enigma.Interfaces.OpenDrawerFragments;
import com.example.enigma.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class WorkingActivity extends AppCompatActivity {

    static OpenDrawerFragments openDrawerFragments;
    private DrawerLayout drawer;
    private FragmentManager manager;
    private NavigationView navigationView;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(manager.getBackStackEntryCount()==0)
        {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working_activity);
        intializeViews();
        transaction(new GameFragment());
        openDrawerFragments = new OpenDrawerFragments() {
            @Override
            public void OpenDrawer() {
                drawer.openDrawer(Gravity.LEFT);
            }
        };
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.item_rules:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        transaction(new ScrollableRulesFragment());
                        break;

                    case R.id.item_leaderboard:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        transaction(new ScrollableLeaderboardFragment());
                        break;

                    case R.id.item_profile:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        transaction(new ScrollablePofileFragment());
                        break;

                    case R.id.item_game:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        transaction(new GameFragment());
                        break;

                    case R.id.item_logout:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                }
                return true;
            }
        });
    }

    private void intializeViews() {
        drawer = findViewById(R.id.drawer_layout);
        manager = getSupportFragmentManager();
        navigationView = findViewById(R.id.nvView);
    }

    private void transaction(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.scollable_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static OpenDrawerFragments getOpenDrawerFragments() {
        return openDrawerFragments;
    }
}
