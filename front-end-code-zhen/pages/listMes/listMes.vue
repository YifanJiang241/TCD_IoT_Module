<template>
	<view>
		<view>
			<view class="bookMes">
				<view class="bookMesHead">Family status</view>
				<input placeholder="error" disabled="false" :value="safeType"/>
			</view>
			<view class="bookMes">
				<view class="bookMesHead">luminosity(lux)</view>
				<input placeholder="error" disabled="false" :value="deviceData.lumValue"/>
			</view>
			<view class="bookMes">
				<view class="bookMesHead">temperature</view>
				<input placeholder="error" disabled="false" :value="deviceData.hum"/>
			</view><view class="bookMes">
				<view class="bookMesHead">humidity</view>
				<input placeholder="error" disabled="false" :value="deviceData.temp"/>
			</view>
			<button @click="toTu()" class="buttonLast">Exit login</button>
			<button @click="toLeave()" class="buttonLast" v-if="!LeaveFlag">Leaving Home Mode</button>
			<button @click="ExitLeave()" class="buttonLast" v-else>Exit Leaving Home Mode</button>
			<button @click="toSleep()" class="buttonLast" v-if="!SleepFlag">Sleep mode</button>
			<button @click="ExitSleep()" class="buttonLast" v-else>Exit Sleep mode</button>
			<button @click="Operation()" class="buttonLast" v-if = "!allFlag">Operation mode</button>
			<button @click="AutoOperation()" class="buttonLast" v-else>Auto mode</button>
			<view v-if="allFlag">
				<button @click="turnOn()" class="buttonLast" v-if="light == 0">turn on the light</button>
				<button @click="turnOff()" class="buttonLast" v-else>Turn off the lights</button>
				<!-- <button @click="simulation()" class="buttonLast" v-if="simulationFlag==0">Turn on the buzzer</button>
				<button @click="simulationClose()" class="buttonLast" v-else>Turn off the buzzer</button> -->
				
				<button @click="left()" class="buttonLast" v-if="curtains==0">Open the curtains</button>
				<button @click="leftClose()" class="buttonLast" v-else>Close the curtains</button>
				<button @click="right()" class="buttonLast" v-if="!door">Open the door</button>
				<button @click="rightClose()" class="buttonLast" v-else>Close the door</button>
			</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				deviceData:{},
				simulationFlag:'',
				curtains:1,
				door:1,
				light:1,
				LeaveFlag:1,
				SleepFlag:1,
				allFlag:false,
				safeType:'Safe'
			}
		},
		onLoad(options) {
			
		},
		methods: {
			toLeave(){
				this.LeaveFlag = 1
				const mes={
					deviceType:'toLeave',
				}
				this.$store.commit("sendToServe",mes)
			},
			ExitLeave(){
				this.LeaveFlag = 0
				const mes={
					deviceType:'ExitLeave',
				}
				this.$store.commit("sendToServe",mes)
			},
			toSleep(){
				this.SleepFlag = 1
				this.allFlag = false
				const mes={
					deviceType:'toSleep',
				}
				this.$store.commit("sendToServe",mes)
			},
			ExitSleep(){
				this.SleepFlag = 0
				const mes={
					deviceType:'ExitSleep',
				}
				this.$store.commit("sendToServe",mes)
			},
			Operation(){
				this.allFlag = true
				const mes={
					deviceType:'allFlagClose',
				}
				this.$store.commit("sendToServe",mes)
			},
			AutoOperation(){
				this.allFlag = false
				const mes={
					deviceType:'allFlagTrue',
				}
				this.$store.commit("sendToServe",mes)
			},
			toTu(){
				uni.reLaunch({
					url: '../index/index'
				});
			},
			turnOn(){
				this.light = 1
				const mes={
					deviceType:'turnOnLight',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			},
			turnOff(){
				this.light = 0
				const mes={
					deviceType:'turnOffLight',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			},
			simulation(){
				this.simulationFlag=1;
				const mes={
					deviceType:'simulation',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			},
			simulationClose(){
				this.simulationFlag=0;
				const mes={
					deviceType:'simulationClose',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			},
			left(){
				this.curtains=1;
				const mes={
					deviceType:'OpenCurtains',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			},
			leftClose(){
				this.curtains=0;
				const mes={
					deviceType:'CloseCurtains',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			},
			right(){
				this.door=1;
				const mes={
					deviceType:'OpenDoor',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			},
			rightClose(){
				this.door=0;
				const mes={
					deviceType:'CloseDoor',
				}
				this.$store.commit("sendToServe",mes)
				uni.showToast({
					title:"OK"
				})
			}
		},
		computed:{
				allData(){
					this.$store.state.allData
				},
			},
		watch:{
				allData:{
					deep: true,
					handler(res){
						this.oldDeviceData=this.$store.state.allData
						if(this.oldDeviceData.deviceId==this.deviceId){
							this.deviceData=this.oldDeviceData
							if(!this.deviceData.pinBuzzerFlag){
								this.safeType = "Warn!"
							}else{
								this.safeType = "Safe"
							}
							this.light = this.deviceData.lightFlag
							this.curtains = this.deviceData.curtains
							this.door = this.deviceData.door
							this.LeaveFlag = this.deviceData.LeaveFlag
							this.SleepFlag = this.deviceData.SleepFlag
						}
					}
				}
			},
		
	}
</script>

<style>
	.bookMes{
		display: flex;
		justify-content: center;
		align-items: center;
		padding-top: 6%;
		font-size: 40rpx;
		margin-bottom: 2%;
	}
	.bookMesHead{
		width: 40%;
		margin-right: 3%;
	}
	input{
		border-bottom: 1rpx solid #55ff7f;
		text-align: center;
		font-size: 40rpx;
		width: 30%;
	}
	.buttonLast{
		margin-top: 5%;
		margin-bottom: 5%;
	}
</style>
