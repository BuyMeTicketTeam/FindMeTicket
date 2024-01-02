export default function writeToken(response) {
  if (response.status === 200) {
    response.headers.forEach((value, key) => {
      if (key === 'authorization') {
        localStorage.setItem('JWTtoken', value);
      }
    });
  }
}
