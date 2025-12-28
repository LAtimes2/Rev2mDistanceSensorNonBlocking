# Rev2mDistanceSensorNonBlocking

The Rev 2M Distance Sensor uses a VL53L0X chip to take distance measurements every 33 milliseconds.

In the FTC SDK, when you call getDistance, it will wait up to 33 milliseconds for the next measurement to finish. This will keep your opMode from doing anything else during that time.

This file will return immediately if a new measurement is not available with the previous measured distance. If a new measurement is available, it takes about 8 milliseconds to read the new value.

To use, just pass the distance sensor object to the constructor for the Rev2mDistanceSensorNonBlocking object. Then call getDistance on the non-blocking object.

An Example opMode is provided.

If you pass in a non-Rev distance sensor, it calls the original SDK getDistance.

To upload the files to OnBotJava:
1. Select the Upload Files icon
2. Browse to the directory with all of the files you downloaded from Github
3. Select the file (and example if desired)
4. Select Open
