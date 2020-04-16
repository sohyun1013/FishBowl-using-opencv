# 영상처리를 이용한 물고기 이상 증세 관리 어항

<br>

## 1. 구현 기간 
2019.02-2019.08

<br>

## 2. 개발 배경
* 물고기를 양육하는 사람들이 늘어남
* 물고기를 편리하게 관리할 수 있는 장치의 필요성 증가

<br>

## 3. 개발 목표
* 영상처리 알고리즘을 통해 물고기의 움직임을 파악하여 의심스러운 이상 증세를 알려준다.
* 애플리케이션을 통해 물고기의 상태와 어항의 환경 정보를 확인하고, 이상 증세를 빠르게 발견할 수 있는 어항을 개발

<br>

## 4. 시스템 구성도

<br>

## 5. 주요 기능
* 어항
  - 수온 제어 기능
  
* 라즈베리파이
  - 객체 설정 및 추적 기능
  - 어항 모니터링
  
* 안드로이드  
  - 실시간 어항 감상
  - 물고기의 통합 모니터링(물고기의 움직임, 수온, 수질 등)
  - 물고기의 움직임 조회(그래프)
  - 어항 환경 조회(그래프)
  - 시간대 별 물고기 상태 변화 알림 

### data-management_python(Raspberry pi)
  - checkValue.py :calculate average of velocity, object location etc.. and send push notification to android in time
  - getArduinoData.py : get arduino data and save data to database
  - saveModel.py : save sample data to database

### object-tracking-with-dlib(Window 10)
##### python object-tracker-multiple.py -v nillamoving.mp4

In raspberry with pi camera, if you want to use it with streaming, change "-v nillamoving.mp4" to "http://localhost:8081"
  this uses 'motion streaming'.. "sudo apt-get install motion"
  - object tracking with dlib
  - calculate velocity, center of object
  - save data in Database
  - check multi object id

### php_file
  - captureImage.php : 물고기의 이름을 지정하기 위한 어항 캡처 사진
  - fishInput.php
  - fishXY_0.php
  - fishXY_1.php 
  - nowStateLoc.php 
  - nowStateVelo.php
  - sensorDB.php 
