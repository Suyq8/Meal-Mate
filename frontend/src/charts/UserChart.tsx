import React, { useEffect } from 'react';
import * as echarts from 'echarts';

interface UserChartProps {
	dateList: string[];
	totalUserList: string[];
	newUserList: string[];
}

const UserChart: React.FC<UserChartProps> = ({ dateList, totalUserList, newUserList }) => {
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
			data: ['Total User', 'New User'],
			textStyle: {
				color: '#e0e0e0',
				fontSize: '12px',
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
				fontSize: '12px',
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
					fontSize: '12px',
				},
			},
		],
		series: [
			{
				name: 'Total User',
				type: 'line',
				smooth: false,
				showSymbol: false,
				data: totalUserList,
			},
			{
				name: 'New User',
				type: 'line',
				smooth: false,
				showSymbol: false,
				data: newUserList,
			},
		],
	};

	useEffect(() => {
		const container = document.getElementById('user');

		if (!container) return;
		const myChart = echarts.init(container);
		myChart.setOption(option);
	}, [dateList]);

	return <div id="user" className="bg-[#1b2537] p-5 rounded" style={{ height: '400px' }} />;
};

export default UserChart;