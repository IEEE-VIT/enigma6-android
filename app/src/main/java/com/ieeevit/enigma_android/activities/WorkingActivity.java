package com.ieeevit.enigma_android.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ieeevit.enigma_android.utils.EnigmaStatus;
import com.ieeevit.enigma_android.fragments.ChangeUsernameBottomSheetFragment;
import com.ieeevit.enigma_android.fragments.GameFragment;
import com.ieeevit.enigma_android.fragments.HintBottomSheet;
import com.ieeevit.enigma_android.fragments.LogoutBottomSheet;
import com.ieeevit.enigma_android.fragments.ScrollableLeaderboardFragment;
import com.ieeevit.enigma_android.fragments.ScrollableProfileFragment;
import com.ieeevit.enigma_android.fragments.ScrollableRulesFragment;
import com.ieeevit.enigma_android.interfaces.UpdateUsernameInterface;
import com.ieeevit.enigma_android.interfaces.WorkingActivityBottomSheets;
import com.ieeevit.enigma_android.interfaces.OpenDrawerFragments;
import com.ieeevit.enigma_android.models.NetworkStateReceiver;
import com.ieeevit.enigma_android.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.Stack;

public class WorkingActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    static OpenDrawerFragments openDrawerFragments;
    static WorkingActivityBottomSheets openBottomSheets;
    private DrawerLayout drawer;
    private FragmentManager manager;
    private NavigationView navigationView;
    private Stack<Fragment> fragmentStack;
    private ChangeUsernameBottomSheetFragment changeUsernameBottomSheetFragment = new ChangeUsernameBottomSheetFragment();
    private HintBottomSheet hintBottomSheet = new HintBottomSheet();
    private LogoutBottomSheet logoutBottomSheet = new LogoutBottomSheet();
    private Snackbar snackbar;
    private ImageView linkedIn;
    private ImageView facebook;
    private ImageView instagram;
    private ImageView twitter;
    private ImageView github;
    private Intent browserIntent;
    private int itemId;
    private static UpdateUsernameInterface updateUsernameInterface;
    private NetworkStateReceiver networkStateReceiver;
    private int isShown = 1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference enigmaStartedReference;
    private SharedPreferences sharedPreferences;



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


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                switch (itemId)
                {
                    case R.id.item_rules:
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
                        ColorStateList list1 = new ColorStateList(new int[][]{
                                new int[] {-android.R.attr.state_checked},
                                new int[] {android.R.attr.state_checked}
                        },
                                new int[]{
                                        getResources().getColor(R.color.white),
                                        getResources().getColor(R.color.majenta)
                                });
                        navigationView.setItemTextColor(list1);
                        navigationView.setItemIconTintList(list1);
                        break;

                    case R.id.item_leaderboard:
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
                        ColorStateList list2 = new ColorStateList(new int[][]{
                                new int[] {-android.R.attr.state_checked},
                                new int[] {android.R.attr.state_checked}
                        },
                                new int[]{
                                        getResources().getColor(R.color.white),
                                        getResources().getColor(R.color.purple)
                                });
                        navigationView.setItemTextColor(list2);
                        navigationView.setItemIconTintList(list2);
                        break;

                    case R.id.item_profile:
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
                        ColorStateList list3 = new ColorStateList(new int[][]{
                                new int[] {-android.R.attr.state_checked},
                                new int[] {android.R.attr.state_checked}
                        },
                                new int[]{
                                        getResources().getColor(R.color.white),
                                        getResources().getColor(R.color.green)
                                });
                        navigationView.setItemTextColor(list3);
                        navigationView.setItemIconTintList(list3);
                        break;

                    case R.id.item_game:
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
                        ColorStateList list4 = new ColorStateList(new int[][]{
                                new int[] {-android.R.attr.state_checked},
                                new int[] {android.R.attr.state_checked}
                        },
                                new int[]{
                                        getResources().getColor(R.color.white),
                                        getResources().getColor(R.color.yellow)
                                });
                        navigationView.setItemTextColor(list4);
                        navigationView.setItemIconTintList(list4);
                        break;

                    case R.id.item_logout:
                        WorkingActivity.getOpenBottomSheets().OpenLogoutBottomSheet();
                        ColorStateList list5 = new ColorStateList(new int[][]{
                                new int[] {-android.R.attr.state_checked},
                                new int[] {android.R.attr.state_checked}
                        },
                                new int[]{
                                        getResources().getColor(R.color.white),
                                        getResources().getColor(R.color.blue)
                                });
                        navigationView.setItemTextColor(list5);
                        navigationView.setItemIconTintList(list5);
                        break;
                }

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

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
                if(!changeUsernameBottomSheetFragment.isAdded())
                    changeUsernameBottomSheetFragment.show(getSupportFragmentManager(), changeUsernameBottomSheetFragment.getTag());
                else
                    WorkingActivity.getOpenBottomSheets().CloseUserNameBottomSheet();
            }

            @Override
            public void CloseUserNameBottomSheet() {
                changeUsernameBottomSheetFragment.dismiss();
            }

            @Override
            public void OpenHintBottomSheet() {
                if(!hintBottomSheet.isAdded())
                    hintBottomSheet.show(getSupportFragmentManager(), hintBottomSheet.getTag());
                else
                    WorkingActivity.getOpenBottomSheets().CloseHintBottomSheet();
            }

            @Override
            public void CloseHintBottomSheet() {
                hintBottomSheet.dismiss();
            }

            @Override
            public void OpenLogoutBottomSheet() {
                if(!logoutBottomSheet.isAdded())
                    logoutBottomSheet.show(getSupportFragmentManager(), logoutBottomSheet.getTag());
                else
                    WorkingActivity.getOpenBottomSheets().CloseLogoutBottomSheet();
            }

            @Override
            public void CloseLogoutBottomSheet() {
                logoutBottomSheet.dismiss();
            }

            @Override
            public void setChecked(int i) {
                navigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void snackBarInternetShow() {
                    snackbar = Snackbar.make(drawer, "Internet Connection Lost!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorErrorSnackbar));
                    snackbar.show();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    isShown++;
            }

            @Override
            public void snackBarInternetDismiss() {
                if (isShown > 1) {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    Snackbar newSnackbar = Snackbar.make(drawer, "You are now connected!", Snackbar.LENGTH_SHORT);
                    newSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorSuccessSnackbar));
                    newSnackbar.show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    isShown++;
                }
            }
        };



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                itemId = item.getItemId();
                drawer.closeDrawer(Gravity.LEFT, true);
                return true;
            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/ieee-vit-vellore/"));
                startActivity(browserIntent);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/IEEEVIT/"));
                        startActivity(browserIntent);
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ieeevitvellore"));
                startActivity(browserIntent);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ieeevitvellore/"));
                startActivity(browserIntent);
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IEEE-VIT/"));
                startActivity(browserIntent);
            }
        });

    }

    private void intializeViews() {
        drawer = findViewById(R.id.drawer_layout);
        manager = getSupportFragmentManager();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        navigationView = findViewById(R.id.nvView);
        fragmentStack = new Stack<>();
        linkedIn = navigationView.findViewById(R.id.linkedln);
        facebook = navigationView.findViewById(R.id.facebook);
        instagram = navigationView.findViewById(R.id.instagram);
        twitter = navigationView.findViewById(R.id.twitter);
        github = navigationView.findViewById(R.id.github);
        ColorStateList list4 = new ColorStateList(new int[][]{
                new int[] {-android.R.attr.state_checked},
                new int[] {android.R.attr.state_checked}
        },
                new int[]{
                        getResources().getColor(R.color.white),
                        getResources().getColor(R.color.yellow)
                });
        navigationView.setItemTextColor(list4);
        navigationView.setItemIconTintList(list4);
        updateUsernameInterface = new UpdateUsernameInterface() {
            @Override
            public void refresh() {
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
            }
        };

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        firebaseDatabase = FirebaseDatabase.getInstance();
        enigmaStartedReference = firebaseDatabase.getReference().child("state");
        enigmaStartedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EnigmaStatus enigmaStatus = dataSnapshot.getValue(EnigmaStatus.class);
                if(!enigmaStatus.getHasStarted())
                {
                    Intent intent = new Intent(WorkingActivity.this, LaunchingActivity.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("LoggedIn", 0);
                    editor.apply();
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void replace(Fragment fragment) {
        manager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right ,
                    R.anim.exit_left_to_right)
                .replace(R.id.scollable_container, fragment)
                .commit();
    }

    public static OpenDrawerFragments getOpenDrawerFragments() {
        return openDrawerFragments;
    }

    public static WorkingActivityBottomSheets getOpenBottomSheets() {
        return openBottomSheets;
    }

    public static UpdateUsernameInterface getUpdateUsernameInterface() {
        return updateUsernameInterface;
    }


    @Override
    public void networkAvailable() {
        WorkingActivity.getOpenBottomSheets().snackBarInternetDismiss();
    }

    @Override
    public void networkUnavailable() {
        WorkingActivity.getOpenBottomSheets().snackBarInternetShow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }
}
