import Header from './components/Header'
import './App.css'
import {Route, Switch, useLocation, useParams} from 'react-router-dom'
import Homepage from './pages/Homepage'
import AddQuestion from './pages/Add-Question'
import useQuestions from './hooks/useQuestions'
import Play from "./pages/Play";
import {useContext, useEffect, useState} from "react";
import {getQuestion, postAccessTokenToGithub} from "./service/devQuizApiService";
import Loginpage from "./pages/Loginpage";
import AuthProvider, {AuthContext} from "./context/AuthProvider";
import PrivateRoute from "./routing/PrivateRoute";

function GithubRedirect() {
    const query = useQuery()
    const code = query.get("code");
    postAccessTokenToGithub(code).then(console.log)
    return code
}



function useQuery() {
    return new URLSearchParams(useLocation().search);
}




function App() {

    const [playQuestion, setPlayQuestion] = useState()
    const {questions, saveQuestion} = useQuestions()
    const {token} = useContext(AuthContext)

    const getNextQuestion = () => {
        getQuestion(token).then(result => {
            setPlayQuestion(result)
        })
    }

    useEffect(() => {
        getNextQuestion();
    }, [token]);

    return (

        <div className="App">
            <Header/>
            <Switch>
                <PrivateRoute exact path="/">
                    <Homepage questions={questions}/>
                </PrivateRoute>
                <PrivateRoute exact path="/add-question">
                    <AddQuestion saveQuestion={saveQuestion}/>
                </PrivateRoute>
                <Route path="/login">
                    <Loginpage />
                </Route>

                <Route path="/oauth/github/:gitHubOAuth:code">
                    <GithubRedirect/>
                </Route>


                <PrivateRoute path="/play">
                    {playQuestion && <Play question={playQuestion} playNext={getNextQuestion}/>}
                </PrivateRoute>
            </Switch>
        </div>

    )
}

export default App
