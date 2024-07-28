import { useFetch } from '../utils/useFetch';
import { Result } from './Result';

export function useOrderService(): IOrderService {
    const fetch = useFetch();
    const basePath = '/order';

    return {
        getList: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/conditionSearch`, undefined, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        getDetailById: async (id: string | number) => {
            try {
                return await fetch.get(`${basePath}/details/${id}`);
            } catch (error: any) {
                console.error(error);
            }
        },
        deliver: async (id: string | number) => {
            try {
                return await fetch.put(`${basePath}/deliver/${id}`);
            } catch (error: any) {
                console.error(error);
            }
        },
        complete: async (id: string | number) => {
            try {
                return await fetch.put(`${basePath}/complete/${id}`);
            } catch (error: any) {
                console.error(error);
            }
        },
        cancel: async (params: any) => {
            try {
                return await fetch.put(`${basePath}/cancel`, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        accept: async (params: any) => {
            try {
                return await fetch.put(`${basePath}/confirm`, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        reject: async (params: any) => {
            try {
                return await fetch.put(`${basePath}/rejection`, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        getOrderCount: async () => {
            try {
                return await fetch.get(`${basePath}/statistics`);
            } catch (error: any) {
                console.error(error);
            }
        }
    }
};

interface IOrderService {
    getList: (params: any) => Promise<Result>,
    getDetailById: (id: string | number) => Promise<Result>,
    deliver: (id: string | number) => Promise<Result>,
    complete: (id: string | number) => Promise<Result>,
    cancel: (params: any) => Promise<Result>,
    accept: (params: any) => Promise<Result>,
    reject: (params: any) => Promise<Result>,
    getOrderCount: () => Promise<Result>
}