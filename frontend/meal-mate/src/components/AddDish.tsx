import React, { use, useEffect, useState } from 'react'
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, useDisclosure } from "@nextui-org/react";
import { useCategoryService } from '@/services/useCategoryService';
import { useDishService } from '@/services/useDishService';
import { MinusIcon, PlusIcon } from '@heroicons/react/24/outline';

interface Props {
	value: Record<string, any>[],
	setValue: (value: any) => void
}

interface ModalProps {
	isOpen: boolean;
	onOpen: () => void;
	onOpenChange: () => void;
	setValue: (value: any) => void;
}

function AddDish({ value, setValue }: Props) {
	const { isOpen, onOpen, onOpenChange } = useDisclosure();

	return (
		<div className='text-[#e0e0e0]'>
			<button onClick={onOpen} className='bg-[#66a2df] hover:bg-[#5791cb] px-2 py-1 rounded-md'>
				Add Dish
			</button>
			<div className='flex flex-col gap-3 my-3'>
				{value.length > 0 &&
					value.map((item: any) => (
						<div className='flex flex-row justify-between items-center' key={item.dishId}>
							<span className='w-[55%]'>{item.name}</span>
							<span>${item.price}</span>
							<div className='flex flex-row items-center h-6'>
								<button
									onClick={() => {
										const newQuantity = Math.max(1, item.quantity - 1);
										const updatedItem = { ...item, quantity: newQuantity };
										setValue((prevValue: Record<string, any>[]) =>
											prevValue.map((prevItem: Record<string, any>) =>
												prevItem.dishId === item.dishId ? updatedItem : prevItem
											)
										);
									}}
									className='button h-full rounded-l px-1'
								>
									<MinusIcon className='h-4 w-4' />
								</button>
								<input
									className='w-10 bg text-[#e0e0e0] outline-none text-center border-y-1 border-[#66a2df] h-full'
									type='text'
									value={item.quantity}
									onChange={(e) => {
										const newQuantity = Number(e.target.value);
										const updatedItem = { ...item, quantity: newQuantity };
										setValue((prevValue: Record<string, any>[]) =>
											prevValue.map((prevItem: Record<string, any>) =>
												prevItem.dishId === item.dishId ? updatedItem : prevItem
											)
										);
									}}
								/>
								<button
									onClick={() => {
										const newQuantity = Math.min(99, item.quantity + 1);
										const updatedItem = { ...item, quantity: newQuantity };
										setValue((prevValue: Record<string, any>[]) =>
											prevValue.map((prevItem: Record<string, any>) =>
												prevItem.dishId === item.dishId ? updatedItem : prevItem
											)
										);
									}}
									className='button h-full rounded-r px-1'
								>
									<PlusIcon className='h-4 w-4' />
								</button>
							</div>
						</div>
					))}
			</div>
			<CustomModal isOpen={isOpen} onOpen={onOpen} onOpenChange={onOpenChange} setValue={setValue} />
		</div>
	);
}

export default AddDish;

function CustomModal({ isOpen, onOpen, onOpenChange, setValue }: ModalProps) {
	const [categories, setCategories] = useState<string[]>([]);
	const [category, setCategory] = useState<string>('');
	const [categoryMap, setCategoryMap] = useState<Record<string, any>>({});
	const [dishes, setDishes] = useState<Record<string, any>[]>([]);
	const [selectedDishes, setSelectedDishes] = useState<Set<number>>(new Set<number>());
	const [allDishes, setAllDishes] = useState<Record<string, any>[]>([]);
	const categoryService = useCategoryService();
	const dishService = useDishService();

	const handleDishSelectionChange = (dishId: number) => {
		const newSelection = new Set(selectedDishes);
		if (newSelection.has(dishId)) {
			newSelection.delete(dishId);
		} else {
			newSelection.add(dishId);
		}
		setSelectedDishes(newSelection);
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

	const submit = async (onClose: () => void) => {
		var newValue: Record<string, any>[] = [];
		selectedDishes.forEach((dishId: number) => {
			const dish = allDishes.find((d: any) => d.id === dishId);
			console.log(dish)
			if (dish) {
				newValue.push({name: dish.name, price: dish.price, quantity: 1, dishId: dish.id});
			}
		})
		console.log(newValue);
		console.log(selectedDishes);
		setValue(newValue);
		onClose();
	};

	const fetchDishes = async (id: number | string) => {
		try {
			const resp = await dishService.getAll({ categoryId: id });

			if (resp.code === 1) {
				setDishes(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const fetchAllDishes = async () => {
		try {
			const resp = await dishService.getAll({});

			if (resp.code === 1) {
				setAllDishes(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	useEffect(() => {
		if (isOpen) {
			fetchCategories();
			if (categories.length > 0) {
				setCategory(categories[0]);
			}
		}
	}, [isOpen]);

	useEffect(() => {
		if (categories.length > 0) {
			setCategory(categories[0]);
		}
	}, [categories]);

	useEffect(() => {
		if(category){
			fetchDishes(categoryMap[category]);
		}
	}, [category]);

	useEffect(() => {
		fetchAllDishes();
	}, []);

	return (
		<Modal isOpen={isOpen} onOpenChange={onOpenChange} onClose={() => {setCategories([]); setDishes([]); }}
			backdrop="blur" className='text-[#e0e0e0] bg-[#1b2537] w-[40rem] max-w-[40rem]'>
			<ModalContent>
				{(onClose) => (
					<div>
						<ModalHeader className="flex flex-col gap-1 pb-0">Add Dish</ModalHeader>

						<ModalBody>
							<div className='flex flex-row'>
								{categories.length > 0 && categories.map((c: string, i: number) => (
									<button onClick={() => setCategory(c)} key={c}
										className={`border border-[#e0e0e0] px-2 py-1 ${i === 0 && 'rounded-l-sm'} ${i + 1 === categories.length && 'rounded-r-sm'}
													${category === c && 'bg-[#66a2df]'}`}>
										<p>{c}</p>
									</button>
								))}
							</div>
							<div className='grid grid-cols-2'>
								{dishes.length > 0 && dishes.map((dish: any, i: number) => (
									<div className='flex flex-row items-center gap-3 py-2' key={i}>
										<input
											type="checkbox"
											checked={selectedDishes.has(dish.id)}
											onChange={() => handleDishSelectionChange(dish.id)}
											className='accent-[#66a2df]'
										/>
										<div className='flex flex-row items-center w-full justify-around'>
											<img src={dish.image} alt={dish.name} className='h-12 w-12' />
											<div className='w-[50%]'>
												<p>{dish.name}</p>

												<div className='flex flex-row items-center gap-2 justify-start'>
													<div className={`h-2 w-2 rounded-full ${dish.status === 1 ? 'bg-[#c4daaf]' : 'bg-red-400'}`} />
													{dish.status === 1 ? <p>Available</p> : <p>Unavailable</p>}
												</div>
											</div>
											<p>${dish.price}</p>
										</div>

									</div>
								))}
							</div>
						</ModalBody>

						<ModalFooter className='pt-0'>
							<button onClick={() => { submit(onClose) }} className="px-5 py-2 rounded-md bg-[#66a2df] hover:bg-[#5791cb] focus:border-0">
								Add
							</button>
						</ModalFooter>
					</div>
				)}
			</ModalContent>
		</Modal>
	)
}