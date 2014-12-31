package com.martinutils;

/**
 * Created by martin on 31/12/14.
 */
public interface TimerCallback
{
    public void updateOutput(int currentgrams, long time);

    void complete(long totalTime);
}
