import '../css/base.css'
import '../css/index.css'
import {getPoster,getUserInfo} from './api'
import {getUrlParam,getWxCode} from './utils'
import config from './contant'

$(function(){
    getWxCode()
    //code, agentId
    const fissionTargetId = getUrlParam('fissionTargetId');
    const posterId = getUrlParam('posterId');
    const taskFissionId = getUrlParam('taskFissionId');
    const agentId = getUrlParam('agentId');
    const code = config.code
    let eid = ''
    try {
        getUserInfo({code,agentId})
        .then(res=>{
            let data = res.data;
            eid = data.external_userid
            getPoster({fissionTargetId,posterId,taskFissionId,eid})
            .then(res=>{
                $('.posterImg').attr('src',res.data.postersUrl)
                localStorage.setItem('postersUrl',res.data.postersUrl)
            })
        })
    } catch (error) {
        console.log(error)
    }
    $('.sharePic').click(function(){
        alert('长按图片在弹出菜单中发送给朋友或者可保存图片分享至朋友圈')
    });
    $('.myTaskDetail').click(function(){
        window.location.href = `/taskProcess.html?eid=${eid}&taskFissionId=${taskFissionId}`
    });
    
})

