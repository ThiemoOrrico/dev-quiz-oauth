import {useContext} from "react";
import {AuthContext} from "../context/AuthProvider";
import {Route} from 'react-router-dom'

function Redirect() {
    return null;
}

export default function PrivateRoute(props){
    const {token} = useContext(AuthContext)

    return(
        token ? <Route {...props}/> : <Redirect to={"/login"}/>

    )

}