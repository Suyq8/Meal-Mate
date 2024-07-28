'use client'
import React, { useEffect, useState } from 'react'
import { Input, Selection, useDisclosure } from "@nextui-org/react";
import CustomSelect from '@/components/CustomSelect';
import { useCategoryService } from '@/services/useCategoryService';
import CategoryModal from '@/components/CategoryModal';

function Page() {
	const styleInput: Record<string, string | string[]> = {
		input: "text-[#e0e0e0]",
		inputWrapper: [
			"group-data-[focus=true]:border-[#6870fa] appearance-none group-data-[focus=true]:outline-none group-data-[focus=true]:ring-0",
			"border-1 rounded-md min-h-[2rem] h-8"
		],
	};

	const categoryType: string[] = ['Dish', 'Meal'];
	const [type, setType] = useState<Selection>(new Set<string>());
	const [pageNumber, setPageNumber] = useState<number>(1);
	const [name, setName] = useState<string>('');
	const { isOpen, onOpen, onOpenChange } = useDisclosure();
	const [categories, setCategories] = useState<{ total: number, records: Record<string, any>[] }>({ total: 0, records: [] });
	const [data, setData] = useState<Record<string, any>>({});
	const header: string[] = ['Name', 'Type', 'Rank', 'Status', 'Update Time', 'Action'];
	const headerToData: Record<string, string> = {
		'Name': 'name',
		'Type': 'type',
		'Rank': 'ranking',
		'Status': 'status',
		'Update Time': 'updateTime'
	};
	const statusMap: Record<number, string> = { 1: 'Enabled', 0: 'Disabled' };
	const categoryMap: Record<string, number> = { 'Dish': 1, 'Meal': 2 };
	const categoryService = useCategoryService();

	const fetchCategories = async () => {
		try {
			var params: { page: number; pageSize: number; name: string; type?: number } = { page: pageNumber, pageSize: 10, name: name };
			if (type !== "all" && type.size > 0) {
				params.type = categoryMap[Array.from(type)[0]];
			}
			const resp = await categoryService.getList(params);

			if (resp.code === 1) {
				setCategories(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const updateStatus = async (id: number, status: number) => {
		try {
			const resp = await categoryService.updateStatus({ id, status });
			if (resp.code === 1) {
				fetchCategories();
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const deleteCategory = async (id: number) => {
		try {
			const resp = await categoryService.delete(id);
			if (resp.code === 1) {
				fetchCategories();
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	useEffect(() => {
		fetchCategories();
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
								<p>Type:</p>
								<CustomSelect value={type} setValue={setType} items={categoryType} label="type" className='h-8 w-36' />

							</div>
							<button onClick={fetchCategories}
								className='bg-[#66a2df] px-3 h-full flex flex-row rounded-md items-center hover:bg-[#5791cb]'>
								Search
							</button>
						</div>
						<button onClick={onOpen}
							className='bg-[#66a2df] px-3 h-8 flex flex-row rounded-md items-center hover:bg-[#5791cb]'>
							Add Category
						</button>
					</div>

					<div className='mt-4'>
						{categories.total === 0 ? (<div className='flex flex-row justify-center items-center h-52'>
							<p className='text-gray-400'>No category</p>
						</div>) : (
							<div className="relative overflow-x-auto sm:rounded-lg">
								<table className="w-full text-sm text-left rtl:text-right">
									<thead className="text-xs text-gray-300 uppercase bg-[#141A28]">
										<tr>
											{header.map(h => (
												<th scope="col" className="px-3 py-3 text-center align-middle" key={h}>
													{h}
												</th>
											))}
										</tr>
									</thead>
									<tbody>
										{categories.records.map((c, index) => (
											<tr className="border-b" key={index}>
												{header.map((h, idx) => (h != 'Action' &&
													(h == 'Type' ? (
														<td scope="row" className="px-3 py-4 font-medium text-center align-middle flex justify-center items-center" key={h + idx.toString()}>
															{categoryType[c[headerToData[h]] - 1]}
														</td>
													) : <td scope="row" className="px-3 py-4 font-medium text-center align-middle" key={h + idx.toString()}>
														{h == 'Status' ? (
															<div className='flex flex-row items-center gap-2 justify-center'>
																<div className={`h-2 w-2 rounded-full ${c.status === 1 ? 'bg-[#c4daaf]' : 'bg-red-400'}`} />
																{statusMap[c[headerToData[h]]]}
															</div>
														) : (
															<div>
																{c[headerToData[h]]}
															</div>
														)}
													</td>)
												))}
												<td scope="row" className="px-3 py-4 text-center align-middle">
													<div className='flex gap-3 justify-center'>
														<button onClick={() => { setData({ name: c.name, ranking: c.ranking, type: c.type, id: c.id }); onOpen(); }}
															className="font-medium text-[#66a2df] hover:underline">Edit</button>
														<button onClick={() => deleteCategory(c.id)}
															className="font-medium text-red-400 hover:underline">Delete</button>
														{c.status === 0 ? <button onClick={() => updateStatus(c.id, 1)}
															className="font-medium text-[#66a2df] hover:underline">Enable</button> :
															<button onClick={() => updateStatus(c.id, 0)}
																className="font-medium text-red-400 hover:underline">Disable</button>}
													</div>
												</td>
											</tr>
										))}
									</tbody>
								</table>

								<nav className="flex items-center flex-column flex-wrap md:flex-row justify-between pt-4" aria-label="Table navigation">
									<span className="text-sm font-normal text-gray-400 mb-4 md:mb-0 block w-full md:inline md:w-auto">Showing <span className="font-semibold text-[#e0e0e0]">{(pageNumber - 1) * 10 + 1}-{Math.min(pageNumber * 10, categories.total)}</span> of <span className="font-semibold text-[#e0e0e0]">{categories.total}</span></span>
									<ul className="inline-flex -space-x-px rtl:space-x-reverse text-sm h-8 bg-[#1b2537] text-[#e0e0e0]">
										<li>
											<button
												onClick={() => setPageNumber(Math.max(1, pageNumber - 1))}
												className="flex items-center justify-center px-3 h-8 ms-0 leading-tight border border-[#e0e0e0] rounded-s-lg hover:bg-gray-100 hover:text-gray-700">Previous</button>
										</li>
										{Array.from({ length: Math.ceil(categories.total / 10) }, (_, index) => (
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
												onClick={() => setPageNumber(Math.min(Math.ceil(categories.total / 10), pageNumber + 1))}
												className="flex items-center justify-center px-3 h-8 leading-tight border border-[#e0e0e0] rounded-e-lg hover:bg-gray-100 hover:text-gray-700">Next</button>
										</li>
									</ul>
								</nav>
							</div>
						)}
					</div>
				</div>

				<CategoryModal isOpen={isOpen} onOpen={onOpen} onOpenChange={onOpenChange} refresh={fetchCategories} data={data} />
			</div>
		</div>

	)
}

export default Page