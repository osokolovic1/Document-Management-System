package ba.unsa.etf.dms.bp.dms_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ba.unsa.etf.dms.bp.rest.Login;
import ba.unsa.etf.dms.bp.rest.LoginAndRegistration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText userText;
    private EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginButton = (Button)findViewById(R.id.loginBtn);
        userText = (EditText)findViewById(R.id.emailInput);
        passwordText = (EditText)findViewById(R.id.passwordInput);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userText.getText().toString();
                String password = passwordText.getText().toString();
                login(username,password);
            }
        });
    }

    public void showRegistrationForm(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private LoginAndRegistration getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.9:8181/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        final LoginAndRegistration mInterfaceService = retrofit.create(LoginAndRegistration.class);
        return mInterfaceService;
    }

    private void login(final String username, String password){
        LoginAndRegistration mApiService = this.getInterfaceService();
        Call<String> mService = mApiService.authenticate(username, password);
        mService.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String mLoginObject = response.body();
                String returnedResponse = mLoginObject.toString();
                Toast.makeText(LoginActivity.this, "Returned " + returnedResponse, Toast.LENGTH_LONG).show();
                //showProgress(false);
                if(returnedResponse.trim().equals("OK")){
                    // redirect to Main Activity page
                    Intent loginIntent = new Intent(LoginActivity.this, DMSMainActivity.class);
                    loginIntent.putExtra("username", username);
                    startActivity(loginIntent);
                } else {
                    Toast.makeText(LoginActivity.this,"Login error.",Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                Toast.makeText(LoginActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
                Log.v("FAIL",t.getMessage());
            }
        });
    }

}
