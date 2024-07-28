'use client'
import React from 'react'
import { Dropdown, DropdownTrigger, DropdownMenu, DropdownItem } from "@nextui-org/dropdown";
import { ChevronDownIcon } from '@heroicons/react/24/outline';
import { useEmployeeService } from '@/services/useEmployeeService';
import { removeCookie } from '@/utils/cookie';
import { useRouter } from 'next/navigation';

function Menu({ name }: { name: string }) {
  const employeeService = useEmployeeService();
  const router = useRouter();
  const logout = async () => {
    try {
      const resp = await employeeService.logout();
      if (resp.code === 1) {
        removeCookie('token');
        removeCookie('name');
        removeCookie('user_name');
        router.push('/login');
      }
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <Dropdown placement="bottom-start">
      <DropdownTrigger className='focus:ring-0'>
        <button className='bg-[#66a2df] hover:bg-[#5791cb] px-4 py-[2px] gap-5 flex flex-row items-center rounded-md outline-none'>
          {name}
          <ChevronDownIcon className='h-4 w-4' />
        </button>
      </DropdownTrigger>
      <DropdownMenu aria-label="User Actions" variant="flat">
        <DropdownItem key="change_password">
          Change Password
        </DropdownItem>
        <DropdownItem key="log_out" color="danger" onClick={logout}>Log Out</DropdownItem>
      </DropdownMenu>
    </Dropdown>
  )
}

export default Menu;