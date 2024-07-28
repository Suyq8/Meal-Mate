'use client'
import React, { useState } from 'react'
import { XMarkIcon } from '@heroicons/react/24/outline'

function PopupNotification() {
	const socket = new WebSocket(`${process.env.NEXT_PUBLIC_WEBSOCKET_URL}${Math.random().toString(36)}`);
	const [notifications, setNotifications] = useState<Record<string, any>[]>([]);

	socket.onopen = () => {
		console.log("Websocket connected");
	};

	socket.onmessage = (event) => {
		const message = event.data;
		setNotifications([...notifications, message]);
	};

	socket.onclose = () => {
		console.log("Websocket closed");
	};

	return (
		<div className='absolute top-4 right-4 z-20 w-64 flex flex-col gap-5'>
			{notifications.map((notification, index) => (
				<div key={index} className='p-5 bg-[#c4daaf] text-[#141A28] rounded-md drop-shadow-md'>
					<div className='flex justify-end'>
						<button onClick={() => setNotifications(notifications.filter((_, i) => i !== index))}>
							<XMarkIcon className='h-4 w-4' />
						</button>
					</div>

					{notification.type===1?`New order: ${notification.content}`:`Cutomer reminds you of order: ${notification.content}`}
				</div>
			))}
		</div>
	)
}

export default PopupNotification