/* eslint-disable max-len */
import { useRef, useCallback } from 'react';
import { useLazyTypeAheadAPIQuery } from '../services/typeAhead';

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
  const [typeAhead] = useLazyTypeAheadAPIQuery();
  const getCities = useCallback(async (inputValue, updateState) => {
    if (inputValue.length > 1) {
      clearInterval(timerId.current);
      const result = await new Promise((resolve) => {
        timerId.current = setTimeout(async () => {
          try {
            const response = await typeAhead(inputValue).unwrap();
            console.log(response);
            resolve(response.map(transformData));
          } catch (error) {
            console.log(error);
            resolve([]);
          }
        }, 500);
      });
      updateState(result[0]);
      return result;
    }
    return [];
  }, []);

  return getCities;
}
