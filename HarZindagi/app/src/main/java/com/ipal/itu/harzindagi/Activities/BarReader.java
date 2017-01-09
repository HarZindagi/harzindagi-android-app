package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Ali on 1/16/2016.
 */
public class BarReader extends BaseActivity implements ZBarScannerView.ResultHandler {
    TextView myTextView;
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.bar_view);
        mScannerView = (ZBarScannerView) findViewById(R.id.barView);  // Programmatically initialize the scanner view
        findViewById(R.id.done).setVisibility(View.GONE);
        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView.resumeCameraPreview(BarReader.this);
                findViewById(R.id.done).setVisibility(View.GONE);
            }
        });
        myTextView = (TextView) findViewById(R.id.example_textview);
        BarcodeFormat format = BarcodeFormat.EAN13;
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(format);
        mScannerView.setFormats(list);
        mScannerView.setupScanner();
      //  mScannerView.setFlash(true);
        //mScannerView.setAutoFocus(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        myTextView.setText("Result: "+rawResult.getContents()+ "\n"+"Format: "+rawResult.getBarcodeFormat().getName());
        findViewById(R.id.done).setVisibility(View.VISIBLE);
      //  mScannerView.resumeCameraPreview(this);

    }
}
