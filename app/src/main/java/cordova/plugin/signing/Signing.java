package cordova.plugin.signing;
import android.content.Intent;
import android.widget.Toast;

import com.qxc.app.SignActivity;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Base64;

public class Signing  extends CordovaPlugin {
private CallbackContext callbackContext;
  public String base64=null;
 //   private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("signaction")) {
            try {
                //下面两句最关键，利用intent启动新的Activity
           //     Intent intent = new Intent().setClass(cordova.getActivity(), Class.forName(args.getString(0)));
                Intent intent = new Intent().setClass(cordova.getActivity(), SignActivity.class);
                this.cordova.startActivityForResult(this, intent, 1);
                //   this.callbackContext=callbackContext;
                //下面三句为cordova插件回调页面的逻辑代码
               // PluginResult mPlugin = new PluginResult(PluginResult.Status.NO_RESULT);
                //    pluginResult.setKeepCallback(true);//设置保持回调通道
                //   callbackContext.sendPluginResult(pluginResult); //先返回NO_RESULT，以后java代码随时返回数据
              this.callbackContext=callbackContext;
             //   mPlugin.setKeepCallback(true);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void aaa(String message, CallbackContext callbackContext) {
        //弹框
       // Toast.makeText(cordova.getActivity(),"aaa",Toast.LENGTH_LONG).show();
        CordovaPlugin cordovaPlugin =null;
        Intent intent=new Intent(this.cordova.getContext(), SignActivity.class);
        this.cordova.getActivity().startActivity(intent);
       // this.cordova.startActivityForResult(cordovaPlugin,intent,1);
      //  startActivity(intent);
        //h5端传给我什么参数，此处再传回去
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
       // super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==1 && resultCode==2){
            base64 =intent.getStringExtra("Base64");
         callbackContext.success(base64);
        }

    }
}
