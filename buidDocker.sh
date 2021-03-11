#!/bin/bash

# 需要使用maven，请自行配置环境变量
cd ./linkwe-admin || exit
mvn docker:build
