import axios from 'axios'

export function getQuestions() {
  return axios
    .get('/api/question')
    .then(response => response.data)
}

export function addQuestion(newQuestion) {
  return axios
    .post('/api/question', newQuestion)
    .then(response => response.data)
    .catch(console.error)
}

export function getQuestion(){
  return axios
      .get('/api/question/quiz')
      .then(response => response.data)
      .catch(console.error)
}
export function checkAnswer(question, chosenId){

  const answerValidation = {
    question : question,
    chosenId : chosenId
  }

  return axios
      .post('/api/question/quiz', answerValidation )
      .then(response => response.data)
      .catch(console.error)
}