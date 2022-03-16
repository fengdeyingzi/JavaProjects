这个mrpbuilder并非是斯凯的mrpbuilder工具，而是自己实现的一个mrp编译器，除了插件功能以外，它已经具备斯凯编译器的所有功能，而且不需要配置mpr文件。除了编译mrp以外，还支持打包/解包mrp，以及查看mrp信息


## mrpbuilder使用方法
### 控制台参数说明
```
    -h 帮助
    -filename 文件名
    -displayname 显示名
    -vendor 作者
    -desc 详情
    -t 类型 unpack 解包;pack 打包;info 获取mrp信息;setinfo 设置mrp信息;build mrp编译;gcc 使用gcc编译本机程序
    -i 输入文件（可指定文件夹）
    -o 输出文件
    -auth 编译器授权字符串 2f7cc7cde
    -appid 应用id
    -version 版本号
    -scrw 屏幕宽度
    -scrh 屏幕高度
```


### 编译mrp

- 简单的编译方法
```
mrpbuilder -t build -i hello.c -o hello.mrp
```

- 配置详细信息的编译方法
```
mrpbuilder -t build -i hello.c -o hello.mrp -filename picoc.mrp -displayname 手机CAPP运行器v1.8 -vendor "风的影子" -desc "手机CAPP运行器，读取mpc.c文件进行运行" -appid 202003 -version 3008
```

### 其它编译参数说明
```
-gzip           使用gzip压缩
-fast           使用即时编译，使用编译缓存加快编译速度，如果需要清除缓存，使用命令mrpbuilder -t clear
-bmp565         压缩bmp图片为bmp565（为了兼容斯凯的编译器）
-Isfw\adv\      添加include路径“sfw\adv\”（类似gcc编译器的命令）
-DSCREEN        添加宏定义SCREEN
-i  help\demo\贪吃蛇.txt(name=24)   为添加的文件设置别名（打包后文件将使用别名放入mrp）
```

### 查看mrp文件信息
```
mrpbuilder -t info -i test.mrp
```

### 解包mrp文件
```
mrpbuilder -t unpack -i test.mrp -o D:\mrp\
```
### 打包mrp文件
```
mrpbuilder -t pack -i 1.txt -o test.mrp
```

### 使用gcc编译C文件为exe
```
# 用法和gcc类似，可使用-fast命令加快编译
mrpbuilder -t gcc -i main.c -o main.exe
```

### 使用javac编译java
```
mrpbuilder -t runjava -mainclass MrpBuilderMain -i src\*.java src\com\xl\util\*.java src\com\xl\window\*.java src\mrpbuilder_java\*.java src\org\json\*.java
```

### 使用java打包jar
```
mrpbuilder -t packjar -mainclass MrpBuilderMain -o mrpbuilder.jar
```

### 其它相关资料
电脑调用mrpoid运行mrp： http://i.yzjlb.net/?p=789
