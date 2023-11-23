/* eslint-disable no-proto */
/* eslint-disable no-undef */
import writeToken from './writeToken';

describe('writeToken function', () => {
  test('writes JWT token to localStorage if present in the response headers', () => {
    // Создаем mock-объект response с заголовком 'Authorization'
    const response = new Response(null, { headers: new Headers({ Authorization: 'Bearer JWTToken123' }) });

    // Шпионим за методом setItem из localStorage
    const setItemSpy = jest.spyOn(window.localStorage.__proto__, 'setItem');

    // Вызываем функцию writeToken с mock-объектом response
    writeToken(response);

    // Проверяем, что JWT token был записан в localStorage
    expect(setItemSpy).toHaveBeenCalledWith('JWTtoken', 'Bearer JWTToken123');

    // Восстанавливаем оригинальный метод после теста
    setItemSpy.mockRestore();
  });

  test('does not write anything to localStorage if Authorization header is not present', () => {
    // Создаем mock-объект response без заголовка 'Authorization'
    const response = new Response();

    // Создаем mock-объект localStorage
    const localStorageMock = {
      setItem: jest.fn(),
    };

    // Заменяем глобальный объект localStorage на наш mock-объект
    global.localStorage = localStorageMock;

    // Вызываем функцию writeToken с mock-объектом response
    writeToken(response);

    // Проверяем, что localStorage.setItem не был вызван
    expect(localStorageMock.setItem).not.toHaveBeenCalled();
  });
});
