## kafka使用

1. 使用homebrew安装kafka：`brew install kafka`

2. 启动zookeeper -> 启动kafka

    ```
    $ zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties &
    $ kafka-server-start /usr/local/etc/kafka/server.properties &
    ```

3. springboot集成kafka项目 -> 运行