package hadj.tn.test.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Objects;

import hadj.tn.test.HomeActivity;
import hadj.tn.test.Model.User;
import hadj.tn.test.R;
import hadj.tn.test.Sign_InActivity;
import hadj.tn.test.util.API;
import hadj.tn.test.util.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
public class HomeFragment extends Fragment {

    int[] images = {R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10,R.drawable.p11};
    String[] names = {"Aicha Gebsi","Tayssir Massoudi","Amal Gharbi","Mouhemed Ben Salah","Omar Maghrbi","Lotfi chakroun","Mouhamed ben massoud","Lina maria","Salma Selmi"};
    String[] pubcontenu = {"Puis-je donner après avoir fait un tatouage ou un piercing ?","Puis-je donner si je suis diabétique ?","Puis-je donner tout de suite après une opération chirurgicale ou dentaire ? ","Puis-je donner si je prends un traitement médical ? ","Puis-je donner si j’ai eu la dengue ?","Puis-je aller au don de sang après une vaccination contre le coronavirus","Puis-je aller au don de sang même si je souffre d’un refroidissement ?","Après avoir eu la grippe, combien de temps dois-je attendre avant de pouvoir retourner au don de sang ?","Combien de sang contient l’organisme humain ?","En combien de temps l’organisme remplace-t-il le sang prélevé ?"};
    ChipNavigationBar chipNavigationBar ;
    Fragment fragment = null;
    String personName,personEmail;
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitClient retrofitClient = new RetrofitClient();
        API userApi = retrofitClient.getRetrofit().create(API.class);

    if(getArguments() != null){

        String email = getArguments().getString("User");
        Call<User> call = userApi.getUser(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                assert user != null;
                String personName = user.getUsername();
                TextView textViewUsername = view.findViewById(R.id.usernameTextView);
                textViewUsername.setText(personName);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }
        /*Sign_InActivity activity = (Sign_InActivity) getActivity();
          String email = activity.getEmail();


        Call<User> call = userApi.getUser(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                String personName = user.getUsername();
                TextView textViewUsername = view.findViewById(R.id.usernameTextView);
                textViewUsername.setText(personName);

             }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log error here since request failed
            }
        });*/


        VideoView videoView = view.findViewById(R.id.videoweview);
        ImageView imageView = view.findViewById(R.id.img_start_vid);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.video;
                Uri uri = Uri.parse(videoPath);
                videoView.setVideoURI(uri);
                imageView.setVisibility(View.INVISIBLE);
            }
        });

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        RecyclerView recyclerView = view.findViewById(R.id.listPub);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new ListPubAdapter(names,images,pubcontenu));
    }
}