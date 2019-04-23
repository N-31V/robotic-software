#!/usr/bin/env python

import socket

sock = socket.socket()
sock.connect(('localhost', 9090))
sock.send('hello, world!\n')

data = sock.recv(1024)
sock.close()

print data
