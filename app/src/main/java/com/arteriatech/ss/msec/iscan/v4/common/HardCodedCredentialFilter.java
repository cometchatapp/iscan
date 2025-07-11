package com.arteriatech.ss.msec.iscan.v4.common;

import android.content.Context;
import android.util.Base64;

import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.httpc.filters.IRequestFilter;
import com.sap.smp.client.httpc.filters.IRequestFilterChain;

import java.nio.charset.Charset;
import java.util.Map;

public class HardCodedCredentialFilter implements IRequestFilter {
    private static final String TRIED_ALREADY_FLAG = "HardCodedCredentialFilter";
    private Context mContext;
   /* public HardCodedCredentialFilter(Context mContext){
        this.mContext=mContext;
    }*/
    @Override
    public Object filter(ISendEvent event, IRequestFilterChain chain) {
        // Get the context to read conversation-scoped information from.
        Map<String, Object> convContext = event.getConversationContext().getStateMap(this.getClass().getName(), false);
//        SharedPreferences sharedPref = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);

        // Check if the hard-coded credentials have been tried already.
        if (convContext==null || !convContext.containsKey(TRIED_ALREADY_FLAG)) {
            // Assemble the BasicAuth header.
            final String user = "S0012486235";//sharedPref.getString(UtilRegistrationActivity.KEY_username,"");
            final String password = "Sap@1218";//sharedPref.getString(UtilRegistrationActivity.KEY_password,"");
            String encodedUserPassword = Base64.encodeToString(
                    (user + ":" + password).getBytes(Charset.forName("UTF-8")),
                    Base64.NO_WRAP);

            // Set it.
            event.getRequestHeaders().put("Authorization", "Basic " + encodedUserPassword);

            // Set the flag so that the next time we'll not retry the credentials.
//            convContext.put(TRIED_ALREADY_FLAG, true);
        }
        return this.getClass();
    }

    @Override
    public Object getDescriptor() {
        return this.getClass();
    }


}
