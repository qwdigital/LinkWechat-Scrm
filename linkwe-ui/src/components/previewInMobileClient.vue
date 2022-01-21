<template>
	<div class="preview">
		<div class="title">{{name}}</div>
		<div class="content">
			<div class="item">
				<div class="avatar"><img src="../assets/drainageCode/header.png"></div>
				<div class="msg">
					<div class="word">
						{{list.welcomeMsg}}
					</div>
				</div>
			</div>
			<div class="item" v-for="(data, key) in list.materialMsgList" :key="key">
				<div class="avatar"><img src="../assets/drainageCode/header.png"></div>
				<div class="msg" v-if="data.msgType === '4'">
					<div class="word">
						{{data.content}}
					</div>
				</div>
				<div class="msg" v-if="data.msgType === '0'">
					<div class="image">
						<img :src="data.materialUrl" />
					</div>
				</div>
				<div class="msg" v-if="data.msgType === '8'">
					<div class="word-and-image">
						<div>
							{{data.materialName}}
						</div>
						<div class="sub-content">
							<div>
								<!-- {{data.materialUrl}} -->
							</div>
							<div>
								<img src="../assets/drainageCode/default_icon.png" />
							</div>
						</div>
					</div>
				</div>
				<div class="msg" v-if="data.msgType === '9'">
					<div class="miniprogram">
						<div class="mini-header">
							<img src="../assets/drainageCode/miniprogram-icon.png" alt=""> {{data.materialName}}
						</div>
						<img class="mini-img" :src="data.coverUrl">
						<div class="mini-footer">
							<img src="../assets/drainageCode/miniprogram-link.png" /> 小程序
						</div>
					</div>
				</div>
			</div>
			<!-- <div class="item">
				<div class="avatar"><img src="../assets/drainageCode/header.png"></div>
				<div class="msg">
					<div class="image">
						<img  src="../assets/drainageCode/demo-img.png" />
					</div>
				</div>
			</div> -->
			<!-- <div class="item">
				<div class="avatar"><img src="../assets/drainageCode/header.png"></div>
				<div class="msg">
					<div class="word-and-image">
						<div>
							全新智能电池系统，首秀小米
						</div>
						<div class="sub-content">
							<div>
								三大能力延缓电池老化
							</div>
							<div>
								<img src="../assets/drainageCode/preview-middle-img.png" />
							</div>
						</div>
					</div>
				</div>
			</div> -->
			<!-- <div class="item">
				<div class="avatar"><img src="../assets/drainageCode/header.png"></div>
				<div class="msg">
					<div class="miniprogram">
						<div class="mini-header">
							<img src="../assets/drainageCode/miniprogram-icon.png" alt="">
							测试测试测试测试
						</div>
						<img class="mini-img" src="../assets/drainageCode/miniprogram-img.png" >
						<div class="mini-footer">
							<img src="../assets/drainageCode/miniprogram-link.png" />
							小程序
						</div>
					</div>
				</div>
			</div> -->

		</div>
	</div>
</template>
<script>
	export default {
		name: 'preview-client',
		components: {},
		props: {
			name: {
				type: String,
				default: "小微"
			},
			list: {
				type: Object,
				default: {}
			}
		},
		data () {
			return {}
		},
		computed: {},
		created () { },
		mounted () { },
		methods: {},
	}
</script>
<style lang="scss" scoped>
	.preview {
	  margin: 0 auto;
	  position: relative;
	  width: 300px;
	  height: 640px;
	  background: url(../assets/drainageCode/preview-bg.png);
	  background-repeat: no-repeat;
	  background-size: 100% 100%;

	  .title {
	    position: absolute;
	    top: 37px;
	    left: 50%;
	    transform: translateX(-50%);
	    font-size: 14px;
	    font-family: PingFangSC-Regular, PingFang SC;
	    font-weight: 400;
	    color: #333333;
	  }

	  .content {
	    position: absolute;
	    height: 545px;
	    width: 280px;
	    overflow-y: auto;
	    top: 70px;
	    left: 50%;
	    transform: translateX(-50%);
	    border-bottom-left-radius: 20px;
	    border-bottom-right-radius: 20px;
	    padding: 20px;

	    .item {
	      font-size: 12px;
	      font-family: PingFangSC-Regular, PingFang SC;
	      font-weight: 400;
	      color: #666666;
	      display: flex;
	      margin-top: 20px;

	      &:first-child {
	        margin-top: 0;
	      }

	      .avatar {
	        // align-self: center;
	        margin-right: 5px;
	      }

	      .msg {
	        position: relative;
	        min-height: 26px;
	        background: #ffffff;
	        box-shadow: 0px 2px 6px 0px rgba(60, 136, 240, 0.1);
	        border: 1px solid #e1edfc;
	        display: inline-block;
	        border-radius: 4px;
	        box-sizing: border-box;
	        word-break: break-all;
	        margin-left: 5px;

	        &:before {
	          content: " ";
	          display: block;
	          position: absolute;
	          left: -5px;
	          box-shadow: 0px 2px 6px 0px rgba(60, 136, 240, 0.1);
	          border-width: 5px;
	          border-left: 0;
	          width: 0;
	          border-style: solid;
	          border-color: transparent;
	          border-right-color: #fff;
	          top: 8px;
	          z-index: 888;
	        }

	        .word {
	          padding: 5px 12px;
	        }

	        .image {
	          padding: 12px;

	          img {
	            width: 94px;
	            height: 90px;
	          }
	        }

	        .word-and-image {
	          padding: 12px;
	          .sub-content {
	            margin-top: 10px;
	            display: flex;
	            justify-content: space-between;
	            font-size: 12px;
	            font-family: PingFangSC-Regular, PingFang SC;
	            font-weight: 400;
	            color: #999999;
	            img {
	              height: 40px;
	              width: 40px;
	            }
	          }
	        }
	        .miniprogram {
	          padding: 12px;
	          .mini-header {
	            display: flex;
	            align-items: center;
	            img {
	              height: 18px;
	              width: 18px;
	              margin-right: 5px;
	            }
	          }
	          .mini-img {
	            margin: 10px 0;
	            width: 143px;
	            height: 120px;
	          }
	          .mini-footer {
	            display: flex;
	            align-items: center;
	            img {
	              height: 14px;
	              width: 14px;
	              margin-right: 5px;
	            }
	          }
	        }
	      }
	    }
	  }
	}
</style>
