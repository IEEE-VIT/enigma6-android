package com.example.enigma.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import com.example.enigma.Fragments.GameFragment;
import com.example.enigma.Fragments.ScrollableLeaderboardFragment;
import com.example.enigma.Fragments.ScrollableProfileFragment;
import com.example.enigma.Fragments.ScrollableRulesFragment;
import com.example.enigma.Interfaces.OpenDrawerFragments;
import com.example.enigma.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Stack;

public class WorkingActivity extends AppCompatActivity {

    static OpenDrawerFragments openDrawerFragments;
    private DrawerLayout drawer;
    private FragmentManager manager;
    private NavigationView navigationView;
    private Stack<Fragment> fragmentStack;


    @Override
    public void onBackPressed() {
        if(fragmentStack.size()==1)
        {
            finish();
        }
        else
        {
            fragmentStack.pop();
            replace(fragmentStack.peek());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working_activity);
        intializeViews();
        fragmentStack.push(new GameFragment());
        replace(fragmentStack.peek());
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
                        if(fragmentStack.size()==2) {
                            fragmentStack.pop();
                            fragmentStack.push(new ScrollableRulesFragment());
                            replace(fragmentStack.peek());
                        }else
                        {
                            fragmentStack.push(new ScrollableRulesFragment());
                            replace(fragmentStack.peek());
                        }
                        break;

                    case R.id.item_leaderboard:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        if(fragmentStack.size()==2)
                        {
                            fragmentStack.pop();
                            fragmentStack.push(new ScrollableLeaderboardFragment());
                            replace(fragmentStack.peek());
                        }
                        else
                        {
                            fragmentStack.push(new ScrollableLeaderboardFragment());
                            replace(fragmentStack.peek());
                        }

                        break;

                    case R.id.item_profile:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        if(fragmentStack.size()==2)
                        {
                            fragmentStack.pop();
                            fragmentStack.push(new ScrollableProfileFragment());
                            replace(fragmentStack.peek());
                        }
                        else
                        {
                            fragmentStack.push(new ScrollableProfileFragment());
                            replace(fragmentStack.peek());
                        }
                        break;

                    case R.id.item_game:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        if(fragmentStack.size()==2)
                        {
                            fragmentStack.pop();
                            fragmentStack.pop();
                            fragmentStack.push(new GameFragment());
                            replace(fragmentStack.peek());

                        }
                        else
                        {
                            fragmentStack.pop();
                            fragmentStack.push(new GameFragment());
                            replace(fragmentStack.peek());
                        }
                        break;

                    case R.id.item_logout:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        new MaterialAlertDialogBuilder(WorkingActivity.this, R.style.DialogBox)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to logout ?")
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseAuth.getInstance().signOut();
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show()
                                .getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));

                }
                return true;
            }
        });
    }

    private void replace(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.scollable_container, fragment)
                .commit();
        int c = 10;
    }

    private void intializeViews() {
        drawer = findViewById(R.id.drawer_layout);
        manager = getSupportFragmentManager();
        navigationView = findViewById(R.id.nvView);
        fragmentStack = new Stack<>();
    }


    public static OpenDrawerFragments getOpenDrawerFragments() {
        return openDrawerFragments;
    }
}
