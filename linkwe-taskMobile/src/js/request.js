const baseUrl = window.location.origin.includes('localhost') ? window.location.origin : ''
const baseUrl = window.location.origin+'/api'
//https://rm-crm.remycn.com

const request = ({url,params,method='get'})=>{
    url = baseUrl + url
    return new Promise((relosve,reject)=>{
        $.ajax({
            url,
            data:params,
            type:method,
            success(res){
                relosve(res)
            },
            error(err){
                reject(err)
            }
        })
    })
}
export default request