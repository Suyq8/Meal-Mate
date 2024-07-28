import { useFetch } from '../utils/useFetch';
import { Result } from './Result';

export function useReportService(): IReportService {
    const fetch = useFetch();
    const basePath = '/report';

    return {
        getTurnoverStatistics: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/turnoverStatistics`, undefined, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        getUserStatistics: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/userStatistics`, undefined, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        getOrderStatistics: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/ordersStatistics`, undefined, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        getTop10: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/top10`, undefined, params);
            } catch (error: any) {
                console.error(error);
            }
        },
        exportData: async () => {
            try {
                return await fetch.get(`${basePath}/export`);
            } catch (error: any) {
                console.error(error);
            }
        }
    }
};

interface IReportService {
    getTurnoverStatistics: (params: any) => Promise<Result>;
    getUserStatistics: (params: any) => Promise<Result>;
    getOrderStatistics: (params: any) => Promise<Result>;
    getTop10: (params: any) => Promise<Result>;
    exportData: () => any;
}