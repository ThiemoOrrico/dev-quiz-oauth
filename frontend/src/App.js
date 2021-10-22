import Header from './components/Header'
import './App.css'
import {Route, Switch} from 'react-router-dom'
import Homepage from './pages/Homepage'
import AddQuestion from './pages/Add-Question'
import useQuestions from './hooks/useQuestions'
import Play from "./pages/Play";
import {useContext, useEffect, useState} from "react";
import {getQuestion} from "./service/devQuizApiService";
import Loginpage from "./pages/Loginpage";
import AuthProvider, {AuthContext} from "./context/AuthProvider";

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
    }, []);

    return (

        <div className="App">
            <Header/>
            <Switch>
                <Route exact path="/">
                    <Homepage questions={questions}/>
                </Route>
                <Route exact path="/add-question">
                    <AddQuestion saveQuestion={saveQuestion}/>
                </Route>
                <Route path="/login">
                    <Loginpage />
                </Route>
                <Route path="/play">
                    {playQuestion && <Play question={playQuestion} playNext={getNextQuestion}/>}
                </Route>
            </Switch>
        </div>

    )
}

export default App
