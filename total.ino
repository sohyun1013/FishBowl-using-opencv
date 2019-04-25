#include <OneWire.h>

int TempSensor = 2;

OneWire ts(TempSensor);

void setup() {
  Serial.begin(9600);
}

void loop() {
  int val;
  val = analogRead(1);
  
  float temperature = getTemp();
  int sensorValue = analogRead(A0);
  Serial.println(temperature);

  float voltage = sensorValue * (5.0 / 1024.0);
  Serial.println(voltage); 
  delay(30000);
}

float getTemp() {
  byte data[12];
  byte addr[8];
  if ( !ts.search(addr)) {
    ts.reset_search();
    return -1000;
  }
  if ( OneWire::crc8( addr, 7) != addr[7]) {
    Serial.println("CRC is not valid!");
    return -1000;
  }
  if ( addr[0] != 0x10 && addr[0] != 0x28) {
    Serial.print("Device is not recognized");
    return -1000;
  }

  ts.reset();
  ts.select(addr);
  ts.write(0x44, 1);
  byte present = ts.reset();
  ts.select(addr);
  ts.write(0xBE);

  for (int i = 0; i < 9; i++)  {
    data[i] = ts.read();
  }

  ts.reset_search();
  byte MSB = data[1];
  byte LSB = data[0];
  float tempRead = ((MSB << 8) | LSB);
  float TemperatureSum = tempRead / 16;
  return TemperatureSum;
}
