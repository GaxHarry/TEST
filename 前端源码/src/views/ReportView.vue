<template>
  <div style="height: 100%; background: rgb(2, 28, 87)">
    <user-top style="height: 50px;"/>
    <div style="height: calc(100% - 40px); display: flex;">
      <div style="width: calc(100% - 400px); height: 100%;">
        <div style="height: 50%; display: flex;">
          <div style="width: 50%;">
            <box-view1 style="height: 100%"/>
          </div>
          <div style="width: 50%;">
            <box-view2 style="height: 100%"/>
          </div>
        </div>
        <div style="height: 50%; display: flex;">
          <div style="width: 65%;">
            <box-view3 style="height: 100%"/>
          </div>
          <div style="width: 35%;">
            <box-view4 style="height: 100%"/>
          </div>
        </div>
      </div>
      <div style="width: 400px; height: 100%;">
        <box-view5 style="height: 100%"/>
      </div>
    </div>
  </div>
</template>

<script>
import UserTop from "@/views/component/UserTop";
import BoxView1 from "@/views/component/BoxView1";
import BoxView2 from "@/views/component/BoxView2";
import BoxView3 from "@/views/component/BoxView3";
import BoxView4 from "@/views/component/BoxView4";
import BoxView5 from "@/views/component/BoxView5";
export default {
  name: "ReportView",
  components: {BoxView1, BoxView2, BoxView3, BoxView4, BoxView5, UserTop},
}
</script>

<style scoped>

</style>