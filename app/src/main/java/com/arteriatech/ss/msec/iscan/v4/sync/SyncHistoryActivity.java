package com.arteriatech.ss.msec.iscan.v4.sync;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo.SyncHistoryInfoFragment;

public class SyncHistoryActivity extends AppCompatActivity implements View.OnClickListener{
    TextView toolbar_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_history);
        toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Sync History");
        toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Fragment fragment = new SyncHistoryInfoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   MSFAApplication.setAnalytics(this,MSFAApplication.SP_UID,this.getClass().getSimpleName(),"Sync History");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.imageViewHome:
                startActivity(new Intent(this, BILMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;*/
        }
    }
}
