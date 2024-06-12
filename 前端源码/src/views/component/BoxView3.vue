<template>
  <div>
    <div style="height: calc(100% - 20px); width: calc(100% - 20px); margin: 0 auto; position: relative;">
      <box-border style="height: 100%;"/>
      <div style="position:absolute; top: 0; width: 100%; height: 100%;">
        <div class="box-bg" style="text-align: center; font-size: 20px; color: #FFFFFF; line-height: 35px;">
          港股通资金流
        </div>
        <div style=" height: calc(100% - 65px); overflow:auto;">
          <div id="main-view" style="height: calc(100% - 40px); width: 60%; margin: 0 auto;"></div>
          <div style="width: calc(60% - 80px); left: 15px; margin: 0 auto; height: 40px; position: relative; display: flex; justify-content: space-between;">
            <div class="box-text-bg" style="line-height: 35px; color: #409EFF; position: absolute; left: -160px; width: 140px; text-align: center; ">资金净流入</div>
            <div :class="{'red-box-text-bg': item.type === -1, 'blue-box-text-bg': item.type === 1}" class="box-text-bg" v-for="(item, index) of data" :key="index" style="width: 24%;">
              <div style="text-align: center; line-height: 35px;">{{item.value}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import BoxBorder from "@/views/component/BoxBorder";
import Count from "@/api/Count";
export default {
  name: "BoxView3",
  components: {BoxBorder},
  data() {
    return {
      data: [],
      lock: false,
      beforeMouse: {},
      mouse: {},
    }
  },
  methods: {
    onLoad(dataDict) {
      // 基于准备好的dom，初始化echarts实例
      let myChart = echarts.init(document.getElementById('main-view'));

      let xData = dataDict.map(item => item.name);

      let data = dataDict.map(item => item.money);
      console.log(data);

      let option = {
        // backgroundColor: "#141f56",

        tooltip: {
          show: "true",
          trigger: 'item',
          backgroundColor: 'rgba(0,0,0,0.4)', // 背景
          padding: [8, 10], //内边距
          // extraCssText: 'box-shadow: 0 0 3px rgba(255, 255, 255, 0.4);', //添加阴影

        },
        grid: {
          borderWidth: 0,
          top: 20,
          bottom: 35,
          left:55,
          right:30,
          textStyle: {
            color: "#fff"
          }
        },
        xAxis: [{
          type: 'category',

          axisTick: {
            show: false
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: '#363e83',
            }
          },
          axisLabel: {
            inside: false,
            textStyle: {
              color: '#bac0c0',
              fontWeight: 'normal',
              fontSize: '12',
            },
          },
          data: xData,
        }, {
          type: 'category',
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            show: false
          },
          splitArea: {
            show: false
          },
          splitLine: {
            show: false
          },
          data: xData,
        }],
        yAxis: {
          type: 'value',
          axisTick: {
            show: false
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: '#32346c',
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#32346c ',
            }
          },
          axisLabel: {
            textStyle: {
              color: '#bac0c0',
              fontWeight: 'normal',
              fontSize: '12',
            },
            formatter: '{value}',
          },
        },
        series: [{
          type: 'bar',
          itemStyle: {
            normal: {
              show: true,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: '#00c0e9'
              }, {
                offset: 1,
                color: '#3b73cf'
              }]),
              barBorderRadius: 50,
              borderWidth: 0,
            },
            emphasis: {
              shadowBlur: 15,
              shadowColor: 'rgba(105,123, 214, 0.7)'
            }
          },
          zlevel: 2,
          barWidth: '20%',
          data: data,
        },
          {
            name: '',
            type: 'bar',
            xAxisIndex: 1,
            zlevel: 1,
            itemStyle: {
              normal: {
                color: '#121847',
                borderWidth: 0,
                shadowBlur: {
                  shadowColor: 'rgba(255,255,255,0.31)',
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowOffsetY: 2,
                },
              }
            },
            barWidth: '20%',
            data: [30, 30, 30, 30, 30]
          }
        ]
      }


      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
      window.addEventListener("resize",function(){
        myChart.resize();
      });
    },
    loadOption() {
      Count.count2("港股通资金流").then(res => {
        let data = res.data;
        data = data.map(item => {
          return {
            name: item.name === '沪港通' || item.name === '深港通' ? item.name2 : item.name,
            value: item.name === '沪港通' || item.name === '深港通' ? item.value2 : item.value,
          };
        })
        data = data.map(item => {
          let money = 0;
          if (item.value.indexOf("万元") !== -1) {
            money = parseFloat(item.value.replace("-", "").replace("万元", "") + "00000")
          }
          if (item.value.indexOf("亿元") !== -1) {
            money = parseFloat(item.value.replace("-", "").replace("亿元", "") + "000000000")
          }
          return {
            ...item,
            money: money,
            type: item.value.indexOf("-") === -1 ? 1 : -1,
          }
        })
        this.data = data;
        this.onLoad(data);
      })
    }
  },
  mounted() {
    document.addEventListener('mousemove', (event) => {
      this.mouse.x = event.clientX;
      this.mouse.y = event.clientY;
    });
    setInterval(() => {
      this.lock = this.beforeMouse.x === this.mouse.x && this.beforeMouse.y === this.mouse.y;
      this.beforeMouse.x = this.mouse.x;
      this.beforeMouse.y = this.mouse.y;
    }, 400);
    setInterval(() => {
      if (!this.lock) {
        return;
      }
      this.loadOption();
    }, 3000)

    this.loadOption();
  },

}
</script>

<style scoped>
.select-item {
  display: flex;
  justify-content: space-between;
  color: #FFFFFF;
  padding: 0 30px;
  margin: 10px 20px 0 20px;
}
.red-box-text-bg {
  box-shadow: -2px 0px 10px rgba(255,0,0,0.7) inset, 2px 0px 10px rgba(255,0,0,0.7) inset !important;
  color: #F56C6C !important;
}
.blue-box-text-bg {
  color: #409EFF;
}
</style>