import MySQLdb
import serial

ser = serial.Serial('/dev/ttyACM1',9600)
db = MySQLdb.connect(host="localhost",user="pi", passwd="**", db="example")
curs = db.cursor()

while 1 : 
	temper = ser.readline()
	tubi = ser.readline()
	print(temper)
	print(tubi)
	try:
		sqlSensor = "insert into sensorData values (" + temper + "," + tubi + ")"
		curs.execute(sqlSensor)
		db.commit()
		print("End")
	except:
		print("error")
		db.rollback()
	
