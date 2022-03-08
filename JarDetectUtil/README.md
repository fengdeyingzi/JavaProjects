#JarDetectUtil

这个工具用来检测项目中的jar包冲突、jar包缺失、jar包循环依赖等问题

dist目录下的exe文件或jar文件可以直接运行!

软件界面如下

![输入图片说明](http://git.oschina.net/uploads/images/2015/1224/171241_a4b136d2_387956.jpeg "在这里输入图片标题")




#简要说明
**CheckConfig**类用来设置检测参数

1)设置哪些package不需要检测，默认已经将jdk下的Jar包目录设置为忽略

2)设置待检测目录下的哪些jar包可以忽略
```
setCheckConfig(String propertyFilePath)//通过指定properties文件来设置
setCheckConfig(String ignorePackagesStr, String ignoreJarsStr, String conflictFileTypes)//直接设置
```

**JarChecker**类为主要的检测方法(超过10个jar包时默认会开启多线程处理)
```
check(String dirPath)//核心检测方法,dirPath为目录名称,多目录时可以以;隔开
getCompleteCount()//获得当前已经完成的检测数量
getJarFileCount(String dirPath)//获得目录下的jar包数量
```

**JarConflictDetectUtil**类用来检测jar包间是否有冲突
```
getConflict(pathname)//检测jar包是否有冲突
```

**JarDetectAPP**类为用swing编写的简单软件界面。


#example
参考example.Test类

示例代码：
```
CheckConfig checkConfig = new CheckConfig();
List<JarInfo> lst = new JarChecker(checkConfig).check("D:/temp/lib1;D:/temp/lib2");
for(JarInfo jar:lst){
    System.out.println(jar);
    System.out.println(jar.getBuildVersion());//编译时版本
    System.out.println(jar.getDependentClasses());
    System.out.println(jar.getDependentJars());
    System.out.println(jar.getCycleJars());//循环依赖
    System.out.println(jar.getTroubleClasses());//缺失的类
}
//冲突问题,多目录以;隔开
HashMap<String, HashSet<String>> conflictMap=new JarConflictDetectUtil(checkConfig).getConflict(pathname);
```