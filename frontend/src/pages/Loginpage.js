import {useContext, useState} from "react";
import {AuthContext} from "../context/AuthProvider";

export default function Loginpage() {

    const {authenticate} = useContext(AuthContext)
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleAuth = (event)=> {
        event.preventDefault()
        authenticate(username, password)
        setUsername('')
        setPassword('')
   }

    return (
        <form onSubmit={handleAuth}>
            <input type="text" onChange={event => setUsername(event.target.value)}/>
            <input type="password" onChange={event => setPassword(event.target.value)}/>
            <button>Submit</button>
        </form>
    )
}