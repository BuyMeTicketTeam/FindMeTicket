/* eslint-disable no-proto */
/* eslint-disable no-undef */
import writeToken from './responseInterceptor';

describe('writeToken function', () => {
  test('writes JWT token to localStorage if present in the response headers', () => {
    const response = new Response(null, { headers: new Headers({ Authorization: 'Bearer JWTToken123' }) });
    const setItemSpy = jest.spyOn(window.localStorage.__proto__, 'setItem');

    writeToken(response);

    expect(setItemSpy).toHaveBeenCalledWith('JWTtoken', 'Bearer JWTToken123');

    setItemSpy.mockRestore();
  });

  test('does not write anything to localStorage if Authorization header is not present', () => {
    const response = new Response();

    const localStorageMock = {
      setItem: jest.fn(),
    };

    global.localStorage = localStorageMock;

    writeToken(response);

    expect(localStorageMock.setItem).not.toHaveBeenCalled();
  });
});
