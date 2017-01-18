# coding: utf-8

from socket import *
from select import *
from MsgHandler import *
from ctypes import *
import struct

from message_pb2 import *

from Log import *


class BaseModule(Module):
    def procMsg(self, user_data):
        if user_data["msg_id"] == Login_Request:
            self.onLoginRequest(user_data)
        elif user_data["msg_id"] == Register_Request:
            self.onRegisterRequest(user_data)
        elif user_data["msg_id"] == Debug_Command:
            self.onDebugCommand(user_data)
        else:
            Log().d("No function to response msg.(msgid=" + str(msg_id) + ")")

    # 响应登录请求消息
    def onLoginRequest(self, user_data):
        req = Request()
        req.ParseFromString(user_data["msg_data"])
        if req.HasField('login'):
            Log().d('onLoginRequest(' + req.login.username +"," + req.login.password + ")")
            # TODO
            res = Response()
            res.result = True
            res.last_response = True
            res.login.token = 1234
            res_str = res.SerializeToString()
            Log().d("len(reponse)="+str(len(res_str)))
            self.sendMsg(user_data, Login_Response, res_str)



    # 响应注册注册消息
    def onRegisterRequest(self, user_data):
        req = Request()
        req.ParseFromString(user_data["msg_data"])
        if req.HasField('register'):
            Log().d('onRegisterRequest(' + req.register.username + "," + req.register.password + ")")
            res = Response()
            res.result = True
            res.last_response = True
            res_str = res.SerializeToString()
            self.sendMsg(user_data, Register_Response, res_str)

    # 响应Debug命令
    def onDebugCommand(self, user_data):
        cmd = Command()
        cmd.ParseFromString(user_data["msg_data"])
        if cmd.HasField('debug'):
            Log().d('onDebugCommand(' + cmd.debug.command + ")")
            notify = Notification()
            notify.welcome.text = "Welcome, i received your debug command!"

    # 发送串行化后的数据
    def sendMsg(self, user_data, msgId, serializeData):
        data_len = len(serializeData) + 8
        buf = create_string_buffer(8)
        struct.pack_into('!I', buf, 0, msgId)
        struct.pack_into('!I', buf, 4, data_len)
        user_data["wbuf"] += (buf.raw + serializeData)
        user_data["epl"].modify(user_data["fd"], EPOLLOUT)
        
        
