'use client'
import { format, subDays } from 'date-fns';
import React, { useEffect, useState } from 'react';
import { ArrowUpOnSquareIcon } from '@heroicons/react/24/outline';
import Turnover from '@/charts/TurnoverChart';
import UserChart from '@/charts/UserChart';
import OrderChart from '@/charts/OrderChart';
import Top10Chart from '@/charts/Top10Chart';
import { useReportService } from '@/services/useReportService';

type TimeOption = 'Yesterday' | 'Last 7 days' | 'Last 30 days';

function Page() {
	const time: TimeOption[] = ['Yesterday', 'Last 7 days', 'Last 30 days'];
	const daysToSubtract: Record<TimeOption, number> = {
		'Yesterday': 0,
		'Last 7 days': 6,
		'Last 30 days': 29,
	};
	const [selectedTime, setSelectedTime] = useState<TimeOption>('Yesterday');
	const endDate = format(subDays(new Date(), 1), 'MMM/dd/yyyy');
	const [startDate, setStartDate] = useState<string>(endDate);
	const reportService = useReportService();
	const [turnover, setTurnover] = useState<Record<string, any>>({});
	const [user, setUser] = useState<Record<string, any>>({});
	const [order, setOrder] = useState<Record<string, any>>({});
	const [top10, setTop10] = useState<Record<string, any>>({});

	const fetchTurnover = async () => {
		try {
			const resp = await reportService.getTurnoverStatistics({ begin: format(startDate, 'yyyy-MM-dd'), end: format(endDate, 'yyyy-MM-dd') });
			if (resp.code === 1) {
				setTurnover(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const fetchUser = async () => {
		try {
			const resp = await reportService.getUserStatistics({ begin: format(startDate, 'yyyy-MM-dd'), end: format(endDate, 'yyyy-MM-dd') });
			if (resp.code === 1) {
				setUser(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const fetchOrder = async () => {
		try {
			const resp = await reportService.getOrderStatistics({ begin: format(startDate, 'yyyy-MM-dd'), end: format(endDate, 'yyyy-MM-dd') });
			if (resp.code === 1) {
				setOrder(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const fetchTop10 = async () => {
		try {
			const resp = await reportService.getTop10({ begin: format(startDate, 'yyyy-MM-dd'), end: format(endDate, 'yyyy-MM-dd') });
			if (resp.code === 1) {
				setTop10(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const exportInfo = async () => {
		try {
			const resp = await reportService.exportData();
			// Create a file handle
			const fileHandle = await (window as any).showSaveFilePicker({
				suggestedName: 'exported_data.xlsx',
				types: [
					{
						description: 'Excel Files',
						accept: { 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': ['.xlsx'] },
					},
				],
			});

			// Create a writable stream
			const writableStream = await fileHandle.createWritable();

			// Write the Blob to the file
			await writableStream.write(resp);

			// Close the file and write the contents to disk
			await writableStream.close();
		} catch (error: any) {
			console.error(error);
		}
	}

	useEffect(() => {
		setStartDate(format(subDays(endDate, daysToSubtract[selectedTime]), 'MMM/dd/yyyy'));
	}, [selectedTime]);

	useEffect(() => {
		fetchOrder();
		fetchTurnover();
		fetchUser();
		fetchTop10();
	}, [startDate]);

	return (
		<div className='bg-[#141A28] text-[#e0e0e0] w-full h-full overflow-auto custom-scrollbar'>
			<div className='min-w-fit'>
				<div className='flex justify-between items-center m-7'>
					<div className='flex flex-row items-end'>
						{time.map((t, index) => (
							<button
								key={index}
								className={`border-1 py-0.5 px-3 h-[60px] xl:h-[30px] ${selectedTime === t && 'bg-[#66a2df]'
									} ${index === 0 && 'rounded-l-sm'} ${index === 2 && 'rounded-r-sm'
									}`}
								onClick={() => setSelectedTime(t)}
							>
								{t}
							</button>
						))}
						<p className='text-sm text-gray-400 ml-3'>
							Selected Time: {startDate} to {endDate}
						</p>
					</div>
					<button onClick={exportInfo}
						className='bg-[#66a2df] px-3 py-1 flex flex-row rounded-md items-center'>
						<ArrowUpOnSquareIcon className='h-4 w-4 mr-1' />
						Export
					</button>
				</div>

				{/*chart*/}
				<div className='grid grid-cols-2 m-7 gap-6 min-w-[700px]'>
					<div>
						<p className='mb-2 font-bold'>Turnover</p>
						<Turnover dateList={turnover.dateList?.split(',')}
							turnoverList={turnover.turnoverList?.split(',')} />
					</div>

					<div>
						<p className='mb-2 font-bold'>User</p>
						<UserChart dateList={user.dateList?.split(',')}
							totalUserList={user.totalUserList?.split(',')}
							newUserList={user.newUserList?.split(',')} />
					</div>

					<div>
						<p className='mb-2 font-bold'>Order</p>
						<OrderChart dateList={order.dateList?.split(',')}
							orderCountList={order.orderCountList?.split(',')}
							validOrderCountList={order.validOrderCountList?.split(',')} />
					</div>

					<div>
						<p className='mb-2 font-bold'>Top 10</p>
						<Top10Chart nameList={top10.nameList?.split(',').reverse()}
							numberList={top10.numberList?.split(',').reverse()} />
					</div>
				</div>

			</div>
		</div>
	);
}

export default Page;