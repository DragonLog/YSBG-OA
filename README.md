
> **写在前面的话：两个前端服务在老师资料上稍微做了下改动，主要改动了请求的域名和端口部分（乘老师的收费隧道还没过期赶紧把域名和端口改的和他一样拿来用，直接省了20RMB）。后端涉及微信和数据库的一些个人公共信息我都置空挖出来了，如果要部署的话需要你根据自己的实际情况填一下。**
## 云尚办公OA（主攻后端）
***
> **小贴士**
1. **项目主要使用JWT生成token充当session记录登录状态，因此直接在前端切换token字符串就可以切换用户身份进行测试。**  
2. **最后几集微信验证并绑定手机进入后如果清空token，下次进入会在手机绑定和微信自动登录之间来回跳转，（解决办法：在后端生成任意一个user的token，在App.vue的wechatLogin方法里直接给token赋值即可）此时若不小心删除了数据库里插入的openId，可在来回跳转时，在后端的/userInfo接口的方法中打断点获取。**
3. **在SpringSecurity过滤器中抛出的异常一般无法被全局异常捕获，可考虑原地catch异常并用response返回或继承SpringSecurity的异常类处理，具体请百度。**
4. **使用@MapperScan注解扫描Mapper接口时请注意包的范围（精确到mapper所在包），范围过大会导致扫描异常（仅使用@Mapper注解无需理会）。**
5. **配置文件里的mapper-locations可以加逗号寻找多个路径下的mapper-xml文件。**
6. **后期涉及微信公众号访问的后端接口建议都加@CrossOrigin注解解决跨域。**
7. **springboot的启动类会自动扫描同包及其子包下打上spring相关注解的类，同步和子包外的类想要是话需要手动添加，或者基于“自动装配”实现。**
8. **强烈建议，如果不是对前端特别有兴趣，请直接使用老师提供的全部前端资料（我中间被前端报错折磨的实在受不了了），node版本请与老师保持绝对一致！（我不明白为什么这玩意向下兼容可以做的这么差...）**
9. **由于个人信息安全原因清空了maven工程，顺带把processes这个文件夹也给清了，所以那个流程定义的zip也没了，但是它已经被加载到数据库变成实例了，这块如果有问题的话建议再上传一个流程zip并发布。**
***
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show1.png)
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show2.png)
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show3.png)
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show4.png)
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show5.png)
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show6.png)
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show7.png)
![示例图片](https://github.com/DragonLog/YSBG-OA/blob/main/pictureForExample/show8.png)
