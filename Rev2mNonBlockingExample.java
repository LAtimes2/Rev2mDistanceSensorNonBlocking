package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp

public class Rev2mNonBlockingExample extends LinearOpMode {

    @Override
    public void runOpMode() {

        // Assumes Rev 2M Distance Sensor is named "sensor_distance" in the robot config

        DistanceSensor sensor = hardwareMap.get(DistanceSensor.class, "sensor_distance");

        Rev2mDistanceSensorNonBlocking sensorNonBlocking = new Rev2mDistanceSensorNonBlocking(sensor);

        telemetry.addData(">>", "Press start to continue");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Distance", sensorNonBlocking.getDistance(DistanceUnit.INCH));
            telemetry.update();

        }
    }
}
