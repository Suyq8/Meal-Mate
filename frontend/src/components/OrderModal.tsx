import React, { useEffect, useState } from 'react';
import { Modal as M, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/react";
import { useOrderService } from '@/services/useOrderService';

interface ModalProps {
	isOpen: boolean;
	onOpen: () => void;
	onOpenChange: () => void;
	id?: string | number;
}

function OrderModal({ isOpen, onOpen, onOpenChange, id }: ModalProps) {
	const styleArea = {
		inputWrapper: ['bg-[#1b2537] border-1 border-[#e0e0e0] data-[hover=true]:bg-[#1b2537] group-data-[focus=true]:bg-[#1b2537]',
			'data-[hover=true]:text-[#e0e0e0] group-data-[focus=true]:text-[#e0e0e0] group-data-[focus=true]:border-[#6870fa]'
		],
		input: 'group-data-[has-value=true]:text-[#e0e0e0]',
		label: 'text-[#e0e0e0] group-data-[filled-within=true]:text-[#e0e0e0] group-data-[focus=true]:text-[#6870fa]'
	}

	const [data, setData] = useState<Record<string, any>>({});
	const orderServerice = useOrderService();

	const fetchOrder = async () => {
		if (!id) return;
		try {
			const resp = await orderServerice.getDetailById(id);

			if (resp.code === 1) {
				setData(resp.data);
			} else {
				console.error(resp.msg);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	useEffect(() => {
		if (isOpen) {
			fetchOrder();
		}
	}, [isOpen]);

	return (
		<div className='flex items-center justify-center text-[#e0e0e0] bg-[#1b2537]'>
			<M isOpen={isOpen} onOpenChange={onOpenChange}
				onClose={() => {
					setData([]);
				}}
				backdrop="blur" className='text-[#e0e0e0] bg-[#1b2537]'>
				<ModalContent>
					{(onClose) => (
						<div>
							<ModalHeader className="flex flex-col gap-1 pb-0">Details</ModalHeader>

							<ModalBody>
								<div>
									<p>{`Serial Number: ${data.serialNumber||""}`}</p>
									<p>{`Order Time: ${data.orderTime}`}</p>
								</div>

								<div className='grid grid-cols-2'>
									<p>{`Name: ${data.name||""}`}</p>
									<p>{`Phone: ${data.phone||""}`}</p>
								</div>

								<p>{`Address: ${data.address||""}`}</p>
								<p>{`Note: ${data.note||""}`}</p>

								<p>Dishes: </p>
								<div className='grid grid-cols-2 gap-3'>
									{data.orderDetailList?.map((item: any, index: number) => (
										<div key={index} className='flex flex-row items-center gap-5'>
											<p>{`${item.name}*${item.quantity}`}</p>
											<p>${item.price}</p>
										</div>
									))}
								</div>
								<p>{`Total: \$${data.price}`}</p>

							</ModalBody>
						</div>
					)}
				</ModalContent>
			</M>
		</div>
	);
}

export default OrderModal;
