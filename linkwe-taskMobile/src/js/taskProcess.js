import '../css/taskProcess.css'
import '../css/base.css'
import { getCustomerList, getReward } from './api'
import { getUrlParam } from './utils'

// const data = {
//     total:12,
//     completed:9,
//     customers:[
//         {
//             createTime:'string'	,
//             updateTime:'string',
//             externalUserid:'string',
//             name:'string',
//             avatar:'https://link-wechat-1251309172.cos.ap-nanjing.myqcloud.com/2021/03/01/77dc22bf-e483-45f0-86fa-d19a85b6e355.jpg',
//             type:'number',
//             gender:'number',
//             isOpenChat:'number'
//         }
//     ]
// }

$(function () {
  const postersUrl = localStorage.getItem('postersUrl')
  $('.activityPosterImg').attr('src', postersUrl)
  const fissionId = getUrlParam('taskFissionId')
  const eid = getUrlParam('eid')

  getCustomerList({ fissionId, eid }).then((res) => {
    let data = res.data
    let htmlStr = ''
    data.customers.forEach((item) => {
      htmlStr +=
        "<div class='customersList'>" +
        '<img src=' +
        item.avatar +
        " class='customersImg'/>" +
        "<span class=''>" +
        item.firstAddTime +
        '</span>' +
        "<span class='customersSuccess'>邀请成功</span>" +
        '</div>'
    })
    $('.completeNum').text(data.completed)
    const left =
      data.total - data.completed < 0 ? 0 : data.total - data.completed
    $('.loseNum').text(left)
    $('.customersBox').html(htmlStr)
  })
  getReward({ fissionId, eid }).then((res) => {
    let data = res.data
    $('.codeNum').text(data.rewardRule)
    $('.codeLink').text(data.rewardUrl)
    $('.codeImg').attr('src', data.rewardImageUrl)
  })
})
