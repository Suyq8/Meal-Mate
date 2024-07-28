import React, { useEffect } from 'react';
import * as echarts from 'echarts';

interface Top10ChartProps {
	nameList: string[];
	numberList: string[];
}

const Top10Chart: React.FC<Top10ChartProps> = ({ nameList, numberList }) => {
	const option: echarts.EChartsOption = {
		tooltip: {
			trigger: 'axis',
			backgroundColor: '#1b2537',
			borderRadius: 2,
			textStyle: {
				color: '#e0e0e0',
				fontSize: 12,
				fontWeight: 300,
			},
		},
		grid: {
			top: '-10px',
			left: '5%',
			right: '0',
			bottom: '0',
			containLabel: true,
		},
		xAxis: {
			show: false,
		},
		yAxis: {
			axisLine: {
				show: false,
			},
			axisTick: {
				show: false,
				alignWithLabel: true,
			},
			type: 'category',
			axisLabel: {
				color: '#e0e0e0',
				fontSize: 12,
			},
			data: nameList,
		},
		series: [
			{
				data: numberList,
				type: 'bar',
				showBackground: true,
				backgroundStyle: {
					color: '#1b2537',
				},
				barWidth: 20,
				barGap: '80%',
				barCategoryGap: '80%',
				itemStyle: {
					borderRadius: [0, 10, 10, 0],
				},
				label: {
					show: true,
					formatter: '{@score}',
					color: '#e0e0e0',
					position: ['8', '5'],
				},
			},
		],
	};

	useEffect(() => {
		const container = document.getElementById('top10');

		if (!container) return;
		const myChart = echarts.init(container);
		myChart.setOption(option);
	}, [numberList]);

	return (
		<div id="top10" className="bg-[#1b2537] p-5 rounded" style={{ height: '400px' }} />
	);
};

export default Top10Chart;