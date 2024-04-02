<template>
	<view>
		<view class="top-box">
			<view>Hi</view>
			<view class="next-text">Welcome to register！</view>
		</view>
		<view class="center-box">
			<view class="input-box">
				<view class="wei-input">
					<icon type="waiting" color="#44ADFB" size="16"></icon>
					<input class="input" v-model="telNum" auto-focus placeholder="Please enter a phone number"></input>
				</view>
				<view class="wei-input">
					<icon type="waiting" color="#44ADFB" size="16"></icon>
					<input class="input" auto-focus placeholder="Please enter a verification code" v-model="code" />
					<text @click="getCode(e)"  class="input-code" >{{codeText}}</text>
				</view>
				<view class="wei-input">
					<icon type="success" color="#44ADFB" size="16"></icon>
					<input class="input" v-model="password" placeholder="Please enter password"></input>
				</view>
				<view class="wei-input">
					<icon type="success" color="#44ADFB" size="16"></icon>
					<input class="input" type="password" v-model="password1" placeholder="Please enter password again"></input>
				</view>
			</view>
			<view class="sumbit-btn">
				<button class="button" style="background-color: #33ccff;font-size: 30rpx;" type="primary" @click="register()">立即注册</button>
				<button class="button" @click="toLogin()" style="background-color: #33ccff;font-size: 30rpx;margin-top: 3%;"	
					type="primary">返回登陆</button>
			</view>
		</view>
		<view class="shadow shadow-1"></view>
		<view class="shadow shadow-2"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				telNum: '',
				codeText: 'code',
				code:'',
				password: '',
				password1:'',
			}
		},
		methods: {
			//注册
			register(){
				var regPhone = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
				if (!regPhone.test(this.telNum)) {
					uni.showToast({
						title: 'Mobile phone format error!',
						duration: 1000,
						icon:'error'
					});
					this.telNum = ''
					return;
				}
				if(this.password != this.password1){
					uni.showToast({
						title: 'Password inconsistency!',
						duration: 1000,
						icon:'error'
					});
					this.password1 = ''
					return;
				}
				let that = this
				uni.request({
				    url: 'http://192.168.195.170:8090/allData/login/insertUser', //仅为示例，并非真实接口地址。
					method:'post',
				    data: {
				        userName: that.telNum,
						userPassword:that.password
				    },
				    header: {
				        'content-type': 'application/x-www-form-urlencoded'
				    },
				    success: (res) => {
				        if(res.data != 'false'){
							uni.setStorageSync('token', res.data);//保存token
							console.log(uni.getStorageSync('token'));//取出token
							uni.switchTab({
							 	url:'../../pages/listMes/listMes'
							})	
						}else{
							uni.showToast({
								title: 'error！',
								duration: 1000,
								icon:'error'
							});
						}
				    }
				});
			},
			//返回登陆
			toLogin() {
				uni.reLaunch({
					url: '../index/index'
				});
			},
			//获取验证码 
			getCode(e) {
				var code = '0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM',//62个字符 随机选择4位
				char = '',
				result = '';
				for (var i = 0; i < 6; i++) {
					//随机选择一位  （0,61） 写出0到61的随机的索引数字
					var code_index = Math.round(Math.random()*61);
					//得到随机的索引  取出随机地字符
					var char = code[code_index];
					//随机取出的字符 存在几个相同重复的问题 ，而且对于字母，不能区分大小写。
					// 避免重复的思路是：取出字符之后,和最后的result对比一下，看看里边是不是已经存在了，如果存在本次循环就终止，进行下一次
					if (result.toUpperCase().indexOf(char.toUpperCase()) > -1)
					//indexOf() == -1 说明结果里边没有要找的字符 那么 > -1 就是 里边有重复的字符
					{
						i --;
						//为什么会 --？ 因为如果条件成立，那么本轮循环就结束进行下一轮循环（自然i就加1了），那么本轮本应该取出的字符就没有了
						//到最后会少一个字符 缺席
						continue;//终止本轮循环 进行下一轮
					}
					result += char;
				}
				this.code = result
				this.countDown(e,10)
			},
			//倒计时60秒
			countDown(e,count) {
				let that = this
				that.codeText =  count
				var timer = setInterval(function() {
					if (count == 0) {
						// 清除定时器和复原按钮
						clearInterval(timer);
						that.codeText =  '获取验证码'
					} else {
						count--
						that.codeText =  count
					}
				}, 1000);
			},
		}
	}
</script>

<style>
	page {
		height: 100%;
		background-color: white;
		padding: 0px;
	}

	/* 顶部背景 */
	.top-box {
		height: 30%;
		background-image: linear-gradient(#44ADFB, #5ed6fd);
		padding: 30rpx;
		color: white;
		font-weight: bold;
		padding-bottom: 40%;	
	}

	.next-text {
		margin-top: 15rpx;
	}

	/* 内容 */
	.center-box {
		background-color: white;
		margin: -20% 20rpx 0rpx 20rpx;
		padding: 25rpx;
		border-radius: 15rpx;
		-webkit-filter: drop-shadow(0 0 8rpx #44ADFB);
		filter: drop-shadow(0 0 8rpx #44ADFB);
	}

	/* 导航 */
	.nav {
		display: flex;
		text-align: center;
		font-size: 32rpx;
		margin-bottom: 8%;
	}

	.left {
		flex: 1;
		font-weight: bold;
	}

	.right {
		flex: 1;
		font-weight: bold;
	}

	.select {
		font-weight: bold;
		color: #33ccff;
	}

	.select text {
		padding-bottom: 5rpx;
		border-bottom-left-radius: 10rpx;
		border-bottom-right-radius: 10rpx;
		border-bottom: 5rpx solid #33ccff;
	}

	.wei-input {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-top: 40rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #f1f1f1;
	}

	.input-box {
		margin: 20rpx;
	}

	.input {
		padding-left: 20rpx;
		font-size: 30rpx;
		width: 100%;
	}

	.input-code {
		position: absolute;
		right: 40rpx;
		font-size: 26rpx;
		padding: 10rpx 15rpx;
		color: white;
		background-color: #FF8C69;
		border-radius: 10rpx;
	}

	.forget {
		font-size: 26rpx;
		color: #33ccff;
		margin-top: 20rpx;
		text-align: right;
	}

	.sumbit-btn {
		margin: 6% 30rpx 30rpx 30rpx;
	}

	/* 重影 */
	.shadow {
		box-shadow: 0rpx 0rpx 10rpx 0rpx #44ADFB;
		border-radius: 25rpx;
		background-color: white;
	}

	.shadow-1 {
		height: 40rpx;
		margin: -20rpx 50rpx 0 50rpx;
	}

	.shadow-2 {
		position: relative;
		z-index: -888;
		height: 50rpx;
		margin: -30rpx 80rpx 0 80rpx;
	}

	/* 最底部 */
	.bottom-box {
		position: fixed;
		bottom: 10rpx;
		width: 100%;
		font-size: 24rpx;
		color: gray;
		display: flex;
		justify-content: center;
	}
</style>