import { useFetch } from '../utils/useFetch';
import { Result } from './Result';

export function useCommonservice(): ICommonService {
    const fetch = useFetch();
    const basePath = '/common';

    return {
        download: async (params: any) => {
            try {
                return await fetch.get(`${basePath}/download`, undefined, params, {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'});
            } catch (error: any) {
                console.error(error);
            }
        },
        upload: async (file: any) => {
            try {
                return await fetch.post(`${basePath}/upload`, file);
            } catch (error: any) {
                console.error(error);
            }
        }
    }
};

interface ICommonService {
    download: (params: any) => Promise<Result>,
    upload: (file: any) => Promise<Result>
}