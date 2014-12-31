package com.martinutils;

/**
 * Created by martin on 31/12/14.
 */
public interface CalibratorCallback
{
    void updateOutput(int currentgrams, long time);

    void updateState(CalibratorState state);
}
