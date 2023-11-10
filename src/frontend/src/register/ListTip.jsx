import React from 'react';

export default function ListTip() {
  return (
    <>
      Ваш nickname повинен відповідати наступним вимогам:
      <ul style={{ marginLeft: '20px', marginTop: '10px' }}>
        <li>5-20 буквенно-цифрових символів</li>
        <li>Малі та великі літери</li>
        <li>Тільки латинські символи</li>
        <li>Пробіли допускаються</li>
      </ul>
    </>
  );
}
