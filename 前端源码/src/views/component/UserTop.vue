<template>
  <div>
    <div style="height: calc(100% - 10px)">
      <div id="title" style="background-image: url('/img/bg01.png');">
        <div id="text-title">资金交收风险提示</div>
      </div>
      <div id="text-time">{{ time }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserTop",
  data() {
    return {
      time: ""
    }
  },
  methods: {
    setTime() {
      let date = new Date();
      let month = date.getMonth() + 1;
      let days = date.getDate();
      let hours = date.getHours();
      let min = date.getMinutes();
      let second = date.getSeconds();
      this.time = `${this.formatTime(month)}-${this.formatTime(days)} ${this.formatTime(hours)}:${this.formatTime(min)}:${this.formatTime(second)}`
    },
    formatTime(value) {
      return value < 10 ? '0' + value: value;
    }
  },
  created() {
    this.setTime();
    setInterval(() => {
      this.setTime();
    }, 1000)
  }
}
</script>

<style scoped>
  #title {
    background-size: 100% 100%;
    background-repeat: no-repeat;
    width: 100%;
    height: 100%;
    text-align: center;
    color: #FFFFFF;
    font-size: 22px;
    line-height: 35px;
  }
  #text-title {
  }
  #text-time {
    position: relative;
    top: -35px;
    font-size: 30px;
    padding: .05rem 0;
    color: #FFFFFF;
    font-family: electronicFont;
    font-weight: bold;
  }
</style>