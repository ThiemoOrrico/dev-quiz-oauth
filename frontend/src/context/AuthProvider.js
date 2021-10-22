import {createContext, useState} from "react";
import {useHistory} from "react-router-dom";
import axios from "axios";

export const AuthContext = createContext({})

export default function AuthProvider({children}) {
    const [token, setToken] = useState()
    const history = useHistory();

   const authenticate = (username, password) => {
        const user = {
            username: username,
            password: password
        }
        return axios
            .post('/auth/login', user)
            .then(response => response.data)
            .then(result => setToken(result))
            .then(()=>history.push('/'))
            .catch(error => console.error(error.message()))
    }

    return (
        <AuthContext.Provider value={{token, authenticate}}>{children}</AuthContext.Provider>

    )

}