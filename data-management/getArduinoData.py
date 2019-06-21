import MySQLdb
import serial

ser = serial.Serial('/dev/ttyACM1',9600)
db = MySQLdb.connect(host="localhost",user="pi", passwd="raspberry", db="sensorValue")
curs = db.cursor()

while 1 : 
	temper = ser.readline()
	tubi = ser.readline()
	print(temper)
	print(tubi)
	try:
		sqlSensor = "insert into sensorData values (" + temper + "," + tubi + ")"
		#sqlTubi = "insert into tubiditySensor values (" + tubi + ")"
		curs.execute(sqlSensor)
		#curs.execute(sqlTubi)
		db.commit()
		print("End")
	except:
		print("error")
		db.rollback()
	
