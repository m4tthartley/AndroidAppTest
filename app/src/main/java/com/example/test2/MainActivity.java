package com.example.test2;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends FragmentActivity {

//    private OnFragmentInteractionListener mListener;
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private int num = 0;

    public void amazingButton(View view) {
        Log.d("MainActivity", "AMAZING BUTTON CLICKED");

        Toast t = Toast.makeText(this, "wow good job", Toast.LENGTH_SHORT);
        t.show();

        ++num;
        TextView textView = findViewById(R.id.textView);
        textView.setText(Integer.toString(num));

        final FragmentActivity context = this;
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://opentdb.com/api.php?amount=10&category=11", new Response.Listener<String>() {
                @Override public void onResponse(String res) {
                    Log.d("MainActivity", res.substring(0,500));

                    try {
                        JSONObject json = new JSONObject(res);
                        Log.d("MainActivity", "" + json.getInt("response_code"));
                        JSONArray results = json.getJSONArray("results");
                        Log.d("MainActivity", "questions " + results.length());

                        for(int i = 0; i < results.length(); i++) {
                            JSONObject q = (JSONObject)results.get(i);
                            Log.d("MainActivity", q.getString("question"));

                            Fragment frag = (Fragment)new Question();
                            Bundle args = new Bundle();
                            args.putString("param1", q.getString("question"));
                            args.putString("param2", "test2");
                            frag.setArguments(args);
                            FragmentManager fragMan = getSupportFragmentManager();
                            FragmentTransaction transaction = fragMan.beginTransaction();
//                            transaction.disallowAddToBackStack();
                            transaction.add(R.id.mainLayout, frag, "q"+i);
                            transaction.commit();
                        }
                    } catch(Exception e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override public void onErrorResponse(VolleyError error) {
                    Log.d("MainActivity", error.toString());
                }
            });

            queue.add(stringRequest);
        } catch(Exception e) {
            System.out.println(e.toString());
        }

    }
}
