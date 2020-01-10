package com.example.casi.uppg5_3.ui.util;

import android.os.AsyncTask;
import android.view.View;

import java.lang.ref.WeakReference;

public class ProgressbarAsync extends AsyncTask<Integer, Integer, String> {

    private WeakReference<ProgressbarListener> _weakActivityContext;


    public ProgressbarAsync(ProgressbarListener strongActivityContext){
        _weakActivityContext = new WeakReference<ProgressbarListener>(strongActivityContext);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ProgressbarListener strongRef = _weakActivityContext.get();
        if(strongRef == null || strongRef.isFinishing()){
            return;
        }
        strongRef.setProgressbarVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Integer... integers) {
        String err_msg = "Connecting";
        for (int i=0; i<integers[0]+1; i++){
            publishProgress((i*100)/integers[0]);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return err_msg;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressbarListener strongRef = _weakActivityContext.get();
        if(strongRef == null || strongRef.isFinishing()){
            return;
        }
        strongRef.setProgressbarProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String err_msg) {
        super.onPostExecute(err_msg);
        ProgressbarListener strongRef = _weakActivityContext.get();
        if(strongRef == null || strongRef.isFinishing()){
            return;
        }

        strongRef.setProgressbarProgress(0);
        strongRef.setProgressbarVisibility(View.INVISIBLE);

        try {
            strongRef.onProgressbarFinished();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
