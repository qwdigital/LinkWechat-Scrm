<template>
    <div class="content">
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="根据员工检测" name="1">
               <employee></employee>
            </el-tab-pane>
            <el-tab-pane label="根据用户检测" name="2">
              <user></user>
            </el-tab-pane>
            <el-tab-pane label="全局检测" name="3">
              <role></role>
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
