import React, { useState, useEffect } from 'react';
import Field from '../components/Field';
import Button from '../components/Button';

export default function Register() {
  const [nickname, onNicknameChange] = useState('');
  const [email, onEmailChange] = useState('');
  const [password, onPasswordChange] = useState('');
  const [confirmPassword, onConfirmPasswordChange] = useState('');
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const [policy, onPolicy] = useState(false);
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
            window.location.href = '/confirm';
          }
        });
    } else if (button && !policy) {
      onButton(false);
      onError('You need to agree with the privacy policy');
    }
  }, [button]);
  return (
    <div className="register">
      <h1 className="title">Registration</h1>
      {error !== '' && <p className="error">{error}</p>}
      <Field name="Nickname" value={nickname} type="text" onInputChange={onNicknameChange} tip="Name must contain at least one character" />
      <Field name="Email" value={email} type="email" onInputChange={onEmailChange} tip="Email must contain @" />
      <Field name="Password" value={password} type="password" onInputChange={onPasswordChange} tip="Password must be at least 8 characters and contain number" />
      <Field name="Confirm password" value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} />
      <input id="policy" type="checkbox" className="checkbox__field" onClick={() => onPolicy(!policy)} />
      <label htmlFor="policy" className="checkbox">
        I agree with the
        <a href="/">privacy policy</a>
      </label>
      <Button name="Register" onButton={onButton} />
    </div>
  );
}
