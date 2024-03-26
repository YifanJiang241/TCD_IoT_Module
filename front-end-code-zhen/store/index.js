import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);
 
export default new Vuex.Store({
    state:{
        account:'cx',
		password:'123',
		allData:{},//自行车数据
		sendMes:{},//发送的数据
    },
    mutations:{
		updateAllData(state,n){
			state.allData=n
		},
		sendToServe(state,n){
			state.sendMes=''
			state.sendMes=n
		}
    }
})