#!/usr/bin/env python
# coding: ascii 
import roslib; roslib.load_manifest('camera')
import rospy
import sys
import cv2 as cv
from PIL import Image, ImageDraw
from std_msgs.msg import String
from sensor_msgs.msg import Image as Img
from cv_bridge import CvBridge, CvBridgeError

'''
def callback(data):
	bridge = CvBridge();
	cv_image = bridge.imgmsg_to_cv2(data, "bgr8")
	cv.imshow('image',cv_image)
	cv.waitKey(0)
	cv.destroyAllWindows()

def camera_listener():
    rospy.init_node('camera_listener', anonymous=True)
    rospy.Subscriber("/usb_cam/image_raw", Img, callback)
    rospy.spin()

if __name__ == '__main__':
    camera_listener()
'''


class CameraListener:	
	
	def __init__(self):
		self.bridge = CvBridge()
		self.sub = rospy.Subscriber("/usb_cam/image_raw", Img, self.callback)

	def callback(self,data):
		cv_image = self.bridge.imgmsg_to_cv2(data, "bgr8")
		cv.imshow('image',cv_image)
		cv.waitKey(0)
		cv.destroyAllWindows()

def main(args):
  cl = CameraListener()
  rospy.init_node('camera_listener', anonymous=True)
  try:
    rospy.spin()
  except KeyboardInterrupt:
    print("Shutting down")
  cv2.destroyAllWindows()

if __name__ == '__main__':
    main(sys.argv)
