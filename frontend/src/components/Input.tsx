import React, { useState, SetStateAction, Dispatch, useEffect, ChangeEvent } from 'react';

interface InputProps {
	label: string;
	type: string;
	title: string;
	value: string;
	placeholder?: string;
	setValue: Dispatch<SetStateAction<string>>;
	setError: Dispatch<SetStateAction<string>>;
}

const Input: React.FC<InputProps> = ({
	label,
	type,
	title,
	value,
	placeholder,
	setValue,
	setError
}) => {
	const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
		const inputValue = e.target.value;
		setValue(inputValue);
	}

	return (
		<div className='w-full'>
			<div className="relative">
				<input
					type={type}
					value={value}
					onChange={handleChange}
					id={label}
					aria-describedby={`${label}-help`}
					placeholder={placeholder||""}
					onClick={() => setError('')}
					className={`block px-2.5 pb-1.5 pt-3 w-full text-sm bg-transparent rounded-lg border-1 appearance-none focus:outline-none focus:ring-0 peer border-gray-300 focus:border-[#6870fa]`}
				/>
				<label
					htmlFor={label}
					className={`absolute text-sm duration-300 transform -translate-y-3 scale-75 top-1 z-10 origin-[0] bg-[#1b2537] px-2
						peer-focus:px-2 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-1/2 
						peer-focus:top-1 peer-focus:scale-75 peer-focus:-translate-y-3 start-1 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto 
						peer-focus:text-[#6870fa]`}
				>
					{title}
				</label>
			</div>
		</div>
	);
};

export default Input;