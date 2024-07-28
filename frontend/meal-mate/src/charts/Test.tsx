import * as echarts from "echarts";
import { useEffect } from "react";

const Test = () => {
  useEffect(() => {
    const container = document.getElementById("echarts");

    if (!container) return;
    const myChart = echarts.init(container);
    myChart.setOption({
      title: {
        text: "ECharts 入门示例",
      },
      tooltip: {},
      xAxis: {
        data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"],
      },
      yAxis: {},
      series: [
        {
          name: "销量",
          type: "bar",
          data: [5, 20, 36, 10, 10, 20],
        },
      ],
    });
  }, []);

  return <div id="echarts" style={{ width: 300, height: 400 }} />;
};

export default Test;