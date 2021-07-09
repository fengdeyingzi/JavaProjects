工程模板创建

//将模板zip解压到指定路径
unzip("template.zip","myapp")

//替换文件夹
replaceDir("com/template/","net/yzjlb")

//替换指定文件中指定字符串
relaceFile("build.gradle","${version}","3.5.1")

