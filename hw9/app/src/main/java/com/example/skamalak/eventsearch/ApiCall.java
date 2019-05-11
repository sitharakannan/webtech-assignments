package com.example.skamalak.eventsearch;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ApiCall {
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public ApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        Log.d("mReqQueue", mRequestQueue.toString());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/suggest?apikey=ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0&keyword=" + query;
        Log.d("query", query);
        Log.d("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}
