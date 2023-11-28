export default function writeToken(response) {
  if (response.status === 200 && response.headers.get('Authorization')) {
    localStorage.setItem('JWTtoken', response.headers.get('Authorization'));
  }
}
