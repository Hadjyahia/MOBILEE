package hadj.tn.test;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import de.hdodenhof.circleimageview.CircleImageView;
import hadj.tn.test.Model.User;
import hadj.tn.test.fragment.AboutUsFragment;
import hadj.tn.test.fragment.DonateBloodFragment;
import hadj.tn.test.fragment.EducateFragment;
import hadj.tn.test.fragment.FindDonorFragment;
import hadj.tn.test.fragment.HistoriqueFragment;
import hadj.tn.test.fragment.HomeFragment;
import hadj.tn.test.fragment.NotificationFragment;
import hadj.tn.test.fragment.ProfileFragment;
import hadj.tn.test.fragment.pfragment;
import hadj.tn.test.menu.DrawerAdapter;
import hadj.tn.test.menu.DrawerItem;
import hadj.tn.test.menu.SimpleItem;
import hadj.tn.test.menu.SpaceItem;
import hadj.tn.test.util.API;
import hadj.tn.test.util.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_HOME = 0;
    private static final int POS_PROFILE = 1;
    private static final int POS_NOTIF = 2;
    private static final int POS_HISTO = 3;
    private static final int POS_ABOUT_US = 4;
    private static final int POS_LOGOUT = 5;
    private String[] screenTitles;
    private Drawable[] screenIcons;

    public static Fragment fragment;

    private SlidingRootNav slidingRootNav;
    String title="";
    ChipNavigationBar chipNavigationBar ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textViewtoolbar = findViewById(R.id.textToolbar);
        CircleImageView imageView = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();
                if (extras != null)
                {
                    String email = extras.getString("emailUser");
                    Bundle bundle = new Bundle();
                    bundle.putString("User",email);
                    pfragment fragmentProfile = new pfragment();
                    fragmentProfile.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentProfile).commit();
                }
            }
        });

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME),
                createItemFor(POS_PROFILE),
                createItemFor(POS_NOTIF),
                createItemFor(POS_HISTO),
                createItemFor(POS_ABOUT_US),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        //adapter.setSelected(POS_HOME);

        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        chipNavigationBar = findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.ic_home,true);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String email = extras.getString("emailUser");
            Bundle bundle = new Bundle();
            bundle.putString("User",email);
            HomeFragment fragmentHome = new HomeFragment();
            fragmentHome.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentHome).commit();
        }

       // String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.ic_home:
                        fragment=new HomeFragment();
                        //title = "Home";
                        textViewtoolbar.setText("Home");
                        break;
                    case R.id.ic_finddonor:
                        fragment=new FindDonorFragment();
                        //mTitle.setText("Home");
                        //title = "Find a Donor";
                        textViewtoolbar.setText("Find a Donor");
                        break;
                    case R.id.ic_db:
                        fragment=new DonateBloodFragment();
                        //mTitle.setText("Home");
                        //title = "Donate Blood";
                        textViewtoolbar.setText("Donate Blood");
                        break;
                    case R.id.ic_educ:
                        fragment=new EducateFragment();
                        //mTitle.setText("Home");
                        //title = "Blood donation";
                        textViewtoolbar.setText("Blood donation");
                        break;
                }
                if (fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                    getSupportActionBar().setTitle(title);
                }
            }
        });
    }

    @Override
    public void onItemSelected(int position) {

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textViewtoolbar = findViewById(R.id.textToolbar);
        setSupportActionBar(toolbar);

        if (position == POS_LOGOUT){
            Intent intent = new Intent(HomeActivity.this,Sign_InActivity.class);
            startActivity(intent);
        }
        if (position == POS_HOME) {
            fragment = new HomeFragment();
            //title="Home";
            //mTitle.setText("Home");
            textViewtoolbar.setText("Home");
        }
        if (position == POS_PROFILE) {
            fragment = new pfragment();
            //title = "My Profile";
            //mTitle.setText("Home");
            textViewtoolbar.setText("My Profile");
        }
        if (position == POS_NOTIF) {
            fragment = new NotificationFragment();
           //title = "Notifications";
            //mTitle.setText("Home");
            textViewtoolbar.setText("Notifications");
        }
        if (position == POS_HISTO) {
            fragment = new HistoriqueFragment();
            //title = "Historical";
            //mTitle.setText("Home");
            textViewtoolbar.setText("Historical");
        }
        if (position == POS_ABOUT_US) {
            fragment = new AboutUsFragment();
            //title = "About Us";
            //mTitle.setText("Home");
            textViewtoolbar.setText("About Us");
        }
        slidingRootNav.closeMenu();
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String email = extras.getString("emailUser");
            Bundle bundle = new Bundle();
            bundle.putString("User",email);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            getSupportActionBar().setTitle(title);
        }



    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.teal_700))
                .withSelectedTextTint(color(R.color.teal_700));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}
