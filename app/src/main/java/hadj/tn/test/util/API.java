package hadj.tn.test.util;

import hadj.tn.test.Model.User;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {

    @POST("api/auth/register")
    Call<ResponseBody> createUser (
            @Body User user
    );
    @POST("api/auth/login")
    Call<ResponseBody> checkUser (
            @Body User user
    );
    @GET("api/auth/info/{email}")
    Call<User> getUser(@Path("email") String email);

    @POST("/api/auth/update")
    Call<ResponseBody> updateUser(@Body User user);

}

