import axios from 'axios'

const getHeader = (token) => {
    return {
        headers: {
            Authorization: `Bearer ${token}`
        },
    }
}

export function getQuestions(token) {
    return axios
        .get('/api/question', getHeader(token))
        .then(response => response.data)
}

export function addQuestion(newQuestion, token) {
    return axios
        .post('/api/question', newQuestion, getHeader(token))
        .then(response => response.data)
        .catch(console.error)
}

export function getQuestion(token) {
    return axios
        .get('/api/question/quiz', getHeader(token))
        .then(response => response.data)
        .catch(console.error)
}

export function checkAnswer(question, chosenId, token) {

    const answerValidation = {
        question: question,
        chosenId: chosenId
    }

    return axios
        .post('/api/question/quiz', answerValidation, getHeader(token))
        .then(response => response.data)
        .catch(console.error)
}

function postCodeToGetAccessTokenFromGithub (code){
    const urlGithub = "https://github.com/login/oauth/access_token?" +
        "client_id=f686cbea3ea2b20973bd&" +
        "client_secret=88f027c081f1d0a49f350df35148584fd807a793&" +
        "code=" + code;

    return axios
        .post(urlGithub)
        .then(response => response.data)
        .catch(console.error)

}


export function postAccessTokenToGithub(code){
   return postCodeToGetAccessTokenFromGithub(code)

}