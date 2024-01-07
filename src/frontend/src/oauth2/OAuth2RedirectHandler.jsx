/* eslint-disable react/react-in-jsx-scope */
/* eslint-disable no-useless-escape */
/* eslint-disable no-restricted-globals */
/* eslint-disable no-param-reassign */
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useEffect } from 'react';

export default function OAuth2RedirectHandler({ updateAuthValue }) {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const token = searchParams.get('token');

    if (token) {
      localStorage.setItem('JWTtoken', token);
      updateAuthValue(true);
      navigate('/user/123123123');
    } else {
      navigate('/login');
    }
  }, [searchParams, navigate]);

  return <>Loading...</>;
}
