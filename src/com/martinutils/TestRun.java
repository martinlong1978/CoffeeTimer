package com.martinutils;

/**
 * Created by martin on 31/12/14.
 */
public class TestRun
{
    private static Calibrator cal;

    public static void main(String[] args)
    {
        Settings settings = new Settings(3500, 500, 9, 18);
        cal = new Calibrator(18, settings, new GrinderControl()
        {
            @Override
            public void start()
            {
                System.out.println("Starting grinder");
            }

            @Override
            public void stop()
            {
                System.out.println("Stopping grinder");

            }

            @Override
            public void addGrindButtonListener(GrindButtonListener listener)
            {

            }
        }, new CalibratorCallback()
        {
            @Override
            public void updateOutput(int currentgrams, long time)
            {
                System.out.println("Output " + currentgrams + " grams in " + time);
            }

            @Override
            public void updateState(CalibratorState state)
            {
                System.out.println("State: " + state);
                switch (state)
                {
                    case INITIALDOSE:
                        System.out.println("Entering 14.5 output");
                        cal.enterActualOutput(14.5);
                        break;
                }
            }
        });

        System.out.println("Starting");
        cal.initialDose();
        System.out.println("Done");


       /* ISettings settings = new ISettings(){

            @Override
            public double getRate()
            {
                return 5000;
            }
        };

        Timer timer = new Timer(10,3.5,settings);

        timer.start(new TimerCallback()
        {
            @Override
            public void updateOutput(int currentgrams, long l)
            {
                System.out.println("Reached " + currentgrams + " grams");
            }

            @Override
            public void complete(long totalTime)
            {
                System.out.println("Done");
            }
        });
        try
        {
            Thread.sleep(60000);
        }
        catch (InterruptedException e)
        {

        }*/
    }
}
