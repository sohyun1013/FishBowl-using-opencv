# Import the required modules

import cv2
import argparse

import MySQLdb

db = MySQLdb.connect(host="localhost", user="pi", passwd="**", db="example")
curs = db.cursor()

def run(im, multi=False):

    im_disp = im.copy()

    im_draw = im.copy()

    window_name = "Select objects to be tracked here."

    #cv2.namedWindow(window_name, cv2.WINDOW_NORMAL)

    cv2.imshow(window_name, im_draw)



    # List containing top-left and bottom-right to crop the image.

    pts_1 = []
    pts_2 = []

    rects = []
    def android_db():
        nameSelect = "select x1, y1, x2, y2 from fishXY where end='false'"
        curs.execute(nameSelect)
        rows = curs.fetchall()

        for row in rows:
            pts_1.append((int(float(row[0])), int(float(row[1]))))
            pts_2.append((int(float(row[2])), int(float(row[3]))))
        print(pts_1)
    android_db()

    while True:

        # Draw the rectangular boxes on the image

        window_name_2 = "Objects to be tracked."


        for pt1, pt2 in zip(pts_1, pts_2):

            rects.append([pt1[0],pt2[0], pt1[1], pt2[1]])

            cv2.rectangle(im_disp, pt1, pt2, (255, 255, 255), 3)

        # Display the cropped images

        #cv2.namedWindow(window_name_2, cv2.WINDOW_NORMAL)

        cv2.imshow(window_name_2, im_disp)

        key = cv2.waitKey(30)

        #if key == ord('p'):

            # Press key `s` to return the selected points

        cv2.destroyAllWindows()

        point= [(tl + br) for tl, br in zip(pts_1, pts_2)]

        corrected_point=check_point(point)

        return corrected_point

        if key == ord('q'):

            # Press key `q` to quit the program

            print("Quitting without saving.")

            exit()

        elif key == ord('d'):

            # Press ket `d` to delete the last rectangular region

            if pts_1:

                print("Object deleted at  [{}, {}]".format(pts_1[-1], pts_2[-1]))

                pts_1.pop()

                pts_2.pop()

                im_disp = im.copy()

            else:

                print("No object to delete.")

    cv2.destroyAllWindows()

    point= [(tl + br) for tl, br in zip(pts_1, pts_2)]

    corrected_point=check_point(point)

    return corrected_point



def check_point(points):

    out=[]

    for point in points:

        #to find min and max x coordinates

        if point[0]<point[2]:

            minx=point[0]

            maxx=point[2]

        else:

            minx=point[2]

            maxx=point[0]

        #to find min and max y coordinates

        if point[1]<point[3]:

            miny=point[1]

            maxy=point[3]

        else:

            miny=point[3]

            maxy=point[1]

        out.append((minx,miny,maxx,maxy))



    return out


if __name__ == "__main__":

    ap = argparse.ArgumentParser()

    ap.add_argument("-i", "--imagepath", required=True, help="Path to image")



    args = vars(ap.parse_args())



    try:

        im = cv2.imread(args["imagepath"])

    except:

        print("Cannot read image and exiting.")

        exit()

    points = run(im)

    print("Rectangular Regions Selected are -> ", points)
