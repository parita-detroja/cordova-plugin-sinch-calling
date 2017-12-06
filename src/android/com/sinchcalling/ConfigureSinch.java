package com.sinchcalling;

import android.content.Context;

import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by parita.detroja on 5/12/17.
 * Initiate sinch-client for different sinch functionality.
 */

public class ConfigureSinch
{
    private static SinchClient sinchClient; //sinch client

    public static SinchClient retrieveSinchClient()
    {
        return sinchClient;
    }

    /**
     * Build sinch client for audio and video calling by providing all keys and user id etc.
     * @param context activity context for sinch client
     * @param configurationData all required keys in JSONObject
     */
    public void initiateSinchClient(Context context, JSONObject configurationData)
    {
        try {
            sinchClient = Sinch.getSinchClientBuilder().context(context)
                    .applicationKey(configurationData.getString(Constant.APPLICATION_KEY))
                    .applicationSecret(configurationData.getString(Constant.APPLICATION_SECRET))
                    .environmentHost(configurationData.getString(Constant.ENVIRONMENT_HOST))
                    .userId(configurationData.getString(Constant.USER_ID))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setSupportMessaging(boolean status)
    {
        sinchClient.setSupportMessaging(status);
    }

    public void setSupportCalling(boolean status)
    {
        sinchClient.setSupportCalling(status);
    }

    public void setSupportManagedPush(boolean status)
    {
        sinchClient.setSupportManagedPush(status);
    }

    public void setSupportActiveConnectionInBackground(boolean status)
    {
        sinchClient.setSupportActiveConnectionInBackground(status);
    }

    public void startListeningOnActiveConnection()
    {
        sinchClient.startListeningOnActiveConnection();
    }

    public void startSinchClient()
    {
        sinchClient.start();
    }

    public void stopListeningOnActiveConnection()
    {
        sinchClient.stopListeningOnActiveConnection();
    }

    public void terminateSinchClient()
    {
        sinchClient.terminate();
    }

}
