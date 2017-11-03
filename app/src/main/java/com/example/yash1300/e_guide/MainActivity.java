package com.example.yash1300.e_guide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
EditText email, password;
    Button login;
    TextView register;
    ProgressDialog progressDialog;

    private static final String AUTH_URL = "http://192.168.43.76:8002/users/authenticate";


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.authRegister);
        register.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });


        email = (EditText) findViewById(R.id.authemail);
        password = (EditText) findViewById(R.id.authPassword);
        login = (Button) findViewById(R.id.authLogin);

        login.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(final View v) {

                final String emailText = email.getText().toString();
                final String passString = password.getText().toString();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Logging In...");
                progressDialog.setCancelable(false);
                progressDialog.show();


                if (emailText.isEmpty() || passString.isEmpty()) {
                    progressDialog.dismiss();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(login);
                    Snackbar.make(v, "Please fill all the details", Snackbar.LENGTH_LONG).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AUTH_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String success = jsonObject.getString("Success");
                                String message = jsonObject.getString("Message");
                                if (success.equals("0")){
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                                } else {
                                    if (jsonObject.getString("Profession").equals("0")){
                                        Intent i0 = new Intent(MainActivity.this, Student.class);
                                        i0.putExtra("email", email.getText().toString());
                                        startActivity(i0);
                                    } else {
                                        Intent i0 = new Intent(MainActivity.this, Teacher.class);
                                        i0.putExtra("email", email.getText().toString());
                                        startActivity(i0);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressDialog.dismiss();
                            volleyError.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", emailText);
                            params.put("password", passString);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(MainActivity.this).add(stringRequest);
                }
            }
        });

    }


}
