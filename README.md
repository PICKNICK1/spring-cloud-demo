### 安装顺序

* 初始化nacos.sql到数据库
* docker启动nacos

  `docker run --name spring_cloud_standalone_nacos -p 8848:8848 -e MODE=standalone -e SPRING_DATASOURCE_PLATFORM=mysql -e MYSQL_SERVICE_HOST=172.17.0.3 -e MYSQL_SERVICE_PORT=3306 -e MYSQL_SERVICE_DB_NAME=nacos_config -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_PASSWORD=xA123456 -e MYSQL_SERVICE_DB_PARAM=characterEncoding=utf8"&"zeroDateTimeBehavior=convertToNull"&"useSSL=false"&"allowMultiQueries=true"&"useJDBCCompliantTimezoneShift=true"&"useLegacyDatetimeCode=false"&"serverTimezone=Asia/Shanghai -d nacos/nacos-server:v2.1.0`

  修改MYSQL_SERVICE_HOST、MYSQL_SERVICE_PORT、MYSQL_SERVICE_DB_NAME、MYSQL_SERVICE_USER、MYSQL_SERVICE_PASSWORD
* 进入nacos，默认端口8848，修改配置文件中如redis，datasource等配置
* 初始化数据库upms.sql
* 启动服务
