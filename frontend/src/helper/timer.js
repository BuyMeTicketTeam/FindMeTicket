export default async function timeOut(seconds, minutes) {
  const response = await new Promise((resolve) => {
    setTimeout(() => {
      if (seconds === 0) {
        resolve({ seconds: 59, minutes: minutes - 1 });
      } else {
        resolve({ seconds: seconds - 1, minutes });
      }
    }, 1000);
  });
  return response;
}
