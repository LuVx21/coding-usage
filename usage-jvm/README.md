$ javap --help
用法: javap <options> <classes>
其中, 可能的选项包括:
  -help  --help  -?        输出此用法消息
  -version                 版本信息
  -v  -verbose             输出附加信息
  -l                       输出行号和本地变量表
  -public                  仅显示公共类和成员
  -protected               显示受保护的/公共类和成员
  -package                 显示程序包/受保护的/公共类
                           和成员 (默认)
  -p  -private             显示所有类和成员
  -c                       对代码进行反汇编
  -s                       输出内部类型签名
  -sysinfo                 显示正在处理的类的
                           系统信息 (路径, 大小, 日期, MD5 散列)
  -constants               显示最终常量
  -classpath <path>        指定查找用户类文件的位置
  -cp <path>               指定查找用户类文件的位置
  -bootclasspath <path>    覆盖引导类文件的位置

javap -v -c -s -l 


javap -v -c -s -l ./demo/BedMerchant.class > ./demo/BedMerchant.txt
javap -v -c -s -l ./demo/Customer.class > ./demo/Customer.txt
javap -v -c -s -l ./demo/Merchant.class > ./demo/Merchant.txt
javap -v -c -s -l ./demo/MethodTest.class > ./demo/MethodTest.txt
javap -v -c -s -l ./demo/Variable.class > ./demo/Variable.txt

javap -v -c -s -l ./demo1/Customer.class > ./demo1/Customer.txt
javap -v -c -s -l ./demo1/Merchant.class > ./demo1/Merchant.txt
javap -v -c -s -l ./demo1/NaiveMerchant.class > ./demo1/NaiveMerchant.txt

javap -v -c -s -l ./demo2/Merchant.class > ./demo2/Merchant.txt
javap -v -c -s -l ./demo2/VIP.class > ./demo2/VIP.txt
javap -v -c -s -l ./demo2/VIPOnlyMerchant.class > ./demo2/VIPOnlyMerchant.txt

javap -v -c -s -l ./demo3/Merchant.class > ./demo3/Merchant.txt
javap -v -c -s -l ./demo3/VIPOnlyMerchant.class > ./demo3/VIPOnlyMerchant.txt


javap -v -c -s -l ./demo4/Node.class > ./demo4/Node.txt
javap -v -c -s -l ./demo4/MyNode.class > ./demo4/MyNode.txt


javap -v -c -s -l ./demo5/Node.class > ./demo5/Node.txt
javap -v -c -s -l ./demo5/MyNode.class > ./demo5/MyNode.txt
