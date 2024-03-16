import React, { useState } from 'react';
import AsyncSelect from 'react-select/async';
import { useTranslation } from 'react-i18next';
import useGetCities from '../hook/useGetCities';

export default function SearchCity() {
  const [cityTo, setCityTo] = useState('');
  const getCities = useGetCities();
  const { t } = useTranslation('translation', { keyPrefix: 'search' });
  const noOptionsMessage = (target) => (target.inputValue.length > 1 ? (t('error')) : null);

  return (
    <AsyncSelect
      isClearable
      value={cityTo}
      noOptionsMessage={noOptionsMessage}
      loadingMessage={() => t('loading')}
      cacheOptions
      classNamePrefix="react-select"
      loadOptions={getCities}
      placeholder="Одеса"
      onChange={setCityTo}
    />
  );
}
