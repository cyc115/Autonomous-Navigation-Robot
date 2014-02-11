lab report summary 
====

note: 3 flavors means the rising edge , falling edge, donno what the last one is 

Observation and conclusions 
---
1. in our case both are good. turning speed, pulling rate, surronding  noise contribute to the performance.
2. ultrasonic is subject to more limitations and the angle is critical since if the USensor is less than a certain angle from the wall it will not detect anything. While on the other hand if you are at the origin and you turn your robot to a line , it is almost certain that the robot is a multiple of 90 degree orientation. also read the following and answer
3.  idealy when a sensor senses minima, it is either at -90 deg or 180 deg.And the two minimas sensor will detect during the turn will be either the x and y position from the wall. please expand more here. it is problematic because the usSensor is only accurate to cm... 

Error Calc
---

Future improvements
---
1.use mean filter , WIKI this, it's better since it will average out the noise 

2. lasor is a better sensor and gives less error and more accurate.depends less on temp , air and stuff...  wiki for more .

3. use light sensor and turn to find the first and second minimal distance and angle (use a mean filter to determin the exact distance ). you now know this is your x and y coordinate from the wall. based on this information. you can just move to the origin at (30,30) and adjust the angle to the defined position

```
Sensing conditions
The ultrasonic transducers are especially optimised for the medium "air". The sensors can also be used for other gaseous media but then they require a sensitivity adjustment.

Blind zone
Diffuse mode ultrasonic sensors are not capable of detecting targets which are located directly in front of the sonic transducer. The area between the sonic transducer surface and the beginning of the detection range is called blind zone and must always be kept free.

Air temperature and humidity
Both air temperature and air humidity influence the sonic pulse duration. An air temperature increase of 20 °C leads to a change of the sensing distance of up to +3.5 % when using the M30 or the Q30 version (resp. +8 % with the CP40-sensor), whereas the distance of the object seems to decrease. An increase of humidity results in an increase of the sound speed of max. 2 % as opposed to dry air conditions.

Air pressure
Normal atmospheric changes of ± 5 % (for a local reference point) can lead to a deviation of the sensing range of about ± 0.6 %.

Air streams
Air streams influence the echo time, however, air flow speeds of up to 10 m/s are insignificant. In conditions where turbulences prevail, e.g. above glowing metal, the use of ultrasonic sensors is not recommended, because the echo of distorted sound waves is difficult to evaluate.
Environmental conditions
Normal concentrations of rain or snow do not affect the sensor but direct wetting of the transducers should be avoided. The transducer types CP40 are not protected against humidity (degree of protection IP40). All other ultrasonic sensors are not damaged by water but correct functionality may be impaired. Therefore, the ultrasonic transducers should generally not be subjected to direct wetting.

source : http://www.tektron.ie/ultrasonic_sensors.htm

```