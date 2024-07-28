import React, { useEffect } from 'react';
import * as echarts from 'echarts';

interface TurnoverChartProps {
	dateList: string[];
	turnoverList: string[];
}

function TurnoverChart({ dateList, turnoverList }: TurnoverChartProps) {
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
		legend: {
			data: ['Turnover'],
			textStyle: {
				color: '#e0e0e0',
				fontSize: 12,
			},
		},
		grid: {
			top: '7%',
			left: '7%',
			right: '7%',
			bottom: '5%',
			containLabel: true,
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			axisLabel: {
				color: '#e0e0e0',
				fontSize: 12,
			},
			axisLine: {
				lineStyle: {
					color: '#e0e0e0',
					width: 1,
				},
			},
			data: dateList,
		},
		yAxis: [
			{
				type: 'value',
				min: 0,
				axisLabel: {
					color: '#e0e0e0',
					fontSize: 12,
				},
			},
		],
		series: [
			{
				name: 'Turnover',
				type: 'line',
				smooth: false,
				showSymbol: false,
				data: turnoverList,
			},
		],
	};

	useEffect(() => {
		const container = document.getElementById('turnover');

		if (!container) return;
		const myChart = echarts.init(container);
		myChart.setOption(option);
	}, [dateList]);

	return (
		<div id="turnover" className="bg-[#1b2537] p-5 rounded" style={{ height: '400px' }} />
	);
}

export default TurnoverChart;