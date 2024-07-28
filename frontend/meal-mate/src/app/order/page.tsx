'use client'
import React, { useEffect, useState } from 'react';
import { DateRangePicker, DateValue, Input, RangeValue, useDisclosure } from "@nextui-org/react";
import { useOrderService } from '@/services/useOrderService';
import { XCircleIcon } from '@heroicons/react/24/outline';
import OrderModal from '@/components/OrderModal';

type OrderStatus = 'All' | 'Pending' | 'Accepted' | 'Delivering' | 'Completed' | 'Cancelled';

function Page() {
  const orderStatus: OrderStatus[] = ['All', 'Pending', 'Accepted', 'Delivering', 'Completed', 'Cancelled'];
  const [selectedStatus, setSelectedStatus] = useState<OrderStatus>('All');
  const [pageNumber, setPageNumber] = useState<number>(1);
  const [serialNumber, setSerialNumber] = useState<string>('');
  const [phone, setPhone] = useState<string>('');
  const [dateRange, setDateRange] = useState<RangeValue<DateValue> | null>(null);
  const [orders, setOrders] = useState<{ total: number, records: Record<string, any>[] }>({ total: 0, records: [] });
  const [isHovered, setIsHovered] = useState<boolean>(false);
  const [id, setId] = useState<string | number>('');
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const orderService = useOrderService();

  const styleInput: Record<string, string | string[]> = {
    input: "text-[#e0e0e0]",
    inputWrapper: [
      "group-data-[focus=true]:border-[#6870fa] appearance-none group-data-[focus=true]:outline-none group-data-[focus=true]:ring-0",
      "border-1 rounded-md min-h-[2rem] h-8"
    ],
  };
  const stylePicker: Record<string, string | string[]> = {
    inputWrapper: [
      "appearance-none focus-within:border-[#6870fa] focus-within:hover:border-[#6870fa]",
      "border rounded-md min-h-[2rem] h-8"
    ],
    selectorButton: ''
  };
  const stylerCalendar: Record<string, string | string[]> = {
    base: 'bg-[#1b2537] text-[#e0e0e0]',
    headerWrapper: 'bg-[#1b2537]',
    title: 'text-[#e0e0e0]',
    gridHeader: 'bg-[#1b2537]',
    gridHeaderCell: 'text-[#e0e0e0]',
    cellButton: [
      'text-[#e0e0e0]',
      'data-[selected=true]:data-[selection-start=true]:data-[range-selection=true]:bg-[#66a2df]',
      'data-[selected=true]:data-[selection-end=true]:data-[range-selection=true]:bg-[#66a2df]',
      'data-[outside-month=true]:text-gray-400'
    ]
  };

  const header: Record<OrderStatus, string[]> = {
    'All': ['Serial Number', 'Status', 'Name', 'Phone', 'Address', 'Order Time', 'Price', 'Action'],
    'Pending': ['Serial Number', 'Dishes', 'Address', 'Expected Delivery Time', 'Price', 'Note', 'Tableware', 'Action'],
    'Accepted': ['Serial Number', 'Dishes', 'Address', 'Expected Delivery Time', 'Note', 'Tableware', 'Action'],
    'Delivering': ['Serial Number', 'Dishes', 'Address', 'Expected Delivery Time', 'Note', 'Tableware', 'Action'],
    'Completed': ['Serial Number', 'Name', 'Phone', 'Address', 'Delivery Time', 'Price', 'Note', 'Action'],
    'Cancelled': ['Serial Number', 'Name', 'Phone', 'Address', 'Order Time', 'Cancel Time', 'Cancel Reason', 'Action'],
  };

  const headerToData: Record<string, string> = {
    'Serial Number': 'serialNumber',
    'Status': 'status',
    'Dishes': 'orderDishes',
    'Name': 'name',
    'Phone': 'phone',
    'Address': 'address',
    'Order Time': 'orderTime',
    'Price': 'price',
    'Note': 'note',
    'Tableware': 'tablewareNumber',
    'Expected Delivery Time': 'estimatedDeliveryTime',
    'Cancel Time': 'cancelTime',
    'Cancel Reason': 'cancelReason',
    'Delivery Time': 'deliveryTime'
  };

  const statusMap: Record<number, OrderStatus> = { 2: 'Pending', 3: 'Accepted', 4: 'Delivering', 5: 'Completed', 6: 'Cancelled' };
  const statusMapReverse: Record<OrderStatus, number> = { 'All': 1, 'Pending': 2, 'Accepted': 3, 'Delivering': 4, 'Completed': 5, 'Cancelled': 6 };

  const fetchOrders = async () => {
    try {
      var params: { page: number; pageSize: number; serialNumber: string; phone: string, status?: number, beginTime?: string, endTime?: string } = { page: pageNumber, pageSize: 10, serialNumber, phone };
      if (selectedStatus !== "All") {
        params.status = statusMapReverse[selectedStatus];
      }
      if (dateRange) {
        params.beginTime = dateRange.start.toString();
        params.endTime = dateRange.end.toString();
      }
      const resp = await orderService.getList(params);

      if (resp.code === 1) {
        setOrders(resp.data);
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  };

  const handleTake = async (order: Record<string, any>) => {
    try {
      const resp = await orderService.accept({ id: order.id });
      if (resp.code === 1) {
        fetchOrders();
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  };

  const handleReject = async (order: Record<string, any>) => {
    try {
      const resp = await orderService.reject({ id: order.id });
      if (resp.code === 1) {
        fetchOrders();
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  };

  const handleDeliver = async (id: string | number) => {
    try {
      const resp = await orderService.deliver(id);
      if (resp.code === 1) {
        fetchOrders();
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  }

  const handleCancel = async (id: string | number) => {
    try {
      const resp = await orderService.cancel(id);
      if (resp.code === 1) {
        fetchOrders();
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  }

  useEffect(() => {
    fetchOrders();
  }, [selectedStatus]);

  return (
    <div className='bg-[#141A28] text-[#e0e0e0] w-full h-full overflow-auto custom-scrollbar'>
      <div className='min-w-fit'>
        <div className='flex justify-between items-center m-7'>
          <div className='flex flex-row items-end'>
            {orderStatus.map((s: OrderStatus, index: number) => (
              <button
                key={index}
                className={`border-1 py-0.5 px-3 ${selectedStatus === s && 'bg-[#66a2df]'} ${index === 0 && 'rounded-l-sm'
                  } ${index === 5 && 'rounded-r-sm'}`}
                onClick={() => { setOrders({ total: 0, records: [] }); setSelectedStatus(s); setPageNumber(1); setDateRange(null); setSerialNumber(''); setPhone(''); }}
              >
                {s}
              </button>
            ))}
          </div>
        </div>

        <div className='bg-[#1b2537] m-7 p-7 rounded-md'>
          <div className='h-8 flex flex-row gap-3 items-center'>
            <div className='flex flex-row gap-2 items-center'>
              <p>Serial Number:</p>
              <Input
                id="serialNumber"
                type="text"
                variant='bordered'
                className='w-28'
                classNames={styleInput}
                value={serialNumber}
                onChange={(e) => setSerialNumber(e.target.value)}
              />
            </div>
            <div className='flex flex-row gap-2 items-center'>
              <p>Phone:</p>
              <Input
                id="phone"
                type="phone"
                variant='bordered'
                className='w-28'
                classNames={styleInput}
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
              />
            </div>
            <div className='flex flex-row gap-2 items-center'>
              <p>Order Time:</p>
              <div className='relative'
                onMouseEnter={() => setIsHovered(true)} onMouseLeave={() => setIsHovered(false)}>
                <DateRangePicker
                  aria-label='Date-Range-Picker'
                  value={dateRange}
                  onChange={(value: RangeValue<DateValue>) => setDateRange(value)}
                  variant='bordered'
                  className='date-range-picker w-[244px]'
                  calendarProps={{ classNames: stylerCalendar }}
                  selectorIcon={
                    dateRange && isHovered && <div />
                  }
                  classNames={stylePicker} />
                {dateRange && isHovered && <XCircleIcon className="h-4 w-4 absolute right-3 top-2" onClick={() => setDateRange(null)} />}
              </div>
            </div>
            <button onClick={fetchOrders}
              className='bg-[#66a2df] px-3 h-full flex flex-row rounded-md items-center'>
              Search
            </button>
          </div>
          <div className='mt-4'>
            {orders.total === 0 ? (<div className='flex flex-row justify-center items-center h-52'>
              <p className='text-gray-400'>No {selectedStatus !== "All" && `${selectedStatus.toLowerCase()} `}order</p>
            </div>) : (
              <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
                <table className="w-full text-sm text-left rtl:text-right">
                  <thead className="text-xs text-gray-300 uppercase bg-[#141A28]">
                    <tr>
                      {header[selectedStatus].map((h, index) => (
                        <th scope="col" className="px-3 py-3 text-center align-middle" key={index}>
                          {h}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {orders.records.map((order, index) => (
                      <tr className="border-b" key={index}>
                        {header[selectedStatus].map((h, idx) => (h != 'Action' &&
                          <td scope="row" className="px-3 py-4 font-medium text-center align-middle" key={h + idx.toString()}>
                            {h != 'Status' ? order[headerToData[h]] : statusMap[order[headerToData[h]]]}
                          </td>
                        ))}
                        <td scope="row" className="px-3 py-4 text-center align-middle">
                          <div className='flex gap-3 justify-center'>
                            {order.status == 2 && <button onClick={() => handleTake(order)}
                              className="font-medium text-[#66a2df] hover:underline">Take</button>}
                            {order.status == 2 && <button onClick={() => handleReject(order)}
                              className="font-medium text-red-400 hover:underline">Reject</button>}
                            {order.status == 3 && <button onClick={() => handleDeliver(order.id)}
                              className="font-medium text-[#66a2df] hover:underline">Deliver</button>}
                            {order.status == 3 && <button onClick={() => handleCancel(order.id)}
                              className="font-medium text-red-400 hover:underline">Cancel</button>}
                            <button onClick={() => { setId(order.id); onOpen() }}
                              className="font-medium text-[#66a2df] hover:underline">Details</button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>

                <nav className="flex items-center flex-column flex-wrap md:flex-row justify-between pt-4" aria-label="Table navigation">
                  <span className="text-sm font-normal text-gray-400 mb-4 md:mb-0 block w-full md:inline md:w-auto">Showing <span className="font-semibold text-[#e0e0e0]">{(pageNumber - 1) * 10 + 1}-{Math.min(pageNumber * 10, orders.total)}</span> of <span className="font-semibold text-[#e0e0e0]">{orders.total}</span></span>
                  <ul className="inline-flex -space-x-px rtl:space-x-reverse text-sm h-8 bg-[#1b2537] text-[#e0e0e0]">
                    <li>
                      <button
                        onClick={() => setPageNumber(Math.max(1, pageNumber - 1))}
                        className="flex items-center justify-center px-3 h-8 ms-0 leading-tight border border-[#e0e0e0] rounded-s-lg hover:bg-gray-100 hover:text-gray-700">Previous</button>
                    </li>
                    {Array.from({ length: Math.ceil(orders.total / 10) }, (_, index) => (
                      <li key={index}>
                        <button onClick={() => setPageNumber(index + 1)}
                          className={`flex items-center justify-center px-3 h-8 leading-tight border border-[#e0e0e0] hover:text-gray-700 ${pageNumber === index + 1 ? 'bg-[#66a2df] hover:bg-[#5791cb]' : 'hover:bg-gray-100'}`}>
                          {index + 1}
                        </button>
                      </li>
                    ))}
                    <li>
                      <button onClick={() => setPageNumber(Math.min(Math.ceil(orders.total / 10), pageNumber + 1))}
                        className="flex items-center justify-center px-3 h-8 leading-tight border border-[#e0e0e0] rounded-e-lg hover:bg-gray-100 hover:text-gray-700">Next</button>
                    </li>
                  </ul>
                </nav>
              </div>
            )}
          </div>

        </div>

        <OrderModal isOpen={isOpen} onOpen={onOpen} onOpenChange={onOpenChange} id={id} />
      </div>
    </div>
  );
}

export default Page;