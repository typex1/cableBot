notes on cableBotClient

Logic:

* Controller class: defines REST methods, invokes base Pojos for object creation, e.g. line creation.
* Pojo class, e.g. Line class: contains getter/setter and helper methods
* Service class: takes care for storing Pojo objects in arrays, also deleting or persisting them


REST methods:
------------

$ find . -type f -exec grep -H RequestMethod {} \;

Line-POST:

./line/LineController.java:     @RequestMapping(method=RequestMethod.POST, value = "/lines")

./line/LineController.java:     @RequestMapping(method=RequestMethod.POST, value = "/linesURL/{id}/{x1}/{y1}/{z1}/{x2}/{y2}/{z2}/{servo}")

* id=line number
* x,y,z: coordinates in cartesian system (unit: motor steps)
* servo: servo values allowed by pi4j/wiringPi/GPIO, i.e. 0 - 180.

Line-PUT:

./line/LineController.java:     @RequestMapping(method=RequestMethod.PUT, value = "/lines/{id}")

Line-DELETE:

./line/LineController.java:     @RequestMapping(method=RequestMethod.DELETE, value = "/lines/{id}")

./line/LineController.java:     @RequestMapping(method=RequestMethod.DELETE, value = "/linesClear")

Motor-POST:

./motor/MotorController.java:import org.springframework.web.bind.annotation.RequestMethod;

./motor/MotorController.java:   @RequestMapping(method=RequestMethod.POST, value = "/moveMM/{x}/{y}/{z}")
