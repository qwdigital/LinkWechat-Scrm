/**
 * vue 自定义阻止按钮连续点击指令
 * 使用 <div @click="search" v-preventReClick="1000">搜索</div>
 */
export default {
  inserted(el, binding) {
    el.addEventListener('click', () => {
      el.style.pointerEvents = 'none'
      setTimeout(() => {
        el.style.pointerEvents = 'auto'
      }, binding.value || 2000)
    })
  },
}
