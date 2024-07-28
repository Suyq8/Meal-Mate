'use server';
import CryptoES from "crypto-es";

const secretKey = process.env.SECRET_KEY;

export async function encrypt(s: string) {
    if (!secretKey) {
        throw new Error('SECRET_KEY is missing');
    }

    console.log('key', secretKey);
    const key = CryptoES.enc.Base64.parse(secretKey);
    const message = CryptoES.enc.Utf8.parse(s);

    return CryptoES.AES.encrypt(message, key, {mode:CryptoES.mode.ECB, padding: CryptoES.pad.Pkcs7}).toString();
}