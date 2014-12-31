package com.martinutils;

/**
 * Created by martin on 31/12/14.
 */
public class Timer
{
    private int targetGrams;
    private ISettings settings;
    private double rate;
    private long startTime;
    private double currentGrams;

    public Timer(int grams, double startingGrams, ISettings settings)
    {
        System.out.println("Creating timer for " + grams + " grams starting at " + startingGrams + " using rate " + settings.getRate());
        this.targetGrams = grams;
        this.currentGrams = startingGrams;
        this.settings = settings;
    }


    public void start(final TimerCallback callback)
    {
        rate = settings.getRate();
        startTime = System.currentTimeMillis() - (long)(rate * currentGrams);
        System.out.println("Set starttime " + startTime + " current: " + System.currentTimeMillis());
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (getWholeGrams() < targetGrams)
                {
                    callback.updateOutput(getWholeGrams(), System.currentTimeMillis() - startTime);
                    try
                    {
                        sleepUntilGrams(getWholeGrams() + 1);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                callback.complete(System.currentTimeMillis() - startTime);
            }
        }).start();
    }

    private int getWholeGrams()
    {
        return (int)Math.floor(currentGrams);
    }

    private void sleepUntilGrams(int i) throws InterruptedException
    {
        long time = startTime + (long) (rate * i);
        final long delay = time - System.currentTimeMillis();
        Thread.sleep(delay);
        currentGrams = (int) Math.floor((System.currentTimeMillis() - startTime + 10) / rate);
    }

}
