package com.example.enigma.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.enigma.Fragments.ChangeUsernameBottomSheetFragment;
import com.example.enigma.Fragments.GameFragment;
import com.example.enigma.Fragments.HintBottomSheet;
import com.example.enigma.Fragments.LogoutBottomSheet;
import com.example.enigma.Fragments.ScrollableLeaderboardFragment;
import com.example.enigma.Fragments.ScrollableProfileFragment;
import com.example.enigma.Fragments.ScrollableRulesFragment;
import com.example.enigma.Interfaces.WorkingActivityBottomSheets;
import com.example.enigma.Interfaces.OpenDrawerFragments;
import com.example.enigma.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Stack;

public class WorkingActivity extends AppCompatActivity {

    static OpenDrawerFragments openDrawerFragments;
    static WorkingActivityBottomSheets openBottomSheets;
    private DrawerLayout drawer;
    private FragmentManager manager;
    private NavigationView navigationView;
    private Stack<Fragment> fragmentStack;
    private ChangeUsernameBottomSheetFragment changeUsernameBottomSheetFragment = new ChangeUsernameBottomSheetFragment();
    private HintBottomSheet hintBottomSheet = new HintBottomSheet();
    private LogoutBottomSheet logoutBottomSheet = new LogoutBottomSheet();

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

        openBottomSheets = new WorkingActivityBottomSheets() {
            @Override
            public void OpenUserNameBottomSheet() {
                changeUsernameBottomSheetFragment.show(getSupportFragmentManager(), changeUsernameBottomSheetFragment.getTag());
            }

            @Override
            public void CloseUserNameBottomSheet() {
                changeUsernameBottomSheetFragment.dismiss();
            }

            @Override
            public void OpenHintBottomSheet() {
                hintBottomSheet.show(getSupportFragmentManager(), hintBottomSheet.getTag());
            }

            @Override
            public void CloseHintBottomSheet() {
                hintBottomSheet.dismiss();
            }

            @Override
            public void OpenLogoutBottomSheet() {
                logoutBottomSheet.show(getSupportFragmentManager(), logoutBottomSheet.getTag());
            }

            @Override
            public void CloseLogoutBottomSheet() {
                logoutBottomSheet.dismiss();
            }

            @Override
            public void setChecked(int i) {
                navigationView.getMenu().getItem(i).setChecked(true);
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

                        TextView enigma = navigationView.getHeaderView(0).findViewById(R.id.enigma);
                        TextView six = navigationView.getHeaderView(0).findViewById(R.id.six);
                        six.setTextColor(getResources().getColor(R.color.majenta));
                        enigma.setTextColor(getResources().getColor(R.color.majenta));
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
                        enigma = navigationView.getHeaderView(0).findViewById(R.id.enigma);
                        six = navigationView.getHeaderView(0).findViewById(R.id.six);
                        six.setTextColor(getResources().getColor(R.color.purple));
                        enigma.setTextColor(getResources().getColor(R.color.purple));
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
                        enigma = navigationView.getHeaderView(0).findViewById(R.id.enigma);
                        six = navigationView.getHeaderView(0).findViewById(R.id.six);
                        six.setTextColor(getResources().getColor(R.color.green));
                        enigma.setTextColor(getResources().getColor(R.color.green));
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
                        enigma = navigationView.getHeaderView(0).findViewById(R.id.enigma);
                        six = navigationView.getHeaderView(0).findViewById(R.id.six);
                        six.setTextColor(getResources().getColor(R.color.yellow));
                        enigma.setTextColor(getResources().getColor(R.color.yellow));
                        break;

                    case R.id.item_logout:
                        drawer.closeDrawer(Gravity.LEFT, true);
                        WorkingActivity.getOpenBottomSheets().OpenLogoutBottomSheet();
                        break;

                    case R.id.item_facebook:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/IEEEVIT/"));
                        startActivity(browserIntent);
                        break;

                    case R.id.item_instagram:
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ieeevitvellore/"));
                        startActivity(browserIntent);
                        break;

                    case R.id.item_twitter:
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ieeevitvellore"));
                        startActivity(browserIntent);
                        break;

                    case R.id.item_github:
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IEEE-VIT/"));
                        startActivity(browserIntent);
                        break;

                    case R.id.item_linkedln:
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/ieee-vit-vellore/"));
                        startActivity(browserIntent);
                        break;
                }
                return true;
            }
        });
    }

    private void replace(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.scollable_container, fragment)
                .commit();
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

    public static WorkingActivityBottomSheets getOpenBottomSheets() {
        return openBottomSheets;
    }
}
