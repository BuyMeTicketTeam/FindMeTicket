import { useRef, useCallback } from 'react';
import { getI18n } from 'react-i18next';
import makeQuerry from '../helper/querry';

export default function useGetCities() {
  function transformData(item) {
    switch (true) {
      case item.siteLanguage === 'ua' && item.cityEng !== null:
        return ({ value: item.cityUa, label: `${item.cityUa} (${item.cityEng}), ${item.country}` });
      case item.siteLanguage === 'ua' && item.cityEng === null:
        return ({ value: item.cityUa, label: `${item.cityUa}, ${item.country}` });
      case item.siteLanguage === 'eng' && item.cityUa === null:
        return ({ value: item.cityEng, label: `${item.cityEng}, ${item.country}` });
      default:
        return ({ value: item.cityEng, label: `${item.cityEng} (${item.cityUa}), ${item.country}` });
    }
  }

  const timerId = useRef();
  const getCities = useCallback(async (inputValue, updateState) => {
    if (inputValue.length > 1) {
      clearInterval(timerId.current);
      const result = await new Promise((resolve) => {
        timerId.current = setTimeout(async () => {
          const response = await makeQuerry('typeAhead', JSON.stringify({ startLetters: inputValue }), { 'Content-language': getI18n().language.toLowerCase() });
          const responseBody = response.status === 200 ? response.body.map(transformData) : [];
          resolve(responseBody);
        }, 500);
      });
      updateState(result[0]);
      return result;
    }
    return [];
  }, []);

  return getCities;
}
