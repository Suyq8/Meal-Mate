import React, { useEffect, useState } from 'react';
import { Modal as M, ModalContent, ModalHeader, ModalBody, ModalFooter, Selection } from "@nextui-org/react";
import Input from "./Input";
import CustomSelect from './CustomSelect';
import { useCategoryService } from '@/services/useCategoryService';

interface ModalProps {
	isOpen: boolean;
	onOpen: () => void;
	onOpenChange: () => void;
	data?: Record<string, any>;
	refresh: () => void;
}

function CategoryModal({ isOpen, onOpen, onOpenChange, data, refresh }: ModalProps) {
	const [name, setName] = useState<string>('');
	const [ranking, setRanking] = useState<string>('');
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const categoryService = useCategoryService();
	const [error, setError] = useState<string>('');
	const [type, setType] = useState<Selection>(new Set<string>());
	const categoryType: string[] = ['Dish', 'Meal'];
	const categoryMap: Record<string, number> = { 'Dish': 1, 'Meal': 2 };

	const submit = async (onClose: () => void) => {
		if (name === "" || ranking === "" || (type !== "all" && type.size == 0)) {
			setError("Please fill in the blank");
			return;
		}

		setIsLoading(true);
		try {
			var resp;
			if (data) {
				resp = await categoryService.update({ id: data.id, name, ranking, type: categoryMap[Array.from(type)[0]] });
			} else {
				resp = await categoryService.add({ name, ranking, type: categoryMap[Array.from(type)[0]] });
			}

			if (resp.code === 1) {
				setName("");
				setRanking("");
				setType(new Set<string>());
				refresh();

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

	useEffect(() => {
		if (isOpen && data) {
			setName(data.name);
			setRanking(data.ranking);
			setType(new Set<string>([categoryType[data.type - 1]]));
		}
	}, [isOpen]);

	return (
		<div className='flex items-center justify-center text-[#e0e0e0] bg-[#1b2537]'>
			<M isOpen={isOpen} onOpenChange={onOpenChange} onClose={() => { setName(""); setRanking(""); setIsLoading(false); setError(""); }}
				backdrop="blur" className='text-[#e0e0e0] bg-[#1b2537]'>
				<ModalContent>
					{(onClose) => (
						<div>
							<ModalHeader className="flex flex-col gap-1 pb-0">{data ? 'Update Category' : 'Add Category'}</ModalHeader>

							<ModalBody>
								<Input label="name" title="Name" type="text" value={name} setValue={setName} setError={setError} />
								<Input label="ranking" title="Ranking" type="text" value={ranking} setValue={setRanking} setError={setError} />
								<CustomSelect className='h-10' value={type} setValue={setType} items={categoryType}
									label="type" setError={setError} useLabel={true} />
							</ModalBody>

							<ModalFooter className='pt-0'>
								<div className='flex flex-row gap-6 items-center'>
									<p className='text-red-400'>{error}</p>
									<button onClick={() => { submit(onClose) }} className="px-5 py-2 rounded-md bg-[#66a2df] hover:bg-[#5791cb] focus:border-0">
										{isLoading ? 'Loading...' : (data ? 'Update' : 'Add')}
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

export default CategoryModal;
