#!/usr/bin/env python

import socket

sock = socket.socket()
sock.bind(('', 9090))
go=True
with open("Py/ggg.jpg", "wb+") as f:
	while go:
		sock.listen(1)
		conn, addr = sock.accept()
		print 'connected:', addr
		while True:
			data = conn.recv(1024)
			if not data:
				break
		
			if data == "q\n":
				go = False
				print (data)
				conn.send(data.upper())
			else:
				f.write(data)
				print('writting...') 
		conn.close()
