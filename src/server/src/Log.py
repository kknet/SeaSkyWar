#coding: utf-8

import logging
import logging.handlers
from Singleton import *


@singleton
class Log:
    # 日志等级
    CRITICAL = logging.CRITICAL
    ERROR = logging.ERROR
    WARNING = logging.WARNING
    INFO = logging.INFO
    DEBUG = logging.DEBUG
    NOTSET = logging.NOTSET

    def __init__(self):
        self.logfile = "SeaSkyWar"
        # 实例化handler
        self.handler = logging.handlers.RotatingFileHandler('../log/'+self.logfile + '.log', maxBytes = 1024 * 1024, backupCount = 5)
        self.fmt = "%(asctime)s - %(levelname)s - %(message)s"
        
        # 实例化formatter
        self.formatter = logging.Formatter(self.fmt) 
        # 为logger添加formatter
        self.handler.setFormatter(self.formatter)

        # 获取名字为为filename的logger
        self.logger = logging.getLogger(self.logfile)
        
        # 为logger 添加handler  
        self.logger.addHandler(self.handler)

        #设置日志等级为DEBUG
        self.logger.setLevel(logging.DEBUG)

    def setLevel(level):
        self.logger.setLevel(level)

    def c(self, msg):
        self.logger.critical(msg)

    def e(self, msg):
        self.logger.error(msg)

    def w(self, msg):
        self.logger.warning(msg)

    def i(self, msg):
        self.logger.info(msg)

    def d(self, msg):
        self.logger.debug(msg)


