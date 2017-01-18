#coding: utf-8

from Singleton import *
from abc import *


class Module:
    def __init__(self, moduleId):
        self.moduleId = moduleId

    @abstractmethod
    def procMsg(self, msg):
        pass


@singleton
class MsgHandler:
    def __init__(self):
	self.modules = {}

    def registerModule(self, module):
        self.modules[module.moduleId] = module

    def handleMsg(self, user_data):
        module = self.modules[user_data["msg_id"] & 0xFFFF0000]
        if module is not None:
            module.procMsg(user_data)
