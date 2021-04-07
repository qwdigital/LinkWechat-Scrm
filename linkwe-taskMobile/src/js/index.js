import '../css/base.css'
import '../css/index.css'
import {getPoster,getUserInfo,getToken,getWXUserInfo} from './api'
import {getUrlParam,getWxCode} from './utils'
import config from './contant'

$(function(){
    getWxCode()
    //code, agentId
    const fissionTargetId = getUrlParam('fissionTargetId');
    const posterId = getUrlParam('posterId');
    const taskFissionId = getUrlParam('fissionId');
    const code = config.code
    try {
        getToken(code)
            .then(res=>{
                let data =res.data
                localStorage.setItem('userinfo',JSON.stringify(data))
                getWXUserInfo({openId:data.openId,lang:"zh_CN"})
                .then(resp=>{
                    let userData = resp.data;
                    let unionId = userData.unionId
                    getPoster({fissionTargetId,posterId,taskFissionId,unionId})
                    .then(res=>{
                        $('.posterImg').attr('src',res.data.postersUrl)
                        localStorage.setItem('postersUrl',res.data.postersUrl)
                    })
                })
            })
       
    } catch (error) {
        console.log(error)
    }
    $('.sharePic').click(function(){
        alert('长按图片在弹出菜单中发送给朋友或者可保存图片分享至朋友圈')
    });
    $('.myTaskDetail').click(function(){
        window.location.href = `./taskProcess.html?eid=${eid}&taskFissionId=${taskFissionId}`
    });
    
})

