package com.akash.cp.vtu.vtupartb_8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Base {
    Intent mIntent;
    AIDLService mAidlService;
    public static final String TAG="MainActivity";
    EditText mEditTexPrincipal,mEditTexRate,mEditTexTerms,mEditTexDownPayment;
    Button mCalculate;
    TextView mTextView;
    String principal,rate,term,downPayment;
    double mEMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listener();
        mIntent=new Intent(MainActivity.this,EMIService.class);
         bindService(mIntent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidlService=AIDLService.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    public void init() {
        mEditTexPrincipal=(EditText)findViewById(R.id.principal);
        mEditTexRate=(EditText)findViewById(R.id.interest_rate);
        mEditTexDownPayment=(EditText)findViewById(R.id.down_payment);
        mEditTexTerms=(EditText)findViewById(R.id.term);
        mCalculate=(Button)findViewById(R.id.calculate);
        mTextView=(TextView)findViewById(R.id.result);


    }

    @Override
    public void listener() {
        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                principal=mEditTexPrincipal.getText().toString();
                rate=mEditTexRate.getText().toString();
                downPayment=mEditTexDownPayment.getText().toString();
                term=mEditTexTerms.getText().toString();

                if(!TextUtils.isEmpty(principal) && !TextUtils.isEmpty(rate) && !TextUtils.isEmpty(downPayment) && !TextUtils.isEmpty(term))
                {
                    if(Double.parseDouble(principal)>=Double.parseDouble(downPayment)) {
                        try {
                          double  mEMI = mAidlService.CarEMICalculation(Double.parseDouble(principal), Double.parseDouble(downPayment), Double.parseDouble(rate), Integer.parseInt(term));
                            mTextView.setText( String.format("%.3f",mEMI));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "please enter all the feildes", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}