import Cookies from 'universal-cookie';
import responseInterceptor from './responseInterceptor';

jest.mock('universal-cookie'); // Mock the Cookies library

describe('responseInterceptor', () => {
  const mockResponse = {
    status: 200,
    headers: new Headers({
      authorization: 'Bearer 12345',
      rememberme: 'yes',
      userid: '42',
    }),
  };

  beforeEach(() => {
    Cookies.mockClear(); // Clear any mock calls before each test
    localStorage.clear();
  });

  it('should set JWT token in localStorage when authorization header is present', () => {
    responseInterceptor(mockResponse);
    expect(localStorage.getItem('JWTtoken')).toBe('Bearer 12345');
  });

  it('should not set cookies or local storage if response status is not 200', () => {
    const mockErrorResponse = { status: 401 };
    responseInterceptor(mockErrorResponse);
    expect(localStorage.getItem('JWTtoken')).toBeNull();
  });
});
