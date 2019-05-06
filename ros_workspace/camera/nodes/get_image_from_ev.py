#!/usr/bin/env python

import socket

sock = socket.socket()
sock.bind(('', 9090))
go=True

while go:
	sock.listen(1)
	conn, addr = sock.accept()
	print('connected:', addr)
	with open("/home/n-31v/robotic-software/ros_workspace/camera/nodes/image.jpg", "wb+") as f:
		while True:
			data = conn.recv(1024)
			if not data:
				break	
			if data == "end\n":
				go = False
				print (data)
				break
			else:
				f.write(data)
				print('writting...') 
		conn.close()
		print('socket close')
print('exit')
