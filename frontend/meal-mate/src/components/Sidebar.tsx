'use client'
import React, { useState, useMemo, use, useEffect } from "react";
import { usePathname } from "next/navigation";
import Link from 'next/link';
import { HomeIcon, DocumentTextIcon, Bars3Icon, PresentationChartLineIcon, GiftIcon, BellIcon, SquaresPlusIcon, UserIcon } from '@heroicons/react/24/outline';

interface SidebarItem {
  id: number;
  name: string;
  icon: React.ComponentType<any>;
  link: string;
}

function Sidebar() {
  const [isCollapsed, setIsCollapsed] = useState<boolean>(false);

  const sidebarItems: SidebarItem[] = [
    { id: 1, name: "Dashboard", icon: HomeIcon, link: '/' },
    { id: 2, name: "Statistics", icon: PresentationChartLineIcon, link: '/statistics' },
    { id: 3, name: "Order", icon: DocumentTextIcon, link: '/order' },
    { id: 4, name: "Meal Management", icon: GiftIcon, link: '/meal' },
    { id: 5, name: "Dish Management", icon: BellIcon, link: '/dish' },
    { id: 6, name: "Category Management", icon: SquaresPlusIcon, link: '/category' },
    { id: 7, name: "Employee Management", icon: UserIcon, link: '/employee' }
  ];

  const pathname = usePathname();

  const activeItem = useMemo(
    () => sidebarItems.find((item) => item.link === pathname),
    [pathname]
  );

  return (
    pathname !== "/login" &&
    <div
      style={{ transition: "width 500ms cubic-bezier(0.2, 0, 0, 1) 0s" }}
      className={`bg-[#1b2537] text-[#e0e0e0] px-2 pt-4 flex justify-start flex-col ${isCollapsed ? "w-[4.5rem]" : "w-72"} h-full`}
    >
      <div className="flex-col justify-start">
        <div className={`flex mx-4 ${isCollapsed ? "justify-center" : "justify-end"}`}>
          <button onClick={() => setIsCollapsed(!isCollapsed)}>
            <Bars3Icon className="h-6 w-6" />
          </button>
        </div>
        {sidebarItems.map((item) => (
          <Link
            key={item.id}
            href={item.link}
            className={`flex items-center space-x-2 p-4 ${activeItem?.id === item.id ? 'text-[#6870fa]' : 'hover:text-[#868dfb]'}`}
          >
            <item.icon className='h-6 w-6 pr-1' />
            {!isCollapsed && item.name}
          </Link>
        ))}
      </div>
    </div>
  );
}

export default Sidebar;
