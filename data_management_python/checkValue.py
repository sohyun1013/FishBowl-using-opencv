import numpy as np
import matplotlib.pyplot as plt
import matplotlib
import MySQLdb
from datetime import datetime
from pyfcm import FCMNotification
import RPi.GPIO as GPIO
import time
import threading

GPIO.setmode(GPIO.BCM)
GPIO.setup(16,GPIO.OUT)

device_id = "epND3LvN95M:APA91bEUQv89P9235QM7XG1p1LAi1DfP2m_H6yFNm8ffKgLmv-IgaRWMqe0mglU_acwmqzUXGKXB1mgDAQt8UTVJChIzi_bPs6poZ97HNyusxUewhVyLHe4j2-jsFSpgbHIzIQNeuiu4"
server_key = "####"
push_service = FCMNotification(api_key=server_key)

db = MySQLdb.connect(host="localhost",user="pi",passwd="**",db="example")
curs = db.cursor()
curs1 = db.cursor()

def sendMessage(msg):
    registration_id = device_id
    message_title = "Smart Fish Bowl"
    message_body = msg

    data_message = { "body" : message_body }
    
    # 스마트폰에 현재 상태를 알림
    result = push_service.notify_single_device(registration_id=registration_id,message_title=message_title,
                                               message_body=message_body,data_message=data_message)

def locationMeasure(inNo, newdata_Y):
    #topBase = 200 Y top 20%
    #bottomBase = 620 Y bottom 20%
    Y = np.array(newdata_Y)
    # 스마트폰에 보낼 문자열
    loc = ""
    topError = (Y<200)
    bottomError = (Y>620)
    
    #Y의 좌표값의 80% 이상이 200(상단)보다 위에 있으면 이상이 있다고 간주
    if np.sum(topError) > (len(newdata_Y)*0.7):
        sendMessage("물고기가 수면에 오래 머무릅니다.")
        loc = "top"
        GPIO.output(16,True)

    elif np.sum(bottomError) > (len(newdata_Y)*0.7):
        sendMessage("물고기가 바닥에 오래 머무릅니다.")
        loc = "bottom"

    else:
        sendMessage("정상입니다.")
        loc = "locNormal"
        GPIO.output(16,True)
    
    curs1.execute('insert into nowStateLoc values(%s, %s)',(idNo,loc))
    db.commit()

def velocityMeasure(idNo, newdata_V,velocityAvg,velocitySD):
    velocityAvg = float(velocityAvg)
    velocitySD = float(velocitySD)
    Data = np.array(newdata_V)
    DataAvg = Data.mean()
    velo = ""
    print(DataAvg, velocityAvg)
    if DataAvg < velocityAvg * 0.5:
        sendMessage("움직임이 너무 느려요. 상태를 확인해주세요.")
        print("too slow")
        velo = "slow"
    elif DataAvg > velocityAvg * 2:
        sendMessage("움직임이 너무 빨라요. 시스템의 이상이 예상됩니다.")
        print("too fast")
        velo = "fast"

    else:
        print("speed normal")
        velo = "veloNormal"

    curs1.execute('insert into nowStateVelo values (%s, %s)', (idNo, velo))
    db.commit()

#릴레이 초기화
GPIO.cleanup()
time.sleep(2)

#시간대별 데이터 받아와서 계산
def run():
    now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    hour = int(datetime.now().strftime('%H'))
    
    idSelect = "select distinct id from fishLocation"
    curs.execute(idSelect)
    idRows = curs.fetchall()
    idList =  []
    for row in idRows:
        idList.append(int(row[0]))
    
    if hour == 9 or hour == 10 or hour == 11:
        for idNo in idList:
            sql = "select curCx,curCy,velocity,nowTime from fishLocationMorning where id="+str(idNo)
            curs.execute(sql)
            rows = curs.fetchall()
        
            sql1 = "select velocityAvg,velocitySD from saveMorning where id="+str(idNo)
            curs.execute(sql1)
            r = curs.fetchall()
    
            for rr in r:
                velocityAvg = rr[0]
                velocitySD = rr[1]
        
            dList = [], vList = [], xList = [], yList = [], i = 0
        
            nowDay = datetime.now().strftime('%Y-%m-%d')
        
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
    
    elif hour == 12 or hour == 13 or hour == 14:
        for idNo in idList:
            sql = "select curCx,curCy,velocity,nowTime from fishLocationNoon where id="+str(idNo)
            curs.execute(sql)
            rows = curs.fetchall()
            
            sql1 = "select velocityAvg,velocitySD from saveNoon where id="+str(idNo)
            curs.execute(sql1)
            r = curs.fetchall()
            
            for rr in r:
                velocityAvg = rr[0]
                velocitySD = rr[1]
            
            dList = [], vList = [], xList = [], yList = [], i = 0
            
            nowDay = datetime.now().strftime('%Y-%m-%d')
            
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

    elif hour == 15 or hour == 16 or hour == 17:
        for idNo in idList:
            sql = "select curCx,curCy,velocity,nowTime from fishLocationAfternoon where id="+str(idNo)
            curs.execute(sql)
            rows = curs.fetchall()
        
            sql1 = "select velocityAvg,velocitySD from saveAfternoon where id="+str(idNo)
            curs.execute(sql1)
            r = curs.fetchall()
        
            for rr in r:
                velocityAvg = rr[0]
                velocitySD = rr[1]
            
            dList = [], vList = [], xList = [], yList = [], i = 0
            
            nowDay = datetime.now().strftime('%Y-%m-%d')
            
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

    else:
        for idNo in idList:
            sql = "select curCx,curCy,velocity,nowTime from fishLocationNoon where id="+str(idNo)
            curs.execute(sql)
            rows = curs.fetchall()
        
            sql1 = "select velocityAvg,velocitySD from saveNoon where id="+str(idNo)
            curs.execute(sql1)
            r = curs.fetchall()
            
            for rr in r:
                velocityAvg = rr[0]
                velocitySD = rr[1]
    
            dList = [], vList = [], xList = [], yList = [], i = 0
            
            nowDay = datetime.now().strftime('%Y-%m-%d')
            
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

    threading.Timer(10800, run).start()

run()
