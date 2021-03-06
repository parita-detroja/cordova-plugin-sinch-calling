/**
 * SinchCalling CordovaPlugin implementation for add method action for ionic project.
 */
package com.sinchcalling;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;

import android.util.Log;

import java.util.Date;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class SinchCalling extends CordovaPlugin 
{
  
  private static final String TAG = "SinchCalling";

  private static final String INIT_SINCH = "initSinch";
  private static final String CONNECT_AUDIO_CALL = "connectAudioCall";
  private static final String HANGUP_AUDIO_CALL = "hangupAudioCall";
  private static final String ANSWER_AUDIO_CALL = "answerAudioCall";

  private SinchAudioCall sinchAudioCall;

  private static CallbackContext callbackContext;

  private ExecutorService executorService;

  private JSONObject status = new JSONObject();

  private PluginResult result = null;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) 
  {
    super.initialize(cordova, webView);

    Log.d(TAG, "Initializing SinchCalling");
  }

   @Override
  protected void pluginInitialize() 
  {
    super.pluginInitialize();

    Log.d(TAG, "pluginInitialize Sinch Calling");

    executorService =  Executors.newSingleThreadExecutor();
  }

  public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException 
  {

    final Activity activity = this.cordova.getActivity();

    if(action.equals(INIT_SINCH)) 
    {

      this.callbackContext = callbackContext;
      executorService.execute(new Runnable() 
      {
        public void run() 
        {

          Log.d(TAG, "from initSinch");
          
          Log.e(TAG, args.toString());

            activity.runOnUiThread(new Runnable() 
            {

              @Override
              public void run() 
              {
                try{

                  Log.e(TAG, args.getJSONObject(0).toString());
                  
                  status = ConfigureSinch.initiateSinchClient(activity.getApplicationContext(), args.getJSONObject(0));

                  ConfigureSinch.setSupportCalling(true);

                  status.put("set support calling","succeessfull");

                  ConfigureSinch.startListeningOnActiveConnection();

                  status.put("start listening on active connection","succeessfull");

                  ConfigureSinch.startSinchClient();

                  status.put("start sinch client","succeessfull");

                  sinchAudioCall = new SinchAudioCall(activity.getApplicationContext());

                  status.put("sinch audio call","succeessfull");

                  sinchAudioCall.addCallClientListener();

                  status.put("add call client listener","succeessfull");

                  if(status.getBoolean("status"))
                  {
                    result = new PluginResult(PluginResult.Status.OK, status);
                  }else 
                  {
                    result = new PluginResult(PluginResult.Status.ERROR, status);
                  }

                  sinchAudioCall.initializeCallbackContex(callbackContext);

                  result.setKeepCallback(true);
                  callbackContext.sendPluginResult(result);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            });  
        }
      });

    } else if(action.equals(CONNECT_AUDIO_CALL)) 
    {

      Log.d(TAG, "from connectAudioCall");
      Log.e(TAG, args.toString());

      activity.runOnUiThread(new Runnable() 
      {
        @Override
        public void run() 
        {
          try{
            callbackContext.success("Call Created");
            sinchAudioCall.createAudioCall(args.getString(0));

          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      });
    } else if(action.equals(HANGUP_AUDIO_CALL))
    {
      activity.runOnUiThread(new Runnable() 
      {
        @Override
        public void run() 
        {
          Log.d(TAG, "from hangupAudioCall");
          sinchAudioCall.hangUpAudioCall();
          result = new PluginResult(PluginResult.Status.OK, "Call Hanged up");
          result.setKeepCallback(true);
          callbackContext.sendPluginResult(result);
        }
      });
    } else if(action.equals(ANSWER_AUDIO_CALL))
    {
      activity.runOnUiThread(new Runnable() 
      {
        @Override
        public void run() 
        {
          try{
            Log.d(TAG, "from answerAudioCall");
            Log.e(TAG, args.toString());
            sinchAudioCall.answerIncomingCall(args.getBoolean(0));
            result = new PluginResult(PluginResult.Status.OK, "Call answered");
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      });
      
    }


    return true;
  }

}
