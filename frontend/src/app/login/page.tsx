'use client'
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { encrypt } from '@/utils/security';
import { setCookie } from '@/utils/cookie';
import { useEmployeeService } from '@/services/useEmployeeService';

const LoginPage: React.FC = () => {
    const router = useRouter();
    const [errorMessage, setErrorMessage] = useState('');
    const employeeService = useEmployeeService();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        let userName = formData.get('username') as string;
        let password = formData.get('password') as string;

        // Encrypt the username and password
        userName = await encrypt(userName);
        password = await encrypt(password);

        console.info(`User ${userName}`);
        console.info(`Password: ${password}`);

        const resp = await employeeService.login(userName, password);

        if (resp.code === 1) {
            setCookie('token', resp.data.token);
            setCookie('name', resp.data.name);
            setCookie('user_name', resp.data.userName);
            router.push('/');
            console.log("logged in");
        } else {
            setErrorMessage("Wrong username or password");
        }
    };

    return (
        <div className='h-screen w-screen flex items-center justify-center bg-[#1b2537]'>
            <div className='flex h-2/3'>
                <img src="burger.webp" className='rounded-l-lg' />
                <form onSubmit={handleSubmit} className='flex flex-col items-center justify-center bg-white px-20 rounded-r-lg py-10'>
                    <p className='text-[#1b2537] text-xl font-[cursive]'>
                        Meal Mate
                    </p>
                    {errorMessage && <p className="text-red-500 fixed text-sm">{errorMessage}</p>}
                    <div className="relative mt-40">
                        <input type="text" id="username" name="username" className="block px-2.5 pb-1.5 pt-3 w-full text-sm text-gray-900 bg-transparent rounded-lg border border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-[#1b2537] peer" placeholder="" />
                        <label htmlFor="username" className="absolute text-sm text-gray-500 duration-300 transform -translate-y-3 scale-75 top-1 z-10 origin-[0] bg-white px-2 peer-focus:px-2 peer-focus:text-[#1b2537] peer-focus:dark:text-[#1b2537] peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-1/2 peer-focus:top-1 peer-focus:scale-75 peer-focus:-translate-y-3 start-1 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto">User Name</label>
                    </div>

                    <div className="relative mt-5">
                        <input type="password" id="password" name="password" className="block px-2.5 pb-1.5 pt-3 w-full text-sm text-gray-900 bg-transparent rounded-lg border border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-[#1b2537] peer" placeholder="" />
                        <label htmlFor="password" className="absolute text-sm text-gray-500 duration-300 transform -translate-y-3 scale-75 top-1 z-10 origin-[0] bg-white px-2 peer-focus:px-2 peer-focus:text-[#1b2537] peer-focus:dark:text-[#1b2537] peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-1/2 peer-focus:top-1 peer-focus:scale-75 peer-focus:-translate-y-3 start-1 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto">Password</label>
                    </div>
                    <button type="submit" className='bg-[#1b2537] text-white px-6 py-1 rounded-full mt-8'>Login</button>
                </form>
            </div>

        </div>
    );
};

export default LoginPage;