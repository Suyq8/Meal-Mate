import React, { useEffect, useState } from 'react';
import { Modal as M, ModalContent, ModalHeader, ModalBody, ModalFooter, Selection, Textarea } from "@nextui-org/react";
import Input from "./Input";
import CustomSelect from './CustomSelect';
import { useCategoryService } from '@/services/useCategoryService';
import { useCommonservice } from '@/services/useCommonService';
import { useMealService } from '@/services/useMealService';
import AddDish from './AddDish';

interface ModalProps {
	isOpen: boolean;
	onOpen: () => void;
	onOpenChange: () => void;
	id?: string | number;
	refresh?: () => void;
}

function MealModal({ isOpen, onOpen, onOpenChange, id, refresh }: ModalProps) {
	const styleArea = {
		inputWrapper: ['bg-[#1b2537] border-1 border-[#e0e0e0] data-[hover=true]:bg-[#1b2537] group-data-[focus=true]:bg-[#1b2537]',
			'data-[hover=true]:text-[#e0e0e0] group-data-[focus=true]:text-[#e0e0e0] group-data-[focus=true]:border-[#6870fa]'
		],
		input: 'group-data-[has-value=true]:text-[#e0e0e0]',
		label: 'text-[#e0e0e0] group-data-[filled-within=true]:text-[#e0e0e0] group-data-[focus=true]:text-[#6870fa]'
	}
	const [name, setName] = useState<string>('');
	const [price, setPrice] = useState<string>('');
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [error, setError] = useState<string>('');
	const [category, setCategory] = useState<Selection>(new Set<string>());
	const [categories, setCategories] = useState<string[]>([]);
	const [categoryMap, setCategoryMap] = useState<Record<string, any>>({});
	const [categoryMapReverse, setCategoryMapReverse] = useState<Record<number, string>>({});
	const [imgUrl, setImgUrl] = useState<string>('');
	const [description, setDescription] = useState<string>('');
	const [dish, setDish] = useState<Record<string, any>[]>([]);
	const [selectedFile, setSelectedFile] = useState<File | null>(null);
	const categoryService = useCategoryService();
	const commonService = useCommonservice();
	const mealServerice = useMealService();

	const submit = async (onClose: () => void) => {
		if (name === "" || price === "" || (category !== "all" && category.size == 0) || dish.length === 0) {
			setError("Please fill in the blank/Select at least one dish");
			return;
		}

		setIsLoading(true);
		try {
			const url = await upload();
			var params: any = { name, categoryId: categoryMap[Array.from(category)[0]], price: Number(price), description, mealDishes: dish }

			if (url !== "") {
				params = { ...params, image: url };
			}

			var resp;
			if (id) {
				params = { ...params, id: id };
				resp = await mealServerice.update(params);
			} else {
				params = { ...params, status: 1 };
				resp = await mealServerice.add(params);
			}

			if (resp.code === 1) {
				setName("");
				setPrice("");
				setCategory(new Set<string>());
				setImgUrl("");
				setDescription("");
				setDish([]);
				if (refresh) refresh();

				setIsLoading(false);
				onClose();
			} else {
				setIsLoading(false);
				setError(resp.msg);
			}
		} catch (error: any) {
			setIsLoading(false);
			console.error(error);
		}
	};

	const fetchCategories = async () => {
		try {
			const resp = await categoryService.getAll({ type: 2 });

			if (resp.code === 1) {
				const tmp = resp.data.reduce((map: Record<string, any>, item: any) => {
					map[item.name] = item.id;
					return map;
				}, {});
				setCategoryMap(tmp);
				const tmp1 = resp.data.reduce((map: Record<number, string>, item: any) => {
					map[item.id] = item.name;
					return map;
				}, {});
				setCategoryMapReverse(tmp1);
				setCategories(resp.data.map((item: any) => item.name));
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const upload = async () => {
		try {
			const formData = new FormData();

			if (selectedFile) {
				formData.append('file', selectedFile);
				const resp = await commonService.upload(formData);
				if (resp.code === 1) {
					return resp.data.url;
				} else {
					console.error(resp.msg);
				}
			} else {
				console.error("No file selected");
			}
		} catch (error: any) {
			console.error(error);
		}
		return "";
	}

	const fetchDish = async () => {
		if (!id) return;
		try {
			const resp = await mealServerice.getById(id);

			if (resp.code === 1) {
				setName(resp.data.name);
				setPrice(resp.data.price as string);
				setCategory(new Set([categoryMapReverse[resp.data.categoryId]]));
				setImgUrl(resp.data.image);
				setDescription(resp.data.description);
				setDish(resp.data.mealDishes);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const file = e.target.files?.[0];
		if (file) {
			const imageUrl = URL.createObjectURL(file);
			setImgUrl(imageUrl);
			setSelectedFile(file);
		}
	};

	useEffect(() => {
		if (isOpen) {
			fetchDish();
		}
	}, [isOpen]);

	useEffect(() => {
		fetchCategories();
	}, []);

	return (
		<div className='flex items-center justify-center text-[#e0e0e0] bg-[#1b2537]'>
			<M isOpen={isOpen} onOpenChange={onOpenChange}
				onClose={() => {
					setName("");
					setPrice("");
					setCategory(new Set<string>());
					setImgUrl("");
					setDescription("");
					setDish([]);
					setIsLoading(false);
					setError("");
				}}
				backdrop="blur" className='text-[#e0e0e0] bg-[#1b2537]'>
				<ModalContent>
					{(onClose) => (
						<div>
							<ModalHeader className="flex flex-col gap-1 pb-0">{id ? 'Update Meal' : 'Add Meal'}</ModalHeader>

							<ModalBody>
								<Input label="name" title="Name" type="text" value={name} setValue={setName} setError={setError} />
								<Input label="price" title="Price" type="text" value={price} setValue={setPrice} setError={setError} />
								<CustomSelect className='h-10' value={category} setValue={setCategory} items={categories}
									label="type" setError={setError} useLabel={true} />
								<AddDish value={dish} setValue={setDish} />
								<div className="flex items-center justify-center w-full">
									<label htmlFor="dropzone-file" className="flex flex-col items-center justify-center w-full h-32 border-1 border-[#e0e0e0] rounded-lg cursor-pointer bg-[#1b2537]">
										{imgUrl ? <img src={imgUrl} className='h-32 w-32' /> :
											<div className="flex flex-col items-center justify-center pt-5 pb-6">
												<svg className="w-8 h-8 mb-4 text-[#e0e0e0]" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
													<path stroke="currentColor" d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2" />
												</svg>
												<p className="mb-2 text-sm text-[#e0e0e0]"><span className="font-semibold">Click to upload</span> or drag and drop</p>
												<p className="text-xs text-[#e0e0e0]">PNG, JPG or JPEG</p>
											</div>}
										<input id="dropzone-file" type="file" className="hidden" accept=".jpg,.jpeg,.png" onChange={handleFileChange} />
									</label>
								</div>
								<Textarea
									label="Description"
									classNames={styleArea}
									value={description}
									onChange={(e) => setDescription(e.target.value)}
								/>
							</ModalBody>

							<ModalFooter className='pt-0'>
								<div className='flex flex-row gap-6 items-center'>
									<p className='text-red-400'>{error}</p>
									<button onClick={() => { submit(onClose) }} className="px-5 py-2 rounded-md bg-[#66a2df] hover:bg-[#5791cb] focus:border-0">
										{isLoading ? 'Loading...' : (id ? 'Update' : 'Add')}
									</button>
								</div>

							</ModalFooter>
						</div>
					)}
				</ModalContent>
			</M>
		</div>
	);
}

export default MealModal;
