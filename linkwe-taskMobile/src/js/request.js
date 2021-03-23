const baseUrl = window.location.origin.includes('localhost') ? window.location.origin : 'http://106.13.201.219'
// const baseUrl = window.location.origin+'/mock/11'
// const baseUrl = 'http://47.112.117.15:40001/mock/11'

const request = (url,params,method='get')=>{
    url = baseUrl + url
    return new Promise((relosve,reject)=>{
        $.ajax({
            headers: {
                "Authorization":'Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MTcyNjk4NzQsImxvZ2luX3VzZXJfa2V5IjoiYzhiZGE0NWEtMTdiNC00YjU2LTk1MzItNmFkMDdhM2UyZTI0In0.-ZFDrpsAL_bE4kLi5yyrpxBhqd5v7ekfbAB47_Rpn_aLaOKCPHCKGa9UCNtWgf2Itu3hb2o_9otVnwku9GbG4g',
              },
            url,
            data:params,
            dataType:"json",
            type:method,
            success(res){
                // if(res.code !== 200){
                //     alert(res.msg)
                // }
                relosve(res)
            },
            error(err){
                reject(err)
            }
        })
    })
}
export default request