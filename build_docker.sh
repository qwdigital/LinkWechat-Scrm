#!/bin/bash
########################################################
############### LinkWeChat Docker 部署脚本 ##############
########################################################
LOG=$1
PROJ_HOME=$PWD
echo "$PROJ_HOME"
CLEAN_BUILD="clean"
BUILD_CODE="install"
BUILD_DOCKER="docker:build"

showLog(){
  if [ "$1" == '--log' -o "$1" == '-l' ]; then
    $PROJ_HOME/mvnw "$2"
  else
    $PROJ_HOME/mvnw "$2" > /dev/null
  fi
}

cd "$PROJ_HOME"
echo '开始从代码构建LinkWeChat'
showLog "$LOG" "$CLEAN_BUILD"
showLog "$LOG" "$BUILD_CODE"
echo '构建完成'
cd "$PROJ_HOME"/linkwe-admin
echo '开始构建Docker镜像'
showLog "$LOG" "$BUILD_DOCKER"
echo 'Docker镜像构建完成'