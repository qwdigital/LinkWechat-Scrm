<template>
    <div class="content">
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="员工检索" name="1">
               <employee></employee>
            </el-tab-pane>
            <el-tab-pane label="客户检索" name="2" >
              <user v-if="activeName==2"></user>
            </el-tab-pane>
            <el-tab-pane label="全局检索" name="3">
              <role v-if="activeName==3"></role>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>

import employee from './page/employeeTest.vue'
import user from './page/userTest.vue'
import role from './page/roleTest.vue'
    export default {
        components:{employee,user,role},
        data() {
            return {
                activeName: '1',
            }
        },
        methods: {
            handleClick(tab, event) {
                console.log(tab, event);
            }
        }
    }
</script>
<style scoped>
 *{margin: 0;padding: 0;}
</style>
