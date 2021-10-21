import { addQuestion, getQuestions } from '../service/devQuizApiService'
import { useEffect, useState } from 'react'

export default function useQuestions() {
  const [questions, setQuestions] = useState([])

  const getAllQuestions = () => {
    getQuestions().then(result => setQuestions(result))
        .catch(err => console.error(err))
  }

  useEffect(() => {
    getAllQuestions()
  }, [])

  const saveQuestion = newQuestion => {
    addQuestion(newQuestion).then(getAllQuestions)
  }
  return {
    getAllQuestions,
    saveQuestion,
    questions,
  }
}
