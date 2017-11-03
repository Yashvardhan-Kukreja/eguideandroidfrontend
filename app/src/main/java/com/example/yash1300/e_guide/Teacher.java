package com.example.yash1300.e_guide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher extends AppCompatActivity {
ListView listView;
String emailOfTeacher;
List<String> list;
TextView heading;
ProgressDialog progressDialog, progressDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        progressDialog = new ProgressDialog(Teacher.this);
        progressDialog.setMessage("Loading the list of students interested in you...");
        progressDialog.setCancelable(false);
        progressDialog2 = new ProgressDialog(Teacher.this);
        progressDialog2.setMessage("Loading the details of the student...");
        progressDialog2.setCancelable(false);
        progressDialog.show();
        heading = findViewById(R.id.studentsInterestedText);
        emailOfTeacher = getIntent().getExtras().getString("email");
        list = new ArrayList<>();
        listView = findViewById(R.id.allInterestedStudentsList);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.76:8002/users/getuserdetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String success = jsonObject.getString("Success");
                    String message = jsonObject.getString("Message");
                    if (success.equals("0")){
                        Toast.makeText(Teacher.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONObject("User").getJSONArray("studentsInterested");
                        if (jsonArray.length() == 0){
                            heading.setVisibility(View.INVISIBLE);
                            Toast.makeText(Teacher.this, "No students interested in you as of now", Toast.LENGTH_LONG).show();
                        } else {
                            for (int k=0;k<jsonArray.length();k++){
                                list.add(jsonArray.getString(k));
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Teacher.this, android.R.layout.simple_list_item_1, list);
                                listView.setAdapter(arrayAdapter);
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(Teacher.this, "An error occured", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Teacher.this, "An error occured", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                volleyError.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailOfTeacher);
                return params;
            }
        };
        Volley.newRequestQueue(Teacher.this).add(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String emailOfStudent = list.get(i);
                progressDialog2.show();
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.43.76:8002/users/getuserdetails", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog2.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String success = jsonObject.getString("Success");
                            String message = jsonObject.getString("Message");
                            if (success.equals("0")){
                                Toast.makeText(Teacher.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Teacher.this);
                                View v = getLayoutInflater().inflate(R.layout.student_details_dialog, null, false);
                                TextView name = v.findViewById(R.id.studentDetailsName);
                                TextView contact = v.findViewById(R.id.studentDetailsPhonenum);
                                TextView email = v.findViewById(R.id.studentDetailsEmail);
                                name.setText("Name: "+jsonObject.getJSONObject("User").getString("firstName") + " " + jsonObject.getJSONObject("User").getString("lastName"));
                                contact.setText("Contact: "+jsonObject.getJSONObject("User").getString("phonenum"));
                                email.setText("E-mail ID: "+jsonObject.getJSONObject("User").getString("email"));
                                builder.setView(v);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Teacher.this, "An error occured", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog2.dismiss();
                        Toast.makeText(Teacher.this, "An error occured", Toast.LENGTH_LONG).show();
                        volleyError.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", emailOfStudent);
                        return params;
                    }
                };
                Volley.newRequestQueue(Teacher.this).add(stringRequest1);
            }
        });
    }
}
