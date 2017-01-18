#coding: utf-8


import time

from Singleton import *
from Log import *
from abc import *

@singleton
class TM:
    def __init__(self):
        self._runningTimers_ms = []
        self._runningTimers_s = []
        self._runningTimers_10s = []
        self._runningTimers_m = []
        self._runningTimers_h = []
        self._runningTimers_d = []
        self._unusedTimers = []
        self._cancelTimers = []
        self._s_time = 0
        self._10s_time = 0
        self._m_time = 0
        self._h_time = 0
        self._d_time = 0
        self._reAddTimerList = []


    def step(self, delta):
        # 移除代取消的定时器
        for timer in self._cancelTimers:
            if timer in self._runningTimers_ms:
                self._runningTimers_ms.remove(timer)
            elif timer in self._runningTimers_s:
                self._runningTimers_s.remove(timer)
            elif timer in self._runningTimers_10s:
                self._runningTimers_10s.remove(timer)
            elif timer in self._runningTimers_m:
                self._runningTimers_m.remove(timer)
            elif timer in self._runningTimers_h:
                self._runningTimers_h.remove(timer)
            elif timer in self._runningTimers_d:
                self._runningTimers_d.remove(timer)
        self._cancelTimers = []

        self._s_time += delta
        self._10s_time += delta
        self._m_time += delta
        self._h_time += delta
        self._d_time += delta

        tmpRemoveList = []

        # 处理粒度为Day的Timer
        if self._d_time >= 60 * 60 * 24:
            for timer in self._runningTimers_d:
                timer.step(60 * 60 * 24)
                if timer.isComplete():
                    self.delTimer(timer)
                else:
                    if timer.getLeftTime() < 60 * 60 * 24:
                        tmpRemoveList.append(timer)
                        self._reAddTimerList.append(timer)
            for timer in tmpRemoveList:
                self._runningTimers_d.remove(timer)
            tmpRemoveList = []
            self._d_time -= 60 * 60 * 24
        
        # 处理粒度为Hour的Timer
        if self._h_time >= 60 * 60:
            for timer in self._runningTimers_h:
                timer.step(60 * 60)
                if timer.isComplete(): 
                    self.delTimer(timer)
                else:
                    if timer.getLeftTime() < 60 * 60:
                        tmpRemoveList.append(timer)
                        self._reAddTimerList.append(timer)
            for timer in tmpRemoveList:
                self._runningTimers_h.remove(timer)        
            tmpRemoveList = []
            self._h_time -= 60 * 60

        # 处理粒度为Minute的Timer
        if self._m_time >= 60:
            for timer in self._runningTimers_m:
                timer.step(60)
                if timer.isComplete():
                    self.delTimer(timer)
                else:
                    if timer.getLeftTime() < 60:
                        tmpRemoveList.append(timer)
                        self._reAddTimerList.append(timer)
            for timer in tmpRemoveList:
                self._runningTimers_m.remove(timer)
            tmpRemoveList = []
            self._m_time -= 60

        # 处理粒度为10 S的Timer
        if self._10s_time >= 10:
            for timer in self._runningTimers_10s:
                timer.step(10)
                if timer.isComplete():
                    self.delTimer(timer)
                else:
                    if timer.getLeftTime() < 10:
                        tmpRemoveList.append(timer)
                        self._reAddTimerList.append(timer)
            for timer in tmpRemoveList:
                self._runningTimers_10s.remove(timer)
            tmpRemoveList = []
            self._10s_time -= 10

        # 处理粒度为Second的Timer
        if self._s_time >= 1:
            for timer in self._runningTimers_s:
                timer.step(self._s_time)
                if timer.isComplete():
                    self.delTimer(timer)
                else:
                    if timer.getLeftTime() < 1:
                        tmpRemoveList.append(timer)
                        self._reAddTimerList.append(timer)
            for timer in tmpRemoveList:
                self._runningTimers_s.remove(timer)
            tmpRemoveList = []
            self._s_time -= 1

        # 处理毫秒级的Timer
        for timer in self._runningTimers_ms:
            timer.step(delta)
            if timer.isComplete():
                self.delTimer(timer)

        # 随着Timer减少，重新调整粒度
        self.reAddTimer(self._reAddTimerList)
        self._reAddTimerList = []

        # 移除完成不再使用的定时器
        for timer in self._unusedTimers:
            if timer in self._runningTimers_ms:
                self._runningTimers_ms.remove(timer)
            elif timer in self._runningTimers_s:
                self._runningTimers_s.remove(timer)
            elif timer in self._runningTimers_10s:
                self._runningTimers_10s.remove(timer)
            elif timer in self._runningTimers_m:
                self._runningTimers_m.remove(timer)
            elif timer in self._runningTimers_h:
                self._runningTimers_h.remove(timer)
            elif timer in self._runningTimers_d:
                self._runningTimers_d.remove(timer)
        self._unusedTimers = []
    def reAddTimer(self, timers):
        for timer in timers:
            self.addTimer(timer)

    def addTimer(self, timer):
        if timer is None:
            Log().e('Timer is None.')
            return
        if timer.getLeftTime() < 0:
            Log().e('Timer left time < 0.')
            return
        if timer._timerTask is None:
            Log().e('Timer task is None.')
            return
        if timer._isComplete is True:
            Log().e('Timer is completed, can`t add.')
            return
        t = timer.getLeftTime() 
        if t >= 60 * 60 * 24:
            self._runningTimers_d.append(timer)
        elif t >= 60 * 60:
            self._runningTimers_h.append(timer)
        elif t >= 60:
            self._runningTimers_m.append(timer)
        elif t >= 10:
            self._runningTimers_10s.append(timer)
        elif t >= 1:
            self._runningTimers_s.append(timer)
        else:
            self._runningTimers_ms.append(timer)


    def delTimer(self, timer):
        self._unusedTimers.append(timer)

    def cancelTimer(self, timer):
        self._cancelTimers.append(timer)

    def clearTimer(self):
        self._runningTimers_d = []
        self._runningTimers_h = []
        self._runningTimers_m = []
        self._runningTimers_10s = []
        self._runningTimers_s = []
        self._runningTimers_ms = []


# 定时器任务抽象类
class TimerTask(object):
    __metaclass__ = ABCMeta

    # 定时器回调函数
    @abstractmethod
    def onTimer(self):
        pass


# 定时器类
class Timer:
    # 构造时传入时间和回调类
    def __init__(self, leftTime = 0, timerTask = None):
        self._leftTime = leftTime
        self._timerTask = timerTask
        self._isPause = False
        self._isComplete = False


    def start(self):
        TM().addTimer(self)

    def step(self, delta):
        if self._isPause == False:
            self._leftTime -= delta
            if self._leftTime <= 0:
                if self._timerTask is not None and self._isComplete is False:
                    self._timerTask.onTimer()
                self._isComplete = True
     
    def pause(self):
        if self._isPause == False:
            self._isPause = True

    def resume(self):
        if self._isPause == True:
            self._isPause = False

    def cancel(self):
        if self._isComplete is False:
            TM().cancelTimer(self)

    def isComplete(self):
        return self._isComplete

    def getLeftTime(self):
        return self._leftTime
