<template>
	<view class="contentAll">
		<image src="../../static/dataShow/1.png" class="temperatureContentImage"></image>
		<view class="contentOne">
			<view class="contentall">
				<view class="content01">
					<view>
						<div class="indexMiddle">Real time humidity:{{Svalue}}</div>
					</view>
				</view>
			</view>
			<view class="eccanvasDiv">
				<qiun-data-charts 
				type="demotype" 
				:chartData="chartDataS"
				:opts="optionS"
				:animation="false"
				canvasId="firstS"
				>
				</qiun-data-charts>
			</view>
		</view>
		<view class="contentOne">
			<view class="contentall">
				<view class="content01">
					<view>
						<div class="indexMiddle">Real time lighting:{{Lvalue}}</div>
					</view>
				</view>
			</view>
			<view class="eccanvasDiv">
				<qiun-data-charts 
				type="demotype" 
				:chartData="chartDataL"
				:opts="optionL"
				:animation="false"
				canvasId="firstL"
				>
				</qiun-data-charts>
			</view>
		</view>
		<view class="contentOne">
			<view class="contentall">
				<view class="content01">
					<view>
						<div class="indexMiddle">Real time temperature:{{Tvalue}}</div>
					</view>
				</view>
			</view>
			<view class="eccanvasDiv">
				<qiun-data-charts 
				type="demotype" 
				:chartData="chartDataT"
				:opts="optionT"
				:animation="false"
				canvasId="firstT"
				>
				</qiun-data-charts>
			</view>
		</view>
	</view>
	
</template>

<script>
	export default {
		data() {
			return {
				chartDataT:{
					categories:[],
					series: [
					          {
					            name: "TempValue",
					            data:[],
					          },
							]
				},
				optionT:{
					update: true,//当update: true时，即你使用chartData更新的数据不会使界面重新渲染，只会更新数据
					xAxis:{
						rotateLabel:true,
						rotateAngle:45
					}
				},
				chartDataL:{
					categories:[],
					series: [
					          {
					            name: "LumValue",
					            data:[],
					          },
							]
				},
				optionL:{
					update: true,//当update: true时，即你使用chartData更新的数据不会使界面重新渲染，只会更新数据
					xAxis:{
						rotateLabel:true,
						rotateAngle:45
					}
				},
				chartDataS:{
					categories:[],
					series: [
					          {
					            name: "HumValue",
					            data:[],
					          },
							]
				},
				optionS:{
					update: true,//当update: true时，即你使用chartData更新的数据不会使界面重新渲染，只会更新数据
					xAxis:{
						rotateLabel:true,
						rotateAngle:45
					}
				},
				Tvalue:0,
				Lvalue:0,
				Svalue:0,
				lookId:''
			};
		},
		components:{
			
		},
		methods: {
			lookUp:function(e){
				this.chartDataT.categories=[];
				this.chartDataT.series[0].data=[];
				this.chartDataS.categories=[];
				this.chartDataS.series[0].data=[];
				this.chartDataL.categories=[];
				this.chartDataL.series[0].data=[];
				this.lookId=e.target.value
				console.log(this.lookId)
			}
		},
		onLoad() {
		},		
		computed:{
			allData(){
				this.$store.state.allData
			}
		},
		watch:{
			allData:{
				deep:true,
				handler(){
					console.log("sss")
					this.Tvalue=this.$store.state.allData.temp;
					this.Svalue=this.$store.state.allData.hum;
					this.Lvalue=this.$store.state.allData.lumValue;
					this.chartDataT.categories.push(this.$store.state.allData.nowTime);
					this.chartDataS.categories.push(this.$store.state.allData.nowTime);
					this.chartDataL.categories.push(this.$store.state.allData.nowTime);
					this.chartDataT.series[0].data.push(this.Tvalue);
					this.chartDataS.series[0].data.push(this.Svalue);
					this.chartDataL.series[0].data.push(this.Lvalue);
					if(this.chartDataT.categories.length>10 ||this.chartDataT.series[0].data.length>10){//修改数据
					   this.chartDataT.categories.shift();
					   this.chartDataT.series[0].data.shift();
					}
					if(this.chartDataS.categories.length>10 ||this.chartDataS.series[0].data.length>10){//修改数据
					   this.chartDataS.categories.shift();
					   this.chartDataS.series[0].data.shift();
					}
					if(this.chartDataL.categories.length>10 ||this.chartDataL.series[0].data.length>10){//修改数据
					   this.chartDataL.categories.shift();
					   this.chartDataL.series[0].data.shift();
					}
				}
			}
		}
	}
</script>

<style>
	.contentOne{
		padding-top:3%;
	}
	.contentall{
		padding-bottom: 3%;
	}
	.temperatureContentImage{
		width: 100%;
		height: 100%;
		border-radius: 20rpx;
		position: fixed;
		left: 0px;
		top: 0px;
		z-index: -1;
	}
	.uni-ec-canvas {
		width: 100%;
		height: 600rpx;
		display: block;
		margin-top: 100rpx;
	}
	.indexImage{
		width: 100%;
		height: 100%;
		border-radius: 20rpx;
		position: absolute;
		left: 0px;
		top: 0px;
		z-index: -1;
	}
	.contentHead{
		padding-top: 5%;
		margin-bottom: 3%;
		display:flex;
		justify-content:center;
		align-items: center;
	}
	.indexHead{
		color: #000000;
		font-size: 150%;
	}
	.indexMiddle{
		margin-top: 3%;
		display: flex;
		justify-content: center;
		align-items: center;
		color: #000000;		
	}
	.contentId{
		margin-top: 5%;
		display:flex;
		justify-content:center;
		align-items: center;
	}
	.contentId input{
		width: 30%;
	}
	
</style>
