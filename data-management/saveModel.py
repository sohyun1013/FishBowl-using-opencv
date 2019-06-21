import numpy as np
import matplotlib.pyplot as plt
import matplotlib
import MySQLdb
from datetime import datetime

db = MySQLdb.connect(host="localhost",user="pi",passwd="raspberry",db="example")

curs = db.cursor()

#moringSave()
#noonSave()
#afternoonSave()
#evenSave()

def save():
    idSelect = "select distinct id from fishLocation"
    curs.execute(idSelect)
    idRows = curs.fetchall()
    idList = []
    for row in idRows:
        idList.append(int(row[0]))
    
    for idNo in idList:
        sql = "select velocity from fishLocation where id="+str(idNo)
        curs.execute(sql)
        rows = curs.fetchall()
        vList = [] 

        for row in rows:
            vList.append(float(row[0]))

        velocity = np.array(vList)
        velocityAvg = velocity.mean() 
        velocitySD = velocity.std() 

        curs.execute('insert into save values(%s, %s, %s)', (idNo, str(velocityAvg), str(velocitySD)))
        db.commit()

save()
