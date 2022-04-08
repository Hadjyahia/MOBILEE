package hadj.tn.test.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import hadj.tn.test.Model.User;
import hadj.tn.test.R;
import hadj.tn.test.util.API;
import hadj.tn.test.util.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsFragment extends Fragment {

    TextView textViewName,textViewPhone;
    TextView textViewEmail;
    ImageView imageProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitClient retrofitClient = new RetrofitClient();
        API userApi = retrofitClient.getRetrofit().create(API.class);
        String email = getArguments().getString("User");

        getInfoUser(view,userApi,email);

        editUserName(view,userApi,email);
        editEmail(view,userApi,email);
        editPhone(view,userApi,email);

        selectImageFromGallery(view);

    }

    private void editPhone(View view, API userApi, String email) {


            ImageView editPhone = view.findViewById(R.id.editPhone);
            editPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogEditPhone = new AlertDialog.Builder(getActivity());

                    final View customLayout
                            = getLayoutInflater()
                            .inflate(
                                    R.layout.custom_dialog,
                                    null);

                    dialogEditPhone.setView(customLayout);

                    AlertDialog dialog = dialogEditPhone.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    Button ok = customLayout.findViewById(R.id.okEditPhone);
                    Button cancel = customLayout.findViewById(R.id.cancelEditPhone);
                    EditText editTextPhone = customLayout.findViewById(R.id.editTextPhone);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String myPhone = editTextPhone.getText().toString();

                            if (myPhone.length() == 0) {
                                textViewPhone.setText(textViewPhone.getText());
                            } else if(!myPhone.matches("[0-9]{8}$")) {
                                textViewPhone.setText(textViewPhone.getText());
                                Toast.makeText(getActivity(), "Enter valid email", Toast.LENGTH_LONG).show();
                            } else {
                                textViewPhone.setText(myPhone);
                                if (getArguments() != null) {

                                    User user = new User();
                                    user.setEmail(email);
                                    user.setPhone(myPhone);
                                    userApi.updateUser(user)
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.code() == 200) {
                                                        Toast.makeText(getActivity(), "phone tbadal", Toast.LENGTH_LONG).show();
                                                        dialog.cancel();
                                                    }
                                                    else Toast.makeText(getActivity(), "phone matbadalch", Toast.LENGTH_LONG).show();
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(getActivity(), "cvppppppp", Toast.LENGTH_LONG).show();

                                                }
                                            });
                                }
                            }

                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                  /*

                    dialogEditPhone.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String myPhone = phoneUser.getText().toString();
                            if (myPhone.length() == 0) {
                                textViewPhone.setText(textViewPhone.getText());
                            } else if(!myPhone.matches("[0-9]{8}$")) {
                                textViewPhone.setText(textViewPhone.getText());
                                Toast.makeText(getActivity(), "Enter valid email", Toast.LENGTH_LONG).show();
                            } else {
                                textViewPhone.setText(myPhone);
                                if (getArguments() != null) {

                                    User user = new User();
                                    user.setEmail(email);
                                    user.setPhone(myPhone);
                                    userApi.updateUser(user)
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.code() == 200) {
                                                        Toast.makeText(getActivity(), "phone tbadal", Toast.LENGTH_LONG).show();
                                                    }
                                                    else Toast.makeText(getActivity(), "phone matbadalch", Toast.LENGTH_LONG).show();
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(getActivity(), "cvppppppp", Toast.LENGTH_LONG).show();

                                                }
                                            });
                                }
                            }
                        }
                    });
                    dialogEditPhone.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialogEditPhone.show();*/
                }
            });

    }

    private void editEmail(View view, API userApi, String email) {

        ImageView editEmail = view.findViewById(R.id.editEmail);
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogEditEmail = new AlertDialog.Builder(getActivity());
                dialogEditEmail.setTitle("Change your Email");

                final EditText emailUser = new EditText(getActivity());
                emailUser.setInputType(InputType.TYPE_CLASS_TEXT);

                dialogEditEmail.setView(emailUser);

                dialogEditEmail.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String myEmail = emailUser.getText().toString();
                        if (myEmail.length() == 0) {
                            textViewEmail.setText(textViewEmail.getText());
                        } else if(!Patterns.EMAIL_ADDRESS.matcher(myEmail).matches()) {
                            textViewEmail.setText(textViewEmail.getText());
                            Toast.makeText(getActivity(), "Enter valid email", Toast.LENGTH_LONG).show();
                        } else {
                            textViewEmail.setText(myEmail);
                            if (getArguments() != null) {

                                User user = new User();
                                user.setEmail(myEmail); //////////////////////////////// kifeechh !!!!
                                userApi.updateUser(user)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.code() == 200) {
                                                    Toast.makeText(getActivity(), "email tbadal", Toast.LENGTH_LONG).show();
                                                }
                                                else Toast.makeText(getActivity(), "email matbadalch", Toast.LENGTH_LONG).show();
                                            }
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(getActivity(), "cvppppppp", Toast.LENGTH_LONG).show();

                                            }
                                        });
                            }
                        }
                    }
                });
                dialogEditEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogEditEmail.show();
            }
        });
    }

    private void selectImageFromGallery(View view) {

        Button buttonEditPhoto = view.findViewById(R.id.editProfilePhoto);
        imageProfile = view.findViewById(R.id.profilePhoto);

        buttonEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    private void editUserName(View view,API userApi, String email) {

        ImageView editName = view.findViewById(R.id.editName);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogEditName = new AlertDialog.Builder(getActivity());
                dialogEditName.setTitle("Change your username");

                final EditText username = new EditText(getActivity());
                username.setInputType(InputType.TYPE_CLASS_TEXT);

                dialogEditName.setView(username);

                dialogEditName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String myText = username.getText().toString();
                        if (myText.length() == 0) {
                            textViewName.setText(textViewName.getText());
                        } else if (!myText.matches("([a-zA-Z]+(\\s)*(\\s[a-zA-Z]+)*)+")) {
                            textViewName.setText(textViewName.getText());
                            Toast.makeText(getActivity(), "Enter only alphabetic characters and spaces", Toast.LENGTH_LONG).show();
                        } else {
                            textViewName.setText(myText);
                            if (getArguments() != null) {

                                User user = new User();
                                user.setEmail(email);
                                user.setUsername(myText);
                                userApi.updateUser(user)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.code() == 200) {
                                                    Toast.makeText(getActivity(), "cvvvvv", Toast.LENGTH_LONG).show();
                                                }
                                                else Toast.makeText(getActivity(), "haja mch nrml", Toast.LENGTH_LONG).show();
                                            }
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(getActivity(), "cvppppppp", Toast.LENGTH_LONG).show();

                                            }
                                        });
                            }
                        }
                    }
                });
                dialogEditName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogEditName.show();
            }
        });
    }

    private void getInfoUser(View view,API userApi, String email) {


        if (getArguments() != null) {

            Call<User> call = userApi.getUser(email);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    User getuser = response.body();

                    assert getuser != null;
                    String personName = getuser.getUsername();
                    String personEmail = getuser.getEmail();
                    String personPhone = getuser.getPhone();
                    String personRegion = getuser.getRegion();

                    textViewName = view.findViewById(R.id.textViewName);
                    textViewEmail = view.findViewById(R.id.textViewEmail);
                    textViewPhone = view.findViewById(R.id.textViewPhone);
                    TextView textViewRegion = view.findViewById(R.id.textViewRegion);
                    ImageView imageView = view.findViewById(R.id.profilePhoto);

                    textViewName.setText(personName);
                    textViewEmail.setText(personEmail);
                    textViewPhone.setText(personPhone);
                    textViewRegion.setText(personRegion);

                    if (getuser.getImage()!=null){
                        String personImage = getuser.getImage();

                        byte[] bytes = Base64.decode(personImage,Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Log error here since request failed
                }
            });
        }
    }

    private void imageChooser() {
        ImagePicker.with(this)
                .crop()
                .start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri selectedImageUri = data.getData();
        imageProfile.setImageURI(selectedImageUri);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //Toast.makeText(requireActivity(), imageString, Toast.LENGTH_LONG).show();
        System.out.println(imageString);
        RetrofitClient retrofitClient = new RetrofitClient();
        API userApi = retrofitClient.getRetrofit().create(API.class);
        String email = getArguments().getString("User");

        if (getArguments() != null) {

            User user = new User();
            user.setEmail(email);
            user.setImage(imageString);

            userApi.updateUser(user)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getActivity(), "cvvvvv", Toast.LENGTH_LONG).show();
                            }
                            else Toast.makeText(getActivity(), "haja mch nrml", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getActivity(), "cvppppp", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}