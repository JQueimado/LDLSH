#! /bin/bash
java -server -Xmx100g -XX:+UseG1GC -Dio.netty.leakDetection.level=disabled -jar LDLSH-3.2.jar LDLSH/MemoryTests/memoryTest1.properties $1