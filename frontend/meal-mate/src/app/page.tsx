'use client'
import { ChevronRightIcon } from '@heroicons/react/24/outline';
import { format, set } from 'date-fns';
import { useRouter } from 'next/navigation';
import { PlusCircleIcon } from '@heroicons/react/24/outline';
import { useEffect, useState } from 'react';
import { AppRouterInstance } from 'next/dist/shared/lib/app-router-context.shared-runtime';
import { useDashboardService } from '@/services/useDashboardService';
import { useOrderService } from '@/services/useOrderService';
import { useDisclosure } from '@nextui-org/react';
import OrderModal from '@/components/OrderModal';
import DishModal from '@/components/DishModal';
import MealModal from '@/components/MealModal';

export default function Home() {
  const currentDate: string = format(new Date(), 'MMM dd, yyyy');
  const router: AppRouterInstance = useRouter();
  const [orderType, setOrderType] = useState<number>(0); // 0: Pending Orders, 1: Accepted Orders
  const [businessData, setBusinessData] = useState<Record<string, any>>({});
  const [orderData, setOrderData] = useState<Record<string, any>>({});
  const [overviewDishes, setOverviewDishes] = useState<Record<string, any>>({});
  const [overviewMeals, setOverviewMeals] = useState<Record<string, any>>({});
  const [orders, setOrders] = useState<{ total: number, records: Record<string, any>[] }>({ total: 0, records: [] });
  const [id, setId] = useState<number | string>('');
  const [pageNumber, setPageNumber] = useState<number>(1);
  const [orderCount, setOrderCount] = useState<Record<string, any>>({});
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const { isOpen: isOpen1, onOpen: onOpen1, onOpenChange: onOpenChange1 } = useDisclosure();
  const { isOpen: isOpen2, onOpen: onOpen2, onOpenChange: onOpenChange2 } = useDisclosure();
  const dashboardService = useDashboardService();
  const orderService = useOrderService();

  const handleStatisitcsClick = () => {
    router.push('/statistics');
  };

  const handleOrderClick = () => {
    router.push('/order');
  };

  const handleDishClick = () => {
    router.push('/dish');
  };

  const handleMealClick = () => {
    router.push('/meal');
  };

  const displayBusinessData = {
    'Turnover': 'turnover', 'Valid Order Count': 'validOrderCount',
    'Order Completion Rate': 'orderCompletionRate', 'Unit Price': 'unitPrice', 'New Users': 'newUsers'
  };

  const displayOrderData = {
    'Pending Orders': 'waitingOrders', 'Accepted Orders': 'deliveringOrders',
    'Completed Orders': 'completedOrders', 'Cancelled Orders': 'cancelledOrders', 'All Orders': 'allOrders'
  };

  const displayOverviewDishes = {
    'Selling': 'selling', 'Not Selling': 'stopSelling'
  };

  const displayOverviewMeals = {
    'Selling': 'selling', 'Not Selling': 'stopSelling'
  };

  const header: Record<string, string[]> = {
    'Pending': ['Serial Number', 'Dishes', 'Address', 'Expected Delivery Time', 'Price', 'Note', 'Tableware', 'Action'],
    'Accepted': ['Serial Number', 'Dishes', 'Address', 'Expected Delivery Time', 'Note', 'Tableware', 'Action']
  };

  const headerToData: Record<string, string> = {
    'Serial Number': 'serialNumber',
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

  const fetchBusinessData = async () => {
    try {
      const resp = await dashboardService.getBusinessStatistics();
      if (resp.code === 1) {
        setBusinessData(resp.data);
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  };

  const fetchOrderData = async () => {
    try {
      const resp = await dashboardService.getOrderStatistics();
      if (resp.code === 1) {
        setOrderData(resp.data);
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  };

  const fetchDishData = async () => {
    try {
      const resp = await dashboardService.getDishStatistics();
      if (resp.code === 1) {
        setOverviewDishes(resp.data);
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  };

  const fetchMealData = async () => {
    try {
      const resp = await dashboardService.getMealStatistics();
      if (resp.code === 1) {
        setOverviewMeals(resp.data);
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  };

  const fetchOrders = async () => {
    try {
      const resp = await orderService.getList({ page: 1, pageSize: 10, status: orderType + 2 });
      if (resp.code === 1) {
        setOrders(resp.data);
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  }

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

  const getOrderStatistics = async () => {
    try {
      const resp = await orderService.getOrderCount();
      if (resp.code === 1) {
        setOrderCount(resp.data);
      } else {
        console.error(resp.msg);
      }
    } catch (error: any) {
      console.error(error);
    }
  }

  useEffect(() => {
    fetchBusinessData();
    fetchOrderData();
    fetchDishData();
    fetchMealData();
  }, []);

  useEffect(() => {
    fetchOrders();
    getOrderStatistics();
  }, [orderType]);

  return (
    <div className='bg-[#141A28] text-[#e0e0e0] w-full h-full overflow-auto custom-scrollbar'>
      <div className='min-w-fit'>
        {/* bussiness data */}
        <div className='bg-[#1b2537] m-7 rounded-md p-5'>
          <div className='flex flex-row justify-between'>
            <div className='flex flex-row gap-2 items-end'>
              <p className='font-semibold'>Today's Statistics</p>
              <p className='text-sm font-semibold text-gray-400'>{currentDate}</p>
            </div>

            <button className='flex flex-row items-center gap-2' onClick={handleStatisitcsClick}>
              <p>Details</p>
              <ChevronRightIcon className='h-4 w-4' />
            </button>
          </div>
          <div className='flex flex-row justify-between gap-3'>
            {Object.entries(displayBusinessData).map(([key, value]) => (
              <div key={key} className='px-5 py-4 bg-[#141A28] w-52 rounded-md mt-2'>
                <p className='text-sm text-[#c4daaf]'>{key}</p>
                <p className='text-2xl'>{(key === 'Turnover' || key === 'Unit Price') ? `\$ ${businessData[value]}` : (key === 'Order Completion Rate' ? `${businessData[value] * 100} %` : businessData[value])}</p>
              </div>
            ))}
          </div>
        </div>

        {/* order data */}
        <div className='bg-[#1b2537] m-7 rounded-md p-5'>
          <div className='flex flex-row justify-between'>
            <div className='flex flex-row gap-2 items-end'>
              <p className='font-semibold'>Order Management</p>
              <p className='text-sm font-semibold text-gray-400'>{currentDate}</p>
            </div>

            <button className='flex flex-row items-center gap-2' onClick={handleOrderClick}>
              <p>Details</p>
              <ChevronRightIcon className='h-4 w-4' />
            </button>
          </div>
          <div className='flex flex-row justify-between gap-3'>
            {Object.entries(displayOrderData).map(([key, value]) => (
              <div key={key} className='px-5 py-4 bg-[#141A28] w-52 rounded-md mt-2 flex flex-row justify-between items-center'>
                <p className='text-sm text-[#c4daaf] w-16 px-2'>{key}</p>
                <p className={`text-2xl px-4 ${(key === 'Waiting Orders' || key === 'Delivered Orders') && "text-red-400"}`}>{orderData[value]}</p>
              </div>
            ))}
          </div>
        </div>

        {/* overview dishes & meals */}
        <div className='grid grid-cols-2 m-7 gap-7'>
          {/*overview dishes*/}
          <div className='bg-[#1b2537] rounded-md p-5'>
            <div className='flex flex-row justify-between'>
              <p className='font-semibold'>Dish Management</p>
              <button className='flex flex-row items-center gap-2' onClick={handleDishClick}>
                <p>Details</p>
                <ChevronRightIcon className='h-4 w-4' />
              </button>
            </div>

            <div className='flex flex-row justify-between'>
              {Object.entries(displayOverviewDishes).map(([key, value]) => (
                <div key={key} className='px-5 py-4 bg-[#141A28] w-40 rounded-md mt-2 flex flex-row justify-between items-center'>
                  <p className='text-sm text-[#c4daaf] px-2'>{key}</p>
                  <p className='text-2xl px-4'>{overviewDishes[value]}</p>
                </div>
              ))}
              <button onClick={onOpen1}
                className='bg-[#66a2df] px-1 py-4 mt-2 rounded-md flex flex-col items-center justify-center'>
                <PlusCircleIcon className='h-6 w-6' />
                <p>Add New Dishes</p>
              </button>
            </div>
          </div>

          {/*overview meals*/}
          <div className='bg-[#1b2537] rounded-md p-5'>
            <div className='flex flex-row justify-between'>
              <p className='font-semibold'>Meal Management</p>
              <button className='flex flex-row items-center gap-2' onClick={handleMealClick}>
                <p>Details</p>
                <ChevronRightIcon className='h-4 w-4' />
              </button>
            </div>

            <div className='flex flex-row justify-between'>
              {Object.entries(displayOverviewMeals).map(([key, value]) => (
                <div key={key} className='px-5 py-4 bg-[#141A28] w-40 rounded-md mt-2 flex flex-row justify-between items-center'>
                  <p className='text-sm text-[#c4daaf] px-2'>{key}</p>
                  <p className='text-2xl px-4'>{overviewMeals[value]}</p>
                </div>
              ))}
              <button onClick={onOpen2}
                className='bg-[#66a2df] px-1 py-4 mt-2 rounded-md flex flex-col items-center justify-center'>
                <PlusCircleIcon className='h-6 w-6' />
                <p>Add New Meals</p>
              </button>
            </div>
          </div>
        </div>

        {/* order details */}
        <div className='bg-[#1b2537] m-7 rounded-md p-5'>
          <div className='flex flex-row justify-between'>
            <p className='font-semibold'>Order Details</p>
            <div className="flex flex-row">
              {Array.from({ length: 2 }, (_, index) => (
                <button key={index} className={`border-1 w-28 rounded-sm flex flex-row items-center justify-center gap-1 ${orderType === index && "bg-[#66a2df]"}`}
                  onClick={() => setOrderType(index)}>
                  {index ? 'Accepted' : 'Pending'}
                  {orderType === 1 && orderCount?.toBeConfirmed > 0 && <div className='h-4 w-4 rounded-full bg-red-400 text-sm flex items-center justify-center'>{orderCount?.toBeConfirmed}</div>}
                  {orderType === 0 && orderCount?.confirmed > 0 && <div className='h-4 w-4 rounded-full bg-red-400 text-sm flex items-center justify-center'>{orderCount?.confirmed}</div>}
                </button>
              ))}
            </div>
          </div>

          <div className='mt-4'>
            {orders.total === 0 ? (<div className='flex flex-row justify-center items-center h-52'>
              <p className='text-gray-400'>No {orderType ? "pending" : "accepted"}order</p>
            </div>) : (
              <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
                <table className="w-full text-sm text-left rtl:text-right">
                  <thead className="text-xs text-gray-300 uppercase bg-[#141A28]">
                    <tr>
                      {header[orderType ? "Pending" : "Accepted"].map((h, index) => (
                        <th scope="col" className="px-3 py-3 text-center align-middle" key={index}>
                          {h}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {orders.records.map((order, index) => (
                      <tr className="border-b" key={index}>
                        {header[orderType ? "Pending" : "Accepted"].map((h, idx) => (h != 'Action' &&
                          <td scope="row" className="px-3 py-4 font-medium text-center align-middle" key={h + idx.toString()}>
                            {order[headerToData[h]]}
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
        <DishModal isOpen={isOpen1} onOpen={onOpen1} onOpenChange={onOpenChange1} refresh={fetchDishData} />
        <MealModal isOpen={isOpen2} onOpen={onOpen2} onOpenChange={onOpenChange2} refresh={fetchMealData} />
      </div>
    </div>

  );
}