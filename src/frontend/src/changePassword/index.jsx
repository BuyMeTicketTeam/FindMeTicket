import React, { useState } from 'react';
import Field from '../components/Field';
import Button from '../components/Button';

export default function Index() {
  const [code, onCodeChange] = useState('');
  const [password, onPasswordChange] = useState('');
  const [confirmPassword, onConfirmPasswordChange] = useState('');
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const [minutes, setMinutes] = useState(10);
  const [seconds, setSeconds] = useState(0);
  const [sendAg, onSendAg] = useState(false);
  return (
    <div className="confirm">
      <h1 className="title">Створення нового пароллю</h1>
      <p className="confirm__text">На вашу пошту прийшов лист з кодом для скидання паролю.</p>
      <p className="confirm__text"><b>У вас є 10 хвилин щоб підтвердити його</b></p>
      {error !== '' && <p data-testid="error" className="error">{error}</p>}
      <Field dataTestId="" name="Введіть код з пошти" value={code} type="text" onInputChange={onCodeChange} />
      <Field dataTestId="" name="Новий пароль" value={password} type="password" onInputChange={onPasswordChange} tip="Password must be at least 8 characters and contain number" />
      <Field dataTestId="" name="Повторіть пароль" value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} />
      <Button name="Відправити" className="confirm__btn" onButton={onButton} />
      <button className="confirm__send-again" disabled={minutes > 0 || seconds > 0} onClick={() => onSendAg(true)} type="button">{`Відправити лист знову: ${minutes}:${seconds}`}</button>
    </div>
  );
}
