package thirdrdhand.ussdtestapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int TAG =124 ;
    private FrameLayout homeFrame, dashBoardFrame, notificationFrame;

    private EditText etUssd;
    private Button btCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        homeFrame = (FrameLayout) findViewById(R.id.content_home);
        dashBoardFrame = (FrameLayout) findViewById(R.id.content_dashboard);
        notificationFrame = (FrameLayout) findViewById(R.id.content_notifications);

        //Ussd
        etUssd = (EditText) findViewById(R.id.etHome_ussd);
        btCall = (Button) findViewById(R.id.btHome_call);


        //set  Listener
        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ussd = etUssd.getText().toString();



                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    Uri ussdData=packageForCall(ussd);

                    intent.setData(ussdData);
                    startActivity(intent);
                    return;
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},TAG);
                }

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private Uri packageForCall(String ussd) {

        if(ussd.contains("#")) {

            /**
             * Example I have   *444*0987#8989898#90#     I want to Encode Hash tags
             *
             */
            String[] segmentsByHash = ussd.split("#");
            /**
             * This will give me this list:
             * segmentsByHash[0]="*444*0987"
             * segmentsByHash[1]="8989898"
             * segmentsByHash[2]="90"
             */

            Uri result = Uri.parse("tel:" + segmentsByHash[0]);
            /**
             * result= *444*0987
             */


            for (int i = 1; i <= segmentsByHash.length; i++) {

                if (i < segmentsByHash.length)
                    result = Uri.parse(result + Uri.encode("#") + Uri.parse(segmentsByHash[1]));
                /**
                 * when i=1;
                 * *444*0987#8989898
                 *
                 * when i=2
                 *  *444*0987#8989898#90
                 */
                    //Finnaly
                else if (i >= segmentsByHash.length) {
                    result = Uri.parse(result + Uri.encode("#"));
                }
                /**
                 *  finnaly add #
                 *  *444*0987#8989898#90#
                 */
            }


            return result;
        }
        else
            return Uri.parse("tel:"+ussd);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideAllFrames();
                    homeFrame.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    hideAllFrames();
                    dashBoardFrame.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    hideAllFrames();
                    notificationFrame.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }

    };

    private void hideAllFrames() {
        homeFrame.setVisibility(View.GONE);
        dashBoardFrame.setVisibility(View.GONE);
        notificationFrame.setVisibility(View.GONE);

    }


}
