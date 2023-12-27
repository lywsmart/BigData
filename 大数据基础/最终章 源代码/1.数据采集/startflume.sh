#!/bin/bash
# sh startflume.sh stop	关闭模拟程序
# sh startflume.sh start 启动模拟程序

# 设置Flume的根目录
export FLUME_HOME=/usr/local/src/flume

# Flume任务命令
FLUME_COMMAND="flume-ng agent \
  --name a1 \
  --conf $FLUME_HOME/job/ \
  --conf-file $FLUME_HOME/job/WebLog.conf \
  -Dflume.root.logger=INFO,console"

# 设置日志文件路径
LOG_FILE="/opt/software/bin/flume.log"

# 设置保存进程ID的文件路径
PID_FILE="/opt/software/bin/flume.pid"

start() {
  if [ -f "$PID_FILE" ]; then
    echo "Flume任务已经在运行，进程ID：$(cat $PID_FILE)"
  else
    nohup $FLUME_COMMAND >> "$LOG_FILE" 2>&1 &
    echo $! > "$PID_FILE"
    echo "Flume任务正在后台运行，进程ID保存在 $PID_FILE 中。"
  fi
}

stop() {
  if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    kill $PID
    rm "$PID_FILE"
    echo "Flume任务已经停止，进程ID：$PID"
  else
    echo "Flume任务没有在运行。"
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

