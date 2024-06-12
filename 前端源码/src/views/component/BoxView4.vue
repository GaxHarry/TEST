<template>
  <div>
    <div style="height: calc(100% - 20px); width: calc(100% - 20px); margin: 0 auto; position: relative;">
      <box-border style="height: 100%;"/>
      <div style="position:absolute; top: 0; width: 100%; height: 100%;">
        <div class="box-bg" style="text-align: center; font-size: 20px; color: #FFFFFF; line-height: 35px;">
          今日跌涨幅
        </div>
        <div style=" height: calc(100% - 65px); overflow:auto;">
          <div>
            <div class="box-text-bg select-item" style="margin: 10px 20px;">
              <div style="height: 40px; line-height: 40px; padding-left: 20px;">板块名称</div>
              <div style="height: 40px; line-height: 40px; padding-right: 20px;">今日涨跌幅</div>
            </div>
            <div class="box-text-bg" style="height: unset; margin: 0 20px;">
              <div class="select-item" v-for="(item, index) in data" :key="index" >
                <div style="height: 40px; line-height: 40px;">{{item.name}}</div>
                <div style="height: 40px; line-height: 40px;">
                  {{item.value}}
                  <i v-if="item.type === 1" class="el-icon-top" style="color: #F56C6C"/>
                  <i v-if="item.type === -1" class="el-icon-bottom" style="color: #67C23A"/>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BoxBorder from "@/views/component/BoxBorder";
import Count from "@/api/Count";
export default {
  name: "BoxView1",
  components: {BoxBorder},
  data() {
    return {
      data: [
      ],
      lock: false,
      beforeMouse: {},
      mouse: {},
    }
  },
  methods: {
    onLoad() {
      Count.count2("板块资金流今日跌涨幅").then(res => {
        let data = res.data.map(item => ({
          ...item,
          otherValue: parseFloat(item.value.replace("%", '').replace('-', '')),
          type: item.value.indexOf("-") === -1 ? 1 : -1,
        }));
        for(let i = 0; i<data.length; i++) {
          for(let j = i + 1; j<data.length; j++) {
            if (data[i].otherValue < data[j].otherValue) {
              let t = data[i];
              data[i] = data[j];
              data[j] = t;
            }
          }
        }

        let top = 0;
        let bottom = 0;
        let dataCopy = [];
        for(let item of data) {
          if (item.type === 1 && top < 3) {
            dataCopy.push(item);
            top++;
          }
          if (item.type === -1 && bottom < 3) {
            dataCopy.push(item);
            bottom++;
          }
        }
        console.log(dataCopy.length, top, bottom);
        this.data = dataCopy;
      })
    }
  },
  mounted() {
    document.addEventListener('mousemove', (event) => {
      this.mouse.x = event.clientX;
      this.mouse.y = event.clientY;
    });
  },
  created() {
    this.onLoad();

    setInterval(() => {
      this.lock = this.beforeMouse.x === this.mouse.x && this.beforeMouse.y === this.mouse.y;
      this.beforeMouse.x = this.mouse.x;
      this.beforeMouse.y = this.mouse.y;
    }, 400);
    setInterval(() => {
      if (!this.lock) {
        return;
      }
      this.onLoad();
    }, 3000)
  }
}
</script>

<style scoped>
.select-item {
  display: flex;
  justify-content: space-between;
  color: #FFFFFF;
  padding: 0 30px;
  margin: 0 20px 0 20px;
}
</style>