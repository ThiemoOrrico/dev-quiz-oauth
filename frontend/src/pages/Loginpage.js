import {useState} from "react";

export default function Loginpage({doAuth}) {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleAuth = (event)=> {
        event.preventDefault()
        doAuth(username, password)
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