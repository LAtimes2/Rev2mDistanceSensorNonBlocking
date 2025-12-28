package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.hardware.stmicroelectronics.VL53L0X;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;

// This class reduces blocking the main task by checking if the sensor
// has a new update (about every 33 msec), and using a cached value
// in between updates.

// example: Rev2mDistanceSensorNonBlocking sensor = new Rev2mDistanceSensorNonBlocking (hardwareMap.get(DistanceSensor.class, "distance"));


public class Rev2mDistanceSensorNonBlocking
{
    private final double UPDATE_TIME_MSEC = 33.0 - 2.0;    // 2 msec overhead

    private double cachedDistance = 999.9;
    private DistanceSensor distanceSensor = null;
    private Rev2mDistanceSensor rev2mDistanceSensor = null;
    private ElapsedTime updateTimer = new ElapsedTime ();

    public Rev2mDistanceSensorNonBlocking (DistanceSensor distanceSensor)
    {
        this.distanceSensor = distanceSensor;
        updateTimer.reset ();

        if (distanceSensor instanceof Rev2mDistanceSensor)
        {
            rev2mDistanceSensor = (Rev2mDistanceSensor)distanceSensor;
        }
    }
    
    public double getDistance(DistanceUnit distanceUnit)
    {
        double range = 0.0;
        
        if (rev2mDistanceSensor != null)
        {
           range = getDistanceMM ();

            if (distanceUnit == DistanceUnit.CM) {
                range /= 10;
            } else if (distanceUnit == DistanceUnit.METER) {
                range /= 1000;
            } else if (distanceUnit == DistanceUnit.INCH) {
                range /= 25.4;
            }
        }
        else   // it's not a Rev 2M sensor
        {
            // call original function
            range = distanceSensor.getDistance (distanceUnit);
        }

        return range;
    }

    public double getDistanceMM ()
    {
        // this code came from
        // https://github.com/Eeshwar-Krishnan/Photon-RC/blob/NeutrinoDev/Hardware/src/main/java/com/qualcomm/hardware/stmicroelectronics/VL53L0X.java
        // lines 1020-1029
        
        // if an update should have occurred
        if (updateTimer.milliseconds () >= UPDATE_TIME_MSEC)
        {
            I2cDeviceSynch deviceClient = rev2mDistanceSensor.getDeviceClient ();

            // check if new update ready (takes about 2-3 msec)
            if ((deviceClient.read8(VL53L0X.Register.RESULT_INTERRUPT_STATUS.bVal) & 0x07) != 0)
            {
                updateTimer.reset ();

                //The device is indicating there is new data available so lets read and cache it
                cachedDistance = TypeConversion.byteArrayToShort(deviceClient.read(VL53L0X.Register.RESULT_RANGE_STATUS.bVal + 10, 2));

                //Clear the interrupt flag and reset the timer so we don't waste time polling the device constantly while reading
                deviceClient.write8(VL53L0X.Register.SYSTEM_INTERRUPT_CLEAR.bVal, 0x01, I2cWaitControl.NONE);
            }
        }
        
        return cachedDistance;
    }

    // The amount of time since the last new read from the sensor
    public long getTimeSinceLastMeasurement(TimeUnit unit){
        return updateTimer.time(unit);
    }
}
