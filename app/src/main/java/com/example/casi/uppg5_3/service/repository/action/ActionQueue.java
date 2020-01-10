package com.example.casi.uppg5_3.service.repository.action;

import android.os.Looper;
import android.util.Log;

import com.example.casi.uppg5_3.service.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActionQueue extends Thread {
    private static final String TAG = "ActionQueue";


    private boolean quit = false;
    private PriorityBlockingQueue<Action> _q;

    @Inject
    public ActionQueue() {
        _q = new PriorityBlockingQueue<>();
        this.start();
    }

    public void add(Action a) {
        //Log.d(TAG, "Added :: " +a.toString());
        if(_q.contains(a)){
            if (a.allow_multiple)
                _q.add(a);
        } else {
            _q.add(a);
        }
    }

    @Override
    public void run() {
        //Check if valid Thread
        try {
            if (Looper.getMainLooper().getThread() == Thread.currentThread())
                throw new Exception("ERROR: ServerThread is on UI/Main");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (!quit) {

            if (_q.peek()!= null) {
                if (_q.peek().okToRun()) {
                    try {
                        _q.take().run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "->run(): Not allowed to run object: " + _q.peek().toString());
                    _q.peek().requires();
                }
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void please_quit() {
        quit = true;
    }

}
