# **yml配置文件更新记录**
#### 注：每次升级涉及到的yml文件更新
---
### ● 日期：2023.07.09
    
  ```
  ● linkwe-common.yml
      linkwechat:
          materialDetailUrl: ${linkwechat.h5Domain}/#/metrialDetail?materiaId={}&otherModle=true #素材详情
          momentsUrl: ${linkwechat.h5Domain}/#/friendsDetail?id={} #朋友圈移动端详情页
  ● linkwe-auth.yml
      rsa:
        privateKey: MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAqhHyZfSsYourNxaY7Nt+PrgrxkiA50efORdI5U5lsW79MmFnusUA355oaSXcLhu5xxB38SMSyP2KvuKNPuH3owIDAQABAkAfoiLyL+Z4lf4Myxk6xUDgLaWGximj20CUf+5BKKnlrK+Ed8gAkM0HqoTt2UZwA5E2MzS4EI2gjfQhz5X28uqxAiEA3wNFxfrCZlSZHb0gn2zDpWowcSxQAgiCstxGUoOqlW8CIQDDOerGKH5OmCJ4Z21v+F25WaHYPxCFMvwxpcw99EcvDQIgIdhDTIqD2jfYjPTY8Jj3EDGPbH2HHuffvflECt3Ek60CIQCFRlCkHpi7hthhYhovyloRYsM+IS9h/0BzlEAuO0ktMQIgSPT3aFAgJYwKpqRYKlLDVcflZFCKY7u3UP8iWi1Qw0Y=
        publicKey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdHnzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ==
```
---
### ● 日期：2023.08.22
  ```
   ● linkwe-common.yml
      linkwechat:
          customerShortLinkDomainName: sl.linkwechat.net/l/
          leadsDetailUrl: /clueHighseas/detail?id={} #线索中心移动端-待办任务-详情页
  
  ```
### ● 日期：2023.09.03
  ```
   ● linkwe-common.yml
      linkwechat:
          customerShortLinkDomainName: sl.linkwechat.net/l/
          leadsDetailUrl: /clueHighseas/detail?id={} #线索中心移动端-待办任务-详情页
          leadsCovenantWaitFollowUpUrl: /clueHighseas/followDetail?recordId={} #线索中心移动端-待办任务-线索约定事项待跟进-详情页
          
          移除
          yshop-mall: yshop-mall
  
  ```
### ● 日期：2023.10.09
  ```
   ● linkwe-common.yml
      linkwechat:
         fincaceProxyConfig:
            proxy: ""  #使用代理的请求，需要传入代理的链接。如：socks5://10.0.0.1:8081 或者 http://10.0.0.1:8081
            paswd: ""  #代理账号密码，需要传入代理的账号密码。如 user_name:passwd_123
   ● linkwe-common.yml
      linkwechat:
         weComeProxyConfig:
            startProxy: false #是否开启代理 true开启 false不开启,如果为false,则proxyIp,proxyPort,proxyUserName,proxyPassword需要配置。
            proxyIp: "" #代理服务器的ip
            #代理服务器端口
            proxyPort: 0  
            proxyUserName: "" #代理服务器账号
            proxyPassword: "" #代理服务器密码
  ```
### ● 日期：2023.10.26
  ```
    ● linkwe-common.yml
        wecom:
          移除
          welcome-msg-default: 您好，欢迎关注LinkWechat,如果对您有帮助，麻烦在码云上帮我们点个star，谢谢！
  ```
### ● 日期：2023.11.03
  ```
    ● linkwe-common.yml
        linkwechat:
          #新增
          communityNewGroupUrl: ${linkwechat.h5Domain}/#/groupCode?type=newCustomerGroup&id={0} #新客拉群H5链接
  ```
### ● 日期：2023.11.13
  ```
   ● linkwe-common.yml
        linkwechat:
         #修改
         tagRedirectUrl: ${linkwechat.h5Domain}/#/groupCode?type=oldCustomerGroup&id={0} #老客标签建群H5链接 
 ```