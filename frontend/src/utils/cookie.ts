'use server'
import { cookies } from 'next/headers';

export async function setCookie(name: string, value: any) {
    cookies().set(name, value);
}

export async function getCookie(name: string) {
    return cookies().get(name)?.value;
}

export async function getCookieJSON(name: string) {
    const value = cookies().get(name)?.value;
    let res = undefined;
    if (value) {
        res = (JSON.parse(value));
    }
    return res;
}

export async function removeCookie(name: string) {
    cookies().delete(name);
}