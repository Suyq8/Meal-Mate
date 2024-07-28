import React, { useEffect, useState } from 'react';
import { Modal as M, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/react";
import Input from "./Input";
import { useEmployeeService } from '@/services/useEmployeeService';

interface ModalProps {
	isOpen: boolean;
	onOpen: () => void;
	onOpenChange: () => void;
	id: string | number;
	refresh: () => void;
}

function EmployeeModal({ isOpen, onOpen, onOpenChange, id, refresh }: ModalProps) {
	const [userName, setUserName] = useState<string>('');
	const [name, setName] = useState<string>('');
	const [phone, setPhone] = useState<string>('');
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const employeeService = useEmployeeService();
	const [error, setError] = useState<string>('');

	const fetchEmployee = async () => {
		if (!id) return;
		try {
			const resp = await employeeService.getEmployeeById(id);
			if (resp.code === 1) {
				setUserName(resp.data.userName);
				setName(resp.data.name);
				setPhone(resp.data.phone);
			}
		} catch (error: any) {
			console.error(error);
		}
	};

	useEffect(() => {
		if (isOpen) {
			fetchEmployee();
		}
	}, [isOpen]);

	const submit = async (onClose: () => void) => {
		setIsLoading(true);

		var resp;
		if (id) {
			resp = await employeeService.updateEmployee({ id, userName, name, phone });
		} else {
			resp = await employeeService.addEmployee({ userName, name, phone });
		}

		if (resp.code === 1) {
			setUserName("");
			setName("");
			setPhone("");
			setError("");
			refresh();

			setIsLoading(false);
			onClose();
		} else {
			setIsLoading(false);
			setError(resp.msg);
		}
	};

	return (
		<div className='flex items-center justify-center text-[#e0e0e0] bg-[#1b2537]'>
			<M isOpen={isOpen} onOpenChange={onOpenChange} onClose={() => { setUserName(""); setName(""); setPhone(""); setIsLoading(false); setError(""); }}
				backdrop="blur" className='text-[#e0e0e0] bg-[#1b2537]'>
				<ModalContent>
					{(onClose) => (
						<div>
							<ModalHeader className="flex flex-col gap-1 pb-0">{id ? 'Update Employee' : 'Add Employee'}</ModalHeader>

							<ModalBody>
								<Input label="user-name" title="User name" type="text" value={userName} setValue={setUserName} setError={setError} />
								<Input label="name" title="Name" type="text" value={name} setValue={setName} setError={setError} />
								<Input label="phone" title="Phone" type="phone" value={phone} setValue={setPhone} setError={setError} />
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

export default EmployeeModal;
