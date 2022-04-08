package hadj.tn.test.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import hadj.tn.test.Model.User;
import hadj.tn.test.R;
import hadj.tn.test.util.API;
import hadj.tn.test.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class pfragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get info user

        if (getArguments() != null) {
            String email = getArguments().getString("User");


            RetrofitClient retrofitClient = new RetrofitClient();
            API userApi = retrofitClient.getRetrofit().create(API.class);

            Call<User> call = userApi.getUser(email);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    User getuser = response.body();

                    assert getuser != null;
                    String personName = getuser.getUsername();
                    String personEmail = getuser.getEmail();
                    String personImage = getuser.getImage();

                    if(personImage!=null){
                        byte[] bytes = Base64.decode(personImage,Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        ImageView ImageView = view.findViewById(R.id.profilePhoto);
                        ImageView.setImageBitmap(bitmap);
                    }

                    TextView textViewUsername = view.findViewById(R.id.usernameProfile);
                    TextView textViewEmail = view.findViewById(R.id.emailProfile);

                    textViewUsername.setText(personName);
                    textViewEmail.setText(personEmail);

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Log error here since request failed
                }
            });
        }
    }
}
