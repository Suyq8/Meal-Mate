'use client'
import React, { useEffect, useState } from 'react'
import { Input, Selection, useDisclosure } from "@nextui-org/react";
import CustomSelect from '@/components/CustomSelect';
import { useCategoryService } from '@/services/useCategoryService';
import { useDishService } from '@/services/useDishService';
import DishModal from '@/components/DishModal';

function Page() {
	const styleInput: Record<string, string | string[]> = {
		input: "text-[#e0e0e0]",
		inputWrapper: [
			"group-data-[focus=true]:border-[#6870fa] appearance-none group-data-[focus=true]:outline-none group-data-[focus=true]:ring-0",
			"border-1 rounded-md min-h-[2rem] h-8"
		],
	};

	const [categories, setCategories] = useState<string[]>([]);
	const saleStatus: string[] = ['Not Selling', 'On Sale'];
	const [name, setName] = useState<string>('');
	const [category, setCategory] = useState<Selection>(new Set<string>());
	const [status, setStatus] = useState<Selection>(new Set<string>());
	const [pageNumber, setPageNumber] = useState<number>(1);
	const statusMap: Record<number, string> = { 1: 'On Sale', 0: 'Not Selling' };
	const [selectedRows, setSelectedRows] = useState<Set<number>>(new Set<number>());
	const [categoryMap, setCategoryMap] = useState<Record<string, any>>({});
	const [dishes, setDishes] = useState<{ total: number, records: Record<string, any>[] }>({ total: 0, records: [] });
	const [id, setId] = useState<string | number>('');
	const categoryService = useCategoryService();
	const dishService = useDishService();
	const { isOpen, onOpen, onOpenChange } = useDisclosure();

	const header: string[] = ['Name', 'Image', 'Category', 'Price', 'Status', 'Update Time', 'Action'];
	const headerToData: Record<string, string> = {
		'Name': 'name',
		'Image': 'image',
		'Category': 'categoryName',
		'Price': 'price',
		'Status': 'status',
		'Update Time': 'updateTime',
		'Action': 'action',
	};

	// Handle individual row selection
	const handleRowSelectionChange = (dishId: number) => {
		const newSelection = new Set(selectedRows);
		if (newSelection.has(dishId)) {
			newSelection.delete(dishId);
		} else {
			newSelection.add(dishId);
		}
		setSelectedRows(newSelection);
	};

	// Handle select/deselect all
	const handleSelectDeselectAll = (e: React.ChangeEvent<HTMLInputElement>) => {
		if (e.target.checked) {
			const allMealIds = dishes.records.map(meal => meal.id);
			setSelectedRows(new Set(allMealIds));
		} else {
			setSelectedRows(new Set());
		}
	};

	const fetchCategories = async () => {
		try {
			const resp = await categoryService.getAll({ type: 1 });

			if (resp.code === 1) {
				const tmp = resp.data.reduce((map: Record<string, any>, item: any) => {
					map[item.name] = item.id;
					return map;
				}, {});
				setCategoryMap(tmp);
				setCategories(resp.data.map((item: any) => item.name));
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const fetchDishes = async () => {
		try {
			var params: { page: number; pageSize: number; name: string; categoryId?: number, status?: number } = { page: pageNumber, pageSize: 10, name: name };
			if (category !== "all" && category.size > 0) {
				params.categoryId = categoryMap[Array.from(category)[0]];
			}
			if (status !== "all" && status.size > 0) {
				params.status = Array.from(status)[0] === 'On Sale' ? 1 : 0;
			}
			const resp = await dishService.getList(params);

			if (resp.code === 1) {
				setDishes(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const deleteDish = async (ids: number | string | number[]) => {
		try {
			var params;
			if (Array.isArray(ids)) {
				params = ids.join(",");
			} else {
				params = ids.toString();
			}

			const resp = await dishService.delete(params);
			if (resp.code === 1) {
				fetchDishes();
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	}

	const updateStatus = async (id: number, status: number) => {
		try {
			const resp = await dishService.updateStatus({ id, status });
			if (resp.code === 1) {
				fetchDishes();
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	}

	useEffect(() => {
		fetchCategories();
	}, []);

	useEffect(() => {
		fetchDishes();
	}, [pageNumber]);

	return (
		<div className='bg-[#141A28] text-[#e0e0e0] w-full h-full overflow-auto custom-scrollbar'>
			<div className='min-w-fit'>
				<div className='bg-[#1b2537] m-7 p-7 rounded-md '>
					<div className='flex flex-row justify-between'>
						<div className='h-8 flex flex-row gap-3 items-center'>
							<div className='flex flex-row gap-2 items-center'>
								<p>Name:</p>
								<Input
									id="name"
									aria-label='name'
									type="text"
									variant='bordered'
									className='w-36'
									classNames={styleInput}
									value={name}
									onChange={(e) => setName(e.target.value)}
								/>
							</div>
							<div className='flex flex-row gap-2 items-center'>
								<p>Category:</p>
								<CustomSelect value={category} setValue={setCategory} items={categories} label="category" className='h-8 w-36' />
							</div>
							<div className='flex flex-row gap-2 items-center'>
								<p>Sale Status:</p>
								<CustomSelect value={status} setValue={setStatus} items={saleStatus} label="sale-status" className='h-8 w-36' />
							</div>
							<button onClick={fetchDishes}
								className='bg-[#66a2df] px-3 h-full flex flex-row rounded-md items-center hover:bg-[#5791cb]'>
								Search
							</button>
						</div>
						<div className='flex flex-row gap-3 items-center'>
							<button onClick={() => deleteDish(Array.from(selectedRows))}
								className='text-red-400 text-sm'>Batch Deletion</button>
							<button onClick={() => { setId(''); onOpen(); }}
								className='bg-[#66a2df] px-3 h-full flex flex-row rounded-md items-center hover:bg-[#5791cb]'>
								Add Dish
							</button>
						</div>
					</div>

					<div className='mt-4'>
						{dishes.total === 0 ? (<div className='flex flex-row justify-center items-center h-52'>
							<p className='text-gray-400'>No dish</p>
						</div>) : (
							<div className="relative overflow-x-auto sm:rounded-lg">
								<table className="w-full text-sm text-left rtl:text-right">
									<thead className="text-xs text-gray-300 uppercase bg-[#141A28]">
										<tr>
											<th className='text-center align-middle'>
												<input
													type="checkbox"
													onChange={handleSelectDeselectAll}
													checked={selectedRows.size === dishes.records.length}
													className='accent-[#66a2df]'
												/>
											</th>
											{header.map(h => (
												<th scope="col" className="px-3 py-3 text-center align-middle" key={h}>
													{h}
												</th>
											))}
										</tr>
									</thead>
									<tbody>
										{dishes.records.map((dish, index) => (
											<tr className="border-b" key={index}>
												<td className='text-center align-middle p-2'>
													<input
														type="checkbox"
														checked={selectedRows.has(dish.id)}
														onChange={() => handleRowSelectionChange(dish.id)}
														className='accent-[#66a2df]'
													/>
												</td>
												{header.map((h, idx) => (h !== 'Action' &&
													(h === 'Image' ? (
														<td scope="row" className="px-3 py-4 font-medium text-center align-middle flex justify-center items-center" key={h + idx.toString()}>
															<img src={dish[headerToData[h]]} className='w-10 h-10' alt={h} />
														</td>
													) : <td scope="row" className="px-3 py-4 font-medium text-center align-middle" key={h + idx.toString()}>
														{h === 'Status' ? (
															<div className='flex flex-row items-center gap-2 justify-center'>
																<div className={`h-2 w-2 rounded-full ${dish.status === 1 ? 'bg-[#c4daaf]' : 'bg-red-400'}`} />
																{statusMap[dish[headerToData[h]]]}
															</div>
														) : (
															<div>
																{dish[headerToData[h]]}
															</div>
														)}
													</td>)
												))}
												<td scope="row" className="px-3 py-4 text-center align-middle">
													<div className='flex gap-3 justify-center'>
														<button onClick={() => { setId(dish.id); onOpen(); }}
															className="font-medium text-[#66a2df] hover:underline">Edit</button>
														<button onClick={() => deleteDish(dish.id)}
															className="font-medium text-red-400 hover:underline">Delete</button>
														{dish.status === 0 ? <button onClick={() => updateStatus(dish.id, 1)}
															className="font-medium text-[#66a2df] hover:underline">Start</button> :
															<button onClick={() => updateStatus(dish.id, 0)}
																className="font-medium text-red-400 hover:underline">Stop</button>}
													</div>
												</td>
											</tr>
										))}
									</tbody>
								</table>

								<nav className="flex items-center flex-column flex-wrap md:flex-row justify-between pt-4" aria-label="Table navigation">
									<span className="text-sm font-normal text-gray-400 mb-4 md:mb-0 block w-full md:inline md:w-auto">Showing <span className="font-semibold text-[#e0e0e0]">{(pageNumber - 1) * 10 + 1}-{Math.min(pageNumber * 10, dishes.total)}</span> of <span className="font-semibold text-[#e0e0e0]">{dishes.total}</span></span>
									<ul className="inline-flex -space-x-px rtl:space-x-reverse text-sm h-8 bg-[#1b2537] text-[#e0e0e0]">
										<li>
											<button
												onClick={() => setPageNumber(Math.max(1, pageNumber - 1))}
												className="flex items-center justify-center px-3 h-8 ms-0 leading-tight border border-[#e0e0e0] rounded-s-lg hover:bg-gray-100 hover:text-gray-700">Previous</button>
										</li>
										{Array.from({ length: Math.ceil(dishes.total / 10) }, (_, index) => (
											<li key={index}>
												<button
													onClick={() => setPageNumber(index + 1)}
													className={`flex items-center justify-center px-3 h-8 leading-tight border border-[#e0e0e0] hover:text-gray-700 ${pageNumber === index + 1 ? 'bg-[#66a2df] hover:bg-[#5791cb]' : 'hover:bg-gray-100'}`}>
													{index + 1}
												</button>
											</li>
										))}
										<li>
											<button
												onClick={() => setPageNumber(Math.min(Math.ceil(dishes.total / 10), pageNumber + 1))}
												className="flex items-center justify-center px-3 h-8 leading-tight border border-[#e0e0e0] rounded-e-lg hover:bg-gray-100 hover:text-gray-700">Next</button>
										</li>
									</ul>
								</nav>
							</div>
						)}
					</div>
				</div>

				<DishModal isOpen={isOpen} onOpen={onOpen} onOpenChange={onOpenChange} refresh={fetchDishes} id={id} />
			</div>
		</div>
	)
}

export default Page;