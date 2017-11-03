package com.example.yash1300.e_guide;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yash 1300 on 27-10-2017.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    List<TeacherItem> teacherItems;
    Context context;
    int condition;
    String studentEmail;
    ProgressDialog progressDialog;

    public CustomRecyclerAdapter(List<TeacherItem> teacherItems, Context context, int condition, String studentEmail) {
        this.teacherItems = teacherItems;
        this.context = context;
        this.condition = condition;
        this.studentEmail = studentEmail;
    }

    @Override
    public CustomRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_layout, parent, false)));
    }

    @Override
    public void onBindViewHolder(CustomRecyclerAdapter.ViewHolder holder, int position) {
        final TeacherItem teacher = teacherItems.get(position);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Adding the teacher to your favorites...");
        progressDialog.setCancelable(false);
        holder.name.setText(teacher.getFirstName() + " " + teacher.getLastName());
        holder.age.setText("Age: "+teacher.getAge());
        holder.email.setText("E-mail ID: "+teacher.getEmail());
        holder.contact.setText("Contact: "+teacher.getContact());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.teacher_skills_dialog, null, false);
        ListView skillsList = v.findViewById(R.id.teacherSkillsList);
        skillsList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, teacher.getSkills()));
        builder.setView(v);
        final AlertDialog skillsDialog = builder.create();
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillsDialog.show();
            }
        });
        if (condition == 0){
            holder.teacherInterestedBtn.setVisibility(View.GONE);
        } else {
            holder.teacherInterestedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.76:8002/users/addinterestedteachertothestudent", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String success = jsonObject.getString("Success");
                                String message = jsonObject.getString("Message");
                                if (success.equals("0")){
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context,  "Teacher added to your favorites", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "An error occured now", Toast.LENGTH_LONG).show();
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
                            params.put("teacher", teacher.getEmail());
                            params.put("student", studentEmail);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(context).add(stringRequest);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return teacherItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, age, email, contact;
        LinearLayout mainLayout;
        Button teacherInterestedBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teacherName);
            age = itemView.findViewById(R.id.teacherAge);
            email = itemView.findViewById(R.id.teacherEmail);
            contact = itemView.findViewById(R.id.teacherContact);
            mainLayout = itemView.findViewById(R.id.teacherLayout);
            teacherInterestedBtn = itemView.findViewById(R.id.teacherInterestedButton);
        }
    }
}
