import dlib
import cv2
import argparse as ap
import get_points
import MySQLdb
import time
import math
from datetime import datetime

#db connection
db = MySQLdb.connect(host="localhost", user="pi", passwd="**", db="example")
curs = db.cursor()

def run(source=0, dispLoc=False):
    # initialize list
    cx = [0.0, 0.0]
    cy = [0.0, 0.0]
    curCx = [0.0, 0.0]
    curCy = [0.0, 0.0]
    prevTime = 0.0
    prevCx = [0.0, 0.0]
    prevCy = [0.0, 0.0]
    frameGap =[0.0, 0.0]
    gapCxCy =[0.0, 0.0]
    velocity = [0.0, 0.0]
    state =[]
    # Create the VideoCapture object
    cam = cv2.VideoCapture(source)

    # If Camera Device is not opened, exit the program

    if not cam.isOpened():
        print("Video device or file couldn't be opened")

        exit()

    print("Press key `p` to pause the video to start tracking")

    while True:

        # Retrieve an image and Display it.

        retval, img = cam.read()

        if not retval:
            print("Cannot capture frame device")

            exit()

        if (cv2.waitKey(10) == ord('p')):
            break

        cv2.namedWindow("Image", cv2.WINDOW_NORMAL)

        cv2.imshow("Image", img)
        height,width = img.shape[:2]
        print(height, width)
        #db 좌표 비우기
        dbdelete = "delete from fishXY"
        nothinginsert = "insert into fishXY values ('0', '0', '0', '0', '0', 'nothing')"
        curs.execute(dbdelete)
        curs.execute(nothinginsert)
        db.commit()
        #img를 안드로이드에 보내기
        cv2.imwrite('D:/fish/startimage.jpg', img)

    cv2.destroyWindow("Image")


    while True:
        def check_db():
            endSelect = "select end from fishXY"
            curs.execute(endSelect)
            rows = curs.fetchall()

            for row in rows:
                state.append(row[0])
            print(state[-1])

        check_db()

        if state[-1]=='true':

            points = get_points.run(img, multi=True)

            if not points:
                print("ERROR: No object to be tracked.")
                exit()

            cv2.namedWindow("Image", cv2.WINDOW_NORMAL)

            cv2.imshow("Image", img)

            # Initial co-ordinates of the object to be tracked

            # Create the tracker object

            tracker = [dlib.correlation_tracker() for _ in range(len(points))]

            # Provide the tracker the initial position of the object

            [tracker[i].start_track(img, dlib.rectangle(*rect)) for i, rect in enumerate(points)]

            while True:

                # Read frame from device or file

                retval, img = cam.read()

                if not retval:
                    print("Cannot capture frame device | CODE TERMINATION :( ")

                    exit()

                # Update the tracker
                for i in range(len(tracker)):

                    tracker[i].update(img)

                    # Get the position of th object, draw a

                    # bounding box around it and display it.

                    rect = tracker[i].get_position()

                    pt1 = (int(rect.left()), int(rect.top()))

                    pt2 = (int(rect.right()), int(rect.bottom()))

                    cv2.rectangle(img, pt1, pt2, (255, 255, 255), 4)

                    # print("Object {} tracked at [{}, {}] \r".format(i, pt1, pt2),)
                    cx[i] = int((pt1[0] + pt2[0]) / 2)  # center x
                    cy[i] = int((pt1[1] + pt2[1]) / 2)  # center y
                    # cv2.circle(img, (cx[i], cy[i]), 10, (0,0,255), -1)
                    frameGap[i] = frameGap[i] + 2
                    if (frameGap[i] % 3) == 0:
                        curTime = time.time()
                        sec = curTime - prevTime
                        curCx[i] = cx[i]
                        curCy[i] = cy[i]
                        a = curCx[i] - prevCx[i]
                        b = curCy[i] - prevCy[i]
                        gapCxCy[i] = math.sqrt((a * a) + (b * b))
                        velocity[i] = (gapCxCy[i]) / (sec)

                        now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                        hour = datetime.now().strftime('%H')
                        hour = int(hour)

                        if hour == 9 or hour == 10 or hour == 11:
                            curs.execute('insert into fishLocationMorning values(%s, %s, %s, %s, %s)',
                                         (i, str(curCx[i]), str(curCy[i]), str(velocity[i]), now))

                        elif hour == 12 or hour == 13 or hour == 14:
                            curs.execute('insert into fishLocationNoon values(%s, %s, %s, %s, %s)', (i, str(curCx[i]), str(curCy[i]), str(velocity[i]),now))

                        elif hour == 15 or hour == 16 or hour == 17 or hour == 18:
                            curs.execute('insert into fishLocationAfternoon values(%s, %s, %s, %s, %s)', (i, str(curCx[i]), str(curCy[i]), str(velocity[i]),now))

                        else:
                            curs.execute('insert into fishLocationEvening values(%s, %s, %s, %s, %s)', (i, str(curCx[i]), str(curCy[i]), str(velocity[i]),now))

                        # cv2.putText(img, "Velocity :" + str(int(velocity)), (100, 80), cv2.FONT_HERSHEY_SCRIPT_SIMPLEX, 0.75, (50, 170, 50),2)

                        db.commit()
                        prevTime = curTime
                        prevCx[i] = curCx[i]
                        prevCy[i] = curCy[i]

                    if dispLoc:
                        loc = (int(rect.left()), int(rect.top() - 20))

                        txt = "fish tracked at [{}, {}]".format(pt1, pt2)

                        cv2.putText(img, txt, loc, cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)

                cv2.namedWindow("Image", cv2.WINDOW_NORMAL)

                cv2.imshow("Image", img)

                # Continue until the user presses ESC key

                if cv2.waitKey(1) == 27:
                    break

            # Relase the VideoCapture object

            cam.release()
            exit()

        elif state[-1]=='nothing' or state[-1]=='false':
            db.commit()
            pass

if __name__ == "__main__":

    # Parse command line arguments

    parser = ap.ArgumentParser()

    group = parser.add_mutually_exclusive_group(required=True)

    group.add_argument('-d', "--deviceID", help="Device ID")

    group.add_argument('-v', "--videoFile", help="Path to Video File")

    parser.add_argument('-l', "--dispLoc", dest="dispLoc", action="store_true")

    args = vars(parser.parse_args())

    # Get the source of video

    if args["videoFile"]:

        source = args["videoFile"]

    else:

        source = int(args["deviceID"])

    run(source, args["dispLoc"])
