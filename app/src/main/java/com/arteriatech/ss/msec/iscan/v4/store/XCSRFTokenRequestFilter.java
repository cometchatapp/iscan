/*
package com.arteriatech.ss.msec.bil.v2.store;

import android.content.Context;
import android.util.Log;

import com.arteriatech.ss.msec.bil.v2.common.Constants;
import com.sap.smp.client.httpc.HttpMethod;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.httpc.filters.IRequestFilter;
import com.sap.smp.client.httpc.filters.IRequestFilterChain;

public class XCSRFTokenRequestFilter implements IRequestFilter {


	private static XCSRFTokenRequestFilter instance;
	public static String lastXCSRFTokenValue = "";
	private String lastXCSRFToken = null;
	private Context mContext;
	
	private XCSRFTokenRequestFilter(Context mContext) {
		this.mContext = mContext;
	}

	*/
/**
	 * @return XCSRFTokenRequestFilter
	 *//*

	public static XCSRFTokenRequestFilter getInstance(Context mContext) {
		if (instance == null) {
			instance = new XCSRFTokenRequestFilter(mContext);
		}
		return instance;
	}


	@Override
	public Object filter(ISendEvent event, IRequestFilterChain chain) {
		HttpMethod method = event.getMethod();
		Log.i("XCSRFTokenRequestFilter", "method: " + method + ", lastXCSRFToken: " + lastXCSRFToken);
		if (method == HttpMethod.GET */
/* && lastXCSRFToken == null *//*
) {
			lastXCSRFToken =null;
			event.getRequestHeaders().put("X-CSRF-Token", "Fetch");
		} else if (lastXCSRFToken != null) {
			event.getRequestHeaders().put("X-CSRF-Token", lastXCSRFToken);
		} else {
			event.getRequestHeaders().put("X-Requested-With", "XMLHttpRequest");
		}

		String appConnID = null;
		*/
/*try {// TODO: 19-12-2018 Hided for version upgrade
			appConnID = lgCtx.getConnId();
		} catch (LogonCoreException e) {
			Log.e("XCSRFTokenRequestFilter", "error getting connection id", e);
		}*//*


		//for backward compatibility. not needed for SMP 3.0 SP05
		if (appConnID != null) {
			event.getRequestHeaders().put(Constants.HTTP_HEADER_SUP_APPCID, appConnID);
			event.getRequestHeaders().put(Constants.HTTP_HEADER_SMP_APPCID, appConnID);
		}
		event.getRequestHeaders().put("Connection", "Keep-Alive");

		return chain.filter();
	}

	@Override
	public Object getDescriptor() {
		return "XCSRFTokenRequestFilter";
	}

	public void setLastXCSRFToken(String lastXCSRFToken) {
		this.lastXCSRFToken = lastXCSRFToken;
		this.lastXCSRFTokenValue = lastXCSRFToken;
	}

}
*/
