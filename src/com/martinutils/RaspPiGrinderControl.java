package com.martinutils;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * Created by martin on 01/01/15.
 */
public class RaspPiGrinderControl implements GrinderControl
{
    private GpioPinDigitalOutput grindMotor;

    public RaspPiGrinderControl(GpioPinDigitalOutput grindMotor)
    {
        this.grindMotor = grindMotor;
    }

    @Override
    public void start()
    {
        grindMotor.high();
    }

    @Override
    public void stop()
    {
        grindMotor.low();
    }
}
