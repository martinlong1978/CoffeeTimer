package com.martinutils;

/**
 * Created by martin on 31/12/14.
 */
public class Calibrator
{

    private int shotSize;
    private Settings settings;
    private GrinderControl control;
    private CalibratorCallback callback;
    private CalibratorState state = CalibratorState.START;
    private long totalTime;


    public Calibrator(int shotSize, Settings settings, GrinderControl control, CalibratorCallback callback)
    {
        this.shotSize = shotSize;
        this.settings = settings;
        this.control = control;
        this.callback = callback;
    }

    public void initialDose()
    {
        updateState(CalibratorState.DOSING);
        settings.resetRate();
        Timer timer = new Timer(shotSize, 0, settings, control);
        timer.start(new TimerCallback()
        {
            @Override
            public void updateOutput(int currentgrams, long time)
            {
                callback.updateOutput(currentgrams, time);
            }

            @Override
            public void complete(long totalTime)
            {
                Calibrator.this.totalTime = totalTime;
                updateState(CalibratorState.INITIALDOSE);
            }
        });
    }

    public void enterActualOutput(double output)
    {
        settings.setRate(totalTime / output);
        Timer timer = new Timer(shotSize, output, settings, control);
        timer.start(new TimerCallback()
        {
            @Override
            public void updateOutput(int currentgrams, long time)
            {
                callback.updateOutput(currentgrams, time);
            }

            @Override
            public void complete(long totalTime)
            {
                Calibrator.this.totalTime = totalTime;
                updateState(CalibratorState.DONE);
            }
        });
    }

    public long getTotalTime()
    {
        return totalTime;
    }

    private void updateState(CalibratorState state)
    {
        this.state = state;
        callback.updateState(state);
    }


}
