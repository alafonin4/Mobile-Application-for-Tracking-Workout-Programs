import axios from "axios";
import { API_URL } from "../../constants/api_url";

export const sign_up = async (firstName, lastName, email, password) => {
    try {
        const response = await axios.post(`http://192.168.116.232:8083/api/auth/register`, { 
            firstName, 
            lastName, 
            email, 
            password
        });
        return response.data;
    } catch (error) {
        console.error("Login error:", error);
        throw error;
    }
};
