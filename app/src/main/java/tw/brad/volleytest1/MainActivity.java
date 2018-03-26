package tw.brad.volleytest1;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private final String url =
            "http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx";
    private ImageView img;

    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();


        img = findViewById(R.id.img);
    }

    public void test1(View view) {
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

        mRequestQueue.add(stringRequest);

    }
    public void test2(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                }, null);

        mRequestQueue.add(stringRequest);
    }

    public void test3(View view) {
        ImageRequest imageRequest = new ImageRequest(
                "https://i2.wp.com/www.bradchao.com/wp-content/uploads/2018/01/IMG_20180108_113633-3.jpg?w=1024&ssl=1",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                },
                0,0,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888,
                null);

        mRequestQueue.add(imageRequest);
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


    // post
    public void test4(View view){
        String postUrl = "https://www.bradchao.com/iii/brad02.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("brad", response);
                    }
                }, null) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("account", "brad");
                params.put("passwd", "123");

                return params;
            }

        };

        mRequestQueue.add(stringRequest);
    }

    // 檔案上傳
    // 會使用到: VolleyMultipartRequest:
    // https://gist.github.com/anggadarkprince/a7c536da091f4b26bb4abf2f92926594
    public void test5(View view) {
        String postUrl = "https://www.bradchao.com/iii/brad02.php";
        File uploadFile = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS),
                    "www.bradchao.com.pdf");
        final byte[] data = new byte[(int)uploadFile.length()];
        try {
            FileInputStream fin = new FileInputStream(uploadFile);
            fin.read(data);
            fin.close();
        }catch (Exception e){
        }

        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(
                        Request.Method.POST, postUrl,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                Log.v("brad", "Upload Finish");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("avatar",
                        new DataPart(
                                "brad.pdf",
                                data
                                ));

                return params;
            }
        };

        mRequestQueue.add(multipartRequest);

    }
}
