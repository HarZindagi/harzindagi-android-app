package com.ipal.itu.harzindagi.Activities;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

final class TokenRequest extends StringRequest {
	TokenRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "client_credentials");
		return params;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		String auth = "Basic "
				+ Base64.encodeToString("ali:uraan1234".getBytes(), Base64.NO_WRAP);
		headers.put("Authorization", auth);
		return headers;
	}
}