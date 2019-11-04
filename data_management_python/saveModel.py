import numpy as np
import matplotlib.pyplot as plt
import matplotlib
import MySQLdb
from datetime import datetime

db = MySQLdb.connect(host="localhost",user="pi",passwd="**",db="example")

curs = db.cursor()

def morningSave():
    idSelect = "select distinct id from fishLocationMorning"
    curs.execute(idSelect)
    idRows = curs.fetchall()
    idList = []
    for row in idRows:
        idList.append(int(row[0]))

    for idNo in idList:
        sql = "select velocity from fishLocationMorning where id="+str(idNo)
        curs.execute(sql)
        rows = curs.fetchall()
        vList = []
        
        for row in rows:
            vList.append(float(row[0]))
    
        velocity = np.array(vList)
        velocityAvg = velocity.mean()
        velocitySD = velocity.std()

        curs.execute('insert into saveMorning values(%s, %s, %s)', (idNo, str(velocityAvg), str(velocitySD)))
        db.commit()


def noonSave():
    idSelect = "select distinct id from fishLocationNoon"
    curs.execute(idSelect)
    idRows = curs.fetchall()
    idList = []
    for row in idRows:
        idList.append(int(row[0]))

    for idNo in idList:
        sql = "select velocity from fishLocationNoon where id="+str(idNo)
        curs.execute(sql)
        rows = curs.fetchall()
        vList = []
        
        for row in rows:
            vList.append(float(row[0]))

        velocity = np.array(vList)
        velocityAvg = velocity.mean()
        velocitySD = velocity.std()

        curs.execute('insert into saveNoon values(%s, %s, %s)', (idNo, str(velocityAvg), str(velocitySD)))
        db.commit()

def afternoonSave():
    idSelect = "select distinct id from fishLocationAfternoon"
    curs.execute(idSelect)
    idRows = curs.fetchall()
    idList = []
    for row in idRows:
        idList.append(int(row[0]))

    for idNo in idList:
        sql = "select velocity from fishLocationAfternoon where id="+str(idNo)
        curs.execute(sql)
        rows = curs.fetchall()
        vList = []
        
        for row in rows:
            vList.append(float(row[0]))

        velocity = np.array(vList)
        velocityAvg = velocity.mean()
        velocitySD = velocity.std()

        curs.execute('insert into saveAfternoon values(%s, %s, %s)', (idNo, str(velocityAvg), str(velocitySD)))
        db.commit()

def eveningSave():
    idSelect = "select distinct id from fishLocationEvening"
    curs.execute(idSelect)
    idRows = curs.fetchall()
    idList = []
    for row in idRows:
        idList.append(int(row[0]))

    for idNo in idList:
        sql = "select velocity from fishLocationEvening where id="+str(idNo)
        curs.execute(sql)
        rows = curs.fetchall()
        vList = []
        
        for row in rows:
            vList.append(float(row[0]))

        velocity = np.array(vList)
        velocityAvg = velocity.mean()
        velocitySD = velocity.std()

        curs.execute('insert into saveEvening values(%s, %s, %s)', (idNo, str(velocityAvg), str(velocitySD)))
        db.commit()

moringSave()
noonSave()
afternoonSave()
eveningSave()
