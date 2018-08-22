package dumyprojects.goodwill.com.dumyprojects;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private ImageView imageView;
private TextView textView;
private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.profilePic);
        textView = (TextView) findViewById(R.id.textDetails);
        button = (Button) findViewById(R.id.logOutBTN);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkedInAUTH();
            }
        });



    }


    public void linkedInAUTH(){

        LISessionManager.getInstance(getApplicationContext()).init(MainActivity.this,buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.

                fetchINFO();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
           Log.e("ERROR OCCURS",error.toString());

            }
        }, true);}

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE,Scope.R_EMAILADDRESS,Scope.R_CONTACTINFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

public void fetchINFO(){
    String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name)";

    APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
    apiHelper.getRequest(this, url, new ApiListener() {
        @Override
        public void onApiSuccess(ApiResponse apiResponse) {
            // Success!

            JSONObject jsonObject=apiResponse.getResponseDataAsJson();
            try {
                String name= jsonObject.getString("firstName");
                String email= jsonObject.getString("emailAddress");
                String pic= jsonObject.getString("pictureOrl");

                Picasso.with(getApplicationContext()).load(pic).into(imageView);
            StringBuilder sb=new StringBuilder();
            sb.append("First Name: "+name);
                sb.append("Email Address: "+email);


            textView.setText(sb);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onApiError(LIApiError liApiError) {
            // Error making GET request!

       // Log.e("LI API ERROR ",LIApiError.getMessage());
        }
    });
}

}





