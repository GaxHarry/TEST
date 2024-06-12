<template>
  <div>
    <div style="height: calc(100% - 20px); width: calc(100% - 20px); margin: 0 auto; position: relative;">
      <box-border style="height: 100%;"/>
      <div style="position:absolute; top: 0; width: 100%; height: 100%;">
        <div class="box-bg" style="text-align: center; font-size: 20px; color: #FFFFFF; line-height: 35px;">
          新股上市日历
        </div>
        <div style=" height: calc(100% - 65px); overflow:auto; display: flex; justify-content: space-between; flex-flow: column;">
          <div>
            <div class="box-text-bg select-item" style="margin: 10px 20px;">
              <div style="height: 40px; line-height: 40px; padding-left: 20px;">证券简称</div>
              <div style="height: 40px; line-height: 40px; padding-right: 20px;">上市日期</div>
            </div>
            <div class="box-text-bg" style="height: unset; margin: 0 20px;">
              <div class="select-item" v-for="(item, index) in data" :key="index" >
                <div style="height: 40px; line-height: 40px;">{{item.name}}</div>
                <div style="height: 40px; line-height: 40px;">{{item.value}}</div>
              </div>
            </div>
          </div>
          <div style="display: flex; justify-content: end; padding-right: 10px;">
            <el-pagination
                @current-change="onLoad"
                background
                :current-page.sync="page.current"
                :page-size="page.size"
                layout="prev, pager, next"
                :total="page.total">
            </el-pagination>
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
      data: [],
      page: {
        current: 1,
        size: 5,
      },
      lock: false,
      beforeMouse: {},
      mouse: {},
    }
  },
  methods: {
    onLoad() {
      Count.count2("新股上市日历").then(res => {
        let data = res.data;
        this.page.total = data.length;
        for(let i = 0; i<data.length; i++) {
          for(let j = i + 1; j<data.lenth; j++) {
            let t = data[i];
            let tData = new Date(t.value);

            let o = data[j];
            let oData = new Date(o.value);

            if (tData.getTime() < oData.getTime()) {
              data[i] = o;
              data[j] = t;
            }
          }
        }
        let start = (this.page.current - 1) * this.page.size;
        let end = start + this.page.size;
        this.data = data.slice(start, end);
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

      this.page.current += 1;
      if ((this.page.current - 1) * this.page.size > this.page.total) {
        this.page.current = 1;
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