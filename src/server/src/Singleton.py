# coding: utf-8

# 使用装饰器(decorator)
# 单例类本身不知道自己是单例的,因为他本身并不是单例的

def singleton(cls, *args, **kw):
    instances = {}
    def _singleton():
        if cls not in instances:
            instances[cls] = cls(*args, **kw)
        return instances[cls]
    return _singleton

# 用法
# @singleton
# class Example:
#     ...
#     ...
