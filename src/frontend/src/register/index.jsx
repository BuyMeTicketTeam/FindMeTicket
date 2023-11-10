import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Field from '../components/Field';
import Button from '../components/Button';
import './register.css';

export default function Register() {
  const [nickname, onNicknameChange] = useState('');
  const [nicknameError, onNicknameError] = useState(false);
  const [email, onEmailChange] = useState('');
  const [emailError, onEmailError] = useState(false);
  const [password, onPasswordChange] = useState('');
  const [passwordError, onPasswordError] = useState(false);
  const [confirmPassword, onConfirmPasswordChange] = useState('');
  const [confirmPasswordError, onConfirmPasswordError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const [policy, onPolicy] = useState(false);
  const navigate = useNavigate();
  useEffect(() => {
    if (button && policy) {
      fetch('/userRegister', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nickname,
          email,
          password,
          confirmPassword,
        }),
      })
        .then((response) => {
          if (response.ok) {
            onError('Some error');
            onButton(false);
          } else {
            navigate('/confirm');
          }
        });
    } else if (button && !policy) {
      onButton(false);
      onError('You need to agree with the privacy policy');
    }
  }, [button]);
  useEffect(() => {
    if (button === true) {
      onNicknameError(false);
      onEmailError(false);
      onPasswordError(false);
      onConfirmPasswordError(false);
      onButton(false);
      if
      (nickname.match(/^[a-zA-Z0-9\s]{5,20}$/) === null) {
        onError('Поле nickname заповнено не вірно');
        onNicknameError(true);
      } else if
       (email.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/) === null) {
        onError('Поле email заповнено не вірно');
        onEmailError(true);
      } else if (password.match(/^(?=.*[A-Za-z])(?=.*\d).{8,30}$/) === null) {
        onError('Поле паролю заповнено не вірно');
        onPasswordError(true);
      } else if (password !== confirmPassword) {
        onError('Паролі не збігаються');
        onConfirmPasswordError(true);
    
      } else {
        fetch('/userRegister', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
          body: JSON.stringify({
            nickname,
            email,
            password,
            confirmPassword,
          }),
        })
          
      }
    }
  }, [button]);
 
  return (
    <div data-testid="register" className="register">
      <h1 className="title">Registration</h1>
      {error !== '' && <p data-testid="error" className="error">{error}</p>}
      <Field error={nicknameError} dataTestId="nickname-input" tipDataTestId="nickname-tip" name="Nickname" value={nickname} type="text" onInputChange={onNicknameChange} tip="Name must contain at least one character" />
      <Field  error={emailError}  dataTestId="email-input" name="Email" value={email} type="email" onInputChange={onEmailChange} tip="Email must contain @" />
      <Field error={passwordError} dataTestId="password-input" name="Password" value={password} type="password" onInputChange={onPasswordChange} tip="Password must be at least 8 characters and contain number" />
      <Field error={confirmPasswordError} dataTestId="confirm-pass-input" name="Confirm password" value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} />
      <input data-testid="checkbox" id="policy" type="checkbox" className="checkbox__field" onClick={() => onPolicy(!policy)} />
      <label htmlFor="policy" className="checkbox">
        I agree with the
        <a href="/">privacy policy</a>
      </label>
      <Button dataTestId="register-btn" name="Register" onButton={onButton} />
    </div>
  );
}
