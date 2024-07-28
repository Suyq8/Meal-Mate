import { useRouter } from 'next/navigation';
import { getCookie } from './cookie';

export { useFetch };

function useFetch() {
    const router = useRouter();

    return {
        get: request('GET'),
        post: request('POST'),
        put: request('PUT'),
        delete: request('DELETE')
    };

    function request(method: string) {
        return async (url: string, body?: any, params?: any, headers?: any) => {
            const requestOptions: RequestInit = {
                method: method,
                next: { revalidate: 10 }
            };

            const token = await getCookie('token');
            if (token) {
                requestOptions.headers = { 'token': token };
            }

            if (body) {
                requestOptions.headers = { ...requestOptions.headers, 'Content-Type': 'application/json' };
                requestOptions.body = JSON.stringify(body);
            }

            if (headers) {
                requestOptions.headers = { ...requestOptions.headers, ...headers };
            }

            let newURL = `${process.env.NEXT_PUBLIC_BASE_API}${url}`;

            if (params) {
                if ('ids' in params) {
                    newURL += '?ids=';
                    newURL += params.ids;
                } else {
                    const searchParams = new URLSearchParams();

                    for (const [key, value] of Object.entries(params)) {
                        if (value !== undefined && value !== null && value !== '') {
                            searchParams.append(key, value.toString());
                        }
                    }

                    newURL += '?';
                    newURL += searchParams.toString();
                }
            }

            const response = await fetch(newURL, requestOptions);
            return handleResponse(response);
        }
    }

    // helper functions
    async function handleResponse(response: any) {
        const isJson = response.headers?.get('content-type')?.includes('application/json');
        const data = isJson ? await response.json() : await response.blob();

        // check for error response
        if (!response.ok) {
            if (response.status === 401) {
                // api auto logs out on 401 Unauthorized, so redirect to login page
                router.push('/login');
            }

            // get error message from body or default to response status
            const error = (data && data.message) || response.statusText;
            return Promise.reject(error);
        }

        return data;
    }
}