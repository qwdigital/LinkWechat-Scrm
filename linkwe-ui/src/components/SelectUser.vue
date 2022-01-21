<script>
  import { getTree, getList } from '@/api/organization'
  import { createUniqueString } from '@/utils'
  export default {
    name: 'SelectUser',
    components: {},
    props: {
      // 添加标签显隐
      visible: {
        type: Boolean,
        default: false,
      },
      title: {
        type: String,
        default: '组织架构',
      },
      // 是否 只选择叶子节点（人员节点）/禁止选择父节点（部门节点）
      isOnlyLeaf: {
        type: Boolean,
        default: true,
      },
      // 是否单选
      isSigleSelect: {
        type: Boolean,
        default: false,
      },
      defaultValues: {
        type: Array,
        default: () => []
      },
      destroyOnClose: Boolean
    },
    data () {
      let isOnlyLeaf = this.isOnlyLeaf
      return {
        treeData: [],
        userList: [],
        defaultProps: {
          label: 'name',
          children: 'children',
          disabled (data, node) {
            return isOnlyLeaf && !data.userId
          },
          isLeaf (data, node) {
            return !data.id
          },
        },
      }
    },
    watch: {
      defaultValues (value) {
        if (this.$refs.tree) this.$refs.tree.setCheckedKeys([])
      }
    },
    computed: {
      Pvisible: {
        get () {
          // this.getTree();
          return this.visible
        },
        set (val) {
          this.$emit('update:visible', val)
        },
      },
      defaultCheckedKeys () {
        const checkedList = []
        const checkedUserList = []
        if (
          !Array.isArray(this.defaultValues) ||
          this.defaultValues.length == 0
        ) {
          this.userList = []
        } else {
          this.defaultValues.forEach(i => {
            const id = this.isOnlyLeaf ? i.userId : (i.userId || i.id)
            if (id) {
              checkedList.push(id)
              checkedUserList.push(i)
            }
          })
          this.userList = checkedUserList
        }
        return checkedList
      }
    },
    created () { },
    mounted () { },
    methods: {
      treeFormat (list) {
        let dealOptions = []
        list.forEach(one => {
          let findIndex = list.findIndex(item => {
            return item.id === one.parentId
          })
          if ((!one.parentId && one.parentId !== 0 && one.parentId !== false) || findIndex === -1) {
            dealOptions.push(one)
          } else {
            if (!list[findIndex].children) {
              list[findIndex].children = []
              list[findIndex].children.push(one)
            } else {
              list[findIndex].children.push(one)
            }
          }
        })
        return dealOptions
      },
      loadNode (node, resolve) {
        if (node.level === 0) {
          if (!Array.isArray(this.defaultValues) ||
            this.defaultValues.length == 0
          ) {
            this.userList = []
          }
          getTree().then(({ data }) => {
            // data.forEach((element) => {
            //   element.key = createUniqueString()
            // })
            let _data = this.handleTree(data)
            resolve(this.treeFormat(_data))
            // api.getList({ department: _data[0].id }).then(({ rows, total }) => {
            //   _data && rows.unshift(..._data);
            //   resolve(rows);
            // });
          })
        } else {
          getList({ department: node.data.id, isActivate: 1 }).then(
            ({ rows }) => {
              // rows.forEach((element) => {
              //   element.key = createUniqueString()
              // })
              node.data.children && rows.push(...node.data.children)
              resolve(rows)
            }
          )
        }
      },
      handleTree (data) {
        return data.map(i => {
          if (i.id) {
            i.userId = i.id
            i.isParty = true
          }
          return i
        })
      },
      // 选择变化
      handleCheckChange (data, checked, indeterminate) {
        // console.log(arguments)
        if (checked) {
          if (this.isSigleSelect) {
            // 单选情况
            this.$refs.tree.setCheckedKeys([data.userId])
          }
          if (this.isOnlyLeaf) {
            if (data.userId && !data.isParty) {
              this.userList.push(data)
            }
          } else {
            const isExist = this.userList.findIndex(i => i.userId === data.userId) > -1
            !isExist && this.userList.push(data)
          }
        } else {
          // let index = this.userList.indexOf(data)
          let index = this.userList.findIndex(i => i.userId === data.userId)
          index > -1 && this.userList.splice(index, 1)
        }
        // console.log(data, checked, indeterminate);
        this.userList = this.unique(this.userList)
      },
      unique (arr) {
        for (var i = 0; i < arr.length; i++) {
          for (var j = i + 1; j < arr.length; j++) {
            if (arr[i].userId.split('_')[0] == arr[j].userId.split('_')[0]) {
              arr.splice(j, 1);
              j--;
            }
          }
        }
        return arr;
      },
      // 确 定
      submit () {
        this.Pvisible = false
        this.$emit('success', [...this.userList])
      },
      // 取消选择
      cancle (key) {
        this.$refs.tree.setChecked(key, false)
        let index = this.userList.findIndex(i => i.userId === key)
        index > -1 && this.userList.splice(index, 1)
        let order = this.defaultValues.findIndex(i => i.userId === key)
        order > -1 && this.defaultValues.splice(order, 1)
      },
    },
  }
</script>
<template>
  <el-dialog :title="title" :visible.sync="Pvisible" :destroy-on-close="destroyOnClose">
    <el-row :gutter="20">
      <el-col :span="12" :xs="24">
        <div class="head-container">
          <el-tree node-key="userId" ref="tree" lazy accordion show-checkbox :check-on-click-node="false" :expand-on-click-node="true" :default-checked-keys="defaultCheckedKeys" :load="loadNode" :props="defaultProps" :check-strictly="isOnlyLeaf" @check-change="handleCheckChange"></el-tree>
        </div>
      </el-col>
      <el-col :span="12" :xs="24" class="user-list">
        <el-row :gutter="10">选择人员列表</el-row>
        <el-row v-for="(item, index) in userList" :key="index">
          <i class="el-icon-user-solid"></i>
          {{ item.name }}
          <i class="el-icon-minus fr cp" title="取消选择" @click="cancle(item.userId)"></i>
        </el-row>
      </el-col>
    </el-row>
    <div slot="footer">
      <el-button @click="Pvisible = false">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
  .user-list {
    .el-row {
      line-height: 26px;
    }
  }
</style>
