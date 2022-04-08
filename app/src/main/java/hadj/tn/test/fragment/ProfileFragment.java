package hadj.tn.test.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.Objects;

import hadj.tn.test.Model.User;
import hadj.tn.test.R;
import hadj.tn.test.SignUpActivity;
import hadj.tn.test.util.API;
import hadj.tn.test.util.RealPathUtil;
import hadj.tn.test.util.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    ImageView imageViewCam,imageProfile;

    private Bitmap bitmap;
    private String path;

    String email = getArguments().getString("User");
    RetrofitClient retrofitClient = new RetrofitClient();
    API userApi = retrofitClient.getRetrofit().create(API.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){

            Call<User> call = userApi.getUser(email);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    User user = response.body();

                    assert user != null;
                    String personName = user.getUsername();
                    String personEmail = user.getEmail();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = data.getData();
        imageProfile.setImageURI(selectedImageUri);
    }

}