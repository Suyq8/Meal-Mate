import { Select, SelectItem, SharedSelection, Selection } from '@nextui-org/react';
import React, { useState } from 'react';
import { ChevronDownIcon, XCircleIcon } from '@heroicons/react/24/outline';

type CustomSelectProps = {
	value: Selection;
	setValue: (keys: SharedSelection) => void;
	items: string[];
	label: string;
	className?: string;
	useLabel?: boolean;
	setError?: (error: string) => void;
};

function CustomSelect({ value, setValue, items, label, className, useLabel, setError }: CustomSelectProps) {
	const styleSelect = {
		trigger: [
			'border-1 rounded-md min-h-[2rem] group/trigger',
			'data-[focus=true]:border-[#6870fa] data-[open=true]:border-[#6870fa]',
			className,
		],
		popoverContent: 'bg-[#1b2537] text-[#e0e0e0]',
		value: 'text-[#e0e0e0]',
		innerWrapper: 'group-data-[has-label=true]:pt-0',
		label: ["group-data-[filled=true]:-translate-y-3/4 group-data-[filled=true]:px-2 bg-[#1b2537]",
			"group-data-[filled=true]:scale-75 group-data-[filled=true]:start-1 text-[#e0e0e0]",
			"group-data-[filled=true]:text-[#e0e0e0] group-[focus=true]/trigger:text-[#6870fa] duration-300"
		]
	};
	const [isHovered, setIsHovered] = useState<boolean>(false);

	return (
		<div
			onMouseEnter={() => setIsHovered(true)}
			onMouseLeave={() => setIsHovered(false)}
		>
			<Select
				aria-label={label}
				label={useLabel ? label : ""}
				variant="bordered"
				selectedKeys={value}
				className="w-full h-full"
				onSelectionChange={setValue}
				classNames={styleSelect}
				onClick={() => setError && setError('')}
				selectorIcon={
					value instanceof Set && value.size !== 0 && isHovered ? (
						<XCircleIcon className="h-4 w-4" onClick={() => setValue(new Set([]))} />
					) : (
						<ChevronDownIcon className="h-4 w-4" />
					)
				}
			>
				{items.map((c) => (
					<SelectItem
						key={c}
						classNames={{ base: 'data-[hover=true]:bg-[#66a2df] data-[selectable=true]:focus:bg-[#66a2df]' }}
					>
						{c}
					</SelectItem>
				))}
			</Select>
		</div>
	);
}

export default CustomSelect;