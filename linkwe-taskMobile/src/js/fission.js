import '../css/fission.css'
import '../css/base.css'

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
            setErrorMessage('获取用户信息失败')
        }
        return
    }
    //缓存中没有用户信息，进入授权流程
    getWxCode()

    const code = config.code
    console.log('code: ', code)
    if(!code){
        console.error('code获取失败')
        //防止跳转前进入流程

        setErrorMessage('微信授权失败')

        return
    }
    try {
        getToken(code)
            .then(res=>{
                let data =res.data
                if(data && data.openId){
                    localStorage.setItem('userinfo',JSON.stringify(data))
                    fissionComplete(userinfo.openId)
                } else {
                    setErrorMessage('获取用户信息失败')
                }
            })
       
    } catch (error) {
        alert(error)
        setErrorMessage('获取用户信息失败')
    }
})

function fissionComplete(openId) {
    const fissionId = getUrlParam('fissionId');
    const recordId = getUrlParam('recordId');

    console.log('参数 fissionId: ', fissionId)
    console.log('参数 recordId: ', recordId)
    console.log('参数 openId: ', openId)

    if (!(fissionId && recordId && openId)) {
        setErrorMessage('缺失必要数据, 请联系管理员')
        return
    }

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
            userid: userData.openId,
            avatar: userData.headImgUrl
        }).then((resp) => {
            if (resp.code === 200 && resp.data) {
                setQrCode(resp.data)
            } else {
                setErrorMessage('未获取到可用的二维码')
            }
        })
    })
}

function setErrorMessage (message) {
    $('#qrcode').css('display', 'none')
    $('#error').css('display', 'block')
    $('#message').html(message)
}

function setQrCode (qrcode) {
    $('#qrcode').css('display', 'block')
    $('#error').css('display', 'none')
    $('#qrcodeImg').attr('src', qrcode)
}
