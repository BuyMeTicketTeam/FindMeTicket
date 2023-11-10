import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Field from '../components/Field';
import Button from '../components/Button';
import ListTip from './ListTip';
import './register.css';

export default function Register() {
  const [nickname, onNicknameChange] = useState('');
  const [email, onEmailChange] = useState('');
  const [password, onPasswordChange] = useState('');
  const [confirmPassword, onConfirmPasswordChange] = useState('');
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
  return (
    <div data-testid="register" className="register">
      <h1 className="title">Registration</h1>
      {error !== '' && <p data-testid="error" className="error">{error}</p>}
      <Field dataTestId="nickname-input" tipDataTestId="nickname-tip" name="Nickname" value={nickname} type="text" onInputChange={onNicknameChange} placeholder="Svillana2012" tip={<ListTip />} />
      <Field dataTestId="email-input" name="Email" value={email} type="email" onInputChange={onEmailChange} tip="Email must contain @" />
      <Field dataTestId="password-input" name="Password" value={password} type="password" onInputChange={onPasswordChange} tip="Password must be at least 8 characters and contain number" />
      <Field dataTestId="confirm-pass-input" name="Confirm password" value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} />
      <input data-testid="checkbox" id="policy" type="checkbox" className="checkbox__field" onClick={() => onPolicy(!policy)} />
      <label htmlFor="policy" className="checkbox">
        I agree with the
        <a href="/">privacy policy</a>
      </label>
      <Button dataTestId="register-btn" name="Register" onButton={onButton} />
    </div>
  );
}
