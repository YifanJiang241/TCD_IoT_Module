<script>
	export default {
		data(){
			return{
				socketTask:null
			}
		},
		created() {
			let _this=this;
			_this.socketTask=uni.connectSocket({
				//url:'ws://192.168.1.100:8099/chat',
				url:'ws://192.168.195.170:8099/chat',
				//url:'ws://192.168.1.101:8099/chat',
				 method: 'GET',
				 success(data) {
						console.log("websocket连接成功");
					}
			}),
			uni.onSocketOpen(function (res) {
			   console.log('WebSocket连接已打开！')			   
			 }),
			 uni.onSocketError(function (res) {
			   console.log('WebSocket连接打开失败，请检查！')
			 }),
			 uni.onSocketMessage(function (res) {
			    var datas=JSON.parse(res.data);
				console.log(datas);
				if(datas.deviceType=="allData"){
					_this.$store.commit('updateAllData',datas)
				}
			 }),		 
			uni.onSocketClose(function (res) {
			  console.log('WebSocket 已关闭！');
			});
		},
		computed:{
			sendMes(){
				this.$store.state.sendMes
			}
		},
		watch:{
			sendMes:{
				deep:true,
				handler(res){
					var datas=this.$store.state.sendMes
					console.log(datas)
					this.socketTask.send({//向后端发送信息
						data: JSON.stringify(datas)
					})
				}
			}
		},
		onHide(){
		},
		destroyed() {
			uni.closeSocket({	
			})
		}
		
	}
</script>

<style>
	/*每个页面公共css */
</style>
