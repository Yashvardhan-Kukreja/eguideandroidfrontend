package com.example.yash1300.e_guide;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
EditText firstName, lastName, age, email, pass, conpass, phonenum;
    CheckBox skill0,skill1,skill2,skill3,skill4,skill5,skill6,skill7,skill8,skill9,skill10,skill11,skill12, skill13;
    Button register, button;
    JSONObject output;
    ProgressDialog progressDialog;
    RadioGroup radioGroup;
    Integer profession;
    List<Integer> skillArr;
    int skillPermission = 0;
    int k;
    StringBuilder skillsString = new StringBuilder();
    private static final String REG_URL = "http://192.168.43.76:8002/users/register";
    private static final String SKILL_REG_URL = "http://192.168.43.76:8002/users/addskill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        skillArr = new ArrayList<>();

        View rootv = getLayoutInflater().inflate(R.layout.skills_dialog, null,false);
        skill0 = rootv.findViewById(R.id.skill0);
        skill1 = rootv.findViewById(R.id.skill1);
        skill2 = rootv.findViewById(R.id.skill2);
        skill3 = rootv.findViewById(R.id.skill3);
        skill4 = rootv.findViewById(R.id.skill4);
        skill5 = rootv.findViewById(R.id.skill5);
        skill6 = rootv.findViewById(R.id.skill6);
        skill7 = rootv.findViewById(R.id.skill7);
        skill8 = rootv.findViewById(R.id.skill8);
        skill9 = rootv.findViewById(R.id.skill9);
        skill10 = rootv.findViewById(R.id.skill10);
        skill11 = rootv.findViewById(R.id.skill11);
        skill12 = rootv.findViewById(R.id.skill12);
        skill13 = rootv.findViewById(R.id.skill13);
        button = rootv.findViewById(R.id.skillRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skill0.isChecked())
                    skillsString.append("00");
                if (skill1.isChecked())
                    skillsString.append("01");
                if (skill2.isChecked())
                    skillsString.append("02");
                if (skill3.isChecked())
                    skillsString.append("03");
                if (skill4.isChecked())
                    skillsString.append("04");
                if (skill5.isChecked())
                    skillsString.append("05");
                if (skill6.isChecked())
                    skillsString.append("06");
                if (skill7.isChecked())
                    skillsString.append("07");
                if (skill8.isChecked())
                    skillsString.append("08");
                if (skill9.isChecked())
                    skillsString.append("09");
                if (skill10.isChecked())
                    skillsString.append("10");
                if (skill11.isChecked())
                    skillsString.append("11");
                if (skill12.isChecked())
                    skillsString.append("12");
                if (skill13.isChecked())
                    skillsString.append("13");
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, SKILL_REG_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(s);
                            String success = jsonObject1.getString("Success");
                            String message = jsonObject1.getString("Message");
                            if (success.equals("0")){
                                Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Register.this, MainActivity.class));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Register.this, "An error occured", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Register.this, "An error occured", Toast.LENGTH_LONG).show();
                        volleyError.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params1 = new HashMap<>();
                        params1.put("email", email.getText().toString());
                        params1.put("skill", skillsString.toString());
                        return params1;
                    }
                };
                Volley.newRequestQueue(Register.this).add(stringRequest1);
            }
        });
        AlertDialog.Builder skillBuilder = new AlertDialog.Builder(Register.this);
        skillBuilder.setView(rootv);
        final AlertDialog skillAlertDialog = skillBuilder.create();
        skillAlertDialog.setCancelable(false);

        firstName = (EditText) findViewById(R.id.regfirstName);
        lastName = (EditText) findViewById(R.id.reglastName);
        age = (EditText) findViewById(R.id.regage);
        email = (EditText) findViewById(R.id.regemail);
        pass = (EditText) findViewById(R.id.regpassword);
        conpass = (EditText) findViewById(R.id.regconpassword);
        phonenum = (EditText) findViewById(R.id.regphonenum);

        profession = -1;
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radioStud:
                        profession = 0;
                    case R.id.radioTeach:
                        profession = 1;
                }
            }
        });

        register = (Button) findViewById(R.id.register);
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage("Registering you with us...");
        progressDialog.setCancelable(false);

        register.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(final View v) {
                progressDialog.show();
                if (firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || age.getText().toString().isEmpty()
                        || email.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || conpass.getText().toString().isEmpty()
                        || profession == -1){
                    progressDialog.dismiss();
                    Snackbar.make(v, "Please Enter all the details", Snackbar.LENGTH_LONG).show();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(register);
                } else {
                    if (!pass.getText().toString().equals(conpass.getText().toString())){
                        progressDialog.dismiss();
                        Snackbar.make(v, "Password and Confirm password are not same", Snackbar.LENGTH_LONG).show();
                        YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(register);
                    } else {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, REG_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {

                                    JSONObject jsonObject = new JSONObject(s);
                                    String success = jsonObject.getString("Success");
                                    String message = jsonObject.getString("Message");
                                    if (success.equals("0")){
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                                        skillAlertDialog.show();
                                    }
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressDialog.dismiss();
                                volleyError.printStackTrace();
                                Toast.makeText(Register.this, "An error occured at 1", Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params1 = new HashMap<>();
                                params1.put("firstName", firstName.getText().toString());
                                params1.put("lastName", lastName.getText().toString());
                                params1.put("age", age.getText().toString());
                                params1.put("profession", Integer.toString(profession));
                                params1.put("email", email.getText().toString());
                                params1.put("password", pass.getText().toString());
                                params1.put("phonenum", phonenum.getText().toString());
                                return params1;
                            }
                        };
                        Volley.newRequestQueue(Register.this).add(stringRequest);

                    }
                }
            }
        });
    }
}
