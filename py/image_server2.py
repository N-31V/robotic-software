#!/usr/bin/env python3

import numpy as np
import cv2 as cv
import socket
from cnn_chocolatevanilla import *

def response(tmp, mod):
	nparr = np.fromstring(tmp, np.uint8)
	img = cv.imdecode(nparr, 1)
	img_np=np.array(img)
	data = img_np.reshape(IMG_WIDTH,IMG_HEIGHT,3)
	model_out = model.predict([data])[0]
	if np.argmax(model_out) == 1:
		bstr_label=b'true'
		str_label='Vanilla'
	else: 
		bstr_label=b'false'
		str_label='Chocolate'
	#cv.imshow ( str_label , img)
	#cv.waitKey (0)
	#cv.destroyAllWindows()
	cv.imwrite ( "/home/n-31v/robotic-software/py/final/cv_image.jpg" , img)
	return bstr_label

if __name__ == "__main__":
	model = make_model()
	sock = socket.socket()
	sock.bind(('', 9099))
	print('server is ready')
	go=True
	while go:
		sock.listen(1)
		conn, addr = sock.accept()
		conn.settimeout(1)
		print('connected:', addr)
		tmp=b''
		try:
			while True:			
				data = conn.recv(1024)
				if not data:
					break	
				if data ==b'end':
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
				conn.send(response(tmp, model))	
			conn.close()
			print('socket close')	
	print('exit')

