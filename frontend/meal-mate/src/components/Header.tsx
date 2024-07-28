'use client'
import { usePathname } from 'next/navigation';
import React, { useEffect, useState } from 'react'
import Modal from './Modal';
import { useDisclosure } from '@nextui-org/react';
import Menu from './Menu';
import { useShopservice } from '@/services/useShopService';
import { getCookie } from '@/utils/cookie';

function Header() {
	const pathname = usePathname();
	const [status, setStatus] = useState<number>(0);
	const { isOpen, onOpen, onOpenChange } = useDisclosure();
	const shopService = useShopservice();
	const [name, setName] = useState<string | undefined>('');

	const getStatus = async () => {
		try {
			const resp = await shopService.getStatus();
			if (resp.code === 1) {
				setStatus(resp.data);
			} else {
				console.log(resp.msg);
			}
		} catch (error) {
			console.error(error);
		}
	};

	const getName = async () => {
		try {
			const name = await getCookie('user_name');
			setName(name);
		} catch (error) {
			console.error(error);
		}
	};

	useEffect(() => {
		getStatus();
		getName();
	}, []);

	return (
		pathname != "/login" &&
		<div>
			<div className='flex h-14 w-full justify-between items-center bg-[#323c5b] text-[#e0e0e0]'>
				<div className='flex flex-row gap-9'>
					<div className='text-xl px-6 font-[cursive]'>
						Meal Mate
					</div>

					<div className='bg-[#66a2df] rounded-md text-sm px-3 items-center flex'>
						{/* shop status */}
						{status ? 'Opening' : 'Closed'}
					</div>
				</div>

				<div className='flex flex-row gap-6 h-full items-center'>
					<div className='hover:bg-[#c4daaf] hover:text-[#6870fa] px-6 flex items-center h-full'>
						<button onClick={onOpen} className='h-full'>
							{/* set shop status */}
							Set Opening Status
						</button>
					</div>
					<div className='px-6'>
						{/* user profile */}
						<Menu name={name||""} />
					</div>
				</div>

			</div>
			<Modal isOpen={isOpen} onOpen={onOpen} onOpenChange={onOpenChange} status={status} setStatus={setStatus} />
		</div>


	)
}

export default Header