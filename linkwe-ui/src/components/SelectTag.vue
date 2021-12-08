<script>
  import { getList } from '@/api/customer/tag'

  export default {
    name: 'SelectTag',
    components: {},
    props: {
      // 添加标签显隐
      visible: {
        type: Boolean,
        default: false,
      },
      title: {
        type: String,
        default: '选择标签',
      },
      // selected: {
      //   type: Array,
      //   default: () => [],
      // },
      type: {
        type: String,
        default: 'add',
      },
      destroyOnClose: Boolean,
      defaultValues: {
        type: Array,
        default: () => []
      }
    },
    data () {
      return {
        list: [],
        listOneArray: [],
        selectedGroup: '', // 选择的标签分组
        removeTag: [],
        Pselected: [],
        selectedList: []
      }
    },
    watch: {
      // type(val) {
      //   val === 'remove' && (this.removeTag = this.selected.slice())
      // },
      // selected(val) {
      //   if (this.type === 'add') {
      //     this.Pselected = []
      //     val.forEach((element) => {
      //       let find = this.listOneArray.find((d) => {
      //         return element.tagId === d.tagId
      //       })
      //       this.Pselected.push(find)
      //     })
      //   } else if (this.type === 'remove') {
      //     this.removeTag = val.slice()
      //     this.Pselected = val.slice()
      //   }
      //   // this.list = JSON.parse(JSON.stringify(this.list))
      // },
      // list (val) {
      //   if (this.type === 'add') {
      //     this.Pselected = []
      //     this.selected.forEach((element) => {
      //       let find = this.listOneArray.find((d) => {
      //         return element.tagId === d.tagId
      //       })
      //       this.Pselected.push(find)
      //     })
      //   } else if (this.type === 'remove') {
      //     this.removeTag = this.selected.slice()
      //     this.Pselected = this.selected.slice()
      //   }
      // },
      defaultValues: {
        handler (val = []) {
          this.selectedList = [...val]
        },
        immediate: true
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
          // this.$nextTick(()=> this.selectedList = [])
        },
      },
      checkedTagMap () {
        return new Set(this.selectedList.map(i => i.tagId || (typeof i == 'string' && i)))
      }
    },
    created () {
      this.getList()
    },
    mounted () { },
    methods: {
      // getUserTags() {
      //   getUserTags(this, this.externalUserid)
      //   .then(res => {
      //     if(res.code == 200) {
      //       console.log(res)
      //     }
      //   })
      // },
      getList () {
        getList().then(({ rows }) => {
          // this.list = Object.freeze(rows)
          this.list = rows
          this.listOneArray = []
          this.list.forEach((element) => {
            element.weTags.forEach((d) => {
              this.listOneArray.push(d)
            })
          })
        })
      },
      submit () {
        // if (!this.Pselected.length) {
        //   this.msgError('请至少选择一个标签')
        //   return
        // }
        this.$emit('success', JSON.parse(JSON.stringify(this.selectedList)))
        this.Pvisible = false
      },
      onSelectTag (tag) {
        const existIdx = this.selectedList.findIndex(i => i.tagId === tag.tagId)
        if (existIdx > -1) {
          this.selectedList.splice(existIdx, 1)
        } else {
          this.selectedList.push(tag)
        }
      },
      toJson (data) {
        return JSON.stringify(data)
      },
      setTagSelect (data) {
        return this.checkedTagMap.has(data.tagId) || this.checkedTagMap.has(data.tagName)
      }
    },
  }
</script>

<template>
  <el-dialog :title="title" :visible.sync="Pvisible" :destroy-on-close="destroyOnClose">
    <div>
      <!-- <span class="mr20">选择分组</span>
      <el-select v-model="selectedGroup" placeholder="请选择">
        <el-option label="所有标签" value></el-option>
        <el-option
          v-for="(item, index) in list"
          :key="index"
          :label="item.gourpName"
          :value="item.groupId"
        ></el-option>
      </el-select>
      <div class="mt20" v-if="Pvisible">
        <el-checkbox-group v-if="type !== 'remove'" v-model="Pselected">
          <template v-for="(item, index) in list">
            <div
              class="bfc-d mr30"
              v-if="item.groupId === selectedGroup || !selectedGroup"
              :key="index"
            >
              <template v-for="(unit, unique) in item.weTags">
                <el-checkbox
                  v-if="unit.name.trim()"
                  :label="unit"
                  :key="index + '' + unique"
                  >{{ unit.name }}</el-checkbox
                >
              </template>
            </div>
          </template>
        </el-checkbox-group>
        <el-checkbox-group v-else v-model="Pselected">
          <template v-for="(item, index) in removeTag">
            <el-checkbox
              v-if="item.groupId === selectedGroup || !selectedGroup"
              :label="item"
              :key="index"
              >{{ item.name.trim() || '(空的无效标签，请移除)' }}</el-checkbox
            >
          </template>
        </el-checkbox-group>
      </div> -->
      <section class="label-group">
        <div v-for="item in list" :key="item.groupId" class="label-group-item">
          <div class="label-group-item-title">{{ item.gourpName }}</div>
          <div v-if="item.weTags" class="label-group-item-body">
            <el-tag v-for="tag in item.weTags" :key="tag.tagId" :type="setTagSelect(tag) ? '' : 'info'" @click="onSelectTag(tag)">{{ tag.name }}</el-tag>
          </div>
        </div>

      </section>
      <slot></slot>
    </div>
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
  .mr30 {
    margin-right: 30px;
  }
  .label-group {
    max-height: 400px;
    overflow-x: hidden;
    overflow-y: scroll;
    &-item {
      margin: 10px;
      border-bottom: 1px solid #f1f1f1;

      &-title {
        font-weight: 700;
        line-height: 24px;
      }
      &-body {
        padding: 10px 0;
      }

      .el-tag {
        cursor: pointer;
      }
    }
  }
</style>
