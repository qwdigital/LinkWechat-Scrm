import {getPoster,getUserInfo,getToken,getWXUserInfo, setFissionComplete} from './api'
import {getUrlParam,getWxCode} from './utils'
import config from './contant'

$(function(){
    let userinfo = localStorage.getItem('userinfo')
    //取缓存中的用户信息
    if(userinfo){
        try {
            userinfo = JSON.parse(userinfo)
            fissionComplete(userinfo.openId)
        } catch (error) {
            alert(error)
        }
        return
    }
    //缓存中没有用户信息，进入授权流程
    getWxCode()
    const code = config.code
    if(!code){
        //防止跳转前进入流程
        return
    }
    try {
        getToken(code)
            .then(res=>{
                let data =res.data
                if(data && data.openId){
                    localStorage.setItem('userinfo',JSON.stringify(data))
                    fissionComplete(userinfo.openId)
                }
            })
       
    } catch (error) {
        alert(error)
    }

    
})

function fissionComplete(openId) {
    const fissionId = getUrlParam('fissionId');
    const recordId = getUrlParam('recordId');

    const wxparams = {
        openId: openId,
        lang: 'zh_CN'
    }

    getWXUserInfo(wxparams)
    .then(resp=>{
        let userData = resp.data;
        setFissionComplete(fissionId, recordId, {
            name: userData.nickName,
            unionid: userData.unionId,
            userid: userData.openId
        })
    })
}
