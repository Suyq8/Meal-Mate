import { useFetch } from '../utils/useFetch';
import { Result } from './Result';

export function useDashboardService(): IDashboardService {
    const fetch = useFetch();
    const basePath = '/dashboard';

    return {
        getOrderStatistics: async () => {
            try {
                return await fetch.get(`${basePath}/overviewOrders`);
            } catch (error: any) {
                console.error(error);
            }
        },
        getDishStatistics: async () => {
            try {
                return await fetch.get(`${basePath}/overviewDishes`);
            } catch (error: any) {
                console.error(error);
            }
        },
        getMealStatistics: async () => {
            try {
                return await fetch.get(`${basePath}/overviewMeals`);
            } catch (error: any) {
                console.error(error);
            }
        },
        getBusinessStatistics: async () => {
            try {
                return await fetch.get(`${basePath}/businessData`);
            } catch (error: any) {
                console.error(error);
            }
        }
    }
};

interface IDashboardService {
    getOrderStatistics: () => Promise<Result>;
    getDishStatistics: () => Promise<Result>;
    getMealStatistics: () => Promise<Result>;
    getBusinessStatistics: () => Promise<Result>;
}