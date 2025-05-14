# Project Description

This repository is about a Java code generator of my own. 

# Enviroment Details

Here's some information about my development environment:

    LANGUAGE : Java 8.
    MAVEN : Apache Maven 3.8.5
    IDE : Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components) Version: 2023-12 (4.30.0) Build id: 20231201-2043
    OS : Windows 11 x64.

# Packaging

In terms of packaging, you just need to install the tool 'Apache Maven'. In the root folder of the repository, run the following command to pack: Please pay attention to the output information.

```Bash
mvn clean
mvn install
```

# How to

Currently, the generator can connect to 3 types of databases: mysql, oracle, sql server. Here's an example of how I use it.

## 1. get help

If you want to get parameter information, you can do the following:

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar -h
```

The output of this command might be as follows:

```Bash
Usage: <main class> [options]
  Options:
    -acn, --action_class_name
      The name of action Class which is being generated.
    -at, --action_type
      The action type of generator.
      Possible Values: [OBJECT, SQL]
    -ac, --auto_commit
      Whether or not to commit automatically.
      Default: false
    -ct, --connection_timeout
      The threshold of database connection. In milliseconds.
      Default: 5000
    --console
      If you are running on console by yourself, please add this parameter. 
      The output informations will be shorter.
      Default: false
    -dun, --db_user_name
      The name of user who connecting database.
    -dup, --db_user_passwd
      The password of user who connecting to database.
    -db, --dbtype
      Your database type. for example: mysql, oracle, sqlserver.
      Possible Values: [MYSQL, ORACLE, SQLSERVER]
    -d, --dir
      This is the directory where the entity is stored after it is generated. 
      This parameter can also be used to specify the output folder for scaled 
      images. Please ensure that the folder has write permissions.
    -fc, --file_charset
      The character set used when the file is saved.
    -haz, --height_after_zooming
      The max height after image scaling, unit (pixels). The image is scaled 
      proportionally. 
      Default: -1
    -h, --help
      Show help informations.
    -ifs, --image_files
      This is the full path of your image files. If there are multiple files, 
      separate them with commas.
    -ifd, --image_files_dir
      This is the original folder path where the images are stored. This 
      parameter is mutually exclusive with '-ifs'. When both appear, only the 
      '-ifs' parameter is processed.
    -jdn, --jdbc_driver_name
      The JDBC driver class name. For example: 'com.mysql.cj.jdbc.Driver'.
    -ju, --jdbc_url
      The JDBC connection URL. For example: 
      'jdbc:mysql://127.0.0.1:3306/myspace_db' 
    -max, --max_thread
      The maximum number of connections in the connection pool.
      Default: 40
    -min, --min_thread
      The minimum number of connections in a connection pool.
      Default: 10
  * -m, --mode
      Running mode of the generator.
      Possible Values: [JAR_TEST, DB_CONN_TEST, DB_OBJ_SELECT, JAVA_BEAN_GEN, JAVA_FEATURE_GEN, IMAGE_SCALING]
    -n, --name
      The name of configuration.
      Default: defaultName
    -sc, --schema
      The name of the database schema.
    -scu, --schema_user
      The username of the database schema.
    -so, --sql_objects
      The name of the database object to be generated.
    -st, --sql_string
      The database SQL statement to be generated.
    -waz, --width_after_zooming
      The max width after image scaling, unit (pixels). The image is scaled 
      proportionally. 
      Default: -1
```

## 2. test jar

If you want to test if this jar will work, you can enter the following command:

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m jar_test
```

If there is no parameter `--console` , it will be output in json format.

## 3. connect to database

### 3.1 MySql 8+

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m db_conn_test -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://192.168.220.129:3306/myspace_db" -dun spaceadmin -dup "1234567890-=AA" -ct 5000 -min 10 -max 20 
```

### 3.2 Oracle 11g+

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m db_conn_test -db oracle -jdn oracle.jdbc.driver.OracleDriver -ju "jdbc:oracle:thin:@192.168.220.129:1521:ORCL" -dun scott -dup tiger -ct 5000 -min 10 -max 20
```

### 3.3 SQL Server 2016+

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m db_conn_test -db sqlserver -jdn com.microsoft.sqlserver.jdbc.SQLServerDriver -ju "jdbc:sqlserver://192.168.220.129:1433;DatabaseName=pdc_test;encrypt=true;trustServerCertificate=true" -dun pdc -dup "1234567890-=AA" -ct 5000 -min 10 -max 20
```

## 4. select database objects

### 4.1 MySql 8+

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m db_obj_select -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://192.168.220.129:3306/myspace_db" -dun spaceadmin -dup "1234567890-=AA" -ct 5000 -min 10 -max 20 -sc myspace_db -scu spaceadmin
```

### 4.2 Oracle 11g+

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m db_obj_select -db oracle -jdn oracle.jdbc.driver.OracleDriver -ju "jdbc:oracle:thin:@192.168.220.129:1521:ORCL" -dun scott -dup tiger -ct 5000 -min 10 -max 20 -sc scott -scu scott
```

### 4.3 SQL Server 2016+

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m db_obj_select -db sqlserver -jdn com.microsoft.sqlserver.jdbc.SQLServerDriver -ju "jdbc:sqlserver://192.168.220.129:1433;DatabaseName=pdc_test;encrypt=true;trustServerCertificate=true" -dun pdc -dup "1234567890-=AA" -ct 5000 -min 10 -max 20 -sc dbo -scu dbo
```

## 5. generate java beans

Before processing, please ensure that your output folder has write permissions. 

Do not set the output folder to a non-writable path, such as the root directory of the C drive on a Windows system (C:\\\\).

### 5.1 MySql 8+

#### 5.1.1 objects

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m java_bean_gen -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://192.168.220.129:3306/myspace_db" -dun spaceadmin -dup "1234567890-=AA" -ct 5000 -min 10 -max 20 -sc myspace_db -scu spaceadmin -fc utf-8 -at object -so "TEST_A,TEST_B" -d "C:\\Users\\Michael\\Desktop"
```

#### 5.1.2 sql

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m java_bean_gen -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://192.168.220.129:3306/myspace_db" -dun spaceadmin -dup "1234567890-=AA" -ct 5000 -min 10 -max 20 -sc myspace_db -scu spaceadmin -fc utf-8 -at sql -st "select 1 as col_1" -d "C:\\Users\\Michael\\Desktop"
```

### 5.2 Oracle 11g+

#### 5.2.1 objects

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m java_bean_gen -db oracle -jdn oracle.jdbc.driver.OracleDriver -ju "jdbc:oracle:thin:@192.168.220.129:1521:ORCL" -dun scott -dup tiger -ct 5000 -min 10 -max 20 -sc scott -scu scott -fc utf-8 -at object -so "DEPT,EMP" -d "C:\\Users\\Michael\\Desktop"
```

#### 5.2.2 sql

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m java_bean_gen -db oracle -jdn oracle.jdbc.driver.OracleDriver -ju "jdbc:oracle:thin:@192.168.220.129:1521:ORCL" -dun scott -dup tiger -ct 5000 -min 10 -max 20 -sc scott -scu scott -fc utf-8 -at sql -st "select 1 as col_222 from dual" -d "C:\\Users\\Michael\\Desktop"
```

### 5.3 SQL Server 2016+

#### 5.3.1 objects

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m java_bean_gen -db sqlserver -jdn com.microsoft.sqlserver.jdbc.SQLServerDriver -ju "jdbc:sqlserver://192.168.220.129:1433;DatabaseName=pdc_test;encrypt=true;trustServerCertificate=true" -dun pdc -dup "1234567890-=AA" -ct 5000 -min 10 -max 20 -sc dbo -scu dbo -fc utf-8 -at object -so "SCORES,STUDENT" -d "C:\\Users\\Michael\\Desktop"
```

#### 5.3.2 sql

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m java_bean_gen -db sqlserver -jdn com.microsoft.sqlserver.jdbc.SQLServerDriver -ju "jdbc:sqlserver://192.168.220.129:1433;DatabaseName=pdc_test;encrypt=true;trustServerCertificate=true" -dun pdc -dup "1234567890-=AA" -ct 5000 -min 10 -max 20 -sc dbo -scu dbo -fc utf-8 -at sql -st "select 1 as col_444" -d "C:\\Users\\Michael\\Desktop"
```

## 6. generate java action classes

This feature is ok now. But it is only for my own use. (from Fri May 9 15:18:53 2025 UTC+8)

Before processing, please ensure that your output folder has write permissions. 

Do not set the output folder to a non-writable path, such as the root directory of the C drive on a Windows system (C:\\\\).

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m java_feature_gen -acn MyTester01 -fc utf-8 -d "C:\\Users\\Michael\\Desktop"
```

## 7. images scaling

This feature allows you to scale the image according to your needs. Currently supports 3 types of image formats: png, jpg, jpeg.

Before processing, please ensure that your output folder has write permissions. 

Do not set the output folder to a non-writable path, such as the root directory of the C drive on a Windows system (C:\\\\).

### 7.1 scale the specified file

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m image_scaling -ifs "C:\\Users\\Michael\\Pictures\\testimgs\\DSC00016.JPG, C:\\Users\\Michael\\Pictures\\testimgs\\DSC00019.JPG " -d "C:\\Users\\Michael\\Pictures\\testimgs\\output"  -waz 1600 -haz 901
```

### 7.2 scale all images in the specified folder (non-recursive search)

```Bash
java -jar .\generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar --console -m image_scaling -ifd "C:\\Users\\Michael\\Pictures\\testimgs" -d "C:\\Users\\Michael\\Pictures\\testimgs\\output"  -waz 1600 -haz 901
```
