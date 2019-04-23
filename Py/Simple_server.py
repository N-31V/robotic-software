#!/usr/bin/env python

import socket

sock = socket.socket()
sock.bind(('localhost', 9090))
while True:
	sock.listen(1)
	conn, addr = sock.accept()
	print 'connected:', addr
	while True:
		data = conn.recv(1024)
		if not data:
			break
		print data
		conn.send(data.upper())
	conn.close()
