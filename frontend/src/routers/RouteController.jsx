/* eslint-disable react/destructuring-assignment */
/* eslint-disable react/jsx-props-no-spreading */
import { useNavigate } from 'react-router-dom';

export default function RouteController({ access, children }) {
  const navigate = useNavigate();
  console.log('controller');
  if (!access) {
    navigate(-1);
  }
  return children;
}
