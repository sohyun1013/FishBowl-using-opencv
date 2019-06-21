import numpy as np
import matplotlib.pyplot as plt
import matplotlib
import MySQLdb
from datetime import datetime
from pyfcm import FCMNotification
#import RPi.GPIO as GPIO
#import time

#GPIO.setmode(GPIO.BCM)
#GPIO.setup(16,GPIO.OUT)
#import firebase_admin
#from firebase_admin import credentials

#cred = credentials.Certificate("/media/pi/JUNGMIN/smartfishbowl2.json")
#firebase_admin.initialize_app(cred)

#push_service = FCMNotification(api_key="AIzaSyDUgvJtkek3XMlNlkhabziNa-4sNoU-Ou0")
#mToken = "AAAAiwXvMLY:APA91bEtfWuXWxSbUOxQp4Nj5pc4lpRk9zbs4c9ptKjbeThqfLzq97J2iJHXp9VDf9FltQz8ZH9WfzZmMsG8vnIr1sNBdlGTjO8-LtyBmgvfNjQLrnv6ljP5gruq9Phbl483WczvyMmD"
device_id = "epND3LvN95M:APA91bEUQv89P9235QM7XG1p1LAi1DfP2m_H6yFNm8ffKgLmv-IgaRWMqe0mglU_acwmqzUXGKXB1mgDAQt8UTVJChIzi_bPs6poZ97HNyusxUewhVyLHe4j2-jsFSpgbHIzIQNeuiu4"
server_key = "AAAAiwXvMLY:APA91bEtfWuXWxSbUOxQp4Nj5pc4lpRk9zbs4c9ptKjbeThqfLzq97J2iJHXp9VDf9FltQz8ZH9WfzZmMsG8vnIr1sNBdlGTjO8-LtyBmgvfNjQLrnv6ljP5gruq9Phbl483WczvyMmD"
push_service = FCMNotification(api_key=server_key)

db = MySQLdb.connect(host="localhost",user="pi",passwd="raspberry",db="example")
curs = db.cursor()
curs1 = db.cursor()

def sendMessage(msg):
    registration_id = device_id
    message_title = "Smart Fish Bowl"
    message_body = msg

    data_message = { "body" : message_body }

    result = push_service.notify_single_device(registration_id=registration_id,message_title=message_title,message_body=message_body,data_message=data_message)
    print(result)

def locationMeasure(inNo, newdata_Y):
    #topBase = 200 #Y top 20%
    #bottomBase = 620 #Y bottom 20%
    Y = np.array(newdata_Y)
    loc = ""
    topError = (Y<200)
    bottomError = (Y>620)
    
#Y의 좌표값의 80% 이상이 200(상단)보다 위에 있으면 이상이 있다고 간주
    if np.sum(topError) > (len(newdata_Y)*0.7):
        sendMessage("물고기가 수면에 오래 머물러요")
        print("have top problem")
        loc = "top"
        #time.sleep(2)
        #GPIO.output(16,True)
        #time.sleep(2)

    elif np.sum(bottomError) > (len(newdata_Y)*0.7):
        sendMessage("물고기가 바닥에 오래 머물러요")
        print("have bottom problem")
        loc = "bottom"

    else:
        print("location normal")
        loc = "locNormal"
        #time.sleep(2)
        #GPIO.output(16,True)
        #time.sleep(2)
    
    #stateLoc = "insert into nowStateLoc values (" + str(idNo) + "," + loc + ")"
    curs1.execute('insert into nowStateLoc values(%s, %s)',(idNo,loc))
    db.commit()

def velocityMeasure(idNo, newdata_V,velocityAvg,velocitySD):
    velocityAvg = float(velocityAvg)
    velocitySD = float(velocitySD)
    Data = np.array(newdata_V)
    DataAvg = Data.mean()
    X = (DataAvg - velocityAvg)/velocitySD
    velo = ""
    #if X > 0.85:
    #    print("fast")
    #elif X < -0.85:
    #    print("slow")
    #else:
    #    print("normal")
    print(DataAvg, velocityAvg)
    if DataAvg < velocityAvg*0.5:
        sendMessage("움직임이 너무 느려요. 상태를 확인해주세요.")
        print("too slow")
        velo = "slow"
    elif DataAvg > velocityAvg*2:
        sendMessage("움직임이 너무 빨라요. 시스템의 이상이 예상됩니다.")
        print("too fast")
        velo = "fast"

    else:
        print("speed normal")
        velo = "veloNormal"

    #stateVelo = "insert into nowStateVelo values (" + str(idNo) + "," +  velo + ")"
    curs1.execute('insert into nowStateVelo values (%s, %s)', (idNo, velo))
    db.commit()

#릴레이 초기화
#GPIO.cleanup()
#time.sleep(2)

morning = True
#시간대별 데이터 받아와서 계산
while True:
    now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    hour = int(datetime.now().strftime('%H'))
    minute = int(datetime.now().strftime('%M'))
    second = int(datetime.now().strftime('%S'))
    
    if morning==True and hour == 23  and minute == 34  and second == 00:
        idSelect = "select distinct id from fishLocation"
        curs.execute(idSelect)
        idRows = curs.fetchall()
        idList =  []
        for row in idRows:
            idList.append(int(row[0]))

        for idNo in idList:
            sql = "select curCx,curCy,velocity,nowTime from fishLocation where id="+str(idNo)
            curs.execute(sql)
            rows = curs.fetchall()

            sql1 = "select velocityAvg,velocitySD from save where id="+str(idNo)
            curs.execute(sql1)
            r = curs.fetchall()
            nowDay = datetime.now().strftime('%Y-%m-%d')
            for rr in r:
                velocityAvg = rr[0]
                velocitySD = rr[1]

            dList = []
            vList = []
            xList = []
            yList = []

            i = 0
            for row in rows:
                dList.append(row[3].strftime('%Y-%m-%d'))
                if nowDay == dList[i]:
                    vList.append(float(row[2]))
                    xList.append(int(row[0]))
                    yList.append(int(row[1]))
                else:
                    pass
                    i=i+1

            locationMeasure(idNo, yList)
            velocityMeasure(idNo, vList, velocityAvg, velocitySD)
        morning = False
