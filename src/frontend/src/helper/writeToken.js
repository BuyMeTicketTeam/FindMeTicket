export default function writeToken(response) {
  if (response.headers.get('Authorization')) {
    localStorage.setItem('JWTtoken', response.headers.get('Authorization'));
  }
}
