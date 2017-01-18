
all:
	protoc -I=./ --python_out=../server/src/ message.proto
	protoc -I=./ --python_out=./python/ message.proto
	protoc -I=./ --java_out=./java/ message.proto
	protoc -I=./ --cpp_out=./cpp/ message.proto

clean:
	rm -rf ./java/com/
	rm ../server/src/message_pb2.py
	rm ./cpp/*
	rm ./python/*
