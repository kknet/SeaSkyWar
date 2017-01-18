# coding: utf-8

import sys
import os
import signal

if __name__ == '__main__':
    try:
        pidfile = file('../proc/pid', 'r')    
        data = pidfile.readlines()
        pidfile.close()
	pid = data[0]
        os.kill(int(pid), signal.SIGUSR1)
    except:
	pass
