import { useRouter } from 'next/navigation';
import { useFetch } from '../utils/useFetch';
import { Result } from './Result';

export function useEmployeeService(): IUserService {
	const fetch = useFetch();
	const router = useRouter();
	const basePath = '/employee';

	return {
		login: async (userName, password) => {
			try {
				return await fetch.post(`${basePath}/login`, { userName, password });
			} catch (error: any) {
				console.error(error);
			}
		},
		logout: async () => {
			try {
				return await fetch.post(`${basePath}/logout`);
			} catch (error: any) {
				console.error(error);
			}
		},
		getList: async (params: any) => {
			try {
				return await fetch.get(`${basePath}/page`, undefined, params);
			} catch (error: any) {
				console.error(error);
			}
		},
		updateEmployeeStatus: async (params: any) => {
			try {
				return await fetch.post(`${basePath}/status/${params.status}`, undefined, { id: params.id });
			} catch (error: any) {
				console.error(error);
			}
		},
		addEmployee: async (data: any) => {
			try {
				return await fetch.post(`${basePath}`, data);
			} catch (error: any) {
				console.error(error);
			}
		},
		updateEmployee: async (data: any) => {
			try {
				return await fetch.put(`${basePath}`, data);
			} catch (error: any) {
				console.error(error);
			}
		},
		getEmployeeById: async (id: string | number) => {
			try {
				return await fetch.get(`${basePath}/${id}`);
			} catch (error: any) {
				console.error(error);
			}
		}
	}
};

interface IUserService {
	login: (username: string, password: string) => Promise<Result>,
	logout: () => Promise<Result>,
	getList: (params: any) => Promise<Result>,
	updateEmployeeStatus: (params: any) => Promise<Result>,
	addEmployee: (data: any) => Promise<Result>,
	updateEmployee: (data: any) => Promise<Result>,
	getEmployeeById: (id: string | number) => Promise<Result>
}