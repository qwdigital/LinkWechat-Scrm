<script>
import { wxQrLogin } from '@/api/login'
import { setToken } from '@/utils/auth'
export default {
  components: {},
  props: {},
  data() {
    return {}
  },
  watch: {},
  computed: {},
  beforeCreate() {
    // http://106.13.201.219/?auth_code=xxx#/authWehatCallback
    // console.log('routerbeforeCreate', this.$route);
    let auth_code = location.search.slice(1).split('=')[1]
    if (!auth_code) {
      this.msgError('未获得授权')
      location.href = '/'
      return
    }
    wxQrLogin(auth_code).then((res) => {
      setToken(res.token)
      this.$store.commit('SET_TOKEN', res.token)
      location.href = '/'
    })
  },
  mounted() {},
  methods: {},
}
</script>

<template>
  <div></div>
</template>
