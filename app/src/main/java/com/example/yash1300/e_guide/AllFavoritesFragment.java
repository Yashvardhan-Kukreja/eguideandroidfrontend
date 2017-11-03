package com.example.yash1300.e_guide;

import android.annotation.SuppressLint;
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
 * Created by Yash 1300 on 31-10-2017.
 */

@SuppressLint("ValidFragment")
public class AllFavoritesFragment extends Fragment {
    Context context;
    RecyclerView recyclerView;
    String emailOfStudent;
    List<TeacherItem> teacherItems;

    @SuppressLint("ValidFragment")
    public AllFavoritesFragment(Context context, String emailOfStudent) {
        this.context = context;
        this.emailOfStudent = emailOfStudent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_favorites, container, false);
        recyclerView = v.findViewById(R.id.allFavoritesRecyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.76:8002/users/getuserdetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject mainJsonObject = new JSONObject(s);
                    JSONArray interestedTeachers = mainJsonObject.getJSONArray("interestedTeachers");
                    String success = mainJsonObject.getString("Success");
                    teacherItems = new ArrayList<>();
                    String message = mainJsonObject.getString("Message");
                    if (success.equals("0")){
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    } else {
                        if (interestedTeachers.length() == 0){
                            Toast.makeText(context, "No favorite teachers as of now", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i=0;i<interestedTeachers.length();i++){
                                List<String> skills = new ArrayList<>();
                                List<String> studentsInterested = new ArrayList<>();
                                JSONObject currentJsonObjectTeacher = interestedTeachers.getJSONObject(i);
                                for (int j=0;j<currentJsonObjectTeacher.getJSONArray("studentsInterested").length();j++){
                                    studentsInterested.add(currentJsonObjectTeacher.getJSONArray("studentsInterested").getString(j));
                                }
                                for (int k=0;k<currentJsonObjectTeacher.getJSONArray("skills").length();k++){
                                    skills.add(currentJsonObjectTeacher.getJSONArray("skills").getString(k));
                                }
                                List<String> skillsRedefined = new ArrayList<>();
                                for (int l=0;l<skills.size();l++){
                                    if (skills.get(l).equals("0")){
                                        skillsRedefined.add("Android Application Development");
                                    } else if (skills.get(l).equals("1")){
                                        skillsRedefined.add("Web Development");
                                    } else if (skills.get(l).equals("2")){
                                        skillsRedefined.add("iOS application Development");
                                    } else if (skills.get(l).equals("3")){
                                        skillsRedefined.add("UI/UX Development");
                                    } else if (skills.get(l).equals("4")){
                                        skillsRedefined.add("Back end development");
                                    } else if (skills.get(l).equals("5")){
                                        skillsRedefined.add("Database Management");
                                    } else if (skills.get(l).equals("6")){
                                        skillsRedefined.add("Game Development");
                                    } else if (skills.get(l).equals("7")){
                                        skillsRedefined.add("Graphic designing");
                                    } else if (skills.get(l).equals("8")){
                                        skillsRedefined.add("Material designing");
                                    } else if (skills.get(l).equals("9")){
                                        skillsRedefined.add("Python frameworks");
                                    } else if (skills.get(l).equals("10")){
                                        skillsRedefined.add("Object oriented programming");
                                    } else if (skills.get(l).equals("11")){
                                        skillsRedefined.add("Personality development");
                                    } else if (skills.get(l).equals("12")){
                                        skillsRedefined.add("Debating");
                                    } else {
                                        skillsRedefined.add("Machine Learning");
                                    }
                                }
                                TeacherItem newTeacher = new TeacherItem(currentJsonObjectTeacher.getString("firstName"), currentJsonObjectTeacher.getString("lastName"),
                                        currentJsonObjectTeacher.getString("age"), currentJsonObjectTeacher.getString("email"), currentJsonObjectTeacher.getString("phonenum"), skillsRedefined, studentsInterested);
                                teacherItems.add(newTeacher);
                                CustomRecyclerAdapter customRecyclerAdapter = new CustomRecyclerAdapter(teacherItems,context, 0,emailOfStudent);
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