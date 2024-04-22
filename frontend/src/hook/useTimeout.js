import { useState, useEffect } from 'react';

export default function useTimeout(initialMinutes, initialSeconds) {
  const [time, setTime] = useState({ minutes: initialMinutes, seconds: initialSeconds });
  const [isActive, setIsActive] = useState(true);

  useEffect(() => {
    const timer = setInterval(() => {
      setTime((prevTime) => {
        if (prevTime.seconds === 0 && prevTime.minutes === 0) {
          clearInterval(timer);
          setIsActive(false);
          return prevTime;
        } if (prevTime.seconds === 0) {
          return { minutes: prevTime.minutes - 1, seconds: 59 };
        }
        return { ...prevTime, seconds: prevTime.seconds - 1 };
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [isActive]);

  function restart() {
    setIsActive(true);
  }

  return { ...time, restart };
}
