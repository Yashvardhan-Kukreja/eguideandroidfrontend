package com.example.yash1300.e_guide;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Yash 1300 on 29-10-2017.
 */

@SuppressLint("ValidFragment")
public class AllTeachersFragment extends Fragment {
    Context context;
    String emailOfStudent;
    ProgressDialog progressDialog;
    int i, j;
    RecyclerView recyclerView;
    List<TeacherItem> teacherItems;
    String base_url = "http://192.168.43.76:8002";

    @SuppressLint("ValidFragment")
    public AllTeachersFragment(Context context, String emailOfStudent) {
        this.context = context;
        this.emailOfStudent = emailOfStudent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_all_teachers, container, false);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading the teachers for you...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        recyclerView = v.findViewById(R.id.allTeachersRecyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        teacherItems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, base_url + "/users/findteachersforthestudent", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String success = jsonObject.getString("Success");
                    String message = jsonObject.getString("Message");
                    if (success.equals("0")) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("teachers");
                        if (jsonArray.length() == 0){
                            Toast.makeText(context, "No teachers available for you right now", Toast.LENGTH_LONG).show();
                        } else {
                            for (i=0;i<jsonArray.length();i++){
                                List<String> skills = new ArrayList<>();
                                List<String> studentsInterested = new ArrayList<>();
                                for (j=0;j<jsonArray.getJSONObject(i).getJSONArray("skills").length();j++){
                                    skills.add(jsonArray.getJSONObject(i).getJSONArray("skills").getString(j));
                                }
                                for (j=0;j<jsonArray.getJSONObject(i).getJSONArray("studentsInterested").length();j++){
                                    skills.add(jsonArray.getJSONObject(i).getJSONArray("studentsInterested").getString(j));
                                }
                                List<String> skillsRedefined = new ArrayList<>();
                                for (int k=0;k<skills.size();k++){
                                    if (skills.get(k).equals("0")){
                                        skillsRedefined.add("Android Application Development");
                                    } else if (skills.get(k).equals("1")){
                                        skillsRedefined.add("Web Development");
                                    } else if (skills.get(k).equals("2")){
                                        skillsRedefined.add("iOS application Development");
                                    } else if (skills.get(k).equals("3")){
                                        skillsRedefined.add("UI/UX Development");
                                    } else if (skills.get(k).equals("4")){
                                        skillsRedefined.add("Back end development");
                                    } else if (skills.get(k).equals("5")){
                                        skillsRedefined.add("Database Management");
                                    } else if (skills.get(k).equals("6")){
                                        skillsRedefined.add("Game Development");
                                    } else if (skills.get(k).equals("7")){
                                        skillsRedefined.add("Graphic designing");
                                    } else if (skills.get(k).equals("8")){
                                        skillsRedefined.add("Material designing");
                                    } else if (skills.get(k).equals("9")){
                                        skillsRedefined.add("Python frameworks");
                                    } else if (skills.get(k).equals("10")){
                                        skillsRedefined.add("Object oriented programming");
                                    } else if (skills.get(k).equals("11")){
                                        skillsRedefined.add("Personality development");
                                    } else if (skills.get(k).equals("12")){
                                        skillsRedefined.add("Debating");
                                    } else {
                                        skillsRedefined.add("Machine Learning");
                                    }
                                }
                                TeacherItem teacher = new TeacherItem(jsonArray.getJSONObject(i).getString("firstName"), jsonArray.getJSONObject(i).getString("lastName"), jsonArray.getJSONObject(i).getString("age"), jsonArray.getJSONObject(i).getString("email"), jsonArray.getJSONObject(i).getString("phonenum"), skillsRedefined, studentsInterested);
                                teacherItems.add(teacher);
                                CustomRecyclerAdapter customRecyclerAdapter = new CustomRecyclerAdapter(teacherItems, context, 1, emailOfStudent);
                                recyclerView.setAdapter(customRecyclerAdapter);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                volleyError.printStackTrace();
                Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailOfStudent);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);


        return v;
    }
}
