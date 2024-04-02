<template>
	<view class="content">
		<image src="../../static/login/loginAll.png" class="backgroundImage"></image>
		<view class="loginHead">
				<text>Welcome!</text>
		</view>
		<view class="loginImg"></view> 
		<view class="loginContent">
			<view class="loginaccount">
				<view class="loginaccountHead">
					<input @input="getaccount"  type="text" placeholder="Account" :value="logindata.account" >
				</view>
				<view class="buttom01"></view>
				<view class="loginaccountLast">
					<input @input="getpassword"  type="password" placeholder="Password" :value="logindata.password">	
				</view>
				<view class="buttom01"></view>
			</view>
			<view>
				<button :class="logindata.account && logindata.password ? 'loginButton01' : 'loginButton'" style="color: #A4B3DE;" @click="login">Login</button>
				<button class="loginButton01" style="color: #A4B3DE;margin-top: 30rpx;" @click="register">Register</button>
			</view>
			
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				logindata:{
					account:'',
					password:'',
				}
			};
		},
		onLoad() {
			let _this=this; 
			
		},
		methods: {
			getaccount(e){
				this.logindata.account=e.target.value;
			},
			getpassword(e){
				this.logindata.password=e.target.value;
			},
			login(){
				var that = this;
				uni.request({
				    url: 'http://192.168.195.170:8090/allData/login/loginMsg', //仅为示例，并非真实接口地址。
					method:'post',
				    data: {
				        account: that.logindata.account,
						password:that.logindata.password
				    },
				    header: {
				        'content-type': 'application/x-www-form-urlencoded'
				    },
				    success: (res) => {
				        if(res.data != 'fail'){
							uni.setStorageSync('token', res.data);//保存token
							console.log(uni.getStorageSync('token'));//取出token
							uni.switchTab({
							 	url:'../../pages/listMes/listMes'
							})	
						}else{
							uni.showToast({
								title: '登陆错误！',
								duration: 1000,
								icon:'error'
							});
						}
				    }
				});
			},
			register(){
				uni.reLaunch({
					url: '../register/register'
				});
			}
		}
	}
</script>

<style>
	.content{
		width: 750rpx;
		height: 1624rpx;

	}
	.backgroundImage{
		width: 100%;
		height: 100%;
		border-radius: 20rpx;
		position: absolute;
		left: 0px;
		top: 0px;
		z-index: -1;
	}
	.icon-denglu{
		color: #3D7CFF ;
	}
	.icon-denglumima{
		color: #3D7CFF ;
	}
	
	.content{
		height: 100%;
	}
	.loginHead{
		height: 35rpx;
		font-size: 36rpx;
		color: #A4B3DE;
		padding-top: 90rpx;
		padding-left: 30rpx;
		margin-bottom: 40rpx;
	}
	.loginImg{
		padding-top: 141rpx;
		background-repeat: no-repeat;
		width: 100%;
		height: 260rpx;
	}
	.loginImg image{
		width: 100%;
	}
	.loginContent{
		margin-top:30rpx;
	}
	.loginaccount{
		padding-left: 70rpx;
		padding-bottom: 65rpx;
	}
	.loginaccountHead{
		height: 140rpx;
		display:flex;
		align-items: center;
        color: #A4B3DE;
	}
	.loginaccountLast{
		height: 140rpx;
		display:flex;
		align-items: center;
	}
	#accounticon{
		font-size: 44rpx;
		float: left;
		margin-right: 22rpx;
	}
	#passwordicon{
		font-size: 44rpx;
		float: left;
		margin-right: 22rpx;
	}
	input{
		width: 550rpx;
		/* border-bottom: 1rpx solid #A4B3DE; */
		height: 44rpx;
		float: left;
	}
	.buttom01{
		width: 610rpx;
		height: 1rpx;
		border-bottom: 1rpx solid #A4B3DE;
	}
	.loginButton{
		width: 610rpx;
		height: 88rpx;
		line-height: 88rpx;
		background-color: #E6EDFF ;
		border-bottom: 100rpx;
		border-radius: 20rpx;
	}
	.loginButton01{
		width: 610rpx;
		height: 88rpx;
		background: #3D7CFF;
		box-shadow: 0rpx 4rpx 20rpx 0rpx rgba(5,86,255,0.5000);
		border-radius: 20rpx;
		border-bottom: 100rpx;
		
	}
	.loginlast{
		width: 311rpx;
		height: 23rpx;
		font-size: 26rpx;
		font-family: Noto Sans S Chinese;
		font-weight: 400;
		color: #5555ff;
		padding-left: 266rpx;
	}
</style>
