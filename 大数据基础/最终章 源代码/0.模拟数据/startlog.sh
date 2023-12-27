#!/bin/bash
# sh startlog.sh stop	关闭正在运行
# sh startlog.sh start	直接启动

# 设置LogDataSource.jar文件的路径
JAR_FILE="/opt/software/LogDataSource.jar"

# 设置日志文件路径
LOG_FILE="/opt/software/bin/startlog.txt"

# 设置保存进程ID的文件路径
PID_FILE="/opt/software/bin/pid.txt"

start() {
  if [ -f "$PID_FILE" ]; then
    echo "LogDataSource.jar 已经在运行，进程ID：$(cat $PID_FILE)"
  else
    nohup java -cp "$JAR_FILE" com.lcvc.generate.GenerateLog /opt/software/data/weblog/nginx.log >> "$LOG_FILE" 2>&1 &
    echo $! > "$PID_FILE"
    echo "LogDataSource.jar 正在后台运行，进程ID保存在 $PID_FILE 中。"
  fi
}

stop() {
  if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    kill $PID
    rm "$PID_FILE"
    echo "LogDataSource.jar 已经停止，进程ID：$PID"
  else
    echo "LogDataSource.jar 没有在运行。"
  fi
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
    start
    ;;
  *)
    echo "用法: $0 {start|stop|restart}"
    exit 1
    ;;
esac

exit 0

