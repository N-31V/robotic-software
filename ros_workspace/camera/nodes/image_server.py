#!/usr/bin/env python

import numpy as np
import cv2 as cv
import socket

def response(tmp):
	nparr = np.fromstring(tmp, np.uint8)
	img = cv.imdecode(nparr, 1)
	cv.namedWindow ('Image', cv.WINDOW_NORMAL)
	cv.imshow ( 'Image' , img)
	cv.waitKey (0)
	cv.destroyAllWindows()
	cv.imwrite ( "/home/n-31v/robotic-software/ros_workspace/camera/nodes/cv_image.jpg" , img)
	return 'response'

if __name__ == "__main__":
	sock = socket.socket()
	sock.bind(('', 9090))
	print('server is ready')
	go=True
	while go:
		sock.listen(1)
		conn, addr = sock.accept()
		conn.settimeout(1)
		print('connected:', addr)
		tmp=''
		try:
			while True:			
				data = conn.recv(1024)
				if not data:
					break	
				if data =='end':
					go = False
					print (data)
					break
				else:
					tmp+=data
					print('reading...') 
		except socket.timeout:
			print('timeout')
		else:
			print('ok')		
		finally:			
			if go: 
				print('doing response')
				conn.send(response(tmp))	
			conn.close()
			print('socket close')	
	print('exit')

