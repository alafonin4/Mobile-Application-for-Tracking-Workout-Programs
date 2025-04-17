import axios from "axios";
import { API_URL } from "../../constants/api_url";

export const login = async (email, password) => {
    try {
        const response = await fetch(`http://192.168.116.232:8083/api/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });
    
        return await response.json();
    } catch (error) {
        console.error("Ошибка с fetch:", error);
    }
    
};
