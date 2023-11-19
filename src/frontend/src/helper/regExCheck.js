export function passwordCheck(password) {
  return (password.match(/^(?=.*[A-Za-z])(?=.*\d).{8,30}$/) === null);
}
export function emailCheck(email) {
  return (email.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,50}$/) === null);
}
export function codeCheck(code) {
  return (code.match(/^[a-zA-Z0-9\s]{5}$/) === null);
}
export function nicknameCheck(nickname) {
  return (nickname.match(/^[a-zA-Z0-9\s]{5,20}$/) === null);
}
