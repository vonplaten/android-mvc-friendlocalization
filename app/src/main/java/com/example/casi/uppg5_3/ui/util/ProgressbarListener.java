package com.example.casi.uppg5_3.ui.util;

public interface ProgressbarListener {
    void onProgressbarFinished() throws InterruptedException;
    boolean isFinishing();
    void setProgressbarVisibility(int visibility);
    void setProgressbarProgress(Integer progress);
}
