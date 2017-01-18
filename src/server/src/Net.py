# coding: utf-8

from socket import *
from select import *
from ctypes import *
import struct

from MsgHandler import *
from Log import *
import time
from Timer import *

class Net:
    def __init__(self, ip, port, timeout):
        # 创建服务端socket
        self.server_sock = socket(AF_INET, SOCK_STREAM)
        # 设置地址复用
        self.server_sock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        # 绑定IP地址
        self.server_sock.bind((ip, port))
        # 监听，并设置监听队列长度
        self.server_sock.listen(10)
	Log().d("服务器socket初始化完成，监听IP：" + ip + ":" + str(port))

	# 设置服务端socket为非阻塞方式
        self.server_sock.setblocking(False)

        # 超时时间
	self.timeout = timeout
        self.epl = epoll()

        # 注册服务器监听fd到等待读事件集合
        self.epl.register(self.server_sock.fileno(), EPOLLIN)

        # 保存客户端消息的字典
        self.user_map = {}

        # 文件句柄到所对应套接字对象的字典,格式为{句柄：对象}
	self.fd_to_socket = {self.server_sock.fileno():self.server_sock}
        self.run_flag = True

    def run(self):
        t1 = time.time()
        while self.run_flag:
            try:
                events = self.epl.poll(self.timeout)
                if not events:
                    continue
	    except:
                continue

            for fd, event in events:
                sock = self.fd_to_socket[fd]
                # 如果活动socket为当前服务socket，表示有新的连接
                if sock == self.server_sock:
                    conn, addr = self.server_sock.accept()
                    Log().d("新连接:"+ str(addr))
                    # 新的socket设置为非阻塞
                    conn.setblocking(False)
                    # 注册新连接fd到待读事件集合
                    self.epl.register(conn.fileno(), EPOLLIN)
                    # 把新连接的文件句柄保存到字典
                    self.fd_to_socket[conn.fileno()] = conn
                    # 以新连接的对象为键值，值存储在队列中，保存每个连接的信息
                    user_data = {}
                    user_data["fd"] = conn.fileno()
                    user_data["rbuf"] = ""
                    user_data["wbuf"] = ""
                    user_data["epl"] = self.epl
                    self.user_map[conn] = user_data

                # 关闭事件
                elif event & EPOLLHUP:
                    Log().d("连接关闭")
                    self.epl.unregister(fd)
                    self.fd_to_socket[fd].close()
                    del self.fd_to_socket[fd]
                # 可读事件
                elif event & EPOLLIN:
                    # 接收数据
                    data = sock.recv(1024)
                    if data:
                        Log().d("收到数据,客户端:" + str(sock.getpeername()))
                        self.user_map[sock]["rbuf"] += data
                        bufdata = self.user_map[sock]["rbuf"]
			while len(bufdata) >= 8:
                            msg_id = struct.unpack('!I', bufdata[0:4])[0]
                            data_len = struct.unpack('!I', bufdata[4:8])[0]
                            if len(bufdata) >= data_len:
                                packet_data = bufdata[8:data_len]
                                self.user_map[sock]["rbuf"] = bufdata[data_len:]
                                bufdata = self.user_map[sock]["rbuf"]
                                user_data = self.user_map[sock]
				user_data["msg_id"] = msg_id
                                user_data["msg_data"] = packet_data
				MsgHandler().handleMsg(user_data)
                            else:
                                break
                    else:
                        Log().d("连接关闭，客户端：" + str(sock.getpeername()))
                        self.epl.unregister(fd)
                        self.user_map.pop(sock)
                        sock.close()                        
                        del self.fd_to_socket[fd]
                # 可写事件
                elif event & EPOLLOUT:
                    if len(self.user_map[sock]["wbuf"]) > 0:
                        sock.send(self.user_map[sock]["wbuf"])
                        self.user_map[sock]["wbuf"] = ""
                        Log().d("发送数据,客户端:" + str(sock.getpeername()))
                    else:
                        self.epl.modify(fd, EPOLLIN)
            t2 = time.time()
            delta = t2 - t1
            t1 = t2
            TM().step(delta)
        for fd in self.fd_to_socket:
            self.epl.unregister(fd)
            self.fd_to_socket[fd].close()
        self.epl.close()



