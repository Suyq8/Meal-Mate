import { useFetch } from '../utils/useFetch';
import { Result } from './Result';

export function useCategoryService(): ICategoryService {
    const fetch = useFetch();
    const basePath = '/category';

    return {
        getList: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/page`, undefined, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        updateStatus: async (params: any) => {
            try {
                return await fetch.post(`${basePath}/status/${params.status}`, undefined, { id: params.id });
            } catch (error: any) {
                console.error(error);
            }
        },
        add: async (data: any) => {
            try {
                return await fetch.post(`${basePath}`, data);
            } catch (error: any) {
                console.error(error);
            }
        },
        update: async (data: any) => {
            try {
                return await fetch.put(`${basePath}`, data);
            } catch (error: any) {
                console.error(error);
            }
        },
        delete: async (id: string|number) => {
            try {
                return await fetch.delete(`${basePath}`, undefined, { id: id });
            } catch (error: any) {
                console.error(error);
            }
        },
        getAll: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/list`, undefined, params);
            } catch (error: any) {
                console.error(error);
            }
        }
    }
};

interface ICategoryService {
    getList: (params: any) => Promise<Result>,
    updateStatus: (params: any) => Promise<Result>,
    add: (data: any) => Promise<Result>,
    update: (data: any) => Promise<Result>,
    delete: (id: string|number) => Promise<Result>,
    getAll: (params: any) => Promise<Result>,
}