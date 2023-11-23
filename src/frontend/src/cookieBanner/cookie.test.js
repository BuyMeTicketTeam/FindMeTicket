/* eslint-disable no-undef */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import CookieBanner from './cookie';

describe('CookieBanner component', () => {
  beforeEach(() => {
    // Очищаем localStorage перед каждым тестом
    localStorage.clear();
  });

  test('renders CookieBanner component and accepts cookies', () => {
    render(<CookieBanner />);

    // Проверяем, что баннер рендерится
    const bannerElement = screen.getByText('Этот веб-сайт использует куки для улучшения вашего опыта.');
    expect(bannerElement).toBeInTheDocument();

    // Проверяем, что изображение куки рендерится
    const cookieImgElement = screen.getByAltText('cookie');
    expect(cookieImgElement).toBeInTheDocument();

    // Проверяем, что кнопка "Принять куки" рендерится
    const acceptButton = screen.getByText('Принять куки');
    expect(acceptButton).toBeInTheDocument();

    // Кликаем на кнопку "Принять куки"
    fireEvent.click(acceptButton);

    // Проверяем, что после клика на кнопку, компонент не рендерится
    expect(screen.queryByTestId('cookie-banner')).not.toBeInTheDocument();

    // Проверяем, что в localStorage установлен флаг acceptedCookies в 'true'
    expect(localStorage.getItem('acceptedCookies')).toBe('true');
  });
});
