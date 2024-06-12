<template>
  <div>
    <div style="height: calc(100% - 20px); width: calc(100% - 20px); margin: 0 auto; position: relative;">
      <box-border style="height: 100%;"/>
      <div style="position:absolute; top: 0; width: 100%; height: 100%;">
        <div class="box-bg" style="text-align: center; font-size: 20px; color: #FFFFFF; line-height: 35px;">
          收款
        </div>
        <div style=" height: calc(100% - 65px); overflow:auto;">
          <div>
            <div class="box-text-bg select-item" style="height: unset" v-for="(item, index) in data" :key="index" >
              <div style="padding: 10px 0;">{{item.content}} {{item.newsTime}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BoxBorder from "@/views/component/BoxBorder";
export default {
  name: "BoxView1",
  components: {BoxBorder},
  data() {
    return {
      data: [],
      socket: null,
    }
  },
  methods: {
    openSocket() {
      const socketUrl = "ws://localhost:9999/api/pushMessage";
      if(this.socket!=null){
        this.socket.close();
        this.socket=null;
      }
      this.socket = new WebSocket(socketUrl);
      //打开事件
      this.socket.onopen = function() {
        console.log("websocket已打开");
      };
      //获得消息事件
      this.socket.onmessage = (msg) => {
        let data = JSON.parse(msg.data);
        for(let i = 0; i<data.length; i++) {
          for(let j = i + 1; j<data.lenth; j++) {
            let t = data[i];
            let tData = new Date(t.newTime);

            let o = data[j];
            let oData = new Date(o.newTime);

            if (tData.getTime() < oData.getTime()) {
              data[i] = o;
              data[j] = t;
            }
          }
        }
        this.data = data.map(item => {
          let start = item.content.indexOf("【");
          let end = item.content.indexOf("】");
          return {
            newsTime: item.newsTime,
            content: item.content.substring(start, end),
          }
        }).filter(item => item.content);
        //发现消息进入,开始处理前端触发逻辑
      };
      //关闭事件
      this.socket.onclose = function() {
        console.log("websocket已关闭");
      };
      //发生了错误事件
      this.socket.onerror = function() {
        console.log("websocket发生了错误");
      }
    }

  },
  created() {
    this.openSocket();
  },
  destroyed() {
    if(this.socket!=null){
      this.socket.close();
      this.socket=null;
    }
  }
}
</script>

<style scoped>
.select-item {
  display: flex;
  justify-content: space-between;
  color: #FFFFFF;
  padding: 0 30px;
  margin: 10px 20px 0 20px;
  background: rgba(255,0,0,0.1);
  height: 60px;
  box-shadow: -2px 0px 10px rgba(255,0,0,0.7) inset, 2px 0px 10px rgba(255,0,0,0.7) inset !important;
}
</style>