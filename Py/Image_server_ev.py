#!/usr/bin/env python

import socket

sock = socket.socket()
sock.bind(('localhost', 9090))
go=True
with open("Py/ggg.jpg", "wb+") as f:
	sock.listen(1)
	conn, addr = sock.accept()
	print 'connected:', addr
	while True:
		data = conn.recv(1024)
		if not data:
			break;
	conn.close()
