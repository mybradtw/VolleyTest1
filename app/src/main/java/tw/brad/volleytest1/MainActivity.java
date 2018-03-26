package tw.brad.volleytest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private final String url ="http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
    }

    public void test1(View view) {
        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("brad", response);
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("brad", error.getMessage());
                }
        });

        queue.add(stringRequest);

    }
    public void test2(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                }, null);

        queue.add(stringRequest);
    }

    private void parseJSON(String json){
        try{
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                JSONObject data = root.getJSONObject(i);
                String name = data.getString("Name");
                String address = data.getString("Address");
                Log.v("brad", name + ":" + address );
            }
        }catch (Exception e){

        }
    }

}
