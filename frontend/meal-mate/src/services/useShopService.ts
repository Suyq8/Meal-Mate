import { useFetch } from '../utils/useFetch';
import { Result } from './Result';

export function useShopservice(): IShopService {
    const fetch = useFetch();
    const basePath = '/shop';

    return {
        updateStatus: async (params: any) => {
            try {
                return await fetch.put(`${basePath}/${params.status}`);
            } catch (error: any) {
                console.error(error);
            }
        },
        getStatus: async () => {
            try {
                return await fetch.get(`${basePath}/status`);
            } catch (error: any) {
                console.error(error);
            }
        }
    }
};

interface IShopService {
    updateStatus: (params: any) => Promise<Result>,
    getStatus: () => Promise<Result>
}