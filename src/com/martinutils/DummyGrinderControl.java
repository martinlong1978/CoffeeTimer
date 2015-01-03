package com.martinutils;

import java.io.IOException;

/**
 * Created by martin on 02/01/15.
 */
public class DummyGrinderControl implements GrinderControl
{
    private GrindButtonListener listener;

    public DummyGrinderControl()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                while (true)
                {
                    try
                    {
                        System.in.read();
                        if (listener != null)
                        {
                            listener.buttonPressed();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void start()
    {
        System.out.println("Starting Grinder");
    }

    @Override
    public void stop()
    {
        System.out.println("Stopping Grinder");
    }

    @Override
    public void addGrindButtonListener(GrindButtonListener listener)
    {
        this.listener = listener;
    }
}
