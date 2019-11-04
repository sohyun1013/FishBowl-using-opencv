# FishBowl-using-opencv

### SmartFishBowl
  안드로이드 앱
  
### data-management_python(Raspberry pi)
  checkValue.py :calculate average of velocity, object location etc.. and send push notification to android in time
  getArduinoData.py : get arduino data and save data to database
  saveModel.py : save sample data to database

### object-tracking-with-dlib(Window 10)
##### python object-tracker-multiple.py -v nillamoving.mp4

In raspberry with pi camera, if you want to use it with streaming, change "-v nillamoving.mp4" to "http://localhost:8081"
  this uses 'motion streaming'.. "sudo apt-get install motion"
  - object tracking with dlib
  -calculate velocity, center of object
  -save data in Database
  -check multi object id

### php_file
  captureImage.php : 물고기의 이름을 지정하기 위한 어항 캡처 사진
  fishInput.php : 
  fishXY_0.php : 
  fishXY_1.php : 
  nowStateLoc.php : 
  nowStateVelo.php : 
  sensorDB.php : 
  
### [논문]영상처리 기반 물고기 이상 증세 관리 어항의 개발

