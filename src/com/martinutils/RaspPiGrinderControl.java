package com.martinutils;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Created by martin on 01/01/15.
 */
public class RaspPiGrinderControl implements GrinderControl
{
    private GpioPinDigitalOutput grindMotor;
    public static final Pin INPUT_PIN = RaspiPin.GPIO_02;  //Pin 13
    public static final Pin OUTPUT_PIN = RaspiPin.GPIO_03; //Pin 15 - Relay 1
    private GrindButtonListener listener;


    public RaspPiGrinderControl()
    {

        final GpioController gpio = GpioFactory.getInstance();

        GpioPinDigitalInput grindButton = gpio.provisionDigitalInputPin(INPUT_PIN, "GrindButton", PinPullResistance.PULL_UP);
        grindMotor = gpio.provisionDigitalOutputPin(OUTPUT_PIN, "GrindMotor", PinState.LOW);

        grindButton.addListener(new GpioPinListenerDigital()
        {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent)
            {
                if (gpioPinDigitalStateChangeEvent.getState() == PinState.LOW)
                {
                    if (listener != null)
                    {
                        listener.buttonPressed();
                    }
                }
            }
        });
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

    public void addGrindButtonListener(GrindButtonListener listener)
    {
        this.listener = listener;
    }
}
